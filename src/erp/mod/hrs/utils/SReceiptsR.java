/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.utils;

import cfd.DCfdUtils;
import cfd.DElement;
import cfd.ver3.DCfdVer3Utils;
import cfd.ver33.DElementCfdiRelacionados;
import cfd.ver33.DElementConcepto;
import cfd.ver4.DCfdVer4Consts;
import erp.cfd.SCfdConsts;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbFormerPayrollImport;
import erp.mod.hrs.db.SDbPayrollReceipt;
import erp.mod.hrs.db.SDbPayrollReceiptIssue;
import erp.mod.hrs.db.SHrsCfdUtils;
import erp.mod.hrs.db.SHrsFormerPayroll;    
import erp.mod.hrs.db.SHrsFormerReceipt;
import erp.mod.hrs.db.SHrsUtils;
import erp.mtrn.data.SCfdPacket;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.SLibConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;
import sa.lib.srv.SSrvConsts;

/**
 * Esta clase es para usarse una sola vez.
 * Objetivo: corregir la información relacionada con Subsidio para el empleo indebidamente registrado en CFDI previos.
 * @author Edwin Carmona
 */
public class SReceiptsR {
    
    protected SGuiClient miClient;
    
    private final static int CHANGE_CAUSED_SUBSIDY = 8;
    private final static int ADD_OTHER_SUBSIDY = 3;
    
    public final static int CANCEL_RECEIPTS = 1;
    public final static int EMMIT_RECEIPTS = 2;
    
    public final static int SUCCESS = 10;
    public final static int ANNULED = 5;
    public final static int SKIP = 6;
    public final static int NOT_FOUND = 4;
    public final static int NOT_APPLY = 3;
    public final static int ERROR = 1;

    public SReceiptsR(SGuiClient client) {
        this.miClient = client;
    }
    
    /**
     * process the row of SInputData and return the result
     * 
     * @param row
     * @param nAction
     * @return integer:
        SUCCESS
        ANNULED
        SKIP
        NOT_FOUND
        NOT_APPLY
        ERROR
     */
    public int processReceipt(SInputData row, final int nAction) {
        return this.processCfd(row.getUuid(), row.getSubsidy(), row.getTax(), nAction);
    }
    
    /**
     * @param uuid  String with the uuid of cfdi
     * @param subsidy value of subsidy in the file
     * @param tax value of tax in the file
     * @param nAction
     * 
     * @return integer:
        SUCCESS
        ANNULED
        SKIP
        NOT_FOUND
        NOT_APPLY
        ERROR
     */
    public int processCfd(String uuid, double subsidy, double tax, final int nAction) {
        SDataCfd cfd = SReceiptsR.readCfdByUuid(this.miClient, uuid);
        
        if (cfd != null) {
            int r = this.validateCfd(cfd, nAction);
            if (r == SUCCESS) {
                return this.processReceipt(cfd, subsidy, tax, nAction);
            }
            
            return r;
        }
        
        return NOT_FOUND;
    }
    
    /**
     * Validate if cfd received is candidate to continue with the process
     * 
     * @param cfd
     * @return integer:
        SUCCESS if the cfd is candidate to continue
        SKIP if the cfd is previous annuled
        NOT_APPLY if the cfd isn't a cfd of payrol or isn't emited
        ERROR if the received xml is null
     */
    private int validateCfd(SDataCfd cfd, final int nAction) {
        if (cfd == null) {
            return ERROR;
        }
        
        if (nAction == CANCEL_RECEIPTS) {
            return SUCCESS;
        }
        
        if (cfd.getFkPayrollReceiptPayrollId_n() > 0 && !cfd.getUuid().equals("")) {
            if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                return SUCCESS;
            }
            else if(cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                if (this.hasBeenReplaced(cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n(), cfd.getUuid())) {
                    return SKIP;
                }
                
                return SUCCESS;
            }
        }
        
        return NOT_APPLY;
    }
    
    /**
     * check if the cfd and receipt with de uuid received has been
     * replaced with another cfd, searching in the hrs_pay_rcp_iss table
     * 
     * @param uuid
     * @return true if the uuid received is in uuid_rel field
     */
    private boolean hasBeenReplaced(final int payroll, final int emp, String uuid) {
        ResultSet resulReceipts;
        
        try {
            resulReceipts = miClient.getSession().getStatement().
                    getConnection().createStatement().
                    executeQuery("SELECT " +
                        "    * " +
                        "FROM " +
                        "    hrs_pay_rcp_iss hpri " +
                        "        INNER JOIN " +
                        "    trn_cfd tc ON hpri.id_iss = tc.fid_pay_rcp_iss_n " +
                        "        AND hpri.id_pay = tc.fid_pay_rcp_pay_n " +
                        "        AND hpri.id_emp = tc.fid_pay_rcp_emp_n " +
                        "WHERE " +
                        "    id_pay = " + payroll + " AND id_emp = " + emp + " " +
                        "        AND tc.fid_st_xml = 2 " +
                        "        AND uuid_rel = '"+ uuid +"' " +
                        "        AND NOT b_del " +
                        "ORDER BY id_iss DESC LIMIT 1;");
            
            if (resulReceipts.next()) {
                return true;
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SReceiptsR.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }
            
    
    /**
     * annul and issue the cfd
     * 
     * @param cfd
     * @param dSubsidy in file
     * @param dTax in file
     * 
     * @return integer:
        SUCCESS if the cfd was annuled and issued
        ANNULED if the process only was annuled but not issued
        ERROR
     */
    private int processReceipt(SDataCfd cfd, double dSubsidy, double dTax, final int nAction) {
        boolean annuled = false;
        try {
            double dXmlSubsidy = this.getSubsidyFromXml(cfd.getDocXml());
            double dXmlTax = this.getTaxFromXml(cfd.getDocXml());
            
            int iValid = this.validateSubsidyAndTax(dSubsidy, dTax, dXmlSubsidy, dXmlTax);
            
            if (iValid == ERROR) {
                return ERROR;
            }
            
            int receiptKey [] = new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n(), cfd.getFkPayrollReceiptIssueId_n() };
            SDbPayrollReceipt payrollReceipt = new SDbPayrollReceipt();
            payrollReceipt.read(miClient.getSession(), receiptKey);
            SDbPayrollReceiptIssue issue = payrollReceipt.getChildPayrollReceiptIssue();
            int number = SHrsUtils.getPayrollReceiptNextNumber(miClient.getSession(), issue.getNumberSeries());
            
            SDbPayrollReceiptIssue issuen = issue.clone();
            
            writeXml(cfd.getUuid(), cfd.getDocXml());
            System.out.println(cfd.getUuid());
            
            //anular
            if (! this.annulCfd(cfd, issue)) {
                System.out.println("ERROR, NO ANULADO");
            }
            else {
                System.out.println("ANULADO");
            }

            if (nAction == CANCEL_RECEIPTS) {
               return ANNULED;
            }
            
            annuled = true;

            switch (iValid) {
                case CHANGE_CAUSED_SUBSIDY:
                        
                        break;
                case ADD_OTHER_SUBSIDY:
                        dXmlSubsidy = 0.01d;
                        issuen.setPayment_r(issuen.getPayment_r() + dXmlSubsidy);
                        issuen.setUuidRelated(cfd.getUuid());
                        issuen.setDateOfIssue(new Date());
                        break;
            }
            
            //rIssue
            issuen.setPkIssueId(0);
            issuen.setRegistryNew(true);
            issuen.setNumber(number);
            issuen.setFkReceiptStatusId(SDataConstantsSys.TRNS_ST_DPS_EMITED);
            issuen.save(miClient.getSession());
          
            receiptKey[2] = issuen.getPkIssueId();
            
            //timbrar y enviar
            this.computeSignCfdi(miClient.getSession(), receiptKey, dSubsidy, dXmlSubsidy, cfd.getUuid());
            
            return SUCCESS;
        }
        catch (Exception ex) {
            Logger.getLogger(SReceiptsR.class.getName()).log(Level.SEVERE, null, ex);
            if (annuled) {
                return ANNULED;
            }
            
            return ERROR;
        }
    }
    
    /**
     * annul the cfd local and with the SAT
     * 
     * @param cfd
     * @return true if the cfd was annuled
     */
    private boolean annulCfd(SDataCfd cfd, SDbPayrollReceiptIssue issue) {
        boolean annuled = false;
        
        try {
            boolean cancel;
            if (((SClientInterface) miClient).getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticHrs()) {
                cancel = SCfdUtils.cancelAndSendCfdi(((SClientInterface) miClient), cfd, SCfdConsts.CFDI_PAYROLL_VER_CUR, new Date(), true, false, SModSysConsts.TRNU_TP_DPS_ANN_NA, DCfdVer4Consts.CAN_MOTIVO_ERROR_SIN_REL, "", false);
            }
            else {
                cancel = SCfdUtils.cancelCfdi(((SClientInterface) miClient), cfd, SCfdConsts.CFDI_PAYROLL_VER_CUR, new Date(), true, false, SModSysConsts.TRNU_TP_DPS_ANN_NA, DCfdVer4Consts.CAN_MOTIVO_ERROR_SIN_REL, "", false);
            }
            
            if (cancel) {
                issue.setFkReceiptStatusId(SDataConstantsSys.TRNS_ST_DPS_ANNULED);
                issue.save(miClient.getSession());
                
//                cfd.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_ANNULED);
//                cfd.save(miClient.getSession().getStatement().getConnection());

                annuled = true;
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SReceiptsR.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SReceiptsR.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return annuled;
    }
    
    /**
     * get the value of SubsidioCausado from the xml string
     * if the node of subsidy doesn't exist returns 0
     * 
     * @param xml
     * 
     * @return SubsidioCausado="##.##"
     */
    private double getSubsidyFromXml(final String xml) {
        double subsidyXml = 0;
        
        if (xml.contains("SubsidioCausado")) {
            String aux = xml.substring(xml.indexOf("SubsidioCausado"));
            
            int f = aux.indexOf("/>");
            String attSubsidy = aux.substring(0, f);
            String sSubsidy = attSubsidy.substring(attSubsidy.indexOf("\"") + 1, attSubsidy.indexOf("\"", attSubsidy.indexOf("\"") + 1));

            subsidyXml = Double.parseDouble(sSubsidy);
        }
        
        return subsidyXml;
    }
    
     /**
     * get the value of TotalImpuestosRetenidos from the xml string
     * if the node of ISR doesn't exist returns 0
     * 
     * @param xml
     * 
     * @return TotalImpuestosRetenidos="##.##"
     */
    private double getTaxFromXml(final String xml) {
        double taxXml = 0;
        
        if (xml.contains("TotalImpuestosRetenidos")) {
            String isrAux = xml.substring(xml.indexOf("TotalImpuestosRetenidos"));
            String sIsr = isrAux.substring(isrAux.indexOf("\"") + 1, isrAux.indexOf("\"", isrAux.indexOf("\"") + 1));
            
            taxXml = Double.parseDouble(sIsr);
        }
        
        return taxXml;
    }
    
    /**
     * determine the action to do with the xml
     * 
     * @param subsidy of file
     * @param tax of file
     * @param subsidyXml subsidy obtained from xml
     * @param taxXml ISR obtained from xml
     * 
     * @return integer:
     *      ADD_OTHER_SUBSIDY if the node of OtrosPagos must be added
     *      CHANGE_CAUSED_SUBSIDY if the node of OtrosPagos must be changed
     *      ERROR
     */
    private int validateSubsidyAndTax(double subsidy, double tax, double subsidyXml, double taxXml) {
        if (subsidy <= 0 || tax <= 0) {
            return ERROR;
        }
        
        if (subsidy > tax) {
            double subsidyReceived = Double.sum(subsidy, -tax);
            if (Math.abs(subsidyReceived - subsidyXml) <= 0.001d) {
                return CHANGE_CAUSED_SUBSIDY; // se cambia del valor del subsidio en otros pagos
            }
            else {
                return ERROR; // error
            }
        }
        else if (tax > subsidy) {
            double taxReceived = Double.sum(tax, -subsidy);
            if (Math.abs(taxReceived - taxXml) <= 0.001d) {
                return ADD_OTHER_SUBSIDY; // se agrega el subsidio a otros pagos
            }
            else {
                return ERROR; // error
            }
        }
        
        // el impuesto y el subsidio son iguales
        if (subsidyXml == 0 && taxXml == 0) {
            return ADD_OTHER_SUBSIDY; // se agrega el subsidio a otros pagos
        }
            
        return ERROR; // error
    }
    
    /**
     * process the cfd and make the new cfd object and registry
     * sign and send the cfdi based in the configuration of system
     * 
     * @param session
     * @param keyReceipt
     * @param causedSubsidy
     * @param payedSubsidy
     * @param relationedUuid UUID of the cfdi annuled previously
     * 
     * @return true if the process is successful
     * @throws Exception 
     */
    private boolean computeSignCfdi(final SGuiSession session, int[] keyReceipt, double causedSubsidy, double payedSubsidy, String relationedUuid) throws Exception {
        SDataCfd cfd = null;
        SHrsFormerPayroll hrsFormerPayroll = SHrsCfdUtils.readHrsFormerPayrollAndReceipt((SClientInterface) session.getClient(), keyReceipt);
        SHrsFormerReceipt hrsFormerReceipt = hrsFormerPayroll.getChildReceipts().get(0); // there is allways only one receipt
        
        hrsFormerReceipt.setParentPayroll(hrsFormerPayroll);
        hrsFormerReceipt.setFechaEdicion(session.getCurrentDate());
        hrsFormerReceipt.setMoneda(session.getSessionCustom().getLocalCurrencyCode()); 
        hrsFormerReceipt.setLugarExpedicion(((SClientInterface) session.getClient()).getSessionXXX().getCurrentCompanyBranch().getDbmsBizPartnerBranchAddressOfficial().getZipCode());
        hrsFormerReceipt.setRegimenFiscal(((SClientInterface) session.getClient()).getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdRegimenFiscal());
        
        cfd = this.computeCfdi(session, hrsFormerReceipt, keyReceipt[2], true, causedSubsidy, payedSubsidy, relationedUuid);
        if (cfd == null) {
            throw new Exception("Error al leer el CFD, no se encontró el registro.");
        }

        if (((SClientInterface) session.getClient()).getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticHrs()) {
            SCfdUtils.signAndSendCfdi((SClientInterface) session.getClient(), cfd, SCfdConsts.CFDI_PAYROLL_VER_CUR, false, false);
            System.out.println("Timbrar y enviar");
        }
        else {
            SCfdUtils.signCfdi((SClientInterface) session.getClient(), cfd, SCfdConsts.CFDI_PAYROLL_VER_CUR, false, false);
            System.out.println("solo timbrar");
        }
        
        writeXml("REEXP" + relationedUuid, cfd.getDocXml());
        
        return true;
    }
    
    /**
     * 
     * @param session
     * @param receipt
     * @param receiptIssue
     * @param cfdiPendingSigned
     * @param causedSubsidy
     * @param payedSubsidy
     * @param relationedUuid
     * 
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    private SDataCfd computeCfdi(final SGuiSession session, final SHrsFormerReceipt receipt, final int receiptIssue, final boolean cfdiPendingSigned, double causedSubsidy, double payedSubsidy, String relationedUuid) throws Exception {
        boolean add = true;
        int cfdId = SLibConsts.UNDEFINED;
        String docXmlUuid = "";
        
        String sql = "SELECT id_cfd, doc_xml_uuid, fid_st_xml " 
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " "
                + "WHERE fid_pay_rcp_pay_n = " + receipt.getParentPayroll().getPkNominaId() + " AND fid_pay_rcp_emp_n = " + receipt.getPkEmpleadoId() + " AND fid_pay_rcp_iss_n = " + receiptIssue + " "
                + "ORDER BY id_cfd ";

        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            if (resultSet.getInt("fid_st_xml") != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                if (resultSet.getInt("fid_st_xml") == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                    add = !cfdiPendingSigned;
                }
                else {
                    cfdId = resultSet.getInt("id_cfd");
                    docXmlUuid = resultSet.getString("doc_xml_uuid");
                }
            }
        }

        if (add) {
            // generate CFDI:

            cfd.ver32.DElementComprobante comprobanteCfdi32 = null;
            cfd.ver33.DElementComprobante comprobanteCfdi33 = null;
            
            SCfdPacket packet = new SCfdPacket();
            packet.setCfdId(cfdId);
            packet.setIsCfdConsistent(cfdId == SLibConstants.UNDEFINED);
        
            int xmlType = ((SSessionCustom) session.getSessionCustom()).getCfdTypeXmlTypes().get(SDataConstantsSys.TRNS_TP_CFD_PAYROLL);
            float cfdVersion = SLibConsts.UNDEFINED;
            
            switch (xmlType) {
                case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                        comprobanteCfdi32 = (cfd.ver32.DElementComprobante) SCfdUtils.createCfdi32RootElement((SClientInterface) session.getClient(), receipt);
                        cfdVersion = comprobanteCfdi32.getVersion();
                        
                        packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi32));
                        packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW); // after stamping changes to emitted
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                        comprobanteCfdi33 = (cfd.ver33.DElementComprobante) SCfdUtils.createCfdi33RootElement((SClientInterface) session.getClient(), receipt);
                        comprobanteCfdi33 = this.addOtrosPagosSubsidy(comprobanteCfdi33, causedSubsidy, payedSubsidy, relationedUuid);
                        cfdVersion = comprobanteCfdi33.getVersion();
                        comprobanteCfdi33.getAttFecha().setDatetime(new Date());
                        
                        packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi33));
                        packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW); // after stamping changes to emitted
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }
            
            packet.setFkCfdTypeId(SDataConstantsSys.TRNS_TP_CFD_PAYROLL);
            packet.setFkXmlTypeId(xmlType);
            packet.setFkXmlDeliveryTypeId(SModSysConsts.TRNS_TP_XML_DVY_NA);
            packet.setFkXmlDeliveryStatusId(SModSysConsts.TRNS_ST_XML_DVY_PENDING);
            packet.setFkUserDeliveryId(session.getUser().getPkUserId());
            packet.setPayrollReceiptPayrollId(receipt.getParentPayroll().getPkNominaId());
            packet.setPayrollReceiptEmployeeId(receipt.getAuxEmpleadoId());
            packet.setPayrollReceiptIssueId(receiptIssue);
            
            packet.setCfdCertNumber(((SClientInterface) session.getClient()).getCfdSignature(cfdVersion).getCertNumber());
            packet.setCfdSignature(((SClientInterface) session.getClient()).getCfdSignature(cfdVersion).sign(packet.getCfdStringSigned(), SLibTimeUtilities.digestYear(receipt.getParentPayroll().getFecha())[0]));
            packet.setBaseXUuid(docXmlUuid);
            
            switch (xmlType) {
                case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                    comprobanteCfdi32.getAttSello().setString(packet.getCfdSignature());
                    packet.setAuxCfdRootElement(comprobanteCfdi32);
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                    comprobanteCfdi33.getAttSello().setString(packet.getCfdSignature());
                    packet.setAuxCfdRootElement(comprobanteCfdi33);
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }

            ArrayList<SCfdPacket> cfdPackets = new ArrayList<>();
            cfdPackets.add(packet);

            // end of Generate CFDI
            
            SDbFormerPayrollImport payrollImport = new SDbFormerPayrollImport();
            payrollImport.setPayrollId(receipt.getParentPayroll().getPkNominaId());
            payrollImport.setRegenerateNonStampedCfdi(cfdiPendingSigned);
            payrollImport.getCfdPackets().addAll(cfdPackets);
            
            SServerRequest request = new SServerRequest(SServerConstants.REQ_DB_ACTION_SAVE);
            request.setPacket(payrollImport);
            SServerResponse response = ((SClientInterface)session.getClient()).getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                throw new Exception(response.getMessage());
            }
            else {
                if (response.getResultType() != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception("Código de error al emitir el CFD: " + SLibConstants.MSG_ERR_DB_REG_SAVE + ".");
                }
            }
        }
        
        return SCfdUtils.getPayrollReceiptLastCfd((SClientInterface)session.getClient(), SCfdConsts.CFDI_PAYROLL_VER_CUR, new int[] { receipt.getParentPayroll().getPkNominaId(), receipt.getPkEmpleadoId(), receiptIssue });
    }
    
    /**
     * add or change the node OtrosPagos
     * update the fields Total, SubTotal and totalOtrosPagos
     * add the node Cfdis relacionados
     * 
     * @param comprobante
     * @param causedSubsidy
     * @param payedSubsidy
     * @param relationedUuid
     * 
     * @return the object Comprobante modified
     */
    private cfd.ver33.DElementComprobante addOtrosPagosSubsidy(cfd.ver33.DElementComprobante comprobante, double causedSubsidy, double payedSubsidy, String relationedUuid) {
        cfd.ver3.nom12.DElementOtrosPagos otrosPagos = new cfd.ver3.nom12.DElementOtrosPagos();
        cfd.ver3.nom12.DElementOtroPago otroPago;
        
        // crea el elemento otros pagos con los datos del subsidio
        otroPago = new cfd.ver3.nom12.DElementOtroPago();
        otroPago.getAttTipoOtroPago().setString((String) miClient.getSession().readField(SModConsts.HRSS_TP_OTH_PAY, new int[] { SModSysConsts.HRSS_TP_OTH_PAY_TAX_SUB }, SDbRegistry.FIELD_CODE));
        otroPago.getAttClave().setString(DCfdVer3Utils.formatAttributeValueAsKey("012" + ""));
        otroPago.getAttConcepto().setString(SCfdConsts.CFDI_OTHER_PAY_TAX_SUBSIDY_EFF.toUpperCase());
        otroPago.getAttImporte().setDouble(payedSubsidy);

        cfd.ver3.nom12.DElementSubsidioEmpleo subsidioEmpleo = new cfd.ver3.nom12.DElementSubsidioEmpleo();
        subsidioEmpleo.getAttSubsidioCausado().setDouble(causedSubsidy);
        otroPago.setEltSubsidioEmpleo(subsidioEmpleo);
        
        for (DElement element : comprobante.getEltOpcComplemento().getElements()) {
            if (element.getName().compareTo("nomina12:Nomina") == 0) {
                // si la nómina ya tiene el nodo OtrosPagos
                if (((cfd.ver3.nom12.DElementNomina) element).getEltOtrosPagos() != null) {
                    cfd.ver3.nom12.DElementOtroPago aux = null;
                    //Si alguno de los pagos de OtrosPagos es subsidio
                    for (cfd.ver3.nom12.DElementOtroPago op : ((cfd.ver3.nom12.DElementNomina) element).getEltOtrosPagos().getEltHijosOtroPago()) {
                        if(op.getAttClave().getString().equals("012")) {
                            aux = op;
                        }
                    }
                    
                    // se quita el nodo de subsidio anterior
                    if (aux != null) {
                        ((cfd.ver3.nom12.DElementNomina) element).getEltOtrosPagos().getEltHijosOtroPago().remove(aux);
                    }
                    
                    //se reemplaza con el nuevo con los valores correctos
                    ((cfd.ver3.nom12.DElementNomina) element).getEltOtrosPagos().getEltHijosOtroPago().add(otroPago);
                    otrosPagos = ((cfd.ver3.nom12.DElementNomina) element).getEltOtrosPagos();
                }
                // si no tiene OtrosPagos, se agrega este con un hijo subsidio
                else {
                    otrosPagos.getEltHijosOtroPago().add(otroPago);
                    ((cfd.ver3.nom12.DElementNomina) element).setEltOtrosPagos(otrosPagos);
                }
                
                ((cfd.ver3.nom12.DElementNomina) element).getAttTotalOtrosPagos().setDouble(otrosPagos.getTotal());
            }
        }
        
        // se agrega el nodo CfdiRelacionados
        cfd.ver33.DElementCfdiRelacionados cfdiRelacionados = new DElementCfdiRelacionados();
        cfdiRelacionados.getAttTipoRelacion().setString("04");

        cfd.ver33.DElementCfdiRelacionado cfdiRelacionado = new cfd.ver33.DElementCfdiRelacionado();
        cfdiRelacionado.getAttUuid().setString(relationedUuid);
        cfdiRelacionados.getEltCfdiRelacionados().add(cfdiRelacionado);
        
        comprobante.setEltOpcCfdiRelacionados(cfdiRelacionados);
        
        // Si se paga un centavo al empleado se actualizan los montos sumandoles 0.01
        if (payedSubsidy == 0.01d) {
            comprobante.getAttSubTotal().setDouble(comprobante.getAttSubTotal().getDouble() + payedSubsidy);
            comprobante.getAttTotal().setDouble(comprobante.getAttTotal().getDouble() + payedSubsidy);
            
            for (DElementConcepto concepto : comprobante.getEltConceptos().getEltConceptos()) {
                if (concepto.getAttClaveProdServ().getString().equals("84111505")) {
                    concepto.getAttImporte().setDouble(concepto.getAttImporte().getDouble() + payedSubsidy);
                    concepto.getAttValorUnitario().setDouble(concepto.getAttValorUnitario().getDouble() + payedSubsidy);
                }
            }
        }
        
        return comprobante;   
    }
    
    /**
     * read the object SDataCfd from database based in the uuid received
     * 
     * @param client
     * @param uuid
     * 
     * @return the last cfdi with the uuid received
     */
    public static SDataCfd readCfdByUuid(SGuiClient client, String uuid) {
        SDataCfd cfd = null;
        
        try {
            ResultSet resulReceipts = client.getSession().getStatement().
                    getConnection().createStatement().
                    executeQuery("SELECT " +
                                    "id_cfd " +
                                "FROM " +
                                    "trn_cfd " +
                                "WHERE " +
                                    "uuid = '" + uuid + "' " +
                                "ORDER BY id_cfd DESC " +
                                "LIMIT 1;");
            
            if (resulReceipts.next()) {
                int id = resulReceipts.getInt("id_cfd");
                
                cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.TRN_CFD, new int[] { id }, SLibConstants.EXEC_MODE_SILENT);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SReceiptsR.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return cfd;
    }
    
    public void writeXml(String name, String xml) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("reexpedicion/" + name + ".xml"));
        writer.write(xml);

        writer.close();
    }
}

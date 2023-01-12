/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import cfd.DAttributeOptionImpuestoTraslado;
import cfd.DCfdConsts;
import cfd.DCfdUtils;
import cfd.DElement;
import cfd.ver2.DAttributeOptionFormaDePago;
import cfd.ver3.DCfdVer3Consts;
import cfd.ver33.DCfdi33Catalogs;
import cfd.ver33.DCfdi33Consts;
import cfd.ver33.DElementCfdiRelacionados;
import cfd.ver33.DElementImpuestos;
import cfd.ver40.DCfdi40Catalogs;
import cfd.ver40.DElementInformacionGlobal;
import com.finkok.facturacion.cancel.CancelSOAP;
import com.finkok.stamp.AcuseRecepcionCFDI;
import com.finkok.stamp.Incidencia;
import com.finkok.stamp.IncidenciaArray;
import com.finkok.stamp.StampSOAP;
import erp.SClient;
import erp.SClientUtils;
import erp.cfd.SCfdConsts;
import erp.cfd.SCfdDataBizPartner;
import erp.cfd.SCfdDataConcepto;
import erp.cfd.SCfdDataImpuesto;
import erp.cfd.SCfdXmlCfdi32;
import erp.cfd.SCfdXmlCfdi33;
import erp.cfd.SCfdXmlCfdi40;
import erp.cfd.SCfdiSignature;
import erp.cfd.SDbCfdBizPartner;
import erp.cfd.SDialogCfdProcessing;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerAddressee;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mcfg.data.SDataCertificate;
import erp.mcfg.data.SDataParamsCompany;
import erp.mfin.data.SDataTax;
import erp.mfin.data.diot.SDiotConsts;
import erp.mhrs.data.SDataPayrollReceiptIssue;
import erp.mloc.data.SLocUtils;
import erp.mmkt.data.SDataCustomerBranchConfig;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbPayroll;
import erp.mod.hrs.db.SHrsFormerConceptExtraTime;
import erp.mod.hrs.db.SHrsFormerConceptIncident;
import erp.mod.hrs.db.SHrsFormerPayroll;
import erp.mod.hrs.db.SHrsFormerReceipt;
import erp.mod.hrs.db.SHrsFormerReceiptConcept;
import erp.mod.log.db.SDbBillOfLading;
import erp.mtrn.form.SDialogRestoreCfdi;
import erp.print.SDataConstantsPrint;
import erp.redis.SLockUtils;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.awt.Cursor;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import javax.mail.MessagingException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiSession;
import sa.lib.mail.SMailException;
import sa.lib.srv.SLock;
import sa.lib.srv.SSrvConsts;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author Juan Barajas, Edwin Carmona, Alfredo Pérez, Claudio Peña, Sergio Flores, Isabel Servín, Sergio Flores
 * 
 * Maintenance Log:
 * 2018-01-02, Sergio Flores:
 *  Implementation of payroll into CFDI 3.3.
 */
public abstract class SCfdUtils implements Serializable {

    private static ArrayList<SCfdPacket> CfdPackets;
    private static int SignAndCancelLogEntryId;
    
    private static final String TXT_SEND = "Enviar CFDI";
    private static final String TXT_SIGN_SEND = "Timbrar y enviar CFDI";
    
    public static final String TXT_SEND_DPS = "Enviar documento";
    
    /** Constant for be used in DataSet as "key" to identify the CFD-massive-processing case (i.e, the "value"): stamping or printing. */
    public static final int KEY_CFD = 900000;
    /** For identifying a CFD-massive-processing case of stamping. When a CFD is being stamped, it must be recovered from storage. */
    public static final int KEY_CFD_STAMPING = 900001;
    /** For identifying any CFD-massive-processing case different from stamping. When a CFD is being processed, for instance, when being printed, it can be recovered from memory, when available. */
    public static final int KEY_CFD_PROCESSING = 900002;
    /** For identifying a shared issuer (in fact the very curent company in user session) on a CFD-massive-processing case of stamping. */
    public static final int KEY_CFD_ISSUER = 900100;
    
    public static final HashMap<Integer, Object> DataSet = new HashMap<>();
    
    /*
     * Private static methods:
     */
    
    @SuppressWarnings("deprecation")
    private static boolean canCfdiCancelWebService(final SClientInterface client, final SDataCfd cfd, final int pacId) throws Exception {
        SDataCfdPacType cfdPacType = null;
        SDataPac pac = null;
        String certSAT = "";
        String msg = "";
        boolean can = true;

        if (cfd.isStamped()) {
            switch (cfd.getFkXmlTypeId()) {
                case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                    cfd.ver32.DElementComprobante comprobante32 = DCfdUtils.getCfdi32(cfd.getDocXml());
                    cfd.ver32.DElementTimbreFiscalDigital tft32 = comprobante32.getEltOpcComplementoTimbreFiscalDigital();
                    if (tft32 != null) {
                        certSAT = tft32.getAttNoCertificadoSAT().getString();
                    }
                    break;
                    
                case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                    cfd.ver33.DElementComprobante comprobante33 = DCfdUtils.getCfdi33(cfd.getDocXml());
                    cfd.ver33.DElementTimbreFiscalDigital tft33 = comprobante33.getEltOpcComplementoTimbreFiscalDigital();
                    if (tft33 != null) {
                        certSAT = tft33.getAttNoCertificadoSAT().getString();
                    }
                    break;
                    
                case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                    cfd.ver40.DElementComprobante comprobante40 = DCfdUtils.getCfdi40(cfd.getDocXml());
                    cfd.ver40.DElementTimbreFiscalDigital tft40 = comprobante40.getEltOpcComplementoTimbreFiscalDigital();
                    if (tft40 != null) {
                        certSAT = tft40.getAttNoCertificadoSAT().getString();
                    }
                    break;
                    
                default:
                    // to nothing
            }

            cfdPacType = getCfdPacType(client, cfd.getFkCfdTypeId()); 

            if (cfdPacType != null) {
                pac = (SDataPac) SDataUtilities.readRegistry(client, SDataConstants.TRN_PAC, new int[] { pacId != SLibConstants.UNDEFINED ? pacId : cfdPacType.getFkPacId() }, SLibConstants.EXEC_MODE_SILENT);

                if (!pac.getIsAnnulmentEnabled()) {
                    msg = "La cancelación del CFDI ante el SAT mediante el PAC '" + pac.getPac() + "' no está habilitada en el sistema,\n pero puede cancelarlo manualmente en el portal del SAT.";
                }
                else if (pacId == SLibConstants.UNDEFINED && cfdPacType.getIsAnnulmentCertNumberEnabled() && !cfdPacType.getPacCertNumber().isEmpty() && certSAT.compareTo(cfdPacType.getPacCertNumber()) != 0) {
                    msg = "La cancelación del CFDI ante el SAT mediante el PAC '" + pac.getPac() + "' no se puede realizar en el sistema porque no fue timbrado por éste PAC,\n pero puede cancelarlo manualmente en el portal del SAT.";
                }
            }

            if (!msg.isEmpty()) {
                can = false;

                if (client.showMsgBoxConfirm(msg + " Se cancelará solo en el sistema.\n" +
                        SGuiConsts.MSG_CNF_CONT) == JOptionPane.NO_OPTION) {
                    throw new Exception(msg);
                }
            }
        }
        else {
            can = false;
        }

        return can;
    }

    private static boolean canCfdiCancel(final SClientInterface client, final SDataCfd cfd, final boolean isProcessingValidation) throws Exception {
        SDataCfd cfdAux = cfd;

        cfdAux.setAuxIsSign(true);
        cfdAux.setAuxIsProcessingValidation(isProcessingValidation);
        SServerRequest request = new SServerRequest(SServerConstants.REQ_DB_CAN_ANNUL, cfdAux);
        SServerResponse response = client.getSessionXXX().request(request);

        if (response.getResultType() != SLibConstants.DB_CAN_ANNUL_YES) {
            throw new Exception(response.getMessage());
        }

        return true;
    }

    private static boolean canCfdiSign(final SClientInterface client, final SDataCfd cfd, final boolean isProcessingValidation) throws Exception {
        SDataCfd cfdAux = cfd;
        boolean can = true;

        cfdAux.setAuxIsSign(true);
        cfdAux.setAuxIsProcessingValidation(isProcessingValidation);
        SServerRequest request = new SServerRequest(SServerConstants.REQ_DB_CAN_SAVE, cfdAux);
        SServerResponse response = client.getSessionXXX().request(request);

        if (response.getResultType() != SLibConstants.DB_CAN_SAVE_YES) {
            throw new Exception(response.getMessage());
        }
        else {
            if (!cfd.getIsConsistent()) {
                throw new Exception("CFD inconsistente.");
            }
            else if (!existsPacConfiguration(client, cfd)) {
                throw new Exception("No existe ningún PAC configurado para este tipo de CFDI.");
            }
            else if (isNeedStamps(client, cfd, SDbConsts.ACTION_SAVE, 0) && getStampsAvailable(client, cfd.getFkCfdTypeId(), cfd.getTimestamp(), 0) <= 0) {
                throw new Exception("No existen timbres disponibles.");
            }
            else if (!isProcessingValidation) {
                if (SLibTimeUtils.convertToDateOnly(cfd.getTimestamp()).after(client.getSessionXXX().getSystemDate())) {
                    can = client.showMsgBoxConfirm("La fecha del comprobante " +
                            "(" + SLibUtils.DateFormatDate.format(cfd.getTimestamp()) + ") es posterior a la fecha del sistema " +
                            "(" + SLibUtils.DateFormatDate.format(client.getSessionXXX().getSystemDate()) + ").\n" +
                            "¿Está seguro que desea timbrar el comprobante?") == JOptionPane.YES_OPTION;
                }
                else {
                    int[] today = SLibTimeUtils.digestDate(client.getSessionXXX().getSystemDate());
                    GregorianCalendar now = new GregorianCalendar();
                    GregorianCalendar limit = new GregorianCalendar(today[0], today[1] - 1, today[2], now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND));   // mix ofsystem's date + current time
                    
                    if (SLibTimeUtilities.getDaysDiff(limit.getTime(), cfd.getTimestamp()) > SCfdConsts.CFDI_STAMP_DELAY_DAYS) {
                        throw new Exception("La fecha-hora del comprobante " +
                                "(" + SLibUtils.DateFormatDatetime.format(cfd.getTimestamp()) + ") es anterior a la fecha-hora del sistema por más de " + SCfdConsts.CFDI_STAMP_DELAY_DAYS + " días " +
                                "(" + SLibUtils.DateFormatDatetime.format(limit.getTime()) + ").\n" +
                                "No se puede timbrar el comprobante.");
                    }
                }
            }
        }

        return can;
    }

    private static boolean canObtainCfdXml(final SDataCfd cfd) throws Exception {
        if (cfd.isOwnCfd() && !cfd.isCfd() && !cfd.isCfdi()) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nTipo de comprobante desconocido.");
        }
        else if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_NEW) {
            throw new Exception("El comprobante no está emitido.");
        }
        else if (cfd.isCfdi()) {
            if (!cfd.isStamped()) {
                throw new Exception("El CFDI no está timbrado.");
            }
            else if (cfd.getIsProcessingWebService()) {
                throw new Exception(SCfdConsts.ERR_MSG_PROCESS_WS_PAC);
            }
            else if (cfd.getIsProcessingStorageXml()) {
                throw new Exception(SCfdConsts.ERR_MSG_PROCESS_XML_STORAGE);
            }
        }

        return true;
    }

    private static boolean canObtainCfdCancelAck(final SClientInterface client, final SDataCfd cfd) throws Exception {
        if (!cfd.isCfd() && !cfd.isCfdi()) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nTipo de comprobante desconocido.");
        }
        else {
            if (cfd.isCfd()) {
                // CFD:
                throw new Exception("Los CFD no tienen acuse de cancelación.");
            }
            else {
                // CFDI:
                if (cfd.getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                    throw new Exception("El CFDI no está cancelado.");
                }
                else if (!cfd.isStamped()) {
                    throw new Exception("El CFDI no está timbrado.");
                }
                else if (cfd.getIsProcessingWebService()) {
                    throw new Exception(SCfdConsts.ERR_MSG_PROCESS_WS_PAC);
                }
                else if (cfd.getIsProcessingStorageXml()) {
                    throw new Exception(SCfdConsts.ERR_MSG_PROCESS_XML_STORAGE);
                }
                else if (cfd.getIsProcessingStoragePdf()) {
                    throw new Exception(SCfdConsts.ERR_MSG_PROCESS_PDF_STORAGE);
                }
                else if (cfd.getAcknowledgmentCancellationXml().isEmpty() && getCancelAckPdf(client, cfd) == null) {
                    throw new Exception("El CFDI no tiene acuse de cancelación.");
                }
            }
        }

        return true;
    }

    /**
     * Check if CFD can be printed.
     * @param cfd
     * @param isCfdStorageInProcess Tell if CFD storage is in process.
     * @return
     * @throws Exception 
     */
    private static boolean canPrintCfd(final SDataCfd cfd, final boolean isCfdStorageInProcess) throws Exception {
        if (!isCfdStorageInProcess) {
            if (cfd.getIsProcessingWebService()) {
                throw new Exception(SCfdConsts.ERR_MSG_PROCESS_WS_PAC);
            }
        }

        return true;
    }

    private static boolean canSendCfd(final SDataCfd cfd) throws Exception {
        return cfd.isOwnCfd() && canObtainCfdXml(cfd);
    }
    
    private static boolean areCfdInconsistent(final ArrayList<SDataCfd> cfds) throws Exception {
        if (cfds != null) {
            if (cfds.isEmpty()) {
                return false;
            }
            else {
                for (SDataCfd cfd : cfds) {
                    if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED && !cfd.getIsConsistent()) {
                        throw new Exception("Existen CFDI emitidos e inconsistentes, para la nómina '" + cfd.getFkPayrollPayrollId_n() + "'.");
                    }
                }
            }
        }

        return true;
    }

    /**
     * Update sign & cancel log entry.
     * @param client
     * @param message
     * @param actionCode
     * @param stepCode
     * @param cfdId
     * @param pacId
     * @throws Exception 
     */
    private static void createSignCancelLogEntry(final SClientInterface client, final String message, final int actionCode, final int stepCode, final int cfdId, final int pacId) throws Exception {
        createSignCancelLogEntry(client, message, actionCode, stepCode, cfdId, pacId, "", "");
    }

    /**
     * Update sign & cancel log entry.
     * @param client
     * @param message
     * @param actionCode
     * @param stepCode
     * @param cfdId
     * @param pacId
     * @param annulReason
     * @param annulRelatedUuid
     * @throws Exception 
     */
    private static void createSignCancelLogEntry(final SClientInterface client, final String message, final int actionCode, final int stepCode, final int cfdId, final int pacId, final String annulReason, final String annulRelatedUuid) throws Exception {
        SDataCfdSignLog cfdSignLog = new SDataCfdSignLog();
        
        cfdSignLog.setPkLogId(SignAndCancelLogEntryId);

        if (SignAndCancelLogEntryId == 0) {
            cfdSignLog.setDate(client.getSession().getCurrentDate());
            cfdSignLog.setAnnulReasonCode(annulReason);
            cfdSignLog.setAnnulRelatedUuid(annulRelatedUuid);
            cfdSignLog.setIsSystem(pacId == 0);
            cfdSignLog.setIsDeleted(false);
            cfdSignLog.setFkCfdId(cfdId);
            cfdSignLog.setFkPacId_n(pacId);
            cfdSignLog.setFkUserId(client.getSession().getUser().getPkUserId());
        }
        
        cfdSignLog.setCodeAction(actionCode);
        cfdSignLog.setCodeStep(stepCode);

        if (!message.isEmpty()) {
            SDataCfdSignLogMsg cfdSignLogMsg = new SDataCfdSignLogMsg();

            cfdSignLogMsg.setMessage(message);

            cfdSignLog.setDbmsDataCfdSignLogMsg(cfdSignLogMsg);
        }

        if (cfdSignLog.save(client.getSession().getDatabase().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
        }
        else {
            SignAndCancelLogEntryId = cfdSignLog.getPkLogId();
        }
    }

    private static boolean saveCancelAckPdf(final SClientInterface client, final SDataCfd cfd, FileInputStream cancelAckPdfStream) throws Exception {
        String sql = "";
        PreparedStatement preparedStatement = null;
        byte[] byteArray =null;
        byte[] buffer =null;
        int read = -1;

        sql = "UPDATE " + SClientUtils.getComplementaryDdName(client) + ".trn_cfd SET ack_can_pdf_n = ? " +
                "WHERE id_cfd = " + cfd.getPkCfdId() + " ";

        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        buffer = new byte[500];

        while ((read = cancelAckPdfStream.read(buffer)) > 0) {
            byteArrayOS.write(buffer, 0, read);
        }
        cancelAckPdfStream.close();

        byteArray = byteArrayOS.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);

        preparedStatement = client.getSession().getDatabase().getConnection().prepareStatement(sql);

        preparedStatement.setBlob(1, bais);
        preparedStatement.execute();

        return true;
    }

    @SuppressWarnings("deprecation")
    private static void validateHrsFormerReceipts(SClientInterface client, SHrsFormerPayroll hrsFormerPayroll, SHrsFormerPayroll hrsFormerPayrollXml, final boolean isRegenerateOnlyNonStampedCfdi) throws Exception {
        // Validate payroll receipts consistent:

        if (hrsFormerPayroll == null) {
            throw new NullPointerException("Payroll is null.");
        }
        else {
            CfdPackets = new ArrayList<>();

            for (SHrsFormerReceipt receipt: hrsFormerPayroll.getChildReceipts()) {
                boolean isFound = false;
                boolean isConsistent = false;
                boolean add = true;
                int cfdId = SLibConsts.UNDEFINED;
                String docXmlUuid = "";

                if (hrsFormerPayrollXml != null) {
                    for (SHrsFormerReceipt receiptXml : hrsFormerPayrollXml.getChildReceipts()) {
                        if (receipt.getAuxEmpleadoId() == receiptXml.getAuxEmpleadoId() && receipt.getPkEmpleadoId() == receiptXml.getPkEmpleadoId()) {
                            isFound = true;
                            isConsistent = validateHrsFormerReceiptConcepts(receipt, receiptXml) && receipt.getBanco() == receiptXml.getBanco() &&
                                    SLibTimeUtils.convertToDateOnly(receipt.getParentPayroll().getFecha()).compareTo(SLibTimeUtils.convertToDateOnly(receiptXml.getParentPayroll().getFecha())) == 0 &&
                                    receipt.getCuentaBancaria().compareTo(receiptXml.getCuentaBancaria()) == 0 && receipt.getTipoContrato().compareTo(receiptXml.getTipoContrato()) == 0 &&
                                    receipt.getFechaPago().compareTo(receiptXml.getFechaPago()) == 0 && receipt.getFechaFinalPago().compareTo(receiptXml.getFechaFinalPago()) == 0 &&
                                    receipt.getFechaInicialPago().compareTo(receiptXml.getFechaInicialPago()) == 0 && receipt.getFechaInicioRelLaboral().compareTo(receiptXml.getFechaInicioRelLaboral()) == 0 &&
                                    receipt.getNumDiasPagados() == receiptXml.getNumDiasPagados() && receipt.getEmpleadoCurp().compareTo(receiptXml.getEmpleadoCurp()) == 0 &&
                                    receipt.getTipoRegimen() == receiptXml.getTipoRegimen() && receipt.getNumSeguridadSocial().compareTo(receiptXml.getNumSeguridadSocial()) == 0 &&
                                    receipt.getEmpleadoNum().compareTo(receiptXml.getEmpleadoNum()) == 0 && receipt.getRegistroPatronal().compareTo(receiptXml.getRegistroPatronal()) == 0 &&
                                    receipt.getPuesto().compareTo(receiptXml.getPuesto()) == 0 && receipt.getRiesgoPuesto() == receiptXml.getRiesgoPuesto() &&
                                    receipt.getDepartamento().compareTo(receiptXml.getDepartamento()) == 0 && receipt.getPeriodicidadPago().compareTo(receiptXml.getPeriodicidadPago()) == 0 &&
                                    receipt.getSalarioBaseCotApor() == receiptXml.getSalarioBaseCotApor() && receipt.getSalarioDiarioIntegrado() == receiptXml.getSalarioDiarioIntegrado() &&
                                    receipt.getAntiguedad() == receiptXml.getAntiguedad() && receipt.getTotalDeducciones() == receiptXml.getTotalDeducciones() &&
                                    receipt.getTotalNeto() == receiptXml.getTotalNeto() && receipt.getTotalPercepciones() == receiptXml.getTotalPercepciones() &&
                                    receipt.getTotalRetenciones() == receiptXml.getTotalRetenciones() && receipt.getTipoJornada().compareTo(receiptXml.getTipoJornada()) == 0 &&
                                    (SLibTimeUtils.convertToDateOnly(receipt.getParentPayroll().getFecha()).compareTo(SLibTimeUtils.convertToDateOnly(SLibTimeUtils.createDate(2016, 7, 15))) > 0 ?
                                    erp.mod.hrs.db.SHrsFormerUtils.getPaymentMethodCode(client, receipt.getMetodoPago()).compareTo(receiptXml.getMetodoPagoAux()) == 0 : 
                                    erp.mod.hrs.db.SHrsFormerUtils.getPaymentMethodName(client, receipt.getMetodoPago()).compareTo(receiptXml.getMetodoPagoAux()) == 0);
                        }

                        if (isFound) {
                            String sql = "SELECT id_cfd, doc_xml_uuid, fid_st_xml " +
                                    "FROM trn_cfd WHERE fid_pay_pay_n = " + hrsFormerPayroll.getPkNominaId() + " AND fid_pay_emp_n = " + receipt.getAuxEmpleadoId() +
                                    " AND fid_pay_bpr_n = " + receipt.getPkEmpleadoId() + " ORDER BY id_cfd ";

                            ResultSet resultSet = client.getSession().getStatement().executeQuery(sql);
                            while (resultSet.next()) {
                                if (resultSet.getInt("fid_st_xml") != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                                    if (resultSet.getInt("fid_st_xml") == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                                        add = !isRegenerateOnlyNonStampedCfdi;
                                        cfdId = isConsistent ? resultSet.getInt("id_cfd") : SLibConsts.UNDEFINED;
                                    }
                                    else {
                                        if (cfdId == SLibConsts.UNDEFINED) {
                                            cfdId = resultSet.getInt("id_cfd");
                                            docXmlUuid = resultSet.getString("doc_xml_uuid");
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }

                if (add) {
                    // Add missing fields to receipt:
                    
                    receipt.setParentPayroll(hrsFormerPayroll);
                    receipt.setMoneda(client.getSession().getSessionCustom().getLocalCurrencyCode());
                    receipt.setLugarExpedicion(client.getSessionXXX().getCurrentCompanyBranch().getDbmsBizPartnerBranchAddressOfficial().getZipCode());
                    receipt.setRegimenFiscal(client.getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdRegimenFiscal());
                    //payrollReceipt.setConfirmacion(""); XXX WTF!
                    //payrollReceipt.setCfdiRelacionadosTipoRelacion(""); XXX WTF!
                    
                    // Generate CFDI:

                    cfd.ver32.DElementComprobante comprobanteCfdi32 = null;
                    cfd.ver33.DElementComprobante comprobanteCfdi33 = null;
                    cfd.ver40.DElementComprobante comprobanteCfdi40 = null;
                    
                    SCfdPacket packet = new SCfdPacket();
                    packet.setCfdId(cfdId);
                    packet.setIsCfdConsistent(!isFound || cfdId == SLibConsts.UNDEFINED ? true: isConsistent);
                    
                    int xmlType = ((SSessionCustom) client.getSession().getSessionCustom()).getCfdTypeXmlTypes().get(SDataConstantsSys.TRNS_TP_CFD_PAYROLL);
                    float cfdVersion = SLibConsts.UNDEFINED;
                    
                    switch (xmlType) {
                        case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                            comprobanteCfdi32 = (cfd.ver32.DElementComprobante) createCfdi32RootElement(client, receipt);
                            cfdVersion = comprobanteCfdi32.getVersion();
                            
                            packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi32));
                            packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW); // after stamping changes to emitted
                            break;
                        case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                            comprobanteCfdi33 = (cfd.ver33.DElementComprobante) createCfdi33RootElement(client, receipt);
                            cfdVersion = comprobanteCfdi33.getVersion();
                            
                            packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi33));
                            packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW); // after stamping changes to emitted
                            break;
                        case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                            comprobanteCfdi40 = (cfd.ver40.DElementComprobante) createCfdi40RootElement(client, receipt);
                            cfdVersion = comprobanteCfdi40.getVersion();
                            
                            packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi40));
                            packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW); // after stamping changes to emitted
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
                    }
                    
                    packet.setFkCfdTypeId(SDataConstantsSys.TRNS_TP_CFD_PAYROLL);
                    packet.setFkXmlTypeId(xmlType);
                    packet.setFkXmlDeliveryTypeId(SModSysConsts.TRNS_TP_XML_DVY_NA);
                    packet.setFkXmlDeliveryStatusId(SModSysConsts.TRNS_ST_XML_DVY_PENDING);
                    packet.setFkUserDeliveryId(client.getSession().getUser().getPkUserId());
                    packet.setPayrollPayrollId(hrsFormerPayroll.getPkNominaId());
                    packet.setPayrollEmployeeId(receipt.getAuxEmpleadoId());
                    packet.setPayrollBizPartnerId(receipt.getPkEmpleadoId());
                    
                    packet.setCfdCertNumber(client.getCfdSignature(cfdVersion).getCertNumber());
                    packet.setCfdSignature(client.getCfdSignature(cfdVersion).sign(packet.getCfdStringSigned(), SLibTimeUtilities.digestYear(hrsFormerPayroll.getFecha())[0]));
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
                        case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                            comprobanteCfdi40.getAttSello().setString(packet.getCfdSignature());
                            packet.setAuxCfdRootElement(comprobanteCfdi40);
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
                    }

                    CfdPackets.add(packet);
                }
            }
        }

        if (!isRegenerateOnlyNonStampedCfdi && hrsFormerPayroll.getChildReceipts().size() != CfdPackets.size()) {
            throw new Exception("No se generó el CFDI para todos los recibos de la nómina '" + hrsFormerPayroll.getPkNominaId() + "'.");
        }
    }

    private static boolean validateHrsFormerReceiptConcepts(SHrsFormerReceipt hrsFormerReceipt, SHrsFormerReceipt hrsFormerReceiptXml) {
       boolean consistent = true;
       
       ArrayList<SHrsFormerReceiptConcept> hrsFormerReceiptConceptsXml = hrsFormerReceiptXml.getChildConcepts();
       
       if (hrsFormerReceipt.getChildConcepts().size() != hrsFormerReceiptConceptsXml.size()) {
           consistent = false;
       }
       else {
            for (SHrsFormerReceiptConcept concept: hrsFormerReceipt.getChildConcepts()) {
                boolean found = false;
                int i = 0;
                consistent = true;

                for (SHrsFormerReceiptConcept conceptXml: hrsFormerReceiptConceptsXml) {
                    if (concept.getPkTipoConcepto() == conceptXml.getPkTipoConcepto() && concept.getPkSubtipoConcepto() == conceptXml.getPkSubtipoConcepto() &&
                            concept.getClaveOficial() == conceptXml.getClaveOficial() && concept.getClaveEmpresa().compareTo(conceptXml.getClaveEmpresa()) == 0 && !conceptXml.isAuxFound()) {
                        found = true;
                        consistent = validateHrsFormerReceiptIncidents(concept, conceptXml) &&
                                concept.getConcepto().compareTo(conceptXml.getConcepto()) == 0 &&
                                concept.getTotalGravado() == conceptXml.getTotalGravado() && concept.getTotalExento() == conceptXml.getTotalExento() &&
                                (SLibUtils.belongsTo(concept.getPkSubtipoConcepto(), new int[] { SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1], SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_TRIPLE[1] }) ?
                                validateHrsFormerReceiptExtraTimes(concept.getChildExtraTime(), conceptXml.getChildExtraTime()) : true);
                    }

                    if (found && consistent) {
                        hrsFormerReceiptConceptsXml.get(i).setAuxFound(found);
                        break;
                    }
                    i++;
                }

                if (!found) {
                    consistent = false;
                }

                if (!consistent) {
                    break;
                }
            }
       }

        return consistent;
    }

    private static boolean validateHrsFormerReceiptIncidents(SHrsFormerReceiptConcept concept, SHrsFormerReceiptConcept conceptXml) {
       boolean isConsistent = true;
       boolean isFound = false;

        for (SHrsFormerConceptIncident incident: concept.getChildIncidents()) {
            isFound = false;
            isConsistent = true;

            for (SHrsFormerConceptIncident incidentXml: conceptXml.getChildIncidents()) {
                if (incident.getPkTipo() == incidentXml.getPkTipo() && incident.getPkSubtipo() == incidentXml.getPkSubtipo()) {
                    isFound = true;
                    isConsistent = incident.getFecha().compareTo(incidentXml.getFecha()) == 0 && incident.getFechaInicial().compareTo(incidentXml.getFechaInicial()) == 0 &&
                            incident.getFechaFinal().compareTo(incidentXml.getFechaFinal()) == 0;
                }

                if (isFound) {
                    break;
                }
            }

            if (!isConsistent) {
                break;
            }
        }

        return isConsistent;
    }

    private static boolean validateHrsFormerReceiptExtraTimes(SHrsFormerConceptExtraTime extraTime, SHrsFormerConceptExtraTime extraTimeXml) {
       return (extraTime.getDias() == extraTimeXml.getDias() && extraTime.getTipoHoras().compareTo(extraTimeXml.getTipoHoras()) == 0 &&
               extraTime.getHorasExtra() == extraTimeXml.getHorasExtra() && SLibUtils.round(extraTime.getImportePagado(), SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()) == extraTimeXml.getImportePagado());
    }

    private static boolean managementCfdi(final SClientInterface client, final SDataCfd dataCfd, final int newXmlStatusId, final Date date, final boolean isSingle, final boolean isValidation, final int pacId, final int payrollCfdVersion, int annulType, String annulReason, String annulRelatedUuid, boolean retryCancel) throws Exception {
        return managementCfdi(client, dataCfd, newXmlStatusId, date, isSingle, isValidation, pacId, payrollCfdVersion, annulType, annulReason, annulRelatedUuid, retryCancel, "", false);
    }
    
    private static boolean managementCfdi(final SClientInterface client, final SDataCfd dataCfd, final int newXmlStatusId, final Date date, final boolean isSingle, final boolean isValidation, final int pacId, final int payrollCfdVersion, int annulType, String annulReason, String annulRelatedUuid, boolean retryCancel, final String xmlSigned, final boolean isUpdateAckPdf) throws Exception {
        int[] registryKey = null;
        SDataDps dataDps = null;
        SDataCfdPayment dataCfdPayment = null;
        SDataPayrollReceiptIssue dataPayrollReceiptIssue = null;
        SDbBillOfLading bol = null;
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
        SSrvLock lock = null;
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        SRedisLock rlock = null;
        */
        SLock slock = null;
        SCfdiSignature cfdiSignature = null;
        String xmlStamping = "";
        String xmlAckCancellation = "";
        String warningMessage = "";
        boolean consumeStamp = true;
        boolean next = true;

        try {
            client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            
            switch (dataCfd.getFkCfdTypeId()) {
                case SDataConstantsSys.TRNS_TP_CFD_INV:
                    registryKey = new int[] { dataCfd.getFkDpsYearId_n(), dataCfd.getFkDpsDocId_n() };
                    /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                    lock = SSrvUtils.gainLock(client.getSession(), client.getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.TRN_DPS, registryKey, 1000 * 60); // 1 minute timeout
                    */
                    /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                    rlock = SRedisLockUtils.gainLock(client, SDataConstants.TRN_DPS, registryKey, 60);
                    */
                    slock = SLockUtils.gainLock(client, SDataConstants.TRN_DPS, registryKey, 1000 * 60);
                    
                    break;
                    
                case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                    registryKey = new int[] { dataCfd.getPkCfdId() };
                    /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                    lock = SSrvUtils.gainLock(client.getSession(), client.getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.TRNX_CFD_PAY_REC, registryKey, 1000 * 60); // 1 minute timeout
                    */
                    /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                    rlock = SRedisLockUtils.gainLock(client, SDataConstants.TRNX_CFD_PAY_REC, registryKey, 60);
                    */
                    slock = SLockUtils.gainLock(client, SDataConstants.TRNX_CFD_PAY_REC, registryKey, 1000 * 60);
                    
                    break;
                    
                case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                    if (payrollCfdVersion == SCfdConsts.CFDI_PAYROLL_VER_CUR) {
                        registryKey = new int[] { dataCfd.getFkPayrollReceiptPayrollId_n(), dataCfd.getFkPayrollReceiptEmployeeId_n(), dataCfd.getFkPayrollReceiptIssueId_n() };
                        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                        lock = SSrvUtils.gainLock(client.getSession(), client.getSessionXXX().getCompany().getPkCompanyId(), SModConsts.HRS_PAY_RCP_ISS, registryKey, 1000 * 60); // 1 minute timeout
                        */
                        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                        rlock = SRedisLockUtils.gainLock(client, SModConsts.HRS_PAY_RCP_ISS, registryKey, 60);                    
                        */
                        slock = SLockUtils.gainLock(client, SModConsts.HRS_PAY_RCP_ISS, registryKey, 1000 * 60);
                    }
                    break;
                    
                case SDataConstantsSys.TRNS_TP_CFD_BOL:
                    registryKey = new int[] { dataCfd.getFkBillOfLadingId_n() } ;
                    /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                    lock = SSrvUtils.gainLock(client.getSession(), client.getSessionXXX().getCompany().getPkCompanyId(), SModConsts.LOG_BOL, registryKey, 1000 * 60);
                    */
                    /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                    rlock = SRedisLockUtils.gainLock(client, SModConsts.LOG_BOL, registryKey, 60);
                    */
                    slock = SLockUtils.gainLock(client, SModConsts.LOG_BOL, registryKey, 1000 * 60);
                    break;
                default:
            }
            
            if (newXmlStatusId == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                // cancel CFDI:
                
                if (!isUpdateAckPdf) {
                    xmlAckCancellation = cancel(client, dataCfd, date, isValidation, pacId, annulReason, annulRelatedUuid, retryCancel);

                    if (xmlAckCancellation.isEmpty()) {
                        next = pacId == 0;
                    }
                }

                if (!isUpdateAckPdf) {
                    consumeStamp = !xmlAckCancellation.isEmpty();
                }
                else {
                    consumeStamp = dataCfd.getIsProcessingWebService();
                }

                cfdiSignature = extractCfdiSignature(dataCfd.getDocXml());
            }
            else {
                // sign CFDI:
                
                if (xmlSigned.length() == 0) {
                    xmlStamping = sign(client, dataCfd, isValidation);
                }
                else {
                    xmlStamping = xmlSigned;
                }

                cfdiSignature = extractCfdiSignature(xmlStamping);
            }

            if (next) {
                next = false;

                SDataCfdPacType cfdPacType = getCfdPacType(client, dataCfd.getFkCfdTypeId());

                if (cfdPacType == null) {
                    throw new Exception("No existe ninguna configuración para el tipo de CFD.");
                }
                else {
                    if (cfdiSignature == null || cfdiSignature.getUuid().isEmpty()) {
                        throw new Exception("¡El comprobante no ha sido " + (newXmlStatusId == SDataConstantsSys.TRNS_ST_DPS_ANNULED ? "cancelado" : "timbrado") + "!");
                    }
                    else {
                        SCfdPacket packet = new SCfdPacket();
                        
                        packet.setCfdId(dataCfd.getPkCfdId());
                        packet.setCfdSeries(dataCfd.getSeries());
                        packet.setCfdNumber(dataCfd.getNumber());
                        packet.setCfdCertNumber(dataCfd.getCertNumber());
                        packet.setCfdStringSigned(dataCfd.getStringSigned());
                        packet.setCfdSignature(dataCfd.getSignature());
                        packet.setBaseXUuid(dataCfd.getBaseXUuid());
                        
                        if (newXmlStatusId == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                            packet.setDocXml(dataCfd.getDocXml());
                        }
                        else {
                            packet.setDocXml(composeCfdWithTfdTimbreFiscalDigital(client, dataCfd, xmlStamping));
                        }
                        
                        packet.setDocXmlName(dataCfd.getDocXmlName());
                        packet.setXmlDate(dataCfd.getTimestamp());
                        packet.setXmlRfcEmisor(cfdiSignature.getRfcEmisor());
                        packet.setXmlRfcReceptor(cfdiSignature.getRfcReceptor());
                        packet.setXmlTotalCy(cfdiSignature.getTotalCy());
                        packet.setCfdUuid(cfdiSignature.getUuid());
                        packet.setCancellationStatus(dataCfd.getCancellationStatus());
                        packet.setAcknowledgmentCancellationXml(xmlAckCancellation.isEmpty() ? dataCfd.getAcknowledgmentCancellationXml() : xmlAckCancellation);
                        packet.setFkCfdTypeId(dataCfd.getFkCfdTypeId());
                        packet.setFkXmlTypeId(dataCfd.getFkXmlTypeId());
                        packet.setFkXmlStatusId(newXmlStatusId);
                        packet.setFkXmlDeliveryTypeId(dataCfd.getFkXmlDeliveryTypeId());
                        packet.setFkXmlDeliveryStatusId(dataCfd.getFkXmlDeliveryStatusId());
                        packet.setFkUserDeliveryId(client.getSession().getUser().getPkUserId());
                        packet.setFkCompanyBranchId(dataCfd.getFkCompanyBranchId_n());
                        packet.setFkFactoringBankId(dataCfd.getFkFactoringBankId_n());
                        packet.setDpsYearId(dataCfd.getFkDpsYearId_n());
                        packet.setDpsDocId(dataCfd.getFkDpsDocId_n());
                        //packet.setRecordEntryYearId(...);
                        //packet.setRecordEntryPeriodId(...);
                        //packet.setRecordEntryBookkeepingCenterId(...);
                        //packet.setRecordEntryRecordTypeId(...);
                        //packet.setRecordEntryNumberId(...);
                        //packet.setRecordEntryEntryId(...);
                        packet.setPayrollPayrollId(dataCfd.getFkPayrollPayrollId_n());
                        packet.setPayrollEmployeeId(dataCfd.getFkPayrollEmployeeId_n());
                        packet.setPayrollBizPartnerId(dataCfd.getFkPayrollBizPartnerId_n());
                        packet.setPayrollReceiptPayrollId(dataCfd.getFkPayrollReceiptPayrollId_n());
                        packet.setPayrollReceiptEmployeeId(dataCfd.getFkPayrollReceiptEmployeeId_n());
                        packet.setPayrollReceiptIssueId(dataCfd.getFkPayrollReceiptIssueId_n());
                        packet.setReceiptPaymentId(dataCfd.getFkReceiptPaymentId_n());
                        packet.setBillOfLadingId(dataCfd.getFkBillOfLadingId_n());
                        
                        packet.setAuxAnnulType(annulType);

                        if (dataCfd.getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                            switch (dataCfd.getFkCfdTypeId()) {
                                case SDataConstantsSys.TRNS_TP_CFD_INV:
                                    dataDps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, registryKey, SLibConstants.EXEC_MODE_SILENT);
                                    
                                    dataDps.setAuxIsProcessingValidation(isValidation);
                                    
                                    packet.setAuxDataDps(dataDps);
                                    break;

                                case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                                    /*
                                    XXX 2022-02-01, Sergio Flores.
                                    Código preservado para referencias futuras.
                                    Se comentó porque antes de leer el comprobante es necesario indicar que no se desea leer los renglones de las pólizas contables relacionadas con él.
                                    
                                    dataCfdPayment = (SDataCfdPayment) SDataUtilities.readRegistry(client, SDataConstants.TRNX_CFD_PAY_REC, registryKey, SLibConstants.EXEC_MODE_SILENT);
                                    */
                                    
                                    dataCfdPayment = new SDataCfdPayment();
                                    dataCfdPayment.setAuxIsProcessingCfdi(true);
                                    dataCfdPayment.read(registryKey, client.getSession().getStatement());
                                    
                                    dataCfdPayment.setAuxIsProcessingCfdiValidation(isValidation);
                                    
                                    packet.setAuxDataCfdPayment(dataCfdPayment);
                                    break;

                                case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                                    if (payrollCfdVersion == SCfdConsts.CFDI_PAYROLL_VER_CUR) {
                                        dataPayrollReceiptIssue = new SDataPayrollReceiptIssue();
                                        if (dataPayrollReceiptIssue.read(registryKey, client.getSession().getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                                        }
                                        
                                        packet.setAuxDataPayrollReceiptIssue(dataPayrollReceiptIssue);
                                    }
                                    break;
                                case SDataConstantsSys.TRNS_TP_CFD_BOL:
                                    bol = new SDbBillOfLading();
                                    bol.read(client.getSession(), registryKey);
                                    
                                    packet.setAuxDataBillOfLading(bol);
                                default:
                            }
                        }
                        
                        SDataPac pac = (SDataPac) SDataUtilities.readRegistry(client, SDataConstants.TRN_PAC, new int[] { pacId != SDataConstants.UNDEFINED ? pacId : cfdPacType.getFkPacId() }, SLibConstants.EXEC_MODE_SILENT);
                        
                        packet.setAuxPacId(pac.getPkPacId());
                        packet.setAuxSignAndCancelLogEntryId(SignAndCancelLogEntryId);
                        packet.setAuxStampQuantity(newXmlStatusId == SDataConstantsSys.TRNS_ST_DPS_EMITED ? 1 : (pac.getIsPrepayment() && pac.getIsChargedAnnulment() ? 1 : 0));
                        packet.setAuxStampConsume(consumeStamp);

                        // Sign & Cancel Log step #5
                        createSignCancelLogEntry(client, "", newXmlStatusId == SDataConstantsSys.TRNS_ST_DPS_EMITED ? 
                                (!isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN) :
                                (!isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL), 
                                SCfdConsts.STEP_CODE_CFD_SAVED, dataCfd.getPkCfdId(), pacId == 0 ? pacId : pac.getPkPacId());

                        saveCfd(client, packet);

                        next = true;

                        if (isSingle) {
                            warningMessage = verifyCertificateExpiration(client);
                            
                            if (newXmlStatusId == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                                client.showMsgBoxInformation("El comprobante ha sido anulado correctamente." + (!isNeedStamps(client, dataCfd, SDbConsts.ACTION_ANNUL, pacId == 0 ? pacId : pac.getPkPacId()) || !consumeStamp ? "" :
                                                "\nTimbres disponibles: " + getStampsAvailable(client, dataCfd.getFkCfdTypeId(), dataCfd.getTimestamp(), pacId == 0 ? pacId : pac.getPkPacId()) + "." + (warningMessage.isEmpty() ? "" : "\n" + warningMessage)));
                            }
                            else {
                                client.showMsgBoxInformation("El comprobante ha sido timbrado correctamente. " + (!isNeedStamps(client, dataCfd, SDbConsts.ACTION_SAVE, pacId == 0 ? pacId : pac.getPkPacId()) ? "" :
                                                "\nTimbres disponibles: " + getStampsAvailable(client, dataCfd.getFkCfdTypeId(), dataCfd.getTimestamp(), pacId == 0 ? pacId : pac.getPkPacId()) + "." + (warningMessage.isEmpty() ? "" : "\n" + warningMessage)));
                            }
                        }

                        // read again CFD for printing:
                        // XXX NOTE: 2018-02-24, Sergio Flores: Check why is read again the CFD registry:
                        
                        SDataCfd cfdForPrinting = (SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, dataCfd.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                        printCfd(client, cfdForPrinting, payrollCfdVersion, SDataConstantsPrint.PRINT_MODE_PDF_FILE, 1, true);

                        // set flag PDF as correct:

                        cfdForPrinting.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_PRC_STO_PDF, false);
                        cfdForPrinting.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_PRC_USR, client.getSession().getUser().getPkUserId());
                    }
                }
            }
        }
        catch (Exception e) {
            /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
            if (lock != null) {
                SSrvUtils.releaseLock(client.getSession(), lock);
            }
            */
            /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
            if (rlock != null) {
                SRedisLockUtils.releaseLock(client, rlock);
            }
            */
            if (slock != null) {
                SLockUtils.releaseLock(client, slock);
            }
            throw e;
        }
        finally {
            /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
            if (lock != null) {
                SSrvUtils.releaseLock(client.getSession(), lock);
            }
            */
            /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
            if (rlock != null) {
                SRedisLockUtils.releaseLock(client, rlock);
            }            
            */
            if (slock != null) {
                SLockUtils.releaseLock(client, slock);
            }
            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        return next;
    }

    private static boolean existsPacConfiguration(final SClientInterface client, final SDataCfd cfd) {
        boolean exists = false;

        try {
            SDataCfdPacType cfdPacType = getCfdPacType(client, cfd.getFkCfdTypeId());
            exists = (cfdPacType != null && !cfdPacType.getIsDeleted());
        }
        catch (Exception e) {
            SLibUtils.showException(SCfdUtils.class.getName(), e);
        }

        return exists;
    }

    private static int getCfdSigningPacId(final SClientInterface client, final SDataCfd cfd) {
        SDataCfdPacType cfdPacType = null;
        String sql = "";
        ResultSet resultSet = null;
        int pacId = 0;
        int pacIdAux = 0;

        try {
            cfdPacType = getCfdPacType(client, cfd.getFkCfdTypeId());

            sql = "SELECT id_pac " +
                    "FROM trn_sign " +
                    "WHERE b_del = 0 AND fid_ct_sign = " + SDataConstantsSys.TRNS_TP_SIGN_OUT_EMITED[0] + " AND fid_tp_sign = " + SDataConstantsSys.TRNS_TP_SIGN_OUT_EMITED[1] + " AND fid_cfd_n = " + cfd.getPkCfdId() + " ";

            resultSet = client.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                pacIdAux = resultSet.getInt("id_pac");
                if (pacIdAux != cfdPacType.getFkPacId()) {
                    pacId = pacIdAux;
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(SCfdUtils.class.getName(), e);
        }

        return pacId;
    }

    private static boolean stampedCfdiFinkok(final SClientInterface client, final SDataCfd cfd, final int cfdiPayrollVersion) throws Exception {
        boolean signed = false;

        if (canCfdiSign(client, cfd, true)) {
            managementCfdi(client, cfd, SDataConstantsSys.TRNS_ST_DPS_EMITED, null, true, true, 0, cfdiPayrollVersion, 0, "", "", false);
            signed = true;
        }
        
        return signed;
    }

    private static boolean cancelCfdiFinkok(final SClientInterface client, final SDataCfd cfd, final int cfdiPayrollVersion, final int annulType, final String annulReason, final String annulRelatedUuid, final boolean retryCancel) throws Exception {
        boolean canceled = false;
        int pacId = 0;

        pacId = getCfdSigningPacId(client, cfd);

        if (canCfdiCancel(client, cfd, true)) {
            if (canCfdiCancelWebService(client, cfd, pacId)) {
                managementCfdi(client, cfd, SDataConstantsSys.TRNS_ST_DPS_ANNULED, client.getSessionXXX().getSystemDate(), true, true, pacId, cfdiPayrollVersion, annulType, annulReason, annulRelatedUuid, retryCancel);
            }
            else {
                processAnnul(client, cfd, cfdiPayrollVersion, annulType);
            }
            canceled = true;
        }
        return canceled;
    }

    /**
     * Sign CFD.
     * @param client ERP Client interface.
     * @param cfd Object SDataCfd type.
     * @return Signed CFD in XML format.
     * @throws TransformerConfigurationException, TransformerException, Exception
     */
    private static String sign(final SClientInterface client, final SDataCfd cfd, boolean isValidation) throws TransformerConfigurationException, TransformerException, Exception {
        String xml = "";
        SDataPac pac = null;
        SDataCfdPacType cfdPacType = getCfdPacType(client, cfd.getFkCfdTypeId());

        if (isValidation) {
            // sign validation:
            pac = getPacForValidation(client, cfd);
        }
        else {
            // sign:
            if (cfdPacType != null) {
                pac = (SDataPac) SDataUtilities.readRegistry(client, SDataConstants.TRN_PAC, new int[] { cfdPacType.getFkPacId() }, SLibConstants.EXEC_MODE_SILENT);
            }
        }

        if (pac == null) {
            throw new Exception("Error al leer el catálogo de PAC's.\n"
                    + "No existe ningún PAC registrado para el timbrado de CFDI.");
        }
        else {
            String userName = pac.getUser();
            String userPswd = pac.getUserPassword();

            /* Code used in fiscal stamp testing: (DO NOT DELETE! KEEPED JUST FOR REFERENCE!)
            XmlProcess xmlProcess = null;
            oInputFile = new BufferedInputStream(new FileInputStream(sXmlBaseDir + cfd.getDocXmlName()));
            xmlProcess = new XmlProcess(oInputFile);
            sCfdi = xmlProcess.generaTextoXML();
            */

            String sCfdi = removeNode(cfd.getDocXml(), "cfdi:Addenda");    // production code for fiscal stamp
            int actionCode = !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN;

            if (client.getSessionXXX().getParamsCompany().getIsCfdiProduction()) {
                // CFDI signing production environment:
                
                switch (pac.getPkPacId()) {
                    case SModSysConsts.TRN_PAC_FCG:
                        /* This case is obsolete.
                         * It do not implements new CFDI signature and cancellation valid since 2018.
                         * (Sergio Flores, 2018-11-06)
                         */
                        forsedi.timbrado.WSForcogsaService fcgService;
                        forsedi.timbrado.WSForcogsa fcgPort;
                        forsedi.timbrado.WsAutenticarResponse autenticarResponse;
                        forsedi.timbrado.WsTimbradoResponse timbradoResponse;

                        fcgService = new forsedi.timbrado.WSForcogsaService();
                        fcgPort = fcgService.getWSForcogsaPort();

                        // Web Service Autentication:

                        // Sign & Cancel Log step #1
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_PAC_AUTH, cfd.getPkCfdId(), pac.getPkPacId());

                        autenticarResponse = fcgPort.autenticar(userName, userPswd);

                        if (autenticarResponse.getMensaje() != null) {
                            // Close current Sign & Cancel Log entry with error status:
                            createSignCancelLogEntry(client, "WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]", actionCode, SCfdConsts.STEP_CODE_PAC_AUTH, cfd.getPkCfdId(), pac.getPkPacId());

                            System.err.println("WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]");
                            throw new Exception("Error al autenticarse con el PAC:\nMensaje: " + autenticarResponse.getMensaje());
                        }

                        if (autenticarResponse.getToken() == null) {
                            // Close current Sign & Cancel Log entry with error status:
                            createSignCancelLogEntry(client, "WsAutenticarResponse Token is null!", actionCode, SCfdConsts.STEP_CODE_PAC_AUTH, cfd.getPkCfdId(), pac.getPkPacId());

                            System.err.println("WsAutenticarResponse Token is null!");
                            throw new Exception("Error al autenticarse con el PAC:\n¡Token nulo!");
                        }

                        // Document stamp:

                        // Sign & Cancel Log step #2
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_FLAGS_SET, cfd.getPkCfdId(), pac.getPkPacId());

                        updateCfdProcessingFlags(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_SEND_RECV, cfd.getPkCfdId(), pac.getPkPacId());

                        timbradoResponse = fcgPort.timbrar(sCfdi, autenticarResponse.getToken());

                        if (timbradoResponse.getMensaje() != null) {
                            // Sign & Cancel Log step #4
                            createSignCancelLogEntry(client, "WsTimbradoResponse Mensaje: [" + timbradoResponse.getMensaje() + "]", actionCode, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd.getPkCfdId(), pac.getPkPacId());

                            updateCfdProcessingFlags(client, cfd, false);
                            System.err.println("WsTimbradoResponse Codigo: [" + timbradoResponse.getCodigo() + "]");
                            System.err.println("WsTimbradoResponse Mensaje: [" + timbradoResponse.getMensaje() + "]");
                            System.err.println("Cfdi: [" + sCfdi + "]");
                            throw new Exception("Error al timbrar el comprobante:\nCódigo: " + timbradoResponse.getCodigo() + "\nMensaje: " + timbradoResponse.getMensaje());
                        }

                        xml = timbradoResponse.getCfdi();
                        break;

                    case SModSysConsts.TRN_PAC_FNK:
                        // Document stamp:

                        // Sign & Cancel Log step #1, not required in Finkok, preserved for consistence!
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_PAC_AUTH, cfd.getPkCfdId(), pac.getPkPacId());
                        
                        // Sign & Cancel Log step #2
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_FLAGS_SET, cfd.getPkCfdId(), pac.getPkPacId());

                        updateCfdProcessingFlags(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_SEND_RECV, cfd.getPkCfdId(), pac.getPkPacId());

                        AcuseRecepcionCFDI acuseRecepcionCFDI;
                        StampSOAP service = new StampSOAP();
                        com.finkok.stamp.Application port = service.getApplication();
                        
                        if (isValidation) {
                            acuseRecepcionCFDI = port.stamped(sCfdi.getBytes("UTF-8"), userName, userPswd);
                        }
                        else {
                            acuseRecepcionCFDI = port.stamp(sCfdi.getBytes("UTF-8"), userName, userPswd);
                        }

                        String sMessageException = "";
                        JAXBElement<IncidenciaArray> maoIncidencias = acuseRecepcionCFDI.getIncidencias();

                        if (!maoIncidencias.getValue().getIncidencia().isEmpty()) {
                            if (isValidation) {
                                for (Incidencia i : maoIncidencias.getValue().getIncidencia()) {
                                    if (i.getCodigoError().getValue().compareTo(SCfdConsts.UUID_STAMP_NOT_YET) == 0) {
                                        updateCfdProcessingFlags(client, cfd, false);
                                    }
                                    sMessageException += "\nCódigo: " + i.getCodigoError().getValue() + "\nMensaje: " + i.getMensajeIncidencia().getValue();
                                }
                            }
                            else {
                                updateCfdProcessingFlags(client, cfd, false);
                                for (Incidencia i : maoIncidencias.getValue().getIncidencia()) {
                                    System.err.println("WsAcuseRecepcionCFDI Codigo: [" + i.getCodigoError().getValue() + "]");
                                    System.err.println("WsAcuseRecepcionCFDI Mensaje: [" + i.getMensajeIncidencia().getValue() + "]");
                                    sMessageException += "\nCódigo: " + i.getCodigoError().getValue() + "\nMensaje: " + i.getMensajeIncidencia().getValue();

                                    if (i.getCodigoError().getValue().compareTo(SCfdConsts.UUID_STAMP_ALREADY) == 0) {
                                        updateCfdProcessingFlags(client, cfd, true);
                                    }
                                }
                            }
                            
                            // Sign & Cancel Log step #4
                            createSignCancelLogEntry(client, sMessageException, actionCode, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd.getPkCfdId(), pac.getPkPacId());

                            System.err.println("Cfdi: [" + sCfdi + "]");
                            throw new Exception("Error al timbrar el comprobante: " + sMessageException);
                        }

                        xml = acuseRecepcionCFDI.getXml().getValue();
                        break;
                        
                    default:
                }
            }
            else {
                // CFDI signing testing environment:
                
                switch (pac.getPkPacId()) {
                    case SModSysConsts.TRN_PAC_FCG:
                        /* This case is obsolete.
                         * It do not implements new CFDI signature and cancellation valid since 2018.
                         * (Sergio Flores, 2018-11-06)
                         */
                        com.wscliente.WSForcogsaService fcgService = null;
                        com.wscliente.WSForcogsa fcgPort = null;
                        com.wscliente.WsAutenticarResponse autenticarResponse = null;
                        com.wscliente.WsTimbradoResponse timbradoResponse = null;

                        fcgService = new com.wscliente.WSForcogsaService();
                        fcgPort = fcgService.getWSForcogsaPort();

                        // Web Service Autentication:

                        // Sign & Cancel Log step #1
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_PAC_AUTH, cfd.getPkCfdId(), pac.getPkPacId());

                        autenticarResponse = fcgPort.autenticar("pruebasWS", "pruebasWS");

                        if (autenticarResponse.getMensaje() != null) {
                            // Close current Sign & Cancel Log entry with error status:
                            createSignCancelLogEntry(client, "WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]", actionCode, SCfdConsts.STEP_CODE_PAC_AUTH, cfd.getPkCfdId(), pac.getPkPacId());

                            System.err.println("WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]");
                            throw new Exception("Error al autenticarse con el PAC:\nMensaje: " + autenticarResponse.getMensaje());
                        }

                        if (autenticarResponse.getToken() == null) {
                            // Close current Sign & Cancel Log entry with error status:
                            createSignCancelLogEntry(client, "WsAutenticarResponse Token is null!", actionCode, SCfdConsts.STEP_CODE_PAC_AUTH, cfd.getPkCfdId(), pac.getPkPacId());

                            System.err.println("WsAutenticarResponse Token is null!");
                            throw new Exception("Error al autenticarse con el PAC:\n¡Token nulo!");
                        }

                        // Document stamp:

                        // Sign & Cancel Log step #2
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_FLAGS_SET, cfd.getPkCfdId(), pac.getPkPacId());

                        updateCfdProcessingFlags(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_SEND_RECV, cfd.getPkCfdId(), pac.getPkPacId());

                        timbradoResponse = fcgPort.timbrar(sCfdi, autenticarResponse.getToken());

                        if (timbradoResponse.getMensaje() != null) {
                            // Sign & Cancel Log step #4
                            createSignCancelLogEntry(client, "WsTimbradoResponse Mensaje: [" + timbradoResponse.getMensaje() + "]", actionCode, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd.getPkCfdId(), pac.getPkPacId());

                            updateCfdProcessingFlags(client, cfd, false);
                            System.err.println("WsTimbradoResponse Codigo: [" + timbradoResponse.getCodigo() + "]");
                            System.err.println("WsTimbradoResponse Mensaje: [" + timbradoResponse.getMensaje() + "]");
                            System.err.println("Cfdi: [" + sCfdi + "]");
                            throw new Exception("Error al timbrar el comprobante:\nCódigo: " + timbradoResponse.getCodigo() + "\nMensaje: " + timbradoResponse.getMensaje());
                        }

                        xml = timbradoResponse.getCfdi();
                        break;

                    case SModSysConsts.TRN_PAC_FNK:
                        // NOTE 1: these are not really clasess of a testing environment, but production ones!
                        // NOTE 2: there is a set of testing-environment clasess in library ClientWSFinkok2018, but they are not implemented here yet.
                        
                        // Document stamp:

                        // Sign & Cancel Log step #1, not required in Finkok, preserved for consistence!
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_PAC_AUTH, cfd.getPkCfdId(), pac.getPkPacId());
                        
                        // Sign & Cancel Log step #2
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_FLAGS_SET, cfd.getPkCfdId(), pac.getPkPacId());

                        updateCfdProcessingFlags(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_SEND_RECV, cfd.getPkCfdId(), pac.getPkPacId());

                        /* 2018-08-17, Sergio Flores: Code snippet commented for preventing connection with PAC and subsequent exception due to inactive account:
                        AcuseRecepcionCFDI acuseRecepcionCFDI;
                        StampSOAP service = new StampSOAP();
                        com.finkok.facturacion.stamp.Application port = service.getApplication();

                        if (isProcessingValidation) {
                            acuseRecepcionCFDI = port.stamped(sCfdi.getBytes("UTF-8"), sCfdiUser, sCfdiUserPassword);
                        }
                        else {
                            acuseRecepcionCFDI = port.stamp(sCfdi.getBytes("UTF-8"), sCfdiUser, sCfdiUserPassword);
                        }

                        String sMessageException = "";
                        JAXBElement<IncidenciaArray> maoIncidencias = acuseRecepcionCFDI.getIncidencias();

                        if (!maoIncidencias.getValue().getIncidencia().isEmpty()) {
                            if (isProcessingValidation) {
                                for (Incidencia i : maoIncidencias.getValue().getIncidencia()) {
                                    if (i.getCodigoError().getValue().compareTo(SCfdConsts.UUID_STAMP_PREV_NOT) == 0) {
                                        updateProcessCfd(client, cfd, false);
                                    }
                                    sMessageException += "\nCódigo: " + i.getCodigoError().getValue() + "\nMensaje: " + i.getMensajeIncidencia().getValue();
                                }
                            }
                            else {
                                updateProcessCfd(client, cfd, false);
                                for (Incidencia i : maoIncidencias.getValue().getIncidencia()) {
                                    System.err.println("WsAcuseRecepcionCFDI Codigo: [" + i.getCodigoError().getValue() + "]");
                                    System.err.println("WsAcuseRecepcionCFDI Mensaje: [" + i.getMensajeIncidencia().getValue() + "]");
                                    sMessageException += "\nCódigo: " + i.getCodigoError().getValue() + "\nMensaje: " + i.getMensajeIncidencia().getValue();

                                    if (i.getCodigoError().getValue().compareTo(SCfdConsts.UUID_STAMP_PREV) == 0) {
                                        updateProcessCfd(client, cfd, true);
                                    }
                                }
                            }
                            
                            // Sign & Cancel Log step #4
                            createSignCancelLogEntry(client, sMessageException, actionCode, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd.getPkCfdId(), pac.getPkPacId());

                            System.err.println("Cfdi: [" + sCfdi + "]");
                            throw new Exception("Error al timbrar el comprobante: " + sMessageException);
                        }

                        xml = acuseRecepcionCFDI.getXml().getValue();
                        */
                        
                        // scamp code snippet to emulate a stamped CFDI:
                        xml = sCfdi;
                        
                        break;
                        
                    default:
                }
            }
        }

        return xml;
    }

    /**
     * Cancel CFD.
     * @param client ERP Client interface.
     * @param sUuid Universally unique identifier to cancel.
     * @param date Date of cancelation.
     * @throws Exception
     */
    private static String cancel(final SClientInterface client, final SDataCfd cfd, final Date date, boolean isValidation, final int pacId, String annulReason, String annulRelatedUuid, boolean retryCancel) throws Exception {
        String xmlAcuse = "";
        SDataPac pac = null;

        if (isValidation) {
            // cancel validation:
            pac = getPacForValidation(client, cfd);
        }
        else {
            // cancel:
            SDataCfdPacType cfdPacType = getCfdPacType(client, cfd.getFkCfdTypeId());
            
            if (pacId != 0 || cfdPacType != null) {
                pac = (SDataPac) SDataUtilities.readRegistry(client, SDataConstants.TRN_PAC, new int[] { pacId != 0 ? pacId : cfdPacType.getFkPacId() }, SLibConstants.EXEC_MODE_SILENT);
            }
        }

        if (pac == null) {
            throw new Exception("Error al leer el catálogo de PAC's."
                    + "\nNo existe ningún PAC registrado para la cancelación de CFDI.");
        }
        else {
            boolean next = true;
            String message = "";
            int actionCode = !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL;
            String fiscalIdIssuer = client.getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId();
            String userName = pac.getUser();
            String userPswd = pac.getUserPassword();
            SDataCertificate companyCertificate = client.getSessionXXX().getParamsCompany().getDbmsDataCertificate_n();
            
            ArrayList<String> uuids = new ArrayList<>();
            uuids.add(cfd.getUuid());

            if (client.getSessionXXX().getParamsCompany().getIsCfdiProduction()) {
                // CFDI cancelling production environment:
                
                switch (pac.getPkPacId()) {
                    case SModSysConsts.TRN_PAC_FCG:
                        /* This case is obsolete.
                         * It do not implements new CFDI signature and cancellation valid since 2018.
                         * (Sergio Flores, 2018-11-06)
                         */
                        forsedi.timbrado.WSForcogsaService fcgService = null;
                        forsedi.timbrado.WSForcogsa fcgPort = null;
                        forsedi.timbrado.WsAutenticarResponse autenticarResponse = null;
                        forsedi.timbrado.WsCancelacionResponse canceladoResponse = null;

                        fcgService = new forsedi.timbrado.WSForcogsaService();
                        fcgPort = fcgService.getWSForcogsaPort();

                        // Web Service Autentication:

                        // Sign & Cancel Log step #1
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_PAC_AUTH, cfd.getPkCfdId(), pac.getPkPacId());

                        autenticarResponse = fcgPort.autenticar(userName, userPswd);

                        if (autenticarResponse.getMensaje() != null) {
                            message = "Error de autentificación con el PAC:"
                                    + "\nMensaje: [" + autenticarResponse.getMensaje() + "]";
                            
                            // Close current Sign & Cancel Log entry with error status:
                            createSignCancelLogEntry(client, message.replaceAll("\n", " "), actionCode, SCfdConsts.STEP_CODE_PAC_AUTH, cfd.getPkCfdId(), pac.getPkPacId());

                            if (pacId == 0) {
                                System.err.println(message.replaceAll("\n", " "));
                                throw new Exception(message);
                            }
                            
                            next = false;
                        }

                        if (autenticarResponse.getToken() == null) {
                            message = "Error de autentificación con el PAC:"
                                    + "\n¡El token de autentificación es nulo!";
                            
                            // Close current Sign & Cancel Log entry with error status:
                            createSignCancelLogEntry(client, message.replaceAll("\n", " "), actionCode, SCfdConsts.STEP_CODE_PAC_AUTH, cfd.getPkCfdId(), pac.getPkPacId());

                            if (pacId == 0) {
                                System.err.println(message.replaceAll("\n", " "));
                                throw new Exception(message);
                            }
                            
                            next = false;
                        }

                        if (next) {
                            // Document cancel:

                            // Sign & Cancel Log step #2
                            createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_FLAGS_SET, cfd.getPkCfdId(), pac.getPkPacId());

                            updateCfdProcessingFlags(client, cfd, true);

                            // Sign & Cancel Log step #3
                            createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_SEND_RECV, cfd.getPkCfdId(), pac.getPkPacId());

                            canceladoResponse = fcgPort.cancelacion1(fiscalIdIssuer, SLibUtils.DbmsDateFormatDate.format(date), uuids, companyCertificate.getExtraPublicKeyBytes_n(), companyCertificate.getExtraPrivateKeyBytes_n(), "", autenticarResponse.getToken());

                            if (canceladoResponse.getMensaje() != null) {
                                message = "Error al cancelar el CFDI:"
                                        + "\nCódigo estatus: [" + canceladoResponse.getCodEstatus() + "]"
                                        + "\nMensaje: [" + canceladoResponse.getMensaje() + "]"
                                        + "\nUUID: [" + uuids + "]";
                                
                                // Sign & Cancel Log step #4
                                createSignCancelLogEntry(client, message.replaceAll("\n", " "), actionCode, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd.getPkCfdId(), pac.getPkPacId());

                                updateCfdProcessingFlags(client, cfd, false);

                                if (pacId == 0) {
                                    System.err.println(message.replaceAll("\n", " "));
                                    throw new Exception(message);
                                }
                                
                                next = false;
                            }

                            if (next) {
                                forsedi.timbrado.WsFoliosResponse foliosResponse = canceladoResponse.getFolios();

                                if (foliosResponse.getFolio().get(0).getEstatusUUID().compareTo(SCfdConsts.UUID_CANCEL_OK) != 0) {
                                    if (foliosResponse.getFolio().get(0).getEstatusUUID().compareTo(SCfdConsts.UUID_CANCEL_ALREADY) != 0) {
                                        message = "Error al cancelar el CFDI:"
                                                + "\nMensaje: [" + foliosResponse.getFolio().get(0).getMensaje() + "]";
                                        
                                        // Sign & Cancel Log step #4
                                        createSignCancelLogEntry(client, message.replaceAll("\n", " "), actionCode, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd.getPkCfdId(), pac.getPkPacId());

                                        updateCfdProcessingFlags(client, cfd, false);

                                        if (pacId == 0) {
                                            System.err.println(message.replaceAll("\n", " "));
                                            throw new Exception(message);
                                        }
                                        
                                        next = false;
                                    }
                                }
                                else {
                                    xmlAcuse = canceladoResponse.getAcuse();
                                }
                            }
                        }
                        break;

                    case SModSysConsts.TRN_PAC_FNK:
                        // Document cancel:

                        // Sign & Cancel Log step #1, not required in Finkok, preserved for consistence!
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_PAC_AUTH, cfd.getPkCfdId(), pac.getPkPacId());
                        
                        // Sign & Cancel Log step #2
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_FLAGS_SET, cfd.getPkCfdId(), pac.getPkPacId());

                        updateCfdProcessingFlags(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_SEND_RECV, cfd.getPkCfdId(), pac.getPkPacId());
                        
                        // Check CFDI cancellable status:
                        
                        boolean getAckCancellation = false;
                        boolean isDirectlyCancellable = false;
                        SCfdUtilsHandler cfdUtilsHandler = new SCfdUtilsHandler(client);
                        SCfdUtilsHandler.CfdiAckQuery cfdiAckQuery = cfdUtilsHandler.getCfdiSatStatus(cfd);
                        String cancelStatusCode = "";
                        
                        switch (cfdiAckQuery.CfdiStatus) {
                            case DCfdi33Consts.CFDI_ESTATUS_CAN:
                                // CFDI is 'cancelled' before fiscal authority, but is still active in system:
                                
                                getAckCancellation = true;
                                break;

                            case DCfdi33Consts.CFDI_ESTATUS_VIG:
                                // CFDI is 'active' before fiscal authority:
                                
                                if (isValidation) {
                                    // Sign & Cancel Log step #6
                                    createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_PAC_FLAG_CLEAR, cfd.getPkCfdId(), pac.getPkPacId());
                                    
                                    updateCfdProcessingFlags(client, cfd, false);

                                    if (cfdiAckQuery.CancelStatus.isEmpty()) {
                                        cancelStatusCode = "";
                                    }
                                    else {
                                        switch (cfdiAckQuery.CancelStatus) {
                                            case DCfdi33Consts.ESTATUS_CANCEL_PROC: // cancellation in process
                                                cancelStatusCode = DCfdi33Consts.ESTATUS_CANCEL_PROC_CODE;
                                                break;
                                            case DCfdi33Consts.ESTATUS_CANCEL_RECH: // cancellation was rejected
                                                cancelStatusCode = DCfdi33Consts.ESTATUS_CANCEL_RECH_CODE;
                                                break;
                                            case DCfdi33Consts.ESTATUS_CANCEL_NINGUNO: // cancellation in pending buffer
                                                cancelStatusCode = DCfdi33Consts.ESTATUS_CANCEL_PEND_BUFF_CODE;
                                                break;
                                            default:
                                                cancelStatusCode = "???";
                                        }
                                    }
                                    
                                    cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, cancelStatusCode);
                                    client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_CFD);
                                    throw new Exception("El CFDI está vigente." + (cancelStatusCode.isEmpty() ? "" : " Estatus de cancelación: [" + cancelStatusCode + "]"));
                                }
                                
                                // check cancellable status:
                                switch (cfdiAckQuery.CancellableInfo) {
                                    case DCfdi33Consts.CANCELABLE_SIN_ACEPT:
                                        isDirectlyCancellable = true;
                                        // CFDI is cancellable, go through...
                                        break;
                                        
                                    case DCfdi33Consts.CANCELABLE_CON_ACEPT:
                                        // CFDI is cancellable, go through...
                                        break;

                                    case DCfdi33Consts.CANCELABLE_NO:
                                        // CFDI is not cancellable, but
                                        // evaluate if CFDI is directly cancellable for having only one unique relation to its replacement,
                                        isDirectlyCancellable = cfdiAckQuery.CfdiRelatedList.size() == 1 && cfdiAckQuery.CfdiRelatedList.get(0).Uuid.equals(annulRelatedUuid);
                                        // anyway, go through, the authority will resolve it...
                                        break;
                                        
                                    default:
                                        throw new Exception("Estatus de cancelación desconocido: [" + cfdiAckQuery.CancellableInfo + "]");
                                }

                                // check cancellation status:
                                if (!cfdiAckQuery.CancelStatus.isEmpty()) { // since december 2020, cancel status is empty for all cancellable CFDI types!, awkward!
                                    switch (cfdiAckQuery.CancelStatus) {
                                        case DCfdi33Consts.ESTATUS_CANCEL_PROC: // CFDI cancellation is in process
                                            cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_PROC_CODE);
                                            client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_CFD);
                                            throw new Exception("La solicitud de cancelación del CFDI está pendiente de ser aceptada o rechazada por el receptor.");

                                        case DCfdi33Consts.ESTATUS_CANCEL_RECH: // CFDI cancellation was rejected by receptor
                                            if (!retryCancel) {
                                                // Sign & Cancel Log step #6
                                                createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_PAC_FLAG_CLEAR, cfd.getPkCfdId(), pac.getPkPacId());

                                                updateCfdProcessingFlags(client, cfd, false);

                                                cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_RECH_CODE);
                                                client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_CFD);
                                                throw new Exception("La solicitud de cancelación del CFDI fue rechazada por el receptor.");
                                            }
                                            // previous cancellation request was rejected, so try again...
                                            break;
                                            
                                        case DCfdi33Consts.ESTATUS_CANCEL_NINGUNO:
                                            // CFD about to be cancelled for the first time or maybe a cancellation is still in process (in pending buffer)!
                                            break;

                                        case DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT:
                                        case DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT:
                                        case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC:
                                        case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC_ALT:
                                            throw new Exception("El estatus de cancelación del CFDI es inconsistente: [" + cfdiAckQuery.CancelStatus + "]");

                                        default:
                                            throw new Exception("El estatus de cancelación del CFDI es desconocido: [" + cfdiAckQuery.CancelStatus + "]");
                                    }
                                }
                                break;

                            case DCfdi33Consts.CFDI_ESTATUS_NO_ENC:
                                // Sign & Cancel Log step #6
                                createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_PAC_FLAG_CLEAR, cfd.getPkCfdId(), pac.getPkPacId());

                                // CFDI was 'not found' before fiscal authority:
                                updateCfdProcessingFlags(client, cfd, false);

                                cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, "!!!");
                                client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_CFD);
                                throw new Exception("El CFD no fue encontrado ante el SAT.");

                            default:
                                throw new Exception("Estatus de CFDI desconocido: '" + cfdiAckQuery.CfdiStatus + "'");
                        }
                        
                        // Prepare web-service cancel request:
                            
                        com.finkok.facturacion.cancel.CancelSOAP cancelSoap = new com.finkok.facturacion.cancel.CancelSOAP();
                        com.finkok.facturacion.cancel.Application cancelApp = cancelSoap.getApplication();
                        
                        if (isValidation || getAckCancellation) {
                            // validating cancellation:
                            
                            views.core.soap.services.apps.ReceiptResult receiptResult = cancelApp.getReceipt(userName, userPswd, fiscalIdIssuer, cfd.getUuid(), "C");
                            
                            if (receiptResult == null || receiptResult.getSuccess() == null) {
                                message = "Error al cancelar el CFDI con UUID '" + cfd.getUuid() + "'."
                                        + "\nError: [" + (receiptResult == null ? "" : receiptResult.getError().getValue()) + "]";
                                
                                // Sign & Cancel Log step #4
                                createSignCancelLogEntry(client, message.replaceAll("\n", " "), actionCode, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd.getPkCfdId(), pac.getPkPacId());

                                updateCfdProcessingFlags(client, cfd, false);

                                if (pacId == 0) {
                                    throw new Exception(message);
                                }
                                next = false;
                            }
                            else {
                                if (!receiptResult.getSuccess().getValue()) {
                                    message = "La cancelación del CFDI con UUID '" + cfd.getUuid() + "' no fue exitosa."
                                            + "\nRespuesta: [" + receiptResult.getError().getValue() + "]";
                                            
                                    // Sign & Cancel Log step #4
                                    createSignCancelLogEntry(client, message.replaceAll("\n", " "), actionCode, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd.getPkCfdId(), pac.getPkPacId());

                                    updateCfdProcessingFlags(client, cfd, false);

                                    if (pacId == 0) {
                                        throw new Exception(message);
                                    }
                                    next = false;
                                }
                                else {
                                    switch (cfdiAckQuery.CancelStatus) {
                                        case DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT:
                                            cancelStatusCode = DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT_CODE;
                                            break;
                                        case DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT:
                                            cancelStatusCode = DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT_CODE;
                                            break;
                                        case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC:
                                        case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC_ALT:
                                            cancelStatusCode = DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC_CODE;
                                            break;
                                        default:
                                            cancelStatusCode = "???";
                                    }

                                    cfd.setCancellationStatus(cancelStatusCode);
                                    cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, cancelStatusCode);

                                    /*
                                    Cancellation aknowledgment comes wraped in another XML (SOAP response),
                                    so '<' and '>' must be represented with its correspondign character entity references.
                                    */
                                    xmlAcuse = receiptResult.getReceipt().getValue();
                                    xmlAcuse = xmlAcuse.replace("&lt;", "<");
                                    xmlAcuse = xmlAcuse.replace("&gt;", ">");
                                }
                            }
                        }
                        else {
                            // cancellation request:
                            
                            views.core.soap.services.apps.ObjectFactory objectFactory = new views.core.soap.services.apps.ObjectFactory();
                            views.core.soap.services.apps.UUID uuid = objectFactory.createUUID();
                            uuid.setUUID(cfd.getUuid());
                            uuid.setMotivo(annulReason);
                            uuid.setFolioSustitucion(annulRelatedUuid);
                            
                            views.core.soap.services.apps.UUIDArray uuidArray = new views.core.soap.services.apps.UUIDArray();
                            uuidArray.getUUID().add(uuid);

                            views.core.soap.services.apps.CancelaCFDResult cancelaCFDResult = cancelApp.cancel(uuidArray, userName, userPswd, fiscalIdIssuer, companyCertificate.getExtraFnkPublicKeyBytes_n(), companyCertificate.getExtraFnkPrivateKeyBytes_n(), true);
                            JAXBElement<views.core.soap.services.apps.FolioArray> elementFolios = cancelaCFDResult.getFolios();

                            if (elementFolios == null) {
                                message = "Error al cancelar el CFDI: No se recibieron folios cancelados."
                                        + "\nRespuesta PAC: [" + cancelaCFDResult.getCodEstatus().getValue() + "]";
                                
                                // Sign & Cancel Log step #4
                                createSignCancelLogEntry(client, message.replaceAll("\n", " "), actionCode, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd.getPkCfdId(), pac.getPkPacId());

                                updateCfdProcessingFlags(client, cfd, false);

                                if (pacId == 0) {
                                    throw new Exception(message);
                                }
                                next = false;
                            }
                            else if (elementFolios.getValue().getFolio().isEmpty()) {
                                message = "Error al cancelar el CFDI: La lista de folios cancelados está vacía."
                                        + "\nRespuesta PAC: [" + cancelaCFDResult.getCodEstatus().getValue() + "]";
                                
                                // Sign & Cancel Log step #4
                                createSignCancelLogEntry(client, message.replaceAll("\n", " "), actionCode, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd.getPkCfdId(), pac.getPkPacId());

                                updateCfdProcessingFlags(client, cfd, false);

                                if (pacId == 0) {
                                    throw new Exception(message);
                                }
                                next = false;
                            }
                            else {
                                String estatusUuid = elementFolios.getValue().getFolio().get(0).getEstatusUUID().getValue();
                                
                                switch (estatusUuid) {
                                    case SCfdConsts.UUID_CANCEL_OK:
                                    case SCfdConsts.UUID_CANCEL_ALREADY:
                                        // CFDI is cancelled, or a cancellation request is in process:

                                        String estatusCancelacion = elementFolios.getValue().getFolio().get(0).getEstatusCancelacion().getValue();
                                        
                                        if (estatusCancelacion.equalsIgnoreCase(DCfdi33Consts.ESTATUS_CANCEL_RECH)) {
                                            // CFDI cancellation rejected by receptor:
                                            
                                            cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_RECH_CODE);
                                            client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_CFD);
                                            throw new Exception("La solicitud de cancelación del CFDI fue rechazada por el receptor.");
                                        }
                                        else if (estatusCancelacion.equalsIgnoreCase(DCfdi33Consts.ESTATUS_CANCEL_PROC) ||
                                                (estatusCancelacion.equalsIgnoreCase(DCfdi33Consts.RESPONSE_CANCEL) && !isDirectlyCancellable)) { // unexpected message in a succesful cancellation, treated as if cancellation is in process
                                            // CFDI cancellation in process:
                                            
                                            cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_PROC_CODE);
                                            client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_CFD);
                                            throw new Exception("La solicitud de cancelación del CFDI fue enviada al receptor.");
                                        }
                                        else if (estatusCancelacion.equalsIgnoreCase(DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT) ||
                                                estatusCancelacion.equalsIgnoreCase(DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT) ||
                                                estatusCancelacion.equalsIgnoreCase(DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC) ||
                                                estatusCancelacion.equalsIgnoreCase(DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC_ALT) ||
                                                (estatusCancelacion.equalsIgnoreCase(DCfdi33Consts.RESPONSE_CANCEL) && isDirectlyCancellable)) { // unexpected message in a succesful cancellation, treated as if cancellation is done
                                            // CFDI canceled!:
                                            
                                            if (estatusCancelacion.equalsIgnoreCase(DCfdi33Consts.RESPONSE_CANCEL) && isDirectlyCancellable) {
                                                cancelStatusCode = DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT_CODE;
                                            }
                                            else {
                                                switch (estatusCancelacion) {
                                                    case DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT:
                                                        cancelStatusCode = DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT_CODE;
                                                        break;
                                                    case DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT:
                                                        cancelStatusCode = DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT_CODE;
                                                        break;
                                                    case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC:
                                                    case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC_ALT:
                                                        cancelStatusCode = DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC_CODE;
                                                        break;
                                                    default:
                                                        cancelStatusCode = "???";
                                                }
                                            }

                                            cfd.setCancellationStatus(cancelStatusCode);
                                            cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, cancelStatusCode);

                                            xmlAcuse = cancelaCFDResult.getAcuse().getValue();
                                            /*
                                            Cancellation cknowledgment comes wraped in another XML (SOAP response),
                                            so '<' and '>' must be represented with its correspondign character entity references.
                                            */
                                            xmlAcuse = xmlAcuse.replace("&lt;", "<");
                                            xmlAcuse = xmlAcuse.replace("&gt;", ">");
                                        }
                                        else if (estatusCancelacion.equalsIgnoreCase(DCfdi33Consts.ESTATUS_CANCEL_NINGUNO)) {
                                            // CFDI cancellation in pending buffer:
                                            
                                            cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_PEND_BUFF_CODE);
                                            client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_CFD);
                                            throw new Exception("El CFDI ya está en el controlador de espera (pending buffer), en proceso de ser cancelado.");
                                        }
                                        else {
                                            // unexpected cancellation code status:
                                            
                                            cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, "?");
                                            client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_CFD);
                                            throw new Exception("El estatus de cancelación del CFDI es desconocido: [" + estatusCancelacion + "].");
                                        }
                                        
                                        break;

                                    default:
                                        // unexpected response code:
                                        
                                        message = "Error al cancelar el CFDI: El código de respuesta es desconocido."
                                                + "\nRespuesta PAC: [" + estatusUuid + "]";

                                        // Sign & Cancel Log step #4
                                        createSignCancelLogEntry(client, message.replaceAll("\n", " "), actionCode, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd.getPkCfdId(), pac.getPkPacId());

                                        updateCfdProcessingFlags(client, cfd, false);

                                        if (pacId == 0) {
                                            throw new Exception(message);
                                        }
                                        next = false;
                                }
                            }
                        }
                        break;
                        
                    default:
                }
            }
            else {
                // CFDI cancelling testing environment:
                
                switch (pac.getPkPacId()) {
                    case SModSysConsts.TRN_PAC_FCG:
                        /* This case is obsolete.
                         * It do not implements new CFDI signature and cancellation valid since 2018.
                         * (Sergio Flores, 2018-11-06)
                         */
                        com.wscliente.WSForcogsaService fcgService = null;
                        com.wscliente.WSForcogsa fcgPort = null;
                        com.wscliente.WsAutenticarResponse autenticarResponse = null;
                        com.wscliente.WsCancelacionResponse canceladoResponse = null;
                        com.wscliente.WsFoliosResponse foliosResponse = null;

                        fcgService = new com.wscliente.WSForcogsaService();
                        fcgPort = fcgService.getWSForcogsaPort();

                        // Web Service Autentication:

                        // Sign & Cancel Log step #1
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_PAC_AUTH, cfd.getPkCfdId(), pac.getPkPacId());

                        autenticarResponse = fcgPort.autenticar("pruebasWS", "pruebasWS");

                        if (autenticarResponse.getMensaje() != null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLogEntry(client, "WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]", actionCode, SCfdConsts.STEP_CODE_PAC_AUTH, cfd.getPkCfdId(), pac.getPkPacId());

                            if (pacId == 0) {
                                System.err.println("WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]");
                                throw new Exception("Error al autenticarse con el PAC:\nMensaje: " + autenticarResponse.getMensaje());
                            }
                            next = false;
                        }

                        if (autenticarResponse.getToken() == null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLogEntry(client, "WsAutenticarResponse Token is null!", actionCode, SCfdConsts.STEP_CODE_PAC_AUTH, cfd.getPkCfdId(), pac.getPkPacId());

                            if (pacId == 0) {
                                System.err.println("WsAutenticarResponse Token is null!");
                                throw new Exception("Error al autenticarse con el PAC:\n¡Token nulo!");
                            }
                            next = false;
                        }

                        if (next) {
                            // Document cancel:

                            // Sign & Cancel Log step #2
                            createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_FLAGS_SET, cfd.getPkCfdId(), pac.getPkPacId());

                            updateCfdProcessingFlags(client, cfd, true);

                            // Sign & Cancel Log step #3
                            createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_SEND_RECV, cfd.getPkCfdId(), pac.getPkPacId());

                            canceladoResponse = fcgPort.cancelacion1(fiscalIdIssuer, SLibUtils.DbmsDateFormatDate.format(date), uuids, companyCertificate.getExtraPublicKeyBytes_n(), companyCertificate.getExtraPrivateKeyBytes_n(), "", autenticarResponse.getToken());

                            if (canceladoResponse.getMensaje() != null) {
                                // Sign & Cancel Log step #4
                                createSignCancelLogEntry(client, "WsCancelacionResponse Codigo: [" + canceladoResponse.getCodEstatus() + "]", actionCode, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd.getPkCfdId(), pac.getPkPacId());

                                updateCfdProcessingFlags(client, cfd, false);

                                if (pacId == 0) {
                                    System.err.println("WsCancelacionResponse Codigo: [" + canceladoResponse.getCodEstatus() + "]");
                                    System.err.println("WsCancelacionResponse Mensaje: [" + canceladoResponse.getMensaje() + "]");
                                    System.err.println("UUID: [" + uuids + "]\t");
                                    throw new Exception("Error al cancelar el comprobante:\nCódigo: " + canceladoResponse.getCodEstatus() + "\nMensaje: " + canceladoResponse.getMensaje());
                                }
                                next = false;
                            }

                            if (next) {
                                foliosResponse = canceladoResponse.getFolios();

                                if (foliosResponse.getFolio().get(0).getEstatusUUID().compareTo(SCfdConsts.UUID_CANCEL_OK) != 0) {
                                    if (foliosResponse.getFolio().get(0).getEstatusUUID().compareTo(SCfdConsts.UUID_CANCEL_ALREADY) != 0) {
                                        // Sign & Cancel Log step #4
                                        createSignCancelLogEntry(client, foliosResponse.getFolio().get(0).getMensaje(), actionCode, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd.getPkCfdId(), pac.getPkPacId());

                                        updateCfdProcessingFlags(client, cfd, false);

                                        if (pacId == 0) {
                                            throw new Exception(foliosResponse.getFolio().get(0).getMensaje());
                                        }
                                        next = false;
                                    }
                                }
                                else {
                                    xmlAcuse = canceladoResponse.getAcuse();
                                }
                            }
                        }
                        break;

                    case SModSysConsts.TRN_PAC_FNK:
                        // NOTE 1: these are not really clasess of a testing environment, but production ones!
                        // NOTE 2: there is a set of testing-environment clasess in library ClientWSFinkok2018, but they are not implemented here yet.

                        // Document cancel:

                        // Sign & Cancel Log step #1, not required in Finkok, preserved for consistence!
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_PAC_AUTH, cfd.getPkCfdId(), pac.getPkPacId());
                        
                        // Sign & Cancel Log step #2
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_FLAGS_SET, cfd.getPkCfdId(), pac.getPkPacId());

                        updateCfdProcessingFlags(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLogEntry(client, "", actionCode, SCfdConsts.STEP_CODE_SEND_RECV, cfd.getPkCfdId(), pac.getPkPacId());

                        CancelSOAP service = new CancelSOAP();
                        com.finkok.facturacion.cancel.Application port = service.getApplication();
                        
                        /* 2018-08-17, Sergio Flores: Code snippet commented for preventing connection with PAC and subsequent exception due to inactive account:
                        if (isProcessingValidation) {
                            ReceiptResult receiptResult = port.getReceipt("jbarajas@tron.com.mx", "WSfink_2014", sRfcEmisor, cfd.getRelatedUuid(), "C");
                            
                            if (receiptResult != null) {
                                if (receiptResult.getSuccess() == null) {
                                    // Sign & Cancel Log step #4
                                    createSignCancelLogEntry(client, receiptResult.getError().getValue(), actionCode, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd.getPkCfdId(), pac.getPkPacId());

                                    updateProcessCfd(client, cfd, false);

                                    if (pacId == 0) {
                                        throw new Exception("Error al intentar obtener acuse de cancelación de CFDI.\n" + "Error: " + receiptResult.getError().getValue() + " ");
                                    }
                                    next = false;
                                }
                                else {
                                    if (!receiptResult.getSuccess().getValue()) {
                                        // Sign & Cancel Log step #4
                                        createSignCancelLogEntry(client, "El UUID: '" + cfd.getRelatedUuid() + "' no ha sido cancelado.", actionCode, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd.getPkCfdId(), pac.getPkPacId());

                                        updateProcessCfd(client, cfd, false);

                                        if (pacId == 0) {
                                            throw new Exception("Error al intentar obtener acuse de cancelación de CFDI.\n" + "El UUID: '" + cfd.getRelatedUuid() + "' no ha sido cancelado.");
                                        }
                                        next = false;
                                    }
                                    else {
                                        xmlAcuse = receiptResult.getReceipt().getValue();
                                        /*
                                        Cancellation cknowledgment comes wraped in another XML (SOAP response),
                                        so '<' and '>' must be represented with its correspondign character entity references.
                                        * /
                                        xmlAcuse = xmlAcuse.replace("&lt;", "<");
                                        xmlAcuse = xmlAcuse.replace("&gt;", ">");
                                    }
                                }
                            }
                        }
                        else {
                            StringArray stringArray = new StringArray();
                            stringArray.getString().addAll(asUuid);

                            QName qNameUuids = new QName("uuids");
                            JAXBElement<StringArray> elementUuids = new JAXBElement<>(qNameUuids, StringArray.class, stringArray);

                            UUIDS uuids = new UUIDS();
                            uuids.setUuids(elementUuids);

                            CancelaCFDResult cancelaCFDResult = port.cancel(uuids, sCfdiUser, sCfdiUserPassword, sRfcEmisor, companyCertificate.getExtraFnkPublicKeyBytes_n(), companyCertificate.getExtraFnkPrivateKeyBytes_n(), true);
                            JAXBElement<FolioArray> elementFolios = cancelaCFDResult.getFolios();

                            if (elementFolios == null) {
                                // Sign & Cancel Log step #4
                                createSignCancelLogEntry(client, cancelaCFDResult.getCodEstatus().getValue(), actionCode, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd.getPkCfdId(), pac.getPkPacId());

                                updateProcessCfd(client, cfd, false);

                                if (pacId == 0) {
                                    throw new Exception(cancelaCFDResult.getCodEstatus().getValue());
                                }
                                next = false;
                            }
                            else {
                                if (elementFolios.getValue().getFolio().isEmpty()) {
                                    // Sign & Cancel Log step #4
                                    createSignCancelLogEntry(client, "Codigo: [" + cancelaCFDResult.getCodEstatus().getValue() + "] Error al intentar cancelar CFDI.", actionCode, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd.getPkCfdId(), pac.getPkPacId());

                                    updateProcessCfd(client, cfd, false);

                                    if (pacId == 0) {
                                        throw new Exception("Codigo: [" + cancelaCFDResult.getCodEstatus().getValue() + "] Error al intentar cancelar CFDI.");
                                    }
                                    next = false;
                                }
                                else {
                                    if (elementFolios.getValue().getFolio().get(0).getEstatusUUID().getValue().compareTo(SCfdConsts.UUID_ANNUL) != 0) {
                                        if (elementFolios.getValue().getFolio().get(0).getEstatusUUID().getValue().compareTo(SCfdConsts.UUID_ANNUL_PREV) != 0) {
                                            // Sign & Cancel Log step #4
                                            createSignCancelLogEntry(client, "Codigo: [" + elementFolios.getValue().getFolio().get(0).getEstatusUUID().getValue() + "] Error al intentar cancelar CFDI.", actionCode, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd.getPkCfdId(), pac.getPkPacId());

                                            updateProcessCfd(client, cfd, false);

                                            if (pacId == 0) {
                                                throw new Exception("Codigo: [" + elementFolios.getValue().getFolio().get(0).getEstatusUUID().getValue() + "] Error al intentar cancelar CFDI.");
                                            }
                                            next = false;
                                        }
                                    }
                                    else {
                                        xmlAcuse = cancelaCFDResult.getAcuse().getValue();
                                    }
                                }
                            }
                        }
                        */
                        
                        // scamp code snippet to emulate an annulled CFDI:
                        xmlAcuse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" FechaTimbrado=\"2018-08-16T16:00:02\" NoCertificadoSAT=\"00001000000405332712\" RfcProvCertif=\"FIN1203015JA\" SelloCFD=\"ONDe5/YJVqRfuohNNJA/UjxlR5SIwPlASe2/cKZaDHGQ7XKCOUefidDP3Szk9hHIE8hpJGoXjGqETQ/WKotYMcyOqzR+g0F5SGfhz34NZGVuffBLl4Co073g5ZeWGKiM6WXlim2njxxkhIqBTnf5BMcc7WqtyVfOGnwEZlXhx8kIbYKKWrSqEo72hldAZc8xrGkRikUUzp3aS6z5kDjfRcfIqSyBX1z7fOSjgYT9MXVezgEwKjwrhFUydrtz0Jqd5+KycPcHzedKJo6kbtnDmgKeLiZejGKobJ6VSlbXSYdiL+2Mt58WmUkG3JGCEzGXiBSO6ayz1Hmwjrr3rX84BA==\" SelloSAT=\"byIDPVs6qpW+D76RYX9RbZB4+inyp0QjYqzvX5Q0TObgWn9kcNKKsQ94C1OZrGon5qQx65WMlVjQsjSju6pf0Od6042c9S6emU1ANR3dSrcgtn0FjoNukj6lpgEt992hmf74D3wryVfrsc+NlCTuxFxpN0pO5Z2VADHie3GZRBzH9bH3ul8zO8hkihSqZNd1qtNQX3pW2KYnjaG6nQV0Obq441V1W483IUYxscsCrtDLrRyKvPBJQHNUuKAVyTKqzbJpD4u0tRudLIXtpSk9bj5f6ctYbl5ebZuMzOA3p7Nly/qkRoH2onLcZnx45dxJxid8hukCEojbVtW6jBps4w==\" UUID=\"5E43EBB7-D01A-4D44-A372-4A979A4778D4\" Version=\"1.1\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\"/>";  // 2018-08-17, Sergio Flores: scamp code snippet to emulate a cancelled CFDI
                        break;
                        
                    default:
                }
            }
        }
        
        return xmlAcuse;
    }

    /**
     * Creates CFD parameters.
     * NOTE 2018-05-16, Sergio Flores: This method is replicated in private method erp.mtrn.form.SFormDps.createCfdParams()!
     *      It is not clear yet if method version in current class is really needed.
     *      It seems that is used to process Addenda registrys when issuing CFD, so, by now, it cannot be removed.
     * @param miClient ERP client interface.
     * @param moDps DPS regitry.
     * @return CFD parameters.
     */
    private static SCfdParams createCfdParams(final SClientInterface miClient, final SDataDps moDps) {
        String factura = "";
        String pedido = "";
        String contrato = "";
        String ruta = "";
        SDataDps dpsFactura = null;
        SDataDps dpsPedido = null;
        SDataDps dpsContrato = null;
        SDataCustomerBranchConfig customerBranchConfig = null;
        SCfdParams params = new SCfdParams();
        SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[]{ moDps.getFkBizPartnerId_r()}, SLibConstants.EXEC_MODE_SILENT);
        SDataBizPartnerBranch bizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB,  new int[]{ moDps.getFkBizPartnerBranchId()}, SLibConstants.EXEC_MODE_SILENT);

        try {
            params.setReceptor(bizPartner);
            params.setReceptorBranch(bizPartnerBranch);
            params.setEmisor(miClient.getSessionXXX().getCompany().getDbmsDataCompany());

            if (moDps.getFkCompanyBranchId() == miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsBizPartnerBranchHq().getPkBizPartnerBranchId()) {
                params.setLugarExpedicion(null);
            }
            else {
                params.setLugarExpedicion(miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsBizPartnerBranch(new int[] { moDps.getFkCompanyBranchId() }).getDbmsBizPartnerBranchAddressOfficial());
            }

            params.setUnidadPesoBruto(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.ITMU_TP_UNIT, new int[] { SDataConstantsSys.ITMU_TP_UNIT_MASS }, SLibConstants.DESCRIPTION_CODE));
            params.setUnidadPesoNeto(params.getUnidadPesoBruto());

            // Lookup for "pedido" (the first one found):

            for (SDataDpsEntry entryDocumento : moDps.getDbmsDpsEntries()) {
                if (entryDocumento.isAccountable()) {
                    for (SDataDpsDpsLink linkPedido : entryDocumento.getDbmsDpsLinksAsDestiny()) {
                        if (!linkPedido.getDbmsIsSourceDeleted() && !linkPedido.getDbmsIsSourceEntryDeleted()) {
                            //dpsPedido = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, linkPedido.getDbmsSourceDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);
                            dpsPedido = STrnUtilities.getFirtsLinkOrderType(miClient, moDps);
                            pedido = (dpsPedido.getNumberSeries().length() == 0 ? "" : dpsPedido.getNumberSeries() + "-") + dpsPedido.getNumber();

                            // Lookup for "contrato" (the first one found):

                            for (SDataDpsEntry entryPedido : dpsPedido.getDbmsDpsEntries()) {
                                if (!entryPedido.getIsDeleted()) {
                                    for (SDataDpsDpsLink linkContrato : entryPedido.getDbmsDpsLinksAsDestiny()) {
                                        if (!linkContrato.getDbmsIsSourceDeleted() && !linkContrato.getDbmsIsSourceEntryDeleted()) {
                                            dpsContrato = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, linkContrato.getDbmsSourceDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);
                                            contrato = (dpsContrato.getNumberSeries().length() == 0 ? "" : dpsContrato.getNumberSeries() + "-") + dpsContrato.getNumber();

                                            break;  // a "contrato" was found
                                        }
                                    }
                                }

                                if (contrato.length() > 0) {
                                    break;
                                }
                            }

                            break;  // a "pedido" was found
                        }
                    }

                    // Lookup for "factura" (the first one found):

                    for (SDataDpsDpsAdjustment linkFactura : entryDocumento.getDbmsDpsAdjustmentsAsAdjustment()) {
                        if (!linkFactura.getDbmsIsDpsDeleted() && !linkFactura.getDbmsIsDpsEntryDeleted()) {
                            dpsFactura = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, linkFactura.getDbmsDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);
                            factura = (dpsFactura.getNumberSeries().length() == 0 ? "" : dpsFactura.getNumberSeries() + "-") + dpsFactura.getNumber();

                            // Lookup for "pedido" (the first one found):

                            for (SDataDpsEntry entryFactura : dpsFactura.getDbmsDpsEntries()) {
                                if (!entryFactura.getIsDeleted()) {
                                    for (SDataDpsDpsLink linkPedido : entryFactura.getDbmsDpsLinksAsDestiny()) {
                                        if (!linkPedido.getDbmsIsSourceDeleted() && !linkPedido.getDbmsIsSourceEntryDeleted()) {
                                            dpsPedido = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, linkPedido.getDbmsSourceDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);
                                            pedido = (dpsPedido.getNumberSeries().length() == 0 ? "" : dpsPedido.getNumberSeries() + "-") + dpsPedido.getNumber();

                                            break;  // a "pedido" was found
                                        }
                                    }
                                }

                                if (pedido.length() > 0) {
                                    break;
                                }
                            }

                            break;  // a "factura" was found
                        }
                    }
                }

                if (factura.length() > 0) {
                    break;
                }

                if (pedido.length() > 0) {
                    break;
                }
            }

            params.setFactura(factura);
            params.setPedido(pedido);
            params.setContrato(contrato);

            if (moDps.getDbmsDataAddenda() != null && bizPartner.getIsCustomer() && bizPartner.getDbmsCategorySettingsCus().getFkCfdAddendaTypeId() != SDataConstantsSys.BPSS_TP_CFD_ADD_NA) {
                params.setLorealFolioNotaRecepción(moDps.getDbmsDataAddenda().getLorealFolioNotaRecepcion());
                params.setBachocoSociedad(moDps.getDbmsDataAddenda().getBachocoSociedad());
                params.setBachocoOrganizaciónCompra(moDps.getDbmsDataAddenda().getBachocoOrganizacionCompra());
                params.setBachocoDivisión(moDps.getDbmsDataAddenda().getBachocoDivision());
                params.setSorianaTienda(moDps.getDbmsDataAddenda().getSorianaTienda());
                params.setSorianaEntregaMercancía(moDps.getDbmsDataAddenda().getSorianaEntregaMercancia());
                params.setSorianaRemisiónFecha(moDps.getDbmsDataAddenda().getSorianaRemisionFecha());
                params.setSorianaRemisiónFolio(moDps.getDbmsDataAddenda().getSorianaRemisionFolio());
                params.setSorianaPedidoFolio(moDps.getDbmsDataAddenda().getSorianaPedidoFolio());
                params.setSorianaBultoTipo(moDps.getDbmsDataAddenda().getSorianaBultoTipo());
                params.setSorianaBultoCantidad(moDps.getDbmsDataAddenda().getSorianaBultoCantidad());
                params.setSorianaNotaEntradaFolio(moDps.getDbmsDataAddenda().getSorianaNotaEntradaFolio());
                params.setSorianaCita(moDps.getDbmsDataAddenda().getSorianaCita());
                params.setModeloDpsDescripción(moDps.getDbmsDataAddenda().getModeloDpsDescripcion());
                params.setCfdAddendaSubtype(moDps.getDbmsDataAddenda().getCfdAddendaSubtype());
                params.setJsonData(moDps.getDbmsDataAddenda().getJsonData());
                params.setFkCfdAddendaTypeId(moDps.getDbmsDataAddenda().getFkCfdAddendaTypeId());
            }
            
            int xmlType = moDps.getDbmsDataCfd() != null ? moDps.getDbmsDataCfd().getFkXmlTypeId() : ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdTypeXmlTypes().get(SDataConstantsSys.TRNS_TP_CFD_INV);
            switch (xmlType) {
                case SDataConstantsSys.TRNS_TP_XML_CFD:
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                    if (moDps.getDbmsDataDpsCfd() != null && moDps.getDbmsDataDpsCfd().hasInternationalCommerce()) {
                        params.setRegimenFiscal(new String[] { miClient.getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdRegimenFiscal() });
                    }
                    else {
                        params.setRegimenFiscal(SLibUtilities.textExplode(miClient.getSessionXXX().getParamsCompany().getFiscalSettings(), ";"));
                    }
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                    params.setRegimenFiscal(new String[] { moDps.getDbmsDataDpsCfd().getTaxRegimeIssuing() });
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }

            // Ruta:

            customerBranchConfig = (SDataCustomerBranchConfig) SDataUtilities.readRegistry(miClient, SDataConstants.MKT_CFG_CUSB, new int[] { bizPartnerBranch.getPkBizPartnerBranchId() }, SLibConstants.EXEC_MODE_SILENT);

            if (customerBranchConfig != null) {
                if (customerBranchConfig.getFkSalesRouteId() != 0) {
                    ruta = "" + customerBranchConfig.getFkSalesRouteId();
                }
            }

            params.setRuta(ruta);

            // Miscellaneous:

            params.setInterestDelayRate(miClient.getSessionXXX().getParamsCompany().getInterestDelayRate());
        }
        catch (Exception e) {
            params = null;
            SLibUtils.showException(SCfdUtils.class.getName(), e);
        }

        return params;
    }

    /**
     * Save CFD on ERP Server.
     * @param client ERP Client interface.
     * @param packet CFD packet to process.
     * @throws Exception
     */
    private static void saveCfd(final SClientInterface client, final SCfdPacket packet) throws Exception {
        SServerRequest request = new SServerRequest(SServerConstants.REQ_CFD, packet);
        SServerResponse response = client.getSessionXXX().request(request);

        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            throw new Exception(response.getMessage());
        }
        else {
            if (response.getResultType() != SLibConstants.DB_CFD_OK) {
                throw new Exception("Código de error al emitir el CFD: " + response.getResultType() + ".");
            }
        }
    }
    
    /**
     * Process annulment of CFD.
     * @param client
     * @param cfd
     * @param payrollCfdVersion Supported constants: SCfdConsts.CFDI_PAYROLL_VER_OLD, SCfdConsts.CFDI_PAYROLL_VER_CUR or 0 when does not apply.
     * @param annulType
     * @throws Exception 
     */
    private static void processAnnul(final SClientInterface client, final SDataCfd cfd, final int payrollCfdVersion, final int annulType) throws Exception {
        Object key = null;
        SDataDps dps = null;
        SDataCfdPayment cfdPayment = null;
        SDataPayrollReceiptIssue receiptIssue = null;
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro       
        SSrvLock lock = null;
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        SRedisLock rlock = null;        
        */
        SLock slock = null;
        
        SServerRequest request = null;
        SServerResponse response = null;

        try {
            switch (cfd.getFkCfdTypeId()) {
                case SDataConstantsSys.TRNS_TP_CFD_INV:
                    // annul invoice or credit note:
                    
                    key = new int[] { cfd.getFkDpsYearId_n(), cfd.getFkDpsDocId_n() };
                    dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, key, SLibConstants.EXEC_MODE_SILENT);

                    if (dps == null) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ);
                    }
                    else {
                        // lock registry:
                        
                        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                        lock = SSrvUtils.gainLock(client.getSession(), client.getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.TRN_DPS, key, 1000 * 60); // 1 min. timeout
                        */
                        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                        rlock = SRedisLockUtils.gainLock(client, SDataConstants.TRN_DPS, key, 60);                        
                        */
                        slock = SLockUtils.gainLock(client, SDataConstants.TRN_DPS, key, 1000 * 60);
                        // check if registry can be annuled:
                        
                        request = new SServerRequest(SServerConstants.REQ_DB_CAN_ANNUL);
                        request.setPacket(dps);
                        response = client.getSessionXXX().request(request);

                        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                            throw new Exception(response.getMessage());
                        }
                        else {
                            if (response.getResultType() != SLibConstants.DB_CAN_ANNUL_YES) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_ANNUL_CAN + (response.getMessage().isEmpty() ? "" : "\n" + response.getMessage()));
                            }
                            else {
                                // annul registry:

                                dps.setIsRegistryRequestAnnul(true);
                                dps.setFkDpsAnnulationTypeId(annulType);
                                dps.setFkUserEditId(client.getSession().getUser().getPkUserId());

                                request = new SServerRequest(SServerConstants.REQ_DB_ACTION_ANNUL);
                                request.setPacket(dps);
                                response = client.getSessionXXX().request(request);

                                if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                                    throw new Exception(response.getMessage());
                                }
                                else {
                                    if (response.getResultType() != SLibConstants.DB_ACTION_ANNUL_OK) {
                                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_ANNUL + (response.getMessage().isEmpty() ? "" : "\n" + response.getMessage()));
                                    }
                                }
                            }
                        }
                    }
                    break;
                    
                case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                    // annul receipt of payment:

                    key = new int[] { cfd.getPkCfdId() };
                    cfdPayment = (SDataCfdPayment) SDataUtilities.readRegistry(client, SDataConstants.TRNX_CFD_PAY_REC, key, SLibConstants.EXEC_MODE_SILENT);
                    
                    if (cfdPayment == null) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ);
                    }
                    else {
                        // lock registry:

                        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                        lock = SSrvUtils.gainLock(client.getSession(), client.getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.TRNX_CFD_PAY_REC, key, 1000 * 60); // 1 min. timeout
                        */
                        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                        rlock = SRedisLockUtils.gainLock(client, SDataConstants.TRNX_CFD_PAY_REC, key, 60);
                        */
                        slock = SLockUtils.gainLock(client, SDataConstants.TRNX_CFD_PAY_REC, key, 1000 * 60);
                        // check if registry can be annuled:
                        
                        request = new SServerRequest(SServerConstants.REQ_DB_CAN_ANNUL);
                        request.setPacket(cfdPayment);
                        response = client.getSessionXXX().request(request);

                        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                            throw new Exception(response.getMessage());
                        }
                        else {
                            if (response.getResultType() != SLibConstants.DB_CAN_ANNUL_YES) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_ANNUL_CAN + (response.getMessage().isEmpty() ? "" : "\n" + response.getMessage()));
                            }
                            else {
                                // annul registry:

                                cfdPayment.setIsRegistryRequestAnnul(true);
                                cfdPayment.setAuxAnnulType(annulType);
                                cfdPayment.setFkUserEditId(client.getSession().getUser().getPkUserId());
                                cfdPayment.setFkUserDeleteId(client.getSession().getUser().getPkUserId()); // to preserve user when deleting accounting

                                request = new SServerRequest(SServerConstants.REQ_DB_ACTION_ANNUL);
                                request.setPacket(cfdPayment);
                                response = client.getSessionXXX().request(request);

                                if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                                    throw new Exception(response.getMessage());
                                }
                                else {
                                    if (response.getResultType() != SLibConstants.DB_ACTION_ANNUL_OK) {
                                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_ANNUL + (response.getMessage().isEmpty() ? "" : "\n" + response.getMessage()));
                                    }
                                }
                            }
                        }
                    }
                    break;
                    
                case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                    // annul payroll receipt:
                    
                    if (payrollCfdVersion == SCfdConsts.CFDI_PAYROLL_VER_CUR) {
                        key = new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n(), cfd.getFkPayrollReceiptIssueId_n() };
                        receiptIssue = new SDataPayrollReceiptIssue();
                        if (receiptIssue.read(key, client.getSession().getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ);
                        }

                        // lock registry:
                        
                        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                        lock = SSrvUtils.gainLock(client.getSession(), client.getSessionXXX().getCompany().getPkCompanyId(), SModConsts.HRS_PAY_RCP_ISS, key, 1000 * 60); // 1 min. timeout
                        */
                        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                        rlock = SRedisLockUtils.gainLock(client, SModConsts.HRS_PAY_RCP_ISS, key, 60); // 1 min. timeout
                        */
                        slock = SLockUtils.gainLock(client, SModConsts.HRS_PAY_RCP_ISS, key, 1000 * 60);
                        // check if registry can be annuled:
                        
                        request = new SServerRequest(SServerConstants.REQ_DB_CAN_ANNUL);
                        request.setPacket(receiptIssue);
                        response = client.getSessionXXX().request(request);

                        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                            throw new Exception(response.getMessage());
                        }
                        else {
                            if (response.getResultType() != SLibConstants.DB_CAN_ANNUL_YES) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_ANNUL_CAN + (response.getMessage().isEmpty() ? "" : "\n" + response.getMessage()));
                            }
                            else {
                                // annul registry:

                                receiptIssue.setIsRegistryRequestAnnul(true);
                                receiptIssue.setFkUserUpdateId(client.getSession().getUser().getPkUserId());

                                request = new SServerRequest(SServerConstants.REQ_DB_ACTION_ANNUL);
                                request.setPacket(receiptIssue);
                                response = client.getSessionXXX().request(request);

                                if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                                    throw new Exception(response.getMessage());
                                }
                                else {
                                    if (response.getResultType() != SLibConstants.DB_ACTION_ANNUL_OK) {
                                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_ANNUL + (response.getMessage().isEmpty() ? "" : "\n" + response.getMessage()));
                                    }
                                }
                            }
                        }
                    }
                    break;
                    
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        catch (Exception e) {
            /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
            if (lock != null) {
                SSrvUtils.releaseLock(client.getSession(), lock);
            }
            */
            /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
            if (rlock != null) {
                SRedisLockUtils.releaseLock(client, rlock);
            }            
            */
            if (slock != null) {
                SLockUtils.releaseLock(client, slock);
            }
            throw e;
        }
        finally {
            /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
            if (lock != null) {
                SSrvUtils.releaseLock(client.getSession(), lock);
            }
            */
            /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
            if (rlock != null) {
                SRedisLockUtils.releaseLock(client, rlock);
            }            
            */
            if (slock != null) {
                SLockUtils.releaseLock(client, slock);
            }
        }
    }

    /**
     * Obtain CFDI signature.
     * @param xml CFDI in XML format.
     * @return CFDI signature.
     */
    private static SCfdiSignature extractCfdiSignature(String xml) {
        Node node = null;
        Node nodeChild = null;
        NamedNodeMap namedNodeMap = null;
        DocumentBuilder docBuilder = null;
        Document doc = null;
        SCfdiSignature cfdiSign = null;
        
        // XXX Improve this!!!

        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
            
            NodeList comprobante = SXmlUtils.extractElements(doc, "cfdi:Comprobante");
            String version = "";
            
            version = SXmlUtils.extractAttributeValue(comprobante.item(0).getAttributes(), "version", false);
            if (version.isEmpty()) {
                version = SXmlUtils.extractAttributeValue(comprobante.item(0).getAttributes(), "Version", false);
            }

            NodeList nodeList = doc.getElementsByTagName("cfdi:Complemento");

            if (nodeList == null) {
                throw new Exception("XML element '" + "cfdi:Complemento" + "' not found!");
            }
            else {
                node = nodeList.item(0);
            }

            nodeList = node.getChildNodes();

            if (nodeList == null) {
                throw new Exception("XML element '" + "cfdi:Complemento" + "' does not have child elements!");
            }
            else {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    if (nodeList.item(i).getNodeName().compareTo("tfd:TimbreFiscalDigital") == 0) {
                        nodeChild = nodeList.item(i);
                        break;
                    }
                }
            }

            cfdiSign = new SCfdiSignature();    // XXX refactor this, return null if no signature exist!, now allways an object, even empty, is returned!
            
            if (nodeChild == null) {
                throw new Exception("XML element '" + "tfd:TimbreFiscalDigital" + "' does not exists!");
            }
            else {
                namedNodeMap = nodeChild.getAttributes();
                
                if (version.compareTo("" + DCfdConsts.CFDI_VER_32) == 0) {
                    node = namedNodeMap.getNamedItem("UUID");
                    cfdiSign.setUuid(node.getNodeValue());

                    node = namedNodeMap.getNamedItem("FechaTimbrado");
                    cfdiSign.setFechaTimbrado(node.getNodeValue());

                    node = namedNodeMap.getNamedItem("selloCFD");
                    cfdiSign.setSelloCFD(node.getNodeValue());

                    node = namedNodeMap.getNamedItem("noCertificadoSAT");
                    cfdiSign.setNoCertificadoSAT(node.getNodeValue());

                    node = namedNodeMap.getNamedItem("selloSAT");
                    cfdiSign.setSelloSAT(node.getNodeValue());

                    node = SXmlUtils.extractElements(doc, "cfdi:Emisor").item(0);
                    namedNodeMap = node.getAttributes();
                    node = namedNodeMap.getNamedItem("rfc");
                    cfdiSign.setRfcEmisor(node.getNodeValue());

                    node = SXmlUtils.extractElements(doc, "cfdi:Receptor").item(0);
                    namedNodeMap = node.getAttributes();
                    node = namedNodeMap.getNamedItem("rfc");
                    cfdiSign.setRfcReceptor(node.getNodeValue());

                    node = SXmlUtils.extractElements(doc, "cfdi:Comprobante").item(0);
                    namedNodeMap = node.getAttributes();
                    node = namedNodeMap.getNamedItem("total");
                    cfdiSign.setTotalCy(Double.parseDouble(node.getNodeValue()));
                }
                else if ((version.compareTo("" + DCfdConsts.CFDI_VER_33) == 0) || (version.compareTo("" + DCfdConsts.CFDI_VER_40)) == 0) {
                    node = namedNodeMap.getNamedItem("UUID");
                    cfdiSign.setUuid(node.getNodeValue());

                    node = namedNodeMap.getNamedItem("FechaTimbrado");
                    cfdiSign.setFechaTimbrado(node.getNodeValue());

                    node = namedNodeMap.getNamedItem("SelloCFD");
                    cfdiSign.setSelloCFD(node.getNodeValue());

                    node = namedNodeMap.getNamedItem("NoCertificadoSAT");
                    cfdiSign.setNoCertificadoSAT(node.getNodeValue());

                    node = namedNodeMap.getNamedItem("SelloSAT");
                    cfdiSign.setSelloSAT(node.getNodeValue());

                    node = SXmlUtils.extractElements(doc, "cfdi:Emisor").item(0);
                    namedNodeMap = node.getAttributes();
                    node = namedNodeMap.getNamedItem("Rfc");
                    cfdiSign.setRfcEmisor(node.getNodeValue());

                    node = SXmlUtils.extractElements(doc, "cfdi:Receptor").item(0);
                    namedNodeMap = node.getAttributes();
                    node = namedNodeMap.getNamedItem("Rfc");
                    cfdiSign.setRfcReceptor(node.getNodeValue());

                    node = SXmlUtils.extractElements(doc, "cfdi:Comprobante").item(0);
                    namedNodeMap = node.getAttributes();
                    node = namedNodeMap.getNamedItem("Total");
                    cfdiSign.setTotalCy(Double.parseDouble(node.getNodeValue()));
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(SCfdUtils.class.getName(), e);
        }

        return cfdiSign;
    }

    /**
     * Activate/disactivate all CFD processing flags.
     */
    private static void updateCfdProcessingFlags(final SClientInterface client, final SDataCfd cfd, final boolean activate) throws Exception {
        cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_PRC_WS, activate);
        cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_PRC_STO_XML, activate);
        cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_PRC_STO_PDF, activate);
        cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_PRC_USR, client.getSession().getUser().getPkUserId());
        client.getSession().notifySuscriptors(SModConsts.TRN_CFD);
    }

    /**
     * Checks if CFD was sent by email.
     * @param client ERP Client interface.
     * @param cfdId CFD ID.
     * @return <code>true</code> when sent, otherwise <code>false</code>.
     * @throws java.lang.Exception
     */
    private static boolean isCfdSent(final SClientInterface client, final int cfdId) throws SQLException, Exception {
        String sql = "";
        ResultSet resultSet = null;
        boolean was = false;

        sql = "SELECT COUNT(*) AS f_count " +
                "FROM trn_cfd_snd_log " +
                "WHERE id_cfd = " + cfdId + " ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            was = resultSet.getInt("f_count") >= 1;
        }

        return was;
    }

    /**
     * Validate that CFDI's XML corresponds to CFD registry.
     * @param xml CFDI's XML.
     * @param cfd CFD registry.
     * @return true if is correct.
     */
    @SuppressWarnings("deprecation")
    private static boolean doesXmlBelongsToCfd(final String xml, final SDataCfd cfd) throws Exception {
        cfd.ver32.DElementComprobante comprobanteXml = null;
        cfd.ver32.DElementComprobante comprobanteCfd = null;
        boolean valid = false;

        comprobanteXml = DCfdUtils.getCfdi32(xml);
        comprobanteCfd = DCfdUtils.getCfdi32(cfd.getDocXml());

        valid = comprobanteCfd.getEltEmisor().getAttRfc().getString().compareTo(comprobanteXml.getEltEmisor().getAttRfc().getString()) == 0 &&
                comprobanteCfd.getEltReceptor().getAttRfc().getString().compareTo(comprobanteXml.getEltReceptor().getAttRfc().getString()) == 0 &&
                comprobanteCfd.getAttSerie().getString().compareTo(comprobanteXml.getAttSerie().getString()) == 0 &&
                comprobanteCfd.getAttFolio().getString().compareTo(comprobanteXml.getAttFolio().getString()) == 0 &&
                comprobanteCfd.getAttFecha().getDatetime().compareTo(comprobanteXml.getAttFecha().getDatetime()) == 0 &&
                comprobanteCfd.getAttTotal().getDouble() == comprobanteXml.getAttTotal().getDouble();

        return valid;
    }
    
    /**
     * Check if request if for stamping or annulment.
     * @param request Options defined in erp.cfd.SCfdConsts.
     * @return 
     */
    private static boolean isRequestForStamping(final int request) {
        return SLibUtils.belongsTo(request, new int[] {
            SCfdConsts.REQ_STAMP, SCfdConsts.REQ_STAMP_SEND, 
            SCfdConsts.REQ_ANNUL, SCfdConsts.REQ_ANNUL_SEND
        });
    }
    
    /*
     * Public static methods:
     */
    
    /**
     * Get CFD version.
     * @param xmlType Supported options: SDataConstantsSys.TRNS_TP_XML_CFDI_32 or SDataConstantsSys.TRNS_TP_XML_CFDI_33 or SDataConstantsSys.TRNS_TP_XML_CFDI_40.
     * @return DCfdConsts.CFDI_VER_32 or DCfdConsts.CFDI_VER_33.
     */
    public static float getCfdVersion(final int xmlType) {
        float version = 0;
        
        switch (xmlType) {
            case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                version = DCfdConsts.CFDI_VER_32;
                break;
            case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                version = DCfdConsts.CFDI_VER_33;
                break;
            case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                version = DCfdConsts.CFDI_VER_40;
                break;
            default:
        }
        
        return version;
    }
    
    /**
     * Composes a full datetime object from provided date and current time.
     * @param date Provided date.
     * @return Full datetime object.
     */
    public static java.util.Date composeDatetime(final java.util.Date date) {
        int[] digestion = SLibTimeUtils.digestDate(date);
        GregorianCalendar calendar = new GregorianCalendar();
        
        calendar.set(GregorianCalendar.YEAR, digestion[0]);
        calendar.set(GregorianCalendar.MONTH, digestion[1] - 1);  // January is month 0
        calendar.set(GregorianCalendar.DATE, digestion[2]);

        return calendar.getTime();
    }

    /**
     * Init data set for processing a payroll.
     * @param request Options defined in erp.cfd.SCfdConsts.REQ_...
     */
    public static void initDataSetForPayroll(final int request) {
        resetDataSetForPayroll();
        SCfdUtils.DataSet.put(SCfdUtils.KEY_CFD, isRequestForStamping(request) ? SCfdUtils.KEY_CFD_STAMPING : SCfdUtils.KEY_CFD_PROCESSING);
    }
    
    /**
     * Reset data set for processing a payroll.
     */
    public static void resetDataSetForPayroll() {
        SCfdUtils.DataSet.remove(SCfdUtils.KEY_CFD);
        SCfdUtils.DataSet.remove(SCfdUtils.KEY_CFD_ISSUER);
        SCfdUtils.DataSet.remove(SModConsts.HRS_PAY);
    }
    
    /**
     * Streamline retrieval of payroll setting it just once and getting it each time is required from data set.
     * Data set helps prevent from reading payroll multiple times because it is a really lengthy operation.
     * @param session GUI session.
     * @param payrollId Payroll ID to validate available payroll in data set or to retreive it if needed.
     * @return 
     */
    public static SDbPayroll retrieveDataSetForPayroll(final SGuiSession session, final int payrollId) {
        SDbPayroll payroll = (SDbPayroll) SCfdUtils.DataSet.get(SModConsts.HRS_PAY);

        if (payroll == null || payroll.getPkPayrollId() != payrollId) {
            // read and set payroll because it does not exist in data set or the existing is different from the required one:
            payroll = (SDbPayroll) session.readRegistry(SModConsts.HRS_PAY, new int[] { payrollId });
            SCfdUtils.DataSet.put(SModConsts.HRS_PAY, payroll);
        }
        
        return payroll;
    }
    
    /**
     * Get CFD PAC type.
     * @param client GUI client.
     * @param cfdPacTypeId ID of CFD PAC type.
     * @return CFD PAC type.
     */
    public static SDataCfdPacType getCfdPacType(final SClientInterface client, final int cfdPacTypeId) {
        SDataCfdPacType cfdPacType = null;
        
        try {
            cfdPacType = (SDataCfdPacType) SDataUtilities.readRegistry(client, SDataConstants.TRN_TP_CFD_PAC, new int[] { cfdPacTypeId }, SLibConstants.EXEC_MODE_SILENT);
        }
        catch (Exception e) {
            SLibUtils.showException(SCfdUtils.class.getName(), e);
        }
        
        return cfdPacType;
    }

    public static boolean existsCfdiPending(final SClientInterface client, final ArrayList<SDataCfd> cfds) throws Exception {
        if (cfds != null) {
            for (SDataCfd cfd : cfds) {
                if (cfd.getIsProcessingWebService()) {
                    throw new Exception("Existen CFDI pendientes de respuesta del Proveedor Autorizado de Certificación (PAC).");
                }
                else if (cfd.getIsProcessingStorageXml()) {
                    throw new Exception("Existen archivos XML de CFDI por almacenar en el disco duro.");
                }
                else if (cfd.getIsProcessingStoragePdf()) {
                    throw new Exception("Existen archivos PDF de CFDI por almacenar en el disco duro.");
                }
            }
        }

        return true;
    }

    public static void resetCfdiDeactivateFlags(final SClientInterface client, final SDataCfd cfd) throws Exception {
        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del comprobante.");
        }
        else {
            boolean deactivate = true;
            if (isSignedByFinkok(client, cfd)) {
                if (client.showMsgBoxConfirm("El PAC de timbrado del CFDI permite validar el timbrado o cancelación.\n"
                        + "Se sugiere realizar dicha validación de timbrado o cancelación.\n"
                        + "¿Está seguro que desea continuar con la limpieza de inconsistencias de timbrado o cancelación del CFDI?") != JOptionPane.YES_OPTION) {
                    deactivate = false;
                }
            }
            if (deactivate) {
                if (client.showMsgBoxConfirm("Si limpia las inconsistencias del timbrado o cancelación del CFDI puede " + 
                        (!cfd.isStamped() ? "timbrarse más de una vez" : "que ya esté cancelado") + ".\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                    updateCfdProcessingFlags(client, cfd, false);
                    cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, "");
                    
                    // Open Sign & Cancel Log entry:
                    SignAndCancelLogEntryId = 0;
                    createSignCancelLogEntry(client, "", SCfdConsts.ACTION_CODE_RESET_FLAGS, SCfdConsts.STEP_CODE_NA, cfd.getPkCfdId(), 0);
                }
            }
        }
    }

    /**
     * 
     * @param client
     * @param cfd
     * @param payrollCfdVersion Supported constants: SCfdConsts.CFDI_PAYROLL_VER_OLD, SCfdConsts.CFDI_PAYROLL_VER_CUR or 0 when does not apply.
     * @param isSingle
     * @param confirmSending
     * @return
     * @throws Exception 
     */
    public static boolean signCfdi(final SClientInterface client, final SDataCfd cfd, final int payrollCfdVersion, boolean isSingle, boolean confirmSending) throws Exception {
        boolean signed = false;
        
        if (canCfdiSign(client, cfd, false)) {
            if (!isSingle || !confirmSending || client.showMsgBoxConfirm("¿Está seguro que desea timbrar el comprobante?") == JOptionPane.YES_OPTION) {
                // Open Sign & Cancel Log entry:
                SignAndCancelLogEntryId = 0;
                createSignCancelLogEntry(client, "", SCfdConsts.ACTION_CODE_PRC_SIGN, SCfdConsts.STEP_CODE_NA, cfd.getPkCfdId(), getCfdPacType(client, cfd.getFkCfdTypeId()).getFkPacId());

                managementCfdi(client, cfd, SDataConstantsSys.TRNS_ST_DPS_EMITED, null, isSingle, false, 0, payrollCfdVersion, 0, "", "", false);
                signed = true;
            }
        }
        
        return signed;
    }

    /**
     * 
     * @param client
     * @param cfd
     * @param payrollCfdVersion Supported constants: SCfdConsts.CFDI_PAYROLL_VER_OLD, SCfdConsts.CFDI_PAYROLL_VER_CUR or 0 when does not apply.
     * @param cancellationDate
     * @param validateStamp
     * @param isSingle
     * @param annulType
     * @param annulReason
     * @param annulRelatedUuid
     * @param retryCancel
     * @return
     * @throws Exception 
     */
    public static boolean cancelCfdi(final SClientInterface client, final SDataCfd cfd, final int payrollCfdVersion, final Date cancellationDate, boolean validateStamp, boolean isSingle, int annulType, String annulReason, String annulRelatedUuid, boolean retryCancel) throws Exception {
        boolean canceled = false;
        boolean tryCanceled = true;
        int pacId = 0;

        pacId = getCfdSigningPacId(client, cfd);

        if (canCfdiCancel(client, cfd, false)) {
            if (validateStamp) {
                while (tryCanceled) {
                    if (pacId == 0 && !existsPacConfiguration(client, cfd)) {
                        throw new Exception("No existe ningún PAC configurado para este tipo de CFDI.");
                    }
                    else if (validateStamp) {
                        if (isNeedStamps(client, cfd, SDbConsts.ACTION_ANNUL, pacId) && getStampsAvailable(client, cfd.getFkCfdTypeId(), cfd.getTimestamp(), pacId) <= 0) {
                            if (pacId == 0) {
                                throw new Exception("No existen timbres disponibles.");
                            }
                        }
                    }

                    if (!isSingle || client.showMsgBoxConfirm("La anulación de un CFDI no puede revertirse.\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        // Open Sign & Cancel Log entry:
                        SignAndCancelLogEntryId = 0;
                        createSignCancelLogEntry(client, "", SCfdConsts.ACTION_CODE_PRC_ANNUL, SCfdConsts.STEP_CODE_NA, cfd.getPkCfdId(), pacId != 0 ? pacId : getCfdPacType(client, cfd.getFkCfdTypeId()).getFkPacId(), annulReason, annulRelatedUuid);

                        if (canCfdiCancelWebService(client, cfd, pacId)) {
                            canceled = managementCfdi(client, cfd, SDataConstantsSys.TRNS_ST_DPS_ANNULED, cancellationDate, isSingle, false, pacId, payrollCfdVersion, annulType, annulReason, annulRelatedUuid, retryCancel);
                        }
                        else {
                            processAnnul(client, cfd, payrollCfdVersion, annulType);
                            canceled = true;
                        }
                    }
                    else {
                        pacId = 0;
                        updateCfdProcessingFlags(client, cfd, false);
                    }
                    tryCanceled = !(pacId == 0 || canceled);
                    pacId = 0;
                }
            }
            else {
                processAnnul(client, cfd, payrollCfdVersion, annulType);
                canceled = true;
            }
        }
        
        return canceled;
    }

    /**
     * 
     * @param client
     * @param cfd
     * @param payrollCfdVersion Supported constants: SCfdConsts.CFDI_PAYROLL_VER_OLD, SCfdConsts.CFDI_PAYROLL_VER_CUR or 0 when does not apply.
     * @param printMode Constants defined in SDataConstantsPrint.PRINT_MODE_...
     * @param numberCopies
     * @throws Exception 
     */
    @SuppressWarnings("deprecation")
    private static void computePrintCfd(final SClientInterface client, final SDataCfd cfd, final int payrollCfdVersion, int printMode, int numberCopies) throws Exception {
        SDataDps dps = null;
        SDbBillOfLading bol = null;
        SCfdParams params = null;
        SCfdPrint cfdPrint = new SCfdPrint(client);

        switch (cfd.getFkCfdTypeId()) {
            case SDataConstantsSys.TRNS_TP_CFD_INV:
                dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, new int[] { cfd.getFkDpsYearId_n(), cfd.getFkDpsDocId_n() }, SLibConstants.EXEC_MODE_SILENT);
                params = createCfdParams(client, dps);
                dps.setAuxCfdParams(params);

                switch (cfd.getFkXmlTypeId()) {
                    case SDataConstantsSys.TRNS_TP_XML_CFD:
                        cfdPrint.printCfd(cfd, printMode, dps);
                        break;
                    case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                        cfdPrint.printCfdi32(cfd, printMode, dps);
                        break;
                    case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                        cfdPrint.printCfdi33(cfd, printMode, dps);
                        break;
                    case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                        cfdPrint.printCfdi40(cfd, printMode, dps);
                        break;
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;

            case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                switch (cfd.getFkXmlTypeId()) {
                    case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                        cfdPrint.printCfdi33_Crp10(client, cfd, printMode);
                        break;
                    case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                        cfdPrint.printCfdi40_Crp20(client, cfd, printMode);
                        break;
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;

            case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                switch (cfd.getFkXmlTypeId()) {
                    case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                        if (DCfdUtils.getVersionPayrollComplement(cfd.getDocXml()) == DCfdVer3Consts.VER_NOM_11) {
                            cfdPrint.printPayrollReceipt32_11(cfd, printMode, numberCopies, payrollCfdVersion);
                        }
                        else if (DCfdUtils.getVersionPayrollComplement(cfd.getDocXml()) == DCfdVer3Consts.VER_NOM_12) {
                            cfdPrint.printPayrollReceipt32_12(cfd, printMode, numberCopies, payrollCfdVersion);
                        }
                        break;
                    case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                        if (DCfdUtils.getVersionPayrollComplement(cfd.getDocXml()) == DCfdVer3Consts.VER_NOM_11) {
                            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                        else if (DCfdUtils.getVersionPayrollComplement(cfd.getDocXml()) == DCfdVer3Consts.VER_NOM_12) {
                            // prevent from reading payroll multiple times because it is a really lengthy operation:
                            SCfdUtils.retrieveDataSetForPayroll(client.getSession(), cfd.getFkPayrollReceiptPayrollId_n()); // streamline payroll retrieval
                            
                            // proceed with CFD printing:
                            cfdPrint.printPayrollReceipt33_12(cfd, printMode, numberCopies, payrollCfdVersion);
                        }
                        break;
                    case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                        if (DCfdUtils.getVersionPayrollComplement(cfd.getDocXml()) == DCfdVer3Consts.VER_NOM_12) {
                            // prevent from reading payroll multiple times because it is a really lengthy operation:
                            SCfdUtils.retrieveDataSetForPayroll(client.getSession(), cfd.getFkPayrollReceiptPayrollId_n()); // streamline payroll retrieval
                            
                            // proceed with CFD printing:
                            cfdPrint.printPayrollReceipt40_12(cfd, printMode, numberCopies, payrollCfdVersion);
                        }
                        break;
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SDataConstantsSys.TRNS_TP_CFD_BOL:
                switch (cfd.getFkXmlTypeId()) {
                    case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                        bol = new SDbBillOfLading();
                        bol.read(client.getSession(), new int[]{ cfd.getFkBillOfLadingId_n() });
                        cfdPrint.printBolReceip33_20(client, cfd, printMode, bol);
                        break;
                    case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                        bol = new SDbBillOfLading();
                        bol.read(client.getSession(), new int[]{ cfd.getFkBillOfLadingId_n() });
                        cfdPrint.printBolReceip40_20(client, cfd, printMode, bol);
                        break;
                    default:
                }
                break;

            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
    }

    /**
     * Print CFD.
     * @param client
     * @param cfd
     * @param payrollCfdVersion Supported constants: SCfdConsts.CFDI_PAYROLL_VER_OLD, SCfdConsts.CFDI_PAYROLL_VER_CUR or 0 when does not apply.
     * @param printMode Constants defined in SDataConstantsPrint.PRINT_MODE_...
     * @param numberCopies
     * @param isCfdStorageInProcess Tell if CFD storage is in process.
     * @throws Exception 
     */
    public static void printCfd(final SClientInterface client, final SDataCfd cfd, final int payrollCfdVersion, int printMode, int numberCopies, boolean isCfdStorageInProcess) throws Exception {
        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del comprobante.");
        }
        else {
            if (canPrintCfd(cfd, isCfdStorageInProcess)) {
                computePrintCfd(client, cfd, payrollCfdVersion, printMode, numberCopies);
            }
        }
    }

    /**
     * 
     * @param client
     * @param cfds
     * @param payrollCfdVersion Supported constants: SCfdConsts.CFDI_PAYROLL_VER_OLD, SCfdConsts.CFDI_PAYROLL_VER_CUR or 0 when does not apply.
     * @param numberCopies
     * @throws Exception 
     */
    public static void printPayrollCfds(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int payrollCfdVersion, int numberCopies) throws Exception {
        ArrayList<SDataCfd> cfdsPrintable = new ArrayList<>();

        for (SDataCfd cfd : cfds) {
            if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                cfdsPrintable.add(cfd);
            }
        }

        if (cfdsPrintable.isEmpty()) {
            client.showMsgBoxInformation("No hay comprobantes para imprimir.");
        }
        else {
            if (client.showMsgBoxConfirm("¿Está seguro que desea imprimir " + cfdsPrintable.size() + " comprobantes?") == JOptionPane.YES_OPTION) {
                SDialogCfdProcessing dialog = new SDialogCfdProcessing((SClient) client, "Procesamiento de impresión", SCfdConsts.REQ_PRINT_DOC);
                dialog.setFormParams(cfdsPrintable, null, 0, null, false, payrollCfdVersion, SModSysConsts.TRNU_TP_DPS_ANN_NA, "", "", false);
                dialog.setNumberCopies(numberCopies);
                dialog.setVisible(true);
            }
        }
    }

    private static void printCfdCancelAckPdf(final SClientInterface client, final SDataCfd cfd) throws Exception {
        File file = null;
        FileOutputStream fw = null;
        byte[] buffer = new byte[1];
        InputStream ackCancellation = null;

        client.getFileChooser().setSelectedFile(new File(cfd.getDocXmlName().replaceAll(".xml", "_CANCELA_RESP") + ".pdf"));
        if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
            file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());
            fw = new FileOutputStream(file);
            ackCancellation = getCancelAckPdf(client, cfd);

            while (ackCancellation.read(buffer) > 0) {
                fw.write(buffer);
            }
            fw.close();
            client.showMsgBoxInformation(SLibConstants.MSG_INF_FILE_CREATE + file.getAbsolutePath());
        }
    }

    /**
     * 
     * @param client GUI client.
     * @param cfd CFD.
     * @param payrollCfdVersion Supported constants: SCfdConsts.CFDI_PAYROLL_VER_OLD, SCfdConsts.CFDI_PAYROLL_VER_CUR or 0 when does not apply.
     * @param printMode Constants defined in SDataConstantsPrint.PRINT_MODE_...
     * @throws Exception 
     */
    public static void printCfdCancelAck(final SClientInterface client, final SDataCfd cfd, final int payrollCfdVersion, int printMode) throws Exception {
        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del comprobante.");
        }
        else {
            if (canObtainCfdCancelAck(client, cfd)) {
                if (!cfd.getAcknowledgmentCancellationXml().isEmpty()) {
                    SCfdPrint cfdPrint = new SCfdPrint(client);
                    cfdPrint.printCancelAck(cfd, printMode, payrollCfdVersion);
                }
                else {
                    printCfdCancelAckPdf(client, cfd);
                }
            }
        }
    }

    /**
     * Print cancel acknowledgment for an array of CFD.
     * @param client
     * @param cfds
     * @param payrollCfdVersion
     * @throws Exception 
     */
    public static void printCancelAckForCfds(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int payrollCfdVersion) throws Exception {
        ArrayList<SDataCfd> cfdsCancelled = new ArrayList<>();

        for (SDataCfd cfd : cfds) {
            if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                cfdsCancelled.add(cfd);
            }
        }

        if (cfdsCancelled.isEmpty()) {
            client.showMsgBoxInformation("No existen acuses de cancelación para imprimir.");
        }
        else {
            if (client.showMsgBoxConfirm("¿Está seguro que desea imprimir " + cfdsCancelled.size() + " " + (cfdsCancelled.size() == 1 ? "acuse" : "acuses") + " de cancelación?") == JOptionPane.YES_OPTION) {
                SDialogCfdProcessing dialog = new SDialogCfdProcessing((SClient) client, "Procesamiento de impresión de acuses de cancelación", SCfdConsts.REQ_PRINT_ANNUL_ACK);
                dialog.setFormParams(cfdsCancelled, null, 0, null, false, payrollCfdVersion, SDataConstantsSys.TRNU_TP_DPS_ANN_NA, "", "", false);
                dialog.setVisible(true);
            }
        }
    }

    /**
     * Sign and Send a CFD.
     * @param client ERP Client interface.
     * @param cfd CFI to be send.
     * @param payrollCfdVersion Constants defined in SCfdConsts.CFDI_PAYROLL_VER_...
     * @param isSingle It is true when there is one cfd
     * @param confirmSending It is true when the confirmation will be done.
     * @return 
     * @throws Exception
     */
    public static boolean signAndSendCfdi(final SClientInterface client, final SDataCfd cfd, final int payrollCfdVersion, final boolean isSingle, final boolean confirmSending) throws Exception {
        boolean signed = false;
        boolean sent = false;
        int idBizPartner = 0;
        int idBizPartnerBranch = 0;

        try {
            // Sign CFDI:
            
            if (canCfdiSign(client, cfd, false)) {
                switch (cfd.getFkCfdTypeId()) {
                    case SDataConstantsSys.TRNS_TP_CFD_INV:
                        SDataDps dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, new int[] { cfd.getFkDpsYearId_n(), cfd.getFkDpsDocId_n() }, SLibConstants.EXEC_MODE_SILENT);
                        idBizPartner = dps.getFkBizPartnerId_r();
                        idBizPartnerBranch = dps.getFkBizPartnerBranchId();
                        break;

                    case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                        idBizPartner = SDataCfdPayment.getDbmsDataReceptorId(client.getSession().getStatement(), cfd.getPkCfdId());
                        idBizPartnerBranch = 0; // do not really needed, just for consistence
                        break;

                    case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                        idBizPartner = payrollCfdVersion == SCfdConsts.CFDI_PAYROLL_VER_OLD ? cfd.getFkPayrollBizPartnerId_n() : cfd.getFkPayrollReceiptEmployeeId_n();
                        idBizPartnerBranch = 0; // do not really needed, just for consistence
                        break;
                    
                    case SDataConstantsSys.TRNS_TP_CFD_BOL:
                        SDbBillOfLading bol = new SDbBillOfLading();
                        bol.read(client.getSession(), new int[] { cfd.mnFkBillOfLadingId_n });
                        SDataBizPartnerBranch bpb = (SDataBizPartnerBranch) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BPB, new int[] { bol.getFkCompanyBranchId() }, SLibConstants.EXEC_MODE_SILENT);
                        idBizPartner = bpb.getFkBizPartnerId();
                        idBizPartnerBranch = bpb.getPkBizPartnerBranchId();
                        break;
                    default:
                }

                if (!confirmSending || STrnUtilities.confirmSend(client,TXT_SIGN_SEND, cfd, null, idBizPartner, idBizPartnerBranch)) {
                    signed = signCfdi(client, cfd, payrollCfdVersion, isSingle, false);

                    if (signed) {
                        // read again just signed CFDI:
                        SDataCfd cfdFresh = (SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, cfd.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                        
                        // send CFDI:
                        sent = sendCfd(client, cfdFresh, payrollCfdVersion, isSingle, false);
                    }
                }
            }
        }
        catch (Exception e) {
            if (signed) {
                if (sent) {
                    throw new Exception("El comprobante fue timbrado, pero ocurrió una excepción al enviarse vía mail:\n" + e.getMessage());
                }
                else {
                    throw new Exception("El comprobante fue timbrado, pero no enviado:\n" + e.getMessage());
                }
            }
            else {
                throw new Exception("No fue posible timbrar ni enviar el comprobante:\n" + e.getMessage());
            }
        }
        
        return signed;
    }

    /**
     * 
     * @param client
     * @param cfd
     * @param payrollCfdVersion Constants defined in SCfdConsts.CFDI_PAYROLL_VER_...
     * @param cancellationDate
     * @param validateStamp
     * @param isSingle
     * @param annulType
     * @param annulReason
     * @param annulRelatedUuid
     * @param retryCancel
     * @return
     * @throws Exception 
     */
    public static boolean cancelAndSendCfdi(final SClientInterface client, final SDataCfd cfd, final int payrollCfdVersion, final Date cancellationDate, boolean validateStamp, boolean isSingle, int annulType, String annulReason, String annulRelatedUuid, boolean retryCancel) throws Exception {
        boolean canceled = false;
        boolean sent = false;
        boolean tryCanceled = true;
        int pacId = 0;
        boolean sendNotification = false;

        pacId = getCfdSigningPacId(client, cfd);

        if (canCfdiCancel(client, cfd, false)) {
            if (validateStamp) {
                while (tryCanceled) {
                    if (pacId == 0 && !existsPacConfiguration(client, cfd)) {
                        throw new Exception("No existe ningún PAC configurado para este tipo de CFDI.");
                    }
                    else if (validateStamp) {
                        if (isNeedStamps(client, cfd, SDbConsts.ACTION_ANNUL, pacId) && getStampsAvailable(client, cfd.getFkCfdTypeId(), cfd.getTimestamp(), pacId) <= 0) {
                            if (pacId == 0) {
                                throw new Exception("No existen timbres disponibles.");
                            }
                        }
                    }

                    if (!isSingle || client.showMsgBoxConfirm("La anulación de un CFDI no puede revertirse.\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        // Open Sign & Cancel Log entry:
                        SignAndCancelLogEntryId = 0;
                        createSignCancelLogEntry(client, "", SCfdConsts.ACTION_CODE_PRC_ANNUL, SCfdConsts.STEP_CODE_NA, cfd.getPkCfdId(), pacId != 0 ? pacId : getCfdPacType(client, cfd.getFkCfdTypeId()).getFkPacId(), annulReason, annulRelatedUuid);

                        if (canCfdiCancelWebService(client, cfd, pacId)) {
                            canceled = managementCfdi(client, cfd, SDataConstantsSys.TRNS_ST_DPS_ANNULED, cancellationDate, isSingle, false, pacId, payrollCfdVersion, annulType, annulReason, annulRelatedUuid, retryCancel);
                            sendNotification = true;
                        }
                        else {
                            processAnnul(client, cfd, payrollCfdVersion, annulType);
                            canceled = true;
                        }
                    }
                    else {
                        pacId = 0;
                        updateCfdProcessingFlags(client, cfd, false);
                    }
                    tryCanceled = !(pacId == 0 || canceled);
                    pacId = 0;

                    // Send CFDI:

                    if (canceled && sendNotification) {
                        try {
                            // read again just signed CFDI:
                            SDataCfd cfdFresh = (SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, cfd.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                            
                            // send CFDI:
                            sent = sendCfd(client, cfdFresh, payrollCfdVersion, isSingle, false);
                        }
                        catch (Exception e) {
                            if (sent) {
                                throw new Exception("El comprobante fue cancelado, pero ocurrió una excepción al enviarse vía mail:\n" + e.getMessage());
                            }
                            else {
                                throw new Exception("El comprobante fue cancelado, pero no enviado:\n" + e.getMessage());
                            }
                        }
                    }
                }
            }
            else {
                processAnnul(client, cfd, payrollCfdVersion, annulType);
                canceled = true;
            }
        }
        
        return canceled;
    }
    
    /**
     * Compute CFD: generate CFD complementary information, create CFD's XML, sign or cancel it and save CFD registry on server.
     * @param client ERP Client interface.
     * @param dps DPS registry.
     * @param xmlType CFD's XML type. Constants defined in SDataConstantsSys (i.e. TRNS_TP_XML_CFD, TRNS_TP_XML_CFDI_32, TRNS_TP_XML_CFDI_33, TRNS_TP_XML_CFDI_40).
     * @throws Exception
     */
    public static void computeCfdInvoice(final SClientInterface client, final SDataDps dps, final int xmlType) throws Exception {
        SCfdPacket packet = new SCfdPacket();
        packet.setCfdId(dps.getDbmsDataCfd() == null ? SLibConsts.UNDEFINED : dps.getDbmsDataCfd().getPkCfdId());
        packet.setDpsYearId(dps.getPkYearId());
        packet.setDpsDocId(dps.getPkDocId());
        packet.setAuxDataDps(dps);

        SCfdParams params = createCfdParams(client, dps);
        
        if (params != null) {
            dps.setAuxCfdParams(params);

            float cfdVersion = SLibConsts.UNDEFINED;
            cfd.ver2.DElementComprobante comprobanteCfd = null;
            cfd.ver32.DElementComprobante comprobanteCfdi32 = null;
            cfd.ver33.DElementComprobante comprobanteCfdi33 = null;
            cfd.ver40.DElementComprobante comprobanteCfdi40 = null;
            
            switch (xmlType) {
                case SDataConstantsSys.TRNS_TP_XML_CFD:
                    comprobanteCfd = (cfd.ver2.DElementComprobante) createCfdRootElement(client, dps);
                    cfdVersion = comprobanteCfd.getVersion();
                    
                    packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfd));
                    packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_EMITED);
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                    comprobanteCfdi32 = (cfd.ver32.DElementComprobante) createCfdi32RootElement(client, dps);
                    cfdVersion = comprobanteCfdi32.getVersion();
                    
                    packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi32));
                    packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW);
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                    comprobanteCfdi33 = (cfd.ver33.DElementComprobante) createCfdi33RootElement(client, dps);
                    cfdVersion = comprobanteCfdi33.getVersion();
                    
                    packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi33));
                    packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW);
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                    comprobanteCfdi40 = (cfd.ver40.DElementComprobante) createCfdi40RootElement(client, dps);
                    cfdVersion = comprobanteCfdi40.getVersion();
                    
                    packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi40));
                    packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW);
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }
            
            packet.setFkCfdTypeId(SDataConstantsSys.TRNS_TP_CFD_INV);
            packet.setFkXmlTypeId(xmlType);
            packet.setFkXmlDeliveryTypeId(params.getFkCfdAddendaTypeId() == SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA ? SModSysConsts.TRNS_TP_XML_DVY_WS_SOR : SModSysConsts.TRNS_TP_XML_DVY_NA);
            packet.setFkXmlDeliveryStatusId(SModSysConsts.TRNS_ST_XML_DVY_PENDING);
            packet.setFkUserDeliveryId(client.getSession().getUser().getPkUserId());
            
            if (packet.getCfdStringSigned().isEmpty()) {
                throw new Exception("No fue posible generar la cadena original del comprobante.");
            }
            
            packet.setCfdCertNumber(client.getCfdSignature(cfdVersion).getCertNumber());
            packet.setCfdSignature(client.getCfdSignature(cfdVersion).sign(packet.getCfdStringSigned(), SLibTimeUtilities.digestYear(dps.getDate())[0]));
            packet.setBaseXUuid(dps.getDbmsDataCfd() == null ? "" : dps.getDbmsDataCfd().getBaseXUuid());
                        
            switch (xmlType) {
                case SDataConstantsSys.TRNS_TP_XML_CFD:
                    comprobanteCfd.getAttSello().setString(packet.getCfdSignature());
                    packet.setAuxCfdRootElement(comprobanteCfd);
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                    comprobanteCfdi32.getAttSello().setString(packet.getCfdSignature());
                    packet.setAuxCfdRootElement(comprobanteCfdi32);
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                    comprobanteCfdi33.getAttSello().setString(packet.getCfdSignature());
                    packet.setAuxCfdRootElement(comprobanteCfdi33);
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                    comprobanteCfdi40.getAttSello().setString(packet.getCfdSignature());
                    packet.setAuxCfdRootElement(comprobanteCfdi40);
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }

            saveCfd(client, packet);
        }
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public static void computeCfdiPayroll(final SClientInterface client, final SHrsFormerPayroll hrsFormerPayroll, final boolean isRegenerateOnlyNonStampedCfdi) throws Exception {
        ArrayList<SDataCfd> formerPayrollCfds = null;
        ArrayList<SDataCfd> formerPayrollCfdsEmited = null;
        erp.mod.hrs.db.SDbFormerPayrollImport payrollImport = null;

        if (hrsFormerPayroll.isValidPayroll()) {
            formerPayrollCfdsEmited = new ArrayList<>();

            formerPayrollCfds = getPayrollCfds(client, SCfdConsts.CFDI_PAYROLL_VER_OLD, new int[] { hrsFormerPayroll.getPkNominaId() });

            for (SDataCfd cfd : formerPayrollCfds) {
                if (cfd.getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                    formerPayrollCfdsEmited.add(cfd);
                }
            }

            SHrsFormerPayroll hrsFormerPayrollDummy = new SHrsFormerPayroll(client);

            if (!formerPayrollCfdsEmited.isEmpty()) {
                hrsFormerPayrollDummy.setPkNominaId(formerPayrollCfdsEmited.get(0).getFkPayrollPayrollId_n());
                hrsFormerPayrollDummy.setFecha(formerPayrollCfdsEmited.get(0).getTimestamp());

                hrsFormerPayrollDummy.renderPayroll(formerPayrollCfdsEmited);
            }

            validateHrsFormerReceipts(client, hrsFormerPayroll, hrsFormerPayrollDummy, isRegenerateOnlyNonStampedCfdi);

            payrollImport = new erp.mod.hrs.db.SDbFormerPayrollImport();

            payrollImport.setPayrollId(hrsFormerPayroll.getPkNominaId());
            payrollImport.setRegenerateNonStampedCfdi(isRegenerateOnlyNonStampedCfdi);
            payrollImport.getCfdPackets().addAll(CfdPackets);

            SServerRequest request = new SServerRequest(SServerConstants.REQ_DB_ACTION_SAVE);
            request.setPacket(payrollImport);
            SServerResponse response = client.getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                throw new Exception(response.getMessage());
            }
            else {
                if (response.getResultType() != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception("Código de error al emitir el CFD: " + SLibConstants.MSG_ERR_DB_REG_SAVE + ".");
                }
                else {
                    client.showMsgBoxInformation("CFDI de nómina generados correctamente.");
                }
            }
        }
        else {
            throw new Exception("Los CFDI de nómina '" + hrsFormerPayroll.getPkNominaId() + "' no se generaron correctamente.");
        }
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public static void computeCfdiPayment(final SClientInterface client, final SDataCfdPayment cfdPayment, final int xmlType) throws Exception {
        SDataCfd cfd = cfdPayment.getDbmsDataCfd();
        SCfdPacket packet = new SCfdPacket();
        
        if (cfd == null) {
            packet.setCfdId(SLibConsts.UNDEFINED);
            packet.setCfdSeries("");
            packet.setCfdNumber(0);
            packet.setFkCompanyBranchId(SLibConsts.UNDEFINED);
            packet.setFkFactoringBankId(SLibConsts.UNDEFINED);
        }
        else {
            packet.setCfdId(cfd.getPkCfdId());
            packet.setCfdSeries(cfd.getSeries());
            packet.setCfdNumber(cfd.getNumber());
            packet.setFkCompanyBranchId(cfd.getFkCompanyBranchId_n());
            packet.setFkFactoringBankId(cfd.getFkFactoringBankId_n());
        }

        float cfdVersion = SLibConsts.UNDEFINED;
        cfd.ver33.DElementComprobante comprobanteCfdi33 = null;
        cfd.ver40.DElementComprobante comprobanteCfdi40 = null;

        switch (xmlType) {
            case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                comprobanteCfdi33 = (cfd.ver33.DElementComprobante) createCfdi33RootElement(client, cfdPayment);
                cfdVersion = comprobanteCfdi33.getVersion();

                packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi33));
                packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW);
                break;
            case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                comprobanteCfdi40 = (cfd.ver40.DElementComprobante) createCfdi40RootElement(client, cfdPayment);
                cfdVersion = comprobanteCfdi40.getVersion();

                packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi40));
                packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW);
                break;
            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }
        
        if (cfdPayment.getDbmsReceiptPayment() != null) {
            packet.setReceiptPaymentId(cfdPayment.getDbmsReceiptPayment().getPkReceiptId());
        }

        packet.setFkCfdTypeId(SDataConstantsSys.TRNS_TP_CFD_PAY_REC);
        packet.setFkXmlTypeId(xmlType);
        packet.setFkXmlDeliveryTypeId(SModSysConsts.TRNS_TP_XML_DVY_NA);
        packet.setFkXmlDeliveryStatusId(SModSysConsts.TRNS_ST_XML_DVY_PENDING);
        packet.setFkUserDeliveryId(client.getSession().getUser().getPkUserId());

        if (packet.getCfdStringSigned().isEmpty()) {
            throw new Exception("No fue posible generar la cadena original del comprobante.");
        }

        packet.setCfdCertNumber(client.getCfdSignature(cfdVersion).getCertNumber());
        packet.setCfdSignature(client.getCfdSignature(cfdVersion).sign(packet.getCfdStringSigned(), SLibTimeUtilities.digestYear(cfdPayment.getComprobanteFecha())[0]));
        packet.setBaseXUuid(cfd == null ? "" : cfd.getBaseXUuid());

        switch (xmlType) {
            case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                comprobanteCfdi33.getAttSello().setString(packet.getCfdSignature());
                packet.setAuxCfdRootElement(comprobanteCfdi33);
                break;
            case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                comprobanteCfdi40.getAttSello().setString(packet.getCfdSignature());
                packet.setAuxCfdRootElement(comprobanteCfdi40);
                break;
            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        saveCfd(client, packet);
    }
    
    public static void computeCfdiBol(final SClientInterface client, final SDbBillOfLading bol, final int xmlType) throws Exception {
        SDataCfd cfd = bol.getDataCfd();
        SCfdPacket packet = new SCfdPacket();
        
        if (cfd == null) {
            packet.setCfdId(SLibConsts.UNDEFINED);
            packet.setCfdSeries("");
            packet.setCfdNumber(0);
            packet.setFkCompanyBranchId(SLibConsts.UNDEFINED);
            packet.setFkFactoringBankId(SLibConsts.UNDEFINED);
        }
        else {
            packet.setCfdId(cfd.getPkCfdId());
            packet.setCfdSeries(cfd.getSeries());
            packet.setCfdNumber(cfd.getNumber());
            packet.setFkCompanyBranchId(SLibConsts.UNDEFINED);
            packet.setFkFactoringBankId(SLibConsts.UNDEFINED);
        }

        float cfdVersion = SLibConsts.UNDEFINED;
        cfd.ver33.DElementComprobante comprobanteCfdi33 = null;
        cfd.ver40.DElementComprobante comprobanteCfdi40 = null;

        switch (xmlType) {
            case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                bol.readBizPartner(client.getSession(), bol.getFkCompanyBranchId());
                comprobanteCfdi33 = (cfd.ver33.DElementComprobante) createCfdi33RootElement(client, bol);
                cfdVersion = comprobanteCfdi33.getVersion();

                packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi33));
                packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW);
                break;
            case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                bol.readBizPartner(client.getSession(), bol.getFkCompanyBranchId());
                comprobanteCfdi40 = (cfd.ver40.DElementComprobante) createCfdi40RootElement(client, bol);
                cfdVersion = comprobanteCfdi40.getVersion();

                packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi40));
                packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW);
                break;
            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        packet.setBillOfLadingId(bol.getPkBillOfLadingId());
        packet.setFkCfdTypeId(SDataConstantsSys.TRNS_TP_CFD_BOL);
        packet.setFkXmlTypeId(xmlType);
        packet.setFkXmlDeliveryTypeId(SModSysConsts.TRNS_TP_XML_DVY_NA);
        packet.setFkXmlDeliveryStatusId(SModSysConsts.TRNS_ST_XML_DVY_PENDING);
        packet.setFkUserDeliveryId(client.getSession().getUser().getPkUserId());

        if (packet.getCfdStringSigned().isEmpty()) {
            throw new Exception("No fue posible generar la cadena original del comprobante.");
        }

        packet.setCfdCertNumber(client.getCfdSignature(cfdVersion).getCertNumber());
        packet.setCfdSignature(client.getCfdSignature(cfdVersion).sign(packet.getCfdStringSigned(), SLibTimeUtilities.digestYear(bol.getComprobanteFecha())[0]));
        packet.setBaseXUuid(cfd == null ? "" : cfd.getBaseXUuid());

        switch (xmlType) {
            case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                comprobanteCfdi33.getAttSello().setString(packet.getCfdSignature());
                packet.setAuxCfdRootElement(comprobanteCfdi33);
                break;
            case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                comprobanteCfdi40.getAttSello().setString(packet.getCfdSignature());
                packet.setAuxCfdRootElement(comprobanteCfdi40);
                break;
            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        saveCfd(client, packet);
    }

    public static void  processAnnul(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int payrollCfdVersion, int annulType) throws Exception {
        for (SDataCfd cfd : cfds) {
            processAnnul(client, cfd, payrollCfdVersion, annulType);
        }
    }

    public static boolean deletePayroll(final SClientInterface client, int payrollId) throws Exception {
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT uuid " +
                "FROM trn_cfd WHERE fid_pay_pay_n = " + payrollId + " ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        while (resultSet.next()) {
            if (resultSet.getString("uuid").length() > 0) {
                throw new Exception("No se puede eliminar la nómina '" + payrollId + "':\n" + "-Existen recibos de nómina timbrados.");
            }
        }
        sql = "DELETE FROM trn_cfd WHERE fid_pay_pay_n = " + payrollId + " ";

        client.getSession().getStatement().execute(sql);

        return true;
    }

    public static boolean verifyCfdi(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int payrollCfdVersion) throws Exception {
        boolean valid = false;
        ArrayList<SDataCfd> cfdsVerify = null;
        
        cfdsVerify = new ArrayList<>();
        
        if (cfds.isEmpty()) {
            client.showMsgBoxInformation("No existen comprobantes para verificar.");
        }
        else {
            for (SDataCfd cfd : cfds) {
                // XXX 2018-01-08, Sergio Flores: Check this for statement, CFD are being added twice when if statement evaluates to true!:
                if (cfd.getIsProcessingWebService() || cfd.getIsProcessingStorageXml() || cfd.getIsProcessingStoragePdf()) {
                    cfdsVerify.add(cfd);
                }
                //cfdsVerify.add(cfd);
            }

            if (cfdsVerify.isEmpty()) {
                client.showMsgBoxInformation("No existen comprobantes para verificar.");
            }
            else {
                if (areCfdInconsistent(cfdsVerify)) {
                    int stampsAvailable = getStampsAvailable(client, cfdsVerify.get(0).getFkCfdTypeId(), cfdsVerify.get(0).getTimestamp(), 0);
                    SDialogCfdProcessing dialog = new SDialogCfdProcessing((SClient) client, "Procesamiento de verificación", SCfdConsts.REQ_VERIFY);
                    dialog.setFormParams(cfdsVerify, null, stampsAvailable, null, false, payrollCfdVersion, SModSysConsts.TRNU_TP_DPS_ANN_NA, "", "", false);
                    dialog.setVisible(true);
                }
            }
        }

        return valid;
    }

    public static boolean validateCfdi(final SClientInterface client, final SDataCfd cfd, final int payrollCfdVersion, final boolean showFinalMessage) throws Exception {
        boolean valid = false;

        if (cfd == null) {
            throw new Exception("No se proporcionó un CFD para ser validado.");
        }
        else if (cfd.getDocXml().isEmpty()) {
            throw new Exception("El comprobante no cuenta con archivo XML.");
        }
        else if (cfd.getDocXmlName().isEmpty()) {
            throw new Exception("El comprobante no cuenta con nombre de archivo XML.");
        }
        else if (!cfd.isCfdi()) {
            throw new Exception("El comprobante no es un CFDI.");
        }
        else {
            SDataPac pac = getPacForValidation(client, cfd);

            if (pac == null) {
                throw new Exception("No se pudo determinar cuál es el PAC para validar del CFDI.\nNo hay registro de intentos previos de timbrado para el CFDI.");
            }

            if (cfd.getCancellationStatus().isEmpty() && !(cfd.getIsProcessingWebService() || cfd.getIsProcessingStorageXml() || cfd.getIsProcessingStoragePdf())) {
                throw new Exception("No es necesario validar el CFDI.");
            }
            else {
                if (!cfd.getCancellationStatus().isEmpty() || cfd.getIsProcessingWebService()) {
                    // Open Sign & Cancel Log entry:
                    SignAndCancelLogEntryId = 0;
                    createSignCancelLogEntry(client, "", !cfd.isStamped() ? SCfdConsts.ACTION_CODE_VAL_SIGN : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_NA, cfd.getPkCfdId(), pac.getPkPacId());

                    if (!isSignedByFinkok(client, cfd)) {
                        // Request to user the file correct

                        if (!cfd.isStamped()) {
                            // Save file XML and set flag XML as correct:

                            valid = restoreCfdStamped(client, cfd, payrollCfdVersion, false);
                        }
                        else {
                            // Save file PDF and set flag PDF as correct:

                            valid = restoreCfdCancelAck(client, cfd, payrollCfdVersion, false);
                        }
                    }
                    else {
                        if (!cfd.isStamped()) {
                            valid = stampedCfdiFinkok(client, cfd, payrollCfdVersion);
                        }
                        else {
                            valid = getReceiptCancellationCfdi(client, cfd, payrollCfdVersion, SModSysConsts.TRNU_TP_DPS_ANN_NA, "", "", false);
                        }
                    }
                }
                else if (cfd.getIsProcessingStorageXml() || cfd.getIsProcessingStoragePdf()) {
                    if (cfd.getIsProcessingStorageXml()) {
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(client.getSessionXXX().getParamsCompany().getXmlBaseDirectory() + cfd.getDocXmlName()), "UTF-8"));
                        bw.write(cfd.getDocXml());
                        bw.close();
                    }

                    if (cfd.getIsProcessingStoragePdf()) {
                        computePrintCfd(client, cfd, payrollCfdVersion, SDataConstantsPrint.PRINT_MODE_PDF_FILE, 1);
                    }
                    
                    updateCfdProcessingFlags(client, cfd, false);
                    
                    if (showFinalMessage) {
                        client.showMsgBoxInformation(SLibConsts.MSG_PROCESS_FINISHED);
                    }
                }
            }
        }

        return valid;
    }
    
    /**
     * Restore CFD stamped.
     * @param client
     * @param cfd
     * @param payrollCfdVersion Supported constants: SCfdConsts.CFDI_PAYROLL_VER_OLD, SCfdConsts.CFDI_PAYROLL_VER_CUR or 0 when does not apply.
     * @param isRequestByUser
     * @return
     * @throws Exception 
     */
    public static boolean restoreCfdStamped(final SClientInterface client, final SDataCfd cfd, final int payrollCfdVersion, final boolean isRequestByUser) throws Exception {
        SDialogRestoreCfdi restoreCfdi = null;
        String fileXml = "";
        SDataPac pac = null;
        boolean isRestore = false;

        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del comprobante.");
        }
        else if (!cfd.isCfdi()) {
            throw new Exception("El comprobante solicitado no es un CFDI.");
        }
        else if (cfd.isStamped()) {
            throw new Exception("No es necesario restaurar el CFDI.");
        }

        pac = getPacForValidation(client, cfd);

        restoreCfdi = new SDialogRestoreCfdi(client, SCfdConsts.ACTION_CODE_PRC_SIGN, SCfdConsts.CFDI_FILE_XML);
        restoreCfdi.formReset();
        restoreCfdi.setFormVisible(true);

        if (restoreCfdi.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            fileXml = SXmlUtils.readXml(restoreCfdi.getFileXml());

            if (isRequestByUser) {
                // Open Sign & Cancel Log entry:
                SignAndCancelLogEntryId = 0;
                createSignCancelLogEntry(client, "", !cfd.isStamped() ? SCfdConsts.ACTION_CODE_VAL_SIGN : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_NA, cfd.getPkCfdId(), pac == null ? 0 : pac.getPkPacId());
            }

            if (!doesXmlBelongsToCfd(fileXml, cfd)) {
                createSignCancelLogEntry(client, "El archivo XML proporcionado no pertenece al CFDI seleccionado.", !cfd.isStamped() ? SCfdConsts.ACTION_CODE_VAL_SIGN : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_NA, cfd.getPkCfdId(), pac == null ? 0 : pac.getPkPacId());

                throw new Exception("El archivo XML proporcionado no pertenece al CFDI seleccionado.");
            }
            else {
                managementCfdi(client, cfd, SDataConstantsSys.TRNS_ST_DPS_EMITED, null, true, true, pac == null ? 0 : pac.getPkPacId(), payrollCfdVersion, 0, "", "", false, fileXml, false);
                isRestore = true;
            }
        }

        return isRestore;
    }

    /**
     * Restore CFD cancel acknowledgement.
     * @param client
     * @param cfd
     * @param isRequestByUser
     * @param payrollCfdVersion Supported constants: SCfdConsts.CFDI_PAYROLL_VER_OLD, SCfdConsts.CFDI_PAYROLL_VER_CUR or 0 when does not apply.
     * @return
     * @throws Exception 
     */
    public static boolean restoreCfdCancelAck(final SClientInterface client, final SDataCfd cfd, final int payrollCfdVersion, final boolean isRequestByUser) throws Exception {
        SDialogRestoreCfdi restoreCfdi = null;
        SDataPac pac = null;
        boolean isRestore = false;

        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del comprobante.");
        }
        else if (!cfd.isCfdi()) {
            throw new Exception("El comprobante solicitado no es un CFDI.");
        }
        else {
            if (isRequestByUser) {
                if (!cfd.getIsProcessingWebService()) {
                    if (!cfd.isStamped() || cfd.getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                        throw new Exception("No es necesario restaurar el acuse de cancelación.");
                    }
                }
                else if (!(cfd.isStamped() && cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED)) {
                    throw new Exception("No es necesario restaurar el acuse de cancelación.");
                }
            }
        }

        pac = getPacForValidation(client, cfd);

        restoreCfdi = new SDialogRestoreCfdi(client, SCfdConsts.ACTION_CODE_PRC_ANNUL, SCfdConsts.CFDI_FILE_PDF);
        restoreCfdi.formReset();
        restoreCfdi.setFormVisible(true);

        if (restoreCfdi.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            if (restoreCfdi.getFilePdf() != null) {

                if (isRequestByUser) {
                    // Open Sign & Cancel Log entry:
                    SignAndCancelLogEntryId = 0;
                    createSignCancelLogEntry(client, "", !cfd.isStamped() ? SCfdConsts.ACTION_CODE_VAL_SIGN : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_NA, cfd.getPkCfdId(), pac.getPkPacId());
                }

                saveCancelAckPdf(client, cfd, restoreCfdi.getFilePdf());
                managementCfdi(client, cfd, SDataConstantsSys.TRNS_ST_DPS_ANNULED, null, true, true, pac == null ? 0 : pac.getPkPacId(), payrollCfdVersion, 0, "", "", false, "", true);
                isRestore = true;
            }
            else {
                createSignCancelLogEntry(client, "El archivo proporcionado es erróneo.", !cfd.isStamped() ? SCfdConsts.ACTION_CODE_VAL_SIGN : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_NA, cfd.getPkCfdId(), pac.getPkPacId());
                throw new Exception("El archivo proporcionado es erróneo.");
            }
        }

        return isRestore;
    }

    private static boolean isSignedByFinkok(final SClientInterface client, final SDataCfd cfd) {
        SDataCfdPacType cfdPacType = null;
        boolean signedFnk = false;
        String sql = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT id_pac FROM trn_sign WHERE fid_cfd_n =  " + cfd.getPkCfdId() + " ";

            resultSet = client.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                signedFnk = resultSet.getInt("id_pac") == SModSysConsts.TRN_PAC_FNK;
            }
            else {
                cfdPacType = (SDataCfdPacType) SDataUtilities.readRegistry(client, SDataConstants.TRN_TP_CFD_PAC, new int[] { cfd.getFkCfdTypeId() }, SLibConstants.EXEC_MODE_SILENT);

                if (cfdPacType != null) {
                    signedFnk = cfdPacType.getFkPacId() == SModSysConsts.TRN_PAC_FNK;
                }
            }
        }
        catch(Exception e) {
            SLibUtils.showException(SCfdUtils.class.getName(), e);
        }

        return signedFnk;
    }

    private static SDataPac getPacForValidation(final SClientInterface client, final SDataCfd cfd) {
        SDataPac pac = null;
        String sql = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT fid_pac_n FROM trn_cfd_sign_log WHERE b_del = 0 AND fid_cfd = " + cfd.getPkCfdId() + " AND fid_pac_n IS NOT NULL ORDER BY id_log DESC LIMIT 1 ";

            resultSet = client.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                pac = (SDataPac) SDataUtilities.readRegistry(client, SDataConstants.TRN_PAC, new int[] { resultSet.getInt("fid_pac_n") }, SLibConstants.EXEC_MODE_SILENT);
            }
        }
        catch(Exception e) {
            SLibUtils.showException(SCfdUtils.class.getName(), e);
        }

        return pac;
    }

    public static boolean isNeedStamps(final SClientInterface client, final SDataCfd cfd, int action, final int pacId) {
        SDataCfdPacType cfdPacType = null;
        SDataPac pac = null;
        boolean need = false;

        try {
            cfdPacType = getCfdPacType(client, cfd.getFkCfdTypeId());

            if (cfdPacType != null || pacId != 0) {
                pac = (SDataPac) SDataUtilities.readRegistry(client, SDataConstants.TRN_PAC, new int[] { pacId == 0 ? cfdPacType.getFkPacId() : pacId }, SLibConstants.EXEC_MODE_SILENT);
            }

            if (pac != null) {
                if (action == SDbConsts.ACTION_ANNUL) {
                    need = (pac.getIsPrepayment() && pac.getIsChargedAnnulment());
                }
                else {
                    need = pac.getIsPrepayment();
                }
            }
        }
        catch(Exception e) {
            SLibUtils.showException(SCfdUtils.class.getName(), e);
        }

        return need;
    }

    /**
     * Get available stamps.
     * @param client ERP Client interface.
     * @param cfdType
     * @param date
     * @param pacId
     * @return Available stamps.
     */
    public static int getStampsAvailable(final SClientInterface client, final int cfdType, final Date date, final int pacId)  {
        SDataCfdPacType cfdPacType = null;
        SDataPac pac = null;
        String sql = "";
        ResultSet resultSet = null;
        int nStampsAvailable = 0;

        try {
            cfdPacType = getCfdPacType(client, cfdType);

            if (cfdPacType != null || pacId != 0) {
                pac = (SDataPac) SDataUtilities.readRegistry(client, SDataConstants.TRN_PAC, new int[] { pacId == 0 ? cfdPacType.getFkPacId() : pacId }, SLibConstants.EXEC_MODE_SILENT);
            }

            if (pac != null) {
                sql = "SELECT (SUM(mov_in) - SUM(mov_out)) AS f_stamp " +
                        "FROM trn_sign WHERE b_del = 0 AND id_pac = " + pac.getPkPacId() + " AND id_year = " + SLibTimeUtilities.digestYear(date)[0] + " AND dt <= '" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtilities.getEndOfYear(date)) + "' ";

                resultSet = client.getSession().getStatement().executeQuery(sql);
                if (resultSet.next()) {
                    nStampsAvailable = resultSet.getInt("f_stamp");
                }
            }
        }
        catch(Exception e) {
            SLibUtils.showException(SCfdUtils.class.getName(), e);
        }

        return nStampsAvailable;
    }

    /**
     * Remove one specified node of XML file
     * @param xml XML file as string
     * @param node Node to remove of XML
     * @return XML file as string without specified node
     */
    public static String removeNode(String xml, String node) {
        int nIndexBegin = 0;
        int nIndexEnd = 0;
        String xmlWithOutNode = "";

        nIndexBegin = xml.indexOf("<" + node + ">");
        nIndexEnd = xml.indexOf("</" + node + ">");

        xmlWithOutNode = xml;

        if (nIndexBegin != -1 && nIndexEnd != -1) {
            xmlWithOutNode = xml.replace(xml.substring(nIndexBegin, (nIndexEnd + node.length() + 3)), "");
        }

        return xmlWithOutNode;
    }

    @Deprecated
    public static cfd.DElement createCfdRootElement(final SClientInterface client, final SCfdXmlCfdi32 xmlCfd) throws Exception {
        double dTotalImptoRetenido = 0;
        double dTotalImptoTrasladado = 0;
        float fVersion = 0;
        SDbCfdBizPartner emisor = null;
        SDbCfdBizPartner receptor = null;
        SCfdDataBizPartner asociadoNegocios = null;

        if (SLibTimeUtilities.digestYear(xmlCfd.getComprobanteFecha())[0] <= 2011) {
            fVersion = DCfdConsts.CFD_VER_20;
        }
        else if (SLibTimeUtilities.digestYear(xmlCfd.getComprobanteFecha())[0] >= 2012) {
            fVersion = DCfdConsts.CFD_VER_22;
        }

        cfd.ver2.DElementComprobante comprobante = new cfd.ver2.DElementComprobante(fVersion);

        comprobante.getAttSerie().setString(xmlCfd.getComprobanteSerie());
        comprobante.getAttFolio().setString(xmlCfd.getComprobanteFolio());
        comprobante.getAttFecha().setDatetime(xmlCfd.getComprobanteFecha());
        comprobante.getAttNoCertificado().setString(client.getCfdSignature(fVersion).getCertNumber());
        comprobante.getAttCertificado().setString(client.getCfdSignature(fVersion).getCertBase64());

        if (xmlCfd.getComprobanteFormaDePagoPagos() <= 1) {
            comprobante.getAttFormaDePago().setOption(DAttributeOptionFormaDePago.CFD_UNA_EXHIBICION);
        }
        else {
            comprobante.getAttFormaDePago().setOption("" + xmlCfd.getComprobanteFormaDePagoPagos(), DAttributeOptionFormaDePago.CFD_PARCIALIDADES);
        }

        comprobante.getAttCondicionesDePago().setOption(xmlCfd.getComprobanteCondicionesDePago());
        comprobante.getAttSubTotal().setDouble(xmlCfd.getComprobanteSubtotal());
        comprobante.getAttTipoCambio().setDouble(xmlCfd.getComprobanteTipoCambio());
        comprobante.getAttMoneda().setString(xmlCfd.getComprobanteMoneda());
        comprobante.getAttTotal().setDouble(xmlCfd.getComprobanteTotal());

        comprobante.getAttTipoDeComprobante().setOption(xmlCfd.getComprobanteTipoDeComprobante());
        comprobante.getAttMetodoDePago().setString(xmlCfd.getComprobanteMetodoDePago());

        emisor = new SDbCfdBizPartner(client);
        emisor.setBizPartnerIds(xmlCfd.getEmisorId(), xmlCfd.getEmisorSucursalId());
        emisor.setIssuer(true, false);

        asociadoNegocios = emisor.getCfdDataBizPartner();
        asociadoNegocios.setVersion(fVersion);

        comprobante.setEltEmisor((cfd.ver2.DElementEmisor) asociadoNegocios.createRootElementEmisor());
        comprobante.getAttLugarExpedicion().setString(asociadoNegocios.getCfdLugarExpedicion());
        comprobante.getAttNumCtaPago().setString(xmlCfd.getComprobanteNumCtaPago());

        for (DElement regimen : xmlCfd.getElementsEmisorRegimenFiscal()) {
            comprobante.getEltEmisor().getEltHijosRegimenFiscal().add((cfd.ver2.DElementRegimenFiscal) regimen);
        }

        receptor = new SDbCfdBizPartner(client);
        receptor.setBizPartnerIds(xmlCfd.getReceptorId(), xmlCfd.getReceptorSucursalId());

        asociadoNegocios = receptor.getCfdDataBizPartner();

        comprobante.setEltReceptor((cfd.ver2.DElementReceptor) asociadoNegocios.createRootElementReceptor());

        for (SCfdDataConcepto concept : xmlCfd.getElementsConcepto()) {
            cfd.ver2.DElementConcepto concepto = new cfd.ver2.DElementConcepto();

            concepto.getAttNoIdentificacion().setString(concept.getNoIdentificacion());
            concepto.getAttUnidad().setString(concept.getUnidad());
            concepto.getAttCantidad().setDouble(concept.getCantidad());
            concepto.getAttDescripcion().setString(concept.getDescripcion());
            concepto.getAttValorUnitario().setDouble(concept.getValorUnitario());
            concepto.getAttImporte().setDouble(concept.getImporte());

            comprobante.getEltConceptos().getEltHijosConcepto().add(concepto);
        }

        // Taxes:

        cfd.ver2.DElementImpuestosRetenidos impuestosRetenidos = new cfd.ver2.DElementImpuestosRetenidos();
        cfd.ver2.DElementImpuestosTrasladados impuestosTrasladados = new cfd.ver2.DElementImpuestosTrasladados();

        cfd.ver2.DElementImpuestoRetencion impuestoRetencion = new cfd.ver2.DElementImpuestoRetencion();
        cfd.ver2.DElementImpuestoTraslado impuestoTraslado = new cfd.ver2.DElementImpuestoTraslado();

        for (SCfdDataImpuesto tax : xmlCfd.getElementsImpuestos(fVersion)) {
            switch (tax.getImpuestoTipo()) {
                case SModSysConsts.FINS_TP_TAX_RETAINED:
                    dTotalImptoRetenido += tax.getImporte();
                    impuestoRetencion.getAttImpuesto().setOption(tax.getImpuesto());
                    impuestoRetencion.getAttImporte().setDouble(tax.getImporte());

                    impuestosRetenidos.getEltHijosImpuestoRetenido().add(impuestoRetencion);
                    break;
                case SModSysConsts.FINS_TP_TAX_CHARGED:
                    dTotalImptoTrasladado += tax.getImporte();
                    impuestoTraslado.getAttImpuesto().setOption(DAttributeOptionImpuestoTraslado.CFD_IVA);
                    impuestoTraslado.getAttTasa().setDouble(tax.getTasa());
                    impuestoTraslado.getAttImporte().setDouble(tax.getImporte());

                    impuestosTrasladados.getEltHijosImpuestoTrasladado().add(impuestoTraslado);
                    break;
                default:
                    throw new Exception("Todos los tipos de impuestos deben ser conocidos (" + tax.getImpuestoTipo() + ").");
            }
        }

        if (impuestosRetenidos.getEltHijosImpuestoRetenido().size() > 0) {
            comprobante.getEltImpuestos().getAttTotalImpuestosRetenidos().setDouble(dTotalImptoRetenido);
            comprobante.getEltImpuestos().setEltOpcImpuestosRetenidos(impuestosRetenidos);
        }

        if (impuestosTrasladados.getEltHijosImpuestoTrasladado().size() > 0) {
            comprobante.getEltImpuestos().getAttTotalImpuestosTrasladados().setDouble(dTotalImptoTrasladado);
            comprobante.getEltImpuestos().setEltOpcImpuestosTrasladados(impuestosTrasladados);
        }

        if (xmlCfd.getElementAddenda() != null) {
            comprobante.setEltOpcAddenda((cfd.ver2.DElementAddenda) xmlCfd.getElementAddenda());
        }

        return comprobante;
    }

    @Deprecated
    public static cfd.DElement createCfdi32RootElement(final SClientInterface client, final SCfdXmlCfdi32 xmlCfdi) throws Exception {
        double dTotalImptoRetenido = 0;
        double dTotalImptoTrasladado = 0;
        boolean hasIntCommerceNode = false;
        SDbCfdBizPartner emisor = null;
        SDbCfdBizPartner receptor = null;
        SCfdDataBizPartner asociadoNegocios = null;
        cfd.DElement elementComplement = null;

        cfd.ver32.DElementComprobante comprobante = new cfd.ver32.DElementComprobante();

        comprobante.getAttSerie().setString(xmlCfdi.getComprobanteSerie());
        comprobante.getAttFolio().setString(xmlCfdi.getComprobanteFolio());
        comprobante.getAttFecha().setDatetime(xmlCfdi.getComprobanteFecha());
        comprobante.getAttNoCertificado().setString(client.getCfdSignature(DCfdConsts.CFDI_VER_32).getCertNumber());
        comprobante.getAttCertificado().setString(client.getCfdSignature(DCfdConsts.CFDI_VER_32).getCertBase64());

        if (xmlCfdi.getComprobanteFormaDePagoPagos() <= 1) {
            comprobante.getAttFormaDePago().setOption(DAttributeOptionFormaDePago.CFD_UNA_EXHIBICION);
        }
        else {
            comprobante.getAttFormaDePago().setOption("" + xmlCfdi.getComprobanteFormaDePagoPagos(), DAttributeOptionFormaDePago.CFD_PARCIALIDADES);
        }

        if (xmlCfdi.getCfdType() == SDataConstantsSys.TRNS_TP_CFD_PAYROLL) {
            comprobante.getAttCondicionesDePago().setOption("");
        }
        else {
            comprobante.getAttCondicionesDePago().setOption(xmlCfdi.getComprobanteCondicionesDePago());
        }
        comprobante.getAttSubTotal().setDouble(xmlCfdi.getComprobanteSubtotal());
        if (xmlCfdi.getComprobanteDescuento() != 0) {
            comprobante.getAttDescuento().setDouble(xmlCfdi.getComprobanteDescuento());
            comprobante.getAttMotivoDescuento().setString(xmlCfdi.getComprobanteMotivoDescuento());
        }
        if (xmlCfdi.getCfdType() != SDataConstantsSys.TRNS_TP_CFD_PAYROLL) {
            comprobante.getAttTipoCambio().setDouble(xmlCfdi.getComprobanteTipoCambio());
        }
        comprobante.getAttMoneda().setString(xmlCfdi.getComprobanteMoneda());
        comprobante.getAttTotal().setDouble(xmlCfdi.getComprobanteTotal());

        comprobante.getAttTipoDeComprobante().setOption(xmlCfdi.getComprobanteTipoDeComprobante());
        String metodoDePago = ((SSessionCustom) client.getSession().getSessionCustom()).getCfdXmlCatalogs().getEntryCode(SDataConstantsSys.TRNS_CFD_CAT_PAY_WAY, xmlCfdi.getComprobanteMetodoDePago());
        comprobante.getAttMetodoDePago().setString(metodoDePago.isEmpty() ? "NA" : metodoDePago);

        elementComplement = xmlCfdi.getElementComplemento();

        if (elementComplement != null) {
            hasIntCommerceNode = ((cfd.ver32.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior") != null;
        }
        
        emisor = new SDbCfdBizPartner(client);
        emisor.setBizPartnerIds(xmlCfdi.getEmisorId(), xmlCfdi.getEmisorSucursalId());
        emisor.setIssuingBizPartnerIds(xmlCfdi.getEmisorId(), xmlCfdi.getEmisorSucursalId());
        emisor.setIssuer(true, hasIntCommerceNode);

        asociadoNegocios = emisor.getCfdDataBizPartner();
        asociadoNegocios.setIsCfdiWithIntCommerce(hasIntCommerceNode);
        asociadoNegocios.setVersion(DCfdConsts.CFDI_VER_32);
        asociadoNegocios.setCfdiType(xmlCfdi.getCfdType());
        asociadoNegocios.setIsStateCodeAssociate(SLocUtils.hasStates(client.getSession(), asociadoNegocios.getBizPartnerCountryId()));

        comprobante.setEltEmisor((cfd.ver32.DElementEmisor) asociadoNegocios.createRootElementEmisor());
        comprobante.getAttLugarExpedicion().setString(asociadoNegocios.getCfdLugarExpedicion());
        comprobante.getAttNumCtaPago().setString(xmlCfdi.getComprobanteNumCtaPago());

        for (DElement regimen : xmlCfdi.getElementsEmisorRegimenFiscal()) {
            comprobante.getEltEmisor().getEltHijosRegimenFiscal().add((cfd.ver32.DElementRegimenFiscal) regimen);
        }

        receptor = new SDbCfdBizPartner(client);
        receptor.setBizPartnerIds(xmlCfdi.getReceptorId(), xmlCfdi.getReceptorSucursalId());

        asociadoNegocios = receptor.getCfdDataBizPartner();
        asociadoNegocios.setIsCfdiWithIntCommerce(hasIntCommerceNode);
        asociadoNegocios.setVersion(DCfdConsts.CFDI_VER_32);
        asociadoNegocios.setCfdiType(xmlCfdi.getCfdType());
        asociadoNegocios.setIsStateCodeAssociate(SLocUtils.hasStates(client.getSession(), asociadoNegocios.getBizPartnerCountryId()));

        comprobante.setEltReceptor((cfd.ver32.DElementReceptor) asociadoNegocios.createRootElementReceptor());
        
        if (elementComplement != null && hasIntCommerceNode) {
            ((cfd.ver3.cce11.DElementComercioExterior) ((cfd.ver32.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior")).setEltReceptor(asociadoNegocios.createRootElementReceptorIntCommerce());
        }

        for (SCfdDataConcepto concept : xmlCfdi.getElementsConcepto()) {
            cfd.ver32.DElementConcepto concepto = new cfd.ver32.DElementConcepto();

            concepto.getAttNoIdentificacion().setString(concept.getNoIdentificacion());
            concepto.getAttUnidad().setString(concept.getUnidad());
            if (xmlCfdi.getCfdType() == SDataConstantsSys.TRNS_TP_CFD_PAYROLL) {
                concepto.getAttCantidad().setDecimals(0);
            }
            concepto.getAttCantidad().setDouble(concept.getCantidad());
            concepto.getAttDescripcion().setString(concept.getDescripcion());
            concepto.getAttValorUnitario().setDouble(concept.getValorUnitario());
            concepto.getAttImporte().setDouble(concept.getImporte());

            comprobante.getEltConceptos().getEltHijosConcepto().add(concepto);
        }

        // Taxes:

        ArrayList<SCfdDataImpuesto> taxes = xmlCfdi.getElementsImpuestos(DCfdConsts.CFDI_VER_32);
        
        if (taxes != null) {
            cfd.ver32.DElementImpuestosRetenidos impuestosRetenidos = new cfd.ver32.DElementImpuestosRetenidos();
            cfd.ver32.DElementImpuestosTrasladados impuestosTrasladados = new cfd.ver32.DElementImpuestosTrasladados();

            for (SCfdDataImpuesto tax : taxes) {
                cfd.ver32.DElementImpuestoRetencion impuestoRetencion = new cfd.ver32.DElementImpuestoRetencion();
                cfd.ver32.DElementImpuestoTraslado impuestoTraslado = new cfd.ver32.DElementImpuestoTraslado();

                switch (tax.getImpuestoTipo()) {
                    case SModSysConsts.FINS_TP_TAX_RETAINED:
                        dTotalImptoRetenido += tax.getImporte();
                        impuestoRetencion.getAttImpuesto().setOption(tax.getImpuesto());
                        impuestoRetencion.getAttImporte().setDouble(tax.getImporte());

                        impuestosRetenidos.getEltHijosImpuestoRetenido().add(impuestoRetencion);
                        break;
                        
                    case SModSysConsts.FINS_TP_TAX_CHARGED:
                        dTotalImptoTrasladado += tax.getImporte();
                        impuestoTraslado.getAttImpuesto().setOption(tax.getImpuesto());
                        impuestoTraslado.getAttTasa().setDouble(tax.getTasa());
                        impuestoTraslado.getAttImporte().setDouble(tax.getImporte());

                        impuestosTrasladados.getEltHijosImpuestoTrasladado().add(impuestoTraslado);
                        break;
                        
                    default:
                        throw new Exception("Todos los tipos de impuestos deben ser conocidos (" + tax.getImpuestoTipo() + ").");
                }
            }

            if (impuestosRetenidos.getEltHijosImpuestoRetenido().size() > 0) {
                comprobante.getEltImpuestos().getAttTotalImpuestosRetenidos().setDouble(dTotalImptoRetenido);
                comprobante.getEltImpuestos().setEltOpcImpuestosRetenidos(impuestosRetenidos);
            }

            if (impuestosTrasladados.getEltHijosImpuestoTrasladado().size() > 0) {
                comprobante.getEltImpuestos().getAttTotalImpuestosTrasladados().setDouble(dTotalImptoTrasladado);
                comprobante.getEltImpuestos().setEltOpcImpuestosTrasladados(impuestosTrasladados);
            }
        }
        
        if (elementComplement != null) {
            comprobante.setEltOpcComplemento((cfd.ver32.DElementComplemento) elementComplement);
        }

        if (xmlCfdi.getElementAddenda() != null) {
            comprobante.setEltOpcAddenda((cfd.ver3.DElementAddenda) xmlCfdi.getElementAddenda());
        }

        if (xmlCfdi.getCfdType() == SDataConstantsSys.TRNS_TP_CFD_PAYROLL) {
            if (elementComplement == null || comprobante.getEltOpcComplemento().getElements().isEmpty()) {
                comprobante = null;
                throw new Exception("Error al generar el complemento nómina o el complemento no existe.");
            }
        }
        
        validateCorrectnessXml(comprobante);

        return comprobante;
    }
    
    @Deprecated
    public static cfd.DElement createCfdi33RootElement(final SClientInterface client, final SCfdXmlCfdi33 xmlCfdi) throws Exception {
        // Comprobante:
        
        cfd.ver33.DElementComprobante comprobante = new cfd.ver33.DElementComprobante();

        comprobante.getAttSerie().setString(xmlCfdi.getComprobanteSerie());
        comprobante.getAttFolio().setString(xmlCfdi.getComprobanteFolio());
        comprobante.getAttFecha().setDatetime(xmlCfdi.getComprobanteFecha());
        //comprobante.getAttSello(...
        comprobante.getAttFormaPago().setString(xmlCfdi.getComprobanteFormaPago());
        comprobante.getAttNoCertificado().setString(client.getCfdSignature(DCfdConsts.CFDI_VER_33).getCertNumber());
        comprobante.getAttCertificado().setString(client.getCfdSignature(DCfdConsts.CFDI_VER_33).getCertBase64());
        comprobante.getAttCondicionesDePago().setString(xmlCfdi.getComprobanteCondicionesPago());
        comprobante.getAttSubTotal().setDouble(xmlCfdi.getComprobanteSubtotal());
        comprobante.getAttDescuento().setDouble(xmlCfdi.getComprobanteDescuento());
        comprobante.getAttMoneda().setString(xmlCfdi.getComprobanteMoneda());
        if (xmlCfdi.getComprobanteMoneda().compareTo(SModSysConsts.FINS_FISCAL_CUR_MXN_NAME) != 0 && xmlCfdi.getComprobanteMoneda().compareTo(SModSysConsts.FINS_FISCAL_CUR_XXX_NAME) != 0) {
            comprobante.getAttTipoCambio().setDouble(xmlCfdi.getComprobanteTipoCambio());
        }
        comprobante.getAttTotal().setDouble(xmlCfdi.getComprobanteTotal());
        if (xmlCfdi.getComprobanteMoneda().equals(DCfdi33Catalogs.ClaveMonedaXxx)) {
            comprobante.getAttSubTotal().setDecimals(0);
            comprobante.getAttTotal().setDecimals(0);
        }
        comprobante.getAttTipoDeComprobante().setString(xmlCfdi.getComprobanteTipoComprobante());
        comprobante.getAttMetodoPago().setString(xmlCfdi.getComprobanteMetodoPago());
        comprobante.getAttLugarExpedicion().setString(xmlCfdi.getComprobanteLugarExpedicion());
        comprobante.getAttConfirmacion().setString(xmlCfdi.getComprobanteConfirmacion());
        
        if (xmlCfdi.getCfdType() == SDataConstantsSys.TRNS_TP_CFD_PAY_REC) {
            comprobante.getAttSubTotal().setDecimals(0);
            comprobante.getAttTotal().setDecimals(0);
        }
        
        if (!xmlCfdi.getCfdiRelacionados33TipoRelacion().isEmpty() && !xmlCfdi.getCfdiRelacionados33().isEmpty()) {
            cfd.ver33.DElementCfdiRelacionados cfdiRelacionados = new DElementCfdiRelacionados();
            cfdiRelacionados.getAttTipoRelacion().setString(xmlCfdi.getCfdiRelacionados33TipoRelacion());
            
            for (String uuid : xmlCfdi.getCfdiRelacionados33()) {
                cfd.ver33.DElementCfdiRelacionado cfdiRelacionado = new cfd.ver33.DElementCfdiRelacionado();
                cfdiRelacionado.getAttUuid().setString(uuid);
                cfdiRelacionados.getEltCfdiRelacionados().add(cfdiRelacionado);
            }
            
            comprobante.setEltOpcCfdiRelacionados(cfdiRelacionados);
        }
        
        boolean hasIntCommerceComplement = false; // this info is required at this point to properly set data of nodes Emisor and Receptor
        cfd.DElement elementComplement = xmlCfdi.getElementComplemento();

        if (elementComplement != null) {
            hasIntCommerceComplement = ((cfd.ver33.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior") != null;
        }
        
        // Emisor:
        
        SDbCfdBizPartner emisor = new SDbCfdBizPartner(client);
        emisor.setBizPartnerIds(xmlCfdi.getEmisorId(), xmlCfdi.getEmisorSucursalId());
        emisor.setIssuingBizPartnerIds(xmlCfdi.getEmisorId(), xmlCfdi.getEmisorSucursalId());
        emisor.setIssuer(true, hasIntCommerceComplement);

        SCfdDataBizPartner emisorCfd = emisor.getCfdDataBizPartner();
        emisorCfd.setIsCfdiWithIntCommerce(hasIntCommerceComplement);
        emisorCfd.setVersion(DCfdConsts.CFDI_VER_33);
        emisorCfd.setCfdiType(xmlCfdi.getCfdType());

        cfd.ver33.DElementEmisor elementEmisor = (cfd.ver33.DElementEmisor) emisorCfd.createRootElementEmisor();
        
        elementEmisor.getAttRegimenFiscal().setString(xmlCfdi.getEmisorRegimenFiscal());
        
        if (hasIntCommerceComplement) {
            ((cfd.ver3.cce11.DElementComercioExterior) ((cfd.ver33.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior")).setEltEmisor(emisorCfd.createRootElementEmisorIntCommerce());
        }

        comprobante.setEltEmisor(elementEmisor);
        
        // Receptor:

        SDbCfdBizPartner receptor = new SDbCfdBizPartner(client);
        receptor.setBizPartnerIds(xmlCfdi.getReceptorId(), xmlCfdi.getReceptorSucursalId());

        SCfdDataBizPartner receptorCfd = receptor.getCfdDataBizPartner();
        receptorCfd.setIsCfdiWithIntCommerce(hasIntCommerceComplement);
        receptorCfd.setVersion(DCfdConsts.CFDI_VER_33);
        receptorCfd.setCfdiType(xmlCfdi.getCfdType());

        cfd.ver33.DElementReceptor elementReceptor = (cfd.ver33.DElementReceptor) receptorCfd.createRootElementReceptor();
        
        elementReceptor.getAttUsoCFDI().setString(xmlCfdi.getReceptorUsoCFDI());
        
        if (hasIntCommerceComplement) {
            ((cfd.ver3.cce11.DElementComercioExterior) ((cfd.ver33.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior")).setEltReceptor(receptorCfd.createRootElementReceptorIntCommerce());
            
            if (xmlCfdi.getDestinatarioId() != 0) {
                if (xmlCfdi.getDestinatarioSucursalId() != 0 && xmlCfdi.getDestinatarioDomicilioId() != 0) {
                    // get business partner as addressee:
                    
                    SDbCfdBizPartner destinatario = new SDbCfdBizPartner(client);
                    destinatario.setBizPartnerIds(xmlCfdi.getDestinatarioId(), xmlCfdi.getDestinatarioSucursalId(), xmlCfdi.getDestinatarioDomicilioId());

                    SCfdDataBizPartner destinatarioCfd = destinatario.getCfdDataBizPartner();
                    destinatarioCfd.setIsCfdiWithIntCommerce(hasIntCommerceComplement);
                    destinatarioCfd.setVersion(DCfdConsts.CFDI_VER_33);
                    destinatarioCfd.setCfdiType(xmlCfdi.getCfdType());

                    ((cfd.ver3.cce11.DElementComercioExterior) ((cfd.ver33.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior")).setEltDestinatario(destinatarioCfd.createRootElementDestinatarioIntCommerce());
                }
                else {
                    SDataBizPartnerAddressee addressee = (SDataBizPartnerAddressee) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BP_ADDEE, new int[] { xmlCfdi.getDestinatarioId() } , SLibConstants.EXEC_MODE_SILENT);
                    
                    ((cfd.ver3.cce11.DElementComercioExterior) ((cfd.ver33.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior")).setEltDestinatario(addressee.createRootElementDestinatarioIntCommerce(DCfdConsts.CFDI_VER_33));
                }
            }
        }
        
        comprobante.setEltReceptor(elementReceptor);
        
        // Conceptos:

        for (SCfdDataConcepto concept : xmlCfdi.getElementsConcepto()) {
            concept.setHasIntCommerceComplement(hasIntCommerceComplement);
            comprobante.getEltConceptos().getEltConceptos().add(concept.createRootElementConcept33());
        }

        // Impuestos:

        if (!SLibUtils.belongsTo(xmlCfdi.getCfdType(), new int[] { SDataConstantsSys.TRNS_TP_CFD_PAYROLL, SDataConstantsSys.TRNS_TP_CFD_PAY_REC, SDataConstantsSys.TRNS_TP_CFD_BOL })) {
            boolean exemptTaxesAvailable = false;
            double dTotalImptoRetenido = 0;
            double dTotalImptoTrasladado = 0;
            cfd.ver33.DElementImpuestosRetenciones impuestosRetenciones = new cfd.ver33.DElementImpuestosRetenciones();
            cfd.ver33.DElementImpuestosTraslados impuestosTrasladados = new cfd.ver33.DElementImpuestosTraslados();

            for (SCfdDataImpuesto impuesto : xmlCfdi.getElementsImpuestos(DCfdConsts.CFDI_VER_33)) {
                switch (impuesto.getImpuestoTipo()) {
                    case SModSysConsts.FINS_TP_TAX_RETAINED:
                        dTotalImptoRetenido += impuesto.getImporte();
                        impuestosRetenciones.getEltImpuestoRetenciones().add((cfd.ver33.DElementImpuestoRetencion) impuesto.createRootElementImpuesto33());
                        break;
                    case SModSysConsts.FINS_TP_TAX_CHARGED:
                        if (impuesto.getTipoFactor().compareToIgnoreCase(DCfdi33Catalogs.FAC_TP_EXENTO) == 0) {
                            exemptTaxesAvailable = true;
                        }
                        else {
                            dTotalImptoTrasladado += impuesto.getImporte();
                            impuestosTrasladados.getEltImpuestoTrasladados().add((cfd.ver33.DElementImpuestoTraslado) impuesto.createRootElementImpuesto33());
                        }
                        break;
                    default:
                        throw new Exception("Todos los tipos de impuestos deben ser conocidos (" + impuesto.getImpuestoTipo() + ").");
                }
            }

            if (!impuestosRetenciones.getEltImpuestoRetenciones().isEmpty()) {
                if (comprobante.getEltOpcImpuestos() == null) {
                    comprobante.setEltOpcImpuestos(new DElementImpuestos(comprobante));
                }
                
                comprobante.getEltOpcImpuestos().getAttTotalImpuestosRetenidos().setDouble(dTotalImptoRetenido);
                
                comprobante.getEltOpcImpuestos().setEltOpcImpuestosRetenciones(impuestosRetenciones);
            }

            if (!impuestosTrasladados.getEltImpuestoTrasladados().isEmpty() || exemptTaxesAvailable) {
                if (comprobante.getEltOpcImpuestos() == null) {
                    comprobante.setEltOpcImpuestos(new DElementImpuestos(comprobante));
                }
                
                comprobante.getEltOpcImpuestos().getAttTotalImpuestosTraslados().setDouble(dTotalImptoTrasladado);
                if (exemptTaxesAvailable) {
                    comprobante.getEltOpcImpuestos().getAttTotalImpuestosTraslados().setCanBeZero(true);
                }
                
                if (!impuestosTrasladados.getEltImpuestoTrasladados().isEmpty()) {
                    comprobante.getEltOpcImpuestos().setEltOpcImpuestosTrasladados(impuestosTrasladados);
                }
            }
        }
        
        if (SLibUtils.belongsTo(xmlCfdi.getCfdType(), new int[] { SDataConstantsSys.TRNS_TP_CFD_PAYROLL, SDataConstantsSys.TRNS_TP_CFD_PAY_REC, SDataConstantsSys.TRNS_TP_CFD_BOL })) {
            if (elementComplement == null) {
                throw new Exception("Error al generar el complemento nómina o el complemento no existe.");
            }
        }
        
        if (elementComplement != null) {
            comprobante.setEltOpcComplemento((cfd.ver33.DElementComplemento) elementComplement);
        }

        if (xmlCfdi.getElementAddenda() != null) {
            comprobante.setEltOpcAddenda((cfd.ver3.DElementAddenda) xmlCfdi.getElementAddenda());
        }

        validateCorrectnessXml(comprobante);

        return comprobante;
    }
    
    public static cfd.DElement createCfdi40RootElement(final SClientInterface client, final SCfdXmlCfdi40 xmlCfdi) throws Exception {
        // Comprobante:
        
        boolean isGlobal = xmlCfdi.getElementInformacionGlobal() != null;
        
        cfd.ver40.DElementComprobante comprobante = new cfd.ver40.DElementComprobante();

        comprobante.getAttSerie().setString(xmlCfdi.getComprobanteSerie());
        comprobante.getAttFolio().setString(xmlCfdi.getComprobanteFolio());
        comprobante.getAttFecha().setDatetime(xmlCfdi.getComprobanteFecha());
        //comprobante.getAttSello(...
        if (xmlCfdi.getComprobanteFormaPago() != null) {
            comprobante.getAttFormaPago().setString(xmlCfdi.getComprobanteFormaPago());
        }
        comprobante.getAttNoCertificado().setString(client.getCfdSignature(DCfdConsts.CFDI_VER_40).getCertNumber());
        comprobante.getAttCertificado().setString(client.getCfdSignature(DCfdConsts.CFDI_VER_40).getCertBase64());
        if (!isGlobal) comprobante.getAttCondicionesDePago().setString(xmlCfdi.getComprobanteCondicionesPago());
        comprobante.getAttSubTotal().setDouble(xmlCfdi.getComprobanteSubtotal());
        comprobante.getAttDescuento().setDouble(xmlCfdi.getComprobanteDescuento());
        comprobante.getAttMoneda().setString(xmlCfdi.getComprobanteMoneda());
        if (xmlCfdi.getComprobanteMoneda().compareTo(SModSysConsts.FINS_FISCAL_CUR_MXN_NAME) != 0 && xmlCfdi.getComprobanteMoneda().compareTo(SModSysConsts.FINS_FISCAL_CUR_XXX_NAME) != 0) {
            comprobante.getAttTipoCambio().setDouble(xmlCfdi.getComprobanteTipoCambio());
        }
        comprobante.getAttTotal().setDouble(xmlCfdi.getComprobanteTotal());
        if (xmlCfdi.getComprobanteMoneda().equals(DCfdi40Catalogs.ClaveMonedaXxx)) {
            comprobante.getAttSubTotal().setDecimals(0);
            comprobante.getAttTotal().setDecimals(0);
        }
        comprobante.getAttTipoDeComprobante().setString(xmlCfdi.getComprobanteTipoComprobante());
        comprobante.getAttExportacion().setString(xmlCfdi.getComprobanteExportacion());
        comprobante.getAttMetodoPago().setString(xmlCfdi.getComprobanteMetodoPago());
        comprobante.getAttLugarExpedicion().setString(xmlCfdi.getComprobanteLugarExpedicion());
        comprobante.getAttConfirmacion().setString(xmlCfdi.getComprobanteConfirmacion());
        
        if (xmlCfdi.getCfdType() == SDataConstantsSys.TRNS_TP_CFD_PAY_REC) {
            comprobante.getAttSubTotal().setDecimals(0);
            comprobante.getAttTotal().setDecimals(0);
        }
        
        if (xmlCfdi.getElementInformacionGlobal() != null) {
            comprobante.setEltOpcInformacionGlobal((DElementInformacionGlobal) xmlCfdi.getElementInformacionGlobal());
        }
        
        if (xmlCfdi.getCfdiRelacionados() != null && xmlCfdi.getCfdiRelacionados().getRelatedDocuments() != null &&
                !xmlCfdi.getCfdiRelacionados().getRelatedDocuments().isEmpty()) {
            ArrayList<cfd.ver40.DElementCfdiRelacionados> arrCfdiRelacionados = new ArrayList<>();
            for (SRowRelatedDocument row : xmlCfdi.getCfdiRelacionados().getRelatedDocuments()) {
                SRowRelatedDocument relatedDocument = row;
                cfd.ver40.DElementCfdiRelacionados cfdiRelacionados = new cfd.ver40.DElementCfdiRelacionados();
                cfdiRelacionados.getAttTipoRelacion().setString(relatedDocument.getRelationTypeId());
               for (String uuid : relatedDocument.getDocUuids().trim().split(",")) { 
                    cfd.ver40.DElementCfdiRelacionado cfdiRelacionado = new cfd.ver40.DElementCfdiRelacionado();
                    cfdiRelacionado.getAttUuid().setString(uuid);
                    cfdiRelacionados.getEltCfdiRelacionados().add(cfdiRelacionado);
                } 
               arrCfdiRelacionados.add(cfdiRelacionados);
            }
            comprobante.setEltOpcCfdiRelacionados(arrCfdiRelacionados);
        }
        
        boolean hasIntCommerceComplement = false; // this info is required at this point to properly set data of nodes Emisor and Receptor
        cfd.DElement elementComplement = xmlCfdi.getElementComplemento();

        if (elementComplement != null) {
            hasIntCommerceComplement = ((cfd.ver40.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior") != null;
        }
        
        // Emisor:
        
        SDbCfdBizPartner emisor = new SDbCfdBizPartner(client);
        emisor.setBizPartnerIds(xmlCfdi.getEmisorId(), xmlCfdi.getEmisorSucursalId());
        emisor.setIssuingBizPartnerIds(xmlCfdi.getEmisorId(), xmlCfdi.getEmisorSucursalId());
        emisor.setIssuer(true, hasIntCommerceComplement);

        SCfdDataBizPartner emisorCfd = emisor.getCfdDataBizPartner();
        emisorCfd.setIsCfdiWithIntCommerce(hasIntCommerceComplement);
        emisorCfd.setVersion(DCfdConsts.CFDI_VER_40);
        emisorCfd.setCfdiType(xmlCfdi.getCfdType());

        cfd.ver40.DElementEmisor elementEmisor = (cfd.ver40.DElementEmisor) emisorCfd.createRootElementEmisor();
        
        elementEmisor.getAttRegimenFiscal().setString(xmlCfdi.getEmisorRegimenFiscal());
        
        if (hasIntCommerceComplement) {
            ((cfd.ver3.cce11.DElementComercioExterior) ((cfd.ver40.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior")).setEltEmisor(emisorCfd.createRootElementEmisorIntCommerce());
        }

        comprobante.setEltEmisor(elementEmisor);
        
        // Receptor:

        SDbCfdBizPartner receptor = new SDbCfdBizPartner(client);
        receptor.setBizPartnerIds(xmlCfdi.getReceptorId(), xmlCfdi.getReceptorSucursalId());

        SCfdDataBizPartner receptorCfd = receptor.getCfdDataBizPartner();
        receptorCfd.setIsCfdiWithIntCommerce(hasIntCommerceComplement);
        receptorCfd.setVersion(DCfdConsts.CFDI_VER_40);
        receptorCfd.setCfdiType(xmlCfdi.getCfdType());

        cfd.ver40.DElementReceptor elementReceptor = (cfd.ver40.DElementReceptor) receptorCfd.createRootElementReceptor();
        
        if (isGlobal || elementReceptor.getAttRfc().getString().equals(DCfdConsts.RFC_GEN_INT)) {
            elementReceptor.getAttDomicilioFiscalReceptor().setString(xmlCfdi.getComprobanteLugarExpedicion());
        }
        elementReceptor.getAttRegimenFiscalReceptor().setString(xmlCfdi.getReceptorRegimenFiscal());
        elementReceptor.getAttUsoCFDI().setString(xmlCfdi.getReceptorUsoCFDI());
        
        if (hasIntCommerceComplement) {
            ((cfd.ver3.cce11.DElementComercioExterior) ((cfd.ver40.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior")).setEltReceptor(receptorCfd.createRootElementReceptorIntCommerce());
            
            if (xmlCfdi.getDestinatarioId() != 0) {
                if (xmlCfdi.getDestinatarioSucursalId() != 0 && xmlCfdi.getDestinatarioDomicilioId() != 0) {
                    // get business partner as addressee:
                    
                    SDbCfdBizPartner destinatario = new SDbCfdBizPartner(client);
                    destinatario.setBizPartnerIds(xmlCfdi.getDestinatarioId(), xmlCfdi.getDestinatarioSucursalId(), xmlCfdi.getDestinatarioDomicilioId());

                    SCfdDataBizPartner destinatarioCfd = destinatario.getCfdDataBizPartner();
                    destinatarioCfd.setIsCfdiWithIntCommerce(hasIntCommerceComplement);
                    destinatarioCfd.setVersion(DCfdConsts.CFDI_VER_40);
                    destinatarioCfd.setCfdiType(xmlCfdi.getCfdType());

                    ((cfd.ver3.cce11.DElementComercioExterior) ((cfd.ver40.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior")).setEltDestinatario(destinatarioCfd.createRootElementDestinatarioIntCommerce());
                }
                else {
                    SDataBizPartnerAddressee addressee = (SDataBizPartnerAddressee) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BP_ADDEE, new int[] { xmlCfdi.getDestinatarioId() } , SLibConstants.EXEC_MODE_SILENT);
                    
                    ((cfd.ver3.cce11.DElementComercioExterior) ((cfd.ver40.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior")).setEltDestinatario(addressee.createRootElementDestinatarioIntCommerce(DCfdConsts.CFDI_VER_40));
                }
            }
        }
        
        comprobante.setEltReceptor(elementReceptor);
        
        // Conceptos:

        for (SCfdDataConcepto concept : xmlCfdi.getElementsConcepto()) {
            concept.setHasIntCommerceComplement(hasIntCommerceComplement);
            comprobante.getEltConceptos().getEltConceptos().add(concept.createRootElementConcept40(isGlobal));
        }

        // Impuestos:

        if (!SLibUtils.belongsTo(xmlCfdi.getCfdType(), new int[] { SDataConstantsSys.TRNS_TP_CFD_PAYROLL, SDataConstantsSys.TRNS_TP_CFD_PAY_REC, SDataConstantsSys.TRNS_TP_CFD_BOL })) {
            boolean exemptTaxesAvailable = false;
            double dTotalImptoRetenido = 0;
            double dTotalImptoTrasladado = 0;
            cfd.ver40.DElementImpuestosRetenciones impuestosRetenciones = new cfd.ver40.DElementImpuestosRetenciones();
            cfd.ver40.DElementImpuestosTraslados impuestosTrasladados = new cfd.ver40.DElementImpuestosTraslados();

            for (SCfdDataImpuesto impuesto : xmlCfdi.getElementsImpuestos(DCfdConsts.CFDI_VER_40)) {
                switch (impuesto.getImpuestoTipo()) {
                    case SModSysConsts.FINS_TP_TAX_RETAINED:
                        dTotalImptoRetenido += impuesto.getImporte();
                        impuestosRetenciones.getEltImpuestoRetenciones().add((cfd.ver40.DElementImpuestoRetencion) impuesto.createRootElementImpuesto40());
                        break;
                    case SModSysConsts.FINS_TP_TAX_CHARGED:
                        if (impuesto.getTipoFactor().compareToIgnoreCase(DCfdi40Catalogs.FAC_TP_EXENTO) == 0) {
                            exemptTaxesAvailable = true;
                        }
                        else {
                            dTotalImptoTrasladado += impuesto.getImporte();
                            impuestosTrasladados.getEltImpuestoTrasladados().add((cfd.ver40.DElementImpuestoTraslado) impuesto.createRootElementImpuesto40());
                        }
                        break;
                    default:
                        throw new Exception("Todos los tipos de impuestos deben ser conocidos (" + impuesto.getImpuestoTipo() + ").");
                }
            }

            if (!impuestosRetenciones.getEltImpuestoRetenciones().isEmpty()) {
                if (comprobante.getEltOpcImpuestos() == null) {
                    comprobante.setEltOpcImpuestos(new cfd.ver40.DElementImpuestos(comprobante));
                }
                
                comprobante.getEltOpcImpuestos().getAttTotalImpuestosRetenidos().setDouble(dTotalImptoRetenido);
                
                comprobante.getEltOpcImpuestos().setEltOpcImpuestosRetenciones(impuestosRetenciones);
            }

            if (!impuestosTrasladados.getEltImpuestoTrasladados().isEmpty() || exemptTaxesAvailable) {
                if (comprobante.getEltOpcImpuestos() == null) {
                    comprobante.setEltOpcImpuestos(new cfd.ver40.DElementImpuestos(comprobante));
                }
                
                comprobante.getEltOpcImpuestos().getAttTotalImpuestosTraslados().setDouble(dTotalImptoTrasladado);
                if (exemptTaxesAvailable) {
                    comprobante.getEltOpcImpuestos().getAttTotalImpuestosTraslados().setCanBeZero(true);
                }
                
                if (!impuestosTrasladados.getEltImpuestoTrasladados().isEmpty()) {
                    comprobante.getEltOpcImpuestos().setEltOpcImpuestosTrasladados(impuestosTrasladados);
                }
            }
        }
        
        if (SLibUtils.belongsTo(xmlCfdi.getCfdType(), new int[] { SDataConstantsSys.TRNS_TP_CFD_PAYROLL, SDataConstantsSys.TRNS_TP_CFD_PAY_REC, SDataConstantsSys.TRNS_TP_CFD_BOL })) {
            if (elementComplement == null) {
                throw new Exception("Error al generar el complemento nómina o el complemento no existe.");
            }
        }
        
        if (elementComplement != null) {
            comprobante.setEltOpcComplemento((cfd.ver40.DElementComplemento) elementComplement);
        }

        if (xmlCfdi.getElementAddenda() != null) {
            comprobante.setEltOpcAddenda((cfd.ver4.DElementAddenda) xmlCfdi.getElementAddenda());
        }

        validateCorrectnessXml(comprobante);

        return comprobante;
    }
    
    /**
     * Obtiene el impuesto trasladado de SIIE a partir de un ConceptoImpuestoTraslado obtenido de un CFDI.
     * @param client Cliente SIIE.
     * @param traslado Impuesto trasladado del CFDI.
     * @return Una instancia de la clase <code>erp.mfin.data.SDataTax</code>.
     * @throws Exception 
     */
    public static erp.mfin.data.SDataTax obtainTaxCharged(final erp.client.SClientInterface client, final cfd.ver33.DElementConceptoImpuestoTraslado traslado) throws Exception {
        int cfdTaxId = 0;
        
        switch (traslado.getAttImpuesto().getString()) {
            case DCfdi33Catalogs.IMP_IVA:
                cfdTaxId = SModSysConsts.FINS_CFD_TAX_IVA;
                break;
            case DCfdi33Catalogs.IMP_ISR:
                cfdTaxId = SModSysConsts.FINS_CFD_TAX_ISR;
                break;
            case DCfdi33Catalogs.IMP_IEPS:
                throw new Exception("El impuesto trasladado en el concepto no está soportado: '" + traslado.getAttImpuesto().getString() + "'.");
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n"
                        + "El impuesto trasladado en el concepto es desconocido: '" + traslado.getAttImpuesto().getString() + "'.");
        }
        
        String vatType = "";
        
        switch (traslado.getAttTipoFactor().getString()) {
            case DCfdi33Catalogs.FAC_TP_TASA:
                break;
            case DCfdi33Catalogs.FAC_TP_CUOTA:
                throw new Exception("El tipo de factor del impuesto trasladado en el concepto no está soportado: '" + traslado.getAttTipoFactor().getString() + "'.");
            case DCfdi33Catalogs.FAC_TP_EXENTO:
                vatType = SDiotConsts.VAT_TYPE_EXEMPT;
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n"
                        + "El tipo de factor del impuesto trasladado en el concepto es desconocido: '" + traslado.getAttTipoFactor().getString() + "'.");
        }
        
        SDataTax tax = null;
        
        String sql = "SELECT t.id_tax_bas, t.id_tax "
                + "FROM erp.finu_tax AS t "
                + "INNER JOIN erp.finu_tax_bas AS tb ON tb.id_tax_bas = t.id_tax_bas "
                + "WHERE NOT t.b_del AND NOT tb.b_del AND "
                + "tb.fid_cfd_tax = " + cfdTaxId + " AND " + (vatType.isEmpty() ? "NOT t.vat_type = '" + SDiotConsts.VAT_TYPE_EXEMPT 
                + "' AND " : "t.vat_type = '" + vatType + "' AND ")
                + "ROUND(t.per, 4) = " + SLibUtils.DecimalFormatValue4D.format(traslado.getAttTasaOCuota().getDouble()) + " AND "
                + "t.fid_tp_tax = " + SModSysConsts.FINS_TP_TAX_CHARGED + ";";
        
        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            if (resultSet.next()){
                int[] key = new int[] { resultSet.getInt("t.id_tax_bas"), resultSet.getInt("t.id_tax") };
                tax = new SDataTax();
                tax.read(key, client.getSession().getStatement());
            }
        }
        
        return tax;
    }
    
    /**
     * Obtiene el impuesto trasladado de SIIE a partir de un ConceptoImpuestoTraslado obtenido de un CFDI.
     * @param client Cliente SIIE.
     * @param traslado Impuesto trasladado del CFDI.
     * @return Una instancia de la clase <code>erp.mfin.data.SDataTax</code>.
     * @throws Exception 
     */
    public static erp.mfin.data.SDataTax obtainTaxCharged(final erp.client.SClientInterface client, final cfd.ver40.DElementConceptoImpuestoTraslado traslado) throws Exception {
        int cfdTaxId = 0;
        
        switch (traslado.getAttImpuesto().getString()) {
            case DCfdi33Catalogs.IMP_IVA:
                cfdTaxId = SModSysConsts.FINS_CFD_TAX_IVA;
                break;
            case DCfdi33Catalogs.IMP_ISR:
                cfdTaxId = SModSysConsts.FINS_CFD_TAX_ISR;
                break;
            case DCfdi33Catalogs.IMP_IEPS:
                throw new Exception("El impuesto trasladado en el concepto no está soportado: '" + traslado.getAttImpuesto().getString() + "'.");
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n"
                        + "El impuesto trasladado en el concepto es desconocido: '" + traslado.getAttImpuesto().getString() + "'.");
        }
        
        String vatType = "";
        
        switch (traslado.getAttTipoFactor().getString()) {
            case DCfdi33Catalogs.FAC_TP_TASA:
                break;
            case DCfdi33Catalogs.FAC_TP_CUOTA:
                throw new Exception("El tipo de factor del impuesto trasladado en el concepto no está soportado: '" + traslado.getAttTipoFactor().getString() + "'.");
            case DCfdi33Catalogs.FAC_TP_EXENTO:
                vatType = SDiotConsts.VAT_TYPE_EXEMPT;
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n"
                        + "El tipo de factor del impuesto trasladado en el concepto es desconocido: '" + traslado.getAttTipoFactor().getString() + "'.");
        }
        
        SDataTax tax = null;
        
        String sql = "SELECT t.id_tax_bas, t.id_tax "
                + "FROM erp.finu_tax AS t "
                + "INNER JOIN erp.finu_tax_bas AS tb ON tb.id_tax_bas = t.id_tax_bas "
                + "WHERE NOT t.b_del AND NOT tb.b_del AND "
                + "tb.fid_cfd_tax = " + cfdTaxId + " AND " + (vatType.isEmpty() ? "NOT t.vat_type = '" + SDiotConsts.VAT_TYPE_EXEMPT 
                + "' AND " : "t.vat_type = '" + vatType + "' AND ")
                + "ROUND(t.per, 4) = " + SLibUtils.DecimalFormatValue4D.format(traslado.getAttTasaOCuota().getDouble()) + " AND "
                + "t.fid_tp_tax = " + SModSysConsts.FINS_TP_TAX_CHARGED + ";";
        
        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            if (resultSet.next()){
                int[] key = new int[] { resultSet.getInt("t.id_tax_bas"), resultSet.getInt("t.id_tax") };
                tax = new SDataTax();
                tax.read(key, client.getSession().getStatement());
            }
        }
        
        return tax;
    }
    
    /**
     * Obtiene impuesto retenido de SIIE a partir de un ConceptoImpuestoRetencion obtenido de un CFDI.
     * @param client Cliente SIIE.
     * @param retención Impuesto retenido del CFDI.
     * @return Una instancia de la clase <code>erp.mfin.data.SDataTax</code>.
     * @throws Exception 
     */
    public static erp.mfin.data.SDataTax obtainTaxRetained(final erp.client.SClientInterface client, final cfd.ver33.DElementConceptoImpuestoRetencion retención) throws Exception {
        int cfdTaxId = 0;
        
        switch (retención.getAttImpuesto().getString()) {
            case DCfdi33Catalogs.IMP_IVA:
                cfdTaxId = SModSysConsts.FINS_CFD_TAX_IVA;
                break;
            case DCfdi33Catalogs.IMP_ISR:
                cfdTaxId = SModSysConsts.FINS_CFD_TAX_ISR;
                break;
            case DCfdi33Catalogs.IMP_IEPS:
                throw new Exception("El impuesto trasladado en el concepto no está soportado: '" + retención.getAttImpuesto().getString() + "'.");
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n"
                        + "El impuesto trasladado en el concepto es desconocido: '" + retención.getAttImpuesto().getString() + "'.");
        }
        
        String vatType = "";
        
        switch (retención.getAttTipoFactor().getString()) {
            case DCfdi33Catalogs.FAC_TP_TASA:
                break;
            case DCfdi33Catalogs.FAC_TP_CUOTA:
                throw new Exception("El tipo de factor del impuesto trasladado en el concepto no está soportado: '" + retención.getAttTipoFactor().getString() + "'.");
            case DCfdi33Catalogs.FAC_TP_EXENTO:
                vatType = SDiotConsts.VAT_TYPE_EXEMPT;
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n"
                        + "El tipo de factor del impuesto trasladado en el concepto es desconocido: '" + retención.getAttTipoFactor().getString() + "'.");
        }
        
        SDataTax tax = null;
        
        String sql = "SELECT t.id_tax_bas, t.id_tax "
                + "FROM erp.finu_tax AS t "
                + "INNER JOIN erp.finu_tax_bas AS tb ON tb.id_tax_bas = t.id_tax_bas "
                + "WHERE NOT t.b_del AND NOT tb.b_del AND "
                + "tb.fid_cfd_tax = " + cfdTaxId + " AND " + (vatType.isEmpty() ? "" : "t.vat_type = '" + vatType + "' AND ")
                + "ROUND(t.per, 4) = " + SLibUtils.DecimalFormatValue4D.format(retención.getAttTasaOCuota().getDouble()) + " AND "
                + "t.fid_tp_tax = " + SModSysConsts.FINS_TP_TAX_RETAINED + ";";
        
        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            if (resultSet.next()){
                int[] key = new int[] { resultSet.getInt("t.id_tax_bas"), resultSet.getInt("t.id_tax") };
                tax = new SDataTax();
                tax.read(key, client.getSession().getStatement());
            }
        }
        
        return tax;
    }
    
    
    /**
     * Obtiene impuesto retenido de SIIE a partir de un ConceptoImpuestoRetencion obtenido de un CFDI.
     * @param client Cliente SIIE.
     * @param retención Impuesto retenido del CFDI.
     * @return Una instancia de la clase <code>erp.mfin.data.SDataTax</code>.
     * @throws Exception 
     */
    public static erp.mfin.data.SDataTax obtainTaxRetained(final erp.client.SClientInterface client, final cfd.ver40.DElementConceptoImpuestoRetencion retención) throws Exception {
        int cfdTaxId = 0;
        
        switch (retención.getAttImpuesto().getString()) {
            case DCfdi33Catalogs.IMP_IVA:
                cfdTaxId = SModSysConsts.FINS_CFD_TAX_IVA;
                break;
            case DCfdi33Catalogs.IMP_ISR:
                cfdTaxId = SModSysConsts.FINS_CFD_TAX_ISR;
                break;
            case DCfdi33Catalogs.IMP_IEPS:
                throw new Exception("El impuesto trasladado en el concepto no está soportado: '" + retención.getAttImpuesto().getString() + "'.");
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n"
                        + "El impuesto trasladado en el concepto es desconocido: '" + retención.getAttImpuesto().getString() + "'.");
        }
        
        String vatType = "";
        
        switch (retención.getAttTipoFactor().getString()) {
            case DCfdi33Catalogs.FAC_TP_TASA:
                break;
            case DCfdi33Catalogs.FAC_TP_CUOTA:
                throw new Exception("El tipo de factor del impuesto trasladado en el concepto no está soportado: '" + retención.getAttTipoFactor().getString() + "'.");
            case DCfdi33Catalogs.FAC_TP_EXENTO:
                vatType = SDiotConsts.VAT_TYPE_EXEMPT;
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n"
                        + "El tipo de factor del impuesto trasladado en el concepto es desconocido: '" + retención.getAttTipoFactor().getString() + "'.");
        }
        
        SDataTax tax = null;
        
        String sql = "SELECT t.id_tax_bas, t.id_tax "
                + "FROM erp.finu_tax AS t "
                + "INNER JOIN erp.finu_tax_bas AS tb ON tb.id_tax_bas = t.id_tax_bas "
                + "WHERE NOT t.b_del AND NOT tb.b_del AND "
                + "tb.fid_cfd_tax = " + cfdTaxId + " AND " + (vatType.isEmpty() ? "" : "t.vat_type = '" + vatType + "' AND ")
                + "ROUND(t.per, 4) = " + SLibUtils.DecimalFormatValue4D.format(retención.getAttTasaOCuota().getDouble()) + " AND "
                + "t.fid_tp_tax = " + SModSysConsts.FINS_TP_TAX_RETAINED + ";";
        
        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            if (resultSet.next()){
                int[] key = new int[] { resultSet.getInt("t.id_tax_bas"), resultSet.getInt("t.id_tax") };
                tax = new SDataTax();
                tax.read(key, client.getSession().getStatement());
            }
        }
        
        return tax;
    }
    
    /**
     * Validate correctness of subtotal, total and taxes in XML
     * @param comprobante Structure for XML
     * @return true if the XML is correct
     * @throws Exception 
     */
    public static boolean validateCorrectnessXml(final cfd.ver32.DElementComprobante comprobante) throws Exception {
        cfd.ver32.DElementConcepto oConcepto = null;
        cfd.ver32.DElementImpuestoTraslado oTraslado = null;
        cfd.ver32.DElementImpuestoRetencion oRetencion = null;
        double dSubtotalImporte = 0;
        double dSubtotalConceptos = 0;
        double dTotalImptoRetenidos = 0;
        double dTotalImptoTrasladados = 0;
        double dTotal = 0;
        
        // validate subtotal concepts:
        
        for (int i = 0; i < comprobante.getEltConceptos().getEltHijosConcepto().size(); i++) {
            oConcepto = comprobante.getEltConceptos().getEltHijosConcepto().get(i);
            
            dSubtotalImporte = SLibUtils.roundAmount(oConcepto.getAttCantidad().getDouble() * oConcepto.getAttValorUnitario().getDouble());
            
            if (Math.abs(oConcepto.getAttImporte().getDouble() - dSubtotalImporte) >= SLibConstants.RES_VAL_DECS) {
                throw new Exception("El monto del importe del concepto '" + oConcepto.getAttDescripcion().getString() + "' es incorrecto.");
            }
            
            dSubtotalConceptos = SLibUtils.roundAmount(dSubtotalConceptos + dSubtotalImporte);
        }
        
        // validate taxes charged:
        
        if (comprobante.getEltImpuestos().getEltOpcImpuestosTrasladados() != null) {
            for (int i = 0; i < comprobante.getEltImpuestos().getEltOpcImpuestosTrasladados().getEltHijosImpuestoTrasladado().size(); i++) {
                oTraslado = comprobante.getEltImpuestos().getEltOpcImpuestosTrasladados().getEltHijosImpuestoTrasladado().get(i);

                dTotalImptoTrasladados = SLibUtils.roundAmount(dTotalImptoTrasladados + oTraslado.getAttImporte().getDouble());
            }

            if (Math.abs(comprobante.getEltImpuestos().getAttTotalImpuestosTrasladados().getDouble() - dTotalImptoTrasladados) >= SLibConstants.RES_VAL_DECS) {
                throw new Exception("La suma de los impuestos trasladados es incorrecta.");
            }
        }
        
        // validate taxes retained:
        
        if (comprobante.getEltImpuestos().getEltOpcImpuestosRetenidos() != null) {
            for (int i = 0; i < comprobante.getEltImpuestos().getEltOpcImpuestosRetenidos().getEltHijosImpuestoRetenido().size(); i++) {
                oRetencion = comprobante.getEltImpuestos().getEltOpcImpuestosRetenidos().getEltHijosImpuestoRetenido().get(i);

                dTotalImptoRetenidos = SLibUtils.roundAmount(dTotalImptoRetenidos + oRetencion.getAttImporte().getDouble());
            }

            if (Math.abs(comprobante.getEltImpuestos().getAttTotalImpuestosRetenidos().getDouble() - dTotalImptoRetenidos) >= SLibConstants.RES_VAL_DECS) {
                throw new Exception("La suma de los impuestos retenidos es incorrecta.");
            }
        }
        
        // validate subtotal vs. subtotal concepts:
        
        if (Math.abs(comprobante.getAttSubTotal().getDouble() - dSubtotalConceptos) >= SLibConstants.RES_VAL_DECS) {
            throw new Exception("La suma de importes de los conceptos es incorrecta.");
        }
        
        // validate total:
        
        dTotal = SLibUtils.roundAmount(dSubtotalConceptos + dTotalImptoTrasladados - dTotalImptoRetenidos - comprobante.getAttDescuento().getDouble());
        
        if (Math.abs(comprobante.getAttTotal().getDouble() - dTotal) >= SLibConstants.RES_VAL_DECS) {
            throw new Exception("El monto del cálculo del total es incorrecto.");
        }
        
        return true;
    }
    
    /**
     * Validate correctness of subtotal, total and taxes in XML
     * @param comprobante Structure for XML
     * @return true if the XML is correct
     * @throws Exception 
     */
    public static boolean validateCorrectnessXml(final cfd.ver33.DElementComprobante comprobante) throws Exception {
        cfd.ver33.DElementConcepto oConcepto = null;
        cfd.ver33.DElementImpuestoTraslado oTraslado = null;
        cfd.ver33.DElementImpuestoRetencion oRetencion = null;
        double dSubtotalImporte = 0;
        double dSubtotalConceptos = 0;
        double dTotalImptoRetenidos = 0;
        double dTotalImptoTrasladados = 0;
        double dTotal = 0;
        
        // validate concepts' subtotal:
        for (int i = 0; i < comprobante.getEltConceptos().getEltConceptos().size(); i++) {
            oConcepto = comprobante.getEltConceptos().getEltConceptos().get(i);
            
            dSubtotalImporte = SLibUtils.roundAmount(oConcepto.getAttCantidad().getDouble() * oConcepto.getAttValorUnitario().getDouble());
            
            if (Math.abs(SLibUtils.roundAmount(oConcepto.getAttImporte().getDouble() - dSubtotalImporte)) >= SLibConstants.RES_VAL_DECS) {
                throw new Exception("El monto del importe del concepto '" + oConcepto.getAttDescripcion().getString() + "' es incorrecto.");
            }
            
            dSubtotalConceptos = SLibUtils.roundAmount(dSubtotalConceptos + dSubtotalImporte);
        }
        
        if (comprobante.getEltOpcImpuestos() != null) {
            // validate taxes charged:
            if (comprobante.getEltOpcImpuestos().getEltOpcImpuestosTraslados() != null) {
                for (int i = 0; i < comprobante.getEltOpcImpuestos().getEltOpcImpuestosTraslados().getEltImpuestoTrasladados().size(); i++) {
                    oTraslado = comprobante.getEltOpcImpuestos().getEltOpcImpuestosTraslados().getEltImpuestoTrasladados().get(i);

                    dTotalImptoTrasladados = SLibUtils.roundAmount(dTotalImptoTrasladados + oTraslado.getAttImporte().getDouble());
                }

                if (Math.abs(SLibUtils.roundAmount(comprobante.getEltOpcImpuestos().getAttTotalImpuestosTraslados().getDouble() - dTotalImptoTrasladados)) >= SLibConstants.RES_VAL_DECS) {
                    throw new Exception("La suma de los impuestos trasladados es incorrecta.");
                }
            }

            // validate taxes retained:
            if (comprobante.getEltOpcImpuestos().getEltOpcImpuestosRetenciones() != null) {
                for (int i = 0; i < comprobante.getEltOpcImpuestos().getEltOpcImpuestosRetenciones().getEltImpuestoRetenciones().size(); i++) {
                    oRetencion = comprobante.getEltOpcImpuestos().getEltOpcImpuestosRetenciones().getEltImpuestoRetenciones().get(i);

                    dTotalImptoRetenidos = SLibUtils.roundAmount(dTotalImptoRetenidos + oRetencion.getAttImporte().getDouble());
                }

                if (Math.abs(SLibUtils.roundAmount(comprobante.getEltOpcImpuestos().getAttTotalImpuestosRetenidos().getDouble() - dTotalImptoRetenidos)) >= SLibConstants.RES_VAL_DECS) {
                    throw new Exception("La suma de los impuestos retenidos es incorrecta.");
                }
            }
        }
        
        // validate subtotal vs. subtotal concepts:
        if (Math.abs(SLibUtils.roundAmount(comprobante.getAttSubTotal().getDouble() - dSubtotalConceptos)) >= SLibConstants.RES_VAL_DECS) {
            throw new Exception("La suma de importes de los conceptos es incorrecta.");
        }
        
        // validate total:
        dTotal = SLibUtils.roundAmount(dSubtotalConceptos + dTotalImptoTrasladados - dTotalImptoRetenidos - comprobante.getAttDescuento().getDouble());
        if (Math.abs(SLibUtils.roundAmount(comprobante.getAttTotal().getDouble() - dTotal)) >= SLibConstants.RES_VAL_DECS) {
            throw new Exception("El monto del cálculo del total es incorrecto.");
        }
        
        return true;
    }
    
    /**
     * Validate correctness of subtotal, total and taxes in XML
     * @param comprobante Structure for XML
     * @return true if the XML is correct
     * @throws Exception 
     */
    public static boolean validateCorrectnessXml(final cfd.ver40.DElementComprobante comprobante) throws Exception {
        cfd.ver40.DElementConcepto oConcepto = null;
        cfd.ver40.DElementImpuestoTraslado oTraslado = null;
        cfd.ver40.DElementImpuestoRetencion oRetencion = null;
        double dSubtotalImporte = 0;
        double dSubtotalConceptos = 0;
        double dTotalImptoRetenidos = 0;
        double dTotalImptoTrasladados = 0;
        double dTotal = 0;
        
        // validate concepts' subtotal:
        for (int i = 0; i < comprobante.getEltConceptos().getEltConceptos().size(); i++) {
            oConcepto = comprobante.getEltConceptos().getEltConceptos().get(i);
            
            dSubtotalImporte = SLibUtils.roundAmount(oConcepto.getAttCantidad().getDouble() * oConcepto.getAttValorUnitario().getDouble());
            
            if (Math.abs(SLibUtils.roundAmount(oConcepto.getAttImporte().getDouble() - dSubtotalImporte)) >= SLibConstants.RES_VAL_DECS) {
                throw new Exception("El monto del importe del concepto '" + oConcepto.getAttDescripcion().getString() + "' es incorrecto.");
            }
            
            dSubtotalConceptos = SLibUtils.roundAmount(dSubtotalConceptos + dSubtotalImporte);
        }
        
        if (comprobante.getEltOpcImpuestos() != null) {
            // validate taxes charged:
            if (comprobante.getEltOpcImpuestos().getEltOpcImpuestosTraslados() != null) {
                for (int i = 0; i < comprobante.getEltOpcImpuestos().getEltOpcImpuestosTraslados().getEltImpuestoTrasladados().size(); i++) {
                    oTraslado = comprobante.getEltOpcImpuestos().getEltOpcImpuestosTraslados().getEltImpuestoTrasladados().get(i);

                    dTotalImptoTrasladados = SLibUtils.roundAmount(dTotalImptoTrasladados + oTraslado.getAttImporte().getDouble());
                }

                if (Math.abs(SLibUtils.roundAmount(comprobante.getEltOpcImpuestos().getAttTotalImpuestosTraslados().getDouble() - dTotalImptoTrasladados)) >= SLibConstants.RES_VAL_DECS) {
                    throw new Exception("La suma de los impuestos trasladados es incorrecta.");
                }
            }

            // validate taxes retained:
            if (comprobante.getEltOpcImpuestos().getEltOpcImpuestosRetenciones() != null) {
                for (int i = 0; i < comprobante.getEltOpcImpuestos().getEltOpcImpuestosRetenciones().getEltImpuestoRetenciones().size(); i++) {
                    oRetencion = comprobante.getEltOpcImpuestos().getEltOpcImpuestosRetenciones().getEltImpuestoRetenciones().get(i);

                    dTotalImptoRetenidos = SLibUtils.roundAmount(dTotalImptoRetenidos + oRetencion.getAttImporte().getDouble());
                }

                if (Math.abs(SLibUtils.roundAmount(comprobante.getEltOpcImpuestos().getAttTotalImpuestosRetenidos().getDouble() - dTotalImptoRetenidos)) >= SLibConstants.RES_VAL_DECS) {
                    throw new Exception("La suma de los impuestos retenidos es incorrecta.");
                }
            }
        }
        
        // validate subtotal vs. subtotal concepts:
        if (Math.abs(SLibUtils.roundAmount(comprobante.getAttSubTotal().getDouble() - dSubtotalConceptos)) >= SLibConstants.RES_VAL_DECS) {
            throw new Exception("La suma de importes de los conceptos es incorrecta.");
        }
        
        // validate total:
        dTotal = SLibUtils.roundAmount(dSubtotalConceptos + dTotalImptoTrasladados - dTotalImptoRetenidos - comprobante.getAttDescuento().getDouble());
        if (Math.abs(SLibUtils.roundAmount(comprobante.getAttTotal().getDouble() - dTotal)) >= SLibConstants.RES_VAL_DECS) {
            throw new Exception("El monto del cálculo del total es incorrecto.");
        }
        
        return true;
    }

    public static String composeCfdWithTfdTimbreFiscalDigital(final SClientInterface client, final SDataCfd cfd, final String xml) throws Exception {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document docStamping = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        Document docOriginal = docBuilder.parse(new ByteArrayInputStream(cfd.getDocXml().getBytes("UTF-8")));
        DOMImplementationLS domImplementation = (DOMImplementationLS) docStamping.getImplementation();
        LSSerializer lsSerializer = domImplementation.createLSSerializer();
        LSOutput lsOutput =  domImplementation.createLSOutput();
        lsOutput.setEncoding("UTF-8");
        Writer stringWriter = new StringWriter();
        lsOutput.setCharacterStream(stringWriter);
        Node nodeComprobante = null;
        Node nodeTimbre = null;
        Node nodeReferencia = null;
        Node nodeAux = null;
        Node nodeAddenda = null;

        // Comprobante:

        nodeComprobante = SXmlUtils.extractElements(docOriginal, "cfdi:Comprobante").item(0);

        if (SXmlUtils.hasChildElement(nodeComprobante, "cfdi:Complemento")) {
            nodeAux = SXmlUtils.extractChildElements(nodeComprobante, "cfdi:Complemento").get(0);

            nodeReferencia = SXmlUtils.extractElements(docStamping, "cfdi:Complemento").item(0);
            nodeTimbre = SXmlUtils.extractChildElements(nodeReferencia, "tfd:TimbreFiscalDigital").get(0);
            lsSerializer.writeToString(nodeTimbre);

            nodeReferencia = docOriginal.adoptNode(nodeTimbre);

            nodeAux.appendChild(nodeReferencia);
        }
        else {
            if (SXmlUtils.hasChildElement(nodeComprobante, "cfdi:Addenda")) {
                nodeAddenda = SXmlUtils.extractElements(docOriginal, "cfdi:Addenda").item(0);

                nodeAux = SXmlUtils.extractElements(docStamping, "cfdi:Complemento").item(0);
                lsSerializer.writeToString(nodeAux);

                nodeReferencia = docOriginal.adoptNode(nodeAux);

                nodeComprobante.insertBefore(nodeReferencia, nodeAddenda);
            }
            else {
                nodeAux = SXmlUtils.extractElements(docStamping, "cfdi:Complemento").item(0);
                lsSerializer.writeToString(nodeAux);

                nodeReferencia = docOriginal.adoptNode(nodeAux);

                nodeComprobante.appendChild(nodeReferencia);
            }
        }
        lsSerializer.writeToString(nodeComprobante);

        lsSerializer.writeToString(docOriginal);
        lsSerializer.write(docOriginal, lsOutput);

        return stringWriter.toString();
    }

    /**
     * Verifies the expiration date of current certificate. If there are fewer days than the ones specified, it generates a warning message.
     * @param client
     * @return Warning message if any, other wise empty string.
     */
    public static String verifyCertificateExpiration(final SClientInterface client) {
        String message = "";
        Date expiration = client.getSessionXXX().getParamsCompany().getDbmsDataCertificate_n().getExpirationDate();
        long daysToExpire = SLibTimeUtils.getDaysDiff(expiration, client.getSessionXXX().getSystemDate());
        
        if (daysToExpire > 0 && daysToExpire <= SCfdConsts.CER_EXP_DAYS_WARN) {
            message = "!ADVERTENCIA: El CSD caducará en " + daysToExpire + " " + (daysToExpire == 1 ? "día" : "días") + "!";
        }
        else if (daysToExpire <= 0) {
            message = "!ADVERTENCIA: El CSD ya caducó!";
        }
        
        return message;
    }
    
    public static boolean signCfdi(final SClientInterface client, final SDataCfd cfd, int payrollCfdVersion) throws Exception {
        boolean signed = false;
        ArrayList<SDataCfd> cfdsValidate = null;

        if (cfd.getFkCfdTypeId() == SDataConstantsSys.TRNS_TP_CFD_PAYROLL) {
            cfdsValidate = getPayrollCfds(client, payrollCfdVersion, new int[] { payrollCfdVersion == SCfdConsts.CFDI_PAYROLL_VER_OLD ? cfd.getFkPayrollPayrollId_n() : cfd.getFkPayrollReceiptPayrollId_n()});
        }

        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del comprobante.");
        }
        else {
            if (areCfdInconsistent(cfdsValidate)) {
                signed = signCfdi(client, cfd, payrollCfdVersion, true, true);
            }
        }
        
        return signed;
    }

    public static boolean signPayrollCfdis(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int payrollCfdVersion) throws Exception {
        boolean signed = false;
        ArrayList<SDataCfd> cfdsValidate = new ArrayList<>();
        ArrayList<SDataCfd> cfdsAux = new ArrayList<>();

        for (SDataCfd cfd : cfds) {
            if (cfd.getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                cfdsValidate.add(cfd);

                if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_NEW) {
                    cfdsAux.add(cfd);
                }
            }
        }

        if (cfdsAux.isEmpty()) {
            client.showMsgBoxInformation("No existen comprobantes para timbrar.");
        }
        else {
            if (client.showMsgBoxConfirm("¿Está seguro que desea timbrar " + cfdsAux.size() + " comprobantes?") == JOptionPane.YES_OPTION) {
                signed = true;
                boolean signNeeded = isNeedStamps(client, cfdsAux.get(0), SDbConsts.ACTION_SAVE, 0);
                int stampsAvailable = getStampsAvailable(client, cfdsAux.get(0).getFkCfdTypeId(), cfdsAux.get(0).getTimestamp(), 0);

                if (signNeeded && stampsAvailable == 0) {
                    client.showMsgBoxWarning("No existen timbres disponibles.");
                }
                else {
                    if (signNeeded && cfdsAux.size() > stampsAvailable) {
                        signed = client.showMsgBoxConfirm("Timbres insuficientes:\n -Solo existen '" + stampsAvailable + "' timbres disponibles.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
                    }

                    if (areCfdInconsistent(cfdsValidate)) {
                        if (signed) {
                            SDialogCfdProcessing dialog = new SDialogCfdProcessing((SClient) client, "Procesamiento de timbrado", SCfdConsts.REQ_STAMP);
                            dialog.setFormParams(cfdsAux, null, stampsAvailable, null, signNeeded, payrollCfdVersion, SModSysConsts.TRNU_TP_DPS_ANN_NA, "", "", false);
                            dialog.setVisible(true);
                        }
                    }
                }
            }
        }

        return signed;
    }
    
    public static boolean signAndSendCfdi(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int payrollCfdVersion) throws Exception {
        boolean signedSent = false;
        ArrayList<SDataCfd> cfdsValidate = new ArrayList<>();
        ArrayList<SDataCfd> cfdsAux = new ArrayList<>();

        for(SDataCfd cfd : cfds) {
            if (cfd.getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                cfdsValidate.add(cfd);

                if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_NEW) {
                    cfdsAux.add(cfd);
                }
            }
        }

        if (cfdsAux.isEmpty()) {
            client.showMsgBoxInformation("No existen comprobantes para timbrar y enviar.");
        }
        else {
            if (client.showMsgBoxConfirm("¿Está seguro que desea timbrar y enviar " + cfdsAux.size() + " comprobantes?") == JOptionPane.YES_OPTION) {
                signedSent = true;
                boolean signNeeded = isNeedStamps(client, cfdsAux.get(0), SDbConsts.ACTION_SAVE, 0);
                int stampsAvailable = getStampsAvailable(client, cfdsAux.get(0).getFkCfdTypeId(), cfdsAux.get(0).getTimestamp(), 0);

                if (signNeeded && stampsAvailable == 0) {
                    client.showMsgBoxWarning("No existen timbres disponibles.");
                }
                else {
                    if (signNeeded && cfdsAux.size() > stampsAvailable) {
                        signedSent = client.showMsgBoxConfirm("Timbres insuficientes:\n -Solo existen '" + stampsAvailable + "' timbres disponibles.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
                    }

                    if (areCfdInconsistent(cfdsValidate)) {
                        if (signedSent) {
                            SDialogCfdProcessing dialog = new SDialogCfdProcessing((SClient) client, "Procesamiento de timbrado y envío", SCfdConsts.REQ_STAMP_SEND);
                            dialog.setFormParams(cfdsAux, null, stampsAvailable, null, signNeeded, payrollCfdVersion, SModSysConsts.TRNU_TP_DPS_ANN_NA, "", "", false);
                            dialog.setVisible(true);
                        }
                    }
                }
            }
        }

        return signedSent;
    }

    public static boolean cancelCfdis(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int payrollCfdVersion, final Date cancellationDate, boolean validateStamp, int annulType, String annulReason, String annulRelatedUuid, boolean retryCancel) throws Exception {
        ArrayList<SDataCfd> cfdsAux = new ArrayList<>();
        
        for(SDataCfd cfd : cfds) {
            if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                cfdsAux.add(cfd);
            }
        }
        
        boolean cancel = false;

        if (cfdsAux.isEmpty()) {
            client.showMsgBoxInformation("No existen comprobantes para anular.");
        }
        else {
            if (client.showMsgBoxConfirm("¿Está seguro que desea anular " + cfdsAux.size() + " comprobantes?") == JOptionPane.YES_OPTION) {
                cancel = true;
                
                int stampsAvailable = getStampsAvailable(client, cfdsAux.get(0).getFkCfdTypeId(), cfdsAux.get(0).getTimestamp(), 0);
                boolean needSign = isNeedStamps(client, cfdsAux.get(0), SDbConsts.ACTION_SAVE, 0);

                if (needSign && stampsAvailable == 0) {
                    client.showMsgBoxWarning("No existen timbres disponibles.");
                }
                else {
                    if (needSign && cfdsAux.size() > stampsAvailable) {
                        cancel = client.showMsgBoxConfirm("Timbres insuficientes:\n -Solo existen '" + stampsAvailable + "' timbres disponibles.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
                    }

                    if (cancel && client.showMsgBoxConfirm("La anulación de un CFDI no puede revertirse.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        SDialogCfdProcessing dialog = new SDialogCfdProcessing((SClient) client, "Procesamiento de anulación", SCfdConsts.REQ_ANNUL);
                        dialog.setFormParams(cfdsAux, null, stampsAvailable, cancellationDate, validateStamp, payrollCfdVersion, annulType, annulReason, annulRelatedUuid, retryCancel);
                        dialog.setVisible(true);
                    }
                }
            }
        }

        return cancel;
    }
    
    public static boolean cancelAndSendCfdis(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int payrollCfdVersion, final Date cancellationDate, boolean validateStamp, int annulType, String annulReason, String annulRelatedUuid, boolean retryCancel) throws Exception {
        ArrayList<SDataCfd> cfdsAux = new ArrayList<>();

        for(SDataCfd cfd : cfds) {
            if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                cfdsAux.add(cfd);
            }
        }

        boolean cancel = false;

        if (cfdsAux.isEmpty()) {
            client.showMsgBoxInformation("No existen comprobantes para anular.");
        }
        else {
            if (client.showMsgBoxConfirm("¿Está seguro que desea anular " + cfdsAux.size() + " comprobantes?") == JOptionPane.YES_OPTION) {
                cancel = true;
                int stampsAvailable = getStampsAvailable(client, cfdsAux.get(0).getFkCfdTypeId(), cfdsAux.get(0).getTimestamp(), 0);
                boolean needSign = isNeedStamps(client, cfdsAux.get(0), SDbConsts.ACTION_SAVE, 0);

                if (needSign && stampsAvailable == 0) {
                    client.showMsgBoxWarning("No existen timbres disponibles.");
                }
                else {
                    if (needSign && cfdsAux.size() > stampsAvailable) {
                        cancel = client.showMsgBoxConfirm("Timbres insuficientes:\n -Solo existen '" + stampsAvailable + "' timbres disponibles.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
                    }

                    if (cancel && client.showMsgBoxConfirm("La anulación de un CFDI no puede revertirse.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        SDialogCfdProcessing dialog = new SDialogCfdProcessing((SClient) client, "Procesamiento de anulación y envío", SCfdConsts.REQ_ANNUL_SEND);
                        dialog.setFormParams(cfdsAux, null, stampsAvailable, cancellationDate, validateStamp, payrollCfdVersion, annulType, annulReason, annulRelatedUuid, retryCancel);
                        dialog.setVisible(true);
                    }
                }
            }
        }

        return cancel;
    }

    private static void writeXmlToDisk(final File file, final SDataCfd cfd, final int typeXml) throws Exception {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
        bw.write(typeXml == SDataConstantsSys.TRNS_ST_DPS_EMITED ? cfd.getDocXml() : cfd.getAcknowledgmentCancellationXml());
        bw.close();
    }
    
    public static void downloadXmlCfd(final SClientInterface client, final SDataCfd cfd) throws Exception {
        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del comprobante.");
        }
        else {
            if (canObtainCfdXml(cfd)) {
                client.getFileChooser().setSelectedFile(new File(cfd.getDocXmlName()));
                if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
                    File file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());
                    writeXmlToDisk(file, cfd, SDataConstantsSys.TRNS_ST_DPS_EMITED);
                    client.showMsgBoxInformation(SLibConstants.MSG_INF_FILE_CREATE + file.getAbsolutePath());
                }
            }
        }
    }
    
    public static void downloadXmlPdf(final SClientInterface client, final int[] pk) throws Exception {
        String name = SDataPdf.composePdfName(pk[0], pk[1]);
        File origin = new File(SDataPdf.composePdfDirectory(((SDataParamsCompany)client.getSession().getConfigCompany()).getXmlBaseDirectory(), pk[0]) + "/" + name);
        if (!origin.exists()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\n"
                    + "No se encontró el archivo PDF del comprobante.");
        }
        else {
            client.getFileChooser().setSelectedFile(new File(name));
            if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
                File destiny = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());
                Files.copy(Paths.get(origin.getAbsolutePath()), Paths.get(destiny.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
                client.showMsgBoxInformation(SLibConstants.MSG_INF_FILE_CREATE + destiny.getAbsolutePath());
            }
        }
    }
    
    public static void getXmlCfds(final SClientInterface client, final HashSet<SDataCfd> cfds) throws Exception {
        HashSet<SDataCfd> cfdsAux = new HashSet<>();

        for(SDataCfd cfd : cfds) {
            if (cfd != null || !cfd.getDocXml().isEmpty() || !cfd.getDocXmlName().isEmpty()) {
                cfdsAux.add(cfd);
            }
        }

        if (cfdsAux.isEmpty()) {
            client.showMsgBoxInformation("No se contraron comprobantes  para obtener su correspondiente XML.");
        }
        else {
            client.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
                File fileAux = client.getFileChooser().getSelectedFile();
                client.getFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
                for(SDataCfd cfd : cfdsAux) {
                    if (canObtainCfdXml(cfd)) {
                        client.getFileChooser().setSelectedFile(new File(fileAux, cfd.getDocXmlName()));
                        File file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());
                        writeXmlToDisk(file, cfd, SDataConstantsSys.TRNS_ST_DPS_EMITED);
                    }
                }
                client.showMsgBoxInformation("Los archivos fueron creados.");
            }
            client.getFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
        }
    }

    public static void getAcknowledgmentCancellationCfd(final SClientInterface client, final SDataCfd cfd) throws Exception {
        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del comprobante.");
        }
        else {
            if (canObtainCfdCancelAck(client, cfd)) {
                client.getFileChooser().setSelectedFile(new File(cfd.getDocXmlName().replaceAll(".xml", "_CANCELA_RESP") + ".xml"));
                if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
                    File file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());
                    writeXmlToDisk(file, cfd, SDataConstantsSys.TRNS_ST_DPS_ANNULED);
                    client.showMsgBoxInformation(SLibConstants.MSG_INF_FILE_CREATE + file.getAbsolutePath());
                }
            }
        }
    }
    
    public static void getAcknowledgmentCancellationCfds(final SClientInterface client, final ArrayList<SDataCfd> cfds) throws Exception {
        ArrayList<SDataCfd> cfdsAux = new ArrayList<>();

        for(SDataCfd cfd : cfds) {
            if ((cfd != null || !cfd.getDocXml().isEmpty() || !cfd.getDocXmlName().isEmpty()) && cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                cfdsAux.add(cfd);
            }
        }

        if (cfdsAux.isEmpty()) {
            client.showMsgBoxInformation("No existen comprobantes anulados para obtener acuse de cancelación.");
        }
        else {
            client.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
                File fileAux = client.getFileChooser().getSelectedFile();
                client.getFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
                for(SDataCfd cfd : cfdsAux) {
                    if (canObtainCfdCancelAck(client, cfd)) {
                        client.getFileChooser().setSelectedFile(new File(fileAux, cfd.getDocXmlName()));
                        File file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());
                        writeXmlToDisk(file, cfd, SDataConstantsSys.TRNS_ST_DPS_ANNULED);
                    }
                }
                client.showMsgBoxInformation("Los archivos fueron creados.");
            }
            client.getFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
        }
    }

    /**
     * Send a CFD by mail.
     * @param client ERP Client interface.
     * @param cfd CFI to be send.
     * @param payrollCfdVersion Supported constants: SCfdConsts.CFDI_PAYROLL_VER_OLD, SCfdConsts.CFDI_PAYROLL_VER_CUR or 0 when does not apply.
     * @param isSingle It is true when there is one cfd
     * @param confirmSending It is true when the confirmation will be done.
     * @param catchExceptions When true all exceptions are handled internally, otherwise are shown into dialog messages.
     * @throws javax.mail.MessagingException, java.sql.SQLException
     */
    private static boolean sendCfd(final SClientInterface client, final SDataCfd cfd, final int payrollCfdVersion, boolean isSingle, boolean confirmSending) throws MessagingException, SQLException, SMailException, Exception {
        boolean send = false;
        
        if (canSendCfd(cfd)) {
            int idBizPartner = 0;
            int idBizPartnerBranch = 0;
            
            switch (cfd.getFkCfdTypeId()) {
                case SDataConstantsSys.TRNS_TP_CFD_INV:
                    SDataDps dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, new int[] { cfd.getFkDpsYearId_n(), cfd.getFkDpsDocId_n() }, SLibConstants.EXEC_MODE_SILENT);
                    idBizPartner = dps.getFkBizPartnerId_r();
                    idBizPartnerBranch = dps.getFkBizPartnerBranchId();
                    break;

                case SDataConstantsSys.TRNS_TP_CFD_BOL:
                    SDbBillOfLading bol = new SDbBillOfLading();
                    bol.read(client.getSession(), new int[] { cfd.getFkBillOfLadingId_n() });
                    SDataBizPartnerBranch bpb = (SDataBizPartnerBranch) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BPB, new int[] { bol.getFkCompanyBranchId() }, SLibConstants.EXEC_MODE_SILENT);
                    idBizPartner = bpb.getFkBizPartnerId();
                    idBizPartnerBranch = bpb.getPkBizPartnerBranchId();
                    break;
                    
                case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                    idBizPartner = SDataCfdPayment.getDbmsDataReceptorId(client.getSession().getStatement(), cfd.getPkCfdId());
                    idBizPartnerBranch = SLibConsts.UNDEFINED; // do not really needed, just for consistence
                    break;
                        
                case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                    idBizPartner = payrollCfdVersion == SCfdConsts.CFDI_PAYROLL_VER_OLD ? cfd.getFkPayrollBizPartnerId_n() : cfd.getFkPayrollReceiptEmployeeId_n();
                    idBizPartnerBranch = SLibConsts.UNDEFINED; // do not really needed, just for consistence
                    break;
                    
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
            
            if (confirmSending) {
                send = STrnUtilities.confirmSend(client, TXT_SEND, cfd, null, idBizPartner, idBizPartnerBranch);
            }
            else {
                send = true; // go on without confirmation!
            }

            if (send) {
                send = false;
                
                try {
                    send = STrnUtilities.sendMailCfd(client, cfd, payrollCfdVersion, 0, idBizPartner, idBizPartnerBranch, false);
                    
                    if (isSingle) {
                        if (send) {
                            client.showMsgBoxInformation("El comprobante '" + cfd.getCfdNumber() + "' ha sido enviado vía mail correctamente.");
                        }
                        else {
                            client.showMsgBoxInformation("El comprobante '" + cfd.getCfdNumber() + "' no pudo ser enviado vía mail.");
                        }
                    }
                }
                catch (Exception e) {
                    if (isSingle) {
                        client.showMsgBoxInformation("Ocurrió una excepción al enviar vía mail el comprobante '" + cfd.getCfdNumber() + "':\n" + e.getMessage());
                    }
                    else {
                        SLibUtilities.printOutException(SCfdUtils.class.getName(), e);
                    }
                }
            }
        }
        
        return send;
    }
    
    /**
     * Mail a CFD.
     * @param client GUI client.
     * @param cfd CFD.
     * @param payrollCfdVersion Supported constants: SCfdConsts.CFDI_PAYROLL_VER_OLD, SCfdConsts.CFDI_PAYROLL_VER_CUR or 0 when does not apply.
     * @param confirmSending
     * @throws Exception 
     */
    public static void sendCfd(final SClientInterface client, final SDataCfd cfd, final int payrollCfdVersion, boolean confirmSending) throws Exception {
        if (cfd == null) {
            throw new Exception("No se ha proporcionado el CFD para envío.");
        }
        if (cfd.getDocXml().isEmpty()) {
            throw new Exception("El CFD proporcionado para envío carece de XML.");
        }
        if (cfd.getDocXmlName().isEmpty()) {
            throw new Exception("El CFD proporcionado para envío carece de nombre de archivo XML.");
        }
        else {
            sendCfd(client, cfd, payrollCfdVersion, true, confirmSending);
        }
    }

    /**
     * Mail an array of CFD.
     * @param client GUI client.
     * @param cfds Array of CFD.
     * @param payrollCfdVersion Supported constants: SCfdConsts.CFDI_PAYROLL_VER_OLD, SCfdConsts.CFDI_PAYROLL_VER_CUR or 0 when does not apply.
     * @throws Exception 
     */
    public static void sendCfds(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int payrollCfdVersion) throws Exception {
        ArrayList<SDataCfd> cfdsAux = new ArrayList<>();

        for (SDataCfd cfd : cfds) {
            if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED && !isCfdSent(client, cfd.getPkCfdId())) {
                cfdsAux.add(cfd);
            }
        }

        if (cfdsAux.isEmpty()) {
            client.showMsgBoxInformation("No existen comprobantes para enviar por correo-e.");
        }
        else {
            if (client.showMsgBoxConfirm("¿Está seguro que desea enviar por correo-e " + cfdsAux.size() + " comprobantes?") == JOptionPane.YES_OPTION) {
                SDialogCfdProcessing dialog = new SDialogCfdProcessing((SClient) client, "Procesamiento de envío", SCfdConsts.REQ_SEND_DOC);
                dialog.setFormParams(cfdsAux, null, 0, null, false, payrollCfdVersion, SModSysConsts.TRNU_TP_DPS_ANN_NA, "", "", false);
                dialog.setVisible(true);
            }
        }
    }

    public static boolean getReceiptCancellationCfdi(final SClientInterface client, final SDataCfd cfd, final int payrollCfdVersion, final int annulType, String annulReason, String annulRelatedUuid, boolean retryCancel) throws Exception {
       return cancelCfdiFinkok(client, cfd, payrollCfdVersion, annulType, annulReason, annulRelatedUuid, retryCancel);
    }

    public static ArrayList<SDataCfd> getPayrollCfds(final SClientInterface client, final int typeCfd, final int[] payrollKey) throws Exception {
        return getPayrollCfds(client, typeCfd, payrollKey, "" , 0);
    }
    
    /**
     * Get list of emited payroll CFDI.
     * @param client GUI client.
     * @param typeCfd SCfdConsts.CFDI_PAYROLL_VER_OLD or SCfdConsts.CFDI_PAYROLL_VER_CUR.
     * @param payrollKey Primary key of payroll.
     * @param filterDepartmentIds SQL filter of department IDs.
     * @param orderBy SUtilConsts.PER_DOC, SUtilConsts.PER_BRR, SUtilConsts.PER_REF (department).
     * @return
     * @throws Exception 
     */
    public static ArrayList<SDataCfd> getPayrollCfds(final SClientInterface client, final int typeCfd, final int[] payrollKey, String filterDepartmentIds, int orderBy) throws Exception {
        String sqlInnerJoins = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
                                                                                
        sqlWhere = "NOT (c.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " AND c.b_con = 0) ";
        
        switch (typeCfd) {
            case SCfdConsts.CFDI_PAYROLL_VER_OLD:
                sqlWhere += "AND c.fid_pay_pay_n = " + payrollKey[0] + " ";
                
                sqlOrderBy = "c.id_cfd ";
                break;
                
            case SCfdConsts.CFDI_PAYROLL_VER_CUR:
                sqlWhere += "AND c.fid_pay_rcp_pay_n = " + payrollKey[0] + " AND "
                        + "NOT pri.b_del AND pri.fk_st_rcp = "  + SModSysConsts.TRNS_ST_DPS_EMITED + " " +
                        (filterDepartmentIds.isEmpty() ? "" : "AND pr.fk_dep IN ( " +  filterDepartmentIds + " ) ");
                        
                sqlInnerJoins = "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = c.fid_pay_rcp_pay_n AND pr.id_emp = c.fid_pay_rcp_emp_n "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON bp.id_bp = pr.id_emp "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON dep.id_dep = pr.fk_dep "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri ON pri.id_pay = c.fid_pay_rcp_pay_n AND pri.id_emp = c.fid_pay_rcp_emp_n AND pri.id_iss = c.fid_pay_rcp_iss_n ";
                
                switch (orderBy) {
                    case SUtilConsts.PER_DOC:
                        sqlOrderBy = "";
                        break;
                    case SUtilConsts.PER_BPR:
                        sqlOrderBy = "bp.bp, bp.id_bp, ";
                        break;
                    case SUtilConsts.PER_REF:
                        sqlOrderBy = "dep.code, dep.name, dep.id_dep, bp.bp, bp.id_bp, ";
                        break;
                    default:
                        sqlOrderBy = "c.id_cfd, ";
                }
                
                sqlOrderBy += "pri.num_ser, CAST(pri.num AS UNSIGNED INTEGER), "
                        + "pri.id_pay, pri.id_emp, pri.id_iss ";
                break;
                
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        ArrayList<SDataCfd> cfds = new ArrayList<>();

        String sql = "SELECT c.id_cfd "
                + "FROM trn_cfd AS c "
                + sqlInnerJoins
                + "WHERE " + sqlWhere
                + "ORDER BY " + sqlOrderBy;

        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, new int[] { resultSet.getInt("id_cfd") }, SLibConstants.EXEC_MODE_SILENT);
                cfds.add(cfd);
            }
        }

        return cfds;
    }
    
    public static SDataCfd getPayrollReceiptLastCfd(final SClientInterface client, final int typeCfd, final int[] payrollReceiptKey) throws Exception {
        String sql = "";
        String sqlWhere = "";
        ResultSet resultSet = null;
        SDataCfd cfd = null;

        switch (typeCfd) {
            case SCfdConsts.CFDI_PAYROLL_VER_OLD:
                sqlWhere = "WHERE fid_pay_pay_n = " + payrollReceiptKey[0] + " AND fid_pay_emp_n = " + payrollReceiptKey[1] + " AND fid_pay_bpr_n = " + payrollReceiptKey[2] + " ORDER BY id_cfd DESC LIMIT 1 ";
                break;
            case SCfdConsts.CFDI_PAYROLL_VER_CUR:
                sqlWhere = "WHERE fid_pay_rcp_pay_n = " + payrollReceiptKey[0] + " AND fid_pay_rcp_emp_n = " + payrollReceiptKey[1] + " AND fid_pay_rcp_iss_n = " + payrollReceiptKey[2] + " ORDER BY id_cfd DESC LIMIT 1 ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        sql = "SELECT id_cfd " +
                "FROM trn_cfd " + sqlWhere;

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            cfd = (SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, new int[] { resultSet.getInt("id_cfd") }, SLibConstants.EXEC_MODE_SILENT);
        }

        return cfd;
    }
    
    /**
     * Devuelve un CFD a través del tipo de CFD, la versión del CFD de nómina, si es el caso, y el primary key del comprobante.
     * @param client GUI Client.
     * @param cfdType Constants defined in SDataConstantsSys.TRNS_TP_CFD_...
     * @param payrollCfdVersion Supported constants: SCfdConsts.CFDI_PAYROLL_VER_OLD, SCfdConsts.CFDI_PAYROLL_VER_CUR or 0 when does not apply.
     * @param cfdKey Key of CFD.
     * @return A CFD as a <code>SDataCfd</code> object.
     * @throws java.lang.Exception
     */
    public static SDataCfd getCfd(final SClientInterface client, final int cfdType, final int payrollCfdVersion, final int[] cfdKey) throws java.lang.Exception {
        String sqlWhere = "";

        switch (cfdType) {
            case SDataConstantsSys.TRNS_TP_CFD_INV:
                sqlWhere = "WHERE fid_dps_year_n = " + cfdKey[0] + " AND fid_dps_doc_n = " + cfdKey[1] + " ";
                break;
                
            case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                sqlWhere = "WHERE id_cfd = " + cfdKey[0] + " ";
                break;
                
            case SDataConstantsSys.TRNS_TP_CFD_BOL:
                sqlWhere = "WHERE fid_bol_n = " + cfdKey[0] + " ";
                break;
                
            case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                switch (payrollCfdVersion) {
                    case  SCfdConsts.CFDI_PAYROLL_VER_OLD:
                        sqlWhere = "WHERE fid_pay_pay_n = " + cfdKey[0] + " AND fid_pay_emp_n = " + cfdKey[1] + " AND fid_pay_bpr_n = " + cfdKey[2] + " ";
                        break;
                    case SCfdConsts.CFDI_PAYROLL_VER_CUR:
                        sqlWhere = "WHERE fid_pay_rcp_pay_n = " + cfdKey[0] + " AND fid_pay_rcp_emp_n = " + cfdKey[1] + " ";
                        break;
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
                
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        SDataCfd cfd = null;
        String sql = "SELECT id_cfd FROM trn_cfd " + sqlWhere;

        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                cfd = (SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, new int[] { resultSet.getInt("id_cfd") }, SLibConstants.EXEC_MODE_SILENT);
            }
        }

        return cfd;
    }
    
    /**
     * Devuelve un CFD a través del tipo de cfd y el primary key del comprobante.
     * @param client GUI Client.
     * @param cfdType Constants defined in SDataConstantsSys.TRNS_TP_CFD_...
     * @param cfdKey Key of CFD.
     * @return A CFD as a <code>SDataCfd</code> object.
     * @throws java.lang.Exception
     */
    public static SDataCfd getCfd(final SClientInterface client, final int cfdType, final int[] cfdKey) throws java.lang.Exception {
        return getCfd(client, cfdType, 0, cfdKey);
    }
    
    /**
     * Devuelve una lista de CFD a través del tipo de CFD, la versión del CFD de nómina, si es el caso, y el primary key de los comprobantes.
     * @param client GUI Client.
     * @param cfdType Constants defined in SDataConstantsSys.TRNS_TP_CFD_...
     * @param payrollCfdVersion Supported constants: SCfdConsts.CFDI_PAYROLL_VER_OLD, SCfdConsts.CFDI_PAYROLL_VER_CUR or 0 when does not apply.
     * @param cfdKeys Array of keys of CFD.
     * @return A list of CFD as a <code>ArrayList</code> object.
     * @throws java.lang.Exception
     */
    public static ArrayList<SDataCfd> getCfds(final SClientInterface client, final int cfdType, final int payrollCfdVersion, ArrayList<int[]> cfdKeys) throws java.lang.Exception {
        ArrayList<SDataCfd> cfds = new ArrayList<>();
        
        for (int[] cfdKey : cfdKeys) {
            cfds.add(getCfd(client, cfdType, payrollCfdVersion, cfdKey));
        }
        
        return cfds;
    }
    
    /**
     * Devuelve los cfds de una póliza contable.
     * @param client
     * @param cfdKey
     * @return SDataCfd.
     * @throws java.lang.Exception
     */
    public static HashSet<SDataCfd> getCfdRecord(final SClientInterface client, final Object[] cfdKey) throws java.lang.Exception {
        HashSet<SDataCfd> cfds = new HashSet<>();
        
        // CFD de manera directa:
        
        String sql = "SELECT id_cfd FROM trn_cfd "
                + "WHERE fid_fin_rec_year_n = " + cfdKey[0] + " " 
                + "AND fid_fin_rec_per_n = " + cfdKey[1] + " " 
                + "AND fid_fin_rec_bkc_n = " + cfdKey[2] + " " 
                + "AND fid_fin_rec_tp_rec_n = '" + cfdKey[3] + "' " 
                + "AND fid_fin_rec_num_n = " + cfdKey[4] + ";" ;
        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                cfds.add((SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, new int[] { resultSet.getInt("id_cfd") }, SLibConstants.EXEC_MODE_SILENT));
            }
        }
        
        sql = "SELECT id_cfd FROM trn_cfd "
                + "WHERE fid_rec_year_n = " + cfdKey[0] + " " 
                + "AND fid_rec_per_n = " + cfdKey[1] + " " 
                + "AND fid_rec_bkc_n = " + cfdKey[2] + " " 
                + "AND fid_rec_tp_rec_n = '" + cfdKey[3] + "' " 
                + "AND fid_rec_num_n = " + cfdKey[4] + ";" ;
        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                cfds.add((SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, new int[] { resultSet.getInt("id_cfd") }, SLibConstants.EXEC_MODE_SILENT));
            }
        }
        
        /* XXX WARNING (2021-08-11 Isabel Servín): Se comenta este siguiente bloque de código porque: no es necesario descargar los CFDI indirectos
        // CFD de comprobantes de clientes y proveedores:

        String aux = "SELECT DISTINCT c.id_cfd FROM fin_rec_ety AS re1, trn_cfd AS c "
                + "WHERE NOT re1.b_del "
                + "AND re1.id_year = " + cfdKey[0] + " "
                + "AND re1.id_per = " + cfdKey[1] + " "
                + "AND re1.id_bkc = " + cfdKey[2] + " " 
                + "AND re1.id_tp_rec = '" + cfdKey[3] + "' "
                + "AND re1.id_num = " + cfdKey[4] + " ";
        sql = aux + "AND re1.fid_dps_year_n = c.fid_dps_year_n " +
                "AND re1.fid_dps_doc_n = c.fid_dps_doc_n;";
                
        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                cfds.add((SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, new int[] { resultSet.getInt("id_cfd") }, SLibConstants.EXEC_MODE_SILENT));
            }
        }
        
        // CFD de recepción de pagos:
        
        sql = aux + "AND re1.fid_cfd_n = c.id_cfd;";
        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                cfds.add((SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, new int[] { resultSet.getInt("id_cfd") }, SLibConstants.EXEC_MODE_SILENT));
            }
        }
        */
        return cfds;
    }

    private static InputStream getCancelAckPdf(final SClientInterface client, final SDataCfd cfd) throws Exception {
        String sql = "";
        ResultSet resultSet = null;
        InputStream ackCancellation = null;

        try {
            sql = "SELECT ack_can_pdf_n FROM " + SClientUtils.getComplementaryDdName(client) + ".trn_cfd WHERE id_cfd = " + cfd.getPkCfdId() + " AND ack_can_pdf_n IS NOT NULL ";
            resultSet = client.getSession().getStatement().executeQuery(sql);

            if (resultSet.next()) {
                ackCancellation = resultSet.getBinaryStream("ack_can_pdf_n");
            }
        }
        catch (Exception e) {
            SLibUtils.showException(SCfdUtils.class.getName(), e);
        }

        return ackCancellation;
    }

    public static boolean insertCfdSendLog(final SClientInterface client, final SDataCfd cfd, final String sendTo, final boolean isSend) throws Exception {
        String sql = "";
        int id_snd = 0;
        ResultSet resultSet = null;

        sql = "SELECT COALESCE(MAX(id_snd), 0) + 1 AS f_snd FROM trn_cfd_snd_log WHERE id_cfd = " + cfd.getPkCfdId() + " ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            id_snd = resultSet.getInt("f_snd");
        }

        sql = "INSERT INTO trn_cfd_snd_log VALUES(" + cfd.getPkCfdId() + ", " +
                id_snd + ", '" + SLibUtils.DbmsDateFormatDate.format(client.getSession().getCurrentDate()) + "', '" + sendTo + "', " + isSend + ", " + client.getSession().getUser().getPkUserId() + ", NOW())";

        client.getSession().getStatement().execute(sql);

        return true;
    }
    
    /**
     * Obtiene el ID del CFDI a través del UUID.
     * @param client Cliente GUI.
     * @param uuid UUID.
     * @return ID del CFDI si fue encontrado, de lo contrario cero.
     * @throws java.lang.Exception 
     */
    public static int getCfdIdByUuid(final SClientInterface client, final String uuid) throws Exception {
        int cfdId = 0;
        
        String sql = "SELECT id_cfd FROM trn_cfd WHERE uuid = '" + uuid + "';";
        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                cfdId = resultSet.getInt(1);
            }
        }
        
        return cfdId;
    }
    
    /**
     * Check if company in current user session matches the Receptor in CFDI from file.
     * @param client GUI Client
     * @param cfdiFilePath CFDI file path.
     * @return <code>true</code> if company in current user session matches the Receptor in CFDI, otherwise <code>false</code>.
     * @throws Exception 
     */
    public static boolean checkCompanyAsCfdiReceptor(final SClientInterface client, final String cfdiFilePath) throws Exception {
        DocumentBuilder docBuilder = null;
        Document doc = null;
        Node node = null;
        NamedNodeMap namedNodeMap = null;
        String receptorXml = "";
        String xml = "";
        
        try {
            xml = SXmlUtils.readXml(cfdiFilePath);
        } 
        catch(Exception e) {
            throw new Exception("El CFDI proporcionado es inválido:\n" + e.getMessage());
        }
        
        docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        
        node = SXmlUtils.extractElements(doc, "cfdi:Receptor").item(0);

        if (node == null) {
            throw new Exception("No se encontró el nodo 'cfdi:Receptor'");
        }
        else {
            // Receptor:

            namedNodeMap = node.getAttributes();

            try {
                receptorXml = SXmlUtils.extractAttributeValue(namedNodeMap, "rfc", true);
            }
            catch(Exception e) {
                
            }
            
            if (receptorXml.isEmpty()) {
                try {
                    receptorXml = SXmlUtils.extractAttributeValue(namedNodeMap, "Rfc", true);
                }
                catch(Exception e) {

                }
            }
            
            if (client.getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId().compareTo(receptorXml) != 0) {
                throw new Exception("El receptor del comprobante no es la empresa '" + client.getSessionXXX().getCompany().getDbmsDataCompany().getBizPartner() + "'.");
            }
        }
        
        return true;
    }
}

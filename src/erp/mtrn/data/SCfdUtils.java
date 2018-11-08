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
import com.finkok.facturacion.cancel.CancelSOAP;
import com.finkok.facturacion.cancellation.StringArray; 
import com.finkok.stamp.AcuseRecepcionCFDI; 
import com.finkok.stamp.Incidencia;
import com.finkok.stamp.IncidenciaArray;
import com.finkok.stamp.StampSOAP;
import erp.SClient;
import erp.cfd.SCfdConsts;
import erp.cfd.SCfdDataBizPartner;
import erp.cfd.SCfdDataConcepto;
import erp.cfd.SCfdDataImpuesto;
import erp.cfd.SCfdXmlCfdi32;
import erp.cfd.SCfdXmlCfdi33;
import erp.cfd.SCfdiSignature;
import erp.cfd.SDbCfdBizPartner;
import erp.cfd.SDialogResult;
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
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mcfg.data.SDataCertificate;
import erp.mhrs.data.SDataPayrollReceiptIssue;
import erp.mloc.data.SLocUtils;
import erp.mmkt.data.SDataCustomerBranchConfig;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbFormerPayrollImport;
import erp.mod.hrs.db.SHrsFormerPayroll;
import erp.mod.hrs.db.SHrsFormerPayrollConcept;
import erp.mod.hrs.db.SHrsFormerPayrollExtraTime;
import erp.mod.hrs.db.SHrsFormerPayrollIncident;
import erp.mod.hrs.db.SHrsFormerPayrollReceipt;
import erp.mod.hrs.db.SHrsFormerUtils;
import erp.mtrn.form.SDialogRestoreCfdi;
import erp.print.SDataConstantsPrint;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.mail.MessagingException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
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
import sa.lib.srv.SSrvConsts;
import sa.lib.srv.SSrvLock;
import sa.lib.srv.SSrvUtils;
import sa.lib.xml.SXmlUtils;
import views.core.soap.services.apps.CancelaCFDResult; // Finkok WS for cancelling CFDI
import views.core.soap.services.apps.FolioArray; // Finkok WS for cancelling CFDI
import views.core.soap.services.apps.ReceiptResult; // Finkok WS for cancelling CFDI
import views.core.soap.services.apps.UUIDS; // Finkok WS for cancelling CFDI

/**
 *
 * @author Juan Barajas, Sergio Flores, Edwin Carmona, Alfredo Pérez, Claudio Peña, Sergio Flores
 * 
 * Maintenance Log:
 * 2018-01-02, Sergio Flores:
 *  Implementation of payroll into CFDI 3.3.
 */
public abstract class SCfdUtils implements Serializable {

    private static ArrayList<SCfdPacket> CfdPackets;
    private static int LogSignId;
    
    private static final String TXT_SEND = "Enviar CFDI";
    private static final String TXT_SIGN_SEND = "Timbrar y enviar CFDI";
    
    public static final String TXT_SEND_DPS = "Enviar documento";

    /*
     * Private static methods:
     */

    public static SDataCfdPacType getPacConfiguration(final SClientInterface client, final int cfdTypeId) {
        SDataCfdPacType cfdPacType = null;

        try {
            cfdPacType = (SDataCfdPacType) SDataUtilities.readRegistry(client, SDataConstants.TRN_TP_CFD_PAC, new int[] { cfdTypeId }, SLibConstants.EXEC_MODE_SILENT);
        }
        catch (Exception e) {
            SLibUtils.showException(SCfdUtils.class.getName(), e);
        }

        return cfdPacType;
    }

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
                    for (DElement element : comprobante32.getEltOpcComplemento().getElements()) {
                        if (element.getName().compareTo("tfd:TimbreFiscalDigital") == 0) {
                            certSAT = ((cfd.ver32.DElementTimbreFiscalDigital) element).getAttNoCertificadoSAT().getString();
                            break;
                        }
                    }
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                    cfd.ver33.DElementComprobante comprobante33 = DCfdUtils.getCfdi33(cfd.getDocXml());
                    for (DElement element : comprobante33.getEltOpcComplemento().getElements()) {
                        if (element.getName().compareTo("tfd:TimbreFiscalDigital") == 0) {
                            certSAT = ((cfd.ver33.DElementTimbreFiscalDigital) element).getAttNoCertificadoSAT().getString();
                            break;
                        }
                    }
                    break;
                default:
            }

            cfdPacType = getPacConfiguration(client, cfd.getFkCfdTypeId()); 

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
                    can = client.showMsgBoxConfirm("La fecha del documento " +
                            "(" + SLibUtils.DateFormatDate.format(cfd.getTimestamp()) + ") es posterior a la fecha del sistema " +
                            "(" + SLibUtils.DateFormatDate.format(client.getSessionXXX().getSystemDate()) + ").\n" +
                            "¿Está seguro que desea timbrar el documento?") == JOptionPane.YES_OPTION;
                }
                else {
                    int[] today = SLibTimeUtils.digestDate(client.getSessionXXX().getSystemDate());
                    GregorianCalendar now = new GregorianCalendar();
                    GregorianCalendar limit = new GregorianCalendar(today[0], today[1] - 1, today[2], now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND));   // mix ofsystem's date + current time
                    
                    if (SLibTimeUtilities.getDaysDiff(limit.getTime(), cfd.getTimestamp()) > SCfdConsts.CFDI_STAMP_DELAY_DAYS) {
                        throw new Exception("La fecha-hora del documento " +
                                "(" + SLibUtils.DateFormatDatetime.format(cfd.getTimestamp()) + ") es anterior a la fecha-hora del sistema por más de " + SCfdConsts.CFDI_STAMP_DELAY_DAYS + " días " +
                                "(" + SLibUtils.DateFormatDatetime.format(limit.getTime()) + ").\n" +
                                "No se puede timbrar el documento.");
                    }
                }
            }
        }

        return can;
    }

    private static boolean canObtainXml(final SDataCfd cfd) throws Exception {
        if (!(cfd.isCfd() || cfd.isCfdi())) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nTipo de comprobante fiscal desconocido.");
        }
        else if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_NEW) {
            throw new Exception("El comprobante fiscal no ha sido emitido.");
        }
        else if (cfd.isCfdi()) {
            // CFDI:
            if (!cfd.isStamped()) {
                throw new Exception("El CFDI no ha sido timbrado aún.");
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

    private static boolean canObtainAcknowledgmentCancellation(final SClientInterface client, final SDataCfd cfd) throws Exception {
        if (!(cfd.isCfd() || cfd.isCfdi())) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nTipo de comprobante fiscal desconocido.");
        }
        else {
            if (cfd.isCfd()) {
                // CFD:
                throw new Exception("Los CFD por definición no tienen acuse de cancelación.");
            }
            else {
                // CFDI:
                if (cfd.getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                    throw new Exception("El CFDI no ha sido cancelado.");
                }
                else if (!cfd.isStamped()) {
                    throw new Exception("El CFDI no ha sido timbrado aún.");
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
                else if (cfd.getAcknowledgmentCancellationXml().isEmpty() && getAcknowledgmentCancellationPdf(client, cfd) == null) {
                    throw new Exception("El CFDI no tiene acuse de cancelación.");
                }
            }
        }

        return true;
    }

    private static boolean canPrint(final SDataCfd cfd, final boolean isSaveInProcess) throws Exception {
        if (!isSaveInProcess) {
            if (cfd.getIsProcessingWebService()) {
                throw new Exception(SCfdConsts.ERR_MSG_PROCESS_WS_PAC);
            }
        }

        return true;
    }

    private static boolean canSend(final SDataCfd cfd) throws Exception {
        if (!(cfd.isCfd() || cfd.isCfdi())) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nTipo de comprobante fiscal desconocido.");
        }
        else if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_NEW) {
            throw new Exception("El comprobante fiscal no ha sido emitido.");
        }
        else if (cfd.isCfdi()) {
            // CFDI:
            if (!cfd.isStamped()) {
                throw new Exception("El CFDI no ha sido timbrado aún.");
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
    
    private static boolean existsCfdiEmitInconsist(final SClientInterface client, final ArrayList<SDataCfd> cfds) throws Exception {
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

    private static void createSignCancelLog(final SClientInterface client, final String msg, final int actionCode, final int stepCode, final SDataCfd cfd, final int pacId) throws Exception {
        SDataCfdSignLog cfdSignLog = new SDataCfdSignLog();
        
        cfdSignLog.setPkLogId(LogSignId);

        if (LogSignId == 0) {
            cfdSignLog.setDate(client.getSession().getCurrentDate());
            cfdSignLog.setIsSystem(pacId == SLibConsts.UNDEFINED);
            cfdSignLog.setIsDeleted(false);
            cfdSignLog.setFkCfdId(cfd.getPkCfdId());
            cfdSignLog.setFkPacId_n(pacId);
            cfdSignLog.setFkUserId(client.getSession().getUser().getPkUserId());
        }
        
        cfdSignLog.setCodeAction(actionCode);
        cfdSignLog.setCodeStep(stepCode);

        if (!msg.isEmpty()) {
            SDataCfdSignLogMsg cfdSignLogMsg = new SDataCfdSignLogMsg();

            cfdSignLogMsg.setMessage(msg);

            cfdSignLog.setDbmsDataCfdSignLogMsg(cfdSignLogMsg);
        }

        if (cfdSignLog.save(client.getSession().getDatabase().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
        }
        else {
            LogSignId = cfdSignLog.getPkLogId();
        }
    }

    private static boolean saveAcknowledgmentCancellationPdf(final SClientInterface client, final SDataCfd cfd, FileInputStream ackCancellationPdf) throws Exception {
        String sql = "";
        PreparedStatement preparedStatement = null;
        byte[] byteArray =null;
        byte[] buffer =null;
        int read = -1;

        sql = "UPDATE trn_cfd SET ack_can_pdf_n = ? " +
                "WHERE id_cfd = " + cfd.getPkCfdId() + " ";

        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        buffer = new byte[500];

        while ((read = ackCancellationPdf.read(buffer)) > 0) {
            byteArrayOS.write(buffer, 0, read);
        }
        ackCancellationPdf.close();

        byteArray = byteArrayOS.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);

        preparedStatement = client.getSession().getDatabase().getConnection().prepareStatement(sql);

        preparedStatement.setBlob(1, bais);
        preparedStatement.execute();

        return true;
    }

    private static void validateReceiptsConsistent(SClientInterface client, SHrsFormerPayroll payroll, SHrsFormerPayroll payrollXml, final boolean isRegenerateOnlyNonStampedCfdi) throws Exception {
        // Validate payroll receipts consistent:

        if (payroll == null) {
            throw new NullPointerException("Payroll is null.");
        }
        else {
            CfdPackets = new ArrayList<>();

            for (SHrsFormerPayrollReceipt payrollReceipt: payroll.getChildPayrollReceipts()) {
                boolean isFound = false;
                boolean isConsistent = false;
                boolean add = true;
                int cfdId = SLibConsts.UNDEFINED;
                String docXmlUuid = "";

                if (payrollXml != null) {
                    for (SHrsFormerPayrollReceipt receiptXml : payrollXml.getChildPayrollReceipts()) {
                        if (payrollReceipt.getAuxEmpleadoId() == receiptXml.getAuxEmpleadoId() && payrollReceipt.getPkEmpleadoId() == receiptXml.getPkEmpleadoId()) {
                            isFound = true;
                            isConsistent = validateReceiptsConcepts(payrollReceipt, receiptXml) && payrollReceipt.getBanco() == receiptXml.getBanco() &&
                                    SLibTimeUtils.convertToDateOnly(payrollReceipt.getPayroll().getFecha()).compareTo(SLibTimeUtils.convertToDateOnly(receiptXml.getPayroll().getFecha())) == 0 &&
                                    payrollReceipt.getClabe().compareTo(receiptXml.getClabe()) == 0 && payrollReceipt.getTipoContrato().compareTo(receiptXml.getTipoContrato()) == 0 &&
                                    payrollReceipt.getFechaPago().compareTo(receiptXml.getFechaPago()) == 0 && payrollReceipt.getFechaFinalPago().compareTo(receiptXml.getFechaFinalPago()) == 0 &&
                                    payrollReceipt.getFechaInicialPago().compareTo(receiptXml.getFechaInicialPago()) == 0 && payrollReceipt.getFechaInicioRelLaboral().compareTo(receiptXml.getFechaInicioRelLaboral()) == 0 &&
                                    payrollReceipt.getNumDiasPagados() == receiptXml.getNumDiasPagados() && payrollReceipt.getCurp().compareTo(receiptXml.getCurp()) == 0 &&
                                    payrollReceipt.getTipoRegimen() == receiptXml.getTipoRegimen() && payrollReceipt.getNumSeguridadSocial().compareTo(receiptXml.getNumSeguridadSocial()) == 0 &&
                                    payrollReceipt.getNumEmpleado().compareTo(receiptXml.getNumEmpleado()) == 0 && payrollReceipt.getRegistroPatronal().compareTo(receiptXml.getRegistroPatronal()) == 0 &&
                                    payrollReceipt.getPuesto().compareTo(receiptXml.getPuesto()) == 0 && payrollReceipt.getRiesgoPuesto() == receiptXml.getRiesgoPuesto() &&
                                    payrollReceipt.getDepartamento().compareTo(receiptXml.getDepartamento()) == 0 && payrollReceipt.getPeriodicidadPago().compareTo(receiptXml.getPeriodicidadPago()) == 0 &&
                                    payrollReceipt.getSalarioBaseCotApor() == receiptXml.getSalarioBaseCotApor() && payrollReceipt.getSalarioDiarioIntegrado() == receiptXml.getSalarioDiarioIntegrado() &&
                                    payrollReceipt.getAntiguedad() == receiptXml.getAntiguedad() && payrollReceipt.getTotalDeducciones() == receiptXml.getTotalDeducciones() &&
                                    payrollReceipt.getTotalNeto() == receiptXml.getTotalNeto() && payrollReceipt.getTotalPercepciones() == receiptXml.getTotalPercepciones() &&
                                    payrollReceipt.getTotalRetenciones() == receiptXml.getTotalRetenciones() && payrollReceipt.getTipoJornada().compareTo(receiptXml.getTipoJornada()) == 0 &&
                                    (SLibTimeUtils.convertToDateOnly(payrollReceipt.getPayroll().getFecha()).compareTo(SLibTimeUtils.convertToDateOnly(SLibTimeUtils.createDate(2016, 7, 15))) > 0 ?
                                    SHrsFormerUtils.getPaymentMethodCode(client, payrollReceipt.getMetodoPago()).compareTo(receiptXml.getMetodoPagoAux()) == 0 : 
                                    SHrsFormerUtils.getPaymentMethodName(client, payrollReceipt.getMetodoPago()).compareTo(receiptXml.getMetodoPagoAux()) == 0);
                        }

                        if (isFound) {
                            String sql = "SELECT id_cfd, doc_xml_uuid, fid_st_xml " +
                                    "FROM trn_cfd WHERE fid_pay_pay_n = " + payroll.getPkNominaId() + " AND fid_pay_emp_n = " + payrollReceipt.getAuxEmpleadoId() +
                                    " AND fid_pay_bpr_n = " + payrollReceipt.getPkEmpleadoId() + " ORDER BY id_cfd ";

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
                    
                    payrollReceipt.setPayroll(payroll);
                    payrollReceipt.setFechaEdicion(client.getSession().getCurrentDate());
                    payrollReceipt.setMoneda(client.getSession().getSessionCustom().getLocalCurrencyCode());
                    payrollReceipt.setLugarExpedicion(client.getSessionXXX().getCurrentCompanyBranch().getDbmsBizPartnerBranchAddressOfficial().getZipCode());
                    payrollReceipt.setConfirmacion("");
                    payrollReceipt.setRegimenFiscal(client.getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdRegimenFiscal());
                    payrollReceipt.setCfdiRelacionadosTipoRelacion("");
                    
                    // Generate CFDI:

                    cfd.ver32.DElementComprobante comprobanteCfdi32 = null;
                    cfd.ver33.DElementComprobante comprobanteCfdi33 = null;
                    
                    SCfdPacket packet = new SCfdPacket();
                    packet.setCfdId(cfdId);
                    packet.setIsCfdConsistent(!isFound || cfdId == SLibConsts.UNDEFINED ? true: isConsistent);
                    
                    int xmlType = ((SSessionCustom) client.getSession().getSessionCustom()).getCfdTypeXmlTypes().get(SDataConstantsSys.TRNS_TP_CFD_PAYROLL);
                    float cfdVersion = SLibConsts.UNDEFINED;
                    
                    switch (xmlType) {
                        case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                            comprobanteCfdi32 = (cfd.ver32.DElementComprobante) createCfdi32RootElement(client, payrollReceipt);
                            cfdVersion = comprobanteCfdi32.getVersion();
                            
                            packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi32));
                            packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW); // after stamping changes to emitted
                            break;
                        case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                            comprobanteCfdi33 = (cfd.ver33.DElementComprobante) createCfdi33RootElement(client, payrollReceipt);
                            cfdVersion = comprobanteCfdi33.getVersion();
                            
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
                    packet.setFkUserDeliveryId(client.getSession().getUser().getPkUserId());
                    packet.setPayrollPayrollId(payroll.getPkNominaId());
                    packet.setPayrollEmployeeId(payrollReceipt.getAuxEmpleadoId());
                    packet.setPayrollBizPartnerId(payrollReceipt.getPkEmpleadoId());
                    
                    packet.setCfdCertNumber(client.getCfdSignature(cfdVersion).getCertNumber());
                    packet.setCfdSignature(client.getCfdSignature(cfdVersion).sign(packet.getCfdStringSigned(), SLibTimeUtilities.digestYear(payroll.getFecha())[0]));
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

                    CfdPackets.add(packet);
                }
            }
        }

        if (!isRegenerateOnlyNonStampedCfdi && payroll.getChildPayrollReceipts().size() != CfdPackets.size()) {
            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            throw new Exception("No se generó el CFDI para todos los recibos de la nómina '" + payroll.getPkNominaId() + "'.");
        }
    }

    private static boolean validateReceiptsConcepts(SHrsFormerPayrollReceipt receipt, SHrsFormerPayrollReceipt receiptXml) {
       ArrayList<SHrsFormerPayrollConcept> payrollConceptsXml = null;
       boolean isConsistent = true;
       boolean isFound = false;
       int i = 0;

       payrollConceptsXml = receiptXml.getChildPayrollConcept();

       if (receipt.getChildPayrollConcept().size() != payrollConceptsXml.size()) {
           isConsistent = false;
       }
       else {
            for (SHrsFormerPayrollConcept concept: receipt.getChildPayrollConcept()) {
                isFound = false;
                isConsistent = true;
                i = 0;

                for (SHrsFormerPayrollConcept conceptXml: payrollConceptsXml) {
                    if (concept.getPkTipoConcepto() == conceptXml.getPkTipoConcepto() && concept.getPkSubtipoConcepto() == conceptXml.getPkSubtipoConcepto() &&
                            concept.getClaveOficial() == conceptXml.getClaveOficial() && concept.getClaveEmpresa().compareTo(conceptXml.getClaveEmpresa()) == 0 && !conceptXml.getExtIsFound()) {
                        isFound = true;
                        isConsistent = validateReceiptsIncident(concept, conceptXml) &&
                                concept.getConcepto().compareTo(conceptXml.getConcepto()) == 0 &&
                                concept.getTotalGravado() == conceptXml.getTotalGravado() && concept.getTotalExento() == conceptXml.getTotalExento() &&
                                (SLibUtils.belongsTo(concept.getPkSubtipoConcepto(), new int[] { SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1], SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_TRIPLE[1] }) ?
                                validateReceiptsExtraTime(concept.getChildPayrollExtraTimes(), conceptXml.getChildPayrollExtraTimes()) : true);
                    }

                    if (isFound && isConsistent) {
                        payrollConceptsXml.get(i).setExtIsFound(isFound);
                        break;
                    }
                    i++;
                }

                if (!isFound) {
                    isConsistent = false;
                }

                if (!isConsistent) {
                    break;
                }
            }
       }

        return isConsistent;
    }

    private static boolean validateReceiptsExtraTime(SHrsFormerPayrollExtraTime extraTime, SHrsFormerPayrollExtraTime extraTimeXml) {
       return (extraTime.getDias() == extraTimeXml.getDias() && extraTime.getTipoHoras().compareTo(extraTimeXml.getTipoHoras()) == 0 &&
               extraTime.getHorasExtra() == extraTimeXml.getHorasExtra() && SLibUtils.round(extraTime.getImportePagado(), SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()) == extraTimeXml.getImportePagado());
    }

    private static boolean validateReceiptsIncident(SHrsFormerPayrollConcept concept, SHrsFormerPayrollConcept conceptXml) {
       boolean isConsistent = true;
       boolean isFound = false;

        for (SHrsFormerPayrollIncident incident: concept.getChildPayrollIncident()) {
            isFound = false;
            isConsistent = true;

            for (SHrsFormerPayrollIncident incidentXml: conceptXml.getChildPayrollIncident()) {
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

    private static boolean managementCfdi(final SClientInterface client, final SDataCfd dataCfd, final int xmlStatusId, final Date date, final boolean isSingle, final boolean isValidation, final int pacId, final int cfdSubtype) throws Exception {
        return managementCfdi(client, dataCfd, xmlStatusId, date, isSingle, isValidation, pacId, cfdSubtype, "", false);
    }

    private static boolean managementCfdi(final SClientInterface client, final SDataCfd dataCfd, final int newXmlStatusId, final Date date, final boolean isSingle, final boolean isValidation, final int pacId, final int cfdSubtype, final String xmlSigned, final boolean isUpdateAckPdf) throws Exception {
        int[] registryKey = null;
        SDataDps dataDps = null;
        SDataCfdPayment dataCfdPayment = null;
        SDataPayrollReceiptIssue dataPayrollReceiptIssue = null;
        SSrvLock lock = null;
        SCfdiSignature cfdiSignature = null;
        String xmlStamping = "";
        String xmlAckCancellation = "";
        String warningMessage = "";
        boolean consumeStamp = true;
        boolean next = true;

        try {
            switch (dataCfd.getFkCfdTypeId()) {
                case SDataConstantsSys.TRNS_TP_CFD_INV:
                    registryKey = new int[] { dataCfd.getFkDpsYearId_n(), dataCfd.getFkDpsDocId_n() };
                    lock = SSrvUtils.gainLock(client.getSession(), client.getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.TRN_DPS, registryKey, 1000 * 60);   // 1 minute timeout
                    break;
                    
                case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                    registryKey = new int[] { dataCfd.getPkCfdId() };
                    lock = SSrvUtils.gainLock(client.getSession(), client.getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.TRNX_CFD_PAY_REC, registryKey, 1000 * 60);  // 1 minute timeout
                    break;
                    
                case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                    if (cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_CUR) {
                        registryKey = new int[] { dataCfd.getFkPayrollReceiptPayrollId_n(), dataCfd.getFkPayrollReceiptEmployeeId_n(), dataCfd.getFkPayrollReceiptIssueId_n() };
                        lock = SSrvUtils.gainLock(client.getSession(), client.getSessionXXX().getCompany().getPkCompanyId(), SModConsts.HRS_PAY_RCP_ISS, registryKey, 1000 * 60);   // 1 minute timeout
                    }
                    break;
                default:
            }
            
            if (newXmlStatusId == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                // cancel CFDI:
                
                if (!isUpdateAckPdf) {
                    xmlAckCancellation = cancel(client, dataCfd, date, isValidation, pacId);

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

                cfdiSignature = obtainCfdiSignature(dataCfd.getDocXml());
            }
            else {
                // sign CFDI:
                
                if (xmlSigned.length() == 0) {
                    xmlStamping = sign(client, dataCfd, isValidation);
                }
                else {
                    xmlStamping = xmlSigned;
                }

                cfdiSignature = obtainCfdiSignature(xmlStamping);
            }

            if (next) {
                next = false;

                SDataCfdPacType cfdPacType = getPacConfiguration(client, dataCfd.getFkCfdTypeId());

                if (cfdPacType == null) {
                    throw new Exception("No existe ninguna configuración para el tipo de CFD.");
                }
                else {
                    if (cfdiSignature == null || cfdiSignature.getUuid().isEmpty()) {
                        throw new Exception("¡El documento no ha sido " + (newXmlStatusId == SDataConstantsSys.TRNS_ST_DPS_ANNULED ? "cancelado" : "timbrado") + "!");
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

                        if (dataCfd.getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                            switch (dataCfd.getFkCfdTypeId()) {
                                case SDataConstantsSys.TRNS_TP_CFD_INV:
                                    dataDps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, registryKey, SLibConstants.EXEC_MODE_SILENT);
                                    dataDps.setAuxIsProcessingValidation(isValidation);
                                    
                                    packet.setAuxDataDps(dataDps);
                                    break;

                                case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                                    dataCfdPayment = (SDataCfdPayment) SDataUtilities.readRegistry(client, SDataConstants.TRNX_CFD_PAY_REC, registryKey, SLibConstants.EXEC_MODE_SILENT);
                                    dataCfdPayment.setAuxIsProcessingValidation(isValidation);
                                    
                                    packet.setAuxDataCfdPayment(dataCfdPayment);
                                    break;

                                case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                                    if (cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_CUR) {
                                        dataPayrollReceiptIssue = new SDataPayrollReceiptIssue();
                                        if (dataPayrollReceiptIssue.read(registryKey, client.getSession().getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                                        }
                                        
                                        packet.setAuxDataPayrollReceiptIssue(dataPayrollReceiptIssue);
                                    }
                                    break;
                                    
                                default:
                            }
                        }
                        
                        SDataPac pac = (SDataPac) SDataUtilities.readRegistry(client, SDataConstants.TRN_PAC, new int[] { pacId != SDataConstants.UNDEFINED ? pacId : cfdPacType.getFkPacId() }, SLibConstants.EXEC_MODE_SILENT);
                        
                        packet.setAuxPacId(pac.getPkPacId());
                        packet.setAuxLogSignId(LogSignId);
                        packet.setAuxStampQuantity(newXmlStatusId == SDataConstantsSys.TRNS_ST_DPS_EMITED ? 1 : (pac.getIsPrepayment() && pac.getIsChargedAnnulment() ? 1 : 0));
                        packet.setAuxStampConsume(consumeStamp);

                        // Sign & Cancel Log step #5
                        createSignCancelLog(client, "", newXmlStatusId == SDataConstantsSys.TRNS_ST_DPS_EMITED ? !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN :
                                !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_CFD_SAVED, dataCfd, pacId == 0 ? pacId : pac.getPkPacId());

                        saveCfd(client, packet);

                        next = true;

                        if (isSingle) {
                            warningMessage = verifyCertificateExpiration(client);
                            
                            if (newXmlStatusId == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                                client.showMsgBoxInformation("El documento ha sido anulado correctamente." + (!isNeedStamps(client, dataCfd, SDbConsts.ACTION_ANNUL, pacId == 0 ? pacId : pac.getPkPacId()) || !consumeStamp ? "" :
                                                "\nTimbres disponibles: " + getStampsAvailable(client, dataCfd.getFkCfdTypeId(), dataCfd.getTimestamp(), pacId == 0 ? pacId : pac.getPkPacId()) + "." + (warningMessage.isEmpty() ? "" : "\n" + warningMessage)));
                            }
                            else {
                                client.showMsgBoxInformation("El documento ha sido timbrado correctamente. " + (!isNeedStamps(client, dataCfd, SDbConsts.ACTION_SAVE, pacId == 0 ? pacId : pac.getPkPacId()) ? "" :
                                                "\nTimbres disponibles: " + getStampsAvailable(client, dataCfd.getFkCfdTypeId(), dataCfd.getTimestamp(), pacId == 0 ? pacId : pac.getPkPacId()) + "." + (warningMessage.isEmpty() ? "" : "\n" + warningMessage)));
                            }
                        }

                        // read again CFD for printing:
                        // XXX NOTE: 2018-02-24, Sergio Flores: Check why is read again the CFD registry:
                        
                        SDataCfd dataCfdForPrinting = (SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, dataCfd.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                        printCfd(client, dataCfdForPrinting, cfdSubtype, SDataConstantsPrint.PRINT_MODE_PDF_FILE, 1, true);

                        // set flag PDF as correct:

                        dataCfdForPrinting.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_PRC_STO_PDF, false);
                        dataCfdForPrinting.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_PRC_USR, client.getSession().getUser().getPkUserId());
                    }
                }
            }
        }
        catch (Exception e) {
            if (lock != null) {
                SSrvUtils.releaseLock(client.getSession(), lock);
            }
            throw e;
        }
        finally {
            if (lock != null) {
                SSrvUtils.releaseLock(client.getSession(), lock);
            }
            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        return next;
    }

    private static int getPacCfdSignedId(final SClientInterface client, final SDataCfd cfd) {
        SDataCfdPacType cfdPacType = null;
        String sql = "";
        ResultSet resultSet = null;
        int pacId = 0;
        int pacIdAux = 0;

        try {
            cfdPacType = getPacConfiguration(client, cfd.getFkCfdTypeId());

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

    private static boolean stampedCfdiFinkok(final SClientInterface client, final SDataCfd cfd, final int subtypeCfd) throws Exception {
        boolean signed = false;

        if (canCfdiSign(client, cfd, true)) {
            managementCfdi(client, cfd, SDataConstantsSys.TRNS_ST_DPS_EMITED, null, true, true, 0, subtypeCfd);
            signed = true;
        }
        return signed;
    }

    private static boolean cancelCfdiFinkok(final SClientInterface client, final SDataCfd cfd, final int subtypeCfd, final int tpDpsAnn) throws Exception {
        boolean canceled = false;
        int pacId = 0;

        pacId = getPacCfdSignedId(client, cfd);

        if (canCfdiCancel(client, cfd, true)) {
            if (canCfdiCancelWebService(client, cfd, pacId)) {
                managementCfdi(client, cfd, SDataConstantsSys.TRNS_ST_DPS_ANNULED, client.getSessionXXX().getSystemDate(), true, true, pacId, subtypeCfd);
            }
            else {
                processAnnul(client, cfd, subtypeCfd, tpDpsAnn);
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
        SDataCfdPacType cfdPacType = getPacConfiguration(client, cfd.getFkCfdTypeId());

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
            client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            String userName = pac.getUser();
            String userPswd = pac.getUserPassword();

            /* Code used in fiscal stamp testing: (DO NOT DELETE! KEEPED JUST FOR REFERENCE!)
            XmlProcess xmlProcess = null;
            oInputFile = new BufferedInputStream(new FileInputStream(sXmlBaseDir + cfd.getDocXmlName()));
            xmlProcess = new XmlProcess(oInputFile);
            sCfdi = xmlProcess.generaTextoXML();
            */

            String sCfdi = removeNode(cfd.getDocXml(), "cfdi:Addenda");    // production code for fiscal stamp

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
                        createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_PAC_AUTH, cfd, pac.getPkPacId());

                        autenticarResponse = fcgPort.autenticar(userName, userPswd);

                        if (autenticarResponse.getMensaje() != null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLog(client, "WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]", !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_PAC_AUTH, cfd, pac.getPkPacId());

                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            System.err.println("WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]");
                            throw new Exception("Error al autenticarse con el PAC:\nMensaje: " + autenticarResponse.getMensaje());
                        }

                        if (autenticarResponse.getToken() == null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLog(client, "WsAutenticarResponse Token is null!", !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_PAC_AUTH, cfd, pac.getPkPacId());

                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            System.err.println("WsAutenticarResponse Token is null!");
                            throw new Exception("Error al autenticarse con el PAC:\n¡Token nulo!");
                        }

                        // Document stamp:

                        // Sign & Cancel Log step #2
                        createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_FLAGS_SET, cfd, pac.getPkPacId());

                        updateProcessCfd(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_SEND_RECV, cfd, pac.getPkPacId());

                        timbradoResponse = fcgPort.timbrar(sCfdi, autenticarResponse.getToken());

                        if (timbradoResponse.getMensaje() != null) {
                            // Sign & Cancel Log step #4
                            createSignCancelLog(client, "WsTimbradoResponse Mensaje: [" + timbradoResponse.getMensaje() + "]", !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd, pac.getPkPacId());

                            updateProcessCfd(client, cfd, false);
                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            System.err.println("WsTimbradoResponse Codigo: [" + timbradoResponse.getCodigo() + "]");
                            System.err.println("WsTimbradoResponse Mensaje: [" + timbradoResponse.getMensaje() + "]");
                            System.err.println("Cfdi: [" + sCfdi + "]");
                            throw new Exception("Error al timbrar el documento:\nCódigo: " + timbradoResponse.getCodigo() + "\nMensaje: " + timbradoResponse.getMensaje());
                        }

                        xml = timbradoResponse.getCfdi();
                        break;

                    case SModSysConsts.TRN_PAC_FNK:
                        // Document stamp:

                        // Sign & Cancel Log step #1, not required!
                        // Sign & Cancel Log step #2
                        createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_FLAGS_SET, cfd, pac.getPkPacId());

                        updateProcessCfd(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_SEND_RECV, cfd, pac.getPkPacId());

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
                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                            if (isValidation) {
                                for (Incidencia i : maoIncidencias.getValue().getIncidencia()) {
                                    if (i.getCodigoError().getValue().compareTo(SCfdConsts.UUID_STAMP_NOT_YET) == 0) {
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

                                    if (i.getCodigoError().getValue().compareTo(SCfdConsts.UUID_STAMP_ALREADY) == 0) {
                                        updateProcessCfd(client, cfd, true);
                                    }
                                }
                            }
                            
                            // Sign & Cancel Log step #4
                            createSignCancelLog(client, sMessageException, !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd, pac.getPkPacId());

                            System.err.println("Cfdi: [" + sCfdi + "]");
                            throw new Exception("Error al timbrar el documento: " + sMessageException);
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
                        createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_PAC_AUTH, cfd, pac.getPkPacId());

                        autenticarResponse = fcgPort.autenticar("pruebasWS", "pruebasWS");

                        if (autenticarResponse.getMensaje() != null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLog(client, "WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]", !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_PAC_AUTH, cfd, pac.getPkPacId());

                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            System.err.println("WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]");
                            throw new Exception("Error al autenticarse con el PAC:\nMensaje: " + autenticarResponse.getMensaje());
                        }

                        if (autenticarResponse.getToken() == null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLog(client, "WsAutenticarResponse Token is null!", !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_PAC_AUTH, cfd, pac.getPkPacId());

                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            System.err.println("WsAutenticarResponse Token is null!");
                            throw new Exception("Error al autenticarse con el PAC:\n¡Token nulo!");
                        }

                        // Document stamp:

                        // Sign & Cancel Log step #2
                        createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_FLAGS_SET, cfd, pac.getPkPacId());

                        updateProcessCfd(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_SEND_RECV, cfd, pac.getPkPacId());

                        timbradoResponse = fcgPort.timbrar(sCfdi, autenticarResponse.getToken());

                        if (timbradoResponse.getMensaje() != null) {
                            // Sign & Cancel Log step #4
                            createSignCancelLog(client, "WsTimbradoResponse Mensaje: [" + timbradoResponse.getMensaje() + "]", !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd, pac.getPkPacId());

                            updateProcessCfd(client, cfd, false);
                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            System.err.println("WsTimbradoResponse Codigo: [" + timbradoResponse.getCodigo() + "]");
                            System.err.println("WsTimbradoResponse Mensaje: [" + timbradoResponse.getMensaje() + "]");
                            System.err.println("Cfdi: [" + sCfdi + "]");
                            throw new Exception("Error al timbrar el documento:\nCódigo: " + timbradoResponse.getCodigo() + "\nMensaje: " + timbradoResponse.getMensaje());
                        }

                        xml = timbradoResponse.getCfdi();
                        break;

                    case SModSysConsts.TRN_PAC_FNK:
                        // NOTE 1: these are not really clasess of a testing environment, but production ones!
                        // NOTE 2: there is a set of testing-environment clasess in library ClientWSFinkok2018, but they are not implemented here yet.
                        
                        // Document stamp:

                        // Sign & Cancel Log step #1, not required!
                        // Sign & Cancel Log step #2, only when validation is not requested
                        createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_FLAGS_SET, cfd, pac.getPkPacId());

                        updateProcessCfd(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_SIGN : SCfdConsts.ACTION_CODE_VAL_SIGN, SCfdConsts.STEP_CODE_SEND_RECV, cfd, pac.getPkPacId());

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
                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

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
                            createSignCancelLog(client, sMessageException, !isProcessingValidation ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                            System.err.println("Cfdi: [" + sCfdi + "]");
                            throw new Exception("Error al timbrar el documento: " + sMessageException);
                        }

                        xml = acuseRecepcionCFDI.getXml().getValue();
                        */
                        xml = sCfdi;    // 2018-08-17, Sergio Flores: scamp code snippet to emulate a stamped CFDI
                        
                        break;
                        
                    default:
                }
            }
        }
        client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        return xml;
    }

    /**
     * Cancel CFD.
     * @param client ERP Client interface.
     * @param sUuid Universally unique identifier to cancel.
     * @param date Date of cancelation.
     * @throws Exception
     */
    private static String cancel(final SClientInterface client, final SDataCfd cfd, final Date date, boolean isValidation, final int pacId) throws Exception {
        String xmlAcuse = "";
        boolean next = true;
        SDataPac pac = null;

        if (isValidation) {
            // cancel validation:
            pac = getPacForValidation(client, cfd);
        }
        else {
            // cancel:
            SDataCfdPacType cfdPacType = getPacConfiguration(client, cfd.getFkCfdTypeId());
            
            if (pacId != 0 || cfdPacType != null) {
                pac = (SDataPac) SDataUtilities.readRegistry(client, SDataConstants.TRN_PAC, new int[] { pacId != 0 ? pacId : cfdPacType.getFkPacId() }, SLibConstants.EXEC_MODE_SILENT);
            }
        }

        if (pac == null) {
            throw new Exception("Error al leer el catálogo de PAC's.\n"
                    + "No existe ningún PAC registrado para la cancelación de CFDI.");
        }
        else {
            String fiscalIdIssuer = client.getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId();
            String userName = pac.getUser();
            String userPswd = pac.getUserPassword();
            SDataCertificate companyCertificate = client.getSessionXXX().getParamsCompany().getDbmsDataCertificate_n();
            
            ArrayList<String> uuidArray = new ArrayList<>();
            uuidArray.add(cfd.getUuid());

            client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
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
                        forsedi.timbrado.WsFoliosResponse foliosResponse = null;

                        fcgService = new forsedi.timbrado.WSForcogsaService();
                        fcgPort = fcgService.getWSForcogsaPort();

                        // Web Service Autentication:

                        // Sign & Cancel Log step #1
                        createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_AUTH, cfd, pac.getPkPacId());

                        autenticarResponse = fcgPort.autenticar(userName, userPswd);

                        if (autenticarResponse.getMensaje() != null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLog(client, "WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_AUTH, cfd, pac.getPkPacId());

                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                            if (pacId == 0) {
                                System.err.println("WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]");
                                throw new Exception("Error al autenticarse con el PAC:\nMensaje: " + autenticarResponse.getMensaje());
                            }
                            next = false;
                        }

                        if (autenticarResponse.getToken() == null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLog(client, "WsAutenticarResponse Token is null!", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_AUTH, cfd, pac.getPkPacId());

                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                            if (pacId == 0) {
                                System.err.println("WsAutenticarResponse Token is null!");
                                throw new Exception("Error al autenticarse con el PAC:\n¡Token nulo!");
                            }
                            next = false;
                        }

                        if (next) {
                            // Document cancel:

                            // Sign & Cancel Log step #2
                            createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_FLAGS_SET, cfd, pac.getPkPacId());

                            updateProcessCfd(client, cfd, true);

                            // Sign & Cancel Log step #3
                            createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_SEND_RECV, cfd, pac.getPkPacId());

                            canceladoResponse = fcgPort.cancelacion1(fiscalIdIssuer, SLibUtils.DbmsDateFormatDate.format(date), uuidArray, companyCertificate.getExtraPublicKeyBytes_n(), companyCertificate.getExtraPrivateKeyBytes_n(), "", autenticarResponse.getToken());

                            if (canceladoResponse.getMensaje() != null) {
                                // Sign & Cancel Log step #4
                                createSignCancelLog(client, "WsCancelacionResponse Mensaje: [" + canceladoResponse.getMensaje() + "]", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd, pac.getPkPacId());

                                updateProcessCfd(client, cfd, false);
                                client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                if (pacId == 0) {
                                    System.err.println("WsCancelacionResponse Codigo: [" + canceladoResponse.getCodEstatus() + "]");
                                    System.err.println("WsCancelacionResponse Mensaje: [" + canceladoResponse.getMensaje() + "]");
                                    System.err.println("UUID: [" + uuidArray + "]\t");
                                    throw new Exception("Error al cancelar el documento:\nCódigo: " + canceladoResponse.getCodEstatus() + "\nMensaje: " + canceladoResponse.getMensaje());
                                }
                                next = false;
                            }

                            if (next) {
                                foliosResponse = canceladoResponse.getFolios();

                                if (foliosResponse.getFolio().get(0).getEstatusUUID().compareTo(SCfdConsts.UUID_CANCEL_OK) != 0) {
                                    if (foliosResponse.getFolio().get(0).getEstatusUUID().compareTo(SCfdConsts.UUID_CANCEL_ALREADY) != 0) {
                                        // Sign & Cancel Log step #4
                                        createSignCancelLog(client, foliosResponse.getFolio().get(0).getMensaje(), !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd, pac.getPkPacId());

                                        updateProcessCfd(client, cfd, false);
                                        client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

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
                        // Document cancel:

                        // Sign & Cancel Log step #1, not required!
                        // Sign & Cancel Log step #2, only when validation is not requested
                        createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_FLAGS_SET, cfd, pac.getPkPacId());

                        updateProcessCfd(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_SEND_RECV, cfd, pac.getPkPacId());
                        
                        // Check CFD cancel status:
                        
                        boolean getAckCancellation = false;
                        SCfdUtilsHandler cfdUtilsHandler = new SCfdUtilsHandler(client);
                        SCfdUtilsHandler.CfdiAckQuery ackQuery = cfdUtilsHandler.getCfdiSatStatus(cfd);
                        
                        switch (ackQuery.CfdiStatus) {
                            case DCfdi33Consts.CFDI_ESTATUS_CAN:
                                // CFDI is 'cancelled' before fiscal authority, but is still active in system:
                                
                                getAckCancellation = true;
                                break;

                            case DCfdi33Consts.CFDI_ESTATUS_VIG:
                                // CFDI is 'active' before fiscal authority:
                                
                                if (isValidation) {
                                    updateProcessCfd(client, cfd, false);

                                    // Sign & Cancel Log step #3
                                    createSignCancelLog(client, "", SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_FLAG_CLEAR, cfd, pac.getPkPacId());
                                    throw new Exception("El CFDI está vigente.");
                                }
                                
                                // check cancellable status:
                                switch (ackQuery.CancellableInfo) {
                                    case DCfdi33Consts.CANCELABLE_SIN_ACEPT:
                                    case DCfdi33Consts.CANCELABLE_CON_ACEPT:
                                        // CFDI is cancellable, go through...
                                        break;

                                    case DCfdi33Consts.CANCELABLE_NO:
                                        // CFDI is not cancellable:
                                        throw new Exception("El CFDI es no cancelable.\n " + ackQuery.composeCfdiRelated());
                                }

                                // check cancellation status:
                                switch (ackQuery.CancelStatus) {
                                    case DCfdi33Consts.ESTATUS_CANCEL_PROC: // CFDI cancellation is in process
                                        cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_PROC_CODE);
                                        client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_CFD);
                                        throw new Exception("La solicitud de cancelación del CFD está pendiente de ser aceptada o rechazada por el receptor.");

                                    case DCfdi33Consts.ESTATUS_CANCEL_RECH: // CFDI cancellation was rejected by receptor
                                        cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_RECH_CODE);
                                        client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_CFD);
                                        throw new Exception("La solicitud de cancelación del CFD fue rechazada por el receptor.");
                                        
                                    case DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT:
                                    case DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT:
                                    case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC:
                                        throw new Exception("El estatus de cancelación del CFD es inconsistente.");
                                        
                                    case DCfdi33Consts.ESTATUS_CANCEL_NINGUNO:
                                        // CFD about to be cancelled for the first time or maybe a cancellation is still in process (in pending buffer)!
                                        break;
                                        
                                    default:
                                        throw new Exception("El estatus de cancelación del CFD es desconocido.");
                                }
                                break;

                            case DCfdi33Consts.CFDI_ESTATUS_NO_ENC:
                                // CFDI was 'not found' before fiscal authority:
                                if (isValidation) {
                                    updateProcessCfd(client, cfd, false);

                                    // Sign & Cancel Log step #3
                                    createSignCancelLog(client, "", SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_FLAG_CLEAR, cfd, pac.getPkPacId());
                                    throw new Exception("El CFD no fue encontrado ante el SAT.");
                                }

                            default:
                        }
                        
                        CancelSOAP service = new CancelSOAP();
                        com.finkok.facturacion.cancel.Application port = service.getApplication();
                        
                        if (isValidation || getAckCancellation) {
                            // validating cancellation:
                            
                            ReceiptResult receiptResult = port.getReceipt(userName, userPswd, fiscalIdIssuer, cfd.getUuid(), "C");
                            
                            if (receiptResult != null) {
                                if (receiptResult.getSuccess() == null) {
                                    // Sign & Cancel Log step #4
                                    createSignCancelLog(client, receiptResult.getError().getValue(), !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd, pac.getPkPacId());

                                    updateProcessCfd(client, cfd, false);
                                    client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                    if (pacId == 0) {
                                        throw new Exception("Error al intentar obtener acuse de cancelación de CFDI.\n" + "Error: " + receiptResult.getError().getValue() + " ");
                                    }
                                    next = false;
                                }
                                else {
                                    if (!receiptResult.getSuccess().getValue()) {
                                        // Sign & Cancel Log step #4
                                        createSignCancelLog(client, "El UUID: '" + cfd.getUuid() + "' no ha sido cancelado.", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd, pac.getPkPacId());

                                        updateProcessCfd(client, cfd, false);

                                        if (pacId == 0) {
                                            throw new Exception("Error al intentar obtener acuse de cancelación de CFDI.\n" + "El UUID: '" + cfd.getUuid() + "' no ha sido cancelado.");
                                        }
                                        next = false;
                                    }
                                    else {
                                        xmlAcuse = receiptResult.getReceipt().getValue();
                                        xmlAcuse = xmlAcuse.replace("&lt;", "<"); // XXX, 2018-08-29, Sergio Flores: are really needed?
                                        xmlAcuse = xmlAcuse.replace("&gt;", ">"); // XXX, 2018-08-29, Sergio Flores: are really needed?
                                        
                                        switch (ackQuery.CancelStatus) {
                                            case DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT:
                                                cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT_CODE);
                                                break;
                                            case DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT:
                                                cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT_CODE);
                                                break;
                                            case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC:
                                                cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC_CODE);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            // cancelling CFDI:
                            
                            StringArray stringArray = new StringArray();
                            stringArray.getString().addAll(uuidArray);

                            QName qNameUuids = new QName("uuids");
                            JAXBElement<StringArray> elementUuids = new JAXBElement<>(qNameUuids, StringArray.class, stringArray);

                            UUIDS uuids = new UUIDS();
                            uuids.setUuids(elementUuids);

                            CancelaCFDResult cancelaCFDResult = port.cancel(uuids, userName, userPswd, fiscalIdIssuer, companyCertificate.getExtraFnkPublicKeyBytes_n(), companyCertificate.getExtraFnkPrivateKeyBytes_n(), true);
                            JAXBElement<FolioArray> elementFolios = cancelaCFDResult.getFolios();

                            if (elementFolios == null) {
                                // Sign & Cancel Log step #4
                                createSignCancelLog(client, cancelaCFDResult.getCodEstatus().getValue(), !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd, pac.getPkPacId());

                                updateProcessCfd(client, cfd, false);
                                client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                if (pacId == 0) {
                                    throw new Exception(cancelaCFDResult.getCodEstatus().getValue());
                                }
                                next = false;
                            }
                            else {
                                if (elementFolios.getValue().getFolio().isEmpty()) {
                                    // Sign & Cancel Log step #4
                                    createSignCancelLog(client, "Codigo: [" + cancelaCFDResult.getCodEstatus().getValue() + "] Error al intentar cancelar CFDI.", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd, pac.getPkPacId());

                                    updateProcessCfd(client, cfd, false);
                                    client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                    if (pacId == 0) {
                                        throw new Exception("Codigo: [" + cancelaCFDResult.getCodEstatus().getValue() + "] Error al intentar cancelar CFDI.");
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
                                            switch (estatusCancelacion) {
                                                case DCfdi33Consts.ESTATUS_CANCEL_PROC:
                                                    // CFDI cancellation is in process:
                                                    cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_PROC_CODE);
                                                    client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_CFD);
                                                    throw new Exception("La solicitud de cancelación del CFDI ha sido enviada al receptor.");

                                                case DCfdi33Consts.ESTATUS_CANCEL_RECH:
                                                    // CFDI cancellation was rejected by receptor:
                                                    cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_RECH_CODE);
                                                    client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_CFD);
                                                    throw new Exception("La solicitud de cancelación del CFDI ha sido rechazada por el receptor.");

                                                case DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT:
                                                case DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT:
                                                case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC:
                                                    // CFDI was canceled!:
                                                    String cancelStatusCode = "";
                                                    switch (estatusCancelacion) {
                                                        case DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT:
                                                            cancelStatusCode = DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT_CODE;
                                                            break;
                                                        case DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT:
                                                            cancelStatusCode = DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT_CODE;
                                                            break;
                                                        case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC:
                                                            cancelStatusCode = DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC_CODE;
                                                            break;
                                                        default:
                                                    }

                                                    cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, cancelStatusCode);
                                                    xmlAcuse = cancelaCFDResult.getAcuse().getValue();
                                                    xmlAcuse = xmlAcuse.replace("&lt;", "<"); // XXX, 2018-08-29, Sergio Flores: are really needed?
                                                    xmlAcuse = xmlAcuse.replace("&gt;", ">"); // XXX, 2018-08-29, Sergio Flores: are really needed?
                                                    break;
                                                    
                                                case DCfdi33Consts.ESTATUS_CANCEL_NINGUNO:
                                                    // CFDI in pending buffer:
                                                    cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_PEND_BUFF_CODE);
                                                    client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_CFD);
                                                    throw new Exception("El CFDI ya está en el controlador de espera (pending buffer), en proceso de ser cancelado.");
                                                    
                                                default:
                                                    // unexpected cancellation code status:
                                                    cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_CAN_ST, "?");
                                                    client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_CFD);
                                                    throw new Exception("El estatus de cancelación del CFDI es desconocido: [" + estatusCancelacion + "].");
                                            }
                                            break;
                                            
                                        default:
                                            // unexpected response code:
                                            
                                            // Sign & Cancel Log step #4
                                            createSignCancelLog(client, "Codigo: [" + estatusUuid + "] Error al intentar cancelar CFDI.", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd, pac.getPkPacId());

                                            updateProcessCfd(client, cfd, false);
                                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                            if (pacId == 0) {
                                                throw new Exception("Codigo: [" + estatusUuid + "] Error al intentar cancelar CFDI.");
                                            }
                                            next = false;
                                    }
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
                        createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_AUTH, cfd, pac.getPkPacId());

                        autenticarResponse = fcgPort.autenticar("pruebasWS", "pruebasWS");

                        if (autenticarResponse.getMensaje() != null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLog(client, "WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_AUTH, cfd, pac.getPkPacId());

                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                            if (pacId == 0) {
                                System.err.println("WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]");
                                throw new Exception("Error al autenticarse con el PAC:\nMensaje: " + autenticarResponse.getMensaje());
                            }
                            next = false;
                        }

                        if (autenticarResponse.getToken() == null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLog(client, "WsAutenticarResponse Token is null!", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_AUTH, cfd, pac.getPkPacId());

                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                            if (pacId == 0) {
                                System.err.println("WsAutenticarResponse Token is null!");
                                throw new Exception("Error al autenticarse con el PAC:\n¡Token nulo!");
                            }
                            next = false;
                        }

                        if (next) {
                            // Document cancel:

                            // Sign & Cancel Log step #2
                            createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_FLAGS_SET, cfd, pac.getPkPacId());

                            updateProcessCfd(client, cfd, true);

                            // Sign & Cancel Log step #3
                            createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_SEND_RECV, cfd, pac.getPkPacId());

                            canceladoResponse = fcgPort.cancelacion1(fiscalIdIssuer, SLibUtils.DbmsDateFormatDate.format(date), uuidArray, companyCertificate.getExtraPublicKeyBytes_n(), companyCertificate.getExtraPrivateKeyBytes_n(), "", autenticarResponse.getToken());

                            if (canceladoResponse.getMensaje() != null) {
                                // Sign & Cancel Log step #4
                                createSignCancelLog(client, "WsCancelacionResponse Codigo: [" + canceladoResponse.getCodEstatus() + "]", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd, pac.getPkPacId());

                                updateProcessCfd(client, cfd, false);
                                client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                if (pacId == 0) {
                                    System.err.println("WsCancelacionResponse Codigo: [" + canceladoResponse.getCodEstatus() + "]");
                                    System.err.println("WsCancelacionResponse Mensaje: [" + canceladoResponse.getMensaje() + "]");
                                    System.err.println("UUID: [" + uuidArray + "]\t");
                                    throw new Exception("Error al cancelar el documento:\nCódigo: " + canceladoResponse.getCodEstatus() + "\nMensaje: " + canceladoResponse.getMensaje());
                                }
                                next = false;
                            }

                            if (next) {
                                foliosResponse = canceladoResponse.getFolios();

                                if (foliosResponse.getFolio().get(0).getEstatusUUID().compareTo(SCfdConsts.UUID_CANCEL_OK) != 0) {
                                    if (foliosResponse.getFolio().get(0).getEstatusUUID().compareTo(SCfdConsts.UUID_CANCEL_ALREADY) != 0) {
                                        // Sign & Cancel Log step #4
                                        createSignCancelLog(client, foliosResponse.getFolio().get(0).getMensaje(), !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_PAC_RECV_ERR, cfd, pac.getPkPacId());

                                        updateProcessCfd(client, cfd, false);

                                        if (pacId == 0) {
                                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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

                        // Sign & Cancel Log step #1, not required!
                        // Sign & Cancel Log step #2, only when validation is not requested
                        createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_FLAGS_SET, cfd, pac.getPkPacId());

                        updateProcessCfd(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLog(client, "", !isValidation ? SCfdConsts.ACTION_CODE_PRC_ANNUL : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_SEND_RECV, cfd, pac.getPkPacId());

                        CancelSOAP service = new CancelSOAP();
                        com.finkok.facturacion.cancel.Application port = service.getApplication();
                        
                        /* 2018-08-17, Sergio Flores: Code snippet commented for preventing connection with PAC and subsequent exception due to inactive account:
                        if (isProcessingValidation) {
                            ReceiptResult receiptResult = port.getReceipt("jbarajas@tron.com.mx", "WSfink_2014", sRfcEmisor, cfd.getUuid(), "C");
                            
                            if (receiptResult != null) {
                                if (receiptResult.getSuccess() == null) {
                                    // Sign & Cancel Log step #4
                                    createSignCancelLog(client, receiptResult.getError().getValue(), !isProcessingValidation ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                                    updateProcessCfd(client, cfd, false);
                                    client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                    if (pacId == 0) {
                                        throw new Exception("Error al intentar obtener acuse de cancelación de CFDI.\n" + "Error: " + receiptResult.getError().getValue() + " ");
                                    }
                                    next = false;
                                }
                                else {
                                    if (!receiptResult.getSuccess().getValue()) {
                                        // Sign & Cancel Log step #4
                                        createSignCancelLog(client, "El UUID: '" + cfd.getUuid() + "' no ha sido cancelado.", !isProcessingValidation ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                                        updateProcessCfd(client, cfd, false);

                                        if (pacId == 0) {
                                            throw new Exception("Error al intentar obtener acuse de cancelación de CFDI.\n" + "El UUID: '" + cfd.getUuid() + "' no ha sido cancelado.");
                                        }
                                        next = false;
                                    }
                                    else {
                                        xmlAcuse = receiptResult.getReceipt().getValue();
                                        xmlAcuse = xmlAcuse.replace("&lt;", "<"); // XXX, 2018-08-29, Sergio Flores: are really needed?
                                        xmlAcuse = xmlAcuse.replace("&gt;", ">"); // XXX, 2018-08-29, Sergio Flores: are really needed?
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
                                createSignCancelLog(client, cancelaCFDResult.getCodEstatus().getValue(), !isProcessingValidation ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                                updateProcessCfd(client, cfd, false);
                                client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                if (pacId == 0) {
                                    throw new Exception(cancelaCFDResult.getCodEstatus().getValue());
                                }
                                next = false;
                            }
                            else {
                                if (elementFolios.getValue().getFolio().isEmpty()) {
                                    // Sign & Cancel Log step #4
                                    createSignCancelLog(client, "Codigo: [" + cancelaCFDResult.getCodEstatus().getValue() + "] Error al intentar cancelar CFDI.", !isProcessingValidation ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                                    updateProcessCfd(client, cfd, false);
                                    client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                    if (pacId == 0) {
                                        throw new Exception("Codigo: [" + cancelaCFDResult.getCodEstatus().getValue() + "] Error al intentar cancelar CFDI.");
                                    }
                                    next = false;
                                }
                                else {
                                    if (elementFolios.getValue().getFolio().get(0).getEstatusUUID().getValue().compareTo(SCfdConsts.UUID_ANNUL) != 0) {
                                        if (elementFolios.getValue().getFolio().get(0).getEstatusUUID().getValue().compareTo(SCfdConsts.UUID_ANNUL_PREV) != 0) {
                                            // Sign & Cancel Log step #4
                                            createSignCancelLog(client, "Codigo: [" + elementFolios.getValue().getFolio().get(0).getEstatusUUID().getValue() + "] Error al intentar cancelar CFDI.", !isProcessingValidation ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                                            updateProcessCfd(client, cfd, false);
                                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

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
                        xmlAcuse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" FechaTimbrado=\"2018-08-16T16:00:02\" NoCertificadoSAT=\"00001000000405332712\" RfcProvCertif=\"FIN1203015JA\" SelloCFD=\"ONDe5/YJVqRfuohNNJA/UjxlR5SIwPlASe2/cKZaDHGQ7XKCOUefidDP3Szk9hHIE8hpJGoXjGqETQ/WKotYMcyOqzR+g0F5SGfhz34NZGVuffBLl4Co073g5ZeWGKiM6WXlim2njxxkhIqBTnf5BMcc7WqtyVfOGnwEZlXhx8kIbYKKWrSqEo72hldAZc8xrGkRikUUzp3aS6z5kDjfRcfIqSyBX1z7fOSjgYT9MXVezgEwKjwrhFUydrtz0Jqd5+KycPcHzedKJo6kbtnDmgKeLiZejGKobJ6VSlbXSYdiL+2Mt58WmUkG3JGCEzGXiBSO6ayz1Hmwjrr3rX84BA==\" SelloSAT=\"byIDPVs6qpW+D76RYX9RbZB4+inyp0QjYqzvX5Q0TObgWn9kcNKKsQ94C1OZrGon5qQx65WMlVjQsjSju6pf0Od6042c9S6emU1ANR3dSrcgtn0FjoNukj6lpgEt992hmf74D3wryVfrsc+NlCTuxFxpN0pO5Z2VADHie3GZRBzH9bH3ul8zO8hkihSqZNd1qtNQX3pW2KYnjaG6nQV0Obq441V1W483IUYxscsCrtDLrRyKvPBJQHNUuKAVyTKqzbJpD4u0tRudLIXtpSk9bj5f6ctYbl5ebZuMzOA3p7Nly/qkRoH2onLcZnx45dxJxid8hukCEojbVtW6jBps4w==\" UUID=\"5E43EBB7-D01A-4D44-A372-4A979A4778D4\" Version=\"1.1\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\"/>";  // 2018-08-17, Sergio Flores: scamp code snippet to emulate a cancelled CFDI
                        break;
                        
                    default:
                }
            }
        }
        client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

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

            if (moDps.getFkCompanyBranchId() == miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getPkBizPartnerBranchId()) {
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

            if (moDps.getDbmsDataAddenda() != null && bizPartner.getDbmsCategorySettingsCus().getFkCfdAddendaTypeId() != SDataConstantsSys.BPSS_TP_CFD_ADD_NA) {
                params.setTipoAddenda(moDps.getDbmsDataAddenda().getFkCfdAddendaTypeId());
                params.setLorealFolioNotaRecepcion(moDps.getDbmsDataAddenda().getLorealFolioNotaRecepcion());
                params.setBachocoSociedad(moDps.getDbmsDataAddenda().getBachocoSociedad());
                params.setBachocoOrganizacionCompra(moDps.getDbmsDataAddenda().getBachocoOrganizacionCompra());
                params.setBachocoDivision(moDps.getDbmsDataAddenda().getBachocoDivision());
                params.setModeloDpsDescripcion(moDps.getDbmsDataAddenda().getModeloDpsDescripcion());
                
                params.setSorianaTienda(moDps.getDbmsDataAddenda().getSorianaTienda());
                params.setSorianaEntregaMercancia(moDps.getDbmsDataAddenda().getSorianaEntregaMercancia());
                params.setSorianaFechaRemision(moDps.getDbmsDataAddenda().getSorianaRemisionFecha());
                params.setSorianaFolioRemision(moDps.getDbmsDataAddenda().getSorianaRemisionFolio());
                params.setSorianaFolioPedido(moDps.getDbmsDataAddenda().getSorianaPedidoFolio());
                params.setSorianaTipoBulto(moDps.getDbmsDataAddenda().getSorianaBultoTipo());
                params.setSorianaCantidadBulto(moDps.getDbmsDataAddenda().getSorianaBultoCantidad());
                params.setSorianaNotaEntradaFolio(moDps.getDbmsDataAddenda().getSorianaNotaEntradaFolio());
                params.setSorianaCita(moDps.getDbmsDataAddenda().getSorianaCita());
                
                params.setCfdAddendaSubtype(moDps.getDbmsDataAddenda().getCfdAddendaSubtype());
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
                    params.setRegimenFiscal(new String[] { moDps.getDbmsDataDpsCfd().getTaxRegime() });
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }

            params.setAgregarAddenda(true); // WARNING: This line of code differs from counterpart method in class SFormDps!!!
            
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
    
    private static void  processAnnul(final SClientInterface client, final SDataCfd cfd, final int subtypeCfd, final int tpDpsAnn) throws Exception {
        int result = SLibConstants.UNDEFINED;
        String error = "";
        SDataDps dps = null;
        SDataPayrollReceiptIssue receiptIssue = null;
        SSrvLock lock = null;
        SServerRequest request = null;
        SServerResponse response = null;

        try {
            switch (cfd.getFkCfdTypeId()) {
                case SDataConstantsSys.TRNS_TP_CFD_INV:
                    // Annul DPS:

                    dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, new int[] { cfd.getFkDpsYearId_n(), cfd.getFkDpsDocId_n() }, SLibConstants.EXEC_MODE_SILENT);

                    // Attempt to gain data lock:

                    lock = SSrvUtils.gainLock(client.getSession(), client.getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.TRN_DPS, dps.getPrimaryKey(), 1000 * 60);     // 1 minute timeout

                    if (dps != null) {
                        request = new SServerRequest(SServerConstants.REQ_DB_CAN_ANNUL);
                        request.setPacket(dps);
                        response = client.getSessionXXX().request(request);

                        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                            error = response.getMessage();
                        }
                        else {
                            result = response.getResultType();

                            if (result != SLibConstants.DB_CAN_ANNUL_YES) {
                                error = SLibConstants.MSG_ERR_DB_REG_ANNUL_CAN + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage());
                            }
                            else {
                                // Annul registry:

                                dps.setIsRegistryRequestAnnul(true);
                                dps.setFkDpsAnnulationTypeId(tpDpsAnn);
                                dps.setFkUserEditId(client.getSession().getUser().getPkUserId());

                                request = new SServerRequest(SServerConstants.REQ_DB_ACTION_ANNUL);
                                request.setPacket(dps);
                                response = client.getSessionXXX().request(request);

                                if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                                    error = response.getMessage();
                                }
                                else {
                                    result = response.getResultType();

                                    if (result != SLibConstants.DB_ACTION_ANNUL_OK) {
                                        error = SLibConstants.MSG_ERR_DB_REG_ANNUL + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage());
                                    }
                                }
                            }
                        }
                    }
                    else {
                        error = SLibConstants.MSG_ERR_DB_REG_READ;
                    }
                    break;
                    
                case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                    // no action needed
                    break;
                    
                case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                    // Annul Payroll CFDI:

                    if (subtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_CUR) {
                        receiptIssue = new SDataPayrollReceiptIssue();

                        if (receiptIssue.read(new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n(), cfd.getFkPayrollReceiptIssueId_n() }, client.getSession().getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }

                        // Attempt to gain data lock:

                        lock = SSrvUtils.gainLock(client.getSession(), client.getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.TRN_DPS, receiptIssue.getPrimaryKey(), 1000 * 60);     // 1 minute timeout

                        if (receiptIssue != null) {
                            request = new SServerRequest(SServerConstants.REQ_DB_CAN_ANNUL);
                            request.setPacket(receiptIssue);
                            response = client.getSessionXXX().request(request);

                            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                                error = response.getMessage();
                            }
                            else {
                                result = response.getResultType();

                                if (result != SLibConstants.DB_CAN_ANNUL_YES) {
                                    error = SLibConstants.MSG_ERR_DB_REG_ANNUL_CAN + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage());
                                }
                                else {
                                    // Annul registry:

                                    receiptIssue.setIsRegistryRequestAnnul(true);
                                    receiptIssue.setFkUserUpdateId(client.getSession().getUser().getPkUserId());

                                    request = new SServerRequest(SServerConstants.REQ_DB_ACTION_ANNUL);
                                    request.setPacket(receiptIssue);
                                    response = client.getSessionXXX().request(request);

                                    if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                                        error = response.getMessage();
                                    }
                                    else {
                                        result = response.getResultType();

                                        if (result != SLibConstants.DB_ACTION_ANNUL_OK) {
                                            error = SLibConstants.MSG_ERR_DB_REG_ANNUL + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage());
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            error = SLibConstants.MSG_ERR_DB_REG_READ;
                        }
                    }
                    break;
                    
                default:
            }
            
            if (!error.isEmpty()) {
                throw new Exception(error);
            }
        }
        catch (Exception e) {
            if (lock != null) {
                SSrvUtils.releaseLock(client.getSession(), lock);
            }
            throw e;
        }
        finally {
            if (lock != null) {
                SSrvUtils.releaseLock(client.getSession(), lock);
            }
        }
    }

    /**
     * Obtain CFDI signature.
     * @param xml CFDI in XML format.
     * @return CFDI signature.
     */
    private static SCfdiSignature obtainCfdiSignature(String xml) {
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
                else if ((version.compareTo("" + DCfdConsts.CFDI_VER_33) == 0)) {
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

    private static void updateProcessCfd(final SClientInterface client, final SDataCfd cfd, final boolean value) throws Exception {
        cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_PRC_WS, value);
        cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_PRC_STO_XML, value);
        cfd.saveField(client.getSession().getDatabase().getConnection(), SDataCfd.FIELD_PRC_STO_PDF, value);
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
    private static boolean belongsXmlToCfd(final String xml, final SDataCfd cfd) throws Exception {
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
    
    /*
     * Public static methods:
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

    public static boolean validateEmisorXmlExpenses(final SClientInterface client, final String fileXml) throws Exception {
        DocumentBuilder docBuilder = null;
        Document doc = null;
        Node node = null;
        NamedNodeMap namedNodeMap = null;
        String receptorXml = "";
        String xml = "";
        
        try {
            xml = SXmlUtils.readXml(fileXml);
        } 
        catch(Exception e) {
            throw new Exception("El XML no es válido");
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
                throw new Exception("El receptor del archivo XML no es la empresa '" + client.getSessionXXX().getCompany().getDbmsDataCompany().getBizPartner() + "'.");
            }
        }
        
        return true;
    }

    public static boolean existsCfdiPending(final SClientInterface client, final ArrayList<SDataCfd> cfds) throws Exception {
        if (cfds != null) {
            for (SDataCfd cfd : cfds) {
                if (cfd.getIsProcessingWebService()) {
                    throw new Exception("Existen CFDI pendientes de respuesta del Proveedor Autorizado de Certificación (PAC).");
                }
                else if (cfd.getIsProcessingStorageXml()) {
                    throw new Exception("Existen CFDI pendientes de almacenar en el disco.");
                }
            }
        }

        return true;
    }

    public static void resetCfdiDiactivateFlags(final SClientInterface client, final SDataCfd cfd) throws Exception {
        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else {
            if (!isSignedByFinkok(client, cfd)) {
                if (client.showMsgBoxConfirm("Si limpia las inconsistencias del timbrado o cancelación del CFDI puede " + (!cfd.isStamped() ? "timbrarse dos veces" : "ser que ya esté cancelado") + " el CFDI.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                    updateProcessCfd(client, cfd, false);
                    LogSignId = 0;
                    createSignCancelLog(client, "", SCfdConsts.ACTION_CODE_RESET_FLAGS, SCfdConsts.STEP_CODE_NA, cfd, SLibConsts.UNDEFINED);
                }
            }
            else {
                throw new Exception("El PAC sí permite la verificación del timbrado o cancelación del CFDI, favor de intentarlo.");
            }
        }
    }

    public static boolean signCfdi(final SClientInterface client, final SDataCfd cfd, final int subtypeCfd, boolean isSingle, boolean confirmSending) throws Exception {
        boolean signed = false;
        
        if (canCfdiSign(client, cfd, false)) {
            if (!isSingle || !confirmSending || client.showMsgBoxConfirm("¿Está seguro que desea timbrar el documento?") == JOptionPane.YES_OPTION) {
                // Open Sign & Cancel Log entry:
                
                LogSignId = 0;
                createSignCancelLog(client, "", SCfdConsts.ACTION_CODE_PRC_SIGN, SCfdConsts.STEP_CODE_NA, cfd, getPacConfiguration(client, cfd.getFkCfdTypeId()).getFkPacId());

                managementCfdi(client, cfd, SDataConstantsSys.TRNS_ST_DPS_EMITED, null, isSingle, false, 0, subtypeCfd);
                signed = true;
            }
        }
        
        return signed;
    }

    public static boolean cancelCfdi(final SClientInterface client, final SDataCfd cfd, final int subtypeCfd, final Date cancellationDate, boolean validateStamp, boolean isSingle, int tpDpsAnn) throws Exception {
        boolean canceled = false;
        boolean tryCanceled = true;
        int pacId = 0;

        pacId = getPacCfdSignedId(client, cfd);

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

                    if (!isSingle || client.showMsgBoxConfirm("La anulación de un CFDI no puede revertirse.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        // Open Sign & Cancel Log entry:

                        LogSignId = 0;
                        createSignCancelLog(client, "", SCfdConsts.ACTION_CODE_PRC_ANNUL, SCfdConsts.STEP_CODE_NA, cfd, pacId != 0 ? pacId : getPacConfiguration(client, cfd.getFkCfdTypeId()).getFkPacId());

                        if (canCfdiCancelWebService(client, cfd, pacId)) {
                            canceled = managementCfdi(client, cfd, SDataConstantsSys.TRNS_ST_DPS_ANNULED, cancellationDate, isSingle, false, pacId, subtypeCfd);
                        }
                        else {
                            processAnnul(client, cfd, subtypeCfd, tpDpsAnn);
                            canceled = true;
                        }
                    }
                    else {
                        pacId = 0;
                        updateProcessCfd(client, cfd, false);
                    }
                    tryCanceled = !(pacId == 0 || canceled);
                    pacId = 0;
                }
            }
            else {
                processAnnul(client, cfd, subtypeCfd, tpDpsAnn);
                canceled = true;
            }
        }
        
        return canceled;
    }
    
    private static void computePrintCfd(final SClientInterface client, final SDataCfd cfd, final int cfdSubtype, int printMode, int numberCopies) throws Exception {
        SDataDps dps = null;
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
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;

            case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                cfdPrint.printCfdi33_Crp10(client, cfd, printMode);
                break;

            case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                switch (cfd.getFkXmlTypeId()) {
                    case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                        if (DCfdUtils.getVersionPayrollComplement(cfd.getDocXml()) == DCfdVer3Consts.VER_NOM_11) {
                            cfdPrint.printPayrollReceipt32_11(cfd, printMode, numberCopies, cfdSubtype);
                        }
                        else if (DCfdUtils.getVersionPayrollComplement(cfd.getDocXml()) == DCfdVer3Consts.VER_NOM_12) {
                            cfdPrint.printPayrollReceipt32_12(cfd, printMode, numberCopies, cfdSubtype);
                        }
                        break;
                    case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                        if (DCfdUtils.getVersionPayrollComplement(cfd.getDocXml()) == DCfdVer3Consts.VER_NOM_11) {
                            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                        else if (DCfdUtils.getVersionPayrollComplement(cfd.getDocXml()) == DCfdVer3Consts.VER_NOM_12) {
                            cfdPrint.printPayrollReceipt33_12(cfd, printMode, numberCopies, cfdSubtype);
                        }
                        break;
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;

            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
    }

    public static void printCfd(final SClientInterface client, final SDataCfd cfd, final int cfdSubtype, int printMode, int numberCopies, boolean isSaveInProcess) throws Exception {
        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else {
            if (canPrint(cfd, isSaveInProcess)) {
                computePrintCfd(client, cfd, cfdSubtype, printMode, numberCopies);
            }
        }
    }

    public static void printCfds(final SClientInterface client, final ArrayList<SDataCfd> cfds, int numberCopies, final int subtypeCfd) throws Exception {
        ArrayList<SDataCfd> cfdsPrintable = new ArrayList<>();

        for (SDataCfd cfd : cfds) {
            if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                cfdsPrintable.add(cfd);
            }
        }

        if (cfdsPrintable.isEmpty()) {
            client.showMsgBoxInformation("No hay documentos para imprimir.");
        }
        else {
            if (client.showMsgBoxConfirm("¿Está seguro que desea imprimir " + cfdsPrintable.size() + " documentos?") == JOptionPane.YES_OPTION) {
                SDialogResult dialogResult = new SDialogResult((SClient) client, "Resultados de impresión", SCfdConsts.PROC_PRT_DOC);
                dialogResult.setFormParams(client, cfdsPrintable, null, 0, null, false, subtypeCfd, SModSysConsts.TRNU_TP_DPS_ANN_NA);
                dialogResult.setNumberCopies(numberCopies);
                dialogResult.setVisible(true);
            }
        }
    }

    public static void printCfdCancelAck(final SClientInterface client, final SDataCfd cfd, int printMode, final int subtypeCfd) throws Exception {
        SCfdPrint cfdPrint = null;

        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else {
            if (canObtainAcknowledgmentCancellation(client, cfd)) {
                if (!cfd.getAcknowledgmentCancellationXml().isEmpty()) {
                    cfdPrint = new SCfdPrint(client);

                    cfdPrint.printCancelAck(cfd, printMode, subtypeCfd);
                }
                else {
                    printAcknowledgmentCancellationPdf(client, cfd);
                }
            }
        }
    }

    /**
     * Sends a CFD
     * @param client ERP Client interface.
     * @param cfd CFI to be send.
     * @param typeCfd Type DPS or Type Payroll
     * @param subtypeCfd When the typeCfd is Payroll, subtype is old version or new version
     * @param isSingle It is true when there is one cfd
     * @param confirmSending It is true when the confirmation will be done.
     * @param catchExceptions When true all exceptions are handled internally, otherwise are shown into dialog messages.
     * @throws javax.mail.MessagingException, java.sql.SQLException
     */
    public static void sendCfd(final SClientInterface client, final int typeCfd, final SDataCfd cfd, final int subtypeCfd, boolean isSingle, boolean confirmSending, boolean catchExceptions) throws MessagingException, SQLException, Exception {
        boolean send = true;
        int idBizPartner = SLibConsts.UNDEFINED;
        int idBizPartnerBranch = SLibConsts.UNDEFINED;
        SDataDps dps = null;
        
        if (canSend(cfd)) {
            switch (typeCfd) {
                case SDataConstantsSys.TRNS_TP_CFD_INV:
                    dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, new int[] { cfd.getFkDpsYearId_n(), cfd.getFkDpsDocId_n() }, SLibConstants.EXEC_MODE_SILENT);
                    idBizPartner = dps.getFkBizPartnerId_r();
                    idBizPartnerBranch = dps.getFkBizPartnerBranchId();
                    break;

                case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                    idBizPartner = SDataCfdPayment.getDbmsDataReceptorId(client.getSession().getStatement(), cfd.getPkCfdId());
                    idBizPartnerBranch = SLibConsts.UNDEFINED; // do not really needed, just for consistence
                    break;
                        
                case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                    idBizPartner = subtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? cfd.getFkPayrollBizPartnerId_n() : cfd.getFkPayrollReceiptEmployeeId_n();
                    idBizPartnerBranch = SLibConsts.UNDEFINED; // do not really needed, just for consistence
                    break;

                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
            
            if (confirmSending) {
                send = STrnUtilities.confirmSend(client, TXT_SEND, cfd, null, idBizPartner, idBizPartnerBranch);
            }

            if (send) {
                STrnUtilities.sendMailCfd(client, cfd, subtypeCfd, SLibConstants.UNDEFINED, idBizPartner, idBizPartnerBranch, catchExceptions);

                if (isSingle) {
                    client.showMsgBoxInformation("El comprobante ha sido enviado correctamente.\n");
                }
            }
        }
    }
    
    /**
     * Sign and Send a CFD.
     * @param client ERP Client interface.
     * @param cfd CFI to be send.
     * @param subtypeCfd When the typeCfd is Payroll, subtype is old version or new version
     * @param isSingle It is true when there is one cfd
     * @param confirmSending It is true when the confirmation will be done.
     * @return 
     * @throws Exception
     */
    public static boolean signAndSendCfdi(final SClientInterface client, final SDataCfd cfd, final int subtypeCfd, final boolean isSingle, final boolean confirmSending) throws Exception {
        boolean signed = false;
        boolean signedAndSent = false;
        int idBizPartner = SLibConsts.UNDEFINED;
        int idBizPartnerBranch = SLibConsts.UNDEFINED;

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
                        idBizPartnerBranch = SLibConsts.UNDEFINED; // do not really needed, just for consistence
                        break;

                    case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                        idBizPartner = subtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? cfd.getFkPayrollBizPartnerId_n() : cfd.getFkPayrollReceiptEmployeeId_n();
                        idBizPartnerBranch = SLibConsts.UNDEFINED; // do not really needed, just for consistence
                        break;

                    default:
                }

                if (!confirmSending || STrnUtilities.confirmSend(client,TXT_SIGN_SEND, cfd, null, idBizPartner, idBizPartnerBranch)) {
                    signed = signCfdi(client, cfd, subtypeCfd, isSingle, false);

                    if (signed) {
                        // read again just signed CFDI:
                        SDataCfd cfdAuxSend = (SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, cfd.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                        
                        // send CFDI:
                        sendCfd(client, cfdAuxSend.getFkCfdTypeId(), cfdAuxSend, subtypeCfd, false, false, true);
                        signedAndSent = true;
                    }
                }
            }
        }
        catch (Exception e) {
            if (signed) {
                throw new Exception("Documento timbrado, pero no enviado:\n" + e.getMessage());
            }
            else {
                throw new Exception("No fue posible timbrar ni enviar el documento:\n" + e.getMessage());
            }
        }
        
        return signedAndSent;
    }

    public static boolean cancelAndSendCfdi(final SClientInterface client, final SDataCfd cfd, final int subtypeCfd, final Date cancellationDate, boolean validateStamp, boolean isSingle, int tpDpsAnn) throws Exception {
        boolean canceled = false;
        boolean tryCanceled = true;
        int pacId = 0;
        boolean sendNotification = false;
        SDataCfd cfdAuxSend = null;

        pacId = getPacCfdSignedId(client, cfd);

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

                    if (!isSingle || client.showMsgBoxConfirm("La anulación de un CFDI no puede revertirse.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        // Open Sign & Cancel Log entry:

                        LogSignId = 0;
                        createSignCancelLog(client, "", SCfdConsts.ACTION_CODE_PRC_ANNUL, SCfdConsts.STEP_CODE_NA, cfd, pacId != 0 ? pacId : getPacConfiguration(client, cfd.getFkCfdTypeId()).getFkPacId());

                        if (canCfdiCancelWebService(client, cfd, pacId)) {
                            canceled = managementCfdi(client, cfd, SDataConstantsSys.TRNS_ST_DPS_ANNULED, cancellationDate, isSingle, false, pacId, subtypeCfd);
                            sendNotification = true;
                        }
                        else {
                            processAnnul(client, cfd, subtypeCfd, tpDpsAnn);
                            canceled = true;
                        }
                    }
                    else {
                        pacId = 0;
                        updateProcessCfd(client, cfd, false);
                    }
                    tryCanceled = !(pacId == 0 || canceled);
                    pacId = 0;

                    // Send CFDI:

                    if (sendNotification) {
                        try {
                            cfdAuxSend = (SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, cfd.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                            sendCfd(client, cfdAuxSend.getFkCfdTypeId(), cfdAuxSend, subtypeCfd, false, false, false);
                        }
                        catch (Exception e) {
                            throw new Exception("Anulado, pero no enviado: " + e.getMessage());
                        }
                    }
                }
            }
            else {
                processAnnul(client, cfd, subtypeCfd, tpDpsAnn);
                canceled = true;
            }
        }
        
        return canceled;
    }
    
    public static boolean existsPacConfiguration(final SClientInterface client, final SDataCfd cfd) {
        boolean exists = false;

        try {
            exists = (getPacConfiguration(client, cfd.getFkCfdTypeId()) != null && !getPacConfiguration(client, cfd.getFkCfdTypeId()).getIsDeleted());
        }
        catch (Exception e) {
            SLibUtils.showException(SCfdUtils.class.getName(), e);
        }

        return exists;
    }

    /**
     * Compute CFD: generate CFD complementary information, create CFD's XML, sign or cancel it and save CFD registry on server.
     * @param client ERP Client interface.
     * @param dps DPS registry.
     * @param xmlType CFD's XML type. Constants defined in SDataConstantsSys (i.e. TRNS_TP_XML_CFD, TRNS_TP_XML_CFDI_32, TRNS_TP_XML_CFDI_33).
     * @throws Exception
     */
    public static void computeCfd(final SClientInterface client, final SDataDps dps, final int xmlType) throws Exception {
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
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }
            
            packet.setFkCfdTypeId(SDataConstantsSys.TRNS_TP_CFD_INV);
            packet.setFkXmlTypeId(xmlType);
            packet.setFkXmlDeliveryTypeId(params.getTipoAddenda() != SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA ? SModSysConsts.TRNS_TP_XML_DVY_NA : SModSysConsts.TRNS_TP_XML_DVY_WS_SOR);
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
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }

            saveCfd(client, packet);
        }
    }

    public static void computeCfdiPayroll(final SClientInterface client, final SHrsFormerPayroll formerPayroll, final boolean isRegenerateOnlyNonStampedCfdi) throws Exception {
        SHrsFormerPayroll formerPayrollDummy = null;
        ArrayList<SDataCfd> formerPayrollCfds = null;
        ArrayList<SDataCfd> formerPayrollCfdsEmited = null;
        SDbFormerPayrollImport payrollImport = null;

        client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));

        if (formerPayroll.isValidPayroll()) {
            formerPayrollCfdsEmited = new ArrayList<SDataCfd>();

            formerPayrollCfds = getPayrollCfds(client, SCfdConsts.CFDI_PAYROLL_VER_OLD, new int[] { formerPayroll.getPkNominaId() });

            for (SDataCfd cfd : formerPayrollCfds) {
                if (cfd.getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                    formerPayrollCfdsEmited.add(cfd);
                }
            }

            formerPayrollDummy = new SHrsFormerPayroll(client);

            if (!formerPayrollCfdsEmited.isEmpty()) {
                formerPayrollDummy.setPkNominaId(formerPayrollCfdsEmited.get(0).getFkPayrollPayrollId_n());
                formerPayrollDummy.setFecha(formerPayrollCfdsEmited.get(0).getTimestamp());

                formerPayrollDummy.renderPayroll(formerPayrollCfdsEmited);
            }

            validateReceiptsConsistent(client, formerPayroll, formerPayrollDummy, isRegenerateOnlyNonStampedCfdi);

            payrollImport = new SDbFormerPayrollImport();

            payrollImport.setPayrollId(formerPayroll.getPkNominaId());
            payrollImport.setRegenerateOnlyNonStampedCfdi(isRegenerateOnlyNonStampedCfdi);
            payrollImport.getCfdPackets().addAll(CfdPackets);

            SServerRequest request = new SServerRequest(SServerConstants.REQ_DB_ACTION_SAVE);
            request.setPacket(payrollImport);
            SServerResponse response = client.getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                throw new Exception(response.getMessage());
            }
            else {
                if (response.getResultType() != SLibConstants.DB_ACTION_SAVE_OK) {
                    client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    throw new Exception("Código de error al emitir el CFD: " + SLibConstants.MSG_ERR_DB_REG_SAVE + ".");
                }
                else {
                    client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    client.showMsgBoxInformation("CFDI de nómina generados correctamente.");
                }
            }
        }
        else {
            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            throw new Exception("Los CFDI de nómina '" + formerPayroll.getPkNominaId() + "' no se generaron correctamente.");
        }

        client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

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

        switch (xmlType) {
            case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                comprobanteCfdi33 = (cfd.ver33.DElementComprobante) createCfdi33RootElement(client, cfdPayment);
                cfdVersion = comprobanteCfdi33.getVersion();

                packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi33));
                packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW);
                break;
            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
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
            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        saveCfd(client, packet);
    }

    public static void  processAnnul(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int subtypeCfd, int tpDpsAnn) throws Exception {
        for (SDataCfd cfd : cfds) {
            processAnnul(client, cfd, subtypeCfd, tpDpsAnn);
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

    public static boolean verifyCfdi(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int subtypeCfd) throws Exception {
        boolean valid = false;
        ArrayList<SDataCfd> cfdsVerify = null;
        SDialogResult dialogResult = null;
        
        cfdsVerify = new ArrayList<>();
        
        if (cfds.isEmpty()) {
            client.showMsgBoxInformation("No existen documentos para verificar.");
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
                client.showMsgBoxInformation("No existen documentos para verificar.");
            }
            else {
                if (existsCfdiEmitInconsist(client, cfdsVerify)) {
                    int stampsAvailable = getStampsAvailable(client, cfdsVerify.get(0).getFkCfdTypeId(), cfdsVerify.get(0).getTimestamp(), 0);
                    dialogResult = new SDialogResult((SClient) client, "Resultados de verificación", SCfdConsts.PROC_REQ_VERIFY);
                    dialogResult.setFormParams(client, cfdsVerify, null, stampsAvailable, null, false, subtypeCfd, SModSysConsts.TRNU_TP_DPS_ANN_NA);
                    dialogResult.setVisible(true);
                }
            }
        }

        return valid;
    }

    public static boolean validateCfdi(final SClientInterface client, final SDataCfd cfd, final int cfdSubtype, final boolean showFinalMessage) throws Exception {
        boolean valid = false;

        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del comprobante fiscal.");
        }
        else if (!cfd.isCfdi()) {
            throw new Exception("El comprobante fiscal solicitado no es un CFDI.");
        }
        else {
            SDataPac pac = getPacForValidation(client, cfd);

            if (pac == null) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo fue posible determinar un PAC para la verificación del CFDI.");
            }

            if (!(cfd.getIsProcessingWebService() || cfd.getIsProcessingStorageXml() || cfd.getIsProcessingStoragePdf())) {
                throw new Exception("No es necesario validar el CFDI.");
            }
            else {
                client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));

                if (cfd.getIsProcessingWebService()) {
                    // Open Sign & Cancel Log entry:
                    
                    LogSignId = 0;
                    createSignCancelLog(client, "", !cfd.isStamped() ? SCfdConsts.ACTION_CODE_VAL_SIGN : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_NA, cfd, pac.getPkPacId());

                    if (!isSignedByFinkok(client, cfd)) {
                        // Request to user the file correct

                        if (!cfd.isStamped()) {
                            // Save file XML and set flag XML as correct:

                            valid = restoreSignXml(client, cfd, false, cfdSubtype);
                        }
                        else {
                            // Save file PDF and set flag PDF as correct:

                            valid = restoreAcknowledgmentCancellation(client, cfd, false, cfdSubtype);
                        }
                    }
                    else {
                        if (!cfd.isStamped()) {
                            valid = stampedCfdiFinkok(client, cfd, cfdSubtype);
                        }
                        else {
                            valid = getReceiptCancellationCfdi(client, cfd, cfdSubtype, SModSysConsts.TRNU_TP_DPS_ANN_NA);
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
                        computePrintCfd(client, cfd, cfdSubtype, SDataConstantsPrint.PRINT_MODE_PDF_FILE, 1);
                    }
                    
                    updateProcessCfd(client, cfd, false);
                    
                    if (showFinalMessage) {
                        client.showMsgBoxInformation(SLibConsts.MSG_PROCESS_FINISHED);
                    }
                }
                
                client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }

        return valid;
    }
    
    public static boolean restoreSignXml(final SClientInterface client, final SDataCfd cfd, final boolean isUser, final int subtypeCfd) throws Exception {
        SDialogRestoreCfdi restoreCfdi = null;
        String fileXml = "";
        SDataPac pac = null;
        boolean isRestore = false;

        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del comprobante fiscal.");
        }
        else if (!cfd.isCfdi()) {
            throw new Exception("El comprobante fiscal solicitado no es un CFDI.");
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

            if (isUser) {
                // Open Sign & Cancel Log entry:
                
                LogSignId = 0;
                createSignCancelLog(client, "", !cfd.isStamped() ? SCfdConsts.ACTION_CODE_VAL_SIGN : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_NA, cfd, pac == null ? 0 : pac.getPkPacId());
            }

            if (!belongsXmlToCfd(fileXml, cfd)) {
                createSignCancelLog(client, "El archivo XML proporcionado no pertenece al CFDI seleccionado.", !cfd.isStamped() ? SCfdConsts.ACTION_CODE_VAL_SIGN : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_NA, cfd, pac == null ? 0 : pac.getPkPacId());

                throw new Exception("El archivo XML proporcionado no pertenece al CFDI seleccionado.");
            }
            else {
                managementCfdi(client, cfd, SDataConstantsSys.TRNS_ST_DPS_EMITED, null, true, true, pac == null ? 0 : pac.getPkPacId(), subtypeCfd, fileXml, false);
                isRestore = true;
            }
        }

        return isRestore;
    }

    public static boolean restoreAcknowledgmentCancellation(final SClientInterface client, final SDataCfd cfd, final boolean isUser, final int subtypeCfd) throws Exception {
        SDialogRestoreCfdi restoreCfdi = null;
        SDataPac pac = null;
        boolean isRestore = false;

        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del comprobante fiscal.");
        }
        else if (!cfd.isCfdi()) {
            throw new Exception("El comprobante fiscal solicitado no es un CFDI.");
        }
        else {
            if (isUser) {
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

                if (isUser) {
                    // Open Sign & Cancel Log entry:
                    
                    LogSignId = 0;
                    createSignCancelLog(client, "", !cfd.isStamped() ? SCfdConsts.ACTION_CODE_VAL_SIGN : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_NA, cfd, pac.getPkPacId());
                }

                saveAcknowledgmentCancellationPdf(client, cfd, restoreCfdi.getFilePdf());
                managementCfdi(client, cfd, SDataConstantsSys.TRNS_ST_DPS_ANNULED, null, true, true, pac == null ? 0 : pac.getPkPacId(), subtypeCfd, "", true);
                isRestore = true;
            }
            else {
                createSignCancelLog(client, "El archivo proporcionado es erróneo.", !cfd.isStamped() ? SCfdConsts.ACTION_CODE_VAL_SIGN : SCfdConsts.ACTION_CODE_VAL_ANNUL, SCfdConsts.STEP_CODE_NA, cfd, pac.getPkPacId());
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
            cfdPacType = getPacConfiguration(client, cfd.getFkCfdTypeId());

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
     * @param dps DPS registry.
     * @return Available stamps.
     */
    public static int getStampsAvailable(final SClientInterface client, final int cfdType, final Date date, final int pacId)  {
        SDataCfdPacType cfdPacType = null;
        SDataPac pac = null;
        String sql = "";
        ResultSet resultSet = null;
        int nStampsAvailable = 0;

        try {
            cfdPacType = getPacConfiguration(client, cfdType);

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
        emisor.setIsEmisor(true, false);

        asociadoNegocios = emisor.getBizPartner();
        asociadoNegocios.setVersion(fVersion);

        comprobante.setEltEmisor((cfd.ver2.DElementEmisor) asociadoNegocios.createRootElementEmisor());
        comprobante.getAttLugarExpedicion().setString(asociadoNegocios.getCfdLugarExpedicion());
        comprobante.getAttNumCtaPago().setString(xmlCfd.getComprobanteNumCtaPago());

        for (DElement regimen : xmlCfd.getElementsEmisorRegimenFiscal()) {
            comprobante.getEltEmisor().getEltHijosRegimenFiscal().add((cfd.ver2.DElementRegimenFiscal) regimen);
        }

        receptor = new SDbCfdBizPartner(client);
        receptor.setBizPartnerIds(xmlCfd.getReceptorId(), xmlCfd.getReceptorSucursalId());

        asociadoNegocios = receptor.getBizPartner();

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
        emisor.setExpeditionBizPartnerIds(xmlCfdi.getEmisorId(), xmlCfdi.getEmisorSucursalId());
        emisor.setIsEmisor(true, hasIntCommerceNode);

        asociadoNegocios = emisor.getBizPartner();
        asociadoNegocios.setIsCfdiWithIntCommerce(hasIntCommerceNode);
        asociadoNegocios.setVersion(DCfdConsts.CFDI_VER_32);
        asociadoNegocios.setCfdiType(xmlCfdi.getCfdType());
        asociadoNegocios.setIsStateCodeAssociate(SLocUtils.hasAssociateStates(client.getSession(), asociadoNegocios.getBizPartnerCountryId()));

        comprobante.setEltEmisor((cfd.ver32.DElementEmisor) asociadoNegocios.createRootElementEmisor());
        comprobante.getAttLugarExpedicion().setString(asociadoNegocios.getCfdLugarExpedicion());
        comprobante.getAttNumCtaPago().setString(xmlCfdi.getComprobanteNumCtaPago());

        for (DElement regimen : xmlCfdi.getElementsEmisorRegimenFiscal()) {
            comprobante.getEltEmisor().getEltHijosRegimenFiscal().add((cfd.ver32.DElementRegimenFiscal) regimen);
        }

        receptor = new SDbCfdBizPartner(client);
        receptor.setBizPartnerIds(xmlCfdi.getReceptorId(), xmlCfdi.getReceptorSucursalId());

        asociadoNegocios = receptor.getBizPartner();
        asociadoNegocios.setIsCfdiWithIntCommerce(hasIntCommerceNode);
        asociadoNegocios.setVersion(DCfdConsts.CFDI_VER_32);
        asociadoNegocios.setCfdiType(xmlCfdi.getCfdType());
        asociadoNegocios.setIsStateCodeAssociate(SLocUtils.hasAssociateStates(client.getSession(), asociadoNegocios.getBizPartnerCountryId()));

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
    
    public static cfd.DElement createCfdi33RootElement(final SClientInterface client, final SCfdXmlCfdi33 xmlCfdi) throws Exception {
        //Comprobante:
        
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
        comprobante.getAttTipoDeComprobante().setString(xmlCfdi.getComprobanteTipoComprobante());
        comprobante.getAttMetodoPago().setString(xmlCfdi.getComprobanteMetodoPago());
        comprobante.getAttLugarExpedicion().setString(xmlCfdi.getComprobanteLugarExpedicion());
        comprobante.getAttConfirmacion().setString(xmlCfdi.getComprobanteConfirmacion());
        
        if (xmlCfdi.getCfdType() == SDataConstantsSys.TRNS_TP_CFD_PAY_REC) {
            comprobante.getAttSubTotal().setDecimals(0);
            comprobante.getAttTotal().setDecimals(0);
        }
        
        if (!xmlCfdi.getCfdiRelacionadosTipoRelacion().isEmpty() && !xmlCfdi.getCfdiRelacionados().isEmpty()) {
            cfd.ver33.DElementCfdiRelacionados cfdiRelacionados = new DElementCfdiRelacionados();
            cfdiRelacionados.getAttTipoRelacion().setString(xmlCfdi.getCfdiRelacionadosTipoRelacion());
            
            for (String uuid : xmlCfdi.getCfdiRelacionados()) {
                cfd.ver33.DElementCfdiRelacionado cfdiRelacionado = new cfd.ver33.DElementCfdiRelacionado();
                cfdiRelacionado.getAttUuid().setString(uuid);
                cfdiRelacionados.getEltCfdiRelacionados().add(cfdiRelacionado);
            }
            
            comprobante.setEltOpcCfdiRelacionados(cfdiRelacionados);
        }
        
        boolean hasIntCommerceNode = false; // this info is required at this point to properly set data of nodes Emisor and Receptor
        cfd.DElement elementComplement = xmlCfdi.getElementComplemento();

        if (elementComplement != null) {
            hasIntCommerceNode = ((cfd.ver33.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior") != null;
        }
        
        //Emisor:
        
        SDbCfdBizPartner emisor = new SDbCfdBizPartner(client);
        emisor.setBizPartnerIds(xmlCfdi.getEmisorId(), xmlCfdi.getEmisorSucursalId());
        emisor.setExpeditionBizPartnerIds(xmlCfdi.getEmisorId(), xmlCfdi.getEmisorSucursalId());
        emisor.setIsEmisor(true, hasIntCommerceNode);

        SCfdDataBizPartner emisorCfd = emisor.getBizPartner();
        emisorCfd.setIsCfdiWithIntCommerce(hasIntCommerceNode);
        emisorCfd.setVersion(DCfdConsts.CFDI_VER_33);
        emisorCfd.setCfdiType(xmlCfdi.getCfdType());

        cfd.ver33.DElementEmisor elementEmisor = (cfd.ver33.DElementEmisor) emisorCfd.createRootElementEmisor();
        
        elementEmisor.getAttRegimenFiscal().setString(xmlCfdi.getEmisorRegimenFiscal());
        
        if (hasIntCommerceNode) {
            ((cfd.ver3.cce11.DElementComercioExterior) ((cfd.ver33.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior")).setEltEmisor(emisorCfd.createRootElementEmisorIntCommerce());
        }

        comprobante.setEltEmisor(elementEmisor);
        
        //Receptor:

        SDbCfdBizPartner receptor = new SDbCfdBizPartner(client);
        receptor.setBizPartnerIds(xmlCfdi.getReceptorId(), xmlCfdi.getReceptorSucursalId());

        SCfdDataBizPartner receptorCfd = receptor.getBizPartner();
        receptorCfd.setIsCfdiWithIntCommerce(hasIntCommerceNode);
        receptorCfd.setVersion(DCfdConsts.CFDI_VER_33);
        receptorCfd.setCfdiType(xmlCfdi.getCfdType());

        cfd.ver33.DElementReceptor elementReceptor = (cfd.ver33.DElementReceptor) receptorCfd.createRootElementReceptor();
        
        elementReceptor.getAttUsoCFDI().setString(xmlCfdi.getReceptorUsoCFDI());
        
        if (hasIntCommerceNode) {
            ((cfd.ver3.cce11.DElementComercioExterior) ((cfd.ver33.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior")).setEltReceptor(receptorCfd.createRootElementReceptorIntCommerce());
            
            if (xmlCfdi.getDestinatarioId() != SLibConstants.UNDEFINED) {
                SDbCfdBizPartner destinatario = new SDbCfdBizPartner(client);
                destinatario.setBizPartnerIds(xmlCfdi.getDestinatarioId(), xmlCfdi.getDestinatarioSucursalId(), xmlCfdi.getDestinatarioDomicilioId());

                SCfdDataBizPartner destinatarioCfd = destinatario.getBizPartner();
                destinatarioCfd.setIsCfdiWithIntCommerce(hasIntCommerceNode);
                destinatarioCfd.setVersion(DCfdConsts.CFDI_VER_33);
                destinatarioCfd.setCfdiType(xmlCfdi.getCfdType());
                
                ((cfd.ver3.cce11.DElementComercioExterior) ((cfd.ver33.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior")).setEltDestinatario(destinatarioCfd.createRootElementDestinatarioIntCommerce());
            }
        }
        
        comprobante.setEltReceptor(elementReceptor);
        
        //Conceptos:

        for (SCfdDataConcepto concept : xmlCfdi.getElementsConcepto()) {
            comprobante.getEltConceptos().getEltConceptos().add(concept.createRootElementConcept33());
        }

        //Impuestos:

        if (!SLibUtils.belongsTo(xmlCfdi.getCfdType(), new int[] { SDataConstantsSys.TRNS_TP_CFD_PAYROLL, SDataConstantsSys.TRNS_TP_CFD_PAY_REC })) {
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
        
        if (SLibUtils.belongsTo(xmlCfdi.getCfdType(), new int[] { SDataConstantsSys.TRNS_TP_CFD_PAYROLL, SDataConstantsSys.TRNS_TP_CFD_PAY_REC })) {
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
            
            dSubtotalImporte = SLibUtils.round(oConcepto.getAttCantidad().getDouble() * oConcepto.getAttValorUnitario().getDouble(), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
            
            if (Math.abs(oConcepto.getAttImporte().getDouble() - dSubtotalImporte) >= SLibConstants.RES_VAL_DECS) {
                throw new Exception("El monto del cálculo del importe del concepto '" + oConcepto.getAttDescripcion().getString() + "' es incorrecto.");
            }
            
            dSubtotalConceptos = SLibUtils.round((dSubtotalConceptos + dSubtotalImporte), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
        }
        
        // validate taxes charged:
        
        if (comprobante.getEltImpuestos().getEltOpcImpuestosTrasladados() != null) {
            for (int i = 0; i < comprobante.getEltImpuestos().getEltOpcImpuestosTrasladados().getEltHijosImpuestoTrasladado().size(); i++) {
                oTraslado = comprobante.getEltImpuestos().getEltOpcImpuestosTrasladados().getEltHijosImpuestoTrasladado().get(i);

                dTotalImptoTrasladados = SLibUtils.round((dTotalImptoTrasladados + oTraslado.getAttImporte().getDouble()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
            }

            if (Math.abs(comprobante.getEltImpuestos().getAttTotalImpuestosTrasladados().getDouble() - dTotalImptoTrasladados) >= SLibConstants.RES_VAL_DECS) {
                throw new Exception("La suma de los impuestos trasladados es incorrecta.");
            }
        }
        
        // validate taxes retained:
        
        if (comprobante.getEltImpuestos().getEltOpcImpuestosRetenidos() != null) {
            for (int i = 0; i < comprobante.getEltImpuestos().getEltOpcImpuestosRetenidos().getEltHijosImpuestoRetenido().size(); i++) {
                oRetencion = comprobante.getEltImpuestos().getEltOpcImpuestosRetenidos().getEltHijosImpuestoRetenido().get(i);

                dTotalImptoRetenidos = SLibUtils.round((dTotalImptoRetenidos + oRetencion.getAttImporte().getDouble()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
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
        
        dTotal = SLibUtils.round((dSubtotalConceptos + dTotalImptoTrasladados - dTotalImptoRetenidos - comprobante.getAttDescuento().getDouble()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
        
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
            
            dSubtotalImporte = SLibUtils.round(oConcepto.getAttCantidad().getDouble() * oConcepto.getAttValorUnitario().getDouble(), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
            
            if (Math.abs(oConcepto.getAttImporte().getDouble() - dSubtotalImporte) >= SLibConstants.RES_VAL_DECS) {
                throw new Exception("El monto del cálculo del importe del concepto '" + oConcepto.getAttDescripcion().getString() + "' es incorrecto.");
            }
            
            dSubtotalConceptos = SLibUtils.round((dSubtotalConceptos + dSubtotalImporte), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
        }
        
        if (comprobante.getEltOpcImpuestos() != null) {
            // validate taxes charged:
            if (comprobante.getEltOpcImpuestos().getEltOpcImpuestosTraslados() != null) {
                for (int i = 0; i < comprobante.getEltOpcImpuestos().getEltOpcImpuestosTraslados().getEltImpuestoTrasladados().size(); i++) {
                    oTraslado = comprobante.getEltOpcImpuestos().getEltOpcImpuestosTraslados().getEltImpuestoTrasladados().get(i);

                    dTotalImptoTrasladados = SLibUtils.round((dTotalImptoTrasladados + oTraslado.getAttImporte().getDouble()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
                }

                if (Math.abs(comprobante.getEltOpcImpuestos().getAttTotalImpuestosTraslados().getDouble() - dTotalImptoTrasladados) >= SLibConstants.RES_VAL_DECS) {
                    throw new Exception("La suma de los impuestos trasladados es incorrecta.");
                }
            }

            // validate taxes retained:
            if (comprobante.getEltOpcImpuestos().getEltOpcImpuestosRetenciones() != null) {
                for (int i = 0; i < comprobante.getEltOpcImpuestos().getEltOpcImpuestosRetenciones().getEltImpuestoRetenciones().size(); i++) {
                    oRetencion = comprobante.getEltOpcImpuestos().getEltOpcImpuestosRetenciones().getEltImpuestoRetenciones().get(i);

                    dTotalImptoRetenidos = SLibUtils.round((dTotalImptoRetenidos + oRetencion.getAttImporte().getDouble()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
                }

                if (Math.abs(comprobante.getEltOpcImpuestos().getAttTotalImpuestosRetenidos().getDouble() - dTotalImptoRetenidos) >= SLibConstants.RES_VAL_DECS) {
                    throw new Exception("La suma de los impuestos retenidos es incorrecta.");
                }
            }
        }
        
        // validate subtotal vs. subtotal concepts:
        if (Math.abs(comprobante.getAttSubTotal().getDouble() - dSubtotalConceptos) >= SLibConstants.RES_VAL_DECS) {
            throw new Exception("La suma de importes de los conceptos es incorrecta.");
        }
        
        // validate total:
        dTotal = SLibUtils.round((dSubtotalConceptos + dTotalImptoTrasladados - dTotalImptoRetenidos - comprobante.getAttDescuento().getDouble()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
        if (Math.abs(comprobante.getAttTotal().getDouble() - dTotal) >= SLibConstants.RES_VAL_DECS) {
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
    
    public static boolean signCfdi(final SClientInterface client, final SDataCfd cfd, int subtypeCfd) throws Exception {
        boolean signed = false;
        ArrayList<SDataCfd> cfdsValidate = null;

        if (cfd.getFkCfdTypeId() == SDataConstantsSys.TRNS_TP_CFD_PAYROLL) {
            cfdsValidate = getPayrollCfds(client, subtypeCfd, new int[] { subtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? cfd.getFkPayrollPayrollId_n() : cfd.getFkPayrollReceiptPayrollId_n()});
        }

        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else {
            if (existsCfdiEmitInconsist(client, cfdsValidate)) {
                signed = signCfdi(client, cfd, subtypeCfd, true, true);
            }
        }
        
        return signed;
    }

    public static boolean signCfdi(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int subtypeCfd) throws Exception {
        boolean signed = false;
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
            client.showMsgBoxInformation("No existen documentos para timbrar.");
        }
        else {
            if (client.showMsgBoxConfirm("¿Está seguro que desea timbrar " + cfdsAux.size() + " documentos?") == JOptionPane.YES_OPTION) {
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

                    if (existsCfdiEmitInconsist(client, cfdsValidate)) {
                        if (signed) {
                            SDialogResult dialogResult = new SDialogResult((SClient) client, "Resultados de timbrado", SCfdConsts.PROC_REQ_STAMP);
                            dialogResult.setFormParams(client, cfdsAux, null, stampsAvailable, null, signNeeded, subtypeCfd, SModSysConsts.TRNU_TP_DPS_ANN_NA);
                            dialogResult.setVisible(true);
                        }
                    }
                }
            }
        }

        return signed;
    }
    
    public static boolean signAndSendCfdi(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int subtypeCfd) throws Exception {
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
            client.showMsgBoxInformation("No existen documentos para timbrar y enviar.");
        }
        else {
            if (client.showMsgBoxConfirm("¿Está seguro que desea timbrar y enviar " + cfdsAux.size() + " documentos?") == JOptionPane.YES_OPTION) {
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

                    if (existsCfdiEmitInconsist(client, cfdsValidate)) {
                        if (signedSent) {
                            SDialogResult dialogResult = new SDialogResult((SClient) client, "Resultados de timbrado y enviado", SCfdConsts.PROC_REQ_STAMP_AND_SND);
                            dialogResult.setFormParams(client, cfdsAux, null, stampsAvailable, null, signNeeded, subtypeCfd, SModSysConsts.TRNU_TP_DPS_ANN_NA);
                            dialogResult.setVisible(true);
                        }
                    }
                }
            }
        }

        return signedSent;
    }

    public static boolean cancelCfdi(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int subtypeCfd, final Date cancellationDate, boolean validateStamp, int tpDpsAnn) throws Exception {
        boolean cancel = false;
        int stampsAvailable = 0;
        ArrayList<SDataCfd> cfdsAux = null;
        SDialogResult dialogResult = null;
        boolean needSign = false;

        cfdsAux = new ArrayList<SDataCfd>();

        for(SDataCfd cfd : cfds) {
            if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                cfdsAux.add(cfd);
            }
        }

        if (cfdsAux.isEmpty()) {
            client.showMsgBoxInformation("No existen documentos para anular.");
        }
        else {
            if (client.showMsgBoxConfirm("¿Está seguro que desea anular " + cfdsAux.size() + " documentos?") == JOptionPane.YES_OPTION) {
                cancel = true;
                stampsAvailable = getStampsAvailable(client, cfdsAux.get(0).getFkCfdTypeId(), cfdsAux.get(0).getTimestamp(), 0);
                needSign = isNeedStamps(client, cfdsAux.get(0), SDbConsts.ACTION_SAVE, 0);

                if (needSign && stampsAvailable == 0) {
                    client.showMsgBoxWarning("No existen timbres disponibles.");
                }
                else {
                    if (needSign && cfdsAux.size() > stampsAvailable) {
                        cancel = client.showMsgBoxConfirm("Timbres insuficientes:\n -Solo existen '" + stampsAvailable + "' timbres disponibles.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
                    }

                    if (cancel && client.showMsgBoxConfirm("La anulación de un CFDI no puede revertirse.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        dialogResult = new SDialogResult((SClient) client, "Resultados de anulación", SCfdConsts.PROC_REQ_ANNUL);

                        dialogResult.setFormParams(client, cfdsAux, null, stampsAvailable, cancellationDate, validateStamp, subtypeCfd, tpDpsAnn);
                        dialogResult.setVisible(true);
                    }
                }
            }
        }

        return cancel;
    }
    
    public static boolean cancelAndSendCfdi(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int subtypeCfd, final Date cancellationDate, boolean validateStamp, int tpDpsAnn) throws Exception {
        boolean cancel = false;
        int stampsAvailable = 0;
        ArrayList<SDataCfd> cfdsAux = null;
        SDialogResult dialogResult = null;
        boolean needSign = false;

        cfdsAux = new ArrayList<SDataCfd>();

        for(SDataCfd cfd : cfds) {
            if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                cfdsAux.add(cfd);
            }
        }

        if (cfdsAux.isEmpty()) {
            client.showMsgBoxInformation("No existen documentos para anular.");
        }
        else {
            if (client.showMsgBoxConfirm("¿Está seguro que desea anular " + cfdsAux.size() + " documentos?") == JOptionPane.YES_OPTION) {
                cancel = true;
                stampsAvailable = getStampsAvailable(client, cfdsAux.get(0).getFkCfdTypeId(), cfdsAux.get(0).getTimestamp(), 0);
                needSign = isNeedStamps(client, cfdsAux.get(0), SDbConsts.ACTION_SAVE, 0);

                if (needSign && stampsAvailable == 0) {
                    client.showMsgBoxWarning("No existen timbres disponibles.");
                }
                else {
                    if (needSign && cfdsAux.size() > stampsAvailable) {
                        cancel = client.showMsgBoxConfirm("Timbres insuficientes:\n -Solo existen '" + stampsAvailable + "' timbres disponibles.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
                    }

                    if (cancel && client.showMsgBoxConfirm("La anulación de un CFDI no puede revertirse.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        dialogResult = new SDialogResult((SClient) client, "Resultados de anulación y envío", SCfdConsts.PROC_REQ_ANNUL_AND_SND);

                        dialogResult.setFormParams(client, cfdsAux, null, stampsAvailable, cancellationDate, validateStamp, subtypeCfd, tpDpsAnn);
                        dialogResult.setVisible(true);
                    }
                }
            }
        }

        return cancel;
    }

    public static void printAcknowledgmentCancellationCfd(final SClientInterface client, final SDataCfd cfd, final int subtypeCfd) throws Exception {
        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else {
            printCfdCancelAck(client, cfd, SDataConstantsPrint.PRINT_MODE_VIEWER, subtypeCfd);
        }
    }

    public static void printAcknowledgmentCancellationCfd(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int subtypeCfd) throws Exception {
        ArrayList<SDataCfd> cfdsAux = new ArrayList<>();

        for(SDataCfd cfd : cfds) {
            if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                cfdsAux.add(cfd);
            }
        }

        if (cfdsAux.isEmpty()) {
            client.showMsgBoxInformation("No existen acuses de cancelación para imprimir.");
        }
        else {
            if (client.showMsgBoxConfirm("¿Está seguro que desea imprimir " + cfdsAux.size() + " acuses de cancelación?") == JOptionPane.YES_OPTION) {
                SDialogResult dialogResult = new SDialogResult((SClient) client, "Resultados de impresión de acuses de cancelación", SCfdConsts.PROC_PRT_ACK_ANNUL);
                dialogResult.setFormParams(client, cfdsAux, null, 0, null, false, subtypeCfd, subtypeCfd);
                dialogResult.setVisible(true);
            }
        }
    }

    public static void printAcknowledgmentCancellationPdf(final SClientInterface client, final SDataCfd cfd) throws Exception {
        File file = null;
        FileOutputStream fw = null;
        byte[] buffer = new byte[1];
        InputStream ackCancellation = null;

        client.getFileChooser().setSelectedFile(new File(cfd.getDocXmlName().replaceAll(".xml", "_CANCELA_RESP") + ".pdf"));
        if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
            file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());
            fw = new FileOutputStream(file);
            ackCancellation = getAcknowledgmentCancellationPdf(client, cfd);

            while (ackCancellation.read(buffer) > 0) {
                fw.write(buffer);
            }
            fw.close();
            client.showMsgBoxInformation(SLibConstants.MSG_INF_FILE_CREATE + file.getAbsolutePath());
        }
    }

    private static void writeXmlToDisk(final File file, final SDataCfd cfd, final int typeXml) throws Exception {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
        bw.write(typeXml == SDataConstantsSys.TRNS_ST_DPS_EMITED ? cfd.getDocXml() : cfd.getAcknowledgmentCancellationXml());
        bw.close();
    }
    
    public static void getXmlCfd(final SClientInterface client, final SDataCfd cfd) throws Exception {
        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else {
            if (canObtainXml(cfd)) {
                client.getFileChooser().setSelectedFile(new File(cfd.getDocXmlName()));
                if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
                    File file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());
                    writeXmlToDisk(file, cfd, SDataConstantsSys.TRNS_ST_DPS_EMITED);
                    client.showMsgBoxInformation(SLibConstants.MSG_INF_FILE_CREATE + file.getAbsolutePath());
                }
            }
        }
    }
    
    public static void getXmlCfds(final SClientInterface client, final ArrayList<SDataCfd> cfds) throws Exception {
        ArrayList<SDataCfd> cfdsAux = new ArrayList<>();

        for(SDataCfd cfd : cfds) {
            if (cfd != null || !cfd.getDocXml().isEmpty() || !cfd.getDocXmlName().isEmpty()) {
                cfdsAux.add(cfd);
            }
        }

        if (cfdsAux.isEmpty()) {
            client.showMsgBoxInformation("No existen documentos timbrados para obtener XML.");
        }
        else {
            client.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
                File fileAux = client.getFileChooser().getSelectedFile();
                client.getFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
                for(SDataCfd cfd : cfdsAux) {
                    if (canObtainXml(cfd)) {
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
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else {
            if (canObtainAcknowledgmentCancellation(client, cfd)) {
                client.getFileChooser().setSelectedFile(new File(cfd.getDocXmlName().replaceAll(".xml", "_CANCELA_RESP") + ".xml"));
                if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
                    File file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());
                    writeXmlToDisk(file, cfd, SDataConstantsSys.TRNS_ST_DPS_ANNULED);
                    /*
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
                    bw.write(cfd.getAcknowledgmentCancellation());
                    bw.close();
                    */
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
            client.showMsgBoxInformation("No existen documentos anulados para obtener acuse de cancelación.");
        }
        else {
            client.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
                File fileAux = client.getFileChooser().getSelectedFile();
                client.getFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
                for(SDataCfd cfd : cfdsAux) {
                    if (canObtainAcknowledgmentCancellation(client, cfd)) {
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

    public static void sendCfd(final SClientInterface client, final int typeCfd, final SDataCfd cfd, final int subtypeCfd, boolean confirmationMail, boolean catchExceptions) throws Exception {
        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else {
            sendCfd(client, typeCfd, cfd, subtypeCfd, true, confirmationMail, catchExceptions);
        }
    }

    public static void sendCfd(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int subtypeCfd) throws Exception {
        ArrayList<SDataCfd> cfdsAux = new ArrayList<>();

        for(SDataCfd cfd : cfds) {
            if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED && !isCfdSent(client, cfd.getPkCfdId())) {
                cfdsAux.add(cfd);
            }
        }

        if (cfdsAux.isEmpty()) {
            client.showMsgBoxInformation("No existen documentos para enviar por correo-e.");
        }
        else {
            if (client.showMsgBoxConfirm("¿Está seguro que desea enviar por correo-e " + cfdsAux.size() + " documentos?") == JOptionPane.YES_OPTION) {
                SDialogResult dialogResult = new SDialogResult((SClient) client, "Resultados de envío", SCfdConsts.PROC_SND_DOC);
                dialogResult.setFormParams(client, cfdsAux, null, 0, null, false, subtypeCfd, SModSysConsts.TRNU_TP_DPS_ANN_NA);
                dialogResult.setVisible(true);
            }
        }
    }

    public static boolean stampedCfdi(final SClientInterface client, final SDataCfd cfd, final int subtypeCfd) throws Exception {
        boolean signed = false;
        ArrayList<SDataCfd> cfdsValidate = null;

        if (cfd.getFkCfdTypeId() == SDataConstantsSys.TRNS_TP_CFD_PAYROLL) {
            cfdsValidate = getPayrollCfds(client, subtypeCfd,  new int[] { subtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? cfd.getFkPayrollPayrollId_n() : cfd.getFkPayrollReceiptPayrollId_n() });
        }

        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else {
            if (existsCfdiEmitInconsist(client, cfdsValidate)) {
                signed = stampedCfdiFinkok(client, cfd, subtypeCfd);
            }
        }
        return signed;
    }

    public static boolean getReceiptCancellationCfdi(final SClientInterface client, final SDataCfd cfd, final int subtypeCfd, final int tpDpsAnn) throws Exception {
       return cancelCfdiFinkok(client, cfd, subtypeCfd, tpDpsAnn);
    }

    public static ArrayList<SDataCfd> getPayrollCfds(final SClientInterface client, final int typeCfd, final int[] payrollKey) throws Exception {
        return getPayrollCfds(client, typeCfd, payrollKey, "" , SLibConsts.UNDEFINED);
    }    
    
    public static ArrayList<SDataCfd> getPayrollCfds(final SClientInterface client, final int typeCfd, final int[] payrollKey, String typeDepPayroll, int orderBy) throws Exception {
        String sql = "";
        String sqlInner = "";
        String sqlWhere = "";
        ResultSet resultSet = null;
        ArrayList<SDataCfd> cfds = null;
        SDataCfd cfd = null;
                                                                                
        cfds = new ArrayList<SDataCfd>();

        switch (typeCfd) {
            case SCfdConsts.CFDI_PAYROLL_VER_OLD:
                sqlWhere = "WHERE NOT (c.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " AND c.b_con = 0) AND c.fid_pay_pay_n = " + payrollKey[0] + (orderBy == SLibConsts.UNDEFINED ? " ORDER BY c.id_cfd " : "");
                break;
            case SCfdConsts.CFDI_PAYROLL_VER_CUR:
                sqlInner = "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pei ON "
                            + "c.fid_pay_rcp_pay_n = pei.id_pay AND c.fid_pay_rcp_emp_n = pei.id_emp AND c.fid_pay_rcp_iss_n = pei.id_iss AND pei.b_del = 0 AND pei.fk_st_rcp = "  + SModSysConsts.TRNS_ST_DPS_EMITED + " ";
                
                if (orderBy == SUtilConsts.PER_DOC) {
                    sqlInner += "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                            + "bp.id_bp = pei.id_emp "
                            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pe ON "
                            + "c.fid_pay_rcp_pay_n = pe.id_pay AND c.fid_pay_rcp_emp_n = pe.id_emp "
                            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON "
                            + "dep.id_dep = pe.fk_dep ";
                }
                else if (orderBy == SUtilConsts.PER_BPR) {
                    sqlInner += "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                                + "bp.id_bp = pei.id_emp "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pe ON "
                                + "c.fid_pay_rcp_pay_n = pe.id_pay AND c.fid_pay_rcp_emp_n = pe.id_emp "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON "
                                + "dep.id_dep = pe.fk_dep ";
                }
                else if (orderBy == SUtilConsts.PER_REF) {
                    sqlInner += "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                                + "bp.id_bp = pei.id_emp "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pe ON "
                                + "c.fid_pay_rcp_pay_n = pe.id_pay AND c.fid_pay_rcp_emp_n = pe.id_emp "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON "
                                + "dep.id_dep = pe.fk_dep ";
                }                
                sqlWhere += "WHERE " ;
                        if (typeDepPayroll.equals("")) {
                            sqlWhere += "";
                        }
                        else {
                            sqlWhere += "pe.fk_dep IN ( " +  typeDepPayroll + " ) AND ";
                             }
                        sqlWhere += "NOT (c.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " AND c.b_con = 0) AND c.fid_pay_rcp_pay_n = " + payrollKey[0] + 
                        (orderBy == SUtilConsts.PER_DOC ? " ORDER BY pei.num_ser, CAST(pei.num AS UNSIGNED INTEGER), pei.id_pay, pei.id_emp, pei.id_iss " : 
                        orderBy == SUtilConsts.PER_BPR ? " ORDER BY bp.bp, bp.id_bp " : orderBy == SUtilConsts.PER_REF ? " ORDER BY dep.code, dep.name, dep.id_dep, bp.bp, bp.id_bp " : " ORDER BY c.id_cfd ");
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        sql = "SELECT c.id_cfd " +
                "FROM trn_cfd AS c " + sqlInner + sqlWhere;

        resultSet = client.getSession().getStatement().executeQuery(sql);
        while (resultSet.next()) {
            cfd = (SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, new int[] { resultSet.getInt("id_cfd") }, SLibConstants.EXEC_MODE_SILENT);

            cfds.add(cfd);
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
                sqlWhere = "WHERE  fid_pay_pay_n = " + payrollReceiptKey[0] + " AND fid_pay_emp_n = " + payrollReceiptKey[1] + " AND fid_pay_bpr_n = " + payrollReceiptKey[2] + " ORDER BY id_cfd DESC LIMIT 1 ";
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
        while (resultSet.next()) {
            cfd = (SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, new int[] { resultSet.getInt("id_cfd") }, SLibConstants.EXEC_MODE_SILENT);
        }

        return cfd;
    }
    
    public static SDataCfd getCfd(final SClientInterface client, final int typeCfd, final int[] cfdKey) throws java.lang.Exception {
        return getCfd(client, typeCfd, SLibConsts.UNDEFINED, cfdKey);
    }
    
    public static SDataCfd getCfd(final SClientInterface client, final int typeCfd, final int subtypeCfd, final int[] cfdKey) throws java.lang.Exception {
        String sql = "";
        String sqlWhere = "";
        ResultSet resultSet = null;
        SDataCfd cfd = null;

        switch (typeCfd) {
            case SDataConstantsSys.TRNS_TP_CFD_INV:
                sqlWhere = "WHERE fid_dps_year_n = " + cfdKey[0] + " AND fid_dps_doc_n = " + cfdKey[1] + " ";
                break;
                
            case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                sqlWhere = "WHERE id_cfd = " + cfdKey[0] + " ";
                break;
                
            case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                switch (subtypeCfd) {
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

        sql = "SELECT id_cfd " +
                "FROM trn_cfd " + sqlWhere;

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            cfd = (SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, new int[] { resultSet.getInt("id_cfd") }, SLibConstants.EXEC_MODE_SILENT);
        }

        return cfd;
    }
    
    public static ArrayList<SDataCfd> getCfds(final SClientInterface client, final int typeCfd, final int subtypeCfd, ArrayList<int[]> keysDps) throws java.lang.Exception {
        ArrayList<SDataCfd> cfds ;
        
        cfds = new ArrayList<>();
        if (!keysDps.isEmpty()){
            for (int x = 0; x < keysDps.size(); x++) {
                cfds.add(getCfd(client, typeCfd, subtypeCfd, keysDps.get(x)));
            }
        }
        return cfds;
    }

    public static InputStream getAcknowledgmentCancellationPdf(final SClientInterface client, final SDataCfd cfd) {
        String sql = "";
        ResultSet resultSet = null;
        InputStream ackCancellation = null;

        try {
            sql = "SELECT ack_can_pdf_n FROM trn_cfd WHERE id_cfd = " + cfd.getPkCfdId() + " AND ack_can_pdf_n IS NOT NULL ";
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
}

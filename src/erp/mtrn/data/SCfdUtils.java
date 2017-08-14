/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import cancel.CancelSOAP;
import cancel.CancelaCFDResult;
import cancel.FolioArray;
import cancel.ReceiptResult;
import cancel.StringArray;
import cancel.UUIDS;
import cfd.DAttributeOptionImpuestoTraslado;
import cfd.DCfdConsts;
import cfd.DCfdUtils;
import cfd.DElement;
import cfd.ver2.DAttributeOptionFormaDePago;
import cfd.ver32.DCfdVer3Consts;
import cfd.ver32.DElementTimbreFiscalDigital;
import cfd.ver33.DElementCfdiRelacionados;
import cfd.ver33.DElementImpuestos;
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
import stamp.AcuseRecepcionCFDI;
import stamp.Application;
import stamp.Incidencia;
import stamp.IncidenciaArray;
import stamp.StampSOAP;

/**
 *
 * @author Juan Barajas, Sergio Flores, Edwin Carmona, Alfredo Pérez, Claudio Peña
 */
public abstract class SCfdUtils implements Serializable {

    protected static ArrayList<SCfdPacket> maCfdPackets;
    protected static int mnLogSignId;
    
    private static final String TXT_SEND = "Enviar CFDI";
    private static final String TXT_SIGN_SEND = "Timbrar y enviar CFDI";
    public static final String TXT_SEND_DPS = "Enviar documento";
    
    /*
     * Private static methods:
     */

    private static SDataCfdPacType getPacConfiguration(final SClientInterface client, final int cfdTypeId) {
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
        cfd.ver32.DElementComprobante comprobante = null;
        String certSAT = "";
        String msg = "";
        boolean can = true;

        if (cfd.isStamped()) {
            comprobante = DCfdUtils.getCfdi32(cfd.getDocXml());

            cfdPacType = getPacConfiguration(client, cfd.getFkCfdTypeId());

            for (DElement element : comprobante.getEltOpcComplemento().getElements()) {
                if (element.getName().compareTo("tfd:TimbreFiscalDigital") == 0) {
                    certSAT = ((DElementTimbreFiscalDigital) element).getAttNoCertificadoSAT().getString();
                    break;
                }
            }

            if (cfdPacType != null) {
                pac = (SDataPac) SDataUtilities.readRegistry(client, SDataConstants.TRN_PAC, new int[] { pacId == 0 ? cfdPacType.getFkPacId() : pacId }, SLibConstants.EXEC_MODE_SILENT);

                if (!pac.getIsAnnulmentEnabled()) {
                    msg = "La cancelación del CFDI ante el SAT mediante el PAC '" + pac.getPac() + "' no está habilitada en el sistema,\n pero puede cancelarlo manualmente en el portal del SAT.";
                }
                else if (pacId == 0 && cfdPacType.getIsAnnulmentCertNumberEnabled() && !cfdPacType.getPacCertNumber().isEmpty() && certSAT.compareTo(cfdPacType.getPacCertNumber()) != 0) {
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

    private static boolean canCfdiCancel(final SClientInterface client, final SDataCfd cfd, final boolean isValidate) throws Exception {
        SDataCfd cfdAux = cfd;

        cfdAux.setAuxIsSign(true);
        cfdAux.setAuxIsValidate(isValidate);
        SServerRequest request = new SServerRequest(SServerConstants.REQ_DB_CAN_ANNUL, cfdAux);
        SServerResponse response = client.getSessionXXX().request(request);

        if (response.getResultType() != SLibConstants.DB_CAN_ANNUL_YES) {
            throw new Exception(response.getMessage());
        }

        return true;
    }

    private static boolean canCfdiSign(final SClientInterface client, final SDataCfd cfd, final boolean isValidate) throws Exception {
        SDataCfd cfdAux = cfd;
        boolean can = true;

        cfdAux.setAuxIsSign(true);
        cfdAux.setAuxIsValidate(isValidate);
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
            else if (!isValidate) {
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

    private static boolean canObtainXml(final SClientInterface client, final SDataCfd cfd) throws Exception {
        if ((cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) && !cfd.isStamped()) {
            throw new Exception("El documento no ha sido emitido.");
        }
        else if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_NEW) {
            if (cfd.getIsProcessingWebService()) {
                throw new Exception(SCfdConsts.ERR_MSG_PROCESSING_WEB_SERVICE);
            }
            else if (cfd.getIsProcessingStorageXml()) {
                throw new Exception(SCfdConsts.ERR_MSG_PROCESSING_XML_STORAGE);
            }
            else if (cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFD) {
                throw new Exception("El documento no ha sido emitido.");
            }
            else if ((cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33)) {
                throw new Exception("El documento no ha sido timbrado.");
            }
            else if (cfd.getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFD || cfd.getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFDI_32 || cfd.getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFDI_33) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }

        return true;
    }

    private static boolean canObtainAcknowledgmentCancellation(final SClientInterface client, final SDataCfd cfd) throws Exception {
        if (cfd.getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
            if (cfd.getIsProcessingWebService()) {
                throw new Exception(SCfdConsts.ERR_MSG_PROCESSING_WEB_SERVICE);
            }
            else if (cfd.getIsProcessingStorageXml()) {
                throw new Exception(SCfdConsts.ERR_MSG_PROCESSING_XML_STORAGE);
            }
            else if (cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFD) {
                throw new Exception("El documento es un CFD y no tiene acuse de cancelación.");
            }
            else if ((cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33)) {
                throw new Exception("El documento no ha sido cancelado.");
            }
            else if (cfd.getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFD || cfd.getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFDI_32 || cfd.getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFDI_33) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        else if (cfd.getAcknowledgmentCancellation().isEmpty() && getAcknowledgmentCancellationPdf(client, cfd) == null) {
            if (cfd.getIsProcessingWebService()) {
                throw new Exception(SCfdConsts.ERR_MSG_PROCESSING_WEB_SERVICE);
            }
            else if (cfd.getIsProcessingStorageXml()) {
                throw new Exception(SCfdConsts.ERR_MSG_PROCESSING_XML_STORAGE);
            }
            else if (cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFD) {
                throw new Exception("El documento es un CFD y no tiene acuse de cancelación.");
            }
            else if (cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) {
                throw new Exception("El documento no tiene acuse de cancelación.");
            }
            else if (cfd.getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFD || cfd.getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFDI_32 || cfd.getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFDI_33) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }

        return true;
    }

    private static boolean canPrint(final SDataCfd cfd, final boolean isSaving) throws Exception {
        if (!isSaving) {
            if (cfd.getIsProcessingWebService()) {
                throw new Exception(SCfdConsts.ERR_MSG_PROCESSING_WEB_SERVICE);
            }
        }

        return true;
    }

    private static boolean canSend(final SDataCfd cfd) throws Exception {
        if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_NEW) {
            throw new Exception("El documento no ha sido timbrado.");
        }
        else if (cfd.getIsProcessingWebService()) {
            throw new Exception(SCfdConsts.ERR_MSG_PROCESSING_WEB_SERVICE);
        }
        else if (cfd.getIsProcessingStorageXml()) {
            throw new Exception(SCfdConsts.ERR_MSG_PROCESSING_XML_STORAGE);
        }
        else if ((cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) && !cfd.isStamped()) {
            throw new Exception("El documento no ha sido timbrado.");
        }
        return true;
    }
    
    
    private static boolean existsCfdiEmitInconsist(final SClientInterface client, final ArrayList<SDataCfd> cfds) throws Exception {
        if (cfds != null) {
            for (SDataCfd cfd : cfds) {
                if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED && !cfd.getIsConsistent()) {
                    throw new Exception("Existen CFDI emitidos e inconsistentes, para la nómina '" + cfd.getFkPayrollPayrollId_n() + "'.");
                }
            }
        }

        return true;
    }

    private static void createSignCancelLog(final SClientInterface client, final String msg, final int code_act, final int code_step, final SDataCfd cfd, final int pacId) throws Exception {
        SDataCfdSignLog cfdSignLog = null;
        SDataCfdSignLogMsg cfdSignLogMsg = null;
        boolean sys = false;

        cfdSignLog = new SDataCfdSignLog();

        cfdSignLog.setPkLogId(mnLogSignId);

        if (mnLogSignId == 0) {
            cfdSignLog.setDate(client.getSession().getCurrentDate());
            cfdSignLog.setIsSystem(pacId == SLibConsts.UNDEFINED);
            cfdSignLog.setIsDeleted(false);
            cfdSignLog.setFkCfdId(cfd.getPkCfdId());
            cfdSignLog.setFkPacId_n(pacId);
            cfdSignLog.setFkUserId(client.getSession().getUser().getPkUserId());
        }
        cfdSignLog.setCodeAction(code_act);
        cfdSignLog.setCodeStep(code_step);

        if (!msg.isEmpty()) {
            cfdSignLogMsg = new SDataCfdSignLogMsg();

            cfdSignLogMsg.setMessage(msg);

            cfdSignLog.setDbmsDataCfdSignLogMsg(cfdSignLogMsg);
        }

        if (cfdSignLog.save(client.getSession().getDatabase().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
        }
        else {
            mnLogSignId = cfdSignLog.getPkLogId();
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
        boolean isConsistent = false;
        boolean isFound = false;
        boolean add = true;
        int cfdId = SLibConsts.UNDEFINED;
        String sql = "";
        ResultSet resultSet = null;
        SCfdPacket packet = null;
        cfd.ver32.DElementComprobante comprobanteCfdi32 = null;
        cfd.ver33.DElementComprobante comprobanteCfdi33 = null;

        // Validate payroll receipts consistent:

        if (payroll == null) {
            throw new Exception("Payroll is null.");
        }
        else {
            maCfdPackets = new ArrayList<SCfdPacket>();

            for (SHrsFormerPayrollReceipt receipt: payroll.getChildPayrollReceipts()) {
                isFound = false;
                isConsistent = false;
                add = true;
                cfdId = 0;

                if (payrollXml != null) {
                    for (SHrsFormerPayrollReceipt receiptXml: payrollXml.getChildPayrollReceipts()) {
                        if (receipt.getAuxEmpleadoId() == receiptXml.getAuxEmpleadoId() && receipt.getPkEmpleadoId() == receiptXml.getPkEmpleadoId()) {
                            isFound = true;
                            isConsistent = validateReceiptsConcepts(receipt, receiptXml) && receipt.getBanco() == receiptXml.getBanco() &&
                                    SLibTimeUtils.convertToDateOnly(receipt.getPayroll().getFecha()).compareTo(SLibTimeUtils.convertToDateOnly(receiptXml.getPayroll().getFecha())) == 0 &&
                                    receipt.getClabe().compareTo(receiptXml.getClabe()) == 0 && receipt.getTipoContrato().compareTo(receiptXml.getTipoContrato()) == 0 &&
                                    receipt.getFechaPago().compareTo(receiptXml.getFechaPago()) == 0 && receipt.getFechaFinalPago().compareTo(receiptXml.getFechaFinalPago()) == 0 &&
                                    receipt.getFechaInicialPago().compareTo(receiptXml.getFechaInicialPago()) == 0 && receipt.getFechaInicioRelLaboral().compareTo(receiptXml.getFechaInicioRelLaboral()) == 0 &&
                                    receipt.getNumDiasPagados() == receiptXml.getNumDiasPagados() && receipt.getCurp().compareTo(receiptXml.getCurp()) == 0 &&
                                    receipt.getTipoRegimen() == receiptXml.getTipoRegimen() && receipt.getNumSeguridadSocial().compareTo(receiptXml.getNumSeguridadSocial()) == 0 &&
                                    receipt.getNumEmpleado().compareTo(receiptXml.getNumEmpleado()) == 0 && receipt.getRegistroPatronal().compareTo(receiptXml.getRegistroPatronal()) == 0 &&
                                    receipt.getPuesto().compareTo(receiptXml.getPuesto()) == 0 && receipt.getRiesgoPuesto() == receiptXml.getRiesgoPuesto() &&
                                    receipt.getDepartamento().compareTo(receiptXml.getDepartamento()) == 0 && receipt.getPeriodicidadPago().compareTo(receiptXml.getPeriodicidadPago()) == 0 &&
                                    receipt.getSalarioBaseCotApor() == receiptXml.getSalarioBaseCotApor() && receipt.getSalarioDiarioIntegrado() == receiptXml.getSalarioDiarioIntegrado() &&
                                    receipt.getAntiguedad() == receiptXml.getAntiguedad() && receipt.getTotalDeducciones() == receiptXml.getTotalDeducciones() &&
                                    receipt.getTotalNeto() == receiptXml.getTotalNeto() && receipt.getTotalPercepciones() == receiptXml.getTotalPercepciones() &&
                                    receipt.getTotalRetenciones() == receiptXml.getTotalRetenciones() && receipt.getTipoJornada().compareTo(receiptXml.getTipoJornada()) == 0 &&
                                    (SLibTimeUtils.convertToDateOnly(receipt.getPayroll().getFecha()).compareTo(SLibTimeUtils.convertToDateOnly(SLibTimeUtils.createDate(2016, 7, 15))) > 0 ?
                                    SHrsFormerUtils.getPaymentMethodCode(client, receipt.getMetodoPago()).compareTo(receiptXml.getMetodoPagoAux()) == 0 : 
                                    SHrsFormerUtils.getPaymentMethodName(client, receipt.getMetodoPago()).compareTo(receiptXml.getMetodoPagoAux()) == 0);
                        }

                        if (isFound) {
                            sql = "SELECT id_cfd, fid_st_xml " +
                                    "FROM trn_cfd WHERE fid_pay_pay_n = " + payroll.getPkNominaId() + " AND fid_pay_emp_n = " + receipt.getAuxEmpleadoId() +
                                    " AND fid_pay_bpr_n = " + receipt.getPkEmpleadoId() + " ORDER BY id_cfd ";

                            resultSet = client.getSession().getStatement().executeQuery(sql);
                            while (resultSet.next()) {
                                if (resultSet.getInt("fid_st_xml") != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                                    if (resultSet.getInt("fid_st_xml") == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                                        add = !isRegenerateOnlyNonStampedCfdi;
                                        cfdId = isConsistent ? resultSet.getInt("id_cfd") : SLibConsts.UNDEFINED;
                                    }
                                    else {
                                        if (cfdId == SLibConsts.UNDEFINED) {
                                            cfdId = resultSet.getInt("id_cfd");
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
                    
                    receipt.setPayroll(payroll);
                    receipt.setFechaEdicion(client.getSession().getCurrentDate());
                    receipt.setMoneda(client.getSession().getSessionCustom().getLocalCurrencyCode());
                    
                    // Generate CFDI:

                    packet = new SCfdPacket();
                    packet.setDpsYearId(SLibConsts.UNDEFINED);
                    packet.setDpsDocId(SLibConsts.UNDEFINED);
                    packet.setCfdId(cfdId);
                    packet.setIsConsistent(!isFound || cfdId == SLibConsts.UNDEFINED ? true: isConsistent);
                    
                    int xmlType = ((SSessionCustom) client.getSession().getSessionCustom()).getCfdTypeXmlTypes().get(SDataConstantsSys.TRNS_TP_CFD_PAYROLL);
                    float cfdVersion = SLibConsts.UNDEFINED;
                    switch (xmlType) {
                        case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                            comprobanteCfdi32 = (cfd.ver32.DElementComprobante) createCfdi32RootElement(client, receipt);
                            cfdVersion = comprobanteCfdi32.getVersion();
                            
                            packet.setStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi32));
                            packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW); // after stamping changes to emitted
                            break;
                        case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                            comprobanteCfdi33 = (cfd.ver33.DElementComprobante) createCfdi33RootElement(client, receipt);
                            cfdVersion = comprobanteCfdi33.getVersion();
                            
                            packet.setStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi33));
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
                    packet.setPayrollEmployeeId(receipt.getAuxEmpleadoId());
                    packet.setPayrollBizPartnerId(receipt.getPkEmpleadoId());
                    packet.setRfcEmisor("");
                    packet.setRfcReceptor("");
                    packet.setTotalCy(0);
                    packet.setAcknowledgmentCancellation("");
                    packet.setUuid("");
                    packet.setConsumeStamp(false);
                    packet.setGenerateQrCode(false);
                    
                    packet.setSignature(client.getCfdSignature(cfdVersion).sign(packet.getStringSigned(), SLibTimeUtilities.digestYear(payroll.getFecha())[0]));
                    packet.setCertNumber(client.getCfdSignature(cfdVersion).getCertNumber());
                    packet.setCertBase64(client.getCfdSignature(cfdVersion).getCertBase64());
                    
                    switch (xmlType) {
                        case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                            comprobanteCfdi32.getAttSello().setString(packet.getSignature());
                            packet.setCfdRootElement(comprobanteCfdi32);
                            break;
                        case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                            comprobanteCfdi33.getAttSello().setString(packet.getSignature());
                            packet.setCfdRootElement(comprobanteCfdi33);
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
                    }

                    maCfdPackets.add(packet);
                }
            }
        }

        if (!isRegenerateOnlyNonStampedCfdi && payroll.getChildPayrollReceipts().size() != maCfdPackets.size()) {
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

    private static boolean managementCfdi(final SClientInterface client, final SDataCfd cfd, final int status, final Date date, final boolean isSingle, final boolean isValidate, final int pacId, final int subtypeCfd) throws Exception {
        return managementCfdi(client, cfd, status, date, isSingle, isValidate, pacId, subtypeCfd, "", false);
    }

    private static boolean managementCfdi(final SClientInterface client, final SDataCfd cfd, final int status, final Date date, final boolean isSingle, final boolean isValidate, final int pacId, final int subtypeCfd, final String xmlSigned, final boolean isUpdateAckPdf) throws Exception {
        SCfdiSignature cfdiSign = null;
        SCfdPacket packet = null;
        SDataDps dps = null;
        SDataPayrollReceiptIssue receiptIssue = null;
        SDataCfd cfdAuxPrint = null;
        SDataCfdPacType cfdPacType = null;
        SDataPac pac = null;
        SSrvLock lock = null;
        String acknowledgmentCancel = "";
        String xmlStamping = "";
        String warningMessage = "";
        boolean consumeStamp = true;
        boolean next = true;

        try {
            switch (cfd.getFkCfdTypeId()) {
                case SDataConstantsSys.TRNS_TP_CFD_INV:
                    dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, new int[] { cfd.getFkDpsYearId_n(), cfd.getFkDpsDocId_n() }, SLibConstants.EXEC_MODE_SILENT);
                    dps.setAuxIsValidate(isValidate);

                    lock = SSrvUtils.gainLock(client.getSession(), client.getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.TRN_DPS, dps.getPrimaryKey(), 1000 * 60); // 1 minute timeout
                    break;
                case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                    if (subtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_CUR) {
                        receiptIssue = new SDataPayrollReceiptIssue();

                        if (receiptIssue.read(new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n(), cfd.getFkPayrollReceiptIssueId_n() }, client.getSession().getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }

                        lock = SSrvUtils.gainLock(client.getSession(), client.getSessionXXX().getCompany().getPkCompanyId(), SModConsts.HRS_PAY_RCP_ISS, receiptIssue.getPrimaryKey(), 1000 * 60); // 1 minute timeout
                    }
                    break;
                default:
            }
            
            if (status == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                if (!isUpdateAckPdf) {
                    acknowledgmentCancel = cancel(client, cfd, date, isValidate, pacId);

                    if (acknowledgmentCancel.isEmpty()) {
                        next = pacId == 0;
                    }
                }

                if (!isUpdateAckPdf) {
                    consumeStamp = !acknowledgmentCancel.isEmpty();
                }
                else {
                    consumeStamp = cfd.getIsProcessingWebService();
                }

                cfdiSign = obtainCfdiSignature(cfd.getDocXml());
            }
            else {
                if (xmlSigned.length() == 0) {
                    xmlStamping = sign(client, cfd, isValidate);
                }
                else {
                    xmlStamping = xmlSigned;
                }

                cfdiSign = obtainCfdiSignature(xmlStamping);
            }

            if (next) {
                next = false;

                cfdPacType = getPacConfiguration(client, cfd.getFkCfdTypeId());

                if (cfdPacType == null) {
                    throw new Exception("No existe ninguna configuración para el tipo de CFD.");
                }
                else {

                    if (cfdiSign == null || cfdiSign.getUuid().isEmpty()) {
                        throw new Exception("¡El documento no ha sido timbrado!");
                    }
                    else {
                        pac = (SDataPac) SDataUtilities.readRegistry(client, SDataConstants.TRN_PAC, new int[] { pacId == 0 ? cfdPacType.getFkPacId() : pacId }, SLibConstants.EXEC_MODE_SILENT);

                        packet = new SCfdPacket();
                        packet.setDpsYearId(cfd.getFkDpsYearId_n());
                        packet.setDpsDocId(cfd.getFkDpsDocId_n());
                        packet.setCfdId(cfd.getPkCfdId());
                        packet.setPacId(pac.getPkPacId());
                        packet.setIsConsistent(true);
                        packet.setDps(cfd.getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED ? dps : null);
                        packet.setPayrollReceiptIssue(cfd.getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED ? receiptIssue : null);
                        packet.setLogSignId(mnLogSignId);

                        packet.setStringSigned(cfd.getStringSigned());
                        packet.setFkCfdTypeId(cfd.getFkCfdTypeId());
                        packet.setFkXmlTypeId(cfd.getFkXmlTypeId());
                        packet.setFkXmlStatusId(status);
                        packet.setFkXmlDeliveryTypeId(cfd.getFkXmlDeliveryTypeId());
                        packet.setFkXmlDeliveryStatusId(cfd.getFkXmlDeliveryStatusId());
                        packet.setFkUserDeliveryId(client.getSession().getUser().getPkUserId());
                        packet.setPayrollPayrollId(cfd.getFkPayrollPayrollId_n());
                        packet.setPayrollEmployeeId(cfd.getFkPayrollEmployeeId_n());
                        packet.setPayrollBizPartnerId(cfd.getFkPayrollBizPartnerId_n());
                        packet.setPayrollReceiptPayrollId(cfd.getFkPayrollReceiptPayrollId_n());
                        packet.setPayrollReceiptEmployeeId(cfd.getFkPayrollReceiptEmployeeId_n());
                        packet.setPayrollReceiptIssueId(cfd.getFkPayrollReceiptIssueId_n());
                        packet.setRfcEmisor(cfdiSign.getRfcEmisor());
                        packet.setRfcReceptor(cfdiSign.getRfcReceptor());
                        packet.setTotalCy(cfdiSign.getTotalCy());
                        packet.setAcknowledgmentCancellation(acknowledgmentCancel.isEmpty() ? cfd.getAcknowledgmentCancellation() : acknowledgmentCancel);
                        packet.setUuid(cfdiSign.getUuid());
                        packet.setQuantityStamp(status == SDataConstantsSys.TRNS_ST_DPS_EMITED ? 1 : (pac.getIsPrepayment() && pac.getIsChargedAnnulment() ? 1 : 0) );
                        packet.setConsumeStamp(consumeStamp);
                        packet.setGenerateQrCode(status != SDataConstantsSys.TRNS_ST_DPS_ANNULED);
                        if (status == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                            packet.setXml(cfd.getDocXml());
                        }
                        else {
                            packet.setXml(composeCfdWithTfdTimbreFiscalDigital(client, cfd, xmlStamping));
                        }
                        packet.setXmlName(cfd.getDocXmlName());
                        packet.setXmlDate(cfd.getTimestamp());
                        packet.setSignature(cfd.getSignature());
                        packet.setCertNumber(cfd.getCertNumber());

                        // Sign & Cancel Log step #5
                        createSignCancelLog(client, "", status == SDataConstantsSys.TRNS_ST_DPS_EMITED ? !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN :
                                !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_SAVE, cfd, pacId == 0 ? pacId : pac.getPkPacId());

                        saveCfd(client, packet);

                        next = true;

                        if (isSingle) {
                            warningMessage = verifyCertificateExpiration(client);
                            
                            if (status == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                                client.showMsgBoxInformation("El documento ha sido anulado correctamente." + (!isNeedStamps(client, cfd, SDbConsts.ACTION_ANNUL, pacId == 0 ? pacId : pac.getPkPacId()) || !consumeStamp ? "" :
                                                "\nTimbres disponibles: " + getStampsAvailable(client, cfd.getFkCfdTypeId(), cfd.getTimestamp(), pacId == 0 ? pacId : pac.getPkPacId()) + "." + (warningMessage.isEmpty() ? "" : "\n" + warningMessage)));
                            }
                            else {
                                client.showMsgBoxInformation("El documento ha sido timbrado correctamente. " + (!isNeedStamps(client, cfd, SDbConsts.ACTION_SAVE, pacId == 0 ? pacId : pac.getPkPacId()) ? "" :
                                                "\nTimbres disponibles: " + getStampsAvailable(client, cfd.getFkCfdTypeId(), cfd.getTimestamp(), pacId == 0 ? pacId : pac.getPkPacId()) + "." + (warningMessage.isEmpty() ? "" : "\n" + warningMessage)));
                            }
                        }

                        cfdAuxPrint = (SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, cfd.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                        printCfd(client, cfdAuxPrint.getFkCfdTypeId(), cfdAuxPrint, SDataConstantsPrint.PRINT_MODE_PDF, subtypeCfd, true);

                        // Set flag PDF as correct:

                        cfdAuxPrint.saveField(client.getSession().getDatabase().getConnection(), new int[] { cfdAuxPrint.getPkCfdId() }, SDataCfd.FIELD_ACC_PDF_STO, false);
                        cfdAuxPrint.saveField(client.getSession().getDatabase().getConnection(), new int[] { cfdAuxPrint.getPkCfdId() }, SDataCfd.FIELD_ACC_USR, client.getSession().getUser().getPkUserId());
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
    private static String sign(final SClientInterface client, final SDataCfd cfd, boolean isValidate) throws TransformerConfigurationException, TransformerException, Exception {
        SDataCfdPacType cfdPacType = null;
        SDataPac pac = null;
        String sCfdi = "";
        String sCfdiUser = "";
        String sCfdiUserPassword = "";
        String xml = "";

        cfdPacType = getPacConfiguration(client, cfd.getFkCfdTypeId());

        if (isValidate) {
            pac = getPacForValidate(client, cfd);
        }
        else {
            if (cfdPacType != null) {
                pac = (SDataPac) SDataUtilities.readRegistry(client, SDataConstants.TRN_PAC, new int[] { cfdPacType.getFkPacId() }, SLibConstants.EXEC_MODE_SILENT);
            }
        }

        if (pac == null) {
            throw new Exception("Error al leer el catálogo de PAC's.\n -No existe ningún PAC registrado para el timbrado de CFDI.");
        }
        else {
            client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            sCfdiUser = pac.getUser();
            sCfdiUserPassword = pac.getUserPassword();

            /* Code used in fiscal stamp testing: (DO NOT DELETE! KEEPED JUST FOR REFERENCE!)
            XmlProcess xmlProcess = null;
            oInputFile = new BufferedInputStream(new FileInputStream(sXmlBaseDir + cfd.getDocXmlName()));
            xmlProcess = new XmlProcess(oInputFile);
            sCfdi = xmlProcess.generaTextoXML();
            */

            sCfdi = removeNode(cfd.getDocXml(), "cfdi:Addenda");    // production code for fiscal stamp

            if (client.getSessionXXX().getParamsCompany().getIsCfdiProduction()) {
                // CFDI signing production environment:
                
                switch (pac.getPkPacId()) {
                    case SModSysConsts.TRN_PAC_FCG:
                        forsedi.timbrado.WSForcogsaService fcgService = null;
                        forsedi.timbrado.WSForcogsa fcgPort = null;
                        forsedi.timbrado.WsAutenticarResponse autenticarResponse = null;
                        forsedi.timbrado.WsTimbradoResponse timbradoResponse = null;

                        fcgService = new forsedi.timbrado.WSForcogsaService();
                        fcgPort = fcgService.getWSForcogsaPort();

                        // Web Service Autentication:

                        // Sign & Cancel Log step #1
                        createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_AUTHENTICATION_PAC, cfd, pac.getPkPacId());

                        autenticarResponse = fcgPort.autenticar(sCfdiUser, sCfdiUserPassword);

                        if (autenticarResponse.getMensaje() != null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLog(client, "WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]", !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_AUTHENTICATION_PAC, cfd, pac.getPkPacId());

                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            System.err.println("WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]");
                            throw new Exception("Error al autenticarse con el PAC:\nMensaje: " + autenticarResponse.getMensaje());
                        }

                        if (autenticarResponse.getToken() == null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLog(client, "WsAutenticarResponse Token is null!", !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_AUTHENTICATION_PAC, cfd, pac.getPkPacId());

                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            System.err.println("WsAutenticarResponse Token is null!");
                            throw new Exception("Error al autenticarse con el PAC:\n¡Token nulo!");
                        }

                        // Document stamp:

                        // Sign & Cancel Log step #2
                        createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_ACTIVATE, cfd, pac.getPkPacId());

                        updateProcessCfd(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_SEND_RECEIVE, cfd, pac.getPkPacId());

                        timbradoResponse = fcgPort.timbrar(sCfdi, autenticarResponse.getToken());

                        if (timbradoResponse.getMensaje() != null) {
                            // Sign & Cancel Log step #4
                            createSignCancelLog(client, "WsTimbradoResponse Mensaje: [" + timbradoResponse.getMensaje() + "]", !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

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
                        String sMessageException = "";
                        AcuseRecepcionCFDI acuseRecepcionCFDI = null;
                        JAXBElement<IncidenciaArray> maoIncidencias = null;
                        StampSOAP service = null;
                        Application port = null;

                        service = new StampSOAP();
                        port = service.getApplication();

                        // Document stamp:

                        // Sign & Cancel Log step #1, not required!
                        // Sign & Cancel Log step #2
                        createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_ACTIVATE, cfd, pac.getPkPacId());

                        updateProcessCfd(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_SEND_RECEIVE, cfd, pac.getPkPacId());

                        if (isValidate) {
                            acuseRecepcionCFDI = port.stamped(sCfdi.getBytes("UTF-8"), sCfdiUser, sCfdiUserPassword);
                        }
                        else {
                            acuseRecepcionCFDI = port.stamp(sCfdi.getBytes("UTF-8"), sCfdiUser, sCfdiUserPassword);
                        }

                        maoIncidencias = acuseRecepcionCFDI.getIncidencias();

                        if (!maoIncidencias.getValue().getIncidencia().isEmpty()) {
                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                            if (isValidate) {
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
                            createSignCancelLog(client, sMessageException, !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

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
                        com.wscliente.WSForcogsaService fcgService = null;
                        com.wscliente.WSForcogsa fcgPort = null;
                        com.wscliente.WsAutenticarResponse autenticarResponse = null;
                        com.wscliente.WsTimbradoResponse timbradoResponse = null;

                        fcgService = new com.wscliente.WSForcogsaService();
                        fcgPort = fcgService.getWSForcogsaPort();

                        // Web Service Autentication:

                        // Sign & Cancel Log step #1
                        createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_AUTHENTICATION_PAC, cfd, pac.getPkPacId());

                        autenticarResponse = fcgPort.autenticar("pruebasWS", "pruebasWS");

                        if (autenticarResponse.getMensaje() != null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLog(client, "WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]", !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_AUTHENTICATION_PAC, cfd, pac.getPkPacId());

                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            System.err.println("WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]");
                            throw new Exception("Error al autenticarse con el PAC:\nMensaje: " + autenticarResponse.getMensaje());
                        }

                        if (autenticarResponse.getToken() == null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLog(client, "WsAutenticarResponse Token is null!", !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_AUTHENTICATION_PAC, cfd, pac.getPkPacId());

                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            System.err.println("WsAutenticarResponse Token is null!");
                            throw new Exception("Error al autenticarse con el PAC:\n¡Token nulo!");
                        }

                        // Document stamp:

                        // Sign & Cancel Log step #2
                        createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_ACTIVATE, cfd, pac.getPkPacId());

                        updateProcessCfd(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_SEND_RECEIVE, cfd, pac.getPkPacId());

                        timbradoResponse = fcgPort.timbrar(sCfdi, autenticarResponse.getToken());

                        if (timbradoResponse.getMensaje() != null) {
                            // Sign & Cancel Log step #4
                            createSignCancelLog(client, "WsTimbradoResponse Mensaje: [" + timbradoResponse.getMensaje() + "]", !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

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
                        String sMessageException = "";
                        AcuseRecepcionCFDI acuseRecepcionCFDI = null;
                        JAXBElement<IncidenciaArray> maoIncidencias = null;
                        StampSOAP service = null;
                        Application port = null;

                        service = new StampSOAP();
                        port = service.getApplication();

                        // Document stamp:

                        // Sign & Cancel Log step #1, not required!
                        // Sign & Cancel Log step #2, only when validation is not requested
                        createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_ACTIVATE, cfd, pac.getPkPacId());

                        updateProcessCfd(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_SEND_RECEIVE, cfd, pac.getPkPacId());

                        if (isValidate) {
                            acuseRecepcionCFDI = port.stamped(sCfdi.getBytes("UTF-8"), "jbarajas@swaplicado.com.mx", "WSfink_2017");
                        }
                        else {
                            acuseRecepcionCFDI = port.stamp(sCfdi.getBytes("UTF-8"), "jbarajas@swaplicado.com.mx", "WSfink_2017");
                        }

                        maoIncidencias = acuseRecepcionCFDI.getIncidencias();

                        if (!maoIncidencias.getValue().getIncidencia().isEmpty()) {
                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                            if (isValidate) {
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
                            createSignCancelLog(client, sMessageException, !isValidate ? SCfdConsts.ACTION_SIGN : SCfdConsts.ACTION_RESTORE_SIGN, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                            System.err.println("Cfdi: [" + sCfdi + "]");
                            throw new Exception("Error al timbrar el documento: " + sMessageException);
                        }

                        xml = acuseRecepcionCFDI.getXml().getValue();
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
    private static String cancel(final SClientInterface client, final SDataCfd cfd, final Date date, boolean isValidate, final int pacId) throws Exception {
        SDataCfdPacType cfdPacType = null;
        SDataPac pac = null;
        String sRfcEmisor = "";
        String sCfdiUser = "";
        String sCfdiUserPassword = "";
        SDataCertificate companyCertificate = null;
        ArrayList<String> asUuid = null;
        String xmlAcuse = "";
        boolean next = true;

        cfdPacType = getPacConfiguration(client, cfd.getFkCfdTypeId());

        if (isValidate) {
            pac = getPacForValidate(client, cfd);
        }
        else {
            if (cfdPacType != null || pacId != 0) {
                pac = (SDataPac) SDataUtilities.readRegistry(client, SDataConstants.TRN_PAC, new int[] { pacId == 0 ? cfdPacType.getFkPacId() : pacId }, SLibConstants.EXEC_MODE_SILENT);
            }
        }

        if (pac == null) {
            throw new Exception("Error al leer el catálogo de PAC's.\n -No existe ningún PAC registrado para la cancelación de CFDI.");
        }
        else {
            sRfcEmisor = client.getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId();
            sCfdiUser = pac.getUser();
            sCfdiUserPassword = pac.getUserPassword();
            companyCertificate = client.getSessionXXX().getParamsCompany().getDbmsDataCertificate_n();
            asUuid = new ArrayList<String>();

            asUuid.add(cfd.getUuid());

            client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if (client.getSessionXXX().getParamsCompany().getIsCfdiProduction()) {
                // CFDI signing production environment:
                
                switch (pac.getPkPacId()) {
                    case SModSysConsts.TRN_PAC_FCG:
                        forsedi.timbrado.WSForcogsaService fcgService = null;
                        forsedi.timbrado.WSForcogsa fcgPort = null;
                        forsedi.timbrado.WsAutenticarResponse autenticarResponse = null;
                        forsedi.timbrado.WsCancelacionResponse canceladoResponse = null;
                        forsedi.timbrado.WsFoliosResponse foliosResponse = null;

                        fcgService = new forsedi.timbrado.WSForcogsaService();
                        fcgPort = fcgService.getWSForcogsaPort();

                        // Web Service Autentication:

                        // Sign & Cancel Log step #1
                        createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_AUTHENTICATION_PAC, cfd, pac.getPkPacId());

                        autenticarResponse = fcgPort.autenticar(sCfdiUser, sCfdiUserPassword);

                        if (autenticarResponse.getMensaje() != null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLog(client, "WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_AUTHENTICATION_PAC, cfd, pac.getPkPacId());

                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                            if (pacId == 0) {
                                System.err.println("WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]");
                                throw new Exception("Error al autenticarse con el PAC:\nMensaje: " + autenticarResponse.getMensaje());
                            }
                            next = false;
                        }

                        if (autenticarResponse.getToken() == null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLog(client, "WsAutenticarResponse Token is null!", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_AUTHENTICATION_PAC, cfd, pac.getPkPacId());

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
                            createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_ACTIVATE, cfd, pac.getPkPacId());

                            updateProcessCfd(client, cfd, true);

                            // Sign & Cancel Log step #3
                            createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_SEND_RECEIVE, cfd, pac.getPkPacId());

                            canceladoResponse = fcgPort.cancelacion1(sRfcEmisor, SLibUtils.DbmsDateFormatDate.format(date), asUuid, companyCertificate.getExtraPublicKeyBytes_n(), companyCertificate.getExtraPrivateKeyBytes_n(), "", autenticarResponse.getToken());

                            if (canceladoResponse.getMensaje() != null) {
                                // Sign & Cancel Log step #4
                                createSignCancelLog(client, "WsCancelacionResponse Mensaje: [" + canceladoResponse.getMensaje() + "]", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                                updateProcessCfd(client, cfd, false);
                                client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                if (pacId == 0) {
                                    System.err.println("WsCancelacionResponse Codigo: [" + canceladoResponse.getCodEstatus() + "]");
                                    System.err.println("WsCancelacionResponse Mensaje: [" + canceladoResponse.getMensaje() + "]");
                                    System.err.println("UUID: [" + asUuid + "]\t");
                                    throw new Exception("Error al cancelar el documento:\nCódigo: " + canceladoResponse.getCodEstatus() + "\nMensaje: " + canceladoResponse.getMensaje());
                                }
                                next = false;
                            }

                            if (next) {
                                foliosResponse = canceladoResponse.getFolios();

                                if (foliosResponse.getFolio().get(0).getEstatusUUID().compareTo(SCfdConsts.UUID_ANNUL) != 0) {
                                    if (foliosResponse.getFolio().get(0).getEstatusUUID().compareTo(SCfdConsts.UUID_ANNUL_PREV) != 0) {
                                        // Sign & Cancel Log step #4
                                        createSignCancelLog(client, foliosResponse.getFolio().get(0).getMensaje(), !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

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
                        UUIDS uuids = null;
                        JAXBElement<FolioArray> maoFolios = null;
                        StringArray aUuids = null;
                        CancelaCFDResult cancelaCFDResult = null;
                        ReceiptResult receiptResult = null;
                        CancelSOAP service = null;
                        cancel.Application port = null;

                        uuids = new cancel.UUIDS();

                        aUuids = new StringArray();
                        aUuids.getString().addAll(asUuid);

                        QName uuidQName = new QName("uuids");
                        JAXBElement<StringArray> uuidValue = new JAXBElement<StringArray>(uuidQName, StringArray.class, aUuids);

                        uuids.setUuids(uuidValue);

                        service = new cancel.CancelSOAP();
                        port = service.getApplication();

                        // Document cancel:

                        // Sign & Cancel Log step #1, not required!
                        // Sign & Cancel Log step #2, only when validation is not requested
                        createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_ACTIVATE, cfd, pac.getPkPacId());

                        updateProcessCfd(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_SEND_RECEIVE, cfd, pac.getPkPacId());

                        if (isValidate) {
                            receiptResult = port.getReceipt(sCfdiUser, sCfdiUserPassword, sRfcEmisor, cfd.getUuid(), "C");
                        }
                        else {
                            cancelaCFDResult = port.cancel(uuids, sCfdiUser, sCfdiUserPassword, sRfcEmisor, companyCertificate.getExtraFnkPublicKeyBytes_n(), companyCertificate.getExtraFnkPrivateKeyBytes_n(), true);
                        }

                        if (isValidate) {
                            if (receiptResult != null) {
                                if (receiptResult.getSuccess() == null) {
                                    // Sign & Cancel Log step #4
                                    createSignCancelLog(client, receiptResult.getError().getValue(), !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

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
                                        createSignCancelLog(client, "El UUID: '" + cfd.getUuid() + "' no ha sido cancelado.", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                                        updateProcessCfd(client, cfd, false);

                                        if (pacId == 0) {
                                            throw new Exception("Error al intentar obtener acuse de cancelación de CFDI.\n" + "El UUID: '" + cfd.getUuid() + "' no ha sido cancelado.");
                                        }
                                        next = false;
                                    }
                                    else {
                                        xmlAcuse = receiptResult.getReceipt().getValue();
                                    }
                                }
                            }
                        }
                        else {
                            maoFolios = cancelaCFDResult.getFolios();

                            if (maoFolios == null) {
                                // Sign & Cancel Log step #4
                                createSignCancelLog(client, cancelaCFDResult.getCodEstatus().getValue(), !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                                updateProcessCfd(client, cfd, false);
                                client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                if (pacId == 0) {
                                    throw new Exception(cancelaCFDResult.getCodEstatus().getValue());
                                }
                                next = false;
                            }
                            else {
                                if (maoFolios.getValue().getFolio().isEmpty()) {
                                    // Sign & Cancel Log step #4
                                    createSignCancelLog(client, "Codigo: [" + cancelaCFDResult.getCodEstatus().getValue() + "] Error al intentar cancelar CFDI.", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                                    updateProcessCfd(client, cfd, false);
                                    client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                    if (pacId == 0) {
                                        throw new Exception("Codigo: [" + cancelaCFDResult.getCodEstatus().getValue() + "] Error al intentar cancelar CFDI.");
                                    }
                                    next = false;
                                }
                                else {
                                    if (maoFolios.getValue().getFolio().get(0).getEstatusUUID().getValue().compareTo(SCfdConsts.UUID_ANNUL) != 0) {
                                        if (maoFolios.getValue().getFolio().get(0).getEstatusUUID().getValue().compareTo(SCfdConsts.UUID_ANNUL_PREV) != 0) {
                                            // Sign & Cancel Log step #4
                                            createSignCancelLog(client, "Codigo: [" + maoFolios.getValue().getFolio().get(0).getEstatusUUID().getValue() + "] Error al intentar cancelar CFDI.", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                                            updateProcessCfd(client, cfd, false);
                                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                            if (pacId == 0) {
                                                throw new Exception("Codigo: [" + maoFolios.getValue().getFolio().get(0).getEstatusUUID().getValue() + "] Error al intentar cancelar CFDI.");
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
                        break;
                        
                    default:
                }
            }
            else {
                // CFDI signing production environment:
                
                switch (pac.getPkPacId()) {
                    case SModSysConsts.TRN_PAC_FCG:
                        com.wscliente.WSForcogsaService fcgService = null;
                        com.wscliente.WSForcogsa fcgPort = null;
                        com.wscliente.WsAutenticarResponse autenticarResponse = null;
                        com.wscliente.WsCancelacionResponse canceladoResponse = null;
                        com.wscliente.WsFoliosResponse foliosResponse = null;

                        fcgService = new com.wscliente.WSForcogsaService();
                        fcgPort = fcgService.getWSForcogsaPort();

                        // Web Service Autentication:

                        // Sign & Cancel Log step #1
                        createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_AUTHENTICATION_PAC, cfd, pac.getPkPacId());

                        autenticarResponse = fcgPort.autenticar("pruebasWS", "pruebasWS");

                        if (autenticarResponse.getMensaje() != null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLog(client, "WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_AUTHENTICATION_PAC, cfd, pac.getPkPacId());

                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                            if (pacId == 0) {
                                System.err.println("WsAutenticarResponse Mensaje: [" + autenticarResponse.getMensaje() + "]");
                                throw new Exception("Error al autenticarse con el PAC:\nMensaje: " + autenticarResponse.getMensaje());
                            }
                            next = false;
                        }

                        if (autenticarResponse.getToken() == null) {
                            // Close current Sign & Cancel Log entry with error status:
                            
                            createSignCancelLog(client, "WsAutenticarResponse Token is null!", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_AUTHENTICATION_PAC, cfd, pac.getPkPacId());

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
                            createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_ACTIVATE, cfd, pac.getPkPacId());

                            updateProcessCfd(client, cfd, true);

                            // Sign & Cancel Log step #3
                            createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_SEND_RECEIVE, cfd, pac.getPkPacId());

                            canceladoResponse = fcgPort.cancelacion1(sRfcEmisor, SLibUtils.DbmsDateFormatDate.format(date), asUuid, companyCertificate.getExtraPublicKeyBytes_n(), companyCertificate.getExtraPrivateKeyBytes_n(), "", autenticarResponse.getToken());

                            if (canceladoResponse.getMensaje() != null) {
                                // Sign & Cancel Log step #4
                                createSignCancelLog(client, "WsCancelacionResponse Codigo: [" + canceladoResponse.getCodEstatus() + "]", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                                updateProcessCfd(client, cfd, false);
                                client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                if (pacId == 0) {
                                    System.err.println("WsCancelacionResponse Codigo: [" + canceladoResponse.getCodEstatus() + "]");
                                    System.err.println("WsCancelacionResponse Mensaje: [" + canceladoResponse.getMensaje() + "]");
                                    System.err.println("UUID: [" + asUuid + "]\t");
                                    throw new Exception("Error al cancelar el documento:\nCódigo: " + canceladoResponse.getCodEstatus() + "\nMensaje: " + canceladoResponse.getMensaje());
                                }
                                next = false;
                            }

                            if (next) {
                                foliosResponse = canceladoResponse.getFolios();

                                if (foliosResponse.getFolio().get(0).getEstatusUUID().compareTo(SCfdConsts.UUID_ANNUL) != 0) {
                                    if (foliosResponse.getFolio().get(0).getEstatusUUID().compareTo(SCfdConsts.UUID_ANNUL_PREV) != 0) {
                                        // Sign & Cancel Log step #4
                                        createSignCancelLog(client, foliosResponse.getFolio().get(0).getMensaje(), !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

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
                        UUIDS uuids = null;
                        JAXBElement<FolioArray> maoFolios = null;
                        StringArray aUuids = null;
                        CancelaCFDResult cancelaCFDResult = null;
                        ReceiptResult receiptResult = null;
                        CancelSOAP service = null;
                        cancel.Application port = null;

                        uuids = new cancel.UUIDS();

                        aUuids = new StringArray();
                        aUuids.getString().addAll(asUuid);

                        QName uuidQName = new QName("uuids");
                        JAXBElement<StringArray> uuidValue = new JAXBElement<StringArray>(uuidQName, StringArray.class, aUuids);

                        uuids.setUuids(uuidValue);

                        service = new cancel.CancelSOAP();
                        port = service.getApplication();

                        // Document cancel:

                        // Sign & Cancel Log step #1, not required!
                        // Sign & Cancel Log step #2, only when validation is not requested
                        createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_ACTIVATE, cfd, pac.getPkPacId());

                        updateProcessCfd(client, cfd, true);

                        // Sign & Cancel Log step #3
                        createSignCancelLog(client, "", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_SEND_RECEIVE, cfd, pac.getPkPacId());

                        if (isValidate) {
                            receiptResult = port.getReceipt("jbarajas@tron.com.mx", "WSfink_2014", sRfcEmisor, cfd.getUuid(), "C");
                        }
                        else {
                            cancelaCFDResult = port.cancel(uuids, "jbarajas@tron.com.mx", "WSfink_2014", sRfcEmisor, companyCertificate.getExtraFnkPublicKeyBytes_n(), companyCertificate.getExtraFnkPrivateKeyBytes_n(), true);
                        }

                        if (isValidate) {
                            if (receiptResult != null) {
                                if (receiptResult.getSuccess() == null) {
                                    // Sign & Cancel Log step #4
                                    createSignCancelLog(client, receiptResult.getError().getValue(), !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                                    updateProcessCfd(client, cfd, false);

                                    if (pacId == 0) {
                                        client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                                        throw new Exception("Error al intentar obtener acuse de cancelación de CFDI.\n" + "Error: " + receiptResult.getError().getValue() + " ");
                                    }
                                    next = false;
                                }
                                else {
                                    if (!receiptResult.getSuccess().getValue()) {
                                        // Sign & Cancel Log step #4
                                        createSignCancelLog(client, "El UUID: '" + cfd.getUuid() + "' no ha sido cancelado.", !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                                        updateProcessCfd(client, cfd, false);

                                        if (pacId == 0) {
                                            throw new Exception("Error al intentar obtener acuse de cancelación de CFDI.\n" + "El UUID: '" + cfd.getUuid() + "' no ha sido cancelado.");
                                        }
                                        next = false;
                                    }
                                    else {
                                        xmlAcuse = receiptResult.getReceipt().getValue();
                                        xmlAcuse = xmlAcuse.replace("&lt;", "<");
                                        xmlAcuse = xmlAcuse.replace("&gt;", ">");
                                    }
                                }
                            }
                        }
                        else {
                            maoFolios = cancelaCFDResult.getFolios();

                            if (maoFolios == null) {
                                // Sign & Cancel Log step #4
                                createSignCancelLog(client, cancelaCFDResult.getCodEstatus().getValue(), !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                                updateProcessCfd(client, cfd, false);
                                client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                if (pacId == 0) {
                                    throw new Exception(cancelaCFDResult.getCodEstatus().getValue());
                                }
                                next = false;
                            }
                            else {
                                if (maoFolios.getValue().getFolio().get(0).getEstatusUUID().getValue().compareTo(SCfdConsts.UUID_ANNUL) != 0) {
                                    if (maoFolios.getValue().getFolio().get(0).getEstatusUUID().getValue().compareTo(SCfdConsts.UUID_ANNUL_PREV) != 0) {
                                        // Sign & Cancel Log step #4
                                        createSignCancelLog(client, maoFolios.getValue().getFolio().get(0).getEstatusUUID().getValue(), !isValidate ? SCfdConsts.ACTION_ANNUL : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_RECEIVE_ERR_PAC, cfd, pac.getPkPacId());

                                        updateProcessCfd(client, cfd, false);
                                        client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                        if (pacId == 0) {
                                            throw new Exception("Codigo: [" + maoFolios.getValue().getFolio().get(0).getEstatusUUID().getValue() + "]\n Error al intentar cancelar CFDI.");
                                        }
                                        next = false;
                                    }
                                }
                                else {
                                    xmlAcuse = cancelaCFDResult.getAcuse().getValue();
                                }
                            }
                        }
                        break;
                        
                    default:
                }
            }
        }
        client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        return xmlAcuse;

    }

    /**
     * Create CFD parameters.
     * @param client ERP Client interface.
     * @param dps DPS regitry.
     * @return CFD parameters.
     */
    private static SCfdParams createCfdParams(final SClientInterface client, final SDataDps dps) {
        String factura = "";
        String pedido = "";
        String contrato = "";
        String ruta = "";
        SDataDps dpsFactura = null;
        SDataDps dpsPedido = null;
        SDataDps dpsContrato = null;
        SDataCustomerBranchConfig cusBranchConfig = null;
        SCfdParams params = new SCfdParams();
        SDataBizPartner moBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BP, new int[]{ dps.getFkBizPartnerId_r()}, SLibConstants.EXEC_MODE_SILENT);
        SDataBizPartnerBranch moBizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BPB,  new int[]{ dps.getFkBizPartnerBranchId()}, SLibConstants.EXEC_MODE_SILENT);

        try {
            params.setReceptor(moBizPartner);
            params.setReceptorBranch(moBizPartnerBranch);
            params.setEmisor(client.getSessionXXX().getCompany().getDbmsDataCompany());

            if (dps.getFkCompanyBranchId() == client.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getPkBizPartnerBranchId()) {
                params.setLugarExpedicion(null);
            }
            else {
                params.setLugarExpedicion(client.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsBizPartnerBranch(new int[] { dps.getFkCompanyBranchId() }).getDbmsBizPartnerBranchAddressOfficial());
            }

            params.setUnidadPesoBruto(SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.ITMU_TP_UNIT, new int[] { SDataConstantsSys.ITMU_TP_UNIT_MASS }, SLibConstants.DESCRIPTION_CODE));
            params.setUnidadPesoNeto(params.getUnidadPesoBruto());

            // Lookup for "pedido" (the first one found):

            for (SDataDpsEntry entryDocumento : dps.getDbmsDpsEntries()) {
                if (entryDocumento.isAccountable()) {
                    for (SDataDpsDpsLink linkPedido : entryDocumento.getDbmsDpsLinksAsDestiny()) {
                        if (!linkPedido.getDbmsIsSourceDeleted() && !linkPedido.getDbmsIsSourceEntryDeleted()) {
                            //dpsPedido = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, linkPedido.getDbmsSourceDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);
                            dpsPedido = STrnUtilities.getFirtsLinkOrderType(client, dps);
                            pedido = (dpsPedido.getNumberSeries().length() == 0 ? "" : dpsPedido.getNumberSeries() + "-") + dpsPedido.getNumber();

                            // Lookup for "contrato" (the first one found):

                            for (SDataDpsEntry entryPedido : dpsPedido.getDbmsDpsEntries()) {
                                if (!entryPedido.getIsDeleted()) {
                                    for (SDataDpsDpsLink linkContrato : entryPedido.getDbmsDpsLinksAsDestiny()) {
                                        if (!linkContrato.getDbmsIsSourceDeleted() && !linkContrato.getDbmsIsSourceEntryDeleted()) {
                                            dpsContrato = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, linkContrato.getDbmsSourceDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);
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
                            dpsFactura = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, linkFactura.getDbmsDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);
                            factura = (dpsFactura.getNumberSeries().length() == 0 ? "" : dpsFactura.getNumberSeries() + "-") + dpsFactura.getNumber();

                            // Lookup for "pedido" (the first one found):

                            for (SDataDpsEntry entryFactura : dpsFactura.getDbmsDpsEntries()) {
                                if (!entryFactura.getIsDeleted()) {
                                    for (SDataDpsDpsLink linkPedido : entryFactura.getDbmsDpsLinksAsDestiny()) {
                                        if (!linkPedido.getDbmsIsSourceDeleted() && !linkPedido.getDbmsIsSourceEntryDeleted()) {
                                            dpsPedido = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, linkPedido.getDbmsSourceDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);
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

            if (dps.getDbmsDataAddenda() != null && moBizPartner.getDbmsCategorySettingsCus().getFkCfdAddendaTypeId() != SDataConstantsSys.BPSS_TP_CFD_ADD_NA) {
                params.setTipoAddenda(dps.getDbmsDataAddenda().getFkCfdAddendaTypeId());
                params.setLorealFolioNotaRecepcion(dps.getDbmsDataAddenda().getLorealFolioNotaRecepcion());
                params.setBachocoSociedad(dps.getDbmsDataAddenda().getBachocoSociedad());
                params.setBachocoOrganizacionCompra(dps.getDbmsDataAddenda().getBachocoOrganizacionCompra());
                params.setBachocoDivision(dps.getDbmsDataAddenda().getBachocoDivision());
                params.setModeloDpsDescripcion(dps.getDbmsDataAddenda().getModeloDpsDescripcion());
                params.setSorianaTienda(dps.getDbmsDataAddenda().getSorianaTienda());
                params.setSorianaEntregaMercancia(dps.getDbmsDataAddenda().getSorianaEntregaMercancia());
                params.setSorianaFechaRemision(dps.getDbmsDataAddenda().getSorianaRemisionFecha());
                params.setSorianaFolioRemision(dps.getDbmsDataAddenda().getSorianaRemisionFolio());
                params.setSorianaFolioPedido(dps.getDbmsDataAddenda().getSorianaPedidoFolio());
                params.setSorianaTipoBulto(dps.getDbmsDataAddenda().getSorianaBultoTipo());
                params.setSorianaCantidadBulto(dps.getDbmsDataAddenda().getSorianaBultoCantidad());
                params.setSorianaNotaEntradaFolio(dps.getDbmsDataAddenda().getSorianaNotaEntradaFolio());
                params.setCfdAddendaSubtype(dps.getDbmsDataAddenda().getCfdAddendaSubtype());
            }

            int xmlType = ((SSessionCustom) client.getSession().getSessionCustom()).getCfdTypeXmlTypes().get(SDataConstantsSys.TRNS_TP_CFD_INV);
            switch (xmlType) {
                case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                    if (dps.getDbmsDataDpsCfd() != null && dps.getDbmsDataDpsCfd().hasInternationalCommerce()) {
                        params.setRegimenFiscal(new String[] { client.getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdRegimenFiscal() });
                    }
                    else {
                        params.setRegimenFiscal(SLibUtilities.textExplode(client.getSessionXXX().getParamsCompany().getFiscalSettings(), ";"));
                    }
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                    params.setRegimenFiscal(new String[] { dps.getDbmsDataDpsCfd().getTaxRegime() });
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }

            // Ruta:

            cusBranchConfig = (SDataCustomerBranchConfig) SDataUtilities.readRegistry(client, SDataConstants.MKT_CFG_CUSB, new int[] { moBizPartnerBranch.getPkBizPartnerBranchId() }, SLibConstants.EXEC_MODE_SILENT);

            if (cusBranchConfig != null) {
                if (cusBranchConfig.getFkSalesRouteId() != 0) {
                    ruta = "" + cusBranchConfig.getFkSalesRouteId();
                }
            }

            params.setRuta(ruta);

            // Miscellaneous:

            params.setInterestDelayRate(client.getSessionXXX().getParamsCompany().getInterestDelayRate());

            params.setAgregarAddenda(true);
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
            if (cfd.getFkCfdTypeId() == SDataConstantsSys.TRNS_TP_CFD_INV) {
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
            }
            else {
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

        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
            cfdiSign = new SCfdiSignature();

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

            if (nodeChild == null) {
                throw new Exception("XML element '" + "tfd:TimbreFiscalDigital" + "' does not exists!");
            }
            else {
                namedNodeMap = nodeChild.getAttributes();

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
        }
        catch (Exception e) {
            SLibUtils.showException(SCfdUtils.class.getName(), e);
        }

        return cfdiSign;
    }

    private static void updateProcessCfd(final SClientInterface client, final SDataCfd cfd, final boolean value) throws Exception {
        cfd.saveField(client.getSession().getDatabase().getConnection(), new int[] { cfd.getPkCfdId() }, SDataCfd.FIELD_ACC_WS, value);
        cfd.saveField(client.getSession().getDatabase().getConnection(), new int[] { cfd.getPkCfdId() }, SDataCfd.FIELD_ACC_XML_STO, value);
        cfd.saveField(client.getSession().getDatabase().getConnection(), new int[] { cfd.getPkCfdId() }, SDataCfd.FIELD_ACC_PDF_STO, value);
        cfd.saveField(client.getSession().getDatabase().getConnection(), new int[] { cfd.getPkCfdId() }, SDataCfd.FIELD_ACC_USR, client.getSession().getUser().getPkUserId());
        client.getSession().notifySuscriptors(SModConsts.HRS_SIE_PAY);
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

    /*
     * Public static methods:
     */

    public static boolean existsCfdiPending(final SClientInterface client, final ArrayList<SDataCfd> cfds) throws Exception {
        if (cfds != null) {
            for (SDataCfd cfd : cfds) {
                if (cfd.getIsProcessingWebService()) {
                    throw new Exception("Existen CFDI pendientes de respuesta del Proveedor Autorizado de Certificación.");
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
                    mnLogSignId = 0;
                    createSignCancelLog(client, "", SCfdConsts.ACTION_DIACTIVATE, SCfdConsts.STATUS_NA, cfd, SLibConsts.UNDEFINED);
                }
            }
            else {
                throw new Exception("El PAC permite la verificación del timbrado o cancelación del CFDI.");
            }
        }
    }

    public static boolean signCfdi(final SClientInterface client, final SDataCfd cfd, final int subtypeCfd, boolean isSingle, boolean confirmSending) throws Exception {
        boolean signed = false;
        
        if (canCfdiSign(client, cfd, false)) {
            if (!isSingle || !confirmSending || client.showMsgBoxConfirm("¿Está seguro que desea timbrar el documento?") == JOptionPane.YES_OPTION) {
                // Open Sign & Cancel Log entry:
                
                mnLogSignId = 0;
                createSignCancelLog(client, "", SCfdConsts.ACTION_SIGN, SCfdConsts.STATUS_NA, cfd, getPacConfiguration(client, cfd.getFkCfdTypeId()).getFkPacId());

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

                        mnLogSignId = 0;
                        createSignCancelLog(client, "", SCfdConsts.ACTION_ANNUL, SCfdConsts.STATUS_NA, cfd, pacId != 0 ? pacId : getPacConfiguration(client, cfd.getFkCfdTypeId()).getFkPacId());

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

    public static void printCfd(final SClientInterface client, final int typeCfd, final SDataCfd cfd, int printMode, final int subtypeCfd, boolean isSaving) throws Exception {
        printCfd(client, typeCfd, cfd, printMode, SDataConstantsPrint.PRINT_A_COPY, subtypeCfd, isSaving);
    }
    
    public static void printCfd(final SClientInterface client, final int typeCfd, final SDataCfd cfd, int printMode, int copies, final int subtypeCfd, boolean isSaving) throws Exception {
        SCfdPrint cfdPrint = null;
        SDataDps dps = null;
        SCfdParams params = null;

        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else {
            if (canPrint(cfd, isSaving)) {
                cfdPrint = new SCfdPrint(client);

                if (typeCfd != SDataConstantsSys.TRNS_TP_CFD_PAYROLL) {
                    dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, new int[] { cfd.getFkDpsYearId_n(), cfd.getFkDpsDocId_n() }, SLibConstants.EXEC_MODE_SILENT);
                    params = createCfdParams(client, dps);
                    dps.setAuxCfdParams(params);
                }

                if (cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFD) {
                    cfdPrint.printCfd(cfd, printMode, dps);
                }
                else if (cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_32) {
                    switch (typeCfd) {
                        case SDataConstantsSys.TRNS_TP_CFD_INV:
                            cfdPrint.printCfdi32(cfd, printMode, dps);
                            break;
                        case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                            if (DCfdUtils.getVersionPayrollComplement(cfd.getDocXml()) == DCfdVer3Consts.VER_NOM_11) {
                                cfdPrint.printPayrollReceipt(cfd, printMode, copies, subtypeCfd);
                            }
                            else if (DCfdUtils.getVersionPayrollComplement(cfd.getDocXml()) == DCfdVer3Consts.VER_NOM_12) {
                                cfdPrint.printPayrollReceipt32_12(cfd, printMode, copies, subtypeCfd);
                            }
                            break;
                        default:
                            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }
                }
                else if (cfd.getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI_33) {
                    switch (typeCfd) {
                        case SDataConstantsSys.TRNS_TP_CFD_INV:
                            cfdPrint.printCfdi33(cfd, printMode, dps);
                            break;
                        case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                            if (DCfdUtils.getVersionPayrollComplement(cfd.getDocXml()) == DCfdVer3Consts.VER_NOM_11) {
                                cfdPrint.printPayrollReceipt(cfd, printMode, copies, subtypeCfd);
                            }
                            else if (DCfdUtils.getVersionPayrollComplement(cfd.getDocXml()) == DCfdVer3Consts.VER_NOM_12) {
                                //cfdPrint.printPayrollReceipt33_12(cfd, printMode, copies, subtypeCfd); XXX jbarajas pendiente CFDI 3.3
                            }
                            break;
                        default:
                            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }
                }
                else {
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
            }
        }
    }

    public static void printAcknowledgmentCancellationCfd(final SClientInterface client, final SDataCfd cfd, int printMode, final int subtypeCfd) throws Exception {
        SCfdPrint cfdPrint = null;

        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else {
            if (canObtainAcknowledgmentCancellation(client, cfd)) {
                if (!cfd.getAcknowledgmentCancellation().isEmpty()) {
                    cfdPrint = new SCfdPrint(client);

                    cfdPrint.printAcknowledgment(cfd, printMode, subtypeCfd);
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
     * @throws Exception
     */
    public static boolean signAndSendCfdi(final SClientInterface client, final SDataCfd cfd, final int subtypeCfd, boolean isSingle, boolean confirmSending) throws Exception {
        boolean sign = false;
        boolean signAndSend = false;
        boolean catchExceptions = false;
        int idBizPartner = SLibConsts.UNDEFINED;
        int idBizPartnerBranch = SLibConsts.UNDEFINED;
        SDataDps dps = null;
        SDataCfd cfdAuxSend = null;

        try {
            // Sign CFDI:
            
            if (canCfdiSign(client, cfd, false)) {

                switch (cfd.getFkCfdTypeId()) {
                    case SDataConstantsSys.TRNS_TP_CFD_INV:
                        dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, new int[] { cfd.getFkDpsYearId_n(), cfd.getFkDpsDocId_n() }, SLibConstants.EXEC_MODE_SILENT);
                        idBizPartner = dps.getFkBizPartnerId_r();
                        idBizPartnerBranch = dps.getFkBizPartnerBranchId();
                        break;

                    case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                        idBizPartner = subtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? cfd.getFkPayrollBizPartnerId_n() : cfd.getFkPayrollReceiptEmployeeId_n();
                        idBizPartnerBranch = SLibConsts.UNDEFINED; // do not really needed, just for consistence
                        break;

                    default:
                }

                if (!confirmSending || STrnUtilities.confirmSend(client,TXT_SIGN_SEND, cfd, null, idBizPartner, idBizPartnerBranch)) {
                    sign = signCfdi(client, cfd, subtypeCfd, isSingle, false);

                    // Send CFDI:

                    if (sign) {
                        cfdAuxSend = (SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, cfd.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                        if (confirmSending) {
                            confirmSending = false;
                            catchExceptions = true;
                        }
                        else{
                            catchExceptions = true;
                        }

                        sendCfd(client, cfdAuxSend.getFkCfdTypeId(), cfdAuxSend, subtypeCfd, false, confirmSending, catchExceptions);
                        signAndSend = true;
                    }
                }
            }
        }
        catch (Exception e) {
            if (sign) {
                throw new Exception("Timbrado, pero no enviado:\n" + e.getMessage());
            }
            else {
                throw new Exception("No fue posible timbrar ni enviar el documento:\n" + e.getMessage());
            }
        }
        
        return signAndSend;
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

                        mnLogSignId = 0;
                        createSignCancelLog(client, "", SCfdConsts.ACTION_ANNUL, SCfdConsts.STATUS_NA, cfd, pacId != 0 ? pacId : getPacConfiguration(client, cfd.getFkCfdTypeId()).getFkPacId());

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
        SCfdPacket packet = null;
        SCfdParams params = null;
        cfd.ver2.DElementComprobante comprobanteCfd = null;
        cfd.ver32.DElementComprobante comprobanteCfdi32 = null;
        cfd.ver33.DElementComprobante comprobanteCfdi33 = null;

        packet = new SCfdPacket();
        packet.setDpsYearId(dps.getPkYearId());
        packet.setDpsDocId(dps.getPkDocId());
        packet.setIsConsistent(true);
        packet.setDps(dps);
        packet.setCfdId(dps.getDbmsDataCfd() == null ? 0 : dps.getDbmsDataCfd().getPkCfdId());

        params = createCfdParams(client, dps);

        if (params != null) {
            dps.setAuxCfdParams(params);

            float cfdVersion = SLibConsts.UNDEFINED;
            switch (xmlType) {
                case SDataConstantsSys.TRNS_TP_XML_CFD:
                    comprobanteCfd = (cfd.ver2.DElementComprobante) createCfdRootElement(client, dps);
                    cfdVersion = comprobanteCfd.getVersion();
                    
                    packet.setStringSigned(DCfdUtils.generateOriginalString(comprobanteCfd));
                    packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_EMITED);
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                    comprobanteCfdi32 = (cfd.ver32.DElementComprobante) createCfdi32RootElement(client, dps);
                    cfdVersion = comprobanteCfdi32.getVersion();
                    
                    packet.setStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi32));
                    packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW);
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                    comprobanteCfdi33 = (cfd.ver33.DElementComprobante) createCfdi33RootElement(client, dps);
                    cfdVersion = comprobanteCfdi33.getVersion();
                    
                    packet.setStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi33));
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

            packet.setSignature(client.getCfdSignature(cfdVersion).sign(packet.getStringSigned(), SLibTimeUtilities.digestYear(dps.getDate())[0]));
            packet.setCertNumber(client.getCfdSignature(cfdVersion).getCertNumber());
            packet.setCertBase64(client.getCfdSignature(cfdVersion).getCertBase64());

            switch (xmlType) {
                case SDataConstantsSys.TRNS_TP_XML_CFD:
                    comprobanteCfd.getAttSello().setString(packet.getSignature());
                    packet.setCfdRootElement(comprobanteCfd);
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                    comprobanteCfdi32.getAttSello().setString(packet.getSignature());
                    packet.setCfdRootElement(comprobanteCfdi32);
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                    comprobanteCfdi33.getAttSello().setString(packet.getSignature());
                    packet.setCfdRootElement(comprobanteCfdi33);
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

                formerPayrollDummy.renderPayroll(formerPayrollCfdsEmited, SCfdConsts.CFDI_PAYROLL_VER_OLD);
            }

            validateReceiptsConsistent(client, formerPayroll, formerPayrollDummy, isRegenerateOnlyNonStampedCfdi);

            payrollImport = new SDbFormerPayrollImport();

            payrollImport.setPayrollId(formerPayroll.getPkNominaId());
            payrollImport.setRegenerateOnlyNonStampedCfdi(isRegenerateOnlyNonStampedCfdi);
            payrollImport.setCfdPackets(maCfdPackets);

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
        int stampAvailables = 0;
        ArrayList<SDataCfd> cfdsValidate = null;
        SDialogResult dialogResult = null;
        
        cfdsValidate = new ArrayList<SDataCfd>();
        
        if (cfds.isEmpty()) {
            client.showMsgBoxInformation("No existen documentos para validar.");
        }
        else {
            for(SDataCfd cfd : cfds) {
                if (cfd.getIsProcessingWebService() || cfd.getIsProcessingStorageXml() || cfd.getIsProcessingStoragePdf()) {
                    cfdsValidate.add(cfd);
                }
                cfdsValidate.add(cfd);
            }

            stampAvailables = getStampsAvailable(client, cfdsValidate.get(0).getFkCfdTypeId(), cfdsValidate.get(0).getTimestamp(), 0);
            if (existsCfdiEmitInconsist(client, cfdsValidate)) {
                dialogResult = new SDialogResult((SClient) client, "Resultados de verificación", SCfdConsts.PROC_REQ_VERIFY);

                dialogResult.setFormParams(client, cfdsValidate, null, stampAvailables, null, false, subtypeCfd, SModSysConsts.TRNU_TP_DPS_ANN_NA);
                dialogResult.setVisible(true);
            }
        }

        return valid;
    }

    public static boolean verifyCfdi(final SClientInterface client, final SDataCfd cfd, final int subtypeCfd) throws Exception {
        boolean valid = false;
        SCfdPrint cfdPrint = null;
        SDataDps dps = null;
        SCfdParams params = null;
        SDataPac pac = null;
        BufferedWriter bw = null;

        if (cfd == null) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else if (cfd.getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFDI_32 && cfd.getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFDI_33) {
            throw new Exception("No es un CFDI.");
        }
        else {
            pac = getPacForValidate(client, cfd);

            if (pac == null) {
                throw new Exception("Error al leer el catálogo de PAC's.\n -No existe ningún PAC registrado para la verificación del CFDI.");
            }

            if (cfd.getIsProcessingWebService() || cfd.getIsProcessingStorageXml() || cfd.getIsProcessingStoragePdf()) {
                client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));

                if (cfd.getIsProcessingWebService()) {
                    // Open Sign & Cancel Log entry:
                    
                    mnLogSignId = 0;
                    createSignCancelLog(client, "", !cfd.isStamped() ? SCfdConsts.ACTION_RESTORE_SIGN : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_NA, cfd, pac.getPkPacId());

                    if (!isSignedByFinkok(client, cfd)) {
                        // Request to user the file correct

                        if (!cfd.isStamped()) {
                            // Save file XML and set flag XML as correct:

                            valid = restoreSignXml(client, cfd, false, subtypeCfd);
                        }
                        else {
                            // Save file PDF and set flag PDF as correct:

                            valid = restoreAcknowledgmentCancellation(client, cfd, false, subtypeCfd);
                        }
                    }
                    else {
                        if (!cfd.isStamped()) {
                            valid = stampedCfdiFinkok(client, cfd, subtypeCfd);
                        }
                        else {
                            valid = getReceiptCancellationCfdi(client, cfd, subtypeCfd, SModSysConsts.TRNU_TP_DPS_ANN_NA);
                        }
                    }
                }
                else if (cfd.getIsProcessingStorageXml() || cfd.getIsProcessingStoragePdf()) {
                    cfdPrint = new SCfdPrint(client);

                    if (cfd.getIsProcessingStorageXml()) {
                        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(client.getSessionXXX().getParamsCompany().getXmlBaseDirectory() + cfd.getDocXmlName()), "UTF-8"));
                        bw.write(cfd.getDocXml());
                        bw.close();
                    }

                    if (cfd.getIsProcessingStoragePdf()) {
                        switch (cfd.getFkCfdTypeId()) {
                            case SDataConstantsSys.TRNS_TP_CFD_INV:
                                dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, new int[] { cfd.getFkDpsYearId_n(), cfd.getFkDpsDocId_n() }, SLibConstants.EXEC_MODE_SILENT);
                                params = createCfdParams(client, dps);
                                dps.setAuxCfdParams(params);
                                cfdPrint.printCfdi32(cfd, SDataConstantsPrint.PRINT_MODE_PDF, dps);
                                break;
                            case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                                if (DCfdUtils.getVersionPayrollComplement(cfd.getDocXml()) == DCfdVer3Consts.VER_NOM_11) {
                                    cfdPrint.printPayrollReceipt(cfd, SDataConstantsPrint.PRINT_MODE_PDF, subtypeCfd);
                                }
                                else if (DCfdUtils.getVersionPayrollComplement(cfd.getDocXml()) == DCfdVer3Consts.VER_NOM_12) {
                                    cfdPrint.printPayrollReceipt32_12(cfd, SDataConstantsPrint.PRINT_MODE_PDF, subtypeCfd);
                                }
                                break;
                            default:
                                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                    }
                    updateProcessCfd(client, cfd, false);
                    client.showMsgBoxInformation(SLibConsts.MSG_PROCESS_FINISHED);
                }
                client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            else {
                throw new Exception("No es necesario validar el CFDI.");
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
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else if (cfd.getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFDI_32 && cfd.getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFDI_33) {
            throw new Exception("No es un CFDI.");
        }
        else {
            if (cfd.isStamped()) {
                throw new Exception("No es necesario restaurar el CFDI.");
            }
        }

        pac = getPacForValidate(client, cfd);

        restoreCfdi = new SDialogRestoreCfdi(client, SCfdConsts.ACTION_SIGN, SCfdConsts.CFDI_FILE_XML);
        restoreCfdi.formReset();
        restoreCfdi.setFormVisible(true);

        if (restoreCfdi.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            fileXml = SXmlUtils.readXml(restoreCfdi.getFileXml());

            if (isUser) {
                // Open Sign & Cancel Log entry:
                
                mnLogSignId = 0;
                createSignCancelLog(client, "", !cfd.isStamped() ? SCfdConsts.ACTION_RESTORE_SIGN : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_NA, cfd, pac == null ? 0 : pac.getPkPacId());
            }

            if (!belongsXmlToCfd(fileXml, cfd)) {
                createSignCancelLog(client, "El archivo XML proporcionado no pertenece al CFDI seleccionado.", !cfd.isStamped() ? SCfdConsts.ACTION_RESTORE_SIGN : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_NA, cfd, pac == null ? 0 : pac.getPkPacId());

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
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else if (cfd.getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFDI_32 && cfd.getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFDI_33) {
            throw new Exception("No es un CFDI.");
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

        pac = getPacForValidate(client, cfd);

        restoreCfdi = new SDialogRestoreCfdi(client, SCfdConsts.ACTION_ANNUL, SCfdConsts.CFDI_FILE_PDF);
        restoreCfdi.formReset();
        restoreCfdi.setFormVisible(true);

        if (restoreCfdi.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            if (restoreCfdi.getFilePdf() != null) {

                if (isUser) {
                    // Open Sign & Cancel Log entry:
                    
                    mnLogSignId = 0;
                    createSignCancelLog(client, "", !cfd.isStamped() ? SCfdConsts.ACTION_RESTORE_SIGN : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_NA, cfd, pac.getPkPacId());
                }

                saveAcknowledgmentCancellationPdf(client, cfd, restoreCfdi.getFilePdf());
                managementCfdi(client, cfd, SDataConstantsSys.TRNS_ST_DPS_ANNULED, null, true, true, pac == null ? 0 : pac.getPkPacId(), subtypeCfd, "", true);
                isRestore = true;
            }
            else {
                createSignCancelLog(client, "El archivo proporcionado es erróneo.", !cfd.isStamped() ? SCfdConsts.ACTION_RESTORE_SIGN : SCfdConsts.ACTION_RESTORE_ANNUL, SCfdConsts.STATUS_NA, cfd, pac.getPkPacId());
                throw new Exception("El archivo proporcionado es erróneo.");
            }
        }

        return isRestore;
    }

    public static boolean isSignedByFinkok(final SClientInterface client, final SDataCfd cfd) {
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

    public static SDataPac getPacForValidate(final SClientInterface client, final SDataCfd cfd) {
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
    public static int getStampsAvailable(final SClientInterface client, final int cfdTypeId, final Date date, final int pacId)  {
        SDataCfdPacType cfdPacType = null;
        SDataPac pac = null;
        String sql = "";
        ResultSet resultSet = null;
        int nStampsAvailable = 0;

        try {
            cfdPacType = getPacConfiguration(client, cfdTypeId);

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
        comprobante.getAttTipoCambio().setDouble(xmlCfd.getComprobanteTipoDeCambio());
        comprobante.getAttMoneda().setString(xmlCfd.getComprobanteMoneda());
        comprobante.getAttTotal().setDouble(xmlCfd.getComprobanteTotal());

        comprobante.getAttTipoDeComprobante().setOption(xmlCfd.getComprobanteTipoDeComprobante());
        comprobante.getAttMetodoDePago().setString(xmlCfd.getComprobanteMetodoDePago());

        emisor = new SDbCfdBizPartner(client);
        emisor.setBizPartnerId(xmlCfd.getEmisorId());
        emisor.setBizPartnerBranchId(xmlCfd.getEmisorSucursalId());
        emisor.setIsEmisor(true);

        asociadoNegocios = emisor.getBizPartner();
        asociadoNegocios.setVersion(fVersion);

        comprobante.setEltEmisor((cfd.ver2.DElementEmisor) asociadoNegocios.createRootElementEmisor());
        comprobante.getAttLugarExpedicion().setString(asociadoNegocios.getCfdLugarExpedicion());
        comprobante.getAttNumCtaPago().setString(xmlCfd.getComprobanteNumCtaPago());

        for (DElement regimen : xmlCfd.getElementsEmisorRegimenFiscal()) {
            comprobante.getEltEmisor().getEltHijosRegimenFiscal().add((cfd.ver2.DElementRegimenFiscal) regimen);
        }

        receptor = new SDbCfdBizPartner(client);
        receptor.setBizPartnerId(xmlCfd.getReceptorId());
        receptor.setBizPartnerBranchId(xmlCfd.getReceptorSucursalId());

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
            switch (tax.getImpuestoBasico()) {
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
                    throw new Exception("Todos los tipos de impuestos deben ser conocidos (" + tax.getImpuestoBasico() + ").");
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
        boolean hasInternationalTradeNode = false;
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
            comprobante.getAttTipoCambio().setDouble(xmlCfdi.getComprobanteTipoDeCambio());
        }
        comprobante.getAttMoneda().setString(xmlCfdi.getComprobanteMoneda());
        comprobante.getAttTotal().setDouble(xmlCfdi.getComprobanteTotal());

        comprobante.getAttTipoDeComprobante().setOption(xmlCfdi.getComprobanteTipoDeComprobante());
        comprobante.getAttMetodoDePago().setString(((SSessionCustom) client.getSession().getSessionCustom()).getCfdXmlCatalogs().getEntryCode(SDataConstantsSys.TRNS_CFD_CAT_PAY_WAY, xmlCfdi.getComprobanteMetodoDePago()));

        elementComplement = xmlCfdi.getElementComplemento();

        if (elementComplement != null) {
            hasInternationalTradeNode = ((cfd.ver32.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior") != null;
        }
        
        emisor = new SDbCfdBizPartner(client);
        emisor.setBizPartnerId(xmlCfdi.getEmisorId());
        emisor.setBizPartnerBranchId(xmlCfdi.getEmisorSucursalId());
        emisor.setBizPartnerExpeditionId(xmlCfdi.getEmisorId());
        emisor.setBizPartnerBranchExpeditionId(xmlCfdi.getEmisorSucursalId());
        emisor.setIsEmisor(true);
        emisor.setIsEmisorForCce(hasInternationalTradeNode);

        asociadoNegocios = emisor.getBizPartner();
        asociadoNegocios.setIsCfdi(true);
        asociadoNegocios.setIsCfdiWithCce(hasInternationalTradeNode);
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
        receptor.setBizPartnerId(xmlCfdi.getReceptorId());
        receptor.setBizPartnerBranchId(xmlCfdi.getReceptorSucursalId());
        receptor.setIsEmisorForCce(false);

        asociadoNegocios = receptor.getBizPartner();
        asociadoNegocios.setIsCfdi(true);
        asociadoNegocios.setIsCfdiWithCce(hasInternationalTradeNode);
        asociadoNegocios.setIsStateCodeAssociate(SLocUtils.hasAssociateStates(client.getSession(), asociadoNegocios.getBizPartnerCountryId()));
        asociadoNegocios.setVersion(DCfdConsts.CFDI_VER_32);
        asociadoNegocios.setCfdiType(xmlCfdi.getCfdType());

        comprobante.setEltReceptor((cfd.ver32.DElementReceptor) asociadoNegocios.createRootElementReceptor());
        
        if (elementComplement != null && hasInternationalTradeNode) {
            ((cfd.ver3.cce11.DElementComercioExterior) ((cfd.ver32.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior")).setEltReceptor((cfd.ver3.cce11.DElementReceptor) asociadoNegocios.createRootElementReceptorCce());
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

        cfd.ver32.DElementImpuestosRetenidos impuestosRetenidos = new cfd.ver32.DElementImpuestosRetenidos();
        cfd.ver32.DElementImpuestosTrasladados impuestosTrasladados = new cfd.ver32.DElementImpuestosTrasladados();

        for (SCfdDataImpuesto tax : xmlCfdi.getElementsImpuestos(DCfdConsts.CFDI_VER_32)) {
            cfd.ver32.DElementImpuestoRetencion impuestoRetencion = new cfd.ver32.DElementImpuestoRetencion();
            cfd.ver32.DElementImpuestoTraslado impuestoTraslado = new cfd.ver32.DElementImpuestoTraslado();

            switch (tax.getImpuestoBasico()) {
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
                    throw new Exception("Todos los tipos de impuestos deben ser conocidos (" + tax.getImpuestoBasico() + ").");
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
        
        if (elementComplement != null) {
            comprobante.setEltOpcComplemento((cfd.ver32.DElementComplemento) elementComplement);
        }

        if (xmlCfdi.getElementAddenda() != null) {
            comprobante.setEltOpcAddenda((cfd.ver32.DElementAddenda) xmlCfdi.getElementAddenda());
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
        double dTotalImptoRetenido = 0;
        double dTotalImptoTrasladado = 0;
        SDbCfdBizPartner emisor = null;
        SDbCfdBizPartner receptor = null;
        SCfdDataBizPartner asociadoNegocios = null;
        
        boolean isPayroll = xmlCfdi.getCfdType() == SDataConstantsSys.TRNS_TP_CFD_PAYROLL;

        cfd.ver33.DElementComprobante comprobante = new cfd.ver33.DElementComprobante();

        comprobante.getAttSerie().setString(xmlCfdi.getComprobanteSerie());
        comprobante.getAttFolio().setString(xmlCfdi.getComprobanteFolio());
        comprobante.getAttFecha().setDatetime(xmlCfdi.getComprobanteFecha());
        //comprobante.getAttSello(...
        comprobante.getAttFormaPago().setString(xmlCfdi.getComprobanteFormaPago());
        comprobante.getAttNoCertificado().setString(client.getCfdSignature(DCfdConsts.CFDI_VER_33).getCertNumber());
        comprobante.getAttCertificado().setString(client.getCfdSignature(DCfdConsts.CFDI_VER_33).getCertBase64());
        comprobante.getAttCondicionesDePago().setString(isPayroll ? "" : xmlCfdi.getComprobanteCondicionesPago());
        comprobante.getAttSubTotal().setDouble(xmlCfdi.getComprobanteSubtotal());
        comprobante.getAttDescuento().setDouble(isPayroll ? 0d : xmlCfdi.getComprobanteDescuento());
        comprobante.getAttMoneda().setString(xmlCfdi.getComprobanteMoneda());
        if (xmlCfdi.getComprobanteMoneda().compareTo(SModSysConsts.FINS_FISCAL_CUR_MXN_NAME) != 0 && xmlCfdi.getComprobanteMoneda().compareTo(SModSysConsts.FINS_FISCAL_CUR_XXX_NAME) != 0) {
            comprobante.getAttTipoCambio().setDouble(isPayroll ? 0d : xmlCfdi.getComprobanteTipoCambio());
        }
        comprobante.getAttTotal().setDouble(xmlCfdi.getComprobanteTotal());
        comprobante.getAttTipoDeComprobante().setString(xmlCfdi.getComprobanteTipoComprobante());
        comprobante.getAttLugarExpedicion().setString(xmlCfdi.getComprobanteLugarExpedicion());
        comprobante.getAttConfirmacion().setString(xmlCfdi.getComprobanteConfirmacion());
        
        if (!xmlCfdi.getCfdiRelacionados().isEmpty()) {
            cfd.ver33.DElementCfdiRelacionados cfdiRelacionados = new DElementCfdiRelacionados();
            
            cfdiRelacionados.getAttTipoRelacion().setString(xmlCfdi.getCfdiRelacionadosTipoRelacion());
            
            for (String uuid : xmlCfdi.getCfdiRelacionados()) {
                cfd.ver33.DElementCfdiRelacionado cfdiRelacionado = new cfd.ver33.DElementCfdiRelacionado();
                cfdiRelacionado.getAttUuid().setString(uuid);
                cfdiRelacionados.getEltCfdiRelacionados().add(cfdiRelacionado);
            }
            
            comprobante.setEltOpcCfdiRelacionados(cfdiRelacionados);
        }
        
        boolean hasIntCommerceNode = false;
        cfd.DElement elementComplement = xmlCfdi.getElementComplemento();

        if (elementComplement != null) {
            hasIntCommerceNode = ((cfd.ver32.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior") != null;
        }
        
        emisor = new SDbCfdBizPartner(client);
        emisor.setBizPartnerId(xmlCfdi.getEmisorId());
        emisor.setBizPartnerBranchId(xmlCfdi.getEmisorSucursalId());
        emisor.setBizPartnerExpeditionId(xmlCfdi.getEmisorId());
        emisor.setBizPartnerBranchExpeditionId(xmlCfdi.getEmisorSucursalId());
        emisor.setIsEmisor(true);
        emisor.setIsEmisorForCce(hasIntCommerceNode);

        asociadoNegocios = emisor.getBizPartner();
        asociadoNegocios.setIsCfdi(true);
        asociadoNegocios.setIsCfdiWithCce(hasIntCommerceNode);
        asociadoNegocios.setVersion(DCfdConsts.CFDI_VER_33);
        asociadoNegocios.setCfdiType(xmlCfdi.getCfdType());

        cfd.ver33.DElementEmisor elementEmisor = (cfd.ver33.DElementEmisor) asociadoNegocios.createRootElementEmisor();
        elementEmisor.getAttRegimenFiscal().setString(xmlCfdi.getEmisorRegimenFiscal());
        
        if (elementComplement != null && hasIntCommerceNode) {
            ((cfd.ver3.cce11.DElementComercioExterior) ((cfd.ver32.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior")).setEltEmisor((cfd.ver3.cce11.DElementEmisor) asociadoNegocios.createRootElementEmisorCce());
        }

        comprobante.setEltEmisor(elementEmisor);

        receptor = new SDbCfdBizPartner(client);
        receptor.setBizPartnerId(xmlCfdi.getReceptorId());
        receptor.setBizPartnerBranchId(xmlCfdi.getReceptorSucursalId());
        receptor.setIsEmisorForCce(false);

        asociadoNegocios = receptor.getBizPartner();
        asociadoNegocios.setIsCfdi(true);
        asociadoNegocios.setIsCfdiWithCce(hasIntCommerceNode);
        asociadoNegocios.setVersion(DCfdConsts.CFDI_VER_33);
        asociadoNegocios.setCfdiType(xmlCfdi.getCfdType());

        cfd.ver33.DElementReceptor elementReceptor = (cfd.ver33.DElementReceptor) asociadoNegocios.createRootElementReceptor();
        elementReceptor.getAttUsoCFDI().setString(xmlCfdi.getReceptorUsoCFDI());
        
        if (elementComplement != null && hasIntCommerceNode) {
            ((cfd.ver3.cce11.DElementComercioExterior) ((cfd.ver32.DElementComplemento) elementComplement).extractChildElements("cce11:ComercioExterior")).setEltReceptor((cfd.ver3.cce11.DElementReceptor) asociadoNegocios.createRootElementReceptorCce());
        }
        
        comprobante.setEltReceptor(elementReceptor);

        for (SCfdDataConcepto concept : xmlCfdi.getElementsConcepto()) {
            comprobante.getEltConceptos().getEltConceptos().add(concept.createRootElementConcept33());
        }

        // Taxes:

        if (xmlCfdi.getCfdType() != SDataConstantsSys.TRNS_TP_CFD_PAYROLL) {
            cfd.ver33.DElementImpuestosRetenciones impuestosRetenciones = new cfd.ver33.DElementImpuestosRetenciones();
            cfd.ver33.DElementImpuestosTraslados impuestosTrasladados = new cfd.ver33.DElementImpuestosTraslados();

            for (SCfdDataImpuesto tax : xmlCfdi.getElementsImpuestos(DCfdConsts.CFDI_VER_33)) {
                switch (tax.getImpuestoBasico()) {
                    case SModSysConsts.FINS_TP_TAX_RETAINED:
                        dTotalImptoRetenido += tax.getImporte();
                        impuestosRetenciones.getEltImpuestoRetenciones().add((cfd.ver33.DElementImpuestoRetencion) tax.createRootElementImpuesto33());
                        break;
                    case SModSysConsts.FINS_TP_TAX_CHARGED:
                        dTotalImptoTrasladado += tax.getImporte();
                        impuestosTrasladados.getEltImpuestoTrasladados().add((cfd.ver33.DElementImpuestoTraslado) tax.createRootElementImpuesto33());
                        break;
                    default:
                        throw new Exception("Todos los tipos de impuestos deben ser conocidos (" + tax.getImpuestoBasico() + ").");
                }
            }

            if (!impuestosRetenciones.getEltImpuestoRetenciones().isEmpty()) {
                if (comprobante.getEltOpcImpuestos() == null) {
                    comprobante.setEltOpcImpuestos(new DElementImpuestos(comprobante));
                }
                comprobante.getEltOpcImpuestos().getAttTotalImpuestosRetenidos().setDouble(dTotalImptoRetenido);
                comprobante.getEltOpcImpuestos().setEltOpcImpuestosRetenidos(impuestosRetenciones);
            }

            if (!impuestosTrasladados.getEltImpuestoTrasladados().isEmpty()) {
                if (comprobante.getEltOpcImpuestos() == null) {
                    comprobante.setEltOpcImpuestos(new DElementImpuestos(comprobante));
                }
                comprobante.getEltOpcImpuestos().getAttTotalImpuestosTraslados().setDouble(dTotalImptoTrasladado);
                comprobante.getEltOpcImpuestos().setEltOpcImpuestosTrasladados(impuestosTrasladados);
            }
        }
        
        if (elementComplement != null) {
            comprobante.setEltOpcComplemento((cfd.ver32.DElementComplemento) elementComplement);
        }

        if (xmlCfdi.getElementAddenda() != null) {
            comprobante.setEltOpcAddenda((cfd.ver32.DElementAddenda) xmlCfdi.getElementAddenda());
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
        
        // validate subtotal concepts:
        
        for (int i = 0; i < comprobante.getEltConceptos().getEltConceptos().size(); i++) {
            oConcepto = comprobante.getEltConceptos().getEltConceptos().get(i);
            
            dSubtotalImporte = SLibUtils.round(oConcepto.getAttCantidad().getDouble() * oConcepto.getAttValorUnitario().getDouble(), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
            
            if (Math.abs(oConcepto.getAttImporte().getDouble() - dSubtotalImporte) >= SLibConstants.RES_VAL_DECS) {
                throw new Exception("El monto del cálculo del importe del concepto '" + oConcepto.getAttDescripcion().getString() + "' es incorrecto.");
            }
            
            dSubtotalConceptos = SLibUtils.round((dSubtotalConceptos + dSubtotalImporte), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
        }
        
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
        
        if (comprobante.getEltOpcImpuestos().getEltOpcImpuestosRetenidos() != null) {
            for (int i = 0; i < comprobante.getEltOpcImpuestos().getEltOpcImpuestosRetenidos().getEltImpuestoRetenciones().size(); i++) {
                oRetencion = comprobante.getEltOpcImpuestos().getEltOpcImpuestosRetenidos().getEltImpuestoRetenciones().get(i);

                dTotalImptoRetenidos = SLibUtils.round((dTotalImptoRetenidos + oRetencion.getAttImporte().getDouble()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
            }

            if (Math.abs(comprobante.getEltOpcImpuestos().getAttTotalImpuestosRetenidos().getDouble() - dTotalImptoRetenidos) >= SLibConstants.RES_VAL_DECS) {
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

    public static String composeCfdWithTfdTimbreFiscalDigital(final SClientInterface client, final SDataCfd xmlCfd, final String xml) throws Exception {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document docStamping = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        Document docOriginal = docBuilder.parse(new ByteArrayInputStream(xmlCfd.getDocXml().getBytes("UTF-8")));
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
            cfdsValidate = getPayrollCfds(client, subtypeCfd,  new int[] { subtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? cfd.getFkPayrollPayrollId_n() : cfd.getFkPayrollReceiptPayrollId_n()});
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
        int stampAvailables = 0;
        ArrayList<SDataCfd> cfdsValidate = null;
        ArrayList<SDataCfd> cfdsAux = null;
        SDialogResult dialogResult = null;
        boolean needSign = false;

        cfdsValidate = new ArrayList<SDataCfd>();
        cfdsAux = new ArrayList<SDataCfd>();

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
                stampAvailables = getStampsAvailable(client, cfdsAux.get(0).getFkCfdTypeId(), cfdsAux.get(0).getTimestamp(), 0);
                needSign = isNeedStamps(client, cfdsAux.get(0), SDbConsts.ACTION_SAVE, 0);

                if (needSign && stampAvailables == 0) {
                    client.showMsgBoxWarning("No existen timbres disponibles.");
                }
                else {
                    if (needSign && cfdsAux.size() > stampAvailables) {
                        signed = client.showMsgBoxConfirm("Timbres insuficientes:\n -Solo existen '" + stampAvailables + "' timbres disponibles.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
                    }

                    if (existsCfdiEmitInconsist(client, cfdsValidate)) {
                        if (signed) {
                            dialogResult = new SDialogResult((SClient) client, "Resultados de timbrado", SCfdConsts.PROC_REQ_STAMP);

                            dialogResult.setFormParams(client, cfdsAux, null, stampAvailables, null, needSign, subtypeCfd, SModSysConsts.TRNU_TP_DPS_ANN_NA);
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
        int stampAvailables = 0;
        ArrayList<SDataCfd> cfdsValidate = null;
        ArrayList<SDataCfd> cfdsAux = null;
        SDialogResult dialogResult = null;
        boolean needSign = false;

        cfdsValidate = new ArrayList<SDataCfd>();
        cfdsAux = new ArrayList<SDataCfd>();

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
            if (client.showMsgBoxConfirm("¿Está seguro que desea timbrar y enviar " + cfdsAux.size() + " documentos?") == JOptionPane.YES_OPTION) {
                signedSent = true;
                stampAvailables = getStampsAvailable(client, cfdsAux.get(0).getFkCfdTypeId(), cfdsAux.get(0).getTimestamp(), 0);
                needSign = isNeedStamps(client, cfdsAux.get(0), SDbConsts.ACTION_SAVE, 0);

                if (needSign && stampAvailables == 0) {
                    client.showMsgBoxWarning("No existen timbres disponibles.");
                }
                else {
                    if (needSign && cfdsAux.size() > stampAvailables) {
                        signedSent = client.showMsgBoxConfirm("Timbres insuficientes:\n -Solo existen '" + stampAvailables + "' timbres disponibles.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
                    }

                    if (existsCfdiEmitInconsist(client, cfdsValidate)) {
                        if (signedSent) {
                            dialogResult = new SDialogResult((SClient) client, "Resultados de timbrado y enviado", SCfdConsts.PROC_REQ_STAMP_AND_SND);

                            dialogResult.setFormParams(client, cfdsAux, null, stampAvailables, null, needSign, subtypeCfd, SModSysConsts.TRNU_TP_DPS_ANN_NA);
                            dialogResult.setVisible(true);
                        }
                    }
                }
            }
        }

        return signedSent;
    }

    public static boolean cancelCfdi(final SClientInterface client, final SDataCfd cfd, final int subtypeCfd, final Date cancellationDate, boolean validateStamp, int tpDpsAnn) throws Exception {
        return cancelCfdi(client, cfd, subtypeCfd, cancellationDate, validateStamp, true, tpDpsAnn);
    }

    public static boolean cancelCfdi(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int subtypeCfd, final Date cancellationDate, boolean validateStamp, int tpDpsAnn) throws Exception {
        boolean cancel = false;
        int stampAvailables = 0;
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
                stampAvailables = getStampsAvailable(client, cfdsAux.get(0).getFkCfdTypeId(), cfdsAux.get(0).getTimestamp(), 0);
                needSign = isNeedStamps(client, cfdsAux.get(0), SDbConsts.ACTION_SAVE, 0);

                if (needSign && stampAvailables == 0) {
                    client.showMsgBoxWarning("No existen timbres disponibles.");
                }
                else {
                    if (needSign && cfdsAux.size() > stampAvailables) {
                        cancel = client.showMsgBoxConfirm("Timbres insuficientes:\n -Solo existen '" + stampAvailables + "' timbres disponibles.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
                    }

                    if (cancel && client.showMsgBoxConfirm("La anulación de un CFDI no puede revertirse.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        dialogResult = new SDialogResult((SClient) client, "Resultados de anulación", SCfdConsts.PROC_REQ_ANNUL);

                        dialogResult.setFormParams(client, cfdsAux, null, 0, cancellationDate, validateStamp, subtypeCfd, tpDpsAnn);
                        dialogResult.setVisible(true);
                    }
                }
            }
        }

        return cancel;
    }
    
    public static boolean cancelAndSendCfdi(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int subtypeCfd, final Date cancellationDate, boolean validateStamp, int tpDpsAnn) throws Exception {
        boolean cancel = false;
        int stampAvailables = 0;
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
                stampAvailables = getStampsAvailable(client, cfdsAux.get(0).getFkCfdTypeId(), cfdsAux.get(0).getTimestamp(), 0);
                needSign = isNeedStamps(client, cfdsAux.get(0), SDbConsts.ACTION_SAVE, 0);

                if (needSign && stampAvailables == 0) {
                    client.showMsgBoxWarning("No existen timbres disponibles.");
                }
                else {
                    if (needSign && cfdsAux.size() > stampAvailables) {
                        cancel = client.showMsgBoxConfirm("Timbres insuficientes:\n -Solo existen '" + stampAvailables + "' timbres disponibles.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
                    }

                    if (cancel && client.showMsgBoxConfirm("La anulación de un CFDI no puede revertirse.\n " + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        dialogResult = new SDialogResult((SClient) client, "Resultados de anulación y envío", SCfdConsts.PROC_REQ_ANNUL_AND_SND);

                        dialogResult.setFormParams(client, cfdsAux, null, 0, cancellationDate, validateStamp, subtypeCfd, tpDpsAnn);
                        dialogResult.setVisible(true);
                    }
                }
            }
        }

        return cancel;
    }

    public static void printCfd(final SClientInterface client, final int typeCfd, final SDataCfd cfd, final int subtypeCfd) throws Exception {
        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else {
            printCfd(client, typeCfd, cfd, SDataConstantsPrint.PRINT_MODE_VIEWER, subtypeCfd, false);
        }
    }

    public static void printCfd(final SClientInterface client, final ArrayList<SDataCfd> cfds, int copies, final int subtypeCfd) throws Exception {
        ArrayList<SDataCfd> cfdsAux = null;
        SDialogResult dialogResult = null;

        cfdsAux = new ArrayList<SDataCfd>();

        for(SDataCfd cfd : cfds) {
            if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                cfdsAux.add(cfd);
            }
        }

        if (cfdsAux.isEmpty()) {
            client.showMsgBoxInformation("No existen documentos para imprimir.");
        }
        else {
            if (client.showMsgBoxConfirm("¿Está seguro que desea imprimir " + cfdsAux.size() + " documentos?") == JOptionPane.YES_OPTION) {
                dialogResult = new SDialogResult((SClient) client, "Resultados de impresión", SCfdConsts.PROC_PRT_DOC);

                dialogResult.setFormParams(client, cfdsAux, null, 0, null, false, subtypeCfd, SModSysConsts.TRNU_TP_DPS_ANN_NA);
                dialogResult.setNumberCopies(copies);
                dialogResult.setVisible(true);
            }
        }
    }

    public static void printAcknowledgmentCancellationCfd(final SClientInterface client, final SDataCfd cfd, final int subtypeCfd) throws Exception {
        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else {
            printAcknowledgmentCancellationCfd(client, cfd, SDataConstantsPrint.PRINT_MODE_VIEWER, subtypeCfd);
        }
    }

    public static void printAcknowledgmentCancellationCfd(final SClientInterface client, final ArrayList<SDataCfd> cfds, final int subtypeCfd) throws Exception {
        ArrayList<SDataCfd> cfdsAux = null;
        SDialogResult dialogResult = null;

        cfdsAux = new ArrayList<SDataCfd>();

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
                dialogResult = new SDialogResult((SClient) client, "Resultados de impresión de acuses de cancelación", SCfdConsts.PROC_PRT_ACK_ANNUL);

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

    public static void getXmlCfd(final SClientInterface client, final SDataCfd cfd) throws Exception {
        File file = null;

        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else {
            if (canObtainXml(client, cfd)) {
                client.getFileChooser().setSelectedFile(new File(cfd.getDocXmlName()));
                if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
                    file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());
                    writeXmlToDisk(file, cfd, SDataConstantsSys.TRNS_ST_DPS_EMITED);
                    client.showMsgBoxInformation(SLibConstants.MSG_INF_FILE_CREATE + file.getAbsolutePath());
                }
            }
        }
    }
    
    public static void writeXmlToDisk(final File file, final SDataCfd cfd, final int typeXml) throws Exception {
        BufferedWriter bw = null;
        
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
        bw.write(typeXml == SDataConstantsSys.TRNS_ST_DPS_EMITED ? cfd.getDocXml() : cfd.getAcknowledgmentCancellation());
        bw.close();
    }
    
    public static void getXmlCfds(final SClientInterface client, final ArrayList<SDataCfd> cfds) throws Exception {
        File file = null;
        File fileAux = null;
        ArrayList<SDataCfd> cfdsAux = null;

        cfdsAux = new ArrayList<SDataCfd>();

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
                fileAux = client.getFileChooser().getSelectedFile();
                client.getFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
                for(SDataCfd cfd : cfdsAux) {
                    if (canObtainXml(client, cfd)) {
                        client.getFileChooser().setSelectedFile(new File(fileAux, cfd.getDocXmlName()));
                        file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());
                        writeXmlToDisk(file, cfd, SDataConstantsSys.TRNS_ST_DPS_EMITED);
                    }
                }
                client.showMsgBoxInformation("Los archivos fueron creados.");
            }
            client.getFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
        }
    }

    public static void getAcknowledgmentCancellationCfd(final SClientInterface client, final SDataCfd cfd) throws Exception {
        File file = null;

        if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
        }
        else {
            if (canObtainAcknowledgmentCancellation(client, cfd)) {
                client.getFileChooser().setSelectedFile(new File(cfd.getDocXmlName().replaceAll(".xml", "_CANCELA_RESP") + ".xml"));
                if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
                    file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());
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
        File file = null;
        File fileAux = null;
        ArrayList<SDataCfd> cfdsAux = null;

        cfdsAux = new ArrayList<SDataCfd>();

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
                fileAux = client.getFileChooser().getSelectedFile();
                client.getFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
                for(SDataCfd cfd : cfdsAux) {
                    if (canObtainAcknowledgmentCancellation(client, cfd)) {
                        client.getFileChooser().setSelectedFile(new File(fileAux, cfd.getDocXmlName()));
                        file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());
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
        ArrayList<SDataCfd> cfdsAux = null;
        SDialogResult dialogResult = null;

        cfdsAux = new ArrayList<SDataCfd>();

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
                dialogResult = new SDialogResult((SClient) client, "Resultados de envío", SCfdConsts.PROC_SND_DOC);

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
        return getPayrollCfds(client, typeCfd, payrollKey, SLibConsts.UNDEFINED);
    }
    
    public static ArrayList<SDataCfd> getPayrollCfds(final SClientInterface client, final int typeCfd, final int[] payrollKey, int orderBy) throws Exception {
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
                
                if (orderBy == SUtilConsts.PER_BPR) {
                    sqlInner += "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                            + "bp.id_bp = pei.id_emp ";
                }
                else if (orderBy == SUtilConsts.PER_REF) {
                    sqlInner += "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                                + "bp.id_bp = pei.id_emp "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pe ON "
                                + "c.fid_pay_rcp_pay_n = pe.id_pay AND c.fid_pay_rcp_emp_n = pe.id_emp "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON "
                                + "dep.id_dep = pe.fk_dep ";
                }
                sqlWhere = "WHERE NOT (c.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " AND c.b_con = 0) AND c.fid_pay_rcp_pay_n = " + payrollKey[0] + 
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

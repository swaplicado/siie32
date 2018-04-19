/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import cfd.DCfd;
import cfd.DCfdUtils;
import erp.cfd.SCfdConsts;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.mod.trn.db.STrnUtils;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.imageio.ImageIO;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.xml.SXmlUtils;

/* IMPORTANT:
 * Every single change made to the definition of this class' table must be updated also in the following classes:
 * - erp.mod.hrs.db.SDbFormerPayrollImport
 * - erp.mtrn.data.SCfdUtils
 * All of them execute raw SQL insertions.
 */

/**
 *
 * @author Juan Barajas, Claudio Peña, Sergio Flores
 */
public class SDataCfd extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    public static final int FIELD_ACC_WS = 1;
    public static final int FIELD_ACC_XML_STO = 4;
    public static final int FIELD_ACC_PDF_STO = 5;
    public static final int FIELD_ACC_USR = 6;
    public static final int FIELD_ACK_DVY = 7;
    public static final int FIELD_MSJ_DVY = 8;
    public static final int FIELD_TP_XML_DVY = 9;
    public static final int FIELD_ST_XML_DVY = 10;
    public static final int FIELD_USR_DVY = 11;
    public static final int FIELD_B_CON = 12;

    private final static int DATA_TYPE_TEXT = 1;
    private final static int DATA_TYPE_NUMBER = 2;
    private final static int DATA_TYPE_DATE = 3;
    
    protected int mnPkCfdId;
    protected java.lang.String msSeries;
    protected int mnNumber;
    protected java.util.Date mtTimestamp;
    protected java.lang.String msCertNumber;
    protected java.lang.String msStringSigned;
    protected java.lang.String msSignature;
    protected java.lang.String msDocXml;
    protected java.lang.String msDocXmlName;
    protected java.lang.String msDocXmlRfcEmi;
    protected java.lang.String msDocXmlRfcRec;
    protected double mdDocXmlTot;
    protected java.lang.String msDocXmlMon;
    protected double mdDocXmlTc;
    protected java.util.Date mtDocXmlSign_n;
    protected java.lang.String msUuid;
    protected byte[] moQrCode_n;
    protected java.lang.String msAcknowledgmentCancellationXml;
    protected java.sql.Blob moAcknowledgmentCancellationPdf_n;
    protected java.lang.String msAcknowledgmentDelivery;
    protected java.lang.String msMessageDelivery;
    protected boolean mbIsProcessingWebService;
    protected boolean mbIsProcessingStorageXml;
    protected boolean mbIsProcessingStoragePdf;
    protected boolean mbIsConsistent;
    protected int mnFkCfdTypeId;
    protected int mnFkXmlTypeId;
    protected int mnFkXmlStatusId;
    protected int mnFkXmlDeliveryTypeId;
    protected int mnFkXmlDeliveryStatusId;
    protected int mnFkCompanyBranchId_n;
    protected int mnFkDpsYearId_n;
    protected int mnFkDpsDocId_n;
    protected int mnFkRecordYearId_n;
    protected int mnFkRecordPeriodId_n;
    protected int mnFkRecordBookkeepingCenterId_n;
    protected java.lang.String msFkRecordRecordTypeId_n;
    protected int mnFkRecordNumberId_n;
    protected int mnFkRecordEntryId_n;
    protected int mnFkPayrollPayrollId_n;
    protected int mnFkPayrollEmployeeId_n;
    protected int mnFkPayrollBizPartnerId_n;
    protected int mnFkPayrollReceiptPayrollId_n;
    protected int mnFkPayrollReceiptEmployeeId_n;
    protected int mnFkPayrollReceiptIssueId_n;
    protected int mnFkUserProcessingId;
    protected int mnFkUserDeliveryId;
    protected java.util.Date mtUserProcessingTs;
    protected java.util.Date mtUserDeliveryTs;
    
    protected boolean mbGenerateQrCode;
    protected java.lang.String msAuxRfcEmisor;
    protected java.lang.String msAuxRfcReceptor;
    protected double mdAuxTotalCy;
    protected boolean mbAuxIsSign;
    protected boolean mbAuxIsValidate;
    
    protected byte[] mayExtraPrivateDocXml_ns;

    public String getCfdNumber() {
        String numberSer = "";
        String number = "";
        
        try {
            Document doc = SXmlUtils.parseDocument(msDocXml);
            Node node = null;
            NamedNodeMap namedNodeMap = null;

            node = SXmlUtils.extractElements(doc, mnFkXmlTypeId == SDataConstantsSys.TRNS_TP_XML_CFD ? "Comprobante" : "cfdi:Comprobante").item(0);
            namedNodeMap = node.getAttributes();

            numberSer = SXmlUtils.extractAttributeValue(namedNodeMap, "serie", false);
            number = SXmlUtils.extractAttributeValue(namedNodeMap, "folio", false);
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
        }
        return STrnUtils.formatDocNumber(numberSer, number);
    }
    
    private void parseCfdiAttributes(final Connection connection, final String xml) throws java.lang.Exception {
        boolean isCfdi = false;
        String sql = "";
        String xmlSafe = SLibUtils.textToSql(xml);
        ResultSet resultSet = null;
        
        msDocXmlRfcEmi = "";
        msDocXmlRfcRec = "";
        mdDocXmlTot = 0;
        msDocXmlMon = "";
        mdDocXmlTc = 0;
        mtDocXmlSign_n = null;
        msUuid = "";
        
        // is CFDI:
        
        sql = "SELECT " +
                "erp.f_get_xml_atr('cfdi:Emisor', 'rfc=', '" + xmlSafe + "', " + DATA_TYPE_TEXT + ") AS _xml_emisor_rfc, " +
                "erp.f_get_xml_atr('cfdi:Receptor', 'rfc=', '" + xmlSafe + "', " + DATA_TYPE_TEXT + ") AS _xml_receptor_rfc, " +
                "erp.f_get_xml_atr('cfdi:Comprobante', 'Total=', '" + xmlSafe + "', " + DATA_TYPE_NUMBER + ") AS _xml_total, " +
                "erp.f_get_xml_atr('cfdi:Comprobante', 'TipoCambio=', '" + xmlSafe + "', " + DATA_TYPE_NUMBER + ") AS _xml_tc, " +
                "erp.f_get_xml_atr('cfdi:Comprobante', 'Moneda=', '" + xmlSafe + "', " + DATA_TYPE_TEXT + ") AS _xml_moneda, " +
                "CAST(REPLACE(erp.f_get_xml_atr('cfdi:Complemento', 'FechaTimbrado=', '" + xmlSafe + "', " + DATA_TYPE_DATE + "), 'T', ' ') AS DATETIME) AS _xml_timbrado, " +
                "erp.f_get_xml_atr('cfdi:Complemento', 'UUID=', '" + xmlSafe + "', " + DATA_TYPE_TEXT + ") AS _xml_uuid ";

        resultSet = connection.createStatement().executeQuery(sql);
                
        if (resultSet.next()) {
            msDocXmlRfcEmi = DCfdUtils.cleanXmlEntities(resultSet.getString("_xml_emisor_rfc"));
            msDocXmlRfcRec = DCfdUtils.cleanXmlEntities(resultSet.getString("_xml_receptor_rfc"));
            mdDocXmlTot = resultSet.getDouble("_xml_total");
            msDocXmlMon = resultSet.getString("_xml_moneda");
            mdDocXmlTc = resultSet.getDouble("_xml_tc");
            mtDocXmlSign_n = resultSet.getTimestamp("_xml_timbrado");
            msUuid = resultSet.getString("_xml_uuid");
            
            isCfdi = !msDocXmlRfcEmi.isEmpty();
        }

        // is CFD:
        
        if (!isCfdi) {
            sql = "SELECT " +
                "erp.f_get_xml_atr('Emisor', 'rfc=', '" + xmlSafe + "', " + DATA_TYPE_TEXT + ") AS _xml_emisor_rfc, " +
                "erp.f_get_xml_atr('Receptor', 'rfc=', '" + xmlSafe + "', " + DATA_TYPE_TEXT + ") AS _xml_receptor_rfc, " +
                "erp.f_get_xml_atr('Comprobante', 'Total=', '" + xmlSafe + "', " + DATA_TYPE_NUMBER + ") AS _xml_total, " +
                "erp.f_get_xml_atr('Comprobante', 'TipoCambio=', '" + xmlSafe + "', " + DATA_TYPE_NUMBER + ") AS _xml_tc, " +
                "erp.f_get_xml_atr('Comprobante', 'Moneda=', '" + xmlSafe + "', " + DATA_TYPE_TEXT + ") AS _xml_moneda, " +
                "NULL AS _xml_timbrado, " +
                "'' AS _xml_uuid ";

            resultSet = connection.createStatement().executeQuery(sql);
            
            if (resultSet.next()) {
                msDocXmlRfcEmi = DCfdUtils.cleanXmlEntities(resultSet.getString("_xml_emisor_rfc"));
                msDocXmlRfcRec = DCfdUtils.cleanXmlEntities(resultSet.getString("_xml_receptor_rfc"));
                mdDocXmlTot = resultSet.getDouble("_xml_total");
                msDocXmlMon = resultSet.getString("_xml_moneda");
                mdDocXmlTc = resultSet.getDouble("_xml_tc");
                mtDocXmlSign_n = resultSet.getTimestamp("_xml_timbrado");
                msUuid = resultSet.getString("_xml_uuid");
            }
        }
    }
    
    private boolean testDeletion(java.lang.String psMsg, int action) throws java.lang.Exception {
        String sMsg = psMsg;
        
        if (mnFkXmlStatusId == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
            msDbmsError = sMsg + "¡El documento está anulado!";
            throw new Exception(msDbmsError);
        }
        
        switch (action) {
            case SDbConsts.ACTION_DELETE:
                if (mnFkXmlStatusId == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                    msDbmsError = sMsg + "¡El documento está timbrado!";
                    throw new Exception(msDbmsError);
                }
                break;
            case SDbConsts.ACTION_ANNUL:
                if (mnFkXmlStatusId != SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                    msDbmsError = sMsg + "¡El documento no está timbrado!";
                    throw new Exception(msDbmsError);
                }
                break;
            default:
        }
        
        if (!mbAuxIsValidate) {
            if (mbIsProcessingWebService) {
                msDbmsError = sMsg + "¡" + SCfdConsts.ERR_MSG_PROCESSING_WEB_SERVICE + "!";
                throw new Exception(msDbmsError);
            }
            if (action != SDbConsts.ACTION_ANNUL && mbIsProcessingStorageXml) {
                msDbmsError = sMsg + "¡" + SCfdConsts.ERR_MSG_PROCESSING_XML_STORAGE + "!";
                throw new Exception(msDbmsError);
            }
            if (action != SDbConsts.ACTION_ANNUL && mbIsProcessingStoragePdf) {
                msDbmsError = sMsg + "¡" + SCfdConsts.ERR_MSG_PROCESSING_PDF_STORAGE + "!";
                throw new Exception(msDbmsError);
            }
        }
        
        return true;
    }
    
    public SDataCfd() {
        super(SModConsts.TRN_CFD);
        reset();
    }

    public void setPkCfdId(int n) { mnPkCfdId = n; }
    public void setSeries(java.lang.String s) { msSeries = s; }
    public void setNumber(int n) { mnNumber = n; }
    public void setTimestamp(java.util.Date t) { mtTimestamp = t; }
    public void setCertNumber(java.lang.String s) { msCertNumber = s; }
    public void setStringSigned(java.lang.String s) { msStringSigned = s; }
    public void setSignature(java.lang.String s) { msSignature = s; }
    public void setDocXml(java.lang.String s) { msDocXml = s; }
    public void setDocXmlName(java.lang.String s) { msDocXmlName = s; }
    public void setDocXmlRfcEmi(java.lang.String s) { msDocXmlRfcEmi = s; }
    public void setDocXmlRfcRec(java.lang.String s) { msDocXmlRfcRec = s; }
    public void setDocXmlTot(double d) { mdDocXmlTot = d; }
    public void setDocXmlMon(java.lang.String s) { msDocXmlMon = s; }
    public void setDocXmlTc(double d) { mdDocXmlTc = d; }
    public void setDocXmlSign_n(java.util.Date t) { mtDocXmlSign_n = t; }
    public void setUuid(java.lang.String s) { msUuid = s; }
    public void setQrCode_n(byte[] o) { moQrCode_n = o; }
    public void setAcknowledgmentCancellationXml(java.lang.String s) { msAcknowledgmentCancellationXml = s; }
    public void setAcknowledgmentCancellationPdf_n(java.sql.Blob o) { moAcknowledgmentCancellationPdf_n = o; }
    public void setAcknowledgmentDelivery(java.lang.String s) { msAcknowledgmentDelivery = s; }
    public void setMessageDelivery(java.lang.String s) { msMessageDelivery = s; }
    public void setIsProcessingWebService(boolean b) { mbIsProcessingWebService = b; }
    public void setIsProcessingStorageXml(boolean b) { mbIsProcessingStorageXml = b; }
    public void setIsProcessingStoragePdf(boolean b) { mbIsProcessingStoragePdf = b; }
    public void setIsConsistent(boolean b) { mbIsConsistent = b; }
    public void setFkCfdTypeId(int n) { mnFkCfdTypeId = n; }
    public void setFkXmlTypeId(int n) { mnFkXmlTypeId = n; }
    public void setFkXmlStatusId(int n) { mnFkXmlStatusId = n; }
    public void setFkXmlDeliveryTypeId(int n) { mnFkXmlDeliveryTypeId = n; }
    public void setFkXmlDeliveryStatusId(int n) { mnFkXmlDeliveryStatusId = n; }
    public void setFkCompanyBranchId_n(int n) { mnFkCompanyBranchId_n = n; }
    public void setFkDpsYearId_n(int n) { mnFkDpsYearId_n = n; }
    public void setFkDpsDocId_n(int n) { mnFkDpsDocId_n = n; }
    public void setFkRecordYearId_n(int n) { mnFkRecordYearId_n = n; }
    public void setFkRecordPeriodId_n(int n) { mnFkRecordPeriodId_n = n; }
    public void setFkRecordBookkeepingCenterId_n(int n) { mnFkRecordBookkeepingCenterId_n = n; }
    public void setFkRecordRecordTypeId_n(java.lang.String s) { msFkRecordRecordTypeId_n = s; }
    public void setFkRecordNumberId_n(int n) { mnFkRecordNumberId_n = n; }
    public void setFkRecordEntryId_n(int n) { mnFkRecordEntryId_n = n; }
    public void setFkPayrollPayrollId_n(int n) { mnFkPayrollPayrollId_n = n; }
    public void setFkPayrollEmployeeId_n(int n) { mnFkPayrollEmployeeId_n = n; }
    public void setFkPayrollBizPartnerId_n(int n) { mnFkPayrollBizPartnerId_n = n; }
    public void setFkPayrollReceiptPayrollId_n(int n) { mnFkPayrollReceiptPayrollId_n = n; }
    public void setFkPayrollReceiptEmployeeId_n(int n) { mnFkPayrollReceiptEmployeeId_n = n; }
    public void setFkPayrollReceiptIssueId_n(int n) { mnFkPayrollReceiptIssueId_n = n; }
    public void setFkUserProcessingId(int n) { mnFkUserProcessingId = n; }
    public void setFkUserDeliveryId(int n) { mnFkUserDeliveryId = n; }
    public void setUserProcessingTs(java.util.Date t) { mtUserProcessingTs = t; }
    public void setUserDeliveryTs(java.util.Date t) { mtUserDeliveryTs = t; }

    public void setGenerateQrCode(boolean b) { mbGenerateQrCode = b; }
    public void setAuxRfcEmisor(java.lang.String s) { msAuxRfcEmisor = s; }
    public void setAuxRfcReceptor(java.lang.String s) { msAuxRfcReceptor = s; }
    public void setAuxTotalCy(double d) { mdAuxTotalCy = d; }
    public void setAuxIsSign(boolean b) { mbAuxIsSign = b; }
    public void setAuxIsValidate(boolean b) { mbAuxIsValidate = b; }    

    public int getPkCfdId() { return mnPkCfdId; }
    public java.lang.String getSeries() { return msSeries; }
    public int getNumber() { return mnNumber; }
    public java.util.Date getTimestamp() { return mtTimestamp; }
    public java.lang.String getCertNumber() { return msCertNumber; }
    public java.lang.String getStringSigned() { return msStringSigned; }
    public java.lang.String getSignature() { return msSignature; }
    public java.lang.String getDocXml() { return msDocXml; }
    public java.lang.String getDocXmlName() { return msDocXmlName; }
    public java.lang.String getDocXmlRfcEmi() { return msDocXmlRfcEmi; }
    public java.lang.String getDocXmlRfcRec() { return msDocXmlRfcRec; }
    public double getDocXmlTot() { return mdDocXmlTot; }
    public java.lang.String getDocXmlMon() { return msDocXmlMon; }
    public double getDocXmlTc() { return mdDocXmlTc; }
    public java.util.Date getDocXmlSign_n() { return mtDocXmlSign_n; }
    public java.lang.String getUuid() { return msUuid; }
    public byte[] getQrCode_n() { return moQrCode_n; }
    public java.lang.String getAcknowledgmentCancellation() { return msAcknowledgmentCancellationXml; }
    public java.sql.Blob getAcknowledgmentCancellationPdf_n() { return moAcknowledgmentCancellationPdf_n; }
    public java.lang.String getAcknowledgmentDelivery() { return msAcknowledgmentDelivery; }
    public java.lang.String getMessageDelivery() { return msMessageDelivery; }
    public boolean getIsProcessingWebService() { return mbIsProcessingWebService; }
    public boolean getIsProcessingStorageXml() { return mbIsProcessingStorageXml; }
    public boolean getIsProcessingStoragePdf() { return mbIsProcessingStoragePdf; }
    public boolean getIsConsistent() { return mbIsConsistent; }
    public int getFkCfdTypeId() { return mnFkCfdTypeId; }
    public int getFkXmlTypeId() { return mnFkXmlTypeId; }
    public int getFkXmlStatusId() { return mnFkXmlStatusId; }
    public int getFkXmlDeliveryTypeId() { return mnFkXmlDeliveryTypeId; }
    public int getFkXmlDeliveryStatusId() { return mnFkXmlDeliveryStatusId; }
    public int getFkCompanyBranchId_n() { return mnFkCompanyBranchId_n; }
    public int getFkDpsYearId_n() { return mnFkDpsYearId_n; }
    public int getFkDpsDocId_n() { return mnFkDpsDocId_n; }
    public int getFkRecordYearId_n() { return mnFkRecordYearId_n; }
    public int getFkRecordPeriodId_n() { return mnFkRecordPeriodId_n; }
    public int getFkRecordBookkeepingCenterId_n() { return mnFkRecordBookkeepingCenterId_n; }
    public java.lang.String getFkRecordRecordTypeId_n() { return msFkRecordRecordTypeId_n; }
    public int getFkRecordNumberId_n() { return mnFkRecordNumberId_n; }
    public int getFkRecordEntryId_n() { return mnFkRecordEntryId_n; }
    public int getFkPayrollPayrollId_n() { return mnFkPayrollPayrollId_n; }
    public int getFkPayrollEmployeeId_n() { return mnFkPayrollEmployeeId_n; }
    public int getFkPayrollBizPartnerId_n() { return mnFkPayrollBizPartnerId_n; }
    public int getFkPayrollReceiptPayrollId_n() { return mnFkPayrollReceiptPayrollId_n; }
    public int getFkPayrollReceiptEmployeeId_n() { return mnFkPayrollReceiptEmployeeId_n; }
    public int getFkPayrollReceiptIssueId_n() { return mnFkPayrollReceiptIssueId_n; }
    public int getFkUserProcessingId() { return mnFkUserProcessingId; }
    public int getFkUserDeliveryId() { return mnFkUserDeliveryId; }
    public java.util.Date getUserProcessingTs() { return mtUserProcessingTs; }
    public java.util.Date getUserDeliveryTs() { return mtUserDeliveryTs; }

    public boolean getGenerateQrCode() { return mbGenerateQrCode; }
    public java.lang.String getAuxRfcEmisor() { return msAuxRfcEmisor; }
    public java.lang.String getAuxRfcReceptor() { return msAuxRfcReceptor; }
    public double getAuxTotalCy() { return mdAuxTotalCy; }
    public boolean getAuxIsSign() { return mbAuxIsSign; }
    public boolean getAuxIsValidate() { return mbAuxIsValidate; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkCfdId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkCfdId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkCfdId = 0;
        msSeries = "";
        mnNumber = 0;
        mtTimestamp = null;
        msCertNumber = "";
        msStringSigned = "";
        msSignature = "";
        msDocXml = "";
        msDocXmlName = "";
        msDocXmlRfcEmi = "";
        msDocXmlRfcRec = "";
        mdDocXmlTot = 0;
        msDocXmlMon = "";
        mdDocXmlTc = 0;
        mtDocXmlSign_n = null;
        msUuid = "";
        moQrCode_n = null;
        msAcknowledgmentCancellationXml = "";
        moAcknowledgmentCancellationPdf_n = null;
        msAcknowledgmentDelivery = "";
        msMessageDelivery = "";
        mbIsProcessingWebService = false;
        mbIsProcessingStorageXml = false;
        mbIsProcessingStoragePdf = false;
        mbIsConsistent = false;
        mnFkCfdTypeId = 0;
        mnFkXmlTypeId = 0;
        mnFkXmlStatusId = 0;
        mnFkXmlDeliveryTypeId = 0;
        mnFkXmlDeliveryStatusId = 0;
        mnFkCompanyBranchId_n = 0;
        mnFkDpsYearId_n = 0;
        mnFkDpsDocId_n = 0;
        mnFkRecordYearId_n = 0;
        mnFkRecordPeriodId_n = 0;
        mnFkRecordBookkeepingCenterId_n = 0;
        msFkRecordRecordTypeId_n = "";
        mnFkRecordNumberId_n = 0;
        mnFkRecordEntryId_n = 0;
        mnFkPayrollPayrollId_n = 0;
        mnFkPayrollEmployeeId_n = 0;
        mnFkPayrollBizPartnerId_n = 0;
        mnFkPayrollReceiptPayrollId_n = 0;
        mnFkPayrollReceiptEmployeeId_n = 0;
        mnFkPayrollReceiptIssueId_n = 0;
        mnFkUserProcessingId = 0;
        mnFkUserDeliveryId = 0;
        mtUserProcessingTs = null;
        mtUserDeliveryTs = null;

        mbGenerateQrCode = false;
        msAuxRfcEmisor = "";
        msAuxRfcReceptor = "";
        mdAuxTotalCy = 0;
        mbAuxIsSign = false;
        mbAuxIsValidate = false;
        
        mayExtraPrivateDocXml_ns = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_cfd WHERE id_cfd = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkCfdId = resultSet.getInt("id_cfd");
                msSeries = resultSet.getString("ser");
                mnNumber = resultSet.getInt("num");
                mtTimestamp = resultSet.getTimestamp("ts");
                msCertNumber = resultSet.getString("cert_num");
                msStringSigned = resultSet.getString("str_signed");
                msSignature = resultSet.getString("signature");
                msDocXml = resultSet.getString("doc_xml");
                msDocXmlName = resultSet.getString("doc_xml_name");
                msDocXmlRfcEmi = resultSet.getString("xml_rfc_emi");
                msDocXmlRfcRec = resultSet.getString("xml_rfc_rec");
                mdDocXmlTot = resultSet.getDouble("xml_tot");
                msDocXmlMon = resultSet.getString("xml_mon");
                mdDocXmlTc = resultSet.getDouble("xml_tc");
                mtDocXmlSign_n = resultSet.getTimestamp("xml_sign_n");
                msUuid = resultSet.getString("uuid");
                //moQrCode_n = resultSet.getBlob("qrc_n"); it's cannot read a object blob (2014-03-10, jbarajas)
                msAcknowledgmentCancellationXml = resultSet.getString("ack_can_xml");
                //moAcknowledgmentCancellationPdf_n = resultSet.getBlob("ack_can_pdf_n"); it's cannot read a object blob (2014-09-01, jbarajas)
                msAcknowledgmentDelivery = resultSet.getString("ack_dvy");
                msMessageDelivery = resultSet.getString("msg_dvy");
                mbIsProcessingWebService = resultSet.getBoolean("b_prc_ws");
                mbIsProcessingStorageXml = resultSet.getBoolean("b_prc_sto_xml");
                mbIsProcessingStoragePdf = resultSet.getBoolean("b_prc_sto_pdf");
                mbIsConsistent = resultSet.getBoolean("b_con");
                mnFkCfdTypeId = resultSet.getInt("fid_tp_cfd");
                mnFkXmlTypeId = resultSet.getInt("fid_tp_xml");
                mnFkXmlStatusId = resultSet.getInt("fid_st_xml");
                mnFkXmlDeliveryTypeId = resultSet.getInt("fid_tp_xml_dvy");
                mnFkXmlDeliveryStatusId = resultSet.getInt("fid_st_xml_dvy");
                mnFkCompanyBranchId_n = resultSet.getInt("fid_cob_n");
                mnFkDpsYearId_n = resultSet.getInt("fid_dps_year_n");
                mnFkDpsDocId_n = resultSet.getInt("fid_dps_doc_n");
                mnFkRecordYearId_n = resultSet.getInt("fid_rec_year_n");
                mnFkRecordPeriodId_n = resultSet.getInt("fid_rec_per_n");
                mnFkRecordBookkeepingCenterId_n = resultSet.getInt("fid_rec_bkc_n");
                msFkRecordRecordTypeId_n = resultSet.getString("fid_rec_tp_rec_n");
                mnFkRecordNumberId_n = resultSet.getInt("fid_rec_num_n");
                mnFkRecordEntryId_n = resultSet.getInt("fid_rec_ety_n");
                mnFkPayrollPayrollId_n = resultSet.getInt("fid_pay_pay_n");
                mnFkPayrollEmployeeId_n = resultSet.getInt("fid_pay_emp_n");
                mnFkPayrollBizPartnerId_n = resultSet.getInt("fid_pay_bpr_n");
                mnFkPayrollReceiptPayrollId_n = resultSet.getInt("fid_pay_rcp_pay_n");
                mnFkPayrollReceiptEmployeeId_n = resultSet.getInt("fid_pay_rcp_emp_n");
                mnFkPayrollReceiptIssueId_n = resultSet.getInt("fid_pay_rcp_iss_n");
                mnFkUserProcessingId = resultSet.getInt("fid_usr_prc");
                mnFkUserDeliveryId = resultSet.getInt("fid_usr_dvy");
                mtUserProcessingTs = resultSet.getTimestamp("ts_prc");
                mtUserDeliveryTs = resultSet.getTimestamp("ts_dvy");
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int index = 1;
        final int LENGTH_CURRENCY = 15;
        boolean bIsUpd = false;
        String sql = "";
        BufferedImage bufferedImage = null;
        ResultSet resultSet = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            statement = connection.createStatement();
            
            if (mnPkCfdId == SLibConsts.UNDEFINED) {
                // obtain new ID for CFD:
                sql = "SELECT COALESCE(MAX(id_cfd), 0) + 1 FROM trn_cfd ";
                resultSet = statement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
                }
                else {
                    mnPkCfdId = resultSet.getInt(1);
                }

                if (!msSeries.isEmpty() && mnNumber == 0) {
                    // obtain new Number for actual Series of CFD:
                    sql = "SELECT COALESCE(MAX(num), 0) + 1 FROM trn_cfd WHERE ser = '" + msSeries + "' ";
                    resultSet = statement.executeQuery(sql);
                    if (!resultSet.next()) {
                        throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
                    }
                    else {
                        mnNumber = resultSet.getInt(1);
                    }
                }

                sql = "INSERT INTO trn_cfd (id_cfd, ser, num, " +
                        "ts, cert_num, str_signed, signature, doc_xml, doc_xml_name, xml_rfc_emi, xml_rfc_rec, xml_tot, xml_mon, " +
                        "xml_tc, xml_sign_n, uuid, qrc_n, ack_can_xml, ack_can_pdf_n, ack_dvy, msg_dvy, b_prc_ws, b_prc_sto_xml, " +
                        "b_prc_sto_pdf, b_con, fid_tp_cfd, fid_tp_xml, fid_st_xml, fid_tp_xml_dvy, fid_st_xml_dvy, fid_cob_n, fid_dps_year_n, fid_dps_doc_n, " +
                        "fid_rec_year_n, fid_rec_per_n, fid_rec_bkc_n, fid_rec_tp_rec_n, fid_rec_num_n, fid_rec_ety_n, fid_pay_pay_n, fid_pay_emp_n, fid_pay_bpr_n, fid_pay_rcp_pay_n, fid_pay_rcp_emp_n, " +
                        "fid_pay_rcp_iss_n, fid_usr_prc, fid_usr_dvy, ts_prc, ts_dvy) " +
                        "VALUES (" + mnPkCfdId + ", ?, ?, " +
                        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                        "?, ?, ?, NOW(), NOW())";
            }
            else {
                bIsUpd = true;
                
                sql = "UPDATE trn_cfd SET ser = ?, num = ?, ts = ?, cert_num = ?, str_signed = ?, signature = ?, " +
                        "doc_xml = ?, doc_xml_name = ?, xml_rfc_emi = ?, xml_rfc_rec = ?, xml_tot = ?, xml_mon = ?, xml_tc = ?, xml_sign_n = ?, " +
                        "uuid = ?, " + (mnFkXmlStatusId != SDataConstantsSys.TRNS_ST_DPS_ANNULED ? "qrc_n = ?," : "") + " ack_can_xml = ?, ack_dvy = ?, msg_dvy = ?, b_con = ?, fid_tp_cfd = ?, " +
                        "fid_tp_xml = ?, fid_st_xml = ?, fid_tp_xml_dvy = ?, fid_st_xml_dvy = ?, fid_dps_year_n = ?, fid_dps_doc_n = ?, fid_rec_year_n = ?, fid_rec_per_n = ?, fid_rec_bkc_n = ?, fid_rec_tp_rec_n = ?, fid_rec_num_n = ?, fid_rec_ety_n = ?, fid_pay_pay_n = ?, fid_pay_emp_n = ?, fid_pay_bpr_n = ?, fid_pay_rcp_pay_n = ?, fid_pay_rcp_emp_n = ?, fid_pay_rcp_iss_n = ?, fid_usr_dvy = ?, ts_dvy = NOW() " +
                        "WHERE id_cfd = " + mnPkCfdId + " ";
            }
            
            if (mbGenerateQrCode) {
                bufferedImage = DCfd.createQrCodeBufferedImageCfdi32(msAuxRfcEmisor, msAuxRfcReceptor, mdAuxTotalCy, msUuid);
                moQrCode_n = converterBufferedImageToByteArray(bufferedImage);
            }
            
            parseCfdiAttributes(connection, msDocXml);
            
            preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setTimestamp(index++, new java.sql.Timestamp(mtTimestamp.getTime()));
            preparedStatement.setString(index++, msSeries);
            preparedStatement.setInt(index++, mnNumber);
            preparedStatement.setString(index++, msCertNumber);
            preparedStatement.setString(index++, msStringSigned);
            preparedStatement.setString(index++, msSignature);
            preparedStatement.setString(index++, SLibUtils.textToSql(msDocXml));
            preparedStatement.setString(index++, SLibUtils.textToSql(msDocXmlName));
            preparedStatement.setString(index++, msDocXmlRfcEmi);
            preparedStatement.setString(index++, msDocXmlRfcRec);
            preparedStatement.setDouble(index++, mdDocXmlTot);
            if (msDocXmlMon.length() >= LENGTH_CURRENCY) {
                preparedStatement.setString(index++, msDocXmlMon.substring(0, 14)); //Valid value size
            } 
            else {
                preparedStatement.setString(index++, msDocXmlMon);
            }
            preparedStatement.setDouble(index++, mdDocXmlTc);
            
            if (mtDocXmlSign_n == null) {
                preparedStatement.setNull(index++, java.sql.Types.DATE);
            }
            else {
                preparedStatement.setTimestamp(index++, new java.sql.Timestamp(mtDocXmlSign_n.getTime()));
            }
            
            preparedStatement.setString(index++, msUuid);
            
            if (mnFkXmlTypeId != SDataConstantsSys.TRNS_TP_XML_CFDI_32 && mnFkXmlTypeId != SDataConstantsSys.TRNS_TP_XML_CFDI_33) {
                preparedStatement.setNull(index++, java.sql.Types.BLOB);
            }
            else {
                if (mnFkXmlStatusId != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                    if (moQrCode_n == null) {
                        preparedStatement.setNull(index++, java.sql.Types.BLOB);
                    }
                    else {
                        preparedStatement.setBytes(index++, moQrCode_n);
                    }
                }
            }

            preparedStatement.setString(index++, msAcknowledgmentCancellationXml);
            
            if (!bIsUpd) {
                preparedStatement.setNull(index++, java.sql.Types.BLOB); // it's cannot updated a object blob (2014-09-01, jbarajas)
            }
            
            preparedStatement.setString(index++, msAcknowledgmentDelivery);
            preparedStatement.setString(index++, msMessageDelivery);
            
            if (!bIsUpd) {
                preparedStatement.setBoolean(index++, mbIsProcessingWebService);
                preparedStatement.setBoolean(index++, mbIsProcessingStorageXml);
                preparedStatement.setBoolean(index++, mbIsProcessingStoragePdf);
            }
            
            preparedStatement.setBoolean(index++, mbIsConsistent);
            preparedStatement.setInt(index++, mnFkCfdTypeId);
            preparedStatement.setInt(index++, mnFkXmlTypeId);
            preparedStatement.setInt(index++, mnFkXmlStatusId);
            preparedStatement.setInt(index++, mnFkXmlDeliveryTypeId);
            preparedStatement.setInt(index++, mnFkXmlDeliveryStatusId);
            
            if (mnFkCompanyBranchId_n == SLibConsts.UNDEFINED) {
                preparedStatement.setNull(index++, java.sql.Types.INTEGER);
            }
            else {
                preparedStatement.setInt(index++, mnFkCompanyBranchId_n);
            }
            
            if (mnFkDpsYearId_n == SLibConsts.UNDEFINED) {
                preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
                preparedStatement.setNull(index++, java.sql.Types.INTEGER);
            }
            else {
                preparedStatement.setInt(index++, mnFkDpsYearId_n);
                preparedStatement.setInt(index++, mnFkDpsDocId_n);
            }
            
            if (mnFkRecordYearId_n == SLibConsts.UNDEFINED) {
                preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
                preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
                preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
                preparedStatement.setNull(index++, java.sql.Types.CHAR);
                preparedStatement.setNull(index++, java.sql.Types.INTEGER);
                preparedStatement.setNull(index++, java.sql.Types.INTEGER);
            }
            else {
                preparedStatement.setInt(index++, mnFkRecordYearId_n);
                preparedStatement.setInt(index++, mnFkRecordPeriodId_n);
                preparedStatement.setInt(index++, mnFkRecordBookkeepingCenterId_n);
                preparedStatement.setString(index++, msFkRecordRecordTypeId_n);
                preparedStatement.setInt(index++, mnFkRecordNumberId_n);
                preparedStatement.setInt(index++, mnFkRecordEntryId_n);
            }
            
            if (mnFkPayrollPayrollId_n == SLibConsts.UNDEFINED) {
                preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
                preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
                preparedStatement.setNull(index++, java.sql.Types.INTEGER);
            }
            else {
                preparedStatement.setInt(index++, mnFkPayrollPayrollId_n);
                preparedStatement.setInt(index++, mnFkPayrollEmployeeId_n);
                preparedStatement.setInt(index++, mnFkPayrollBizPartnerId_n);
            }
            
            if (mnFkPayrollReceiptPayrollId_n == SLibConsts.UNDEFINED) {
                preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
                preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
                preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
            }
            else {
                preparedStatement.setInt(index++, mnFkPayrollReceiptPayrollId_n);
                preparedStatement.setInt(index++, mnFkPayrollReceiptEmployeeId_n);
                preparedStatement.setInt(index++, mnFkPayrollReceiptIssueId_n);
            }
            
            if (!bIsUpd) {
                preparedStatement.setInt(index++, mnFkUserProcessingId);
            }
            
            preparedStatement.setInt(index++, mnFkUserDeliveryId);
            
            preparedStatement.execute();
            
            mnDbmsErrorId = 0;
            msDbmsError = "";

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }
    
    @Override
    public java.util.Date getLastDbUpdate() {
        return null;
    }
    
    @Override
    public int canSave(java.sql.Connection connection) {
        if (!mbAuxIsSign) {
            mnLastDbActionResult = super.canSave(connection);
        }
        else {
            mnLastDbActionResult = SLibConstants.UNDEFINED;
            
            try {
                if (testDeletion("No se puede timbrar el documento: ", SDbConsts.ACTION_DELETE)) {
                    mnLastDbActionResult = SLibConstants.DB_CAN_SAVE_YES;
                }
            }
            catch (Exception e) {
                mnLastDbActionResult = SLibConstants.DB_CAN_SAVE_NO;
                if (msDbmsError.isEmpty()) {
                    msDbmsError = SLibConstants.MSG_ERR_DB_REG_CAN_SAVE;
                }
                SLibUtilities.printOutException(this, e);
            }
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public int canAnnul(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            if (testDeletion("No se puede anular el documento: ", SDbConsts.ACTION_ANNUL)) {
                mnLastDbActionResult = SLibConstants.DB_CAN_ANNUL_YES;
            }
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_CAN_ANNUL_NO;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_CAN_ANNUL;
            }
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }
    
    @Override
    public int annul(java.sql.Connection connection) {
        String sSql = "";
        Statement oStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            oStatement = connection.createStatement();
            
            // Set CFD as annuled:

            if (testDeletion("No se puede anular el documento: ", SDbConsts.ACTION_ANNUL)) {
                sSql = "UPDATE trn_cfd SET fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " " +
                        "WHERE id_cfd = " + mnPkCfdId + " ";
                oStatement.execute(sSql);

                mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_OK;
            }
        }
        catch (SQLException exception) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_ERROR;
            SLibUtilities.printOutException(this, exception);
        }
        catch (Exception exception) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_ERROR;
            SLibUtilities.printOutException(this, exception);
        }

        return mnLastDbActionResult;
    }
    
    public boolean hasUuidWhenStamped() {
        return mnFkXmlTypeId == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || mnFkXmlTypeId == SDataConstantsSys.TRNS_TP_XML_CFDI_33;
    }
    
    public boolean isStamped() {
        return !hasUuidWhenStamped() || (hasUuidWhenStamped() && !msUuid.isEmpty());
    }
    
    public void saveField(java.sql.Connection connection, final int[] pk, final int field, final Object value) throws Exception {
        String sSql = "";

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        sSql = "UPDATE trn_cfd SET ";

        switch (field) {
            case FIELD_ACC_WS:
                sSql += "b_prc_ws = " + value + " ";
                break;
            case FIELD_ACC_XML_STO:
                sSql += "b_prc_sto_xml = " + value + " ";
                break;
            case FIELD_ACC_PDF_STO:
                sSql += "b_prc_sto_pdf = " + value + " ";
                break;
            case FIELD_ACC_USR:
                sSql += "fid_usr_prc = " + value + ", ts_prc = NOW() ";
                break;
            case FIELD_ACK_DVY:
                sSql += "ack_dvy = '" + value + "' ";
                break;
            case FIELD_MSJ_DVY:
                sSql += "msg_dvy = '" + value + "' ";
                break;
            case FIELD_TP_XML_DVY:
                sSql += "fid_tp_xml_dvy = " + value + " ";
                break;
            case FIELD_ST_XML_DVY:
                sSql += "fid_st_xml_dvy = " + value + " ";
                break;
            case FIELD_USR_DVY:
                sSql += "fid_usr_dvy = " + value + ", ts_dvy = NOW() ";
                break;
            case FIELD_B_CON:
                sSql += " b_con = " + value + " ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        sSql += "WHERE id_cfd = " + pk[0] + " ";
        
        connection.createStatement().execute(sSql);
        
        mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
    }
    
    public static byte[] converterBufferedImageToByteArray(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", byteArrayOS);

        return  byteArrayOS.toByteArray();
    }
}

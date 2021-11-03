/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import cfd.DCfdUtils;
import erp.SBaseXClient;
import erp.SBaseXUtils;
import erp.SClientUtils;
import erp.cfd.SCfdConsts;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.mod.trn.db.STrnUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
 * @author Juan Barajas, Claudio Peña, Isabel Servín, Sergio Flores
 */
public class SDataCfd extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    public static final int FIELD_PRC_WS = 11;
    public static final int FIELD_PRC_STO_XML = 12;
    public static final int FIELD_PRC_STO_PDF = 13;
    public static final int FIELD_PRC_CON = 14;
    public static final int FIELD_PRC_USR = 15;
    
    public static final int FIELD_DVY_ACK = 21;
    public static final int FIELD_DVY_MSG = 22;
    public static final int FIELD_DVY_TP = 23;
    public static final int FIELD_DVY_ST = 24;
    public static final int FIELD_DVY_USR = 25;

    public static final int FIELD_CAN_ST = 31;
    
    public static final int FIELD_FK_RCP_PAY = 41;

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
    protected java.lang.String msBasexUuid;
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
    protected java.lang.String msCancellationStatus;
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
    protected int mnFkFactoringBankId_n;
    protected int mnFkDpsYearId_n;
    protected int mnFkDpsDocId_n;
    protected int mnFkFinRecordYearId_n;
    protected int mnFkFinRecordPeriodId_n;
    protected int mnFkFinRecordBookkeepingCenterId_n;
    protected java.lang.String msFkFinRecordRecordTypeId_n;
    protected int mnFkFinRecordNumberId_n;
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
    protected int mnFkReceiptPaymentId_n;
    protected int mnFkUserProcessingId;
    protected int mnFkUserDeliveryId;
    protected java.util.Date mtUserProcessingTs;
    protected java.util.Date mtUserDeliveryTs;
    
    protected java.lang.String msAuxRfcEmisor;
    protected java.lang.String msAuxRfcReceptor;
    protected double mdAuxTotalCy;
    protected boolean mbAuxIsSign;
    protected boolean mbAuxIsProcessingValidation;
    
    protected byte[] mayExtraPrivateDocXml_ns;

    public SDataCfd() {
        super(SModConsts.TRN_CFD);
        reset();
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
                "erp.f_get_xml_atr('cfdi:Comprobante', ' Total=', '" + xmlSafe + "', " + DATA_TYPE_NUMBER + ") AS _xml_total, " + // without preceeding blank, attribute 'SubTotal' value is get!
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
                "erp.f_get_xml_atr('Comprobante', ' Total=', '" + xmlSafe + "', " + DATA_TYPE_NUMBER + ") AS _xml_total, " +
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
    
    public void setPkCfdId(int n) { mnPkCfdId = n; }
    public void setSeries(java.lang.String s) { msSeries = s; }
    public void setNumber(int n) { mnNumber = n; }
    public void setTimestamp(java.util.Date t) { mtTimestamp = t; }
    public void setCertNumber(java.lang.String s) { msCertNumber = s; }
    public void setStringSigned(java.lang.String s) { msStringSigned = s; }
    public void setSignature(java.lang.String s) { msSignature = s; }
    public void setBaseXUuid(java.lang.String s) { msBasexUuid = s; }
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
    public void setCancellationStatus(java.lang.String s) { msCancellationStatus = s; }
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
    public void setFkFactoringBankId_n(int n) { mnFkFactoringBankId_n = n; }
    public void setFkDpsYearId_n(int n) { mnFkDpsYearId_n = n; }
    public void setFkDpsDocId_n(int n) { mnFkDpsDocId_n = n; }
    public void setFkFinRecordYearId_n(int n) { mnFkFinRecordYearId_n = n; }
    public void setFkFinRecordPeriodId_n(int n) { mnFkFinRecordPeriodId_n = n; }
    public void setFkFinRecordBookkeepingCenterId_n(int n) { mnFkFinRecordBookkeepingCenterId_n = n; }
    public void setFkFinRecordRecordTypeId_n(java.lang.String s) { msFkFinRecordRecordTypeId_n = s; }
    public void setFkFinRecordNumberId_n(int n) { mnFkFinRecordNumberId_n = n; }
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
    public void setFkReceiptPaymentId_n(int n) { mnFkReceiptPaymentId_n = n; }
    public void setFkUserProcessingId(int n) { mnFkUserProcessingId = n; }
    public void setFkUserDeliveryId(int n) { mnFkUserDeliveryId = n; }
    public void setUserProcessingTs(java.util.Date t) { mtUserProcessingTs = t; }
    public void setUserDeliveryTs(java.util.Date t) { mtUserDeliveryTs = t; }

    public void setAuxRfcEmisor(java.lang.String s) { msAuxRfcEmisor = s; }
    public void setAuxRfcReceptor(java.lang.String s) { msAuxRfcReceptor = s; }
    public void setAuxTotalCy(double d) { mdAuxTotalCy = d; }
    public void setAuxIsSign(boolean b) { mbAuxIsSign = b; }
    public void setAuxIsProcessingValidation(boolean b) { mbAuxIsProcessingValidation = b; }    

    public int getPkCfdId() { return mnPkCfdId; }
    public java.lang.String getSeries() { return msSeries; }
    public int getNumber() { return mnNumber; }
    public java.util.Date getTimestamp() { return mtTimestamp; }
    public java.lang.String getCertNumber() { return msCertNumber; }
    public java.lang.String getStringSigned() { return msStringSigned; }
    public java.lang.String getSignature() { return msSignature; }
    public java.lang.String getBaseXUuid() { return msBasexUuid; }
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
    public java.lang.String getCancellationStatus() { return msCancellationStatus; }
    public java.lang.String getAcknowledgmentCancellationXml() { return msAcknowledgmentCancellationXml; }
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
    public int getFkFactoringBankId_n() { return mnFkFactoringBankId_n; }
    public int getFkDpsYearId_n() { return mnFkDpsYearId_n; }
    public int getFkDpsDocId_n() { return mnFkDpsDocId_n; }
    public int getFkFinRecordYearId_n() { return mnFkFinRecordYearId_n; }
    public int getFkFinRecordPeriodId_n() { return mnFkFinRecordPeriodId_n; }
    public int getFkFinRecordBookkeepingCenterId_n() { return mnFkFinRecordBookkeepingCenterId_n; }
    public java.lang.String getFkFinRecordRecordTypeId_n() { return msFkFinRecordRecordTypeId_n; }
    public int getFkFinRecordNumberId_n() { return mnFkFinRecordNumberId_n; }
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
    public int getFkReceiptPaymentId_n() { return mnFkReceiptPaymentId_n; }
    public int getFkUserProcessingId() { return mnFkUserProcessingId; }
    public int getFkUserDeliveryId() { return mnFkUserDeliveryId; }
    public java.util.Date getUserProcessingTs() { return mtUserProcessingTs; }
    public java.util.Date getUserDeliveryTs() { return mtUserDeliveryTs; }

    public java.lang.String getAuxRfcEmisor() { return msAuxRfcEmisor; }
    public java.lang.String getAuxRfcReceptor() { return msAuxRfcReceptor; }
    public double getAuxTotalCy() { return mdAuxTotalCy; }
    public boolean getAuxIsSign() { return mbAuxIsSign; }
    public boolean getAuxIsProcessingValidation() { return mbAuxIsProcessingValidation; }
    
    public boolean testDeletion(java.lang.String msg, int action) throws java.lang.Exception {
        String sMsg = msg;
        
        if (action == SDbConsts.ACTION_DELETE && isStamped()) {
            msDbmsError = sMsg + "¡El comprobante está timbrado!";
            throw new Exception(msDbmsError);
        }
        if (mnFkXmlStatusId == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
            msDbmsError = sMsg + "¡El comprobante está anulado!";
            throw new Exception(msDbmsError);
        }
        
        if (!mbAuxIsProcessingValidation) {
            if (mbIsProcessingWebService) {
                msDbmsError = sMsg + "¡" + SCfdConsts.ERR_MSG_PROCESS_WS_PAC + "!";
                throw new Exception(msDbmsError);
            }
            if (action != SDbConsts.ACTION_ANNUL && mbIsProcessingStorageXml) {
                msDbmsError = sMsg + "¡" + SCfdConsts.ERR_MSG_PROCESS_XML_STORAGE + "!";
                throw new Exception(msDbmsError);
            }
            if (action != SDbConsts.ACTION_ANNUL && mbIsProcessingStoragePdf) {
                msDbmsError = sMsg + "¡" + SCfdConsts.ERR_MSG_PROCESS_PDF_STORAGE + "!";
                throw new Exception(msDbmsError);
            }
        }
        
        return true;
    }
    
    /**
     * Check if CFD is an own CFD.
     * @return True if CFD is an own CFD.
     */
    public boolean isOwnCfd() {
        return !msCertNumber.isEmpty();
    }

    /**
     * Check if CFD is an own CFD and is actually a CFD (prior to CFDI).
     * @return True if CFD is an own CFD and a CFD (prior to CFDI).
     */
    public boolean isCfd() {
        return isOwnCfd() && mnFkXmlTypeId == SDataConstantsSys.TRNS_TP_XML_CFD;
    }

    /**
     * Check if CFD is an own CFD and is actually a CFDI 3.2 or 3.3.
     * @return True if CFD is an own CFD and a CFDI.
     */
    public boolean isCfdi() {
        return isOwnCfd() && (mnFkXmlTypeId == SDataConstantsSys.TRNS_TP_XML_CFDI_32 || mnFkXmlTypeId == SDataConstantsSys.TRNS_TP_XML_CFDI_33);
    }

    /**
     * Check if CFD is stamped, regardless it is an active, annulled or deleted registry.
     * If CFD is a CFDI, then it is stamped if it has an UUID.
     * If CFD is not a CFDI, then it is stamped if it is not new.
     * @return 
     */
    public boolean isStamped() {
        return (isCfdi() && !msUuid.isEmpty()) || (!isCfdi() && mnFkXmlStatusId != SDataConstantsSys.TRNS_ST_DPS_NEW);
    }
    
    public String getCfdNumber() {
        String cfdNumber = "";
        
        if (mnNumber != SLibConstants.UNDEFINED) {
            cfdNumber = STrnUtils.formatDocNumber(msSeries, "" + mnNumber);
        }
        else {
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

            cfdNumber = STrnUtils.formatDocNumber(numberSer, number);
        }
        
        return cfdNumber;
    }
    
    /*
     * Implementation of erp.lib.data.SDataRegistry
     */
    
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
        msBasexUuid = "";
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
        msCancellationStatus = "";
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
        mnFkFactoringBankId_n = 0;
        mnFkDpsYearId_n = 0;
        mnFkDpsDocId_n = 0;
        mnFkFinRecordYearId_n = 0;
        mnFkFinRecordPeriodId_n = 0;
        mnFkFinRecordBookkeepingCenterId_n = 0;
        msFkFinRecordRecordTypeId_n = "";
        mnFkFinRecordNumberId_n = 0;
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
        mnFkReceiptPaymentId_n = 0;
        mnFkUserProcessingId = 0;
        mnFkUserDeliveryId = 0;
        mtUserProcessingTs = null;
        mtUserDeliveryTs = null;

        msAuxRfcEmisor = "";
        msAuxRfcReceptor = "";
        mdAuxTotalCy = 0;
        mbAuxIsSign = false;
        mbAuxIsProcessingValidation = false;
        
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
            sql = "SELECT x.*, xc.doc_xml, xc.doc_xml_name, xc.ack_can_xml, xc.ack_can_pdf_n "
                    + "FROM trn_cfd AS x "
                    + "INNER JOIN " + SClientUtils.getComplementaryDbName(statement.getConnection()) + ".trn_cfd AS xc ON x.id_cfd = xc.id_cfd "
                    + "WHERE x.id_cfd = " + key[0] + " ";
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
                msBasexUuid = resultSet.getString("doc_xml_uuid");
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
                msCancellationStatus = resultSet.getString("can_st");
                msAcknowledgmentCancellationXml = resultSet.getString("ack_can_xml");
                //moAcknowledgmentCancellationPdf_n = resultSet.getBlob("ack_can_pdf_n"); cannot read a object blob (2014-09-01, jbarajas)
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
                mnFkFactoringBankId_n = resultSet.getInt("fid_fact_bank_n");
                mnFkDpsYearId_n = resultSet.getInt("fid_dps_year_n");
                mnFkDpsDocId_n = resultSet.getInt("fid_dps_doc_n");
                mnFkFinRecordYearId_n = resultSet.getInt("fid_fin_rec_year_n");
                mnFkFinRecordPeriodId_n = resultSet.getInt("fid_fin_rec_per_n");
                mnFkFinRecordBookkeepingCenterId_n = resultSet.getInt("fid_fin_rec_bkc_n");
                msFkFinRecordRecordTypeId_n = resultSet.getString("fid_fin_rec_tp_rec_n");
                mnFkFinRecordNumberId_n = resultSet.getInt("fid_fin_rec_num_n");
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
//                mnFkReceiptPaymentId_n = resultSet.getInt("fid_rcp_pay_n");
                mnFkUserProcessingId = resultSet.getInt("fid_usr_prc");
                mnFkUserDeliveryId = resultSet.getInt("fid_usr_dvy");
                mtUserProcessingTs = resultSet.getTimestamp("ts_prc");
                mtUserDeliveryTs = resultSet.getTimestamp("ts_dvy");
                
                /* XXX 2018-09-11, Sergio Flores: By now, BaseX exportation of XML is disabled, until this schema is properly evaluated and validated.
                String mysqlDatabaseURL = statement.getConnection().getMetaData().getURL();
            
                String databaseHost = SBaseXUtils.getDbHostFromUrl(mysqlDatabaseURL);
                String databaseName = SBaseXUtils.getDbNameFromUrl(mysqlDatabaseURL);
            
                String xmlDocument = null;
                try{
                    // Company BaseX database connection
                    SBaseXClient baseXSession = SBaseXUtils.getBaseXSessionInstance(databaseHost, 1984, "admin", "admin");
                    String getDocumentByNodeID = "doc(\"/"+ databaseName + "/" + msDocXmlUuid +".xml\")";                
                    xmlDocument  = SBaseXUtils.executeBaseXQuery(baseXSession, getDocumentByNodeID).get(0);                    
                }
                catch(Exception e){ 
                   SBaseXUtils.logError("READ ERROR - " + ExceptionUtils.getStackTrace(e));
                }
                if (xmlDocument != null) {
                    msDocXml = xmlDocument;
                }
                */
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_READ;
            }
            msDbmsError += "\n" + e.toString();
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int index = 1;
        final int LENGTH_CURRENCY = 15;
        boolean isUpddate = false;
        String sql = "";
        String sqlComp;
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
                    // obtain new Number for actual Series of CFD, only CFD with own number series will be numbered:
                    sql = "SELECT COALESCE(MAX(num), 0) + 1 FROM trn_cfd WHERE ser = '" + msSeries + "' ";
                    resultSet = statement.executeQuery(sql);
                    if (!resultSet.next()) {
                        throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
                    }
                    else {
                        mnNumber = resultSet.getInt(1);
                    }
                }
            
                /* XXX 2018-09-11, Sergio Flores: By now, BaseX exportation of XML is disabled, until this schema is properly evaluated and validated.
                msDocXmlUuid = SBaseXUtils.generateUniqueXmlId(connection);
                */
         
                sql = "INSERT INTO trn_cfd (id_cfd, " +
                        "ser, num, " +
                        "ts, cert_num, str_signed, signature, doc_xml_uuid, xml_rfc_emi, xml_rfc_rec, " +
                        "xml_tot, xml_mon, xml_tc, xml_sign_n, uuid, qrc_n, can_st, ack_dvy, msg_dvy, " +
                        "b_prc_ws, b_prc_sto_xml, b_prc_sto_pdf, " +
                        "b_con, " +
                        "fid_tp_cfd, fid_tp_xml, fid_st_xml, fid_tp_xml_dvy, fid_st_xml_dvy, " +
                        "fid_cob_n, fid_fact_bank_n, " +
                        "fid_dps_year_n, fid_dps_doc_n, " +
                        "fid_fin_rec_year_n, fid_fin_rec_per_n, fid_fin_rec_bkc_n, fid_fin_rec_tp_rec_n, fid_fin_rec_num_n, " + 
                        "fid_rec_year_n, fid_rec_per_n, fid_rec_bkc_n, fid_rec_tp_rec_n, fid_rec_num_n, fid_rec_ety_n, " +
                        "fid_pay_pay_n, fid_pay_emp_n, fid_pay_bpr_n, fid_pay_rcp_pay_n, fid_pay_rcp_emp_n, fid_pay_rcp_iss_n, " +
                        "fid_rcp_pay_n, " +
                        "fid_usr_prc, " +
                        "fid_usr_dvy, " +
                        "ts_prc, " +
                        "ts_dvy) " +
                        "VALUES (" + mnPkCfdId + ", " + // 1
                        "?, ?, " + // 3
                        "?, ?, ?, ?, ?, ?, ?, " + // 10
                        "?, ?, ?, ?, ?, ?, ?, ?, ?, " + // 19
                        "?, ?, ?, " + // 22
                        "?, " + // 23
                        "?, ?, ?, ?, ?, " + // 28
                        "?, ?, " + // 30
                        "?, ?, " + // 32
                        "?, ?, ?, ?, ?, " + // 37
                        "?, ?, ?, ?, ?, ?, " + // 43
                        "?, ?, ?, ?, ?, ?, " + // 49
//                        "?, " + // 50
                        "?, " + // 51
                        "?, " + // 52
                        "NOW(), " + // 53
                        "NOW())"; // 54
            }
            else {
                isUpddate = true;
                
                sql = "UPDATE trn_cfd SET ser = ?, num = ?, " +
                        "ts = ?, cert_num = ?, str_signed = ?, signature = ?, doc_xml_uuid = ?, xml_rfc_emi = ?, xml_rfc_rec = ?, " +
                        "xml_tot = ?, xml_mon = ?, xml_tc = ?, xml_sign_n = ?, uuid = ?, qrc_n = ?, can_st = ?, ack_dvy = ?, msg_dvy = ?, " +
                        //"b_prc_ws = ?, b_prc_sto_xml = ?, b_prc_sto_pdf = ?, " + // managed separately
                        "b_con = ?, " +
                        "fid_tp_cfd = ?, fid_tp_xml = ?, fid_st_xml = ?, fid_tp_xml_dvy = ?, fid_st_xml_dvy = ?, " +
                        "fid_cob_n = ?, fid_fact_bank_n = ?, " +
                        "fid_dps_year_n = ?, fid_dps_doc_n = ?, " +
                        "fid_fin_rec_year_n = ?, fid_fin_rec_per_n = ?, fid_fin_rec_bkc_n = ?, fid_fin_rec_tp_rec_n = ?, fid_fin_rec_num_n = ?, " + 
                        "fid_rec_year_n = ?, fid_rec_per_n = ?, fid_rec_bkc_n = ?, fid_rec_tp_rec_n = ?, fid_rec_num_n = ?, fid_rec_ety_n = ?, " +
                        "fid_pay_pay_n = ?, fid_pay_emp_n = ?, fid_pay_bpr_n = ?, fid_pay_rcp_pay_n = ?, fid_pay_rcp_emp_n = ?, fid_pay_rcp_iss_n = ?, " +
//                        "fid_rcp_pay_n = ?, " +
                        //fid_usr_prc = ?, // managed separately
                        "fid_usr_dvy = ?, " +
                        //ts_prc = NOW(), // managed separately
                        "ts_dvy = NOW() " +
                        "WHERE id_cfd = " + mnPkCfdId + " "; 
            }
            
            parseCfdiAttributes(connection, msDocXml);
            
            preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setString(index++, msSeries);
            preparedStatement.setInt(index++, mnNumber);
            preparedStatement.setTimestamp(index++, new java.sql.Timestamp(mtTimestamp.getTime()));
            preparedStatement.setString(index++, msCertNumber);
            preparedStatement.setString(index++, msStringSigned);
            preparedStatement.setString(index++, msSignature);
            preparedStatement.setString(index++, msBasexUuid);
            preparedStatement.setString(index++, msDocXmlRfcEmi);
            preparedStatement.setString(index++, msDocXmlRfcRec);
            preparedStatement.setDouble(index++, mdDocXmlTot);
            if (msDocXmlMon.length() >= LENGTH_CURRENCY) {
                preparedStatement.setString(index++, msDocXmlMon.substring(0, LENGTH_CURRENCY - 1)); //Valid value size
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
            preparedStatement.setNull(index++, java.sql.Types.BLOB); // NOTE: 2018-05-16, Sergio Flores: QR code will not be saved anymore, it was never useful!
            preparedStatement.setString(index++, msCancellationStatus);
            
            preparedStatement.setString(index++, msAcknowledgmentDelivery);
            preparedStatement.setString(index++, msMessageDelivery);
            
            if (!isUpddate) {
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
            
            if (mnFkCompanyBranchId_n == 0) {
                preparedStatement.setNull(index++, java.sql.Types.INTEGER);
            }
            else {
                preparedStatement.setInt(index++, mnFkCompanyBranchId_n);
            }
            
            if (mnFkFactoringBankId_n == 0) {
                preparedStatement.setNull(index++, java.sql.Types.INTEGER);
            }
            else {
                preparedStatement.setInt(index++, mnFkFactoringBankId_n);
            }
            
            if (mnFkDpsYearId_n == 0) {
                preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
                preparedStatement.setNull(index++, java.sql.Types.INTEGER);
            }
            else {
                preparedStatement.setInt(index++, mnFkDpsYearId_n);
                preparedStatement.setInt(index++, mnFkDpsDocId_n);
            }
            
            if (mnFkFinRecordYearId_n == 0) {
                preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
                preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
                preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
                preparedStatement.setNull(index++, java.sql.Types.CHAR);
                preparedStatement.setNull(index++, java.sql.Types.INTEGER);
            }
            else {
                preparedStatement.setInt(index++, mnFkFinRecordYearId_n);
                preparedStatement.setInt(index++, mnFkFinRecordPeriodId_n);
                preparedStatement.setInt(index++, mnFkFinRecordBookkeepingCenterId_n);
                preparedStatement.setString(index++, msFkFinRecordRecordTypeId_n);
                preparedStatement.setInt(index++, mnFkFinRecordNumberId_n);
            }
            
            if (mnFkRecordYearId_n == 0) {
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
            
            if (mnFkPayrollPayrollId_n == 0) {
                preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
                preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
                preparedStatement.setNull(index++, java.sql.Types.INTEGER);
            }
            else {
                preparedStatement.setInt(index++, mnFkPayrollPayrollId_n);
                preparedStatement.setInt(index++, mnFkPayrollEmployeeId_n);
                preparedStatement.setInt(index++, mnFkPayrollBizPartnerId_n);
            }
            
            if (mnFkPayrollReceiptPayrollId_n == 0) {
                preparedStatement.setNull(index++, java.sql.Types.INTEGER);
                preparedStatement.setNull(index++, java.sql.Types.INTEGER);
                preparedStatement.setNull(index++, java.sql.Types.SMALLINT);
            }
            else {
                preparedStatement.setInt(index++, mnFkPayrollReceiptPayrollId_n);
                preparedStatement.setInt(index++, mnFkPayrollReceiptEmployeeId_n);
                preparedStatement.setInt(index++, mnFkPayrollReceiptIssueId_n);
            }
            
            if (mnFkReceiptPaymentId_n == 0) {
//                preparedStatement.setNull(index++, java.sql.Types.INTEGER);
            }
            else {
//                preparedStatement.setInt(index++, mnFkReceiptPaymentId_n);
            }
            
            if (!isUpddate) {
                preparedStatement.setInt(index++, mnFkUserProcessingId);
            }
            
            preparedStatement.setInt(index++, mnFkUserDeliveryId);
            
            preparedStatement.execute();            
            
            // Ingresar a la BD complementaria:
            
            if (!isUpddate) {
                sqlComp = "INSERT INTO " + SClientUtils.getComplementaryDbName(connection) + ".trn_cfd " +
                            "(id_cfd, doc_xml, doc_xml_name, ack_can_xml, ack_can_pdf_n) " + 
                            "VALUES (" + mnPkCfdId + ", '" + SLibUtils.textToSql(msDocXml) + "', '" + SLibUtils.textToSql(msDocXmlName) + "', " +
                            "'" + SLibUtils.textToSql(msAcknowledgmentCancellationXml) + "', NULL)";
            }
            else {
                sqlComp = "UPDATE " + SClientUtils.getComplementaryDbName(connection) + ".trn_cfd " +
                        "SET doc_xml = '" + SLibUtils.textToSql(msDocXml) + "', doc_xml_name = '" + SLibUtils.textToSql(msDocXmlName) + "', " +
                        "ack_can_xml = '" + SLibUtils.textToSql(msAcknowledgmentCancellationXml) + "' " + 
                        "WHERE id_cfd = " + mnPkCfdId + " ";
            }
            connection.createStatement().execute(sqlComp);
            
            /* XXX 2018-09-11, Sergio Flores: By now, BaseX exportation of XML is disabled, until this schema is properly evaluated and validated.
            try {
                addFileToBaseXDb(connection);
            }
            catch(Exception e) { 
               SBaseXUtils.logError("SAVE ERROR - " + ExceptionUtils.getStackTrace(e) + " - XML DATA: " + msDocXml );
            }
            */
            
            mnDbmsErrorId = 0;
            msDbmsError = "";

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_SAVE;
            }
            msDbmsError += "\n" + e.toString();
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
                if (testDeletion("No se puede timbrar el comprobante: ", SDbConsts.ACTION_DELETE)) {
                    mnLastDbActionResult = SLibConstants.DB_CAN_SAVE_YES;
                }
            }
            catch (Exception e) {
                mnLastDbActionResult = SLibConstants.DB_CAN_SAVE_NO;
                if (msDbmsError.isEmpty()) {
                    msDbmsError = SLibConstants.MSG_ERR_DB_REG_CAN_SAVE;
                }
                msDbmsError += "\n" + e.toString();
                SLibUtilities.printOutException(this, e);
            }
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public int canAnnul(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            if (testDeletion("No se puede anular el comprobante: ", SDbConsts.ACTION_ANNUL)) {
                mnLastDbActionResult = SLibConstants.DB_CAN_ANNUL_YES;
            }
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_CAN_ANNUL_NO;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_CAN_ANNUL;
            }
            msDbmsError += "\n" + e.toString();
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

            if (testDeletion("No se puede anular el comprobante: ", SDbConsts.ACTION_ANNUL)) {
                sSql = "UPDATE trn_cfd SET fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " " +
                        "WHERE id_cfd = " + mnPkCfdId + " ";
                oStatement.execute(sSql);

                mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_OK;
            }
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_ERROR;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_ANNUL;
            }
            msDbmsError += "\n" + e.toString();
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }
    
    /**
     * Updates some field of status of this CFD.
     * @param connection Database connection.
     * @param field Field or status of this CFD to be updated. Available options defined in this class by constants named FIELD_...
     * @param value Updated value.
     * @throws Exception 
     */
    public void saveField(java.sql.Connection connection, final int field, final Object value) throws Exception {
        String sql = "";

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        sql = "UPDATE trn_cfd SET ";

        switch (field) {
            case FIELD_PRC_WS:
                sql += "b_prc_ws = " + value + " ";
                break;
            case FIELD_PRC_STO_XML:
                sql += "b_prc_sto_xml = " + value + " ";
                break;
            case FIELD_PRC_STO_PDF:
                sql += "b_prc_sto_pdf = " + value + " ";
                break;
            case FIELD_PRC_CON:
                sql += "b_con = " + value + " ";
                break;
            case FIELD_PRC_USR:
                sql += "fid_usr_prc = " + value + ", ts_prc = NOW() ";
                break;
            case FIELD_DVY_ACK:
                sql += "ack_dvy = '" + value + "' ";
                break;
            case FIELD_DVY_MSG:
                sql += "msg_dvy = '" + value + "' ";
                break;
            case FIELD_DVY_TP:
                sql += "fid_tp_xml_dvy = " + value + " ";
                break;
            case FIELD_DVY_ST:
                sql += "fid_st_xml_dvy = " + value + " ";
                break;
            case FIELD_DVY_USR:
                sql += "fid_usr_dvy = " + value + ", ts_dvy = NOW() ";
                break;
            case FIELD_CAN_ST:
                sql += "can_st = '" + value + "' ";
                break;
            case FIELD_FK_RCP_PAY:
                sql += "fid_rcp_pay_n = " + value + " ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        sql += "WHERE id_cfd = " + mnPkCfdId + ";";
        
        connection.createStatement().execute(sql);
        
        mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
    }

    /**
     * XXX 2018-09-11, Sergio Flores: This BaseX exportation of XML needs to be properly evaluated and validated to be safely used.
     * @param connection
     * @throws SQLException
     * @throws IOException
     * @throws Exception 
     */
    private void addFileToBaseXDb(Connection connection) throws SQLException, IOException, Exception {
        String mysqlDatabaseURL = connection.getMetaData().getURL();

        String databaseHost = SBaseXUtils.getDbHostFromUrl(mysqlDatabaseURL);
        String databaseName = SBaseXUtils.getDbNameFromUrl(mysqlDatabaseURL);

        // Company BaseX database connection:
        SBaseXClient baseXSession = SBaseXUtils.getBaseXSessionInstance(databaseHost, 1984, "admin", "admin");

        // escape XML special characters:
        String xmlDocBody = SBaseXUtils.escapeSpecialCharacters(msDocXml);

        // Parse the xml body and add it to the BaseX database:
        String addXmlToDBQuery = "db:replace(\"" + databaseName + "\", \"/" + msBasexUuid + ".xml" + "\", fn:parse-xml(\"" + xmlDocBody + "\"))";

        SBaseXUtils.executeBaseXQuery(baseXSession, addXmlToDBQuery);
    }
    
    /**
     * Get all dependent journal voucher keys for supplied CFD.
     * @param statement
     * @param cfdId
     * @return
     * @throws java.lang.Exception
     */
    public static ArrayList<Object[]> getDependentJournalVoucherKeys(final Statement statement, final int cfdId) throws Exception {
        ArrayList<Object[]> keys = new ArrayList<>();
        String sql = "SELECT DISTINCT id_year, id_per, id_bkc, id_tp_rec, id_num "
                + "FROM fin_rec_ety "
                + "WHERE NOT b_del AND fid_cfd_n = " + cfdId + " "
                + "ORDER BY id_year, id_per, id_bkc, id_tp_rec, id_num; ";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            Object[] pk = new Object[5];
            pk[0] = resultSet.getInt("id_year");
            pk[1] = resultSet.getInt("id_per");
            pk[2] = resultSet.getInt("id_bkc");
            pk[3] = resultSet.getString("id_tp_rec");
            pk[4] = resultSet.getInt("id_num");
            keys.add(pk);
        }
        return keys;
    }
}

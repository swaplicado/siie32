/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Sergio Flores
 */
public final class SCfdPacket implements java.io.Serializable {

    private int mnDpsYearId;
    private int mnDpsDocId;
    private int mnCfdId;
    private int mnPacId;
    private cfd.DElement moCfdRootElement;
    private java.lang.String msStringSigned;
    private java.lang.String msSignature;
    private java.lang.String msCertNumber;
    private java.lang.String msCertBase64;
    private java.lang.String msRfcEmisor;
    private java.lang.String msRfcReceptor;
    private double mdTotalCy;
    private java.lang.String msAcknowledgmentCancellation;
    private java.lang.String msUuid;
    private boolean mbIsConsistent;
    private int mnFkCfdTypeId;
    private int mnFkXmlTypeId;
    private int mnFkXmlStatusId;
    private int mnFkXmlDeliveryTypeId;
    private int mnFkXmlDeliveryStatusId;
    private int mnQuantityStamp;
    private boolean mbConsumeStamp;
    private boolean mbGenerateQrCode;
    private int mnPayrollPayrollId;
    private int mnPayrollBizPartnerId;
    private int mnPayrollEmployeeId;
    private int mnPayrollReceiptPayrollId;
    private int mnPayrollReceiptEmployeeId;
    private java.lang.String msXml;
    private java.lang.String msXmlName;
    private java.util.Date mtXmlDate;
    private int mnFkUserDeliveryId;
    private SDataDps moDps;
    private int mnLogSignId;

    public SCfdPacket() {
        mnDpsYearId = 0;
        mnDpsDocId = 0;
        mnCfdId = 0;
        mnPacId = 0;
        moCfdRootElement = null;
        msStringSigned = "";
        msSignature = "";
        msCertNumber = "";
        msCertBase64 = "";
        msRfcEmisor = "";
        msRfcReceptor = "";
        mdTotalCy = 0;
        msAcknowledgmentCancellation = "";
        msUuid = "";
        mbIsConsistent = true;
        mnFkCfdTypeId = 0 ;
        mnFkXmlTypeId = 0 ;
        mnFkXmlStatusId = 0;
        mnFkXmlDeliveryTypeId = 0;
        mnFkXmlDeliveryStatusId = 0;
        mnQuantityStamp = 0;
        mbConsumeStamp = false;
        mbGenerateQrCode = false;
        mnPayrollPayrollId = 0;
        mnPayrollBizPartnerId = 0;
        mnPayrollEmployeeId = 0;
        mnPayrollReceiptPayrollId = 0;
        mnPayrollReceiptEmployeeId = 0;
        msXml = "";
        msXmlName = "";
        mtXmlDate = null;
        mnFkUserDeliveryId = 0;
        
        moDps = null;
        mnLogSignId = 0;
    }

    public void setDpsYearId(int n) { mnDpsYearId = n; }
    public void setDpsDocId(int n) { mnDpsDocId = n; }
    public void setCfdId(int n) { mnCfdId = n; }
    public void setPacId(int n) { mnPacId = n; }
    public void setCfdRootElement(cfd.DElement o) { moCfdRootElement = o; }
    public void setStringSigned(java.lang.String s) { msStringSigned = s; }
    public void setSignature(java.lang.String s) { msSignature = s; }
    public void setCertNumber(java.lang.String s) { msCertNumber = s; }
    public void setCertBase64(java.lang.String s) { msCertBase64 = s; }
    public void setRfcEmisor(java.lang.String s) { msRfcEmisor = s; }
    public void setRfcReceptor(java.lang.String s) { msRfcReceptor = s; }
    public void setTotalCy(double d) { mdTotalCy = d; }
    public void setAcknowledgmentCancellation(java.lang.String s) { msAcknowledgmentCancellation = s; }
    public void setUuid(java.lang.String s) { msUuid = s; }
    public void setIsConsistent(boolean b) { mbIsConsistent = b; }
    public void setFkCfdTypeId(int n) { mnFkCfdTypeId = n; }
    public void setFkXmlTypeId(int n) { mnFkXmlTypeId = n; }
    public void setFkXmlStatusId(int n) { mnFkXmlStatusId = n; }
    public void setFkXmlDeliveryTypeId(int n) { mnFkXmlDeliveryTypeId = n; }
    public void setFkXmlDeliveryStatusId(int n) { mnFkXmlDeliveryStatusId = n; }
    public void setQuantityStamp(int n) { mnQuantityStamp = n; }
    public void setConsumeStamp(java.lang.Boolean b) { mbConsumeStamp = b; }
    public void setGenerateQrCode(boolean b) { mbGenerateQrCode = b; }
    public void setPayrollPayrollId(int n) { mnPayrollPayrollId = n; }
    public void setPayrollBizPartnerId(int n) { mnPayrollBizPartnerId = n; }
    public void setPayrollEmployeeId(int n) { mnPayrollEmployeeId = n; }
    public void setPayrollReceiptPayrollId(int n) { mnPayrollReceiptPayrollId = n; }
    public void setPayrollReceiptEmployeeId(int n) { mnPayrollReceiptEmployeeId = n; }
    public void setXml(java.lang.String s) { msXml = s; }
    public void setXmlName(java.lang.String s) { msXmlName = s; }
    public void setXmlDate(java.util.Date t) { mtXmlDate = t; }
    public void setFkUserDeliveryId(int n) { mnFkUserDeliveryId = n; }
    
    public void setDps(SDataDps o) { moDps = o; }
    public void setLogSignId(int n) { mnLogSignId = n; }

    public int getDpsYearId() { return mnDpsYearId; }
    public int getDpsDocId() { return mnDpsDocId; }
    public int getCfdId() { return mnCfdId; }
    public int getPacId() { return mnPacId; }
    public cfd.DElement getCfdRootElement() { return moCfdRootElement; }
    public java.lang.String getStringSigned() { return msStringSigned; }
    public java.lang.String getSignature() { return msSignature; }
    public java.lang.String getCertNumber() { return msCertNumber; }
    public java.lang.String getCertBase64() { return msCertBase64; }
    public java.lang.String getRfcEmisor() { return msRfcEmisor; }
    public java.lang.String getRfcReceptor() { return msRfcReceptor; }
    public double getTotalCy() { return mdTotalCy; }
    public java.lang.String getAcknowledgmentCancellation() { return msAcknowledgmentCancellation; }
    public java.lang.String getUuid() { return msUuid; }
    public boolean getIsConsistent() { return mbIsConsistent; }
    public int getFkCfdTypeId() { return mnFkCfdTypeId; }
    public int getFkXmlTypeId() { return mnFkXmlTypeId; }
    public int getFkXmlStatusId() { return mnFkXmlStatusId; }
    public int getFkXmlDeliveryTypeId() { return mnFkXmlDeliveryTypeId; }
    public int getFkXmlDeliveryStatusId() { return mnFkXmlDeliveryStatusId; }
    public int getQuantityStamp() { return mnQuantityStamp; }
    public java.lang.Boolean getConsumeStamp() { return mbConsumeStamp; }
    public boolean getGenerateQrCode() { return mbGenerateQrCode; }
    public int getPayrollPayrollId() { return mnPayrollPayrollId; }
    public int getPayrollBizPartnerId() { return mnPayrollBizPartnerId; }
    public int getPayrollEmployeeId() { return mnPayrollEmployeeId; }
    public int getPayrollReceiptPayrollId() { return mnPayrollReceiptPayrollId; }
    public int getPayrollReceiptEmployeeId() { return mnPayrollReceiptEmployeeId; }
    public java.lang.String getXml() { return msXml; }
    public java.lang.String getXmlName() { return msXmlName; }
    public java.util.Date getXmlDate() { return mtXmlDate; }
    public int getFkUserDeliveryId() { return mnFkUserDeliveryId; }
    
    public SDataDps getDps() { return moDps; }
    public int getLogSignId() { return mnLogSignId; }
}

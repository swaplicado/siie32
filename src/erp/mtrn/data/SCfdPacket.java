/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import cfd.DCfd;
import erp.mhrs.data.SDataPayrollReceiptIssue;
import sa.lib.xml.SXmlConsts;

/**
 *
 * @author Sergio Flores
 */
public final class SCfdPacket implements java.io.Serializable {

    private int mnCfdId;                        // used in creation of SDataCfd
    private java.lang.String msCfdSeries;       // used in creation of SDataCfd
    private int mnCfdNumber;                    // used in creation of SDataCfd
    private java.lang.String msCfdCertNumber;   // used in creation of SDataCfd
    private java.lang.String msCfdStringSigned; // used in creation of SDataCfd
    private java.lang.String msCfdSignature;    // used in creation of SDataCfd
    private java.lang.String msDocXmlUuid;      // used in creation of SDataCfd
    private java.lang.String msDocXml;          // used in creation of SDataCfd
    private java.lang.String msDocXmlName;      // used in creation of SDataCfd
    private java.util.Date mtXmlDatetime;       // used in creation of SDataCfd
    private java.lang.String msXmlRfcEmisor;    // used in creation of SDataCfd
    private java.lang.String msXmlRfcReceptor;  // used in creation of SDataCfd
    private double mdXmlTotalCy;                // used in creation of SDataCfd
    private java.lang.String msCfdUuid;         // used in creation of SDataCfd
    private java.lang.String msAcknowledgmentCancellationXml;   // used in creation of SDataCfd
    private boolean mbIsCfdConsistent;          // used in creation of SDataCfd
    private int mnFkCfdTypeId;                  // used in creation of SDataCfd
    private int mnFkXmlTypeId;                  // used in creation of SDataCfd
    private int mnFkXmlStatusId;                // used in creation of SDataCfd
    private int mnFkXmlDeliveryTypeId;          // used in creation of SDataCfd
    private int mnFkXmlDeliveryStatusId;        // used in creation of SDataCfd
    private int mnFkUserDeliveryId;             // used in creation of SDataCfd
    private int mnFkCompanyBranchId;            // used in creation of SDataCfd
    private int mnDpsYearId;                    // used in creation of SDataCfd
    private int mnDpsDocId;                     // used in creation of SDataCfd
    private int mnRecordEntryYearId;                    // used in creation of SDataCfd
    private int mnRecordEntryPeriodId;                  // used in creation of SDataCfd
    private int mnRecordEntryBookkeepingCenterId;       // used in creation of SDataCfd
    private java.lang.String msRecordEntryRecordTypeId; // used in creation of SDataCfd
    private int mnRecordEntryNumberId;                  // used in creation of SDataCfd
    private int mnRecordEntryEntryId;                   // used in creation of SDataCfd
    private int mnPayrollPayrollId;             // used in creation of SDataCfd
    private int mnPayrollEmployeeId;            // used in creation of SDataCfd
    private int mnPayrollBizPartnerId;          // used in creation of SDataCfd
    private int mnPayrollReceiptPayrollId;      // used in creation of SDataCfd
    private int mnPayrollReceiptEmployeeId;     // used in creation of SDataCfd
    private int mnPayrollReceiptIssueId;        // used in creation of SDataCfd
    
    private SDataDps moAuxDataDps;
    private SDataCfdPayment moAuxDataCfdPayment;
    private SDataPayrollReceiptIssue moAuxDataPayrollReceiptIssue;

    private cfd.DElement moAuxCfdRootElement;
    private int mnAuxPacId;
    private int mnAuxLogSignId;
    private int mnAuxStampQuantity;
    private boolean mbAuxStampConsume;
    
    public SCfdPacket() {
        mnCfdId = 0;
        msCfdSeries = "";
        mnCfdNumber = 0;
        msCfdCertNumber = "";
        msCfdStringSigned = "";
        msCfdSignature = "";
        msDocXmlUuid = "";
        msDocXml = "";
        msDocXmlName = "";
        mtXmlDatetime = null;
        msXmlRfcEmisor = "";
        msXmlRfcReceptor = "";
        mdXmlTotalCy = 0;
        msCfdUuid = "";
        msAcknowledgmentCancellationXml = "";
        mbIsCfdConsistent = true;
        mnFkCfdTypeId = 0 ;
        mnFkXmlTypeId = 0 ;
        mnFkXmlStatusId = 0;
        mnFkXmlDeliveryTypeId = 0;
        mnFkXmlDeliveryStatusId = 0;
        mnFkUserDeliveryId = 0;
        mnFkCompanyBranchId = 0;
        mnDpsYearId = 0;
        mnDpsDocId = 0;
        mnRecordEntryYearId = 0;
        mnRecordEntryPeriodId = 0;
        mnRecordEntryBookkeepingCenterId = 0;
        msRecordEntryRecordTypeId = "";
        mnRecordEntryNumberId = 0;
        mnRecordEntryEntryId = 0;
        mnPayrollPayrollId = 0;
        mnPayrollEmployeeId = 0;
        mnPayrollBizPartnerId = 0;
        mnPayrollReceiptPayrollId = 0;
        mnPayrollReceiptEmployeeId = 0;
        mnPayrollReceiptIssueId = 0;
        
        moAuxDataDps = null;
        moAuxDataCfdPayment = null;
        moAuxDataPayrollReceiptIssue = null;
        
        moAuxCfdRootElement = null;
        mnAuxPacId = 0;
        mnAuxLogSignId = 0;
        mnAuxStampQuantity = 0;
        mbAuxStampConsume = false;
    }

    public void setCfdId(int n) { mnCfdId = n; }
    public void setCfdSeries(java.lang.String s) { msCfdSeries = s; }
    public void setCfdNumber(int n) { mnCfdNumber = n; }
    public void setCfdCertNumber(java.lang.String s) { msCfdCertNumber = s; }
    public void setCfdStringSigned(java.lang.String s) { msCfdStringSigned = s; }
    public void setCfdSignature(java.lang.String s) { msCfdSignature = s; }
    public void setDocXmlUuid(java.lang.String s) { msDocXmlUuid = s; }
    public void setDocXml(java.lang.String s) { msDocXml = s; }
    public void setDocXmlName(java.lang.String s) { msDocXmlName = s; }
    public void setXmlDate(java.util.Date t) { mtXmlDatetime = t; }
    public void setXmlRfcEmisor(java.lang.String s) { msXmlRfcEmisor = s; }
    public void setXmlRfcReceptor(java.lang.String s) { msXmlRfcReceptor = s; }
    public void setXmlTotalCy(double d) { mdXmlTotalCy = d; }
    public void setCfdUuid(java.lang.String s) { msCfdUuid = s; }
    public void setAcknowledgmentCancellationXml(java.lang.String s) { msAcknowledgmentCancellationXml = s; }
    public void setIsCfdConsistent(boolean b) { mbIsCfdConsistent = b; }
    public void setFkCfdTypeId(int n) { mnFkCfdTypeId = n; }
    public void setFkXmlTypeId(int n) { mnFkXmlTypeId = n; }
    public void setFkXmlStatusId(int n) { mnFkXmlStatusId = n; }
    public void setFkXmlDeliveryTypeId(int n) { mnFkXmlDeliveryTypeId = n; }
    public void setFkXmlDeliveryStatusId(int n) { mnFkXmlDeliveryStatusId = n; }
    public void setFkUserDeliveryId(int n) { mnFkUserDeliveryId = n; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setDpsYearId(int n) { mnDpsYearId = n; }
    public void setDpsDocId(int n) { mnDpsDocId = n; }
    public void setRecordEntryYearId(int n) { mnRecordEntryYearId = n; }
    public void setRecordEntryPeriodId(int n) { mnRecordEntryPeriodId = n; }
    public void setRecordEntryBookkeepingCenterId(int n) { mnRecordEntryBookkeepingCenterId = n; }
    public void setRecordEntryRecordTypeId(java.lang.String s) { msRecordEntryRecordTypeId = s; }
    public void setRecordEntryNumberId(int n) { mnRecordEntryNumberId = n; }
    public void setRecordEntryEntryId(int n) { mnRecordEntryEntryId = n; }
    public void setPayrollPayrollId(int n) { mnPayrollPayrollId = n; }
    public void setPayrollEmployeeId(int n) { mnPayrollEmployeeId = n; }
    public void setPayrollBizPartnerId(int n) { mnPayrollBizPartnerId = n; }
    public void setPayrollReceiptPayrollId(int n) { mnPayrollReceiptPayrollId = n; }
    public void setPayrollReceiptEmployeeId(int n) { mnPayrollReceiptEmployeeId = n; }
    public void setPayrollReceiptIssueId(int n) { mnPayrollReceiptIssueId = n; }
    
    public void setAuxDataDps(SDataDps o) { moAuxDataDps = o; }
    public void setAuxDataCfdPayment(SDataCfdPayment o) { moAuxDataCfdPayment = o; }
    public void setAuxDataPayrollReceiptIssue(SDataPayrollReceiptIssue o) { moAuxDataPayrollReceiptIssue = o; }

    public void setAuxCfdRootElement(cfd.DElement o) { moAuxCfdRootElement = o; }
    public void setAuxPacId(int n) { mnAuxPacId = n; }
    public void setAuxLogSignId(int n) { mnAuxLogSignId = n; }
    public void setAuxStampQuantity(int n) { mnAuxStampQuantity = n; }
    public void setAuxStampConsume(boolean b) { mbAuxStampConsume = b; }
    
    public int getCfdId() { return mnCfdId; }
    public java.lang.String getCfdSeries() { return msCfdSeries; }
    public int getCfdNumber() { return mnCfdNumber; }
    //public java.lang.String getCfdCertNumber() { return msCfdCertNumber; }
    public java.lang.String getCfdStringSigned() { return msCfdStringSigned; }
    public java.lang.String getCfdSignature() { return msCfdSignature; }
    public java.lang.String getDocXmlUuid() { return msDocXmlUuid; }
    public java.lang.String getDocXml() { return msDocXml; }
    public java.lang.String getDocXmlName() { return msDocXmlName; }
    public java.util.Date getXmlDate() { return mtXmlDatetime; }
    //public java.lang.String getXmlRfcEmisor() { return msXmlRfcEmisor; }
    //public java.lang.String getXmlRfcReceptor() { return msXmlRfcReceptor; }
    //public double getXmlTotalCy() { return mdXmlTotalCy; }
    //public java.lang.String getCfdUuid() { return msCfdUuid; }
    public java.lang.String getAcknowledgmentCancellationXml() { return msAcknowledgmentCancellationXml; }
    public boolean getIsCfdConsistent() { return mbIsCfdConsistent; }
    public int getFkCfdTypeId() { return mnFkCfdTypeId; }
    public int getFkXmlTypeId() { return mnFkXmlTypeId; }
    public int getFkXmlStatusId() { return mnFkXmlStatusId; }
    public int getFkXmlDeliveryTypeId() { return mnFkXmlDeliveryTypeId; }
    public int getFkXmlDeliveryStatusId() { return mnFkXmlDeliveryStatusId; }
    public int getFkUserDeliveryId() { return mnFkUserDeliveryId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getDpsYearId() { return mnDpsYearId; }
    public int getDpsDocId() { return mnDpsDocId; }
    public int getRecordEntryYearId() { return mnRecordEntryYearId; }
    public int getRecordEntryPeriodId() { return mnRecordEntryPeriodId; }
    public int getRecordEntryBookkeepingCenterId() { return mnRecordEntryBookkeepingCenterId; }
    public java.lang.String getRecordEntryRecordTypeId() { return msRecordEntryRecordTypeId; }
    public int getRecordEntryNumberId() { return mnRecordEntryNumberId; }
    public int getRecordEntryEntryId() { return mnRecordEntryEntryId; }
    public int getPayrollPayrollId() { return mnPayrollPayrollId; }
    public int getPayrollEmployeeId() { return mnPayrollEmployeeId; }
    public int getPayrollBizPartnerId() { return mnPayrollBizPartnerId; }
    public int getPayrollReceiptPayrollId() { return mnPayrollReceiptPayrollId; }
    public int getPayrollReceiptEmployeeId() { return mnPayrollReceiptEmployeeId; }
    public int getPayrollReceiptIssueId() { return mnPayrollReceiptIssueId; }
    
    public SDataDps getAuxDataDps() { return moAuxDataDps; }
    public SDataCfdPayment getAuxDataCfdPayment() { return moAuxDataCfdPayment; }
    public SDataPayrollReceiptIssue getAuxDataPayrollReceiptIssue() { return moAuxDataPayrollReceiptIssue; }
    
    public cfd.DElement getAuxCfdRootElement() { return moAuxCfdRootElement; }
    public int getAuxPacId() { return mnAuxPacId; }
    public int getAuxLogSignId() { return mnAuxLogSignId; }
    public int getAuxStampQuantity() { return mnAuxStampQuantity; }
    public boolean getAuxStampConsume() { return mbAuxStampConsume; }
    
    /**
     * Creates a new instance of class <code>erp.mtrn.data.SDataCfd</code> with own data members.
     * @return new instance of class <code>erp.mtrn.data.SDataCfd</code>.
     * @throws java.lang.Exception
     */
    public SDataCfd createDataCfd() throws Exception {
        boolean addXmlHeader = true;

        if (moAuxCfdRootElement == null) {
            // XML, XML name and XML date are supposedly provided
            addXmlHeader = false;   // it is assumed that XML already has its header
        }
        else if (moAuxCfdRootElement instanceof cfd.ver2.DElementComprobante) {
            msDocXml = ((cfd.ver2.DElementComprobante) moAuxCfdRootElement).getElementForXml();
            msDocXmlName = DCfd.createFileName((cfd.ver2.DElementComprobante) moAuxCfdRootElement) + ".xml";
            mtXmlDatetime = ((cfd.ver2.DElementComprobante) moAuxCfdRootElement).getAttFecha().getDatetime();
        }
        else if (moAuxCfdRootElement instanceof cfd.ver32.DElementComprobante) {
            msDocXml = ((cfd.ver32.DElementComprobante) moAuxCfdRootElement).getElementForXml();
            msDocXmlName = DCfd.createFileName((cfd.ver32.DElementComprobante) moAuxCfdRootElement) + ".xml";
            mtXmlDatetime = ((cfd.ver32.DElementComprobante) moAuxCfdRootElement).getAttFecha().getDatetime();
        }
        else if (moAuxCfdRootElement instanceof cfd.ver33.DElementComprobante) {
            msDocXml = ((cfd.ver33.DElementComprobante) moAuxCfdRootElement).getElementForXml();
            msDocXmlName = DCfd.createFileName((cfd.ver33.DElementComprobante) moAuxCfdRootElement) + ".xml";
            mtXmlDatetime = ((cfd.ver33.DElementComprobante) moAuxCfdRootElement).getAttFecha().getDatetime();
        }
        else {
            throw new Exception("Not supported CFD version!");
        }

        if (addXmlHeader) {
            msDocXml = SXmlConsts.XML_HEADER + msDocXml;
        }

        SDataCfd cfd = new SDataCfd();
        cfd.setPkCfdId(mnCfdId);
        cfd.setSeries(msCfdSeries);
        cfd.setNumber(mnCfdNumber);
        cfd.setTimestamp(mtXmlDatetime);
        cfd.setCertNumber(msCfdCertNumber);
        cfd.setStringSigned(msCfdStringSigned);
        cfd.setSignature(msCfdSignature);
        cfd.setDocXmlUuid(msDocXmlUuid);
        cfd.setDocXml(msDocXml);
        cfd.setDocXmlName(msDocXmlName);
        cfd.setDocXmlRfcEmi(msXmlRfcEmisor);
        cfd.setDocXmlRfcRec(msXmlRfcReceptor);
        cfd.setDocXmlTot(mdXmlTotalCy);
        //cfd...
        cfd.setUuid(msCfdUuid);
        //cfd...
        cfd.setAcknowledgmentCancellationXml(msAcknowledgmentCancellationXml);
        //cfd...
        cfd.setIsConsistent(mbIsCfdConsistent);
        cfd.setFkCfdTypeId(mnFkCfdTypeId);
        cfd.setFkXmlTypeId(mnFkXmlTypeId);
        cfd.setFkXmlStatusId(mnFkXmlStatusId);
        cfd.setFkXmlDeliveryTypeId(mnFkXmlDeliveryTypeId);
        cfd.setFkXmlDeliveryStatusId(mnFkXmlDeliveryStatusId);
        cfd.setFkCompanyBranchId_n(mnFkCompanyBranchId);
        cfd.setFkDpsYearId_n(mnDpsYearId);
        cfd.setFkDpsDocId_n(mnDpsDocId);
        cfd.setFkRecordYearId_n(mnRecordEntryYearId);
        cfd.setFkRecordPeriodId_n(mnRecordEntryPeriodId);
        cfd.setFkRecordBookkeepingCenterId_n(mnRecordEntryBookkeepingCenterId);
        cfd.setFkRecordRecordTypeId_n(msRecordEntryRecordTypeId);
        cfd.setFkRecordNumberId_n(mnRecordEntryNumberId);
        cfd.setFkRecordEntryId_n(mnRecordEntryEntryId);
        cfd.setFkPayrollPayrollId_n(mnPayrollPayrollId);
        cfd.setFkPayrollEmployeeId_n(mnPayrollEmployeeId);
        cfd.setFkPayrollBizPartnerId_n(mnPayrollBizPartnerId);
        cfd.setFkPayrollReceiptPayrollId_n(mnPayrollReceiptPayrollId);
        cfd.setFkPayrollReceiptEmployeeId_n(mnPayrollReceiptEmployeeId);
        cfd.setFkPayrollReceiptIssueId_n(mnPayrollReceiptIssueId);
        cfd.setFkUserProcessingId(mnFkUserDeliveryId);
        cfd.setFkUserDeliveryId(mnFkUserDeliveryId);

        cfd.setAuxRfcEmisor(msXmlRfcEmisor);
        cfd.setAuxRfcReceptor(msXmlRfcReceptor);
        cfd.setAuxTotalCy(mdXmlTotalCy);
        
        return cfd;
    }
}

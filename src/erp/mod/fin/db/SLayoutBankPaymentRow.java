/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.mod.SModSysConsts;
import java.util.Date;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;

/**
 * Abstraction of a grid row when paying (doing the accounting of) bank layouts.
 * 
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores, Isabel Servín
 */
public class SLayoutBankPaymentRow implements SGridRow {

    protected SGuiClient miClient;
    protected int mnBizPartnerId;
    protected int mnBizPartnerBranchId;
    protected int mnBizPartnerBranchBankAccountId;
    protected String msBizPartner;
    protected String msBizPartnerKey;
    protected double mdPayment;
    protected double mdPaymentCur;
    protected int mnCurrencyId;
    protected double mdExchangeRate;
    protected String msPayerAccountCurrencyKey;
    protected String msBeneficiaryAccountBankName;
    protected String msBeneficiaryAccountNumber;
    protected String msBeneficiaryAccountNumberShort;
    protected String msAgreement;
    protected String msAgreementReference;
    protected String msAgreementConceptCie;
    protected String msRecordPeriod;
    protected String msRecordBkc;
    protected String msRecordCob;
    protected String msRecordNumber;
    protected Date mtRecordDate;
    protected boolean mbForPayment;
    protected boolean mbPayed;
    
    protected SLayoutBankPayment moLayoutBankPayment;
    protected SLayoutBankRecordKey moLayoutBankRecordKey;
    protected SLayoutBankRecordKey moLayoutBankRecordKeyOld;

    public SLayoutBankPaymentRow(SGuiClient client) {
        miClient = client;
        reset();
    }

    public void reset() {
        mnBizPartnerId = 0;
        mnBizPartnerBranchId = 0;
        mnBizPartnerBranchBankAccountId = 0;
        msBizPartner = "";
        msBizPartnerKey = "";
        mdPayment = 0;
        mdPaymentCur = 0;
        mnCurrencyId = SModSysConsts.CFGU_CUR_MXN;
        mdExchangeRate = 1d;
        msPayerAccountCurrencyKey = "";
        msBeneficiaryAccountBankName = "";
        msBeneficiaryAccountNumber = "";
        msBeneficiaryAccountNumberShort = "";
        msAgreement = "";
        msAgreementReference = "";
        msAgreementConceptCie = "";
        msRecordPeriod = "";
        msRecordBkc = "";
        msRecordCob = "";
        msRecordNumber = "";
        mtRecordDate = null;
        mbForPayment = false;
        mbPayed = false;
        
        moLayoutBankPayment = null;
        moLayoutBankRecordKey = null;
        moLayoutBankRecordKeyOld = null;
    }
    
    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setBizPartnerBranchId(int n) { mnBizPartnerBranchId = n; }
    public void setBizPartnerBranchAccountId(int n) { mnBizPartnerBranchBankAccountId = n; }
    public void setBizPartner(String s) { msBizPartner = s; }
    public void setBizPartnerKey(String s) { msBizPartnerKey = s; }
    public void setPayment(double d) { mdPayment = d; }
    public void setPaymentCur(double d) { mdPaymentCur = d; }
    public void setCurrencyId(int n) { mnCurrencyId = n; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setPayerAccountCurrencyKey(String s) { msPayerAccountCurrencyKey = s; }
    public void seBeneficiaryAccountBankName(String s) { msBeneficiaryAccountBankName = s; }
    public void setBeneficiaryAccountNumber(String s) { msBeneficiaryAccountNumber = s; }
    public void setBeneficiaryAccountNumberShort(String s) { msBeneficiaryAccountNumberShort = s; }
    public void setAgreement(String s) { msAgreement = s; }
    public void setAgreementReference(String s) { msAgreementReference = s; }
    public void setAgreementConceptCie(String s) { msAgreementConceptCie = s; }
    public void setRecordPeriod(String s) { msRecordPeriod = s; }
    public void setRecordBkc(String s) { msRecordBkc = s; }
    public void setRecordCob(String s) { msRecordCob = s; }
    public void setRecordNumber(String s) { msRecordNumber = s; }
    public void setRecordDate(Date t) { mtRecordDate = t; }
    public void setForPayment(boolean b) { mbForPayment = b; }
    public void setPayed(boolean b) { mbPayed = b; }
    
    public void setLayoutBankPayment(SLayoutBankPayment o) { moLayoutBankPayment = o; }
    public void setLayoutBankRecordKey(SLayoutBankRecordKey o) { moLayoutBankRecordKey = o; }
    public void setLayoutBankRecordKeyOld(SLayoutBankRecordKey o) { moLayoutBankRecordKeyOld = o; }
    
    public int getBizPartnerId() { return mnBizPartnerId; }
    public int getBizPartnerBranchId() { return mnBizPartnerBranchId; }
    public int getBizPartnerBranchAccountId() { return mnBizPartnerBranchBankAccountId; }
    public String getBizPartner() { return msBizPartner; }
    public String getBizPartnerKey() { return msBizPartnerKey; }
    public double getPayment() { return mdPayment; }
    public double getPaymentCur() { return mdPaymentCur; }
    public int getCurrencyId() { return mnCurrencyId; }
    public double getExchangeRate() { return mdExchangeRate; }
    public String getPayerAccountCurrencyKey() { return msPayerAccountCurrencyKey; }
    public String getBeneficiaryAccountBankName() { return msBeneficiaryAccountBankName; }
    public String getBeneficiaryAccountNumber() { return msBeneficiaryAccountNumber; }
    public String getBeneficiaryAccountNumberShort() { return msBeneficiaryAccountNumberShort; }
    public String getAgreement() { return msAgreement; }
    public String getAgreementReference() { return msAgreementReference; }
    public String getAgreementConceptCie() { return msAgreementConceptCie; }
    public String getRecordPeriod() { return msRecordPeriod; }
    public String getRecordBkc() { return msRecordBkc; }
    public String getRecordCob() { return msRecordCob; }
    public String getRecordNumber() { return msRecordNumber; }
    public Date getRecordDate() { return mtRecordDate; }
    public boolean isForPayment() { return mbForPayment; }
    public boolean isPayed() { return mbPayed; }
    
    public SLayoutBankPayment getLayoutBankPayment() { return moLayoutBankPayment; }
    public SLayoutBankRecordKey getLayoutBankRecordKey() { return moLayoutBankRecordKey; }
    public SLayoutBankRecordKey getLayoutBankRecordKeyOld() { return moLayoutBankRecordKeyOld; }

    public void setPrimaryKey(int[] pk) {
        mnBizPartnerId = pk[0];
        mnBizPartnerBranchId = pk[1];
        mnBizPartnerBranchBankAccountId = pk[1];
    }
    
    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnBizPartnerId, mnBizPartnerBranchId, mnBizPartnerBranchBankAccountId };
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = msBizPartner;
                break;
            case 1:
                value = msBizPartnerKey;
                break;
            case 2:
                value = mbForPayment;
                break;
            case 3:
                value = mdPayment;
                break;
            case 4:
                value = msPayerAccountCurrencyKey;
                break;
            case 5:
                value = mdExchangeRate;
                break;
            case 6:
                value = msAgreement == null && !msAgreement.isEmpty() ? msAgreement : msBeneficiaryAccountNumber;
                break;
            case 7:
                value = msAgreementReference;
                break;
            case 8:
                value = msAgreementConceptCie;
                break;
            case 9:
                value = msRecordPeriod;
                break;
            case 10:
                value = msRecordBkc;
                break;
            case 11:
                value = msRecordCob;
                break;
            case 12:
                value = msRecordNumber;
                break;
            case 13:
                value = mtRecordDate;
                break;
            default:
        }
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch (row) {
            case 0:
            case 1:
                break;
            case 2:
                mbForPayment = (boolean) value;
                break;
            case 3:
            case 4:
            case 5:   
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                break;
            default:
        }
    }
    
    @Override
    public SLayoutBankPaymentRow clone() throws CloneNotSupportedException {
        SLayoutBankPaymentRow clone = new SLayoutBankPaymentRow(miClient);
        
        clone.setBizPartnerId(this.getBizPartnerId());
        clone.setBizPartnerBranchId(this.getBizPartnerBranchId());
        clone.setBizPartnerBranchAccountId(this.getBizPartnerBranchAccountId());
        clone.setBizPartner(this.getBizPartner());
        clone.setBizPartnerKey(this.getBizPartnerKey());
        clone.setPayment(this.getPayment());
        clone.setPaymentCur(this.getPaymentCur());
        clone.setCurrencyId(this.getCurrencyId());
        clone.setExchangeRate(this.getExchangeRate());
        clone.setPayerAccountCurrencyKey(this.getPayerAccountCurrencyKey());
        clone.seBeneficiaryAccountBankName(this.getBeneficiaryAccountBankName());
        clone.setBeneficiaryAccountNumber(this.getBeneficiaryAccountNumber());
        clone.setBeneficiaryAccountNumberShort(this.getBeneficiaryAccountNumberShort());
        clone.setAgreement(this.getAgreement());
        clone.setAgreementReference(this.getAgreementReference());
        clone.setAgreementConceptCie(this.getAgreementConceptCie());
        clone.setRecordPeriod(this.getRecordPeriod());
        clone.setRecordBkc(this.getRecordBkc());
        clone.setRecordCob(this.getRecordCob());
        clone.setRecordNumber(this.getRecordNumber());
        clone.setRecordDate(this.getRecordDate());
        clone.setForPayment(this.isForPayment());
        clone.setPayed(this.isPayed());
        
        if (this.getLayoutBankPayment() != null) {
            clone.setLayoutBankPayment(this.getLayoutBankPayment().clone());
        }
        
        if (this.getLayoutBankRecordKey() != null) {
            clone.setLayoutBankRecordKey(this.getLayoutBankRecordKey().clone());
        }
        
        if (this.getLayoutBankRecordKeyOld() != null) {
            clone.setLayoutBankRecordKeyOld(this.getLayoutBankRecordKeyOld().clone());
        }
        
        return clone;
    }
}

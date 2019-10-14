/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.mod.SModSysConsts;
import sa.lib.SLibConsts;

/**
 * Abstraction of a payment for XML representation of bank layouts.
 * 
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores
 */
public class SLayoutBankXmlRow {
    
    protected int mnTransactionType;
    protected int mnDpsYearId;
    protected int mnDpsDocId;
    protected double mdAmount;
    protected double mdAmountCy;
    protected double mdAmountPayed;
    protected double mdExchangeRate;
    protected int mnCurrencyId;
    protected int mnBizPartnerId;
    protected int mnBizPartnerBranchId;
    protected int mnBizPartnerBranchBankAccountId;
    protected int mnHsbcFiscalVoucher;
    protected int mnHsbcBankCode;
    protected String msHsbcAccountType;
    protected String msHsbcFiscalIdDebit;
    protected String msHsbcFiscalIdCredit;
    protected String msConcept;
    protected String msDescription;
    protected String msReference;
    protected String msAgreement;
    protected String msAgreementReference;
    protected String msConceptCie;
    protected String msSantanderBankCode;
    protected String msBajioBankCode;
    protected String msBajioBankNick;
    protected int mnBankKey;
    protected int mnRecYearId;
    protected int mnRecPeriodId;
    protected int mnRecBookkeepingCenterId;
    protected String msRecRecordTypeId;
    protected int mnRecNumberId;
    protected int mnBookkeepingYearId;
    protected int mnBookkeepingNumberId;
    protected String msReferenceRecord;
    protected String msObservations;
    protected String msEmail;
    protected boolean mbPayed;

    public SLayoutBankXmlRow() {
        mnTransactionType = 0;
        mnDpsYearId = 0;
        mnDpsDocId = 0;
        mdAmount = 0;
        mdAmountCy = 0;
        mdAmountPayed = 0;
        mdExchangeRate = 0;
        mnCurrencyId = 0;
        mnBizPartnerId = 0;
        mnBizPartnerBranchId = 0;
        mnBizPartnerBranchBankAccountId = 0;
        mnHsbcFiscalVoucher = 0;
        mnHsbcBankCode = 0;
        msHsbcAccountType = "";
        msHsbcFiscalIdDebit = "";
        msHsbcFiscalIdCredit = "";
        msConcept = "";
        msDescription = "";
        msReference = "";
        msAgreement = "";
        msAgreementReference = "";
        msConceptCie = "";
        msSantanderBankCode = "";
        msBajioBankCode = "";
        msBajioBankNick = "";
        mnBankKey = 0;
        mnRecYearId = 0;
        mnRecPeriodId = 0;
        mnRecBookkeepingCenterId = 0;
        msRecRecordTypeId = "";
        mnRecNumberId = 0;
        mnBookkeepingYearId = 0;
        mnBookkeepingNumberId = 0;
        msReferenceRecord = "";
        msObservations = "";
        msEmail = "";
        mbPayed = false;
    }

    public void setTransactionType(int n) { mnTransactionType = n; }
    public void setDpsYearId(int n) { mnDpsYearId = n; }
    public void setDpsDocId(int n) { mnDpsDocId = n; }
    public void setAmount(double d) { mdAmount = d; }
    public void setAmountCy(double d) { mdAmountCy = d; }
    public void setAmountPayed(double d) { mdAmountPayed = d; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setCurrencyId(int n) { mnCurrencyId = n; }
    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setBizPartnerBranchId(int n) { mnBizPartnerBranchId = n; }
    public void setBizPartnerBranchAccountId(int n) { mnBizPartnerBranchBankAccountId = n; }
    public void setHsbcFiscalVoucher(int n) { mnHsbcFiscalVoucher = n; }
    public void setHsbcBankCode(int n) { mnHsbcBankCode = n; }
    public void setHsbcAccountType(String s) { msHsbcAccountType = s; }
    public void setHsbcFiscalIdDebit(String s) { msHsbcFiscalIdDebit = s; }
    public void setHsbcFiscalIdCredit(String s) { msHsbcFiscalIdCredit = s; }
    public void setConcept(String s) { msConcept = s; }
    public void setDescription(String s) { msDescription = s; }
    public void setReference(String s) { msReference = s; }
    public void setAgreement(String s) { msAgreement = s; }
    public void setAgreementReference(String s) { msAgreementReference = s; }
    public void setConceptCie(String s) { msConceptCie = s; }
    public void setSantanderBankCode(String s) { msSantanderBankCode = s; }
    public void setBajioBankCode(String s) { msBajioBankCode = s; }
    public void setBajioBankNick(String s) { msBajioBankNick = s; }
    public void setBankKey(int n) { mnBankKey = n; }
    public void setRecYearId(int n) { mnRecYearId = n; }
    public void setRecPeriodId(int n) { mnRecPeriodId = n; }
    public void setRecBookkeepingCenterId(int n) { mnRecBookkeepingCenterId = n; }
    public void setRecRecordTypeId(String s) { msRecRecordTypeId = s; }
    public void setRecNumberId(int n) { mnRecNumberId = n; }
    public void setBookkeepingYearId(int n) { mnBookkeepingYearId = n; }
    public void setBookkeepingNumberId(int n) { mnBookkeepingNumberId = n; }
    public void setReferenceRecord(String s) { msReferenceRecord = s; }
    public void setObservations(String s) { msObservations = s; }
    public void setEmail(String s) { msEmail = s; }
    public void setPayed(boolean b) { mbPayed = b; }
    
    public int getTransactionType() { return mnTransactionType; }
    public int getDpsYearId() { return mnDpsYearId; }
    public int getDpsDocId() { return mnDpsDocId; }
    public double getAmount() { return mdAmount; }
    public double getAmountCy() { return mdAmountCy; }
    public double getAmountPayed() { return mdAmountPayed; }
    public double getExchangeRate() { return mdExchangeRate; }
    public int getCurrencyId() { return mnCurrencyId; }
    public int getBizPartnerId() { return mnBizPartnerId; }
    public int getBizPartnerBranchId() { return mnBizPartnerBranchId; }
    public int getBizPartnerBranchAccountId() { return mnBizPartnerBranchBankAccountId; }
    public int getHsbcFiscalVoucher() { return mnHsbcFiscalVoucher; }
    public int getHsbcBankCode() { return mnHsbcBankCode; }
    public String getHsbcAccountType() { return msHsbcAccountType; }
    public String getHsbcFiscalIdDebit() { return msHsbcFiscalIdDebit; }
    public String getHsbcFiscalIdCredit() { return msHsbcFiscalIdCredit; }
    public String getConcept() { return msConcept; }
    public String getDescription() { return msDescription; }
    public String getReference() { return msReference; }
    public String getAgreement() { return msAgreement; }
    public String getAgreementReference() { return msAgreementReference; }
    public String getConceptCie() { return msConceptCie; }
    public String getSantanderBankCode() { return msSantanderBankCode; }
    public String getBajioBankCode() { return msBajioBankCode; }
    public String getBajioBankNick() { return msBajioBankNick; }
    public int getBankKey() { return mnBankKey; }
    public int getRecYearId() { return mnRecYearId; }
    public int getRecPeriodId() { return mnRecPeriodId; }
    public int getRecBookkeepingCenterId() { return mnRecBookkeepingCenterId; }
    public String getRecRecordTypeId() { return msRecRecordTypeId; }
    public int getRecNumberId() { return mnRecNumberId; }
    public int getBookkeepingYearId() { return mnBookkeepingYearId; }
    public int getBookkeepingNumberId() { return mnBookkeepingNumberId; }
    public String getReferenceRecord() { return msReferenceRecord; }
    public String getObservations() { return msObservations; }
    public String getEmail() { return msEmail; }
    public boolean isPayed() { return mbPayed; }
    
    public int[] getPrimaryKey() {
        int[] key = new int[] { SLibConsts.UNDEFINED };
        
        if (mnTransactionType == SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY) {
            key = new int[] { mnDpsYearId, mnDpsDocId };
        }
        else if (mnTransactionType == SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY) {
            key = new int[] { mnBizPartnerId };
        }
        
        return key;
    }
    
    public Object[] getRecordKey() { return new Object[] { mnRecYearId, mnRecPeriodId, mnRecBookkeepingCenterId, msRecRecordTypeId, mnRecNumberId }; }
    
    public int[] getBookkeepingNumberKey() { return mnBookkeepingYearId == 0 && mnBookkeepingNumberId == 0 ? null : new int[] { mnBookkeepingYearId, mnBookkeepingNumberId }; }
    
    public SLayoutBankXmlRow clone() {
        SLayoutBankXmlRow clone = new SLayoutBankXmlRow();
        
        clone.setTransactionType(this.getTransactionType());
        clone.setDpsYearId(this.getDpsYearId());
        clone.setDpsDocId(this.getDpsDocId());
        clone.setAmount(this.getAmount());
        clone.setAmountCy(this.getAmountCy());
        clone.setAmountPayed(this.getAmountPayed());
        clone.setExchangeRate(this.getExchangeRate());
        clone.setCurrencyId(this.getCurrencyId());
        clone.setBizPartnerId(this.getBizPartnerId());
        clone.setBizPartnerBranchId(this.getBizPartnerBranchId());
        clone.setBizPartnerBranchAccountId(this.getBizPartnerBranchAccountId());
        clone.setHsbcFiscalVoucher(this.getHsbcFiscalVoucher());
        clone.setHsbcBankCode(this.getHsbcBankCode());
        clone.setHsbcAccountType(this.getHsbcAccountType());
        clone.setHsbcFiscalIdDebit(this.getHsbcFiscalIdDebit());
        clone.setHsbcFiscalIdCredit(this.getHsbcFiscalIdCredit());
        clone.setConcept(this.getConcept());
        clone.setDescription(this.getDescription());
        clone.setReference(this.getReference());
        clone.setAgreement(this.getAgreement());
        clone.setAgreementReference(this.getAgreementReference());
        clone.setConceptCie(this.getConceptCie());
        clone.setSantanderBankCode(this.getSantanderBankCode());
        clone.setBajioBankCode(this.getBajioBankCode());
        clone.setBajioBankNick(this.getBajioBankNick());
        clone.setBankKey(this.getBankKey());
        clone.setRecYearId(this.getRecYearId());
        clone.setRecPeriodId(this.getRecPeriodId());
        clone.setRecBookkeepingCenterId(this.getRecBookkeepingCenterId());
        clone.setRecRecordTypeId(this.getRecRecordTypeId());
        clone.setRecNumberId(this.getRecNumberId());
        clone.setBookkeepingYearId(this.getBookkeepingYearId());
        clone.setBookkeepingNumberId(this.getBookkeepingNumberId());
        clone.setReferenceRecord(this.getReferenceRecord());
        clone.setObservations(this.getObservations());
        clone.setEmail(this.getEmail());
        clone.setPayed(this.isPayed());
        
        return clone;
    }
}

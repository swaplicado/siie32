/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.mod.SModSysConsts;
import sa.lib.SLibConsts;

/**
 *
 * @author Juan Barajas, Alfredo Pérez
 */
public class SLayoutBankXmlRow {
    
    protected int mnLayoutXmlRowType;
    protected int mnDpsYear;
    protected int mnDpsDoc;
    protected double mdAmount;
    protected double mdAmountCy;
    protected double mdAmountPayed;
    protected double mdAmountPayedCy;
    protected double mdExchangeRate;
    protected int mnCurrencyId;
    protected boolean mbIsToPayed;
    protected int mnBizPartner;
    protected int mnBizPartnerBranch;
    protected int mnBizPartnerBranchBankAccount;
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
    protected int mnRecYear;
    protected int mnRecPeriod;
    protected int mnRecBookkeepingCenter;
    protected String msRecRecordType;
    protected int mnRecNumber;
    protected int mnBookkeepingYear;
    protected int mnBookkeepingNumber;
    protected String msReferenceRecord;
    protected String msObservations;

    public SLayoutBankXmlRow() {
        mnLayoutXmlRowType = 0;
        mnDpsYear = 0;
        mnDpsDoc = 0;
        mdAmount = 0;
        mdAmountCy = 0;
        mdAmountPayed = 0;
        mdExchangeRate = 0;
        mnCurrencyId = 0;
        mbIsToPayed = false;
        mnBizPartner = 0;
        mnBizPartnerBranch = 0;
        mnBizPartnerBranchBankAccount = 0;
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
        mnRecYear = 0;
        mnRecPeriod = 0;
        mnRecBookkeepingCenter = 0;
        msRecRecordType = "";
        mnRecNumber = 0;
        mnBookkeepingYear = 0;
        mnBookkeepingNumber = 0;
        msReferenceRecord = "";
        msObservations = "";
    }

    public void setLayoutXmlRowType(int n) { mnLayoutXmlRowType = n; }
    public void setDpsYear(int n) { mnDpsYear = n; }
    public void setDpsDoc(int n) { mnDpsDoc = n; }
    public void setAmount(double d) { mdAmount = d; }
    public void setAmountCy(double d) { mdAmountCy = d; }
    public void setAmountPayed(double d) { mdAmountPayed = d; }
    public void setAmountPayedCy(double d) { mdAmountPayedCy = d; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setCurrencyId(int n) { mnCurrencyId = n; }
    public void setIsToPayed(boolean b) { mbIsToPayed = b; }
    public void setBizPartner(int n) { mnBizPartner = n; }
    public void setBizPartnerBranch(int n) { mnBizPartnerBranch = n; }
    public void setBizPartnerBranchAccount(int n) { mnBizPartnerBranchBankAccount = n; }
    public void setHsbcFiscalVoucher(int n) { mnHsbcFiscalVoucher = n; }
    public void setHsbcBankCode(int n) { mnHsbcBankCode = n; }
    public void setHsbcFiscalIdDebit(String s) { msHsbcFiscalIdDebit = s; }
    public void setHsbcFiscalIdCredit(String s) { msHsbcFiscalIdCredit = s; }
    public void setHsbcAccountType(String s) { msHsbcAccountType = s; }
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
    public void setRecYear(int n) { mnRecYear = n; }
    public void setRecPeriod(int n) { mnRecPeriod = n; }
    public void setRecBookkeepingCenter(int n) { mnRecBookkeepingCenter = n; }
    public void setRecRecordType(String s) { msRecRecordType = s; }
    public void setRecNumber(int n) { mnRecNumber = n; }
    public void setBookkeepingYear(int n) { mnBookkeepingYear = n; }
    public void setBookkeepingNumber(int n) { mnBookkeepingNumber = n; }
    public void setReferenceRecord(String s) { msReferenceRecord = s; }
    public void setObservations(String s) { msObservations = s; }
    
    public int[] getPrimaryKey() {
        int[] key = new int[] { SLibConsts.UNDEFINED };
        
        if (mnLayoutXmlRowType == SModSysConsts.FIN_LAY_BANK_DPS) {
            key = new int[] { mnDpsYear, mnDpsDoc };
        }
        else if (mnLayoutXmlRowType == SModSysConsts.FIN_LAY_BANK_ADV) {
            key = new int[] { mnBizPartner };
        }
        
        return key;
    }
    
    public int getLayoutXmlRowType() { return mnLayoutXmlRowType; }
    public int getDpsYear() { return mnDpsYear; }
    public int getDpsDoc() { return mnDpsDoc; }
    public double getAmount() { return mdAmount; }
    public double getAmountCy() { return mdAmountCy; }
    public double getAmountPayed() { return mdAmountPayed; }
    public double getAmountPayedCy() { return mdAmountPayedCy; }
    public double getExchangeRate() { return mdExchangeRate; }
    public int getCurrencyId() { return mnCurrencyId; }
    public boolean getIsToPayed() { return mbIsToPayed; }
    public int getBizPartner() { return mnBizPartner; }
    public int getBizPartnerBranch() { return mnBizPartnerBranch; }
    public int getBizPartnerBranchAccount() { return mnBizPartnerBranchBankAccount; }
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
    public int getRecYear() { return mnRecYear; }
    public int getRecPeriod() { return mnRecPeriod; }
    public int getRecBookkeepingCenter() { return mnRecBookkeepingCenter; }
    public String getRecRecordType() { return msRecRecordType; }
    public int getRecNumber() { return mnRecNumber; }
    public int getBookkeepingYear() { return mnBookkeepingYear; }
    public int getBookkeepingNumber() { return mnBookkeepingNumber; }
    public String getReferenceRecord() { return msReferenceRecord; }
    public String getObservations() { return msObservations; }
    
    public SLayoutBankXmlRow clone() {
        SLayoutBankXmlRow layoutBankXmlRow = new SLayoutBankXmlRow();
        
        layoutBankXmlRow.setLayoutXmlRowType(this.getLayoutXmlRowType());
        layoutBankXmlRow.setDpsYear(this.getDpsYear());
        layoutBankXmlRow.setDpsDoc(this.getDpsDoc());
        layoutBankXmlRow.setAmount(this.getAmount());
        layoutBankXmlRow.setAmountCy(this.getAmountCy());
        layoutBankXmlRow.setAmountPayed(this.getAmountPayed());
        layoutBankXmlRow.setAmountPayedCy(this.getAmountPayedCy());
        layoutBankXmlRow.setExchangeRate(this.getExchangeRate());
        layoutBankXmlRow.setCurrencyId(this.getCurrencyId());
        layoutBankXmlRow.setIsToPayed(this.getIsToPayed());
        layoutBankXmlRow.setBizPartner(this.getBizPartner());
        layoutBankXmlRow.setBizPartnerBranch(this.getBizPartnerBranch());
        layoutBankXmlRow.setBizPartnerBranchAccount(this.getBizPartnerBranchAccount());
        layoutBankXmlRow.setHsbcFiscalVoucher(this.getHsbcFiscalVoucher());
        layoutBankXmlRow.setHsbcBankCode(this.getHsbcBankCode());
        layoutBankXmlRow.setHsbcFiscalIdDebit(this.getHsbcFiscalIdDebit());
        layoutBankXmlRow.setHsbcFiscalIdCredit(this.getHsbcFiscalIdCredit());
        layoutBankXmlRow.setHsbcAccountType(this.getHsbcAccountType());
        layoutBankXmlRow.setConcept(this.getConcept());
        layoutBankXmlRow.setDescription(this.getDescription());
        layoutBankXmlRow.setReference(this.getReference());
        layoutBankXmlRow.setAgreement(this.getAgreement());
        layoutBankXmlRow.setAgreementReference(this.getAgreementReference());
        layoutBankXmlRow.setConceptCie(this.getConceptCie());
        layoutBankXmlRow.setSantanderBankCode(this.getSantanderBankCode());
        layoutBankXmlRow.setBajioBankCode(this.getBajioBankCode());
        layoutBankXmlRow.setBajioBankNick(this.getBajioBankNick());
        layoutBankXmlRow.setBankKey(this.getBankKey());
        layoutBankXmlRow.setRecYear(this.getRecYear());
        layoutBankXmlRow.setRecPeriod(this.getRecPeriod());
        layoutBankXmlRow.setRecBookkeepingCenter(this.getRecBookkeepingCenter());
        layoutBankXmlRow.setRecRecordType(this.getRecRecordType());
        layoutBankXmlRow.setRecNumber(this.getRecNumber());
        layoutBankXmlRow.setBookkeepingYear(this.getBookkeepingYear());
        layoutBankXmlRow.setBookkeepingNumber(this.getBookkeepingNumber());
        layoutBankXmlRow.setReferenceRecord(this.getReferenceRecord());
        layoutBankXmlRow.setObservations(this.getObservations());
        
        return layoutBankXmlRow;
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

/**
 * Abstraction of a payment for text representation of bank layouts.
 * 
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores
 */
public class SLayoutBankPaymentText {

    protected double mdTotalAmount;
    protected String msReference;
    protected String msConcept;
    protected String msDescription;
    protected int mnBizPartnerId;
    protected String msBizPartner;
    protected String msAccountCredit;
    protected String msAccountBranchCredit;
    protected String msAccountDebit;
    protected String msAccountBranchDebit;
    protected int mnCurrencyId;
    protected String msAgreement;
    protected String msAgreementReference;
    protected String msConceptCie;
    protected int mnHsbcFiscalVoucher;
    protected int mnHsbcBankCode;
    protected String msHsbcFiscalIdDebit;
    protected String msHsbcFiscalIdCredit;
    protected String msHsbcAccountType;
    protected String msSantanderBankCode;
    protected String msBajioBankCode;
    protected String msBajioBankNick;
    protected int mnBankKey;

    public SLayoutBankPaymentText() {
        mdTotalAmount = 0;
        msReference = "";
        msConcept = "";
        msDescription = "";
        mnBizPartnerId = 0;
        msBizPartner = "";
        msAccountCredit = "";
        msAccountBranchCredit = "";
        msAccountDebit = "";
        msAccountBranchDebit = "";
        mnCurrencyId = 0;
        msAgreement = "";
        msAgreementReference = "";
        msConceptCie = "";
        mnHsbcFiscalVoucher = 0;
        mnHsbcBankCode = 0;
        msHsbcAccountType = "";
        msHsbcFiscalIdDebit = "";
        msHsbcFiscalIdCredit = "";
        msSantanderBankCode = "";
        msBajioBankCode = "";
        msBajioBankNick = "";
        mnBankKey = 0;
    }

    public void setTotalAmount(double d) { mdTotalAmount = d; }
    public void setReference(String s) { msReference = s; }
    public void setConcept(String s) { msConcept = s; }
    public void setDescription(String s) { msDescription = s; }
    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setBizPartner(String s) { msBizPartner = s; }
    public void setAccountCredit(String s) { msAccountCredit = s; }
    public void setAccountBranchCredit(String s) { msAccountBranchCredit = s; }
    public void setAccountDebit(String s) { msAccountDebit = s; }
    public void setAccountBranchDebit(String s) { msAccountBranchDebit = s; }
    public void setCurrencyId(int n) { mnCurrencyId = n; }
    public void setAgreement(String s) { msAgreement = s; }
    public void setAgreementReference(String s) { msAgreementReference = s; }
    public void setConceptCie(String s) { msConceptCie = s; }
    public void setHsbcFiscalVoucher(int n) { mnHsbcFiscalVoucher = n; }
    public void setHsbcBankCode(int n) { mnHsbcBankCode = n; }
    public void setHsbcAccountType(String s) { msHsbcAccountType = s; }
    public void setHsbcFiscalIdDebit(String s) { msHsbcFiscalIdDebit = s; }
    public void setHsbcFiscalIdCredit(String s) { msHsbcFiscalIdCredit = s; }
    public void setSantanderBankCode(String s) { msSantanderBankCode = s; }
    public void setBajioBankCode(String s) { msBajioBankCode = s; }
    public void setBajioBankNick(String s) { msBajioBankNick = s; }
    public void setBankKey(int n) { mnBankKey = n; }

    public double getTotalAmount() { return mdTotalAmount; }
    public String getReference() { return msReference; }
    public String getConcept() { return msConcept; }
    public String getDescription() { return msDescription; }
    public int getBizPartnerId() { return mnBizPartnerId; }
    public String getBizPartner() { return msBizPartner; }
    public String getAccountCredit() { return msAccountCredit; }
    public String getAccountBranchCredit() { return msAccountBranchCredit; }
    public String getAccountDebit() { return msAccountDebit; }
    public String getAccountBranchDebit() { return msAccountBranchDebit; }
    public int getCurrencyId() { return mnCurrencyId; }
    public String getAgreement() { return msAgreement; }
    public String getAgreementReference() { return msAgreementReference; }
    public String getConceptCie() { return msConceptCie; }
    public int getHsbcFiscalVoucher() { return mnHsbcFiscalVoucher; }
    public int getHsbcBankCode() { return mnHsbcBankCode; }
    public String getHsbcAccountType() { return msHsbcAccountType; }
    public String getHsbcFiscalIdDebit() { return msHsbcFiscalIdDebit; }
    public String getHsbcFiscalIdCredit() { return msHsbcFiscalIdCredit; }
    public String getSantanderBankCode() { return msSantanderBankCode; }
    public String getBajioBankCode() { return msBajioBankCode; }
    public String getBajioBankNick() { return msBajioBankNick; }
    public int getBankKey() { return mnBankKey; }
}

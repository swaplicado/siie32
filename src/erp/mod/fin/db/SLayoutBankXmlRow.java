/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.mod.SModSysConsts;
import sa.lib.SLibConsts;

/**
 *
 * @author Juan Barajas
 */
public class SLayoutBankXmlRow {
    
    protected int mnLayoutXmlRowType;
    protected int mnDpsYear;
    protected int mnDpsDoc;
    protected double mdAmount;
    protected double mdAmountPayed;
    protected boolean mbIsToPayed;
    protected int mnBizPartner;
    protected int mnBizPartnerBranch;
    protected int mnBizPartnerBranchBankAccount;
    protected int mnHsbcFiscalVoucher;
    protected int mnHsbcBankCode;
    protected String msHsbcFiscalIdDebit;
    protected String msHsbcFiscalIdCredit;
    protected String msHsbcAccountType;
    protected String msConcept;
    protected String msDescription;
    protected String msReference;
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

    public SLayoutBankXmlRow() {
        mnLayoutXmlRowType = 0;
        mnDpsYear = 0;
        mnDpsDoc = 0;
        mdAmount = 0;
        mdAmountPayed = 0;
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
    }

    public void setLayoutXmlRowType(int n) { mnLayoutXmlRowType = n; }
    public void setDpsYear(int n) { mnDpsYear = n; }
    public void setDpsDoc(int n) { mnDpsDoc = n; }
    public void setAmount(double d) { mdAmount = d; }
    public void setAmountPayed(double d) { mdAmountPayed = d; }
    public void setIsToPayed(boolean b) { mbIsToPayed = b; }
    public void setBizPartner(int n) { mnBizPartner = n; }
    public void setBizPartnerBranch(int n) { mnBizPartnerBranch = n; }
    public void setBizPartnerBranchAccount(int n) { mnBizPartnerBranchBankAccount = n; }
    public void setHsbcFiscalVoucher(int n) { mnHsbcFiscalVoucher = n; }
    public void setHsbcBankCode(int n) { mnHsbcBankCode = n; }
    public void setHsbcAccountType(String s) { msHsbcAccountType = s; }
    public void setHsbcFiscalIdDebit(String s) { msHsbcFiscalIdDebit = s; }
    public void setHsbcFiscalIdCredit(String s) { msHsbcFiscalIdCredit = s; }
    public void setConcept(String s) { msConcept = s; }
    public void setDescription(String s) { msDescription = s; }
    public void setReference(String s) { msReference = s; }
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
    public double getAmountPayed() { return mdAmountPayed; }
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
}
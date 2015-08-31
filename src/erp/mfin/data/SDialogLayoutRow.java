/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.mod.fin.db.SFinRecordLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import sa.lib.gui.SGuiItem;

/**
 *
 * @author Juan Barajas
 */
public class SDialogLayoutRow extends erp.lib.table.STableRow {

    protected erp.client.SClientInterface miClient;
    
    protected int mnLayoutRowType;
    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnBizPartnerId;
    protected int mnBizPartnerBranchId;
    protected int mnBizPartnerBranchBankAccountId;
    protected java.lang.String msBizPartner;
    protected java.lang.String msBizPartnerKey;
    protected java.lang.String msBizPartnerBranch;
    protected java.lang.String msSantanderBankCode;
    protected java.lang.String msBajioBankCode;
    protected java.lang.String msBajioBankAlias;
    protected java.lang.String msTypeDps;
    protected java.lang.String msNumberSer;
    protected java.util.Date mtDate;
    protected java.util.Date mtDateMaturityRo;
    protected java.lang.String msBizPartnerBranchCob;
    protected boolean mbIsForPayment;
    protected boolean mbIsToPayed;
    protected double mdBalance;
    protected double mdBalanceTot;
    protected double mdBalancePayed;
    protected java.lang.String msCurrencyKey;
    protected java.lang.String msAccountCredit;
    protected java.lang.String msEmail;
    protected java.lang.String msBizPartnerCreditFiscalId;
    protected double mdSubTotal;
    protected double mdTaxCharged;
    protected double mdTaxRetained;
    protected double mdTotal;
    protected double mdTotalVat;

    protected java.lang.String msAccountDebit;
    protected java.lang.String msBizPartnerDebitFiscalId;
    protected java.lang.String msAccountType;
    protected java.lang.String msConcept;
    protected java.lang.String msDescription;
    protected java.lang.String msReference;
    protected int mnReferenceNumber;
    protected int mnCf;
    protected int mnApply;
    protected int mnBankKey;
    protected int mnCurrencyId;    
    
    protected ArrayList<SGuiItem> mvAccountCredit2;
    protected Vector<String> mvAccountCredit;
    protected Vector<String> mvAccountCreditAll;
    protected HashMap<String, String> moCodeBankAccountCredit;
    protected HashMap<String, String> moAliasBankAccountCredit;
    
    protected ArrayList<SFinRecordLayout> maFinRecordLayout;

    public SDialogLayoutRow(erp.client.SClientInterface client) {
        miClient = client;
        reset();
    }

    public void reset() {
        mnLayoutRowType = 0;
        mnPkYearId = 0;
        mnPkDocId = 0;
        mnBizPartnerId = 0;
        mnBizPartnerBranchId = 0;
        mnBizPartnerBranchBankAccountId = 0;
        msBizPartner = "";
        msBizPartnerKey = "";
        msBizPartnerBranch = "";
        msSantanderBankCode = "";
        msBajioBankCode = "";
        msBajioBankAlias = "";
        msTypeDps = "";
        msNumberSer = "";
        mtDate = null;
        mtDateMaturityRo = null;
        msBizPartnerBranchCob = "";
        mbIsForPayment = false;
        mbIsToPayed = false;
        mdBalance = 0;
        mdBalanceTot = 0;
        mdBalancePayed = 0;
        msCurrencyKey = "";
        msAccountCredit = "";
        msEmail = "";
        msBizPartnerCreditFiscalId = "";
        mdSubTotal = 0;
        mdTaxCharged = 0;
        mdTaxRetained = 0;
        mdTotal = 0;
        mdTotalVat = 0;

        msAccountDebit = "";
        msBizPartnerDebitFiscalId = "";
        msAccountType = "";
        msConcept = "";
        msDescription = "";
        msReference = "";
        mnReferenceNumber = 0;
        mnCf = 0;
        mnApply = 0;
        mnBankKey = 0;
        mnCurrencyId = 0;
        
        moCodeBankAccountCredit = new HashMap<String, String>();
        moAliasBankAccountCredit = new HashMap<String, String>();
        maFinRecordLayout = new ArrayList<SFinRecordLayout>();
    }

    public void setLayoutRowType(int n) { mnLayoutRowType = n; }
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setBizPartnerBranchId(int n) { mnBizPartnerBranchId = n; }
    public void setBizPartnerBranchAccountId(int n) { mnBizPartnerBranchBankAccountId = n; }
    public void setBizPartner(java.lang.String s) { msBizPartner = s; }
    public void setBizPartnerKey(java.lang.String s) { msBizPartnerKey = s; }
    public void setBizPartnerBranch(String s) { msBizPartnerBranch = s; }
    public void setSantanderBankCode(String s) { msSantanderBankCode = s; }
    public void setBajioBankCode(String s) { msBajioBankCode = s; }
    public void setBajioBankAlias(String s) { msBajioBankAlias = s; }
    public void setTypeDps(String s) { msTypeDps = s; }
    public void setNumberSer(String s) { msNumberSer = s; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setDateMaturityRo(java.util.Date t) { mtDateMaturityRo = t; }
    public void setBizPartnerBranchCob(String s) { msBizPartnerBranchCob = s; }
    public void setIsForPayment(boolean b) { mbIsForPayment = b; }
    public void setIsToPayed(boolean b) { mbIsToPayed = b; }
    public void setBalance(double d) { mdBalance = d; }
    public void setBalanceTot(double d) { mdBalanceTot = d; }
    public void setBalancePayed(double d) { mdBalancePayed = d; }
    public void setCurrencyKey(java.lang.String s) { msCurrencyKey = s; }
    public void setAccountCredit(java.lang.String s) { msAccountCredit = s; }
    public void setEmail(java.lang.String s) { msEmail = s; }
    public void setBizPartnerCreditFiscalId(String s) { msBizPartnerCreditFiscalId = s; }
    public void setSubTotal(double d) { mdSubTotal = d; }
    public void setTaxCharged(double d) { mdTaxCharged = d; }
    public void setTaxRetained(double d) { mdTaxRetained = d; }
    public void setTotal(double d) { mdTotal = d; }
    public void setTotalVat(double d) { mdTotalVat = d; }

    public void setAccountDebit(java.lang.String s) { msAccountDebit = s; }
    public void setBizPartnerDebitFiscalId(String s) { msBizPartnerDebitFiscalId = s; }
    public void setAccType(java.lang.String s) { msAccountType = s; }
    public void setConcept(java.lang.String s) { msConcept = s; }
    public void setDescription(java.lang.String s) { msDescription = s; }
    public void setReference(java.lang.String s) { msReference = s; }
    public void setReferenceNumber(int n) { mnReferenceNumber = n; }
    public void setCf(int n) { mnCf = n; }
    public void setApply(int n) { mnApply = n; }
    public void setBankKey(int n) { mnBankKey = n; }
    public void setCurrencyId(int n) { mnCurrencyId = n; }
    public void setAccountCreditArray2(ArrayList<SGuiItem> v) { mvAccountCredit2 = v; }
    public void setAccountCreditArray(Vector<String> v) { mvAccountCredit = v; }
    public void setAccountCreditArrayAll(Vector<String> v) { mvAccountCreditAll = v; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
    }
    
    public int getLayoutRowType() { return mnLayoutRowType; }
    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getBizPartnerId() { return mnBizPartnerId; }
    public int getBizPartnerBranchId() { return mnBizPartnerBranchId; }
    public int getBizPartnerBranchAccountId() { return mnBizPartnerBranchBankAccountId; }
    public java.lang.String getBizPartner() { return msBizPartner; }
    public java.lang.String getBizPartnerKey() { return msBizPartnerKey; }
    public java.lang.String getBizPartnerBranch() { return msBizPartnerBranch; }
    public java.lang.String getSantanderBankCode() { return msSantanderBankCode; }
    public java.lang.String getBajioBankCode() { return msBajioBankCode; }
    public java.lang.String getBajioBankAlias() { return msBajioBankAlias; }
    public java.lang.String getTypeDps() { return msTypeDps; }
    public java.lang.String getNumberSer() { return msNumberSer; }
    public java.util.Date getDate() { return mtDate; }
    public java.util.Date getDateMaturityRo() { return mtDateMaturityRo; }
    public java.lang.String getBizPartnerBranchCob() { return msBizPartnerBranchCob; }
    public boolean getIsForPayment() { return mbIsForPayment; }
    public boolean getIsToPayed() { return mbIsToPayed; }
    public double getBalance() { return mdBalance; }
    public double getBalanceTot() { return mdBalanceTot; }
    public double getBalancePayed() { return mdBalancePayed; }
    public java.lang.String getCurrencyKey() { return msCurrencyKey; }
    public java.lang.String getAccountCredit() { return msAccountCredit; }
    public java.lang.String getEmail() { return msEmail; }
    public java.lang.String getBizPartnerCreditFiscalId() { return msBizPartnerCreditFiscalId; }
    public double getSubTotal() { return mdSubTotal; }
    public double getTaxCharged() { return mdTaxCharged; }
    public double getTaxRetained() { return mdTaxRetained; }
    public double getTotal() { return mdTotal; }
    public double getTotalVat() { return mdTotalVat; }

    public java.lang.String getAccountDebit() { return msAccountDebit; }
    public java.lang.String getBizPartnerDebitFiscalId() { return msBizPartnerDebitFiscalId; }
    public java.lang.String getAccType() { return msAccountType; }
    public java.lang.String getConcept() { return msConcept; }
    public java.lang.String getDescription() { return msDescription; }
    public java.lang.String getReference() { return msReference; }
    public int getReferenceNumber() { return mnReferenceNumber; }
    public int getCf() { return mnCf; }
    public int getApply() { return mnApply; }
    public int getBankKey() { return mnBankKey; }
    public int getCurrencyId() { return mnCurrencyId; }
    public ArrayList<SGuiItem> getAccountCreditArray2() { return mvAccountCredit2; }
    public Vector<String> getAccountCreditArray() { return mvAccountCredit; }
    public Vector<String> getAccountCreditArrayAll() { return mvAccountCreditAll; }
    public HashMap<String, String> getCodeBankAccountCredit() { return moCodeBankAccountCredit; }
    public HashMap<String, String> getAliasBankAccountCredit() { return moAliasBankAccountCredit; }
    
    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }
    
    public ArrayList<SFinRecordLayout> getFinRecordLayout() { return maFinRecordLayout; }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(msBizPartner);
        mvValues.add(msBizPartnerKey);
        mvValues.add(msBizPartnerBranch);
        mvValues.add(msTypeDps);
        mvValues.add(msNumberSer);
        mvValues.add(mtDate);
        mvValues.add(msBizPartnerBranchCob);
        mvValues.add(mbIsForPayment);
        mvValues.add(mdBalance);
        mvValues.add(mdBalanceTot);
        mvValues.add(msCurrencyKey);
        mvValues.add(msAccountCredit);
        mvValues.add(msEmail);
        mvValues.add(msBizPartnerCreditFiscalId);
        mvValues.add(mdSubTotal);
        mvValues.add(mdTaxCharged);
        mvValues.add(mdTaxRetained);
        mvValues.add(mdTotal);
        mvValues.add(mtDateMaturityRo);
    }
}

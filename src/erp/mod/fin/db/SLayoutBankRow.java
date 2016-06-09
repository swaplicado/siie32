/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.data.SDataConstantsSys;
import erp.mbps.data.SDataBizPartnerBranchBankAccount;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
import java.util.HashMap;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiItem;

/**
 *
 * @author Juan Barajas
 */
public class SLayoutBankRow implements SGridRow {

    protected SGuiClient miClient;
    
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
    protected double mdBalanceTotByBizPartner;
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
    
    protected ArrayList<SDataBizPartnerBranchBankAccount> maBranchBankAccountsCredit;
    protected ArrayList<SGuiItem> maAccountCredit;
    protected HashMap<String, String> moCodeBankAccountCredit;
    protected HashMap<String, String> moAliasBankAccountCredit;

    public SLayoutBankRow(SGuiClient client) {
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
        mdBalanceTotByBizPartner = 0;
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
        
        maBranchBankAccountsCredit = new ArrayList<SDataBizPartnerBranchBankAccount>();
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
    public void setBalanceTotByBizPartner(double d) { mdBalanceTotByBizPartner = d; }
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
    public void setAccountCreditArray(ArrayList<SGuiItem> a) { maAccountCredit = a; }
    
    public void setBranchBankAccountCreditArray(ArrayList<SDataBizPartnerBranchBankAccount> a) { maBranchBankAccountsCredit = a; }
    
    public String getBranchBankAccountCreditNumber(int[] pk, int typeLayout) {
        String account = "";
        
        for (SDataBizPartnerBranchBankAccount bankAccount : maBranchBankAccountsCredit) {
            if (SLibUtils.compareKeys(pk, (int[]) bankAccount.getPrimaryKey())) {
                account = typeLayout == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? bankAccount.getBankAccountNumber() : bankAccount.getBankAccountNumberStd();
                break;
            }
        }
        
        return account;
    }
    
    public int[] getBranchBankAccountCreditId(String account, int typeLayout) {
        int[] pk = null;
        
        for (SDataBizPartnerBranchBankAccount bankAccount : maBranchBankAccountsCredit) {
            if (account.compareTo(typeLayout == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? bankAccount.getBankAccountNumber() : bankAccount.getBankAccountNumberStd()) == 0) {
                pk = (int[]) bankAccount.getPrimaryKey();
                break;
            }
        }
        
        return pk;
    }
    
    public void setPrimaryKey(int[] pk) {
        int[] key = new int[] { SLibConsts.UNDEFINED };
        
        if (mnLayoutRowType == SModSysConsts.FIN_LAY_BANK_DPS) {
            mnPkYearId = pk[0];
            mnPkDocId = pk[1];
        }
        else if (mnLayoutRowType == SModSysConsts.FIN_LAY_BANK_ADV) {
            mnBizPartnerId = pk[0];
        }
        
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
    public double getBalanceTotByBizPartner() { return mdBalanceTotByBizPartner; }
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
    public ArrayList<SGuiItem> getAccountCreditArray() { return maAccountCredit; }
    public HashMap<String, String> getCodeBankAccountCredit() { return moCodeBankAccountCredit; }
    public HashMap<String, String> getAliasBankAccountCredit() { return moAliasBankAccountCredit; }
    
    public ArrayList<SDataBizPartnerBranchBankAccount> getBranchBankAccountCreditArray() { return maBranchBankAccountsCredit; }

    @Override
    public int[] getRowPrimaryKey() {
        int[] key = new int[] { SLibConsts.UNDEFINED };
        
        if (mnLayoutRowType == SModSysConsts.FIN_LAY_BANK_DPS) {
            key = new int[] { mnPkYearId, mnPkDocId };
        }
        else if (mnLayoutRowType == SModSysConsts.FIN_LAY_BANK_ADV) {
            key = new int[] { mnBizPartnerId };
        }
        
        return key;
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

        if (mnLayoutRowType == SModSysConsts.FIN_LAY_BANK_DPS) {
            switch (col) {
                case 0:
                    value = msBizPartner;
                    break;
                case 1:
                    value = msBizPartnerKey;
                    break;
                case 2:
                    value = msTypeDps;
                    break;
                case 3:
                    value = msNumberSer;
                    break;
                case 4:
                    value = mtDate;
                    break;
                case 5:
                    value = msBizPartnerBranchCob;
                    break;
                case 6:
                    value = mbIsForPayment;
                    break;
                case 7:
                    value = mdBalance;
                    break;
                case 8:
                    value = mdBalanceTot;
                    break;
                case 9:
                    value = msCurrencyKey;
                    break;
                case 10:
                    value = msAccountCredit;
                    break;
                case 11:
                    value = msEmail;
                    break;
                case 12:
                    value = msBizPartnerCreditFiscalId;
                    break;
                case 13:
                    value = mdSubTotal;
                    break;
                case 14:
                    value = mdTaxCharged;
                    break;
                case 15:
                    value = mdTaxRetained;
                    break;
                case 16:
                    value = mdTotal;
                    break;
                case 17:
                    value = mtDateMaturityRo;
                    break;
                default:
            }
        }
        else if (mnLayoutRowType == SModSysConsts.FIN_LAY_BANK_ADV) {
            switch (col) {
                case 0:
                    value = msBizPartner;
                    break;
                case 1:
                    value = msBizPartnerKey;
                    break;
                case 2:
                    value = mdBalanceTot;
                    break;
                case 3:
                    value = msCurrencyKey;
                    break;
                case 4:
                    value = msAccountCredit;
                    break;
                case 5:
                    value = msEmail;
                    break;
                case 6:
                    value = msBizPartnerCreditFiscalId;
                    break;
                default:
            }
        }
        

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        if (mnLayoutRowType == SModSysConsts.FIN_LAY_BANK_DPS) {
            switch (row) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    mbIsForPayment = (boolean) value;
                    break;
                case 7:
                    break;
                case 8:
                    mdBalanceTot = (double) value;
                    break;
                case 9:
                    break;
                case 10:
                    if (value == null) {
                        msAccountCredit = "";
                    }
                    else {
                        msAccountCredit = ((SGuiItem) value).getItem();
                        //msAccountCredit = (String) value;
                    }
                    break;
                case 11:
                    msEmail = (String) value;
                    break;
                case 12:
                    break;
                case 13:
                    break;
                case 14:
                    break;
                case 15:
                    break;
                case 16:
                    break;
                case 17:
                    break;
                default:
                    break;
            }
        }
        else if (mnLayoutRowType == SModSysConsts.FIN_LAY_BANK_ADV) {
            switch (row) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    mdBalanceTot = (double) value;
                    break;
                case 3:
                    break;
                case 4:
                    if (value == null) {
                        msAccountCredit = "";
                    }
                    else {
                        msAccountCredit = ((SGuiItem) value).getItem();
                        //msAccountCredit = (String) value;
                    }
                    break;
                case 5:
                    msEmail = (String) value;
                    break;
                case 6:
                    break;
                default:
                    break;
            }
        }
    }
}

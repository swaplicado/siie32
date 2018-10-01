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
 * @author Juan Barajas, Alfredo Pérez
 */
public class SLayoutBankRow implements SGridRow {
    
    public static final int LENGTH_MAX_REF = 20;

    protected SGuiClient miClient;
    
    protected int mnLayoutRowType;
    protected int mnLayoutRowSubType;
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
    protected SMoney moBalance;     // amount in local currency
    protected SMoney moBalanceCy;   // amout in origial currency
    protected SMoney moBalanceTot;
    protected double mdExchangeRate;
    protected double mdBalanceCy;
    protected double mdBalanceTotCy;
    protected double mdBalanceAccountDebit;
    protected double mdBalanceAccountDebitTot;
    
    protected double mdBalanceTotByBizPartner;
    protected double mdBalancePayed;
    protected java.lang.String msCurrencyKey;
    protected java.lang.String msCurrencyKeyCy;
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
    protected java.lang.String msAgreement;
    protected java.lang.String msAgreementReference;
    protected java.lang.String msConceptCie;
    protected int mnReferenceNumber;
    protected int mnCf;
    protected int mnApply;
    protected int mnBankKey;
    protected int mnCurrencyId;
    protected String msReferenceRecord;
    protected String msObservations;
    
    protected ArrayList<SDataBizPartnerBranchBankAccount> maBranchBankAccountCredits;
    protected ArrayList<SGuiItem> maAccountCredits;
    protected ArrayList<SGuiItem> maAgreementReferences;
    protected HashMap<String, String> moCodeBankAccountCredits;
    protected HashMap<String, String> moAliasBankAccountCredits;
    
    public SLayoutBankRow(SGuiClient client) {
        miClient = client;
        reset();
    }

    public void reset() {
        mnLayoutRowType = 0;
        mnLayoutRowSubType = 0;
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
        moBalance = null;
        moBalanceCy = null;
        moBalanceTot = null;
        mdExchangeRate = 0;
        mdBalanceCy = 0;
        mdBalanceTotCy = 0;
        mdBalanceAccountDebit = 0;
        mdBalanceAccountDebitTot = 0;
        mdBalanceTotByBizPartner = 0;
        mdBalancePayed = 0;
        msCurrencyKey = "";
        msCurrencyKeyCy = "";      
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
        msAgreement = "";
        msAgreementReference = "";
        msConceptCie = "";
        mnReferenceNumber = 0;
        mnCf = 0;
        mnApply = 0;
        mnBankKey = 0;
        mnCurrencyId = 0;
        msReferenceRecord = "";
        msObservations = "";
        
        maBranchBankAccountCredits = new ArrayList<>();
        maAccountCredits = new ArrayList<>();
        maAgreementReferences = new ArrayList<>();
        moCodeBankAccountCredits = new HashMap<>();
        moAliasBankAccountCredits = new HashMap<>();
    }

    public void setLayoutRowType(int n) { mnLayoutRowType = n; }
    public void setLayoutRowSubType(int n) { mnLayoutRowSubType = n; }
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
    public void setBalance (SMoney o) { moBalance = o; }
    public void setBalanceCy (SMoney o) { moBalanceCy = o; }
    public void setBalanceTot(SMoney o) { moBalanceTot = o; }
    public void setBalanceTotByBizPartner(double d) { mdBalanceTotByBizPartner = d; }
    public void setBalancePayed(double d) { mdBalancePayed = d; }
    public void setCurrencyKey(java.lang.String s) { msCurrencyKey = s; }
    public void setCurrencyKeyCy(java.lang.String s) { msCurrencyKeyCy = s; }
    public void setAccountCredit(java.lang.String s) { msAccountCredit = s; }
    public void setAgreement(java.lang.String s) { msAgreement= s; }
    public void setAgreementReference(java.lang.String s) { msAgreementReference= s; }
    public void setConceptCie(java.lang.String s) { msConceptCie = s; }
    public void setEmail(java.lang.String s) { msEmail = s; }
    public void setBizPartnerCreditFiscalId(String s) { msBizPartnerCreditFiscalId = s; }
    public void setSubTotal(double d) { mdSubTotal = d; }
    public void setTaxCharged(double d) { mdTaxCharged = d; }
    public void setTaxRetained(double d) { mdTaxRetained = d; }
    public void setTotal(double d) { mdTotal = d; }
    public void setTotalVat(double d) { mdTotalVat = d; }
    public void setExchangeRate(double d) { moBalanceTot.setExchangeRate(d); }
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
    public void setReferenceRecord(String s) { msReferenceRecord = s; }
    public void setObservations(String s) { msObservations = s; }
    
    public void setAccountCreditArray(ArrayList<SGuiItem> a) { maAccountCredits = a; }
    public void setAgreementsReferencesArray(ArrayList<SGuiItem> a) { maAgreementReferences = a; }
    public void setBranchBankAccountCreditArray(ArrayList<SDataBizPartnerBranchBankAccount> a) { maBranchBankAccountCredits = a; }
    
    public int getLayoutRowType() { return mnLayoutRowType; }
    public int getLayoutRowSubType() { return mnLayoutRowSubType; }
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
    public SMoney getBalance() { return moBalance; }
    public SMoney getBalanceCy() { return moBalanceCy; }
    public SMoney getBalanceTot() { return moBalanceTot; }
    public double getBalanceTotByBizPartner() { return mdBalanceTotByBizPartner; }
    public double getBalancePayed() { return mdBalancePayed; }
    public java.lang.String getCurrencyKey() { return msCurrencyKey; }
    public java.lang.String getCurrencyKeyCy() { return msCurrencyKeyCy; }
    public java.lang.String getAccountCredit() { return msAccountCredit; }
    public java.lang.String getAgreement() { return msAgreement; }
    public java.lang.String getAgreementReference() { return msAgreementReference; }
    public java.lang.String getConceptCie() { return msConceptCie; }
    public java.lang.String getEmail() { return msEmail; }
    public java.lang.String getBizPartnerCreditFiscalId() { return msBizPartnerCreditFiscalId; }
    public double getSubTotal() { return mdSubTotal; }
    public double getTaxCharged() { return mdTaxCharged; }
    public double getTaxRetained() { return mdTaxRetained; }
    public double getTotal() { return mdTotal; }
    public double getTotalVat() { return mdTotalVat; }
    public double getExchangeRate() { return mdExchangeRate; }
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
    public String getReferenceRecord() { return msReferenceRecord; }
    public String getObservations() { return msObservations; }
    
    public ArrayList<SDataBizPartnerBranchBankAccount> getBranchBankAccountCredits() { return maBranchBankAccountCredits; }
    public ArrayList<SGuiItem> getAccountCredits() { return maAccountCredits; }
    public ArrayList<SGuiItem> getAgreementsReferences() { return maAgreementReferences; }
    public HashMap<String, String> getCodeBankAccountCredits() { return moCodeBankAccountCredits; }
    public HashMap<String, String> getAliasBankAccountCredits() { return moAliasBankAccountCredits; }

    public String getBranchBankAccountCreditNumber(int[] pk, int typeLayout) {
        String account = "";
        
        for (SDataBizPartnerBranchBankAccount bankAccount : maBranchBankAccountCredits) {
            if (SLibUtils.compareKeys(pk, (int[]) bankAccount.getPrimaryKey())) {
                account = typeLayout == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? bankAccount.getBankAccountNumber() : bankAccount.getBankAccountNumberStd();
                break;
            }
        }
        
        return account;
    }
    
    public int[] getBranchBankAccountCreditId(String account, int typeLayout) {
        int[] pk = null;
        
        for (SDataBizPartnerBranchBankAccount bankAccount : maBranchBankAccountCredits) {
            if (account.compareTo(typeLayout == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? bankAccount.getBankAccountNumber() : bankAccount.getBankAccountNumberStd()) == 0) {
                pk = (int[]) bankAccount.getPrimaryKey();
                break;
            }
        }
        
        return pk;
    }
 
    @Override
    public int[] getRowPrimaryKey() {
        int[] key = new int[] { SLibConsts.UNDEFINED };
        
        switch (mnLayoutRowType) {
            case SModSysConsts.FIN_LAY_BANK_PAY:
                key = new int[] { mnPkYearId, mnPkDocId };
                break;
            case SModSysConsts.FIN_LAY_BANK_PREPAY:
                key = new int[] { mnBizPartnerId };
                break;
            default:
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
        
        switch (mnLayoutRowType) {
            case SModSysConsts.FIN_LAY_BANK_PAY:
                switch (col) {
                    case 0:
                        value = msBizPartner;
                        break;
                    case 1:
                        value = msTypeDps;
                        break;
                    case 2:
                        value = msNumberSer;
                        break;
                    case 3:
                        value = mtDate;
                        break;
                    case 4:
                        value = msBizPartnerBranchCob;
                        break;
                    case 5:
                        value = mbIsForPayment;
                        break;
                    case 6:
                        value = moBalance.getAmountOriginal();
                        break;
                    case 7:
                        value = moBalanceTot.getAmountOriginal();               // amount to pay
                        break;
                    case 8:
                        value = msCurrencyKeyCy;
                        break;
                    case 9:
                        value = moBalanceTot.getExchangeRate();
                        break;
                    case 10:
                        value = moBalanceTot.getAmountLocal();                  // amount to pay in bank's currency
                        break;
                    case 11:
                        value = msCurrencyKey;                                  // bank key currency
                        break;    
                    case 12:
                        if (!msAgreement.isEmpty()) { //if (mnLayoutRowSubType == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) { alphalapz
                            value = msAgreement;
                        }
                        else {
                            value = msAccountCredit;
                        }
                        break;
                    case 13:
                        if (msAgreementReference == null) {
                            value = "";
                        }
                        else {
                            value = msAgreementReference;
                        }
                        break;
                    case 14:
                        value = msConceptCie;                                   // reference agreement CIE
                        break;
                    case 15:
                        value = msEmail;                            
                        break;
                    case 16:
                        value = msBizPartnerCreditFiscalId;
                        break;
                    case 17:
                        value = mdSubTotal;
                        break;
                    case 18:
                        value = mdTaxCharged;
                        break;
                    case 19:
                        value = mdTaxRetained;
                        break;
                    case 20:
                        value = mdTotal;
                        break;
                    case 21:
                        value = mtDateMaturityRo;
                        break;
                    case 22:
                        value = msObservations;
                        break;
                    default:
                }
                break;
                
            case SModSysConsts.FIN_LAY_BANK_PREPAY:
                switch (col) {
                    case 0:
                        value = msBizPartner;
                        break;
                    case 1:
                        value = msBizPartnerKey;
                        break;
                    case 2:
                        value = moBalanceTot.getAmountOriginal();   // amount to pay
                        break;
                    case 3:
                        value = msCurrencyKey;
                        break;
                    case 4:
                        if (!msAgreement.isEmpty()) { //if (mnLayoutRowSubType == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) { alphalapz
                            value = msAgreement;
                        }
                        else {
                            value = msAccountCredit;
                        }
                        break;
                    case 5:
                        if (msAgreementReference == null) {
                            value = "";
                        }
                        else {
                            value = msAgreementReference;
                        }
                        break;
                    case 6:
                        value = msConceptCie;                                   // reference agreement CIE
                        break;
                    case 7:
                        value = msEmail;
                        break;
                    case 8:
                        value = msBizPartnerCreditFiscalId;
                        break;
                    case 9:
                        value = msReferenceRecord;
                        break;
                    case 10:
                        value = msObservations;
                        break;
                    default:
                }
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (mnLayoutRowType) {
            case SModSysConsts.FIN_LAY_BANK_PAY:
                switch (col) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        break;
                    case 5:
                        mbIsForPayment = (boolean) value;
                        break;
                    case 6:
                        break;
                    case 7:
                        moBalanceTot.setAmountOriginal((double) value);
                        break;
                    case 8:
                        break;
                    case 9:
                        if (moBalance.getCurrencyLocalId() != moBalanceTot.getCurrencyOriginalId()) {
                            moBalanceTot.setExchangeRate((double) value);
                        }   
                        break;
                    case 10:
                        break;
                    case 11:
                        break;
                    case 12:   
                        //Cuenta/Convenio
                        if (value == null) {
                            msAgreement = "";
                        }
                        else {
                            if (mnLayoutRowSubType == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                                if ( value instanceof String) {
                                    msAgreement =  (String) value;
                                }
                                else {
                                    msAgreement = ((SGuiItem) value).getItem();
                                }
                            }
                            else {
                                msAccountCredit = ((SGuiItem) value).getItem();
                            }
                        }
                        break;    
                    case 13:
                        if (value == null) {
                            msAgreementReference = "";
                        }
                        else {
                            if ( value instanceof String) {
                                msAgreementReference =  (String) value;
                            }
                            else {
                                msAgreementReference = ((SGuiItem) value).getItem();
                            }
                        }
                        break;
                    case 14:
                        if (value == null) {
                            msConceptCie = "";
                        }
                        else {
                            if ( value instanceof String) {
                                msConceptCie =  (String) value;
                            }
                            else {
                                msConceptCie = ((SGuiItem) value).getItem();
                            }
                        }
                        break;
                    case 15:
                        msEmail = (String) value;
                        break;
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                        break;
                    case 22:
                        msObservations = (String) value;
                        break;
                    default:
                        break;
                    }
                    break;
            case SModSysConsts.FIN_LAY_BANK_PREPAY:
                switch (col) {
                    case 0:
                    case 1:
                        break;
                    case 2:
                         moBalanceTot.setAmountOriginal((double) value);
                        break;
                    case 3:
                        break;
                    case 4:   
                        //Cuenta/Convenio
                        if (value == null) {
                            msAgreement = "";
                            msAccountCredit = "";
                        }
                        else {
//                            if (mnLayoutRowSubType == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                            if (!msAgreement.isEmpty()) {
                                if ( value instanceof String) {
                                    msAgreement =  (String) value;
                                }
                                else {
                                    msAgreement = ((SGuiItem) value).getItem();
                                }
                            }
                            else {
                                msAccountCredit = ((SGuiItem) value).getItem();
                            }
                        }
                        break;    
                    case 5:
                        if (value == null) {
                            msAgreementReference = "";
                        }
                        else {
                            if ( value instanceof String) {
                                msAgreementReference =  (String) value;
                            }
                            else {
                                msAgreementReference = ((SGuiItem) value).getItem();
                            }
                        }
                        break;
                    case 6:
                        if (value == null) {
                            msConceptCie = "";
                        }
                        else {
                            if ( value instanceof String) {
                                msConceptCie =  (String) value;
                            }
                            else {
                                msConceptCie = ((SGuiItem) value).getItem();
                            }
                        }
                        break;
                    case 7:
                        msEmail = (String) value;
                        break;
                    case 8:
                        break;
                    case 9:
                        msReferenceRecord = (String) value;
                        break;
                    case 10:
                        msObservations = (String) value;
                        break;
                    default:
                }
                break;
            default:
        }
    }
}
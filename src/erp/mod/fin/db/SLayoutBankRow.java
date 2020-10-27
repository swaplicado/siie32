/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.data.SDataConstantsSys;
import erp.mbps.data.SDataBizPartnerBranchBankAccount;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiItem;

/**
 * Abstraction of a row when editing bank layouts, aplicating payments or just showing bank layouts.
 * Editing bank layouts and aplicating payments in SFormBankLayout.
 * Showing bank layouts in SDialogBankLayoutCardex.
 * 
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores, Isabel Servín
 */
public class SLayoutBankRow implements SGridRow {
    
    public static final int MODE_FORM_EDITION = 1;
    public static final int MODE_DIALOG_CARDEX = 2;
    public static final int AGREEMENT_REFERENCE_MAX_LEN = 20;

    protected SGuiClient miClient;
    
    protected int mnRowMode; // read-only property
    protected int mnTransactionType;
    protected int mnPaymentType;
    protected int mnBizPartnerId;
    protected String msBizPartner;
    protected String msBizPartnerKey;
    protected String msSantanderBankCode;
    protected String msBajioBankCode;
    protected String msBajioBankAlias;
    protected int mnDpsYearId;
    protected int mnDpsDocId;
    protected String msDpsType;
    protected String msDpsNumber;
    protected Date mtDpsDate;
    protected Date mtDpsDateMaturity;
    protected String msDpsCompanyBranchCode;
    protected boolean mbForPayment;
    protected boolean mbPayed;
    protected SMoney moMoneyDpsBalance;
    protected SMoney moMoneyPayment;
    protected double mdBalanceTotByBizPartner;
    protected double mdBalancePayed;
    protected String msPayerAccountCurrencyKey;
    protected String msDpsCurrencyKey;
    protected String msBeneficiaryAccountNumber;
    protected String msBeneficiaryEmail;
    protected String msBeneficiaryFiscalId;
    protected double mdSubTotal;
    protected double mdTaxCharged;
    protected double mdTaxRetained;
    protected double mdTotal;
    protected double mdTotalVat;
    protected String msAccountDebit;
    protected String msBizPartnerDebitFiscalId;
    protected String msAccountType;
    protected String msConcept;
    protected String msDescription;
    protected String msReference;
    protected String msAgreement;
    protected String msAgreementReference;
    protected String msConceptCie;
    protected int mnReferenceNumber;
    protected int mnFiscalVoucher; // 0 or 1
    protected int mnApply;
    protected int mnBankKey;
    protected int mnCurrencyId;
    protected String msReferenceRecord;
    protected String msObservations;
    protected SLayoutBankRecord moLayoutBankRecord;
    
    protected ArrayList<SDataBizPartnerBranchBankAccount> maBranchBankAccountCredits;
    protected ArrayList<SGuiItem> maAccountCredits;
    protected ArrayList<SGuiItem> maAgreementReferences;
    protected HashMap<String, String> moCodeBankAccountCredits;
    protected HashMap<String, String> moAliasBankAccountCredits;
    
    protected boolean mbXml;
    protected String msXmlUuid;
    protected String msXmlRfcEmi;
    protected String msXmlRfcRec;
    protected double mdXmlTotal;
    protected int mnXmlType;
    
    /**
     * Create a new bank layout row.
     * @param client GUI client.
     * @param rowMode Row mode, can be MODE_FORM_EDITION, MODE_DIALOG_CARDEX
     */
    public SLayoutBankRow(SGuiClient client, int rowMode) {
        miClient = client;
        mnRowMode = rowMode;
        reset();
    }

    public void reset() {
        mnTransactionType = 0;
        mnPaymentType = 0;
        mnBizPartnerId = 0;
        msBizPartner = "";
        msBizPartnerKey = "";
        msSantanderBankCode = "";
        msBajioBankCode = "";
        msBajioBankAlias = "";
        mnDpsYearId = 0;
        mnDpsDocId = 0;
        msDpsType = "";
        msDpsNumber = "";
        mtDpsDate = null;
        mtDpsDateMaturity = null;
        msDpsCompanyBranchCode = "";
        mbForPayment = false;
        mbPayed = false;
        moMoneyDpsBalance = null;
        moMoneyPayment = null;
        mdBalanceTotByBizPartner = 0;
        mdBalancePayed = 0;
        msPayerAccountCurrencyKey = "";
        msDpsCurrencyKey = "";      
        msBeneficiaryAccountNumber = "";
        msBeneficiaryEmail = "";
        msBeneficiaryFiscalId = "";
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
        mnFiscalVoucher = 0;
        mnApply = 0;
        mnBankKey = 0;
        mnCurrencyId = 0;
        msReferenceRecord = "";
        msObservations = "";
        moLayoutBankRecord = null;
        
        maBranchBankAccountCredits = new ArrayList<>();
        maAccountCredits = new ArrayList<>();
        maAgreementReferences = new ArrayList<>();
        moCodeBankAccountCredits = new HashMap<>();
        moAliasBankAccountCredits = new HashMap<>();
        
        mbXml = false;
        msXmlUuid = "";
        msXmlRfcEmi = "";
        msXmlRfcRec = "";
        mdXmlTotal = 0;
        mnXmlType = 0;
    }

    public void setTransactionType(int n) { mnTransactionType = n; }
    public void setPaymentType(int n) { mnPaymentType = n; }
    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setBizPartner(String s) { msBizPartner = s; }
    public void setBizPartnerKey(String s) { msBizPartnerKey = s; }
    public void setSantanderBankCode(String s) { msSantanderBankCode = s; }
    public void setBajioBankCode(String s) { msBajioBankCode = s; }
    public void setBajioBankAlias(String s) { msBajioBankAlias = s; }
    public void setDpsYearId(int n) { mnDpsYearId = n; }
    public void setDpsDocId(int n) { mnDpsDocId = n; }
    public void setDpsType(String s) { msDpsType = s; }
    public void setDpsNumber(String s) { msDpsNumber = s; }
    public void setDpsDate(Date t) { mtDpsDate = t; }
    public void setDpsDateMaturity(Date t) { mtDpsDateMaturity = t; }
    public void setDpsCompanyBranchCode(String s) { msDpsCompanyBranchCode = s; }
    public void setForPayment(boolean b) { mbForPayment = b; }
    public void setPayed(boolean b) { mbPayed = b; }
    public void setMoneyDpsBalance(SMoney o) { moMoneyDpsBalance = o; }
    public void setMoneyPayment(SMoney o) { moMoneyPayment = o; }
    public void setBalanceTotByBizPartner(double d) { mdBalanceTotByBizPartner = d; }
    public void setBalancePayed(double d) { mdBalancePayed = d; }
    public void setPayerAccountCurrencyKey(String s) { msPayerAccountCurrencyKey = s; }
    public void setDpsCurrencyKey(String s) { msDpsCurrencyKey = s; }
    public void setBeneficiaryAccountNumber(String s) { msBeneficiaryAccountNumber = s; }
    public void setBeneficiaryEmail(String s) { msBeneficiaryEmail = s; }
    public void setBeneficiaryFiscalId(String s) { msBeneficiaryFiscalId = s; }
    public void setSubTotal(double d) { mdSubTotal = d; }
    public void setTaxCharged(double d) { mdTaxCharged = d; }
    public void setTaxRetained(double d) { mdTaxRetained = d; }
    public void setTotal(double d) { mdTotal = d; }
    public void setTotalVat(double d) { mdTotalVat = d; }
    public void setAccountDebit(String s) { msAccountDebit = s; }
    public void setBizPartnerDebitFiscalId(String s) { msBizPartnerDebitFiscalId = s; }
    public void setAccType(String s) { msAccountType = s; }
    public void setConcept(String s) { msConcept = s; }
    public void setDescription(String s) { msDescription = s; }
    public void setReference(String s) { msReference = s; }
    public void setAgreement(String s) { msAgreement= s; }
    public void setAgreementReference(String s) { msAgreementReference= s; }
    public void setConceptCie(String s) { msConceptCie = s; }
    public void setReferenceNumber(int n) { mnReferenceNumber = n; }
    public void setFiscalVoucher(int n) { mnFiscalVoucher = n; }
    public void setApply(int n) { mnApply = n; }
    public void setBankKey(int n) { mnBankKey = n; }
    public void setCurrencyId(int n) { mnCurrencyId = n; }
    public void setReferenceRecord(String s) { msReferenceRecord = s; }
    public void setObservations(String s) { msObservations = s; }
    public void setLayoutBankRecord(SLayoutBankRecord o) { moLayoutBankRecord = o; }
    
    public void setAccountCreditArray(ArrayList<SGuiItem> a) { maAccountCredits = a; }
    public void setAgreementsReferencesArray(ArrayList<SGuiItem> a) { maAgreementReferences = a; }
    public void setBranchBankAccountCreditArray(ArrayList<SDataBizPartnerBranchBankAccount> a) { maBranchBankAccountCredits = a; }
    
    public void setExchangeRate(double exchangeRate) { moMoneyPayment.setExchangeRate(exchangeRate); }
    
    public void setIsXml(boolean b) { mbXml = b; }
    public void setXmlUuid(String s) { msXmlUuid = s; }
    public void setXmlRfcEmi(String s) { msXmlRfcEmi = s; }
    public void setXmlRfcRec(String s) { msXmlRfcRec = s; }
    public void setXmlTotal(double d) { mdXmlTotal = d; }
    public void setXmlType(int i) { mnXmlType = i; }
    
    public int getRowMode() { return mnRowMode; }
    public int getTransactionType() { return mnTransactionType; }
    public int getPaymentType() { return mnPaymentType; }
    public int getBizPartnerId() { return mnBizPartnerId; }
    public String getBizPartner() { return msBizPartner; }
    public String getBizPartnerKey() { return msBizPartnerKey; }
    public String getSantanderBankCode() { return msSantanderBankCode; }
    public String getBajioBankCode() { return msBajioBankCode; }
    public String getBajioBankAlias() { return msBajioBankAlias; }
    public int getDpsYearId() { return mnDpsYearId; }
    public int getDpsDocId() { return mnDpsDocId; }
    public String getDpsType() { return msDpsType; }
    public String getDpsNumber() { return msDpsNumber; }
    public Date getDpsDate() { return mtDpsDate; }
    public Date getDpsDateMaturity() { return mtDpsDateMaturity; }
    public String getDpsCompanyBranchCode() { return msDpsCompanyBranchCode; }
    public boolean isForPayment() { return mbForPayment; }
    public boolean isPayed() { return mbPayed; }
    public SMoney getMoneyDpsBalance() { return moMoneyDpsBalance; }
    public SMoney getMoneyPayment() { return moMoneyPayment; }
    public double getBalanceTotByBizPartner() { return mdBalanceTotByBizPartner; }
    public double getBalancePayed() { return mdBalancePayed; }
    public String getPayerAccountCurrencyKey() { return msPayerAccountCurrencyKey; }
    public String getDpsCurrencyKey() { return msDpsCurrencyKey; }
    public String getBeneficiaryAccountNumber() { return msBeneficiaryAccountNumber; }
    public String getBeneficiaryEmail() { return msBeneficiaryEmail; }
    public String getBeneficiaryFiscalId() { return msBeneficiaryFiscalId; }
    public double getSubTotal() { return mdSubTotal; }
    public double getTaxCharged() { return mdTaxCharged; }
    public double getTaxRetained() { return mdTaxRetained; }
    public double getTotal() { return mdTotal; }
    public double getTotalVat() { return mdTotalVat; }
    public String getAccountDebit() { return msAccountDebit; }
    public String getBizPartnerDebitFiscalId() { return msBizPartnerDebitFiscalId; }
    public String getAccType() { return msAccountType; }
    public String getConcept() { return msConcept; }
    public String getDescription() { return msDescription; }
    public String getReference() { return msReference; }
    public String getAgreement() { return msAgreement; }
    public String getAgreementReference() { return msAgreementReference; }
    public String getConceptCie() { return msConceptCie; }
    public int getReferenceNumber() { return mnReferenceNumber; }
    public int getFiscalVoucher() { return mnFiscalVoucher; }
    public int getApply() { return mnApply; }
    public int getBankKey() { return mnBankKey; }
    public int getCurrencyId() { return mnCurrencyId; }
    public String getReferenceRecord() { return msReferenceRecord; }
    public String getObservations() { return msObservations; }
    public SLayoutBankRecord getLayoutBankRecord() { return moLayoutBankRecord; }
    
    public ArrayList<SDataBizPartnerBranchBankAccount> getBranchBankAccountCredits() { return maBranchBankAccountCredits; }
    public ArrayList<SGuiItem> getAccountCredits() { return maAccountCredits; }
    public ArrayList<SGuiItem> getAgreementsReferences() { return maAgreementReferences; }
    public HashMap<String, String> getCodeBankAccountCredits() { return moCodeBankAccountCredits; }
    public HashMap<String, String> getAliasBankAccountCredits() { return moAliasBankAccountCredits; }
    
    public boolean isXml() { return mbXml; }
    public String getXmlUuid() { return msXmlUuid; }
    public String getXmlRfcEmi() { return msXmlRfcEmi; }
    public String getXmlRfcRec() { return msXmlRfcRec; }
    public double getXmlTotal() { return mdXmlTotal; }
    public int getXmlType() { return mnXmlType; }

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
        
        switch (mnTransactionType) {
            case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
                key = new int[] { mnDpsYearId, mnDpsDocId };
                break;
            case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
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
        
        switch (mnRowMode) {
            case MODE_FORM_EDITION:
                switch (mnTransactionType) {
                    case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
                        switch (col) {
                            case 0:
                                value = msBizPartner;
                                break;
                            case 1:
                                value = msDpsType;
                                break;
                            case 2:
                                value = msDpsNumber;
                                break;
                            case 3:
                                value = mtDpsDate;
                                break;
                            case 4:
                                value = msDpsCompanyBranchCode;
                                break;
                            case 5:
                                value = mbForPayment;
                                break;
                            case 6:
                                value = moMoneyDpsBalance.getOriginalAmount();
                                break;
                            case 7:
                                value = moMoneyPayment.getOriginalAmount();
                                break;
                            case 8:
                                value = msDpsCurrencyKey;
                                break;
                            case 9:
                                value = moMoneyPayment.getExchangeRate();
                                break;
                            case 10:
                                value = moMoneyPayment.getLocalAmount(); // amount to pay in bank's currency
                                break;
                            case 11:
                                value = msPayerAccountCurrencyKey; // bank key currency
                                break;    
                            case 12:
                                if (!msAgreement.isEmpty()) {
                                    value = msAgreement;
                                }
                                else {
                                    value = msBeneficiaryAccountNumber;
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
                                value = msConceptCie; // reference agreement CIE
                                break;
                            case 15:
                                value = msBeneficiaryEmail;                            
                                break;
                            case 16:
                                value = msBeneficiaryFiscalId;
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
                                value = mtDpsDateMaturity;
                                break;
                            case 22:
                                value = msObservations;
                                break;
                            default:
                        }
                        break;

                    case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
                        switch (col) {
                            case 0:
                                value = msBizPartner;
                                break;
                            case 1:
                                value = msBizPartnerKey;
                                break;
                            case 2:
                                value = moMoneyPayment.getOriginalAmount(); // amount to pay
                                break;
                            case 3:
                                value = msPayerAccountCurrencyKey;
                                break;
                            case 4:
                                if (!msAgreement.isEmpty()) { 
                                    value = msAgreement;
                                }
                                else {
                                    value = msBeneficiaryAccountNumber;
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
                                value = msConceptCie; // reference agreement CIE
                                break;
                            case 7:
                                value = msBeneficiaryEmail;
                                break;
                            case 8:
                                value = msBeneficiaryFiscalId;
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
                break;
                
            case MODE_DIALOG_CARDEX:
                switch (col) {
                    case 0:
                        value = msBizPartner;
                        break;
                    case 1:
                        value = msBizPartnerKey;
                        break;
                    case 2:
                        value = msDpsType;
                        break;
                    case 3:
                        value = msDpsNumber;
                        break;
                    case 4:
                        value = mtDpsDate;
                        break;
                    case 5:
                        value = msDpsCompanyBranchCode;
                        break;
                    case 6:
                        value = moMoneyPayment.getOriginalAmount();
                        break;
                    case 7:
                        value =  msDpsCurrencyKey;
                        break;
                    case 8:
                        value = moMoneyPayment.getExchangeRate();
                        break;
                    case 9:
                        if (!msAgreement.isEmpty()) {
                            value = msAgreement;
                        }
                        else {
                            value = msBeneficiaryAccountNumber;
                        }
                        break;
                    case 10:
                        value = msAgreementReference;
                        break;
                    case 11:
                        if (msConceptCie == null) {
                            value = "";
                        }
                        else{
                            value = msConceptCie;
                        }
                        break;
                    case 12:
                        value = moLayoutBankRecord.getLayoutBankRecordKey().getRecordPeriod();
                        break;
                    case 13:
                        value = moLayoutBankRecord.getBookkeepingCenterCode();
                        break;
                    case 14:
                        value = moLayoutBankRecord.getCompanyBranchCode();
                        break;
                    case 15:
                        value = moLayoutBankRecord.getLayoutBankRecordKey().getRecordNumber();
                        break;
                    case 16:
                        value = moLayoutBankRecord.getDate();
                        break;
                    case 17:
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
        switch (mnRowMode) {
            case MODE_FORM_EDITION:
                switch (mnTransactionType) {
                    case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
                        switch (col) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                                break;
                            case 5:
                                mbForPayment = (boolean) value;
                                break;
                            case 6:
                                break;
                            case 7:
                                moMoneyPayment.setOriginalAmount((double) value);
                                break;
                            case 8:
                                break;
                            case 9:
                                if (!moMoneyDpsBalance.isLocalCurrency() || moMoneyDpsBalance.getOriginalCurrencyId() != moMoneyPayment.getOriginalCurrencyId()) {
                                    setExchangeRate((double) value);
                                }   
                                break;
                            case 10:
                            case 11:
                                break;
                            case 12:   
                                //Cuenta/Convenio
                                if (value == null) {
                                    msAgreement = "";
                                }
                                else {
                                    if (mnPaymentType == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                                        if ( value instanceof String) {
                                            msAgreement =  (String) value;
                                        }
                                        else {
                                            msAgreement = ((SGuiItem) value).getItem();
                                        }
                                    }
                                    else {
                                        msBeneficiaryAccountNumber = ((SGuiItem) value).getItem();
                                    }
                                }
                                break;    
                            case 13:
                                if (value == null) {
                                    msAgreementReference = "";
                                }
                                else {
                                    if (value instanceof String) {
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
                                    if (value instanceof String) {
                                        msConceptCie =  (String) value;
                                    }
                                    else {
                                        msConceptCie = ((SGuiItem) value).getItem();
                                    }
                                }
                                break;
                            case 15:
                                msBeneficiaryEmail = (String) value;
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

                    case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
                        switch (col) {
                            case 0:
                            case 1:
                                break;
                            case 2:
                                 moMoneyPayment.setOriginalAmount((double) value);
                                break;
                            case 3:
                                break;
                            case 4:   
                                //Cuenta/Convenio
                                if (value == null) {
                                    msAgreement = "";
                                    msBeneficiaryAccountNumber = "";
                                }
                                else {
                                    if (!msAgreement.isEmpty()) {
                                        if ( value instanceof String) {
                                            msAgreement =  (String) value;
                                        }
                                        else {
                                            msAgreement = ((SGuiItem) value).getItem();
                                        }
                                    }
                                    else {
                                        msBeneficiaryAccountNumber = ((SGuiItem) value).getItem();
                                    }
                                }
                                break;    
                            case 5:
                                if (value == null) {
                                    msAgreementReference = "";
                                }
                                else {
                                    if (value instanceof String) {
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
                                    if (value instanceof String) {
                                        msConceptCie =  (String) value;
                                    }
                                    else {
                                        msConceptCie = ((SGuiItem) value).getItem();
                                    }
                                }
                                break;
                            case 7:
                                msBeneficiaryEmail = (String) value;
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
                break;
                
            case MODE_DIALOG_CARDEX:
                break;
                
            default:
        }
    }
}
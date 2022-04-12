/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data.cfd;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mcfg.data.SDataParamsCompany;
import erp.mcfg.data.SDataParamsErp;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataBookkeepingNumber;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.data.SFinAccountConfigEntry;
import erp.mfin.data.SFinAccountUtilities;
import erp.mfin.data.SFinBalanceTax;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SDbBizPartner;
import erp.mod.fin.db.SFinConsts;
import erp.mtrn.data.SDataCfdPayment;
import erp.mtrn.data.SDataDsm;
import erp.mtrn.data.SDataDsmEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiSession;

/**
 * GUI data structure for input of an individual payment for CFDI of Payments.
 * Represents the XML element pago10:Pago, child of the CFDI complement's root element pago10:Pagos.
 * @author Sergio Flores
 */
public final class SCfdPaymentEntry extends erp.lib.table.STableRow {
    
    public static final int TYPE_STANDARD = 1;          // standard payment
    public static final int TYPE_FACTORING_PAY = 11;    // factoring payment
    public static final int TYPE_FACTORING_FEE = 12;    // factoring fees
    
    private static final HashMap<Integer, String> EntryTypes = new HashMap<>();
    
    static {
        EntryTypes.put(TYPE_STANDARD, "Estándar");
        EntryTypes.put(TYPE_FACTORING_PAY, "Factoraje: pago");
        EntryTypes.put(TYPE_FACTORING_FEE, "Factoraje: intereses y comisiones");
    }
    
    public SDataCfdPayment DataParentPayment;
    public int EntryNumber;
    public int EntryType;    // payment type
    public Date PaymentDate;
    public String PaymentWay;
    public int CurrencyId;
    public String CurrencyKey;
    public double Amount;       // amount of payment
    public double ExchangeRate; // exchange rate
    public double AmountLocal;  // amount of payment in local currency
    public String Operation;
    public String AccountSrcFiscalId;
    public String AccountSrcNumber;
    public String AccountSrcEntity;
    public String AccountDestFiscalId;
    public String AccountDestNumber;
    public int[] AccountDestKey;    // can be null when destiny cash account (e.g., receipt bank) is not needed
    public SDataRecord DataRecord;
    public ArrayList<SCfdPaymentEntryDoc> PaymentEntryDocs;
    
    public int AuxGridIndex;
    public int AuxUserId;
    public int AuxFactoringBankId;
    public String AuxFactoringBankFiscalId;
    public String AuxConceptDocsCustom; // customized document numbers to be inserted in accounting concept
    public String AuxConceptDocs;       // document numbers to be inserted in accounting concept
    public double AuxTotalPayments;
    public double AuxTotalPaymentsLocal;
    public double AuxTotalLimMin;
    public double AuxTotalLimMax;
    public boolean AuxAllowTotalPaymentsLessThanAmount;
    public boolean AuxAllowTotalPaymentsLocalLessThanAmountLocal;
    public boolean AuxAllowTotalPaymentsLocalGreaterThanAmountLocal;
    public ArrayList<SDataRecordEntry> AuxDbmsRecordEntries;
    
    public SCfdPaymentEntry(SDataCfdPayment parentPayment, int entryNumber, int entryType, Date paymentDate, String paymentWay, int currencyId, String currencyKey, double amount, double exchangeRate, SDataRecord record) {
        DataParentPayment = parentPayment;
        EntryNumber = entryNumber;
        EntryType = entryType;
        PaymentDate = paymentDate;
        PaymentWay = paymentWay;
        CurrencyId = currencyId;
        CurrencyKey = currencyKey;
        Amount = amount;
        ExchangeRate = exchangeRate;
        //AmountLocal... set in method computeAmountLocal()
        Operation = "";
        AccountSrcFiscalId = "";
        AccountSrcNumber = "";
        AccountSrcEntity = "";
        AccountDestFiscalId = "";
        AccountDestNumber = "";
        AccountDestKey = null;
        DataRecord = record;
        PaymentEntryDocs = new ArrayList<>();
        
        AuxGridIndex = -1;
        AuxUserId = 0;
        AuxFactoringBankId = 0;
        AuxFactoringBankFiscalId = "";
        AuxConceptDocsCustom = "";
        AuxConceptDocs = "";
        AuxDbmsRecordEntries = new ArrayList<>();
        
        resetAllowances();
        computeAmountLocal();
        computeTotalPayments();
    }
    
    /**
     * Resets allowances.
     */
    public void resetAllowances() {
        AuxAllowTotalPaymentsLessThanAmount = false;
        AuxAllowTotalPaymentsLocalLessThanAmountLocal = false;
        AuxAllowTotalPaymentsLocalGreaterThanAmountLocal = false;
    }
    
    /**
     * Computes amount in local currency.
     */
    public void computeAmountLocal() {
        AmountLocal = SLibUtils.roundAmount(Amount * ExchangeRate);
    }
    
    /**
     * Computes total payments of documents, both in payment currency and local currency.
     */
    public void computeTotalPayments() {
        AuxTotalPayments = 0;
        AuxTotalPaymentsLocal = 0;
        AuxTotalLimMin = 0;
        AuxTotalLimMax = 0;
        
        HashSet<String> docsSet = new HashSet<>();
        
        for (SCfdPaymentEntryDoc paymentEntryDoc : PaymentEntryDocs) {
            paymentEntryDoc.computePaymentAmounts();
            
            AuxTotalPayments = SLibUtils.roundAmount(AuxTotalPayments + paymentEntryDoc.PayPayment);
            AuxTotalPaymentsLocal = SLibUtils.roundAmount(AuxTotalPaymentsLocal + paymentEntryDoc.PayPaymentLocal);
            AuxTotalLimMin += paymentEntryDoc.PayPaymentLimMin; // sum without rounding!
            AuxTotalLimMax += paymentEntryDoc.PayPaymentLimMax; // sum without rounding!
            
            docsSet.add(paymentEntryDoc.ThinDps.getDpsNumber());
        }
        
        AuxConceptDocs = "";
        for (String doc : docsSet) {
            AuxConceptDocs += (AuxConceptDocs.isEmpty() ? "" : " ") + doc;
        }
    }
    
    /**
     * Gets remainder in payment currency of payment not yet applied to documents.
     * @return Remainder.
     */
    public double getRemainder() {
        computeTotalPayments();
        return SLibUtils.roundAmount(Amount - AuxTotalPayments);
    }

    /**
     * Gets remainder in local currency of payment not yet applied to documents.
     * @return Remainder.
     */
    public double getRemainderLocal() {
        computeAmountLocal();
        computeTotalPayments();
        return SLibUtils.roundAmount(AmountLocal - AuxTotalPaymentsLocal);
    }
    
    /**
     * Checks if entry is for factoring.
     * @return 
     */
    public boolean isFactoring() {
        return SLibUtils.belongsTo(EntryType, new int[] { TYPE_FACTORING_PAY, TYPE_FACTORING_FEE });
    }
    
    /**
     * Gets description of current type.
     * @return 
     */
    public String getTypeDescription() {
        return EntryTypes.get(EntryType);
    }
    
    /**
     * Checks if amount is totally applied.
     * @return 
     */
    public boolean isAmountTotallyApplied() {
        return SLibUtils.compareAmount(Amount, AuxTotalPayments);
    }
    
    /**
     * Gets the customized document numbers for accounting concept if available, otherwise the default ones.
     * @return 
     */
    public String getConceptDocs() {
        return !AuxConceptDocsCustom.isEmpty() ? AuxConceptDocsCustom : AuxConceptDocs;
    }

    /**
     * Creates record entry for account cash.
     * @param session User session.
     * @param accountCash Account cash. Can be null.
     * @param amountCur Amount in original currency.
     * @param xrt Exchange rate.
     * @param amountLoc Amount in local currency.
     * @return
     * @throws Exception 
     */
    private erp.mfin.data.SDataRecordEntry createRecordEntryAccountCash(final SGuiSession session, final SDataAccountCash accountCash, 
            final double amountCur, final double xrt, final double amountLoc) throws Exception {
        int currencyId;
        int[] accountCashKey;
        int[] systemAccountTypeKey;
        int[] systemMoveTypeXXXKey;
        String accountId;
        
        if (accountCash == null) {
            throw new Exception("Se debe especificar la cuenta de dinero deseada.");
        }
        else {
            currencyId = CurrencyId;
            accountCashKey = (int[]) accountCash.getPrimaryKey();
            
            if (accountCash.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH) {
                systemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH;
                systemMoveTypeXXXKey = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH;
            }
            else {
                systemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK;
                systemMoveTypeXXXKey = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK;
            }
            
            accountId = accountCash.getFkAccountId();
        }

        SDataRecordEntry recordEntry = new SDataRecordEntry();
        
        recordEntry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        recordEntry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        recordEntry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
        recordEntry.setFkUserNewId(session.getUser().getPkUserId());
        recordEntry.setDbmsAccountingMoveSubclass(session.readField(SModConsts.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL, SDbRegistry.FIELD_NAME).toString());
        //recordEntry.setConcept(...);          // will be set in method generateRecordEntries()
        recordEntry.setDebit(amountLoc);
        recordEntry.setCredit(0);
        recordEntry.setExchangeRate(xrt);
        recordEntry.setExchangeRateSystem(xrt);
        recordEntry.setDebitCy(amountCur);
        recordEntry.setCreditCy(0);
        //recordEntry.setSortingPosition(...);  // will be set when CFD Payment is saved
        recordEntry.setFkCurrencyId(currencyId);
        recordEntry.setFkAccountIdXXX(accountId);
        recordEntry.setFkCostCenterIdXXX_n("");
        recordEntry.setIsExchangeDifference(false);
        recordEntry.setIsSystem(true);
        recordEntry.setIsDeleted(false);

        recordEntry.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY[0]);
        recordEntry.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY[1]);

        recordEntry.setFkSystemAccountClassId(systemAccountTypeKey[0]);
        recordEntry.setFkSystemAccountTypeId(systemAccountTypeKey[1]);

        recordEntry.setFkSystemMoveCategoryIdXXX(systemMoveTypeXXXKey[0]);
        recordEntry.setFkSystemMoveTypeIdXXX(systemMoveTypeXXXKey[1]);

        recordEntry.setDbmsAccount(SDataReadDescriptions.getCatalogueDescription((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { accountId }));
        recordEntry.setDbmsAccountComplement(accountCash.getDbmsCompanyBranchEntity().getEntity());
        recordEntry.setDbmsCostCenter_n("");
        recordEntry.setDbmsCurrencyKey(session.getSessionCustom().getCurrencyCode(new int[] { currencyId }));

        recordEntry.setReference("");
        recordEntry.setIsReferenceTax(false);
        recordEntry.setFkTaxBasicId_n(0);
        recordEntry.setFkTaxId_n(0);

        recordEntry.setFkBizPartnerId_nr(0);
        recordEntry.setFkBizPartnerBranchId_n(0);

        recordEntry.setFkCompanyBranchId_n(accountCashKey[0]);
        recordEntry.setFkEntityId_n(accountCashKey[1]);
        recordEntry.setUnits(0d);
        recordEntry.setFkItemId_n(0);
        recordEntry.setFkItemAuxId_n(0);
        recordEntry.setFkYearId_n(0);
        recordEntry.setFkDpsYearId_n(0);
        recordEntry.setFkDpsDocId_n(0);
        recordEntry.setFkDpsAdjustmentYearId_n(0);
        recordEntry.setFkDpsAdjustmentDocId_n(0);
        //recordEntry.setFkBookkeepingYearId_n(...);    // will be set in method generateRecordEntries()
        //recordEntry.setFkBookkeepingNumberId_n(...);  // will be set in method generateRecordEntries()

        return recordEntry;
    }

    private erp.mfin.data.SDataRecordEntry createRecordEntryFactoringFee(final SGuiSession session, 
            final String accountId, final String costCenterId, final int itemId, final int currencyId,
            final double amountCur, final double xrt, final double amountLoc) throws Exception {
        SDataRecordEntry recordEntry = new SDataRecordEntry();
        
        recordEntry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        recordEntry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        recordEntry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
        recordEntry.setFkUserNewId(session.getUser().getPkUserId());
        recordEntry.setDbmsAccountingMoveSubclass(session.readField(SModConsts.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL, SDbRegistry.FIELD_NAME).toString());
        //recordEntry.setConcept(...);          // will be set in method generateRecordEntries()
        recordEntry.setDebit(amountLoc);
        recordEntry.setCredit(0);
        recordEntry.setExchangeRate(xrt);
        recordEntry.setExchangeRateSystem(xrt);
        recordEntry.setDebitCy(amountCur);
        recordEntry.setCreditCy(0);
        //recordEntry.setSortingPosition(...);  // will be set when CFD Payment is saved
        recordEntry.setFkCurrencyId(currencyId);
        recordEntry.setFkAccountIdXXX(accountId);
        recordEntry.setFkCostCenterIdXXX_n(costCenterId);
        recordEntry.setIsExchangeDifference(false);
        recordEntry.setIsSystem(true);
        recordEntry.setIsDeleted(false);

        recordEntry.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY[0]);
        recordEntry.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY[1]);

        recordEntry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_NA_NA[0]);
        recordEntry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_NA_NA[1]);

        recordEntry.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_NA[0]);
        recordEntry.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_NA[1]);

        recordEntry.setDbmsAccount(SDataReadDescriptions.getCatalogueDescription((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { accountId }));
        recordEntry.setDbmsAccountComplement("");
        recordEntry.setDbmsCostCenter_n(SDataReadDescriptions.getCatalogueDescription((SClientInterface) session.getClient(), SDataConstants.FIN_CC, new Object[] { costCenterId }));
        recordEntry.setDbmsCurrencyKey(session.getSessionCustom().getCurrencyCode(new int[] { currencyId }));

        recordEntry.setReference("");
        recordEntry.setIsReferenceTax(false);
        recordEntry.setFkTaxBasicId_n(0);
        recordEntry.setFkTaxId_n(0);

        recordEntry.setFkBizPartnerId_nr(0);
        recordEntry.setFkBizPartnerBranchId_n(0);

        recordEntry.setFkCompanyBranchId_n(0);
        recordEntry.setFkEntityId_n(0);
        recordEntry.setUnits(0d);
        recordEntry.setFkItemId_n(itemId);
        recordEntry.setFkItemAuxId_n(0);
        recordEntry.setFkYearId_n(0);
        recordEntry.setFkDpsYearId_n(0);
        recordEntry.setFkDpsDocId_n(0);
        recordEntry.setFkDpsAdjustmentYearId_n(0);
        recordEntry.setFkDpsAdjustmentDocId_n(0);
        //recordEntry.setFkBookkeepingYearId_n(...);    // will be set in method generateRecordEntries()
        //recordEntry.setFkBookkeepingNumberId_n(...);  // will be set in method generateRecordEntries()

        return recordEntry;
    }

    private erp.mfin.data.SDataRecordEntry createRecordEntryDifference(final SGuiSession session, 
            final String accountId, final String costCenterId, final int itemId, final int currencyId,
            final double difference) throws Exception {
        SDataRecordEntry recordEntry = new SDataRecordEntry();
        
        recordEntry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        recordEntry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        recordEntry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
        recordEntry.setFkUserNewId(session.getUser().getPkUserId());
        recordEntry.setDbmsAccountingMoveSubclass(session.readField(SModConsts.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL, SDbRegistry.FIELD_NAME).toString());
        //recordEntry.setConcept(...);          // will be set in method generateRecordEntries()
        if (difference >= 0) {
            recordEntry.setDebit(0);
            recordEntry.setCredit(difference);   // income
        }
        else {
            recordEntry.setDebit(-difference);   // expense
            recordEntry.setCredit(0);
        }
        recordEntry.setExchangeRate(0);
        recordEntry.setExchangeRateSystem(0);
        recordEntry.setDebitCy(0);
        recordEntry.setCreditCy(0);
        //recordEntry.setSortingPosition(...);  // will be set when CFD Payment is saved
        recordEntry.setFkCurrencyId(currencyId);
        recordEntry.setFkAccountIdXXX(accountId);
        recordEntry.setFkCostCenterIdXXX_n(costCenterId);
        recordEntry.setIsExchangeDifference(true);
        recordEntry.setIsSystem(true);
        recordEntry.setIsDeleted(false);

        recordEntry.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY[0]);
        recordEntry.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY[1]);

        recordEntry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_NA_NA[0]);
        recordEntry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_NA_NA[1]);

        recordEntry.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_NA[0]);
        recordEntry.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_NA[1]);

        recordEntry.setDbmsAccount(SDataReadDescriptions.getCatalogueDescription((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { accountId }));
        recordEntry.setDbmsAccountComplement("");
        recordEntry.setDbmsCostCenter_n(SDataReadDescriptions.getCatalogueDescription((SClientInterface) session.getClient(), SDataConstants.FIN_CC, new Object[] { costCenterId }));
        recordEntry.setDbmsCurrencyKey(session.getSessionCustom().getCurrencyCode(new int[] { currencyId }));

        recordEntry.setReference("");
        recordEntry.setIsReferenceTax(false);
        recordEntry.setFkTaxBasicId_n(0);
        recordEntry.setFkTaxId_n(0);

        recordEntry.setFkBizPartnerId_nr(0);
        recordEntry.setFkBizPartnerBranchId_n(0);

        recordEntry.setFkCompanyBranchId_n(0);
        recordEntry.setFkEntityId_n(0);
        recordEntry.setUnits(0d);
        recordEntry.setFkItemId_n(itemId);
        recordEntry.setFkItemAuxId_n(0);
        recordEntry.setFkYearId_n(0);
        recordEntry.setFkDpsYearId_n(0);
        recordEntry.setFkDpsDocId_n(0);
        recordEntry.setFkDpsAdjustmentYearId_n(0);
        recordEntry.setFkDpsAdjustmentDocId_n(0);
        //recordEntry.setFkBookkeepingYearId_n(...);    // will be set in method generateRecordEntries()
        //recordEntry.setFkBookkeepingNumberId_n(...);  // will be set in method generateRecordEntries()

        return recordEntry;
    }

    /**
     * Prepares the supplied record entry.
     */
    private void prepareRecordEntry(SGuiSession session, SDataRecordEntry recordEntry, SDataBookkeepingNumber bookkeepingNumber, String bizPartnerName) {
        recordEntry.setPkYearId(DataRecord.getPkYearId());
        recordEntry.setPkPeriodId(DataRecord.getPkPeriodId());
        recordEntry.setPkBookkeepingCenterId(DataRecord.getPkBookkeepingCenterId());
        recordEntry.setPkRecordTypeId(DataRecord.getPkRecordTypeId());
        recordEntry.setPkNumberId(DataRecord.getPkNumberId());
        //entry.setPkEntryId(...);          // will be set when saved!
        
        //entry.setSortingPosition(...);    // will be set when saved!
        recordEntry.setFkBookkeepingYearId_n(bookkeepingNumber.getPkYearId());
        recordEntry.setFkBookkeepingNumberId_n(bookkeepingNumber.getPkNumberId());
        
        recordEntry.setDbmsAccount(SDataReadDescriptions.getCatalogueDescription((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { recordEntry.getFkAccountId() }));
        recordEntry.setDbmsCurrencyKey(session.getSessionCustom().getCurrencyCode(new int[] { recordEntry.getFkCurrencyId() }));
        
        if (recordEntry.getFkSystemMoveCategoryIdXXX() == SDataConstantsSys.FINS_CT_SYS_MOV_BPS) {
            recordEntry.setDbmsAccountComplement(bizPartnerName);
        }
    }
    
    private String composeConcept(final String docs, final String bizPartner, final SDataAccountCash accountCash) {
        return composeConcept(docs, bizPartner, accountCash, 0, 0);
    }

    private String composeConcept(final String docs, final String bizPartner, final SDataAccountCash accountCash, final double payment, final double xrt) {
        return SFinConsts.TXT_INVOICE + " " + docs + " " + bizPartner +
                (payment == 0 ? "" : " $" + SLibUtils.getDecimalFormatAmount().format(payment) + " " + CurrencyKey) +
                (xrt == 0 ? "" : " TC " + SLibUtils.getDecimalFormatExchangeRate().format(xrt)) +
                (accountCash == null ? "" : " " + accountCash.getAuxCode());
    }
    
    private boolean isBalancedAccountingApplying() {
        /*
        1. Payment amout is totally applied and
        2.1. Payment type may require a bank and a bank is available or
        2.2. Payment type is factoring fee, thus, income or expenses are already processed.
        */
        return isAmountTotallyApplied() && ((SLibUtils.belongsTo(EntryType, new int[] { TYPE_STANDARD, TYPE_FACTORING_PAY }) && AccountDestKey != null) || EntryType == TYPE_FACTORING_FEE);
    }

    /**
     * Generates all record entries (journal voucher movements) for the accounting of this payment.
     * NOTE: This method must be called in GUI form, before saving this registry!
     * @param session GUI session.
     * @throws Exception 
     */
    public void generateRecordEntries(final SGuiSession session) throws Exception {
        /* Structure of the journal voucher movements to be generated:
         * section 1: accounting of document payments: that is, one or more set of entries for each document payment;
         * section 2: accounting of payment: that is, one entry for the payment (bank deposit), only if it is required;
         * section 3: accounting of exchange rate difference or rounding issues: that is, one entry for balancing the accounting itself.
         */
        
        // update redundant values:
        
        computeAmountLocal();
        computeTotalPayments();
        
        // process new journal voucher movements:
        
        AuxUserId = session.getUser().getPkUserId();
        AuxDbmsRecordEntries.clear();
        
        SDataDsm oDsm = new SDataDsm();
        
        String bizPartnerName = session.readField(SModConsts.BPSU_BP, new int[] { DataParentPayment.getAuxCfdDbmsDataReceptor().getPkBizPartnerId() }, SDbBizPartner.FIELD_NAME_COMM).toString();
        SDataAccountCash accountCashDest = null;
        
        if (AccountDestKey != null) {
            accountCashDest = (SDataAccountCash) SDataUtilities.readRegistry((SClientInterface) session.getClient(), SDataConstants.FIN_ACC_CASH, AccountDestKey, SLibConstants.EXEC_MODE_VERBOSE);
        }
        
        /*
         * section 1: accounting of document payments.
         */
        
        boolean isPayCurrencyLocal = session.getSessionCustom().isLocalCurrency(new int[] { CurrencyId });
        boolean areAllDocsCurrencyLocal = true; // marks if all payed documents are in local currency

        for (SCfdPaymentEntryDoc paymentEntryDoc : PaymentEntryDocs) {
            // check if all docs are in local currency:
            
            double destXrt;
            
            if (session.getSessionCustom().isLocalCurrency(new int[] { paymentEntryDoc.ThinDps.getFkCurrencyId() })) {
                destXrt = 1d;
                
                if (Math.abs(SLibUtils.roundAmount(paymentEntryDoc.DocPayment - paymentEntryDoc.PayPaymentLocal)) >= 0.01) {
                    throw new Exception("Hay una diferencia considerable en el pago en moneda local al documento " + paymentEntryDoc.ThinDps.getDpsNumber() + ": " +
                            SLibUtils.getDecimalFormatAmount().format(SLibUtils.roundAmount(paymentEntryDoc.DocPayment - paymentEntryDoc.PayPaymentLocal)) + ".");
                }
            }
            else {
                destXrt = paymentEntryDoc.ExchangeRate == 0d ? 0d : SLibUtils.round(ExchangeRate / paymentEntryDoc.ExchangeRate, SLibUtils.getDecimalFormatExchangeRate().getMaximumFractionDigits());
                
                areAllDocsCurrencyLocal = false;
            }
            
            // generate DSM entry for the accounting of current payment (to process payment and related taxes):
            
            ArrayList<SFinBalanceTax> balances = erp.mod.fin.db.SFinUtils.getBalanceByTax(session.getDatabase().getConnection(), 
                    SLibTimeUtils.digestYear(DataParentPayment.getDbmsDataCfd().getTimestamp())[0], 
                    paymentEntryDoc.ThinDps.getPkYearId(), paymentEntryDoc.ThinDps.getPkDocId(), 
                    SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[0], SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1], 
                    paymentEntryDoc.ParentPaymentEntry.DataRecord);
                        
            double dTotalBalance = 0d;
            double dTotalBalanceCur = 0d;
            
            for (SFinBalanceTax balance : balances) {
                dTotalBalance = SLibUtils.roundAmount(dTotalBalance + balance.getBalance());
                dTotalBalanceCur = SLibUtils.roundAmount(dTotalBalanceCur + balance.getBalanceCurrency());
            }

            HashMap<String, double[]> taxBalances = new HashMap<>();
            String tax;
            double perc;
            double percCur;
            double amtToPay = 0;
            double amtToPayCur = 0;
            double amtDesToPay = 0;
            double amtDesToPayCur = 0;
            int[] taxMax = new int[] { 0, 0 };
            double amtMaj = 0d;
            for (SFinBalanceTax balance : balances) {
                tax = balance.getTaxBasicId() + "_" + balance.getTaxId();

                perc = balance.getBalance() / dTotalBalance;
                percCur = balance.getBalanceCurrency() / dTotalBalanceCur;

                taxBalances.put(tax, new double[] { perc, percCur });

                amtToPay += SLibUtils.roundAmount((paymentEntryDoc.PayPayment * ExchangeRate) * perc);
                amtToPayCur += SLibUtils.roundAmount(paymentEntryDoc.PayPayment * percCur);

                amtDesToPay += SLibUtils.roundAmount(paymentEntryDoc.PayPaymentLocal * perc);
                amtDesToPayCur += SLibUtils.roundAmount(paymentEntryDoc.DocPayment * percCur);

                if (balance.getBalanceCurrency() > amtMaj) {
                    amtMaj = balance.getBalanceCurrency();
                    taxMax = new int[] { balance.getTaxBasicId(), balance.getTaxId() };
                }
            }

            double diff = 0;
            if ((paymentEntryDoc.PayPayment * ExchangeRate) != amtToPay) {
                diff = SLibUtils.roundAmount(paymentEntryDoc.PayPayment - amtToPay);
            }
            double diffCur = 0;
            if (paymentEntryDoc.PayPayment != amtToPayCur) {
                diffCur = SLibUtils.roundAmount(paymentEntryDoc.PayPayment - amtToPayCur);
            }
            double diffDes = 0;
            if (paymentEntryDoc.PayPaymentLocal != amtDesToPay) {
                diffDes = SLibUtils.roundAmount(paymentEntryDoc.PayPaymentLocal - amtToPay);
            }
            double diffDesCur = 0;
            if (paymentEntryDoc.DocPayment != amtDesToPayCur) {
                diffDesCur = SLibUtils.roundAmount(paymentEntryDoc.DocPayment - amtToPayCur);
            }

            for (SFinBalanceTax balance : balances) {
                SDataDsmEntry oDsmEntry = new SDataDsmEntry();

                tax = balance.getTaxBasicId() + "_" + balance.getTaxId();

                oDsmEntry.setSourceValue(SLibUtils.roundAmount((paymentEntryDoc.PayPayment * ExchangeRate) * taxBalances.get(tax)[0]));
                oDsmEntry.setSourceValueCy(SLibUtils.roundAmount(paymentEntryDoc.PayPayment * taxBalances.get(tax)[1]));

                oDsmEntry.setDestinyValue(SLibUtils.roundAmount(paymentEntryDoc.PayPaymentLocal * taxBalances.get(tax)[0]));
                oDsmEntry.setDestinyValueCy(SLibUtils.roundAmount(paymentEntryDoc.DocPayment * taxBalances.get(tax)[1]));

                if (balance.getTaxBasicId() == taxMax[0] && balance.getTaxId() == taxMax[1]) {
                    oDsmEntry.setSourceValue(SLibUtils.roundAmount(oDsmEntry.getSourceValue() + diff));
                    oDsmEntry.setSourceValueCy(SLibUtils.roundAmount(oDsmEntry.getSourceValueCy() + diffCur));

                    oDsmEntry.setDestinyValue(SLibUtils.roundAmount(oDsmEntry.getDestinyValue() + diffDes));
                    oDsmEntry.setDestinyValueCy(SLibUtils.roundAmount(oDsmEntry.getDestinyValueCy() + diffDesCur));
                }

                oDsmEntry.setPkYearId(session.getCurrentYear());
                oDsmEntry.setFkUserNewId(session.getUser().getPkUserId());

                oDsmEntry.setSourceReference("");
                oDsmEntry.setFkSourceCurrencyId(CurrencyId);
                oDsmEntry.setSourceExchangeRateSystem(ExchangeRate);
                oDsmEntry.setSourceExchangeRate(ExchangeRate);
                
                oDsmEntry.setFkTaxBasId_n(balance.getTaxBasicId());
                oDsmEntry.setFkTaxId_n(balance.getTaxId());

                oDsmEntry.setFkDestinyDpsYearId_n(paymentEntryDoc.ThinDps.getPkYearId());
                oDsmEntry.setFkDestinyDpsDocId_n(paymentEntryDoc.ThinDps.getPkDocId());
                oDsmEntry.setFkDestinyCurrencyId(paymentEntryDoc.ThinDps.getFkCurrencyId());
                oDsmEntry.setDestinyExchangeRateSystem(destXrt);
                oDsmEntry.setDestinyExchangeRate(destXrt);
                oDsmEntry.setDbmsFkDpsCategoryId(paymentEntryDoc.ThinDps.getFkDpsCategoryId());
                oDsmEntry.setDbmsDestinyDps(paymentEntryDoc.ThinDps.getDpsNumber());
                oDsmEntry.setDbmsSubclassMove(session.readField(SModConsts.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP, SDbRegistry.FIELD_NAME).toString());
                oDsmEntry.setDbmsBizPartner(bizPartnerName);
                oDsmEntry.setDbmsDestinyTpDps(session.readField(SModConsts.TRNU_TP_DPS, paymentEntryDoc.ThinDps.getDpsTypeKey(), SDbRegistry.FIELD_CODE).toString());

                oDsmEntry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP[0]);
                oDsmEntry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP[1]);
                oDsmEntry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP[2]);
                oDsmEntry.setDbmsCtSysMovId(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[0]);
                oDsmEntry.setDbmsTpSysMovId(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1]);

                oDsmEntry.setFkBizPartnerId(paymentEntryDoc.ThinDps.getFkBizPartnerId_r());
                oDsmEntry.setDbmsFkBizPartnerBranchId_n(paymentEntryDoc.ThinDps.getFkBizPartnerBranchId());

                Vector<SFinAccountConfigEntry> accountConfigEntries = SFinAccountUtilities.obtainBizPartnerAccountConfigs((SClientInterface) session.getClient(), paymentEntryDoc.ThinDps.getFkBizPartnerId_r(), SDataConstantsSys.BPSS_CT_BP_CUS,
                        DataRecord.getPkBookkeepingCenterId(), DataRecord.getDate(), SDataConstantsSys.FINS_TP_ACC_BP_OP, paymentEntryDoc.ThinDps.getFkDpsCategoryId() == SDataConstantsSys.TRNS_CT_DPS_SAL, oDsmEntry.getTaxPk());

                if (! accountConfigEntries.isEmpty()) {
                    oDsmEntry.setDbmsAccountOp(accountConfigEntries.get(0).getAccountId());
                }

                // process payment and related taxes with DSM instance:

                oDsm.getDbmsEntries().add(oDsmEntry);
            }

            oDsm.setDbmsPkRecordTypeId(SDataConstantsSys.FINU_TP_REC_SUBSYS_CUS);
            oDsm.setDbmsSubsystemTypeBiz(session.readField(SModConsts.BPSS_CT_BP, new int[] { SDataConstantsSys.BPSS_CT_BP_CUS }, SDbRegistry.FIELD_CODE) + "");

            oDsm.setDate(session.getCurrentDate());
            oDsm.setDbmsErpTaxModel(((SDataParamsErp) session.getConfigSystem()).getTaxModel());
            oDsm.setFkSubsystemCategoryId(SDataConstantsSys.BPSS_CT_BP_CUS);
            oDsm.setFkCompanyBranchId(DataRecord.getFkCompanyBranchId());
            oDsm.setFkUserNewId(session.getUser().getPkUserId());
            oDsm.setDbmsFkCompanyBranch(((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDbmsDataCompany().getDbmsBizPartnerBranchHq().getPkBizPartnerBranchId());
            oDsm.setDbmsCompanyBranchCode(((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDbmsDataCompany().getDbmsBizPartnerBranch(new int[] { DataRecord.getFkCompanyBranchId() }).getCode());
            oDsm.setDbmsErpDecimalsValue(((SDataParamsErp) session.getConfigSystem()).getDecimalsValue());
            oDsm.setDbmsIsRecordSaved(false);

            oDsm = (SDataDsm) ((SClientInterface) session.getClient()).getGuiModule(SDataConstants.MOD_FIN).processRegistry(oDsm); // a very odd conception of the processing of payment and taxes!
            
            String paymentConcept;
            
            if (isPayCurrencyLocal) {
                paymentConcept = composeConcept(paymentEntryDoc.ThinDps.getDpsNumber(), bizPartnerName, accountCashDest);
            }
            else {
                paymentConcept = composeConcept(paymentEntryDoc.ThinDps.getDpsNumber(), bizPartnerName, accountCashDest, paymentEntryDoc.PayPayment, ExchangeRate);
            }
            
            for (SDataRecordEntry recordEntry : oDsm.getDbmsRecord().getDbmsRecordEntries()) {
                recordEntry.setConcept(paymentConcept); // same concept for all related bookkeeping registries
                AuxDbmsRecordEntries.add(recordEntry);
            }
            
            if (EntryType == TYPE_FACTORING_FEE) {
                // accounting counterpart for factoring fees:
                
                // add as well bokkeeping registry for cash account:
                SDataRecordEntry recordEntry = null;
                SDataParamsCompany paramsCompany = (SDataParamsCompany) session.getConfigCompany();
                
                switch (paymentEntryDoc.EntryDocType) {
                    case SCfdPaymentEntryDoc.TYPE_PAY:
                        throw new Exception("Opción inválida para intereses y comisiones de factoraje.");
                        
                    case SCfdPaymentEntryDoc.TYPE_INT:
                        recordEntry = createRecordEntryFactoringFee(session, 
                                paramsCompany.getFkCfdPaymentAccountExpensesId_n(), paramsCompany.getFkCfdPaymentCostCenterExpensesId_n(), 
                                paramsCompany.getFkCfdPaymentItemBankInterestId_n(), CurrencyId, 
                                paymentEntryDoc.PayPayment, ExchangeRate, paymentEntryDoc.PayPaymentLocal);
                        break;
                        
                    case SCfdPaymentEntryDoc.TYPE_FEE:
                        recordEntry = createRecordEntryFactoringFee(session, 
                                paramsCompany.getFkCfdPaymentAccountExpensesId_n(), paramsCompany.getFkCfdPaymentCostCenterExpensesId_n(), 
                                paramsCompany.getFkCfdPaymentItemBankFeeId_n(), CurrencyId, 
                                paymentEntryDoc.PayPayment, ExchangeRate, paymentEntryDoc.PayPaymentLocal);
                        break;
                        
                    case SCfdPaymentEntryDoc.TYPE_FEE_VAT:
                        String accountId = SFinAccountUtilities.obtainTaxAccountId(new int[] { paramsCompany.getFkCfdPaymentBankFeeTaxBasicId_n(), paramsCompany.getFkCfdPaymentBankFeeTaxId_n() }, 
                                SDataConstantsSys.TRNS_CT_DPS_PUR, PaymentDate, SDataConstantsSys.FINX_ACC_PAY, session.getStatement()); // VAT in favor, so it is treat as VAT from purchases
                        
                        recordEntry = createRecordEntryFactoringFee(session, 
                                accountId, "", 
                                0, CurrencyId, 
                                paymentEntryDoc.PayPayment, ExchangeRate, paymentEntryDoc.PayPaymentLocal);
                        break;
                        
                    default:
                }
                
                recordEntry.setConcept(paymentConcept);
                AuxDbmsRecordEntries.add(recordEntry);
            }
            
            oDsm.getDbmsEntries().clear();
        }
        
        /*
         * section 2: accounting of payment.
         */
        
        String globalConcept;
        
        if (isPayCurrencyLocal) {
            globalConcept = composeConcept(getConceptDocs(), bizPartnerName, accountCashDest);
        }
        else {
            globalConcept = composeConcept(getConceptDocs(), bizPartnerName, accountCashDest, Amount, ExchangeRate);
        }
        
        switch (EntryType) {
            case TYPE_STANDARD:
            case TYPE_FACTORING_PAY:
                // a real payment done:
                if (AccountDestKey != null) {
                    SDataRecordEntry entryAccountCash = createRecordEntryAccountCash(session, accountCashDest, Amount, ExchangeRate, AmountLocal);
                    entryAccountCash.setConcept(globalConcept);
                    AuxDbmsRecordEntries.add(entryAccountCash);
                }
                break;
                
            case TYPE_FACTORING_FEE:
                // a compensation done; journal voucher movements already instanciated above
                break;
                
            default:
        }
        
        // prepare accounting and validate accounting balance:
        
        SDataBookkeepingNumber bookkeepingNumber = new SDataBookkeepingNumber();
        bookkeepingNumber.setPkYearId(DataRecord.getPkYearId());
        bookkeepingNumber.setFkUserNewId(session.getUser().getPkUserId());
        if (bookkeepingNumber.save(session.getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
        }
        
        for (SDataRecordEntry entry : AuxDbmsRecordEntries) {
            prepareRecordEntry(session, entry, bookkeepingNumber, bizPartnerName);
        }
        
        // validate accounting balance:
        
        if (isBalancedAccountingApplying()) {
            // payment amount is totally applied, thus check accounting balance:
            
            double totDebit = 0;
            double totCredit = 0;

            for (SDataRecordEntry entry : AuxDbmsRecordEntries) {
                totDebit = SLibUtils.roundAmount(totDebit + entry.getDebit());
                totCredit = SLibUtils.roundAmount(totCredit + entry.getCredit());
            }

            double difference = SLibUtils.roundAmount(totDebit - totCredit);
            
            if (Math.abs(difference) >= 0.01) {

                if (isPayCurrencyLocal && areAllDocsCurrencyLocal) {
                    // differences not allowed for local currency:
                    throw new Exception("No debe haber diferencias en moneda local en la contabilización del pago #" + EntryNumber + "\n:"
                            + "el total de cargos $" + SLibUtils.getDecimalFormatAmount().format(totDebit) + " " + session.getSessionCustom().getLocalCurrencyCode() + " "
                            + "es distinto al total de abonos $" + SLibUtils.getDecimalFormatAmount().format(totCredit) + " " + session.getSessionCustom().getLocalCurrencyCode() + " "
                            + "por $" + SLibUtils.getDecimalFormatAmountUnitary().format(totDebit - totCredit) + " " + session.getSessionCustom().getLocalCurrencyCode() + "."); // show difference without rounding
                }
                else {
                    /*
                     * section 3: accounting of exchange rate difference or rounding issues.
                     */
                    
                    SDataRecordEntry entryDifference; // treat difference as an exchange rate difference
                    SDataParamsCompany paramsCompany = (SDataParamsCompany) session.getConfigCompany();

                    if (difference > 0) {
                        // income:
                        entryDifference = createRecordEntryDifference(session, 
                                paramsCompany.getFkAccountDifferenceIncomeId_n(), paramsCompany.getFkCostCenterDifferenceIncomeId_n(), 
                                paramsCompany.getFkItemDifferenceIncomeId_n(), CurrencyId, 
                                difference);
                    }
                    else {
                        // expense:
                        entryDifference = createRecordEntryDifference(session, 
                                paramsCompany.getFkAccountDifferenceExpenseId_n(), paramsCompany.getFkCostCenterDifferenceExpenseId_n(), 
                                paramsCompany.getFkItemDifferenceExpenseId_n(), CurrencyId, 
                                difference);
                    }

                    entryDifference.setConcept(globalConcept);
                    prepareRecordEntry(session, entryDifference, bookkeepingNumber, bizPartnerName);
                    AuxDbmsRecordEntries.add(entryDifference);
                }
            }
        }
    }
    
    /**
     * Prepares row to be displayed in GUI grid.
     */
    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(EntryNumber);
        mvValues.add(PaymentDate);
        mvValues.add(PaymentWay);
        mvValues.add(Amount);
        mvValues.add(CurrencyKey);
        mvValues.add(ExchangeRate);
        mvValues.add(DataRecord.getRecordPrimaryKey());
        mvValues.add(Operation);
        mvValues.add(AccountSrcFiscalId);
        mvValues.add(AccountSrcNumber);
        mvValues.add(AccountSrcEntity);
        mvValues.add(AccountDestFiscalId);
        mvValues.add(AccountDestNumber);
        mvValues.add(EntryTypes.get(EntryType));
        mvValues.add(AuxFactoringBankFiscalId);
        mvValues.add(AuxConceptDocsCustom);
    }
}

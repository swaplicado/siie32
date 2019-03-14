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
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SDbBizPartner;
import erp.mtrn.data.SDataCfdPayment;
import erp.mtrn.data.SDataDsm;
import erp.mtrn.data.SDataDsmEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiSession;

/**
 * GUI data structure for input of individual payments for CFDI of Payments.
 * @author Sergio Flores
 */
public final class SCfdPaymentEntry extends erp.lib.table.STableRow {
    
    public static final int TYPE_STANDARD = 1;          // standard payment
    public static final int TYPE_FACTORING_PAY = 11;    // payment make with factoring
    public static final int TYPE_FACTORING_FEE = 12;    // factoring fees considered as payment
    
    public static final HashMap<Integer, String> Types = new HashMap<>();
    
    static {
        Types.put(TYPE_STANDARD, "Estándar");
        Types.put(TYPE_FACTORING_PAY, "Factoraje: pago");
        Types.put(TYPE_FACTORING_FEE, "Factoraje: intereses y comisiones");
    }
    
    public int Number;
    public int Type;    // payment type
    public Date Date;
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
    public String AccountDesFiscalId;
    public String AccountDesNumber;
    public int[] AccountDesKey;
    public SDataRecord DataRecord;
    public SDataCfdPayment DataParentPayment;
    public ArrayList<SCfdPaymentEntryDoc> PaymentEntryDocs;
    
    public int AuxGridIndex;
    public int AuxUserId;
    public int AuxFactoringBankId;
    public String AuxFactoringBankFiscalId;
    public double AuxTotalPayments;
    public double AuxTotalPaymentsLocal;
    public double AuxTotalLimMin;
    public double AuxTotalLimMax;
    public boolean AuxAllowTotalPaymentsLessThanAmount;
    public boolean AuxAllowTotalPaymentsLocalLessThanAmountLocal;
    public boolean AuxAllowTotalPaymentsLocalGreaterThanAmountLocal;
    public ArrayList<SDataRecordEntry> AuxDbmsRecordEntries;
    
    public SCfdPaymentEntry(int number, int type, Date date, String paymentWay, int currencyId, String currencyKey, double amount, double exchangeRate, SDataRecord dataRecord, SDataCfdPayment parentPayment) {
        Number = number;
        Type = type;
        Date = date;
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
        AccountDesFiscalId = "";
        AccountDesNumber = "";
        AccountDesKey = null;
        DataRecord = dataRecord;
        DataParentPayment = parentPayment;
        PaymentEntryDocs = new ArrayList<>();
        
        AuxGridIndex = -1;
        AuxUserId = 0;
        AuxFactoringBankId = 0;
        AuxFactoringBankFiscalId = "";
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
        
        for (SCfdPaymentEntryDoc paymentEntryDoc : PaymentEntryDocs) {
            paymentEntryDoc.computePaymentAmounts();
            AuxTotalPayments = SLibUtils.roundAmount(AuxTotalPayments + paymentEntryDoc.PayPayment);
            AuxTotalPaymentsLocal = SLibUtils.roundAmount(AuxTotalPaymentsLocal + paymentEntryDoc.PayPaymentLocal);
            AuxTotalLimMin += paymentEntryDoc.PayPaymentLimMin;
            AuxTotalLimMax += paymentEntryDoc.PayPaymentLimMax;
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
     */
    public boolean isFactoring() {
        return SLibUtils.belongsTo(Type, new int[] { TYPE_FACTORING_PAY, TYPE_FACTORING_FEE });
    }
    
    /**
     * Gets entry type.
     */
    public String getType() {
        return Types.get(Type);
    }
    
    /**
     * Checks if amount is totally applied.
     * @return 
     */
    public boolean isAmountTotallyApplied() {
        return SLibUtils.compareAmount(Amount, AuxTotalPayments);
    }

    private erp.mfin.data.SDataRecordEntry createRecordEntryAccountCash(final SGuiSession session, final SDataAccountCash accountCash, 
            final double amountCur, final double xrt, final double amountDom) throws Exception {
        int[] keySystemMoveTypeXXX = null;
        
        if (accountCash.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH) {
            keySystemMoveTypeXXX = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH;
        }
        else {
            keySystemMoveTypeXXX = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK;
        }

        SDataRecordEntry recordEntry = new SDataRecordEntry();
        
        recordEntry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        recordEntry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        recordEntry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
        recordEntry.setFkUserNewId(session.getUser().getPkUserId());
        recordEntry.setDbmsAccountingMoveSubclass(session.readField(SModConsts.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL, SDbRegistry.FIELD_NAME).toString());
        //recordEntry.setConcept(...);          // will be set in method processRecord()
        recordEntry.setDebit(amountDom);
        recordEntry.setCredit(0);
        recordEntry.setExchangeRate(xrt);
        recordEntry.setExchangeRateSystem(xrt);
        recordEntry.setDebitCy(amountCur);
        recordEntry.setCreditCy(0);
        //recordEntry.setSortingPosition(...);  // will be set when CFD Payment is saved
        recordEntry.setFkCurrencyId(accountCash.getFkCurrencyId());
        recordEntry.setFkAccountIdXXX(accountCash.getFkAccountId());
        recordEntry.setFkCostCenterIdXXX_n("");
        recordEntry.setIsExchangeDifference(false);
        recordEntry.setIsSystem(true);
        recordEntry.setIsDeleted(false);

        recordEntry.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY[0]);
        recordEntry.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY[1]);

        if (accountCash.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH) {
            recordEntry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH[0]);
            recordEntry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH[1]);
        }
        else {
            recordEntry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK[0]);
            recordEntry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK[1]);
        }

        recordEntry.setFkSystemMoveCategoryIdXXX(keySystemMoveTypeXXX[0]);
        recordEntry.setFkSystemMoveTypeIdXXX(keySystemMoveTypeXXX[1]);

        recordEntry.setDbmsAccount(SDataReadDescriptions.getCatalogueDescription((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { accountCash.getFkAccountId() }));
        recordEntry.setDbmsAccountComplement(accountCash.getDbmsCompanyBranchEntity().getEntity());
        recordEntry.setDbmsCostCenter_n("");
        recordEntry.setDbmsCurrencyKey(session.getSessionCustom().getCurrencyCode(new int[] { accountCash.getFkCurrencyId() }));

        recordEntry.setReference("");
        recordEntry.setIsReferenceTax(false);
        recordEntry.setFkTaxBasicId_n(0);
        recordEntry.setFkTaxId_n(0);

        recordEntry.setFkBizPartnerId_nr(0);
        recordEntry.setFkBizPartnerBranchId_n(0);

        recordEntry.setFkCompanyBranchId_n(accountCash.getPkCompanyBranchId());
        recordEntry.setFkEntityId_n(accountCash.getPkAccountCashId());
        recordEntry.setUnits(0d);
        recordEntry.setFkItemId_n(0);
        recordEntry.setFkItemAuxId_n(0);
        recordEntry.setFkYearId_n(0);
        recordEntry.setFkDpsYearId_n(0);
        recordEntry.setFkDpsDocId_n(0);
        recordEntry.setFkDpsAdjustmentYearId_n(0);
        recordEntry.setFkDpsAdjustmentDocId_n(0);
        //recordEntry.setFkBookkeepingYearId_n(...);    // will be set in method processRecord()
        //recordEntry.setFkBookkeepingNumberId_n(...);  // will be set in method processRecord()

        return recordEntry;
    }

    private erp.mfin.data.SDataRecordEntry createRecordEntryDoubleEntry(final SGuiSession session, 
            final String accountId, final String costCenterId, final int itemId, final int currencyId,
            final double amountCur, final double xrt, final double amountDom) throws Exception {
        SDataRecordEntry recordEntry = new SDataRecordEntry();
        
        recordEntry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        recordEntry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        recordEntry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
        recordEntry.setFkUserNewId(session.getUser().getPkUserId());
        recordEntry.setDbmsAccountingMoveSubclass(session.readField(SModConsts.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL, SDbRegistry.FIELD_NAME).toString());
        //recordEntry.setConcept(...);          // will be set in method processRecord()
        recordEntry.setDebit(amountDom);
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
        //recordEntry.setFkBookkeepingYearId_n(...);    // will be set in method processRecord()
        //recordEntry.setFkBookkeepingNumberId_n(...);  // will be set in method processRecord()

        return recordEntry;
    }

    private erp.mfin.data.SDataRecordEntry createRecordEntryXrtDiff(final SGuiSession session, 
            final String accountId, final String costCenterId, final int itemId, final int currencyId,
            final double xrtDiff) throws Exception {
        SDataRecordEntry recordEntry = new SDataRecordEntry();
        
        recordEntry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        recordEntry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        recordEntry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
        recordEntry.setFkUserNewId(session.getUser().getPkUserId());
        recordEntry.setDbmsAccountingMoveSubclass(session.readField(SModConsts.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL, SDbRegistry.FIELD_NAME).toString());
        //recordEntry.setConcept(...);          // will be set in method processRecord()
        if (xrtDiff >= 0) {
            recordEntry.setDebit(0);
            recordEntry.setCredit(xrtDiff);   // income
        }
        else {
            recordEntry.setDebit(-xrtDiff);   // expense
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
        //recordEntry.setFkBookkeepingYearId_n(...);    // will be set in method processRecord()
        //recordEntry.setFkBookkeepingNumberId_n(...);  // will be set in method processRecord()

        return recordEntry;
    }
    
    /**
     * This will update supplied recor entry!
     */
    private void normalizeRecordEntry(SGuiSession session, SDataRecordEntry recordEntry, SDataBookkeepingNumber bookkeepingNumber, String bizPartnerName) {
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

    /**
     * Process all entries of record (journal voucher) for accounting of payment.
     * NOTE: This method must be called in GUI form, before saving this registry!
     * @param session GUI session.
     * @throws Exception 
     */
    public void processRecord(final SGuiSession session) throws Exception {
        // process new journal voucher movements:
        
        AuxUserId = session.getUser().getPkUserId();
        AuxDbmsRecordEntries.clear();
        
        SDataDsm oDsm = new SDataDsm();
        
        HashSet<String> documentsSet = new HashSet<>();
        String bizPartnerName = session.readField(SModConsts.BPSU_BP, new int[] { DataParentPayment.getAuxCfdDbmsDataReceptor().getPkBizPartnerId() }, SDbBizPartner.FIELD_NAME_COMM).toString();
        SDataAccountCash accountCash = (SDataAccountCash) SDataUtilities.readRegistry((SClientInterface) session.getClient(), SDataConstants.FIN_ACC_CASH, AccountDesKey, SLibConstants.EXEC_MODE_VERBOSE);
        
        // bookkeeping of application of payments:
        
        boolean areAllDocsLocal = true;

        for (SCfdPaymentEntryDoc paymentEntryDoc : PaymentEntryDocs) {
            // check if all docs are in local currency:
            
            if (!session.getSessionCustom().isLocalCurrency(new int[] { paymentEntryDoc.DataDps.getFkCurrencyId() })) {
                areAllDocsLocal = false;
            }
            
            // generate DSM entry for the accounting of current payment:
            
            SDataDsmEntry oDsmEntry = new SDataDsmEntry();

            oDsmEntry.setPkYearId(session.getCurrentYear());
            oDsmEntry.setFkUserNewId(session.getUser().getPkUserId());

            oDsmEntry.setSourceReference("");
            oDsmEntry.setFkSourceCurrencyId(CurrencyId);
            oDsmEntry.setSourceValueCy(paymentEntryDoc.PayPayment);
            oDsmEntry.setSourceValue(SLibUtils.roundAmount(paymentEntryDoc.PayPayment * ExchangeRate));
            oDsmEntry.setSourceExchangeRateSystem(ExchangeRate);
            oDsmEntry.setSourceExchangeRate(ExchangeRate);
            
            boolean isLocalCurrency = session.getSessionCustom().isLocalCurrency(new int[] { paymentEntryDoc.DataDps.getFkCurrencyId() });
            double xrt;
            
            if (isLocalCurrency) {
                xrt = 1d;
                if (Math.abs(paymentEntryDoc.DocPayment - paymentEntryDoc.PayPaymentLocal) >= 0.01) {
                    throw new Exception("Hay una diferencia considerable en pago en moneda local al documento " + paymentEntryDoc.DataDps.getDpsNumber() + ": " + SLibUtils.roundAmount(paymentEntryDoc.DocPayment - paymentEntryDoc.PayPaymentLocal) + ".");
                }
            }
            else {
                xrt = paymentEntryDoc.ExchangeRate == 0d ? 0d : SLibUtils.round(ExchangeRate / paymentEntryDoc.ExchangeRate, SLibUtils.getDecimalFormatExchangeRate().getMaximumFractionDigits());
            }
            
            oDsmEntry.setFkDestinyDpsYearId_n(paymentEntryDoc.DataDps.getPkYearId());
            oDsmEntry.setFkDestinyDpsDocId_n(paymentEntryDoc.DataDps.getPkDocId());
            oDsmEntry.setFkDestinyCurrencyId(paymentEntryDoc.DataDps.getFkCurrencyId());
            oDsmEntry.setDestinyValueCy(paymentEntryDoc.DocPayment);
            oDsmEntry.setDestinyValue(paymentEntryDoc.PayPaymentLocal);
            oDsmEntry.setDestinyExchangeRateSystem(xrt);
            oDsmEntry.setDestinyExchangeRate(xrt);
            oDsmEntry.setDbmsFkDpsCategoryId(paymentEntryDoc.DataDps.getFkDpsCategoryId());
            oDsmEntry.setDbmsDestinyDps(paymentEntryDoc.DataDps.getDpsNumber());
            oDsmEntry.setDbmsSubclassMove(session.readField(SModConsts.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP, SDbRegistry.FIELD_NAME).toString());
            oDsmEntry.setDbmsBizPartner(bizPartnerName);
            oDsmEntry.setDbmsDestinyTpDps(session.readField(SModConsts.TRNU_TP_DPS, paymentEntryDoc.DataDps.getDpsTypeKey(), SDbRegistry.FIELD_CODE).toString());

            oDsmEntry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP[0]);
            oDsmEntry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP[1]);
            oDsmEntry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP[2]);
            oDsmEntry.setDbmsCtSysMovId(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[0]);
            oDsmEntry.setDbmsTpSysMovId(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1]);
            
            oDsmEntry.setFkBizPartnerId(paymentEntryDoc.DataDps.getFkBizPartnerId_r());
            oDsmEntry.setDbmsFkBizPartnerBranchId_n(paymentEntryDoc.DataDps.getFkBizPartnerBranchId());

            Vector<SFinAccountConfigEntry> accountConfigEntries = SFinAccountUtilities.obtainBizPartnerAccountConfigs((SClientInterface) session.getClient(), paymentEntryDoc.DataDps.getFkBizPartnerId_r(), SDataConstantsSys.BPSS_CT_BP_CUS,
                    DataRecord.getPkBookkeepingCenterId(), DataRecord.getDate(), SDataConstantsSys.FINS_TP_ACC_BP_OP, paymentEntryDoc.DataDps.getFkDpsCategoryId() == SDataConstantsSys.TRNS_CT_DPS_SAL);
            if (!accountConfigEntries.isEmpty()) {
                oDsmEntry.setDbmsAccountOp(accountConfigEntries.get(0).getAccountId());
            }
            
            oDsm.getDbmsEntry().add(oDsmEntry);

            oDsm.setDbmsPkRecordTypeId(SDataConstantsSys.FINU_TP_REC_SUBSYS_CUS);
            oDsm.setDbmsSubsystemTypeBiz(session.readField(SModConsts.BPSS_CT_BP, new int[] { SDataConstantsSys.BPSS_CT_BP_CUS }, SDbRegistry.FIELD_CODE) + "");

            oDsm.setDate(session.getCurrentDate());
            oDsm.setDbmsErpTaxModel(((SDataParamsErp) session.getConfigSystem()).getTaxModel());
            oDsm.setFkSubsystemCategoryId(SDataConstantsSys.BPSS_CT_BP_CUS);
            oDsm.setFkCompanyBranchId(DataRecord.getFkCompanyBranchId_n());
            oDsm.setFkUserNewId(session.getUser().getPkUserId());
            oDsm.setDbmsFkCompanyBranch(((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getPkBizPartnerBranchId());
            oDsm.setDbmsCompanyBranchCode(((SClientInterface) session.getClient()).getSessionXXX().getCompany().getDbmsDataCompany().getDbmsBizPartnerBranch(new int[] { DataRecord.getFkCompanyBranchId_n() }).getCode());
            oDsm.setDbmsErpDecimalsValue(((SDataParamsErp) session.getConfigSystem()).getDecimalsValue());
            oDsm.setDbmsIsRecordSaved(false);

            oDsm = (SDataDsm) ((SClientInterface) session.getClient()).getGuiModule(SDataConstants.MOD_FIN).processRegistry(oDsm);
            
            String concept;
            if (session.getSessionCustom().isLocalCurrency(new int[] { CurrencyId })) {
                // payment in domestic currency:
                concept = "F " + paymentEntryDoc.DataDps.getDpsNumber() + " " + 
                        bizPartnerName + " " + 
                        accountCash.getAuxCode();
            }
            else {
                // payment in foreign currency:
                concept = "F " + paymentEntryDoc.DataDps.getDpsNumber() + " " + 
                        bizPartnerName + " " + 
                        "$" + SLibUtils.getDecimalFormatAmount().format(paymentEntryDoc.PayPayment) + " " + CurrencyKey + " " + 
                        "TC " + SLibUtils.getDecimalFormatExchangeRate().format(ExchangeRate) + " " + 
                        accountCash.getAuxCode();
            }
            
            for (SDataRecordEntry entry : oDsm.getDbmsRecord().getDbmsRecordEntries()) {
                entry.setConcept(concept); // same concept for all related bookkeeping registries
                AuxDbmsRecordEntries.add(entry);
            }
            
            if (Type == TYPE_FACTORING_FEE) {
                // accounting counterpart for factoring fees:
                
                // add as well bokkeeping registry for cash account:
                SDataRecordEntry entryDoubleEntry = null;
                SDataParamsCompany paramsCompany = (SDataParamsCompany) session.getConfigCompany();
                
                switch (paymentEntryDoc.Type) {
                    case SCfdPaymentEntryDoc.TYPE_PAY:
                        throw new Exception("Opción inválida para intereses y comisiones de factoraje.");
                        
                    case SCfdPaymentEntryDoc.TYPE_INT:
                        entryDoubleEntry = createRecordEntryDoubleEntry(session, 
                                paramsCompany.getFkCfdPaymentAccountExpensesId_n(), paramsCompany.getFkCfdPaymentCostCenterExpensesId_n(), 
                                paramsCompany.getFkCfdPaymentItemBankInterestId_n(), CurrencyId, 
                                paymentEntryDoc.PayPayment, ExchangeRate, paymentEntryDoc.PayPaymentLocal);
                        break;
                        
                    case SCfdPaymentEntryDoc.TYPE_FEE:
                        entryDoubleEntry = createRecordEntryDoubleEntry(session, 
                                paramsCompany.getFkCfdPaymentAccountExpensesId_n(), paramsCompany.getFkCfdPaymentCostCenterExpensesId_n(), 
                                paramsCompany.getFkCfdPaymentItemBankFeeId_n(), CurrencyId, 
                                paymentEntryDoc.PayPayment, ExchangeRate, paymentEntryDoc.PayPaymentLocal);
                        break;
                        
                    case SCfdPaymentEntryDoc.TYPE_FEE_VAT:
                        String accountId = SFinAccountUtilities.obtainTaxAccountId(
                                new int[] { paramsCompany.getFkCfdPaymentBankFeeTaxBasicId_n(), paramsCompany.getFkCfdPaymentBankFeeTaxId_n() }, 
                                SDataConstantsSys.TRNS_CT_DPS_PUR, Date, SDataConstantsSys.FINX_ACC_PAY, session.getStatement()); // VAT in favor, so it is treat as VAT from purchases
                        
                        entryDoubleEntry = createRecordEntryDoubleEntry(session, 
                                accountId, "", 
                                0, CurrencyId, 
                                paymentEntryDoc.PayPayment, ExchangeRate, paymentEntryDoc.PayPaymentLocal);
                        break;
                        
                    default:
                }
                
                entryDoubleEntry.setConcept(concept);
                AuxDbmsRecordEntries.add(entryDoubleEntry);
            }
            
            oDsm.getDbmsEntry().clear();
            
            documentsSet.add(paymentEntryDoc.DataDps.getDpsNumber());
        }
        
        String documents = "";
        for (String document : documentsSet.toArray(new String[0])) {
            documents += (documents.isEmpty() ? "" : " ") + document;
        }
        
        String concept;
        if (session.getSessionCustom().isLocalCurrency(new int[] { CurrencyId })) {
            // payment in domestic currency:
            concept = "F " + documents + " " + 
                    bizPartnerName + " " + 
                    accountCash.getAuxCode();
        }
        else {
            // payment in foreign currency:
            concept = "F " + documents + " " + 
                    bizPartnerName + " " + 
                    "$" + SLibUtils.getDecimalFormatAmount().format(Amount) + " " + CurrencyKey + " " + 
                    "TC " + SLibUtils.getDecimalFormatExchangeRate().format(ExchangeRate) + " " + 
                    accountCash.getAuxCode();
        }
        
        if (Type == TYPE_STANDARD || Type == TYPE_FACTORING_PAY) {
            SDataRecordEntry entryAccountCash = createRecordEntryAccountCash(session, accountCash, Amount, ExchangeRate, AmountLocal);
            entryAccountCash.setConcept(concept);
            AuxDbmsRecordEntries.add(entryAccountCash);
        }
        
        SDataBookkeepingNumber bookkeepingNumber = new SDataBookkeepingNumber();
        bookkeepingNumber.setPkYearId(DataRecord.getPkYearId());
        bookkeepingNumber.setFkUserNewId(session.getUser().getPkUserId());
        if (bookkeepingNumber.save(session.getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
        }
        
        double totDebit = 0;
        double totCredit = 0;

        for (SDataRecordEntry entry : AuxDbmsRecordEntries) {
            normalizeRecordEntry(session, entry, bookkeepingNumber, bizPartnerName);
            
            totDebit = SLibUtils.roundAmount(totDebit + entry.getDebit());
            totCredit = SLibUtils.roundAmount(totCredit + entry.getCredit());
        }
        
        if (isAmountTotallyApplied()) {
            double xrtDiff = SLibUtils.roundAmount(totDebit - totCredit);
            
            if (Math.abs(xrtDiff) >= 0.01) {
                if (session.getSessionCustom().isLocalCurrency(new int[] { CurrencyId }) && areAllDocsLocal) {
                    // differences not allowed for system currency:
                    throw new Exception("La suma de cargos " + SLibUtils.getDecimalFormatAmount().format(totDebit) + " " + session.getSessionCustom().getLocalCurrencyCode() + " " +
                            "es distinto a la suma de abonos " + SLibUtils.getDecimalFormatAmount().format(totCredit) + " " + session.getSessionCustom().getLocalCurrencyCode() + " " +
                            "por " + SLibUtils.getDecimalFormatAmountUnitary().format(totDebit - totCredit) + ".");
                }
                else {
                    // treat difference as exchange rate difference:
                    SDataParamsCompany paramsCompany = (SDataParamsCompany) session.getConfigCompany();

                    SDataRecordEntry entryXrtDiff;

                    if (xrtDiff > 0) {
                        entryXrtDiff = createRecordEntryXrtDiff(session, 
                                paramsCompany.getFkAccountDifferenceIncomeId_n(), paramsCompany.getFkCostCenterDifferenceIncomeId_n(), 
                                paramsCompany.getFkItemDifferenceIncomeId_n(), CurrencyId, 
                                xrtDiff);
                    }
                    else {
                        entryXrtDiff = createRecordEntryXrtDiff(session, 
                                paramsCompany.getFkAccountDifferenceExpenseId_n(), paramsCompany.getFkCostCenterDifferenceExpenseId_n(), 
                                paramsCompany.getFkItemDifferenceExpenseId_n(), CurrencyId, 
                                xrtDiff);
                    }

                    normalizeRecordEntry(session, entryXrtDiff, bookkeepingNumber, bizPartnerName);
                    entryXrtDiff.setConcept(concept);
                    AuxDbmsRecordEntries.add(entryXrtDiff);
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
        mvValues.add(Number);
        mvValues.add(Date);
        mvValues.add(PaymentWay);
        mvValues.add(Amount);
        mvValues.add(CurrencyKey);
        mvValues.add(ExchangeRate);
        mvValues.add(DataRecord.getRecordPrimaryKey());
        mvValues.add(Operation);
        mvValues.add(AccountSrcFiscalId);
        mvValues.add(AccountSrcNumber);
        mvValues.add(AccountSrcEntity);
        mvValues.add(AccountDesFiscalId);
        mvValues.add(AccountDesNumber);
        mvValues.add(Types.get(Type));
        mvValues.add(AuxFactoringBankFiscalId);
    }
}

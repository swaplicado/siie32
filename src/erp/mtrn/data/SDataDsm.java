/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mod.SModSysConsts;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SDataDsm extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected java.util.Date mtDate;
    protected java.lang.String msConcept;
    protected boolean mbIsAudited;
    protected boolean mbIsAuthorized;
    protected boolean mbIsRecordAutomatic;
    protected boolean mbIsSystem;
    protected boolean mbIsDeleted;
    protected int mnFkSubsystemCategoryId;
    protected int mnFkCompanyBranchId;
    protected int mnFkUserAuditedId;
    protected int mnFkUserAuthorizedId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserAuditedTs;
    protected java.util.Date mtUserAuthorizedTs;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected boolean mbDbmsIsRecordSaved;
    protected int mnDbmsPkPeriodId;
    protected int mnDbmsPkBookkeepingCenterId;
    protected int mnDbmsFkCompanyBranch;
    protected int mnDbmsErpDecimalsValue;
    protected int mnDbmsTaxModel;
    protected java.lang.String msDbmsCompanyBranchCode;
    protected java.lang.String msDbmsPkRecordTypeId;
    protected java.lang.String msDbmsSubsystemTypeBiz;

    protected boolean mbAuxIsRecordAutomatic;
    protected java.util.Date mtDbmsRecordDate;
    protected java.lang.Object moAuxRecordUserKey;
    protected java.lang.Object moDbmsRecordKey;

    protected java.util.Vector<erp.mtrn.data.SDataDsmNotes> mvDbmsDsmNotes;
    protected java.util.Vector<erp.mtrn.data.SDataDsmEntry> mvDbmsDsmEntries;

    protected erp.mfin.data.SDataRecord moDbmsRecord;
    protected int mnParamPkCheckWalletId;
    protected int mnParamPkCheckId;

    public SDataDsm() {
        super(SDataConstants.TRN_DSM);

        mvDbmsDsmNotes = new Vector<>();
        mvDbmsDsmEntries = new Vector<>();
        reset();
    }

    /*
     * Private methods
     */

    private erp.mfin.data.SDataRecord saveRecord(java.sql.Connection connection, boolean isNewRecord) {
        SDataRecord record = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            // 4.2.2 Save accounting record:

            record = new SDataRecord();
            Statement statement = connection.createStatement();
            String concept = msDbmsSubsystemTypeBiz + "; " + msDbmsCompanyBranchCode + "; " + SLibUtils.DateFormatDate.format(mtDate);
            
            if (!isNewRecord) {
                if (record.read(moDbmsRecordKey, statement) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                if (record.getIsDeleted()) {
                    record.setIsDeleted(false);
                }

                if (mbIsRecordAutomatic) {
                    record.setConcept(concept.length() > 100 ? concept.substring(0, 96).trim() + "..." : concept);
                }
            }
            else {
                record.setPkYearId(moDbmsRecordKey != null ? (Integer) ((Object[]) moDbmsRecordKey)[0] : mnPkYearId);
                record.setPkPeriodId(moDbmsRecordKey != null ? (Integer) ((Object[]) moDbmsRecordKey)[1] : mnDbmsPkPeriodId);
                record.setPkBookkeepingCenterId(moDbmsRecordKey != null ? (Integer) ((Object[]) moDbmsRecordKey)[2] : mnDbmsPkBookkeepingCenterId);
                record.setPkRecordTypeId(moDbmsRecordKey != null ? (String) ((Object[]) moDbmsRecordKey)[3] : msDbmsPkRecordTypeId);
                record.setPkNumberId(0);
                record.setConcept(concept.length() > 100 ? concept.substring(0, 96).trim() + "..." : concept);
                record.setDate(mtDate);
                record.setIsAudited(false);
                record.setIsAuthorized(false);
                record.setIsSystem(true);
                record.setIsDeleted(false);
                record.setFkCompanyBranchId(mnFkCompanyBranchId);
                record.setFkCompanyBranchId_n(0);
                record.setFkAccountCashId_n(0);

                if (mbIsRegistryNew) {
                    record.setFkUserNewId(mnFkUserNewId);
                    record.setFkUserEditId(mnFkUserNewId);
                }
                else {
                    record.setFkUserNewId(mnFkUserEditId);
                    record.setFkUserEditId(mnFkUserEditId);
                }
            }

            // Save record entries:

            mnLastDbActionResult = saveRecordEntries(connection, record);

            if (mbDbmsIsRecordSaved) {
                if (record.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
            }

            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return record;
    }

    private erp.mfin.data.SDataRecordEntry createRecordEntry(int numberId, erp.mtrn.data.SDataDsmEntry dsmEntry, java.lang.String concept, int sortPos) {
        SDataRecordEntry recordEntry = new SDataRecordEntry();
        
        // Prepare record entry header:

        if (moDbmsRecordKey != null) {
            recordEntry.setPkYearId((Integer) ((Object[]) moDbmsRecordKey)[0]);
            recordEntry.setPkPeriodId((Integer) ((Object[]) moDbmsRecordKey)[1]);
            recordEntry.setPkBookkeepingCenterId((Integer) ((Object[]) moDbmsRecordKey)[2]);
            recordEntry.setPkRecordTypeId((String) ((Object[]) moDbmsRecordKey)[3]);
        }
        else {
            recordEntry.setPkYearId(mnPkYearId);
            recordEntry.setPkPeriodId(mnDbmsPkPeriodId);
            recordEntry.setPkBookkeepingCenterId(mnDbmsPkBookkeepingCenterId);
            recordEntry.setPkRecordTypeId(msDbmsPkRecordTypeId);
        }
        
        recordEntry.setPkNumberId(numberId);
        recordEntry.setPkEntryId(0);
        
        // Prepare record entry body:

        recordEntry.setConcept(concept.length() > 100 ? concept.substring(0, 96).trim() + "..." : concept);
        recordEntry.setReference("");
        recordEntry.setFkDpsYearId_n(0);
        recordEntry.setFkDpsDocId_n(0);
        recordEntry.setFkAccountIdXXX("");
        recordEntry.setUnits(0);
        recordEntry.setSortingPosition(sortPos);
        recordEntry.setIsDeleted(dsmEntry.getIsDeleted());
        recordEntry.setFkAccountingMoveTypeId(dsmEntry.getFkAccountingMoveTypeId());
        recordEntry.setFkAccountingMoveClassId(dsmEntry.getFkAccountingMoveClassId());
        recordEntry.setFkAccountingMoveSubclassId(dsmEntry.getFkAccountingMoveSubclassId());
        recordEntry.setFkCostCenterIdXXX_n("");
        recordEntry.setFkCompanyBranchId_n(mnFkCompanyBranchId);
        recordEntry.setFkEntityId_n(0);
        recordEntry.setFkTaxBasicId_n(dsmEntry.getFkTaxBasId_n());
        recordEntry.setFkTaxId_n(dsmEntry.getFkTaxId_n());
        recordEntry.setFkBizPartnerId_nr(dsmEntry.getFkBizPartnerId());
        recordEntry.setFkBizPartnerBranchId_n(dsmEntry.getDbmsFkBizPartnerBranchId_n());
        recordEntry.setFkYearId_n(0);
        //oRecordEntry.setFkDsmYearId_n(mnPkYearId);    XXX obsolete code (sflores, 2013-06-27)
        //oRecordEntry.setFkDsmDocId_n(mnPkDocId);      XXX obsolete code (sflores, 2013-06-27)
        recordEntry.setFkReferenceCategoryId_n(mnFkSubsystemCategoryId);
        recordEntry.setFkDpsAdjustmentYearId_n(0);
        recordEntry.setFkDpsAdjustmentDocId_n(0);
        recordEntry.setFkDiogYearId_n(0);
        recordEntry.setFkDiogDocId_n(0);
        recordEntry.setFkItemId_n(0);
        recordEntry.setFkCheckWalletId_n(0);
        recordEntry.setFkCheckId_n(0);
        recordEntry.setFkUserNewId(mnFkUserNewId);
        recordEntry.setFkUserEditId(1);
        recordEntry.setFkUserDeleteId(1);
        recordEntry.setUserNewTs(mtDate);
        recordEntry.setUserEditTs(mtDate);
        recordEntry.setUserDeleteTs(mtDate);
        
        return recordEntry;
    }

    private int saveRecordEntries(java.sql.Connection connection, erp.mfin.data.SDataRecord record) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            // Determinar si existe configuración de impuestos:
            
            boolean hasTaxConfig = false;
            
            for (SDataDsmEntry dsmEntry : mvDbmsDsmEntries) {
                hasTaxConfig = dsmEntry.getTaxPk() != null;
                if (hasTaxConfig) {
                    break;
                }
            }
            
            // Procesar movimientos contables:
            
            int sortPos = 1;
            Vector<String> accounts = new Vector<>();
            
            for (SDataDsmEntry dsmEntry : mvDbmsDsmEntries) {
                if (dsmEntry != null) {
                    // Render concept for the entry:

                    String concept = renderSubsystemSourceToDestiny(dsmEntry);

                    // Get accounts debit and credit:

                    accounts.removeAllElements();
                    accounts.add(dsmEntry.getDbmsAccountPay());
                    accounts.add(dsmEntry.getDbmsAccountOp());

                    for (int accountCase = 0; accountCase < accounts.size(); accountCase++) {
                        // Check if account isn't empty [SFormRecordApp]:

                        if (!accounts.get(accountCase).isEmpty()) {
                            SDataRecordEntry recordEntry = createRecordEntry(0, dsmEntry, concept, sortPos);

                            // Save accounting data depending of subsystem type:

                            recordEntry = renderReferenceDps(accountCase, dsmEntry, recordEntry);
                            recordEntry.setIsSystem(true);
                            record.getDbmsRecordEntries().add(recordEntry);

                            sortPos++;
                        }
                    }

                    // Record entry for taxes source:

                    if (dsmEntry.getFkSourceDpsYearId_n() != 0 && dsmEntry.getFkSourceDpsDocId_n() != 0) {
                        sortPos = calculateDpsTaxes(connection, record, dsmEntry, SDataConstants.TRNX_DSM_ETY_SOURCE, sortPos, 0, dsmEntry.getFkSourceDpsYearId_n(), dsmEntry.getFkSourceDpsDocId_n(), concept);
                    }

                    // Record entry for taxes destiny:

                    if (dsmEntry.getFkDestinyDpsYearId_n() != 0 && dsmEntry.getFkDestinyDpsDocId_n() != 0) {
                        sortPos = calculateDpsTaxes(connection, record, dsmEntry, SDataConstants.TRNX_DSM_ETY_DESTINY, sortPos, 0, dsmEntry.getFkDestinyDpsYearId_n(), dsmEntry.getFkDestinyDpsDocId_n(), concept);
                    }

                    mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
                }
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    private int saveRecordEntriesTaxes(erp.mfin.data.SDataRecord record, erp.mtrn.data.SDataDsmEntry dsmEntry,
            int numberId, java.lang.String concept, int dpsYearId, int dpsDocId, int dpsCategoryId, int sortPos,
            Vector<Object> typeSysMovIds, int taxBasicId, int taxId, double valueTax, double valueTaxCur, int typeSource) {
        Vector<Object> accounts = new Vector<>();

        // Add accounts (debit, credit):

        accounts.add(typeSysMovIds.get(0));
        accounts.add(typeSysMovIds.get(3));

        try {
            for (int index = 0; index < accounts.size(); index++) {
                SDataRecordEntry recordEntry = createRecordEntry(numberId, dsmEntry, concept, sortPos);

                // Complement record entry:

                recordEntry.setFkDpsYearId_n(dpsYearId);
                recordEntry.setFkDpsDocId_n(dpsDocId);

                recordEntry.setFkAccountIdXXX(accounts.get(index).toString());
                recordEntry.setFkTaxBasicId_n(taxBasicId);
                recordEntry.setFkTaxId_n(taxId);
                recordEntry = renderRecordEntryValuesTaxes(recordEntry, dsmEntry,
                        index, valueTax, valueTaxCur, typeSource, typeSysMovIds, dpsCategoryId);
                recordEntry.setIsSystem(true);
                record.getDbmsRecordEntries().add(recordEntry);

                sortPos = sortPos + 1;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return sortPos;
    }

    private erp.mfin.data.SDataRecordEntry renderRecordEntryValuesTaxes(erp.mfin.data.SDataRecordEntry recordEntry, erp.mtrn.data.SDataDsmEntry dsmEntry,
            int index, double valueTax, double valueTaxCur, int typeSource, Vector<Object> typeSysMovIds, int dpsCategoryId) {
        int[] sysMoveTypeKey = null;

        // Assign values depending if it is debit or credit:

        if (index == 0) {
            recordEntry.setDebit(valueTax);
            recordEntry.setCredit(0);
            recordEntry.setDebitCy(valueTaxCur);
            recordEntry.setCreditCy(0);

            // Set type system move:

            recordEntry.setFkSystemMoveCategoryIdXXX((Integer)((int [])  typeSysMovIds.get(1))[0]);
            recordEntry.setFkSystemMoveTypeIdXXX((Integer)((int [])  typeSysMovIds.get(1))[1]);
            recordEntry.setReference(typeSysMovIds.get(2).toString());

            switch (dpsCategoryId) {
                case SDataConstantsSys.TRNS_CT_DPS_PUR:
                    sysMoveTypeKey = SModSysConsts.FINS_TP_SYS_MOV_PUR_TAX_DBT_PAI;
                    break;
                case SDataConstantsSys.TRNS_CT_DPS_SAL:
                    sysMoveTypeKey = SModSysConsts.FINS_TP_SYS_MOV_SAL_TAX_DBT_PAI;
                    break;
                default:
            }
        }
        else {
            recordEntry.setDebit(0);
            recordEntry.setCredit(valueTax);
            recordEntry.setDebitCy(0);
            recordEntry.setCreditCy(valueTaxCur);

            // Set type system move:

            recordEntry.setFkSystemMoveCategoryIdXXX((Integer)((int [])  typeSysMovIds.get(4))[0]);
            recordEntry.setFkSystemMoveTypeIdXXX((Integer)((int [])  typeSysMovIds.get(4))[1]);
            recordEntry.setReference(typeSysMovIds.get(5).toString());

            switch (dpsCategoryId) {
                case SDataConstantsSys.TRNS_CT_DPS_PUR:
                    sysMoveTypeKey = SModSysConsts.FINS_TP_SYS_MOV_PUR_TAX_CDT_PAI;
                    break;
                case SDataConstantsSys.TRNS_CT_DPS_SAL:
                    sysMoveTypeKey = SModSysConsts.FINS_TP_SYS_MOV_SAL_TAX_CDT_PAI;
                    break;
                default:
            }
        }

        recordEntry.setFkSystemMoveClassId(sysMoveTypeKey[0]);
        recordEntry.setFkSystemMoveTypeId(sysMoveTypeKey[1]);
        recordEntry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_NA_NA[0]);
        recordEntry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_NA_NA[1]);

        if (typeSource == SDataConstants.TRNX_DSM_ETY_SOURCE) {
            recordEntry.setExchangeRate(dsmEntry.getSourceExchangeRate());
            recordEntry.setExchangeRateSystem(dsmEntry.getSourceExchangeRateSystem());
            recordEntry.setFkCurrencyId(dsmEntry.getFkSourceCurrencyId());
        }
        else {
            recordEntry.setExchangeRate(dsmEntry.getDestinyExchangeRate());
            recordEntry.setExchangeRateSystem(dsmEntry.getDestinyExchangeRateSystem());
            recordEntry.setFkCurrencyId(dsmEntry.getFkDestinyCurrencyId());
        }

        return recordEntry;
    }

    private erp.mfin.data.SDataRecordEntry renderCreditDebit(erp.mfin.data.SDataRecordEntry recordEntry, java.lang.String reference, int dpsYearId, int dpsDocId,
            double debit, double credit, double debitCur, double creditCur, double exchangeRate, double exchangeRateSys, int currencyId, java.lang.String accountId,
            int[] sysAccountTypeKey, int[] sysMoveTypeKey, int[] sysMoveTypeKeyXXX) {
        recordEntry.setDebit(debit);
        recordEntry.setCredit(credit);
        recordEntry.setDebitCy(debitCur);
        recordEntry.setCreditCy(creditCur);
        recordEntry.setExchangeRate(exchangeRate);
        recordEntry.setExchangeRateSystem(exchangeRateSys);
        recordEntry.setFkCurrencyId(currencyId);
        recordEntry.setReference(reference);
        recordEntry.setFkDpsYearId_n(dpsYearId);
        recordEntry.setFkDpsDocId_n(dpsDocId);
        recordEntry.setFkAccountIdXXX(accountId);
        recordEntry.setFkSystemMoveClassId(sysMoveTypeKey[0]);
        recordEntry.setFkSystemMoveTypeId(sysMoveTypeKey[1]);
        recordEntry.setFkSystemAccountClassId(sysAccountTypeKey[0]);
        recordEntry.setFkSystemAccountTypeId(sysAccountTypeKey[1]);
        recordEntry.setFkSystemMoveCategoryIdXXX(sysMoveTypeKeyXXX[0]);
        recordEntry.setFkSystemMoveTypeIdXXX(sysMoveTypeKeyXXX[1]);

        return recordEntry;
    }

    /**
     * 
     * @param accountingCase 0 = payment; 1 = operations
     * @param dsmEntry
     * @param recordEntry
     * @return 
     */
    private erp.mfin.data.SDataRecordEntry renderReferenceDps(int accountingCase, erp.mtrn.data.SDataDsmEntry dsmEntry, erp.mfin.data.SDataRecordEntry recordEntry) {
        int[] accMovSubclassKey = new int[] {dsmEntry.getFkAccountingMoveTypeId(), dsmEntry.getFkAccountingMoveClassId(), dsmEntry.getFkAccountingMoveSubclassId() };

        if (accountingCase == 0) {
            if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        recordEntry = renderCreditDebit(recordEntry, dsmEntry.getSourceReference(), 0, 0,
                            0, dsmEntry.getSourceValue(), 0, dsmEntry.getSourceValueCy(), dsmEntry.getSourceExchangeRate(),
                            dsmEntry.getSourceExchangeRateSystem(), dsmEntry.getFkSourceCurrencyId(), dsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_MI_SUP_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_CDR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        recordEntry = renderCreditDebit(recordEntry, dsmEntry.getSourceReference(), 0, 0,
                            dsmEntry.getSourceValue(), 0, dsmEntry.getSourceValueCy(), 0, dsmEntry.getSourceExchangeRate(),
                            dsmEntry.getSourceExchangeRateSystem(), dsmEntry.getFkSourceCurrencyId(), dsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_DBR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_TRA)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        recordEntry = renderCreditDebit(recordEntry, dsmEntry.getSourceReference(), 0, 0,
                            0, dsmEntry.getSourceValue(), 0, dsmEntry.getSourceValueCy(), dsmEntry.getSourceExchangeRate(),
                            dsmEntry.getSourceExchangeRateSystem(), dsmEntry.getFkSourceCurrencyId(), dsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        recordEntry = renderCreditDebit(recordEntry, dsmEntry.getSourceReference(), 0, 0,
                            dsmEntry.getSourceValue(), 0, dsmEntry.getSourceValueCy(), 0, dsmEntry.getSourceExchangeRate(),
                            dsmEntry.getSourceExchangeRateSystem(), dsmEntry.getFkSourceCurrencyId(), dsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_CLO)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        recordEntry = renderCreditDebit(recordEntry, dsmEntry.getSourceReference(), 0, 0,
                            0, dsmEntry.getSourceValue(), 0, dsmEntry.getSourceValueCy(), dsmEntry.getSourceExchangeRate(),
                            dsmEntry.getSourceExchangeRateSystem(), dsmEntry.getFkSourceCurrencyId(), dsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_DEC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_DEC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        recordEntry = renderCreditDebit(recordEntry, dsmEntry.getSourceReference(), 0, 0,
                            dsmEntry.getSourceValue(), 0, dsmEntry.getSourceValueCy(), 0, dsmEntry.getSourceExchangeRate(),
                            dsmEntry.getSourceExchangeRateSystem(), dsmEntry.getFkSourceCurrencyId(), dsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_DEC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_DEC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_OPE)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        recordEntry = renderCreditDebit(recordEntry, "", 0, 0,
                            0, dsmEntry.getSourceValue(), 0, dsmEntry.getSourceValueCy(), dsmEntry.getSourceExchangeRate(),
                            dsmEntry.getSourceExchangeRateSystem(), dsmEntry.getFkSourceCurrencyId(), dsmEntry.getFkSourceAccountId_n(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        recordEntry = renderCreditDebit(recordEntry, "", 0, 0,
                            dsmEntry.getSourceValue(), 0, dsmEntry.getSourceValueCy(), 0, dsmEntry.getSourceExchangeRate(),
                            dsmEntry.getSourceExchangeRateSystem(), dsmEntry.getFkSourceCurrencyId(), dsmEntry.getFkSourceAccountId_n(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_APP)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        recordEntry = renderCreditDebit(recordEntry, "", dsmEntry.getFkSourceDpsYearId_n(), dsmEntry.getFkSourceDpsDocId_n(),
                            dsmEntry.getSourceValue(), 0, dsmEntry.getSourceValueCy(), 0, dsmEntry.getSourceExchangeRate(),
                            dsmEntry.getSourceExchangeRateSystem(), dsmEntry.getFkSourceCurrencyId(), dsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_MI_SUP_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_CDR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        recordEntry = renderCreditDebit(recordEntry, "", dsmEntry.getFkSourceDpsYearId_n(), dsmEntry.getFkSourceDpsDocId_n(),
                            0, dsmEntry.getSourceValue(), 0, dsmEntry.getSourceValueCy(), dsmEntry.getSourceExchangeRate(),
                            dsmEntry.getSourceExchangeRateSystem(), dsmEntry.getFkSourceCurrencyId(), dsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_DBR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_TRA)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        recordEntry = renderCreditDebit(recordEntry, "", dsmEntry.getFkSourceDpsYearId_n(), dsmEntry.getFkSourceDpsDocId_n(),
                            dsmEntry.getSourceValue(), 0, dsmEntry.getSourceValueCy(), 0, dsmEntry.getSourceExchangeRate(),
                            dsmEntry.getSourceExchangeRateSystem(), dsmEntry.getFkSourceCurrencyId(), dsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        recordEntry = renderCreditDebit(recordEntry, "", dsmEntry.getFkSourceDpsYearId_n(), dsmEntry.getFkSourceDpsDocId_n(),
                            0, dsmEntry.getSourceValue(), 0, dsmEntry.getSourceValueCy(), dsmEntry.getSourceExchangeRate(),
                            dsmEntry.getSourceExchangeRateSystem(), dsmEntry.getFkSourceCurrencyId(), dsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_CLO)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        recordEntry = renderCreditDebit(recordEntry, "", dsmEntry.getFkSourceDpsYearId_n(), dsmEntry.getFkSourceDpsDocId_n(),
                            dsmEntry.getSourceValue(), 0, dsmEntry.getSourceValueCy(), 0, dsmEntry.getSourceExchangeRate(),
                            dsmEntry.getSourceExchangeRateSystem(), dsmEntry.getFkSourceCurrencyId(), dsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_DEC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_DEC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        recordEntry = renderCreditDebit(recordEntry, "", dsmEntry.getFkSourceDpsYearId_n(), dsmEntry.getFkSourceDpsDocId_n(),
                            0, dsmEntry.getSourceValue(), 0, dsmEntry.getSourceValueCy(), dsmEntry.getSourceExchangeRate(),
                            dsmEntry.getSourceExchangeRateSystem(), dsmEntry.getFkSourceCurrencyId(), dsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_DEC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_DEC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_OPE)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        recordEntry = renderCreditDebit(recordEntry, "", 0, 0,
                            dsmEntry.getSourceValue(), 0, dsmEntry.getSourceValueCy(), 0, dsmEntry.getSourceExchangeRate(),
                            dsmEntry.getSourceExchangeRateSystem(), dsmEntry.getFkSourceCurrencyId(), dsmEntry.getFkSourceAccountId_n(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        recordEntry = renderCreditDebit(recordEntry, "", 0, 0,
                            0, dsmEntry.getSourceValue(), 0, dsmEntry.getSourceValueCy(), dsmEntry.getSourceExchangeRate(),
                            dsmEntry.getSourceExchangeRateSystem(), dsmEntry.getFkSourceCurrencyId(), dsmEntry.getFkSourceAccountId_n(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA);
                        break;
                }
            }
        }
        else {
            if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        recordEntry = renderCreditDebit(recordEntry, "", dsmEntry.getFkDestinyDpsYearId_n(), dsmEntry.getFkDestinyDpsDocId_n(),
                            dsmEntry.getDestinyValue(), 0, dsmEntry.getDestinyValueCy(), 0, dsmEntry.getDestinyExchangeRate(),
                            dsmEntry.getDestinyExchangeRateSystem(), dsmEntry.getFkDestinyCurrencyId(), dsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_MI_SUP_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_CDR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        recordEntry = renderCreditDebit(recordEntry, "", dsmEntry.getFkDestinyDpsYearId_n(), dsmEntry.getFkDestinyDpsDocId_n(),
                            0, dsmEntry.getDestinyValue(), 0, dsmEntry.getDestinyValueCy(), dsmEntry.getDestinyExchangeRate(),
                            dsmEntry.getDestinyExchangeRateSystem(), dsmEntry.getFkDestinyCurrencyId(), dsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_DBR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_TRA)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        recordEntry = renderCreditDebit(recordEntry, dsmEntry.getDestinyReference(), 0, 0,
                            dsmEntry.getDestinyValue(), 0, dsmEntry.getDestinyValueCy(), 0, dsmEntry.getDestinyExchangeRate(),
                            dsmEntry.getDestinyExchangeRateSystem(), dsmEntry.getFkDestinyCurrencyId(), dsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        recordEntry = renderCreditDebit(recordEntry, dsmEntry.getDestinyReference(), 0, 0,
                            0, dsmEntry.getDestinyValue(), 0, dsmEntry.getDestinyValueCy(), dsmEntry.getDestinyExchangeRate(),
                            dsmEntry.getDestinyExchangeRateSystem(), dsmEntry.getFkDestinyCurrencyId(), dsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_CLO)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        recordEntry = renderCreditDebit(recordEntry, "", 0, 0,
                            dsmEntry.getDestinyValue(), 0, dsmEntry.getDestinyValueCy(), 0, dsmEntry.getDestinyExchangeRate(),
                            dsmEntry.getDestinyExchangeRateSystem(), dsmEntry.getFkDestinyCurrencyId(), dsmEntry.getFkDestinyAccountId_n(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_DEC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_DEC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        recordEntry = renderCreditDebit(recordEntry, "", 0, 0,
                            0, dsmEntry.getDestinyValue(), 0, dsmEntry.getDestinyValueCy(), dsmEntry.getDestinyExchangeRate(),
                            dsmEntry.getDestinyExchangeRateSystem(), dsmEntry.getFkDestinyCurrencyId(), dsmEntry.getFkDestinyAccountId_n(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_DBR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_OPE)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        recordEntry = renderCreditDebit(recordEntry, dsmEntry.getDestinyReference(), 0, 0,
                            dsmEntry.getDestinyValue(), 0, dsmEntry.getDestinyValueCy(), 0, dsmEntry.getDestinyExchangeRate(),
                            dsmEntry.getDestinyExchangeRateSystem(), dsmEntry.getFkDestinyCurrencyId(), dsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        recordEntry = renderCreditDebit(recordEntry, dsmEntry.getDestinyReference(), 0, 0,
                            0, dsmEntry.getDestinyValue(), 0, dsmEntry.getDestinyValueCy(), dsmEntry.getDestinyExchangeRate(),
                            dsmEntry.getDestinyExchangeRateSystem(), dsmEntry.getFkDestinyCurrencyId(), dsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_APP)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        recordEntry = renderCreditDebit(recordEntry, dsmEntry.getDestinyReference(), 0, 0,
                            0, dsmEntry.getDestinyValue(), 0, dsmEntry.getDestinyValueCy(), dsmEntry.getDestinyExchangeRate(),
                            dsmEntry.getDestinyExchangeRateSystem(), dsmEntry.getFkDestinyCurrencyId(), dsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_MI_SUP_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_CDR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        recordEntry = renderCreditDebit(recordEntry, dsmEntry.getDestinyReference(), 0, 0,
                            dsmEntry.getDestinyValue(), 0, dsmEntry.getDestinyValueCy(), 0, dsmEntry.getDestinyExchangeRate(),
                            dsmEntry.getDestinyExchangeRateSystem(), dsmEntry.getFkDestinyCurrencyId(), dsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_DBR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_TRA)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        recordEntry = renderCreditDebit(recordEntry, "", dsmEntry.getFkDestinyDpsYearId_n(), dsmEntry.getFkDestinyDpsDocId_n(),
                            0, dsmEntry.getDestinyValue(), 0, dsmEntry.getDestinyValueCy(), dsmEntry.getDestinyExchangeRate(),
                            dsmEntry.getDestinyExchangeRateSystem(), dsmEntry.getFkDestinyCurrencyId(), dsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        recordEntry = renderCreditDebit(recordEntry, "", dsmEntry.getFkDestinyDpsYearId_n(), dsmEntry.getFkDestinyDpsDocId_n(),
                            dsmEntry.getDestinyValue(), 0, dsmEntry.getDestinyValueCy(), 0, dsmEntry.getDestinyExchangeRate(),
                            dsmEntry.getDestinyExchangeRateSystem(), dsmEntry.getFkDestinyCurrencyId(), dsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_CLO)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        recordEntry = renderCreditDebit(recordEntry, "", 0, 0,
                            0, dsmEntry.getDestinyValue(), 0, dsmEntry.getDestinyValueCy(), dsmEntry.getDestinyExchangeRate(),
                            dsmEntry.getDestinyExchangeRateSystem(), dsmEntry.getFkDestinyCurrencyId(), dsmEntry.getFkDestinyAccountId_n(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_DEC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_DEC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        recordEntry = renderCreditDebit(recordEntry, "", 0, 0,
                            dsmEntry.getDestinyValue(), 0, dsmEntry.getDestinyValueCy(), 0, dsmEntry.getDestinyExchangeRate(),
                            dsmEntry.getDestinyExchangeRateSystem(), dsmEntry.getFkDestinyCurrencyId(), dsmEntry.getFkDestinyAccountId_n(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_DEC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_DEC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_OPE)) {
                 switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        recordEntry = renderCreditDebit(recordEntry, "", dsmEntry.getFkDestinyDpsYearId_n(), dsmEntry.getFkDestinyDpsDocId_n(),
                            0, dsmEntry.getDestinyValue(), 0, dsmEntry.getDestinyValueCy(), dsmEntry.getDestinyExchangeRate(),
                            dsmEntry.getDestinyExchangeRateSystem(), dsmEntry.getFkDestinyCurrencyId(), dsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        recordEntry = renderCreditDebit(recordEntry, "", dsmEntry.getFkDestinyDpsYearId_n(), dsmEntry.getFkDestinyDpsDocId_n(),
                            dsmEntry.getDestinyValue(), 0, dsmEntry.getDestinyValueCy(), 0, dsmEntry.getDestinyExchangeRate(),
                            dsmEntry.getDestinyExchangeRateSystem(), dsmEntry.getFkDestinyCurrencyId(), dsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
        }

        return recordEntry;
    }

    private java.lang.String renderReference(erp.mtrn.data.SDataDsmEntry dataDsmEntry, boolean isSource) {
        return "REP. '" + (isSource ? dataDsmEntry.getSourceReference() : dataDsmEntry.getDestinyReference())  + "' ";
    }

    private java.lang.String renderSubsystemSourceToDestiny(erp.mtrn.data.SDataDsmEntry dsmEntry) {
        int[] accMovSubclassKey = new int[] {dsmEntry.getFkAccountingMoveTypeId(), dsmEntry.getFkAccountingMoveClassId(), dsmEntry.getFkAccountingMoveSubclassId() };
        String sSubsystem = "";

        if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP)) {
            sSubsystem = "APP; " + dsmEntry.getDbmsDestinyTpDps() + " " + dsmEntry.getDbmsDestinyDps() + "; " +
                    (dsmEntry.getDbmsBizPartner().toString().length() > 30 ? dsmEntry.getDbmsBizPartner().toString().substring(0, 27) + "..." : dsmEntry.getDbmsBizPartner()) + "; " +
                    renderReference(dsmEntry, true);
        }
        else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_TRA)) {
            sSubsystem = "TRP; " + renderReference(dsmEntry, true) + " > " + renderReference(dsmEntry, false) + "; " +
                    (dsmEntry.getDbmsBizPartner().toString().length() > 30 ? dsmEntry.getDbmsBizPartner().toString().substring(0, 27) + "..." : dsmEntry.getDbmsBizPartner());
        }
        else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_CLO)) {
            sSubsystem = "CIP; " + dsmEntry.getFkDestinyAccountId_n() + "; " +
                    (dsmEntry.getDbmsBizPartner().toString().length() > 30 ? dsmEntry.getDbmsBizPartner().toString().substring(0, 27) + "..." : dsmEntry.getDbmsBizPartner()) + "; "  +
                    renderReference(dsmEntry, true);
        }
        else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_OPE)) {
            sSubsystem = "ABP; " + dsmEntry.getFkSourceAccountId_n() + "; " +
                    (dsmEntry.getDbmsBizPartner().toString().length() > 30 ? dsmEntry.getDbmsBizPartner().toString().substring(0, 27) + "..." : dsmEntry.getDbmsBizPartner()) + "; "  +
                    renderReference(dsmEntry, false);
        }
        else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_APP)) {
            sSubsystem = "APS; " + dsmEntry.getDbmsSourceTpDps() + " " + dsmEntry.getDbmsSourceDps() + "; " +
                    (dsmEntry.getDbmsBizPartner().toString().length() > 30 ? dsmEntry.getDbmsBizPartner().toString().substring(0, 27) + "..." : dsmEntry.getDbmsBizPartner()) + "; "  +
                    renderReference(dsmEntry, false);
        }
        else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_TRA)) {
            sSubsystem = "TRS; " + dsmEntry.getDbmsSourceTpDps() + " " + dsmEntry.getDbmsSourceDps() + " > " +
                    dsmEntry.getDbmsDestinyTpDps() + " " + dsmEntry.getDbmsDestinyDps() + "; " +
                    (dsmEntry.getDbmsBizPartner().toString().length() > 30 ? dsmEntry.getDbmsBizPartner().toString().substring(0, 27) + "..." : dsmEntry.getDbmsBizPartner());
        }
        else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_CLO)) {
            sSubsystem = "CIS; " + dsmEntry.getDbmsSourceTpDps() + " " + dsmEntry.getDbmsSourceDps() + "; " +
                    (dsmEntry.getDbmsBizPartner().toString().length() > 30 ? dsmEntry.getDbmsBizPartner().toString().substring(0, 27) + "..." : dsmEntry.getDbmsBizPartner()) + "; "  +
                     dsmEntry.getFkDestinyAccountId_n();
        }
        else if (SLibUtilities.compareKeys(accMovSubclassKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_OPE)) {
            sSubsystem = "ABS; " + dsmEntry.getDbmsDestinyTpDps() + " " + dsmEntry.getDbmsDestinyDps() + "; " +
                    (dsmEntry.getDbmsBizPartner().toString().length() > 30 ? dsmEntry.getDbmsBizPartner().toString().substring(0, 27) + "..." : dsmEntry.getDbmsBizPartner()) + "; "  +
                    dsmEntry.getFkSourceAccountId_n();
        }

        return sSubsystem;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtSupSrc(java.util.Vector<Object> vTpSysMov, java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType) {
        // [DEBIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPay : sAccTaxPayPend);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND);
        vTpSysMov.add("");

        // [CREDIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPend : sAccTaxPay);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT);
        vTpSysMov.add("");

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtCusSrc(java.util.Vector<Object> vTpSysMov, java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType) {
        // [DEBIT] Set values for type system move (outstanding tax charge):

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPend : sAccTaxPay);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT);
        vTpSysMov.add("");

        // [CREDIT] Set values for type system move (tax charge):

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPay : sAccTaxPayPend);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND);
        vTpSysMov.add("");

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtSupDest(java.util.Vector<Object> vTpSysMov, java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType) {
        // [DEBIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPend : sAccTaxPay);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT);
        vTpSysMov.add("");

        // [CREDIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPay : sAccTaxPayPend);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND);
        vTpSysMov.add("");

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtCusDest(java.util.Vector<Object> vTpSysMov, java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType) {
        // [DEBIT] Set values for type system move (outstanding tax charge):

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPay : sAccTaxPayPend);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND);
        vTpSysMov.add("");

        // [CREDIT] Set values for type system move (tax charge):

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPend : sAccTaxPay);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT);
        vTpSysMov.add("");

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtSupSrcAdv(java.util.Vector<Object> vTpSysMov, java.lang.String sAccTaxPayPend, java.lang.String sAccTaxPayPendAdv, int nPkTaxType) {
        // [DEBIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPendAdv : sAccTaxPayPend);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND_ADV : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND);
        vTpSysMov.add("");

        // [CREDIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPend : sAccTaxPayPendAdv);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND_ADV);
        vTpSysMov.add("");

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtSupDestAdv(java.util.Vector<Object> vTpSysMov, java.lang.String sAccTaxPayPend, java.lang.String sAccTaxPayPendAdv, int nPkTaxType) {
        // [DEBIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPend : sAccTaxPayPendAdv);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND_ADV);
        vTpSysMov.add("");

        // [CREDIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPendAdv : sAccTaxPayPend);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND_ADV : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND);
        vTpSysMov.add("");

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtCusSrcAdv(java.util.Vector<Object> vTpSysMov, java.lang.String sAccTaxPayPend, java.lang.String sAccTaxPayPendAdv, int nPkTaxType) {
        // [DEBIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPend : sAccTaxPayPendAdv);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND_ADV);
        vTpSysMov.add("");

        // [CREDIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPendAdv : sAccTaxPayPend);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND_ADV : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND);
        vTpSysMov.add("");

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtCusDestAdv(java.util.Vector<Object> vTpSysMov, java.lang.String sAccTaxPayPend, java.lang.String sAccTaxPayPendAdv, int nPkTaxType) {
        // [DEBIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPendAdv : sAccTaxPayPend);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND_ADV : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND);
        vTpSysMov.add("");

        // [CREDIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPend : sAccTaxPayPendAdv);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND_ADV);
        vTpSysMov.add("");

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtPayApp(java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType) {
        Vector<Object> vTpSysMov = new Vector<>(); // 0. DB_ACC, 1. DBT_TP, 2. DBT_REF, 3. CDT_ACC, 4. CDT_TP, 5. CDT_REF

        switch (mnFkSubsystemCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                vTpSysMov = renderTaxAccDbtCdtSupSrc(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                vTpSysMov = renderTaxAccDbtCdtCusSrc(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                break;
            default:
        }

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtPayTra(java.lang.String sAccTaxPayPend, java.lang.String sAccTaxPayPendAdv, int nPkTaxType, int nTypeSource) {
        Vector<Object> vTpSysMov = new Vector<>(); // 0. DB_ACC, 1. DBT_TP, 2. DBT_REF, 3. CDT_ACC, 4. CDT_TP, 5. CDT_REF

        switch (mnFkSubsystemCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                // Check if document is source or destiny:

                if (nTypeSource == SDataConstants.TRNX_DSM_ETY_SOURCE) {
                    vTpSysMov = renderTaxAccDbtCdtSupSrcAdv(vTpSysMov, sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
                }
                else {
                    vTpSysMov = renderTaxAccDbtCdtSupDestAdv(vTpSysMov, sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
                }
                break;
                
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                // Check if document is source or destiny:

                if (nTypeSource == SDataConstants.TRNX_DSM_ETY_SOURCE) {
                    vTpSysMov = renderTaxAccDbtCdtCusSrcAdv(vTpSysMov, sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
                }
                else {
                    vTpSysMov = renderTaxAccDbtCdtCusDestAdv(vTpSysMov, sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
                }
                break;
                
            default:
        }

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtPayClo(java.lang.String sAccTaxPayPend, java.lang.String sAccTaxPayPendAdv, int nPkTaxType) {
        Vector<Object> vTpSysMov = new Vector<>(); // 0. DB_ACC, 1. DBT_TP, 2. DBT_REF, 3. CDT_ACC, 4. CDT_TP, 5. CDT_REF

        switch (mnFkSubsystemCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                vTpSysMov = renderTaxAccDbtCdtSupSrcAdv(vTpSysMov, sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                vTpSysMov = renderTaxAccDbtCdtCusSrcAdv(vTpSysMov, sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
                break;
            default:
        }

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtPayOpen(java.lang.String sAccTaxPayPend, java.lang.String sAccTaxPayPendAdv, int nPkTaxType) {
        Vector<Object> vTpSysMov = new Vector<>(); // 0. DB_ACC, 1. DBT_TP, 2. DBT_REF, 3. CDT_ACC, 4. CDT_TP, 5. CDT_REF

        switch (mnFkSubsystemCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                vTpSysMov = renderTaxAccDbtCdtSupDestAdv(vTpSysMov, sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                vTpSysMov = renderTaxAccDbtCdtCusDestAdv(vTpSysMov, sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
                break;
            default:
        }

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtBalApp(java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType) {
        Vector<Object> vTpSysMov = new Vector<>(); // 0. DB_ACC, 1. DBT_TP, 2. DBT_REF, 3. CDT_ACC, 4. CDT_TP, 5. CDT_REF

        switch (mnFkSubsystemCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                vTpSysMov = renderTaxAccDbtCdtSupSrc(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                vTpSysMov = renderTaxAccDbtCdtCusSrc(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                break;
            default:
        }

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtBalTra(java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType, int nTypeSource) {
        Vector<Object> vTpSysMov = new Vector<>(); // 0. DB_ACC, 1. DBT_TP, 2. DBT_REF, 3. CDT_ACC, 4. CDT_TP, 5. CDT_REF

        switch (mnFkSubsystemCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                // Check if document is source or destiny:

                if (nTypeSource == SDataConstants.TRNX_DSM_ETY_SOURCE) {
                    vTpSysMov = renderTaxAccDbtCdtSupSrc(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                }
                else {
                    vTpSysMov = renderTaxAccDbtCdtSupDest(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                }
                break;
                
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                // Check if document is source or destiny:

                if (nTypeSource == SDataConstants.TRNX_DSM_ETY_SOURCE) {
                    vTpSysMov = renderTaxAccDbtCdtCusSrc(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                }
                else {
                    vTpSysMov = renderTaxAccDbtCdtCusDest(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                }
                break;
                
            default:
        }

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtBalClo(java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType) {
        Vector<Object> vTpSysMov = new Vector<>(); // 0. DB_ACC, 1. DBT_TP, 2. DBT_REF, 3. CDT_ACC, 4. CDT_TP, 5. CDT_REF

        switch (mnFkSubsystemCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                vTpSysMov = renderTaxAccDbtCdtSupSrc(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                vTpSysMov = renderTaxAccDbtCdtCusSrc(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                break;
            default:
        }

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtBalOpen(java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType) {
        Vector<Object> vTpSysMov = new Vector<>(); // 0. DB_ACC, 1. DBT_TP, 2. DBT_REF, 3. CDT_ACC, 4. CDT_TP, 5. CDT_REF

        switch (mnFkSubsystemCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                vTpSysMov = renderTaxAccDbtCdtSupDest(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                vTpSysMov = renderTaxAccDbtCdtCusDest(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                break;
            default:
        }

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdt(java.util.Vector<Object> vTpSysMov, java.lang.Object oKey, 
            java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, java.lang.String sAccTaxPayPendAdv, int nPkTaxType, int nTypeSource) {
        if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP)) {
            vTpSysMov = renderTaxAccDbtCdtPayApp(sAccTaxPay, sAccTaxPayPend, nPkTaxType);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_TRA)) {
            vTpSysMov = renderTaxAccDbtCdtPayTra(sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType, nTypeSource);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_CLO)) {
            vTpSysMov = renderTaxAccDbtCdtPayClo(sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_OPE)) {
            vTpSysMov = renderTaxAccDbtCdtPayOpen(sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_APP)) {
            vTpSysMov = renderTaxAccDbtCdtBalApp(sAccTaxPay, sAccTaxPayPend, nPkTaxType);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_TRA)) {
            vTpSysMov = renderTaxAccDbtCdtBalTra(sAccTaxPay, sAccTaxPayPend, nPkTaxType, nTypeSource);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_CLO)) {
            vTpSysMov = renderTaxAccDbtCdtBalClo(sAccTaxPay, sAccTaxPayPend, nPkTaxType);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_OPE)) {
            vTpSysMov = renderTaxAccDbtCdtBalOpen(sAccTaxPay, sAccTaxPayPend, nPkTaxType);
        }

        return vTpSysMov;
    }

    private double[] getDpsBalance(java.sql.Connection connection, int dpsYearId, int dpsDocId, int[] sysMoveTypeKey) {
        double balance = 0;
        double balanceCur = 0;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            int nParam = 1;
            CallableStatement callableStatement = connection.prepareCall("{ CALL trn_dps_bal_get(?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, dpsYearId);
            callableStatement.setInt(nParam++, dpsDocId);
            callableStatement.setInt(nParam++, sysMoveTypeKey[0]);
            callableStatement.setInt(nParam++, sysMoveTypeKey[1]);
            callableStatement.registerOutParameter(nParam, java.sql.Types.DOUBLE); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DOUBLE); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.INTEGER); nParam++;
            callableStatement.execute();

            balance = callableStatement.getDouble(nParam - 3);
            balanceCur = callableStatement.getDouble(nParam - 2);
            
            mnLastDbActionResult = SLibConstants.DB_PROCEDURE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_PROCEDURE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return new double[] { balance, balanceCur };
    }

    private double[] getTaxBalance(java.sql.Connection connection, int dpsCategoryId, int dpsYearId, int dpsDocId, int[] sysMoveTypeKey, int taxType, int taxBasicId, int taxId) throws Exception {
        double balanceLoc = 0;
        double balanceCur = 0;
        
        String fieldsLoc = "";
        String fieldsCur = "";
        
        switch (dpsCategoryId) {
            case SDataConstantsSys.TRNS_CT_DPS_PUR:
                switch (taxType) {
                    case SModSysConsts.FINS_TP_TAX_CHARGED: // same case as customer & tax withheld
                        fieldsLoc = "re.debit - re.credit";
                        fieldsCur = "re.debit_cur - re.credit_cur";
                        break;
                    case SModSysConsts.FINS_TP_TAX_RETAINED: // same case as customer & tax
                        fieldsLoc = "re.credit - re.debit";
                        fieldsCur = "re.credit_cur - re.debit_cur";
                        break;
                    default:
                        // do nothing
                }
                break;
                
            case SDataConstantsSys.TRNS_CT_DPS_SAL:
                switch (taxType) {
                    case SModSysConsts.FINS_TP_TAX_CHARGED: // same case as supplier & tax withheld
                        fieldsLoc = "re.credit - re.debit";
                        fieldsCur = "re.credit_cur - re.debit_cur";
                        break;
                    case SModSysConsts.FINS_TP_TAX_RETAINED: // same case as supplier & tax
                        fieldsLoc = "re.debit - re.credit";
                        fieldsCur = "re.debit_cur - re.credit_cur";
                        break;
                    default:
                        // do nothing
                }
                break;
                
            default:
                // do nothing
        }
        
        String sql = "SELECT SUM(" + fieldsLoc + ") AS _balance_loc, SUM(" + fieldsCur + ") AS _balance_cur "
                + "FROM fin_rec AS r "
                + "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num "
                + "WHERE NOT r.b_del AND NOT re.b_del AND "
                + "re.fid_dps_year_n = " + dpsYearId +  " AND re.fid_dps_doc_n = " + dpsDocId + " AND "
                + "re.fid_tax_bas_n = " + taxBasicId + " AND re.fid_tax_n = " + taxId + " AND "
                + "re.fid_ct_sys_mov_xxx = " + sysMoveTypeKey[0] + " AND re.fid_tp_sys_mov_xxx = " + sysMoveTypeKey[1] + ";";

        try (ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                balanceLoc = resultSet.getDouble("_balance_loc");
                balanceCur = resultSet.getDouble("_balance_cur");
            }
        }

        return new double[] { balanceLoc, balanceCur };
    }

    /**
     * Compute payment of DPS taxes.
     * @param connection
     * @param record
     * @param dsmEntry
     * @param sourceType SDataConstants.TRNX_DSM_ETY_...
     * @param sortPos
     * @param numberId
     * @param dpsYearId
     * @param dpsDocId
     * @param concept
     * @return 
     */
    private int calculateDpsTaxes(java.sql.Connection connection, erp.mfin.data.SDataRecord record, erp.mtrn.data.SDataDsmEntry dsmEntry, int sourceType, int sortPos, int numberId, int dpsYearId, int dpsDocId, java.lang.String concept) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            try (Statement statement = connection.createStatement()) {
                String sql;
                ResultSet resultSet;
                
                // Get DPS category:

                int dpsCategory = SLibConsts.UNDEFINED;
                
                sql = "SELECT fid_ct_dps " +
                        "FROM trn_dps " +
                        "WHERE id_year = " + dpsYearId + " AND id_doc = " + dpsDocId + ";";
                
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    dpsCategory = resultSet.getInt(1);
                }
                resultSet.close();
                
                // Get taxes of DPS document:
                
                sql = "SELECT DISTINCT det.id_tax_bas, det.id_tax, det.fid_tp_tax, det.fid_tp_tax_cal, det.fid_tp_tax_app " +
                        "FROM trn_dps_ety AS de " +
                        "INNER JOIN trn_dps_ety_tax AS det ON det.id_year = de.id_year AND det.id_doc = de.id_doc AND det.id_ety = de.id_ety " +
                        "WHERE det.id_year = " + dpsYearId + " AND det.id_doc = " + dpsDocId + " AND NOT de.b_del " +
                        "ORDER BY det.id_tax_bas, det.id_tax;";
                
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    // Check if tax needs to be processed:
                    
                    int taxTypeApp = resultSet.getInt("det.fid_tp_tax_app");
                    int taxTypeCal = resultSet.getInt("det.fid_tp_tax_cal");
                    
                    if (taxTypeApp == SModSysConsts.FINS_TP_TAX_APP_CASH && (taxTypeCal == SModSysConsts.FINS_TP_TAX_CAL_RATE || taxTypeCal == SModSysConsts.FINS_TP_TAX_CAL_EXEMPT)) {
                        // Find accounts for debits and credits:
                        
                        int taxType = resultSet.getInt("det.fid_tp_tax");
                        int taxBasicId = resultSet.getInt("det.id_tax_bas");
                        int taxId = resultSet.getInt("det.id_tax");
                        int[] sysMoveTypeKeyDps = new int[] { dsmEntry.getDbmsCtSysMovId(), dsmEntry.getDbmsTpSysMovId() };
                        int[] sysMoveTypeKeyTax = null;
                        Vector<Object> sysMoveTypeKeys = new Vector<>();

                        try (Statement statementAux = connection.createStatement()) {
                            sql = "SELECT a.fid_acc_pay, a.fid_acc_pay_pend " +
                                    "FROM fin_acc_tax AS a " +
                                    "INNER JOIN erp.finu_tax AS t ON a.id_tax_bas = t.id_tax_bas AND a.id_tax = t.id_tax " +
                                    "WHERE NOT a.b_del AND " +
                                    "a.id_tax_bas = " + taxBasicId + " AND " +
                                    "a.id_tax = " + taxId + " AND " +
                                    "a.id_ct_dps = " + dsmEntry.getDbmsFkDpsCategoryId() + " AND " +
                                    "a.id_dt_start <= '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "' " +
                                    "ORDER BY a.id_dt_start DESC;";

                            ResultSet resultSetAux = statementAux.executeQuery(sql);
                            if (resultSetAux.next()) {
                                String accountPayId = resultSetAux.getString("a.fid_acc_pay");
                                String accountPayPendId = resultSetAux.getString("a.fid_acc_pay_pend");

                                // Get accountId for debit and credit, and systemMoveId (ct and tp):

                                sysMoveTypeKeys = renderTaxAccDbtCdt(sysMoveTypeKeys,
                                        new int[] { dsmEntry.getFkAccountingMoveTypeId(), dsmEntry.getFkAccountingMoveClassId(), dsmEntry.getFkAccountingMoveSubclassId() },
                                        accountPayId, accountPayPendId, "", taxType, sourceType);

                                // Obtain tp_sys_mov_id (purchase, sales):

                                if (dsmEntry.getDbmsTpSysMovId() == SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1]) {
                                    // is supplier:
                                    if (taxType == SModSysConsts.FINS_TP_TAX_CHARGED) {
                                        sysMoveTypeKeyTax = (int[]) (Object) sysMoveTypeKeys.get(4);
                                    }
                                    else if (taxType == SModSysConsts.FINS_TP_TAX_RETAINED) {
                                        sysMoveTypeKeyTax = (int[]) (Object) sysMoveTypeKeys.get(1);
                                    }
                                }
                                else {
                                    // is customer:
                                    if (taxType == SModSysConsts.FINS_TP_TAX_CHARGED) {
                                        sysMoveTypeKeyTax = (int[]) (Object) sysMoveTypeKeys.get(1);
                                    }
                                    else if (taxType == SModSysConsts.FINS_TP_TAX_RETAINED) {
                                        sysMoveTypeKeyTax = (int[]) (Object) sysMoveTypeKeys.get(4);
                                    }
                                }
                                
                                if (sysMoveTypeKeyTax != null) {
                                    // Get current balance of DPS:

                                    double valueTaxLoc = 0;
                                    double valueTaxCur = 0;
                                    double[] dpsBalance = getDpsBalance(connection, dpsYearId, dpsDocId, sysMoveTypeKeyDps);
                                    double[] taxBalance = getTaxBalance(connection, dpsCategory, dpsYearId, dpsDocId, sysMoveTypeKeyTax, taxType, taxBasicId, taxId);

                                    if (dpsBalance[1] == 0 || dpsBalance[1] == dsmEntry.getSourceValueCy()) {
                                        // account all tax balance:

                                        valueTaxCur = SLibUtils.roundAmount(taxBalance[1]);

                                        if (mnDbmsTaxModel == SDataConstantsSys.CFGS_TAX_MODEL_XRT_DOP) {
                                            valueTaxLoc = SLibUtils.roundAmount(valueTaxCur * (sourceType == SDataConstants.TRNX_DSM_ETY_SOURCE ? dsmEntry.getSourceExchangeRate() : dsmEntry.getDestinyExchangeRate()));
                                        }
                                        else {
                                            valueTaxLoc = SLibUtils.roundAmount(taxBalance[0]);
                                        }
                                    }
                                    else if (dpsBalance[1] > 0) {
                                        // account proportional tax balance:

                                        switch (sourceType) {
                                            case SDataConstants.TRNX_DSM_ETY_SOURCE:
                                                valueTaxCur = SLibUtils.roundAmount(((taxBalance[1] / dpsBalance[1]) * dsmEntry.getSourceValueCy()));

                                                if (mnDbmsTaxModel == SDataConstantsSys.CFGS_TAX_MODEL_XRT_DOP) {
                                                    valueTaxLoc = SLibUtils.roundAmount(valueTaxCur * dsmEntry.getSourceExchangeRate());
                                                }
                                                else {
                                                    valueTaxLoc = SLibUtils.roundAmount(((taxBalance[0] / dpsBalance[0]) * dsmEntry.getSourceValue()));
                                                }
                                                break;

                                            case SDataConstants.TRNX_DSM_ETY_DESTINY:
                                                valueTaxCur = SLibUtils.roundAmount(((taxBalance[1] / dpsBalance[1]) * dsmEntry.getDestinyValueCy()));

                                                if (mnDbmsTaxModel == SDataConstantsSys.CFGS_TAX_MODEL_XRT_DOP) {
                                                    valueTaxLoc = SLibUtils.roundAmount(valueTaxCur * dsmEntry.getDestinyExchangeRate());
                                                }
                                                else {
                                                    valueTaxLoc = SLibUtils.roundAmount(((taxBalance[0] / dpsBalance[0]) * dsmEntry.getDestinyValue()));
                                                }
                                                break;

                                            default:
                                                // do nothing
                                        }

                                        // validate tax to be accounted:
                                        
                                        if (mnDbmsTaxModel == SDataConstantsSys.CFGS_TAX_MODEL_XRT_ORIG) {
                                            if (valueTaxCur > taxBalance[1]) {
                                                valueTaxCur = SLibUtils.roundAmount(taxBalance[1]);
                                            }

                                            if (valueTaxLoc > taxBalance[0]) {
                                                valueTaxLoc = SLibUtils.roundAmount(taxBalance[0]);
                                            }
                                        }
                                        else {
                                            // do nothing
                                        }
                                    }

                                    sortPos = saveRecordEntriesTaxes(record, dsmEntry,
                                            numberId, concept, dpsYearId, dpsDocId, dpsCategory, sortPos,
                                            sysMoveTypeKeys, taxBasicId, taxId, valueTaxLoc, valueTaxCur, sourceType);
                                }
                            }
                        }
                    }
                }
            }

            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return sortPos;
    }

    private boolean deleteRecord(java.lang.Object recordKey, boolean deleteEverything, java.sql.Connection connection) throws java.lang.Exception {
        SDataRecord record = new SDataRecord();
        Statement statement = connection.createStatement();

        if (record.read(recordKey, statement) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ);
        }
        else {
            if (deleteEverything) {
                record.setIsRegistryEdited(true);
                record.setFkUserEditId(mnFkUserEditId);
                record.setIsDeleted(true);
                record.setFkUserDeleteId(mnFkUserEditId);
                for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
                    if (!entry.getIsDeleted()) {
                        entry.setIsRegistryEdited(true);
                        entry.setFkUserEditId(mnFkUserEditId);
                        entry.setIsDeleted(true);
                        entry.setFkUserDeleteId(mnFkUserEditId);
                    }
                }
            }
            else {
                for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
                    if (!entry.getIsDeleted()) {

                        //System.out.println("entry.getFkDsmYearId_n(): " + entry.getFkDsmYearId_n() + " - " +
                        //        " entry.getFkDsmDocId_n(): " + entry.getFkDsmDocId_n());
                        //System.out.println("mnPkYearId: " + mnPkYearId + " - " +
                        //        " mnPkDocId: " + mnPkDocId);
                        /* XXX obsolete code (sflores, 2013-06-27)
                        if (entry.getFkDsmYearId_n() == mnPkYearId && entry.getFkDsmDocId_n() == mnPkDocId) {
                            entry.setIsRegistryEdited(true);
                            entry.setFkUserEditId(mnFkUserEditId);
                            entry.setIsDeleted(true);
                            entry.setFkUserDeleteId(mnFkUserEditId);
                        }
                        */
                    }
                }
            }

            if (record.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE);
            }
        }

        return true;
    }

    /*
     * Public methods
     */

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setConcept(java.lang.String s) { msConcept = s; }
    public void setIsAudited(boolean b) { mbIsAudited = b; }
    public void setIsAuthorized(boolean b) { mbIsAuthorized = b; }
    public void setIsRecordAutomatic(boolean b) { mbIsRecordAutomatic = b; }
    public void setIsSystem(boolean b) { mbIsSystem = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkSubsystemCategoryId(int n) { mnFkSubsystemCategoryId = n; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkUserAuditedId(int n) { mnFkUserAuditedId = n; }
    public void setFkUserAuthorizedId(int n) { mnFkUserAuthorizedId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserAuditedTs(java.util.Date t) { mtUserAuditedTs = t; }
    public void setUserAuthorizedTs(java.util.Date t) { mtUserAuthorizedTs = t; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public java.util.Date getDate() { return mtDate; }
    public java.lang.String getConcept() { return msConcept; }
    public boolean getIsAudited() { return mbIsAudited; }
    public boolean getIsAuthorized() { return mbIsAuthorized; }
    public boolean getIsRecordAutomatic() { return mbIsRecordAutomatic; }
    public boolean getIsSystem() { return mbIsSystem; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkSubsystemCategoryId() { return mnFkSubsystemCategoryId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkUserAuditedId() { return mnFkUserAuditedId; }
    public int getFkUserAuthorizedId() { return mnFkUserAuthorizedId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserAuditedTs() { return mtUserAuditedTs; }
    public java.util.Date getUserAuthorizedTs() { return mtUserAuthorizedTs; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsIsRecordSaved(boolean b) { mbDbmsIsRecordSaved = b; }
    public void setDbmsPkPeriodId(int n) { mnDbmsPkPeriodId = n; }
    public void setDbmsPkBookkeepingCenterId(int n) { mnDbmsPkBookkeepingCenterId = n; }
    public void setDbmsFkCompanyBranch(int n) { mnDbmsFkCompanyBranch = n;  }
    public void setDbmsErpDecimalsValue(int n) { mnDbmsErpDecimalsValue = n; }
    public void setDbmsErpTaxModel(int n) { mnDbmsTaxModel = n; }
    public void setDbmsCompanyBranchCode(java.lang.String s) { msDbmsCompanyBranchCode = s; }
    public void setDbmsPkRecordTypeId(java.lang.String s) { msDbmsPkRecordTypeId = s; }
    public void setDbmsSubsystemTypeBiz(java.lang.String s) { msDbmsSubsystemTypeBiz = s; }

    public void setAuxIsRecordAutomatic(boolean b) { mbAuxIsRecordAutomatic = b; }
    public void setDbmsRecordDate(java.util.Date t) { mtDbmsRecordDate = t; }
    public void setDbmsRecordKey(java.lang.Object o) { moDbmsRecordKey = o; }
    public void setAuxRecordUserKey(java.lang.Object o) { moAuxRecordUserKey = o; }

    public boolean getAuxIsRecordAutomatic() { return mbAuxIsRecordAutomatic; }
    public java.util.Date getDbmsRecordDate() { return mtDbmsRecordDate; }
    public java.lang.Object getAuxRecordKeyUser() { return moAuxRecordUserKey; }

    public java.lang.Object getDbmsRecordKey() { return moDbmsRecordKey; }
    public java.util.Vector<SDataDsmNotes> getDbmsNotes() { return mvDbmsDsmNotes; }
    public java.util.Vector<SDataDsmEntry> getDbmsEntries() { return mvDbmsDsmEntries; }

    public erp.mfin.data.SDataRecord getDbmsRecord() { return moDbmsRecord; }

    public void setParamPkCheckWalletId(int n) { mnParamPkCheckWalletId = n; }
    public void setParamPkCheckId(int n) { mnParamPkCheckId = n; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mtDate = null;
        msConcept = "";
        mbIsAudited = false;
        mbIsAuthorized = false;
        mbIsRecordAutomatic = false;
        mbIsSystem = false;
        mbIsDeleted = false;
        mnFkSubsystemCategoryId = 0;
        mnFkCompanyBranchId = 0;
        mnFkUserAuditedId = 0;
        mnFkUserAuthorizedId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserAuditedTs = null;
        mtUserAuthorizedTs = null;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mbDbmsIsRecordSaved = true;
        mnDbmsPkPeriodId = 0;
        mnDbmsPkBookkeepingCenterId = 0;
        mnDbmsFkCompanyBranch = 0;
        mnDbmsErpDecimalsValue = 0;
        mnDbmsTaxModel = 0;
        msDbmsCompanyBranchCode = "";
        msDbmsPkRecordTypeId = "";
        msDbmsSubsystemTypeBiz = "";

        mvDbmsDsmNotes.clear();
        mvDbmsDsmEntries.clear();

        mbAuxIsRecordAutomatic = false;
        moAuxRecordUserKey = null;
        moDbmsRecordKey = null;
        mtDbmsRecordDate = null;

        moDbmsRecord = null;
        mnParamPkCheckWalletId = -1;
        mnParamPkCheckId = -1;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;
        java.sql.Statement statementAux = null;
        SDataDsmNotes dsmNotes = null;
        SDataDsmEntry dsmEntry = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT ds.* " +
                    "FROM trn_dsm AS ds " +
                    "WHERE ds.id_year = " + key[0] +
                        " AND ds.id_doc = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                mtDate = resultSet.getDate("dt");
                msConcept = resultSet.getString("concept");
                mbIsAudited = resultSet.getBoolean("b_audit");
                mbIsAuthorized = resultSet.getBoolean("b_authorn");
                mbIsRecordAutomatic = resultSet.getBoolean("b_rec_aut");
                mbIsSystem = resultSet.getBoolean("b_sys");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkSubsystemCategoryId = resultSet.getInt("fid_ct_dsm");
                mnFkCompanyBranchId = resultSet.getInt("fid_cob");
                mnFkUserAuditedId = resultSet.getInt("fid_usr_audit");
                mnFkUserAuthorizedId = resultSet.getInt("fid_usr_authorn");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserAuditedTs = resultSet.getTimestamp("ts_audit");
                mtUserAuthorizedTs = resultSet.getTimestamp("ts_authorn");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                // Read record:

                statementAux = statement.getConnection().createStatement();
                sql = "SELECT DISTINCT et.fid_dsm_year_n, et.fid_dsm_doc_n, et.id_year, et.id_per, et.id_bkc, et.id_tp_rec, et.id_num " +
                        "FROM fin_rec_ety AS et " +
                        "WHERE et.fid_dsm_year_n = " + key[0] + " AND " +
                            "et.fid_dsm_doc_n = " + key[1] + " AND " +
                            "et.fid_dps_year_n IS NULL AND " +
                            "et.fid_dps_doc_n IS NULL AND " +
                            "et.fid_dps_adj_year_n IS NULL AND " +
                            "et.fid_dps_adj_doc_n IS NULL AND " +
                            "et.fid_diog_year_n IS NULL AND " +
                            "et.fid_diog_doc_n IS NULL AND " +
                            "et.fid_item_n IS NULL AND " +
                            "et.b_del = FALSE";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    moDbmsRecordKey = new Object[] { resultSet.getInt("id_year"), resultSet.getInt("id_per"), resultSet.getInt("id_bkc"), resultSet.getString("id_tp_rec"), resultSet.getInt("id_num") } ;

                    sql = "SELECT dt FROM fin_rec WHERE id_year = " + ((Object[]) moDbmsRecordKey)[0] + " AND id_per = " + ((Object[]) moDbmsRecordKey)[1] + " AND " +
                            "id_bkc = " + ((Object[]) moDbmsRecordKey)[2] + " AND id_tp_rec = '" + ((Object[]) moDbmsRecordKey)[3] + "' AND id_num = " + ((Object[]) moDbmsRecordKey)[4] + " ";
                    resultSet = statement.executeQuery(sql);
                    if (!resultSet.next()) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mtDbmsRecordDate = resultSet.getDate("dt");
                        mbAuxIsRecordAutomatic = mbIsRecordAutomatic;
                        moAuxRecordUserKey = new Object[5];
                        ((Object[]) moAuxRecordUserKey)[0] = ((Object[]) moDbmsRecordKey)[0];
                        ((Object[]) moAuxRecordUserKey)[1] = ((Object[]) moDbmsRecordKey)[1];
                        ((Object[]) moAuxRecordUserKey)[2] = ((Object[]) moDbmsRecordKey)[2];
                        ((Object[]) moAuxRecordUserKey)[3] = ((Object[]) moDbmsRecordKey)[3];
                        ((Object[]) moAuxRecordUserKey)[4] = ((Object[]) moDbmsRecordKey)[4];
                    }
                }

                // Read notes:

                statementAux = statement.getConnection().createStatement();
                mvDbmsDsmNotes.removeAllElements();
                sql = "SELECT * FROM trn_dsm_nts " +
                    "WHERE id_year = " + key[0] +
                    " AND id_doc = " + key[1] +
                    " ORDER BY nts ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    dsmNotes = new SDataDsmNotes();
                    if (dsmNotes.read(new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_doc"), resultSet.getInt("id_nts") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsDsmNotes.add(dsmNotes);
                    }
                }

                // Read dsm entries:

                statementAux = statement.getConnection().createStatement();
                mvDbmsDsmEntries.removeAllElements();
                sql = "SELECT * FROM trn_dsm_ety " +
                    "WHERE id_year = " + key[0] +
                    " AND id_doc = " + key[1] +
                    " ORDER BY id_doc ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    dsmEntry = new SDataDsmEntry();
                    if (dsmEntry.read(new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_doc"), resultSet.getInt("id_ety") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsDsmEntries.add(dsmEntry);
                    }
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        boolean isNewRecord = false;
        int param = 1;
        String sql = "";

        SDataDsmNotes dataDsmNotes = null;
        SDataDsmEntry dataDsmEntry = null;
        java.sql.Statement statementAux = null;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        try {
            callableStatement = connection.prepareCall(
                    "{ CALL trn_dsm_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?) }");
            callableStatement.setInt(param++, mnPkYearId);
            callableStatement.setInt(param++, mnPkDocId);
            callableStatement.setDate(param++, new java.sql.Date(mtDate.getTime()));
            callableStatement.setString(param++, msConcept.length() > 100 ? msConcept.substring(0, 96).trim() + "..." : msConcept);
            callableStatement.setBoolean(param++, mbIsAudited);
            callableStatement.setBoolean(param++, mbIsAuthorized);
            callableStatement.setBoolean(param++, mbIsRecordAutomatic);
            callableStatement.setBoolean(param++, mbIsSystem);
            callableStatement.setBoolean(param++, mbIsDeleted);
            callableStatement.setInt(param++, mnFkSubsystemCategoryId);
            callableStatement.setInt(param++, mnFkCompanyBranchId);
            callableStatement.setInt(param++, mnFkUserAuditedId);
            callableStatement.setInt(param++, mnFkUserAuthorizedId);
            callableStatement.setInt(param++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(param++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(param++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(param++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkDocId = callableStatement.getInt(param - 3);
            mnDbmsErrorId = callableStatement.getInt(param - 2);
            msDbmsError = callableStatement.getString(param - 1);

            if (mnDbmsErrorId != 0) {
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            }
            else {
                // Save notes:

                statementAux = connection.createStatement();
                sql = "DELETE FROM trn_dsm_nts WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
                statementAux.execute(sql);
                statementAux.close();

                for (int i = 0; i < mvDbmsDsmNotes.size(); i++) {
                    dataDsmNotes = (SDataDsmNotes) mvDbmsDsmNotes.get(i);
                    if (dataDsmNotes != null) {
                        dataDsmNotes.setPkYearId(mnPkYearId);
                        dataDsmNotes.setPkDocId(mnPkDocId);
                        dataDsmNotes.setPkNotesId(0);
                        dataDsmNotes.setIsDeleted(mbIsDeleted == true ? mbIsDeleted : dataDsmNotes.getIsDeleted());
                        if (dataDsmNotes.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                // Save DSM Entry:

                for (int m = 0; m < mvDbmsDsmEntries.size(); m++) {
                    dataDsmEntry = (SDataDsmEntry) mvDbmsDsmEntries.get(m);

                    if (dataDsmEntry != null) {
                        dataDsmEntry.setPkYearId(mnPkYearId);
                        dataDsmEntry.setPkDocId(mnPkDocId);
                        dataDsmEntry.setIsDeleted(mbIsDeleted == true ? mbIsDeleted : dataDsmEntry.getIsDeleted());
                        if (dataDsmEntry.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
                        }
                    }
                }

                // Save record if there is DSM Entry:

                if (mvDbmsDsmEntries.size() > 0) {
                    // 4. Save aswell accounting record if document class requires one:

                    // 4.1 Prepare accounting record:2

                    if (mbIsDeleted) {

                        // Delete aswell former accounting record:

                        //System.out.println("Save 1");
                        if (moAuxRecordUserKey != null) {
                            // If document had automatic accounting record, delete it including its header:
                            //System.out.println("Save 2");
                            deleteRecord(moAuxRecordUserKey, mbAuxIsRecordAutomatic, connection);
                        }
                    }
                    else {
                        // Save aswell accounting record:

                        //System.out.println("Save 3");
                        if (moDbmsRecordKey == null) {
                            if (!mbIsRecordAutomatic) {
                                // Accounting record is not automatic:
                                throw new Exception("No se ha especificado la póliza contable de usuario.");
                            }
                            else {
                                // Accounting record is automatic:

                                //System.out.println("Save 4");
                                if (moAuxRecordUserKey == null || !mbAuxIsRecordAutomatic) {
                                    //System.out.println("Save 5");
                                    isNewRecord = true;
                                    // moDbmsRecordKey = (Object[]) createAccountingRecordKey(statementAux);
                                }
                                else {
                                    //System.out.println("Save 6");
                                    if (SLibTimeUtilities.isBelongingToPeriod(mtDate, (Integer) ((Object[]) moAuxRecordUserKey)[0], (Integer) ((Object[]) moAuxRecordUserKey)[1])) {
                                        moDbmsRecordKey = moAuxRecordUserKey;
                                        //System.out.println("Save 7");
                                    }
                                    else {
                                        isNewRecord = true;
                                        //System.out.println("Save 8");
                                        // moDbmsRecordKey = (Object[]) createAccountingRecordKey(statementAux);
                                    }
                                }
                            }
                        }

                        if (moAuxRecordUserKey != null) {
                            // If document was previously saved, delete former accounting moves, if needed, including its header:
                            //System.out.println("Save 9");
                            deleteRecord(moAuxRecordUserKey, mbAuxIsRecordAutomatic && !mbIsRecordAutomatic, connection);
                        }

                        // 4.2 Save document's accounting record:

                        // Save record:

                        saveRecord(connection, isNewRecord);
                    }
                }
                else {
                    mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int canDelete(java.sql.Connection connection) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int delete(java.sql.Connection connection) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int process(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            moDbmsRecord = saveRecord(connection, true);
            mnLastDbActionResult = SLibConstants.DB_ACTION_PROCESS_OK;
        }
        catch (Exception exception) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_PROCESS_ERROR;
            SLibUtilities.printOutException(this, exception);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return mtUserEditTs;
    }

    public erp.mfin.data.SDataRecord publicSaveRecord(java.sql.Connection conn) {
        return saveRecord(conn, true);
    }
}

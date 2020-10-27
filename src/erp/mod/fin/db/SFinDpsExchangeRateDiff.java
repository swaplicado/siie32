/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mcfg.data.SDataParamsCompany;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.data.SFinUtilities;
import erp.mitm.data.SDataItem;
import erp.mod.SModSysConsts;
import erp.mod.trn.db.STrnUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Gerardo Hernández, Uriel Castañeda, Isabel Servín
 */
public class SFinDpsExchangeRateDiff {

    protected SGuiClient miClient;
    protected int mnLocalCurrency;
    protected int mnRecYear;
    protected int mnRecPeriod;
    protected double mdExchangeRate;
    protected Date mtEndOfMonth;
    private String msConcept;
    protected SDataRecord moRecord;
    protected ArrayList<SDataRecordEntry> maRecordEntries;
    protected SDataParamsCompany moParamsCompany;
    protected SDataItem moItemIncome;
    protected SDataItem moItemExpenses;
    
    public SFinDpsExchangeRateDiff(SGuiClient client) {
        miClient = client;
        mnLocalCurrency = miClient.getSession().getSessionCustom().getLocalCurrencyKey()[0];
        mnRecYear = 0;
        mnRecPeriod = 0;
        mdExchangeRate = 0d;
        mtEndOfMonth = null;
        msConcept = "";
        moRecord = null;
        maRecordEntries = new ArrayList<>();
        moParamsCompany = ((SClientInterface) miClient).getSessionXXX().getParamsCompany();
        moItemIncome = (SDataItem) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.ITMU_ITEM, new int[] { moParamsCompany.getFkItemDifferenceIncomeId_n() }, SLibConstants.EXEC_MODE_SILENT);
        if (moParamsCompany.getFkItemDifferenceExpenseId_n() == moParamsCompany.getFkItemDifferenceIncomeId_n()) {
            moItemExpenses = moItemIncome;
        }
        else {
            moItemExpenses = (SDataItem) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.ITMU_ITEM, new int[] { moParamsCompany.getFkItemDifferenceExpenseId_n()}, SLibConstants.EXEC_MODE_SILENT);
        }
    }

    public void setClient(SGuiClient n) {
        miClient = n;
    }
    
    public void setRecYear(int n) {
        mnRecYear = n;
    }

    public void setRecPeriod(int n) {
        mnRecPeriod = n;
    }

    public void setExchangeRate(double d) {
        mdExchangeRate = d;
    }

    public void setEndOfMonth(Date t) {
        mtEndOfMonth = t;
    }

    public String getMsConcept() {
        return msConcept;
    }
    
    public void setRecord(SDataRecord o) {
        moRecord = o;
    }

    public SGuiClient getClient() {
        return miClient;
    }        
            
    public int getRecYear() {
        return mnRecYear;
    }

    public int getRecPeriod() {
        return mnRecPeriod;
    }

    public double getExchangeRate() {
        return mdExchangeRate;
    }

    public Date getEndOfMonth() {
        return mtEndOfMonth;
    }

    public void setMsConcept(String msConcept) {
        this.msConcept = msConcept;
    }
    
    public SDataRecord getRecord() {
        return moRecord;
    }

    public ArrayList<SDataRecordEntry> getRecordEntries() {
        return maRecordEntries;
    }

    /**
     * Creates concept for accounting.
     *
     * @param docNumber Series and number of document for sales and purchases.
     * @param bizPartnerName Business partner or bank name.
     * @return String wiht the concept
     */
    private String createConceptRecordEntry(final String docNumber, final String reference, final String bizPartnerName) {
        String concept;

        if (!docNumber.isEmpty()) {
            concept = SLibUtils.textTrim("DIFERENCIA CAMBIARIA/ F " + docNumber + "/ " + bizPartnerName);
        }
        else {
            if (!reference.isEmpty()) {
                concept = SLibUtils.textTrim("DIFERENCIA CAMBIARIA/ REF " + reference + "/ " + bizPartnerName);
            }
            else {
                concept = SLibUtils.textTrim("DIFERENCIA CAMBIARIA/ SIN REF/ " + bizPartnerName);
            }
        }

        return concept;
    }

    /**
     * Composes SQL query for DPS balances.
     * @param sysMoveTypeXxxKey Key of system movement typeSDataConstantsSys.FINS_CT_SYS_MOV_...
     * @return SQL query.
     */
    private String composeQueryBizPartnersBalance(final int[] sysMoveTypeXxxKey) {
        String sql = "SELECT re.fid_dps_year_n, re.fid_dps_doc_n, re.ref, re.fid_acc, re.fk_acc, "
                + "re.fid_bp_nr, re.fid_bpb_n, d.num_ser, d.num, b.bp_comm, bb.bpb, re.fid_cur, "
                + "SUM(re.debit) AS _dbt, SUM(re.credit) AS _cdt "
                + "FROM fin_rec AS r "
                + "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "INNER JOIN erp.bpsu_bp AS b ON b.id_bp = re.fid_bp_nr "
                + "LEFT OUTER JOIN erp.bpsu_bpb AS bb ON bb.id_bpb = re.fid_bpb_n "
                + "LEFT OUTER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc "
                + "WHERE NOT r.b_del AND NOT re.b_del AND "
                + "r.id_year = " + mnRecYear + " AND r.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtEndOfMonth) + "' AND "
                + "re.fid_ct_sys_mov_xxx = " + sysMoveTypeXxxKey[0] + " AND re.fid_tp_sys_mov_xxx = " + sysMoveTypeXxxKey[1] + " AND "
                + "re.fid_cur <> " + mnLocalCurrency + " "
                + "GROUP BY re.fid_dps_year_n, re.fid_dps_doc_n, re.ref, re.fid_acc, re.fk_acc, "
                + "re.fid_bp_nr, re.fid_bpb_n, d.num_ser, d.num, b.bp_comm, bb.bpb, re.fid_cur "
                + "HAVING SUM(re.debit_cur - re.credit_cur) = 0 AND SUM(re.debit - re.credit) <> 0 " // documentos liquidados con diferencia en cambios
                + "ORDER BY b.bp_comm, bb.bpb, re.fid_bp_nr, re.fid_bpb_n, "
                + "d.num_ser, d.num, re.fid_dps_year_n, re.fid_dps_doc_n, re.ref, "
                + "re.fid_acc, re.fk_acc;";
        
        return sql;
    }
    
    private int[] getSysMoveTypeXxxForTaxes(final int bizPartnerCategory, final boolean isTaxCharged) {
        int[] sysMovTypeXxx = null;
        
        switch (bizPartnerCategory) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                if (isTaxCharged) {
                    sysMovTypeXxx = SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND;
                }
                else {
                    sysMovTypeXxx = SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND;
                }
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                if (isTaxCharged) {
                    sysMovTypeXxx = SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND;
                }
                else {
                    sysMovTypeXxx = SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND;
                }
                break;
            default:
        }
        
        return sysMovTypeXxx;
    }

    /**
     * Composes SQL query for DPS Taxes balances.
     * @param bizPartnerCategory Business partner category, constants defined in SDataConstantsSys.BPSS_CT_BP_...
     * @param isTaxCharged Indicates if tax is charged or withheld.
     * @param dpsKey Key of DPS.
     * @return SQL query.
     */
    private String composeQueryTaxesBalance(final int bizPartnerCategory, final boolean isTaxCharged, final int[] dpsKey) {
        int[] sysMoveTypeXxx = getSysMoveTypeXxxForTaxes(bizPartnerCategory, isTaxCharged);

        String sql = "SELECT re.fid_acc, re.fk_acc, re.fid_tax_bas_n, re.fid_tax_n, "
            + "t.tax, "
            + "SUM(re.debit) AS _dbt, SUM(re.credit) AS _cdt "
            + "FROM fin_rec AS r "
            + "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
            + "INNER JOIN erp.finu_tax AS t ON re.fid_tax_bas_n = t.id_tax_bas AND re.fid_tax_n = t.id_tax "
            + "WHERE NOT r.b_del AND NOT re.b_del AND "
            + "re.fid_ct_sys_mov_xxx = " + sysMoveTypeXxx[0] + " AND re.fid_tp_sys_mov_xxx = " + sysMoveTypeXxx[1] + " AND " 
            + "re.fid_dps_year_n = " + dpsKey[0] + " and re.fid_dps_doc_n = " + dpsKey[1] + " "
            + "GROUP BY re.fid_acc, re.fk_acc, re.fid_tax_bas_n, re.fid_tax_n, "
            + "t.tax "
            + "HAVING SUM(re.debit_cur - re.credit_cur) = 0 AND SUM(re.debit - re.credit) <> 0 "
            + "ORDER BY re.fid_acc, re.fk_acc, re.fid_tax_bas_n, re.fid_tax_n;";
        
        return sql;
    }
    
    /**
     * Creates accointug record
     * @param pbBizPartnerCategory Business partner category.
     * @param pbIsIncomeExpensesEntry Indicates if is exchange difference entry.
     * @param psConcept Concept of the entry.
     * @param pdDebit If it´s debit type.
     * @param pdCredit If it´s credit type.
     * @param psAccountId Account Id.
     * @param pnSortingPosition Position.
     * @param panSysMoveTypeKeyXXX System movment type key.
     * @param pnBizPartnerId Bussiner partner Id.
     * @param pnBizParnerBranchId Business parner branch Id.
     * @param panDocumentKey Document key.
     * @param pnCurrencyId Currency Id.
     * @param taxKey Tax key.
     * @return SDataRecordEntry
     */
    private SDataRecordEntry createRecordEntry(final int pbBizPartnerCategory, final boolean pbIsIncomeExpensesEntry, 
            final String psConcept, final double pdDebit, final double pdCredit, final String psAccountId, final int pnSortingPosition, 
            final int[] panSysMoveTypeKeyXXX, final int pnBizPartnerId, final int pnBizParnerBranchId, 
            final int[] panDocumentKey, final String psReference, final int pnCurrencyId, final int[] taxKey) {
        int[] anSystemMoveTypeKey = null;
        int[] anSystemAccountTypeKey = null;
        
        SDataRecordEntry entry = new SDataRecordEntry();

        /*
        entry.setPkYearId(...);
        entry.setPkPeriodId(...);
        entry.setPkBookkeepingCenterId(...);
        entry.setPkRecordTypeId(...);
        entry.setPkNumberId(...);
        entry.setPkEntryId(...);
        */
        entry.setConcept(psConcept);
        entry.setReference(psReference);
        entry.setIsReferenceTax(false);
        entry.setDebit(pdDebit);
        entry.setCredit(pdCredit);
        entry.setExchangeRate(pbIsIncomeExpensesEntry ? 1 : 0);
        entry.setExchangeRateSystem(pbIsIncomeExpensesEntry ? 1 : 0);
        entry.setDebitCy(pbIsIncomeExpensesEntry ? pdDebit : 0);
        entry.setCreditCy(pbIsIncomeExpensesEntry ? pdCredit : 0);
        entry.setUnits(0);
        entry.setUserId(0);
        entry.setSortingPosition(pnSortingPosition);
        entry.setIsExchangeDifference(!pbIsIncomeExpensesEntry);
        entry.setIsSystem(false);
        entry.setIsDeleted(false);
        
        if (!pbIsIncomeExpensesEntry) {
            entry.setFkAccountIdXXX(psAccountId);
            //entry.setFkAccountId(...); // set on save by store procedure
            entry.setFkCostCenterIdXXX_n("");
            //entry.setFkCostCenterId_n(...); // set on save by store procedure
            entry.setFkItemId_n(0);
            entry.setFkItemAuxId_n(0);
            entry.setFkUnitId_n(0);
        }
        else {
            if (pdCredit > 0) {
                entry.setFkAccountIdXXX(moParamsCompany.getFkAccountDifferenceIncomeId_n());
                //entry.setFkAccountId(...); // set on save by store procedure
                entry.setFkCostCenterIdXXX_n(moParamsCompany.getFkCostCenterDifferenceIncomeId_n());
                //entry.setFkCostCenterId_n(...); // set on save by store procedure
                entry.setFkItemId_n(moItemIncome.getPkItemId());
                entry.setFkItemAuxId_n(0);
                entry.setFkUnitId_n(moItemIncome.getFkUnitId());
            }
            else {
                entry.setFkAccountIdXXX(moParamsCompany.getFkAccountDifferenceExpenseId_n());
                //entry.setFkAccountId(...); // set on save by store procedure
                entry.setFkCostCenterIdXXX_n(moParamsCompany.getFkCostCenterDifferenceExpenseId_n());
                //entry.setFkCostCenterId_n(...); // set on save by store procedure
                entry.setFkItemId_n(moItemExpenses.getPkItemId());
                entry.setFkItemAuxId_n(0);
                entry.setFkUnitId_n(moItemExpenses.getFkUnitId());
            }
        }
        
        // System movement and system account foreign keys:
        
        /*
         * 1) Former style (but still valid on almost all queries and reports):
         *      - AccountingMove (type, class & subclass) means: system movement (WTF!)
         *      - SystemMoveXXX (category & type) means: system account (WTF!)
         *
         * 2) Newest style (but not yet widespread to all queries and reports):
         *      - SystemMove (class & type) means: system movement
         *      - SystemAccount (class & type) means: system account
         */
        
        entry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);     // belongs to former accounting style
        entry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);    // belongs to former accounting style
        entry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]); // belongs to former accounting style
        
        int[] incrementKey;
        int[] decrementKey;
        
        switch (pbBizPartnerCategory) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                if (pbBizPartnerCategory == SDataConstantsSys.BPSS_CT_BP_SUP) {
                    incrementKey = SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_EXR;
                    decrementKey = SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_DEC_EXR;
                }
                else {
                    incrementKey = SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_EXR;
                    decrementKey = SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_DEC_EXR;
                }
                
                if (SFinUtilities.isSysMovementBizPartner(panSysMoveTypeKeyXXX)) {
                    if (!pbIsIncomeExpensesEntry) {
                        anSystemMoveTypeKey = pdCredit > 0 ? incrementKey : decrementKey;
                    }
                    else {
                        anSystemMoveTypeKey = pdDebit > 0 ? incrementKey : decrementKey;
                    }
                }
                else if (SLibUtils.compareKeys(panSysMoveTypeKeyXXX, SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND)) {
                    if (!pbIsIncomeExpensesEntry) {
                        anSystemMoveTypeKey = pdDebit > 0 ? incrementKey : decrementKey;
                    }
                    else {
                        anSystemMoveTypeKey = pdCredit > 0 ? incrementKey : decrementKey;
                    }
                }
                else if (SLibUtils.compareKeys(panSysMoveTypeKeyXXX, SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND)) {
                    if (!pbIsIncomeExpensesEntry) {
                        anSystemMoveTypeKey = pdCredit > 0 ? incrementKey : decrementKey;
                    }
                    else {
                        anSystemMoveTypeKey = pdDebit > 0 ? incrementKey : decrementKey;
                    }
                }
                break;
                
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                if (pbBizPartnerCategory == SDataConstantsSys.BPSS_CT_BP_CUS) {
                    incrementKey = SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_EXR;
                    decrementKey = SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_DEC_EXR;
                }
                else {
                    incrementKey = SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_EXR;
                    decrementKey = SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_DEC_EXR;
                }
                
                if (SFinUtilities.isSysMovementBizPartner(panSysMoveTypeKeyXXX)) {
                    if (!pbIsIncomeExpensesEntry) {
                        anSystemMoveTypeKey = pdDebit > 0 ? incrementKey : decrementKey;
                    }
                    else {
                        anSystemMoveTypeKey = pdCredit > 0 ? incrementKey : decrementKey;
                    }
                }
                else if (SLibUtils.compareKeys(panSysMoveTypeKeyXXX, SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND)) {
                    if (!pbIsIncomeExpensesEntry) {
                        anSystemMoveTypeKey = pdCredit > 0 ? incrementKey : decrementKey;
                    }
                    else {
                        anSystemMoveTypeKey = pdDebit > 0 ? incrementKey : decrementKey;
                    }
                }
                else if (SLibUtils.compareKeys(panSysMoveTypeKeyXXX, SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND)) {
                    if (!pbIsIncomeExpensesEntry) {
                        anSystemMoveTypeKey = pdDebit > 0 ? incrementKey : decrementKey;
                    }
                    else {
                        anSystemMoveTypeKey = pdCredit > 0 ? incrementKey : decrementKey;
                    }
                }
                break;
                
            default:
        }
        
        if (pbIsIncomeExpensesEntry || !SFinUtilities.isSysMovementBizPartner(panSysMoveTypeKeyXXX)) {
            anSystemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_NA_NA;
        }
        else {
            switch (pbBizPartnerCategory) {
                case SDataConstantsSys.BPSS_CT_BP_SUP:
                    anSystemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL;
                    break;
                case SDataConstantsSys.BPSS_CT_BP_CUS:
                    anSystemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL;
                    break;
                case SDataConstantsSys.BPSS_CT_BP_CDR:
                    anSystemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL;
                    break;
                case SDataConstantsSys.BPSS_CT_BP_DBR:
                    anSystemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL;
                    break;
                default:
            }
        }
        
        entry.setFkSystemMoveClassId(anSystemMoveTypeKey[0]);           // belongs to newest accounting style
        entry.setFkSystemMoveTypeId(anSystemMoveTypeKey[1]);            // belongs to newest accounting style
        
        entry.setFkSystemAccountClassId(anSystemAccountTypeKey[0]);     // belongs to newest accounting style
        entry.setFkSystemAccountTypeId(anSystemAccountTypeKey[1]);      // belongs to newest accounting style
        
        if (pbIsIncomeExpensesEntry) {
            entry.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_NA[0]);    // belongs to former accounting style
            entry.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_NA[1]);        // belongs to former accounting style
        }
        else {
            entry.setFkSystemMoveCategoryIdXXX(panSysMoveTypeKeyXXX[0]);                   // belongs to former accounting style
            entry.setFkSystemMoveTypeIdXXX(panSysMoveTypeKeyXXX[1]);                       // belongs to former accounting style
        }
        
        // Other record entry foreign keys:
        
        entry.setFkCurrencyId(pnCurrencyId);
        entry.setFkCheckWalletId_n(0);
        entry.setFkCheckId_n(0);
        
        entry.setFkBizPartnerId_nr(pnBizPartnerId);
        entry.setFkBizPartnerBranchId_n(pnBizParnerBranchId);
        
        if (pnBizPartnerId != 0 && taxKey == null) {
            entry.setFkReferenceCategoryId_n(panSysMoveTypeKeyXXX[1]);
        }
        else {
            entry.setFkReferenceCategoryId_n(0);
        }
        
        entry.setFkCompanyBranchId_n(0);
        entry.setFkEntityId_n(0);
        entry.setFkPlantCompanyBranchId_n(0);
        entry.setFkPlantEntityId_n(0);
        
        entry.setFkTaxBasicId_n(taxKey == null ? 0 : taxKey[0]);
        entry.setFkTaxId_n(taxKey == null ? 0 : taxKey[1]);
        
        entry.setFkYearId_n(0);
        entry.setFkDpsYearId_n(panDocumentKey == null ? 0 : panDocumentKey[0]);
        entry.setFkDpsDocId_n(panDocumentKey == null ? 0 : panDocumentKey[1]);
        entry.setFkDpsAdjustmentYearId_n(0);
        entry.setFkDpsAdjustmentDocId_n(0);
        entry.setFkDiogYearId_n(0);
        entry.setFkDiogDocId_n(0);
        entry.setFkMfgYearId_n(0);
        entry.setFkMfgOrdId_n(0);
        entry.setFkCostGicId_n(0);
        entry.setFkPayrollFormerId_n(0);
        entry.setFkPayrollId_n(0);
        entry.setFkBookkeepingYearId_n(0);
        entry.setFkBookkeepingNumberId_n(0);
        
        entry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        //entry.setFkUserEditId(...);
        //entry.setFkUserDeleteId(...);
        //entry.setUserNewTs(...);
        //entry.setUserEditTs(...);
        //entry.setUserDeleteTs(...);

        return entry;
    }
    
    /**
     * Determines whether the amount will be charged or credited
     * considering if is a debit movement.
     * @param debit 
     * @param credit
     * @param isDebit 
     * @return array  with the aconting register.
     */
    private double[] computeDebitCredit(final double debit, final double credit, final boolean isDebit){
        double balance;
        double[] debitCredit =  new double[] { 0, 0 };
      
        balance = isDebit ? debit - credit : credit - debit;

        if (balance > 0) {
            if (isDebit) {
                debitCredit[1] = SLibUtils.roundAmount(balance);
            }
            else {
                debitCredit[0] = SLibUtils.roundAmount(balance);
            }
        }
        else if (balance < 0) {
            if (isDebit) {
                debitCredit[0] = SLibUtils.roundAmount(-balance);
            }
            else {
                debitCredit[1] = SLibUtils.roundAmount(-balance);
            }
        }
        
        return debitCredit;
    }
    
    /**
     * Check and processes the documents that will be adjusted for exchange rate differences.
     *
     * @throws Exception
     */
    private void procesExchangeRateDifferences() throws Exception {
        Connection connection = miClient.getSession().getStatement().getConnection();
        int nSortingPosition = 0;
        int nBizParnerId;
        int nBizParnerBranchId;
        int nCurrencyId;
        int[] anSysMoveTypeXxxKeyBizPartner;
        int[] anSysMovTypeXxxKeyTax;
        int[] anDocumentKey;
        int[] anTaxKey;
        double[] adDebitCreditBal;
        boolean isDebit;
        String sReference;
        String sRecordConcept;
        String sIdAccBal;
        ResultSet resultSet;
        
        // setting sorting position of new accounting voucher entries:
        
        for (SDataRecordEntry entry : moRecord.getDbmsRecordEntries()) {
            if (!entry.getIsDeleted()) {
                nSortingPosition = entry.getSortingPosition();
            }
        }
        
        // processing business partners:
        
        int[] bizPartnerCategories = new int[] { 
            SDataConstantsSys.BPSS_CT_BP_SUP,
            SDataConstantsSys.BPSS_CT_BP_CUS,
            SDataConstantsSys.BPSS_CT_BP_CDR,
            SDataConstantsSys.BPSS_CT_BP_DBR };
        
        HashMap<Integer, int[]> sysMoveTypeXxxBizPartners = new HashMap<>(); // key = business partner category; value = key of system movement
        sysMoveTypeXxxBizPartners.put(SDataConstantsSys.BPSS_CT_BP_SUP, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
        sysMoveTypeXxxBizPartners.put(SDataConstantsSys.BPSS_CT_BP_CUS, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
        sysMoveTypeXxxBizPartners.put(SDataConstantsSys.BPSS_CT_BP_CDR, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CDR);
        sysMoveTypeXxxBizPartners.put(SDataConstantsSys.BPSS_CT_BP_DBR, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_DBR);
        
        for (int bizPartnerCategory : bizPartnerCategories) {
            anSysMoveTypeXxxKeyBizPartner = sysMoveTypeXxxBizPartners.get(bizPartnerCategory);
            
            String sql = composeQueryBizPartnersBalance(anSysMoveTypeXxxKeyBizPartner);
            resultSet = connection.createStatement().executeQuery(sql);
            
            while (resultSet.next()) {
                // ajuste al saldo del documento:
                
                anDocumentKey = resultSet.getInt("re.fid_dps_year_n") == 0 ? null : new int[] { resultSet.getInt("re.fid_dps_year_n"), resultSet.getInt("re.fid_dps_doc_n") };
                sReference = resultSet.getString("re.ref");
                sIdAccBal = resultSet.getString("re.fid_acc");
                nBizParnerId = resultSet.getInt("re.fid_bp_nr");
                nBizParnerBranchId = resultSet.getInt("re.fid_bpb_n");
                nCurrencyId = resultSet.getInt("re.fid_cur");
                sRecordConcept = createConceptRecordEntry(
                        STrnUtils.formatDocNumber(resultSet.getString("d.num_ser") == null ? "" : resultSet.getString("d.num_ser"), resultSet.getString("d.num") == null ? "" : resultSet.getString("d.num")), 
                        sReference, resultSet.getString("b.bp_comm"));
                isDebit = SLibUtils.belongsTo(bizPartnerCategory, new int[] { SDataConstantsSys.BPSS_CT_BP_CUS, SDataConstantsSys.BPSS_CT_BP_DBR });
                adDebitCreditBal = computeDebitCredit(resultSet.getDouble("_dbt"), resultSet.getDouble("_cdt"), isDebit);

                maRecordEntries.add(createRecordEntry(bizPartnerCategory, false, sRecordConcept, adDebitCreditBal[0], adDebitCreditBal[1], sIdAccBal, ++nSortingPosition, 
                        anSysMoveTypeXxxKeyBizPartner, nBizParnerId, nBizParnerBranchId, anDocumentKey, sReference, nCurrencyId, null));
                maRecordEntries.add(createRecordEntry(bizPartnerCategory, true, sRecordConcept, adDebitCreditBal[1], adDebitCreditBal[0], "", ++nSortingPosition, 
                        anSysMoveTypeXxxKeyBizPartner, 0, 0, anDocumentKey, "", mnLocalCurrency, null));

                if (anDocumentKey != null) {
                    // ajuste a impuestos trasladados:

                    String sqlTax = composeQueryTaxesBalance(bizPartnerCategory, true, anDocumentKey);
                    try (ResultSet rsTax = connection.createStatement().executeQuery(sqlTax)) {
                        while (rsTax.next()) { 
                            sIdAccBal = rsTax.getString("fid_acc");
                            anTaxKey = new int[] {rsTax.getInt("re.fid_tax_bas_n"), rsTax.getInt("re.fid_tax_n")};
                            isDebit = !SLibUtils.belongsTo(bizPartnerCategory, new int[] { SDataConstantsSys.BPSS_CT_BP_CUS, SDataConstantsSys.BPSS_CT_BP_DBR });
                            adDebitCreditBal = computeDebitCredit(rsTax.getDouble("_dbt"), rsTax.getDouble("_cdt"), isDebit);
                            anSysMovTypeXxxKeyTax = getSysMoveTypeXxxForTaxes(bizPartnerCategory, true);

                            maRecordEntries.add(createRecordEntry(bizPartnerCategory, false, sRecordConcept, adDebitCreditBal[0], adDebitCreditBal[1], sIdAccBal, ++nSortingPosition, 
                                    anSysMovTypeXxxKeyTax, nBizParnerId, nBizParnerBranchId, anDocumentKey, "", nCurrencyId, anTaxKey));
                            maRecordEntries.add(createRecordEntry(bizPartnerCategory, true, sRecordConcept, adDebitCreditBal[1], adDebitCreditBal[0], "", ++nSortingPosition, 
                                    anSysMovTypeXxxKeyTax, 0, 0, anDocumentKey, "", mnLocalCurrency, null));
                        }
                    }

                    // ajuste a impuestos retenidos:

                    sqlTax = composeQueryTaxesBalance(bizPartnerCategory, false, anDocumentKey);
                    try (ResultSet rsTax = connection.createStatement().executeQuery(sqlTax)) {
                        while (rsTax.next()) { 
                            sIdAccBal = rsTax.getString("fid_acc");
                            anTaxKey  = new int[] {rsTax.getInt("re.fid_tax_bas_n"), rsTax.getInt("re.fid_tax_n")};
                            isDebit = SLibUtils.belongsTo(bizPartnerCategory, new int[] { SDataConstantsSys.BPSS_CT_BP_CUS, SDataConstantsSys.BPSS_CT_BP_DBR });
                            adDebitCreditBal = computeDebitCredit(rsTax.getDouble("_dbt"), rsTax.getDouble("_cdt"), isDebit);
                            anSysMovTypeXxxKeyTax = getSysMoveTypeXxxForTaxes(bizPartnerCategory, false);

                            maRecordEntries.add(createRecordEntry(bizPartnerCategory, false, sRecordConcept, adDebitCreditBal[0], adDebitCreditBal[1], sIdAccBal, ++nSortingPosition, 
                                    anSysMovTypeXxxKeyTax, nBizParnerId, nBizParnerBranchId, anDocumentKey, "", nCurrencyId, anTaxKey));
                            maRecordEntries.add(createRecordEntry(bizPartnerCategory, true, sRecordConcept, adDebitCreditBal[1], adDebitCreditBal[0], "", ++nSortingPosition, 
                                    anSysMovTypeXxxKeyTax, 0, 0, anDocumentKey, "", mnLocalCurrency, null));
                        }
                    }
                }
            }
        }
    }

    /**
     * Start the process.
     *
     * @throws Exception
     */
    public void save() throws Exception {
        procesExchangeRateDifferences();

        moRecord.getDbmsRecordEntries().addAll(maRecordEntries);

        if (moRecord.save(miClient.getSession().getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
        }
    }
}

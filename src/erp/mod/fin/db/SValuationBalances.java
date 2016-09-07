/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.data.SFinUtilities;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Gerardo Hernández, Uriel Castañeda
 */
public class SValuationBalances {

    protected SGuiClient miClient;
    protected int mnRecYear;
    protected int mnRecPeriod;
    protected int mnCurrencyId;
    protected double mdExchangeRate;
    protected Date mtEndOfMonth;
    private String msConcept;
    protected SDataRecord moRecord;
    protected ArrayList<SDataRecordEntry> maRecordEntries;
    
    public SValuationBalances(SGuiClient client) {
        miClient = client;
        mnRecYear = 0;
        mnRecPeriod = 0;
        mnCurrencyId = 0;
        mdExchangeRate = 0d;
        mtEndOfMonth = null;
        msConcept = "";
        moRecord = null;
        maRecordEntries = new ArrayList<>();
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

    public void setCurrencyId(int n) {
        mnCurrencyId = n;
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

    public int getCurrencyId() {
        return mnCurrencyId;
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
    private String createConceptRecordEntry(final String docNumber, final String bizPartnerName) {
        String concept = "";

        if (docNumber.isEmpty()) {
            concept = SLibUtils.textTrim("VALUACIÓN " + mnRecYear + "-" + SLibUtils.DecimalFormatCalendarMonth.format(mnRecPeriod) + " / " + bizPartnerName);
        }
        else {
            concept = SLibUtils.textTrim("VALUACIÓN " + mnRecYear + "-" + SLibUtils.DecimalFormatCalendarMonth.format(mnRecPeriod) + " /F " + docNumber + " / " + bizPartnerName);
        }

        return concept.length() <= SDataRecordEntry.LEN_CONCEPT ? concept : SLibUtils.textLeft(concept, SDataRecordEntry.LEN_CONCEPT);
    }

    /**
     * Composes SQL query acording the type of movement (Business Partner or Cash Account).
     *
     * @param idCategorySystemMove Category of system movement.
     * @return SQL query.
     */
    private String composeQuery(final int idCategorySystemMove) {
        String sql = "";

        switch (idCategorySystemMove) {
            case SDataConstantsSys.FINS_CT_SYS_MOV_CASH:
                sql = "SELECT re.fid_cob_n AS _id_cob, re.fid_ent_n AS _id_ent, e.ent, e.code, "
                        + "re.fid_acc, re.fk_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, "
                        + "SUM(re.debit_cur - re.credit_cur) AS _bal_cur, SUM(re.debit - re.credit) AS _bal "
                        + "FROM fin_rec AS r "
                        + "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                        + "INNER JOIN erp.cfgu_cur AS c ON re.fid_cur = c.id_cur "
                        + "INNER JOIN erp.cfgu_cob_ent AS e ON re.fid_cob_n = e.id_cob AND re.fid_ent_n = e.id_ent "
                        + "WHERE r.id_year = " + mnRecYear + " AND r.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtEndOfMonth) + "' AND NOT r.b_del AND NOT re.b_del AND "
                        + "re.fid_ct_sys_mov_xxx = " + idCategorySystemMove + " AND re.fid_cur = " + mnCurrencyId + " "
                        + "GROUP BY re.fid_cob_n , re.fid_ent_n , e.ent, e.code, "
                        + "re.fid_acc, re.fk_acc, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx "
                        + "HAVING SUM(re.debit_cur - re.credit_cur) <> 0.0 "
                        + "ORDER BY e.ent, e.code, re.fid_cob_n, re.fid_ent_n; ";
                break;
                
            case SDataConstantsSys.FINS_CT_SYS_MOV_BPS:
                sql = "SELECT re.fid_bp_nr AS _id_bp, re.fid_bpb_n AS _id_bpb, b.bp, b.bp_comm, re.fid_dps_year_n, re.fid_dps_doc_n, "
                        + "re.fid_acc, re.fk_acc, re.fid_ct_ref_n, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, "
                        + "COALESCE(CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num),'') as _doc_num, "
                        + "SUM(re.debit_cur - re.credit_cur) AS _bal_cur, SUM(re.debit - re.credit) AS _bal "
                        + "FROM fin_rec AS r "
                        + "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                        + "INNER JOIN erp.cfgu_cur AS c ON re.fid_cur = c.id_cur "
                        + "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp "
                        + "LEFT OUTER JOIN trn_dps as d on re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc "
                        + "WHERE r.id_year = " + mnRecYear + " AND r.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtEndOfMonth) + "' AND NOT r.b_del AND NOT re.b_del AND "
                        + "re.fid_ct_sys_mov_xxx = " + idCategorySystemMove + " AND re.fid_cur = " + mnCurrencyId + " "
                        + "GROUP BY re.fid_bp_nr, re.fid_bpb_n, b.bp, b.bp_comm, re.fid_dps_year_n, re.fid_dps_doc_n, "
                        + "re.fid_acc, re.fk_acc, re.fid_ct_ref_n, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, "
                        + "d.num_ser, d.num "
                        + "HAVING SUM(re.debit_cur - re.credit_cur) <> 0.0 "
                        + "ORDER BY b.bp, b.bp_comm, re.fid_bp_nr, re.fid_bpb_n, d.num_ser, CAST(d.num AS UNSIGNED), re.fid_dps_year_n, re.fid_dps_doc_n; ";
                break;
                
            default:
        }

        return sql;
    }

    /**
     * Creates accointug record
     * @param pbExchangeRateDifferenceEntry Indicates if is exchange difference entry.
     * @param psConcept Concept of the entry.
     * @param psDebit If it´s debit type.
     * @param psCredit If it´s credit type.
     * @param psAccountId Account Id.
     * @param pnSortingPosition Position.
     * @param panSysMovementTypeKeyXXX System movment type key.
     * @param panCompanyBranchEntityKey Company branch entity key.
     * @param pnBizPartnerId Bussiner partner Id.
     * @param pnBizParnerBranchId Business parner branch Id.
     * @param pnBizParnerCategoryId Business partner category Id.
     * @param panDocumentKey Document year.
     * @return SDataRecordEntry
     */
    private SDataRecordEntry createRecordEntry(final boolean pbExchangeRateDifferenceEntry, 
            final String psConcept, final double pdDebit, final double pdCredit, final String psAccountId, final int pnSortingPosition, 
            final int[] panSysMovementTypeKeyXXX, final int[] panCompanyBranchEntityKey, final int pnBizPartnerId, final int pnBizParnerBranchId, final int pnBizParnerCategoryId, final int[] panDocumentKey) {
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
        entry.setReference("");
        entry.setIsReferenceTax(false);
        entry.setDebit(pdDebit);
        entry.setCredit(pdCredit);
        entry.setExchangeRate(0);
        entry.setExchangeRateSystem(0);
        entry.setDebitCy(0);
        entry.setCreditCy(0);
        entry.setUnits(0);
        entry.setUserId(0);
        entry.setSortingPosition(pnSortingPosition);
        entry.setIsExchangeDifference(true);
        entry.setIsSystem(false);
        entry.setIsDeleted(false);
        
        if (!pbExchangeRateDifferenceEntry) {
            entry.setFkAccountIdXXX(psAccountId);
            //entry.setFkAccountId(...);    // set on save by store procedure
            entry.setFkCostCenterIdXXX_n("");
            entry.setFkCostCenterId_n(SLibConsts.UNDEFINED);
            entry.setFkItemId_n(SLibConsts.UNDEFINED);
        }
        else {
            if (pdCredit > 0) {
                entry.setFkAccountIdXXX(((SClientInterface) miClient).getSessionXXX().getParamsCompany().getFkAccountDifferenceIncomeId_n());
                //entry.setFkAccountId(...);        // set on save by store procedure
                entry.setFkCostCenterIdXXX_n(((SClientInterface) miClient).getSessionXXX().getParamsCompany().getFkCostCenterDifferenceIncomeId_n());
                //entry.setFkCostCenterId_n(...);   // set on save by store procedure
                entry.setFkItemId_n(((SClientInterface) miClient).getSessionXXX().getParamsCompany().getFkItemDifferenceIncomeId_n());
                entry.setFkItemAuxId_n(SLibConsts.UNDEFINED);   // just for consistence
                entry.setFkUnitId_n(SLibConsts.UNDEFINED);      // just for consistence
            }
            else {
                entry.setFkAccountIdXXX(((SClientInterface) miClient).getSessionXXX().getParamsCompany().getFkAccountDifferenceExpenseId_n());
                //entry.setFkAccountId(...);        // set on save by store procedure
                entry.setFkCostCenterIdXXX_n(((SClientInterface) miClient).getSessionXXX().getParamsCompany().getFkCostCenterDifferenceExpenseId_n());
                //entry.setFkCostCenterId_n(...);   // set on save by store procedure
                entry.setFkItemId_n(((SClientInterface) miClient).getSessionXXX().getParamsCompany().getFkItemDifferenceExpenseId_n());
                entry.setFkItemAuxId_n(SLibConsts.UNDEFINED);   // just for consistence
                entry.setFkUnitId_n(SLibConsts.UNDEFINED);      // just for consistence
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
        
        if (pbExchangeRateDifferenceEntry) {
            anSystemMoveTypeKey = pdDebit > 0 ? SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT : SModSysConsts.FINS_TP_SYS_MOV_JOU_CDT;
            anSystemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_NA_NA;
        }
        else {
            if (SFinUtilities.isSysMovementCashAccount(panSysMovementTypeKeyXXX)) {
                if (SLibUtils.compareKeys(panSysMovementTypeKeyXXX, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH)) {
                    anSystemMoveTypeKey = pdDebit > 0 ? SModSysConsts.FINS_TP_SYS_MOV_MI_ADJ : SModSysConsts.FINS_TP_SYS_MOV_MO_ADJ;
                    anSystemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH;
                }
                else if (SLibUtils.compareKeys(panSysMovementTypeKeyXXX, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK)) {
                    anSystemMoveTypeKey = pdDebit > 0 ? SModSysConsts.FINS_TP_SYS_MOV_MI_ADJ : SModSysConsts.FINS_TP_SYS_MOV_MO_ADJ;
                    anSystemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK;
                }
            }
            else if (SFinUtilities.isSysMovementBizPartner(panSysMovementTypeKeyXXX)) {
                if (SLibUtils.compareKeys(panSysMovementTypeKeyXXX, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP)) {
                    anSystemMoveTypeKey = pdCredit > 0 ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_ADJ : SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_DEC_ADJ;
                    anSystemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL;
                }
                else if (SLibUtils.compareKeys(panSysMovementTypeKeyXXX, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS)) {
                    anSystemMoveTypeKey = pdDebit > 0 ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_ADJ : SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_DEC_ADJ;
                    anSystemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL;
                }
                else if (SLibUtils.compareKeys(panSysMovementTypeKeyXXX, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CDR)) {
                    anSystemMoveTypeKey = pdCredit > 0 ? SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_ADJ : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_DEC_ADJ;
                    anSystemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL;
                }
                else if (SLibUtils.compareKeys(panSysMovementTypeKeyXXX, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_DBR)) {
                    anSystemMoveTypeKey = pdDebit > 0 ? SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_ADJ : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_DEC_ADJ;
                    anSystemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL;
                }
            }
        }
        
        entry.setFkSystemMoveClassId(anSystemMoveTypeKey[0]);           // belongs to newest accounting style
        entry.setFkSystemMoveTypeId(anSystemMoveTypeKey[1]);            // belongs to newest accounting style
        
        entry.setFkSystemAccountClassId(anSystemAccountTypeKey[0]);     // belongs to newest accounting style
        entry.setFkSystemAccountTypeId(anSystemAccountTypeKey[1]);      // belongs to newest accounting style
        
        if (pbExchangeRateDifferenceEntry) {
            entry.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_NA[0]);    // belongs to former accounting style
            entry.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_NA[1]);        // belongs to former accounting style
        }
        else {
            entry.setFkSystemMoveCategoryIdXXX(panSysMovementTypeKeyXXX[0]);                   // belongs to former accounting style
            entry.setFkSystemMoveTypeIdXXX(panSysMovementTypeKeyXXX[1]);                       // belongs to former accounting style
        }
        
        // Other record entry foreign keys:
        
        entry.setFkCurrencyId(mnCurrencyId);
        entry.setFkCheckWalletId_n(SLibConsts.UNDEFINED);
        entry.setFkCheckId_n(SLibConsts.UNDEFINED);
        
        entry.setFkBizPartnerId_nr(pnBizPartnerId);
        entry.setFkBizPartnerBranchId_n(pnBizParnerBranchId);
        entry.setFkReferenceCategoryId_n(pnBizParnerCategoryId);
        
        if (!pbExchangeRateDifferenceEntry && SFinUtilities.isSysMovementCashAccount(panSysMovementTypeKeyXXX)) {
            entry.setFkCompanyBranchId_n(panCompanyBranchEntityKey[0]);
            entry.setFkEntityId_n(panCompanyBranchEntityKey[1]);
        }
        else {
            entry.setFkCompanyBranchId_n(SLibConsts.UNDEFINED);
            entry.setFkEntityId_n(SLibConsts.UNDEFINED);
        }
        
        entry.setFkPlantCompanyBranchId_n(SLibConsts.UNDEFINED);
        entry.setFkPlantEntityId_n(SLibConsts.UNDEFINED);
        entry.setFkTaxBasicId_n(SLibConsts.UNDEFINED);
        entry.setFkTaxId_n(SLibConsts.UNDEFINED);
        entry.setFkYearId_n(SLibConsts.UNDEFINED);
        entry.setFkDpsYearId_n(panDocumentKey == null ? SLibConsts.UNDEFINED : panDocumentKey[0]);
        entry.setFkDpsDocId_n(panDocumentKey == null ? SLibConsts.UNDEFINED : panDocumentKey[1]);
        entry.setFkDpsAdjustmentYearId_n(SLibConsts.UNDEFINED);
        entry.setFkDpsAdjustmentDocId_n(SLibConsts.UNDEFINED);
        entry.setFkDiogYearId_n(SLibConsts.UNDEFINED);
        entry.setFkDiogDocId_n(SLibConsts.UNDEFINED);
        entry.setFkMfgYearId_n(SLibConsts.UNDEFINED);
        entry.setFkMfgOrdId_n(SLibConsts.UNDEFINED);
        entry.setFkCostGicId_n(SLibConsts.UNDEFINED);
        entry.setFkPayrollFormerId_n(SLibConsts.UNDEFINED);
        entry.setFkPayrollId_n(SLibConsts.UNDEFINED);
        entry.setFkBookkeepingYearId_n(SLibConsts.UNDEFINED);
        entry.setFkBookkeepingNumberId_n(SLibConsts.UNDEFINED);
        
        entry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        //entry.setFkUserEditId(...);
        //entry.setFkUserDeleteId(...);
        //entry.setUserNewTs(...);
        //entry.setUserEditTs(...);
        //entry.setUserDeleteTs(...);

        return entry;
    }

    /**
     * Check and processes the documents that will be revaluated
     *
     * @throws Exception
     */
    private void procesExchangeData() throws Exception {
        int nSortingPosition = 0;
        int nBizParnerId = 0;
        int nBizParnerBranchId = 0;
        int nBizParnerCategoryId = 0;
        int[] anCompanyBranchEntityKey = null;
        int[] anSysMovementTypeKeyXXX = null;
        int[] anDocumentKey = null;
        double dDebit = 0;
        double dCredit = 0;
        double dBalanceNew = 0;     // new balance in local currency
        double dBalanceCur = 0;     // current balance in local currency
        double dBalanceCurCy = 0;   // current balance in requested currency
        boolean isNatureDebtor = false;
        String sRecordConcept = "";
        String sAccountId = "";
        String sSql = "";
        ResultSet resultSet = null;
        
        for (SDataRecordEntry entry : moRecord.getDbmsRecordEntries()) {
            if (!entry.getIsDeleted()) {
                nSortingPosition = entry.getSortingPosition();
            }
        }
        
        // Valuation of Cash Accounts' balances:

        sSql = composeQuery(SDataConstantsSys.FINS_CT_SYS_MOV_CASH);
        resultSet = miClient.getSession().getStatement().executeQuery(sSql);
        
        while (resultSet.next()) {
            dDebit = 0;
            dCredit = 0;
            dBalanceCur = resultSet.getDouble("_bal");
            dBalanceCurCy = resultSet.getDouble("_bal_cur");
            dBalanceNew = SLibUtils.round(dBalanceCurCy * mdExchangeRate, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
            
            if (dBalanceNew > dBalanceCur) {
                dDebit = SLibUtils.round(dBalanceNew - dBalanceCur, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
            }
            else if (dBalanceNew < dBalanceCur) {
                dCredit = SLibUtils.round(dBalanceCur - dBalanceNew, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
            }

            if (dDebit != 0 || dCredit != 0) {
                anSysMovementTypeKeyXXX = new int[] { resultSet.getInt("re.fid_ct_sys_mov_xxx"), resultSet.getInt("re.fid_tp_sys_mov_xxx") };
                anCompanyBranchEntityKey = new int[] { resultSet.getInt("_id_cob"), resultSet.getInt("_id_ent") };
                sRecordConcept = createConceptRecordEntry("", resultSet.getString("e.ent"));
                sAccountId = resultSet.getString("re.fid_acc");
                              
                maRecordEntries.add(createRecordEntry(false, sRecordConcept, dDebit, dCredit, sAccountId,  ++nSortingPosition, 
                        anSysMovementTypeKeyXXX, anCompanyBranchEntityKey, SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, null)); // exchange rate into cash accounts
                
                maRecordEntries.add(createRecordEntry(true, sRecordConcept, dCredit, dDebit, "",  ++nSortingPosition, 
                       anSysMovementTypeKeyXXX ,null , SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, null)); // exchange rate into income or expenses
            }
        }

        // Valuation of Business Partners' balances:

        sSql = composeQuery(SDataConstantsSys.FINS_CT_SYS_MOV_BPS);
        resultSet = miClient.getSession().getStatement().executeQuery(sSql);
        
        while (resultSet.next()) {
            anSysMovementTypeKeyXXX = new int[] { resultSet.getInt("fid_ct_sys_mov_xxx"), resultSet.getInt("fid_tp_sys_mov_xxx") };
            anDocumentKey = resultSet.getInt("fid_dps_year_n") == SLibConsts.UNDEFINED ? null : new int[] { resultSet.getInt("fid_dps_year_n"), resultSet.getInt("fid_dps_doc_n") };
            isNatureDebtor = SFinUtilities.isSysMovementNatureDebtor(anSysMovementTypeKeyXXX, anDocumentKey != null);
            dDebit = 0;
            dCredit = 0;
            dBalanceCur = SLibUtils.round(resultSet.getDouble("_bal") * (isNatureDebtor ? 1 : -1), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
            dBalanceCurCy = SLibUtils.round(resultSet.getDouble("_bal_cur") * (isNatureDebtor ? 1 : -1), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
            dBalanceNew = SLibUtils.round(dBalanceCurCy * mdExchangeRate, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
            
            /* ASSETS                LIABILITIES
             * ---------------------------------
             * customer w/doc  |  supplier w/doc  
             * debtors         |       creditors
             * supplier wo/doc | customer wo/doc  
             */
            if (dBalanceNew > dBalanceCur) {
                if (isNatureDebtor) {
                    // Debtors or creditors without documents:
                    dDebit = SLibUtils.round(dBalanceNew - dBalanceCur, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                }
                else {
                    // Creditors or debtors without documents:
                    dCredit = SLibUtils.round(dBalanceNew - dBalanceCur, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                }
            }
            else if (dBalanceNew < dBalanceCur) {
                if (isNatureDebtor) {
                    // Creditors or debtors without documents:
                    dCredit = SLibUtils.round(dBalanceCur - dBalanceNew, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                }
                else {
                    // Debtors or creditors without documents:
                    dDebit = SLibUtils.round(dBalanceCur - dBalanceNew, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                }
            }
            
            if (dDebit != 0 || dCredit != 0) {
                nBizParnerId = resultSet.getInt("_id_bp");
                nBizParnerBranchId = resultSet.getInt("_id_bpb");
                nBizParnerCategoryId = resultSet.getInt("re.fid_ct_ref_n");
                sRecordConcept = createConceptRecordEntry(resultSet.getString("_doc_num"), resultSet.getString("b.bp_comm"));
                sAccountId = resultSet.getString("re.fid_acc");
                
                maRecordEntries.add(createRecordEntry(false, sRecordConcept, dDebit, dCredit, sAccountId, ++nSortingPosition, 
                        anSysMovementTypeKeyXXX, null, nBizParnerId, nBizParnerBranchId, nBizParnerCategoryId, anDocumentKey));  // exchange rate into business partners
                maRecordEntries.add(createRecordEntry(true, sRecordConcept, dCredit, dDebit, "",  ++nSortingPosition, 
                        anSysMovementTypeKeyXXX, null , SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, null)); // exchange rate into income or expenses
            }
        }
    }

    /**
     * Starts the proces.
     *
     * @throws Exception
     */
    public void save() throws Exception {
        procesExchangeData();

        moRecord.getDbmsRecordEntries().addAll(maRecordEntries);

        if (moRecord.save(miClient.getSession().getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
        }
    }
}

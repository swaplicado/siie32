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
 * Generación masiva de diferencias cambiarias de revaluación de saldos de documentos en moneda extranjera y cuentas de dinero en moneda extranjera.
 * @author Gerardo Hernández, Uriel Castañeda, Isabel Servín
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
    protected SDataParamsCompany moParamsCompany;
    protected SDataItem moItemIncome;
    protected SDataItem moItemExpenses;
    private final int mnLocalCurrency;
    
    public SValuationBalances(SGuiClient client) {
        miClient = client;
        mnLocalCurrency = miClient.getSession().getSessionCustom().getLocalCurrencyKey()[0];
        mnRecYear = 0;
        mnRecPeriod = 0;
        mnCurrencyId = 0;
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
     * @param reference reference number
     * @param bizPartnerName Business partner or bank name.
     * @return String wiht the concept
     */
    private String createConceptRecordEntry(final String docNumber, final String reference, final String bizPartnerName) {
        String concept;

        if (!docNumber.isEmpty()) {
            concept = SLibUtils.textTrim("REVALUACIÓN/ F " + docNumber + "/ " + bizPartnerName);
        }
        else {
            if (!reference.isEmpty()) {
                concept = SLibUtils.textTrim("REVALUACIÓN/ REF " + reference + "/ " + bizPartnerName);
            }
            else{
                concept = SLibUtils.textTrim("REVALUACIÓN/ SIN REF/ " + bizPartnerName);
            }
        }

        return concept;
    }

    /**
     * Composes SQL query acording the type of movement.
     *
     * @param sysMoveTypeXxx Type of movement.
     * @return SQL query.
     */
    private String composeQueryBizPartnersBalance(final int bizPartnerCat, final int[] sysMoveTypeXxx) {
        String sql = "SELECT re.fid_dps_year_n, re.fid_dps_doc_n, re.ref, re.fid_acc, re.fk_acc, "
                + "re.fid_bp_nr, re.fid_bpb_n, d.num_ser, d.num, b.bp_comm, bb.bpb, re.fid_cur, " 
                + "SUM(re.debit_cur) AS _dbt_cur, SUM(re.credit_cur) AS _cdt_cur, SUM(re.debit) AS _dbt, SUM(re.credit) AS _cdt " 
                + "FROM fin_rec AS r " 
                + "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " 
                + "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " 
                + "INNER JOIN erp.bpsu_bp_ct AS bct ON re.fid_bp_nr = bct.id_bp AND bct.id_ct_bp = " + bizPartnerCat + " "
                + "INNER JOIN erp.bpsu_tp_bp AS btp ON bct.fid_ct_bp = btp.id_ct_bp AND bct.fid_tp_bp = btp.id_tp_bp " 
                + "LEFT OUTER JOIN erp.bpsu_bpb AS bb ON re.fid_bpb_n = bb.id_bpb " 
                + "LEFT OUTER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc " 
                + "WHERE r.id_year = " + mnRecYear + " AND r.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtEndOfMonth) + "' " 
                + "AND NOT r.b_del AND NOT re.b_del " 
                + "AND re.fid_ct_sys_mov_xxx = " + sysMoveTypeXxx[0] + " AND re.fid_tp_sys_mov_xxx = " + sysMoveTypeXxx[1] + " " 
                + "AND re.fid_cur = " + mnCurrencyId + " " 
                + "GROUP BY re.fid_bp_nr, re.fid_bpb_n, re.fid_cur, re.fid_dps_year_n, re.fid_dps_doc_n, re.ref, re.fid_acc, re.fk_acc " 
                + "HAVING SUM(re.debit_cur - re.credit_cur) <> 0 " 
                + "ORDER BY b.bp_comm, bb.bpb, re.fid_bp_nr, re.fid_bpb_n ,d.num_ser, d.num, re.fid_dps_year_n, re.fid_dps_doc_n, re.ref, re.fid_acc, re.fk_acc;";
        
        return sql;
    }
    
    private String composeQueryCashAccountsBalance(final int[] sysMoveTypeXxx){
        String sql = "SELECT re.fid_cob_n, re.fid_ent_n, ent.ent, re.ref, re.fid_acc, re.fk_acc, re.fid_cur, "
                + "SUM(re.debit_cur) AS _dbt_cur, SUM(re.credit_cur) AS _cdt_cur, SUM(re.debit) AS _dbt, SUM(re.credit) AS _cdt "
                + "FROM fin_rec AS r "
                + "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "INNER JOIN erp.cfgu_cob_ent AS ent ON re.fid_cob_n = ent.id_cob AND re.fid_ent_n = ent.id_ent "
                + "WHERE NOT r.b_del AND NOT re.b_del AND "
                + "r.id_year = " + mnRecYear + " AND r.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtEndOfMonth) + "' AND " 
                + "re.fid_ct_sys_mov_xxx = " + sysMoveTypeXxx[0] + " AND re.fid_tp_sys_mov_xxx = " + sysMoveTypeXxx[1] + " AND " 
                + "re.fid_cur = " + mnCurrencyId + " " 
                + "GROUP BY re.fid_cob_n, re.fid_ent_n, ent.ent, re.ref, re.fid_acc, re.fk_acc, re.fid_cur "
                + "HAVING SUM(re.debit_cur - re.credit_cur) <> 0 " // cuentas de dinero con saldo
                + "ORDER BY ent.ent, re.fid_cob_n, re.fid_ent_n, re.ref, re.fid_acc, re.fk_acc;";
        
        return sql; 
    }

    /**
     * Creates accointug record
     * @param pnBizPartnerCategory Business partner category.
     * @param pnAccountCashType Account cash type.
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
     * @param panCobEntKey Company branch entity key (cash account).
     * @param pnCurrencyId Currency Id.
     * @return SDataRecordEntry
     */
    private SDataRecordEntry createRecordEntry(final int pnBizPartnerCategory, final int pnAccountCashType, final boolean pbIsIncomeExpensesEntry, 
            final String psConcept, final double pdDebit, final double pdCredit, final String psAccountId, final int pnSortingPosition, 
            final int[] panSysMoveTypeKeyXXX, final int pnBizPartnerId, final int pnBizParnerBranchId, 
            final int[] panDocumentKey, final int[] panCobEntKey, final String psReference, final int pnCurrencyId) {
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
        
        if (pnBizPartnerCategory != 0) {
            switch (pnBizPartnerCategory) {
                case SDataConstantsSys.BPSS_CT_BP_SUP:
                case SDataConstantsSys.BPSS_CT_BP_CDR:
                    if (pnBizPartnerCategory == SDataConstantsSys.BPSS_CT_BP_SUP) {
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
                    else if (SLibUtils.compareKeys(panSysMoveTypeKeyXXX, SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND)) { // note that taxes ar not supported yet!
                        if (!pbIsIncomeExpensesEntry) {
                            anSystemMoveTypeKey = pdDebit > 0 ? incrementKey : decrementKey;
                        }
                        else {
                            anSystemMoveTypeKey = pdCredit > 0 ? incrementKey : decrementKey;
                        }
                    }
                    else if (SLibUtils.compareKeys(panSysMoveTypeKeyXXX, SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND)) { // note that taxes ar not supported yet!
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
                    if (pnBizPartnerCategory == SDataConstantsSys.BPSS_CT_BP_CUS) {
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
                    else if (SLibUtils.compareKeys(panSysMoveTypeKeyXXX, SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND)) { // note that taxes ar not supported yet!
                        if (!pbIsIncomeExpensesEntry) {
                            anSystemMoveTypeKey = pdCredit > 0 ? incrementKey : decrementKey;
                        }
                        else {
                            anSystemMoveTypeKey = pdDebit > 0 ? incrementKey : decrementKey;
                        }
                    }
                    else if (SLibUtils.compareKeys(panSysMoveTypeKeyXXX, SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND)) { // note that taxes ar not supported yet!
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
        }
        else if (pnAccountCashType != 0) {
            switch (pnAccountCashType) {
                case SDataConstantsSys.FINS_CT_ACC_CASH_CASH:
                case SDataConstantsSys.FINS_CT_ACC_CASH_BANK:
                    if (pdDebit != 0) {
                        anSystemMoveTypeKey = SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT;
                    }
                    else {
                        anSystemMoveTypeKey = SModSysConsts.FINS_TP_SYS_MOV_JOU_CDT;
                    }
                    break;
                default:
            }
        }
        
        if (pbIsIncomeExpensesEntry || (!SFinUtilities.isSysMovementBizPartner(panSysMoveTypeKeyXXX) && !SFinUtilities.isSysMovementCashAccount(panSysMoveTypeKeyXXX))) {
            anSystemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_NA_NA;
        }
        else if (pnBizPartnerCategory != 0) {
            switch (pnBizPartnerCategory) {
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
        else if (pnAccountCashType != 0) {
            switch (pnAccountCashType) {
                case SDataConstantsSys.FINS_CT_ACC_CASH_CASH:
                    anSystemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH;
                    break;
                case SDataConstantsSys.FINS_CT_ACC_CASH_BANK:
                    anSystemAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK;
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
        
        if (pnBizPartnerId != 0) {
            entry.setFkReferenceCategoryId_n(panSysMoveTypeKeyXXX[1]);
        }
        else {
            entry.setFkReferenceCategoryId_n(0);
        }
        
        entry.setFkCompanyBranchId_n(panCobEntKey == null ? 0 : panCobEntKey[0]);
        entry.setFkEntityId_n(panCobEntKey == null ? 0 : panCobEntKey[1]);
        entry.setFkPlantCompanyBranchId_n(0);
        entry.setFkPlantEntityId_n(0);
        
        entry.setFkTaxBasicId_n(0);
        entry.setFkTaxId_n(0);
                
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
    
    private double[] computeDebitCredit(final double debitCur, final double creditCur, 
            final double debit, final double credit, final boolean isDebit) {
        double[] debitCredit =  new double[] { 0, 0 };
        double balanceCur = SLibUtils.roundAmount(isDebit ? debitCur - creditCur : creditCur - debitCur);
        double balance = SLibUtils.roundAmount(isDebit ? debit - credit : credit - debit);
        double newBalance = SLibUtils.roundAmount(balanceCur * mdExchangeRate);
        double difference = SLibUtils.roundAmount(newBalance - balance);
        
        if (difference > 0) {
            if (isDebit) {
                debitCredit[0] = SLibUtils.roundAmount(difference);
            }
            else {
                debitCredit[1] = SLibUtils.roundAmount(difference);
            }
        }
        else if (difference < 0) {
            if (isDebit) {
                debitCredit[1] = SLibUtils.roundAmount(-difference);
            }
            else {
                debitCredit[0] = SLibUtils.roundAmount(-difference);
            }
        }
        
        return debitCredit;
    }

    /**
     * Check and processes the documents and cash accounts that will be revaluated.
     *
     * @throws Exception
     */
    private void processRevaluation() throws Exception {
        Connection connection = miClient.getSession().getStatement().getConnection();
        int nSortingPosition = 0;
        int nBizParnerId;
        int nBizParnerBranchId;
        int nCurrencyId;
        int[] anSysMovTypeXxxKeyBizPartner;
        int[] anSysMovTypeXxxKeyCashAccount;
        int[] anDocumentKey;
        int[] anCobEntKey;
        double[] adDebitCredit;
        boolean isDebit;
        String sReference;
        String sRecordConcept;
        String sSql;
        String sIdAccBal;
        ResultSet resultSet;
        
        // setting sorting position of new accounting voucher entries:
        
        for (SDataRecordEntry entry : moRecord.getDbmsRecordEntries()) {
            if (!entry.getIsDeleted()) {
                nSortingPosition = entry.getSortingPosition();
            }
        }
        
        // processing busines partners:
        
        int[] bizPartnerCategories = new int[] {
            SDataConstantsSys.BPSS_CT_BP_SUP, // proveedores
            SDataConstantsSys.BPSS_CT_BP_CUS, // clientes
            SDataConstantsSys.BPSS_CT_BP_CDR, // acreedores
            SDataConstantsSys.BPSS_CT_BP_DBR }; // deudores
        
        HashMap<Integer, int[]> sysMoveTypeXxxBizPartners = new HashMap<>();
        
        sysMoveTypeXxxBizPartners.put(SDataConstantsSys.BPSS_CT_BP_SUP, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
        sysMoveTypeXxxBizPartners.put(SDataConstantsSys.BPSS_CT_BP_CUS, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
        sysMoveTypeXxxBizPartners.put(SDataConstantsSys.BPSS_CT_BP_CDR, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CDR); 
        sysMoveTypeXxxBizPartners.put(SDataConstantsSys.BPSS_CT_BP_DBR, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_DBR); 
        
        int docSup = 0;
        int accSup = 0;
        int docCus = 0;
        int accCus = 0;
        int cdr = 0;
        int dbr = 0;
        
        for (int bizPartnerCategory : bizPartnerCategories) {
            anSysMovTypeXxxKeyBizPartner = sysMoveTypeXxxBizPartners.get(bizPartnerCategory);
            sSql = composeQueryBizPartnersBalance(bizPartnerCategory, anSysMovTypeXxxKeyBizPartner);
            resultSet = connection.createStatement().executeQuery(sSql);
            
            while (resultSet.next()) {
                switch (bizPartnerCategory) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP: 
                        if (resultSet.getInt("fid_dps_year_n") == 0) {
                            accSup++;
                        }
                        else {
                            docSup++; 
                        }
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS: 
                        if (resultSet.getInt("fid_dps_year_n") == 0) {
                            accCus++;
                        }
                        else {
                            docCus++; 
                        }
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CDR: cdr++; break;
                    case SDataConstantsSys.BPSS_CT_BP_DBR: dbr++; break;
                }
                
                isDebit = SLibUtils.belongsTo(bizPartnerCategory, new int[] { SDataConstantsSys.BPSS_CT_BP_CUS, SDataConstantsSys.BPSS_CT_BP_DBR });
                
                adDebitCredit = computeDebitCredit(resultSet.getDouble("_dbt_cur"), resultSet.getDouble("_cdt_cur"), 
                        resultSet.getDouble("_dbt"), resultSet.getDouble("_cdt"), isDebit);
                
                if (adDebitCredit[0] != 0 || adDebitCredit[1] != 0) {
                    anDocumentKey = resultSet.getInt("re.fid_dps_year_n") == 0 ? null : new int[] { resultSet.getInt("re.fid_dps_year_n"), resultSet.getInt("re.fid_dps_doc_n") };
                    anCobEntKey = null;
                    sReference = resultSet.getString("re.ref");
                    sIdAccBal = resultSet.getString("re.fid_acc");
                    nBizParnerId = resultSet.getInt("fid_bp_nr");
                    nBizParnerBranchId = resultSet.getInt("fid_bpb_n");
                    nCurrencyId = resultSet.getInt("re.fid_cur");
                    sRecordConcept = createConceptRecordEntry(
                            STrnUtils.formatDocNumber(resultSet.getString("d.num_ser") == null ? "" : resultSet.getString("d.num_ser"), resultSet.getString("d.num") == null ? "" : resultSet.getString("d.num")), 
                            sReference, resultSet.getString("b.bp_comm"));
                    
                    maRecordEntries.add(createRecordEntry(bizPartnerCategory, 0, false, sRecordConcept, adDebitCredit[0], adDebitCredit[1], sIdAccBal, ++nSortingPosition, 
                            anSysMovTypeXxxKeyBizPartner, nBizParnerId, nBizParnerBranchId, anDocumentKey, anCobEntKey, sReference, nCurrencyId));

                    maRecordEntries.add(createRecordEntry(bizPartnerCategory, 0, true, sRecordConcept, adDebitCredit[1], adDebitCredit[0], "", ++nSortingPosition, 
                            anSysMovTypeXxxKeyBizPartner, 0, 0, anDocumentKey, anCobEntKey, "", mnLocalCurrency));
                }
            }
        }
        
        // processing cash accounts:

        int[] cashAccountTypes = new int[] {
            SDataConstantsSys.FINS_CT_ACC_CASH_CASH,
            SDataConstantsSys.FINS_CT_ACC_CASH_BANK };
        
        HashMap<Integer, int[]> sysMoveTypeXxxCashAccounts = new HashMap<>();
        
        sysMoveTypeXxxCashAccounts.put(SDataConstantsSys.FINS_CT_ACC_CASH_CASH, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH);
        sysMoveTypeXxxCashAccounts.put(SDataConstantsSys.FINS_CT_ACC_CASH_BANK, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK);
        
        int cash = 0;
        int bank = 0;
        
        for (int cashAccountType : cashAccountTypes) {
            anSysMovTypeXxxKeyCashAccount = sysMoveTypeXxxCashAccounts.get(cashAccountType);
            sSql = composeQueryCashAccountsBalance(anSysMovTypeXxxKeyCashAccount);
            resultSet = connection.createStatement().executeQuery(sSql);
            
            while (resultSet.next()) {
                switch (cashAccountType) {
                    case SDataConstantsSys.FINS_CT_ACC_CASH_CASH: cash++; break;
                    case SDataConstantsSys.FINS_CT_ACC_CASH_BANK: bank++; break;
                }
                
                isDebit = true;
                
                adDebitCredit = computeDebitCredit(resultSet.getDouble("_dbt_cur"), resultSet.getDouble("_cdt_cur"), 
                        resultSet.getDouble("_dbt"), resultSet.getDouble("_cdt"), isDebit);
                
                if (adDebitCredit[0] != 0 || adDebitCredit[1] != 0) {
                    anDocumentKey = null;
                    anCobEntKey = new int[] {resultSet.getInt("fid_cob_n"), resultSet.getInt("fid_ent_n")};
                    sReference = resultSet.getString("re.ref");
                    sIdAccBal = resultSet.getString("re.fid_acc");
                    nCurrencyId = resultSet.getInt("re.fid_cur");
                    sRecordConcept = createConceptRecordEntry("", sReference, resultSet.getString("ent"));

                    maRecordEntries.add(createRecordEntry(0, cashAccountType, false, sRecordConcept, adDebitCredit[0], adDebitCredit[1], sIdAccBal, ++nSortingPosition, 
                        anSysMovTypeXxxKeyCashAccount, 0, 0, anDocumentKey, anCobEntKey, sReference, nCurrencyId));

                    maRecordEntries.add(createRecordEntry(0, cashAccountType, true, sRecordConcept, adDebitCredit[1], adDebitCredit[0], "", ++nSortingPosition, 
                        anSysMovTypeXxxKeyCashAccount, 0, 0, anDocumentKey, anCobEntKey, "", mnLocalCurrency));
                }
            }
        }
        miClient.showMsgBoxInformation("Se procesaron:\n-" + docSup + " documentos de proveedores.\n-" + accSup + " cuentas de proveedores.\n"
                + "-" + docCus + " documentos de clientes.\n-" + accCus + " cuentas de clientes.\n"
                + "-" + cdr + " cuentas de acreedores.\n-" + dbr + " cuentas de deudores.\n-" + cash + " cuentas de caja.\n-" + bank + " cuentas de bancos.");
    }           
    
    /**
     * Start the process.
     *
     * @throws Exception
     */
    public void save() throws Exception {
        processRevaluation();

        moRecord.getDbmsRecordEntries().addAll(maRecordEntries);

        if (moRecord.save(miClient.getSession().getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
        }
    }
}

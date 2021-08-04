/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mfin.data.SDataBookkeepingNumber;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Sergio Flores
 */
public class SBalanceExchangeDiff {
    protected SGuiClient miClient;
    
    protected int mnRecYear;
    protected int mnRecPeriod;
    protected int mnCurrencyId;
    protected double mdExchangeRate;
    protected SDataRecord moRecord;
    
    protected ArrayList<SDataRecordEntry> maRecordEntries;
    private int nBookkeepingYear;
    private int nBookkeepingNum;
    
    private final int SYS_MOVE_ACC = 3;
    private final int SYS_MOVE_BPS = 4;
    

    protected Date mtDate;
    
    public SBalanceExchangeDiff(SGuiClient client) {
        maRecordEntries = new ArrayList<SDataRecordEntry>();
        mnRecYear = 0;
        mnRecPeriod = 0;
        mnCurrencyId = 0;
        mdExchangeRate = 0d;
        moRecord = null;
        miClient = client;
    }
    
    public void setRecYear(int n) { mnRecYear = n; }
    public void setRecPeriod(int n) { mnRecPeriod = n; }
    public void setCurrencyId(int n) { mnCurrencyId = n; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setRecord(SDataRecord o) { moRecord = o; }
    public void setRecordEntries(ArrayList<SDataRecordEntry> a) { maRecordEntries = a; }
    public void setDate(Date t) { mtDate = t; }
    
    public int getRecYear() { return mnRecYear; }
    public int getRecPeriod() { return mnRecPeriod; }
    public int getCurrencyId() { return mnCurrencyId; }
    public double getExchangeRate() { return mdExchangeRate; }
    public SDataRecord getRecord() { return moRecord; }
    public ArrayList<SDataRecordEntry> getRecordEntries() { return maRecordEntries; }
    public Date getDate() { return mtDate; }
    

    

private String createConceptRecordEntry(String f_acc, String entity) {
    String concept;
    concept = "Reval men/ F " + f_acc + " / " + entity;
    return (concept.length() > 100 ? SLibUtilities.textLeft(concept, 100) : concept).trim();
}
private void computeBookKeeping() throws Exception{
    nBookkeepingYear = 0;
    nBookkeepingNum = 0;
    SDataBookkeepingNumber bookkeepingNumber = null;
    bookkeepingNumber = new SDataBookkeepingNumber();
    
    bookkeepingNumber.setPkYearId(moRecord.getPkYearId());
    bookkeepingNumber.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
    if (bookkeepingNumber.save(miClient.getSession().getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
    }
    else {
        nBookkeepingYear = bookkeepingNumber.getPkYearId();
        nBookkeepingNum = bookkeepingNumber.getPkNumberId();
    }
}

    private String getQuery(int tpSysMov) {
        String sql = "";
        SimpleDateFormat Dateformater = new SimpleDateFormat("yyyy-MM-dd");

        switch(tpSysMov){
            case SYS_MOVE_ACC :
                sql = "SELECT e.id_cob, e.id_ent, e.fid_ct_ent, fid_tp_ent, bb.bpb, e.ent, e.code, re.fid_acc, re.fid_cc_n, ce.cur_key, e.b_del, fid_ct_ref_n, " +
                       "SUM(re.debit_cur - re.credit_cur) AS f_sf_cur, " +
                       "SUM(re.debit - re.credit) AS f_sf, " +
                       "re.fid_tp_sys_mov_xxx, re.exc_rate " +
                       "FROM fin_rec AS r " +
                       "INNER JOIN fin_rec_ety AS re ON  r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
                       "INNER JOIN erp.cfgu_cur AS c ON re.fid_cur = c.id_cur " +
                       "INNER JOIN fin_acc AS a ON re.fid_acc = a.id_acc " +
                       "INNER JOIN erp.cfgu_cob_ent AS e ON re.fid_cob_n = e.id_cob AND re.fid_ent_n = e.id_ent " +
                       "INNER JOIN erp.bpsu_bpb AS bb ON e.id_cob = bb.id_bpb " +
                       "INNER JOIN fin_acc_cash AS ac ON e.id_cob = ac.id_cob AND e.id_ent = ac.id_acc_cash " +
                       "INNER JOIN erp.cfgu_cur AS ce ON ac.fid_cur = ce.id_cur " +
                       "WHERE re.fid_ct_sys_mov_xxx = " + tpSysMov + " AND r.id_year = " + mnRecYear + " AND r.dt <= '" + Dateformater.format(mtDate) + "'  AND r.b_del = 0 AND re.b_del = 0 AND b_adj_year = 0  AND b_adj_audit = 0 AND re.fid_cur = " + mnCurrencyId + " " +
                       "GROUP BY e.id_cob, e.id_ent, bb.bpb, e.ent, e.code, re.fid_acc, re.fid_cc_n, ce.cur_key, e.b_del HAVING SUM(re.debit_cur - re.credit_cur) != 0 " +
                       "ORDER BY bb.bpb, e.id_cob, e.ent, e.id_ent";
                break;
            case SYS_MOVE_BPS :
                sql = "SELECT b.id_bp, b.bp, b.b_del, re.fid_dps_year_n, re.fid_dps_doc_n, re.fid_acc, re.fid_cc_n, re.fid_ct_ref_n, re.fid_bpb_n, re.fid_ct_sys_mov_xxx, re.fid_tp_sys_mov_xxx, " +
                       "SUM(re.debit_cur - re.credit_cur) AS f_sf_cur, " +
                       "SUM(re.debit - re.credit) AS f_sf, " +
                       "((SUM(re.debit_cur - re.credit_cur) * " + mdExchangeRate + ") - SUM(re.debit - re.credit)) AS dif_sf, re.exc_rate " +
                       "FROM fin_rec AS r " +
                       "INNER JOIN fin_rec_ety AS re ON  r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
                       "INNER JOIN erp.cfgu_cur AS c ON re.fid_cur = c.id_cur " +
                       "INNER JOIN fin_acc AS a ON re.fid_acc = a.id_acc " +
                       "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " +
                       "WHERE re.fid_ct_sys_mov_xxx = " + tpSysMov + " AND re.fid_tp_sys_mov_xxx != 1 " +
                       "AND r.id_year = " + mnRecYear + " AND r.dt <= '" + Dateformater.format(mtDate) + "'" +
                       "AND r.b_del = 0 AND re.b_del = 0 AND b_adj_year = 0 AND b_adj_audit = 0 AND re.fid_cur = " + mnCurrencyId + " " +
                       "GROUP BY re.fid_dps_year_n, re.fid_dps_doc_n, re.fid_acc, re.fid_cc_n, b.id_bp, b.bp, b.b_del HAVING SUM(re.debit_cur - re.credit_cur) != 0 " +
                       "ORDER BY b.bp, b.id_bp;";
        }

        return sql;
    }



    private SDataRecordEntry createBpsRecordEntry(int tpSysMov, int nSortingPosition, double debit, double credit, String accountIdXXX, String costCenterIdXXX, int [] bp_cat, int [] acc_cat, int BizPartnerId_nr, int BizPartnerBranchId_n, int companyBranchId_n, int entityId_n, int dpsYearId, int dpsDocId_n) throws Exception{

        SDataRecordEntry entry=null;
        entry = new SDataRecordEntry();
        computeBookKeeping();

        /*se añade al guardar
        entry.setPkYearId(int n);
        entry.setPkPeriodId(int n);
        entry.setPkBookkeepingCenterId(int n);
        entry.setPkRecordTypeId(java.lang.String s);
        entry.setPkNumberId(int n);
        entry.setPkEntryId(int n);
        */
        entry.setConcept("");
        entry.setReference("");
        entry.setIsReferenceTax(false);
        entry.setDebit(debit);
        entry.setCredit(credit);
        entry.setExchangeRate(0);
        entry.setExchangeRateSystem(0);
        entry.setDebitCy(0);
        entry.setCreditCy(0);
        entry.setUnits(0d);
        entry.setUserId(0);
        entry.setSortingPosition(nSortingPosition);
        entry.setIsExchangeDifference(true);
        entry.setIsSystem(false);
        entry.setIsDeleted(false);
        entry.setFkAccountIdXXX(accountIdXXX);
        entry.setFkAccountId(SLibConstants.UNDEFINED);
        entry.setFkCostCenterId_n(SLibConstants.UNDEFINED);
        entry.setFkCostCenterIdXXX_n(costCenterIdXXX);
        entry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        entry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        entry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
        if (debit > 0) {
            entry.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_MI_ADJ[0]);
            entry.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_MI_ADJ[1]);
        }
        else if (credit > 0) {
            entry.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_MO_ADJ[0]);
            entry.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_MO_ADJ[1]);
        }

        if (SLibUtilities.compareKeys(bp_cat, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP)) {
            entry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL[0]);
            entry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL[1]);

            entry.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0]);
            entry.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1]);

        }
        else if (SLibUtilities.compareKeys(bp_cat, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS)) {
            entry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL[0]);
            entry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL[1]);

            entry.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[0]);
            entry.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1]);

        }
        else if (SLibUtilities.compareKeys(bp_cat, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CDR)) {
            entry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL[0]);
            entry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL[1]);

            entry.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CDR[0]);
            entry.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CDR[1]);
        }
        else if (SLibUtilities.compareKeys(bp_cat, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_DBR)) {
            entry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL[0]);
            entry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL[1]);

            entry.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_DBR[0]);
            entry.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_BPS_DBR[1]);
        }

        else if (SLibUtilities.compareKeys(acc_cat, SDataConstantsSys.CFGS_TP_ENT_CASH_CASH)) {
            entry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH[0]);
            entry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH[1]);

            entry.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH[0]);
            entry.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH[1]);
        }

        else if (SLibUtilities.compareKeys(acc_cat, SDataConstantsSys.CFGS_TP_ENT_CASH_BANK)) {
            entry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK[0]);
            entry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK[1]);

            entry.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK[0]);
            entry.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK[1]);
        }

        entry.setFkCurrencyId(mnCurrencyId);
        entry.setFkCheckWalletId_n(SLibConstants.UNDEFINED);
        entry.setFkCheckId_n(SLibConstants.UNDEFINED);
        entry.setFkBizPartnerId_nr(BizPartnerId_nr);  
        entry.setFkBizPartnerBranchId_n(BizPartnerBranchId_n);
        entry.setFkReferenceCategoryId_n(SLibConstants.UNDEFINED);

        if (tpSysMov == SYS_MOVE_ACC) {
            entry.setFkCompanyBranchId_n(companyBranchId_n);
            entry.setFkEntityId_n(entityId_n);
        }
        else {
            entry.setFkCompanyBranchId_n(SLibConstants.UNDEFINED);
            entry.setFkEntityId_n(SLibConstants.UNDEFINED);
        }

        entry.setFkPlantCompanyBranchId_n(SLibConstants.UNDEFINED);
        entry.setFkPlantEntityId_n(SLibConstants.UNDEFINED);
        entry.setFkTaxBasicId_n(SLibConstants.UNDEFINED);
        entry.setFkTaxId_n(SLibConstants.UNDEFINED);
        entry.setFkYearId_n(SLibConstants.UNDEFINED);

        if (tpSysMov == SYS_MOVE_ACC) {
            entry.setFkDpsYearId_n(SLibConstants.UNDEFINED);
            entry.setFkDpsDocId_n(SLibConstants.UNDEFINED);
        }
        else {
            entry.setFkDpsYearId_n(dpsYearId);
            entry.setFkDpsDocId_n(dpsDocId_n);
        }


        entry.setFkDpsAdjustmentYearId_n(SLibConstants.UNDEFINED);
        entry.setFkDpsAdjustmentDocId_n(SLibConstants.UNDEFINED);
        entry.setFkDiogYearId_n(SLibConstants.UNDEFINED);
        entry.setFkDiogDocId_n(SLibConstants.UNDEFINED);
        entry.setFkMfgYearId_n(SLibConstants.UNDEFINED);
        entry.setFkMfgOrdId_n(SLibConstants.UNDEFINED);
        entry.setFkCostGicId_n(SLibConstants.UNDEFINED);
        entry.setFkPayrollFormerId_n(SLibConstants.UNDEFINED);
        entry.setFkPayrollId_n(SLibConstants.UNDEFINED);
        entry.setFkItemId_n(SLibConstants.UNDEFINED);
        entry.setFkItemAuxId_n(SLibConstants.UNDEFINED);
        entry.setFkUnitId_n(SLibConstants.UNDEFINED);
        entry.setFkBookkeepingYearId_n(nBookkeepingYear);
        entry.setFkBookkeepingNumberId_n(nBookkeepingNum);
        entry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        entry.setFkUserEditId(SUtilConsts.USR_NA_ID);
        entry.setFkUserDeleteId(SUtilConsts.USR_NA_ID);
        /*se añade al guardar
        entry.setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
        entry.setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
        entry.setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }
        */
        
        return entry;
    }
    /*private SdataRecordEntry createAccCashRecordEntry()
    */
    private SDataRecordEntry createDiffExpRecordEntry( int nSortingPosition, double debit, double credit, int BizPartnerId_nr, int BizPartnerBranchId_n, int companyBranchId_n, int entityId_n, int dpsYearId, int dpsDocId_n){
        SDataRecordEntry entry;

        entry = new SDataRecordEntry();
        /*se añade al guardar
        entry.setPkYearId(int n);
        entry.setPkPeriodId(int n);
        entry.setPkBookkeepingCenterId(int n);
        entry.setPkRecordTypeId(java.lang.String s);
        entry.setPkNumberId(int n);
        entry.setPkEntryId(int n);
        */
        entry.setConcept("");
        entry.setReference("");
        entry.setIsReferenceTax(false);
        entry.setDebit(debit);
        entry.setCredit(credit);
        entry.setExchangeRate(1);
        entry.setExchangeRateSystem(1);
        entry.setDebitCy(debit);
        entry.setCreditCy(credit);
        entry.setUnits(0d);
        entry.setUserId(0); 
        entry.setSortingPosition(nSortingPosition);
        entry.setIsExchangeDifference(false);
        entry.setIsSystem(false);
        entry.setIsDeleted(false);
        if(debit > 0) {
            entry.setFkAccountIdXXX(((SClientInterface) miClient).getSessionXXX().getParamsCompany().getFkAccountDifferenceIncomeId_n());//este cambia supongo que por 7000-0003-0000
            entry.setFkCostCenterIdXXX_n(((SClientInterface) miClient).getSessionXXX().getParamsCompany().getFkCostCenterDifferenceIncomeId_n());
            entry.setFkItemId_n(((SClientInterface) miClient).getSessionXXX().getParamsCompany().getFkItemDifferenceIncomeId_n());// aqui iria el item 7247?

        }
        else if (credit > 0){
            entry.setFkAccountIdXXX(((SClientInterface) miClient).getSessionXXX().getParamsCompany().getFkAccountDifferenceExpenseId_n());//este cambia supongo que por 7000-0003-0000
            entry.setFkCostCenterIdXXX_n(((SClientInterface) miClient).getSessionXXX().getParamsCompany().getFkCostCenterDifferenceExpenseId_n());
            entry.setFkItemId_n(((SClientInterface) miClient).getSessionXXX().getParamsCompany().getFkItemDifferenceExpenseId_n());// aqui iria el item 7247?
        }

        entry.setFkAccountId(SLibConstants.UNDEFINED);
        entry.setFkCostCenterId_n(SLibConstants.UNDEFINED);
        entry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        entry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        entry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
        if (debit > 0) {
            entry.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_MI_ADJ[0]);//11 o 12 
            entry.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_MI_ADJ[1]);//33
        }
        else if (credit > 0) {
            entry.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_MO_ADJ[0]);//11 o 12 
            entry.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_MO_ADJ[1]);//33
        }
        entry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_NA_NA[0]);
        entry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_NA_NA[1]);
        entry.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_NA[0]);
        entry.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_NA[1]);
        entry.setFkCurrencyId(((SClientInterface) miClient).getSessionXXX().getParamsErp().getFkCurrencyId());
        entry.setFkCheckWalletId_n(SLibConstants.UNDEFINED);
        entry.setFkCheckId_n(SLibConstants.UNDEFINED);
        entry.setFkBizPartnerId_nr(BizPartnerId_nr);  
        entry.setFkBizPartnerBranchId_n(BizPartnerBranchId_n);
        entry.setFkReferenceCategoryId_n(SLibConstants.UNDEFINED);
        entry.setFkCompanyBranchId_n(companyBranchId_n);
        entry.setFkEntityId_n(entityId_n);
        entry.setFkPlantCompanyBranchId_n(SLibConstants.UNDEFINED);
        entry.setFkPlantEntityId_n(SLibConstants.UNDEFINED);
        entry.setFkTaxBasicId_n(SLibConstants.UNDEFINED);
        entry.setFkTaxId_n(SLibConstants.UNDEFINED);
        entry.setFkYearId_n(SLibConstants.UNDEFINED);
        entry.setFkDpsYearId_n(dpsYearId);
        entry.setFkDpsDocId_n(dpsDocId_n);
        entry.setFkDpsAdjustmentYearId_n(SLibConstants.UNDEFINED);
        entry.setFkDpsAdjustmentDocId_n(SLibConstants.UNDEFINED);
        entry.setFkDiogYearId_n(SLibConstants.UNDEFINED);
        entry.setFkDiogDocId_n(SLibConstants.UNDEFINED);
        entry.setFkMfgYearId_n(SLibConstants.UNDEFINED);
        entry.setFkMfgOrdId_n(SLibConstants.UNDEFINED);
        entry.setFkCostGicId_n(SLibConstants.UNDEFINED);
        entry.setFkPayrollFormerId_n(SLibConstants.UNDEFINED);
        entry.setFkPayrollId_n(SLibConstants.UNDEFINED);
        entry.setFkItemAuxId_n(SLibConstants.UNDEFINED);
        entry.setFkUnitId_n(SLibConstants.UNDEFINED);
        entry.setFkBookkeepingYearId_n(nBookkeepingYear);
        entry.setFkBookkeepingNumberId_n(nBookkeepingNum);
        entry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        entry.setFkUserEditId(SUtilConsts.USR_NA_ID);
        entry.setFkUserDeleteId(SUtilConsts.USR_NA_ID);
        /*se añade al guardar
        entry.setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
        entry.setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
        entry.setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }
        */

        return entry;
    }

    private void createBpsRecordEntries() throws Exception{
        ResultSet resultSet = null;
        String sSql;
        double nBalanceTotal = 0;
        double nBalanceTotalCur = 0;
        double nBalanceTotalNew = 0;
        double debit = 0;
        double credit = 0;
        int nSortingPosition = 0;
        int idCob = 0;
        int idEnt = 0;
        int dpsYear = 0;
        int dpsDoc = 0;
        int [] bp_cat;
        int bp;
        int bpb;
        String idAcc;
        String costC;
        int [] acc_cat;

       for (SDataRecordEntry rEntry : moRecord.getDbmsRecordEntries()) {
            if (!rEntry.getIsDeleted()) {
                nSortingPosition = rEntry.getSortingPosition();
            }
        }
        acc_cat = new int [2];
        bp_cat = null;
        sSql = getQuery(SYS_MOVE_ACC);
        resultSet = miClient.getSession().getStatement().executeQuery(sSql);
        while (resultSet.next()){
            nBalanceTotal =resultSet.getDouble("f_sf");
            nBalanceTotalCur =resultSet.getDouble("f_sf_cur");
            idCob = resultSet.getInt("id_cob");
            idEnt = resultSet.getInt("id_ent");
            idAcc = resultSet.getString("fid_acc");
            costC = resultSet.getString("fid_cc_n");
            acc_cat[0] = resultSet.getInt("fid_ct_ent");
            acc_cat[1] = resultSet.getInt("fid_tp_ent");
            debit = 0;
            credit = 0;
            if (costC == null) {
                costC = "";
            }
            nBalanceTotalNew = nBalanceTotalCur * mdExchangeRate; 
            if (nBalanceTotal > nBalanceTotalNew){
                credit = SLibUtilities.round(nBalanceTotal - nBalanceTotalNew, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                debit = 0;
            }
            else if (nBalanceTotal < nBalanceTotalNew) {
                credit = 0;
                debit = SLibUtilities.round(nBalanceTotalNew - nBalanceTotal, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
            }
            if (debit !=0 || credit != 0) {
                maRecordEntries.add( createBpsRecordEntry(SYS_MOVE_ACC, ++nSortingPosition, debit, credit, idAcc, costC, bp_cat, acc_cat, 0, 0, idCob, idEnt, 0, 0  ) );
                maRecordEntries.add( createDiffExpRecordEntry(++nSortingPosition, credit, debit, 0, 0, idCob, idEnt, 0, 0) );
            }
        }
        
        acc_cat = null;
        bp_cat = new int [2];
        sSql = getQuery(SYS_MOVE_BPS);
        resultSet = miClient.getSession().getStatement().executeQuery(sSql);
        while (resultSet.next()){
            debit = 0;
            credit = 0;
            
            bp_cat[0] = resultSet.getInt("fid_ct_sys_mov_xxx");
            bp_cat[1] = resultSet.getInt("fid_tp_sys_mov_xxx");
            dpsYear = resultSet.getInt("fid_dps_year_n");
            dpsDoc = resultSet.getInt("fid_dps_doc_n");
            bpb = resultSet.getInt("fid_bpb_n");
            bp = resultSet.getInt("id_bp");
            idAcc = resultSet.getString("fid_acc");
            costC = resultSet.getString("fid_cc_n");
            
            if (costC == null) {
                costC = "";
            }
            
            nBalanceTotal = resultSet.getDouble("f_sf");
            nBalanceTotalCur = resultSet.getDouble("f_sf_cur");

            if (SLibUtilities.compareKeys(bp_cat, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP) || SLibUtilities.compareKeys(bp_cat, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CDR)) {
                nBalanceTotal = (-1) * nBalanceTotal;
                nBalanceTotalCur = (-1) * nBalanceTotalCur;
            }
            nBalanceTotalNew = nBalanceTotalCur * mdExchangeRate;
            
            if (nBalanceTotal > nBalanceTotalNew) {
                if((SLibUtilities.compareKeys(bp_cat, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS) && dpsYear != 0 && dpsDoc != 0) || SLibUtilities.compareKeys(bp_cat, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_DBR) || (SLibUtilities.compareKeys(bp_cat, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP) && dpsYear == 0 && dpsDoc == 0)){
                    credit = SLibUtilities.round(nBalanceTotal - nBalanceTotalNew, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                    debit = 0;
                }
                else {
                    credit = 0;
                    debit = SLibUtilities.round(nBalanceTotal - nBalanceTotalNew, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                }
            }
            else if ( nBalanceTotalNew > nBalanceTotal) {
                if((SLibUtilities.compareKeys(bp_cat, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS) && dpsYear != 0 && dpsDoc != 0) || SLibUtilities.compareKeys(bp_cat, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_DBR) || (SLibUtilities.compareKeys(bp_cat, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP) && dpsYear == 0 && dpsDoc == 0)){
                    credit = 0;
                    debit = SLibUtilities.round(nBalanceTotalNew - nBalanceTotal, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                }
                else {
                    credit = SLibUtilities.round(nBalanceTotalNew - nBalanceTotal, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                    debit = 0;
                }
            }
            System.out.println("SA: " + nBalanceTotal);
            System.out.println("SAD: " + nBalanceTotalCur);
            System.out.println("TC: " + mdExchangeRate);
            System.out.println("SN: " + nBalanceTotalNew);
            System.out.println("d: " + debit);
            System.out.println("c: " + credit);
            
            if (debit != 0 || credit != 0) {
                maRecordEntries.add(createBpsRecordEntry(SYS_MOVE_BPS, ++nSortingPosition, debit, credit, idAcc, costC, bp_cat, acc_cat, bp, bpb, 0, 0, dpsYear, dpsDoc));
                maRecordEntries.add(createDiffExpRecordEntry(++nSortingPosition, credit, debit, bp , bpb, 0, 0, dpsYear, dpsDoc ));
            }
        }

    }

    public void save() throws Exception {
        createBpsRecordEntries();
        moRecord.getDbmsRecordEntries().addAll(maRecordEntries);
        if (moRecord.save(miClient.getSession().getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
           
            System.out.println("-------------------------------------------");
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
        }

    }

    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbAbpEntity extends SDbRegistryUser implements SAbpCategory {

    private static final int CSH_ACC = 101;
    private static final int CSH_ACC_INV = 102;
    private static final int CSH_ACC_IN_EQY = 111;
    private static final int CSH_ACC_IN_EXR = 112;
    private static final int CSH_ACC_IN_ADJ = 113;
    private static final int CSH_ACC_IN_FIN = 114;
    private static final int CSH_ACC_OUT_EQY = 121;
    private static final int CSH_ACC_OUT_EXR = 122;
    private static final int CSH_ACC_OUT_ADJ = 123;
    private static final int CSH_ACC_OUT_FIN = 124;
    private static final int WAH_ACC = 201;
    private static final int PLA_ACC_WH = 301;
    private static final int PLA_ACC_WP = 311;
    private static final int PLA_ACC_PR = 312;
    private static final int PLA_ACC_OH = 313;

    private static final String TXT_CSH_ACC = "ACTIVO CAJA Y BANCOS";
    private static final String TXT_CSH_ACC_INV = "ACTIVO INVERSIONES";
    private static final String TXT_CSH_ACC_IN_EQY = "INGRESO X SOCIOS";
    private static final String TXT_CSH_ACC_IN_EXR = "INGRESO X DIFERENCIAS CAMBIARIAS";
    private static final String TXT_CSH_ACC_IN_ADJ = "INGRESO X AJUSTES";
    private static final String TXT_CSH_ACC_IN_FIN = "INGRESO X FINANCIEROS";
    private static final String TXT_CSH_ACC_OUT_EQY = "EGRESO X SOCIOS";
    private static final String TXT_CSH_ACC_OUT_EXR = "EGRESO X DIFERENCIAS CAMBIARIAS";
    private static final String TXT_CSH_ACC_OUT_ADJ = "EGRESO X AJUSTES";
    private static final String TXT_CSH_ACC_OUT_FIN = "EGRESO X FINANCIEROS";
    private static final String TXT_WAH_ACC = "ACTIVO INVENTARIOS";
    private static final String TXT_PLA_ACC_WH = "ACTIVO INVENTARIOS PT";
    private static final String TXT_PLA_ACC_WP = "GASTOS PRODUCCIÓN EN PROCESO";
    private static final String TXT_PLA_ACC_PR = "GASTOS MANO DE OBRA";
    private static final String TXT_PLA_ACC_OH = "GASTOS INDIRECTOS DE FABRICACIÓN";

    protected int mnPkAbpEntityId;
    protected String msName;
    //protected boolean mbDeleted;
    protected int mnFkEntityCategoryId;
    protected int mnFkCashAccountId;
    protected int mnFkCashAccountInvestmentId;
    protected int mnFkCashAccountInOwnersEquityId;
    protected int mnFkCashAccountInExchangeRateId;
    protected int mnFkCashAccountInAdjustmentId;
    protected int mnFkCashAccountInFinanceId;
    protected int mnFkCashAccountOutOwnersEquityId;
    protected int mnFkCashAccountOutExchangeRateId;
    protected int mnFkCashAccountOutAdjustmentId;
    protected int mnFkCashAccountOutFinanceId;
    protected int mnFkCashCostCenterInExchangeRateId;
    protected int mnFkCashCostCenterInAdjustmentId;
    protected int mnFkCashCostCenterInFinanceId;
    protected int mnFkCashCostCenterOutExchangeRateId;
    protected int mnFkCashCostCenterOutAdjustmentId;
    protected int mnFkCashCostCenterOutFinanceId;
    protected int mnFkCashItemInExchangeRateId_n;
    protected int mnFkCashItemInAdjustmentId_n;
    protected int mnFkCashItemInFinanceId_n;
    protected int mnFkCashItemOutExchangeRateId_n;
    protected int mnFkCashItemOutAdjustmentId_n;
    protected int mnFkCashItemOutFinanceId_n;
    protected int mnFkWarehouseAccountId;
    protected int mnFkPlantAccountWarehouseId;
    protected int mnFkPlantAccountWorkInProgressId;
    protected int mnFkPlantAccountPayrollId;
    protected int mnFkPlantAccountMfgOverheadId;
    protected int mnFkPlantCostCenterWorkInProgressId;
    protected int mnFkPlantCostCenterPayrollId;
    protected int mnFkPlantCostCenterMfgOverheadId;
    protected int mnFkPlantItemWorkInProgressId_n;
    protected int mnFkPlantItemPayrollId_n;
    protected int mnFkPlantItemMfgOverheadId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbAbpEntity() {
        super(SModConsts.FIN_ABP_ENT);
    }

    public void setPkAbpEntityId(int n) { mnPkAbpEntityId = n; }
    public void setName(String s) { msName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkEntityCategoryId(int n) { mnFkEntityCategoryId = n; }
    public void setFkCashAccountId(int n) { mnFkCashAccountId = n; }
    public void setFkCashAccountInvestmentId(int n) { mnFkCashAccountInvestmentId = n; }
    public void setFkCashAccountInOwnersEquityId(int n) { mnFkCashAccountInOwnersEquityId = n; }
    public void setFkCashAccountInExchangeRateId(int n) { mnFkCashAccountInExchangeRateId = n; }
    public void setFkCashAccountInAdjustmentId(int n) { mnFkCashAccountInAdjustmentId = n; }
    public void setFkCashAccountInFinanceId(int n) { mnFkCashAccountInFinanceId = n; }
    public void setFkCashAccountOutOwnersEquityId(int n) { mnFkCashAccountOutOwnersEquityId = n; }
    public void setFkCashAccountOutExchangeRateId(int n) { mnFkCashAccountOutExchangeRateId = n; }
    public void setFkCashAccountOutAdjustmentId(int n) { mnFkCashAccountOutAdjustmentId = n; }
    public void setFkCashAccountOutFinanceId(int n) { mnFkCashAccountOutFinanceId = n; }
    public void setFkCashCostCenterInExchangeRateId(int n) { mnFkCashCostCenterInExchangeRateId = n; }
    public void setFkCashCostCenterInAdjustmentId(int n) { mnFkCashCostCenterInAdjustmentId = n; }
    public void setFkCashCostCenterInFinanceId(int n) { mnFkCashCostCenterInFinanceId = n; }
    public void setFkCashCostCenterOutExchangeRateId(int n) { mnFkCashCostCenterOutExchangeRateId = n; }
    public void setFkCashCostCenterOutAdjustmentId(int n) { mnFkCashCostCenterOutAdjustmentId = n; }
    public void setFkCashCostCenterOutFinanceId(int n) { mnFkCashCostCenterOutFinanceId = n; }
    public void setFkCashItemInExchangeRateId_n(int n) { mnFkCashItemInExchangeRateId_n = n; }
    public void setFkCashItemInAdjustmentId_n(int n) { mnFkCashItemInAdjustmentId_n = n; }
    public void setFkCashItemInFinanceId_n(int n) { mnFkCashItemInFinanceId_n = n; }
    public void setFkCashItemOutExchangeRateId_n(int n) { mnFkCashItemOutExchangeRateId_n = n; }
    public void setFkCashItemOutAdjustmentId_n(int n) { mnFkCashItemOutAdjustmentId_n = n; }
    public void setFkCashItemOutFinanceId_n(int n) { mnFkCashItemOutFinanceId_n = n; }
    public void setFkWarehouseAccountId(int n) { mnFkWarehouseAccountId = n; }
    public void setFkPlantAccountWarehouseId(int n) { mnFkPlantAccountWarehouseId = n; }
    public void setFkPlantAccountWorkInProgressId(int n) { mnFkPlantAccountWorkInProgressId = n; }
    public void setFkPlantAccountPayrollId(int n) { mnFkPlantAccountPayrollId = n; }
    public void setFkPlantAccountMfgOverheadId(int n) { mnFkPlantAccountMfgOverheadId = n; }
    public void setFkPlantCostCenterWorkInProgressId(int n) { mnFkPlantCostCenterWorkInProgressId = n; }
    public void setFkPlantCostCenterPayrollId(int n) { mnFkPlantCostCenterPayrollId = n; }
    public void setFkPlantCostCenterMfgOverheadId(int n) { mnFkPlantCostCenterMfgOverheadId = n; }
    public void setFkPlantItemWorkInProgressId_n(int n) { mnFkPlantItemWorkInProgressId_n = n; }
    public void setFkPlantItemPayrollId_n(int n) { mnFkPlantItemPayrollId_n = n; }
    public void setFkPlantItemMfgOverheadId_n(int n) { mnFkPlantItemMfgOverheadId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkAbpEntityId() { return mnPkAbpEntityId; }
    public String getName() { return msName; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkEntityCategoryId() { return mnFkEntityCategoryId; }
    public int getFkCashAccountId() { return mnFkCashAccountId; }
    public int getFkCashAccountInvestmentId() { return mnFkCashAccountInvestmentId; }
    public int getFkCashAccountInOwnersEquityId() { return mnFkCashAccountInOwnersEquityId; }
    public int getFkCashAccountInExchangeRateId() { return mnFkCashAccountInExchangeRateId; }
    public int getFkCashAccountInAdjustmentId() { return mnFkCashAccountInAdjustmentId; }
    public int getFkCashAccountInFinanceId() { return mnFkCashAccountInFinanceId; }
    public int getFkCashAccountOutOwnersEquityId() { return mnFkCashAccountOutOwnersEquityId; }
    public int getFkCashAccountOutExchangeRateId() { return mnFkCashAccountOutExchangeRateId; }
    public int getFkCashAccountOutAdjustmentId() { return mnFkCashAccountOutAdjustmentId; }
    public int getFkCashAccountOutFinanceId() { return mnFkCashAccountOutFinanceId; }
    public int getFkCashCostCenterInExchangeRateId() { return mnFkCashCostCenterInExchangeRateId; }
    public int getFkCashCostCenterInAdjustmentId() { return mnFkCashCostCenterInAdjustmentId; }
    public int getFkCashCostCenterInFinanceId() { return mnFkCashCostCenterInFinanceId; }
    public int getFkCashCostCenterOutExchangeRateId() { return mnFkCashCostCenterOutExchangeRateId; }
    public int getFkCashCostCenterOutAdjustmentId() { return mnFkCashCostCenterOutAdjustmentId; }
    public int getFkCashCostCenterOutFinanceId() { return mnFkCashCostCenterOutFinanceId; }
    public int getFkCashItemInExchangeRateId_n() { return mnFkCashItemInExchangeRateId_n; }
    public int getFkCashItemInAdjustmentId_n() { return mnFkCashItemInAdjustmentId_n; }
    public int getFkCashItemInFinanceId_n() { return mnFkCashItemInFinanceId_n; }
    public int getFkCashItemOutExchangeRateId_n() { return mnFkCashItemOutExchangeRateId_n; }
    public int getFkCashItemOutAdjustmentId_n() { return mnFkCashItemOutAdjustmentId_n; }
    public int getFkCashItemOutFinanceId_n() { return mnFkCashItemOutFinanceId_n; }
    public int getFkWarehouseAccountId() { return mnFkWarehouseAccountId; }
    public int getFkPlantAccountWarehouseId() { return mnFkPlantAccountWarehouseId; }
    public int getFkPlantAccountWorkInProgressId() { return mnFkPlantAccountWorkInProgressId; }
    public int getFkPlantAccountPayrollId() { return mnFkPlantAccountPayrollId; }
    public int getFkPlantAccountMfgOverheadId() { return mnFkPlantAccountMfgOverheadId; }
    public int getFkPlantCostCenterWorkInProgressId() { return mnFkPlantCostCenterWorkInProgressId; }
    public int getFkPlantCostCenterPayrollId() { return mnFkPlantCostCenterPayrollId; }
    public int getFkPlantCostCenterMfgOverheadId() { return mnFkPlantCostCenterMfgOverheadId; }
    public int getFkPlantItemWorkInProgressId_n() { return mnFkPlantItemWorkInProgressId_n; }
    public int getFkPlantItemPayrollId_n() { return mnFkPlantItemPayrollId_n; }
    public int getFkPlantItemMfgOverheadId_n() { return mnFkPlantItemMfgOverheadId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAbpEntityId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAbpEntityId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkAbpEntityId = 0;
        msName = "";
        mbDeleted = false;
        mnFkEntityCategoryId = 0;
        mnFkCashAccountId = SModSysConsts.FIN_ACC_NA;
        mnFkCashAccountInvestmentId = SModSysConsts.FIN_ACC_NA;
        mnFkCashAccountInOwnersEquityId = SModSysConsts.FIN_ACC_NA;
        mnFkCashAccountInExchangeRateId = SModSysConsts.FIN_ACC_NA;
        mnFkCashAccountInAdjustmentId = SModSysConsts.FIN_ACC_NA;
        mnFkCashAccountInFinanceId = SModSysConsts.FIN_ACC_NA;
        mnFkCashAccountOutOwnersEquityId = SModSysConsts.FIN_ACC_NA;
        mnFkCashAccountOutExchangeRateId = SModSysConsts.FIN_ACC_NA;
        mnFkCashAccountOutAdjustmentId = SModSysConsts.FIN_ACC_NA;
        mnFkCashAccountOutFinanceId = SModSysConsts.FIN_ACC_NA;
        mnFkCashCostCenterInExchangeRateId = SModSysConsts.FIN_CC_NA;
        mnFkCashCostCenterInAdjustmentId = SModSysConsts.FIN_CC_NA;
        mnFkCashCostCenterInFinanceId = SModSysConsts.FIN_CC_NA;
        mnFkCashCostCenterOutExchangeRateId = SModSysConsts.FIN_CC_NA;
        mnFkCashCostCenterOutAdjustmentId = SModSysConsts.FIN_CC_NA;
        mnFkCashCostCenterOutFinanceId = SModSysConsts.FIN_CC_NA;
        mnFkCashItemInExchangeRateId_n = 0;
        mnFkCashItemInAdjustmentId_n = 0;
        mnFkCashItemInFinanceId_n = 0;
        mnFkCashItemOutExchangeRateId_n = 0;
        mnFkCashItemOutAdjustmentId_n = 0;
        mnFkCashItemOutFinanceId_n = 0;
        mnFkWarehouseAccountId = SModSysConsts.FIN_ACC_NA;
        mnFkPlantAccountWarehouseId = SModSysConsts.FIN_ACC_NA;
        mnFkPlantAccountWorkInProgressId = SModSysConsts.FIN_ACC_NA;
        mnFkPlantAccountPayrollId = SModSysConsts.FIN_ACC_NA;
        mnFkPlantAccountMfgOverheadId = SModSysConsts.FIN_ACC_NA;
        mnFkPlantCostCenterWorkInProgressId = SModSysConsts.FIN_CC_NA;
        mnFkPlantCostCenterPayrollId = SModSysConsts.FIN_CC_NA;
        mnFkPlantCostCenterMfgOverheadId = SModSysConsts.FIN_CC_NA;
        mnFkPlantItemWorkInProgressId_n = 0;
        mnFkPlantItemPayrollId_n = 0;
        mnFkPlantItemMfgOverheadId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_abp_ent = " + mnPkAbpEntityId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_abp_ent = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAbpEntityId = 0;

        msSql = "SELECT COALESCE(MAX(id_abp_ent), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAbpEntityId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkAbpEntityId = resultSet.getInt("id_abp_ent");
            msName = resultSet.getString("name");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkEntityCategoryId = resultSet.getInt("fk_ct_ent");
            mnFkCashAccountId = resultSet.getInt("fk_cash_acc");
            mnFkCashAccountInvestmentId = resultSet.getInt("fk_cash_acc_inv");
            mnFkCashAccountInOwnersEquityId = resultSet.getInt("fk_cash_acc_in_eqy");
            mnFkCashAccountInExchangeRateId = resultSet.getInt("fk_cash_acc_in_exc_rate");
            mnFkCashAccountInAdjustmentId = resultSet.getInt("fk_cash_acc_in_adj");
            mnFkCashAccountInFinanceId = resultSet.getInt("fk_cash_acc_in_fin");
            mnFkCashAccountOutOwnersEquityId = resultSet.getInt("fk_cash_acc_out_eqy");
            mnFkCashAccountOutExchangeRateId = resultSet.getInt("fk_cash_acc_out_exc_rate");
            mnFkCashAccountOutAdjustmentId = resultSet.getInt("fk_cash_acc_out_adj");
            mnFkCashAccountOutFinanceId = resultSet.getInt("fk_cash_acc_out_fin");
            mnFkCashCostCenterInExchangeRateId = resultSet.getInt("fk_cash_cc_in_exc_rate");
            mnFkCashCostCenterInAdjustmentId = resultSet.getInt("fk_cash_cc_in_adj");
            mnFkCashCostCenterInFinanceId = resultSet.getInt("fk_cash_cc_in_fin");
            mnFkCashCostCenterOutExchangeRateId = resultSet.getInt("fk_cash_cc_out_exc_rate");
            mnFkCashCostCenterOutAdjustmentId = resultSet.getInt("fk_cash_cc_out_adj");
            mnFkCashCostCenterOutFinanceId = resultSet.getInt("fk_cash_cc_out_fin");
            mnFkCashItemInExchangeRateId_n = resultSet.getInt("fk_cash_item_in_exc_rate_n");
            mnFkCashItemInAdjustmentId_n = resultSet.getInt("fk_cash_item_in_adj_n");
            mnFkCashItemInFinanceId_n = resultSet.getInt("fk_cash_item_in_fin_n");
            mnFkCashItemOutExchangeRateId_n = resultSet.getInt("fk_cash_item_out_exc_rate_n");
            mnFkCashItemOutAdjustmentId_n = resultSet.getInt("fk_cash_item_out_adj_n");
            mnFkCashItemOutFinanceId_n = resultSet.getInt("fk_cash_item_out_fin_n");
            mnFkWarehouseAccountId = resultSet.getInt("fk_wh_acc");
            mnFkPlantAccountWarehouseId = resultSet.getInt("fk_pla_acc_wh");
            mnFkPlantAccountWorkInProgressId = resultSet.getInt("fk_pla_acc_wp");
            mnFkPlantAccountPayrollId = resultSet.getInt("fk_pla_acc_exp_pr");
            mnFkPlantAccountMfgOverheadId = resultSet.getInt("fk_pla_acc_exp_oh");
            mnFkPlantCostCenterWorkInProgressId = resultSet.getInt("fk_pla_cc_wp");
            mnFkPlantCostCenterPayrollId = resultSet.getInt("fk_pla_cc_exp_pr");
            mnFkPlantCostCenterMfgOverheadId = resultSet.getInt("fk_pla_cc_exp_oh");
            mnFkPlantItemWorkInProgressId_n = resultSet.getInt("fk_pla_item_wp_n");
            mnFkPlantItemPayrollId_n = resultSet.getInt("fk_pla_item_exp_pr_n");
            mnFkPlantItemMfgOverheadId_n = resultSet.getInt("fk_pla_item_exp_oh_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkAbpEntityId + ", " +
                    "'" + msName + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkEntityCategoryId + ", " +
                    mnFkCashAccountId + ", " +
                    mnFkCashAccountInvestmentId + ", " +
                    mnFkCashAccountInOwnersEquityId + ", " +
                    mnFkCashAccountInExchangeRateId + ", " +
                    mnFkCashAccountInAdjustmentId + ", " +
                    mnFkCashAccountInFinanceId + ", " +
                    mnFkCashAccountOutOwnersEquityId + ", " +
                    mnFkCashAccountOutExchangeRateId + ", " +
                    mnFkCashAccountOutAdjustmentId + ", " +
                    mnFkCashAccountOutFinanceId + ", " +
                    mnFkCashCostCenterInExchangeRateId + ", " +
                    mnFkCashCostCenterInAdjustmentId + ", " +
                    mnFkCashCostCenterInFinanceId + ", " +
                    mnFkCashCostCenterOutExchangeRateId + ", " +
                    mnFkCashCostCenterOutAdjustmentId + ", " +
                    mnFkCashCostCenterOutFinanceId + ", " +
                    (mnFkCashItemInExchangeRateId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashItemInExchangeRateId_n) + ", " +
                    (mnFkCashItemInAdjustmentId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashItemInAdjustmentId_n) + ", " +
                    (mnFkCashItemInFinanceId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashItemInFinanceId_n) + ", " +
                    (mnFkCashItemOutExchangeRateId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashItemOutExchangeRateId_n) + ", " +
                    (mnFkCashItemOutAdjustmentId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashItemOutAdjustmentId_n) + ", " +
                    (mnFkCashItemOutFinanceId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashItemOutFinanceId_n) + ", " +
                    mnFkWarehouseAccountId + ", " +
                    mnFkPlantAccountWarehouseId + ", " +
                    mnFkPlantAccountWorkInProgressId + ", " +
                    mnFkPlantAccountPayrollId + ", " +
                    mnFkPlantAccountMfgOverheadId + ", " +
                    mnFkPlantCostCenterWorkInProgressId + ", " +
                    mnFkPlantCostCenterPayrollId + ", " +
                    mnFkPlantCostCenterMfgOverheadId + ", " +
                    (mnFkPlantItemWorkInProgressId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkPlantItemWorkInProgressId_n) + ", " +
                    (mnFkPlantItemPayrollId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkPlantItemPayrollId_n) + ", " +
                    (mnFkPlantItemMfgOverheadId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkPlantItemMfgOverheadId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_abp_ent = " + mnPkAbpEntityId + ", " +
                    "name = '" + msName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_ct_ent = " + mnFkEntityCategoryId + ", " +
                    "fk_cash_acc = " + mnFkCashAccountId + ", " +
                    "fk_cash_acc_inv = " + mnFkCashAccountInvestmentId + ", " +
                    "fk_cash_acc_in_eqy = " + mnFkCashAccountInOwnersEquityId + ", " +
                    "fk_cash_acc_in_exc_rate = " + mnFkCashAccountInExchangeRateId + ", " +
                    "fk_cash_acc_in_adj = " + mnFkCashAccountInAdjustmentId + ", " +
                    "fk_cash_acc_in_fin = " + mnFkCashAccountInFinanceId + ", " +
                    "fk_cash_acc_out_eqy = " + mnFkCashAccountOutOwnersEquityId + ", " +
                    "fk_cash_acc_out_exc_rate = " + mnFkCashAccountOutExchangeRateId + ", " +
                    "fk_cash_acc_out_adj = " + mnFkCashAccountOutAdjustmentId + ", " +
                    "fk_cash_acc_out_fin = " + mnFkCashAccountOutFinanceId + ", " +
                    "fk_cash_cc_in_exc_rate = " + mnFkCashCostCenterInExchangeRateId + ", " +
                    "fk_cash_cc_in_adj = " + mnFkCashCostCenterInAdjustmentId + ", " +
                    "fk_cash_cc_in_fin = " + mnFkCashCostCenterInFinanceId + ", " +
                    "fk_cash_cc_out_exc_rate = " + mnFkCashCostCenterOutExchangeRateId + ", " +
                    "fk_cash_cc_out_adj = " + mnFkCashCostCenterOutAdjustmentId + ", " +
                    "fk_cash_cc_out_fin = " + mnFkCashCostCenterOutFinanceId + ", " +
                    "fk_cash_item_in_exc_rate_n = " + (mnFkCashItemInExchangeRateId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashItemInExchangeRateId_n) + ", " +
                    "fk_cash_item_in_adj_n = " + (mnFkCashItemInAdjustmentId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashItemInAdjustmentId_n) + ", " +
                    "fk_cash_item_in_fin_n = " + (mnFkCashItemInFinanceId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashItemInFinanceId_n) + ", " +
                    "fk_cash_item_out_exc_rate_n = " + (mnFkCashItemOutExchangeRateId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashItemOutExchangeRateId_n) + ", " +
                    "fk_cash_item_out_adj_n = " + (mnFkCashItemOutAdjustmentId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashItemOutAdjustmentId_n) + ", " +
                    "fk_cash_item_out_fin_n = " + (mnFkCashItemOutFinanceId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashItemOutFinanceId_n) + ", " +
                    "fk_wh_acc = " + mnFkWarehouseAccountId + ", " +
                    "fk_pla_acc_wh = " + mnFkPlantAccountWarehouseId + ", " +
                    "fk_pla_acc_wp = " + mnFkPlantAccountWorkInProgressId + ", " +
                    "fk_pla_acc_exp_pr = " + mnFkPlantAccountPayrollId + ", " +
                    "fk_pla_acc_exp_oh = " + mnFkPlantAccountMfgOverheadId + ", " +
                    "fk_pla_cc_wp = " + mnFkPlantCostCenterWorkInProgressId + ", " +
                    "fk_pla_cc_exp_pr = " + mnFkPlantCostCenterPayrollId + ", " +
                    "fk_pla_cc_exp_oh = " + mnFkPlantCostCenterMfgOverheadId + ", " +
                    "fk_pla_item_wp_n = " + (mnFkPlantItemWorkInProgressId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkPlantItemWorkInProgressId_n) + ", " +
                    "fk_pla_item_exp_pr_n = " + (mnFkPlantItemPayrollId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkPlantItemPayrollId_n) + ", " +
                    "fk_pla_item_exp_oh_n = " + (mnFkPlantItemMfgOverheadId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkPlantItemMfgOverheadId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbAbpEntity clone() throws CloneNotSupportedException {
        SDbAbpEntity registry = new SDbAbpEntity();

        registry.setPkAbpEntityId(this.getPkAbpEntityId());
        registry.setName(this.getName());
        registry.setDeleted(this.isDeleted());
        registry.setFkEntityCategoryId(this.getFkEntityCategoryId());
        registry.setFkCashAccountId(this.getFkCashAccountId());
        registry.setFkCashAccountInvestmentId(this.getFkCashAccountInvestmentId());
        registry.setFkCashAccountInOwnersEquityId(this.getFkCashAccountInOwnersEquityId());
        registry.setFkCashAccountInExchangeRateId(this.getFkCashAccountInExchangeRateId());
        registry.setFkCashAccountInAdjustmentId(this.getFkCashAccountInAdjustmentId());
        registry.setFkCashAccountInFinanceId(this.getFkCashAccountInFinanceId());
        registry.setFkCashAccountOutOwnersEquityId(this.getFkCashAccountOutOwnersEquityId());
        registry.setFkCashAccountOutExchangeRateId(this.getFkCashAccountOutExchangeRateId());
        registry.setFkCashAccountOutAdjustmentId(this.getFkCashAccountOutAdjustmentId());
        registry.setFkCashAccountOutFinanceId(this.getFkCashAccountOutFinanceId());
        registry.setFkCashCostCenterInExchangeRateId(this.getFkCashCostCenterInExchangeRateId());
        registry.setFkCashCostCenterInAdjustmentId(this.getFkCashCostCenterInAdjustmentId());
        registry.setFkCashCostCenterInFinanceId(this.getFkCashCostCenterInFinanceId());
        registry.setFkCashCostCenterOutExchangeRateId(this.getFkCashCostCenterOutExchangeRateId());
        registry.setFkCashCostCenterOutAdjustmentId(this.getFkCashCostCenterOutAdjustmentId());
        registry.setFkCashCostCenterOutFinanceId(this.getFkCashCostCenterOutFinanceId());
        registry.setFkCashItemInExchangeRateId_n(this.getFkCashItemInExchangeRateId_n());
        registry.setFkCashItemInAdjustmentId_n(this.getFkCashItemInAdjustmentId_n());
        registry.setFkCashItemInFinanceId_n(this.getFkCashItemInFinanceId_n());
        registry.setFkCashItemOutExchangeRateId_n(this.getFkCashItemOutExchangeRateId_n());
        registry.setFkCashItemOutAdjustmentId_n(this.getFkCashItemOutAdjustmentId_n());
        registry.setFkCashItemOutFinanceId_n(this.getFkCashItemOutFinanceId_n());
        registry.setFkWarehouseAccountId(this.getFkWarehouseAccountId());
        registry.setFkPlantAccountWarehouseId(this.getFkPlantAccountWarehouseId());
        registry.setFkPlantAccountWorkInProgressId(this.getFkPlantAccountWorkInProgressId());
        registry.setFkPlantAccountPayrollId(this.getFkPlantAccountPayrollId());
        registry.setFkPlantAccountMfgOverheadId(this.getFkPlantAccountMfgOverheadId());
        registry.setFkPlantCostCenterWorkInProgressId(this.getFkPlantCostCenterWorkInProgressId());
        registry.setFkPlantCostCenterPayrollId(this.getFkPlantCostCenterPayrollId());
        registry.setFkPlantCostCenterMfgOverheadId(this.getFkPlantCostCenterMfgOverheadId());
        registry.setFkPlantItemWorkInProgressId_n(this.getFkPlantItemWorkInProgressId_n());
        registry.setFkPlantItemPayrollId_n(this.getFkPlantItemPayrollId_n());
        registry.setFkPlantItemMfgOverheadId_n(this.getFkPlantItemMfgOverheadId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        return registry;
    }

    @Override
    public HashMap<Integer, SAbpRegistry> getAbpRows(SGuiSession session, int entityCategory) {
        HashMap<Integer, SAbpRegistry> rowsMap = new HashMap<Integer, SAbpRegistry>();

        switch (entityCategory) {
            case SDataConstantsSys.CFGS_CT_ENT_CASH:
                rowsMap.put(CSH_ACC, new SAbpRegistry(CSH_ACC, TXT_CSH_ACC, new int[] { },
                        mnFkCashAccountId,
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountId }, SDbRegistry.FIELD_NAME)));

                rowsMap.put(CSH_ACC_INV, new SAbpRegistry(CSH_ACC_INV, TXT_CSH_ACC_INV, new int[] { },
                        mnFkCashAccountInvestmentId,
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountInvestmentId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountInvestmentId }, SDbRegistry.FIELD_NAME)));

                rowsMap.put(CSH_ACC_IN_EQY, new SAbpRegistry(CSH_ACC_IN_EQY, TXT_CSH_ACC_IN_EQY, new int[] { },
                        mnFkCashAccountInOwnersEquityId,
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountInOwnersEquityId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountInOwnersEquityId }, SDbRegistry.FIELD_NAME)));

                rowsMap.put(CSH_ACC_IN_EXR, new SAbpRegistry(CSH_ACC_IN_EXR, TXT_CSH_ACC_IN_EXR, new int[] { },
                        mnFkCashAccountInExchangeRateId,
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountInExchangeRateId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountInExchangeRateId }, SDbRegistry.FIELD_NAME),
                        mnFkCashCostCenterInExchangeRateId,
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCashCostCenterInExchangeRateId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCashCostCenterInExchangeRateId }, SDbRegistry.FIELD_NAME),
                        mnFkCashItemInExchangeRateId_n,
                        mnFkCashItemInExchangeRateId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkCashItemInExchangeRateId_n }, SDbRegistry.FIELD_CODE),
                        mnFkCashItemInExchangeRateId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkCashItemInExchangeRateId_n }, SDbRegistry.FIELD_NAME)));

                rowsMap.put(CSH_ACC_IN_ADJ, new SAbpRegistry(CSH_ACC_IN_ADJ, TXT_CSH_ACC_IN_ADJ, new int[] { },
                        mnFkCashAccountInAdjustmentId,
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountInAdjustmentId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountInAdjustmentId }, SDbRegistry.FIELD_NAME),
                        mnFkCashCostCenterInAdjustmentId,
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCashCostCenterInAdjustmentId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCashCostCenterInAdjustmentId }, SDbRegistry.FIELD_NAME),
                        mnFkCashItemInAdjustmentId_n,
                        mnFkCashItemInAdjustmentId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkCashItemInAdjustmentId_n }, SDbRegistry.FIELD_CODE),
                        mnFkCashItemInAdjustmentId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkCashItemInAdjustmentId_n }, SDbRegistry.FIELD_NAME)));

                rowsMap.put(CSH_ACC_IN_FIN, new SAbpRegistry(CSH_ACC_IN_FIN, TXT_CSH_ACC_IN_FIN, new int[] { },
                        mnFkCashAccountInFinanceId,
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountInFinanceId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountInFinanceId }, SDbRegistry.FIELD_NAME),
                        mnFkCashCostCenterInFinanceId,
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCashCostCenterInFinanceId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCashCostCenterInFinanceId }, SDbRegistry.FIELD_NAME),
                        mnFkCashItemInFinanceId_n,
                        mnFkCashItemInFinanceId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkCashItemInFinanceId_n }, SDbRegistry.FIELD_CODE),
                        mnFkCashItemInFinanceId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkCashItemInFinanceId_n }, SDbRegistry.FIELD_NAME)));

                rowsMap.put(CSH_ACC_OUT_EQY, new SAbpRegistry(CSH_ACC_OUT_EQY, TXT_CSH_ACC_OUT_EQY, new int[] { },
                        mnFkCashAccountOutOwnersEquityId,
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountOutOwnersEquityId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountOutOwnersEquityId }, SDbRegistry.FIELD_NAME)));

                rowsMap.put(CSH_ACC_OUT_EXR, new SAbpRegistry(CSH_ACC_OUT_EXR, TXT_CSH_ACC_OUT_EXR, new int[] { },
                        mnFkCashAccountOutExchangeRateId,
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountOutExchangeRateId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountOutExchangeRateId }, SDbRegistry.FIELD_NAME),
                        mnFkCashCostCenterOutExchangeRateId,
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCashCostCenterOutExchangeRateId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCashCostCenterOutExchangeRateId }, SDbRegistry.FIELD_NAME),
                        mnFkCashItemOutExchangeRateId_n,
                        mnFkCashItemOutExchangeRateId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkCashItemOutExchangeRateId_n }, SDbRegistry.FIELD_CODE),
                        mnFkCashItemOutExchangeRateId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkCashItemOutExchangeRateId_n }, SDbRegistry.FIELD_NAME)));

                rowsMap.put(CSH_ACC_OUT_ADJ, new SAbpRegistry(CSH_ACC_OUT_ADJ, TXT_CSH_ACC_OUT_ADJ, new int[] { },
                        mnFkCashAccountOutAdjustmentId,
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountOutAdjustmentId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountOutAdjustmentId }, SDbRegistry.FIELD_NAME),
                        mnFkCashCostCenterOutAdjustmentId,
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCashCostCenterOutAdjustmentId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCashCostCenterOutAdjustmentId }, SDbRegistry.FIELD_NAME),
                        mnFkCashItemOutAdjustmentId_n,
                        mnFkCashItemOutAdjustmentId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkCashItemOutAdjustmentId_n }, SDbRegistry.FIELD_CODE),
                        mnFkCashItemOutAdjustmentId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkCashItemOutAdjustmentId_n }, SDbRegistry.FIELD_NAME)));

                rowsMap.put(CSH_ACC_OUT_FIN, new SAbpRegistry(CSH_ACC_OUT_FIN, TXT_CSH_ACC_OUT_FIN, new int[] { },
                        mnFkCashAccountOutFinanceId,
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountOutFinanceId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkCashAccountOutFinanceId }, SDbRegistry.FIELD_NAME),
                        mnFkCashCostCenterOutFinanceId,
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCashCostCenterOutFinanceId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCashCostCenterOutFinanceId }, SDbRegistry.FIELD_NAME),
                        mnFkCashItemOutFinanceId_n,
                        mnFkCashItemOutFinanceId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkCashItemOutFinanceId_n }, SDbRegistry.FIELD_CODE),
                        mnFkCashItemOutFinanceId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkCashItemOutFinanceId_n }, SDbRegistry.FIELD_NAME)));
                break;

            case SDataConstantsSys.CFGS_CT_ENT_WH:
                rowsMap.put(WAH_ACC, new SAbpRegistry(WAH_ACC, TXT_WAH_ACC, new int[] { },
                        mnFkWarehouseAccountId,
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkWarehouseAccountId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkWarehouseAccountId }, SDbRegistry.FIELD_NAME)));
                break;

            case SDataConstantsSys.CFGS_CT_ENT_PLANT:
                rowsMap.put(PLA_ACC_WH, new SAbpRegistry(PLA_ACC_WH, TXT_PLA_ACC_WH, new int[] { },
                        mnFkPlantAccountWarehouseId,
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkPlantAccountWarehouseId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkPlantAccountWarehouseId }, SDbRegistry.FIELD_NAME)));

                rowsMap.put(PLA_ACC_WP, new SAbpRegistry(PLA_ACC_WP, TXT_PLA_ACC_WP, new int[] { },
                        mnFkPlantAccountWorkInProgressId,
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkPlantAccountWorkInProgressId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkPlantAccountWorkInProgressId }, SDbRegistry.FIELD_NAME),
                        mnFkPlantCostCenterWorkInProgressId,
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkPlantCostCenterWorkInProgressId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkPlantCostCenterWorkInProgressId }, SDbRegistry.FIELD_NAME),
                        mnFkPlantItemWorkInProgressId_n,
                        mnFkPlantItemWorkInProgressId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkPlantItemWorkInProgressId_n }, SDbRegistry.FIELD_CODE),
                        mnFkPlantItemWorkInProgressId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkPlantItemWorkInProgressId_n }, SDbRegistry.FIELD_NAME)));

                rowsMap.put(PLA_ACC_PR, new SAbpRegistry(PLA_ACC_PR, TXT_PLA_ACC_PR, new int[] { },
                        mnFkPlantAccountPayrollId,
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkPlantAccountPayrollId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkPlantAccountPayrollId }, SDbRegistry.FIELD_NAME),
                        mnFkPlantCostCenterPayrollId,
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkPlantCostCenterPayrollId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkPlantCostCenterPayrollId }, SDbRegistry.FIELD_NAME),
                        mnFkPlantItemPayrollId_n,
                        mnFkPlantItemPayrollId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkPlantItemPayrollId_n }, SDbRegistry.FIELD_CODE),
                        mnFkPlantItemPayrollId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkPlantItemPayrollId_n }, SDbRegistry.FIELD_NAME)));

                rowsMap.put(PLA_ACC_OH, new SAbpRegistry(PLA_ACC_OH, TXT_PLA_ACC_OH, new int[] { },
                        mnFkPlantAccountMfgOverheadId,
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkPlantAccountMfgOverheadId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkPlantAccountMfgOverheadId }, SDbRegistry.FIELD_NAME),
                        mnFkPlantCostCenterMfgOverheadId,
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkPlantCostCenterMfgOverheadId }, SDbRegistry.FIELD_CODE),
                        (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkPlantCostCenterMfgOverheadId }, SDbRegistry.FIELD_NAME),
                        mnFkPlantItemMfgOverheadId_n,
                        mnFkPlantItemMfgOverheadId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkPlantItemMfgOverheadId_n }, SDbRegistry.FIELD_CODE),
                        mnFkPlantItemMfgOverheadId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkPlantItemMfgOverheadId_n }, SDbRegistry.FIELD_NAME)));
                break;

            default:
        }

        return rowsMap;
    }

    @Override
    public void setAbpRows(int entityCategory, HashMap<Integer, SAbpRegistry> rowsMap) {
        SAbpRegistry row = null;

        switch (entityCategory) {
            case SDataConstantsSys.CFGS_CT_ENT_CASH:
                row = rowsMap.get(CSH_ACC);
                mnFkCashAccountId = row.getAccountId();

                row = rowsMap.get(CSH_ACC_INV);
                mnFkCashAccountInvestmentId = row.getAccountId();

                row = rowsMap.get(CSH_ACC_IN_EQY);
                mnFkCashAccountInOwnersEquityId = row.getAccountId();

                row = rowsMap.get(CSH_ACC_IN_EXR);
                mnFkCashAccountInExchangeRateId = row.getAccountId();
                mnFkCashCostCenterInExchangeRateId = row.getCostCenterId();
                mnFkCashItemInExchangeRateId_n = row.getItemId();

                row = rowsMap.get(CSH_ACC_IN_ADJ);
                mnFkCashAccountInAdjustmentId = row.getAccountId();
                mnFkCashCostCenterInAdjustmentId = row.getCostCenterId();
                mnFkCashItemInAdjustmentId_n = row.getItemId();

                row = rowsMap.get(CSH_ACC_IN_FIN);
                mnFkCashAccountInFinanceId = row.getAccountId();
                mnFkCashCostCenterInFinanceId = row.getCostCenterId();
                mnFkCashItemInFinanceId_n = row.getItemId();

                row = rowsMap.get(CSH_ACC_OUT_EQY);
                mnFkCashAccountOutOwnersEquityId = row.getAccountId();

                row = rowsMap.get(CSH_ACC_OUT_EXR);
                mnFkCashAccountOutExchangeRateId = row.getAccountId();
                mnFkCashCostCenterOutExchangeRateId = row.getCostCenterId();
                mnFkCashItemOutExchangeRateId_n = row.getItemId();

                row = rowsMap.get(CSH_ACC_OUT_ADJ);
                mnFkCashAccountOutAdjustmentId = row.getAccountId();
                mnFkCashCostCenterOutAdjustmentId = row.getCostCenterId();
                mnFkCashItemOutAdjustmentId_n = row.getItemId();

                row = rowsMap.get(CSH_ACC_OUT_FIN);
                mnFkCashAccountOutFinanceId = row.getAccountId();
                mnFkCashCostCenterOutFinanceId = row.getCostCenterId();
                mnFkCashItemOutFinanceId_n = row.getItemId();
                break;

            case SDataConstantsSys.CFGS_CT_ENT_WH:
                row = rowsMap.get(WAH_ACC);
                mnFkWarehouseAccountId = row.getAccountId();
                break;

            case SDataConstantsSys.CFGS_CT_ENT_PLANT:
                row = rowsMap.get(PLA_ACC_WH);
                mnFkPlantAccountWarehouseId = row.getAccountId();

                row = rowsMap.get(PLA_ACC_WP);
                mnFkPlantAccountWorkInProgressId = row.getAccountId();
                mnFkPlantCostCenterWorkInProgressId = row.getAccountId();
                mnFkPlantItemWorkInProgressId_n = row.getAccountId();

                row = rowsMap.get(PLA_ACC_PR);
                mnFkPlantAccountPayrollId = row.getAccountId();
                mnFkPlantCostCenterPayrollId = row.getAccountId();
                mnFkPlantItemPayrollId_n = row.getAccountId();

                row = rowsMap.get(PLA_ACC_OH);
                mnFkPlantAccountMfgOverheadId = row.getAccountId();
                mnFkPlantCostCenterMfgOverheadId = row.getAccountId();
                mnFkPlantItemMfgOverheadId_n = row.getAccountId();
                break;

            default:
        }
    }
}

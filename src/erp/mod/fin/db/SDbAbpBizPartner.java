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
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbAbpBizPartner extends SDbRegistryUser implements SAbpCategory {

    private static final int ACC_BAL = 101;
    private static final int ACC_ADV = 102;
    private static final int ACC_DEC_BAL_EXR = 111;
    private static final int ACC_DEC_BAL_ADJ = 112;
    private static final int ACC_DEC_ADV_EXR = 115;
    private static final int ACC_DEC_ADV_ADJ = 116;
    private static final int ACC_INC_BAL_EXR = 121;
    private static final int ACC_INC_BAL_ADJ = 122;
    private static final int ACC_INC_ADV_EXR = 125;
    private static final int ACC_INC_ADV_ADJ = 126;

    private static final String TXT_ACC_BAL = "SALDOS";
    private static final String TXT_ACC_ADV = "ANTICIPOS";
    private static final String TXT_ACC_DEC_BAL_EXR = "DECREMENTO SALDO X DIFERENCIAS CAMBIARIAS";
    private static final String TXT_ACC_DEC_BAL_ADJ = "DECREMENTO SALDO X AJUSTES";
    private static final String TXT_ACC_DEC_ADV_EXR = "DECREMENTO ANTICIPO X DIFERENCIAS CAMBIARIAS";
    private static final String TXT_ACC_DEC_ADV_ADJ = "DECREMENTO ANTICIPO X AJUSTES";
    private static final String TXT_ACC_INC_BAL_EXR = "INCREMENTO SALDO X DIFERENCIAS CAMBIARIAS";
    private static final String TXT_ACC_INC_BAL_ADJ = "INCREMENTO SALDO X AJUSTES";
    private static final String TXT_ACC_INC_ADV_EXR = "INCREMENTO ANTICIPO X DIFERENCIAS CAMBIARIAS";
    private static final String TXT_ACC_INC_ADV_ADJ = "INCREMENTO ANTICIPO X AJUSTES";

    protected int mnPkAbpBizPartnerId;
    protected String msName;
    //protected boolean mbDeleted;
    protected int mnFkBizPartnerCategoryId;
    protected int mnFkAccountBalanceId;
    protected int mnFkAccountPaymentAdvanceId;
    protected int mnFkAccountDecBalanceExchangeRateId;
    protected int mnFkAccountDecBalanceAdjustmentId;
    protected int mnFkAccountDecPaymentAdvanceExchangeRateId;
    protected int mnFkAccountDecPaymentAdvanceAdjustmentId;
    protected int mnFkAccountIncBalanceExchangeRateId;
    protected int mnFkAccountIncBalanceAdjustmentId;
    protected int mnFkAccountIncPaymentAdvanceExchangeRateId;
    protected int mnFkAccountIncPaymentAdvanceAdjustmentId;
    protected int mnFkCostCenterDecBalanceExchangeRateId;
    protected int mnFkCostCenterDecBalanceAdjustmentId;
    protected int mnFkCostCenterDecPaymentAdvanceExchangeRateId;
    protected int mnFkCostCenterDecPaymentAdvanceAdjustmentId;
    protected int mnFkCostCenterIncBalanceExchangeRateId;
    protected int mnFkCostCenterIncBalanceAdjustmentId;
    protected int mnFkCostCenterIncPaymentAdvanceExchangeRateId;
    protected int mnFkCostCenterIncPaymentAdvanceAdjustmentId;
    protected int mnFkItemDecBalanceExchangeRateId_n;
    protected int mnFkItemDecBalanceAdjustmentId_n;
    protected int mnFkItemDecPaymentAdvanceExchangeRateId_n;
    protected int mnFkItemDecPaymentAdvanceAdjustmentId_n;
    protected int mnFkItemIncBalanceExchangeRateId_n;
    protected int mnFkItemIncBalanceAdjustmentId_n;
    protected int mnFkItemIncPaymentAdvanceExchangeRateId_n;
    protected int mnFkItemIncPaymentAdvanceAdjustmentId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbAbpBizPartner() {
        super(SModConsts.FIN_ABP_BP);
    }

    public void setPkAbpBizPartnerId(int n) { mnPkAbpBizPartnerId = n; }
    public void setName(String s) { msName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkBizPartnerCategoryId(int n) { mnFkBizPartnerCategoryId = n; }
    public void setFkAccountBalanceId(int n) { mnFkAccountBalanceId = n; }
    public void setFkAccountPaymentAdvanceId(int n) { mnFkAccountPaymentAdvanceId = n; }
    public void setFkAccountDecBalanceExchangeRateId(int n) { mnFkAccountDecBalanceExchangeRateId = n; }
    public void setFkAccountDecBalanceAdjustmentId(int n) { mnFkAccountDecBalanceAdjustmentId = n; }
    public void setFkAccountDecPaymentAdvanceExchangeRateId(int n) { mnFkAccountDecPaymentAdvanceExchangeRateId = n; }
    public void setFkAccountDecPaymentAdvanceAdjustmentId(int n) { mnFkAccountDecPaymentAdvanceAdjustmentId = n; }
    public void setFkAccountIncBalanceExchangeRateId(int n) { mnFkAccountIncBalanceExchangeRateId = n; }
    public void setFkAccountIncBalanceAdjustmentId(int n) { mnFkAccountIncBalanceAdjustmentId = n; }
    public void setFkAccountIncPaymentAdvanceExchangeRateId(int n) { mnFkAccountIncPaymentAdvanceExchangeRateId = n; }
    public void setFkAccountIncPaymentAdvanceAdjustmentId(int n) { mnFkAccountIncPaymentAdvanceAdjustmentId = n; }
    public void setFkCostCenterDecBalanceExchangeRateId(int n) { mnFkCostCenterDecBalanceExchangeRateId = n; }
    public void setFkCostCenterDecBalanceAdjustmentId(int n) { mnFkCostCenterDecBalanceAdjustmentId = n; }
    public void setFkCostCenterDecPaymentAdvanceExchangeRateId(int n) { mnFkCostCenterDecPaymentAdvanceExchangeRateId = n; }
    public void setFkCostCenterDecPaymentAdvanceAdjustmentId(int n) { mnFkCostCenterDecPaymentAdvanceAdjustmentId = n; }
    public void setFkCostCenterIncBalanceExchangeRateId(int n) { mnFkCostCenterIncBalanceExchangeRateId = n; }
    public void setFkCostCenterIncBalanceAdjustmentId(int n) { mnFkCostCenterIncBalanceAdjustmentId = n; }
    public void setFkCostCenterIncPaymentAdvanceExchangeRateId(int n) { mnFkCostCenterIncPaymentAdvanceExchangeRateId = n; }
    public void setFkCostCenterIncPaymentAdvanceAdjustmentId(int n) { mnFkCostCenterIncPaymentAdvanceAdjustmentId = n; }
    public void setFkItemDecBalanceExchangeRateId_n(int n) { mnFkItemDecBalanceExchangeRateId_n = n; }
    public void setFkItemDecBalanceAdjustmentId_n(int n) { mnFkItemDecBalanceAdjustmentId_n = n; }
    public void setFkItemDecPaymentAdvanceExchangeRateId_n(int n) { mnFkItemDecPaymentAdvanceExchangeRateId_n = n; }
    public void setFkItemDecPaymentAdvanceAdjustmentId_n(int n) { mnFkItemDecPaymentAdvanceAdjustmentId_n = n; }
    public void setFkItemIncBalanceExchangeRateId_n(int n) { mnFkItemIncBalanceExchangeRateId_n = n; }
    public void setFkItemIncBalanceAdjustmentId_n(int n) { mnFkItemIncBalanceAdjustmentId_n = n; }
    public void setFkItemIncPaymentAdvanceExchangeRateId_n(int n) { mnFkItemIncPaymentAdvanceExchangeRateId_n = n; }
    public void setFkItemIncPaymentAdvanceAdjustmentId_n(int n) { mnFkItemIncPaymentAdvanceAdjustmentId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkAbpBizPartnerId() { return mnPkAbpBizPartnerId; }
    public String getName() { return msName; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkBizPartnerCategoryId() { return mnFkBizPartnerCategoryId; }
    public int getFkAccountBalanceId() { return mnFkAccountBalanceId; }
    public int getFkAccountPaymentAdvanceId() { return mnFkAccountPaymentAdvanceId; }
    public int getFkAccountDecBalanceExchangeRateId() { return mnFkAccountDecBalanceExchangeRateId; }
    public int getFkAccountDecBalanceAdjustmentId() { return mnFkAccountDecBalanceAdjustmentId; }
    public int getFkAccountDecPaymentAdvanceExchangeRateId() { return mnFkAccountDecPaymentAdvanceExchangeRateId; }
    public int getFkAccountDecPaymentAdvanceAdjustmentId() { return mnFkAccountDecPaymentAdvanceAdjustmentId; }
    public int getFkAccountIncBalanceExchangeRateId() { return mnFkAccountIncBalanceExchangeRateId; }
    public int getFkAccountIncBalanceAdjustmentId() { return mnFkAccountIncBalanceAdjustmentId; }
    public int getFkAccountIncPaymentAdvanceExchangeRateId() { return mnFkAccountIncPaymentAdvanceExchangeRateId; }
    public int getFkAccountIncPaymentAdvanceAdjustmentId() { return mnFkAccountIncPaymentAdvanceAdjustmentId; }
    public int getFkCostCenterDecBalanceExchangeRateId() { return mnFkCostCenterDecBalanceExchangeRateId; }
    public int getFkCostCenterDecBalanceAdjustmentId() { return mnFkCostCenterDecBalanceAdjustmentId; }
    public int getFkCostCenterDecPaymentAdvanceExchangeRateId() { return mnFkCostCenterDecPaymentAdvanceExchangeRateId; }
    public int getFkCostCenterDecPaymentAdvanceAdjustmentId() { return mnFkCostCenterDecPaymentAdvanceAdjustmentId; }
    public int getFkCostCenterIncBalanceExchangeRateId() { return mnFkCostCenterIncBalanceExchangeRateId; }
    public int getFkCostCenterIncBalanceAdjustmentId() { return mnFkCostCenterIncBalanceAdjustmentId; }
    public int getFkCostCenterIncPaymentAdvanceExchangeRateId() { return mnFkCostCenterIncPaymentAdvanceExchangeRateId; }
    public int getFkCostCenterIncPaymentAdvanceAdjustmentId() { return mnFkCostCenterIncPaymentAdvanceAdjustmentId; }
    public int getFkItemDecBalanceExchangeRateId_n() { return mnFkItemDecBalanceExchangeRateId_n; }
    public int getFkItemDecBalanceAdjustmentId_n() { return mnFkItemDecBalanceAdjustmentId_n; }
    public int getFkItemDecPaymentAdvanceExchangeRateId_n() { return mnFkItemDecPaymentAdvanceExchangeRateId_n; }
    public int getFkItemDecPaymentAdvanceAdjustmentId_n() { return mnFkItemDecPaymentAdvanceAdjustmentId_n; }
    public int getFkItemIncBalanceExchangeRateId_n() { return mnFkItemIncBalanceExchangeRateId_n; }
    public int getFkItemIncBalanceAdjustmentId_n() { return mnFkItemIncBalanceAdjustmentId_n; }
    public int getFkItemIncPaymentAdvanceExchangeRateId_n() { return mnFkItemIncPaymentAdvanceExchangeRateId_n; }
    public int getFkItemIncPaymentAdvanceAdjustmentId_n() { return mnFkItemIncPaymentAdvanceAdjustmentId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAbpBizPartnerId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAbpBizPartnerId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkAbpBizPartnerId = 0;
        msName = "";
        mbDeleted = false;
        mnFkBizPartnerCategoryId = 0;
        mnFkAccountBalanceId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountPaymentAdvanceId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountDecBalanceExchangeRateId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountDecBalanceAdjustmentId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountDecPaymentAdvanceExchangeRateId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountDecPaymentAdvanceAdjustmentId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountIncBalanceExchangeRateId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountIncBalanceAdjustmentId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountIncPaymentAdvanceExchangeRateId = SModSysConsts.FIN_ACC_NA;
        mnFkAccountIncPaymentAdvanceAdjustmentId = SModSysConsts.FIN_ACC_NA;
        mnFkCostCenterDecBalanceExchangeRateId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterDecBalanceAdjustmentId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterDecPaymentAdvanceExchangeRateId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterDecPaymentAdvanceAdjustmentId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterIncBalanceExchangeRateId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterIncBalanceAdjustmentId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterIncPaymentAdvanceExchangeRateId = SModSysConsts.FIN_CC_NA;
        mnFkCostCenterIncPaymentAdvanceAdjustmentId = SModSysConsts.FIN_CC_NA;
        mnFkItemDecBalanceExchangeRateId_n = 0;
        mnFkItemDecBalanceAdjustmentId_n = 0;
        mnFkItemDecPaymentAdvanceExchangeRateId_n = 0;
        mnFkItemDecPaymentAdvanceAdjustmentId_n = 0;
        mnFkItemIncBalanceExchangeRateId_n = 0;
        mnFkItemIncBalanceAdjustmentId_n = 0;
        mnFkItemIncPaymentAdvanceExchangeRateId_n = 0;
        mnFkItemIncPaymentAdvanceAdjustmentId_n = 0;
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
        return "WHERE id_abp_bp = " + mnPkAbpBizPartnerId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_abp_bp = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAbpBizPartnerId = 0;

        msSql = "SELECT COALESCE(MAX(id_abp_bp), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAbpBizPartnerId = resultSet.getInt(1);
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
            mnPkAbpBizPartnerId = resultSet.getInt("id_abp_bp");
            msName = resultSet.getString("name");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkBizPartnerCategoryId = resultSet.getInt("fk_ct_bp");
            mnFkAccountBalanceId = resultSet.getInt("fk_acc_bal");
            mnFkAccountPaymentAdvanceId = resultSet.getInt("fk_acc_adv");
            mnFkAccountDecBalanceExchangeRateId = resultSet.getInt("fk_acc_dec_bal_exc_rate");
            mnFkAccountDecBalanceAdjustmentId = resultSet.getInt("fk_acc_dec_bal_adj");
            mnFkAccountDecPaymentAdvanceExchangeRateId = resultSet.getInt("fk_acc_dec_adv_exc_rate");
            mnFkAccountDecPaymentAdvanceAdjustmentId = resultSet.getInt("fk_acc_dec_adv_adj");
            mnFkAccountIncBalanceExchangeRateId = resultSet.getInt("fk_acc_inc_bal_exc_rate");
            mnFkAccountIncBalanceAdjustmentId = resultSet.getInt("fk_acc_inc_bal_adj");
            mnFkAccountIncPaymentAdvanceExchangeRateId = resultSet.getInt("fk_acc_inc_adv_exc_rate");
            mnFkAccountIncPaymentAdvanceAdjustmentId = resultSet.getInt("fk_acc_inc_adv_adj");
            mnFkCostCenterDecBalanceExchangeRateId = resultSet.getInt("fk_cc_dec_bal_exc_rate");
            mnFkCostCenterDecBalanceAdjustmentId = resultSet.getInt("fk_cc_dec_bal_adj");
            mnFkCostCenterDecPaymentAdvanceExchangeRateId = resultSet.getInt("fk_cc_dec_adv_exc_rate");
            mnFkCostCenterDecPaymentAdvanceAdjustmentId = resultSet.getInt("fk_cc_dec_adv_adj");
            mnFkCostCenterIncBalanceExchangeRateId = resultSet.getInt("fk_cc_inc_bal_exc_rate");
            mnFkCostCenterIncBalanceAdjustmentId = resultSet.getInt("fk_cc_inc_bal_adj");
            mnFkCostCenterIncPaymentAdvanceExchangeRateId = resultSet.getInt("fk_cc_inc_adv_exc_rate");
            mnFkCostCenterIncPaymentAdvanceAdjustmentId = resultSet.getInt("fk_cc_inc_adv_adj");
            mnFkItemDecBalanceExchangeRateId_n = resultSet.getInt("fk_item_dec_bal_exc_rate_n");
            mnFkItemDecBalanceAdjustmentId_n = resultSet.getInt("fk_item_dec_bal_out_adj_n");
            mnFkItemDecPaymentAdvanceExchangeRateId_n = resultSet.getInt("fk_item_dec_adv_exc_rate_n");
            mnFkItemDecPaymentAdvanceAdjustmentId_n = resultSet.getInt("fk_item_dec_adv_out_adj_n");
            mnFkItemIncBalanceExchangeRateId_n = resultSet.getInt("fk_item_inc_bal_exc_rate_n");
            mnFkItemIncBalanceAdjustmentId_n = resultSet.getInt("fk_item_inc_bal_adj_n");
            mnFkItemIncPaymentAdvanceExchangeRateId_n = resultSet.getInt("fk_item_inc_adv_exc_rate_n");
            mnFkItemIncPaymentAdvanceAdjustmentId_n = resultSet.getInt("fk_item_inc_adv_adj_n");
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
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkAbpBizPartnerId + ", " +
                    "'" + msName + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkBizPartnerCategoryId + ", " +
                    mnFkAccountBalanceId + ", " +
                    mnFkAccountPaymentAdvanceId + ", " +
                    mnFkAccountDecBalanceExchangeRateId + ", " +
                    mnFkAccountDecBalanceAdjustmentId + ", " +
                    mnFkAccountDecPaymentAdvanceExchangeRateId + ", " +
                    mnFkAccountDecPaymentAdvanceAdjustmentId + ", " +
                    mnFkAccountIncBalanceExchangeRateId + ", " +
                    mnFkAccountIncBalanceAdjustmentId + ", " +
                    mnFkAccountIncPaymentAdvanceExchangeRateId + ", " +
                    mnFkAccountIncPaymentAdvanceAdjustmentId + ", " +
                    mnFkCostCenterDecBalanceExchangeRateId + ", " +
                    mnFkCostCenterDecBalanceAdjustmentId + ", " +
                    mnFkCostCenterDecPaymentAdvanceExchangeRateId + ", " +
                    mnFkCostCenterDecPaymentAdvanceAdjustmentId + ", " +
                    mnFkCostCenterIncBalanceExchangeRateId + ", " +
                    mnFkCostCenterIncBalanceAdjustmentId + ", " +
                    mnFkCostCenterIncPaymentAdvanceExchangeRateId + ", " +
                    mnFkCostCenterIncPaymentAdvanceAdjustmentId + ", " +
                    (mnFkItemDecBalanceExchangeRateId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemDecBalanceExchangeRateId_n) + ", " +
                    (mnFkItemDecBalanceAdjustmentId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemDecBalanceAdjustmentId_n) + ", " +
                    (mnFkItemDecPaymentAdvanceExchangeRateId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemDecPaymentAdvanceExchangeRateId_n) + ", " +
                    (mnFkItemDecPaymentAdvanceAdjustmentId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemDecPaymentAdvanceAdjustmentId_n) + ", " +
                    (mnFkItemIncBalanceExchangeRateId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemIncBalanceExchangeRateId_n) + ", " +
                    (mnFkItemIncBalanceAdjustmentId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemIncBalanceAdjustmentId_n) + ", " +
                    (mnFkItemIncPaymentAdvanceExchangeRateId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemIncPaymentAdvanceExchangeRateId_n) + ", " +
                    (mnFkItemIncPaymentAdvanceAdjustmentId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemIncPaymentAdvanceAdjustmentId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_abp_bp = " + mnPkAbpBizPartnerId + ", " +
                    */
                    "name = '" + msName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_ct_bp = " + mnFkBizPartnerCategoryId + ", " +
                    "fk_acc_bal = " + mnFkAccountBalanceId + ", " +
                    "fk_acc_adv = " + mnFkAccountPaymentAdvanceId + ", " +
                    "fk_acc_dec_bal_exc_rate = " + mnFkAccountDecBalanceExchangeRateId + ", " +
                    "fk_acc_dec_bal_adj = " + mnFkAccountDecBalanceAdjustmentId + ", " +
                    "fk_acc_dec_adv_exc_rate = " + mnFkAccountDecPaymentAdvanceExchangeRateId + ", " +
                    "fk_acc_dec_adv_adj = " + mnFkAccountDecPaymentAdvanceAdjustmentId + ", " +
                    "fk_acc_inc_bal_exc_rate = " + mnFkAccountIncBalanceExchangeRateId + ", " +
                    "fk_acc_inc_bal_adj = " + mnFkAccountIncBalanceAdjustmentId + ", " +
                    "fk_acc_inc_adv_exc_rate = " + mnFkAccountIncPaymentAdvanceExchangeRateId + ", " +
                    "fk_acc_inc_adv_adj = " + mnFkAccountIncPaymentAdvanceAdjustmentId + ", " +
                    "fk_cc_dec_bal_exc_rate = " + mnFkCostCenterDecBalanceExchangeRateId + ", " +
                    "fk_cc_dec_bal_adj = " + mnFkCostCenterDecBalanceAdjustmentId + ", " +
                    "fk_cc_dec_adv_exc_rate = " + mnFkCostCenterDecPaymentAdvanceExchangeRateId + ", " +
                    "fk_cc_dec_adv_adj = " + mnFkCostCenterDecPaymentAdvanceAdjustmentId + ", " +
                    "fk_cc_inc_bal_exc_rate = " + mnFkCostCenterIncBalanceExchangeRateId + ", " +
                    "fk_cc_inc_bal_adj = " + mnFkCostCenterIncBalanceAdjustmentId + ", " +
                    "fk_cc_inc_adv_exc_rate = " + mnFkCostCenterIncPaymentAdvanceExchangeRateId + ", " +
                    "fk_cc_inc_adv_adj = " + mnFkCostCenterIncPaymentAdvanceAdjustmentId + ", " +
                    "fk_item_dec_bal_exc_rate_n = " + (mnFkItemDecBalanceExchangeRateId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemDecBalanceExchangeRateId_n) + ", " +
                    "fk_item_dec_bal_out_adj_n = " + (mnFkItemDecBalanceAdjustmentId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemDecBalanceAdjustmentId_n) + ", " +
                    "fk_item_dec_adv_exc_rate_n = " + (mnFkItemDecPaymentAdvanceExchangeRateId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemDecPaymentAdvanceExchangeRateId_n) + ", " +
                    "fk_item_dec_adv_out_adj_n = " + (mnFkItemDecPaymentAdvanceAdjustmentId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemDecPaymentAdvanceAdjustmentId_n) + ", " +
                    "fk_item_inc_bal_exc_rate_n = " + (mnFkItemIncBalanceExchangeRateId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemIncBalanceExchangeRateId_n) + ", " +
                    "fk_item_inc_bal_adj_n = " + (mnFkItemIncBalanceAdjustmentId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemIncBalanceAdjustmentId_n) + ", " +
                    "fk_item_inc_adv_exc_rate_n = " + (mnFkItemIncPaymentAdvanceExchangeRateId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemIncPaymentAdvanceExchangeRateId_n) + ", " +
                    "fk_item_inc_adv_adj_n = " + (mnFkItemIncPaymentAdvanceAdjustmentId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemIncPaymentAdvanceAdjustmentId_n) + ", " +
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
    public SDbAbpBizPartner clone() throws CloneNotSupportedException {
        SDbAbpBizPartner registry = new SDbAbpBizPartner();

        registry.setPkAbpBizPartnerId(this.getPkAbpBizPartnerId());
        registry.setName(this.getName());
        registry.setDeleted(this.isDeleted());
        registry.setFkBizPartnerCategoryId(this.getFkBizPartnerCategoryId());
        registry.setFkAccountBalanceId(this.getFkAccountBalanceId());
        registry.setFkAccountPaymentAdvanceId(this.getFkAccountPaymentAdvanceId());
        registry.setFkAccountDecBalanceExchangeRateId(this.getFkAccountDecBalanceExchangeRateId());
        registry.setFkAccountDecBalanceAdjustmentId(this.getFkAccountDecBalanceAdjustmentId());
        registry.setFkAccountDecPaymentAdvanceExchangeRateId(this.getFkAccountDecPaymentAdvanceExchangeRateId());
        registry.setFkAccountDecPaymentAdvanceAdjustmentId(this.getFkAccountDecPaymentAdvanceAdjustmentId());
        registry.setFkAccountIncBalanceExchangeRateId(this.getFkAccountIncBalanceExchangeRateId());
        registry.setFkAccountIncBalanceAdjustmentId(this.getFkAccountIncBalanceAdjustmentId());
        registry.setFkAccountIncPaymentAdvanceExchangeRateId(this.getFkAccountIncPaymentAdvanceExchangeRateId());
        registry.setFkAccountIncPaymentAdvanceAdjustmentId(this.getFkAccountIncPaymentAdvanceAdjustmentId());
        registry.setFkCostCenterDecBalanceExchangeRateId(this.getFkCostCenterDecBalanceExchangeRateId());
        registry.setFkCostCenterDecBalanceAdjustmentId(this.getFkCostCenterDecBalanceAdjustmentId());
        registry.setFkCostCenterDecPaymentAdvanceExchangeRateId(this.getFkCostCenterDecPaymentAdvanceExchangeRateId());
        registry.setFkCostCenterDecPaymentAdvanceAdjustmentId(this.getFkCostCenterDecPaymentAdvanceAdjustmentId());
        registry.setFkCostCenterIncBalanceExchangeRateId(this.getFkCostCenterIncBalanceExchangeRateId());
        registry.setFkCostCenterIncBalanceAdjustmentId(this.getFkCostCenterIncBalanceAdjustmentId());
        registry.setFkCostCenterIncPaymentAdvanceExchangeRateId(this.getFkCostCenterIncPaymentAdvanceExchangeRateId());
        registry.setFkCostCenterIncPaymentAdvanceAdjustmentId(this.getFkCostCenterIncPaymentAdvanceAdjustmentId());
        registry.setFkItemDecBalanceExchangeRateId_n(this.getFkItemDecBalanceExchangeRateId_n());
        registry.setFkItemDecBalanceAdjustmentId_n(this.getFkItemDecBalanceAdjustmentId_n());
        registry.setFkItemDecPaymentAdvanceExchangeRateId_n(this.getFkItemDecPaymentAdvanceExchangeRateId_n());
        registry.setFkItemDecPaymentAdvanceAdjustmentId_n(this.getFkItemDecPaymentAdvanceAdjustmentId_n());
        registry.setFkItemIncBalanceExchangeRateId_n(this.getFkItemIncBalanceExchangeRateId_n());
        registry.setFkItemIncBalanceAdjustmentId_n(this.getFkItemIncBalanceAdjustmentId_n());
        registry.setFkItemIncPaymentAdvanceExchangeRateId_n(this.getFkItemIncPaymentAdvanceExchangeRateId_n());
        registry.setFkItemIncPaymentAdvanceAdjustmentId_n(this.getFkItemIncPaymentAdvanceAdjustmentId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public HashMap<Integer, SAbpRegistry> getAbpRows(SGuiSession session, int bizPartnerCategory) {
        HashMap<Integer, SAbpRegistry> rowsMap = new HashMap<Integer, SAbpRegistry>();


        rowsMap.put(ACC_BAL, new SAbpRegistry(ACC_BAL, TXT_ACC_BAL, new int[] { },
                mnFkAccountBalanceId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountBalanceId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountBalanceId }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_DEC_BAL_EXR, new SAbpRegistry(ACC_DEC_BAL_EXR, TXT_ACC_DEC_BAL_EXR, new int[] { },
                mnFkAccountDecBalanceExchangeRateId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountDecBalanceExchangeRateId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountDecBalanceExchangeRateId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterDecBalanceExchangeRateId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterDecBalanceExchangeRateId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterDecBalanceExchangeRateId }, SDbRegistry.FIELD_NAME),
                mnFkItemDecBalanceExchangeRateId_n,
                mnFkItemDecBalanceExchangeRateId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkItemDecBalanceExchangeRateId_n }, SDbRegistry.FIELD_CODE),
                mnFkItemDecBalanceExchangeRateId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkItemDecBalanceExchangeRateId_n }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_DEC_BAL_ADJ, new SAbpRegistry(ACC_DEC_BAL_ADJ, TXT_ACC_DEC_BAL_ADJ, new int[] { },
                mnFkAccountDecBalanceAdjustmentId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountDecBalanceAdjustmentId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountDecBalanceAdjustmentId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterDecBalanceAdjustmentId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterDecBalanceAdjustmentId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterDecBalanceAdjustmentId }, SDbRegistry.FIELD_NAME),
                mnFkItemDecBalanceAdjustmentId_n,
                mnFkItemDecBalanceAdjustmentId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkItemDecBalanceAdjustmentId_n }, SDbRegistry.FIELD_CODE),
                mnFkItemDecBalanceAdjustmentId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkItemDecBalanceAdjustmentId_n }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_INC_BAL_EXR, new SAbpRegistry(ACC_INC_BAL_EXR, TXT_ACC_INC_BAL_EXR, new int[] { },
                mnFkAccountIncBalanceExchangeRateId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountIncBalanceExchangeRateId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountIncBalanceExchangeRateId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterIncBalanceExchangeRateId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterIncBalanceExchangeRateId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterIncBalanceExchangeRateId }, SDbRegistry.FIELD_NAME),
                mnFkItemIncBalanceExchangeRateId_n,
                mnFkItemIncBalanceExchangeRateId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkItemIncBalanceExchangeRateId_n }, SDbRegistry.FIELD_CODE),
                mnFkItemIncBalanceExchangeRateId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkItemIncBalanceExchangeRateId_n }, SDbRegistry.FIELD_NAME)));

        rowsMap.put(ACC_INC_BAL_ADJ, new SAbpRegistry(ACC_INC_BAL_ADJ, TXT_ACC_INC_BAL_ADJ, new int[] { },
                mnFkAccountIncBalanceAdjustmentId,
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountIncBalanceAdjustmentId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountIncBalanceAdjustmentId }, SDbRegistry.FIELD_NAME),
                mnFkCostCenterIncBalanceAdjustmentId,
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterIncBalanceAdjustmentId }, SDbRegistry.FIELD_CODE),
                (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterIncBalanceAdjustmentId }, SDbRegistry.FIELD_NAME),
                mnFkItemIncBalanceAdjustmentId_n,
                mnFkItemIncBalanceAdjustmentId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkItemIncBalanceAdjustmentId_n }, SDbRegistry.FIELD_CODE),
                mnFkItemIncBalanceAdjustmentId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkItemIncBalanceAdjustmentId_n }, SDbRegistry.FIELD_NAME)));

        if (SLibUtils.belongsTo(bizPartnerCategory, new int[] { SDataConstantsSys.BPSS_CT_BP_SUP, SDataConstantsSys.BPSS_CT_BP_CUS })) {
            rowsMap.put(ACC_ADV, new SAbpRegistry(ACC_ADV, TXT_ACC_ADV, new int[] { },
                    mnFkAccountPaymentAdvanceId,
                    (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPaymentAdvanceId }, SDbRegistry.FIELD_CODE),
                    (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountPaymentAdvanceId }, SDbRegistry.FIELD_NAME)));

            rowsMap.put(ACC_DEC_ADV_EXR, new SAbpRegistry(ACC_DEC_ADV_EXR, TXT_ACC_DEC_ADV_EXR, new int[] { },
                    mnFkAccountDecPaymentAdvanceExchangeRateId,
                    (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountDecPaymentAdvanceExchangeRateId }, SDbRegistry.FIELD_CODE),
                    (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountDecPaymentAdvanceExchangeRateId }, SDbRegistry.FIELD_NAME),
                    mnFkCostCenterDecPaymentAdvanceExchangeRateId,
                    (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterDecPaymentAdvanceExchangeRateId }, SDbRegistry.FIELD_CODE),
                    (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterDecPaymentAdvanceExchangeRateId }, SDbRegistry.FIELD_NAME),
                    mnFkItemDecPaymentAdvanceExchangeRateId_n,
                    mnFkItemDecPaymentAdvanceExchangeRateId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkItemDecPaymentAdvanceExchangeRateId_n }, SDbRegistry.FIELD_CODE),
                    mnFkItemDecPaymentAdvanceExchangeRateId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkItemDecPaymentAdvanceExchangeRateId_n }, SDbRegistry.FIELD_NAME)));

            rowsMap.put(ACC_DEC_ADV_ADJ, new SAbpRegistry(ACC_DEC_ADV_ADJ, TXT_ACC_DEC_ADV_ADJ, new int[] { },
                    mnFkAccountDecPaymentAdvanceAdjustmentId,
                    (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountDecPaymentAdvanceAdjustmentId }, SDbRegistry.FIELD_CODE),
                    (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountDecPaymentAdvanceAdjustmentId }, SDbRegistry.FIELD_NAME),
                    mnFkCostCenterDecPaymentAdvanceAdjustmentId,
                    (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterDecPaymentAdvanceAdjustmentId }, SDbRegistry.FIELD_CODE),
                    (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterDecPaymentAdvanceAdjustmentId }, SDbRegistry.FIELD_NAME),
                    mnFkItemDecPaymentAdvanceAdjustmentId_n,
                    mnFkItemDecPaymentAdvanceAdjustmentId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkItemDecPaymentAdvanceAdjustmentId_n }, SDbRegistry.FIELD_CODE),
                    mnFkItemDecPaymentAdvanceAdjustmentId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkItemDecPaymentAdvanceAdjustmentId_n }, SDbRegistry.FIELD_NAME)));

            rowsMap.put(ACC_INC_ADV_EXR, new SAbpRegistry(ACC_INC_ADV_EXR, TXT_ACC_INC_ADV_EXR, new int[] { },
                    mnFkAccountIncPaymentAdvanceExchangeRateId,
                    (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountIncPaymentAdvanceExchangeRateId }, SDbRegistry.FIELD_CODE),
                    (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountIncPaymentAdvanceExchangeRateId }, SDbRegistry.FIELD_NAME),
                    mnFkCostCenterIncPaymentAdvanceExchangeRateId,
                    (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterIncPaymentAdvanceExchangeRateId }, SDbRegistry.FIELD_CODE),
                    (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterIncPaymentAdvanceExchangeRateId }, SDbRegistry.FIELD_NAME),
                    mnFkItemIncPaymentAdvanceExchangeRateId_n,
                    mnFkItemIncPaymentAdvanceExchangeRateId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkItemIncPaymentAdvanceExchangeRateId_n }, SDbRegistry.FIELD_CODE),
                    mnFkItemIncPaymentAdvanceExchangeRateId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkItemIncPaymentAdvanceExchangeRateId_n }, SDbRegistry.FIELD_NAME)));

            rowsMap.put(ACC_INC_ADV_ADJ, new SAbpRegistry(ACC_INC_ADV_ADJ, TXT_ACC_INC_ADV_ADJ, new int[] { },
                    mnFkAccountIncPaymentAdvanceAdjustmentId,
                    (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountIncPaymentAdvanceAdjustmentId }, SDbRegistry.FIELD_CODE),
                    (String) session.readField(SModConsts.FIN_ACC, new int[] { mnFkAccountIncPaymentAdvanceAdjustmentId }, SDbRegistry.FIELD_NAME),
                    mnFkCostCenterIncPaymentAdvanceAdjustmentId,
                    (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterIncPaymentAdvanceAdjustmentId }, SDbRegistry.FIELD_CODE),
                    (String) session.readField(SModConsts.FIN_CC, new int[] { mnFkCostCenterIncPaymentAdvanceAdjustmentId }, SDbRegistry.FIELD_NAME),
                    mnFkItemIncPaymentAdvanceAdjustmentId_n,
                    mnFkItemIncPaymentAdvanceAdjustmentId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkItemIncPaymentAdvanceAdjustmentId_n }, SDbRegistry.FIELD_CODE),
                    mnFkItemIncPaymentAdvanceAdjustmentId_n == SLibConsts.UNDEFINED ? "" : (String) session.readField(SModConsts.ITMU_ITEM, new int[] { mnFkItemIncPaymentAdvanceAdjustmentId_n }, SDbRegistry.FIELD_NAME)));
        }

        return rowsMap;
    }

    @Override
    public void setAbpRows(int bizPartnerCategory, HashMap<Integer, SAbpRegistry> rowsMap) {
        SAbpRegistry row = null;

        row = rowsMap.get(ACC_BAL);
        mnFkAccountBalanceId = row.getAccountId();

        row = rowsMap.get(ACC_DEC_BAL_EXR);
        mnFkAccountDecBalanceExchangeRateId = row.getAccountId();
        mnFkCostCenterDecBalanceExchangeRateId = row.getCostCenterId();
        mnFkItemDecBalanceExchangeRateId_n = row.getItemId();

        row = rowsMap.get(ACC_DEC_BAL_ADJ);
        mnFkAccountDecBalanceAdjustmentId = row.getAccountId();
        mnFkCostCenterDecBalanceAdjustmentId = row.getCostCenterId();
        mnFkItemDecBalanceAdjustmentId_n = row.getItemId();

        row = rowsMap.get(ACC_INC_BAL_EXR);
        mnFkAccountIncBalanceExchangeRateId = row.getAccountId();
        mnFkCostCenterIncBalanceExchangeRateId = row.getCostCenterId();
        mnFkItemIncBalanceExchangeRateId_n = row.getItemId();

        row = rowsMap.get(ACC_INC_BAL_ADJ);
        mnFkAccountIncBalanceAdjustmentId = row.getAccountId();
        mnFkCostCenterIncBalanceAdjustmentId = row.getCostCenterId();
        mnFkItemIncBalanceAdjustmentId_n = row.getItemId();

        if (SLibUtils.belongsTo(bizPartnerCategory, new int[] { SDataConstantsSys.BPSS_CT_BP_SUP, SDataConstantsSys.BPSS_CT_BP_CUS })) {
            row = rowsMap.get(ACC_ADV);
            mnFkAccountPaymentAdvanceId = row.getAccountId();

            row = rowsMap.get(ACC_DEC_ADV_EXR);
            mnFkAccountDecPaymentAdvanceExchangeRateId = row.getAccountId();
            mnFkCostCenterDecPaymentAdvanceExchangeRateId = row.getCostCenterId();
            mnFkItemDecPaymentAdvanceExchangeRateId_n = row.getItemId();

            row = rowsMap.get(ACC_DEC_ADV_ADJ);
            mnFkAccountDecPaymentAdvanceAdjustmentId = row.getAccountId();
            mnFkCostCenterDecPaymentAdvanceAdjustmentId = row.getCostCenterId();
            mnFkItemDecPaymentAdvanceAdjustmentId_n = row.getItemId();

            row = rowsMap.get(ACC_INC_ADV_EXR);
            mnFkAccountIncPaymentAdvanceExchangeRateId = row.getAccountId();
            mnFkCostCenterIncPaymentAdvanceExchangeRateId = row.getCostCenterId();
            mnFkItemIncPaymentAdvanceExchangeRateId_n = row.getItemId();

            row = rowsMap.get(ACC_INC_ADV_ADJ);
            mnFkAccountIncPaymentAdvanceAdjustmentId = row.getAccountId();
            mnFkCostCenterIncPaymentAdvanceAdjustmentId = row.getCostCenterId();
            mnFkItemIncPaymentAdvanceAdjustmentId_n = row.getItemId();
        }
    }
}

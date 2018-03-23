/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import static com.sun.webkit.perf.WCFontPerfLogger.reset;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Gil De Jesús, Sergio Flores
 */
public class SDbMaintConfig extends SDbRegistryUser {

    protected int mnPkMaintConfigId;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkPartsCompanyBranchId_n;
    protected int mnFkPartsWarehouseId_n;
    protected int mnFkToolsAvailableCompanyBranchId_n;
    protected int mnFkToolsAvailableWarehouseId_n;
    protected int mnFkToolsLentCompanyBranchId_n;
    protected int mnFkToolsLentWarehouseId_n;
    protected int mnFkToolsMaintCompanyBranchId_n;
    protected int mnFkToolsMaintWarehouseId_n;
    protected int mnFkToolsLostCompanyBranchId_n;
    protected int mnFkToolsLostWarehouseId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbMaintConfig() {
        super(SModConsts.TRN_MAINT_CFG);
        reset();
    }

    public void setPkMaintConfigId(int n) { mnPkMaintConfigId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkPartsCompanyBranchId_n(int n) { mnFkPartsCompanyBranchId_n = n; }
    public void setFkPartsWarehouseId_n(int n) { mnFkPartsWarehouseId_n = n; }
    public void setFkToolsAvailableCompanyBranchId_n(int n) { mnFkToolsAvailableCompanyBranchId_n = n; }
    public void setFkToolsAvailableWarehouseId_n(int n) { mnFkToolsAvailableWarehouseId_n = n; }
    public void setFkToolsLentCompanyBranchId_n(int n) { mnFkToolsLentCompanyBranchId_n = n; }
    public void setFkToolsLentWarehouseId_n(int n) { mnFkToolsLentWarehouseId_n = n; }
    public void setFkToolsMaintCompanyBranchId_n(int n) { mnFkToolsMaintCompanyBranchId_n = n; }
    public void setFkToolsMaintWarehouseId_n(int n) { mnFkToolsMaintWarehouseId_n = n; }
    public void setFkToolsLostCompanyBranchId_n(int n) { mnFkToolsLostCompanyBranchId_n = n; }
    public void setFkToolsLostWarehouseId_n(int n) { mnFkToolsLostWarehouseId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkMaintConfigId() { return mnPkMaintConfigId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkPartsCompanyBranchId_n() { return mnFkPartsCompanyBranchId_n; }
    public int getFkPartsWarehouseId_n() { return mnFkPartsWarehouseId_n; }
    public int getFkToolsAvailableCompanyBranchId_n() { return mnFkToolsAvailableCompanyBranchId_n; }
    public int getFkToolsAvailableWarehouseId_n() { return mnFkToolsAvailableWarehouseId_n; }
    public int getFkToolsLentCompanyBranchId_n() { return mnFkToolsLentCompanyBranchId_n; }
    public int getFkToolsLentWarehouseId_n() { return mnFkToolsLentWarehouseId_n; }
    public int getFkToolsMaintCompanyBranchId_n() { return mnFkToolsMaintCompanyBranchId_n; }
    public int getFkToolsMaintWarehouseId_n() { return mnFkToolsMaintWarehouseId_n; }
    public int getFkToolsLostCompanyBranchId_n() { return mnFkToolsLostCompanyBranchId_n; }
    public int getFkToolsLostWarehouseId_n() { return mnFkToolsLostWarehouseId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public int[] getKeyWarehouseParts() { return mnFkPartsCompanyBranchId_n == SLibConsts.UNDEFINED ? null : new int[] { mnFkPartsCompanyBranchId_n, mnFkPartsWarehouseId_n }; }
    public int[] getKeyWarehouseToolsAvailable() { return mnFkToolsAvailableCompanyBranchId_n == SLibConsts.UNDEFINED ? null : new int[] { mnFkToolsAvailableCompanyBranchId_n, mnFkToolsAvailableWarehouseId_n }; }
    public int[] getKeyWarehouseToolsLent() { return mnFkToolsLentCompanyBranchId_n == SLibConsts.UNDEFINED ? null : new int[] { mnFkToolsLentCompanyBranchId_n, mnFkToolsLentWarehouseId_n }; }
    public int[] getKeyWarehouseToolsMaint() { return mnFkToolsMaintCompanyBranchId_n == SLibConsts.UNDEFINED ? null : new int[] { mnFkToolsMaintCompanyBranchId_n, mnFkToolsMaintWarehouseId_n }; }
    public int[] getKeyWarehouseToolsLost() { return mnFkToolsLostCompanyBranchId_n == SLibConsts.UNDEFINED ? null : new int[] { mnFkToolsLostCompanyBranchId_n, mnFkToolsLostWarehouseId_n }; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
       mnPkMaintConfigId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMaintConfigId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkMaintConfigId = 0;
        mbDeleted = false;
        mnFkPartsCompanyBranchId_n = 0;
        mnFkPartsWarehouseId_n = 0;
        mnFkToolsAvailableCompanyBranchId_n = 0;
        mnFkToolsAvailableWarehouseId_n = 0;
        mnFkToolsLentCompanyBranchId_n = 0;
        mnFkToolsLentWarehouseId_n = 0;
        mnFkToolsMaintCompanyBranchId_n = 0;
        mnFkToolsMaintWarehouseId_n = 0;
        mnFkToolsLostCompanyBranchId_n = 0;
        mnFkToolsLostWarehouseId_n = 0;
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
        return "WHERE id_maint_cfg = " + mnPkMaintConfigId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_maint_cfg = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkMaintConfigId = 0;

        msSql = "SELECT COALESCE(MAX(id_maint_cfg), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkMaintConfigId = resultSet.getInt(1);
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
            mnPkMaintConfigId = resultSet.getInt("id_maint_cfg");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkPartsCompanyBranchId_n = resultSet.getInt("fk_part_cob_n");
            mnFkPartsWarehouseId_n = resultSet.getInt("fk_part_wh_n");
            mnFkToolsAvailableCompanyBranchId_n = resultSet.getInt("fk_tool_av_cob_n");
            mnFkToolsAvailableWarehouseId_n = resultSet.getInt("fk_tool_av_wh_n");
            mnFkToolsLentCompanyBranchId_n = resultSet.getInt("fk_tool_lent_cob_n");
            mnFkToolsLentWarehouseId_n = resultSet.getInt("fk_tool_lent_wh_n");
            mnFkToolsMaintCompanyBranchId_n = resultSet.getInt("fk_tool_maint_cob_n");
            mnFkToolsMaintWarehouseId_n = resultSet.getInt("fk_tool_maint_wh_n");
            mnFkToolsLostCompanyBranchId_n = resultSet.getInt("fk_tool_lost_cob_n");
            mnFkToolsLostWarehouseId_n = resultSet.getInt("fk_tool_lost_wh_n");
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
                    mnPkMaintConfigId + ", " +
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mnFkPartsCompanyBranchId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkPartsCompanyBranchId_n) + ", " + 
                    (mnFkPartsWarehouseId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkPartsWarehouseId_n) + ", " + 
                    (mnFkToolsAvailableCompanyBranchId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkToolsAvailableCompanyBranchId_n) + ", " + 
                    (mnFkToolsAvailableWarehouseId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkToolsAvailableWarehouseId_n) + ", " + 
                    (mnFkToolsLentCompanyBranchId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkToolsLentCompanyBranchId_n) + ", " + 
                    (mnFkToolsLentWarehouseId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkToolsLentWarehouseId_n) + ", " + 
                    (mnFkToolsMaintCompanyBranchId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkToolsMaintCompanyBranchId_n) + ", " + 
                    (mnFkToolsMaintWarehouseId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkToolsMaintWarehouseId_n) + ", " + 
                    (mnFkToolsLostCompanyBranchId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkToolsLostCompanyBranchId_n) + ", " + 
                    (mnFkToolsLostWarehouseId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkToolsLostWarehouseId_n) + ", " + 
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    "id_maint_cfg = " + mnPkMaintConfigId + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_part_cob_n = " + (mnFkPartsCompanyBranchId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkPartsCompanyBranchId_n) + ", " +
                    "fk_part_wh_n = " + (mnFkPartsWarehouseId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkPartsWarehouseId_n) + ", " +
                    "fk_tool_av_cob_n = " + (mnFkToolsAvailableCompanyBranchId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkToolsAvailableCompanyBranchId_n) + ", " +
                    "fk_tool_av_wh_n = " + (mnFkToolsAvailableWarehouseId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkToolsAvailableWarehouseId_n) + ", " +
                    "fk_tool_lent_cob_n = " + (mnFkToolsLentCompanyBranchId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkToolsLentCompanyBranchId_n) + ", " +
                    "fk_tool_lent_wh_n = " + (mnFkToolsLentWarehouseId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkToolsLentWarehouseId_n) + ", " +
                    "fk_tool_maint_cob_n = " + (mnFkToolsMaintCompanyBranchId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkToolsMaintCompanyBranchId_n) + ", " +
                    "fk_tool_maint_wh_n = " + (mnFkToolsMaintWarehouseId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkToolsMaintWarehouseId_n) + ", " +
                    "fk_tool_lost_cob_n = " + (mnFkToolsLostCompanyBranchId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkToolsLostCompanyBranchId_n) + ", " +
                    "fk_tool_lost_wh_n = " + (mnFkToolsLostWarehouseId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkToolsLostWarehouseId_n) + ", " +
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
    public SDbMaintConfig clone() throws CloneNotSupportedException {
        SDbMaintConfig registry = new SDbMaintConfig();

        registry.setPkMaintConfigId(this.getPkMaintConfigId());
        registry.setDeleted(this.isDeleted());
        registry.setFkPartsCompanyBranchId_n(this.getFkPartsCompanyBranchId_n());
        registry.setFkPartsWarehouseId_n(this.getFkPartsWarehouseId_n());
        registry.setFkToolsAvailableCompanyBranchId_n(this.getFkToolsAvailableCompanyBranchId_n());
        registry.setFkToolsAvailableWarehouseId_n(this.getFkToolsAvailableWarehouseId_n());
        registry.setFkToolsLentCompanyBranchId_n(this.getFkToolsLentCompanyBranchId_n());
        registry.setFkToolsLentWarehouseId_n(this.getFkToolsLentWarehouseId_n());
        registry.setFkToolsMaintCompanyBranchId_n(this.getFkToolsMaintCompanyBranchId_n());
        registry.setFkToolsMaintWarehouseId_n(this.getFkToolsMaintWarehouseId_n());
        registry.setFkToolsLostCompanyBranchId_n(this.getFkToolsLostCompanyBranchId_n());
        registry.setFkToolsLostWarehouseId_n(this.getFkToolsLostWarehouseId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
 */
public class SDbAbpEntityLink extends SDbRegistryUser {

    protected int mnPkAbpEntityId;
    protected int mnPkCompanyBranchId;
    protected int mnPkEntityId;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbAbpEntityLink() {
        super(SModConsts.FIN_ABP_ENT_LINK);
    }

    public void setPkAbpEntityId(int n) { mnPkAbpEntityId = n; }
    public void setPkCompanyBranchId(int n) { mnPkCompanyBranchId = n; }
    public void setPkEntityId(int n) { mnPkEntityId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkAbpEntityId() { return mnPkAbpEntityId; }
    public int getPkCompanyBranchId() { return mnPkCompanyBranchId; }
    public int getPkEntityId() { return mnPkEntityId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAbpEntityId = pk[0];
        mnPkCompanyBranchId = pk[1];
        mnPkEntityId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAbpEntityId, mnPkCompanyBranchId, mnPkEntityId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkAbpEntityId = 0;
        mnPkCompanyBranchId = 0;
        mnPkEntityId = 0;
        mbDeleted = false;
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
        return "WHERE id_abp_ent = " + mnPkAbpEntityId + " AND " +
                "id_cob = " + mnPkCompanyBranchId + " AND " +
                "id_ent = " + mnPkEntityId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_abp_ent = " + pk[0] + " AND " +
                "id_cob = " + pk[1] + " AND " +
                "id_ent = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
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
            mnPkCompanyBranchId = resultSet.getInt("id_cob");
            mnPkEntityId = resultSet.getInt("id_ent");
            mbDeleted = resultSet.getBoolean("b_del");
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
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkAbpEntityId + ", " +
                    mnPkCompanyBranchId + ", " +
                    mnPkEntityId + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
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
                    "id_abp_ent = " + mnPkAbpEntityId + ", " +
                    "id_cob = " + mnPkCompanyBranchId + ", " +
                    "id_ent = " + mnPkEntityId + ", " +
                    */
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
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
    public SDbAbpEntityLink clone() throws CloneNotSupportedException {
        SDbAbpEntityLink registry = new SDbAbpEntityLink();

        registry.setPkAbpEntityId(this.getPkAbpEntityId());
        registry.setPkCompanyBranchId(this.getPkCompanyBranchId());
        registry.setPkEntityId(this.getPkEntityId());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

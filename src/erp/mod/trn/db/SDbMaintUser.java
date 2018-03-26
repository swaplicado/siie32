/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

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
 * @author Gil De Jesús, Sergio Flores
 */
public class SDbMaintUser extends SDbRegistryUser {
    
    protected int mnPkMaintUserId;
    protected java.sql.Blob moFingerprint_n;
    protected boolean mbEmployee;
    protected boolean mbContractor;
    protected boolean mbToolsMaintProvider;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbMaintUser() {
        super(SModConsts.TRN_MAINT_USER);
    }

    public void setPkMaintUserId(int n) { mnPkMaintUserId = n; }
    public void setFingerprint_n(java.sql.Blob o) { moFingerprint_n = o; }
    public void setEmployee(boolean b) { mbEmployee = b; }
    public void setContractor(boolean b) { mbContractor = b; }
    public void setToolsMaintProvider(boolean b) { mbToolsMaintProvider = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkMaintUserId() { return mnPkMaintUserId; }
    public java.sql.Blob getFingerprint_n() { return moFingerprint_n; }
    public boolean isEmployee() { return mbEmployee; }
    public boolean isContractor() { return mbContractor; }
    public boolean isToolsMaintProvider() { return mbToolsMaintProvider; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMaintUserId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMaintUserId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkMaintUserId = 0;
        moFingerprint_n = null;
        mbEmployee = false;
        mbContractor = false;
        mbToolsMaintProvider = false;
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
        return "WHERE id_maint_user = " + mnPkMaintUserId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_maint_user = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession sgs) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            mnPkMaintUserId = resultSet.getInt("id_maint_user");
            moFingerprint_n = resultSet.getBlob("fingerprint_n");
            mbEmployee = resultSet.getBoolean("b_employee");
            mbContractor = resultSet.getBoolean("b_contractor");
            mbToolsMaintProvider = resultSet.getBoolean("b_tool_maint_prov");
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
        
        verifyRegistryNew(session);

        if (mbRegistryNew) {
            //computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkMaintUserId + ", " + 
                    "NULL, " + 
                    (mbEmployee ? 1 : 0) + ", " + 
                    (mbContractor ? 1 : 0) + ", " + 
                    (mbToolsMaintProvider ? 1 : 0) + ", " + 
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
                    //"id_maint_area = " + mnPkMaintAreaId + ", " +
                    //"fingerprint_n = " + moFingerprint_n + ", " +
                    "b_employee = " + (mbEmployee ? 1 : 0) + ", " +
                    "b_contractor = " + (mbContractor ? 1 : 0) + ", " +
                    "b_tool_maint_prov = " + (mbToolsMaintProvider ? 1 : 0) + ", " +
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
    public SDbMaintUser clone() throws CloneNotSupportedException {
        SDbMaintUser registry = new SDbMaintUser();

        registry.setPkMaintUserId(this.getPkMaintUserId());
        registry.setFingerprint_n(this.getFingerprint_n());
        registry.setEmployee(this.isEmployee());
        registry.setContractor(this.isContractor());
        registry.setToolsMaintProvider(this.isToolsMaintProvider());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

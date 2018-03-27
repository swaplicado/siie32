/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
public class SDbMaintUserSupervisor extends SDbRegistryUser {

    protected int mnPkMaintUserSupervisorId;
    protected String msName;
    protected java.sql.Blob moFingerprint_n;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkMaintUserId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected byte[] maAuxFingerprintBytes;

    public SDbMaintUserSupervisor() {
        super(SModConsts.TRN_MAINT_USER_SUPV);
    }

    public void setPkMaintUserSupervisorId(int n) { mnPkMaintUserSupervisorId = n; }
    public void setName(String s) { msName = s; }
    public void setFingerprint_n(java.sql.Blob o) { moFingerprint_n = o; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkMaintUserId_n(int n) { mnFkMaintUserId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkMaintUserSupervisorId() { return mnPkMaintUserSupervisorId; }
    public String getName() { return msName; }
    public java.sql.Blob getFingerprint_n() { return moFingerprint_n; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkMaintUserId_n() { return mnFkMaintUserId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setAuxFingerprintBytes(byte[] bytes) { maAuxFingerprintBytes = bytes; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMaintUserSupervisorId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMaintUserSupervisorId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkMaintUserSupervisorId = 0;
        msName = "";
        moFingerprint_n = null;
        mbDeleted = false;
        mnFkMaintUserId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maAuxFingerprintBytes = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_maint_user_supv = " + mnPkMaintUserSupervisorId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_maint_user_supv = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkMaintUserSupervisorId = 0;

        msSql = "SELECT COALESCE(MAX(id_maint_user_supv), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkMaintUserSupervisorId = resultSet.getInt(1);
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
            mnPkMaintUserSupervisorId = resultSet.getInt("id_maint_user_supv");
            msName = resultSet.getString("name");
            moFingerprint_n = resultSet.getBlob("fingerprint_n");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkMaintUserId_n = resultSet.getInt("fk_maint_user_n");
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
                    mnPkMaintUserSupervisorId + ", " + 
                    "'" + msName + "', " + 
                    "NULL, " + 
                    (mbDeleted ? 1 : 0) + ", " +
                    (mnFkMaintUserId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkMaintUserId_n) + ", " + 
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    // "id_maint_user_supv = " + mnPkMaintUserSupervisorId + ", " +
                    "name = '" + msName + "', " +
                    //"fingerprint_n = " + moFingerprint_n + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_maint_user_n = " + (mnFkMaintUserId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkMaintUserId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // save fingerprint:
        PreparedStatement preparedStatement = session.getStatement().getConnection().prepareStatement("UPDATE " + getSqlTable() + " SET fingerprint_n = ? " + getSqlWhere());
        if (maAuxFingerprintBytes == null) {
            preparedStatement.setNull(1, Types.BLOB);
        }
        else {
            preparedStatement.setBlob(1, new ByteArrayInputStream(maAuxFingerprintBytes));
        }
        preparedStatement.execute();
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMaintUserSupervisor clone() throws CloneNotSupportedException {
        SDbMaintUserSupervisor registry = new SDbMaintUserSupervisor();

        registry.setPkMaintUserSupervisorId(this.getPkMaintUserSupervisorId());
        registry.setName(this.getName());
        registry.setFingerprint_n(this.getFingerprint_n());
        registry.setDeleted(this.isDeleted());
        registry.setFkMaintUserId_n(this.getFkMaintUserId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setAuxFingerprintBytes(maAuxFingerprintBytes == null ? null : maAuxFingerprintBytes.clone());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

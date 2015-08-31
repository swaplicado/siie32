/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Irving Sánchez
 */
public class SDbMmsConfig extends SDbRegistryUser {
    
    protected int mnPkMmsTypeId;
    protected int mnPkConfigId;
    protected String msEmail;
    protected int mnFkLinkTypeId;
    protected int mnFkReferenceId;

    public SDbMmsConfig() {
        super(SModConsts.TRN_MMS_CFG);
    }
    
    public void setPkMmsTypeId(int n) { mnPkMmsTypeId = n; }
    public void setPkConfigId(int n) { mnPkConfigId = n; }
    public void setEmail(String s) { msEmail = s; }
    public void setFkLinkTypeId(int n) { mnFkLinkTypeId = n; }
    public void setFkReferenceId(int n) { mnFkReferenceId = n; }

    public int getPkMmsTypeId() { return mnPkMmsTypeId; }
    public int getPkConfigId() { return mnPkConfigId; }
    public String getEmail() { return msEmail; }
    public int getFkLinkTypeId() { return mnFkLinkTypeId; }
    public int getFkReferenceId() { return mnFkReferenceId; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMmsTypeId = pk[0];
        mnPkConfigId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMmsTypeId, mnPkConfigId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMmsTypeId = 0;
        mnPkConfigId = 0;
        msEmail = "";
        mbDeleted = false;
        mnFkLinkTypeId = 0;
        mnFkReferenceId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

    }

    @Override
    public void initPrimaryKey() {
        setPrimaryKey(new int[]{mnPkMmsTypeId, 0});
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_tp_mms = " + mnPkMmsTypeId + " AND "
                + "id_cfg = " + mnPkConfigId  + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_tp_mms = " + pk[0] + " AND "
                + "id_cfg = " + pk[1]  + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkConfigId = 0;

        msSql = "SELECT COALESCE(MAX(id_cfg), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkConfigId = resultSet.getInt(1);
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
            mnPkMmsTypeId = resultSet.getInt("id_tp_mms");
            mnPkConfigId = resultSet.getInt("id_cfg");
            msEmail = resultSet.getString("email");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkLinkTypeId = resultSet.getInt("fk_tp_link");
            mnFkReferenceId = resultSet.getInt("fk_ref");
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
                    mnPkMmsTypeId + ", " + 
                    mnPkConfigId + ", " + 
                    "'" + msEmail + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkLinkTypeId + ", " + 
                    mnFkReferenceId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + "); " ; 
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    "id_tp_mms = " + mnPkMmsTypeId + ", " +
                    "id_cfg = " + mnPkConfigId + ", " +
                    "email = '" + msEmail + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_link = " + mnFkLinkTypeId + ", " +
                    "fk_ref = " + mnFkReferenceId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " 
                    + getSqlWhere() ;
        }
        
        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMmsConfig clone() throws CloneNotSupportedException {
        SDbMmsConfig registry = new SDbMmsConfig();
        
        registry.setPkMmsTypeId(this.getPkMmsTypeId());
        registry.setPkConfigId(this.getPkConfigId());
        registry.setEmail(this.getEmail());
        registry.setDeleted(this.isDeleted());
        registry.setFkLinkTypeId(this.getFkLinkTypeId());
        registry.setFkReferenceId(this.getFkReferenceId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        return registry;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.cfg.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Edwin Carmona
 */
public class SDbAuthorizationNodeUser extends SDbRegistryUser {

    protected int mnPkAuthorizationNodeId;
    protected int mnPkAuthorizationUserId;
//    protected boolean mbDeleted;
//    protected boolean mbSystem;
//    protected int mnFkUserInsertId;
//    protected int mnFkUserUpdateId;
//    protected Date mtTsUserInsert;
//    protected Date mtTsUserUpdate;

    public SDbAuthorizationNodeUser() {
        super(SModConsts.CFGU_AUTHORN_NODE_USR);
    }
    
    public void setPkAuthorizationNodeId(int n) { mnPkAuthorizationNodeId = n; }
    public void setPkAuthorizationUserId(int n) { mnPkAuthorizationUserId = n; }
//    public void setDeleted(boolean b) { mbDeleted = b; }
//    public void setSystem(boolean b) { mbSystem = b; }
//    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
//    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
//    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
//    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkAuthorizationNodeId() { return mnPkAuthorizationNodeId; }
    public int getPkAuthorizationUserId() { return mnPkAuthorizationUserId; }
//    public boolean isDeleted() { return mbDeleted; }
//    public boolean isSystem() { return mbSystem; }
//    public int getFkUserInsertId() { return mnFkUserInsertId; }
//    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
//    public Date getTsUserInsert() { return mtTsUserInsert; }
//    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAuthorizationNodeId = pk[0];
        mnPkAuthorizationUserId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAuthorizationNodeId, mnPkAuthorizationUserId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkAuthorizationNodeId = 0;
        mnPkAuthorizationUserId = 0;
        mbDeleted = false;
        mbSystem = false;
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
        return "WHERE id_authorn_node = " + mnPkAuthorizationNodeId + " AND id_authorn_user = " + mnPkAuthorizationUserId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_authorn_node = " + pk[0] + " AND id_authorn_user = " + pk[1];
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
            mnPkAuthorizationNodeId = resultSet.getInt("id_authorn_node");
            mnPkAuthorizationUserId = resultSet.getInt("id_authorn_user");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
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
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkAuthorizationNodeId + ", " + 
                    mnPkAuthorizationUserId + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
//                    "id_authorn_node = " + mnPkAuthorizationNodeId + ", " +
//                    "id_authorn_user = " + mnPkAuthorizationUserId + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
//                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
//                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + ", " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbAuthorizationNodeUser registry = new SDbAuthorizationNodeUser();

        registry.setPkAuthorizationNodeId(this.getPkAuthorizationNodeId());
        registry.setPkAuthorizationUserId(this.getPkAuthorizationUserId());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

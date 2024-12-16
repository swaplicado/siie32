/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author Isabel Servín
 */
public class SDbMaterialRequestExternalStorageLog extends SDbRegistryUser {

    protected int mnPkMatRequestId;
    protected int mnPkLogId;
    protected Date mtTsExternalStorage;
    protected int mnFkUserId;
    protected Date mtTsUser;

    public SDbMaterialRequestExternalStorageLog() {
        super(SModConsts.TRN_MAT_REQ_EXT_STO_LOG);
    }
    
    public void setPkMatRequestId(int n) { mnPkMatRequestId = n; }
    public void setPkLogId(int n) { mnPkLogId = n; }
    public void setTsExternalStorage(Date t) { mtTsExternalStorage = t; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }

    public int getPkMatRequestId() { return mnPkMatRequestId; }
    public int getPkLogId() { return mnPkLogId; }
    public Date getTsExternalStorage() { return mtTsExternalStorage; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMatRequestId = pk[0];
        mnPkLogId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMatRequestId, mnPkLogId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMatRequestId = 0;
        mnPkLogId = 0;
        mtTsExternalStorage = null;
        mnFkUserId = 0;
        mtTsUser = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mat_req = " + mnPkMatRequestId + " AND id_log = " + mnPkLogId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) { 
        return "WHERE id_mat_req = " + pk[0] + " AND id_log = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkLogId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_log), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_mat_req = " + mnPkMatRequestId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkLogId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkMatRequestId = resultSet.getInt("id_mat_req");
            mnPkLogId = resultSet.getInt("id_log");
            mtTsExternalStorage = resultSet.getTimestamp("ts_ext_sto");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");

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
            mnFkUserId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkMatRequestId + ", " + 
                    mnPkLogId + ", " + 
                    "NOW()" + ", " + 
                    mnFkUserId + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_mat_req = " + mnPkMatRequestId + ", " +
                    //"id_log = " + mnPkLogId + ", " +
                    "ts_ext_sto = " + "NOW()" + " " +
                    //"fk_usr = " + mnFkUserId + ", " +
                    //"ts_usr = " + "NOW()" + " " +
                    getSqlWhere();
        }
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMaterialRequestExternalStorageLog clone() throws CloneNotSupportedException {
        SDbMaterialRequestExternalStorageLog registry = new SDbMaterialRequestExternalStorageLog();
        
        registry.setPkMatRequestId(this.getPkMatRequestId());
        registry.setPkLogId(this.getPkLogId());
        registry.setTsExternalStorage(this.getTsExternalStorage());
        registry.setFkUserId(this.getFkUserId());
        registry.setTsUser(this.getTsUser());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}

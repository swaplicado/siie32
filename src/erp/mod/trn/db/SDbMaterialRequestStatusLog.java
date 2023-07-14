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
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbMaterialRequestStatusLog extends SDbRegistryUser {
    
    protected int mnPkMatRequestId;
    protected int mnPkLogId;
    protected int mnFkMatRequestStatusId;
    protected int mnFkMatRequestStatusAuthId;
    protected int mnFkMatProvisionStatusId;
    protected int mnFkMatPurchaseStatusId;
    protected int mnFkUserId;
    protected Date mtTsUser;

    public SDbMaterialRequestStatusLog() {
        super(SModConsts.TRN_MAT_REQ_ST_LOG);
    }

    public void setPkMatRequestId(int n) { mnPkMatRequestId = n; }
    public void setPkLogId(int n) { mnPkLogId = n; }
    public void setFkMatRequestStatusId(int n) { mnFkMatRequestStatusId = n; }
    public void setFkMatRequestStatusAuthId(int n) { mnFkMatRequestStatusAuthId = n; }
    public void setFkMatProvisionStatusId(int n) { mnFkMatProvisionStatusId = n; }
    public void setFkMatPurchaseStatusId(int n) { mnFkMatPurchaseStatusId = n; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }
    
    public int getPkMatRequestId() { return mnPkMatRequestId; }
    public int getPkLogId() { return mnPkLogId; }
    public int getFkMatRequestStatusId() { return mnFkMatRequestStatusId; }
    public int getFkMatRequestStatusAuthId() { return mnFkMatRequestStatusAuthId; }
    public int getFkMatProvisionStatusId() { return mnFkMatProvisionStatusId; }
    public int getFkMatPurchaseStatusId() { return mnFkMatPurchaseStatusId; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMatRequestId = pk[0];
        mnPkLogId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMatRequestId, mnPkLogId };
    }

    @Override
    public void initRegistry() {
        mnPkMatRequestId = 0;
        mnPkLogId = 0;
        mnFkMatRequestStatusId = 0;
        mnFkMatRequestStatusAuthId = 0;
        mnFkMatProvisionStatusId = 0;
        mnFkMatPurchaseStatusId = 0;
        mnFkUserId = 0;
        mtTsUser = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSqlWhere(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mnFkUserId = session.getUser().getPkUserId();
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" + 
                    mnPkMatRequestId + ", " + 
                    mnPkLogId + ", " + 
                    mnFkMatRequestStatusId + ", " + 
                    mnFkMatRequestStatusAuthId + ", " + 
                    mnFkMatProvisionStatusId + ", " + 
                    mnFkMatPurchaseStatusId + ", " + 
                    mnFkUserId + ", " + 
                    "NOW()" + " " +
                    ")";
        }
        
        session.getStatement().execute(msSql);
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

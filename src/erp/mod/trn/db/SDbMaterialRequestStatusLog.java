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
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín, Edwin Carmona
 */
public class SDbMaterialRequestStatusLog extends SDbRegistryUser implements SGridRow {
    
    protected int mnPkMatRequestId;
    protected int mnPkLogId;
    protected String msNotes_n;
    protected int mnFkMatRequestStatusId;
    protected int mnFkMatRequestAuthotizationStatusId;
    protected int mnFkMatProvisionStatusId;
    protected int mnFkMatPurchaseStatusId;
    protected int mnFkUserId;
    protected Date mtTsUser;
    
    protected String msAuxUser;
    protected String msAuxMatReqStatus;
    protected String msAuxMatReqAuthStatus;
    protected String msAuxMatReqProvStatus;
    protected String msAuxMatReqPurStatus;

    public SDbMaterialRequestStatusLog() {
        super(SModConsts.TRN_MAT_REQ_ST_LOG);
    }

    public void setPkMatRequestId(int n) { mnPkMatRequestId = n; }
    public void setPkLogId(int n) { mnPkLogId = n; }
    public void setNotes_n(String s) { msNotes_n = s; }
    public void setFkMatRequestStatusId(int n) { mnFkMatRequestStatusId = n; }
    public void setFkMatRequestAuthotizationStatusId(int n) { mnFkMatRequestAuthotizationStatusId = n; }
    public void setFkMatProvisionStatusId(int n) { mnFkMatProvisionStatusId = n; }
    public void setFkMatPurchaseStatusId(int n) { mnFkMatPurchaseStatusId = n; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }
    
    public int getPkMatRequestId() { return mnPkMatRequestId; }
    public int getPkLogId() { return mnPkLogId; }
    public String getNotes_n() { return msNotes_n; }
    public int getFkMatRequestStatusId() { return mnFkMatRequestStatusId; }
    public int getFkMatRequestAuthorizationStatusId() { return mnFkMatRequestAuthotizationStatusId; }
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
        msNotes_n = null;
        mnFkMatRequestStatusId = 0;
        mnFkMatRequestAuthotizationStatusId = 0;
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
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT  " +
                    "l.*, " +
                    "sr.name AS st_mr, " +
                    "sa.name AS st_auth, " +
                    "spro.name AS st_pro, " +
                    "spur.name AS st_pur, " +
                    "u.usr " +
                    "FROM " +
                    getSqlTable() + " AS l " +
                    "INNER JOIN " +
                    SModConsts.TablesMap.get(SModConsts.TRNS_ST_MAT_REQ) + " AS sr ON l.fk_st_mat_req = sr.id_st_mat_req " +
                    "INNER JOIN " +
                    SModConsts.TablesMap.get(SModConsts.CFGS_ST_AUTHORN) + " AS sa ON l.fk_st_mat_authorn = sa.id_st_authorn " +
                    "INNER JOIN " +
                    SModConsts.TablesMap.get(SModConsts.TRNS_ST_MAT_PROV) + " AS spro ON l.fk_st_mat_prov = spro.id_st_mat_prov " +
                    "INNER JOIN " +
                    SModConsts.TablesMap.get(SModConsts.TRNS_ST_MAT_PUR) + " AS spur ON l.fk_st_mat_pur = spur.id_st_mat_pur " +
                    "INNER JOIN " +
                    SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS u ON l.fk_usr = u.id_usr " +
                    "WHERE " +
                    "l.id_mat_req = " + pk[0] + " AND l.id_log = " + pk[1] + " " +
                    "ORDER BY id_log ASC;";
        
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkLogId = resultSet.getInt("id_log");
            msNotes_n = resultSet.getString("notes_n");
            mnFkMatRequestStatusId = resultSet.getInt("fk_st_mat_req");
            mnFkMatRequestAuthotizationStatusId = resultSet.getInt("fk_st_mat_authorn");
            mnFkMatProvisionStatusId = resultSet.getInt("fk_st_mat_prov");
            mnFkMatPurchaseStatusId = resultSet.getInt("fk_st_mat_pur");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");

            msAuxUser = resultSet.getString("usr");
            msAuxMatReqStatus = resultSet.getString("st_mr");
            msAuxMatReqAuthStatus = resultSet.getString("st_auth");
            msAuxMatReqProvStatus = resultSet.getString("st_pro");
            msAuxMatReqPurStatus = resultSet.getString("st_pur");
            
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
            mnFkUserId = session.getUser().getPkUserId();
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" + 
                    mnPkMatRequestId + ", " + 
                    mnPkLogId + ", " + 
                    (msNotes_n == null ? "null" : ("'" + msNotes_n + "'")) + ", " + 
                    mnFkMatRequestStatusId + ", " + 
                    mnFkMatRequestAuthotizationStatusId + ", " + 
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

    @Override
    public int[] getRowPrimaryKey() {
        return this.getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
    }

    @Override
    public boolean isRowSystem() {
        return this.isSystem();
    }

    @Override
    public boolean isRowDeletable() {
        return false;
    }

    @Override
    public boolean isRowEdited() {
        return false;
    }

    @Override
    public void setRowEdited(boolean edited) {
        
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = mnPkLogId;
                break;
            case 1:
                value = msAuxMatReqStatus;
                break;
            case 2:
                value = mtTsUser;
                break;
            case 3:
                value = msAuxUser;
                break;
            case 4:
                value = msNotes_n;
                break;
            default:
                break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

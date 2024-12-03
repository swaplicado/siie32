/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Isabel Servín
 */
public class SDataDpsAuthorn extends SDataRegistry {
    
    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnFkAuthorizationStatusId;
    protected int mnFkUserId;
    protected java.util.Date mtTsUser;

    public SDataDpsAuthorn() {
        super(SDataConstants.TRN_DPS_AUTHORN);
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setFkAuthorizationStatusId(int n) { mnFkAuthorizationStatusId = n; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(java.util.Date t) { mtTsUser = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getFkAuthorizationStatusId() { return mnFkAuthorizationStatusId; }
    public int getFkUserId() { return mnFkUserId; }
    public java.util.Date getTsUser() { return mtTsUser; }
    
    @Override
    public void setPrimaryKey(Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkYearId = 0;
        mnPkDocId = 0;
        mnFkAuthorizationStatusId = 0;
        mnFkUserId = 0;
        mtTsUser = null;
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_dps_authorn WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                mnFkAuthorizationStatusId = resultSet.getInt("fid_st_authorn");
                mnFkUserId = resultSet.getInt("fid_usr");
                mtTsUser = resultSet.getTimestamp("ts_usr");
            }
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public int save(Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        String sql;
        try {
            if (mbIsRegistryNew) {
                sql = "INSERT INTO trn_dps_authorn VALUES(" + 
                        mnPkYearId + ", " +
                        mnPkDocId + ", " +
                        mnFkAuthorizationStatusId + ", " +
                        mnFkUserId + ", " +
                        "NOW()" +
                        ");";
            }
            else {
                sql = "UPDATE trn_dps_authorn SET " +
                        //"id_year = " + mnPkYearId + ", " +
                        //"id_doc = " + mnPkDocId + ", " +
                        "fid_st_authorn = " + mnFkAuthorizationStatusId + ", " +
                        "fid_usr = " + mnFkUserId + ", " +
                        "ts_usr = NOW() " +
                        "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
            }
            connection.createStatement().execute(sql);
            
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
    
    @Override
    public int delete(Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            String sql = "DELETE FROM trn_dps_authorn WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId;
            connection.createStatement().execute(sql);
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }
}

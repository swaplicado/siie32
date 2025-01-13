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
    protected int mnPkAuthornId;
    protected java.lang.String msNotes;
    protected java.lang.String msException;
    protected boolean mbDeleted;
    protected int mnFkAuthorizationStatusId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;
    
    public SDataDpsAuthorn() {
        super(SDataConstants.TRN_DPS_AUTHORN);
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkAuthornId(int n) { mnPkAuthornId = n; }
    public void setNotes(java.lang.String s) { msNotes = s; }
    public void setException(java.lang.String s) { msException = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkAuthorizationStatusId(int n) { mnFkAuthorizationStatusId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkAuthornId() { return mnPkAuthornId; }
    public java.lang.String getNotes() { return msNotes; }
    public java.lang.String getException() { return msException; }
    public boolean getDeleted() { return mbDeleted; }
    public int getFkAuthorizationStatusId() { return mnFkAuthorizationStatusId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    private void deleteAllRegistries(Connection connection) throws Exception {
        String sql = "UPDATE trn_dps_authorn SET " +
                "b_del = 1, fid_usr_del = " + mnFkUserDeleteId + ", ts_del = NOW() " +
                "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId;
        connection.createStatement().execute(sql);
    }

    private void computePrimaryKey(Connection connection) throws Exception {
        ResultSet resultSet;
        
        mnPkAuthornId = 0;
        
        String sql = "SELECT COALESCE(MAX(id_authorn), 0) + 1 FROM trn_dps_authorn " + 
                "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId; 
        resultSet = connection.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            mnPkAuthornId = resultSet.getInt(1);
        }
    }
    
    @Override
    public void setPrimaryKey(Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkAuthornId = ((int[]) pk)[2];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkAuthornId };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkAuthornId = 0;
        msNotes = "";
        msException = "";
        mbDeleted = false;
        mnFkAuthorizationStatusId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
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
                mnPkAuthornId = resultSet.getInt("id_authorn");
                msNotes = resultSet.getString("nts");
                msException = resultSet.getString("exc");
                mbDeleted = resultSet.getBoolean("b_del");
                mnFkAuthorizationStatusId = resultSet.getInt("fid_st_authorn");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");
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
                deleteAllRegistries(connection);
                computePrimaryKey(connection);
                
                sql = "INSERT INTO trn_dps_authorn VALUES(" + 
                        mnPkYearId + ", " +
                        mnPkDocId + ", " +
                        mnPkAuthornId + ", " +
                        "'" + msNotes + "', " +
                        "'" + msException + "', " +
                        mbDeleted + ", " +
                        mnFkAuthorizationStatusId + ", " +
                        mnFkUserNewId + ", " +
                        mnFkUserEditId + ", " +
                        mnFkUserDeleteId + ", " +
                        "NOW(), " +
                        "NOW(), " +
                        "NOW()" +
                        ");";
            }
            else {
                sql = "UPDATE trn_dps_authorn SET " +
                        //"id_year = " + mnPkYearId + ", " +
                        //"id_doc = " + mnPkDocId + ", " +
                        //"id_authorn = " + mnPkAuthornId + ", " +
                        //"nts = '" + msNotes + ", " +
                        "exc='" + msException + "', " +
                        //"b_del = " + (mbDeleted ? 1 : 0) + ", " +
                        "fid_st_authorn = " + mnFkAuthorizationStatusId + ", " +
                        //"fid_usr_new = " + mnFkUserNewId + ", " +
                        "fid_usr_edit = " + mnFkUserEditId + ", " +
                        //"fid_usr_del = " + mnFkUserDeleteId + ", " +
                        //"ts_new = NOW(), " +
                        "ts_edit = NOW() " +
                        //"ts_del = NOW() " +
                        "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_authorn = " + mnPkAuthornId;
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
            String sql = "UPDATE FROM trn_dps_authorn SET " +
                    "b_del = 1, fid_usr_del = " + mnFkUserDeleteId + ", ts_del = NOW() " +
                    "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_authorn = " + mnPkAuthornId;
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

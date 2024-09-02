/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mcfg.data;

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
public class SDataScale extends SDataRegistry {

    protected int mnPkScaleId;
    protected java.lang.String msScaleKey;
    protected java.lang.String msScale;
    protected java.lang.String msDbmsDriver;
    protected java.lang.String msHost;
    protected java.lang.String msPort;
    protected java.lang.String msUserName;
    protected java.lang.String msUserPassword;
    protected java.lang.String msDbName;
    protected java.lang.String msScaleCompanyId;
    protected boolean mbIsCanEdit;
    protected boolean mbIsCanDelete;
    protected boolean mbIsDeleted;
    protected int mnFkDbmsTypeId;
    protected int mnFkUnitId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;
    
    public SDataScale() {
        super(SDataConstants.CFGU_SCA);
        reset();
    }
    
    public void setPkScaleId(int n) { mnPkScaleId = n; }
    public void setScaleKey(java.lang.String s) { msScaleKey = s; }
    public void setScale(java.lang.String s) { msScale = s; }
    public void setDbmsDriver(java.lang.String s) { msDbmsDriver = s; }
    public void setHost(java.lang.String s) { msHost = s; }
    public void setPort(java.lang.String s) { msPort = s; }
    public void setUserName(java.lang.String s) { msUserName = s; }
    public void setUserPassword(java.lang.String s) { msUserPassword = s; }
    public void setDbName(java.lang.String s) { msDbName = s; }
    public void setScaleCompanyId(java.lang.String s) { msScaleCompanyId = s; }
    public void setIsCanEdit(boolean b) { mbIsCanEdit = b; }
    public void setIsCanDelete(boolean b) { mbIsCanDelete = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkDbmsTypeId(int n) { mnFkDbmsTypeId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkScaleId() { return mnPkScaleId; }
    public java.lang.String getScaleKey() { return msScaleKey; }
    public java.lang.String getScale() { return msScale; }
    public java.lang.String getDbmsDriver() { return msDbmsDriver; }
    public java.lang.String getHost() { return msHost; }
    public java.lang.String getPort() { return msPort; }
    public java.lang.String getUserName() { return msUserName; }
    public java.lang.String getUserPassword() { return msUserPassword; }
    public java.lang.String getDbName() { return msDbName; }
    public java.lang.String getScaleCompanyId() { return msScaleCompanyId; }
    public int getFkDbmsTypeId() { return mnFkDbmsTypeId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public boolean hasConnection() {
        return !msDbmsDriver.isEmpty() && !msHost.isEmpty() && !msPort.isEmpty() && !msUserName.isEmpty() && 
                !msUserPassword.isEmpty() && !msDbName.isEmpty();
    }
    
    @Override
    public void setPrimaryKey(Object pk) {
        mnPkScaleId = ((int[]) pk)[0];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkScaleId };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkScaleId = 0;
        msScaleKey = "";
        msScale = "";
        msDbmsDriver = "";
        msHost = "";
        msPort = "";
        msUserName = "";
        msUserPassword = "";
        msDbName = "";
        msScaleCompanyId = "";
        mbIsCanEdit = false;
        mbIsCanDelete = false;
        mbIsDeleted = false;
        mnFkDbmsTypeId = 0;
        mnFkUnitId = 0;
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
            sql = "SELECT * FROM erp.cfgu_sca WHERE id_sca = " + key[0];
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkScaleId = resultSet.getInt("id_sca");
                mnPkScaleId = resultSet.getInt("id_sca");
                msScaleKey = resultSet.getString("sca_key");
                msScale = resultSet.getString("sca");
                msDbmsDriver = resultSet.getString("dbms_driver");
                msHost = resultSet.getString("host");
                msPort = resultSet.getString("port");
                msUserName = resultSet.getString("user_name");
                msUserPassword = resultSet.getString("user_pswd");
                msDbName = resultSet.getString("db_name");
                msScaleCompanyId = resultSet.getString("sca_com_id");
                mbIsCanEdit = resultSet.getBoolean("b_can_edit");
                mbIsCanDelete = resultSet.getBoolean("b_can_del");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkDbmsTypeId = resultSet.getInt("fid_tp_dbms");
                mnFkUnitId = resultSet.getInt("fid_unit");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(Connection connection) {
        return mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}

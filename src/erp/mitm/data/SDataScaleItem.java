/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mitm.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Isabel Servín
 */
public class SDataScaleItem extends SDataRegistry {

    protected int mnPkScaleId;
    protected int mnPkScaleItemId;
    protected java.lang.String msCode;
    protected java.lang.String msName;
    protected boolean mbIsPurchase;
    protected boolean mbIsSales;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;
    
    protected boolean mbAuxInScale;
    
    public SDataScaleItem() {
        super(SDataConstants.ITMU_SCA_ITEM);
        reset();
    }

    public void setPkScaleId(int n) { mnPkScaleId = n; }
    public void setPkScaleItemId(int n) { mnPkScaleItemId = n; }
    public void setCode(java.lang.String s) { msCode = s; }
    public void setName(java.lang.String s) { msName = s; }
    public void setIsPurchase(boolean b) { mbIsPurchase = b; }
    public void setIsSales(boolean b) { mbIsSales = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkScaleId() { return mnPkScaleId; }
    public int getPkScaleItemId() { return mnPkScaleItemId; }
    public java.lang.String getCode() { return msCode; }
    public java.lang.String getName() { return msName; }
    public boolean getIsPurchase() { return mbIsPurchase; }
    public boolean getIsSales() { return mbIsSales; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    public void setAuxInScale(boolean b) { mbAuxInScale = b; }
    
    public boolean getAuxInScale() { return mbAuxInScale; }
    
    public int saveInScale(Connection connection) {
        String sql;
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            if (!mbAuxInScale) {
                sql = "UPDATE erp.itmu_sca_item SET " +
                        "b_del = " + !mbAuxInScale + ", " +
                        "fid_usr_del = " + mnFkUserDeleteId + ", " +
                        "ts_del = NOW() " +
                        "WHERE id_sca = " + mnPkScaleId + " AND id_sca_item = " + mnPkScaleItemId + "";
                connection.createStatement().execute(sql);
            }
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public void setPrimaryKey(Object pk) {
        mnPkScaleId = (int)((Object[]) pk)[0];
        mnPkScaleItemId = (int)((Object[]) pk)[1];
    }

    @Override
    public Object getPrimaryKey() {
        return new Object[] { mnPkScaleId, mnPkScaleItemId };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkScaleId = 0;
        mnPkScaleItemId = 0;
        msCode = "";
        msName = "";
        mbIsPurchase = false;
        mbIsSales = false;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
        
        mbAuxInScale = false;
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.itmu_sca_item WHERE id_sca = " + key[0] + " "
                    + "AND id_sca_item = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkScaleId = resultSet.getInt("id_sca");
                mnPkScaleItemId = resultSet.getInt("id_sca_item");
                msCode = resultSet.getString("code");
                msName = resultSet.getString("name");
                mbIsPurchase = resultSet.getBoolean("b_pur");
                mbIsSales = resultSet.getBoolean("b_sal");
                mbIsDeleted = resultSet.getBoolean("b_del");
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
        String sql;
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        ResultSet resultSet;
        
        try {
            sql = "SELECT * FROM erp.itmu_sca_item WHERE id_sca = " + mnPkScaleId + " AND code = '" + msCode + "'";
            resultSet = connection.createStatement().executeQuery(sql);
            if (resultSet.next()) {
                mnPkScaleItemId = resultSet.getInt("id_sca_item");
                sql = "UPDATE erp.itmu_sca_item SET " +
                        "name = '" + msName + "', " + 
                        "b_pur = " + (resultSet.getBoolean("b_pur") == true || mbIsPurchase == true) + ", " +
                        "b_sal = " + (resultSet.getBoolean("b_sal") == true || mbIsSales == true) + ", " +
                        "fid_usr_edit = " + mnFkUserEditId + ", " +
                        "ts_edit = NOW() " +
                        "WHERE id_sca = " + mnPkScaleId + " AND id_sca_item = " + mnPkScaleItemId + ""; 
            }
            else {
                mnPkScaleItemId = 0;
                sql = "SELECT COALESCE(MAX(id_sca_item), 0) + 1 FROM erp.itmu_sca_item";
                resultSet = connection.createStatement().executeQuery(sql);
                if (resultSet.next()) {
                    mnPkScaleItemId = resultSet.getInt(1);
                }
                sql = "INSERT INTO erp.itmu_sca_item VALUES(" +
                        mnPkScaleId + ", " +
                        mnPkScaleItemId + ", " +
                        "'" + msCode + "', " +
                        "'" + msName + "', " +
                        mbIsPurchase + ", " + 
                        mbIsSales + ", " +
                        mbIsDeleted + ", " +
                        mnFkUserNewId + ", " +
                        SUtilConsts.USR_NA_ID + ", " +
                        SUtilConsts.USR_NA_ID + ", " +
                        "NOW(), " +
                        "NOW(), " +
                        "NOW() " +
                        ")";
            }
            
            connection.createStatement().execute(sql);
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
}

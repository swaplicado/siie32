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

/**
 *
 * @author Isabel Servín
 */
public class SDataScaleItemMap extends SDataRegistry {

    protected int mnPkScaleId;
    protected int mnPkScaleItemId;
    protected int mnPkItemId;
    protected boolean mbIsDefault;
    protected int mnFkUserId;
    protected java.util.Date mtUserTs;
    
    public SDataScaleItemMap() {
        super(SDataConstants.ITMU_SCA_ITEM_MAP);
        reset();
    }
    
    public void setPkScaleId(int n) { mnPkScaleId = n; }
    public void setPkScaleItemId(int n) { mnPkScaleItemId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setIsDefault(boolean b) { mbIsDefault = b; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setUserTs(java.util.Date t) { mtUserTs = t; }

    public int getPkScaleId() { return mnPkScaleId; }
    public int getPkScaleItemId() { return mnPkScaleItemId; }
    public int getPkItemId() { return mnPkItemId; }
    public boolean getIsDefault() { return mbIsDefault; }
    public int getFkUserId() { return mnFkUserId; }
    public java.util.Date getUserTs() { return mtUserTs; }

    @Override
    public void setPrimaryKey(Object pk) {
        mnPkScaleId = ((int[]) pk)[0];
        mnPkScaleItemId = ((int[]) pk)[1];
        mnPkItemId = ((int[]) pk)[2];
    }

    @Override
    public Object getPrimaryKey() {
        return new Object[] { mnPkScaleId, mnPkScaleItemId, mnPkItemId };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkScaleId = 0;
        mnPkScaleItemId = 0;
        mnPkItemId = 0;
        mbIsDefault = false;
        mnFkUserId = 0;
        mtUserTs = null;
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.itmu_sca_item_map WHERE id_sca = " + key[0] + " "
                    + "AND id_sca_item = " + key[1] + " "
                    + "AND id_item = " + key[2];
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkScaleId = resultSet.getInt("id_sca");
                mnPkScaleItemId = resultSet.getInt("id_sca_item");
                mnPkItemId = resultSet.getInt("id_item");
                mbIsDefault = resultSet.getBoolean("b_def");
                mnFkUserId = resultSet.getInt("fid_usr");
                mtUserTs = resultSet.getTimestamp("ts");

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

        try {
            if (mbIsRegistryNew) {
                sql = "INSERT INTO erp.itmu_sca_item_map VALUES(" +
                        mnPkScaleId + ", " +
                        mnPkScaleItemId + ", " +
                        mnPkItemId + ", " +
                        mbIsDefault + ", " +
                        mnFkUserId + ", " + 
                        "NOW() " +
                        ")";
            }
            else {
                sql = "UPDATE erp.itmu_sca_item_map SET " +
                        "b_def = " + mbIsDefault + ", " +
                        "fid_usr = " + mnFkUserId + ", " +
                        "ts = NOW() " +
                        "WHERE id_sca = " + mnPkScaleId + " " +
                        "AND id_sca_item = " + mnPkScaleItemId + " " +
                        "AND id_item = " + mnPkItemId;
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
}

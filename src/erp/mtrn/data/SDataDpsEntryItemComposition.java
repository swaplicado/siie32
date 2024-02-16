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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Isabel Servín
 */
public class SDataDpsEntryItemComposition extends SDataRegistry {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected int mnPkItemId;
    protected double mdPercentage;
    protected double mdQuantity;
    protected double mdOriginalQuantity;
    
    public SDataDpsEntryItemComposition() {
        super(SDataConstants.TRN_DPS_ETY_ITEM_COMP);
        reset();
    }
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPercentage(double d) { mdPercentage = d; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setOriginalQuantity(double d) { mdOriginalQuantity = d; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public int getPkItemId() { return mnPkItemId; }
    public double getPercentage() { return mdPercentage; }
    public double getQuantity() { return mdQuantity; }
    public double getOriginalQuantity() { return mdOriginalQuantity; }
    
    @Override
    public void setPrimaryKey(Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkEntryId = ((int[]) pk)[2];
        mnPkItemId = ((int[]) pk)[3];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId, mnPkItemId };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        mnPkItemId = 0;
        mdPercentage = 0;
        mdQuantity = 0;
        mdOriginalQuantity = 0;
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = "SELECT * FROM trn_dps_ety_item_comp " +
                    "WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " AND id_ety = " + key[2] + " AND id_item = " + key[3] + " ";
            
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                mnPkEntryId = resultSet.getInt("id_ety");
                mnPkItemId = resultSet.getInt("id_item");
                mdPercentage = resultSet.getDouble("per");
                mdQuantity = resultSet.getDouble("qty");
                mdOriginalQuantity = resultSet.getDouble("orig_qty");
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (Exception e) {
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
                sql = "INSERT INTO trn_dps_ety_item_comp VALUES (" +
                        mnPkYearId + ", " +
                        mnPkDocId + ", " +
                        mnPkEntryId + ", " +
                        mnPkItemId + ", " +
                        mdPercentage + ", " +
                        mdQuantity + ", " + 
                        mdOriginalQuantity + ")";
            }
            else {
                sql = "UPDATE trn_dps_ety_item_comp SET " +
                        //"id_year = " + mnPkYearId + ", " +
                        //"id_doc = " + mnPkDocId + ", " +
                        //"id_ety = " + mnPkEntryId + ", " +
                        "id_item = " + mnPkItemId + ", " +
                        "per = " + mdPercentage + ", " +
                        "qty = " + mdQuantity + ", " +
                        "orig_qty = " + mdOriginalQuantity + ", " +
                        "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId + " ";
            }
            
            connection.createStatement().execute(sql);
            
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return new Date();
    }
}

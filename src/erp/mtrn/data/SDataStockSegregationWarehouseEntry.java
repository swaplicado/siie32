package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.lib.db.SDbConsts;

/**
 *
 * @author Edwin Carmona
 */
public class SDataStockSegregationWarehouseEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    protected int mnPkStockSegregationId;
    protected int mnPkWarehouseId;
    protected int mnPkEntryId;
    protected double mdQuantityIncrement;
    protected double mdQuantityDecrement;
    protected int mnFkStockSegregationMovementTypeId;
    protected int mnFkYearId;
    protected int mnFkItemId;
    protected int mnFkUnitId;

    public SDataStockSegregationWarehouseEntry() {
        super(SDataConstants.TRN_STK_SEG_WHS_ETY);
        reset();
    }

    public void setPkStockSegregationId(int n) { mnPkStockSegregationId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setQuantityIncrement(double d) { mdQuantityIncrement = d; }
    public void setQuantityDecrement(double d) { mdQuantityDecrement = d; }
    public void setFkStockSegregationMovementTypeId(int n) { mnFkStockSegregationMovementTypeId = n; }
    public void setFkYearId(int n) { mnFkYearId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }

    public int getPkStockSegregationId() { return mnPkStockSegregationId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public double getQuantityIncrement() { return mdQuantityIncrement; }
    public double getQuantityDecrement() { return mdQuantityDecrement; }
    public int getFkStockSegregationMovementTypeId() { return mnFkStockSegregationMovementTypeId; }
    public int getFkYearId() { return mnFkYearId; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }

    public String getSqlWhere() {
        return "WHERE id_stk_seg = " + mnPkStockSegregationId + " AND id_whs = " + mnPkWarehouseId + " AND id_ety = " + mnPkEntryId + " ";
    }

    public String getSqlWhere(int[] pk) {
        return "WHERE id_stk_seg = " + pk[0] + " AND id_whs = " + pk[1] + " AND id_ety = " + pk[2] + " ";
    }

    public void computeNewPrimaryKey(Connection connection) throws SQLException, Exception {
        String sql;
        ResultSet resultSet = null;

        mnPkEntryId = 0;

        sql = "SELECT COALESCE(MAX(id_ety), 0) + 1 FROM trn_stk_seg_whs_ety";
        
        resultSet = connection.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            mnPkEntryId = resultSet.getInt(1);
        }
    }
    
    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStockSegregationId, mnPkWarehouseId };
    }

    @Override
    public void setPrimaryKey(Object pk) {
        mnPkStockSegregationId = ((int[]) pk)[0];
        mnPkWarehouseId = ((int[]) pk)[1];
    }

    @Override
    public void reset() {
        mnPkStockSegregationId = 0;
        mnPkWarehouseId = 0;
        mnPkEntryId = 0;
        mdQuantityIncrement = 0;
        mdQuantityDecrement = 0;
        mnFkStockSegregationMovementTypeId = 0;
        mnFkYearId = 0;
        mnFkItemId = 0;
        mnFkUnitId = 0;
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_stk_seg_whs_ety " + getSqlWhere(key);
            
            resultSet = statement.getConnection().createStatement().executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                mnPkStockSegregationId = resultSet.getInt("id_stk_seg");
                mnPkWarehouseId = resultSet.getInt("id_whs");
                mnPkEntryId = resultSet.getInt("id_ety");
                mdQuantityIncrement = resultSet.getDouble("qty_inc");
                mdQuantityDecrement = resultSet.getDouble("qty_dec");
                mnFkStockSegregationMovementTypeId = resultSet.getInt("fid_tp_stk_seg_mov");
                mnFkYearId = resultSet.getInt("fid_year");
                mnFkItemId = resultSet.getInt("fid_item");
                mnFkUnitId = resultSet.getInt("fid_unit");

                // Finish registry reading:
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
                computeNewPrimaryKey(connection);

                sql = "INSERT INTO trn_stk_seg_whs_ety VALUES (" +
                        mnPkStockSegregationId + ", " + 
                        mnPkWarehouseId + ", " + 
                        mnPkEntryId + ", " + 
                        mdQuantityIncrement + ", " +
                        mdQuantityDecrement + ", " +
                        mnFkStockSegregationMovementTypeId + ", " +
                        mnFkYearId + ", " + 
                        mnFkItemId + ", " + 
                        mnFkUnitId +
                        ")";
                
            }
            else {
                throw new Exception("El registro no puede modificarse.");
            }

            connection.createStatement().execute(sql);

            // Finish registry saving:
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

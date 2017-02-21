package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.db.SDbConsts;

/**
 *
 * @author Edwin Carmona
 */
public class SDataStockSegregationWarehouse extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    protected int mnPkStockSegregationId;
    protected int mnPkWarehouseId;
    protected int mnFkCompanyBranchId;
    protected int mnFkWarehouseId;

    protected ArrayList<SDataStockSegregationWarehouseEntry> maChildEntries;
    
    public SDataStockSegregationWarehouse() {
        super(SDataConstants.TRN_STK_SEG_WHS);
        maChildEntries = new ArrayList<>();
        reset();
    }

    public void setPkStockSegregationId(int n) { mnPkStockSegregationId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkWarehouseId(int n) { mnFkWarehouseId = n; }

    public int getPkStockSegregationId() { return mnPkStockSegregationId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkWarehouseId() { return mnFkWarehouseId; }
    
    private ArrayList<SDataStockSegregationWarehouseEntry> readEntries(final Statement statement) throws Exception {
        String sql;
        ResultSet resultSet = null;
        ArrayList<SDataStockSegregationWarehouseEntry> entries = new ArrayList<>();

        sql = "SELECT id_ety FROM trn_stk_seg_whs_ety " 
                + getSqlWhere()
                + "ORDER BY id_ety; ";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            SDataStockSegregationWarehouseEntry ety = new SDataStockSegregationWarehouseEntry();
            ety.read(new int[] { mnPkStockSegregationId, mnPkWarehouseId, resultSet.getInt(1) }, statement.getConnection().createStatement());
            entries.add(ety);
        }
        
        return entries;
    }
    
    private void deleteEntries(final Connection connection) throws Exception {
        String sql;
        sql = "DELETE FROM trn_stk_seg_whs_ety " + getSqlWhere();
        
        connection.createStatement().execute(sql);
    }

    public ArrayList<SDataStockSegregationWarehouseEntry> getChildEntries() { return maChildEntries; }

    public String getSqlWhere() {
        return "WHERE id_stk_seg = " + mnPkStockSegregationId + " AND id_whs = " + mnPkWarehouseId + " ";
    }
    
    public String getSqlWhere(int[] pk) {
        return "WHERE id_stk_seg = " + pk[0] + " AND id_whs = " + pk[1] + " ";
    }
    
    @Override
    public int delete(final Connection connection) {
        String sql;
        
        try {
            this.deleteEntries(connection);
            sql = "DELETE FROM trn_stk_seg_whs " + getSqlWhere();
            connection.createStatement().execute(sql);
            
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_OK;
        }
        catch (Exception e) {
             mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
 
        return mnLastDbActionResult;
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
        mnFkCompanyBranchId = 0;
        mnFkWarehouseId = 0;

        maChildEntries.clear();
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_stk_seg_whs " + getSqlWhere(key);
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                mnPkStockSegregationId = resultSet.getInt("id_stk_seg");
                mnPkWarehouseId = resultSet.getInt("id_whs");
                mnFkCompanyBranchId = resultSet.getInt("fid_cob");
                mnFkWarehouseId = resultSet.getInt("fid_whs");

                // Read aswell child registries:

                maChildEntries.addAll(readEntries(statement.getConnection().createStatement()));

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

                sql = "INSERT INTO trn_stk_seg_whs VALUES (" +
                        mnPkStockSegregationId + ", " + 
                        mnPkWarehouseId + ", " + 
                        mnFkCompanyBranchId + ", " + 
                        mnFkWarehouseId +
                        ")";

                connection.createStatement().execute(sql);
                
                // c) delete former child registries:
                this.deleteEntries(connection);

                // d) save new child registries:
                for (SDataStockSegregationWarehouseEntry ety : maChildEntries) {
                    ety.setPkStockSegregationId(mnPkStockSegregationId);
                    ety.setPkWarehouseId(mnPkWarehouseId);
                    ety.setIsRegistryNew(true);
                    ety.save(connection);
                }
            }
            else {
                throw new Exception("El registro no puede modificarse.");
            }

            // Finish registry saving:
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

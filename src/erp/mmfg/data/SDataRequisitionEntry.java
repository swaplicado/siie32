/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.util.Date;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataRequisitionEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkReqYearId;
    protected int mnPkReqId;
    protected int mnPkEntryId;
    protected double mdQuantity;
    protected int mnFkItemId;
    
    protected java.lang.String msDbmsItem;
    protected java.lang.String msDbmsItemKey;
    protected java.lang.String msDbmsItemUnitSymbol;
    
    public SDataRequisitionEntry() {
        super(SDataConstants.MFG_REQ_ETY);
        reset();
    }

    public void setPkReqYearId(int n) { mnPkReqYearId = n; }
    public void setPkReqId(int n) { mnPkReqId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    
    public int getPkReqYearId() { return mnPkReqYearId; }
    public int getPkReqId() { return mnPkReqId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public double getQuantity() { return mdQuantity; }
    public int getFkItemId() { return mnFkItemId; }
    
    public void setDbmsItem(java.lang.String s) { msDbmsItem = s; }
    public void setDbmsItemKey(java.lang.String s) { msDbmsItemKey = s; }
    public void setDbmsItemUnitSymbol(java.lang.String s) { msDbmsItemUnitSymbol = s; }

    public java.lang.String getDbmsItem() { return msDbmsItem; }
    public java.lang.String getDbmsItemKey() { return msDbmsItemKey; }
    public java.lang.String getDbmsItemUnitSymbol() { return msDbmsItemUnitSymbol; }
    
    @Override
    public void reset() {
        super.resetRegistry();

        mnPkReqYearId = 0;
        mnPkReqId = 0;
        mnPkEntryId = 0;
        mdQuantity = 0;
        mnFkItemId = 0;
        
        msDbmsItem = "";
        msDbmsItemKey = "";
        msDbmsItemUnitSymbol = "";
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object key) {
        mnPkReqYearId = ((int[]) key)[0];
        mnPkReqId = ((int[]) key)[1];
        mnPkEntryId = ((int[]) key)[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkReqYearId, mnPkReqId, mnPkEntryId };
    }
    
    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        
        ResultSet resultSet = null;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = "SELECT r.*, i.item_key, i.item, u.symbol " +
                "FROM mfg_req_ety AS r " +
                "INNER JOIN erp.itmu_item AS i ON r.fid_item = i.id_item " +
                "INNER JOIN erp.itmu_unit as u ON i.fid_unit = u.id_unit " +
                "WHERE r.id_req_year = " + key[0] + " AND r.id_req = " + key[1] + " AND r.id_ety = " + key[2];

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkReqYearId = resultSet.getInt("r.id_req_year");
                mnPkReqId = resultSet.getInt("r.id_req");
                mnPkEntryId = resultSet.getInt("r.id_ety");
                mdQuantity = resultSet.getDouble("r.qty");
                mnFkItemId = resultSet.getInt("r.fid_item");
                
                msDbmsItem = resultSet.getString("i.item");
                msDbmsItemKey = resultSet.getString("i.item_key");
                msDbmsItemUnitSymbol = resultSet.getString("u.symbol");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int i = 0;
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mfg_req_ety_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkReqYearId);
            callableStatement.setInt(nParam++, mnPkReqId);
            callableStatement.setInt(nParam++, mnPkEntryId);
            callableStatement.setDouble(nParam++, mdQuantity);
            callableStatement.setInt(nParam++, mnFkItemId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkEntryId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

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
    public int delete(java.sql.Connection connection) {
        String sSql = "";

        Statement statement = null;

        try {
            sSql = "DELETE FROM mfg_req_ety WHERE id_req_year = " + mnPkReqYearId + " AND id_req = " + mnPkReqId + " ";
            statement = connection.createStatement();
            statement.executeUpdate(sSql);

            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}

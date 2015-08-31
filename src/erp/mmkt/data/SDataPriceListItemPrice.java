/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.util.Date;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataPriceListItemPrice extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkPriceListId;
    protected int mnPkItemId;
    protected double mdPrice;

    protected java.lang.String msDbmsItem;
    protected java.lang.String msDbmsItemKey;

    public SDataPriceListItemPrice() {
        super(SDataConstants.MKT_PLIST_PRICE);
        reset();
    }

    public void setPkPriceListId(int n) { mnPkPriceListId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPrice(double d) { mdPrice = d; }

    public void setDbmsItem(java.lang.String s) { msDbmsItem = s; }
    public void setDbmsItemKey(java.lang.String s) { msDbmsItemKey = s; }

    public int getPkPriceListId() { return mnPkPriceListId; }
    public int getPkItemId() { return mnPkItemId; }
    public double getPrice() { return mdPrice; }

    public java.lang.String getDbmsItem() { return msDbmsItem; }
    public java.lang.String getDbmsItemKey() { return msDbmsItemKey; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkPriceListId = ((int[]) pk)[0];
        mnPkItemId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkPriceListId, mnPkItemId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkPriceListId = 0;
        mnPkItemId = 0;
        mdPrice = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT pr.id_plist, pr.id_item, pr.price, i.item_key, i.item " +
                "FROM mkt_plist_price AS pr " +
                "INNER JOIN erp.itmu_item AS i ON pr.id_item = i.id_item " +
                "WHERE pr.id_plist = " + key[0] + " AND pr.id_item = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkPriceListId = resultSet.getInt("pr.id_plist");
                mnPkItemId = resultSet.getInt("pr.id_item");
                mdPrice = resultSet.getDouble("pr.price");

                msDbmsItem = resultSet.getString("i.item");
                msDbmsItemKey = resultSet.getString("i.item_key");
                
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
    public int save(java.sql.Connection connection) {
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mkt_plist_item_save(" +
                    "?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkPriceListId);
            callableStatement.setInt(nParam++, mnPkItemId);
            callableStatement.setDouble(nParam++, mdPrice);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

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

    public int del(java.sql.Connection connection) {
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mkt_plist_item_del(" +
                    "?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkPriceListId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();
            
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            mbIsRegistryNew = false;
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

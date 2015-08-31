/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 *
 * @author Alfonso Flores
 */
public class SDataItemBarcode extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkItemId;
    protected int mnPkBarcodeId;
    protected java.lang.String msBarcode;
    protected boolean mbIsDeleted;

    public SDataItemBarcode() {
        super(SDataConstants.ITMU_ITEM_BARC);
        reset();
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkBarcodeId(int n) { mnPkBarcodeId = n; }
    public void setBarcode(java.lang.String s) { msBarcode = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkBarcodeId() { return mnPkBarcodeId; }
    public java.lang.String getBarcode() { return msBarcode; }
    public boolean getIsDeleted() { return mbIsDeleted; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkItemId = ((int[]) pk)[0];
        mnPkBarcodeId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkItemId, mnPkBarcodeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkItemId = 0;
        mnPkBarcodeId = 0;
        msBarcode = "";
        mbIsDeleted = false;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.itmu_item_barc WHERE id_item = " + key[0] + " AND id_barc = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkItemId = resultSet.getInt("id_item");
                mnPkBarcodeId = resultSet.getInt("id_barc");
                msBarcode = resultSet.getString("barc");
                mbIsDeleted = resultSet.getBoolean("b_del");

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
                    "{ CALL erp.itmu_item_barc_save(" +
                    "?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkItemId);
            callableStatement.setInt(nParam++, mnPkBarcodeId);
            callableStatement.setString(nParam++, msBarcode);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkBarcodeId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return null;
    }
}

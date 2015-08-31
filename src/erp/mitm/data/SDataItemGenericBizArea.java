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
public class SDataItemGenericBizArea extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkItemGenericId;
    protected int mnPkBizAreaId;

    public SDataItemGenericBizArea() {
        super(SDataConstants.ITMU_IGEN);
        reset();
    }

    public void setPkItemGenericId(int n) { mnPkItemGenericId = n; }
    public void setPkBizAreaId(int n) { mnPkBizAreaId = n; }

    public int getPkItemGenericId() { return mnPkItemGenericId; }
    public int getPkBizAreaId() { return mnPkBizAreaId; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkItemGenericId = ((int[]) pk)[0];
        mnPkBizAreaId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkItemGenericId, mnPkBizAreaId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkItemGenericId = 0;
        mnPkBizAreaId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.itmu_igen_ba WHERE id_igen = " + key[0] + " AND id_ba = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkItemGenericId = resultSet.getInt("id_igen");
                mnPkBizAreaId = resultSet.getInt("id_ba");

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
                    "{ CALL erp.itmu_igen_ba_save(" +
                    "?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkItemGenericId);
            callableStatement.setInt(nParam++, mnPkBizAreaId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

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
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
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

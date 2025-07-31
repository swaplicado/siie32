/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import java.sql.ResultSet;

/**
 *
 * @author Juan Barajas
 */
public class SDataUserFunctionalArea extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkUserId;
    protected int mnPkFunctionalAreaId;
    
    public SDataUserFunctionalArea() {
        super(SModConsts.USR_USR_FUNC);
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkFunctionalAreaId(int n) { mnPkFunctionalAreaId = n; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkFunctionalAreaId() { return mnPkFunctionalAreaId; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkUserId = ((int[]) pk)[0];
        mnPkFunctionalAreaId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkUserId, mnPkFunctionalAreaId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkUserId = 0;
        mnPkFunctionalAreaId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM usr_usr_func where id_usr = " + key[0] + " AND id_func = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkUserId = resultSet.getInt("id_usr");
                mnPkFunctionalAreaId = resultSet.getInt("id_func");

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
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (mbIsRegistryNew) {
                sql = "SELECT COUNT(*) FROM usr_usr_func WHERE id_usr = " + mnPkUserId + " AND id_func = " + mnPkFunctionalAreaId + " ";
                resultSet = connection.createStatement().executeQuery(sql);

                if (resultSet.next()) {
                    mbIsRegistryNew = resultSet.getInt(1) == 0;
                }
            }

            if (mbIsRegistryNew) {
                sql = "INSERT INTO usr_usr_func VALUES (" +
                        mnPkUserId + ", " +
                        mnPkFunctionalAreaId + " " +
                        ")";
            }
            else {
                /*
                sql = "UPDATE usr_usr_func SET " +
                        //"id_usr = " + mnPkUserId + ", " +
                        //"id_func = " + mnPkFunctionalAreaId + " ";
                */
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
    public java.util.Date getLastDbUpdate() {
        return null;
    }
}

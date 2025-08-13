/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import sa.lib.db.SDbConsts;

/**
 *
 * @author Sergio Flores
 */
public class SDataUserFunctionalSubArea extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkUserId;
    protected int mnPkFunctionalSubAreaId;
    
    public SDataUserFunctionalSubArea() {
        super(SModConsts.USR_USR_FUNC);
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkFunctionalSubAreaId(int n) { mnPkFunctionalSubAreaId = n; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkFunctionalSubAreaId() { return mnPkFunctionalSubAreaId; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkUserId = ((int[]) pk)[0];
        mnPkFunctionalSubAreaId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkUserId, mnPkFunctionalSubAreaId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkUserId = 0;
        mnPkFunctionalSubAreaId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM usr_usr_func_sub where id_usr = " + key[0] + " AND id_func_sub = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkUserId = resultSet.getInt("id_usr");
                mnPkFunctionalSubAreaId = resultSet.getInt("id_func_sub");

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

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (mbIsRegistryNew) {
                sql = "INSERT INTO usr_usr_func_sub VALUES (" +
                        mnPkUserId + ", " +
                        mnPkFunctionalSubAreaId + " " +
                        ")";
            }
            else {
                throw new Exception(SDbConsts.ERR_MSG_REG_NON_UPDATABLE);
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

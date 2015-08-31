/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Alfonso Flores
 */
public class SDataAccessCompanyBranchEntityUniversal extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkUserId;
    protected int mnPkCompanyBranchId;
    protected int mnPkEntityCategoryId;

    public SDataAccessCompanyBranchEntityUniversal() {
        super(SDataConstants.USRU_ACCESS_COB_ENT_UNIV);
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkCompanyBranchId(int n) { mnPkCompanyBranchId = n; }
    public void setPkEntityCategoryId(int n) { mnPkEntityCategoryId = n; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkCompanyBranchId() { return mnPkCompanyBranchId; }
    public int getPkEntityCategoryId() { return mnPkEntityCategoryId; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkUserId = ((int[]) pk)[0];
        mnPkCompanyBranchId = ((int[]) pk)[1];
        mnPkEntityCategoryId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkUserId, mnPkCompanyBranchId, mnPkEntityCategoryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkUserId = 0;
        mnPkCompanyBranchId = 0;
        mnPkEntityCategoryId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.usru_access_cob_ent_univ WHERE id_usr = " + key[0] + " AND id_cob = " + key[1] +
                    " AND id_ct_ent = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkUserId = resultSet.getInt("id_usr");
                mnPkCompanyBranchId = resultSet.getInt("id_cob");
                mnPkEntityCategoryId = resultSet.getInt("id_ct_ent");

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
                    "{ CALL erp.usru_access_cob_ent_univ_save(" +
                    "?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkUserId);
            callableStatement.setInt(nParam++, mnPkCompanyBranchId);
            callableStatement.setInt(nParam++, mnPkEntityCategoryId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
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

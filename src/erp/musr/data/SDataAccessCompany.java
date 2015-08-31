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
public class SDataAccessCompany extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkUserId;
    protected int mnPkCompanyId;
    protected boolean mbIsDefault;
    protected boolean mbIsUniversal;

    protected java.lang.String msDbmsCompany;

    public SDataAccessCompany() {
        super(SDataConstants.USRU_ACCESS_CO);
        reset();
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkCompanyId(int n) { mnPkCompanyId = n; }
    public void setIsDefault(boolean b) { mbIsDefault = b; }
    public void setIsUniversal(boolean b) { mbIsUniversal = b; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkCompanyId() { return mnPkCompanyId; }
    public boolean getIsDefault() { return mbIsDefault; }
    public boolean getIsUniversal() { return mbIsUniversal; }

    public void setDbmsCompany(java.lang.String s) { msDbmsCompany = s; }
    public java.lang.String getDbmsCompany() {return msDbmsCompany; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkUserId = ((int[]) pk)[0];
        mnPkCompanyId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkUserId,  mnPkCompanyId};
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkUserId = 0;
        mnPkCompanyId = 0;
        mbIsDefault = false;
        mbIsUniversal = false;

        msDbmsCompany = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT access_co.*, bp.bp " +
                    "FROM erp.usru_access_co AS access_co " +
                    "INNER JOIN erp.bpsu_bp AS bp ON " +
                    "access_co.id_co = bp.id_bp " +
                    "WHERE access_co.id_usr = " + key[0] + " AND access_co.id_co = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkUserId = resultSet.getInt("access_co.id_usr");
                mnPkCompanyId = resultSet.getInt("access_co.id_co");
                mbIsDefault = resultSet.getBoolean("access_co.b_def");
                mbIsUniversal = resultSet.getBoolean("access_co.b_univ");

                msDbmsCompany = resultSet.getString("bp.bp");

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
                    "{ CALL erp.usru_access_co_save(" +
                    "?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkUserId);
            callableStatement.setInt(nParam++, mnPkCompanyId);
            callableStatement.setBoolean(nParam++, mbIsDefault);
            callableStatement.setBoolean(nParam++, mbIsUniversal);
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

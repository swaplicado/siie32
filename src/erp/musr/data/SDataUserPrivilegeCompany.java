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
public class SDataUserPrivilegeCompany extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkUserId;
    protected int mnPkCompanyId;
    protected int mnPkPrivilegeId;
    protected int mnFkLevelTypeId;

    protected int mnDbmsFkTypePrivilegeId;
    protected java.lang.String msDbmsCompany;
    protected java.lang.String msDbmsPrivilege;
    protected java.lang.String msDbmsLevelType;

    public SDataUserPrivilegeCompany() {
        super(SDataConstants.USRU_PRV_CO);
        reset();
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkCompanyId(int n) { mnPkCompanyId = n; }
    public void setPkPrivilegeId(int n) { mnPkPrivilegeId = n; }
    public void setFkLevelTypeId(int n) { mnFkLevelTypeId = n; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkCompanyId() { return mnPkCompanyId; }
    public int getPkPrivilegeId() { return mnPkPrivilegeId; }
    public int getFkLevelTypeId() { return mnFkLevelTypeId; }

    public void setDbmsFkTypePrivilegeId(int n) { mnDbmsFkTypePrivilegeId = n; }
    public void setDbmsCompany(java.lang.String s) { msDbmsCompany = s; }
    public void setDbmsPrivilege(java.lang.String s) { msDbmsPrivilege = s; }
    public void setDbmsLevelType(java.lang.String s) { msDbmsLevelType = s; }

    public int getDbmsFkTypePrivilegeId() { return mnDbmsFkTypePrivilegeId; }
    public java.lang.String getDbmsCompany() { return msDbmsCompany; }
    public java.lang.String getDbmsPrivilege() { return msDbmsPrivilege; }
    public java.lang.String getDbmsLevelType() { return msDbmsLevelType; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkUserId = ((int[]) pk)[0];
        mnPkCompanyId = ((int[]) pk)[1];
        mnPkPrivilegeId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkUserId,  mnPkCompanyId, mnPkPrivilegeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkUserId = 0;
        mnPkCompanyId = 0;
        mnPkPrivilegeId = 0;
        mnFkLevelTypeId = 0;

        mnDbmsFkTypePrivilegeId = 0;
        msDbmsCompany = "";
        msDbmsPrivilege = "";
        msDbmsLevelType = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT prv.*,co.bp, p.prv, p.fid_tp_prv, tp.tp_lev " +
                    "FROM erp.usru_prv_co AS prv " +
                    "INNER JOIN erp.usrs_prv AS p ON " +
                    "prv.id_prv = p.id_prv " +
                    "INNER JOIN erp.bpsu_bp AS co ON " +
                    "prv.id_co = co.id_bp " +
                    "INNER JOIN erp.usrs_tp_lev AS tp ON " +
                    "prv.fid_tp_lev = tp.id_tp_lev " +
                    "WHERE prv.id_usr = " + key[0] + " AND prv.id_co = " + key[1] + " AND prv.id_prv = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkUserId = resultSet.getInt("prv.id_usr");
                mnPkCompanyId = resultSet.getInt("prv.id_co");
                mnPkPrivilegeId = resultSet.getInt("prv.id_prv");
                mnFkLevelTypeId = resultSet.getInt("prv.fid_tp_lev");

                msDbmsCompany = resultSet.getString("co.bp");
                mnDbmsFkTypePrivilegeId = resultSet.getInt("p.fid_tp_prv");
                msDbmsPrivilege = resultSet.getString("p.prv");
                msDbmsLevelType = resultSet.getString("tp.tp_lev");

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
                    "{ CALL erp.usru_prv_co_save(" +
                    "?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkUserId);
            callableStatement.setInt(nParam++, mnPkCompanyId);
            callableStatement.setInt(nParam++, mnPkPrivilegeId);
            callableStatement.setInt(nParam++, mnFkLevelTypeId);
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

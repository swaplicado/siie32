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
public class SDataUserRoleCompany extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkUserId;
    protected int mnPkCompanyId;
    protected int mnPkRoleId;
    protected int mnFkLevelTypeId;

    protected int mnDbmsFkTypeRoleId;
    protected java.lang.String msDbmsCompany;
    protected java.lang.String msDbmsTypeRole;
    protected java.lang.String msDbmsRole;
    protected java.lang.String msDbmsLevelType;

    public SDataUserRoleCompany() {
        super(SDataConstants.USRU_ROL_CO);
        reset();
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkCompanyId(int n) { mnPkCompanyId = n; }
    public void setPkRoleId(int n) { mnPkRoleId = n; }
    public void setFkLevelTypeId(int n) { mnFkLevelTypeId = n; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkCompanyId() { return mnPkCompanyId; }
    public int getPkRoleId() { return mnPkRoleId; }
    public int getFkLevelTypeId() { return mnFkLevelTypeId; }

    public void setDbmsFkTypeRoleId(int n) { mnDbmsFkTypeRoleId = n; }
    public void setDbmsCompany(java.lang.String s) { msDbmsCompany = s; }
    public void setDbmsTypeRole(java.lang.String s) { msDbmsTypeRole = s; }
    public void setDbmsRole(java.lang.String s) { msDbmsRole = s; }
    public void setDbmsLevelType(java.lang.String s) { msDbmsLevelType = s; }

    public int getDbmsFkTypeRoleId() { return mnDbmsFkTypeRoleId; }
    public java.lang.String getDbmsCompany() { return msDbmsCompany; }
    public java.lang.String getDbmsTypeRole() { return msDbmsTypeRole; }
    public java.lang.String getDbmsRole() { return msDbmsRole; }
    public java.lang.String getDbmsLevelType() { return msDbmsLevelType; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkUserId = ((int[]) pk)[0];
        mnPkCompanyId = ((int[]) pk)[1];
        mnPkRoleId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkUserId,  mnPkCompanyId, mnPkRoleId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkUserId = 0;
        mnPkCompanyId = 0;
        mnPkRoleId = 0;
        mnFkLevelTypeId = 0;

        mnDbmsFkTypeRoleId = 0;
        msDbmsCompany = "";
        msDbmsTypeRole = "";
        msDbmsRole = "";
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
            sql = "SELECT rol.*, tr.tp_rol, co.bp, r.rol, r.fid_tp_rol, tp.tp_lev " +
                    "FROM erp.usru_rol_co AS rol " +
                    "INNER JOIN erp.usrs_rol AS r ON rol.id_rol = r.id_rol " +
                    "INNER JOIN erp.usrs_tp_rol AS tr ON r.fid_tp_rol = tr.id_tp_rol " +
                    "INNER JOIN erp.bpsu_bp AS co ON rol.id_co = co.id_bp " +
                    "INNER JOIN erp.usrs_tp_lev AS tp ON rol.fid_tp_lev = tp.id_tp_lev " +
                    "WHERE rol.id_usr = " + key[0] + " AND rol.id_co = " + key[1] + " AND rol.id_rol = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkUserId = resultSet.getInt("rol.id_usr");
                mnPkCompanyId = resultSet.getInt("rol.id_co");
                mnPkRoleId = resultSet.getInt("rol.id_rol");
                mnFkLevelTypeId = resultSet.getInt("rol.fid_tp_lev");

                msDbmsCompany = resultSet.getString("co.bp");
                mnDbmsFkTypeRoleId = resultSet.getInt("r.fid_tp_rol");
                msDbmsTypeRole = resultSet.getString("tr.tp_rol");
                msDbmsRole = resultSet.getString("r.rol");
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
                    "{ CALL erp.usru_rol_co_save(" +
                    "?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkUserId);
            callableStatement.setInt(nParam++, mnPkCompanyId);
            callableStatement.setInt(nParam++, mnPkRoleId);
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

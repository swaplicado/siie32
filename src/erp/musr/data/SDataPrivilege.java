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
 * @author Sergio Flores
 */
public class SDataPrivilege extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkPrivilegeId;
    protected java.lang.String msPrivilege;
    protected boolean mbIsTypeLevelRequired;
    protected boolean mbIsDeleted;
    protected int mnFkPrivilegeTypeId;
    protected int mnFkUserId;
    protected java.util.Date mtUserTs;

    public SDataPrivilege() {
        super(SDataConstants.USRS_PRV);
        reset();
    }

    public void setPkPrivilegeId(int n) { mnPkPrivilegeId = n; }
    public void setPrivilege(java.lang.String s) { msPrivilege = s; }
    public void setIsTypeLevelRequired(boolean b) { mbIsTypeLevelRequired = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkPrivilegeTypeId(int n) { mnFkPrivilegeTypeId = n; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setUserTs(java.util.Date t) { mtUserTs = t; }

    public int getPkPrivilegeId() { return mnPkPrivilegeId; }
    public java.lang.String getPrivilege() { return msPrivilege; }
    public boolean getIsTypeLevelRequired() { return mbIsTypeLevelRequired; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkPrivilegeTypeId() { return mnFkPrivilegeTypeId; }
    public int getFkUserId() { return mnFkUserId; }
    public java.util.Date getUserTs() { return mtUserTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkPrivilegeId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkPrivilegeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkPrivilegeId = 0;
        msPrivilege = "";
        mbIsTypeLevelRequired = false;
        mbIsDeleted = false;
        mnFkPrivilegeTypeId = 0;
        mnFkUserId = 0;
        mtUserTs = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.usrs_prv where id_prv = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkPrivilegeId = resultSet.getInt("id_prv");
                msPrivilege = resultSet.getString("prv");
                mbIsTypeLevelRequired = resultSet.getBoolean("b_tp_lev");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkPrivilegeTypeId = resultSet.getInt("fid_tp_prv");
                mnFkUserId = resultSet.getInt("fid_usr");
                mtUserTs = resultSet.getTimestamp("ts");

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
                    "{ CALL erp.usr_prv_save(" +
                    "?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkPrivilegeId);
            callableStatement.setString(nParam++, msPrivilege);
            callableStatement.setBoolean(nParam++, mbIsTypeLevelRequired);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkPrivilegeTypeId);
            callableStatement.setInt(nParam++, mnFkUserId);
            callableStatement.setTimestamp(nParam++, new java.sql.Timestamp(mtUserTs.getTime()));
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

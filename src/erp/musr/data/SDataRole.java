/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Sergio Flores
 */
public class SDataRole extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkRoleId;
    protected java.lang.String msRole;
    protected boolean mbIsDeleted;
    protected int mnFkRoleTypeId;
    protected int mnFkUserId;
    protected java.util.Date mtUserTs;

    protected java.util.Vector<erp.musr.data.SDataPrivilege> mvDbmsPrivileges;

    public SDataRole() {
        super(SDataConstants.USRS_ROL);
        mvDbmsPrivileges = new Vector<SDataPrivilege>();
        reset();
    }

    public void setPkRoleId(int n) { mnPkRoleId = n; }
    public void setRole(java.lang.String s) { msRole = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkRoleTypeId(int n) { mnFkRoleTypeId = n; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setUserTs(java.util.Date t) { mtUserTs = t; }

    public int getPkRoleId() { return mnPkRoleId; }
    public java.lang.String getRole() { return msRole; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkRoleTypeId() { return mnFkRoleTypeId; }
    public int getFkUserId() { return mnFkUserId; }
    public java.util.Date getUserTs() { return mtUserTs; }

    public java.util.Vector<erp.musr.data.SDataPrivilege> getDbmsPrivileges() { return mvDbmsPrivileges; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkRoleId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkRoleId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkRoleId = 0;
        msRole = "";
        mbIsDeleted = false;
        mnFkRoleTypeId = 0;
        mnFkUserId = 0;
        mtUserTs = null;

        mvDbmsPrivileges.clear();
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;
        Statement statementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.usrs_rol where id_rol = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkRoleId = resultSet.getInt("id_rol");
                msRole = resultSet.getString("rol");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkRoleTypeId = resultSet.getInt("fid_tp_rol");
                mnFkUserId = resultSet.getInt("fid_usr");
                mtUserTs = resultSet.getTimestamp("ts");
                
                // Read dependent registries:

                statementAux = statement.getConnection().createStatement();

                // Read aswell role privileges:

                //System.out.println("Reading role privileges (role=" + mnPkRoleId + ")");

                sql = "SELECT id_prv FROM erp.usrs_rol_prv WHERE id_rol = " + mnPkRoleId + " ";
                resultSet = statementAux.executeQuery(sql);
                while (resultSet.next()) {
                    SDataPrivilege privilege = new SDataPrivilege();
                    if (privilege.read(new int[] { resultSet.getInt("id_prv") }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        //System.out.println("Adding privilege (role=" + mnPkRoleId + ", privilege=" + privilege.getPkPrivilegeId() + ")");
                        mvDbmsPrivileges.add(privilege);
                    }
                }

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
                    "{ CALL erp.usr_rol_save(" +
                    "?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkRoleId);
            callableStatement.setString(nParam++, msRole);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkRoleTypeId);
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

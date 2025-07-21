/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.musr.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Isabel Servín
 */
public class SDataUserGroup extends SDataRegistry {
    
    protected int mnPkUserGroupId;
    protected java.lang.String msUserGroup;
    protected boolean mbIsCanEdit;
    protected boolean mbIsCanDelete;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;
    
    protected ArrayList<SDataUserGroupUser> maDbmsUserGroupUsers;

    public SDataUserGroup() {
        super(SDataConstants.USRU_USR_GRP);
        
        maDbmsUserGroupUsers = new ArrayList<>();
        
        reset();
    }

    public void setPkUserGroupId(int n) { mnPkUserGroupId = n; }
    public void setUserGroup(java.lang.String s) { msUserGroup = s; }
    public void setIsCanEdit(boolean b) { mbIsCanEdit = b; }
    public void setIsCanDelete(boolean b) { mbIsCanDelete = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkUserGroupId() { return mnPkUserGroupId; }
    public java.lang.String getUserGroup() { return msUserGroup; }
    public boolean getIsCanEdit() { return mbIsCanEdit; }
    public boolean getIsCanDelete() { return mbIsCanDelete; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    public ArrayList<SDataUserGroupUser> getDbmsUserGroupUsers() { return maDbmsUserGroupUsers; }
    
    @Override
    public void setPrimaryKey(Object pk) {
        mnPkUserGroupId = ((int[]) pk)[0];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkUserGroupId };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkUserGroupId = 0;
        msUserGroup = "";
        mbIsCanEdit = false;
        mbIsCanDelete = false;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
        
        maDbmsUserGroupUsers.clear();
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;
        Statement statementAux;
       
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.usru_usr_grp WHERE id_usr_grp = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkUserGroupId = resultSet.getInt("id_usr_grp");
                msUserGroup = resultSet.getString("usr_grp");
                mbIsCanEdit = resultSet.getBoolean("b_can_edit");
                mbIsCanDelete = resultSet.getBoolean("b_can_del");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");
                
                statementAux = statement.getConnection().createStatement();
                
                // Leer usuarios pertenecientes al grupo de usuarios
                
                sql = "SELECT ugu.id_usr FROM erp.usru_usr_grp_usr AS ugu "
                        + "INNER JOIN erp.usru_usr AS u ON ugu.id_usr = u.id_usr "
                        + "WHERE ugu.id_usr_grp = " + mnPkUserGroupId + " "
                        + "ORDER BY u.usr";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataUserGroupUser ugu = new SDataUserGroupUser();
                    if (ugu.read(new int[] { mnPkUserGroupId, resultSet.getInt(1) }, statementAux) == SLibConstants.DB_ACTION_READ_OK) {
                        maDbmsUserGroupUsers.add(ugu);
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
    public int save(Connection connection) {
        int nParam = 1;
        CallableStatement callableStatement;
        Statement statement;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            callableStatement = connection.prepareCall(
                    "{ CALL erp.usru_usr_grp_save(" + 
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?) }");
            callableStatement.setInt(nParam++, mnPkUserGroupId);
            callableStatement.setString(nParam++, msUserGroup);
            callableStatement.setBoolean(nParam++, mbIsCanEdit);
            callableStatement.setBoolean(nParam++, mbIsCanDelete);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkUserNewId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnPkUserGroupId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                statement = connection.createStatement();
                String sql = "DELETE FROM erp.usru_usr_grp_usr WHERE id_usr_grp = " + mnPkUserGroupId + ";";
                statement.execute(sql);
                
                for (SDataUserGroupUser ugu : maDbmsUserGroupUsers) {
                    ugu.setPkUserGroupId(mnPkUserGroupId);
                    ugu.setIsRegistryNew(true);
                    ugu.save(connection);
                }
                
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
    public Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}

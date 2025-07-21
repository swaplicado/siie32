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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Isabel Servín
 */
public class SDataUserGroupUser extends SDataRegistry {
    
    protected int mnPkUserGroupId;
    protected int mnPkUserId;
    
    protected String msUserName;
    protected boolean mbIsUserDeleted;

    public SDataUserGroupUser() {
        super(SDataConstants.USRU_USR_GRP_USR);
    }
    
    public void setPkUserGroupId(int n) { mnPkUserGroupId = n; }
    public void setPkUserId(int n) { mnPkUserId = n; }
    
    public void setUserName(String s) { msUserName = s; }
    public void setIsUserDeleted(boolean b) { mbIsUserDeleted = b; }

    public int getPkUserGroupId() { return mnPkUserGroupId; }
    public int getPkUserId() { return mnPkUserId; }
    
    public String getUserName() { return msUserName; }
    public boolean getIsUserDeleted() { return mbIsUserDeleted; }
    
    private void readUserInfo(Statement statement) {
        try {
            String sql = "SELECT usr, b_del FROM erp.usru_usr WHERE id_usr = " + mnPkUserId;

            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                msUserName = resultSet.getString(1);
                mbIsUserDeleted = resultSet.getBoolean(2);
                
                if (mbIsUserDeleted) {
                    msUserName += " (eliminado)";
                }
            }
        }
        catch (java.sql.SQLException e) {
            SLibUtilities.printOutException(this, e);
        }
    }

    @Override
    public void setPrimaryKey(Object pk) {
        mnPkUserGroupId = ((int[]) pk)[0];
        mnPkUserId = ((int[]) pk)[1];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkUserGroupId, mnPkUserId };
    }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkUserGroupId = 0;
        mnPkUserId = 0;
        
        msUserName = "";
        mbIsUserDeleted = false;
    }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet;
       
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.usru_usr_grp_usr "
                    + "WHERE id_usr_grp = " + key[0] + " "
                    + "AND id_usr = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkUserGroupId = resultSet.getInt("id_usr_grp");
                mnPkUserId = resultSet.getInt("id_usr");
                
                readUserInfo(statement);
                
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
        String sql = "";
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            if (mbIsRegistryNew) {
                sql = "INSERT INTO erp.usru_usr_grp_usr VALUES(" +
                        mnPkUserGroupId + ", " + 
                        mnPkUserId + " " + 
                        ")"; 
            }
            else {
                // UPDATE, no es necesario
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
    public Date getLastDbUpdate() {
        return null;
    }
}

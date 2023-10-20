/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Edwin Carmona
 */
public class SDataMaterialType extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkMaterialTypeId;
    protected String msCode;
    protected String msName;
    protected String msPrefix;
    protected boolean mbIsCanEdit;
    protected boolean mbIsCanDelete;
    protected boolean mbIsDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected int mnFkUserDeleteId;
    protected Date mtUserInsertedTs;
    protected Date mtUserUpdatedTs;
    protected Date mtUserDeletedTs;

    public SDataMaterialType() {
        super(SDataConstants.ITMU_TP_MAT);
        reset();
    }

    public void setPkMaterialTypeId(int n) { mnPkMaterialTypeId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setPrefix(String s) { msPrefix = s; }
    public void setIsCanEdit(boolean b) { mbIsCanEdit = b; }
    public void setIsCanDelete(boolean b) { mbIsCanDelete = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserInsertedTs(Date t) { mtUserInsertedTs = t; }
    public void setUserUpdatedTs(Date t) { mtUserUpdatedTs = t; }
    public void setUserDeletedTs(Date t) { mtUserDeletedTs = t; }

    public int getPkMaterialTypeId() { return mnPkMaterialTypeId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getPrefix() { return msPrefix; }
    public boolean isIsCanEdit() { return mbIsCanEdit; }
    public boolean isIsCanDelete() { return mbIsCanDelete; }
    public boolean isIsDeleted() { return mbIsDeleted; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public Date getUserInsertedTs() { return mtUserInsertedTs; }
    public Date getUserUpdatedTs() { return mtUserUpdatedTs; }
    public Date getUserDeletedTs() { return mtUserDeletedTs; }
    
    private void computePrimaryKey(java.sql.Statement statement) throws SQLException {
        ResultSet resultSet = null;

        mnPkMaterialTypeId = 0;

        String sql = "SELECT COALESCE(MAX(id_tp_mat), 0) + 1 FROM " + SDataConstants.TablesMap.get(mnRegistryType) + " ";
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            mnPkMaterialTypeId = resultSet.getInt(1);
        }
    }
    
    private String getSqlWhere() {
        return "WHERE id_tp_mat = " + mnPkMaterialTypeId + " ";
    }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkMaterialTypeId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkMaterialTypeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkMaterialTypeId = 0;
        msCode = "";
        msName = "";
        msPrefix = "";
        mbIsCanEdit = false;
        mbIsCanDelete = false;
        mbIsDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mnFkUserDeleteId = 0;
        mtUserInsertedTs = null;
        mtUserUpdatedTs = null;
        mtUserDeletedTs = null;
        
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM " + SDataConstants.TablesMap.get(mnRegistryType) + " WHERE id_tp_mat = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkMaterialTypeId = resultSet.getInt("id_tp_mat");
                msCode = resultSet.getString("code");
                msName = resultSet.getString("name");
                msPrefix = resultSet.getString("prefix");
                mbIsCanEdit = resultSet.getBoolean("b_can_edit");
                mbIsCanDelete = resultSet.getBoolean("b_can_del");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserInsertId = resultSet.getInt("fid_usr_new");
                mnFkUserUpdateId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserInsertedTs = resultSet.getTimestamp("ts_new");
                mtUserUpdatedTs = resultSet.getTimestamp("ts_edit");
                mtUserDeletedTs = resultSet.getTimestamp("ts_del");

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
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        String sql = "";
        try {
            if (mbIsRegistryNew) {
                computePrimaryKey(connection.createStatement());
                mbIsDeleted = false;
                mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
                mnFkUserDeleteId = mbIsDeleted ? mnFkUserInsertId : SUtilConsts.USR_NA_ID;

                sql = "INSERT INTO " + SDataConstants.TablesMap.get(mnRegistryType) + " VALUES (" +
                        mnPkMaterialTypeId + ", " + 
                        "'" + msCode + "', " + 
                        "'" + msName + "', " + 
                        "'" + msPrefix + "', " + 
                        (mbIsCanEdit ? 1 : 0) + ", " + 
                        (mbIsCanDelete ? 1 : 0) + ", " + 
                        (mbIsDeleted ? 1 : 0) + ", " + 
                        mnFkUserInsertId + ", " + 
                        mnFkUserUpdateId + ", " + 
                        mnFkUserDeleteId + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + " " + 
                        ")";
            }
            else {
                sql = "UPDATE " + SDataConstants.TablesMap.get(mnRegistryType) + " SET " +
//                        "id_tp_mat = " + mnPkMaterialTypeId + ", " +
                        "code = '" + msCode + "', " +
                        "name = '" + msName + "', " +
                        "prefix = '" + msPrefix + "', " +
                        "b_can_edit = " + (mbIsCanEdit ? 1 : 0) + ", " +
                        "b_can_del = " + (mbIsCanDelete ? 1 : 0) + ", " +
                        "b_del = " + (mbIsDeleted ? 1 : 0) + ", " +
//                        "fid_usr_new = " + mnFkUserInsertId + ", " +
                        "fid_usr_edit = " + mnFkUserUpdateId + ", " +
                        "fid_usr_del = " + mnFkUserDeleteId + ", " +
//                        "ts_new = " + "NOW()" + ", " +
                        "ts_edit = " + "NOW()" + ", " +
                        "ts_del = " + "NOW()" + " " +
                        getSqlWhere();
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
        return mtUserUpdatedTs;
    }
    
    @Override
    public SDataMaterialType clone() throws CloneNotSupportedException {
        SDataMaterialType registry = new SDataMaterialType();

        registry.setPkMaterialTypeId(this.getPkMaterialTypeId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setPrefix(this.getPrefix());
        registry.setIsCanEdit(this.isIsCanEdit());
        registry.setIsCanDelete(this.isIsCanDelete());
        registry.setIsDeleted(this.isIsDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setFkUserDeleteId(this.getFkUserDeleteId());
        registry.setUserInsertedTs(this.getUserInsertedTs());
        registry.setUserUpdatedTs(this.getUserUpdatedTs());
        registry.setUserDeletedTs(this.getUserDeletedTs());

        registry.setIsRegistryNew(this.getIsRegistryNew());
        return registry;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.util.Date;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Edwin Carmona
 */
public class SDataAttributeMaterialType extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    public static final int MAX_ATTRIBUTES = 15;

    protected int mnPkItemMaterialTypeId;
    protected int mnPkItemMaterialAttributeId;
    protected int mnSortingPos;
    protected boolean mbIsRequired;
    protected boolean mbIsDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected int mnFkUserDeleteId;
    protected Date mtUserInsertedTs;
    protected Date mtUserUpdatedTs;
    protected Date mtUserDeletedTs;

    public SDataAttributeMaterialType() {
        super(SDataConstants.ITMU_TP_ATT_MAT);
        reset();
    }

    public void setPkItemMaterialTypeId(int n) { mnPkItemMaterialTypeId = n; }
    public void setPkItemMaterialAttributeId(int n) { mnPkItemMaterialAttributeId = n; }
    public void setSortingPos(int n) { mnSortingPos = n; }
    public void setIsRequired(boolean b) { mbIsRequired = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserInsertedTs(Date t) { mtUserInsertedTs = t; }
    public void setUserUpdatedTs(Date t) { mtUserUpdatedTs = t; }
    public void setUserDeletedTs(Date t) { mtUserDeletedTs = t; }

    public int getPkItemMaterialTypeId() { return mnPkItemMaterialTypeId; }
    public int getPkItemMaterialAttributeId() { return mnPkItemMaterialAttributeId; }
    public int getSortingPos() { return mnSortingPos; }
    public boolean isIsRequired() { return mbIsRequired; }
    public boolean isIsDeleted() { return mbIsDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public Date getUserInsertedTs() { return mtUserInsertedTs; }
    public Date getUserUpdatedTs() { return mtUserUpdatedTs; }
    public Date getUserDeletedTs() { return mtUserDeletedTs; }
    
    private String getSqlWhere() {
        return "WHERE id_tp_mat = " + mnPkItemMaterialTypeId + " AND id_att_mat = " + mnPkItemMaterialAttributeId + " ";
    }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkItemMaterialTypeId = ((int[]) pk)[0];
        mnPkItemMaterialAttributeId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkItemMaterialTypeId, mnPkItemMaterialAttributeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkItemMaterialTypeId = 0;
        mnPkItemMaterialAttributeId = 0;
        mnSortingPos = 0;
        mbIsRequired = false;
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
            sql = "SELECT * FROM " + SDataConstants.TablesMap.get(mnRegistryType) + " "
                    + "WHERE id_tp_mat = " + key[0] + " AND id_att_mat = " + key[1] + " ";
            resultSet = statement.getConnection().createStatement().executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkItemMaterialTypeId = resultSet.getInt("id_tp_mat");
                mnPkItemMaterialAttributeId = resultSet.getInt("id_att_mat");
                mnSortingPos = resultSet.getInt("sort");
                mbIsRequired = resultSet.getBoolean("b_req");
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
                mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

                sql = "INSERT INTO " + SDataConstants.TablesMap.get(mnRegistryType) + " VALUES (" +
                        mnPkItemMaterialTypeId + ", " + 
                        mnPkItemMaterialAttributeId + ", " + 
                        mnSortingPos + ", " + 
                        (mbIsRequired ? 1 : 0) + ", " + 
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
//                        "id_tp_mat = " + mnPkItemMaterialTypeId + ", " +
//                        "id_att_mat = " + mnPkItemMaterialAttributeId + ", " +
                        "sort = " + mnSortingPos + ", " +
                        "b_req = " + (mbIsRequired ? 1 : 0) + ", " +
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
    public SDataAttributeMaterialType clone() throws CloneNotSupportedException {
        SDataAttributeMaterialType registry = new SDataAttributeMaterialType();

        registry.setPkItemMaterialTypeId(this.getPkItemMaterialTypeId());
        registry.setPkItemMaterialAttributeId(this.getPkItemMaterialAttributeId());
        registry.setSortingPos(this.getSortingPos());
        registry.setIsRequired(this.isIsRequired());
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

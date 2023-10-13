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
public class SDataItemAttribute extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkItemId;
    protected int mnPkAttributeId;
    protected String msAttributeValue;
    protected int mnSortingPos;
    protected boolean mbIsRequired;
    protected boolean mbIsDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected int mnFkUserDeleteId;
    protected Date mtUserInsertedTs;
    protected Date mtUserUpdatedTs;
    protected Date mtUserDeletedTs;

    public SDataItemAttribute() {
        super(SDataConstants.ITMU_ITEM_ATT);
        reset();
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkAttributeId(int n) { mnPkAttributeId = n; }
    public void setAttributeValue(String s) { msAttributeValue = s; }
    public void setSortingPos(int n) { mnSortingPos = n; }
    public void setIsRequired(boolean b) { mbIsRequired = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserInsertedTs(Date t) { mtUserInsertedTs = t; }
    public void setUserUpdatedTs(Date t) { mtUserUpdatedTs = t; }
    public void setUserDeletedTs(Date t) { mtUserDeletedTs = t; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkAttributeId() { return mnPkAttributeId; }
    public String getAttributeValue() { return msAttributeValue; }
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
        return "WHERE id_item = " + mnPkItemId + " AND id_att_mat = " + mnPkAttributeId + " ";
    }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkItemId = ((int[]) pk)[0];
        mnPkAttributeId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkItemId, mnPkAttributeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkItemId = 0;
        mnPkAttributeId = 0;
        msAttributeValue = "";
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
                    + "WHERE id_item = " + key[0] + " AND "
                    + "id_att_mat = " + key[1] + " ";
            
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkItemId = resultSet.getInt("id_item");
                mnPkAttributeId = resultSet.getInt("id_att_mat");
                msAttributeValue = resultSet.getString("att_value");
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
                mbIsDeleted = false;
                mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

                sql = "INSERT INTO " + SDataConstants.TablesMap.get(mnRegistryType) + " VALUES (" +
                        mnPkItemId + ", " + 
                        mnPkAttributeId + ", " + 
                        "'" + msAttributeValue + "', " + 
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
//                        "id_item = " + mnPkItemId + ", " +
//                        "id_att_mat = " + mnPkAttributeId + ", " +
                        "att_value = '" + msAttributeValue + "', " +
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
    public SDataItemAttribute clone() throws CloneNotSupportedException {
        SDataItemAttribute registry = new SDataItemAttribute();

        registry.setPkItemId(this.getPkItemId());
        registry.setPkAttributeId(this.getPkAttributeId());
        registry.setAttributeValue(this.getAttributeValue());
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

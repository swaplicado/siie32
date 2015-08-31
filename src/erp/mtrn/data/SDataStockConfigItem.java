/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Juan M. Barajas Cárabes
 */
public class SDataStockConfigItem extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkLinkTypeId;
    protected int mnPkReferenceId;
    protected int mnPkCompanyBranchId;
    protected int mnPkWarehouseId;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataStockConfigItem() {
        super(SDataConstants.TRN_STK_CFG_ITEM);
        reset();
    }

    public void setPkLinkTypeId(int n) { mnPkLinkTypeId = n; }
    public void setPkReferenceId(int n) { mnPkReferenceId = n; }
    public void setPkCompanyBranchId(int n) { mnPkCompanyBranchId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkLinkTypeId() { return mnPkLinkTypeId; }
    public int getPkReferenceId() { return mnPkReferenceId; }
    public int getPkCompanyBranchId() { return mnPkCompanyBranchId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkLinkTypeId = ((int[]) pk)[0];
        mnPkReferenceId = ((int[]) pk)[1];
        mnPkCompanyBranchId = ((int[]) pk)[2];
        mnPkWarehouseId = ((int[]) pk)[3];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkLinkTypeId, mnPkReferenceId, mnPkCompanyBranchId, mnPkWarehouseId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkLinkTypeId = 0;
        mnPkReferenceId = 0;
        mnPkCompanyBranchId = 0;
        mnPkWarehouseId = 0;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_stk_cfg_item WHERE " +
                    "id_tp_link = " + key[0] + " AND id_ref = " + key[1] + " AND id_cob = " + key[2] + " AND id_wh = " + key[3] + " ";
            
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkLinkTypeId = resultSet.getInt("id_tp_link");
                mnPkReferenceId = resultSet.getInt("id_ref");
                mnPkCompanyBranchId = resultSet.getInt("id_cob");
                mnPkWarehouseId = resultSet.getInt("id_wh");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

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
                    "{ CALL trn_stk_cfg_item_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkLinkTypeId);
            callableStatement.setInt(nParam++, mnPkReferenceId);
            callableStatement.setInt(nParam++, mnPkCompanyBranchId);
            callableStatement.setInt(nParam++, mnPkWarehouseId);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
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
        return mtUserEditTs;
    }
}

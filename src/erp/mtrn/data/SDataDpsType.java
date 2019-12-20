/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Sergio Flores
 */
public class SDataDpsType extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkDpsCategoryId;
    protected int mnPkDpsClassId;
    protected int mnPkDpsTypeId;
    protected java.lang.String msDpsType;
    protected boolean mbIsCanEdit;
    protected boolean mbIsCanDelete;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;
    
    protected erp.mtrn.data.STrnDpsType moXtaDpsType; // read-only member

    public SDataDpsType() {
        super(SDataConstants.TRNU_TP_DPS);
        reset();
    }
    
    private void createXtaDpsType() {
        moXtaDpsType = new STrnDpsType(mnPkDpsCategoryId, mnPkDpsClassId, mnPkDpsTypeId);
    }

    public void setPkDpsCategoryId(int n) { mnPkDpsCategoryId = n; }
    public void setPkDpsClassId(int n) { mnPkDpsClassId = n; }
    public void setPkDpsTypeId(int n) { mnPkDpsTypeId = n; createXtaDpsType(); }
    public void setDpsType(java.lang.String s) { msDpsType = s; }
    public void setIsCanEdit(boolean b) { mbIsCanEdit = b; }
    public void setIsCanDelete(boolean b) { mbIsCanDelete = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkDpsCategoryId() { return mnPkDpsCategoryId; }
    public int getPkDpsClassId() { return mnPkDpsClassId; }
    public int getPkDpsTypeId() { return mnPkDpsTypeId; }
    public java.lang.String getDpsType() { return msDpsType; }
    public boolean getIsCanEdit() { return mbIsCanEdit; }
    public boolean getIsCanDelete() { return mbIsCanDelete; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public erp.mtrn.data.STrnDpsType getXtaDpsType() { return moXtaDpsType; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkDpsCategoryId = ((int[]) pk)[0];
        mnPkDpsClassId = ((int[]) pk)[1];
        mnPkDpsTypeId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkDpsCategoryId, mnPkDpsClassId, mnPkDpsTypeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkDpsCategoryId = 0;
        mnPkDpsClassId = 0;
        mnPkDpsTypeId = 0;
        msDpsType = "";
        mbIsCanEdit = false;
        mbIsCanDelete = false;
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
            sql = "SELECT * FROM erp.trnu_tp_dps " +
                    "WHERE id_ct_dps = " + key[0]  + " AND id_cl_dps = " + key[1]  + " AND id_tp_dps = " + key[2]  + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkDpsCategoryId = resultSet.getInt("id_ct_dps");
                mnPkDpsClassId = resultSet.getInt("id_cl_dps");
                mnPkDpsTypeId = resultSet.getInt("id_tp_dps");
                msDpsType = resultSet.getString("tp_dps");
                mbIsCanEdit = resultSet.getBoolean("b_can_edit");
                mbIsCanDelete = resultSet.getBoolean("b_can_del");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");
                
                createXtaDpsType();

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}

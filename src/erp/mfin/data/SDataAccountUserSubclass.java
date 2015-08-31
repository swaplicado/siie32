/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Sergio Flores
 */
public class SDataAccountUserSubclass extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkAccountUserTypeId;
    protected int mnPkAccountUserClassId;
    protected int mnPkAccountUserSubclassId;
    protected java.lang.String msAccountUserSubclass;
    protected int mnNumberStart;
    protected int mnNumberEnd;
    protected boolean mbIsApplying;
    protected boolean mbIsCanEdit;
    protected boolean mbIsCanDelete;
    protected boolean mbIsDeleted;
    protected int mnFkAccountTypeId;
    protected int mnFkAccountClassId;
    protected int mnFkAccountSubclassId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataAccountUserSubclass() {
        super(SDataConstants.FINU_CLS_ACC_USR);
        reset();
    }

    public void setPkAccountUserTypeId(int n) { mnPkAccountUserTypeId = n; }
    public void setPkAccountUserClassId(int n) { mnPkAccountUserClassId = n; }
    public void setPkAccountUserSubclassId(int n) { mnPkAccountUserSubclassId = n; }
    public void setAccountUserSubclass(java.lang.String s) { msAccountUserSubclass = s; }
    public void setNumberStart(int n) { mnNumberStart = n; }
    public void setNumberEnd(int n) { mnNumberEnd = n; }
    public void setIsApplying(boolean b) { mbIsApplying = b; }
    public void setIsCanEdit(boolean b) { mbIsCanEdit = b; }
    public void setIsCanDelete(boolean b) { mbIsCanDelete = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkAccountTypeId(int n) { mnFkAccountTypeId = n; }
    public void setFkAccountClassId(int n) { mnFkAccountClassId = n; }
    public void setFkAccountSubclassId(int n) { mnFkAccountSubclassId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkAccountUserTypeId() { return mnPkAccountUserTypeId; }
    public int getPkAccountUserClassId() { return mnPkAccountUserClassId; }
    public int getPkAccountUserSubclassId() { return mnPkAccountUserSubclassId; }
    public java.lang.String getAccountUserSubclass() { return msAccountUserSubclass; }
    public int getNumberStart() { return mnNumberStart; }
    public int getNumberEnd() { return mnNumberEnd; }
    public boolean getIsApplying() { return mbIsApplying; }
    public boolean getIsCanEdit() { return mbIsCanEdit; }
    public boolean getIsCanDelete() { return mbIsCanDelete; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkAccountTypeId() { return mnFkAccountTypeId; }
    public int getFkAccountClassId() { return mnFkAccountClassId; }
    public int getFkAccountSubclassId() { return mnFkAccountSubclassId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkAccountUserTypeId = ((int[]) pk)[0];
        mnPkAccountUserClassId = ((int[]) pk)[1];
        mnPkAccountUserSubclassId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkAccountUserTypeId, mnPkAccountUserClassId, mnPkAccountUserSubclassId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkAccountUserTypeId = 0;
        mnPkAccountUserClassId = 0;
        mnPkAccountUserSubclassId = 0;
        msAccountUserSubclass = "";
        mnNumberStart = 0;
        mnNumberEnd = 0;
        mbIsApplying = false;
        mbIsCanEdit = false;
        mbIsCanDelete = false;
        mbIsDeleted = false;
        mnFkAccountTypeId = 0;
        mnFkAccountClassId = 0;
        mnFkAccountSubclassId = 0;
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
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.finu_cls_acc_usr WHERE id_tp_acc_usr = " + key[0] + " AND id_cl_acc_usr = " + key[1] + " AND id_cls_acc_usr = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkAccountUserTypeId = resultSet.getInt("id_tp_acc_usr");
                mnPkAccountUserClassId = resultSet.getInt("id_cl_acc_usr");
                mnPkAccountUserSubclassId = resultSet.getInt("id_cls_acc_usr");
                msAccountUserSubclass = resultSet.getString("cls_acc_usr");
                mnNumberStart = resultSet.getInt("num_start");
                mnNumberEnd = resultSet.getInt("num_end");
                mbIsApplying = resultSet.getBoolean("b_apply");
                mbIsCanEdit = resultSet.getBoolean("b_can_edit");
                mbIsCanDelete = resultSet.getBoolean("b_can_del");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkAccountTypeId = resultSet.getInt("fid_tp_acc");
                mnFkAccountClassId = resultSet.getInt("fid_cl_acc");
                mnFkAccountSubclassId = resultSet.getInt("fid_cls_acc");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}

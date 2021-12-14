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
public class SDataRecordType extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected java.lang.String msPkRecordTypeIdXXX;
    protected int mnPkRecordTypeId;
    protected java.lang.String msCode;
    protected java.lang.String msRecordType;
    protected boolean mbIsAccountCashRequired;
    protected boolean mbIsSystem;
    protected boolean mbIsCanEdit;
    protected boolean mbIsCanDelete;
    protected boolean mbIsDeleted;
    protected int mnFkAccountingMoveTypeId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataRecordType() {
        super(SDataConstants.FINU_TP_REC);
        reset();
    }

    public void setPkRecordTypeIdXXX(java.lang.String s) { msPkRecordTypeIdXXX = s; }
    public void setPkRecordTypeId(int n) { mnPkRecordTypeId = n; }
    public void setCode(java.lang.String s) { msCode = s; }
    public void setRecordType(java.lang.String s) { msRecordType = s; }
    public void setIsAccountCashRequired(boolean b) { mbIsAccountCashRequired = b; }
    public void setIsSystem(boolean b) { mbIsSystem = b; }
    public void setIsCanEdit(boolean b) { mbIsCanEdit = b; }
    public void setIsCanDelete(boolean b) { mbIsCanDelete = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkAccountingMoveTypeId(int n) { mnFkAccountingMoveTypeId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public java.lang.String getPkRecordTypeIdXXX() { return msPkRecordTypeIdXXX; }
    public int getPkRecordTypeId() { return mnPkRecordTypeId; }
    public java.lang.String getCode() { return msCode; }
    public java.lang.String getRecordType() { return msRecordType; }
    public boolean getIsAccountCashRequired() { return mbIsAccountCashRequired; }
    public boolean getIsSystem() { return mbIsSystem; }
    public boolean getIsCanEdit() { return mbIsCanEdit; }
    public boolean getIsCanDelete() { return mbIsCanDelete; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkAccountingMoveTypeId() { return mnFkAccountingMoveTypeId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        msPkRecordTypeIdXXX = (String) ((Object[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new Object[] { msPkRecordTypeIdXXX };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        msPkRecordTypeIdXXX = "";
        mnPkRecordTypeId = 0;
        msCode = "";
        msRecordType = "";
        mbIsAccountCashRequired = false;
        mbIsSystem = false;
        mbIsCanEdit = false;
        mbIsCanDelete = false;
        mbIsDeleted = false;
        mnFkAccountingMoveTypeId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        Object[] key = (Object[]) pk;
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.finu_tp_rec WHERE id_tp_rec = '" + key[0] + "' ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                msPkRecordTypeIdXXX = resultSet.getString("id_tp_rec");
                mnPkRecordTypeId = resultSet.getInt("pk_tp_rec");
                msCode = resultSet.getString("code");
                msRecordType = resultSet.getString("tp_rec");
                mbIsAccountCashRequired = resultSet.getBoolean("b_acc_cash");
                mbIsSystem = resultSet.getBoolean("b_sys");
                mbIsCanEdit = resultSet.getBoolean("b_can_edit");
                mbIsCanDelete = resultSet.getBoolean("b_can_del");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkAccountingMoveTypeId = resultSet.getInt("fid_tp_acc_mov");
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

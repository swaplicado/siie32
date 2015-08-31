/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 *
 * @author Juan Barajas
 */
public class SDataBankLayoutType extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkBankLayoutTypeId;
    protected java.lang.String msBankLayoutType;
    protected int mnLayoutBank;
    protected boolean mbIsCanEdit;
    protected boolean mbIsCanDelete;
    protected boolean mbIsDeleted;
    protected int mnFkBankPaymentTypeId;
    protected int mnFkBankId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataBankLayoutType() {
        super(SDataConstants.FINU_TP_LAY_BANK);
        reset();
    }

    public void setPkBankLayoutTypeId(int n) { mnPkBankLayoutTypeId = n; }
    public void setBankLayoutType(java.lang.String s) { msBankLayoutType = s; }
    public void setLayoutBank(int n) { mnLayoutBank = n; }
    public void setIsCanEdit(boolean b) { mbIsCanEdit = b; }
    public void setIsCanDelete(boolean b) { mbIsCanDelete = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkBankPaymentTypeId(int n) { mnFkBankPaymentTypeId = n; }
    public void setFkBankId(int n) { mnFkBankId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkBankLayoutTypeId() { return mnPkBankLayoutTypeId; }
    public java.lang.String getBankLayoutType() { return msBankLayoutType; }
    public int getLayoutBank() { return mnLayoutBank; }
    public boolean getIsCanEdit() { return mbIsCanEdit; }
    public boolean getIsCanDelete() { return mbIsCanDelete; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkBankPaymentTypeId() { return mnFkBankPaymentTypeId; }
    public int getFkBankId() { return mnFkBankId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBankLayoutTypeId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkBankLayoutTypeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkBankLayoutTypeId = 0;
        msBankLayoutType = "";
        mnLayoutBank = 0;
        mbIsCanEdit = false;
        mbIsCanDelete = false;
        mbIsDeleted = false;
        mnFkBankPaymentTypeId = 0;
        mnFkBankId = 0;
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
            sql = "SELECT * FROM erp.finu_tp_lay_bank WHERE id_tp_lay_bank = " + key[0] + "";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBankLayoutTypeId = resultSet.getInt("id_tp_lay_bank");
                msBankLayoutType = resultSet.getString("tp_lay_bank");
                mnLayoutBank = resultSet.getInt("lay_bank");
                mbIsCanEdit = resultSet.getBoolean("b_can_edit");
                mbIsCanDelete = resultSet.getBoolean("b_can_del");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkBankPaymentTypeId = resultSet.getInt("fid_tp_pay_bank");
                mnFkBankId = resultSet.getInt("fid_bank");
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
                    "{ CALL erp.finu_tp_lay_bank_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ? ) }");
            callableStatement.setInt(nParam++, mnPkBankLayoutTypeId);
            callableStatement.setString(nParam++, msBankLayoutType);
            callableStatement.setInt(nParam++, mnLayoutBank);
            callableStatement.setBoolean(nParam++, mbIsCanEdit);
            callableStatement.setBoolean(nParam++, mbIsCanDelete);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkBankPaymentTypeId);
            callableStatement.setInt(nParam++, mnFkBankId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
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
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}

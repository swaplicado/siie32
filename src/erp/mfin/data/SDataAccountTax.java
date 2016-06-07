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
 * @author Sergio Flores
 */
public class SDataAccountTax extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkTaxBasicId;
    protected int mnPkTaxId;
    protected int mnPkDpsCategoryId;
    protected java.util.Date mtPkDateStartId;
    protected boolean mbIsDeleted;
    protected java.lang.String msFkAccountPaymentId;
    protected java.lang.String msFkAccountPaymentPendingId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataAccountTax() {
        super(SDataConstants.FIN_ACC_TAX);
        reset();
    }

    public void setPkTaxBasicId(int n) { mnPkTaxBasicId = n; }
    public void setPkTaxId(int n) { mnPkTaxId = n; }
    public void setPkDpsCategoryId(int n) { mnPkDpsCategoryId = n; }
    public void setPkDateStartId(java.util.Date t) { mtPkDateStartId = t; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkAccountPaymentId(java.lang.String s) { msFkAccountPaymentId = s; }
    public void setFkAccountPaymentPendingId(java.lang.String s) { msFkAccountPaymentPendingId = s; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkTaxBasicId() { return mnPkTaxBasicId; }
    public int getPkTaxId() { return mnPkTaxId; }
    public int getPkDpsCategoryId() { return mnPkDpsCategoryId; }
    public java.util.Date getPkDateStartId() { return mtPkDateStartId; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public java.lang.String getFkAccountPaymentId() { return msFkAccountPaymentId; }
    public java.lang.String getFkAccountPaymentPendingId() { return msFkAccountPaymentPendingId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkTaxBasicId = (Integer) ((Object[]) pk)[0];
        mnPkTaxId = (Integer) ((Object[]) pk)[1];
        mnPkDpsCategoryId = (Integer) ((Object[]) pk)[2];
        mtPkDateStartId = (java.util.Date) ((Object[]) pk)[3];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new Object[] { mnPkTaxBasicId, mnPkTaxId, mnPkDpsCategoryId, mtPkDateStartId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkTaxBasicId = 0;
        mnPkTaxId = 0;
        mnPkDpsCategoryId = 0;
        mtPkDateStartId = null;
        mbIsDeleted = false;
        msFkAccountPaymentId = "";
        msFkAccountPaymentPendingId = "";
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
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM fin_acc_tax " +
                    "WHERE id_tax_bas = " + key[0] + " AND id_tax = " + key[1] + " AND id_ct_dps = " + key[2] + " AND id_dt_start = '" + key[3] + "' ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkTaxBasicId = resultSet.getInt("id_tax_bas");
                mnPkTaxId = resultSet.getInt("id_tax");
                mnPkDpsCategoryId = resultSet.getInt("id_ct_dps");
                mtPkDateStartId = resultSet.getDate("id_dt_start");
                mbIsDeleted = resultSet.getBoolean("b_del");
                msFkAccountPaymentId = resultSet.getString("fid_acc_pay");
                msFkAccountPaymentPendingId = resultSet.getString("fid_acc_pay_pend");
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
                    "{ CALL fin_acc_tax_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkTaxBasicId);
            callableStatement.setInt(nParam++, mnPkTaxId);
            callableStatement.setInt(nParam++, mnPkDpsCategoryId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtPkDateStartId.getTime()));
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setString(nParam++, msFkAccountPaymentId);
            callableStatement.setString(nParam++, msFkAccountPaymentPendingId);
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
        return null;
    }
}

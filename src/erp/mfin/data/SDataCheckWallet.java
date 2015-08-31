/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Alfonso Flores
 */
public class SDataCheckWallet extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkCheckWalletId;
    protected int mnNumberStart;
    protected int mnNumberEnd_n;
    protected boolean mbIsActive;
    protected boolean mbIsDeleted;
    protected int mnFkCompanyBranchId_n;
    protected int mnFkAccountCashId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataCheckWallet() {
        super(SDataConstants.FIN_CHECK_WAL);
        reset();
    }

    public void setPkCheckWalletId(int n) { mnPkCheckWalletId = n; }
    public void setNumberStart(int n) { mnNumberStart = n; }
    public void setNumberEnd_n(int n) { mnNumberEnd_n = n; }
    public void setIsActive(boolean b) { mbIsActive = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId_n = n; }
    public void setFkAccountCashId(int n) { mnFkAccountCashId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkCheckWalletId() { return mnPkCheckWalletId; }
    public int getNumberStart() { return mnNumberStart; }
    public int getNumberEnd_n() { return mnNumberEnd_n; }
    public boolean getIsActive() { return mbIsActive; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId_n; }
    public int getFkAccountCashId() { return mnFkAccountCashId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkCheckWalletId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkCheckWalletId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkCheckWalletId = 0;
        mnNumberStart = 0;
        mnNumberEnd_n = 0;
        mbIsActive = false;
        mbIsDeleted = false;
        mnFkCompanyBranchId_n = 0;
        mnFkAccountCashId_n = 0;
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
            sql = "SELECT * FROM fin_check_wal WHERE id_check_wal = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkCheckWalletId = resultSet.getInt("id_check_wal");
                mnNumberStart = resultSet.getInt("num_start");
                mnNumberEnd_n = resultSet.getInt("num_end_n");
                if (resultSet.wasNull()) mnNumberEnd_n = -1;
                mbIsActive = resultSet.getBoolean("b_act");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkCompanyBranchId_n = resultSet.getInt("fid_cob");
                if (resultSet.wasNull()) mnFkCompanyBranchId_n = -1;
                mnFkAccountCashId_n = resultSet.getInt("fid_acc_cash");
                if (resultSet.wasNull()) mnFkAccountCashId_n = -1;
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
                    "{ CALL fin_check_wal_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?) }");
            callableStatement.setInt(nParam++, mnPkCheckWalletId);
            callableStatement.setInt(nParam++, mnNumberStart);
            if (mnNumberEnd_n >= 0) callableStatement.setInt(nParam++, mnNumberEnd_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            callableStatement.setBoolean(nParam++, mbIsActive);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            if (mnFkCompanyBranchId_n >= 0) callableStatement.setInt(nParam++, mnFkCompanyBranchId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkAccountCashId_n >= 0) callableStatement.setInt(nParam++, mnFkAccountCashId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkCheckWalletId = callableStatement.getInt(nParam - 3);
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mhrs.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 *
 * @author Sergio Flores
 */
public class SDataFormerPayrollMove extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkPayrollId;
    protected int mnPkMoveId;
    protected int mnType;
    protected int mnTransactionId;
    protected java.lang.String msTransaction;
    protected int mnReferenceId;
    protected java.lang.String msReference;
    protected java.lang.String msReferenceKey;
    protected double mdAmount;
    protected int mnFkYearId;
    protected int mnFkPeriodId;
    protected int mnFkBookkeepingCenterId;
    protected java.lang.String msFkRecordTypeId;
    protected int mnFkNumberId;
    protected int mnFkEntryId;

    public SDataFormerPayrollMove() {
        super(SDataConstants.HRS_FORMER_PAYR_MOV);
        reset();
    }

    public void setPkPayrollId(int n) { mnPkPayrollId = n; }
    public void setPkMoveId(int n) { mnPkMoveId = n; }
    public void setType(int n) { mnType = n; }
    public void setTransactionId(int n) { mnTransactionId = n; }
    public void setTransaction(java.lang.String s) { msTransaction = s; }
    public void setReferenceId(int n) { mnReferenceId = n; }
    public void setReference(java.lang.String s) { msReference = s; }
    public void setReferenceKey(java.lang.String s) { msReferenceKey = s; }
    public void setAmount(double d) { mdAmount = d; }
    public void setFkYearId(int n) { mnFkYearId = n; }
    public void setFkPeriodId(int n) { mnFkPeriodId = n; }
    public void setFkBookkeepingCenterId(int n) { mnFkBookkeepingCenterId = n; }
    public void setFkRecordTypeId(java.lang.String s) { msFkRecordTypeId = s; }
    public void setFkNumberId(int n) { mnFkNumberId = n; }
    public void setFkEntryId(int n) { mnFkEntryId = n; }

    public int getPkPayrollId() { return mnPkPayrollId; }
    public int getPkMoveId() { return mnPkMoveId; }
    public int getType() { return mnType; }
    public int getTransactionId() { return mnTransactionId; }
    public java.lang.String getTransaction() { return msTransaction; }
    public int getReferenceId() { return mnReferenceId; }
    public java.lang.String getReference() { return msReference; }
    public java.lang.String getReferenceKey() { return msReferenceKey; }
    public double getAmount() { return mdAmount; }
    public int getFkYearId() { return mnFkYearId; }
    public int getFkPeriodId() { return mnFkPeriodId; }
    public int getFkBookkeepingCenterId() { return mnFkBookkeepingCenterId; }
    public java.lang.String getFkRecordTypeId() { return msFkRecordTypeId; }
    public int getFkNumberId() { return mnFkNumberId; }
    public int getFkEntryId() { return mnFkEntryId; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkPayrollId = ((int[]) pk)[0];
        mnPkMoveId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkPayrollId, mnPkMoveId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkPayrollId = 0;
        mnPkMoveId = 0;
        mnType = 0;
        mnTransactionId = 0;
        msTransaction = "";
        mnReferenceId = 0;
        msReference = "";
        msReferenceKey = "";
        mdAmount = 0;
        mnFkYearId = 0;
        mnFkPeriodId = 0;
        mnFkBookkeepingCenterId = 0;
        msFkRecordTypeId = "";
        mnFkNumberId = 0;
        mnFkEntryId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM hrs_sie_pay_mov WHERE id_pay = " + key[0] + " AND id_mov = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkPayrollId = resultSet.getInt("id_pay");
                mnPkMoveId = resultSet.getInt("id_mov");
                mnType = resultSet.getInt("type");
                mnTransactionId = resultSet.getInt("trn_id");
                msTransaction = resultSet.getString("trn");
                mnReferenceId = resultSet.getInt("ref_id");
                msReference = resultSet.getString("ref");
                msReferenceKey = resultSet.getString("ref_key");
                mdAmount = resultSet.getDouble("amt");
                mnFkYearId = resultSet.getInt("fid_year");
                mnFkPeriodId = resultSet.getInt("fid_per");
                mnFkBookkeepingCenterId = resultSet.getInt("fid_bkc");
                msFkRecordTypeId = resultSet.getString("fid_tp_rec");
                mnFkNumberId = resultSet.getInt("fid_num");
                mnFkEntryId = resultSet.getInt("fid_ety");

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
                    "{ CALL hrs_sie_pay_mov_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkPayrollId);
            callableStatement.setInt(nParam++, mnPkMoveId);
            callableStatement.setInt(nParam++, mnType);
            callableStatement.setInt(nParam++, mnTransactionId);
            callableStatement.setString(nParam++, msTransaction);
            callableStatement.setInt(nParam++, mnReferenceId);
            callableStatement.setString(nParam++, msReference);
            callableStatement.setString(nParam++, msReferenceKey);
            callableStatement.setDouble(nParam++, mdAmount);
            callableStatement.setInt(nParam++, mnFkYearId);
            callableStatement.setInt(nParam++, mnFkPeriodId);
            callableStatement.setInt(nParam++, mnFkBookkeepingCenterId);
            callableStatement.setString(nParam++, msFkRecordTypeId);
            callableStatement.setInt(nParam++, mnFkNumberId);
            callableStatement.setInt(nParam++, mnFkEntryId);
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
        return null;
    }
}

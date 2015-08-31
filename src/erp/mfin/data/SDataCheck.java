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
 * @author Alfonso Flores
 */
public class SDataCheck extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkCheckWalletId;
    protected int mnPkCheckId;
    protected java.util.Date mtDate;
    protected int mnNumber;
    protected java.lang.String msBeneficiary;
    protected java.lang.String msBeneficiaryAccount;
    protected double mdValue;
    protected boolean mbIsForBeneficiaryAccount;
    protected boolean mbIsDeleted;
    protected int mnFkCheckStatusId;
    protected int mnFkBizPartnerId_nr;
    protected int mnFkBizPartnerBranchId_n;
    protected int mnFkBankAccountId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected int mnAuxCurrencyId;

    public SDataCheck() {
        super(SDataConstants.FIN_CHECK);
        reset();
    }

    public void setPkCheckWalletId(int n) { mnPkCheckWalletId = n; }
    public void setPkCheckId(int n) { mnPkCheckId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setNumber(int n) { mnNumber = n; }
    public void setBeneficiary(java.lang.String s) { msBeneficiary = s; }
    public void setBeneficiaryAccount(java.lang.String s) { msBeneficiaryAccount = s; }
    public void setValue(double d) { mdValue = d; }
    public void setIsForBeneficiaryAccount(boolean b) { mbIsForBeneficiaryAccount = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkCheckStatusId(int n) { mnFkCheckStatusId = n; }
    public void setFkBizPartnerId_nr(int n) { mnFkBizPartnerId_nr = n; }
    public void setFkBizPartnerBranchId(int n) { mnFkBizPartnerBranchId_n = n; }
    public void setFkBankAccountId(int n) { mnFkBankAccountId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkCheckWalletId() { return mnPkCheckWalletId; }
    public int getPkCheckId() { return mnPkCheckId; }
    public java.util.Date getDate() { return mtDate; }
    public int getNumber() { return mnNumber; }
    public java.lang.String getBeneficiary() { return msBeneficiary; }
    public java.lang.String getBeneficiaryAccount() { return msBeneficiaryAccount; }
    public double getValue() { return mdValue; }
    public boolean getIsForBeneficiaryAccount() { return mbIsForBeneficiaryAccount; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkCheckStatusId() { return mnFkCheckStatusId; }
    public int getFkBizPartnerId_nr() { return mnFkBizPartnerId_nr; }
    public int getFkBizPartnerBranchId() { return mnFkBizPartnerBranchId_n; }
    public int getFkBankAccountId() { return mnFkBankAccountId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void seAuxCurrencyId(int n) { mnAuxCurrencyId = n; }
    public int getAuxCurrencyId() { return mnAuxCurrencyId; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkCheckWalletId = ((int[]) pk)[0];
        mnPkCheckId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkCheckWalletId, mnPkCheckId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkCheckWalletId = 0;
        mnPkCheckId = 0;
        mtDate = null;
        mnNumber = 0;
        msBeneficiary = "";
        msBeneficiaryAccount = "";
        mdValue = 0;
        mbIsForBeneficiaryAccount = false;
        mbIsDeleted = false;
        mnFkCheckStatusId = 0;
        mnFkBizPartnerId_nr = 0;
        mnFkBizPartnerBranchId_n = 0;
        mnFkBankAccountId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mnAuxCurrencyId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT c.*, re.fid_cur " +
                    "FROM fin_check AS c " +
                    "LEFT OUTER JOIN fin_rec_ety AS re ON " +
                    "c.id_check_wal = re.fid_check_wal_n AND c.id_check = re.fid_check_n AND re.b_del = 0 " +
                    "WHERE c.id_check_wal = " + key[0] + " AND c.id_check = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkCheckWalletId = resultSet.getInt("c.id_check_wal");
                mnPkCheckId = resultSet.getInt("c.id_check");
                mtDate = resultSet.getDate("c.dt");
                mnNumber = resultSet.getInt("c.num");
                msBeneficiary = resultSet.getString("c.benef");
                msBeneficiaryAccount = resultSet.getString("c.benef_acc");
                mdValue = resultSet.getDouble("c.val");
                mbIsForBeneficiaryAccount = resultSet.getBoolean("c.b_benef_acc");
                mbIsDeleted = resultSet.getBoolean("c.b_del");
                mnFkCheckStatusId = resultSet.getInt("c.fid_st_check");
                mnFkBizPartnerId_nr = resultSet.getInt("c.fid_bp_nr");
                if (resultSet.wasNull()) mnFkBizPartnerId_nr = 0;
                mnFkBizPartnerBranchId_n = resultSet.getInt("c.fid_bpb_n");
                if (resultSet.wasNull()) mnFkBizPartnerBranchId_n = 0;
                mnFkBankAccountId_n = resultSet.getInt("c.fid_bank_acc_n");
                if (resultSet.wasNull()) mnFkBankAccountId_n = 0;
                mnFkUserNewId = resultSet.getInt("c.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("c.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("c.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("c.ts_new");
                mtUserEditTs = resultSet.getTimestamp("c.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("c.ts_del");

                mnAuxCurrencyId = resultSet.getInt("re.fid_cur");

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
                    "{ CALL fin_check_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkCheckWalletId);
            callableStatement.setInt(nParam++, mnPkCheckId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDate.getTime()));
            callableStatement.setInt(nParam++, mnNumber);
            callableStatement.setString(nParam++, msBeneficiary);
            callableStatement.setString(nParam++, msBeneficiaryAccount);
            callableStatement.setDouble(nParam++, mdValue);
            callableStatement.setBoolean(nParam++, mbIsForBeneficiaryAccount);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkCheckStatusId);
            if (mnFkBizPartnerId_nr > 0) callableStatement.setInt(nParam++, mnFkBizPartnerId_nr); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkBizPartnerBranchId_n > 0) callableStatement.setInt(nParam++, mnFkBizPartnerBranchId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkBankAccountId_n > 0) callableStatement.setInt(nParam++, mnFkBankAccountId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkCheckId = callableStatement.getInt(nParam - 3);
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

    public java.lang.String getRecordConcept() {
        String concept = "";

        concept = "CHQ. " + mnNumber + "; " + msBeneficiary.substring(0, msBeneficiary.length() > 80 ? 80 : msBeneficiary.length());

        return concept;
    }
}

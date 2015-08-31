/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 *
 * @author Juan Barajas
 */
public class SDataPac extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkPacId;
    protected java.lang.String msPac;
    protected java.lang.String msUser;
    protected java.lang.String msUserPassword;
    protected boolean mbIsPrepayment;
    protected boolean mbIsAnnulmentEnabled;
    protected boolean mbIsChargedAnnulment;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataPac() {
        super(SDataConstants.TRN_PAC);
        reset();
    }

    public void setPkPacId(int n) { mnPkPacId = n; }
    public void setPac(java.lang.String s) { msPac = s; }
    public void setUser(java.lang.String s) { msUser = s; }
    public void setUserPassword(java.lang.String s) { msUserPassword = s; }
    public void setIsPrepayment(boolean b) { mbIsPrepayment = b; }
    public void setIsAnnulmentEnabled(boolean b) { mbIsAnnulmentEnabled = b; }
    public void setIsChargedAnnulment(boolean b) { mbIsChargedAnnulment = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkPacId() { return mnPkPacId; }
    public java.lang.String getPac() { return msPac; }
    public java.lang.String getUser() { return msUser; }
    public java.lang.String getUserPassword() { return msUserPassword; }
    public boolean getIsPrepayment() { return mbIsPrepayment; }
    public boolean getIsAnnulmentEnabled() { return mbIsAnnulmentEnabled; }
    public boolean getIsChargedAnnulment() { return mbIsChargedAnnulment; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkPacId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkPacId};
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkPacId = 0;
        msPac = "";
        msUser = "";
        msUserPassword = "";
        mbIsPrepayment = false;
        mbIsAnnulmentEnabled = false;
        mbIsChargedAnnulment = false;
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
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_pac WHERE id_pac = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkPacId = resultSet.getInt("id_pac");
                msPac = resultSet.getString("pac");
                msUser = resultSet.getString("usr");
                msUserPassword = resultSet.getString("usr_pswd");
                mbIsPrepayment = resultSet.getBoolean("b_pre_pay");
                mbIsAnnulmentEnabled = resultSet.getBoolean("b_ann");
                mbIsChargedAnnulment = resultSet.getBoolean("b_cha_ann");
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
            /*
            callableStatement = connection.prepareCall(
                    "{ CALL erp.trnu_tp_iog_adj_save(" +
                    "?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkIogAdjustmentTypeId);
            callableStatement.setString(nParam++, msIogAdjustmentType);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkPacId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);
            */

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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 *
 * @author Alfonso Flores
 */
public class SDataBizPartnerType extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkBizPartnerCategoryId;
    protected int mnPkBizPartnerTypeId;
    protected java.lang.String msBizPartnerType;
    protected double mdCreditLimit;
    protected int mnDaysOfCredit;
    protected int mnDaysOfGrace;
    protected int mnTypeIndex;
    protected boolean mbIsCanEdit;
    protected boolean mbIsCanDelete;
    protected boolean mbIsDeleted;
    protected int mnFkCreditTypeId;
    protected int mnFkRiskTypeId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataBizPartnerType() {
        super(SDataConstants.BPSU_TP_BP);
        reset();
    }

    public void setPkBizPartnerCategoryId(int n) { mnPkBizPartnerCategoryId = n; }
    public void setPkBizPartnerTypeId(int n) { mnPkBizPartnerTypeId = n; }
    public void setBizPartnerType(java.lang.String s) { msBizPartnerType = s; }
    public void setCreditLimit(double d) { mdCreditLimit = d; }
    public void setDaysOfCredit(int n) { mnDaysOfCredit = n; }
    public void setDaysOfGrace(int n) { mnDaysOfGrace = n; }
    public void setTypeIndex(int n) { mnTypeIndex = n; }
    public void setIsCanEdit(boolean b) { mbIsCanEdit = b; }
    public void setIsCanDelete(boolean b) { mbIsCanDelete = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkCreditTypeId(int n) { mnFkCreditTypeId = n; }
    public void setFkRiskTypeId(int n) { mnFkRiskTypeId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkBizPartnerCategoryId() { return mnPkBizPartnerCategoryId; }
    public int getPkBizPartnerTypeId() { return mnPkBizPartnerTypeId; }
    public java.lang.String getBizPartnerType() { return msBizPartnerType; }
    public double getCreditLimit() { return mdCreditLimit; }
    public int getDaysOfCredit() { return mnDaysOfCredit; }
    public int getDaysOfGrace() { return mnDaysOfGrace; }
    public int getTypeIndex() { return mnTypeIndex; }
    public boolean getIsCanEdit() { return mbIsCanEdit; }
    public boolean getIsCanDelete() { return mbIsCanDelete; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkCreditTypeId() { return mnFkCreditTypeId; }
    public int getFkRiskTypeId() { return mnFkRiskTypeId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBizPartnerCategoryId = ((int[]) pk)[0];
        mnPkBizPartnerTypeId= ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkBizPartnerCategoryId, mnPkBizPartnerTypeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkBizPartnerCategoryId = 0;
        mnPkBizPartnerTypeId = 0;
        msBizPartnerType = "";
        mdCreditLimit = 0;
        mnDaysOfCredit = 0;
        mnDaysOfGrace = 0;
        mnTypeIndex = 0;
        mbIsCanEdit = false;
        mbIsCanDelete = false;
        mbIsDeleted = false;
        mnFkCreditTypeId = 0;
        mnFkCreditTypeId = 0;
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
            sql = "SELECT * FROM erp.bpsu_tp_bp WHERE id_ct_bp = " + key[0] + " AND id_tp_bp = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBizPartnerCategoryId = resultSet.getInt("id_ct_bp");
                mnPkBizPartnerTypeId = resultSet.getInt("id_tp_bp");
                msBizPartnerType = resultSet.getString("tp_bp");
                mdCreditLimit = resultSet.getDouble("cred_lim");
                mnDaysOfCredit = resultSet.getInt("days_cred");
                mnDaysOfGrace = resultSet.getInt("days_grace");
                mnTypeIndex = resultSet.getInt("tp_idx");
                mbIsCanEdit = resultSet.getBoolean("b_can_edit");
                mbIsCanDelete = resultSet.getBoolean("b_can_del");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkCreditTypeId = resultSet.getInt("fid_tp_cred");
                mnFkRiskTypeId = resultSet.getInt("fid_risk");
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
                    "{ CALL erp.bpsu_tp_bp_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + 
                    "?, ?, ?, ?, ?, ? ) }");
            callableStatement.setInt(nParam++, mnPkBizPartnerCategoryId);
            callableStatement.setInt(nParam++, mnPkBizPartnerTypeId);
            callableStatement.setString(nParam++, msBizPartnerType);
            callableStatement.setDouble(nParam++, mdCreditLimit);
            callableStatement.setInt(nParam++, mnDaysOfCredit);
            callableStatement.setInt(nParam++, mnDaysOfGrace);
            callableStatement.setInt(nParam++, mnTypeIndex);
            callableStatement.setBoolean(nParam++, mbIsCanEdit);
            callableStatement.setBoolean(nParam++, mbIsCanDelete);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkCreditTypeId);
            callableStatement.setInt(nParam++, mnFkRiskTypeId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkBizPartnerTypeId = callableStatement.getInt(nParam - 3);
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

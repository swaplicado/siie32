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
public class SDataAccountBizPartnerBpType extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkBizPartnerCategoryId;
    protected int mnPkBizPartnerTypeId;
    protected int mnPkBookkepingCenterId;
    protected int mnPkAccountBizPartnerId;
    protected java.util.Date mtPkDateStartId;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsBizPartnerCategory;
    protected java.lang.String msDbmsBizPartnerType;
    protected java.lang.String msDbmsBookkeepingCenter;

    public SDataAccountBizPartnerBpType() {
        super(SDataConstants.FIN_ACC_BP_TP_BP);
        reset();
    }

    public void setPkBizPartnerCategoryId(int n) { mnPkBizPartnerCategoryId = n; }
    public void setPkBizPartnerTypeId(int n) { mnPkBizPartnerTypeId = n; }
    public void setPkBookkepingCenterId(int n) { mnPkBookkepingCenterId = n; }
    public void setPkAccountBizPartnerId(int n) { mnPkAccountBizPartnerId = n; }
    public void setPkDateStartId(java.util.Date t) { mtPkDateStartId = t; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkBizPartnerCategoryId() { return mnPkBizPartnerCategoryId; }
    public int getPkBizPartnerTypeId() { return mnPkBizPartnerTypeId; }
    public int getPkBookkepingCenterId() { return mnPkBookkepingCenterId; }
    public int getPkAccountBizPartnerId() { return mnPkAccountBizPartnerId; }
    public java.util.Date getPkDateStartId() { return mtPkDateStartId; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsBizPartnerCategory(java.lang.String s) { msDbmsBizPartnerCategory = s; }
    public void setDbmsBizPartnerType(java.lang.String s) { msDbmsBizPartnerType = s; }
    public void setDbmsBookkeepingCenter(java.lang.String s) { msDbmsBookkeepingCenter = s; }

    public java.lang.String getDbmsBizPartnerCategory() { return msDbmsBizPartnerCategory; }
    public java.lang.String getDbmsBizPartnerType() { return msDbmsBizPartnerType; }
    public java.lang.String getDbmsBookkeepingCenter() { return msDbmsBookkeepingCenter; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBizPartnerCategoryId = (Integer) ((Object[]) pk)[0];
        mnPkBizPartnerTypeId = (Integer) ((Object[]) pk)[1];
        mnPkBookkepingCenterId = (Integer) ((Object[]) pk)[2];
        mnPkAccountBizPartnerId = (Integer) ((Object[]) pk)[3];
        mtPkDateStartId = (java.util.Date) ((Object[]) pk)[3];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new Object[] { mnPkBizPartnerCategoryId, mnPkBizPartnerTypeId, mnPkBookkepingCenterId, mnPkAccountBizPartnerId, mtPkDateStartId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkBizPartnerCategoryId = 0;
        mnPkBizPartnerTypeId = 0;
        mnPkBookkepingCenterId = 0;
        mnPkAccountBizPartnerId = 0;
        mtPkDateStartId = null;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsBizPartnerCategory = "";
        msDbmsBizPartnerType = "";
        msDbmsBookkeepingCenter = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        Object[] key = (Object[]) pk;
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT ab.*, cb.ct_bp, tb.tp_bp, bk.bkc " +
                    "FROM fin_acc_bp_tp_bp AS ab INNER JOIN erp.bpss_ct_bp AS cb ON ab.id_ct_bp = cb.id_ct_bp " +
                    "INNER JOIN erp.bpsu_tp_bp AS tb ON ab.id_ct_bp = tb.id_ct_bp AND ab.id_tp_bp = tb.id_tp_bp " +
                    "INNER JOIN fin_bkc AS bk ON ab.id_bkc = bk.id_bkc " +
                    "WHERE ab.id_ct_bp = " + key[0] + " AND ab.id_tp_bp = " + key[1] + " AND " +
                    "ab.id_bkc = " + key[2] + " AND ab.id_acc_bp = " + key[3] + " AND ab.id_dt_start = '" + key[4] + "' ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBizPartnerCategoryId = resultSet.getInt("ab.id_ct_bp");
                mnPkBizPartnerTypeId = resultSet.getInt("ab.id_tp_bp");
                mnPkBookkepingCenterId = resultSet.getInt("ab.id_bkc");
                mnPkAccountBizPartnerId = resultSet.getInt("ab.id_acc_bp");
                mtPkDateStartId = resultSet.getDate("ab.id_dt_start");
                mbIsDeleted = resultSet.getBoolean("ab.b_del");
                mnFkUserNewId = resultSet.getInt("ab.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("ab.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("ab.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ab.ts_new");
                mtUserEditTs = resultSet.getTimestamp("ab.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ab.ts_del");

                msDbmsBizPartnerCategory = resultSet.getString("cb.ct_bp");
                msDbmsBizPartnerType = resultSet.getString("tb.tp_bp");
                msDbmsBookkeepingCenter = resultSet.getString("bk.bkc");

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
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL fin_acc_bp_tp_bp_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkBizPartnerCategoryId);
            callableStatement.setInt(nParam++, mnPkBizPartnerTypeId);
            callableStatement.setInt(nParam++, mnPkBookkepingCenterId);
            callableStatement.setInt(nParam++, mnPkAccountBizPartnerId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtPkDateStartId.getTime()));
            callableStatement.setBoolean(nParam++, mbIsDeleted);
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

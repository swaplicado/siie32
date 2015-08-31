/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDataDpsDncDocumentNumberSeries extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkDocNumberingCenterId;
    protected int mnPkDocNumberSeriesId;
    protected int mnPkNumberId;
    protected int mnNumberStart;
    protected int mnNumberEnd_n;
    protected int mnApproveYear_n;
    protected java.util.Date mtApproveDate_n;
    protected int mnApproveNumber_n;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataDpsDncDocumentNumberSeries() {
        super(SDataConstants.TRN_DNC_DPS_DNS);
        reset();
    }

    public void setPkDocNumberingCenterId(int n) { mnPkDocNumberingCenterId = n; }
    public void setPkDocNumberSeriesId(int n) { mnPkDocNumberSeriesId = n; }
    public void setPkNumberId(int n) { mnPkNumberId = n; }
    public void setNumberStart(int n) { mnNumberStart = n; }
    public void setNumberEnd_n(int n) { mnNumberEnd_n = n; }
    public void setApproveYear_n(int n) { mnApproveYear_n = n; }
    public void setApproveNumber_n(int n) { mnApproveNumber_n = n; }
    public void setApproveDate_n(java.util.Date t) { mtApproveDate_n = t; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkDocNumberingCenterId() { return mnPkDocNumberingCenterId; }
    public int getPkDocNumberSeriesId() { return mnPkDocNumberSeriesId; }
    public int getPkNumberId() { return mnPkNumberId; }
    public int getNumberStart() { return mnNumberStart; }
    public int getNumberEnd_n() { return mnNumberEnd_n; }
    public int getApproveYear_n() { return mnApproveYear_n; }
    public int getApproveNumber_n() { return mnApproveNumber_n; }
    public java.util.Date getApproveDate_n() { return mtApproveDate_n; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkDocNumberingCenterId = ((int[]) pk)[0];
        mnPkDocNumberSeriesId = ((int[]) pk)[1];
        mnPkNumberId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkDocNumberingCenterId, mnPkDocNumberSeriesId, mnPkNumberId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkDocNumberingCenterId = 0;
        mnPkDocNumberSeriesId = 0;
        mnPkNumberId = 0;
        mnNumberStart = 0;
        mnNumberEnd_n = 0;
        mnApproveYear_n = 0;
        mnApproveNumber_n = 0;
        mtApproveDate_n = null;
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
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_dnc_dps_dns WHERE id_dnc = " + key[0] + " AND id_dns = " + key[1] + "  AND id_num = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkDocNumberingCenterId = resultSet.getInt("id_dnc");
                mnPkDocNumberSeriesId = resultSet.getInt("id_dns");
                mnPkNumberId = resultSet.getInt("id_num");
                mnNumberStart = resultSet.getInt("num_start");
                mnNumberEnd_n = resultSet.getInt("num_end_n");
                mnApproveYear_n = resultSet.getInt("approve_year_n");
                mnApproveNumber_n = resultSet.getInt("approve_num_n");
                mtApproveDate_n = resultSet.getDate("approve_dt_n");
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
            callableStatement = connection.prepareCall(
                    "{ CALL trn_dnc_dps_dns_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkDocNumberingCenterId);
            callableStatement.setInt(nParam++, mnPkDocNumberSeriesId);
            callableStatement.setInt(nParam++, mnPkNumberId);
            callableStatement.setInt(nParam++, mnNumberStart);
            if (mnNumberEnd_n > 0) callableStatement.setInt(nParam++, mnNumberEnd_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnApproveYear_n > 0) callableStatement.setInt(nParam++, mnApproveYear_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnApproveNumber_n > 0) callableStatement.setInt(nParam++, mnApproveNumber_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mtApproveDate_n != null) callableStatement.setDate(nParam++, new java.sql.Date(mtApproveDate_n.getTime())); else callableStatement.setNull(nParam++, java.sql.Types.DATE);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkNumberId = callableStatement.getInt(nParam - 3);
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

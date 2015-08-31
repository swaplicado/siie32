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
 * @author Alfonso Flores
 */
public class SDataDpsEvent extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEventId;
    protected int mnFkDpsEventTypeId;
    protected int mnFkDpsPriningTypeId;
    protected int mnFkUserId;
    protected java.util.Date mtUserTs;

    public SDataDpsEvent() {
        super(SDataConstants.TRN_DPS_EVT);
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEventId(int n) { mnPkEventId = n; }
    public void setFkDpsEventTypeId(int n) { mnFkDpsEventTypeId = n; }
    public void setFkDpsPriningTypeId(int n) { mnFkDpsPriningTypeId = n; }
    public void setPkUserId(int n) { mnFkUserId = n; }
    public void setUserTs(java.util.Date t) { mtUserTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEventId() { return mnPkEventId; }
    public int getFkDpsEventTypeId() { return mnFkDpsEventTypeId; }
    public int getFkDpsPriningTypeId() { return mnFkDpsPriningTypeId; }
    public int getPkUserId() { return mnFkUserId; }
    public java.util.Date getUserTs() { return mtUserTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkEventId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEventId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEventId = 0;
        mnFkDpsEventTypeId = 0;
        mnFkDpsPriningTypeId = 0;
        mnFkUserId = 0;
        mtUserTs = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_dps_evt WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " AND id_nts = " + key[2] +  " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                mnPkEventId = resultSet.getInt("id_evt");
                mnFkDpsEventTypeId = resultSet.getInt("fid_tp_dps_evt");
                mnFkDpsPriningTypeId = resultSet.getInt("fid_tp_dps_prt");
                mnFkUserId = resultSet.getInt("fid_usr");
                mtUserTs = resultSet.getTimestamp("ts");

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
                    "{ CALL trn_diog_evt_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkDocId);
            callableStatement.setInt(nParam++, mnPkEventId);
            callableStatement.setInt(nParam++, mnFkDpsEventTypeId);
            callableStatement.setInt(nParam++, mnFkDpsPriningTypeId);
            callableStatement.setInt(nParam++, mnFkUserId);
            callableStatement.setTimestamp(nParam++, new java.sql.Timestamp(mtUserTs.getTime()));
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkEventId = callableStatement.getInt(nParam - 3);
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
        return mtUserTs;
    }
}

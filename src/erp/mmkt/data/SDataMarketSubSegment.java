/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataMarketSubSegment extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkMarketSegmentId;
    protected int mnPkMarketSubsegmentId;
    protected java.lang.String msMarketSubsegment;
    protected boolean mbIsCanEdit;
    protected boolean mbIsCanDelete;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataMarketSubSegment() {
        super(SDataConstants.MKTU_MKT_SEGM_SUB);
        reset();
    }

    public void setPkMarketSegmentId(int n) { mnPkMarketSegmentId = n; }
    public void setPkMarketSubsegmentId(int n) { mnPkMarketSubsegmentId = n; }
    public void setMarketSubsegment(java.lang.String s) { msMarketSubsegment = s; }
    public void setIsCanEdit(boolean b) { mbIsCanEdit = b; }
    public void setIsCanDelete(boolean b) { mbIsCanDelete = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkMarketSegmentId() { return mnPkMarketSegmentId; }
    public int getPkMarketSubsegmentId() { return mnPkMarketSubsegmentId; }
    public java.lang.String getMarketSubsegment() { return msMarketSubsegment; }
    public boolean getIsCanEdit() { return mbIsCanEdit; }
    public boolean getIsCanDelete() { return mbIsCanDelete; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkMarketSegmentId = ((int[]) pk)[0];
        mnPkMarketSubsegmentId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkMarketSegmentId, mnPkMarketSubsegmentId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkMarketSegmentId = 0;
        mnPkMarketSubsegmentId = 0;
        msMarketSubsegment = "";
        mbIsCanEdit = false;
        mbIsCanDelete = false;
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
            sql = "SELECT * FROM mktu_mkt_segm_sub WHERE id_mkt_segm = " + key[0] + " AND id_mkt_sub = " + key[1];
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkMarketSegmentId = resultSet.getInt("id_mkt_segm");
                mnPkMarketSubsegmentId = resultSet.getInt("id_mkt_sub");
                msMarketSubsegment = resultSet.getString("mkt_segm_sub");
                mbIsCanEdit = resultSet.getBoolean("b_can_edit");
                mbIsCanDelete = resultSet.getBoolean("b_can_del");
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
                    "{ CALL mkt_mkt_segm_sub_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkMarketSegmentId);
            callableStatement.setInt(nParam++, mnPkMarketSubsegmentId);
            callableStatement.setString(nParam++, msMarketSubsegment);
            callableStatement.setBoolean(nParam++, mbIsCanEdit);
            callableStatement.setBoolean(nParam++, mbIsCanDelete);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkMarketSubsegmentId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}

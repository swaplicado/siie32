/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

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
public class SDataManufacturingLine extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkMfgLineId;
    protected java.lang.String msMfgLine;
    protected boolean mbIsDeleted;
    protected int mnFkCostCenterId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsPkCostCenterId;
    protected java.lang.String msDbmsCostCenter;

    public SDataManufacturingLine() {
        super(SDataConstants.MFGU_LINE);
        reset();
    }

    public void setPkMfgLineId(int n) { mnPkMfgLineId = n; }
    public void setMfgLine(java.lang.String s) { msMfgLine = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkCostCenterId(int n) { mnFkCostCenterId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkMfgLineId() { return mnPkMfgLineId; }
    public java.lang.String getMfgLine() { return msMfgLine; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkCostCenterId() { return mnFkCostCenterId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsPkCostCenterId(java.lang.String s) { msDbmsPkCostCenterId = s; }
    public void setDbmsCostCenter(java.lang.String s) { msDbmsCostCenter = s; }

    public java.lang.String getDbmsPkCostCenterId() { return msDbmsPkCostCenterId; }
    public java.lang.String getDbmsCostCenter() { return msDbmsCostCenter; }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkMfgLineId = 0;
        msMfgLine = "";
        mbIsDeleted = false;
        mnFkCostCenterId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsPkCostCenterId = "";
        msDbmsCostCenter = "";
    }

    @Override
    public void setPrimaryKey(java.lang.Object key) {
        mnPkMfgLineId = ((int[]) key)[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMfgLineId };
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";

        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT l.*, cc.id_cc, cc.cc " +
                "FROM mfgu_line AS l " +
                "INNER JOIN fin_cc AS cc ON " +
                "l.fid_cc = cc.pk_cc " +
                "WHERE l.id_line = " + key[0] + " ";

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkMfgLineId = resultSet.getInt("l.id_line");
                msMfgLine = resultSet.getString("l.line");
                mbIsDeleted = resultSet.getBoolean("l.b_del");
                mnFkCostCenterId = resultSet.getInt("l.fid_cc");
                mnFkUserNewId = resultSet.getInt("l.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("l.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("l.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("l.ts_new");
                mtUserEditTs = resultSet.getTimestamp("l.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("l.ts_del");

                msDbmsPkCostCenterId = resultSet.getString("cc.id_cc");
                msDbmsCostCenter = resultSet.getString("cc.cc");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int i = 0;
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mfgu_line_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkMfgLineId);
            callableStatement.setString(nParam++, msMfgLine);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkCostCenterId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkMfgLineId = callableStatement.getInt(nParam - 3);
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
    public int delete(java.sql.Connection connection) {
        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}

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
public class SDataCostCenter extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected java.lang.String msPkCostCenterIdXXX;
    protected int mnPkCostCenterId;
    protected java.lang.String msCode;
    protected java.lang.String msCostCenter;
    protected int mnDeep;
    protected int mnLevel;
    protected java.util.Date mtDateStart;
    protected java.util.Date mtDateEnd_n;
    protected boolean mbIsActive;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected int mnDbmsMajorDeep;
    protected java.lang.String msDbmsPkCostCenterMajorId;

    public SDataCostCenter() {
        super(SDataConstants.FIN_CC);
        reset();
    }

    public void setPkCostCenterIdXXX(java.lang.String s) { msPkCostCenterIdXXX = s; }
    public void setPkCostCenterId(int n) { mnPkCostCenterId = n; }
    public void setCode(java.lang.String s) { msCode = s; }
    public void setCostCenter(java.lang.String s) { msCostCenter = s; }
    public void setDeep(int n) { mnDeep = n; }
    public void setLevel(int n) { mnLevel = n; }
    public void setDateStart(java.util.Date t) { mtDateStart = t; }
    public void setDateEnd_n(java.util.Date t) { mtDateEnd_n = t; }
    public void setIsActive(boolean b) { mbIsActive = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public java.lang.String getPkCostCenterIdXXX() { return msPkCostCenterIdXXX; }
    public int getPkCostCenterId() { return mnPkCostCenterId; }
    public java.lang.String getCode() { return msCode; }
    public java.lang.String getCostCenter() { return msCostCenter; }
    public int getDeep() { return mnDeep; }
    public int getLevel() { return mnLevel; }
    public java.util.Date getDateStart() { return mtDateStart; }
    public java.util.Date getDateEnd_n() { return mtDateEnd_n; }
    public boolean getIsActive() { return mbIsActive; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsMajorDeep(int n) { mnDbmsMajorDeep = n; }

    public int getDbmsMajorDeep() { return mnDbmsMajorDeep; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        msPkCostCenterIdXXX = (String) ((Object[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new Object[] { msPkCostCenterIdXXX };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        msPkCostCenterIdXXX = "";
        mnPkCostCenterId = 0;
        msCode = "";
        msCostCenter = "";
        mnDeep = 0;
        mnLevel = 0;
        mtDateStart = null;
        mtDateEnd_n = null;
        mbIsActive = false;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mnDbmsMajorDeep = 0;
        msDbmsPkCostCenterMajorId = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        Object[] key = (Object[]) pk;
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM fin_cc WHERE id_cc = '" + key[0] + "' ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                msPkCostCenterIdXXX = resultSet.getString("id_cc");
                mnPkCostCenterId = resultSet.getInt("pk_cc");
                msCode = resultSet.getString("code");
                msCostCenter = resultSet.getString("cc");
                mnDeep = resultSet.getInt("deep");
                mnLevel = resultSet.getInt("lev");
                mtDateStart = resultSet.getDate("dt_start");
                mtDateEnd_n = resultSet.getDate("dt_end_n");
                mbIsActive = resultSet.getBoolean("b_act");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                // Read aswell major cost center's deep:

                sql = "SELECT fmt_id_cc FROM erp.cfg_param_erp ";  // read first cost center format
                resultSet = statement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    msDbmsPkCostCenterMajorId = resultSet.getString(1);
                    msDbmsPkCostCenterMajorId = msPkCostCenterIdXXX.substring(0, msPkCostCenterIdXXX.indexOf('-')) + msDbmsPkCostCenterMajorId.substring(msPkCostCenterIdXXX.indexOf('-')).replace('9', '0');

                    sql = "SELECT deep FROM fin_cc WHERE id_cc = '" + msDbmsPkCostCenterMajorId + "' ";
                    resultSet = statement.executeQuery(sql);
                    if (!resultSet.next()) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mnDbmsMajorDeep = resultSet.getInt("deep");

                        mbIsRegistryNew = false;
                        mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
                    }
                }
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
                    "{ CALL fin_cc_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?) }");
            callableStatement.setString(nParam++, msPkCostCenterIdXXX);
            callableStatement.setInt(nParam++, mnPkCostCenterId);
            callableStatement.setString(nParam++, msCode);
            callableStatement.setString(nParam++, msCostCenter);
            callableStatement.setInt(nParam++, mnDeep);
            callableStatement.setInt(nParam++, mnLevel);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDateStart.getTime()));
            if (mtDateEnd_n != null) callableStatement.setDate(nParam++, new java.sql.Date(mtDateEnd_n.getTime())); else callableStatement.setNull(nParam++, java.sql.Types.DATE);
            callableStatement.setBoolean(nParam++, mbIsActive);
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
        return mtUserEditTs;
    }
}

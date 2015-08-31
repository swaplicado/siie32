/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.util.Date;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataDrcEntryRecord extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkDrcYearId;
    protected int mnPkDrcDocId;
    protected int mnPkDrcEntryId;
    protected java.util.Date mtPkDrcDayId;
    protected int mnPkRecordYearId;
    protected int mnPkRecordPeriodId;
    protected int mnPkRecordBookkeepingCenterId;
    protected java.lang.String msPkRecordTypeId;
    protected int mnPkRecordNumberId;

    public SDataDrcEntryRecord() {
        super(SDataConstants.MFG_BOM); //DRC_ETY_REC
        reset();
    }

    public void setPkDrcYearId(int n) { mnPkDrcYearId = n; }
    public void setPkDrcDocId(int n) { mnPkDrcDocId = n; }
    public void setPkDrcEntryId(int n) { mnPkDrcEntryId = n; }
    public void setPkDrcDayId(java.util.Date t) { mtPkDrcDayId = t; }
    public void setPkRecordYearId(int n) { mnPkRecordYearId = n; }
    public void setPkRecordPeriodId(int n) { mnPkRecordPeriodId = n; }
    public void setPkRecordBookkeepingCenterId(int n) { mnPkRecordBookkeepingCenterId = n; }
    public void setPkRecordTypeId(java.lang.String s) { msPkRecordTypeId = s; }
    public void setPkRecordNumberId(int n) { mnPkRecordNumberId = n; }

    public int getPkDrcYearId() { return mnPkDrcYearId; }
    public int getPkDrcDocId() { return mnPkDrcDocId; }
    public int getPkDrcEntryId() { return mnPkDrcEntryId; }
    public java.util.Date getPkDrcDayId() { return mtPkDrcDayId; }
    public int getPkRecordYearId() { return mnPkRecordYearId; }
    public int getPkRecordPeriodId() { return mnPkRecordPeriodId; }
    public int getPkRecordBookkeepingCenterId() { return mnPkRecordBookkeepingCenterId; }
    public java.lang.String getPkRecordTypeId() { return msPkRecordTypeId; }
    public int getPkRecordNumberId() { return mnPkRecordNumberId; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkDrcYearId = (Integer)((java.lang.Object[]) pk)[0];
        mnPkDrcDocId = (Integer)((java.lang.Object[]) pk)[1];
        mnPkDrcEntryId = (Integer)((java.lang.Object[]) pk)[2];
        mtPkDrcDayId = (java.util.Date)((java.lang.Object[]) pk)[3];
        mnPkRecordYearId = (Integer)((java.lang.Object[]) pk)[4];
        mnPkRecordPeriodId = (Integer)((java.lang.Object[]) pk)[5];
        mnPkRecordBookkeepingCenterId = (Integer)((java.lang.Object[]) pk)[6];
        msPkRecordTypeId = (java.lang.String)((java.lang.Object[]) pk)[7];
        mnPkRecordNumberId = (Integer)((java.lang.Object[]) pk)[8];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new Object[] { mnPkDrcYearId, mnPkDrcDocId, mnPkDrcEntryId, mtPkDrcDayId, mnPkRecordYearId, mnPkRecordPeriodId, mnPkRecordPeriodId, mnPkRecordBookkeepingCenterId, msPkRecordTypeId, mnPkRecordNumberId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkDrcYearId = 0;
        mnPkDrcDocId = 0;
        mnPkDrcEntryId = 0;
        mtPkDrcDayId = null;
        mnPkRecordYearId = 0;
        mnPkRecordPeriodId = 0;
        mnPkRecordBookkeepingCenterId = 0;
        msPkRecordTypeId = "";
        mnPkRecordNumberId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * " +
                    "FROM mfg_drc_ety_rec " +
                    "WHERE id_drc_year = " + key[0] + " AND id_drc_doc =  " + key[1] + " AND id_drc_ety = " + key[2] + " AND id_drc_day = " + key[3] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkDrcYearId = resultSet.getInt("id_drc_year");
                mnPkDrcDocId = resultSet.getInt("id_drc_doc");
                mnPkDrcEntryId = resultSet.getInt("id_drc_ety");
                mtPkDrcDayId = resultSet.getDate("id_drc_day");
                mnPkRecordYearId = resultSet.getInt("id_rec_year");
                mnPkRecordPeriodId = resultSet.getInt("id_rec_per");
                mnPkRecordBookkeepingCenterId = resultSet.getInt("id_rec_bkc");
                msPkRecordTypeId = resultSet.getString("id_rec_tp_rec");
                mnPkRecordNumberId = resultSet.getInt("id_rec_num");

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
                    "{ CALL mfg_drc_ety_rec_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?) }");
            callableStatement.setInt(nParam++, mnPkDrcYearId);
            callableStatement.setInt(nParam++, mnPkDrcDocId);
            callableStatement.setInt(nParam++, mnPkDrcEntryId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtPkDrcDayId.getTime()));
            callableStatement.setInt(nParam++, mnPkRecordYearId);
            callableStatement.setInt(nParam++, mnPkRecordPeriodId);
            callableStatement.setInt(nParam++, mnPkRecordBookkeepingCenterId);
            callableStatement.setString(nParam++, msPkRecordTypeId);
            callableStatement.setInt(nParam++, mnPkRecordNumberId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();
            
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
        return null;
    }
}

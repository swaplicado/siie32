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
public class SDataDrcEntryHour extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected java.util.Date mtPkDayId;
    protected int mnPkHourTypeId;
    protected double mdQuantity;

    public SDataDrcEntryHour() {
        super(SDataConstants.MFG_DRC_ETY_HR);
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setPkDayId(java.util.Date t) { mtPkDayId = t; }
    public void setPkHourType(int n) { mnPkHourTypeId = n; }
    public void setQuantity(double d) { mdQuantity = d; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public java.util.Date getPkDayId() { return mtPkDayId; }
    public int getPkHourTypeId() { return mnPkHourTypeId; }
    public double getQuantity() { return mdQuantity; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = (Integer)((java.lang.Object[]) pk)[0];
        mnPkDocId = (Integer)((java.lang.Object[]) pk)[1];
        mnPkEntryId = (Integer)((java.lang.Object[]) pk)[2];
        mtPkDayId = (java.util.Date)((java.lang.Object[]) pk)[3];
        mnPkEntryId = (Integer)((java.lang.Object[]) pk)[4];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new Object[] { mnPkYearId, mnPkDocId, mnPkEntryId, mtPkDayId, mnPkEntryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        mtPkDayId = null;
        mnPkHourTypeId = 0;
        mdQuantity = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        java.lang.Object[] key = (java.lang.Object[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * " +
                    "FROM mfg_drc_ety_hr " +
                    "WHERE id_year = " + key[0] + " AND id_doc =  " + key[1] + " AND id_ety = " + key[2] + " AND id_day = '" + key[3] + "' AND id_tp_hr = " + key[4];
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                mnPkEntryId = resultSet.getInt("id_ety");
                mtPkDayId = resultSet.getDate("id_day");
                mnPkHourTypeId = resultSet.getInt("id_tp_hr");
                mdQuantity = resultSet.getDouble("qty");

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
                    "{ CALL mfg_drc_ety_hr_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkDocId);
            callableStatement.setInt(nParam++, mnPkEntryId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtPkDayId.getTime()));
            callableStatement.setInt(nParam++, mnPkHourTypeId);
            callableStatement.setDouble(nParam++, mdQuantity);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
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

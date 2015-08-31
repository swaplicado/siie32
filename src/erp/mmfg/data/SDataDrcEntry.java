/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.util.Vector;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.util.Date;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataDrcEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected java.util.Date mtPkDayId;
    protected boolean mbIsDeleted;
    protected int mnFkOrdYearId_n;
    protected int mnFkOrdDocId_n;
    protected int mnFkCobId_n;
    protected int mnFkEntId_n;
    protected int mnFkCostGicId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsProductionOrder;
    protected java.lang.String msDbmsPlant;
    protected java.lang.String[] masDbmsAccouts;

    protected java.util.Vector<erp.mmfg.data.SDataDrcEntryHour> mvDbmsDrcEntryHours;

    public SDataDrcEntry() {
        super(SDataConstants.MFG_DRC_ETY);

        mvDbmsDrcEntryHours = new Vector<erp.mmfg.data.SDataDrcEntryHour>();

        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setPkDayId(java.util.Date t) { mtPkDayId = t; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkOrdYearId_n(int n) { mnFkOrdYearId_n = n; }
    public void setFkOrdDocId_n(int n) { mnFkOrdDocId_n = n; }
    public void setFkCobId_n(int n) { mnFkCobId_n = n; }
    public void setFkEntId_n(int n) { mnFkEntId_n = n; }
    public void setFkCostGicId_n(int n) { mnFkCostGicId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public java.util.Date getPkDayId() { return mtPkDayId; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkOrdYearId_n() { return mnFkOrdYearId_n; }
    public int getFkOrdDocId_n() { return mnFkOrdDocId_n; }
    public int getFkCobId_n() { return mnFkCobId_n; }
    public int getFkEntId_n() { return mnFkEntId_n; }
    public int getFkCostGicId_n() { return mnFkCostGicId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsProductionOrder(java.lang.String s) { msDbmsProductionOrder = s; }
    public void setDbmsPlant(java.lang.String s) { msDbmsPlant = s; }
    public void setDbmsAccounts(java.lang.String[] as) { masDbmsAccouts = as; }

    public java.lang.String getDbmsProductionOrder() { return msDbmsProductionOrder; }
    public java.lang.String getDbmsPlant() { return msDbmsPlant; }
    public java.lang.String[] getDbmsAccounts() { return masDbmsAccouts; }

    public java.util.Vector<SDataDrcEntryHour> getDbmsDrcEntriesHours() { return mvDbmsDrcEntryHours; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = (Integer)((java.lang.Object[]) pk)[0];
        mnPkDocId = (Integer)((java.lang.Object[]) pk)[1];
        mnPkEntryId = (Integer)((java.lang.Object[]) pk)[2];
        mtPkDayId = (java.util.Date)((java.lang.Object[]) pk)[3];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        mtPkDayId = null;
        mbIsDeleted = false;
        mnFkOrdYearId_n = 0;
        mnFkOrdDocId_n = 0;
        mnFkCobId_n = 0;
        mnFkEntId_n = 0;
        mnFkCostGicId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsProductionOrder = "";
        msDbmsPlant = "";
        masDbmsAccouts = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        java.lang.Object[] key = (java.lang.Object[]) pk;
        String sql;

        Statement statementAux = null;
        ResultSet resultSet = null;

        SDataDrcEntryHour oDrcEntryHour = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT e.*, en.code, CONCAT(m.id_year, '-',erp.lib_fix_int(m.id_ord,6), ' ', m.ref) AS num " +
                    "FROM mfg_drc_ety AS e " +
                    "LEFT OUTER JOIN mfg_ord AS m ON e.fid_ord_year_n = m.id_year AND e.fid_ord_ord_n = m.id_ord " +
                    "LEFT OUTER JOIN erp.cfgu_cob_ent AS en ON e.fid_cob_n = en.id_cob AND e.fid_ent_n = en.id_ent " +                    
                    "WHERE e.id_year = " + key[0] + " AND e.id_doc =  " + key[1] + " AND e.id_ety = " + key[2] + " " +
                    "GROUP BY e.id_year, e.id_doc, e.id_ety "; // Group by day (date)
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("e.id_year");
                mnPkDocId = resultSet.getInt("e.id_doc");
                mnPkEntryId = resultSet.getInt("e.id_ety");
                mtPkDayId = resultSet.getDate("e.id_day");
                mbIsDeleted = resultSet.getBoolean("e.b_del");
                mnFkOrdYearId_n = resultSet.getInt("e.fid_ord_year_n");
                mnFkOrdDocId_n = resultSet.getInt("e.fid_ord_ord_n");
                mnFkCobId_n = resultSet.getInt("e.fid_cob_n");
                mnFkEntId_n = resultSet.getInt("e.fid_ent_n");
                mnFkCostGicId_n = resultSet.getInt("e.fid_cost_gic_n");
                mnFkUserNewId = resultSet.getInt("e.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("e.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("e.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("e.ts_new");
                mtUserEditTs = resultSet.getTimestamp("e.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("e.ts_del");

                msDbmsProductionOrder = resultSet.getString("num");
                msDbmsPlant = resultSet.getString("en.code");

                // Read entries:

                statementAux = statement.getConnection().createStatement();
                mvDbmsDrcEntryHours.removeAllElements();
                sql = "SELECT * FROM mfg_drc_ety_hr " +
                    "WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " AND id_ety = " + key[2] + " " +
                    "ORDER BY id_year, id_doc, id_ety, id_day, id_tp_hr ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    oDrcEntryHour = new SDataDrcEntryHour();
                    if (oDrcEntryHour.read(new Object[] { resultSet.getInt("id_year"), resultSet.getInt("id_doc"), resultSet.getInt("id_ety"), resultSet.getDate("id_day"), resultSet.getInt("id_tp_hr") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsDrcEntryHours.add(oDrcEntryHour);
                    }
                }

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

        SDataDrcEntryHour oDrcEntryHour = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mfg_drc_ety_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkDocId);
            callableStatement.setInt(nParam++, mnPkEntryId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtPkDayId.getTime()));
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            if (mnFkOrdYearId_n > 0) callableStatement.setInt(nParam++, mnFkOrdYearId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkOrdDocId_n > 0) callableStatement.setInt(nParam++, mnFkOrdDocId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkCobId_n > 0) callableStatement.setInt(nParam++, mnFkCobId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkEntId_n > 0) callableStatement.setInt(nParam++, mnFkEntId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkCostGicId_n > 0) callableStatement.setInt(nParam++, mnFkCostGicId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkEntryId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            }
            else {
                // Save entries hours:

                for (int i=0; i<mvDbmsDrcEntryHours.size(); i++) {
                    oDrcEntryHour = (SDataDrcEntryHour) mvDbmsDrcEntryHours.get(i);
                    if (oDrcEntryHour != null) {
                        oDrcEntryHour.setPkYearId(mnPkYearId);
                        oDrcEntryHour.setPkDocId(mnPkDocId);
                        oDrcEntryHour.setPkEntryId(mnPkEntryId);
                        oDrcEntryHour.setPkDayId(mtPkDayId);
                        oDrcEntryHour.setPkHourType(oDrcEntryHour.getPkHourTypeId());
                        
                        if (oDrcEntryHour.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }
            }

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

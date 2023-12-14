/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 *
 * @author Alfonso Flores, Juan Barajas, Sergio Flores
 */
public class SDataUnit extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkUnitId;
    protected java.lang.String msUnit;
    protected java.lang.String msUnitCustoms;
    protected java.lang.String msSymbol;
    protected java.lang.String msSymbolCustoms;
    protected double mdUnitBaseEquivalence;
    protected int mnSortingPosition;
    protected boolean mbIsCanEdit;
    protected boolean mbIsCanDelete;
    protected boolean mbIsDeleted;
    protected int mnFkUnitTypeId;
    protected int mnFkCfdUnitId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;
    
    protected boolean mbAuxHasEquivalentUnits;

    public SDataUnit() {
        super(SDataConstants.ITMU_UNIT);
        reset();
    }

    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setUnit(java.lang.String s) { msUnit = s; }
    public void setUnitCustoms(java.lang.String s) { msUnitCustoms = s; }
    public void setSymbol(java.lang.String s) { msSymbol = s; }
    public void setSymbolCustoms(java.lang.String s) { msSymbolCustoms = s; }
    public void setUnitBaseEquivalence(double d) { mdUnitBaseEquivalence = d; }
    public void setSortingPosition(int n) { mnSortingPosition = n; }
    public void setIsCanEdit(boolean b) { mbIsCanEdit = b; }
    public void setIsCanDelete(boolean b) { mbIsCanDelete = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUnitTypeId(int n) { mnFkUnitTypeId = n; }
    public void setFkCfdUnitId(int n) { mnFkCfdUnitId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkUnitId() { return mnPkUnitId; }
    public java.lang.String getUnit() { return msUnit; }
    public java.lang.String getUnitCustoms() { return msUnitCustoms; }
    public java.lang.String getSymbol() { return msSymbol; }
    public java.lang.String getSymbolCustoms() { return msSymbolCustoms; }
    public double getUnitBaseEquivalence() { return mdUnitBaseEquivalence; }
    public int getSortingPosition() { return mnSortingPosition; }
    public boolean getIsCanEdit() { return mbIsCanEdit; }
    public boolean getIsCanDelete() { return mbIsCanDelete; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUnitTypeId() { return mnFkUnitTypeId; }
    public int getFkCfdUnitId() { return mnFkCfdUnitId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    public boolean hasEquivalentUnits() { return mbAuxHasEquivalentUnits; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkUnitId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkUnitId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkUnitId = 0;
        msUnit = "";
        msUnitCustoms = "";
        msSymbol = "";
        msSymbolCustoms = "";
        mdUnitBaseEquivalence = 0;
        mnSortingPosition = 0;
        mbIsCanEdit = false;
        mbIsCanDelete = false;
        mbIsDeleted = false;
        mnFkUnitTypeId = 0;
        mnFkCfdUnitId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
        
        mbAuxHasEquivalentUnits = false;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.itmu_unit WHERE id_unit = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkUnitId = resultSet.getInt("id_unit");
                msUnit = resultSet.getString("unit");
                msUnitCustoms = resultSet.getString("unit_custs");
                msSymbol = resultSet.getString("symbol");
                msSymbolCustoms = resultSet.getString("symbol_custs");
                mdUnitBaseEquivalence = resultSet.getDouble("unit_base_equiv");
                mnSortingPosition = resultSet.getInt("sort_pos");
                mbIsCanEdit = resultSet.getBoolean("b_can_edit");
                mbIsCanDelete = resultSet.getBoolean("b_can_del");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUnitTypeId = resultSet.getInt("fid_tp_unit");
                mnFkCfdUnitId = resultSet.getInt("fid_cfd_unit");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
            
            sql = "SELECT " +
                "COUNT(*) > 0 AS has_equiv " +
                "FROM " +
                "    erp.itmu_unit_equiv " +
                "WHERE " +
                "    NOT b_del " +
                "        AND (id_unit = " + key[0] + " OR id_unit_equiv = " + key[0] + ");";
            
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                mbAuxHasEquivalentUnits = resultSet.getBoolean("has_equiv");
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
                    "{ CALL erp.itmu_unit_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkUnitId);
            callableStatement.setString(nParam++, msUnit);
            callableStatement.setString(nParam++, msUnitCustoms);
            callableStatement.setString(nParam++, msSymbol);
            callableStatement.setString(nParam++, msSymbolCustoms);
            callableStatement.setDouble(nParam++, mdUnitBaseEquivalence);
            callableStatement.setInt(nParam++, mnSortingPosition);
            callableStatement.setBoolean(nParam++, mbIsCanEdit);
            callableStatement.setBoolean(nParam++, mbIsCanDelete);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkUnitTypeId);
            callableStatement.setInt(nParam++, mnFkCfdUnitId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkUnitId = callableStatement.getInt(nParam - 3);
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

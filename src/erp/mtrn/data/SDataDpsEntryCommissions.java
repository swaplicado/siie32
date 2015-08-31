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
public class SDataDpsEntryCommissions extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected int mnPkCommissionsId;
    protected double mdPercentage;
    protected double mdPercentageSystem;
    protected double mdValueUnitary;
    protected double mdValueUnitarySystem;
    protected double mdValue;
    protected double mdValueSystem;
    protected double mdCommissions;
    protected double mdCommissionsSystem;
    protected double mdCommissionsCy;
    protected double mdCommissionsSystemCy;
    protected int mnFkCommissionsTypeId;
    protected int mnFkCommissionsSystemTypeId;

    protected java.lang.String msDbmsCommissionsType;
    protected java.lang.String msDbmsCommissionsSystemType;

    /**
     * Overrides java.lang.Object.clone() function.
     */
    public SDataDpsEntryCommissions() {
        super(SDataConstants.TRN_DPS_ETY_COMMS);
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setPkCommissionsId(int n) { mnPkCommissionsId = n; }
    public void setPercentage(double d) { mdPercentage = d; }
    public void setPercentageSystem(double d) { mdPercentageSystem = d; }
    public void setValueUnitary(double d) { mdValueUnitary = d; }
    public void setValueUnitarySystem(double d) { mdValueUnitarySystem = d; }
    public void setValue(double d) { mdValue = d; }
    public void setValueSystem(double d) { mdValueSystem = d; }
    public void setCommissions(double d) { mdCommissions = d; }
    public void setCommissionsSystem(double d) { mdCommissionsSystem = d; }
    public void setCommissionsCy(double d) { mdCommissionsCy = d; }
    public void setCommissionsSystemCy(double d) { mdCommissionsSystemCy = d; }
    public void setFkCommissionsTypeId(int n) { mnFkCommissionsTypeId = n; }
    public void setFkCommissionsSystemTypeId(int n) { mnFkCommissionsSystemTypeId = n; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public int getPkCommissionsId() { return mnPkCommissionsId; }
    public double getPercentage() { return mdPercentage; }
    public double getPercentageSystem() { return mdPercentageSystem; }
    public double getValueUnitary() { return mdValueUnitary; }
    public double getValueUnitarySystem() { return mdValueUnitarySystem; }
    public double getValue() { return mdValue; }
    public double getValueSystem() { return mdValueSystem; }
    public double getCommissions() { return mdCommissions; }
    public double getCommissionsSystem() { return mdCommissionsSystem; }
    public double getCommissionsCy() { return mdCommissionsCy; }
    public double getCommissionsSystemCy() { return mdCommissionsSystemCy; }
    public int getFkCommissionsTypeId() { return mnFkCommissionsTypeId; }
    public int getFkCommissionsSystemTypeId() { return mnFkCommissionsSystemTypeId; }

    public void setDbmsCommissionsType(java.lang.String s) { msDbmsCommissionsType = s; }
    public void setDbmsCommissionsSystemType(java.lang.String s) { msDbmsCommissionsSystemType = s; }

    public java.lang.String getDbmsCommissionsType() { return msDbmsCommissionsType; }
    public java.lang.String getDbmsCommissionsSystemType() { return msDbmsCommissionsSystemType; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkEntryId = ((int[]) pk)[2];
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
        mnPkCommissionsId = 0;
        mdPercentage = 0;
        mdPercentageSystem = 0;
        mdValueUnitary = 0;
        mdValueUnitarySystem = 0;
        mdValue = 0;
        mdValueSystem = 0;
        mdCommissions = 0;
        mdCommissionsSystem = 0;
        mdCommissionsCy = 0;
        mdCommissionsSystemCy = 0;
        mnFkCommissionsTypeId = 0;
        mnFkCommissionsSystemTypeId = 0;

        msDbmsCommissionsType = "";
        msDbmsCommissionsSystemType = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT c.*, tc.tp_comms, tcs.tp_comms " +
                    "FROM trn_dps_ety_comms AS c " +
                    "INNER JOIN erp.mkts_tp_comms AS tc ON " +
                    "c.fid_tp_comms = tc.id_tp_comms " +
                    "INNER JOIN erp.mkts_tp_comms AS tcs ON " +
                    "c.fid_tp_comms_sys = tcs.id_tp_comms " +
                    "WHERE c.id_year = " + key[0] + " AND c.id_doc = " + key[1] + " AND c.id_ety = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("c.id_year");
                mnPkDocId = resultSet.getInt("c.id_doc");
                mnPkEntryId = resultSet.getInt("c.id_ety");
                mnPkCommissionsId = resultSet.getInt("c.id_comms");
                mdPercentage = resultSet.getDouble("c.per");
                mdPercentageSystem = resultSet.getDouble("c.per_sys");
                mdValueUnitary = resultSet.getDouble("c.val_u");
                mdValueUnitarySystem = resultSet.getDouble("c.val_u_sys");
                mdValue = resultSet.getDouble("c.val");
                mdValueSystem = resultSet.getDouble("c.val_sys");
                mdCommissions = resultSet.getDouble("c.comms");
                mdCommissionsSystem = resultSet.getDouble("c.comms_sys");
                mdCommissionsCy = resultSet.getDouble("c.comms_cur");
                mdCommissionsSystemCy = resultSet.getDouble("c.comms_sys_cur");
                mnFkCommissionsTypeId = resultSet.getInt("c.fid_tp_comms");
                mnFkCommissionsSystemTypeId = resultSet.getInt("c.fid_tp_comms_sys");

                msDbmsCommissionsType = resultSet.getString("tc.tp_comms");
                msDbmsCommissionsSystemType = resultSet.getString("tcs.tp_comms");

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
                    "{ CALL trn_dps_ety_comms_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkDocId);
            callableStatement.setInt(nParam++, mnPkEntryId);
            callableStatement.setInt(nParam++, mnPkCommissionsId);
            callableStatement.setDouble(nParam++, mdPercentage);
            callableStatement.setDouble(nParam++, mdPercentageSystem);
            callableStatement.setDouble(nParam++, mdValueUnitary);
            callableStatement.setDouble(nParam++, mdValueUnitarySystem);
            callableStatement.setDouble(nParam++, mdValue);
            callableStatement.setDouble(nParam++, mdValueSystem);
            callableStatement.setDouble(nParam++, mdCommissions);
            callableStatement.setDouble(nParam++, mdCommissionsSystem);
            callableStatement.setDouble(nParam++, mdCommissionsCy);
            callableStatement.setDouble(nParam++, mdCommissionsSystemCy);
            callableStatement.setInt(nParam++, mnFkCommissionsTypeId);
            callableStatement.setInt(nParam++, mnFkCommissionsSystemTypeId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnDbmsErrorId = callableStatement.getInt(nParam - 3);
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
        return null;
    }

    @Override
    public erp.mtrn.data.SDataDpsEntryCommissions clone() {
        SDataDpsEntryCommissions clone = new SDataDpsEntryCommissions();

        clone.setIsRegistryNew(mbIsRegistryNew);

        clone.setPkYearId(mnPkYearId);
        clone.setPkDocId(mnPkDocId);
        clone.setPkEntryId(mnPkEntryId);
        clone.setPkCommissionsId(mnPkCommissionsId);
        clone.setPercentage(mdPercentage);
        clone.setPercentageSystem(mdPercentageSystem);
        clone.setValueUnitary(mdValueUnitary);
        clone.setValueUnitarySystem(mdValueUnitarySystem);
        clone.setValue(mdValue);
        clone.setValueSystem(mdValueSystem);
        clone.setCommissions(mdCommissions);
        clone.setCommissionsSystem(mdCommissionsSystem);
        clone.setCommissionsCy(mdCommissionsCy);
        clone.setCommissionsSystemCy(mdCommissionsSystemCy);
        clone.setFkCommissionsTypeId(mnFkCommissionsTypeId);
        clone.setFkCommissionsSystemTypeId(mnFkCommissionsSystemTypeId);

        clone.setDbmsCommissionsType(msDbmsCommissionsType);
        clone.setDbmsCommissionsSystemType(msDbmsCommissionsSystemType);

        return clone;
    }
}

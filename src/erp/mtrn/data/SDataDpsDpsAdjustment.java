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
 * @author Sergio Flores, Alfonso Flores
 */
public class SDataDpsDpsAdjustment extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkDpsYearId;
    protected int mnPkDpsDocId;
    protected int mnPkDpsEntryId;
    protected int mnPkDpsAdjustmentYearId;
    protected int mnPkDpsAdjustmentDocId;
    protected int mnPkDpsAdjustmentEntryId;
    protected double mdQuantity;
    protected double mdOriginalQuantity;
    protected double mdValue;
    protected double mdValueCy;

    protected java.util.Date mtAuxDpsTimestamp;
    protected java.util.Date mtAuxDpsAdjustmentTimestamp;
    protected erp.mtrn.data.SDataDpsEntry moAuxDpsEntryComplementary;

    protected int mnDbmsFkDpsStatusId;
    protected int mnDbmsFkDpsAdjustmentStatusId;
    protected boolean mbDbmsIsDpsDeleted;
    protected boolean mbDbmsIsDpsEntryDeleted;
    protected boolean mbDbmsIsDpsAdjustmentDeleted;
    protected boolean mbDbmsIsDpsAdjustmentEntryDeleted;

    /**
     * Overrides java.lang.Object.clone() function.
     */
    public SDataDpsDpsAdjustment() {
        super(SDataConstants.TRN_DPS_DPS_SUPPLY);
        reset();
    }

    public void setPkDpsYearId(int n) { mnPkDpsYearId = n; }
    public void setPkDpsDocId(int n) { mnPkDpsDocId = n; }
    public void setPkDpsEntryId(int n) { mnPkDpsEntryId = n; }
    public void setPkDpsAdjustmentYearId(int n) { mnPkDpsAdjustmentYearId = n; }
    public void setPkDpsAdjustmentDocId(int n) { mnPkDpsAdjustmentDocId = n; }
    public void setPkDpsAdjustmentEntryId(int n) { mnPkDpsAdjustmentEntryId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setOriginalQuantity(double d) { mdOriginalQuantity = d; }
    public void setValue(double d) { mdValue = d; }
    public void setValueCy(double d) { mdValueCy = d; }

    public int getPkDpsYearId() { return mnPkDpsYearId; }
    public int getPkDpsDocId() { return mnPkDpsDocId; }
    public int getPkDpsEntryId() { return mnPkDpsEntryId; }
    public int getPkDpsAdjustmentYearId() { return mnPkDpsAdjustmentYearId; }
    public int getPkDpsAdjustmentDocId() { return mnPkDpsAdjustmentDocId; }
    public int getPkDpsAdjustmentEntryId() { return mnPkDpsAdjustmentEntryId; }
    public double getQuantity() { return mdQuantity; }
    public double getOriginalQuantity() { return mdOriginalQuantity; }
    public double getValue() { return mdValue; }
    public double getValueCy() { return mdValueCy; }

    public int[] getDbmsDpsKey() { return new int[] { mnPkDpsYearId, mnPkDpsDocId }; }
    public int[] getDbmsDpsEntryKey() { return new int[] { mnPkDpsYearId, mnPkDpsDocId, mnPkDpsEntryId }; }
    public int[] getDbmsDpsAdjustmentKey() { return new int[] { mnPkDpsAdjustmentYearId, mnPkDpsAdjustmentDocId }; }
    public int[] getDbmsDpsAdjustmentEntryKey() { return new int[] { mnPkDpsAdjustmentYearId, mnPkDpsAdjustmentDocId, mnPkDpsAdjustmentEntryId }; }

    public void setAuxDpsTimestamp(java.util.Date t) { mtAuxDpsTimestamp = t; }
    public void setAuxDpsAdjustmentTimestamp(java.util.Date t) { mtAuxDpsAdjustmentTimestamp = t; }
    public void setAuxDpsEntryComplementary(erp.mtrn.data.SDataDpsEntry o) { moAuxDpsEntryComplementary = o; }

    public void setDbmsFkDpsStatusId(int n) { mnDbmsFkDpsStatusId = n; }
    public void setDbmsFkDpsAdjustmentStatusId(int n) { mnDbmsFkDpsAdjustmentStatusId = n; }
    public void setDbmsIsDpsDeleted(boolean b) { mbDbmsIsDpsDeleted = b; }
    public void setDbmsIsDpsEntryDeleted(boolean b) { mbDbmsIsDpsEntryDeleted = b; }
    public void setDbmsIsDpsAdjustmentDeleted(boolean b) { mbDbmsIsDpsAdjustmentDeleted = b; }
    public void setDbmsIsDpsAdjustmentEntryDeleted(boolean b) { mbDbmsIsDpsAdjustmentEntryDeleted = b; }

    public java.util.Date getAuxDpsTimestamp() { return mtAuxDpsTimestamp; }
    public java.util.Date getAuxDpsAdjustmentTimestamp() { return mtAuxDpsAdjustmentTimestamp; }
    public erp.mtrn.data.SDataDpsEntry getAuxDpsEntryComplementary() { return moAuxDpsEntryComplementary; }

    public int getDbmsFkDpsStatusId() { return mnDbmsFkDpsStatusId; }
    public int getDbmsFkDpsAdjustmentStatusId() { return mnDbmsFkDpsAdjustmentStatusId; }
    public boolean getDbmsIsDpsDeleted() { return mbDbmsIsDpsDeleted; }
    public boolean getDbmsIsDpsEntryDeleted() { return mbDbmsIsDpsEntryDeleted; }
    public boolean getDbmsIsDpsAdjustmentDeleted() { return mbDbmsIsDpsAdjustmentDeleted; }
    public boolean getDbmsIsDpsAdjustmentEntryDeleted() { return mbDbmsIsDpsAdjustmentEntryDeleted; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkDpsYearId = ((int[]) pk)[0];
        mnPkDpsDocId = ((int[]) pk)[1];
        mnPkDpsEntryId = ((int[]) pk)[2];
        mnPkDpsAdjustmentYearId = ((int[]) pk)[3];
        mnPkDpsAdjustmentDocId = ((int[]) pk)[4];
        mnPkDpsAdjustmentEntryId = ((int[]) pk)[5];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkDpsYearId, mnPkDpsDocId, mnPkDpsEntryId, mnPkDpsAdjustmentYearId, mnPkDpsAdjustmentDocId, mnPkDpsAdjustmentEntryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkDpsYearId = 0;
        mnPkDpsDocId = 0;
        mnPkDpsEntryId = 0;
        mnPkDpsAdjustmentYearId = 0;
        mnPkDpsAdjustmentDocId = 0;
        mnPkDpsAdjustmentEntryId = 0;
        mdQuantity = 0;
        mdOriginalQuantity = 0;
        mdValue = 0;
        mdValueCy = 0;

        mtAuxDpsTimestamp = null;
        mtAuxDpsAdjustmentTimestamp = null;
        moAuxDpsEntryComplementary = null;

        mnDbmsFkDpsStatusId = 0;
        mnDbmsFkDpsAdjustmentStatusId = 0;
        mbDbmsIsDpsDeleted = false;
        mbDbmsIsDpsEntryDeleted = false;
        mbDbmsIsDpsAdjustmentDeleted = false;
        mbDbmsIsDpsAdjustmentEntryDeleted = false;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_dps_dps_adj " +
                    "WHERE id_dps_year = " + key[0] + " AND id_dps_doc = " + key[1] + " AND id_dps_ety = " + key[2] +  " AND " +
                    "id_adj_year = " + key[3] + " AND id_adj_doc = " + key[4] + " AND id_adj_ety = " + key[5] +  " ";

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkDpsYearId = resultSet.getInt("id_dps_year");
                mnPkDpsDocId = resultSet.getInt("id_dps_doc");
                mnPkDpsEntryId = resultSet.getInt("id_dps_ety");
                mnPkDpsAdjustmentYearId = resultSet.getInt("id_adj_year");
                mnPkDpsAdjustmentDocId = resultSet.getInt("id_adj_doc");
                mnPkDpsAdjustmentEntryId = resultSet.getInt("id_adj_ety");
                mdQuantity = resultSet.getDouble("qty");
                mdOriginalQuantity = resultSet.getDouble("orig_qty");
                mdValue = resultSet.getDouble("val");
                mdValueCy = resultSet.getDouble("val_cur");

                // Read aswell related documents information:

                sql = "SELECT b_del, fid_st_dps FROM trn_dps " +
                        "WHERE id_year = " + mnPkDpsYearId + " AND id_doc = " + mnPkDpsDocId + " ";

                resultSet = statement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    mnDbmsFkDpsStatusId = resultSet.getInt("fid_st_dps");
                    mbDbmsIsDpsDeleted = resultSet.getBoolean("b_del");
                }

                sql = "SELECT b_del, fid_st_dps FROM trn_dps " +
                        "WHERE id_year = " + mnPkDpsAdjustmentYearId + " AND id_doc = " + mnPkDpsAdjustmentDocId + " ";

                resultSet = statement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    mnDbmsFkDpsAdjustmentStatusId = resultSet.getInt("fid_st_dps");
                    mbDbmsIsDpsAdjustmentDeleted = resultSet.getBoolean("b_del");
                }

                // Read aswell related document entries information:

                sql = "SELECT b_del FROM trn_dps_ety " +
                        "WHERE id_year = " + mnPkDpsYearId + " AND id_doc = " + mnPkDpsDocId + " AND id_ety = " + mnPkDpsEntryId +  " ";

                resultSet = statement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    mbDbmsIsDpsEntryDeleted = resultSet.getBoolean("b_del");
                }

                sql = "SELECT b_del, b_disc_retail_chain FROM trn_dps_ety " +
                        "WHERE id_year = " + mnPkDpsAdjustmentYearId + " AND id_doc = " + mnPkDpsAdjustmentDocId + " AND id_ety = " + mnPkDpsAdjustmentEntryId +  " ";

                resultSet = statement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    mbDbmsIsDpsAdjustmentEntryDeleted = resultSet.getBoolean("b_del");

                    if (resultSet.getBoolean("b_disc_retail_chain")) {
                        moAuxDpsEntryComplementary = new SDataDpsEntry();
                        moAuxDpsEntryComplementary.setFlagReadLinksAswell(false);

                        if (moAuxDpsEntryComplementary.read(new int[] { mnPkDpsYearId, mnPkDpsDocId, mnPkDpsEntryId }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
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
        int param = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (moAuxDpsEntryComplementary != null) {
                if (moAuxDpsEntryComplementary.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
                else {
                    mnPkDpsYearId = moAuxDpsEntryComplementary.getPkYearId();
                    mnPkDpsDocId = moAuxDpsEntryComplementary.getPkDocId();
                    mnPkDpsEntryId = moAuxDpsEntryComplementary.getPkEntryId();
                }
            }

            callableStatement = connection.prepareCall(
                    "{ CALL trn_dps_dps_adj_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?) }");
            callableStatement.setInt(param++, mnPkDpsYearId);
            callableStatement.setInt(param++, mnPkDpsDocId);
            callableStatement.setInt(param++, mnPkDpsEntryId);
            callableStatement.setInt(param++, mnPkDpsAdjustmentYearId);
            callableStatement.setInt(param++, mnPkDpsAdjustmentDocId);
            callableStatement.setInt(param++, mnPkDpsAdjustmentEntryId);
            callableStatement.setDouble(param++, mdQuantity);
            callableStatement.setDouble(param++, mdOriginalQuantity);
            callableStatement.setDouble(param++, mdValue);
            callableStatement.setDouble(param++, mdValueCy);
            callableStatement.registerOutParameter(param++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(param++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnDbmsErrorId = callableStatement.getInt(param - 2);
            msDbmsError = callableStatement.getString(param - 1);

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
}

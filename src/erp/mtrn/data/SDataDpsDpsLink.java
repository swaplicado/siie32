/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 *
 * @author Sergio Flores, Alfonso Flores
 */
public class SDataDpsDpsLink extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkSourceYearId;
    protected int mnPkSourceDocId;
    protected int mnPkSourceEntryId;
    protected int mnPkDestinyYearId;
    protected int mnPkDestinyDocId;
    protected int mnPkDestinyEntryId;
    protected double mdQuantity;
    protected double mdOriginalQuantity;

    protected java.util.Date mtAuxSourceTimestamp;
    protected java.util.Date mtAuxDestinyTimestamp;

    protected int mnDbmsFkSourceStatusId;
    protected int mnDbmsFkDestinyStatusId;
    protected boolean mbDbmsIsSourceDeleted;
    protected boolean mbDbmsIsSourceEntryDeleted;
    protected boolean mbDbmsIsDestinyDeleted;
    protected boolean mbDbmsIsDestinyEntryDeleted;

    /**
     * Overrides java.lang.Object.clone() function.
     */
    public SDataDpsDpsLink() {
        super(SDataConstants.TRN_DPS_DPS_SUPPLY);
        reset();
    }

    public void setPkSourceYearId(int n) { mnPkSourceYearId = n; }
    public void setPkSourceDocId(int n) { mnPkSourceDocId = n; }
    public void setPkSourceEntryId(int n) { mnPkSourceEntryId = n; }
    public void setPkDestinyYearId(int n) { mnPkDestinyYearId = n; }
    public void setPkDestinyDocId(int n) { mnPkDestinyDocId = n; }
    public void setPkDestinyEntryId(int n) { mnPkDestinyEntryId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setOriginalQuantity(double d) { mdOriginalQuantity = d; }

    public int getPkSourceYearId() { return mnPkSourceYearId; }
    public int getPkSourceDocId() { return mnPkSourceDocId; }
    public int getPkSourceEntryId() { return mnPkSourceEntryId; }
    public int getPkDestinyYearId() { return mnPkDestinyYearId; }
    public int getPkDestinyDocId() { return mnPkDestinyDocId; }
    public int getPkDestinyEntryId() { return mnPkDestinyEntryId; }
    public double getQuantity() { return mdQuantity; }
    public double getOriginalQuantity() { return mdOriginalQuantity; }
    
    public int[] getDbmsSourceDpsKey() { return new int[] { mnPkSourceYearId, mnPkSourceDocId }; }
    public int[] getDbmsSourceDpsEntryKey() { return new int[] { mnPkSourceYearId, mnPkSourceDocId, mnPkSourceEntryId }; }
    public int[] getDbmsDestinyDpsKey() { return new int[] { mnPkDestinyYearId, mnPkDestinyDocId }; }
    public int[] getDbmsDestinyDpsEntryKey() { return new int[] { mnPkDestinyYearId, mnPkDestinyDocId, mnPkDestinyEntryId }; }

    public void setAuxSourceTimestamp(java.util.Date t) { mtAuxSourceTimestamp = t; }
    public void setAuxDestinyTimestamp(java.util.Date t) { mtAuxDestinyTimestamp = t; }

    public void setDbmsFkSourceStatusId(int n) { mnDbmsFkSourceStatusId = n; }
    public void setDbmsFkDestinyStatusId(int n) { mnDbmsFkDestinyStatusId = n; }
    public void setDbmsIsSourceDeleted(boolean b) { mbDbmsIsSourceDeleted = b; }
    public void setDbmsIsSourceEntryDeleted(boolean b) { mbDbmsIsSourceEntryDeleted = b; }
    public void setDbmsIsDestinyDeleted(boolean b) { mbDbmsIsDestinyDeleted = b; }
    public void setDbmsIsDestinyEntryDeleted(boolean b) { mbDbmsIsDestinyEntryDeleted = b; }

    public java.util.Date getAuxSourceTimestamp() { return mtAuxSourceTimestamp; }
    public java.util.Date getAuxDestinyTimestamp() { return mtAuxDestinyTimestamp; }

    public int getDbmsFkSourceStatusId() { return mnDbmsFkSourceStatusId; }
    public int getDbmsFkDestinyStatusId() { return mnDbmsFkDestinyStatusId; }
    public boolean getDbmsIsSourceDeleted() { return mbDbmsIsSourceDeleted; }
    public boolean getDbmsIsSourceEntryDeleted() { return mbDbmsIsSourceEntryDeleted; }
    public boolean getDbmsIsDestinyDeleted() { return mbDbmsIsDestinyDeleted; }
    public boolean getDbmsIsDestinyEntryDeleted() { return mbDbmsIsDestinyEntryDeleted; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkSourceYearId = ((int[]) pk)[0];
        mnPkSourceDocId = ((int[]) pk)[1];
        mnPkSourceEntryId = ((int[]) pk)[2];
        mnPkDestinyYearId = ((int[]) pk)[3];
        mnPkDestinyDocId = ((int[]) pk)[4];
        mnPkDestinyEntryId = ((int[]) pk)[5];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkSourceYearId, mnPkSourceDocId, mnPkSourceEntryId, mnPkDestinyYearId, mnPkDestinyDocId, mnPkDestinyEntryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkSourceYearId = 0;
        mnPkSourceDocId = 0;
        mnPkSourceEntryId = 0;
        mnPkDestinyYearId = 0;
        mnPkDestinyDocId = 0;
        mnPkDestinyEntryId = 0;
        mdQuantity = 0;
        mdOriginalQuantity = 0;

        mtAuxSourceTimestamp = null;
        mtAuxDestinyTimestamp = null;

        mnDbmsFkSourceStatusId = 0;
        mnDbmsFkDestinyStatusId = 0;
        mbDbmsIsSourceDeleted = false;
        mbDbmsIsSourceEntryDeleted = false;
        mbDbmsIsDestinyDeleted = false;
        mbDbmsIsDestinyEntryDeleted = false;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_dps_dps_supply " +
                    "WHERE id_src_year = " + key[0] + " AND id_src_doc = " + key[1] + " AND id_src_ety = " + key[2] +  " AND " +
                    "id_des_year = " + key[3] + " AND id_des_doc = " + key[4] + " AND id_des_ety = " + key[5] +  " ";

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkSourceYearId = resultSet.getInt("id_src_year");
                mnPkSourceDocId = resultSet.getInt("id_src_doc");
                mnPkSourceEntryId = resultSet.getInt("id_src_ety");
                mnPkDestinyYearId = resultSet.getInt("id_des_year");
                mnPkDestinyDocId = resultSet.getInt("id_des_doc");
                mnPkDestinyEntryId = resultSet.getInt("id_des_ety");
                mdQuantity = resultSet.getDouble("qty");
                mdOriginalQuantity = resultSet.getDouble("orig_qty");

                // Read aswell related documents information:

                sql = "SELECT b_del, fid_st_dps FROM trn_dps " +
                        "WHERE id_year = " + mnPkSourceYearId + " AND id_doc = " + mnPkSourceDocId + " ";

                resultSet = statement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    mnDbmsFkSourceStatusId = resultSet.getInt("fid_st_dps");
                    mbDbmsIsSourceDeleted = resultSet.getBoolean("b_del");
                }

                sql = "SELECT b_del, fid_st_dps FROM trn_dps " +
                        "WHERE id_year = " + mnPkDestinyYearId + " AND id_doc = " + mnPkDestinyDocId + " ";

                resultSet = statement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    mnDbmsFkDestinyStatusId = resultSet.getInt("fid_st_dps");
                    mbDbmsIsDestinyDeleted = resultSet.getBoolean("b_del");
                }

                sql = "SELECT b_del FROM trn_dps_ety " +
                        "WHERE id_year = " + mnPkSourceYearId + " AND id_doc = " + mnPkSourceDocId + " AND id_ety = " + mnPkSourceEntryId +  " ";

                resultSet = statement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    mbDbmsIsSourceEntryDeleted = resultSet.getBoolean("b_del");
                }

                sql = "SELECT b_del FROM trn_dps_ety " +
                        "WHERE id_year = " + mnPkDestinyYearId + " AND id_doc = " + mnPkDestinyDocId + " AND id_ety = " + mnPkDestinyEntryId +  " ";

                resultSet = statement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    mbDbmsIsDestinyEntryDeleted = resultSet.getBoolean("b_del");
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
            callableStatement = connection.prepareCall(
                    "{ CALL trn_dps_dps_supply_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(param++, mnPkSourceYearId);
            callableStatement.setInt(param++, mnPkSourceDocId);
            callableStatement.setInt(param++, mnPkSourceEntryId);
            callableStatement.setInt(param++, mnPkDestinyYearId);
            callableStatement.setInt(param++, mnPkDestinyDocId);
            callableStatement.setInt(param++, mnPkDestinyEntryId);
            callableStatement.setDouble(param++, mdQuantity);
            callableStatement.setDouble(param++, mdOriginalQuantity);
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Edwin Carmona
 */
public class SDataDpsDpsMerge extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkDpsDpsMergeId;
    protected int mnFkDpsOriginalYearId;
    protected int mnFkDpsOriginalDocId;
    protected int mnFkDpsOriginalEntryId;
    protected int mnPkDpsDestinyYearId;
    protected int mnPkDpsDestinyDocId;
    protected int mnPkDpsDestinyEntryId;
    protected int mnVersion;
    protected boolean mbIsDeleted;
    protected double mdOriginalQuantity;
    protected double mdQuantity;
    protected double mdUnitaryPrice;
    protected double mdUnitaryPriceCurrency;

    protected java.util.Date mtAuxSourceTimestamp;
    protected java.util.Date mtAuxDestinyTimestamp;

    protected int mnDbmsFkSourceStatusId;
    protected int mnDbmsFkDestinyStatusId;
    protected boolean mbDbmsIsSourceDeleted;
    protected boolean mbDbmsIsSourceEntryDeleted;
    protected boolean mbDbmsIsSourceOrderSupplied;
    protected boolean mbDbmsIsDestinyDeleted;
    protected boolean mbDbmsIsDestinyEntryDeleted;
    protected boolean mbDbmsIsDestinyOrderSupplied;

    /**
     * Overrides java.lang.Object.clone() function.
     */
    public SDataDpsDpsMerge() {
        super(SDataConstants.TRN_DPS_DPS_MERGE);
        reset();
    }

    public void setPkDpsDpsMergeId(int n) { mnPkDpsDpsMergeId = n; }
    public void setFkDpsOriginalYearId(int n) { mnFkDpsOriginalYearId = n; }
    public void setFkDpsOriginalDocId(int n) { mnFkDpsOriginalDocId = n; }
    public void setFkDpsOriginalEntryId(int n) { mnFkDpsOriginalEntryId = n; }
    public void setFkDpsDestinyYearId(int n) { mnPkDpsDestinyYearId = n; }
    public void setFkDpsDestinyDocId(int n) { mnPkDpsDestinyDocId = n; }
    public void setFkDpsDestinyEntryId(int n) { mnPkDpsDestinyEntryId = n; }
    public void setVersion(int n) { mnVersion = n; }
//    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setOriginalQuantity(double d) { mdOriginalQuantity = d; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setUnitaryPrice(double d) { mdUnitaryPrice = d; }
    public void setUnitaryPriceCy(double d) { mdUnitaryPriceCurrency = d; }

    public int getPkDpsDpsMergeId() { return mnPkDpsDpsMergeId; }
    public int getFkDpsOriginalYearId() { return mnFkDpsOriginalYearId; }
    public int getFkDpsOriginalDocId() { return mnFkDpsOriginalDocId; }
    public int getFkDpsOriginalEntryId() { return mnFkDpsOriginalEntryId; }
    public int getFkDpsDestinyYearId() { return mnPkDpsDestinyYearId; }
    public int getFkDpsDestinyDocId() { return mnPkDpsDestinyDocId; }
    public int getFkDpsDestinyEntryId() { return mnPkDpsDestinyEntryId; }
    public int getVersion() { return mnVersion; }
//    public boolean getIsDeleted() { return mbIsDeleted; }
    public double getOriginalQuantity() { return mdOriginalQuantity; }
    public double getQuantity() { return mdQuantity; }
    public double getUnitaryPrice() { return mdUnitaryPrice; }
    public double getUnitaryPriceCurrency() { return mdUnitaryPriceCurrency; }
    
    public int[] getDbmsDpsKey() { return new int[] { mnPkDpsDpsMergeId, mnFkDpsOriginalYearId }; }
    public int[] getDbmsSourceDpsEntryKey() { return new int[] { mnFkDpsOriginalYearId, mnFkDpsOriginalDocId, mnFkDpsOriginalEntryId }; }
    public int[] getDbmsDestinyDpsKey() { return new int[] { mnPkDpsDestinyYearId, mnPkDpsDestinyDocId }; }
    public int[] getDbmsDestinyDpsEntryKey() { return new int[] { mnPkDpsDestinyYearId, mnPkDpsDestinyDocId, mnPkDpsDestinyEntryId }; }

    public void setAuxSourceTimestamp(java.util.Date t) { mtAuxSourceTimestamp = t; }
    public void setAuxDestinyTimestamp(java.util.Date t) { mtAuxDestinyTimestamp = t; }

    public void setDbmsFkSourceStatusId(int n) { mnDbmsFkSourceStatusId = n; }
    public void setDbmsFkDestinyStatusId(int n) { mnDbmsFkDestinyStatusId = n; }
    public void setDbmsIsSourceDeleted(boolean b) { mbDbmsIsSourceDeleted = b; }
    public void setDbmsIsSourceEntryDeleted(boolean b) { mbDbmsIsSourceEntryDeleted = b; }
    public void setDbmsIsSouceOrderSupplied(boolean b) { mbDbmsIsSourceOrderSupplied = b; }
    public void setDbmsIsDestinyDeleted(boolean b) { mbDbmsIsDestinyDeleted = b; }
    public void setDbmsIsDestinyEntryDeleted(boolean b) { mbDbmsIsDestinyEntryDeleted = b; }
    public void setDbmsIsDestinyOrderSupplied(boolean b) { mbDbmsIsDestinyOrderSupplied = b; }
    
    public java.util.Date getAuxSourceTimestamp() { return mtAuxSourceTimestamp; }
    public java.util.Date getAuxDestinyTimestamp() { return mtAuxDestinyTimestamp; }

    public int getDbmsFkSourceStatusId() { return mnDbmsFkSourceStatusId; }
    public int getDbmsFkDestinyStatusId() { return mnDbmsFkDestinyStatusId; }
    public boolean getDbmsIsSourceDeleted() { return mbDbmsIsSourceDeleted; }
    public boolean getDbmsIsSourceEntryDeleted() { return mbDbmsIsSourceEntryDeleted; }
    public boolean getDbmsIsSourceOrderSupplied() { return mbDbmsIsSourceOrderSupplied; }
    public boolean getDbmsIsDestinyDeleted() { return mbDbmsIsDestinyDeleted; }
    public boolean getDbmsIsDestinyEntryDeleted() { return mbDbmsIsDestinyEntryDeleted; }
    public boolean getDbmsIsDestinyOrderSupplied() { return mbDbmsIsDestinyOrderSupplied; }
    
    private int computePrimaryKey(java.sql.Statement statement) throws SQLException {
        String sql = "SELECT COALESCE(MAX(id_dps_dps_merge), 0) + 1 AS f_id FROM trn_dps_dps_merge";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            return resultSet.getInt("f_id");
        }
        
        return 0;
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkDpsDpsMergeId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkDpsDpsMergeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkDpsDpsMergeId = 0;
        mnFkDpsOriginalYearId = 0;
        mnFkDpsOriginalDocId = 0;
        mnFkDpsOriginalEntryId = 0;
        mnPkDpsDestinyYearId = 0;
        mnPkDpsDestinyDocId = 0;
        mnPkDpsDestinyEntryId = 0;
        mnVersion = 0;
        mbIsDeleted = false;
        mdOriginalQuantity = 0; 
        mdQuantity = 0;
        mdUnitaryPrice = 0;
        mdUnitaryPriceCurrency = 0;

        mtAuxSourceTimestamp = null;
        mtAuxDestinyTimestamp = null;

        mnDbmsFkSourceStatusId = 0;
        mnDbmsFkDestinyStatusId = 0;
        mbDbmsIsSourceDeleted = false;
        mbDbmsIsSourceEntryDeleted = false;
        mbDbmsIsDestinyDeleted = false;
        mbDbmsIsDestinyEntryDeleted = false;
        mbDbmsIsSourceOrderSupplied = false;
    }

    public static boolean hasDpsMerged(java.sql.Statement statement, final int idYear, final int idDoc) {
        boolean hasDpsMerged = false;
        String sql = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT COUNT(*) AS f_count "
                    + "FROM trn_dps_dps_merge " +
                    "WHERE fk_dps_year_des = " + idYear + " "
                    + "AND fk_dps_doc_des = " + idDoc + " "
                    + "AND NOT b_del "
                    + "AND ver <> 0 ";

            resultSet = statement.getConnection().createStatement().executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
            }
            else {
                hasDpsMerged = resultSet.getInt("f_count") > 0;
            }
        }
        catch (java.sql.SQLException e) {
            Logger.getLogger(SDataDpsDpsMerge.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (java.lang.Exception e) {
            Logger.getLogger(SDataDpsDpsMerge.class.getName()).log(Level.SEVERE, null, e);
        }

        return hasDpsMerged;
    }

    public static List<SDataDpsDpsMerge> getMergedEntries(java.sql.Statement statement, final int idYear, final int idDoc) {
        List<SDataDpsDpsMerge> mergedEntries = new ArrayList<>();
        String sql = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT id_dps_dps_merge FROM trn_dps_dps_merge " +
                    "WHERE fk_dps_year_des = " + idYear + " "
                    + "AND fk_dps_doc_des = " + idDoc + " "
                    + "AND NOT b_del "
                    + "AND ver <> 0 "
                    + "ORDER BY ver ";

            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                SDataDpsDpsMerge merge = new SDataDpsDpsMerge();
                merge.read(new int[] { resultSet.getInt("id_dps_dps_merge") }, statement);

                mergedEntries.add(merge);
            }
        }
        catch (java.sql.SQLException e) {
            Logger.getLogger(SDataDpsDpsMerge.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (java.lang.Exception e) {
            Logger.getLogger(SDataDpsDpsMerge.class.getName()).log(Level.SEVERE, null, e);
        }

        return mergedEntries;
    }

    public static List<SDataDpsDpsMerge> getMergedVersionZero(java.sql.Statement statement, final int idYear, final int idDoc) {
        List<SDataDpsDpsMerge> mergedEntries = new ArrayList<>();
        String sql = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT id_dps_dps_merge FROM trn_dps_dps_merge " +
                    "WHERE fk_dps_year_des = " + idYear + " "
                    + "AND fk_dps_doc_des = " + idDoc + " "
                    + "AND NOT b_del "
                    + "AND ver = 0 ";

            resultSet = statement.getConnection().createStatement().executeQuery(sql);
            while (resultSet.next()) {
                SDataDpsDpsMerge merge = new SDataDpsDpsMerge();
                merge.read(new int[] { resultSet.getInt("id_dps_dps_merge") }, statement);

                mergedEntries.add(merge);
            }
        }
        catch (java.sql.SQLException e) {
            Logger.getLogger(SDataDpsDpsMerge.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (java.lang.Exception e) {
            Logger.getLogger(SDataDpsDpsMerge.class.getName()).log(Level.SEVERE, null, e);
        }

        return mergedEntries;
    }
    
    public static int getNewVersion(java.sql.Statement statement, final int idYear, final int idDoc) {
        int version = 0;
        String sql = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT COALESCE(MAX(ver), -1) + 1 AS f_ver "
                    + "FROM trn_dps_dps_merge " +
                    "WHERE fk_dps_year_des = " + idYear + " "
                    + "AND fk_dps_doc_des = " + idDoc + ";";

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
            }
            else {
                version = resultSet.getInt("f_ver");
            }
        }
        catch (java.sql.SQLException e) {
            Logger.getLogger(SDataDpsDpsMerge.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (java.lang.Exception e) {
            Logger.getLogger(SDataDpsDpsMerge.class.getName()).log(Level.SEVERE, null, e);
        }

        return version;
    }

    public static int deletePastVersions(java.sql.Statement statement, final int idYear, final int idDoc, final int version) {
        int rowsAffected = 0;
        String sql = "";

        try {
            sql = "UPDATE trn_dps_dps_merge " +
                    "SET b_del = 1 " +
                    "WHERE fk_dps_year_des = " + idYear + " "
                    + "AND fk_dps_doc_des = " + idDoc + " "
                    + "AND ver <> 0 "
                    + "AND ver < " + version + " AND NOT b_del";

            rowsAffected = statement.executeUpdate(sql);
        }
        catch (java.sql.SQLException e) {
            Logger.getLogger(SDataDpsDpsMerge.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (java.lang.Exception e) {
            Logger.getLogger(SDataDpsDpsMerge.class.getName()).log(Level.SEVERE, null, e);
        }

        return rowsAffected;
    }

    public static int deleteAllVersions(java.sql.Statement statement, final int idYear, final int idDoc) {
        int rowsAffected = 0;
        String sql = "";

        try {
            sql = "UPDATE trn_dps_dps_merge " +
                    "SET b_del = 1 " +
                    "WHERE fk_dps_year_des = " + idYear + " "
                    + "AND fk_dps_doc_des = " + idDoc + " "
                    + "AND ver <> 0 "
                    + "AND NOT b_del";

            rowsAffected = statement.executeUpdate(sql);
        }
        catch (java.sql.SQLException e) {
            Logger.getLogger(SDataDpsDpsMerge.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (java.lang.Exception e) {
            Logger.getLogger(SDataDpsDpsMerge.class.getName()).log(Level.SEVERE, null, e);
        }

        return rowsAffected;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int pkDpsDpsMergeId = ((int[]) pk)[0];
        
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_dps_dps_merge " +
                    "WHERE id_dps_dps_merge = " + pkDpsDpsMergeId;

            resultSet = statement.getConnection().createStatement().executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkDpsDpsMergeId = pkDpsDpsMergeId;
                mnFkDpsOriginalYearId = resultSet.getInt("fk_dps_year_orig");
                mnFkDpsOriginalDocId = resultSet.getInt("fk_dps_doc_orig");
                mnFkDpsOriginalEntryId = resultSet.getInt("fk_dps_ety_orig");
                mnPkDpsDestinyYearId = resultSet.getInt("fk_dps_year_des");
                mnPkDpsDestinyDocId = resultSet.getInt("fk_dps_doc_des");
                mnPkDpsDestinyEntryId = resultSet.getInt("fk_dps_ety_des");
                mnVersion = resultSet.getInt("ver");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mdOriginalQuantity = resultSet.getDouble("orig_qty");
                mdQuantity = resultSet.getDouble("qty");
                mdUnitaryPrice = resultSet.getDouble("price_u");
                mdUnitaryPriceCurrency = resultSet.getDouble("price_u_cur");

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
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        String sql = "";
        try {
            if (mbIsRegistryNew) {
                mnPkDpsDpsMergeId = computePrimaryKey(connection.createStatement());
                sql = "INSERT INTO trn_dps_dps_merge VALUES (" +
                        mnPkDpsDpsMergeId + ", " +
                        mnFkDpsOriginalYearId + ", " +
                        mnFkDpsOriginalDocId + ", " +
                        mnFkDpsOriginalEntryId + ", " +
                        mnPkDpsDestinyYearId + ", " +
                        mnPkDpsDestinyDocId + ", " +
                        mnPkDpsDestinyEntryId + ", " +
                        mnVersion + ", " +
                        (mbIsDeleted ? 1 : 0) + ", " +
                        mdOriginalQuantity + ", " +
                        mdQuantity + ", " +
                        mdUnitaryPrice + ", " +
                        mdUnitaryPriceCurrency + ")";
            }
            else {
                sql = "UPDATE trn_dps_dps_merge SET " +
                        //"id_dps_dps_merge = " + mnPkDpsDpsMergeId + ", " +
                        "fk_dps_year_orig = " + mnFkDpsOriginalYearId + ", " +
                        "fk_dps_doc_orig = " + mnFkDpsOriginalDocId + ", " +
                        "fk_dps_ety_orig = " + mnFkDpsOriginalEntryId + ", " +
                        "fk_dps_year_des = " + mnPkDpsDestinyYearId + ", " +
                        "fk_dps_doc_des = " + mnPkDpsDestinyDocId + ", " +
                        "fk_dps_ety_des = " + mnPkDpsDestinyEntryId + ", " +
                        "ver = " + mnVersion + ", " +
                        "b_del = " + (mbIsDeleted ? 1 : 0) + ", " +
                        "orig_qty = " + mdOriginalQuantity + ", " +
                        "qty = " + mdQuantity + ", " +
                        "price_u = " + mdUnitaryPrice + ", " +
                        "price_u_cur = " + mdUnitaryPriceCurrency + 
                        " WHERE id_dps_dps_merge = " + mnPkDpsDpsMergeId;
            }

            connection.createStatement().execute(sql);
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            Logger.getLogger(SDataDpsDpsMerge.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            Logger.getLogger(SDataDpsDpsMerge.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return null;
    }
}

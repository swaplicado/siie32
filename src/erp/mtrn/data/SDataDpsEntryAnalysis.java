/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import erp.data.SDataConstants;
import java.sql.ResultSet;
import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Edwin Carmona
 */
public class SDataDpsEntryAnalysis extends erp.lib.data.SDataRegistry implements java.io.Serializable, SGridRow {

    protected int mnPkEntryAnalysisId;
    protected int mnSortPosition;
    protected String msMinValue;
    protected String msMaxValue;
    protected boolean mbIsMin;
    protected boolean mbIsMax;
    protected boolean mbIsRequired;
    protected boolean mbIsLimitModified;
    protected boolean mbIsRequiredModified;
    protected boolean mbIsDeleted;
    protected int mnFkDpsYearId_n;
    protected int mnFkDpsDocId_n;
    protected int mnFkDpsEtyId_n;
    protected int mnFkAnalysisId;
    protected int mnFkItemId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected boolean mbRowEdited;
    protected boolean mbAuxNewVersion;
    protected String msAnalysisName;
    protected String msAnalysisUnit;
    protected String msAnalysisType;

    public SDataDpsEntryAnalysis() {
        super(SDataConstants.TRN_DPS_ETY_ANALYSIS);
        reset();
    }

    public void setPkEntryAnalysisId(int n) { mnPkEntryAnalysisId = n; }
    public void setSortPosition(int n) { mnSortPosition = n; }
    public void setMinValue(String s) { msMinValue = s; }
    public void setMaxValue(String s) { msMaxValue = s; }
    public void setIsMin(boolean b) { mbIsMin = b; }
    public void setIsMax(boolean b) { mbIsMax = b; }
    public void setIsRequired(boolean b) { mbIsRequired = b; }
    public void setIsLimitModified(boolean b) { mbIsLimitModified = b; }
    public void setIsRequiredModified(boolean b) { mbIsRequiredModified = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkDpsYearId_n(int n) { mnFkDpsYearId_n = n; }
    public void setFkDpsDocId_n(int n) { mnFkDpsDocId_n = n; }
    public void setFkDpsEtyId_n(int n) { mnFkDpsEtyId_n = n; }
    public void setFkAnalysisId(int n) { mnFkAnalysisId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }
    
    public void setAuxNewVersion(boolean b) { mbAuxNewVersion = b; }
    public void setAuxAnalysisName(String s) { msAnalysisName = s; }
    public void setAuxAnalysisUnit(String s) { msAnalysisUnit = s; }
    public void setAuxAnalysisType(String s) { msAnalysisType = s; }

    public int getPkEntryAnalysisId() { return mnPkEntryAnalysisId; }
    public int getSortPosition() { return mnSortPosition; }
    public String getMinValue() { return msMinValue; }
    public String getMaxValue() { return msMaxValue; }
    public boolean isMin() { return mbIsMin; }
    public boolean isMax() { return mbIsMax; }
    public boolean isRequired() { return mbIsRequired; }
    public boolean isLimitModified() { return mbIsLimitModified; }
    public boolean isRequiredModified() { return mbIsRequiredModified; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkDpsYearId_n() { return mnFkDpsYearId_n; }
    public int getFkDpsDocId_n() { return mnFkDpsDocId_n; }
    public int getFkDpsEtyId_n() { return mnFkDpsEtyId_n; }
    public int getFkAnalysisId() { return mnFkAnalysisId; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    public String getAuxAnalysisName() { return msAnalysisName; }
    public String getAuxAnalysisUnit() { return msAnalysisUnit; }
    public String getAuxAnalysisType() { return msAnalysisType; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkEntryAnalysisId = ((int[]) pk)[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEntryAnalysisId };
    }
    
    @Override
    public void reset() {
        super.resetRegistry();

        mnPkEntryAnalysisId = 0;
        mnSortPosition = 0;
        msMinValue = "";
        msMaxValue = "";
        mbIsMin = false;
        mbIsMax = false;
        mbIsRequired = false;
        mbIsLimitModified = false;
        mbIsRequiredModified = false;
        mbIsDeleted = false;
        mnFkDpsYearId_n = 0;
        mnFkDpsDocId_n = 0;
        mnFkDpsEtyId_n = 0;
        mnFkAnalysisId = 0;
        mnFkItemId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mbRowEdited = false;
        mbAuxNewVersion = false;
        msAnalysisName = "";
        msAnalysisUnit = "";
        msAnalysisType = "";
    }
    
    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;
        java.sql.ResultSet resultSetAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = "SELECT * FROM " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_ETY_ANALYSIS) + " " +
                    "WHERE id_ety_analysis = " + key[0];

            resultSet = statement.getConnection().createStatement().executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkEntryAnalysisId = resultSet.getInt("id_ety_analysis");
                mnSortPosition = resultSet.getInt("sort_pos");
                msMinValue = resultSet.getString("min_value");
                msMaxValue = resultSet.getString("max_value");
                mbIsMin = resultSet.getBoolean("b_min");
                mbIsMax = resultSet.getBoolean("b_max");
                mbIsRequired = resultSet.getBoolean("b_required");
                mbIsLimitModified = resultSet.getBoolean("b_lim_mod");
                mbIsRequiredModified = resultSet.getBoolean("b_req_mod");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkDpsYearId_n = resultSet.getInt("fid_dps_year_n");
                mnFkDpsDocId_n = resultSet.getInt("fid_dps_doc_n");
                mnFkDpsEtyId_n = resultSet.getInt("fid_dps_ety_n");
                mnFkAnalysisId = resultSet.getInt("fid_analysis_id");
                mnFkItemId = resultSet.getInt("fid_item_id");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getDate("ts_new");
                mtUserEditTs = resultSet.getDate("ts_edit");
                mtUserDeleteTs = resultSet.getDate("ts_del");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
            
            sql = "SELECT qa.unit_symbol, qa.analysis_name, qtp.name "
                    + "FROM " + SDataConstants.TablesMap.get(SDataConstants.QLT_ANALYSIS) + " AS qa "
                    + "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.QLT_TP_ANALYSIS) + " AS qtp "
                    + "ON qa.fk_tp_analysis_id = qtp.id_tp_analysis "
                    + "WHERE qa.id_analysis = " + mnFkAnalysisId;
            
            resultSetAux = statement.getConnection().createStatement().executeQuery(sql);
            if (resultSetAux.next()) {
                msAnalysisName = resultSetAux.getString("analysis_name");
                msAnalysisUnit = resultSetAux.getString("unit_symbol");
                msAnalysisType = resultSetAux.getString("name");
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
        String sql;        
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (mbIsRegistryNew) {
                
                String sqlPk = "SELECT " +
                                "COALESCE(MAX(id_ety_analysis) + 1, 1) AS new_id " +
                                "FROM " +
                                "" + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_ETY_ANALYSIS) + ";";
                
                ResultSet resPk = connection.createStatement().executeQuery(sqlPk);
                if (resPk.next()) {
                    mnPkEntryAnalysisId = resPk.getInt("new_id");
                }
                
                sql = "INSERT INTO " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_ETY_ANALYSIS) + " VALUES (" +
                        mnPkEntryAnalysisId + ", " + 
                        mnSortPosition + ", " + 
                        "'" + msMinValue + "', " + 
                        "'" + msMaxValue + "', " + 
                        (mbIsMin ? 1 : 0) + ", " + 
                        (mbIsMax ? 1 : 0) + ", " + 
                        (mbIsRequired ? 1 : 0) + ", " + 
                        (mbIsLimitModified ? 1 : 0) + ", " + 
                        (mbIsRequiredModified ? 1 : 0) + ", " + 
                        (mbIsDeleted ? 1 : 0) + ", " + 
                        mnFkDpsYearId_n + ", " + 
                        mnFkDpsDocId_n + ", " + 
                        mnFkDpsEtyId_n + ", " + 
                        mnFkAnalysisId + ", " + 
                        mnFkItemId + ", " + 
                        mnFkUserNewId + ", " + 
                        mnFkUserEditId + ", " + 
                        mnFkUserDeleteId + ", " + 
                        "NOW()" + ", " +
                        "NOW()" + ", " +
                        "NOW()" + " " +
                        ")";
            }
            else {
                sql = "UPDATE " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_ETY_ANALYSIS) + " SET " +
//                        "id_ety_analysis = " + mnPkEntryAnalysisId + ", " +
                        "sort_pos = " + mnSortPosition + ", " +
                        "min_value = '" + msMinValue + "', " +
                        "max_value = '" + msMaxValue + "', " +
                        "b_min = " + (mbIsMin ? 1 : 0) + ", " +
                        "b_max = " + (mbIsMax ? 1 : 0) + ", " +
                        "b_required = " + (mbIsRequired ? 1 : 0) + ", " +
                        "b_lim_mod = " + (mbIsLimitModified ? 1 : 0) + ", " +
                        "b_req_mod = " + (mbIsRequiredModified ? 1 : 0) + ", " +
                        "b_del = " + (mbIsDeleted ? 1 : 0) + ", " +
                        "fid_dps_year_n = " + mnFkDpsYearId_n + ", " +
                        "fid_dps_doc_n = " + mnFkDpsDocId_n + ", " +
                        "fid_dps_ety_n = " + mnFkDpsEtyId_n + ", " +
                        "fid_analysis_id = " + mnFkAnalysisId + ", " +
                        "fid_item_id = " + mnFkItemId + ", " +
//                        "fid_usr_new = " + mnFkUserNewId + ", " +
                        "fid_usr_edit = " + mnFkUserEditId + ", ";

                if (mbIsDeleted) {
                    sql += "fid_usr_del = " + mnFkUserDeleteId + ", ";
                }
//                        "ts_new = " + "NOW()" + ", " +
                sql += "ts_edit = " + "NOW()" + (mbIsDeleted ? ", " : " ");
                
                if (mbIsDeleted) {
                    sql += "ts_del = " + "NOW()" + " ";
                }
                
                sql += "WHERE id_ety_analysis = " + mnPkEntryAnalysisId;
            }
            
            connection.createStatement().execute(sql);
            
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }
    
    @Override
    public Date getLastDbUpdate() {
        return mtUserEditTs;
    }
    
    public SDataRegistry clone() throws CloneNotSupportedException {
        SDataDpsEntryAnalysis registry = new SDataDpsEntryAnalysis();
        
        registry.setPkEntryAnalysisId(this.getPkEntryAnalysisId());
        registry.setSortPosition(this.getSortPosition());
        registry.setMinValue(this.getMinValue());
        registry.setMaxValue(this.getMaxValue());
        registry.setIsMin(this.isMin());
        registry.setIsMax(this.isMax());
        registry.setIsRequired(this.isRequired());
        registry.setIsLimitModified(this.isLimitModified());
        registry.setIsRequiredModified(this.isRequiredModified());
        registry.setIsDeleted(this.getIsDeleted());
        registry.setFkDpsYearId_n(this.getFkDpsYearId_n());
        registry.setFkDpsDocId_n(this.getFkDpsDocId_n());
        registry.setFkDpsEtyId_n(this.getFkDpsEtyId_n());
        registry.setFkAnalysisId(this.getFkAnalysisId());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUserNewId(this.getFkUserNewId());
        registry.setFkUserEditId(this.getFkUserEditId());
        registry.setFkUserDeleteId(this.getFkUserDeleteId());
        registry.setUserNewTs(this.getUserNewTs());
        registry.setUserEditTs(this.getUserEditTs());
        registry.setUserDeleteTs(this.getUserDeleteTs());
        
        registry.setAuxAnalysisName(this.getAuxAnalysisName());
        registry.setAuxAnalysisType(this.getAuxAnalysisType());
        registry.setAuxAnalysisUnit(this.getAuxAnalysisUnit());
        
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return false;
    }

    @Override
    public void setRowEdited(boolean edited) {
        
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = mnSortPosition;
                break;
            case 1:
                value = msAnalysisName;
                break;
            case 2:
                value = msAnalysisUnit;
                break;
            case 3:
                value = msMinValue;
                break;
            case 4:
                value = msMaxValue;
                break;
            case 5:
                value = mbIsRequired;
                break;
                
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 3:
                msMinValue = (String) value;
                mbIsLimitModified = true;
                break;
            case 4:
                msMaxValue = (String) value;
                mbIsLimitModified = true;
                break;
            case 5:
                mbIsRequired = (Boolean) value;
                mbIsRequiredModified = true;
                break;
            
            default:
        }
    }
}

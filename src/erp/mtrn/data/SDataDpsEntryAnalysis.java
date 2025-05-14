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
    protected String msOriginalSpecification;
    protected String msSpecification;
    protected String msOriginalMinValue;
    protected String msOriginalMaxValue;
    protected String msMinValue;
    protected String msMaxValue;
    protected boolean mbIsMin;
    protected boolean mbIsMax;
    protected boolean mbIsRequired;
    protected boolean mbIsForDps;
    protected boolean mbIsForCoA;
    protected boolean mbWasAdded;
    protected boolean mbIsDeleted;
    protected int mnFkDpsYearId;
    protected int mnFkDpsDocId;
    protected int mnFkDpsEtyId;
    protected int mnFkAnalysisId;
    protected int mnFkItemId;
    protected int mnFkDatasheetTemplateId_n;
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
    public void setOriginalSpecification(String s) { msOriginalSpecification = s; }
    public void setSpecification(String s) { msSpecification = s; }
    public void setOriginalMinValue(String s) { msOriginalMinValue = s; }
    public void setOriginalMaxValue(String s) { msOriginalMaxValue = s; }
    public void setMinValue(String s) { msMinValue = s; }
    public void setMaxValue(String s) { msMaxValue = s; }
    public void setIsMin(boolean b) { mbIsMin = b; }
    public void setIsMax(boolean b) { mbIsMax = b; }
    public void setIsRequired(boolean b) { mbIsRequired = b; }
    public void setIsForDps(boolean b) { mbIsForDps = b; }
    public void setIsForCoA(boolean b) { mbIsForCoA = b; }
    public void setWasAdded(boolean b) { mbWasAdded = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkDpsYearId(int n) { mnFkDpsYearId = n; }
    public void setFkDpsDocId(int n) { mnFkDpsDocId = n; }
    public void setFkDpsEtyId(int n) { mnFkDpsEtyId = n; }
    public void setFkAnalysisId(int n) { mnFkAnalysisId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkDatasheetTemplateId_n(int n) { mnFkDatasheetTemplateId_n = n; }
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
    public String getOriginalSpecification() { return msOriginalSpecification; }
    public String getSpecification() { return msSpecification; }
    public String getOriginalMinValue() { return msOriginalMinValue; }
    public String getOriginalMaxValue() { return msOriginalMaxValue; }
    public String getMinValue() { return msMinValue; }
    public String getMaxValue() { return msMaxValue; }
    public boolean isMin() { return mbIsMin; }
    public boolean isMax() { return mbIsMax; }
    public boolean isRequired() { return mbIsRequired; }
    public boolean isForDps() { return mbIsForDps; }
    public boolean isForCoA() { return mbIsForCoA; }
    public boolean wasAdded() { return mbWasAdded; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkDpsYearId() { return mnFkDpsYearId; }
    public int getFkDpsDocId() { return mnFkDpsDocId; }
    public int getFkDpsEtyId() { return mnFkDpsEtyId; }
    public int getFkAnalysisId() { return mnFkAnalysisId; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkDatasheetTemplateId_n() { return mnFkDatasheetTemplateId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    public String getAuxAnalysisName() { return msAnalysisName; }
    public String getAuxAnalysisUnit() { return msAnalysisUnit; }
    public String getAuxAnalysisType() { return msAnalysisType; }

    public void readAnalysisAuxData(java.sql.Statement statement) {
        String sql = "SELECT qa.unit_symbol, qa.analysis_name, qtp.name "
                + "FROM " + SDataConstants.TablesMap.get(SDataConstants.QLT_ANALYSIS) + " AS qa "
                + "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.QLT_TP_ANALYSIS) + " AS qtp "
                + "ON qa.fk_tp_analysis_id = qtp.id_tp_analysis "
                + "WHERE qa.id_analysis = " + mnFkAnalysisId;
        
        try {
            ResultSet resultSet = statement.getConnection().createStatement().executeQuery(sql);
            if (resultSet.next()) {
                msAnalysisName = resultSet.getString("analysis_name");
                msAnalysisUnit = resultSet.getString("unit_symbol");
                msAnalysisType = resultSet.getString("name");
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
        }
    }
    
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
        msOriginalSpecification = "";
        msSpecification = "";
        msOriginalMinValue = "";
        msOriginalMaxValue = "";
        msMinValue = "";
        msMaxValue = "";
        mbIsMin = false;
        mbIsMax = false;
        mbIsRequired = false;
        mbIsForDps = false;
        mbIsForCoA = false;
        mbWasAdded = false;
        mbIsDeleted = false;
        mnFkDpsYearId = 0;
        mnFkDpsDocId = 0;
        mnFkDpsEtyId = 0;
        mnFkAnalysisId = 0;
        mnFkItemId = 0;
        mnFkDatasheetTemplateId_n = 0;
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
                msOriginalSpecification = resultSet.getString("orig_specification");
                msSpecification = resultSet.getString("specification");
                msOriginalMinValue = resultSet.getString("orig_min_value");
                msOriginalMaxValue = resultSet.getString("orig_max_value");
                msMinValue = resultSet.getString("min_value");
                msMaxValue = resultSet.getString("max_value");
                mbIsMin = resultSet.getBoolean("b_min");
                mbIsMax = resultSet.getBoolean("b_max");
                mbIsRequired = resultSet.getBoolean("b_req");
                mbIsForDps = resultSet.getBoolean("b_dps");
                mbIsForCoA = resultSet.getBoolean("b_coa");
                mbWasAdded = resultSet.getBoolean("b_added");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkDpsYearId = resultSet.getInt("fid_dps_year");
                mnFkDpsDocId = resultSet.getInt("fid_dps_doc");
                mnFkDpsEtyId = resultSet.getInt("fid_dps_ety");
                mnFkAnalysisId = resultSet.getInt("fid_analysis");
                mnFkItemId = resultSet.getInt("fid_item");
                mnFkDatasheetTemplateId_n = resultSet.getInt("fid_dtsht_tmplte_n");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getDate("ts_new");
                mtUserEditTs = resultSet.getDate("ts_edit");
                mtUserDeleteTs = resultSet.getDate("ts_del");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
            
           this.readAnalysisAuxData(statement);
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
                        "'" + msOriginalSpecification + "', " +
                        "'" + msSpecification + "', " +
                        "'" + msOriginalMinValue + "', " +
                        "'" + msOriginalMaxValue + "', " +
                        "'" + msMinValue + "', " + 
                        "'" + msMaxValue + "', " + 
                        (mbIsMin ? 1 : 0) + ", " + 
                        (mbIsMax ? 1 : 0) + ", " + 
                        (mbIsRequired ? 1 : 0) + ", " +
                        (mbIsForDps ? 1 : 0) + ", " +
                        (mbIsForCoA ? 1 : 0) + ", " +
                        (mbWasAdded ? 1 : 0) + ", " +
                        (mbIsDeleted ? 1 : 0) + ", " + 
                        mnFkDpsYearId + ", " + 
                        mnFkDpsDocId + ", " + 
                        mnFkDpsEtyId + ", " + 
                        mnFkAnalysisId + ", " + 
                        mnFkItemId + ", " + 
                        (mnFkDatasheetTemplateId_n == 0 ? "NULL" : mnFkDatasheetTemplateId_n) + ", " +
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
                        "orig_specification = '" + msOriginalSpecification + "', " +
                        "specification = '" + msSpecification + "', " +
                        "orig_min_value = '" + msOriginalMinValue + "', " +
                        "orig_max_value = '" + msOriginalMaxValue + "', " +
                        "min_value = '" + msMinValue + "', " +
                        "max_value = '" + msMaxValue + "', " +
                        "b_min = " + (mbIsMin ? 1 : 0) + ", " +
                        "b_max = " + (mbIsMax ? 1 : 0) + ", " +
                        "b_req = " + (mbIsRequired ? 1 : 0) + ", " +
                        "b_dps = " + (mbIsForDps ? 1 : 0) + ", " +
                        "b_coa = " + (mbIsForCoA ? 1 : 0) + ", " +
                        "b_added = " + (mbWasAdded ? 1 : 0) + ", " +
                        "b_del = " + (mbIsDeleted ? 1 : 0) + ", " +
                        "fid_dps_year = " + mnFkDpsYearId + ", " +
                        "fid_dps_doc = " + mnFkDpsDocId + ", " +
                        "fid_dps_ety = " + mnFkDpsEtyId + ", " +
                        "fid_analysis = " + mnFkAnalysisId + ", " +
                        "fid_item = " + mnFkItemId + ", " +
                        "fid_dtsht_tmplte_n = " + (mnFkDatasheetTemplateId_n == 0 ? "NULL" : mnFkDatasheetTemplateId_n) + ", " +
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
        registry.setOriginalSpecification(this.getOriginalSpecification());
        registry.setSpecification(this.getSpecification());
        registry.setOriginalMinValue(this.getOriginalMinValue());
        registry.setOriginalMaxValue(this.getOriginalMaxValue());
        registry.setMinValue(this.getMinValue());
        registry.setMaxValue(this.getMaxValue());
        registry.setIsMin(this.isMin());
        registry.setIsMax(this.isMax());
        registry.setIsRequired(this.isRequired());
        registry.setIsForDps(this.isForDps());
        registry.setIsForCoA(this.isForCoA());
        registry.setWasAdded(this.wasAdded());
        registry.setIsDeleted(this.getIsDeleted());
        registry.setFkDpsYearId(this.getFkDpsYearId());
        registry.setFkDpsDocId(this.getFkDpsDocId());
        registry.setFkDpsEtyId(this.getFkDpsEtyId());
        registry.setFkAnalysisId(this.getFkAnalysisId());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkDatasheetTemplateId_n(this.getFkDatasheetTemplateId_n());
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
        return mbIsRequired;
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
                value = msSpecification;
                break;
            case 3:
                value = msAnalysisUnit;
                break;
            // case 3:
            //     value = msMinValue;
            //     break;
            // case 4:
            //     value = msMaxValue;
            //     break;
            case 4:
                value = mbIsRequired;
                break;
            case 5:
                value = mbIsForDps;
                break;
            case 6:
                value = mbIsForCoA;
                break;
                
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 2:
                msSpecification = (String) value;
                break;
            // case 3:
            //     msMinValue = (String) value;
            //     break;
            // case 4:
            //     msMaxValue = (String) value;
            //     break;
            case 5:
                mbIsForDps = mbIsRequired ? true : (Boolean) value;
                break;
            case 6:
                mbIsForCoA = (Boolean) value;
                break;
            
            default:
        }
    }
}

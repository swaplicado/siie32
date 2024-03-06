/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mqlt.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import erp.data.SDataConstants;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;

/**
 *
 * @author Edwin Carmona
 */
public class SDataAnalysisItem extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkAnalysisId;
    protected int mnPkItemId;
    protected int mnSortPosition;
    protected java.lang.String msMinValue;
    protected java.lang.String msMaxValue;
    protected boolean mbMin;
    protected boolean mbMax;
    protected boolean mbIsRequired;
    protected boolean mbIsDeleted;
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

    public SDataAnalysisItem() {
        super(SDataConstants.QLT_ANALYSIS_ITEM);
        reset();
    }

    public void setPkAnalysisId(int n) { mnPkAnalysisId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setSortPosition(int n) { mnSortPosition = n; }
    public void setMinValue(java.lang.String s) { msMinValue = s; }
    public void setMaxValue(java.lang.String s) { msMaxValue = s; }
    public void setMin(boolean b) { mbMin = b; }
    public void setMax(boolean b) { mbMax = b; }
    public void setIsRequired(boolean b) { mbIsRequired = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
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

    public int getPkAnalysisId() { return mnPkAnalysisId; }
    public int getPkItemId() { return mnPkItemId; }
    public int getSortPosition() { return mnSortPosition; }
    public java.lang.String getMinValue() { return msMinValue; }
    public java.lang.String getMaxValue() { return msMaxValue; }
    public boolean getMin() { return mbMin; }
    public boolean getMax() { return mbMax; }
    public boolean getIsRequired() { return mbIsRequired; }
    public boolean getIsDeleted() { return mbIsDeleted; }
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
        mnPkAnalysisId = ((int[]) pk)[0];
        mnPkItemId = ((int[]) pk)[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAnalysisId, mnPkItemId };
    }
    
    @Override
    public void reset() {
        super.resetRegistry();

        mnPkAnalysisId = 0;
        mnPkItemId = 0;
        mnSortPosition = 0;
        msMinValue = "";
        msMaxValue = "";
        mbMin = false;
        mbMax = false;
        mbIsRequired = false;
        mbIsDeleted = false;
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
            sql = "SELECT * FROM " + SDataConstants.TablesMap.get(SDataConstants.QLT_ANALYSIS_ITEM) + " " +
                    "WHERE id_analysis = " + key[0] + " AND id_item = " + key[1];

            resultSet = statement.getConnection().createStatement().executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkAnalysisId = resultSet.getInt("id_analysis");
                mnPkItemId = resultSet.getInt("id_item");
                mnSortPosition = resultSet.getInt("sort_pos");
                msMinValue = resultSet.getString("min_value");
                msMaxValue = resultSet.getString("max_value");
                mbMin = resultSet.getBoolean("b_min");
                mbMax = resultSet.getBoolean("b_max");
                mbIsRequired = resultSet.getBoolean("b_required");
                mbIsDeleted = resultSet.getBoolean("b_del");
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
                    + "WHERE qa.id_analysis = " + mnPkAnalysisId;
            
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
                
                sql = "INSERT INTO " + SDataConstants.TablesMap.get(SDataConstants.QLT_ANALYSIS_ITEM) + " VALUES (" +
                        mnPkAnalysisId + ", " + 
                        mnPkItemId + ", " + 
                        mnSortPosition + ", " + 
                        "'" + msMinValue + "', " + 
                        "'" + msMaxValue + "', " + 
                        (mbMin ? 1 : 0) + ", " + 
                        (mbMax ? 1 : 0) + ", " + 
                        (mbIsRequired ? 1 : 0) + ", " + 
                        (mbIsDeleted ? 1 : 0) + ", " + 
                        mnFkUserNewId + ", " + 
                        mnFkUserEditId + ", " + 
                        mnFkUserDeleteId + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + " " +
                        ")";
            }
            else {
                sql = "UPDATE " + SDataConstants.TablesMap.get(SDataConstants.QLT_ANALYSIS_ITEM) + " SET " +
//                        "id_analysis = " + mnPkAnalysisId + ", " +
//                        "id_item = " + mnPkItemId + ", " +
                        "sort_pos = " + mnSortPosition + ", " +
                        "min_value = '" + msMinValue + "', " +
                        "max_value = '" + msMaxValue + "', " +
                        "b_min = " + (mbMin ? 1 : 0) + ", " +
                        "b_max = " + (mbMax ? 1 : 0) + ", " +
                        "b_required = " + (mbIsRequired ? 1 : 0) + ", " +
                        "b_del = " + (mbIsDeleted ? 1 : 0) + ", " +
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
                
                sql += "WHERE id_analysis = " + mnPkAnalysisId + " AND id_item = " + mnPkItemId;
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
    public int delete(java.sql.Connection connection) {
        String sMsg = "No se puede eliminar el documento: ";
        
        try {
            if (mbIsRegistryRequestDelete) {
                // Set document as deleted:

                if (mbIsDeleted) {
                    mnDbmsErrorId = 1;
                    msDbmsError = sMsg + "El documento ya está marcado como eliminado.";
                    throw new Exception(msDbmsError);
                }
                else {
                    mbIsDeleted = true;

                    if (save(connection) == SLibConstants.DB_ACTION_SAVE_OK) {
                        mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_OK;
                    }
                    else {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE);
                    }
                }
            }
            else {
                // Revert registry deletion:
                
                if (!mbIsDeleted) {
                    mnDbmsErrorId = 2;
                    msDbmsError = sMsg + "El documento ya está desmarcado como eliminado.";
                    throw new Exception(msDbmsError);
                }
                else {
                    mbIsDeleted = false;

                    if (save(connection) == SLibConstants.DB_ACTION_SAVE_OK) {
                        mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_OK;
                    }
                    else {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE);
                    }
                }
            }
        }
        catch (SQLException exception) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
            SLibUtilities.printOutException(this, exception);
        }
        catch (Exception exception) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
            SLibUtilities.printOutException(this, exception);
        }

        return mnLastDbActionResult;
    }
    
    @Override
    public Date getLastDbUpdate() {
        return mtUserEditTs;
    }
    
    public SDataRegistry clone() throws CloneNotSupportedException {
        SDataAnalysisItem registry = new SDataAnalysisItem();
        
        registry.setPkAnalysisId(this.getPkAnalysisId());
        registry.setPkItemId(this.getPkItemId());
        registry.setSortPosition(this.getSortPosition());
        registry.setMinValue(this.getMinValue());
        registry.setMaxValue(this.getMaxValue());
        registry.setMin(this.getMin());
        registry.setMax(this.getMax());
        registry.setIsRequired(this.getIsRequired());
        registry.setIsDeleted(this.getIsDeleted());
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
}

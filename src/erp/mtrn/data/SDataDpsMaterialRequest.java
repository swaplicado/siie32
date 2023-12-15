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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edwin Carmona
 */
public class SDataDpsMaterialRequest extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkDpsMatReqId;
    protected double mdQuantity;
    protected double mdValue;
    protected double mdValueCy;
    protected int mnFkDpsYearId;
    protected int mnFkDpsDocId;
    protected int mnFkDpsEntryId;
    protected int mnFkMaterialRequestId;
    protected int mnFkMaterialRequestEntryId;

    /**
     * Overrides java.lang.Object.clone() function.
     */
    public SDataDpsMaterialRequest() {
        super(SDataConstants.TRN_DPS_MAT_REQ);
        reset();
    }

    public void setPkDpsMatReqId(int n) { mnPkDpsMatReqId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setValue(double d) { mdValue = d; }
    public void setValueCy(double d) { mdValueCy = d; }
    public void setFkDpsYearId(int n) { mnFkDpsYearId = n; }
    public void setFkDpsDocId(int n) { mnFkDpsDocId = n; }
    public void setFkDpsEntryId(int n) { mnFkDpsEntryId = n; }
    public void setFkMaterialRequestId(int n) { mnFkMaterialRequestId = n; }
    public void setFkMaterialRequestEntryId(int n) { mnFkMaterialRequestEntryId = n; }

    public int getPkDpsMatReqId() { return mnPkDpsMatReqId; }
    public double getQuantity() { return mdQuantity; }
    public double getValue() { return mdValue; }
    public double getValueCy() { return mdValueCy; }
    public int getFkDpsYearId() { return mnFkDpsYearId; }
    public int getFkDpsDocId() { return mnFkDpsDocId; }
    public int getFkDpsEntryId() { return mnFkDpsEntryId; }
    public int getFkMaterialRequestId() { return mnFkMaterialRequestId; }
    public int getFkMaterialRequestEntryId() { return mnFkMaterialRequestEntryId; }
    
    public int[] getDbmsDpsKey() { return new int[] { mnFkDpsYearId, mnFkDpsDocId }; }
    public int[] getDbmsDpsEntryKey() { return new int[] { mnFkDpsYearId, mnFkDpsDocId, mnFkDpsEntryId }; }
    public int[] getDbmsMaterialRequestKey() { return new int[] { mnFkMaterialRequestId }; }
    public int[] getDbmsMaterialRequestEntryKey() { return new int[] { mnFkMaterialRequestId, mnFkMaterialRequestEntryId }; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkDpsMatReqId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkDpsMatReqId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkDpsMatReqId = 0;
        mdQuantity = 0;
        mdValue = 0;
        mdValueCy = 0;
        mnFkDpsYearId = 0;
        mnFkDpsDocId = 0;
        mnFkDpsEntryId = 0;
        mnFkMaterialRequestId = 0;
        mnFkMaterialRequestEntryId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_MAT_REQ) + " " +
                    "WHERE id_dps_mat_req = " + key[0] + " ";

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkDpsMatReqId = resultSet.getInt("id_dps_mat_req");
                mdQuantity = resultSet.getDouble("qty");
                mdValue = resultSet.getDouble("val");
                mdValueCy = resultSet.getDouble("val_cur");
                mnFkDpsYearId = resultSet.getInt("fid_dps_year");
                mnFkDpsDocId = resultSet.getInt("fid_dps_doc");
                mnFkDpsEntryId = resultSet.getInt("fid_dps_ety");
                mnFkMaterialRequestId = resultSet.getInt("fid_mat_req");
                mnFkMaterialRequestEntryId = resultSet.getInt("fid_mat_req_ety");

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
        String sql;        
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (mbIsRegistryNew) {
                sql = "SELECT COALESCE(MAX(id_dps_mat_req) + 1, 1) AS new_id "
                        + "FROM " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_MAT_REQ) + " ";

                ResultSet resultSet = connection.createStatement().executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
                }
                else {
                    mnPkDpsMatReqId = resultSet.getInt("new_id");
                    
                    sql = "INSERT INTO " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_MAT_REQ) + " VALUES (" +
                                mnPkDpsMatReqId + ", " + 
                                mdQuantity + ", " + 
                                mdValue + ", " + 
                                mdValueCy + ", " + 
                                mnFkDpsYearId + ", " + 
                                mnFkDpsDocId + ", " + 
                                mnFkDpsEntryId + ", " + 
                                mnFkMaterialRequestId + ", " + 
                                mnFkMaterialRequestEntryId + " " + 
                            ")";
                }
            }
            else {
                sql = "UPDATE " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_MAT_REQ) + " SET " +
//                    "id_dps_mat_req = " + mnPkDpsDpsMatReqId + ", " +
                    "qty = " + mdQuantity + ", " +
                    "val = " + mdValue + ", " +
                    "val_cur = " + mdValueCy + ", " +
                    "fid_dps_year = " + mnFkDpsYearId + ", " +
                    "fid_dps_doc = " + mnFkDpsDocId + ", " +
                    "fid_dps_ety = " + mnFkDpsEntryId + ", " +
                    "fid_mat_req = " + mnFkMaterialRequestId + ", " +
                    "fid_mat_req_ety = " + mnFkMaterialRequestEntryId + " " +
                    "WHERE id_dps_mat_req = " + mnPkDpsMatReqId + ";";
            }
            
            connection.createStatement().execute(sql);
            
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (SQLException ex) {
            Logger.getLogger(SDataDpsMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SDataDpsMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, ex);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return null;
    }
    
    public SDataDpsMaterialRequest clone() throws CloneNotSupportedException {
        SDataDpsMaterialRequest registry = new SDataDpsMaterialRequest();
        
        registry.setPkDpsMatReqId(this.getPkDpsMatReqId());
        registry.setQuantity(this.getQuantity());
        registry.setValue(this.getValue());
        registry.setValueCy(this.getValueCy());
        registry.setFkDpsYearId(this.getFkDpsYearId());
        registry.setFkDpsDocId(this.getFkDpsDocId());
        registry.setFkDpsEntryId(this.getFkDpsEntryId());
        registry.setFkMaterialRequestId(this.getFkMaterialRequestId());
        registry.setFkMaterialRequestEntryId(this.getFkMaterialRequestEntryId());
        
        return registry;
    }
}

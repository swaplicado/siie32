/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import erp.data.SDataConstants;
import java.util.Date;

/**
 *
 * @author Edwin Carmona
 */
public class SDataDiogMatConsEntCostCenter extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkDiogConsEntCostCenter;
    protected double mdPercentage;
    protected int mnFkDiogYearId;
    protected int mnFkDiogDocId;
    protected int mnFkSubentMatConsumptionEntityId;
    protected int mnFkSubentMatConsumptionSubentityId;
    protected int mnFkCostCenterId;

    public SDataDiogMatConsEntCostCenter() {
        super(SDataConstants.TRN_DIOG_CONS_ENT_CC);
        reset();
    }

    public void setPkDiogConsEntCostCenter(int n) { mnPkDiogConsEntCostCenter = n; }
    public void setPercentage(double d) { mdPercentage = d; }
    public void setFkDiogYearId(int n) { mnFkDiogYearId = n; }
    public void setFkDiogDocId(int n) { mnFkDiogDocId = n; }
    public void setFkSubentMatConsumptionEntityId(int n) { mnFkSubentMatConsumptionEntityId = n; }
    public void setFkSubentMatConsumptionSubentityId(int n) { mnFkSubentMatConsumptionSubentityId = n; }
    public void setFkCostCenterId(int n) { mnFkCostCenterId = n; }

    public int getPkDiogConsEntCostCenter() { return mnPkDiogConsEntCostCenter; }
    public double getPercentage() { return mdPercentage; }
    public int getFkDiogYearId() { return mnFkDiogYearId; }
    public int getFkDiogDocId() { return mnFkDiogDocId; }
    public int getFkSubentMatConsumptionEntityId() { return mnFkSubentMatConsumptionEntityId; }
    public int getFkSubentMatConsumptionSubentityId() { return mnFkSubentMatConsumptionSubentityId; }
    public int getFkCostCenterId() { return mnFkCostCenterId; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkDiogConsEntCostCenter = ((int[]) pk)[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDiogConsEntCostCenter };
    }
    
    @Override
    public void reset() {
        super.resetRegistry();

        mnPkDiogConsEntCostCenter = 0;
        mdPercentage = 0;
        mnFkDiogYearId = 0;
        mnFkDiogDocId = 0;
        mnFkSubentMatConsumptionEntityId = 0;
        mnFkSubentMatConsumptionSubentityId = 0;
        mnFkCostCenterId = 0;
    }
    
    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = "SELECT * FROM " + SDataConstants.TablesMap.get(SDataConstants.TRN_DIOG_CONS_ENT_CC) + " " +
                    "WHERE id_diog_ce_cc = " + key[0];

            resultSet = statement.getConnection().createStatement().executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkDiogConsEntCostCenter = resultSet.getInt("id_diog_ce_cc");
                mdPercentage = resultSet.getDouble("percentage");
                mnFkDiogYearId = resultSet.getInt("fid_diog_year");
                mnFkDiogDocId = resultSet.getInt("fid_diog_doc");
                mnFkSubentMatConsumptionEntityId = resultSet.getInt("fid_mat_sub_cons_ent");
                mnFkSubentMatConsumptionSubentityId = resultSet.getInt("fid_mat_sub_cons_sub_ent");
                mnFkCostCenterId = resultSet.getInt("fid_cc");

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
                
                sql = "INSERT INTO " + SDataConstants.TablesMap.get(SDataConstants.TRN_DIOG_CONS_ENT_CC) + " VALUES (" +
                        mnPkDiogConsEntCostCenter + ", " + 
                        mdPercentage + ", " + 
                        mnFkDiogYearId + ", " + 
                        mnFkDiogDocId + ", " + 
                        mnFkSubentMatConsumptionEntityId + ", " + 
                        mnFkSubentMatConsumptionSubentityId + ", " + 
                        "'" + mnFkCostCenterId + "' " +
                        ")";
            }
            else {
                sql = "UPDATE " + SDataConstants.TablesMap.get(SDataConstants.TRN_DIOG_CONS_ENT_CC) + " SET " +
//                    "id_diog_ce_cc = " + mnPkDiogConsEntCostCenter + ", " +
                    "percentage = " + mdPercentage + ", " +
                    "fid_diog_year = " + mnFkDiogYearId + ", " +
                    "fid_diog_doc = " + mnFkDiogDocId + ", " +
                    "fid_mat_sub_cons_ent = " + mnFkSubentMatConsumptionEntityId + ", " +
                    "fid_mat_sub_cons_sub_ent = " + mnFkSubentMatConsumptionSubentityId + ", " +
                    "fid_cc = '" + mnFkCostCenterId + "' ";
                
                sql += "WHERE id_diog_ce_cc = " + mnPkDiogConsEntCostCenter;
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
        return null;
    }
    
    public SDataRegistry clone() throws CloneNotSupportedException {
        SDataDiogMatConsEntCostCenter registry = new SDataDiogMatConsEntCostCenter();
        
        registry.setPkDiogConsEntCostCenter(this.getPkDiogConsEntCostCenter());
        registry.setPercentage(this.getPercentage());
        registry.setFkDiogYearId(this.getFkDiogYearId());
        registry.setFkDiogDocId(this.getFkDiogDocId());
        registry.setFkSubentMatConsumptionEntityId(this.getFkSubentMatConsumptionEntityId());
        registry.setFkSubentMatConsumptionSubentityId(this.getFkSubentMatConsumptionSubentityId());
        registry.setFkCostCenterId(this.getFkCostCenterId());
        
        return registry;
    }
}

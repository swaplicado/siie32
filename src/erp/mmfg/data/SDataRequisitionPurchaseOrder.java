/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.util.Date;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataRequisitionPurchaseOrder extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkReqYearId;
    protected int mnPkReqId;
    protected int mnPkReqEntryId;
    protected int mnPkDpsYearId;
    protected int mnPkDpsDocId;
    protected int mnPkDpsEntryId;
    protected double mdQuantity;
            
    public SDataRequisitionPurchaseOrder() {
        super(SDataConstants.MFG_REQ_PUR);
        reset();
    }

    public void setPkReqYearId(int n) { mnPkReqYearId = n; }
    public void setPkReqId(int n) { mnPkReqId = n; }
    public void setPkReqEntryId(int n) { mnPkReqEntryId = n; }
    public void setPkDpsYearId(int n) { mnPkDpsYearId = n; }
    public void setPkDpsDocId(int n) { mnPkDpsDocId = n; }
    public void setPkDpsEntryId(int n) { mnPkDpsEntryId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    
    public int getPkReqYearId() { return mnPkReqYearId; }
    public int getPkReqId() { return mnPkReqId; }
    public int getPkReqEntryId() { return mnPkReqEntryId; }
    public int getPkDpsYearId() { return mnPkDpsYearId; }
    public int getPkDpsDocId() { return mnPkDpsDocId; }
    public int getPkDpsEntryId() { return mnPkDpsEntryId; }
    public double getQuantity() { return mdQuantity; }
            
    @Override
    public void reset() {
        super.resetRegistry();

        mnPkReqYearId = 0;
        mnPkReqId = 0;
        mnPkReqEntryId = 0;
        mnPkDpsYearId = 0;
        mnPkDpsDocId = 0;
        mnPkDpsEntryId = 0;
        mdQuantity = 0;        
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object key) {
        mnPkReqYearId = ((int[]) key)[0];
        mnPkReqId = ((int[]) key)[1];
        mnPkReqEntryId = ((int[]) key)[2];
        mnPkDpsYearId = ((int[]) key)[3];
        mnPkDpsDocId = ((int[]) key)[4];
        mnPkDpsEntryId = ((int[]) key)[5];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkReqYearId, mnPkReqId, mnPkReqEntryId, mnPkDpsYearId, mnPkDpsDocId, mnPkDpsEntryId };
    }
    
    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        
        ResultSet resultSet = null;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = "SELECT m.* " +
                "FROM mfg_req_pur AS m " +
                "WHERE m.id_req_year = " + key[0] + " AND m.id_req = " + key[1] + " AND m.id_ety = " + key[2] + " AND " +
                "m.id_dps_year = " + key[3] + " AND m.id_dps_doc = " + key[4] + " AND m.id_dps_ety = " + key[5];

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkReqYearId = resultSet.getInt("m.id_req_year");
                mnPkReqId = resultSet.getInt("m.id_req");
                mnPkReqEntryId = resultSet.getInt("m.id_req_ety");
                mnPkDpsYearId = resultSet.getInt("m.id_dps_year");
                mnPkDpsDocId = resultSet.getInt("m.id_dps_doc");
                mnPkDpsEntryId = resultSet.getInt("m.id_dps_ety");
                mdQuantity = resultSet.getDouble("m.qty");
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int i = 0;
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mfg_req_pur_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkReqYearId);
            callableStatement.setInt(nParam++, mnPkReqId);
            callableStatement.setInt(nParam++, mnPkReqEntryId);
            callableStatement.setInt(nParam++, mnPkDpsYearId);
            callableStatement.setInt(nParam++, mnPkDpsDocId);
            callableStatement.setInt(nParam++, mnPkDpsEntryId);
            callableStatement.setDouble(nParam++, mdQuantity);            
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int delete(java.sql.Connection connection) {
        
        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.util.Date;
import java.util.Vector;
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
public class SDataExplotionMaterialsRequisition extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    protected int mnPkExpYearId;
    protected int mnPkExpId;
    protected int mnPkReqYearId;
    protected int mnPkReqId;

    protected boolean mbDbmsIsDelRequisition;

    public SDataExplotionMaterialsRequisition() {
        super(SDataConstants.MFG_EXP_REQ);
        reset();
    }
    
    public void setPkExpYearId(int n) { mnPkExpYearId = n; }
    public void setPkExpId(int n) { mnPkExpId = n; }
    public void setPkReqYearId(int n) { mnPkReqYearId = n; }
    public void setPkReqId(int n) { mnPkReqId = n; }

    public int getPkExpYearId() { return mnPkExpYearId; }
    public int getPkExpId() { return mnPkExpId; }
    public int getPkReqYearId() { return mnPkReqYearId; }
    public int getPkReqId() { return mnPkReqId; }

    public void setDbmsIsDelRequisition(boolean b) { mbDbmsIsDelRequisition = b; }

    public boolean getDbmsIsDelRequisition() { return mbDbmsIsDelRequisition; }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkExpYearId = 0;
        mnPkExpId = 0;
        mnPkReqYearId = 0;
        mnPkReqId = 0;

        mbDbmsIsDelRequisition = false;
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkExpYearId = ((int[]) pk)[0];
        mnPkExpId = ((int[]) pk)[1];
        mnPkReqYearId = ((int[]) pk)[2];
        mnPkReqId = ((int[]) pk)[3];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkExpYearId, mnPkExpId, mnPkReqYearId, mnPkReqId };
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int save(java.sql.Connection connection) {
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL mfg_exp_req_save(" +
                    "?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkExpYearId);
            callableStatement.setInt(nParam++, mnPkExpId);
            callableStatement.setInt(nParam++, mnPkReqYearId);
            callableStatement.setInt(nParam++, mnPkReqId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

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
        int[] nPkRequisition = null;
        Vector<int[]> vRequisitions = new Vector<int[]>();
        String sSql = "";

        SDataRequisition oRequisition = null;
        SDataRequisitionEntry oRequisitionEntry = null;

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Get ids of requisition of materials:

            statement = connection.createStatement();
            sSql = "SELECT * FROM mfg_exp_req WHERE id_exp_year = " + mnPkExpYearId + " AND id_exp = " + mnPkExpId + " ";
            resultSet = statement.executeQuery(sSql);
            while (resultSet.next()) {
                vRequisitions.add(new int[] { resultSet.getInt("id_req_year"), resultSet.getInt("id_req") });
            }

            // Delete explotion materials vs. requisition materials:

            sSql = "DELETE FROM mfg_exp_req WHERE id_exp_year = " + mnPkExpYearId + " AND id_exp = " + mnPkExpId + " ";
            statement = connection.createStatement();
            statement.executeUpdate(sSql);

            if (mbDbmsIsDelRequisition) {

                // Delete requisition and its entries:
                
                for (int i=0; i<vRequisitions.size(); i++) {

                    // Delete requisition entries:

                    nPkRequisition = vRequisitions.get(i);
                    oRequisitionEntry = new SDataRequisitionEntry();
                    oRequisitionEntry.setPkReqYearId(nPkRequisition[0]);
                    oRequisitionEntry.setPkReqId(nPkRequisition[1]);
                    if (oRequisitionEntry.delete(connection) != SLibConstants.DB_ACTION_DELETE_OK) {
                        mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_DELETE_DEP);
                    }

                    // Delete requisition:

                    oRequisition = new SDataRequisition();
                    oRequisition.setPkYearId(nPkRequisition[0]);
                    oRequisition.setPkReqId(nPkRequisition[1]);
                    if (oRequisition.delete(connection) != SLibConstants.DB_ACTION_DELETE_OK) {
                        mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_DELETE_DEP);
                    }
                }
            }
            
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}

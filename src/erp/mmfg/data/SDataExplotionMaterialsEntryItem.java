/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.util.Date;
import java.sql.CallableStatement;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataExplotionMaterialsEntryItem extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    protected int mnPkYearId;
    protected int mnPkExpId;
    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnPkOrdYearId;
    protected int mnPkOrdId;
    protected double mdGrossRequirement;

    protected boolean mbDbmsIsRequest;

    public SDataExplotionMaterialsEntryItem() {
        super(SDataConstants.MFG_EXP_ETY_ITEM);
        reset();
    }
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkExpId(int n) { mnPkExpId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkOrdYearId(int n) { mnPkOrdYearId = n; }
    public void setPkOrdId(int n) { mnPkOrdId = n; }
    public void setGrossRequirement(double d) { mdGrossRequirement = d; }
    
    public int getPkYearId() { return mnPkYearId; }
    public int getPkExpId() { return mnPkExpId; }
    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkOrdYearId() { return mnPkOrdYearId; }
    public int getPkOrdId() { return mnPkOrdId; }
    public double getGrossRequirement() { return mdGrossRequirement; }

    public void setDbmsIsRequest(boolean b) { mbDbmsIsRequest = b; }

    public boolean getDbmsIsRequest() { return mbDbmsIsRequest; }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkYearId = 0;
        mnPkExpId = 0;
        mnPkItemId = 0;
        mnPkUnitId = 0;
        mdGrossRequirement = 0;
        mnPkOrdYearId = 0;
        mnPkOrdId = 0;

        mbDbmsIsRequest = false;
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkExpId = ((int[]) pk)[1];
        mnPkItemId = ((int[]) pk)[2];
        mnPkUnitId = ((int[]) pk)[3];
        mnPkOrdYearId = ((int[]) pk)[4];
        mnPkOrdId = ((int[]) pk)[5];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkExpId, mnPkItemId, mnPkUnitId, mnPkOrdYearId, mnPkOrdId };
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
            callableStatement = connection.prepareCall("{ CALL mfg_exp_ety_item_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkExpId);
            callableStatement.setInt(nParam++, mnPkItemId);
            callableStatement.setInt(nParam++, mnPkUnitId);
            callableStatement.setInt(nParam++, mnPkOrdYearId);
            callableStatement.setInt(nParam++, mnPkOrdId);
            callableStatement.setDouble(nParam++, mdGrossRequirement);            
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
    public Date getLastDbUpdate() {
        return null;
    }
}

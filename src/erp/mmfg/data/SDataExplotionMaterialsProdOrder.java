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
public class SDataExplotionMaterialsProdOrder extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    protected int mnPkExpYearId;
    protected int mnPkExpId;
    protected int mnPkOrdYearId;
    protected int mnPkOrdId;

    public SDataExplotionMaterialsProdOrder() {
        super(SDataConstants.MFG_EXP_ORD);
        reset();
    }
    
    public void setPkExpYearId(int n) { mnPkExpYearId = n; }
    public void setPkExpId(int n) { mnPkExpId = n; }
    public void setPkOrdYearId(int n) { mnPkOrdYearId = n; }
    public void setPkOrdId(int n) { mnPkOrdId = n; }

    public int getPkExpYearId() { return mnPkExpYearId; }
    public int getPkExpId() { return mnPkExpId; }
    public int getPkOrdYearId() { return mnPkOrdYearId; }
    public int getPkOrdId() { return mnPkOrdId; }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkExpYearId = 0;
        mnPkExpId = 0;
        mnPkOrdYearId = 0;
        mnPkOrdId = 0;
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkExpYearId = ((int[]) pk)[0];
        mnPkExpId = ((int[]) pk)[1];
        mnPkOrdYearId = ((int[]) pk)[2];
        mnPkOrdId = ((int[]) pk)[3];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkExpYearId, mnPkExpId, mnPkOrdYearId, mnPkOrdId };
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
            callableStatement = connection.prepareCall("{ CALL mfg_exp_ord_save(" +
                    "?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkExpYearId);
            callableStatement.setInt(nParam++, mnPkExpId);
            callableStatement.setInt(nParam++, mnPkOrdYearId);
            callableStatement.setInt(nParam++, mnPkOrdId);
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

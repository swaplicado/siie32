/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SProcProductionOrderUpdate extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcProductionOrderUpdate() {
        super(SProcConstants.MFG_ORD_UPD);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        java.sql.CallableStatement callableStatement = null;
        java.util.Date date = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL mfg_ord_upd(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam, (Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setBoolean(nParam, (Boolean) mvParamsIn.get(nParam - 1)); nParam++;

            date = (java.util.Date) mvParamsIn.get(nParam - 1);
            if (date == null) { callableStatement.setNull(nParam, java.sql.Types.DATE); } else { callableStatement.setDate(nParam, new java.sql.Date(date.getTime())); }
            nParam++;

            date = (java.util.Date) mvParamsIn.get(nParam - 1);
            if (date == null) { callableStatement.setNull(nParam, java.sql.Types.DATE); } else { callableStatement.setDate(nParam, new java.sql.Date(date.getTime())); }
            nParam++;
                        
            callableStatement.setInt(nParam, (Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.INTEGER); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.VARCHAR); nParam++;
            callableStatement.execute();

            mvParamsOut.clear();
            mnLastDbActionResult = SLibConstants.DB_PROCEDURE_OK;
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_PROCEDURE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_PROCEDURE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }
}

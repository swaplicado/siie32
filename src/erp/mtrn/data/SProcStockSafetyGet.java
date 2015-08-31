/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import java.sql.CallableStatement;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SProcStockSafetyGet extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcStockSafetyGet() {
        super(0);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ " +
                    "CALL trn_stk_sfty_get(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?) }");
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;            
            if ((java.lang.Integer) mvParamsIn.get(nParam - 1) > 0) callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); else callableStatement.setNull(nParam, java.sql.Types.INTEGER); nParam++;
            if ((java.lang.Integer) mvParamsIn.get(nParam - 1) > 0) callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); else callableStatement.setNull(nParam, java.sql.Types.INTEGER); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DOUBLE); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DOUBLE); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DOUBLE); nParam++;
            callableStatement.execute();

            mvParamsOut.clear();
            mvParamsOut.add(callableStatement.getDouble(nParam-3));
            mvParamsOut.add(callableStatement.getDouble(nParam-2));
            mvParamsOut.add(callableStatement.getDouble(nParam-1));
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

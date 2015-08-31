/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos B.
 */
public class SProcDpsNumBalanceGet extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcDpsNumBalanceGet() {
        super(SProcConstants.TRN_DPS_NUM_BAL_GET);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL trn_dps_num_bal_get(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?) }");
            callableStatement.setInt(nParam, (Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setString(nParam, (String) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.INTEGER); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.INTEGER); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DECIMAL); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DECIMAL); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.SMALLINT);
            callableStatement.execute();

            mvParamsOut.clear();
            mvParamsOut.add(callableStatement.getInt(nParam-4));
            mvParamsOut.add(callableStatement.getInt(nParam-3));
            mvParamsOut.add(callableStatement.getDouble(nParam-2));
            mvParamsOut.add(callableStatement.getDouble(nParam-1));
            mvParamsOut.add(callableStatement.getInt(nParam));
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

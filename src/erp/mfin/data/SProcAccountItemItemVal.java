/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Sergio Flores
 */
public class SProcAccountItemItemVal extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcAccountItemItemVal() {
        super(SProcConstants.FIN_ACC_ITEM_ITEM_VAL);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL fin_acc_item_item_val(?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam, (Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setDate(nParam, new java.sql.Date(((java.util.Date) mvParamsIn.get(nParam - 1)).getTime())); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.INTEGER);
            callableStatement.execute();

            mvParamsOut.clear();
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

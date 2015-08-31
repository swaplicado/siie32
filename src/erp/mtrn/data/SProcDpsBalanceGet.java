/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.mmkt.data.*;
import erp.mmfg.data.*;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SProcDpsBalanceGet extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcDpsBalanceGet() {
        super(SProcConstants.TRN_DPS_BAL_GET);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        int anFkTpSysMovId[] = (int[]) mvParamsIn.get(2);
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL trn_dps_bal_get(?, ?, ?, ?, ?,?,?) }");
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) anFkTpSysMovId[0]); nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) anFkTpSysMovId[1]); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DOUBLE); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DOUBLE); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.INTEGER); nParam++;
            callableStatement.execute();

            mvParamsOut.clear();
            mvParamsOut.add(callableStatement.getString(nParam - 3));
            mvParamsOut.add(callableStatement.getString(nParam - 2));
            mvParamsOut.add(callableStatement.getString(nParam - 1));
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

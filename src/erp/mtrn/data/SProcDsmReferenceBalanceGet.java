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
public class SProcDsmReferenceBalanceGet extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcDsmReferenceBalanceGet() {
        super(SProcConstants.TRN_DSM_REF_BAL_GET);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL trn_diom_ref_bal_get(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?) }");
            callableStatement.setString(nParam, (java.lang.String) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setString(nParam, (java.lang.String) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DOUBLE); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DOUBLE); nParam++;
            callableStatement.execute();

            mvParamsOut.clear();
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

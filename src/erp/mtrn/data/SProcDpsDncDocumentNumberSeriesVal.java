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
 * @author Alfonso Flores
 */
public class SProcDpsDncDocumentNumberSeriesVal extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcDpsDncDocumentNumberSeriesVal() {
        super(0);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL trn_dnc_dps_dns_val(?, ?, ?) }");
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
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

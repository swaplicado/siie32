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
 * @author Alfonso Flores
 */
public class SProcDpsTypeCode extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcDpsTypeCode() {
        super(SProcConstants.TRN_TP_DPS_CODE);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        java.sql.CallableStatement callableStatement = null;

        try {
            callableStatement = connection.prepareCall("{ CALL trn_get_tp_doc_descrip(?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.CHAR); nParam++;
            callableStatement.execute();

            mvParamsOut.clear();
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

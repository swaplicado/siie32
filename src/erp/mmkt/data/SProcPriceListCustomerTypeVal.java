/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.data;

import erp.mmfg.data.*;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SProcPriceListCustomerTypeVal extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcPriceListCustomerTypeVal() {
        super(SProcConstants.MKT_PLIST_CUS_TP_VAL);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL mkt_plist_cus_tp_val(?, ?, ?) }");
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
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

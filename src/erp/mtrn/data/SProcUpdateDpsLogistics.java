/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import java.sql.CallableStatement;

import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Sergio Flores
 */
public class SProcUpdateDpsLogistics extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcUpdateDpsLogistics() {
        super(SProcConstants.TRN_DPS_LOG_UPD);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int param = 0;
        CallableStatement callableStatement = null;
        java.util.Date date = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL trn_dps_log_upd(?, ?, ?, ?, ?, ?, ?) }");
            
            /*
             * param 1 in: integer (id_year)
             * param 2 in: integer (id_doc)
             * param 3 in: date (dt_start_credit_n)
             * param 4 in: date (dt_shipment_n)
             * param 5 in: date (dt_delivery_n)
             * param 6 out: smallint (id_error)
             * param 7 out: varchar (error)
             */

            for (param = 1; param <= 2; param++) {
                callableStatement.setInt(param, (Integer) mvParamsIn.get(param - 1));
            }

            for (param = 3; param <= 5; param++) {
                if (mvParamsIn.size() < param) {
                    callableStatement.setNull(param, java.sql.Types.DATE);
                }
                else {
                    date = (java.util.Date) mvParamsIn.get(param - 1);

                    if (date.getTime() == 0) {
                        callableStatement.setNull(param, java.sql.Types.DATE);
                    }
                    else {
                        callableStatement.setDate(param, new java.sql.Date(date.getTime()));
                    }
                }
            }

            callableStatement.registerOutParameter(6, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mvParamsOut.clear();
            mvParamsOut.add(callableStatement.getInt(6));
            mvParamsOut.add(callableStatement.getString(7));
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

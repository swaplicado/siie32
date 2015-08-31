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
public class SProcGetDpsEntrySuppliedInformation extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcGetDpsEntrySuppliedInformation() {
        super(SProcConstants.TRN_GET_DPS_ETY_PEND_SUP);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        java.sql.CallableStatement callableStatement = null;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL trn_get_dps_ety_supply_info" +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    " ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setBoolean(nParam, (java.lang.Boolean) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.CHAR); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.CHAR); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DECIMAL); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DECIMAL); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DECIMAL); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.CHAR); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DECIMAL); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DECIMAL); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.SMALLINT); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DECIMAL); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.SMALLINT); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DECIMAL); nParam++;

            callableStatement.execute();

            mvParamsOut.clear();
            mvParamsOut.add(callableStatement.getString(nParam - 12));
            mvParamsOut.add(callableStatement.getString(nParam - 11));
            mvParamsOut.add(callableStatement.getDouble(nParam - 10));
            mvParamsOut.add(callableStatement.getDouble(nParam - 9));
            mvParamsOut.add(callableStatement.getDouble(nParam - 8));
            mvParamsOut.add(callableStatement.getString(nParam - 7));
            mvParamsOut.add(callableStatement.getDouble(nParam - 6));
            mvParamsOut.add(callableStatement.getDouble(nParam - 5));
            mvParamsOut.add(callableStatement.getInt(nParam - 4));
            mvParamsOut.add(callableStatement.getDouble(nParam - 3));
            mvParamsOut.add(callableStatement.getInt(nParam - 2));
            mvParamsOut.add(callableStatement.getDouble(nParam - 1));
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

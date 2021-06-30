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
 * @author Juan Barajas
 */
public class SProcDpsEntryDateVal extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcDpsEntryDateVal() {
        super(SProcConstants.TRN_DPS_ETY_DATE_VAL);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            callableStatement = connection.prepareCall("{ CALL trn_dps_ety_date_val(?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, ((int[]) mvParamsIn.get(0))[0]);
            callableStatement.setInt(nParam++, ((int[]) mvParamsIn.get(0))[1]);
            callableStatement.setInt(nParam++, ((int[]) mvParamsIn.get(0))[2]);
            callableStatement.setDate(nParam++, new java.sql.Date(((java.util.Date) mvParamsIn.get(1)).getTime()));
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mvParamsOut.clear();
            mvParamsOut.add(callableStatement.getInt(nParam - 1));
            mvParamsOut.add(callableStatement.getString(nParam));
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SProcLeadtimeCoGet extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcLeadtimeCoGet() {
        super(SProcConstants.MFG_LTIME_CO_GET);
    }

    @Override
    public int call(java.sql.Connection connection) {
        boolean bNext = false;
        int nParam = 1;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL mfg_ltime_co_get(?, ?, ?) }");
            callableStatement.setInt(nParam++, (Integer) mvParamsIn.get(0));
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            bNext = callableStatement.execute();

            mvParamsOut.clear();
            while (bNext && mvParamsOut.size() <= 0) {
                resultSet = callableStatement.getResultSet();
                while (resultSet.next()) {
                    mvParamsOut.add(new int[] { resultSet.getInt(1), resultSet.getInt(2) });
                }

                bNext = callableStatement.getMoreResults();
            }

            
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

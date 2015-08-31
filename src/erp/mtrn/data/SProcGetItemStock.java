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
 * @author Alfonso Flores
 */
public class SProcGetItemStock extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcGetItemStock() {
        super(SProcConstants.ITMU_ITEM_STOCK);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL trn_calc_stock(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            if (mvParamsIn.get(nParam - 1)!= null) { callableStatement.setDate(nParam, new java.sql.Date(((java.util.Date) mvParamsIn.get(nParam - 1)).getTime()));  nParam++; } else { callableStatement.setNull(nParam++, java.sql.Types.DATE); }
            if (mvParamsIn.get(nParam - 1)!= null) { callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++; } else { callableStatement.setNull(nParam++, java.sql.Types.INTEGER);  }
            if (mvParamsIn.get(nParam - 1)!= null) { callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++; } else { callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);  }
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setLong(nParam, (java.lang.Long) mvParamsIn.get(nParam - 1)); nParam++;
            if (mvParamsIn.get(nParam - 1)!= null) { callableStatement.setString(nParam, (java.lang.String) mvParamsIn.get(nParam - 1)); nParam++; } else { callableStatement.setNull(nParam++, java.sql.Types.VARCHAR);  }
            callableStatement.registerOutParameter(nParam, java.sql.Types.DECIMAL);
            callableStatement.execute();

            mvParamsOut.clear();
            mvParamsOut.add(callableStatement.getDouble(nParam));
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

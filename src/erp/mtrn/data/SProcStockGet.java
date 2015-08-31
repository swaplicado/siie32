/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import java.sql.Statement;
import java.sql.ResultSet;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SProcStockGet extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcStockGet() {
        super(0);
    }

    @Override
    public int call(java.sql.Connection connection) {
        String sSql = "";

        ResultSet resultset = null;
        Statement statement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            sSql = "SELECT trn_stk_get(" +
                    mvParamsIn.get(0) + ", " +
                    mvParamsIn.get(1) + ", " +
                    mvParamsIn.get(2) + ", ";
            sSql += (mvParamsIn.size() <= 3 || mvParamsIn.get(3) == null ? "NULL" : mvParamsIn.get(3)) + ", ";
            sSql += (mvParamsIn.size() <= 4 || mvParamsIn.get(4) == null ? "NULL" : mvParamsIn.get(4)) + ", ";
            sSql += (mvParamsIn.size() <= 5 || mvParamsIn.get(5) == null ? "NULL" : mvParamsIn.get(5)) + ", ";
            sSql += (mvParamsIn.size() <= 6 || mvParamsIn.get(6) == null ? "NULL" : mvParamsIn.get(6)) + ", ";
            sSql += (mvParamsIn.size() <= 7 || mvParamsIn.get(7) == null ? "NULL" : mvParamsIn.get(7)) + ", ";
            sSql += (mvParamsIn.size() <= 8 || mvParamsIn.get(8) == null ? "NULL" : mvParamsIn.get(8)) + ") AS f_stk ";

            statement = connection.createStatement();
            statement.execute(sSql);
            resultset = statement.getResultSet();

            if (resultset.next()) {
                mvParamsOut.clear();
                mvParamsOut.add(resultset.getDouble("f_stk"));
                mnLastDbActionResult = SLibConstants.DB_PROCEDURE_OK;
            }
            else {
                mnLastDbActionResult = SLibConstants.DB_PROCEDURE_ERROR;
            }

            /*
            callableStatement = connection.prepareCall("{ " +
                    "CALL trn_stk_get(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?) }");

            //System.out.println(nParam + " - " + mvParamsIn.get(nParam - 1));
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;

            //System.out.println(nParam + " - " + mvParamsIn.get(nParam - 1));
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;

            //System.out.println(nParam + " - " + mvParamsIn.get(nParam - 1));
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;

            //System.out.println(nParam + " - " + mvParamsIn.get(nParam - 1));
            if (mvParamsIn.get(nParam - 1)!= null) {
                callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1));
                nParam++;
            }
            else {
                callableStatement.setNull(nParam, java.sql.Types.INTEGER);
                nParam++;
            }

            //System.out.println(nParam + " - " + mvParamsIn.get(nParam - 1));
            if (mvParamsIn.get(nParam - 1)!= null) {
                callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1));
                nParam++;
            }
            else {
                callableStatement.setNull(nParam, java.sql.Types.INTEGER);
                nParam++;
            }

            //System.out.println(nParam + " - " + mvParamsIn.get(nParam - 1));
            if (mvParamsIn.get(nParam - 1)!= null) {
                callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1));
                nParam++;
            }
            else {
                callableStatement.setNull(nParam, java.sql.Types.INTEGER);
                nParam++;
            }

            //System.out.println(nParam + " - " + mvParamsIn.get(nParam - 1));
            if (mvParamsIn.get(nParam - 1)!= null) {
                callableStatement.setDate(nParam, new java.sql.Date(((java.util.Date) mvParamsIn.get(nParam - 1)).getTime()));
                nParam++; }
            else {
                callableStatement.setNull(nParam, java.sql.Types.DATE);
                nParam++;
            }
            callableStatement.execute();
            */

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

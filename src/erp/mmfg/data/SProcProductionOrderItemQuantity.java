/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SProcProductionOrderItemQuantity extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcProductionOrderItemQuantity() {
        super(SProcConstants.MFG_ORD_ITEM_QRY);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        java.sql.CallableStatement callableStatement = null;
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL mfg_ord_item_qry(?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, SLibUtilities.parseInt(mvParamsIn.get(0).toString()));
            callableStatement.setInt(nParam++, (Integer) mvParamsIn.get(1));
            callableStatement.setInt(nParam++, (Integer) mvParamsIn.get(2));
            callableStatement.setInt(nParam++, (Integer) mvParamsIn.get(3));
            callableStatement.setInt(nParam++, (Integer) mvParamsIn.get(4));
            callableStatement.setBoolean(nParam++, (Boolean) mvParamsIn.get(5));
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();
            resultSet = callableStatement.getResultSet();
            mvParamsOut.clear();
            
            if (((Integer) mvParamsIn.get(0)) == 1) {
                while (resultSet.next()) {
                    mvParamsOut.add(new Object[] {
                        resultSet.getInt("id_year"),
                        resultSet.getInt("id_ord"),
                        resultSet.getString("ref"),
                        resultSet.getInt("id_bom"),
                        resultSet.getString("bom"),
                        resultSet.getInt("root"),
                        resultSet.getDouble("qty"),
                        resultSet.getString("symbol"),
                        resultSet.getDate("dt_dly"),
                        resultSet.getInt("o.chgs")
                    });
                }
            } else {
                while (resultSet.next()) {
                    mvParamsOut.add(new Object[] {
                        0,
                        0,
                        "(n/a)",
                        resultSet.getInt("id_bom"),
                        resultSet.getString("bom"),
                        resultSet.getInt("root"),
                        1,
                        "",
                        null,
                        1
                    });
                }
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

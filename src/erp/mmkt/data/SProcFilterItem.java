/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.data;

import erp.data.SDataConstantsSys;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SProcFilterItem extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcFilterItem() {
        super(SProcConstants.MKT_PLIST_PRICE_FILTER);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        java.sql.CallableStatement callableStatement = null;
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL mkt_filter_item(?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, (java.lang.Integer) mvParamsIn.get(0));
            callableStatement.setInt(nParam++, (java.lang.Integer) mvParamsIn.get(1));
            callableStatement.setInt(nParam++, (java.lang.Integer) mvParamsIn.get(2));
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();
            resultSet = callableStatement.getResultSet();

            mvParamsOut.clear();
            while (resultSet.next()) {
                if ((java.lang.Integer) mvParamsIn.get(2) == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    mvParamsOut.add(new Object[] { resultSet.getInt("i.id_item"), resultSet.getString("i.item_key"), resultSet.getString("i.item"), 0 });
                }
                else {
                    mvParamsOut.add(new Object[] { resultSet.getInt("i.id_item"), resultSet.getString("i.item"), resultSet.getString("i.item_key"), 0 });
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

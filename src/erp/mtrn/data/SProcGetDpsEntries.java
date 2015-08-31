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
public class SProcGetDpsEntries extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcGetDpsEntries() {
        super(SProcConstants.TRN_GET_DPS_ETY);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        java.sql.CallableStatement callableStatement = null;
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL trn_get_dps_ety(?, ?) }");
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.setInt(nParam, (java.lang.Integer) mvParamsIn.get(nParam - 1)); nParam++;
            callableStatement.execute();
            resultSet = callableStatement.getResultSet();

            mvParamsOut.clear();
            while (resultSet.next()) {
                mvParamsOut.add(new Object[] { resultSet.getInt("ety.id_year"), resultSet.getInt("ety.id_doc"), resultSet.getInt("ety.id_ety"),
                resultSet.getDouble("ety.orig_qty"), resultSet.getString("i.item_key"), resultSet.getString("i.item"), resultSet.getString("u.symbol"),
                resultSet.getDouble("adj"), resultSet.getInt("ety.fid_item"), resultSet.getInt("ety.fid_unit"), resultSet.getInt("ety.fid_orig_unit"),
                resultSet.getDouble("ety.price_u_real_cur_r"), resultSet.getDouble("d.exc_rate"), resultSet.getInt("d.fid_cur")});
            }
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

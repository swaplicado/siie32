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
public class SProcBomExplotionMaterialsReq extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcBomExplotionMaterialsReq() {
        super(SProcConstants.MFG_BOM_EXP_GREQ);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        java.sql.CallableStatement callableStatement = null;
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ " + "CALL mfg_bom_exp_greq(?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, SLibUtilities.parseInt(mvParamsIn.get(0).toString()));
            callableStatement.setString(nParam++, mvParamsIn.get(1).toString());
            callableStatement.setDouble(nParam++, SLibUtilities.parseDouble(mvParamsIn.get(2).toString()));
            callableStatement.setInt(nParam++, SLibUtilities.parseInt(mvParamsIn.get(3).toString()));
            callableStatement.setInt(nParam++, SLibUtilities.parseInt(mvParamsIn.get(4).toString()));
            callableStatement.setString(nParam++, mvParamsIn.get(5).toString());
            callableStatement.setInt(nParam++, SLibUtilities.parseInt(mvParamsIn.get(6).toString()));
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();
            resultSet = callableStatement.getResultSet();

            mvParamsOut.clear();
            while (resultSet.next()) {
                mvParamsOut.add(new Object[] {
                    resultSet.getString("f_item"),
                    resultSet.getInt("id_item"),
                    resultSet.getDouble("f_qty"),
                    resultSet.getInt("t.id_bom"),
                    resultSet.getDate("t.ts_start"),
                    resultSet.getInt("t.level"),
                    resultSet.getBoolean("t.b_exp"),
                    resultSet.getInt("t.fid_unit"),
                    resultSet.getString("t.item"),
                    resultSet.getString("u.symbol"),
                    resultSet.getBoolean("t.b_req")
                });
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

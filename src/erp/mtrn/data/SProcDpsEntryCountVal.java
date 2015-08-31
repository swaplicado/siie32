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
public class SProcDpsEntryCountVal extends erp.lib.data.SDataProcedure implements java.io.Serializable {

    public SProcDpsEntryCountVal(int procedureType) {
        super(procedureType);
    }

    @Override
    public int call(java.sql.Connection connection) {
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            switch (mnProcedureType) {
                case SProcConstants.TRN_DPS_ETY_COUNT_LINK_DPS_SRC:
                    callableStatement = connection.prepareCall("{ CALL trn_dps_ety_count_supply_dps_src(?, ?, ?, ?) }");
                    break;
                case SProcConstants.TRN_DPS_ETY_COUNT_LINK_DPS_DES:
                    callableStatement = connection.prepareCall("{ CALL trn_dps_ety_count_supply_dps_des(?, ?, ?, ?) }");
                    break;
                case SProcConstants.TRN_DPS_ETY_COUNT_ADJ_DOC:
                    callableStatement = connection.prepareCall("{ CALL trn_dps_ety_count_adj_doc(?, ?, ?, ?) }");
                    break;
                case SProcConstants.TRN_DPS_ETY_COUNT_ADJ_ADJ:
                    callableStatement = connection.prepareCall("{ CALL trn_dps_ety_count_adj_adj(?, ?, ?, ?) }");
                    break;
                case SProcConstants.TRN_DPS_ETY_COUNT_COMMS:
                    callableStatement = connection.prepareCall("{ CALL trn_dps_ety_count_comms(?, ?, ?, ?) }");
                    break;
                case SProcConstants.TRN_DPS_ETY_COUNT_DIOG:
                    callableStatement = connection.prepareCall("{ CALL trn_dps_ety_count_diog(?, ?, ?, ?) }");
                    break;
                case SProcConstants.TRN_DPS_ETY_COUNT_SHIP:
                    callableStatement = connection.prepareCall("{ CALL trn_dps_ety_count_ship(?, ?, ?, ?) }");
                    break;
                case SProcConstants.TRN_DIOG_ETY_COUNT_SHIP:
                    callableStatement = connection.prepareCall("{ CALL trn_diog_ship_val(?, ?, ?, ?) }");
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }

            callableStatement.setInt(nParam++, ((int[]) mvParamsIn.get(0))[0]);
            callableStatement.setInt(nParam++, ((int[]) mvParamsIn.get(0))[1]);
            callableStatement.setInt(nParam++, ((int[]) mvParamsIn.get(0))[2]);
            callableStatement.registerOutParameter(nParam, java.sql.Types.INTEGER);
            callableStatement.execute();

            mvParamsOut.clear();
            mvParamsOut.add(callableStatement.getInt(nParam));
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

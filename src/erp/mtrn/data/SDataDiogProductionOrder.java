/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Alfonso Flores
 */
public class SDataDiogProductionOrder extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkDiogYearId;
    protected int mnPkDiogDocId;
    protected int mnPkProductionOrderYearId;
    protected int mnPkProductionOrderDocId;

    public SDataDiogProductionOrder() {
        //super(SDataConstants.TRN_DIOG_ORD); XXX
        super(0);
        reset();
    }

    public void setPkDiogYearId(int n) { mnPkDiogYearId = n; }
    public void setPkDiogDocId(int n) { mnPkDiogDocId = n; }
    public void setPkProductionOrderYearId(int n) { mnPkProductionOrderYearId = n; }
    public void setPkProductionOrderDocId(int n) { mnPkProductionOrderDocId = n; }

    public int getPkDiogYearId() { return mnPkDiogYearId; }
    public int getPkDiogDocId() { return mnPkDiogDocId; }
    public int getPkProductionOrderYearId() { return mnPkProductionOrderYearId; }
    public int getPkProductionOrderDocId() { return mnPkProductionOrderDocId; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkDiogYearId = ((int[]) pk)[0];
        mnPkDiogDocId = ((int[]) pk)[1];
        mnPkProductionOrderYearId = ((int[]) pk)[2];
        mnPkProductionOrderDocId = ((int[]) pk)[3];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkDiogYearId, mnPkDiogDocId, mnPkProductionOrderYearId, mnPkProductionOrderDocId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkDiogYearId = 0;
        mnPkDiogDocId = 0;
        mnPkProductionOrderYearId = 0;
        mnPkProductionOrderDocId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_stk_diog_ord WHERE id_diog_year = " + key[0] + " AND id_diog_doc = " + key[1] +
                    " AND id_ord_year = " + key[2] + " AND id_ord_doc = " + key[3] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkDiogYearId = resultSet.getInt("id_diog_year");
                mnPkDiogDocId = resultSet.getInt("id_diog_doc");
                mnPkProductionOrderYearId = resultSet.getInt("id_ord_year");
                mnPkProductionOrderDocId = resultSet.getInt("id_ord_doc");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL trn_stk_diog_ord_save(?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkDiogYearId);
            callableStatement.setInt(nParam++, mnPkDiogDocId);
            callableStatement.setInt(nParam++, mnPkProductionOrderYearId);
            callableStatement.setInt(nParam++, mnPkProductionOrderDocId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return null;
    }
}

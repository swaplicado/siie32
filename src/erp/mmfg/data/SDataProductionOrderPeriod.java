/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.util.Date;
import java.util.Vector;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataProductionOrderPeriod extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkOrdYearId;
    protected int mnPkOrdId;
    protected int mnPkPerId;
    protected java.util.Date mtDateStart;
    protected java.util.Date mtDateEnd;
    protected double mdQty;

    protected java.lang.String msDbmsUnitSymbol;

    public SDataProductionOrderPeriod(){
        super(SDataConstants.MFG_ORD_PER);
        reset();
    }

    public void setPkOrdYearId(int n) { mnPkOrdYearId = n; }
    public void setPkOrdId(int n) { mnPkOrdId = n; }
    public void setPkPerId(int n) { mnPkPerId = n; }
    public void setDateStart(java.util.Date o) { mtDateStart = o; }
    public void setDateEnd(java.util.Date o) { mtDateEnd = o; }
    public void setQty(double d) { mdQty = d; }

    public int getPkOrdYearId() { return mnPkOrdYearId; }
    public int getPkOrdId() { return mnPkOrdId; }
    public int getPkPerId() { return mnPkPerId; }
    public java.util.Date getDateStart() { return mtDateStart; }
    public java.util.Date getDateEnd() { return mtDateEnd; }
    public double getQty() { return mdQty; }

    public void setDbmsUnitSymbol(java.lang.String s) { msDbmsUnitSymbol = s; }

    public java.lang.String getDbmsUnitSymbol() { return msDbmsUnitSymbol; }

    @Override
    public void setPrimaryKey(Object key) {
        mnPkOrdYearId = ((int[]) key)[0];
        mnPkOrdId = ((int[]) key)[1];
        mnPkPerId = ((int[]) key)[2];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkOrdYearId, mnPkOrdId, mnPkPerId };
        
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkOrdYearId = 0;
        mnPkOrdId = 0;
        mnPkPerId = 0;
        mtDateStart = null;
        mtDateEnd = null;
        mdQty = 0;

        msDbmsUnitSymbol = "";
    }

    @Override
    public int read(Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT p.*, u.symbol " +
                "FROM mfg_ord_per AS p " +
                "INNER JOIN mfg_ord AS o ON p.id_ord_year = o.id_year AND p.id_ord = o.id_ord " +
                "INNER JOIN erp.itmu_item AS i ON o.fid_item_r = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON o.fid_unit_r = u.id_unit " +
                "WHERE p.id_ord_year = " + key[0] + " AND p.id_ord = " + key[1] + " " + "AND p.id_per = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkOrdYearId = resultSet.getInt("p.id_ord_year");
                mnPkOrdId = resultSet.getInt("p.id_ord");
                mnPkPerId = resultSet.getInt("p.id_per");
                mtDateStart = resultSet.getDate("p.dt_start");
                mtDateEnd = resultSet.getDate("p.dt_end");
                mdQty = resultSet.getInt("p.qty");

                msDbmsUnitSymbol = resultSet.getString("u.symbol");
                
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
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ " +
                    "CALL mfg_ord_per_save(?, ?, ?, ?, ?, " +
                                          "?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkOrdYearId);
            callableStatement.setInt(nParam++, mnPkOrdId);
            callableStatement.setInt(nParam++, mnPkPerId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDateStart.getTime()));
            callableStatement.setDate(nParam++, new java.sql.Date(mtDateEnd.getTime()));
            callableStatement.setDouble(nParam++, mdQty);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}

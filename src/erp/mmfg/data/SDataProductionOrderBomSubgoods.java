/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.util.Date;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataProductionOrderBomSubgoods extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkOrderId;
    protected int mnPkSubgoodsId;
    protected double mdQuantity;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataProductionOrderBomSubgoods() {
        super(SDataConstants.MFG_ORD_SGDS);
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkOrderId(int n) { mnPkOrderId = n; }
    public void setPkSubgoodsId(int n) { mnPkSubgoodsId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkOrderId() { return mnPkOrderId; }
    public int getPkSubgoodsId() { return mnPkSubgoodsId; }
    public double getQuantity() { return mdQuantity; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkOrderId = 0;
        mnPkSubgoodsId = 0;
        mdQuantity = 0;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object key) {
        mnPkYearId = ((int[]) key)[0];
        mnPkOrderId = ((int[]) key)[1];
        mnPkSubgoodsId = ((int[]) key)[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkOrderId, mnPkSubgoodsId };
    }
    
    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        
        ResultSet resultSet = null;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = "SELECT o.* " +
                "FROM mfg_ord_sgds AS o " +
                "INNER JOIN mfg_sgds as s ON o.id_sgds = s.id_sgds " +
                "WHERE o.id_year = " + key[0] + " AND o.id_ord = " + key[1] + " AND o.id_sgds = " + key[2] + "; ";

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("o.id_year");
                mnPkOrderId = resultSet.getInt("o.id_ord");
                mnPkSubgoodsId = resultSet.getInt("o.id_sgds");
                mdQuantity = resultSet.getDouble("o.qty");
                mbIsDeleted = resultSet.getBoolean("o.b_del");
                mnFkUserNewId = resultSet.getInt("o.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("o.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("o.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("o.ts_new");
                mtUserEditTs = resultSet.getTimestamp("o.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("o.ts_del");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int i = 0;
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mfg_ord_sgds_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkOrderId);
            callableStatement.setInt(nParam++, mnPkSubgoodsId);
            callableStatement.setDouble(nParam++, mdQuantity);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

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

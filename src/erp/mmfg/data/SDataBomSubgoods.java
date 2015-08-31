/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.util.Date;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataBomSubgoods extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkSgdsId;
    protected java.util.Date mtDateStart;
    protected java.util.Date mtDateEnd_n;
    protected double mdQty;
    protected double mdCostPercentage;
    protected boolean mbIsDeleted;
    protected int mnFkBomId;
    protected int mnFkItem;
    protected int mnFkUnit;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsItemKey;
    protected java.lang.String msDbmsItem;
    protected java.lang.String msDbmsItemUnitSymbol;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;
    
    public SDataBomSubgoods() {
        super(SDataConstants.MFG_SGDS);
        reset();
    }
    
    public void setPkSgdsId(int n) { mnPkSgdsId = n; }
    public void setDateStart(java.util.Date t) { mtDateStart = t; }
    public void setDateEnd_n(java.util.Date t) { mtDateEnd_n = t; }
    public void setQty(double d) { mdQty = d; }
    public void setCostPercentage(double d) { mdCostPercentage = d; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkBomId(int n) { mnFkBomId = n; }
    public void setFkItem(int n) { mnFkItem = n; }
    public void setFkUnit(int n) { mnFkUnit = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }
    
    public int getPkSgdsId() { return mnPkSgdsId; }
    public java.util.Date getDateStart() { return mtDateStart; }
    public java.util.Date getDateEnd_n() { return mtDateEnd_n; }
    public double getQuantity() { return mdQty; }
    public double getCostPercentage() { return mdCostPercentage; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkBomId() { return mnFkBomId; }
    public int getFkItem() { return mnFkItem; }
    public int getFkUnit() { return mnFkUnit; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsItemKey(java.lang.String s) { msDbmsItemKey = s; }
    public void setDbmsItem(java.lang.String s) { msDbmsItem = s; }
    public void setDbmsUnitSymbol(java.lang.String s) { msDbmsItemUnitSymbol = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public java.lang.String getDbmsItemKey() { return msDbmsItemKey; }
    public java.lang.String getDbmsItem() { return msDbmsItem; }
    public java.lang.String getDbmsUnitSymbol() { return msDbmsItemUnitSymbol; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }
    
    @Override
    public void reset() {
        super.resetRegistry();

        mnPkSgdsId = 0;
        mtDateStart = null;
        mtDateEnd_n = null;
        mdQty = 0;
        mdCostPercentage = 0;
        mbIsDeleted = false;
        mnFkBomId = 0;
        mnFkItem = 0;
        mnFkUnit = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsItemKey = "";
        msDbmsItem = "";
        msDbmsItemUnitSymbol = "";
        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object key) {
        mnPkSgdsId = ((int[]) key)[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSgdsId };
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = "SELECT s.*, i.item_key, i.item, u.symbol, un.usr, ue.usr, ud.usr " +
                "FROM mfg_sgds as s " +
                "INNER JOIN erp.itmu_item AS i ON s.fid_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON s.fid_unit = u.id_unit " +
                "INNER JOIN erp.usru_usr AS un ON s.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON s.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON s.fid_usr_del = ud.id_usr " +
                "WHERE s.id_sgds = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkSgdsId = resultSet.getInt("s.id_sgds");
                mtDateStart = resultSet.getTimestamp("s.ts_start");
                mtDateEnd_n = resultSet.getTimestamp("s.ts_end_n");
                if (resultSet.wasNull()) mtDateEnd_n = null;
                mdQty = resultSet.getDouble("s.qty");
                mdCostPercentage = resultSet.getDouble("s.cost_per");
                mbIsDeleted = resultSet.getBoolean("s.b_del");
                mnFkBomId = resultSet.getInt("s.fid_bom");
                mnFkItem = resultSet.getInt("s.fid_item");
                mnFkUnit = resultSet.getInt("s.fid_unit");
                mnFkUserNewId = resultSet.getInt("s.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("s.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("s.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("s.ts_new");
                mtUserEditTs = resultSet.getTimestamp("s.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("s.ts_del");

                msDbmsItemKey = resultSet.getString("i.item_key");
                msDbmsItem = resultSet.getString("i.item");
                msDbmsItemUnitSymbol = resultSet.getString("u.symbol");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");

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
        java.sql.CallableStatement callableStatement;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL mfg_bom_sgds_save(" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?) }");
            callableStatement.setInt(nParam++, mnPkSgdsId);
            callableStatement.setTimestamp(nParam++, new java.sql.Timestamp(mtDateStart.getTime()));
            if (mtDateEnd_n != null) callableStatement.setDate(nParam++, new java.sql.Date(mtDateEnd_n.getTime())); else callableStatement.setNull(nParam++, java.sql.Types.DATE);
            callableStatement.setDouble(nParam++, mdQty);
            callableStatement.setDouble(nParam++, mdCostPercentage);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkBomId);
            callableStatement.setInt(nParam++, mnFkItem);
            callableStatement.setInt(nParam++, mnFkUnit);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
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
        return mtUserEditTs;
    }
}

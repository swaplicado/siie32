/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;

/**
 *
 * @author Sergio Flores
 */
public class SDataStockLot extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    public static final int LEN_LOT = 50;

    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnPkLotId;
    protected java.lang.String msLot;
    protected java.util.Date mtDateExpiration_n;
    protected boolean mbIsBlocked;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataStockLot() {
        super(SDataConstants.TRN_LOT);
        reset();
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkLotId(int n) { mnPkLotId = n; }
    public void setLot(java.lang.String s) { msLot = s; }
    public void setDateExpiration_n(java.util.Date t) { mtDateExpiration_n = t; }
    public void setIsBlocked(boolean b) { mbIsBlocked = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkLotId() { return mnPkLotId; }
    public java.lang.String getLot() { return msLot; }
    public java.util.Date getDateExpiration_n() { return mtDateExpiration_n; }
    public boolean getIsBlocked() { return mbIsBlocked; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkItemId = ((int[]) pk)[0];
        mnPkUnitId = ((int[]) pk)[1];
        mnPkLotId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkItemId, mnPkUnitId, mnPkLotId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkItemId = 0;
        mnPkUnitId = 0;
        mnPkLotId = 0;
        msLot = "";
        mtDateExpiration_n = null;
        mbIsBlocked = false;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_lot " +
                    "WHERE id_item = " + key[0] + " AND id_unit = " + key[1] + " AND id_lot = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkItemId = resultSet.getInt("id_item");
                mnPkUnitId = resultSet.getInt("id_unit");
                mnPkLotId = resultSet.getInt("id_lot");
                msLot = resultSet.getString("lot");
                mtDateExpiration_n = resultSet.getDate("dt_exp_n");
                mbIsBlocked = resultSet.getBoolean("b_block");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

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
            callableStatement = connection.prepareCall(
                    "{ CALL trn_lot_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?) }");
            callableStatement.setInt(nParam++, mnPkItemId);
            callableStatement.setInt(nParam++, mnPkUnitId);
            callableStatement.setInt(nParam++, mnPkLotId);
            callableStatement.setString(nParam++, msLot);
            if (mtDateExpiration_n != null) callableStatement.setDate(nParam++, new java.sql.Date(mtDateExpiration_n.getTime())); else callableStatement.setNull(nParam++, Types.DATE);
            callableStatement.setBoolean(nParam++, mbIsBlocked);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, Types.VARCHAR);
            callableStatement.execute();

            mnPkLotId = callableStatement.getInt(nParam - 3);
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
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}

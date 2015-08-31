/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.util.Date;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataDiogEntryLot extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEtyId;
    protected int mnPkLotId;
    protected java.lang.String msLot;
    protected double mdQuantity;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;

    public SDataDiogEntryLot() {
        //super(SDataConstants.TRN_DIOG_ETY_LOT); XXX
        super(0);
        reset();
    }
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEtyId = n; }
    public void setPkLotId(int n) { mnPkLotId = n; }
    public void setLot(java.lang.String s) { msLot = s; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEtyId; }
    public int getPkLotId() { return mnPkLotId; }
    public java.lang.String getLot() { return msLot; }
    public double getQuantity() { return mdQuantity; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEtyId = 0;
        mnPkLotId = 0;
        msLot = "";
        mdQuantity = 0;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkEtyId = ((int[]) pk)[2];
        mnPkLotId = ((int[]) pk)[3];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEtyId, mnPkLotId };
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT e.*, un.usr, ue.usr, ud.usr " +
                "FROM trn_diog_ety_lot AS e " +
                "INNER JOIN erp.usru_usr AS un ON e.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON e.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON e.fid_usr_del = ud.id_usr " +
                "WHERE e.id_year = " + key[0] + " AND e.id_doc = " + key[1] + " AND e.id_ety = " + key[2] + " AND e.id_lot = " + key[3] ;
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("e.id_year");
                mnPkDocId = resultSet.getInt("e.id_doc");
                mnPkEtyId = resultSet.getInt("e.id_ety");
                mnPkLotId = resultSet.getInt("e.id_lot");
                msLot = resultSet.getString("e.lot");
                mdQuantity = resultSet.getDouble("e.qty");
                mbIsDeleted = resultSet.getBoolean("e.b_del");
                mnFkUserNewId = resultSet.getInt("e.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("e.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("e.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("e.ts_new");
                mtUserEditTs = resultSet.getTimestamp("e.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("e.ts_del");

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
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL trn_diog_ety_lot_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkDocId);
            callableStatement.setInt(nParam++, mnPkEtyId);
            callableStatement.setInt(nParam++, mnPkLotId);
            callableStatement.setString(nParam++, msLot);
            callableStatement.setDouble(nParam++, mdQuantity);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkUserEditId == 0 ? mnFkUserNewId : mnFkUserEditId);
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.util.Date;
import java.util.Vector;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mtrn.data.SDataStockLot;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataProductionOrderChargeEntryLot extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkOrderId;
    protected int mnPkChargeId;
    protected int mnPkEntryId;
    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnPkLotId;
    protected double mdQuantity;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.util.Date mtDbmsDateExpiration_n;
    protected java.lang.String msDbmsLot;
    protected java.lang.String msDbmsItem;
    protected java.lang.String msDbmsItemKey;
    protected java.lang.String msDbmsUnit;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;

    protected java.util.Vector<erp.mtrn.data.SDataStockLot> mvDbmsStockLot;

    public SDataProductionOrderChargeEntryLot() {
        super(SDataConstants.MFG_ORD_CHG_ETY_LOT);

        mvDbmsStockLot = new Vector<SDataStockLot>();
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkOrderId(int n) { mnPkOrderId = n; }
    public void setPkChargeId(int n) { mnPkChargeId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkLotId(int n) { mnPkLotId = n; }
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
    public int getPkChargeId() { return mnPkChargeId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkLotId() { return mnPkLotId; }
    public double getQuantity() { return mdQuantity; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsLot(java.lang.String s) { msDbmsLot = s; }
    public void setDbmsDateExpiration_n(java.util.Date t) { mtDbmsDateExpiration_n = t; }
    public void setDbmsItem(java.lang.String s) { msDbmsItem = s; }
    public void setDbmsItemKey(java.lang.String s) { msDbmsItemKey = s; }
    public void setDbmsUnit(java.lang.String s) { msDbmsUnit = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public java.util.Date getDbmsDateExpiration_n() { return mtDbmsDateExpiration_n; }
    public java.lang.String getDbmsLot() { return msDbmsLot; }
    public java.lang.String getDbmsItem() { return msDbmsItem; }
    public java.lang.String getDbmsItemKey() { return msDbmsItemKey; }
    public java.lang.String getDbmsUnit() { return msDbmsUnit; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }

    public java.util.Vector<erp.mtrn.data.SDataStockLot> getDbmsStockLot() { return mvDbmsStockLot; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkOrderId = ((int[]) pk)[1];
        mnPkChargeId = ((int[]) pk)[2];
        mnPkEntryId = ((int[]) pk)[3];
        mnPkItemId = ((int[]) pk)[4];
        mnPkUnitId = ((int[]) pk)[5];
        mnPkLotId = ((int[]) pk)[6];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkOrderId, mnPkChargeId, mnPkEntryId, mnPkItemId, mnPkUnitId, mnPkLotId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkOrderId = 0;
        mnPkChargeId = 0;
        mnPkEntryId = 0;
        mnPkItemId = 0;
        mnPkUnitId = 0;
        mnPkLotId = 0;
        mdQuantity = 0;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mtDbmsDateExpiration_n = null;
        msDbmsLot = "";
        msDbmsItem = "";
        msDbmsItemKey = "";
        msDbmsUnit = "";
        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";
        mvDbmsStockLot.clear();
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;

        ResultSet resultSet = null;
        Statement statementAux = null;

        SDataStockLot oStockLot = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT c.*, i.item, i.item_key, u.symbol, un.usr, ue.usr, ud.usr, l.lot, l.dt_exp_n " +
                    "FROM mfg_ord_chg_ety_lot AS c " +
                    "INNER JOIN trn_lot AS l ON c.id_item = l.id_item AND c.id_unit = l.id_unit AND c.id_lot = l.id_lot " +
                    "INNER JOIN erp.itmu_item AS i ON c.id_item = i.id_item " +
                    "INNER JOIN erp.itmu_unit AS u ON c.id_unit = u.id_unit " +
                    "INNER JOIN erp.usru_usr AS un ON c.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON c.fid_usr_edit = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON c.fid_usr_del = ud.id_usr " +
                    "WHERE c.id_year = " + key[0] + " AND c.id_ord = " + key[1] + " AND c.id_chg = " + key[2] + " AND c.id_ety = " + key[3] +
                    " AND c.id_item = " + key[4] + " AND c.id_unit = " + key[5] + " AND c.id_lot =  " + key[6];
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("c.id_year");
                mnPkOrderId = resultSet.getInt("c.id_ord");
                mnPkChargeId = resultSet.getInt("c.id_chg");
                mnPkEntryId = resultSet.getInt("c.id_ety");
                mnPkItemId = resultSet.getInt("c.id_item");
                mnPkUnitId = resultSet.getInt("c.id_unit");
                mnPkLotId = resultSet.getInt("c.id_lot");
                mdQuantity = resultSet.getDouble("c.qty");
                mbIsDeleted = resultSet.getBoolean("c.b_del");
                mnFkUserNewId = resultSet.getInt("c.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("c.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("c.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("c.ts_new");
                mtUserEditTs = resultSet.getTimestamp("c.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("c.ts_del");

                msDbmsLot = resultSet.getString("l.lot");
                mtDbmsDateExpiration_n = resultSet.getDate("l.dt_exp_n");
                msDbmsItem = resultSet.getString("i.item");
                msDbmsItemKey = resultSet.getString("i.item_key");
                msDbmsUnit = resultSet.getString("u.symbol" );
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");

                // Read lot from 'trn_lot':

                statementAux = statement.getConnection().createStatement();
                mvDbmsStockLot.removeAllElements();
                oStockLot = new SDataStockLot();
                if (oStockLot.read(new int[] { resultSet.getInt("c.id_item"), resultSet.getInt("c.id_unit"), resultSet.getInt("c.id_lot") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    mvDbmsStockLot.add(oStockLot);
                }
                
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

        SDataStockLot oStockLot = null;

        try {
            // Save lot entry:
            
            if (mnPkLotId == 0) {

                // Create lot record:

                oStockLot = new SDataStockLot();
                oStockLot.setPkItemId(mnPkItemId);
                oStockLot.setPkUnitId(mnPkUnitId);
                oStockLot.setPkLotId(mnPkLotId);
                oStockLot.setLot(msDbmsLot);
                oStockLot.setDateExpiration_n(mtDbmsDateExpiration_n);
                oStockLot.setIsBlocked(false);
                oStockLot.setIsDeleted(false);
                oStockLot.setFkUserNewId(mnFkUserNewId);
                oStockLot.setFkUserEditId(mnFkUserEditId);
                oStockLot.setFkUserDeleteId(1);

                if (oStockLot.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
            }

            callableStatement = connection.prepareCall(
                "{ CALL mfg_ord_chg_ety_lot_save(" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?) }");            
            callableStatement.setInt(nParam++, mnPkYearId);            
            callableStatement.setInt(nParam++, mnPkOrderId);            
            callableStatement.setInt(nParam++, mnPkChargeId);            
            callableStatement.setInt(nParam++, mnPkEntryId);            
            callableStatement.setInt(nParam++, mnPkItemId);
            callableStatement.setInt(nParam++, mnPkUnitId);            
            callableStatement.setInt(nParam++, mnPkLotId);            
            callableStatement.setDouble(nParam++, mdQuantity);            
            callableStatement.setBoolean(nParam++, mbIsDeleted);            
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
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
        return mtUserEditTs;
    }
}

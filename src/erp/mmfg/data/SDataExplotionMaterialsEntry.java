/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.util.Date;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataExplotionMaterialsEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkExpId;
    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected double mdGrossRequirement;
    protected double mdSafetyStock;
    protected double mdAvailable;
    protected double mdBackorder;
    protected double mdNet;
    protected java.util.Date mtDeliveryTs;
    protected java.util.Date mtLtimeTs;
    protected int mnFkCobId;
    protected int mnFkCobId_n;
    protected int mnFkWarehouseId_n;
    protected int mnFkBizPartnerId_n;

    protected int mnDbmsAuxSortingItem;
    protected java.lang.String msDbmsBizPartner;
    protected java.lang.String msDbmsItemKey;
    protected java.lang.String msDbmsItem;
    protected java.lang.String msDbmsItemUnitSymbol;

    public SDataExplotionMaterialsEntry() {
        super(SDataConstants.MFG_EXP_ETY);
        reset();        
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkExpId(int n) { mnPkExpId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setGrossReq(double d) { mdGrossRequirement = d; }
    public void setSafetyStock(double d) { mdSafetyStock = d; }
    public void setAvailable(double d) { mdAvailable = d; }
    public void setBackorder(double d) { mdBackorder = d; }
    public void setNet(double d) { mdNet = d; }
    public void setDeliveryTs(java.util.Date t) { mtDeliveryTs = t; }
    public void setLtimeTs_n(java.util.Date t) { mtLtimeTs = t; }
    public void setFkCobId(int n) { mnFkCobId = n; }
    public void setFkCobId_n(int n) { mnFkCobId_n = n; }
    public void setFkWarehouseId_n(int n) { mnFkWarehouseId_n = n; }
    public void setFkBizPartnerId_n(int n) { mnFkBizPartnerId_n = n; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkExpId() { return mnPkExpId; }
    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public double getGrossReq() { return mdGrossRequirement; }
    public double getSafetyStock() { return mdSafetyStock; }
    public double getAvailable() { return mdAvailable; }
    public double getBackorder() { return mdBackorder; }
    public double getNet() { return mdNet; }
    public java.util.Date getDeliveryTs() { return mtDeliveryTs; }
    public java.util.Date getLtimeTs() { return mtLtimeTs; }
    public int getFkCobId() { return mnFkCobId; }
    public int getFkCobId_n() { return mnFkCobId_n; }
    public int getFkWarehouseId_n() { return mnFkWarehouseId_n; }
    public int getFkBizPartnerId_n() { return mnFkBizPartnerId_n; }

    public void setDbmsBizPartner(java.lang.String s) { msDbmsBizPartner = s; }
    public void setDbmsItemKey(java.lang.String s) { msDbmsItemKey = s; }
    public void setDbmsItem(java.lang.String s) { msDbmsItem = s; }
    public void setDbmsItemUnitSymbol(java.lang.String s) { msDbmsItemUnitSymbol = s; }
    public void setDbmsAuxSortingItem(int n) { mnDbmsAuxSortingItem = n; }
    
    public java.lang.String getDbmsBizPartner() { return msDbmsBizPartner; }
    public java.lang.String getDbmsItemKey() { return msDbmsItemKey; }
    public java.lang.String getDbmsItem() { return msDbmsItem; }
    public java.lang.String getDbmsItemUnitSymbol() { return msDbmsItemUnitSymbol; }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkExpId = 0;
        mnPkItemId = 0;
        mnPkUnitId = 0;
        mdGrossRequirement = 0;
        mdSafetyStock = 0;
        mdAvailable = 0;
        mdBackorder = 0;
        mdNet = 0;
        mtDeliveryTs = null;
        mtLtimeTs = null;
        mnFkCobId = 0;
        mnFkCobId_n = 0;
        mnFkWarehouseId_n = 0;
        mnFkBizPartnerId_n = 0;

        mnDbmsAuxSortingItem = 0;
        msDbmsBizPartner = "";
        msDbmsItemKey = "";
        msDbmsItem = "";
        msDbmsItemUnitSymbol = "";
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object key) {
        mnPkYearId = ((int[]) key)[0];
        mnPkExpId = ((int[]) key)[1];
        mnPkItemId = ((int[]) key)[2];
        mnPkUnitId = ((int[]) key)[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkExpId, mnPkItemId, mnPkUnitId };
    }
    
    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";

        ResultSet resultSet = null;
        Statement statementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = "SELECT e.*, i.item_key, i.item, b.bp, u.symbol " +
                 "FROM mfg_exp_ety AS e " +
                "INNER JOIN erp.itmu_item AS i ON e.id_item = i.id_item " +
                "INNER JOIN erp.itmu_unit as u ON e.id_unit = u.id_unit " +
                "LEFT JOIN erp.bpsu_bp AS b ON e.fid_bp_n = b.id_bp " +
                "WHERE e.id_year = " + key[0] + " AND e.id_exp = " + key[1] + " AND e.id_item = " + key[2] + " AND e.id_unit = " + key[3] + " " +
                "ORDER BY " + (mnDbmsAuxSortingItem == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "i.item_key, i.item" : "i.item, i.item_key");

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("e.id_year");
                mnPkExpId = resultSet.getInt("e.id_exp");
                mnPkItemId = resultSet.getInt("e.id_item");
                mnPkUnitId = resultSet.getInt("e.id_unit");
                mdGrossRequirement = resultSet.getDouble("e.gross_req");
                mdSafetyStock = resultSet.getDouble("e.sfty_stck");
                mdAvailable = resultSet.getDouble("e.avlb");
                mdBackorder = resultSet.getDouble("e.backorder");
                mdNet = resultSet.getDouble("e.net");
                mtDeliveryTs = resultSet.getDate("e.ts_deliver");
                mtLtimeTs = resultSet.getDate("e.ts_ltime_n");
                mnFkCobId = resultSet.getInt("e.fid_cob");
                mnFkCobId_n = resultSet.getInt("e.fid_cob_n");
                mnFkWarehouseId_n = resultSet.getInt("e.fid_wh_n");
                mnFkBizPartnerId_n = resultSet.getInt("e.fid_bp_n");

                msDbmsBizPartner = resultSet.getString("b.bp");
                msDbmsItemKey = resultSet.getString("item_key");
                msDbmsItem = resultSet.getString("item");
                msDbmsItemUnitSymbol = resultSet.getString("u.symbol");
                
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
        String sql = "";

        CallableStatement callableStatement = null;
        Statement statementAux = null;

        /* XXX: Change form lot by new form lot general
        SDataExplotionMaterialsEntryLot oDataExplotionMaterialsEntryLot = null;
         */

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mfg_exp_ety_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkExpId);
            callableStatement.setInt(nParam++, mnPkItemId);
            callableStatement.setInt(nParam++, mnPkUnitId);
            callableStatement.setDouble(nParam++, mdGrossRequirement);
            callableStatement.setDouble(nParam++, mdSafetyStock);
            callableStatement.setDouble(nParam++, mdAvailable);
            callableStatement.setDouble(nParam++, mdBackorder);
            callableStatement.setDouble(nParam++, mdNet);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDeliveryTs.getTime()));
            callableStatement.setDate(nParam++, mtLtimeTs != null ? new java.sql.Date(mtLtimeTs.getTime()) : null);
            callableStatement.setInt(nParam++, mnFkCobId);
            if (mnFkCobId_n > 0) callableStatement.setInt(nParam++, mnFkCobId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkWarehouseId_n > 0) callableStatement.setInt(nParam++, mnFkWarehouseId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkBizPartnerId_n > 0) callableStatement.setInt(nParam++, mnFkBizPartnerId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
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
    public Date getLastDbUpdate() {
        return null;
    }
}

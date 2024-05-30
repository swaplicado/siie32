/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mfin.data.SDataCostCenter;
import erp.mod.SModSysConsts;
import erp.mod.trn.db.SMatConsumeSubEntCcConfig;
import erp.mod.trn.db.SMaterialRequestUtils;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Vector;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores, Claudio Peña, Edwin Carmona, Isabel Servín
 */
public class SDataDiogEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected double mdQuantity;
    protected double mdValueUnitary;
    protected double mdValue;
    protected double mdOriginalQuantity;
    protected double mdOriginalValueUnitary;
    protected int mnSortingPosition;
    protected boolean mbIsInventoriable;
    protected boolean mbIsDeleted;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkOriginalUnitId;
    protected int mnFkDpsYearId_n;
    protected int mnFkDpsDocId_n;
    protected int mnFkDpsEntryId_n;
    protected int mnFkDpsAdjustmentYearId_n;
    protected int mnFkDpsAdjustmentDocId_n;
    protected int mnFkDpsAdjustmentEntryId_n;
    protected int mnFkMfgYearId_n;
    protected int mnFkMfgOrderId_n;
    protected int mnFkMfgChargeId_n;
    protected int mnFkMatRequestId_n;
    protected int mnFkMatRequestEtyId_n;
    protected int mnFkMaintAreaId;
    protected int mnFkCostCenterId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected int mnDbmsBookkeepingYearId;
    protected int mnDbmsBookkeepingNumberId;
    protected java.lang.String msDbmsItem;
    protected java.lang.String msDbmsItemKey;
    protected java.lang.String msDbmsUnit;
    protected java.lang.String msDbmsUnitSymbol;
    protected java.lang.String msDbmsOriginalUnit;
    protected java.lang.String msDbmsOriginalUnitSymbol;
    protected java.lang.String msDbmsMaintArea;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;
    
    protected SDataCostCenter moDbmsCostCenter;

    protected java.util.Vector<erp.mtrn.data.STrnStockMove> mvAuxStockMoves;
    protected ArrayList<SDataDiogEtyMatConsEntCostCenter> mlAuxDiogEtyMatEntCcsConfigs;

    protected int mnClonedFkMfgYearId_n;
    protected int mnClonedFkMfgOrderId_n;
    protected int mnClonedFkMfgChargeId_n;
    
    protected boolean mbHasLinksShipment;

    public SDataDiogEntry() {
        super(SDataConstants.TRN_DIOG_ETY);
        mvAuxStockMoves = new Vector<>();
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setValueUnitary(double d) { mdValueUnitary = d; }
    public void setValue(double d) { mdValue = d; }
    public void setOriginalQuantity(double d) { mdOriginalQuantity = d; }
    public void setOriginalValueUnitary(double d) { mdOriginalValueUnitary = d; }
    public void setSortingPosition(int n) { mnSortingPosition = n; }
    public void setIsInventoriable(boolean b) { mbIsInventoriable = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkOriginalUnitId(int n) { mnFkOriginalUnitId = n; }
    public void setFkDpsYearId_n(int n) { mnFkDpsYearId_n = n; }
    public void setFkDpsDocId_n(int n) { mnFkDpsDocId_n = n; }
    public void setFkDpsEntryId_n(int n) { mnFkDpsEntryId_n = n; }
    public void setFkDpsAdjustmentYearId_n(int n) { mnFkDpsAdjustmentYearId_n = n; }
    public void setFkDpsAdjustmentDocId_n(int n) { mnFkDpsAdjustmentDocId_n = n; }
    public void setFkDpsAdjustmentEntryId_n(int n) { mnFkDpsAdjustmentEntryId_n = n; }
    public void setFkMfgYearId_n(int n) { mnFkMfgYearId_n = n; }
    public void setFkMfgOrderId_n(int n) { mnFkMfgOrderId_n = n; }
    public void setFkMfgChargeId_n(int n) { mnFkMfgChargeId_n = n; }
    public void setFkMatRequestId_n(int n) { mnFkMatRequestId_n = n; }
    public void setFkMatRequestEtyId_n(int n) { mnFkMatRequestEtyId_n = n; }
    public void setFkMaintAreaId(int n) { mnFkMaintAreaId = n; }
    public void setFkCostCenterId(int n) { mnFkCostCenterId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public double getQuantity() { return mdQuantity; }
    public double getValueUnitary() { return mdValueUnitary; }
    public double getValue() { return mdValue; }
    public double getOriginalQuantity() { return mdOriginalQuantity; }
    public double getOriginalValueUnitary() { return mdOriginalValueUnitary; }
    public int getSortingPosition() { return mnSortingPosition; }
    public boolean getIsInventoriable() { return mbIsInventoriable; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkOriginalUnitId() { return mnFkOriginalUnitId; }
    public int getFkDpsYearId_n() { return mnFkDpsYearId_n; }
    public int getFkDpsDocId_n() { return mnFkDpsDocId_n; }
    public int getFkDpsEntryId_n() { return mnFkDpsEntryId_n; }
    public int getFkDpsAdjustmentYearId_n() { return mnFkDpsAdjustmentYearId_n; }
    public int getFkDpsAdjustmentDocId_n() { return mnFkDpsAdjustmentDocId_n; }
    public int getFkDpsAdjustmentEntryId_n() { return mnFkDpsAdjustmentEntryId_n; }
    public int getFkMfgYearId_n() { return mnFkMfgYearId_n; }
    public int getFkMfgOrderId_n() { return mnFkMfgOrderId_n; }
    public int getFkMfgChargeId_n() { return mnFkMfgChargeId_n; }
    public int getFkMatRequestId_n() { return mnFkMatRequestId_n; }
    public int getFkMatRequestEtyId_n() { return mnFkMatRequestEtyId_n; }
    public int getFkMaintAreaId() { return mnFkMaintAreaId; }
    public int getFkCostCenterId() { return mnFkCostCenterId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsBookkeepingYearId(int n) { mnDbmsBookkeepingYearId = n; }
    public void setDbmsBookkeepingNumberId(int n) { mnDbmsBookkeepingNumberId = n; }
    public void setDbmsItem(java.lang.String s) { msDbmsItem = s; }
    public void setDbmsItemKey(java.lang.String s) { msDbmsItemKey = s; }
    public void setDbmsUnit(java.lang.String s) { msDbmsUnit = s; }
    public void setDbmsUnitSymbol(java.lang.String s) { msDbmsUnitSymbol = s; }
    public void setDbmsOriginalUnit(java.lang.String s) { msDbmsOriginalUnit = s; }
    public void setDbmsOriginalUnitSymbol(java.lang.String s) { msDbmsOriginalUnitSymbol = s; }
    public void setDbmsMaintArea(java.lang.String s) { msDbmsMaintArea = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }
    
    public void setDbmsCostCenter(SDataCostCenter o) { moDbmsCostCenter = o; }

    public int getDbmsBookkeepingYearId() { return mnDbmsBookkeepingYearId; }
    public int getDbmsBookkeepingNumberId() { return mnDbmsBookkeepingNumberId; }
    public java.lang.String getDbmsItem() { return msDbmsItem; }
    public java.lang.String getDbmsItemKey() { return msDbmsItemKey; }
    public java.lang.String getDbmsUnit() { return msDbmsUnit; }
    public java.lang.String getDbmsUnitSymbol() { return msDbmsUnitSymbol; }
    public java.lang.String getDbmsOriginalUnit() { return msDbmsOriginalUnit; }
    public java.lang.String getDbmsOriginalUnitSymbol() { return msDbmsOriginalUnitSymbol; }
    public java.lang.String getDbmsMaintArea() { return msDbmsMaintArea; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }
    
    public SDataCostCenter getDbmsCostCenter() { return moDbmsCostCenter; }

    public java.util.Vector<erp.mtrn.data.STrnStockMove> getAuxStockMoves() { return mvAuxStockMoves; }
    public ArrayList<SDataDiogEtyMatConsEntCostCenter> getAuxDiogEtyMatEntCcsConfigs() { return mlAuxDiogEtyMatEntCcsConfigs; }

    public void setClonedFkMfgYearId_n(int n) { mnClonedFkMfgYearId_n = n; }
    public void setClonedFkMfgOrderId_n(int n) { mnClonedFkMfgOrderId_n = n; }
    public void setClonedFkMfgChargeId_n(int n) { mnClonedFkMfgChargeId_n = n; }

    public int getClonedFkMfgYearId_n() { return mnClonedFkMfgYearId_n; }
    public int getClonedFkMfgOrderId_n() { return mnClonedFkMfgOrderId_n; }
    public int getClonedFkMfgChargeId_n() { return mnClonedFkMfgChargeId_n; }
    
    public boolean hasDiogLinksShipment() { return mbHasLinksShipment; }

    public int[] getDiogKey() { return new int[] { mnPkYearId, mnPkDocId }; }
    public int[] getLinkedDpsEntryKey_n() { return mnFkDpsYearId_n == SLibConstants.UNDEFINED || mnFkDpsDocId_n == SLibConstants.UNDEFINED || mnFkDpsEntryId_n == SLibConstants.UNDEFINED ? null : new int[] { mnFkDpsYearId_n, mnFkDpsDocId_n, mnFkDpsEntryId_n }; }
    public int[] getLinkedDpsAdjustmentEntryKey_n() { return mnFkDpsAdjustmentYearId_n == SLibConstants.UNDEFINED || mnFkDpsAdjustmentDocId_n == SLibConstants.UNDEFINED || mnFkDpsAdjustmentEntryId_n == SLibConstants.UNDEFINED ? null : new int[] { mnFkDpsAdjustmentYearId_n, mnFkDpsAdjustmentDocId_n, mnFkDpsAdjustmentEntryId_n }; }
    public int[] getProdOrderKey_n() { return mnFkMfgYearId_n == SLibConstants.UNDEFINED || mnFkMfgOrderId_n == SLibConstants.UNDEFINED ? null : new int[] { mnFkMfgYearId_n, mnFkMfgOrderId_n }; }
    public int[] getProdOrderChargeKey_n() { return new int[] { mnFkMfgYearId_n == SLibConstants.UNDEFINED || mnFkMfgOrderId_n == SLibConstants.UNDEFINED || mnFkMfgChargeId_n == SLibConstants.UNDEFINED ? null : mnFkMfgYearId_n, mnFkMfgOrderId_n, mnFkMfgChargeId_n }; }

    public java.lang.String getAuxStockMovesAsString() {
        String stockMoves = "";

        for (STrnStockMove stockMove : mvAuxStockMoves) {
            stockMoves += (stockMoves.length() == 0 ? "" : "; ") + stockMove.getAuxLot();
        }

        return stockMoves;
    }
    
    public String getConsumeCenterAsString() {
        if (mlAuxDiogEtyMatEntCcsConfigs != null && mlAuxDiogEtyMatEntCcsConfigs.size() > 0) {
            String text = "";
            for (SDataDiogEtyMatConsEntCostCenter oCcCfg : mlAuxDiogEtyMatEntCcsConfigs) {
                text += oCcCfg.getAuxEntConsumeName() + " / ";
            }
            
            return text.substring(0, text.length() - 3); 
        }
        
        return "";
    }
    
    public String getSubConsumeCenterAsString() {
        if (mlAuxDiogEtyMatEntCcsConfigs != null && mlAuxDiogEtyMatEntCcsConfigs.size() > 0) {
            String text = "";
            for (SDataDiogEtyMatConsEntCostCenter oCcCfg : mlAuxDiogEtyMatEntCcsConfigs) {
                text += oCcCfg.getAuxSubEntConsumeName()+ " / ";
            }
            
            return text.substring(0, text.length() - 3); 
        }
        
        return "";
    }
    
    public String getCostCenterAsString() {
        if (mlAuxDiogEtyMatEntCcsConfigs != null && mlAuxDiogEtyMatEntCcsConfigs.size() > 0) {
            String text = "";
            for (SDataDiogEtyMatConsEntCostCenter oCcCfg : mlAuxDiogEtyMatEntCcsConfigs) {
                text += oCcCfg.getAuxCostCenterName()+ " / ";
            }
            
            return text.substring(0, text.length() - 3); 
        }
        
        return "";
    }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkEntryId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        mdQuantity = 0;
        mdValueUnitary = 0;
        mdValue = 0;
        mdOriginalQuantity = 0;
        mdOriginalValueUnitary = 0;
        mnSortingPosition = 0;
        mbIsInventoriable = false;
        mbIsDeleted = false;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkOriginalUnitId = 0;
        mnFkDpsYearId_n = 0;
        mnFkDpsDocId_n = 0;
        mnFkDpsEntryId_n = 0;
        mnFkDpsAdjustmentYearId_n = 0;
        mnFkDpsAdjustmentDocId_n = 0;
        mnFkDpsAdjustmentEntryId_n = 0;
        mnFkMfgYearId_n = 0;
        mnFkMfgOrderId_n = 0;
        mnFkMfgChargeId_n = 0;
        mnFkMatRequestId_n = 0;
        mnFkMatRequestEtyId_n = 0;
        mnFkMaintAreaId = SModSysConsts.TRN_MAINT_AREA_NA;              // default value set only for preventing bugs
        mnFkCostCenterId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mnDbmsBookkeepingYearId = 0;
        mnDbmsBookkeepingNumberId = 0;
        msDbmsItem = "";
        msDbmsItemKey = "";
        msDbmsUnit = "";
        msDbmsUnitSymbol = "";
        msDbmsOriginalUnit = "";
        msDbmsOriginalUnitSymbol = "";
        msDbmsMaintArea = "";
        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";
        
        moDbmsCostCenter = null;

        mvAuxStockMoves.clear();
        mlAuxDiogEtyMatEntCcsConfigs = new ArrayList<>();

        mnClonedFkMfgYearId_n = 0;
        mnClonedFkMfgOrderId_n = 0;
        mnClonedFkMfgChargeId_n = 0;
        
        mbHasLinksShipment = false;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT de.*, d.fid_bkk_year_n, d.fid_bkk_num_n, " +
                    "i.item, i.item_key, i.part_num, u.unit, u.symbol, uo.unit, " +
                        "uo.symbol, ma.name, un.usr, ue.usr, ud.usr " +
                    "FROM trn_diog_ety AS de " +
                    "INNER JOIN trn_diog AS d ON de.id_year = d.id_year AND de.id_doc = d.id_doc " +
                    "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item " +
                    "INNER JOIN erp.itmu_unit AS u ON de.fid_unit = u.id_unit " +
                    "INNER JOIN erp.itmu_unit AS uo ON de.fid_orig_unit = uo.id_unit " +
                    "INNER JOIN trn_maint_area AS ma ON de.fid_maint_area = ma.id_maint_area " +
                    "INNER JOIN erp.usru_usr AS un ON de.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON de.fid_usr_edit = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON de.fid_usr_del = ud.id_usr " +
                    "WHERE de.id_year = " + key[0] + " AND de.id_doc = " + key[1] + " AND de.id_ety = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("de.id_year");
                mnPkDocId = resultSet.getInt("de.id_doc");
                mnPkEntryId = resultSet.getInt("de.id_ety");
                mdQuantity = resultSet.getDouble("de.qty");
                mdValueUnitary = resultSet.getDouble("de.val_u");
                mdValue = resultSet.getDouble("de.val");
                mdOriginalQuantity = resultSet.getDouble("de.orig_qty");
                mdOriginalValueUnitary = resultSet.getDouble("de.orig_val_u");
                mnSortingPosition = resultSet.getInt("de.sort_pos");
                mbIsInventoriable = resultSet.getBoolean("de.b_inv");
                mbIsDeleted = resultSet.getBoolean("de.b_del");
                mnFkItemId = resultSet.getInt("de.fid_item");
                mnFkUnitId = resultSet.getInt("de.fid_unit");
                mnFkOriginalUnitId = resultSet.getInt("de.fid_orig_unit");
                mnFkDpsYearId_n = resultSet.getInt("de.fid_dps_year_n");
                mnFkDpsDocId_n = resultSet.getInt("de.fid_dps_doc_n");
                mnFkDpsEntryId_n = resultSet.getInt("de.fid_dps_ety_n");
                mnFkDpsAdjustmentYearId_n = resultSet.getInt("de.fid_dps_adj_year_n");
                mnFkDpsAdjustmentDocId_n = resultSet.getInt("de.fid_dps_adj_doc_n");
                mnFkDpsAdjustmentEntryId_n = resultSet.getInt("de.fid_dps_adj_ety_n");
                mnFkMfgYearId_n = resultSet.getInt("de.fid_mfg_year_n");
                mnFkMfgOrderId_n = resultSet.getInt("de.fid_mfg_ord_n");
                mnFkMfgChargeId_n = resultSet.getInt("de.fid_mfg_chg_n");
                mnFkMatRequestId_n = resultSet.getInt("de.fid_mat_req_n");
                mnFkMatRequestEtyId_n = resultSet.getInt("de.fid_mat_req_ety_n");
                mnFkMaintAreaId = resultSet.getInt("de.fid_maint_area");
                mnFkCostCenterId = resultSet.getInt("fid_cc");
                mnFkUserNewId = resultSet.getInt("de.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("de.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("de.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("de.ts_new");
                mtUserEditTs = resultSet.getTimestamp("de.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("de.ts_del");

                mnDbmsBookkeepingYearId = resultSet.getInt("d.fid_bkk_year_n");
                mnDbmsBookkeepingNumberId = resultSet.getInt("d.fid_bkk_num_n");
                msDbmsItem = resultSet.getString("i.item");
                msDbmsItem = resultSet.getString("i.item");
                msDbmsItemKey = resultSet.getString("i.item_key");
                msDbmsUnit = resultSet.getString("u.unit");
                msDbmsUnitSymbol = resultSet.getString("u.symbol");
                msDbmsOriginalUnit = resultSet.getString("uo.unit");
                msDbmsOriginalUnitSymbol = resultSet.getString("uo.symbol");
                msDbmsMaintArea = resultSet.getString("ma.name");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");

                /*
                 * Read aswell stock moves:
                 * (Note that when document is deleted, stock moves are also deleted, so they can be read by bookkeeping number.)
                 */

                sql = "SELECT s.id_year, s.id_item, s.id_unit, s.id_lot, s.id_cob, s.id_wh, " +
                        "s.mov_in, s.mov_out, l.lot, l.dt_exp_n " +
                        "FROM trn_stk AS s " +
                        "INNER JOIN trn_lot AS l ON s.id_item = l.id_item AND s.id_unit = l.id_unit AND s.id_lot = l.id_lot " +
                        "WHERE s.fid_diog_year = " + key[0] + " AND s.fid_diog_doc = " + key[1] + " AND s.fid_diog_ety = " + key[2] + " AND " +
                        "(s.b_del = 0 OR (s.fid_bkk_year_n = " + mnDbmsBookkeepingYearId + " AND s.fid_bkk_num_n = " + mnDbmsBookkeepingNumberId + ")) ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    STrnStockMove move = new STrnStockMove(new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getInt(5), resultSet.getInt(6) },
                            resultSet.getDouble("s.mov_in") + resultSet.getDouble("s.mov_out"));
                    move.setAuxLot(resultSet.getString("l.lot"));
                    move.setAuxLotDateExpiration(resultSet.getDate("l.dt_exp_n"));
                    mvAuxStockMoves.add(move);
                }
                
                sql = "SELECT COUNT(*) AS f_count " +
                        "FROM log_ship AS s " +
                        "INNER JOIN log_ship_dest AS sd ON s.id_ship = sd.id_ship " +
                        "INNER JOIN log_ship_dest_ety AS sde ON sd.id_ship = sde.id_ship AND sd.id_dest = sde.id_dest " +
                        "WHERE s.b_del = 0 AND sde.fk_diog_year_n = " + mnPkYearId + " AND sde.fk_diog_doc_n = " + mnPkDocId + " AND sde.fk_diog_ety_n = " + mnPkEntryId + " ";
               
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    mbHasLinksShipment = resultSet.getInt("f_count") > 0;
                }
                
                String ccsQuery = "SELECT id_diog_ety_ce_cc FROM trn_diog_ety_cons_ent_cc "
                + "WHERE fid_diog_doc = " + mnFkDpsYearId_n + " "
                + "AND fid_diog_year = " + mnFkDpsDocId_n + " "
                + "AND fid_diog_ety = " + mnFkDpsEntryId_n + " ";
                ResultSet res = statement.getConnection().createStatement().executeQuery(ccsQuery);
                while (res.next()) {
                    SDataDiogEtyMatConsEntCostCenter oConfig = new SDataDiogEtyMatConsEntCostCenter();
                    oConfig.read(new int[] { res.getInt("id_diog_ety_ce_cc") }, statement);
                    
                    mlAuxDiogEtyMatEntCcsConfigs.add(oConfig);
                }
                
                sql = "SELECT id_cc FROM fin_cc WHERE pk_cc = " + mnFkCostCenterId;
                res = statement.getConnection().createStatement().executeQuery(sql);
                if (res.next()) {
                    moDbmsCostCenter = new SDataCostCenter();
                    moDbmsCostCenter.read(new String[] {res.getString(1)}, statement.getConnection().createStatement());
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

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL trn_diog_ety_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkDocId);
            callableStatement.setInt(nParam++, mnPkEntryId);
            callableStatement.setDouble(nParam++, mdQuantity);
            callableStatement.setDouble(nParam++, mdValueUnitary);
            callableStatement.setDouble(nParam++, mdValue);
            callableStatement.setDouble(nParam++, mdOriginalQuantity);
            callableStatement.setDouble(nParam++, mdOriginalValueUnitary);
            callableStatement.setInt(nParam++, mnSortingPosition);
            callableStatement.setBoolean(nParam++, mbIsInventoriable);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkItemId);
            callableStatement.setInt(nParam++, mnFkUnitId);
            callableStatement.setInt(nParam++, mnFkOriginalUnitId);
            if (mnFkDpsYearId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkDpsYearId_n); else callableStatement.setNull(nParam++, Types.SMALLINT);
            if (mnFkDpsDocId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkDpsDocId_n); else callableStatement.setNull(nParam++, Types.INTEGER);
            if (mnFkDpsEntryId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkDpsEntryId_n); else callableStatement.setNull(nParam++, Types.INTEGER);
            if (mnFkDpsAdjustmentYearId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkDpsAdjustmentYearId_n); else callableStatement.setNull(nParam++, Types.SMALLINT);
            if (mnFkDpsAdjustmentDocId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkDpsAdjustmentDocId_n); else callableStatement.setNull(nParam++, Types.INTEGER);
            if (mnFkDpsAdjustmentEntryId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkDpsAdjustmentEntryId_n); else callableStatement.setNull(nParam++, Types.INTEGER);
            if (mnFkMfgYearId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkMfgYearId_n); else callableStatement.setNull(nParam++, Types.SMALLINT);
            if (mnFkMfgOrderId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkMfgOrderId_n); else callableStatement.setNull(nParam++, Types.INTEGER);
            if (mnFkMfgChargeId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkMfgChargeId_n); else callableStatement.setNull(nParam++, Types.INTEGER);
            if (mnFkMatRequestId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkMatRequestId_n); else callableStatement.setNull(nParam++, Types.SMALLINT);
            if (mnFkMatRequestEtyId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkMatRequestEtyId_n); else callableStatement.setNull(nParam++, Types.SMALLINT);
            callableStatement.setInt(nParam++, mnFkMaintAreaId);
            callableStatement.setInt(nParam++, mnFkCostCenterId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, Types.VARCHAR);
            callableStatement.execute();

            mnPkEntryId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);
            
            String deleteQuery = "DELETE FROM trn_diog_ety_cons_ent_cc "
                    + "WHERE fid_diog_doc = " + mnFkDpsYearId_n + " "
                    + "AND fid_diog_year = " + mnFkDpsDocId_n + " "
                    + "AND fid_diog_ety = " + mnFkDpsEntryId_n + " ";
            connection.createStatement().executeUpdate(deleteQuery);
            if (mnFkMatRequestEtyId_n > 0) {
                ArrayList<SMatConsumeSubEntCcConfig> lConfigs = SMaterialRequestUtils.getCcConfigsFromMatReqEty(connection, 
                                                                                new int[] { mnFkMatRequestId_n, mnFkMatRequestEtyId_n });
                
                if (lConfigs.isEmpty()) {
                    lConfigs = SMaterialRequestUtils.getCcConfigsFromMatReq(connection, mnFkMatRequestId_n);
                }
                
                SDataDiogEtyMatConsEntCostCenter oDataConfig;
                for (SMatConsumeSubEntCcConfig oConfig : lConfigs) {
                    oDataConfig = new SDataDiogEtyMatConsEntCostCenter();
                    
                    oDataConfig.setPercentage(oConfig.getPercentage() <= 0d ? 100d : oConfig.getPercentage());
                    oDataConfig.setFkDiogYearId(mnPkYearId);
                    oDataConfig.setFkDiogDocId(mnPkDocId);
                    oDataConfig.setFkDiogEntryId(mnPkEntryId);
                    oDataConfig.setFkSubentMatConsumptionEntityId(oConfig.getFkSubentMatConsumptionEntityId());
                    oDataConfig.setFkSubentMatConsumptionSubentityId(oConfig.getFkSubentMatConsumptionSubentityId());
                    oDataConfig.setFkCostCenterId(oConfig.getFkCostCenterId());
                    
                    oDataConfig.save(connection);
                }
            }
            else if (mlAuxDiogEtyMatEntCcsConfigs != null && mlAuxDiogEtyMatEntCcsConfigs.size() > 0) {
                for (SDataDiogEtyMatConsEntCostCenter oDataConfig : mlAuxDiogEtyMatEntCcsConfigs) {
                    oDataConfig.setPercentage(oDataConfig.getPercentage() <= 0d ? 100d : oDataConfig.getPercentage());
                    oDataConfig.setIsRegistryNew(true);
                    oDataConfig.setFkDiogYearId(mnPkYearId);
                    oDataConfig.setFkDiogDocId(mnPkDocId);
                    oDataConfig.setFkDiogEntryId(mnPkEntryId);

                    oDataConfig.save(connection);
                }
            }

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
    
    public void calculateTotal() {
        if (mdQuantity == 0) {
            mdValue = SLibUtils.roundAmount(mdValueUnitary);
        }
        else {
            mdValue = SLibUtils.roundAmount(mdQuantity * mdValueUnitary);
        }
    }

    private boolean isEntryAlreadyDeleted() {
        return mbIsDeleted && !mbIsRegistryEdited;
    }

    private boolean isEntryBeingDeleted() {
        return mbIsDeleted && mbIsRegistryEdited;
    }

    /**
     * Checks if stock lots validation must be processed.
     */
    public boolean shouldValidateStockLots() {
        return mbIsInventoriable && (!isEntryAlreadyDeleted() || isEntryBeingDeleted());
    }

    /**
     * Checks if stock validation must be processed for outgoing items.
     * @param iogCategoryId Document IOG category ID. Constants defined in SDataConstantsSys.TRNS_CT_IOG_...
     * @param isDocBeingDeleted Flag that indicates if document is being deleted.
     */
    public boolean shouldValidateOutgoingItems(final int iogCategoryId, final boolean isDocBeingDeleted) {
        boolean check = false;

        if (isDocBeingDeleted) {
            if (isEntryBeingDeleted()) {
                check = iogCategoryId == SDataConstantsSys.TRNS_CT_IOG_OUT;
            }
            else {
                check = iogCategoryId == SDataConstantsSys.TRNS_CT_IOG_IN;
            }
        }
        else {
            if (isEntryBeingDeleted()) {
                check = iogCategoryId == SDataConstantsSys.TRNS_CT_IOG_IN;
            }
            else {
                check = iogCategoryId == SDataConstantsSys.TRNS_CT_IOG_OUT;
            }
        }

        return check;
    }

    /**
     * Validates entry stock move lots.
     * @param client GUI Client.
     * @param iogCategoryId DIOG category.
     * @param isDocBeingDeleted Flag that indicates that parent document is being deleted.
     * @throws java.lang.Exception
     * @return <code>String</code> with error message otherwise emtpy <code>String</code>.
     */
    public java.lang.String validateStockLots(final erp.client.SClientInterface client, final int iogCategoryId, final boolean isDocBeingDeleted) throws Exception {
        String msg = "";
        SDataStockLot lot = null;

        if (shouldValidateStockLots()) {
            // Validate when entry is not deleted or have just been deleted:

            if (mvAuxStockMoves.isEmpty()) {
                msg = "No se han especificado movimientos de inventarios del ítem '" + msDbmsItem + "', clave '" + msDbmsItemKey + "'.";
            }
            else {
                for (STrnStockMove stockMove : mvAuxStockMoves) {
                    if (stockMove.getPkLotId() == SLibConstants.UNDEFINED) {
                        lot = STrnUtilities.readLot(client, mnFkItemId, mnFkUnitId, stockMove.getAuxLot());
                    }
                    else {
                        lot = (SDataStockLot) SDataUtilities.readRegistry(client, SDataConstants.TRN_LOT, stockMove.getLotKey(), SLibConstants.EXEC_MODE_SILENT);
                    }

                    if (lot == null) {
                        if (shouldValidateOutgoingItems(iogCategoryId, isDocBeingDeleted)) {
                            msg = "El lote '" + stockMove.getAuxLot() + "' del ítem '" + msDbmsItem + "', clave '" + msDbmsItemKey + "' no existe o está eliminado.";
                            break;
                        }
                    }
                    else {
                        if (lot.getIsBlocked()) {
                            msg = "El lote '" + stockMove.getAuxLot() + "' del ítem '" + msDbmsItem + "', clave '" + msDbmsItemKey + "' está bloqueado.";
                            break;
                        }
                        else if (lot.getIsDeleted()) {
                            msg = "El lote '" + stockMove.getAuxLot() + "' del ítem '" + msDbmsItem + "', clave '" + msDbmsItemKey + "' está eliminado.";
                            break;
                        }
                        else {
                            stockMove.setPkLotId(lot.getPkLotId());
                        }
                    }
                }
            }
        }

        return msg;
    }

    @Override
    public SDataDiogEntry clone() throws CloneNotSupportedException {
        SDataDiogEntry registry = new SDataDiogEntry();

        registry.setPkYearId(this.getPkYearId());
        registry.setPkDocId(this.getPkDocId());
        registry.setPkEntryId(this.getPkEntryId());
        registry.setQuantity(this.getQuantity());
        registry.setValueUnitary(this.getValueUnitary());
        registry.setValue(this.getValue());
        registry.setOriginalQuantity(this.getOriginalQuantity());
        registry.setOriginalValueUnitary(this.getOriginalValueUnitary());
        registry.setSortingPosition(this.getSortingPosition());
        registry.setIsInventoriable(this.getIsInventoriable());
        registry.setIsDeleted(this.getIsDeleted());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkOriginalUnitId(this.getFkOriginalUnitId());
        registry.setFkDpsYearId_n(this.getFkDpsYearId_n());
        registry.setFkDpsDocId_n(this.getFkDpsDocId_n());
        registry.setFkDpsEntryId_n(this.getFkDpsEntryId_n());
        registry.setFkDpsAdjustmentYearId_n(this.getFkDpsAdjustmentYearId_n());
        registry.setFkDpsAdjustmentDocId_n(this.getFkDpsAdjustmentDocId_n());
        registry.setFkDpsAdjustmentEntryId_n(this.getFkDpsAdjustmentEntryId_n());
        registry.setFkMfgYearId_n(this.getClonedFkMfgYearId_n() != SLibConstants.UNDEFINED ? this.getClonedFkMfgYearId_n() : this.getFkMfgYearId_n());
        registry.setFkMfgOrderId_n(this.getClonedFkMfgOrderId_n() != SLibConstants.UNDEFINED ? this.getClonedFkMfgOrderId_n() : this.getFkMfgOrderId_n());
        registry.setFkMfgChargeId_n(this.getClonedFkMfgChargeId_n() != SLibConstants.UNDEFINED ? this.getClonedFkMfgChargeId_n() : this.getFkMfgChargeId_n());
        registry.setFkMatRequestId_n(this.getFkMatRequestId_n());
        registry.setFkMatRequestEtyId_n(this.getFkMatRequestEtyId_n());
        registry.setFkMaintAreaId(this.getFkMaintAreaId());
        registry.setFkCostCenterId(this.getFkCostCenterId());
        registry.setFkUserNewId(this.getFkUserNewId());
        registry.setFkUserEditId(this.getFkUserEditId());
        registry.setFkUserDeleteId(this.getFkUserDeleteId());
        registry.setUserNewTs(this.getUserNewTs());
        registry.setUserEditTs(this.getUserEditTs());
        registry.setUserDeleteTs(this.getUserDeleteTs());

        registry.setDbmsBookkeepingYearId(this.getDbmsBookkeepingYearId());
        registry.setDbmsBookkeepingNumberId(this.getDbmsBookkeepingNumberId());
        registry.setDbmsItem(this.getDbmsItem());
        registry.setDbmsItemKey(this.getDbmsItemKey());
        registry.setDbmsUnit(this.getDbmsUnit());
        registry.setDbmsUnitSymbol(this.getDbmsUnitSymbol());
        registry.setDbmsMaintArea(this.getDbmsMaintArea());
        registry.setDbmsUserNew(this.getDbmsUserNew());
        registry.setDbmsUserEdit(this.getDbmsUserEdit());
        registry.setDbmsUserDelete(this.getDbmsUserDelete());
        
        registry.setDbmsCostCenter(this.getDbmsCostCenter());

        for (STrnStockMove move : mvAuxStockMoves) {
            registry.getAuxStockMoves().add(move.clone());
        }
        
        for (SDataDiogEtyMatConsEntCostCenter oConfig : mlAuxDiogEtyMatEntCcsConfigs) {
            registry.getAuxDiogEtyMatEntCcsConfigs().add(oConfig.clone());
        }

        return registry;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.trn.form.SDialogItemPicker;
import erp.mod.trn.form.SDialogUnitPicker;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataDiogEntry;
import erp.mtrn.data.SDataMaterialRequestRow;
import erp.mtrn.data.SDataStockSegregation;
import erp.mtrn.data.SDataStockSegregationWarehouseEntry;
import erp.mtrn.data.STrnStockMove;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiOptionPickerSettings;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona, Isabel Servín
 */
public abstract class SMaterialRequestUtils {

    public static String ITEM_INV = "is_inv";
    public static final int EQUIVALENCES = 1;
    
    public static SDataStockSegregation getSegregationOfMaterialRequest(SGuiSession session, final int idMaterialRequest) {
        String query = "SELECT id_stk_seg "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_SEG) + " "
                + "WHERE fid_tp_stk_seg = " + SDataConstantsSys.TRNS_TP_STK_SEG_REQ_MAT + " "
                + "AND fid_ref_2_n IS NULL "
                + "AND fid_ref_1 = " + idMaterialRequest + " "
                + "AND NOT b_del;";
        
        try {
            SDataStockSegregation oStkSeg = new SDataStockSegregation();
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(query);
            if (res.next()) {
                oStkSeg.read(new int[] { res.getInt("id_stk_seg") }, session.getDatabase().getConnection().createStatement());
                return oStkSeg;
            }
            
            return oStkSeg;
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static ArrayList<SDataStockSegregationWarehouseEntry> getSegregationEtysOfMaterialRequest(SGuiSession session, final int idMaterialRequest, final int idMatEty, final int idItem, final int idUnit) {
        String query = "SELECT  " +
                        "id_cob, " +
                        "id_whs, " +
                        "SUM(qty_inc) - SUM(qty_dec) AS qty, " +
                        "fid_tp_stk_seg_mov, " +
                        "fid_year, " +
                        "fid_item, " +
                        "fid_unit " +
                        "FROM " +
                        " " + SModConsts.TablesMap.get(SModConsts.TRN_STK_SEG_WHS_ETY) + " " +
                        "WHERE " +
                        "fid_mat_req_n = " + idMaterialRequest + " " +
                        "AND fid_item = " + idItem + " " +
                        "AND fid_unit = " + idUnit + " " +
                        "GROUP BY id_cob , id_whs , fid_year , fid_item , fid_unit " +
                        "HAVING qty <> 0;";
        
        try {
            ArrayList<SDataStockSegregationWarehouseEntry> etys = new ArrayList<>();
            ResultSet res = session.getDatabase().getConnection().createStatement().executeQuery(query);
            SDataStockSegregationWarehouseEntry oStkSegEty;
            while (res.next()) {
                oStkSegEty = new SDataStockSegregationWarehouseEntry();
                
                oStkSegEty.setPkCompanyBranchId(res.getInt("id_cob"));
                oStkSegEty.setPkWarehouseId(res.getInt("id_whs"));
                if (res.getDouble("qty") > 0d) {
                    oStkSegEty.setQuantityIncrement(res.getDouble("qty"));
                    oStkSegEty.setQuantityDecrement(0d);
                    oStkSegEty.setFkStockSegregationMovementTypeId(SDataConstantsSys.TRNS_TP_STK_SEG_INC);
                }
                else {
                    oStkSegEty.setQuantityIncrement(0d);
                    oStkSegEty.setQuantityDecrement(res.getDouble("qty"));
                    oStkSegEty.setFkStockSegregationMovementTypeId(SDataConstantsSys.TRNS_TP_STK_SEG_DEC);
                }
                oStkSegEty.setFkItemId(res.getInt("fid_item"));
                oStkSegEty.setFkUnitId(res.getInt("fid_unit"));
                oStkSegEty.setFkYearId(res.getInt("fid_year"));
                oStkSegEty.setFkMatRequestId_n(idMaterialRequest);
                oStkSegEty.setFkMatRequestEtyId_n(idMatEty);
                
                oStkSegEty.readAuxs(session.getStatement());
                
                etys.add(oStkSegEty);
            }
            
            return etys;
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static ArrayList<SMaterialRequestSupplyRow> getMaterialRequestSupplies(SGuiClient client, final int idMaterialRequest, final int idMatEty) {
        String query = "SELECT  " +
                    "    * " +
                    "FROM " +
                    SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS diog " +
                    "INNER JOIN " +
                    SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS dety ON diog.id_year = dety.id_year " +
                    "        AND diog.id_doc = dety.id_doc " +
                    "WHERE " +
                    "NOT diog.b_del " +
                    "  AND NOT dety.b_del AND dety.fid_mat_req_n = " + idMaterialRequest + " " +
                    "        AND dety.fid_mat_req_ety_n = " + idMatEty + " " +
                    "        AND diog.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_SUPP_CONS[0] + " " +
                    "        AND diog.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_SUPP_CONS[1] + " " +
                    "        AND diog.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_SUPP_CONS[2] + " " +
                    "ORDER BY dety.sort_pos ASC;";
        
        try {
            ResultSet res = client.getSession().getDatabase().getConnection().createStatement().executeQuery(query);
            ArrayList<SMaterialRequestSupplyRow> lRows = new ArrayList<>();
            SMaterialRequestSupplyRow oSuppRow;
            while (res.next()) {
                oSuppRow = new SMaterialRequestSupplyRow((SClientInterface) client, 
                                                        res.getInt("dety.fid_item"), 
                                                        res.getInt("dety.fid_unit"), 
                                                        res.getInt("dety.fid_orig_unit"), 
                                                        res.getInt("fid_cob"), 
                                                        res.getInt("fid_wh"));
                
                oSuppRow.setQuantity(res.getDouble("qty"));
                oSuppRow.setPkDiogYearId(res.getInt("id_year"));
                oSuppRow.setPkDiogDocId(res.getInt("id_doc"));
                oSuppRow.setPkDiogEtyId(res.getInt("id_ety"));
                oSuppRow.setFkMatRequestId(idMaterialRequest);
                oSuppRow.setFkMatRequestEntryId(idMatEty);
                
                lRows.add(oSuppRow);
            }
            
            return lRows;
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>();
    }
    
    public static double getQtySuppliedOfReqMatEty(SGuiClient client, final int idMaterialRequest, final int idMatEty) {
        ArrayList<SMaterialRequestSupplyRow> lRows = SMaterialRequestUtils.getMaterialRequestSupplies(client, idMaterialRequest, idMatEty);
        double qtySupplied = 0d;
        if (lRows.size() > 0) {
            qtySupplied = lRows.stream()
                        .mapToDouble(o -> o.getQuantity())
                        .sum();
        }
        
        return qtySupplied;
    }
    
    public static ArrayList<SDataDiog> makeDiogs(SGuiClient client, final int pkYear, final Date date, final int fkUser, ArrayList<SMaterialRequestSupplyRow> lSupplies, final int idStkseg, final int user, final int userSup) {
        HashMap<String, SDataDiog> mDiogs = new HashMap<>();
        String warehouseKey = null;
        for (SMaterialRequestSupplyRow oSupply : lSupplies) {
            SDataDiog oDiog = null;
            warehouseKey = oSupply.getFkCompanyBranchId() + "_" + oSupply.getFkWarehouseId();
            if (! mDiogs.containsKey(warehouseKey)) {
                oDiog = new SDataDiog();
                
                oDiog.setPkYearId(pkYear);
                oDiog.setPkDocId(0);
                oDiog.setDate(date);
                oDiog.setNumberSeries("");
                oDiog.setNumber("");
                oDiog.setReference("");
                oDiog.setValue_r(0d);
                oDiog.setCostAsigned(0);
                oDiog.setCostTransferred(0);
                oDiog.setIsShipmentRequired(false);
                oDiog.setIsShipped(false);
                oDiog.setIsAudited(false);
                oDiog.setIsAuthorized(false);
                oDiog.setIsRecordAutomatic(false);
                oDiog.setIsSystem(true);
                oDiog.setIsDeleted(false);
                oDiog.setFkDiogCategoryId(SModSysConsts.TRNS_TP_IOG_OUT_SUPP_CONS[0]);
                oDiog.setFkDiogClassId(SModSysConsts.TRNS_TP_IOG_OUT_SUPP_CONS[1]);
                oDiog.setFkDiogTypeId(SModSysConsts.TRNS_TP_IOG_OUT_SUPP_CONS[2]);
                oDiog.setFkDiogAdjustmentTypeId(1);
                oDiog.setFkCompanyBranchId(oSupply.getFkCompanyBranchId());
                oDiog.setFkWarehouseId(oSupply.getFkWarehouseId());
                oDiog.setFkDpsYearId_n(0);
                oDiog.setFkDpsDocId_n(0);
                oDiog.setFkDiogYearId_n(0);
                oDiog.setFkDiogDocId_n(0);
                oDiog.setFkMfgYearId_n(0);
                oDiog.setFkMfgOrderId_n(0);
                oDiog.setFkMatRequestId_n(oSupply.getFkMatRequestId());
                oDiog.setFkBookkeepingYearId_n(0);
                oDiog.setFkBookkeepingNumberId_n(0);
                oDiog.setFkMaintMovementTypeId(SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_MAT);
                oDiog.setFkMaintUserId_n(user);
                oDiog.setFkMaintUserSupervisorId(userSup);
                oDiog.setFkMaintReturnUserId_n(0);
                oDiog.setFkMaintReturnUserSupervisorId(1);
                oDiog.setFkUserShippedId(1);
                oDiog.setFkUserNewId(fkUser);
                oDiog.setFkUserAuditedId(1);
                oDiog.setFkUserAuthorizedId(1);
                oDiog.setFkUserDeleteId(1);
                
                oDiog.setAuxSegregationStockId(idStkseg);
                        
                mDiogs.put(warehouseKey, oDiog);
            }
            else {
                oDiog = mDiogs.get(warehouseKey);
            }
            
            SDataDiogEntry oDiogEty = new SDataDiogEntry();
            
            oDiogEty.setQuantity(oSupply.getQuantity());
            oDiogEty.setValueUnitary(0d);
            oDiogEty.setValue(0d);
            oDiogEty.setOriginalQuantity(SLibUtilities.round(oSupply.getQuantity() * 
                                                        ((SSessionCustom) client.getSession().getSessionCustom()).getUnitsFactorForQuantity(oSupply.getFkItemId(), oSupply.getFkUnitId(), oSupply.getFkOrigUnitId()), ((SClientInterface) client).getSessionXXX().getParamsErp().getDecimalsQuantity()));
            oDiogEty.setOriginalValueUnitary(0d);
            oDiogEty.setSortingPosition(0);
            oDiogEty.setIsInventoriable(oSupply.getIsInventorable());
            oDiogEty.setIsDeleted(false);
            oDiogEty.setFkItemId(oSupply.getFkItemId());
            oDiogEty.setFkUnitId(oSupply.getFkUnitId());
            oDiogEty.setFkOriginalUnitId(oSupply.getFkOrigUnitId());
            oDiogEty.setFkDpsYearId_n(0);
            oDiogEty.setFkDpsDocId_n(0);
            oDiogEty.setFkDpsEntryId_n(0);
            oDiogEty.setFkDpsAdjustmentYearId_n(0);
            oDiogEty.setFkDpsAdjustmentDocId_n(0);
            oDiogEty.setFkDpsAdjustmentEntryId_n(0);
            oDiogEty.setFkMfgYearId_n(0);
            oDiogEty.setFkMfgOrderId_n(0);
            oDiogEty.setFkMfgChargeId_n(0);
            oDiogEty.setFkMatRequestId_n(oSupply.getFkMatRequestId());
            oDiogEty.setFkMatRequestEtyId_n(oSupply.getFkMatRequestEntryId());
            oDiogEty.setFkUserNewId(fkUser);
            
            // year, item, unit, lot, company branch, warehouse
            oDiogEty.getAuxStockMoves().add(new STrnStockMove(new int[] { pkYear, 
                                                                            oSupply.getFkItemId(), 
                                                                            oSupply.getFkUnitId(), 
                                                                            0, 
                                                                            oSupply.getFkCompanyBranchId(), 
                                                                            oSupply.getFkWarehouseId()
                                                                        }, 
                                                                        oSupply.getQuantity()));
            oDiog.getDbmsEntries().add(oDiogEty);
        }
        
        ArrayList<SDataDiog> lDiogs = new ArrayList<>();
        for (Map.Entry<String, SDataDiog> entry : mDiogs.entrySet()) {
            lDiogs.add(entry.getValue());
        }
        
        return lDiogs;
    }
    
    public static String saveDiogs(Connection connection, ArrayList<SDataDiog> lDiogs) {
        String result;
        try {
            for (SDataDiog oDiog : lDiogs) {
                if (! oDiog.getIsRegistryNew()) {
                    SMaterialRequestUtils.deleteMaintDiogSign(connection, oDiog.getPkYearId(), oDiog.getPkDocId());
                }
                
                boolean areAllDeleted = true;
                for (SDataDiogEntry oEntry : oDiog.getDbmsEntries()) {
                    if (! oEntry.getIsDeleted()) {
                        areAllDeleted = false;
                        break;
                    }
                }
                
                if (areAllDeleted) {
                    oDiog.setIsDeleted(true);
                }
                
                oDiog.save(connection.createStatement().getConnection());
                
                if (oDiog.getLastDbActionResult() == SLibConstants.DB_ACTION_SAVE_ERROR) {
                    return "Error en el guardado del documento, no es posible acceder al código de error.";
                }
            }
            
            return "";
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
            result = ex.getMessage();
        }
        
        return result;
    }
    
    public static void deleteMaintDiogSign(Connection connection, final int diogYear, final int diogDoc) {
        String sql = "DELETE FROM trn_maint_diog_sig "
                + "WHERE fk_diog_year = " + diogYear + " AND fk_diog_doc = " + diogDoc + ";";
        
        try {
            connection.createStatement().executeUpdate(sql);
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Devuelve el objeto del grupo del centro de costo a traves del ítem
     * @param session
     * @param link 
     * @param id 
     * @return Grupo cc
     * @throws java.lang.Exception
     */
    public static SDbMaterialCostCenterGroup getCostCenterGroupByItem(SGuiSession session, int link, int id) throws Exception {
        SDbMaterialCostCenterGroup ccg = new SDbMaterialCostCenterGroup();
        Statement statement = session.getDatabase().getConnection().createStatement();
        String sql = "SELECT id_mat_cc_grp FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CC_GRP_ITEM) + " " +
                "WHERE id_link = " + link + " AND id_ref = " + id + " ";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            ccg.read(session, new int[] { resultSet.getInt(1)} );
        }
        return ccg;
    }
    
    /**
     * Devuelve el objeto del grupo del centro de costo a traves de un id de usuario o empleado
     * @param session
     * @param link 1 Usuario, 2 Empleado
     * @param id 
     * @return Grupo cc
     * @throws java.lang.Exception
     */
    public static SDbMaterialCostCenterGroup getCostCenterGroupByUser(SGuiSession session, int link, int id) throws Exception {
        SDbMaterialCostCenterGroup ccg = new SDbMaterialCostCenterGroup();
        Statement statement = session.getDatabase().getConnection().createStatement();
        String sql = "SELECT id_mat_cc_grp FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CC_GRP_USR) + " " +
                "WHERE id_link = " + link + " AND id_ref = " + id + " ";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            ccg.read(session, new int[] { resultSet.getInt(1)} );
        }
        return ccg;
    }
    
    /**
     * Devuelve el objeto del grupo del centro de costo a traves del pk de la subentidad de consumo y el pk del centro de costo
     * @param session 
     * @param pkConsSubent 
     * @param pkCc 
     * @return Grupo cc
     * @throws java.lang.Exception
     */
    public static ArrayList<SDbMaterialCostCenterGroup> getCostCenterGroupByUser(SGuiSession session, int[] pkConsSubent, int pkCc) throws Exception {
        ArrayList<SDbMaterialCostCenterGroup> array = new ArrayList<>();
        Statement statement = session.getDatabase().getConnection().createStatement();
        String sql = "SELECT id_mat_cc_grp FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT_CC_CC_GRP) + " " +
                "WHERE id_mat_cons_ent = " + pkConsSubent[0] + " AND id_mat_cons_subent = " + pkConsSubent[1] + " AND id_cc = " + pkCc;
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            SDbMaterialCostCenterGroup ccg = new SDbMaterialCostCenterGroup();
            ccg.read(session, new int[] { resultSet.getInt(1)} );
            array.add(ccg);
        }
        return array;
    }
    
    @SuppressWarnings("unchecked")
    public static SDialogItemPicker getOptionItemPicker(SGuiClient client, int type, int subtype, SGuiParams params) {
        String sql = "";
        ArrayList<SGridColumnForm> gridColumns = new ArrayList<>();
        SGuiOptionPickerSettings settings;
        SDialogItemPicker picker = new SDialogItemPicker();
        switch (type) {
            case SModConsts.ITMU_ITEM:
                switch (subtype) {
                    case SModConsts.TRN_MAT_REQ:
                        String in = "";
                        ArrayList<SDbMaterialCostCenterGroup> arr = (ArrayList<SDbMaterialCostCenterGroup>) params.getParamsMap().get(SModConsts.TRN_MAT_CC_GRP);
                        for (SDbMaterialCostCenterGroup ccg : arr) {
                            in += (in.isEmpty() ? "(" : ", ") + ccg.getPkMaterialCostCenterGroupId();
                        }
                        in += (in.isEmpty() ? "" : ")");
                        sql = "SELECT a.id_item AS " + SDbConsts.FIELD_ID + "1, "
                                + "a.item_key AS " + SDbConsts.FIELD_PICK + "1, a.item AS " + SDbConsts.FIELD_PICK + "2, "
                                + "a.part_num AS " + SDbConsts.FIELD_PICK + "3, "
                                + "a.b_inv AS " + ITEM_INV + " "
                                + "FROM ("
                                + "SELECT * FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CC_GRP_ITEM) + " AS igen ON "
                                + "igen.id_link = " + SModSysConsts.ITMS_LINK_IGEN + " AND igen.id_ref = i.fid_igen " 
                                + "UNION "
                                + "SELECT * FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CC_GRP_ITEM) + " AS ii ON "
                                + "ii.id_link = " + SModSysConsts.ITMS_LINK_ITEM + " AND ii.id_ref = i.id_item "
                                + ") AS a "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CC_GRP_USR) + " AS ccgu ON "
                                + "a.id_mat_cc_grp = ccgu.id_mat_cc_grp "
                                + "WHERE NOT a.b_del AND a.id_mat_cc_grp IN " + in + " "
                                + "AND a.fid_st_item = " + SModSysConsts.ITMS_ST_ITEM_ACT + " " 
                                + "AND ccgu.id_link = " + SModSysConsts.USRS_LINK_USR + " "
                                + "AND ccgu.id_ref = " + params.getParamsMap().get(SModConsts.USRU_USR) + " "
                                + "ORDER BY a.item_key, a.item, a.id_item, a.part_num ";
                        break;
                    case SLibConstants.UNDEFINED:
                        sql = "SELECT a.id_item AS " + SDbConsts.FIELD_ID + "1, "
                                + "a.item_key AS " + SDbConsts.FIELD_PICK + "1, a.item AS " + SDbConsts.FIELD_PICK + "2, "
                                + "a.part_num AS " + SDbConsts.FIELD_PICK + "3, "
                                + "a.b_inv AS " + ITEM_INV + " "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS a " 
                                + "WHERE NOT a.b_del "
                                + "AND a.fid_st_item = " + SModSysConsts.ITMS_ST_ITEM_ACT + " ";
                }
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "Clave"));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Ítem"));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "Parte"));
                settings = new SGuiOptionPickerSettings("Ítem", sql, gridColumns, 1);
                
                picker.setPickerSettings(client, type, subtype, settings);
                break;
        }
        
        return picker;
    }
    
    @SuppressWarnings("unchecked")
    public static SDialogUnitPicker getOptionUnitPicker(SGuiClient client, int type, int subtype, SGuiParams params) {
        String sql = "";
        ArrayList<SGridColumnForm> gridColumns = new ArrayList<>();
        SGuiOptionPickerSettings settings;
        SDialogUnitPicker picker = new SDialogUnitPicker();
        switch (type) {
            case SModConsts.ITMU_UNIT:
                switch (subtype) {
                    case SLibConsts.UNDEFINED:
                        sql = "SELECT a.id_unit AS " + SDbConsts.FIELD_ID + "1, "
                                + "a.unit AS " + SDbConsts.FIELD_PICK + "1, a.symbol AS " + SDbConsts.FIELD_PICK + "2 "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS a " 
                                + "WHERE NOT a.b_del ";
                        break;
                    case EQUIVALENCES:
                        sql = "SELECT u.id_unit AS " + SDbConsts.FIELD_ID + "1, " +
                                "u.unit AS " + SDbConsts.FIELD_PICK + "1, u.symbol AS " + SDbConsts.FIELD_PICK + "2 " +
                                "FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u " +
                                "WHERE " +
                                "    NOT u.b_del " +
                                "        AND (fid_tp_unit = (SELECT  " +
                                "            fid_tp_unit " +
                                "        FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS ui " +
                                "        WHERE " +
                                "            ui.id_unit = " + params.getParamsMap().get(0) + ") " +
                                "        OR fid_tp_unit = (COALESCE((SELECT  " +
                                "                    fid_tp_unit_alt " +
                                "                FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS itm " +
                                "                WHERE " +
                                "                    id_item = " + params.getParamsMap().get(1) + "), " +
                                "            0)) " +
                                "        OR u.id_unit = (COALESCE((SELECT  " +
                                "                    fid_unit_comm_n " +
                                "                FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS itm " +
                                "                WHERE " +
                                "                    id_item = " + params.getParamsMap().get(1) + "), " +
                                "            0)) " +
                                "        OR u.id_unit IN (SELECT  " +
                                "            id_unit " +
                                "        FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT_EQUIV) + " AS ue " +
                                "        WHERE " +
                                "            ue.id_unit_equiv = " + params.getParamsMap().get(0) + " AND NOT ue.b_del) " +
                                "        OR u.id_unit IN (SELECT  " +
                                "            id_unit_equiv " +
                                "        FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT_EQUIV) + " AS ue " +
                                "        WHERE " +
                                "            ue.id_unit = " + params.getParamsMap().get(0) + " AND NOT ue.b_del));";
                        break;
                }
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Unidad"));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Símbolo"));
                settings = new SGuiOptionPickerSettings("Unidad", sql, gridColumns, 1);
                
                picker.setPickerSettings(client, type, subtype, settings);
                break;
        }
        
        return picker;
    }
    
    /**
     * Obtiene las requisiciones de materiales para ser mostradas en el picker de selección en la importación
     * 
     * @param client
     * @param idUser
     * @param idConsumeEntity
     * @param idConsumeSubentity
     * @param folio
     * @return 
     */
    public static ArrayList<SDataMaterialRequestRow> getMaterialRequest(SGuiClient client, final int idUser, final int idConsumeEntity, final int idConsumeSubentity, final String folio) {
        ArrayList<SDataMaterialRequestRow> rows = new ArrayList<>();
        try {
            String sql = "SELECT id_mat_req FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " WHERE NOT b_del "
                    + "AND NOT b_clo_pur AND b_clo_prov AND fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PUR + " ";
            if (idUser > 0) {
                sql += "AND fk_usr_req = " + idUser + " ";
            }
            if (folio != null && !folio.isEmpty()) {
                sql += "AND num LIKE '%" + folio + "%' ";
            }
            
            ResultSet resultSet = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            
            SDataMaterialRequestRow row;
            SDbMaterialRequest matReq;
            while(resultSet.next()) {
                matReq = new SDbMaterialRequest();
                matReq.read(client.getSession(), new int[] { resultSet.getInt("id_mat_req") });
                row = new SDataMaterialRequestRow((SClientInterface) client, matReq);
                rows.add(row);
            }
            
            return rows;
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Obtiene la cantidad importada de la partida de la requisición, puede filtrarse por tipo de documento.
     * 
     * @param session
     * @param pkMatReqEty llave primaria de la partida de la requisición
     * @param dpsType tipo de documento, si este es nulo, no filtra y muestra la suma de todas las referencias de todos los tipos de documento
     * @param pkDpsEtyExcluded
     * @param qtyOrOrigQty 1 = qty, 2 = orig_qty
     * @return 
     */
    public static double getQuantityLinkedOfReqEty(SGuiSession session, final int[] pkMatReqEty, final int[] dpsType, final int[] pkDpsEtyExcluded, final int qtyOrOrigQty) {
        String query = "SELECT "+ (qtyOrOrigQty == 1 ? "SUM(dmr.qty) " : "SUM(dety.orig_qty) ") + " AS qty_linked FROM "
                + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " AS dmr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS dety ON dmr.fid_dps_year = dety.id_year "
                                                                                        + "AND dmr.fid_dps_doc = dety.id_doc "
                                                                                        + "AND dmr.fid_dps_ety = dety.id_ety "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON dety.id_year = d.id_year "
                                                                                            + "AND dety.id_doc = d.id_doc ";
        if (dpsType != null) {
            query += "AND d.fid_ct_dps = " + dpsType[0]  + " " +
                   "AND d.fid_cl_dps = " + dpsType[1]  + " " +
                   "AND d.fid_tp_dps = " + dpsType[2]  + " ";
        }
        
        query += "WHERE NOT dety.b_del AND NOT d.b_del AND dmr.fid_mat_req = " + pkMatReqEty[0] + " AND dmr.fid_mat_req_ety = " + pkMatReqEty[1] + " ";
        
        if (pkDpsEtyExcluded != null) {
            if (pkDpsEtyExcluded.length == 2) {
                query += "AND dmr.fid_dps_year <> " + pkDpsEtyExcluded[0] + " AND dmr.fid_dps_doc <> " + pkDpsEtyExcluded[1] + " ";
            }
            if (pkDpsEtyExcluded.length == 3) {
                query += "AND dmr.fid_dps_year <> " + pkDpsEtyExcluded[0] + " AND dmr.fid_dps_doc <> " + pkDpsEtyExcluded[1] + " AND dmr.fid_dps_ety <> " + pkDpsEtyExcluded[2];
            }
        }
        
        try {
            ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(query);
            
            if (resultSet.next()) {
                return resultSet.getDouble("qty_linked");
            }
            
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0d;
    }
    
    /**
     * 
     * @param session
     * @param pkMatRequestEty
     * @param qtyOrQtyOrig 1 = qty, 2 = orig_qty
     * @return 
     */
    public static double getQuantitySuppliedOfReqEty(SGuiSession session, final int[] pkMatRequestEty, final int qtyOrQtyOrig) {
        String query = "SELECT  " +
                "     SUM(" + (qtyOrQtyOrig == 1 ? "de.qty" : "de.orig_qty"  ) + ") AS qty_supplied " +
                "    FROM " +
                "        trn_diog AS d " +
                "    INNER JOIN trn_diog_ety AS de ON d.id_year = de.id_year " +
                "        AND d.id_doc = de.id_doc " +
                "    INNER JOIN trn_mat_req AS v ON de.fid_mat_req_n = v.id_mat_req " +
                "    WHERE " +
                "        de.fid_mat_req_n IS NOT NULL " +
                "            AND de.fid_mat_req_ety_n IS NOT NULL " +
                "            AND NOT de.b_del " +
                "            AND NOT d.b_del " +
                "            AND d.fid_ct_iog IN (" + SDataConstantsSys.TRNS_CT_IOG_OUT +") " +
                "            AND d.fid_cl_iog IN (" + SModSysConsts.TRNS_TP_IOG_OUT_SUPP_CONS[1] + ") " +
                "            AND d.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_OUT_SUPP_CONS[2] + ") " +
                "            AND de.fid_mat_req_n = " + pkMatRequestEty[0] + " " +
                "            AND de.fid_mat_req_ety_n = " + pkMatRequestEty[1] + ";";
        
        try {
            ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(query);
            
            if (resultSet.next()) {
                return resultSet.getDouble("qty_supplied");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0d;
    }
    
    public static double getPurchasedPercent(SGuiSession session, final int idMatReq, final int[] dpsType) {
        String query = "SELECT " +
                        "tmre.id_mat_req, " +
                        "tmre.id_ety, " +
                        "tmre.qty " +
                        "FROM " +
                        SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS tmr " +
                        "INNER JOIN " +
                        SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " AS tmre ON (tmr.id_mat_req = tmre.id_mat_req) " +
                        "WHERE " +
                        "NOT tmr.b_del AND NOT tmre.b_del AND tmr.id_mat_req = " + idMatReq + ";";
        try {
            ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(query);
            double qtyReq = 0d;
            double qtySupplied = 0d;
            double qtyLinked = 0d;
            //1 = qty, 2 = orig_qty;
            int qtyOrOrigQty = 1;
            while (resultSet.next()) {
                qtyReq += resultSet.getDouble("qty");
                qtySupplied += SMaterialRequestUtils.getQuantitySuppliedOfReqEty(session, new int[] { resultSet.getInt("id_mat_req"), resultSet.getInt("id_ety") }, qtyOrOrigQty);
                qtyLinked += SMaterialRequestUtils.getQuantityLinkedOfReqEty(session, new int[] { resultSet.getInt("id_mat_req"), resultSet.getInt("id_ety") }, dpsType, null, qtyOrOrigQty);
            }
            
            double percent = 0d;
            
            if (qtyLinked <= 0d || qtyReq <= 0d) {
                percent = 0d;
            }
            else if (qtyLinked > (qtyReq - qtySupplied)) {
                percent = 1d;
            }
            else {
                percent = qtyLinked / (qtyReq - qtySupplied);
            }
            
            return percent < 0d ? 0d : percent;
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0d;
    }
    
    public static double getSuppliedPercent(SGuiSession session, final int idMatReq) {
        String query = "SELECT " +
                        "tmre.id_mat_req, " +
                        "tmre.id_ety, " +
                        "tmre.qty " +
                        "FROM " +
                        SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS tmr " +
                        "INNER JOIN " +
                        SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " AS tmre ON (tmr.id_mat_req = tmre.id_mat_req) " +
                        "WHERE " +
                        "NOT tmr.b_del AND NOT tmre.b_del AND tmr.id_mat_req = " + idMatReq + ";";
        try {
            ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(query);
            double qtyReq = 0d;
            double qtySupplied = 0d;
            //1 = qty, 2 = orig_qty;
            int qtyOrOrigQty = 1;
            while (resultSet.next()) {
                qtyReq += resultSet.getDouble("qty");
                qtySupplied += SMaterialRequestUtils.getQuantitySuppliedOfReqEty(session, new int[] { resultSet.getInt("id_mat_req"), resultSet.getInt("id_ety") }, qtyOrOrigQty);
            }
            
            double percent = 0d;
            
            if (qtySupplied <= 0d || qtyReq <= 0d) {
                percent = 0d;
            }
            else if (qtySupplied > qtyReq) {
                percent = 1d;
            }
            else {
                percent = qtySupplied / qtyReq;
            }
            
            return percent < 0d ? 0d : percent;
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0d;
    }
    
    /*
     * Devuelve la cantidad de presupuesto que ha sido pedido a un centro de consumo, se excluyen los documentos eliminados y rechazados
     * @param session 
     * @param pkBudget 
     * @param reqId 
     * @return presupuesto solicitado
     * @throws java.lang.Exception
     */
    public static double getConsumedBudget(SGuiSession session, int[] pkBudget, int reqId) throws Exception {
        double consBud = 0;
        ResultSet resultSet;
        
        String sql = "SELECT cc.per, r.tot_r, " +
                "cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", '" + 
                SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', r.id_mat_req, NULL, NULL, NULL, NULL) AS auth " +
                "FROM trn_mat_req_cc AS cc " +
                "INNER JOIN trn_mat_req AS r ON " +
                "cc.id_mat_req = r.id_mat_req " +
                "WHERE cc.fk_budget_mat_cons_ent = " + pkBudget[0] + " " +
                "AND cc.fk_budget_year = " + pkBudget[1] + " " +
                "AND cc.fk_budget_period = " + pkBudget[2] + " " +
                "AND r.id_mat_req <> " + reqId + " " + 
                "AND NOT r.b_del;";
        resultSet = session.getStatement().executeQuery(sql);
        while (resultSet.next()) {
            if (resultSet.getInt(3) != SAuthorizationUtils.AUTH_STATUS_REJECTED) {
                consBud += resultSet.getDouble(1) * resultSet.getDouble(2);
            }
        }
        
        return consBud;
    }
    
    public static ArrayList<SMatConsumeSubEntCcConfig> getCcConfigsFromMatReq(Connection conn, final int idMatRequest) {
        ResultSet resultSet;
        
        String query = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_CC) + " "
                + "WHERE id_mat_req = " + idMatRequest;
        
        ArrayList<SMatConsumeSubEntCcConfig> lConfigs = new ArrayList<>();
        try {
            resultSet = conn.createStatement().executeQuery(query);
            SMatConsumeSubEntCcConfig oConfig;
            while (resultSet.next()) {
                oConfig = new SMatConsumeSubEntCcConfig();
                oConfig.setFkMaterialRequestId(idMatRequest);
                oConfig.setFkSubentMatConsumptionEntityId(resultSet.getInt("id_mat_subent_cons_ent"));
                oConfig.setFkSubentMatConsumptionSubentityId(resultSet.getInt("id_mat_subent_cons_subent"));
                oConfig.setFkCostCenterId(resultSet.getInt("id_cc"));

                lConfigs.add(oConfig);
            }
            
            return lConfigs;
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }
    
    public static ArrayList<SMatConsumeSubEntCcConfig> getCcConfigsFromMatReqEty(Connection conn, final int[] pkMatRequestEntry) {
        ResultSet resultSet;
        
        String query = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " "
                + "WHERE NOT b_del AND id_mat_req = " + pkMatRequestEntry[0] + " AND id_ety = " + pkMatRequestEntry[1];
        
        ArrayList<SMatConsumeSubEntCcConfig> lConfigs = new ArrayList<>();
        try {
            resultSet = conn.createStatement().executeQuery(query);
            SMatConsumeSubEntCcConfig oConfig;
            if (resultSet.next()) {
                if (resultSet.getInt("fk_subent_mat_cons_ent_n") == 0) {
                    return new ArrayList<>();
                }
                
                oConfig = new SMatConsumeSubEntCcConfig();
                oConfig.setPercentage(100d);
                oConfig.setFkMaterialRequestId(pkMatRequestEntry[0]);
                oConfig.setFkMaterialRequestEntryId(pkMatRequestEntry[1]);
                oConfig.setFkSubentMatConsumptionEntityId(resultSet.getInt("fk_subent_mat_cons_ent_n"));
                oConfig.setFkSubentMatConsumptionSubentityId(resultSet.getInt("fk_subent_mat_cons_subent_n"));
                oConfig.setFkCostCenterId(resultSet.getInt("fk_cc_n"));

                lConfigs.add(oConfig);
            }
            
            return lConfigs;
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }
    
    public static String hasLinksMaterialRequest(SGuiSession session, int[] pkMatReq) throws SQLException {
        String sql = "SELECT ety.* FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS ety "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS di ON ety.id_year = di.id_year "
                    + "AND ety.id_doc = di.id_doc "
                    + "WHERE ety.fid_mat_req_n = " + pkMatReq[0] + " "
                    + "AND NOT ety.b_del "
                    + "AND NOT di.b_del;";
        
        String message = "";
        ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql);
        
        if (resultSet.next()) {
            message = "La requisición tiene suministros de almacén.";
        }
        
        sql = "SELECT " +
            "dpsmr.* " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS ety " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON ety.id_year = d.id_year AND ety.id_doc = d.id_doc " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " AS dpsmr ON ety.id_year = dpsmr.fid_dps_year AND ety.id_doc = dpsmr.fid_dps_doc AND ety.id_ety = dpsmr.fid_dps_ety " +
            "WHERE NOT d.b_del AND NOT ety.b_del AND dpsmr.fid_mat_req = " + pkMatReq[0] + ";";
        
        ResultSet resultSetDps = session.getStatement().getConnection().createStatement().executeQuery(sql);
        
        if (resultSetDps.next()) {
            message = "La requisición tiene vínculos con documentos de compra.";
        }

        return message;
    }
    
    public static String updateStatusOfMaterialRequest(SGuiSession session, int[] pkMatReq, int statusId) {
        try {
            JTextArea textArea = new JTextArea(5, 40); // Set the number of rows and columns
            JScrollPane scrollPane = new JScrollPane(textArea);
            int option = JOptionPane.showOptionDialog(null, scrollPane, "Ingrese comentario/observación:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            String reason = "";
            if (option == JOptionPane.OK_OPTION) {
                reason = textArea.getText();
            }
            else {
                return "Acción cancelada";
            }
            
            SDbMaterialRequest req = new SDbMaterialRequest();
            req.read(session, pkMatReq);
            req.setFkMatRequestStatusId(statusId);
            
            if (statusId == SModSysConsts.TRNS_ST_MAT_REQ_NEW) {
                SAuthorizationUtils.deleteStepsOfAuthorization(session, SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST, pkMatReq);
            }
            else if (statusId == SModSysConsts.TRNS_ST_MAT_REQ_PUR) {
                req.setCloseProvision(true);
                req.setClosePurchase(false);
            }
            else if (statusId == SModSysConsts.TRNS_ST_MAT_REQ_PROV) {
                req.setCloseProvision(false);
                req.setClosePurchase(true);
            }
            
            req.setAuxNotesChangeStatus_n(reason);
            req.save(session);
            
            return "";
        }
        catch (Exception ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
    }
    
    public static String openOrCloseToSupply(SGuiSession session, int[] pkMatReq) {
        try {
            JTextArea textArea = new JTextArea(5, 40); // Set the number of rows and columns
            JScrollPane scrollPane = new JScrollPane(textArea);
            int option = JOptionPane.showOptionDialog(null, scrollPane, "Ingrese comentario/observación:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            String reason = "";
            if (option == JOptionPane.OK_OPTION) {
                reason = textArea.getText();
            }
            else {
                return "Acción cancelada";
            }
            
            SDbMaterialRequest req = new SDbMaterialRequest();
            req.read(session, pkMatReq);
            req.setCloseProvision(! req.isCloseProvision());
            req.setAuxNotesChangeStatus_n(reason);
            req.save(session);
            
            return "";
        }
        catch (Exception ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
    }
    
    public static boolean hasMatReqEtyEstimation(SGuiSession session, int[] pkMatReqEty) {
        String sql = "SELECT * FROM " + SModConsts.TablesMap.get(SModConsts.TRN_EST_REQ_ETY) + " "
                    + "WHERE fk_mat_req_n = " + pkMatReqEty[0] + " "
                    + "AND fk_mat_req_ety_n = " + pkMatReqEty[1] + " "
                    + "AND NOT b_del;";
        
        try {
            ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql);
        
            return resultSet.next();
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    /**
     * Determina si la partida de una RM ya ha sido agregada a una solicitud de cotización.
     * Basándose en la tabla @see SModConsts.TRN_MAT_REQ_ST_LOG
     * 
     * @param session
     * @param pkMatReq
     * 
     * @return true si la partida ya fue cotizada
     */
    public static ArrayList<SDbMaterialRequestStatusLog> getMaterialRequestLogs(SGuiSession session, int pkMatReq) {
        String sql = "SELECT id_log FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ST_LOG) + " "
                    + "WHERE id_mat_req = " + pkMatReq + " ;";
        
        try {
            ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql);
            ArrayList<SDbMaterialRequestStatusLog> lLogs = new ArrayList<>();
            SDbMaterialRequestStatusLog oLog;
            while (resultSet.next()) {
                oLog = new SDbMaterialRequestStatusLog();
                oLog.read(session, new int[] { pkMatReq, resultSet.getInt("id_log") });
                lLogs.add(oLog);
            }
            
            return lLogs;
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static ArrayList<SRowMaterialRequestDocs> getMaterialRequestDocs(SGuiSession session, int pkMatReq) {
        String sql = "SELECT  " +
                "    req.id_mat_req, " +
                "    req.num, " +
                "    req.dt, " +
                "    req.fk_usr_req, " +
                "    reqty.id_ety, " +
                "    reqty.qty, " +
                "    reqty.fk_item, " +
                "    reqty.fk_unit, " +
                "    itm.item_key, " +
                "    itm.item, " +
                "    itm.part_num, " +
                "    uni.symbol," +
                "    GROUP_CONCAT(IF(dps.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_EST[0] + " " +
                "            AND dps.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_EST[1] + " " +
                "            AND dps.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_EST[2] + ", " +
                "        CONCAT('COT ', (IF(length(dps.num_ser) > 0, CONCAT(dps.num_ser, '-'), '')), dps.num), " +
                "        '')) AS cot, " +
                "    GROUP_CONCAT(IF(dps.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] + " " +
                "            AND dps.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[1] + " " +
                "            AND dps.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[2] + ", " +
                "        CONCAT('PED ', (IF(length(dps.num_ser) > 0, CONCAT(dps.num_ser, '-'), '')), dps.num), " +
                "        '')) AS ped, " +
                "    GROUP_CONCAT(IF(dps.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[0] + " " +
                "            AND dps.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[1] + " " +
                "            AND dps.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[2] + ", " +
                "        CONCAT('FACT ', (IF(length(dps.num_ser) > 0, CONCAT(dps.num_ser, '-'), '')), dps.num), " +
                "        '')) AS fact " +
                "FROM " +
                "    " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS req " +
                "        INNER JOIN " +
                "    " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " AS reqty ON req.id_mat_req = reqty.id_mat_req " +
                "        INNER JOIN " +
                "    " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS itm ON reqty.fk_item = itm.id_item " +
                "        INNER JOIN " +
                "    " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS uni ON reqty.fk_unit = uni.id_unit " +
                "        LEFT JOIN " +
                "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " dmr ON reqty.id_mat_req = dmr.fid_mat_req " +
                "        AND reqty.id_ety = dmr.fid_mat_req_ety " +
                "        LEFT JOIN " +
                "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " ety ON dmr.fid_dps_year = ety.id_year " +
                "        AND dmr.fid_dps_doc = ety.id_doc " +
                "        AND dmr.fid_dps_ety = ety.id_ety " +
                "        LEFT JOIN " +
                "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " dps ON ety.id_year = dps.id_year " +
                "        AND ety.id_doc = dps.id_doc " +
                "WHERE " +
                "    req.id_mat_req = " + pkMatReq + " AND NOT reqty.b_del " +
                "        AND (dmr.fid_mat_req IS NULL " +
                "        OR (NOT ety.b_del AND NOT dps.b_del " +
                "        AND ((dps.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_EST[0] + " " +
                "        AND dps.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_EST[1] + " " +
                "        AND dps.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_EST[2] + ") " +
                "        OR (dps.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] + " " +
                "        AND dps.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[1] + " " +
                "        AND dps.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[2] + ") " +
                "        OR (dps.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[0] + " " +
                "        AND dps.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[1] + " " +
                "        AND dps.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[2] + ")))) " +
                "GROUP BY reqty.id_mat_req, reqty.id_ety;";
        
        try {
            ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql);
            ArrayList<SRowMaterialRequestDocs> lMatReqDocsRows = new ArrayList<>();
            SRowMaterialRequestDocs oMatReqEtyDocsRow;
            while (resultSet.next()) {
                oMatReqEtyDocsRow = new SRowMaterialRequestDocs();
                oMatReqEtyDocsRow.mnMatReqId = resultSet.getInt("id_mat_req");
                oMatReqEtyDocsRow.mnMatReqEtyId = resultSet.getInt("id_ety");
                oMatReqEtyDocsRow.mnItemId = resultSet.getInt("fk_item");
                oMatReqEtyDocsRow.mnUnitId = resultSet.getInt("fk_unit");
                oMatReqEtyDocsRow.msItemKey = resultSet.getString("item_key");
                oMatReqEtyDocsRow.msItem = resultSet.getString("item");
                oMatReqEtyDocsRow.msPartNumber = resultSet.getString("part_num");
                oMatReqEtyDocsRow.msUnitSymbol = resultSet.getString("symbol");
                oMatReqEtyDocsRow.msCot = SMaterialRequestUtils.formatTextWithCommas(resultSet.getString("cot"));
                oMatReqEtyDocsRow.msPed = SMaterialRequestUtils.formatTextWithCommas(resultSet.getString("ped"));
                oMatReqEtyDocsRow.msFact = SMaterialRequestUtils.formatTextWithCommas(resultSet.getString("fact"));
                        
                lMatReqDocsRows.add(oMatReqEtyDocsRow);
            }
            
            return lMatReqDocsRows;
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static Double getItemPriceCommercial(SGuiSession session, int itemPk, int unitPk) {
        double price = 0.0;
        ResultSet resultSet;
        
        String sql = "SELECT pcl.price " 
                + "FROM itmu_price_comm_log AS pcl " 
                + "INNER JOIN ( " 
                + " SELECT p.id_item, p.id_unit, MAX(p.id_log) AS _max_id_log " 
                + " FROM itmu_price_comm_log AS p " 
                + " INNER JOIN ( " 
                + "  SELECT id_item, id_unit, MAX(dt) AS _max_dt " 
                + "  FROM itmu_price_comm_log " 
                + "  WHERE NOT b_del " 
                + "  GROUP BY id_item, id_unit " 
                + "  ORDER BY id_item, id_unit) AS t1 ON t1.id_item = p.id_item AND t1.id_unit = p.id_unit AND t1._max_dt = p.dt " 
                + " WHERE NOT p.b_del " 
                + " GROUP BY p.id_item, p.id_unit " 
                + " ORDER BY p.id_item, p.id_unit) AS t2 ON t2.id_item = pcl.id_item AND t2.id_unit = pcl.id_unit AND t2._max_id_log = pcl.id_log " 
                + "WHERE NOT pcl.b_del AND pcl.id_item = " + itemPk + " AND pcl.id_unit = " + unitPk + " "  
                + "ORDER BY pcl.id_item, pcl.id_unit";
        try {
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                price = resultSet.getDouble(1);
            }
        }
        catch (Exception ex) {
            Logger.getLogger(SMaterialRequestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return price;
    }
    
    private static String formatTextWithCommas(String sText) {
        // Eliminar comas al inicio
        sText = sText.replaceAll("^,+", "");

        // Eliminar comas al final
        sText = sText.replaceAll(",+$", "");

        // Eliminar comas repetidas en el medio
        sText = sText.replaceAll(",+", ",");

        return sText;
    }
}

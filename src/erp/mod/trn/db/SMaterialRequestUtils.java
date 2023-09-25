/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.trn.form.SDialogItemPicker;
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
    
    public static ArrayList<SDataDiog> makeDiogs(final int pkYear, final Date date, final int fkUser, ArrayList<SMaterialRequestSupplyRow> lSupplies, final int idStkseg, final int user, final int userSup) {
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
                oDiog.setFkMaintMovementTypeId(SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_PART);
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
            oDiogEty.setOriginalQuantity(oSupply.getQuantity());
            oDiogEty.setOriginalValueUnitary(0d);
            oDiogEty.setSortingPosition(0);
            oDiogEty.setIsInventoriable(oSupply.getIsInventorable());
            oDiogEty.setIsDeleted(false);
            oDiogEty.setFkItemId(oSupply.getFkItemId());
            oDiogEty.setFkUnitId(oSupply.getFkUnitId());
            oDiogEty.setFkOriginalUnitId(oSupply.getFkUnitId());
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
    public static SDbMaterialCostCenterGroup getCostCenterGroupByUser(SGuiSession session, int[] pkConsSubent, int pkCc) throws Exception {
        SDbMaterialCostCenterGroup ccg = new SDbMaterialCostCenterGroup();
        Statement statement = session.getDatabase().getConnection().createStatement();
        String sql = "SELECT id_mat_cc_grp FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT_CC_CC_GRP) + " " +
                "WHERE id_mat_cons_ent = " + pkConsSubent[0] + " AND id_mat_cons_subent = " + pkConsSubent[1] + " AND id_cc = " + pkCc;
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            ccg.read(session, new int[] { resultSet.getInt(1)} );
        }
        return ccg;
    }
    
    public static SDialogItemPicker getOptionPicker(SGuiClient client, int type, int subtype, SGuiParams params) {
        String sql = "";
        ArrayList<SGridColumnForm> gridColumns = new ArrayList<SGridColumnForm>();
        SGuiOptionPickerSettings settings = null;
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
                                + "a.item_key AS " + SDbConsts.FIELD_PICK + "1, a.item AS " + SDbConsts.FIELD_PICK + "2 "
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
                                + "AND ccgu.id_link = " + SModSysConsts.USRS_LINK_USR + " "
                                + "AND ccgu.id_ref = " + params.getParamsMap().get(SModConsts.USRU_USR) + " "
                                + "ORDER BY a.item_key, a.item, a.id_item ";
                        break;
                }
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "Clave"));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Ítem"));
                settings = new SGuiOptionPickerSettings("Ítem", sql, gridColumns, 1);
                
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
            String sql = "SELECT id_mat_req FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " WHERE NOT b_del ";
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
     * @return 
     */
    public static double getQuantityLinkedOfReqEty(SGuiSession session, final int[] pkMatReqEty, final int[] dpsType, final int[] pkDpsEtyExcluded) {
        String query = "SELECT SUM(dmr.qty) AS qty_linked FROM "
                + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_MAT_REQ) + " AS dmr "
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
    
    public static double getQuantitySupplied(SGuiSession session, final int[] pkMatRequestEty) {
        String query = "SELECT " +
                        "SUM(mov_out) AS qty_supplied " +
                        "FROM " +
                        SModConsts.TablesMap.get(SModConsts.TRN_STK) + " " +
                        "WHERE " +
                        "    NOT b_del AND id_year = " + session.getCurrentYear() + " " +
                        "        AND dt <= '" + session.getCurrentDate() + "' " +
                        "        AND fid_mat_req_n = " + pkMatRequestEty[0] + " " +
                        "        AND fid_mat_req_ety_n = " + pkMatRequestEty[1] + " " +
                        "        AND fid_ct_iog = " + SDataConstantsSys.TRNS_CT_IOG_OUT +";";
        
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
                oConfig.setFkSubentMatConsumptionEntityId(resultSet.getInt("fk_subent_mat_cons_ent"));
                oConfig.setFkSubentMatConsumptionSubentityId(resultSet.getInt("fk_subent_mat_cons_subent"));
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
}

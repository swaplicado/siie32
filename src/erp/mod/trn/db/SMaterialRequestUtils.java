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
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataDiogEntry;
import erp.mtrn.data.SDataStockSegregation;
import erp.mtrn.data.SDataStockSegregationWarehouseEntry;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SMaterialRequestUtils {
    
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
                    "    NOT dety.b_del AND dety.fid_mat_req_n = " + idMaterialRequest + " " +
                    "        AND dety.fid_mat_req_ety_n = " + idMatEty + " " +
                    "        AND diog.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON[0] + " " +
                    "        AND diog.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON[1] + " " +
                    "        AND diog.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON[2] + " " +
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
                                                        res.getInt("fid_wh"),
                                                        res.getInt("fid_mat_cons_ent_n"),
                                                        res.getInt("fid_mat_sub_cons_sub_ent_n"));
                
                oSuppRow.setQuantity(res.getDouble("qty"));
                oSuppRow.setPkDiogYearId(res.getInt("id_year"));
                oSuppRow.setPkDiogDocId(res.getInt("id_doc"));
                oSuppRow.setPkDiogEtyId(res.getInt("id_ety"));
                oSuppRow.setFkMatRequestId(idMaterialRequest);
                oSuppRow.setFkMatRequestEntryId(idMatEty);
                oSuppRow.setFkConsumeEntityId_n(res.getInt("fid_mat_cons_ent_n"));
                oSuppRow.setFkSubConsumeEntityId_n(res.getInt("fid_mat_sub_cons_ent_n"));
                oSuppRow.setFkSubConsumeSubEntityId_n(res.getInt("fid_mat_sub_cons_sub_ent_n"));
                
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
                oDiog.setFkDiogCategoryId(SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON[0]);
                oDiog.setFkDiogClassId(SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON[1]);
                oDiog.setFkDiogTypeId(SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON[2]);
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
            oDiogEty.setIsInventoriable(false);
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
            oDiogEty.setFkConsumeEntityId_n(oSupply.getFkConsumeEntityId_n());
            oDiogEty.setFkSubConsumeEntityId_n(oSupply.getFkSubConsumeEntityId_n());
            oDiogEty.setFkSubConsumeSubEntityId_n(oSupply.getFkSubConsumeSubEntityId_n());
            oDiogEty.setFkUserNewId(fkUser);
                    
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
}

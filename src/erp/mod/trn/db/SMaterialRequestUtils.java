/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mtrn.data.SDataStockSegregation;
import erp.mtrn.data.SDataStockSegregationWarehouseEntry;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
}

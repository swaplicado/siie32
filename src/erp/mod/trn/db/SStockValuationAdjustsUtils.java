/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataDiogEntry;
import erp.mtrn.data.STrnStockMove;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;

import sa.lib.gui.SGuiSession;

/**
 * Utilidades para ajustar los movimientos de valuación de inventario.
 *
 * @author Edwin Carmona
 */
public abstract class SStockValuationAdjustsUtils {

    /**
     * Crea los movimientos de valuación de inventario para ajustar los costos unitarios de los movimientos provenientes de entradas con 
     * precios provisionales.
     * 
     * @param session
     * @param dateStart
     * @param dateEnd
     * @param idValuation
     * @return Lista de movimientos de valuación de inventario ajustados.
     * @throws SQLException
     * @throws Exception
     */
    public static List<SDbStockValuationMvt> createStockValuationAdjusts(SGuiSession session, Date dateStart, Date dateEnd, final int idValuation) throws SQLException, Exception {
        List<SDbStockValuationMvt> lStkValMvtToRev = new ArrayList<>();
        List<SDbStockValuationMvt> lStkValMvtAdjusts = new ArrayList<>();

        String sql = "SELECT  "
                + "    mvt.*, "
                + "    oc_e.price_u_real_r, "
                + "    oc.fid_ct_dps, "
                + "    oc.fid_cl_dps, "
                + "    oc.fid_tp_dps, "
                + "    fac_e.price_u_real_r, "
                + "    fac_e.id_year AS fact_e_id_year, "
                + "    fac_e.id_doc AS fact_e_id_doc, "
                + "    fac_e.id_ety AS fact_e_id_ety "
                + "FROM "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " AS mvt "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS oc ON mvt.fk_dps_year_in_n = oc.id_year "
                + "        AND mvt.fk_dps_doc_in_n = oc.id_doc "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS oc_e ON mvt.fk_dps_year_in_n = oc_e.id_year "
                + "        AND mvt.fk_dps_doc_in_n = oc_e.id_doc "
                + "        AND mvt.fk_dps_ety_in_n = oc_e.id_ety "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " AS supp ON mvt.fk_dps_year_in_n = supp.id_src_year "
                + "        AND mvt.fk_dps_doc_in_n = supp.id_src_doc "
                + "        AND mvt.fk_dps_ety_in_n = supp.id_src_ety "
                + "        INNER JOIN "
                + "	" + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS fac_e ON fac_e.id_year = supp.id_des_year "
                + "        AND fac_e.id_doc = supp.id_des_doc "
                + "        AND fac_e.id_ety = supp.id_des_ety "
                + "WHERE "
                + "    NOT mvt.b_del AND mvt.b_temp_price "
                + "        AND NOT mvt.b_rev "
                + "        AND mvt.fk_ct_iog = " + SModSysConsts.TRNS_CT_IOG_OUT + " "
                + "        AND oc.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] + " "
                + "        AND oc.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[1] + " "
                + "        AND oc.fid_tp_dps =  " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[2] + ";";
        
        ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql);
        SDbStockValuationMvt oMvtAdjust = null;
        SDbStockValuationMvt oMvtRevised = null;
        while (resultSet.next()) {
            oMvtRevised = new SDbStockValuationMvt();
            oMvtRevised.setPkStockValuationMvtId(resultSet.getInt("id_stk_val_mvt"));
            oMvtRevised.setRevised(true);
            
            if (resultSet.getDouble("fac_e.price_u_real_r") != resultSet.getDouble("mvt.cost_u")) {
                oMvtAdjust = new SDbStockValuationMvt();

                oMvtAdjust.setDateMove(resultSet.getDate("dt_mov"));
                oMvtAdjust.setQuantityMovement(0d);
                oMvtAdjust.setCostUnitary(resultSet.getDouble("fac_e.price_u_real_r"));
                oMvtAdjust.setCost_r(SLibUtils.round((resultSet.getDouble("fac_e.price_u_real_r") * resultSet.getDouble("qty_mov")) 
                                                    - resultSet.getDouble("mvt.cost_r"), 8));
                oMvtAdjust.setFkStockValuationId(idValuation);
                oMvtAdjust.setFkStockValuationMvtId_n(resultSet.getInt("fk_stk_val_mvt_n"));
                oMvtAdjust.setFkDiogCategoryId(resultSet.getInt("fk_ct_iog"));
                oMvtAdjust.setFkDiogYearInId_n(resultSet.getInt("fk_diog_year_in_n"));
                oMvtAdjust.setFkDiogDocInId_n(resultSet.getInt("fk_diog_doc_in_n"));
                oMvtAdjust.setFkDiogEntryInId_n(resultSet.getInt("fk_diog_ety_in_n"));
                oMvtAdjust.setFkDpsYearInId_n(resultSet.getInt("fact_e_id_year"));
                oMvtAdjust.setFkDpsDocInId_n(resultSet.getInt("fact_e_id_doc"));
                oMvtAdjust.setFkDpsEntryInId_n(resultSet.getInt("fact_e_id_ety"));
                oMvtAdjust.setFkDiogYearOutId_n(resultSet.getInt("fk_diog_year_out_n"));
                oMvtAdjust.setFkDiogDocOutId_n(resultSet.getInt("fk_diog_doc_out_n"));
                oMvtAdjust.setFkDiogEntryOutId_n(resultSet.getInt("fk_diog_ety_out_n"));
                oMvtAdjust.setFkMaterialRequestId_n(resultSet.getInt("fk_mat_req_n"));
                oMvtAdjust.setFkMaterialRequestEntryId_n(resultSet.getInt("fk_mat_req_ety_n"));
                oMvtAdjust.setFkStockValuationMvtRevisionId_n(resultSet.getInt("id_stk_val_mvt"));
                oMvtAdjust.setFkItemId(resultSet.getInt("fk_item"));
                oMvtAdjust.setFkUnitId(resultSet.getInt("fk_unit"));
                oMvtAdjust.setFkLotId(resultSet.getInt("fk_lot"));
                oMvtAdjust.setFkCompanyBranchId(resultSet.getInt("fk_cob"));
                oMvtAdjust.setFkWarehouseId(resultSet.getInt("fk_wh"));

                oMvtAdjust.save(session);

                lStkValMvtAdjusts.add(oMvtAdjust);
                oMvtRevised.setFkStockValuationMvtRevisionId_n(oMvtAdjust.getPkStockValuationMvtId());
            }
            else {
                oMvtRevised.setFkStockValuationMvtRevisionId_n(0);
            }
            
            SStockValuationAdjustsUtils.updateStockValuationMvt(session, oMvtRevised);
            lStkValMvtToRev.add(oMvtRevised);
        }
        
        return lStkValMvtAdjusts;
    }

    /**
     * Actualiza el movimiento de valuación de inventario revisado.
     * 
     * @param session
     * @param oStkValMvtRev
     * @throws SQLException
     */
    private static void updateStockValuationMvt(SGuiSession session, SDbStockValuationMvt oStkValMvtRev) throws SQLException {
        String sql = "UPDATE "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " "
                + "SET "
                + "    fk_stk_val_mvt_rev_n = " + (oStkValMvtRev.getFkStockValuationMvtRevisionId_n() == 0 ? "NULL" : oStkValMvtRev.getFkStockValuationMvtRevisionId_n()) + ","
                + "    b_rev = " + oStkValMvtRev.isRevised() + " "
                + "WHERE "
                + "    id_stk_val_mvt = " + oStkValMvtRev.getPkStockValuationMvtId() + ";";

        
        session.getStatement().getConnection().createStatement().executeUpdate(sql);
    }

    /**
     * Método para crear e insertar ajustes al inventario con cantidad 0 y el valor del ajuste.
     * 
     * @param session
     * @param dateStart
     * @param lMvtAdjusts
     * @return 
     * 
     * @throws java.sql.SQLException
     */
    public static List<SDataDiog> createDiogAdjusts(SGuiSession session, Date dateStart, List<SDbStockValuationMvt> lMvtAdjusts) throws SQLException {
        HashMap<String, SDataDiog> mDiogs = new HashMap<>();
        // Obtener el entero año a partir de la fecha de inicio con Calendar:
        int pkYear = SLibTimeUtils.digestYear(dateStart)[0];
        String warehouseKey = null;
        for (SDbStockValuationMvt oSupply : lMvtAdjusts) {
            SDataDiog oDiog = null;
            warehouseKey = oSupply.getFkCompanyBranchId() + "_" + oSupply.getFkWarehouseId();
            if (! mDiogs.containsKey(warehouseKey)) {
                oDiog = new SDataDiog();
                
                oDiog.setPkYearId(pkYear);
                oDiog.setPkDocId(0);
                oDiog.setDate(dateStart);
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
                oDiog.setFkDiogCategoryId(SModSysConsts.TRNS_TP_IOG_OUT_ADJ_INV[0]);
                oDiog.setFkDiogClassId(SModSysConsts.TRNS_TP_IOG_OUT_ADJ_INV[1]);
                oDiog.setFkDiogTypeId(SModSysConsts.TRNS_TP_IOG_OUT_ADJ_INV[2]);
                oDiog.setFkDiogAdjustmentTypeId(1);
                oDiog.setFkCompanyBranchId(oSupply.getFkCompanyBranchId());
                oDiog.setFkWarehouseId(oSupply.getFkWarehouseId());
                oDiog.setFkDpsYearId_n(0);
                oDiog.setFkDpsDocId_n(0);
                oDiog.setFkDiogYearId_n(0);
                oDiog.setFkDiogDocId_n(0);
                oDiog.setFkMfgYearId_n(0);
                oDiog.setFkMfgOrderId_n(0);
                oDiog.setFkMatRequestId_n(oSupply.getFkMaterialRequestId_n());
                oDiog.setFkBookkeepingYearId_n(0);
                oDiog.setFkBookkeepingNumberId_n(0);
                oDiog.setFkMaintMovementTypeId(SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_MAT);
                oDiog.setFkMaintUserId_n(1);
                oDiog.setFkMaintUserSupervisorId(1);
                oDiog.setFkMaintReturnUserId_n(0);
                oDiog.setFkMaintReturnUserSupervisorId(1);
                oDiog.setFkUserShippedId(1);
                oDiog.setFkUserNewId(session.getUser().getPkUserId());
                oDiog.setFkUserAuditedId(1);
                oDiog.setFkUserAuthorizedId(1);
                oDiog.setFkUserDeleteId(1);
                        
                mDiogs.put(warehouseKey, oDiog);
            }
            else {
                oDiog = mDiogs.get(warehouseKey);
            }
            
            SDataDiogEntry oDiogEty = new SDataDiogEntry();
            
            oDiogEty.setQuantity(0d);
            oDiogEty.setValueUnitary(oSupply.getCostUnitary());
            oDiogEty.setValue(oSupply.getCost_r());
            oDiogEty.setOriginalQuantity(0d);
            oDiogEty.setOriginalValueUnitary(oSupply.getCostUnitary());
            oDiogEty.setSortingPosition(0);
            oDiogEty.setIsInventoriable(true);
            oDiogEty.setIsDeleted(false);
            oDiogEty.setFkItemId(oSupply.getFkItemId());
            oDiogEty.setFkUnitId(oSupply.getFkUnitId());
            oDiogEty.setFkOriginalUnitId(oSupply.getFkUnitId());
            oDiogEty.setFkDpsYearId_n(oSupply.getFkDpsYearOutId_n());
            oDiogEty.setFkDpsDocId_n(oSupply.getFkDpsDocOutId_n());
            oDiogEty.setFkDpsEntryId_n(oSupply.getFkDpsEntryOutId_n());
            oDiogEty.setFkDpsAdjustmentYearId_n(0);
            oDiogEty.setFkDpsAdjustmentDocId_n(0);
            oDiogEty.setFkDpsAdjustmentEntryId_n(0);
            oDiogEty.setFkMfgYearId_n(0);
            oDiogEty.setFkMfgOrderId_n(0);
            oDiogEty.setFkMfgChargeId_n(0);
            oDiogEty.setFkMatRequestId_n(oSupply.getFkMaterialRequestId_n());
            oDiogEty.setFkMatRequestEtyId_n(oSupply.getFkMaterialRequestEntryId_n());
            oDiogEty.setFkUserNewId(session.getUser().getPkUserId());
            
            // year, item, unit, lot, company branch, warehouse
            oDiogEty.getAuxStockMoves().add(new STrnStockMove(new int[] { pkYear, 
                                                                            oSupply.getFkItemId(), 
                                                                            oSupply.getFkUnitId(), 
                                                                            0,
                                                                            oSupply.getFkCompanyBranchId(), 
                                                                            oSupply.getFkWarehouseId()
                                                                        }, 
                                                                        oDiogEty.getQuantity()));
            oDiog.getDbmsEntries().add(oDiogEty);
        }
        
        List<SDataDiog> lDiogs = new ArrayList<>();
        for (Map.Entry<String, SDataDiog> entry : mDiogs.entrySet()) {
            SDataDiog oDiog = entry.getValue();
            double totalValue = 0d;
            for (SDataDiogEntry oDiogEty : oDiog.getDbmsEntries()) {
                totalValue = SLibUtils.round(oDiogEty.getValue() + totalValue, 8);
            }
            oDiog.setValue_r(totalValue);
            oDiog.save(session.getStatement().getConnection());
            lDiogs.add(oDiog);
        }
        
        return lDiogs;
    }
}

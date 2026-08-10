/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.utils;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.trn.db.SDbStockValuationMvt;
import erp.mod.trn.db.SDbStockValuationMvtNote;
import erp.mod.trn.db.SStockValuationConfiguration;
import erp.mod.trn.utils.SStockValuationRecordUtils.DocNature;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataDiogEntry;
import erp.mtrn.data.STrnStockMove;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
     * Crea los movimientos de valuación de inventario para ajustar los costos
     * unitarios de los movimientos provenientes de entradas con precios
     * provisionales.
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
        List<SDbStockValuationMvt> lStkValMvtAdjusts = new ArrayList<>();
        double priceDiffPercent = 0d;
        try {
            SStockValuationConfiguration oCfg = SStockValuationUtils.getStockValuationConfig(session.getStatement().getConnection().createStatement());
            // P.ej. para el 10% se configura 0.10
            priceDiffPercent = oCfg.getDiffPricePercent();
        }
        catch (Exception e) {
            Logger.getLogger(SStockValuationAdjustsUtils.class.getName()).log(Level.SEVERE,
                    "Error al obtener el porcentaje de diferencia de precio, definido en 0",
                    e);
        }

        String sql = "SELECT  "
                + "    mvt.*, "
                + "    oc_e.id_year, "
                + "    oc_e.id_doc, "
                + "    oc_e.id_ety, "
                + "    oc_e.price_u_real_r, "
                + "    oc_e.price_u_real_cur_r AS oc_e_price_u_real_cur_r, "
                + "    oc.num_ser AS oc_num_ser, "
                + "    oc.num AS oc_num, "
                + "    oc.dt AS dt_oc, "
                + "    oc.fid_ct_dps, "
                + "    oc.fid_cl_dps, "
                + "    oc.fid_tp_dps,"
                + "    oc.fid_dps_nat, "
                + "    fac.fid_dps_nat, "
                + "    fac.num_ser AS fac_num_ser, "
                + "    fac.num AS fac_num, "
                + "    fac.dt AS dt_fac, "
                + "    fac_e.price_u_real_r, "
                + "    fac_e.price_u_real_cur_r AS fact_e_price_u_real_cur_r, "
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
                + "        INNER JOIN "
                + "	" + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS fac ON fac_e.id_year = fac.id_year "
                + "        AND fac_e.id_doc = fac.id_doc "
                + "WHERE "
                + "    NOT mvt.b_del AND mvt.b_temp_price "
                + "        AND NOT mvt.b_rev "
                + "        AND mvt.fk_ct_iog = " + SModSysConsts.TRNS_CT_IOG_OUT + " "
                + "        AND oc.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] + " "
                + "        AND oc.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[1] + " "
                + "        AND oc.fid_tp_dps =  " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[2] + " "
                + "        AND fac.dt <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' "
                + "        AND fac.fid_st_dps <> " + SModSysConsts.TRNS_ST_DPS_ANNULED + " "
                + "        AND NOT fac.b_del ";

        try (java.sql.Statement st = session.getStatement().getConnection().createStatement();
                ResultSet resultSet = st.executeQuery(sql)) {
            SDbStockValuationMvt oMvtAdjust = null;
            SDbStockValuationMvt oMvtRevised = null;
            SDbStockValuationMvtNote oMvtNote = null;
            String sComment;
            while (resultSet.next()) {
                oMvtRevised = new SDbStockValuationMvt();
                oMvtRevised.setPkStockValuationMvtId(resultSet.getInt("id_stk_val_mvt"));
                oMvtRevised.setRevised(true);

                double priceDiff = Math.abs(resultSet.getDouble("oc_e_price_u_real_cur_r") - resultSet.getDouble("fact_e_price_u_real_cur_r"));
                if (priceDiff > (resultSet.getDouble("oc_e_price_u_real_cur_r") * priceDiffPercent)) {
                    throw new Exception("El precio unitario de la entrada de la orden de compra es diferente al precio unitario de la factura del proveedor.\n"
                            + "Diferencia: " + priceDiff + " > " + (resultSet.getDouble("oc_e_price_u_real_cur_r") * priceDiffPercent) + "\n"
                            + "OC " + resultSet.getString("oc_num_ser") + " " + resultSet.getString("oc_num") + " "
                            + "con fecha: " + SLibUtils.DateFormatDate.format(resultSet.getDate("dt_oc")) + " "
                            + "[" + resultSet.getInt("oc_e.id_year") + ", "
                            + resultSet.getInt("oc_e.id_doc") + ", "
                            + resultSet.getInt("oc_e.id_ety")
                            + "] \n - "
                            + "FACT " + resultSet.getString("fac_num_ser") + " " + resultSet.getString("fac_num") + " "
                            + "con fecha: " + SLibUtils.DateFormatDate.format(resultSet.getDate("dt_fac")) + " "
                            + "[" + resultSet.getInt("fact_e_id_year") + ", "
                            + resultSet.getInt("fact_e_id_doc") + ", "
                            + resultSet.getInt("fact_e_id_ety") + " ]");
                }

                if (resultSet.getDouble("fac_e.price_u_real_r") != resultSet.getDouble("mvt.cost_u")) {
                    oMvtAdjust = new SDbStockValuationMvt();
                    sComment = "";

                    oMvtAdjust.setSystem(true);
                    oMvtAdjust.setDateMove(resultSet.getDate("dt_mov"));
                    oMvtAdjust.setQuantityMovement(0d);
                    DocNature documentNature = SStockValuationRecordUtils.getDocumentNature(session, resultSet.getInt("fact_e_id_year"), resultSet.getInt("fact_e_id_doc"));
                    if (documentNature.nature != SDataConstantsSys.TRNU_DPS_NAT_ASSET) {
                        oMvtAdjust.setCostUnitary(resultSet.getDouble("fac_e.price_u_real_r"));
                        oMvtAdjust.setCost_r(SLibUtils.round((resultSet.getDouble("fac_e.price_u_real_r") * resultSet.getDouble("qty_mov"))
                                - resultSet.getDouble("mvt.cost_r"), 8));
                        oMvtAdjust.setFkStockTypeValuationMvtId(SDbStockValuationMvt.TYPE_VAL_MVT_PRICE_ADJ);
                        // Crear comentario para el movimiento de ajuste:
                        sComment = "Ajuste de costo unitario por diferencia de la factura de proveedor. "
                                + "De OC: " + resultSet.getDouble("oc_e.price_u_real_r") + ", "
                                + "FACT: " + resultSet.getDouble("fac_e.price_u_real_r") + ", "
                                + "MVT: " + resultSet.getDouble("mvt.cost_r") + " - "
                                + "OC [" + resultSet.getInt("oc_e.id_year") + ", "
                                + resultSet.getInt("oc_e.id_doc") + ", "
                                + resultSet.getInt("oc_e.id_ety")
                                + "] - FACT [ " + resultSet.getInt("fact_e_id_year") + ", "
                                + resultSet.getInt("fact_e_id_doc") + ", "
                                + resultSet.getInt("fact_e_id_ety") + " ]";
                    }
                    else {
                        oMvtAdjust.setFkStockTypeValuationMvtId(SDbStockValuationMvt.TYPE_VAL_MVT_ASSET_ADJ);
                        // Crear comentario para el movimiento de ajuste:
                        sComment = "Ajuste de valor de activo. "
                                + "De OC: " + resultSet.getDouble("oc_e.price_u_real_r") + ", "
                                + "FACT: " + resultSet.getDouble("fac_e.price_u_real_r") + ", "
                                + "MVT: " + resultSet.getDouble("mvt.cost_r") + " - "
                                + "OC [" + resultSet.getInt("oc_e.id_year") + ", "
                                + resultSet.getInt("oc_e.id_doc") + ", "
                                + resultSet.getInt("oc_e.id_ety")
                                + "] - FACT [ " + resultSet.getInt("fact_e_id_year") + ", "
                                + resultSet.getInt("fact_e_id_doc") + ", "
                                + resultSet.getInt("fact_e_id_ety") + " ]";
                        oMvtAdjust.setCostUnitary(0d);
                        oMvtAdjust.setCost_r(0d);
                    }

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

                    oMvtAdjust.setAuxIsAdjust(true);

                    if (!sComment.isEmpty()) {
                        oMvtNote = new SDbStockValuationMvtNote();
                        oMvtNote.setNotes(sComment);
                        oMvtAdjust.getNotes().add(oMvtNote);
                    }

                    oMvtAdjust.save(session);

                    lStkValMvtAdjusts.add(oMvtAdjust);
                    oMvtRevised.setFkStockValuationMvtRevisionId_n(oMvtAdjust.getPkStockValuationMvtId());
                }
                else {
                    oMvtRevised.setFkStockValuationMvtRevisionId_n(0);
                }

                SStockValuationAdjustsUtils.updateStockValuationMvt(session, oMvtRevised);
            }

            return lStkValMvtAdjusts;
        }
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
                + "    fk_stk_val_mvt_rev_n = " + (oStkValMvtRev.getFkStockValuationMvtRevisionId_n() == 0
                        ? "NULL"
                        : oStkValMvtRev.getFkStockValuationMvtRevisionId_n()) + ","
                + "    b_rev = " + oStkValMvtRev.isRevised() + " "
                + "WHERE "
                + "    id_stk_val_mvt = " + oStkValMvtRev.getPkStockValuationMvtId() + ";";

        try (java.sql.Statement st = session.getStatement().getConnection().createStatement()) {
            st.executeUpdate(sql);
        }
    }

    /**
     * Método para crear e insertar ajustes al inventario con cantidad 0 y el
     * valor del ajuste.
     *
     * @param session
     * @param oValEndDate
     * @param lMvtAdjusts
     * @return
     *
     * @throws java.sql.SQLException
     */
    public static List<SDataDiog> createDiogAdjusts(SGuiSession session, Date oValEndDate, List<SDbStockValuationMvt> lMvtAdjusts) throws SQLException {
        HashMap<String, SDataDiog> mDiogs = new HashMap<>();
        // Sumar un día a fecha recibida:
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oValEndDate);
        calendar.add(Calendar.DATE, 1);
        Date oDiogDate = calendar.getTime();

        // Obtener el entero año a partir de la fecha de inicio con Calendar:
        int pkYear = SLibTimeUtils.digestYear(oDiogDate)[0];
        String warehouseKey = null;
        for (SDbStockValuationMvt oSupply : lMvtAdjusts) {
            SDataDiog oDiog = null;
            warehouseKey = oSupply.getFkCompanyBranchId() + "_" + oSupply.getFkWarehouseId();
            if (!mDiogs.containsKey(warehouseKey)) {
                oDiog = new SDataDiog();

                oDiog.setPkYearId(pkYear);
                oDiog.setPkDocId(0);
                oDiog.setDate(oDiogDate);
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
                oDiog.setFkMaintUserId_n(0);
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
            oDiogEty.getAuxStockMoves().add(new STrnStockMove(new int[]{pkYear,
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

    public static double getDpsEtyCostUnitary(SGuiSession session, final int idDpsYear, final int idDpsDoc, final int idDpsEty) throws Exception {
        String sql = "SELECT price_u_real_r "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " "
                + "WHERE id_year = " + idDpsYear + " AND id_doc = " + idDpsDoc + " AND id_ety = " + idDpsEty;
        try (java.sql.Statement st = session.getStatement().getConnection().createStatement();
            ResultSet resultSet = st.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getDouble("price_u_real_r");
            }
            else {
                throw new Exception("No se encontró el registro de la entrada del documento de compra.");
            }
        }
    }
}

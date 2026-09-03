/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.core;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.trn.db.SDbStockValuationKardex;
import erp.mod.trn.db.SDbStockValuationMvt;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 * Utilidades para generar ajustes de kardex de valuación de inventario.
 * <p>
 * Detecta entradas de kardex provenientes de órdenes de compra que ya tienen
 * factura asociada y cuyo costo difiere del registrado originalmente, generando
 * los movimientos de ajuste necesarios antes de iniciar el periodo de
 * valuación.
 * </p>
 *
 * @author Edwin Carmona
 */
public class SStockValuationKardexAdjustUtils {

    /**
     * Genera los ajustes de kardex para entradas de periodos anteriores al
     * inicio de la valuación cuya orden de compra ya tiene factura asociada.
     * <p>
     * Para cada entrada de kardex (OC con factura ligada) anterior a
     * {@code startDate}, calcula la cantidad y costo consumidos hasta ese
     * momento y genera los registros de ajuste correspondientes según la
     * naturaleza del documento:
     * <ul>
     * <li>Naturaleza <b>activo fijo</b>: genera un ajuste de retiro de lo
     * consumido y un ajuste de salida para llevar el costo de entrada a
     * cero.</li>
     * <li>Naturaleza <b>inventario</b>: genera un ajuste de salida por la
     * diferencia de costo en lo ya consumido, y un ajuste de entrada por la
     * diferencia de costo en el total de la entrada.</li>
     * </ul>
     * Al final actualiza la referencia principal del kardex de OC a factura
     * ({@link #updateKardexDpsMainIn}) y persiste todos los ajustes generados.
     * </p>
     *
     * @param session sesión activa de base de datos
     * @param startDate fecha de inicio del periodo de valuación; solo se
     * procesan entradas de kardex anteriores a esta fecha
     * @param cutDate fecha de corte del periodo de valuación (no se usa en la
     * consulta principal, se recibe por consistencia de firma)
     * @param idValuation ID de la valuación a la que pertenecen los ajustes
     * @return lista de movimientos de valuación ({@link SDbStockValuationMvt})
     * generados
     * @throws Exception si ocurre un error al ejecutar las consultas o al
     * guardar los registros
     */
    public static List<SDbStockValuationMvt> generateKardexAdjusts(SGuiSession session, final Date startDate, final Date cutDate, final int idValuation) throws Exception {
        String sql = "SELECT "
                + "	oe.id_year AS oc_id_year, "
                + "	oe.id_doc AS oc_id_doc, "
                + "	oe.id_ety AS oc_id_ety, "
                + "	oe.price_u_real_r AS oc_real_u, "
                + "	oe.price_u_real_cur_r AS oc_real_u_cur, "
                + "     o.fid_cur AS oc_fid_cur, "
                + "     o.exc_rate AS oc_exc_rate, "
                + "	fe.id_year AS f_id_year, "
                + "	fe.id_doc AS f_id_doc, "
                + "	fe.id_ety AS f_id_ety, "
                + "	fe.price_u_real_r AS f_real_u, "
                + "	fe.price_u_real_cur_r AS f_real_u_cur, "
                + "     trn_get_dps_nat(" + SModSysConsts.TRNS_CT_DPS_PUR + ", f.id_year, f.id_doc) AS f_nat, "
                + "     f.fid_cur AS f_fid_cur, "
                + "	f.exc_rate AS f_exc_rate, "
                + "     k.id_stk_val_kardex, "
                + "	k.fk_diog_year_in_n, "
                + "	k.fk_diog_doc_in_n, "
                + "	k.fk_diog_ety_in_n, "
                + "	k.qty_mov_in, "
                + "	k.total_in, "
                + "	k.total_in_cur, "
                + "	k.fk_item, "
                + "	k.fk_unit, "
                + "	k.fk_lot, "
                + "	k.fk_cob, "
                + "	k.fk_wh "
                + "FROM "
                + "	" + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_KARDEX) + " k "
                + "INNER JOIN trn_dps_ety oe ON "
                + "	k.fk_dps_year_in_main_n = oe.id_year "
                + "	AND k.fk_dps_doc_in_main_n = oe.id_doc "
                + "	AND k.fk_dps_ety_in_main_n = oe.id_ety "
                + "INNER JOIN trn_dps o ON "
                + "	k.fk_dps_year_in_main_n = o.id_year "
                + "	AND k.fk_dps_doc_in_main_n = o.id_doc "
                + "LEFT JOIN trn_dps_dps_supply sup ON "
                + "	sup.id_src_year = oe.id_year "
                + "	AND sup.id_src_doc = oe.id_doc "
                + "	AND sup.id_src_ety = oe.id_ety "
                + "LEFT JOIN trn_dps_ety fe ON "
                + "	sup.id_des_year = fe.id_year "
                + "	AND sup.id_des_doc = fe.id_doc "
                + "	AND sup.id_des_ety = fe.id_ety "
                + "LEFT JOIN trn_dps f ON "
                + "	sup.id_des_year = f.id_year "
                + "	AND sup.id_des_doc = f.id_doc "
                + "	AND f.b_del = 0 "
                + "WHERE "
                + "	k.b_del = 0 "
                + "	AND k.fk_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN + " "
                + "	AND o.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] + " "
                + "	AND o.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[1] + " "
                + "	AND o.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[2] + " "
                + "	AND f.id_doc IS NOT NULL "
                + "	AND k.dt_mov < '" + SLibUtils.DbmsDateFormatDate.format(startDate) + "' "
                + "ORDER BY k.dt_mov ASC;";

        List<SDbStockValuationKardex> lKardexAdjusts = new ArrayList<>();
        try (java.sql.Statement st = session.getStatement().getConnection().createStatement();
                ResultSet res = st.executeQuery(sql)) {
            while (res.next()) {
                // Precios reales de la factura (MN y moneda extranjera)
                double fPriceCur = res.getDouble("f_real_u_cur");
                double fPrice = res.getDouble("f_real_u");
                // Totales originalmente registrados en el kardex (con precio de OC)
                double totalIn = res.getDouble("total_in");
                double totalInCur = res.getDouble("total_in_cur");
                double quantityIn = res.getDouble("qty_mov_in");
                // Totales recalculados con el precio real de la factura
                double newTotalIn = SLibUtils.roundAmount(quantityIn * fPrice);
                double newTotalInCur = SLibUtils.roundAmount(quantityIn * fPriceCur);
                int ocCurrencyId = res.getInt("oc_fid_cur");
                int fCurrencyId = res.getInt("f_fid_cur");
                double fExchangeRate = res.getDouble("f_exc_rate");

                // Llaves del movimiento de almacén de entrada, de la factura y de la OC
                int[] pkDiogEtyIn = new int[]{res.getInt("fk_diog_year_in_n"), res.getInt("fk_diog_doc_in_n"), res.getInt("fk_diog_ety_in_n")};
                int[] pkInvoiceDps = new int[]{res.getInt("f_id_year"), res.getInt("f_id_doc"), res.getInt("f_id_ety")};
                int pkKardex = res.getInt("id_stk_val_kardex");
                int fkItem = res.getInt("fk_item"), fkUnit = res.getInt("fk_unit"), fkLot = res.getInt("fk_lot");
                int fkCob = res.getInt("fk_cob"), fkWh = res.getInt("fk_wh");

                // Obtener los registros de salida de kardex asociados a esta entrada de OC
                // para calcular cuánto ya fue consumido antes del periodo actual
                int[] pkMainOcDpsEtyIn = new int[]{res.getInt("oc_id_year"), res.getInt("oc_id_doc"), res.getInt("oc_id_ety")};
                List<SDbStockValuationKardex> lKardexOut = SStockValuationKardexUtils.getOutKardexOfMainDpsEty(session, pkDiogEtyIn, pkMainOcDpsEtyIn);
                double consumedQuantity = 0d, consumedCost = 0d, consumedCostCur = 0d;
                for (SDbStockValuationKardex oKardexOut : lKardexOut) {
                    consumedQuantity += oKardexOut.getQuantityOut();
                    consumedCost += oKardexOut.getTotalOut();
                    consumedCostCur += oKardexOut.getTotalOutCurrency();
                }
                consumedQuantity = SLibUtils.round(consumedQuantity, 4);
                consumedCost = SLibUtils.roundAmount(consumedCost);
                consumedCostCur = SLibUtils.roundAmount(consumedCostCur);

                if (res.getInt("f_nat") == SDataConstantsSys.TRNU_DPS_NAT_ASSET) {
                    // Naturaleza activo: todo debió entrar y consumirse en $0
                    if (consumedCost > 0) {
                        // Ajuste de retiro: reversa el costo consumido para dejarlo en $0
                        SDbStockValuationKardex oAdjust = buildAdjust(idValuation, SDbStockValuationKardex.TYPE_VAL_KARDEX_IN_RET_CONSUM,
                                pkKardex, pkDiogEtyIn, pkMainOcDpsEtyIn, pkInvoiceDps, fkItem, fkUnit, fkLot, fkCob, fkWh);
                        oAdjust.setMovDate(startDate);
                        oAdjust.setQuantityIn(0d);
                        oAdjust.setCostUnit(fPrice);
                        oAdjust.setCostUnitCurrency(fPriceCur);
                        oAdjust.setTotalIn(consumedCost);
                        oAdjust.setTotalInCurrency(consumedCostCur);
                        oAdjust.setFkDpsCurrencyInMainId_n(fCurrencyId);
                        oAdjust.setFkDpsCurrencyInOrdId_n(ocCurrencyId);
                        oAdjust.setExchangeRate(fExchangeRate);
                        SStockValuationKardexUtils.addNote(oAdjust, "ACTIVO FIJO. Ajuste a lo consumido por diferencia de costo de activo fijo.");
                        lKardexAdjusts.add(oAdjust);
                    }
                    if (totalIn > 0) {
                        // Ajuste de salida: elimina el costo de entrada registrado con precio de OC
                        SDbStockValuationKardex oAdjust = buildAdjust(idValuation, SDbStockValuationKardex.TYPE_VAL_KARDEX_OUT_ADJUST_FIX_ASSET,
                                pkKardex, pkDiogEtyIn, pkMainOcDpsEtyIn, pkInvoiceDps, fkItem, fkUnit, fkLot, fkCob, fkWh);
                        oAdjust.setMovDate(startDate);
                        oAdjust.setQuantityOut(0d);
                        oAdjust.setCostUnit(fPrice);
                        oAdjust.setCostUnitCurrency(fPriceCur);
                        oAdjust.setTotalOut(totalIn);
                        oAdjust.setTotalOutCurrency(totalInCur);
                        oAdjust.setFkDpsCurrencyInMainId_n(fCurrencyId);
                        oAdjust.setFkDpsCurrencyInOrdId_n(ocCurrencyId);
                        oAdjust.setExchangeRate(fExchangeRate);
                        SStockValuationKardexUtils.addNote(oAdjust, "ACTIVO FIJO. Ajuste de entrada al costo por activo fijo.");
                        lKardexAdjusts.add(oAdjust);
                    }
                }
                else {
                    if (consumedQuantity > 0) {
                        // Ajuste de salida: corrige la diferencia de costo en lo ya consumido
                        // (consumedQuantity * precioFactura) - costoConsumidoConPrecioOC
                        double totalAdjust = SLibUtils.roundAmount((consumedQuantity * fPrice) - consumedCost);
                        double totalAdjustCur = SLibUtils.roundAmount((consumedQuantity * fPriceCur) - consumedCostCur);
                        SDbStockValuationKardex oAdjust = buildAdjust(idValuation, SDbStockValuationKardex.TYPE_VAL_KARDEX_OUT_ADJUST_DIFF_COST,
                                pkKardex, pkDiogEtyIn, pkMainOcDpsEtyIn, pkInvoiceDps, fkItem, fkUnit, fkLot, fkCob, fkWh);
                        oAdjust.setMovDate(startDate);
                        oAdjust.setQuantityOut(0d);
                        oAdjust.setCostUnit(fPrice);
                        oAdjust.setCostUnitCurrency(fPriceCur);
                        oAdjust.setTotalOut(totalAdjust);
                        oAdjust.setTotalOutCurrency(totalAdjustCur);
                        oAdjust.setFkDpsCurrencyInMainId_n(fCurrencyId);
                        oAdjust.setFkDpsCurrencyInOrdId_n(ocCurrencyId);
                        oAdjust.setExchangeRate(fExchangeRate);
                        SStockValuationKardexUtils.addNote(oAdjust, "PED-FAC. Ajuste a lo consumido por diferencia de costo.");
                        lKardexAdjusts.add(oAdjust);
                    }
                    // Ajuste de entrada: corrige la diferencia de costo en el total de la entrada
                    // (totalConPrecioFactura) - (totalConPrecioOC)
                    double totalAdjust = SLibUtils.roundAmount(newTotalIn - totalIn);
                    double totalAdjustCur = SLibUtils.roundAmount(newTotalInCur - totalInCur);
                    if (totalAdjust != 0 || totalAdjustCur != 0) {
                        SDbStockValuationKardex oAdjust = buildAdjust(idValuation, SDbStockValuationKardex.TYPE_VAL_KARDEX_IN_ADJUST_DIFF_COST,
                                pkKardex, pkDiogEtyIn, pkMainOcDpsEtyIn, pkInvoiceDps, fkItem, fkUnit, fkLot, fkCob, fkWh);
                        oAdjust.setMovDate(startDate);
                        oAdjust.setQuantityIn(0d);
                        oAdjust.setCostUnit(fPrice);
                        oAdjust.setCostUnitCurrency(fPriceCur);
                        oAdjust.setTotalIn(totalAdjust);
                        oAdjust.setTotalInCurrency(totalAdjustCur);
                        oAdjust.setFkDpsCurrencyInMainId_n(fCurrencyId);
                        oAdjust.setFkDpsCurrencyInOrdId_n(ocCurrencyId);
                        oAdjust.setExchangeRate(fExchangeRate);
                        SStockValuationKardexUtils.addNote(oAdjust, "PED-FAC. Ajuste de entrada por diferencia de costo.");
                        lKardexAdjusts.add(oAdjust);
                    }
                }

                // Actualizar la referencia principal del kardex de OC a factura
                updateKardexDpsMainIn(session, pkDiogEtyIn, pkMainOcDpsEtyIn, pkInvoiceDps, idValuation);
            }
        }

        // Persistir todos los ajustes de kardex y convertirlos a movimientos de valuación
        List<SDbStockValuationMvt> lMvtAdjusts = new ArrayList<>();
        SDbStockValuationMvt oAdjMvt;
        for (SDbStockValuationKardex oKardex : lKardexAdjusts) {
            oKardex.save(session);
            oAdjMvt = SStockValuationKardexUtils.toMvt(oKardex);
            oAdjMvt.save(session);
            lMvtAdjusts.add(oAdjMvt);
        }

        return lMvtAdjusts;
    }

//    private static List<SDbStockValuationKardex> getPendingAdjusts(SGuiSession session) throws SQLException, Exception {
//        List<SDbStockValuationKardex> lKardexOuts = new ArrayList<>();
//        String sql = "SELECT  " +
//                    "    k.id_stk_val_kardex " +
//                    "FROM " +
//                    "    " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_KARDEX) + " AS k " +
//                    "WHERE " +
//                    "    k.adj_st = '" + SDbStockValuationKardex.ADJ_STATUS_TYPE_PENDING + "' AND k.b_del = 0 " +
//                    "        AND k.fk_ct_iog = " + SModSysConsts.TRNS_CT_IOG_OUT + ";";
//        try (Statement st = session.getStatement().getConnection().createStatement()) {
//            ResultSet res = st.executeQuery(sql);
//            while (res.next()) {
//                SDbStockValuationKardex oKardexConsumpt = new SDbStockValuationKardex(0);
//                oKardexConsumpt.read(session, new int[]{res.getInt("id_stk_val_kardex")});
//                lKardexOuts.add(oKardexConsumpt);
//            }
//        }
//
//        return lKardexOuts;
//    }

    /**
     * Registra en la tabla de log el documento principal anterior del kardex y
     * actualiza la referencia principal de la entrada de kardex de la orden de
     * compra a la factura asociada.
     * <p>
     * Esto permite trazar el cambio de referencia OC → factura para todas las
     * entradas de kardex que coincidan con {@code pkDiogIn} y
     * {@code pkOrderDps}.
     * </p>
     *
     * @param session sesión activa de base de datos
     * @param pkDiogIn llave [año, doc, entrada] del movimiento de almacén de
     * entrada
     * @param pkOrderDps llave [año, doc, entrada] del renglón de la orden de
     * compra original
     * @param pkInvoiceDps llave [año, doc, entrada] del renglón de la factura
     * destino
     * @param idValuation ID de la valuación activa
     * @throws Exception si ocurre un error al ejecutar las sentencias SQL
     */
    private static void updateKardexDpsMainIn(SGuiSession session, int[] pkDiogIn, int[] pkOrderDps, int[] pkInvoiceDps, int idValuation) throws Exception {
        String sqlLog = "INSERT INTO " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_KARDEX_MAIN_LOG)
                + " (id_log, b_sys, b_del, fk_stk_val, fk_stk_val_kardex, fk_old_dps_year, fk_old_dps_doc, fk_old_dps_ety, fk_usr_ins, fk_usr_upd, ts_usr_ins, ts_usr_upd) "
                + "SELECT (SELECT COALESCE(MAX(id_log), 0) + 1 FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_KARDEX_MAIN_LOG) + "), 0, 0, "
                + "" + idValuation + ", id_stk_val_kardex, "
                + "fk_dps_year_in_main_n, fk_dps_doc_in_main_n, fk_dps_ety_in_main_n, fk_usr_ins, fk_usr_ins, NOW(), NOW() "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_KARDEX) + " "
                + "WHERE fk_dps_year_in_main_n = " + pkOrderDps[0] + " "
                + "AND fk_dps_doc_in_main_n = " + pkOrderDps[1] + " "
                + "AND fk_dps_ety_in_main_n = " + pkOrderDps[2] + " "
                + "AND fk_diog_year_in_n = " + pkDiogIn[0] + " "
                + "AND fk_diog_doc_in_n = " + pkDiogIn[1] + " "
                + "AND fk_diog_ety_in_n = " + pkDiogIn[2] + " "
                + "AND b_del = 0;";

        String sqlUpd = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_KARDEX) + " SET "
                + "fk_dps_year_in_main_n = " + pkInvoiceDps[0] + ", "
                + "fk_dps_doc_in_main_n = " + pkInvoiceDps[1] + ", "
                + "fk_dps_ety_in_main_n = " + pkInvoiceDps[2] + " "
                + "WHERE fk_dps_year_in_main_n = " + pkOrderDps[0] + " "
                + "AND fk_dps_doc_in_main_n = " + pkOrderDps[1] + " "
                + "AND fk_dps_ety_in_main_n = " + pkOrderDps[2] + " "
                + "AND fk_diog_year_in_n = " + pkDiogIn[0] + " "
                + "AND fk_diog_doc_in_n = " + pkDiogIn[1] + " "
                + "AND fk_diog_ety_in_n = " + pkDiogIn[2] + " "
                + "AND b_del = 0;";

        try (java.sql.Statement st = session.getStatement().getConnection().createStatement()) {
            st.executeUpdate(sqlLog);
            st.executeUpdate(sqlUpd);
        }
    }

    /**
     * Construye un objeto {@link SDbStockValuationKardex} de ajuste con los
     * datos de identificación del movimiento original, sin asignar aún fechas
     * ni montos.
     *
     * @param idValuation ID de la valuación activa
     * @param type tipo de ajuste de kardex (constante
     * {@code TYPE_VAL_KARDEX_*})
     * @param pkKardex ID del registro de kardex de entrada al que se ajusta
     * @param pkDiogEtyIn llave [año, doc, entrada] del movimiento de almacén de
     * entrada
     * @param pkMainOcDpsEtyIn llave [año, doc, entrada] del renglón de la OC
     * principal
     * @param pkInvoiceDps llave [año, doc, entrada] del renglón de la factura
     * asociada
     * @param fkItem ID del artículo
     * @param fkUnit ID de la unidad
     * @param fkLot ID del lote
     * @param fkCob ID de la sucursal
     * @param fkWh ID del almacén
     * @return objeto de kardex de ajuste listo para configurar montos y guardar
     */
    private static SDbStockValuationKardex buildAdjust(
            int idValuation, int type, int pkKardex,
            int[] pkDiogEtyIn, int[] pkMainOcDpsEtyIn, int[] pkInvoiceDps,
            int fkItem, int fkUnit, int fkLot, int fkCob, int fkWh) {
        SDbStockValuationKardex o = new SDbStockValuationKardex(idValuation, type);
        o.setAuxIsAdjust(true);
//        o.setAdjustStatus(SDbStockValuationKardex.ADJ_STATUS_TYPE_PENDING);
        o.setFkStockValuationKardexId_n(pkKardex);
        o.setFkStockValuationMovementId_n(0);
        o.setFkDiogYearInId_n(pkDiogEtyIn[0]);
        o.setFkDiogDocInId_n(pkDiogEtyIn[1]);
        o.setFkDiogEntryInId_n(pkDiogEtyIn[2]);
        o.setFkDpsYearInMainId_n(pkInvoiceDps[0]);
        o.setFkDpsDocInMainId_n(pkInvoiceDps[1]);
        o.setFkDpsEntryInMainId_n(pkInvoiceDps[2]);
        o.setFkDpsYearInOrdId_n(pkMainOcDpsEtyIn[0]);
        o.setFkDpsDocInOrdId_n(pkMainOcDpsEtyIn[1]);
        o.setFkDpsEntryInOrdId_n(pkMainOcDpsEtyIn[2]);
        o.setFkItemId(fkItem);
        o.setFkUnitId(fkUnit);
        o.setFkLotId(fkLot);
        o.setFkCompanyBranchId(fkCob);
        o.setFkWarehouseId(fkWh);
        return o;
    }

}

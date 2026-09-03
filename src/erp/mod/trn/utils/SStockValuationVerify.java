/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.utils;

import erp.data.SDataConstantsSys;
import erp.mod.SModSysConsts;
import erp.mod.trn.db.SStockValuationConfiguration;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 * Utilería para verificar la integridad de la valuación de inventario.
 * <p>
 * Contiene verificaciones que detectan inconsistencias entre los movimientos de
 * almacén (trn_stk) y sus registros en la valuación (trn_stk_val_mvt), así como
 * movimientos cuya factura o artículo corresponde a activo fijo, y consumos que
 * rebasan la cantidad de entrada registrada.
 * </p>
 *
 * @author Edwin Carmona
 */
public class SStockValuationVerify {

    /**
     * Fecha de inicio a partir de la cual se aplican las verificaciones de
     * valuación.
     */
    public static final String SINCE_DATE = "2026-07-01";

    /**
     * Ejecuta todas las verificaciones de integridad de la valuación de
     * inventario y devuelve la concatenación de todos los errores encontrados.
     *
     * @param oSession sesión activa de base de datos
     * @return cadena con todos los errores encontrados, o cadena vacía si no
     * hay errores
     * @throws SQLException si ocurre un error al ejecutar alguna consulta
     */
    public static String verifyStockValuation(SGuiSession oSession) throws SQLException {
        return verifyStkInMovements(oSession)
                + verifyStkOutMovements(oSession)
                + verifyAssetAccounting(oSession)
                + verifyOrdersByDpsNature(oSession)
                + verifyOrdersWithAssetItems(oSession)
                + verifyStockValuationQtyConsumptions(oSession)
                + verifyStockValuationCostConsumptions(oSession)
                + verifyPurchaseInvoiceWithZero(oSession)
                + verifyOrdersWithoutInvoiceWithZero(oSession);
        // + verifyStockEntryValue(oSession);
    }

    /**
     * Verifica que todos los movimientos de entrada de almacén (trn_stk)
     * existan como movimiento de entrada en la valuación (trn_stk_val_mvt).
     * <p>
     * Excluye los movimientos de tipo ajuste de inventario
     * ({@code TRNS_TP_IOG_IN_ADJ_INV}), ya que estos no generan registro en
     * valuación.
     * </p>
     *
     * @param oSession sesión activa de base de datos
     * @return cadena con los errores encontrados, o cadena vacía si no hay
     * errores
     * @throws SQLException si ocurre un error al ejecutar la consulta
     */
    private static String verifyStkInMovements(SGuiSession oSession) throws SQLException {
        Logger.getLogger(SStockValuationVerify.class.getName()).info("Verificando movimientos de entrada al almacén...");
        String sErrors = "";
        String sql = "SELECT s.* FROM trn_stk s "
                + "LEFT JOIN trn_stk_val_mvt mvt ON s.fid_diog_year = mvt.fk_diog_year_in_n "
                + "AND s.fid_diog_doc = mvt.fk_diog_doc_in_n AND s.fid_diog_ety = mvt.fk_diog_ety_in_n AND mvt.b_del = 0 "
                + "LEFT JOIN trn_stk_val v ON mvt.fk_stk_val = v.id_stk_val AND v.b_del = 0 "
                + "WHERE s.id_year >= 2026 AND s.b_del = 0 "
                + "AND s.fid_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN + " "
                + "AND mvt.id_stk_val_mvt IS NULL "
                + "AND s.dt <= (SELECT COALESCE(vf.dt_end, NOW()) FROM trn_stk_val vf WHERE vf.b_del = 0 ORDER BY vf.dt_end DESC LIMIT 1) "
                + "AND NOT (fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_ADJ_INV[0] + " "
                + "         AND fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_ADJ_INV[1] + " "
                + "         AND fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_ADJ_INV[2] + ") "
                + "ORDER BY s.dt ASC;";
        ResultSet rs = oSession.getStatement().executeQuery(sql);
        while (rs.next()) {
            sErrors += "El movimiento de entrada al almacén con ID "
                    + "[" + rs.getInt("s.fid_diog_year") + ", "
                    + rs.getInt("s.fid_diog_doc") + ", "
                    + rs.getInt("s.fid_diog_ety") + "] no existe en la valuación.\n";
        }
        if (!sErrors.isEmpty()) {
            Logger.getLogger(SStockValuationVerify.class.getName()).severe(sErrors);
        }
        return sErrors;
    }

    /**
     * Verifica que todos los movimientos de salida de almacén (trn_stk) existan
     * como movimiento de salida en la valuación (trn_stk_val_mvt).
     * <p>
     * Excluye los movimientos de tipo ajuste de inventario
     * ({@code TRNS_TP_IOG_OUT_ADJ_INV}), ya que estos no generan registro en
     * valuación.
     * </p>
     *
     * @param oSession sesión activa de base de datos
     * @return cadena con los errores encontrados, o cadena vacía si no hay
     * errores
     * @throws SQLException si ocurre un error al ejecutar la consulta
     */
    private static String verifyStkOutMovements(SGuiSession oSession) throws SQLException {
        Logger.getLogger(SStockValuationVerify.class.getName()).info("Verificando movimientos de salida del almacén...");
        String sErrors = "";
        String sql = "SELECT s.* FROM trn_stk s "
                + "LEFT JOIN trn_stk_val_mvt mvt "
                + "ON  s.fid_diog_year = mvt.fk_diog_year_out_n "
                + "AND s.fid_diog_doc = mvt.fk_diog_doc_out_n "
                + "AND s.fid_diog_ety = mvt.fk_diog_ety_out_n "
                + "AND mvt.b_del = 0 "
                + "LEFT JOIN trn_stk_val v ON mvt.fk_stk_val = v.id_stk_val AND v.b_del = 0 "
                + "WHERE s.id_year >= 2026 AND s.b_del = 0 "
                + "AND s.fid_ct_iog = " + SModSysConsts.TRNS_CT_IOG_OUT + " "
                + "AND mvt.id_stk_val_mvt IS NULL "
                + "AND s.dt <= (SELECT COALESCE(vf.dt_end, NOW()) FROM trn_stk_val vf WHERE vf.b_del = 0 ORDER BY vf.dt_end DESC LIMIT 1) "
                + "AND NOT (fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_ADJ_INV[0] + " "
                + "         AND fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_ADJ_INV[1] + " "
                + "         AND fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_ADJ_INV[2] + ") "
                + "ORDER BY s.dt ASC;";
        ResultSet rs = oSession.getStatement().executeQuery(sql);
        while (rs.next()) {
            sErrors += "El movimiento de salida del almacén con ID "
                    + "[" + rs.getInt("s.fid_diog_year") + ", "
                    + rs.getInt("s.fid_diog_doc") + ", "
                    + rs.getInt("s.fid_diog_ety") + "] no existe en la valuación.\n";
        }
        if (!sErrors.isEmpty()) {
            Logger.getLogger(SStockValuationVerify.class.getName()).severe(sErrors);
        }
        return sErrors;
    }

    /**
     * Verifica que no existan movimientos de valuación cuya factura de compra
     * haya sido contabilizada en cuentas de activo fijo (rango 1200 a 1399) y
     * cuyo costo registrado sea diferente de cero.
     * <p>
     * Este caso indica que el costo de un activo fijo fue incorrectamente
     * incluido en la valuación de inventario.
     * </p>
     *
     * @param oSession sesión activa de base de datos
     * @return cadena con los errores encontrados, o cadena vacía si no hay
     * errores
     * @throws SQLException si ocurre un error al ejecutar la consulta
     */
    private static String verifyAssetAccounting(SGuiSession oSession) throws SQLException {
        Logger.getLogger(SStockValuationVerify.class.getName()).info("Verificando contabilización de activos fijos...");
        String sErrors = "";
        String sql = "SELECT DISTINCT mvt.* FROM trn_stk_val_mvt mvt "
                + "INNER JOIN trn_stk_val v ON mvt.fk_stk_val = v.id_stk_val "
                + "INNER JOIN trn_dps td ON mvt.fk_dps_year_in_n = td.id_year AND mvt.fk_dps_doc_in_n = td.id_doc "
                + "INNER JOIN ("
                + "  SELECT DISTINCT td.id_year, td.id_doc, fre.fid_acc FROM fin_rec fr "
                + "  INNER JOIN fin_rec_ety fre ON fr.id_year = fre.id_year AND fr.id_per = fre.id_per "
                + "    AND fr.id_bkc = fre.id_bkc AND fr.id_tp_rec = fre.id_tp_rec AND fr.id_num = fre.id_num "
                + "  INNER JOIN fin_acc fa ON fre.fk_acc = fa.pk_acc "
                + "  INNER JOIN trn_dps td ON fre.fid_dps_year_n = td.id_year AND fre.fid_dps_doc_n = td.id_doc "
                + "  WHERE fr.b_del = 0 AND fre.b_del = 0 AND td.b_del = 0 "
                + "    AND td.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[0] + " "
                + "    AND td.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[1] + " "
                + "    AND td.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[2] + " "
                + "    AND fre.fid_acc BETWEEN '1200-0000-0000' AND '1399-9999-9999'"
                + ") AS acc_ins ON td.id_year = acc_ins.id_year AND td.id_doc = acc_ins.id_doc "
                + "WHERE td.b_del = 0 AND mvt.b_del = 0 AND v.b_del = 0 "
                + "AND mvt.dt_mov >= '" + SINCE_DATE + "' "
                + "AND mvt.cost_r <> 0 "
                + "ORDER BY mvt.dt_mov DESC;";
        ResultSet rs = oSession.getStatement().executeQuery(sql);
        while (rs.next()) {
            sErrors += "El movimiento de almacén con ID "
                    + "[" + rs.getInt("mvt.fk_diog_year_in_n") + ", "
                    + rs.getInt("mvt.fk_diog_doc_in_n") + ", "
                    + rs.getInt("mvt.fk_diog_ety_in_n") + "]\n "
                    + "corresponde a una factura contabilizada como activo y valuada a costo diferente de cero.\n";
        }
        if (!sErrors.isEmpty()) {
            Logger.getLogger(SStockValuationVerify.class.getName()).severe(sErrors);
        }
        return sErrors;
    }

    /**
     * Verifica que no existan movimientos de entrada en la valuación
     * provenientes de facturas de compra contabilizadas como gasto (sin póliza
     * en cuentas de activo fijo 1200-1399) pero valuadas a costo cero.
     * <p>
     * Una factura de compra contabilizada como gasto debería tener un costo
     * diferente de cero en la valuación. Si el costo es cero, indica que el
     * movimiento no fue correctamente valorizado al momento de la entrada.
     * </p>
     *
     * @param oSession sesión activa de base de datos
     * @return cadena con los errores encontrados, o cadena vacía si no hay
     * errores
     * @throws SQLException si ocurre un error al ejecutar la consulta
     */
    private static String verifyPurchaseInvoiceWithZero(SGuiSession oSession) throws SQLException {
        Logger.getLogger(SStockValuationVerify.class.getName()).info("Verificando facturas de compra con costo cero...");
        String sErrors = "";
        String sql = "SELECT DISTINCT mvt.* FROM trn_stk_val_mvt mvt "
                + "INNER JOIN trn_stk_val v ON mvt.fk_stk_val = v.id_stk_val "
                + "INNER JOIN trn_dps_ety tde ON mvt.fk_dps_year_in_n = tde.id_year "
                + "  AND mvt.fk_dps_doc_in_n = tde.id_doc AND mvt.fk_dps_ety_in_n = tde.id_ety "
                + "INNER JOIN trn_dps td ON td.id_year = tde.id_year AND td.id_doc = tde.id_doc "
                // Subquery: facturas de compra sin contabilización en cuentas de activo fijo
                + "INNER JOIN ("
                + "  SELECT DISTINCT td.id_year, td.id_doc FROM trn_dps td "
                + "  WHERE td.b_del = 0 "
                + "    AND td.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[0] + " "
                + "    AND td.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[1] + " "
                + "    AND td.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[2] + " "
                + "    AND td.id_year >= 2024 "
                + "    AND NOT EXISTS ("
                + "      SELECT 1 FROM fin_rec fr "
                + "      INNER JOIN fin_rec_ety fre ON fr.id_year = fre.id_year AND fr.id_per = fre.id_per "
                + "        AND fr.id_bkc = fre.id_bkc AND fr.id_tp_rec = fre.id_tp_rec AND fr.id_num = fre.id_num "
                + "      WHERE fr.b_del = 0 AND fre.b_del = 0 "
                + "        AND fre.fid_dps_year_n = td.id_year AND fre.fid_dps_doc_n = td.id_doc "
                + "        AND fre.fid_acc BETWEEN '1200-0000-0000' AND '1399-9999-9999'"
                + "    )"
                + ") AS acc_ins ON td.id_year = acc_ins.id_year AND td.id_doc = acc_ins.id_doc "
                + "WHERE td.b_del = 0 AND mvt.b_del = 0 AND v.b_del = 0 "
                + "AND td.id_year >= 2024 "
                + "AND td.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[0] + " "
                + "AND td.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[1] + " "
                + "AND td.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[2] + " "
                + "AND mvt.fk_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN + " "
                + "AND mvt.cost_r = 0 "
                + "AND mvt.dt_mov >= '" + SINCE_DATE + "' "
                + "ORDER BY mvt.dt_mov DESC;";
        ResultSet rs = oSession.getStatement().executeQuery(sql);
        while (rs.next()) {
            sErrors += "El movimiento de almacén con ID "
                    + "[" + rs.getInt("mvt.fk_diog_year_in_n") + ", "
                    + rs.getInt("mvt.fk_diog_doc_in_n") + ", "
                    + rs.getInt("mvt.fk_diog_ety_in_n") + "] "
                    + "corresponde a una factura de compra contabilizada como gasto pero valuada a costo cero.\n";
        }
        if (!sErrors.isEmpty()) {
            Logger.getLogger(SStockValuationVerify.class.getName()).severe(sErrors);
        }
        return sErrors;
    }

    /**
     * Verifica que no existan movimientos de entrada en la valuación
     * provenientes de órdenes de compra con naturaleza predeterminada
     * ({@code TRNU_DPS_NAT_DEF}) que estén valuados a costo cero, tengan
     * importe mayor a cero en el renglón del documento, no tengan factura de
     * destino asociada y cuyos artículos no sean de categoría activo fijo
     * ({@code ITMS_CT_ITEM_ASS}).
     * <p>
     * Este caso indica que una orden de compra de inventario fue recibida en
     * almacén pero su costo no fue registrado en la valuación, posiblemente
     * porque la factura correspondiente aún no ha sido capturada o ligada.
     * </p>
     *
     * @param oSession sesión activa de base de datos
     * @return cadena con los errores encontrados, o cadena vacía si no hay
     * errores
     * @throws SQLException si ocurre un error al ejecutar la consulta
     */
    private static String verifyOrdersWithoutInvoiceWithZero(SGuiSession oSession) throws SQLException {
        Logger.getLogger(SStockValuationVerify.class.getName()).info("Verificando órdenes de compra sin factura con costo cero...");
        String sErrors = "";
        String sql = "SELECT DISTINCT mvt.* FROM trn_stk_val_mvt mvt "
                + "INNER JOIN trn_stk_val v ON mvt.fk_stk_val = v.id_stk_val "
                + "INNER JOIN trn_dps_ety tde ON mvt.fk_dps_year_in_n = tde.id_year "
                + "  AND mvt.fk_dps_doc_in_n = tde.id_doc AND mvt.fk_dps_ety_in_n = tde.id_ety "
                + "INNER JOIN trn_dps td ON td.id_year = tde.id_year AND td.id_doc = tde.id_doc "
                // Joins para obtener la categoría del artículo de referencia y del artículo del renglón
                + "LEFT JOIN erp.itmu_item ir ON tde.fid_item_ref_n = ir.id_item "
                + "LEFT JOIN erp.itmu_igen irg ON ir.fid_igen = irg.id_igen "
                + "LEFT JOIN erp.itmu_item i ON tde.fid_item = i.id_item "
                + "LEFT JOIN erp.itmu_igen ig ON i.fid_igen = ig.id_igen "
                // Join para verificar si el renglón ya tiene factura de destino ligada
                + "LEFT JOIN trn_dps_dps_supply pt ON tde.id_year = pt.id_src_year "
                + "  AND tde.id_doc = pt.id_src_doc AND tde.id_ety = pt.id_src_ety "
                + "WHERE td.b_del = 0 AND mvt.b_del = 0 AND v.b_del = 0 "
                + "AND td.id_year >= 2024 "
                + "AND td.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] + " "
                + "AND td.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[1] + " "
                + "AND td.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[2] + " "
                + "AND mvt.fk_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN + " "
                + "AND mvt.cost_r = 0 "
                + "AND tde.tot_r > 0 " // el renglón tiene importe
                + "AND pt.id_des_doc IS NULL " // no tiene factura de destino ligada
                + "AND td.fid_dps_nat = " + SDataConstantsSys.TRNU_DPS_NAT_DEF + " "
                + "AND COALESCE(irg.fid_ct_item, 0) <> " + SModSysConsts.ITMS_CT_ITEM_ASS + " "
                + "AND COALESCE(ig.fid_ct_item, 0) <> " + SModSysConsts.ITMS_CT_ITEM_ASS + " "
                + "AND mvt.dt_mov >= '" + SINCE_DATE + "' "
                + "ORDER BY mvt.dt_mov DESC;";
        ResultSet rs = oSession.getStatement().executeQuery(sql);
        while (rs.next()) {
            sErrors += "El movimiento de almacén con ID "
                    + "[" + rs.getInt("mvt.fk_diog_year_in_n") + ", "
                    + rs.getInt("mvt.fk_diog_doc_in_n") + ", "
                    + rs.getInt("mvt.fk_diog_ety_in_n") + "] "
                    + "corresponde a una orden de compra con naturaleza predeterminada valuada a costo cero.\n";
        }
        if (!sErrors.isEmpty()) {
            Logger.getLogger(SStockValuationVerify.class.getName()).severe(sErrors);
        }
        return sErrors;
    }

    /**
     * Verifica que no existan movimientos de valuación provenientes de órdenes
     * de compra con naturaleza de activo ({@code TRNU_DPS_NAT_ASSET}) cuyo
     * costo sea diferente de cero y cuya factura de destino no esté
     * contabilizada en cuentas de activo fijo.
     * <p>
     * Detecta órdenes de compra que fueron clasificadas como activo pero cuya
     * factura asociada no refleja esa contabilización.
     * </p>
     *
     * @param oSession sesión activa de base de datos
     * @return cadena con los errores encontrados, o cadena vacía si no hay
     * errores
     * @throws SQLException si ocurre un error al ejecutar la consulta
     */
    private static String verifyOrdersByDpsNature(SGuiSession oSession) throws SQLException {
        Logger.getLogger(SStockValuationVerify.class.getName()).info("Verificando órdenes de compra por naturaleza de documento...");
        String sErrors = "";
        String sql = "SELECT DISTINCT mvt.* FROM trn_stk_val_mvt mvt "
                + "INNER JOIN trn_stk_val v ON mvt.fk_stk_val = v.id_stk_val "
                + "INNER JOIN trn_dps td ON mvt.fk_dps_year_in_n = td.id_year AND mvt.fk_dps_doc_in_n = td.id_doc "
                + "INNER JOIN trn_dps_ety tde ON td.id_year = tde.id_year AND td.id_doc = tde.id_doc "
                + "LEFT JOIN erp.itmu_item ir ON tde.fid_item_ref_n = ir.id_item "
                + "LEFT JOIN erp.itmu_igen irg ON ir.fid_igen = irg.id_igen "
                + "LEFT JOIN erp.itmu_item i ON tde.fid_item = i.id_item "
                + "LEFT JOIN erp.itmu_igen ig ON i.fid_igen = ig.id_igen "
                + "LEFT JOIN trn_dps_dps_supply pt ON tde.id_year = pt.id_src_year AND tde.id_doc = pt.id_src_doc AND tde.id_ety = pt.id_src_ety "
                + "LEFT JOIN ("
                + "  SELECT DISTINCT td.id_year, td.id_doc FROM fin_rec fr "
                + "  INNER JOIN fin_rec_ety fre ON fr.id_year = fre.id_year AND fr.id_per = fre.id_per "
                + "    AND fr.id_bkc = fre.id_bkc AND fr.id_tp_rec = fre.id_tp_rec AND fr.id_num = fre.id_num "
                + "  INNER JOIN fin_acc fa ON fre.fk_acc = fa.pk_acc "
                + "  INNER JOIN trn_dps td ON fre.fid_dps_year_n = td.id_year AND fre.fid_dps_doc_n = td.id_doc "
                + "  WHERE fr.b_del = 0 AND fre.b_del = 0 AND td.b_del = 0 "
                + "    AND td.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[0] + " "
                + "    AND td.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[1] + " "
                + "    AND td.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[2] + " "
                + "    AND fre.fid_acc BETWEEN '1200-0000-0000' AND '1399-9999-9999'"
                + ") AS acc_ins ON pt.id_des_year = acc_ins.id_year AND pt.id_des_doc = acc_ins.id_doc "
                + "WHERE td.b_del = 0 AND mvt.b_del = 0 AND v.b_del = 0 "
                + "AND td.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] + " "
                + "AND td.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[1] + " "
                + "AND td.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[2] + " "
                + "AND acc_ins.id_doc IS NULL "
                + "AND mvt.cost_r <> 0 "
                + "AND td.fid_dps_nat = " + SDataConstantsSys.TRNU_DPS_NAT_ASSET + " "
                + "AND mvt.dt_mov >= '" + SINCE_DATE + "' "
                + "ORDER BY mvt.dt_mov DESC;";
        ResultSet rs = oSession.getStatement().executeQuery(sql);
        while (rs.next()) {
            sErrors += "El movimiento de almacén con ID "
                    + "[" + rs.getInt("mvt.fk_diog_year_in_n") + ", "
                    + rs.getInt("mvt.fk_diog_doc_in_n") + ", "
                    + rs.getInt("mvt.fk_diog_ety_in_n") + "]\n "
                    + "corresponde a una orden de compra con naturaleza activo y valuada a costo diferente de cero.\n";
        }
        if (!sErrors.isEmpty()) {
            Logger.getLogger(SStockValuationVerify.class.getName()).severe(sErrors);
        }
        return sErrors;
    }

    /**
     * Verifica que no existan movimientos de salida de valuación provenientes
     * de órdenes de compra cuyos artículos pertenezcan a la categoría de activo
     * fijo ({@code ITMS_CT_ITEM_ASS}), con costo diferente de cero y sin
     * factura de destino contabilizada en cuentas de activo.
     * <p>
     * Complementa {@link #verifyOrdersByDpsNature} detectando el caso en que la
     * naturaleza de activo no está en el documento sino en el artículo mismo.
     * </p>
     *
     * @param oSession sesión activa de base de datos
     * @return cadena con los errores encontrados, o cadena vacía si no hay
     * errores
     * @throws SQLException si ocurre un error al ejecutar la consulta
     */
    private static String verifyOrdersWithAssetItems(SGuiSession oSession) throws SQLException {
        Logger.getLogger(SStockValuationVerify.class.getName()).info("Verificando órdenes de compra con ítems de activo fijo...");
        String sErrors = "";
        String sql = "SELECT DISTINCT mvt.*, acc_ins.id_doc FROM trn_stk_val_mvt mvt "
                + "INNER JOIN trn_stk_val v ON mvt.fk_stk_val = v.id_stk_val "
                + "INNER JOIN trn_dps td ON mvt.fk_dps_year_in_n = td.id_year AND mvt.fk_dps_doc_in_n = td.id_doc "
                + "INNER JOIN trn_dps_ety tde ON td.id_year = tde.id_year AND td.id_doc = tde.id_doc "
                + "LEFT JOIN erp.itmu_item ir ON tde.fid_item_ref_n = ir.id_item "
                + "LEFT JOIN erp.itmu_igen irg ON ir.fid_igen = irg.id_igen "
                + "LEFT JOIN erp.itmu_item i ON tde.fid_item = i.id_item "
                + "LEFT JOIN erp.itmu_igen ig ON i.fid_igen = ig.id_igen "
                + "LEFT JOIN trn_dps_dps_supply pt ON tde.id_year = pt.id_src_year AND tde.id_doc = pt.id_src_doc AND tde.id_ety = pt.id_src_ety "
                + "LEFT JOIN ("
                + "  SELECT DISTINCT td.id_year, td.id_doc FROM fin_rec fr "
                + "  INNER JOIN fin_rec_ety fre ON fr.id_year = fre.id_year AND fr.id_per = fre.id_per "
                + "    AND fr.id_bkc = fre.id_bkc AND fr.id_tp_rec = fre.id_tp_rec AND fr.id_num = fre.id_num "
                + "  INNER JOIN fin_acc fa ON fre.fk_acc = fa.pk_acc "
                + "  INNER JOIN trn_dps td ON fre.fid_dps_year_n = td.id_year AND fre.fid_dps_doc_n = td.id_doc "
                + "  WHERE fr.b_del = 0 AND fre.b_del = 0 AND td.b_del = 0 "
                + "    AND td.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[0] + " "
                + "    AND td.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[1] + " "
                + "    AND td.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[2] + " "
                + "    AND fre.fid_acc BETWEEN '1200-0000-0000' AND '1399-9999-9999'"
                + ") AS acc_ins ON pt.id_des_year = acc_ins.id_year AND pt.id_des_doc = acc_ins.id_doc "
                + "WHERE td.b_del = 0 AND mvt.b_del = 0 AND v.b_del = 0 "
                + "AND td.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] + " "
                + "AND td.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[1] + " "
                + "AND td.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[2] + " "
                + "AND mvt.fk_ct_iog = " + SModSysConsts.TRNS_CT_IOG_OUT + " "
                + "AND mvt.cost_r <> 0 AND acc_ins.id_doc IS NULL "
                + "AND td.fid_dps_nat = " + SDataConstantsSys.TRNU_DPS_NAT_ASSET + " "
                + "AND mvt.dt_mov >= '" + SINCE_DATE + "' "
                + "AND (COALESCE(irg.fid_ct_item, 0) = " + SModSysConsts.ITMS_CT_ITEM_ASS + " OR COALESCE(ig.fid_ct_item, 0) = " + SModSysConsts.ITMS_CT_ITEM_ASS + ");";
        ResultSet rs = oSession.getStatement().executeQuery(sql);
        while (rs.next()) {
            sErrors += "El movimiento de almacén con ID "
                    + "[" + rs.getInt("mvt.fk_diog_year_in_n") + ", "
                    + rs.getInt("mvt.fk_diog_doc_in_n") + ", "
                    + rs.getInt("mvt.fk_diog_ety_in_n") + "]\n "
                    + "corresponde a una orden de compra con ítems de activo fijo y valuada con costo diferente de cero.\n";
        }
        if (!sErrors.isEmpty()) {
            Logger.getLogger(SStockValuationVerify.class.getName()).severe(sErrors);
        }
        return sErrors;
    }

    /**
     * Verifica que ningún movimiento de entrada en la valuación tenga una
     * cantidad total de consumos (salidas) mayor a su cantidad de entrada.
     * <p>
     * Agrupa por el identificador del movimiento de entrada (diog_year,
     * diog_doc, diog_ety) y compara la suma de entradas contra la suma de
     * salidas. Un resultado positivo indica que se consumió más de lo que
     * entró, lo cual es un error de integridad.
     * </p>
     *
     * @param oSession sesión activa de base de datos
     * @return cadena con los errores encontrados, o cadena vacía si no hay
     * errores
     * @throws SQLException si ocurre un error al ejecutar la consulta
     */
    private static String verifyStockValuationQtyConsumptions(SGuiSession oSession) throws SQLException {
        Logger.getLogger(SStockValuationVerify.class.getName()).info("Verificando cantidades consumidas vs entradas...");
        String sErrors = "";
        String sql = "SELECT "
                + "  i.item_key, "
                + "  mvt.fk_item, "
                + "  mvt.fk_unit, "
                + "  mvt.fk_diog_year_in_n, "
                + "  mvt.fk_diog_doc_in_n, "
                + "  mvt.fk_diog_ety_in_n, "
                + "  sum(IF (mvt.fk_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN + ", mvt.qty_mov, 0)) AS mvt_in, "
                + "  sum(IF (mvt.fk_ct_iog = " + SModSysConsts.TRNS_CT_IOG_OUT + ", mvt.qty_mov, 0)) AS mvt_out "
                + "FROM trn_stk_val_mvt mvt "
                + "INNER JOIN trn_stk_val v ON mvt.fk_stk_val = v.id_stk_val "
                + "INNER JOIN erp.itmu_item i ON mvt.fk_item = i.id_item "
                + "INNER JOIN trn_diog diog_in ON mvt.fk_diog_year_in_n = diog_in.id_year AND mvt.fk_diog_doc_in_n = diog_in.id_doc "
                + "WHERE mvt.b_del = 0 AND v.b_del = 0 "
                + "AND mvt.fk_diog_doc_in_n IS NOT NULL "
                + "AND diog_in.dt >= '" + SINCE_DATE + "' "
                + "GROUP BY mvt.fk_diog_year_in_n, mvt.fk_diog_doc_in_n, mvt.fk_diog_ety_in_n "
                + "HAVING mvt_out > mvt_in;";
        ResultSet rs = oSession.getStatement().executeQuery(sql);
        while (rs.next()) {
            sErrors += "El movimiento de entrada de almacén con ID "
                    + "[" + rs.getInt("mvt.fk_diog_year_in_n") + ", "
                    + rs.getInt("mvt.fk_diog_doc_in_n") + ", "
                    + rs.getInt("mvt.fk_diog_ety_in_n") + "] \n "
                    + "tiene una cantidad de consumos que rebasan la cantidad de entrada.\n";
        }
        if (!sErrors.isEmpty()) {
            Logger.getLogger(SStockValuationVerify.class.getName()).severe(sErrors);
        }
        return sErrors;
    }

    /**
     * Verifica que ningún movimiento de entrada en la valuación tenga un costo
     * total de consumos (salidas) mayor a su costo de entrada.
     * <p>
     * Agrupa por el identificador del movimiento de entrada (diog_year,
     * diog_doc, diog_ety) y compara la suma de costos de entradas contra la
     * suma de costos de salidas. Un resultado positivo indica que se consumió
     * más valor del que entró, lo cual es un error de integridad.
     * </p>
     *
     * @param oSession sesión activa de base de datos
     * @return cadena con los errores encontrados, o cadena vacía si no hay
     * errores
     * @throws SQLException si ocurre un error al ejecutar la consulta
     */
    private static String verifyStockValuationCostConsumptions(SGuiSession oSession) throws SQLException {
        Logger.getLogger(SStockValuationVerify.class.getName()).info("Verificando costos consumidos vs entradas...");
        String sErrors = "";
        String sLog = "";
        boolean debugMovimientosCostos = false; // cambiar a true para inspeccionar los movimientos individuales

        String sql = "SELECT "
                + "	i.item_key, "
                + "	i.item, "
                + "	mvt.fk_item, "
                + "	mvt.fk_unit, "
                + "	mvt.fk_diog_year_in_n,  "
                + "	mvt.fk_diog_doc_in_n,  "
                + "	mvt.fk_diog_ety_in_n, "
                + "	SUM(IF (mvt.fk_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN + ", mvt.cost_r, 0)) AS cost_in, "
                + "	SUM(IF (mvt.fk_ct_iog = " + SModSysConsts.TRNS_CT_IOG_OUT + ", mvt.cost_r, 0)) AS cost_out "
                + "FROM "
                + "	trn_stk_val_mvt mvt "
                + "INNER JOIN trn_stk_val v ON mvt.fk_stk_val = v.id_stk_val "
                + "INNER JOIN erp.itmu_item i ON mvt.fk_item = i.id_item "
                + "INNER JOIN trn_diog diog_in ON mvt.fk_diog_year_in_n = diog_in.id_year AND mvt.fk_diog_doc_in_n = diog_in.id_doc "
                + "WHERE "
                + "	mvt.b_del = 0 "
                + "	AND v.b_del = 0 "
                + "	AND diog_in.dt >= '" + SINCE_DATE + "' "
                + "GROUP BY mvt.fk_diog_year_in_n, mvt.fk_diog_doc_in_n, mvt.fk_diog_ety_in_n "
                + "HAVING cost_out > cost_in AND ABS(cost_in - cost_out) > 1;";

        ResultSet rs = oSession.getStatement().executeQuery(sql);
        while (rs.next()) {
            int yearIn = rs.getInt("mvt.fk_diog_year_in_n");
            int docIn = rs.getInt("mvt.fk_diog_doc_in_n");
            int etyIn = rs.getInt("mvt.fk_diog_ety_in_n");
            double costIn = rs.getDouble("cost_in");
            double costOut = rs.getDouble("cost_out");

            sErrors += "El movimiento de entrada de almacén con ID "
                    + "[" + yearIn + ", " + docIn + ", " + etyIn + "] \n "
                    + "tiene un costo de consumos que rebasan el costo de entrada.\n";
            sLog = "Item: " + rs.getString("item_key") + " - " + rs.getString("item") + "\n"
                    + "Entradas: $" + costIn + ", salidas: $" + costOut;
            sErrors += sLog + "\n";

            if (debugMovimientosCostos) {
                StringBuilder sbDetails = new StringBuilder();
                sbDetails.append("Movimientos individuales para [")
                        .append(yearIn).append(", ")
                        .append(docIn).append(", ")
                        .append(etyIn).append("]:\n");

                String sqlMovs = "SELECT "
                        + "    mvt.*, "
                        + "    i.item_key, "
                        + "    i.item "
                        + "FROM trn_stk_val_mvt mvt "
                        + "INNER JOIN erp.itmu_item i ON mvt.fk_item = i.id_item "
                        + "WHERE mvt.b_del = 0 "
                        + "AND mvt.fk_diog_year_in_n = ? "
                        + "AND mvt.fk_diog_doc_in_n = ? "
                        + "AND mvt.fk_diog_ety_in_n = ? "
                        + "ORDER BY mvt.dt_mov ASC;";

                java.sql.PreparedStatement psMovs
                        = oSession.getStatement().getConnection().prepareStatement(sqlMovs);
                psMovs.setInt(1, yearIn);
                psMovs.setInt(2, docIn);
                psMovs.setInt(3, etyIn);

                ResultSet rsMovs = psMovs.executeQuery();
                while (rsMovs.next()) {
                    sbDetails.append("  - tipo=").append(rsMovs.getInt("fk_ct_iog"))
                            .append(" | qty=").append(rsMovs.getDouble("qty_mov"))
                            .append(" | cost=").append(rsMovs.getDouble("cost_r"))
                            .append(" | fecha=").append(rsMovs.getDate("dt_mov"))
                            .append(" | doc_in=[").append(rsMovs.getInt("fk_diog_year_in_n"))
                            .append(", ").append(rsMovs.getInt("fk_diog_doc_in_n"))
                            .append(", ").append(rsMovs.getInt("fk_diog_ety_in_n")).append("]\n");
                }

                rsMovs.close();
                psMovs.close();

                sErrors += sbDetails.toString() + "\n";
            }
        }

        if (!sErrors.isEmpty()) {
            Logger.getLogger(SStockValuationVerify.class.getName()).severe(sErrors);
        }
        return sErrors;
    }

    /**
     * Verifica que los movimientos de entrada de almacén tengan un valor
     * unitario consistente con el documento de origen (orden de compra o
     * factura).
     * <p>
     * Para entradas con naturaleza de inventario (nat = 1), compara el costo
     * unitario del movimiento ({@code trn_stk.cost_u}) contra el precio
     * unitario real del renglón del documento
     * ({@code trn_dps_ety.price_u_real_r}). Para entradas con naturaleza de
     * activo fijo (nat = 2), detecta movimientos que tengan cargo o abono
     * contable ({@code debit > 0} o {@code credit > 0}), lo cual indica que
     * fueron incorrectamente valuados.
     * </p>
     *
     * @param oSession sesión activa de base de datos
     * @return cadena con los errores encontrados, o cadena vacía si no hay
     * errores
     * @throws SQLException si ocurre un error al ejecutar la consulta
     */
    private static String verifyStockEntryValue(SGuiSession oSession) throws SQLException {
        Logger.getLogger(SStockValuationVerify.class.getName()).info("Verificando valores de entradas de almacén...");
        double priceDiffPercent = 0d;
        try {
            SStockValuationConfiguration oCfg = SStockValuationUtils.getStockValuationConfig(oSession.getStatement().getConnection().createStatement());
            // P.ej. para el 10% se configura 0.10
            priceDiffPercent = oCfg.getDiffPricePercent();
        }
        catch (Exception e) {
            Logger.getLogger(SStockValuationUtils.class.getName()).log(Level.SEVERE,
                    "Error al obtener el porcentaje de diferencia de precio, definido en 0",
                    e);
        }
        SStockValuationUpdateStkUtils.updateStockInRowsSinceDate(oSession, SINCE_DATE);
        String sErrors = "";
        String sql = "SELECT "
                + "  ie.id_year, "
                + "  ie.id_doc, "
                + "  ie.id_ety, "
                + "  i.dt, "
                + "  i.num, "
                + "  trn_get_dps_nat(" + SModSysConsts.TRNS_CT_DPS_PUR + ", d.id_year, d.id_doc) = 2 AS is_asset_nat, "
                + "  de.id_year AS dps_year, "
                + "  de.id_doc AS dps_doc, "
                + "  de.id_ety AS dps_ety, "
                + "  d.num_ser AS dps_num_ser, "
                + "  d.num AS dps_num, "
                + "  d.dt AS dps_dt, "
                + "  d.fid_ct_dps, "
                + "  d.fid_cl_dps, "
                + "  d.fid_tp_dps, "
                + "  ie.fid_item, "
                + "  ie.fid_unit, "
                + "  ie.val_u, "
                + "  ie.qty, "
                + "  ie.orig_qty, "
                + "  ie.val, "
                + "  s.cost_u, "
                + "  de.fid_item, "
                + "  de.fid_unit, "
                + "  de.qty, "
                + "  de.price_u_real_r "
                + "FROM trn_diog i "
                + "INNER JOIN trn_diog_ety ie ON i.id_year = ie.id_year AND i.id_doc = ie.id_doc "
                + "INNER JOIN trn_stk s ON s.fid_diog_year = ie.id_year AND s.fid_diog_doc = ie.id_doc AND s.fid_diog_ety = ie.id_ety "
                + "INNER JOIN trn_stk_val_mvt mvt ON mvt.fk_diog_year_in_n = ie.id_year AND mvt.fk_diog_doc_in_n = ie.id_doc AND mvt.fk_diog_ety_in_n = ie.id_ety "
                + " AND mvt.fk_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN + " AND mvt.b_del = 0 "
                + "INNER JOIN trn_stk_val v ON v.id_stk_val = mvt.fk_stk_val AND v.b_del = 0 "
                + "INNER JOIN trn_dps_ety de ON ie.fid_dps_year_n = de.id_year AND ie.fid_dps_doc_n = de.id_doc AND ie.fid_dps_ety_n = de.id_ety "
                + "INNER JOIN trn_dps d ON de.id_year = d.id_year AND de.id_doc = d.id_doc "
                + "WHERE i.dt >= '" + SINCE_DATE + "' ";

        sql += "AND ( "
                // Si la naturaleza del documento es predeterminada y la diferencia del costo excede el porcentaje configurado
                + "  (trn_get_dps_nat(" + SModSysConsts.TRNS_CT_DPS_PUR + ", d.id_year, d.id_doc) = 1 AND "
                + "     ROUND(ABS(s.cost_u - de.price_u_real_r), 2) > ROUND((de.price_u_real_r * " + priceDiffPercent + "), 2)) "
                // O si la naturaleza del documento es activo fijo y tiene cargo o abono contable
                + "  OR (trn_get_dps_nat(" + SModSysConsts.TRNS_CT_DPS_PUR + ", d.id_year, d.id_doc) = 2 AND (s.debit > 0 OR s.credit > 0)) "
                + ") "
                + "AND i.fid_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN + " "
                + "AND i.b_del = 0 AND ie.b_del = 0 AND s.b_del = 0;";
        ResultSet rs = oSession.getStatement().executeQuery(sql);
        while (rs.next()) {
            sErrors += "El movimiento de almacén con ID "
                    + "[" + rs.getInt("ie.id_year") + ", "
                    + rs.getInt("ie.id_doc") + ", "
                    + rs.getInt("ie.id_ety") + "] "
                    + "precio unitario almacén [" + rs.getDouble("s.cost_u") + "] vs "
                    + "precio documento [" + rs.getDouble("de.price_u_real_r") + "].\n "
                    + "Folio documento: " + rs.getString("dps_num_ser") + " " + rs.getString("dps_num") + ", "
                    + "fecha: " + SLibUtils.DateFormatDate.format(rs.getDate("dps_dt")) + " " + ", "
                    + "no tiene un valor de entrada consistente con su documento de origen.\n";
        }
        if (!sErrors.isEmpty()) {
            Logger.getLogger(SStockValuationVerify.class.getName()).severe(sErrors);
        }
        return sErrors;
    }
}

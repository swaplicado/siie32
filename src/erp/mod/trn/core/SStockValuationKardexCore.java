/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.core;

import erp.mod.SModSysConsts;
import erp.mod.trn.db.SDbStockKardexEntry;
import erp.mod.trn.db.SDbStockValuationKardex;
import erp.mod.trn.db.SDbStockValuationMvt;
import erp.mod.trn.db.SRowKardexRemaining;
import erp.mod.trn.db.SStockValuationConfiguration;
import erp.mod.trn.utils.SStockValuationUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiSession;

/**
 * Núcleo del proceso de kardex para la valuación de inventario.
 * <p>
 * Contiene los métodos principales para crear entradas de kardex a partir de
 * movimientos de almacén, generar las salidas de kardex por consumo PEPS y
 * construir los movimientos de valuación ({@link SDbStockValuationMvt}) a
 * partir de los registros de kardex ya persistidos.
 * </p>
 *
 * @author Edwin Carmona
 */
public abstract class SStockValuationKardexCore {

    /**
     * Crea y persiste las entradas de kardex para todos los movimientos de
     * entrada de almacén dentro del rango de fechas indicado.
     * <p>
     * Obtiene los movimientos de entrada ({@code TRNS_CT_IOG_IN}) mediante
     * {@link SStockValuationUtils#getStockMovementsQuery} y por cada uno llama
     * a {@link #createKardexEntry} para resolver el costo real y guardar el
     * registro. Se omiten entradas duplicadas detectadas por
     * {@link SStockValuationKardexUtils#existsKardexEntry}.
     * </p>
     *
     * @param session sesión activa de base de datos
     * @param startDate fecha de inicio del rango de movimientos a procesar
     * @param cutDate fecha de corte del rango de movimientos a procesar
     * @throws Exception si ocurre un error al resolver costos o al guardar
     * registros
     */
    public static void createKardexEntries(SGuiSession session, final Date startDate, final Date cutDate) throws Exception {
        String sql;
        double priceDiffPercent = 0d;
        try {
            SStockValuationConfiguration oCfg = SStockValuationUtils.getStockValuationConfig(session.getStatement().getConnection().createStatement());
            // P.ej. para el 10% se configura 0.10
            priceDiffPercent = oCfg.getDiffPricePercent();
        }
        catch (Exception e) {
            Logger.getLogger(SStockValuationUtils.class.getName()).log(Level.SEVERE,
                    "Error al obtener el porcentaje de diferencia de precio, definido en 0",
                    e);
        }
        try (Statement st = session.getStatement().getConnection().createStatement()) {
            sql = SStockValuationUtils.getStockMovementsQuery(st, SModSysConsts.TRNS_CT_IOG_IN, startDate, cutDate, true);
            ResultSet res = st.executeQuery(sql);
            SDbStockKardexEntry oKardexEntry;
            while (res.next()) {
                oKardexEntry = createKardexEntry(session, res, priceDiffPercent, 0, 0);
            }
        }
    }

    /**
     * Crea y opcionalmente persiste una entrada de kardex a partir de un
     * renglón de resultado de movimiento de almacén de entrada.
     * <p>
     * Resuelve el costo real de la entrada mediante
     * {@link SStockValuationKardexUtils#resolveEntryCost}, que determina si el
     * costo proviene de la OC, de la factura asociada o se establece en cero
     * por naturaleza de activo. Si ya existe un registro de kardex para el
     * mismo movimiento de almacén, omite la inserción y registra un aviso en
     * consola.
     * </p>
     *
     * @param session sesión activa de base de datos
     * @param res {@link ResultSet} posicionado en el movimiento de entrada a
     * procesar
     * @param priceDiffPercent porcentaje máximo de diferencia de precio
     * permitido entre OC y factura
     * @param idValuationEntry ID del movimiento de valuación asociado (0 si aún
     * no existe)
     * @param idValuation ID de la valuación activa (0 si se crea fuera de una
     * valuación)
     * @return objeto {@link SDbStockKardexEntry} creado y guardado (o solo
     * creado si era duplicado)
     * @throws SQLException si ocurre un error al leer el {@code ResultSet}
     * @throws Exception si ocurre un error al resolver el costo o al guardar el
     * registro
     */
    public static SDbStockKardexEntry createKardexEntry(SGuiSession session,
            ResultSet res,
            double priceDiffPercent,
            int idValuationEntry,
            int idValuation) throws SQLException, Exception {
        SDbStockKardexEntry oKardexEntry = new SDbStockKardexEntry(res.getDate("dt"),
                res.getInt("fid_diog_year"),
                res.getInt("fid_diog_doc"),
                res.getInt("fid_diog_ety"),
                idValuation);

        // Inicializar valores con los datos del movimiento de almacén (costo provisional)
        oKardexEntry.setFkStockValuationMovementId_n(idValuationEntry);
        double qty = res.getDouble("mov_in");
        double costU = res.getDouble("cost_u");   // costo unitario registrado en trn_stk
        double totalIn = res.getDouble("debit");    // importe total registrado en trn_stk

        oKardexEntry.setQuantityIn(qty);
        oKardexEntry.setQuantityOut(0);
        oKardexEntry.setCostUnit(costU);
        oKardexEntry.setCostUnitCurrency(costU);
        oKardexEntry.setTotalIn(totalIn);
        oKardexEntry.setTotalInCurrency(totalIn);
        oKardexEntry.setTotalOut(0);
        oKardexEntry.setTotalOutCurrency(0);
        oKardexEntry.setExchangeRate(1d);
        oKardexEntry.setFkItemId(res.getInt("id_item"));
        oKardexEntry.setFkUnitId(res.getInt("id_unit"));
        oKardexEntry.setFkLotId(res.getInt("id_lot"));
        oKardexEntry.setFkCompanyBranchId(res.getInt("id_cob"));
        oKardexEntry.setFkWarehouseId(res.getInt("id_wh"));
        oKardexEntry.setAuxTypeDpsIn(new int[]{res.getInt("fid_ct_dps"),
            res.getInt("fid_cl_dps"),
            res.getInt("fid_tp_dps")});

        // Resolver el costo real: determina si usar precio de OC, factura o cero (activo)
        SStockValuationKardexUtils.SResolvedEntryCost rc = SStockValuationKardexUtils.resolveEntryCost(
                session, res, priceDiffPercent, qty, costU, totalIn, oKardexEntry.getAuxTypeDpsIn());

        oKardexEntry.setCostUnit(rc.costUnit);
        oKardexEntry.setCostUnitCurrency(rc.costUnitCur);
        oKardexEntry.setTotalIn(rc.totalIn);
        oKardexEntry.setTotalInCurrency(rc.totalInCur);
        oKardexEntry.setExchangeRate(rc.exchangeRate);
        oKardexEntry.setFkDpsCurrencyInMainId_n(rc.fkCurrencyId);
        oKardexEntry.setFkDpsYearInMainId_n(rc.fkDpsYearMain);
        oKardexEntry.setFkDpsDocInMainId_n(rc.fkDpsDocMain);
        oKardexEntry.setFkDpsEntryInMainId_n(rc.fkDpsEtyMain);
        oKardexEntry.setFkDpsYearInOrdId_n(rc.fkDpsYearOrd);
        oKardexEntry.setFkDpsDocInOrdId_n(rc.fkDpsDocOrd);
        oKardexEntry.setFkDpsEntryInOrdId_n(rc.fkDpsEtyOrd);
        oKardexEntry.setFkDpsCurrencyInOrdId_n(rc.fkCurrencyOrdId);
        oKardexEntry.setAuxDpsCostCenterCode(rc.auxDpsCostCenterCode);
        for (String note : rc.notes) {
            SStockValuationKardexUtils.addNote(oKardexEntry, note);
        }

        // ── Verificar duplicado antes de insertar ──
        if (!SStockValuationKardexUtils.existsKardexEntry(session, oKardexEntry.getFkDiogYearInId_n(),
                oKardexEntry.getFkDiogDocInId_n(),
                oKardexEntry.getFkDiogEntryInId_n())) {
            oKardexEntry.save(session);
        }
        else {
            String sLog = "Movimiento de kardex duplicado omitido"
                    + ", año=" + oKardexEntry.getFkDiogYearInId_n()
                    + ", doc=" + oKardexEntry.getFkDiogDocInId_n()
                    + ", entrada=" + oKardexEntry.getFkDiogEntryInId_n();
            System.out.println(sLog);
        }

        return oKardexEntry;
    }

    /**
     * Genera y persiste los registros de salida de kardex para todos los
     * movimientos de salida de almacén dentro del rango de fechas, aplicando el
     * método PEPS.
     * <p>
     * Para cada salida de almacén obtiene las existencias disponibles en kardex
     * ({@link SStockValuationKardexUtils#getKardexRemaining}) ordenadas por
     * fecha de entrada y consume la cantidad requerida de las entradas más
     * antiguas primero. Si no hay existencias suficientes para cubrir una
     * salida, la omite y acumula un mensaje de advertencia en el valor de
     * retorno.
     * </p>
     *
     * @param session sesión activa de base de datos
     * @param startDate fecha de inicio del rango de movimientos de salida a
     * procesar
     * @param cutDate fecha de corte del rango de movimientos de salida a
     * procesar
     * @param idValuation ID de la valuación activa
     * @return cadena con los mensajes de advertencia de salidas sin
     * existencias, o cadena vacía si todas las salidas se procesaron
     * correctamente
     * @throws Exception si ocurre un error al ejecutar las consultas o al
     * guardar registros
     */
    public static String createKardexOuts(SGuiSession session, final Date startDate, final Date cutDate, final int idValuation) throws Exception {
        // Cargar el mapa de existencias disponibles en kardex (PEPS) y caché de entradas
        ArrayList<SDbStockValuationKardex> lConsumptions = new ArrayList<>();
        HashMap<String, SRowKardexRemaining> mapRemaining = SStockValuationKardexUtils.getKardexRemaining(session);
        HashMap<Integer, SDbStockValuationKardex> mapKardex = new HashMap<>();
        String sWarnings = "";

        try (Statement st = session.getStatement().getConnection().createStatement()) {
            String sql = SStockValuationUtils.getStockMovementsQuery(st, SModSysConsts.TRNS_CT_IOG_OUT, startDate, cutDate, true);
            ResultSet res = st.executeQuery(sql);
            SDbStockValuationKardex oKardexIn = null;

            while (res.next()) {
                int outDiogYear = res.getInt("fid_diog_year");
                int outDiogDoc = res.getInt("fid_diog_doc");
                int outDiogEty = res.getInt("fid_diog_ety");
                double qtyOut = res.getDouble("mov_out");
                double dQtyToConsume = qtyOut;   // cantidad pendiente de consumir en esta salida
                double dQtyKardexConsume = 0;    // cantidad a consumir de la entrada actual
                int idItem = res.getInt("id_item");
                int idUnit = res.getInt("id_unit");
                int idLot = res.getInt("id_lot");

                if (idItem == 23762) {
                    System.out.println("");
                }

                // Obtener las entradas disponibles para este artículo/unidad/lote en orden PEPS
                String sItemKey = idItem + "-" + idUnit + "-" + idLot;
                List<SRowKardexRemaining> lRemaining = SStockValuationKardexUtils.getRemaingingByItemKey(sItemKey, mapRemaining);
                ArrayList<SDbStockValuationKardex> lStkOutConsump = new ArrayList<>();
                int iRemIndex = -1;
                for (SRowKardexRemaining oRemaining : lRemaining) {
                    iRemIndex++;
                    if (dQtyToConsume == 0) {
                        break;
                    }
                    double dRemaining = oRemaining.getQtyAvailable() - oRemaining.getQtyConsumed();
                    if (dRemaining == 0d) {
                        // si es el último registro de lo remanente y aún se tiene que consumir, lanzar la excepción
                        if (iRemIndex == lRemaining.size() - 1) {
                            lStkOutConsump.clear();
                            System.out.println("Movimiento saltado key = [" + outDiogYear + ", " + outDiogDoc + ", " + outDiogEty + " ]");
                            sWarnings += "ERROR: Sin existencias para diog out "
                                    + "num: " + res.getString("num") + " " + ", fecha: " + res.getString("dt") + " "
                                    + "key = [" + outDiogYear + ", " + outDiogDoc + ", " + outDiogEty + " ].\n"
                                    + "Item: " + res.getString("item_key") + " - " + res.getString("item_name")
                                    + ", id_item: " + idItem + ", id_unit: " + idUnit + ", id_lot: " + idLot
                                    + ",\n salida qty=" + qtyOut + ", quedan por consumir=" + dQtyToConsume + ".\n";
                        }

                        continue;
                    }

                    // Cargar la entrada de kardex desde caché o desde BD si no está en memoria
                    if (oKardexIn == null || oKardexIn.getPkStockValKardexId() != oRemaining.getPkKardexId()) {
                        if (mapKardex.containsKey(oRemaining.getPkKardexId())) {
                            oKardexIn = mapKardex.get(oRemaining.getPkKardexId());
                        }
                        else {
                            oKardexIn = new SDbStockValuationKardex(idValuation);
                            oKardexIn.read(session, new int[]{oRemaining.getPkKardexId()});
                            if (oKardexIn.getQueryResultId() == SDbConsts.READ_OK) {
                                mapKardex.put(oRemaining.getPkKardexId(), oKardexIn);
                            }
                        }
                    }

                    // Determinar cuánto consumir de esta entrada: lo que queda o lo que falta
                    if (dQtyToConsume <= dRemaining) {
                        dQtyKardexConsume = dQtyToConsume;
                        dQtyToConsume = 0;
                    }
                    else {
                        dQtyKardexConsume = dRemaining;
                        dQtyToConsume -= dQtyKardexConsume;
                    }
                    oRemaining.setQtyConsumed(oRemaining.getQtyConsumed() + dQtyKardexConsume);

                    // Calcular el costo unitario proporcional al remanente disponible
                    double costUnit = SLibUtils.roundAmount(oRemaining.getRemaining() / oRemaining.getQtyAvailable());
                    double costUnitCur = SLibUtils.roundAmount(oRemaining.getRemainingCurrency() / oRemaining.getQtyAvailable());
                    double totalOut = SLibUtils.roundAmount(costUnit * dQtyKardexConsume);
                    double totalOutCur = SLibUtils.roundAmount(costUnitCur * dQtyKardexConsume);

                    SDbStockValuationKardex oKardexOut = new SDbStockValuationKardex(idValuation);

                    oKardexOut.setMovDate(res.getDate("dt"));
                    oKardexOut.setQuantityIn(0d);
                    oKardexOut.setQuantityOut(dQtyKardexConsume);
                    oKardexOut.setCostUnit(costUnit);
                    oKardexOut.setCostUnitCurrency(costUnitCur);
                    oKardexOut.setTotalIn(0d);
                    oKardexOut.setTotalInCurrency(0d);
                    oKardexOut.setTotalOut(totalOut);
                    oKardexOut.setTotalOutCurrency(totalOutCur);
                    oKardexOut.setExchangeRate(oRemaining.getExchangeRate());
                    oKardexOut.setFkDiogCategoryId(SModSysConsts.TRNS_CT_IOG_OUT);
                    oKardexOut.setFkStockValuationKardexTypeId(SDbStockValuationKardex.TYPE_VAL_KARDEX_OUT_CONSUM);
                    oKardexOut.setFkStockValuationKardexId_n(oRemaining.getPkKardexId());
                    oKardexOut.setFkDiogYearInId_n(oRemaining.getFkDiogYearInId());
                    oKardexOut.setFkDiogDocInId_n(oRemaining.getFkDiogDocInId());
                    oKardexOut.setFkDiogEntryInId_n(oRemaining.getFkDiogEntryInId());
                    oKardexOut.setFkDiogYearOutId_n(outDiogYear);
                    oKardexOut.setFkDiogDocOutId_n(outDiogDoc);
                    oKardexOut.setFkDiogEntryOutId_n(outDiogEty);
                    oKardexOut.setFkDpsYearInMainId_n(oKardexIn.getFkDpsYearInMainId_n());
                    oKardexOut.setFkDpsDocInMainId_n(oKardexIn.getFkDpsDocInMainId_n());
                    oKardexOut.setFkDpsEntryInMainId_n(oKardexIn.getFkDpsEntryInMainId_n());
                    oKardexOut.setFkDpsCurrencyInMainId_n(oKardexIn.getFkDpsCurrencyInMainId_n());
                    oKardexOut.setFkDpsYearInOrdId_n(oKardexIn.getFkDpsYearInOrdId_n());
                    oKardexOut.setFkDpsDocInOrdId_n(oKardexIn.getFkDpsDocInOrdId_n());
                    oKardexOut.setFkDpsEntryInOrdId_n(oKardexIn.getFkDpsEntryInOrdId_n());
                    oKardexOut.setFkDpsCurrencyInOrdId_n(oKardexIn.getFkDpsCurrencyInOrdId_n());
                    oKardexOut.setFkDpsYearOutMainId_n(res.getInt("fid_dps_year_n"));
                    oKardexOut.setFkDpsDocOutMainId_n(res.getInt("fid_dps_doc_n"));
                    oKardexOut.setFkDpsEntryOutMainId_n(res.getInt("fid_dps_ety_n"));
                    oKardexOut.setFkItemId(oRemaining.getFkItemId());
                    oKardexOut.setFkUnitId(oRemaining.getFkUnitId());
                    oKardexOut.setFkLotId(oRemaining.getFkLotId());
                    oKardexOut.setFkCompanyBranchId(oRemaining.getFkCompanyBranchId());
                    oKardexOut.setFkWarehouseId(oRemaining.getFkWarehouseId());
                    oKardexOut.setAuxFkCostCenterId(res.getInt("fid_cc"));
                    oKardexOut.setAuxDpsCostCenterCode(oRemaining.getAuxDpsCostCenterCode());
                    if (res.getInt("fid_mat_req_n") > 0) {
                        oKardexOut.setFkMatRequestId_n(res.getInt("fid_mat_req_n"));
                        oKardexOut.setFkMatRequestEntryId_n(res.getInt("fid_mat_req_ety_n"));
                    }

                    lStkOutConsump.add(oKardexOut);
                }

                // Agregar los consumos de esta salida a la lista global solo si se procesaron todos
                lConsumptions.addAll(lStkOutConsump);
            }

            // Persistir todos los registros de salida de kardex generados
            for (SDbStockValuationKardex oKardexConsumpt : lConsumptions) {
                oKardexConsumpt.save(session);
            }
        }

        return sWarnings;
    }

    /**
     * Construye los movimientos de valuación de salida
     * ({@link SDbStockValuationMvt}) a partir de los registros de salida de
     * kardex ya persistidos.
     * <p>
     * Para cada movimiento de salida de almacén dentro del rango de fechas,
     * obtiene los registros de kardex de salida correspondientes mediante
     * {@link SStockValuationKardexUtils#getOutKardexOfStockMovements} y
     * construye un movimiento de consumo por cada uno. Si no se encuentran
     * registros de kardex para una salida, lanza una excepción indicando el
     * movimiento faltante.
     * </p>
     * <p>
     * Los movimientos devueltos no están guardados en base de datos; el
     * llamador es responsable de persistirlos.
     * </p>
     *
     * @param session sesión activa de base de datos
     * @param startDate fecha de inicio del rango de movimientos de salida a
     * procesar
     * @param cutDate fecha de corte del rango de movimientos de salida a
     * procesar
     * @param idValuation ID de la valuación activa
     * @return lista de movimientos de consumo listos para guardar
     * @throws Exception si no se encuentran registros de kardex para alguna
     * salida, o si ocurre un error al ejecutar las consultas
     */
    public static ArrayList<SDbStockValuationMvt> consumeFromKardex(SGuiSession session, final Date startDate, final Date cutDate, final int idValuation) throws Exception {
        String sql;
        ArrayList<SDbStockValuationMvt> lConsumptions = new ArrayList<>();
        try (Statement st = session.getStatement().getConnection().createStatement()) {
            sql = SStockValuationUtils.getStockMovementsQuery(st, SModSysConsts.TRNS_CT_IOG_OUT, startDate, cutDate, false);
            ResultSet res = st.executeQuery(sql);

            while (res.next()) {
                // Buscar los registros de kardex de salida ya persistidos para este movimiento de almacén
                int[] pkOutDiogEty = new int[]{res.getInt("fid_diog_year"), res.getInt("fid_diog_doc"), res.getInt("fid_diog_ety")};
                List<SDbStockValuationKardex> lKardexOut = SStockValuationKardexUtils.getOutKardexOfStockMovements(session, pkOutDiogEty);
                if (lKardexOut != null && !lKardexOut.isEmpty()) {
                    // Construir un movimiento de consumo por cada registro de kardex de salida
                    for (SDbStockValuationKardex oKardexOut : lKardexOut) {

                        SDbStockValuationMvt oConsumption = new SDbStockValuationMvt();

                        oConsumption.setDateMove(res.getDate("dt"));
                        oConsumption.setQuantityMovement(oKardexOut.getQuantityOut());
                        oConsumption.setCostUnitary(oKardexOut.getCostUnit());
                        oConsumption.setCostUnitaryCurrency(oKardexOut.getCostUnitCurrency());
                        oConsumption.setCost_r(oKardexOut.getTotalOut());
                        oConsumption.setCostCurrency_r(oKardexOut.getTotalOutCurrency());
                        oConsumption.setFkItemId(res.getInt("id_item"));
                        oConsumption.setFkUnitId(res.getInt("id_unit"));
                        oConsumption.setFkLotId(res.getInt("id_lot"));
                        oConsumption.setFkDiogYearInId_n(oKardexOut.getFkDiogYearInId_n());
                        oConsumption.setFkDiogDocInId_n(oKardexOut.getFkDiogDocInId_n());
                        oConsumption.setFkDiogEntryInId_n(oKardexOut.getFkDiogEntryInId_n());
                        oConsumption.setFkDpsYearInId_n(oKardexOut.getFkDpsYearInMainId_n());
                        oConsumption.setFkDpsDocInId_n(oKardexOut.getFkDpsDocInMainId_n());
                        oConsumption.setFkDpsEntryInId_n(oKardexOut.getFkDpsEntryInMainId_n());
                        oConsumption.setFkDiogYearOutId_n(res.getInt("fid_diog_year"));
                        oConsumption.setFkDiogDocOutId_n(res.getInt("fid_diog_doc"));
                        oConsumption.setFkDiogEntryOutId_n(res.getInt("fid_diog_ety"));
                        oConsumption.setFkDpsYearOutId_n(res.getInt("stk.fid_dps_year_n"));
                        oConsumption.setFkDpsDocOutId_n(res.getInt("stk.fid_dps_doc_n"));
                        oConsumption.setFkDpsEntryOutId_n(res.getInt("stk.fid_dps_ety_n"));
                        oConsumption.setFkDiogCategoryId(SModSysConsts.TRNS_CT_IOG_OUT);
                        oConsumption.setFkStockValuationId(idValuation);
                        oConsumption.setFkStockValuationMvtId_n(0);
                        oConsumption.setFkStockTypeValuationMvtId(SDbStockValuationMvt.TYPE_VAL_MVT_CONSUMP);
                        oConsumption.setAuxFkCostCenterId(res.getInt("fid_cc"));
                        oConsumption.setAuxDpsCostCenterCode(oKardexOut.getAuxDpsCostCenterCode());

                        oConsumption.setFkCompanyBranchId(res.getInt("id_cob"));
                        oConsumption.setFkWarehouseId(res.getInt("id_wh"));
                        oConsumption.setFkUserInsertId(session.getUser().getPkUserId());

                        if (res.getInt("fid_mat_req_n") > 0) {
                            oConsumption.setFkMaterialRequestId_n(oKardexOut.getFkMatRequestId_n());
                            oConsumption.setFkMaterialRequestEntryId_n(oKardexOut.getFkMatRequestEntryId_n());
                        }

                        lConsumptions.add(oConsumption);
                    }
                }
                else {
                    String sError = "No hay movimientos en kardex correspondientes a la salida de almacén "
                            + " (fecha: " + res.getDate("dt") + ") " + "num: " + res.getString("num") + ".\n"
                            + "Item: " + res.getString("item_key") + " - " + res.getString("item_name") + ". ";
                    throw new Exception(sError);
                }
            }
        }

        return lConsumptions;
    }
}

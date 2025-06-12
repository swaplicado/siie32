/*
 * Para cambiar este encabezado de licencia, consulte Propiedades del proyecto.
 * Para cambiar esta plantilla de archivo, elija Herramientas | Plantillas
 * y abra la plantilla en el editor.
 */
package erp.mod.trn.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDiog;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;

/**
 * Utilerías para la valuación de inventario.
 * Contiene métodos estáticos para obtener configuraciones, crear, consumir y eliminar movimientos de valuación,
 * así como validar y actualizar información relacionada con la valuación de inventario.
 * 
 * @author Edwin Carmona
 */
public class SStockValuationUtils {
    
    static final int DEBIT = 1;
    static final int CREDIT = 2;
    
    /**
     * Obtiene el objeto de configuración de valuación de inventario a partir de un string JSON.
     * 
     * @param statement Objeto Statement para ejecutar la consulta de configuración.
     * @return Objeto de configuración de valuación de inventario.
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     * @throws Exception 
     */
    public static SStockValuationConfiguration getStockValuationConfig(final Statement statement) throws JsonProcessingException, Exception {
        String sCfg = SCfgUtils.getParamValue(statement, SDataConstantsSys.CFG_PARAM_TRN_STK_MAT_VAL);
        ObjectMapper mapper = new ObjectMapper();
        SStockValuationConfiguration oCfg = mapper.readValue(sCfg, SStockValuationConfiguration.class);
        
        return oCfg;
    }
    
    /**
     * Devuelve una consulta SQL para obtener los movimientos de stock según los parámetros recibidos.
     * 
     * @param statement Objeto Statement para ejecutar la consulta.
     * @param diogCategory Categoría del movimiento de inventario (entrada o salida).
     * @param startDate Fecha de inicio del periodo.
     * @param cutDate Fecha de corte del periodo.
     * @return Consulta SQL como cadena.
     * @throws Exception 
     */
    private static String getStockMovementsQuery(final Statement statement, final int diogCategory, final Date startDate, final Date cutDate) throws Exception {
        String sql = "SELECT  "
                + "    stk.*,"
                + "    d.num, "
                + "    mre.fk_item_ref_n AS ref_ety, "
                + "    mr.fk_item_ref_n ref_rm, "
                + "    de.fid_cc,"
                + "    tp.tp_iog, "
                + "    i.item_key, "
                + "    i.item AS item_name,"
                + "    dps.num AS dps_num, "
                + "    dps.dt AS dps_date, "
                + "    dps.fid_ct_dps, "
                + "    dps.fid_cl_dps, "
                + "    dps.fid_tp_dps, "
                + "    dps_ety.price_u_real_r, "
                + "    dps_ety.stot_r, "
                + "    supp.id_des_year, "
                + "    supp.id_des_doc, "
                + "    supp.id_des_ety, "
                + "    dps_ety_des.price_u_real_r AS ety_des_price_real "
                + "FROM "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " stk "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " d ON stk.fid_diog_year = d.id_year "
                + "        AND stk.fid_diog_doc = d.id_doc "
                + "        AND NOT d.b_del "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " de ON stk.fid_diog_year = de.id_year "
                + "        AND stk.fid_diog_doc = de.id_doc "
                + "        AND stk.fid_diog_ety = de.id_ety "
                + "        AND NOT de.b_del "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " i ON stk.id_item = i.id_item "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRNS_TP_IOG) + " tp ON stk.fid_ct_iog = tp.id_ct_iog "
                + "        AND stk.fid_cl_iog = tp.id_cl_iog "
                + "        AND stk.fid_tp_iog = tp.id_tp_iog "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " mre ON stk.fid_mat_req_n = mre.id_mat_req "
                + "        AND stk.fid_mat_req_ety_n = mre.id_ety "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " mr ON d.fid_mat_req_n = mr.id_mat_req "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " dps ON stk.fid_dps_year_n = dps.id_year AND stk.fid_dps_doc_n = dps.id_doc "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " dps_ety ON stk.fid_dps_year_n = dps_ety.id_year "
                + "        AND stk.fid_dps_doc_n = dps_ety.id_doc "
                + "        AND stk.fid_dps_ety_n = dps_ety.id_ety "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " AS supp ON stk.fid_dps_year_n = supp.id_src_year "
                + "        AND stk.fid_dps_doc_n = supp.id_src_doc "
                + "        AND stk.fid_dps_ety_n = supp.id_src_ety "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS dps_ety_des ON supp.id_des_year = dps_ety_des.id_year "
                + "        AND supp.id_des_doc = dps_ety_des.id_doc "
                + "        AND supp.id_des_ety = dps_ety_des.id_ety "
                + "WHERE "
                + "    NOT stk.b_del "
                + "    AND stk.id_year = YEAR('" + SLibUtils.DbmsDateFormatDate.format(cutDate) + "') "
                + "    AND stk.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(startDate) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(cutDate) + "' "
                + "    AND stk.fid_ct_iog = " + diogCategory + " ";

        SStockValuationConfiguration oCfg = SStockValuationUtils.getStockValuationConfig(statement);
        ArrayList<int[]> iogTpmovs;

        // Determinar si son movimientos de entrada o salida
        boolean isIn = diogCategory == SModSysConsts.TRNS_CT_IOG_IN;
        iogTpmovs = isIn ? oCfg.getIogTpmovsIn() : oCfg.getIogTpmovsOut();
        sql += isIn ? "AND stk.mov_in > 0 " : "AND stk.mov_out > 0 ";

        if (iogTpmovs.isEmpty()) {
            throw new Exception("No existe configuración de movimientos de almacén para la valuación");
        }

        // Optimización: Agrupar por fid_cl_iog para reducir condiciones OR
        Map<Integer, List<Integer>> groupedTpmovs = new HashMap<>();
        for (int[] iogTpmov : iogTpmovs) {
            groupedTpmovs.computeIfAbsent(iogTpmov[1], k -> new ArrayList<>()).add(iogTpmov[2]);
        }

        // Construir condiciones SQL optimizadas
        if (!groupedTpmovs.isEmpty()) {
            sql += "AND (";
            boolean firstGroup = true;

            for (Map.Entry<Integer, List<Integer>> entry : groupedTpmovs.entrySet()) {
                if (!firstGroup) {
                    sql += " OR ";
                }
                firstGroup = false;

                int fidClIog = entry.getKey();
                List<Integer> tpIogs = entry.getValue();

                if (tpIogs.size() == 1) {
                    sql += String.format("(stk.fid_cl_iog = %d AND stk.fid_tp_iog = %d)", 
                                       fidClIog, tpIogs.get(0));
                } else {
                    sql += String.format("(stk.fid_cl_iog = %d AND stk.fid_tp_iog IN (%s))", 
                                       fidClIog, tpIogs.stream()
                                                      .map(String::valueOf)
                                                      .collect(Collectors.joining(",")));
                }
            }

            sql += ") ";
        }

        sql += "ORDER BY stk.dt ASC, de.id_doc ASC, de.id_ety ASC";

        return sql;
    }
    
    /**
     * Crea movimientos de valuación de inventario según la categoría, fechas y el ID de valuación.
     * 
     * @param session Sesión de usuario para ejecutar consultas.
     * @param startDate Fecha de inicio.
     * @param cutDate Fecha de corte.
     * @param idValuation ID de la valuación.
     * @throws Exception 
     */
    public static void createValuationEntries(SGuiSession session, final Date startDate, final Date cutDate, final int idValuation) throws Exception {
        String sql = SStockValuationUtils.getStockMovementsQuery(session.getStatement().getConnection().createStatement(), SModSysConsts.TRNS_CT_IOG_IN, startDate, cutDate);
        
        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        SDbStockValuationMvt oEntry;
        while (res.next()) {
            oEntry = new SDbStockValuationMvt();

            oEntry.setDateMove(res.getDate("dt"));
            oEntry.setQuantityMovement(res.getDouble("mov_in"));
            oEntry.setCostUnitary(res.getDouble("cost_u"));
            oEntry.setCost_r(res.getDouble("debit"));
            oEntry.setFkStockValuationId(idValuation);
            oEntry.setFkDiogCategoryId(SModSysConsts.TRNS_CT_IOG_IN);
            oEntry.setFkDiogYearInId_n(res.getInt("fid_diog_year"));
            oEntry.setFkDiogDocInId_n(res.getInt("fid_diog_doc"));
            oEntry.setFkDiogEntryInId_n(res.getInt("fid_diog_ety"));
            oEntry.setFkDpsYearInId_n(res.getInt("stk.fid_dps_year_n"));
            oEntry.setFkDpsDocInId_n(res.getInt("stk.fid_dps_doc_n"));
            oEntry.setFkDpsEntryInId_n(res.getInt("stk.fid_dps_ety_n"));
            oEntry.setFkItemId(res.getInt("id_item"));
            oEntry.setFkUnitId(res.getInt("id_unit"));
            oEntry.setFkLotId(res.getInt("id_lot"));
            oEntry.setFkCompanyBranchId(res.getInt("id_cob"));
            oEntry.setFkWarehouseId(res.getInt("id_wh"));
            oEntry.setFkUserInsertId(session.getUser().getPkUserId());
            oEntry.setAuxTypeDpsIn(new int[] { res.getInt("fid_ct_dps"), 
                                                res.getInt("fid_cl_dps"), 
                                                res.getInt("fid_tp_dps") });

            if (oEntry.getAuxTypeDpsIn()[0] == SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] && 
                oEntry.getAuxTypeDpsIn()[1] == SModSysConsts.TRNU_TP_DPS_PUR_ORD[1] && 
                oEntry.getAuxTypeDpsIn()[2] == SModSysConsts.TRNU_TP_DPS_PUR_ORD[2]) {
                    if (res.getInt("supp.id_des_year") == 0 || res.getInt("supp.id_des_doc") == 0) {
                        oEntry.setAuxItemDescription(res.getString("item_key") + " - " + res.getString("item_name"));
                        oEntry.setTemporalPrice(true);
                        String sLog = "No se puede crear la valuación. El movimiento de entrada al almacén "
                                + "con número de documento " + res.getInt("d.num") + " y "
                                + "fecha " + SLibUtils.DateFormatDate.format(oEntry.getDateMove()) + " "
                                + "no tiene una factura asociada.\nPedido folio: " + res.getString("dps_num") + ", " 
                                + "fecha: " + SLibUtils.DateFormatDate.format(res.getDate("dps_date")) + ".";
                        SStockValuationLogUtils.logConsume(startDate, cutDate, oEntry, sLog);
                    }
                    else if (oEntry.getCostUnitary() != res.getDouble("ety_des_price_real")) {
                            oEntry.setCostUnitary(res.getDouble("ety_des_price_real"));
                            oEntry.setCost_r(SLibUtils.roundAmount(oEntry.getQuantityMovement() * oEntry.getCostUnitary()));
                            oEntry.setFkDpsYearInId_n(res.getInt("supp.id_des_year"));
                            oEntry.setFkDpsDocInId_n(res.getInt("supp.id_des_doc"));
                            oEntry.setFkDpsEntryInId_n(res.getInt("supp.id_des_ety"));
                        }
            }
            else if (oEntry.getAuxTypeDpsIn()[0] == SModSysConsts.TRNU_TP_DPS_PUR_INV[0] && 
                oEntry.getAuxTypeDpsIn()[1] == SModSysConsts.TRNU_TP_DPS_PUR_INV[1] && 
                oEntry.getAuxTypeDpsIn()[2] == SModSysConsts.TRNU_TP_DPS_PUR_INV[2]) {
                    if (oEntry.getCostUnitary() != res.getDouble("dps_ety.price_u_real_r")) {
                        oEntry.setCostUnitary(res.getDouble("dps_ety.price_u_real_r"));
                        oEntry.setCost_r(SLibUtils.roundAmount(oEntry.getQuantityMovement() * oEntry.getCostUnitary()));
                    }
            }
            
            oEntry.save(session);
        }
    }

    /**
     * Obtiene una lista de movimientos de valuación de inventario que no han sido consumidos.
     * 
     * @param session Sesión de usuario.
     * @param idYear Año de la valuación.
     * @return Lista de movimientos no consumidos.
     * @throws Exception 
     */
    private static ArrayList<SDbStockValuationMvt> getNotConsumedValuationEntries(SGuiSession session) throws Exception {
        String sql = "SELECT "
                + "ve.id_stk_val_mvt, "
                + "ve.fk_stk_val, "
                + "SUM(IF(ve.fk_ct_iog = 1, ve.qty_mov, ve.qty_mov * - 1)) AS qty, "
                + "ve.dt_mov, "
                + "ve.fk_diog_year_in_n, "
                + "ve.fk_diog_doc_in_n, "
                + "ve.fk_diog_ety_in_n, "
                + "ve.fk_dps_year_in_n, "
                + "ve.fk_dps_doc_in_n, "
                + "ve.fk_dps_ety_in_n, "
                + "ve.fk_item, "
                + "ve.fk_unit, "
                + "ve.fk_lot, "
                + "ve.cost_u, "
                + "ve.fk_cob,"
                + "ve.fk_wh, "
                + "dps.fid_ct_dps, "
                + "dps.fid_cl_dps, "
                + "dps.fid_tp_dps, "
                + "supp.id_des_year, "
                + "supp.id_des_doc "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " AS ve "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS diog_ety "
                + "         ON ve.fk_diog_year_in_n = diog_ety.id_year "
                + "        AND ve.fk_diog_doc_in_n = diog_ety.id_doc " 
                + "        AND ve.fk_diog_ety_in_n = diog_ety.id_ety "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS dps "
                + "        ON ve.fk_dps_year_in_n = dps.id_year "
                + "        AND ve.fk_dps_doc_in_n = dps.id_doc "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " AS supp ON ve.fk_dps_year_in_n = supp.id_src_year "
                + "        AND ve.fk_dps_doc_in_n = supp.id_src_doc "
                + "        AND ve.fk_dps_ety_in_n = supp.id_src_ety "
                + "WHERE "
                + "NOT ve.b_del "
//                + "AND ve.fk_diog_year_in_n = " + idYear + " " XXX Después de la revisión remover esta línea si todo funciona OK
                + "GROUP BY ve.fk_cob, ve.fk_wh, ve.fk_item, ve.fk_unit, ve.fk_lot, ve.cost_u, "
                + "ve.fk_diog_year_in_n, ve.fk_diog_doc_in_n, ve.fk_diog_ety_in_n "
                + "HAVING qty > 0 "
                + "ORDER BY ve.dt_mov ASC, ve.fk_stk_val ASC";
      
        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        SDbStockValuationMvt oEntry;
        ArrayList<SDbStockValuationMvt> entries = new ArrayList<>();
        while (res.next()) {
            oEntry = new SDbStockValuationMvt();

            oEntry.setDateMove(res.getDate("dt_mov"));
            oEntry.setQuantityMovement(res.getDouble("qty"));
            oEntry.setCostUnitary(res.getDouble("cost_u"));
            oEntry.setPkStockValuationMvtId(res.getInt("id_stk_val_mvt"));
            oEntry.setFkStockValuationId(res.getInt("fk_stk_val"));
            oEntry.setFkDiogYearInId_n(res.getInt("fk_diog_year_in_n"));
            oEntry.setFkDiogDocInId_n(res.getInt("fk_diog_doc_in_n"));
            oEntry.setFkDiogEntryInId_n(res.getInt("fk_diog_ety_in_n"));
            oEntry.setFkDpsYearInId_n(res.getInt("fk_dps_year_in_n"));
            oEntry.setFkDpsDocInId_n(res.getInt("fk_dps_doc_in_n"));
            oEntry.setFkDpsEntryInId_n(res.getInt("fk_dps_ety_in_n"));
            oEntry.setFkItemId(res.getInt("fk_item"));
            oEntry.setFkUnitId(res.getInt("fk_unit"));
            oEntry.setFkLotId(res.getInt("fk_lot"));
            oEntry.setFkDiogCategoryId(SModSysConsts.TRNS_CT_IOG_IN);
            oEntry.setFkCompanyBranchId(res.getInt("fk_cob"));
            oEntry.setFkWarehouseId(res.getInt("fk_wh"));

            if (res.getInt("dps.fid_ct_dps") > 0 && res.getInt("dps.fid_cl_dps") > 0 && 
                    res.getInt("dps.fid_tp_dps") > 0) {
                oEntry.setAuxTypeDpsIn(new int[] {
                                                    res.getInt("dps.fid_ct_dps"), 
                                                    res.getInt("dps.fid_cl_dps"), 
                                                    res.getInt("dps.fid_tp_dps") 
                                                });
                if (oEntry.getAuxTypeDpsIn()[0] == SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] &&
                    oEntry.getAuxTypeDpsIn()[1] == SModSysConsts.TRNU_TP_DPS_PUR_ORD[1] &&
                    oEntry.getAuxTypeDpsIn()[2] == SModSysConsts.TRNU_TP_DPS_PUR_ORD[2]) {
                        oEntry.setTemporalPrice(true);
                }
            }

            entries.add(oEntry);
        }

        return entries;
    }
    
    /**
     * Consume movimientos de valuación de inventario según los criterios y devuelve la lista de consumos.
     * 
     * @param session Sesión de usuario.
     * @param startDate Fecha de inicio.
     * @param cutDate Fecha de corte.
     * @param idValuation ID de la valuación.
     * @return Lista de consumos realizados.
     * @throws Exception 
     */
    public static ArrayList<SDbStockValuationMvt> consumeEntries(SGuiSession session, final Date startDate, final Date cutDate, final int idValuation) throws Exception {
        String sql = SStockValuationUtils.getStockMovementsQuery(session.getStatement().getConnection().createStatement(), SModSysConsts.TRNS_CT_IOG_OUT, startDate, cutDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        HashMap<String, ArrayList<SDbStockValuationMvt>> mapEntriesPending = SStockValuationUtils.getNotConsumedValuationEntriesGrouped(session);
        ArrayList<SDbStockValuationMvt> lConsumptions = new ArrayList<>();
        ArrayList<SDbStockValuationMvt> lTempConsumptions = new ArrayList<>();

        try (ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql)) {
            while (res.next()) {
                double qtyDiogEty = res.getDouble("mov_out");
                double qtyToConsume = qtyDiogEty;
                lTempConsumptions.clear();
                /**
                 * Se deja este comentario para cuando se requiera hacer una inspección
                 * del proceso para un ítem en específico. Edwin Carmona 2024-07-29  
                 */
//                if (res.getInt("id_item") == 24015) {
//                    int r = 0;
//                }
                ArrayList<SDbStockValuationMvt> lEntriesPending = mapEntriesPending.get(res.getInt("id_item") + "-" + res.getInt("id_unit"));
                for (SDbStockValuationMvt entry : lEntriesPending) {
                    if (entry.isAuxConsumed()) {
                        continue;
                    }
                    double consumeQuantity = 0d;
                    double entryAvailableQuantity = entry.getQuantityMovement() - entry.getAuxConsumption();
                    if (qtyToConsume >= entryAvailableQuantity) {
                        entry.setAuxConsumed(true);
                        entry.setAuxConsumption(entry.getAuxConsumption() + entryAvailableQuantity);
                        consumeQuantity = entryAvailableQuantity;
                        qtyToConsume = qtyToConsume - entryAvailableQuantity;
                    }
                    else {
                        entry.setAuxConsumption(entry.getAuxConsumption() + qtyToConsume);
                        consumeQuantity = qtyToConsume;
                        qtyToConsume = 0d;
                    }

                    SDbStockValuationMvt oConsumption = new SDbStockValuationMvt();

                    oConsumption.setDateMove(res.getDate("dt"));
                    oConsumption.setQuantityMovement(consumeQuantity);
                    oConsumption.setCostUnitary(entry.getCostUnitary());
                    oConsumption.setCost_r(SLibUtils.roundAmount(consumeQuantity * oConsumption.getCostUnitary()));

                    if (entry.isTemporalPrice()) {
                        oConsumption.setTemporalPrice(true);
                    }

                    oConsumption.setFkItemId(res.getInt("id_item"));
                    oConsumption.setFkUnitId(res.getInt("id_unit"));
                    oConsumption.setFkLotId(res.getInt("id_lot"));
                    oConsumption.setFkDiogYearInId_n(entry.getFkDiogYearInId_n());
                    oConsumption.setFkDiogDocInId_n(entry.getFkDiogDocInId_n());
                    oConsumption.setFkDiogEntryInId_n(entry.getFkDiogEntryInId_n());
                    oConsumption.setFkDpsYearInId_n(entry.getFkDpsYearInId_n());
                    oConsumption.setFkDpsDocInId_n(entry.getFkDpsDocInId_n());
                    oConsumption.setFkDpsEntryInId_n(entry.getFkDpsEntryInId_n());
                    oConsumption.setFkDiogYearOutId_n(res.getInt("fid_diog_year"));
                    oConsumption.setFkDiogDocOutId_n(res.getInt("fid_diog_doc"));
                    oConsumption.setFkDiogEntryOutId_n(res.getInt("fid_diog_ety"));
                    oConsumption.setFkDpsYearOutId_n(res.getInt("stk.fid_dps_year_n"));
                    oConsumption.setFkDpsDocOutId_n(res.getInt("stk.fid_dps_doc_n"));
                    oConsumption.setFkDpsEntryOutId_n(res.getInt("stk.fid_dps_ety_n"));
                    oConsumption.setFkDiogCategoryId(SModSysConsts.TRNS_CT_IOG_OUT);
                    oConsumption.setFkStockValuationId(idValuation);
                    oConsumption.setFkStockValuationMvtId_n(entry.getPkStockValuationMvtId());
                    oConsumption.setAuxFkCostCenterId(res.getInt("fid_cc"));

                    oConsumption.setFkCompanyBranchId(res.getInt("id_cob"));
                    oConsumption.setFkWarehouseId(res.getInt("id_wh"));
                    oConsumption.setFkUserInsertId(session.getUser().getPkUserId());

                    if (res.getInt("fid_mat_req_n") > 0) {
                        oConsumption.setFkMaterialRequestId_n(res.getInt("fid_mat_req_n"));
                        oConsumption.setFkMaterialRequestEntryId_n(res.getInt("fid_mat_req_ety_n"));
                    }

                    lTempConsumptions.add(oConsumption);

                    if (qtyToConsume == 0d) {
                        break;
                    }
                }

                if (qtyToConsume > 0d) {
                    SDbStockValuationMvt oConsumption = new SDbStockValuationMvt();
                        
                    oConsumption.setDateMove(res.getDate("dt"));
                    oConsumption.setQuantityMovement(qtyToConsume);
                    oConsumption.setCostUnitary(0d);
                    oConsumption.setCost_r(SLibUtils.roundAmount(0d));
                    oConsumption.setFkItemId(res.getInt("id_item"));
                    oConsumption.setFkUnitId(res.getInt("id_unit"));
                    oConsumption.setFkLotId(res.getInt("id_lot"));
                    oConsumption.setFkDiogYearInId_n(0);
                    oConsumption.setFkDiogDocInId_n(0);
                    oConsumption.setFkDiogEntryInId_n(0);
                    oConsumption.setFkDiogYearOutId_n(res.getInt("fid_diog_year"));
                    oConsumption.setFkDiogDocOutId_n(res.getInt("fid_diog_doc"));
                    oConsumption.setFkDiogEntryOutId_n(res.getInt("fid_diog_ety"));
                    oConsumption.setFkDpsYearInId_n(res.getInt("stk.fid_dps_year_n"));
                    oConsumption.setFkDpsDocInId_n(res.getInt("stk.fid_dps_doc_n"));
                    oConsumption.setFkDpsEntryInId_n(res.getInt("stk.fid_dps_ety_n"));
                    oConsumption.setFkDiogCategoryId(SModSysConsts.TRNS_CT_IOG_OUT);
                    oConsumption.setFkStockValuationId(idValuation);
                    oConsumption.setFkStockValuationMvtId_n(0);
                    oConsumption.setAuxFkCostCenterId(res.getInt("fid_cc"));

                    oConsumption.setFkCompanyBranchId(res.getInt("id_cob"));
                    oConsumption.setFkWarehouseId(res.getInt("id_wh"));
                    oConsumption.setFkUserInsertId(session.getUser().getPkUserId());
                    
                    if (res.getInt("fid_mat_req_n") > 0) {
                        oConsumption.setFkMaterialRequestId_n(res.getInt("fid_mat_req_n"));
                        oConsumption.setFkMaterialRequestEntryId_n(res.getInt("fid_mat_req_ety_n"));
                    }
                    
                    oConsumption.setAuxItemDescription(res.getString("item_key") + " - " + res.getString("item_name"));
                    oConsumption.setAuxDiogTypeDescription(res.getString("tp.tp_iog"));
                    String log = "WARNING: Movimiento de consumo en $0. " +
                            "No hay suficiente stock para consumir. Fecha mov: " + (res.getString("dt")) +
                            ", num: " + res.getString("d.num") +
                            ", Tipo: " + res.getString("tp.tp_iog") +
                            " / ID_YEAR = " + (res.getInt("fid_diog_year")) + ", " +
                            "ID_DOC = " + (res.getInt("fid_diog_doc")) +
                            ", ID_ETY = " + res.getInt("fid_diog_ety") + ".";
                    oConsumption.setLogMessage(log);

                    lTempConsumptions.add(oConsumption);
                }
                
                lConsumptions.addAll(lTempConsumptions);
            }
        }

        return lConsumptions;
    }
    
    /**
     * Elimina una valuación y sus registros relacionados en la base de datos.
     * 
     * @param session Sesión de usuario.
     * @param idValuation ID de la valuación a eliminar.
     * @return true si la operación fue exitosa.
     * @throws SQLException 
     */
    public static boolean deleteValuation(SGuiSession session, final int idValuation) throws SQLException {
        /**
         * Eliminar pólizas
         */
        String sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_ACC) + " AS va "
                + "ON re.id_year = va.fk_fin_rec_year "
                + "AND re.id_per = va.fk_fin_rec_per "
                + "AND re.id_bkc = va.fk_fin_rec_bkc "
                + "AND re.id_tp_rec = va.fk_fin_rec_tp_rec "
                + "AND re.id_num = va.fk_fin_rec_num "
                + "AND re.id_ety = va.fk_fin_rec_ety "
                + "SET re.b_del = 1, re.sort_pos = 0 "
                + "WHERE va.fk_stk_val = " + idValuation + ";";
        session.getStatement().getConnection().createStatement().executeUpdate(sql);
        
        /**
         * Eliminar registros de valuación vs pólizas
         */
        String sqlDelLinks = "UPDATE " +
                            SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_ACC) + " " +
                            "SET b_del = 1 " +
                            "WHERE " +
                            "    fk_stk_val = " + idValuation + ";";
                            
        session.getStatement().getConnection().createStatement().executeUpdate(sqlDelLinks);
        
        /**
         * Eliminar revisiones de ajuste de precios temporales
         */
        String sqlDelRevs = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " AS mvt1 " +
                            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " AS mvt2  " +
                            "    ON mvt1.fk_stk_val_mvt_rev_n = mvt2.id_stk_val_mvt " +
                            "    AND NOT mvt2.b_del " +
                            "    AND mvt2.fk_stk_val = " + idValuation + " " +
                            "SET  " +
                            "    mvt1.b_rev = 0, " +
                            "    mvt1.fk_stk_val_mvt_n = NULL " +
                            "WHERE NOT mvt1.b_del;";
        
        session.getStatement().getConnection().createStatement().executeUpdate(sqlDelRevs);
        
        /**
         * Eliminar movimientos de valuación
         */
        String sqlDelEtys = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " SET b_del = 1 "
                + "WHERE fk_stk_val = " + idValuation + ";";
        
        session.getStatement().getConnection().createStatement().executeUpdate(sqlDelEtys);

        /**
         * Eliminar Diogs y stk de ajustes
         */
        String sqlDelDiogs = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS diog "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_DIOG_ADJ) + " AS piv ON "
                + "             (diog.id_year = piv.id_year AND diog.id_doc = piv.id_doc) "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS stk ON "
                + "             (piv.id_year = stk.fid_diog_year AND piv.id_doc = stk.fid_diog_doc) "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS diog_ety ON "
                + "             (diog_ety.id_year = piv.id_year AND diog_ety.id_doc = piv.id_doc) "
                + "SET diog.b_del = 1,"
                + "diog_ety.b_del = 1, "
                + "stk.b_del = 1 "
                + "WHERE NOT diog.b_del AND NOT diog_ety.b_del AND NOT stk.b_del AND piv.id_stk_val = " + idValuation + ";";
        
        session.getStatement().getConnection().createStatement().executeUpdate(sqlDelDiogs);
        
        return true;
    }
    
    /**
     * Verifica si se puede crear un movimiento de inventario (DIOG) para una fecha dada según la valuación.
     * 
     * @param session Sesión de usuario.
     * @param dtDiog Fecha del documento.
     * @return true si se puede crear, false en caso contrario.
     */
    public static boolean canCreateDiogByValuation(SGuiSession session, final Date dtDiog) {
        try {
            String sql = "SELECT id_stk_val "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " "
                    + "WHERE NOT b_del "
                    + "AND '" + SLibUtils.DbmsDateFormatDate.format(dtDiog) + "' BETWEEN dt_sta AND dt_end;";
            
            ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
            
            return ! res.next();
        }
        catch (SQLException ex) {
            Logger.getLogger(SStockValuationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    /**
     * Revalúa una lista de valuaciones de inventario.
     * 
     * @param client Cliente de la sesión.
     * @param lValuations Lista de valuaciones a revaluar.
     * @return Cadena vacía si fue exitoso, mensaje de error en caso contrario.
     * @throws SQLException 
     */
    public static String revaluateValuations(SGuiClient client, ArrayList<SDbStockValuation> lValuations) throws SQLException {
        try {
            client.getSession().getStatement().getConnection().setAutoCommit(false);
            
            SDbStockValuation oNewValuation;
            for (SDbStockValuation oValuation : lValuations) {
                oNewValuation = (SDbStockValuation) oValuation.clone();
                oNewValuation.setRegistryNew(true);
                oNewValuation.setDeleted(false);
                oNewValuation.setAuxRecordPk(oValuation.getAuxRecordPk());
                oNewValuation.setAuxIsAllInsert(true);
                
                oNewValuation.save(client.getSession());
            }
            
            client.getSession().getStatement().getConnection().commit();
        }
        catch (CloneNotSupportedException ex) {
            Logger.getLogger(SStockValuationUtils.class.getName()).log(Level.SEVERE, null, ex);
            
            client.getSession().getStatement().getConnection().rollback();
            
            return ex.getMessage();
        }
        catch (Exception ex) {
            Logger.getLogger(SStockValuationUtils.class.getName()).log(Level.SEVERE, null, ex);
            
            return ex.getMessage();
        }
        
        return "";
    }
    
    /**
     * Elimina las valuaciones a partir de una fecha de corte.
     * 
     * @param client Cliente de la sesión.
     * @param endDate Fecha de corte.
     * @return Lista de valuaciones eliminadas.
     */
    public static ArrayList<SDbStockValuation> deleteValuations(SGuiClient client, Date endDate) {
        try {
            client.getSession().getStatement().getConnection().setAutoCommit(false);

            Date firstInvalidValuationDate = SStockValuationUtils.getFirstInvalidValuationDate(client.getSession(), endDate);

            String sql = "SELECT val.id_stk_val "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " AS val "
                    + "WHERE NOT val.b_del "
                    + "AND dt_sta >= '" + SLibUtils.DbmsDateFormatDate.format(firstInvalidValuationDate) + "' "
                    + "AND YEAR(dt_sta) = YEAR('" + SLibUtils.DbmsDateFormatDate.format(firstInvalidValuationDate) + "') "
                    + "ORDER BY dt_end ASC";

            ResultSet res = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            ArrayList<SDbStockValuation> lValuations = new ArrayList<>();
            SDbStockValuation oVal;
            while (res.next()) {
                oVal = new SDbStockValuation();
                oVal.read(client.getSession(), new int[] { res.getInt("id_stk_val") });
                oVal.setAuxIsAllDelete(true);
                oVal.delete(client.getSession());

                if (oVal.getQueryResultId() != SDbConsts.SAVE_OK) {
                    throw new Exception("Hubo un error al eliminar las valuaciones.");
                }
                
                lValuations.add(oVal);
            }
            
            client.getSession().getStatement().getConnection().commit();
            
            return lValuations;
        }
        catch (SQLException ex) {
            try {
                client.getSession().getStatement().getConnection().rollback();
            }
            catch (SQLException ex1) {
                Logger.getLogger(SStockValuationUtils.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
            Logger.getLogger(SStockValuationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SStockValuationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Verifica si se pueden eliminar las valuaciones a partir de una fecha de corte.
     * 
     * @param client Cliente de la sesión.
     * @param endDate Fecha de corte.
     * @return Mensaje de error si no se puede eliminar, cadena vacía si es posible.
     * @throws Exception 
     */
    public static String canDeleteValuations(SClientInterface client, Date endDate) throws Exception {
        Date firstInvalidValuationDate = SStockValuationUtils.getFirstInvalidValuationDate(client.getSession(), endDate);
        
        String sql = "SELECT DISTINCT val.id_stk_val, r.dt, val.dt_sta, val.dt_end, r.id_tp_rec, r.id_num "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " AS val "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_ACC) + " AS vacc ON (val.id_stk_val = vacc.fk_stk_val) "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r ON ("
                + "vacc.fk_fin_rec_year = r.id_year AND "
                + "vacc.fk_fin_rec_per = r.id_per AND "
                + "vacc.fk_fin_rec_bkc = r.id_bkc AND "
                + "vacc.fk_fin_rec_tp_rec = r.id_tp_rec AND "
                + "vacc.fk_fin_rec_num = r.id_num) "
                + "WHERE NOT val.b_del AND NOT vacc.b_del "
                + "AND val.dt_sta >= '" + SLibUtils.DbmsDateFormatDate.format(firstInvalidValuationDate) + "' "
                + "AND YEAR(val.dt_sta) = YEAR('" + SLibUtils.DbmsDateFormatDate.format(firstInvalidValuationDate) + "') "
                + "ORDER BY val.dt_end ASC";
        
        ResultSet res = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
        String result = "";
        while (res.next()) {
            if (! SDataUtilities.isPeriodOpen(client, res.getDate("dt"))) {
                result += "La valuación del '" + SLibUtils.DbmsDateFormatDate.format(firstInvalidValuationDate) + "' al "
                            + "'" + SLibUtils.DbmsDateFormatDate.format(firstInvalidValuationDate) + "' no se puede reevaluar porque la póliza: "
                            + "'" + res.getString("r.id_tp_rec") + "', num: " + (res.getInt("r.id_num")) + ", "
                            + "fecha: '" + SLibUtils.DateFormatDate.format(res.getDate("dt")) + "' está en un periodo cerrado.";
            }
        }
        
        return result;
    }
    
    /**
     * Obtiene la fecha de la primera valuación inválida anterior a la fecha de corte.
     * 
     * @param session Sesión de usuario.
     * @param endDate Fecha de corte.
     * @return Fecha de la primera valuación inválida, o null si todas son válidas.
     * @throws SQLException
     * @throws Exception 
     */
    public static Date getFirstInvalidValuationDate(SGuiSession session, Date endDate) throws SQLException, Exception {
        String sql = "SELECT id_stk_val, dt_sta  "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " "
                + "WHERE NOT b_del "
                + "AND dt_end < '" + SLibUtils.DbmsDateFormatDate.format(endDate) + "' "
                + "AND YEAR(dt_sta) = YEAR('" + SLibUtils.DbmsDateFormatDate.format(endDate) + "') "
                + "ORDER BY dt_end ASC";

        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        while (res.next()) {
            if (! isValuationDiogsValidById(session, res.getInt("id_stk_val")).isEmpty() || 
                ! isValuationMatReqValidById(session, res.getInt("id_stk_val")).isEmpty()) {
                return res.getDate("dt_sta");
            }
        }
        
        return null;
    }
    
    /**
     * Recorre las valuaciones anteriores a la actual y determina si alguna no es válida.
     * Si alguna no es válida, en el string de retorno se muestra el detalle de qué valuaciones no son válidas.
     * 
     * @param session Sesión de usuario.
     * @param endDate Fecha de corte.
     * @return Detalle de las valuaciones no válidas.
     * @throws SQLException
     * @throws Exception 
     */
    public static String arePastValuationsValid(SGuiSession session, Date endDate) throws SQLException, Exception {
        String sql = "SELECT id_stk_val, dt_sta, dt_end "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " "
                + "WHERE NOT b_del "
                + "AND dt_end < '" + SLibUtils.DbmsDateFormatDate.format(endDate) + "' "
                + "AND YEAR(dt_sta) = YEAR('" + SLibUtils.DbmsDateFormatDate.format(endDate) + "') "
                + "ORDER BY dt_end ASC";

        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        ArrayList<SDataDiog> lDiogs = null;
        ArrayList<SDbMaterialRequest> lMatReqs = null;
        String result = "";
        while (res.next()) {
            lDiogs = isValuationDiogsValidById(session, res.getInt("id_stk_val"));
            if (! lDiogs.isEmpty()) {
                for (SDataDiog oDiog : lDiogs) {
                    result += "El movimiento de almacén: " + oDiog.getDbmsDiogCategory() + "-"
                            + oDiog.getDbmsDiogClass() + "-"
                            + oDiog.getDbmsDiogType() + " "
                            + "Numero: " + oDiog.getNumber() + " "
                            + "Fecha: '" + SLibUtils.DateFormatDate.format(oDiog.getDate()) + "' "
                            + "invalida la valuación de las fechas del '" + SLibUtils.DateFormatDate.format(res.getDate("dt_sta")) + "' "
                            + "al '" + SLibUtils.DateFormatDate.format(res.getDate("dt_end")) + "' \n";
                }
            }
            
            lMatReqs = isValuationMatReqValidById(session, res.getInt("id_stk_val"));
            if (! lMatReqs.isEmpty()) {
                for (SDbMaterialRequest oMatReq : lMatReqs) {
                    result += "La requisición con folio: " + oMatReq.getNumber() + ", "
                            + "Fecha: '" + SLibUtils.DateFormatDate.format(oMatReq.getDate()) + "' "
                            + "invalida la valuación de las fechas del '" + SLibUtils.DateFormatDate.format(res.getDate("dt_sta")) + "' "
                            + "al '" + SLibUtils.DateFormatDate.format(res.getDate("dt_end")) + "' \n";
                }
            }
        }
        
        return result;
    }
    
    /**
     * Determina si la valuación con el ID recibido es válida.
     * Consulta si hay un DIOG actualizado después del timestamp de la última actualización de la valuación.
     * Si hay movimientos que cumplen el criterio, los devuelve en la lista; si la lista está vacía, la valuación es válida.
     * 
     * @param session Sesión de usuario.
     * @param idValuation ID de la valuación.
     * @return Lista de movimientos de almacén que invalidan la valuación.
     * @throws Exception 
     */
    public static ArrayList<SDataDiog> isValuationDiogsValidById(SGuiSession session, final int idValuation) throws Exception {
        SDbStockValuation oVal = new SDbStockValuation();
        oVal.read(session, new int[] { idValuation });
        
        String sql = "SELECT DISTINCT d.id_year, d.id_doc "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON "
                    + "(d.id_year = de.id_year AND d.id_doc = de.id_doc) " 
                    + "WHERE d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(oVal.getDateStart()) + "' AND "
                    + "'" + SLibUtils.DbmsDateFormatDate.format(oVal.getDateEnd()) + "' " 
                    + "        AND (d.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(oVal.getTsUserUpdate()) + "' " 
                    + "        OR de.ts_edit >= '" + SLibUtils.DbmsDateFormatDatetime.format(oVal.getTsUserUpdate()) + "')"
                    + "AND NOT d.b_del AND NOT de.b_del;";
            
        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        ArrayList<SDataDiog> lDiogs = new ArrayList<>();
        while (res.next()) {
            SDataDiog oDiog = new SDataDiog();
            oDiog.read(new int [] { res.getInt("id_year"), res.getInt("id_doc") }, session.getStatement().getConnection().createStatement());
            lDiogs.add(oDiog);
        }
        
        return lDiogs;
    }
    
    /**
     * Determina si la valuación con el ID recibido es válida.
     * Consulta si hay una Requisición de Materiales actualizada después del timestamp de la última actualización de la valuación.
     * Si hay requisiciones que cumplen el criterio, las devuelve en la lista; si la lista está vacía, la valuación es válida.
     * 
     * @param session Sesión de usuario.
     * @param idValuation ID de la valuación.
     * @return Lista de requisiciones que invalidan la valuación.
     * @throws Exception 
     */
    public static ArrayList<SDbMaterialRequest> isValuationMatReqValidById(SGuiSession session, final int idValuation) throws Exception {
        String sql = "SELECT DISTINCT " +
                    " mr.id_mat_req " +
                    "FROM " +
                    " " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " AS v " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " AS vm ON v.id_stk_val = vm.fk_stk_val " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " AS mre ON vm.fk_mat_req_n = mre.id_mat_req " +
                    " AND vm.fk_mat_req_ety_n = mre.id_ety " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS mr ON mre.id_mat_req = mr.id_mat_req " +
                    "WHERE " +
                    "    NOT v.b_del AND NOT vm.b_del " +
                    "    AND v.id_stk_val = " + idValuation + " " +
                    "    AND mr.ts_usr_chg >= v.ts_usr_upd;";
            
        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        ArrayList<SDbMaterialRequest> lMatReqs = new ArrayList<>();
        while (res.next()) {
            SDbMaterialRequest oMatReq = new SDbMaterialRequest();
            oMatReq.read(session, new int[] { res.getInt("id_mat_req") });
            lMatReqs.add(oMatReq);
        }
        
        return lMatReqs;
    }

    /**
     * Verifica si en el periodo existen movimientos de entrada sin factura asociada.
     * 
     * @param session Sesión de usuario.
     * @param startDate Fecha de inicio.
     * @param cutDate Fecha de corte.
     * @return Mensaje con los movimientos sin factura, o vacío si no existen.
     * @throws SQLException 
     */
    public static String periodHasDiogsWithoutInvoice(SGuiSession session, Date startDate, Date cutDate) throws SQLException {
        String sql = "SELECT d.id_year, d.id_doc, d.dt, d.num, d.fid_dps_year_n, d.fid_dps_doc_n, "
                + "dps.num, dps.dt "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS dps ON (d.fid_dps_year_n = dps.id_year AND d.fid_dps_doc_n = dps.id_doc) "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " AS supp ON d.fid_dps_year_n = supp.id_src_year "
                + "        AND d.fid_dps_doc_n = supp.id_src_doc "
                + "WHERE NOT d.b_del "
                + "AND d.dt >= '" + SLibUtils.DbmsDateFormatDate.format(startDate) + "' "
                + "AND d.dt <= '" + SLibUtils.DbmsDateFormatDate.format(cutDate) + "' "
                + "AND d.fid_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN + " "
                + "AND dps.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] + " "
                + "AND dps.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[1] + " "
                + "AND dps.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[2] + " "
                + "AND COALESCE(supp.id_des_year, 0) = 0 "
                + "ORDER BY d.dt ASC, d.num ASC;";

        String sResult = "";
        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        while (res.next()) {
            sResult += "Folio: " + res.getString("d.num") + ", fecha: "
                    + SLibUtils.DateFormatDate.format(res.getDate("d.dt")) 
                    +  " y pedido con folio:" + res.getString("dps.num") + ", fecha: " 
                    + SLibUtils.DateFormatDate.format(res.getDate("dps.dt")) + ".\n";
        }
        if (! sResult.isEmpty()) {
            sResult = "Los movimientos de entrada al almacén: \n" + sResult
                    + "no tienen una factura asociada.\n";
        }

        return sResult;
    }
    
    /**
     * Actualiza el costo y la cantidad de una fila en la tabla TRN_STK según los parámetros recibidos.
     * 
     * @param session Sesión de usuario.
     * @param idYear Año.
     * @param idItem ID del ítem.
     * @param idUnit ID de la unidad.
     * @param idLot ID del lote.
     * @param idCob ID de la sucursal.
     * @param idWh ID del almacén.
     * @param idMov ID del movimiento.
     * @param dCost Costo a actualizar.
     * @param opType Tipo de operación (DEBIT/CREDIT).
     * @throws SQLException 
     */
    private static void updateTrnStockRowCost(SGuiSession session, final int idYear,
                                                                final int idItem, 
                                                                final int idUnit, 
                                                                final int idLot, 
                                                                final int idCob, 
                                                                final int idWh, 
                                                                final int idMov, 
                                                                final double dCost,
                                                                final int opType) throws SQLException {
        String sql = "UPDATE "
                + "" + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " SET "
                + "cost_u = " + SLibUtils.roundAmount(dCost) + ", ";
        
        if (DEBIT == opType) {
            sql += "debit = ROUND((" + SLibUtils.roundAmount(dCost) + " * mov_in), 2) ";
        }
        else {
            sql += "credit = ROUND((" + SLibUtils.roundAmount(dCost) + " * mov_out), 2) ";
        }

        sql += "WHERE (id_year = " + idYear + ") and "
                + "(id_item = " + idItem + ") and "
                + "(id_unit = " + idUnit + ") and "
                + "(id_lot = " + idLot + ") and "
                + "(id_cob = " + idCob + ") and "
                + "(id_wh = " + idWh + ") and "
                + "(id_mov = " + idMov + ");";

       session.getStatement().getConnection().createStatement().executeUpdate(sql);
    }
    
    /**
     * Actualiza el costo y la cantidad de una fila en la tabla TRN_STK según los datos del documento de inventario.
     * 
     * @param session Sesión de usuario.
     * @param idDiogYear Año del documento.
     * @param idDiogDoc ID del documento.
     * @param idDiogEty ID de la partida del documento.
     * @param dCost Costo a actualizar.
     * @param opType Tipo de operación (DEBIT/CREDIT).
     * @throws SQLException 
     */
    public static void updateTrnStockRowCostByDiog(SGuiSession session, 
                                                            final int idDiogYear, 
                                                            final int idDiogDoc, 
                                                            final int idDiogEty, 
                                                            final double dCost,
                                                            final int opType) throws SQLException {
        String sql = "SELECT id_year, id_item, id_unit, id_lot, id_cob, id_wh, id_mov "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                + "WHERE fid_diog_year = " + idDiogYear + " "
                + "AND fid_diog_doc = " + idDiogDoc + " "
                + "AND fid_diog_ety = " + idDiogEty + " "
                + "AND NOT b_del;";
        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        while (res.next()) {
            updateTrnStockRowCost(session,
                            res.getInt("id_year"), 
                            res.getInt("id_item"), 
                            res.getInt("id_unit"), 
                            res.getInt("id_lot"), 
                            res.getInt("id_cob"), 
                            res.getInt("id_wh"), 
                            res.getInt("id_mov"),
                            dCost,
                            opType);
        }
    }
    
    private static HashMap<String, ArrayList<SDbStockValuationMvt>> getNotConsumedValuationEntriesGrouped(SGuiSession session) throws Exception {
        String sql = "SELECT "
                + "ve.id_stk_val_mvt, "
                + "ve.fk_stk_val, "
                + "SUM(IF(ve.fk_ct_iog = 1, ve.qty_mov, ve.qty_mov * - 1)) AS qty, "
                + "ve.dt_mov, "
                + "ve.fk_diog_year_in_n, "
                + "ve.fk_diog_doc_in_n, "
                + "ve.fk_diog_ety_in_n, "
                + "ve.fk_dps_year_in_n, "
                + "ve.fk_dps_doc_in_n, "
                + "ve.fk_dps_ety_in_n, "
                + "ve.fk_item, "
                + "ve.fk_unit, "
                + "ve.fk_lot, "
                + "ve.cost_u, "
                + "ve.fk_cob,"
                + "ve.fk_wh, "
                + "dps.fid_ct_dps, "
                + "dps.fid_cl_dps, "
                + "dps.fid_tp_dps, "
                + "supp.id_des_year, "
                + "supp.id_des_doc "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " AS ve "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS diog_ety "
                + "         ON ve.fk_diog_year_in_n = diog_ety.id_year "
                + "        AND ve.fk_diog_doc_in_n = diog_ety.id_doc " 
                + "        AND ve.fk_diog_ety_in_n = diog_ety.id_ety "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS dps "
                + "        ON ve.fk_dps_year_in_n = dps.id_year "
                + "        AND ve.fk_dps_doc_in_n = dps.id_doc "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " AS supp ON ve.fk_dps_year_in_n = supp.id_src_year "
                + "        AND ve.fk_dps_doc_in_n = supp.id_src_doc "
                + "        AND ve.fk_dps_ety_in_n = supp.id_src_ety "
                + "WHERE "
                + "NOT ve.b_del "
                + "GROUP BY ve.fk_cob, ve.fk_wh, ve.fk_item, ve.fk_unit, ve.fk_lot, ve.cost_u, "
                + "ve.fk_diog_year_in_n, ve.fk_diog_doc_in_n, ve.fk_diog_ety_in_n "
                + "HAVING qty > 0 "
                + "ORDER BY ve.dt_mov ASC, ve.fk_stk_val ASC";

        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        SDbStockValuationMvt oEntry;
        HashMap<String, ArrayList<SDbStockValuationMvt>> groupedEntries = new HashMap<>();
        while (res.next()) {
            oEntry = new SDbStockValuationMvt();

            oEntry.setDateMove(res.getDate("dt_mov"));
            oEntry.setQuantityMovement(res.getDouble("qty"));
            oEntry.setCostUnitary(res.getDouble("cost_u"));
            oEntry.setPkStockValuationMvtId(res.getInt("id_stk_val_mvt"));
            oEntry.setFkStockValuationId(res.getInt("fk_stk_val"));
            oEntry.setFkDiogYearInId_n(res.getInt("fk_diog_year_in_n"));
            oEntry.setFkDiogDocInId_n(res.getInt("fk_diog_doc_in_n"));
            oEntry.setFkDiogEntryInId_n(res.getInt("fk_diog_ety_in_n"));
            oEntry.setFkDpsYearInId_n(res.getInt("fk_dps_year_in_n"));
            oEntry.setFkDpsDocInId_n(res.getInt("fk_dps_doc_in_n"));
            oEntry.setFkDpsEntryInId_n(res.getInt("fk_dps_ety_in_n"));
            oEntry.setFkItemId(res.getInt("fk_item"));
            oEntry.setFkUnitId(res.getInt("fk_unit"));
            oEntry.setFkLotId(res.getInt("fk_lot"));
            oEntry.setFkDiogCategoryId(SModSysConsts.TRNS_CT_IOG_IN);
            oEntry.setFkCompanyBranchId(res.getInt("fk_cob"));
            oEntry.setFkWarehouseId(res.getInt("fk_wh"));

            if (res.getInt("dps.fid_ct_dps") > 0 && res.getInt("dps.fid_cl_dps") > 0 && 
                    res.getInt("dps.fid_tp_dps") > 0) {
                oEntry.setAuxTypeDpsIn(new int[] {
                                            res.getInt("dps.fid_ct_dps"), 
                                            res.getInt("dps.fid_cl_dps"), 
                                            res.getInt("dps.fid_tp_dps") 
                                        });
                if (oEntry.getAuxTypeDpsIn()[0] == SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] &&
                    oEntry.getAuxTypeDpsIn()[1] == SModSysConsts.TRNU_TP_DPS_PUR_ORD[1] &&
                    oEntry.getAuxTypeDpsIn()[2] == SModSysConsts.TRNU_TP_DPS_PUR_ORD[2]) {
                        oEntry.setTemporalPrice(true);
                }
            }

            String key = oEntry.getFkItemId() + "-" + oEntry.getFkUnitId();
            groupedEntries.computeIfAbsent(key, k -> new ArrayList<>()).add(oEntry);
        }

        return groupedEntries;
    }
}

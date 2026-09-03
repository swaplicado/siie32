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
import erp.mod.trn.db.SDbStockValuationKardexNote;
import erp.mod.trn.db.SDbStockValuationMvt;
import erp.mod.trn.db.SDbStockValuationMvtNote;
import erp.mod.trn.db.SRowKardexRemaining;
import erp.mod.trn.utils.SStockValuationRecordUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public abstract class SStockValuationKardexUtils {

    public static void deleteKardex(SGuiSession session, final Date startDate, final Date cutDate) throws SQLException {
        String sql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_KARDEX) + " WHERE dt_mov < '" + SLibUtils.DbmsDateFormatDate.format(cutDate) + "'";
        try (Statement st = session.getStatement().getConnection().createStatement()) {
            st.execute(sql);
        }
    }

    protected static boolean isTypeMatch(int[] type, int[] expected) {
        return type[0] == expected[0] && type[1] == expected[1] && type[2] == expected[2];
    }

    protected static void addNote(SDbStockValuationKardex oKardex, String msg) {
        SDbStockValuationKardexNote note = new SDbStockValuationKardexNote();
        note.setNotes(msg);
        oKardex.getNotes().add(note);
    }

    /**
     * Aplica el costo unitario y totales según la naturaleza del documento. Si
     * es activo, fuerza costo a 0. Si el costo cambió, actualiza totales y
     * agrega nota.
     * @param oKardex
     * @param nature
     * @param newCostMxn
     * @param newCostCur
     * @param assetNoteMsg
     */
    protected static void applyCostByNature(SDbStockValuationKardex oKardex,
            SStockValuationRecordUtils.DocNature nature,
            double newCostMxn, double newCostCur, String assetNoteMsg) {
        boolean isAsset = nature.nature == SDataConstantsSys.TRNU_DPS_NAT_ASSET;
        oKardex.setCostUnitCurrency(isAsset ? 0d : newCostCur);
        oKardex.setTotalInCurrency(isAsset ? 0d : SLibUtils.roundAmount(newCostCur * oKardex.getQuantityIn()));
        double resolvedCost = isAsset ? 0d : newCostMxn;
        if (oKardex.getCostUnit() != resolvedCost) {
            oKardex.setCostUnit(resolvedCost);
            oKardex.setTotalIn(SLibUtils.roundAmount(oKardex.getQuantityIn() * resolvedCost));
            if (isAsset) {
                oKardex.setCostUnitCurrency(0d);
                oKardex.setTotalInCurrency(0d);
                addNote(oKardex, assetNoteMsg);
            }
        }
    }

    protected static List<SDbStockValuationKardex> getOutKardexOfStockMovements(SGuiSession session, final int[] pkDiogOut) throws SQLException, Exception {
        String sql = "";
        ArrayList<SDbStockValuationKardex> lKardexOuts = new ArrayList<>();

        sql = "SELECT id_stk_val_kardex FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_KARDEX) + " WHERE "
                + "fk_diog_year_out_n = " + pkDiogOut[0] + " AND "
                + "fk_diog_doc_out_n = " + pkDiogOut[1] + " AND "
                + "fk_diog_ety_out_n = " + pkDiogOut[2] + " AND "
                + "fk_ct_iog = " + SModSysConsts.TRNS_CT_IOG_OUT + " AND "
                + "b_del = 0;";

        try (Statement st = session.getStatement().getConnection().createStatement()) {
            ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                SDbStockValuationKardex oKardexConsumpt = new SDbStockValuationKardex(0);
                oKardexConsumpt.read(session, new int[]{res.getInt("id_stk_val_kardex")});
                lKardexOuts.add(oKardexConsumpt);
            }
        }

        return lKardexOuts;
    }

    protected static List<SDbStockValuationKardex> getOutKardexOfMainDpsEty(SGuiSession session,
                                                    final int[] pkDiogEtyIn,
                                                    final int[] pkMainDpsEtyIn) throws SQLException {
        List<SDbStockValuationKardex> lKardexOut = new ArrayList<>();
        SDbStockValuationKardex oKardex;
        String sql = "SELECT " +
                    "	k.* " +
                    "FROM " +
                    "	" + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_KARDEX) + " k " +
                    "WHERE " +
                    "	k.b_del = 0 " +
                    "	AND k.fk_ct_iog = " + SModSysConsts.TRNS_CT_IOG_OUT + " " +
                    "	AND k.fk_diog_year_in_n = " + pkDiogEtyIn[0] + " " +
                    "	AND k.fk_diog_doc_in_n = " + pkDiogEtyIn[1] + " " +
                    "	AND k.fk_diog_ety_in_n = " + pkDiogEtyIn[2] + " " +
                    "	AND k.fk_dps_year_in_main_n = " + pkMainDpsEtyIn[0] + " " +
                    "	AND k.fk_dps_doc_in_main_n = " + pkMainDpsEtyIn[1] + " " +
                    "	AND k.fk_dps_ety_in_main_n = " + pkMainDpsEtyIn[2] + " " + ";";
        try (Statement st = session.getStatement().getConnection().createStatement();
                ResultSet rs = st.executeQuery(sql);
                ) {
            while (rs.next()) {
                oKardex = new SDbStockValuationKardex(0);
                oKardex.setQuantityOut(rs.getDouble("qty_mov_out"));
                oKardex.setQuantityIn(0d);
                oKardex.setTotalOut(rs.getDouble("total_out"));
                oKardex.setTotalOutCurrency(rs.getDouble("total_out_cur"));
                oKardex.setFkStockValuationMovementId_n(rs.getInt("fk_stk_val_mvt_n"));
                lKardexOut.add(oKardex);
            }
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }

        return lKardexOut;
    }

    protected static HashMap<String, SRowKardexRemaining> getKardexRemaining(SGuiSession session) throws Exception {
        String sql = "SELECT "
                + "    k.fk_diog_year_in_n, k.fk_diog_doc_in_n, k.fk_diog_ety_in_n, "
                + "    k.dt_mov, k.fk_dps_year_in_main_n, k.fk_dps_doc_in_main_n, k.fk_dps_ety_in_main_n, k.fk_dps_cur_in_main_n, "
                + "    k.fk_item, k.fk_unit, k.fk_lot, k.fk_cob, k.fk_wh, k.exc_rate, "
                + "    MIN(k.id_stk_val_kardex) AS id_stk_val_kardex, "
                + "    SUM(k.qty_mov_in - k.qty_mov_out) AS qty_available, "
                + "    SUM(k.total_in - k.total_out) AS remaining, "
                + "    SUM(k.total_in_cur - k.total_out_cur) AS remaining_cur "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_KARDEX) + " k "
                + "WHERE k.b_del = 0 "
                + "GROUP BY k.fk_diog_year_in_n, k.fk_diog_doc_in_n, k.fk_diog_ety_in_n "
                + "HAVING qty_available > 0 "
                + "ORDER BY k.dt_mov ASC;";
        HashMap<String, SRowKardexRemaining> map = new HashMap<>();
        try (Statement st = session.getStatement().getConnection().createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                SRowKardexRemaining oRemaining = new SRowKardexRemaining();
                oRemaining.setPkKardexId(rs.getInt("id_stk_val_kardex"));
                oRemaining.setMovDate(rs.getDate("dt_mov"));
                oRemaining.setQtyAvailable(rs.getDouble("qty_available"));
                oRemaining.setRemaining(rs.getDouble("remaining"));
                oRemaining.setRemainingCurrency(rs.getDouble("remaining_cur"));
                oRemaining.setExchangeRate(rs.getDouble("exc_rate"));
                oRemaining.setFkDiogYearInId(rs.getInt("fk_diog_year_in_n"));
                oRemaining.setFkDiogDocInId(rs.getInt("fk_diog_doc_in_n"));
                oRemaining.setFkDiogEntryInId(rs.getInt("fk_diog_ety_in_n"));
                oRemaining.setFkDpsYearInMainId(rs.getInt("fk_dps_year_in_main_n"));
                oRemaining.setFkDpsDocInMainId(rs.getInt("fk_dps_doc_in_main_n"));
                oRemaining.setFkDpsEntryInMainId(rs.getInt("fk_dps_ety_in_main_n"));
                oRemaining.setFkDpsCurrencyInMainId(rs.getInt("fk_dps_cur_in_main_n"));
                oRemaining.setFkItemId(rs.getInt("fk_item"));
                oRemaining.setFkUnitId(rs.getInt("fk_unit"));
                oRemaining.setFkLotId(rs.getInt("fk_lot"));
                oRemaining.setFkCompanyBranchId(rs.getInt("fk_cob"));
                oRemaining.setFkWarehouseId(rs.getInt("fk_wh"));
                map.put(oRemaining.getDiogInKey(), oRemaining);
            }
        }
        return map;
    }

    protected static List<SRowKardexRemaining> getRemaingingByItemKey(String sKey, HashMap<String, SRowKardexRemaining> mRemaining) {
        List<SRowKardexRemaining> lRemaining = mRemaining.values().stream()
                .filter(p -> sKey.equals(p.getItemKey()))
                .sorted(Comparator.comparing(SRowKardexRemaining::getMovDate))
                .collect(Collectors.toList());

        return lRemaining;
    }

    /**
     * Resultado de resolver el costo de una entrada de almacén según el tipo
     * de DPS asociado (OC sin factura, OC con factura, factura directa).
     * Usado para evitar duplicar la lógica entre createValuationEntries y createKardexEntry.
     */
    public static class SResolvedEntryCost {
        public double costUnit;
        public double costUnitCur;
        public double totalIn;
        public double totalInCur;
        public double exchangeRate;
        public int fkCurrencyId;
        public int fkDpsYearMain;
        public int fkDpsDocMain;
        public int fkDpsEtyMain;
        public int fkDpsYearOrd;
        public int fkDpsDocOrd;
        public int fkDpsEtyOrd;
        public int fkCurrencyOrdId;
        public String auxDpsCostCenterCode;
        public final List<String> notes = new ArrayList<>();
    }

    /**
     * Resuelve el costo de una entrada de almacén a partir del ResultSet de
     * getStockMovementsQuery. Centraliza los 3 casos: OC sin factura, OC con
     * factura y factura directa. El resultado se aplica tanto al kardex como
     * al mvt de valuación.
     *
     * @param session           Sesión de usuario.
     * @param res               ResultSet posicionado en la fila actual.
     * @param priceDiffPercent  Porcentaje máximo de diferencia de precio permitido.
     * @param qty               Cantidad de entrada (mov_in).
     * @param baseCostUnit      Costo unitario base leído del stk (cost_u).
     * @param baseTotalIn       Total base leído del stk (debit).
     * @param auxTypeDpsIn      Tipo de DPS [ct, cl, tp].
     * @return SResolvedEntryCost con todos los valores calculados.
     * @throws Exception
     */
    public static SResolvedEntryCost resolveEntryCost(SGuiSession session,
            ResultSet res,
            double priceDiffPercent,
            double qty,
            double baseCostUnit,
            double baseTotalIn,
            int[] auxTypeDpsIn) throws Exception {
        SResolvedEntryCost r = new SResolvedEntryCost();
        r.costUnit = baseCostUnit;
        r.costUnitCur = baseCostUnit;
        r.totalIn = baseTotalIn;
        r.totalInCur = baseTotalIn;
        r.exchangeRate = 1d;
        r.fkCurrencyId = 0;
        r.fkDpsYearMain = res.getInt("stk.fid_dps_year_n");
        r.fkDpsDocMain  = res.getInt("stk.fid_dps_doc_n");
        r.fkDpsEtyMain  = res.getInt("stk.fid_dps_ety_n");
        r.fkDpsYearOrd  = 0;
        r.fkDpsDocOrd   = 0;
        r.fkDpsEtyOrd   = 0;
        r.fkCurrencyOrdId = 0;
        r.auxDpsCostCenterCode = "";

        boolean hasDps = r.fkDpsYearMain > 0 && r.fkDpsDocMain > 0 && r.fkDpsEtyMain > 0;
        if (!hasDps) {
            return r;
        }

        boolean isPurchaseOrder = isTypeMatch(auxTypeDpsIn, SModSysConsts.TRNU_TP_DPS_PUR_ORD);
        boolean isInvoice       = isTypeMatch(auxTypeDpsIn, SModSysConsts.TRNU_TP_DPS_PUR_INV);

        if (isPurchaseOrder && (res.getInt("id_des_year") == 0 || res.getInt("id_des_doc") == 0)) {
            // ── Caso 1: OC sin factura ──
            r.exchangeRate    = res.getDouble("exc_rate");
            r.fkCurrencyId    = res.getInt("fid_cur");
            r.fkDpsYearOrd    = res.getInt("stk.fid_dps_year_n");
            r.fkDpsDocOrd     = res.getInt("stk.fid_dps_doc_n");
            r.fkDpsEtyOrd     = res.getInt("stk.fid_dps_ety_n");
            r.fkCurrencyOrdId = res.getInt("fid_cur");
            r.auxDpsCostCenterCode = res.getString("dps_ety_cc");
            r.notes.add("OC sin factura. Mvt alm. num: " + res.getInt("d.num") + ", "
                    + "fecha " + SLibUtils.DateFormatDate.format(res.getDate("dt")) + ". "
                    + "\nPedido folio: " + res.getString("dps_num") + ", "
                    + "fecha: " + SLibUtils.DateFormatDate.format(res.getDate("dps_date")) + ".");

            SStockValuationRecordUtils.DocNature nature = SStockValuationRecordUtils.getDocumentNature(
                    session, r.fkDpsYearMain, r.fkDpsDocMain);
            boolean isAsset = nature.nature == SDataConstantsSys.TRNU_DPS_NAT_ASSET;
            r.costUnit    = isAsset ? 0d : res.getDouble("price_u_real_r");
            r.costUnitCur = isAsset ? 0d : res.getDouble("price_u_real_cur_r");
            r.totalIn     = SLibUtils.roundAmount(qty * r.costUnit);
            r.totalInCur  = SLibUtils.roundAmount(qty * r.costUnitCur);
            if (isAsset) {
                r.notes.add("La OC tiene naturaleza de activo, por lo que el costo unitario se establece en 0.");
            }
        }
        else if (isPurchaseOrder) {
            // ── Caso 2: OC con factura ──
            if (res.getInt("fid_cur") != res.getInt("des_fid_cur")) {
                throw new Exception("No se puede continuar con la valuación.\n"
                        + "El pedido y la factura asociados al movimiento de entrada al almacén "
                        + "con número de documento " + res.getInt("d.num") + " y "
                        + "fecha " + SLibUtils.DateFormatDate.format(res.getDate("dt")) + "\n "
                        + "tienen monedas diferentes.\n Factura: " + res.getString("des_num")
                        + ". Pedido folio: " + res.getString("dps_num") + ", "
                        + "fecha pedido: " + SLibUtils.DateFormatDate.format(res.getDate("dps_date")) + ".");
            }

            SStockValuationRecordUtils.DocNature nature = SStockValuationRecordUtils.getDocumentNature(
                    session, res.getInt("id_des_year"), res.getInt("id_des_doc"));
            boolean isAsset = nature.nature == SDataConstantsSys.TRNU_DPS_NAT_ASSET;

            if (!isAsset) {
                double orderPriceCur  = res.getDouble("price_u_real_cur_r");
                double invoicePriceCur = res.getDouble("ety_des_price_real_cur");
                if (orderPriceCur != 0 && Math.abs(invoicePriceCur - orderPriceCur) / orderPriceCur > priceDiffPercent) {
                    throw new Exception("No se puede continuar con la valuación.\n"
                            + "El pedido y la factura asociados al movimiento de entrada al almacén "
                            + "con número de documento " + res.getInt("d.num") + " y "
                            + "fecha " + SLibUtils.DateFormatDate.format(res.getDate("dt")) + "\n "
                            + "tienen diferencia de costo unitario mayor a la configurada.\n Factura: " + res.getString("des_num")
                            + ". Pedido folio: " + res.getString("dps_num") + ", "
                            + "fecha pedido: " + SLibUtils.DateFormatDate.format(res.getDate("dps_date")) + ".");
                }
                r.costUnitCur = invoicePriceCur;
                r.totalInCur  = SLibUtils.roundAmount(invoicePriceCur * qty);
            }
            else {
                r.costUnitCur = 0d;
                r.totalInCur  = 0d;
            }

            double newCost = isAsset ? 0d : res.getDouble("ety_des_price_real");
            if (baseCostUnit != newCost) {
                r.notes.add((isAsset
                        ? "La factura tiene naturaleza de activo, por lo que el costo unitario se establece en 0."
                        : "")
                        + "Entrada almacén número: " + res.getInt("d.num") + " y "
                        + "fecha: " + SLibUtils.DateFormatDate.format(res.getDate("dt")) + " "
                        + "tiene un costo unitario diferente a la factura: " + res.getString("des_num")
                        + ". Pedido folio: " + res.getString("dps_num") + ", "
                        + "fecha: " + SLibUtils.DateFormatDate.format(res.getDate("dps_date")) + ".");
            }
            r.costUnit   = newCost;
            r.totalIn    = SLibUtils.roundAmount(qty * newCost);
            r.fkDpsYearMain   = res.getInt("id_des_year");
            r.fkDpsDocMain    = res.getInt("id_des_doc");
            r.fkDpsEtyMain    = res.getInt("id_des_ety");
            r.exchangeRate    = res.getDouble("des_exc_rate");
            r.fkCurrencyId    = res.getInt("des_fid_cur");
            r.fkDpsYearOrd    = res.getInt("stk.fid_dps_year_n");
            r.fkDpsDocOrd     = res.getInt("stk.fid_dps_doc_n");
            r.fkDpsEtyOrd     = res.getInt("stk.fid_dps_ety_n");
            r.fkCurrencyOrdId = res.getInt("fid_cur");
            r.auxDpsCostCenterCode = res.getString("ety_des_cc");
        }
        else if (isInvoice) {
            // ── Caso 3: Factura directa ──
            r.exchangeRate = res.getDouble("exc_rate");
            r.fkCurrencyId = res.getInt("fid_cur");
            r.auxDpsCostCenterCode = res.getString("dps_ety_cc");

            SStockValuationRecordUtils.DocNature nature = SStockValuationRecordUtils.getDocumentNature(
                    session, r.fkDpsYearMain, r.fkDpsDocMain);
            boolean isAsset = nature.nature == SDataConstantsSys.TRNU_DPS_NAT_ASSET;
            double newCostMxn = isAsset ? 0d : res.getDouble("price_u_real_r");
            double newCostCur = isAsset ? 0d : res.getDouble("price_u_real_cur_r");
            if (baseCostUnit != newCostMxn) {
                r.notes.add("La entrada al almacén y la factura tienen un costo unitario distinto. "
                        + (isAsset ? "La factura tiene naturaleza de activo, por lo que el costo unitario se establece en 0. " : "")
                        + "Entrada almacén número: " + res.getInt("d.num") + " y "
                        + "fecha " + SLibUtils.DateFormatDate.format(res.getDate("dt")) + " "
                        + "tiene un costo unitario diferente a la factura: " + res.getString("dps_num") + ". ");
            }
            r.costUnit    = newCostMxn;
            r.costUnitCur = newCostCur;
            r.totalIn     = SLibUtils.roundAmount(qty * newCostMxn);
            r.totalInCur  = SLibUtils.roundAmount(qty * newCostCur);
        }

        return r;
    }

    public static SDbStockValuationMvt toMvt(SDbStockValuationKardex oKardex) {
        boolean isIn = oKardex.getFkStockValuationKardexTypeId() < 20;
        SDbStockValuationMvt oMvt = new SDbStockValuationMvt();
        oMvt.setFkStockValuationId(oKardex.getFkStockValuationId_n());
        oMvt.setFkStockValuationMvtId_n(oKardex.getFkStockValuationMovementId_n());
        oMvt.setFkDiogCategoryId(oKardex.getFkDiogCategoryId());
        oMvt.setDateMove(oKardex.getMovDate());
        oMvt.setExchangeRate(oKardex.getExchangeRate());
        oMvt.setCostUnitary(oKardex.getCostUnit());
        oMvt.setCostUnitaryCurrency(oKardex.getCostUnitCurrency());
        oMvt.setQuantityMovement(isIn ? oKardex.getQuantityIn() : oKardex.getQuantityOut());
        oMvt.setCost_r(isIn ? oKardex.getTotalIn() : oKardex.getTotalOut());
        oMvt.setCostCurrency_r(isIn ? oKardex.getTotalInCurrency() : oKardex.getTotalOutCurrency());
        oMvt.setFkDiogYearInId_n(oKardex.getFkDiogYearInId_n());
        oMvt.setFkDiogDocInId_n(oKardex.getFkDiogDocInId_n());
        oMvt.setFkDiogEntryInId_n(oKardex.getFkDiogEntryInId_n());
        oMvt.setFkDpsYearInId_n(oKardex.getFkDpsYearInMainId_n());
        oMvt.setFkDpsDocInId_n(oKardex.getFkDpsDocInMainId_n());
        oMvt.setFkDpsEntryInId_n(oKardex.getFkDpsEntryInMainId_n());
        oMvt.setFkDpsCurrencyInId_n(oKardex.getFkDpsCurrencyInMainId_n());
        oMvt.setFkDiogYearOutId_n(oKardex.getFkDiogYearOutId_n());
        oMvt.setFkDiogDocOutId_n(oKardex.getFkDiogDocOutId_n());
        oMvt.setFkDiogEntryOutId_n(oKardex.getFkDiogEntryOutId_n());
        oMvt.setFkDpsYearOutId_n(oKardex.getFkDpsYearOutMainId_n());
        oMvt.setFkDpsDocOutId_n(oKardex.getFkDpsDocOutMainId_n());
        oMvt.setFkDpsEntryOutId_n(oKardex.getFkDpsEntryOutMainId_n());
        oMvt.setFkDpsCurrencyOutId_n(oKardex.getFkDpsCurrencyOutMainId_n());
        oMvt.setFkMaterialRequestId_n(oKardex.getFkMatRequestId_n());
        oMvt.setFkMaterialRequestEntryId_n(oKardex.getFkMatRequestEntryId_n());
        oMvt.setFkItemId(oKardex.getFkItemId());
        oMvt.setFkUnitId(oKardex.getFkUnitId());
        oMvt.setFkLotId(oKardex.getFkLotId());
        oMvt.setFkCompanyBranchId(oKardex.getFkCompanyBranchId());
        oMvt.setFkWarehouseId(oKardex.getFkWarehouseId());
        
        oMvt.setAuxIsAdjust(oKardex.isAuxAdjust());
        oMvt.setAuxFkCostCenterId(oKardex.getAuxFkCostCenter());
        oMvt.setAuxDpsCostCenterCode(oKardex.getAuxDpsCostCenterCode());

        // convertir las notas del kardex a movement valuación:
        SDbStockValuationMvtNote oNote;
        for (SDbStockValuationKardexNote note : oKardex.getNotes()) {
            oNote = new SDbStockValuationMvtNote();
            oNote.setNotes(note.getNotes());
            oMvt.getNotes().add(oNote);
        }
        
        return oMvt;
    }

    /**
     * Verifica si ya existe un movimiento de entrada insertado previamente para
     * evitar duplicados en la tabla trn_stk_val_kardex.
     * 
     * @param session Sesión de usuario para ejecutar consultas.
     * @param diogYear Año del documento de almacén (fk_diog_year_in_n).
     * @param diogDoc Número del documento de almacén (fk_diog_doc_in_n).
     * @param diogEty Entrada del documento de almacén (fk_diog_ety_in_n).
     * @return true si ya existe, false si no existe.
     * @throws Exception
     */
    protected static boolean existsKardexEntry(SGuiSession session, int diogYear, int diogDoc, int diogEty) throws Exception {
        String sql = "SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_KARDEX) + " "
                + "WHERE fk_diog_year_in_n = " + diogYear
                + " AND fk_diog_doc_in_n = " + diogDoc
                + " AND fk_diog_ety_in_n = " + diogEty
                + " AND fk_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN
                + " AND b_del = 0 ";

        try (Statement st = session.getStatement().getConnection().createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}

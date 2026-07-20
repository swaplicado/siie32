/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.utils;

import erp.mod.SModConsts;
import static erp.mod.trn.utils.SStockValuationUtils.updateTrnStockRowCost;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SStockValuationUpdateStkUtils {
    
    /**
     * Actualiza las filas de stock de salida según los datos de valuación.
     * 
     * @param oSession
     * @param idValuation
     * @throws SQLException
     */
    public static void updateStockOutRows(SGuiSession oSession, final int idValuation) throws SQLException {
        String mainSql = "SELECT " +
                    "    s.*, " +
                    "    agg.mvts, " +
                    "    agg.fid_dps_nat, " +
                    "    agg.qty_t, " +
                    "    agg.cost_t, " +
                    "    ROUND(IF(s.mov_out <> 0, " +
                    "        agg.cost_t / s.mov_out, " +
                    "        0), 2) AS cost_stk, " +
                    "    ABS(s.credit - agg.cost_t) AS cost_r_diff " +
                    "FROM " +
                    "    " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s " +
                    "        INNER JOIN " +
                    "    ( " +
                    "        SELECT " +
                    "            mvt.fk_diog_year_out_n AS fid_diog_year, " +
                    "            mvt.fk_diog_doc_out_n AS fid_diog_doc, " +
                    "            mvt.fk_diog_ety_out_n AS fid_diog_ety, " +
                    "            GROUP_CONCAT(mvt.id_stk_val_mvt SEPARATOR '-') AS mvts, " +
                    "            GROUP_CONCAT(DISTINCT d.fid_dps_nat SEPARATOR ',') AS fid_dps_nat, " +
                    "            SUM(mvt.qty_mov) AS qty_t, " +
                    "            SUM(mvt.cost_r) AS cost_t " +
                    "        FROM " +
                    "            " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " AS mvt " +
                    "                INNER JOIN " +
                    "            " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " AS v ON mvt.fk_stk_val = v.id_stk_val " +
                    "                LEFT JOIN " +
                    "            " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON mvt.fk_dps_year_in_n = d.id_year " +
                    "                AND mvt.fk_dps_doc_in_n = d.id_doc " +
                    "        WHERE " +
                    "            mvt.b_del = 0 " +
                    "            AND v.b_del = 0 ";
        if (idValuation > 0) {
            mainSql += "AND v.id_stk_val = " + idValuation + " ";
        }
        mainSql += "        GROUP BY " +
                    "            mvt.fk_diog_year_out_n, " +
                    "            mvt.fk_diog_doc_out_n, " +
                    "            mvt.fk_diog_ety_out_n " +
                    "    ) AS agg ON s.fid_diog_year = agg.fid_diog_year " +
                    "        AND s.fid_diog_doc = agg.fid_diog_doc " +
                    "        AND s.fid_diog_ety = agg.fid_diog_ety " +
                    "WHERE " +
                    "    s.b_del = 0 " +
                    "    AND s.fid_ct_iog = 2 ";
        if (idValuation <= 0) {
            mainSql += "AND s.dt >= '2024-03-01' ";
        }
        mainSql += "AND ( " +
                   "    ROUND(IF(s.mov_out <> 0, agg.cost_t / s.mov_out, 0), 2) <> s.cost_u " +
                   "    OR ABS(s.credit - agg.cost_t) > 0 " +
                   ");";

        ResultSet resultSet = oSession.getStatement().executeQuery(mainSql);
        while (resultSet.next()) {
            System.out.println("Salida: " + resultSet.getString("dt") + ". "
                    + "Diferencia de costo: " + resultSet.getDouble("cost_r_diff") + ".");

            updateTrnStockRowCost(oSession,
                    resultSet.getInt("id_year"),
                    resultSet.getInt("id_item"),
                    resultSet.getInt("id_unit"),
                    resultSet.getInt("id_lot"),
                    resultSet.getInt("id_cob"),
                    resultSet.getInt("id_wh"),
                    resultSet.getInt("id_mov"),
                    resultSet.getDouble("cost_stk"),
                    SStockValuationUtils.CREDIT);
        }
    }

    /**
     * Actualiza las filas de stock de entrada según los datos de valuación.
     *
     * @param oSession Sesión de base de datos.
     * @param idValuation ID de la valuación.
     * @throws SQLException
     */
    public static void updateStockInRows(SGuiSession oSession, final int idValuation) throws SQLException {
        String mainSql = "SELECT " +
            "	ts.* , " +
            "	tsvm.fk_diog_year_in_n, " +
            "	tsvm.fk_diog_doc_in_n, " +
            "	tsvm.fk_diog_ety_in_n, " +
            "   tsvm.cost_u AS mvt_cost_u, " +
            "	tsvm.cost_r AS mvt_cost_r " +
            "FROM " +
            "	" + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " ts " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " tsvm on " +
            "	ts.fid_diog_year = tsvm.fk_diog_year_in_n " +
            "	AND ts.fid_diog_doc = tsvm.fk_diog_doc_in_n " +
            "	AND ts.fid_diog_ety = tsvm.fk_diog_ety_in_n " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " tsv on " +
            "	tsvm.fk_stk_val = tsv.id_stk_val " +
            "WHERE " +
            "	ts.fid_ct_iog = 1 " +
            "	AND ts.b_del = 0 " +
            "   AND tsv.b_del = 0 " +
            "	AND tsvm.fk_ct_iog = 1 " +
            "	AND tsvm.b_del = 0 ";

        if (idValuation > 0) {
            mainSql += "AND tsvm.fk_stk_val = " + idValuation + " ";
        }
        else {
            mainSql += "AND ts.dt >= '2024-03-01' ";
        }

        mainSql += "AND ts.debit <> tsvm.cost_r " +
            "	AND ABS(ts.debit - tsvm.cost_r) > 1;";

        ResultSet resultSet = oSession.getStatement().executeQuery(mainSql);
        while (resultSet.next()) {
            System.out.println("Entrada: " + resultSet.getString("dt") + ". "
                    + "Diferencia de costo: " + (resultSet.getDouble("debit") - resultSet.getDouble("mvt_cost_r")) + ".");

            updateTrnStockRowCost(oSession,
                    resultSet.getInt("id_year"),
                    resultSet.getInt("id_item"),
                    resultSet.getInt("id_unit"),
                    resultSet.getInt("id_lot"),
                    resultSet.getInt("id_cob"),
                    resultSet.getInt("id_wh"),
                    resultSet.getInt("id_mov"),
                    resultSet.getDouble("mvt_cost_u"),
                    SStockValuationUtils.DEBIT);
        }
    }
}

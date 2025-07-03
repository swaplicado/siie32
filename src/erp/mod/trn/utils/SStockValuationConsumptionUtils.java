/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.utils;

import erp.mod.trn.db.SDbStockValuationMvt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author Edwin Carmona
 */
public abstract class SStockValuationConsumptionUtils {
    
    public static List<SDbStockValuationMvt> getCardexMvt(Statement oStatement, int year, int doc, int ety) throws SQLException {
        String sql = "SELECT  " +
                "    COALESCE(dps.id_year, 0) AS f_id_1, " +
                "    COALESCE(dps.id_doc, 0) AS f_id_2, " +
                "    i.item_key AS f_code, " +
                "    '' AS f_name, " +
                "    val.dt_sta AS f_date, " +
                "    mvt.id_stk_val_mvt, " +
                "    val.dt_sta, " +
                "    val.dt_end, " +
                "    mvt.dt_mov, " +
                "    i.item_key, " +
                "    i.item, " +
                "    u.unit, " +
                "    u.symbol, " +
                "    mvt.qty_mov, " +
                "    mvt.cost_u, " +
                "    mvt.qty_mov * mvt.cost_u AS mvt_total, " +
                "    mvt.fk_ct_iog, " +
                "    ctd.ct_iog, " +
                "    CONCAT(di_in.num_ser, " +
                "            IF(LENGTH(di_in.num_ser) = 0, '', '-'), " +
                "            erp.lib_fix_int(di_in.num, 6)) AS din_num, " +
                "    tpii.tp_iog AS tp_diog_in, " +
                "    di_in.dt AS di_in_dt, " +
                "    tdps.code AS dps_type, " +
                "    CONCAT(dps.num_ser, " +
                "            IF(LENGTH(dps.num_ser) = 0, '', '-'), " +
                "            dps.num) AS f_num, " +
                "    dps.dt_doc, " +
                "    bp.bp, " +
                "    di_in.fid_dps_year_n, " +
                "    di_in.fid_dps_doc_n, " +
                "    COALESCE(dps.id_year, 0) AS dps_year, " +
                "    COALESCE(dps.id_doc, 0) AS dps_doc, " +
                "    CONCAT(di_out.num_ser, " +
                "            IF(LENGTH(di_out.num_ser) = 0, '', '-'), " +
                "            erp.lib_fix_int(di_out.num, 6)) AS dout_num, " +
                "    tpio.tp_iog AS tp_diog_out, " +
                "    di_out.dt AS di_out_dt, " +
                "    LPAD(mr.num, 6, 0) AS mr_num, " +
                "    mr.dt AS mr_dt, " +
                "    CONCAT(mpe.code, ' - ', mpe.name) AS prov_ent, " +
                "    re.fid_item_n, " +
                "    ir.item_key AS itm_ref_key, " +
                "    ir.item AS itm_ref, " +
                "    re.fid_cc_n, " +
                "    fcc.cc, " +
                "    re.fid_acc, " +
                "    re.debit, " +
                "    re.units, " +
                "    vacc.prorat_per * 100 AS _percnt, " +
                "    facc.acc, " +
                "    CONCAT(r.id_year, " +
                "            '-', " +
                "            erp.lib_fix_int(r.id_per, 2)) AS f_per, " +
                "    CONCAT(r.id_tp_rec, " +
                "            '-', " +
                "            erp.lib_fix_int(r.id_num, 6)) AS fin_num, " +
                "    r.id_year, " +
                "    r.id_per, " +
                "    r.id_bkc, " +
                "    r.id_tp_rec, " +
                "    r.id_num, " +
                "    re.sort_pos, " +
                "    r.dt, " +
                "    mvt.fk_lot, " +
                "    mvt.fk_cob, " +
                "    mvt.fk_wh, " +
                "    val.b_del AS b_del, " +
                "    val.fk_usr_ins AS fk_usr_ins, " +
                "    val.fk_usr_upd AS fk_usr_upd, " +
                "    val.ts_usr_ins AS ts_usr_ins, " +
                "    val.ts_usr_upd AS ts_usr_upd, " +
                "    ui.usr AS f_usr_ins, " +
                "    uu.usr AS f_usr_upd " +
                "FROM " +
                "    trn_stk_val_mvt AS mvt " +
                "        INNER JOIN " +
                "    trn_stk_val AS val ON mvt.fk_stk_val = val.id_stk_val " +
                "        INNER JOIN " +
                "    erp.itmu_item AS i ON mvt.fk_item = i.id_item " +
                "        INNER JOIN " +
                "    erp.itmu_unit AS u ON mvt.fk_unit = u.id_unit " +
                "        INNER JOIN " +
                "    trn_diog AS di_in ON mvt.fk_diog_year_in_n = di_in.id_year " +
                "        AND mvt.fk_diog_doc_in_n = di_in.id_doc " +
                "        INNER JOIN " +
                "    erp.trns_tp_iog AS tpii ON di_in.fid_ct_iog = tpii.id_ct_iog " +
                "        AND di_in.fid_cl_iog = tpii.id_cl_iog " +
                "        AND di_in.fid_tp_iog = tpii.id_tp_iog " +
                "        LEFT JOIN " +
                "    trn_dps AS dps ON mvt.fk_dps_year_in_n = dps.id_year " +
                "        AND mvt.fk_dps_doc_in_n = dps.id_doc " +
                "        LEFT JOIN " +
                "    erp.bpsu_bp AS bp ON dps.fid_bp_r = bp.id_bp " +
                "        LEFT JOIN " +
                "    erp.trnu_tp_dps AS tdps ON dps.fid_ct_dps = tdps.id_ct_dps " +
                "        AND dps.fid_cl_dps = tdps.id_cl_dps " +
                "        AND dps.fid_tp_dps = tdps.id_tp_dps " +
                "        INNER JOIN " +
                "    erp.trns_ct_iog AS ctd ON mvt.fk_ct_iog = ctd.id_ct_iog " +
                "        LEFT JOIN " +
                "    trn_stk_val_acc AS vacc ON mvt.id_stk_val_mvt = vacc.fk_stk_val_mvt " +
                "        LEFT JOIN " +
                "    fin_rec_ety AS re ON vacc.fk_fin_rec_year_n = re.id_year " +
                "        AND vacc.fk_fin_rec_per_n = re.id_per " +
                "        AND vacc.fk_fin_rec_bkc_n = re.id_bkc " +
                "        AND vacc.fk_fin_rec_tp_rec_n = re.id_tp_rec " +
                "        AND vacc.fk_fin_rec_num_n = re.id_num " +
                "        AND vacc.fk_fin_rec_ety_n = re.id_ety " +
                "        LEFT JOIN " +
                "    fin_rec AS r ON r.id_year = re.id_year " +
                "        AND r.id_per = re.id_per " +
                "        AND r.id_bkc = re.id_bkc " +
                "        AND r.id_tp_rec = re.id_tp_rec " +
                "        AND r.id_num = re.id_num " +
                "        LEFT JOIN " +
                "    erp.itmu_item AS ir ON re.fid_item_n = ir.id_item " +
                "        LEFT JOIN " +
                "    fin_cc AS fcc ON re.fid_cc_n = fcc.id_cc " +
                "        LEFT JOIN " +
                "    fin_acc AS facc ON re.fid_acc = facc.id_acc " +
                "        LEFT JOIN " +
                "    trn_diog AS di_out ON mvt.fk_diog_year_out_n = di_out.id_year " +
                "        AND mvt.fk_diog_doc_out_n = di_out.id_doc " +
                "        LEFT JOIN " +
                "    erp.trns_tp_iog AS tpio ON di_out.fid_ct_iog = tpio.id_ct_iog " +
                "        AND di_out.fid_cl_iog = tpio.id_cl_iog " +
                "        AND di_out.fid_tp_iog = tpio.id_tp_iog " +
                "        LEFT JOIN " +
                "    trn_mat_req AS mr ON mvt.fk_mat_req_n = mr.id_mat_req " +
                "        LEFT JOIN " +
                "    trn_mat_prov_ent AS mpe ON mr.fk_mat_prov_ent = mpe.id_mat_prov_ent " +
                "        INNER JOIN " +
                "    erp.usru_usr AS ui ON val.fk_usr_ins = ui.id_usr " +
                "        INNER JOIN " +
                "    erp.usru_usr AS uu ON val.fk_usr_upd = uu.id_usr " +
                "WHERE " +
                "    NOT mvt.b_del AND NOT val.b_del " +
                "        AND (vacc.b_del IS NULL OR NOT vacc.b_del) " +
                "        AND (r.b_del IS NULL OR NOT r.b_del) " +
                "        AND (re.b_del IS NULL OR NOT re.b_del) " +
                "        AND (re.fid_cc_n IS NOT NULL) " +
                "        AND mvt.fk_ct_iog = 2 " +
                "        AND mvt.fk_diog_year_in_n = " + year + " " +
                "        AND mvt.fk_diog_doc_in_n = " + doc + " " +
                "        AND mvt.fk_diog_ety_in_n = " + ety + " " +
                "ORDER BY val.dt_sta ASC , mvt.dt_mov ASC , mvt.fk_ct_iog ASC , mvt.id_stk_val_mvt ASC , r.id_year ASC , r.id_per ASC , r.id_bkc ASC , r.id_tp_rec ASC , r.id_num ASC , re.sort_pos ASC;";

        List<SDbStockValuationMvt> lMovements = new java.util.ArrayList<>();
        try (Statement statement = oStatement.getConnection().createStatement()) {
            ResultSet oRes = statement.executeQuery(sql);
            while (oRes.next()) {
                SDbStockValuationMvt oMvt = new SDbStockValuationMvt();

                oMvt.setPkStockValuationMvtId(oRes.getInt("id_stk_val_mvt"));
                oMvt.setDateMove(oRes.getDate("mvt.dt_mov"));
                oMvt.setAuxItemDescription(oRes.getString("i.item"));
                oMvt.setAuxDiogTypeDescription(oRes.getString("tp_diog_out"));
                oMvt.setAuxDiogData(oRes.getString("dout_num") + " " + oRes.getDate("di_out_dt"));
                oMvt.setAuxMaterialRequestData(oRes.getString("mr_num") + " " + oRes.getDate("mr_dt"));

                lMovements.add(oMvt);
            }
        }
        
        return lMovements;
    }
}

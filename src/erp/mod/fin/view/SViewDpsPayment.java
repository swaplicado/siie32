/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.data.SDataConstantsSys;
import erp.lib.SLibTimeUtilities;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRow;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Isabel Servín
 */
public class SViewDpsPayment extends SGridPaneView {

    private SGridFilterDatePeriod moFilterDatePeriod;
    
    public SViewDpsPayment(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_DPS_PAYS, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }
    
    @Override
    public void computeGridData() {
        try {
            Object filter;
            int period[] = null;
            int pk[] = null;
            int count = 0;
            if (moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD) != null) {
                filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
                if (filter != null) {
                    period = SLibTimeUtilities.digestYearMonth((SGuiDate) filter);
                }
            }
            for (SGridRow row : moModel.getGridRows()) {
                if (!SLibUtils.compareKeys(row.getRowPrimaryKey(), pk)) {
                    pk = row.getRowPrimaryKey();
                    
                    String sql = "SELECT COUNT(*) FROM fin_rec_ety " +
                            "WHERE fid_tp_acc_mov = 16 " +
                            "AND fid_cl_acc_mov = 1 " +
                            "AND fid_cls_acc_mov = 1 " +
                            "AND fid_ct_sys_mov_xxx = 4 " +
                            "AND fid_tp_sys_mov_xxx = 2 " +
                            "AND id_year <= " + period[0] + " AND id_per < " + period[1] + " " +
                            "AND fid_dps_year_n = " + row.getRowPrimaryKey()[0] + " " +
                            "AND fid_dps_doc_n = " + row.getRowPrimaryKey()[1];
                    ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
                    if (resultSet.next()) {
                        count = resultSet.getInt(1);
                    }
                }
                row.getRowValueAt(10);
                row.setRowValueAt(count + 1, 10);
                row.getRowValueAt(10);
                count++;
            }
        } catch (SQLException e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        int year = 0;

        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setDisabledApplying(false);
        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setDateApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        
        if (moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD) != null) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            if (filter != null) {
                year = SLibTimeUtilities.digestYear((SGuiDate) filter)[0];
                where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("r.dt", (SGuiDate) filter);
            }
        }
        
        msSql = "SELECT " +
                "d.id_year AS " + SDbConsts.FIELD_ID + "1, " +
                "d.id_doc AS " + SDbConsts.FIELD_ID + "2, " +
                "' ' AS " + SDbConsts.FIELD_CODE + ", " +
                "' ' AS " + SDbConsts.FIELD_NAME + ", " +
                "'' AS parc, " +
                "b.id_bp, b.bp, b.fiscal_id, bc.bp_key, bpb.bpb, re.ref, SUM(re.debit) AS f_debit, SUM(re.credit) AS f_credit, dt.code, cb.code, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, d.num_ref, d.dt, SUM(d.stot_r) AS f_stot, " +
                "SUM(d.tax_charged_r) AS f_tax_charged, SUM(d.tax_retained_r) AS f_tax_retained, SUM(d.tot_r) AS f_tot, " +
                "c.uuid, " +
                "agt.id_bp, agt.bp, r.id_bkc, r.id_tp_rec, r.id_num, r.dt, re.concept, bkc.code, cob.code, " +
                "CAST(CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, 2)) AS CHAR) as f_per, CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) as f_rec_num " +
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND r.b_del = 0 AND re.b_del = 0 AND r.id_year = " + year + " " +
                "AND " + where + " " +
                "AND (r.id_tp_rec = '" + SDataConstantsSys.FINU_TP_REC_CASH_BANK + "' OR r.id_tp_rec = '" + SDataConstantsSys.FINU_TP_REC_JOURNAL + "') " +
                "AND re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " " +
                "AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + " " +
                "AND re.b_exc_diff = 0 " +
                "INNER JOIN fin_bkc AS bkc ON r.id_bkc = bkc.id_bkc " +
                "INNER JOIN erp.bpsu_bpb AS cob ON r.fid_cob = cob.id_bpb " +
                "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON re.fid_bpb_n = bpb.id_bpb " +
                "INNER JOIN erp.bpsu_bp_ct AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = 2 " +
                "LEFT JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc " +
                "lEFT JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                "LEFT JOIN trn_cfd AS c ON d.id_year = c.fid_dps_year_n AND d.id_doc = c.fid_dps_doc_n " +
                "LEFT JOIN erp.bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
                "LEFT JOIN erp.bpsu_bp AS agt ON d.fid_sal_agt_n = agt.id_bp " +
                "GROUP BY b.id_bp, b.bp, bc.bp_key, d.num_ser, d.num, re.ts_new, d.dt, re.ref, re.debit, re.credit, dt.code, cb.code, d.stot_r, d.tax_charged_r, d.tax_retained_r, " +
                "d.tot_r, r.id_bkc, r.id_tp_rec, r.id_num, r.dt, re.concept, bkc.code, cob.code " +
                "ORDER BY b.bp, bc.bp_key, b.id_bp, re.ref, re.debit, re.credit, dt.code, " +
                "cb.code, d.num_ser, d.num, d.dt, d.stot_r, d.tax_charged_r, d.tax_retained_r, d.tot_r, r.dt, r.id_bkc, r.id_tp_rec, r.id_num, r.dt, re.concept, bkc.code, cob.code;";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column;
        ArrayList<SGridColumnView> columns = new ArrayList<>();
        
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "b.bp", "Asociado negocios"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "b.fiscal_id", "RFC", 100));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "bc.bp_key", "Clave AN", 50));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "bpb.bpb", "Sucursal AN", 100));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "d.dt", "Fecha doc."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "dt.code", "Tipo doc.", 35));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_num", "Folio doc.", 75));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "d.num_ref", "Referencia doc.", 50));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "cb.code", "Sucursal empresa", 35));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "uuid", "UUID"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "parc", "Parcialidad"));
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_stot", "Subtotal $");
        column.setSumApplying(true);
        columns.add(column);
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_tax_charged", "Imp tras $");
        column.setSumApplying(true);
        columns.add(column);
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_tax_retained", "Imp ret $");
        column.setSumApplying(true);
        columns.add(column);
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_tot", "Total $");
        column.setSumApplying(true);
        columns.add(column);
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_per", "Período póliza", 50));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "bkc.code", "Centro contable", 35));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "cob.code", "Sucursal empresa", 35));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_rec_num", "Folio póliza", 65));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "r.dt", "Fecha póliza"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "re.concept", "Concepto", 200));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_debit", "Cargos $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_credit", "Abonos $"));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.data.SDataConstantsSys;
import erp.gui.grid.SGridFilterPanelValMovType;
import erp.mod.SModConsts;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateRange;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Edwin Carmona
 */
public class SViewStockValuationDetail extends SGridPaneView implements ActionListener {

    private SGridFilterDateRange moFilterDateRange;
    private JButton mjbToSearch;
    private JButton mjbCleanSearch;
    private String msSeekQueryText;
    private JTextField moTextToSearch;
    private SGridFilterPanelValMovType moFilterMatReqStatus;
    
    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     */
    public SViewStockValuationDetail(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_STK_VAL_DET, SLibConsts.UNDEFINED, title, null);
        initComponents();
    }
    
    private void initComponents() {
        jbRowNew.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jbRowDelete.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowCopy.setEnabled(false);
        
        moFilterDateRange = new SGridFilterDateRange(miClient, this);
        int[] month = SLibTimeUtils.digestMonth(miClient.getSession().getCurrentDate());
        moFilterDateRange.initFilter(new Date[] {
            new SGuiDate(SGuiConsts.GUI_DATE_DATE, SViewStockValuationDetail.getStartDateOfMonth(month[0], month[1]).getTime()), 
            new SGuiDate(SGuiConsts.GUI_DATE_DATE, SViewStockValuationDetail.getEndDateOfMonth(month[0], month[1]).getTime())}
        );
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateRange);
        
        mjbToSearch = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/switch_filter.gif")), "Filtar", this);
        mjbCleanSearch = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif")), "Quitar filtro", this);
        
        moFilterMatReqStatus = new SGridFilterPanelValMovType(miClient, this);
        moFilterMatReqStatus.initFilter(mnGridMode);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateRange);
        moTextToSearch = new JTextField("");
        moTextToSearch.setPreferredSize(new Dimension(150, 23));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moTextToSearch);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbToSearch);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbCleanSearch);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterMatReqStatus);
        
        mjbToSearch.setEnabled(true);
        mjbCleanSearch.setEnabled(true);
        
        msSeekQueryText = "";
    }
    
    private void actionSearch() {
        if (jtTable.getRowCount() > 1) {
            String text = moTextToSearch.getText().trim();
            if (text.length() > 0) {
                msSeekQueryText = "(val.dt_sta LIKE '%" + text + "%' OR "
                        + "val.dt_end LIKE '%" + text + "%' OR "
                        + "mvt.dt_mov LIKE '%" + text + "%' OR "
                        + "i.item_key LIKE '%" + text + "%' OR "
                        + "i.item LIKE '%" + text + "%' OR "
                        + "u.symbol LIKE '%" + text + "%' OR "
                        + "ctd.ct_iog LIKE '%" + text + "%' OR "
                        + "di_in.num LIKE '%" + text + "%' OR "
                        + "tpii.tp_iog LIKE '%" + text + "%' OR "
                        + "tdps.code LIKE '%" + text + "%' OR "
                        + "CONCAT(dps.num_ser, "
                        + " IF(LENGTH(dps.num_ser) = 0, '', '-'), "
                        + " dps.num) LIKE '%" + text + "%' OR "
                        + "di_out.num LIKE '%" + text + "%' OR "
                        + "tpio.tp_iog LIKE '%" + text + "%' OR "
                        + "LPAD(mr.num, 6, 0) LIKE '%" + text + "%' OR "
                        + "mr.dt LIKE '%" + text + "%' OR "
                        + "re.fid_item_n LIKE '%" + text + "%' OR "
                        + "ir.item_key LIKE '%" + text + "%' OR "
                        + "ir.item LIKE '%" + text + "%' OR "
                        + "re.fid_cc_n LIKE '%" + text + "%' OR "
                        + "fcc.cc LIKE '%" + text + "%' OR "
                        + "re.fid_acc LIKE '%" + text + "%' OR "
                        + "facc.acc LIKE '%" + text + "%' OR "
                        + "bp.bp LIKE '%" + text + "%' OR "
                        + "CONCAT(r.id_tp_rec, "
                        + " '-', "
                        + " erp.lib_fix_int(r.id_num, 6))  LIKE '%" + text + "%' ";
                
                msSeekQueryText += ") ";
            }
            else {
                msSeekQueryText = "";
            }

            moTextToSearch.requestFocus();
            actionGridReload();
        }
    }
    
    private void actionCleanSearch() {
        moTextToSearch.setText("");
        msSeekQueryText = "";
        moTextToSearch.requestFocus();
        actionGridReload();
    }
    
    private static Date getStartDateOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private static Date getEndDateOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1, 23, 59, 59);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, maxDay);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
    
    private String getPeriodFilter(Date startDate, Date endDate) {
        String filt = "AND ((val.dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(endDate) + "' " +
                            "AND val.dt_end >= '" + SLibUtils.DbmsDateFormatDate.format(startDate) + "') " +
                            "OR (val.dt_sta >= '" + SLibUtils.DbmsDateFormatDate.format(startDate) + "' " +
                            "AND val.dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(endDate) + "') " +
                            "OR (val.dt_end >= '" + SLibUtils.DbmsDateFormatDate.format(startDate) + "' " +
                            "AND val.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(endDate) + "')) ";
        return filt;
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        Date[] dateRange = null;

        moPaneSettings = new SGridPaneSettings(1);

        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(false);
        
        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DATE_RANGE)).getValue();
        if (filter != null) {
            dateRange = (Date[]) filter;
            where += getPeriodFilter(dateRange[0], dateRange[1]);
        }
        
        if (moFiltersMap.get(SModConsts.TRNX_TP_VAL_MVT) != null && moFiltersMap.get(SModConsts.TRNX_TP_VAL_MVT).getValue() != null) {
            int tpMvtFilter = ((int[]) moFiltersMap.get(SModConsts.TRNX_TP_VAL_MVT).getValue())[0];
            if (tpMvtFilter > 0) {
                where += "AND mvt.fk_ct_iog = " + tpMvtFilter + " ";
            }
        }
        
        if (msSeekQueryText.length() > 0) {
            where += (where.isEmpty() ? "" : "AND ") + msSeekQueryText;
        }
        
        msSql = "SELECT val.id_stk_val AS " + SDbConsts.FIELD_ID + "1, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + "val.dt_sta AS " + SDbConsts.FIELD_DATE + ", "
                + "mvt.id_stk_val_mvt, "
                + "val.dt_sta, "
                + "val.dt_end, "
                + "mvt.dt_mov, "
                + "i.item_key, "
                + "i.item, "
                + "u.unit, "
                + "u.symbol, "
                + "mvt.qty_mov, "
                + "mvt.cost_u, "
                + "mvt.qty_mov * mvt.cost_u AS mvt_total, "
                + "mvt.fk_ct_iog, "
                + "ctd.ct_iog, "
                + "CONCAT(di_in.num_ser, IF(LENGTH(di_in.num_ser) = 0, '', '-'), "
                + " erp.lib_fix_int(di_in.num, " + SDataConstantsSys.NUM_LEN_IOG + ")) "
                + " AS din_num, "
                + "tpii.tp_iog AS tp_diog_in, "
                + "di_in.dt AS di_in_dt, "
                + "tdps.code AS dps_type, "
                + "CONCAT(dps.num_ser, "
                + "    IF(LENGTH(dps.num_ser) = 0, '', '-'), "
                + "    dps.num) AS f_num, "
                + "dps.dt_doc, "
                + "bp.bp, "
                + "di_in.fid_dps_year_n, "
                + "di_in.fid_dps_doc_n, "
                + "CONCAT(di_out.num_ser, IF(LENGTH(di_out.num_ser) = 0, '', '-'), "
                + " erp.lib_fix_int(di_out.num, " + SDataConstantsSys.NUM_LEN_IOG + ")) "
                + " AS dout_num, "
                + "tpio.tp_iog AS tp_diog_out, "
                + "di_out.dt AS di_out_dt, "
                + "LPAD(mr.num, " + SDataConstantsSys.NUM_LEN_MAT_REQ + ", 0) AS mr_num, "
                + "mr.dt AS mr_dt, "
                + "re.fid_item_n, "
                + "ir.item_key AS itm_ref_key, "
                + "ir.item AS itm_ref, "
                + "re.fid_cc_n, "
                + "fcc.cc, "
                + "re.fid_acc, "
                + "facc.acc, "
                + "CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, 2)) as f_per, "
                + "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) as fin_num, "
                + "r.id_year, "
                + "r.id_per, "
                + "r.id_bkc, "
                + "r.id_tp_rec, "
                + "r.id_num, "
                + "re.sort_pos, "
                + "r.dt, "
                + "mvt.fk_lot, "
                + "mvt.fk_cob, "
                + "mvt.fk_wh, "
                + "val.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "val.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "val.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "val.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "val.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " AS mvt "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " AS val ON mvt.fk_stk_val = val.id_stk_val "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON mvt.fk_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON mvt.fk_unit = u.id_unit "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS di_in ON mvt.fk_diog_year_in_n = di_in.id_year "
                + " AND mvt.fk_diog_doc_in_n = di_in.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_TP_IOG) + " AS tpii ON di_in.fid_ct_iog = tpii.id_ct_iog "
                + " AND di_in.fid_cl_iog = tpii.id_cl_iog "
                + " AND di_in.fid_tp_iog = tpii.id_tp_iog "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS dps ON di_in.fid_dps_year_n = dps.id_year "
                + " AND di_in.fid_dps_doc_n = dps.id_doc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON dps.fid_bp_r = bp.id_bp "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS tdps ON dps.fid_ct_dps = tdps.id_ct_dps "
                + " AND dps.fid_cl_dps = tdps.id_cl_dps "
                + " AND dps.fid_tp_dps = tdps.id_tp_dps "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_CT_IOG) + " AS ctd ON mvt.fk_ct_iog = ctd.id_ct_iog "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_ACC) + " AS vacc ON mvt.id_stk_val_mvt = vacc.fk_stk_val_mvt "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON vacc.fk_fin_rec_year = re.id_year "
                + " AND vacc.fk_fin_rec_per = re.id_per "
                + " AND vacc.fk_fin_rec_bkc = re.id_bkc "
                + " AND vacc.fk_fin_rec_tp_rec = re.id_tp_rec "
                + " AND vacc.fk_fin_rec_num = re.id_num "
                + " AND vacc.fk_fin_rec_ety = re.id_ety "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r ON r.id_year = re.id_year "
                + " AND r.id_per = re.id_per "
                + " AND r.id_bkc = re.id_bkc "
                + " AND r.id_tp_rec = re.id_tp_rec "
                + " AND r.id_num = re.id_num "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS ir ON re.fid_item_n = ir.id_item "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS fcc ON re.fid_cc_n = fcc.id_cc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS facc ON re.fid_acc = facc.id_acc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS di_out ON mvt.fk_diog_year_out_n = di_out.id_year "
                + " AND mvt.fk_diog_doc_out_n = di_out.id_doc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_TP_IOG) + " AS tpio ON di_out.fid_ct_iog = tpio.id_ct_iog "
                + " AND di_out.fid_cl_iog = tpio.id_cl_iog "
                + " AND di_out.fid_tp_iog = tpio.id_tp_iog "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS mr ON mvt.fk_mat_req_n = mr.id_mat_req "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON val.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON val.fk_usr_upd = uu.id_usr "
                + "WHERE "
                + "NOT mvt.b_del AND NOT val.b_del "
                + "AND (vacc.b_del IS NULL OR NOT vacc.b_del) "
                + "AND (r.b_del IS NULL OR NOT r.b_del) "
                + "AND (re.b_del IS NULL OR NOT re.b_del) "
                + where + " "
                + "ORDER BY val.dt_sta ASC , "
                + "mvt.dt_mov ASC , "
                + "mvt.fk_ct_iog ASC , "
                + "mvt.id_stk_val_mvt ASC, "
                + "r.id_year ASC, "
                + "r.id_per ASC, "
                + "r.id_bkc ASC, "
                + "r.id_tp_rec ASC, "
                + "r.id_num ASC, "
                + "re.sort_pos ASC;";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_sta", "Fecha inicio Val."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_end", "Fecha fin Val."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_mov", "Fecha Mvto. Alm."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "item_key", "Clave"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "item", "Ítem"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "symbol", "Unidad"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "qty_mov", "Cantidad"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "cost_u", "Costo unitario"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "mvt_total", "Total Mvto."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "din_num", "Fol. Mvto. Ent."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tp_diog_in", "Tipo Mvto. Ent."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "di_in_dt", "Fecha Mvto. Ent."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "dps_type", "Doc. compra"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_num", "Num. Doc."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_doc", "Fecha Doc."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "bp", "Asoc. negocios"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "dout_num", "Fol. Mvto. Sal."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tp_diog_out", "Tipo Mvto. Sal."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "di_out_dt", "Fecha Mvto. Sal."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "mr_num", "Req. Mat."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "mr_dt", "Fecha RM"));
        // ítem referencia
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "itm_ref_key", "Cod. Ref."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "itm_ref", "Itm Ref."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "fid_cc_n", "Ctro. costo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "cc", "Centro costo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "fid_acc", "Cta. contable"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "acc", "Cuenta contable"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "fin_num", "Póliza contable"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_STK_VAL);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == mjbToSearch) {
                actionSearch();
            }
            else if (button == mjbCleanSearch) {
                actionCleanSearch();
            }
        }
    }
}

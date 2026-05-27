/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.data.SDataConstantsSys;
import erp.lib.SLibTimeUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import sa.lib.SLibConsts;
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
 * Consulta de pagos y cobros por período a detalle de compras.
 * 2026-05-27, Sergio Flores: Para resumen y detalle de ventas y resumen de comrpas se usa la clase erp.mfin.view.SViewDpsPayment.
 * @author Isabel Servín, Sergio Flores
 */
public class SViewDpsPayment extends SGridPaneView implements ActionListener {
    
    private static final int COL_INSTALLMENT = 23;

    private SGridFilterDatePeriod moFilterDatePeriod;
    private javax.swing.JToggleButton mjCurrency;
    
    public SViewDpsPayment(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_DPS_PAYS, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new java.awt.Dimension(3, 23));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(separator);
        
        mjCurrency = new javax.swing.JToggleButton();
        mjCurrency.setPreferredSize(new java.awt.Dimension(23, 23));
        mjCurrency.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_money_off.gif")));
        mjCurrency.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_money_on.gif")));
        mjCurrency.addActionListener(this);
        updateCurrencyToolTip();
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjCurrency);
        
        setShowSums(!mjCurrency.isSelected()); // show totals row only when local currency is selected!
    }
    
    private void updateCurrencyToolTip() {
        mjCurrency.setToolTipText("<html>Mostrar moneda local o moneda de transacción (mostrando: <b>" + (!mjCurrency.isSelected() ? "moneda local" : "moneda de transacción") + "</b>)</html>");
    }

    @Override
    public void computeGridData() {
        try {
            // update in grid data installment number of payments:
            
            int period[] = null;
            
            if (moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD) != null) {
                SGuiDate filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
                if (filter != null) {
                    period = SLibTimeUtilities.digestYearMonth(filter);
                }
            }
            
            String sql = "SELECT COUNT(*) AS _entries "
                    + "FROM fin_rec AS r "
                    + "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num "
                    + "WHERE NOT r.b_del AND NOT re.b_del "
                    + "AND re.fid_tp_acc_mov = 16 "
                    + "AND re.fid_cl_acc_mov = 1 "
                    + "AND re.fid_cls_acc_mov = 1 "
                    + "AND re.fid_ct_sys_mov_xxx = 4 "
                    + "AND re.fid_tp_sys_mov_xxx = 2 "
                    + "AND re.fid_dps_year_n = ? AND re.fid_dps_doc_n = ? "
                    + "AND (r.id_year < ? OR (r.id_year = ? AND r.id_per <= ?));";
            
            try (PreparedStatement preparedStatement = miClient.getSession().getStatement().getConnection().prepareStatement(sql)) {
                for (SGridRow row : moModel.getGridRows()) {
                    int entries = 0;
                    int[] key = row.getRowPrimaryKey(); // convenience variable

                    if (key.length == moPaneSettings.getPrimaryKeyLength() && key[0] != 0 && key[1] != 0) {
                        preparedStatement.setInt(1, key[0]); // document's year
                        preparedStatement.setInt(2, key[0]); // document's ID
                        preparedStatement.setInt(3, period[0]); // record's year
                        preparedStatement.setInt(4, period[0]); // record's year
                        preparedStatement.setInt(5, period[1]); // record's month

                        ResultSet resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            entries = resultSet.getInt("_entries");
                        }
                    }

                    row.setRowValueAt(entries, COL_INSTALLMENT); // installment!
                }
            }
        }
        catch (SQLException e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        int year = 0;
        boolean isLocalCurrency = !mjCurrency.isSelected();

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
                "d.id_year AS " + SDbConsts.FIELD_ID + "1, "
                + "d.id_doc AS " + SDbConsts.FIELD_ID + "2, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + "b.id_bp, b.bp, b.fiscal_id, bc.bp_key, bb.bpb, "
                + "COALESCE(CONCAT(d.num_ser, IF(d.num_ser = '', '', '-'), d.num), 'ANTICIPO') AS f_num, d.num_ref, d.dt, "
                + (isLocalCurrency ? "d.stot_r AS f_stot, d.tax_charged_r AS f_tax_charged, d.tax_retained_r AS f_tax_retained, d.tot_r AS f_tot, " :
                "d.stot_cur_r AS f_stot, d.tax_charged_cur_r AS f_tax_charged, d.tax_retained_cur_r AS f_tax_retained, d.tot_cur_r AS f_tot, ")
                + "COALESCE(dt.code, '(N/A)') AS f_dt_code, dcob.code AS f_dcob_code, dn.code, c.uuid, "
                + "CONCAT(r.id_year, '-', LPAD(r.id_per, 2, '0')) as f_rec_per, "
                + "CONCAT(r.id_tp_rec, '-', LPAD(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ", '0')) as f_rec_num, "
                + "r.id_bkc, rbkc.code AS f_rbkc_code, r.fid_cob, rcob.code AS f_rcob_code, r.dt, re.id_ety, re.sort_pos, re.concept, "
                + (isLocalCurrency ? "SUM(re.debit) AS f_debit, SUM(re.credit) AS f_credit, " +
                SModSysConsts.CFGU_CUR_MXN + " AS f_id_cur, '" + miClient.getSession().getSessionCustom().getLocalCurrencyCode() + "' AS f_cur, " :
                "SUM(re.debit_cur) AS f_debit, SUM(re.credit_cur) AS f_credit, " +
                "re.fid_cur AS f_id_cur, c.cur_key AS f_cur, ")
                + "0 AS f_installment " // this value will be set in method computeGridData() after query is processed!
                + "FROM "
                + "fin_rec AS r "
                + "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "INNER JOIN fin_bkc AS rbkc ON r.id_bkc = rbkc.id_bkc "
                + "INNER JOIN erp.bpsu_bpb AS rcob ON r.fid_cob = rcob.id_bpb "
                + "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp "
                + "INNER JOIN erp.bpsu_bpb AS bb ON re.fid_bpb_n = bb.id_bpb "
                + "INNER JOIN erp.bpsu_bp_ct AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = 2 "
                + (isLocalCurrency ? "" : "INNER JOIN erp.cfgu_cur AS c ON re.fid_cur = c.id_cur ")
                + "LEFT JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc "
                + "LEFT JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps "
                + "LEFT JOIN erp.trnu_dps_nat AS dn ON d.fid_dps_nat = dn.id_dps_nat "
                + "LEFT JOIN trn_cfd AS c ON d.id_year = c.fid_dps_year_n AND d.id_doc = c.fid_dps_doc_n "
                + "LEFT JOIN erp.bpsu_bpb AS dcob ON d.fid_cob = dcob.id_bpb "
                + "WHERE "
                + "NOT r.b_del AND NOT re.b_del AND NOT re.b_exc_diff AND r.id_year = " + year + " AND " + where + " "
                + "AND (r.id_tp_rec = '" + SDataConstantsSys.FINU_TP_REC_CASH_BANK + "' OR r.id_tp_rec = '" + SDataConstantsSys.FINU_TP_REC_JOURNAL + "') "
                + "AND re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " "
                + "AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + " "
                + "GROUP BY "
                + "b.id_bp, b.bp, b.fiscal_id, bc.bp_key, bb.bpb, d.id_year, d.id_doc, d.num_ser, d.num, d.num_ref, d.dt, "
                + "dt.code, dcob.code, "
                + "r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num, rbkc.code, "
                + "r.fid_cob, rcob.code, r.dt, re.id_ety, re.sort_pos, re.concept"
                + (isLocalCurrency ? "" : ", c.id_cur, c.cur_key")
                + " "
                + "ORDER BY "
                + "b.bp, b.fiscal_id, bc.bp_key, bb.bpb, b.id_bp, "
                + "dt.code, f_num, d.num_ref, d.dt, d.id_year, d.id_doc, dcob.code, "
                + "f_rec_per, f_rec_num, rbkc.code, r.id_bkc, rcob.code, r.fid_cob, r.dt, re.sort_pos, re.concept"
                + (isLocalCurrency ? "" : ", c.cur_key, c.id_cur") + ";";
        
        setShowSums(!mjCurrency.isSelected()); // show totals row only when local currency is selected!
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column;
        ArrayList<SGridColumnView> columns = new ArrayList<>();
        
        /*
        IMPORTANTE: Si se cambian las columnas, hay que actualizar en concordancia los índices de columna de las constantes de esta clase.
        */
        
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "b.bp", "Asociado negocios")); // 0
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "b.fiscal_id", "RFC AN", 100));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "bc.bp_key", "Clave AN", 50));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "bb.bpb", "Sucursal AN", 100));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "d.dt", "Fecha doc."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_dt_code", "Tipo doc.", 35)); // 5
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_num", "Folio doc.", 75));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "d.num_ref", "Referencia doc.", 50));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_dcob_code", "Sucursal empresa doc.", 35));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "dn.code", "Naturaleza doc."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "c.uuid", "UUID doc.", 225)); // 10
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_stot", "Subtotal doc. $"); // referenced by index!
        column.setSumApplying(true);
        columns.add(column);
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_tax_charged", "Impto. tras. doc. $"); // referenced by index!
        column.setSumApplying(true);
        columns.add(column);
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_tax_retained", "Impto. ret. doc. $"); // referenced by index!
        column.setSumApplying(true);
        columns.add(column);
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_tot", "Total doc. $"); // referenced by index!
        column.setSumApplying(true);
        columns.add(column);
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "f_cur", "Moneda")); // 15
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_rec_per", "Período póliza", 50));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_rbkc_code", "Centro contable póliza", 35));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_rcob_code", "Sucursal empresa póliza", 35));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_rec_num", "Número póliza", 65));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "r.dt", "Fecha póliza")); // 20
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "re.concept", "Concepto partida póliza", 200));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "re.sort_pos", "# partida póliza"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "f_installment", "No. parcialidad"));
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_debit", "Cargos $");
        column.setSumApplying(true);
        columns.add(column);
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_credit", "Abonos $"); // 25
        column.setSumApplying(true);
        columns.add(column);
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "f_cur", "Moneda"));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
    }

    private void actionCurrency() {
        refreshGridWithRefresh();
        updateCurrencyToolTip();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mjCurrency) {
            actionCurrency();
        }
    }
}

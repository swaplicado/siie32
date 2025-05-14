/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SHrsUtils;
import erp.print.SDataConstantsPrint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Néstor Ávalos, Juan Barajas, Alfredo Perez, Sergio Flores
 */
public class SViewPayrollReceipt extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;

    private JButton jbPrintReceipt;
    private JButton jbSendReceipt;

    public SViewPayrollReceipt(SGuiClient client, String title, int subtype) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_PAY_RCP, subtype, title);
        initComponentCustom();
    }

    /*
     * Private methods
     */

    private void initComponentCustom() {
        setRowButtonsEnabled(false);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        
        jbPrintReceipt = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_print.gif")), "Imprimir recibo nómina", this);
        jbSendReceipt = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")), "Enviar recibo nómina vía mail", this);

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrintReceipt);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSendReceipt);
    }

    /*
     * Private methods
     */

    private void actionPrintReceipt() {
        if (jbPrintReceipt.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    try {
                        SHrsUtils.printPayrollReceipt(miClient, SDataConstantsPrint.PRINT_MODE_VIEWER,  gridRow.getRowPrimaryKey());
                    } 
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void actionSendReceipt() {
        if (jbSendReceipt.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (miClient.showMsgBoxConfirm("¿Está seguro que desea enviar vía mail el recibo de nómina?") == JOptionPane.YES_OPTION) {
                    try {
                        SHrsUtils.sendPayrollReceipt(miClient, SDataConstantsPrint.PRINT_MODE_PDF_FILE, gridRow.getRowPrimaryKey());
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }
    
    /*
     * Public methods
     */

    @Override
    public void prepareSqlQuery() {
        String sql = "p.fk_tp_pay = " + mnGridSubtype + " ";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("p.dt_end", (SGuiDate) filter);

        msSql = "SELECT "
                + "pr.id_pay AS " + SDbConsts.FIELD_ID + "1, "
                + "pr.id_emp AS " + SDbConsts.FIELD_ID + "2, "
                + "e.num AS " + SDbConsts.FIELD_CODE + ", "
                + "bp.bp AS " + SDbConsts.FIELD_NAME + ", "
                + "p.per_year, "
                + "p.num, "
                + "p.dt_sta, "
                + "p.dt_end, "
                + "p.nts, "
                + "e.b_act, "
                + "dep.name, "
                + "tpsc.code, "
                + "tps.name, "
                + "trs.name, "
                + "pr.ear_r, "
                + "pr.ded_r, "
                + "pr.pay_r, "
                + "pr.b_cfd_req, "
                + "pr.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "pr.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "pr.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "pr.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "pr.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
                + "(SELECT COUNT(*) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c ON c.fid_pay_rcp_pay_n = pri.id_pay AND c.fid_pay_rcp_emp_n = pri.id_emp AND c.fid_pay_rcp_iss_n = pri.id_iss "
                + "WHERE pri.id_pay = pr.id_pay AND pri.id_emp = pr.id_emp AND NOT pri.b_del AND c.fid_st_xml = " + SModSysConsts.TRNS_ST_DPS_NEW + ") AS _count_rcp_cfd_new, "
                + "(SELECT COUNT(*) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c ON c.fid_pay_rcp_pay_n = pri.id_pay AND c.fid_pay_rcp_emp_n = pri.id_emp AND c.fid_pay_rcp_iss_n = pri.id_iss "
                + "WHERE pri.id_pay = pr.id_pay AND pri.id_emp = pr.id_emp AND NOT pri.b_del AND c.fid_st_xml = " + SModSysConsts.TRNS_ST_DPS_EMITED + ") AS _count_rcp_cfd_emited, "
                + "(SELECT COUNT(*) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c ON c.fid_pay_rcp_pay_n = pri.id_pay AND c.fid_pay_rcp_emp_n = pri.id_emp AND c.fid_pay_rcp_iss_n = pri.id_iss "
                + "WHERE pri.id_pay = pr.id_pay AND pri.id_emp = pr.id_emp AND NOT pri.b_del AND c.fid_st_xml = " + SModSysConsts.TRNS_ST_DPS_ANNULED + ") AS _count_rcp_cfd_annuled, "
                + "IF((SELECT COUNT(*) > 0 "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c ON c.fid_pay_rcp_pay_n = pri.id_pay AND c.fid_pay_rcp_emp_n = pri.id_emp AND c.fid_pay_rcp_iss_n = pri.id_iss "
                + "WHERE pri.id_pay = pr.id_pay AND pri.id_emp = pr.id_emp AND NOT pri.b_del AND (NOT c.b_con OR c.b_prc_ws OR c.b_prc_sto_xml OR c.b_prc_sto_pdf)), " + SGridConsts.ICON_WARN + ", " + SGridConsts.ICON_NULL + ") AS _are_rcp_cfd_inc_prc "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_PAY_SHT_CUS) + " AS tpsc ON p.fk_tp_pay_sht_cus = tpsc.id_tp_pay_sht_cus "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY_SHT) + " AS tps ON p.fk_tp_pay_sht = tps.id_tp_pay_sht "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON pr.id_emp = e.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON pr.id_emp = bp.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON pr.fk_dep = dep.id_dep "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS trs ON pr.fk_tp_rec_sche = trs.id_tp_rec_sche "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON pr.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON pr.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE NOT p.b_del AND NOT pr.b_del AND " + sql)
                + "ORDER BY p.per_year, p.per, p.num, tpsc.code, tps.name, bp.bp, pr.id_pay, pr.id_emp ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "p.num", "Núm nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "p.dt_sta", "F inicial nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "p.dt_end", "F final nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "tpsc.code", "Tipo nómina empresa"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tps.name", "Tipo nómina"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, SDbConsts.FIELD_NAME, "Nombre empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, SDbConsts.FIELD_CODE, "Número empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "e.b_act", "Activo empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "dep.name", "Departamento recibo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "pr.ear_r", "Percepciones $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "pr.ded_r", "Deducciones $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "pr.pay_r", "Total neto $"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "trs.name", "Tipo régimen recibo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "p.nts", "Notas nómina", 200));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "pr.b_cfd_req", "CFDI requerido"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_are_rcp_cfd_inc_prc", "CFDI inconsistentes o errores (recibo)"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_count_rcp_cfd_new", "CFDI nuevos (recibo)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_count_rcp_cfd_emited", "CFDI timbrados (recibo)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_count_rcp_cfd_annuled", "CFDI anulados (recibo)"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.HRS_PAY);
        moSuscriptionsSet.add(SModConsts.HRS_PAY_RCP_ISS);
        moSuscriptionsSet.add(SModConsts.HRS_SIE_PAY);
        moSuscriptionsSet.add(SModConsts.TRN_CFD);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPrintReceipt) {
                actionPrintReceipt();
            }
            else if (button == jbSendReceipt) {
                actionSendReceipt();
            }
        }
    }
}

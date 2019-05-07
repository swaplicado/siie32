/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mod.SModConsts;
import erp.mod.hrs.db.SHrsUtils;
import erp.print.SDataConstantsPrint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;
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
public class SViewPayrollRow extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;

    private JButton jbPrintReceipt;
    private JButton jbSendReceipt;

    public SViewPayrollRow(SGuiClient client, String title, int subtype) {
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
        String sql = "v.fk_tp_pay = " + mnGridSubtype + " ";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "r.b_del = 0 ";
        }
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt_end", (SGuiDate) filter);

        msSql = "SELECT "
            + "r.id_pay AS " + SDbConsts.FIELD_ID + "1, "
            + "r.id_emp AS " + SDbConsts.FIELD_ID + "2, "
            + "(SELECT emp.num FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp WHERE emp.id_emp = r.id_emp) AS " + SDbConsts.FIELD_CODE + ", "
            + "(SELECT bp.bp FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp WHERE bp.id_bp = r.id_emp) AS " + SDbConsts.FIELD_NAME + ", "
            + "v.num AS f_num, "
            + "v.per_year, "
            + "v.per, "
            + "v.dt_sta, "
            + "v.dt_end, "
            + "v.rcp_day, "
            + "v.wrk_day, "
            + "v.mwz_wage, "
            + "v.mwz_ref_wage, "
            + "v.nts, "
            + "(SELECT name FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY_SHT) + " WHERE id_tp_pay_sht = v.fk_tp_pay_sht) AS tp_pay_sht, "
            + "v.b_ssc, "
            + "r.ear_r AS f_debit, "
            + "r.ded_r AS f_credit, "
            + "v.b_clo, "
            + "r.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
            + "v.fk_tp_pay, "
            + "v.fk_tp_mwz, "
            + "v.fk_tp_mwz_ref, "
            + "v.fk_tp_tax_comp, "
            + "v.fk_tax, "
            + "v.fk_tax_sub, "
            + "v.fk_ssc, "
            + "v.fk_usr_clo, "
            + "v.ts_usr_clo, "
            + "(SELECT t.code FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS t WHERE v.fk_tp_pay = t.id_tp_pay) AS f_pay_code, "
            + "(SELECT tm.code FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_MWZ) + " AS tm WHERE v.fk_tp_mwz_ref = tm.id_tp_mwz) AS f_mwz_code, "
            + "(SELECT tr.code FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_MWZ) + " AS tr WHERE v.fk_tp_mwz_ref = tr.id_tp_mwz) AS f_mwz_ref_code, "
            + "(SELECT tc.code FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_TAX_COMP) + " AS tc WHERE v.fk_tp_tax_comp = tc.id_tp_tax_comp) AS f_tax_comp, "
            + "uc.usr AS f_usr_close, "
            + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
            + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
            + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
            + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
            + "(COALESCE((SELECT SUM(pe.amt_r) "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON "
            + "p.id_pay = pr.id_pay AND pr.b_del = 0 "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pe ON "
            + "pr.id_pay = pe.id_pay AND pr.id_emp = pe.id_emp AND pe.b_del = 0 "
            + "WHERE pr.id_pay = r.id_pay AND pr.id_emp = r.id_emp "
            + "GROUP BY p.id_pay, pr.id_emp), 0) - "
            + "COALESCE((SELECT SUM(pd.amt_r) "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON "
            + "p.id_pay = pr.id_pay AND pr.b_del = 0 "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS pd ON "
            + "pr.id_pay = pd.id_pay AND pr.id_emp = pd.id_emp AND pd.b_del = 0 "
            + "WHERE pr.id_pay = r.id_pay AND pr.id_emp = r.id_emp "
            + "GROUP BY p.id_pay, pr.id_emp), 0)) AS f_total_net "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS v "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS r ON "
            + "v.id_pay = r.id_pay "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON "
            + "v.fk_usr_clo = uc.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
            + "v.fk_usr_ins = ui.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
            + "v.fk_usr_upd = uu.id_usr "
            + (sql.isEmpty() ? "" : "WHERE v.b_del = 0 AND " + sql)
            + "GROUP BY v.num, r.id_pay, r.id_emp "
            + "ORDER BY v.num, v.dt_sta, v.dt_end, " + SDbConsts.FIELD_NAME + ", r.id_pay, r.id_emp ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "v.f_num", "Número"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_sta", "F inicial"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_end", "F final"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "tp_pay_sht", "Tipo nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_clo", "Cerrada"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_debit", "Percepciones $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_credit", "Deducciones $"));
        
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "", "Alcance neto $");
        column.getRpnArguments().add(new SLibRpnArgument("f_debit", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument("f_credit", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        gridColumnsViews.add(column);
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "f_usr_close", "Usr cierre"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_usr_clo", "Usr TS cierre"));
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

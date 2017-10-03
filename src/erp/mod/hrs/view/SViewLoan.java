/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.gui.grid.SGridFilterPanelEmployee;
import erp.gui.grid.SGridFilterPanelLoan;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbLoan;
import erp.mod.hrs.db.SHrsConsts;
import erp.mod.hrs.form.SDialogLoanPaymentsCardex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateCutOff;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Juan Barajas
 */
public class SViewLoan extends SGridPaneView implements ActionListener {

    private SGridFilterDateCutOff moFilterDateCutOff;
    private SGridFilterPanelEmployee moFilterEmployee;
    private SGridFilterPanelLoan moFilterLoan;
    private JButton jbCloseLoan;
    private JButton jbCardex;
    
    private SDialogLoanPaymentsCardex moDialogLoanPaymentsCardex;
    private Date mtDateCut;
    
    public SViewLoan(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_LOAN, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(true, true, true, false, true);
        mtDateCut = null;
        
        moFilterDateCutOff = new SGridFilterDateCutOff(miClient, this);
        moFilterDateCutOff.initFilter((Date) null);

        moFilterEmployee = new SGridFilterPanelEmployee(miClient, this, SModConsts.HRSS_TP_PAY, SModConsts.HRSU_DEP);
        moFilterEmployee.initFilter(null);
        
        moFilterLoan = new SGridFilterPanelLoan(miClient, this, SModConsts.HRSS_TP_LOAN);
        moFilterLoan.initFilter(null);
        
        jbCloseLoan = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif")), "Cerrar crédito/préstamo", this);
        jbCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Ver movimientos", this);
        
        moDialogLoanPaymentsCardex = new SDialogLoanPaymentsCardex(miClient, "Control de crédito/préstamo");
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateCutOff);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCloseLoan);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCardex);
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterEmployee);
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterLoan);
    }

    private void actionCloseLoan() {
        SDbLoan loan = null;

        if (jbCloseLoan.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                    loan = (SDbLoan) miClient.getSession().readRegistry(SModConsts.HRS_LOAN, gridRow.getRowPrimaryKey());

                    if (miClient.showMsgBoxConfirm("Está por cerrar el crédito/préstamo '" + loan.getNumber() + "'.\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        loan.setClosed(!loan.isClosed());
                        loan.setFkUserClosedId(miClient.getSession().getUser().getPkUserId());
                        if (loan.canSave(miClient.getSession())) {
                            loan.save(miClient.getSession());
                        }
                        else {
                            miClient.showMsgBoxWarning(loan.getQueryResult());
                        }

                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                }
                catch (SQLException e) {
                    SLibUtils.showException(this, e);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionShowCardex() {
        SDbLoan loan = null;
        
        if (jbCardex.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    loan = (SDbLoan) miClient.getSession().readRegistry(SModConsts.HRS_LOAN, (int[]) gridRow.getRowPrimaryKey());
                    
                    moDialogLoanPaymentsCardex.setValue(SGuiConsts.PARAM_DATE_END, mtDateCut);
                    moDialogLoanPaymentsCardex.setValue(SModConsts.HRS_LOAN, loan);
                    moDialogLoanPaymentsCardex.setVisible(true);
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String sqlHaving = "";
        Object filter = null;
        String dateCutForPayroll = "";
        String dateCutForLoan = "";

        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();

        if (filter != null) {
            mtDateCut = (SGuiDate) filter;
            dateCutForPayroll = " AND p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(mtDateCut) + "' ";
            dateCutForLoan = " AND v.dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(mtDateCut) + "' ";
        }
            
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSS_TP_PAY)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "emp.fk_tp_pay = " + ((int[]) filter)[0] + " ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSU_DEP)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "emp.fk_dep = " + ((int[]) filter)[0] + " ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SGridFilterPanelEmployee.EMP_STATUS)).getValue();
        if (filter != null && ((int) filter) != SLibConsts.UNDEFINED) {
            if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ACT) {
                sql += (sql.isEmpty() ? "" : "AND ") + "emp.b_act = 1 ";
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_INA) {
                sql += (sql.isEmpty() ? "" : "AND ") + "emp.b_act = 0 ";
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ALL) {
            }
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSS_TP_LOAN))== null ? null : ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSS_TP_LOAN)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_tp_loan = " + ((int[]) filter)[0] + " ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SGridFilterPanelLoan.LOAN_STATUS))== null ? null : ((SGridFilterValue) moFiltersMap.get(SGridFilterPanelLoan.LOAN_STATUS)).getValue();
        if (filter != null && ((int) filter) != SLibConsts.UNDEFINED) {
            if ((int)filter == SGridFilterPanelLoan.LOAN_STATUS_OPEN) {
                sqlHaving = "HAVING (v.b_clo = 0 AND v.fk_tp_loan NOT IN(" + SModSysConsts.HRSS_TP_LOAN_HOM + ", " + SModSysConsts.HRSS_TP_LOAN_CON + ") AND _bal <> 0) OR (v.b_clo = 0 AND v.fk_tp_loan IN(" + SModSysConsts.HRSS_TP_LOAN_HOM + ", " + SModSysConsts.HRSS_TP_LOAN_CON + ")) ";
            }
            else if ((int)filter == SGridFilterPanelLoan.LOAN_STATUS_CLO) {
                sqlHaving = "HAVING (_bal = 0 AND v.fk_tp_loan NOT IN(" + SModSysConsts.HRSS_TP_LOAN_HOM + ", " + SModSysConsts.HRSS_TP_LOAN_CON + ")) OR v.b_clo = 1 ";
            }
            else if ((int)filter == SGridFilterPanelLoan.LOAN_STATUS_ALL) {
                sqlHaving = "";
            }
        }

        msSql = "SELECT "
                + "v.id_emp AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_loan AS " + SDbConsts.FIELD_ID + "2, "
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt_sta AS " + SDbConsts.FIELD_DATE + ", "
                + "v.dt_end_n AS " + SDbConsts.FIELD_DATE + "1, "
                + "v.cap, "
                + "v.tot_amt, "
                + "COALESCE(incs._inc, 0.0) AS _inc, "
                + "COALESCE(decs._dec, 0.0) AS _dec, "
                + "decs._last_move, "
                + "IF(v.fk_tp_loan IN(" + SModSysConsts.HRSS_TP_LOAN_HOM + ", " + SModSysConsts.HRSS_TP_LOAN_CON + "), 0, (v.tot_amt + "
                + "COALESCE(incs._inc, 0.0) - COALESCE(decs._dec, 0.0))) AS _bal, "
                + "v.pay_amt, "
                + "v.pay_fix, "
                + "v.pay_uma, "
                + "v.fk_tp_loan, "
                 + "IF(v.pay_per_ref = " + SHrsConsts.SAL_REF_SAL  + ", '" + SHrsConsts.TXT_SAL_REF_SAL + "',"
                 + "IF(v.pay_per_ref = " + SHrsConsts.SAL_REF_SAL_SS  + ", '" + SHrsConsts.TXT_SAL_REF_SAL_SS + "',"
                 + "IF(v.pay_per_ref = " + SHrsConsts.SAL_REF_SAL_FIX  + ", '" + SHrsConsts.TXT_SAL_REF_SAL_FIX + "', ''))) AS f_sal_ref, "
                + "v.pay_per, "
                + "v.pay_per_amt, "
                + "v.fk_tp_loan, "
                + "bp.bp, "
                + "vt.name, "
                + "vtp.name, "
                + "v.b_clo, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_clo AS " + SDbConsts.FIELD_USER_USR_ID + "_clo, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_clo AS " + SDbConsts.FIELD_USER_USR_TS + "_clo, "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "uc.usr AS " + SDbConsts.FIELD_USER_USR_NAME + "_clo, "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_LOAN) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_LOAN) + " AS vt ON "
                + "v.fk_tp_loan = vt.id_tp_loan "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_LOAN_PAY) + " AS vtp ON "
                + "v.fk_tp_loan_pay = vtp.id_tp_loan_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                + "v.id_emp = bp.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp ON "
                + "v.id_emp = emp.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON "
                + "v.fk_usr_clo = uc.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN (SELECT pre.fk_loan_emp_n AS _id_emp, pre.fk_loan_loan_n AS _id_loan, COALESCE(SUM(pre.amt_r), 0) AS _inc "
                + "FROM hrs_pay AS p "
                + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                + "INNER JOIN hrs_pay_rcp_ear AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                + "WHERE (p.id_pay = 0 OR NOT p.b_del) AND NOT pr.b_del AND NOT pre.b_del AND pre.fk_loan_emp_n IS NOT NULL "
                + (dateCutForPayroll.isEmpty() ? "" : dateCutForPayroll)
                + "GROUP BY pre.fk_loan_emp_n, pre.fk_loan_loan_n "
                + "ORDER BY pre.fk_loan_emp_n, pre.fk_loan_loan_n) "
                + "AS incs ON incs._id_emp = v.id_emp AND incs._id_loan = v.id_loan "
                + "LEFT OUTER JOIN (SELECT prd.fk_loan_emp_n AS _id_emp, prd.fk_loan_loan_n AS _id_loan, COALESCE(SUM(prd.amt_r), 0) AS _dec, MAX(p.dt_end) AS _last_move "
                + "FROM hrs_pay AS p "
                + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                + "INNER JOIN hrs_pay_rcp_ded AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp "
                + "WHERE (p.id_pay = 0 OR NOT p.b_del) AND NOT PR.b_del AND NOT prd.b_del AND prd.fk_loan_emp_n IS NOT NULL "
                + (dateCutForPayroll.isEmpty() ? "" : dateCutForPayroll)
                + "GROUP BY prd.fk_loan_emp_n, prd.fk_loan_loan_n "
                + "ORDER BY prd.fk_loan_emp_n, prd.fk_loan_loan_n) "
                + "AS decs ON decs._id_emp = v.id_emp AND decs._id_loan = v.id_loan "
                + (sql.isEmpty() ? "" : "WHERE " + sql + dateCutForLoan) + sqlHaving
                + "ORDER BY v.num, bp.bp, v.id_emp, vt.name, vtp.name, v.id_loan ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_NAME, "Número"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "bp.bp", "Empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vt.name", "Tipo crédito/préstamo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vtp.name", "Tipo pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha inicial"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE + "1", "Fecha final"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.cap", "Capital $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.tot_amt", "Total a pagar $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_bal", "Saldo $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_last_move", "Último pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pay_amt", "Monto $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_8D, "v.pay_fix", "Salarios mínimos"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "v.pay_per", "Porcentaje de salario base"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_8D, "v.pay_uma", "Número UMA"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_8D, "v.pay_per_amt", "Salario referencia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_sal_ref", "Salario referencia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_clo", "Cerrado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_USR_NAME + "_clo", SGridConsts.COL_TITLE_USER_USR_NAME + " cer"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_USR_TS + "_clo", SGridConsts.COL_TITLE_USER_USR_TS + " cer"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbCloseLoan) {
                actionCloseLoan();
            }
            else if (button == jbCardex) {
                actionShowCardex();
            }
        }
    }
}

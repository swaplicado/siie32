/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.gui.grid.SGridFilterPanelEmployee;
import erp.gui.grid.SGridFilterPanelLoan;
import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbLoan;
import erp.mod.hrs.db.SHrsConsts;
import erp.mod.hrs.form.SDialogLoanPaymentsCardex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Juan Barajas
 */
public class SViewLoan extends SGridPaneView implements ActionListener {

    private SGridFilterPanelEmployee moFilterEmployee;
    private SGridFilterPanelLoan moFilterLoan;
    private JButton jbCloseLoan;
    private JButton jbCardex;
    
    private SDialogLoanPaymentsCardex moDialogLoanPaymentsCardex;
    
    public SViewLoan(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_LOAN, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(true, true, true, false, true);

        moFilterEmployee = new SGridFilterPanelEmployee(miClient, this, SModConsts.HRSS_TP_PAY, SModConsts.HRSU_DEP);
        moFilterEmployee.initFilter(null);
        
        moFilterLoan = new SGridFilterPanelLoan(miClient, this, SModConsts.HRSS_TP_LOAN);
        moFilterLoan.initFilter(null);
        
        jbCloseLoan = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif")), "Cerrar crédito/préstamo", this);
        jbCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Ver movimientos", this);
        
        moDialogLoanPaymentsCardex = new SDialogLoanPaymentsCardex(miClient, "Control de crédito/préstamo");
        
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
                    
                    moDialogLoanPaymentsCardex.setValue(SModConsts.HRS_LOAN, loan);
                    moDialogLoanPaymentsCardex.setVisible(true);
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

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
                sql += (sql.isEmpty() ? "" : "AND ") + "v.b_clo = 0 ";
            }
            else if ((int)filter == SGridFilterPanelLoan.LOAN_STATUS_CLO) {
                sql += (sql.isEmpty() ? "" : "AND ") + "v.b_clo = 1 ";
            }
            else if ((int)filter == SGridFilterPanelLoan.LOAN_STATUS_ALL) {
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
                + "v.pay_amt, "
                + "v.pay_fix, "
                + "v.pay_per, "
                 + "IF(v.pay_per_ref = " + SHrsConsts.SAL_REF_SAL  + ", '" + SHrsConsts.TXT_SAL_REF_SAL + "',"
                 + "IF(v.pay_per_ref = " + SHrsConsts.SAL_REF_SAL_SS  + ", '" + SHrsConsts.TXT_SAL_REF_SAL_SS + "',"
                 + "IF(v.pay_per_ref = " + SHrsConsts.SAL_REF_SAL_FIX  + ", '" + SHrsConsts.TXT_SAL_REF_SAL_FIX + "', ''))) AS f_sal_ref, "
                + "v.pay_per_amt, "
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
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.num, bp.bp, v.id_emp, v.id_loan  ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_NAME, "Número"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "bp.bp", "Empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha inicial"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE + "1", "Fecha final"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.cap", "Capital $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.tot_amt", "Total a pagar $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "v.pay_amt", "Monto $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_8D, "v.pay_fix", "Salarios mínimos"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "v.pay_per", "Porcentaje de salario base"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_8D, "v.pay_per_amt", "Salarios referencia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vt.name", "Tipo crédito/préstamo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vtp.name", "Tipo pago"));
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

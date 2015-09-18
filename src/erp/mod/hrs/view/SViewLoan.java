/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbLoan;
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

    private JButton jbCloseLoan;
    private JButton jbCardex;
    
    private SDialogLoanPaymentsCardex moDialogLoanPaymentsCardex;
    
    public SViewLoan(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_LOAN, SLibConsts.UNDEFINED, title);

        jbCloseLoan = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif")), "Cerrar crédito/préstamo", this);
        jbCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Ver movimientos", this);
        
        moDialogLoanPaymentsCardex = new SDialogLoanPaymentsCardex(client, "Control de crédito/préstamo");
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCloseLoan);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCardex);
        
        setRowButtonsEnabled(true, true, true, false, true);
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

        msSql = "SELECT "
                + "v.id_emp AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_loan AS " + SDbConsts.FIELD_ID + "2, "
                + "num AS " + SDbConsts.FIELD_CODE + ", "
                + "num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt_sta AS " + SDbConsts.FIELD_DATE + ", "
                + "v.dt_end_n AS " + SDbConsts.FIELD_DATE + "1, "
                + "v.cap, "
                + "v.tot_amt, "
                + "v.pay_amt, "
                + "v.pay_fix, "
                + "v.pay_per, "
                + "bp.bp, "
                + "vt.code, "
                + "vtp.code, "
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

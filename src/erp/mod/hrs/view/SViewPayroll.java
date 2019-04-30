/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.cfd.SCfdConsts;
import erp.cfd.SDialogCfdProcessing;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mhrs.form.SDialogPayrollAccounting;
import erp.mhrs.form.SDialogPayrollCfdi;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbPayroll;
import erp.mod.hrs.db.SHrsCfdUtils;
import erp.mod.hrs.db.SHrsFinUtils;
import erp.mod.hrs.db.SHrsUtils;
import erp.mod.hrs.form.SDialogLayoutPayroll;
import erp.mod.hrs.form.SDialogRepHrsReportsPayroll;
import erp.mtrn.data.SCfdUtils;
import erp.print.SDataConstantsPrint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
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
 * @author Néstor Ávalos, Juan Barajas, Alfredo Perez, Claudio Peña, Sergio Flores
 */
public class SViewPayroll extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    
    private JButton jbCloseOpen;
    private JButton jbGenerateSign;
    private JButton jbPrintReports;
    private JButton jbGenerateLayout;
    private JButton jbSendReceipts;
    
    public SViewPayroll(SGuiClient client, String title, int subtype) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_PAY, subtype, title);
        initComponentCustom();
    }
    
    /*
     * Private methods
     */

    private void initComponentCustom() {
        setRowButtonsEnabled(true, false, true, false, true);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        jbCloseOpen = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_lock.gif")), "Cerrar/abrir nómina", this);
        jbGenerateSign = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_IMPORT), "Generar y timbrar CFDI", this);
        jbPrintReports = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT), "Imprimir reportes nómina", this);
        jbGenerateLayout = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif")), "Generar layout dispersión nómina", this);
        jbSendReceipts = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")), "Enviar recibos nómina", this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCloseOpen);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGenerateSign);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrintReports);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGenerateLayout);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSendReceipts);
    }

    @Override
    public void actionRowDelete() {
        if (jbRowDelete.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                
                if (miClient.showMsgBoxConfirm("Se eliminará la nómina '" + gridRow.getRowCode()+ "', y no es posible reactivar nóminas eliminadas. \n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                    super.actionRowDelete();
                }
            }
        }
    }
    
    private void actionCloseOpen() {
        boolean canClose = false;
        boolean close = false;
        SDbPayroll payroll = null;
        
        if (jbCloseOpen.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                    payroll = (SDbPayroll) miClient.getSession().readRegistry(SModConsts.HRS_PAY, gridRow.getRowPrimaryKey());
                    if (miClient.showMsgBoxConfirm("Está por " + (!payroll.isClosed() ? "cerrar" : "abrir") + " la nómina '" + payroll.getAuxPaymentType() + " - " + payroll.getNumber() + "'.\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        if (payroll.isClosed() && SHrsFinUtils.canOpenPayRoll(miClient.getSession(), payroll.getPkPayrollId())) {
                            SHrsFinUtils.deletePayRollRecords(miClient.getSession(), payroll.getPkPayrollId());
                            close = false; // Open payroll
                            canClose = true;
                        }
                        else {
                            if (miClient.showMsgBoxConfirm("¿Desea contabilizar la nómina?") == JOptionPane.YES_OPTION) {
                                SDialogPayrollAccounting dialog = new SDialogPayrollAccounting((SClientInterface) miClient, payroll);
                                dialog.resetForm();
                                dialog.setVisible(true);

                                if (dialog.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                    close = true; // Close payroll
                                    canClose = true;
                                }
                            }
                            else {
                                close = true; // Close payroll
                                canClose = true;
                            }
                        }
                        
                        if (close) {
                            SHrsUtils.createPayrollReceiptIssues(miClient.getSession(), payroll);
                            canClose = true;
                        }
                        else {
                            SHrsUtils.updateToNewStatusPayrollReceiptIssues(miClient.getSession(), payroll);
                            canClose = true;
                        }
                        
                        if (canClose) {
                            payroll.saveField(miClient.getSession().getStatement(), gridRow.getRowPrimaryKey(), SDbPayroll.FIELD_CLOSE, close); // Close payroll
                        }

                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionGenerateSign() {
        if (jbGenerateSign.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    int payroll = gridRow.getRowPrimaryKey()[0];
                    
                    if (SHrsCfdUtils.canGenetareCfdReceipts(miClient.getSession(), payroll)) {
                        SDialogPayrollCfdi payrollCfdi = new SDialogPayrollCfdi((SClientInterface) miClient, SHrsCfdUtils.getReceiptsPendig(miClient.getSession(), payroll));
                        payrollCfdi.resetForm();
                        payrollCfdi.setVisible(true);

                        if (payrollCfdi.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                            int stampsAvailable = SCfdUtils.getStampsAvailable((SClientInterface) miClient, SDataConstantsSys.TRNS_TP_CFD_PAYROLL, miClient.getSession().getCurrentDate(), SLibConsts.UNDEFINED);
                            SDialogCfdProcessing dialog = new SDialogCfdProcessing(miClient, "Procesamiento de timbrado y envío", SCfdConsts.PROC_REQ_STAMP);
                            dialog.setFormParams((SClientInterface) miClient, null, payrollCfdi.getPayrollEmployeeReceipts(), stampsAvailable, null, false, SCfdConsts.CFDI_PAYROLL_VER_CUR, SModSysConsts.TRNU_TP_DPS_ANN_NA);
                            dialog.setVisible(true);
                        }
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionPrintReports() {
        SDialogRepHrsReportsPayroll hrsReportsPayroll = null;
        if (jbPrintReports.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else {
                    try {
                        //SHrsUtils.printPrePayroll(miClient, gridRow.getRowPrimaryKey()[0]);
                        
                        hrsReportsPayroll = new SDialogRepHrsReportsPayroll(miClient, "Reportes nómina");
                        
                        hrsReportsPayroll.setReportsParams(gridRow.getRowPrimaryKey()[0]);
                        
                        hrsReportsPayroll.setVisible(true);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }
    
    private void actionGenerateLayout() {
        if (jbGenerateLayout.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else {
                    try {
                        new SDialogLayoutPayroll(miClient, gridRow.getRowPrimaryKey()[0], "Layout para dispersión de nóminas").setVisible(true);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void actionSendReceipts() {
        if (jbSendReceipts.isEnabled()) {
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
                        SHrsUtils.sendPayrollReceipts(miClient, SDataConstantsPrint.PRINT_MODE_PDF_FILE, gridRow.getRowPrimaryKey());
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

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt_end", (SGuiDate) filter);

        msSql = "SELECT "
            + "v.id_pay AS " + SDbConsts.FIELD_ID + "1, "
            + "v.num AS " + SDbConsts.FIELD_CODE + ", "
            + "'' AS " + SDbConsts.FIELD_NAME + ", "
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
            + "v.b_clo, "
            + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
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
            + "(SELECT COUNT(DISTINCT r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num) "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
            + "WHERE re.fid_pay_n = v.id_pay AND r.b_del = 0 AND re.b_del = 0) AS f_is_record, "
            + "uc.usr AS f_usr_close, "
            + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
            + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
            + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
            + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
            + "COALESCE((SELECT SUM(pe.amt_r) "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON "
            + "p.id_pay = pr.id_pay AND pr.b_del = 0 "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pe ON "
            + "pr.id_pay = pe.id_pay AND pr.id_emp = pe.id_emp AND pe.b_del = 0 "
            + "WHERE p.id_pay = v.id_pay "
            + "GROUP BY p.id_pay), 0) AS f_debit, "
            + "COALESCE((SELECT SUM(pd.amt_r) "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON "
            + "p.id_pay = pr.id_pay AND pr.b_del = 0 "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS pd ON "
            + "pr.id_pay = pd.id_pay AND pr.id_emp = pd.id_emp AND pd.b_del = 0 "
            + "WHERE p.id_pay = v.id_pay "
            + "GROUP BY p.id_pay), 0) AS f_credit, "
            + "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " WHERE b_del = 0 AND id_pay = v.id_pay GROUP BY id_pay) AS f_tot "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS v "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON "
            + "v.fk_usr_clo = uc.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
            + "v.fk_usr_ins = ui.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
            + "v.fk_usr_upd = uu.id_usr "
            + (sql.isEmpty() ? "" : "WHERE " + sql)
            + "GROUP BY v.num, v.id_pay "
            + "ORDER BY v.num, v.id_pay ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, SDbConsts.FIELD_CODE, "Número"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_sta", "F inicial"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_end", "F final"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_debit", "Percepciones $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_credit", "Deducciones $"));
        
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "", "Alcance neto $");
        column.getRpnArguments().add(new SLibRpnArgument("f_debit", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument("f_credit", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        gridColumnsViews.add(column);
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "tp_pay_sht", "Tipo nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_clo", "Cerrada"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "f_is_record", "Contabilizada"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.nts", "Notas"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, "f_tot", "Recibos totales", 100));
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
        moSuscriptionsSet.add(SModConsts.HRS_PAY_RCP);
        moSuscriptionsSet.add(SModConsts.HRS_PAY_RCP_ISS);
        moSuscriptionsSet.add(SModConsts.HRS_SIE_PAY);
        moSuscriptionsSet.add(SModConsts.TRN_CFD);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbCloseOpen) {
                actionCloseOpen();
            }
            else if (button == jbGenerateSign) {
                actionGenerateSign();
            }
            else if (button == jbPrintReports) {
                actionPrintReports();
            }
            else if (button == jbGenerateLayout) {
                actionGenerateLayout();
            }
            else if (button == jbSendReceipts) {
                actionSendReceipts();
            }
        }
    }
}

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
    private JButton jbGenerateSignCfdi;
    private JButton jbPrintReports;
    private JButton jbSendCfdi;
    private JButton jbGenerateBankLayout;
    
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
        jbGenerateSignCfdi = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_IMPORT), "Generar y timbrar recibos nómina", this);
        jbPrintReports = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT), "Imprimir reportes nómina", this);
        jbSendCfdi = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")), "Enviar recibos nómina vía mail", this);
        jbGenerateBankLayout = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif")), "Generar layout dispersión nómina", this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCloseOpen);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGenerateSignCfdi);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrintReports);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSendCfdi);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGenerateBankLayout);
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
        if (jbCloseOpen.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                    SDbPayroll payroll = (SDbPayroll) miClient.getSession().readRegistry(SModConsts.HRS_PAY, gridRow.getRowPrimaryKey());
                    if (miClient.showMsgBoxConfirm("Está por " + (!payroll.isClosed() ? "cerrar" : "abrir") + " la nómina '" + payroll.getAuxPaymentType() + " - " + payroll.getNumber() + "'.\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        boolean close = false;
                        boolean canClose = false;
                        
                        if (payroll.isClosed() && SHrsFinUtils.canOpenPayroll(miClient.getSession(), payroll.getPkPayrollId())) {
                            SHrsFinUtils.deletePayrollRecordEntries(miClient.getSession(), payroll.getPkPayrollId());
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
                            // payroll is being closed:
                            payroll.createPayrollReceiptIssues(miClient.getSession());
                            canClose = true;
                        }
                        else {
                            // payroll is being opened:
                            payroll.updatePayrollReceiptIssuesAsNewOnes(miClient.getSession());
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

    private void actionGenerateSignCfdi() {
        if (jbGenerateSignCfdi.isEnabled()) {
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
    
    private void actionSendCfdi() {
        if (jbSendCfdi.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (miClient.showMsgBoxConfirm("¿Está seguro que desea enviar vía mail los recibos de nómina?") == JOptionPane.YES_OPTION) {
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
 
    private void actionGenerateBankLayout() {
        if (jbGenerateBankLayout.isEnabled()) {
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
                + "v.num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.num, "
                + "v.dt_sta, "
                + "v.dt_end, "
                + "v.nts, "
                + "(SELECT COUNT(*) "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON "
                + " r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + " WHERE re.fid_pay_n = v.id_pay AND NOT r.b_del AND NOT re.b_del) AS _posted, "
                + "tps.name, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_clo, "
                + "v.fk_usr_clo, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_clo, "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "uc.usr AS _usr_close, "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
                + "COALESCE((SELECT SUM(pre.amt_r) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pr.id_pay = pre.id_pay AND pr.id_emp = pre.id_emp "
                + "WHERE p.id_pay = v.id_pay AND NOT pr.b_del AND NOT pre.b_del), 0.0) AS _sum_ears, "
                + "COALESCE((SELECT SUM(prd.amt_r) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON pr.id_pay = prd.id_pay AND pr.id_emp = prd.id_emp "
                + "WHERE p.id_pay = v.id_pay AND NOT pr.b_del AND NOT prd.b_del), 0.0) AS _sum_deds, "
                + "(SELECT COUNT(*) "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr "
                + " WHERE NOT pr.b_del AND pr.id_pay = v.id_pay) AS _count_rcps "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY_SHT) + " AS tps ON v.fk_tp_pay_sht = tps.id_tp_pay_sht "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON v.fk_usr_clo = uc.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.num, tps.name, v.id_pay ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "v.num", "Número nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_sta", "F inicial nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_end", "F final nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tps.name", "Tipo nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_sum_ears", "Percepciones $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_sum_deds", "Deducciones $"));
        
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "", "Alcance neto $");
        column.getRpnArguments().add(new SLibRpnArgument("_sum_ears", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument("_sum_deds", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        gridColumnsViews.add(column);
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.nts", "Notas nómina", 200));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "_count_rcps", "Recibos nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_clo", "Cerrada"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "_posted", "Contabilizada"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "_usr_close", "Usr cierre"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_usr_clo", "Usr TS cierre"));
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
            else if (button == jbGenerateSignCfdi) {
                actionGenerateSignCfdi();
            }
            else if (button == jbPrintReports) {
                actionPrintReports();
            }
            else if (button == jbSendCfdi) {
                actionSendCfdi();
            }
            else if (button == jbGenerateBankLayout) {
                actionGenerateBankLayout();
            }
        }
    }
}

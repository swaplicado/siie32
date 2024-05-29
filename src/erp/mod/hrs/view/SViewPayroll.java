/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.cfd.SCfdConsts;
import erp.cfd.SDialogCfdProcessing;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
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
import erp.mod.hrs.form.SDialogLayoutGroceryService;
import erp.mod.hrs.form.SDialogLayoutPayroll;
import erp.mod.hrs.form.SDialogRepHrsReportsPayroll;
import erp.mtrn.data.SCfdUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
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
    
    private JButton jbCloseOpenPayroll;
    private JButton jbCompleteAccounting;
    private JButton jbViewAccounting;
    private JButton jbGenerateSignCfdi;
    private JButton jbPrintReports;
    private JButton jbSendCfdi;
    private JButton jbGenerateLayoutBank;
    private JButton jbGenerateLayoutGroceryService;
    
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
        jbCloseOpenPayroll = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_lock.gif")), "Cerrar/abrir nómina", this);
        jbCompleteAccounting = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_bkk_csh.gif")), "Completar contabilización", this);
        jbViewAccounting = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_query_rec.gif")), "Ver contabilización", this);
        jbGenerateSignCfdi = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML_SIGN), "Generar y timbrar recibos nómina", this);
        jbPrintReports = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT), "Imprimir reportes nómina", this);
        jbSendCfdi = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")), "Enviar recibos nómina vía mail", this);
        jbGenerateLayoutBank = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif")), "Generar layout dispersión nómina", this);
        jbGenerateLayoutGroceryService = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_delivery.gif")), "Generar layout dispersión despensas", this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCloseOpenPayroll);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCompleteAccounting);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbViewAccounting);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGenerateSignCfdi);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrintReports);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSendCfdi);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGenerateLayoutBank);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGenerateLayoutGroceryService);
    }

    @Override
    public void actionRowDelete() {
        if (jbRowDelete.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                
                if (miClient.showMsgBoxConfirm("Se eliminará la nómina '" + gridRow.getRowCode()+ "', y no es posible recuperar nóminas eliminadas.\n"
                        + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                    super.actionRowDelete();
                }
            }
        }
    }
    
    private void actionCloseOpenPayroll() {
        if (jbCloseOpenPayroll.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    int action = 0;
                    boolean isAccounting = false;
                    boolean isAccountingGradual = false;
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDbPayroll payroll = (SDbPayroll) miClient.getSession().readRegistry(SModConsts.HRS_PAY, gridRow.getRowPrimaryKey());
                    
                    if (!payroll.isClosed()) {
                        // close payroll:
                        if (miClient.showMsgBoxConfirm("Está por CERRAR la nómina '" + payroll.composePayrollYearAndNumber() + "'.\n"
                                + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                            if (miClient.showMsgBoxConfirm("¿Desea contabilizar la nómina?") == JOptionPane.YES_OPTION) {
                                SDialogPayrollAccounting dialog = new SDialogPayrollAccounting((SClientInterface) miClient, payroll);
                                dialog.resetForm();
                                dialog.setVisible(true);

                                if (dialog.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                    action = SUtilConsts.ACTION_CLOSE;
                                    isAccounting = true;
                                    isAccountingGradual = dialog.isAccountingGradual();
                                }
                            }
                            else {
                                action = SUtilConsts.ACTION_CLOSE;
                            }
                        }
                    }
                    else {
                        // open payroll:
                        if (SHrsFinUtils.canOpenPayroll(miClient.getSession(), payroll.getPkPayrollId())) {
                            if (miClient.showMsgBoxConfirm("Está por ABRIR la nómina '" + payroll.composePayrollYearAndNumber() + "'.\n"
                                    + (payroll.isAccounting() ? "¡La nómina está contabilizada, y sus asientos contables serán eliminados!\n" : "")
                                    + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                                action = SUtilConsts.ACTION_OPEN;
                            }
                        }
                    }
                    
                    if (action != 0) {
                        switch (action) {
                            case SUtilConsts.ACTION_CLOSE:
                                payroll.updatePayrollReceiptIssuesOnClose(miClient.getSession());
                                
                                payroll.setAuxFkUserCloseId(miClient.getSession().getUser().getPkUserId());
                                payroll.saveField(miClient.getSession().getStatement(), payroll.getPrimaryKey(), SDbPayroll.FIELD_CLOSE, true);

                                if (isAccounting) {
                                    payroll.saveField(miClient.getSession().getStatement(), payroll.getPrimaryKey(), SDbPayroll.FIELD_ACCOUNTING, true);

                                    if (isAccountingGradual) {
                                        payroll.saveField(miClient.getSession().getStatement(), payroll.getPrimaryKey(), SDbPayroll.FIELD_ACCOUNTING_GRADUAL, true);
                                    }
                                }
                                break;

                            case SUtilConsts.ACTION_OPEN:
                                SHrsFinUtils.deletePayrollRecordEntries(miClient.getSession(), payroll.getPkPayrollId());
                                
                                payroll.updatePayrollReceiptIssuesOnOpen(miClient.getSession());
                                
                                payroll.setAuxFkUserCloseId(miClient.getSession().getUser().getPkUserId());
                                payroll.saveField(miClient.getSession().getStatement(), payroll.getPrimaryKey(), SDbPayroll.FIELD_CLOSE, false);
                                payroll.saveField(miClient.getSession().getStatement(), payroll.getPrimaryKey(), SDbPayroll.FIELD_ACCOUNTING, false);
                                payroll.saveField(miClient.getSession().getStatement(), payroll.getPrimaryKey(), SDbPayroll.FIELD_ACCOUNTING_GRADUAL, false);
                                break;

                            default:
                                // do nothing
                        }
                        
                        miClient.getSession().notifySuscriptors(mnGridType);
                        ((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(SDataConstants.FIN_REC);
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionCompleteAccounting() {
        if (jbCompleteAccounting.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDbPayroll payroll = (SDbPayroll) miClient.getSession().readRegistry(SModConsts.HRS_PAY, gridRow.getRowPrimaryKey());
                    
                    if (payroll.isClosed()) {
                        if (payroll.isAccounting()) {
                            if (payroll.isAccountingGradual()) {
                                SDialogPayrollAccounting dialog = new SDialogPayrollAccounting((SClientInterface) miClient, payroll);
                                dialog.resetForm();
                                dialog.setVisible(true);
                                
                                if (dialog.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                    miClient.getSession().notifySuscriptors(mnGridType);
                                }
                            }
                            else {
                                miClient.showMsgBoxWarning("Cuando se contabilizó la nómina '" + payroll.composePayrollYearAndNumber() + "' no se indicó que se haría gradualmente.");
                            }
                        }
                        else {
                            miClient.showMsgBoxWarning("Cuando se cerró la nómina '" + payroll.composePayrollYearAndNumber() + "' no se solicitó que fuera contabilizada.");
                        }
                    }
                    else {
                        miClient.showMsgBoxWarning("Para completar la contabilización de la nómina '" + payroll.composePayrollYearAndNumber() + "' es necesario que esté cerrada.");
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionViewAccounting() {
        if (jbViewAccounting.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDbPayroll payroll = (SDbPayroll) miClient.getSession().readRegistry(SModConsts.HRS_PAY, gridRow.getRowPrimaryKey());
                    
                    if (payroll.isClosed()) {
                        if (payroll.isAccounting()) {
                            int receipts = payroll.getPayrollReceiptsActiveCount();
                            String message = "Nómina: " + payroll.getName() + ".\n"
                                    + "Número de recibos: " + SLibUtils.DecimalFormatInteger.format(receipts) + ".\n"
                                    + "Modalidad de contabilización: " + (payroll.isAccountingGradual() ? "gradual": "un solo paso") + ".\n"
                                    + "Detalle de la contabilización:\n"
                                    + SHrsFinUtils.getAccountingRecords(miClient.getSession(), payroll.getPkPayrollId(), receipts);
                            
                            miClient.showMsgBoxInformation(message);
                        }
                        else {
                            miClient.showMsgBoxWarning("Cuando se cerró la nómina '" + payroll.composePayrollYearAndNumber() + "' no se solicitó que fuera contabilizada.");
                        }
                    }
                    else {
                        miClient.showMsgBoxWarning("Para ver la información de contabilización de la nómina '" + payroll.composePayrollYearAndNumber() + "' es necesario que esté cerrada y, por supuesto, contabilizada.");
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
                    int payrollId = gridRow.getRowPrimaryKey()[0];
                    
                    if (SHrsCfdUtils.canGenetareCfdReceipts(miClient.getSession(), payrollId)) {
                        SDialogPayrollCfdi payrollCfdi = new SDialogPayrollCfdi((SClientInterface) miClient, SHrsCfdUtils.getReceiptsPendig(miClient.getSession(), payrollId));
                        payrollCfdi.resetForm();
                        payrollCfdi.setVisible(true);

                        if (payrollCfdi.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                            int stampsAvailable = SCfdUtils.getStampsAvailable((SClientInterface) miClient, SDataConstantsSys.TRNS_TP_CFD_PAYROLL, miClient.getSession().getCurrentDate(), SLibConsts.UNDEFINED);
                            SDialogCfdProcessing dialog = new SDialogCfdProcessing(miClient, "Procesamiento de timbrado y envío", SCfdConsts.REQ_STAMP);
                            dialog.setFormParams(null, payrollCfdi.getPayrollEmployeeReceiptKeys(), stampsAvailable, null, false, SCfdConsts.CFDI_PAYROLL_VER_CUR, SModSysConsts.TRNU_TP_DPS_ANN_NA, "", "", false);
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
                        SHrsUtils.sendPayrollReceipts(miClient, gridRow.getRowPrimaryKey()[0]);
                    } 
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }
 
    private void actionGenerateLayoutBank() {
        if (jbGenerateLayoutBank.isEnabled()) {
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

    private void actionGenerateLayoutGroceryService() {
        if (jbGenerateLayoutGroceryService.isEnabled()) {
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
                        new SDialogLayoutGroceryService(miClient, gridRow.getRowPrimaryKey()[0]).setVisible(true);
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
        
        // set connection variables:
        
        try {
            miClient.getSession().getStatement().execute("SET @rcp = 0.0, @acc = 0.0;");
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        // prepare query:

        msSql = "SELECT "
                + "v.id_pay AS " + SDbConsts.FIELD_ID + "1, "
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.per_year, "
                + "v.num, "
                + "v.dt_sta, "
                + "v.dt_end, "
                + "v.hint, "
                + "v.nts, "
                + "tpsc.code, "
                + "tps.name, "
                + "trs.name, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_acc, "
                + "v.b_acc_grad, "
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
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pr.id_pay = pre.id_pay AND pr.id_emp = pre.id_emp "
                + "WHERE pr.id_pay = v.id_pay AND NOT pr.b_del AND NOT pre.b_del), 0.0) AS _sum_ear, "
                + "COALESCE((SELECT SUM(prd.amt_r) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON pr.id_pay = prd.id_pay AND pr.id_emp = prd.id_emp "
                + "WHERE pr.id_pay = v.id_pay AND NOT pr.b_del AND NOT prd.b_del), 0.0) AS _sum_ded, "
                + "@rcp := (SELECT COUNT(*) "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr "
                + " WHERE pr.id_pay = v.id_pay AND NOT pr.b_del) AS _count_rcp, "
                + "@acc := (SELECT COUNT(*) "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_PAY) + " AS ap "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_PAY_RCP) + " AS apr ON apr.id_pay = ap.id_pay AND apr.id_acc = ap.id_acc "
                + " WHERE ap.id_pay = v.id_pay AND NOT ap.b_del) AS _count_acc_rcp, "
                + "IF(NOT v.b_clo OR @rcp = 0, 0.0, @acc / @rcp) AS _acc_prc, "
                + "IF(NOT v.b_clo OR @rcp = 0, 0, @acc = @rcp) AS _acc_ok, "
                + "(SELECT COUNT(*) "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr "
                + " WHERE pr.id_pay = v.id_pay AND NOT pr.b_del AND NOT pr.b_cfd_req) AS _count_rcp_cfd_not_req, "
                + "(SELECT COUNT(DISTINCT pr.id_emp) "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri ON pri.id_pay = pr.id_pay AND pri.id_emp = pr.id_emp "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c ON c.fid_pay_rcp_pay_n = pri.id_pay AND c.fid_pay_rcp_emp_n = pri.id_emp AND c.fid_pay_rcp_iss_n = pri.id_iss "
                + " WHERE pr.id_pay = v.id_pay AND NOT pr.b_del AND pr.b_cfd_req AND NOT pri.b_del AND c.fid_st_xml = " + SModSysConsts.TRNS_ST_DPS_NEW + ") AS _count_rcp_cfd_new, "
                + "(SELECT COUNT(DISTINCT pr.id_emp) "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri ON pri.id_pay = pr.id_pay AND pri.id_emp = pr.id_emp "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c ON c.fid_pay_rcp_pay_n = pri.id_pay AND c.fid_pay_rcp_emp_n = pri.id_emp AND c.fid_pay_rcp_iss_n = pri.id_iss "
                + " WHERE pr.id_pay = v.id_pay AND NOT pr.b_del AND pr.b_cfd_req AND NOT pri.b_del AND c.fid_st_xml = " + SModSysConsts.TRNS_ST_DPS_EMITED + ") AS _count_rcp_cfd_emited, "
                + "IF((SELECT COUNT(DISTINCT pr.id_emp) > 0 "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr " 
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri ON pri.id_pay = pr.id_pay AND pri.id_emp = pr.id_emp " 
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c ON c.fid_pay_rcp_pay_n = pri.id_pay AND c.fid_pay_rcp_emp_n = pri.id_emp AND c.fid_pay_rcp_iss_n = pri.id_iss " 
                + " WHERE pr.id_pay = v.id_pay AND NOT pr.b_del AND pr.b_cfd_req AND NOT pri.b_del AND (NOT c.b_con OR c.b_prc_ws OR c.b_prc_sto_xml OR c.b_prc_sto_pdf)), " + SGridConsts.ICON_WARN + ", " + SGridConsts.ICON_NULL + ") AS _are_rcp_cfd_inc_prc, "
                + "(SELECT COUNT(DISTINCT pr.id_emp) "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr "
                + " WHERE pr.id_pay = v.id_pay AND NOT pr.b_del AND pr.b_cfd_req AND "
                + " NOT EXISTS (SELECT * FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri ON c.fid_pay_rcp_pay_n = pri.id_pay AND c.fid_pay_rcp_emp_n = pri.id_emp AND c.fid_pay_rcp_iss_n = pri.id_iss "
                + " WHERE pri.id_pay = pr.id_pay AND pri.id_emp = pr.id_emp AND NOT pri.b_del AND "
                + " c.fid_st_xml IN (" + SModSysConsts.TRNS_ST_DPS_NEW + ", " + SModSysConsts.TRNS_ST_DPS_EMITED + "))) AS _count_rcp_cfd_pending, "
                + "(SELECT COUNT(*) "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri ON pri.id_pay = pr.id_pay AND pri.id_emp = pr.id_emp "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c ON c.fid_pay_rcp_pay_n = pri.id_pay AND c.fid_pay_rcp_emp_n = pri.id_emp AND c.fid_pay_rcp_iss_n = pri.id_iss "
                + " WHERE pr.id_pay = v.id_pay AND NOT pr.b_del AND pr.b_cfd_req AND NOT pri.b_del AND c.fid_st_xml = " + SModSysConsts.TRNS_ST_DPS_NEW + ") AS _count_tot_cfd_new, "
                + "(SELECT COUNT(*) "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri ON pri.id_pay = pr.id_pay AND pri.id_emp = pr.id_emp "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c ON c.fid_pay_rcp_pay_n = pri.id_pay AND c.fid_pay_rcp_emp_n = pri.id_emp AND c.fid_pay_rcp_iss_n = pri.id_iss "
                + " WHERE pr.id_pay = v.id_pay AND NOT pr.b_del AND pr.b_cfd_req AND NOT pri.b_del AND c.fid_st_xml = " + SModSysConsts.TRNS_ST_DPS_EMITED + ") AS _count_tot_cfd_emited, "
                + "(SELECT COUNT(*) "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri ON pri.id_pay = pr.id_pay AND pri.id_emp = pr.id_emp "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c ON c.fid_pay_rcp_pay_n = pri.id_pay AND c.fid_pay_rcp_emp_n = pri.id_emp AND c.fid_pay_rcp_iss_n = pri.id_iss "
                + " WHERE pr.id_pay = v.id_pay AND NOT pr.b_del AND pr.b_cfd_req AND NOT pri.b_del AND c.fid_st_xml = " + SModSysConsts.TRNS_ST_DPS_ANNULED + ") AS _count_tot_cfd_annuled, "
                + "(SELECT COUNT(*) > 0 "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON "
                + " r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + " WHERE re.fid_pay_n = v.id_pay AND NOT r.b_del AND NOT re.b_del) AS _accounted "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_PAY_SHT_CUS) + " AS tpsc ON v.fk_tp_pay_sht_cus = tpsc.id_tp_pay_sht_cus "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY_SHT) + " AS tps ON v.fk_tp_pay_sht = tps.id_tp_pay_sht "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS trs ON v.fk_tp_rec_sche = trs.id_tp_rec_sche "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON v.fk_usr_clo = uc.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.per_year, v.per, v.num, tpsc.code, tps.name, v.id_pay ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "v.num", "Núm nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_sta", "F inicial nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_end", "F final nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "tpsc.code", "Tipo nómina empresa"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tps.name", "Tipo nómina"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_sum_ear", "Percepciones $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_sum_ded", "Deducciones $"));
        
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "", "Total neto $");
        column.getRpnArguments().add(new SLibRpnArgument("_sum_ear", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument("_sum_ded", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        gridColumnsViews.add(column);
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "trs.name", "Tipo régimen nómina", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.hint", "Texto aclaratorio (asunto correo-e recibos)", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.nts", "Notas nómina", 200));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_clo", "Cerrada"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "_acc_ok", "Contabilizada"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "_acc_prc", "Contabilización %", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_acc_grad", "Contabilización gradual"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_count_rcp", "Recibos"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_count_rcp_cfd_not_req", "CFDI no requeridos"));
        
        column = new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "", "CFDI requeridos");
        column.getRpnArguments().add(new SLibRpnArgument("_count_rcp", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument("_count_rcp_cfd_not_req", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        gridColumnsViews.add(column);
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_count_rcp_cfd_pending", "CFDI pendientes (recibos)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_count_rcp_cfd_new", "CFDI nuevos (recibos)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_count_rcp_cfd_emited", "CFDI timbrados (recibos)"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_are_rcp_cfd_inc_prc", "CFDI inconsistentes o errores (recibos)"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_count_tot_cfd_new", "CFDI nuevos (nómina)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_count_tot_cfd_emited", "CFDI timbrados (nómina)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_count_tot_cfd_annuled", "CFDI anulados (nómina)"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "_usr_close", "Usr cierre nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_usr_clo", "Usr TS cierre nómina"));
        
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

            if (button == jbCloseOpenPayroll) {
                actionCloseOpenPayroll();
            }
            else if (button == jbCompleteAccounting) {
                actionCompleteAccounting();
            }
            else if (button == jbViewAccounting) {
                actionViewAccounting();
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
            else if (button == jbGenerateLayoutBank) {
                actionGenerateLayoutBank();
            }
            else if (button == jbGenerateLayoutGroceryService) {
                actionGenerateLayoutGroceryService();
            }
        }
    }
}

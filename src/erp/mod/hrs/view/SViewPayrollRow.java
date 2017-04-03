/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.cfd.SCfdConsts;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mhrs.data.SDataPayrollReceiptIssue;
import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbPayroll;
import erp.mod.hrs.db.SDbPayrollReceiptIssue;
import erp.mod.hrs.db.SHrsPayrollAnnul;
import erp.mod.hrs.db.SHrsUtils;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.form.SDialogAnnulCfdi;
import erp.print.SDataConstantsPrint;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
 * @author Néstor Ávalos, Juan Barajas, Alfredo Perez
 */
public class SViewPayrollRow extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;

    private JButton jbPrintReceipt;
    private JButton jbSignXml;
    private JButton jbAnnul;
    private JButton jbGetXml;
    private JButton jbGetAcknowledgmentCancellation;
    private JButton jbPrint;
    private JButton jbPrintAcknowledgmentCancellation;
    private JButton jbSend;
    private JButton jbSendPayrollReceipt;
    private JButton jbVerifyCfdi;
    private JButton jbDiactivatePac;

    private SDialogAnnulCfdi moDialogAnnulCfdi;

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
        jbSignXml = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML_SIGN), "Timbrar CFDI", this);
        jbAnnul = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_ANNUL), "Anular recibo nómina", this);
        jbGetXml = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML), "Obtener XML del comprobante", this);
        jbGetAcknowledgmentCancellation = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML_CANCEL), "Obtener XML del acuse de cancelación del CFDI", this);
        jbPrint = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT), "Imprimir CFDI recibo nómina", this);
        jbPrintAcknowledgmentCancellation = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT_ACK_CAN), "Imprimir acuse de cancelación", this);
        jbSend = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")), "Enviar comprobante", this);
        jbSendPayrollReceipt = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")), "Enviar recibo de nómina", this);
        jbVerifyCfdi = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif")), "Verificar timbrado o cancelación del CFDI", this);
        jbDiactivatePac = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif")), "Limpiar inconsistencias de timbrado o cancelación del CFDI", this);
        
        moDialogAnnulCfdi = new SDialogAnnulCfdi((SClientInterface) miClient);

        /* XXX (jbarajas, 2016-08-16) slowly open payroll
        jbSignXml.setEnabled(true);
        jbAnnul.setEnabled(true);
        jbGetXml.setEnabled(true);
        jbGetAcknowledgmentCancellation.setEnabled(true);
        jbPrint.setEnabled(true);
        jbPrintAcknowledgmentCancellation.setEnabled(true);
        jbSend.setEnabled(true);
        jbVerifyCfdi.setEnabled(true);
        jbDiactivatePac.setEnabled(true);
        */
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrintReceipt);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSendPayrollReceipt);
        /* XXX (jbarajas, 2016-08-16) slowly open payroll
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSignXml);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAnnul);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGetXml);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGetAcknowledgmentCancellation);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrint);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrintAcknowledgmentCancellation);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSend);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbVerifyCfdi);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbDiactivatePac);
        */
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

    private void actionSignPayroll() {
        boolean needUpdate = false;
        SDataCfd cfd = null;
        SDbPayrollReceiptIssue receiptIssue = null;
        
        if (jbSignXml.isEnabled()) {
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
                    receiptIssue = new SDbPayrollReceiptIssue();
                    try {
                        cfd = SCfdUtils.getPayrollReceiptLastCfd((SClientInterface) miClient, SCfdConsts.CFDI_PAYROLL_VER_CUR, gridRow.getRowPrimaryKey());
                        if (cfd != null) {
                            receiptIssue.read(miClient.getSession(), new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n(), cfd.getFkPayrollReceiptIssueId_n() });
                        }
                        
                        if (receiptIssue.getPkIssueId() == SLibConsts.UNDEFINED) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n El recibo no ha sido emitido.");
                        }
                        else {
                            if (receiptIssue.isDeleted()) {
                                miClient.showMsgBoxWarning("El recibo '" + receiptIssue.getIssueNumber() + "' está eliminado.");
                            }
                            else if (receiptIssue.getFkReceiptStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                                miClient.showMsgBoxWarning("El recibo '" + receiptIssue.getIssueNumber() + "' está anulado.");
                            }
                            else if (!SDataUtilities.isPeriodOpen((SClientInterface) miClient, receiptIssue.getDateIssue())) {
                                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
                            }
                            else {
                                /* XXX jbarajas 04/02/2016 sign and sending CFDI
                                    needUpdate = SCfdUtils.signCfdi((SClientInterface) miClient, cfd, SCfdConsts.CFDI_PAYROLL_VER_CUR);
                                */
                                if (((SClientInterface) miClient).getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticHrs()) {
                                    needUpdate = SCfdUtils.signAndSendCfdi((SClientInterface) miClient, cfd, SCfdConsts.CFDI_PAYROLL_VER_CUR, true, true);
                                }
                                else {
                                    needUpdate = SCfdUtils.signCfdi((SClientInterface) miClient, cfd, SCfdConsts.CFDI_PAYROLL_VER_CUR);
                                }
                            }
                        }
                                
                        if (needUpdate) {
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void actionAnnulPayroll() {
        boolean needUpdate = false;
        SDataCfd cfd = null;
        SDataPayrollReceiptIssue receiptIssue = null;
        SHrsPayrollAnnul payrollAnnul = null;
        ArrayList<SDataCfd> cfds = null;
        ArrayList<SDataPayrollReceiptIssue> receiptIssues = null;
        SDbPayroll payroll = null;

        if (jbAnnul.isEnabled()) {
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
                        payroll = (SDbPayroll) miClient.getSession().readRegistry(SModConsts.HRS_PAY, new int[] { gridRow.getRowPrimaryKey()[0] });
                        
                        if (payroll.isClosed()) {
                            miClient.showMsgBoxWarning("La nómina debe estar abierta.");
                        }
                        else {
                            cfds = new ArrayList<SDataCfd>();
                            receiptIssues = new ArrayList<SDataPayrollReceiptIssue>();

                            cfd = SCfdUtils.getPayrollReceiptLastCfd((SClientInterface) miClient, SCfdConsts.CFDI_PAYROLL_VER_CUR, gridRow.getRowPrimaryKey());
                            if (cfd != null) {
                                cfds.add(cfd);
                            }

                            receiptIssue = new SDataPayrollReceiptIssue();

                            if (receiptIssue.read(new int[] { gridRow.getRowPrimaryKey()[0], gridRow.getRowPrimaryKey()[1], gridRow.getRowPrimaryKey()[2] }, miClient.getSession().getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n El recibo no ha sido emitido.");
                            }
                            receiptIssues.add(receiptIssue);
                            
                            moDialogAnnulCfdi.formReset();
                            moDialogAnnulCfdi.formRefreshCatalogues();
                            moDialogAnnulCfdi.setValue(SGuiConsts.PARAM_DATE, (cfds.isEmpty() ? miClient.getSession().getCurrentDate() : cfds.get(0).getTimestamp()));
                            moDialogAnnulCfdi.setValue(SModConsts.TRNS_TP_CFD, (cfds == null || cfds.isEmpty() ? cfd == null ? SLibConstants.UNDEFINED : cfd.getFkCfdTypeId() : cfds.get(0).getFkCfdTypeId()));
                            moDialogAnnulCfdi.setVisible(true);

                            if (moDialogAnnulCfdi.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                payrollAnnul = new SHrsPayrollAnnul((SClientInterface) miClient, cfds, receiptIssues, SCfdConsts.CFDI_PAYROLL_VER_CUR, false, moDialogAnnulCfdi.getDate(), moDialogAnnulCfdi.getAnnulSat(), moDialogAnnulCfdi.getDpsAnnulationType());
                                needUpdate = payrollAnnul.annulPayroll();
                            }

                            if (needUpdate) {
                                miClient.getSession().notifySuscriptors(mnGridType);
                            }

                            /*
                            if (cfd == null) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
                            }
                            else {
                                moDialogAnnulCfdi.formReset();
                                moDialogAnnulCfdi.formRefreshCatalogues();
                                moDialogAnnulCfdi.setValue(SGuiConsts.PARAM_DATE, cfd.getTimestamp());
                                moDialogAnnulCfdi.setVisible(true);

                                if (moDialogAnnulCfdi.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                    needUpdate = SCfdUtils.cancelCfdi((SClientInterface) miClient, cfd, SCfdConsts.CFDI_PAYROLL_VER_CUR, moDialogAnnulCfdi.getDate(), moDialogAnnulCfdi.getAnnulSat());
                                }

                                if (needUpdate) {
                                    miClient.getSession().notifySuscriptors(mnGridType);
                                }
                            }
                            */
                        }
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void actionGetXml() {
        if (jbGetXml.isEnabled()) {
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
                        SCfdUtils.getXmlCfd((SClientInterface) miClient, SCfdUtils.getPayrollReceiptLastCfd((SClientInterface) miClient, SCfdConsts.CFDI_PAYROLL_VER_CUR, gridRow.getRowPrimaryKey()));
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void actionGetAcknowledgmentCancellation() {
        if (jbGetAcknowledgmentCancellation.isEnabled()) {
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
                        SCfdUtils.getAcknowledgmentCancellationCfd((SClientInterface) miClient, SCfdUtils.getPayrollReceiptLastCfd((SClientInterface) miClient, SCfdConsts.CFDI_PAYROLL_VER_CUR, gridRow.getRowPrimaryKey()));
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void actionPrint() {
        if (jbPrint.isEnabled()) {
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
                        SCfdUtils.printCfd((SClientInterface) miClient, SCfdConsts.CFD_TYPE_PAYROLL, SCfdUtils.getPayrollReceiptLastCfd((SClientInterface) miClient, SCfdConsts.CFDI_PAYROLL_VER_CUR, gridRow.getRowPrimaryKey()), SCfdConsts.CFDI_PAYROLL_VER_CUR);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void actionPrintAcknowledgmentCancellation() {
        if (jbPrintAcknowledgmentCancellation.isEnabled()) {
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
                        SCfdUtils.printAcknowledgmentCancellationCfd((SClientInterface) miClient, SCfdUtils.getPayrollReceiptLastCfd((SClientInterface) miClient, SCfdConsts.CFDI_PAYROLL_VER_CUR, gridRow.getRowPrimaryKey()), SCfdConsts.CFDI_PAYROLL_VER_CUR);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void actionSendMail() {
        if (jbSend.isEnabled()) {
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
                        SCfdUtils.sendCfd((SClientInterface) miClient, SCfdConsts.CFD_TYPE_PAYROLL, SCfdUtils.getPayrollReceiptLastCfd((SClientInterface) miClient, SCfdConsts.CFDI_PAYROLL_VER_CUR, gridRow.getRowPrimaryKey()), SCfdConsts.CFDI_PAYROLL_VER_CUR, false, false);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void actionVerifyCfdi() {
        boolean needUpdate = false;

        if (jbVerifyCfdi.isEnabled()) {
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
                        needUpdate = SCfdUtils.verifyCfdi((SClientInterface) miClient, SCfdUtils.getPayrollReceiptLastCfd((SClientInterface) miClient, SCfdConsts.CFDI_PAYROLL_VER_CUR, gridRow.getRowPrimaryKey()), SCfdConsts.CFDI_PAYROLL_VER_CUR);

                        if (needUpdate) {
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                    catch (Exception e) {
                        miClient.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }
    
    private void actionCfdiDiactivateFlags() {
        if (jbVerifyCfdi.isEnabled()) {
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
                        SCfdUtils.resetCfdiDiactivateFlags((SClientInterface) miClient, SCfdUtils.getPayrollReceiptLastCfd((SClientInterface) miClient, SCfdConsts.CFDI_PAYROLL_VER_CUR, gridRow.getRowPrimaryKey()));
                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }
    
    private void actionSendPayrollReceipt() {
        if (jbSendPayrollReceipt.isEnabled()) {
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
                        SHrsUtils.SendPayrollReceipt(miClient, SDataConstantsPrint.PRINT_MODE_PDF, gridRow.getRowPrimaryKey());
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
        moSuscriptionsSet.add(mnGridSubtype);
        moSuscriptionsSet.add(SModConsts.HRS_PAY);
        moSuscriptionsSet.add(SModConsts.HRS_PAY_RCP_EAR);
        moSuscriptionsSet.add(SModConsts.HRS_PAY_RCP_DED);
        moSuscriptionsSet.add(SModConsts.HRS_SIE_PAY);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPrintReceipt) {
                actionPrintReceipt();
            }
            else if (button == jbSignXml) {
                actionSignPayroll();
            }
            else if (button == jbAnnul) {
                actionAnnulPayroll();
            }
            else if (button == jbGetXml) {
                actionGetXml();
            }
            else if (button == jbGetAcknowledgmentCancellation) {
                actionGetAcknowledgmentCancellation();
            }
            else if (button == jbPrint) {
                actionPrint();
            }
            else if (button == jbPrintAcknowledgmentCancellation) {
                actionPrintAcknowledgmentCancellation();
            }
            else if (button == jbSend) {
                actionSendMail();
            }
            else if (button == jbVerifyCfdi) {
                actionVerifyCfdi();
            }
            else if (button == jbDiactivatePac) {
                actionCfdiDiactivateFlags();
            }
            else if (button == jbSendPayrollReceipt) {
                actionSendPayrollReceipt();
            }
        }
    }
}

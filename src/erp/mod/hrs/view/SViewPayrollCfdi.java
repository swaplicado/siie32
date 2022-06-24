/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.SClientUtils;
import erp.cfd.SCfdConsts;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mhrs.data.SDataFormerPayroll;
import erp.mhrs.data.SDataPayrollReceiptIssue;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SHrsFormerPayroll;
import erp.mod.hrs.db.SHrsPayrollAnnul;
import erp.mod.hrs.db.SHrsUtils;
import erp.mod.hrs.form.SDialogFormerPayrollDate;
import erp.mod.hrs.form.SDialogPrintOrderPayroll;
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
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRow;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;

/**
 *
 * @author Juan Barajas, Claudio Peña, Sergio Flores, Isabel Servín, Sergio Flores
 */
public class SViewPayrollCfdi extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private JButton jbReemitPayroll;
    private JButton jbSignCfdi;
    private JButton jbAnnulCfdi;
    private JButton jbGetCfdiXml;
    private JButton jbGetCfdiCancelAck;
    private JButton jbPrintCfdi;
    private JButton jbPrintCfdiCancelAck;
    private JButton jbSendCfdi;
    private JButton jbVerifyCfdi;
    private JButton jbRestoreCfdiStamped;
    private JButton jbRestoreCfdiCancelAck;
    private JButton jbDeactivateControlFlags;

    private SDialogAnnulCfdi moDialogAnnulCfdi;
    private SDialogFormerPayrollDate moDialogFormerPayrollDate;
  
    public SViewPayrollCfdi(SGuiClient client, int subType, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_SIE_PAY, subType, title, params);
        setRowButtonsEnabled(false, false, false, false, isViewSummary());
        jtbFilterDeleted.setEnabled(false);
        initComponetsCustom();
    }
    
    /**
     * Check if view is for summary, that is at payroll level.
     * @return 
     */
    private boolean isViewSummary() {
        return mnGridSubtype == SModConsts.VIEW_SC_SUM;
    }

    /**
     * Check if view is for detail, that is at payroll receipt level.
     * @return 
     */
    private boolean isViewDetail() {
        return mnGridSubtype == SModConsts.VIEW_SC_DET;
    }
    
    private boolean isPayrollCfdVersionOld() {
        return mnGridMode == SCfdConsts.CFDI_PAYROLL_VER_OLD;
    }
    
    private int getPayrollCfdVersion() {
        return isPayrollCfdVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR;
    }

    private void initComponetsCustom() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));

        jbReemitPayroll = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_IMPORT), "Regenerar CFDI de nómina", this);
        jbSignCfdi = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML_SIGN), "Timbrar " + (isViewDetail() ? "recibo de " : "") + "nómina", this);
        jbAnnulCfdi = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_ANNUL), "Anular " + (isViewDetail() ? "recibo de " : "") + "nómina", this);
        jbGetCfdiXml = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML), "Obtener XML del CFDI de nómina", this);
        jbGetCfdiCancelAck = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML_CANCEL), "Obtener XML del acuse de cancelación del CFDI de nómina", this);
        jbPrintCfdi = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT), "Imprimir " + (isViewDetail() ? "recibo de " : "") + "nómina", this);
        jbPrintCfdiCancelAck = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT_ACK_CAN), "Imprimir " + (isViewDetail() ? "acuse " : "acuses ") + "de cancelación " + (isViewDetail() ? "del CFDI " : "de los CFDI ") + "de nómina", this);
        jbSendCfdi = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")), "Enviar " + (isViewDetail() ? "recibo de " : "") + "nómina vía mail", this);
        jbVerifyCfdi = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif")), "Verificar timbrado o cancelación " + (isViewDetail() ? "del CFDI " : "de los CFDI ") + "de nómina", this);
        jbRestoreCfdiStamped = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_insert.gif")), "Insertar XML timbrado del CFDI de nómina", this);
        jbRestoreCfdiCancelAck = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_insert.gif")), "Insertar PDF del acuse de cancelación del CFDI de nómina", this);
        jbDeactivateControlFlags = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif")), "Limpiar inconsistencias de timbrado o cancelación del CFDI de nómina", this);

        moDialogAnnulCfdi = new SDialogAnnulCfdi((SClientInterface) miClient);
        moDialogFormerPayrollDate = new SDialogFormerPayrollDate(miClient, SModConsts.HRSX_DATE, "Fecha de pago");

        switch (mnGridSubtype) {
            case SModConsts.VIEW_SC_SUM:
                jbReemitPayroll.setEnabled(isPayrollCfdVersionOld());
                jbSignCfdi.setEnabled(true);
                jbAnnulCfdi.setEnabled(true);
                jbGetCfdiXml.setEnabled(false);
                jbGetCfdiCancelAck.setEnabled(false);
                jbPrintCfdi.setEnabled(true);
                jbPrintCfdiCancelAck.setEnabled(true);
                jbSendCfdi.setEnabled(true);
                jbVerifyCfdi.setEnabled(true);
                jbRestoreCfdiStamped.setEnabled(false);
                jbRestoreCfdiCancelAck.setEnabled(false);
                jbDeactivateControlFlags.setEnabled(false);
                break;
                
            case SModConsts.VIEW_SC_DET:
                jbReemitPayroll.setEnabled(false);
                jbSignCfdi.setEnabled(true);
                jbAnnulCfdi.setEnabled(true);
                jbGetCfdiXml.setEnabled(true);
                jbGetCfdiCancelAck.setEnabled(true);
                jbPrintCfdi.setEnabled(true);
                jbPrintCfdiCancelAck.setEnabled(true);
                jbSendCfdi.setEnabled(true);
                jbVerifyCfdi.setEnabled(true);
                jbRestoreCfdiStamped.setEnabled(true);
                jbRestoreCfdiCancelAck.setEnabled(true);
                jbDeactivateControlFlags.setEnabled(true);
                break;
                
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbReemitPayroll);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSignCfdi);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAnnulCfdi);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGetCfdiXml);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGetCfdiCancelAck);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrintCfdi);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrintCfdiCancelAck);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSendCfdi);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbVerifyCfdi);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbRestoreCfdiStamped);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbRestoreCfdiCancelAck);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbDeactivateControlFlags);
    }

    @SuppressWarnings("deprecation")
    private void actionReemitPayroll() {
        SDataFormerPayroll formerPayroll = null;
        SHrsFormerPayroll hrsFormerPayroll = null;
        
        if (jbReemitPayroll.isEnabled()) {
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
                        moDialogFormerPayrollDate.setFormReset();
                        moDialogFormerPayrollDate.setVisible(true);

                        if (moDialogFormerPayrollDate.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                            miClient.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
                            
                            hrsFormerPayroll = erp.mod.hrs.db.SHrsFormerUtils.readHrsFormerPayroll((SClientInterface) miClient, miClient.getSession().getStatement(),
                                    ((int []) gridRow.getRowPrimaryKey())[0], miClient.getSession().getConfigCompany().getCompanyId(), moDialogFormerPayrollDate.getDateEmission(), moDialogFormerPayrollDate.getDatePayment());
                            SCfdUtils.computeCfdiPayroll((SClientInterface) miClient, hrsFormerPayroll, moDialogFormerPayrollDate.isRegenerateOnlyNonStampedCfdi());

                            // Update date of payment:

                            formerPayroll = new SDataFormerPayroll();
                            formerPayroll.read(new int[] { ((int []) gridRow.getRowPrimaryKey())[0] }, miClient.getSession().getStatement());

                            if (formerPayroll != null) {
                                formerPayroll.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
                                formerPayroll.setDatePayment(moDialogFormerPayrollDate.getDatePayment());

                                if (formerPayroll.saveField((miClient.getSession().getDatabase().getConnection()), new int[] { ((int []) gridRow.getRowPrimaryKey())[0] }) != SLibConstants.DB_ACTION_SAVE_OK) {
                                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE + "\n- No se pudo actualizar la fecha de pago.");
                                }
                            }
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                    finally {
                        miClient.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            }
        }
    }
    
    private void actionSignCfdi() {
        boolean sign = true;
        boolean needUpdate = false;
        SDataCfd cfd = null;
        SDataPayrollReceiptIssue receiptIssue = null;
        
        if (jbSignCfdi.isEnabled()) {
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
                        if (isViewSummary()) {
                            // Stamp all payroll receipts:
                            
                            needUpdate = SCfdUtils.signPayrollCfdis((SClientInterface) miClient, SCfdUtils.getPayrollCfds((SClientInterface) miClient, getPayrollCfdVersion(), gridRow.getRowPrimaryKey()), getPayrollCfdVersion());
                        }
                        else {
                            // Stamp current payroll receipt:
                            
                            cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                            if (!isPayrollCfdVersionOld()) {
                                receiptIssue = new SDataPayrollReceiptIssue();
                                
                                if (receiptIssue.read(new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n(), cfd.getFkPayrollReceiptIssueId_n() }, miClient.getSession().getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n El recibo no ha sido emitido.");
                                }

                                if (receiptIssue.isDeleted()) {
                                    sign = false;
                                    miClient.showMsgBoxWarning("El recibo '" + receiptIssue.getIssueNumber() + "' está eliminado.");
                                }
                                else if (receiptIssue.getFkReceiptStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                                    sign = false;
                                    miClient.showMsgBoxWarning("El recibo '" + receiptIssue.getIssueNumber() + "' está anulado.");
                                }
                                else if (!SDataUtilities.isPeriodOpen((SClientInterface) miClient, receiptIssue.getDateIssue())) {
                                    sign = false;
                                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
                                }
                            }
                            
                            if (sign) {
                                needUpdate = SCfdUtils.signCfdi((SClientInterface) miClient, cfd, getPayrollCfdVersion());
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

    private void actionAnnulCfdi() {
        if (jbAnnulCfdi.isEnabled()) {
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
                        SDataCfd cfd = null;
                        ArrayList<SDataCfd> cfds = null;
                        ArrayList<SDataPayrollReceiptIssue> receiptIssues = new ArrayList<>();
                        
                        if (isViewSummary()) {
                            cfds = SCfdUtils.getPayrollCfds((SClientInterface) miClient, getPayrollCfdVersion(), gridRow.getRowPrimaryKey());
                            
                            if (!isPayrollCfdVersionOld()) {
                                receiptIssues = SHrsUtils.getPayrollReceiptIssues(miClient.getSession(), gridRow.getRowPrimaryKey());
                            }
                        }
                        else {
                            cfds = new ArrayList<>();
                            cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                            
                            if (cfd != null) {
                                cfds.add(cfd);
                            }
            
                            if (!isPayrollCfdVersionOld()) {
                                SDataPayrollReceiptIssue receiptIssue = new SDataPayrollReceiptIssue();
                                
                                if (receiptIssue.read(new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n(), cfd.getFkPayrollReceiptIssueId_n() }, miClient.getSession().getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                                }
                                receiptIssues.add(receiptIssue);
                            }
                        }
                        
                        boolean annulled = false;
                        
                        moDialogAnnulCfdi.formReset();
                        moDialogAnnulCfdi.formRefreshCatalogues();
                        moDialogAnnulCfdi.setValue(SGuiConsts.PARAM_DATE, (cfds == null || cfds.isEmpty() ? miClient.getSession().getCurrentDate() : cfds.get(0).getTimestamp()));
                        moDialogAnnulCfdi.setValue(SModConsts.TRNS_TP_CFD, (cfds == null || cfds.isEmpty() ? cfd == null ? SLibConstants.UNDEFINED : cfd.getFkCfdTypeId() : cfds.get(0).getFkCfdTypeId()));
                        moDialogAnnulCfdi.setVisible(true);

                        if (moDialogAnnulCfdi.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                            SHrsPayrollAnnul payrollAnnul = new SHrsPayrollAnnul(
                                    (SClientInterface) miClient, 
                                    cfds, 
                                    receiptIssues, 
                                    getPayrollCfdVersion(), 
                                    isViewSummary(), 
                                    moDialogAnnulCfdi.getAnnulDate(), 
                                    moDialogAnnulCfdi.getAnnulSat(), 
                                    moDialogAnnulCfdi.getDpsAnnulType(), 
                                    moDialogAnnulCfdi.getAnnulReason(), 
                                    moDialogAnnulCfdi.getAnnulRelatedUuid(),
                                    moDialogAnnulCfdi.isRetryCancelSelected()
                            );
                            
                            annulled = payrollAnnul.annulPayroll();
                        }

                        if (annulled) {
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

    private void actionGetCfdiXml() {
        if (jbGetCfdiXml.isEnabled()) {
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
                        if (isViewDetail()) {
                            SCfdUtils.downloadXmlCfd((SClientInterface) miClient, (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT));
                        }
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void actionGetCfdiCancelAck() {
        if (jbGetCfdiCancelAck.isEnabled()) {
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
                        if (isViewDetail()) {
                            SCfdUtils.getAcknowledgmentCancellationCfd((SClientInterface) miClient, (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT));
                        }
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void actionPrintCfdi() {
        if (jbPrintCfdi.isEnabled()) {
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
                        if (isViewSummary()) {
                            SDialogPrintOrderPayroll dialogPrintOrderPayroll = new SDialogPrintOrderPayroll(miClient, gridRow.getRowPrimaryKey(), "Ordenamiento de impresión");
                            dialogPrintOrderPayroll.setVisible(true);

                            if (dialogPrintOrderPayroll.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                int orderBy = (int) dialogPrintOrderPayroll.getValue(SGuiConsts.PARAM_KEY);
                                String typeDepPayroll = (String) dialogPrintOrderPayroll.getString(SLibConstants.TXT_OK);
                                int numberCopies = (int) dialogPrintOrderPayroll.getValue(SLibConsts.UNDEFINED);

                                ArrayList<SDataCfd> availableCfds = SCfdUtils.getPayrollCfds((SClientInterface) miClient, getPayrollCfdVersion(), gridRow.getRowPrimaryKey(), typeDepPayroll, orderBy);
                                ArrayList<SDataCfd> printableCfds = new ArrayList<>();
 
                                for(SDataCfd cfd : availableCfds) {
                                    if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                                        printableCfds.add(cfd);
                                    }
                                }
                                
                                SCfdUtils.printPayrollCfds((SClientInterface) miClient, printableCfds, getPayrollCfdVersion(), numberCopies);
                            }
                        }
                        else {
                            SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                            SCfdUtils.printCfd((SClientInterface) miClient, cfd, getPayrollCfdVersion(), SDataConstantsPrint.PRINT_MODE_VIEWER, 1, false);
                        }
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void actionPrintCfdiCancelAck() {

        if (jbPrintCfdiCancelAck.isEnabled()) {
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
                        if (isViewSummary()) {
                            ArrayList<SDataCfd> availableCfds = SCfdUtils.getPayrollCfds((SClientInterface) miClient, getPayrollCfdVersion(), gridRow.getRowPrimaryKey());
                            ArrayList<SDataCfd> printableCfds = new ArrayList<>();

                            for(SDataCfd cfd : availableCfds) {
                                if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                                    printableCfds.add(cfd);
                                }
                            }

                            SCfdUtils.printCancelAckForCfds((SClientInterface) miClient, printableCfds, getPayrollCfdVersion());
                        }
                        else {
                            SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                            SCfdUtils.printCfdCancelAck((SClientInterface) miClient, cfd, getPayrollCfdVersion(), SDataConstantsPrint.PRINT_MODE_VIEWER);
                        }
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
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else if (miClient.showMsgBoxConfirm("¿Está seguro que desea enviar " + (isViewDetail() ? "el recibo de " : "la ") + "nómina vía mail?") == JOptionPane.YES_OPTION) {
                    try {
                        if (isViewSummary()) {
                            SCfdUtils.sendCfds((SClientInterface) miClient, SCfdUtils.getPayrollCfds((SClientInterface) miClient, getPayrollCfdVersion(), gridRow.getRowPrimaryKey()), getPayrollCfdVersion());
                        }
                        else {
                            SCfdUtils.sendCfd((SClientInterface) miClient, SDataConstantsSys.TRNS_TP_CFD_PAYROLL, (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT), getPayrollCfdVersion(), true, false, false);
                         }
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
                        SGuiUtils.setCursorWait(miClient);
                        
                        if (isViewSummary()) {
                            needUpdate = SCfdUtils.verifyCfdi((SClientInterface) miClient, SCfdUtils.getPayrollCfds((SClientInterface) miClient, getPayrollCfdVersion(), gridRow.getRowPrimaryKey()), getPayrollCfdVersion());
                        }
                        else {
                            needUpdate = SCfdUtils.validateCfdi((SClientInterface) miClient, (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT), getPayrollCfdVersion(), true);
                        }
                         
                        if (needUpdate) {
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                    catch (Exception e) {
                        miClient.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        SLibUtils.showException(this, e);
                    }
                    finally {
                        SGuiUtils.setCursorDefault(miClient);
                    }
                }
            }
        }
    }

    private void actionRestoreCfdStamped() {
        boolean restore = true;
        boolean needUpdate = true;
        SDataCfd cfd = null;
        SDataPayrollReceiptIssue receiptIssue = null;

        if (jbRestoreCfdiStamped.isEnabled()) {
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
                        cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                        if (!isPayrollCfdVersionOld()) {
                            receiptIssue = new SDataPayrollReceiptIssue();
                            
                            if (receiptIssue.read(new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n(), cfd.getFkPayrollReceiptIssueId_n() }, miClient.getSession().getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                                restore = false;
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                            }

                            if (receiptIssue.isDeleted()) {
                                restore = false;
                                miClient.showMsgBoxWarning("El documento '" + receiptIssue.getIssueNumber() + "' está eliminado.");
                            }
                        }
                        
                        if (restore) {
                            needUpdate = SCfdUtils.restoreCfdStamped((SClientInterface) miClient, (receiptIssue == null ? cfd : receiptIssue.getDbmsDataCfd()), 0, true);

                            if (needUpdate) {
                                miClient.getSession().notifySuscriptors(mnGridType);
                            }
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

    private void actionRestoreCfdCancelAck() {
        boolean restore = true;
        boolean needUpdate = true;
        SDataCfd cfd = null;
        SDataPayrollReceiptIssue receiptIssue = null;

        if (jbRestoreCfdiCancelAck.isEnabled()) {
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
                        cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                        
                        if (!isPayrollCfdVersionOld()) {
                            receiptIssue = new SDataPayrollReceiptIssue();
                            
                            if (receiptIssue.read(new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n(), cfd.getFkPayrollReceiptIssueId_n() }, miClient.getSession().getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                                restore = false;
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                            }

                            if (receiptIssue.isDeleted()) {
                                restore = false;
                                miClient.showMsgBoxWarning("El documento '" + receiptIssue.getIssueNumber() + "' está eliminado.");
                            }
                        }
                        
                        if (restore) {
                            needUpdate = SCfdUtils.restoreCfdCancelAck((SClientInterface) miClient, (receiptIssue == null ? cfd : receiptIssue.getDbmsDataCfd()), 0, true);

                            if (needUpdate) {
                                miClient.getSession().notifySuscriptors(mnGridType);
                            }
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
    
    private void actionDeactivateControlFlags() {
        if (jbDeactivateControlFlags.isEnabled()) {
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
                        SCfdUtils.resetCfdiDeactivateFlags((SClientInterface) miClient, (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT));
                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;
        int[] date = null;
        int sqlLenght = 0;

        moPaneSettings = new SGridPaneSettings(1);

        jbRowDisable.setEnabled(false);

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();

        if (filter != null){
            switch (((SGuiDate) filter).getGuiType()) {
                case SGuiConsts.GUI_DATE_DATE:
                    sql += (sql.length() == 0 ? "" : "AND ") + "dt_end = '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
                    break;
                case SGuiConsts.GUI_DATE_MONTH:
                    date = SLibTimeUtils.digestMonth((SGuiDate) filter);
                    if (isPayrollCfdVersionOld()) {
                        sql += (sql.length() == 0 ? "" : "AND ") + "pay_year = " + date[0] + " AND pay_per = " + date[1] + " ";
                    }
                    else {
                        sql += (sql.length() == 0 ? "" : "AND ") + "per_year = " + date[0] + " AND per = " + date[1] + " ";
                    }
                    break;
                case SGuiConsts.GUI_DATE_YEAR:
                    date = SLibTimeUtils.digestYear((SGuiDate) filter);
                    if (isPayrollCfdVersionOld()) {
                        sql += (sql.length() == 0 ? "" : "AND ") + "pay_year = " + date[0] + " ";
                    }
                    else {
                        sql += (sql.length() == 0 ? "" : "AND ") + "per_year = " + date[0] + " ";
                    }
                    break;
                default:
            }
        }
        
        msSql = "";
        sqlLenght = isViewDetail() ? 2 : 1;
        
        for (int i = 0; i < sqlLenght; i++) {
            msSql += "SELECT "
                    + (isViewDetail() ? "COALESCE(c.id_cfd, 0)" : "id_pay") + " AS " + SDbConsts.FIELD_ID + "1, "
                    + "pay_num AS " + SDbConsts.FIELD_CODE + ", "
                    + "pay_num AS " + SDbConsts.FIELD_NAME + ", "
                    + "pay_num, "
                    + "pay_year, pay_per, "
                    + "pay_type, pay_sht_type, pay_sht_cus_type, "
                    + "dt_beg, dt_end, pay_notes, "
                    + "f_debit, f_credit, f_balance, "
                    + "count_rcps, ";
            
            if (isViewDetail()) {
                msSql += "b.bp AS f_bp, b.id_bp AS f_id_bp, e.num AS emp_num, id_pay, "
                        + "f_dt_iss, f_dt_pay, f_tp_pay_sys, c.UUID AS _UUID, ";
            }
            
            if (isViewDetail()) {
                msSql += "CONCAT(rcp_num_ser, IF(LENGTH(rcp_num_ser) = 0, '', '-'), erp.lib_fix_int(rcp_num, " + SDataConstantsSys.NUM_LEN_DPS + ")) AS rcp_num, "
                        + "IF(" + (isPayrollCfdVersionOld() ? "c.fid_st_xml = " : "fk_st_rcp = ") + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", " + SGridConsts.ICON_ANNUL + ", " + SGridConsts.ICON_NULL + ") AS _ico, "
                        + "IF(c.fid_st_xml IS NULL, " + SGridConsts.ICON_NULL + ", " /* not have CFDI associated */
                        + "IF(c.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " OR LENGTH(c.uuid) = 0, " + SGridConsts.ICON_XML_PEND + ", " /* CFDI pending sign */
                        + "IF(LENGTH(xc.ack_can_xml) = 0 AND xc.ack_can_pdf_n IS NULL, " + SGridConsts.ICON_XML_ISSU + ", " /* CFDI signed, canceled only SIIE */
                        + "IF(LENGTH(xc.ack_can_xml) != 0, " + SGridConsts.ICON_XML_ANNUL_XML + ", " /* CFDI canceled with cancellation acknowledgment in XML format */
                        + "IF(xc.ack_can_pdf_n IS NOT NULL, " + SGridConsts.ICON_XML_ANNUL_PDF + ", " /* CFDI canceled with cancellation acknowledgment in PDF format */
                        + SGridConsts.ICON_XML_ISSU + " " /* CFDI signed, canceled only SIIE */
                        + "))))) AS _ico_xml, "
                        + "!c.b_con AS _inconsistent, c.b_prc_ws, c.b_prc_sto_xml, c.b_prc_sto_pdf ";
            }
            else {
                msSql += "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c1 "
                        + (isPayrollCfdVersionOld() ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pei ON "
                        + "c1.fid_pay_rcp_pay_n = pei.id_pay AND c1.fid_pay_rcp_emp_n = pei.id_emp AND c1.fid_pay_rcp_iss_n = pei.id_iss AND pei.b_del = 0 AND pei.fk_st_rcp = "  + SModSysConsts.TRNS_ST_DPS_EMITED + " ")
                        + "WHERE b_con = 1 AND " + (isPayrollCfdVersionOld() ? "fid_pay_pay_n = c.fid_pay_pay_n" : "fid_pay_rcp_pay_n = c.fid_pay_rcp_pay_n") + " AND fid_st_xml = " + SModSysConsts.TRNS_ST_DPS_EMITED + ") AS count_sign, "
                        + "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c1 "
                        + (isPayrollCfdVersionOld() ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pei ON "
                        + "c1.fid_pay_rcp_pay_n = pei.id_pay AND c1.fid_pay_rcp_emp_n = pei.id_emp AND c1.fid_pay_rcp_iss_n = pei.id_iss AND pei.b_del = 0 AND pei.fk_st_rcp <> "  + SModSysConsts.TRNS_ST_DPS_ANNULED + " ")
                        + "WHERE b_con = 1 AND " + (isPayrollCfdVersionOld() ? "fid_pay_pay_n = c.fid_pay_pay_n" : "fid_pay_rcp_pay_n = c.fid_pay_rcp_pay_n") + " AND fid_st_xml = " + SModSysConsts.TRNS_ST_DPS_NEW + ") AS count_to_sign, "
                        + "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c1 "
                        + (isPayrollCfdVersionOld() ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pei ON "
                        + "c1.fid_pay_rcp_pay_n = pei.id_pay AND c1.fid_pay_rcp_emp_n = pei.id_emp AND c1.fid_pay_rcp_iss_n = pei.id_iss AND pei.b_del = 0 AND pei.fk_st_rcp = "  + SModSysConsts.TRNS_ST_DPS_EMITED + " ")
                        + "WHERE b_con = 0 AND " + (isPayrollCfdVersionOld() ? "fid_pay_pay_n = c.fid_pay_pay_n" : "fid_pay_rcp_pay_n = c.fid_pay_rcp_pay_n") + " AND fid_st_xml = " + SModSysConsts.TRNS_ST_DPS_EMITED + ") AS count_inconsistent, "
                        + "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c1 "
                        + (isPayrollCfdVersionOld() ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pei ON "
                        + "c1.fid_pay_rcp_pay_n = pei.id_pay AND c1.fid_pay_rcp_emp_n = pei.id_emp AND c1.fid_pay_rcp_iss_n = pei.id_iss AND pei.b_del = 0 ")
                        + "WHERE b_prc_ws = 1 AND " + (isPayrollCfdVersionOld() ? "fid_pay_pay_n = c.fid_pay_pay_n" : "fid_pay_rcp_pay_n = c.fid_pay_rcp_pay_n") + ") AS count_wrong_ws, "
                        + "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c1 "
                        + (isPayrollCfdVersionOld() ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pei ON "
                        + "c1.fid_pay_rcp_pay_n = pei.id_pay AND c1.fid_pay_rcp_emp_n = pei.id_emp AND c1.fid_pay_rcp_iss_n = pei.id_iss AND pei.b_del = 0 ")
                        + "WHERE b_prc_sto_xml = 1 AND " + (isPayrollCfdVersionOld() ? "fid_pay_pay_n = c.fid_pay_pay_n" : "fid_pay_rcp_pay_n = c.fid_pay_rcp_pay_n") + ") AS count_wrong_sto_xml, "
                        + "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c1 "
                        + (isPayrollCfdVersionOld() ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pei ON "
                        + "c1.fid_pay_rcp_pay_n = pei.id_pay AND c1.fid_pay_rcp_emp_n = pei.id_emp AND c1.fid_pay_rcp_iss_n = pei.id_iss AND pei.b_del = 0 ")
                        + "WHERE b_prc_sto_pdf = 1 AND " + (isPayrollCfdVersionOld() ? "fid_pay_pay_n = c.fid_pay_pay_n" : "fid_pay_rcp_pay_n = c.fid_pay_rcp_pay_n") + ") AS count_wrong_sto_pdf ";
            }

            if (isPayrollCfdVersionOld()) {
                msSql += "FROM ( "
                        + "SELECT v.id_pay AS id_pay, v.pay_num, v.pay_year, v.pay_per, "
                        + "v.pay_type AS pay_type, IF(v.b_reg, 'NORMAL', 'ESPECIAL') AS pay_sht_type, '' AS pay_sth_cus_type, v.dt_beg, v.dt_end, '' AS pay_notes, "
                        + "pe.id_emp, pe.num_ser AS rcp_num_ser, pe.num AS rcp_num, "
                        + "'' AS f_dt_iss, "
                        + "'' AS f_dt_pay, "
                        + "'' AS f_tp_pay_sys, "
                        + "SUM(pe.debit) AS f_debit, "
                        + "SUM(pe.credit) AS f_credit, "
                        + "SUM(pe.debit - pe.credit) AS f_balance, "
                        + "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.HRS_SIE_PAY_EMP) + " AS spe WHERE NOT spe.b_del AND spe.id_pay = v.id_pay) AS count_rcps "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_SIE_PAY) + " AS v "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_SIE_PAY_EMP) + " AS pe ON v.id_pay = pe.id_pay AND NOT pe.b_del "
                        + "WHERE " + (sqlLenght == 2 && i == 0 ? "NOT pe.b_del " : "") + (sql.isEmpty() ? "" : (sqlLenght == 2 && i == 0 ? "AND " : "") + sql)
                        + "GROUP BY v.id_pay, v.pay_year, v.pay_per, v.pay_num, v.pay_type, v.dt_beg, v.dt_end " + (isViewDetail() ? ", pe.id_emp " : "")
                        + "ORDER BY v.pay_year, v.pay_per, v.pay_type, v.pay_num, v.id_pay " + (isViewDetail() ? ", pe.id_emp " : "")
                        + ") AS t ";
            }
            else {
                msSql += "FROM ( "
                        + "SELECT v.id_pay AS id_pay, v.num AS pay_num, v.per_year AS pay_year, v.per AS pay_per, "
                        + "tp.name AS pay_type, v.dt_sta AS dt_beg, v.dt_end, v.nts AS pay_notes, tpsht.name AS pay_sht_type, tpshtc.code AS pay_sht_cus_type, "
                        + "pe.id_emp, pei.id_iss, pei.num_ser AS rcp_num_ser, pei.num AS rcp_num, pei.fk_st_rcp, "
                        + "pei.dt_iss AS f_dt_iss, "
                        + "pei.dt_pay AS f_dt_pay, "
                        + "tpsys.tp_pay_sys AS f_tp_pay_sys, "
                        + "SUM(pei.ear_r) AS f_debit, "
                        + "SUM(pei.ded_r) AS f_credit, "
                        + "SUM(pei.pay_r) AS f_balance, "
                        + "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " WHERE b_del = 0 AND id_pay = v.id_pay GROUP BY id_pay ) AS count_rcps "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS v "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS tp ON v.fk_tp_pay = tp.id_tp_pay "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_PAY_SHT_CUS) + " AS tpshtc ON v.fk_tp_pay_sht_cus = tpshtc.id_tp_pay_sht_cus "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY_SHT) + " AS tpsht ON v.fk_tp_pay_sht = tpsht.id_tp_pay_sht "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pe ON v.id_pay = pe.id_pay AND pe.b_del = 0 "
                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pei ON pe.id_pay = pei.id_pay AND pe.id_emp = pei.id_emp AND pei.b_del = 0 " + (isViewDetail() ? "" : " AND pei.fk_st_rcp <> " + SModSysConsts.TRNS_ST_DPS_ANNULED + " ")
                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_PAY_SYS) + " AS tpsys ON pei.fk_tp_pay_sys = tpsys.id_tp_pay_sys "
                        + "WHERE v.b_del = 0 " + (sql.isEmpty() ? "" : "AND " + sql)
                        + "GROUP BY v.id_pay, v.per_year, v.per, v.num, pay_type, v.dt_sta, v.dt_end " + (isViewDetail() ? ", pe.id_emp, pei.id_iss " : "")
                        + "ORDER BY v.per_year, v.per, pay_type, v.num, v.id_pay " + (isViewDetail() ? ", pe.id_emp, pei.id_iss " : "")
                        + ") AS t ";
            }
            
            String complementaryDbName = "";
            
            try {
                complementaryDbName = SClientUtils.getComplementaryDdName((SClientInterface) miClient);
            }
            catch (Exception e) {
                SLibUtils.printException(this, e);
            }

            msSql += (isViewDetail() ? "INNER JOIN " : "LEFT OUTER JOIN ") + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c ON "
                    + (isPayrollCfdVersionOld() ? "t.id_pay = c.fid_pay_pay_n AND t.id_emp = c.fid_pay_emp_n " : "t.id_pay = c.fid_pay_rcp_pay_n AND t.id_emp = c.fid_pay_rcp_emp_n AND t.id_iss = c.fid_pay_rcp_iss_n ")
                    + "AND NOT (c.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " AND c.b_con = 0) " 
                    + "LEFT OUTER JOIN " + complementaryDbName + "." + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS xc ON c.id_cfd = xc.id_cfd ";

            if (isViewDetail()) {
                msSql += "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON " + (isPayrollCfdVersionOld() ? "t.fid_bpr_n = b.id_bp " : "t.id_emp = b.id_bp ") + 
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON b.id_bp = e.id_emp ";
            }

            if (sqlLenght == 2 && i == 0) {
                msSql += " UNION ";
            }
        }
        
        msSql += "GROUP BY id_pay, pay_year, pay_per, pay_type, pay_sht_cus_type, pay_sht_type, t.dt_beg, t.dt_end " +
                (isViewDetail() ? ", t.id_emp, f_id_bp, c.id_cfd " : "") +
                "ORDER BY pay_year, pay_per, pay_type, pay_num, pay_sht_cus_type, pay_sht_type, id_pay " + (isViewDetail() ? ", rcp_num, f_bp, f_id_bp " : "");
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, "pay_year", "Ejercicio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_MONTH, "pay_per", "Período"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "pay_type", "Período pago nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "pay_num", "Número nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_beg", "F. inicial nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_end", "F. final nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "pay_sht_cus_type", "Tipo nómina empresa"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "pay_sht_type", "Tipo nómina"));
        if (isViewDetail()) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_ico", "Estatus CFD"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_ico_xml", "CFD"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "rcp_num", "Folio CFD", 75));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "f_bp", "Empleado"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "emp_num", "Clave"));
        }
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_debit", "Percepciones $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_credit", "Deducciones $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_balance", "Alcance neto $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "pay_notes", "Notas nómina", 200));
        if (isViewDetail()) {
            if (!isPayrollCfdVersionOld()) {
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "f_dt_iss", "F emisión"));
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "f_dt_pay", "F pago"));
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_tp_pay_sys", "Método pago"));
            }
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "_inconsistent", "Iconsistente"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "b_prc_ws", "Incorrecto PAC"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "b_prc_sto_xml", "Incorrecto XML disco"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "b_prc_sto_pdf", "Incorrecto PDF disco"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_UUID", "UUID", 250));
        }
        else {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "count_rcps", "Recibos"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_8B, "count_sign", "CFDI timbrados"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_8B, "count_to_sign", "CFDI x timbrar"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_8B, "count_inconsistent", "CFDI inconsistentes"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_8B, "count_wrong_ws", "CFDI incorrectos PAC"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_8B, "count_wrong_sto_xml", "CFDI incorrectos XML disco"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_8B, "count_wrong_sto_pdf", "CFDI incorrectos PDF disco"));
        }

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.HRS_PAY);
        moSuscriptionsSet.add(SModConsts.HRS_PAY_RCP);
        moSuscriptionsSet.add(SModConsts.HRS_PAY_RCP_ISS);
        moSuscriptionsSet.add(SModConsts.TRN_CFD);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
    }

    @Override
    public void actionRowDelete() {
        if (jbRowDelete.isEnabled()) {
            if (jtTable.getSelectedRowCount() == 0) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROWS);
            }
            else if (miClient.showMsgBoxConfirm(SGridConsts.MSG_CONFIRM_REG_DEL) == JOptionPane.YES_OPTION) {
                boolean updates = false;
                SGridRow[] gridRows = getSelectedGridRows();

                for (SGridRow gridRow : gridRows) {
                    if (((SGridRowView) gridRow).getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else if (((SGridRowView) gridRow).isRowSystem()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                    }
                    else if (!((SGridRowView) gridRow).isDeletable()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_DELETABLE);
                    }
                    else {
                        try {
                            if (SCfdUtils.deletePayroll((SClientInterface) miClient, gridRow.getRowPrimaryKey()[0])) {
                                updates = true;
                            }
                        }
                        catch (Exception e) {
                            SLibUtils.showException(this, e);
                        }
                    }
                }

                if (updates) {
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbReemitPayroll) {
                actionReemitPayroll();
            }
            else if (button == jbSignCfdi) {
                actionSignCfdi();
            }
            else if (button == jbAnnulCfdi) {
                actionAnnulCfdi();
            }
            else if (button == jbGetCfdiXml) {
                actionGetCfdiXml();
            }
            else if (button == jbGetCfdiCancelAck) {
                actionGetCfdiCancelAck();
            }
            else if (button == jbPrintCfdi) {
                actionPrintCfdi();
            }
            else if (button == jbPrintCfdiCancelAck) {
                actionPrintCfdiCancelAck();
            }
            else if (button == jbSendCfdi) {
                actionSendCfdi();
            }
            else if (button == jbVerifyCfdi) {
                actionVerifyCfdi();
            }
            else if (button == jbRestoreCfdiStamped) {
                actionRestoreCfdStamped();
            }
            else if (button == jbRestoreCfdiCancelAck) {
                actionRestoreCfdCancelAck();
            }
            else if (button == jbDeactivateControlFlags) {
                actionDeactivateControlFlags();
            }
        }
    }
}

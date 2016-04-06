/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

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
import erp.mod.hrs.db.SHrsFormerUtils;
import erp.mod.hrs.db.SHrsPayrollAnnul;
import erp.mod.hrs.db.SHrsUtils;
import erp.mod.hrs.form.SDialogFormerPayrollDate;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.form.SDialogAnnulCfdi;
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

/**
 *
 * @author Juan Barajas
 */
public class SViewCfdiPayroll extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private JButton jbReemit;
    private JButton jbSignXml;
    private JButton jbAnnul;
    private JButton jbGetXml;
    private JButton jbGetAcknowledgmentCancellation;
    private JButton jbPrint;
    private JButton jbPrintAcknowledgmentCancellation;
    private JButton jbSend;
    private JButton jbVerifyCfdi;
    private JButton jbRestoreSignXml;
    private JButton jbRestoreAcknowledgmentCancellation;
    private JButton jbDiactivatePac;

    private SDialogAnnulCfdi moDialogAnnulCfdi;
    private SDialogFormerPayrollDate moDialogDate;

    public SViewCfdiPayroll(SGuiClient client, int subType, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_SIE_PAY, subType, title, params);
        setRowButtonsEnabled(false, false, false, false, mnGridSubtype == SModConsts.VIEW_SC_SUM);
        jtbFilterDeleted.setEnabled(false);
        initComponetsCustom();
    }

    private void initComponetsCustom() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));

        jbReemit = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_IMPORT), "Regenerar CFDI", this);
        jbSignXml = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML_SIGN), "Timbrar CFDI", this);
        jbAnnul = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_ANNUL), "Anular " + (mnGridSubtype == SModConsts.VIEW_SC_SUM ? "" : "recibo ") + "nómina", this);
        jbGetXml = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML), "Obtener XML del comprobante", this);
        jbGetAcknowledgmentCancellation = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML_CANCEL), "Obtener XML del acuse de cancelación del CFDI", this);
        jbPrint = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT), "Imprimir " + (mnGridSubtype == SModConsts.VIEW_SC_SUM ? "" : "recibo ") + "nómina", this);
        jbPrintAcknowledgmentCancellation = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT_ACK_CAN), "Imprimir acuse de cancelación", this);
        jbSend = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")), "Enviar " + (mnGridSubtype == SModConsts.VIEW_SC_SUM ? "" : "recibo ") + "nómina", this);
        jbVerifyCfdi = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif")), "Verificar timbrado o cancelación del CFDI", this);
        jbRestoreSignXml = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_insert.gif")), "Insertar XML timbrado del CFDI", this);
        jbRestoreAcknowledgmentCancellation = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_insert.gif")), "Insertar PDF del acuse de cancelación del CFDI", this);
        jbDiactivatePac = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif")), "Limpiar inconsistencias del timbrado o cancelación del CFDI", this);

        moDialogAnnulCfdi = new SDialogAnnulCfdi((SClientInterface) miClient, true);
        moDialogDate = new SDialogFormerPayrollDate(miClient, SModConsts.HRSX_DATE, "Fecha de pago");

        switch (mnGridSubtype) {
            case SModConsts.VIEW_SC_SUM:
                jbReemit.setEnabled(isCfdiPayrollVersionOld());
                jbSignXml.setEnabled(true);
                jbAnnul.setEnabled(true);
                jbGetXml.setEnabled(false);
                jbGetAcknowledgmentCancellation.setEnabled(false);
                jbPrint.setEnabled(true);
                jbPrintAcknowledgmentCancellation.setEnabled(true);
                jbSend.setEnabled(true);
                jbVerifyCfdi.setEnabled(false);
                jbRestoreSignXml.setEnabled(false);
                jbRestoreAcknowledgmentCancellation.setEnabled(false);
                jbDiactivatePac.setEnabled(false);
                break;
            case SModConsts.VIEW_SC_DET:
                jbReemit.setEnabled(false);
                jbSignXml.setEnabled(true);
                jbAnnul.setEnabled(true);
                jbGetXml.setEnabled(true);
                jbGetAcknowledgmentCancellation.setEnabled(true);
                jbPrint.setEnabled(true);
                jbPrintAcknowledgmentCancellation.setEnabled(true);
                jbSend.setEnabled(true);
                jbVerifyCfdi.setEnabled(true);
                jbRestoreSignXml.setEnabled(true);
                jbRestoreAcknowledgmentCancellation.setEnabled(true);
                jbDiactivatePac.setEnabled(true);
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbReemit);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSignXml);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAnnul);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGetXml);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGetAcknowledgmentCancellation);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrint);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrintAcknowledgmentCancellation);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSend);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbVerifyCfdi);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbRestoreSignXml);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbRestoreAcknowledgmentCancellation);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbDiactivatePac);
    }

    private void actionReemitPayroll() {
        SDataFormerPayroll oFormerPayroll = null;
        SHrsFormerPayroll payroll = null;
        
        if (jbReemit.isEnabled()) {
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
                        moDialogDate.setFormReset();
                        moDialogDate.setVisible(true);

                        if (moDialogDate.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                            payroll = SHrsFormerUtils.readFormerPayroll((SClientInterface) miClient, miClient.getSession().getStatement(),
                                    ((int []) gridRow.getRowPrimaryKey())[0], miClient.getSession().getConfigCompany().getCompanyId(), moDialogDate.getDateEmission(), moDialogDate.getDatePayment());
                            SCfdUtils.computeCfdiPayroll((SClientInterface) miClient, payroll, moDialogDate.getGenerateCfdiPendingSigned());

                            // Update date of payment:

                            oFormerPayroll = new SDataFormerPayroll();
                            oFormerPayroll.read(new int[] { ((int []) gridRow.getRowPrimaryKey())[0] }, miClient.getSession().getStatement());

                            if (oFormerPayroll != null) {
                                oFormerPayroll.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
                                oFormerPayroll.setDatePayment(moDialogDate.getDatePayment());

                                if (oFormerPayroll.saveField((miClient.getSession().getDatabase().getConnection()), new int[] { ((int []) gridRow.getRowPrimaryKey())[0] }) != SLibConstants.DB_ACTION_SAVE_OK) {
                                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE + "\n- No se pudo actualizar la fecha de pago.");
                                }
                            }
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
    
    private boolean isCfdiPayrollVersionOld() {
        return mnGridMode == SCfdConsts.CFDI_PAYROLL_VER_OLD;
    }

    private void actionSignPayroll() {
        boolean needUpdate = false;
        SDataCfd cfd = null;
        SDataPayrollReceiptIssue receiptIssue = null;
        
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
                    receiptIssue = new SDataPayrollReceiptIssue();
                    
                    try {
                        if (mnGridSubtype == SModConsts.VIEW_SC_SUM) {
                            needUpdate = SCfdUtils.signCfdi((SClientInterface) miClient, SCfdUtils.getPayrollCfds((SClientInterface) miClient, (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR), gridRow.getRowPrimaryKey()), (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR));
                        }
                        else {
                            cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                            if (receiptIssue.read(new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n(), cfd.getFkPayrollReceiptIssueId_n() }, miClient.getSession().getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n El recibo no ha sido emitido.");
                            }
                            
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
                                needUpdate = SCfdUtils.signCfdi((SClientInterface) miClient, cfd, (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR));
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
        ArrayList<SDataCfd> cfds = null;
        SHrsPayrollAnnul payrollAnnul = null;
        ArrayList<SDataPayrollReceiptIssue> receiptIssues = null;
        SDataPayrollReceiptIssue receiptIssue = null;

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
                        receiptIssues = new ArrayList<SDataPayrollReceiptIssue>();
                        receiptIssue = new SDataPayrollReceiptIssue();
                        
                        if (mnGridSubtype == SModConsts.VIEW_SC_SUM) {
                            cfds = SCfdUtils.getPayrollCfds((SClientInterface) miClient, (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR), gridRow.getRowPrimaryKey());
                            receiptIssues = SHrsUtils.getPayrollReceiptIssues(miClient.getSession(), gridRow.getRowPrimaryKey());
                        }
                        else {
                            cfds = new ArrayList<SDataCfd>();
                            cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                            
                            if (cfd != null) {
                                cfds.add(cfd);
                            }
            
                            if (receiptIssue.read(new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n(), cfd.getFkPayrollReceiptIssueId_n() }, miClient.getSession().getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                            }
                            receiptIssues.add(receiptIssue);
                        }

                        moDialogAnnulCfdi.formReset();
                        moDialogAnnulCfdi.formRefreshCatalogues();
                        moDialogAnnulCfdi.setValue(SGuiConsts.PARAM_DATE, (cfds == null || cfds.isEmpty() ? miClient.getSession().getCurrentDate() : cfds.get(0).getTimestamp()));
                        moDialogAnnulCfdi.setVisible(true);

                        if (moDialogAnnulCfdi.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                            payrollAnnul = new SHrsPayrollAnnul((SClientInterface) miClient, cfds, receiptIssues, (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR), mnGridSubtype == SModConsts.VIEW_SC_SUM, moDialogAnnulCfdi.getDate(), moDialogAnnulCfdi.getAnnulSat());
                            needUpdate = payrollAnnul.annulPayroll();
                        }

                        if (needUpdate) {
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                        
                        /*
                        if (cfds == null) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
                        }
                        else {
                            moDialogAnnulCfdi.formReset();
                            moDialogAnnulCfdi.formRefreshCatalogues();
                            moDialogAnnulCfdi.setValue(SGuiConsts.PARAM_DATE, cfds.get(0).getTimestamp());
                            moDialogAnnulCfdi.setVisible(true);

                            if (moDialogAnnulCfdi.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                if (mnGridSubtype == SModConsts.VIEW_SC_SUM) {
                                    needUpdate = SCfdUtils.cancelCfdi((SClientInterface) miClient, SCfdUtils.getPayrollCfds((SClientInterface) miClient, (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR), gridRow.getRowPrimaryKey()), (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR), moDialogAnnulCfdi.getDate(), moDialogAnnulCfdi.getAnnulSat());
                                }
                                else {
                                    needUpdate = SCfdUtils.cancelCfdi((SClientInterface) miClient, (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT), (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR), moDialogAnnulCfdi.getDate(), moDialogAnnulCfdi.getAnnulSat());
                                }
                            }

                            if (needUpdate) {
                                miClient.getSession().notifySuscriptors(mnGridType);
                            }
                        }
                        */
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
                        if (mnGridSubtype == SModConsts.VIEW_SC_DET) {
                            SCfdUtils.getXmlCfd((SClientInterface) miClient, (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT));
                        }
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
                        if (mnGridSubtype == SModConsts.VIEW_SC_DET) {
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

    private void actionPrint() {
        ArrayList<SDataCfd> cfdAux = null;
        ArrayList<SDataCfd> cfds = null;

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
                        if (mnGridSubtype == SModConsts.VIEW_SC_SUM) {
                            cfdAux = new ArrayList<SDataCfd>();
                            cfds = new ArrayList<SDataCfd>();

                            cfdAux = SCfdUtils.getPayrollCfds((SClientInterface) miClient, (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR), gridRow.getRowPrimaryKey());

                            for(SDataCfd cfd : cfdAux) {
                                if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                                    cfds.add(cfd);
                                }
                            }

                            SCfdUtils.printCfd((SClientInterface) miClient, cfds, (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR));
                        }
                        else {
                            SCfdUtils.printCfd((SClientInterface) miClient, SCfdConsts.CFD_TYPE_PAYROLL, (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT), (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR));
                         }
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void actionPrintAcknowledgmentCancellation() {
        ArrayList<SDataCfd> cfdAux = null;
        ArrayList<SDataCfd> cfds = null;

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
                        if (mnGridSubtype == SModConsts.VIEW_SC_SUM) {
                            cfdAux = new ArrayList<SDataCfd>();
                            cfds = new ArrayList<SDataCfd>();

                            cfdAux = SCfdUtils.getPayrollCfds((SClientInterface) miClient, (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR), gridRow.getRowPrimaryKey());

                            for(SDataCfd cfd : cfdAux) {
                                if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                                    cfds.add(cfd);
                                }
                            }

                            SCfdUtils.printAcknowledgmentCancellationCfd((SClientInterface) miClient, cfds, (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR));
                        }
                        else {
                            SCfdUtils.printAcknowledgmentCancellationCfd((SClientInterface) miClient, (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT), (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR));
                        }
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
                        if (mnGridSubtype == SModConsts.VIEW_SC_SUM) {
                            SCfdUtils.sendCfd((SClientInterface) miClient, SCfdUtils.getPayrollCfds((SClientInterface) miClient, (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR), gridRow.getRowPrimaryKey()), (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR));
                        }
                        else {
                            SCfdUtils.sendCfd((SClientInterface) miClient, SCfdConsts.CFD_TYPE_PAYROLL, (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT), (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR));
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
                        needUpdate = SCfdUtils.verifyCfdi((SClientInterface) miClient, (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT), (isCfdiPayrollVersionOld() ? SCfdConsts.CFDI_PAYROLL_VER_OLD : SCfdConsts.CFDI_PAYROLL_VER_CUR));

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

    private void actionRestoreSignXml() {
        boolean needUpdate = true;
        SDataCfd cfd = null;
        SDataPayrollReceiptIssue receiptIssue = null;

        if (jbRestoreSignXml.isEnabled()) {
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
                        receiptIssue = new SDataPayrollReceiptIssue();
                        cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                         if (receiptIssue.read(new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n(), cfd.getFkPayrollReceiptIssueId_n() }, miClient.getSession().getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                             throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                         }

                         if (receiptIssue.isDeleted()) {
                             miClient.showMsgBoxWarning("El documento '" + receiptIssue.getIssueNumber() + "' está eliminado.");
                         }
                         else {
                             needUpdate = SCfdUtils.restoreSignXml((SClientInterface) miClient, receiptIssue.getDbmsDataCfd(), true, SLibConstants.UNDEFINED);

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

    private void actionRestoreAcknowledgmentCancellation() {
        boolean needUpdate = true;
        SDataCfd cfd = null;
        SDataPayrollReceiptIssue receiptIssue = null;

        if (jbRestoreAcknowledgmentCancellation.isEnabled()) {
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
                        receiptIssue = new SDataPayrollReceiptIssue();
                        cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                        if (receiptIssue.read(new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n(), cfd.getFkPayrollReceiptIssueId_n() }, miClient.getSession().getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }

                        if (receiptIssue.isDeleted()) {
                            miClient.showMsgBoxWarning("El documento '" + receiptIssue.getIssueNumber() + "' está eliminado.");
                        }
                        else {
                            needUpdate = SCfdUtils.restoreAcknowledgmentCancellation((SClientInterface) miClient, receiptIssue.getDbmsDataCfd(), true, SLibConstants.UNDEFINED);

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
    
    private void actionCfdiDiactivateFlags() {
        if (jbDiactivatePac.isEnabled()) {
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
                        SCfdUtils.resetCfdiDiactivateFlags((SClientInterface) miClient, (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, gridRow.getRowPrimaryKey(), SLibConstants.EXEC_MODE_SILENT));
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
                    if (isCfdiPayrollVersionOld()) {
                        sql += (sql.length() == 0 ? "" : "AND ") + "pay_year = " + date[0] + " AND pay_per = " + date[1] + " ";
                    }
                    else {
                        sql += (sql.length() == 0 ? "" : "AND ") + "per_year = " + date[0] + " AND per = " + date[1] + " ";
                    }
                    break;
                case SGuiConsts.GUI_DATE_YEAR:
                    date = SLibTimeUtils.digestYear((SGuiDate) filter);
                    if (isCfdiPayrollVersionOld()) {
                        sql += (sql.length() == 0 ? "" : "AND ") + "pay_year = " + date[0] + " ";
                    }
                    else {
                        sql += (sql.length() == 0 ? "" : "AND ") + "per_year = " + date[0] + " ";
                    }
                    break;
                default:
            }
        }
        sqlLenght = mnGridSubtype == SModConsts.VIEW_SC_DET ? 2 : 1;

        msSql = "";
        for (int i = 0; i < sqlLenght; i++) {
            msSql += "SELECT "
                    + (mnGridSubtype == SModConsts.VIEW_SC_DET ? "COALESCE(id_cfd, 0) AS " : "f_id_pay AS ") + SDbConsts.FIELD_ID + "1, "
                    + "pay_num AS " + SDbConsts.FIELD_CODE + ", "
                    + "pay_num AS " + SDbConsts.FIELD_NAME + ", "
                    + "pay_year AS f_pay_year, "
                    + "pay_per AS f_pay_per, "
                    + "pay_type AS f_pay_type, "
                    + "dt_beg, "
                    + "dt_end, "
                    + "f_debit, "
                    + "f_credit, "
                    + "f_balance, "
                    + "f_tot, ";
                    if (mnGridSubtype == SModConsts.VIEW_SC_DET) {
                        msSql += "b.bp AS f_bp, num_ser, f_rcp_num, f_id_pay, b.id_bp AS f_id_bp, "
                                + "f_dt_iss, f_dt_pay, f_tp_pay_sys, ";
                    }
                    if (mnGridSubtype == SModConsts.VIEW_SC_DET) {
                        msSql += "CONCAT(num_ser, IF(LENGTH(num_ser) = 0, '', '-'), erp.lib_fix_int(f_rcp_num, " + SDataConstantsSys.NUM_LEN_DPS + ")) AS f_num, "
                                + "IF(" + (isCfdiPayrollVersionOld() ? "c.fid_st_xml = " : "fk_st_rcp = ") + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", " + SGridConsts.ICON_ANNUL + ", " + SGridConsts.ICON_NULL + ") AS f_ico, "
                                + "IF(c.fid_st_xml IS NULL, " + SGridConsts.ICON_NULL + ", " /* not have CFDI associated */
                                + "IF(c.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " OR LENGTH(c.uuid) = 0, " + SGridConsts.ICON_XML_PEND + ", "  /* CFDI pending sign */
                                + "IF(LENGTH(c.ack_can_xml) = 0 AND c.ack_can_pdf_n IS NULL, " + SGridConsts.ICON_XML_ISSU + ", " /* CFDI signed, canceled only SIIE */
                                + "IF(LENGTH(c.ack_can_xml) != 0, " + SGridConsts.ICON_XML_ANNUL_XML + ", " /* CFDI canceled with cancellation acknowledgment in XML format */
                                + "IF(c.ack_can_pdf_n IS NOT NULL, " + SGridConsts.ICON_XML_ANNUL_PDF + ", " /* CFDI canceled with cancellation acknowledgment in PDF format */
                                + SGridConsts.ICON_XML_ISSU + " " /* CFDI signed, canceled only SIIE */
                                + "))))) AS f_ico_xml, "
                                + "!c.b_con AS f_con, c.b_prc_ws, c.b_prc_sto_xml, c.b_prc_sto_pdf ";
                    }
                    else {
                        msSql += "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c1 "
                                + (isCfdiPayrollVersionOld() ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pei ON "
                                + "c1.fid_pay_rcp_pay_n = pei.id_pay AND c1.fid_pay_rcp_emp_n = pei.id_emp AND c1.fid_pay_rcp_iss_n = pei.id_iss AND pei.b_del = 0 AND pei.fk_st_rcp = "  + SModSysConsts.TRNS_ST_DPS_EMITED + " ")
                                + "WHERE b_con = 1 AND " + (isCfdiPayrollVersionOld() ? "fid_pay_pay_n = c.fid_pay_pay_n" : "fid_pay_rcp_pay_n = c.fid_pay_rcp_pay_n") + " AND fid_st_xml = " + SModSysConsts.TRNS_ST_DPS_EMITED + ") AS f_sign, "
                                + "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c1 "
                                + (isCfdiPayrollVersionOld() ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pei ON "
                                + "c1.fid_pay_rcp_pay_n = pei.id_pay AND c1.fid_pay_rcp_emp_n = pei.id_emp AND c1.fid_pay_rcp_iss_n = pei.id_iss AND pei.b_del = 0 AND pei.fk_st_rcp <> "  + SModSysConsts.TRNS_ST_DPS_ANNULED + " ")
                                + "WHERE b_con = 1 AND " + (isCfdiPayrollVersionOld() ? "fid_pay_pay_n = c.fid_pay_pay_n" : "fid_pay_rcp_pay_n = c.fid_pay_rcp_pay_n") + " AND fid_st_xml = " + SModSysConsts.TRNS_ST_DPS_NEW + ") AS f_new, "
                                + "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c1 "
                                + (isCfdiPayrollVersionOld() ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pei ON "
                                + "c1.fid_pay_rcp_pay_n = pei.id_pay AND c1.fid_pay_rcp_emp_n = pei.id_emp AND c1.fid_pay_rcp_iss_n = pei.id_iss AND pei.b_del = 0 AND pei.fk_st_rcp = "  + SModSysConsts.TRNS_ST_DPS_EMITED + " ")
                                + "WHERE b_con = 0 AND " + (isCfdiPayrollVersionOld() ? "fid_pay_pay_n = c.fid_pay_pay_n" : "fid_pay_rcp_pay_n = c.fid_pay_rcp_pay_n") + " AND fid_st_xml = " + SModSysConsts.TRNS_ST_DPS_EMITED + ") AS f_incon, "
                                + "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c1 "
                                + (isCfdiPayrollVersionOld() ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pei ON "
                                + "c1.fid_pay_rcp_pay_n = pei.id_pay AND c1.fid_pay_rcp_emp_n = pei.id_emp AND c1.fid_pay_rcp_iss_n = pei.id_iss AND pei.b_del = 0 ")
                                + "WHERE b_prc_ws = 1 AND " + (isCfdiPayrollVersionOld() ? "fid_pay_pay_n = c.fid_pay_pay_n" : "fid_pay_rcp_pay_n = c.fid_pay_rcp_pay_n") + ") AS f_prc_ws, "
                                + "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c1 "
                                + (isCfdiPayrollVersionOld() ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pei ON "
                                + "c1.fid_pay_rcp_pay_n = pei.id_pay AND c1.fid_pay_rcp_emp_n = pei.id_emp AND c1.fid_pay_rcp_iss_n = pei.id_iss AND pei.b_del = 0 ")
                                + "WHERE b_prc_sto_xml = 1 AND " + (isCfdiPayrollVersionOld() ? "fid_pay_pay_n = c.fid_pay_pay_n" : "fid_pay_rcp_pay_n = c.fid_pay_rcp_pay_n") + ") AS f_prc_sto_xml, "
                                + "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c1 "
                                + (isCfdiPayrollVersionOld() ? "" : "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pei ON "
                                + "c1.fid_pay_rcp_pay_n = pei.id_pay AND c1.fid_pay_rcp_emp_n = pei.id_emp AND c1.fid_pay_rcp_iss_n = pei.id_iss AND pei.b_del = 0 ")
                                + "WHERE b_prc_sto_pdf = 1 AND " + (isCfdiPayrollVersionOld() ? "fid_pay_pay_n = c.fid_pay_pay_n" : "fid_pay_rcp_pay_n = c.fid_pay_rcp_pay_n") + ") AS f_prc_sto_pdf ";
                    }

                    if (isCfdiPayrollVersionOld()) {
                        msSql += "FROM( "
                                + "SELECT v.id_pay AS f_id_pay, v.pay_num, v.pay_num AS f_name, v.pay_year, v.pay_per, v.pay_type AS pay_type, v.dt_beg, v.dt_end, fid_bpr_n, pe.id_emp, pe.num_ser, pe.num AS f_rcp_num, "
                                + "'' AS f_dt_iss, "
                                + "'' AS f_dt_pay, "
                                + "'' AS f_tp_pay_sys, "
                                + "SUM(pe.debit) AS f_debit, "
                                + "SUM(pe.credit) AS f_credit, "
                                + "SUM(pe.debit - pe.credit) AS f_balance, "
                                + "(SELECT COUNT(*) FROM hrs_sie_pay_emp WHERE b_del = 0 AND id_pay = v.id_pay GROUP BY id_pay ) AS f_tot "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_SIE_PAY) + " AS v "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_SIE_PAY_EMP) + " AS pe ON "
                                + "v.id_pay = pe.id_pay AND pe.b_del = FALSE "
                                + "WHERE " + (sqlLenght == 2 && i == 0 ? "pe.b_del = 0 " : "") + (sql.isEmpty() ? "" : (sqlLenght == 2 && i == 0 ? "AND " : "") + sql)
                                + "GROUP BY v.id_pay, v.pay_year, v.pay_per, v.pay_num, v.pay_type, v.dt_beg, v.dt_end " + (mnGridSubtype == SModConsts.VIEW_SC_DET ? ", pe.id_emp, pe.fid_bpr_n " : "")
                                + "ORDER BY v.pay_year, v.pay_per, v.pay_type, v.pay_num, v.id_pay " + (mnGridSubtype == SModConsts.VIEW_SC_DET ? ", pe.id_emp, pe.fid_bpr_n " : "")
                                + ") AS t ";
                    }
                    else {
                        msSql += "FROM( "
                                + "SELECT v.id_pay AS f_id_pay, v.num AS pay_num, v.num AS f_name, v.per_year AS pay_year, v.per AS pay_per, v.dt_sta AS dt_beg, v.dt_end, pe.id_emp, pei.id_iss, pei.fk_st_rcp, pei.num_ser, pei.num AS f_rcp_num, "
                                + "pei.dt_iss AS f_dt_iss, "
                                + "pei.dt_pay AS f_dt_pay, "
                                + "(SELECT tp_pay_sys FROM " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_PAY_SYS) + " WHERE id_tp_pay_sys = pei.fk_tp_pay_sys) AS f_tp_pay_sys, "
                                + "SUM(pei.ear_r) AS f_debit, "
                                + "SUM(pei.ded_r) AS f_credit, "
                                + "SUM(pei.pay_r) AS f_balance, "
                                + "(SELECT t.name FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS t WHERE v.fk_tp_pay = t.id_tp_pay) AS pay_type, "
                                + "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " WHERE b_del = 0 AND id_pay = v.id_pay GROUP BY id_pay ) AS f_tot "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS v "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pe ON "
                                + "v.id_pay = pe.id_pay AND pe.b_del = 0 "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pei ON "
                                + "pe.id_pay = pei.id_pay AND pe.id_emp = pei.id_emp AND pei.b_del = 0 " + (mnGridSubtype == SModConsts.VIEW_SC_DET ? "" : " AND pei.fk_st_rcp <> " + SModSysConsts.TRNS_ST_DPS_ANNULED + " ")
                                + "WHERE v.b_del = 0 " + (sql.isEmpty() ? "" : "AND " + sql)
                                + "GROUP BY v.id_pay, v.per_year, v.per, v.num, pay_type, v.dt_sta, v.dt_end " + (mnGridSubtype == SModConsts.VIEW_SC_DET ? ", pe.id_emp, pei.id_iss " : "")
                                + "ORDER BY v.per_year, v.per, pay_type ,v.num, v.id_pay " + (mnGridSubtype == SModConsts.VIEW_SC_DET ? ", pe.id_emp, pei.id_iss " : "")
                                + ") AS t ";
                    }

                    msSql += (mnGridSubtype == SModConsts.VIEW_SC_DET ? "INNER JOIN " : "LEFT OUTER JOIN ") + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c ON "
                            + (isCfdiPayrollVersionOld() ? "t.f_id_pay = c.fid_pay_pay_n AND t.id_emp = c.fid_pay_emp_n " : "t.f_id_pay = c.fid_pay_rcp_pay_n AND t.id_emp = c.fid_pay_rcp_emp_n AND t.id_iss = c.fid_pay_rcp_iss_n ")
                            + "AND NOT (c.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " AND c.b_con = 0) ";
                    /*
                            + (mnGridSubtype == SModConsts.VIEW_SC_DET ? "INNER JOIN " : "LEFT OUTER JOIN ") + SModConsts.TablesMap.get(SModConsts.TRNS_ST_DPS) + " AS st ON "
                            + "c.fid_st_xml = st.id_st_dps ";*/
                    if (mnGridSubtype == SModConsts.VIEW_SC_DET) {
                        msSql += "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON "
                                + (isCfdiPayrollVersionOld() ? "t.fid_bpr_n = b.id_bp " : "t.id_emp = b.id_bp ");
                    }

                    if (sqlLenght == 2 && i == 0) {
                        msSql += " UNION ";
                    }
        }
        msSql += "GROUP BY f_id_pay, f_pay_year, f_pay_per, t.f_name, f_pay_type, t.dt_beg, t.dt_end " + (mnGridSubtype == SModConsts.VIEW_SC_DET ? ", t.id_emp, f_id_bp, c.id_cfd " : "")
        + "ORDER BY f_pay_year, f_pay_per, f_pay_type, f_name, f_id_pay " + (mnGridSubtype == SModConsts.VIEW_SC_DET ? ", f_num, f_bp, f_id_bp " : "");
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, "f_pay_year", "Ejercicio", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_MONTH, "f_pay_per", "Período", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_pay_type", "Tipo nómina", 75));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, SDbConsts.FIELD_NAME, "No. nómina", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_beg", "F. inicial"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_end", "F. final"));
        if (mnGridSubtype == SModConsts.VIEW_SC_DET) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "f_ico", "Estatus"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "f_ico_xml", "CFD"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_num", "Folio recibo", 75));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "f_bp", "Empleado"));
        }
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_debit", "Percepciones $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_credit", "Deducciones $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_balance", "Alcance neto $"));
        if (mnGridSubtype == SModConsts.VIEW_SC_DET) {
            if (!isCfdiPayrollVersionOld()) {
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "f_dt_iss", "F emisión"));
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "f_dt_pay", "F pago"));
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_tp_pay_sys", "Método pago"));
            }
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "f_con", "Iconsistente"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "b_prc_ws", "Incorrecto PAC"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "b_prc_sto_xml", "Incorrecto XML disco"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "b_prc_sto_pdf", "Incorrecto PDF disco"));
        }
        else {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, "f_tot", "Recibos totales", 100));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, "f_sign", "CFDI timbrados", 100));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, "f_new", "CFDI x timbrar", 100));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, "f_incon", "CFDI inconsistentes", 100));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, "f_prc_ws", "CFDI incorrectos PAC", 100));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, "f_prc_sto_xml", "CFDI incorrectos XML disco", 100));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, "f_prc_sto_pdf", "CFDI incorrectos PDF disco", 100));
        }

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_CFD);
        moSuscriptionsSet.add(SModConsts.HRS_PAY);
        moSuscriptionsSet.add(SModConsts.HRS_PAY_RCP);
        moSuscriptionsSet.add(SModConsts.HRS_SIE_PAY);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbReemit) {
                actionReemitPayroll();
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
            else if (button == jbRestoreSignXml) {
                actionRestoreSignXml();
            }
            else if (button == jbRestoreAcknowledgmentCancellation) {
                actionRestoreAcknowledgmentCancellation();
            }
            else if (button == jbDiactivatePac) {
                actionCfdiDiactivateFlags();
            }
        }
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
}

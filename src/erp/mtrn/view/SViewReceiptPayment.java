/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.SClientUtils;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SCfdPaymentUtils;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SCfdUtilsHandler;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.form.SDialogAnnulCfdi;
import erp.print.SDataConstantsPrint;
import erp.redis.SLockUtils;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;
import sa.lib.srv.SLock;

/**
 * User view for management of database registries of CFDI of Payments.
 * @author Sergio Flores
 */
public class SViewReceiptPayment extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbAnnul;
    private javax.swing.JButton jbPrint;
    private javax.swing.JButton jbPrintCancelAck;
    private javax.swing.JButton jbGetXml;
    private javax.swing.JButton jbGetXmlCancelAck;
    private javax.swing.JButton jbSignXml;
    private javax.swing.JButton jbVerifyCfdi;
    private javax.swing.JButton jbGetCfdiStatus;
    private javax.swing.JButton jbSendCfdi;
    private javax.swing.JButton jbRestoreCfdStamped;
    private javax.swing.JButton jbRestoreCfdCancelAck;
    private javax.swing.JButton jbDeactivateFlags;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.mtrn.form.SDialogAnnulCfdi moDialogAnnulCfdi;

    /**
     * Creates user view for management of database registries of CFDI of Payments.
     * @param client GUI client interface.
     * @param tabTitle View tab title.
     */
    public SViewReceiptPayment(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.TRN_PAY);
        initComponents();
        miClient.showMsgBoxInformation("ACLARACIÓN:\n"
                + "Esta vista está en versión beta.\n"
                + "Favor de confirmar con soporte técnico SIIE si ya es posible usarla.\n"
                + "Tel. 443 204-1032 ext. 105\n"
                + "Mail: claudio.pena@swaplicado.com.mx");
    }

    private void initComponents() {
        jbAnnul = new JButton(miClient.getImageIcon(SLibConstants.ICON_ANNUL));
        jbAnnul.setPreferredSize(new Dimension(23, 23));
        jbAnnul.addActionListener(this);
        jbAnnul.setToolTipText("Anular comprobante");

        jbPrint = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));
        jbPrint.setPreferredSize(new Dimension(23, 23));
        jbPrint.addActionListener(this);
        jbPrint.setToolTipText("Imprimir comprobante");
        
        jbPrintCancelAck = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT_ACK_CAN));
        jbPrintCancelAck.setPreferredSize(new Dimension(23, 23));
        jbPrintCancelAck.addActionListener(this);
        jbPrintCancelAck.setToolTipText("Imprimir acuse de cancelación");

        jbGetXml = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML));
        jbGetXml.setPreferredSize(new Dimension(23, 23));
        jbGetXml.addActionListener(this);
        jbGetXml.setToolTipText("Obtener XML del comprobante");

        jbGetXmlCancelAck = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML_CANCEL));
        jbGetXmlCancelAck.setPreferredSize(new Dimension(23, 23));
        jbGetXmlCancelAck.addActionListener(this);
        jbGetXmlCancelAck.setToolTipText("Obtener XML del acuse de cancelación del CFDI");

        jbSignXml = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML_SIGN));
        jbSignXml.setPreferredSize(new Dimension(23, 23));
        jbSignXml.addActionListener(this);
        jbSignXml.setToolTipText("Timbrar CFDI");

        jbVerifyCfdi = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif")));
        jbVerifyCfdi.setPreferredSize(new Dimension(23, 23));
        jbVerifyCfdi.addActionListener(this);
        jbVerifyCfdi.setToolTipText("Verificar timbrado o cancelación del CFDI");
        
        jbGetCfdiStatus = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif")));
        jbGetCfdiStatus.setPreferredSize(new Dimension(23, 23));
        jbGetCfdiStatus.addActionListener(this);
        jbGetCfdiStatus.setToolTipText("Checar estatus cancelación del CFDI");
        
        jbSendCfdi = new JButton(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")));
        jbSendCfdi.setPreferredSize(new Dimension(23, 23));
        jbSendCfdi.addActionListener(this);
        jbSendCfdi.setToolTipText("Enviar comprobante vía mail");

        jbRestoreCfdStamped = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_insert.gif")));
        jbRestoreCfdStamped.setPreferredSize(new Dimension(23, 23));
        jbRestoreCfdStamped.addActionListener(this);
        jbRestoreCfdStamped.setToolTipText("Insertar XML timbrado del CFDI");

        jbRestoreCfdCancelAck = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_insert_annul.gif")));
        jbRestoreCfdCancelAck.setPreferredSize(new Dimension(23, 23));
        jbRestoreCfdCancelAck.addActionListener(this);
        jbRestoreCfdCancelAck.setToolTipText("Insertar PDF del acuse de cancelación del CFDI");

        jbDeactivateFlags = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif")));
        jbDeactivateFlags.setPreferredSize(new Dimension(23, 23));
        jbDeactivateFlags.addActionListener(this);
        jbDeactivateFlags.setToolTipText("Limpiar inconsistencias de timbrado o cancelación del CFDI");

        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moDialogAnnulCfdi = new SDialogAnnulCfdi(miClient);

        addTaskBarUpperComponent(jbAnnul);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarLowerComponent(jbPrint);
        addTaskBarLowerComponent(jbPrintCancelAck);
        addTaskBarLowerComponent(jbGetXml);
        addTaskBarLowerComponent(jbGetXmlCancelAck);
        addTaskBarLowerComponent(jbSignXml);
        addTaskBarLowerComponent(jbVerifyCfdi);
        addTaskBarLowerComponent(jbGetCfdiStatus);
        addTaskBarLowerComponent(jbSendCfdi);
        addTaskBarLowerComponent(jbRestoreCfdStamped);
        addTaskBarLowerComponent(jbRestoreCfdCancelAck);
        addTaskBarLowerComponent(jbDeactivateFlags);
        
        if (miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_CFD_PAY).HasRight) {
            enableButtons(miClient.getSession().getUser().getPrivilegeLevel(SDataConstantsSys.PRV_FIN_CFD_PAY)); // a more specific right has precedence
        }
        else if (miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_MOV_ACC_CASH).HasRight){
            enableButtons(miClient.getSession().getUser().getPrivilegeLevel(SDataConstantsSys.PRV_FIN_MOV_ACC_CASH));
        }
        
        STableField[] aoKeyFields = new STableField[1];
        aoKeyFields[0] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_cfd");
        moTablePane.getPrimaryKeyFields().add(aoKeyFields[0]);

        int col = 0;
        STableColumn[] aoTableColumns = new STableColumn[18];
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_num", "Folio CFDI", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "p.dt", "Fecha-hora CFDI", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "_ico", "Estatus", STableConstants.WIDTH_ICON);
        aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
        aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "_ico_xml", "CFD", STableConstants.WIDTH_ICON);
        aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Deudor", 250);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.fiscal_id", "RFC Deudor", 100);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "p.pay_loc_r", "Total loc $", 100);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.uuid", "UUID CFDI", 250);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "fb.bp", "Banco factoraje", 250);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "fb.fiscal_id", "RFC banco factoraje", 100);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "p.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_usr_new", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "p.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_usr_edit", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "p.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_usr_del", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "p.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        
        for (col = 0; col < aoTableColumns.length; col++) {
            moTablePane.addTableColumn(aoTableColumns[col]);
        }

        SFormUtilities.createActionMap(this, this, "publicActionPrint", "print", KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);
        setIsSummaryApplying(true);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.TRNX_CFD_PAY_REC);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }
    
    private void enableButtons(int level) {
        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false); // deletion is not allowed
        jbAnnul.setEnabled(false);
        jbPrint.setEnabled(false);
        jbPrintCancelAck.setEnabled(false);
        jbGetXml.setEnabled(false);
        jbGetXmlCancelAck.setEnabled(false);
        jbSignXml.setEnabled(false);
        jbVerifyCfdi.setEnabled(false);
        jbGetCfdiStatus.setEnabled(false);
        jbSendCfdi.setEnabled(false);
        jbRestoreCfdStamped.setEnabled(false);
        jbRestoreCfdCancelAck.setEnabled(false);
        jbDeactivateFlags.setEnabled(false);
        
        switch (level) {
            case SUtilConsts.LEV_READ:
                jbPrint.setEnabled(true);
                jbPrintCancelAck.setEnabled(true);
                jbGetXml.setEnabled(true);
                jbGetXmlCancelAck.setEnabled(true);
                jbGetCfdiStatus.setEnabled(true);
                break;
            case SUtilConsts.LEV_CAPTURE:
            case SUtilConsts.LEV_AUTHOR:
            case SUtilConsts.LEV_EDITOR:
                jbNew.setEnabled(true);
                jbEdit.setEnabled(true);
                jbPrint.setEnabled(true);
                jbPrintCancelAck.setEnabled(true);
                jbGetXml.setEnabled(true);
                jbGetXmlCancelAck.setEnabled(true);
                jbSignXml.setEnabled(true);
                jbVerifyCfdi.setEnabled(true);
                jbGetCfdiStatus.setEnabled(true);
                jbSendCfdi.setEnabled(true);
                jbRestoreCfdStamped.setEnabled(true);
                jbDeactivateFlags.setEnabled(true);
                break;
            case SUtilConsts.LEV_MANAGER:
                jbNew.setEnabled(true);
                jbEdit.setEnabled(true);
                jbAnnul.setEnabled(true);
                jbPrint.setEnabled(true);
                jbPrintCancelAck.setEnabled(true);
                jbGetXml.setEnabled(true);
                jbGetXmlCancelAck.setEnabled(true);
                jbSignXml.setEnabled(true);
                jbVerifyCfdi.setEnabled(true);
                jbGetCfdiStatus.setEnabled(true);
                jbSendCfdi.setEnabled(true);
                jbRestoreCfdStamped.setEnabled(true);
                jbRestoreCfdCancelAck.setEnabled(true);
                jbDeactivateFlags.setEnabled(true);
                break;
            default:
        }
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_SAL).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
       if (jbEdit.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null) {
               miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
           }
           else{
                if (miClient.getGuiModule(SDataConstants.MOD_SAL).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    @Override
    public void actionDelete() {
       if (jbDelete.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                    if (miClient.getGuiModule(SDataConstants.MOD_SAL).deleteRegistry(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_DELETE_OK) {
                        miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                    }
                }
            }
        }
    }

    private void actionAnnul() throws Exception {
        if (jbAnnul.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_ANNUL) == JOptionPane.YES_OPTION) {
                    boolean annul = true;
                    SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                    ArrayList<Object[]> journalVoucherKeys = SDataCfd.getDependentJournalVoucherKeys(miClient.getSession().getStatement(), cfd.getPkCfdId());
                    /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                    ArrayList<SSrvLock> locks = new ArrayList<>();
                    */
                    /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                    ArrayList<SRedisLock> rlocks = new ArrayList<>();
                    */
                    ArrayList<SLock> slocks = new ArrayList<>();
                    try {
                        try {
                            // gain locks for all journal vouchers:
                            
                            for (int index = 0; index < journalVoucherKeys.size(); index++) {
                                if (journalVoucherKeys.get(index) != null) {
                                    /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                                    SSrvLock lock = SSrvUtils.gainLock(miClient.getSession(), miClient.getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.FIN_REC, journalVoucherKeys.get(index), 10 * 60 * 1000); // 10 min.
                                    locks.add(lock);
                                    */
                                    /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                                    SRedisLock rlock = SRedisLockUtils.gainLock(miClient, SDataConstants.FIN_REC, journalVoucherKeys.get(index), 10 * 60); // 10 min.
                                    rlocks.add(rlock);
                                    */
                                    SLock slock = SLockUtils.gainLock(miClient, SDataConstants.FIN_REC, journalVoucherKeys.get(index), 10 * 60 * 1000); // 10 min.
                                    slocks.add(slock);
                                }
                            }
                        }
                        catch (Exception e) {
                            annul = false;
                            SLibUtils.showException(this, e);
                        }
                        
                        if (annul) {
                            SGuiParams params = new SGuiParams();
                            
                            if (cfd.isCfdi()) {
                                annul = false;

                                if (cfd.isStamped()) {
                                    moDialogAnnulCfdi.formReset();
                                    moDialogAnnulCfdi.formRefreshCatalogues();
                                    moDialogAnnulCfdi.setValue(SGuiConsts.PARAM_DATE, cfd.getTimestamp());
                                    moDialogAnnulCfdi.setValue(SModConsts.TRNS_TP_CFD, SDataConstantsSys.TRNS_TP_CFD_PAY_REC);
                                    moDialogAnnulCfdi.setVisible(true);

                                    if (moDialogAnnulCfdi.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                        annul = true;
                                        params.getParamsMap().put(SGuiConsts.PARAM_DATE, moDialogAnnulCfdi.getAnnulDate());
                                        // SGuiConsts.PARAM_REQ_DOC is used to indicate if SAT cancellation is required (true/false):
                                        params.getParamsMap().put(SGuiConsts.PARAM_REQ_DOC, moDialogAnnulCfdi.getAnnulSat());
                                        // cause of annulation:
                                        params.getParamsMap().put(SModConsts.TRNU_TP_DPS_ANN, moDialogAnnulCfdi.getDpsAnnulType());
                                        
                                        params.getParamsMap().put(SGuiConsts.PARAM_ANNUL_REASON, moDialogAnnulCfdi.getAnnulReason());
                                        params.getParamsMap().put(SGuiConsts.PARAM_ANNUL_RELATED_UUID, moDialogAnnulCfdi.getAnnulRelatedUuid());
                                    }
                                }
                                else {
                                    annul = true;
                                    params.getParamsMap().put(SGuiConsts.PARAM_DATE, miClient.getSession().getCurrentDate());
                                    // SGuiConsts.PARAM_REQ_DOC is used to indicate if SAT cancellation is required (false):
                                    params.getParamsMap().put(SGuiConsts.PARAM_REQ_DOC, false);
                                    // cause of annulation:
                                    params.getParamsMap().put(SModConsts.TRNU_TP_DPS_ANN, SModSysConsts.TRNU_TP_DPS_ANN_NA);

                                    params.getParamsMap().put(SGuiConsts.PARAM_ANNUL_REASON, "");
                                    params.getParamsMap().put(SGuiConsts.PARAM_ANNUL_RELATED_UUID, "");
                                }
                            }

                            if (annul) {
                                if (miClient.getGuiModule(SDataConstants.MOD_SAL).annulRegistry(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey(), params) == SLibConstants.DB_ACTION_ANNUL_OK) {
                                    miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                    finally {
                        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                        for (SSrvLock lock : locks) {
                            SSrvUtils.releaseLock(miClient.getSession(), lock);
                        }
                        */
                        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                        for (SRedisLock rlock : rlocks) {
                            SRedisLockUtils.releaseLock(miClient, rlock);
                        }
                        */
                        for (SLock slock : slocks) {
                            SLockUtils.releaseLock(miClient, slock);
                        }
                    }
                }
            }
        }
    }

    private void actionPrint() {
        if (jbPrint.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                    SCfdUtils.printCfd((SClientInterface) miClient, cfd, SLibConstants.UNDEFINED, SDataConstantsPrint.PRINT_MODE_VIEWER, 1, false);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionPrintCancelAck() {
        if (jbPrintCancelAck.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SDataCfd cfd = SCfdUtils.getCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_PAY_REC, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                    SCfdUtils.printCfdCancelAck(miClient, cfd, 0, SDataConstantsPrint.PRINT_MODE_VIEWER);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionGetXml() {
        if (jbGetXml.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SDataCfd cfd = SCfdUtils.getCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_PAY_REC, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                    SCfdUtils.downloadXmlCfd(miClient, cfd);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionGetCancelAck() {
        if (jbGetXmlCancelAck.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SDataCfd cfd = SCfdUtils.getCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_PAY_REC, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                    SCfdUtils.getAcknowledgmentCancellationCfd(miClient, cfd);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionSignXml() throws Exception {
        if (jbSignXml.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SCfdPaymentUtils.sign(miClient, ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0]);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionVerifyCfdi() throws Exception {
        if (jbVerifyCfdi.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                    if (SCfdUtils.validateCfdi(miClient, cfd, 0, true)) {
                        miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionGetCfdiStatus() throws Exception {
        if (jbGetCfdiStatus.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                    if (!cfd.isStamped()) {
                        miClient.showMsgBoxInformation("El comprobante " + cfd.getCfdNumber() + " no está timbrado.");
                    }
                    else {
                        miClient.showMsgBoxInformation(new SCfdUtilsHandler(miClient).getCfdiSatStatus(cfd).getDetailedStatus());
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionSendCfdi() {
        if (jbSendCfdi.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                    SCfdUtils.sendCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_PAY_REC, cfd, 0, true, false, true);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionRestoreSignedXml() throws Exception {
        if (jbRestoreCfdStamped.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                    boolean needUpdate = SCfdUtils.restoreCfdStamped(miClient, cfd, 0, true);

                    if (needUpdate) {
                        miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionRestoreSignedXmlCancelAck() throws Exception {
        if (jbRestoreCfdCancelAck.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                    boolean needUpdate = SCfdUtils.restoreCfdCancelAck(miClient, cfd, 0, true);

                    if (needUpdate) {
                        miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionDeactivateFlags() throws Exception {
        if (jbDeactivateFlags.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                    SCfdUtils.resetCfdiDeactivateFlags(miClient, cfd);
                    miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    public void publicActionPrint() {
        actionPrint();
    }
    
    @Override
    public void createSqlQuery() {
        String where = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                int[] period = (int[]) setting.getSetting();
                where += (where.isEmpty() ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter(period, "p.dt");
            }
        }
        
        String complementaryDbName = "";
        
        try {
            complementaryDbName = SClientUtils.getComplementaryDdName((SClientInterface) miClient);
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
         
        msSql = "SELECT c.id_cfd, p.dt, CONCAT(p.ser, IF(length(p.ser) = 0, '', '-'), p.num) AS _num, p.pay_loc_r, p.b_del, " +
                "c.uuid, cob.code AS _cob_code, bp.bp, bp.fiscal_id, fb.bp, fb.fiscal_id, " +
                "IF(c.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", " + STableConstants.ICON_ST_ANNUL + ", " + STableConstants.ICON_NULL + ") AS _ico, " +
                "IF(c.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_CFD + " OR c.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_NA + ", " + STableConstants.ICON_XML + ", " + /* is CFD */
                "IF(c.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " OR LENGTH(c.uuid) = 0, " + STableConstants.ICON_XML_PEND + ", " + /* CFDI pending sign */
                "IF(LENGTH(xc.ack_can_xml) = 0 AND xc.ack_can_pdf_n IS NULL, " + STableConstants.ICON_XML_SIGN  + ", " + /* CFDI signed, canceled only SIIE */
                "IF(LENGTH(xc.ack_can_xml) != 0, " + STableConstants.ICON_XML_CANC_XML + ", " + /* CFDI canceled with cancellation acknowledgment in XML format */
                "IF(xc.ack_can_pdf_n IS NOT NULL, " + STableConstants.ICON_XML_CANC_PDF + ", " + /* CFDI canceled with cancellation acknowledgment in PDF format */
                STableConstants.ICON_XML_SIGN + " " + /* CFDI signed, canceled only SIIE */
                "))))) AS _ico_xml, " +
                "p.ts_new, p.ts_edit, p.ts_del, " +
                "usr_new.usr AS _usr_new, usr_edit.usr AS _usr_edit, usr_del.usr AS _usr_del " + 
                "FROM trn_pay AS p " +
                "INNER JOIN erp.bpsu_bpb AS cob ON cob.id_bpb = p.fid_cob " +
                "INNER JOIN erp.bpsu_bp AS bp ON bp.id_bp = p.fid_bp " +
                "INNER JOIN trn_cfd AS c ON c.fid_rcp_pay_n = p.id_rcp " +
                "INNER JOIN " + complementaryDbName + ".trn_cfd AS xc ON c.id_cfd = xc.id_cfd " +
                "INNER JOIN erp.usru_usr AS usr_new ON p.fid_usr_new = usr_new.id_usr " +
                "INNER JOIN erp.usru_usr AS usr_edit ON p.fid_usr_edit = usr_edit.id_usr " +
                "INNER JOIN erp.usru_usr AS usr_del ON p.fid_usr_del = usr_del.id_usr " +
                "LEFT OUTER JOIN erp.bpsu_bp AS fb ON fb.id_bp = p.fid_fact_bank_n " +
                (where.isEmpty() ? "" : "WHERE " + where) +
                "ORDER BY p.ser, CONVERT(p.num, UNSIGNED), p.dt, c.id_cfd ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        super.actionPerformed(evt);

        try {
            if (evt.getSource() instanceof javax.swing.JButton) {
                JButton button = (JButton) evt.getSource();

                if (button == jbAnnul) {
                    actionAnnul();
                }
                else if (button == jbPrint) {
                    actionPrint();
                }
                else if (button == jbPrintCancelAck) {
                    actionPrintCancelAck();
                }
                else if (button == jbGetXml) {
                    actionGetXml();
                }
                else if (button == jbGetXmlCancelAck) {
                    actionGetCancelAck();
                }
                else if (button == jbSignXml) {
                    actionSignXml();
                }
                else if (button == jbVerifyCfdi) {
                    actionVerifyCfdi();
                }
                else if (button == jbGetCfdiStatus) {
                    actionGetCfdiStatus();
                }
                else if (button == jbSendCfdi) {
                    actionSendCfdi();
                }
                else if (button == jbRestoreCfdStamped) {
                    actionRestoreSignedXml();
                }
                else if (button == jbRestoreCfdCancelAck) {
                    actionRestoreSignedXmlCancelAck();
                }
                else if (button == jbDeactivateFlags) {
                    actionDeactivateFlags();
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }
}

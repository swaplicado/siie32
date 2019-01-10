/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

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
import erp.mfin.form.SDialogAccountingMoveDpsBizPartner;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SCfdPaymentUtils;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.form.SDialogAnnulCfdi;
import erp.print.SDataConstantsPrint;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;

/**
 * User view for management of database registries of CFDI of Payments.
 * @author Sergio Flores
 */
public class SViewCfdPayment extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbAnnul;
    private javax.swing.JButton jbPrint;
    private javax.swing.JButton jbPrintCancelAck;
    private javax.swing.JButton jbGetXml;
    private javax.swing.JButton jbGetXmlCancelAck;
    private javax.swing.JButton jbSignXml;
    private javax.swing.JButton jbVerifyCfdi;
    private javax.swing.JButton jbSendCfdi;
    private javax.swing.JButton jbDeactivateFlags;
    private javax.swing.JButton jbRestoreSignedXml;
    private javax.swing.JButton jbRestoreSignedXmlCancelAck;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.mfin.form.SDialogAccountingMoveDpsBizPartner moDialogAccountingMoveDpsBizPartner;
    private erp.mtrn.form.SDialogAnnulCfdi moDialogAnnulCfdi;

    /**
     * Creates user view for management of database registries of CFDI of Payments.
     * @param client GUI client interface.
     * @param tabTitle View tab title.
     */
    public SViewCfdPayment(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.TRNX_CFD_PAY_REC);
        initComponents();
    }

    private void initComponents() {
        jbAnnul = new JButton(miClient.getImageIcon(SLibConstants.ICON_ANNUL));
        jbAnnul.setPreferredSize(new Dimension(23, 23));
        jbAnnul.addActionListener(this);
        jbAnnul.setToolTipText("Anular documento");

        jbPrint = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));
        jbPrint.setPreferredSize(new Dimension(23, 23));
        jbPrint.addActionListener(this);
        jbPrint.setToolTipText("Imprimir documento");
        
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
        
        jbSendCfdi = new JButton(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")));
        jbSendCfdi.setPreferredSize(new Dimension(23, 23));
        jbSendCfdi.addActionListener(this);
        jbSendCfdi.setToolTipText("Enviar comprobante");

        jbRestoreSignedXml = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_insert.gif")));
        jbRestoreSignedXml.setPreferredSize(new Dimension(23, 23));
        jbRestoreSignedXml.addActionListener(this);
        jbRestoreSignedXml.setToolTipText("Insertar XML timbrado del CFDI");

        jbRestoreSignedXmlCancelAck = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_insert_annul.gif")));
        jbRestoreSignedXmlCancelAck.setPreferredSize(new Dimension(23, 23));
        jbRestoreSignedXmlCancelAck.addActionListener(this);
        jbRestoreSignedXmlCancelAck.setToolTipText("Insertar PDF del acuse de cancelación del CFDI");

        jbDeactivateFlags = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif")));
        jbDeactivateFlags.setPreferredSize(new Dimension(23, 23));
        jbDeactivateFlags.addActionListener(this);
        jbDeactivateFlags.setToolTipText("Limpiar inconsistencias de timbrado o cancelación del CFDI");

        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moDialogAccountingMoveDpsBizPartner = new SDialogAccountingMoveDpsBizPartner(miClient, mnTabTypeAux01);
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
        addTaskBarLowerComponent(jbSendCfdi);
        addTaskBarLowerComponent(jbRestoreSignedXml);
        addTaskBarLowerComponent(jbRestoreSignedXmlCancelAck);
        addTaskBarLowerComponent(jbDeactivateFlags);

        jbNew.setEnabled(true);
        jbEdit.setEnabled(true);
        jbDelete.setEnabled(false); // deletion is not allowed
        jbAnnul.setEnabled(true);
        jbPrint.setEnabled(true);
        jbPrintCancelAck.setEnabled(true);
        jbGetXml.setEnabled(true);
        jbGetXmlCancelAck.setEnabled(true);
        jbSignXml.setEnabled(true);
        jbVerifyCfdi.setEnabled(true);
        jbSendCfdi.setEnabled(true);
        jbRestoreSignedXml.setEnabled(true);
        jbRestoreSignedXmlCancelAck.setEnabled(true);
        jbDeactivateFlags.setEnabled(true);

        STableField[] aoKeyFields = new STableField[1];
        aoKeyFields[0] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_cfd");
        moTablePane.getPrimaryKeyFields().add(aoKeyFields[0]);

        int col = 0;
        STableColumn[] aoTableColumns = new STableColumn[11];
        
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_num", "Folio CFDI", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts", "Fecha-hora CFDI", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "_ico", "Estatus", STableConstants.WIDTH_ICON);
        aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
        aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "_ico_xml", "CFD", STableConstants.WIDTH_ICON);
        aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.uuid", "UUID CFDI", 250);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.xml_rfc_rec", "RFC receptor CFDI", 100);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Receptor original", 250);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.fiscal_id", "RFC receptor original", 100);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "fb.bp", "Banco factoraje", 250);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "fb.fiscal_id", "RFC banco factoraje", 100);
        
        for (col = 0; col < aoTableColumns.length; col++) {
            moTablePane.addTableColumn(aoTableColumns[col]);
        }

        SFormUtilities.createActionMap(this, this, "publicActionPrint", "print", KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);
        setIsSummaryApplying(true);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
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
                    try {
                        boolean annul = true;
                        SGuiParams params = new SGuiParams();
                        SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

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
                                    params.getParamsMap().put(SGuiConsts.PARAM_DATE, moDialogAnnulCfdi.getDate());
                                    // SGuiConsts.PARAM_REQ_DOC is used to indicate if SAT cancellation is required (true/false):
                                    params.getParamsMap().put(SGuiConsts.PARAM_REQ_DOC, moDialogAnnulCfdi.getAnnulSat());
                                    // cause of annulation:
                                    params.getParamsMap().put(SModConsts.TRNU_TP_DPS_ANN, moDialogAnnulCfdi.getDpsAnnulationType());
                                }
                            }
                            else {
                                annul = true;
                                params.getParamsMap().put(SGuiConsts.PARAM_DATE, miClient.getSession().getCurrentDate());
                                // SGuiConsts.PARAM_REQ_DOC is used to indicate if SAT cancellation is required (false):
                                params.getParamsMap().put(SGuiConsts.PARAM_REQ_DOC, false);
                                // cause of annulation:
                                params.getParamsMap().put(SModConsts.TRNU_TP_DPS_ANN, SModSysConsts.TRNU_TP_DPS_ANN_NA);
                            }
                        }

                        if (annul) {
                            if (miClient.getGuiModule(SDataConstants.MOD_SAL).annulRegistry(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey(), params) == SLibConstants.DB_ACTION_ANNUL_OK) {
                                miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                            }
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
        if (jbPrint.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                    SCfdUtils.printCfd((SClientInterface) miClient, cfd, 0, SDataConstantsPrint.PRINT_MODE_VIEWER, 1, false);
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
                    SCfdUtils.printAcknowledgmentCancellationCfd(miClient, SCfdUtils.getCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_PAY_REC, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey()), 0);
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
                    SCfdUtils.getXmlCfd(miClient, SCfdUtils.getCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_PAY_REC, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey()));
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
                    SCfdUtils.getAcknowledgmentCancellationCfd(miClient, SCfdUtils.getCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_PAY_REC, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey()));
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

    private void actionSendCfdi() {
        if (jbSendCfdi.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                    SCfdUtils.sendCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_PAY_REC, cfd, 0, true, false);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionRestoreSignedXml() throws Exception {
        if (jbRestoreSignedXml.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                    boolean needUpdate = SCfdUtils.restoreSignXml(miClient, cfd, true, 0);

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
        if (jbRestoreSignedXmlCancelAck.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                    boolean needUpdate = SCfdUtils.restoreAcknowledgmentCancellation(miClient, cfd, true, 0);

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
        String where = "";      // for main from
        String whereRe = "";    // for from in derived table 're'
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                int[] period = (int[]) setting.getSetting();
                where += (where.isEmpty() ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter(period, "c.ts");
                whereRe += (whereRe.isEmpty() ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter(new int[] { period[0] }, "r.dt");
            }
        }

        msSql = "SELECT c.id_cfd, c.ts, CONCAT(c.ser, IF(length(c.ser) = 0, '', '-'), c.num) AS _num, c.uuid, c.xml_rfc_rec, " +
                "IF(c.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", " + STableConstants.ICON_ST_ANNUL + ", " + STableConstants.ICON_NULL + ") AS _ico, " +
                "IF(c.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_CFD + " OR c.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_NA + ", " + STableConstants.ICON_XML + ", " + /* is CFD */
                "IF(c.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " OR LENGTH(c.uuid) = 0, " + STableConstants.ICON_XML_PEND + ", " + /* CFDI pending sign */
                "IF(LENGTH(c.ack_can_xml) = 0 AND c.ack_can_pdf_n IS NULL, " + STableConstants.ICON_XML_SIGN  + ", " + /* CFDI signed, canceled only SIIE */
                "IF(LENGTH(c.ack_can_xml) != 0, " + STableConstants.ICON_XML_CANC_XML + ", " + /* CFDI canceled with cancellation acknowledgment in XML format */
                "IF(c.ack_can_pdf_n IS NOT NULL, " + STableConstants.ICON_XML_CANC_PDF + ", " + /* CFDI canceled with cancellation acknowledgment in PDF format */
                STableConstants.ICON_XML_SIGN + " " + /* CFDI signed, canceled only SIIE */
                "))))) AS _ico_xml, " +
                "cob.code AS _cob_code, " +
                "b.bp, b.fiscal_id, " +
                "fb.bp, fb.fiscal_id " +
                "FROM trn_cfd AS c " +
                "INNER JOIN erp.bpsu_bpb AS cob ON c.fid_cob_n = cob.id_bpb " +
                "LEFT OUTER JOIN " +
                "(SELECT DISTINCT re.fid_cfd_n, re.fid_bp_nr " +
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
                "WHERE " + whereRe + " AND NOT r.b_del AND NOT re.b_del AND re.fid_cfd_n IS NOT NULL AND re.fid_bp_nr IS NOT NULL) AS re ON re.fid_cfd_n = c.id_cfd " +
                "LEFT OUTER JOIN erp.bpsu_bp AS b ON b.id_bp = re.fid_bp_nr " +
                "LEFT OUTER JOIN erp.bpsu_bp AS fb ON fb.id_bp = c.fid_fact_bank_n " +
                (where.isEmpty() ? "" : "WHERE " + where) +
                "ORDER BY c.ser, CONVERT(c.num, UNSIGNED), c.ts, c.id_cfd ";
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
                else if (button == jbSendCfdi) {
                    actionSendCfdi();
                }
                else if (button == jbRestoreSignedXml) {
                    actionRestoreSignedXml();
                }
                else if (button == jbRestoreSignedXmlCancelAck) {
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

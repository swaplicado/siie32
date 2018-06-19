/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.cfd.SCfdConsts;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mbps.data.SDataBizPartnerBranchContact;
import erp.mcfg.data.SDataCurrency;
import erp.mfin.form.SDialogAccountingMoveDpsBizPartner;
import erp.mitm.data.SDataUnit;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.STrnUtilities;
import erp.mtrn.form.SDialogAnnulCfdi;
import erp.musr.data.SDataUser;
import erp.print.SDataConstantsPrint;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;

/**
 * User view for management of database registries of CFDI of Payments.
 * @author Sergio Flores
 */
public class SViewCfdPayment extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbAnnul;
    private javax.swing.JButton jbPrint;
    private javax.swing.JButton jbPrintAcknowledgmentCancellation;
    private javax.swing.JButton jbGetXml;
    private javax.swing.JButton jbGetAcknowledgmentCancellation;
    private javax.swing.JButton jbSignXml;
    private javax.swing.JButton jbVerifyCfdi;
    private javax.swing.JButton jbSendCfdi;
    private javax.swing.JButton jbDiactivateFlags;
    private javax.swing.JButton jbRestoreSignXml;
    private javax.swing.JButton jbRestoreAcknowledgmentCancellation;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.mfin.form.SDialogAccountingMoveDpsBizPartner moDialogAccountingMoveDpsBizPartner;
    private erp.mtrn.form.SDialogAnnulCfdi moDialogAnnulCfdi;

    private static final String TXT_SEND = "Enviar documento";

    /**
     * Creates user view for management of database registries of CFDI of Payments.
     * @param client GUI client interface.
     * @param tabTitle View tab title.
     */
    public SViewCfdPayment(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.TRNX_CFD_PAY);
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
        
        jbPrintAcknowledgmentCancellation = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT_ACK_CAN));
        jbPrintAcknowledgmentCancellation.setPreferredSize(new Dimension(23, 23));
        jbPrintAcknowledgmentCancellation.addActionListener(this);
        jbPrintAcknowledgmentCancellation.setToolTipText("Imprimir acuse de cancelación");

        jbGetXml = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML));
        jbGetXml.setPreferredSize(new Dimension(23, 23));
        jbGetXml.addActionListener(this);
        jbGetXml.setToolTipText("Obtener XML del comprobante");

        jbGetAcknowledgmentCancellation = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML_CANCEL));
        jbGetAcknowledgmentCancellation.setPreferredSize(new Dimension(23, 23));
        jbGetAcknowledgmentCancellation.addActionListener(this);
        jbGetAcknowledgmentCancellation.setToolTipText("Obtener XML del acuse de cancelación del CFDI");

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

        jbRestoreSignXml = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_insert.gif")));
        jbRestoreSignXml.setPreferredSize(new Dimension(23, 23));
        jbRestoreSignXml.addActionListener(this);
        jbRestoreSignXml.setToolTipText("Insertar XML timbrado del CFDI");

        jbRestoreAcknowledgmentCancellation = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_insert_annul.gif")));
        jbRestoreAcknowledgmentCancellation.setPreferredSize(new Dimension(23, 23));
        jbRestoreAcknowledgmentCancellation.addActionListener(this);
        jbRestoreAcknowledgmentCancellation.setToolTipText("Insertar PDF del acuse de cancelación del CFDI");

        jbDiactivateFlags = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif")));
        jbDiactivateFlags.setPreferredSize(new Dimension(23, 23));
        jbDiactivateFlags.addActionListener(this);
        jbDiactivateFlags.setToolTipText("Limpiar inconsistencias de timbrado o cancelación del CFDI");

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moDialogAccountingMoveDpsBizPartner = new SDialogAccountingMoveDpsBizPartner(miClient, mnTabTypeAux01);
        moDialogAnnulCfdi = new SDialogAnnulCfdi(miClient);

        addTaskBarUpperComponent(jbAnnul);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarLowerComponent(jbPrint);
        addTaskBarLowerComponent(jbPrintAcknowledgmentCancellation);
        addTaskBarLowerComponent(jbGetXml);
        addTaskBarLowerComponent(jbGetAcknowledgmentCancellation);
        addTaskBarLowerComponent(jbSignXml);
        addTaskBarLowerComponent(jbVerifyCfdi);
        addTaskBarLowerComponent(jbSendCfdi);
        addTaskBarLowerComponent(jbRestoreSignXml);
        addTaskBarLowerComponent(jbRestoreAcknowledgmentCancellation);
        addTaskBarLowerComponent(jbDiactivateFlags);

        jbNew.setEnabled(true);
        jbEdit.setEnabled(true);
        jbDelete.setEnabled(true);
        jbAnnul.setEnabled(true);
        jbPrint.setEnabled(true);
        jbPrintAcknowledgmentCancellation.setEnabled(true);
        jbGetXml.setEnabled(true);
        jbGetAcknowledgmentCancellation.setEnabled(true);
        jbSignXml.setEnabled(true);
        jbVerifyCfdi.setEnabled(true);
        jbSendCfdi.setEnabled(true);
        jbRestoreSignXml.setEnabled(true);
        jbRestoreAcknowledgmentCancellation.setEnabled(true);
        jbDiactivateFlags.setEnabled(true);

        STableField[] aoKeyFields = new STableField[1];
        aoKeyFields[0] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_cfd");
        moTablePane.getPrimaryKeyFields().add(aoKeyFields[0]);

        int col = 0;
        STableColumn[] aoTableColumns = new STableColumn[8];
        
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_num", "Folio CFDI", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts", "Fecha-hora CFDI", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "_ico", "Estatus", STableConstants.WIDTH_ICON);
        aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
        aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "_ico_xml", "CFD", STableConstants.WIDTH_ICON);
        aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.uuid", "UUID", 200);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.xml_rfc_rec", "RFC receptor", 100);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_bp", "Receptor", 300);
/*
        if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
        }
        else {
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
        }
        aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_r", "Total $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[col++].setSumApplying(true);

        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
*/
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
           if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
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
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
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
        SDataDps dps = null;
        SGuiParams params = null;
        boolean annul = true;

        if (jbAnnul.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_ANNUL) == JOptionPane.YES_OPTION) {
                    dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                    if (dps.getDbmsDataCfd() != null && dps.getDbmsDataCfd().isCfdi()) {
                        annul = false;
                        params = new SGuiParams();

                        if (dps.getDbmsDataCfd().isStamped()) {
                            moDialogAnnulCfdi.formReset();
                            moDialogAnnulCfdi.formRefreshCatalogues();
                            moDialogAnnulCfdi.setValue(SGuiConsts.PARAM_DATE, dps.getDate());
                            moDialogAnnulCfdi.setValue(SModConsts.TRNS_TP_CFD, dps.getComprobanteTipoDeComprobante());
                            moDialogAnnulCfdi.setVisible(true);

                            if (moDialogAnnulCfdi.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                annul = true;
                                params.getParamsMap().put(SGuiConsts.PARAM_DATE, moDialogAnnulCfdi.getDate());
                                params.getParamsMap().put(SGuiConsts.PARAM_REQ_DOC, moDialogAnnulCfdi.getAnnulSat()); // SGuiConsts.PARAM_REQ_DOC is used to indicate if SAT cancellation is required
                                params.getParamsMap().put(SModConsts.TRNU_TP_DPS_ANN, moDialogAnnulCfdi.getDpsAnnulationType()); // cause of annulation
                            }
                        }
                        else {
                            annul = true;
                            params.getParamsMap().put(SGuiConsts.PARAM_DATE, miClient.getSession().getCurrentDate());
                            params.getParamsMap().put(SGuiConsts.PARAM_REQ_DOC, false);
                            params.getParamsMap().put(SModConsts.TRNU_TP_DPS_ANN, SModSysConsts.TRNU_TP_DPS_ANN_NA); // cause of annulation
                        }
                    }

                    if (annul) {
                        if (miClient.getGuiModule(SDataConstants.MOD_SAL).annulRegistry(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey(), params) == SLibConstants.DB_ACTION_ANNUL_OK) {
                            miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                        }
                    }
                }
            }
        }
    }

    private void actionPrint() {
        if (jbPrint.isEnabled()) {
            int i = 0;
            String sUserBuyer = "";
            String sUserAuthorize = "";
            int nFkEmiAddressFormatTypeId_n = 0;
            int nFkRecAddressFormatTypeId_n = 0;
            boolean bincludeCountry = false;
            int[] keyDpsType = null;
            String[] addressOficial = null;
            String[] addressContract = null;
            String[] addressDelivery = null;
            String[] addressDeliveryCompany = null;
            Cursor cursor = getCursor();
            Map<String, Object> map = null;
            JasperPrint jasperPrint = null;
            JasperViewer jasperViewer = null;
            SDataDps oDps = null;
            SDataBizPartnerBranch oCompanyBranch = null;
            SDataBizPartnerBranch oBizPartnerBranch = null;
            SDataBizPartnerBranchAddress oAddress = null;
            SDataBizPartnerBranchContact oContact = null;
            SDataUser oUserBuyer = null;
            SDataUser oUserAuthorize = null;
            SDataCurrency oCurrency = null;
            SDataUnit oUnit = null;
            Vector<String> vPrice = null;
            Vector<String> vUnit = null;
            String sContact = "";

            if (moTablePane.getSelectedTableRow() == null/* && !isBizPartnerBlocked()*/|| moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                oDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (oDps.getDbmsDataCfd() != null) {
                    try {
                        SCfdUtils.printCfd(miClient, oDps.getDbmsDataCfd(), SLibConstants.UNDEFINED, SDataConstantsPrint.PRINT_MODE_VIEWER, 1, false);
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                }
                else {
                    oCompanyBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { oDps.getFkCompanyBranchId() }, SLibConstants.EXEC_MODE_SILENT);
                    oBizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { oDps.getFkBizPartnerBranchId() }, SLibConstants.EXEC_MODE_SILENT);
                    oAddress = (SDataBizPartnerBranchAddress) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB_ADD, new int [] { oDps.getFkBizPartnerBranchId(),
                        oDps.getFkBizPartnerBranchAddressId() }, SLibConstants.EXEC_MODE_SILENT);
                    oCurrency = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR, new int[] { oDps.getFkCurrencyId() }, SLibConstants.EXEC_MODE_SILENT);

                    if (oBizPartnerBranch.getFkAddressFormatTypeId_n() != SLibConstants.UNDEFINED) {
                        nFkRecAddressFormatTypeId_n = oBizPartnerBranch.getFkAddressFormatTypeId_n();
                    }
                    else {
                        nFkRecAddressFormatTypeId_n = miClient.getSessionXXX().getParamsCompany().getFkDefaultAddressFormatTypeId_n();
                    }

                    if (oCompanyBranch.getFkAddressFormatTypeId_n() != SLibConstants.UNDEFINED) {
                        nFkEmiAddressFormatTypeId_n = oAddress.getFkAddressTypeId();
                    }
                    else {
                        nFkEmiAddressFormatTypeId_n = miClient.getSessionXXX().getParamsCompany().getFkDefaultAddressFormatTypeId_n();
                    }

                    if (oAddress.getFkCountryId_n() == oCompanyBranch.getDbmsBizPartnerBranchAddressOfficial().getFkCountryId_n()) {
                        bincludeCountry = false;
                    }
                    else {
                        bincludeCountry = true;
                    }

                    addressOficial = oBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(nFkRecAddressFormatTypeId_n,
                        SDataBizPartnerBranchAddress.ADDRESS_4ROWS, bincludeCountry);
                    addressContract = oBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(nFkRecAddressFormatTypeId_n,
                        SDataBizPartnerBranchAddress.ADDRESS_4ROWS, bincludeCountry);
                    addressDelivery = oAddress.obtainAddress(nFkRecAddressFormatTypeId_n,
                        SDataBizPartnerBranchAddress.ADDRESS_4ROWS, bincludeCountry);
                    addressDeliveryCompany = oCompanyBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(nFkEmiAddressFormatTypeId_n,
                        SDataBizPartnerBranchAddress.ADDRESS_4ROWS, bincludeCountry);
                    oUserBuyer = (SDataUser) SDataUtilities.readRegistry(miClient, SDataConstants.USRU_USR, new int[] { oDps.getFkUserNewId() }, SLibConstants.EXEC_MODE_SILENT);
                    oUserAuthorize = (SDataUser) SDataUtilities.readRegistry(miClient, SDataConstants.USRU_USR, new int[] { oDps.getFkUserAuthorizedId() }, SLibConstants.EXEC_MODE_SILENT);

                    sUserBuyer = SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.USRU_USR, new int[] { oDps.getFkUserNewId() });
                    sUserAuthorize = SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.USRU_USR, new int[] { oDps.getFkUserAuthorizedId() });

                    keyDpsType = new int[] { oDps.getFkDpsCategoryId(), oDps.getFkDpsClassId(), oDps.getFkDpsTypeId() };

                    if (SLibUtilities.compareKeys(keyDpsType, SDataConstantsSys.TRNU_TP_DPS_PUR_ORD) ||
                        SLibUtilities.compareKeys(keyDpsType, SDataConstantsSys.TRNU_TP_DPS_SAL_ORD)) {
                        // Order:

                        if (SLibUtilities.compareKeys(keyDpsType, SDataConstantsSys.TRNU_TP_DPS_PUR_ORD) && !oDps.getIsAuthorized()) {
                            miClient.showMsgBoxWarning("No se puede imprimir el documento porque:\n-No está autorizado.");
                        }
                        else {
                            try {
                                
                                STrnUtilities.createReportOrder(miClient, null, oDps, SDataConstantsPrint.PRINT_MODE_VIEWER);
                                /* XXX New way of printing the report (jbarajas, 25-09-2015)
                                setCursor(new Cursor(Cursor.WAIT_CURSOR));

                                map = miClient.createReportParams();
                                map.put("nIdYear", oDps.getPkYearId());
                                map.put("nIdDoc", oDps.getPkDocId());
                                map.put("sTitle", SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNU_TP_DPS, new int[] { oDps.getFkDpsCategoryId(),
                                oDps.getFkDpsClassId(), oDps.getFkDpsTypeId() }));
                                map.put("bIsSupplier", mbIsCategoryPur ? true : false);
                                map.put("sAddressLine1", addressOficial[0]);
                                map.put("sAddressLine2", addressOficial[1]);
                                map.put("sAddressLine3", addressOficial[2]);
                                map.put("sAddressLine4", addressOficial[3]);
                                map.put("sAddressDelivery1", mbIsCategoryPur ? addressDeliveryCompany[0] : addressDelivery[0]);
                                map.put("sAddressDelivery2", mbIsCategoryPur ? addressDeliveryCompany[1] : addressDelivery[1]);
                                map.put("sAddressDelivery3", mbIsCategoryPur ? addressDeliveryCompany[2] : addressDelivery[2]);
                                map.put("sAddressDelivery4", mbIsCategoryPur ? addressDeliveryCompany.length > 3 ? addressDeliveryCompany[3] : "" :
                                    addressDelivery.length > 3 ? addressDelivery[3] : "");
                                map.put("sUserBuyer", sUserBuyer != null ? sUserBuyer : oUserBuyer.getUser());
                                map.put("sUserAuthorize", sUserAuthorize != null ? sUserAuthorize : oUserAuthorize.getUser());
                                map.put("nBizPartnerCategory", mbIsCategoryPur ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);
                                map.put("nIdTpCarSup", SModSysConsts.LOGS_TP_CAR_CAR);
                                map.put("sNotes", miClient.getSessionXXX().getParamsCompany().getNotesPurchasesOrder());

                                jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_DPS_ORDER, map);
                                jasperViewer = new JasperViewer(jasperPrint, false);
                                jasperViewer.setTitle("Impresión de pedido");
                                jasperViewer.setVisible(true);
                                */
                            }
                            catch (Exception e) {
                                SLibUtilities.renderException(this, e);
                            }
                            finally {
                                setCursor(cursor);
                            }
                        }
                    }
                    else if (SLibUtilities.compareKeys(keyDpsType, SDataConstantsSys.TRNU_TP_DPS_SAL_INV)) {
                        // Sales document:

                        try {
                            setCursor(new Cursor(Cursor.WAIT_CURSOR));
                            oContact = miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().size() <= 1 ? null :
                                    miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(1);

                            map = miClient.createReportParams();
                            map.put("nIdYear", oDps.getPkYearId());
                            map.put("nIdDoc", oDps.getPkDocId());
                            map.put("sAddressLine1", addressOficial[0]);
                            map.put("sAddressLine2", addressOficial[1]);
                            map.put("sAddressLine3", addressOficial[2]);
                            map.put("sAddressLine4", addressOficial.length > 3 ? addressOficial[3] : "");
                            map.put("sAddressDelivery1", addressDelivery[0].compareTo(addressOficial[0]) == 0 ? "": addressDelivery[0]);
                            map.put("sAddressDelivery2", addressDelivery[1].compareTo(addressOficial[1]) == 0 ? "DOMICILIO DEL CLIENTE": addressDelivery[1]);
                            map.put("sAddressDelivery3", addressDelivery[2].compareTo(addressOficial[2]) == 0 ? "": addressDelivery[2]);
                            map.put("sAddressDelivery4", addressDelivery.length > 3 && addressOficial.length > 3 ?
                                addressDelivery[3].compareTo(addressOficial[3]) == 0 ? "": addressDelivery[3] : "");
                            map.put("nBizPartnerCategory", SDataConstantsSys.BPSS_CT_BP_CUS);
                            map.put("sValueText", SLibUtilities.translateValueToText(oDps.getTotalCy_r(), 2,
                                oDps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                SLibConstants.LAN_ENGLISH, oCurrency.getTextSingular(), oCurrency.getTextPlural(), oCurrency.getTextPrefix(), oCurrency.getTextSuffix()));
                            map.put("sErpCurrencyKey", miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
                            map.put("bShowBackground", false);
                            map.put("sManagerFinance", oContact == null ? "" : SLibUtilities.textTrim(oContact.getContactPrefix() + " " + oContact.getFirstname() + " " + oContact.getLastname()));

                            jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_DPS, map);
                            jasperViewer = new JasperViewer(jasperPrint, false);
                            jasperViewer.setTitle("Impresión de factura");
                            jasperViewer.setVisible(true);
                        }
                        catch (Exception e) {
                            SLibUtilities.renderException(this, e);
                        }
                        finally {
                            setCursor(cursor);
                        }
                    }
                    else if (SLibUtilities.compareKeys(keyDpsType, SDataConstantsSys.TRNU_TP_DPS_SAL_CN)) {
                        // Credit note:

                        try {
                            setCursor(new Cursor(Cursor.WAIT_CURSOR));
                            oContact = miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().size() <= 1 ? null :
                                    miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(1);

                            map = miClient.createReportParams();
                            map.put("nIdYear", oDps.getPkYearId());
                            map.put("nIdDoc", oDps.getPkDocId());
                            map.put("sAddressLine1", addressOficial[0]);
                            map.put("sAddressLine2", addressOficial[1]);
                            map.put("sAddressLine3", addressOficial[2]);
                            map.put("sAddressLine4", addressOficial.length > 3 ? addressOficial[3] : "");
                            map.put("nBizPartnerCategory", SDataConstantsSys.BPSS_CT_BP_CUS);
                            map.put("sValueText", SLibUtilities.translateValueToText(oDps.getTotalCy_r(), 2,
                                oDps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                SLibConstants.LAN_ENGLISH, oCurrency.getTextSingular(), oCurrency.getTextPlural(), oCurrency.getTextPrefix(), oCurrency.getTextSuffix()));
                            map.put("sErpCurrencyKey", miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
                            map.put("bShowBackground", false);
                            map.put("sManagerFinance", oContact == null ? "" : SLibUtilities.textTrim(oContact.getContactPrefix() + " " + oContact.getFirstname() + " " + oContact.getLastname()));

                            jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_DPS_ADJ, map);
                            jasperViewer = new JasperViewer(jasperPrint, false);
                            jasperViewer.setTitle("Impresión de nota de crédito");
                            jasperViewer.setVisible(true);
                        }
                        catch (Exception e) {
                            SLibUtilities.renderException(this, e);
                        }
                        finally {
                            setCursor(cursor);
                        }
                    }
                    else if (SLibUtilities.compareKeys(keyDpsType, SDataConstantsSys.TRNU_TP_DPS_SAL_CON) || SLibUtilities.compareKeys(keyDpsType, SDataConstantsSys.TRNU_TP_DPS_PUR_CON)) {
                        // Sales contract:

                        vPrice = new Vector<>();
                        vUnit = new Vector<>();
                        try {
                            setCursor(new Cursor(Cursor.WAIT_CURSOR));

                            map = miClient.createReportParams();
                            map.put("nIdYear", oDps.getPkYearId());
                            map.put("nIdDoc", oDps.getPkDocId());
                            
                            if (SLibUtilities.compareKeys(keyDpsType, SDataConstantsSys.TRNU_TP_DPS_SAL_CON)) {
                                map.put("sAddressLine1", addressContract[0]);
                                map.put("sAddressLine2", addressContract[1]);
                                map.put("sAddressLine3", addressContract[2]);
                                map.put("sAddressLine4", addressContract.length > 3 ? addressContract[3] : "");
                            }
                            else {
                                addressOficial = miClient.getSessionXXX().getCurrentCompanyBranch().getDbmsBizPartnerBranchAddressOfficial().obtainAddress(nFkRecAddressFormatTypeId_n,
                                    SDataBizPartnerBranchAddress.ADDRESS_4ROWS, bincludeCountry);
                                map.put("sAddressLine1", addressOficial[0] == null || addressOficial[0].isEmpty() ? "": addressOficial[0]);
                                map.put("sAddressLine2", addressOficial[1] == null || addressOficial[1].isEmpty() ? "": addressOficial[1]);
                                map.put("sAddressLine3", addressOficial[2] == null || addressOficial[2].isEmpty() ? "": addressOficial[2]);
                                map.put("sAddressLine4", addressOficial.length > 3 ? addressOficial[3] : "");
                            }

                            vPrice.add("");
                            for (i = 0; i < oDps.getDbmsDpsEntries().size(); i++) {
                                if (!oDps.getDbmsDpsEntries().get(i).getIsDeleted()) {
                                    vPrice.add(SLibUtilities.translateValueToText(oDps.getDbmsDpsEntries().get(i).getOriginalPriceUnitaryCy(), miClient.getSessionXXX().getParamsErp().getDecimalsValue(),
                                    oDps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                    SLibConstants.LAN_ENGLISH, oCurrency.getTextSingular(), oCurrency.getTextPlural(), oCurrency.getTextPrefix(), oCurrency.getTextSuffix()));
                                }
                            }
                            map.put("oVectorTextPrice", vPrice);

                            vUnit.add("");
                            for (i = 0; i < oDps.getDbmsDpsEntries().size(); i++) {
                                if (!oDps.getDbmsDpsEntries().get(i).getIsDeleted()) {
                                    oUnit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT,
                                            new int[] { oDps.getDbmsDpsEntries().get(i).getFkOriginalUnitId() }, SLibConstants.EXEC_MODE_SILENT);

                                    vUnit.add(SLibUtilities.translateUnitsToText(oDps.getDbmsDpsEntries().get(i).getOriginalQuantity(), miClient.getSessionXXX().getParamsErp().getDecimalsQuantity(),
                                    oDps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                    SLibConstants.LAN_ENGLISH, oUnit.getUnit(), oUnit.getUnit()));
                                }
                            }

                            map.put("oVectorTextUnit", vUnit);
                            oContact = oBizPartnerBranch.getDbmsBizPartnerBranchContacts(new int[] { oDps.getFkContactBizPartnerBranchId_n(), oDps.getFkContactContactId_n() });

                            if (oContact == null) {
                                for (i = 0; i < oBizPartnerBranch.getDbmsBizPartnerBranchContacts().size(); i++) {
                                    if (oBizPartnerBranch.getDbmsBizPartnerBranchContacts().get(i).getPkContactTypeId() == SDataConstantsSys.BPSS_TP_CON_PUR && !oBizPartnerBranch.getDbmsBizPartnerBranchContacts().get(i).getIsDeleted()) {
                                        sContact = oBizPartnerBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname() +
                                                (oBizPartnerBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname().length() > 0 ? " " : "") +
                                                oBizPartnerBranch.getDbmsBizPartnerBranchContacts().get(i).getLastname();
                                    }
                                    else if (oBizPartnerBranch.getDbmsBizPartnerBranchContacts().get(i).getPkContactTypeId() == SDataConstantsSys.BPSS_TP_CON_ADM && !oBizPartnerBranch.getDbmsBizPartnerBranchContacts().get(i).getIsDeleted()) {
                                        sContact = oBizPartnerBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname() +
                                                (oBizPartnerBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname().length() > 0 ? " " : "") +
                                                oBizPartnerBranch.getDbmsBizPartnerBranchContacts().get(i).getLastname();
                                    }

                                    if (sContact.length() > 0) {
                                        break;
                                    }
                                }
                            }
                            else {
                                sContact = oContact.getFirstname() + (oContact.getFirstname().length() > 0 ? " " : "") + oContact.getLastname();
                            }

                            map.put("sContact", sContact.length() == 0 ? oBizPartnerBranch.getDbmsBizPartner() : sContact);
                            map.put("ctBpCus", SDataConstantsSys.BPSS_CT_BP_CUS);
                            map.put("ctBpSup", SDataConstantsSys.BPSS_CT_BP_SUP);
                            map.put("nFidCtBp", SLibUtilities.compareKeys(keyDpsType, SDataConstantsSys.TRNU_TP_DPS_SAL_CON) ? SDataConstantsSys.BPSS_CT_BP_CUS : SDataConstantsSys.BPSS_CT_BP_SUP);
                            map.put("oDecimalFormat", miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormatFixed4());
                            map.put("oQuantityFormat", miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat());
                            map.put("oDateTextFormat", miClient.getSessionXXX().getFormatters().getDateTextFormat());
                            map.put("nFidTpCarBp", SModSysConsts.LOGS_TP_CAR_CAR);
                            map.put("nLogIncotermExw", SModSysConsts.LOGS_INC_EXW);
                            map.put("bPrintTon", false);

                            jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_CON, map);
                            jasperViewer = new JasperViewer(jasperPrint, false);
                            jasperViewer.setTitle("Impresión de contrato");
                            jasperViewer.setVisible(true);
                        }
                        catch (Exception e) {
                            SLibUtilities.renderException(this, e);
                        }
                        finally {
                            setCursor(cursor);
                        }
                    }
                }
            }
        }
    }

    private void actionGetXml() {
        if (jbGetXml.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SCfdUtils.getXmlCfd(miClient, SCfdUtils.getCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_INV, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey()));
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionPrintAcknowledgmentCancellation() {
        if (jbPrintAcknowledgmentCancellation.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SCfdUtils.printAcknowledgmentCancellationCfd(miClient, SCfdUtils.getCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_INV, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey()), SLibConstants.UNDEFINED);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionGetAcknowledgmentCancellation() {
        if (jbGetAcknowledgmentCancellation.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SCfdUtils.getAcknowledgmentCancellationCfd(miClient, SCfdUtils.getCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_INV, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey()));
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionSignXml() throws Exception {
        //boolean sign = true; XXX Change to new structure of CFDI generation (jbarajas 2014-05-07)
        SDataDps dps = null;

        if (jbSignXml.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (dps.getIsDeleted()) {
                    miClient.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' está eliminado.");
                }
                else if (dps.getFkDpsStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                    miClient.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' está anulado.");
                }
                else if (!SDataUtilities.isPeriodOpen(miClient, dps.getDate())) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
                }
                else if (dps.getDbmsDataCfd() == null) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento '" + dps.getDpsNumber() + "'.");
                }
                else {
                    switch(dps.getDbmsDataCfd().getFkXmlTypeId()) {
                        case SDataConstantsSys.TRNS_TP_XML_CFD:
                            if (dps.getDbmsDataCfd().getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                                miClient.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' debe estar emitido para volver a timbrarlo.");
                            }
                            else {
                                if (miClient.getSessionXXX().getParamsCompany().getDbmsDataCertificate_n() == null) {
                                    miClient.showMsgBoxWarning("No se ha configurado un certificado de sello digital (CSD).");
                                }
                                else if (miClient.getSessionXXX().getParamsCompany().getDbmsDataCertificate_n().getDate().compareTo(dps.getDate()) > 0) {
                                    miClient.showMsgBoxWarning("La vigencia del certificado de sello digital (CSD) actual es inválida para la fecha del documento '" + dps.getDpsNumber() + "' " +
                                            "(" + miClient.getSessionXXX().getFormatters().getDateFormat().format(dps.getDate()) + "):\n" +
                                            "La vigencia del certificado comienza el " + miClient.getSessionXXX().getFormatters().getDateFormat().format(miClient.getSessionXXX().getParamsCompany().getDbmsDataCertificate_n().getDate()) + ".");
                                }
                                else if (miClient.getSessionXXX().getParamsCompany().getDbmsDataCertificate_n().getExpirationDate().compareTo(dps.getDate()) < 0) {
                                    miClient.showMsgBoxWarning("La vigencia del certificado de sello digital (CSD) actual es inválida para la fecha del documento '" + dps.getDpsNumber() + "' " +
                                            "(" + miClient.getSessionXXX().getFormatters().getDateFormat().format(dps.getDate()) + "):\n" +
                                            "El certificado expiró el " + miClient.getSessionXXX().getFormatters().getDateFormat().format(miClient.getSessionXXX().getParamsCompany().getDbmsDataCertificate_n().getExpirationDate()) + ".");
                                }
                                else {
                                    if (miClient.showMsgBoxConfirm("¿Está seguro que desea volver a timbrar el documento '" + dps.getDpsNumber() + "'?") == JOptionPane.YES_OPTION) {
                                        try {
                                             SCfdUtils.computeCfd(miClient, dps, SDataConstantsSys.TRNS_TP_XML_CFD);
                                             miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                                             miClient.showMsgBoxInformation("El comprobante fiscal ha sido timbrado correctamente.");
                                        }
                                        catch (java.lang.Exception e) {
                                            throw new Exception("Ha ocurrido una excepción al timbrar o imprimir el comprobante fiscal: " + e);
                                        }
                                    }
                                }
                            }
                            break;
                        case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                        case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                            try {
                                if (((SClientInterface) miClient).getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticSal()) {
                                    if (SCfdUtils.signAndSendCfdi(miClient, dps.getDbmsDataCfd(), SLibConstants.UNDEFINED, true, true)) {
                                        miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                                    }
                                }
                                else {
                                    if (SCfdUtils.signCfdi(miClient, dps.getDbmsDataCfd(), SLibConstants.UNDEFINED)) {
                                        miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                                    }
                                }
                            }
                            catch (java.lang.Exception e) {
                                throw new Exception("Ha ocurrido una excepción al timbrar o imprimir el comprobante fiscal:\n" + e);
                            }
                            break;
                        default:
                            miClient.showMsgBoxWarning("No se puede timbrar el documento '" + dps.getDpsNumber() + "' porque su tipo es desconocido.");
                    }
                }
            }
        }
    }

    private void actionVerifyCfdi() throws Exception {
        SDataDps dps = null;

        if (jbVerifyCfdi.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (dps.getIsDeleted()) {
                    miClient.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' está eliminado.");
                }
                else if (dps.getDbmsDataCfd() == null) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento '" + dps.getDpsNumber() + "'.");
                }
                else {
                    if (SCfdUtils.validateCfdi(miClient, dps.getDbmsDataCfd(), SLibConstants.UNDEFINED, true)) {
                        miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                    }
                }
           }
        }

    }

    private void actionSendCfdi() {
        if (jbSendCfdi.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    switch (mnTabTypeAux02) {
                        case SDataConstantsSys.TRNX_TP_DPS_DOC:
                        case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                            SCfdUtils.sendCfd((SClientInterface) miClient, SDataConstantsSys.TRNS_TP_CFD_INV, SCfdUtils.getCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_INV, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey()), SCfdConsts.CFDI_PAYROLL_VER_OLD, true, false);
                            break;
                        case SDataConstantsSys.TRNX_TP_DPS_ORD:
                            STrnUtilities.sendDps((SClientInterface) miClient, mnTabTypeAux01, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey(), true, false);
                            break;
                        default:
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionRestoreSignXml() throws Exception {
        boolean needUpdate = true;
        SDataDps dps = null;

        if (jbRestoreSignXml.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (dps.getIsDeleted()) {
                    miClient.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' está eliminado.");
                }
                else {
                    needUpdate = SCfdUtils.restoreSignXml(miClient, dps.getDbmsDataCfd(), true, SLibConstants.UNDEFINED);

                    if (needUpdate) {
                        miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                    }
                }
           }
        }
    }

    private void actionRestoreAcknowledgmentCancellation() throws Exception {
        boolean needUpdate = true;
        SDataDps dps = null;

        if (jbRestoreAcknowledgmentCancellation.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (dps.getIsDeleted()) {
                    miClient.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' está eliminado.");
                }
                else {
                    needUpdate = SCfdUtils.restoreAcknowledgmentCancellation(miClient, dps.getDbmsDataCfd(), true, SLibConstants.UNDEFINED);

                    if (needUpdate) {
                        miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                    }
                }
           }
        }
    }

    private void actionCfdiDiactivateFlags() throws Exception {
        SDataDps dps = null;

        if (jbDiactivateFlags.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (dps.getIsDeleted()) {
                    miClient.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' está eliminado.");
                }
                else {
                    SCfdUtils.resetCfdiDiactivateFlags(miClient, dps.getDbmsDataCfd());
                    miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
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
            /*
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                where += (where.length() == 0 ? "" : "AND ") + "c.b_del = 0 ";
            }
            else */if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                where += (where.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "c.ts");
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
                "(SELECT b.bp " +
                "FROM erp.bpsu_bp AS b " +
                "WHERE b.id_bp IN (" +
                "SELECT DISTINCT re.fid_bp_nr " +
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
                "WHERE re.fid_cfd_n = c.id_cfd AND NOT r.b_del AND NOT re.b_del)) AS _bp " +
                "FROM trn_cfd AS c " +
                "INNER JOIN erp.bpsu_bpb AS cob ON c.fid_cob_n = cob.id_bpb " +
                (where.isEmpty() ? "" : "WHERE " + where ) +
                "ORDER BY c.ser, c.num, c.ts, c.id_cfd ";
        /*
        if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            msSql += "bpc.bp_key, bp.bp, ";
        }
        else {
            msSql += "bp.bp, bpc.bp_key, ";
        }

        msSql += "bp.id_bp, bpb.bpb, bpb.id_bpb ";
        */
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
                else if (button == jbGetXml) {
                    actionGetXml();
                }
                else if (button == jbPrintAcknowledgmentCancellation) {
                    actionPrintAcknowledgmentCancellation();
                }
                else if (button == jbGetAcknowledgmentCancellation) {
                    actionGetAcknowledgmentCancellation();
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
                else if (button == jbRestoreSignXml) {
                    actionRestoreSignXml();
                }
                else if (button == jbRestoreAcknowledgmentCancellation) {
                    actionRestoreAcknowledgmentCancellation();
                }
                else if (button == jbDiactivateFlags) {
                    actionCfdiDiactivateFlags();
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }
}

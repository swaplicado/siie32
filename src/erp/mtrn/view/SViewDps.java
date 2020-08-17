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
import erp.gui.SModuleUtilities;
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
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mbps.data.SDataBizPartnerBranchContact;
import erp.mcfg.data.SDataCurrency;
import erp.mfin.form.SDialogAccountingMoveDpsBizPartner;
import erp.mitm.data.SDataUnit;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SCfdUtilsHandler;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.STrnUtilities;
import erp.mtrn.data.cfd.SCfdRenderer;
import erp.mtrn.form.SDialogAnnulCfdi;
import erp.mtrn.form.SDialogContractAnalysis;
import erp.mtrn.form.SDialogDpsFinder;
import erp.mtrn.form.SDialogPrintCfdiMasive;
import erp.mtrn.form.SDialogUpdateDpsDeliveryAddress;
import erp.mtrn.form.SDialogUpdateDpsLogistics;
import erp.mtrn.form.SDialogUpdateDpsReferenceComms;
import erp.mtrn.form.SDialogUpdateDpsSalesAgentComms;
import erp.musr.data.SDataUser;
import erp.print.SDataConstantsPrint;
import erp.table.SFilterConstants;
import erp.table.STabFilterDocumentNature;
import erp.table.STabFilterFunctionalArea;
import erp.table.STabFilterUsers;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sa.gui.util.SUtilConsts;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Sergio Flores, Edwin Carmona, Alfredo Pérez, Sergio Flores, Isabel Servín
 *
 * BUSINESS PARTNER BLOCKING NOTES:
 * Business Partner Blocking applies only to order and document for purchases and sales,
 * aswell as printing them.
 * Estimates, contracts and credit notes are independent.
 */
public class SViewDps extends erp.lib.table.STableTab implements java.awt.event.ActionListener {
    
    private javax.swing.JButton jbAnnul;
    private javax.swing.JButton jbImport;
    private javax.swing.JButton jbCopy;
    private javax.swing.JButton jbChangeDeliveryAddress;
    private javax.swing.JButton jbChangeAgentSupervisor;
    private javax.swing.JButton jbSetDeliveryDate;
    private javax.swing.JButton jbSetReferenceCommissions;
    private javax.swing.JButton jbViewNotes;
    private javax.swing.JButton jbViewLinks;
    private javax.swing.JButton jbViewContractAnalysis;
    private javax.swing.JButton jbViewAccountingRecord;
    private javax.swing.JButton jbViewAccountingDetailsDps;
    private javax.swing.JButton jbViewAccountingDetailsBizPartner;
    private javax.swing.JButton jbPrint;
    private javax.swing.JButton jbPrintByRange;
    private javax.swing.JButton jbPrintAcknowledgmentCancellation;
    private javax.swing.JButton jbPrintPhotoInvoice;
    private javax.swing.JButton jbPrintContractKgAsTon;
    private javax.swing.JButton jbPrintContractMoves;
    private javax.swing.JButton jbPrintOrderGoods;
    private javax.swing.JButton jbGetXml;
    private javax.swing.JButton jbGetAcknowledgmentCancellation;
    private javax.swing.JButton jbSignXml;
    private javax.swing.JButton jbValidateCfdi;
    private javax.swing.JButton jbGetCfdiStatus;
    private javax.swing.JButton jbSendCfdi;
    private javax.swing.JButton jbResetPacFlags;
    private javax.swing.JButton jbImportCfdiWithOutPurchaseOrder;
    private javax.swing.JButton jbImportCfdiWithPurchaseOrder;
    private javax.swing.JButton jbRestoreSignXml;
    private javax.swing.JButton jbRestoreAckCancellation;
    private erp.table.STabFilterUsers moTabFilterUser;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.table.STabFilterDocumentNature moTabFilterDocumentNature;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;
    private erp.mtrn.form.SDialogUpdateDpsDeliveryAddress moDialogUpdateDpsDlvryAddrss;
    private erp.mtrn.form.SDialogUpdateDpsSalesAgentComms moDialogUpdateDpsSalesAgentComms;
    private erp.mtrn.form.SDialogUpdateDpsLogistics moDialogUpdateDpsLogistics;
    private erp.mtrn.form.SDialogUpdateDpsReferenceComms moDialogUpdateDpsRefCommissions;
    private erp.mtrn.form.SDialogContractAnalysis moDialogContractAnalysis;
    private erp.mtrn.form.SDialogDpsFinder moDialogDpsFinder;
    private erp.mfin.form.SDialogAccountingMoveDpsBizPartner moDialogAccountingMoveDpsBizPartner;
    private erp.mtrn.form.SDialogAnnulCfdi moDialogAnnulCfdi;

    private boolean mbIsCategoryPur = false;
    private boolean mbIsCategorySal = false;
    private boolean mbIsEstEst = false;
    private boolean mbIsEstCon = false;
    private boolean mbIsOrd = false;
    private boolean mbIsDoc = false;
    private boolean mbIsDocAdj = false;
    private boolean mbHasRightNew = false;
    private boolean mbHasRightEdit = false;
    private boolean mbHasRightAuthor = false;
    private boolean mbHasRightDelete = false;
    private boolean mbHasRightAnnul = false;
    private boolean mbHasRightLogistics = false;
    
    /**
     * View to audit documents.
     * @param client GUI client interface.
     * @param tabTitle View tab title.
     * @param auxType01 Constants defined in SDataConstantsSys (TRNS_CT_DPS...).
     * @param auxType02 Constants defined in SDataConstantsSys (TRNX_TP_DPS...).
     */
    public SViewDps(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRN_DPS, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        int levelDoc = SDataConstantsSys.UNDEFINED;
        int typeImportFinder = SLibConstants.UNDEFINED;
        boolean createImportFinder = false;

        mbIsCategoryPur = mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR;
        mbIsCategorySal = mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL;
        mbIsEstEst = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_EST_EST;
        mbIsEstCon = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_EST_CON;
        mbIsOrd = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ORD;
        mbIsDoc = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_DOC;
        mbIsDocAdj = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ADJ;
        mbHasRightNew = false;
        mbHasRightEdit = false;
        mbHasRightAuthor = false;
        mbHasRightDelete = false;
        mbHasRightAnnul = false;
        mbHasRightLogistics = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_CRED).HasRight;

        if (mbIsCategoryPur) {
            if (mbIsEstEst || mbIsEstCon) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_EST).Level;
            }
            else if (mbIsOrd) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD).Level;
            }
            else if (mbIsDoc) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_TRN).Level;
            }
            else if (mbIsDocAdj) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_TRN_ADJ).Level;
            }

            mbHasRightAnnul =
                    miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_ANNUL).HasRight ||
                    miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_ANNUL_DAY).HasRight;
        }
        else {
            if (mbIsEstEst || mbIsEstCon) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_EST).Level;
            }
            else if (mbIsOrd) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD).Level;
            }
            else if (mbIsDoc) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_TRN).Level;
            }
            else if (mbIsDocAdj) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_TRN_ADJ).Level;
            }

            mbHasRightAnnul =
                    miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_ANNUL).HasRight ||
                    miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_ANNUL_DAY).HasRight;
        }

        mbHasRightNew = levelDoc >= SUtilConsts.LEV_AUTHOR;
        mbHasRightEdit = levelDoc >= SUtilConsts.LEV_AUTHOR;
        mbHasRightAuthor = levelDoc == SUtilConsts.LEV_AUTHOR;
        mbHasRightDelete = levelDoc == SUtilConsts.LEV_MANAGER;

        jbAnnul = new JButton(miClient.getImageIcon(SLibConstants.ICON_ANNUL));
        jbAnnul.setPreferredSize(new Dimension(23, 23));
        jbAnnul.addActionListener(this);
        jbAnnul.setToolTipText("Anular documento");

        jbCopy = new JButton(miClient.getImageIcon(SLibConstants.ICON_COPY));
        jbCopy.setPreferredSize(new Dimension(23, 23));
        jbCopy.addActionListener(this);
        jbCopy.setToolTipText("Copiar documento");
        
        jbImport = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_IMPORT));
        jbImport.setPreferredSize(new Dimension(23, 23));
        jbImport.addActionListener(this);
        jbImport.setToolTipText("Importar documento");
        
        jbImportCfdiWithOutPurchaseOrder = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_IMPORT_CFD));
        jbImportCfdiWithOutPurchaseOrder.setPreferredSize(new Dimension(23, 23));
        jbImportCfdiWithOutPurchaseOrder.addActionListener(this);
        jbImportCfdiWithOutPurchaseOrder.setToolTipText("Importar CFDI sin orden de compra");
        
        jbImportCfdiWithPurchaseOrder = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_IMPORT_CFD_ORD));
        jbImportCfdiWithPurchaseOrder.setPreferredSize(new Dimension(23, 23));
        jbImportCfdiWithPurchaseOrder.addActionListener(this);
        jbImportCfdiWithPurchaseOrder.setToolTipText("Importar CFDI con orden de compra");

        jbChangeDeliveryAddress = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_loc.gif")));
        jbChangeDeliveryAddress.setPreferredSize(new Dimension(23, 23));
        jbChangeDeliveryAddress.addActionListener(this);
        jbChangeDeliveryAddress.setToolTipText("Cambiar domicilio de la operación");

        jbChangeAgentSupervisor = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_bp_pay_cash.gif")));
        jbChangeAgentSupervisor.setPreferredSize(new Dimension(23, 23));
        jbChangeAgentSupervisor.addActionListener(this);
        jbChangeAgentSupervisor.setToolTipText("Cambiar agente/supervisor");

        jbSetDeliveryDate = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_DELIVERY));
        jbSetDeliveryDate.setPreferredSize(new Dimension(23, 23));
        jbSetDeliveryDate.addActionListener(this);
        jbSetDeliveryDate.setToolTipText("Actualizar fechas de entrega del documento");

        jbSetReferenceCommissions = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_money_in.gif")));
        jbSetReferenceCommissions.setPreferredSize(new Dimension(23, 23));
        jbSetReferenceCommissions.addActionListener(this);
        jbSetReferenceCommissions.setToolTipText("Actualizar referencia comisiones");

        jbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));
        jbViewNotes.setPreferredSize(new Dimension(23, 23));
        jbViewNotes.addActionListener(this);
        jbViewNotes.setToolTipText("Ver notas del documento");

        jbViewLinks = new JButton(miClient.getImageIcon(SLibConstants.ICON_LINK));
        jbViewLinks.setPreferredSize(new Dimension(23, 23));
        jbViewLinks.addActionListener(this);
        jbViewLinks.setToolTipText("Ver vínculos del documento");

        jbViewContractAnalysis = new JButton(miClient.getImageIcon(SLibConstants.ICON_CONTRACT_ANALYSIS));
        jbViewContractAnalysis.setPreferredSize(new Dimension(23, 23));
        jbViewContractAnalysis.addActionListener(this);
        jbViewContractAnalysis.setToolTipText("Ver detalles de vínculos");

        jbViewAccountingRecord = new JButton(miClient.getImageIcon(SLibConstants.ICON_QUERY_REC));
        jbViewAccountingRecord.setPreferredSize(new Dimension(23, 23));
        jbViewAccountingRecord.addActionListener(this);
        jbViewAccountingRecord.setToolTipText("Ver póliza contable");

        jbViewAccountingDetailsDps = new JButton(miClient.getImageIcon(SLibConstants.ICON_QUERY_DOC));
        jbViewAccountingDetailsDps.setPreferredSize(new Dimension(23, 23));
        jbViewAccountingDetailsDps.addActionListener(this);
        jbViewAccountingDetailsDps.setToolTipText("Ver movimientos documento");

        jbViewAccountingDetailsBizPartner = new JButton(miClient.getImageIcon(SLibConstants.ICON_QUERY_BP));
        jbViewAccountingDetailsBizPartner.setPreferredSize(new Dimension(23, 23));
        jbViewAccountingDetailsBizPartner.addActionListener(this);
        jbViewAccountingDetailsBizPartner.setToolTipText("Ver movimientos asociado de negocios");

        jbPrint = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));
        jbPrint.setPreferredSize(new Dimension(23, 23));
        jbPrint.addActionListener(this);
        jbPrint.setToolTipText("Imprimir documento");

        jbPrintByRange = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_print_many.gif")));
        jbPrintByRange.setPreferredSize(new Dimension(23, 23));
        jbPrintByRange.addActionListener(this);
        jbPrintByRange.setToolTipText("Imprimir varios documentos");

        jbPrintAcknowledgmentCancellation = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT_ACK_CAN));
        jbPrintAcknowledgmentCancellation.setPreferredSize(new Dimension(23, 23));
        jbPrintAcknowledgmentCancellation.addActionListener(this);
        jbPrintAcknowledgmentCancellation.setToolTipText("Imprimir acuse de cancelación");

        jbPrintPhotoInvoice = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT_PHOTO));
        jbPrintPhotoInvoice.setPreferredSize(new Dimension(23, 23));
        jbPrintPhotoInvoice.addActionListener(this);
        jbPrintPhotoInvoice.setToolTipText("Imprimir foto factura");

        jbPrintContractKgAsTon = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));
        jbPrintContractKgAsTon.setPreferredSize(new Dimension(23, 23));
        jbPrintContractKgAsTon.addActionListener(this);
        jbPrintContractKgAsTon.setToolTipText("Imprimir documento (ton)");

        jbPrintContractMoves = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_print_moves.gif")));
        jbPrintContractMoves.setPreferredSize(new Dimension(23, 23));
        jbPrintContractMoves.addActionListener(this);
        jbPrintContractMoves.setToolTipText("Imprimir movimientos de contrato");

        jbPrintOrderGoods = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT_ORDER));
        jbPrintOrderGoods.setPreferredSize(new Dimension(23, 23));
        jbPrintOrderGoods.addActionListener(this);
        jbPrintOrderGoods.setToolTipText("Imprimir orden de salida de almacén");

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

        jbValidateCfdi = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif")));
        jbValidateCfdi.setPreferredSize(new Dimension(23, 23));
        jbValidateCfdi.addActionListener(this);
        jbValidateCfdi.setToolTipText("Validar timbrado o cancelación del CFDI");

        jbGetCfdiStatus = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif")));
        jbGetCfdiStatus.setPreferredSize(new Dimension(23, 23));
        jbGetCfdiStatus.addActionListener(this);
        jbGetCfdiStatus.setToolTipText("Checar estatus cancelación del CFDI");

        jbSendCfdi = new JButton(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")));
        jbSendCfdi.setPreferredSize(new Dimension(23, 23));
        jbSendCfdi.addActionListener(this);
        jbSendCfdi.setToolTipText("Enviar comprobante vía mail");

        jbRestoreSignXml = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_insert.gif")));
        jbRestoreSignXml.setPreferredSize(new Dimension(23, 23));
        jbRestoreSignXml.addActionListener(this);
        jbRestoreSignXml.setToolTipText("Insertar XML timbrado del CFDI");

        jbRestoreAckCancellation = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_insert_annul.gif")));
        jbRestoreAckCancellation.setPreferredSize(new Dimension(23, 23));
        jbRestoreAckCancellation.addActionListener(this);
        jbRestoreAckCancellation.setToolTipText("Insertar PDF del acuse de cancelación del CFDI");

        jbResetPacFlags = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif")));
        jbResetPacFlags.setPreferredSize(new Dimension(23, 23));
        jbResetPacFlags.addActionListener(this);
        jbResetPacFlags.setToolTipText("Limpiar inconsistencias de timbrado o cancelación del CFDI");
        
        moTabFilterUser = new STabFilterUsers(miClient, this);
        moTabFilterUser.removeButtonUser();
        moTabFilterUser.setUserId(mbHasRightAuthor ? miClient.getSession().getUser().getPkUserId() : SDataConstantsSys.UNDEFINED);

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, mbIsEstCon ? SLibConstants.GUI_DATE_AS_YEAR : SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moTabFilterDocumentNature = new STabFilterDocumentNature(miClient, this, SDataConstants.TRNU_DPS_NAT);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this, SModConsts.CFGU_FUNC);
        moDialogUpdateDpsDlvryAddrss = new SDialogUpdateDpsDeliveryAddress(miClient);
        moDialogUpdateDpsSalesAgentComms = new SDialogUpdateDpsSalesAgentComms(miClient);
        moDialogUpdateDpsLogistics = new SDialogUpdateDpsLogistics(miClient);
        moDialogUpdateDpsRefCommissions = new SDialogUpdateDpsReferenceComms(miClient);
        moDialogContractAnalysis = new SDialogContractAnalysis(miClient);
        moDialogAccountingMoveDpsBizPartner = new SDialogAccountingMoveDpsBizPartner(miClient, mnTabTypeAux01);
        moDialogAnnulCfdi = new SDialogAnnulCfdi(miClient);

        if (mbIsOrd || mbIsDoc || mbIsDocAdj) {
            createImportFinder = true;

            if (mbIsOrd || mbIsDoc) {
                typeImportFinder = SDataConstants.TRNX_DPS_PEND_LINK;
            }
            else {
                typeImportFinder = SDataConstants.TRNX_DPS_PEND_ADJ;
            }
        }

        moDialogDpsFinder = !createImportFinder ? null : new SDialogDpsFinder(miClient, typeImportFinder);

        addTaskBarUpperComponent(jbAnnul);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbCopy);
        addTaskBarUpperComponent(jbImport);
        addTaskBarUpperComponent(jbImportCfdiWithOutPurchaseOrder);
        addTaskBarUpperComponent(jbImportCfdiWithPurchaseOrder);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbChangeDeliveryAddress);
        addTaskBarUpperComponent(jbChangeAgentSupervisor);
        addTaskBarUpperComponent(jbSetDeliveryDate);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbSetReferenceCommissions);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbViewNotes);
        addTaskBarUpperComponent(jbViewLinks);
        addTaskBarUpperComponent(jbViewContractAnalysis);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbViewAccountingRecord);
        addTaskBarUpperComponent(jbViewAccountingDetailsDps);
        addTaskBarUpperComponent(jbViewAccountingDetailsBizPartner);
        addTaskBarLowerComponent(jbPrint);
        addTaskBarLowerComponent(jbPrintByRange);
        addTaskBarLowerComponent(jbPrintAcknowledgmentCancellation);
        addTaskBarLowerComponent(jbPrintPhotoInvoice);
        addTaskBarLowerComponent(jbPrintContractKgAsTon);
        addTaskBarLowerComponent(jbPrintContractMoves);
        addTaskBarLowerComponent(jbPrintOrderGoods);
        addTaskBarLowerComponent(jbGetXml);
        addTaskBarLowerComponent(jbGetAcknowledgmentCancellation);
        addTaskBarLowerComponent(jbSignXml);
        addTaskBarLowerComponent(jbValidateCfdi);
        addTaskBarLowerComponent(jbGetCfdiStatus);
        addTaskBarLowerComponent(jbSendCfdi);
        addTaskBarLowerComponent(jbRestoreSignXml);
        addTaskBarLowerComponent(jbRestoreAckCancellation);
        addTaskBarLowerComponent(jbResetPacFlags);
        addTaskBarLowerSeparator();
        addTaskBarLowerComponent(moTabFilterUser);
        addTaskBarLowerSeparator();
        addTaskBarLowerComponent(moTabFilterDocumentNature);
        addTaskBarLowerComponent(moTabFilterFunctionalArea);

        jbNew.setEnabled(mbHasRightNew);
        jbEdit.setEnabled(true);
        jbDelete.setEnabled(mbHasRightDelete);
        jbAnnul.setEnabled(mbHasRightAnnul && mbHasRightEdit && (mbIsDoc || mbIsDocAdj));
        jbCopy.setEnabled(mbHasRightNew && !mbIsDocAdj);
        jbImport.setEnabled(mbHasRightNew && createImportFinder);
        jbImportCfdiWithOutPurchaseOrder.setEnabled(mbIsCategoryPur && mbIsDoc);
        jbImportCfdiWithPurchaseOrder.setEnabled(mbIsCategoryPur && mbIsDoc);
        jbChangeDeliveryAddress.setEnabled(mbIsCategorySal && mbIsDoc && mbHasRightLogistics);
        jbChangeAgentSupervisor.setEnabled(mbIsCategorySal && mbIsDoc && mbHasRightLogistics);
        jbSetDeliveryDate.setEnabled(mbIsCategorySal && mbIsDoc && mbHasRightLogistics);
        jbSetReferenceCommissions.setEnabled(mbIsCategorySal && mbIsDoc && mbHasRightEdit);
        jbViewNotes.setEnabled(true);
        jbViewLinks.setEnabled(true);
        jbViewContractAnalysis.setEnabled(mbIsEstCon);
        jbViewAccountingRecord.setEnabled(mbIsDoc || mbIsDocAdj);
        jbViewAccountingDetailsDps.setEnabled(mbIsDoc);
        jbViewAccountingDetailsBizPartner.setEnabled(mbIsDoc);
        jbPrint.setEnabled(mbIsOrd || mbIsCategorySal && (mbIsEstEst || mbIsEstCon || mbIsDoc || mbIsDocAdj) || (mbIsCategoryPur && mbIsEstCon));
        jbPrintByRange.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));
        jbPrintAcknowledgmentCancellation.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));
        jbPrintPhotoInvoice.setEnabled(mbIsCategorySal && mbIsDoc);
        jbPrintContractKgAsTon.setEnabled((mbIsCategorySal || mbIsCategoryPur) && mbIsEstCon);
        jbPrintContractMoves.setEnabled(mbIsCategorySal && mbIsEstCon);
        jbPrintOrderGoods.setEnabled(mbIsCategorySal && mbIsOrd);
        jbGetXml.setEnabled((mbIsDoc || mbIsDocAdj));
        jbGetAcknowledgmentCancellation.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));
        jbSignXml.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));
        jbValidateCfdi.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));
        jbGetCfdiStatus.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));
        jbSendCfdi.setEnabled((mbIsCategoryPur && mbIsOrd) || (mbIsCategorySal && (mbIsDoc || mbIsDocAdj)));
        jbRestoreSignXml.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));
        jbRestoreAckCancellation.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));
        jbResetPacFlags.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = null;

        if (mbIsDoc || mbIsDocAdj) {
            aoTableColumns = new STableColumn[48];  // extra columns for accounting record and CFD info
        }
        else {
            aoTableColumns = new STableColumn[42];
        }

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        if (getDpsSortingType() == SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC) {
            if (mbIsCategoryPur) {
                if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave proveedor", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Proveedor", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Proveedor", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave proveedor", 50);
                }
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal proveedor", 75);
            }
            else {
                if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
                }
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal cliente", 75);
            }

            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico", "Estatus", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico_xml", "XML", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
            if (mbIsDoc || mbIsDocAdj) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "x.can_st", "Estatus cancelación", 35);
            }
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo documento", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio documento", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Ref documento", STableConstants.WIDTH_DOC_NUM_REF);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha documento", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt_doc_delivery_n", "Entrega programada", STableConstants.WIDTH_DATE);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo documento", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio documento", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Ref documento", STableConstants.WIDTH_DOC_NUM_REF);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha documento", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt_doc_delivery_n", "Entrega programada", STableConstants.WIDTH_DATE);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico", "Estatus", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico_xml", "CFD", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
            if (mbIsDoc || mbIsDocAdj) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "x.can_st", "Estatus cancelación", 35);
            }

            if (mbIsCategoryPur) {
                if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave proveedor", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Proveedor", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Proveedor", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave proveedor", 50);
                }
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal proveedor", 75);
            }
            else {
                if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
                }
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal cliente", 75);
            }
        }

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.stot_cur_r", "Subtotal mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tax_charged_cur_r", "Impto tras mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tax_retained_cur_r", "Impto ret mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.exc_rate", "T cambio", STableConstants.WIDTH_EXCHANGE_RATE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererExchangeRate());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.stot_r", "Subtotal $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tax_charged_r", "Impto tras $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tax_retained_r", "Impto ret $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_r", "Total $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key_local", "Moneda local", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.comms_ref", "Ref comercial", STableConstants.WIDTH_DOC_NUM_REF);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_dn_code", "Naturaleza documento", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_fa_code", "Área funcional", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_concept", "Concepto documento", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_mfg_ord", "Orden producción", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_copy", "Copia", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_audit", "Auditado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_link", "Vinculado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ul.usr", "Usr. vinculación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_link", "Vinculación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_close", "Cerrado surtido", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uc.usr", "Usr. cierre surtido", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_close", "Cierre surtido", STableConstants.WIDTH_DATE_TIME);

        if (mbIsDoc || mbIsDocAdj) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ord_num", "Folio ped.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rper", "Período póliza", STableConstants.WIDTH_YEAR_PERIOD);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rbc_code", "Centro contable", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rcb_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rnum", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
        }

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        SFormUtilities.createActionMap(this, this, "publicActionPrint", "print", KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);
        setIsSummaryApplying(true);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_AUDIT_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_AUDITED);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    private int getDpsSortingType() {
        int type = SLibConstants.UNDEFINED;

        if (mbIsCategoryPur && (mbIsDoc || mbIsDocAdj)) {
            type = miClient.getSessionXXX().getParamsErp().getFkSortingDpsSupplierTypeId();
        }
        else {
            type = miClient.getSessionXXX().getParamsErp().getFkSortingDpsCustomerTypeId();
        }

        return type;
    }

    private int[] getDpsTypeKey() {
        int[] key = null;

        switch (mnTabTypeAux02) {
            case SDataConstantsSys.TRNX_TP_DPS_EST_EST:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNU_TP_DPS_PUR_EST : SDataConstantsSys.TRNU_TP_DPS_SAL_EST;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_EST_CON:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNU_TP_DPS_PUR_CON : SDataConstantsSys.TRNU_TP_DPS_SAL_CON;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_ORD:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNU_TP_DPS_PUR_ORD : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_DOC:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV : SDataConstantsSys.TRNU_TP_DPS_SAL_INV;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN : SDataConstantsSys.TRNU_TP_DPS_SAL_CN;
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        return key;
    }

    private int[] getDpsClassPreviousKey() {
        int[] key = null;

        switch (mnTabTypeAux02) {
            case SDataConstantsSys.TRNX_TP_DPS_ORD:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNS_CL_DPS_PUR_EST : SDataConstantsSys.TRNS_CL_DPS_SAL_EST;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_DOC:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNS_CL_DPS_PUR_ORD : SDataConstantsSys.TRNS_CL_DPS_SAL_ORD;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNS_CL_DPS_PUR_DOC : SDataConstantsSys.TRNS_CL_DPS_SAL_DOC;
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        return key;
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

            miClient.getGuiModule(gui).setFormComplement(getDpsTypeKey());  // document type key
            if (miClient.getGuiModule(gui).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                SDataUtilities.showDpsRecord(miClient, (SDataDps) miClient.getGuiModule(gui).getRegistry());
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
                int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

                miClient.getGuiModule(gui).setFormComplement(getDpsTypeKey());  // document type key
                miClient.getGuiModule(gui).setCurrentUserPrivilegeLevel(mbHasRightEdit ? SUtilConsts.LEV_AUTHOR : SUtilConsts.LEV_READ);

                if (miClient.getGuiModule(gui).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                    SDataUtilities.showDpsRecord(miClient, (SDataDps) miClient.getGuiModule(gui).getRegistry());
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
                    int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

                    if (miClient.getGuiModule(gui).deleteRegistry(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_DELETE_OK) {
                        miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
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
                    int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module
                    dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                    if (dps.getDbmsDataCfd() != null && dps.getDbmsDataCfd().isCfdi()) {
                        annul = false;
                        params = new SGuiParams();

                        if (dps.getDbmsDataCfd().isStamped()) {
                            moDialogAnnulCfdi.formReset();
                            moDialogAnnulCfdi.formRefreshCatalogues();
                            moDialogAnnulCfdi.setValue(SGuiConsts.PARAM_DATE, dps.getDate());
                            moDialogAnnulCfdi.setValue(SModConsts.TRNS_TP_CFD, SDataConstantsSys.TRNS_TP_CFD_INV);
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
                        if (miClient.getGuiModule(gui).annulRegistry(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey(), params) == SLibConstants.DB_ACTION_ANNUL_OK) {
                            miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                        }
                    }
                }
            }
        }
    }

    private void actionImport() {
        if (jbImport.isEnabled()) {
            moDialogDpsFinder.formReset();
            moDialogDpsFinder.setValue(SLibConstants.VALUE_FILTER_KEY, getDpsClassPreviousKey());
            moDialogDpsFinder.setVisible(true);

            if (moDialogDpsFinder.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module
                int[] adjustmentSubtypeKey = (int[]) moDialogDpsFinder.getValue(SDataConstants.TRNS_STP_DPS_ADJ);
                Object complement = null;

                if (adjustmentSubtypeKey == null) {
                    complement = new Object[] { getDpsTypeKey(), moDialogDpsFinder.getValue(SDataConstants.TRN_DPS), null, moDialogDpsFinder.getValue(SLibConstants.VALUE_CURRENCY_LOCAL) };
                }
                else {
                    complement = new Object[] { getDpsTypeKey(), moDialogDpsFinder.getValue(SDataConstants.TRN_DPS), adjustmentSubtypeKey, moDialogDpsFinder.getValue(SLibConstants.VALUE_CURRENCY_LOCAL) };
                }
                
                miClient.getGuiModule(gui).setFormComplement(complement);   // document type key, reference document and adjustment type (optional)
                if (miClient.getGuiModule(gui).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                    SDataUtilities.showDpsRecord(miClient, (SDataDps) miClient.getGuiModule(gui).getRegistry());
                }
            }
        }
    }

    private void actionCopy() {
        if (jbCopy.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

                miClient.getGuiModule(gui).setFormComplement(getDpsTypeKey());  // document type key
                if (miClient.getGuiModule(gui).showFormForCopy(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                    SDataUtilities.showDpsRecord(miClient, (SDataDps) miClient.getGuiModule(gui).getRegistry());
                }
            }
        }
    }
    
    private void actionImportCfdi(boolean withPurchaseOrder) {
        if (withPurchaseOrder ? jbImportCfdiWithPurchaseOrder.isEnabled() :
                jbImportCfdiWithOutPurchaseOrder.isEnabled()) {
            SDataDps purchaseOrderDps = null; 
            FileFilter filter = new FileNameExtensionFilter("XML file", "xml");
            miClient.getFileChooser().repaint();
            miClient.getFileChooser().setAcceptAllFileFilterUsed(false);
            miClient.getFileChooser().setFileFilter(filter);

            if (withPurchaseOrder) {
                moDialogDpsFinder.formReset();
                moDialogDpsFinder.setValue(SLibConstants.VALUE_FILTER_KEY, getDpsClassPreviousKey());
                moDialogDpsFinder.setVisible(true);

                if (moDialogDpsFinder.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    purchaseOrderDps = (SDataDps) moDialogDpsFinder.getValue(SDataConstants.TRN_DPS);
                }
            }

            try {
                if (!withPurchaseOrder  || (withPurchaseOrder && purchaseOrderDps != null)) {
                    if (miClient.getFileChooser().showOpenDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION ) {
                        if (miClient.getFileChooser().getSelectedFile().getName().toLowerCase().contains(".xml")) {
                            SCfdRenderer renderer = new SCfdRenderer(miClient);
                            SDataDps dpsRendered = renderer.renderCfdi(miClient.getFileChooser().getSelectedFile(), purchaseOrderDps, mbIsCategoryPur ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);
                            if (dpsRendered != null){
                                int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

                                miClient.getGuiModule(gui).setFormComplement(getDpsTypeKey());  // document type key
                                miClient.getGuiModule(gui).setAuxRegistry(dpsRendered);
                                if (miClient.getGuiModule(gui).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                                    miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                                    SDataUtilities.showDpsRecord(miClient, (SDataDps) miClient.getGuiModule(gui).getRegistry());
                                }
                            }
                        }
                        else {
                            miClient.showMsgBoxInformation("El archivo solo puede ser XML.");
                        }
                    }
                    miClient.getFileChooser().resetChoosableFileFilters();
                }
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
    }
    
    private void actionChangeDeliveryAddress() {
        if (jbChangeDeliveryAddress.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

                moDialogUpdateDpsDlvryAddrss.formReset();
                moDialogUpdateDpsDlvryAddrss.setValue(SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey());
                moDialogUpdateDpsDlvryAddrss.setFormVisible(true);

                if (moDialogUpdateDpsDlvryAddrss.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                }
            }
        }
    }
            
    private void actionChangeAgentSupervisor() {
        if (jbChangeAgentSupervisor.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

                moDialogUpdateDpsSalesAgentComms.formReset();
                moDialogUpdateDpsSalesAgentComms.setValue(SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey());
                moDialogUpdateDpsSalesAgentComms.setFormVisible(true);

                if (moDialogUpdateDpsSalesAgentComms.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    private void actionSetDeliveryDate() {
        if (jbSetDeliveryDate.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

                moDialogUpdateDpsLogistics.formReset();
                moDialogUpdateDpsLogistics.setValue(SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey());
                moDialogUpdateDpsLogistics.setFormVisible(true);

                if (moDialogUpdateDpsLogistics.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                }
            }
        }
    }
    
    private void actionSetReferenceCommissions() {
        if (jbSetReferenceCommissions.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

                moDialogUpdateDpsRefCommissions.formReset();
                moDialogUpdateDpsRefCommissions.setValue(SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey());
                moDialogUpdateDpsRefCommissions.setFormVisible(true);

                if (moDialogUpdateDpsRefCommissions.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    private void actionViewNotes() {
        if (jbViewNotes.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                SModuleUtilities.showDocumentNotes(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow());
            }
        }
    }

    private void actionViewLinks() {
        if (jbViewLinks.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                SModuleUtilities.showDocumentLinks(miClient, moTablePane.getSelectedTableRow());
            }
        }
    }

    private void actionViewContractAnalysis() {
        if (jbViewContractAnalysis.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);

                moDialogContractAnalysis.formReset();
                moDialogContractAnalysis.setValue(SDataConstants.TRN_DPS, dps);
                moDialogContractAnalysis.setFormVisible(true);
            }
        }
    }

    private void actionViewAccountingRecord() {
        if (jbViewAccountingRecord.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                SDataDps oDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                if (oDps.getDbmsRecordKey() == null) {
                    miClient.showMsgBoxInformation("El documento no tiene póliza contable.");
                }
                else {
                    miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(SDataConstants.FINX_REC_RO, oDps.getDbmsRecordKey());
                }
            }
        }
    }

    private void actionViewAccountingDetailsDps() {
        if (jbViewAccountingDetailsDps.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                SDataDps oDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                SDataBizPartner oBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { oDps.getFkBizPartnerId_r() }, SLibConstants.EXEC_MODE_SILENT);

                moDialogAccountingMoveDpsBizPartner.refreshAccountingDetail();
                moDialogAccountingMoveDpsBizPartner.setParamBizPartnerName(oBizPartner.getBizPartner());
                moDialogAccountingMoveDpsBizPartner.setParamDpsNumber(oDps.getNumberSeries() + (oDps.getNumberSeries().length() == 0 ? "" : "-") + oDps.getNumber());
                moDialogAccountingMoveDpsBizPartner.setParamDpsKey(moTablePane.getSelectedTableRow().getPrimaryKey());
                moDialogAccountingMoveDpsBizPartner.setParamBizPartnerKey(null);
                moDialogAccountingMoveDpsBizPartner.showAccountingDetail(miClient.getSessionXXX().getWorkingYear(),
                        miClient.getSessionXXX().getWorkingDate());
            }
        }
    }

    private void actionViewAccountingDetailsBizPartner() {
        if (jbViewAccountingDetailsDps.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                SDataDps oDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                SDataBizPartner oBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { oDps.getFkBizPartnerId_r() }, SLibConstants.EXEC_MODE_SILENT);

                moDialogAccountingMoveDpsBizPartner.refreshAccountingDetail();
                moDialogAccountingMoveDpsBizPartner.setParamBizPartnerName(oBizPartner.getBizPartner());
                moDialogAccountingMoveDpsBizPartner.setParamDpsNumber("");
                moDialogAccountingMoveDpsBizPartner.setParamDpsKey(null);
                moDialogAccountingMoveDpsBizPartner.setParamBizPartnerKey(new int[] { oDps.getFkBizPartnerId_r() });
                moDialogAccountingMoveDpsBizPartner.showAccountingDetail(miClient.getSessionXXX().getWorkingYear(),
                        miClient.getSessionXXX().getWorkingDate());
            }
        }
    }

    private void actionPrintByRange() throws Exception {
        new SDialogPrintCfdiMasive(miClient, getDpsTypeKey()).setVisible(true);
    }
    
    private void actionPrint(final boolean contractKgAsTon) {
        if ((!contractKgAsTon && jbPrint.isEnabled()) || (contractKgAsTon && jbPrintContractKgAsTon.isEnabled())) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (dps.getDbmsDataCfd() != null) {
                    try {
                        SCfdUtils.printCfd(miClient, dps.getDbmsDataCfd(), SLibConstants.UNDEFINED, SDataConstantsPrint.PRINT_MODE_VIEWER, 1, false);
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                }
                else {
                    SDataBizPartnerBranch comBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { dps.getFkCompanyBranchId() }, SLibConstants.EXEC_MODE_SILENT);
                    SDataBizPartnerBranch bpBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { dps.getFkBizPartnerBranchId() }, SLibConstants.EXEC_MODE_SILENT);
                    SDataBizPartnerBranchAddress bpBranchAddress = (SDataBizPartnerBranchAddress) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB_ADD, new int [] { dps.getFkBizPartnerBranchId(), dps.getFkBizPartnerBranchAddressId() }, SLibConstants.EXEC_MODE_SILENT);
                    SDataBizPartnerBranchContact bpBranchContact = null;
                    SDataCurrency currency = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR, new int[] { dps.getFkCurrencyId() }, SLibConstants.EXEC_MODE_SILENT);

                    int addressFormatType = 0;
                    boolean addCountry = false;
                    
                    if (bpBranch.getFkAddressFormatTypeId_n() != SLibConstants.UNDEFINED) {
                        addressFormatType = bpBranch.getFkAddressFormatTypeId_n();
                    }
                    else {
                        addressFormatType = miClient.getSessionXXX().getParamsCompany().getFkDefaultAddressFormatTypeId_n();
                    }
                    
                    if (bpBranchAddress.getFkCountryId_n() == comBranch.getDbmsBizPartnerBranchAddressOfficial().getFkCountryId_n()) {
                        addCountry = false;
                    }
                    else {
                        addCountry = true;
                    }

                    String[] hqAddressTexts = bpBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_4ROWS, addCountry);
                    String[] dvyAddressTexts = bpBranchAddress.obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_4ROWS, addCountry);

                    Cursor cursor = getCursor();
                    Map<String, Object> map = null;
                    JasperPrint jasperPrint = null;
                    JasperViewer jasperViewer = null;
                    
                    if (SLibUtilities.belongsTo(dps.getDpsTypeKey(), new int[][] { SDataConstantsSys.TRNU_TP_DPS_SAL_ORD, SDataConstantsSys.TRNU_TP_DPS_PUR_ORD })) {
                        // order:

                        if (SLibUtilities.compareKeys(dps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_PUR_ORD) && !dps.getIsAuthorized()) {
                            miClient.showMsgBoxWarning("No se puede imprimir el documento porque:\n-No está autorizado.");
                        }
                        else {
                            try {
                                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                                
                                STrnUtilities.createReportOrder(miClient, null, dps, SDataConstantsPrint.PRINT_MODE_VIEWER);
                            }
                            catch (Exception e) {
                                SLibUtilities.renderException(this, e);
                            }
                            finally {
                                setCursor(cursor);
                            }
                        }
                    }
                    else if (SLibUtilities.compareKeys(dps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_INV)) {
                        // sales invoice:

                        try {
                            setCursor(new Cursor(Cursor.WAIT_CURSOR));
                            
                            bpBranchContact = miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().size() <= 1 ? null :
                                    miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(1);

                            map = miClient.createReportParams();
                            map.put("nIdYear", dps.getPkYearId());
                            map.put("nIdDoc", dps.getPkDocId());
                            map.put("sAddressLine1", hqAddressTexts[0]);
                            map.put("sAddressLine2", hqAddressTexts[1]);
                            map.put("sAddressLine3", hqAddressTexts[2]);
                            map.put("sAddressLine4", hqAddressTexts.length > 3 ? hqAddressTexts[3] : "");
                            map.put("sAddressDelivery1", dvyAddressTexts[0].compareTo(hqAddressTexts[0]) == 0 ? "": dvyAddressTexts[0]);
                            map.put("sAddressDelivery2", dvyAddressTexts[1].compareTo(hqAddressTexts[1]) == 0 ? "DOMICILIO DEL CLIENTE": dvyAddressTexts[1]);
                            map.put("sAddressDelivery3", dvyAddressTexts[2].compareTo(hqAddressTexts[2]) == 0 ? "": dvyAddressTexts[2]);
                            map.put("sAddressDelivery4", dvyAddressTexts.length > 3 && hqAddressTexts.length > 3 ?
                                dvyAddressTexts[3].compareTo(hqAddressTexts[3]) == 0 ? "" : dvyAddressTexts[3] : "");
                            map.put("nBizPartnerCategory", SDataConstantsSys.BPSS_CT_BP_CUS);
                            map.put("sValueText", SLibUtilities.translateValueToText(dps.getTotalCy_r(), 2,
                                dps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                SLibConstants.LAN_ENGLISH, currency.getTextSingular(), currency.getTextPlural(), currency.getTextPrefix(), currency.getTextSuffix()));
                            map.put("sErpCurrencyKey", miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
                            map.put("bShowBackground", false);
                            map.put("sManagerFinance", bpBranchContact == null ? "" : SLibUtilities.textTrim(bpBranchContact.getContactPrefix() + " " + bpBranchContact.getFirstname() + " " + bpBranchContact.getLastname()));

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
                    else if (SLibUtilities.compareKeys(dps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_CN)) {
                        // sales credit note:

                        try {
                            setCursor(new Cursor(Cursor.WAIT_CURSOR));
                            bpBranchContact = miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().size() <= 1 ? null :
                                    miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(1);

                            map = miClient.createReportParams();
                            map.put("nIdYear", dps.getPkYearId());
                            map.put("nIdDoc", dps.getPkDocId());
                            map.put("sAddressLine1", hqAddressTexts[0]);
                            map.put("sAddressLine2", hqAddressTexts[1]);
                            map.put("sAddressLine3", hqAddressTexts[2]);
                            map.put("sAddressLine4", hqAddressTexts.length > 3 ? hqAddressTexts[3] : "");
                            map.put("nBizPartnerCategory", SDataConstantsSys.BPSS_CT_BP_CUS);
                            map.put("sValueText", SLibUtilities.translateValueToText(dps.getTotalCy_r(), 2,
                                dps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                SLibConstants.LAN_ENGLISH, currency.getTextSingular(), currency.getTextPlural(), currency.getTextPrefix(), currency.getTextSuffix()));
                            map.put("sErpCurrencyKey", miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
                            map.put("bShowBackground", false);
                            map.put("sManagerFinance", bpBranchContact == null ? "" : SLibUtilities.textTrim(bpBranchContact.getContactPrefix() + " " + bpBranchContact.getFirstname() + " " + bpBranchContact.getLastname()));

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
                    else if (SLibUtilities.belongsTo(dps.getDpsTypeKey(), new int[][] { SDataConstantsSys.TRNU_TP_DPS_SAL_CON, SDataConstantsSys.TRNU_TP_DPS_PUR_CON })) {
                        // contract:

                        String textContact = "";
                        Vector<String> textsPrices = new Vector<>();
                        Vector<String> textsSalesPrices = new Vector<>();
                        Vector<String> textsSalesFreights = new Vector<>();
                        Vector<String> textsUnits = new Vector<>();
                        
                        try {
                            setCursor(new Cursor(Cursor.WAIT_CURSOR));

                            map = miClient.createReportParams();
                            map.put("nIdYear", dps.getPkYearId());
                            map.put("nIdDoc", dps.getPkDocId());
                            
                            if (SLibUtilities.compareKeys(dps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_CON)) {
                                map.put("sAddressLine1", hqAddressTexts[0]);
                                map.put("sAddressLine2", hqAddressTexts[1]);
                                map.put("sAddressLine3", hqAddressTexts[2]);
                                map.put("sAddressLine4", hqAddressTexts.length > 3 ? hqAddressTexts[3] : "");
                            }
                            else {
                                String[] comAddressTexts = miClient.getSessionXXX().getCurrentCompanyBranch().getDbmsBizPartnerBranchAddressOfficial().obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_4ROWS, addCountry);
                                map.put("sAddressLine1", comAddressTexts[0] == null || comAddressTexts[0].isEmpty() ? "": comAddressTexts[0]);
                                map.put("sAddressLine2", comAddressTexts[1] == null || comAddressTexts[1].isEmpty() ? "": comAddressTexts[1]);
                                map.put("sAddressLine3", comAddressTexts[2] == null || comAddressTexts[2].isEmpty() ? "": comAddressTexts[2]);
                                map.put("sAddressLine4", comAddressTexts.length > 3 ? comAddressTexts[3] : "");
                            }

                            textsPrices.add("");
                            textsSalesPrices.add("");
                            textsSalesFreights.add("");
                            
                            for (SDataDpsEntry entry : dps.getDbmsDpsEntries()) {
                                if (!entry.getIsDeleted()) {
                                    double price = entry.getOriginalPriceUnitaryCy();
                                    double salesPrice = entry.getSalesPriceUnitaryCy();
                                    double salesFreight = entry.getSalesFreightUnitaryCy();
                                    
                                    if (contractKgAsTon && entry.getFkOriginalUnitId() == SModSysConsts.ITMU_UNIT_KG) {
                                        price *= 1000.0;
                                        salesPrice *= 1000.0;
                                        salesFreight *= 1000.0;
                                    }
                                    
                                    textsPrices.add(SLibUtilities.translateValueToText(price, miClient.getSessionXXX().getParamsErp().getDecimalsValue(),
                                    dps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                    SLibConstants.LAN_ENGLISH, currency.getTextSingular(), currency.getTextPlural(), currency.getTextPrefix(), currency.getTextSuffix()));
                                    
                                    textsSalesPrices.add(SLibUtilities.translateValueToText(salesPrice, miClient.getSessionXXX().getParamsErp().getDecimalsValue(),
                                    dps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                    SLibConstants.LAN_ENGLISH, currency.getTextSingular(), currency.getTextPlural(), currency.getTextPrefix(), currency.getTextSuffix()));

                                    textsSalesFreights.add(SLibUtilities.translateValueToText(salesFreight, miClient.getSessionXXX().getParamsErp().getDecimalsValue(),
                                    dps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                    SLibConstants.LAN_ENGLISH, currency.getTextSingular(), currency.getTextPlural(), currency.getTextPrefix(), currency.getTextSuffix()));
                                }
                            }
                            
                            map.put("oVectorTextPrice", textsPrices);
                            map.put("oVectorTextSalesPrice", textsSalesPrices);
                            map.put("oVectorTextSalesFreight", textsSalesFreights);

                            textsUnits.add("");
                            
                            for (SDataDpsEntry entry : dps.getDbmsDpsEntries()) {
                                if (!entry.getIsDeleted()) {
                                    double quantity = 0;
                                    SDataUnit unit = null;
                                    
                                    if (contractKgAsTon && entry.getFkOriginalUnitId() == SModSysConsts.ITMU_UNIT_KG) {
                                        quantity = entry.getOriginalQuantity() / 1000.0;
                                        unit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { SModSysConsts.ITMU_UNIT_MT_TON }, SLibConstants.EXEC_MODE_SILENT);
                                    }
                                    else {
                                        quantity = entry.getOriginalQuantity();
                                        unit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { entry.getFkOriginalUnitId() }, SLibConstants.EXEC_MODE_SILENT);
                                    }
                                    
                                    textsUnits.add(SLibUtilities.translateUnitsToText(quantity, miClient.getSessionXXX().getParamsErp().getDecimalsQuantity(),
                                    dps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH : SLibConstants.LAN_ENGLISH, 
                                    unit.getUnit(), unit.getUnit()));
                                }
                            }
                            
                            map.put("oVectorTextUnit", textsUnits);
                            
                            bpBranchContact = bpBranch.getDbmsBizPartnerBranchContact(new int[] { dps.getFkContactBizPartnerBranchId_n(), dps.getFkContactContactId_n() });
                            if (bpBranchContact == null) {
                                for (int i = 0; i < bpBranch.getDbmsBizPartnerBranchContacts().size(); i++) {
                                    if (bpBranch.getDbmsBizPartnerBranchContacts().get(i).getFkContactTypeId() == SDataConstantsSys.BPSS_TP_CON_PUR && !bpBranch.getDbmsBizPartnerBranchContacts().get(i).getIsDeleted()) {
                                        textContact = bpBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname() +
                                                (bpBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname().length() > 0 ? " " : "") +
                                                bpBranch.getDbmsBizPartnerBranchContacts().get(i).getLastname();
                                    }
                                    else if (bpBranch.getDbmsBizPartnerBranchContacts().get(i).getFkContactTypeId() == SDataConstantsSys.BPSS_TP_CON_ADM && !bpBranch.getDbmsBizPartnerBranchContacts().get(i).getIsDeleted()) {
                                        textContact = bpBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname() +
                                                (bpBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname().length() > 0 ? " " : "") +
                                                bpBranch.getDbmsBizPartnerBranchContacts().get(i).getLastname();
                                    }

                                    if (!textContact.isEmpty()) {
                                        break;
                                    }
                                }
                            }
                            else {
                                textContact = bpBranchContact.getFirstname() + (bpBranchContact.getFirstname().length() > 0 ? " " : "") + bpBranchContact.getLastname();
                            }

                            map.put("sContact", textContact.isEmpty() ? bpBranch.getDbmsBizPartner() : textContact);
                            map.put("ctBpCus", SDataConstantsSys.BPSS_CT_BP_CUS);
                            map.put("ctBpSup", SDataConstantsSys.BPSS_CT_BP_SUP);
                            map.put("nFidCtBp", SLibUtilities.compareKeys(dps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_CON) ? SDataConstantsSys.BPSS_CT_BP_CUS : SDataConstantsSys.BPSS_CT_BP_SUP);
                            map.put("oDecimalFormat", miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormatFixed4());
                            map.put("oQuantityFormat", miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat());
                            map.put("oDateTextFormat", miClient.getSessionXXX().getFormatters().getDateTextFormat());
                            map.put("nFidTpCarBp", SModSysConsts.LOGS_TP_CAR_CAR);
                            map.put("nLogIncotermExw", SModSysConsts.LOGS_INC_EXW);
                            map.put("bPrintTon", contractKgAsTon);

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

    /* XXX 2020-06-12 Sergio Flores: Remove if no longer needed. Preserved only if new code fails.
    private void actionPrintContract() {
        if (jbPrintContractKgAsTon.isEnabled()) {
            int i;
            int nFkRecAddressFormatTypeId_n = 0;
            boolean bincludeCountry = false;
            double dQty = 0;
            String[] addressOficial = null;
            String[] addressContract = null;
            String[] addressDelivery = null;
            Cursor cursor = getCursor();
            Map<String, Object> map = null;
            JasperPrint jasperPrint = null;
            JasperViewer jasperViewer = null;
            SDataDps oDps = null;
            SDataBizPartnerBranch oCompanyBranch = null;
            SDataBizPartnerBranch oBizPartnerBranch = null;
            SDataBizPartnerBranchContact oContact = null;
            SDataBizPartnerBranchAddress oAddress = null;
            SDataCurrency oCurrency = null;
            SDataUnit oUnit = null;
            Vector<String> vPrice = null;
            Vector<String> vUnit = null;
            String sContact = "";

            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                oDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                
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

                if (SLibUtilities.belongsTo(oDps.getDpsTypeKey(), new int[][] { SDataConstantsSys.TRNU_TP_DPS_SAL_CON, SDataConstantsSys.TRNU_TP_DPS_PUR_CON })) {
                    // contract:

                    vPrice = new Vector<>();
                    vUnit = new Vector<>();
                    
                    try {
                        setCursor(new Cursor(Cursor.WAIT_CURSOR));

                        map = miClient.createReportParams();
                        map.put("nIdYear", oDps.getPkYearId());
                        map.put("nIdDoc", oDps.getPkDocId());
                        
                        if (SLibUtilities.compareKeys(oDps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_CON)) {
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
                                if (oDps.getDbmsDpsEntries().get(i).getFkOriginalUnitId() == SModSysConsts.ITMU_UNIT_KG) {
                                    vPrice.add(SLibUtilities.translateValueToText(oDps.getDbmsDpsEntries().get(i).getOriginalPriceUnitaryCy() * 1000, miClient.getSessionXXX().getParamsErp().getDecimalsValue(),
                                    oDps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                    SLibConstants.LAN_ENGLISH, oCurrency.getTextSingular(), oCurrency.getTextPlural(), oCurrency.getTextPrefix(), oCurrency.getTextSuffix()));
                                }
                                else {
                                    vPrice.add(SLibUtilities.translateValueToText(oDps.getDbmsDpsEntries().get(i).getOriginalPriceUnitaryCy(), miClient.getSessionXXX().getParamsErp().getDecimalsValue(),
                                    oDps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                    SLibConstants.LAN_ENGLISH, oCurrency.getTextSingular(), oCurrency.getTextPlural(), oCurrency.getTextPrefix(), oCurrency.getTextSuffix()));
                                }
                            }
                        }
                        
                        map.put("oVectorTextPrice", vPrice);

                        vUnit.add("");
                        for (i = 0; i < oDps.getDbmsDpsEntries().size(); i++) {
                            if (!oDps.getDbmsDpsEntries().get(i).getIsDeleted()) {
                                if (oDps.getDbmsDpsEntries().get(i).getFkOriginalUnitId() == SModSysConsts.ITMU_UNIT_KG) {
                                    dQty = oDps.getDbmsDpsEntries().get(i).getOriginalQuantity() / 1000;
                                    oUnit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT,
                                        new int[] { SModSysConsts.ITMU_UNIT_MT_TON }, SLibConstants.EXEC_MODE_SILENT);
                                }
                                else {
                                    dQty = oDps.getDbmsDpsEntries().get(i).getOriginalQuantity();
                                    oUnit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT,
                                        new int[] { oDps.getDbmsDpsEntries().get(i).getFkOriginalUnitId() }, SLibConstants.EXEC_MODE_SILENT);
                                }

                                vUnit.add(SLibUtilities.translateUnitsToText(dQty, miClient.getSessionXXX().getParamsErp().getDecimalsQuantity(),
                                oDps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                SLibConstants.LAN_ENGLISH, oUnit.getUnit(), oUnit.getUnit()));
                            }
                        }

                        map.put("oVectorTextUnit", vUnit);
                        oContact = oBizPartnerBranch.getDbmsBizPartnerBranchContact(new int[] { oDps.getFkContactBizPartnerBranchId_n(), oDps.getFkContactContactId_n() });

                        if (oContact == null) {
                            for (i = 0; i < oBizPartnerBranch.getDbmsBizPartnerBranchContact().size(); i++) {
                                if (oBizPartnerBranch.getDbmsBizPartnerBranchContact().get(i).getFkContactTypeId() == SDataConstantsSys.BPSS_TP_CON_PUR && !oBizPartnerBranch.getDbmsBizPartnerBranchContact().get(i).getIsDeleted()) {
                                    sContact = oBizPartnerBranch.getDbmsBizPartnerBranchContact().get(i).getFirstname() +
                                            (oBizPartnerBranch.getDbmsBizPartnerBranchContact().get(i).getFirstname().length() > 0 ? " " : "") +
                                            oBizPartnerBranch.getDbmsBizPartnerBranchContact().get(i).getLastname();
                                }
                                else if (oBizPartnerBranch.getDbmsBizPartnerBranchContact().get(i).getFkContactTypeId() == SDataConstantsSys.BPSS_TP_CON_ADM && !oBizPartnerBranch.getDbmsBizPartnerBranchContact().get(i).getIsDeleted()) {
                                    sContact = oBizPartnerBranch.getDbmsBizPartnerBranchContact().get(i).getFirstname() +
                                            (oBizPartnerBranch.getDbmsBizPartnerBranchContact().get(i).getFirstname().length() > 0 ? " " : "") +
                                            oBizPartnerBranch.getDbmsBizPartnerBranchContact().get(i).getLastname();
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
                        map.put("nFidCtBp", SLibUtilities.compareKeys(oDps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_CON) ? SDataConstantsSys.BPSS_CT_BP_CUS : SDataConstantsSys.BPSS_CT_BP_SUP);
                        map.put("oDecimalFormat", miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormatFixed4());
                        map.put("oQuantityFormat", miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat());
                        map.put("oDateTextFormat", miClient.getSessionXXX().getFormatters().getDateTextFormat());
                        map.put("nFidTpCarBp", SModSysConsts.LOGS_TP_CAR_CAR);
                        map.put("nLogIncotermExw", SModSysConsts.LOGS_INC_EXW);
                        map.put("bPrintTon", true);

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
    */

    private void actionPrintContractMoves() {
        if (jbPrintContractMoves.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                STrnUtilities.createReportContractAnalysis(miClient, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
            }
        }
    }

    private void actionPrintOrderGoods() {
        if (jbPrintOrderGoods.isEnabled()) {
            int i;
            String sUserBuyer = "";
            String sUserAuthorize = "";
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
            SDataUser oUserBuyer = null;
            SDataUser oUserAuthorize = null;

            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                oDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                oCompanyBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { oDps.getFkCompanyBranchId() }, SLibConstants.EXEC_MODE_SILENT);
                oBizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { oDps.getFkBizPartnerBranchId() }, SLibConstants.EXEC_MODE_SILENT);
                oAddress = (SDataBizPartnerBranchAddress) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB_ADD, new int [] { oDps.getFkBizPartnerBranchId(),
                    oDps.getFkBizPartnerBranchAddressId() }, SLibConstants.EXEC_MODE_SILENT);

                addressOficial = oBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(miClient.getSessionXXX().getParamsErp().getFkLanguageId(),
                    SDataBizPartnerBranchAddress.ADDRESS_4ROWS, false);
                addressContract = oBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(miClient.getSessionXXX().getParamsErp().getFkLanguageId(),
                    SDataBizPartnerBranchAddress.ADDRESS_4ROWS, false);
                addressDelivery = oAddress.obtainAddress(miClient.getSessionXXX().getParamsErp().getFkLanguageId(),
                    SDataBizPartnerBranchAddress.ADDRESS_4ROWS, false);
                addressDeliveryCompany = oCompanyBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(miClient.getSessionXXX().getParamsErp().getFkLanguageId(),
                    SDataBizPartnerBranchAddress.ADDRESS_4ROWS, false);
                oUserBuyer = (SDataUser) SDataUtilities.readRegistry(miClient, SDataConstants.USRU_USR, new int[] { oDps.getFkUserNewId() }, SLibConstants.EXEC_MODE_SILENT);
                oUserAuthorize = (SDataUser) SDataUtilities.readRegistry(miClient, SDataConstants.USRU_USR, new int[] { oDps.getFkUserAuthorizedId() }, SLibConstants.EXEC_MODE_SILENT);

                sUserBuyer = SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.USRU_USR, new int[] { oDps.getFkUserNewId() });
                sUserAuthorize = SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.USRU_USR, new int[] { oDps.getFkUserAuthorizedId() });

                if ((oDps.getFkDpsCategoryId() == SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] && oDps.getFkDpsClassId() == SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] &&
                        oDps.getFkDpsTypeId() == SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2]) || oDps.getFkDpsCategoryId() == SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[0] &&
                        oDps.getFkDpsClassId() == SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[1] && oDps.getFkDpsTypeId() == SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[2]) {
                    try {
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
                        map.put("sAddressLine4", addressOficial.length > 3 ? addressOficial[3] : "");
                        map.put("sAddressDelivery1", mbIsCategoryPur ? addressDeliveryCompany[0] : addressDelivery[0]);
                        map.put("sAddressDelivery2", mbIsCategoryPur ? addressDeliveryCompany[1] : addressDelivery[1]);
                        map.put("sAddressDelivery3", mbIsCategoryPur ? addressDeliveryCompany[2] : addressDelivery[2]);
                        map.put("sAddressDelivery4", mbIsCategoryPur ? addressDeliveryCompany.length > 3 ? addressDeliveryCompany[3] : "" :
                            addressDelivery.length > 3 ? addressDelivery[3] : "");
                        map.put("sUserBuyer", sUserBuyer != null ? sUserBuyer : oUserBuyer.getUser());
                        map.put("sUserAuthorize", sUserAuthorize != null ? sUserAuthorize : oUserAuthorize.getUser());
                        map.put("nBizPartnerCategory", mbIsCategoryPur ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);
                        map.put("nIdTpCarSup", SModSysConsts.LOGS_TP_CAR_CAR);

                        jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_ORD_GDS, map);
                        jasperViewer = new JasperViewer(jasperPrint, false);
                        jasperViewer.setTitle("Impresión de orden de " + (mbIsCategoryPur ? "entrada" : "salida") + " de mercancias");
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

    private void actionPrintPhotoInvoice() {
        if (jbPrintPhotoInvoice.isEnabled()) {
            String[] addressOficial = null;
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
            SDataCurrency oCurrency = null;

            if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
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

                    addressOficial = oBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(miClient.getSessionXXX().getParamsErp().getFkLanguageId(),
                        SDataBizPartnerBranchAddress.ADDRESS_4ROWS, false);
                    addressDelivery = oAddress.obtainAddress(miClient.getSessionXXX().getParamsErp().getFkLanguageId(),
                        SDataBizPartnerBranchAddress.ADDRESS_4ROWS, false);
                    addressDeliveryCompany = oCompanyBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(miClient.getSessionXXX().getParamsErp().getFkLanguageId(),
                        SDataBizPartnerBranchAddress.ADDRESS_4ROWS, false);

                    if (oDps.getFkDpsCategoryId() == SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0] && oDps.getFkDpsClassId() == SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1] &&
                            oDps.getFkDpsTypeId() == SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2]) {
                        try {
                            setCursor(new Cursor(Cursor.WAIT_CURSOR));

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
                            map.put("bShowBackground", true);

                            jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_DPS, map);
                            jasperViewer = new JasperViewer(jasperPrint, false);
                            jasperViewer.setTitle("Impresión de foto factura");
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

        if (jbSignXml.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                boolean canEmitCompMonExt = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_COMP_MON_EXT).HasRight;
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

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
                else if (!miClient.getSession().getSessionCustom().isLocalCurrency(new int[] { dps.getFkCurrencyId() }) && !canEmitCompMonExt) {
                    miClient.showMsgBoxWarning("El usuario '" + miClient.getSession().getUser().getName() + "' no puede emitir comprobantes en moneda extranjera.");
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

    private void actionValidateCfdi() throws Exception {
        SDataDps dps = null;

        if (jbValidateCfdi.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (dps.getIsDeleted()) {
                    miClient.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' está eliminado.");
                }
                else if (dps.getDbmsDataCfd() == null) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el registro CFD del documento '" + dps.getDpsNumber() + "'.");
                }
                else {
                    if (SCfdUtils.validateCfdi(miClient, dps.getDbmsDataCfd(), SLibConstants.UNDEFINED, true)) {
                        miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                    }
                }
            }
        }
    }
    
    private void actionGetCfdiStatus() throws Exception {
        if (jbGetCfdiStatus.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (dps.getIsDeleted()) {
                    miClient.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' está eliminado.");
                }
                else if (dps.getDbmsDataCfd() == null) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el registro CFD del documento '" + dps.getDpsNumber() + "'.");
                }
                else {
                    miClient.showMsgBoxInformation(new SCfdUtilsHandler(miClient).getCfdiSatStatus(dps.getDbmsDataCfd()).composeMessage());
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

    private void actionRestoreAckCancellation() throws Exception {
        boolean needUpdate = true;
        SDataDps dps = null;

        if (jbRestoreAckCancellation.isEnabled()) {
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

    private void actionResetPacFlags() throws Exception {
        SDataDps dps = null;

        if (jbResetPacFlags.isEnabled()) {
           if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (dps.getIsDeleted()) {
                    miClient.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' está eliminado.");
                }
                else {
                    SCfdUtils.resetCfdiDeactivateFlags(miClient, dps.getDbmsDataCfd());
                    miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                }
           }
        }
    }
    
    public void publicActionPrint() {
        actionPrint(false);
    }
    
    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.b_del = 0 ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "d.dt");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_DOC_NAT) {
                if (((Integer) setting.getSetting()) != SLibConstants.UNDEFINED) {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.fid_dps_nat = " + ((Integer) setting.getSetting()) + " ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_ARE) {
                if (((Integer) setting.getSetting()) != SLibConstants.UNDEFINED) {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.fid_func = " + ((Integer) setting.getSetting()) + " ";
                }
            }
        }

        msSql = "SELECT d.id_year, d.id_doc, d.dt, d.dt_doc_delivery_n, d.num_ref, d.comms_ref, d.exc_rate, " +
                "d.stot_r, d.tax_charged_r, d.tax_retained_r, d.tot_r, " +
                "d.stot_cur_r, d.tax_charged_cur_r, d.tax_retained_cur_r, d.tot_cur_r, " +
                "d.b_copy, d.b_link, d.b_close, d.b_audit, d.b_del, d.ts_link, d.ts_close, d.ts_new, d.ts_edit, d.ts_del, dt.code, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                "(SELECT fa.code FROM cfgu_func AS fa WHERE d.fid_func = fa.id_func) AS f_fa_code, " +
                "(SELECT dn.code FROM erp.trnu_dps_nat AS dn WHERE d.fid_dps_nat = dn.id_dps_nat) AS f_dn_code, " +
                "(SELECT CONCAT(dps_src.num_ser, IF(length(dps_src.num_ser) = 0, '', '-'), dps_src.num) " +
                "FROM trn_dps AS dps_src INNER JOIN trn_dps_dps_supply AS spl ON dps_src.id_doc = spl.id_src_doc AND dps_src.id_year = spl.id_src_year " +
                "WHERE spl.id_des_doc = d.id_doc AND dps_src.id_year = d.id_year AND dps_src.b_del = 0 LIMIT 1) AS f_ord_num, " +
                "(SELECT de.concept FROM trn_dps_ety AS de WHERE de.id_doc = d.id_doc AND de.id_year = d.id_year AND NOT de.b_del ORDER BY de.id_ety LIMIT 1) AS f_concept, " +
                "(SELECT CONCAT(mo.id_year, '-', mo.num) FROM mfg_ord AS mo WHERE d.fid_mfg_year_n = mo.id_year AND d.fid_mfg_ord_n = mo.id_ord) AS f_mfg_ord, " +
                "IF(d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", " + STableConstants.ICON_ST_ANNUL + ", " + STableConstants.ICON_NULL + ") AS f_ico, " +
                "IF(x.ts IS NULL OR doc_xml = '', " + STableConstants.ICON_NULL  + ", " + // not is CFD nor CFDI
                "IF(x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_CFD + " OR x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_NA + ", " + STableConstants.ICON_XML + ", " + // is CFD
                "IF(x.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " OR LENGTH(uuid) = 0, " + STableConstants.ICON_XML_PEND + ", " + // CFDI pending sign
                "IF(LENGTH(x.ack_can_xml) = 0 AND x.ack_can_pdf_n IS NULL, " + STableConstants.ICON_XML_SIGN  + ", " + // CFDI signed, canceled only SIIE
                "IF(LENGTH(x.ack_can_xml) != 0, " + STableConstants.ICON_XML_CANC_XML + ", " +  // CFDI canceled with cancellation acknowledgment in XML format
                "IF(x.ack_can_pdf_n IS NOT NULL, " + STableConstants.ICON_XML_CANC_PDF + ", " + // CFDI canceled with cancellation acknowledgment in PDF format
                STableConstants.ICON_XML_SIGN + " " + // CFDI signed, canceled only SIIE
                ")))))) AS f_ico_xml, " +
                "x.can_st, " + // cancellation status
                "bp.id_bp, bp.bp, bpc.bp_key, bpb.id_bpb, bpb.bpb, " +
                "(SELECT c.cur_key FROM erp.cfgu_cur AS c WHERE d.fid_cur = c.id_cur) AS f_cur_key, " +
                "'" + miClient.getSession().getSessionCustom().getLocalCurrencyCode() + "' AS f_cur_key_local, " +
                "(SELECT cob.code FROM erp.bpsu_bpb AS cob WHERE d.fid_cob = cob.id_bpb) AS f_cob_code, ul.usr, uc.usr, un.usr, ue.usr, ud.usr ";

        if (mbIsDoc || mbIsDocAdj) {
            msSql += ", (SELECT rbc.code FROM fin_bkc AS rbc WHERE r.id_bkc = rbc.id_bkc) AS f_rbc_code, (SELECT rcb.code FROM erp.bpsu_bpb AS rcb WHERE r.fid_cob = rcb.id_bpb) AS f_rcb_code, CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, 2)) as f_rper, " +
                    "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) as f_rnum ";
        }

        msSql +=
                "FROM trn_dps AS d " +
                "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bpc ON bp.id_bp = bpc.id_bp AND bpc.id_ct_bp = " +
                (mbIsCategoryPur ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND ";

        switch (mnTabTypeAux02) {
            case SDataConstantsSys.TRNX_TP_DPS_EST_EST:
            case SDataConstantsSys.TRNX_TP_DPS_EST_CON:
                msSql += (mbIsCategoryPur ?
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_EST[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1] + " " :
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_EST[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1] + " ");

                if (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_EST_EST) {
                    msSql += (mbIsCategoryPur ?
                            "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_EST[2] + " " :
                            "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_EST[2] + " ");
                }
                else if (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_EST_CON) {
                    msSql += (mbIsCategoryPur ?
                            "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CON[2] + " " :
                            "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CON[2] + " ");
                }
                break;

            case SDataConstantsSys.TRNX_TP_DPS_ORD:
                msSql += (mbIsCategoryPur ?
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[1] + " " :
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ORD[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ORD[1] + " ");
                break;

            case SDataConstantsSys.TRNX_TP_DPS_DOC:
                msSql += (mbIsCategoryPur ?
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1] + " " :
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] + " ");
                break;

            case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                msSql += (mbIsCategoryPur ?
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1] + " " :
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1] + " ");
                break;
            default:
        }

        msSql +=
                "INNER JOIN erp.usru_usr AS ul ON d.fid_usr_link = ul.id_usr " +
                "INNER JOIN erp.usru_usr AS uc ON d.fid_usr_close = uc.id_usr " +
                "INNER JOIN erp.usru_usr AS un ON d.fid_usr_new = un.id_usr " + (mbHasRightAuthor ? " AND d.fid_usr_new = " + miClient.getSession().getUser().getPkUserId() + " " : "") +
                "INNER JOIN erp.usru_usr AS ue ON d.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON d.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN trn_cfd AS x ON d.id_year = x.fid_dps_year_n AND d.id_doc = x.fid_dps_doc_n ";

        if (mbIsDoc || mbIsDocAdj) {
            msSql +=
                    "LEFT OUTER JOIN trn_dps_rec AS dr ON d.id_year = dr.id_dps_year AND d.id_doc = dr.id_dps_doc " +
                    "LEFT OUTER JOIN fin_rec AS r ON dr.fid_rec_year = r.id_year AND dr.fid_rec_per = r.id_per AND dr.fid_rec_bkc = r.id_bkc AND dr.fid_rec_tp_rec = r.id_tp_rec AND dr.fid_rec_num = r.id_num ";
        }

        msSql += (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere);

        if (getDpsSortingType() == SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC) {
            msSql += "ORDER BY ";

            if ((mbIsCategoryPur && miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) ||
                (mbIsCategorySal && miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME)) {
                msSql += "bpc.bp_key, bp.bp, ";
            }
            else {
                msSql += "bp.bp, bpc.bp_key, ";
            }

            msSql += "bp.id_bp, bpb.bpb, bpb.id_bpb, dt.code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt ";
        }
        else {
            msSql += "ORDER BY dt.code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt, ";

            if ((mbIsCategoryPur && miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) ||
                (mbIsCategorySal && miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME)) {
                msSql += "bpc.bp_key, bp.bp, ";
            }
            else {
                msSql += "bp.bp, bpc.bp_key, ";
            }

            msSql += "bp.id_bp, bpb.bpb, bpb.id_bpb ";
        }
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
                else if (button == jbCopy) {
                    actionCopy();
                }
                else if (button == jbImport) {
                    actionImport();
                }
                else if (button == jbImportCfdiWithOutPurchaseOrder){
                    actionImportCfdi(false);
                }
                else if (button == jbImportCfdiWithPurchaseOrder){
                    actionImportCfdi(true);
                }
                else if (button == jbChangeDeliveryAddress) {
                    actionChangeDeliveryAddress();
                }
                else if (button == jbChangeAgentSupervisor) {
                    actionChangeAgentSupervisor();
                }
                else if (button == jbSetDeliveryDate) {
                    actionSetDeliveryDate();
                }
                else if (button == jbSetReferenceCommissions) {
                    actionSetReferenceCommissions();
                }
                else if (button == jbViewNotes) {
                    actionViewNotes();
                }
                else if (button == jbViewLinks) {
                    actionViewLinks();
                }
                else if (button == jbViewContractAnalysis) {
                    actionViewContractAnalysis();
                }
                else if (button == jbViewAccountingRecord) {
                    actionViewAccountingRecord();
                }
                else if (button == jbViewAccountingDetailsDps) {
                    actionViewAccountingDetailsDps();
                }
                else if (button == jbViewAccountingDetailsBizPartner) {
                    actionViewAccountingDetailsBizPartner();
                }
                else if (button == jbPrint) {
                    actionPrint(false);
                }
                else if (button == jbPrintByRange) {
                    actionPrintByRange();
                }
                else if (button == jbPrintContractKgAsTon) {
                    actionPrint(true);
                }
                else if (button == jbPrintContractMoves) {
                    actionPrintContractMoves();
                }
                else if (button == jbPrintOrderGoods) {
                    actionPrintOrderGoods();
                }
                else if (button == jbPrintPhotoInvoice) {
                    actionPrintPhotoInvoice();
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
                else if (button == jbValidateCfdi) {
                    actionValidateCfdi();
                }
                else if (button == jbGetCfdiStatus) {
                    actionGetCfdiStatus();
                }
                else if (button == jbSendCfdi) {
                    actionSendCfdi();
                }
                else if (button == jbRestoreSignXml) {
                    actionRestoreSignXml();
                }
                else if (button == jbRestoreAckCancellation) {
                    actionRestoreAckCancellation();
                }
                else if (button == jbResetPacFlags) {
                    actionResetPacFlags();
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            miClient.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}

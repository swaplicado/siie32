/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import cfd.DCfdConsts;
import erp.SClientUtils;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
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
import erp.mmkt.data.SDataCustomerConfig;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mqlt.data.SDpsQualityUtils;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SCfdUtilsHandler;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.SDataMinorChangesDps;
import erp.mtrn.data.SDataUserDnsDps;
import erp.mtrn.data.STrnUtilities;
import erp.mtrn.data.cfd.SCfdRenderer;
import erp.mtrn.form.SDialogAnnulCfdi;
import erp.mtrn.form.SDialogContractAnalysis;
import erp.mtrn.form.SDialogDpsFinder;
import erp.mtrn.form.SDialogDpsMaterialRequestLink;
import erp.mtrn.form.SDialogPrintCfdiMasive;
import erp.mtrn.form.SDialogUpdateDpsDate;
import erp.mtrn.form.SDialogUpdateDpsDeliveryAddress;
import erp.mtrn.form.SDialogUpdateDpsLogistics;
import erp.mtrn.form.SDialogUpdateDpsReferenceComms;
import erp.mtrn.form.SDialogUpdateDpsSalesAgentComms;
import erp.musr.data.SDataUser;
import erp.print.SDataConstantsPrint;
import erp.table.SFilterConstants;
import erp.table.STabFilterDnsDps;
import erp.table.STabFilterDocumentNature;
import erp.table.STabFilterFunctionalArea;
import erp.table.STabFilterUsers;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
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
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;

/**
 * @author Sergio Flores, Alfredo Pérez, Isabel Servín, Claudio Peña, Sergio Flores, Edwin Carmona
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
    private javax.swing.JButton jbChangeDpsDate;
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
    private javax.swing.JButton jbPrintEnglish;
    private javax.swing.JButton jbPrintContractKgAsTon;
    private javax.swing.JButton jbPrintContractMoves;
    private javax.swing.JButton jbPrintOrderGoods;
    private javax.swing.JButton jbGetXml;
    private javax.swing.JButton jbGetPdf;
    private javax.swing.JButton jbGetAcknowledgmentCancellation;
    private javax.swing.JButton jbShowCfdi;
    private javax.swing.JButton jbSignXml;
    private javax.swing.JButton jbValidateCfdi;
    private javax.swing.JButton jbGetCfdiStatus;
    private javax.swing.JButton jbSendCfdi;
    private javax.swing.JButton jbResetPacFlags;
    private javax.swing.JButton jbImportCfdiWithOutPurchaseOrder;
    private javax.swing.JButton jbImportCfdiWithPurchaseOrder;
    private javax.swing.JButton jbImportMatRequest;
    private javax.swing.JButton jbChangeDpsEntryItem;
    private javax.swing.JButton jbRestoreCfdStamped;
    private javax.swing.JButton jbRestoreCfdCancelAck;
    private erp.table.STabFilterUsers moTabFilterUser;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.table.STabFilterDocumentNature moTabFilterDocumentNature;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;
    private erp.table.STabFilterDnsDps moTabFilterDnsDps;
    private erp.mtrn.form.SDialogUpdateDpsDeliveryAddress moDialogUpdateDpsDlvryAddrss;
    private erp.mtrn.form.SDialogUpdateDpsSalesAgentComms moDialogUpdateDpsSalesAgentComms;
    private erp.mtrn.form.SDialogUpdateDpsLogistics moDialogUpdateDpsLogistics;
    private erp.mtrn.form.SDialogUpdateDpsDate moDialogUpdateDpsDate;
    private erp.mtrn.form.SDialogUpdateDpsReferenceComms moDialogUpdateDpsRefCommissions;
    private erp.mtrn.form.SDialogContractAnalysis moDialogContractAnalysis;
    private erp.mtrn.form.SDialogDpsFinder moDialogDpsFinder;
    private erp.mfin.form.SDialogAccountingMoveDpsBizPartner moDialogAccountingMoveDpsBizPartner;
    private erp.mtrn.form.SDialogAnnulCfdi moDialogAnnulCfdi;

    private boolean mbIsCategoryPur;
    private boolean mbIsCategorySal;
    private boolean mbIsEstEst;
    private boolean mbIsEstCon;
    private boolean mbIsOrd;
    private boolean mbIsDoc;
    private boolean mbIsDocAdj;
    private boolean mbHasRightNew;
    private boolean mbHasRightEdit;
    private boolean mbHasRightAuthor;
    private boolean mbHasRightDelete;
    private boolean mbHasRightAnnul;
    private boolean mbHasRightLogistics;
    private int mnModule;
    
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
            mnModule = SDataConstants.MOD_PUR;
            
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
            mnModule = SDataConstants.MOD_SAL;
            
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
        
        jbImportMatRequest = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_IMPORT_MAT_REQ));
        jbImportMatRequest.setPreferredSize(new Dimension(23, 23));
        jbImportMatRequest.addActionListener(this);
        jbImportMatRequest.setToolTipText("Importar requisición de materiales");
        
        jbChangeDpsEntryItem = new JButton(miClient.getImageIcon(SLibConstants.ICON_EDIT));
        jbChangeDpsEntryItem.setPreferredSize(new Dimension(23, 23));
        jbChangeDpsEntryItem.addActionListener(this);
        jbChangeDpsEntryItem.setToolTipText("Cambiar ítems de documentos");

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
        
        jbChangeDpsDate = new JButton(miClient.getImageIcon(SLibConstants.ICON_GUI_CAL));
        jbChangeDpsDate.setPreferredSize(new Dimension(23, 23));
        jbChangeDpsDate.addActionListener(this);
        jbChangeDpsDate.setToolTipText("Cambiar fecha del documento");

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
        
        jbPrintEnglish = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT_PHOTO));
        jbPrintEnglish.setPreferredSize(new Dimension(23, 23));
        jbPrintEnglish.addActionListener(this);
        jbPrintEnglish.setToolTipText("Imprimir documento en inglés");

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
        
        jbGetPdf = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_TYPE));
        jbGetPdf.setPreferredSize(new Dimension(23, 23));
        jbGetPdf.addActionListener(this);
        jbGetPdf.setToolTipText("Obtener PDF del comprobante");
        
        jbGetAcknowledgmentCancellation = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML_CANCEL));
        jbGetAcknowledgmentCancellation.setPreferredSize(new Dimension(23, 23));
        jbGetAcknowledgmentCancellation.addActionListener(this);
        jbGetAcknowledgmentCancellation.setToolTipText("Obtener XML del acuse de cancelación del CFDI");

        jbShowCfdi = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif")));
        jbShowCfdi.setPreferredSize(new Dimension(23, 23)); 
        jbShowCfdi.addActionListener(this);
        jbShowCfdi.setToolTipText("Mostrar CFDI del comprobante");
        
        jbSignXml = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML_SIGN));
        jbSignXml.setPreferredSize(new Dimension(23, 23));
        jbSignXml.addActionListener(this);
        jbSignXml.setToolTipText("Timbrar CFDI");

        jbValidateCfdi = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif")));
        jbValidateCfdi.setPreferredSize(new Dimension(23, 23));
        jbValidateCfdi.addActionListener(this);
        jbValidateCfdi.setToolTipText("Validar timbrado o cancelación del CFDI");

        jbGetCfdiStatus = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_query.gif")));
        jbGetCfdiStatus.setPreferredSize(new Dimension(23, 23));
        jbGetCfdiStatus.addActionListener(this);
        jbGetCfdiStatus.setToolTipText("Consultar estatus SAT del CFDI");

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
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);
        moTabFilterDnsDps = new STabFilterDnsDps(miClient, this);
        moDialogUpdateDpsDlvryAddrss = new SDialogUpdateDpsDeliveryAddress(miClient);
        moDialogUpdateDpsSalesAgentComms = new SDialogUpdateDpsSalesAgentComms(miClient);
        moDialogUpdateDpsLogistics = new SDialogUpdateDpsLogistics(miClient);
        moDialogUpdateDpsDate = new SDialogUpdateDpsDate(miClient);
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
        addTaskBarUpperComponent(jbImportMatRequest);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbChangeDpsEntryItem);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbChangeDeliveryAddress);
        addTaskBarUpperComponent(jbChangeAgentSupervisor);
        addTaskBarUpperComponent(jbSetDeliveryDate);
        addTaskBarUpperComponent(jbChangeDpsDate);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbSetReferenceCommissions);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbViewNotes);
        addTaskBarUpperComponent(jbViewLinks);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbViewContractAnalysis);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbViewAccountingRecord);
        addTaskBarUpperComponent(jbViewAccountingDetailsDps);
        addTaskBarUpperComponent(jbViewAccountingDetailsBizPartner);
        
        addTaskBarLowerComponent(jbPrint);
        addTaskBarLowerComponent(jbPrintByRange);
        addTaskBarLowerComponent(jbPrintAcknowledgmentCancellation);
        addTaskBarLowerComponent(jbPrintPhotoInvoice);
        addTaskBarLowerComponent(jbPrintEnglish);
        addTaskBarLowerComponent(jbPrintContractKgAsTon);
        addTaskBarLowerComponent(jbPrintContractMoves);
        addTaskBarLowerComponent(jbPrintOrderGoods);
        addTaskBarLowerSeparator();
        addTaskBarLowerComponent(jbShowCfdi);
        addTaskBarLowerComponent(jbGetXml);
        addTaskBarLowerComponent(jbGetPdf);
        addTaskBarLowerComponent(jbGetAcknowledgmentCancellation);
        addTaskBarLowerComponent(jbRestoreCfdStamped);
        addTaskBarLowerComponent(jbRestoreCfdCancelAck);
        addTaskBarLowerSeparator();
        addTaskBarLowerComponent(jbSignXml);
        addTaskBarLowerComponent(jbValidateCfdi);
        addTaskBarLowerComponent(jbResetPacFlags);
        addTaskBarLowerComponent(jbGetCfdiStatus);
        addTaskBarLowerSeparator();
        addTaskBarLowerComponent(jbSendCfdi);
        addTaskBarLowerSeparator();
        addTaskBarLowerComponent(moTabFilterUser);
        addTaskBarLowerSeparator();
        addTaskBarLowerComponent(moTabFilterDocumentNature);
        addTaskBarLowerComponent(moTabFilterFunctionalArea);
        addTaskBarLowerComponent(moTabFilterDnsDps);

        jbNew.setEnabled(mbHasRightNew);
        jbEdit.setEnabled(true);
        jbDelete.setEnabled(mbHasRightDelete);
        jbAnnul.setEnabled(mbHasRightAnnul && mbHasRightEdit && (mbIsDoc || mbIsDocAdj));
        jbCopy.setEnabled(mbHasRightNew && !mbIsDocAdj);
        jbImport.setEnabled(mbHasRightNew && createImportFinder);
        jbImportCfdiWithOutPurchaseOrder.setEnabled(mbIsCategoryPur && mbIsDoc);
        jbImportCfdiWithPurchaseOrder.setEnabled(mbIsCategoryPur && mbIsDoc);
        jbImportMatRequest.setEnabled(mbIsCategoryPur);
        jbChangeDpsEntryItem.setEnabled(true);
        jbChangeDeliveryAddress.setEnabled(mbIsCategorySal && mbIsDoc && mbHasRightLogistics);
        jbChangeAgentSupervisor.setEnabled(mbIsCategorySal && mbIsDoc && mbHasRightLogistics);
        jbSetDeliveryDate.setEnabled(mbIsCategorySal && mbIsDoc && mbHasRightLogistics);
        jbChangeDpsDate.setEnabled(mbIsCategorySal && mbHasRightAnnul && mbHasRightEdit && (mbIsDoc || mbIsDocAdj));
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
        jbPrintEnglish.setEnabled(mbIsOrd || mbIsCategorySal && (mbIsEstEst || mbIsEstCon || mbIsDoc || mbIsDocAdj) || (mbIsCategoryPur && mbIsEstCon));
        jbPrintContractKgAsTon.setEnabled((mbIsCategorySal || mbIsCategoryPur) && mbIsEstCon);
        jbPrintContractMoves.setEnabled(mbIsCategorySal && mbIsEstCon);
        jbPrintOrderGoods.setEnabled(mbIsCategorySal && mbIsOrd);
        jbGetXml.setEnabled(mbIsDoc || mbIsDocAdj);
        jbGetPdf.setEnabled(mbIsCategoryPur && (mbIsDoc || mbIsDocAdj));
        jbShowCfdi.setEnabled(mbIsDoc || mbIsDocAdj);
        jbGetAcknowledgmentCancellation.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));
        jbSignXml.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj) && mbHasRightEdit);
        jbValidateCfdi.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj) && mbHasRightEdit);
        jbGetCfdiStatus.setEnabled((mbIsDoc || mbIsDocAdj) && mbHasRightEdit);
        jbSendCfdi.setEnabled(((mbIsCategoryPur && mbIsOrd) || (mbIsCategorySal && (mbIsDoc || mbIsDocAdj))) && mbHasRightEdit);
        jbRestoreCfdStamped.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj) && mbHasRightEdit);
        jbRestoreCfdCancelAck.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj) && mbHasRightEdit);
        jbResetPacFlags.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj) && mbHasRightEdit);
        moTabFilterDnsDps.setVisible(mbIsOrd);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = null;

        if (mbIsDoc || mbIsDocAdj) {
            aoTableColumns = new STableColumn[50];  // extra columns for accounting record and CFD info
        }
        else if (mbIsOrd) {
            aoTableColumns = new STableColumn[43];
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
            if (mbIsOrd) {
                aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_status", "autorizado", STableConstants.WIDTH_ICON);
                aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
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
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rbkc_code", "Centro contable", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rbpb_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rnum", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "xu.usr", "Usr. timbrado/cancelación", STableConstants.WIDTH_USER);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "x.ts_prc", "Timbrado/cancelación", STableConstants.WIDTH_DATE_TIME);
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
        mvSuscriptors.add(SDataConstants.TRN_CFD);
        mvSuscriptors.add(SModConsts.TRN_MAT_REQ);
        mvSuscriptors.add(SModConsts.TRNX_MAT_REQ_PEND_SUP);
        mvSuscriptors.add(SModConsts.TRNX_MAT_REQ_PEND_PUR);

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
    
    private boolean isRowSelected() {
        if (moTablePane.getSelectedTableRow() == null || moTablePane.getSelectedTableRow().getIsSummary()) {
//            miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            return false;
        }
        return true;
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            miClient.getGuiModule(mnModule).setFormComplement(new Object[] { getDpsTypeKey(), false });  // document type key, is imported
            if (miClient.getGuiModule(mnModule).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(mnModule).refreshCatalogues(mnTabType);
                SDataUtilities.showDpsRecord(miClient, (SDataDps) miClient.getGuiModule(mnModule).getRegistry());
            }
        }
    }

    @Override
    public void actionEdit() {
       if (jbEdit.isEnabled()) {
           if (isRowSelected()) {
                miClient.getGuiModule(mnModule).setFormComplement(new Object[] { getDpsTypeKey(), false });  // document type key, is imported
                miClient.getGuiModule(mnModule).setCurrentUserPrivilegeLevel(mbHasRightEdit ? SUtilConsts.LEV_AUTHOR : SUtilConsts.LEV_READ);

                if (miClient.getGuiModule(mnModule).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(mnModule).refreshCatalogues(mnTabType);
                    
                    SDataRegistry registry = miClient.getGuiModule(mnModule).getRegistry();
                    
                    if (registry instanceof SDataDps) {
                        SDataUtilities.showDpsRecord(miClient, (SDataDps) registry);
                    }
                    else if (registry instanceof SDataMinorChangesDps) {
                        SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                        
                        if (dps.getDbmsDataCfd() != null) {
                            try {
                                SCfdUtils.printCfd(miClient, dps.getDbmsDataCfd(), SLibConstants.UNDEFINED, SDataConstantsPrint.PRINT_MODE_PDF_FILE, 1, false);
                            }
                            catch (Exception e) {
                                SLibUtilities.renderException(this, e);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void actionDelete() {
       if (jbDelete.isEnabled()) {
            if (isRowSelected()) {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                    if (miClient.getGuiModule(mnModule).deleteRegistry(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_DELETE_OK) {
                        miClient.getGuiModule(mnModule).refreshCatalogues(mnTabType);
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
            if (isRowSelected()) {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_ANNUL) == JOptionPane.YES_OPTION) {
                    dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                    if (dps.getDbmsDataCfd() != null && dps.getDbmsDataCfd().isCfdi()) {
                        annul = false;
                        params = new SGuiParams();

                        if (dps.getDbmsDataCfd().isStamped()) {
                            moDialogAnnulCfdi.formReset();
                            moDialogAnnulCfdi.formRefreshCatalogues();
                            moDialogAnnulCfdi.setValue(SGuiConsts.PARAM_DATE, dps.getDate());
                            moDialogAnnulCfdi.setValue(SDialogAnnulCfdi.PARAM_NUMBER, dps.getDpsNumber());
                            moDialogAnnulCfdi.setValue(SDialogAnnulCfdi.PARAM_UUID, dps.getDbmsDataCfd().getUuid());
                            moDialogAnnulCfdi.setValue(SModConsts.TRNS_TP_CFD, SDataConstantsSys.TRNS_TP_CFD_INV);
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
                                params.getParamsMap().put(SGuiConsts.PARAM_ANNUL_RETRY_CANCEL, moDialogAnnulCfdi.isRetryCancelSelected());
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
                            params.getParamsMap().put(SGuiConsts.PARAM_ANNUL_RETRY_CANCEL, false);
                        }
                    }

                    if (annul) {
                        if (miClient.getGuiModule(mnModule).annulRegistry(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey(), params) == SLibConstants.DB_ACTION_ANNUL_OK) {
                            miClient.getGuiModule(mnModule).refreshCatalogues(mnTabType);
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
                int[] adjustmentSubtypeKey = (int[]) moDialogDpsFinder.getValue(SDataConstants.TRNS_STP_DPS_ADJ);
                Object complement = null;
                boolean isMatReqImport = false;
                if (adjustmentSubtypeKey == null) {
                    complement = new Object[] { 
                                                getDpsTypeKey(), 
                                                false, 
                                                moDialogDpsFinder.getValue(SDataConstants.TRN_DPS), 
                                                null, 
                                                moDialogDpsFinder.getValue(SLibConstants.VALUE_CURRENCY_LOCAL),
                                                isMatReqImport
                                            };
                }
                else { 
                    complement = new Object[] { 
                                                getDpsTypeKey(), 
                                                true, 
                                                moDialogDpsFinder.getValue(SDataConstants.TRN_DPS), 
                                                adjustmentSubtypeKey, 
                                                moDialogDpsFinder.getValue(SLibConstants.VALUE_CURRENCY_LOCAL),
                                                isMatReqImport
                                            };
                }
                
                miClient.getGuiModule(mnModule).setFormComplement(complement);   // document type key, reference document and adjustment type (optional)
                if (miClient.getGuiModule(mnModule).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(mnModule).refreshCatalogues(mnTabType);
                    SDataUtilities.showDpsRecord(miClient, (SDataDps) miClient.getGuiModule(mnModule).getRegistry());
                }
            }
        }
    }

    private void actionCopy() {
        if (jbCopy.isEnabled()) {
            if (isRowSelected()) {
                miClient.getGuiModule(mnModule).setFormComplement(new Object[] { getDpsTypeKey(), false });  // document type key, is imported
                if (miClient.getGuiModule(mnModule).showFormForCopy(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(mnModule).refreshCatalogues(mnTabType);
                    SDataUtilities.showDpsRecord(miClient, (SDataDps) miClient.getGuiModule(mnModule).getRegistry());
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
                                miClient.getGuiModule(mnModule).setFormComplement(new Object[] { getDpsTypeKey(), false });  // document type key, is imported
                                miClient.getGuiModule(mnModule).setAuxRegistry(dpsRendered);
                                if (miClient.getGuiModule(mnModule).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                                    miClient.getGuiModule(mnModule).refreshCatalogues(mnTabType);
                                    SDataUtilities.showDpsRecord(miClient, (SDataDps) miClient.getGuiModule(mnModule).getRegistry());
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
    
    private void actionImportMaterialRequest() {
        SDialogDpsMaterialRequestLink oDialog = new SDialogDpsMaterialRequestLink(miClient, getDpsTypeKey());
        oDialog.setValue(SDataConstants.TRN_DPS, null);
        oDialog.setFormVisible(true);
        if (oDialog.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            Object complement = new Object[] { getDpsTypeKey(), 
                                                false, 
                                                oDialog.getValue(SDataConstants.TRN_DPS), 
                                                null, 
                                                true,
                                                true
                                            };
            
            miClient.getGuiModule(mnModule).setFormComplement(complement);
            if (miClient.getGuiModule(mnModule).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(mnModule).refreshCatalogues(mnTabType);
                SDataUtilities.showDpsRecord(miClient, (SDataDps) miClient.getGuiModule(mnModule).getRegistry());
            }
        }
    }
    
    private void actionChangeDpsEntryItem() {
        if (jbChangeDpsEntryItem.isEnabled()) {
            if (isRowSelected()) {
                if (miClient.getGuiModule(mnModule).showForm(SDataConstants.TRNX_DPS_EDIT, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(mnModule).refreshCatalogues(mnTabType);
                }
            }
        }
    }
    
    private void actionChangeDeliveryAddress() {
        if (jbChangeDeliveryAddress.isEnabled()) {
            if (isRowSelected()) {
                moDialogUpdateDpsDlvryAddrss.formReset();
                moDialogUpdateDpsDlvryAddrss.setValue(SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey());
                moDialogUpdateDpsDlvryAddrss.setFormVisible(true);

                if (moDialogUpdateDpsDlvryAddrss.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    miClient.getGuiModule(mnModule).refreshCatalogues(mnTabType);
                }
            }
        }
    }
            
    private void actionChangeAgentSupervisor() {
        if (jbChangeAgentSupervisor.isEnabled()) {
            if (isRowSelected()) {
                moDialogUpdateDpsSalesAgentComms.formReset();
                moDialogUpdateDpsSalesAgentComms.setValue(SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey());
                moDialogUpdateDpsSalesAgentComms.setFormVisible(true);

                if (moDialogUpdateDpsSalesAgentComms.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    miClient.getGuiModule(mnModule).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    private void actionSetDeliveryDate() {
        if (jbSetDeliveryDate.isEnabled()) {
            if (isRowSelected()) {
                moDialogUpdateDpsLogistics.formReset();
                moDialogUpdateDpsLogistics.setValue(SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey());
                moDialogUpdateDpsLogistics.setFormVisible(true);

                if (moDialogUpdateDpsLogistics.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    miClient.getGuiModule(mnModule).refreshCatalogues(mnTabType);
                }
            }
        }
    }
    
    private void actionChangeDpsDate() {
        if (jbChangeDpsDate.isEnabled()) {
            if (isRowSelected()) {
                moDialogUpdateDpsDate.formReset();
                moDialogUpdateDpsDate.setValue(SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey());
                if (moDialogUpdateDpsDate.getFormResult() == SLibConstants.UNDEFINED) {
                    moDialogUpdateDpsDate.setFormVisible(true);
                }
                if (moDialogUpdateDpsDate.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    miClient.getGuiModule(mnModule).refreshCatalogues(mnTabType);
                }
            }
        }
    }
    
    private void actionSetReferenceCommissions() {
        if (jbSetReferenceCommissions.isEnabled()) {
            if (isRowSelected()) {
                moDialogUpdateDpsRefCommissions.formReset();
                moDialogUpdateDpsRefCommissions.setValue(SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey());
                moDialogUpdateDpsRefCommissions.setFormVisible(true);

                if (moDialogUpdateDpsRefCommissions.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    miClient.getGuiModule(mnModule).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    private void actionViewNotes() {
        if (jbViewNotes.isEnabled()) {
            if (isRowSelected()) {
                SModuleUtilities.showDocumentNotes(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow());
            }
        }
    }

    private void actionViewLinks() {
        if (jbViewLinks.isEnabled()) {
            if (isRowSelected()) {
                SModuleUtilities.showDocumentLinks(miClient, moTablePane.getSelectedTableRow());
            }
        }
    }

    private void actionViewContractAnalysis() {
        if (jbViewContractAnalysis.isEnabled()) {
            if (isRowSelected()) {
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);

                moDialogContractAnalysis.formReset();
                moDialogContractAnalysis.setValue(SDataConstants.TRN_DPS, dps);
                moDialogContractAnalysis.setFormVisible(true);
            }
        }
    }

    private void actionViewAccountingRecord() {
        if (jbViewAccountingRecord.isEnabled()) {
            if (isRowSelected()) {
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                
                if (dps.getDbmsRecordKey() == null) {
                    miClient.showMsgBoxInformation("El documento no tiene póliza contable.");
                }
                else {
                    miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(SDataConstants.FINX_REC_RO, dps.getDbmsRecordKey());
                }
            }
        }
    }

    private void actionViewAccountingDetailsDps() {
        if (jbViewAccountingDetailsDps.isEnabled()) {
            if (isRowSelected()) {
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { dps.getFkBizPartnerId_r() }, SLibConstants.EXEC_MODE_SILENT);

                moDialogAccountingMoveDpsBizPartner.refreshAccountingDetail();
                moDialogAccountingMoveDpsBizPartner.setParamBizPartnerName(bizPartner.getBizPartner());
                moDialogAccountingMoveDpsBizPartner.setParamDpsNumber(dps.getNumberSeries() + (dps.getNumberSeries().length() == 0 ? "" : "-") + dps.getNumber());
                moDialogAccountingMoveDpsBizPartner.setParamDpsKey(moTablePane.getSelectedTableRow().getPrimaryKey());
                moDialogAccountingMoveDpsBizPartner.setParamBizPartnerKey(null);
                moDialogAccountingMoveDpsBizPartner.showAccountingDetail(miClient.getSessionXXX().getWorkingYear(), miClient.getSessionXXX().getWorkingDate());
            }
        }
    }

    private void actionViewAccountingDetailsBizPartner() {
        if (jbViewAccountingDetailsDps.isEnabled()) {
            if (isRowSelected()) {
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { dps.getFkBizPartnerId_r() }, SLibConstants.EXEC_MODE_SILENT);

                moDialogAccountingMoveDpsBizPartner.refreshAccountingDetail();
                moDialogAccountingMoveDpsBizPartner.setParamBizPartnerName(bizPartner.getBizPartner());
                moDialogAccountingMoveDpsBizPartner.setParamDpsNumber("");
                moDialogAccountingMoveDpsBizPartner.setParamDpsKey(null);
                moDialogAccountingMoveDpsBizPartner.setParamBizPartnerKey(new int[] { dps.getFkBizPartnerId_r() });
                moDialogAccountingMoveDpsBizPartner.showAccountingDetail(miClient.getSessionXXX().getWorkingYear(), miClient.getSessionXXX().getWorkingDate());
            }
        }
    }

    private void actionPrintByRange() throws Exception {
        new SDialogPrintCfdiMasive(miClient, getDpsTypeKey()).setVisible(true);
    }
    
    private File getRouteImgReport() {
        JFileChooser save = new JFileChooser();
        save.setCurrentDirectory(new java.io.File("C:\\"));
        save.setDialogTitle("Seleccionar imagen");
        save.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("JPG y PNG","jpg","png");
        save.setFileFilter(filtro);
        save.setAcceptAllFileFilterUsed(false);

        if (save.showDialog(null, "Subir") == JFileChooser.APPROVE_OPTION) {
            System.out.println("getSelectedFile() : " + save.getSelectedFile());
        }
        
        return save.getSelectedFile();
    }
    
    private void actionPrint(final boolean contractKgAsTon) {
        if ((!contractKgAsTon && jbPrint.isEnabled()) || (contractKgAsTon && jbPrintContractKgAsTon.isEnabled())) {
            if (isRowSelected()) {
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
                    SDataBizPartnerBranch bprBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { dps.getFkBizPartnerBranchId() }, SLibConstants.EXEC_MODE_SILENT);
                    SDataBizPartnerBranchAddress bprBranchAddress = (SDataBizPartnerBranchAddress) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB_ADD, new int [] { dps.getFkBizPartnerBranchId(), dps.getFkBizPartnerBranchAddressId() }, SLibConstants.EXEC_MODE_SILENT);
                    SDataBizPartnerBranchContact comBranchContact = null;
                    SDataBizPartnerBranchContact bprBranchContact = null;
                    SDataCurrency currency = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR, new int[] { dps.getFkCurrencyId() }, SLibConstants.EXEC_MODE_SILENT);

                    int addressFormatType = 0;
                    boolean addCountry = false;
                    
                    if (bprBranch.getFkAddressFormatTypeId_n() != SLibConstants.UNDEFINED) {
                        addressFormatType = bprBranch.getFkAddressFormatTypeId_n();
                    }
                    else {
                        addressFormatType = miClient.getSessionXXX().getParamsCompany().getFkDefaultAddressFormatTypeId_n();
                    }
                    
                    if (bprBranchAddress.getFkCountryId_n() == comBranch.getDbmsBizPartnerBranchAddressOfficial().getFkCountryId_n()) {
                        addCountry = false;
                    }
                    else {
                        addCountry = true;
                    }

                    SDataBizPartner company = miClient.getSessionXXX().getCompany().getDbmsDataCompany();
                    String[] comAddressTexts = comBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_4ROWS, addCountry);
                    String[] bprAddressTexts = bprBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_4ROWS, addCountry);
                    String[] dvyAddressTexts = bprBranchAddress.obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_4ROWS, addCountry);

                    Cursor cursor = getCursor();
                    Map<String, Object> map = null;
                    JasperPrint jasperPrint = null;
                    JasperViewer jasperViewer = null;
                    
                    if (SLibUtilities.belongsTo(dps.getDpsTypeKey(), new int[][] { SDataConstantsSys.TRNU_TP_DPS_SAL_ORD, SDataConstantsSys.TRNU_TP_DPS_PUR_ORD })) {
                        // order:

                        if (SLibUtilities.compareKeys(dps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_PUR_ORD) && !dps.getIsAuthorized()) {
                            miClient.showMsgBoxWarning("No se puede imprimir el documento porque su estatus es:\n-" + dps.getDbmsAuthorizationStatusName() + ".");
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
                            
                            if (company.getFiscalId().equals(DCfdConsts.RFC_GEN_INT)) {
                                // international company:
                                
                                comAddressTexts = comBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_2ROWS, addCountry);
                                bprAddressTexts = bprBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_2ROWS, addCountry);
                                dvyAddressTexts = bprBranchAddress.obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_2ROWS, addCountry);
                                
                                SDataBizPartnerBranchContact bprBranchContactForPhone = company.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchContacts().get(0);
                                bprBranchContact = bprBranch.getDbmsBizPartnerBranchContacts().size() <= 1 ? null : bprBranch.getDbmsBizPartnerBranchContacts().get(1);

                                map = miClient.createReportParams();
                                map.put("nDpsYearId", dps.getPkYearId());
                                map.put("nDpsDocId", dps.getPkDocId());
                                map.put("sDpsNumber", dps.getDpsNumber());
                                map.put("tDpsDate", dps.getDate());
                                map.put("nDpsDueDays", dps.getDaysOfCredit());
                                
                                map.put("sDpsNote1", "If any fees are not received by Saporis International by the due date, those fees may accrue late interest at a rate of 1.5% of the outstanding balance per month or the maximum rate permitted by the law, whichever is lower.");
                                map.put("sDpsNote2", "<p><b>PLEASE WIRE TRANSFER TO THE FOLLOWING BANK ACCOUNT NUMBER</b></p>"
                                        + "<p>BANK OF AMERICA</p>"
                                        + "<p>ACCOUNT NAME: SAPORIS INTERNATIONAL INC</p>"
                                        + "<p>ACCOUNT NUMBER: 325143301493</p>"
                                        + "<p>ROUTING NUMBER: 121000358</p>"
                                        + "<p>WIRE: 026009593</p>"
                                        + "<br>"
                                        + "<p>PLEASE SEND CHECKS TO 1546 CHIA WAY LOS ANGELES CA 90041 USA</p>"
                                        + "<p>If you have any questions about this invoice, please contact: jacinta@simplefoods.mx</p>");
                                
                                map.put("sPONumber", dps.getNumberReference());
                                map.put("sONumber", "");
                                
                                map.put("sIssName", company.getBizPartner());
                                map.put("sIssAddress1", comAddressTexts[0]);
                                map.put("sIssAddress2", comAddressTexts[1]);
                                
                                SDataBizPartner customer = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { dps.getFkBizPartnerId_r() }, SLibConstants.EXEC_MODE_SILENT);
                                map.put("sCusNumber", customer.getDbmsCategorySettingsCus().getKey());
                                map.put("sCusName", customer.getBizPartner());
                                map.put("sCusAddress1", bprAddressTexts[0]);
                                map.put("sCusAddress2", bprAddressTexts[1]);
                                
                                map.put("sDvyName", customer.getBizPartner());
                                map.put("sDvyAddress1", dvyAddressTexts[0]);
                                map.put("sDvyAddress2", dvyAddressTexts[1]);
                                
                                map.put("sDvyPhone", bprBranchContactForPhone.getAuxTelephoneNumbers());
                                map.put("sDvyContact", bprBranchContact == null ? "" : bprBranchContact.getContact());
                                
                                map.put("sCurCode", miClient.getSession().getSessionCustom().getCurrencyCode(new int[] { dps.getFkCurrencyId() }));
                                
                                map.put("sValueText", SLibUtilities.translateValueToText(dps.getTotalCy_r(), 2,
                                    dps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                    SLibConstants.LAN_ENGLISH, currency.getTextSingular(), currency.getTextPlural(), currency.getTextPrefix(), currency.getTextSuffix()));

                                jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_DPS_US, map);
                                jasperViewer = new JasperViewer(jasperPrint, false);
                                jasperViewer.setTitle("Invoice printing");
                                jasperViewer.setVisible(true);
                            }
                            else {
                                // national company:
                                
                                comBranchContact = company.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchContacts().size() <= 1 ? null :
                                        company.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchContacts().get(1);

                                map = miClient.createReportParams();
                                map.put("nIdYear", dps.getPkYearId());
                                map.put("nIdDoc", dps.getPkDocId());
                                map.put("sAddressLine1", bprAddressTexts[0]);
                                map.put("sAddressLine2", bprAddressTexts[1]);
                                map.put("sAddressLine3", bprAddressTexts[2]);
                                map.put("sAddressLine4", bprAddressTexts.length > 3 ? bprAddressTexts[3] : "");
                                map.put("sAddressDelivery1", dvyAddressTexts[0].compareTo(bprAddressTexts[0]) == 0 ? "": dvyAddressTexts[0]);
                                map.put("sAddressDelivery2", dvyAddressTexts[1].compareTo(bprAddressTexts[1]) == 0 ? "DOMICILIO DEL CLIENTE": dvyAddressTexts[1]);
                                map.put("sAddressDelivery3", dvyAddressTexts[2].compareTo(bprAddressTexts[2]) == 0 ? "": dvyAddressTexts[2]);
                                map.put("sAddressDelivery4", dvyAddressTexts.length > 3 && bprAddressTexts.length > 3 ?
                                    dvyAddressTexts[3].compareTo(bprAddressTexts[3]) == 0 ? "" : dvyAddressTexts[3] : "");
                                map.put("nBizPartnerCategory", SDataConstantsSys.BPSS_CT_BP_CUS);
                                map.put("sValueText", SLibUtilities.translateValueToText(dps.getTotalCy_r(), 2,
                                    dps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                    SLibConstants.LAN_ENGLISH, currency.getTextSingular(), currency.getTextPlural(), currency.getTextPrefix(), currency.getTextSuffix()));
                                map.put("sErpCurrencyKey", miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
                                map.put("bShowBackground", false);
                                map.put("sManagerFinance", comBranchContact == null ? "" : SLibUtilities.textTrim(comBranchContact.getContactPrefix() + " " + comBranchContact.getFirstname() + " " + comBranchContact.getLastname()));

                                jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_DPS, map);
                                jasperViewer = new JasperViewer(jasperPrint, false);
                                jasperViewer.setTitle("Impresión de factura");
                                jasperViewer.setVisible(true);
                            }
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
                            
                            comBranchContact = company.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchContacts().size() <= 1 ? null :
                                    company.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchContacts().get(1);

                            map = miClient.createReportParams();
                            map.put("nIdYear", dps.getPkYearId());
                            map.put("nIdDoc", dps.getPkDocId());
                            map.put("sAddressLine1", bprAddressTexts[0]);
                            map.put("sAddressLine2", bprAddressTexts[1]);
                            map.put("sAddressLine3", bprAddressTexts[2]);
                            map.put("sAddressLine4", bprAddressTexts.length > 3 ? bprAddressTexts[3] : "");
                            map.put("nBizPartnerCategory", SDataConstantsSys.BPSS_CT_BP_CUS);
                            map.put("sValueText", SLibUtilities.translateValueToText(dps.getTotalCy_r(), 2,
                                dps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                SLibConstants.LAN_ENGLISH, currency.getTextSingular(), currency.getTextPlural(), currency.getTextPrefix(), currency.getTextSuffix()));
                            map.put("sErpCurrencyKey", miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
                            map.put("bShowBackground", false);
                            map.put("sManagerFinance", comBranchContact == null ? "" : SLibUtilities.textTrim(comBranchContact.getContactPrefix() + " " + comBranchContact.getFirstname() + " " + comBranchContact.getLastname()));

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
                                map.put("sAddressLine1", bprAddressTexts[0]);
                                map.put("sAddressLine2", bprAddressTexts[1]);
                                map.put("sAddressLine3", bprAddressTexts[2]);
                                map.put("sAddressLine4", bprAddressTexts.length > 3 ? bprAddressTexts[3] : "");
                            }
                            else {
                                String[] comBranchAddressTexts = miClient.getSessionXXX().getCurrentCompanyBranch().getDbmsBizPartnerBranchAddressOfficial().obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_4ROWS, addCountry);
                                map.put("sAddressLine1", comBranchAddressTexts[0] == null || comBranchAddressTexts[0].isEmpty() ? "": comBranchAddressTexts[0]);
                                map.put("sAddressLine2", comBranchAddressTexts[1] == null || comBranchAddressTexts[1].isEmpty() ? "": comBranchAddressTexts[1]);
                                map.put("sAddressLine3", comBranchAddressTexts[2] == null || comBranchAddressTexts[2].isEmpty() ? "": comBranchAddressTexts[2]);
                                map.put("sAddressLine4", comBranchAddressTexts.length > 3 ? comBranchAddressTexts[3] : "");
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
                            
                            bprBranchContact = bprBranch.getDbmsBizPartnerBranchContact(new int[] { dps.getFkContactBizPartnerBranchId_n(), dps.getFkContactContactId_n() });
                            
                            if (bprBranchContact == null) {
                                for (int i = 0; i < bprBranch.getDbmsBizPartnerBranchContacts().size(); i++) {
                                    if (bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFkContactTypeId() == SDataConstantsSys.BPSS_TP_CON_PUR && !bprBranch.getDbmsBizPartnerBranchContacts().get(i).getIsDeleted()) {
                                        textContact = bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname() +
                                                (bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname().length() > 0 ? " " : "") +
                                                bprBranch.getDbmsBizPartnerBranchContacts().get(i).getLastname();
                                    }
                                    else if (bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFkContactTypeId() == SDataConstantsSys.BPSS_TP_CON_ADM && !bprBranch.getDbmsBizPartnerBranchContacts().get(i).getIsDeleted()) {
                                        textContact = bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname() +
                                                (bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname().length() > 0 ? " " : "") +
                                                bprBranch.getDbmsBizPartnerBranchContacts().get(i).getLastname();
                                    }

                                    if (!textContact.isEmpty()) {
                                        break;
                                    }
                                }
                            }
                            else {
                                textContact = bprBranchContact.getFirstname() + (bprBranchContact.getFirstname().length() > 0 ? " " : "") + bprBranchContact.getLastname();
                            }

                            map.put("sContact", textContact.isEmpty() ? bprBranch.getDbmsBizPartner() : textContact);
                            map.put("ctBpCus", SDataConstantsSys.BPSS_CT_BP_CUS);
                            map.put("ctBpSup", SDataConstantsSys.BPSS_CT_BP_SUP);
                            map.put("nFidCtBp", SLibUtilities.compareKeys(dps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_CON) ? SDataConstantsSys.BPSS_CT_BP_CUS : SDataConstantsSys.BPSS_CT_BP_SUP);
                            map.put("oDecimalFormat", miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormatFixed4());
                            map.put("oQuantityFormat", miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat());
                            map.put("oDateTextFormat", miClient.getSessionXXX().getFormatters().getDateTextFormat());
                            map.put("nFidTpCarBp", SModSysConsts.LOGS_TP_CAR_CAR);
                            map.put("nLogIncotermExw", SModSysConsts.LOGS_INC_EXW);
                            map.put("bPrintTon", contractKgAsTon);
                            
                            boolean hasAnalysis = false;
                            boolean isPrint = true;
                            if (! dps.getDbmsDpsEntries().isEmpty() && SLibUtilities.compareKeys(dps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_CON)) {
                                for (int i = 0; i < dps.getDbmsDpsEntries().size(); i++) {
                                    int[] pkEty = new int[] { dps.getDbmsDpsEntries().get(i).getPkYearId(), dps.getDbmsDpsEntries().get(i).getPkDocId(), dps.getDbmsDpsEntries().get(i).getPkEntryId() };
                                    hasAnalysis = ! SDpsQualityUtils.getAnalysisByDocumentEty(miClient.getSession(), dps.getDbmsDpsEntries().get(i).getFkItemId(), pkEty, isPrint).isEmpty();
                                    if (hasAnalysis) {
                                        break;
                                    }
                                }
                            }
                            
                            map.put("bIsSalesContract", hasAnalysis);

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
                    else if (SLibUtilities.compareKeys(dps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_EST)) {
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
                            miClient.getSessionXXX().getCurrentCompany().getDbmsDataCompany().getDbmsBizPartnerBranches();
                            
                            for (int i = 0; i < company.getDbmsBizPartnerBranches().size(); i++ ) {
                                if (company.getDbmsBizPartnerBranches().get(i).getPkBizPartnerBranchId() == dps.getFkCompanyBranchId()) {
                                    comAddressTexts = company.getDbmsBizPartnerBranches().get(i).getDbmsBizPartnerBranchAddresses().get(0).obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_4ROWS, addCountry);
                                    map.put("sAddressLine1", comAddressTexts[0] == null || comAddressTexts[0].isEmpty() ? "": comAddressTexts[0]);
                                    map.put("sAddressLine2", comAddressTexts[1] == null || comAddressTexts[1].isEmpty() ? "": comAddressTexts[1]);
                                    map.put("sAddressLine3", comAddressTexts[2].toUpperCase() == null || comAddressTexts[2].isEmpty() ? "": comAddressTexts[2].toUpperCase());
                                    map.put("sAddressLine4", comAddressTexts.length > 3 ? comAddressTexts[3] : "");
                                    map.put("sFiscalIdBp", miClient.getSessionXXX().getCurrentCompany().getDbmsDataCompany().getFiscalId());
                                    map.put("sTelephone", company.getDbmsBizPartnerBranches().get(i).getDbmsBizPartnerBranchContacts().get(0).getAuxTelephoneNumbers());
                                    break;
                                }
                            }

                            textsPrices.add("");
                            textsSalesPrices.add("");
                            textsSalesFreights.add("");

                            for (SDataDpsEntry entry : dps.getDbmsDpsEntries()) {
                                if (!entry.getIsDeleted()) {
                                    double price = entry.getOriginalPriceUnitaryCy();
                                    double salesPrice = entry.getSalesPriceUnitaryCy();
                                    double salesFreight = entry.getSalesFreightUnitaryCy();

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
                                    quantity = entry.getOriginalQuantity();
                                    unit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { entry.getFkOriginalUnitId() }, SLibConstants.EXEC_MODE_SILENT);
                                    
                                    textsUnits.add(SLibUtilities.translateUnitsToText(quantity, miClient.getSessionXXX().getParamsErp().getDecimalsQuantity(),
                                    dps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH : SLibConstants.LAN_ENGLISH, 
                                    unit.getUnit(), unit.getUnit()));
                                }
                            }

                            map.put("oVectorTextUnit", textsUnits);

                            bprBranchContact = bprBranch.getDbmsBizPartnerBranchContact(new int[] { dps.getFkContactBizPartnerBranchId_n(), dps.getFkContactContactId_n() });
                            
                            if (bprBranchContact == null) {
                                for (int i = 0; i < bprBranch.getDbmsBizPartnerBranchContacts().size(); i++) {
                                    if (bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFkContactTypeId() == SDataConstantsSys.BPSS_TP_CON_ADM && !bprBranch.getDbmsBizPartnerBranchContacts().get(i).getIsDeleted()) {
                                        textContact = bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname() +
                                                (bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname().length() > 0 ? " " : "") +
                                                bprBranch.getDbmsBizPartnerBranchContacts().get(i).getLastname();
                                    }

                                    if (!textContact.isEmpty()) {
                                        break;
                                    }
                                }
                            }
                            else {
                                textContact = bprBranchContact.getFirstname() + (bprBranchContact.getFirstname().length() > 0 ? " " : "") + bprBranchContact.getLastname();
                            }

                            map.put("sContact", textContact.isEmpty() ? bprBranch.getDbmsBizPartner() : textContact);
                            map.put("ctBpCus", SDataConstantsSys.BPSS_CT_BP_CUS);
                            map.put("ctBpSup", SDataConstantsSys.BPSS_CT_BP_SUP);
                            map.put("nFidCtBp", SLibUtilities.compareKeys(dps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_CON) ? SDataConstantsSys.BPSS_CT_BP_CUS : SDataConstantsSys.BPSS_CT_BP_SUP);
                            map.put("oDecimalFormat", miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormatFixed4());
                            map.put("oQuantityFormat", miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat());
                            map.put("oDateTextFormat", miClient.getSessionXXX().getFormatters().getDateTextFormat());
                            map.put("nFidTpCarBp", SModSysConsts.LOGS_TP_CAR_CAR);
                            map.put("nLogIncotermExw", SModSysConsts.LOGS_INC_EXW);
                            if (JOptionPane.showConfirmDialog(this, "Desea incluir una imagen en el reporte", "Cargar imagen", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                map.put("imgItem", "" + getRouteImgReport());
                            }
                            
                            map.put("bPrintTon", contractKgAsTon);

                            jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_EST, map);
                            jasperViewer = new JasperViewer(jasperPrint, false);
                            jasperViewer.setTitle("Impresión de cotización");
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
    
    private void actionPrintEnglish(final boolean contractKgAsTon) {
        if ((!contractKgAsTon && jbPrintEnglish.isEnabled()) || (contractKgAsTon && jbPrintContractKgAsTon.isEnabled())) {
            if (isRowSelected()) {
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (dps.getDbmsDataCfd() != null) {
                    try {
                        SCfdUtils.printCfdEnglish(miClient, dps.getDbmsDataCfd(), SLibConstants.UNDEFINED, SDataConstantsPrint.PRINT_MODE_VIEWER, 1, false);
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                }
                else {
                    SDataBizPartnerBranch comBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { dps.getFkCompanyBranchId() }, SLibConstants.EXEC_MODE_SILENT);
                    SDataBizPartnerBranch bprBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { dps.getFkBizPartnerBranchId() }, SLibConstants.EXEC_MODE_SILENT);
                    SDataBizPartnerBranchAddress bprBranchAddress = (SDataBizPartnerBranchAddress) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB_ADD, new int [] { dps.getFkBizPartnerBranchId(), dps.getFkBizPartnerBranchAddressId() }, SLibConstants.EXEC_MODE_SILENT);
                    SDataBizPartnerBranchContact comBranchContact = null;
                    SDataBizPartnerBranchContact bprBranchContact = null;
                    SDataCurrency currency = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR, new int[] { dps.getFkCurrencyId() }, SLibConstants.EXEC_MODE_SILENT);

                    int addressFormatType = 0;
                    boolean addCountry = false;
                    
                    if (bprBranch.getFkAddressFormatTypeId_n() != SLibConstants.UNDEFINED) {
                        addressFormatType = bprBranch.getFkAddressFormatTypeId_n();
                    }
                    else {
                        addressFormatType = miClient.getSessionXXX().getParamsCompany().getFkDefaultAddressFormatTypeId_n();
                    }
                    
                    if (bprBranchAddress.getFkCountryId_n() == comBranch.getDbmsBizPartnerBranchAddressOfficial().getFkCountryId_n()) {
                        addCountry = false;
                    }
                    else {
                        addCountry = true;
                    }

                    SDataBizPartner company = miClient.getSessionXXX().getCompany().getDbmsDataCompany();
                    String[] comAddressTexts = comBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_4ROWS, addCountry);
                    String[] bprAddressTexts = bprBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_4ROWS, addCountry);
                    String[] dvyAddressTexts = bprBranchAddress.obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_4ROWS, addCountry);

                    Cursor cursor = getCursor();
                    Map<String, Object> map = null;
                    JasperPrint jasperPrint = null;
                    JasperViewer jasperViewer = null;
                    
                    if (SLibUtilities.belongsTo(dps.getDpsTypeKey(), new int[][] { SDataConstantsSys.TRNU_TP_DPS_SAL_ORD, SDataConstantsSys.TRNU_TP_DPS_PUR_ORD })) {
                        // order:

                        if (SLibUtilities.compareKeys(dps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_PUR_ORD) && !dps.getIsAuthorized()) {
                            miClient.showMsgBoxWarning("No se puede imprimir el documento porque su estatus es:\n-" + dps.getDbmsAuthorizationStatusName() + ".");
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
                            
                            if (company.getFiscalId().equals(DCfdConsts.RFC_GEN_INT)) {
                                // international company:
                                
                                comAddressTexts = comBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_2ROWS, addCountry);
                                bprAddressTexts = bprBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_2ROWS, addCountry);
                                dvyAddressTexts = bprBranchAddress.obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_2ROWS, addCountry);
                                
                                SDataBizPartnerBranchContact bprBranchContactForPhone = company.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchContacts().get(0);
                                bprBranchContact = bprBranch.getDbmsBizPartnerBranchContacts().size() <= 1 ? null : bprBranch.getDbmsBizPartnerBranchContacts().get(1);

                                map = miClient.createReportParams();
                                map.put("nDpsYearId", dps.getPkYearId());
                                map.put("nDpsDocId", dps.getPkDocId());
                                map.put("sDpsNumber", dps.getDpsNumber());
                                map.put("tDpsDate", dps.getDate());
                                map.put("nDpsDueDays", dps.getDaysOfCredit());
                                
                                map.put("sDpsNote1", "If any fees are not received by Saporis International by the due date, those fees may accrue late interest at a rate of 1.5% of the outstanding balance per month or the maximum rate permitted by the law, whichever is lower.");
                                map.put("sDpsNote2", "<p><b>PLEASE WIRE TRANSFER TO THE FOLLOWING BANK ACCOUNT NUMBER</b></p>"
                                        + "<p>BANK OF AMERICA</p>"
                                        + "<p>ACCOUNT NAME: SAPORIS INTERNATIONAL INC</p>"
                                        + "<p>ACCOUNT NUMBER: 325143301493</p>"
                                        + "<p>ROUTING NUMBER: 121000358</p>"
                                        + "<p>WIRE: 026009593</p>"
                                        + "<br>"
                                        + "<p>PLEASE SEND CHECKS TO 1546 CHIA WAY LOS ANGELES CA 90041 USA</p>"
                                        + "<p>If you have any questions about this invoice, please contact: jacinta@simplefoods.mx</p>");
                                
                                map.put("sPONumber", dps.getNumberReference());
                                map.put("sONumber", "");
                                
                                map.put("sIssName", company.getBizPartner());
                                map.put("sIssAddress1", comAddressTexts[0]);
                                map.put("sIssAddress2", comAddressTexts[1]);
                                
                                SDataBizPartner customer = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { dps.getFkBizPartnerId_r() }, SLibConstants.EXEC_MODE_SILENT);
                                map.put("sCusNumber", customer.getDbmsCategorySettingsCus().getKey());
                                map.put("sCusName", customer.getBizPartner());
                                map.put("sCusAddress1", bprAddressTexts[0]);
                                map.put("sCusAddress2", bprAddressTexts[1]);
                                
                                map.put("sDvyName", customer.getBizPartner());
                                map.put("sDvyAddress1", dvyAddressTexts[0]);
                                map.put("sDvyAddress2", dvyAddressTexts[1]);
                                
                                map.put("sDvyPhone", bprBranchContactForPhone.getAuxTelephoneNumbers());
                                map.put("sDvyContact", bprBranchContact == null ? "" : bprBranchContact.getContact());
                                
                                map.put("sCurCode", miClient.getSession().getSessionCustom().getCurrencyCode(new int[] { dps.getFkCurrencyId() }));
                                
                                map.put("sValueText", SLibUtilities.translateValueToText(dps.getTotalCy_r(), 2,
                                    dps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                    SLibConstants.LAN_ENGLISH, currency.getTextSingular(), currency.getTextPlural(), currency.getTextPrefix(), currency.getTextSuffix()));

                                jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_DPS_US, map);
                                jasperViewer = new JasperViewer(jasperPrint, false);
                                jasperViewer.setTitle("Invoice printing");
                                jasperViewer.setVisible(true);
                            }
                            else {
                                // national company:
                                
                                comBranchContact = company.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchContacts().size() <= 1 ? null :
                                        company.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchContacts().get(1);

                                map = miClient.createReportParams();
                                map.put("nIdYear", dps.getPkYearId());
                                map.put("nIdDoc", dps.getPkDocId());
                                map.put("sAddressLine1", bprAddressTexts[0]);
                                map.put("sAddressLine2", bprAddressTexts[1]);
                                map.put("sAddressLine3", bprAddressTexts[2]);
                                map.put("sAddressLine4", bprAddressTexts.length > 3 ? bprAddressTexts[3] : "");
                                map.put("sAddressDelivery1", dvyAddressTexts[0].compareTo(bprAddressTexts[0]) == 0 ? "": dvyAddressTexts[0]);
                                map.put("sAddressDelivery2", dvyAddressTexts[1].compareTo(bprAddressTexts[1]) == 0 ? "DOMICILIO DEL CLIENTE": dvyAddressTexts[1]);
                                map.put("sAddressDelivery3", dvyAddressTexts[2].compareTo(bprAddressTexts[2]) == 0 ? "": dvyAddressTexts[2]);
                                map.put("sAddressDelivery4", dvyAddressTexts.length > 3 && bprAddressTexts.length > 3 ?
                                    dvyAddressTexts[3].compareTo(bprAddressTexts[3]) == 0 ? "" : dvyAddressTexts[3] : "");
                                map.put("nBizPartnerCategory", SDataConstantsSys.BPSS_CT_BP_CUS);
                                map.put("sValueText", SLibUtilities.translateValueToText(dps.getTotalCy_r(), 2,
                                    dps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                    SLibConstants.LAN_ENGLISH, currency.getTextSingular(), currency.getTextPlural(), currency.getTextPrefix(), currency.getTextSuffix()));
                                map.put("sErpCurrencyKey", miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
                                map.put("bShowBackground", false);
                                map.put("sManagerFinance", comBranchContact == null ? "" : SLibUtilities.textTrim(comBranchContact.getContactPrefix() + " " + comBranchContact.getFirstname() + " " + comBranchContact.getLastname()));

                                jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_DPS, map);
                                jasperViewer = new JasperViewer(jasperPrint, false);
                                jasperViewer.setTitle("Impresión de factura");
                                jasperViewer.setVisible(true);
                            }
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
                            
                            comBranchContact = company.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchContacts().size() <= 1 ? null :
                                    company.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchContacts().get(1);

                            map = miClient.createReportParams();
                            map.put("nIdYear", dps.getPkYearId());
                            map.put("nIdDoc", dps.getPkDocId());
                            map.put("sAddressLine1", bprAddressTexts[0]);
                            map.put("sAddressLine2", bprAddressTexts[1]);
                            map.put("sAddressLine3", bprAddressTexts[2]);
                            map.put("sAddressLine4", bprAddressTexts.length > 3 ? bprAddressTexts[3] : "");
                            map.put("nBizPartnerCategory", SDataConstantsSys.BPSS_CT_BP_CUS);
                            map.put("sValueText", SLibUtilities.translateValueToText(dps.getTotalCy_r(), 2,
                                dps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                SLibConstants.LAN_ENGLISH, currency.getTextSingular(), currency.getTextPlural(), currency.getTextPrefix(), currency.getTextSuffix()));
                            map.put("sErpCurrencyKey", miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
                            map.put("bShowBackground", false);
                            map.put("sManagerFinance", comBranchContact == null ? "" : SLibUtilities.textTrim(comBranchContact.getContactPrefix() + " " + comBranchContact.getFirstname() + " " + comBranchContact.getLastname()));

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
                                map.put("sAddressLine1", bprAddressTexts[0]);
                                map.put("sAddressLine2", bprAddressTexts[1]);
                                map.put("sAddressLine3", bprAddressTexts[2]);
                                map.put("sAddressLine4", bprAddressTexts.length > 3 ? bprAddressTexts[3] : "");
                            }
                            else {
                                String[] comBranchAddressTexts = miClient.getSessionXXX().getCurrentCompanyBranch().getDbmsBizPartnerBranchAddressOfficial().obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_4ROWS, addCountry);
                                map.put("sAddressLine1", comBranchAddressTexts[0] == null || comBranchAddressTexts[0].isEmpty() ? "": comBranchAddressTexts[0]);
                                map.put("sAddressLine2", comBranchAddressTexts[1] == null || comBranchAddressTexts[1].isEmpty() ? "": comBranchAddressTexts[1]);
                                map.put("sAddressLine3", comBranchAddressTexts[2] == null || comBranchAddressTexts[2].isEmpty() ? "": comBranchAddressTexts[2]);
                                map.put("sAddressLine4", comBranchAddressTexts.length > 3 ? comBranchAddressTexts[3] : "");
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
                            
                            bprBranchContact = bprBranch.getDbmsBizPartnerBranchContact(new int[] { dps.getFkContactBizPartnerBranchId_n(), dps.getFkContactContactId_n() });
                            
                            if (bprBranchContact == null) {
                                for (int i = 0; i < bprBranch.getDbmsBizPartnerBranchContacts().size(); i++) {
                                    if (bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFkContactTypeId() == SDataConstantsSys.BPSS_TP_CON_PUR && !bprBranch.getDbmsBizPartnerBranchContacts().get(i).getIsDeleted()) {
                                        textContact = bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname() +
                                                (bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname().length() > 0 ? " " : "") +
                                                bprBranch.getDbmsBizPartnerBranchContacts().get(i).getLastname();
                                    }
                                    else if (bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFkContactTypeId() == SDataConstantsSys.BPSS_TP_CON_ADM && !bprBranch.getDbmsBizPartnerBranchContacts().get(i).getIsDeleted()) {
                                        textContact = bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname() +
                                                (bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname().length() > 0 ? " " : "") +
                                                bprBranch.getDbmsBizPartnerBranchContacts().get(i).getLastname();
                                    }

                                    if (!textContact.isEmpty()) {
                                        break;
                                    }
                                }
                            }
                            else {
                                textContact = bprBranchContact.getFirstname() + (bprBranchContact.getFirstname().length() > 0 ? " " : "") + bprBranchContact.getLastname();
                            }

                            map.put("sContact", textContact.isEmpty() ? bprBranch.getDbmsBizPartner() : textContact);
                            map.put("ctBpCus", SDataConstantsSys.BPSS_CT_BP_CUS);
                            map.put("ctBpSup", SDataConstantsSys.BPSS_CT_BP_SUP);
                            map.put("nFidCtBp", SLibUtilities.compareKeys(dps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_CON) ? SDataConstantsSys.BPSS_CT_BP_CUS : SDataConstantsSys.BPSS_CT_BP_SUP);
                            map.put("oDecimalFormat", miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormatFixed4());
                            map.put("oQuantityFormat", miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat());
                            map.put("oDateTextFormat", miClient.getSessionXXX().getFormatters().getDateTextFormat());
                            map.put("nFidTpCarBp", SModSysConsts.LOGS_TP_CAR_CAR);
                            map.put("nLogIncotermExw", SModSysConsts.LOGS_INC_EXW);
                            map.put("bPrintTon", contractKgAsTon);
                            
                            boolean hasAnalysis = false;
                            boolean isPrint = true;
                            if (! dps.getDbmsDpsEntries().isEmpty() && SLibUtilities.compareKeys(dps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_CON)) {
                                for (int i = 0; i < dps.getDbmsDpsEntries().size(); i++) {
                                    int[] pkEty = new int[] { dps.getDbmsDpsEntries().get(i).getPkYearId(), dps.getDbmsDpsEntries().get(i).getPkDocId(), dps.getDbmsDpsEntries().get(i).getPkEntryId() };
                                    hasAnalysis = ! SDpsQualityUtils.getAnalysisByDocumentEty(miClient.getSession(), dps.getDbmsDpsEntries().get(i).getFkItemId(), pkEty, isPrint).isEmpty();
                                    if (hasAnalysis) {
                                        break;
                                    }
                                }
                            }
                            
                            map.put("bIsSalesContract", hasAnalysis);

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
                    else if (SLibUtilities.compareKeys(dps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_EST)) {
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
                            miClient.getSessionXXX().getCurrentCompany().getDbmsDataCompany().getDbmsBizPartnerBranches();
                            
                            for (int i = 0; i < company.getDbmsBizPartnerBranches().size(); i++ ) {
                                if (company.getDbmsBizPartnerBranches().get(i).getPkBizPartnerBranchId() == dps.getFkCompanyBranchId()) {
                                    comAddressTexts = company.getDbmsBizPartnerBranches().get(i).getDbmsBizPartnerBranchAddresses().get(0).obtainAddress(addressFormatType, SDataBizPartnerBranchAddress.ADDRESS_4ROWS, addCountry);
                                    map.put("sAddressLine1", comAddressTexts[0] == null || comAddressTexts[0].isEmpty() ? "": comAddressTexts[0]);
                                    map.put("sAddressLine2", comAddressTexts[1] == null || comAddressTexts[1].isEmpty() ? "": comAddressTexts[1]);
                                    map.put("sAddressLine3", comAddressTexts[2].toUpperCase() == null || comAddressTexts[2].isEmpty() ? "": comAddressTexts[2].toUpperCase());
                                    map.put("sAddressLine4", comAddressTexts.length > 3 ? comAddressTexts[3] : "");
                                    map.put("sFiscalIdBp", miClient.getSessionXXX().getCurrentCompany().getDbmsDataCompany().getFiscalId());
                                    map.put("sTelephone", company.getDbmsBizPartnerBranches().get(i).getDbmsBizPartnerBranchContacts().get(0).getAuxTelephoneNumbers());
                                    break;
                                }
                            }

                            textsPrices.add("");
                            textsSalesPrices.add("");
                            textsSalesFreights.add("");

                            for (SDataDpsEntry entry : dps.getDbmsDpsEntries()) {
                                if (!entry.getIsDeleted()) {
                                    double price = entry.getOriginalPriceUnitaryCy();
                                    double salesPrice = entry.getSalesPriceUnitaryCy();
                                    double salesFreight = entry.getSalesFreightUnitaryCy();

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
                                    quantity = entry.getOriginalQuantity();
                                    unit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { entry.getFkOriginalUnitId() }, SLibConstants.EXEC_MODE_SILENT);
                                    
                                    textsUnits.add(SLibUtilities.translateUnitsToText(quantity, miClient.getSessionXXX().getParamsErp().getDecimalsQuantity(),
                                    dps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH : SLibConstants.LAN_ENGLISH, 
                                    unit.getUnit(), unit.getUnit()));
                                }
                            }

                            map.put("oVectorTextUnit", textsUnits);

                            bprBranchContact = bprBranch.getDbmsBizPartnerBranchContact(new int[] { dps.getFkContactBizPartnerBranchId_n(), dps.getFkContactContactId_n() });
                            
                            if (bprBranchContact == null) {
                                for (int i = 0; i < bprBranch.getDbmsBizPartnerBranchContacts().size(); i++) {
                                    if (bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFkContactTypeId() == SDataConstantsSys.BPSS_TP_CON_ADM && !bprBranch.getDbmsBizPartnerBranchContacts().get(i).getIsDeleted()) {
                                        textContact = bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname() +
                                                (bprBranch.getDbmsBizPartnerBranchContacts().get(i).getFirstname().length() > 0 ? " " : "") +
                                                bprBranch.getDbmsBizPartnerBranchContacts().get(i).getLastname();
                                    }

                                    if (!textContact.isEmpty()) {
                                        break;
                                    }
                                }
                            }
                            else {
                                textContact = bprBranchContact.getFirstname() + (bprBranchContact.getFirstname().length() > 0 ? " " : "") + bprBranchContact.getLastname();
                            }

                            map.put("sContact", textContact.isEmpty() ? bprBranch.getDbmsBizPartner() : textContact);
                            map.put("ctBpCus", SDataConstantsSys.BPSS_CT_BP_CUS);
                            map.put("ctBpSup", SDataConstantsSys.BPSS_CT_BP_SUP);
                            map.put("nFidCtBp", SLibUtilities.compareKeys(dps.getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_CON) ? SDataConstantsSys.BPSS_CT_BP_CUS : SDataConstantsSys.BPSS_CT_BP_SUP);
                            map.put("oDecimalFormat", miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormatFixed4());
                            map.put("oQuantityFormat", miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat());
                            map.put("oDateTextFormat", miClient.getSessionXXX().getFormatters().getDateTextFormat());
                            map.put("nFidTpCarBp", SModSysConsts.LOGS_TP_CAR_CAR);
                            map.put("nLogIncotermExw", SModSysConsts.LOGS_INC_EXW);
                            if (JOptionPane.showConfirmDialog(this, "Desea incluir una imagen en el reporte", "Cargar imagen", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                map.put("imgItem", "" + getRouteImgReport());
                            }
                            
                            map.put("bPrintTon", contractKgAsTon);

                            jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_EST, map);
                            jasperViewer = new JasperViewer(jasperPrint, false);
                            jasperViewer.setTitle("Impresión de cotización");
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

    private void actionPrintContractMoves() {
        if (jbPrintContractMoves.isEnabled()) {
            if (isRowSelected()) {
                STrnUtilities.createReportContractAnalysis(miClient, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
            }
        }
    }

    private void actionPrintOrderGoods() {
        if (jbPrintOrderGoods.isEnabled()) {
            if (isRowSelected()) {
                String sUserBuyer = "";
                String sUserAuthorize = "";
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
                SDataUser oUserBuyer = null;
                SDataUser oUserAuthorize = null;
                
                oDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                oCompanyBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { oDps.getFkCompanyBranchId() }, SLibConstants.EXEC_MODE_SILENT);
                oBizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { oDps.getFkBizPartnerBranchId() }, SLibConstants.EXEC_MODE_SILENT);
                oAddress = (SDataBizPartnerBranchAddress) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB_ADD, new int [] { oDps.getFkBizPartnerBranchId(),
                    oDps.getFkBizPartnerBranchAddressId() }, SLibConstants.EXEC_MODE_SILENT);

                addressOficial = oBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(miClient.getSessionXXX().getParamsErp().getFkLanguageId(),
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
            if (isRowSelected()) {
                String[] addressOficial = null;
                String[] addressDelivery = null;
                Cursor cursor = getCursor();
                Map<String, Object> map = null;
                JasperPrint jasperPrint = null;
                JasperViewer jasperViewer = null;
                SDataDps oDps = null;
                SDataBizPartnerBranch oCompanyBranch = null;
                SDataBizPartnerBranch oBizPartnerBranch = null;
                SDataBizPartnerBranchAddress oAddress = null;
                SDataCurrency oCurrency = null;

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
            if (isRowSelected()) {
                try {
                    SDataCfd cfd = SCfdUtils.getCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_INV, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                    SCfdUtils.downloadXmlCfd(miClient, cfd);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }
    
    private void actionGetPdf() {
        if (jbGetPdf.isEnabled()) {
            if (isRowSelected()) {
                try {
                    SCfdUtils.downloadXmlPdf(miClient, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }
    
    private void actionPrintAcknowledgmentCancellation() {
        if (jbPrintAcknowledgmentCancellation.isEnabled()) {
            if (isRowSelected()) {
                try {
                    SDataCfd cfd = SCfdUtils.getCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_INV, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                    SCfdUtils.printCfdCancelAck(miClient, cfd, 0, SDataConstantsPrint.PRINT_MODE_VIEWER);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }
    
    private void actionShowCfdi() {
        if(jbShowCfdi.isEnabled()) {
            if (isRowSelected()) {
                try {
                    SDataCfd cfd = SCfdUtils.getCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_INV, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                    if (cfd == null || cfd.getDocXml().isEmpty() || cfd.getDocXmlName().isEmpty()) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento.");
                    }
                    else {
                        SCfdRenderer renderer = new SCfdRenderer(miClient);
                        renderer.showCfdi(cfd.getDocXml());
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionGetAcknowledgmentCancellation() {
        if (jbGetAcknowledgmentCancellation.isEnabled()) {
            if (isRowSelected()) {
                try {
                    SDataCfd cfd = SCfdUtils.getCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_INV, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
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
           if (isRowSelected()) {
                boolean canEmitCompMonExt = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_COMP_MON_EXT).HasRight;
                boolean canEmitCompSignRestricted = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_COMP_SIGN_RESTRICT).HasRight;
                boolean canEmitCompSignImmex = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_COMP_SIGN_RESTRICT).HasRight;
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                SDataCustomerConfig customerConfig = (SDataCustomerConfig) SDataUtilities.readRegistry(miClient, SDataConstants.MKT_CFG_CUS, new int[] { dps.getFkBizPartnerId_r() } , SLibConstants.EXEC_MODE_SILENT);
                
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
                else if (customerConfig != null && customerConfig.getIsSignRestricted() && !canEmitCompSignRestricted) {
                    miClient.showMsgBoxWarning("El usuario '" + miClient.getSession().getUser().getName() + "' no puede emitir comprobantes de clientes restringidos.");
                }
                else if (customerConfig != null && customerConfig.getIsSignImmex()&& !canEmitCompSignImmex) {
                    miClient.showMsgBoxWarning("El usuario '" + miClient.getSession().getUser().getName() + "' no puede emitir comprobantes de clientes IMMEX.");
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
                                             SCfdUtils.computeCfdInvoice(miClient, dps, SDataConstantsSys.TRNS_TP_XML_CFD);
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
                        case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                            try {
                                if (((SClientInterface) miClient).getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticSal()) {
                                    if (SCfdUtils.signAndSendCfdi(miClient, dps.getDbmsDataCfd(), 0, true, true)) {
                                        miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                                    }
                                }
                                else {
                                    if (SCfdUtils.signCfdi(miClient, dps.getDbmsDataCfd(), 0)) {
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
        if (jbValidateCfdi.isEnabled()) {
           if (isRowSelected()) {
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (dps.getIsDeleted()) {
                    miClient.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' está eliminado.");
                }
                else if (dps.getDbmsDataCfd() == null) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el registro CFD del documento '" + dps.getDpsNumber() + "'.");
                }
                else {
                    Exception exception = null;
                    
                    try {
                        SGuiUtils.setCursorWait((SGuiClient) miClient);
                        
                        if (SCfdUtils.validateCfdi(miClient, dps.getDbmsDataCfd(), 0, true)) {
                            miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                        }
                    }
                    catch(Exception e) {
                        exception = e;
                    }
                    finally {
                        SGuiUtils.setCursorDefault((SGuiClient) miClient);
                    }
                    
                    if (exception != null) {
                        throw exception;
                    }
                }
            }
        }
    }
    
    private void actionGetCfdiStatus() throws Exception {
        if (jbGetCfdiStatus.isEnabled()) {
            if (isRowSelected()) {
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (dps.getIsDeleted()) {
                    miClient.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' está eliminado.");
                }
                else if (dps.getDbmsDataCfd() == null) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el registro CFD del documento '" + dps.getDpsNumber() + "'.");
                }
                else {
                    if (!dps.getDbmsDataCfd().isStamped()) {
                        miClient.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' no está timbrado.");
                    }
                    else {
                        try {
                            miClient.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
                            
                            SDataCfd cfd = dps.getDbmsDataCfd();
                            miClient.showMsgBoxInformation(new SCfdUtilsHandler(miClient).getCfdiSatStatus(cfd).getDetailedStatus());
                        }
                        catch (Exception e) {
                            SLibUtilities.renderException(this, e);
                        }
                        finally {
                            miClient.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        }
                    }
                }
            }
        }
    }

    private void actionSendCfdi() {
        if (jbSendCfdi.isEnabled()) {
            if (isRowSelected()) {
                try {
                    switch (mnTabTypeAux02) {
                        case SDataConstantsSys.TRNX_TP_DPS_DOC:
                        case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                            SDataCfd cfd = SCfdUtils.getCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_INV, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                            SCfdUtils.sendCfd(miClient, cfd, 0, true);
                            break;
                            
                        case SDataConstantsSys.TRNX_TP_DPS_ORD:
                            STrnUtilities.sendDps(miClient, mnTabTypeAux01, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey(), true);
                            break;
                            
                        default:
                            // do nothing
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionRestoreCfdStamped() throws Exception {
        if (jbRestoreCfdStamped.isEnabled()) {
            if (isRowSelected()) {
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (dps.getIsDeleted()) {
                    miClient.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' está eliminado.");
                }
                else {
                    boolean needUpdate = SCfdUtils.restoreCfdStamped(miClient, dps.getDbmsDataCfd(), 0, true);

                    if (needUpdate) {
                        miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                    }
                }
           }
        }
    }

    private void actionRestoreCfdCancelAck() throws Exception {
        if (jbRestoreCfdCancelAck.isEnabled()) {
           if (isRowSelected()) {
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (dps.getIsDeleted()) {
                    miClient.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' está eliminado.");
                }
                else {
                    boolean needUpdate = SCfdUtils.restoreCfdCancelAck(miClient, dps.getDbmsDataCfd(), 0, true);

                    if (needUpdate) {
                        miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                    }
                }
            }
        }
    }

    private void actionResetPacFlags() throws Exception {
        if (jbResetPacFlags.isEnabled()) {
           if (isRowSelected()) {
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

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
        String sqlSeries = "";
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
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (!((String) setting.getSetting()).isEmpty()) {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
        }
        
        boolean dnsRight = false; 
        if (mbIsOrd) {
            if (mbIsCategoryPur) {
                dnsRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD_ALL_DNS).HasRight;
            }
            else if (mbIsCategorySal) {
                dnsRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD_ALL_DNS).HasRight;
            }
            if (!dnsRight) {
                ArrayList<SDataUserDnsDps> usrDnsDpss = miClient.getSessionXXX().getUser().getDbmsConfigurationTransaction().getUserDnsDps();
                if (!usrDnsDpss.isEmpty()) {
                    for (SDataUserDnsDps usrDnsDps : usrDnsDpss) {
                        sqlSeries += sqlSeries.isEmpty() ? "(" : "OR ";
                        sqlSeries += "d.num_ser = '" + usrDnsDps.getDocumentNumberSeries().getDocNumberSeries() + "' ";
                    }
                    sqlSeries += ") ";
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
                "IF((x.ts IS NULL OR doc_xml = '') AND p.doc_pdf_name IS NULL, " + STableConstants.ICON_NULL  + ", " + // not is CFD nor CFDI
                "IF((x.ts IS NULL OR doc_xml = '') AND p.doc_pdf_name IS NOT NULL, " + STableConstants.ICON_PDF + ", " + 
                "IF((x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_CFD + " OR x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_NA + ") AND p.doc_pdf_name IS NULL , " + STableConstants.ICON_XML + ", " + // is CFD
                "IF((x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_CFD + " OR x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_NA + ") AND p.doc_pdf_name IS NOT NULL , " + STableConstants.ICON_XML_PDF + ", " + // is CFD
                "IF(x.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " OR LENGTH(uuid) = 0, " + STableConstants.ICON_XML_PEND + ", " + // CFDI pending sign
                "IF(LENGTH(xc.ack_can_xml) = 0 AND xc.ack_can_pdf_n IS NULL, " + STableConstants.ICON_XML_SIGN  + ", " + // CFDI signed, canceled only SIIE
                "IF(LENGTH(xc.ack_can_xml) != 0, " + STableConstants.ICON_XML_CANC_XML + ", " +  // CFDI canceled with cancellation acknowledgment in XML format
                "IF(xc.ack_can_pdf_n IS NOT NULL, " + STableConstants.ICON_XML_CANC_PDF + ", " + // CFDI canceled with cancellation acknowledgment in PDF format
                STableConstants.ICON_XML_SIGN + " " + // CFDI signed, canceled only SIIE
                ")))))))) AS f_ico_xml, " +
                "x.ts_prc, x.can_st, " + // cancellation status
                "bp.id_bp, bp.bp, bpc.bp_key, bpb.id_bpb, bpb.bpb, " +
                "(SELECT c.cur_key FROM erp.cfgu_cur AS c WHERE d.fid_cur = c.id_cur) AS f_cur_key, " +
                "'" + miClient.getSession().getSessionCustom().getLocalCurrencyCode() + "' AS f_cur_key_local, " +
                "(SELECT cob.code FROM erp.bpsu_bpb AS cob WHERE d.fid_cob = cob.id_bpb) AS f_cob_code, ul.usr, uc.usr, un.usr, ue.usr, ud.usr, " +
                "CASE d.fid_st_dps_authorn " +
                "WHEN " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_PENDING + " THEN " + STableConstants.ICON_ST_WAIT + " " +
                "WHEN " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN + " THEN " + STableConstants.ICON_ST_THUMBS_UP + " " +
                "WHEN " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_REJECT + " THEN " + STableConstants.ICON_ST_THUMBS_DOWN + " " +
                "ELSE " + STableConstants.ICON_NULL + " END AS f_status ";

        if (mbIsDoc || mbIsDocAdj) {
            msSql += ", xu.usr, " +
                    "(SELECT rbkc.code FROM fin_bkc AS rbkc WHERE rbkc.id_bkc = r.id_bkc) AS f_rbkc_code, " +
                    "(SELECT rbpb.code FROM erp.bpsu_bpb AS rbpb WHERE rbpb.id_bpb = r.fid_cob) AS f_rbpb_code, " +
                    "CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, 2)) as f_rper, " +
                    "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) as f_rnum ";
        }

        msSql +=
                "FROM trn_dps AS d " +
                "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bpc ON bp.id_bp = bpc.id_bp AND bpc.id_ct_bp = " + (mbIsCategoryPur ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
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

        String complementaryDbName = "";
        
        try {
            complementaryDbName = SClientUtils.getComplementaryDdName((SClientInterface) miClient);
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
            
        msSql +=
                "INNER JOIN erp.usru_usr AS ul ON d.fid_usr_link = ul.id_usr " +
                "INNER JOIN erp.usru_usr AS uc ON d.fid_usr_close = uc.id_usr " +
                "INNER JOIN erp.usru_usr AS un ON d.fid_usr_new = un.id_usr " + (mbHasRightAuthor ? " AND d.fid_usr_new = " + miClient.getSession().getUser().getPkUserId() + " " : "") +
                "INNER JOIN erp.usru_usr AS ue ON d.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON d.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN trn_cfd AS x ON d.id_year = x.fid_dps_year_n AND d.id_doc = x.fid_dps_doc_n " + 
                "LEFT OUTER JOIN " + complementaryDbName + ".trn_cfd AS xc ON x.id_cfd = xc.id_cfd " +
                "LEFT OUTER JOIN " + complementaryDbName + ".trn_pdf AS p ON d.id_year = p.id_year AND d.id_doc = p.id_doc " +
                "LEFT OUTER JOIN erp.usru_usr AS xu ON x.fid_usr_prc = xu.id_usr ";

        if (mbIsDoc || mbIsDocAdj) {
            msSql +=
                    "LEFT OUTER JOIN trn_dps_rec AS dr ON d.id_year = dr.id_dps_year AND d.id_doc = dr.id_dps_doc " +
                    "LEFT OUTER JOIN fin_rec AS r ON dr.fid_rec_year = r.id_year AND dr.fid_rec_per = r.id_per AND dr.fid_rec_bkc = r.id_bkc AND dr.fid_rec_tp_rec = r.id_tp_rec AND dr.fid_rec_num = r.id_num ";
        }

        msSql += (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere);
        
        msSql += (sqlSeries.isEmpty() ? "" : (sqlWhere.isEmpty() ? "WHERE " : "AND ") + sqlSeries);

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
                else if (button == jbImportMatRequest){
                    actionImportMaterialRequest();
                }
                else if (button == jbChangeDpsEntryItem) {
                    actionChangeDpsEntryItem();
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
                else if (button == jbChangeDpsDate) {
                    actionChangeDpsDate();
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
                else if (button == jbPrintEnglish) {
                    actionPrintEnglish(false);
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
                else if (button == jbGetPdf) {
                    actionGetPdf();
                }
                else if (button == jbPrintAcknowledgmentCancellation) {
                    actionPrintAcknowledgmentCancellation();
                }
                else if (button == jbShowCfdi) {
                    actionShowCfdi();
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
                else if (button == jbRestoreCfdStamped) {
                    actionRestoreCfdStamped();
                }
                else if (button == jbRestoreCfdCancelAck) {
                    actionRestoreCfdCancelAck();
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

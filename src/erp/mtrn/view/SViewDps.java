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
import erp.mod.SModSysConsts;
import erp.mtrn.data.SCfdPrint;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.STrnUtilities;
import erp.mtrn.form.SDialogAnnulCfdi;
import erp.mtrn.form.SDialogContractAnalysis;
import erp.mtrn.form.SDialogDpsFinder;
import erp.mtrn.form.SDialogUpdateDpsLogistics;
import erp.mtrn.form.SDialogUpdateDpsReferenceComms;
import erp.musr.data.SDataUser;
import erp.print.SDataConstantsPrint;
import erp.table.SFilterConstants;
import erp.table.STabFilterDocumentNature;
import erp.table.STabFilterUsers;
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
import sa.gui.util.SUtilConsts;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Sergio Flores
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
    private javax.swing.JButton jbDelivery;
    private javax.swing.JButton jbReferenceCommissions;
    private javax.swing.JButton jbViewNotes;
    private javax.swing.JButton jbViewLinks;
    private javax.swing.JButton jbViewContractAnalysis;
    private javax.swing.JButton jbViewAccountingRecord;
    private javax.swing.JButton jbViewAccountingDetailsDps;
    private javax.swing.JButton jbViewAccountingDetailsBizPartner;
    private javax.swing.JButton jbPrint;
    private javax.swing.JButton jbPrintAcknowledgmentCancellation;
    private javax.swing.JButton jbPrintPhotoInvoice;
    private javax.swing.JButton jbPrintContract;
    private javax.swing.JButton jbPrintContractMoves;
    private javax.swing.JButton jbPrintOrderGoods;
    private javax.swing.JButton jbGetXml;
    private javax.swing.JButton jbGetAcknowledgmentCancellation;
    private javax.swing.JButton jbSignXml;
    private javax.swing.JButton jbVerifyCfdi;
    private javax.swing.JButton jbSend;
    private javax.swing.JButton jbDiactivateFlags;
    private javax.swing.JButton jbRestoreSignXml;
    private javax.swing.JButton jbRestoreAcknowledgmentCancellation;
    private erp.table.STabFilterUsers moTabFilterUser;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.table.STabFilterDocumentNature moTabFilterDocumentNature;
    private erp.mtrn.form.SDialogUpdateDpsLogistics moDialogUpdateDpsLogistics;
    private erp.mtrn.form.SDialogUpdateDpsReferenceComms moDialogUpdateDpsRefCommissions;
    private erp.mtrn.form.SDialogContractAnalysis moDialogContractAnalysis;
    private erp.mtrn.form.SDialogDpsFinder moDialogDpsFinder;
    private erp.mfin.form.SDialogAccountingMoveDpsBizPartner moDialogAccountingMoveDpsBizPartner;
    private erp.mtrn.form.SDialogAnnulCfdi moDialogAnnulCfdi;
    private erp.mtrn.data.SCfdPrint moCfdPrint;

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

        jbImport = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_IMPORT));
        jbImport.setPreferredSize(new Dimension(23, 23));
        jbImport.addActionListener(this);
        jbImport.setToolTipText("Importar documento");

        jbCopy = new JButton(miClient.getImageIcon(SLibConstants.ICON_COPY));
        jbCopy.setPreferredSize(new Dimension(23, 23));
        jbCopy.addActionListener(this);
        jbCopy.setToolTipText("Copiar documento");

        jbDelivery = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_DELIVERY));
        jbDelivery.setPreferredSize(new Dimension(23, 23));
        jbDelivery.addActionListener(this);
        jbDelivery.setToolTipText("Actualizar fechas de entrega del documento");

        jbReferenceCommissions = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_DELIVERY));
        jbReferenceCommissions.setPreferredSize(new Dimension(23, 23));
        jbReferenceCommissions.addActionListener(this);
        jbReferenceCommissions.setToolTipText("Actualizar referencia comisiones");

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

        jbPrintAcknowledgmentCancellation = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT_ACK_CAN));
        jbPrintAcknowledgmentCancellation.setPreferredSize(new Dimension(23, 23));
        jbPrintAcknowledgmentCancellation.addActionListener(this);
        jbPrintAcknowledgmentCancellation.setToolTipText("Imprimir acuse de cancelación");

        jbPrintPhotoInvoice = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT_PHOTO));
        jbPrintPhotoInvoice.setPreferredSize(new Dimension(23, 23));
        jbPrintPhotoInvoice.addActionListener(this);
        jbPrintPhotoInvoice.setToolTipText("Imprimir foto factura");

        jbPrintContract = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));
        jbPrintContract.setPreferredSize(new Dimension(23, 23));
        jbPrintContract.addActionListener(this);
        jbPrintContract.setToolTipText("Imprimir documento (ton)");

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

        jbVerifyCfdi = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif")));
        jbVerifyCfdi.setPreferredSize(new Dimension(23, 23));
        jbVerifyCfdi.addActionListener(this);
        jbVerifyCfdi.setToolTipText("Verificar timbrado o cancelación del CFDI");
        
        jbSend = new JButton(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")));
        jbSend.setPreferredSize(new Dimension(23, 23));
        jbSend.addActionListener(this);
        jbSend.setToolTipText("Enviar comprobante");

        jbRestoreSignXml = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_insert.gif")));
        jbRestoreSignXml.setPreferredSize(new Dimension(23, 23));
        jbRestoreSignXml.addActionListener(this);
        jbRestoreSignXml.setToolTipText("Insertar XML timbrado del CFDI");

        jbRestoreAcknowledgmentCancellation = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_insert.gif")));
        jbRestoreAcknowledgmentCancellation.setPreferredSize(new Dimension(23, 23));
        jbRestoreAcknowledgmentCancellation.addActionListener(this);
        jbRestoreAcknowledgmentCancellation.setToolTipText("Insertar PDF del acuse de cancelación del CFDI");

        jbDiactivateFlags = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif")));
        jbDiactivateFlags.setPreferredSize(new Dimension(23, 23));
        jbDiactivateFlags.addActionListener(this);
        jbDiactivateFlags.setToolTipText("Limpiar inconsistencias del timbrado o cancelación del CFDI");

        moTabFilterUser = new STabFilterUsers(miClient, this);
        moTabFilterUser.removeButtonUser();
        moTabFilterUser.setUserId(mbHasRightAuthor ? miClient.getSession().getUser().getPkUserId() : SDataConstantsSys.UNDEFINED);

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, mbIsEstCon ? SLibConstants.GUI_DATE_AS_YEAR : SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moTabFilterDocumentNature = new STabFilterDocumentNature(miClient, this, SDataConstants.TRNU_DPS_NAT);
        moDialogUpdateDpsLogistics = new SDialogUpdateDpsLogistics(miClient);
        moDialogUpdateDpsRefCommissions = new SDialogUpdateDpsReferenceComms(miClient);
        moDialogContractAnalysis = new SDialogContractAnalysis(miClient);
        moDialogAccountingMoveDpsBizPartner = new SDialogAccountingMoveDpsBizPartner(miClient, mnTabTypeAux01);
        moDialogAnnulCfdi = new SDialogAnnulCfdi(miClient, true);
        moCfdPrint = new SCfdPrint(miClient);

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
        addTaskBarUpperComponent(jbImport);
        addTaskBarUpperComponent(jbCopy);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbDelivery);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbReferenceCommissions);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbViewNotes);
        addTaskBarUpperComponent(jbViewLinks);
        addTaskBarUpperComponent(jbViewContractAnalysis);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbViewAccountingRecord);
        addTaskBarUpperComponent(jbViewAccountingDetailsDps);
        addTaskBarUpperComponent(jbViewAccountingDetailsBizPartner);
        addTaskBarLowerComponent(jbPrint);
        addTaskBarLowerComponent(jbPrintAcknowledgmentCancellation);
        addTaskBarLowerComponent(jbPrintPhotoInvoice);
        addTaskBarLowerComponent(jbPrintContract);
        addTaskBarLowerComponent(jbPrintContractMoves);
        addTaskBarLowerComponent(jbPrintOrderGoods);
        addTaskBarLowerComponent(jbGetXml);
        addTaskBarLowerComponent(jbGetAcknowledgmentCancellation);
        addTaskBarLowerComponent(jbSignXml);
        addTaskBarLowerComponent(jbVerifyCfdi);
        addTaskBarLowerComponent(jbSend);
        addTaskBarLowerComponent(jbRestoreSignXml);
        addTaskBarLowerComponent(jbRestoreAcknowledgmentCancellation);
        addTaskBarLowerComponent(jbDiactivateFlags);
        addTaskBarLowerSeparator();
        addTaskBarLowerComponent(moTabFilterUser);
        addTaskBarLowerSeparator();
        addTaskBarLowerComponent(moTabFilterDocumentNature);

        jbNew.setEnabled(mbHasRightNew);
        jbEdit.setEnabled(true);
        jbDelete.setEnabled(mbHasRightDelete);
        jbAnnul.setEnabled(mbHasRightAnnul && mbHasRightEdit && (mbIsDoc || mbIsDocAdj));
        jbImport.setEnabled(mbHasRightNew && createImportFinder);
        jbCopy.setEnabled(mbHasRightNew && !mbIsDocAdj);
        jbDelivery.setEnabled(mbIsCategorySal && mbIsDoc && mbHasRightLogistics);
        jbReferenceCommissions.setEnabled(mbIsCategorySal && mbIsDoc && mbHasRightEdit);
        jbViewNotes.setEnabled(true);
        jbViewLinks.setEnabled(true);
        jbViewContractAnalysis.setEnabled(mbIsEstCon);
        jbViewAccountingRecord.setEnabled(mbIsDoc || mbIsDocAdj);
        jbViewAccountingDetailsDps.setEnabled(mbIsDoc);
        jbViewAccountingDetailsBizPartner.setEnabled(mbIsDoc);
        jbPrint.setEnabled(mbIsOrd || mbIsCategorySal && (mbIsEstEst || mbIsEstCon || mbIsDoc || mbIsDocAdj));
        jbPrintAcknowledgmentCancellation.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));
        jbPrintPhotoInvoice.setEnabled(mbIsCategorySal && mbIsDoc);
        jbPrintContract.setEnabled(mbIsCategorySal && mbIsEstCon);
        jbPrintContractMoves.setEnabled(mbIsCategorySal && mbIsEstCon);
        jbPrintOrderGoods.setEnabled(mbIsCategorySal && mbIsOrd);
        jbGetXml.setEnabled((mbIsDoc || mbIsDocAdj));
        jbGetAcknowledgmentCancellation.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));
        jbSignXml.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));
        jbVerifyCfdi.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));
        jbSend.setEnabled((mbIsCategoryPur && mbIsOrd) || (mbIsCategorySal && (mbIsDoc || mbIsDocAdj)));
        jbRestoreSignXml.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));
        jbRestoreAcknowledgmentCancellation.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));
        jbDiactivateFlags.setEnabled(mbIsCategorySal && (mbIsDoc || mbIsDocAdj));

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = null;

        if (mbIsDoc || mbIsDocAdj) {
            aoTableColumns = new STableColumn[44];  // four extra columns for accounting record
        }
        else {
            aoTableColumns = new STableColumn[40];
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
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt_doc_delivery_n", "Entrega programada", STableConstants.WIDTH_DATE);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt_doc_delivery_n", "Entrega programada", STableConstants.WIDTH_DATE);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico", "Estatus", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico_xml", "CFD", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());

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
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tax_charged_cur_r", "Imp tras mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tax_retained_cur_r", "Imp ret mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.exc_rate", "T cambio", STableConstants.WIDTH_EXCHANGE_RATE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererExchangeRate());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.stot_r", "Subtotal $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tax_charged_r", "Imp tras $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tax_retained_r", "Imp ret $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_r", "Total $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key_local", "Moneda local", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_dn_code", "Naturaleza doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.comms_ref", "Referencia coms.", STableConstants.WIDTH_DOC_NUM_REF);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "num_ord", "Ord. prod.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_copy", "Copia", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_audit", "Auditado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_link", "Vinculado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ul.usr", "Usr. vinculación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_link", "Vinculación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_close", "Cerrado surtido", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uc.usr", "Usr. cierre surtido", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_close", "Cierre surtido", STableConstants.WIDTH_DATE_TIME);

        if (mbIsDoc || mbIsDocAdj) {
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

    /* XXX Method temporarily eliminated. Used in all printing methods. Check if it is not really needed. (sflores 2013-08-28)
    private boolean isBizPartnerBlocked() {
        boolean isBlocked = false;

        if (mbIsOrd || mbIsDoc) {
            try {
                isBlocked = SDataUtilities.obtainIsBizPartnerBlockedByDps(miClient, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                if (isBlocked) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_INF_BP_BLOCKED);
                }
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }

        return isBlocked;
    }
    */

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
           if (moTablePane.getSelectedTableRow() != null && !moTablePane.getSelectedTableRow().getIsSummary()) {
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
            if (moTablePane.getSelectedTableRow() == null) {
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
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_ANNUL) == JOptionPane.YES_OPTION) {
                    int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module
                    dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                    if (dps.getDbmsDataCfd() != null && dps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI) {
                        annul = false;
                        params = new SGuiParams();

                        if (dps.getDbmsDataCfd().isStamped()) {
                            moDialogAnnulCfdi.formReset();
                            moDialogAnnulCfdi.formRefreshCatalogues();
                            moDialogAnnulCfdi.setValue(SGuiConsts.PARAM_DATE, dps.getDate());
                            moDialogAnnulCfdi.setVisible(true);

                            if (moDialogAnnulCfdi.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                annul = true;
                                params.getParamsMap().put(SGuiConsts.PARAM_DATE, moDialogAnnulCfdi.getDate());
                                params.getParamsMap().put(SGuiConsts.PARAM_REQ_DOC, moDialogAnnulCfdi.getAnnulSat()); // SGuiConsts.PARAM_REQ_DOC is used to indicate if SAT cancellation is required
                            }
                        }
                        else {
                            annul = true;
                            params.getParamsMap().put(SGuiConsts.PARAM_DATE, miClient.getSession().getCurrentDate());
                            params.getParamsMap().put(SGuiConsts.PARAM_REQ_DOC, false);
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
            if (moTablePane.getSelectedTableRow() == null) {
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

    private void actionDelivery() {
        if (jbDelivery.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
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

    private void actionReferenceCommissions() {
        if (jbReferenceCommissions.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
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
            SModuleUtilities.showDocumentNotes(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow());
        }
    }

    private void actionViewLinks() {
        if (jbViewLinks.isEnabled()) {
            SModuleUtilities.showDocumentLinks(miClient, moTablePane.getSelectedTableRow());
        }
    }

    private void actionViewContractAnalysis() {
        if (jbViewContractAnalysis.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);

                moDialogContractAnalysis.formReset();
                moDialogContractAnalysis.setValue(SDataConstants.TRN_DPS, dps);
                moDialogContractAnalysis.setFormVisible(true);
            }
        }
    }

    private void actionViewAccountingRecord() {
        if (jbViewAccountingRecord.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
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
            if (moTablePane.getSelectedTableRow() == null) {
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
            if (moTablePane.getSelectedTableRow() == null) {
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

            if (moTablePane.getSelectedTableRow() != null/* && !isBizPartnerBlocked()*/) {
                oDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (oDps.getDbmsDataCfd() != null) {
                    try {
                        SCfdUtils.printCfd(miClient, SCfdConsts.CFD_TYPE_DPS, oDps.getDbmsDataCfd(), SDataConstantsPrint.PRINT_MODE_VIEWER, SLibConstants.UNDEFINED, false);
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
                    else if (SLibUtilities.compareKeys(keyDpsType, SDataConstantsSys.TRNU_TP_DPS_SAL_CON)) {
                        // Sales contract:

                        vPrice = new Vector<>();
                        vUnit = new Vector<>();
                        try {
                            setCursor(new Cursor(Cursor.WAIT_CURSOR));

                            map = miClient.createReportParams();
                            map.put("nIdYear", oDps.getPkYearId());
                            map.put("nIdDoc", oDps.getPkDocId());
                            map.put("sAddressLine1", addressContract[0]);
                            map.put("sAddressLine2", addressContract[1]);
                            map.put("sAddressLine3", addressContract[2]);
                            map.put("sAddressLine4", addressContract.length > 3 ? addressContract[3] : "");

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

    private void actionPrintContract() {
        if (jbPrintContract.isEnabled()) {
            int i;
            int nFkEmiAddressFormatTypeId_n = 0;
            int nFkRecAddressFormatTypeId_n = 0;
            boolean bincludeCountry = false;
            double dQty = 0;
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
            SDataBizPartnerBranchContact oContact = null;
            SDataBizPartnerBranchAddress oAddress = null;
            SDataCurrency oCurrency = null;
            SDataUnit oUnit = null;
            Vector<String> vPrice = null;
            Vector<String> vUnit = null;
            String sContact = "";

            if (moTablePane.getSelectedTableRow() != null) {
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

                if (oDps.getFkDpsCategoryId() == SDataConstantsSys.TRNU_TP_DPS_SAL_CON[0] && oDps.getFkDpsClassId() == SDataConstantsSys.TRNU_TP_DPS_SAL_CON[1] &&
                        oDps.getFkDpsTypeId() == SDataConstantsSys.TRNU_TP_DPS_SAL_CON[2]) {
                    vPrice = new Vector<>();
                    vUnit = new Vector<>();
                    try {
                        setCursor(new Cursor(Cursor.WAIT_CURSOR));

                        map = miClient.createReportParams();
                        map.put("nIdYear", oDps.getPkYearId());
                        map.put("nIdDoc", oDps.getPkDocId());
                        map.put("sAddressLine1", addressContract[0]);
                        map.put("sAddressLine2", addressContract[1]);
                        map.put("sAddressLine3", addressContract[2]);
                        map.put("sAddressLine4", addressContract.length > 3 ? addressContract[3] : "");

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
                        map.put("oDecimalFormat", miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormatFixed4());
                        map.put("oQuantityFormat", miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat());
                        map.put("oDateTextFormat", miClient.getSessionXXX().getFormatters().getDateTextFormat());
                        map.put("nFidTpCarBp", SModSysConsts.LOGS_TP_CAR_CAR);
                        map.put("nLogIncotermExw", SModSysConsts.LOGS_INC_EXW);
                        map.put("bPrintTon", true);

                        jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_CON_FIX, map);
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

    private void actionPrintContractMoves() {
        if (jbPrintContractMoves.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
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

            if (moTablePane.getSelectedTableRow() != null/* && !isBizPartnerBlocked()*/) {
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

            if (moTablePane.getSelectedTableRow() != null/* && !isBizPartnerBlocked()*/) {
                oDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (oDps.getDbmsDataCfd() != null) {
                    try {
                        SCfdUtils.printCfd(miClient, SCfdConsts.CFD_TYPE_DPS, oDps.getDbmsDataCfd(), SDataConstantsPrint.PRINT_MODE_VIEWER, SLibConstants.UNDEFINED, false);
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
            if (moTablePane.getSelectedTableRow() != null && !moTablePane.getSelectedTableRow().getIsSummary()) {
                try {
                    SCfdUtils.getXmlCfd(miClient, SCfdUtils.getCfd(miClient, SCfdConsts.CFD_TYPE_DPS, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey()));
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionPrintAcknowledgmentCancellation() {
        if (jbPrintAcknowledgmentCancellation.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null && !moTablePane.getSelectedTableRow().getIsSummary()) {
                try {
                    SCfdUtils.printAcknowledgmentCancellationCfd(miClient, SCfdUtils.getCfd(miClient, SCfdConsts.CFD_TYPE_DPS, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey()), SLibConstants.UNDEFINED);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionGetAcknowledgmentCancellation() {
        if (jbGetAcknowledgmentCancellation.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null && !moTablePane.getSelectedTableRow().getIsSummary()) {
                try {
                    SCfdUtils.getAcknowledgmentCancellationCfd(miClient, SCfdUtils.getCfd(miClient, SCfdConsts.CFD_TYPE_DPS, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey()));
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
           if (moTablePane.getSelectedTableRow() != null && !moTablePane.getSelectedTableRow().getIsSummary()) {
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
                        case SDataConstantsSys.TRNS_TP_XML_CFDI:
                            try {
                                /* XXX jbarajas 03/02/2016 sign and sending CFDI
                                if (SCfdUtils.signCfdi(miClient, dps.getDbmsDataCfd(), SLibConstants.UNDEFINED)) {
                                    miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                                }
                                */
                                if (SCfdUtils.signAndSendCfdi(miClient, dps.getDbmsDataCfd(), SLibConstants.UNDEFINED, true)) {
                                    miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
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
           if (moTablePane.getSelectedTableRow() != null && !moTablePane.getSelectedTableRow().getIsSummary()) {
                dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (dps.getIsDeleted()) {
                    miClient.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' está eliminado.");
                }
                else if (dps.getDbmsDataCfd() == null) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_READ + "\nNo se encontró el archivo XML del documento '" + dps.getDpsNumber() + "'.");
                }
                else {
                    if (SCfdUtils.verifyCfdi(miClient, dps.getDbmsDataCfd(), SLibConstants.UNDEFINED)) {
                        miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                    }
                }
           }
        }

    }

    private void actionSend() {
        if (jbSend.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                try {
                    switch (mnTabTypeAux02) {
                        case SDataConstantsSys.TRNX_TP_DPS_DOC:
                        case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                            SCfdUtils.sendCfd((SClientInterface) miClient, SCfdConsts.CFD_TYPE_DPS, SCfdUtils.getCfd(miClient, SCfdConsts.CFD_TYPE_DPS, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey()), SCfdConsts.CFDI_PAYROLL_VER_OLD);
                            break;
                        case SDataConstantsSys.TRNX_TP_DPS_ORD:
                            STrnUtilities.sendMailOrder(miClient, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey(), mnTabTypeAux01);
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
           if (moTablePane.getSelectedTableRow() != null && !moTablePane.getSelectedTableRow().getIsSummary()) {
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
           if (moTablePane.getSelectedTableRow() != null && !moTablePane.getSelectedTableRow().getIsSummary()) {
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
           if (moTablePane.getSelectedTableRow() != null && !moTablePane.getSelectedTableRow().getIsSummary()) {
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
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.b_del = FALSE ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "d.dt");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_DOC_NAT) {
                if (((Integer) setting.getSetting()) != SLibConstants.UNDEFINED) {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.fid_dps_nat = " + ((Integer) setting.getSetting()) + " ";
                }
            }
        }

        msSql = "SELECT d.id_year, d.id_doc, d.dt, d.dt_doc_delivery_n, d.num_ref, d.comms_ref, d.exc_rate, " +
                "d.stot_r, d.tax_charged_r, d.tax_retained_r, d.tot_r, " +
                "d.stot_cur_r, d.tax_charged_cur_r, d.tax_retained_cur_r, d.tot_cur_r, " +
                "d.b_copy, d.b_link, d.b_close, d.b_audit, d.b_del, d.ts_link, d.ts_close, d.ts_new, d.ts_edit, d.ts_del, dt.code, " +
                "(SELECT dn.code FROM erp.trnu_dps_nat AS dn WHERE d.fid_dps_nat = dn.id_dps_nat) AS f_dn_code, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                "(SELECT CONCAT(ord.id_year, '-', ord.num) FROM mfg_ord AS ord WHERE d.fid_mfg_year_n = ord.id_year AND d.fid_mfg_ord_n = ord.id_ord) AS num_ord, " +
                "IF(d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", " + STableConstants.ICON_ST_ANNUL + ", " + STableConstants.ICON_NULL + ") AS f_ico, " +
                //"IF(x.ts IS NULL OR doc_xml = '', " + STableConstants.ICON_NULL  + ", IF(x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_CFD + " OR x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_NA + ", " + STableConstants.ICON_XML + ", IF(x.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " OR LENGTH(uuid) = 0, " + STableConstants.ICON_XML_PEND + ", " + STableConstants.ICON_XML_SIGN + "))) AS f_ico_xml, " +
                "IF(x.ts IS NULL OR doc_xml = '', " + STableConstants.ICON_NULL  + ", " + /* not is CFD not is CFDI */
                "IF(x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_CFD + " OR x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_NA + ", " + STableConstants.ICON_XML + ", " + /* is CFD */
                "IF(x.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " OR LENGTH(uuid) = 0, " + STableConstants.ICON_XML_PEND + ", " + /* CFDI pending sign */
                "IF(LENGTH(x.ack_can_xml) = 0 AND x.ack_can_pdf_n IS NULL, " + STableConstants.ICON_XML_SIGN  + ", " + /* CFDI signed, canceled only SIIE */
                "IF(LENGTH(x.ack_can_xml) != 0, " + STableConstants.ICON_XML_CANC_XML + ", " + /* CFDI canceled with cancellation acknowledgment in XML format */
                "IF(x.ack_can_pdf_n IS NOT NULL, " + STableConstants.ICON_XML_CANC_PDF + ", " + /* CFDI canceled with cancellation acknowledgment in PDF format */
                STableConstants.ICON_XML_SIGN + " " + /* CFDI signed, canceled only SIIE */
                ")))))) AS f_ico_xml, " +
                "bp.id_bp, bp.bp, bpc.bp_key, bpb.id_bpb, bpb.bpb, " +
                "(SELECT c.cur_key FROM erp.cfgu_cur AS c WHERE d.fid_cur = c.id_cur) AS f_cur_key, 'MXN' AS f_cur_key_local, '" +
                miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey() + "' AS f_cur_key, " +
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
            case SDataConstantsSys.TRNX_TP_DPS_DOC + SDataConstantsSys.TRNX_DPS_DOC_ANNUL:
            case SDataConstantsSys.TRNX_TP_DPS_DOC + SDataConstantsSys.TRNX_DPS_DOC_RISS:
            case SDataConstantsSys.TRNX_TP_DPS_DOC + SDataConstantsSys.TRNX_DPS_DOC_REPL:
                msSql += (mbIsCategoryPur ?
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1] + " " :
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] + " ") + (
                        mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_DOC + SDataConstantsSys.TRNX_DPS_DOC_ANNUL ? " AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED :
                        mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_DOC + SDataConstantsSys.TRNX_DPS_DOC_RISS ? " AND d.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_RISS :
                        mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_DOC + SDataConstantsSys.TRNX_DPS_DOC_REPL ? " AND d.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_REPL : "") + " ";
                break;

            case SDataConstantsSys.TRNX_TP_DPS_ADJ:
            case SDataConstantsSys.TRNX_TP_DPS_ADJ + SDataConstantsSys.TRNX_DPS_DOC_ANNUL:
            case SDataConstantsSys.TRNX_TP_DPS_ADJ + SDataConstantsSys.TRNX_DPS_DOC_RISS:
            case SDataConstantsSys.TRNX_TP_DPS_ADJ + SDataConstantsSys.TRNX_DPS_DOC_REPL:
                msSql += (mbIsCategoryPur ?
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1] + " " :
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1] + " ") + (
                        mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_DOC + SDataConstantsSys.TRNX_DPS_DOC_ANNUL ? " AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED :
                        mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_DOC + SDataConstantsSys.TRNX_DPS_DOC_RISS ? " AND d.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_RISS :
                        mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_DOC + SDataConstantsSys.TRNX_DPS_DOC_REPL ? " AND d.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_REPL : "") + " ";
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
                else if (button == jbImport) {
                    actionImport();
                }
                else if (button == jbCopy) {
                    actionCopy();
                }
                else if (button == jbDelivery) {
                    actionDelivery();
                }
                else if (button == jbReferenceCommissions) {
                    actionReferenceCommissions();
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
                    actionPrint();
                }
                else if (button == jbPrintContract) {
                    actionPrintContract();
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
                else if (button == jbVerifyCfdi) {
                    actionVerifyCfdi();
                }
                else if (button == jbSend) {
                    actionSend();
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

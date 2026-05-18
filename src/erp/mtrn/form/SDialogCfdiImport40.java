/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogCfdiImport40.java
 *
 * Created on 28/07/2022, 02:14:00 PM
 */

package erp.mtrn.form;

import cfd.ver40.DCfdi40Catalogs;
import cfd.ver40.DElementConcepto;
import cfd.ver40.DElementConceptoImpuestoRetencion;
import cfd.ver40.DElementConceptoImpuestoTraslado;
import cfd.ver40.DElementConceptoImpuestos;
import erp.SErpConsts;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.form.SFormOptionPicker;
import erp.form.SFormOptionPickerItems;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mfin.data.SDataCostCenter;
import erp.mfin.data.SDataTax;
import erp.mfin.data.SDataTaxRegion;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataMatchingItemBizPartnerConcept;
import erp.mitm.data.SDataUnit;
import erp.mitm.data.SItemUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SBpsUtils;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.SDataDpsEntryTax;
import erp.mtrn.data.SDataDpsEntryTaxRow;
import erp.mtrn.data.SDataEntryDpsDpsLink;
import erp.mtrn.data.SRowCfdiImport40;
import erp.mtrn.data.SRowCfdiTaxImport40;
import erp.mtrn.data.STrnDpsUtilities;
import erp.swap.form.SDialogPdfViewer;
import erp.swap.form.SDocumentInfo;
import erp.swap.form.SDocumentUtils;
import erp.swap.utils.SImportUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;

/**
 * Diálogo para importar CFDI 4.0 tipo "I" en un nuevo DPS.
 * 
 * @author Isabel Servín, Sergio Flores
 */
public class SDialogCfdiImport40 extends javax.swing.JDialog implements java.awt.event.ActionListener, javax.swing.event.ListSelectionListener, javax.swing.event.CellEditorListener {
    
    private static final int CASE_ITEM = 1;
    private static final int CASE_ITEM_REF = 2;
    
    private static final int COL_ITEM_NAME = 8;
    private static final int COL_FACT_CONV = 11;
    
    private final erp.client.SClientInterface miClient;
    private int mnFormResult;
    private boolean mbFirstTime;
    private java.util.Vector<SFormField> mvFields;
    private erp.lib.table.STablePane moCfdiConceptsPane;
    private erp.lib.table.STablePane moCfdiTaxesPane;
    private erp.lib.table.STablePane moDpsTaxesPane;

    private final SDataDps moDpsToLink;
    private File moCfdiXmlFile;
    private File moCfdiPdfFile;
    private SDocumentInfo moDocumentInfo;
    private cfd.ver40.DElementComprobante moComprobante;
    private int[] manAdjustmentSubtypeKey;
    private int mnDocumentType;
    private boolean mbIsCreditNoteToApplyPrepayments;
    private String msDocumentName;
    private SDataDps moNewDps;
    private SDataBizPartner moBizPartnerEmisor;
    private SDataBizPartner moBizPartnerReceptor;
    private SFormOptionPickerItems moPickerItems;
    private SFormOptionPicker moPickerUnit;
    private SFormOptionPicker moPickerTaxRegion;
    private SFormOptionPicker moPickerCostCenter;
    private SFormOptionPicker moPickerAdjustmentSubtype;

    private SFormField moFieldTaxRegion;
    private SFormField moFieldDpsNature;
    private SFormField moFieldFunctionalSubArea;
    private SFormOptionPicker moPickerOpsType;
    private int mnCfdiCurrencyId;
    private SDialogPdfViewer moDialogPdfViewer;
    private SDialogCfdiConceptsLinker40 moDialogCfdiConceptsLinker40;
    
    SRowCfdiImport40 moRowCfdiCopy;

    /** Creates new form SDialogCfdiImport40.
     * @param client GUI client.
     * @param dpsToLink DPS to link: purchase order for invoices or invoice for credit notes.
     * @param cfdiFile XML file of CFDI to be imported.
     * @param pdfFile PDF file of CFDI to be imported.
     * @param documentInfo Document information.
     */
    public SDialogCfdiImport40(erp.client.SClientInterface client, SDataDps dpsToLink, File cfdiFile, File pdfFile, SDocumentInfo documentInfo) {
        super(client.getFrame(), true);
        
        miClient = client;
        moDpsToLink = dpsToLink;
        moCfdiXmlFile = cfdiFile;
        moCfdiPdfFile = pdfFile;
        moDocumentInfo = documentInfo;
        
        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpCfdiImport = new javax.swing.JPanel();
        jpCfdiData = new javax.swing.JPanel();
        jpCfdi = new javax.swing.JPanel();
        jpCfdi1 = new javax.swing.JPanel();
        jlCfdiEmisor = new javax.swing.JLabel();
        jtfCfdiEmisor = new javax.swing.JTextField();
        jlCfdiEmisorFiscalId = new javax.swing.JLabel();
        jtfCfdiEmisorFiscalId = new javax.swing.JTextField();
        jpCfdi2 = new javax.swing.JPanel();
        jlCfdi = new javax.swing.JLabel();
        jtfCfdiFolio = new javax.swing.JTextField();
        jtfCfdiPaymentMethod = new javax.swing.JTextField();
        jbViewCfdiPdf = new javax.swing.JButton();
        jlCfdiDate = new javax.swing.JLabel();
        jtfCfdiDate = new javax.swing.JTextField();
        jpImportAdditionalData = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jlTaxRegion = new javax.swing.JLabel();
        jcbTaxRegion = new javax.swing.JComboBox();
        jbPickTaxRegion = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jlDpsToLink = new javax.swing.JLabel();
        jtfDpsToLinkFolio = new javax.swing.JTextField();
        jlDpsToLinkDate = new javax.swing.JLabel();
        jtfDpsToLinkDate = new javax.swing.JTextField();
        jbViewDpsToLink = new javax.swing.JButton();
        jpCfdiConcepts = new javax.swing.JPanel();
        jpCfdiConceptsGrid = new javax.swing.JPanel();
        jpCfdiConceptsGridCommands = new javax.swing.JPanel();
        jbCopyRowSettings = new javax.swing.JButton();
        jbPasteRowSettings = new javax.swing.JButton();
        jtfCopyRowInfo = new javax.swing.JTextField();
        jlDpsNature = new javax.swing.JLabel();
        jcbDpsNature = new javax.swing.JComboBox();
        jlFunctionalSubArea = new javax.swing.JLabel();
        jcbFunctionalSubArea = new javax.swing.JComboBox();
        jpCfdiConceptsData = new javax.swing.JPanel();
        jpCfdiConceptsDataNorth = new javax.swing.JPanel();
        jpCfdiConceptSetup = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jbSelectItem = new javax.swing.JButton();
        jbSelectOperationsType = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jbSelectUnit = new javax.swing.JButton();
        jbSelectCostCenter = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jbSelectTaxRegion = new javax.swing.JButton();
        jbSelectItemReference = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jlIsItemNameEditable = new javax.swing.JLabel();
        jpDpsToLinkEntry = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jbAsignDpsToLinkEntries = new javax.swing.JButton();
        jbSelectAdjustmentSubtype = new javax.swing.JButton();
        jlPoBefore = new javax.swing.JLabel();
        jlPoAfter = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jbProcessAsService = new javax.swing.JButton();
        jlDummy2 = new javax.swing.JLabel();
        jlPoOriginalQuantity = new javax.swing.JLabel();
        jtfPoOriginalQuantity = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jlPoEntry = new javax.swing.JLabel();
        jtfPoEntry = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jlPoProcessedQuantity = new javax.swing.JLabel();
        jtfPoProcessedQuantity = new javax.swing.JTextField();
        jtfPoProcessedQuantityCurrent = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jlPoSurplusPct = new javax.swing.JLabel();
        jtfPoSurplusPct = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jlPoPendingQuantity = new javax.swing.JLabel();
        jtfPoPendingQuantity = new javax.swing.JTextField();
        jtfPoPendingQuantityCurrent = new javax.swing.JTextField();
        jpCfdiConceptsDataCenter = new javax.swing.JPanel();
        jpCfdiConceptTaxes = new javax.swing.JPanel();
        jpCfdiTaxes = new javax.swing.JPanel();
        jpDpsTaxes = new javax.swing.JPanel();
        jpCfdiTotal = new javax.swing.JPanel();
        jPanel = new javax.swing.JPanel();
        jlCurrency = new javax.swing.JLabel();
        jtfCurrency = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jlSubtotal = new javax.swing.JLabel();
        jtfSubtotal = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jlExchangeRate = new javax.swing.JLabel();
        jtfExchangeRate = new javax.swing.JTextField();
        jlTaxCharged = new javax.swing.JLabel();
        jtfTaxCharged = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jlProvSubtotal = new javax.swing.JLabel();
        jtfProvSubtotal = new javax.swing.JTextField();
        jlTaxRetained = new javax.swing.JLabel();
        jtfTaxRetained = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jlDiscountDoc = new javax.swing.JLabel();
        jtfDiscountDoc = new javax.swing.JTextField();
        jlTotal = new javax.swing.JLabel();
        jtfTotal = new javax.swing.JTextField();
        jpControls = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Vinculación de documento de compras-ventas");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpCfdiImport.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpCfdiImport.setLayout(new java.awt.BorderLayout());

        jpCfdiData.setLayout(new java.awt.BorderLayout());

        jpCfdi.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos generales del CFDI:"));
        jpCfdi.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jpCfdi1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdiEmisor.setText("Emisor:");
        jlCfdiEmisor.setPreferredSize(new java.awt.Dimension(75, 23));
        jpCfdi1.add(jlCfdiEmisor);

        jtfCfdiEmisor.setEditable(false);
        jtfCfdiEmisor.setFocusable(false);
        jtfCfdiEmisor.setPreferredSize(new java.awt.Dimension(265, 23));
        jpCfdi1.add(jtfCfdiEmisor);

        jlCfdiEmisorFiscalId.setText("  RFC:");
        jlCfdiEmisorFiscalId.setPreferredSize(new java.awt.Dimension(75, 23));
        jpCfdi1.add(jlCfdiEmisorFiscalId);

        jtfCfdiEmisorFiscalId.setEditable(false);
        jtfCfdiEmisorFiscalId.setFocusable(false);
        jtfCfdiEmisorFiscalId.setPreferredSize(new java.awt.Dimension(125, 23));
        jpCfdi1.add(jtfCfdiEmisorFiscalId);

        jpCfdi.add(jpCfdi1);

        jpCfdi2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdi.setText("Folio CFDI:");
        jlCfdi.setPreferredSize(new java.awt.Dimension(75, 23));
        jpCfdi2.add(jlCfdi);

        jtfCfdiFolio.setEditable(false);
        jtfCfdiFolio.setFocusable(false);
        jtfCfdiFolio.setPreferredSize(new java.awt.Dimension(180, 23));
        jpCfdi2.add(jtfCfdiFolio);

        jtfCfdiPaymentMethod.setEditable(false);
        jtfCfdiPaymentMethod.setToolTipText("Método pago");
        jtfCfdiPaymentMethod.setFocusable(false);
        jtfCfdiPaymentMethod.setPreferredSize(new java.awt.Dimension(52, 23));
        jpCfdi2.add(jtfCfdiPaymentMethod);

        jbViewCfdiPdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon-file-pdf.png"))); // NOI18N
        jbViewCfdiPdf.setToolTipText("Ver PDF del CFDI...");
        jbViewCfdiPdf.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCfdi2.add(jbViewCfdiPdf);

        jlCfdiDate.setText("  Fecha CFDI:");
        jlCfdiDate.setPreferredSize(new java.awt.Dimension(75, 23));
        jpCfdi2.add(jlCfdiDate);

        jtfCfdiDate.setEditable(false);
        jtfCfdiDate.setFocusable(false);
        jtfCfdiDate.setPreferredSize(new java.awt.Dimension(125, 23));
        jpCfdi2.add(jtfCfdiDate);

        jpCfdi.add(jpCfdi2);

        jpCfdiData.add(jpCfdi, java.awt.BorderLayout.CENTER);

        jpImportAdditionalData.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos adicionales de importación:"));
        jpImportAdditionalData.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTaxRegion.setText("Región impuestos:*");
        jlTaxRegion.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlTaxRegion);

        jcbTaxRegion.setPreferredSize(new java.awt.Dimension(275, 23));
        jPanel8.add(jcbTaxRegion);

        jbPickTaxRegion.setText("...");
        jbPickTaxRegion.setToolTipText("Seleccionar región de impuestos...");
        jbPickTaxRegion.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel8.add(jbPickTaxRegion);

        jpImportAdditionalData.add(jPanel8);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDpsToLink.setText("<Document>:");
        jlDpsToLink.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel13.add(jlDpsToLink);

        jtfDpsToLinkFolio.setEditable(false);
        jtfDpsToLinkFolio.setEnabled(false);
        jtfDpsToLinkFolio.setFocusable(false);
        jtfDpsToLinkFolio.setPreferredSize(new java.awt.Dimension(130, 23));
        jPanel13.add(jtfDpsToLinkFolio);

        jlDpsToLinkDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDpsToLinkDate.setText("Fecha:");
        jlDpsToLinkDate.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel13.add(jlDpsToLinkDate);

        jtfDpsToLinkDate.setEditable(false);
        jtfDpsToLinkDate.setEnabled(false);
        jtfDpsToLinkDate.setFocusable(false);
        jtfDpsToLinkDate.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel13.add(jtfDpsToLinkDate);

        jbViewDpsToLink.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_seek.gif"))); // NOI18N
        jbViewDpsToLink.setToolTipText("Ver <document>...");
        jbViewDpsToLink.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel13.add(jbViewDpsToLink);

        jpImportAdditionalData.add(jPanel13);

        jpCfdiData.add(jpImportAdditionalData, java.awt.BorderLayout.EAST);

        jpCfdiImport.add(jpCfdiData, java.awt.BorderLayout.NORTH);

        jpCfdiConcepts.setBorder(javax.swing.BorderFactory.createTitledBorder("Conceptos del CFDI:"));
        jpCfdiConcepts.setLayout(new java.awt.BorderLayout(0, 5));

        jpCfdiConceptsGrid.setName(""); // NOI18N
        jpCfdiConceptsGrid.setLayout(new java.awt.BorderLayout(0, 5));

        jpCfdiConceptsGridCommands.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbCopyRowSettings.setText("Copiar valores renglón");
        jbCopyRowSettings.setMargin(new java.awt.Insets(2, 1, 2, 1));
        jbCopyRowSettings.setPreferredSize(new java.awt.Dimension(150, 23));
        jpCfdiConceptsGridCommands.add(jbCopyRowSettings);

        jbPasteRowSettings.setText("Pegar valores renglón");
        jbPasteRowSettings.setMargin(new java.awt.Insets(2, 1, 2, 1));
        jbPasteRowSettings.setPreferredSize(new java.awt.Dimension(150, 23));
        jpCfdiConceptsGridCommands.add(jbPasteRowSettings);

        jtfCopyRowInfo.setEditable(false);
        jtfCopyRowInfo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfCopyRowInfo.setFocusable(false);
        jtfCopyRowInfo.setPreferredSize(new java.awt.Dimension(150, 23));
        jpCfdiConceptsGridCommands.add(jtfCopyRowInfo);

        jlDpsNature.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDpsNature.setText("Naturaleza doc.:*");
        jlDpsNature.setPreferredSize(new java.awt.Dimension(110, 23));
        jpCfdiConceptsGridCommands.add(jlDpsNature);

        jcbDpsNature.setEnabled(false);
        jcbDpsNature.setPreferredSize(new java.awt.Dimension(140, 23));
        jpCfdiConceptsGridCommands.add(jcbDpsNature);

        jlFunctionalSubArea.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlFunctionalSubArea.setText("Área funcional:*");
        jlFunctionalSubArea.setPreferredSize(new java.awt.Dimension(100, 23));
        jpCfdiConceptsGridCommands.add(jlFunctionalSubArea);

        jcbFunctionalSubArea.setEnabled(false);
        jcbFunctionalSubArea.setPreferredSize(new java.awt.Dimension(140, 23));
        jpCfdiConceptsGridCommands.add(jcbFunctionalSubArea);

        jpCfdiConceptsGrid.add(jpCfdiConceptsGridCommands, java.awt.BorderLayout.SOUTH);

        jpCfdiConcepts.add(jpCfdiConceptsGrid, java.awt.BorderLayout.CENTER);

        jpCfdiConceptsData.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jpCfdiConceptsData.setPreferredSize(new java.awt.Dimension(100, 275));
        jpCfdiConceptsData.setLayout(new java.awt.BorderLayout());

        jpCfdiConceptsDataNorth.setLayout(new java.awt.BorderLayout());

        jpCfdiConceptSetup.setBorder(javax.swing.BorderFactory.createTitledBorder("Valores de importación del concepto seleccionado del CFDI:"));
        jpCfdiConceptSetup.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbSelectItem.setText("Elegir ítem");
        jbSelectItem.setMargin(new java.awt.Insets(2, 1, 2, 1));
        jbSelectItem.setPreferredSize(new java.awt.Dimension(165, 23));
        jPanel11.add(jbSelectItem);

        jbSelectOperationsType.setText("Elegir tipo de operación");
        jbSelectOperationsType.setMargin(new java.awt.Insets(2, 1, 2, 1));
        jbSelectOperationsType.setPreferredSize(new java.awt.Dimension(165, 23));
        jPanel11.add(jbSelectOperationsType);

        jpCfdiConceptSetup.add(jPanel11);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbSelectUnit.setText("Elegir unidad");
        jbSelectUnit.setMargin(new java.awt.Insets(2, 1, 2, 1));
        jbSelectUnit.setPreferredSize(new java.awt.Dimension(165, 23));
        jPanel15.add(jbSelectUnit);

        jbSelectCostCenter.setText("Elegir centro de costo");
        jbSelectCostCenter.setMargin(new java.awt.Insets(2, 1, 2, 1));
        jbSelectCostCenter.setPreferredSize(new java.awt.Dimension(165, 23));
        jPanel15.add(jbSelectCostCenter);

        jpCfdiConceptSetup.add(jPanel15);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbSelectTaxRegion.setText("Elegir región de impuestos");
        jbSelectTaxRegion.setMargin(new java.awt.Insets(2, 1, 2, 1));
        jbSelectTaxRegion.setPreferredSize(new java.awt.Dimension(165, 23));
        jPanel16.add(jbSelectTaxRegion);

        jbSelectItemReference.setText("Elegir ítem de referencia");
        jbSelectItemReference.setMargin(new java.awt.Insets(2, 1, 2, 1));
        jbSelectItemReference.setPreferredSize(new java.awt.Dimension(165, 23));
        jPanel16.add(jbSelectItemReference);

        jpCfdiConceptSetup.add(jPanel16);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlIsItemNameEditable.setBackground(java.awt.SystemColor.controlHighlight);
        jlIsItemNameEditable.setOpaque(true);
        jlIsItemNameEditable.setPreferredSize(new java.awt.Dimension(335, 23));
        jPanel17.add(jlIsItemNameEditable);

        jpCfdiConceptSetup.add(jPanel17);

        jpCfdiConceptsDataNorth.add(jpCfdiConceptSetup, java.awt.BorderLayout.CENTER);

        jpDpsToLinkEntry.setBorder(javax.swing.BorderFactory.createTitledBorder("Partidas de la <document> del concepto seleccionado del CFDI:"));
        jpDpsToLinkEntry.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbAsignDpsToLinkEntries.setText("Asignar partidas de la factura");
        jbAsignDpsToLinkEntries.setMargin(new java.awt.Insets(2, 1, 2, 1));
        jbAsignDpsToLinkEntries.setPreferredSize(new java.awt.Dimension(165, 23));
        jPanel7.add(jbAsignDpsToLinkEntries);

        jbSelectAdjustmentSubtype.setText("Elegir tipo de ajuste");
        jbSelectAdjustmentSubtype.setMargin(new java.awt.Insets(2, 1, 2, 1));
        jbSelectAdjustmentSubtype.setPreferredSize(new java.awt.Dimension(165, 23));
        jPanel7.add(jbSelectAdjustmentSubtype);

        jlPoBefore.setText("Antes:");
        jlPoBefore.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel7.add(jlPoBefore);

        jlPoAfter.setText("Ahora:");
        jlPoAfter.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel7.add(jlPoAfter);

        jpDpsToLinkEntry.add(jPanel7);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbProcessAsService.setText("Procesar servicio");
        jbProcessAsService.setMargin(new java.awt.Insets(2, 1, 2, 1));
        jbProcessAsService.setPreferredSize(new java.awt.Dimension(165, 23));
        jPanel14.add(jbProcessAsService);

        jlDummy2.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel14.add(jlDummy2);

        jlPoOriginalQuantity.setText(" Cant. original:");
        jlPoOriginalQuantity.setMinimumSize(new java.awt.Dimension(125, 16));
        jlPoOriginalQuantity.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlPoOriginalQuantity);

        jtfPoOriginalQuantity.setEditable(false);
        jtfPoOriginalQuantity.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPoOriginalQuantity.setText("999,999,999.9999");
        jtfPoOriginalQuantity.setFocusable(false);
        jtfPoOriginalQuantity.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel14.add(jtfPoOriginalQuantity);

        jpDpsToLinkEntry.add(jPanel14);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPoEntry.setText("No. de partida:");
        jlPoEntry.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlPoEntry);

        jtfPoEntry.setEditable(false);
        jtfPoEntry.setText("1");
        jtfPoEntry.setFocusable(false);
        jtfPoEntry.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel9.add(jtfPoEntry);

        jLabel1.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel9.add(jLabel1);

        jlPoProcessedQuantity.setText(" Cant. procesada:");
        jlPoProcessedQuantity.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlPoProcessedQuantity);

        jtfPoProcessedQuantity.setEditable(false);
        jtfPoProcessedQuantity.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPoProcessedQuantity.setText("999,999,999.9999");
        jtfPoProcessedQuantity.setFocusable(false);
        jtfPoProcessedQuantity.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel9.add(jtfPoProcessedQuantity);

        jtfPoProcessedQuantityCurrent.setEditable(false);
        jtfPoProcessedQuantityCurrent.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPoProcessedQuantityCurrent.setText("999,999,999.9999");
        jtfPoProcessedQuantityCurrent.setFocusable(false);
        jtfPoProcessedQuantityCurrent.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel9.add(jtfPoProcessedQuantityCurrent);

        jpDpsToLinkEntry.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPoSurplusPct.setText("% excedente:");
        jlPoSurplusPct.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlPoSurplusPct);

        jtfPoSurplusPct.setEditable(false);
        jtfPoSurplusPct.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPoSurplusPct.setText("0.0000%");
        jtfPoSurplusPct.setFocusable(false);
        jtfPoSurplusPct.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel10.add(jtfPoSurplusPct);

        jLabel2.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel10.add(jLabel2);

        jlPoPendingQuantity.setText(" Cant. pendiente:");
        jlPoPendingQuantity.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlPoPendingQuantity);

        jtfPoPendingQuantity.setEditable(false);
        jtfPoPendingQuantity.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPoPendingQuantity.setText("999,999,999.9999");
        jtfPoPendingQuantity.setFocusable(false);
        jtfPoPendingQuantity.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel10.add(jtfPoPendingQuantity);

        jtfPoPendingQuantityCurrent.setEditable(false);
        jtfPoPendingQuantityCurrent.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPoPendingQuantityCurrent.setText("999,999,999.9999");
        jtfPoPendingQuantityCurrent.setFocusable(false);
        jtfPoPendingQuantityCurrent.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel10.add(jtfPoPendingQuantityCurrent);

        jpDpsToLinkEntry.add(jPanel10);

        jpCfdiConceptsDataNorth.add(jpDpsToLinkEntry, java.awt.BorderLayout.EAST);

        jpCfdiConceptsData.add(jpCfdiConceptsDataNorth, java.awt.BorderLayout.NORTH);

        jpCfdiConceptsDataCenter.setLayout(new java.awt.BorderLayout());

        jpCfdiConceptTaxes.setLayout(new java.awt.GridLayout(1, 2));

        jpCfdiTaxes.setBorder(javax.swing.BorderFactory.createTitledBorder("Impuestos del concepto del CFDI:"));
        jpCfdiTaxes.setLayout(new java.awt.BorderLayout());
        jpCfdiConceptTaxes.add(jpCfdiTaxes);

        jpDpsTaxes.setBorder(javax.swing.BorderFactory.createTitledBorder("Impuestos de la partida de la <doc>:"));
        jpDpsTaxes.setLayout(new java.awt.BorderLayout());
        jpCfdiConceptTaxes.add(jpDpsTaxes);

        jpCfdiConceptsDataCenter.add(jpCfdiConceptTaxes, java.awt.BorderLayout.CENTER);

        jpCfdiTotal.setBorder(javax.swing.BorderFactory.createTitledBorder("Total del CFDI:"));
        jpCfdiTotal.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCurrency.setText("Moneda:");
        jlCurrency.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel.add(jlCurrency);

        jtfCurrency.setEditable(false);
        jtfCurrency.setText("TEXT");
        jtfCurrency.setFocusable(false);
        jtfCurrency.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel.add(jtfCurrency);

        jLabel6.setPreferredSize(new java.awt.Dimension(55, 25));
        jPanel.add(jLabel6);

        jlSubtotal.setText("  Subtotal:");
        jlSubtotal.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel.add(jlSubtotal);

        jtfSubtotal.setEditable(false);
        jtfSubtotal.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSubtotal.setText("999,999,999.99");
        jtfSubtotal.setFocusable(false);
        jtfSubtotal.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel.add(jtfSubtotal);

        jpCfdiTotal.add(jPanel);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlExchangeRate.setText("Tipo de cambio:");
        jlExchangeRate.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel1.add(jlExchangeRate);

        jtfExchangeRate.setEditable(false);
        jtfExchangeRate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfExchangeRate.setText("999.9999");
        jtfExchangeRate.setFocusable(false);
        jtfExchangeRate.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel1.add(jtfExchangeRate);

        jlTaxCharged.setText("  Imptos. trasladados:");
        jlTaxCharged.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel1.add(jlTaxCharged);

        jtfTaxCharged.setEditable(false);
        jtfTaxCharged.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTaxCharged.setText("999,999,999.99");
        jtfTaxCharged.setFocusable(false);
        jtfTaxCharged.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel1.add(jtfTaxCharged);

        jpCfdiTotal.add(jPanel1);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProvSubtotal.setText("Subtotal provisional:");
        jlProvSubtotal.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel3.add(jlProvSubtotal);

        jtfProvSubtotal.setEditable(false);
        jtfProvSubtotal.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfProvSubtotal.setText("999,999,999.99");
        jtfProvSubtotal.setFocusable(false);
        jtfProvSubtotal.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel3.add(jtfProvSubtotal);

        jlTaxRetained.setText("  Imptos. retenidos:");
        jlTaxRetained.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel3.add(jlTaxRetained);

        jtfTaxRetained.setEditable(false);
        jtfTaxRetained.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTaxRetained.setText("999,999,999.99");
        jtfTaxRetained.setFocusable(false);
        jtfTaxRetained.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel3.add(jtfTaxRetained);

        jpCfdiTotal.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDiscountDoc.setText("Descuento:");
        jlDiscountDoc.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel4.add(jlDiscountDoc);

        jtfDiscountDoc.setEditable(false);
        jtfDiscountDoc.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDiscountDoc.setText("999,999,999.99");
        jtfDiscountDoc.setFocusable(false);
        jtfDiscountDoc.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel4.add(jtfDiscountDoc);

        jlTotal.setText("  Total:");
        jlTotal.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel4.add(jlTotal);

        jtfTotal.setEditable(false);
        jtfTotal.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTotal.setText("999,999,999.99");
        jtfTotal.setFocusable(false);
        jtfTotal.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel4.add(jtfTotal);

        jpCfdiTotal.add(jPanel4);

        jpCfdiConceptsDataCenter.add(jpCfdiTotal, java.awt.BorderLayout.EAST);

        jpCfdiConceptsData.add(jpCfdiConceptsDataCenter, java.awt.BorderLayout.CENTER);

        jpCfdiConcepts.add(jpCfdiConceptsData, java.awt.BorderLayout.SOUTH);

        jpCfdiImport.add(jpCfdiConcepts, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpCfdiImport, java.awt.BorderLayout.CENTER);

        jpControls.setPreferredSize(new java.awt.Dimension(392, 33));
        jpControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jpControls.add(jbCancel);

        getContentPane().add(jpControls, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(1040, 709));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbAsignDpsToLinkEntries;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbCopyRowSettings;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbPasteRowSettings;
    private javax.swing.JButton jbPickTaxRegion;
    private javax.swing.JButton jbProcessAsService;
    private javax.swing.JButton jbSelectAdjustmentSubtype;
    private javax.swing.JButton jbSelectCostCenter;
    private javax.swing.JButton jbSelectItem;
    private javax.swing.JButton jbSelectItemReference;
    private javax.swing.JButton jbSelectOperationsType;
    private javax.swing.JButton jbSelectTaxRegion;
    private javax.swing.JButton jbSelectUnit;
    private javax.swing.JButton jbViewCfdiPdf;
    private javax.swing.JButton jbViewDpsToLink;
    private javax.swing.JComboBox jcbDpsNature;
    private javax.swing.JComboBox jcbFunctionalSubArea;
    private javax.swing.JComboBox jcbTaxRegion;
    private javax.swing.JLabel jlCfdi;
    private javax.swing.JLabel jlCfdiDate;
    private javax.swing.JLabel jlCfdiEmisor;
    private javax.swing.JLabel jlCfdiEmisorFiscalId;
    private javax.swing.JLabel jlCurrency;
    private javax.swing.JLabel jlDiscountDoc;
    private javax.swing.JLabel jlDpsNature;
    private javax.swing.JLabel jlDpsToLink;
    private javax.swing.JLabel jlDpsToLinkDate;
    private javax.swing.JLabel jlDummy2;
    private javax.swing.JLabel jlExchangeRate;
    private javax.swing.JLabel jlFunctionalSubArea;
    private javax.swing.JLabel jlIsItemNameEditable;
    private javax.swing.JLabel jlPoAfter;
    private javax.swing.JLabel jlPoBefore;
    private javax.swing.JLabel jlPoEntry;
    private javax.swing.JLabel jlPoOriginalQuantity;
    private javax.swing.JLabel jlPoPendingQuantity;
    private javax.swing.JLabel jlPoProcessedQuantity;
    private javax.swing.JLabel jlPoSurplusPct;
    private javax.swing.JLabel jlProvSubtotal;
    private javax.swing.JLabel jlSubtotal;
    private javax.swing.JLabel jlTaxCharged;
    private javax.swing.JLabel jlTaxRegion;
    private javax.swing.JLabel jlTaxRetained;
    private javax.swing.JLabel jlTotal;
    private javax.swing.JPanel jpCfdi;
    private javax.swing.JPanel jpCfdi1;
    private javax.swing.JPanel jpCfdi2;
    private javax.swing.JPanel jpCfdiConceptSetup;
    private javax.swing.JPanel jpCfdiConceptTaxes;
    private javax.swing.JPanel jpCfdiConcepts;
    private javax.swing.JPanel jpCfdiConceptsData;
    private javax.swing.JPanel jpCfdiConceptsDataCenter;
    private javax.swing.JPanel jpCfdiConceptsDataNorth;
    private javax.swing.JPanel jpCfdiConceptsGrid;
    private javax.swing.JPanel jpCfdiConceptsGridCommands;
    private javax.swing.JPanel jpCfdiData;
    private javax.swing.JPanel jpCfdiImport;
    private javax.swing.JPanel jpCfdiTaxes;
    private javax.swing.JPanel jpCfdiTotal;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpDpsTaxes;
    private javax.swing.JPanel jpDpsToLinkEntry;
    private javax.swing.JPanel jpImportAdditionalData;
    private javax.swing.JTextField jtfCfdiDate;
    private javax.swing.JTextField jtfCfdiEmisor;
    private javax.swing.JTextField jtfCfdiEmisorFiscalId;
    private javax.swing.JTextField jtfCfdiFolio;
    private javax.swing.JTextField jtfCfdiPaymentMethod;
    private javax.swing.JTextField jtfCopyRowInfo;
    private javax.swing.JTextField jtfCurrency;
    private javax.swing.JTextField jtfDiscountDoc;
    private javax.swing.JTextField jtfDpsToLinkDate;
    private javax.swing.JTextField jtfDpsToLinkFolio;
    private javax.swing.JTextField jtfExchangeRate;
    private javax.swing.JTextField jtfPoEntry;
    private javax.swing.JTextField jtfPoOriginalQuantity;
    private javax.swing.JTextField jtfPoPendingQuantity;
    private javax.swing.JTextField jtfPoPendingQuantityCurrent;
    private javax.swing.JTextField jtfPoProcessedQuantity;
    private javax.swing.JTextField jtfPoProcessedQuantityCurrent;
    private javax.swing.JTextField jtfPoSurplusPct;
    private javax.swing.JTextField jtfProvSubtotal;
    private javax.swing.JTextField jtfSubtotal;
    private javax.swing.JTextField jtfTaxCharged;
    private javax.swing.JTextField jtfTaxRetained;
    private javax.swing.JTextField jtfTotal;
    // End of variables declaration//GEN-END:variables

    @SuppressWarnings("unchecked")
    private void initComponentsExtra() {
        mnFormResult = 0;
        mbFirstTime = true;
        
        // Tabla de conceptos del CFDI:
        
        int i = 0;
        STableColumnForm[] cfdiConceptColumns;
        
        moCfdiConceptsPane = new STablePane(miClient);
        jpCfdiConceptsGrid.add(moCfdiConceptsPane, BorderLayout.CENTER);
       
        cfdiConceptColumns = new STableColumnForm[25];
        // CFDI cols:
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "#", STableConstants.WIDTH_NUM_TINYINT);
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "No. identificación", STableConstants.WIDTH_ITEM_KEY);
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Descripción", 250);
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "ProdServ", STableConstants.WIDTH_ITEM_KEY);
        cfdiConceptColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Cantidad", STableConstants.WIDTH_QUANTITY_2X);
        cfdiConceptColumns[i].setCellRenderer(SGridUtils.getCellRendererNumberQuantity());
        cfdiConceptColumns[i++].setCellRenderer(SGridUtils.CellRendererValue8D);
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad", STableConstants.WIDTH_NUM_SMALLINT);
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad SAT", STableConstants.WIDTH_VALUE);
        // Matching cols:
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
        cfdiConceptColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Ítem", 250);
        cfdiConceptColumns[i++].setEditable(true);
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "ProdServ SAT", STableConstants.WIDTH_ITEM_KEY);
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad ítem", STableConstants.WIDTH_NUM_SMALLINT);
        cfdiConceptColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Factor conversión", STableConstants.WIDTH_QUANTITY_2X);
        cfdiConceptColumns[i].setCellRenderer(SGridUtils.CellRendererValue8D);
        cfdiConceptColumns[i++].setEditable(true);
        cfdiConceptColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Cantidad equivalente", STableConstants.WIDTH_QUANTITY_2X);
        cfdiConceptColumns[i++].setCellRenderer(SGridUtils.CellRendererValue8D);
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad", STableConstants.WIDTH_NUM_SMALLINT);
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad SAT", STableConstants.WIDTH_VALUE);
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Región impuestos", 150);
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo operación", STableConstants.WIDTH_ITEM);
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Clave centro costo", STableConstants.WIDTH_ITEM_KEY);
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Centro costo", STableConstants.WIDTH_ACCOUNT);
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Código ítem referencia", STableConstants.WIDTH_ITEM_KEY);
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Ítem referencia", 250);
        // CFDI (complemento) cols:
        cfdiConceptColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Valor unitario $", STableConstants.WIDTH_QUANTITY_2X);
        cfdiConceptColumns[i++].setCellRenderer(SGridUtils.CellRendererValue8D);
        cfdiConceptColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Importe $", STableConstants.WIDTH_QUANTITY);
        cfdiConceptColumns[i++].setCellRenderer(SGridUtils.CellRendererValue2D);
        cfdiConceptColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Descuento $", STableConstants.WIDTH_QUANTITY);
        cfdiConceptColumns[i++].setCellRenderer(SGridUtils.CellRendererValue2D);
        // Other cols:
        cfdiConceptColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo ajuste", 250);
        
        for (i = 0; i < cfdiConceptColumns.length; i++) {
            moCfdiConceptsPane.addTableColumn(cfdiConceptColumns[i]);
        }
        
        // Tabla de impuestos del CFDI:
        
        i = 0;
        STableColumnForm[] taxCfdiColumns;
        
        moCfdiTaxesPane = new STablePane(miClient);
        jpCfdiTaxes.add(moCfdiTaxesPane, BorderLayout.CENTER);

        taxCfdiColumns = new STableColumnForm[4];
        taxCfdiColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo impuesto", STableConstants.WIDTH_VALUE);
        taxCfdiColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo impuesto (SAT)", STableConstants.WIDTH_VALUE);
        taxCfdiColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tasa o cuota", STableConstants.WIDTH_NUM_INTEGER);
        taxCfdiColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Factor", STableConstants.WIDTH_VALUE);
        
        for (i = 0; i < taxCfdiColumns.length; i++) {
            moCfdiTaxesPane.addTableColumn(taxCfdiColumns[i]);
        }
        
        // Tabla de impuestos del documento:
        
        i = 0;
        STableColumnForm[] taxDpsColumns;
        
        moDpsTaxesPane = new STablePane(miClient);
        jpDpsTaxes.add(moDpsTaxesPane, BorderLayout.CENTER);

        taxDpsColumns = new STableColumnForm[9];
        taxDpsColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Impuesto", 200);
        taxDpsColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Tasa", STableConstants.WIDTH_PERCENTAGE);
        taxDpsColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());
        taxDpsColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Valor u.", STableConstants.WIDTH_VALUE_UNITARY);
        taxDpsColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        taxDpsColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Valor", STableConstants.WIDTH_VALUE);
        taxDpsColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Monto $", STableConstants.WIDTH_VALUE);
        taxDpsColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Monto mon $", STableConstants.WIDTH_VALUE);
        taxDpsColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo impuesto", 150);
        taxDpsColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Cálculo impuesto", 150);
        taxDpsColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Aplicación impuesto", 150);
        
        for (i = 0; i < taxDpsColumns.length; i++) {
            moDpsTaxesPane.addTableColumn(taxDpsColumns[i]);
        }
        
        // Listeners:
        
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        
        jbViewCfdiPdf.addActionListener(this);
        jbPickTaxRegion.addActionListener(this);
        jbViewDpsToLink.addActionListener(this);
        
        jbCopyRowSettings.addActionListener(this);
        jbPasteRowSettings.addActionListener(this);
        
        jbSelectItem.addActionListener(this);
        jbSelectUnit.addActionListener(this);
        jbSelectTaxRegion.addActionListener(this);
        jbSelectOperationsType.addActionListener(this); 
        
        jbSelectCostCenter.addActionListener(this);
        jbSelectItemReference.addActionListener(this);
        jbSelectAdjustmentSubtype.addActionListener(this);
        
        jbAsignDpsToLinkEntries.addActionListener(this);
        jbProcessAsService.addActionListener(this);
        
        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionCancel = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionCancel, "cancel", KeyEvent.VK_ESCAPE, 0);
        
        // Field con la información de la región de impuestos
        
        moFieldTaxRegion = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbTaxRegion, jlTaxRegion);
        moFieldTaxRegion.setPickerButton(jbPickTaxRegion);
        moFieldDpsNature = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbDpsNature, jlDpsNature);
        moFieldFunctionalSubArea = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFunctionalSubArea, jlFunctionalSubArea);
        
        mvFields = new Vector<>();
        //mvFields.add(moFieldTaxRegion); is not a DPS header field!
        mvFields.add(moFieldDpsNature);
        mvFields.add(moFieldFunctionalSubArea);
        
        SFormUtilities.populateComboBox(miClient, jcbTaxRegion, SDataConstants.FINU_TAX_REG);
        SFormUtilities.populateComboBox(miClient, jcbDpsNature, SDataConstants.TRNU_DPS_NAT);
        
        if (!miClient.getSessionXXX().getParamsCompany().getIsFunctionalAreas()) {
            SFormUtilities.populateComboBox(miClient, jcbFunctionalSubArea, SModConsts.CFGU_FUNC_SUB); // load all functional sub-areas, "non-applying" inclusive
        }
        else {
            SFormUtilities.populateComboBox(miClient, jcbFunctionalSubArea, SModConsts.CFGU_FUNC_SUB, new int[] { miClient.getSessionXXX().getUser().getPkUserId() }); // load only user-asigned functional sub-areas, "non-applying" may not be included
        }
        
        // Activar o desactivar componentes:
        
        jbViewCfdiPdf.setEnabled(isWithInvoicePdf());
        jcbTaxRegion.setEnabled(!isWithDpsToLink());
        jbPickTaxRegion.setEnabled(!isWithDpsToLink());
        jbViewDpsToLink.setEnabled(isWithDpsToLink());
        jcbDpsNature.setEnabled(!isWithDpsToLink());
        jcbFunctionalSubArea.setEnabled(!isWithDpsToLink() && miClient.getSessionXXX().getParamsCompany().getIsFunctionalAreas());
        
        jbCopyRowSettings.setEnabled(!isWithDpsToLink());
        jbPasteRowSettings.setEnabled(false);
        jbAsignDpsToLinkEntries.setEnabled(isWithDpsToLink());
        jbProcessAsService.setEnabled(isWithDpsToLink() && moDpsToLink.isOrder());
        enabledButtons(null);
        
        jtfDpsToLinkFolio.setEditable(isWithDpsToLink());
        jtfDpsToLinkDate.setEditable(isWithDpsToLink());
        jlDpsToLink.setEnabled(isWithDpsToLink());
        jlDpsToLinkDate.setEnabled(isWithDpsToLink());
        jlPoEntry.setEnabled(isWithDpsToLink());
        jlPoSurplusPct.setEnabled(isWithDpsToLink());
        jlPoOriginalQuantity.setEnabled(isWithDpsToLink());
        jlPoProcessedQuantity.setEnabled(isWithDpsToLink());
        jlPoPendingQuantity.setEnabled(isWithDpsToLink());
        jlPoBefore.setEnabled(isWithDpsToLink());
        jlPoAfter.setEnabled(isWithDpsToLink());
        
        // Borrar texto de las casillas:
        
        jtfPoEntry.setText("");
        jtfPoSurplusPct.setText("");
        jtfPoOriginalQuantity.setText("");
        jtfPoProcessedQuantity.setText("");
        jtfPoPendingQuantity.setText("");
        jtfPoProcessedQuantityCurrent.setText("");
        jtfPoPendingQuantityCurrent.setText("");
        
        moRowCfdiCopy = null;
    }
    
    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            moCfdiConceptsPane.getTable().requestFocus();
        }
    }
    
    private boolean isInvoice() {
        return mnDocumentType == SDataConstantsSys.TRNX_TP_DPS_DOC;
    }
    
    private boolean isWithInvoicePdf() {
        return moCfdiPdfFile != null;
    }
    
    private boolean isWithDpsToLink() {
        return moDpsToLink != null;
    }
    
    /**
     * Get default operation type for given CFDI row.
     * @param rowCfdiImport CFDI row. It must have an item already set.
     * @return 
     */
    private int getDefaultOperationsType(final SRowCfdiImport40 rowCfdiImport) {
        int type = 0;
        
        if (mbIsCreditNoteToApplyPrepayments) {
            type = SDataConstantsSys.TRNX_OPS_TYPE_ADJ_APP_PREPAY;
        }
        else {
            if (isInvoice()) {
                // importing an invoice
                
                type = rowCfdiImport.getItem().getIsPrepayment() ?
                        SDataConstantsSys.TRNX_OPS_TYPE_OPS_PREPAY_INVOICED :
                        SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS;
            }
            else {
                // importing a credit note

                type = rowCfdiImport.getItem().getIsPrepayment() ?
                        SDataConstantsSys.TRNX_OPS_TYPE_ADJ_PREPAY_INVOICED :
                        SDataConstantsSys.TRNX_OPS_TYPE_ADJ_OPS;
            }
        }
        
        return type;
    }
    
    private int pickAndGetItemId(final int selectionCase, final SRowCfdiImport40 rowCfdiImport) {
        int id = 0;
        
        if (moPickerItems == null) {
            moPickerItems = SFormOptionPickerItems.createOptionPicker(miClient, SDataConstants.ITMX_ITEM_IOG, moPickerItems);
        }

        moPickerItems.formReset();

        if (mbIsCreditNoteToApplyPrepayments) {
            moPickerItems.setFormParam(SLibConstants.VALUE_ADV_ONLY, true);
        }
        else if (selectionCase == CASE_ITEM) {
            moPickerItems.setFilterKey(SDataConstantsSys.ITMS_CL_ITEM_PUR_CON);
        }

        moPickerItems.formRefreshOptionPane();
        moPickerItems.setSelectedPrimaryKey(rowCfdiImport.getItem() != null ? new int [] { rowCfdiImport.getItem().getPkItemId() } : null);
        moPickerItems.setFormVisible(true); 

        if (moPickerItems.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            id = ((int[]) moPickerItems.getSelectedPrimaryKey())[0];
        }
        
        return id;
    }
    
    private SDataCostCenter getCostCenterByItem(final int itemId) {
        SDataCostCenter costCenter = null;
        
        try {
            String costCenterId = SDataUtilities.obtainCostCenterItem(miClient.getSession(), itemId);
            
            if (!costCenterId.isEmpty()) {
                costCenter = (SDataCostCenter) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_CC, new String[] { costCenterId }, SLibConstants.EXEC_MODE_SILENT);
            }
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
        
        return costCenter;
    }
    
    private void enabledButtons(final SRowCfdiImport40 rowCfdiImport) { 
        if (rowCfdiImport != null) {
            boolean enable = rowCfdiImport.getItem() != null;
            boolean isCreditNoteInvoicedAdvance = !isInvoice() && (rowCfdiImport.getItem() != null && rowCfdiImport.getItem().getIsPrepayment()) || (rowCfdiImport.getItem() == null && rowCfdiImport.isInvoicedAdvance());
            
            jbSelectItem.setEnabled(enable && (!isWithDpsToLink() || isCreditNoteInvoicedAdvance));
            jbSelectUnit.setEnabled(enable && !isWithDpsToLink());
            jbSelectTaxRegion.setEnabled(enable && !isWithDpsToLink()); 
            jbSelectOperationsType.setEnabled(enable && (!isWithDpsToLink() || isCreditNoteInvoicedAdvance)); 
            jbSelectCostCenter.setEnabled(enable);
            jbSelectItemReference.setEnabled(enable && rowCfdiImport.getItem().getDbmsDataItemGeneric().getIsItemReferenceRequired());
            jbSelectAdjustmentSubtype.setEnabled(enable && !isInvoice());
        }
        else {
            jbSelectItem.setEnabled(false);
            jbSelectUnit.setEnabled(false);
            jbSelectTaxRegion.setEnabled(false); 
            jbSelectOperationsType.setEnabled(false); 
            jbSelectCostCenter.setEnabled(false);
            jbSelectItemReference.setEnabled(false);
            jbSelectAdjustmentSubtype.setEnabled(false);
        }    
    }
    
    private void updateNameItem() {
        if (((SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow()).getItem().getDbmsDataItemGeneric().getIsItemNameEditable()) {
            String concept = ((String) moCfdiConceptsPane.getSelectedTableRow().getValues().get(COL_ITEM_NAME)); 
            concept = SLibUtils.textLeft(SLibUtils.textTrim(concept.toUpperCase()), SDataDpsEntry.LEN_CONCEPT);
            SRowCfdiImport40 row = (SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow();
            row.getItem().setItem(concept);
            for (SDataDpsEntry entry : row.getNewDpsEntries()) {
                entry.setConcept(concept);
            }
            row.prepareTableRow();
        }
        else {
            miClient.showMsgBoxInformation("El concepto que esta tratando modificar no es editable.");
        }
        
        int selectedRow = moCfdiConceptsPane.getTable().getSelectedRow();
        moCfdiConceptsPane.renderTableRows();
        moCfdiConceptsPane.setTableRowSelection(selectedRow);
    }
    
    private void updateConversionFactor() { 
        SRowCfdiImport40 row = (SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow();
        row.setConvFactor((double) moCfdiConceptsPane.getSelectedTableRow().getValues().get(COL_FACT_CONV));
        row.prepareTableRow();
        
        int selectedRow = moCfdiConceptsPane.getTable().getSelectedRow();
        moCfdiConceptsPane.renderTableRows();
        moCfdiConceptsPane.setTableRowSelection(selectedRow);
    }
    
    private void calculateRowTotalDpsEntries(final SRowCfdiImport40 rowCfdiImport) {
        int taxIdyEmisor, taxIdyReceiver;
        
        if (isWithDpsToLink()) {
            taxIdyEmisor = moDpsToLink.getFkTaxIdentityEmisorTypeId();
            taxIdyReceiver = moDpsToLink.getFkTaxIdentityReceptorTypeId();
        }
        else {
            taxIdyEmisor = moBizPartnerEmisor.getFkTaxIdentityId();
            taxIdyReceiver = moBizPartnerReceptor.getFkTaxIdentityId();
        }
        
        double xrt = miClient.getSession().getSessionCustom().isLocalCurrency(new int[] { mnCfdiCurrencyId }) ? 1.0 : moComprobante.getAttTipoCambio().getDouble();
        
        rowCfdiImport.calculateTotalDpsEntries(moComprobante.getAttFecha().getDatetime(), taxIdyEmisor, taxIdyReceiver, xrt);
    }
    
    private void validateTaxes(final SRowCfdiImport40 rowCfdiImport) {
        boolean showMessage = false;
        String message = "No corresponden los impuestos con la " + msDocumentName + ":\n";
        DElementConcepto concepto = rowCfdiImport.getConcepto();
        if (concepto.getEltOpcConceptoImpuestos() != null) {
            if (concepto.getEltOpcConceptoImpuestos().getEltOpcImpuestosTrasladados() != null) {
                ArrayList<cfd.ver40.DElementConceptoImpuestoTraslado> traslados = concepto.getEltOpcConceptoImpuestos().getEltOpcImpuestosTrasladados().getEltImpuestoTrasladados();
                TAXES:
                for (DElementConceptoImpuestoTraslado traslado : traslados) {
                    if (!rowCfdiImport.getTaxChargedMatched().contains(traslado)) {
                        message += "Impuesto: " + DCfdi40Catalogs.Impuesto.get(traslado.getAttImpuesto().getString()) + ".\n"
                                + "Tipo: trasladado. \n"
                                + "Factor: " + traslado.getAttTipoFactor().getString() + " de "
                                + SLibUtils.DecimalFormatPercentage2D.format(traslado.getAttTasaOCuota().getDouble()) + ".";
                        showMessage = true;
                    }
                }
            }

            if (concepto.getEltOpcConceptoImpuestos().getEltOpcImpuestosRetenciones() != null) {
                ArrayList<cfd.ver40.DElementConceptoImpuestoRetencion> retenciones = concepto.getEltOpcConceptoImpuestos().getEltOpcImpuestosRetenciones().getEltImpuestoRetenciones();
                TAXES:
                for (DElementConceptoImpuestoRetencion retencion : retenciones) {
                    if (!rowCfdiImport.getTaxRetainedMatched().contains(retencion)) { 
                        message += "Impuesto: " + DCfdi40Catalogs.Impuesto.get(retencion.getAttImpuesto().getString()) + ".\n"
                                + "Tipo: retenido. \n"
                                + "Factor: " +retencion.getAttTipoFactor().getString() + " de "
                                + SLibUtils.DecimalFormatPercentage2D.format(retencion.getAttTasaOCuota().getDouble()) + ".";
                        showMessage = true;
                    }
                }
            }
        }
        if (showMessage) {
            miClient.showMsgBoxWarning(message);
        }
    }
    
    private void renderTableCfdiTaxes(final int selectedCfdiRow) { 
        moCfdiTaxesPane.createTable();
        moCfdiTaxesPane.clearTableRows();
        
        cfd.ver40.DElementConceptoImpuestos oImpuestos = moComprobante.getEltConceptos().getEltConceptos().get(selectedCfdiRow).getEltOpcConceptoImpuestos();
        
        if (oImpuestos != null) {
            if (oImpuestos.getEltOpcImpuestosTrasladados() != null) {
                for (int i = 0; i < oImpuestos.getEltOpcImpuestosTrasladados().getEltImpuestoTrasladados().size(); i++) {
                    moCfdiTaxesPane.addTableRow(new SRowCfdiTaxImport40(oImpuestos, SModSysConsts.FINS_TP_TAX_CHARGED, i));
                }
            }
            
            if (oImpuestos.getEltOpcImpuestosRetenciones() != null) {
                for (int i = 0; i < oImpuestos.getEltOpcImpuestosRetenciones().getEltImpuestoRetenciones().size(); i++) {
                    moCfdiTaxesPane.addTableRow(new SRowCfdiTaxImport40(oImpuestos, SModSysConsts.FINS_TP_TAX_RETAINED, i));
                }
            }
            
            moCfdiTaxesPane.renderTableRows();
            moCfdiTaxesPane.setTableRowSelection(0);
        }
    }
    
    private void renderTableDpsTaxes(final SRowCfdiImport40 rowCfdiImport) { 
        moDpsTaxesPane.createTable();
        moDpsTaxesPane.clearTableRows();
        
        if (!rowCfdiImport.getNewDpsEntries().isEmpty()) {
            for (SDataDpsEntry dpsEntry : rowCfdiImport.getNewDpsEntries()) {
                for (int tax = 0; tax < dpsEntry.getDbmsEntryTaxes().size(); tax++) {
                    SDataDpsEntryTax entryTax = dpsEntry.getDbmsEntryTaxes().get(tax);
                    boolean found = false;
                    
                    for (int row = 0; row < moDpsTaxesPane.getTableModelRowCount(); row++) {
                        SDataDpsEntryTaxRow entryTaxRow = (SDataDpsEntryTaxRow) moDpsTaxesPane.getTableRow(row);
                        if (entryTax.getPkTaxBasicId() == entryTaxRow.getDpsEntryTax().getPkTaxBasicId() &&
                                entryTax.getPkTaxId() == entryTaxRow.getDpsEntryTax().getPkTaxId() &&
                                entryTax.getFkTaxTypeId() == entryTaxRow.getDpsEntryTax().getFkTaxTypeId()) {
                            found = true;
                        }
                    }
                    
                    if (!found) {
                        moDpsTaxesPane.addTableRow(new SDataDpsEntryTaxRow(entryTax));
                    }
                }
            }
            
            moDpsTaxesPane.renderTableRows();
            moDpsTaxesPane.setTableRowSelection(0);
        }
    }
    
    private void renderPanelDpsToLinkEntry() {
        jtfPoEntry.setText("");
        jtfPoSurplusPct.setText("");
        
        jtfPoOriginalQuantity.setText("");
        jtfPoProcessedQuantity.setText("");
        jtfPoPendingQuantity.setText("");
        
        jtfPoProcessedQuantityCurrent.setText("");
        jtfPoPendingQuantityCurrent.setText("");
        
        if (isWithDpsToLink()) {
            SRowCfdiImport40 rowCfdiImport = (SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow();

            if (!rowCfdiImport.getImportedEntryDpsDpsLinks().isEmpty()) {
                SDataEntryDpsDpsLink entryDpsDpsLink = rowCfdiImport.getImportedEntryDpsDpsLinks().get(0);

                jtfPoEntry.setText(SLibUtils.DecimalFormatValue0D.format(entryDpsDpsLink.getSortingPosition()));
                jtfPoSurplusPct.setText(SLibUtils.DecimalFormatPercentage2D.format(entryDpsDpsLink.getSurplusPercentage()));

                jtfPoOriginalQuantity.setText(SLibUtils.DecimalFormatValue2D.format(entryDpsDpsLink.getQuantity()));
                jtfPoProcessedQuantity.setText(SLibUtils.DecimalFormatValue2D.format(entryDpsDpsLink.getQuantityLinked()));
                jtfPoPendingQuantity.setText(SLibUtils.DecimalFormatValue2D.format(entryDpsDpsLink.getQuantity() - entryDpsDpsLink.getQuantityLinked()));

                double processedQuantityCurrent = 0;
                for (int i = 0; i < moCfdiConceptsPane.getTableGuiRowCount(); i++) {
                    SRowCfdiImport40 auxRowCfdiImport = (SRowCfdiImport40) moCfdiConceptsPane.getTableRow(i);

                    if (!auxRowCfdiImport.getImportedEntryDpsDpsLinks().isEmpty()) {
                        if (SLibUtils.compareKeys(entryDpsDpsLink.getDpsEntryKey(), auxRowCfdiImport.getImportedEntryDpsDpsLinks().get(0).getDpsEntryKey())) {
                            processedQuantityCurrent += auxRowCfdiImport.getImportedEntryDpsDpsLinks().get(0).getQuantityToLink();
                        } 
                    }
                }

                jtfPoProcessedQuantityCurrent.setText(SLibUtils.DecimalFormatValue2D.format(entryDpsDpsLink.getQuantityLinked() + processedQuantityCurrent));
                jtfPoPendingQuantityCurrent.setText(SLibUtils.DecimalFormatValue2D.format(entryDpsDpsLink.getQuantity() - entryDpsDpsLink.getQuantityLinked() - processedQuantityCurrent)); 
            }
        }
    }
    
    private void renderLabelIsItemNameEditable(SRowCfdiImport40 rowCfdiImport) {
        if (rowCfdiImport != null && rowCfdiImport.getItem() != null) {
            jlIsItemNameEditable.setText(rowCfdiImport.getItem().getDbmsDataItemGeneric().getIsItemNameEditable() ? "El concepto es editable." : "El concepto no es editable.");
        }
        else {
            jlIsItemNameEditable.setText("El renglón no tiene asignado un ítem.");
        }
    }
    
    private boolean canCfdiRowBeShownInCfdiConceptsLinker(final int linkType, final SRowCfdiImport40 rowCfdiImport) {
        boolean canShowDialog = false;
        
        switch (linkType) {
            case SRowCfdiImport40.LINK_1_ON_1:
                canShowDialog = true;
                break;
                
            case SRowCfdiImport40.LINK_AS_SERVICE:
                try {
                    String unitCode = rowCfdiImport.getConcepto().getAttClaveUnidad().getString();

                    if (unitCode.equalsIgnoreCase(DCfdi40Catalogs.ClaveUnidadServicio) || unitCode.equals(DCfdi40Catalogs.ClaveUnidadUnidad)) {
                        String message = "";
                        boolean confirmToProceedWithUnitUnits = false;
                        boolean hasDpsToLinkServiceUnits = false;
                        
                        if (unitCode.equals(DCfdi40Catalogs.ClaveUnidadUnidad)) {
                            message += "El concepto del CFDI seleccionado tiene la ClaveUnidad '" + DCfdi40Catalogs.ClaveUnidadUnidad + "'.";
                            confirmToProceedWithUnitUnits = true;
                        }

                        String sql = "SELECT cu.code FROM trn_dps_ety AS de " +
                                "INNER JOIN erp.itmu_unit AS u ON de.fid_orig_unit = u.id_unit " +
                                "INNER JOIN erp.itms_cfd_unit AS cu ON u.fid_cfd_unit = cu.id_cfd_unit " +
                                "WHERE de.id_year = " + moDpsToLink.getPkYearId() + " AND de.id_doc = " + moDpsToLink.getPkDocId() + " " +
                                "AND cu.code in ('" + DCfdi40Catalogs.ClaveUnidadServicio + "', '" + DCfdi40Catalogs.ClaveUnidadUnidad + "');";
                        try (ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql)) {
                            int unitUnitsCount = 0;
                            
                            while (resultSet.next()) {
                                hasDpsToLinkServiceUnits = true;
                                
                                if (resultSet.getString(1).equals(DCfdi40Catalogs.ClaveUnidadUnidad)) {
                                    unitUnitsCount++;
                                }
                            }
                            
                            if (unitUnitsCount > 0) {
                                message += (message.isEmpty() ? "" : "\n") + (unitUnitsCount == 1 ? "Una partida de la " + msDocumentName + " tiene" : (unitUnitsCount + " partidas de la " + msDocumentName + " tienen")) + " la ClaveUnidad '" + DCfdi40Catalogs.ClaveUnidadUnidad + "'.";
                                confirmToProceedWithUnitUnits = true;
                            }
                        }

                        if (hasDpsToLinkServiceUnits) {
                            if (confirmToProceedWithUnitUnits) {
                                message += "\n" + SLibConstants.MSG_CNF_MSG_CONT;
                                canShowDialog = miClient.showMsgBoxConfirm(message) == JOptionPane.OK_OPTION;
                            }
                            else {
                                canShowDialog = true;
                            }
                        }
                        else {
                            miClient.showMsgBoxInformation("La " + msDocumentName + " no se puede procesar como servicio porque no tiene partidas con la ClaveUnidad "
                                    + "'" + DCfdi40Catalogs.ClaveUnidadServicio + "' o '" + DCfdi40Catalogs.ClaveUnidadUnidad + "'.");
                        }
                    }
                    else {
                        miClient.showMsgBoxInformation("El concepto del CFDI seleccionado no se puede procesar como servicio porque su ClaveUnidad, '" + unitCode + "', no es "
                                + "'" + DCfdi40Catalogs.ClaveUnidadServicio + "' ni '" + DCfdi40Catalogs.ClaveUnidadUnidad + "'.");
                    }
                }
                catch (SQLException e) {
                    SLibUtilities.renderException(this, e);
                }
                break;
                
            default:
                // nothing
        }
        
        return canShowDialog;
    }
    
    private void updateCfdiRowIntoCfdiConceptsLinker(final int linkType, SRowCfdiImport40 rowCfdiImport) {
        // IMPORTANT: Order of set values into concepts linker dialog must be obeyed!
        
        moDialogCfdiConceptsLinker40.setValue(SDialogCfdiConceptsLinker40.VALUE_IN_DPS_TO_LINK, moDpsToLink); // 1st value
                
        HashMap<String, Double> dpsToLinkEntries = new HashMap<>(); // key: PK of entry of purchase order as a String; value: total quantity already assigned from entry

        for (SGridRow row : moCfdiConceptsPane.getTableModel().getTableRows()) {
            SRowCfdiImport40 rci = (SRowCfdiImport40) row; // variable de conveniencia

            if (rci != rowCfdiImport && !rci.getImportedEntryDpsDpsLinks().isEmpty()) {
                for (SDataEntryDpsDpsLink entryDpsDpsLink : rci.getImportedEntryDpsDpsLinks()) {
                    String key = SLibUtils.textKey(entryDpsDpsLink.getDpsEntryKey());
                    Double quantityToLink = dpsToLinkEntries.get(key);

                    if (quantityToLink == null) {
                        quantityToLink = entryDpsDpsLink.getQuantityToLink();
                    }
                    else {
                        quantityToLink += entryDpsDpsLink.getQuantityToLink();
                    }
                    
                    dpsToLinkEntries.put(key, quantityToLink);
                }
            }
        }
        
        moDialogCfdiConceptsLinker40.setValue(SDialogCfdiConceptsLinker40.VALUE_IN_DPS_TO_LINK_ENTRIES, dpsToLinkEntries); // 2nd value
        moDialogCfdiConceptsLinker40.setValue(SDialogCfdiConceptsLinker40.VALUE_IN_CFDI_ROW, rowCfdiImport); // 3th value
        moDialogCfdiConceptsLinker40.setValue(SDialogCfdiConceptsLinker40.VALUE_IN_DPS_ENTRY_DPS_DPS_LINKS, rowCfdiImport.getImportedEntryDpsDpsLinks()); // 4th value
        
        int[] adjustmentSubtypeKey = null;
        
        if (!isInvoice()) {
            // importing a credit note
            
            adjustmentSubtypeKey = rowCfdiImport.getAdjustmentSubtypeKey();
            
            if (adjustmentSubtypeKey == null) {
                if (mbIsCreditNoteToApplyPrepayments) {
                    adjustmentSubtypeKey = SDataConstantsSys.TRNS_STP_DPS_ADJ_DISC_DISC;
                }
                else {
                    adjustmentSubtypeKey = manAdjustmentSubtypeKey;
                }
            }
        }
        
        moDialogCfdiConceptsLinker40.setFormSettings(linkType, adjustmentSubtypeKey);
    }
    
    @SuppressWarnings("unchecked")
    private boolean updateCfdiRowFromCfdiConceptsLinker(final int linkType, final SRowCfdiImport40 rowCfdiImport) {
        // Validate selected DPS entries:
        
        if (mbIsCreditNoteToApplyPrepayments) {
            int entryNum = 0;
            ArrayList<SDataDpsEntry> dpsEntries = (ArrayList<SDataDpsEntry>) moDialogCfdiConceptsLinker40.getValue(SDialogCfdiConceptsLinker40.VALUE_OUT_DPS_ENTRIES); // 3th value
            
            for (SDataDpsEntry dpsEntry : dpsEntries) {
                entryNum++;
                SDataItem item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int [] { dpsEntry.getFkItemId() }, SLibConstants.EXEC_MODE_SILENT);
                
                if (!item.getIsPrepayment()) {
                    // pick a prepayment item for current selected DPS entry:
                    
                    miClient.showMsgBoxInformation("Favor de elegir un ítem configurado como \"anticipo para facturar\" "
                            + "para el renglón seleccionado #" + (entryNum) + " de la " + msDocumentName + ".");
                    
                    int itemId = pickAndGetItemId(CASE_ITEM, rowCfdiImport);

                    if (itemId != 0) {
                        item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int [] { itemId }, SLibConstants.EXEC_MODE_SILENT);
                        dpsEntry.setFkItemId(item.getPkItemId());
                        dpsEntry.setFkOriginalUnitId(item.getFkUnitId());
                        
                        // set default cost center of picked item:
                        SDataCostCenter costCenter = getCostCenterByItem(itemId);
                        if (costCenter != null) {
                            dpsEntry.setFkCostCenterId_n(costCenter.getPkCostCenterIdXXX());
                        }
                    }
                    else {
                        miClient.showMsgBoxInformation("Se debe elegir un ítem configurado como \"anticipo para facturar\" "
                                + "para " + (dpsEntries.size() == 1 ? "el renglón seleccionado" : "todos los renglones seleccionados") + " de la " + msDocumentName + ".\n"
                                + "Se cancela la asignación de partidas de la " + msDocumentName + " al CFDI.");
                        return false; // abort
                    }
                }
            }
        }
        
        // Update CFDI row from concepts linker dialog:
        // IMPORTANT: Order of get values from concepts linker dialog must be obeyed!
        
        rowCfdiImport.getImportedEntryDpsDpsLinks().clear();
        rowCfdiImport.getImportedEntryDpsDpsLinks().addAll((ArrayList<SDataEntryDpsDpsLink>) moDialogCfdiConceptsLinker40.getValue(SDialogCfdiConceptsLinker40.VALUE_OUT_DPS_ENTRY_DPS_DPS_LINKS)); // 1st value
        
        rowCfdiImport.setCfdLinkType(linkType);
        rowCfdiImport.setConvFactor((double) moDialogCfdiConceptsLinker40.getValue(SDialogCfdiConceptsLinker40.VALUE_OUT_CONV_FACTOR)); // 2nd value
        
        ArrayList<SDataDpsEntry> dpsEntries = (ArrayList<SDataDpsEntry>) moDialogCfdiConceptsLinker40.getValue(SDialogCfdiConceptsLinker40.VALUE_OUT_DPS_ENTRIES); // 3th value
        
        for (SDataDpsEntry dpsEntry : dpsEntries) {
            SDataItem item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int [] { dpsEntry.getFkItemId() }, SLibConstants.EXEC_MODE_SILENT);
            
            rowCfdiImport.setItem(item);

            // si se requiere, se asigna el ítem de referencia de la partida de la OC o el predefinido del ítem principal:
            if (rowCfdiImport.getItem().getDbmsDataItemGeneric().getIsItemReferenceRequired()) {
                int itemReferenceId;

                if (dpsEntry.getFkItemRefId_n() != 0) {
                    itemReferenceId = dpsEntry.getFkItemRefId_n();
                }
                else {
                    itemReferenceId = rowCfdiImport.getItem().getDbmsFkDefaultItemRefId_n();
                }

                SDataItem itemReference = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { itemReferenceId }, SLibConstants.EXEC_MODE_SILENT);
                rowCfdiImport.setItemReference(itemReference);
            }
            else {
                rowCfdiImport.setItemReference(null);
            }

            // se asigna la unidad que viene en la partida del pedido:
            SDataUnit unit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int [] { dpsEntry.getFkOriginalUnitId() }, SLibConstants.EXEC_MODE_SILENT);
            rowCfdiImport.setUnit(unit);

            // se asigna la región de impuestos de la partida del pedido:
            SDataTaxRegion taxRegion = (SDataTaxRegion) SDataUtilities.readRegistry(miClient, SDataConstants.FINU_TAX_REG, new int[] { dpsEntry.getFkTaxRegionId() }, SLibConstants.EXEC_MODE_SILENT);
            rowCfdiImport.setTaxRegion(taxRegion);

            // se asigna el centro de costo de la partida del pedido o el predefinido del ítem principal:
            SDataCostCenter costCenter;

            if (!dpsEntry.getFkCostCenterId_n().isEmpty()) {
                costCenter = (SDataCostCenter) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_CC, new String[] { dpsEntry.getFkCostCenterId_n() }, SLibConstants.EXEC_MODE_SILENT);
            }
            else {
                costCenter = getCostCenterByItem(rowCfdiImport.getItem().getPkItemId());
            }

            rowCfdiImport.setCostCenter(costCenter);

            // se asigna tipo de operación por defecto:
            if (rowCfdiImport.getOperationsType() == 0) {
                rowCfdiImport.setOperationsType(getDefaultOperationsType(rowCfdiImport));
            }
        }
        
        if (!isInvoice()) {
            // importing a credit note
            
            int[] adjustmentSubtypeKey = (int[]) moDialogCfdiConceptsLinker40.getValue(SDialogCfdiConceptsLinker40.VALUE_OUT_ADJ_SUBTYPE); // 4th value
            
            if (adjustmentSubtypeKey != null) {
                rowCfdiImport.setAdjustmentSubtypeKey(adjustmentSubtypeKey);
                rowCfdiImport.setAdjustmentSubtypeName(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_STP_DPS_ADJ, adjustmentSubtypeKey));
            }
            else {
                rowCfdiImport.setAdjustmentSubtypeKey(null);
                rowCfdiImport.setAdjustmentSubtypeName("");
            }
        }
        
        rowCfdiImport.getImportedDpsEntries().clear();
        rowCfdiImport.getImportedDpsEntries().addAll(dpsEntries);
        
        return true; // go on
    }
    
    private void refreshRowEntriesAndTotalAndTaxes(final SRowCfdiImport40 rowCfdiImport) {
        try {
            rowCfdiImport.refreshDpsEntries();
            
            calculateRowTotalDpsEntries(rowCfdiImport); 
            renderTableDpsTaxes(rowCfdiImport);
            
            DElementConceptoImpuestos impuestos = rowCfdiImport.getConcepto().getEltOpcConceptoImpuestos();
            
            if (impuestos != null) {
                if (impuestos.getEltOpcImpuestosTrasladados() != null) {
                    ArrayList<cfd.ver40.DElementConceptoImpuestoTraslado> trasladado = impuestos.getEltOpcImpuestosTrasladados().getEltImpuestoTrasladados();
                    
                    for (int i = 0; trasladado.size() > i; i++) {
                        SDataTax tax = SCfdUtils.obtainTaxCharged(miClient, trasladado.get(i));
                        
                        if (tax != null) {
                            int cantDpsEntries = rowCfdiImport.getNewDpsEntries().size();
                            for (SDataDpsEntry newDpsEntry : rowCfdiImport.getNewDpsEntries()) {
                                for (int j = 0; j < newDpsEntry.getDbmsEntryTaxes().size(); j++) {
                                    SDataDpsEntryTax dpsEntryTax = newDpsEntry.getDbmsEntryTaxes().get(j);

                                    if (dpsEntryTax.getPkTaxBasicId() == tax.getPkTaxBasicId() &&
                                            dpsEntryTax.getPkTaxId() == tax.getPkTaxId() &&
                                            SLibUtils.DecimalFormatPercentage4D.format(dpsEntryTax.getPercentage()).equals(SLibUtils.DecimalFormatPercentage4D.format(trasladado.get(i).getAttTasaOCuota().getDouble())) &&
                                            dpsEntryTax.getFkTaxTypeId() == SModSysConsts.FINS_TP_TAX_CHARGED) {
                                        
                                        if (rowCfdiImport.isLinkedAsService()) {
                                            newDpsEntry.getDbmsEntryTaxes().get(j).setTaxCy(SLibUtils.round(newDpsEntry.getOriginalPriceUnitaryCy() * trasladado.get(i).getAttTasaOCuota().getDouble(), SErpConsts.VAL_QTY_MAX_DECS));
                                        }
                                        else {
                                            newDpsEntry.getDbmsEntryTaxes().get(j).setTaxCy(trasladado.get(i).getAttImporte().getDouble() / cantDpsEntries);
                                        }
                                        
                                        rowCfdiImport.addTaxChargedMatched(trasladado.get(i)); 
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (impuestos.getEltOpcImpuestosRetenciones() != null) {
                    ArrayList<cfd.ver40.DElementConceptoImpuestoRetencion> retencion = impuestos.getEltOpcImpuestosRetenciones().getEltImpuestoRetenciones();
                    
                    for (int i = 0; retencion.size() > i ; i++) {                
                        SDataTax tax = SCfdUtils.obtainTaxRetained(miClient, retencion.get(i));
                        
                        if (tax != null) {
                            int cantDpsEntries = rowCfdiImport.getNewDpsEntries().size();
                            for (SDataDpsEntry newDpsEntry : rowCfdiImport.getNewDpsEntries()) {
                                for (int j = 0; j < newDpsEntry.getDbmsEntryTaxes().size(); j++) {
                                    SDataDpsEntryTax dpsEntryTax = newDpsEntry.getDbmsEntryTaxes().get(j);

                                    if (dpsEntryTax.getPkTaxBasicId() == tax.getPkTaxBasicId() &&
                                            dpsEntryTax.getPkTaxId() == tax.getPkTaxId() &&
                                            SLibUtils.DecimalFormatPercentage4D.format(dpsEntryTax.getPercentage()).equals(SLibUtils.DecimalFormatPercentage4D.format(retencion.get(i).getAttTasaOCuota().getDouble())) &&
                                            dpsEntryTax.getFkTaxTypeId() == SModSysConsts.FINS_TP_TAX_RETAINED) {

                                        if (rowCfdiImport.isLinkedAsService()) {
                                            newDpsEntry.getDbmsEntryTaxes().get(j).setTaxCy(SLibUtils.round(newDpsEntry.getOriginalPriceUnitaryCy() * retencion.get(i).getAttTasaOCuota().getDouble(), SErpConsts.VAL_PRC_UNT_MAX_DECS));
                                        }
                                        else {
                                            newDpsEntry.getDbmsEntryTaxes().get(j).setTaxCy(retencion.get(i).getAttImporte().getDouble() / cantDpsEntries);
                                        }
                                        
                                        rowCfdiImport.addTaxRetainedMatched(retencion.get(i));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    private void saveMatchingItemConcept(SRowCfdiImport40 rowCfdiImport) {
        try {
            SDataMatchingItemBizPartnerConcept mibpc = new SDataMatchingItemBizPartnerConcept();
            
            int id = SItemUtilities.getMatchingItemBizPartnerConceptId(miClient,
                    rowCfdiImport.getConcepto().getAttNoIdentificacion().getString(),
                    rowCfdiImport.getConcepto().getAttClaveProdServ().getString(),
                    moBizPartnerEmisor.getPkBizPartnerId(),
                    rowCfdiImport.getItem().getPkItemId());
            
            if (id == 0) {
                mibpc.setConceptKey(rowCfdiImport.getConcepto().getAttNoIdentificacion().getString());
                mibpc.setConceptProductService(rowCfdiImport.getConcepto().getAttClaveProdServ().getString());
                
                mibpc.setFactorConversion(rowCfdiImport.getConvFactor());
                mibpc.setUses(1);
                mibpc.setUseFirst(miClient.getSession().getSystemDate());
                mibpc.setUseLast(miClient.getSession().getSystemDate());
                mibpc.setIsDeleted(false);
                mibpc.setFkBizPartnerId(moBizPartnerEmisor.getPkBizPartnerId());
                mibpc.setFkItemId(rowCfdiImport.getItem().getPkItemId());
                mibpc.setFkUnitId(rowCfdiImport.getUnit().getPkUnitId());
                mibpc.setFkTaxRegionId(rowCfdiImport.getTaxRegion().getPkTaxRegionId());
                mibpc.setFkItemReferenceId_n(rowCfdiImport.getItemReference() == null ? 0 : rowCfdiImport.getItemReference().getPkItemId());
                mibpc.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
                mibpc.setFkCostCenterId_n(rowCfdiImport.getCostCenter() == null ? "" : rowCfdiImport.getCostCenter().getPkCostCenterIdXXX());
            }
            else {
                mibpc.read(id, miClient.getSession().getStatement());
                
                //mibpc.setConceptKey(...);
                //mibpc.setConceptProductService(...);
                mibpc.setFactorConversion(rowCfdiImport.getConvFactor());
                mibpc.setUses(mibpc.getUses() + 1);
                //mibpc.setUseFirst(...);
                mibpc.setUseLast(miClient.getSession().getSystemDate());
                //mibpc.setIsDeleted(...);
                //mibpc.setFkBizPartnerId(...);
                mibpc.setFkUnitId(rowCfdiImport.getUnit().getPkUnitId());
                mibpc.setFkTaxRegionId(rowCfdiImport.getTaxRegion().getPkTaxRegionId());
                mibpc.setFkItemReferenceId_n(rowCfdiImport.getItemReference() == null ? 0 : rowCfdiImport.getItemReference().getPkItemId());
                mibpc.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
                mibpc.setFkCostCenterId_n(rowCfdiImport.getCostCenter() == null ? "" : rowCfdiImport.getCostCenter().getPkCostCenterIdXXX());
            }
            
            mibpc.save(miClient.getSession().getStatement().getConnection());
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }

    private void retrieveMatchingItemConcept(SRowCfdiImport40 rowCfdiImport) { // Carga los datos empatados con anterioridad.
        int matchingId = SItemUtilities.getMatchingItemBizPartnerConceptId(miClient,
                rowCfdiImport.getConcepto().getAttNoIdentificacion().getString(),
                rowCfdiImport.getConcepto().getAttClaveProdServ().getString(),
                moBizPartnerEmisor.getPkBizPartnerId());
        
        if (matchingId != 0) {
            // Se cargan los datos del empate preexistente:
            
            SDataMatchingItemBizPartnerConcept mibpc = new SDataMatchingItemBizPartnerConcept();
            mibpc.read(matchingId, miClient.getSession().getStatement());
            
            SDataItem item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { mibpc.getFkItemId() }, SLibConstants.EXEC_MODE_SILENT);
            SDataItem itemReference = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { mibpc.getFkItemReferenceId_n() }, SLibConstants.EXEC_MODE_SILENT);
            SDataUnit unit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int [] { mibpc.getFkUnitId() }, SLibConstants.EXEC_MODE_SILENT);
            SDataTaxRegion taxRegion = (SDataTaxRegion) SDataUtilities.readRegistry(miClient, SDataConstants.FINU_TAX_REG, new int [] { mibpc.getFkTaxRegionId() }, SLibConstants.EXEC_MODE_SILENT);
            SDataCostCenter costCenter = (SDataCostCenter) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_CC, new String[] { mibpc.getFkCostCenterId_n() }, SLibConstants.EXEC_MODE_SILENT);
           
            if (item.getDbmsDataItemGeneric().getIsItemReferenceRequired() && itemReference == null) {
                itemReference = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { item.getDbmsFkDefaultItemRefId_n() }, SLibConstants.EXEC_MODE_SILENT);
            }
            
            // Se asignan los valores:
            rowCfdiImport.setItem(item);
            rowCfdiImport.setItemReference(itemReference);
            rowCfdiImport.setUnit(unit);
            rowCfdiImport.setTaxRegion(taxRegion);
            rowCfdiImport.setCostCenter(costCenter);
            rowCfdiImport.setConvFactor(mibpc.getFactorConversion());
            rowCfdiImport.setOperationsType(getDefaultOperationsType(rowCfdiImport));
            
            rowCfdiImport.prepareTableRow();
            
            refreshRowEntriesAndTotalAndTaxes(rowCfdiImport);
        }
    }

    private void populateCfdiTables() {
        moCfdiConceptsPane.createTable(this);
        
        for (int i = 0; i < moComprobante.getEltConceptos().getEltConceptos().size(); i++) {
            SRowCfdiImport40 row = new SRowCfdiImport40(miClient, mnDocumentType, moComprobante.getEltConceptos().getEltConceptos().get(i), i + 1);
            retrieveMatchingItemConcept(row);
            moCfdiConceptsPane.addTableRow(row);
        }
        
        moCfdiConceptsPane.renderTableRows();
        moCfdiConceptsPane.setTableRowSelection(0);
        moCfdiConceptsPane.getTable().getColumnModel().getColumn(COL_FACT_CONV).setCellEditor(moCfdiConceptsPane.getTable().getDefaultEditor(Number.class));
        moCfdiConceptsPane.getTable().getColumnModel().getColumn(COL_ITEM_NAME).setCellEditor(moCfdiConceptsPane.getTable().getDefaultEditor(Number.class));
        moCfdiConceptsPane.getTable().getColumnModel().getColumn(COL_FACT_CONV).getCellEditor().addCellEditorListener(this);
        moCfdiConceptsPane.getTable().getColumnModel().getColumn(COL_ITEM_NAME).getCellEditor().addCellEditorListener(this);
        moCfdiConceptsPane.getTable().getTableHeader().setReorderingAllowed(false);
        
        renderTableCfdiTaxes(moCfdiConceptsPane.getTable().getSelectedRow());
        renderTableDpsTaxes((SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow());
        renderPanelDpsToLinkEntry();
    }
       
    /**
     * Asigna los valores del comprobante CFDI a la forma para realizar el empate de los conceptos con los conceptos del CFDI.
     * @param comprobante CFDI.
     * @param adjustmentSubtypeKey Subtipo de ajuste. Es opcional al crear notas de crédito;; en caso contrario debe ser <code>null</code>.
     */
    public void setComprobante(final cfd.ver40.DElementComprobante comprobante, final int[] adjustmentSubtypeKey) {
        moComprobante = comprobante;
        manAdjustmentSubtypeKey = adjustmentSubtypeKey;
        mnDocumentType = moComprobante.getAttTipoDeComprobante().getString().equals(DCfdi40Catalogs.CFD_TP_I) ? SDataConstantsSys.TRNX_TP_DPS_DOC : SDataConstantsSys.TRNX_TP_DPS_ADJ;
        mbIsCreditNoteToApplyPrepayments = false;
        
        if (isInvoice()) {
            // importing an invoice
            
            msDocumentName = "OC";
            jlDpsToLink.setText(jlDpsToLink.getText().replaceAll("<Document>", "Orden compra"));
            jbViewDpsToLink.setToolTipText(jbViewDpsToLink.getToolTipText().replaceAll("<document>", "orden compra"));
            jbAsignDpsToLinkEntries.setText(jbAsignDpsToLinkEntries.getText().replaceAll("<doc>", "OC"));
            ((TitledBorder) jpDpsToLinkEntry.getBorder()).setTitle(((TitledBorder) jpDpsToLinkEntry.getBorder()).getTitle().replaceAll("<document>", "orden compra"));
            ((TitledBorder) jpDpsTaxes.getBorder()).setTitle(((TitledBorder) jpDpsTaxes.getBorder()).getTitle().replaceAll("<doc>", "OC"));
        }
        else {
            // importing a credit note
            
            msDocumentName = "factura";
            jlDpsToLink.setText(jlDpsToLink.getText().replaceAll("<Document>", "Factura"));
            jbViewDpsToLink.setToolTipText(jbViewDpsToLink.getToolTipText().replaceAll("<document>", "factura"));
            jbAsignDpsToLinkEntries.setText(jbAsignDpsToLinkEntries.getText().replaceAll("<doc>", "factura"));
            ((TitledBorder) jpDpsToLinkEntry.getBorder()).setTitle(((TitledBorder) jpDpsToLinkEntry.getBorder()).getTitle().replaceAll("<document>", "factura"));
            ((TitledBorder) jpDpsTaxes.getBorder()).setTitle(((TitledBorder) jpDpsTaxes.getBorder()).getTitle().replaceAll("<doc>", "factura"));
            
            if (moComprobante.getEltOpcCfdiRelacionados() != null && !moComprobante.getEltOpcCfdiRelacionados().isEmpty()) {
                mbIsCreditNoteToApplyPrepayments = moComprobante.getEltOpcCfdiRelacionados().get(0).getAttTipoRelacion().getString().equals(DCfdi40Catalogs.ClaveTipoRelaciónAplicaciónAnticipo);
            }
        }
        
        jtfCfdiEmisor.setText(comprobante.getEltEmisor().getAttNombre().getString());
        jtfCfdiEmisorFiscalId.setText(comprobante.getEltEmisor().getAttRfc().getString());
        jtfCfdiFolio.setText(SDocumentUtils.composeFolio(comprobante.getAttSerie().getString(), comprobante.getAttFolio().getString(), moComprobante.getEltOpcComplementoTimbreFiscalDigital().getAttUUID().getString()));
        jtfCfdiPaymentMethod.setText(comprobante.getAttMetodoPago().getString());
        jtfCfdiDate.setText(SLibUtils.DbmsDateFormatDatetime.format(comprobante.getAttFecha().getDatetime()));
        
        jtfCurrency.setText(comprobante.getAttMoneda().getString());
        jtfExchangeRate.setText(SLibUtils.getDecimalFormatExchangeRate().format(comprobante.getAttTipoCambio().getDouble() == 0 ? 1 : comprobante.getAttTipoCambio().getDouble()));
        jtfProvSubtotal.setText(SLibUtils.getDecimalFormatAmount().format(comprobante.getAttSubTotal().getDouble()));
        jtfDiscountDoc.setText(SLibUtils.getDecimalFormatAmount().format(comprobante.getAttDescuento().getDouble()));
        jtfSubtotal.setText(SLibUtils.getDecimalFormatAmount().format(comprobante.getAttSubTotal().getDouble() - comprobante.getAttDescuento().getDouble()));
        jtfTaxCharged.setText(SLibUtils.getDecimalFormatAmount().format(comprobante.getEltOpcImpuestos() == null ? 0 : comprobante.getEltOpcImpuestos().getAttTotalImpuestosTraslados().getDouble()));
        jtfTaxRetained.setText(SLibUtils.getDecimalFormatAmount().format(comprobante.getEltOpcImpuestos() == null ? 0 : comprobante.getEltOpcImpuestos().getAttTotalImpuestosRetenidos().getDouble()));
        jtfTotal.setText(SLibUtils.getDecimalFormatAmount().format(comprobante.getAttTotal().getDouble()));
        
        jtfCfdiEmisor.setCaretPosition(0);
        jtfCfdiEmisorFiscalId.setCaretPosition(0); 
        jtfCfdiFolio.setCaretPosition(0);
        jtfCfdiPaymentMethod.setCaretPosition(0);
        jtfCfdiDate.setCaretPosition(0);
        
        jtfCurrency.setCaretPosition(0);
        jtfExchangeRate.setCaretPosition(0);
        jtfProvSubtotal.setCaretPosition(0);
        jtfDiscountDoc.setCaretPosition(0);
        jtfSubtotal.setCaretPosition(0);
        jtfTaxCharged.setCaretPosition(0);
        jtfTaxRetained.setCaretPosition(0);
        jtfTotal.setCaretPosition(0);
        
        int bizPartnerIdEmisor = SBpsUtils.getBizParterIdByFiscalId(miClient.getSession().getStatement(), comprobante.getEltEmisor().getAttRfc().getString(), "", SDataConstantsSys.BPSS_CT_BP_SUP);
        moBizPartnerEmisor = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { bizPartnerIdEmisor }, SLibConstants.EXEC_MODE_SILENT);
        
        int emisorTaxRegionId = moBizPartnerEmisor.getDbmsBizPartnerBranches().get(0).getFkTaxRegionId_n();
        SFormUtilities.locateComboBoxItem(jcbTaxRegion, new int[] { emisorTaxRegionId != 0 ? emisorTaxRegionId : miClient.getSessionXXX().getParamsCompany().getFkDefaultTaxRegionId_n() });          
        
        try {
            mnCfdiCurrencyId = SImportUtils.getCurrencyId(moComprobante.getAttMoneda().getString());
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
        
        if (isWithDpsToLink()) {
            // there is DPS to link
            
            SDataBizPartnerBranch branch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { moDpsToLink.getFkCompanyBranchId()}, SLibConstants.EXEC_MODE_SILENT);
            moBizPartnerReceptor = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { branch.getFkBizPartnerId() }, SLibConstants.EXEC_MODE_SILENT);
            
            jtfDpsToLinkFolio.setText(moDpsToLink.getDpsNumber());
            jtfDpsToLinkDate.setText(SLibUtils.DbmsDateFormatDate.format(moDpsToLink.getDate()));
            jbViewDpsToLink.setEnabled(true);

            jtfDpsToLinkFolio.setCaretPosition(0);
            jtfDpsToLinkDate.setCaretPosition(0);
            
            moFieldDpsNature.setKey(new int[] { moDpsToLink.getFkDpsNatureId() });
            moFieldFunctionalSubArea.setKey(new int[] { moDpsToLink.getFkFunctionalSubAreaId() });
        }
        else {
            // there is not DPS to link:
            
            int bizPartnerIdReceptor = SBpsUtils.getBizParterIdByFiscalId(miClient.getSession().getStatement(), comprobante.getEltReceptor().getAttRfc().getString(), "", SDataConstantsSys.BPSS_CT_BP_CO); 
            
            moBizPartnerReceptor = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { bizPartnerIdReceptor }, SLibConstants.EXEC_MODE_SILENT);
            
            jtfDpsToLinkFolio.setText("");
            jtfDpsToLinkDate.setText("");
            jbViewDpsToLink.setEnabled(false);
            
            moFieldDpsNature.setKey(new int[] { SDataConstantsSys.TRNU_DPS_NAT_DEF });
            
            if (!miClient.getSessionXXX().getParamsCompany().getIsFunctionalAreas()) {
                moFieldFunctionalSubArea.setKey(new int[] { SModSysConsts.CFGU_FUNC_SUB_NA });
            }
            else {
                if (jcbFunctionalSubArea.getItemCount() == 2) {
                    jcbFunctionalSubArea.setSelectedIndex(1);
                }
            }
        }
        
        populateCfdiTables();
    }
    
    private SDataDps createDps() {
        ArrayList<SDataDpsEntry> dpsEntries = new ArrayList<>();
        
        // retrieval of new DPS entries and final grooming:
        
        for (int i = 0; i < moCfdiConceptsPane.getTableGuiRowCount(); i++) {
            SRowCfdiImport40 row = (SRowCfdiImport40) moCfdiConceptsPane.getTableRow(i);
            
            if (isWithDpsToLink()) {
                if (!row.getItem().getIsPrepayment()) {
                    // update concept name from DPS to link:
                    if (row.getNewDpsEntries().size() == row.getImportedDpsEntries().size()) {
                        for (int j = 0; j < row.getNewDpsEntries().size(); j++) {
                            row.getNewDpsEntries().get(j).setConcept(moDpsToLink.getDbmsDpsEntry(row.getImportedEntryDpsDpsLinks().get(j).getDpsEntryKey()).getConcept());
                        }
                    }
                }
            }
            
            dpsEntries.addAll(row.getNewDpsEntries());
            saveMatchingItemConcept(row); // preserve preferences for current busines partner!
        }
        
        // prepare DPS:
        
        int dpsNatureId = isWithDpsToLink() ? moDpsToLink.getFkDpsNatureId() : moFieldDpsNature.getKeyAsIntArray()[0];
        
        int funcAreaId = 0;
        int funcSubAreaId = 0;
        
        if (!miClient.getSessionXXX().getParamsCompany().getIsFunctionalAreas() || jcbFunctionalSubArea.getSelectedIndex() <= 0) {
            funcAreaId = SModSysConsts.CFGU_FUNC_NA;
            funcSubAreaId = SModSysConsts.CFGU_FUNC_SUB_NA;
        }
        else {
            SFormComponentItem item = (SFormComponentItem) jcbFunctionalSubArea.getSelectedItem();
            funcAreaId = ((int[]) item.getForeignKey())[0]; // yes!, index 0 of the FOREIGN key!
            funcSubAreaId = ((int[]) item.getPrimaryKey())[0]; // yes!, index 0 of the PRIMARY key!
        }
        
        SDataDps dps = null;
        
        try {
            int[] dpsTypeKey = isInvoice() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV : SDataConstantsSys.TRNU_TP_DPS_PUR_CN;
            dps = SImportUtils.createDps(miClient, dpsTypeKey, moComprobante, moCfdiXmlFile, moCfdiPdfFile, moBizPartnerReceptor, moBizPartnerEmisor, dpsEntries, moDpsToLink, dpsNatureId, funcAreaId, funcSubAreaId);
            
            if (mbIsCreditNoteToApplyPrepayments) {
                for (SDataDpsEntry dpsEntry : dpsEntries) {
                    dpsEntry.setIsDiscountRetailChain(true); // needed as well when applying advances!
                    dpsEntry.setFlagDpsEtyOpened(true); // simulate that entry was already opened in SFormDpsEntry!
                    
                    dpsEntry.setAuxPkDpsYearId(moDpsToLink.getPkYearId());
                    dpsEntry.setAuxPkDpsDocId(moDpsToLink.getPkDocId());

                    // Create affected DPS's virtual entry:

                    SDataDpsEntry dpsEntryComplementary = new SDataDpsEntry();
                    dpsEntryComplementary.setPkYearId(moDpsToLink.getPkYearId());
                    dpsEntryComplementary.setPkDocId(moDpsToLink.getPkDocId());
                    ///oDpsEntryComplementary.setPkEntryId(...);

                    STrnDpsUtilities.prepareDpsEntryComplementaryAndCreateAdjustment(miClient, dps, dpsEntry, dpsEntryComplementary);
                }
            }
            else if (!isInvoice()) {
                for (SDataDpsEntry dpsEntry : dpsEntries) {
                    dpsEntry.setAuxPkDpsYearId(moDpsToLink.getPkYearId());
                    dpsEntry.setAuxPkDpsDocId(moDpsToLink.getPkDocId());
                }
            }
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
        
        return dps;
    }

    /**
     * Devuelve el nuevo DPS renderizado.
     * @return 
     */
    public SDataDps getNewDps() {
        return moNewDps;
    }
    
    /*
     * Event methods
     */
    
    private SFormValidation validateForm() {
        SFormValidation validation = new SFormValidation();
        
        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(mvFields.get(i).getComponent());
                break;
            }
        }
        
        if (!validation.getIsError()) {
            ArrayList<LinkedQuantity> linkedQuantities = new ArrayList<>();
            
            if (isWithDpsToLink()) {
                if (jcbDpsNature.getSelectedIndex() <= 0) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlDpsNature) + "'.");
                    validation.setComponent(jcbDpsNature); // useless but for consistence: component is disabled!
                }
                else if (jcbFunctionalSubArea.getSelectedIndex() <= 0) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlFunctionalSubArea) + "'.");
                    validation.setComponent(jcbFunctionalSubArea); // useless but for consistence: component is disabled!
                }
                
                for (int i = 0; i < moCfdiConceptsPane.getTableGuiRowCount(); i++) {
                    SRowCfdiImport40 row = (SRowCfdiImport40) moCfdiConceptsPane.getTableRow(i);
                    
                    if (!row.getImportedEntryDpsDpsLinks().isEmpty()) {
                        for (int j = 0; j < row.getImportedEntryDpsDpsLinks().size(); j++) {
                            int[] dpsKey = row.getImportedEntryDpsDpsLinks().get(j).getDpsEntryKey();
                            boolean found = false;
                            
                            for (LinkedQuantity linkedQuantity : linkedQuantities) {
                                if (SLibUtils.compareKeys(linkedQuantity.dpsKey, dpsKey)) {
                                    found = true;
                                    linkedQuantity.quantity += row.getImportedEntryDpsDpsLinks().get(j).getQuantityToLink();
                                }
                            }
                            
                            if (!found) {
                                LinkedQuantity linkedQ = new LinkedQuantity(dpsKey, row.getImportedEntryDpsDpsLinks().get(j).getQuantityToLink());
                                linkedQuantities.add(linkedQ);
                            }
                        }
                    }
                }
            }
            
            int rowIndex;

            ROWS:
            for (rowIndex = 0; rowIndex < moCfdiConceptsPane.getTableGuiRowCount(); rowIndex++) {
                SRowCfdiImport40 row = (SRowCfdiImport40) moCfdiConceptsPane.getTableRow(rowIndex); // variable de conveniencia
                DElementConcepto concepto = row.getConcepto(); // variable de conveniencia
                String descripcion = (concepto.getAttNoIdentificacion().getString().isEmpty() ? "" : concepto.getAttNoIdentificacion().getString() + " - ") + concepto.getAttDescripcion().getString();
                String msgPrefix = "El concepto del CFDI #" + (rowIndex + 1) + ", \"" + descripcion + "\", ";

                if (isWithDpsToLink()) {
                    if (row.getImportedEntryDpsDpsLinks().isEmpty()) {
                        validation.setMessage(msgPrefix + "no tiene asignada una partida de la " + msDocumentName + ".");
                        validation.setComponent(jbAsignDpsToLinkEntries);
                        break;
                    }
                    else {
                        for (SDataEntryDpsDpsLink entryDpsDpsLink : row.getImportedEntryDpsDpsLinks()) {
                            try {
                                double totalsupplied = STrnDpsUtilities.obtainEntryTotalQuantitySupplied(miClient, (int[]) entryDpsDpsLink.getDpsEntryKey());
                                
                                for (LinkedQuantity linkedQuantity : linkedQuantities) {
                                    if (SLibUtils.compareKeys(linkedQuantity.dpsKey, entryDpsDpsLink.getDpsEntryKey())) {
                                        if (totalsupplied > linkedQuantity.quantity) {
                                            String message = "Para el ítem '" + entryDpsDpsLink.getConcept() + " (" + entryDpsDpsLink.getConceptKey() + ")' en la partida # " + entryDpsDpsLink.getSortingPosition() + "\n" +
                                                    "la cantidad minima a vincular debe ser " + (totalsupplied < linkedQuantity.quantity ? "mayor o " : "") + "igual a " + 
                                                    SLibUtils.getDecimalFormatQuantity().format(totalsupplied) + " ya que tiene surtidos previos.\n" + 
                                                    "¿Está seguro que desea hacer caso omiso y continuar?";
                                            if (miClient.showMsgBoxConfirm(message) != JOptionPane.YES_OPTION) {
                                                validation.setMessage("La cantidad a vincular debería ser al menos " + SLibUtils.getDecimalFormatQuantity().format(linkedQuantity.quantity) + ".");
                                            }
                                            break ROWS;
                                        }
                                        break;
                                    }
                                }
                            }
                            catch (Exception e) {
                                SLibUtils.showException(this, e);
                            }
                        }
                    }

                    if (!validation.getIsError() && !row.isLinkedAsService()) {
                        double cantConcept = row.getConcepto().getAttCantidad().getDouble();
                        double convFact = row.getConvFactor();
                        double toLink = 0;
                        toLink = SLibUtils.round(row.getImportedEntryDpsDpsLinks().stream().map((entryDpsDpsLink) -> entryDpsDpsLink.getQuantityToLink()).reduce(toLink, (accumulator, _item) -> accumulator + _item), 4);
                        double totConcept = SLibUtils.round(cantConcept * convFact, 4);
                        
                        if (totConcept < toLink) {
                            validation.setMessage(msgPrefix + "tiene vinculada una cantidad mayor (" + toLink + ") a la cantidad del concepto (" + totConcept + ").");
                        }
                        else if (totConcept > toLink) {
                            validation.setMessage(msgPrefix + "tiene vinculada una cantidad menor (" + toLink + ") a la cantidad del concepto (" + totConcept + ").");
                        }
                    }
                }
                
                if (!validation.getIsError()) {
                    if (row.getItem() == null) {
                        validation.setMessage(msgPrefix + "no tiene asignado un ítem.");
                        validation.setComponent(jbSelectItem);
                        break;
                    }
                    else {
                        // las partidas de facturas de servicios de facturación deben tener un ítem para facturar anticipos:
                        if (isInvoice() && row.isInvoicedAdvance() && !row.getItem().getIsPrepayment()) {
                            validation.setMessage(msgPrefix + "tiene la Clave de ProdServ SAT '" + DCfdi40Catalogs.ClaveProdServServsFacturacion + "',\n"
                                    + "pero su ítem '" + row.getItem().getItem() + "' no está configurado como 'anticipo para facturar'.");
                        }
                        // las partidas de facturas de servicios de facturación deben tener el tipo de operación "facturación´de anticipos":
                        else if (isInvoice() && row.isInvoicedAdvance() && row.getOperationsType() != SDataConstantsSys.TRNX_OPS_TYPE_OPS_PREPAY_INVOICED) {
                            validation.setMessage(msgPrefix + "tiene la Clave de ProdServ SAT '" + DCfdi40Catalogs.ClaveProdServServsFacturacion + "',\n"
                                    + "pero su tipo de operación no es '" + SDataConstantsSys.OperationsTypesOpsMap.get(SDataConstantsSys.TRNX_OPS_TYPE_OPS_PREPAY_INVOICED) + "'.");
                        }
                        // las partidas de notas de crédito de aplicación de anticipos facturados deben tener un ítem para facturar anticipos:
                        else if (!isInvoice() && mbIsCreditNoteToApplyPrepayments && !row.getItem().getIsPrepayment()) {
                            validation.setMessage(msgPrefix + "tiene la Clave de ProdServ SAT '" + DCfdi40Catalogs.ClaveProdServServsFacturacion + "',\n"
                                    + "y el tipo de relación de los CFDI relacionados del CFDI es '" + DCfdi40Catalogs.TipoRelación.get(DCfdi40Catalogs.ClaveTipoRelaciónAplicaciónAnticipo) + "',\n"
                                    + "pero su ítem '" + row.getItem().getItem() + "' no está configurado como 'anticipo para facturar'.");
                        }
                        // las partidas de notas de crédito de aplicación de anticipos facturados deben tener el tipo de operación "aplicación de anticipos facturados":
                        else if (!isInvoice() && mbIsCreditNoteToApplyPrepayments && row.getOperationsType() != SDataConstantsSys.TRNX_OPS_TYPE_ADJ_APP_PREPAY) {
                            validation.setMessage(msgPrefix + "tiene la Clave de ProdServ SAT '" + DCfdi40Catalogs.ClaveProdServServsFacturacion + "',\n"
                                    + "y el tipo de relación de los CFDI relacionados del CFDI es '" + DCfdi40Catalogs.TipoRelación.get(DCfdi40Catalogs.ClaveTipoRelaciónAplicaciónAnticipo) + "',\n"
                                    + "pero su tipo de operación no es '" + SDataConstantsSys.OperationsTypesAdjMap.get(SDataConstantsSys.TRNX_OPS_TYPE_ADJ_APP_PREPAY) + "'.");
                        }
                        // confirmar si es correcto que las partidas de notas de crédito de servicios de facturación tengan un ítem que no sea para facturar anticipos (puede tratarse de un simple descuento):
                        else if (!isInvoice() && !mbIsCreditNoteToApplyPrepayments && row.isInvoicedAdvance() && !row.getItem().getIsPrepayment() &&
                                miClient.showMsgBoxConfirm(msgPrefix + "tiene la Clave de ProdServ SAT '" + DCfdi40Catalogs.ClaveProdServServsFacturacion + "',\n"
                                        + "pero su ítem '" + row.getItem().getItem() + "' no está configurado como 'anticipo para facturar'.\n" + SGuiConsts.MSG_CNF_CONT_OMIT_VAL) != JOptionPane.YES_OPTION) {
                            validation.setMessage(msgPrefix + "tiene la Clave de ProdServ SAT '" + DCfdi40Catalogs.ClaveProdServServsFacturacion + "',\n"
                                    + "pero su ítem '" + row.getItem().getItem() + "' no está configurado como 'anticipo para facturar'.\n"
                                    + "Se debe elegir un ítem diferente.");
                        }
                        // confirmar si es correcto que las partidas de notas de crédito de servicios de facturación tengan un tipo de operación que no sea "ajuste de facturación de anticipos" (puede tratarse de un simple descuento):
                        else if (!isInvoice() && !mbIsCreditNoteToApplyPrepayments && row.isInvoicedAdvance() && row.getOperationsType() != SDataConstantsSys.TRNX_OPS_TYPE_ADJ_PREPAY_INVOICED &&
                                miClient.showMsgBoxConfirm(msgPrefix + "tiene la Clave de ProdServ SAT '" + DCfdi40Catalogs.ClaveProdServServsFacturacion + "',\n"
                                    + "pero su tipo de operación no es '" + SDataConstantsSys.OperationsTypesAdjMap.get(SDataConstantsSys.TRNX_OPS_TYPE_ADJ_PREPAY_INVOICED) + "'.\n" + SGuiConsts.MSG_CNF_CONT_OMIT_VAL) != JOptionPane.YES_OPTION) {
                            validation.setMessage(msgPrefix + "tiene la Clave de ProdServ SAT '" + DCfdi40Catalogs.ClaveProdServServsFacturacion + "',\n"
                                    + "pero su tipo de operación no es '" + SDataConstantsSys.OperationsTypesAdjMap.get(SDataConstantsSys.TRNX_OPS_TYPE_ADJ_PREPAY_INVOICED) + "'.\n"
                                    + "Se debe elegir un tipo de operación diferente.");
                        }
                        // las partidas con ítems que requieren ítem de referencia deben tenerlo:
                        else if (row.getItem().getDbmsDataItemGeneric().getIsItemReferenceRequired() && row.getItemReference() == null) {
                            validation.setMessage(msgPrefix + "no tiene asignado un ítem de referencia.\n"
                                    + "Se debe elegir un ítem de referencia.");
                            validation.setComponent(jbSelectItemReference);
                            break;
                        }
                        // las partidas con ítems que requieren ítem de referencia no pueden tener el mismo ítem como ítem de referencia:
                        else if (row.getItem().getDbmsDataItemGeneric().getIsItemReferenceRequired() && row.getItemReference().getPkItemId() == row.getItem().getPkItemId()) {
                            validation.setMessage(msgPrefix + "tiene asignado el mismo ítem de referencia que el ítem principal.\n"
                                    + "Se debe elegir un ítem de referencia diferente.");
                            validation.setComponent(jbSelectItemReference);
                            break;
                        }
                        else if (row.getUnit() == null) {
                            validation.setMessage(msgPrefix + "no tiene asignada una unidad.");
                            validation.setComponent(jbSelectUnit);
                            break;
                        }
                        else if (row.getUnit().getDbmsClaveUnidad().isEmpty()) {
                            validation.setMessage(msgPrefix + "su unidad asignada carece de ClaveUnidad SAT.");
                            validation.setComponent(jbSelectUnit);
                            break;
                        }
                        else if (row.getTaxRegion() == null) {
                            validation.setMessage(msgPrefix + "no tiene asignada una región de impuestos.");
                            validation.setComponent(jbSelectTaxRegion);
                            break;
                        }
                        else if (row.getOperationsType() == 0) {
                            validation.setMessage(msgPrefix + "no tiene asignado un tipo de operación.");
                            validation.setComponent(jbSelectOperationsType);
                            break;
                        }
                        else if (row.getCostCenter() == null) {
                            validation.setMessage(msgPrefix + "no tiene asignado un centro de costo.");
                            validation.setComponent(jbSelectCostCenter);
                            break;
                        }
                        else if (row.getConvFactor() == 0.0) {
                            validation.setMessage(msgPrefix + "no tiene especificado un factor de conversión.");
                            validation.setComponent(moCfdiConceptsPane.getTable());
                            break;
                        }
                        else {
                            if (!validation.getIsError() && !row.getImportedDpsEntries().isEmpty()) {
                                ArrayList<SDataDpsEntry> newDpsEntries = row.getNewDpsEntries(); // variable de conveniencia
                                ArrayList<SDataDpsEntry> importedDpsEntries = row.getImportedDpsEntries(); // variable de conveniencia

                                if (newDpsEntries.size() == importedDpsEntries.size()) {
                                    for (int j = 0; j < newDpsEntries.size(); j++) {
                                        if (newDpsEntries.get(j).getSubtotalCy_r() > importedDpsEntries.get(j).getSubtotalCy_r()) {
                                            validation.setMessage(msgPrefix + "tiene un importe mayor ($" + SLibUtils.getDecimalFormatAmount().format(newDpsEntries.get(j).getSubtotalCy_r()) + ") "
                                                    + "que el de la partida de la " + msDocumentName + " elegida ($" + SLibUtils.getDecimalFormatAmount().format(importedDpsEntries.get(j).getSubtotalCy_r()) + ").");
                                            break;
                                        }
                                    }
                                }
                            }

                            if (!validation.getIsError()) {
                                if (concepto.getEltOpcConceptoImpuestos() != null) {
                                    if (concepto.getEltOpcConceptoImpuestos().getEltOpcImpuestosTrasladados() != null) {
                                        ArrayList<cfd.ver40.DElementConceptoImpuestoTraslado> traslados = concepto.getEltOpcConceptoImpuestos().getEltOpcImpuestosTrasladados().getEltImpuestoTrasladados();
                                        TAXES:
                                        for (DElementConceptoImpuestoTraslado traslado : traslados) {
                                            if (!row.getTaxChargedMatched().contains(traslado)) {
                                                validation.setMessage(msgPrefix + "no tiene empatado el impuesto:\n" 
                                                        + "Impuesto: " + DCfdi40Catalogs.Impuesto.get(traslado.getAttImpuesto().getString()) + ".\n"
                                                        + "Tipo: trasladado. \n"
                                                        + "Factor: " + traslado.getAttTipoFactor().getString() + " de "
                                                        + SLibUtils.DecimalFormatPercentage2D.format(traslado.getAttTasaOCuota().getDouble()) + ".");
                                                break ROWS;
                                            }
                                        }
                                    }

                                    if (concepto.getEltOpcConceptoImpuestos().getEltOpcImpuestosRetenciones() != null) {
                                        ArrayList<cfd.ver40.DElementConceptoImpuestoRetencion> retenciones = concepto.getEltOpcConceptoImpuestos().getEltOpcImpuestosRetenciones().getEltImpuestoRetenciones();
                                        TAXES:
                                        for (DElementConceptoImpuestoRetencion retencion : retenciones) {
                                            if (!row.getTaxRetainedMatched().contains(retencion)) { 
                                                validation.setMessage(msgPrefix + "no tiene empatado el impuesto:\n"
                                                        + "Impuesto: " + DCfdi40Catalogs.Impuesto.get(retencion.getAttImpuesto().getString()) + ".\n"
                                                        + "Tipo: retenido. \n"
                                                        + "Factor: " +retencion.getAttTipoFactor().getString() + " de "
                                                        + SLibUtils.DecimalFormatPercentage2D.format(retencion.getAttTasaOCuota().getDouble()) + ".");
                                                break ROWS;
                                            }
                                        }
                                    }
                                }
                            }
                            
                            if (!validation.getIsError() && !isInvoice()) {
                                // importing a credit note
                                
                                if (row.getAdjustmentSubtypeKey() == null) {
                                    validation.setMessage(msgPrefix + "carece de tipo de ajuste.");
                                    validation.setComponent(jbSelectAdjustmentSubtype);
                                }
                                else if (mbIsCreditNoteToApplyPrepayments && !SLibUtils.compareKeys(row.getAdjustmentSubtypeKey(), SDataConstantsSys.TRNS_STP_DPS_ADJ_DISC_DISC)) {
                                    String name = SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_STP_DPS_ADJ, SDataConstantsSys.TRNS_STP_DPS_ADJ_DISC_DISC);
                                    validation.setMessage(msgPrefix + "tiene un tipo de ajuste inadecuado.\n"
                                            + "Para la aplicación de anticipos facturados, el tipo de ajuste debe ser '" + name + "'.");
                                    validation.setComponent(jbSelectAdjustmentSubtype);
                                }
                            }
                        }
                    }
                }
            } // ROWS scope

            if (validation.getIsError()) {
                validation.setComplement(rowIndex);
            }
            else {
                for (int i = 0; i < moCfdiConceptsPane.getTableGuiRowCount(); i++) {
                    rowIndex = i;
                    
                    SRowCfdiImport40 row = (SRowCfdiImport40) moCfdiConceptsPane.getTableRow(i); // variable de conveniencia
                    DElementConcepto concepto = row.getConcepto(); // variable de conveniencia
                    String descripcion = (concepto.getAttNoIdentificacion().getString().isEmpty() ? "" : concepto.getAttNoIdentificacion().getString() + " - ") + concepto.getAttDescripcion().getString();
                    String msgPrefix = "El factor de conversión del concepto del CFDI #" + (i + 1) + ", \"" + descripcion + "\", ";

                    if (isWithDpsToLink() && row.getConvFactor() != 1) {
                        if (miClient.showMsgBoxConfirm(msgPrefix + "es diferente de 1.0,\n"
                                + "pero está asignado a una partida de la " + msDocumentName + ".\n"
                                + "¿Esta seguro de que el factor de conversión es correcto?") != JOptionPane.YES_OPTION) {
                            validation.setMessage("Cambiar el factor de conversión del concepto del CFDI #" + (i + 1) + " para que sea igual a 1.0.");
                            break;
                        }
                    }

                    if (!validation.getIsError()) {
                        if (concepto.getAttClaveUnidad().getString().equals(row.getUnit().getDbmsClaveUnidad())) {
                            if (row.getConvFactor() != 1) {
                                if (miClient.showMsgBoxConfirm(msgPrefix + "es diferente de 1.0,\n"
                                        + "pero las Claves de Unidad SAT del concepto y del ítem seleccionado son iguales.\n"
                                        + "¿Esta seguro de que el factor de conversión es correcto?") != JOptionPane.YES_OPTION) {
                                    validation.setMessage("Cambiar el factor de conversión del concepto del CFDI #" + (i + 1) + " para que sea igual a 1.0.");
                                    break;
                                }
                            }
                        }
                        else {
                            if (row.getConvFactor() == 1) {
                                if (miClient.showMsgBoxConfirm(msgPrefix + "es igual a 1.0,\n"
                                        + "pero las Claves de Unidad SAT del concepto y del ítem seleccionado son diferentes.\n"
                                        + "¿Esta seguro de que el factor de conversión es correcto?") != JOptionPane.YES_OPTION) {
                                    validation.setMessage("Cambiar el factor de conversión del concepto del CFDI #" + (i + 1) + " para que sea diferente de 1.0.");
                                    break;
                                }
                            }
                        }
                    }
                }

                if (validation.getIsError()) {
                    validation.setComplement(rowIndex);
                }
            }
        }
        
        return validation;
    }
    
    private void actionOk() {
        SFormValidation validation = validateForm();
                
        if (!validation.getIsError()) {
            moNewDps = createDps();
            
            if (moNewDps != null) { // when null, do nothing!
                setVisible(false);
            }
        }
        else { 
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocus();
            }
            
            if (validation.getComplement() != null && validation.getComplement() instanceof Integer) {
                moCfdiConceptsPane.setTableRowSelection((int) validation.getComplement()); // validations's complement is the row being validated
            }
            
            if (!validation.getMessage().isEmpty()) {
                miClient.showMsgBoxWarning(validation.getMessage());
            }
        }
    }

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    private void actionCopyRow() {
        if (jbCopyRowSettings.isEnabled()) {
            int selectedRow = moCfdiConceptsPane.getTable().getSelectedRow();
            
            if (selectedRow == -1) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_ROW_UNDEF); 
            }
            else {
                moRowCfdiCopy = (SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow();
                jbPasteRowSettings.setEnabled(true);
                jtfCopyRowInfo.setText("¡Renglón #" + (selectedRow + 1) + " copiado!");
                jtfCopyRowInfo.setCaretPosition(0);
            }
        }
    }
    
    private void actionPasteRow() {
        if (jbPasteRowSettings.isEnabled() && moRowCfdiCopy != null) {
            int selectedRow = moCfdiConceptsPane.getTable().getSelectedRow();
            
            if (selectedRow == -1) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_ROW_UNDEF); 
            }
            else {
                SRowCfdiImport40 rowCfdiImport = (SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow();
                
                rowCfdiImport.setItem(moRowCfdiCopy.getItem());
                rowCfdiImport.setItemReference(moRowCfdiCopy.getItemReference());
                rowCfdiImport.setUnit(moRowCfdiCopy.getUnit());
                rowCfdiImport.setTaxRegion(moRowCfdiCopy.getTaxRegion()); 
                rowCfdiImport.setCostCenter(moRowCfdiCopy.getCostCenter());
                rowCfdiImport.setConvFactor(moRowCfdiCopy.getConvFactor());
                rowCfdiImport.setOperationsType(moRowCfdiCopy.getOperationsType());
                
                rowCfdiImport.prepareTableRow();
                moCfdiConceptsPane.renderTableRows();
                moCfdiConceptsPane.setTableRowSelection(selectedRow);
                
                refreshRowEntriesAndTotalAndTaxes(rowCfdiImport);
                
                moRowCfdiCopy = null;
                jbPasteRowSettings.setEnabled(false);
                jtfCopyRowInfo.setText(""); 
            }
        }
    }
    
    private void actionViewCfdiPdf() {
        if (isWithInvoicePdf()) {
            if (moDialogPdfViewer == null) {
                moDialogPdfViewer = new SDialogPdfViewer((SGuiClient) miClient, false);
            }

            moDialogPdfViewer.setPdf(moDocumentInfo, moCfdiPdfFile);
            moDialogPdfViewer.setVisible(true);
        }
    }
    
    private void actionPickTaxRegion() {
        miClient.pickOption(SDataConstants.FINU_TAX_REG, moFieldTaxRegion, null);
    }

    private void actionViewDpsToLink() {
        if (isWithDpsToLink()) {
            int[] dpsTypeKey = isInvoice() ? SDataConstantsSys.TRNU_TP_DPS_PUR_ORD : SDataConstantsSys.TRNU_TP_DPS_PUR_INV;
            miClient.getGuiModule(SDataConstants.MOD_PUR).setFormComplement(dpsTypeKey);
            miClient.getGuiModule(SDataConstants.MOD_PUR).showForm(SDataConstants.TRNX_DPS_RO, moDpsToLink.getPrimaryKey());
        }
    }
    
    /**
     * Seleccionar ítem.
     * @param forceSelection Force selection.
     * @param selectionCase Indica el ítem deseado: ITEM_MAIN or ITEM_REF.
     */
    private void actionSelectItem(final boolean forceSelection, final int selectionCase) {
        if (forceSelection || (selectionCase == CASE_ITEM && jbSelectItem.isEnabled() || selectionCase == CASE_ITEM_REF && jbSelectItemReference.isEnabled())) {
            int selectedRow = moCfdiConceptsPane.getTable().getSelectedRow();

            if (selectedRow == -1) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_ROW_UNDEF); 
            }
            else if (jcbTaxRegion.getSelectedIndex() <= 0) {
                jcbTaxRegion.requestFocus();
                miClient.showMsgBoxWarning(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlTaxRegion) + "'.");
            }
            else {
                SRowCfdiImport40 rowCfdiImport = (SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow();
                int itemId = pickAndGetItemId(selectionCase, rowCfdiImport);

                if (itemId != 0) {
                    SDataItem item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { itemId }, SLibConstants.EXEC_MODE_SILENT);

                    if (selectionCase == CASE_ITEM) {
                        rowCfdiImport.setItem(item);

                        // se busca si el ítem principal requiere un ítem de refefencia, de ser así, se asigna:
                        if (rowCfdiImport.getItem().getDbmsDataItemGeneric().getIsItemReferenceRequired()) {
                            SDataItem itemReference = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { rowCfdiImport.getItem().getDbmsFkDefaultItemRefId_n() }, SLibConstants.EXEC_MODE_SILENT);
                            rowCfdiImport.setItemReference(itemReference);
                        }
                        else {
                            rowCfdiImport.setItemReference(null);
                        }

                        // se asigna la unidad del ítem principal:
                        rowCfdiImport.setUnit(rowCfdiImport.getItem().getDbmsDataUnit());

                        // se asigna la región de impuestos seleccionada:
                        SDataTaxRegion taxRegion = (SDataTaxRegion) SDataUtilities.readRegistry(miClient, SDataConstants.FINU_TAX_REG, ((SFormComponentItem) jcbTaxRegion.getSelectedItem()).getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                        rowCfdiImport.setTaxRegion(taxRegion);

                        // se asigna tipo de operación por defecto:
                        if (rowCfdiImport.getOperationsType() == 0) {
                            rowCfdiImport.setOperationsType(getDefaultOperationsType(rowCfdiImport));
                        }

                        // se obtiene y se asigna el centro de costo definido para el ítem principal:
                        rowCfdiImport.setCostCenter(getCostCenterByItem(itemId));
                    }
                    else {
                        // es ítem de referencia:
                        rowCfdiImport.setItemReference(item);
                    }

                    rowCfdiImport.prepareTableRow();
                    moCfdiConceptsPane.renderTableRows();
                    moCfdiConceptsPane.setTableRowSelection(selectedRow);
                }

                refreshRowEntriesAndTotalAndTaxes(rowCfdiImport);
            }
        }
    }

    private void actionSelectUnit() {
        if (jbSelectUnit.isEnabled()) {
            int selectedRow = moCfdiConceptsPane.getTable().getSelectedRow();
            
            if (selectedRow == -1) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_ROW_UNDEF); 
            }
            else {    
                SRowCfdiImport40 rowCfdiImport = (SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow();
                SDataItem item = rowCfdiImport.getItem();
                
                if (moPickerUnit == null) {
                    moPickerUnit = SFormOptionPicker.createOptionPicker(miClient, SDataConstants.ITMU_UNIT, moPickerUnit);
                }
                
                moPickerUnit.formReset();
                moPickerUnit.setFilterKey(new int[] { item.getDbmsDataItemGeneric().getFkUnitTypeId() });
                moPickerUnit.formRefreshOptionPane();
                moPickerUnit.setSelectedPrimaryKey(rowCfdiImport.getUnit() != null ? new int [] { rowCfdiImport.getUnit().getPkUnitId() } : null);
                moPickerUnit.setFormVisible(true); 

                if (moPickerUnit.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    SDataUnit unit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, moPickerUnit.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                    rowCfdiImport.setUnit(unit);
                    
                    rowCfdiImport.prepareTableRow();
                    moCfdiConceptsPane.renderTableRows();
                    moCfdiConceptsPane.setTableRowSelection(selectedRow);
                }
                
                refreshRowEntriesAndTotalAndTaxes(rowCfdiImport);
            }
        }
    }

    private void actionSelectTaxesRegion() {
        if (jbSelectTaxRegion.isEnabled()) {
            int selectedRow = moCfdiConceptsPane.getTable().getSelectedRow();
            
            if (selectedRow == -1) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_ROW_UNDEF); 
            }
            else {
                SRowCfdiImport40 rowCfdiImport = (SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow();
                
                if (moPickerTaxRegion == null) {
                    moPickerTaxRegion = SFormOptionPicker.createOptionPicker(miClient, SDataConstants.FINU_TAX_REG, moPickerTaxRegion);
                }
                
                moPickerTaxRegion.formReset();
                moPickerTaxRegion.formRefreshOptionPane();
                moPickerTaxRegion.setSelectedPrimaryKey(moFieldTaxRegion.getKey());
                moPickerTaxRegion.setFormVisible(true); 

                if (moPickerTaxRegion.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    SDataTaxRegion taxRegion = (SDataTaxRegion) SDataUtilities.readRegistry(miClient, SDataConstants.FINU_TAX_REG, moPickerTaxRegion.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                    rowCfdiImport.setTaxRegion(taxRegion);
                    
                    rowCfdiImport.prepareTableRow();
                    moCfdiConceptsPane.renderTableRows();
                    moCfdiConceptsPane.setTableRowSelection(selectedRow);
                }
                
                refreshRowEntriesAndTotalAndTaxes(rowCfdiImport);
            }
        }
    }
    
    private void actionSelectOperationsType() {
        if (jbSelectOperationsType.isEnabled()) {
            int selectedRow = moCfdiConceptsPane.getTable().getSelectedRow();
            
            if (selectedRow == -1) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_ROW_UNDEF); 
            }
            else {
                SRowCfdiImport40 rowCfdiImport = (SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow();
                
                if (moPickerOpsType == null) {
                    moPickerOpsType = SFormOptionPicker.createOptionPicker(miClient, SDataConstants.TRNX_OPE_TYPE, moPickerOpsType);
                }
                
                moPickerOpsType.formReset();
                moPickerOpsType.setFilterKey(SDataConstantsSys.TRNX_TP_DPS_DOC);
                moPickerOpsType.formRefreshOptionPane();
                moPickerOpsType.setFormVisible(true);
                
                if (moPickerOpsType.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    Object[] keyObject = (Object[]) moPickerOpsType.getSelectedPrimaryKey();
                    Long key = (Long) keyObject[0];
                    
                    rowCfdiImport.setOperationsType(key.intValue());
                    
                    rowCfdiImport.prepareTableRow();
                    moCfdiConceptsPane.renderTableRows();
                    moCfdiConceptsPane.setTableRowSelection(selectedRow);
                }
                
                refreshRowEntriesAndTotalAndTaxes(rowCfdiImport);
            }
        }
    }
    
    private void actionSelectCostCenter() {
        if (jbSelectCostCenter.isEnabled()) {
            int selectedRow = moCfdiConceptsPane.getTable().getSelectedRow();
            
            if (selectedRow == -1) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_ROW_UNDEF); 
            }
            else {
                SRowCfdiImport40 rowCfdiImport = (SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow();
                
                if (moPickerCostCenter == null) {        
                    moPickerCostCenter = SFormOptionPicker.createOptionPicker(miClient, SDataConstants.FIN_CC, moPickerCostCenter);
                }
                
                moPickerCostCenter.formReset();
                moPickerCostCenter.formRefreshOptionPane();
                
                try {
                    moPickerCostCenter.setSelectedPrimaryKey(SDataUtilities.obtainCostCenterItem(miClient.getSession(), rowCfdiImport.getItem().getPkItemId()));
                }
                catch (Exception e) {
                    SLibUtils.printException(this, e);
                }
                
                moPickerCostCenter.setFormVisible(true); 

                if (moPickerCostCenter.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    SDataCostCenter costCenter = (SDataCostCenter) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_CC, moPickerCostCenter.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                    
                    rowCfdiImport.setCostCenter(costCenter);
                    
                    rowCfdiImport.prepareTableRow();
                    moCfdiConceptsPane.renderTableRows();
                    moCfdiConceptsPane.setTableRowSelection(selectedRow);
                }
                
                refreshRowEntriesAndTotalAndTaxes(rowCfdiImport);
            }
        }
    }
    
    private void actionSelectAdjustmentSubtype() {
        if (jbSelectAdjustmentSubtype.isEnabled()) {
            int selectedRow = moCfdiConceptsPane.getTable().getSelectedRow();
            
            if (selectedRow == -1) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_ROW_UNDEF); 
            }
            else {
                SRowCfdiImport40 rowCfdiImport = (SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow();
                
                if (moPickerAdjustmentSubtype == null) {
                    moPickerAdjustmentSubtype = SFormOptionPicker.createOptionPicker(miClient, SDataConstants.TRNS_STP_DPS_ADJ, moPickerAdjustmentSubtype);
                }
                
                moPickerAdjustmentSubtype.formReset();
                moPickerAdjustmentSubtype.formRefreshOptionPane();
                moPickerAdjustmentSubtype.setSelectedPrimaryKey(rowCfdiImport.getAdjustmentSubtypeKey() != null ? rowCfdiImport.getAdjustmentSubtypeKey() : manAdjustmentSubtypeKey);
                moPickerAdjustmentSubtype.setFormVisible(true); 

                if (moPickerAdjustmentSubtype.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    int[] key = (int[]) moPickerAdjustmentSubtype.getSelectedPrimaryKey();
                    String name = SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_STP_DPS_ADJ, key);

                    rowCfdiImport.setAdjustmentSubtypeKey(key);
                    rowCfdiImport.setAdjustmentSubtypeName(name);
                    
                    rowCfdiImport.prepareTableRow();
                    moCfdiConceptsPane.renderTableRows();
                    moCfdiConceptsPane.setTableRowSelection(selectedRow);
                }
                
                refreshRowEntriesAndTotalAndTaxes(rowCfdiImport);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private void actionPickDpsToLinkEntries(int linkType) {
        if (jbAsignDpsToLinkEntries.isEnabled()) {
            int selectedRow = moCfdiConceptsPane.getTable().getSelectedRow();
            
            if (selectedRow == -1) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_ROW_UNDEF); 
            }
            else if (jcbTaxRegion.getSelectedIndex() <= 0) {
                jcbTaxRegion.requestFocus();
                miClient.showMsgBoxWarning(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlTaxRegion) + "'.");
            }
            else {
                SRowCfdiImport40 rowCfdiImport = (SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow();
                
                if (moDialogCfdiConceptsLinker40 == null) {
                    moDialogCfdiConceptsLinker40 = new SDialogCfdiConceptsLinker40(miClient, mnDocumentType);
                }
                
                updateCfdiRowIntoCfdiConceptsLinker(linkType, rowCfdiImport);
                
                if (canCfdiRowBeShownInCfdiConceptsLinker(linkType, rowCfdiImport)) {
                    moDialogCfdiConceptsLinker40.setFormVisible(true);

                    if (moDialogCfdiConceptsLinker40.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                        if (updateCfdiRowFromCfdiConceptsLinker(linkType, rowCfdiImport)) {
                            rowCfdiImport.prepareTableRow();
                            moCfdiConceptsPane.renderTableRows();
                            moCfdiConceptsPane.setTableRowSelection(selectedRow);
                            
                            refreshRowEntriesAndTotalAndTaxes(rowCfdiImport);
                            renderPanelDpsToLinkEntry();
                            validateTaxes(rowCfdiImport);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbViewCfdiPdf) {
                actionViewCfdiPdf();
            }
            else if (button == jbPickTaxRegion) {
                actionPickTaxRegion();
            }
            else if (button == jbViewDpsToLink) {
                actionViewDpsToLink();
            }
            else if (button == jbCopyRowSettings) {
                actionCopyRow();
            }
            else if (button == jbPasteRowSettings) {
                actionPasteRow();
            }
            else if (button == jbSelectItem) {
                actionSelectItem(false, CASE_ITEM);
            }
            else if (button == jbSelectUnit) {
                actionSelectUnit();
            }
            else if (button == jbSelectTaxRegion) {
                actionSelectTaxesRegion();
            }
            else if (button == jbSelectOperationsType) {
                actionSelectOperationsType();
            }
            else if (button == jbSelectCostCenter) {
                actionSelectCostCenter();
            }
            else if (button == jbSelectItemReference) {
                actionSelectItem(false, CASE_ITEM_REF);
            }
            else if (button == jbSelectAdjustmentSubtype) {
                actionSelectAdjustmentSubtype();
            }
            else if (button == jbAsignDpsToLinkEntries) {
                actionPickDpsToLinkEntries(SRowCfdiImport40.LINK_1_ON_1);
            }
            else if (button == jbProcessAsService) {
                actionPickDpsToLinkEntries(SRowCfdiImport40.LINK_AS_SERVICE);
            }
        }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) { 
        if (!e.getValueIsAdjusting()) {
            if (moCfdiConceptsPane.getTable().getSelectedRow() != -1) {
                renderTableCfdiTaxes(moCfdiConceptsPane.getTable().getSelectedRow());
                renderTableDpsTaxes((SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow());
                renderPanelDpsToLinkEntry();
                
                renderLabelIsItemNameEditable((SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow());
                
                enabledButtons((SRowCfdiImport40) moCfdiConceptsPane.getSelectedTableRow());
            }
            else {
                enabledButtons(null);
            }
        }
    }

    @Override
    public void editingStopped(ChangeEvent e) {
        if (moCfdiConceptsPane.getTable().getSelectedColumn() == COL_ITEM_NAME) {
            updateNameItem();
        }
        else if (moCfdiConceptsPane.getTable().getSelectedColumn() == COL_FACT_CONV) {
            updateConversionFactor();
        }
    }

    @Override
    public void editingCanceled(ChangeEvent e) {
        // nothing
    }

    public static class LinkedQuantity {
        int[] dpsKey;
        double quantity;

        public LinkedQuantity(int[] dpsKey, double quantity) {
            this.dpsKey = dpsKey;
            this.quantity = quantity;
        }
    }
}

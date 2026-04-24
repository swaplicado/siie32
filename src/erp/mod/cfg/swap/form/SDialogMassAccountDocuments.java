/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.form;

import cfd.ver40.DCfdi40Catalogs;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mcfg.data.SCfgUtils;
import erp.mfin.data.SFinUtilities;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mod.SModConsts;
import erp.mod.cfg.db.SDbFunctionalSubArea;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.SSwapUtils;
import erp.mod.cfg.swap.model.account.Config;
import erp.mod.cfg.swap.utils.SDataRejectResource;
import erp.mod.cfg.swap.utils.SExportDataAuthActor;
import erp.mod.cfg.swap.utils.SExportDataSomUtils;
import erp.mod.cfg.swap.utils.SImportUtils;
import erp.mod.cfg.swap.utils.SServicesUtils;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.SThinDps;
import erp.mtrn.view.SViewDps;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.grid.SGridRow;
import sa.lib.grid.cell.SGridCellRendererIcon;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiItem;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFieldBoolean;
import sa.lib.gui.bean.SBeanFieldKey;
import sa.lib.gui.bean.SBeanFieldRadio;
import sa.lib.gui.bean.SBeanFormDialog;

/**
 * Importación de documentos desde el Portal de Compras.
 * Ejemplo de la URL de consulta de documentos:
 * "https://transaction-backend-368437194061.us-central1.run.app/api/documents/filter-by-date-and-type/?start_date=2025-08-01&end_date=2025-09-30&document_type=41"
 * Ejemplo de la URL de descarga de documentos:
 * "https://transaction-backend-368437194061.us-central1.run.app/api/documents/download-docs-zip/"
 * 
 * @author Sergio Flores
 */
public class SDialogMassAccountDocuments extends SBeanFormDialog implements ActionListener, ListSelectionListener, ItemListener {
    
    private static final String ND = "(N/D)";
    
    public static final int VALUE_SETTINGS = 1;
    public static final int VALUE_DOCUMENTS_AND_ADVANCES = 2;
    public static final int VALUE_EXPORT_PAYMENTS = 11;
    public static final int VALUE_REJECTED_INVOICES = 12;
    public static final int VALUE_ADVANCES = 21;
    
    protected String msCompanyName;
    protected SGridPaneForm moDocumentsGrid;
    protected SGridPaneForm moConceptsGrid;
    
    protected Config moConfig;
    protected SDialogImportDocuments.Settings moSettings;
    protected ArrayList<SMassAccountDocument> maDocuments;
    protected ArrayList<SMassAccountDocument> maDocumentsRejected;
    protected HashMap<Integer, SFinUtilities.Balance[]> moAdvancesMap; // key: business partner ID; value: balances
    protected Date mtNewRequiredDate;
    protected Pattern moPatternScaleTicketBol;
    protected Pattern moPatternScaleTicketRef;
    protected Pattern moPatternWarehouse;
    protected JLabel jlStatus;
    protected boolean mbAllowLinkGreaterInvoices;
    
    protected boolean mbDocumentsBeingReloaded;
    protected boolean mbDocumentsBeingFiltered;
    protected boolean mbDocumentsBeingRendered;
    protected boolean mbDocumentsBeingRefreshed;
    protected boolean mbDocumentsBeingProcessed;
    protected boolean mbExportPaymentRequests;
    protected int mnDocsRecordedAndLinked;
    protected ImageIcon moIconEdit;
    protected ImageIcon moIconSave;
    protected SDbDatabase moSomDatabase;
    protected SDialogPdfViewer moDialogPdfViewer;
    
    /**
     * Creates new form SDialogMassAccountDocuments
     * @param client GUI client.
     */
    public SDialogMassAccountDocuments(SGuiClient client) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.CFGX_SWAP_MASS_ACC, 0, "Contabilización de facturas autorizadas");
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgSearchBy = new javax.swing.ButtonGroup();
        bgDocMode = new javax.swing.ButtonGroup();
        bgFilter = new javax.swing.ButtonGroup();
        jpSettings = new javax.swing.JPanel();
        jpSettingsW = new javax.swing.JPanel();
        jpSettingsW1 = new javax.swing.JPanel();
        jlUser = new javax.swing.JLabel();
        jtfUserName = new javax.swing.JTextField();
        jtfUserFuncSubAreas = new javax.swing.JTextField();
        jpSettingsW2 = new javax.swing.JPanel();
        moRadSearchByPeriod = new sa.lib.gui.bean.SBeanFieldRadio();
        moDatePeriodStart = new sa.lib.gui.bean.SBeanFieldDate();
        jLabelPeriiod1 = new javax.swing.JLabel();
        moDatePeriodEnd = new sa.lib.gui.bean.SBeanFieldDate();
        moRadDocModeType = new sa.lib.gui.bean.SBeanFieldRadio();
        moKeyDocModeType = new sa.lib.gui.bean.SBeanFieldKey();
        jpSettingsW3 = new javax.swing.JPanel();
        moRadSearchByWeek = new sa.lib.gui.bean.SBeanFieldRadio();
        moCalWeekYear = new sa.lib.gui.bean.SBeanFieldCalendarYear();
        jlLabelWeek1 = new javax.swing.JLabel();
        moCalWeekStart = new sa.lib.gui.bean.SBeanFieldCalendarWeek();
        jlLabelWeek2 = new javax.swing.JLabel();
        moCalWeekEnd = new sa.lib.gui.bean.SBeanFieldCalendarWeek();
        moRadDocModeCase = new sa.lib.gui.bean.SBeanFieldRadio();
        moKeyDocModeCase = new sa.lib.gui.bean.SBeanFieldKey();
        jpSettingsE = new javax.swing.JPanel();
        jpSettingsE1 = new javax.swing.JPanel();
        moRadFilterPartner = new sa.lib.gui.bean.SBeanFieldRadio();
        moKeyFilterPartner = new sa.lib.gui.bean.SBeanFieldKey();
        jpSettingsE2 = new javax.swing.JPanel();
        moRadFilterItem = new sa.lib.gui.bean.SBeanFieldRadio();
        moKeyFilterItem = new sa.lib.gui.bean.SBeanFieldKey();
        jpSettingsE3 = new javax.swing.JPanel();
        moRadFilterAll = new sa.lib.gui.bean.SBeanFieldRadio();
        jbSelectAllDocs = new javax.swing.JButton();
        jlFilter1 = new javax.swing.JLabel();
        jbDeselectAllDocs = new javax.swing.JButton();
        jpDocuments = new javax.swing.JPanel();
        jpDocumentsRendering = new javax.swing.JPanel();
        jpVouchers = new javax.swing.JPanel();
        jpDocumentsPanel = new javax.swing.JPanel();
        jpVouchersDetail = new javax.swing.JPanel();
        jpConceptsPanel = new javax.swing.JPanel();
        jpBol = new javax.swing.JPanel();
        jpBol2 = new javax.swing.JPanel();
        jlBolSrc = new javax.swing.JLabel();
        jtfBolSrcAddress = new javax.swing.JTextField();
        jlBolSrcDistrict = new javax.swing.JLabel();
        jtfBolSrcDistrict = new javax.swing.JTextField();
        jlBolSrcZipCode = new javax.swing.JLabel();
        jtfBolSrcZipCode = new javax.swing.JTextField();
        jlBolSrcLocality = new javax.swing.JLabel();
        jtfBolSrcLocality = new javax.swing.JTextField();
        jlBolSrcCounty = new javax.swing.JLabel();
        jtfBolSrcCounty = new javax.swing.JTextField();
        jlBolSrcState = new javax.swing.JLabel();
        jtfBolSrcState = new javax.swing.JTextField();
        jlBolSrcCountry = new javax.swing.JLabel();
        jtfBolSrcCountry = new javax.swing.JTextField();
        jpBol3 = new javax.swing.JPanel();
        jlBolDes = new javax.swing.JLabel();
        jtfBolDesAddress = new javax.swing.JTextField();
        jlBolDesDistrict = new javax.swing.JLabel();
        jtfBolDesDistrict = new javax.swing.JTextField();
        jlBolDesZipCode = new javax.swing.JLabel();
        jtfBolDesZipCode = new javax.swing.JTextField();
        jlBolDesLocality = new javax.swing.JLabel();
        jtfBolDesLocality = new javax.swing.JTextField();
        jlBolDesCounty = new javax.swing.JLabel();
        jtfBolDesCounty = new javax.swing.JTextField();
        jlBolDesState = new javax.swing.JLabel();
        jtfBolDesState = new javax.swing.JTextField();
        jlBolDesCountry = new javax.swing.JLabel();
        jtfBolDesCountry = new javax.swing.JTextField();
        jpBol4 = new javax.swing.JPanel();
        jlBolScaleTicket = new javax.swing.JLabel();
        jbBolViewScaleTicket = new javax.swing.JButton();
        jtfBolScaleTicket = new javax.swing.JTextField();
        jlBolDistanceKm = new javax.swing.JLabel();
        jtfBolDistanceKm = new javax.swing.JTextField();
        jlBolGoodWeightKg = new javax.swing.JLabel();
        jtfBolGoodWeightKg = new javax.swing.JTextField();
        jlBolGood = new javax.swing.JLabel();
        jtfBolGoodCode = new javax.swing.JTextField();
        jtfBolGoodDescrip = new javax.swing.JTextField();
        jlBolGoodUnit = new javax.swing.JLabel();
        jtfBolGoodUnitCode = new javax.swing.JTextField();
        jtfBolGoodUnitDescrip = new javax.swing.JTextField();
        jpAccounting = new javax.swing.JPanel();
        jlAcc = new javax.swing.JLabel();
        jtfAccItem = new javax.swing.JTextField();
        jtfAccItemAux = new javax.swing.JTextField();
        jtfAccUnits = new javax.swing.JTextField();
        jtfAccUnit = new javax.swing.JTextField();
        jtfAccAccount = new javax.swing.JTextField();
        jtfAccCostCenter = new javax.swing.JTextField();
        jbAccShowParsingErrorOrWarning = new javax.swing.JButton();
        jpDocumentsProcessing = new javax.swing.JPanel();
        jpProcessingN = new javax.swing.JPanel();
        jpProcessingN1 = new javax.swing.JPanel();
        jbRejectInvoice = new javax.swing.JButton();
        jpProcessingN2 = new javax.swing.JPanel();
        jlInvoice = new javax.swing.JLabel();
        jbViewInvoicePdf = new javax.swing.JButton();
        jpProcessingN3 = new javax.swing.JPanel();
        jtfInvoice = new javax.swing.JTextField();
        jbViewOrder = new javax.swing.JButton();
        jpProcessingN4 = new javax.swing.JPanel();
        jtfAccountCase = new javax.swing.JTextField();
        jpProcessingN5 = new javax.swing.JPanel();
        moBoolReqPayRequire = new sa.lib.gui.bean.SBeanFieldBoolean();
        jbViewAdvances = new javax.swing.JButton();
        jpProcessingN6 = new javax.swing.JPanel();
        jtfReqPayAmount = new javax.swing.JTextField();
        jtfReqPayAmountPct = new javax.swing.JTextField();
        jpProcessingN7 = new javax.swing.JPanel();
        moDecReqPayAmount = new sa.lib.gui.bean.SBeanFieldDecimal();
        jpProcessingN61 = new javax.swing.JPanel();
        jbEditAndSaveReqPayAmount = new javax.swing.JButton();
        jbCancelEditReqPayAmount = new javax.swing.JButton();
        jpProcessingN8 = new javax.swing.JPanel();
        jtfReqPayRequiredDate = new javax.swing.JTextField();
        jbChangeReqPayRequiredDate = new javax.swing.JButton();
        jpProcessingN9 = new javax.swing.JPanel();
        jlDocs = new javax.swing.JLabel();
        jpProcessingN10 = new javax.swing.JPanel();
        jlDocsShown = new javax.swing.JLabel();
        jtfDocsShown = new javax.swing.JTextField();
        jpProcessingN11 = new javax.swing.JPanel();
        jlDocsRecordable = new javax.swing.JLabel();
        jtfDocsRecordable = new javax.swing.JTextField();
        jpProcessingN12 = new javax.swing.JPanel();
        jlDocsToRecord = new javax.swing.JLabel();
        jtfDocsToRecord = new javax.swing.JTextField();
        jpProcessingN13 = new javax.swing.JPanel();
        jlReqPays = new javax.swing.JLabel();
        jpProcessingN14 = new javax.swing.JPanel();
        jlReqPaysRequestable = new javax.swing.JLabel();
        jtfReqPaysRequestable = new javax.swing.JTextField();
        jpProcessingN15 = new javax.swing.JPanel();
        jlReqPaysToRequest = new javax.swing.JLabel();
        jtfReqPaysToRequest = new javax.swing.JTextField();
        jpProcessingN16 = new javax.swing.JPanel();
        jlReqPaysNewRequiredDate = new javax.swing.JLabel();
        jpProcessingN17 = new javax.swing.JPanel();
        jtfReqPaysNewRequiredDate = new javax.swing.JTextField();
        jbPickReqPaysNewRequiredDate = new javax.swing.JButton();
        jpProcessingN18 = new javax.swing.JPanel();
        jbSetReqPaysNewRequiredDate = new javax.swing.JButton();
        jpProcessingN19 = new javax.swing.JPanel();
        jbRecordDocs = new javax.swing.JButton();
        jpProcessingN20 = new javax.swing.JPanel();
        jlDocsRecordedAndLinked = new javax.swing.JLabel();
        jtfDocsRecordedAndLinked = new javax.swing.JTextField();
        jpProcessingN21 = new javax.swing.JPanel();
        jProgressBar = new javax.swing.JProgressBar();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Procesamiento de facturas autorizadas:"));
        jpSettings.setLayout(new java.awt.BorderLayout());

        jpSettingsW.setLayout(new java.awt.GridLayout(3, 1, 0, 1));

        jpSettingsW1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlUser.setText("Usuario:");
        jlUser.setPreferredSize(new java.awt.Dimension(75, 21));
        jpSettingsW1.add(jlUser);

        jtfUserName.setEditable(false);
        jtfUserName.setText("user.name");
        jtfUserName.setToolTipText("Usuario actual");
        jtfUserName.setFocusable(false);
        jtfUserName.setPreferredSize(new java.awt.Dimension(103, 21));
        jpSettingsW1.add(jtfUserName);

        jtfUserFuncSubAreas.setEditable(false);
        jtfUserFuncSubAreas.setText("FUNC. AREAS");
        jtfUserFuncSubAreas.setToolTipText("Subáreas funcionales");
        jtfUserFuncSubAreas.setFocusable(false);
        jtfUserFuncSubAreas.setPreferredSize(new java.awt.Dimension(313, 21));
        jpSettingsW1.add(jtfUserFuncSubAreas);

        jpSettingsW.add(jpSettingsW1);

        jpSettingsW2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgSearchBy.add(moRadSearchByPeriod);
        moRadSearchByPeriod.setText("Período:");
        moRadSearchByPeriod.setEnabled(false);
        moRadSearchByPeriod.setPreferredSize(new java.awt.Dimension(75, 21));
        jpSettingsW2.add(moRadSearchByPeriod);

        moDatePeriodStart.setToolTipText("Fecha inicial");
        moDatePeriodStart.setEnabled(false);
        moDatePeriodStart.setPreferredSize(new java.awt.Dimension(103, 21));
        jpSettingsW2.add(moDatePeriodStart);

        jLabelPeriiod1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPeriiod1.setText("−");
        jLabelPeriiod1.setPreferredSize(new java.awt.Dimension(15, 21));
        jpSettingsW2.add(jLabelPeriiod1);

        moDatePeriodEnd.setToolTipText("Fecha final");
        moDatePeriodEnd.setEnabled(false);
        moDatePeriodEnd.setPreferredSize(new java.awt.Dimension(103, 21));
        jpSettingsW2.add(moDatePeriodEnd);

        bgDocMode.add(moRadDocModeType);
        moRadDocModeType.setText("Tipo:");
        moRadDocModeType.setEnabled(false);
        moRadDocModeType.setPreferredSize(new java.awt.Dimension(65, 21));
        jpSettingsW2.add(moRadDocModeType);

        moKeyDocModeType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Activo fijo", "Compras y gastos" }));
        moKeyDocModeType.setEnabled(false);
        moKeyDocModeType.setPreferredSize(new java.awt.Dimension(115, 21));
        jpSettingsW2.add(moKeyDocModeType);

        jpSettingsW.add(jpSettingsW2);

        jpSettingsW3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgSearchBy.add(moRadSearchByWeek);
        moRadSearchByWeek.setText("Semana:");
        moRadSearchByWeek.setEnabled(false);
        moRadSearchByWeek.setPreferredSize(new java.awt.Dimension(75, 21));
        jpSettingsW3.add(moRadSearchByWeek);

        moCalWeekYear.setToolTipText("Año");
        moCalWeekYear.setEnabled(false);
        moCalWeekYear.setPreferredSize(new java.awt.Dimension(75, 21));
        jpSettingsW3.add(moCalWeekYear);

        jlLabelWeek1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlLabelWeek1.setText(":");
        jlLabelWeek1.setPreferredSize(new java.awt.Dimension(21, 21));
        jpSettingsW3.add(jlLabelWeek1);

        moCalWeekStart.setToolTipText("Semana inicial");
        moCalWeekStart.setEnabled(false);
        moCalWeekStart.setPreferredSize(new java.awt.Dimension(50, 21));
        jpSettingsW3.add(moCalWeekStart);

        jlLabelWeek2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlLabelWeek2.setText("−");
        jlLabelWeek2.setPreferredSize(new java.awt.Dimension(15, 21));
        jpSettingsW3.add(jlLabelWeek2);

        moCalWeekEnd.setToolTipText("Semana final");
        moCalWeekEnd.setEnabled(false);
        moCalWeekEnd.setPreferredSize(new java.awt.Dimension(50, 21));
        jpSettingsW3.add(moCalWeekEnd);

        bgDocMode.add(moRadDocModeCase);
        moRadDocModeCase.setText("Caso:");
        moRadDocModeCase.setEnabled(false);
        moRadDocModeCase.setPreferredSize(new java.awt.Dimension(65, 21));
        jpSettingsW3.add(moRadDocModeCase);

        moKeyDocModeCase.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Estándar", "Fletes fruta", "Compras fruta" }));
        moKeyDocModeCase.setEnabled(false);
        moKeyDocModeCase.setPreferredSize(new java.awt.Dimension(115, 21));
        jpSettingsW3.add(moKeyDocModeCase);

        jpSettingsW.add(jpSettingsW3);

        jpSettings.add(jpSettingsW, java.awt.BorderLayout.WEST);

        jpSettingsE.setLayout(new java.awt.GridLayout(3, 1, 0, 1));

        jpSettingsE1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        bgFilter.add(moRadFilterPartner);
        moRadFilterPartner.setText("Ver Emisor:");
        jpSettingsE1.add(moRadFilterPartner);

        moKeyFilterPartner.setToolTipText("Emisor");
        moKeyFilterPartner.setPreferredSize(new java.awt.Dimension(350, 23));
        jpSettingsE1.add(moKeyFilterPartner);

        jpSettingsE.add(jpSettingsE1);

        jpSettingsE2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        bgFilter.add(moRadFilterItem);
        moRadFilterItem.setText("Ver ProdServ:");
        jpSettingsE2.add(moRadFilterItem);

        moKeyFilterItem.setToolTipText("ProdServ");
        moKeyFilterItem.setPreferredSize(new java.awt.Dimension(350, 23));
        jpSettingsE2.add(moKeyFilterItem);

        jpSettingsE.add(jpSettingsE2);

        jpSettingsE3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        bgFilter.add(moRadFilterAll);
        moRadFilterAll.setText("Ver todos");
        jpSettingsE3.add(moRadFilterAll);

        jbSelectAllDocs.setText("Seleccionar todos");
        jbSelectAllDocs.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbSelectAllDocs.setPreferredSize(new java.awt.Dimension(150, 23));
        jpSettingsE3.add(jbSelectAllDocs);

        jlFilter1.setPreferredSize(new java.awt.Dimension(40, 23));
        jpSettingsE3.add(jlFilter1);

        jbDeselectAllDocs.setText("Deseleccionar todos");
        jbDeselectAllDocs.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbDeselectAllDocs.setPreferredSize(new java.awt.Dimension(150, 23));
        jpSettingsE3.add(jbDeselectAllDocs);

        jpSettingsE.add(jpSettingsE3);

        jpSettings.add(jpSettingsE, java.awt.BorderLayout.EAST);

        getContentPane().add(jpSettings, java.awt.BorderLayout.NORTH);

        jpDocuments.setLayout(new java.awt.BorderLayout());

        jpDocumentsRendering.setLayout(new java.awt.BorderLayout());

        jpVouchers.setLayout(new java.awt.BorderLayout());

        jpDocumentsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Comprobantes:"));
        jpDocumentsPanel.setLayout(new java.awt.BorderLayout());
        jpVouchers.add(jpDocumentsPanel, java.awt.BorderLayout.CENTER);

        jpVouchersDetail.setPreferredSize(new java.awt.Dimension(100, 235));
        jpVouchersDetail.setLayout(new java.awt.BorderLayout());

        jpConceptsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Conceptos del comprobante:"));
        jpConceptsPanel.setLayout(new java.awt.BorderLayout());
        jpVouchersDetail.add(jpConceptsPanel, java.awt.BorderLayout.CENTER);

        jpBol.setBorder(javax.swing.BorderFactory.createTitledBorder("Complemento carta porte del comprobante:"));
        jpBol.setLayout(new java.awt.GridLayout(3, 1, 0, 2));

        jpBol2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBolSrc.setText("Ubic. origen:");
        jlBolSrc.setPreferredSize(new java.awt.Dimension(75, 23));
        jpBol2.add(jlBolSrc);

        jtfBolSrcAddress.setEditable(false);
        jtfBolSrcAddress.setText("TEXT");
        jtfBolSrcAddress.setFocusable(false);
        jtfBolSrcAddress.setPreferredSize(new java.awt.Dimension(225, 23));
        jpBol2.add(jtfBolSrcAddress);

        jlBolSrcDistrict.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBolSrcDistrict.setText("Colonia:");
        jlBolSrcDistrict.setPreferredSize(new java.awt.Dimension(50, 23));
        jpBol2.add(jlBolSrcDistrict);

        jtfBolSrcDistrict.setEditable(false);
        jtfBolSrcDistrict.setText("TEXT");
        jtfBolSrcDistrict.setFocusable(false);
        jtfBolSrcDistrict.setPreferredSize(new java.awt.Dimension(40, 23));
        jpBol2.add(jtfBolSrcDistrict);

        jlBolSrcZipCode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBolSrcZipCode.setText("CP:");
        jlBolSrcZipCode.setPreferredSize(new java.awt.Dimension(25, 23));
        jpBol2.add(jlBolSrcZipCode);

        jtfBolSrcZipCode.setEditable(false);
        jtfBolSrcZipCode.setText("TEXT");
        jtfBolSrcZipCode.setFocusable(false);
        jtfBolSrcZipCode.setPreferredSize(new java.awt.Dimension(50, 23));
        jpBol2.add(jtfBolSrcZipCode);

        jlBolSrcLocality.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBolSrcLocality.setText("Loc.:");
        jlBolSrcLocality.setPreferredSize(new java.awt.Dimension(40, 23));
        jpBol2.add(jlBolSrcLocality);

        jtfBolSrcLocality.setEditable(false);
        jtfBolSrcLocality.setText("TEXT");
        jtfBolSrcLocality.setFocusable(false);
        jtfBolSrcLocality.setPreferredSize(new java.awt.Dimension(35, 23));
        jpBol2.add(jtfBolSrcLocality);

        jlBolSrcCounty.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBolSrcCounty.setText("Mcpio.:");
        jlBolSrcCounty.setPreferredSize(new java.awt.Dimension(50, 23));
        jpBol2.add(jlBolSrcCounty);

        jtfBolSrcCounty.setEditable(false);
        jtfBolSrcCounty.setText("TEXT");
        jtfBolSrcCounty.setFocusable(false);
        jtfBolSrcCounty.setPreferredSize(new java.awt.Dimension(35, 23));
        jpBol2.add(jtfBolSrcCounty);

        jlBolSrcState.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBolSrcState.setText("Edo.:");
        jlBolSrcState.setPreferredSize(new java.awt.Dimension(40, 23));
        jpBol2.add(jlBolSrcState);

        jtfBolSrcState.setEditable(false);
        jtfBolSrcState.setText("TEXT");
        jtfBolSrcState.setFocusable(false);
        jtfBolSrcState.setPreferredSize(new java.awt.Dimension(35, 23));
        jpBol2.add(jtfBolSrcState);

        jlBolSrcCountry.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBolSrcCountry.setText("País:");
        jlBolSrcCountry.setPreferredSize(new java.awt.Dimension(35, 23));
        jpBol2.add(jlBolSrcCountry);

        jtfBolSrcCountry.setEditable(false);
        jtfBolSrcCountry.setText("TEXT");
        jtfBolSrcCountry.setFocusable(false);
        jtfBolSrcCountry.setPreferredSize(new java.awt.Dimension(35, 23));
        jpBol2.add(jtfBolSrcCountry);

        jpBol.add(jpBol2);

        jpBol3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBolDes.setText("Ubic. destino:");
        jlBolDes.setPreferredSize(new java.awt.Dimension(75, 23));
        jpBol3.add(jlBolDes);

        jtfBolDesAddress.setEditable(false);
        jtfBolDesAddress.setText("TEXT");
        jtfBolDesAddress.setFocusable(false);
        jtfBolDesAddress.setPreferredSize(new java.awt.Dimension(225, 23));
        jpBol3.add(jtfBolDesAddress);

        jlBolDesDistrict.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBolDesDistrict.setText("Colonia:");
        jlBolDesDistrict.setPreferredSize(new java.awt.Dimension(50, 23));
        jpBol3.add(jlBolDesDistrict);

        jtfBolDesDistrict.setEditable(false);
        jtfBolDesDistrict.setText("TEXT");
        jtfBolDesDistrict.setFocusable(false);
        jtfBolDesDistrict.setPreferredSize(new java.awt.Dimension(40, 23));
        jpBol3.add(jtfBolDesDistrict);

        jlBolDesZipCode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBolDesZipCode.setText("CP:");
        jlBolDesZipCode.setPreferredSize(new java.awt.Dimension(25, 23));
        jpBol3.add(jlBolDesZipCode);

        jtfBolDesZipCode.setEditable(false);
        jtfBolDesZipCode.setText("TEXT");
        jtfBolDesZipCode.setFocusable(false);
        jtfBolDesZipCode.setPreferredSize(new java.awt.Dimension(50, 23));
        jpBol3.add(jtfBolDesZipCode);

        jlBolDesLocality.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBolDesLocality.setText("Loc.:");
        jlBolDesLocality.setPreferredSize(new java.awt.Dimension(40, 23));
        jpBol3.add(jlBolDesLocality);

        jtfBolDesLocality.setEditable(false);
        jtfBolDesLocality.setText("TEXT");
        jtfBolDesLocality.setFocusable(false);
        jtfBolDesLocality.setPreferredSize(new java.awt.Dimension(35, 23));
        jpBol3.add(jtfBolDesLocality);

        jlBolDesCounty.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBolDesCounty.setText("Mcpio.:");
        jlBolDesCounty.setPreferredSize(new java.awt.Dimension(50, 23));
        jpBol3.add(jlBolDesCounty);

        jtfBolDesCounty.setEditable(false);
        jtfBolDesCounty.setText("TEXT");
        jtfBolDesCounty.setFocusable(false);
        jtfBolDesCounty.setPreferredSize(new java.awt.Dimension(35, 23));
        jpBol3.add(jtfBolDesCounty);

        jlBolDesState.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBolDesState.setText("Edo.:");
        jlBolDesState.setPreferredSize(new java.awt.Dimension(40, 23));
        jpBol3.add(jlBolDesState);

        jtfBolDesState.setEditable(false);
        jtfBolDesState.setText("TEXT");
        jtfBolDesState.setFocusable(false);
        jtfBolDesState.setPreferredSize(new java.awt.Dimension(35, 23));
        jpBol3.add(jtfBolDesState);

        jlBolDesCountry.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBolDesCountry.setText("País:");
        jlBolDesCountry.setPreferredSize(new java.awt.Dimension(35, 23));
        jpBol3.add(jlBolDesCountry);

        jtfBolDesCountry.setEditable(false);
        jtfBolDesCountry.setText("TEXT");
        jtfBolDesCountry.setFocusable(false);
        jtfBolDesCountry.setPreferredSize(new java.awt.Dimension(35, 23));
        jpBol3.add(jtfBolDesCountry);

        jpBol.add(jpBol3);

        jpBol4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBolScaleTicket.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlBolScaleTicket.setText("Boleto:");
        jlBolScaleTicket.setPreferredSize(new java.awt.Dimension(47, 23));
        jpBol4.add(jlBolScaleTicket);

        jbBolViewScaleTicket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_mod_qlt.png"))); // NOI18N
        jbBolViewScaleTicket.setToolTipText("Ver boleto...");
        jbBolViewScaleTicket.setEnabled(false);
        jbBolViewScaleTicket.setPreferredSize(new java.awt.Dimension(23, 23));
        jpBol4.add(jbBolViewScaleTicket);

        jtfBolScaleTicket.setEditable(false);
        jtfBolScaleTicket.setText("000000");
        jtfBolScaleTicket.setFocusable(false);
        jtfBolScaleTicket.setPreferredSize(new java.awt.Dimension(60, 23));
        jpBol4.add(jtfBolScaleTicket);

        jlBolDistanceKm.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBolDistanceKm.setText("Dist. (km):");
        jlBolDistanceKm.setPreferredSize(new java.awt.Dimension(60, 23));
        jpBol4.add(jlBolDistanceKm);

        jtfBolDistanceKm.setEditable(false);
        jtfBolDistanceKm.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfBolDistanceKm.setText("0");
        jtfBolDistanceKm.setFocusable(false);
        jtfBolDistanceKm.setPreferredSize(new java.awt.Dimension(60, 23));
        jpBol4.add(jtfBolDistanceKm);

        jlBolGoodWeightKg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBolGoodWeightKg.setText("Peso (kg):");
        jlBolGoodWeightKg.setPreferredSize(new java.awt.Dimension(60, 23));
        jpBol4.add(jlBolGoodWeightKg);

        jtfBolGoodWeightKg.setEditable(false);
        jtfBolGoodWeightKg.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfBolGoodWeightKg.setText("0");
        jtfBolGoodWeightKg.setFocusable(false);
        jtfBolGoodWeightKg.setPreferredSize(new java.awt.Dimension(65, 23));
        jpBol4.add(jtfBolGoodWeightKg);

        jlBolGood.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBolGood.setText("Mercía.:");
        jlBolGood.setPreferredSize(new java.awt.Dimension(55, 23));
        jpBol4.add(jlBolGood);

        jtfBolGoodCode.setEditable(false);
        jtfBolGoodCode.setText("00000000");
        jtfBolGoodCode.setToolTipText("Clave ProdServ");
        jtfBolGoodCode.setFocusable(false);
        jtfBolGoodCode.setPreferredSize(new java.awt.Dimension(65, 23));
        jpBol4.add(jtfBolGoodCode);

        jtfBolGoodDescrip.setEditable(false);
        jtfBolGoodDescrip.setText("TEXT");
        jtfBolGoodDescrip.setToolTipText("Descripción ProdServ");
        jtfBolGoodDescrip.setFocusable(false);
        jtfBolGoodDescrip.setPreferredSize(new java.awt.Dimension(130, 23));
        jpBol4.add(jtfBolGoodDescrip);

        jlBolGoodUnit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBolGoodUnit.setText("Unid.:");
        jlBolGoodUnit.setPreferredSize(new java.awt.Dimension(40, 23));
        jpBol4.add(jlBolGoodUnit);

        jtfBolGoodUnitCode.setEditable(false);
        jtfBolGoodUnitCode.setText("TEXT");
        jtfBolGoodUnitCode.setToolTipText("Clave Unidad");
        jtfBolGoodUnitCode.setFocusable(false);
        jtfBolGoodUnitCode.setPreferredSize(new java.awt.Dimension(35, 23));
        jpBol4.add(jtfBolGoodUnitCode);

        jtfBolGoodUnitDescrip.setEditable(false);
        jtfBolGoodUnitDescrip.setText("TEXT");
        jtfBolGoodUnitDescrip.setToolTipText("Nombre Unidad");
        jtfBolGoodUnitDescrip.setFocusable(false);
        jtfBolGoodUnitDescrip.setPreferredSize(new java.awt.Dimension(75, 23));
        jpBol4.add(jtfBolGoodUnitDescrip);

        jpBol.add(jpBol4);

        jpVouchersDetail.add(jpBol, java.awt.BorderLayout.SOUTH);

        jpVouchers.add(jpVouchersDetail, java.awt.BorderLayout.SOUTH);

        jpDocumentsRendering.add(jpVouchers, java.awt.BorderLayout.CENTER);

        jpAccounting.setBorder(javax.swing.BorderFactory.createTitledBorder("Contabilización:"));
        jpAccounting.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAcc.setText("Datos:");
        jlAcc.setPreferredSize(new java.awt.Dimension(75, 23));
        jpAccounting.add(jlAcc);

        jtfAccItem.setEditable(false);
        jtfAccItem.setText("TEXT");
        jtfAccItem.setToolTipText("Ítem");
        jtfAccItem.setFocusable(false);
        jtfAccItem.setPreferredSize(new java.awt.Dimension(225, 23));
        jpAccounting.add(jtfAccItem);

        jtfAccItemAux.setEditable(false);
        jtfAccItemAux.setText("TEXT");
        jtfAccItemAux.setToolTipText("Ítem auxiliar");
        jtfAccItemAux.setFocusable(false);
        jtfAccItemAux.setPreferredSize(new java.awt.Dimension(150, 23));
        jpAccounting.add(jtfAccItemAux);

        jtfAccUnits.setEditable(false);
        jtfAccUnits.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfAccUnits.setText("0");
        jtfAccUnits.setToolTipText("Unidades");
        jtfAccUnits.setFocusable(false);
        jtfAccUnits.setPreferredSize(new java.awt.Dimension(95, 23));
        jpAccounting.add(jtfAccUnits);

        jtfAccUnit.setEditable(false);
        jtfAccUnit.setText("TEXT");
        jtfAccUnit.setToolTipText("Unidad");
        jtfAccUnit.setFocusable(false);
        jtfAccUnit.setPreferredSize(new java.awt.Dimension(35, 23));
        jpAccounting.add(jtfAccUnit);

        jtfAccAccount.setEditable(false);
        jtfAccAccount.setText("0000-0000-0000");
        jtfAccAccount.setToolTipText("Cuenta contable");
        jtfAccAccount.setFocusable(false);
        jtfAccAccount.setPreferredSize(new java.awt.Dimension(100, 23));
        jpAccounting.add(jtfAccAccount);

        jtfAccCostCenter.setEditable(false);
        jtfAccCostCenter.setText("000-00-00-000");
        jtfAccCostCenter.setToolTipText("Centro de costo");
        jtfAccCostCenter.setFocusable(false);
        jtfAccCostCenter.setPreferredSize(new java.awt.Dimension(95, 23));
        jpAccounting.add(jtfAccCostCenter);

        jbAccShowParsingErrorOrWarning.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_warn.png"))); // NOI18N
        jbAccShowParsingErrorOrWarning.setToolTipText("Ver error o advertencia...");
        jbAccShowParsingErrorOrWarning.setPreferredSize(new java.awt.Dimension(23, 23));
        jpAccounting.add(jbAccShowParsingErrorOrWarning);

        jpDocumentsRendering.add(jpAccounting, java.awt.BorderLayout.SOUTH);

        jpDocuments.add(jpDocumentsRendering, java.awt.BorderLayout.CENTER);

        jpDocumentsProcessing.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 2));
        jpDocumentsProcessing.setLayout(new java.awt.BorderLayout());

        jpProcessingN.setLayout(new java.awt.GridLayout(21, 1, 0, 1));

        jpProcessingN1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbRejectInvoice.setForeground(java.awt.Color.red);
        jbRejectInvoice.setText("Rechazar factura");
        jbRejectInvoice.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbRejectInvoice.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN1.add(jbRejectInvoice);

        jpProcessingN.add(jpProcessingN1);

        jpProcessingN2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInvoice.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlInvoice.setText("Factura:");
        jlInvoice.setPreferredSize(new java.awt.Dimension(122, 23));
        jpProcessingN2.add(jlInvoice);

        jbViewInvoicePdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon-file-pdf.png"))); // NOI18N
        jbViewInvoicePdf.setToolTipText("Ver PDF de la factura...");
        jbViewInvoicePdf.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN2.add(jbViewInvoicePdf);

        jpProcessingN.add(jpProcessingN2);

        jpProcessingN3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfInvoice.setEditable(false);
        jtfInvoice.setText("ABC-000000");
        jtfInvoice.setToolTipText("Factura");
        jtfInvoice.setFocusable(false);
        jtfInvoice.setPreferredSize(new java.awt.Dimension(122, 23));
        jpProcessingN3.add(jtfInvoice);

        jbViewOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_seek.gif"))); // NOI18N
        jbViewOrder.setToolTipText("Ver pedido de la factura...");
        jbViewOrder.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN3.add(jbViewOrder);

        jpProcessingN.add(jpProcessingN3);

        jpProcessingN4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfAccountCase.setEditable(false);
        jtfAccountCase.setText("FTE PM + Compra semilla girasol alto oleico");
        jtfAccountCase.setFocusable(false);
        jtfAccountCase.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN4.add(jtfAccountCase);

        jpProcessingN.add(jpProcessingN4);

        jpProcessingN5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolReqPayRequire.setText("Pago requerido:");
        moBoolReqPayRequire.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        moBoolReqPayRequire.setPreferredSize(new java.awt.Dimension(122, 23));
        jpProcessingN5.add(moBoolReqPayRequire);

        jbViewAdvances.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_warn.png"))); // NOI18N
        jbViewAdvances.setToolTipText("Ver anticipos del proveedor...");
        jbViewAdvances.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN5.add(jbViewAdvances);

        jpProcessingN.add(jpProcessingN5);

        jpProcessingN6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfReqPayAmount.setEditable(false);
        jtfReqPayAmount.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfReqPayAmount.setText("000,000,000.00 MXN");
        jtfReqPayAmount.setToolTipText("Pago requerido");
        jtfReqPayAmount.setFocusable(false);
        jtfReqPayAmount.setPreferredSize(new java.awt.Dimension(105, 23));
        jpProcessingN6.add(jtfReqPayAmount);

        jtfReqPayAmountPct.setEditable(false);
        jtfReqPayAmountPct.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfReqPayAmountPct.setText("100%");
        jtfReqPayAmountPct.setToolTipText("Porcentaje de pago requerido");
        jtfReqPayAmountPct.setFocusable(false);
        jtfReqPayAmountPct.setPreferredSize(new java.awt.Dimension(40, 23));
        jpProcessingN6.add(jtfReqPayAmountPct);

        jpProcessingN.add(jpProcessingN6);

        jpProcessingN7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moDecReqPayAmount.setPreferredSize(new java.awt.Dimension(105, 23));
        jpProcessingN7.add(moDecReqPayAmount);

        jpProcessingN61.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jbEditAndSaveReqPayAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditAndSaveReqPayAmount.setToolTipText("Modificar monto requerido de pago");
        jbEditAndSaveReqPayAmount.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN61.add(jbEditAndSaveReqPayAmount);

        jbCancelEditReqPayAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/ico_close.png"))); // NOI18N
        jbCancelEditReqPayAmount.setToolTipText("Cancelar modificación");
        jbCancelEditReqPayAmount.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbCancelEditReqPayAmount.setPreferredSize(new java.awt.Dimension(17, 23));
        jpProcessingN61.add(jbCancelEditReqPayAmount);

        jpProcessingN7.add(jpProcessingN61);

        jpProcessingN.add(jpProcessingN7);

        jpProcessingN8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfReqPayRequiredDate.setEditable(false);
        jtfReqPayRequiredDate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfReqPayRequiredDate.setText("dow 01/mon/2001");
        jtfReqPayRequiredDate.setToolTipText("Fecha requerida de pago");
        jtfReqPayRequiredDate.setFocusable(false);
        jtfReqPayRequiredDate.setPreferredSize(new java.awt.Dimension(105, 23));
        jpProcessingN8.add(jtfReqPayRequiredDate);

        jbChangeReqPayRequiredDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbChangeReqPayRequiredDate.setToolTipText("Cambiar fecha requerida de pago...");
        jbChangeReqPayRequiredDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN8.add(jbChangeReqPayRequiredDate);

        jpProcessingN.add(jpProcessingN8);

        jpProcessingN9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocs.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlDocs.setText("Comprobantes:");
        jlDocs.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN9.add(jlDocs);

        jpProcessingN.add(jpProcessingN9);

        jpProcessingN10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocsShown.setText("Mostrados:");
        jlDocsShown.setPreferredSize(new java.awt.Dimension(75, 23));
        jpProcessingN10.add(jlDocsShown);

        jtfDocsShown.setEditable(false);
        jtfDocsShown.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocsShown.setText("0");
        jtfDocsShown.setFocusable(false);
        jtfDocsShown.setPreferredSize(new java.awt.Dimension(70, 23));
        jpProcessingN10.add(jtfDocsShown);

        jpProcessingN.add(jpProcessingN10);

        jpProcessingN11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocsRecordable.setText("Procesables:");
        jlDocsRecordable.setPreferredSize(new java.awt.Dimension(75, 23));
        jpProcessingN11.add(jlDocsRecordable);

        jtfDocsRecordable.setEditable(false);
        jtfDocsRecordable.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocsRecordable.setText("0");
        jtfDocsRecordable.setFocusable(false);
        jtfDocsRecordable.setPreferredSize(new java.awt.Dimension(70, 23));
        jpProcessingN11.add(jtfDocsRecordable);

        jpProcessingN.add(jpProcessingN11);

        jpProcessingN12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocsToRecord.setText("A procesar:");
        jlDocsToRecord.setPreferredSize(new java.awt.Dimension(75, 23));
        jpProcessingN12.add(jlDocsToRecord);

        jtfDocsToRecord.setEditable(false);
        jtfDocsToRecord.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfDocsToRecord.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocsToRecord.setText("0");
        jtfDocsToRecord.setFocusable(false);
        jtfDocsToRecord.setPreferredSize(new java.awt.Dimension(70, 23));
        jpProcessingN12.add(jtfDocsToRecord);

        jpProcessingN.add(jpProcessingN12);

        jpProcessingN13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReqPays.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlReqPays.setText("Solicitudes de pago:");
        jlReqPays.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN13.add(jlReqPays);

        jpProcessingN.add(jpProcessingN13);

        jpProcessingN14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReqPaysRequestable.setText("Solicitables:");
        jlReqPaysRequestable.setPreferredSize(new java.awt.Dimension(75, 23));
        jpProcessingN14.add(jlReqPaysRequestable);

        jtfReqPaysRequestable.setEditable(false);
        jtfReqPaysRequestable.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfReqPaysRequestable.setText("0");
        jtfReqPaysRequestable.setFocusable(false);
        jtfReqPaysRequestable.setPreferredSize(new java.awt.Dimension(70, 23));
        jpProcessingN14.add(jtfReqPaysRequestable);

        jpProcessingN.add(jpProcessingN14);

        jpProcessingN15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReqPaysToRequest.setText("A solicitar:");
        jlReqPaysToRequest.setPreferredSize(new java.awt.Dimension(75, 23));
        jpProcessingN15.add(jlReqPaysToRequest);

        jtfReqPaysToRequest.setEditable(false);
        jtfReqPaysToRequest.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfReqPaysToRequest.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfReqPaysToRequest.setText("0");
        jtfReqPaysToRequest.setFocusable(false);
        jtfReqPaysToRequest.setPreferredSize(new java.awt.Dimension(70, 23));
        jpProcessingN15.add(jtfReqPaysToRequest);

        jpProcessingN.add(jpProcessingN15);

        jpProcessingN16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReqPaysNewRequiredDate.setText("Nueva fecha requerida:");
        jlReqPaysNewRequiredDate.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN16.add(jlReqPaysNewRequiredDate);

        jpProcessingN.add(jpProcessingN16);

        jpProcessingN17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfReqPaysNewRequiredDate.setEditable(false);
        jtfReqPaysNewRequiredDate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfReqPaysNewRequiredDate.setText("dow 01/mon/2001");
        jtfReqPaysNewRequiredDate.setToolTipText("Fecha requerida de pago");
        jtfReqPaysNewRequiredDate.setFocusable(false);
        jtfReqPaysNewRequiredDate.setPreferredSize(new java.awt.Dimension(105, 23));
        jpProcessingN17.add(jtfReqPaysNewRequiredDate);

        jbPickReqPaysNewRequiredDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbPickReqPaysNewRequiredDate.setToolTipText("Cambiar fechas requeridas de pago...");
        jbPickReqPaysNewRequiredDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN17.add(jbPickReqPaysNewRequiredDate);

        jpProcessingN.add(jpProcessingN17);

        jpProcessingN18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbSetReqPaysNewRequiredDate.setText("Asignar nueva fecha");
        jbSetReqPaysNewRequiredDate.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbSetReqPaysNewRequiredDate.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN18.add(jbSetReqPaysNewRequiredDate);

        jpProcessingN.add(jpProcessingN18);

        jpProcessingN19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbRecordDocs.setForeground(java.awt.Color.blue);
        jbRecordDocs.setText("Procesar comprobantes");
        jbRecordDocs.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbRecordDocs.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN19.add(jbRecordDocs);

        jpProcessingN.add(jpProcessingN19);

        jpProcessingN20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocsRecordedAndLinked.setText("Procesados:");
        jlDocsRecordedAndLinked.setPreferredSize(new java.awt.Dimension(75, 23));
        jpProcessingN20.add(jlDocsRecordedAndLinked);

        jtfDocsRecordedAndLinked.setEditable(false);
        jtfDocsRecordedAndLinked.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfDocsRecordedAndLinked.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocsRecordedAndLinked.setText("0");
        jtfDocsRecordedAndLinked.setFocusable(false);
        jtfDocsRecordedAndLinked.setPreferredSize(new java.awt.Dimension(70, 23));
        jpProcessingN20.add(jtfDocsRecordedAndLinked);

        jpProcessingN.add(jpProcessingN20);

        jpProcessingN21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jProgressBar.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN21.add(jProgressBar);

        jpProcessingN.add(jpProcessingN21);

        jpDocumentsProcessing.add(jpProcessingN, java.awt.BorderLayout.NORTH);

        jpDocuments.add(jpDocumentsProcessing, java.awt.BorderLayout.EAST);

        getContentPane().add(jpDocuments, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgDocMode;
    private javax.swing.ButtonGroup bgFilter;
    private javax.swing.ButtonGroup bgSearchBy;
    private javax.swing.JLabel jLabelPeriiod1;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JButton jbAccShowParsingErrorOrWarning;
    private javax.swing.JButton jbBolViewScaleTicket;
    private javax.swing.JButton jbCancelEditReqPayAmount;
    private javax.swing.JButton jbChangeReqPayRequiredDate;
    private javax.swing.JButton jbDeselectAllDocs;
    private javax.swing.JButton jbEditAndSaveReqPayAmount;
    private javax.swing.JButton jbPickReqPaysNewRequiredDate;
    private javax.swing.JButton jbRecordDocs;
    private javax.swing.JButton jbRejectInvoice;
    private javax.swing.JButton jbSelectAllDocs;
    private javax.swing.JButton jbSetReqPaysNewRequiredDate;
    private javax.swing.JButton jbViewAdvances;
    private javax.swing.JButton jbViewInvoicePdf;
    private javax.swing.JButton jbViewOrder;
    private javax.swing.JLabel jlAcc;
    private javax.swing.JLabel jlBolDes;
    private javax.swing.JLabel jlBolDesCountry;
    private javax.swing.JLabel jlBolDesCounty;
    private javax.swing.JLabel jlBolDesDistrict;
    private javax.swing.JLabel jlBolDesLocality;
    private javax.swing.JLabel jlBolDesState;
    private javax.swing.JLabel jlBolDesZipCode;
    private javax.swing.JLabel jlBolDistanceKm;
    private javax.swing.JLabel jlBolGood;
    private javax.swing.JLabel jlBolGoodUnit;
    private javax.swing.JLabel jlBolGoodWeightKg;
    private javax.swing.JLabel jlBolScaleTicket;
    private javax.swing.JLabel jlBolSrc;
    private javax.swing.JLabel jlBolSrcCountry;
    private javax.swing.JLabel jlBolSrcCounty;
    private javax.swing.JLabel jlBolSrcDistrict;
    private javax.swing.JLabel jlBolSrcLocality;
    private javax.swing.JLabel jlBolSrcState;
    private javax.swing.JLabel jlBolSrcZipCode;
    private javax.swing.JLabel jlDocs;
    private javax.swing.JLabel jlDocsRecordable;
    private javax.swing.JLabel jlDocsRecordedAndLinked;
    private javax.swing.JLabel jlDocsShown;
    private javax.swing.JLabel jlDocsToRecord;
    private javax.swing.JLabel jlFilter1;
    private javax.swing.JLabel jlInvoice;
    private javax.swing.JLabel jlLabelWeek1;
    private javax.swing.JLabel jlLabelWeek2;
    private javax.swing.JLabel jlReqPays;
    private javax.swing.JLabel jlReqPaysNewRequiredDate;
    private javax.swing.JLabel jlReqPaysRequestable;
    private javax.swing.JLabel jlReqPaysToRequest;
    private javax.swing.JLabel jlUser;
    private javax.swing.JPanel jpAccounting;
    private javax.swing.JPanel jpBol;
    private javax.swing.JPanel jpBol2;
    private javax.swing.JPanel jpBol3;
    private javax.swing.JPanel jpBol4;
    private javax.swing.JPanel jpConceptsPanel;
    private javax.swing.JPanel jpDocuments;
    private javax.swing.JPanel jpDocumentsPanel;
    private javax.swing.JPanel jpDocumentsProcessing;
    private javax.swing.JPanel jpDocumentsRendering;
    private javax.swing.JPanel jpProcessingN;
    private javax.swing.JPanel jpProcessingN1;
    private javax.swing.JPanel jpProcessingN10;
    private javax.swing.JPanel jpProcessingN11;
    private javax.swing.JPanel jpProcessingN12;
    private javax.swing.JPanel jpProcessingN13;
    private javax.swing.JPanel jpProcessingN14;
    private javax.swing.JPanel jpProcessingN15;
    private javax.swing.JPanel jpProcessingN16;
    private javax.swing.JPanel jpProcessingN17;
    private javax.swing.JPanel jpProcessingN18;
    private javax.swing.JPanel jpProcessingN19;
    private javax.swing.JPanel jpProcessingN2;
    private javax.swing.JPanel jpProcessingN20;
    private javax.swing.JPanel jpProcessingN21;
    private javax.swing.JPanel jpProcessingN3;
    private javax.swing.JPanel jpProcessingN4;
    private javax.swing.JPanel jpProcessingN5;
    private javax.swing.JPanel jpProcessingN6;
    private javax.swing.JPanel jpProcessingN61;
    private javax.swing.JPanel jpProcessingN7;
    private javax.swing.JPanel jpProcessingN8;
    private javax.swing.JPanel jpProcessingN9;
    private javax.swing.JPanel jpSettings;
    private javax.swing.JPanel jpSettingsE;
    private javax.swing.JPanel jpSettingsE1;
    private javax.swing.JPanel jpSettingsE2;
    private javax.swing.JPanel jpSettingsE3;
    private javax.swing.JPanel jpSettingsW;
    private javax.swing.JPanel jpSettingsW1;
    private javax.swing.JPanel jpSettingsW2;
    private javax.swing.JPanel jpSettingsW3;
    private javax.swing.JPanel jpVouchers;
    private javax.swing.JPanel jpVouchersDetail;
    private javax.swing.JTextField jtfAccAccount;
    private javax.swing.JTextField jtfAccCostCenter;
    private javax.swing.JTextField jtfAccItem;
    private javax.swing.JTextField jtfAccItemAux;
    private javax.swing.JTextField jtfAccUnit;
    private javax.swing.JTextField jtfAccUnits;
    private javax.swing.JTextField jtfAccountCase;
    private javax.swing.JTextField jtfBolDesAddress;
    private javax.swing.JTextField jtfBolDesCountry;
    private javax.swing.JTextField jtfBolDesCounty;
    private javax.swing.JTextField jtfBolDesDistrict;
    private javax.swing.JTextField jtfBolDesLocality;
    private javax.swing.JTextField jtfBolDesState;
    private javax.swing.JTextField jtfBolDesZipCode;
    private javax.swing.JTextField jtfBolDistanceKm;
    private javax.swing.JTextField jtfBolGoodCode;
    private javax.swing.JTextField jtfBolGoodDescrip;
    private javax.swing.JTextField jtfBolGoodUnitCode;
    private javax.swing.JTextField jtfBolGoodUnitDescrip;
    private javax.swing.JTextField jtfBolGoodWeightKg;
    private javax.swing.JTextField jtfBolScaleTicket;
    private javax.swing.JTextField jtfBolSrcAddress;
    private javax.swing.JTextField jtfBolSrcCountry;
    private javax.swing.JTextField jtfBolSrcCounty;
    private javax.swing.JTextField jtfBolSrcDistrict;
    private javax.swing.JTextField jtfBolSrcLocality;
    private javax.swing.JTextField jtfBolSrcState;
    private javax.swing.JTextField jtfBolSrcZipCode;
    private javax.swing.JTextField jtfDocsRecordable;
    private javax.swing.JTextField jtfDocsRecordedAndLinked;
    private javax.swing.JTextField jtfDocsShown;
    private javax.swing.JTextField jtfDocsToRecord;
    private javax.swing.JTextField jtfInvoice;
    private javax.swing.JTextField jtfReqPayAmount;
    private javax.swing.JTextField jtfReqPayAmountPct;
    private javax.swing.JTextField jtfReqPayRequiredDate;
    private javax.swing.JTextField jtfReqPaysNewRequiredDate;
    private javax.swing.JTextField jtfReqPaysRequestable;
    private javax.swing.JTextField jtfReqPaysToRequest;
    private javax.swing.JTextField jtfUserFuncSubAreas;
    private javax.swing.JTextField jtfUserName;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolReqPayRequire;
    private sa.lib.gui.bean.SBeanFieldCalendarWeek moCalWeekEnd;
    private sa.lib.gui.bean.SBeanFieldCalendarWeek moCalWeekStart;
    private sa.lib.gui.bean.SBeanFieldCalendarYear moCalWeekYear;
    private sa.lib.gui.bean.SBeanFieldDate moDatePeriodEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDatePeriodStart;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecReqPayAmount;
    private sa.lib.gui.bean.SBeanFieldKey moKeyDocModeCase;
    private sa.lib.gui.bean.SBeanFieldKey moKeyDocModeType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyFilterItem;
    private sa.lib.gui.bean.SBeanFieldKey moKeyFilterPartner;
    private sa.lib.gui.bean.SBeanFieldRadio moRadDocModeCase;
    private sa.lib.gui.bean.SBeanFieldRadio moRadDocModeType;
    private sa.lib.gui.bean.SBeanFieldRadio moRadFilterAll;
    private sa.lib.gui.bean.SBeanFieldRadio moRadFilterItem;
    private sa.lib.gui.bean.SBeanFieldRadio moRadFilterPartner;
    private sa.lib.gui.bean.SBeanFieldRadio moRadSearchByPeriod;
    private sa.lib.gui.bean.SBeanFieldRadio moRadSearchByWeek;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods.
     */
    
    @SuppressWarnings("unchecked")
    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 670);
        
        moRadFilterPartner.setBooleanSettings(SGuiUtils.getLabelName(moRadFilterPartner.getText()), true);
        moRadFilterItem.setBooleanSettings(SGuiUtils.getLabelName(moRadFilterItem.getText()), false);
        moRadFilterAll.setBooleanSettings(SGuiUtils.getLabelName(moRadFilterAll.getText()), false);
        moKeyFilterPartner.setKeySettings(miClient, SGuiUtils.getLabelName(moRadFilterPartner.getText()), false);
        moKeyFilterItem.setKeySettings(miClient, SGuiUtils.getLabelName(moRadFilterItem.getText()), false);
        moBoolReqPayRequire.setBooleanSettings(SGuiUtils.getLabelName(moBoolReqPayRequire.getText()), false);
        moDecReqPayAmount.setDecimalSettings(SGuiUtils.getLabelName(moBoolReqPayRequire.getText()), SGuiConsts.GUI_TYPE_DEC_AMT, false);
        
        moFields.addField(moRadFilterPartner);
        moFields.addField(moRadFilterItem);
        moFields.addField(moRadFilterAll);
        moFields.addField(moKeyFilterPartner);
        moFields.addField(moKeyFilterItem);
        moFields.addField(moBoolReqPayRequire);
        moFields.addField(moDecReqPayAmount);
        
        moFields.setFormButton(jbSelectAllDocs);
        
        moKeyDocModeType.removeAllItems();
        moKeyDocModeType.addItem(new SGuiItem(new int[] { SImportedDocument.DOC_TYPE_ALL }, SImportedDocument.DocTypes.get(SImportedDocument.DOC_TYPE_ALL)));
        moKeyDocModeType.addItem(new SGuiItem(new int[] { SImportedDocument.DOC_TYPE_ASSETS }, SImportedDocument.DocTypes.get(SImportedDocument.DOC_TYPE_ASSETS)));
        moKeyDocModeType.addItem(new SGuiItem(new int[] { SImportedDocument.DOC_TYPE_EXPENSES }, SImportedDocument.DocTypes.get(SImportedDocument.DOC_TYPE_EXPENSES)));
        
        moKeyDocModeCase.removeAllItems();
        moKeyDocModeCase.addItem(new SGuiItem(new int[] { SImportedDocument.DOC_CASE_ALL }, SImportedDocument.DocCases.get(SImportedDocument.DOC_CASE_ALL)));
        moKeyDocModeCase.addItem(new SGuiItem(new int[] { SImportedDocument.DOC_CASE_STANDARD}, SImportedDocument.DocCases.get(SImportedDocument.DOC_CASE_STANDARD)));
        moKeyDocModeCase.addItem(new SGuiItem(new int[] { SImportedDocument.DOC_CASE_RAW_MAT_FREIGHT}, SImportedDocument.DocCases.get(SImportedDocument.DOC_CASE_RAW_MAT_FREIGHT)));
        moKeyDocModeCase.addItem(new SGuiItem(new int[] { SImportedDocument.DOC_CASE_RAW_MAT_PURCHASE}, SImportedDocument.DocCases.get(SImportedDocument.DOC_CASE_RAW_MAT_PURCHASE)));
        
        msCompanyName = SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.CFGU_CO, new int[] { miClient.getSession().getConfigCompany().getCompanyId() }, SLibConstants.DESCRIPTION_NAME);
        
        moDocumentsGrid = new SGridPaneForm(miClient, SModConsts.CFGX_SWAP_MASS_ACC, 1, "Facturas", null) {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                SGridColumnForm column;
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Proveedor factura", 200)); // col 0
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Folio factura", 75));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha factura"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Referencias factura", 75));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Descripción factura"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Total factura $")); // col 5
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda factura"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_ICON, "Urgente"));
                column = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Procesar", moDocumentsGrid.getTable().getDefaultEditor(Boolean.class));
                column.setEditable(true);
                gridColumnsForm.add(column);
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_ICON, "Procesable"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_ICON, "Procesado")); // col 10
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Caso"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Caso auxiliar"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Boleto", 60));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_ICON, "Validación boleto"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Médogo pago factura"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Régimen fiscal proveedor"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Uso CFDI factura")); // col 15
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Subárea funcional factura"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Semana revisión factura", 50));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE_DATETIME, "Fecha-hora revisión factura"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Pago requerido"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_PER_0D, "Pago requerido %")); // col 20
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Pago requerido $"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Nuevo pago requerido $"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda pago requerido"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha requerida pago"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Nueva fecha requerida pago")); // col 25
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Pago requerido moneda local"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Instrucciones pago requerido"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Tipo definición pago requerido"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha vencimiento factura"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Etiqueta contable")); // col 30
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "UUID factura " + SSwapConsts.SWAP_SERVICES, 225));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_RAW, "ID factura " + SSwapConsts.SWAP_SERVICES));
                
                return gridColumnsForm;
            }
        };

        moDocumentsGrid.setForm(null);
        moDocumentsGrid.setPaneFormOwner(null);
        jpDocumentsPanel.add(moDocumentsGrid, BorderLayout.CENTER);
        
        moConceptsGrid = new SGridPaneForm(miClient, SModConsts.CFGX_SWAP_MASS_ACC, 2, "Conceptos", null) {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Clave ProdServ")); // col 0
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Descripción ProdServ", 200));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Cantidad"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Clave Unidad"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Nombre Unidad"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT_UNIT, "Valor Unitario")); // col 5
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Importe"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Objeto Impuesto"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "IVA Trasladado"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "IVA Retenido"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "ISR Retenido")); // col 10
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Descuento"));
                
                return gridColumnsForm;
            }
        };

        moConceptsGrid.setForm(null);
        moConceptsGrid.setPaneFormOwner(null);
        jpConceptsPanel.add(moConceptsGrid, BorderLayout.CENTER);
        
        jlStatus = new JLabel();
        jpCommandLeft.add(jlStatus);
        
        jbSave.setEnabled(false);
        jbCancel.setText(SUtilConsts.TXT_CLOSE);
        
        mbAllowLinkGreaterInvoices = miClient.getSession().getUser().hasPrivilege(SDataConstantsSys.PRV_PUR_LINK_INV_GREATER);
        
        maDocuments = new ArrayList<>();
        maDocumentsRejected = new ArrayList<>();
        moAdvancesMap = new HashMap<>();
        moPatternScaleTicketBol = SMassAccountUtils.createPatternForScaleTicketBol();
        moPatternScaleTicketRef = SMassAccountUtils.createPatternForScaleTicketRef();
        moPatternWarehouse = SMassAccountUtils.createPatternForWarehouse();
        
        moIconEdit = new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"));
        moIconSave = new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif"));
    }
    
    private boolean isDocAlreadyRecorded(final SImportedDocument document, boolean refreshDocumentsGrid) throws Exception {
        boolean isRecorded = document.isRecorded();
        
        if (!isRecorded) {
            int[] dpsKey = SImportedDocument.getDpsKeyByDocData(moSettings.PrepStatToGetDpsKeyByDocData, document.BizPartnerId, SLibTimeUtils.convertToDateOnly(document.Date), document.NumberSeries, document.Number, document.Total, document.CurrencyId);

            if (dpsKey != null) {
                isRecorded = true;

                String dpsNumber = SThinDps.readDpsNumber(dpsKey, miClient.getSession().getStatement());

                if (miClient.showMsgBoxConfirm("Se encontró la factura " + SSwapConsts.SIIE + " '" + dpsNumber + "' de " + document.BizPartner + ".\n"
                        + "¿Desea vincularla a esta factura autorizada?") == JOptionPane.YES_OPTION) {
                    if (document.link(miClient.getSession(), moSettings.SyncUrlDownload, dpsKey, SImportedDocument.MATCH_PAY_TP_CONF_DIFF, false, false, false, false) && refreshDocumentsGrid) {
                        refreshDocumentsGrid();
                    }
                }
            }
        }
        
        return isRecorded;
    }
    
    /**
     * Update filters.
     * NOTICE: It must be invoked ONLY within the scope of flag mbDocumentsBeingSet.
     */
    @SuppressWarnings("unchecked")
    private void updateFilters() {
        bgFilter.clearSelection();
        
        moRadFilterPartner.setEnabled(false);
        moRadFilterItem.setEnabled(false);
        moRadFilterAll.setEnabled(false);
        
        moKeyFilterPartner.setEnabled(false);
        moKeyFilterItem.setEnabled(false);
        
        moKeyFilterPartner.removeAllItems();
        moKeyFilterItem.removeAllItems();
        
        if (!maDocuments.isEmpty()) {
            HashSet<String> partnersSet = new HashSet<>();
            HashSet<String> itemsSet = new HashSet<>();

            for (SMassAccountDocument document : maDocuments) {
                if (document.isCfdiInvoice()) {
                    partnersSet.add(document.EmisorDescripByName);

                    switch (moSettings.ModeCase) {
                        case SImportedDocument.DOC_CASE_RAW_MAT_FREIGHT:
                            if (!document.CartaPorteBienesTranspsDescripByCode.isEmpty()) {
                                itemsSet.add(document.CartaPorteBienesTranspsDescripByCode);
                            }
                            break;
                        case SImportedDocument.DOC_CASE_RAW_MAT_PURCHASE:
                            if (!document.ComprobanteProdServDescripByCode.isEmpty()) {
                                itemsSet.add(document.ComprobanteProdServDescripByCode);
                            }
                            break;
                        default:
                            // nothing
                    }
                }
            }

            ArrayList<String> partners = new ArrayList<>(partnersSet);
            Collections.sort(partners);

            moKeyFilterPartner.addItem(new SGuiItem("- " + SUtilConsts.TXT_SELECT + " " + moKeyFilterPartner.getToolTipText() + " -"));
            for (String partner : partners) {
                String[] elements = partner.split(" - "); // name + " - " + ID
                moKeyFilterPartner.addItem(new SGuiItem(new int[] { SLibUtils.parseInt(elements[1]) }, elements[0]));
            }

            ArrayList<String> items = new ArrayList<>(itemsSet);
            Collections.sort(items);

            moKeyFilterItem.addItem(new SGuiItem("- " + SUtilConsts.TXT_SELECT + " " + moKeyFilterItem.getToolTipText() + " -"));
            for (String item : items) {
                String[] elements = item.split(" - "); // code + " - " + name
                moKeyFilterItem.addItem(new SGuiItem(new int[] { SLibUtils.parseInt(elements[0]) }, item));
            }

            bgFilter.setSelected(moRadFilterAll.getModel(), true);

            moRadFilterPartner.setEnabled(true);
            moRadFilterItem.setEnabled(true);
            moRadFilterAll.setEnabled(true);
        }
    }
    
    private void populateDocumentsGrid(final ArrayList<SMassAccountDocument> documents, final boolean focusDocumentsGrid) {
        Collections.sort(documents);
        
        moDocumentsGrid.populateGrid(new Vector<>(documents), this);
        moDocumentsGrid.getTable().getTableHeader().setReorderingAllowed(true);
        moDocumentsGrid.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moDocumentsGrid.setSelectedGridRow(0);
        
        if (focusDocumentsGrid) {
            moDocumentsGrid.getTable().requestFocusInWindow();
        }
        
        jtfDocsShown.setText(SLibUtils.DecimalFormatInteger.format(documents.size()));
        recountDocsProcessable();
        recountDocsToProcess();
        
        jlStatus.setText("Facturas autorizadas elegibles: " + SLibUtils.DecimalFormatInteger.format(maDocuments.size()) + "; mostradas: " + SLibUtils.DecimalFormatInteger.format(documents.size()));
    }
    
    private void reloadDocumentsGrid() {
        mbDocumentsBeingReloaded = true;
        
        updateFilters();
        populateDocumentsGrid(maDocuments, !maDocuments.isEmpty());
        
        mbDocumentsBeingReloaded = false;
    }
    
    private void refreshDocumentsGrid() {
        mbDocumentsBeingRefreshed = true;
        
        int index = moDocumentsGrid.getTable().getSelectedRow();
        moDocumentsGrid.renderGridRows();
        moDocumentsGrid.setSelectedGridRow(index);
        
        renderCurrentDoc();
        
        mbDocumentsBeingRefreshed = false;
    }
    
    private void retrieveAllAdvances(final ArrayList<Integer> bizPartnerIds) throws Exception {
        for (Integer bizPartnerId : bizPartnerIds) {
            if (moAdvancesMap.get(bizPartnerId) == null) {
                ArrayList<SFinUtilities.Balance> advancesList = new ArrayList<>();
                SFinUtilities.Balance[] balances = SFinUtilities.getBizPartnerBalances((SClientInterface) miClient, bizPartnerId, SDataConstantsSys.BPSS_CT_BP_SUP, miClient.getSession().getSystemDate());

                for (SFinUtilities.Balance balance : balances) {
                    if (balance.CurAdvance != 0 || balance.LocAdvance != 0) {
                        advancesList.add(balance);
                    }
                }

                SFinUtilities.Balance[] advances = advancesList.toArray(new SFinUtilities.Balance[0]);
                moAdvancesMap.put(bizPartnerId, advances);
            }
        }
    }
    
    private void setSettings(final SDialogImportDocuments.Settings settings) {
        moSettings = settings;
        
        if (moSettings == null) {
            jtfUserName.setText("");
            jtfUserFuncSubAreas.setText("");
            
            bgSearchBy.clearSelection();

            moDatePeriodStart.resetField();
            moDatePeriodEnd.resetField();

            moCalWeekYear.resetField();
            moCalWeekStart.resetField();
            moCalWeekEnd.resetField();
            
            bgDocMode.clearSelection();
            
            moKeyDocModeType.resetField();
            moKeyDocModeCase.resetField();
        }
        else {
            jtfUserName.setText(moSettings.UserName);
            jtfUserName.setCaretPosition(0);

            jtfUserFuncSubAreas.setText(moSettings.UserFuncSubAreas);
            jtfUserFuncSubAreas.setCaretPosition(0);
            jtfUserFuncSubAreas.setToolTipText("Subáreas funcionales: " + moSettings.UserFuncSubAreas);

            switch (moSettings.SearchBy) {
                case SDialogImportDocuments.SEARCH_BY_PERIOD:
                    moRadSearchByPeriod.setSelected(true);

                    moDatePeriodStart.setValue(moSettings.PeriodStart);
                    moDatePeriodEnd.setValue(moSettings.PeriodEnd);

                    moCalWeekYear.resetField();
                    moCalWeekStart.resetField();
                    moCalWeekEnd.resetField();
                    break;

                case SDialogImportDocuments.SEARCH_BY_WEEK:
                    moRadSearchByPeriod.setSelected(true);

                    moDatePeriodStart.resetField();
                    moDatePeriodEnd.resetField();

                    moCalWeekYear.setValue(moSettings.WeekYear);
                    moCalWeekStart.setValue(moSettings.WeekStart);
                    moCalWeekEnd.setValue(moSettings.WeekEnd);
                    break;

                default:
                    bgSearchBy.clearSelection();

                    moDatePeriodStart.resetField();
                    moDatePeriodEnd.resetField();

                    moCalWeekYear.resetField();
                    moCalWeekStart.resetField();
                    moCalWeekEnd.resetField();
            }

            bgDocMode.setSelected(moRadDocModeCase.getModel(), true);
            
            moKeyDocModeType.resetField(); // mode type preserved only for consistency
            moKeyDocModeCase.setValue(new int[] { moSettings.ModeCase });
        }
    }
    
    private void setImportedDocuments(final ArrayList<SImportedDocument> documents, final HashMap<Integer, SFinUtilities.Balance[]> advances) {
        // set documents:
        
        maDocuments.clear();
        maDocumentsRejected.clear();
        
        if (documents != null) {
            try {
                for (SImportedDocument document : documents) {
                    maDocuments.add(new SMassAccountDocument(document, this));
                }
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
        
        // set advances:
        
        moAdvancesMap.clear();
        
        if (advances != null) {
            moAdvancesMap.putAll(advances);
            
            try {
                HashSet<Integer> bizPartnerIds = new HashSet<>();

                for (SMassAccountDocument document : maDocuments) {
                    bizPartnerIds.add(document.ImportedDocument.BizPartnerId);
                }

                retrieveAllAdvances(new ArrayList<>(bizPartnerIds));
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
        
        // make the documents visible:
        
        reloadDocumentsGrid();
    }
    
    private void enableControlsForShowingDocs(final boolean enable) {
        moRadFilterPartner.setEnabled(enable);
        moRadFilterItem.setEnabled(enable);
        moRadFilterAll.setEnabled(enable);
        
        moKeyFilterPartner.setEnabled(enable && moRadFilterPartner.isSelected());
        moKeyFilterItem.setEnabled(enable && moRadFilterItem.isSelected());
        
        jbSelectAllDocs.setEnabled(enable);
        jbDeselectAllDocs.setEnabled(enable);
        
        moDocumentsGrid.getTable().setEnabled(enable);
        moConceptsGrid.getTable().setEnabled(enable);
    }
    
    private void enableControlsForActionsOnDocsAndConcepts(final boolean enable) {
        SGridRow row = moDocumentsGrid.getSelectedGridRow();
        
        jbRejectInvoice.setEnabled(enable && row != null);
        jbViewInvoicePdf.setEnabled(enable && row != null);
        jbViewOrder.setEnabled(enable && row != null);
        
        moBoolReqPayRequire.setEnabled(enable && row != null);
        jbEditAndSaveReqPayAmount.setEnabled(enable && row != null);
        jbCancelEditReqPayAmount.setEnabled(false); // always keep disabled in this context
        jbChangeReqPayRequiredDate.setEnabled(enable && row != null);
        
        jbPickReqPaysNewRequiredDate.setEnabled(enable);
        jbSetReqPaysNewRequiredDate.setEnabled(enable);
        jbRecordDocs.setEnabled(enable);
        
        SMassAccountDocument document = row != null ? (SMassAccountDocument) row : null;
        
        jbBolViewScaleTicket.setEnabled(enable && document != null && document.isCfdiInvoiceAndBol());
        jbAccShowParsingErrorOrWarning.setEnabled(enable && document != null && (document.ParsingError || document.ParsingWarningType != 0));
    }
    
    private void enableEditingReqPayAmount(final boolean enable) {
        jbEditAndSaveReqPayAmount.setIcon(enable ? moIconSave : moIconEdit);
        jbEditAndSaveReqPayAmount.setEnabled(true);
        jbCancelEditReqPayAmount.setEnabled(enable);
        
        moDecReqPayAmount.setEditable(enable);
        
        if (enable) {
            moDecReqPayAmount.requestFocusInWindow();
        }
        else {
            moDecReqPayAmount.resetField();
        }
    }

    private void initProgress() {
        jProgressBar.setValue(0);
        jProgressBar.setStringPainted(false);
        jProgressBar.setIndeterminate(true);
    }
    
    private void startProgress() {
        jProgressBar.setValue(0);
        jProgressBar.setStringPainted(true);
        jProgressBar.setIndeterminate(false);
    }
    
    private void clearProgress() {
        jProgressBar.setValue(0);
        jProgressBar.setStringPainted(false);
        jProgressBar.setIndeterminate(false);
    }
    
    private void evaluateClosingDialog() {
        if (maDocuments.isEmpty()) {
            miClient.showMsgBoxInformation("Ya no hay más facturas autorizadas por procesar. Se cerrará este diálogo.");
            actionCancel();
        }
    }
    
    private void backgroundProcessForRecordingDocs(int docsToRecord, final SProgressCallback callback) {
        try {
            mbDocumentsBeingProcessed = true;

            enableControlsForShowingDocs(false);
            enableControlsForActionsOnDocsAndConcepts(false);
            jbCancel.setEnabled(false);

            int countProcessed = 0;
            int docsAlreadyRecorded = 0;
            int docsProcessed = 0;
            int excpsOccured = 0;
            int docsRecorded = 0;
            int paysRequested = 0;
            ArrayList<SMassAccountDocument> docsRecordedAndLinked = new ArrayList<>();

            startProgress();

            SDataBizPartner company = ((SClientInterface) miClient).getSessionXXX().getCompany().getDbmsDataCompany();

            for (SGridRow row : moDocumentsGrid.getModel().getGridRows()) {
                SMassAccountDocument document = (SMassAccountDocument) row;

                if (document.Record) {
                    callback.onProgress((int) ((++countProcessed / (double) docsToRecord) * 100));
                    
                    try {
                        if (document.ImportedDocument.isRecorded()) {
                            docsAlreadyRecorded++;
                            throw new Exception(SImportedDocument.EXC_DOC_ALREADY_RECORDED_IN_ + document.ImportedDocument.ProcessedDps.composeRecord() + ".");
                        }
                        else {
                            if (isDocAlreadyRecorded(document.ImportedDocument, true)) {
                                docsAlreadyRecorded++;
                            }
                            else {
                                document.IconRecorded = SGridConsts.ICON_WAIT; // restore original icon
                                docsProcessed++;

                                // prepare accounting data:
                                
                                SDataBizPartner bizPartner = SMassAccountUtils.getBizPartner((SClientInterface) miClient, document.ImportedDocument.BizPartnerId);
                                SDataBizPartnerBranch bizPartnerBranch = bizPartner.getDbmsBizPartnerBranchHq();
                                int taxRegionId = bizPartnerBranch.getFkTaxRegionId_n() != 0 ? bizPartnerBranch.getFkTaxRegionId_n() : ((SClientInterface) miClient).getSessionXXX().getParamsCompany().getFkDefaultTaxRegionId_n();
                                int docNatureId = document.isCfdiInvoiceAndBol() ? document.GoodsCase.getDocNature() : document.InvoiceCase.getDocNature();
                                SDbFunctionalSubArea functionalSubArea = SMassAccountUtils.getFunctionalSubArea((SClientInterface) miClient, document.isCfdiInvoiceAndBol() ? document.GoodsCase.getFuncSubAreaBol() : document.InvoiceCase.getFuncSubArea());
                                
                                // create new invoice:

                                ArrayList<SDataDpsEntry> dpsEntries = document.createDpsEntriesAndSetAccountSettings((SClientInterface) miClient, taxRegionId);

                                SDataDps dps = SImportUtils.createDps((SClientInterface) miClient, SDataConstantsSys.TRNU_TP_DPS_PUR_INV,
                                        document.Comprobante, document.ImportedDocument.AuxFiles[SImportUtils.CFDI_XML_IDX], document.ImportedDocument.AuxFiles[SImportUtils.CFDI_PDF_IDX],
                                        company, bizPartner, dpsEntries, null, docNatureId, functionalSubArea.getFkFunctionalAreaId(), functionalSubArea.getPkFunctionalSubAreaId());

                                document.IconRecorded = SGridConsts.ICON_XML_ANNUL; // assume that processing can fail
                                
                                // save new invoice and link it to current document:
                                
                                if (SImportUtils.saveRegistry((SClientInterface) miClient, dps) == SLibConstants.DB_ACTION_SAVE_OK) {
                                    document.IconRecorded = SGridConsts.ICON_XML_ISSU; // new invoice saved!
                                    docsRecorded++;

                                    if (document.ImportedDocument.link(miClient.getSession(), moSettings.SyncUrlDownload, (int[]) dps.getPrimaryKey(), SImportedDocument.MATCH_PAY_TP_CONF_DIFF, false, false, true, true)) {
                                        document.IconRecorded = SGridConsts.ICON_OK; // document linked to new invoice!
                                        docsRecordedAndLinked.add(document);

                                        if (document.ImportedDocument.isPaymentRequested()) {
                                            paysRequested++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        excpsOccured++;
                        SLibUtils.printException(this, e);
                    }
                }
            }

            callback.onProgress(100); // assure to show 100%
            
            // inform about the processing:
            
            String message;

            if (docsToRecord == 1) {
                message = "La única factura seleccionada para procesar:";

                if (docsAlreadyRecorded > 0) {
                    message += "\n+ Ya estaba contabilizada.";
                }
                if (docsProcessed > 0) {
                    message += "\n+ Se procesó.";
                }
                if (excpsOccured > 0) {
                    message += "\n+ Se generó una excepción.";
                }
                
                if (docsRecorded == 0) {
                    message += "\n+ No se contabilizó.";
                }
                else {
                    message += "\n+ Se contabilizó.";
                }
                if (docsRecordedAndLinked.isEmpty()) {
                    message += "\n+ No se vinculó a la factura " + SSwapConsts.SIIE + ".";
                }
                else {
                    message += "\n+ Se vinculó a la factura " + SSwapConsts.SIIE + ".";
                }
                if (paysRequested == 0) {
                    message += "\n+ No se solicitó el pago.";
                }
                else {
                    message += "\n+ Se solicitó el pago.";
                }
            }
            else {
                message = "De las " + SLibUtils.DecimalFormatInteger.format(docsToRecord) + " facturas seleccionadas para procesar:";

                if (docsAlreadyRecorded > 0) {
                    message += "\n+ " + (docsAlreadyRecorded == 1 ? "Ya estaba contabilizada una" : "Ya estaban contabilizadas " + SLibUtils.DecimalFormatInteger.format(docsAlreadyRecorded)) + ".";
                }
                if (docsProcessed > 0) {
                    message += "\n+ " + (docsProcessed == 1 ? "Solo se procesó una" : "Se procesaron " + SLibUtils.DecimalFormatInteger.format(docsProcessed)) + ".";
                }
                if (excpsOccured > 0) {
                    message += "\n+ " + (excpsOccured == 1 ? "Se generó una excepción" : "Se generaron " + SLibUtils.DecimalFormatInteger.format(excpsOccured) + " excepciones") + ".";
                }
                
                if (docsRecorded == 0) {
                    message += "\n+ " + "No se contabilizó ninguna.";
                }
                else {
                    message += "\n+ " + (docsRecorded == 1 ? "Solo se contabilizó una" : "Se contabilizaron " + SLibUtils.DecimalFormatInteger.format(docsRecorded)) + ".";
                }
                if (docsRecordedAndLinked.isEmpty()) {
                    message += "\n+ " + "No se vinculó a ninguna factura " + SSwapConsts.SIIE + ".";
                }
                else {
                    message += "\n+ " + (docsRecordedAndLinked.size() == 1 ? "Solo se vinculó a una factura" : "Se vincularon a " + SLibUtils.DecimalFormatInteger.format(docsRecordedAndLinked.size()) + " facturas") + " " + SSwapConsts.SIIE + ".";
                }
                if (paysRequested == 0) {
                    message += "\n+ " + "No se solicitó ningún pago.";
                }
                else {
                    message += "\n+ " + (paysRequested == 1 ? "Solo se solicitó un pago" : "Se solicitaron " + SLibUtils.DecimalFormatInteger.format(paysRequested) + " pagos") + ".";
                }
            }

            miClient.showMsgBoxInformation(message);

            if (docsRecordedAndLinked.size() > 0) {
                mnDocsRecordedAndLinked += docsRecordedAndLinked.size();
                jtfDocsRecordedAndLinked.setText(SLibUtils.DecimalFormatInteger.format(mnDocsRecordedAndLinked));

                if (paysRequested > 0) {
                    mbExportPaymentRequests = true;
                }

                maDocuments.removeAll(docsRecordedAndLinked);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            reloadDocumentsGrid();
            
            enableControlsForShowingDocs(true);
            enableControlsForActionsOnDocsAndConcepts(true);
            jbCancel.setEnabled(true);
            
            mbDocumentsBeingProcessed = false;
        }
    }
    
    private void actionPerformedSelectAllDocs() {
        for (SGridRow row : moDocumentsGrid.getModel().getGridRows()) {
            SMassAccountDocument document = (SMassAccountDocument) row;
            
            if (document.isRecordable()) {
                document.Record = true;
            }
        }
        
        refreshDocumentsGrid();
        recountDocsToProcess();
    }
    
    private void actionPerformedDeselectAllDocs() {
        for (SGridRow row : moDocumentsGrid.getModel().getGridRows()) {
            SMassAccountDocument document = (SMassAccountDocument) row;
            
            document.Record = false;
        }
        
        refreshDocumentsGrid();
        recountDocsToProcess();
    }
    
    private void actionPerformedRejectInvoice() {
        boolean rejected = false;
        
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SMassAccountDocument mad = (SMassAccountDocument) row;
                SImportedDocument document = mad.ImportedDocument; // convenience variable
                
                if (document.isRecorded()) {
                    throw new Exception(SImportedDocument.EXC_DOC_ALREADY_RECORDED_IN_ + document.ProcessedDps.composeRecord() + ".");
                }
                else {
                    String confirm = "Será posible rechazar la factura autorizada '" + document.getFolio() + "' de " + document.BizPartner + ", solamente si aún no está contabilizada.\n"
                            + "IMPORTANTE: ¡Considere que el rechazo de una factura autorizada es una acción que no se puede revertir!\n"
                            + SGuiConsts.MSG_CNF_CONT;
                    
                    if (miClient.showMsgBoxConfirm(confirm) == JOptionPane.YES_OPTION) {
                        if (!isDocAlreadyRecorded(document, true)) {
                            SServicesUtils.RejectData rejectData = SServicesUtils.askForRejectData(miClient.getSession());
                            
                            if (rejectData != null) {
                                confirm = "Se rechazará la factura autorizada '" + document.getFolio() + "' de " + document.BizPartner + ",\n"
                                        + "por el usuario: " + rejectData.User + ",\n"
                                        + "con los siguientes comentarios:\n"
                                        + "\"" + rejectData.Notes + "\"\n"
                                        + SGuiConsts.MSG_CNF_CONT;

                                if (miClient.showMsgBoxConfirm(confirm) == JOptionPane.YES_OPTION) {
                                    SDataRejectResource data = new SDataRejectResource();

                                    data.id_external_system = SSwapConsts.SIIE_EXT_SYS_ID;
                                    data.id_company = miClient.getSession().getConfigCompany().getCompanyId();
                                    data.id_resource_type = SSwapConsts.RESOURCE_TYPE_PUR_INVOICE;
                                    data.external_resource_id = "" + document.ExternalDocumentId;
                                    data.external_resource_uuid = document.ExternalDocumentUuid; // UUID (not required in SWAP Services!)
                                    data.id_actor_type = SExportDataAuthActor.ACTOR_TYPE_USER;
                                    data.external_user_id = rejectData.UserId;
                                    data.notes = rejectData.Notes;

                                    SServicesUtils.requestRejectResource(miClient.getSession(), data);
                                    
                                    rejected = true;

                                    int index = moDocumentsGrid.getTable().getSelectedRow();

                                    maDocumentsRejected.add(mad);
                                    maDocuments.remove(mad);
                                    reloadDocumentsGrid();

                                    moDocumentsGrid.setSelectedGridRow(index < moDocumentsGrid.getTable().getRowCount() ? index : --index);

                                    miClient.showMsgBoxInformation("La factura originalmente autorizada '" + document.getFolio() + "' de " + document.BizPartner + " acaba de ser rechazada.");
                                }
                            }
                            else {
                                miClient.showMsgBoxWarning("Para proceder es necesario especificar los comentarios de rechazo.");
                            }
                        }
                        else {
                            miClient.showMsgBoxWarning("La factura autorizada '" + document.getFolio() + "' de " + document.BizPartner + " no se puede rechazar porque ya está contabilizada.");
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            if (rejected) {
                evaluateClosingDialog();
            }
        }
    }
    
    private void actionPerformedViewInvoicePdf() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SMassAccountDocument document = (SMassAccountDocument) row;
                
                if (moDialogPdfViewer == null) {
                    moDialogPdfViewer = new SDialogPdfViewer(miClient, true);
                }
                
                if (document.ImportedDocument.isRecorded()) {
                    // if document is recorded, prefer PDF stored in ERP:
                    SViewDps.showDocPdf((SClientInterface) miClient, document.ImportedDocument.ProcessedDps.getDpsKey(), moDialogPdfViewer);
                }
                else {
                    // retrieve PDF from SWAP Services:
                    File pdf = document.ImportedDocument.retrievePdf(miClient.getSession(), moSettings.SyncUrlDownload);
                    
                    if (pdf != null) {
                        moDialogPdfViewer.setPdf(new SDocumentInfo(document.ImportedDocument), pdf);
                        moDialogPdfViewer.setVisible(true);
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedViewOrder() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SMassAccountDocument document = (SMassAccountDocument) row;
                int[] orderKey = document.ImportedDocument.getFirstReferenceKey(miClient, SSwapConsts.TXN_REF_TYPE_ORDER);

                if (orderKey == null) {
                    throw new Exception("La factura autorizada '" + document.ImportedDocument.getFolio() + "' no está relacionada con ningún pedido.");
                }
                else {
                    ((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_PUR).setFormComplement(SDataConstantsSys.TRNU_TP_DPS_PUR_ORD);
                    ((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_PUR).showForm(SDataConstants.TRNX_DPS_RO, orderKey);
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedViewAdvances() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SMassAccountDocument mad = (SMassAccountDocument) row;
                SImportedDocument document = mad.ImportedDocument;
                
                if (document.hasAdvances()) {
                    miClient.showMsgBoxInformation(document.getAdvancesAsString(miClient));
                }
                else {
                    miClient.showMsgBoxWarning(SImportedDocument.EXC_ADV_NO_ADVANCES);
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedEditAndSaveReqPayAmount() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SMassAccountDocument document = (SMassAccountDocument) row;
                
                if (document.ImportedDocument.isPaymentRequestDataAvailable()) {
                    if (!moDecReqPayAmount.isEditable()) {
                        // edit amount:
                        
                        enableControlsForShowingDocs(false);
                        enableControlsForActionsOnDocsAndConcepts(false);
                        enableEditingReqPayAmount(true);
                    }
                    else {
                        // save amount:

                        if (moDecReqPayAmount.getValue() <= 0) {
                            miClient.showMsgBoxWarning(SGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + moDecReqPayAmount.getFieldName() + "'" + SGuiConsts.ERR_MSG_FIELD_VAL_GREAT + "cero.");
                            moDecReqPayAmount.requestFocusInWindow();
                        }
                        else if (moDecReqPayAmount.getValue() > document.ImportedDocument.Total) {
                            miClient.showMsgBoxWarning(SGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + moDecReqPayAmount.getFieldName() + "'" + SGuiConsts.ERR_MSG_FIELD_VAL_LESS_EQUAL + "$ " + SLibUtils.getDecimalFormatAmount().format(document.ImportedDocument.Total) + ".");
                            moDecReqPayAmount.requestFocusInWindow();
                        }
                        else {
                            // save only if effective date is available:

                            if (document.ImportedDocument.getRequiredPaymentDateEffective() == null) {
                                actionPerformedChangeReqPayRequiredDate();
                            }

                            if (document.ImportedDocument.getRequiredPaymentDateEffective() != null) {
                                document.ImportedDocument.RequiredPaymentDefinition = SSwapConsts.PAY_DEF_BY_AMT_MAN;
                                document.ImportedDocument.RequiredPaymentAmountNew = moDecReqPayAmount.getValue();

                                refreshDocumentsGrid();

                                actionPerformedCancelEditReqPayAmount(false);
                                
                                moDocumentsGrid.getTable().requestFocusInWindow();
                            }
                            else {
                                miClient.showMsgBoxWarning("No se puede cambiar el monto requerido de pago porque la factura autorizada '" + document.ImportedDocument.getFolio() + "' no tiene una fecha efectiva de pago.");
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedCancelEditReqPayAmount(final boolean focusDocumentsGrid) {
        enableControlsForShowingDocs(true);
        enableControlsForActionsOnDocsAndConcepts(true);
        enableEditingReqPayAmount(false);
        
        if (focusDocumentsGrid) {
            moDocumentsGrid.getTable().requestFocusInWindow();
        }
    }
    
    private void actionPerformedChangeReqPayRequiredDate() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SMassAccountDocument document = (SMassAccountDocument) row;
                
                if (document.ImportedDocument.changeRequiredPaymentDate(miClient.getSession())) {
                    refreshDocumentsGrid();
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedPickReqPaysNewRequiredDate() {
        mtNewRequiredDate = SDocumentUtils.pickDate(miClient.getSession(), mtNewRequiredDate);

        if (mtNewRequiredDate == null) {
            jtfReqPaysNewRequiredDate.setText("");
        }
        else if (mtNewRequiredDate.before(SLibTimeUtils.convertToDateOnly(miClient.getSession().getSystemDate()))) {
            mtNewRequiredDate = null;
            miClient.showMsgBoxWarning(SGuiConsts.ERR_MSG_FIELD_DATE_ + "'" + SGuiUtils.getLabelName(jlReqPaysNewRequiredDate) + "' no puede ser anterior al día de hoy, " + SLibUtils.DateFormatDate.format(miClient.getSession().getSystemDate()) + ".");
        }
        else {
            jtfReqPaysNewRequiredDate.setText(SLibUtils.GuiDateFormat.format(mtNewRequiredDate));
            jtfReqPaysNewRequiredDate.setCaretPosition(0);
        }
    }
    
    private void actionPerformedSetReqPaysNewRequiredDate() {
        int docsShown = moDocumentsGrid.getModel().getRowCount();
        String msgDateNotAsignable = "No se puede asignar la nueva fecha requerida de pago, porque ";
        
        if (docsShown == 0) {
            miClient.showMsgBoxWarning(msgDateNotAsignable + "no hay facturas que se estén siendo mostradas.");
        }
        else if (mtNewRequiredDate == null) {
            miClient.showMsgBoxWarning(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlReqPaysNewRequiredDate.getText()) + "'.");
            jbPickReqPaysNewRequiredDate.requestFocusInWindow();
        }
        else {
            int paysOutOfDate = 0;
            int paysToRequest = 0;
            
            for (SGridRow row : moDocumentsGrid.getModel().getGridRows()) {
                SMassAccountDocument document = (SMassAccountDocument) row;
                
                if (document.Record && document.ImportedDocument.RequirePayment) {
                    if (mtNewRequiredDate.before(SLibTimeUtils.convertToDateOnly(document.ImportedDocument.Date))) {
                        paysOutOfDate++;
                    }
                    else {
                        paysToRequest++;
                    }
                }
            }
            
            if (paysToRequest == 0) {
                miClient.showMsgBoxWarning(msgDateNotAsignable + "no hay facturas seleccionadas para ser procesadas y que sean solicitables de pago.");
            }
            else {
                boolean assign = true;
                
                if (paysOutOfDate > 0) {
                    assign = miClient.showMsgBoxConfirm("Hay " + (paysOutOfDate == 1 ? "una factura" : SLibUtils.DecimalFormatInteger.format(paysOutOfDate) + " facturas")
                            + " cuya fecha es anterior a la nueva fecha requerida de pago, " + SLibUtils.DateFormatDate.format(mtNewRequiredDate) + ".\n"
                            + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
                }
                
                if (assign) {
                    assign = miClient.showMsgBoxConfirm("¿Está seguro que desea asignar la nueva fecha requerida de pago, " + SLibUtils.DateFormatDate.format(mtNewRequiredDate) + ",\n"
                            + "a " + (paysToRequest == 1 ? "la única factura procesable y solicitable de pago" : "las " + SLibUtils.DecimalFormatInteger.format(paysToRequest) + " facturas procesables y solicitables de pago?")) == JOptionPane.YES_OPTION;
                    
                    if (assign) {
                        for (SGridRow row : moDocumentsGrid.getModel().getGridRows()) {
                            SMassAccountDocument document = (SMassAccountDocument) row;

                            if (document.Record && document.ImportedDocument.RequirePayment && !mtNewRequiredDate.before(SLibTimeUtils.convertToDateOnly(document.ImportedDocument.Date))) {
                                document.ImportedDocument.RequiredPaymentDateNew = mtNewRequiredDate;
                            }
                        }

                        refreshDocumentsGrid();
                    }
                }
            }
            
            mtNewRequiredDate = null;
            jtfReqPaysNewRequiredDate.setText("");
        }
    }
    
    private void actionPerformedRecordDocs() {
        if (!moRadFilterAll.isSelected()) {
            miClient.showMsgBoxWarning("Favor de seleccionar la opción '" + moRadFilterAll.getText() + "' para procesar los comprobantes.");
            moRadFilterAll.requestFocus();
        }
        else if (moDocumentsGrid.getModel().getRowCount() == 0) {
            miClient.showMsgBoxWarning("No hay comprobantes para procesar.");
        }
        else {
            int docsToRecord = 0;
            int paysRequestableWithAdvances = 0;
            HashSet<String> bizPartnersWithAdvances = new HashSet<>();
            
            for (SGridRow row : moDocumentsGrid.getModel().getGridRows()) {
                SMassAccountDocument document = (SMassAccountDocument) row;

                if (document.Record) {
                    docsToRecord++;
                    
                    if (document.ImportedDocument.isPaymentRequestable(false)) {
                        if (document.ImportedDocument.AuxAdvances == null) {
                            document.ImportedDocument.AuxAdvances = moAdvancesMap.get(document.ImportedDocument.BizPartnerId);
                        }
                        
                        if (document.ImportedDocument.hasAdvances()) {
                            paysRequestableWithAdvances++;
                            bizPartnersWithAdvances.add(document.ImportedDocument.BizPartner);
                        }
                    }
                }
            }
            
            if (docsToRecord == 0) {
                miClient.showMsgBoxWarning("No hay comprobantes seleccionados para procesar.");
            }
            else {
                // prepare to background processing:
                
                initProgress();
                
                String confirm = "¿Está seguro que desea procesar "
                        + (docsToRecord == 1 ? "la única factura seleccionada?" : "las " + SLibUtils.DecimalFormatInteger.format(docsToRecord) + " facturas seleccionadas?");
                
                if (paysRequestableWithAdvances > 0) {
                    confirm += "\n\nConsidere que hay " + (paysRequestableWithAdvances == 0 ? "una factura" : SLibUtils.DecimalFormatInteger.format(paysRequestableWithAdvances) + " facturas") + " "
                            + (bizPartnersWithAdvances.size() == 1 ? "cuyo proveedor tiene anticipos" : "cuyos proveedores tienen anticipos") + ", "
                            + "al corte del " + SLibUtils.DateFormatDate.format(miClient.getSession().getSystemDate()) + ":";
                    
                    ArrayList<String> bizPartners = new ArrayList<>(bizPartnersWithAdvances);
                    bizPartners.sort(null);
                    
                    for (String bizPartner : bizPartners) {
                        confirm += "\n+ " + bizPartner;
                    }
                    
                    confirm += "\n\n" + SGuiConsts.MSG_CNF_CONT;
                }
                
                if (miClient.showMsgBoxConfirm(confirm) == JOptionPane.YES_OPTION) {
                    // start of background processing...
                    
                    final int docs = docsToRecord;
                
                    SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {

                        @Override
                        protected Void doInBackground() throws Exception {
                            backgroundProcessForRecordingDocs(docs, progress -> {
                                publish(progress);
                            });
                            return null;
                        }

                        @Override
                        protected void process(List<Integer> chunks) {
                            int latest = chunks.get(chunks.size() - 1);
                            jProgressBar.setValue(latest); // runs on EDT
                        }

                        @Override
                        protected void done() {
                            clearProgress();
                            evaluateClosingDialog();
                        }
                    };

                    worker.execute();
                    
                    // ... end of background processing
                }
                else {
                    clearProgress(); // restore monitoring of background processing
                }
            }
            
            mtNewRequiredDate = null;
            jtfReqPaysNewRequiredDate.setText("");
        }
    }
    
    private void actionPerformedBolViewScaleTicket() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SMassAccountDocument document = (SMassAccountDocument) row;
                
                if (document.ScaleTicketBol.isEmpty()) {
                    miClient.showMsgBoxWarning("La factura autorizada '" + document.ImportedDocument.getFolio() + "' no tiene boleto.");
                }
                else {
                    if (moSomDatabase == null) {
                        String swapSomParamValue = SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SOM);
                        SSwapUtils.SomSettings somSettings = new SSwapUtils.SomSettings(swapSomParamValue);

                        if (somSettings.LinkUp) {
                            moSomDatabase = new SDbDatabase(SDbConsts.DBMS_MYSQL);
                            if (moSomDatabase.connect(somSettings.DbmsHost, 
                                                    somSettings.DbmsPort, 
                                                    somSettings.DbName, 
                                                    somSettings.DbmsUser, 
                                                    somSettings.DbmsPswd) != SDbConsts.CONNECTION_OK) {
                                miClient.showMsgBoxError(SDbConsts.ERR_MSG_DB_CONNECTION + "\n(" + SSwapConsts.SOM + ")");
                            }
                        }
                    }

                    if (moSomDatabase == null || !moSomDatabase.isConnected()) {
                        miClient.showMsgBoxWarning("No se puede mostrar el boleto '" + document.ScaleTicketBol + "'. No hay conexión a " + SSwapConsts.SOM + ".");
                    }
                    else {
                        int somTicketId = SExportDataSomUtils.retrieveTicketId(moSomDatabase.getConnection(), document.ScaleTicketBol);
                        
                        if (somTicketId == 0) {
                            miClient.showMsgBoxWarning("El boleto '" + document.ScaleTicketBol + "' no existe.");
                        }
                        else {
                            File pdf = SExportDataSomUtils.createTicketPdf(miClient.getSession(), moSomDatabase.getConnection(), somTicketId, false, false);

                            if (pdf != null) {
                                if (moDialogPdfViewer == null) {
                                    moDialogPdfViewer = new SDialogPdfViewer(miClient, true);
                                }

                                SDocument documentInfo = new SDocument() {

                                    @Override
                                    public String getFolio() {
                                        return document.ScaleTicketBol;
                                    }

                                    @Override
                                    public String getIssuer() {
                                        return msCompanyName;
                                    }
                                };

                                moDialogPdfViewer.setPdf(documentInfo, pdf);
                                moDialogPdfViewer.setVisible(true);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedAccShowParsingErrorOrWarning() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SMassAccountDocument document = (SMassAccountDocument) row;
                
                if (document.ParsingError) {
                    miClient.showMsgBoxError("Hay un problema con el comprobante '" + document.ImportedDocument.getFolio() + "':\n" + document.getParsingError());
                }
                else if (document.ParsingWarningType != 0) {
                    miClient.showMsgBoxWarning("Hay un inconveniente con el comprobante '" + document.ImportedDocument.getFolio() + "':\n" + document.getParsingWarning());
                }
                else {
                    miClient.showMsgBoxInformation("El comprobante '" + document.ImportedDocument.getFolio() + "' es correcto.");
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void renderReqPay(final SImportedDocument document) {
        if (document == null) {
            jtfReqPayAmount.setText("");
            jtfReqPayAmountPct.setText("");
            jtfReqPayRequiredDate.setText("");
        }
        else {
            if (!document.isPaymentRequestDataAvailable()) {
                jtfReqPayAmount.setText("");
                jtfReqPayAmountPct.setText("");
                jtfReqPayRequiredDate.setText("");
            }
            else {
                jtfReqPayAmount.setText(SLibUtils.getDecimalFormatAmount().format(document.getRequiredPaymentAmountEffective(null)) + " " + document.CurrencyCode);
                jtfReqPayAmountPct.setText(SLibUtils.DecimalFormatPercentage0D.format(document.getRequiredPaymentPct()));
                jtfReqPayRequiredDate.setText(SLibUtils.GuiDateFormat.format(document.getRequiredPaymentDateEffective()));
                
                jtfReqPayAmount.setCaretPosition(0);
                jtfReqPayAmountPct.setCaretPosition(0);
                jtfReqPayRequiredDate.setCaretPosition(0);
            }
        }
        
        moDecReqPayAmount.setEditable(false);
        moDecReqPayAmount.resetField();
    }
    
    private void renderAdvances(final SImportedDocument document) {
        if (document == null) {
            jbViewAdvances.setEnabled(false);
        }
        else {
            if (document.AuxAdvances == null) { // retrieve advances just only once!
                document.AuxAdvances = moAdvancesMap.get(document.BizPartnerId);
                
                if (document.AuxAdvances == null) {
                    try {
                        ArrayList<SFinUtilities.Balance> advancesList = new ArrayList<>();
                        SFinUtilities.Balance[] balances = SFinUtilities.getBizPartnerBalances((SClientInterface) miClient, document.BizPartnerId, SDataConstantsSys.BPSS_CT_BP_SUP, miClient.getSession().getSystemDate());

                        for (SFinUtilities.Balance balance : balances) {
                            if (balance.CurAdvance != 0 || balance.LocAdvance != 0) {
                                advancesList.add(balance);
                            }
                        }

                        SFinUtilities.Balance[] advances = advancesList.toArray(new SFinUtilities.Balance[0]);
                        moAdvancesMap.put(document.BizPartnerId, advances);
                        document.AuxAdvances = advances;
                    }
                    catch (Exception e) {
                        SLibUtils.printException(this, e);
                    }
                }
            }

            jbViewAdvances.setEnabled(document.hasAdvances());
        }
    }
    
    private void renderBol(final SMassAccountDocument document) {
        if (document == null || !document.isCfdiInvoiceAndBol()) {
            jtfBolSrcAddress.setText("");
            jtfBolSrcAddress.setToolTipText(null);
            jtfBolSrcDistrict.setText("");
            jtfBolSrcZipCode.setText("");
            jtfBolSrcLocality.setText("");
            jtfBolSrcCounty.setText("");
            jtfBolSrcState.setText("");
            jtfBolSrcCountry.setText("");
            
            jtfBolDesAddress.setText("");
            jtfBolDesAddress.setToolTipText(null);
            jtfBolDesDistrict.setText("");
            jtfBolDesZipCode.setText("");
            jtfBolDesLocality.setText("");
            jtfBolDesCounty.setText("");
            jtfBolDesState.setText("");
            jtfBolDesCountry.setText("");
            
            jbBolViewScaleTicket.setEnabled(false);
            jtfBolScaleTicket.setText("");
            jtfBolDistanceKm.setText("");
            jtfBolGoodWeightKg.setText("");
            jtfBolGoodCode.setText("");
            jtfBolGoodDescrip.setText("");
            jtfBolGoodUnitCode.setText("");
            jtfBolGoodUnitDescrip.setText("");
        }
        else {
            cfd.ver4.ccp31.DElementDomicilio domicilioOrigen = document.CartaPorte.getEltUbicaciones().getEltUbicaciones(DCfdi40Catalogs.CcpUbicaciónOrigen).get(0).getEltDomicilio();
            
            String srcAddress = !domicilioOrigen.composeAddress().isEmpty() ? domicilioOrigen.composeAddress() : ND;
            jtfBolSrcAddress.setText(srcAddress);
            jtfBolSrcAddress.setToolTipText("Calle y no. origen: " + srcAddress);
            jtfBolSrcDistrict.setText(domicilioOrigen.getAttColonia().getString());
            jtfBolSrcZipCode.setText(domicilioOrigen.getAttCodigoPostal().getString());
            jtfBolSrcLocality.setText(domicilioOrigen.getAttLocalidad().getString());
            jtfBolSrcCounty.setText(domicilioOrigen.getAttMunicipio().getString());
            jtfBolSrcState.setText(domicilioOrigen.getAttEstado().getString());
            jtfBolSrcCountry.setText(domicilioOrigen.getAttPais().getString());
            
            cfd.ver4.ccp31.DElementDomicilio domicilioDestino = document.CartaPorte.getEltUbicaciones().getEltUbicaciones(DCfdi40Catalogs.CcpUbicaciónDestino).get(0).getEltDomicilio();
            
            String desAddress = !domicilioDestino.composeAddress().isEmpty() ? domicilioDestino.composeAddress() : ND;
            jtfBolDesAddress.setText(desAddress);
            jtfBolDesAddress.setToolTipText("Calle y no. destino: " + desAddress);
            jtfBolDesDistrict.setText(domicilioDestino.getAttColonia().getString());
            jtfBolDesZipCode.setText(domicilioDestino.getAttCodigoPostal().getString());
            jtfBolDesLocality.setText(domicilioDestino.getAttLocalidad().getString());
            jtfBolDesCounty.setText(domicilioDestino.getAttMunicipio().getString());
            jtfBolDesState.setText(domicilioDestino.getAttEstado().getString());
            jtfBolDesCountry.setText(domicilioDestino.getAttPais().getString());
            
            cfd.ver4.ccp31.DElementMercancia mercancia = document.CartaPorte.getEltMercancias().getEltMercancias().get(0);
            String unitCode = mercancia.getAttClaveUnidad().getString();
            
            if (unitCode.matches("\\d+")) { // is only digits?
                unitCode = "\"" + unitCode + "\""; // surround by quotation marks for preventing confusion of being a quantity instead of a code
            }
            
            jbBolViewScaleTicket.setEnabled(!document.ScaleTicketBol.isEmpty());
            jtfBolScaleTicket.setText(document.ScaleTicketBol);
            jtfBolDistanceKm.setText(SLibUtils.DecimalFormatValue1D.format(document.CartaPorte.getAttTotalDistRec().getDouble()));
            jtfBolGoodWeightKg.setText(SLibUtils.DecimalFormatValue1D.format(mercancia.getAttPesoEnKg().getDouble()));
            jtfBolGoodCode.setText(mercancia.getAttBienesTransp().getString());
            jtfBolGoodDescrip.setText(mercancia.getAttDescripcion().getString());
            jtfBolGoodUnitCode.setText(unitCode);
            jtfBolGoodUnitDescrip.setText(mercancia.getAttUnidad().getString());
            
            jtfBolSrcAddress.setCaretPosition(0);
            jtfBolSrcDistrict.setCaretPosition(0);
            jtfBolSrcZipCode.setCaretPosition(0);
            jtfBolSrcLocality.setCaretPosition(0);
            jtfBolSrcCounty.setCaretPosition(0);
            jtfBolSrcState.setCaretPosition(0);
            jtfBolSrcCountry.setCaretPosition(0);
            
            jtfBolDesAddress.setCaretPosition(0);
            jtfBolDesDistrict.setCaretPosition(0);
            jtfBolDesZipCode.setCaretPosition(0);
            jtfBolDesLocality.setCaretPosition(0);
            jtfBolDesCounty.setCaretPosition(0);
            jtfBolDesState.setCaretPosition(0);
            jtfBolDesCountry.setCaretPosition(0);
            
            jtfBolScaleTicket.setCaretPosition(0);
            jtfBolDistanceKm.setCaretPosition(0);
            jtfBolGoodWeightKg.setCaretPosition(0);
            jtfBolGoodCode.setCaretPosition(0);
            jtfBolGoodDescrip.setCaretPosition(0);
            jtfBolGoodUnitCode.setCaretPosition(0);
            jtfBolGoodUnitDescrip.setCaretPosition(0);
        }
    }
    
    private void renderAccountSettings(final SMassAccountDocument document) {
        if (document == null || document.AccountSettingsUser == null || document.AccountSettingsSystem == null) {
            jtfAccItem.setText("");
            jtfAccItemAux.setText("");
            jtfAccUnits.setText("");
            jtfAccUnit.setText("");
            jtfAccAccount.setText("");
            jtfAccCostCenter.setText("");
        }
        else {
            SDataItem item = null;
            SDataItem itemAux = null;
            SDataUnit unit = null;
            
            try {
                item = SMassAccountUtils.getErpItem((SClientInterface) miClient, document.AccountSettingsUser.ItemId);
                itemAux = document.isCfdiInvoiceAndBol() ? SMassAccountUtils.getErpItem((SClientInterface) miClient, document.AccountSettingsUser.ItemAuxId) : null;
                unit = SMassAccountUtils.getErpUnit((SClientInterface) miClient, document.AccountSettingsUser.UnitId);
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
            
            jtfAccItem.setText(item != null ? item.getItem() : "");
            jtfAccItemAux.setText(itemAux != null ? itemAux.getItem() : "");
            jtfAccUnits.setText(SLibUtils.getDecimalFormatQuantity().format(document.Units));
            jtfAccUnit.setText(unit != null ? unit.getSymbol() : "");
            jtfAccAccount.setText(document.AccountSettingsUser.AccountCode);
            jtfAccCostCenter.setText(document.AccountSettingsUser.CostCenterCode);
            
            jtfAccItem.setCaretPosition(0);
            jtfAccItemAux.setCaretPosition(0);
            jtfAccUnits.setCaretPosition(0);
            jtfAccUnit.setCaretPosition(0);
            jtfAccAccount.setCaretPosition(0);
            jtfAccCostCenter.setCaretPosition(0);
        }
    }
    
    private void renderCurrentDoc() {
        mbDocumentsBeingRendered = true;
        
        SGridRow row = moDocumentsGrid.getSelectedGridRow();
        
        if (row == null) {
            jbRejectInvoice.setEnabled(false);
            jbViewInvoicePdf.setEnabled(false);
            jbViewOrder.setEnabled(false);
            
            jtfInvoice.setText("");
            jtfAccountCase.setText("");
            
            moBoolReqPayRequire.setEnabled(false);
            moBoolReqPayRequire.resetField();
            itemStateChangedReqPayRequire();
            renderReqPay(null);
            renderAdvances(null);
            
            jbAccShowParsingErrorOrWarning.setEnabled(false);
            jbAccShowParsingErrorOrWarning.setIcon(SGridCellRendererIcon.moIconDoc);
            
            renderBol(null);
            renderAccountSettings(null);
            
            moConceptsGrid.populateGrid(new Vector<>());
            moConceptsGrid.getTable().getTableHeader().setReorderingAllowed(true);
        }
        else {
            SMassAccountDocument document = (SMassAccountDocument) row;
            
            jbRejectInvoice.setEnabled(true);
            jbViewInvoicePdf.setEnabled(true);
            jbViewOrder.setEnabled(true);
            
            jtfInvoice.setText(document.ImportedDocument.getFolio());
            jtfInvoice.setCaretPosition(0);
            jtfAccountCase.setText(document.getAccountCase());
            jtfAccountCase.setCaretPosition(0);
            
            moBoolReqPayRequire.setEnabled(document.ImportedDocument.isPaymentRequestDataAvailable());
            moBoolReqPayRequire.setValue(document.ImportedDocument.RequirePayment);
            itemStateChangedReqPayRequire();
            renderReqPay(document.ImportedDocument);
            renderAdvances(document.ImportedDocument);
            
            if (document.ParsingError) {
                jbAccShowParsingErrorOrWarning.setEnabled(true);
                jbAccShowParsingErrorOrWarning.setIcon(SGridCellRendererIcon.moIconAnnul);
            }
            else if (document.ParsingWarningType != 0) {
                jbAccShowParsingErrorOrWarning.setEnabled(true);
                jbAccShowParsingErrorOrWarning.setIcon(SGridCellRendererIcon.moIconWarn);
            }
            else {
                jbAccShowParsingErrorOrWarning.setEnabled(false);
                jbAccShowParsingErrorOrWarning.setIcon(SGridCellRendererIcon.moIconDoc);
            }
            
            renderBol(document);
            renderAccountSettings(document);
            
            moConceptsGrid.populateGrid(new Vector<>(document.Conceptos));
            moConceptsGrid.getTable().getTableHeader().setReorderingAllowed(true);
        }
        
        mbDocumentsBeingRendered = false;
    }
    
    private void itemStateChangedReqPayRequire() {
        boolean enable = false;
        boolean require = moBoolReqPayRequire.isSelected(); // convenience variable
        
        if (mbDocumentsBeingRendered) {
            // rendering current document:
            enable = true;
        }
        else {
            // editing current document:
            try {
                SGridRow row = moDocumentsGrid.getSelectedGridRow();

                if (row == null) {
                    throw new Exception(SGridConsts.MSG_SELECT_ROW);
                }
                else {
                    SMassAccountDocument document = (SMassAccountDocument) row;
                    document.ImportedDocument.RequirePayment = require;

                    refreshDocumentsGrid();
                    recountDocsToProcess();

                    enable = true;
                }
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
        
        if (enable) {
            jbEditAndSaveReqPayAmount.setEnabled(require);
            jbCancelEditReqPayAmount.setEnabled(false);
            jbChangeReqPayRequiredDate.setEnabled(require);
        }
    }
    
    private void itemStateChangedFilter() {
        mbDocumentsBeingFiltered = true;
        
        moKeyFilterPartner.resetField();
        moKeyFilterItem.resetField();
        
        moKeyFilterPartner.setEnabled(false);
        moKeyFilterItem.setEnabled(false);
        
        if (moRadFilterPartner.isSelected()) {
            moKeyFilterPartner.setEnabled(true);
        }
        else if (moRadFilterItem.isSelected()) {
            moKeyFilterItem.setEnabled(true);
        }
        
        if (moDocumentsGrid.getModel().getRowCount() < maDocuments.size()) {
            populateDocumentsGrid(maDocuments, false);
        }
        
        mbDocumentsBeingFiltered = false;
    }
    
    private void itemStateChangedFilterPartner() {
        mbDocumentsBeingFiltered = true;
        
        ArrayList<SMassAccountDocument> documents = new ArrayList<>();
        
        if (moKeyFilterPartner.getSelectedIndex() <= 0) {
            documents.addAll(maDocuments);
        }
        else {
            int id = moKeyFilterPartner.getSelectedItem().getPrimaryKey()[0];
            
            for (SMassAccountDocument mad : maDocuments) {
                if (mad.ImportedDocument.BizPartnerId == id) {
                    documents.add(mad);
                }
            }
        }
        
        populateDocumentsGrid(documents, false);
        
        mbDocumentsBeingFiltered = false;
    }
    
    private void itemStateChangedFilterItem() {
        mbDocumentsBeingFiltered = true;
        
        ArrayList<SMassAccountDocument> documents = new ArrayList<>();
        
        if (moKeyFilterItem.getSelectedIndex() <= 0) {
            documents.addAll(maDocuments);
        }
        else {
            String code = "" + moKeyFilterItem.getSelectedItem().getPrimaryKey()[0];
            
            switch (moSettings.ModeCase) {
                case SImportedDocument.DOC_CASE_RAW_MAT_FREIGHT:
                    for (SMassAccountDocument mad : maDocuments) {
                        if (mad.CartaPorteBienesTranspsCode.equals(code)) {
                            documents.add(mad);
                        }
                    }
                    break;
                case SImportedDocument.DOC_CASE_RAW_MAT_PURCHASE:
                    for (SMassAccountDocument mad : maDocuments) {
                        if (mad.ComprobanteProdServCode.equals(code)) {
                            documents.add(mad);
                        }
                    }
                    break;
                default:
                    // nothing
            }
        }
        
        populateDocumentsGrid(documents, false);
        
        mbDocumentsBeingFiltered = false;
    }
    
    /*
     * Public methods.
     */
    
    public Config getConfig() {
        return moConfig;
    }

    public Pattern getPatternScaleTicketBol() {
        return moPatternScaleTicketBol;
    }
    
    public Pattern getPatternScaleTicketRef() {
        return moPatternScaleTicketRef;
    }
    
    public Pattern getPatternWarehouse() {
        return moPatternWarehouse;
    }
    
    public void recountDocsProcessable() {
        int docsRecordable = 0;
        int reqPaysRequestable = 0;
        
        for (SGridRow row : moDocumentsGrid.getModel().getGridRows()) {
            SMassAccountDocument document = (SMassAccountDocument) row;
            
            if (document.isRecordable()) {
                docsRecordable++;
                
                if (document.ImportedDocument.isPaymentRequestDataAvailable()) {
                    reqPaysRequestable++;
                }
            }
        }
        
        jtfDocsRecordable.setText(SLibUtils.DecimalFormatInteger.format(docsRecordable));
        jtfReqPaysRequestable.setText(SLibUtils.DecimalFormatInteger.format(reqPaysRequestable));
    }
    
    public void recountDocsToProcess() {
        int docsToRecord = 0;
        int reqPaysToRequest = 0;
        
        for (SGridRow row : moDocumentsGrid.getModel().getGridRows()) {
            SMassAccountDocument document = (SMassAccountDocument) row;
            
            if (document.Record) {
                docsToRecord++;
                
                if (document.ImportedDocument.RequirePayment) {
                    reqPaysToRequest++;
                }
            }
        }
        
        jtfDocsToRecord.setText(SLibUtils.DecimalFormatInteger.format(docsToRecord));
        jtfReqPaysToRequest.setText(SLibUtils.DecimalFormatInteger.format(reqPaysToRequest));
    }
    
    /*
     * Overriden methods.
     */
    
    @Override
    protected void windowActivated() {
        if (mbFirstActivation) {
            if (((SClientInterface) miClient).getSessionXXX().getCurrentCompanyBranchId() == 0) {
                // no branch selected in current user session:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_SESSION_BRANCH + "\n"
                        + "No se podrá importar o capturar facturas, hasta que se seleccione una sucursal de la empresa.");
                
                jbRecordDocs.setEnabled(false);
            }
            else {
                jbRecordDocs.setEnabled(true);
            }
            
            super.windowActivated();
        }
    }
    
    @Override
    public void resetForm() {
        removeAllListeners();
        
        mnFormResult = 0;
        mbFirstActivation = true;
        
        try {
            moConfig = new ObjectMapper().readValue(SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_SIIE_CFG_MASS_ACC), Config.class);
            
            setSettings(null);
            setImportedDocuments(null, null);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        mtNewRequiredDate = null;
        jtfReqPaysNewRequiredDate.setText("");
        
        mbExportPaymentRequests = false;
        mnDocsRecordedAndLinked = 0;
        jtfDocsRecordedAndLinked.setText("" + mnDocsRecordedAndLinked);
        
        addAllListeners();
    }
    
    @Override
    public void addAllListeners() {
        jbSelectAllDocs.addActionListener(this);
        jbDeselectAllDocs.addActionListener(this);
        
        jbRejectInvoice.addActionListener(this);
        jbViewInvoicePdf.addActionListener(this);
        jbViewOrder.addActionListener(this);
        
        jbViewAdvances.addActionListener(this);
        jbEditAndSaveReqPayAmount.addActionListener(this);
        jbCancelEditReqPayAmount.addActionListener(this);
        jbChangeReqPayRequiredDate.addActionListener(this);
        jbPickReqPaysNewRequiredDate.addActionListener(this);
        jbSetReqPaysNewRequiredDate.addActionListener(this);
        jbRecordDocs.addActionListener(this);
        
        jbBolViewScaleTicket.addActionListener(this);
        jbAccShowParsingErrorOrWarning.addActionListener(this);
        
        moBoolReqPayRequire.addItemListener(this);
        
        moRadFilterPartner.addItemListener(this);
        moRadFilterItem.addItemListener(this);
        moRadFilterAll.addItemListener(this);
        
        moKeyFilterPartner.addItemListener(this);
        moKeyFilterItem.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbSelectAllDocs.removeActionListener(this);
        jbDeselectAllDocs.removeActionListener(this);
        
        jbRejectInvoice.removeActionListener(this);
        jbViewInvoicePdf.removeActionListener(this);
        jbViewOrder.removeActionListener(this);
        
        jbViewAdvances.removeActionListener(this);
        jbEditAndSaveReqPayAmount.removeActionListener(this);
        jbCancelEditReqPayAmount.removeActionListener(this);
        jbChangeReqPayRequiredDate.removeActionListener(this);
        jbPickReqPaysNewRequiredDate.removeActionListener(this);
        jbSetReqPaysNewRequiredDate.removeActionListener(this);
        jbRecordDocs.removeActionListener(this);
        
        jbBolViewScaleTicket.removeActionListener(this);
        jbAccShowParsingErrorOrWarning.removeActionListener(this);
        
        moBoolReqPayRequire.removeItemListener(this);
        
        moRadFilterPartner.removeItemListener(this);
        moRadFilterItem.removeItemListener(this);
        moRadFilterAll.removeItemListener(this);
        
        moKeyFilterPartner.removeItemListener(this);
        moKeyFilterItem.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiValidation validateForm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void setValue(final int type, final Object value) {
        switch (type) {
            case VALUE_SETTINGS:
                setSettings(((SDialogImportDocuments.Settings) value));
                break;
                
            case VALUE_DOCUMENTS_AND_ADVANCES:
                Object[] params = (Object[]) value;
                setImportedDocuments((ArrayList<SImportedDocument>) params[0], (HashMap<Integer, SFinUtilities.Balance[]>) params[1]);
                break;
                
            default:
                // nothing
        }
    }

    @Override
    public Object getValue(final int type) {
        Object value = null;
        
        try {
            switch (type) {
                case VALUE_EXPORT_PAYMENTS:
                    value = mbExportPaymentRequests;
                    break;
                    
                case VALUE_REJECTED_INVOICES:
                    ArrayList<SImportedDocument> rejectedInvoices = new ArrayList<>();
                    for (SMassAccountDocument mad : maDocumentsRejected) {
                        rejectedInvoices.add(mad.ImportedDocument);
                    }
                    value = rejectedInvoices;
                    break;
                    
                case VALUE_ADVANCES:
                    value = moAdvancesMap;
                    break;
                    
                default:
                    // nothing
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        return value;
    }

    @Override
    public void actionCancel() {
        if (mbDocumentsBeingProcessed) {
            miClient.showMsgBoxWarning("No se puede cerrar este diálogo durante el procesamiento de los comprobantes.");
        }
        else if (jbCancel.isEnabled()) {
            boolean cancel = true;
            
            if (!maDocuments.isEmpty()) {
                String confirm = "Aún " + (maDocuments.size() == 1 ? "queda un comprobante" : "quedan " + SLibUtils.DecimalFormatInteger.format(maDocuments.size()) + " comprobantes") + " por procesar.";
                cancel = miClient.showMsgBoxConfirm(confirm + "\n¿Está seguro que desea cerrar este diálogo?") == JOptionPane.YES_OPTION;
            }
            
            if (cancel) {
                super.actionCancel();
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbSelectAllDocs) {
                actionPerformedSelectAllDocs();
            }
            else if (button == jbDeselectAllDocs) {
                actionPerformedDeselectAllDocs();
            }
            else if (button == jbRejectInvoice) {
                actionPerformedRejectInvoice();
            }
            else if (button == jbViewInvoicePdf) {
                actionPerformedViewInvoicePdf();
            }
            else if (button == jbViewOrder) {
                actionPerformedViewOrder();
            }
            else if (button == jbViewAdvances) {
                actionPerformedViewAdvances();
            }
            else if (button == jbEditAndSaveReqPayAmount) {
                actionPerformedEditAndSaveReqPayAmount();
            }
            else if (button == jbCancelEditReqPayAmount) {
                actionPerformedCancelEditReqPayAmount(true);
            }
            else if (button == jbChangeReqPayRequiredDate) {
                actionPerformedChangeReqPayRequiredDate();
            }
            else if (button == jbPickReqPaysNewRequiredDate) {
                actionPerformedPickReqPaysNewRequiredDate();
            }
            else if (button == jbSetReqPaysNewRequiredDate) {
                actionPerformedSetReqPaysNewRequiredDate();
            }
            else if (button == jbRecordDocs) {
                actionPerformedRecordDocs();
            }
            else if (button == jbBolViewScaleTicket) {
                actionPerformedBolViewScaleTicket();
            }
            else if (button == jbAccShowParsingErrorOrWarning) {
                actionPerformedAccShowParsingErrorOrWarning();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!mbDocumentsBeingRefreshed) {
            if (!e.getValueIsAdjusting()) {
                renderCurrentDoc();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (!mbDocumentsBeingReloaded && !mbDocumentsBeingFiltered && !mbDocumentsBeingRendered) {
            if (e.getSource() instanceof SBeanFieldBoolean) {
                SBeanFieldBoolean field = (SBeanFieldBoolean) e.getSource();

                if (field == moBoolReqPayRequire) {
                    itemStateChangedReqPayRequire();
                }
            }
            else if (e.getSource() instanceof SBeanFieldRadio && e.getStateChange() == ItemEvent.SELECTED) {
                SBeanFieldRadio field = (SBeanFieldRadio) e.getSource();

                if (field == moRadFilterPartner || field == moRadFilterItem || field == moRadFilterAll) {
                    itemStateChangedFilter();
                }
            }
            else if (e.getSource() instanceof SBeanFieldKey && e.getStateChange() == ItemEvent.SELECTED) {
                SBeanFieldKey field = (SBeanFieldKey) e.getSource();

                if (field == moKeyFilterPartner) {
                    itemStateChangedFilterPartner(); // reloads documents grid
                }
                else if (field == moKeyFilterItem) {
                    itemStateChangedFilterItem(); // reloads documents grid
                }
            }
        }
    }
}

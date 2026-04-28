/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.form;

import cfd.ver40.DCfdi40Catalogs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mcfg.data.SCfgUtils;
import erp.mcfg.data.SDataParamsCompany;
import erp.mfin.data.SFinUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbComImportLog;
import erp.mod.cfg.db.SDbFunctionalSubArea;
import erp.mod.cfg.swap.SHttpConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.SSwapUtils;
import erp.mod.cfg.swap.SSyncType;
import erp.mod.cfg.swap.utils.SDataRejectResource;
import erp.mod.cfg.swap.utils.SExportDataAuthActor;
import erp.mod.cfg.swap.utils.SExportUtils;
import erp.mod.cfg.swap.utils.SImportUtils;
import erp.mod.cfg.swap.utils.SResponses;
import erp.mod.cfg.swap.utils.SServicesUtils;
import erp.mod.cfg.utils.SAuthJsonUtils;
import erp.mod.fin.db.SDbPayment;
import erp.mod.trn.db.SDbSwapDataProcessing;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SThinDps;
import erp.mtrn.form.SDialogDpsFinder;
import erp.mtrn.view.SViewDps;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiField;
import sa.lib.gui.SGuiItem;
import sa.lib.gui.SGuiOptionPicker;
import sa.lib.gui.SGuiParams;
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
 * @author Sergio Flores, Cesar Orozco
 */
public class SDialogImportDocuments extends SBeanFormDialog implements ActionListener, ListSelectionListener, ItemListener {
    
    public static final int SEARCH_BY_PERIOD = 1;
    public static final int SEARCH_BY_WEEK = 2;
    
    public static final int CREATE_FROM_CFDI = 1;
    public static final int CREATE_FROM_SCRATCH = 2;
    
    protected static final int OFF = 0;
    protected static final int ON = 1;
    protected static final int LIMIT_DAYS = 31; // 1 calendar month
    protected static final int LIMIT_WEEKS = 4; // 1 lunar month
    protected static final int BATCH_DOWNLOADS = 100;
    
    protected static final int FUNC_SUB_AREA_CODES_PER_LINE = 15;
    
    protected String msCompanyName;
    protected int mnShowingDocsMode;
    protected SGridPaneForm moDocumentsGrid;
    protected SDialogDpsFinder moDialogDpsFinder;
    protected ArrayList<SImportedDocument> maDocuments;
    protected ArrayList<SDbFunctionalSubArea> maFunctionalSubAreas;
    protected HashMap<Integer, SFinUtilities.Balance[]> moAdvancesMap;
    protected SServicesUtils.ConfigSettings moServicesConfigSettings;
    protected String msUserFunctionalSubAreaCodes;
    protected String msSyncUrlRetrieveByPeriod;
    protected String msSyncUrlRetrieveByWeek;
    protected String msSyncUrlDownload;
    protected String msSyncToken;
    protected String msSyncApiKey;
    protected int mnSyncLimit;
    protected PreparedStatement moPrepStatToCountImports;
    protected PreparedStatement moPrepStatToGetProcessedDpsByExternalId;
    protected PreparedStatement moPrepStatToGetDpsKeyByDocData;
    protected JLabel jlStatus;
    protected SBeanFieldBoolean moBoolExportPaymentRequestsOnClose;
    protected boolean mbAllowLinkGreaterInvoices;
    
    protected boolean mbExportPaymentRequests;
    protected boolean mbDocumentsBeingProcessed;
    protected boolean mbDocumentsBeingReloaded;
    protected boolean mbDocumentsBeingRendered;
    protected boolean mbDocumentsBeingRefreshed;
    protected ImageIcon moIconEdit;
    protected ImageIcon moIconSave;
    protected SDialogPdfViewer moDialogPdfViewer;
    protected SDialogMassAccountDocuments moDialogMassAccountDocuments;
    
    /**
     * Creates new form SDialogImportDocuments
     * @param client GUI client.
     */
    public SDialogImportDocuments(SGuiClient client) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.CFGX_SWAP_IMP_DOCS, 0, "Importación de facturas autorizadas");
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
        jpDownload = new javax.swing.JPanel();
        jpDownloadW = new javax.swing.JPanel();
        jpDownloadW1 = new javax.swing.JPanel();
        jlUser = new javax.swing.JLabel();
        jtfUserName = new javax.swing.JTextField();
        jtfUserFuncSubAreas = new javax.swing.JTextField();
        jpDownloadW2 = new javax.swing.JPanel();
        moRadSearchByPeriod = new sa.lib.gui.bean.SBeanFieldRadio();
        moDatePeriodStart = new sa.lib.gui.bean.SBeanFieldDate();
        jLabelPeriiod1 = new javax.swing.JLabel();
        moDatePeriodEnd = new sa.lib.gui.bean.SBeanFieldDate();
        moRadDocModeType = new sa.lib.gui.bean.SBeanFieldRadio();
        moKeyDocModeType = new sa.lib.gui.bean.SBeanFieldKey();
        jpDownloadW3 = new javax.swing.JPanel();
        moRadSearchByWeek = new sa.lib.gui.bean.SBeanFieldRadio();
        moCalWeekYear = new sa.lib.gui.bean.SBeanFieldCalendarYear();
        jlLabelWeek1 = new javax.swing.JLabel();
        moCalWeekStart = new sa.lib.gui.bean.SBeanFieldCalendarWeek();
        jlLabelWeek2 = new javax.swing.JLabel();
        moCalWeekEnd = new sa.lib.gui.bean.SBeanFieldCalendarWeek();
        moRadDocModeCase = new sa.lib.gui.bean.SBeanFieldRadio();
        moKeyDocModeCase = new sa.lib.gui.bean.SBeanFieldKey();
        jpDownloadE = new javax.swing.JPanel();
        jpDownloadE1 = new javax.swing.JPanel();
        jbShowDocs = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jbSelectRemainingDocs = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jbDownloadSelectedDocs = new javax.swing.JButton();
        jpDownloadE2 = new javax.swing.JPanel();
        jbClearDocs = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jbSelectAllDocs = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jbRecordAllDocs = new javax.swing.JButton();
        jpDownloadE3 = new javax.swing.JPanel();
        moBoolExcludeRecorded = new sa.lib.gui.bean.SBeanFieldBoolean();
        jLabel31 = new javax.swing.JLabel();
        jbDeselectAllDocs = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jbLinkAllDocs = new javax.swing.JButton();
        jpDocuments = new javax.swing.JPanel();
        jpDocumentsPanel = new javax.swing.JPanel();
        jpDocumentsGrid1 = new javax.swing.JPanel();
        jpDocumentsGrid11 = new javax.swing.JPanel();
        jpDocumentsGrid111 = new javax.swing.JPanel();
        jlInvoiceUserNew = new javax.swing.JLabel();
        jpDocumentsGrid112 = new javax.swing.JPanel();
        jtfInvoiceUserNew = new javax.swing.JTextField();
        jpDocumentsGrid12 = new javax.swing.JPanel();
        jpDocumentsGrid121 = new javax.swing.JPanel();
        jlProgress = new javax.swing.JLabel();
        jpDocumentsGrid122 = new javax.swing.JPanel();
        jProgressBar = new javax.swing.JProgressBar();
        jpDocumentsProcessing = new javax.swing.JPanel();
        jpProcessingN = new javax.swing.JPanel();
        jpProcessingN1 = new javax.swing.JPanel();
        jbCreateInvoiceFromCfdi = new javax.swing.JButton();
        jpProcessingN2 = new javax.swing.JPanel();
        jbCreateInvoiceFromScratch = new javax.swing.JButton();
        jpProcessingN3 = new javax.swing.JPanel();
        jbLinkInvoice = new javax.swing.JButton();
        jpProcessingN4 = new javax.swing.JPanel();
        jbUnlinkInvoice = new javax.swing.JButton();
        jpProcessingN5 = new javax.swing.JPanel();
        jbRejectInvoice = new javax.swing.JButton();
        jpProcessingN6 = new javax.swing.JPanel();
        jlInvoice = new javax.swing.JLabel();
        jbViewInvoicePdf = new javax.swing.JButton();
        jpProcessingN7 = new javax.swing.JPanel();
        jtfInvoice = new javax.swing.JTextField();
        jbViewInvoice = new javax.swing.JButton();
        jbViewOrder = new javax.swing.JButton();
        jpProcessingN8 = new javax.swing.JPanel();
        jtfRecord = new javax.swing.JTextField();
        jbViewRecord = new javax.swing.JButton();
        jpProcessingN9 = new javax.swing.JPanel();
        moBoolReqPayRequire = new sa.lib.gui.bean.SBeanFieldBoolean();
        jbViewAdvances = new javax.swing.JButton();
        jpProcessingN10 = new javax.swing.JPanel();
        jtfReqPayAmount = new javax.swing.JTextField();
        jtfReqPayAmountPct = new javax.swing.JTextField();
        jpProcessingN11 = new javax.swing.JPanel();
        moDecReqPayAmount = new sa.lib.gui.bean.SBeanFieldDecimal();
        jpProcessingN111 = new javax.swing.JPanel();
        jbEditAndSaveReqPayAmount = new javax.swing.JButton();
        jbCancelEditReqPayAmount = new javax.swing.JButton();
        jpProcessingN12 = new javax.swing.JPanel();
        jtfReqPayRequiredDate = new javax.swing.JTextField();
        jbChangeReqPayRequiredDate = new javax.swing.JButton();
        jpProcessingN13 = new javax.swing.JPanel();
        jbRequestPayment = new javax.swing.JButton();
        jpProcessingN14 = new javax.swing.JPanel();
        jlPay = new javax.swing.JLabel();
        jpProcessingN15 = new javax.swing.JPanel();
        jtfPayFolio = new javax.swing.JTextField();
        jtfPayDate = new javax.swing.JTextField();
        jpProcessingN16 = new javax.swing.JPanel();
        jtfPayRequiredDate = new javax.swing.JTextField();
        jbChangePayRequiredDate = new javax.swing.JButton();
        jpProcessingN17 = new javax.swing.JPanel();
        jtfPayStatus = new javax.swing.JTextField();
        jpProcessingN18 = new javax.swing.JPanel();
        jlPayScheduledDate = new javax.swing.JLabel();
        jpProcessingN19 = new javax.swing.JPanel();
        jtfPayScheduledDate = new javax.swing.JTextField();
        jbChangePayScheduledDate = new javax.swing.JButton();
        jpProcessingN20 = new javax.swing.JPanel();
        jlPayExecution = new javax.swing.JLabel();
        jpProcessingN21 = new javax.swing.JPanel();
        jtfPayExecutionDate = new javax.swing.JTextField();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpDownload.setBorder(javax.swing.BorderFactory.createTitledBorder("Búsqueda de facturas autorizadas:"));
        jpDownload.setLayout(new java.awt.BorderLayout());

        jpDownloadW.setLayout(new java.awt.GridLayout(3, 1, 0, 1));

        jpDownloadW1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlUser.setText("Usuario:");
        jlUser.setPreferredSize(new java.awt.Dimension(75, 21));
        jpDownloadW1.add(jlUser);

        jtfUserName.setEditable(false);
        jtfUserName.setText("user.name");
        jtfUserName.setToolTipText("Usuario actual");
        jtfUserName.setFocusable(false);
        jtfUserName.setPreferredSize(new java.awt.Dimension(103, 21));
        jpDownloadW1.add(jtfUserName);

        jtfUserFuncSubAreas.setEditable(false);
        jtfUserFuncSubAreas.setText("FUNC. AREAS");
        jtfUserFuncSubAreas.setToolTipText("Subáreas funcionales");
        jtfUserFuncSubAreas.setFocusable(false);
        jtfUserFuncSubAreas.setPreferredSize(new java.awt.Dimension(313, 21));
        jpDownloadW1.add(jtfUserFuncSubAreas);

        jpDownloadW.add(jpDownloadW1);

        jpDownloadW2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgSearchBy.add(moRadSearchByPeriod);
        moRadSearchByPeriod.setText("Período:");
        moRadSearchByPeriod.setPreferredSize(new java.awt.Dimension(75, 21));
        jpDownloadW2.add(moRadSearchByPeriod);

        moDatePeriodStart.setToolTipText("Fecha inicial");
        moDatePeriodStart.setPreferredSize(new java.awt.Dimension(103, 21));
        jpDownloadW2.add(moDatePeriodStart);

        jLabelPeriiod1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPeriiod1.setText("−");
        jLabelPeriiod1.setPreferredSize(new java.awt.Dimension(15, 21));
        jpDownloadW2.add(jLabelPeriiod1);

        moDatePeriodEnd.setToolTipText("Fecha final");
        moDatePeriodEnd.setPreferredSize(new java.awt.Dimension(103, 21));
        jpDownloadW2.add(moDatePeriodEnd);

        bgDocMode.add(moRadDocModeType);
        moRadDocModeType.setText("Tipo:");
        moRadDocModeType.setPreferredSize(new java.awt.Dimension(65, 21));
        jpDownloadW2.add(moRadDocModeType);

        moKeyDocModeType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Activo fijo", "Compras y gastos" }));
        moKeyDocModeType.setPreferredSize(new java.awt.Dimension(115, 21));
        jpDownloadW2.add(moKeyDocModeType);

        jpDownloadW.add(jpDownloadW2);

        jpDownloadW3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgSearchBy.add(moRadSearchByWeek);
        moRadSearchByWeek.setText("Semana:");
        moRadSearchByWeek.setPreferredSize(new java.awt.Dimension(75, 21));
        jpDownloadW3.add(moRadSearchByWeek);

        moCalWeekYear.setToolTipText("Año");
        moCalWeekYear.setPreferredSize(new java.awt.Dimension(75, 21));
        jpDownloadW3.add(moCalWeekYear);

        jlLabelWeek1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlLabelWeek1.setText(":");
        jlLabelWeek1.setPreferredSize(new java.awt.Dimension(21, 21));
        jpDownloadW3.add(jlLabelWeek1);

        moCalWeekStart.setToolTipText("Semana inicial");
        moCalWeekStart.setPreferredSize(new java.awt.Dimension(50, 21));
        jpDownloadW3.add(moCalWeekStart);

        jlLabelWeek2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlLabelWeek2.setText("−");
        jlLabelWeek2.setPreferredSize(new java.awt.Dimension(15, 21));
        jpDownloadW3.add(jlLabelWeek2);

        moCalWeekEnd.setToolTipText("Semana final");
        moCalWeekEnd.setPreferredSize(new java.awt.Dimension(50, 21));
        jpDownloadW3.add(moCalWeekEnd);

        bgDocMode.add(moRadDocModeCase);
        moRadDocModeCase.setText("Caso:");
        moRadDocModeCase.setPreferredSize(new java.awt.Dimension(65, 21));
        jpDownloadW3.add(moRadDocModeCase);

        moKeyDocModeCase.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Estándar", "Fletes fruta", "Compras fruta" }));
        moKeyDocModeCase.setPreferredSize(new java.awt.Dimension(115, 21));
        jpDownloadW3.add(moKeyDocModeCase);

        jpDownloadW.add(jpDownloadW3);

        jpDownload.add(jpDownloadW, java.awt.BorderLayout.WEST);

        jpDownloadE.setLayout(new java.awt.GridLayout(3, 1, 0, 1));

        jpDownloadE1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jbShowDocs.setText("Mostrar facturas");
        jbShowDocs.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbShowDocs.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE1.add(jbShowDocs);

        jLabel11.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE1.add(jLabel11);

        jbSelectRemainingDocs.setText("Seleccionar restantes");
        jbSelectRemainingDocs.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbSelectRemainingDocs.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE1.add(jbSelectRemainingDocs);

        jLabel12.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE1.add(jLabel12);

        jbDownloadSelectedDocs.setText("Descargar seleccionadas");
        jbDownloadSelectedDocs.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbDownloadSelectedDocs.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE1.add(jbDownloadSelectedDocs);

        jpDownloadE.add(jpDownloadE1);

        jpDownloadE2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jbClearDocs.setText("Limpiar facturas");
        jbClearDocs.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbClearDocs.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE2.add(jbClearDocs);

        jLabel21.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE2.add(jLabel21);

        jbSelectAllDocs.setText("Seleccionar todas");
        jbSelectAllDocs.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbSelectAllDocs.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE2.add(jbSelectAllDocs);

        jLabel22.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE2.add(jLabel22);

        jbRecordAllDocs.setForeground(java.awt.Color.blue);
        jbRecordAllDocs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_mod_fin.png"))); // NOI18N
        jbRecordAllDocs.setText("Contabilizar todas");
        jbRecordAllDocs.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jbRecordAllDocs.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbRecordAllDocs.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE2.add(jbRecordAllDocs);

        jpDownloadE.add(jpDownloadE2);

        jpDownloadE3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        moBoolExcludeRecorded.setText("Excluir contabilizadas");
        moBoolExcludeRecorded.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE3.add(moBoolExcludeRecorded);

        jLabel31.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE3.add(jLabel31);

        jbDeselectAllDocs.setText("Deseleccionar todas");
        jbDeselectAllDocs.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbDeselectAllDocs.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE3.add(jbDeselectAllDocs);

        jLabel32.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE3.add(jLabel32);

        jbLinkAllDocs.setForeground(java.awt.Color.blue);
        jbLinkAllDocs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_mod_cfg.png"))); // NOI18N
        jbLinkAllDocs.setText("Vincular todas");
        jbLinkAllDocs.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jbLinkAllDocs.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbLinkAllDocs.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE3.add(jbLinkAllDocs);

        jpDownloadE.add(jpDownloadE3);

        jpDownload.add(jpDownloadE, java.awt.BorderLayout.EAST);

        getContentPane().add(jpDownload, java.awt.BorderLayout.NORTH);

        jpDocuments.setLayout(new java.awt.BorderLayout(5, 0));

        jpDocumentsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Facturas autorizadas:"));
        jpDocumentsPanel.setLayout(new java.awt.BorderLayout());

        jpDocumentsGrid1.setLayout(new java.awt.BorderLayout());

        jpDocumentsGrid11.setLayout(new java.awt.GridLayout(2, 1, 0, 2));

        jpDocumentsGrid111.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInvoiceUserNew.setText("Usr. factura:");
        jlInvoiceUserNew.setPreferredSize(new java.awt.Dimension(100, 20));
        jpDocumentsGrid111.add(jlInvoiceUserNew);

        jpDocumentsGrid11.add(jpDocumentsGrid111);

        jpDocumentsGrid112.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfInvoiceUserNew.setEditable(false);
        jtfInvoiceUserNew.setText("user.name");
        jtfInvoiceUserNew.setFocusable(false);
        jtfInvoiceUserNew.setPreferredSize(new java.awt.Dimension(100, 20));
        jpDocumentsGrid112.add(jtfInvoiceUserNew);

        jpDocumentsGrid11.add(jpDocumentsGrid112);

        jpDocumentsGrid1.add(jpDocumentsGrid11, java.awt.BorderLayout.CENTER);

        jpDocumentsGrid12.setLayout(new java.awt.GridLayout(2, 1, 0, 2));

        jpDocumentsGrid121.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProgress.setText("Progreso...");
        jlProgress.setPreferredSize(new java.awt.Dimension(200, 20));
        jpDocumentsGrid121.add(jlProgress);

        jpDocumentsGrid12.add(jpDocumentsGrid121);

        jpDocumentsGrid122.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jProgressBar.setPreferredSize(new java.awt.Dimension(200, 20));
        jpDocumentsGrid122.add(jProgressBar);

        jpDocumentsGrid12.add(jpDocumentsGrid122);

        jpDocumentsGrid1.add(jpDocumentsGrid12, java.awt.BorderLayout.EAST);

        jpDocumentsPanel.add(jpDocumentsGrid1, java.awt.BorderLayout.SOUTH);

        jpDocuments.add(jpDocumentsPanel, java.awt.BorderLayout.CENTER);

        jpDocumentsProcessing.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 2));
        jpDocumentsProcessing.setLayout(new java.awt.BorderLayout());

        jpProcessingN.setLayout(new java.awt.GridLayout(21, 1, 0, 1));

        jpProcessingN1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbCreateInvoiceFromCfdi.setForeground(java.awt.Color.blue);
        jbCreateInvoiceFromCfdi.setText("Importar CFDI");
        jbCreateInvoiceFromCfdi.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbCreateInvoiceFromCfdi.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN1.add(jbCreateInvoiceFromCfdi);

        jpProcessingN.add(jpProcessingN1);

        jpProcessingN2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbCreateInvoiceFromScratch.setForeground(java.awt.Color.blue);
        jbCreateInvoiceFromScratch.setText("Crear factura");
        jbCreateInvoiceFromScratch.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbCreateInvoiceFromScratch.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN2.add(jbCreateInvoiceFromScratch);

        jpProcessingN.add(jpProcessingN2);

        jpProcessingN3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbLinkInvoice.setForeground(java.awt.Color.blue);
        jbLinkInvoice.setText("Vincular factura");
        jbLinkInvoice.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbLinkInvoice.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN3.add(jbLinkInvoice);

        jpProcessingN.add(jpProcessingN3);

        jpProcessingN4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbUnlinkInvoice.setForeground(java.awt.Color.red);
        jbUnlinkInvoice.setText("Desvincular factura");
        jbUnlinkInvoice.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbUnlinkInvoice.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN4.add(jbUnlinkInvoice);

        jpProcessingN.add(jpProcessingN4);

        jpProcessingN5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbRejectInvoice.setForeground(java.awt.Color.red);
        jbRejectInvoice.setText("Rechazar factura");
        jbRejectInvoice.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbRejectInvoice.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN5.add(jbRejectInvoice);

        jpProcessingN.add(jpProcessingN5);

        jpProcessingN6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInvoice.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlInvoice.setText("Factura:");
        jlInvoice.setPreferredSize(new java.awt.Dimension(122, 23));
        jpProcessingN6.add(jlInvoice);

        jbViewInvoicePdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon-file-pdf.png"))); // NOI18N
        jbViewInvoicePdf.setToolTipText("Ver PDF de la factura...");
        jbViewInvoicePdf.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN6.add(jbViewInvoicePdf);

        jpProcessingN.add(jpProcessingN6);

        jpProcessingN7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfInvoice.setEditable(false);
        jtfInvoice.setText("ABC-000000");
        jtfInvoice.setToolTipText("Factura");
        jtfInvoice.setFocusable(false);
        jtfInvoice.setPreferredSize(new java.awt.Dimension(95, 23));
        jpProcessingN7.add(jtfInvoice);

        jbViewInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif"))); // NOI18N
        jbViewInvoice.setToolTipText("Ver factura...");
        jbViewInvoice.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN7.add(jbViewInvoice);

        jbViewOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_seek.gif"))); // NOI18N
        jbViewOrder.setToolTipText("Ver pedido de la factura...");
        jbViewOrder.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN7.add(jbViewOrder);

        jpProcessingN.add(jpProcessingN7);

        jpProcessingN8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfRecord.setEditable(false);
        jtfRecord.setText("2001-01 SUC C-000000");
        jtfRecord.setToolTipText("Póliza contable de la factura");
        jtfRecord.setFocusable(false);
        jtfRecord.setPreferredSize(new java.awt.Dimension(122, 23));
        jpProcessingN8.add(jtfRecord);

        jbViewRecord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif"))); // NOI18N
        jbViewRecord.setToolTipText("Ver póliza contable de la factura...");
        jbViewRecord.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN8.add(jbViewRecord);

        jpProcessingN.add(jpProcessingN8);

        jpProcessingN9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolReqPayRequire.setText("Pago requerido:");
        moBoolReqPayRequire.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        moBoolReqPayRequire.setPreferredSize(new java.awt.Dimension(122, 23));
        jpProcessingN9.add(moBoolReqPayRequire);

        jbViewAdvances.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_warn.png"))); // NOI18N
        jbViewAdvances.setToolTipText("Ver anticipos del proveedor...");
        jbViewAdvances.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN9.add(jbViewAdvances);

        jpProcessingN.add(jpProcessingN9);

        jpProcessingN10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfReqPayAmount.setEditable(false);
        jtfReqPayAmount.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfReqPayAmount.setText("000,000,000.00 MXN");
        jtfReqPayAmount.setToolTipText("Pago requerido");
        jtfReqPayAmount.setFocusable(false);
        jtfReqPayAmount.setPreferredSize(new java.awt.Dimension(105, 23));
        jpProcessingN10.add(jtfReqPayAmount);

        jtfReqPayAmountPct.setEditable(false);
        jtfReqPayAmountPct.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfReqPayAmountPct.setText("100%");
        jtfReqPayAmountPct.setToolTipText("Porcentaje de pago requerido");
        jtfReqPayAmountPct.setFocusable(false);
        jtfReqPayAmountPct.setPreferredSize(new java.awt.Dimension(40, 23));
        jpProcessingN10.add(jtfReqPayAmountPct);

        jpProcessingN.add(jpProcessingN10);

        jpProcessingN11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moDecReqPayAmount.setPreferredSize(new java.awt.Dimension(105, 23));
        jpProcessingN11.add(moDecReqPayAmount);

        jpProcessingN111.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jbEditAndSaveReqPayAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditAndSaveReqPayAmount.setToolTipText("Modificar monto requerido de pago");
        jbEditAndSaveReqPayAmount.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN111.add(jbEditAndSaveReqPayAmount);

        jbCancelEditReqPayAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/ico_close.png"))); // NOI18N
        jbCancelEditReqPayAmount.setToolTipText("Cancelar modificación");
        jbCancelEditReqPayAmount.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbCancelEditReqPayAmount.setPreferredSize(new java.awt.Dimension(17, 23));
        jpProcessingN111.add(jbCancelEditReqPayAmount);

        jpProcessingN11.add(jpProcessingN111);

        jpProcessingN.add(jpProcessingN11);

        jpProcessingN12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfReqPayRequiredDate.setEditable(false);
        jtfReqPayRequiredDate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfReqPayRequiredDate.setText("dow 01/mon/2001");
        jtfReqPayRequiredDate.setToolTipText("Fecha requerida de pago");
        jtfReqPayRequiredDate.setFocusable(false);
        jtfReqPayRequiredDate.setPreferredSize(new java.awt.Dimension(105, 23));
        jpProcessingN12.add(jtfReqPayRequiredDate);

        jbChangeReqPayRequiredDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbChangeReqPayRequiredDate.setToolTipText("Cambiar fecha requerida de pago...");
        jbChangeReqPayRequiredDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN12.add(jbChangeReqPayRequiredDate);

        jpProcessingN.add(jpProcessingN12);

        jpProcessingN13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbRequestPayment.setText("Solicitar pago");
        jbRequestPayment.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbRequestPayment.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN13.add(jbRequestPayment);

        jpProcessingN.add(jpProcessingN13);

        jpProcessingN14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPay.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlPay.setText("Solicitud de pago:");
        jlPay.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN14.add(jlPay);

        jpProcessingN.add(jpProcessingN14);

        jpProcessingN15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfPayFolio.setEditable(false);
        jtfPayFolio.setText("P-000000");
        jtfPayFolio.setToolTipText("Folio de solicitud de pago");
        jtfPayFolio.setFocusable(false);
        jtfPayFolio.setPreferredSize(new java.awt.Dimension(75, 23));
        jpProcessingN15.add(jtfPayFolio);

        jtfPayDate.setEditable(false);
        jtfPayDate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPayDate.setText("01/01/2001");
        jtfPayDate.setToolTipText("Fecha de solicitud de pago");
        jtfPayDate.setFocusable(false);
        jtfPayDate.setPreferredSize(new java.awt.Dimension(70, 23));
        jpProcessingN15.add(jtfPayDate);

        jpProcessingN.add(jpProcessingN15);

        jpProcessingN16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfPayRequiredDate.setEditable(false);
        jtfPayRequiredDate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPayRequiredDate.setText("dow 01/mon/2001");
        jtfPayRequiredDate.setToolTipText("Fecha requerida de pago en solicitud de pago");
        jtfPayRequiredDate.setFocusable(false);
        jtfPayRequiredDate.setPreferredSize(new java.awt.Dimension(105, 23));
        jpProcessingN16.add(jtfPayRequiredDate);

        jbChangePayRequiredDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbChangePayRequiredDate.setToolTipText("Cambiar fecha requerida de pago en solicitud de pago...");
        jbChangePayRequiredDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN16.add(jbChangePayRequiredDate);

        jpProcessingN.add(jpProcessingN16);

        jpProcessingN17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfPayStatus.setEditable(false);
        jtfPayStatus.setText("STATUS");
        jtfPayStatus.setToolTipText("Estatus de solicitud de pago");
        jtfPayStatus.setFocusable(false);
        jtfPayStatus.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN17.add(jtfPayStatus);

        jpProcessingN.add(jpProcessingN17);

        jpProcessingN18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayScheduledDate.setText("Programación del pago:");
        jlPayScheduledDate.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN18.add(jlPayScheduledDate);

        jpProcessingN.add(jpProcessingN18);

        jpProcessingN19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfPayScheduledDate.setEditable(false);
        jtfPayScheduledDate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPayScheduledDate.setText("dow 01/mon/2001");
        jtfPayScheduledDate.setToolTipText("Fecha de programación del pago");
        jtfPayScheduledDate.setFocusable(false);
        jtfPayScheduledDate.setPreferredSize(new java.awt.Dimension(105, 23));
        jpProcessingN19.add(jtfPayScheduledDate);

        jbChangePayScheduledDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbChangePayScheduledDate.setToolTipText("Cambiar fecha de programación del pago...");
        jbChangePayScheduledDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN19.add(jbChangePayScheduledDate);

        jpProcessingN.add(jpProcessingN19);

        jpProcessingN20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayExecution.setText("Operación del pago:");
        jlPayExecution.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN20.add(jlPayExecution);

        jpProcessingN.add(jpProcessingN20);

        jpProcessingN21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfPayExecutionDate.setEditable(false);
        jtfPayExecutionDate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPayExecutionDate.setText("dow 01/mon/2001");
        jtfPayExecutionDate.setToolTipText("Fecha de operación del pago");
        jtfPayExecutionDate.setFocusable(false);
        jtfPayExecutionDate.setPreferredSize(new java.awt.Dimension(105, 23));
        jpProcessingN21.add(jtfPayExecutionDate);

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
    private javax.swing.ButtonGroup bgSearchBy;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabelPeriiod1;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JButton jbCancelEditReqPayAmount;
    private javax.swing.JButton jbChangePayRequiredDate;
    private javax.swing.JButton jbChangePayScheduledDate;
    private javax.swing.JButton jbChangeReqPayRequiredDate;
    private javax.swing.JButton jbClearDocs;
    private javax.swing.JButton jbCreateInvoiceFromCfdi;
    private javax.swing.JButton jbCreateInvoiceFromScratch;
    private javax.swing.JButton jbDeselectAllDocs;
    private javax.swing.JButton jbDownloadSelectedDocs;
    private javax.swing.JButton jbEditAndSaveReqPayAmount;
    private javax.swing.JButton jbLinkAllDocs;
    private javax.swing.JButton jbLinkInvoice;
    private javax.swing.JButton jbRecordAllDocs;
    private javax.swing.JButton jbRejectInvoice;
    private javax.swing.JButton jbRequestPayment;
    private javax.swing.JButton jbSelectAllDocs;
    private javax.swing.JButton jbSelectRemainingDocs;
    private javax.swing.JButton jbShowDocs;
    private javax.swing.JButton jbUnlinkInvoice;
    private javax.swing.JButton jbViewAdvances;
    private javax.swing.JButton jbViewInvoice;
    private javax.swing.JButton jbViewInvoicePdf;
    private javax.swing.JButton jbViewOrder;
    private javax.swing.JButton jbViewRecord;
    private javax.swing.JLabel jlInvoice;
    private javax.swing.JLabel jlInvoiceUserNew;
    private javax.swing.JLabel jlLabelWeek1;
    private javax.swing.JLabel jlLabelWeek2;
    private javax.swing.JLabel jlPay;
    private javax.swing.JLabel jlPayExecution;
    private javax.swing.JLabel jlPayScheduledDate;
    private javax.swing.JLabel jlProgress;
    private javax.swing.JLabel jlUser;
    private javax.swing.JPanel jpDocuments;
    private javax.swing.JPanel jpDocumentsGrid1;
    private javax.swing.JPanel jpDocumentsGrid11;
    private javax.swing.JPanel jpDocumentsGrid111;
    private javax.swing.JPanel jpDocumentsGrid112;
    private javax.swing.JPanel jpDocumentsGrid12;
    private javax.swing.JPanel jpDocumentsGrid121;
    private javax.swing.JPanel jpDocumentsGrid122;
    private javax.swing.JPanel jpDocumentsPanel;
    private javax.swing.JPanel jpDocumentsProcessing;
    private javax.swing.JPanel jpDownload;
    private javax.swing.JPanel jpDownloadE;
    private javax.swing.JPanel jpDownloadE1;
    private javax.swing.JPanel jpDownloadE2;
    private javax.swing.JPanel jpDownloadE3;
    private javax.swing.JPanel jpDownloadW;
    private javax.swing.JPanel jpDownloadW1;
    private javax.swing.JPanel jpDownloadW2;
    private javax.swing.JPanel jpDownloadW3;
    private javax.swing.JPanel jpProcessingN;
    private javax.swing.JPanel jpProcessingN1;
    private javax.swing.JPanel jpProcessingN10;
    private javax.swing.JPanel jpProcessingN11;
    private javax.swing.JPanel jpProcessingN111;
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
    private javax.swing.JPanel jpProcessingN7;
    private javax.swing.JPanel jpProcessingN8;
    private javax.swing.JPanel jpProcessingN9;
    private javax.swing.JTextField jtfInvoice;
    private javax.swing.JTextField jtfInvoiceUserNew;
    private javax.swing.JTextField jtfPayDate;
    private javax.swing.JTextField jtfPayExecutionDate;
    private javax.swing.JTextField jtfPayFolio;
    private javax.swing.JTextField jtfPayRequiredDate;
    private javax.swing.JTextField jtfPayScheduledDate;
    private javax.swing.JTextField jtfPayStatus;
    private javax.swing.JTextField jtfRecord;
    private javax.swing.JTextField jtfReqPayAmount;
    private javax.swing.JTextField jtfReqPayAmountPct;
    private javax.swing.JTextField jtfReqPayRequiredDate;
    private javax.swing.JTextField jtfUserFuncSubAreas;
    private javax.swing.JTextField jtfUserName;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolExcludeRecorded;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolReqPayRequire;
    private sa.lib.gui.bean.SBeanFieldCalendarWeek moCalWeekEnd;
    private sa.lib.gui.bean.SBeanFieldCalendarWeek moCalWeekStart;
    private sa.lib.gui.bean.SBeanFieldCalendarYear moCalWeekYear;
    private sa.lib.gui.bean.SBeanFieldDate moDatePeriodEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDatePeriodStart;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecReqPayAmount;
    private sa.lib.gui.bean.SBeanFieldKey moKeyDocModeCase;
    private sa.lib.gui.bean.SBeanFieldKey moKeyDocModeType;
    private sa.lib.gui.bean.SBeanFieldRadio moRadDocModeCase;
    private sa.lib.gui.bean.SBeanFieldRadio moRadDocModeType;
    private sa.lib.gui.bean.SBeanFieldRadio moRadSearchByPeriod;
    private sa.lib.gui.bean.SBeanFieldRadio moRadSearchByWeek;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods.
     */
    
    @SuppressWarnings("unchecked")
    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 670);
        
        moRadSearchByPeriod.setBooleanSettings(SGuiUtils.getLabelName(moRadSearchByPeriod.getText()), true);
        moRadSearchByWeek.setBooleanSettings(SGuiUtils.getLabelName(moRadSearchByWeek.getText()), false);
        moDatePeriodStart.setDateSettings(miClient, moDatePeriodStart.getToolTipText(), true);
        moDatePeriodEnd.setDateSettings(miClient, moDatePeriodEnd.getToolTipText(), true);
        moCalWeekYear.setCalendarSettings(moCalWeekYear.getToolTipText());
        moCalWeekStart.setCalendarSettings(moCalWeekStart.getToolTipText());
        moCalWeekEnd.setCalendarSettings(moCalWeekEnd.getToolTipText());
        moRadDocModeType.setBooleanSettings(SGuiUtils.getLabelName(moRadDocModeType.getText()), true);
        moRadDocModeCase.setBooleanSettings(SGuiUtils.getLabelName(moRadDocModeCase.getText()), false);
        moKeyDocModeType.setKeySettings(miClient, SGuiUtils.getLabelName(moRadDocModeType.getText()), false);
        moKeyDocModeCase.setKeySettings(miClient, SGuiUtils.getLabelName(moRadDocModeCase.getText()), false);
        moBoolExcludeRecorded.setBooleanSettings(SGuiUtils.getLabelName(moBoolExcludeRecorded.getText()), false);
        moBoolReqPayRequire.setBooleanSettings(SGuiUtils.getLabelName(moBoolReqPayRequire.getText()), false);
        moDecReqPayAmount.setDecimalSettings(SGuiUtils.getLabelName(moBoolReqPayRequire.getText()), SGuiConsts.GUI_TYPE_DEC_AMT, false);
        
        moFields.addField(moRadSearchByPeriod);
        moFields.addField(moRadSearchByWeek);
        moFields.addField(moDatePeriodStart);
        moFields.addField(moDatePeriodEnd);
        moFields.addField(moCalWeekYear);
        moFields.addField(moCalWeekStart);
        moFields.addField(moCalWeekEnd);
        moFields.addField(moRadDocModeType);
        moFields.addField(moRadDocModeCase);
        moFields.addField(moKeyDocModeType);
        moFields.addField(moKeyDocModeCase);
        moFields.addField(moBoolExcludeRecorded);
        moFields.addField(moBoolReqPayRequire);
        moFields.addField(moDecReqPayAmount);
        moFields.setFormButton(jbShowDocs);
        
        jbSave.setEnabled(false);
        jbCancel.setText(SGuiConsts.TXT_BTN_CLOSE);
        jbCancel.setPreferredSize(new Dimension(75, 23));
        
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
        mnShowingDocsMode = OFF;
        
        moDocumentsGrid = new SGridPaneForm(miClient, SModConsts.CFGX_SWAP_IMP_DOCS, 1, "Facturas", null) {
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
                column = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Descargar", moDocumentsGrid.getTable().getDefaultEditor(Boolean.class));
                column.setEditable(true);
                gridColumnsForm.add(column);
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Descargada (factura)"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Contabilizada (factura)")); // col 10
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Póliza contable ", 150));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "XML en factura"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "PDF en factura"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Subárea funcional factura"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Uso CFDI factura")); // col 15
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Caso factura"));
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
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Folio solicitud pago", 75));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha solicitud pago")); // col 30
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Folio factura SIIE", 75));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha factura SIIE"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Total factura SIIE $"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda factura SIIE"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Validación factura SIIE", 150)); // col 35
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha vencimiento factura"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Etiqueta contable"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "UUID factura " + SSwapConsts.SWAP_SERVICES, 225));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_RAW, "ID factura " + SSwapConsts.SWAP_SERVICES));
                
                return gridColumnsForm;
            }
        };

        moDocumentsGrid.setForm(null);
        moDocumentsGrid.setPaneFormOwner(null);
        jpDocumentsPanel.add(moDocumentsGrid, BorderLayout.CENTER);
        
        jlStatus = new JLabel();
        jpCommandLeft.add(jlStatus);
        clearProgress();
        
        moBoolExportPaymentRequestsOnClose = new SBeanFieldBoolean();
        moBoolExportPaymentRequestsOnClose.setText("Exportar solicitudes de pago al cerrar");
        moBoolExportPaymentRequestsOnClose.setPreferredSize(new Dimension(250, 23));
        ((FlowLayout) jpCommandCenter.getLayout()).setAlignment(FlowLayout.RIGHT);
        jpCommandCenter.add(moBoolExportPaymentRequestsOnClose);
        
        mbAllowLinkGreaterInvoices = miClient.getSession().getUser().hasPrivilege(SDataConstantsSys.PRV_PUR_LINK_INV_GREATER);
        
        jtfUserName.setText(miClient.getSession().getUser().getName());
        jtfUserName.setCaretPosition(0);
        
        maDocuments = new ArrayList<>();
        
        try {
            moServicesConfigSettings = SServicesUtils.getConfigSettings(miClient.getSession());
            
            if (((SDataParamsCompany) miClient.getSession().getConfigCompany()).getIsFunctionalAreas()) {
                maFunctionalSubAreas = SDbFunctionalSubArea.readUserFunctionalSubAreas(miClient.getSession());
                msUserFunctionalSubAreaCodes = SDbFunctionalSubArea.composeFunctionalSubAreaCodes(maFunctionalSubAreas);

                if (msUserFunctionalSubAreaCodes.isEmpty()) {
                    msUserFunctionalSubAreaCodes = "¡NINGUNA!";
                    miClient.showMsgBoxWarning("El usuario '" + miClient.getSession().getUser().getName() + "' no podrá ver ni procesar facturas autorizadas porque no tiene subáreas funcionales asignadas.");
                }
            }
            else {
                SDbFunctionalSubArea functionalSubArea = (SDbFunctionalSubArea) miClient.getSession().readRegistry(SModConsts.CFGU_FUNC_SUB, new int[] { SModSysConsts.CFGU_FUNC_SUB_NA });
                maFunctionalSubAreas = new ArrayList<>();
                maFunctionalSubAreas.add(functionalSubArea);
                msUserFunctionalSubAreaCodes = functionalSubArea.getCode();
            }
            
            jtfUserFuncSubAreas.setText(msUserFunctionalSubAreaCodes);
            jtfUserFuncSubAreas.setCaretPosition(0);
            jtfUserFuncSubAreas.setToolTipText("Subáreas funcionales: " + msUserFunctionalSubAreaCodes);
            
            moAdvancesMap = new HashMap<>();
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode config = mapper.readTree(SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG));
            
            msSyncUrlRetrieveByPeriod = "";
            msSyncUrlRetrieveByWeek = "";
            msSyncToken = "";
            msSyncApiKey = "";
            mnSyncLimit = 0;

            // Recuperar la configuración base:

            String syncHost;
            
            if (((SClientInterface) miClient).isDev()) {
                //syncHost = "http://192.168.7.92:8004"; // entorno César Orozco
                //syncHost = "https://transaction-backend-test-515680676790.europe-west1.run.app"; // entorno pruebas
                syncHost = "https://transaction-backend-368437194061.us-central1.run.app"; // entorno producción
            }
            else {
                syncHost = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_SRV, SSwapConsts.CFG_ATT_URL);
            }
            
            msSyncToken = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_SRV, SSwapConsts.CFG_ATT_TOKEN);
            msSyncApiKey = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_SRV, SSwapConsts.CFG_ATT_API_KEY);
            
            // documents retreival service: /api/documents/filter-by-date-and-type/?start_date=<start_date>&end_date=<end_date>&document_type=<document_type>; date format: yyyy-mm-dd; document type format: 0 (raw integer)
            msSyncUrlRetrieveByPeriod = syncHost + SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_PUR_DOC, SSwapConsts.CFG_ATT_URL); // complementar la URL
            msSyncUrlRetrieveByWeek = msSyncUrlRetrieveByPeriod.substring(0, msSyncUrlRetrieveByPeriod.indexOf("?") + 1);
            
            // documents download service: /api/documents/download-docs-zip/
            msSyncUrlDownload = syncHost + SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_PUR_DOC_DWNLD, SSwapConsts.CFG_ATT_URL); // complementar la URL

            if (msSyncToken.isEmpty()) {
                msSyncToken = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_PUR_DOC, SSwapConsts.CFG_ATT_TOKEN); // recuperar token específico
            }

            if (msSyncApiKey.isEmpty()) {
                msSyncApiKey = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_PUR_DOC, SSwapConsts.CFG_ATT_API_KEY); // recuperar API key específica
            }

            mnSyncLimit = SLibUtils.parseInt(SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_PUR_DOC, SSwapConsts.CFG_ATT_LIMIT));
            
            // Instanciar prepared statements:
            
            moPrepStatToCountImports = SImportUtils.createPreparedStatementToCountImports(miClient.getSession().getStatement());
            moPrepStatToGetProcessedDpsByExternalId = SImportedDocument.createPrepStatementToGetProcessedDpsByExternalId(miClient.getSession().getStatement());
            moPrepStatToGetDpsKeyByDocData = SImportedDocument.createPrepStatementToGetDpsKeyByDocData(miClient.getSession().getStatement(), SDataConstantsSys.TRNU_TP_DPS_PUR_INV);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        moIconEdit = new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"));
        moIconSave = new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif"));
    }
    
    private boolean isMassAccountingElegible() {
        return mnShowingDocsMode == ON && moRadDocModeCase.isSelected() && (moKeyDocModeCase.getValue()[0] == SImportedDocument.DOC_CASE_RAW_MAT_FREIGHT || moKeyDocModeCase.getValue()[0] == SImportedDocument.DOC_CASE_RAW_MAT_PURCHASE);
    }
    
    private boolean isDocAlreadyRecorded(final SImportedDocument document, boolean refreshDocumentsGrid) throws Exception {
        boolean isRecorded = document.isRecorded();
        
        if (!isRecorded) {
            int[] dpsKey = SImportedDocument.getDpsKeyByDocData(moPrepStatToGetDpsKeyByDocData, document.BizPartnerId, SLibTimeUtils.convertToDateOnly(document.Date), document.NumberSeries, document.Number, document.Total, document.CurrencyId);

            if (dpsKey != null) {
                isRecorded = true;

                String dpsNumber = SThinDps.readDpsNumber(dpsKey, miClient.getSession().getStatement());

                if (miClient.showMsgBoxConfirm("Se encontró la factura " + SSwapConsts.SIIE + " '" + dpsNumber + "' de " + document.BizPartner + ".\n"
                        + "¿Desea vincularla a esta factura autorizada?") == JOptionPane.YES_OPTION) {
                    if (document.link(miClient.getSession(), msSyncUrlDownload, dpsKey, SImportedDocument.MATCH_PAY_TP_CONF_DIFF, false, false, false, false) && refreshDocumentsGrid) {
                        refreshDocumentsGrid();
                    }
                }
            }
        }
        
        return isRecorded;
    }
    
    private void populateDocumentsGrid(final ArrayList<SImportedDocument> documents, final boolean focusDocumentsGrid) {
        Collections.sort(documents);
        
        moDocumentsGrid.populateGrid(new Vector<>(documents), this);
        moDocumentsGrid.getTable().getTableHeader().setReorderingAllowed(true);
        moDocumentsGrid.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moDocumentsGrid.setSelectedGridRow(0);
        
        if (focusDocumentsGrid) {
            moDocumentsGrid.getTable().requestFocusInWindow();
        }
        
        jlStatus.setText("Facturas autorizadas elegibles: " + SLibUtils.DecimalFormatInteger.format(maDocuments.size()) + "; mostradas: " + SLibUtils.DecimalFormatInteger.format(documents.size()));
    }
    
    public void reloadDocumentsGrid() {
        mbDocumentsBeingReloaded = true;
        
        if (moRadDocModeType.isSelected()) {
            itemStateChangedDocType(false); // reloads documents grid
        }
        else if (moRadDocModeCase.isSelected()) {
            itemStateChangedDocCase(false); // reloads documents grid
        }
        
        mbDocumentsBeingReloaded = false;
    }
    
    private void refreshDocumentsGrid() {
        mbDocumentsBeingRefreshed = true;
        
        int index = moDocumentsGrid.getTable().getSelectedRow();
        moDocumentsGrid.renderGridRows();
        moDocumentsGrid.setSelectedGridRow(index);
        
        renderCurrentDoc(false);
        
        mbDocumentsBeingRefreshed = false;
    }
    
    private SDataDps readOrderAndPrepareDialogDpsFinder(final SImportedDocument document) throws Exception {
        SDataDps order = null;
        boolean linkToOrder = document.hasReferences(SSwapConsts.TXN_REF_TYPE_ORDER);

        if (linkToOrder) {
            int[] orderKey = document.getFirstReferenceKey(miClient, SSwapConsts.TXN_REF_TYPE_ORDER);

            if (orderKey != null) {
                order = new SDataDps();
                if (order.read(orderKey, miClient.getSession().getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\n(Orden de compra PK " + SLibUtils.textKey(orderKey) + ".)");
                }
            }
        }

        // prepare DPS finder dialog:

        if (linkToOrder && order == null && moDialogDpsFinder == null) {
            moDialogDpsFinder = new SDialogDpsFinder((SClientInterface) miClient, SDataConstants.TRNX_DPS_PEND_LINK);
        }
        
        return order;
    }
    
    private void handleExceptionWhenShowingDocs(final Exception e) {
        System.err.println(e);
        SLibUtils.showException(this, e);
        
        actionPerformedClearDocs();
        jbShowDocs.requestFocusInWindow();
    }
    
    private void disableFieldsToSearchDocs() {
        moRadSearchByPeriod.setEnabled(false);
        moDatePeriodStart.setEditable(false);
        moDatePeriodEnd.setEditable(false);
        
        moRadSearchByWeek.setEnabled(false);
        moCalWeekYear.setEditable(false);
        moCalWeekStart.setEditable(false);
        moCalWeekEnd.setEditable(false);
        
        moBoolExcludeRecorded.setEnabled(false);
        
        jbShowDocs.setEnabled(false);
    }
    
    private void disableFieldsWhenRegisteringDocs() {
        moRadDocModeType.setEnabled(false);
        moRadDocModeCase.setEnabled(false);
        
        moKeyDocModeType.setEnabled(false);
        moKeyDocModeCase.setEnabled(false);
        
        jbShowDocs.setEnabled(false);
        jbClearDocs.setEnabled(false);
        
        jbSelectRemainingDocs.setEnabled(false);
        jbSelectAllDocs.setEnabled(false);
        jbDeselectAllDocs.setEnabled(false);
        
        jbDownloadSelectedDocs.setEnabled(false);
        jbRecordAllDocs.setEnabled(false);
        jbLinkAllDocs.setEnabled(false);
    }
    
    private void enableFieldsOfSearchBy() {
        boolean isShowingDocsModeOff = mnShowingDocsMode == OFF;
        
        boolean isSearchByPeriod = moRadSearchByPeriod.isSelected();
        moDatePeriodStart.setEditable(isShowingDocsModeOff && isSearchByPeriod);
        moDatePeriodEnd.setEditable(isShowingDocsModeOff && isSearchByPeriod);
        
        boolean isSearchByWeek = moRadSearchByWeek.isSelected();
        moCalWeekYear.setEditable(isShowingDocsModeOff && isSearchByWeek);
        moCalWeekStart.setEditable(isShowingDocsModeOff && isSearchByWeek);
        moCalWeekEnd.setEditable(isShowingDocsModeOff && isSearchByWeek);
    }
    
    private void enableFieldsOfDocMode() {
        boolean isShowingDocsModeOn = mnShowingDocsMode == ON;
        
        boolean isDocModeType = moRadDocModeType.isSelected();
        moKeyDocModeType.setEnabled(isShowingDocsModeOn && isDocModeType);
        if (!isDocModeType) {
            moKeyDocModeType.setValue(new int[] { SImportedDocument.DOC_TYPE_ALL });
        }

        boolean isDocModeCase = moRadDocModeCase.isSelected();
        moKeyDocModeCase.setEnabled(isShowingDocsModeOn && isDocModeCase);
        if (!isDocModeCase) {
            moKeyDocModeCase.setValue(new int[] { SImportedDocument.DOC_CASE_ALL });
        }
    }
    
    private void enableFieldsForShowingDocs(final boolean setShowingDocsModeOn) {
        mnShowingDocsMode = setShowingDocsModeOn ? ON : OFF;
        
        // START OF item-state-chage events free section if mbDocumentsBeingUpdated is true:
        
        if (!setShowingDocsModeOn) {
            bgDocMode.setSelected(moRadDocModeType.getModel(), true);
            moKeyDocModeType.setValue(new int[] { SImportedDocument.DOC_TYPE_ALL });
            moKeyDocModeCase.setValue(new int[] { SImportedDocument.DOC_CASE_ALL });
        }
        
        // END OF item-state-chage events free section if mbDocumentsBeingUpdated is true:
        
        moRadSearchByPeriod.setEnabled(!setShowingDocsModeOn);
        moRadSearchByWeek.setEnabled(!setShowingDocsModeOn);
        enableFieldsOfSearchBy();
        
        moRadDocModeType.setEnabled(setShowingDocsModeOn);
        moRadDocModeCase.setEnabled(setShowingDocsModeOn);
        enableFieldsOfDocMode();
        
        moBoolExcludeRecorded.setEnabled(!setShowingDocsModeOn);
        
        jbShowDocs.setEnabled(!setShowingDocsModeOn);
        jbClearDocs.setEnabled(setShowingDocsModeOn);
        
        jbSelectRemainingDocs.setEnabled(setShowingDocsModeOn);
        jbSelectAllDocs.setEnabled(setShowingDocsModeOn);
        jbDeselectAllDocs.setEnabled(setShowingDocsModeOn);
        
        jbDownloadSelectedDocs.setEnabled(setShowingDocsModeOn);
        jbRecordAllDocs.setEnabled(setShowingDocsModeOn && isMassAccountingElegible());
        jbLinkAllDocs.setEnabled(setShowingDocsModeOn);
    }
    
    private void enableEditingReqPayAmount(final boolean enable) {
        // set according status in dialog:
        
        if (enable) {
            disableFieldsWhenRegisteringDocs();
        }
        else {
            enableFieldsForShowingDocs(true);
        }
        
        moDocumentsGrid.getTable().setEnabled(!enable);
        
        jbCreateInvoiceFromCfdi.setEnabled(!enable);
        jbCreateInvoiceFromScratch.setEnabled(!enable);
        jbLinkInvoice.setEnabled(!enable);
        jbUnlinkInvoice.setEnabled(!enable);
        jbRejectInvoice.setEnabled(!enable);
        jbViewInvoicePdf.setEnabled(!enable);
        jbViewInvoice.setEnabled(!enable);
        jbViewOrder.setEnabled(!enable);
        jbViewRecord.setEnabled(!enable);
        
        moBoolReqPayRequire.setEnabled(!enable);
        jbChangeReqPayRequiredDate.setEnabled(!enable);
        jbRequestPayment.setEnabled(!enable);
        jbChangePayRequiredDate.setEnabled(!enable);
        jbChangePayScheduledDate.setEnabled(!enable);        
        
        // set according status in edition field and controls:
        
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
    
    private void exportPaymentRequestsIfNeeded() {
        if (moBoolExportPaymentRequestsOnClose.isSelected()) {
            if (!mbExportPaymentRequests) {
                // one last check:
                for (SGridRow row : moDocumentsGrid.getModel().getGridRows()) {
                    SImportedDocument document = (SImportedDocument) row;
                    if (document.isPaymentRequested() && SLibUtils.belongsTo(document.Payment.getFkStatusPaymentId(), new int[] { SModSysConsts.FINS_ST_PAY_NEW, SModSysConsts.FINS_ST_PAY_SCHED_P })) {
                        mbExportPaymentRequests = true;
                        break;
                    }
                }
            }

            if (mbExportPaymentRequests) {
                // export payment requests to SWAP Services:
                try {
                    miClient.getFrame().getRootPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    SResponses responses = SExportUtils.exportData(miClient.getSession(), SSyncType.PUR_PAYMENT, true, SExportUtils.EXPORT_MODE_CONFIRM);
                    SExportUtils.processResponses(miClient.getSession(), responses, 0, 0);
                }
                catch (Exception e) {
                    SLibUtilities.printOutException(this, e);
                }
                finally {
                    miClient.getFrame().getRootPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        }
    }
    
    private void initProgress(final String message) {
        jlProgress.setText(message);
        
        jProgressBar.setValue(0);
        jProgressBar.setStringPainted(false);
        jProgressBar.setIndeterminate(true);
    }
    
    private void startProgress(final String message) {
        jlProgress.setText(message);
        
        jProgressBar.setValue(0);
        jProgressBar.setStringPainted(true);
        jProgressBar.setIndeterminate(false);
    }
    
    private void clearProgress() {
        jlProgress.setText("");
        
        jProgressBar.setValue(0);
        jProgressBar.setStringPainted(false);
        jProgressBar.setIndeterminate(false);
    }
    
    private String formatFunctionalSubAreasCodes() {
        String formatedCodes = "";
        String[] codes = msUserFunctionalSubAreaCodes.split(", ");
        
        for (int i = 0; i < codes.length; i++) {
            String br = "";
            
            if (i > 0 && (i % FUNC_SUB_AREA_CODES_PER_LINE == 0)) {
                br = "\n" + SLibUtils.textRepeat(" ", 3); // indent of 3 blank spaces
            }
            
            formatedCodes += (formatedCodes.isEmpty() ? "" : ", ") + br + codes[i];
        }
        
        return formatedCodes;
    }
    
    private Settings createSettings() {
        Settings settings = new Settings(jtfUserName.getText(), jtfUserFuncSubAreas.getText(), moKeyDocModeCase.getValue()[0],
                msSyncUrlDownload, moPrepStatToGetDpsKeyByDocData);
        
        if (moRadSearchByPeriod.isSelected()) {
            settings.setSearchByPeriod(moDatePeriodStart.getValue(), moDatePeriodEnd.getValue());
        }
        else if (moRadSearchByWeek.isSelected()) {
            settings.setSearchByWeek(moCalWeekYear.getValue(), moCalWeekStart.getValue(), moCalWeekEnd.getValue());
        }
        
        return settings;
    }
    
    private void backgroundProcessForShowingDocs(final HttpURLConnection connection, final SProgressCallback callback) throws Exception {
        Exception exception = null;
        
        try {
            int countRetreived = 0;
            int countElegible = 0;
            int countShown = 0;
            int companyId = miClient.getSession().getConfigCompany().getCompanyId();

            try (InputStream is = connection.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(is);

                if (root.isArray()) {
                    startProgress("Procesando " + (root.size() == 1 ? "1 factura" : root.size() + " facturas") + "...");
                            
                    for (JsonNode docNode : root) {
                        callback.onProgress((int) ((++countRetreived / (double) root.size()) * 100));

                        JsonNode companyNode = docNode.path("company");

                        if (companyNode.get("external_id").asInt() == companyId &&
                                docNode.get("transaction_class").asInt() == SSwapConsts.TXN_CAT_PURCHASE &&
                                docNode.get("document_type").asInt() == SSwapConsts.TXN_DOC_TYPE_INVOICE) {
                            countElegible++;

                            int externalDocumentId = docNode.get("id").asInt();

                            JsonNode functionalAreaNode = docNode.path("functional_area");
                            int functionalSubAreaId = functionalAreaNode.get("external_id").asInt();

                            if (SDbFunctionalSubArea.belongsToFunctionalSubAreas(maFunctionalSubAreas, functionalSubAreaId)) {
                                int countOfImports = SImportUtils.countImports(moPrepStatToCountImports, SDbComImportLog.SYNC_TYPE_PUR_INV, "" + SHttpConsts.RSC_SUCC_OK, miClient.getSession().getUser().getPkUserId(), "" + externalDocumentId);

                                SImportedDocument document = new SImportedDocument(moServicesConfigSettings);

                                document.ExternalDocumentId = externalDocumentId;
                                document.retrieveProcessing(miClient.getSession(), moPrepStatToGetProcessedDpsByExternalId, SDbSwapDataProcessing.DATA_TYPE_INV, SDataConstantsSys.TRNS_CT_DPS_PUR, document.ExternalDocumentId);

                                if (!moBoolExcludeRecorded.isSelected() || !document.isRecorded()) {
                                    if (docNode.has("uuid") && !docNode.path("uuid").isNull()) {
                                        document.ExternalDocumentUuid = docNode.path("uuid").asText();
                                    }
                                    else {
                                        document.ExternalDocumentUuid = "";
                                    }

                                    JsonNode partnerNode = docNode.path("partner");
                                    document.BizPartnerId = partnerNode.get("external_id").asInt();
                                    document.BizPartner = partnerNode.get("full_name").asText();

                                    document.NumberSeries = docNode.get("series").asText();
                                    document.Number = docNode.get("number").asText();

                                    if (document.NumberSeries.isEmpty() && document.Number.isEmpty() && document.ExternalDocumentUuid.isEmpty()) {
                                        document.Number = docNode.get("folio").asText();
                                    }

                                    document.Date = SLibUtils.IsoFormatDate.parse(docNode.get("date").asText());
                                    
                                    String dueDateAsText = docNode.has("due_date") && !docNode.path("due_date").isNull() ? docNode.get("due_date").asText() : "";
                                    document.DueDate = dueDateAsText == null || dueDateAsText.isEmpty() || dueDateAsText.equals("null") ? null : SLibUtils.IsoFormatDate.parse(dueDateAsText);

                                    JsonNode referencesNode = docNode.path("references");
                                    if (referencesNode.isArray()) {
                                        ArrayList<SImportedDocument.Reference> references = new ArrayList<>();

                                        for (JsonNode referenceNode : referencesNode) {
                                            int referenceType = referenceNode.get("document_ref_type").asInt();
                                            String reference = referenceNode.get("reference").asText();
                                            SImportUtils.DpsKey dpsKey = SImportUtils.createDpsKey(referenceNode.get("external_id").asText()); // e.g., "2025_1"

                                            references.add(new SImportedDocument.Reference(referenceType, reference, dpsKey));
                                        }

                                        if (!references.isEmpty()) {
                                            document.References = references.toArray(new SImportedDocument.Reference[0]);

                                            document.ReferencesType = references.get(0).ReferenceType; // PLEASE NOTE THAT: reference type will be that of the first reference!
                                            document.ReferencesAsText = document.composeReferences();
                                        }
                                    }

                                    document.Description = docNode.get("notes").asText();
                                    document.AccountingTag = docNode.has("account_tag") && !docNode.path("account_tag").isNull() ? docNode.get("account_tag").asText() : "";

                                    document.FunctionalSubAreaId = functionalSubAreaId;
                                    document.FunctionalSubArea = functionalAreaNode.get("name").asText();

                                    document.FiscalUseCode = docNode.get("fiscal_use").asText();

                                    document.Total = SLibUtils.parseDouble(docNode.get("amount").asText());

                                    JsonNode currencyNode = docNode.path("currency");
                                    document.CurrencyId = SSwapUtils.getCurrencyId(currencyNode.get("id").asInt());
                                    document.CurrencyCode = currencyNode.get("code").asText();

                                    int requiredPaymentDefinition = docNode.has("payment_definition") ? docNode.get("payment_definition").asInt() : SSwapConsts.PAY_NOT_REQ;
                                    double requiredPaymentAmount = docNode.has("payment_amount") ? SLibUtils.parseDouble(docNode.get("payment_amount").asText()) : 0d;
                                    double requiredPaymentPct = SLibUtils.parseDouble(docNode.get("payment_percentage").asText());
                                    String requiredPaymentDateAsText = docNode.has("payment_date") && !docNode.path("payment_date").isNull() ? docNode.get("payment_date").asText() : "";
                                    Date requiredPaymentDate = requiredPaymentDateAsText == null || requiredPaymentDateAsText.isEmpty() || requiredPaymentDateAsText.equals("null") ? null : SLibUtils.IsoFormatDate.parse(requiredPaymentDateAsText);

                                    if (requiredPaymentDate == null && requiredPaymentPct == 0) {
                                        document.RequirePayment = false;
                                        document.RequiredPaymentDefinition = SSwapConsts.PAY_NOT_REQ;
                                        document.RequiredPaymentPct = 0;
                                        document.RequiredPaymentAmount = 0;
                                        document.RequiredPaymentDate = null;
                                        document.IsRequiredPaymentLoc = false;
                                        document.RequiredPaymentNotes = docNode.get("payment_notes").asText();
                                    }
                                    else {
                                        document.RequirePayment = true;
                                        document.RequiredPaymentDefinition = requiredPaymentDefinition != SSwapConsts.PAY_NOT_REQ ? requiredPaymentDefinition : (requiredPaymentPct > 0 ? SSwapConsts.PAY_DEF_BY_PCT : SSwapConsts.PAY_DEF_BY_AMT);
                                        document.RequiredPaymentPct = requiredPaymentPct;
                                        document.RequiredPaymentAmount = requiredPaymentAmount;
                                        document.RequiredPaymentDate = requiredPaymentDate;
                                        document.IsRequiredPaymentLoc = docNode.get("is_payment_loc").asBoolean();
                                        document.RequiredPaymentNotes = docNode.get("payment_notes").asText();
                                    }

                                    String revisionDatetimeAsText = docNode.has("date_week_revision") ? docNode.get("date_week_revision").asText() : docNode.get("authorized_at").asText();
                                    Date revisionDatetime = docNode.path("date_week_revision").isNull() || revisionDatetimeAsText == null || revisionDatetimeAsText.isEmpty() || revisionDatetimeAsText.equals("null") ? null : SSwapUtils.SwapDatetimeMicrosecsTimeZoneFormat.parse(revisionDatetimeAsText.replaceFirst("(\\.\\d{3})\\d+", "$1")); // trunc microsecontds to milliseconds

                                    document.RevisionYear = docNode.get("year_week_revision").asInt();
                                    document.RevisionWeek = docNode.get("number_week_revision").asInt();
                                    document.RevisionDatetime = revisionDatetime;
                                    document.Priority = docNode.get("priority").asInt();
                                    document.ProcessingTypeId = docNode.get("processing_type_id").asInt();
                                    document.ProcessingTypeCode = SDbSwapDataProcessing.ProcTypes.get(document.ProcessingTypeId);
                                    document.StatusId = 0;
                                    document.Status = "";
                                    document.Download = false;
                                    document.AlreadyDownloaded = countOfImports > 0;

                                    maDocuments.add(document);
                                    countShown++;
                                }
                            }
                        }
                    }
                }
                
                callback.onProgress(100); // assure to show 100%
                
                String range = "";

                if (moRadSearchByPeriod.isSelected()) {
                    range = (SLibTimeUtils.isSameDate(moDatePeriodStart.getValue(), moDatePeriodEnd.getValue()) ?
                            ("Día:\n- " + SLibUtils.DateFormatDate.format(moDatePeriodStart.getValue())) :
                            ("Período:\n- del " + SLibUtils.DateFormatDate.format(moDatePeriodStart.getValue()) + " al " + SLibUtils.DateFormatDate.format(moDatePeriodEnd.getValue())));
                }
                else if (moRadSearchByWeek.isSelected()) {
                    range = "Año:\n- " + moCalWeekYear.getValue() + "\n" +
                            (Objects.equals(moCalWeekStart.getValue(), moCalWeekEnd.getValue()) ?
                            ("Semana:\n- " + SLibUtils.DecimalFormatCalendarWeek.format(moCalWeekStart.getValue())) :
                            ("Semana:\n- de la " + SLibUtils.DecimalFormatCalendarWeek.format(moCalWeekStart.getValue()) + " a la " + SLibUtils.DecimalFormatCalendarWeek.format(moCalWeekEnd.getValue())));
                }

                String message = "Resumen de la búsqueda de facturas autorizadas en " + SSwapConsts.PURCHASE_PORTAL + ":\n\n"
                        + "Empresa actual:\n- " + msCompanyName + ".\n"
                        + "Subáreas funcionales del usuario actual:\n- " + formatFunctionalSubAreasCodes() + ".\n"
                        + range + ".\n\n";

                message += "Búsqueda de facturas autorizadas:\n";

                if (countRetreived == 0) {
                    message += "- ¡No se encontraron facturas autorizadas!";

                    miClient.showMsgBoxWarning(message);
                }
                else {
                    if (countRetreived != countElegible) {
                        message += "- Facturas autorizadas totales: " + countRetreived + ";\n"; // this case should not happen
                    }

                    message += "- Facturas autorizadas de la empresa actual: " + countElegible + ";\n"
                            + "- Facturas autorizadas elegibles al usuario actual: " + countShown + ".";

                    miClient.showMsgBoxInformation(message);
                }

                enableFieldsForShowingDocs(true);
                populateDocumentsGrid(maDocuments, !maDocuments.isEmpty());
                renderCurrentDoc(false);
            }
        }
        catch (Exception e) {
            exception = e;
            handleExceptionWhenShowingDocs(e);
        }
        finally {
            mbDocumentsBeingProcessed = false; // enables item state change events from being handled again!
            
            if (exception != null) {
                throw exception;
            }
        }
    }
    
    private void backgroundProcessForDownloadingDocs(final ArrayList<Integer> documents, final SProgressCallback callback) {
        int docsDownloaded = 0;
        ArrayList<Integer> externalIdsDownloaded = new ArrayList<>();
        
        try {
            // prepare GUI:
            
            mbDocumentsBeingProcessed = true;

            moDocumentsGrid.getTable().setEnabled(false);
            renderCurrentDoc(true); // forcing clearing!

            disableFieldsWhenRegisteringDocs();
            
            // process download:
            
            startProgress("Descargando " + (documents.size() == 1 ? "una factura" : SLibUtils.DecimalFormatInteger.format(documents.size()) + " facturas") + "...");
            
            ArrayList<List<Integer>> documentsBatches = new ArrayList<>();
            int batches = (documents.size() + BATCH_DOWNLOADS - 1)/ BATCH_DOWNLOADS; // ceilling division
            
            for (int i = 0; i < batches; i++) {
                int from = i * BATCH_DOWNLOADS;
                int to = (i + 1) * BATCH_DOWNLOADS;
                        
                documentsBatches.add(documents.subList(from, to < documents.size() ? to : documents.size()));
            }
            
            int zipsSaved = 0;
            String zipPaths = "";
            File desiredZipFile = null;
            boolean download = documentsBatches.size() == 1;
            
            if (documentsBatches.size() > 1) {
                // get the desired ZIP file once when there are several download batches:
                desiredZipFile = SImportUtils.chooseDownloadZipFile(miClient.getSession(), SSwapConsts.TXN_DOC_TYPE_INVOICE);
                download = desiredZipFile != null;
            }
            
            if (download) {
                for (int zipBatch = 1; zipBatch <= documentsBatches.size(); zipBatch++) {
                    int docs = documentsBatches.get(zipBatch - 1).size();
                    
                    try {
                        ArrayList<Integer> documentsBatch = new ArrayList<>(documentsBatches.get(zipBatch - 1));
                        File[] files = SImportUtils.downloadDocumentsAllFilesAsZip(miClient.getSession(), msSyncUrlDownload, documentsBatch, SSwapConsts.TXN_DOC_TYPE_INVOICE, desiredZipFile, zipBatch);

                        if (files != null) {
                            zipsSaved++;
                            docsDownloaded += docs;
                            externalIdsDownloaded.addAll(documentsBatch);
                            
                            File zipFile = files[SImportUtils.DOC_FILES_ZIP_IDX];
                            zipPaths += (!zipPaths.isEmpty() ? "\n" : "") + "+ " + zipFile.getAbsolutePath();
                            System.out.println("ZIP #" + zipBatch + " saved to: " + zipFile.getAbsolutePath());
                        }
                    }
                    catch (Exception e) {
                        int remaining = documentsBatches.size() - zipBatch;
                        String warning = "Ocurrió un problema con la descarga del bloque #" + zipBatch + " de facturas, "
                                + "que debería contener " + (docs == 1 ? "una factura" : docs + " facturas") + ".\n"
                                + (remaining > 0  ? "Se proseguirá con la descarga " + (remaining == 1 ? "del bloque restante" : "de los " + remaining + " bloques restantes") + ".\n" : "")
                                + "\n" + e;
                        miClient.showMsgBoxWarning(warning);
                        SLibUtils.printException(this, e);
                    }
                    finally {
                        callback.onProgress((int) ((zipBatch / (double) documentsBatches.size()) * 100)); // reveal progress after the long-taking previous task
                    }
                }

                callback.onProgress(100); // assure to show 100%

                // inform about the download process:
                
                if (docsDownloaded == 0) {
                    String warning = "¡No se " + (documents.size() == 1 ?
                            "descargaron los archivos de la factura autorizada seleccionada" :
                            "descargó ninguno de los archivos de las " + SLibUtils.DecimalFormatInteger.format(documents.size()) + " facturas autorizadas seleccionadas")
                            + "!";
                    miClient.showMsgBoxWarning(warning);
                }
                else {
                    String message = "";
                    
                    if (docsDownloaded == documents.size()) {
                        message = "Se descargaron " + (documents.size() == 1 ? "" : "todos ") + "los archivos de " + (documents.size() == 1 ?
                                "la factura autorizada seleccionada" :
                                "las " + SLibUtils.DecimalFormatInteger.format(documents.size()) + " facturas autorizadas seleccionadas")
                                + ".";
                    }
                    else {
                        message = "Solamente se descargaron los archivos de " + (docsDownloaded == 1 ?
                                "una" :
                                "" + docsDownloaded)
                                + " de las " + SLibUtils.DecimalFormatInteger.format(documents.size()) + " facturas autorizadas seleccionadas.";
                    }
                    
                    message += "\n" + (zipsSaved == 1 ? "El archivo ZIP fue guardado en" : "Los " + zipsSaved + " archivos ZIP fueron guardados en") + ":"
                            + "\n" + zipPaths;
                    miClient.showMsgBoxInformation(message);
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            // refresh documents grid:

            if (docsDownloaded > 0) {
                for (SGridRow row : moDocumentsGrid.getModel().getGridRows()) {
                    SImportedDocument document = (SImportedDocument) row;

                    if (document.Download && !document.AlreadyDownloaded) {
                        for (Integer externalId : externalIdsDownloaded) {
                            if (externalId == document.ExternalDocumentId) {
                                document.AlreadyDownloaded = true;
                                break;
                            }
                        }
                    }
                }

                refreshDocumentsGrid();
            }
            
            // restore GUI:
            
            clearProgress();
            enableFieldsForShowingDocs(true);
            
            moDocumentsGrid.getTable().setEnabled(true);
            renderCurrentDoc(false);
                                    
            mbDocumentsBeingProcessed = false;
        }
    }
    
    @SuppressWarnings("unchecked")
    private void backgroundProcessForRecordingDocs(final ArrayList<SImportedDocument> recordableDocs, final SProgressCallback callback) {
        ArrayList<SImportedDocument> rejectedInvoices = null;
        
        try {
            mbDocumentsBeingProcessed = true;

            moDocumentsGrid.getTable().setEnabled(false);
            renderCurrentDoc(true); // forcing clearing!

            disableFieldsWhenRegisteringDocs();

            startProgress("Revisando " + (recordableDocs.size() == 1 ? "1 factura" : recordableDocs.size() + " facturas") + "...");
            
            int countProcessed = 0;
            int docsJustRecorded = 0;
            int docsAlreadyRecorded = 0;
            int bizPartnersUnknown = 0;
            int bizPartnersForeign = 0;
            int docsOrderReferenced = 0;
            int docsMissingXrt = 0;
            int docsMissingFiles = 0;
            int docsMissingFileXml = 0;
            int docsMissingFilePdf = 0;
            HashMap<Integer, Double> todayExchangeRates = new HashMap<>(); // key: currencyId; value: exchange rate (when available) or NaN (when not available)
            ArrayList<SImportedDocument> elegibleDocs = new ArrayList<>();

            for (SImportedDocument document : recordableDocs) {
                callback.onProgress((int) ((++countProcessed / (double) recordableDocs.size()) * 100));
                
                // check that document is not already recorded:
                
                boolean previouslyRecorded = document.isRecorded();

                if (previouslyRecorded || isDocAlreadyRecorded(document, false)) {
                    if (!previouslyRecorded && document.isRecorded()) {
                        docsJustRecorded++;
                    }
                    
                    docsAlreadyRecorded++;
                }
                else {
                    // check that document's business partner is domestic:

                    Boolean isBizPartnerDomestic = SDataBizPartner.checkIsDomestic(document.BizPartnerId, (SClientInterface) miClient);

                    if (isBizPartnerDomestic == null) {
                        bizPartnersUnknown++;
                    }
                    else if (!isBizPartnerDomestic) {
                        bizPartnersForeign++;
                    }
                    else {
                        // check that document does not have references of type order:
                        
                        boolean isReferenced = document.hasReferences(SSwapConsts.TXN_REF_TYPE_ORDER);
                        
                        if (isReferenced) {
                            docsOrderReferenced++;
                        }
                        else {
                            // check that document's exchange rate, if needed, is available for today:

                            boolean exchangeRateOk = true;

                            // validate availability of exchange rates, if needed:

                            if (!miClient.getSession().getSessionCustom().isLocalCurrency(new int[] { document.CurrencyId })) {
                                Double exchangeRate = todayExchangeRates.get(document.CurrencyId);

                                if (exchangeRate == null) {
                                    try {
                                        exchangeRate = SDocumentUtils.getExchangeRate(miClient.getSession(), document.CurrencyId, miClient.getSession().getCurrentDate()); // throws exception if exchange rate is unavailable
                                    }
                                    catch (Exception e) {
                                        exchangeRate = Double.NaN;
                                        SLibUtils.printException(this, e);
                                    }
                                    finally {
                                        todayExchangeRates.put(document.CurrencyId, exchangeRate);
                                    }
                                }

                                if (exchangeRate == Double.NaN) {
                                    exchangeRateOk = false;
                                }
                            }

                            if (!exchangeRateOk) {
                                docsMissingXrt++;
                            }
                            else {
                                // retrieve CFDI files:

                                boolean filesOk = document.AuxFiles != null && document.AuxFiles.length == SImportUtils.CFDI_FILES && document.AuxFiles[SImportUtils.CFDI_XML_IDX] != null && document.AuxFiles[SImportUtils.CFDI_PDF_IDX] != null;

                                if (!filesOk) {
                                    File[] files = document.retrieveFiles(miClient.getSession(), msSyncUrlDownload);

                                    if (files == null || files.length != SImportUtils.CFDI_FILES) {
                                        docsMissingFiles++;
                                    }
                                    else if (files[SImportUtils.CFDI_XML_IDX] == null) {
                                        docsMissingFileXml++;
                                    }
                                    else if (files[SImportUtils.CFDI_PDF_IDX] == null) {
                                        docsMissingFilePdf++;
                                    }
                                    else {
                                        filesOk = true;
                                    }
                                }

                                if (filesOk) {
                                    elegibleDocs.add(document);
                                }
                            }
                        }
                    }
                }
            }

            callback.onProgress(100); // assure to show 100%
            
            // inform about the processing:
            
            if (elegibleDocs.size() < recordableDocs.size()) {
                String message = "";

                if (elegibleDocs.isEmpty()) {
                    message = "¡No hay facturas autorizadas que se puedan contabilizar!\n\n";
                }
                else {
                    if (recordableDocs.size() == 1) {
                        message = "¡Ninguna factura autorizada se puede contabilizar!\n\n";
                    }
                    else {
                        message = "¡No todas las facturas autorizadas se pueden contabilizar!\n\n";
                    }
                }

                if (recordableDocs.size() == 1) {
                    message += "La única factura autorizada disponible para contabilizar:";

                    if (docsAlreadyRecorded > 0) {
                        message += "\n+ Ya está contabilizada.";
                    }
                    if (bizPartnersUnknown > 0) {
                        message += "\n+ Tiene asociado de negocios desconocido.";
                    }
                    if (bizPartnersForeign > 0) {
                        message += "\n+ Es extranjera. (Solo se pueden procesar facturas nacionales.)";
                    }
                    if (docsOrderReferenced > 0) {
                        message += "\n+ Está referenciada a un pedido. (Solo se pueden procesar facturas que no están referenciados a un pedido.)";
                    }
                    if (docsMissingXrt > 0) {
                        message += "\n+ Está en moneda extranjera, pero no hay tipo de cambio para hoy.";
                    }
                    if (docsMissingFiles > 0) {
                        message += "\n+ Carece de archivos XML y PDF.";
                    }
                    if (docsMissingFileXml > 0) {
                        message += "\n+ Carece de archivo XML.";
                    }
                    if (docsMissingFilePdf > 0) {
                        message += "\n+ Carece de archivo PDF.";
                    }
                }
                else {
                    message += "De las " + SLibUtils.DecimalFormatInteger.format(recordableDocs.size()) + " facturas autorizadas disponibles para contabilizar:\n";
                    
                    if (docsAlreadyRecorded > 0) {
                        message += "\n+ " + (docsAlreadyRecorded == 1 ? "Una ya está contabilizada" : SLibUtils.DecimalFormatInteger.format(docsAlreadyRecorded) + " ya están contabilizadas") + ".";
                    }
                    if (bizPartnersUnknown > 0) {
                        message += "\n+ " + (bizPartnersUnknown == 1 ? "Una tiene asociado de negocios desconocido" : SLibUtils.DecimalFormatInteger.format(bizPartnersUnknown) + " tienen asociados de negocios desconocidos") + ".";
                    }
                    if (bizPartnersForeign > 0) {
                        message += "\n+ " + (bizPartnersForeign == 1 ? "Una es extranjera" : SLibUtils.DecimalFormatInteger.format(bizPartnersForeign) + " son extranjeras") + ". (Solo se pueden procesar facturas nacionales.)";
                    }
                    if (docsOrderReferenced > 0) {
                        message += "\n+ " + (docsOrderReferenced == 1 ? "Una está referenciada a un pedido" : SLibUtils.DecimalFormatInteger.format(docsOrderReferenced) + " están referenciadas a un pedido") + ". (Solo se pueden procesar facturas que no están referenciados a un pedido.)";
                    }
                    if (docsMissingXrt > 0) {
                        message += "\n+ " + (docsMissingXrt == 1 ? "Una está" : SLibUtils.DecimalFormatInteger.format(docsMissingXrt) + " están") + " en moneda extranjera, pero no hay tipo de cambio para hoy.";
                    }
                    if (docsMissingFiles > 0) {
                        message += "\n+ " + (docsMissingFiles == 1 ? "Una carece" : SLibUtils.DecimalFormatInteger.format(docsMissingFiles) + " carecen") + " de archivos XML y PDF.";
                    }
                    if (docsMissingFileXml > 0) {
                        message += "\n+ " + (docsMissingFileXml == 1 ? "Una carece" : SLibUtils.DecimalFormatInteger.format(docsMissingFileXml) + " carecen") + " de archivo XML.";
                    }
                    if (docsMissingFilePdf > 0) {
                        message += "\n+ " + (docsMissingFilePdf == 1 ? "Una carece" : SLibUtils.DecimalFormatInteger.format(docsMissingFilePdf) + " carecen") + " de archivo PDF.";
                    }
                }

                if (elegibleDocs.isEmpty()) {
                    miClient.showMsgBoxWarning(message);
                }
                else {
                    message += "\n\n" + (elegibleDocs.size() == 1 ? "Se procesará una factura autorizada." : "Se procesarán " + SLibUtils.DecimalFormatInteger.format(elegibleDocs.size()) + " facturas autorizadas.");
                    miClient.showMsgBoxInformation(message);
                }
            }
            
            if (docsJustRecorded > 0) {
                refreshDocumentsGrid();
            }
                    
            if (!elegibleDocs.isEmpty()) {
                // process documents:
                
                initProgress("Validando estatus SAT de " + (elegibleDocs.size() == 1 ? "1 factura" : elegibleDocs.size() + " facturas") + "...");
                
                if (moDialogMassAccountDocuments == null) {
                    moDialogMassAccountDocuments = new SDialogMassAccountDocuments(miClient);
                }

                moDialogMassAccountDocuments.resetForm();
                moDialogMassAccountDocuments.setValue(SDialogMassAccountDocuments.VALUE_SETTINGS, createSettings());
                moDialogMassAccountDocuments.setValue(SDialogMassAccountDocuments.VALUE_DOCUMENTS_AND_ADVANCES, new Object[] { elegibleDocs, moAdvancesMap });
                
                callback.onProgress(100); // assure to show 100% again
                
                moDialogMassAccountDocuments.setVisible(true);
                
                // check whether payments need to be exported:
                if ((boolean) moDialogMassAccountDocuments.getValue(SDialogMassAccountDocuments.VALUE_EXPORT_PAYMENTS)) {
                    mbExportPaymentRequests = true;
                }
                
                // check whether invoices were rejected:
                rejectedInvoices = (ArrayList<SImportedDocument>) moDialogMassAccountDocuments.getValue(SDialogMassAccountDocuments.VALUE_REJECTED_INVOICES);
                
                // update advances:
                moAdvancesMap.clear();
                moAdvancesMap.putAll((HashMap<Integer, SFinUtilities.Balance[]>) moDialogMassAccountDocuments.getValue(SDialogMassAccountDocuments.VALUE_ADVANCES));
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            clearProgress();
            enableFieldsForShowingDocs(true);
            
            if (rejectedInvoices != null && !rejectedInvoices.isEmpty()) {
                int index = moDocumentsGrid.getTable().getSelectedRow();

                maDocuments.removeAll(rejectedInvoices);
                reloadDocumentsGrid();

                moDocumentsGrid.setSelectedGridRow(index < moDocumentsGrid.getTable().getRowCount() ? index : --index);
            }
            
            moDocumentsGrid.getTable().setEnabled(true);
            renderCurrentDoc(false);
                                    
            mbDocumentsBeingProcessed = false;
        }
    }
    
    private void createAndLinkDps(final SImportedDocument document, final int creationMode) throws Exception {
        if (document.isRecorded()) {
            throw new Exception(SImportedDocument.EXC_DOC_ALREADY_RECORDED_IN_ + document.ProcessedDps.composeRecord() + ".");
        }
        else if (!isDocAlreadyRecorded(document, true)) {
            if (((SClientInterface) miClient).getSessionXXX().getCurrentCompanyBranchId() == 0) {
                throw new Exception(SLibConstants.MSG_ERR_GUI_SESSION_BRANCH); // no branch selected in current user session
            }
            else if (document.checkAdvancesOnUpcommingPaymentRequest(miClient, false)) {
                // validate availability of exchange rate, if needed:

                if (!miClient.getSession().getSessionCustom().isLocalCurrency(new int[] { document.CurrencyId })) {
                    SDocumentUtils.getExchangeRate(miClient.getSession(), document.CurrencyId, miClient.getSession().getCurrentDate()); // throws exception if exchange rate is unavailable
                }

                // retrieve CFDI files:
                File[] files = document.retrieveFiles(miClient.getSession(), msSyncUrlDownload);

                // retrieve order, if available:
                SDataDps order = readOrderAndPrepareDialogDpsFinder(document);
                boolean linkToOrder = document.hasReferences(SSwapConsts.TXN_REF_TYPE_ORDER);

                // creaet DPS:
                
                int[] dpsKey = null;
                
                switch (creationMode) {
                    case CREATE_FROM_CFDI:
                        // import CFDI (dialog DPS Finder should be previously prepared):
                        dpsKey = SImportUtils.importCfdiAndCreateAndSaveDps((SClientInterface) miClient, true, moDialogDpsFinder, files[SImportUtils.CFDI_XML_IDX], files[SImportUtils.CFDI_PDF_IDX], linkToOrder, order, document);
                        break;
                        
                    case CREATE_FROM_SCRATCH:
                        // create CFDI (dialog DPS Finder should be previously prepared):
                        dpsKey = SImportUtils.createAndSaveDps((SClientInterface) miClient, true, moDialogDpsFinder, files[SImportUtils.CFDI_XML_IDX], files[SImportUtils.CFDI_PDF_IDX], linkToOrder, order, document);
                        break;
                        
                    default:
                        // nothing
                }
                
                // link DPS:
                
                if (dpsKey != null) {
                    if (document.link(miClient.getSession(), msSyncUrlDownload, dpsKey, SImportedDocument.MATCH_PAY_TP_CONF_DIFF, false, false, false, true)) {
                        refreshDocumentsGrid();

                        if (document.isPaymentRequested()) {
                            mbExportPaymentRequests = true;
                        }
                    }
                }
            }
        }
    }
    
    private void actionPerformedShowDocs() {
        SGuiValidation validation = null;
        String capacityLimit = "Por eficiencia en el procesamiento de su petición, la consulta está restringida máximo a ";
        
        if (moRadSearchByPeriod.isSelected()) {
            validation = SGuiUtils.validateDateRange(moDatePeriodStart, moDatePeriodEnd);
            
            if (validation.isValid()) {
                if (SLibTimeUtils.countPeriodDays(moDatePeriodStart.getValue(), moDatePeriodEnd.getValue()) > LIMIT_DAYS) {
                    validation.setMessage(capacityLimit + LIMIT_DAYS + " días.");
                    validation.setComponent(moDatePeriodStart.getComponent());
                }
            }
        }
        else if (moRadSearchByWeek.isSelected()) {
            for (SGuiField field : new SGuiField[] { moCalWeekYear, moCalWeekStart, moCalWeekEnd }) {
                validation = field.validateField();
                if (!validation.isValid()) {
                    break;
                }
            }
            
            if (validation.isValid()) {
                if (moCalWeekStart.getValue() > moCalWeekEnd.getValue()) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + moCalWeekStart.getFieldName() + "'" + SGuiConsts.ERR_MSG_FIELD_DATE_LESS_EQUAL + "'" + moCalWeekEnd.getFieldName() + "'.");
                    validation.setComponent(((JSpinner.NumberEditor) moCalWeekStart.getEditor()).getTextField());
                }
                else if (moCalWeekEnd.getValue() - moCalWeekStart.getValue() + 1 > LIMIT_WEEKS) {
                    validation.setMessage(capacityLimit + LIMIT_WEEKS + " semanas.");
                    validation.setComponent(((JSpinner.NumberEditor) moCalWeekStart.getEditor()).getTextField());
                }
            }
        }
        
        if (SGuiUtils.computeValidation(miClient, validation)) {
            try {
                mbDocumentsBeingProcessed = true; // prevents item-state-change events from being handled!
                
                disableFieldsToSearchDocs();
                
                String charset = java.nio.charset.StandardCharsets.UTF_8.name();
                String urlQuery = "";
                
                if (moRadSearchByPeriod.isSelected()) {
                    urlQuery = msSyncUrlRetrieveByPeriod;

                    urlQuery = urlQuery.replace("<" + SSwapConsts.QRY_START_DATE + ">", SLibUtils.IsoFormatDate.format(moDatePeriodStart.getValue()));
                    urlQuery = urlQuery.replace("<" + SSwapConsts.QRY_END_DATE + ">", SLibUtils.IsoFormatDate.format(moDatePeriodEnd.getValue()));
                    urlQuery = urlQuery.replace("<" + SSwapConsts.QRY_DOCUMENT_TYPE + ">", "" + SSwapConsts.TXN_DOC_TYPE_INVOICE);
                }
                else if (moRadSearchByWeek.isSelected()) {
                    urlQuery = msSyncUrlRetrieveByWeek
                            + "year_revision=" + moCalWeekYear.getValue()
                            + "&week_revision_start=" + moCalWeekStart.getValue()
                            + "&week_revision_end=" + moCalWeekEnd.getValue()
                            + "&document_type=" + SSwapConsts.TXN_DOC_TYPE_INVOICE;
                }
                
                urlQuery += "&company_id=" + miClient.getSession().getConfigCompany().getCompanyId();

                URL url = new URL(urlQuery);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setConnectTimeout(SSwapConsts.TIME_30_SEC); // timeout para conectar
                connection.setReadTimeout(SSwapConsts.TIME_30_SEC); // timeout para leer la respuesta
                connection.setRequestMethod(SHttpConsts.METHOD_GET);

                connection.setRequestProperty("Accept-Charset", charset);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");

                if (msSyncToken != null && !msSyncToken.isEmpty()) {
                    connection.setRequestProperty("Authorization", msSyncToken);
                }
                if (msSyncApiKey != null && !msSyncApiKey.isEmpty()) {
                    connection.setRequestProperty("x-api-key", msSyncApiKey);
                }

                connection.setDoInput(true); // true is already the default value!
                
                // prepare to background processing:
                
                initProgress("Preparando la petición...");
                
                // start of background processing...
                
                SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        backgroundProcessForShowingDocs(connection, progress -> {
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
                    }
                };
                
                worker.execute();
                
                // ... end of background processing
            }
            catch (Exception e) {
                handleExceptionWhenShowingDocs(e);
            }
        }
    }
    
    private void actionPerformedClearDocs() {
        try {
            mbDocumentsBeingProcessed = true; // prevents item-state-change events from being handled!
            
            maDocuments.clear();
            moAdvancesMap.clear();
            
            moDocumentsGrid.populateGrid(new Vector<>());
            renderCurrentDoc(true); // forcing clearing!

            enableFieldsForShowingDocs(false);

            jlStatus.setText("");
            clearProgress();
            
            if (moRadSearchByPeriod.isSelected()) {
                moDatePeriodStart.getComponent().requestFocusInWindow();
            }
            else if (moRadSearchByWeek.isSelected()) {
                ((JSpinner.NumberEditor) moCalWeekStart.getEditor()).getTextField().requestFocusInWindow();
            }
        }
        catch (Exception e) {
            System.err.println(e);
            SLibUtils.showException(this, e);
        }
        finally {
            mbDocumentsBeingProcessed = false; // enables item state change events from being handled again!
        }
    }
    
    private void actionPerformedSelectRemainingDocs() {
        for (SGridRow row : moDocumentsGrid.getModel().getGridRows()) {
            if (((SImportedDocument) row).AlreadyDownloaded) {
                ((SImportedDocument) row).Download = false;
            }
            else {
                ((SImportedDocument) row).Download = true;
            }
        }
        
        refreshDocumentsGrid();
    }
    
    private void actionPerformedSelectAllDocs() {
        for (SGridRow row : moDocumentsGrid.getModel().getGridRows()) {
            ((SImportedDocument) row).Download = true;
        }
        
        refreshDocumentsGrid();
    }
    
    private void actionPerformedDeselectAllDocs() {
        for (SGridRow row : moDocumentsGrid.getModel().getGridRows()) {
            ((SImportedDocument) row).Download = false;
        }
        
        refreshDocumentsGrid();
    }
    
    private void actionPerformedDownloadSelectedDocs() {
        ArrayList<Integer> documents = new ArrayList<>();
        
        for (SGridRow row : moDocumentsGrid.getModel().getGridRows()) {
            if (((SImportedDocument) row).Download) {
                documents.add(((SImportedDocument) row).ExternalDocumentId);
            }
        }
        
        if (documents.isEmpty()) {
            miClient.showMsgBoxWarning("Se debe seleccionar al menos una factura autorizada para realizar la descarga de sus archivos.");
        }
        else {
            // prepare to background processing:
            
            initProgress("Preparando la descarga...");
            
            String confirm;
            
            if (documents.size() > BATCH_DOWNLOADS) {
                confirm = "Se descargarán los archivos de las " + SLibUtils.DecimalFormatInteger.format(documents.size()) + " facturas autorizadas seleccionadas, agrupándolos en bloques de " + BATCH_DOWNLOADS + " facturas.";
            }
            else {
                confirm = "Se descargarán los archivos de " + (documents.size() == 1 ? "la factura autorizada seleccionada" : "las " + SLibUtils.DecimalFormatInteger.format(documents.size()) + " facturas autorizadas seleccionadas") + ".";
            }
            
            boolean process = miClient.showMsgBoxConfirm(confirm + "\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
            
            if (process) {
                // start of background processing...

                SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        backgroundProcessForDownloadingDocs(documents, progress -> {
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
                    }
                };

                worker.execute();

                // ... end of background processing
            }
            else {
                clearProgress();
            }
        }
    }
    
    private void actionPerformedRecordAllDocs() {
        if (((SClientInterface) miClient).getSessionXXX().getCurrentCompanyBranchId() == 0) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_SESSION_BRANCH); // no branch selected in current user session
        }
        else if (moDocumentsGrid.getModel().getRowCount() == 0) {
            miClient.showMsgBoxInformation("No hay facturas autorizadas para ser contabilizadas.");
        }
        else {
            ArrayList<SImportedDocument> recordableDocs = new ArrayList<>();

            for (SGridRow row : moDocumentsGrid.getModel().getGridRows()) {
                if (!((SImportedDocument) row).isRecorded()) {
                    recordableDocs.add((SImportedDocument) row);
                }
            }

            if (recordableDocs.isEmpty()) {
                miClient.showMsgBoxInformation("Todas las facturas autorizadas ya están contabilizadas.");
            }
            else {
                // prepare to background processing:
                
                initProgress("Preparando la revisión...");
                
                String confirm;
                
                if (recordableDocs.size() == 1) {
                    confirm = "Se descargarán los archivos XML y PDF del comprobante, y se revisará que:\n";
                    confirm += "la única factura autorizada sin contabilizar se pueda procesar.";
                }
                else {
                    confirm = "Se descargarán los archivos XML y PDF de los comprobantes, y se revisará que:\n";
                    confirm += "las " + SLibUtils.DecimalFormatInteger.format(recordableDocs.size()) + " facturas autorizadas sin contabilizar se puedan procesar.";
                }
                
                boolean process = miClient.showMsgBoxConfirm(confirm + "\nLa descarga y revisión pueden demorar algunos segundos.\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
                
                if (process) {
                    // start of background processing...
                
                    SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {

                        @Override
                        protected Void doInBackground() throws Exception {
                            backgroundProcessForRecordingDocs(recordableDocs, progress -> {
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
                        }
                    };

                    worker.execute();
                    
                    // ... end of background processing
                }
                else {
                    clearProgress(); // restore monitoring of background processing
                }
            }
        }
    }
    
    private void actionPerformedLinkAllDocs() {
        try {
            if (moDocumentsGrid.getModel().getRowCount() == 0) {
                miClient.showMsgBoxInformation("No hay facturas autorizadas.");
            }
            else if (miClient.showMsgBoxConfirm("¿Está seguro que desea vincular todas las facturas autorizadas faltantes con sus respectivas facturas " + SSwapConsts.SIIE + "?") == JOptionPane.YES_OPTION) {
                int unlinked = 0;
                int newlyLinked = 0;
                
                for (SGridRow row : moDocumentsGrid.getModel().getGridRows()) {
                    SImportedDocument document = (SImportedDocument) row;

                    if (!document.isRecorded()) {
                        unlinked++;
                        int[] dpsKey = SImportedDocument.getDpsKeyByDocData(moPrepStatToGetDpsKeyByDocData, document.BizPartnerId, SLibTimeUtils.convertToDateOnly(document.Date), document.NumberSeries, document.Number, document.Total, document.CurrencyId);

                        if (dpsKey != null) {
                            if (document.link(miClient.getSession(), msSyncUrlDownload, dpsKey, SImportedDocument.MATCH_PAY_TP_MAND, false, false, false, false)) {
                                newlyLinked++;
                            }
                        }
                    }
                }

                if (unlinked == 0) {
                    miClient.showMsgBoxInformation("No hay facturas autorizadas sin vincular.");
                }
                else if (unlinked == newlyLinked) {
                    if (unlinked == 1) {
                        miClient.showMsgBoxInformation("Se vinculó la única factura autorizada que faltaba.");
                    }
                    else {
                        miClient.showMsgBoxInformation("Se vincularon todas las " + SLibUtils.DecimalFormatInteger.format(unlinked) + " facturas autorizadas que faltaban.");
                    }
                }
                else {
                    if (unlinked == 1) {
                        miClient.showMsgBoxInformation("La única factura autorizada que falta no se pudo vincular.");
                    }
                    else {
                        if (newlyLinked == 0) {
                            miClient.showMsgBoxInformation("Todas las " + SLibUtils.DecimalFormatInteger.format(unlinked) + " facturas autorizadas que faltan no se pudieron vincular.");
                        }
                        else {
                            String message = "De las " + SLibUtils.DecimalFormatInteger.format(unlinked) + " facturas autorizadas que faltan se ";
                            
                            if (newlyLinked == 1) {
                                message += "pudo vincular 1.";
                            }
                            else {
                                message += "pudieron vincular " + SLibUtils.DecimalFormatInteger.format(newlyLinked) + ".";
                            }
                            
                            miClient.showMsgBoxInformation(message);
                        }
                    }
                }
                
                if (newlyLinked > 0) {
                    moDocumentsGrid.setSelectedGridRow(0);
                    refreshDocumentsGrid();
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedLinkInvoice() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SImportedDocument document = (SImportedDocument) row;
                
                if (document.isRecorded()) {
                    throw new Exception(SImportedDocument.EXC_DOC_ALREADY_RECORDED_IN_ + document.ProcessedDps.composeRecord() + ".");
                }
                else if (!isDocAlreadyRecorded(document, true)) {
                    SGuiParams params = new SGuiParams();
                    params.getParamsMap().put(SGuiConsts.PARAM_YEAR, SLibTimeUtils.digestYear(moDatePeriodEnd.getValue())[0]);
                    params.getParamsMap().put(SGuiConsts.PARAM_BPR, document.BizPartnerId);

                    SGuiOptionPicker picker = miClient.getSession().getModule(SModConsts.MOD_TRN_N, SModConsts.MOD_TRN_PUR_N).getOptionPicker(SModConsts.TRN_DPS, SDataConstantsSys.TRNX_TP_DPS_DOC, params);
                    picker.resetPicker();
                    picker.setPickerVisible(true);

                    if (picker.getPickerResult() == SGuiConsts.FORM_RESULT_OK) {
                        int[] dpsKey = (int[]) picker.getOption();

                        if (document.link(miClient.getSession(), msSyncUrlDownload, dpsKey, SImportedDocument.MATCH_PAY_TP_CONF_DIFF, mbAllowLinkGreaterInvoices, true, false, false)) {
                            refreshDocumentsGrid();
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedRejectInvoice() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SImportedDocument document = (SImportedDocument) row;
                
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

                                    int index = moDocumentsGrid.getTable().getSelectedRow();

                                    maDocuments.remove(document);
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
    }
    
    private void actionPerformedUnlinkInvoice() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SImportedDocument document = (SImportedDocument) row;
                
                if (!document.isRecorded()) {
                    throw new Exception(SImportedDocument.EXC_DOC_NOT_RECORDED);
                }
                else {
                    if (miClient.showMsgBoxConfirm("¿Está seguro que desea desvincular la factura " + SSwapConsts.SIIE + " de esta factura autorizada?\n(IMPORTANTE: Esta acción no se puede revertir.)") == JOptionPane.YES_OPTION) {
                        if (document.unlink(miClient.getSession())) {
                            refreshDocumentsGrid();
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedCreateInvoiceFromCfdi() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SImportedDocument document = (SImportedDocument) row;
                
                // check that business partner is domestic:
                
                boolean isBizPartnerDomestic = SDataBizPartner.checkIsDomestic(document.BizPartnerId, (SClientInterface) miClient);

                if (!isBizPartnerDomestic) {
                    throw new Exception("Los CFDI solamente son emitidos por proveedores nacionales. El proveedor de este factura autorizada es extranjero.\n"
                            + "Se puede contabilizar la factura autorizada en '" + jbCreateInvoiceFromScratch.getText() + "'");
                }
                else {
                    createAndLinkDps(document, CREATE_FROM_CFDI);
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedCreateInvoiceFromScratch() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SImportedDocument document = (SImportedDocument) row;
                
                createAndLinkDps(document, CREATE_FROM_SCRATCH);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedViewInvoicePdf() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SImportedDocument document = (SImportedDocument) row;
                
                if (moDialogPdfViewer == null) {
                    moDialogPdfViewer = new SDialogPdfViewer(miClient, true);
                }
                
                if (document.isRecorded()) {
                    // if document is recorded, prefer PDF stored in ERP:
                    SViewDps.showDocPdf((SClientInterface) miClient, document.ProcessedDps.getDpsKey(), moDialogPdfViewer);
                }
                else {
                    // retrieve PDF from SWAP Services:
                    File pdf = document.retrievePdf(miClient.getSession(), msSyncUrlDownload);
                    
                    if (pdf != null) {
                        moDialogPdfViewer.setPdf(new SDocumentInfo(document), pdf);
                        moDialogPdfViewer.setVisible(true);
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedViewInvoice() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SImportedDocument document = (SImportedDocument) row;
                
                if (!document.isRecorded()) {
                    throw new Exception(SImportedDocument.EXC_DOC_NOT_RECORDED);
                }
                else {
                    ((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_PUR).setFormComplement(SDataConstantsSys.TRNU_TP_DPS_PUR_INV);
                    ((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_PUR).showForm(SDataConstants.TRNX_DPS_RO, document.ProcessedDps.getDpsKey());
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
                SImportedDocument document = (SImportedDocument) row;
                int[] orderKey = document.getFirstReferenceKey(miClient, SSwapConsts.TXN_REF_TYPE_ORDER);

                if (orderKey == null) {
                    throw new Exception("La factura autorizada no está relacionada con ningún pedido.");
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
    
    private void actionPerformedViewRecord() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SImportedDocument document = (SImportedDocument) row;
                
                if (!document.isRecorded()) {
                    throw new Exception(SImportedDocument.EXC_DOC_NOT_RECORDED);
                }
                else {
                    ((SClientInterface) miClient).getGuiModule(SDataConstants.MOD_FIN).showForm(SDataConstants.FINX_REC_RO, document.ProcessedDps.getRecordKey());
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
                SImportedDocument document = (SImportedDocument) row;
                
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
                SImportedDocument document = (SImportedDocument) row;
                
                if (document.isPaymentRequestDataAvailable()) {
                    if (!moDecReqPayAmount.isEditable()) {
                        // edit amount:
                        
                        enableEditingReqPayAmount(true);
                    }
                    else {
                        // save amount:

                        if (moDecReqPayAmount.getValue() <= 0) {
                            miClient.showMsgBoxWarning(SGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + moDecReqPayAmount.getFieldName() + "'" + SGuiConsts.ERR_MSG_FIELD_VAL_GREAT + "cero.");
                            moDecReqPayAmount.requestFocusInWindow();
                        }
                        else if (moDecReqPayAmount.getValue() > document.Total) {
                            miClient.showMsgBoxWarning(SGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + moDecReqPayAmount.getFieldName() + "'" + SGuiConsts.ERR_MSG_FIELD_VAL_LESS_EQUAL + "$ " + SLibUtils.getDecimalFormatAmount().format(document.Total) + ".");
                            moDecReqPayAmount.requestFocusInWindow();
                        }
                        else {
                            // save only if effective date is available:

                            if (document.getRequiredPaymentDateEffective() == null) {
                                actionPerformedChangeReqPayRequiredDate();
                            }

                            if (document.getRequiredPaymentDateEffective() != null) {
                                document.RequiredPaymentDefinition = SSwapConsts.PAY_DEF_BY_AMT_MAN;
                                document.RequiredPaymentAmountNew = moDecReqPayAmount.getValue();

                                refreshDocumentsGrid();

                                actionPerformedCancelEditReqPayAmount(false);
                                
                                moDocumentsGrid.getTable().requestFocusInWindow();
                            }
                            else {
                                miClient.showMsgBoxWarning("No se puede cambiar el monto requerido de pago porque la factura autorizada '" + document.getFolio() + "' no tiene una fecha efectiva de pago.");
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
        enableEditingReqPayAmount(false);
        
        if (focusDocumentsGrid) {
            moDocumentsGrid.getTable().requestFocusInWindow();
        }
    }
    
    private void actionPerformedRequestPayment() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SImportedDocument document = (SImportedDocument) row;
                
                if (!document.isRecorded()) {
                    throw new Exception(SImportedDocument.EXC_DOC_NOT_RECORDED);
                }
                else if (document.checkAdvancesOnUpcommingPaymentRequest(miClient, true)) {
                    if (document.requestPayment(miClient.getSession())) {
                        mbExportPaymentRequests = true;
                        refreshDocumentsGrid();
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedChangeReqPayRequiredDate() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SImportedDocument document = (SImportedDocument) row;
                
                if (document.isPaymentRequested()) {
                    String message = "La factura autorizada ya tiene solicitud de pago.\n";
                    
                    switch (document.Payment.getFkStatusPaymentId()) {
                        case SModSysConsts.FINS_ST_PAY_NEW:
                            message += "Se puede cambiar la '" + jtfPayRequiredDate.getToolTipText().toLowerCase() + "' en " + jbChangePayRequiredDate.getToolTipText().toLowerCase() + "'.";
                            break;
                        case SModSysConsts.FINS_ST_PAY_SCHED:
                            message += "Se puede cambiar la '" + jtfPayScheduledDate.getToolTipText().toLowerCase() + "' en " + jbChangePayScheduledDate.getToolTipText().toLowerCase() + "'.";
                            break;
                        default:
                            message += "Debido al estatus actual de la solicitud de pago, la '" + jtfReqPayRequiredDate.getToolTipText().toLowerCase() + "' no se puede modificar.";
                    }
                    
                    throw new Exception(message);
                }
                else {
                    if (document.changeRequiredPaymentDate(miClient.getSession())) {
                        mbExportPaymentRequests = true;
                        refreshDocumentsGrid();
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedChangePayRequiredDate() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SImportedDocument document = (SImportedDocument) row;
                
                if (!document.isPaymentRequested()) {
                    String message = "La factura autorizada no tiene solicitud de pago.\n"
                            + "Se puede cambiar la '" + jtfReqPayRequiredDate.getToolTipText().toLowerCase() + "' en '" + jbChangeReqPayRequiredDate.getToolTipText().toLowerCase() + "'.";
                    
                    throw new Exception(message);
                }
                else if (document.Payment.getFkStatusPaymentId() != SModSysConsts.FINS_ST_PAY_NEW) {
                    String message = "No se puede cambiar la '" + jtfPayRequiredDate.getToolTipText().toLowerCase() + "', "
                            + "para poder hacerlo el estatus de la solicitud de pago debe ser '" + SDbPayment.ST_NEW + "'.";
                    
                    if (document.Payment.getFkStatusPaymentId() == SModSysConsts.FINS_ST_PAY_SCHED) {
                        message += "\nSin embargo, como su estatus ya es '" + SDbPayment.ST_SCHED + "', se puede cambiar más bien la '" + jtfPayScheduledDate.getToolTipText().toLowerCase() + "'.";
                    }
                    
                    throw new Exception(message);
                }
                else {
                    if (document.changeRequiredPaymentDate(miClient.getSession())) {
                        mbExportPaymentRequests = true;
                        refreshDocumentsGrid();
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedChangePayScheduledDate() {
        try {
            SGridRow row = moDocumentsGrid.getSelectedGridRow();
            
            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SImportedDocument document = (SImportedDocument) row;
                
                if (!document.isPaymentRequested()) {
                    String message = "La factura autorizada no tiene solicitud de pago.\n"
                            + "Se puede cambiar la '" + jtfReqPayRequiredDate.getToolTipText().toLowerCase() + "' en '" + jbChangeReqPayRequiredDate.getToolTipText() + "'.";
                    
                    throw new Exception(message);
                }
                else if (document.Payment.getFkStatusPaymentId() != SModSysConsts.FINS_ST_PAY_SCHED) {
                    String message = "No se puede cambiar la '" + jtfPayScheduledDate.getToolTipText().toLowerCase() + "', "
                            + "para poder hacerlo el estatus de la solicitud de pago debe ser '" + SDbPayment.ST_SCHED + "'.";
                    
                    if (document.Payment.getFkStatusPaymentId() == SModSysConsts.FINS_ST_PAY_NEW) {
                        message += "\nSin embargo su estatus es '" + SDbPayment.ST_NEW + "', y se puede cambiar más bien la '" + jtfReqPayRequiredDate.getToolTipText().toLowerCase() + "'.";
                    }
                    
                    throw new Exception(message);
                }
                else {
                    if (document.changeRequiredPaymentDate(miClient.getSession())) {
                        mbExportPaymentRequests = true;
                        refreshDocumentsGrid();
                    }
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
    
    private void renderCurrentDoc(final boolean forceClearing) {
        mbDocumentsBeingRendered = true;
        
        SGridRow row = moDocumentsGrid.getSelectedGridRow();
        
        if (forceClearing || row == null) {
            jbCreateInvoiceFromCfdi.setEnabled(false);
            jbCreateInvoiceFromScratch.setEnabled(false);
            jbLinkInvoice.setEnabled(false);
            jbRejectInvoice.setEnabled(false);
            jbUnlinkInvoice.setEnabled(false);
            jbViewInvoicePdf.setEnabled(false);
            jbViewInvoice.setEnabled(false);
            jbViewOrder.setEnabled(false);
            jbViewRecord.setEnabled(false);
            
            moBoolReqPayRequire.setEnabled(false);
            moBoolReqPayRequire.resetField();
            itemStateChangedReqPayRequire();
            renderReqPay(null);
            renderAdvances(null);
            
            jbChangePayRequiredDate.setEnabled(false);
            jbChangePayScheduledDate.setEnabled(false);
            
            jtfInvoice.setText("");
            jtfInvoice.setToolTipText(null);
            jtfRecord.setText("");
            jtfRecord.setToolTipText(null);
            
            jtfInvoiceUserNew.setText("");
            
            jtfReqPayAmount.setText("");
            jtfReqPayAmountPct.setText("");
            jtfReqPayRequiredDate.setText("");
            
            jtfPayFolio.setText("");
            jtfPayDate.setText("");
            jtfPayRequiredDate.setText("");
            jtfPayStatus.setText("");
            jtfPayScheduledDate.setText("");
            jtfPayExecutionDate.setText("");
            
        }
        else {
            SImportedDocument document = (SImportedDocument) row;
            
            jbCreateInvoiceFromCfdi.setEnabled(true);
            jbCreateInvoiceFromScratch.setEnabled(true);
            jbLinkInvoice.setEnabled(true);
            jbRejectInvoice.setEnabled(true);
            jbUnlinkInvoice.setEnabled(true);
            jbViewInvoicePdf.setEnabled(true);
            jbViewInvoice.setEnabled(true);
            jbViewOrder.setEnabled(true);
            jbViewRecord.setEnabled(true);
            
            moBoolReqPayRequire.setEnabled(document.isPaymentRequestDataAvailable());
            moBoolReqPayRequire.setValue(document.RequirePayment);
            itemStateChangedReqPayRequire();
            renderReqPay(document);
            renderAdvances(document);
            
            jbRequestPayment.setEnabled(true);
            jbChangePayRequiredDate.setEnabled(true);
            jbChangePayScheduledDate.setEnabled(true);
            
            if (!document.isRecorded()) {
                jtfInvoice.setText("");
                jtfInvoice.setToolTipText(null);
                jtfRecord.setText("");
                jtfRecord.setToolTipText(null);
                
                jtfInvoiceUserNew.setText("");
            }
            else {
                jtfInvoice.setText(document.getFolio()); // show folio of current document as a visual indicator that is an invoice already linked!
                jtfInvoice.setToolTipText("Factura: " + document.getFolio());
                jtfRecord.setText(document.ProcessedDps.composeRecord());
                jtfRecord.setToolTipText("Póliza contable: " + document.ProcessedDps.composeRecord());
                
                jtfInvoiceUserNew.setText(document.ProcessedDps.UserNew);
                
                jtfInvoice.setCaretPosition(0);
                jtfRecord.setCaretPosition(0);
                jtfInvoiceUserNew.setCaretPosition(0);
            }
            
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
            
            if (!document.isPaymentRequested()) {
                jtfPayFolio.setText("");
                jtfPayDate.setText("");
                jtfPayRequiredDate.setText("");
                jtfPayStatus.setText("");
                jtfPayScheduledDate.setText("");
                jtfPayExecutionDate.setText("");
            }
            else {
                jtfPayFolio.setText(document.Payment.getFolio());
                jtfPayDate.setText(SLibUtils.DateFormatDate.format(document.Payment.getDateApplication()));
                jtfPayRequiredDate.setText(SLibUtils.GuiDateFormat.format(document.Payment.getDateRequired()));
                jtfPayStatus.setText(document.Payment.getDbmsStatus());
                jtfPayScheduledDate.setText(document.Payment.getDateSchedule_n() == null ? "ND" : SLibUtils.GuiDateFormat.format(document.Payment.getDateSchedule_n()));
                jtfPayExecutionDate.setText(document.Payment.getDateExecution_n() == null ? "ND" : SLibUtils.GuiDateFormat.format(document.Payment.getDateExecution_n()));
                
                jtfPayFolio.setCaretPosition(0);
                jtfPayDate.setCaretPosition(0);
                jtfPayRequiredDate.setCaretPosition(0);
                jtfPayStatus.setCaretPosition(0);
                jtfPayScheduledDate.setCaretPosition(0);
                jtfPayExecutionDate.setCaretPosition(0);
            }
        }
        
        mbDocumentsBeingRendered = false;
    }
    
    private void itemStateChangedSearchBy() {
        enableFieldsOfSearchBy();
    }
    
    private void itemStateChangedDocMode() {
        enableFieldsOfDocMode();
        reloadDocumentsGrid();
    }
    
    private void itemStateChangedDocType(final boolean focusDocumentsGrid) {
        if (moKeyDocModeType.isEnabled()) {
            if (moKeyDocModeType.getValue()[0] == SImportedDocument.DOC_TYPE_ALL) {
                populateDocumentsGrid(maDocuments, focusDocumentsGrid);
            }
            else {
                ArrayList<SImportedDocument> documents = new ArrayList<>();

                if (moKeyDocModeType.getValue()[0] == SImportedDocument.DOC_TYPE_ASSETS) {
                    for (SImportedDocument document : maDocuments) {
                        if (DCfdi40Catalogs.ClavesUsoCfdiActivoFijo.contains(document.FiscalUseCode)) {
                            documents.add(document);
                        }
                    }
                }
                else if (moKeyDocModeType.getValue()[0] == SImportedDocument.DOC_TYPE_EXPENSES) {
                    for (SImportedDocument document : maDocuments) {
                        if (!DCfdi40Catalogs.ClavesUsoCfdiActivoFijo.contains(document.FiscalUseCode)) {
                            documents.add(document);
                        }
                    }
                }

                populateDocumentsGrid(documents, focusDocumentsGrid);
            }
            
            jbRecordAllDocs.setEnabled(isMassAccountingElegible());
        }
    }
    
    private void itemStateChangedDocCase(final boolean focusDocumentsGrid) {
        if (moKeyDocModeCase.isEnabled()) {
            if (moKeyDocModeCase.getValue()[0] == SImportedDocument.DOC_CASE_ALL) {
                populateDocumentsGrid(maDocuments, focusDocumentsGrid);
            }
            else {
                Integer processingTypeId = null;
                
                switch (moKeyDocModeCase.getValue()[0]) {
                    case SImportedDocument.DOC_CASE_STANDARD:
                        processingTypeId = SDbSwapDataProcessing.PROC_TYPE_STANDARD;
                        break;
                    case SImportedDocument.DOC_CASE_RAW_MAT_FREIGHT:
                        processingTypeId = SDbSwapDataProcessing.PROC_TYPE_RAW_MAT_FREIGHT;
                        break;
                    case SImportedDocument.DOC_CASE_RAW_MAT_PURCHASE:
                        processingTypeId = SDbSwapDataProcessing.PROC_TYPE_RAW_MAT_PURCHASE;
                        break;
                    default:
                        // nothing
                }
                
                ArrayList<SImportedDocument> documents = new ArrayList<>();

                if (processingTypeId != null) {
                    for (SImportedDocument document : maDocuments) {
                        if (document.ProcessingTypeId == processingTypeId) {
                            documents.add(document);
                        }
                    }
                }

                populateDocumentsGrid(documents, focusDocumentsGrid);
            }
            
            jbRecordAllDocs.setEnabled(isMassAccountingElegible());
        }
    }
    
    private void itemStateChangedReqPayRequire() {
        boolean enable = false;
        boolean require = moBoolReqPayRequire.isSelected(); // convenience variable
        
        if (mbDocumentsBeingRendered) {
            // just rendering current document:
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
                    SImportedDocument document = (SImportedDocument) row;
                    document.RequirePayment = require;

                    refreshDocumentsGrid();

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
            jbRequestPayment.setEnabled(require);
        }
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
            }
            
            super.windowActivated();
        }
    }
    
    @Override
    public void resetForm() {
        removeAllListeners();
        
        mnFormResult = 0;
        mbFirstActivation = true;
        
        mbExportPaymentRequests = false;
        moBoolExportPaymentRequestsOnClose.setSelected(true);
        
        Date date = miClient.getSession().getCurrentDate();
        int week = SLibTimeUtils.getIsoWeekOfWeekBasedYear(date);
        
        bgSearchBy.setSelected(moRadSearchByPeriod.getModel(), true);
        moDatePeriodStart.setValue(SLibTimeUtils.getBeginOfMonth(date));
        moDatePeriodEnd.setValue(SLibTimeUtils.getEndOfMonth(date));
        moCalWeekYear.setValue(miClient.getSession().getCurrentYear());
        moCalWeekStart.setValue(week);
        moCalWeekEnd.setValue(week);
        
        bgDocMode.setSelected(moRadDocModeType.getModel(), true);
        
        actionPerformedClearDocs();
        
        addAllListeners();
    }
    
    @Override
    public void addAllListeners() {
        jbShowDocs.addActionListener(this);
        jbClearDocs.addActionListener(this);
        jbSelectRemainingDocs.addActionListener(this);
        jbSelectAllDocs.addActionListener(this);
        jbDeselectAllDocs.addActionListener(this);
        jbDownloadSelectedDocs.addActionListener(this);
        jbRecordAllDocs.addActionListener(this);
        jbLinkAllDocs.addActionListener(this);
        
        jbCreateInvoiceFromCfdi.addActionListener(this);
        jbCreateInvoiceFromScratch.addActionListener(this);
        jbLinkInvoice.addActionListener(this);
        jbRejectInvoice.addActionListener(this);
        jbUnlinkInvoice.addActionListener(this);
        jbViewInvoicePdf.addActionListener(this);
        jbViewInvoice.addActionListener(this);
        jbViewOrder.addActionListener(this);
        jbViewRecord.addActionListener(this);
        
        jbViewAdvances.addActionListener(this);
        jbEditAndSaveReqPayAmount.addActionListener(this);
        jbCancelEditReqPayAmount.addActionListener(this);
        jbChangeReqPayRequiredDate.addActionListener(this);
        jbRequestPayment.addActionListener(this);
        jbChangePayRequiredDate.addActionListener(this);
        jbChangePayScheduledDate.addActionListener(this);
        
        moBoolReqPayRequire.addItemListener(this);
        
        moRadSearchByPeriod.addItemListener(this);
        moRadSearchByWeek.addItemListener(this);
        moRadDocModeType.addItemListener(this);
        moRadDocModeCase.addItemListener(this);
        moKeyDocModeType.addItemListener(this);
        moKeyDocModeCase.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbShowDocs.removeActionListener(this);
        jbClearDocs.removeActionListener(this);
        jbSelectRemainingDocs.removeActionListener(this);
        jbSelectAllDocs.removeActionListener(this);
        jbDeselectAllDocs.removeActionListener(this);
        jbDownloadSelectedDocs.removeActionListener(this);
        jbRecordAllDocs.removeActionListener(this);
        jbLinkAllDocs.removeActionListener(this);
        
        jbCreateInvoiceFromCfdi.removeActionListener(this);
        jbCreateInvoiceFromScratch.removeActionListener(this);
        jbLinkInvoice.removeActionListener(this);
        jbRejectInvoice.removeActionListener(this);
        jbUnlinkInvoice.removeActionListener(this);
        jbViewInvoicePdf.removeActionListener(this);
        jbViewInvoice.removeActionListener(this);
        jbViewOrder.removeActionListener(this);
        jbViewRecord.removeActionListener(this);
        
        jbViewAdvances.removeActionListener(this);
        jbEditAndSaveReqPayAmount.removeActionListener(this);
        jbCancelEditReqPayAmount.removeActionListener(this);
        jbChangeReqPayRequiredDate.removeActionListener(this);
        jbRequestPayment.removeActionListener(this);
        jbChangePayRequiredDate.removeActionListener(this);
        jbChangePayScheduledDate.removeActionListener(this);
        
        moBoolReqPayRequire.removeItemListener(this);
        
        moRadSearchByPeriod.removeItemListener(this);
        moRadSearchByWeek.removeItemListener(this);
        moRadDocModeType.removeItemListener(this);
        moRadDocModeCase.removeItemListener(this);
        moKeyDocModeType.removeItemListener(this);
        moKeyDocModeCase.removeItemListener(this);
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
    public void windowClosed() {
        super.windowClosed();
        
        exportPaymentRequestsIfNeeded();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbShowDocs) {
                actionPerformedShowDocs();
            }
            else if (button == jbClearDocs) {
                actionPerformedClearDocs();
            }
            else if (button == jbSelectRemainingDocs) {
                actionPerformedSelectRemainingDocs();
            }
            else if (button == jbSelectAllDocs) {
                actionPerformedSelectAllDocs();
            }
            else if (button == jbDeselectAllDocs) {
                actionPerformedDeselectAllDocs();
            }
            else if (button == jbDownloadSelectedDocs) {
                actionPerformedDownloadSelectedDocs();
            }
            else if (button == jbRecordAllDocs) {
                actionPerformedRecordAllDocs();
            }
            else if (button == jbLinkAllDocs) {
                actionPerformedLinkAllDocs();
            }
            else if (button == jbCreateInvoiceFromCfdi) {
                actionPerformedCreateInvoiceFromCfdi();
            }
            else if (button == jbCreateInvoiceFromScratch) {
                actionPerformedCreateInvoiceFromScratch();
            }
            else if (button == jbLinkInvoice) {
                actionPerformedLinkInvoice();
            }
            else if (button == jbRejectInvoice) {
                actionPerformedRejectInvoice();
            }
            else if (button == jbUnlinkInvoice) {
                actionPerformedUnlinkInvoice();
            }
            else if (button == jbViewInvoicePdf) {
                actionPerformedViewInvoicePdf();
            }
            else if (button == jbViewInvoice) {
                actionPerformedViewInvoice();
            }
            else if (button == jbViewOrder) {
                actionPerformedViewOrder();
            }
            else if (button == jbViewRecord) {
                actionPerformedViewRecord();
            }
            else if (button == jbRequestPayment) {
                actionPerformedRequestPayment();
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
            else if (button == jbChangePayRequiredDate) {
                actionPerformedChangePayRequiredDate();
            }
            else if (button == jbChangePayScheduledDate) {
                actionPerformedChangePayScheduledDate();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!mbDocumentsBeingProcessed && !mbDocumentsBeingRefreshed) {
            if (!e.getValueIsAdjusting()) {
                renderCurrentDoc(false);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (!mbDocumentsBeingProcessed && !mbDocumentsBeingReloaded && !mbDocumentsBeingRendered) {
            if (e.getSource() instanceof SBeanFieldBoolean) {
                SBeanFieldBoolean field = (SBeanFieldBoolean) e.getSource();

                if (field == moBoolReqPayRequire) {
                    itemStateChangedReqPayRequire();
                }
            }
            else if (e.getSource() instanceof SBeanFieldRadio && e.getStateChange() == ItemEvent.SELECTED) {
                SBeanFieldRadio field = (SBeanFieldRadio) e.getSource();

                if (field == moRadSearchByPeriod || field == moRadSearchByWeek) {
                    itemStateChangedSearchBy();
                }
                else if (field == moRadDocModeType || field == moRadDocModeCase) {
                    itemStateChangedDocMode();
                }
            }
            else if (e.getSource() instanceof SBeanFieldKey && e.getStateChange() == ItemEvent.SELECTED) {
                SBeanFieldKey field = (SBeanFieldKey) e.getSource();
                
                if (field == moKeyDocModeType) {
                    itemStateChangedDocType(false); // reloads documents grid
                }
                else if (field == moKeyDocModeCase) {
                    itemStateChangedDocCase(false); // reloads documents grid
                }
            }
        }
    }
    
    public static class Settings {
        
        public String UserName;
        public String UserFuncSubAreas;
        public int ModeCase;
        public String SyncUrlDownload;
        public PreparedStatement PrepStatToGetDpsKeyByDocData;
        
        public int SearchBy;
        public Date PeriodStart;
        public Date PeriodEnd;
        public int WeekYear;
        public int WeekStart;
        public int WeekEnd;
        
        public Settings(final String userName, final String userFuncSubAreas, final int modeCase,
                final String syncUrlDownload, final PreparedStatement prepStatToGetDpsKeyByDocData) {
            UserName = userName;
            UserFuncSubAreas = userFuncSubAreas;
            ModeCase = modeCase;
            SyncUrlDownload = syncUrlDownload;
            PrepStatToGetDpsKeyByDocData = prepStatToGetDpsKeyByDocData;
            
            SearchBy = 0;
            PeriodStart = null;
            PeriodEnd = null;
            WeekYear = 0;
            WeekStart = 0;
            WeekEnd = 0;
        }
        
        public void setSearchByPeriod(final Date periodStart, final Date periodEnd) {
            SearchBy = SEARCH_BY_PERIOD;
            PeriodStart = periodStart;
            PeriodEnd = periodEnd;
        }
        
        public void setSearchByWeek(final int weekYear, final int weekStart, final int weekEnd) {
            SearchBy = SEARCH_BY_WEEK;
            WeekYear = weekYear;
            WeekStart = weekStart;
            WeekEnd = weekEnd;
        }
    }
}

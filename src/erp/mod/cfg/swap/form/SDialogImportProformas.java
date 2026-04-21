/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.form;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mcfg.data.SCfgUtils;
import erp.mcfg.data.SDataParamsCompany;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbComImportLog;
import erp.mod.cfg.db.SDbFunctionalSubArea;
import erp.mod.cfg.swap.SHttpConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.SSyncType;
import erp.mod.cfg.swap.model.SImportedCRP;
import erp.mod.cfg.swap.utils.SExportUtils;
import erp.mod.cfg.swap.utils.SImportUtils;
import erp.mod.cfg.swap.utils.SResponses;
import erp.mod.cfg.utils.SAuthJsonUtils;
import erp.mod.fin.db.SDbPayment;
import erp.mtrn.form.SDialogDpsFinder;
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
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFieldBoolean;
import sa.lib.gui.bean.SBeanFormDialog;

/**
 * Importación de documentos desde el Portal de Compras. Ejemplo de la URL de
 * consulta de documentos:
 * "https://transaction-backend-368437194061.us-central1.run.app/api/documents/filter-by-date-and-type/?start_date=2025-08-01&end_date=2025-09-30&document_type=41"
 * Ejemplo de la URL de descarga de documentos:
 * "https://transaction-backend-368437194061.us-central1.run.app/api/documents/download-docs-zip/"
 *
 * @author Cesar Orozco, Sergio Flores, Edwin Carmona
 */
public class SDialogImportProformas extends SBeanFormDialog implements ActionListener, ListSelectionListener, ItemListener {

    protected static final int OFF = 0;
    protected static final int ON = 1;
    protected static final int LIMIT_DAYS = 31; // 1 calendar month
    protected static final int LIMIT_WEEKS = 4; // 1 lunar month
    protected static final int LIMIT_DOWNLOADS = 250; // 0.25 k documents
    protected static final int FUNC_SUB_AREA_CODES_PER_LINE = 15;

    protected String msCompanyName;
    protected int mnShowingDocsMode;
    protected SGridPaneForm moImportationsGrid;
    protected SDialogDpsFinder moDialogDpsFinder;
    protected ArrayList<SImportedProforma> maImportedDocuments;
    protected ArrayList<SImportedCRP> maCRPs;
    protected ArrayList<SDbFunctionalSubArea> maFunctionalSubAreas;
    protected String msUserFunctionalSubAreaCodes;
    protected String msSyncUrlRetrieveByPeriod;
    protected String msSyncUrlRetrieveByWeek;
    protected String msSyncUrlDownload;
    protected String msSyncToken;
    protected String msSyncApiKey;
    protected int mnSyncLimit;
    protected PreparedStatement moPrepStatToCountImports;
    protected PreparedStatement moPrepStatToGetProcessedProformaByExternalId;
    protected PreparedStatement moPrepStatToGetDpsKeyByDocData;
    protected JLabel jlStatus;
    protected SBeanFieldBoolean moBoolExportPaymentRequestsOnClose;
    protected boolean mbAllowLinkGreaterInvoices;

    protected boolean mbDocumentsBeingUpdated;
    protected boolean mbExportPaymentRequests;
    protected SDialogPdfViewer moDialogPdfViewer;

    /**
     * Creates new form SDialogImportDocuments
     *
     * @param client GUI client.
     * @param formSubtype
     * @param title
     */
    public SDialogImportProformas(SGuiClient client, final int formSubtype, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.CFGX_SWAP_IMP_PROFS, formSubtype, title);
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
        jlPeriod = new javax.swing.JLabel();
        moDatePeriodStart = new sa.lib.gui.bean.SBeanFieldDate();
        jlPeriiod1 = new javax.swing.JLabel();
        moDatePeriodEnd = new sa.lib.gui.bean.SBeanFieldDate();
        jpDownloadE = new javax.swing.JPanel();
        jpDownloadE1 = new javax.swing.JPanel();
        jbShowProformas = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jbSelectRemainingProformas = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jbDownloadSelectedProformas = new javax.swing.JButton();
        jpDownloadE2 = new javax.swing.JPanel();
        jbClearProformas = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jbSelectAllProformas = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jLabel2b3 = new javax.swing.JLabel();
        jpDownloadE3 = new javax.swing.JPanel();
        jLabel3b1 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jbDeselectAllProformas = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jLabel3b3 = new javax.swing.JLabel();
        jpDocuments = new javax.swing.JPanel();
        jpDocumentsGrid = new javax.swing.JPanel();
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
        jlProforma = new javax.swing.JLabel();
        jpProcessingN2 = new javax.swing.JPanel();
        jtfProforma = new javax.swing.JTextField();
        jpProcessingN4 = new javax.swing.JPanel();
        jlReqPay = new javax.swing.JLabel();
        jpProcessingN5 = new javax.swing.JPanel();
        jtfReqPayAmount = new javax.swing.JTextField();
        jtfReqPayAmountPct = new javax.swing.JTextField();
        jpProcessingN6 = new javax.swing.JPanel();
        jtfReqPayReqDate = new javax.swing.JTextField();
        jbChangeRequiredPaymentDate = new javax.swing.JButton();
        jpProcessingN7 = new javax.swing.JPanel();
        jbRequestPayment = new javax.swing.JButton();
        jpProcessingN8 = new javax.swing.JPanel();
        jlPay = new javax.swing.JLabel();
        jpProcessingN9 = new javax.swing.JPanel();
        jtfPayFolio = new javax.swing.JTextField();
        jtfPayDate = new javax.swing.JTextField();
        jpProcessingN10 = new javax.swing.JPanel();
        jtfPayReqDate = new javax.swing.JTextField();
        jbChangePaymentRequiredDate = new javax.swing.JButton();
        jpProcessingN11 = new javax.swing.JPanel();
        jtfPayStatus = new javax.swing.JTextField();
        jpProcessingN12 = new javax.swing.JPanel();
        jlPaySched = new javax.swing.JLabel();
        jpProcessingN13 = new javax.swing.JPanel();
        jtfPaySchedDate = new javax.swing.JTextField();
        jbChangePaymentScheduledDate = new javax.swing.JButton();
        jpProcessingN14 = new javax.swing.JPanel();
        jlPayExec = new javax.swing.JLabel();
        jpProcessingN15 = new javax.swing.JPanel();
        jtfPayExecDate = new javax.swing.JTextField();
        jpProcessingN16 = new javax.swing.JPanel();
        jpProcessingN17 = new javax.swing.JPanel();
        jpProcessingN18 = new javax.swing.JPanel();
        jpProcessingN19 = new javax.swing.JPanel();
        jpProcessingN20 = new javax.swing.JPanel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpDownload.setBorder(javax.swing.BorderFactory.createTitledBorder("Búsqueda de " + ((mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_PROFORMA) ? "proformas" : "CRPs") + " autorizadas"));
        jpDownload.setToolTipText("");
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

        jlPeriod.setText("Período:");
        jlPeriod.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDownloadW2.add(jlPeriod);

        moDatePeriodStart.setToolTipText("Fecha inicial");
        moDatePeriodStart.setPreferredSize(new java.awt.Dimension(103, 21));
        jpDownloadW2.add(moDatePeriodStart);

        jlPeriiod1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlPeriiod1.setText("−");
        jlPeriiod1.setPreferredSize(new java.awt.Dimension(15, 21));
        jpDownloadW2.add(jlPeriiod1);

        moDatePeriodEnd.setToolTipText("Fecha final");
        moDatePeriodEnd.setPreferredSize(new java.awt.Dimension(103, 21));
        jpDownloadW2.add(moDatePeriodEnd);

        jpDownloadW.add(jpDownloadW2);

        jpDownload.add(jpDownloadW, java.awt.BorderLayout.WEST);

        jpDownloadE.setLayout(new java.awt.GridLayout(3, 1, 0, 1));

        jpDownloadE1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jbShowProformas.setText((mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_PROFORMA) ? "Mostrar proformas" : "Mostrar CRPs");
        jbShowProformas.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbShowProformas.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE1.add(jbShowProformas);

        jLabel11.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE1.add(jLabel11);

        jbSelectRemainingProformas.setText("Seleccionar restantes");
        jbSelectRemainingProformas.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbSelectRemainingProformas.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE1.add(jbSelectRemainingProformas);

        jLabel12.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE1.add(jLabel12);

        jbDownloadSelectedProformas.setText("Descargar seleccionadas");
        jbDownloadSelectedProformas.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbDownloadSelectedProformas.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE1.add(jbDownloadSelectedProformas);

        jpDownloadE.add(jpDownloadE1);

        jpDownloadE2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jbClearProformas.setText((mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_PROFORMA) ? "Limpiar proformas" : "Limpiar CRPs");
        jbClearProformas.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbClearProformas.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE2.add(jbClearProformas);

        jLabel21.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE2.add(jLabel21);

        jbSelectAllProformas.setText("Seleccionar todas");
        jbSelectAllProformas.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbSelectAllProformas.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE2.add(jbSelectAllProformas);

        jLabel22.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE2.add(jLabel22);

        jLabel2b3.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE2.add(jLabel2b3);

        jpDownloadE.add(jpDownloadE2);

        jpDownloadE3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jLabel3b1.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE3.add(jLabel3b1);

        jLabel31.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE3.add(jLabel31);

        jbDeselectAllProformas.setText("Deseleccionar todas");
        jbDeselectAllProformas.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbDeselectAllProformas.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE3.add(jbDeselectAllProformas);

        jLabel32.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE3.add(jLabel32);

        jLabel3b3.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE3.add(jLabel3b3);

        jpDownloadE.add(jpDownloadE3);

        jpDownload.add(jpDownloadE, java.awt.BorderLayout.EAST);

        getContentPane().add(jpDownload, java.awt.BorderLayout.NORTH);
        jpDownload.getAccessibleContext().setAccessibleName("Búsqueda de  autorizadas:");

        jpDocuments.setBorder(javax.swing.BorderFactory.createTitledBorder(((mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_PROFORMA) ? "Proformas autorizadas" : "CRPs autorizados")));
        jpDocuments.setLayout(new java.awt.BorderLayout(5, 0));

        jpDocumentsGrid.setLayout(new java.awt.BorderLayout());

        jpDocumentsGrid1.setLayout(new java.awt.BorderLayout());

        jpDocumentsGrid11.setLayout(new java.awt.GridLayout(2, 1, 0, 2));

        jpDocumentsGrid111.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlInvoiceUserNew.setText("Usuario:");
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

        jpDocumentsGrid.add(jpDocumentsGrid1, java.awt.BorderLayout.SOUTH);

        jpDocuments.add(jpDocumentsGrid, java.awt.BorderLayout.CENTER);

        jpDocumentsProcessing.setLayout(new java.awt.BorderLayout());

        jpProcessingN.setLayout(new java.awt.GridLayout(20, 1, 0, 1));

        jpProcessingN1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProforma.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlProforma.setText((mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_PROFORMA) ? "Proforma" : "");
        jlProforma.setPreferredSize(new java.awt.Dimension(122, 23));
        jpProcessingN1.add(jlProforma);

        jpProcessingN.add(jpProcessingN1);

        jpProcessingN2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfProforma.setEditable(false);
        jtfProforma.setText("ABC-000000");
        jtfProforma.setToolTipText("Proforma");
        jtfProforma.setFocusable(false);
        jtfProforma.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN2.add(jtfProforma);

        jpProcessingN.add(jpProcessingN2);

        jpProcessingN4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReqPay.setText("Pago requerido:");
        jlReqPay.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN4.add(jlReqPay);

        jpProcessingN.add(jpProcessingN4);

        jpProcessingN5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfReqPayAmount.setEditable(false);
        jtfReqPayAmount.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfReqPayAmount.setText("000,000,000.00 MXN");
        jtfReqPayAmount.setToolTipText("Pago requerido");
        jtfReqPayAmount.setFocusable(false);
        jtfReqPayAmount.setPreferredSize(new java.awt.Dimension(105, 23));
        jpProcessingN5.add(jtfReqPayAmount);

        jtfReqPayAmountPct.setEditable(false);
        jtfReqPayAmountPct.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfReqPayAmountPct.setText("100%");
        jtfReqPayAmountPct.setToolTipText("Porcentaje de pago requerido");
        jtfReqPayAmountPct.setFocusable(false);
        jtfReqPayAmountPct.setPreferredSize(new java.awt.Dimension(40, 23));
        jpProcessingN5.add(jtfReqPayAmountPct);

        jpProcessingN.add(jpProcessingN5);

        jpProcessingN6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfReqPayReqDate.setEditable(false);
        jtfReqPayReqDate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfReqPayReqDate.setText("dow 01/mon/2001");
        jtfReqPayReqDate.setToolTipText("Fecha requerida de pago");
        jtfReqPayReqDate.setFocusable(false);
        jtfReqPayReqDate.setPreferredSize(new java.awt.Dimension(105, 23));
        jpProcessingN6.add(jtfReqPayReqDate);

        jbChangeRequiredPaymentDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbChangeRequiredPaymentDate.setToolTipText("Cambiar fecha requerida de pago...");
        jbChangeRequiredPaymentDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN6.add(jbChangeRequiredPaymentDate);

        jpProcessingN.add(jpProcessingN6);

        jpProcessingN7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbRequestPayment.setForeground(java.awt.Color.blue);
        jbRequestPayment.setText("Solicitar pago");
        jbRequestPayment.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbRequestPayment.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN7.add(jbRequestPayment);

        jpProcessingN.add(jpProcessingN7);

        jpProcessingN8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPay.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlPay.setText("Solicitud de pago:");
        jlPay.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN8.add(jlPay);

        jpProcessingN.add(jpProcessingN8);

        jpProcessingN9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfPayFolio.setEditable(false);
        jtfPayFolio.setText("P-000000");
        jtfPayFolio.setToolTipText("Folio de solicitud de pago");
        jtfPayFolio.setFocusable(false);
        jtfPayFolio.setPreferredSize(new java.awt.Dimension(75, 23));
        jpProcessingN9.add(jtfPayFolio);

        jtfPayDate.setEditable(false);
        jtfPayDate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPayDate.setText("01/01/2001");
        jtfPayDate.setToolTipText("Fecha de solicitud de pago");
        jtfPayDate.setFocusable(false);
        jtfPayDate.setPreferredSize(new java.awt.Dimension(70, 23));
        jpProcessingN9.add(jtfPayDate);

        jpProcessingN.add(jpProcessingN9);

        jpProcessingN10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfPayReqDate.setEditable(false);
        jtfPayReqDate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPayReqDate.setText("dow 01/mon/2001");
        jtfPayReqDate.setToolTipText("Fecha requerida de pago en solicitud de pago");
        jtfPayReqDate.setFocusable(false);
        jtfPayReqDate.setPreferredSize(new java.awt.Dimension(105, 23));
        jpProcessingN10.add(jtfPayReqDate);

        jbChangePaymentRequiredDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbChangePaymentRequiredDate.setToolTipText("Cambiar fecha requerida de pago en solicitud de pago...");
        jbChangePaymentRequiredDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN10.add(jbChangePaymentRequiredDate);

        jpProcessingN.add(jpProcessingN10);

        jpProcessingN11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfPayStatus.setEditable(false);
        jtfPayStatus.setText("STATUS");
        jtfPayStatus.setToolTipText("Estatus de solicitud de pago");
        jtfPayStatus.setFocusable(false);
        jtfPayStatus.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN11.add(jtfPayStatus);

        jpProcessingN.add(jpProcessingN11);

        jpProcessingN12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPaySched.setText("Programación del pago:");
        jlPaySched.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN12.add(jlPaySched);

        jpProcessingN.add(jpProcessingN12);

        jpProcessingN13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfPaySchedDate.setEditable(false);
        jtfPaySchedDate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPaySchedDate.setText("dow 01/mon/2001");
        jtfPaySchedDate.setToolTipText("Fecha de programación del pago");
        jtfPaySchedDate.setFocusable(false);
        jtfPaySchedDate.setPreferredSize(new java.awt.Dimension(105, 23));
        jpProcessingN13.add(jtfPaySchedDate);

        jbChangePaymentScheduledDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbChangePaymentScheduledDate.setToolTipText("Cambiar fecha de programación del pago...");
        jbChangePaymentScheduledDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProcessingN13.add(jbChangePaymentScheduledDate);

        jpProcessingN.add(jpProcessingN13);

        jpProcessingN14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayExec.setText("Operación del pago:");
        jlPayExec.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN14.add(jlPayExec);

        jpProcessingN.add(jpProcessingN14);

        jpProcessingN15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfPayExecDate.setEditable(false);
        jtfPayExecDate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPayExecDate.setText("dow 01/mon/2001");
        jtfPayExecDate.setToolTipText("Fecha de operación del pago");
        jtfPayExecDate.setFocusable(false);
        jtfPayExecDate.setPreferredSize(new java.awt.Dimension(105, 23));
        jpProcessingN15.add(jtfPayExecDate);

        jpProcessingN.add(jpProcessingN15);

        jpProcessingN16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpProcessingN.add(jpProcessingN16);

        jpProcessingN17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpProcessingN.add(jpProcessingN17);

        jpProcessingN18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpProcessingN.add(jpProcessingN18);

        jpProcessingN19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpProcessingN.add(jpProcessingN19);

        jpProcessingN20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpProcessingN.add(jpProcessingN20);

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
    private javax.swing.JLabel jLabel2b3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel3b1;
    private javax.swing.JLabel jLabel3b3;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JButton jbChangePaymentRequiredDate;
    private javax.swing.JButton jbChangePaymentScheduledDate;
    private javax.swing.JButton jbChangeRequiredPaymentDate;
    private javax.swing.JButton jbClearProformas;
    private javax.swing.JButton jbDeselectAllProformas;
    private javax.swing.JButton jbDownloadSelectedProformas;
    private javax.swing.JButton jbRequestPayment;
    private javax.swing.JButton jbSelectAllProformas;
    private javax.swing.JButton jbSelectRemainingProformas;
    private javax.swing.JButton jbShowProformas;
    private javax.swing.JLabel jlInvoiceUserNew;
    private javax.swing.JLabel jlPay;
    private javax.swing.JLabel jlPayExec;
    private javax.swing.JLabel jlPaySched;
    private javax.swing.JLabel jlPeriiod1;
    private javax.swing.JLabel jlPeriod;
    private javax.swing.JLabel jlProforma;
    private javax.swing.JLabel jlProgress;
    private javax.swing.JLabel jlReqPay;
    private javax.swing.JLabel jlUser;
    private javax.swing.JPanel jpDocuments;
    private javax.swing.JPanel jpDocumentsGrid;
    private javax.swing.JPanel jpDocumentsGrid1;
    private javax.swing.JPanel jpDocumentsGrid11;
    private javax.swing.JPanel jpDocumentsGrid111;
    private javax.swing.JPanel jpDocumentsGrid112;
    private javax.swing.JPanel jpDocumentsGrid12;
    private javax.swing.JPanel jpDocumentsGrid121;
    private javax.swing.JPanel jpDocumentsGrid122;
    private javax.swing.JPanel jpDocumentsProcessing;
    private javax.swing.JPanel jpDownload;
    private javax.swing.JPanel jpDownloadE;
    private javax.swing.JPanel jpDownloadE1;
    private javax.swing.JPanel jpDownloadE2;
    private javax.swing.JPanel jpDownloadE3;
    private javax.swing.JPanel jpDownloadW;
    private javax.swing.JPanel jpDownloadW1;
    private javax.swing.JPanel jpDownloadW2;
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
    private javax.swing.JPanel jpProcessingN4;
    private javax.swing.JPanel jpProcessingN5;
    private javax.swing.JPanel jpProcessingN6;
    private javax.swing.JPanel jpProcessingN7;
    private javax.swing.JPanel jpProcessingN8;
    private javax.swing.JPanel jpProcessingN9;
    private javax.swing.JTextField jtfInvoiceUserNew;
    private javax.swing.JTextField jtfPayDate;
    private javax.swing.JTextField jtfPayExecDate;
    private javax.swing.JTextField jtfPayFolio;
    private javax.swing.JTextField jtfPayReqDate;
    private javax.swing.JTextField jtfPaySchedDate;
    private javax.swing.JTextField jtfPayStatus;
    private javax.swing.JTextField jtfProforma;
    private javax.swing.JTextField jtfReqPayAmount;
    private javax.swing.JTextField jtfReqPayAmountPct;
    private javax.swing.JTextField jtfReqPayReqDate;
    private javax.swing.JTextField jtfUserFuncSubAreas;
    private javax.swing.JTextField jtfUserName;
    private sa.lib.gui.bean.SBeanFieldDate moDatePeriodEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDatePeriodStart;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods.
     */
    @SuppressWarnings("unchecked")
    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 670);

        moDatePeriodStart.setDateSettings(miClient, moDatePeriodStart.getToolTipText(), true);
        moDatePeriodEnd.setDateSettings(miClient, moDatePeriodEnd.getToolTipText(), true);

        moFields.addField(moDatePeriodStart);
        moFields.addField(moDatePeriodEnd);
        moFields.setFormButton(jbShowProformas);

        jbSave.setEnabled(false);
        jbCancel.setText(SGuiConsts.TXT_BTN_CLOSE);
        jbCancel.setPreferredSize(new Dimension(75, 23));

        msCompanyName = SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.CFGU_CO, new int[]{miClient.getSession().getConfigCompany().getCompanyId()}, SLibConstants.DESCRIPTION_NAME);
        mnShowingDocsMode = OFF;

        moImportationsGrid = new SGridPaneForm(miClient, SModConsts.CFGX_SWAP_IMP_PROFS, 1, "Proformas", null) {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                SGridColumnForm column;
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();

                if (mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_PROFORMA) {
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Proveedor proforma", 200));  // col 0
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Folio proforma", 75));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha proforma"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Referencias proforma", 75));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Descripción proforma"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Total proforma $")); // col 5
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda proforma"));
                    column = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Descargar", moImportationsGrid.getTable().getDefaultEditor(Boolean.class));
                    column.setEditable(true);
                    gridColumnsForm.add(column);
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Descargada (proforma)"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Subárea funcional proforma"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Uso CFDI proforma"));// col 10
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE_DATETIME, "Fecha-hora revisión proforma"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Pago requerido $"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda pago requerido"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_PER_0D, "Pago requerido %"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha pago requerido")); // col 15
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Nueva fecha pago requerido"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Pago requerido moneda local"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Instrucciones pago requerido"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Folio solicitud pago", 75));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha solicitud pago")); // col 20
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Tipo definición pago requerido"));
                }
                else if (mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_RECEIPT_PAYMENT) {
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Proveedor CRP", 200));       // col 0
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Folio CRP", 75));             // col 1
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha CRP"));                 // col 2
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Total CRP $"));            // col 3
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda CRP"));       // col 4
                    column = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Descargar", moImportationsGrid.getTable().getDefaultEditor(Boolean.class));
                    column.setEditable(true);
                    gridColumnsForm.add(column);                                                                      // col 5
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Descargado (CRP)"));        // col 6
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Subárea funcional CRP"));     // col 7
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Uso CFDI CRP"));     // col 8
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "UUID CRP", 280));             // col 9
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_2B, "Archivos"));                   // col 10
                }

                return gridColumnsForm;
            }
        };

        moImportationsGrid.setForm(null);
        moImportationsGrid.setPaneFormOwner(null);
        jpDocumentsGrid.add(moImportationsGrid, BorderLayout.CENTER);

        jlStatus = new JLabel();
        jpCommandLeft.add(jlStatus);
        clearProgress();

        moBoolExportPaymentRequestsOnClose = new SBeanFieldBoolean();
        moBoolExportPaymentRequestsOnClose.setText("Exportar solicitudes de pago al cerrar");
        moBoolExportPaymentRequestsOnClose.setPreferredSize(new Dimension(250, 23));
        ((FlowLayout) jpCommandCenter.getLayout()).setAlignment(FlowLayout.RIGHT);
        jpCommandCenter.add(moBoolExportPaymentRequestsOnClose);
        moBoolExportPaymentRequestsOnClose.setEnabled(mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_PROFORMA);

        mbAllowLinkGreaterInvoices = miClient.getSession().getUser().hasPrivilege(SDataConstantsSys.PRV_PUR_LINK_INV_GREATER);

        jtfUserName.setText(miClient.getSession().getUser().getName());
        jtfUserName.setCaretPosition(0);

        try {
            maImportedDocuments = new ArrayList<>();
            maCRPs = new ArrayList<>();

            if (((SDataParamsCompany) miClient.getSession().getConfigCompany()).getIsFunctionalAreas()) {
                maFunctionalSubAreas = SDbFunctionalSubArea.readUserFunctionalSubAreas(miClient.getSession());
                msUserFunctionalSubAreaCodes = SDbFunctionalSubArea.composeFunctionalSubAreaCodes(maFunctionalSubAreas);

                if (msUserFunctionalSubAreaCodes.isEmpty()) {
                    msUserFunctionalSubAreaCodes = "¡NINGUNA!";
                    miClient.showMsgBoxWarning("El usuario '" + miClient.getSession().getUser().getName() + "' no podrá ver ni procesar " + getDocumentName("aa") + "s autorizadas porque no tiene subáreas funcionales asignadas.");
                }
            }
            else {
                SDbFunctionalSubArea functionalSubArea = (SDbFunctionalSubArea) miClient.getSession().readRegistry(SModConsts.CFGU_FUNC_SUB, new int[]{SModSysConsts.CFGU_FUNC_SUB_NA});
                maFunctionalSubAreas = new ArrayList<>();
                maFunctionalSubAreas.add(functionalSubArea);
                msUserFunctionalSubAreaCodes = functionalSubArea.getCode();
            }

            jtfUserFuncSubAreas.setText(msUserFunctionalSubAreaCodes);
            jtfUserFuncSubAreas.setCaretPosition(0);
            jtfUserFuncSubAreas.setToolTipText("Subáreas funcionales: " + msUserFunctionalSubAreaCodes);

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
                //syncHost = "http://192.168.1.87:8003"; // entorno César Orozco
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
            moPrepStatToGetProcessedProformaByExternalId = SImportedProforma.createPrepStatementToGetProcessedProformaByExternalId(miClient.getSession().getStatement());
            //considerar quitarlo
            //moPrepStatToGetDpsKeyByDocData = SImportedProforma.createPrepStatementToGetDpsKeyByDocData(miClient.getSession().getStatement(), SDataConstantsSys.TRNU_TP_DPS_PUR_INV);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private String getDocumentName(String sCase) {
        if (mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_PROFORMA) {
            if (sCase.equals("AA")) {
                return "PROFORMA";
            }
            else if (sCase.equals("Aa")) {
                return "Proforma";
            }
            else {
                return "proforma";
            }
        }
        else {
            if (sCase.equals("AA")) {
                return "CRP";
            }
            else if (sCase.equals("Aa")) {
                return "CPR";
            }
            else {
                return "CRP";
            }
        }
    }

    private void handleShowException(final Exception e) {
        System.err.println(e);
        SLibUtils.showException(this, e);

        actionPerformedClearProformas();
        jbShowProformas.requestFocusInWindow();
    }

    private void disableFieldsOfSearchBy() {
        moDatePeriodStart.setEditable(false);
        moDatePeriodEnd.setEditable(false);

        jbShowProformas.setEnabled(false);
    }

    private void enableFieldsOfSearchBy() {
        boolean isShowingDocsModeOff = mnShowingDocsMode == OFF;

        moDatePeriodStart.setEditable(isShowingDocsModeOff);
        moDatePeriodEnd.setEditable(isShowingDocsModeOff);
    }

    private void enableFieldsForShowingProforms(final boolean setShowingProformsModeOn) {
        mnShowingDocsMode = setShowingProformsModeOn ? ON : OFF;

        // START OF item-state-chage events free section if mbDocumentsBeingUpdated is true:
        // END OF item-state-chage events free section if mbDocumentsBeingUpdated is true:
        enableFieldsOfSearchBy();

        jbShowProformas.setEnabled(!setShowingProformsModeOn);
        jbClearProformas.setEnabled(setShowingProformsModeOn);

        jbSelectRemainingProformas.setEnabled(setShowingProformsModeOn);
        jbSelectAllProformas.setEnabled(setShowingProformsModeOn);
        jbDeselectAllProformas.setEnabled(setShowingProformsModeOn);

        jbDownloadSelectedProformas.setEnabled(setShowingProformsModeOn);
    }

    private void exportPaymentRequestsIfNeeded() {
        if (moBoolExportPaymentRequestsOnClose.isSelected()) {
            if (!mbExportPaymentRequests) {
                for (SGridRow row : moImportationsGrid.getModel().getGridRows()) {
                    SImportedProforma proforma = (SImportedProforma) row;
                    if (proforma.isPaymentRequested() && SLibUtils.belongsTo(proforma.Payment.getFkStatusPaymentId(), new int[]{SModSysConsts.FINS_ST_PAY_NEW, SModSysConsts.FINS_ST_PAY_SCHED_P})) {
                        mbExportPaymentRequests = true;
                        break;
                    }
                }
            }

            if (mbExportPaymentRequests) {
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

    private void initProgress() {
        jlProgress.setText("Preparando la petición...");

        jProgressBar.setValue(0);
        jProgressBar.setStringPainted(false);
        jProgressBar.setIndeterminate(true);
    }

    private void startProgress(final int docs) {
        jlProgress.setText("Procesando " + (docs == 1 ? "1 " + getDocumentName("aa") : docs + " " + getDocumentName("aa") + "s") + "...");

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

    private void processShowProforms(final HttpURLConnection connection, final SProgressCallback callback) throws Exception {
        int countRetreived = 0;
        int countElegible = 0;
        int countShown = 0;
        int companyId = miClient.getSession().getConfigCompany().getCompanyId();
        Exception exception = null;

        try {
            try (InputStream is = connection.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(is);

                if (root.isArray()) {
                    startProgress(root.size());

                    for (JsonNode docNode : root) {
                        callback.onProgress((int) ((++countRetreived / (double) root.size()) * 100));

                        JsonNode companyNode = docNode.path("company");

                        if (companyNode.get("external_id").asInt() == companyId
                                && docNode.get("transaction_class").asInt() == SSwapConsts.TXN_CAT_PURCHASE) {
                            countElegible++;

                            int externalProformaId = docNode.get("id").asInt();

                            JsonNode functionalAreaNode = docNode.path("functional_area");
                            int functionalSubAreaId = functionalAreaNode.get("external_id").asInt();

                            if (SDbFunctionalSubArea.belongsToFunctionalSubAreas(maFunctionalSubAreas, functionalSubAreaId)) {
                                String imporType;
                                if (mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_PROFORMA) {
                                    imporType = SDbComImportLog.SYNC_TYPE_PUR_PROF;
                                }
                                else {
                                    imporType = SDbComImportLog.SYNC_TYPE_PUR_PAY_RC;
                                }
                                int countOfImports = SImportUtils.countImports(moPrepStatToCountImports, 
                                                                                imporType,
                                                                                "" + SHttpConsts.RSC_SUCC_OK,
                                                                                miClient.getSession().getUser().getPkUserId(),
                                                                                "" + externalProformaId);

                                if (mnFormSubtype != SSwapConsts.TXN_DOC_TYPE_PROFORMA) {
                                    SImportedCRP oCrp = new SImportedCRP(docNode, externalProformaId, functionalSubAreaId, countOfImports > 0);
                                    maCRPs.add(oCrp);
                                    countShown++;
                                }
                                else {
                                    SImportedProforma oProforma = new SImportedProforma(docNode, externalProformaId, functionalSubAreaId, countOfImports > 0,
                                            miClient.getSession(), moPrepStatToGetProcessedProformaByExternalId);
                                    maImportedDocuments.add(oProforma);
                                    countShown++;
                                }
                            }
                            /* 2025-11-19, Sergio Flores: Uncomment fot debugging purposes:
                             else {
                             System.out.println("Documento no elegible: ID externo = " + externalDocumentId + "; ID subárea funcional = " + functionalSubAreaId + ".");
                             }
                             */
                        }
                    }
                }

                callback.onProgress(100);
                enableFieldsForShowingProforms(true);

                String range = "";

                range = (SLibTimeUtils.isSameDate(moDatePeriodStart.getValue(), moDatePeriodEnd.getValue())
                        ? ("Día:\n- " + SLibUtils.DateFormatDate.format(moDatePeriodStart.getValue()))
                        : ("Período:\n- del " + SLibUtils.DateFormatDate.format(moDatePeriodStart.getValue()) + " al " + SLibUtils.DateFormatDate.format(moDatePeriodEnd.getValue())));

                String message = "Resumen de la búsqueda de " + getDocumentName("aa") + "s autorizadas en " + SSwapConsts.PURCHASE_PORTAL + ":\n\n"
                        + "Empresa actual:\n- " + msCompanyName + ".\n"
                        + "Subáreas funcionales del usuario actual:\n- " + formatFunctionalSubAreasCodes() + ".\n"
                        + range + ".\n\n";

                message += "Búsqueda de " + getDocumentName("aa") + "s autorizadas:\n";

                if (countRetreived == 0) {
                    message += "- ¡No se encontraron " + getDocumentName("aa") + "s autorizadas!";

                    miClient.showMsgBoxWarning(message);
                }
                else {
                    if (countRetreived != countElegible) {
                        message += "- " + getDocumentName("Aa") + "s autorizadas totales: " + countRetreived + ";\n"; // this case should not happen
                    }

                    message += "- " + getDocumentName("Aa") + "s autorizadas de la empresa actual: " + countElegible + ";\n"
                            + "- " + getDocumentName("Aa") + "s autorizadas elegibles al usuario actual: " + countShown + ".";

                    miClient.showMsgBoxInformation(message);
                }
                itemStateChangedDocType(true);
            }
        }
        catch (Exception e) {
            exception = e;
            handleShowException(e);
        }
        finally {
            mbDocumentsBeingUpdated = false; // enables item state change events from being handled again!

            if (exception != null) {
                throw exception;
            }
        }
    }

    private void actionPerformedShowProformas() {
        SGuiValidation validation = null;
        String capacityLimit = "Por eficiencia en el procesamiento de su petición, la consulta está restringida máximo a ";

        validation = SGuiUtils.validateDateRange(moDatePeriodStart, moDatePeriodEnd);

        if (validation.isValid()) {
            if (SLibTimeUtils.countPeriodDays(moDatePeriodStart.getValue(), moDatePeriodEnd.getValue()) > LIMIT_DAYS) {
                validation.setMessage(capacityLimit + LIMIT_DAYS + " días.");
                validation.setComponent(moDatePeriodStart.getComponent());
            }
        }

        if (SGuiUtils.computeValidation(miClient, validation)) {
            try {
                mbDocumentsBeingUpdated = true; // prevents item-state-change events from being handled!

                disableFieldsOfSearchBy();

                String charset = java.nio.charset.StandardCharsets.UTF_8.name();
                String urlQuery = "";

                urlQuery = msSyncUrlRetrieveByPeriod;

                urlQuery = urlQuery.replace("<" + SSwapConsts.QRY_START_DATE + ">", SLibUtils.IsoFormatDate.format(moDatePeriodStart.getValue()));
                urlQuery = urlQuery.replace("<" + SSwapConsts.QRY_END_DATE + ">", SLibUtils.IsoFormatDate.format(moDatePeriodEnd.getValue()));
                urlQuery = urlQuery.replace("<" + SSwapConsts.QRY_DOCUMENT_TYPE + ">", "" + mnFormSubtype);

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
                initProgress();

                SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        processShowProforms(connection, progress -> {
                            publish(progress);
                        });
                        return null;
                    }

                    @Override
                    protected void process(List<Integer> chunks) {
                        int latest = chunks.get(chunks.size() - 1);
                        jProgressBar.setValue(latest);   // runs on EDT
                    }

                    @Override
                    protected void done() {
                        clearProgress();
                    }
                };

                worker.execute();
            }
            catch (Exception e) {
                handleShowException(e);
            }
        }
    }

    private void actionPerformedClearProformas() {
        try {
            mbDocumentsBeingUpdated = true; // prevents item-state-change events from being handled!

            maImportedDocuments.clear();
            maCRPs.clear();

            moImportationsGrid.populateGrid(new Vector<>());
            moImportationsGrid.getTable().getTableHeader().setReorderingAllowed(true);
            renderCurrentProforma();

            enableFieldsForShowingProforms(false);

            jlStatus.setText("");
            clearProgress();

            moDatePeriodStart.getComponent().requestFocusInWindow();
        }
        catch (Exception e) {
            System.err.println(e);
            SLibUtils.showException(this, e);
        }
        finally {
            mbDocumentsBeingUpdated = false; // enables item state change events from being handled again!
        }
    }

    private void actionPerformedSelectRemainingProformas() {
        for (SGridRow row : moImportationsGrid.getModel().getGridRows()) {
            if (((SImportedProforma) row).AlreadyDownloaded) {
                ((SImportedProforma) row).Download = false;
            }
            else {
                ((SImportedProforma) row).Download = true;
            }
        }

        int index = moImportationsGrid.getTable().getSelectedRow();
        moImportationsGrid.renderGridRows();
        moImportationsGrid.setSelectedGridRow(index);
    }

    private void actionPerformedSelectAllProformas() {
        for (SGridRow row : moImportationsGrid.getModel().getGridRows()) {
            ((SImportedProforma) row).Download = true;
        }

        int index = moImportationsGrid.getTable().getSelectedRow();
        moImportationsGrid.renderGridRows();
        moImportationsGrid.setSelectedGridRow(index);
    }

    private void actionPerformedDeselectAllProformas() {
        for (SGridRow row : moImportationsGrid.getModel().getGridRows()) {
            ((SImportedProforma) row).Download = false;
        }

        int index = moImportationsGrid.getTable().getSelectedRow();
        moImportationsGrid.renderGridRows();
        moImportationsGrid.setSelectedGridRow(index);
    }

    private void actionPerformedDownloadSelectedProformas() {
        ArrayList<Integer> documents = new ArrayList<>();

        if (mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_PROFORMA) {
            for (SGridRow row : moImportationsGrid.getModel().getGridRows()) {
                if (((SImportedProforma) row).Download) {
                    documents.add(((SImportedProforma) row).ExternalDocumentId);
                }
            }
        }
        else {
            for (SGridRow row : moImportationsGrid.getModel().getGridRows()) {
                if (((SImportedCRP) row).download) {
                    documents.add(((SImportedCRP) row).externalDocumentId);
                }
            }
        }

        if (documents.isEmpty()) {
            String sMessage = "";
            if (mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_PROFORMA) {
                sMessage = "una " + getDocumentName("aa") + " autorizada";
            }
            else {
                sMessage = "un " + getDocumentName("aa") + " de pago autorizado";
            }
            miClient.showMsgBoxWarning("Se debe seleccionar al menos " + sMessage + " para realizar la descarga.");
        }
        else if (documents.size() > LIMIT_DOWNLOADS && miClient.showMsgBoxConfirm("Se recomienda descargar los archivos en bloques no mayores a " + LIMIT_DOWNLOADS + " " + getDocumentName("aa") + "s autorizadas.\n"
                + "Sin embargo, puede intentar descargar las " + documents.size() + " " + getDocumentName("aa") + "s autorizadas seleccionadas.\n" + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
            miClient.showMsgBoxWarning("Se sugiere seleccionar hasta " + LIMIT_DOWNLOADS + " " + getDocumentName("aa") + "s autorizadas para realizar la descarga.");
        }
        else {
            try {
                File[] files = SImportUtils.downloadDocumentsAllFilesAsZip(miClient.getSession(), msSyncUrlDownload, documents, mnFormSubtype);

                if (files != null) {
                    File zipFile = files[SImportUtils.DOC_FILES_ZIP_IDX];

                    if (mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_PROFORMA) {
                        for (SGridRow row : moImportationsGrid.getModel().getGridRows()) {
                            if (((SImportedProforma) row).Download) {
                                int externalId = ((SImportedProforma) row).ExternalDocumentId;
                                for (Integer document : documents) {
                                    if (externalId == document) {
                                        ((SImportedProforma) row).Download = false; // uncheck downloaded documents
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    else {
                        for (SGridRow row : moImportationsGrid.getModel().getGridRows()) {
                            if (((SImportedCRP) row).download) {
                                int externalId = ((SImportedCRP) row).externalDocumentId;
                                for (Integer document : documents) {
                                    if (externalId == document) {
                                        ((SImportedCRP) row).download = false; // uncheck downloaded documents
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    int index = moImportationsGrid.getTable().getSelectedRow();
                    moImportationsGrid.renderGridRows();
                    moImportationsGrid.setSelectedGridRow(index);

                    String zipPath = zipFile.getAbsolutePath();
                    System.out.println("ZIP saved to: " + zipPath);

                    String message;

                    if (documents.size() == 1) {
                        message = "La " + getDocumentName("aa") + " autorizada seleccionada fue descargada en:\n";
                    }
                    else {
                        message = "Las " + SLibUtils.DecimalFormatInteger.format(documents.size()) + " " + getDocumentName("aa") + "s autorizadas seleccionadas fueron descargadas en:\n";
                    }

                    miClient.showMsgBoxInformation(message + zipPath);
                }
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
    }

    private void actionPerformedRequestPayment() {
        try {
            SGridRow row = moImportationsGrid.getSelectedGridRow();

            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SImportedProforma document = (SImportedProforma) row;

                if (document.requestPayment(miClient, msSyncUrlDownload)) {
                    int index = moImportationsGrid.getTable().getSelectedRow();
                    moImportationsGrid.renderGridRows();
                    moImportationsGrid.setSelectedGridRow(index);

                    mbExportPaymentRequests = true;
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private void actionPerformedChangeRequiredPaymentDate() {
        try {
            SGridRow row = moImportationsGrid.getSelectedGridRow();

            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SImportedProforma document = (SImportedProforma) row;

                if (document.isPaymentRequested()) {
                    String message = "La " + getDocumentName("aa") + " autorizada ya tiene solicitud de pago.\n";

                    switch (document.Payment.getFkStatusPaymentId()) {
                        case SModSysConsts.FINS_ST_PAY_NEW:
                            message += "Se puede cambiar la '" + jtfPayReqDate.getToolTipText().toLowerCase() + "' en " + jbChangePaymentRequiredDate.getToolTipText().toLowerCase() + "'.";
                            break;
                        case SModSysConsts.FINS_ST_PAY_SCHED:
                            message += "Se puede cambiar la '" + jtfPaySchedDate.getToolTipText().toLowerCase() + "' en " + jbChangePaymentScheduledDate.getToolTipText().toLowerCase() + "'.";
                            break;
                        default:
                            message += "Debido al estatus actual de la solicitud de pago, la '" + jtfReqPayReqDate.getToolTipText().toLowerCase() + "' no se puede modificar.";
                    }

                    throw new Exception(message);
                }
                else {
                    if (document.changeRequiredPaymentDate(miClient.getSession())) {
                        int index = moImportationsGrid.getTable().getSelectedRow();
                        moImportationsGrid.renderGridRows();
                        moImportationsGrid.setSelectedGridRow(index);

                        mbExportPaymentRequests = true;
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private void actionPerformedChangePaymentRequiredDate() {
        try {
            SGridRow row = moImportationsGrid.getSelectedGridRow();

            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SImportedProforma document = (SImportedProforma) row;

                if (!document.isPaymentRequested()) {
                    String message = "La " + getDocumentName("aa") + " autorizada no tiene solicitud de pago.\n"
                            + "Se puede cambiar la '" + jtfReqPayReqDate.getToolTipText().toLowerCase() + "' en '" + jbChangeRequiredPaymentDate.getToolTipText().toLowerCase() + "'.";

                    throw new Exception(message);
                }
                else if (document.Payment.getFkStatusPaymentId() != SModSysConsts.FINS_ST_PAY_NEW) {
                    String message = "No se puede cambiar la '" + jtfPayReqDate.getToolTipText().toLowerCase() + "', "
                            + "para poder hacerlo el estatus de la solicitud de pago debe ser '" + SDbPayment.ST_NEW + "'.";

                    if (document.Payment.getFkStatusPaymentId() == SModSysConsts.FINS_ST_PAY_SCHED) {
                        message += "\nSin embargo, como su estatus ya es '" + SDbPayment.ST_SCHED + "', se puede cambiar más bien la '" + jtfPaySchedDate.getToolTipText().toLowerCase() + "'.";
                    }

                    throw new Exception(message);
                }
                else {
                    if (document.changeRequiredPaymentDate(miClient.getSession())) {
                        int index = moImportationsGrid.getTable().getSelectedRow();
                        moImportationsGrid.renderGridRows();
                        moImportationsGrid.setSelectedGridRow(index);

                        mbExportPaymentRequests = true;
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private void actionPerformedChangePaymentScheduledDate() {
        try {
            SGridRow row = moImportationsGrid.getSelectedGridRow();

            if (row == null) {
                throw new Exception(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SImportedProforma document = (SImportedProforma) row;

                if (!document.isPaymentRequested()) {
                    String message = "La " + getDocumentName("aa") + " autorizada no tiene solicitud de pago.\n"
                            + "Se puede cambiar la '" + jtfReqPayReqDate.getToolTipText().toLowerCase() + "' en '" + jbChangeRequiredPaymentDate.getToolTipText() + "'.";

                    throw new Exception(message);
                }
                else if (document.Payment.getFkStatusPaymentId() != SModSysConsts.FINS_ST_PAY_SCHED) {
                    String message = "No se puede cambiar la '" + jtfPaySchedDate.getToolTipText().toLowerCase() + "', "
                            + "para poder hacerlo el estatus de la solicitud de pago debe ser '" + SDbPayment.ST_SCHED + "'.";

                    if (document.Payment.getFkStatusPaymentId() == SModSysConsts.FINS_ST_PAY_NEW) {
                        message += "\nSin embargo su estatus es '" + SDbPayment.ST_NEW + "', y se puede cambiar más bien la '" + jtfReqPayReqDate.getToolTipText().toLowerCase() + "'.";
                    }

                    throw new Exception(message);
                }
                else {
                    if (document.changeRequiredPaymentDate(miClient.getSession())) {
                        int index = moImportationsGrid.getTable().getSelectedRow();
                        moImportationsGrid.renderGridRows();
                        moImportationsGrid.setSelectedGridRow(index);

                        mbExportPaymentRequests = true;
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private void renderCurrentProforma() {
        SGridRow row = moImportationsGrid.getSelectedGridRow();

        if (row == null) {
            jbChangeRequiredPaymentDate.setEnabled(false);
            jbRequestPayment.setEnabled(false);
            jbChangePaymentRequiredDate.setEnabled(false);
            jbChangePaymentScheduledDate.setEnabled(false);

            jtfProforma.setText("");
            jtfProforma.setToolTipText(null);

            jtfInvoiceUserNew.setText("");

            jtfReqPayAmount.setText("");
            jtfReqPayAmountPct.setText("");
            jtfReqPayReqDate.setText("");

            jtfPayFolio.setText("");
            jtfPayDate.setText("");
            jtfPayReqDate.setText("");
            jtfPayStatus.setText("");
            jtfPaySchedDate.setText("");
            jtfPayExecDate.setText("");

        }
        else if (mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_PROFORMA) {
            SImportedProforma proforma = (SImportedProforma) row;

            jbChangeRequiredPaymentDate.setEnabled(true);
            jbRequestPayment.setEnabled(true);
            jbChangePaymentRequiredDate.setEnabled(true);
            jbChangePaymentScheduledDate.setEnabled(true);

            jtfProforma.setText(proforma.getFolio()); // show folio of current document as a visual indicator that is an invoice already linked!
            jtfProforma.setToolTipText(getDocumentName("Aa") + ": " + proforma.getFolio());

            if (proforma.ProcessedProforma != null) {
                jtfInvoiceUserNew.setText(proforma.ProcessedProforma.Usr);
            }

            jtfProforma.setCaretPosition(0);
            jtfInvoiceUserNew.setCaretPosition(0);

            if (!proforma.isPaymentRequestDataAvailable()) {
                jtfReqPayAmount.setText("");
                jtfReqPayAmountPct.setText("");
                jtfReqPayReqDate.setText("");
            }
            else {
                jtfReqPayAmount.setText(SLibUtils.getDecimalFormatAmount().format(proforma.Total * proforma.RequiredPaymentPct / 100) + " " + proforma.CurrencyCode);
                jtfReqPayAmountPct.setText(SLibUtils.DecimalFormatPercentage0D.format(proforma.RequiredPaymentPct / 100));
                jtfReqPayReqDate.setText(SLibUtils.GuiDateFormat.format(proforma.getRequiredPaymentDateEffective()));

                jtfReqPayAmount.setCaretPosition(0);
                jtfReqPayAmountPct.setCaretPosition(0);
                jtfReqPayReqDate.setCaretPosition(0);
            }

            if (!proforma.isPaymentRequested()) {
                jtfPayFolio.setText("");
                jtfPayDate.setText("");
                jtfPayReqDate.setText("");
                jtfPayStatus.setText("");
                jtfPaySchedDate.setText("");
                jtfPayExecDate.setText("");
            }
            else {
                jtfPayFolio.setText(proforma.Payment.getFolio());
                jtfPayDate.setText(SLibUtils.DateFormatDate.format(proforma.Payment.getDateApplication()));
                jtfPayReqDate.setText(SLibUtils.GuiDateFormat.format(proforma.Payment.getDateRequired()));
                jtfPayStatus.setText(proforma.Payment.getDbmsStatus());
                jtfPaySchedDate.setText(proforma.Payment.getDateSchedule_n() == null ? "ND" : SLibUtils.GuiDateFormat.format(proforma.Payment.getDateSchedule_n()));
                jtfPayExecDate.setText(proforma.Payment.getDateExecution_n() == null ? "ND" : SLibUtils.GuiDateFormat.format(proforma.Payment.getDateExecution_n()));

                jtfPayFolio.setCaretPosition(0);
                jtfPayDate.setCaretPosition(0);
                jtfPayReqDate.setCaretPosition(0);
                jtfPayStatus.setCaretPosition(0);
                jtfPaySchedDate.setCaretPosition(0);
                jtfPayExecDate.setCaretPosition(0);
            }
        }
        else {
        
        }
    }

    private void populateProformasGrid(final ArrayList<SImportedProforma> proformas,
            final ArrayList<SImportedCRP> crps,
            final boolean focusProformasGridTable) {
        if (mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_PROFORMA) {
            Collections.sort(proformas);
            moImportationsGrid.populateGrid(new Vector<>(proformas), this);
        }
        else {
            Collections.sort(crps);
            moImportationsGrid.populateGrid(new Vector<>(crps), this);
        }
        moImportationsGrid.getTable().getTableHeader().setReorderingAllowed(true);
        moImportationsGrid.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moImportationsGrid.setSelectedGridRow(0);

        if (focusProformasGridTable) {
            moImportationsGrid.getTable().requestFocusInWindow();
        }

        jlStatus.setText(getDocumentName("Aa") + "s autorizadas elegibles: " + SLibUtils.DecimalFormatInteger.format(maImportedDocuments.size()) + "; mostradas: " + SLibUtils.DecimalFormatInteger.format(proformas.size()));
    }

    private void itemStateChangedDocType(final boolean focusDocumentsGridTable) {
        populateProformasGrid(maImportedDocuments, maCRPs, focusDocumentsGridTable);
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
                        + "No se podrá importar o capturar proformas, hasta que se seleccione una sucursal de la empresa.");
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
        moBoolExportPaymentRequestsOnClose.setSelected(mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_PROFORMA);

        Date date = miClient.getSession().getCurrentDate();

        moDatePeriodStart.setValue(SLibTimeUtils.getBeginOfMonth(date));
        moDatePeriodEnd.setValue(SLibTimeUtils.getEndOfMonth(date));

        actionPerformedClearProformas();

        addAllListeners();
    }

    @Override
    public void addAllListeners() {
        jbShowProformas.addActionListener(this);
        jbClearProformas.addActionListener(this);
        jbSelectRemainingProformas.addActionListener(this);
        jbSelectAllProformas.addActionListener(this);
        jbDeselectAllProformas.addActionListener(this);
        jbDownloadSelectedProformas.addActionListener(this);

        jbChangeRequiredPaymentDate.addActionListener(this);
        jbRequestPayment.addActionListener(this);
        jbChangePaymentRequiredDate.addActionListener(this);
        jbChangePaymentScheduledDate.addActionListener(this);

    }

    @Override
    public void removeAllListeners() {
        jbShowProformas.removeActionListener(this);
        jbClearProformas.removeActionListener(this);
        jbSelectRemainingProformas.removeActionListener(this);
        jbSelectAllProformas.removeActionListener(this);
        jbDeselectAllProformas.removeActionListener(this);
        jbDownloadSelectedProformas.removeActionListener(this);

        jbChangeRequiredPaymentDate.removeActionListener(this);
        jbRequestPayment.removeActionListener(this);
        jbChangePaymentRequiredDate.removeActionListener(this);
        jbChangePaymentScheduledDate.removeActionListener(this);

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

            if (button == jbShowProformas) {
                actionPerformedShowProformas();
            }
            else if (button == jbClearProformas) {
                actionPerformedClearProformas();
            }
            else if (button == jbSelectRemainingProformas) {
                actionPerformedSelectRemainingProformas();
            }
            else if (button == jbSelectAllProformas) {
                actionPerformedSelectAllProformas();
            }
            else if (button == jbDeselectAllProformas) {
                actionPerformedDeselectAllProformas();
            }
            else if (button == jbDownloadSelectedProformas) {
                actionPerformedDownloadSelectedProformas();
            }
            else if (button == jbRequestPayment) {
                actionPerformedRequestPayment();
            }
            else if (button == jbChangeRequiredPaymentDate) {
                actionPerformedChangeRequiredPaymentDate();
            }
            else if (button == jbChangePaymentRequiredDate) {
                actionPerformedChangePaymentRequiredDate();
            }
            else if (button == jbChangePaymentScheduledDate) {
                actionPerformedChangePaymentScheduledDate();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            renderCurrentProforma();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}

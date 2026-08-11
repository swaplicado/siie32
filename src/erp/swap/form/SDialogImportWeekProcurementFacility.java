/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.form;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.lib.SLibConstants;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.cfg.db.SDbFunctionalSubArea;
import erp.mod.cfg.utils.SAuthJsonUtils;
import erp.mtrn.form.SDialogDpsFinder;
import erp.swap.SHttpConsts;
import erp.swap.SHttpStatusCodeException;
import erp.swap.SSwapConsts;
import erp.swap.model.SImportedCRP;
import erp.swap.utils.SExportUtils;
import erp.swap.utils.SImportUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
 * Importación de proformas de compras desde el Portal de Compras.
 * Ejemplo de la URL de consulta de documentos:
 * "https://transaction-backend-368437194061.us-central1.run.app/api/documents/filter-by-date-and-type/?start_date=2025-08-01&end_date=2025-09-30&document_type=41"
 * Ejemplo de la URL de descarga de documentos:
 * "https://transaction-backend-368437194061.us-central1.run.app/api/documents/download-docs-zip/"
 *
 * @author Adrián Avilés
 */
public class SDialogImportWeekProcurementFacility extends SBeanFormDialog implements ActionListener, ListSelectionListener {
    
    public static final int FORM_TYPE_WEEK_PROCUREMENT_FACILITY = 1;

    protected String msCompanyName;
    protected int mnShowingDocsMode;
    protected SGridPaneForm moImportationsGrid;
    protected SDialogDpsFinder moDialogDpsFinder;
    protected ArrayList<SImportWeekMovProcurementFacility> maImportedDocuments;
    protected ArrayList<SImportedCRP> maCRPs;
    protected ArrayList<SDbFunctionalSubArea> maFunctionalSubAreas;
    protected String msUserFunctionalSubAreaCodes;
    protected String msSyncUrlRetrieveByMonth;
    protected String msSyncUrlRetrieveByWeek;
    protected String msSyncUrlDownload;
    protected int mnSyncLimit;
    protected PreparedStatement moPrepStatToCountImports;
    protected PreparedStatement moPrepStatToGetProcessedProformaByExternalId;
    protected PreparedStatement moPrepStatToGetDpsKeyByDocData;
    protected JLabel jlStatus;
    protected SBeanFieldBoolean moBoolExportPaymentRequestsOnClose;
    protected boolean mbAllowLinkGreaterInvoices;
    protected boolean mbDocumentsBeingUpdated;
    protected boolean mbExportPaymentRequests;
    protected String sAmeToken;
    protected SImportProcurementFacility oProcurementFacility;
    private SFormEditWeekProcurementFacility moForm;
    protected ArrayList<SImportAccountingAccount> maImportedAccountingaccount;
    protected ArrayList<SImportCostCenter> maImportedCostCenter;
    protected ArrayList<SImportItems> maImportedItems;
    protected List<Integer> smaEdited = new ArrayList<>();

    /**
     * Creates new form SDialogImportDocuments
     *
     * @param client GUI client.
     * @param formSubtype
     * @param title
     * @param ameToken
     * @param procurementFacility
     * @param importedAccountingaccount
     * @param importedCostCenter
     * @param importedItems
     */
    public SDialogImportWeekProcurementFacility(
            SGuiClient client, 
            final int formSubtype, 
            String title, 
            String ameToken, 
            SImportProcurementFacility procurementFacility,
            ArrayList<SImportAccountingAccount> importedAccountingaccount,
            ArrayList<SImportCostCenter> importedCostCenter,
            ArrayList<SImportItems> importedItems
    ) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.CFGX_SWAP_IMP_AVO_WEEKS_MOVEMENTS, formSubtype, title);
        oProcurementFacility = procurementFacility;
        sAmeToken = ameToken;
        maImportedAccountingaccount = importedAccountingaccount;
        maImportedCostCenter = importedCostCenter;
        maImportedItems = importedItems;
        
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
        jpDownloadE = new javax.swing.JPanel();
        jpDownloadE1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jbOpenProcurementFacility = new javax.swing.JButton();
        jpDownloadE2 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel2b3 = new javax.swing.JLabel();
        jpDownloadE3 = new javax.swing.JPanel();
        jLabel3b1 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel3b3 = new javax.swing.JLabel();
        jpDocuments = new javax.swing.JPanel();
        jpDocumentsGrid = new javax.swing.JPanel();
        jpDocumentsGrid1 = new javax.swing.JPanel();
        jpDocumentsGrid11 = new javax.swing.JPanel();
        jpDocumentsGrid111 = new javax.swing.JPanel();
        jpDocumentsGrid112 = new javax.swing.JPanel();
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

        jpDownloadW.add(jpDownloadW1);

        jpDownload.add(jpDownloadW, java.awt.BorderLayout.WEST);

        jpDownloadE.setLayout(new java.awt.GridLayout(3, 1, 0, 1));

        jpDownloadE1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jLabel11.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE1.add(jLabel11);

        jLabel12.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE1.add(jLabel12);

        jbOpenProcurementFacility.setText("Modificar");
        jbOpenProcurementFacility.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbOpenProcurementFacility.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE1.add(jbOpenProcurementFacility);

        jpDownloadE.add(jpDownloadE1);

        jpDownloadE2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jLabel21.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE2.add(jLabel21);

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
        jpDocumentsGrid11.add(jpDocumentsGrid111);

        jpDocumentsGrid112.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpDocumentsGrid11.add(jpDocumentsGrid112);

        jpDocumentsGrid1.add(jpDocumentsGrid11, java.awt.BorderLayout.CENTER);

        jpDocumentsGrid12.setLayout(new java.awt.GridLayout(2, 1, 0, 2));

        jpDocumentsGrid121.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProgress.setBackground(java.awt.SystemColor.controlHighlight);
        jlProgress.setText("Progreso...");
        jlProgress.setOpaque(true);
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
        jlProforma.setText("Gasto:");
        jlProforma.setPreferredSize(new java.awt.Dimension(150, 23));
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
        jpDocuments.getAccessibleContext().setAccessibleDescription("");
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
    private javax.swing.JButton jbOpenProcurementFacility;
    private javax.swing.JButton jbRequestPayment;
    private javax.swing.JLabel jlPay;
    private javax.swing.JLabel jlPayExec;
    private javax.swing.JLabel jlPaySched;
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
    private javax.swing.JTextField jtfUserName;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods.
     */
    @SuppressWarnings("unchecked")
    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 670);
        
        jbSave.setEnabled(true);
        jbCancel.setText(SGuiConsts.TXT_BTN_CLOSE);
        jbCancel.setPreferredSize(new Dimension(75, 23));

        msCompanyName = SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.CFGU_CO, new int[]{miClient.getSession().getConfigCompany().getCompanyId()}, SLibConstants.DESCRIPTION_NAME);

        moImportationsGrid = new SGridPaneForm(miClient, SModConsts.CFGX_SWAP_IMP_PROFS, 1, "Proformas", null) {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha movimiento"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Concepto"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Referencia"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Debe"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Haber"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Moneda"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "CoCo clave"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "CoCo nombre"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Cuenta contable clave"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Cuenta contable nombre"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "RFC"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "usuario siie"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Costo unitario"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Cantidad"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_0D, "Cajas"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Tipo movimiento"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Item clave"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Item nombre"));
                
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

            ObjectMapper mapper = new ObjectMapper();
            JsonNode config = mapper.readTree(SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_AVO_CONFIG));

            msSyncUrlRetrieveByMonth = "";
            msSyncUrlRetrieveByWeek = "";
            mnSyncLimit = 0;

            // Recuperar la configuración base:
            String syncHost;

            syncHost = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AVO_SRV, SSwapConsts.CFG_ATT_URL);

            msSyncUrlRetrieveByMonth = syncHost + SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AVO_SRV_SEASON_EXPORT_MOVEMENTS, SSwapConsts.CFG_ATT_URL);
            msSyncUrlRetrieveByWeek = msSyncUrlRetrieveByMonth.substring(0, msSyncUrlRetrieveByMonth.indexOf("?") + 1);

            mnSyncLimit = SLibUtils.parseInt(SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_PUR_DOC, SSwapConsts.CFG_ATT_LIMIT));

            // Instanciar prepared statements:
            moPrepStatToCountImports = SImportUtils.createPrepStatementToCountImports(miClient.getSession().getStatement());
            moPrepStatToGetProcessedProformaByExternalId = SImportedProforma.createPrepStatementToGetProcessedProformaByExternalId(miClient.getSession().getStatement());
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private void handleShowException(final Exception e) {
        System.err.println(e);
        SLibUtils.showException(this, e);

        actionPerformedClearProformas();
    }

    private void enableFieldsForShowingProforms(final boolean setShowingProformsModeOn) {
        jbOpenProcurementFacility.setEnabled(setShowingProformsModeOn);
    }
    
    private void initProgress() {
        jlProgress.setText("Preparando la petición...");

        jProgressBar.setValue(0);
        jProgressBar.setStringPainted(false);
        jProgressBar.setIndeterminate(true);
    }

    private void startProgress(final int docs) {
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
    
    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }

    private void processUpdateAllWeekProcurementFacility(final SProgressCallback callback) throws Exception {
        int countUpdated = 0;
        int total = smaEdited.size();
        Exception exception = null;

        try {
            // Iniciar el progreso
            startProgress(total);

            // Construir el JSON de manera más eficiente
            StringBuilder jsonBody = new StringBuilder("");
            StringBuilder jsonMovements = new StringBuilder("");
            jsonBody
                .append("{")
                    .append("\"user_name\": ").append("\"").append(miClient.getSession().getUser().getName()).append("\",")
//                    .append("\"user_name\": ").append("\"").append("swapst").append("\",")
                    .append("\"movements\": ").append("[");
            
            for (int i = 0; i < smaEdited.size(); i++) {
                SImportWeekMovProcurementFacility row = maImportedDocuments.get(smaEdited.get(i));

                // Actualizar progreso
                int progress = (int) ((++countUpdated / (double) total) * 100);
                callback.onProgress(progress);

                // Construir el objeto JSON
                jsonMovements
                .append("{")
                    .append("\"id\": ").append(row.Id).append(",")
                    .append("\"item\": ").append(row.Item.Id).append(",")
                    .append("\"concept\": \"").append(escapeJson(row.Concept)).append("\",")
                    .append("\"reference\": \"").append(escapeJson(row.Reference)).append("\",")
                    .append("\"unit_cost\": ").append(row.Unit_cost).append(",")
                    .append("\"stock_in\": ").append(row.Stock_in).append(",")
                    .append("\"cash\": ").append( row.Debe != 0 ? row.Debe : row.Haber ).append(",")
                    .append("\"cost_center_code\": \"").append( row.getDataCostCenter() != null ? row.getDataCostCenter().getPkCostCenterIdXXX() : "" ).append("\",")
                    .append("\"accounting_account_code\": \"").append( row.oDataAccount.getPkAccountIdXXX() ).append("\",")
                    .append("\"business_partners\": ").append( row.oDataBizPartner != null ? "[{ "
                            + "\"business_partner_erp_id\":" + row.oDataBizPartner.getPkBizPartnerId()
                            + "\"business_partner_type\":" + (row.oDataBizPartner.getIsAttributeEmployee() ? 2 : 1)
                            + " }]" : "[]" )
                .append("}");
                        
                if (i < (smaEdited.size() - 1)) {
                    jsonMovements.append(",");
                }
            }
            jsonBody.append(jsonMovements);
            jsonBody.append("]")
                .append("}");

            // Enviar la petición (aquí no hay progreso, pero se puede reportar)
            callback.onProgress(95); // 95% = proceso casi completo

            // Hacer la petición HTTP
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode config = mapper.readTree(SCfgUtils.getParamValue(
                    miClient.getSession().getStatement(), 
                    SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_AVO_CONFIG
                ));
                String baseUrl = SAuthJsonUtils.getValueOfElementAsText(
                    config, 
                    SSwapConsts.CFG_OBJ_AVO_SRV, 
                    SSwapConsts.CFG_ATT_URL
                );
                String url = baseUrl + SAuthJsonUtils.getValueOfElementAsText(
                    config, 
                    SSwapConsts.CFG_OBJ_AVO_SRV_UPDATE_WEEK, 
                    SSwapConsts.CFG_ATT_URL
                );

                String cleanToken = sAmeToken.trim().replaceAll("^\"|\"$", "");
                String responseBody = SExportUtils.requestSwapService(
                    "", 
                    url, 
                    SHttpConsts.METHOD_PATCH, 
                    jsonBody.toString(), 
                    "Bearer " + cleanToken, 
                    "", 
                    SSwapConsts.TIME_30_SEC
                );

                JsonNode responseJson = mapper.readTree(responseBody);

                callback.onProgress(100);
                
                if (responseJson.isArray()) {
                    boolean hasError = false;

                    for (JsonNode node : responseJson) {
                        String status = node.path("status").asText();
                        String message = node.path("message").asText();
                        int id = node.path("id").asInt();

                        if ("error".equals(status)) {
                            hasError = true;
                            System.err.println("Error en ID " + id + ": " + message);
                        } else if ("success".equals(status)) {
                            System.out.println("Éxito en ID " + id + ": " + message);
                        }
                    }

                    if (hasError) {
                        // Manejar que hubo errores
                        throw new Exception("Se encontraron errores en la petición");
                    }
                }

                if (responseJson.has("error")) {
                    throw new Exception("Portal ame respondió: " + responseBody);
                }

            } catch (SHttpStatusCodeException ex) {
                throw new Exception(ex.getMessage());
            } catch (Exception ex) {
                throw new Exception(ex.getMessage());
            }

        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            if (exception == null) {
                enableFieldsForShowingProforms(true);
            }
        }
    }
    
    public void actionUpdateWeek() {
        // Validar que haya elementos editados
        if (smaEdited == null || smaEdited.isEmpty()) {
            miClient.showMsgBoxInformation("No hay registros editados para actualizar.");
            return;
        }

        // Confirmar antes de actualizar
        int confirm = miClient.showMsgBoxConfirm("¿Estás seguro de actualizar " + smaEdited.size() + " registro(s)?");
        if (confirm != 0) {
            return;
        }

        try {
            mbDocumentsBeingUpdated = true; // Evita eventos durante la actualización

            initProgress(); // Inicializa la barra de progreso

            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                @Override
                protected Void doInBackground() throws Exception {
                    // Llamar al método con callback de progreso
                    processUpdateAllWeekProcurementFacility(progress -> {
                        publish(progress);
                    });
                    return null;
                }

                @Override
                protected void process(List<Integer> chunks) {
                    int latest = chunks.get(chunks.size() - 1);
                    jProgressBar.setValue(latest); // Actualiza en EDT
                }

                @Override
                protected void done() {
                    clearProgress(); // Limpia la barra de progreso
                    mbDocumentsBeingUpdated = false;

                    try {
                        // Verificar si hubo errores
                        get(); // Esto lanza excepción si ocurrió en doInBackground
                        miClient.showMsgBoxInformation("¡Actualización completada exitosamente!");
                        // Refrescar la tabla después de actualizar
//                        moImportationsGrid.renderGridRows();
//                        smaEdited.clear(); // Limpiar la lista de editados
                    } catch (Exception e) {
                        miClient.showMsgBoxWarning("Error en la actualización: " + e.getMessage());
                    }
                }
            };

            worker.execute();

        } catch (Exception e) {
            clearProgress();
            mbDocumentsBeingUpdated = false;
            handleShowException(e);
        }
    }

    private void processShowWeekProcurementFacility(final HttpURLConnection connection, final SProgressCallback callback) throws Exception {
        int countRetreived = 0;
        Exception exception = null;
        Statement statement = miClient.getSession().getStatement();
        try {
            try (InputStream is = connection.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(is);

                if (root.isArray()) {
                    startProgress(root.size());

                    for (JsonNode docNode : root) {
                        callback.onProgress((int) ((++countRetreived / (double) root.size()) * 100));
                        SImportWeekMovProcurementFacility oWeekProcurementFacility = new SImportWeekMovProcurementFacility(docNode, statement);
                        maImportedDocuments.add(oWeekProcurementFacility);
                    }
                }

                callback.onProgress(100);
                enableFieldsForShowingProforms(true);
                
                SClientInterface client = (SClientInterface) miClient;
                
                String message = "Resumen de la búsqueda de movimiento de la bodega: " + oProcurementFacility.ProcurementFacilityName + "\n\n"
                        + "Semana del " + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(oProcurementFacility.StartDate) + " a " + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(oProcurementFacility.EndDate) + "\n\n"
                        + "Movimientos totales: " + countRetreived;

                if (countRetreived == 0) {
                    message = "No se encontraron movimientos para la bodega:  " + oProcurementFacility.ProcurementFacilityName + "\n\n"
                            + "Semana del " + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(oProcurementFacility.StartDate) + " a " + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(oProcurementFacility.EndDate) + "\n\n";
                }
                miClient.showMsgBoxInformation(message);
                
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
    
    public boolean validateFields(){
        boolean valid = true;
        return valid;
    }

    public void actionShowWeek() {
        boolean validation;
        validation = validateFields();

        if (validation) {
            try {
                mbDocumentsBeingUpdated = true;

                String charset = java.nio.charset.StandardCharsets.UTF_8.name();
                
                String urlQuery;
                urlQuery = msSyncUrlRetrieveByMonth;
                
                urlQuery = urlQuery.replace("<facility_season_week_id>", "" + oProcurementFacility.FacilitySeasonWeekId);
                urlQuery = urlQuery.replace("<company_id>", "" + miClient.getSession().getConfigCompany().getCompanyId());

                URL url = new URL(urlQuery);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setConnectTimeout(SSwapConsts.TIME_30_SEC); // timeout para conectar
                connection.setReadTimeout(SSwapConsts.TIME_30_SEC); // timeout para leer la respuesta
                connection.setRequestMethod(SHttpConsts.METHOD_GET);

                if (sAmeToken != null && !sAmeToken.isEmpty()) {
                    // Eliminar comillas dobles al inicio y al final
                    String cleanToken = sAmeToken.trim().replaceAll("^\"|\"$", "");

                    // Debug (opcional)
                    connection.setRequestProperty("Authorization", "Bearer " + cleanToken);
                }
                
                connection.setRequestProperty("Accept-Charset", charset);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");                
                
                connection.setDoInput(true); // true is already the default value!
                initProgress();

                SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        processShowWeekProcurementFacility(connection, progress -> {
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
            moImportationsGrid.getTable().setRowSorter(null);
            moImportationsGrid.getTable().getTableHeader().setReorderingAllowed(false);
            renderCurrentProforma();

            enableFieldsForShowingProforms(false);

            jlStatus.setText("");
            clearProgress();
        }
        catch (Exception e) {
            System.err.println(e);
            SLibUtils.showException(this, e);
        }
        finally {
            mbDocumentsBeingUpdated = false; // enables item state change events from being handled again!
        }
    }

    private void actionOpenSelectedWeekProcurementFacility() {
        SGridRow row = moImportationsGrid.getModel().getGridRows().get(moImportationsGrid.getTable().getSelectedRow());

        moForm = new SFormEditWeekProcurementFacility(miClient, mnFormType, "", maImportedAccountingaccount, maImportedCostCenter, maImportedItems);
        moForm.reloadCatalogues();
        moForm.setValue(FORM_TYPE_WEEK_PROCUREMENT_FACILITY, (SImportWeekMovProcurementFacility) row);
        moForm.setVisible(true);
        
        if (moForm.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
            SImportWeekMovProcurementFacility weekProcurementFacility = (SImportWeekMovProcurementFacility) moForm.getValue(1);
            for (int i = 0; i < maImportedDocuments.size(); i++) {
                SImportWeekMovProcurementFacility maImportedDocument = maImportedDocuments.get(i);

                if (maImportedDocument.Id == weekProcurementFacility.Id) {
                    maImportedDocument.setConcept(weekProcurementFacility.Concept);
                    maImportedDocument.setReference(weekProcurementFacility.Reference);
                    maImportedDocument.setDebe(weekProcurementFacility.Debe);
                    maImportedDocument.setHaber(weekProcurementFacility.Haber);
                    maImportedDocument.setFiscal_id(weekProcurementFacility.Fiscal_id);
                    maImportedDocument.setUnit_cost(weekProcurementFacility.Unit_cost);
                    maImportedDocument.setStock_in(weekProcurementFacility.Stock_in);

                    maImportedDocument.setCurrency(
                        weekProcurementFacility.oCurrency.Id,
                        weekProcurementFacility.oCurrency.Code,
                        weekProcurementFacility.oCurrency.Name
                    );

                    maImportedDocument.setItem(
                        weekProcurementFacility.Item.Id,
                        weekProcurementFacility.Item.Code,
                        weekProcurementFacility.Item.Name
                    );
                    
                    maImportedDocument.setDataAccount(weekProcurementFacility.oDataAccount);
                    maImportedDocument.setDataAccountMajor(weekProcurementFacility.oDataAccountMajor);
                    maImportedDocument.setDataCostCenter(weekProcurementFacility.oDataCostCenter);
                    maImportedDocument.setDataBizPartner(weekProcurementFacility.oDataBizPartner);

                    if (!smaEdited.contains(i)) {
                        smaEdited.add(i);
                    }
                }
            }
            moImportationsGrid.renderGridRows();
        }
        else {
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

            jbChangeRequiredPaymentDate.setEnabled(true);
            jbRequestPayment.setEnabled(true);
            jbChangePaymentRequiredDate.setEnabled(true);
            jbChangePaymentScheduledDate.setEnabled(true);
        }
    }

    private void populateWeekProcurementFacilityGrid(final ArrayList<SImportWeekMovProcurementFacility> proformas,
            final boolean focusProformasGridTable) {
        moImportationsGrid.populateGrid(new Vector<>(proformas), this);
        moImportationsGrid.getTable().setRowSorter(null);
        moImportationsGrid.getTable().getTableHeader().setReorderingAllowed(false);
        moImportationsGrid.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moImportationsGrid.setSelectedGridRow(0);

        if (focusProformasGridTable) {
            moImportationsGrid.getTable().requestFocusInWindow();
        }
        
        jlStatus.setText("Movimientos elegibles: "
                + SLibUtils.DecimalFormatInteger.format(maImportedDocuments.size()));
    }

    private void itemStateChangedDocType(final boolean focusDocumentsGridTable) {
        populateWeekProcurementFacilityGrid(maImportedDocuments, focusDocumentsGridTable);
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
        smaEdited.clear();

        mbExportPaymentRequests = false;
        moBoolExportPaymentRequestsOnClose.setSelected(mnFormSubtype == SSwapConsts.TXN_DOC_TYPE_PROFORMA);

        actionPerformedClearProformas();

        addAllListeners();
    }

    @Override
    public void addAllListeners() {
        jbOpenProcurementFacility.addActionListener(this);

        jbChangeRequiredPaymentDate.addActionListener(this);
        jbRequestPayment.addActionListener(this);
        jbChangePaymentRequiredDate.addActionListener(this);
        jbChangePaymentScheduledDate.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbOpenProcurementFacility.removeActionListener(this);

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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbOpenProcurementFacility) {
                actionOpenSelectedWeekProcurementFacility();
            }
            else if (button == jbSave) {
                actionSave();
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
    public void actionSave() {
        if (jbSave.isEnabled()) {
            actionUpdateWeek();
        }
    }
}

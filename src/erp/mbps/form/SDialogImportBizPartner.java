/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mbps.form;

import erp.mbps.data.SImportedBizPartner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.cfd.SCfdXmlCatalogs;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mbps.data.SDataBizPartnerBranchContact;
import erp.mbps.data.SDataBizPartnerCategory;
import erp.mcfg.data.SCfgUtils;
import erp.mcfg.data.SDataParamsCompany;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbFunctionalSubArea;
import erp.swap.SSwapConsts;
import erp.swap.utils.SImportUtils;
import erp.mod.cfg.utils.SAuthJsonUtils;
import erp.mtrn.form.SDialogDpsFinder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import sa.lib.gui.bean.SBeanFormDialog;


/**
 /**
 * Diálogo para la importación de proveedores autorizados desde el
 * Portal de Compras hacia el ERP.
 *
 * Esta clase permite:
 *  Consultar proveedores autorizados mediante servicios REST.
 *  Visualizar proveedores en un grid.
 *  Importar proveedores al catálogo de socios de negocios.
 *  Crear automáticamente sucursales, direcciones y contactos.
 *
 * El formulario utiliza componentes Swing y extiende
 * {@code SBeanFormDialog}, integrándose con la infraestructura
 * estándar del ERP.
 *
 *
 * @author Claudio Peña
 */
public class SDialogImportBizPartner extends SBeanFormDialog implements ActionListener, ListSelectionListener, ItemListener {

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
    protected boolean mbAllowLinkGreaterInvoices;
    private int idCountryMexPC = 150;
    private int idCountryMexSiie = 251;
    private int idBizPartnerIdentityTypeId = 1;

    protected boolean mbDocumentsBeingUpdated;
    protected boolean mbExportPaymentRequests;

    /**
     * Creates new form SDialogImportBizParther
     *
     * @param client GUI client.
     * @param formType
     * @param title
     */
    public SDialogImportBizPartner(SGuiClient client, final int formType, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, formType, 0, title);
        initComponents();
        initComponentsCustom();
        addAllListeners();
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
        jbShowBizPartner = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel2b4 = new javax.swing.JLabel();
        jLabel2b5 = new javax.swing.JLabel();
        jpDownloadE2 = new javax.swing.JPanel();
        jbClearBizPartner = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel2b3 = new javax.swing.JLabel();
        jLabel2b6 = new javax.swing.JLabel();
        jpDownloadE3 = new javax.swing.JPanel();
        jLabel3b1 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel3b3 = new javax.swing.JLabel();
        jLabel2b7 = new javax.swing.JLabel();
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
        jpProcessingN4 = new javax.swing.JPanel();
        jbImportBizPartner = new javax.swing.JButton();
        jpProcessingN7 = new javax.swing.JPanel();
        jbRejectBizPartner = new javax.swing.JButton();
        jpProcessingN8 = new javax.swing.JPanel();
        jpProcessingN11 = new javax.swing.JPanel();
        jpProcessingN12 = new javax.swing.JPanel();
        jpProcessingN14 = new javax.swing.JPanel();
        jpProcessingN15 = new javax.swing.JPanel();
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

        jpDownload.setBorder(javax.swing.BorderFactory.createTitledBorder("Búsqueda de proveedores autorizados"));
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

        jbShowBizPartner.setText("Mostrar proveedores");
        jbShowBizPartner.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbShowBizPartner.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE1.add(jbShowBizPartner);

        jLabel11.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE1.add(jLabel11);

        jLabel12.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE1.add(jLabel12);

        jLabel2b4.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE1.add(jLabel2b4);

        jLabel2b5.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE1.add(jLabel2b5);

        jpDownloadE.add(jpDownloadE1);

        jpDownloadE2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jbClearBizPartner.setText("Limpiar proveedores");
        jbClearBizPartner.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbClearBizPartner.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE2.add(jbClearBizPartner);

        jLabel21.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE2.add(jLabel21);

        jLabel22.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE2.add(jLabel22);

        jLabel2b3.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE2.add(jLabel2b3);

        jLabel2b6.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE2.add(jLabel2b6);

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

        jLabel2b7.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE3.add(jLabel2b7);

        jpDownloadE.add(jpDownloadE3);

        jpDownload.add(jpDownloadE, java.awt.BorderLayout.EAST);

        getContentPane().add(jpDownload, java.awt.BorderLayout.NORTH);
        jpDownload.getAccessibleContext().setAccessibleName("Búsqueda de  autorizadas:");

        jpDocuments.setBorder(javax.swing.BorderFactory.createTitledBorder("Proveedores autorizados"));
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
        jlProforma.setText((mnFormType == SSwapConsts.TXN_DOC_TYPE_PROFORMA) ? "Proforma" : "");
        jlProforma.setPreferredSize(new java.awt.Dimension(122, 23));
        jpProcessingN1.add(jlProforma);

        jpProcessingN.add(jpProcessingN1);

        jpProcessingN4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbImportBizPartner.setForeground(java.awt.Color.blue);
        jbImportBizPartner.setText("Importar proveedor");
        jbImportBizPartner.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbImportBizPartner.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN4.add(jbImportBizPartner);

        jpProcessingN.add(jpProcessingN4);

        jpProcessingN7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbRejectBizPartner.setForeground(java.awt.Color.red);
        jbRejectBizPartner.setText("Rechazar proveedor");
        jbRejectBizPartner.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbRejectBizPartner.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN7.add(jbRejectBizPartner);

        jpProcessingN.add(jpProcessingN7);

        jpProcessingN8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpProcessingN.add(jpProcessingN8);

        jpProcessingN11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpProcessingN.add(jpProcessingN11);

        jpProcessingN12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpProcessingN.add(jpProcessingN12);

        jpProcessingN14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpProcessingN.add(jpProcessingN14);

        jpProcessingN15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
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
    private javax.swing.JLabel jLabel2b4;
    private javax.swing.JLabel jLabel2b5;
    private javax.swing.JLabel jLabel2b6;
    private javax.swing.JLabel jLabel2b7;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel3b1;
    private javax.swing.JLabel jLabel3b3;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JButton jbClearBizPartner;
    private javax.swing.JButton jbImportBizPartner;
    private javax.swing.JButton jbRejectBizPartner;
    private javax.swing.JButton jbShowBizPartner;
    private javax.swing.JLabel jlPeriiod1;
    private javax.swing.JLabel jlPeriod;
    private javax.swing.JLabel jlProforma;
    private javax.swing.JLabel jlProgress;
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
    private javax.swing.JPanel jpProcessingN11;
    private javax.swing.JPanel jpProcessingN12;
    private javax.swing.JPanel jpProcessingN14;
    private javax.swing.JPanel jpProcessingN15;
    private javax.swing.JPanel jpProcessingN16;
    private javax.swing.JPanel jpProcessingN17;
    private javax.swing.JPanel jpProcessingN18;
    private javax.swing.JPanel jpProcessingN19;
    private javax.swing.JPanel jpProcessingN20;
    private javax.swing.JPanel jpProcessingN4;
    private javax.swing.JPanel jpProcessingN7;
    private javax.swing.JPanel jpProcessingN8;
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

        Date date = miClient.getSession().getCurrentDate();
        moDatePeriodStart.setValue(SLibTimeUtils.getBeginOfMonth(date));
        moDatePeriodEnd.setValue(SLibTimeUtils.getEndOfMonth(date));
        
        moFields.addField(moDatePeriodStart);
        moFields.addField(moDatePeriodEnd);
        moFields.setFormButton(jbShowBizPartner);
        
        jbRejectBizPartner.setForeground(Color.GRAY);
        jbRejectBizPartner.setEnabled(false);

        jbSave.setEnabled(false);
        jbCancel.setText(SGuiConsts.TXT_BTN_CLOSE);
        jbCancel.setPreferredSize(new Dimension(75, 23));

        msCompanyName = SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.CFGU_CO, new int[]{miClient.getSession().getConfigCompany().getCompanyId()}, SLibConstants.DESCRIPTION_NAME);
        mnShowingDocsMode = OFF;

        moImportationsGrid = new SGridPaneForm(miClient, 0, 0, "Proveedores", null) {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> cols = new ArrayList<>();
                cols.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Proveedor", 200));
                cols.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Email", 200));
                cols.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "RFC", 120));
                cols.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Área funcional", 120));
                cols.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE_DATETIME, "Fecha autorización", 150));
                SGridColumnForm colDownload = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Importado en SIIE",moImportationsGrid.getTable().getDefaultEditor(Boolean.class));
                colDownload.setEditable(false);
                cols.add(colDownload);

                return cols;
            }
        };

        moImportationsGrid.setForm(null);
        moImportationsGrid.setPaneFormOwner(null);
        jpDocumentsGrid.add(moImportationsGrid, BorderLayout.CENTER);

        jlStatus = new JLabel();
        jpCommandLeft.add(jlStatus);
        clearProgress();

        mbAllowLinkGreaterInvoices = miClient.getSession().getUser().hasPrivilege(SDataConstantsSys.PRV_PUR_LINK_INV_GREATER);

        jtfUserName.setText(miClient.getSession().getUser().getName());
        jtfUserName.setCaretPosition(0);

        try {
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
                //syncHost = "https://transaction-backend-test-515680676790.europe-west1.run.app"; // entorno pruebas
                syncHost = "https://transaction-backend-368437194061.us-central1.run.app"; // entorno producción
            }
            else {
                syncHost = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_SRV, SSwapConsts.CFG_ATT_URL);
            }

            msSyncToken = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_SRV, SSwapConsts.CFG_ATT_TOKEN);
            msSyncApiKey = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_SRV, SSwapConsts.CFG_ATT_API_KEY);

            // documents retreival service: /api/documents/filter-by-date-and-type/?start_date=<start_date>&end_date=<end_date>&document_type=<document_type>; date format: yyyy-mm-dd; document type format: 0 (raw integer)
            msSyncUrlRetrieveByPeriod = syncHost + "/api/partner-applying/get_partner_applaying/";
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
            moPrepStatToCountImports = SImportUtils.createPrepStatementToCountImports(miClient.getSession().getStatement());
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private String getDocumentName(String sCase) {
        return null;
       
    }

    private void clearProgress() {
        jlProgress.setText("");

        jProgressBar.setValue(0);
        jProgressBar.setStringPainted(false);
        jProgressBar.setIndeterminate(false);
    }
    
    private void actionPerformedShowBizPartner() {
    List<Map<String, Object>> results = new ArrayList<>();
    try {
        
        Date startDate = moDatePeriodStart.getValue();
        Date endDate = moDatePeriodEnd.getValue();
        if (startDate == null || endDate == null) {
            miClient.showMsgBoxWarning("Debe indicar el período inicial y final para consultar proveedores.");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = sdf.format(startDate);
        String endDateStr = sdf.format(endDate);
        String urlStr = "https://transaction-backend-368437194061.us-central1.run.app/api/partner-applying/get_partner_applaying/" + "?start_date=" + startDateStr + "&end_date=" + endDateStr + "&company_id=" + miClient.getSession().getConfigCompany().getCompanyId();

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();

        InputStream inputStream = (responseCode >= 200 && responseCode < 300) ? conn.getInputStream() : conn.getErrorStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        if (responseCode < 200 || responseCode >= 300) {
            throw new Exception("Error HTTP: " + responseCode + " - " + response.toString());
        }
        
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> json = mapper.readValue(response.toString(), new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
        results = mapper.convertValue(json.get("results"), new com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Object>>>() {} );
        
        ArrayList<SGridRow> rows = new ArrayList<>();
        Map<Integer, String> mapAreas = getFunctionalAreas();

        for (Map<String, Object> partner : results) {
            String name = partner.get("full_name") != null ? partner.get("full_name").toString() : "";
            String email = partner.get("email") != null ? partner.get("email").toString() : "";
            String fiscalId = partner.get("partner_fiscal_id") != null ? partner.get("partner_fiscal_id").toString() : "";

            String functionalArea = "";
            if (partner.get("functional_area") != null) {
                int areaId = Integer.parseInt(partner.get("functional_area").toString());
                functionalArea = mapAreas.getOrDefault(areaId, String.valueOf(areaId));
            }

            Date authorizedAt = null;
            if (partner.get("authorized_at") != null) {
                String dateStr = partner.get("authorized_at").toString();
                OffsetDateTime odt = OffsetDateTime.parse(dateStr);
                authorizedAt = Date.from(odt.toInstant());
            }

            boolean isImported = existsBizPartner(fiscalId);
            SImportedBizPartner row = new SImportedBizPartner(
                name, email, fiscalId, functionalArea, authorizedAt, partner
            );

            row.setIsImported(isImported);
            rows.add(row);
        }

        moImportationsGrid.populateGrid(new Vector<SGridRow>(rows));
        }
        catch (Exception e) {
            e.printStackTrace();
            miClient.showMsgBoxError("Error al consultar proveedores:\n" + e.getMessage());
        }
    }

    private Map<Integer, String> getFunctionalAreas() throws Exception {
        Map<Integer, String> map = new HashMap<>();

        String sql = "SELECT id_func_sub, name FROM cfgu_func_sub";
        Statement st = miClient.getSession().getStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            map.put(rs.getInt("id_func_sub"), rs.getString("name"));
        }

        rs.close();
        st.close();

        return map;
    }
    
    private void actionPerformedClearBizPartner() {
        try {
            mbDocumentsBeingUpdated = true; // prevents item-state-change events from being handled!

            moImportationsGrid.populateGrid(new Vector<>());

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
    
    private int getStateIdByName(String stateName) throws Exception {
        int id = 0;

        if (stateName == null || stateName.trim().isEmpty()) {
            return 0;
        }

        String sql = "SELECT id_sta " +
                     "FROM erp.LOCU_STA " +
                     "WHERE UPPER(sta) LIKE ? " +
                     "AND b_del = 0 " +
                     "LIMIT 1";

        PreparedStatement ps = miClient.getSession().getStatement().getConnection().prepareStatement(sql);
        ps.setString(1, "%" + stateName.trim().toUpperCase() + "%");
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            id = rs.getInt("id_sta");
        }
        rs.close();
        ps.close();

        return id;
    }
    
    private void actionPerformedImportBizPartner() {
        try {
            if (moImportationsGrid.getTable().getSelectedRow() == -1) {
                miClient.showMsgBoxWarning("Selecciona un registro.");
                return;
            }

            SImportedBizPartner row = (SImportedBizPartner) moImportationsGrid.getModel().getGridRows().get(moImportationsGrid.getTable().getSelectedRow());

            if (existsBizPartner(row.getFiscalId())) {
                miClient.showMsgBoxWarning("El proveedor ya existe: " + row.getFiscalId());
                return;
            }

            SDataBizPartner bp = new SDataBizPartner();
            bp.setIsRegistryNew(true);
            bp.setBizPartner(row.getName());
            if (row.getCountryId() == idCountryMexPC) {
                bp.setFiscalId(row.getFiscalId());
                bp.setFiscalFrgId("");
            } 
            else {
                bp.setFiscalId("XAXX010101000");
                bp.setFiscalFrgId(row.getFiscalId());
            }
            bp.setFirstname(row.getFirstName());
            bp.setLastname(row.getLastName());
            bp.setBizPartnerCommercial(row.getTradeName());
            if (row.getEntityType() == idBizPartnerIdentityTypeId) {
                bp.setFkBizPartnerIdentityTypeId(SDataConstantsSys.BPSS_TP_BP_IDY_PER);
                bp.setFkTaxIdentityId(SDataConstantsSys.BPSS_TP_BP_IDY_PER);
            }
            else {
                bp.setFkBizPartnerIdentityTypeId(SDataConstantsSys.BPSS_TP_BP_IDY_ORG);
                bp.setFkTaxIdentityId(SDataConstantsSys.BPSS_TP_BP_IDY_ORG);
            }
            bp.setFkBizAreaId(row.getFunctionalAreaId());
            bp.setIsSupplier(row.isVendor());
            bp.setIsCustomer(row.isCustomer());
            // Sucursal
            SDataBizPartnerBranch branch = new SDataBizPartnerBranch();
            branch.setIsRegistryNew(true);
            branch.setPkBizPartnerBranchId(0);
            branch.setBizPartnerBranch("Matriz");
            branch.setFkBizPartnerBranchTypeId(SDataConstantsSys.BPSS_TP_BPB_HQ);
            branch.setIsAddressPrintable(true);
            branch.setIsDeleted(false);
            //Dirección
            SDataBizPartnerBranchAddress address = new SDataBizPartnerBranchAddress();
            address.setIsRegistryNew(true);
            address.setPkBizPartnerBranchId(0);
            address.setFkAddressTypeId(SDataConstantsSys.BPSS_TP_ADD_OFF);
            address.setIsDeleted(false);
            address.setStreet(row.getStreet());
            address.setStreetNumberExt(row.getStreetNumberExt());
            address.setNeighborhood(row.getLocality());
            address.setLocality(row.getLocality());
            address.setCounty(row.getCounty());
            address.setZipCode(row.getZipCode());
            address.setFkCountryId_n(row.getCountryId());
            int countryId = row.getCountryId();
            if (countryId == idCountryMexPC) {
                address.setFkCountryId_n(idCountryMexSiie);
            } 
            else {
               address.setFkCountryId_n(countryId);
            }
            if (countryId == idCountryMexPC) {
                address.setFkCountryId_n(idCountryMexSiie);
                int stateId = getStateIdByName(row.getState());
                if (stateId > 0) {
                    address.setFkStateId_n(stateId);
                }
                else {
                    address.setFkStateId_n(0);
                }
            }
            branch.getDbmsBizPartnerBranchAddresses().add(address);
            
            if ((row.getEmail() != null && !row.getEmail().isEmpty()) ||
                (row.getPhone() != null && !row.getPhone().isEmpty())) {

                SDataBizPartnerBranchContact contact = new SDataBizPartnerBranchContact();
                contact.setIsRegistryNew(true);
                contact.setContact(row.getName());
                contact.setFkContactTypeId(SDataConstantsSys.BPSS_TP_CON_ADM);
                contact.setEmail01(row.getEmail());
                contact.setTelNumber01(row.getPhone());
                contact.setIsDeleted(false);

                branch.getDbmsBizPartnerBranchContacts().add(contact);
            }                
            bp.getDbmsBizPartnerBranches().add(branch);            
            SFormBizPartner form = new SFormBizPartner((SClientInterface) miClient);
            form.setValue(SDataConstantsSys.VALUE_BIZ_PARTNER_TYPE, new int[] { SDataConstants.BPSX_BP_SUP });

            form.formRefreshCatalogues();
            form.formReset();
            form.setRegistry(bp);
            form.setVisible(true);
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
            e.printStackTrace();
        }
    }
    
    private boolean existsBizPartner(String rfc) throws Exception {
        boolean exists = false;

        String sql = "SELECT COUNT(*) AS total FROM erp.bpsu_bp WHERE fiscal_id = '" + rfc + "' AND b_del = 0";

        Statement st = miClient.getSession().getStatement();
        ResultSet rs = st.executeQuery(sql);

        if (rs.next()) {
            exists = rs.getInt("total") > 0;
        }

        rs.close();
        st.close();

        return exists;
    }

    @Override
    protected void windowActivated() {
        if (mbFirstActivation) {
            if (((SClientInterface) miClient).getSessionXXX().getCurrentCompanyBranchId() == 0) {
                // no branch selected in current user session:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_SESSION_BRANCH + "\n"
                        + "No se podrá importar o crear proveedores, hasta que se seleccione una sucursal de la empresa.");
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

        Date date = miClient.getSession().getCurrentDate();

        moDatePeriodStart.setValue(SLibTimeUtils.getBeginOfMonth(date));
        moDatePeriodEnd.setValue(SLibTimeUtils.getEndOfMonth(date));

        addAllListeners();
    }

    @Override
    public void addAllListeners() {
        jbShowBizPartner.addActionListener(this);
        jbClearBizPartner.addActionListener(this);
        jbImportBizPartner.addActionListener(this);
     
    }

    @Override
    public void removeAllListeners() {
        jbShowBizPartner.removeActionListener(this);
        jbClearBizPartner.removeActionListener(this);
        jbImportBizPartner.removeActionListener(this);
       
    }

    @Override
    public void reloadCatalogues() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

            if (button == jbShowBizPartner) {
                actionPerformedShowBizPartner();
            }
            else if (button == jbClearBizPartner) {
                actionPerformedClearBizPartner();
            }
            else if (button == jbImportBizPartner) {
                actionPerformedImportBizPartner();
            }      
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

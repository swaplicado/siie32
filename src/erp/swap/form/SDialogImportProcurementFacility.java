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
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mcfg.data.SCfgUtils;
import erp.mcfg.data.SDataParamsCompany;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataFacilityRec;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.form.SDialogRecordPicker;
import erp.mfin.form.SFinRecordEntry;
import erp.mfin.form.SFinRecordUtils;
import erp.mfin.form.SFormRecord;
import erp.mod.SModConsts;
import erp.mod.cfg.utils.SAuthJsonUtils;
import erp.mtrn.form.SDialogDpsFinder;
import erp.redis.SLockUtils;
import erp.swap.SHttpConsts;
import erp.swap.SSwapConsts;
import erp.swap.utils.SAvoUtils;
import erp.swap.utils.SExportUtils;
import erp.swap.utils.SServicesUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
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
import sa.lib.gui.bean.SBeanFormDialog;
import sa.lib.srv.SLock;

/**
 * Importación de semanas de bodegas aguacate
 *
 * @author Adrián Avilés
 */
public class SDialogImportProcurementFacility extends SBeanFormDialog implements ActionListener, ListSelectionListener {
    
    protected String msCompanyName;
    protected int mnShowingDocsMode;
    protected SGridPaneForm moImportationsGrid;
    protected SDialogDpsFinder moDialogDpsFinder;
    protected ArrayList<SImportProcurementFacility> maImportedDocuments;
    protected ArrayList<SImportAccountingAccount> maImportedAccountingaccount;
    protected ArrayList<SImportCostCenter> maImportedCostCenter;
    protected ArrayList<SImportItems> maImportedItems;
    protected String msSyncUrlGetProcurementFacilities;
    protected String msSyncUrlGetLAccountingAccount;
    protected String msSyncUrlGetLCostCenter;
    protected String msSyncUrlGetLItems;
    protected String msSyncUrlGetMovements;
    protected String msSyncUrlPostToggleAccounting;
    protected PreparedStatement moPrepStatToCountImports;
    protected PreparedStatement moPrepStatToGetDpsKeyByDocData;
    protected JLabel jlStatus;
    protected boolean mbAllowLinkGreaterInvoices;
    protected boolean mbDocumentsBeingUpdated;
    protected boolean mbExportPaymentRequests;
    protected String sAmeToken;
    private erp.swap.form.SDialogImportWeekProcurementFacility moDialogImportWeekProcurementFacility;
    private erp.mfin.form.SDialogRecordPicker moDialogRecordPicker;
    private SDataRecord moCurrentRecord;
    protected ArrayList<SImportWeekMovProcurementFacility> maWeekMovProcurementFacility;
    protected ArrayList<SImportedBatchWeekMovProcurementFacility> maBatchWeekMovProcurementFacility;
    
    private SFinRecordEntry moFinRecordEntry;
    
    private boolean mbCanToAccount;
    private String msErrorMessageToAccount;
    List<SImportProcurementFacility> arrfacilities;
    List<SDataFacilityRec> arrDataFacilityRec;
    private java.util.HashMap<java.lang.String, sa.lib.srv.SLock> moRecordSLocksMap;
    
    protected boolean showMessageGetWeeks;

    /**
     * Creates new form SDialogImportDocuments
     *
     * @param client GUI client.
     * @param formSubtype
     * @param title
     */
    public SDialogImportProcurementFacility(SGuiClient client, final int formSubtype, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.CFGX_SWAP_IMP_AVO_WEEKS, formSubtype, title);
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
        jpDownloadW2 = new javax.swing.JPanel();
        jlPeriod = new javax.swing.JLabel();
        jcbMonths = new javax.swing.JComboBox<>();
        jpDownloadW3 = new javax.swing.JPanel();
        jlPeriod1 = new javax.swing.JLabel();
        moCalYear = new sa.lib.gui.bean.SBeanFieldCalendarYear();
        jpDownloadE = new javax.swing.JPanel();
        jpDownloadE1 = new javax.swing.JPanel();
        jbShowWeeks = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jbOpenProcurementFacility = new javax.swing.JButton();
        jbRecord = new javax.swing.JButton();
        jtfRecordSelected = new javax.swing.JTextField();
        jpDownloadE2 = new javax.swing.JPanel();
        jbClearWeeksProcurements = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jbSelectAll = new javax.swing.JButton();
        jbToAccount = new javax.swing.JButton();
        jbRejectWeek = new javax.swing.JButton();
        jpDownloadE3 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jbUnselectAll = new javax.swing.JButton();
        jbReverseAccounting = new javax.swing.JButton();
        jLabel2b5 = new javax.swing.JLabel();
        jpDownloadW4 = new javax.swing.JPanel();
        jckOnlyWithOutAccountFor = new javax.swing.JCheckBox();
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
        jlFolioRecord = new javax.swing.JLabel();
        jpProcessingN2 = new javax.swing.JPanel();
        jtfFolioRecord = new javax.swing.JTextField();
        jpProcessingN4 = new javax.swing.JPanel();
        jlReqPay = new javax.swing.JLabel();
        jpProcessingN5 = new javax.swing.JPanel();
        jtfRecordPeriod = new javax.swing.JTextField();
        jpProcessingN7 = new javax.swing.JPanel();
        jlReqPay1 = new javax.swing.JLabel();
        jpProcessingN8 = new javax.swing.JPanel();
        jtfRecordbranch = new javax.swing.JTextField();
        jpProcessingN10 = new javax.swing.JPanel();
        jlReqPay2 = new javax.swing.JLabel();
        jpProcessingN11 = new javax.swing.JPanel();
        jtfRecordCostCenter = new javax.swing.JTextField();
        jpProcessingN12 = new javax.swing.JPanel();
        jbOpenRecord = new javax.swing.JButton();
        jpProcessingN13 = new javax.swing.JPanel();
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

        jpDownload.setBorder(javax.swing.BorderFactory.createTitledBorder("Semanas de bodegas"));
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

        jpDownloadW2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPeriod.setText("Mes:");
        jlPeriod.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDownloadW2.add(jlPeriod);

        jcbMonths.setPreferredSize(new java.awt.Dimension(103, 21));
        jcbMonths.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbMonthsActionPerformed(evt);
            }
        });
        jpDownloadW2.add(jcbMonths);

        jpDownloadW.add(jpDownloadW2);

        jpDownloadW3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPeriod1.setText("Año");
        jlPeriod1.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDownloadW3.add(jlPeriod1);

        moCalYear.setDoubleBuffered(true);
        moCalYear.setPreferredSize(new java.awt.Dimension(103, 21));
        jpDownloadW3.add(moCalYear);

        jpDownloadW.add(jpDownloadW3);

        jpDownload.add(jpDownloadW, java.awt.BorderLayout.WEST);

        jpDownloadE.setLayout(new java.awt.GridLayout(3, 1, 0, 1));

        jpDownloadE1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jbShowWeeks.setText("Mostrar semanas");
        jbShowWeeks.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbShowWeeks.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE1.add(jbShowWeeks);

        jLabel11.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE1.add(jLabel11);

        jbOpenProcurementFacility.setText("Ver semana");
        jbOpenProcurementFacility.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbOpenProcurementFacility.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE1.add(jbOpenProcurementFacility);

        jbRecord.setText("Pólizas");
        jbRecord.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbRecord.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE1.add(jbRecord);

        jtfRecordSelected.setEditable(false);
        jtfRecordSelected.setToolTipText("");
        jtfRecordSelected.setFocusable(false);
        jtfRecordSelected.setMaximumSize(new java.awt.Dimension(150, 23));
        jtfRecordSelected.setMinimumSize(new java.awt.Dimension(150, 23));
        jtfRecordSelected.setPreferredSize(new java.awt.Dimension(150, 23));
        jtfRecordSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfRecordSelectedActionPerformed(evt);
            }
        });
        jpDownloadE1.add(jtfRecordSelected);

        jpDownloadE.add(jpDownloadE1);

        jpDownloadE2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jbClearWeeksProcurements.setText("Limpiar semanas");
        jbClearWeeksProcurements.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbClearWeeksProcurements.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE2.add(jbClearWeeksProcurements);

        jLabel21.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE2.add(jLabel21);

        jbSelectAll.setText("Seleccionar todas");
        jbSelectAll.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbSelectAll.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE2.add(jbSelectAll);

        jbToAccount.setText("Contabilizar");
        jbToAccount.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbToAccount.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE2.add(jbToAccount);

        jbRejectWeek.setText("Rechazar semana");
        jbRejectWeek.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbRejectWeek.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE2.add(jbRejectWeek);

        jpDownloadE.add(jpDownloadE2);

        jpDownloadE3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jLabel32.setPreferredSize(new java.awt.Dimension(5, 23));
        jpDownloadE3.add(jLabel32);

        jbUnselectAll.setText("Deleccionar todas");
        jbUnselectAll.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbUnselectAll.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE3.add(jbUnselectAll);

        jbReverseAccounting.setText("Descontabilizar");
        jbReverseAccounting.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbReverseAccounting.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE3.add(jbReverseAccounting);

        jLabel2b5.setPreferredSize(new java.awt.Dimension(150, 23));
        jpDownloadE3.add(jLabel2b5);

        jpDownloadE.add(jpDownloadE3);

        jpDownload.add(jpDownloadE, java.awt.BorderLayout.EAST);

        jpDownloadW4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckOnlyWithOutAccountFor.setText("Solo semanas sin contabilizar");
        jckOnlyWithOutAccountFor.setFocusable(false);
        jckOnlyWithOutAccountFor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jckOnlyWithOutAccountForActionPerformed(evt);
            }
        });
        jpDownloadW4.add(jckOnlyWithOutAccountFor);

        jpDownload.add(jpDownloadW4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpDownload, java.awt.BorderLayout.NORTH);
        jpDownload.getAccessibleContext().setAccessibleName("");

        jpDocuments.setBorder(javax.swing.BorderFactory.createTitledBorder("Semanas de bodegas"));
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

        jlFolioRecord.setText("Folio póliza:");
        jlFolioRecord.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN1.add(jlFolioRecord);

        jpProcessingN.add(jpProcessingN1);

        jpProcessingN2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfFolioRecord.setEditable(false);
        jtfFolioRecord.setText("ABC-000000");
        jtfFolioRecord.setToolTipText("");
        jtfFolioRecord.setFocusable(false);
        jtfFolioRecord.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN2.add(jtfFolioRecord);

        jpProcessingN.add(jpProcessingN2);

        jpProcessingN4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReqPay.setText("Período póliza:");
        jlReqPay.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN4.add(jlReqPay);

        jpProcessingN.add(jpProcessingN4);

        jpProcessingN5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfRecordPeriod.setEditable(false);
        jtfRecordPeriod.setText("ABC-000000");
        jtfRecordPeriod.setToolTipText("");
        jtfRecordPeriod.setFocusable(false);
        jtfRecordPeriod.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN5.add(jtfRecordPeriod);

        jpProcessingN.add(jpProcessingN5);

        jpProcessingN7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReqPay1.setText("Sucursal empresa:");
        jlReqPay1.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN7.add(jlReqPay1);

        jpProcessingN.add(jpProcessingN7);

        jpProcessingN8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfRecordbranch.setEditable(false);
        jtfRecordbranch.setText("ABC-000000");
        jtfRecordbranch.setToolTipText("");
        jtfRecordbranch.setFocusable(false);
        jtfRecordbranch.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN8.add(jtfRecordbranch);

        jpProcessingN.add(jpProcessingN8);

        jpProcessingN10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReqPay2.setText("Centro contable:");
        jlReqPay2.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN10.add(jlReqPay2);

        jpProcessingN.add(jpProcessingN10);

        jpProcessingN11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfRecordCostCenter.setEditable(false);
        jtfRecordCostCenter.setText("ABC-000000");
        jtfRecordCostCenter.setToolTipText("");
        jtfRecordCostCenter.setFocusable(false);
        jtfRecordCostCenter.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN11.add(jtfRecordCostCenter);

        jpProcessingN.add(jpProcessingN11);

        jpProcessingN12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbOpenRecord.setText("Abrir póliza");
        jbOpenRecord.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbOpenRecord.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProcessingN12.add(jbOpenRecord);

        jpProcessingN.add(jpProcessingN12);

        jpProcessingN13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpProcessingN.add(jpProcessingN13);

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
        jpDocuments.getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jcbMonthsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbMonthsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbMonthsActionPerformed

    private void jckOnlyWithOutAccountForActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jckOnlyWithOutAccountForActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jckOnlyWithOutAccountForActionPerformed

    private void jtfRecordSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfRecordSelectedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfRecordSelectedActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgDocMode;
    private javax.swing.ButtonGroup bgSearchBy;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel2b5;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JButton jbClearWeeksProcurements;
    private javax.swing.JButton jbOpenProcurementFacility;
    private javax.swing.JButton jbOpenRecord;
    private javax.swing.JButton jbRecord;
    private javax.swing.JButton jbRejectWeek;
    private javax.swing.JButton jbReverseAccounting;
    private javax.swing.JButton jbSelectAll;
    private javax.swing.JButton jbShowWeeks;
    private javax.swing.JButton jbToAccount;
    private javax.swing.JButton jbUnselectAll;
    private javax.swing.JComboBox<String> jcbMonths;
    private javax.swing.JCheckBox jckOnlyWithOutAccountFor;
    private javax.swing.JLabel jlFolioRecord;
    private javax.swing.JLabel jlPeriod;
    private javax.swing.JLabel jlPeriod1;
    private javax.swing.JLabel jlProgress;
    private javax.swing.JLabel jlReqPay;
    private javax.swing.JLabel jlReqPay1;
    private javax.swing.JLabel jlReqPay2;
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
    private javax.swing.JPanel jpDownloadW3;
    private javax.swing.JPanel jpDownloadW4;
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
    private javax.swing.JPanel jpProcessingN7;
    private javax.swing.JPanel jpProcessingN8;
    private javax.swing.JTextField jtfFolioRecord;
    private javax.swing.JTextField jtfRecordCostCenter;
    private javax.swing.JTextField jtfRecordPeriod;
    private javax.swing.JTextField jtfRecordSelected;
    private javax.swing.JTextField jtfRecordbranch;
    private javax.swing.JTextField jtfUserName;
    private sa.lib.gui.bean.SBeanFieldCalendarYear moCalYear;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods.
     */
    @SuppressWarnings("unchecked")
    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 670);

        String[] months = SLibTimeUtils.createMonthsOfYear(Locale.getDefault(), Calendar.LONG);
        for (String month : months) {
            jcbMonths.addItem(month);
        }
        
        jcbMonths.setSelectedIndex(SLibTimeUtils.digestMonth(miClient.getSession().getCurrentDate())[1] - 1);
        moCalYear.setValue(SLibTimeUtils.digestYear(miClient.getSession().getCurrentDate())[0]);

        moDialogRecordPicker = new SDialogRecordPicker((SClientInterface) miClient, SDataConstants.FINX_REC_USER);
        
        moFields.setFormButton(jbShowWeeks);

        jbSave.setEnabled(false);
        jbCancel.setText(SGuiConsts.TXT_BTN_CLOSE);
        jbCancel.setPreferredSize(new Dimension(75, 23));

        msCompanyName = SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.CFGU_CO, new int[]{miClient.getSession().getConfigCompany().getCompanyId()}, SLibConstants.DESCRIPTION_NAME);
        
        moImportationsGrid = new SGridPaneForm(miClient, SModConsts.CFGX_SWAP_PROCUREMENT_WEEKS, 1, "Bodegas", null) {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                SGridColumnForm column;
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_1B, "Semana #", 60));  // col 0
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha inicio"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha fin"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Bodega"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Estatus"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Tipo movimiento"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Movimientos"));
                column = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Contabilizado", 90);
                column.setEditable(false);
                gridColumnsForm.add(column);
                column = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Seleccionar", 70);
                column.setEditable(true);
                gridColumnsForm.add(column);
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Período póliza"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Sucursal empresa"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Centro contable"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Folio póliza"));
                
                return gridColumnsForm;
            }
        };

        moImportationsGrid.setForm(null);
        moImportationsGrid.setPaneFormOwner(null);
        jpDocumentsGrid.add(moImportationsGrid, BorderLayout.CENTER);

        jlStatus = new JLabel();
        jpCommandLeft.add(jlStatus);
        clearProgress();

        ((FlowLayout) jpCommandCenter.getLayout()).setAlignment(FlowLayout.RIGHT);

        mbAllowLinkGreaterInvoices = miClient.getSession().getUser().hasPrivilege(SDataConstantsSys.PRV_PUR_LINK_INV_GREATER);

        jtfUserName.setText(miClient.getSession().getUser().getName());
        jtfUserName.setCaretPosition(0);

        try {
            maImportedDocuments = new ArrayList<>();
            maImportedAccountingaccount = new ArrayList<>();
            maImportedCostCenter = new ArrayList<>();
            maImportedItems = new ArrayList<>();
            maWeekMovProcurementFacility = new ArrayList<>();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode config = mapper.readTree(SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_AVO_CONFIG));

            msSyncUrlGetProcurementFacilities = "";

            // Recuperar la configuración base de las URLS:
            String syncHost;
            syncHost = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AVO_SRV, SSwapConsts.CFG_ATT_URL);

            msSyncUrlGetProcurementFacilities = syncHost + SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AVO_SRV_SEASON_EXPORT, SSwapConsts.CFG_ATT_URL);
            msSyncUrlGetLAccountingAccount = syncHost + SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AVO_SRV_ACCOUNTING_ACCOUNTS, SSwapConsts.CFG_ATT_URL);
            msSyncUrlGetLCostCenter = syncHost + SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AVO_SRV_COST_CENTERS, SSwapConsts.CFG_ATT_URL);
            msSyncUrlGetLItems = syncHost + SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AVO_SRV_ITEMS, SSwapConsts.CFG_ATT_URL);
            msSyncUrlGetMovements = syncHost + SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AVO_SRV_BATCH_MOVEMENTS, SSwapConsts.CFG_ATT_URL);
            msSyncUrlPostToggleAccounting = syncHost + SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_AVO_SRV_TOGGLE_ACCOUNTING, SSwapConsts.CFG_ATT_URL);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private void handleShowException(final Exception e) {
        System.err.println(e);
        SLibUtils.showException(this, e);

        actionPerformedClearWeeks();
        jbShowWeeks.requestFocusInWindow();
    }

    private void disableFieldsOfSearchBy() {
        jbShowWeeks.setEnabled(false);
    }

    private void enableFieldsForShowingProcurementFacilities(final boolean setShowingProcurementFacilitiesModeOn) {
        jbShowWeeks.setEnabled(!setShowingProcurementFacilitiesModeOn);
        jbClearWeeksProcurements.setEnabled(setShowingProcurementFacilitiesModeOn);
        jbOpenProcurementFacility.setEnabled(setShowingProcurementFacilitiesModeOn);
        jbRecord.setEnabled(setShowingProcurementFacilitiesModeOn);
        jbSelectAll.setEnabled(setShowingProcurementFacilitiesModeOn);
        jbUnselectAll.setEnabled(setShowingProcurementFacilitiesModeOn);
        jbReverseAccounting.setEnabled(setShowingProcurementFacilitiesModeOn);
        jbRejectWeek.setEnabled(false);
    }
    
    private void initProgress() {
        jlProgress.setText("Preparando la petición...");
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
        jlProgress.setText("");
        jProgressBar.setValue(0);
        jProgressBar.setStringPainted(false);
        jProgressBar.setIndeterminate(false);
    }

    /**
     * Metodo para obtener y procesar la lista de las bodegas por semana,
     * regresa un array que se puede consultar en la ruta api/season-export/ de apiDog
     *
     * @param connectio
     * @param callback
     * @param percentaje
     * @param initPercentaje
     */
    private void processShowProcurementFacilities(final HttpURLConnection connection, final SProgressCallback callback, int percentaje, int initPercentaje) throws Exception {
        int countRetreived = 0;
        Exception exception = null;
        int countProcurement = 0;
        Statement statement = miClient.getSession().getStatement();

        try {
            try (InputStream is = connection.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(is);
                JsonNode weeks = root.path("weeks");

                if (weeks.isArray()) {
                    actionPerformedClearWeeks();
                    startProgress();
                    for (JsonNode docNode : weeks) {
                        callback.onProgress((int) ((++countRetreived / (double) root.size()) * percentaje));
                        JsonNode lProcurements = docNode.path("procurement_facilities");
                        for (JsonNode oProcurement : lProcurements) {
                            JsonNode accountingTypes = oProcurement.path("accounting_types");
                            for (JsonNode accountingType : accountingTypes) {
                                SImportProcurementFacility oProcurementFacilityWeek = new SImportProcurementFacility(
                                    root.get("season_year").asInt(),
                                    root.get("month_number").asInt(),
                                    docNode.get("week_month_number").asInt(),
                                    docNode.get("start_date").asText(),
                                    docNode.get("end_date").asText(),
                                    oProcurement,
                                    miClient.getSession().getStatement(),
                                    accountingType
                                );
                                
                                SDataFacilityRec oFacilityRec = new SDataFacilityRec();

                                if(oFacilityRec.findByExtDataId(oProcurementFacilityWeek.FacilitySeasonWeekId, oProcurementFacilityWeek.accountingTypeId, statement) == SLibConstants.DB_ACTION_READ_OK){
                                    oProcurementFacilityWeek.setIsAccountedFor(true);
                                }

                                if (jckOnlyWithOutAccountFor.isSelected() && !oProcurementFacilityWeek.isAccountedFor) {
                                    ++countProcurement;
                                    maImportedDocuments.add(oProcurementFacilityWeek);
                                }

                                if (!jckOnlyWithOutAccountFor.isSelected()) {
                                    ++countProcurement;
                                    maImportedDocuments.add(oProcurementFacilityWeek);
                                }
                            }
                        }
                    }
                }

                callback.onProgress(initPercentaje + percentaje);
                enableFieldsForShowingProcurementFacilities(true);
                
                String message = "Resumen de la búsqueda de semanas por bodega para " + jcbMonths.getSelectedItem().toString() + ":\n\n"
                        + "Semanas de " + jcbMonths.getSelectedItem().toString() + " por bodega: " + countRetreived + "\n\n"
                        + "Renglones totales: " + countProcurement;

                if (countRetreived == 0) {
                    message = "No se encontraron semanas por bodega para " + jcbMonths.getSelectedItem().toString();
                }
                
                if (showMessageGetWeeks) {
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
            mbDocumentsBeingUpdated = false;
            if (exception != null) {
                throw exception;
            }
        }
    }
    
    /**
     * Metodo para obtener y procesar la lista de las cuentas contables que se encuentran en el portal de AME,
     * regresa un array que se puede consultar en la ruta api/accounting-accounts/ de apiDog
     *
     * @param connectio
     * @param callback
     * @param percentaje
     * @param initPercentaje
     */
    private void processLAccountingAccount(final HttpURLConnection connection, final SProgressCallback callback, int percentaje, int initPercentaje) throws Exception {
        int countRetreived = initPercentaje;
        Exception exception = null;

        try {
            try (InputStream is = connection.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(is);

                if (root.isArray()) {
                    startProgress();

                    for (JsonNode docNode : root) {
                        callback.onProgress((int) ((++countRetreived / (double) root.size()) * percentaje));
                        SImportAccountingAccount oAccountingAccount = new SImportAccountingAccount(docNode);
                        maImportedAccountingaccount.add(oAccountingAccount);
                    }
                }

                callback.onProgress(initPercentaje + percentaje);
            }
        }
        catch (Exception e) {
            exception = e;
            handleShowException(e);
        }
        finally {
            if (exception != null) {
                throw exception;
            }
        }
    }
    
    /**
     * Metodo para obtener y procesar la lista de los centros de costo que se encuentran en el portal de AME,
     * regresa un array que se puede consultar en la ruta api/cost-centers/ de apiDog
     *
     * @param connectio
     * @param callback
     * @param percentaje
     * @param initPercentaje
     */
    private void processLCostCenter(final HttpURLConnection connection, final SProgressCallback callback, int percentaje, int initPercentaje) throws Exception {
        int countRetreived = initPercentaje;
        Exception exception = null;

        try {
            try (InputStream is = connection.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(is);

                if (root.isArray()) {
                    startProgress();

                    for (JsonNode docNode : root) {
                        callback.onProgress((int) ((++countRetreived / (double) root.size()) * percentaje));
                        SImportCostCenter oCostCenter = new SImportCostCenter(docNode);
                        maImportedCostCenter.add(oCostCenter);
                    }
                }

                callback.onProgress(initPercentaje + percentaje);
            }
        }
        catch (Exception e) {
            exception = e;
            handleShowException(e);
        }
        finally {
            if (exception != null) {
                throw exception;
            }
        }
    }
    
    /**
     * Metodo para obtener y procesar la lista de items contables que se encuentran en el portal de AME,
     * regresa un array que se puede consultar en la ruta /api/items/ de apiDog
     *
     * @param connectio
     * @param callback
     * @param percentaje
     * @param initPercentaje
     */
    private void processLItems(final HttpURLConnection connection, final SProgressCallback callback, int percentaje, int initPercentaje) throws Exception {
        int countRetreived = initPercentaje;
        Exception exception = null;

        try {
            try (InputStream is = connection.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(is);

                if (root.isArray()) {
                    startProgress();

                    for (JsonNode docNode : root) {
                        callback.onProgress((int) ((++countRetreived / (double) root.size()) * percentaje));
                        SImportItems oItem = new SImportItems(docNode);
                        maImportedItems.add(oItem);
                    }
                }

                callback.onProgress(initPercentaje + percentaje);
            }
        }
        catch (Exception e) {
            exception = e;
            handleShowException(e);
        }
        finally {
            if (exception != null) {
                throw exception;
            }
        }
    }
    
    public boolean validateFields(){
        boolean valid = true;
        if (jcbMonths.getSelectedItem().equals("")) {
            valid = false;
            miClient.showMsgBoxWarning("Se debe seleccionar mes");
        }
        if (moCalYear.getValue() <= 0){
            valid = false;
            miClient.showMsgBoxWarning("Se debe seleccionar un año");
        }
        return valid;
    }
    
    /**
     * Método genérico para crear conexiones
     *
     * @param urlString
     * @param method
     */
    private HttpURLConnection createConnection(String urlString, String method) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(SSwapConsts.TIME_30_SEC);
        connection.setReadTimeout(SSwapConsts.TIME_30_SEC);
        connection.setRequestMethod(method);

        // Configurar headers comunes
        String charset = java.nio.charset.StandardCharsets.UTF_8.name();
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        // Agregar token si existe
        if (sAmeToken != null && !sAmeToken.isEmpty()) {
            String cleanToken = sAmeToken.trim().replaceAll("^\"|\"$", "");
            connection.setRequestProperty("Authorization", "Bearer " + cleanToken);
        }

        connection.setDoInput(true);

        return connection;
    }

    /**
     * Metodo principal para obtener las cuentas contables, centros de costo, items y las bodegas por semana
     */
    private void actionShowWeeks() {
        boolean validation;
        validation = validateFields();

        if (validation) {
            try {
                mbDocumentsBeingUpdated = true;

                disableFieldsOfSearchBy();
                
                String urlQueryGetProcurementFacilities;
                urlQueryGetProcurementFacilities = msSyncUrlGetProcurementFacilities;
                urlQueryGetProcurementFacilities = urlQueryGetProcurementFacilities.replace("<season_year>", moCalYear.getValue().toString());
                urlQueryGetProcurementFacilities = urlQueryGetProcurementFacilities.replace("<month_number>", "" + jcbMonths.getSelectedIndex());
                urlQueryGetProcurementFacilities = urlQueryGetProcurementFacilities.replace("<only_accountable>", "" + true);
                urlQueryGetProcurementFacilities = urlQueryGetProcurementFacilities.replace("<group_by_accounting_type>", "" + true);
                
                HttpURLConnection connectionGetProcurementFacilities = createConnection(urlQueryGetProcurementFacilities, SHttpConsts.METHOD_GET);
                
                String urlQueryGetLAccountingAccount;
                urlQueryGetLAccountingAccount = msSyncUrlGetLAccountingAccount;
                urlQueryGetLAccountingAccount = urlQueryGetLAccountingAccount.replace("<company_erp_id>", "" + miClient.getSession().getConfigCompany().getCompanyId());
                
                HttpURLConnection connectionGetLAccountingAccount = createConnection(urlQueryGetLAccountingAccount, SHttpConsts.METHOD_GET);
                
                String urlQueryGetLCostCenter;
                urlQueryGetLCostCenter = msSyncUrlGetLCostCenter;
                urlQueryGetLCostCenter = urlQueryGetLCostCenter.replace("<company_erp_id>", "" + miClient.getSession().getConfigCompany().getCompanyId());
                
                HttpURLConnection connectionGetLCostCenter = createConnection(urlQueryGetLCostCenter, SHttpConsts.METHOD_GET);
                
                String urlQueryGetLItems;
                urlQueryGetLItems = msSyncUrlGetLItems;
                urlQueryGetLItems = urlQueryGetLItems.replace("<company_erp_id>", "" + miClient.getSession().getConfigCompany().getCompanyId());
                
                HttpURLConnection connectionGetLItems = createConnection(urlQueryGetLItems, SHttpConsts.METHOD_GET);

                initProgress();

                SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        processLAccountingAccount(connectionGetLAccountingAccount, progress -> {
                            publish(progress);
                        }, 25, 0);
                        
                        processLCostCenter(connectionGetLCostCenter, progress -> {
                            publish(progress);
                        }, 25, 25);
                        
                        processLItems(connectionGetLItems, progress -> {
                            publish(progress);
                        }, 25, 50);
                        
                        processShowProcurementFacilities(connectionGetProcurementFacilities, progress -> {
                            publish(progress);
                        }, 25, 75);
                        return null;
                    }

                    @Override
                    protected void process(List<Integer> chunks) {
                        int latest = chunks.get(chunks.size() - 1);
                        jProgressBar.setValue(latest);
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

    /**
     * Metodo para limpiar la tabla de las semanas de bodegas
     */
    private void actionPerformedClearWeeks() {
        try {
            mbDocumentsBeingUpdated = true; // prevents item-state-change events from being handled!

            maImportedDocuments.clear();
            moImportationsGrid.populateGrid(new Vector<>());
            moImportationsGrid.getTable().setRowSorter(null);
            moImportationsGrid.getTable().getTableHeader().setReorderingAllowed(false);
            renderCurrentRecord();

            enableFieldsForShowingProcurementFacilities(false);

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

    /**
     * Metodo para abrir una semana de una bodega y ver sus movimientos
     */
    private void actionOpenSelectedProcurementFacility() {
        SGridRow row = moImportationsGrid.getModel().getGridRows().get(moImportationsGrid.getTable().getSelectedRow());

        if(((SImportProcurementFacility) row).isAccountedFor) {
            miClient.showMsgBoxWarning("La semana ya fue contabilizada, no se puede editar.");
            return;
        }
        
        String title = "Importacion de gastos";
        moDialogImportWeekProcurementFacility = new SDialogImportWeekProcurementFacility(
                (SGuiClient) miClient, 
                SSwapConsts.TXN_DOC_TYPE_AVO_WEEKS_MOVEMENTS, 
                title, sAmeToken, 
                (SImportProcurementFacility) row,
                maImportedAccountingaccount,
                maImportedCostCenter,
                maImportedItems
        );
        
        moDialogImportWeekProcurementFacility.resetForm();
        moDialogImportWeekProcurementFacility.actionShowWeek();
        moDialogImportWeekProcurementFacility.setVisible(true);
    }
    
    /**
     * Metodo para mostrar la póliza seleccionada
     */
    private void renderCurrentRecord() {
        SGridRow row = moImportationsGrid.getSelectedGridRow();
        
        if (row != null) {
            SImportProcurementFacility oProcurement = ((SImportProcurementFacility) row);
            
            if (oProcurement.moRecord != null) {
                if (oProcurement.moRecord.getPkNumberId() != 0) {
                    jtfFolioRecord.setText(oProcurement.moRecord.getRecordNumber());
                    jtfRecordPeriod.setText(oProcurement.moRecord.getRecordPeriod());
                    jtfRecordbranch.setText(oProcurement.moRecord.getDbmsCompanyBranchCode());
                    jtfRecordCostCenter.setText(oProcurement.moRecord.getDbmsBookkeepingCenterCode());
                    jbOpenRecord.setEnabled(true);
                } else {
                    jtfFolioRecord.setText("");
                    jtfRecordPeriod.setText("");
                    jtfRecordbranch.setText("");
                    jtfRecordCostCenter.setText("");
                    jbOpenRecord.setEnabled(false);
                }
            }
        }
    }

    private void populateProcurementFacilityGrid(final ArrayList<SImportProcurementFacility> procurementFacility,
            final boolean focusProcurementFacilityGridTable) {
        moImportationsGrid.populateGrid(new Vector<>(procurementFacility), this);
        moImportationsGrid.getTable().setRowSorter(null);
        moImportationsGrid.getTable().setDragEnabled(false);
        moImportationsGrid.getTable().getTableHeader().setReorderingAllowed(false);
        moImportationsGrid.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moImportationsGrid.setSelectedGridRow(0);

        if (focusProcurementFacilityGridTable) {
            moImportationsGrid.getTable().requestFocusInWindow();
        }

        jlStatus.setText("Semanas por bodega elegibles: "
                + SLibUtils.DecimalFormatInteger.format(maImportedDocuments.size())
                + "; mostrados: "
                + SLibUtils.DecimalFormatInteger.format(procurementFacility.size()));
    }

    private void itemStateChangedDocType(final boolean focusDocumentsGridTable) {
        populateProcurementFacilityGrid(maImportedDocuments, focusDocumentsGridTable);
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
                        + "No se podrá importar semanas de bodega, hasta que se seleccione una sucursal de la empresa.");
            }

            super.windowActivated();
        }
    }

    @Override
    public void resetForm() {
        removeAllListeners();
        
        moRecordSLocksMap = new HashMap<>();
        
        try {
            sAmeToken = SAvoUtils.loginToAvoOperationControl(miClient.getSession());
        } catch (Exception ex) {
            Logger.getLogger(SDialogImportProcurementFacility.class.getName()).log(Level.SEVERE, null, ex);
        }

        mnFormResult = 0;
        mbFirstActivation = true;

        mbExportPaymentRequests = false;
        
        jbToAccount.setEnabled(false);
        jtfRecordSelected.setText("");
        moCurrentRecord = null;
        
        jtfFolioRecord.setText("");
        jtfRecordPeriod.setText("");
        jtfRecordbranch.setText("");
        jtfRecordCostCenter.setText("");
        jbOpenRecord.setEnabled(false);

        jcbMonths.setSelectedIndex(SLibTimeUtils.digestMonth(miClient.getSession().getCurrentDate())[1] - 1);
        moCalYear.setValue(SLibTimeUtils.digestYear(miClient.getSession().getCurrentDate())[0]);
        
        jckOnlyWithOutAccountFor.setSelected(false);
        jckOnlyWithOutAccountFor.setEnabled(true);

        actionPerformedClearWeeks();

        addAllListeners();
    }

    @Override
    public void addAllListeners() {
        jbShowWeeks.addActionListener(this);
        jbClearWeeksProcurements.addActionListener(this);
        jbOpenProcurementFacility.addActionListener(this);
        jbRecord.addActionListener(this);
        jbSelectAll.addActionListener(this);
        jbUnselectAll.addActionListener(this);
        jbToAccount.addActionListener(this);
        jbReverseAccounting.addActionListener(this);
        jbRejectWeek.addActionListener(this);
        jbOpenRecord.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbShowWeeks.removeActionListener(this);
        jbClearWeeksProcurements.removeActionListener(this);
        jbOpenProcurementFacility.removeActionListener(this);
        jbRecord.removeActionListener(this);
        jbSelectAll.removeActionListener(this);
        jbUnselectAll.removeActionListener(this);
        jbToAccount.removeActionListener(this);
        jbReverseAccounting.removeActionListener(this);
        jbRejectWeek.removeActionListener(this);
        jbOpenRecord.removeActionListener(this);
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

            if (button == jbShowWeeks) {
                showMessageGetWeeks = true;
                actionShowWeeks();
            }
            else if (button == jbClearWeeksProcurements) {
                actionPerformedClearWeeks();
            }
            else if (button == jbOpenProcurementFacility) {
                actionOpenSelectedProcurementFacility();
            }
            else if (button == jbRecord) {
                actionPerformedPickRecord();
            }
            else if (button == jbSelectAll) {
                actionPerformedSelectAllWeeks();
            }
            else if (button == jbUnselectAll) {
                actionPerformedDeselectAllWeeks();
            }
            else if (button == jbToAccount) {
                showMessageGetWeeks = false;
                actionToAccount();
            }
            else if (button == jbReverseAccounting) {
                showMessageGetWeeks = false;
                actionReverseAccounting();
            }
            else if (button == jbRejectWeek) {
                actionRejectWeek();
            }
            else if (button == jbOpenRecord) {
                openRecord();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            renderCurrentRecord();
        }
    }

    /**
     * Metodo para mostrar la información de la póliza de la semana seleccionada en la tabla
     */
    private void actionPerformedPickRecord() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M");
        try {
            
            Date dMovementDate = sdf.parse(moCalYear.getValue().toString() + "-" + (jcbMonths.getSelectedIndex() + 1));
            moDialogRecordPicker.formReset();
            moDialogRecordPicker.setFilterKey(dMovementDate);
            moDialogRecordPicker.formRefreshOptionPane();
            moDialogRecordPicker.setFormVisible(true);
            
            if (moDialogRecordPicker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                SDataRecord selectedRecord = (SDataRecord) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.FIN_REC, moDialogRecordPicker.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
                
                releaseRecordLock(moCurrentRecord);
                gainRecordLock(selectedRecord);
                
                moCurrentRecord = selectedRecord;
                jtfRecordSelected.setText(
                    moCurrentRecord.getRecordPeriod() + " "
                    + moCurrentRecord.getDbmsBookkeepingCenterCode() + " "
                    + moCurrentRecord.getDbmsCompanyBranchCode() + " "
                    + moCurrentRecord.getRecordNumber()
                );
                
                jbToAccount.setEnabled(true);
            }
            
        } catch (ParseException ex) {
            Logger.getLogger(SDialogImportProcurementFacility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SDialogImportProcurementFacility.class.getName()).log(Level.SEVERE, null, ex);
            SLibUtils.showException(this, ex);
        }
    }
    
    private void refreshWeeksGrid() {
        int index = moImportationsGrid.getTable().getSelectedRow();
        moImportationsGrid.renderGridRows();
        moImportationsGrid.setSelectedGridRow(index);
    }
    
    private void actionPerformedSelectAllWeeks() {
        for (SGridRow row : moImportationsGrid.getModel().getGridRows()) {
            ((SImportProcurementFacility) row).ToAccount = true;
        }
        refreshWeeksGrid();
    }
    
    private void actionPerformedDeselectAllWeeks() {
        for (SGridRow row : moImportationsGrid.getModel().getGridRows()) {
            ((SImportProcurementFacility) row).ToAccount = false;
        }
        refreshWeeksGrid();
    }
    
    /**
     * Metodo para obtener los movimientos de todas las semanas seleccionadas para su contabilización
     * @param callback 
     * @param percentaje 
     * @param initPercentaje 
     */
    private void processGetMovements(final SProgressCallback callback, int percentaje, int initPercentaje) throws Exception {
        int countUpdated = 0;
        int total = arrfacilities.size();
        int countErros = 0;
        maBatchWeekMovProcurementFacility = new ArrayList<>();
        
        int company_id = miClient.getSession().getConfigCompany().getCompanyId();
        
        Statement statement = miClient.getSession().getStatement();

        try {
            // Iniciar el progreso
            startProgress();

            // Construir el JSON de manera más eficiente
            StringBuilder jsonBody = new StringBuilder("");
            jsonBody.append("[");
            
            for (int i = 0; i < arrfacilities.size(); i++) {
                int facilityWeekId = arrfacilities.get(i).FacilitySeasonWeekId;
                int accountingTypeId = arrfacilities.get(i).accountingTypeId;

                // Actualizar progreso
                int progress = (int) ((++countUpdated / (double) total) * percentaje);
                callback.onProgress(progress);

                // Construir el objeto JSON
                jsonBody
                    .append("{")
                        .append("\"facility_season_week_id\": ").append(facilityWeekId).append(", ")
                        .append("\"company_id\": ").append(company_id).append(", ")
                        .append("\"accounting_type_id\": ").append(accountingTypeId)
                    .append("}");

                if (i < (arrfacilities.size() - 1)) {
                    jsonBody.append(",");
                }
            }
            jsonBody.append("]");

            // Hacer la petición HTTP
            try {
                ObjectMapper mapper = new ObjectMapper();
                String cleanToken = sAmeToken.trim().replaceAll("^\"|\"$", "");
                String responseBody = SExportUtils.requestSwapService(
                    "", 
                    msSyncUrlGetMovements,
                    SHttpConsts.METHOD_POST, 
                    jsonBody.toString(), 
                    "Bearer " + cleanToken, 
                    "", 
                    SSwapConsts.TIME_30_SEC
                );

                JsonNode responseJson = mapper.readTree(responseBody);

                if (responseJson.has("error")) {
                    throw new Exception("Portal ame respondió: " + responseBody);
                }
                
                // Crear un mapa con FacilitySeasonWeekId como clave y mnSortingPosition como valor
                Map<Integer, Integer> sortingPositionMap = arrfacilities.stream()
                    .collect(Collectors.toMap(
                        facility -> facility.FacilitySeasonWeekId,
                        facility -> facility.mnSortingPosition,
                        (existing, replacement) -> existing // En caso de duplicados, mantener el primero
                    ));
                
                for (JsonNode jsonNode : responseJson) {
                    maWeekMovProcurementFacility = new ArrayList<>();
                    JsonNode info = jsonNode.get("info");
                    JsonNode data = jsonNode.get("data");
                    
                    SImportedBatchWeekMovProcurementFacility oBatchWeekMovProcurementFacility = new SImportedBatchWeekMovProcurementFacility();
                    oBatchWeekMovProcurementFacility.setMnFacilitySeasonWeekId(info.get("facility_season_week_id").asInt());

                    oBatchWeekMovProcurementFacility.setMnProcurementId(info.get("facility_id").asInt());
                    oBatchWeekMovProcurementFacility.setMsProcurementName(info.get("facility_name").asText());
                    oBatchWeekMovProcurementFacility.setMnWeekNumebr(info.get("week_number").asInt());
                    oBatchWeekMovProcurementFacility.setMnAccountingTypeId(info.get("accounting_type_id").asInt());
                    
                    JsonNode cashHolding = info.path("cash_holding");
                    oBatchWeekMovProcurementFacility.setMnCashHoldingIdCob(cashHolding.get("id_cob").asInt());
                    oBatchWeekMovProcurementFacility.setMnCashHoldingIdEnt(cashHolding.get("id_ent").asInt());
                    
                    Optional<SImportProcurementFacility> procurementFacility = arrfacilities.stream()
                    .filter(p -> info.get("facility_season_week_id").asInt() == p.FacilitySeasonWeekId )
                    .findFirst();

                    SImportProcurementFacility oProcurementFacility = new SImportProcurementFacility();
                    if (procurementFacility.isPresent()) {
                        oProcurementFacility = procurementFacility.get();
                    }
                    
                    if (data.isArray()) {
                        for (JsonNode docNode : data) {
                            
                            SImportWeekMovProcurementFacility oWeekMovProcurementFacility = new SImportWeekMovProcurementFacility(docNode, statement, (SClientInterface) miClient, oProcurementFacility);
                            oWeekMovProcurementFacility.setMnFacilitySeasonWeekId(info.get("facility_season_week_id").asInt());
                            SValidateMovementWeekProcurementFacility oValid = new SValidateMovementWeekProcurementFacility(miClient, oWeekMovProcurementFacility);

                            if (oValid.isValid()) {
                                maWeekMovProcurementFacility.add(oWeekMovProcurementFacility);
                            } else {
                                mbCanToAccount = false;
                                countErros += 1;
                                if(countErros < 10) {
                                    msErrorMessageToAccount += oValid.getErros();
                                }
                            }
                        }
                    }
                    
                    // Actualizar cada elemento de maWeekMovProcurementFacility
                    maWeekMovProcurementFacility.forEach(weekMov -> {
                        Integer sortingPosition = sortingPositionMap.get(weekMov.mnFacilitySeasonWeekId);
                        if (sortingPosition != null) {
                            weekMov.mnSortingPosition = sortingPosition;
                        }
                    });
                    
                    ArrayList<SImportWeekMovProcurementFacility> counterpart = genereateCounterpart(oBatchWeekMovProcurementFacility, maWeekMovProcurementFacility);
                    maWeekMovProcurementFacility.addAll(counterpart);
                    
                    oBatchWeekMovProcurementFacility.setMaWeekMovProcurementFacility(maWeekMovProcurementFacility);
                    maBatchWeekMovProcurementFacility.add(oBatchWeekMovProcurementFacility);
                }

                callback.onProgress(initPercentaje + percentaje);

            } catch (Exception ex) {
                throw new Exception(ex.getMessage());
            }

        } catch (Exception e) {
            throw e;
        } finally {
            
        }
    }
    
    /**
     * Metodo para marcar como contabilizado/sin contabilizar una semana en el portal de AME
     * @param facilityWeekId 
     * @param accounting_type_id
     * @param action
     */
    private void processPostToggleAccounting(int facilityWeekId, int accounting_type_id, boolean action) throws Exception {
        int company_id = miClient.getSession().getConfigCompany().getCompanyId();

        try {
            // Iniciar el progreso
            startProgress();

            // Construir el JSON de manera más eficiente
            StringBuilder jsonBody = new StringBuilder("");

                // Construir el objeto JSON
                jsonBody
                    .append("{")
                        .append("\"facility_season_week_id\": ").append(facilityWeekId).append(",")
                        .append("\"company_erp_id\": ").append(company_id).append(",")
                        .append("\"action\": ").append( action ? 1 : 0 ).append(",")
                        .append("\"accounting_type_id\": ").append(accounting_type_id)
                    .append("}");

            // Hacer la petición HTTP
            try {
                ObjectMapper mapper = new ObjectMapper();
                String cleanToken = sAmeToken.trim().replaceAll("^\"|\"$", "");
                String responseBody = SExportUtils.requestSwapService(
                    "", 
                    msSyncUrlPostToggleAccounting,
                    SHttpConsts.METHOD_POST, 
                    jsonBody.toString(), 
                    "Bearer " + cleanToken, 
                    "", 
                    SSwapConsts.TIME_30_SEC
                );

                JsonNode responseJson = mapper.readTree(responseBody);

                if (responseJson.has("error")) {
                    throw new Exception("Portal ame respondió: " + responseBody);
                }
            } catch (Exception ex) {
                throw new Exception(ex.getMessage());
            }

        } catch (Exception e) {
            throw e;
        } finally {
            
        }
    }
    
    /**
     * Metodo para guardar los renglones en la póliza y guardar la relación de
     * semana contabilizada vs póliza
     *
     * @param callback
     * @param percentaje 
     * @param initPercentaje 
     */
    private void processWeeksToAccount(final SProgressCallback callback, int percentaje, int initPercentaje) throws Exception {
        if (!mbCanToAccount) {
            throw new Exception(msErrorMessageToAccount);
        }
        int countUpdated = 0;
        int total = arrfacilities.size();
        Connection con = miClient.getSession().getStatement().getConnection();
        startProgress();
        
        for (SImportedBatchWeekMovProcurementFacility oBatchWeekMovProcurementFacility : maBatchWeekMovProcurementFacility) {
            ArrayList<SImportWeekMovProcurementFacility> arrWeekMov = oBatchWeekMovProcurementFacility.getMaWeekMovProcurementFacility();
            int sortPosition = moCurrentRecord.getLastSortingPosition();
            for (SImportWeekMovProcurementFacility otWeekProcurementFacility : arrWeekMov) {
                sortPosition += 1;
                int progress = (int) ((++countUpdated / (double) total) * percentaje);
                callback.onProgress(progress);
                moFinRecordEntry = new SFinRecordEntry();

                moFinRecordEntry.AccountId = otWeekProcurementFacility.oDataAccount.getPkAccountIdXXX();
                moFinRecordEntry.Account = otWeekProcurementFacility.getDataAccount();
                moFinRecordEntry.AccountMajor = otWeekProcurementFacility.getDataAccountMajor();
                moFinRecordEntry.Concept = otWeekProcurementFacility.Concept;
                moFinRecordEntry.Debit = otWeekProcurementFacility.Debe;
                moFinRecordEntry.Credit = otWeekProcurementFacility.Haber;
                moFinRecordEntry.ExchangeRate = 1;
                moFinRecordEntry.ExchangeRateSystem = 1;
                moFinRecordEntry.DebitCy = otWeekProcurementFacility.Debe;
                moFinRecordEntry.CreditCy = otWeekProcurementFacility.Haber;
                moFinRecordEntry.CurId = otWeekProcurementFacility.oCurrency.Id;
                moFinRecordEntry.IsExchangeDifference = false;
                moFinRecordEntry.IsSystem = true;
                moFinRecordEntry.IsDeleted = false;
                moFinRecordEntry.Reference = otWeekProcurementFacility.Reference;
                moFinRecordEntry.IsReferenceTax = false;
                moFinRecordEntry.ItemKey = otWeekProcurementFacility.Item != null ? otWeekProcurementFacility.Item.Id : 0;
                moFinRecordEntry.Quantity = otWeekProcurementFacility.Stock_in;
                moFinRecordEntry.Year = moCalYear.getValue();
                moFinRecordEntry.CostCenter =  otWeekProcurementFacility.oDataCostCenter != null ? otWeekProcurementFacility.oDataCostCenter.getPkCostCenterIdXXX() : "";
                moFinRecordEntry.IsBizPartnerRequired = otWeekProcurementFacility.oDataAccountMajor.getIsRequiredBizPartner();

                if (otWeekProcurementFacility.getDataBizPartner() != null) {
                    moFinRecordEntry.BizPartnerId = otWeekProcurementFacility.getDataBizPartner().getPkBizPartnerId();
                    SDataBizPartner bp = (SDataBizPartner) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BP, new int[] {otWeekProcurementFacility.getDataBizPartner().getPkBizPartnerId()}, SLibConstants.EXEC_MODE_VERBOSE);
                    moFinRecordEntry.IsForegn = !bp.getFiscalFrgId().isEmpty();
                }
                
                if (otWeekProcurementFacility.getDataAccountCash() != null) {
                    moFinRecordEntry.EntityKey = new int[] {otWeekProcurementFacility.getDataAccountCash().getPkCompanyBranchId(), otWeekProcurementFacility.getDataAccountCash().getPkAccountCashId()};
                }

                SDataRecordEntry moRecordEntry = SFinRecordUtils.composeRecordEntry((SClientInterface) miClient, null, moFinRecordEntry);
                
                moRecordEntry.setPkYearId(moCurrentRecord.getPkYearId());
                moRecordEntry.setPkPeriodId(moCurrentRecord.getPkPeriodId());
                moRecordEntry.setPkBookkeepingCenterId(moCurrentRecord.getPkBookkeepingCenterId());
                moRecordEntry.setPkRecordTypeId(moCurrentRecord.getPkRecordTypeId());
                moRecordEntry.setPkNumberId(moCurrentRecord.getPkNumberId());
                moRecordEntry.setUserId(otWeekProcurementFacility.getMnSortingPosition());
                moRecordEntry.setSortingPosition(sortPosition);
                moCurrentRecord.getDbmsRecordEntries().add(moRecordEntry);
            }
            
            if (arrWeekMov.size() > 0) {
                moCurrentRecord.save(con);
                
                Optional<SImportProcurementFacility> optionalFacility = arrfacilities.stream()
                    .filter(obj -> 
                            obj.FacilitySeasonWeekId == oBatchWeekMovProcurementFacility.getMnFacilitySeasonWeekId() &&
                            obj.accountingTypeId == oBatchWeekMovProcurementFacility.getMnAccountingTypeId()
                    )
                    .findFirst();

                if (optionalFacility.isPresent()) {
                    SImportProcurementFacility oFacility = optionalFacility.get();
                    SDataFacilityRec oFacilityRec = new SDataFacilityRec();
                    oFacilityRec.setMnRecYear(oFacility.Year);
                    oFacilityRec.setMnRecMonth(oFacility.MonthNumber);
                    oFacilityRec.setMnRecWeek(oFacility.WeekMonthNumber);
                    oFacilityRec.setMnExtDataId(oFacility.FacilitySeasonWeekId);
                    oFacilityRec.setMnUserId(oFacility.mnSortingPosition);
                    oFacilityRec.setMnFkExtFacility(oFacility.ProcurementFacilityId);
                    oFacilityRec.setMnFkRecYear(moCurrentRecord.getPkYearId());
                    oFacilityRec.setMnFkRecPer(moCurrentRecord.getPkPeriodId());
                    oFacilityRec.setMnFkRecBkc(moCurrentRecord.getPkBookkeepingCenterId());
                    oFacilityRec.setMsFkRecTpRec(moCurrentRecord.getPkRecordTypeId());
                    oFacilityRec.setMnFkRecNum(moCurrentRecord.getPkNumberId());
                    oFacilityRec.setMnFkUserNewId(miClient.getSession().getUser().getPkUserId());
                    oFacilityRec.setMnFkUserEditId(miClient.getSession().getUser().getPkUserId());
                    oFacilityRec.setMnFkUserDeleteId(miClient.getSession().getUser().getPkUserId());
                    oFacility.setIsAccountedFor(true);
                    oFacility.setMoRecord(moCurrentRecord);
                    oFacility.setToAccount(false);
                    oFacilityRec.setMnAccountingTypeId(oFacility.accountingTypeId);
                    oFacilityRec.save(con);
                    
                    processPostToggleAccounting(oFacility.FacilitySeasonWeekId, oFacility.accountingTypeId, true);
                }
            }
            
        }

        callback.onProgress(initPercentaje + percentaje);
    }
   
    /**
     * Metodo para descontabilizar semanas de la póliza
     *
     * @param callback
     * @param percentaje 
     * @param initPercentaje 
     */
    private boolean reverseAccounting(final SProgressCallback callback, int percentaje, int initPercentaje) throws SQLException {
        arrfacilities = new ArrayList<>();
        arrDataFacilityRec = new ArrayList<>();
        mbCanToAccount = true;
        msErrorMessageToAccount = "";
        
        Statement statement = miClient.getSession().getStatement();
        Connection con = miClient.getSession().getStatement().getConnection();
        
        int countUpdated = 0;
        int total = arrfacilities.size();
        
        boolean result = true;
        
        for (int i = 0; i < maImportedDocuments.size(); i++) {
            
            int progress = (int) ((++countUpdated / (double) total) * percentaje);
            callback.onProgress(progress);
            
            SImportProcurementFacility ProcurementFacility = maImportedDocuments.get(i);
            if(ProcurementFacility.ToAccount && ProcurementFacility.isAccountedFor){
                ProcurementFacility.setToAccount(false);
                ProcurementFacility.setIsAccountedFor(false);
                ProcurementFacility.setMoRecord(null);
                
                SDataFacilityRec oDataFacilityRec = new SDataFacilityRec();
                oDataFacilityRec.findByExtDataId(ProcurementFacility.FacilitySeasonWeekId, ProcurementFacility.accountingTypeId, statement);
                
                if (oDataFacilityRec.getMnExtDataId() == 0) {
                    continue;
                }
                
                SDataRecord record = new SDataRecord();
                record.read(new Object[] { 
                    oDataFacilityRec.getMnFkRecYear(),
                    oDataFacilityRec.getMnFkRecPer(),
                    oDataFacilityRec.getMnFkRecBkc(),
                    oDataFacilityRec.getMsFkRecTpRec(),
                    oDataFacilityRec.getMnFkRecNum()
                }, statement);
                
                try {
                    gainRecordLock(record);
                } catch (Exception ex) {
                    Logger.getLogger(SDialogImportProcurementFacility.class.getName()).log(Level.SEVERE, null, ex);
                    continue;
                }
                
                if (!SDataUtilities.isPeriodOpen((SClientInterface) miClient, record.getDate())) {
                    msErrorMessageToAccount = SLibConstants.MSG_ERR_GUI_PER_CLOSE + " (" + record.getDate().toString() + ")";
                    continue;
                }

                Vector<SDataRecordEntry> entryes = record.getDbmsRecordEntries();

                for (int j = 0; j < entryes.size(); j++) {
                    SDataRecordEntry entry = entryes.get(j);
                    if (entry.getUserId() == oDataFacilityRec.getMnUserId()) {
                        entry.setIsDeleted(true);
                        entry.save(con);
                    }
                }

                oDataFacilityRec.delete(con);
                
                try {
                    processPostToggleAccounting(ProcurementFacility.FacilitySeasonWeekId, ProcurementFacility.accountingTypeId, false);
                } catch (Exception ex) {
                    Logger.getLogger(SDialogImportProcurementFacility.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        try {
            releaseAllRecordLocks();
        } catch (Exception ex) {
            Logger.getLogger(SDialogImportProcurementFacility.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        callback.onProgress(initPercentaje + percentaje);
        
        result = true;
        return result;
    }
    
    /**
     * Metodo principal para descontabilizar las semanas
     */
    private void actionReverseAccounting() {
        try {
            jbReverseAccounting.setEnabled(false);
            initProgress();

            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {

                @Override
                protected Void doInBackground() throws Exception {
                    reverseAccounting(progress -> {
                            publish(progress);
                        }, 100, 0);
                    
                    return null;
                }

                @Override
                protected void process(List<Integer> chunks) {
                    int latest = chunks.get(chunks.size() - 1);
                    jProgressBar.setValue(latest);
                }

                @Override
                protected void done() {
                    clearProgress();
                    try {
                        // Ejecutar en EDT después de que el worker termine
                        jbReverseAccounting.setEnabled(true);
                        SwingUtilities.invokeLater(() -> {
                            try {
//                                moImportationsGrid.renderGridRows();
                                actionShowWeeks();
                            } catch (Exception e) {
                                handleShowException(e);
                            }
                        });
                    } catch (Exception e) {
                        handleShowException(e);
                    }
                }
            };

            worker.execute();
            
            worker.addPropertyChangeListener(evt -> {
                if ("state".equals(evt.getPropertyName()) && 
                    SwingWorker.StateValue.DONE == evt.getNewValue()) {
                    // El worker ya terminó, la UI se actualizará en done()
                }
            });
            
            if (!msErrorMessageToAccount.isEmpty()) {
                miClient.showMsgBoxWarning(msErrorMessageToAccount);
            }
            msErrorMessageToAccount = "";
        } catch (Exception e) {
            jbReverseAccounting.setEnabled(true);
            handleShowException(e);
        }
    }
    
    /**
     * Metodo principal para contabilizar las semanas seleccionadas
     */
    private void actionToAccount() {
        try {
            jbToAccount.setEnabled(false);
            arrfacilities = new ArrayList<>();
            mbCanToAccount = true;
            msErrorMessageToAccount = "";
            int countFacility = 0;
            
            Vector<SDataRecordEntry> entryes = moCurrentRecord.getDbmsRecordEntries();
            int lastSortingPosition = 0;
            for (SDataRecordEntry entry : entryes) {
                if (entry.getUserId() > lastSortingPosition) {
                    lastSortingPosition = entry.getUserId();
                }
            }
        
            for (int i = 0; i < maImportedDocuments.size(); i++) {
                SImportProcurementFacility ProcurementFacility = maImportedDocuments.get(i);
                countFacility += 1;
                if(ProcurementFacility.ToAccount){
                    
                    int status = ProcurementFacility.getStatusId();
                    int statusApproval = ProcurementFacility.getStatusApproval();
                    int statuPartialAccount = ProcurementFacility.getSTATUS_PARTIAL_ACCOUNT();

                    if (statusApproval != status && statuPartialAccount != statuPartialAccount) {
                        miClient.showMsgBoxWarning("La semana " 
                                        + ProcurementFacility.getWeekMonthNumber() 
                                        + " de " 
                                        + ProcurementFacility.getProcurementFacilityName() 
                                        +  " no ha sido aprovada, no se puede contabilizar."
                        );
                        return;
                    }
                    
                    if (!ProcurementFacility.isAccountedFor) {
                        lastSortingPosition += 1;
                        ProcurementFacility.setMnSortingPosition(lastSortingPosition);
                        arrfacilities.add(ProcurementFacility);
                    } else {
                        mbCanToAccount = false;
                        if (countFacility <= 10) {
                            msErrorMessageToAccount += "La Semana: " 
                                    + ProcurementFacility.WeekMonthNumber 
                                    + " de la bodega: " + ProcurementFacility.ProcurementFacilityName
                                    + " ya ha sido contabilizada." + "\n";
                        }
                    }
                }
            }
            
            if (arrfacilities.size() < 1) {
                miClient.showMsgBoxWarning("Seleccione al menos una semana para contabilizar.");
                return;
            }
            
            if (!mbCanToAccount) {
                miClient.showMsgBoxWarning(msErrorMessageToAccount);
                return;
            }
            
            initProgress();

            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {

                @Override
                protected Void doInBackground() throws Exception {
                    processGetMovements(progress -> {
                        publish(progress);
                    }, 50, 0);
                    
                    processWeeksToAccount(progress -> {
                        publish(progress);
                    }, 100, 50);
                    
                    return null;
                }

                @Override
                protected void process(List<Integer> chunks) {
                    int latest = chunks.get(chunks.size() - 1);
                    jProgressBar.setValue(latest);
                }

                @Override
                protected void done() {
                    clearProgress();
                    try {
                        // Ejecutar en EDT después de que el worker termine
                        SwingUtilities.invokeLater(() -> {
                            jbToAccount.setEnabled(true);
                            try {
//                                moImportationsGrid.renderGridRows();
                                actionShowWeeks();
                            } catch (Exception e) {
                                handleShowException(e);
                            }
                        });
                    } catch (Exception e) {
                        handleShowException(e);
                    }
                }
            };
            
            worker.addPropertyChangeListener(evt -> {
                if ("state".equals(evt.getPropertyName()) && 
                    SwingWorker.StateValue.DONE == evt.getNewValue()) {
                    // El worker ya terminó, la UI se actualizará en done()
                }
            });


            worker.execute();

        } catch (Exception e) {
            handleShowException(e);
            jbToAccount.setEnabled(true);
        }
    }
    
    private void gainRecordLock(SDataRecord record) throws Exception {
        SLock slock = moRecordSLocksMap.get(record.getRecordPrimaryKey());
        if (slock == null) {
            slock = SLockUtils.gainLock((SClientInterface) miClient, SDataConstants.FIN_REC, record.getPrimaryKey(), record.getRegistryTimeout());
            if (slock != null) {
                moRecordSLocksMap.put(record.getRecordPrimaryKey(), slock);
            }
        }
    }
    
    private void releaseRecordLock(SDataRecord record) throws Exception {
        if (record != null) {
            SLock slock = moRecordSLocksMap.get(record.getRecordPrimaryKey());            
            if (slock != null) {
                SLockUtils.releaseLock((SClientInterface) miClient, slock);
                moRecordSLocksMap.remove(record.getRecordPrimaryKey());
            }
        }
    }
    
    private void releaseAllRecordLocks() throws Exception {
        String exception = "";
        
        ArrayList<SLock> slocks = new ArrayList<>(moRecordSLocksMap.values());
        for (int index = 0; index < slocks.size(); index++) {
            try {
                SLockUtils.releaseLock((SClientInterface) miClient, slocks.get(index));
            }
            catch (Exception e) {
                exception += (exception.isEmpty() ? "" : "\n") + e;
            }
        }
        
        if (!exception.isEmpty()) {
            throw new Exception(exception);
        }
    }
    
    /**
     * Metodo para rechazar una semana en el portal AME
     */
    private void actionRejectWeek() {
        SServicesUtils.RejectData rejectData = SServicesUtils.askForRejectData(miClient.getSession());
        String confirm;
        if (rejectData != null) {
            SGridRow row = moImportationsGrid.getModel().getGridRows().get(moImportationsGrid.getTable().getSelectedRow());
            SImportProcurementFacility oProcurement = ((SImportProcurementFacility) row);
            confirm = "Se rechazará la semana " + oProcurement.WeekMonthNumber + " \n"
                    + "de la bodega: " + oProcurement.ProcurementFacilityName + ",\n"
                    + "con los siguientes comentarios:\n"
                    + "\"" + rejectData.Notes + "\"\n"
                    + SGuiConsts.MSG_CNF_CONT;
            
            if (miClient.showMsgBoxConfirm(confirm) == JOptionPane.YES_OPTION) {
                //enviar a rechazar y recargar tabla
            }
        }
    }
    
    /**
     * Metodo para generar las contrapartidas de la póliza
     *
     * @param oBatchWeekMovProcurementFacility 
     * @param maWeekMovProcurementFacility 
     */
    private ArrayList<SImportWeekMovProcurementFacility> genereateCounterpart(
            SImportedBatchWeekMovProcurementFacility oBatchWeekMovProcurementFacility, 
            ArrayList<SImportWeekMovProcurementFacility> maWeekMovProcurementFacility 
    ) {
        double caja_central = 0;
        double salida_caja_compras = 0;
        double salida_caja_gastos = 0;
        double salida_caja_deudores = 0;
        double salida_caja_acreedores = 0;
        double entrada_caja_deudores = 0;
        double entrada_caja_acreedores = 0;
        double caja_x = 0;
        String concept = "";
        ArrayList<SImportWeekMovProcurementFacility> maCounterpart = new ArrayList<>();
        int sortingPosition = 0;
        
        for (SImportWeekMovProcurementFacility otWeekProcurementFacility : maWeekMovProcurementFacility) {
            ArrayList<String> checkAccountTypeResult = otWeekProcurementFacility.checkAccountType();
            sortingPosition = otWeekProcurementFacility.mnSortingPosition;
            switch(checkAccountTypeResult.get(1)){
                case "salida_caja_compras":
                    if ("debe".equals(checkAccountTypeResult.get(0))) {
                        salida_caja_compras += otWeekProcurementFacility.Debe;
                    }
                    if ("haber".equals(checkAccountTypeResult.get(0))) {
                        salida_caja_compras += otWeekProcurementFacility.Haber;
                    }
                    break;
                case "salida_caja_gastos":
                    if ("debe".equals(checkAccountTypeResult.get(0))) {
                        salida_caja_gastos += otWeekProcurementFacility.Debe;
                    }
                    if ("haber".equals(checkAccountTypeResult.get(0))) {
                        salida_caja_gastos += otWeekProcurementFacility.Haber;
                    }
                    break;
                case "salida_caja_deudores":
                    if ("debe".equals(checkAccountTypeResult.get(0))) {
                        salida_caja_deudores += otWeekProcurementFacility.Debe;
                    }
                    if ("haber".equals(checkAccountTypeResult.get(0))) {
                        salida_caja_deudores += otWeekProcurementFacility.Haber;
                    }
                    break;
                case "salida_caja_acreedores":
                    if ("debe".equals(checkAccountTypeResult.get(0))) {
                        salida_caja_acreedores += otWeekProcurementFacility.Debe;
                    }
                    if ("haber".equals(checkAccountTypeResult.get(0))) {
                        salida_caja_acreedores += otWeekProcurementFacility.Haber;
                    }
                    break;
                case "entrada_caja_deudores":
                    if ("debe".equals(checkAccountTypeResult.get(0))) {
                        entrada_caja_deudores += otWeekProcurementFacility.Debe;
                    }
                    if ("haber".equals(checkAccountTypeResult.get(0))) {
                        entrada_caja_deudores += otWeekProcurementFacility.Haber;
                    }
                    break;
                case "entrada_caja_acreedor":
                    if ("debe".equals(checkAccountTypeResult.get(0))) {
                        entrada_caja_acreedores += otWeekProcurementFacility.Debe;
                    }
                    if ("haber".equals(checkAccountTypeResult.get(0))) {
                        entrada_caja_acreedores += otWeekProcurementFacility.Haber;
                    }
                    break;
                case "caja_central":
                    if ("debe".equals(checkAccountTypeResult.get(0))) {
                        caja_central += otWeekProcurementFacility.Debe;
                    }
                    if ("haber".equals(checkAccountTypeResult.get(0))) {
                        caja_central += otWeekProcurementFacility.Haber;
                    }
                    break;
                case "caja_x":
                    if ("debe".equals(checkAccountTypeResult.get(0))) {
                        caja_x += otWeekProcurementFacility.Debe;
                    }
                    if ("haber".equals(checkAccountTypeResult.get(0))) {
                        caja_x += otWeekProcurementFacility.Haber;
                    }
                    break;
                default:
                    break;
            }
        }
        
        SImportWeekMovProcurementFacility mov = getAccountByAccCash(oBatchWeekMovProcurementFacility.getMnCashHoldingIdCob(), oBatchWeekMovProcurementFacility.getMnCashHoldingIdEnt());
        
        if (caja_central != 0) {
            SImportWeekMovProcurementFacility counterpart = new SImportWeekMovProcurementFacility();
        
            counterpart.setMnSortingPosition(sortingPosition);
            counterpart.setDataAccount(mov.getDataAccount());
            counterpart.setDataAccountMajor(mov.getDataAccountMajor());
            counterpart.setCurrency(mov.getCurrency().Id, mov.getCurrency().Code, mov.getCurrency().Name);
            counterpart.setMovement_date(new Date());
            counterpart.setDataAccountCash(mov.getDataAccountCash());
            
            concept = "CAJA CENTRAL SEMANA " + oBatchWeekMovProcurementFacility.getMnWeekNumebr();
            counterpart.setConcept(concept);
            counterpart.setHaber(caja_central);
            
            maCounterpart.add(counterpart);
        }
        if (salida_caja_compras != 0) {
            SImportWeekMovProcurementFacility counterpart = new SImportWeekMovProcurementFacility();
        
            counterpart.setMnSortingPosition(sortingPosition);
            counterpart.setDataAccount(mov.getDataAccount());
            counterpart.setDataAccountMajor(mov.getDataAccountMajor());
            counterpart.setCurrency(mov.getCurrency().Id, mov.getCurrency().Code, mov.getCurrency().Name);
            counterpart.setMovement_date(new Date());
            counterpart.setDataAccountCash(mov.getDataAccountCash());
            
            concept = "SALIDA CAJA " + oBatchWeekMovProcurementFacility.getMsProcurementName() + " X COMPRAS SEMANA " + oBatchWeekMovProcurementFacility.getMnWeekNumebr();
            counterpart.setConcept(concept);
            counterpart.setHaber(salida_caja_compras);
            
            maCounterpart.add(counterpart);
        }
        if (salida_caja_gastos != 0) {
            SImportWeekMovProcurementFacility counterpart = new SImportWeekMovProcurementFacility();
        
            counterpart.setMnSortingPosition(sortingPosition);
            counterpart.setDataAccount(mov.getDataAccount());
            counterpart.setDataAccountMajor(mov.getDataAccountMajor());
            counterpart.setCurrency(mov.getCurrency().Id, mov.getCurrency().Code, mov.getCurrency().Name);
            counterpart.setMovement_date(new Date());
            counterpart.setDataAccountCash(mov.getDataAccountCash());
            
            concept = "SALIDA CAJA " + oBatchWeekMovProcurementFacility.getMsProcurementName() + " X GASTOS SEMANA " + oBatchWeekMovProcurementFacility.getMnWeekNumebr();
            counterpart.setConcept(concept);
            counterpart.setHaber(salida_caja_gastos);
            
            maCounterpart.add(counterpart);
        }
        if (salida_caja_deudores != 0) {
            SImportWeekMovProcurementFacility counterpart = new SImportWeekMovProcurementFacility();
        
            counterpart.setMnSortingPosition(sortingPosition);
            counterpart.setDataAccount(mov.getDataAccount());
            counterpart.setDataAccountMajor(mov.getDataAccountMajor());
            counterpart.setCurrency(mov.getCurrency().Id, mov.getCurrency().Code, mov.getCurrency().Name);
            counterpart.setMovement_date(new Date());
            counterpart.setDataAccountCash(mov.getDataAccountCash());
            
            concept = "SALIDA CAJA" + oBatchWeekMovProcurementFacility.getMsProcurementName() + " X DEUDORES SEMANA " + oBatchWeekMovProcurementFacility.getMnWeekNumebr();
            counterpart.setConcept(concept);
            counterpart.setHaber(salida_caja_deudores);
            
            maCounterpart.add(counterpart);
        }
        if (salida_caja_acreedores != 0) {
            SImportWeekMovProcurementFacility counterpart = new SImportWeekMovProcurementFacility();
        
            counterpart.setMnSortingPosition(sortingPosition);
            counterpart.setDataAccount(mov.getDataAccount());
            counterpart.setDataAccountMajor(mov.getDataAccountMajor());
            counterpart.setCurrency(mov.getCurrency().Id, mov.getCurrency().Code, mov.getCurrency().Name);
            counterpart.setMovement_date(new Date());
            counterpart.setDataAccountCash(mov.getDataAccountCash());
            
            concept = "SALIDA CAJA " + oBatchWeekMovProcurementFacility.getMsProcurementName() + " X ACREEDORES SEMANA " + oBatchWeekMovProcurementFacility.getMnWeekNumebr();
            counterpart.setConcept(concept);
            counterpart.setHaber(salida_caja_acreedores);
            
            maCounterpart.add(counterpart);
        }
        if (entrada_caja_deudores != 0) {
            SImportWeekMovProcurementFacility counterpart = new SImportWeekMovProcurementFacility();
        
            counterpart.setMnSortingPosition(sortingPosition);
            counterpart.setDataAccount(mov.getDataAccount());
            counterpart.setDataAccountMajor(mov.getDataAccountMajor());
            counterpart.setCurrency(mov.getCurrency().Id, mov.getCurrency().Code, mov.getCurrency().Name);
            counterpart.setMovement_date(new Date());
            counterpart.setDataAccountCash(mov.getDataAccountCash());
            
            concept = "ENTRADA CAJA " + oBatchWeekMovProcurementFacility.getMsProcurementName() + " X DEUDORES SEMANA " + oBatchWeekMovProcurementFacility.getMnWeekNumebr();
            counterpart.setConcept(concept);
            counterpart.setDebe(entrada_caja_deudores);
            
            maCounterpart.add(counterpart);
        }
        if (entrada_caja_acreedores != 0) {
            SImportWeekMovProcurementFacility counterpart = new SImportWeekMovProcurementFacility();
        
            counterpart.setMnSortingPosition(sortingPosition);
            counterpart.setDataAccount(mov.getDataAccount());
            counterpart.setDataAccountMajor(mov.getDataAccountMajor());
            counterpart.setCurrency(mov.getCurrency().Id, mov.getCurrency().Code, mov.getCurrency().Name);
            counterpart.setMovement_date(new Date());
            counterpart.setDataAccountCash(mov.getDataAccountCash());
            
            concept = "ENTRADA CAJA " + oBatchWeekMovProcurementFacility.getMsProcurementName() + " X ACREEDORES SEMANA " + oBatchWeekMovProcurementFacility.getMnWeekNumebr();
            counterpart.setConcept(concept);
            counterpart.setDebe(entrada_caja_acreedores);
            
            maCounterpart.add(counterpart);
        }
        if (caja_x != 0) {
            SImportWeekMovProcurementFacility counterpart = new SImportWeekMovProcurementFacility();
        
            counterpart.setMnSortingPosition(sortingPosition);
            counterpart.setDataAccount(mov.getDataAccount());
            counterpart.setDataAccountMajor(mov.getDataAccountMajor());
            counterpart.setCurrency(mov.getCurrency().Id, mov.getCurrency().Code, mov.getCurrency().Name);
            counterpart.setMovement_date(new Date());
            counterpart.setDataAccountCash(mov.getDataAccountCash());
            
            concept = "CAJA X SEMANA " + oBatchWeekMovProcurementFacility.getMnWeekNumebr();
            counterpart.setConcept(concept);
            counterpart.setDebe(caja_x);
            
            maCounterpart.add(counterpart);
        }
        
        return maCounterpart;
    }
    
    /**
     * Metodo para obtener la cuenta contable mediante la cuenta de caja
     * @param id_cob 
     * @param id_ent 
     * @return  
     */
    public SImportWeekMovProcurementFacility getAccountByAccCash(int id_cob, int id_ent) {
        try {
            String sql;
            SClientInterface client = (SClientInterface) miClient;
            Statement statement = miClient.getSession().getStatement();
            SDataAccount oAccount = new SDataAccount();
            SDataAccount oAccountLedger = new SDataAccount();
            
            SImportWeekMovProcurementFacility weekMov = new SImportWeekMovProcurementFacility();
            
            sql = "SELECT ac.id_cob, ac.id_acc_cash, e.ent, e.code, e.b_act, ac.b_del, ac.fid_acc, f_acc_usr(" + ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskAccount() + ", a.code) \n" +
                    "AS f_acc, cob.id_bpb, cob.bpb, a.acc, c.cur_key, c.id_cur, c.cur\n" +
                    "FROM fin_acc_cash AS ac \n" +
                    "INNER JOIN erp.cfgu_cob_ent AS e ON ac.id_cob = e.id_cob AND ac.id_acc_cash = e.id_ent \n" +
                    "INNER JOIN erp.bpsu_bpb AS cob ON ac.id_cob = cob.id_bpb \n" +
                    "INNER JOIN fin_acc AS a ON ac.fid_acc = a.id_acc \n" +
                    "INNER JOIN erp.cfgu_cur AS c ON ac.fid_cur = c.id_cur \n" +
                    "WHERE cob.fid_bp = " + client.getSessionXXX().getCurrentCompany().getPkCompanyId() +
                    " AND ac.fid_ct_acc_cash = " + SDataConstantsSys.FINS_CT_ACC_CASH_CASH + " AND ac.b_del = FALSE" +
                    " AND ac.id_acc_cash = " + id_ent +
                    " AND cob.id_bpb = " + id_cob + ";";
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            
            weekMov.setCurrency(resultSet.getInt("id_cur"), resultSet.getString("cur_key"), resultSet.getString("cur"));
            
            oAccount.read(new String[] {resultSet.getString("fid_acc")}, statement);
            oAccount.getDbmsPkLedgerAccountIdXXX();
            oAccountLedger.read( new String[] { oAccount.getDbmsPkLedgerAccountIdXXX() }, statement);
            
            weekMov.setDataAccount(oAccount);
            weekMov.setDataAccountMajor(oAccountLedger);

            int[] pkAccountCash = new int[] { id_cob, id_ent };
            SDataAccountCash moDataAccountCash = (SDataAccountCash) SDataUtilities.readRegistry(client, SDataConstants.FIN_ACC_CASH, pkAccountCash, SLibConstants.EXEC_MODE_SILENT);
            
            weekMov.setDataAccountCash(moDataAccountCash);
            
            return weekMov;
        } catch (SQLException ex) {
            Logger.getLogger(SDialogImportProcurementFacility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SDialogImportProcurementFacility.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Metodo para abrir la póliza de la semana
     */
    public void openRecord() {
        SGridRow row = moImportationsGrid.getSelectedGridRow();
        
        if (row != null) {
            SImportProcurementFacility oProcurement = ((SImportProcurementFacility) row);
            
            if (oProcurement.moRecord != null) {
                SClientInterface client = (SClientInterface) miClient;
                SFormRecord formRecord = new SFormRecord(client);
                formRecord.setValue(SLibConstants.VALUE_READ_ONLY, true);
                formRecord.formRefreshCatalogues();
                formRecord.formReset();
                formRecord.setRegistry(SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.FIN_REC, oProcurement.moRecord.getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE));
                formRecord.setFormVisible(true);
            }
        }
    }
}
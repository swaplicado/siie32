/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormInterface;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePaneGrid;
import erp.lib.table.STableRow;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataCfdRecordRow;
import erp.mfin.data.SDataCheck;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.data.SDataRecordEntryRow;
import erp.mfin.data.SDataRecordType;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SFinConsts;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiUtils;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author  Sergio Flores, Isabel Servín
 */
public class SFormRecord extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.ItemListener, javax.swing.event.ListSelectionListener {

    private final int CONCEPT_POLICY_BLANK = 1;
    private final int CONCEPT_POLICY_RECORD = 2;
    private final int CONCEPT_POLICY_CURR_ENTRY = 3;
    private final int CONCEPT_POLICY_LAST_ENTRY = 4;
    
    private final int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private final erp.client.SClientInterface miClient;

    private erp.mfin.data.SDataRecord moRecord;
    private erp.mfin.data.SDataRecordType moRecordType;
    private erp.mfin.data.SDataAccountCash moAccountCash;
    private erp.lib.form.SFormField moFieldDate;
    private erp.lib.form.SFormField moFieldConcept;
    private erp.lib.form.SFormField moFieldIsAdjustmentYearEnd;
    private erp.lib.form.SFormField moFieldIsAdjustmentAudit;
    private erp.lib.form.SFormField moFieldFkAccountCashId_n;
    private erp.lib.form.SFormField moFieldIsDeleted;
    private erp.mfin.form.SFormRecordEntry moFormEntry;
    private erp.mfin.form.SDialogRecordPayment moDialogPaymentSup;
    private erp.mfin.form.SDialogRecordPayment moDialogPaymentCus;
    private erp.mfin.form.SDialogRecordPaymentComplete moDialogPaymentCompleteSup;
    private erp.mfin.form.SDialogRecordPaymentComplete moDialogPaymentCompleteCus;
    private erp.mfin.form.SDialogRecordPaymentMultiple moDialogPaymentMultipleSup;
    private erp.mfin.form.SDialogRecordPaymentMultiple moDialogPaymentMultipleCus;
    private erp.mfin.form.SDialogRecordApportionment moDialogApportioment;
    private erp.mfin.form.SFormMoneyInOut moFormMoneyInOut;
    private erp.mfin.form.SFormMoneyInOutBizPartner moFormMoneyInOutBizPartner;
    private erp.mfin.form.SFormMoneyOutCheck moFormMoneyOutCheck;
    private erp.mfin.form.SFormMoneyOutTransfer moFormMoneyOutTransfer;
    private erp.mfin.form.SFormExchangeRateDiff moFormExchangeRateDiff;
    private erp.lib.table.STablePaneGrid moPaneGridEntries;

    private boolean mbParamReadOnly;
    private boolean mbNonEditable;
    private boolean mbOldIsDeleted;
    private java.lang.String msAuxLastEntryConcept;
    
    private ArrayList<SDataCfdRecordRow> maCfdRecordRows = null;
    private boolean mbIsXmlTranfer = false;

    /**
     * Creates new form SFormRecord
     * @param client GUI client.
     */
    public SFormRecord(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.FIN_REC;
        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgModeInput = new javax.swing.ButtonGroup();
        jpRegistry = new javax.swing.JPanel();
        jpRecord = new javax.swing.JPanel();
        jpRecord1 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        jftDate = new javax.swing.JFormattedTextField();
        jbDate = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jlPeriod = new javax.swing.JLabel();
        jtfPeriod = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jlCompanyBranch = new javax.swing.JLabel();
        jtfCompanyBranch = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jlPkNumberId = new javax.swing.JLabel();
        jtfPkNumberId = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jlConcept = new javax.swing.JLabel();
        jtfConcept = new javax.swing.JTextField();
        jckIsRecordConceptCopyEnabled = new javax.swing.JCheckBox();
        jlXmlFiles = new javax.swing.JLabel();
        jtfXmlFilesNumber = new javax.swing.JTextField();
        jbAddXml = new javax.swing.JButton();
        jbRecordToRecordEntryXml = new javax.swing.JButton();
        jbGetXml = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jlFkAccountCashId_n = new javax.swing.JLabel();
        jcbFkAccountCashId_n = new javax.swing.JComboBox<>();
        jtfAccountCashCurrencyKey = new javax.swing.JTextField();
        jbAccountCashEdit = new javax.swing.JButton();
        jpRecord2 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jlDebit = new javax.swing.JLabel();
        jtfDebit = new javax.swing.JTextField();
        jtfCurrencyKeyDebit = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jlCredit = new javax.swing.JLabel();
        jtfCredit = new javax.swing.JTextField();
        jtfCurrencyKeyCredit = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jlBalance = new javax.swing.JLabel();
        jtfBalance = new javax.swing.JTextField();
        jtfCurrencyKeyBalance = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jckIsAudited = new javax.swing.JCheckBox();
        jckIsAdjustmentYearEnd = new javax.swing.JCheckBox();
        jPanel19 = new javax.swing.JPanel();
        jckIsAuthorized = new javax.swing.JCheckBox();
        jckIsAdjustmentAudit = new javax.swing.JCheckBox();
        jPanel20 = new javax.swing.JPanel();
        jckIsSystem = new javax.swing.JCheckBox();
        jckIsDeleted = new javax.swing.JCheckBox();
        jpEntries = new javax.swing.JPanel();
        jpCommands = new javax.swing.JPanel();
        jpCommandsEntries = new javax.swing.JPanel();
        jpCommands11 = new javax.swing.JPanel();
        jbEntryNew = new javax.swing.JButton();
        jbEntryNewInsert = new javax.swing.JButton();
        jbEntryNewCopy = new javax.swing.JButton();
        jbEntryEdit = new javax.swing.JButton();
        jbEntryDelete = new javax.swing.JButton();
        jsEntry01 = new javax.swing.JSeparator();
        jbEntryMoveDown = new javax.swing.JButton();
        jbEntryMoveUp = new javax.swing.JButton();
        jsEntry2 = new javax.swing.JSeparator();
        jbEntryViewSum = new javax.swing.JButton();
        jsEntry4 = new javax.swing.JSeparator();
        jbEntryGetXml = new javax.swing.JButton();
        jbEntryShowDirectCfd = new javax.swing.JButton();
        jbEntryShowIndirectCfd = new javax.swing.JButton();
        jsEntry3 = new javax.swing.JSeparator();
        jtbEntryDeletedFilter = new javax.swing.JToggleButton();
        jsEntry5 = new javax.swing.JSeparator();
        jbImportFile = new javax.swing.JButton();
        jbDownFile = new javax.swing.JButton();
        jsEntry6 = new javax.swing.JSeparator();
        jbApportionment = new javax.swing.JButton();
        jpCommands12 = new javax.swing.JPanel();
        jpCommands13 = new javax.swing.JPanel();
        jlGuiModeInput = new javax.swing.JLabel();
        jrbModeInputSimple = new javax.swing.JRadioButton();
        jrbModeInputMultiple = new javax.swing.JRadioButton();
        jlGuiModeConcept = new javax.swing.JLabel();
        jcbGuiModeConcept = new javax.swing.JComboBox<>();
        jpCommandsCashAccount = new javax.swing.JPanel();
        jpCommandsCashAccount1 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jbMoneyIn = new javax.swing.JButton();
        jbMoneyInOther = new javax.swing.JButton();
        jbMoneyInPaymentCus = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jbMiPaymentCus = new javax.swing.JButton();
        jbMiAdvanceCus = new javax.swing.JButton();
        jbMiAdvanceSupDev = new javax.swing.JButton();
        jbMiCreditCdr = new javax.swing.JButton();
        jbMiCreditDbr = new javax.swing.JButton();
        jbMiExchangeRateDiff = new javax.swing.JButton();
        jpCommandsCashAccount2 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jbMoneyOut = new javax.swing.JButton();
        jbMoneyOutOther = new javax.swing.JButton();
        jbMoneyOutPaymentSup = new javax.swing.JButton();
        jbMoneyOutCheck = new javax.swing.JButton();
        jbMoneyOutTransfer = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        jbMoPaymentSup = new javax.swing.JButton();
        jbMoAdvanceSup = new javax.swing.JButton();
        jbMoAdvanceCusDev = new javax.swing.JButton();
        jbMoDebitCdr = new javax.swing.JButton();
        jbMoDebitDbr = new javax.swing.JButton();
        jbMoExchangeRateDiff = new javax.swing.JButton();
        jpCommandsJournal = new javax.swing.JPanel();
        jpCommandsJournal1 = new javax.swing.JPanel();
        jpControlCashAccount2 = new javax.swing.JPanel();
        jbDbtPaymentSup = new javax.swing.JButton();
        jbDbtAdvanceSup = new javax.swing.JButton();
        jbDbtAdvanceCusDev = new javax.swing.JButton();
        jbDbtDebitCdr = new javax.swing.JButton();
        jbDbtDebitDbr = new javax.swing.JButton();
        jbDbtExchangeRateDiff = new javax.swing.JButton();
        jpCommandsJournal2 = new javax.swing.JPanel();
        jpControlCashAccount3 = new javax.swing.JPanel();
        jbCdtPaymentCus = new javax.swing.JButton();
        jbCdtAdvanceCus = new javax.swing.JButton();
        jbCdtAdvanceSupDev = new javax.swing.JButton();
        jbCdtCreditCdr = new javax.swing.JButton();
        jbCdtCreditDbr = new javax.swing.JButton();
        jbCdtExchangeRateDiff = new javax.swing.JButton();
        jpControls = new javax.swing.JPanel();
        jpControls1 = new javax.swing.JPanel();
        jckEnableTempFileData = new javax.swing.JCheckBox();
        jpControls2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Póliza contable"); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jpRegistry.setLayout(new java.awt.BorderLayout(5, 5));

        jpRecord.setLayout(new java.awt.BorderLayout());

        jpRecord1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpRecord1.setLayout(new java.awt.GridLayout(6, 1, 0, 2));

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlDate.setText("Fecha póliza: *");
        jlDate.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel11.add(jlDate);

        jftDate.setText("yyyy/mm/dd");
        jftDate.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel11.add(jftDate);

        jbDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDate.setToolTipText("Seleccionar fecha");
        jbDate.setFocusable(false);
        jbDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jbDate);

        jpRecord1.add(jPanel11);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlPeriod.setText("Período contable:");
        jlPeriod.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel6.add(jlPeriod);

        jtfPeriod.setEditable(false);
        jtfPeriod.setText("yyyy-dd");
        jtfPeriod.setFocusable(false);
        jtfPeriod.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel6.add(jtfPeriod);

        jpRecord1.add(jPanel6);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCompanyBranch.setText("Sucursal empresa:");
        jlCompanyBranch.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel14.add(jlCompanyBranch);

        jtfCompanyBranch.setEditable(false);
        jtfCompanyBranch.setText("COMPANY BRANCH");
        jtfCompanyBranch.setFocusable(false);
        jtfCompanyBranch.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel14.add(jtfCompanyBranch);

        jpRecord1.add(jPanel14);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlPkNumberId.setText("Número póliza:");
        jlPkNumberId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel5.add(jlPkNumberId);

        jtfPkNumberId.setEditable(false);
        jtfPkNumberId.setText("0");
        jtfPkNumberId.setFocusable(false);
        jtfPkNumberId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jtfPkNumberId);

        jpRecord1.add(jPanel5);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlConcept.setText("Concepto póliza: *");
        jlConcept.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel7.add(jlConcept);

        jtfConcept.setText("CONCEPT");
        jtfConcept.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel7.add(jtfConcept);

        jckIsRecordConceptCopyEnabled.setText("Copiar");
        jckIsRecordConceptCopyEnabled.setEnabled(false);
        jckIsRecordConceptCopyEnabled.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jckIsRecordConceptCopyEnabled.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jckIsRecordConceptCopyEnabled.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel7.add(jckIsRecordConceptCopyEnabled);

        jlXmlFiles.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlXmlFiles.setText("Archivos XML CFD:");
        jlXmlFiles.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlXmlFiles);

        jtfXmlFilesNumber.setEditable(false);
        jtfXmlFilesNumber.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfXmlFilesNumber.setText("100");
        jtfXmlFilesNumber.setToolTipText("Número de archivos XML de CFD");
        jtfXmlFilesNumber.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel7.add(jtfXmlFilesNumber);

        jbAddXml.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_doc_add.gif"))); // NOI18N
        jbAddXml.setToolTipText("Agregar archivos XML de CFD");
        jbAddXml.setFocusable(false);
        jbAddXml.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel7.add(jbAddXml);

        jbRecordToRecordEntryXml.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_dps_link.gif"))); // NOI18N
        jbRecordToRecordEntryXml.setToolTipText("Mover CFD a una partida");
        jbRecordToRecordEntryXml.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel7.add(jbRecordToRecordEntryXml);

        jbGetXml.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_doc_xml.gif"))); // NOI18N
        jbGetXml.setToolTipText("Obtener CFD");
        jbGetXml.setFocusable(false);
        jbGetXml.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel7.add(jbGetXml);

        jpRecord1.add(jPanel7);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkAccountCashId_n.setText("Cuenta dinero:");
        jlFkAccountCashId_n.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jlFkAccountCashId_n);

        jcbFkAccountCashId_n.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel9.add(jcbFkAccountCashId_n);

        jtfAccountCashCurrencyKey.setEditable(false);
        jtfAccountCashCurrencyKey.setText("CUR");
        jtfAccountCashCurrencyKey.setToolTipText("Moneda");
        jtfAccountCashCurrencyKey.setFocusable(false);
        jtfAccountCashCurrencyKey.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel9.add(jtfAccountCashCurrencyKey);

        jbAccountCashEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbAccountCashEdit.setToolTipText("Modificar cuenta dinero");
        jbAccountCashEdit.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel9.add(jbAccountCashEdit);

        jpRecord1.add(jPanel9);

        jpRecord.add(jpRecord1, java.awt.BorderLayout.CENTER);

        jpRecord2.setBorder(javax.swing.BorderFactory.createTitledBorder("Saldo de la póliza contable:"));
        jpRecord2.setLayout(new java.awt.GridLayout(6, 1, 0, 2));

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlDebit.setText("Cargos:");
        jlDebit.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel15.add(jlDebit);

        jtfDebit.setEditable(false);
        jtfDebit.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDebit.setText("0.00");
        jtfDebit.setFocusable(false);
        jtfDebit.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel15.add(jtfDebit);

        jtfCurrencyKeyDebit.setEditable(false);
        jtfCurrencyKeyDebit.setText("CUR");
        jtfCurrencyKeyDebit.setFocusable(false);
        jtfCurrencyKeyDebit.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel15.add(jtfCurrencyKeyDebit);

        jpRecord2.add(jPanel15);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlCredit.setText("Abonos:");
        jlCredit.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel16.add(jlCredit);

        jtfCredit.setEditable(false);
        jtfCredit.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfCredit.setText("0.00");
        jtfCredit.setFocusable(false);
        jtfCredit.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel16.add(jtfCredit);

        jtfCurrencyKeyCredit.setEditable(false);
        jtfCurrencyKeyCredit.setText("CUR");
        jtfCurrencyKeyCredit.setFocusable(false);
        jtfCurrencyKeyCredit.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel16.add(jtfCurrencyKeyCredit);

        jpRecord2.add(jPanel16);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlBalance.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlBalance.setText("Saldo:");
        jlBalance.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel17.add(jlBalance);

        jtfBalance.setEditable(false);
        jtfBalance.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfBalance.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfBalance.setText("0.00");
        jtfBalance.setFocusable(false);
        jtfBalance.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel17.add(jtfBalance);

        jtfCurrencyKeyBalance.setEditable(false);
        jtfCurrencyKeyBalance.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfCurrencyKeyBalance.setText("CUR");
        jtfCurrencyKeyBalance.setFocusable(false);
        jtfCurrencyKeyBalance.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel17.add(jtfCurrencyKeyBalance);

        jpRecord2.add(jPanel17);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jckIsAudited.setText("Registro auditado");
        jckIsAudited.setEnabled(false);
        jckIsAudited.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel18.add(jckIsAudited);

        jckIsAdjustmentYearEnd.setText("Ajuste de cierre");
        jckIsAdjustmentYearEnd.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel18.add(jckIsAdjustmentYearEnd);

        jpRecord2.add(jPanel18);

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jckIsAuthorized.setText("Registro autorizado");
        jckIsAuthorized.setEnabled(false);
        jckIsAuthorized.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel19.add(jckIsAuthorized);

        jckIsAdjustmentAudit.setText("Ajuste de auditoría");
        jckIsAdjustmentAudit.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel19.add(jckIsAdjustmentAudit);

        jpRecord2.add(jPanel19);

        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jckIsSystem.setText("Registro de sistema");
        jckIsSystem.setEnabled(false);
        jckIsSystem.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel20.add(jckIsSystem);

        jckIsDeleted.setText("Registro eliminado");
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel20.add(jckIsDeleted);

        jpRecord2.add(jPanel20);

        jpRecord.add(jpRecord2, java.awt.BorderLayout.EAST);

        jpRegistry.add(jpRecord, java.awt.BorderLayout.NORTH);

        jpEntries.setBorder(javax.swing.BorderFactory.createTitledBorder("Partidas de la póliza contable:"));
        jpEntries.setLayout(new java.awt.BorderLayout());

        jpCommands.setLayout(new java.awt.BorderLayout());

        jpCommandsEntries.setLayout(new java.awt.BorderLayout());

        jpCommands11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 0));

        jbEntryNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbEntryNew.setToolTipText("Crear partida [Ctrl + N]");
        jbEntryNew.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCommands11.add(jbEntryNew);

        jbEntryNewInsert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_insert.gif"))); // NOI18N
        jbEntryNewInsert.setToolTipText("Insertar partida [Ctrl + Mayúsc + I]");
        jbEntryNewInsert.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCommands11.add(jbEntryNewInsert);

        jbEntryNewCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_copy.gif"))); // NOI18N
        jbEntryNewCopy.setToolTipText("Copiar partida [Ctrl + Mayúsc + C]");
        jbEntryNewCopy.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCommands11.add(jbEntryNewCopy);

        jbEntryEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEntryEdit.setToolTipText("Modificar partida [Ctrl + M]");
        jbEntryEdit.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCommands11.add(jbEntryEdit);

        jbEntryDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbEntryDelete.setToolTipText("Eliminar partida [Ctrl + D]");
        jbEntryDelete.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCommands11.add(jbEntryDelete);

        jsEntry01.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jsEntry01.setPreferredSize(new java.awt.Dimension(3, 23));
        jpCommands11.add(jsEntry01);

        jbEntryMoveDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_move_down.gif"))); // NOI18N
        jbEntryMoveDown.setToolTipText("Mover partida abajo [Ctrl + Mayúsc + D]");
        jbEntryMoveDown.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCommands11.add(jbEntryMoveDown);

        jbEntryMoveUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_move_up.gif"))); // NOI18N
        jbEntryMoveUp.setToolTipText("Mover partida arriba [Ctrl + Mayúsc + U]");
        jbEntryMoveUp.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCommands11.add(jbEntryMoveUp);

        jsEntry2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jsEntry2.setPreferredSize(new java.awt.Dimension(3, 23));
        jpCommands11.add(jsEntry2);

        jbEntryViewSum.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_sum.gif"))); // NOI18N
        jbEntryViewSum.setToolTipText("Ver sumatoria [Ctrl + S]");
        jbEntryViewSum.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCommands11.add(jbEntryViewSum);

        jsEntry4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jsEntry4.setPreferredSize(new java.awt.Dimension(3, 23));
        jpCommands11.add(jsEntry4);

        jbEntryGetXml.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_doc_xml.gif"))); // NOI18N
        jbEntryGetXml.setToolTipText("Obtener CFD de la partida");
        jbEntryGetXml.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCommands11.add(jbEntryGetXml);

        jbEntryShowDirectCfd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_xml_dir.gif"))); // NOI18N
        jbEntryShowDirectCfd.setToolTipText("Ver CFD directos de la partida");
        jbEntryShowDirectCfd.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCommands11.add(jbEntryShowDirectCfd);

        jbEntryShowIndirectCfd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_xml_ind.gif"))); // NOI18N
        jbEntryShowIndirectCfd.setToolTipText("Ver CFD indirectos de la partida");
        jbEntryShowIndirectCfd.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCommands11.add(jbEntryShowIndirectCfd);

        jsEntry3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jsEntry3.setPreferredSize(new java.awt.Dimension(3, 23));
        jpCommands11.add(jsEntry3);

        jtbEntryDeletedFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_filter_off.gif"))); // NOI18N
        jtbEntryDeletedFilter.setToolTipText("Filtrar partidas eliminadas [Ctrl + F]");
        jtbEntryDeletedFilter.setPreferredSize(new java.awt.Dimension(23, 23));
        jtbEntryDeletedFilter.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_filter_on.gif"))); // NOI18N
        jpCommands11.add(jtbEntryDeletedFilter);

        jsEntry5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jsEntry5.setPreferredSize(new java.awt.Dimension(3, 23));
        jpCommands11.add(jsEntry5);

        jbImportFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_doc_add.gif"))); // NOI18N
        jbImportFile.setToolTipText("Importar partidas desde archivo externo");
        jbImportFile.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCommands11.add(jbImportFile);

        jbDownFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_doc_down.gif"))); // NOI18N
        jbDownFile.setToolTipText("Descargar formato importación de partidas desde archivo externo");
        jbDownFile.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCommands11.add(jbDownFile);

        jsEntry6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jsEntry6.setPreferredSize(new java.awt.Dimension(3, 23));
        jpCommands11.add(jsEntry6);

        jbApportionment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_wizard.gif"))); // NOI18N
        jbApportionment.setToolTipText("Captura por prorrateo");
        jbApportionment.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCommands11.add(jbApportionment);

        jpCommandsEntries.add(jpCommands11, java.awt.BorderLayout.WEST);

        jpCommands12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 1, 0));
        jpCommandsEntries.add(jpCommands12, java.awt.BorderLayout.CENTER);

        jpCommands13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 2, 0));

        jlGuiModeInput.setForeground(java.awt.Color.blue);
        jlGuiModeInput.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlGuiModeInput.setText("Modo captura:");
        jlGuiModeInput.setToolTipText("Modo de captura de cobros y pagos");
        jlGuiModeInput.setPreferredSize(new java.awt.Dimension(95, 23));
        jpCommands13.add(jlGuiModeInput);

        bgModeInput.add(jrbModeInputSimple);
        jrbModeInputSimple.setSelected(true);
        jrbModeInputSimple.setText("Simple");
        jrbModeInputSimple.setToolTipText("Un sólo cobro o pago a la vez");
        jrbModeInputSimple.setPreferredSize(new java.awt.Dimension(65, 23));
        jpCommands13.add(jrbModeInputSimple);

        bgModeInput.add(jrbModeInputMultiple);
        jrbModeInputMultiple.setText("Múltiple");
        jrbModeInputMultiple.setToolTipText("Varios cobros o pagos a la vez");
        jrbModeInputMultiple.setPreferredSize(new java.awt.Dimension(65, 23));
        jpCommands13.add(jrbModeInputMultiple);

        jlGuiModeConcept.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlGuiModeConcept.setText("Modo concepto:");
        jlGuiModeConcept.setToolTipText("Modo de concepto para nuevas partidas");
        jlGuiModeConcept.setPreferredSize(new java.awt.Dimension(100, 23));
        jpCommands13.add(jlGuiModeConcept);

        jcbGuiModeConcept.setToolTipText("Modo de concepto para nuevas partidas");
        jcbGuiModeConcept.setPreferredSize(new java.awt.Dimension(150, 23));
        jpCommands13.add(jcbGuiModeConcept);

        jpCommandsEntries.add(jpCommands13, java.awt.BorderLayout.EAST);

        jpCommands.add(jpCommandsEntries, java.awt.BorderLayout.NORTH);

        jpCommandsCashAccount.setLayout(new java.awt.GridLayout(2, 1));

        jpCommandsCashAccount1.setLayout(new java.awt.BorderLayout());

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingresos (cargos):"));
        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jbMoneyIn.setText("Ingreso");
        jbMoneyIn.setToolTipText("Ingreso de dinero");
        jbMoneyIn.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMoneyIn.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel22.add(jbMoneyIn);

        jbMoneyInOther.setText("Ingr. otra cta.");
        jbMoneyInOther.setToolTipText("Ingreso de dinero en otra cuenta");
        jbMoneyInOther.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMoneyInOther.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel22.add(jbMoneyInOther);

        jbMoneyInPaymentCus.setForeground(java.awt.Color.blue);
        jbMoneyInPaymentCus.setText("Ingr.+ cobro cte.");
        jbMoneyInPaymentCus.setToolTipText("Ingreso de dinero + cobro cliente");
        jbMoneyInPaymentCus.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMoneyInPaymentCus.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel22.add(jbMoneyInPaymentCus);

        jLabel2.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel22.add(jLabel2);

        jLabel3.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel22.add(jLabel3);

        jpCommandsCashAccount1.add(jPanel22, java.awt.BorderLayout.CENTER);

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder("Contrapartidas ingresos (abonos):"));
        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jbMiPaymentCus.setForeground(java.awt.Color.blue);
        jbMiPaymentCus.setText("Cobro cte.");
        jbMiPaymentCus.setToolTipText("Cobro cliente");
        jbMiPaymentCus.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMiPaymentCus.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel23.add(jbMiPaymentCus);

        jbMiAdvanceCus.setText("Antic. cte.");
        jbMiAdvanceCus.setToolTipText("Cobro anticipo cliente");
        jbMiAdvanceCus.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMiAdvanceCus.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel23.add(jbMiAdvanceCus);

        jbMiAdvanceSupDev.setText("Dev. antic. prv.");
        jbMiAdvanceSupDev.setToolTipText("Devolución anticipo proveedor");
        jbMiAdvanceSupDev.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMiAdvanceSupDev.setPreferredSize(new java.awt.Dimension(95, 23));
        jPanel23.add(jbMiAdvanceSupDev);

        jbMiCreditCdr.setText("Abo. acr. div.");
        jbMiCreditCdr.setToolTipText("Abono a acreedor diverso");
        jbMiCreditCdr.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMiCreditCdr.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel23.add(jbMiCreditCdr);

        jbMiCreditDbr.setText("Abo. ddr. div.");
        jbMiCreditDbr.setToolTipText("Abono a deudor diverso");
        jbMiCreditDbr.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMiCreditDbr.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel23.add(jbMiCreditDbr);

        jbMiExchangeRateDiff.setText("Util. camb.");
        jbMiExchangeRateDiff.setToolTipText("Utilidad cambiaria");
        jbMiExchangeRateDiff.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMiExchangeRateDiff.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel23.add(jbMiExchangeRateDiff);

        jpCommandsCashAccount1.add(jPanel23, java.awt.BorderLayout.EAST);

        jpCommandsCashAccount.add(jpCommandsCashAccount1);

        jpCommandsCashAccount2.setLayout(new java.awt.BorderLayout());

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder("Egresos (abonos):"));
        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jbMoneyOut.setText("Egreso");
        jbMoneyOut.setToolTipText("Egreso de dinero");
        jbMoneyOut.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMoneyOut.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel25.add(jbMoneyOut);

        jbMoneyOutOther.setText("Egr. otra cta.");
        jbMoneyOutOther.setToolTipText("Egreso de dinero en otra cuenta");
        jbMoneyOutOther.setFocusable(false);
        jbMoneyOutOther.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMoneyOutOther.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel25.add(jbMoneyOutOther);

        jbMoneyOutPaymentSup.setForeground(java.awt.Color.blue);
        jbMoneyOutPaymentSup.setText("Egr.+ pago prv.");
        jbMoneyOutPaymentSup.setToolTipText("Egreso de dinero + pago proveedor");
        jbMoneyOutPaymentSup.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMoneyOutPaymentSup.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel25.add(jbMoneyOutPaymentSup);

        jbMoneyOutCheck.setText("Egr. c/ cheque");
        jbMoneyOutCheck.setToolTipText("Egreso de dinero con cheque");
        jbMoneyOutCheck.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMoneyOutCheck.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel25.add(jbMoneyOutCheck);

        jbMoneyOutTransfer.setText("Egr. c/ traspaso");
        jbMoneyOutTransfer.setToolTipText("Egreso de dinero con traspaso ");
        jbMoneyOutTransfer.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMoneyOutTransfer.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel25.add(jbMoneyOutTransfer);

        jpCommandsCashAccount2.add(jPanel25, java.awt.BorderLayout.CENTER);

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder("Contrapartidas egresos (cargos):"));
        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jbMoPaymentSup.setForeground(java.awt.Color.blue);
        jbMoPaymentSup.setText("Pago prv.");
        jbMoPaymentSup.setToolTipText("Pago proveedor");
        jbMoPaymentSup.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMoPaymentSup.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel26.add(jbMoPaymentSup);

        jbMoAdvanceSup.setText("Antic. prv.");
        jbMoAdvanceSup.setToolTipText("Pago anticipo proveedor");
        jbMoAdvanceSup.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMoAdvanceSup.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel26.add(jbMoAdvanceSup);

        jbMoAdvanceCusDev.setText("Dev. antic. cte.");
        jbMoAdvanceCusDev.setToolTipText("Devolución anticipo cliente");
        jbMoAdvanceCusDev.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMoAdvanceCusDev.setPreferredSize(new java.awt.Dimension(95, 23));
        jPanel26.add(jbMoAdvanceCusDev);

        jbMoDebitCdr.setText("Cgo. acr. div.");
        jbMoDebitCdr.setToolTipText("Cargo a acreedor diverso");
        jbMoDebitCdr.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMoDebitCdr.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel26.add(jbMoDebitCdr);

        jbMoDebitDbr.setText("Cgo. ddr. div.");
        jbMoDebitDbr.setToolTipText("Cargo a deudor diverso");
        jbMoDebitDbr.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMoDebitDbr.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel26.add(jbMoDebitDbr);

        jbMoExchangeRateDiff.setText("Pérd. camb.");
        jbMoExchangeRateDiff.setToolTipText("Pérdida cambiaria");
        jbMoExchangeRateDiff.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbMoExchangeRateDiff.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel26.add(jbMoExchangeRateDiff);

        jpCommandsCashAccount2.add(jPanel26, java.awt.BorderLayout.EAST);

        jpCommandsCashAccount.add(jpCommandsCashAccount2);

        jpCommands.add(jpCommandsCashAccount, java.awt.BorderLayout.CENTER);

        jpCommandsJournal.setLayout(new java.awt.GridLayout(1, 2));

        jpCommandsJournal1.setLayout(new java.awt.BorderLayout());

        jpControlCashAccount2.setBorder(javax.swing.BorderFactory.createTitledBorder("Cargos:"));
        jpControlCashAccount2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jbDbtPaymentSup.setForeground(java.awt.Color.blue);
        jbDbtPaymentSup.setText("Pago prv.");
        jbDbtPaymentSup.setToolTipText("Pago proveedor");
        jbDbtPaymentSup.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbDbtPaymentSup.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControlCashAccount2.add(jbDbtPaymentSup);

        jbDbtAdvanceSup.setText("Antic. prv.");
        jbDbtAdvanceSup.setToolTipText("Pago anticipo proveedor");
        jbDbtAdvanceSup.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbDbtAdvanceSup.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControlCashAccount2.add(jbDbtAdvanceSup);

        jbDbtAdvanceCusDev.setText("Dev. antic. cte.");
        jbDbtAdvanceCusDev.setToolTipText("Devolución anticipo cliente");
        jbDbtAdvanceCusDev.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbDbtAdvanceCusDev.setPreferredSize(new java.awt.Dimension(95, 23));
        jpControlCashAccount2.add(jbDbtAdvanceCusDev);

        jbDbtDebitCdr.setText("Cgo. acr. div.");
        jbDbtDebitCdr.setToolTipText("Cargo a acreedor diverso");
        jbDbtDebitCdr.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbDbtDebitCdr.setPreferredSize(new java.awt.Dimension(80, 23));
        jpControlCashAccount2.add(jbDbtDebitCdr);

        jbDbtDebitDbr.setText("Cgo. ddr. div.");
        jbDbtDebitDbr.setToolTipText("Cargo a deudor diverso");
        jbDbtDebitDbr.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbDbtDebitDbr.setPreferredSize(new java.awt.Dimension(80, 23));
        jpControlCashAccount2.add(jbDbtDebitDbr);

        jbDbtExchangeRateDiff.setText("Pérd. camb.");
        jbDbtExchangeRateDiff.setToolTipText("Pérdida cambiaria");
        jbDbtExchangeRateDiff.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbDbtExchangeRateDiff.setPreferredSize(new java.awt.Dimension(80, 23));
        jpControlCashAccount2.add(jbDbtExchangeRateDiff);

        jpCommandsJournal1.add(jpControlCashAccount2, java.awt.BorderLayout.CENTER);

        jpCommandsJournal.add(jpCommandsJournal1);

        jpCommandsJournal2.setBorder(javax.swing.BorderFactory.createTitledBorder("Abonos:"));
        jpCommandsJournal2.setLayout(new java.awt.BorderLayout());

        jpControlCashAccount3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 2, 0));

        jbCdtPaymentCus.setForeground(java.awt.Color.blue);
        jbCdtPaymentCus.setText("Cobro cte.");
        jbCdtPaymentCus.setToolTipText("Cobro cliente");
        jbCdtPaymentCus.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbCdtPaymentCus.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControlCashAccount3.add(jbCdtPaymentCus);

        jbCdtAdvanceCus.setText("Antic. cte.");
        jbCdtAdvanceCus.setToolTipText("Cobro anticipo cliente");
        jbCdtAdvanceCus.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbCdtAdvanceCus.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControlCashAccount3.add(jbCdtAdvanceCus);

        jbCdtAdvanceSupDev.setText("Dev. antic. prv.");
        jbCdtAdvanceSupDev.setToolTipText("Devolución anticipo proveedor");
        jbCdtAdvanceSupDev.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbCdtAdvanceSupDev.setPreferredSize(new java.awt.Dimension(95, 23));
        jpControlCashAccount3.add(jbCdtAdvanceSupDev);

        jbCdtCreditCdr.setText("Abo. acr. div.");
        jbCdtCreditCdr.setToolTipText("Abono a acreedor diverso");
        jbCdtCreditCdr.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbCdtCreditCdr.setPreferredSize(new java.awt.Dimension(80, 23));
        jpControlCashAccount3.add(jbCdtCreditCdr);

        jbCdtCreditDbr.setText("Abo. ddr. div.");
        jbCdtCreditDbr.setToolTipText("Abono a deudor diverso");
        jbCdtCreditDbr.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbCdtCreditDbr.setPreferredSize(new java.awt.Dimension(80, 23));
        jpControlCashAccount3.add(jbCdtCreditDbr);

        jbCdtExchangeRateDiff.setText("Util. camb.");
        jbCdtExchangeRateDiff.setToolTipText("Utilidad cambiaria");
        jbCdtExchangeRateDiff.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbCdtExchangeRateDiff.setPreferredSize(new java.awt.Dimension(80, 23));
        jpControlCashAccount3.add(jbCdtExchangeRateDiff);

        jpCommandsJournal2.add(jpControlCashAccount3, java.awt.BorderLayout.CENTER);

        jpCommandsJournal.add(jpCommandsJournal2);

        jpCommands.add(jpCommandsJournal, java.awt.BorderLayout.SOUTH);

        jpEntries.add(jpCommands, java.awt.BorderLayout.NORTH);

        jpRegistry.add(jpEntries, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpRegistry, java.awt.BorderLayout.CENTER);

        jpControls.setLayout(new java.awt.BorderLayout());

        jckEnableTempFileData.setText("Habilitar recuperación de datos");
        jckEnableTempFileData.setPreferredSize(new java.awt.Dimension(200, 23));
        jpControls1.add(jckEnableTempFileData);

        jpControls.add(jpControls1, java.awt.BorderLayout.WEST);

        jbOk.setText("Aceptar"); // NOI18N
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls2.add(jbOk);

        jbCancel.setText("Cancelar"); // NOI18N
        jbCancel.setToolTipText("[Escape]");
        jpControls2.add(jbCancel);

        jpControls.add(jpControls2, java.awt.BorderLayout.EAST);

        getContentPane().add(jpControls, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(1056, 689));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        actionPerformedCancel();
    }//GEN-LAST:event_formWindowClosing

    private void initComponentsExtra() {
        mvFields = new Vector<>();

        moFieldDate = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDate, jlDate);
        moFieldDate.setPickerButton(jbDate);
        moFieldConcept = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfConcept, jlConcept);
        moFieldConcept.setLengthMax(100);
        moFieldIsAdjustmentYearEnd = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsAdjustmentYearEnd);
        moFieldIsAdjustmentAudit = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsAdjustmentAudit);
        moFieldFkAccountCashId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkAccountCashId_n, jlFkAccountCashId_n);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsDeleted);
        
        mvFields.add(moFieldDate);
        mvFields.add(moFieldConcept);
        mvFields.add(moFieldFkAccountCashId_n);
        mvFields.add(moFieldIsDeleted);

        moFormEntry = new SFormRecordEntry(miClient);
        moDialogPaymentSup = new SDialogRecordPayment(miClient, SDataConstantsSys.BPSS_CT_BP_SUP);
        moDialogPaymentCus = new SDialogRecordPayment(miClient, SDataConstantsSys.BPSS_CT_BP_CUS);
        moDialogPaymentCompleteSup = new SDialogRecordPaymentComplete(miClient, SDataConstantsSys.BPSS_CT_BP_SUP);
        moDialogPaymentCompleteCus = new SDialogRecordPaymentComplete(miClient, SDataConstantsSys.BPSS_CT_BP_CUS);
        moDialogPaymentMultipleSup = new SDialogRecordPaymentMultiple(miClient, SDataConstantsSys.BPSS_CT_BP_SUP);
        moDialogPaymentMultipleCus = new SDialogRecordPaymentMultiple(miClient, SDataConstantsSys.BPSS_CT_BP_CUS);
        moFormMoneyInOut = new SFormMoneyInOut(miClient);
        moFormMoneyInOutBizPartner = new SFormMoneyInOutBizPartner(miClient);
        moFormMoneyOutCheck = new SFormMoneyOutCheck(miClient);
        moFormMoneyOutTransfer = new SFormMoneyOutTransfer(miClient);
        moFormExchangeRateDiff = new SFormExchangeRateDiff(miClient);
        moPaneGridEntries = new STablePaneGrid(miClient);
        moPaneGridEntries.setDoubleClickAction(this, "publicActionEntryEdit");
        jpEntries.add(moPaneGridEntries, BorderLayout.CENTER);
        jckEnableTempFileData.setSelected(true);

        int i = 0;
        STableColumnForm[] aoTableColumns = new STableColumnForm[35];
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "#", STableConstants.WIDTH_NUM_TINYINT);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "No. cuenta contable", STableConstants.WIDTH_ACCOUNT_ID);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Cuenta contable", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Concepto", 175);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Debe $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Haber $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "T. cambio", STableConstants.WIDTH_EXCHANGE_RATE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererExchangeRate());
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Debe mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Haber mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Sistema", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Diferencia cambiaria", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Subclase movimiento", 200);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "No. centro costo", STableConstants.WIDTH_ACCOUNT_ID);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Centro costo", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Asociado negocios", 175);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "RFC ocasional", 100);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Repositorio contable", 75);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Impuesto", 100);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Entidad", 100);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Ítem", STableConstants.WIDTH_ITEM_2X);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Ítem auxiliar", STableConstants.WIDTH_ITEM);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Documento (factura)", 100);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "CFDI directos", STableConstants.WIDTH_NUM_INTEGER);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "CFDI indirectos", STableConstants.WIDTH_NUM_INTEGER);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Ejercicio contable", STableConstants.WIDTH_YEAR_PERIOD);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "No. cheque", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Eliminación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Id interno", STableConstants.WIDTH_NUM_TINYINT);

        for (i = 0; i < aoTableColumns.length; i++) {
            moPaneGridEntries.addTableColumn(aoTableColumns[i]);
        }

        jcbGuiModeConcept.removeAllItems();
        jcbGuiModeConcept.addItem(new SFormComponentItem(new int[] { CONCEPT_POLICY_BLANK }, "En blanco"));
        jcbGuiModeConcept.addItem(new SFormComponentItem(new int[] { CONCEPT_POLICY_RECORD }, "Copiar concepto póliza"));
        jcbGuiModeConcept.addItem(new SFormComponentItem(new int[] { CONCEPT_POLICY_CURR_ENTRY }, "Copiar concepto actual"));
        jcbGuiModeConcept.addItem(new SFormComponentItem(new int[] { CONCEPT_POLICY_LAST_ENTRY }, "Copiar último concepto"));

        if (miClient.getSessionXXX().getParamsErp().getIsRecordConceptCopyEnabled()) {
            SFormUtilities.locateComboBoxItem(jcbGuiModeConcept, new int[] { CONCEPT_POLICY_RECORD });
        }
        else {
            SFormUtilities.locateComboBoxItem(jcbGuiModeConcept, new int[] { CONCEPT_POLICY_BLANK });
        }

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbDate.addActionListener(this);
        jbAddXml.addActionListener(this);
        jbRecordToRecordEntryXml.addActionListener(this);
        jbGetXml.addActionListener(this);
        jbAccountCashEdit.addActionListener(this);
        jbEntryNew.addActionListener(this);
        jbEntryNewCopy.addActionListener(this);
        jbEntryNewInsert.addActionListener(this);
        jbEntryEdit.addActionListener(this);
        jbEntryDelete.addActionListener(this);
        jbEntryMoveDown.addActionListener(this);
        jbEntryMoveUp.addActionListener(this);
        jbEntryViewSum.addActionListener(this);
        jbEntryGetXml.addActionListener(this); 
        jbEntryShowDirectCfd.addActionListener(this);
        jbEntryShowIndirectCfd.addActionListener(this);
        jtbEntryDeletedFilter.addActionListener(this);
        jbImportFile.addActionListener(this);
        jbDownFile.addActionListener(this);
        jbApportionment.addActionListener(this);
        
        jbMoneyIn.addActionListener(this);
        jbMoneyInOther.addActionListener(this);
        jbMoneyInPaymentCus.addActionListener(this);
        jbMiPaymentCus.addActionListener(this);
        jbMiAdvanceCus.addActionListener(this);
        jbMiAdvanceSupDev.addActionListener(this);
        jbMiCreditCdr.addActionListener(this);
        jbMiCreditDbr.addActionListener(this);
        jbMiExchangeRateDiff.addActionListener(this);
        
        jbMoneyOut.addActionListener(this);
        jbMoneyOutOther.addActionListener(this);
        jbMoneyOutPaymentSup.addActionListener(this);
        jbMoneyOutCheck.addActionListener(this);
        jbMoneyOutTransfer.addActionListener(this);
        jbMoPaymentSup.addActionListener(this);
        jbMoAdvanceSup.addActionListener(this);
        jbMoAdvanceCusDev.addActionListener(this);
        jbMoDebitCdr.addActionListener(this);
        jbMoDebitDbr.addActionListener(this);
        jbMoExchangeRateDiff.addActionListener(this);
        
        jbDbtPaymentSup.addActionListener(this);
        jbDbtAdvanceSup.addActionListener(this);
        jbDbtAdvanceCusDev.addActionListener(this);
        jbDbtDebitCdr.addActionListener(this);
        jbDbtDebitDbr.addActionListener(this);
        jbDbtExchangeRateDiff.addActionListener(this);
        
        jbCdtPaymentCus.addActionListener(this);
        jbCdtAdvanceCus.addActionListener(this);
        jbCdtAdvanceSupDev.addActionListener(this);
        jbCdtCreditCdr.addActionListener(this);
        jbCdtCreditDbr.addActionListener(this);
        jbCdtExchangeRateDiff.addActionListener(this);
        
        jcbFkAccountCashId_n.addItemListener(this);
        jckEnableTempFileData.addItemListener(this);

        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryNew", "entryNew", KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryNewInsert", "entryNewInsert", KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryNewCopy", "entryNewCopy", KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryEdit", "entryEdit", KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryDelete", "entryDelete", KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryMoveDown", "entryMoveDown", KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryMoveUp", "entryMoveUp", KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryViewSum", "entryViewSum", KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionEntryFilter", "entryFilter", KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionPerformedOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionCancel = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionPerformedCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionCancel, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            
            if (jftDate.isEnabled()) {
                jftDate.requestFocus();
            }
            else {
                jbCancel.requestFocus();
            }
        }
    }

    private void updateRecordEntries() {
        moRecord.getDbmsRecordEntries().clear();

        for (STableRow row : moPaneGridEntries.getGridRows()) {
            moRecord.getDbmsRecordEntries().add((SDataRecordEntry) row.getData());
        }
    }

    private void updateRecord() throws Exception {
        if (moRecordType.getIsAccountCashRequired() && jcbFkAccountCashId_n.getSelectedIndex() <= 0) {
            jcbFkAccountCashId_n.requestFocusInWindow();
            throw new Exception(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkAccountCashId_n.getText() + "'.");
        }
        
        moRecord.setDate(moFieldDate.getDate());
        moRecord.setConcept(moFieldConcept.getString());
        moRecord.setIsAdjustmentsYearEnd(moFieldIsAdjustmentYearEnd.getBoolean());
        moRecord.setIsAdjustmentsAudit(moFieldIsAdjustmentAudit.getBoolean());
        moRecord.setIsDeleted(moFieldIsDeleted.getBoolean());
        
        if (moRecordType.getIsAccountCashRequired()) {
            moRecord.setFkCompanyBranchId_n(moFieldFkAccountCashId_n.getKeyAsIntArray()[0]);
            moRecord.setFkAccountCashId_n(moFieldFkAccountCashId_n.getKeyAsIntArray()[1]);
        }
        else {
            moRecord.setFkCompanyBranchId_n(0);
            moRecord.setFkAccountCashId_n(0);
        }
        
        moRecord.setFkUserEditId(miClient.getSession().getUser().getPkUserId());

        moRecord.setDbmsDataAccountCash(moAccountCash);

        updateRecordEntries();
    }
    
    private void deleteTempFile() {
        if (moRecord != null) { // delete temporal data if record was instantiated
            try {
                moRecord.deleteTempFile(miClient);
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
    }
    
    private void saveTempFileData() throws IOException, Exception {
        if (moRecord != null) {
            if (!moRecord.getTempDataJustRecovered() && !mbNonEditable && jckEnableTempFileData.isSelected()) { // save temporal file only if record is not of system
                updateRecord();

                moRecord.saveTempFileData(miClient);
            }
            
            moRecord.setTempDataJustRecovered(false); // allow to preserve subsequent changes to record
        }
    }
    
    private void renderAccountCash() {
        if (jcbFkAccountCashId_n.getSelectedIndex() <= 0) {
            moAccountCash = null;
            jtfAccountCashCurrencyKey.setText("");
        }
        else {
            moAccountCash = (SDataAccountCash) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC_CASH, moFieldFkAccountCashId_n.getKey(), SLibConstants.EXEC_MODE_SILENT);
            jtfAccountCashCurrencyKey.setText(miClient.getSession().getSessionCustom().getCurrencyCode(new int[] { moAccountCash.getFkCurrencyId() }));
        }
    }

    private void renderHeader() {
        // render period:
        
        java.util.Date date = moFieldDate.getDate();

        if (date == null) {
            jtfPeriod.setText("");
        }
        else {
            jtfPeriod.setText(miClient.getSessionXXX().getFormatters().getDateYearMonthFormat().format(date));
        }
        
        // render company branch:
        
        jtfCompanyBranch.setText(moRecord == null ? miClient.getSessionXXX().getCurrentCompanyBranchName() : SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.BPSU_BPB, new int[] { moRecord.getFkCompanyBranchId() }));
        jtfCompanyBranch.setToolTipText(jtfCompanyBranch.getText());
        jtfCompanyBranch.setCaretPosition(0);
        
        // render account cash settings:
        
        renderAccountCash();
    }

    private void renderRecordEntries(boolean renumber) {
        moPaneGridEntries.renderTableRows();
        
        if (renumber) {
            int sortingPosition = 0;
            
            for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
                SDataRecordEntry entry = (SDataRecordEntry) moPaneGridEntries.getTableRow(i).getData();
                int oldSortingPosition = entry.getSortingPosition();

                if (entry.getIsDeleted()) {
                    entry.setSortingPosition(0);
                }
                else {
                    entry.setSortingPosition(++sortingPosition);
                }

                if (entry.getSortingPosition() != oldSortingPosition) {
                    entry.setIsRegistryEdited(true);
                    moPaneGridEntries.getTableRow(i).prepareTableRow();
                }
            }
        }
    }

    private void calculateBalance() throws Exception {
        double debit = 0;
        double credit = 0;

        for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
            SDataRecordEntry entry = (SDataRecordEntry) moPaneGridEntries.getTableRow(i).getData();
            
            debit = SLibUtils.roundAmount(debit + entry.getDebit());
            credit = SLibUtils.roundAmount(credit + entry.getCredit());
        }

        jtfDebit.setText(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(debit));
        jtfCredit.setText(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(credit));
        jtfBalance.setText(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(debit - credit));
        
        saveTempFileData();
    }
    
    private int obtainNextUserId() {
        int userId = 0;

        for (STableRow row : moPaneGridEntries.getGridRows()) {
            if (((SDataRecordEntry) row.getData()).getUserId() > userId) {
                userId = ((SDataRecordEntry) row.getData()).getUserId();
            }
        }

        return ++userId;
    }

    private java.util.Vector<erp.mfin.data.SDataCheck> obtainRecordChecks() {
        Vector<SDataCheck> checks = new Vector<>();

        for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); i++) {
            if (((SDataRecordEntry) moPaneGridEntries.getTableRow(i).getData()).getDbmsCheck() != null) {
                checks.add(((SDataRecordEntry) moPaneGridEntries.getTableRow(i).getData()).getDbmsCheck());
            }
        }

        return checks;
    }

    private void itemStateChangedAccountCashId_n() {
        renderAccountCash();
    }
    
    private void itemStateChangedEnableTempFileData() {
        if (jckEnableTempFileData.isSelected()) {
            try {
                saveTempFileData();
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
        else {
            deleteTempFile();
        }
    }

    private void actionPerformedDate() {
        miClient.getGuiDatePickerXXX().formReset();
        miClient.getGuiDatePickerXXX().setDate(moFieldDate.getDate());
        miClient.getGuiDatePickerXXX().setVisible(true);

        if (miClient.getGuiDatePickerXXX().getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldDate.setDate(miClient.getGuiDatePickerXXX().getGuiDate());
            jftDate.requestFocus();
        }
    }
    
    private void actionPerformedAddXml() {
        SDialogRecordEntryXml recordDps;
        ArrayList<SDataCfdRecordRow> aCfdRecordRows;
        
        aCfdRecordRows = new ArrayList<>();
        
        for (SDataCfdRecordRow row : maCfdRecordRows) {
            aCfdRecordRows.add(new SDataCfdRecordRow(row.getMoveId(), row.getCfdId(), row.getNameXml(), row.getPathXml()));
        }
        
        recordDps = new SDialogRecordEntryXml(miClient, aCfdRecordRows);
        recordDps.setVisible(true);
        
        if (recordDps.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            maCfdRecordRows.clear();
            maCfdRecordRows = recordDps.getGridRows();
        }
        
        mbIsXmlTranfer = false;
        updateFilesXmlInfo();
    }
    
    private void actionPerformedRecordToRecordEntryXml() {
        SDialogRecordXml recordDps;
        ArrayList<SDataCfdRecordRow> aCfdRecordRows;
        
        aCfdRecordRows = new ArrayList<>();
        
        for (SDataCfdRecordRow row : maCfdRecordRows) {
            aCfdRecordRows.add(new SDataCfdRecordRow(row.getMoveId(), row.getCfdId(), row.getNameXml(), row.getPathXml()));
        }
        
        if (!moRecord.getDbmsRecordEntries().isEmpty()) {
            recordDps = new SDialogRecordXml(miClient, moRecord.getDbmsRecordEntries(), aCfdRecordRows);
            recordDps.setVisible(true);

            if (recordDps.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                maCfdRecordRows.clear();
                maCfdRecordRows = recordDps.getGridRecordRows();
                ArrayList<SDataRecordEntryRow> recordEntriesRows = recordDps.getGridRecordEntryRows();
                for (SDataRecordEntry entry : moRecord.getDbmsRecordEntries()){
                    for (SDataRecordEntryRow recordEntryRow : recordEntriesRows) {
                        if (SLibUtilities.compareKeys(entry.getPrimaryKey(), recordEntryRow.getPrimaryKey())) {
                            entry.setDbmsXmlFilesNumber(recordEntryRow.getCfds().size());
                            break;
                        }
                    }
                }
                mbIsXmlTranfer = true;
            }
            updateFilesXmlInfo();
            renderRecordEntries(true);
        }
        else {
            miClient.showMsgBoxWarning("La póliza contable no tiene partidas activas.");
        }
    }
    
    private void actionPerformedGetXml() {
        if (jbGetXml.isEnabled()) {
            try {
            HashSet<SDataCfd> cfds = new HashSet<>();
            maCfdRecordRows.stream().map((cfdRecordRow) -> (SDataCfd) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_CFD, new int[] { cfdRecordRow.getCfdId() }, SLibConstants.EXEC_MODE_SILENT)).forEach((cfd) -> {
                cfds.add(cfd);
            });
            SCfdUtils.getXmlCfds(miClient, cfds);
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
    }
    
    private void actionPerformedAccountCashEdit() {
        if (jbAccountCashEdit.isEnabled()) {
            if (miClient.showMsgBoxConfirm("¿Está seguro que desea modificar el valor del campo '" + SGuiUtils.getLabelName(jlFkAccountCashId_n) + "'?") == JOptionPane.YES_OPTION) {
                jbAccountCashEdit.setEnabled(false);
                jcbFkAccountCashId_n.setEnabled(true);
                jcbFkAccountCashId_n.requestFocusInWindow();
            }
        }
    }
    
    private void actionPerformedEntryNew() throws Exception {
        if (jbEntryNew.isEnabled()) {
            SDataRecordEntry entry = null;

            updateRecord();

            moFormEntry.formReset();
            moFormEntry.setValue(SDataConstants.FIN_REC, moRecord);
            moFormEntry.setValue(SDataConstants.FIN_CHECK, obtainRecordChecks());

            switch (((int[]) ((SFormComponentItem) jcbGuiModeConcept.getSelectedItem()).getPrimaryKey())[0]) {
                case CONCEPT_POLICY_BLANK:
                    moFormEntry.setValue(SLibConstants.VALUE_TEXT, "");
                    break;
                case CONCEPT_POLICY_RECORD:
                    moFormEntry.setValue(SLibConstants.VALUE_TEXT, moFieldConcept.getString());
                    break;
                case CONCEPT_POLICY_CURR_ENTRY:
                    if (moPaneGridEntries.getTable().getSelectedRow() != -1) {
                        moFormEntry.setValue(SLibConstants.VALUE_TEXT, ((SDataRecordEntry) moPaneGridEntries.getSelectedTableRow().getData()).getConcept());
                    }
                    break;
                case CONCEPT_POLICY_LAST_ENTRY:
                    moFormEntry.setValue(SLibConstants.VALUE_TEXT, msAuxLastEntryConcept);
                    break;
                default:
            }

            moFormEntry.setFormVisible(true);

            if (moFormEntry.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                entry = (SDataRecordEntry) moFormEntry.getRegistry();

                moPaneGridEntries.addTableRow(new SDataRecordEntryRow(entry));
                renderRecordEntries(true);
                calculateBalance();
                moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);

                msAuxLastEntryConcept = (String) moFormEntry.getValue(SLibConstants.VALUE_TEXT);    // preserve last entry concept
            }
        }
    }

    private void actionPerformedEntryNewCopy() throws Exception {
        if (jbEntryNewCopy.isEnabled()) {
            int index = moPaneGridEntries.getTable().getSelectedRow();
            SDataRecordEntry entry = null;
            SDataRecordEntry entryCopy = null;

            if (index != -1) {
                entry = (SDataRecordEntry) moPaneGridEntries.getSelectedTableRow().getData();
                entryCopy = entry.clone();
                entryCopy.setIsRegistryNew(false);

                updateRecord();

                moFormEntry.formReset();
                moFormEntry.setValue(SDataConstants.FIN_REC, moRecord);
                moFormEntry.setValue(SDataConstants.FIN_CHECK, obtainRecordChecks());
                moFormEntry.setRegistry(entryCopy);
                moFormEntry.setFormVisible(true);

                if (moFormEntry.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    entry = (SDataRecordEntry) moFormEntry.getRegistry();
                    entry.resetRegistry();
                    entry.setPkEntryId(0);
                    entry.setIsSystem(false);

                    if (index + 1 < moPaneGridEntries.getTableGuiRowCount()) {
                        moPaneGridEntries.insertTableRow(new SDataRecordEntryRow(entry), index + 1);
                    }
                    else {
                        moPaneGridEntries.addTableRow(new SDataRecordEntryRow(entry));
                    }

                    renderRecordEntries(true);
                    calculateBalance();
                    moPaneGridEntries.setTableRowSelection(index + 1);

                    msAuxLastEntryConcept = (String) moFormEntry.getValue(SLibConstants.VALUE_TEXT);    // preserve last entry concept
                }
            }
        }
    }

    private void actionPerformedEntryNewInsert() throws Exception {
        if (jbEntryNewInsert.isEnabled()) {
            int index = moPaneGridEntries.getTable().getSelectedRow();
            SDataRecordEntry entry = null;

            if (index != -1) {
                updateRecord();

                moFormEntry.formReset();
                moFormEntry.setValue(SDataConstants.FIN_REC, moRecord);
                moFormEntry.setValue(SDataConstants.FIN_CHECK, obtainRecordChecks());

                switch (((int[]) ((SFormComponentItem) jcbGuiModeConcept.getSelectedItem()).getPrimaryKey())[0]) {
                    case CONCEPT_POLICY_BLANK:
                        moFormEntry.setValue(SLibConstants.VALUE_TEXT, "");
                        break;
                    case CONCEPT_POLICY_RECORD:
                        moFormEntry.setValue(SLibConstants.VALUE_TEXT, moFieldConcept.getString());
                        break;
                    case CONCEPT_POLICY_CURR_ENTRY:
                        if (moPaneGridEntries.getTable().getSelectedRow() != -1) {
                            moFormEntry.setValue(SLibConstants.VALUE_TEXT, ((SDataRecordEntry) moPaneGridEntries.getSelectedTableRow().getData()).getConcept());
                        }
                        break;
                    case CONCEPT_POLICY_LAST_ENTRY:
                        moFormEntry.setValue(SLibConstants.VALUE_TEXT, msAuxLastEntryConcept);
                        break;
                    default:
                }

                moFormEntry.setFormVisible(true);

                if (moFormEntry.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    entry = (SDataRecordEntry) moFormEntry.getRegistry();

                    moPaneGridEntries.insertTableRow(new SDataRecordEntryRow(entry), index);
                    renderRecordEntries(true);
                    calculateBalance();
                    moPaneGridEntries.setTableRowSelection(index + 1);

                    msAuxLastEntryConcept = (String) moFormEntry.getValue(SLibConstants.VALUE_TEXT);    // preserve last entry concept
                }
            }
        }
    }

    private void actionPerformedEntryEdit() throws Exception {
        if (jbEntryEdit.isEnabled()) {
            int index = moPaneGridEntries.getTable().getSelectedRow();
            SDataRecordEntry entry = null;
            SFormInterface form = null;

            if (index != -1) {
                entry = (SDataRecordEntry) moPaneGridEntries.getSelectedTableRow().getData();

                updateRecord();

                if (entry.getDbmsCheck() != null) {
                    form = moFormMoneyOutCheck;
                    form.setValue(SDataConstants.FIN_REC, moRecord);
                    form.setValue(SDataConstants.FIN_CHECK, obtainRecordChecks());
                    form.formRefreshCatalogues();
                    form.formReset();
                }
                else {
                    form = moFormEntry;
                    form.formReset();
                    form.setValue(SDataConstants.FIN_REC, moRecord);
                    form.setValue(SDataConstants.FIN_CHECK, obtainRecordChecks());
                }

                form.setRegistry(entry);
                form.setFormVisible(true);

                if (form.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    moPaneGridEntries.setTableRow(new SDataRecordEntryRow((SDataRecordEntry) form.getRegistry()), index);
                    renderRecordEntries(true);
                    calculateBalance();
                    moPaneGridEntries.setTableRowSelection(index);
                }
            }
        }
    }

    private void actionPerformedEntryDelete() throws Exception {
        if (jbEntryDelete.isEnabled()) {
            int index = moPaneGridEntries.getTable().getSelectedRow();
            int userId = 0;
            SDataRecordEntry entry = null;

            if (index != -1 && miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                entry = (SDataRecordEntry) moPaneGridEntries.getTableRow(index).getData();

                if (entry.getIsDeleted()) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_REG_ALREADY_DELETE);
                }
                else {
                    userId = entry.getUserId();

                    if (userId == 0) {
                        // Delete single entry:

                        if (entry.getIsRegistryNew()) {
                            moPaneGridEntries.removeTableRow(index);
                        }
                        else {
                            entry.setIsDeleted(true);
                            entry.setFkUserDeleteId(miClient.getSession().getUser().getPkUserId());
                            entry.setIsRegistryEdited(true);
                            entry.setFkUserEditId(miClient.getSession().getUser().getPkUserId());

                            moPaneGridEntries.setTableRow(new SDataRecordEntryRow(entry), index);
                        }
                    }
                    else {
                        // Delete group of entries:

                        for (int i = 0; i < moPaneGridEntries.getTableGuiRowCount(); ) {
                            entry = (SDataRecordEntry) moPaneGridEntries.getTableRow(i).getData();
                            if (entry.getUserId() != userId) {
                                i++;
                            }
                            else {
                                if (entry.getIsRegistryNew()) {
                                    moPaneGridEntries.removeTableRow(i);
                                    moPaneGridEntries.renderTableRows();
                                }
                                else {
                                    entry.setIsDeleted(true);
                                    entry.setFkUserDeleteId(miClient.getSession().getUser().getPkUserId());
                                    entry.setIsRegistryEdited(true);
                                    entry.setFkUserEditId(miClient.getSession().getUser().getPkUserId());

                                    moPaneGridEntries.setTableRow(new SDataRecordEntryRow(entry), i++);
                                }
                            }
                        }
                    }
                }

                renderRecordEntries(true);
                calculateBalance();
                moPaneGridEntries.setTableRowSelection(index < moPaneGridEntries.getTableGuiRowCount() ? index : moPaneGridEntries.getTableGuiRowCount() - 1);
            }
        }
    }

    private void actionPerformedEntryMoveDown() {
        if (jbEntryMoveDown.isEnabled()) {
            int index = moPaneGridEntries.getTable().getSelectedRow();

            if (index != -1 && index + 1 < moPaneGridEntries.getTableGuiRowCount()) {
                SDataRecordEntry entryA = (SDataRecordEntry) moPaneGridEntries.getTableRow(index).getData();
                SDataRecordEntry entryB = (SDataRecordEntry) moPaneGridEntries.getTableRow(index + 1).getData();

                moPaneGridEntries.setTableRow(new SDataRecordEntryRow(entryB), index);
                moPaneGridEntries.setTableRow(new SDataRecordEntryRow(entryA), index + 1);
                renderRecordEntries(true);
                moPaneGridEntries.setTableRowSelection(index + 1);
            }
        }
    }

    private void actionPerformedEntryMoveUp() {
        if (jbEntryMoveUp.isEnabled()) {
            int index = moPaneGridEntries.getTable().getSelectedRow();

            if (index > 0) {
                SDataRecordEntry entryA = (SDataRecordEntry) moPaneGridEntries.getTableRow(index - 1).getData();
                SDataRecordEntry entryB = (SDataRecordEntry) moPaneGridEntries.getTableRow(index).getData();

                moPaneGridEntries.setTableRow(new SDataRecordEntryRow(entryB), index - 1);
                moPaneGridEntries.setTableRow(new SDataRecordEntryRow(entryA), index);
                renderRecordEntries(true);
                moPaneGridEntries.setTableRowSelection(index - 1);
            }
        }
    }

    private void actionPerformedEntryViewSum() {
        double debit = 0;
        double credit = 0;

        if (moPaneGridEntries.getTable().getSelectedRow() != -1) {
            for (int row : moPaneGridEntries.getTable().getSelectedRows()) {
                debit += ((SDataRecordEntry) ((SDataRecordEntryRow) moPaneGridEntries.getTableRow(row)).getData()).getDebit();
                credit += ((SDataRecordEntry) ((SDataRecordEntryRow) moPaneGridEntries.getTableRow(row)).getData()).getCredit();
            }

            miClient.showMsgBoxInformation(
                    "Total cargos: $ " + SLibUtils.getDecimalFormatAmount().format(debit) + ".\n" +
                    "Total abonos: $ " + SLibUtils.getDecimalFormatAmount().format(credit) + "." +
                    (debit == 0 || credit == 0 ? "" : "\nDiferencia (cargos - abonos): $ " + SLibUtils.getDecimalFormatAmount().format(debit - credit) + "."));
        }
    }
    
    private void actionPerformedEntryGetXml() {
        if (jbEntryGetXml.isEnabled()) {
            if (moPaneGridEntries.getSelectedTableRow() == null || moPaneGridEntries.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    HashSet<SDataCfd> cfds = ((SDataRecordEntryRow)moPaneGridEntries.getSelectedTableRow()).getCfds();
                    SCfdUtils.getXmlCfds(miClient, cfds);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }
    
    private void actionPerformedEntryShowDirectCfd() {
        if (jbEntryShowDirectCfd.isEnabled()) {
            if (moPaneGridEntries.getSelectedTableRow() == null || moPaneGridEntries.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                SDataRecordEntry entry = (SDataRecordEntry) moPaneGridEntries.getSelectedTableRow().getData();
                SModuleUtilities.showCfdi(miClient, entry.getPrimaryKey(), entry, SDataConstants.FINX_REC_CFD_DIRECT);
            }
        }
    }
    
    private void actionPerformedEntryShowIndirectCfd() {
        if (jbEntryShowIndirectCfd.isEnabled()) {
            if (moPaneGridEntries.getSelectedTableRow() == null || moPaneGridEntries.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                SDataRecordEntry entry = (SDataRecordEntry) moPaneGridEntries.getSelectedTableRow().getData();
                SModuleUtilities.showCfdi(miClient, entry.getPrimaryKey(), entry, SDataConstants.FINX_REC_CFD_INDIRECT);
            }
        }
    }
    
    private void actionPerfomedImportFile() {
        if (jbImportFile.isEnabled()) {
            try {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de Excel (*.xlsx)", "xlsx");
                miClient.getFileChooser().setFileFilter(filter);
                if (miClient.getFileChooser().showOpenDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
                    if (miClient.getFileChooser().getSelectedFile().getName().toLowerCase().matches(".*\\.(xlsx)$")) {
                        
                        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                        ArrayList<SDataRecordEntry> entries = SFinRecordUtils.processRecordEntriesFile(miClient, miClient.getFileChooser().getSelectedFile(), moRecord);
                        for (SDataRecordEntry entry : entries) {
                            moPaneGridEntries.addTableRow(new SDataRecordEntryRow(entry));
                            renderRecordEntries(true);
                            calculateBalance();
                            
                            msAuxLastEntryConcept = (String) moFormEntry.getValue(SLibConstants.VALUE_TEXT);
                        }
                        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            }
            catch (Exception e) {
                miClient.showMsgBoxWarning(e.getMessage());
            }
        }
    }

    private void actionPerfomedDownFile() {
        if (jbDownFile.isEnabled()) {
            String filePath = "reps/formato polizas contables.xlsx";
            File fileOrig = new File(filePath);
            if (fileOrig.exists()) {
                miClient.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (miClient.getFileChooser().showSaveDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
                    File folderDest = miClient.getFileChooser().getSelectedFile();
                    File fileDest = new File(folderDest, fileOrig.getName());

                    try {
                        Files.copy(fileOrig.toPath(), fileDest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        miClient.showMsgBoxInformation("Archivo descargado correctamente.");
                    }
                    catch (IOException e) {
                        miClient.showMsgBoxWarning("Error al descargar el archivo.");
                    }
                }
            }
            else {
                miClient.showMsgBoxInformation("El archivo de origen no existe, contactar a soporte.");
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private void actionApportionment() {
        if (jbApportionment.isEnabled()) {
            try {
                if (moDialogApportioment == null) {
                    moDialogApportioment = new SDialogRecordApportionment(miClient);
                    moDialogApportioment.formRefreshCatalogues();
                }
                moDialogApportioment.formReset();
                moDialogApportioment.setValue(SDataConstants.FIN_REC, moRecord);
                moDialogApportioment.setFormVisible(true);

                if (moDialogApportioment.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    ArrayList<SDataRecordEntry> entries = (ArrayList<SDataRecordEntry>) moDialogApportioment.getValue(SDataConstants.FIN_REC_ETY);
                    for (SDataRecordEntry entry : entries) {
                        moPaneGridEntries.addTableRow(new SDataRecordEntryRow(entry));
                        renderRecordEntries(true);
                        calculateBalance();

                        msAuxLastEntryConcept = (String) moFormEntry.getValue(SLibConstants.VALUE_TEXT);
                    }
                }
            }
            catch (Exception e) {
                miClient.showMsgBoxInformation("Error al agregar las partidas, contactar a soporte.");
            }
        }
    }
    
    private void actionPerformedMoneyInOut(final SDataAccountCash cash_n, final boolean in) throws Exception {
        updateRecord();

        moFormMoneyInOut.formRefreshCatalogues();
        moFormMoneyInOut.formReset();
        moFormMoneyInOut.setValue(SDataConstants.FIN_REC, moRecord);
        moFormMoneyInOut.setValue(SDataConstants.FIN_ACC_CASH, cash_n);
        moFormMoneyInOut.setValue(SDataConstantsSys.FINS_CT_SYS_MOV_CASH, in);
        moFormMoneyInOut.setFormVisible(true);

        if (moFormMoneyInOut.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            SDataRecordEntry entry = (SDataRecordEntry) moFormMoneyInOut.getRegistry();

            moPaneGridEntries.addTableRow(new SDataRecordEntryRow(entry));
            renderRecordEntries(true);
            calculateBalance();
            moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);
        }
    }

    private void actionPerformedMoneyInOutBizPartner(final int bizPartnerCategoryX, final int[] keyAccountingMoveSubclass) throws Exception {
        updateRecord();
        
        moFormMoneyInOutBizPartner.setValue(SDataConstants.FIN_REC, moRecord);
        moFormMoneyInOutBizPartner.setValue(SDataConstants.BPSU_BP, bizPartnerCategoryX);
        moFormMoneyInOutBizPartner.setValue(SDataConstants.FINS_CLS_ACC_MOV, keyAccountingMoveSubclass);
        moFormMoneyInOutBizPartner.setValue(SDataConstants.FIN_CHECK, obtainRecordChecks());
        moFormMoneyInOutBizPartner.formRefreshCatalogues();
        moFormMoneyInOutBizPartner.formReset();
        moFormMoneyInOutBizPartner.setFormVisible(true);

        if (moFormMoneyInOutBizPartner.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            SDataRecordEntry entry = (SDataRecordEntry) moFormMoneyInOutBizPartner.getRegistry();

            moPaneGridEntries.addTableRow(new SDataRecordEntryRow(entry));
            renderRecordEntries(true);
            calculateBalance();
            moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);
        }
    }

    private void actionPerformedPayment(final int bizPartnerCategoryX) {
        erp.lib.form.SFormInterface dialog = null;
        
        switch (bizPartnerCategoryX) {
            case SDataConstants.BPSX_BP_SUP:
                dialog = jrbModeInputMultiple.isSelected() ? moDialogPaymentMultipleSup : moDialogPaymentSup;
                break;
            case SDataConstants.BPSX_BP_CUS:
                dialog = jrbModeInputMultiple.isSelected() ? moDialogPaymentMultipleCus : moDialogPaymentCus;
                break;
            default:
                // do nothing
        }
        
        if (dialog != null) {
            try {
                double exchangeRate = 0d;
                double[] cashBalance = null;
                
                if (moAccountCash == null) {
                    exchangeRate = 1;
                    cashBalance = new double[] { 0, 0 };
                }
                else {
                    exchangeRate = SDataUtilities.obtainExchangeRate(miClient, moAccountCash.getFkCurrencyId(), moRecord.getDate());
                    cashBalance = SDataUtilities.obtainAccountCashBalanceUpdated(miClient, moAccountCash.getFkCurrencyId(), moRecord.getDate(), moAccountCash.getPrimaryKey(), 
                            moAccountCash.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH ? SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH : SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK, moRecord, null);
                }
                
                updateRecord();

                dialog.formRefreshCatalogues();
                dialog.formReset();
                dialog.setValue(SDataConstants.FIN_ACC_CASH, new Object[] { moRecord, moAccountCash, exchangeRate, cashBalance });
                dialog.setValue(SDataConstants.FIN_CHECK, obtainRecordChecks()); // applies only for single payments!, otherwise value is discarded
                dialog.setValue(SDialogRecordPaymentMultiple.MODE, SDialogRecordPaymentMultiple.MODE_BP_ONLY); // applies only for multiple payments!, otherwise value is discarded
                dialog.setFormVisible(true);

                if (dialog.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    int userId = obtainNextUserId();
                    SDataRecord record = (SDataRecord) dialog.getRegistry();

                    if (!record.getDbmsRecordEntries().isEmpty()) {
                        for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
                            entry.setUserId(userId);
                            moPaneGridEntries.addTableRow(new SDataRecordEntryRow(entry));
                        }

                        renderRecordEntries(true);
                        calculateBalance();
                        moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);
                    }
                }
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
    }

    private void actionPerformedPaymentComplete(final int bizPartnerCategoryX) {
        erp.lib.form.SFormInterface dialog = null;
        
        switch (bizPartnerCategoryX) {
            case SDataConstants.BPSX_BP_SUP:
                dialog = jrbModeInputMultiple.isSelected() ? moDialogPaymentMultipleSup : moDialogPaymentCompleteSup;
                break;
            case SDataConstants.BPSX_BP_CUS:
                dialog = jrbModeInputMultiple.isSelected() ? moDialogPaymentMultipleCus : moDialogPaymentCompleteCus;
                break;
            default:
                // do nothing
        }
        
        if (dialog != null) {
            try {
                double exchangeRate = 0d;
                double[] cashBalance = null;
                
                if (moAccountCash == null) {
                    exchangeRate = 1;
                    cashBalance = new double[] { 0, 0 };
                }
                else {
                    exchangeRate = SDataUtilities.obtainExchangeRate(miClient, moAccountCash.getFkCurrencyId(), moRecord.getDate());
                    cashBalance = SDataUtilities.obtainAccountCashBalanceUpdated(miClient, moAccountCash.getFkCurrencyId(), moRecord.getDate(), moAccountCash.getPrimaryKey(), 
                            moAccountCash.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH ? SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH : SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK, moRecord, null);
                }
                
                updateRecord();

                dialog.formRefreshCatalogues();
                dialog.formReset();
                dialog.setValue(SDataConstants.FIN_ACC_CASH, new Object[] { moRecord, moAccountCash, exchangeRate, cashBalance });
                dialog.setValue(SDialogRecordPaymentMultiple.MODE, SDialogRecordPaymentMultiple.MODE_BP_CASH_ACC); // applies only for multiple payments!, otherwise value is discarded
                dialog.setFormVisible(true);

                if (dialog.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    int userId = obtainNextUserId();
                    SDataRecord record = (SDataRecord) dialog.getRegistry();

                    if (!record.getDbmsRecordEntries().isEmpty()) {
                        for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
                            entry.setUserId(userId);
                            moPaneGridEntries.addTableRow(new SDataRecordEntryRow(entry));
                        }

                        renderRecordEntries(true);
                        calculateBalance();
                        moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);
                    }
                }
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
    }

    private void actionPerformedExchangeRateDiff(final boolean profit) throws Exception {
        int userId = 0;
        String msg = "";
        SDataRecord record = null;

        if (profit) {
            if (!SDataUtilities.validateAccountSyntax(miClient.getSessionXXX().getParamsCompany().getFkAccountDifferenceIncomeId_n())) {
                msg = SFinConsts.MSG_ERR_GUI_CFG_DIFF_ACC;
            }
            else if (!SDataUtilities.validateAccountSyntax(miClient.getSessionXXX().getParamsCompany().getFkCostCenterDifferenceIncomeId_n())) {
                msg = SFinConsts.MSG_ERR_GUI_CFG_DIFF_CC;
            }
            else if (miClient.getSessionXXX().getParamsCompany().getFkItemDifferenceIncomeId_n() == SLibConsts.UNDEFINED) {
                msg = SFinConsts.MSG_ERR_GUI_CFG_DIFF_ITEM;
            }
        }
        else {
            if (!SDataUtilities.validateAccountSyntax(miClient.getSessionXXX().getParamsCompany().getFkAccountDifferenceExpenseId_n())) {
                msg = SFinConsts.MSG_ERR_GUI_CFG_DIFF_ACC;
            }
            else if (!SDataUtilities.validateAccountSyntax(miClient.getSessionXXX().getParamsCompany().getFkCostCenterDifferenceExpenseId_n())) {
                msg = SFinConsts.MSG_ERR_GUI_CFG_DIFF_CC;
            }
            else if (miClient.getSessionXXX().getParamsCompany().getFkItemDifferenceExpenseId_n() == SLibConsts.UNDEFINED) {
                msg = SFinConsts.MSG_ERR_GUI_CFG_DIFF_ITEM;
            }
        }
        
        if (!msg.isEmpty()) {
            miClient.showMsgBoxWarning(msg);
        }
        else {
            updateRecord();
            
            moFormExchangeRateDiff.setValue(SDataConstants.FIN_REC, moRecord);
            moFormExchangeRateDiff.setValue(SDataConstantsSys.FINS_CT_SYS_MOV_CASH, profit);
            moFormExchangeRateDiff.formReset();
            moFormExchangeRateDiff.setFormVisible(true);

            if (moFormExchangeRateDiff.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                userId = obtainNextUserId();
                record = (SDataRecord) moFormExchangeRateDiff.getRegistry();

                if (!record.getDbmsRecordEntries().isEmpty()) {
                    for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
                        entry.setUserId(userId);
                        moPaneGridEntries.addTableRow(new SDataRecordEntryRow(entry));
                    }

                    renderRecordEntries(true);
                    calculateBalance();
                    moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);
                }
            }
        }
    }

    private void actionPerformedCheck() throws Exception {
        updateRecord();

        moFormMoneyOutCheck.setValue(SDataConstants.FIN_REC, moRecord);
        moFormMoneyOutCheck.setValue(SDataConstants.FIN_CHECK, obtainRecordChecks());
        moFormMoneyOutCheck.formRefreshCatalogues();
        moFormMoneyOutCheck.formReset();
        moFormMoneyOutCheck.setFormVisible(true);

        if (moFormMoneyOutCheck.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            SDataRecordEntry entry = (SDataRecordEntry) moFormMoneyOutCheck.getRegistry();

            moPaneGridEntries.addTableRow(new SDataRecordEntryRow(entry));
            renderRecordEntries(true);
            calculateBalance();
            moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);
        }
    }

    private void actionPerformedTransfer() throws Exception {
        updateRecord();

        moFormMoneyOutTransfer.setValue(SDataConstants.FIN_REC, moRecord);
        moFormMoneyOutTransfer.formRefreshCatalogues();
        moFormMoneyOutTransfer.formReset();
        moFormMoneyOutTransfer.setFormVisible(true);

        if (moFormMoneyOutTransfer.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            int userId = obtainNextUserId();
            SDataRecord record = (SDataRecord) moFormMoneyOutTransfer.getRegistry();

            if (record.getDbmsRecordEntries().size() > 0) {
                for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
                    entry.setUserId(userId);
                    moPaneGridEntries.addTableRow(new SDataRecordEntryRow(entry));
                }

                renderRecordEntries(true);
                calculateBalance();
                moPaneGridEntries.setTableRowSelection(moPaneGridEntries.getTableGuiRowCount() - 1);
            }
        }
    }

    private void actionPerformedEntryDeletedFilter() {
        if (jtbEntryDeletedFilter.isEnabled()) {
            int index = moPaneGridEntries.getTable().getSelectedRow();

            moPaneGridEntries.setGridViewStatus(!jtbEntryDeletedFilter.isSelected() ? STableConstants.VIEW_STATUS_ALL : STableConstants.VIEW_STATUS_ALIVE);
            renderRecordEntries(false);
            moPaneGridEntries.setTableRowSelection(index < moPaneGridEntries.getTableGuiRowCount() ? index : moPaneGridEntries.getTableGuiRowCount() - 1);

            jbEntryMoveUp.setEnabled(jtbEntryDeletedFilter.isSelected() && jbEntryNew.isEnabled());
            jbEntryMoveDown.setEnabled(jtbEntryDeletedFilter.isSelected() && jbEntryNew.isEnabled());
        }
    }

    private void actionEdit(boolean edit) {

    }

    private void actionPerformedOk() {
        if (jbOk.isEnabled()) {
            boolean ok = true;
            SFormValidation validation = formValidate();

            if (validation.getIsError()) {
                if (validation.getComponent() != null) {
                    validation.getComponent().requestFocus();
                }
                if (!validation.getMessage().isEmpty()) {
                    miClient.showMsgBoxWarning(validation.getMessage());
                }
            }
            else {
                if (!mbOldIsDeleted && jckIsDeleted.isSelected() && miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) != JOptionPane.YES_OPTION) {
                    ok = false;
                }

                if (ok) {
                    mnFormResult = SLibConstants.FORM_RESULT_OK;
                    setVisible(false);
                }
            }
        }
    }

    private void actionPerformedCancel() {
        if (jbCancel.isEnabled()) {
            if (mbNonEditable || miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_FORM_CLOSE) == JOptionPane.YES_OPTION) {
                deleteTempFile();
                
                mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
                setVisible(false);
            }
        }
    }
    
    private void updateFilesXmlInfo() {
        int countFilesXml = 0;
        
        countFilesXml = maCfdRecordRows.stream().filter((row) -> (!row.getNameXml().isEmpty())).map((_item) -> 1).reduce(countFilesXml, Integer::sum);
        jtfXmlFilesNumber.setText(countFilesXml + "");
    }

    public void publicActionEntryNew() {
        try {
            actionPerformedEntryNew();
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    public void publicActionEntryNewInsert() {
        try {
            actionPerformedEntryNewInsert();
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    public void publicActionEntryNewCopy() {
        try {
            actionPerformedEntryNewCopy();
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    public void publicActionEntryEdit() {
        try {
            actionPerformedEntryEdit();
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    public void publicActionEntryDelete() {
        try {
            actionPerformedEntryDelete();
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    public void publicActionEntryMoveDown() {
        actionPerformedEntryMoveDown();
    }

    public void publicActionEntryMoveUp() {
        actionPerformedEntryMoveUp();
    }

    public void publicActionEntryViewSum() {
        actionPerformedEntryViewSum();
    }

    public void publicActionEntryFilter() {
        jtbEntryDeletedFilter.setSelected(!jtbEntryDeletedFilter.isSelected());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgModeInput;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbAccountCashEdit;
    private javax.swing.JButton jbAddXml;
    private javax.swing.JButton jbApportionment;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbCdtAdvanceCus;
    private javax.swing.JButton jbCdtAdvanceSupDev;
    private javax.swing.JButton jbCdtCreditCdr;
    private javax.swing.JButton jbCdtCreditDbr;
    private javax.swing.JButton jbCdtExchangeRateDiff;
    private javax.swing.JButton jbCdtPaymentCus;
    private javax.swing.JButton jbDate;
    private javax.swing.JButton jbDbtAdvanceCusDev;
    private javax.swing.JButton jbDbtAdvanceSup;
    private javax.swing.JButton jbDbtDebitCdr;
    private javax.swing.JButton jbDbtDebitDbr;
    private javax.swing.JButton jbDbtExchangeRateDiff;
    private javax.swing.JButton jbDbtPaymentSup;
    private javax.swing.JButton jbDownFile;
    private javax.swing.JButton jbEntryDelete;
    private javax.swing.JButton jbEntryEdit;
    private javax.swing.JButton jbEntryGetXml;
    private javax.swing.JButton jbEntryMoveDown;
    private javax.swing.JButton jbEntryMoveUp;
    private javax.swing.JButton jbEntryNew;
    private javax.swing.JButton jbEntryNewCopy;
    private javax.swing.JButton jbEntryNewInsert;
    private javax.swing.JButton jbEntryShowDirectCfd;
    private javax.swing.JButton jbEntryShowIndirectCfd;
    private javax.swing.JButton jbEntryViewSum;
    private javax.swing.JButton jbGetXml;
    private javax.swing.JButton jbImportFile;
    private javax.swing.JButton jbMiAdvanceCus;
    private javax.swing.JButton jbMiAdvanceSupDev;
    private javax.swing.JButton jbMiCreditCdr;
    private javax.swing.JButton jbMiCreditDbr;
    private javax.swing.JButton jbMiExchangeRateDiff;
    private javax.swing.JButton jbMiPaymentCus;
    private javax.swing.JButton jbMoAdvanceCusDev;
    private javax.swing.JButton jbMoAdvanceSup;
    private javax.swing.JButton jbMoDebitCdr;
    private javax.swing.JButton jbMoDebitDbr;
    private javax.swing.JButton jbMoExchangeRateDiff;
    private javax.swing.JButton jbMoPaymentSup;
    private javax.swing.JButton jbMoneyIn;
    private javax.swing.JButton jbMoneyInOther;
    private javax.swing.JButton jbMoneyInPaymentCus;
    private javax.swing.JButton jbMoneyOut;
    private javax.swing.JButton jbMoneyOutCheck;
    private javax.swing.JButton jbMoneyOutOther;
    private javax.swing.JButton jbMoneyOutPaymentSup;
    private javax.swing.JButton jbMoneyOutTransfer;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbRecordToRecordEntryXml;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkAccountCashId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbGuiModeConcept;
    private javax.swing.JCheckBox jckEnableTempFileData;
    private javax.swing.JCheckBox jckIsAdjustmentAudit;
    private javax.swing.JCheckBox jckIsAdjustmentYearEnd;
    private javax.swing.JCheckBox jckIsAudited;
    private javax.swing.JCheckBox jckIsAuthorized;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JCheckBox jckIsRecordConceptCopyEnabled;
    private javax.swing.JCheckBox jckIsSystem;
    private javax.swing.JFormattedTextField jftDate;
    private javax.swing.JLabel jlBalance;
    private javax.swing.JLabel jlCompanyBranch;
    private javax.swing.JLabel jlConcept;
    private javax.swing.JLabel jlCredit;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlDebit;
    private javax.swing.JLabel jlFkAccountCashId_n;
    private javax.swing.JLabel jlGuiModeConcept;
    private javax.swing.JLabel jlGuiModeInput;
    private javax.swing.JLabel jlPeriod;
    private javax.swing.JLabel jlPkNumberId;
    private javax.swing.JLabel jlXmlFiles;
    private javax.swing.JPanel jpCommands;
    private javax.swing.JPanel jpCommands11;
    private javax.swing.JPanel jpCommands12;
    private javax.swing.JPanel jpCommands13;
    private javax.swing.JPanel jpCommandsCashAccount;
    private javax.swing.JPanel jpCommandsCashAccount1;
    private javax.swing.JPanel jpCommandsCashAccount2;
    private javax.swing.JPanel jpCommandsEntries;
    private javax.swing.JPanel jpCommandsJournal;
    private javax.swing.JPanel jpCommandsJournal1;
    private javax.swing.JPanel jpCommandsJournal2;
    private javax.swing.JPanel jpControlCashAccount2;
    private javax.swing.JPanel jpControlCashAccount3;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpControls1;
    private javax.swing.JPanel jpControls2;
    private javax.swing.JPanel jpEntries;
    private javax.swing.JPanel jpRecord;
    private javax.swing.JPanel jpRecord1;
    private javax.swing.JPanel jpRecord2;
    private javax.swing.JPanel jpRegistry;
    private javax.swing.JRadioButton jrbModeInputMultiple;
    private javax.swing.JRadioButton jrbModeInputSimple;
    private javax.swing.JSeparator jsEntry01;
    private javax.swing.JSeparator jsEntry2;
    private javax.swing.JSeparator jsEntry3;
    private javax.swing.JSeparator jsEntry4;
    private javax.swing.JSeparator jsEntry5;
    private javax.swing.JSeparator jsEntry6;
    private javax.swing.JToggleButton jtbEntryDeletedFilter;
    private javax.swing.JTextField jtfAccountCashCurrencyKey;
    private javax.swing.JTextField jtfBalance;
    private javax.swing.JTextField jtfCompanyBranch;
    private javax.swing.JTextField jtfConcept;
    private javax.swing.JTextField jtfCredit;
    private javax.swing.JTextField jtfCurrencyKeyBalance;
    private javax.swing.JTextField jtfCurrencyKeyCredit;
    private javax.swing.JTextField jtfCurrencyKeyDebit;
    private javax.swing.JTextField jtfDebit;
    private javax.swing.JTextField jtfPeriod;
    private javax.swing.JTextField jtfPkNumberId;
    private javax.swing.JTextField jtfXmlFilesNumber;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {

    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        moRecord = null;
        moRecordType = null;
        moAccountCash = null;
        mbNonEditable = false;
        mbOldIsDeleted = false;
        msAuxLastEntryConcept = "";

        moPaneGridEntries.createTable(this);
        moPaneGridEntries.clearTableRows();
        moPaneGridEntries.setGridViewStatus(STableConstants.VIEW_STATUS_ALIVE);
        moPaneGridEntries.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jtbEntryDeletedFilter.setSelected(true);
        jpCommands.remove(jpCommandsCashAccount);
        jpCommands.remove(jpCommandsJournal);

        for (int i = 0; i < mvFields.size(); i++) {
            mvFields.get(i).resetField();
        }

        jckIsRecordConceptCopyEnabled.setSelected(miClient.getSessionXXX().getParamsErp().getIsRecordConceptCopyEnabled());
        jtfCurrencyKeyDebit.setText(miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
        jtfCurrencyKeyCredit.setText(miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
        jtfCurrencyKeyBalance.setText(miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());

        jftDate.setEditable(true);
        jftDate.setFocusable(true);
        jtfConcept.setEditable(true);
        jtfConcept.setFocusable(true);
        jcbFkAccountCashId_n.setEnabled(false); // by default, is read only
        jbAccountCashEdit.setEnabled(false);
        
        jbDate.setEnabled(true);
        jbAddXml.setEnabled(true);
        jbRecordToRecordEntryXml.setEnabled(true);
        jbGetXml.setEnabled(true);
        jbEntryNew.setEnabled(true);
        jbEntryNewInsert.setEnabled(true);
        jbEntryNewCopy.setEnabled(true);
        jbEntryEdit.setEnabled(true);
        jbEntryDelete.setEnabled(true);
        jbEntryMoveDown.setEnabled(true);
        jbEntryMoveUp.setEnabled(true);
        
        jbMoneyIn.setEnabled(true);
        jbMoneyInOther.setEnabled(true);
        jbMoneyInPaymentCus.setEnabled(true);
        jbMiPaymentCus.setEnabled(true);
        jbMiAdvanceCus.setEnabled(true);
        jbMoAdvanceCusDev.setEnabled(true);
        jbMiCreditCdr.setEnabled(true);
        jbMiCreditDbr.setEnabled(true);
        jbMiExchangeRateDiff.setEnabled(true);
        
        jbMoneyOut.setEnabled(true);
        jbMoneyOutOther.setEnabled(true);
        jbMoneyOutPaymentSup.setEnabled(true);
        jbMoneyOutCheck.setEnabled(true);
        jbMoneyOutTransfer.setEnabled(true);
        jbMoPaymentSup.setEnabled(true);
        jbMoAdvanceSup.setEnabled(true);
        jbMiAdvanceSupDev.setEnabled(true);
        jbMoDebitCdr.setEnabled(true);
        jbMoDebitDbr.setEnabled(true);
        jbMoExchangeRateDiff.setEnabled(true);
        
        jbDbtPaymentSup.setEnabled(true);
        jbDbtAdvanceSup.setEnabled(true);
        jbDbtAdvanceCusDev.setEnabled(true);
        jbDbtDebitCdr.setEnabled(true);
        jbDbtDebitDbr.setEnabled(true);
        jbDbtExchangeRateDiff.setEnabled(true);
        
        jbCdtPaymentCus.setEnabled(true);
        jbCdtAdvanceCus.setEnabled(true);
        jbCdtAdvanceSupDev.setEnabled(true);
        jbCdtCreditCdr.setEnabled(true);
        jbCdtCreditDbr.setEnabled(true);
        jbCdtExchangeRateDiff.setEnabled(true);
        
        jckEnableTempFileData.setEnabled(true);
        jbOk.setEnabled(true);

        jlGuiModeInput.setEnabled(true);
        jrbModeInputSimple.setEnabled(true);
        jrbModeInputMultiple.setEnabled(true);
        
        jlGuiModeConcept.setEnabled(true);
        jcbGuiModeConcept.setEnabled(true);
        
        jckIsDeleted.setEnabled(true);
        jckIsAdjustmentYearEnd.setEnabled(true);
        jckIsAdjustmentAudit.setEnabled(true);
        
        maCfdRecordRows = new ArrayList<>();
        jtfXmlFilesNumber.setText("0");

        try {
            renderHeader();
            calculateBalance();
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbFkAccountCashId_n, SDataConstants.FIN_ACC_CASH, new int[] { miClient.getSessionXXX().getCurrentCompanyBranchId() });
        moFormEntry.formRefreshCatalogues();
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(mvFields.get(i).getComponent());
                break;
            }
        }

        if (!validation.getIsError()) {
            // Validate accounting record period:

            if (!SLibTimeUtilities.isBelongingToPeriod(moFieldDate.getDate(), moRecord.getPkYearId(), moRecord.getPkPeriodId())) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_DATE_REC);
                validation.setComponent(jftDate);
            }
            else if (moRecordType.getIsAccountCashRequired() && jcbFkAccountCashId_n.getSelectedIndex() <= 0) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkAccountCashId_n.getText() + "'.");
                validation.setComponent(jcbFkAccountCashId_n);
            }
            if (moFieldIsDeleted.getBoolean()) {
                int deleted = 0;

                for (STableRow row : moPaneGridEntries.getGridRows()) {
                    SDataRecordEntry entry = (SDataRecordEntry) row.getData();
                    if (entry.getIsSystem() && entry.getUserId() == 0) {
                        deleted++;
                    }
                }

                if (deleted > 0) {
                    validation.setMessage("No se puede eliminar esta póliza contable porque tiene " + SLibUtils.DecimalFormatInteger.format(deleted) + " " + (deleted == 1 ? "partida" : "partidas") + " de sistema.");
                }
            }

            if (!validation.getIsError()) {
                if (jcbFkAccountCashId_n.getSelectedIndex() > 0) {
                    try {
                        updateRecord();
                    }
                    catch (Exception e) {
                        validation.setMessage(e.getMessage());
                    }
                    
                    if (!validation.getIsError()) {
                        // Validate account cash currency:

                        String currency = miClient.getSession().getSessionCustom().getCurrency(new int[] { moRecord.getDbmsDataAccountCash().getFkCurrencyId() });

                        for (STableRow row : moPaneGridEntries.getGridRows()) {
                            SDataRecordEntry entry = (SDataRecordEntry) row.getData();

                            if (!entry.getIsDeleted() && !entry.getIsExchangeDifference()) {
                                if (entry.getFkCurrencyId() != moRecord.getDbmsDataAccountCash().getFkCurrencyId()) {
                                    if (miClient.showMsgBoxConfirm("Al menos una de las monedas de las partidas no coincide con " +
                                            "la moneda de la cuenta de efectivo de la póliza contable (" + currency + ").\n¿Desea continuar?") != JOptionPane.YES_OPTION) {
                                        validation.setMessage("Se deben capturar todas las partidas con la moneda '" + currency + "'.");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        return validation;
    }

    @Override
    public void setFormStatus(int status) {
        mnFormStatus = status;
    }

    @Override
    public void setFormVisible(boolean visible) {
        setVisible(visible);
    }

    @Override
    public int getFormStatus() {
        return mnFormStatus;
    }

    @Override
    public int getFormResult() {
        return mnFormResult;
    }

    @Override
    public void setRegistry(erp.lib.data.SDataRegistry registry) {
        moRecord = (SDataRecord) registry;
        moRecordType = (SDataRecordType) SDataUtilities.readRegistry(miClient, SDataConstants.FINU_TP_REC, new Object[] { moRecord.getPkRecordTypeId() }, SLibConstants.EXEC_MODE_VERBOSE);
        moAccountCash = moRecord.getDbmsDataAccountCash();

        SFormUtilities.populateComboBox(miClient, jcbFkAccountCashId_n, SDataConstants.FIN_ACC_CASH, new int[] { moRecord.getFkCompanyBranchId_n() });

        moFieldDate.setFieldValue(moRecord.getDate());
        moFieldConcept.setFieldValue(moRecord.getConcept());
        moFieldIsAdjustmentYearEnd.setFieldValue(moRecord.getIsAdjustmentsYearEnd());
        moFieldIsAdjustmentAudit.setFieldValue(moRecord.getIsAdjustmentsAudit());
        moFieldFkAccountCashId_n.setFieldValue(new int[] { moRecord.getFkCompanyBranchId_n(), moRecord.getFkAccountCashId_n() });
        jtfPkNumberId.setText(moRecord.getPkRecordTypeId() + "-" + moRecord.getPkNumberId());
        //jtfPkNumberId.setText(moRecord.getPkRecordTypeId() + "-" + miClient.getSessionXXX().getFormatters().getRecordNumberFormat().format(moRecord.getPkNumberId()));
        jckIsAudited.setSelected(moRecord.getIsAudited());
        jckIsAuthorized.setSelected(moRecord.getIsAuthorized());
        jckIsSystem.setSelected(moRecord.getIsSystem());
        moFieldIsDeleted.setFieldValue(moRecord.getIsDeleted());

        mbOldIsDeleted = moRecord.getIsDeleted();

        for (SDataRecordEntry entry : moRecord.getDbmsRecordEntries()) {
            moPaneGridEntries.addTableRow(new SDataRecordEntryRow(entry));
        }
        renderRecordEntries(false);
        moPaneGridEntries.setTableRowSelection(0);

        if (mbParamReadOnly || moRecord.getIsSystem() || moRecord.getIsAudited() || moRecord.getIsAuthorized() || !SDataUtilities.isPeriodOpen(miClient, moRecord.getDate())) {
            mbNonEditable = true;
        }

        if (mbNonEditable) {
            jftDate.setEditable(false);
            jftDate.setFocusable(false);
            jtfConcept.setEditable(false);
            jtfConcept.setFocusable(false);
            jcbFkAccountCashId_n.setEnabled(false);
            jbAccountCashEdit.setEnabled(false);
            
            jbDate.setEnabled(false);
            jbAddXml.setEnabled(false);
            jbRecordToRecordEntryXml.setEnabled(false);
            //jbGetXml.setEnabled(...); // keep enabled
            jbEntryNew.setEnabled(false);
            jbEntryNewInsert.setEnabled(false);
            jbEntryNewCopy.setEnabled(false);
            jbEntryEdit.setEnabled(false);
            jbEntryDelete.setEnabled(false);
            jbEntryMoveDown.setEnabled(false);
            jbEntryMoveUp.setEnabled(false);
            //jbEntryViewSum.setEnabled(...); // keep enabled
            //jbEntryGetXml.setEnabled(...); // keep enabled
            //jbEntryShowDirectCfd.setEnabled(...); // keep enabled
            //jbEntryShowIndirectCfd.setEnabled(...); // keep enabled
            //jbImportXlsx.setEnabled(...); // keep enabled

            jbMoneyIn.setEnabled(false);
            jbMoneyInOther.setEnabled(false);
            jbMoneyInPaymentCus.setEnabled(false);
            jbMiPaymentCus.setEnabled(false);
            jbMiAdvanceCus.setEnabled(false);
            jbMoAdvanceCusDev.setEnabled(false);
            jbMiCreditCdr.setEnabled(false);
            jbMiCreditDbr.setEnabled(false);
            jbMiExchangeRateDiff.setEnabled(false);

            jbMoneyOut.setEnabled(false);
            jbMoneyOutOther.setEnabled(false);
            jbMoneyOutPaymentSup.setEnabled(false);
            jbMoneyOutCheck.setEnabled(false);
            jbMoneyOutTransfer.setEnabled(false);
            jbMoPaymentSup.setEnabled(false);
            jbMoAdvanceSup.setEnabled(false);
            jbMiAdvanceSupDev.setEnabled(false);
            jbMoDebitCdr.setEnabled(false);
            jbMoDebitDbr.setEnabled(false);
            jbMoExchangeRateDiff.setEnabled(false);

            jbDbtPaymentSup.setEnabled(false);
            jbDbtAdvanceSup.setEnabled(false);
            jbDbtAdvanceCusDev.setEnabled(false);
            jbDbtDebitCdr.setEnabled(false);
            jbDbtDebitDbr.setEnabled(false);
            jbDbtExchangeRateDiff.setEnabled(false);

            jbCdtPaymentCus.setEnabled(false);
            jbCdtAdvanceCus.setEnabled(false);
            jbCdtAdvanceSupDev.setEnabled(false);
            jbCdtCreditCdr.setEnabled(false);
            jbCdtCreditDbr.setEnabled(false);
            jbCdtExchangeRateDiff.setEnabled(false);

            jckEnableTempFileData.setEnabled(false);
            jbOk.setEnabled(false);

            jcbGuiModeConcept.setEnabled(false);
            
            jckIsDeleted.setEnabled(false);
        }

        if (moAccountCash == null) {
            jpCommands.add(jpCommandsJournal, BorderLayout.CENTER);
            jlGuiModeInput.setEnabled(true);
            jrbModeInputSimple.setEnabled(true);
            jrbModeInputMultiple.setEnabled(true);
            bgModeInput.setSelected(jrbModeInputSimple.getModel(), true);
            
            jbDbtExchangeRateDiff.setEnabled(false); // action reserved to cash-account journal vouchers
            jbCdtExchangeRateDiff.setEnabled(false); // action reserved to cash-account journal vouchers
        }
        else {
            jpCommands.add(jpCommandsCashAccount, BorderLayout.CENTER);
            jlGuiModeInput.setEnabled(!mbNonEditable);
            jrbModeInputSimple.setEnabled(!mbNonEditable);
            jrbModeInputMultiple.setEnabled(!mbNonEditable);
            bgModeInput.setSelected(jrbModeInputSimple.getModel(), !mbNonEditable);
            
            boolean enableXrtDiff = !mbNonEditable && !miClient.getSession().getSessionCustom().isLocalCurrency(new int[] { moAccountCash.getFkCurrencyId() });
            jbMiExchangeRateDiff.setEnabled(enableXrtDiff);
            jbMoExchangeRateDiff.setEnabled(enableXrtDiff);
            
            boolean enableCheck = !mbNonEditable && moAccountCash.getIsCheckWalletApplying();
            jbMoneyOutCheck.setEnabled(enableCheck);
            
            jbAccountCashEdit.setEnabled(!mbNonEditable);
        }

        if (moRecord.getPkRecordTypeId().equals(SDataConstantsSys.FINU_TP_REC_JOURNAL)) {
            jckIsAdjustmentYearEnd.setEnabled(!mbNonEditable);
            jckIsAdjustmentAudit.setEnabled(!mbNonEditable);
        }
        else {
            jckIsAdjustmentYearEnd.setEnabled(false);
            jckIsAdjustmentAudit.setEnabled(false);
        }
        
        if (moRecord.getDbmsDataCfds().size() > 0) {
            for (SDataCfd cfd : moRecord.getDbmsDataCfds()) {
                maCfdRecordRows.add(new SDataCfdRecordRow(maCfdRecordRows.size() + 1, cfd.getPkCfdId(), cfd.getDocXmlName(), ""));
            }
            updateFilesXmlInfo();
        }

        try {
            renderHeader();
            calculateBalance();
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (moRecord == null) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FORM_EDIT_ONLY);
        }
        else {
            try {
                updateRecord();
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
        
        // obtain XML to delete:
        
        if (!mbIsXmlTranfer) {
            for (SDataCfd cfdAux : moRecord.getDbmsDataCfds()) {
                if (maCfdRecordRows.isEmpty()) {
                    moRecord.getAuxDataCfdToDel().add(cfdAux);
                }
                else {
                    boolean found = false;
                    for (SDataCfdRecordRow row : maCfdRecordRows) {
                        if (SLibUtilities.compareKeys(new int[] { row.getCfdId() }, new int[] { cfdAux.getPkCfdId() })) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        moRecord.getAuxDataCfdToDel().add(cfdAux);
                    }
                }
            }
        }
        
        // process added XML files of CFDI:
        
        ArrayList<SDataCfd> cfds = new ArrayList<>();
        
        for (SDataCfdRecordRow row : maCfdRecordRows) {
            SDataCfd cfd = null;
            
            boolean found = false;
            for (SDataCfd cfdAux : moRecord.getDbmsDataCfds()) {
                if (SLibUtilities.compareKeys(new int[] { row.getCfdId() }, new int[] { cfdAux.getPkCfdId() })) {
                    cfd = cfdAux;
                    found = true;
                    break;
                }
            }
            if (!found) {
                if (row.getCfdId() != 0 && !row.getNameXml().isEmpty()) {
                    cfd = (SDataCfd) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_CFD, new int[] { row.getCfdId() }, SLibConstants.EXEC_MODE_SILENT);
                }
            }
            
            if (!row.getNameXml().isEmpty()) {
                try {
                    if (cfd == null) {
                        String xml;
                        xml = SXmlUtils.readXml(row.getPathXml());
                        
                        cfd = new SDataCfd();
                        cfd.setCertNumber("");
                        cfd.setStringSigned("");
                        cfd.setSignature("");
                        cfd.setDocXml(xml);
                        cfd.setDocXmlName(row.getNameXml());
                        cfd.setIsConsistent(true);
                        cfd.setFkCfdTypeId(SDataConstantsSys.TRNS_TP_CFD_INV);
                        cfd.setFkXmlTypeId(SDataConstantsSys.TRNS_TP_XML_NA);
                        cfd.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_EMITED);
                        cfd.setFkXmlDeliveryTypeId(SModSysConsts.TRNS_TP_XML_DVY_NA);
                        cfd.setFkXmlDeliveryStatusId(SModSysConsts.TRNS_ST_XML_DVY_PENDING);
                        cfd.setFkUserProcessingId(miClient.getSession().getUser().getPkUserId());
                        cfd.setFkUserDeliveryId(miClient.getSession().getUser().getPkUserId());
                    }
                }
                catch (FileNotFoundException e) {
                    SLibUtilities.renderException(this, e);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
            else {
                try {
                    if (row.getNameXml().length() == 0 && cfd != null) {
                        cfd.setDocXml("");
                        cfd.setDocXmlName("");
                        cfd.setIsConsistent(true);
                        cfd.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW);
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
            
            if (cfd != null) {
                cfds.add(cfd);
            }
        }
        
        moRecord.getDbmsDataCfds().clear();
        if (cfds.size() > 0) {
            moRecord.getDbmsDataCfds().addAll(cfds);
        }
        
        moRecord.setDbmsXmlFilesNumber(Integer.parseInt(jtfXmlFilesNumber.getText()));

        return moRecord;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case SLibConstants.VALUE_STATUS:
                mbParamReadOnly = (Boolean) value;
                break;
            default:
        }
    }

    @Override
    public java.lang.Object getValue(int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public javax.swing.JLabel getTimeoutLabel() {
        return null;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        try {
            if (e.getSource() instanceof javax.swing.JButton) {
                JButton button = (JButton) e.getSource();

                if (button == jbOk) {
                    actionPerformedOk();
                }
                else if (button == jbCancel) {
                    actionPerformedCancel();
                }
                else if (button == jbDate) {
                    actionPerformedDate();
                }
                else if (button == jbAddXml) {
                    actionPerformedAddXml();
                }
                else if (button == jbRecordToRecordEntryXml) {
                    actionPerformedRecordToRecordEntryXml();
                }
                else if (button == jbGetXml) {
                    actionPerformedGetXml();
                }
                else if (button == jbAccountCashEdit) {
                    actionPerformedAccountCashEdit();
                }
                else if (button == jbEntryNew) {
                    actionPerformedEntryNew();
                }
                else if (button == jbEntryNewCopy) {
                    actionPerformedEntryNewCopy();
                }
                else if (button == jbEntryNewInsert) {
                    actionPerformedEntryNewInsert();
                }
                else if (button == jbEntryEdit) {
                    actionPerformedEntryEdit();
                }
                else if (button == jbEntryDelete) {
                    actionPerformedEntryDelete();
                }
                else if (button == jbEntryMoveDown) {
                    actionPerformedEntryMoveDown();
                }
                else if (button == jbEntryMoveUp) {
                    actionPerformedEntryMoveUp();
                }
                else if (button == jbEntryViewSum) {
                    actionPerformedEntryViewSum();
                }
                else if (button == jbEntryGetXml) {
                    actionPerformedEntryGetXml();
                }
                else if (button == jbEntryShowDirectCfd) {
                    actionPerformedEntryShowDirectCfd();
                }
                else if (button == jbEntryShowIndirectCfd) {
                    actionPerformedEntryShowIndirectCfd();
                }
                else if (button == jbImportFile) {
                    actionPerfomedImportFile();
                }
                else if (button == jbDownFile) {
                    actionPerfomedDownFile();
                }
                else if (button == jbApportionment) {
                    actionApportionment();
                }
                else if (button == jbMoneyIn) {
                    actionPerformedMoneyInOut(moRecord.getDbmsDataAccountCash(), true);
                }
                else if (button == jbMoneyInOther) {
                    actionPerformedMoneyInOut(null, true);
                }
                else if (button == jbMoneyInPaymentCus) {
                    actionPerformedPaymentComplete(SDataConstants.BPSX_BP_CUS);
                }
                else if (button == jbMiPaymentCus || button == jbCdtPaymentCus) {
                    actionPerformedPayment(SDataConstants.BPSX_BP_CUS);
                }
                else if (button == jbMiAdvanceCus || button == jbCdtAdvanceCus) {
                    actionPerformedMoneyInOutBizPartner(SDataConstants.BPSX_BP_CUS, SDataConstantsSys.FINS_CLS_ACC_MOV_CASH_IN_CUS_ADV);
                }
                else if (button == jbMiAdvanceSupDev || button == jbCdtAdvanceSupDev) {
                    actionPerformedMoneyInOutBizPartner(SDataConstants.BPSX_BP_SUP, SDataConstantsSys.FINS_CLS_ACC_MOV_CASH_IN_SUP_ADV_REF);
                }
                else if (button == jbMiCreditCdr || button == jbCdtCreditCdr) {
                    actionPerformedMoneyInOutBizPartner(SDataConstants.BPSX_BP_CDR, SDataConstantsSys.FINS_CLS_ACC_MOV_CASH_IN_EXT_CDR);
                }
                else if (button == jbMiCreditDbr || button == jbCdtCreditDbr) {
                    actionPerformedMoneyInOutBizPartner(SDataConstants.BPSX_BP_DBR, SDataConstantsSys.FINS_CLS_ACC_MOV_CASH_IN_EXT_DBR);
                }
                else if (button == jbMiExchangeRateDiff || button == jbCdtExchangeRateDiff) {
                    actionPerformedExchangeRateDiff(true);
                }
                else if (button == jbMoneyOut) {
                    actionPerformedMoneyInOut(moRecord.getDbmsDataAccountCash(), false);
                }
                else if (button == jbMoneyOutOther) {
                    actionPerformedMoneyInOut(null, false);
                }
                else if (button == jbMoneyOutPaymentSup) {
                    actionPerformedPaymentComplete(SDataConstants.BPSX_BP_SUP);
                }
                else if (button == jbMoneyOutCheck) {
                    actionPerformedCheck();
                }
                else if (button == jbMoneyOutTransfer) {
                    actionPerformedTransfer();
                }
                else if (button == jbMoPaymentSup || button == jbDbtPaymentSup) {
                    actionPerformedPayment(SDataConstants.BPSX_BP_SUP);
                }
                else if (button == jbMoAdvanceSup || button == jbDbtAdvanceSup) {
                    actionPerformedMoneyInOutBizPartner(SDataConstants.BPSX_BP_SUP, SDataConstantsSys.FINS_CLS_ACC_MOV_CASH_OUT_SUP_ADV);
                }
                else if (button == jbMoAdvanceCusDev || button == jbDbtAdvanceCusDev) {
                    actionPerformedMoneyInOutBizPartner(SDataConstants.BPSX_BP_CUS, SDataConstantsSys.FINS_CLS_ACC_MOV_CASH_OUT_CUS_ADV_REF);
                }
                else if (button == jbMoDebitCdr || button == jbDbtDebitCdr) {
                    actionPerformedMoneyInOutBizPartner(SDataConstants.BPSX_BP_CDR, SDataConstantsSys.FINS_CLS_ACC_MOV_CASH_OUT_EXT_CDR);
                }
                else if (button == jbMoDebitDbr || button == jbDbtDebitDbr) {
                    actionPerformedMoneyInOutBizPartner(SDataConstants.BPSX_BP_DBR, SDataConstantsSys.FINS_CLS_ACC_MOV_CASH_OUT_EXT_DBR);
                }
                else if (button == jbMoExchangeRateDiff || button == jbDbtExchangeRateDiff) {
                    actionPerformedExchangeRateDiff(false);
                }
            }
            else if (e.getSource() instanceof javax.swing.JToggleButton) {
                JToggleButton toggleButton = (JToggleButton) e.getSource();

                if (toggleButton == jtbEntryDeletedFilter) {
                    actionPerformedEntryDeletedFilter();
                }
            }
        }
        catch (Exception exception) {
            SLibUtilities.renderException(this, exception);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JComboBox comboBox = (JComboBox) e.getSource();

                if (comboBox == jcbFkAccountCashId_n) {
                    itemStateChangedAccountCashId_n();
                }
            }
        }
        else if (e.getSource() instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            
            if (checkBox == jckEnableTempFileData) {
                itemStateChangedEnableTempFileData();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            SDataRecordEntryRow row = (SDataRecordEntryRow) moPaneGridEntries.getSelectedTableRow();

            if (row != null) {
                SDataRecordEntry entry = (SDataRecordEntry) row.getData();

                jbEntryNewCopy.setEnabled(!jckIsSystem.isSelected() && !entry.getIsSystem() && !mbNonEditable);
                jbEntryEdit.setEnabled(!jckIsSystem.isSelected() && !entry.getIsSystem() && !mbNonEditable);
                jbEntryDelete.setEnabled(!jckIsSystem.isSelected() && (!entry.getIsSystem() || entry.getUserId() != 0) && !mbNonEditable);
            }
        }
    }
}
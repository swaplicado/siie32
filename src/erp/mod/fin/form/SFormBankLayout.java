/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.form;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.table.STableCellEditorOptions;
import erp.lib.table.STableConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchBankAccount;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SFinUtilities;
import erp.mfin.form.SDialogRecordPicker;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SDbBankLayout;
import erp.mod.fin.db.SFinConsts;
import erp.mod.fin.db.SLayoutBankDps;
import erp.mod.fin.db.SLayoutBankPayment;
import erp.mod.fin.db.SLayoutBankPaymentRow;
import erp.mod.fin.db.SLayoutBankRecord;
import erp.mod.fin.db.SLayoutBankRecordKey;
import erp.mod.fin.db.SLayoutBankRow;
import erp.mod.fin.db.SLayoutBankXmlRow;
import erp.mod.fin.db.SMoney;
import erp.mod.fin.db.SXmlBankLayout;
import erp.mod.fin.db.SXmlBankLayoutPayment;
import erp.mod.fin.db.SXmlBankLayoutPaymentDoc;
import erp.mtrn.data.SDataDps;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableRowSorter;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiItem;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFieldKey;
import sa.lib.gui.bean.SBeanForm;
import sa.lib.srv.SSrvLock;
import sa.lib.srv.SSrvUtils;
import sa.lib.xml.SXmlElement;

/**
 *
 * @author Juan Barajas, Uriel Castañeda, Alfredo Pérez, Sergio Flores
 */
public class SFormBankLayout extends SBeanForm implements ActionListener, ItemListener, CellEditorListener {

    private static final int COL_APP_PAY = 2;               // apply payment, used acounting and prepayments
    private static final int COL_BANK_ACC_OR_AGREE = 4;
    private static final int COL_APP = 5;
    private static final int COL_BAL = 7;
    private static final int COL_EXC_RATE = 9;
    private static final int COL_APP_PAY_BANK = 10;
    private static final int COL_AGREE = 12;
    private static final int COL_AGREE_REF = 13;
    
    private int mnLayoutBank;
    private int mnBankLayoutType;
    private int mnBankPaymentType;
    private int mnBizPartnerBank;
    private int mnBankCurrency;
    private int mnDpsCurrency;
    private int mnNumberDocs;
    private int mnNumberRecordDistint;
    private java.lang.String msDebitFiscalId;
    private java.lang.String msAccountCredit;
    private java.lang.String msAgreement;
    private java.lang.String msAgreementReference;
    private java.lang.String msConceptCie;
    private ArrayList<SGuiItem> maAccountCredit;
    private ArrayList<SGuiItem> maAgreements;
    private ArrayList<SGuiItem> maAgreementsReference;
    private ArrayList<SDataBizPartnerBranchBankAccount> maBranchBankAccountsCredit;
    private List<ArrayList<SGuiItem>> mltAccountCredit;
    private List<ArrayList<SGuiItem>> mltAgreement;
    private List<ArrayList<SGuiItem>> mltAgreementReference;
    private ArrayList<SLayoutBankXmlRow> maXmlRows = null;

    private erp.lib.table.STableCellEditorOptions moTableCellEditorOptions;
    private erp.lib.table.STableCellEditorOptions moTableCellAgreementReference;

    private Vector<SLayoutBankRow> mvLayoutRows;
    private ArrayList<SGuiItem> maBankLayoutTypes;
    private JButton jbExchangeRateReset;
    private JButton jbExchangeRateRefresh;
    private JButton jbGridRowsCheckAll;
    private JButton jbGridRowsUncheckAll;
    private JCheckBox jckShowOnlyDocsDateDue;
    private JCheckBox jckShowOnlyBenefsWithAccounts;

    private SDbBankLayout moRegistry;
    private erp.mbps.data.SDataBizPartner moDataBizPartner;
    private erp.mbps.data.SDataBizPartnerBranchBankAccount moDataBizPartnerBranchBankAccount;
    private erp.mfin.data.SDataAccountCash moDataAccountCash;
    private erp.mfin.form.SDialogRecordPicker moDialogRecordPicker;
    private erp.mfin.data.SDataRecord moCurrentRecord;
    private ArrayList<SLayoutBankRecord> maLayoutBankRecords;
    private SGridPaneForm moGridPayments;
    
    private HashMap<Integer, Object> moParamsMap;
    private HashMap<String, ArrayList<SGuiItem>> mhAgreeAgreeRef;
    private ArrayList<SSrvLock> maLocks;

    /**
     * Creates new form SFormLayoutBank
     * @param client
     * @param formSubtype
     * @param title
     */
    public SFormBankLayout(SGuiClient client, int formSubtype, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.FIN_LAY_BANK, formSubtype, title);
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

        jPanel13 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlDateLayout = new javax.swing.JLabel();
        moDateDateLayout = new sa.lib.gui.bean.SBeanFieldDate();
        jlDummy = new javax.swing.JLabel();
        jlNumber = new javax.swing.JLabel();
        jtfNumber = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jlLayoutBank = new javax.swing.JLabel();
        moKeyLayoutBank = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel6 = new javax.swing.JPanel();
        jlBankLayoutType = new javax.swing.JLabel();
        moKeyBankLayoutType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel8 = new javax.swing.JPanel();
        jlBankCurrency = new javax.swing.JLabel();
        moKeyBankCurrency = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel9 = new javax.swing.JPanel();
        jlBankAccountCash = new javax.swing.JLabel();
        moKeyBankAccountCash = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jlRecord = new javax.swing.JLabel();
        jtfRecordDate = new javax.swing.JTextField();
        jtfRecordCompanyBranch = new javax.swing.JTextField();
        jtfRecordBookkeepingCenter = new javax.swing.JTextField();
        jtfRecordNumber = new javax.swing.JTextField();
        jbPickRecord = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jlConsecutive = new javax.swing.JLabel();
        moIntConsecutive = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel16 = new javax.swing.JPanel();
        jlConcept = new javax.swing.JLabel();
        moTextConcept = new sa.lib.gui.bean.SBeanFieldText();
        jPanel18 = new javax.swing.JPanel();
        jlDpsCurrency = new javax.swing.JLabel();
        moKeyDpsCurrency = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel20 = new javax.swing.JPanel();
        jlExchangeRate = new javax.swing.JLabel();
        moDecExchangeRate = new sa.lib.gui.bean.SBeanFieldDecimal();
        jbPickExchangeRate = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jlDateDue = new javax.swing.JLabel();
        moDateDateDue = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel22 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jbGridRowsShow = new javax.swing.JButton();
        jbGridRowsClear = new javax.swing.JButton();
        jpSettings = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jtfRows = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jlBalanceTot = new javax.swing.JLabel();
        moDecBalanceTot = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlBalanceTotPay = new javax.swing.JLabel();
        moDecBalanceTotPay = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel17 = new javax.swing.JPanel();
        jlLayoutPath = new javax.swing.JLabel();
        jtfLayoutPath = new javax.swing.JTextField();
        jbPickLayoutPath = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel13.setLayout(new java.awt.GridLayout(1, 2, 5, 0));

        jPanel1.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateLayout.setText("Fecha apliación:");
        jlDateLayout.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel4.add(jlDateLayout);
        jPanel4.add(moDateDateLayout);

        jlDummy.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlDummy);

        jlNumber.setText("Folio: ");
        jlNumber.setToolTipText("");
        jlNumber.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel4.add(jlNumber);

        jtfNumber.setEditable(false);
        jtfNumber.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfNumber.setText("0");
        jtfNumber.setFocusable(false);
        jtfNumber.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jtfNumber);

        jPanel1.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLayoutBank.setText("Layout bancario:*");
        jlLayoutBank.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel5.add(jlLayoutBank);

        moKeyLayoutBank.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel5.add(moKeyLayoutBank);

        jPanel1.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBankLayoutType.setText("Tipo layout bancario:*");
        jlBankLayoutType.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel6.add(jlBankLayoutType);

        moKeyBankLayoutType.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel6.add(moKeyBankLayoutType);

        jPanel1.add(jPanel6);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBankCurrency.setText("Moneda origen:*");
        jlBankCurrency.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel8.add(jlBankCurrency);

        moKeyBankCurrency.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel8.add(moKeyBankCurrency);

        jPanel1.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBankAccountCash.setText("Cuenta bancaria:*");
        jlBankAccountCash.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jlBankAccountCash);

        moKeyBankAccountCash.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel9.add(moKeyBankAccountCash);

        jPanel1.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel1.add(jPanel10);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRecord.setText("Póliza contable:");
        jlRecord.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel11.add(jlRecord);

        jtfRecordDate.setEditable(false);
        jtfRecordDate.setText("01/01/2000");
        jtfRecordDate.setToolTipText("Fecha de la póliza contable");
        jtfRecordDate.setFocusable(false);
        jtfRecordDate.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel11.add(jtfRecordDate);

        jtfRecordCompanyBranch.setEditable(false);
        jtfRecordCompanyBranch.setText("BRA");
        jtfRecordCompanyBranch.setToolTipText("Sucursal de la empresa");
        jtfRecordCompanyBranch.setFocusable(false);
        jtfRecordCompanyBranch.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel11.add(jtfRecordCompanyBranch);

        jtfRecordBookkeepingCenter.setEditable(false);
        jtfRecordBookkeepingCenter.setText("BKC");
        jtfRecordBookkeepingCenter.setToolTipText("Centro contable");
        jtfRecordBookkeepingCenter.setFocusable(false);
        jtfRecordBookkeepingCenter.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel11.add(jtfRecordBookkeepingCenter);

        jtfRecordNumber.setEditable(false);
        jtfRecordNumber.setText("TP-000001");
        jtfRecordNumber.setToolTipText("Número de póliza contable");
        jtfRecordNumber.setFocusable(false);
        jtfRecordNumber.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel11.add(jtfRecordNumber);

        jbPickRecord.setText("...");
        jbPickRecord.setToolTipText("Seleccionar póliza contable");
        jbPickRecord.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jbPickRecord);

        jPanel1.add(jPanel11);

        jPanel13.add(jPanel1);

        jPanel14.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlConsecutive.setText("Consecutivo día:*");
        jlConsecutive.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel7.add(jlConsecutive);
        jPanel7.add(moIntConsecutive);

        jPanel14.add(jPanel7);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlConcept.setText("Concepto/descripción:*");
        jlConcept.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel16.add(jlConcept);

        moTextConcept.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel16.add(moTextConcept);

        jPanel14.add(jPanel16);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDpsCurrency.setText("Moneda documentos:*");
        jlDpsCurrency.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel18.add(jlDpsCurrency);

        moKeyDpsCurrency.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel18.add(moKeyDpsCurrency);

        jPanel14.add(jPanel18);

        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlExchangeRate.setText("Tipo de cambio:");
        jlExchangeRate.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel20.add(jlExchangeRate);
        jPanel20.add(moDecExchangeRate);

        jbPickExchangeRate.setText("...");
        jbPickExchangeRate.setToolTipText("Seleccionar tipo de cambio");
        jbPickExchangeRate.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel20.add(jbPickExchangeRate);

        jPanel14.add(jPanel20);

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateDue.setText("Vencimiento:*");
        jlDateDue.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel19.add(jlDateDue);
        jPanel19.add(moDateDateDue);

        jPanel14.add(jPanel19);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel14.add(jPanel22);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbGridRowsShow.setText("Mostrar renglones");
        jbGridRowsShow.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbGridRowsShow.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel21.add(jbGridRowsShow);

        jbGridRowsClear.setText("Limpiar renglones");
        jbGridRowsClear.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbGridRowsClear.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel21.add(jbGridRowsClear);

        jPanel14.add(jPanel21);

        jPanel13.add(jPanel14);

        getContentPane().add(jPanel13, java.awt.BorderLayout.NORTH);

        jpSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle:"));
        jpSettings.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfRows.setEditable(false);
        jtfRows.setText("000,000/000,000");
        jtfRows.setToolTipText("Renglón actual");
        jtfRows.setFocusable(false);
        jtfRows.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jtfRows);

        jPanel2.add(jPanel12, java.awt.BorderLayout.WEST);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBalanceTot.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBalanceTot.setText("Total layout:");
        jlBalanceTot.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel15.add(jlBalanceTot);

        moDecBalanceTot.setEditable(false);
        jPanel15.add(moDecBalanceTot);

        jlBalanceTotPay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBalanceTotPay.setText("Total pago:");
        jlBalanceTotPay.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel15.add(jlBalanceTotPay);

        moDecBalanceTotPay.setEditable(false);
        jPanel15.add(moDecBalanceTotPay);

        jPanel2.add(jPanel15, java.awt.BorderLayout.EAST);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLayoutPath.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlLayoutPath.setText("Ruta layout:*");
        jlLayoutPath.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel17.add(jlLayoutPath);

        jtfLayoutPath.setEditable(false);
        jtfLayoutPath.setFocusable(false);
        jtfLayoutPath.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel17.add(jtfLayoutPath);

        jbPickLayoutPath.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif"))); // NOI18N
        jbPickLayoutPath.setToolTipText("Seleccionar ruta de archivo layout...");
        jbPickLayoutPath.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel17.add(jbPickLayoutPath);

        jPanel2.add(jPanel17, java.awt.BorderLayout.CENTER);

        jpSettings.add(jPanel2, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jpSettings, java.awt.BorderLayout.CENTER);

        getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        actionCancel();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbGridRowsClear;
    private javax.swing.JButton jbGridRowsShow;
    private javax.swing.JButton jbPickExchangeRate;
    private javax.swing.JButton jbPickLayoutPath;
    private javax.swing.JButton jbPickRecord;
    private javax.swing.JLabel jlBalanceTot;
    private javax.swing.JLabel jlBalanceTotPay;
    private javax.swing.JLabel jlBankAccountCash;
    private javax.swing.JLabel jlBankCurrency;
    private javax.swing.JLabel jlBankLayoutType;
    private javax.swing.JLabel jlConcept;
    private javax.swing.JLabel jlConsecutive;
    private javax.swing.JLabel jlDateDue;
    private javax.swing.JLabel jlDateLayout;
    private javax.swing.JLabel jlDpsCurrency;
    private javax.swing.JLabel jlDummy;
    private javax.swing.JLabel jlExchangeRate;
    private javax.swing.JLabel jlLayoutBank;
    private javax.swing.JLabel jlLayoutPath;
    private javax.swing.JLabel jlNumber;
    private javax.swing.JLabel jlRecord;
    private javax.swing.JPanel jpSettings;
    private javax.swing.JTextField jtfLayoutPath;
    private javax.swing.JTextField jtfNumber;
    private javax.swing.JTextField jtfRecordBookkeepingCenter;
    private javax.swing.JTextField jtfRecordCompanyBranch;
    private javax.swing.JTextField jtfRecordDate;
    private javax.swing.JTextField jtfRecordNumber;
    private javax.swing.JTextField jtfRows;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateDue;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateLayout;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecBalanceTot;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecBalanceTotPay;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecExchangeRate;
    private sa.lib.gui.bean.SBeanFieldInteger moIntConsecutive;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBankAccountCash;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBankCurrency;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBankLayoutType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyDpsCurrency;
    private sa.lib.gui.bean.SBeanFieldKey moKeyLayoutBank;
    private sa.lib.gui.bean.SBeanFieldText moTextConcept;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 640);

        moDialogRecordPicker = new SDialogRecordPicker((SClientInterface) miClient, SDataConstants.FINX_REC_USER);
        
        moDateDateLayout.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateLayout), true);
        moKeyLayoutBank.setKeySettings(miClient, SGuiUtils.getLabelName(jlLayoutBank), true);
        moKeyBankLayoutType.setKeySettings(miClient, SGuiUtils.getLabelName(jlBankLayoutType), true);
        moKeyBankCurrency.setKeySettings(miClient, SGuiUtils.getLabelName(jlBankCurrency), true);
        moKeyBankAccountCash.setKeySettings(miClient, SGuiUtils.getLabelName(jlBankAccountCash), true);
        moIntConsecutive.setIntegerSettings(SGuiUtils.getLabelName(jlConsecutive), SGuiConsts.GUI_TYPE_INT, true);
        moTextConcept.setTextSettings(SGuiUtils.getLabelName(jlConcept), 255);
        moKeyDpsCurrency.setKeySettings(miClient, SGuiUtils.getLabelName(jlDpsCurrency), true);
        moDecExchangeRate.setDecimalSettings(SGuiUtils.getLabelName(jlExchangeRate), SGuiConsts.GUI_TYPE_DEC_EXC_RATE, true);
        moDateDateDue.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateDue), true);
        moDecBalanceTot.setDecimalSettings(SGuiUtils.getLabelName(jlBalanceTot), SGuiConsts.GUI_TYPE_DEC_AMT, false);
        moDecBalanceTotPay.setDecimalSettings(SGuiUtils.getLabelName(jlBalanceTotPay), SGuiConsts.GUI_TYPE_DEC_AMT, false);
        
        moFields.addField(moDateDateLayout);
        moFields.addField(moKeyLayoutBank);
        moFields.addField(moKeyBankLayoutType);
        moFields.addField(moKeyBankCurrency);
        moFields.addField(moKeyBankAccountCash);
        moFields.addField(moIntConsecutive);
        moFields.addField(moTextConcept);
        moFields.addField(moKeyDpsCurrency);
        moFields.addField(moDecExchangeRate);
        moFields.addField(moDateDateDue);
        moFields.setFormButton(jbGridRowsShow);

        moTableCellEditorOptions = new STableCellEditorOptions((SClientInterface) miClient);
        moTableCellAgreementReference = new STableCellEditorOptions((SClientInterface) miClient, true);

        jbExchangeRateReset = new JButton("TC original");
        jbExchangeRateReset.setToolTipText("Poner TC original del documento");
        jbExchangeRateReset.setMargin(new Insets (2, 2, 2, 2));
        jbExchangeRateReset.setPreferredSize(new java.awt.Dimension(85, 23));
        
        jbExchangeRateRefresh = new JButton("TC actual");
        jbExchangeRateRefresh.setToolTipText("Poner TC actual");
        jbExchangeRateRefresh.setMargin(new Insets (2, 2, 2, 2));
        jbExchangeRateRefresh.setPreferredSize(new java.awt.Dimension(85, 23));

        jbGridRowsCheckAll = new JButton("Todo");
        jbGridRowsCheckAll.setToolTipText("Seleccionar todo");
        jbGridRowsCheckAll.setPreferredSize(new java.awt.Dimension(85, 23));

        jbGridRowsUncheckAll = new JButton("Nada");
        jbGridRowsUncheckAll.setToolTipText("Selecionar nada");
        jbGridRowsUncheckAll.setPreferredSize(new java.awt.Dimension(85, 23));
        
        jckShowOnlyDocsDateDue = new JCheckBox("Solo documentos del vecimiento",true);
        jckShowOnlyDocsDateDue.setPreferredSize(new Dimension(200, 23));
        jckShowOnlyBenefsWithAccounts = new JCheckBox("Solo cuentas bancarias configuradas");
        jckShowOnlyBenefsWithAccounts.setPreferredSize(new Dimension(200, 23));
        
        switch (mnFormSubtype) {
            case SModSysConsts.FIN_LAY_BANK_ACC:
                // custom text of buttons fits well
                break;

            case SModSysConsts.FIN_LAY_BANK_PAY:
                jbGridRowsShow.setText("Mostrar documentos");
                jbGridRowsClear.setText("Limpiar documentos");
                break;

            case SModSysConsts.FIN_LAY_BANK_PREPAY:
                jbGridRowsShow.setText("Mostrar beneficiarios");
                jbGridRowsClear.setText("Limpiar beneficiarios");
                break;

            default:
        }
        
        moGridPayments = new SGridPaneForm(miClient, SModConsts.FIN_LAY_BANK, mnFormSubtype, "Detalle") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();
                SGridColumnForm column = null;
                
                switch (mnFormSubtype) {
                    case SModSysConsts.FIN_LAY_BANK_ACC:
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Proveedor",200));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Clave proveedor", 50));
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Aplicar pago", moGridPayments.getTable().getDefaultEditor(Boolean.class));
                        column.setEditable(true);
                        gridColumnsForm.add(column);
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Monto a pagar $"));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda"));
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Cuenta/Convenio", 120);
                        column.setApostropheOnCsvRequired(true);
                        gridColumnsForm.add(column);
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Referencia", 120);
                        column.setApostropheOnCsvRequired(true);
                        gridColumnsForm.add(column);
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Concepto", 120);
                        gridColumnsForm.add(column);
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_PER, "Período póliza"));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CO, "Centro contable"));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CO, "Sucursal empresa"));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Folio póliza", 65));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha póliza"));
                        break;
                        
                    case SModSysConsts.FIN_LAY_BANK_PAY:
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Proveedor",200));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "Tipo doc", 40));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Folio doc", 80));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha doc"));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Sucursal empresa", STableConstants.WIDTH_CODE_COB));
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Aplicar pago", moGridPayments.getTable().getDefaultEditor(Boolean.class));
                        column.setEditable(true);
                        gridColumnsForm.add(column);
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Saldo doc $", STableConstants.WIDTH_VALUE_2X));
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Pago doc $", STableConstants.WIDTH_VALUE_2X);
                        column.setEditable(true);
                        gridColumnsForm.add(column);
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda doc"));
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_EXC_RATE, "TC", 60);
                        column.setEditable(true);
                        gridColumnsForm.add(column);
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Pago cta $", STableConstants.WIDTH_VALUE_2X));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda cta"));
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Cuenta/Convenio", 120, moTableCellEditorOptions);
                        column.setEditable(true);
                        column.setApostropheOnCsvRequired(true);
                        gridColumnsForm.add(column);
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Referencia", 120, moTableCellAgreementReference);
                        column.setEditable(isEditableGridReferenceConcept());
                        column.setApostropheOnCsvRequired(true);
                        gridColumnsForm.add(column);
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Concepto", 120, moGridPayments.getTable().getDefaultEditor(String.class));
                        column.setEditable(isEditableGridReferenceConcept());
                        gridColumnsForm.add(column);
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Mail", 100, moGridPayments.getTable().getDefaultEditor(String.class));
                        column.setEditable(true);
                        gridColumnsForm.add(column);
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "RFC", 100));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Subtotal $"));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Impuesto trasladado $"));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Impuesto retenido $"));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Total doc $"));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha vencimiento"));
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Observaciones", 100, moGridPayments.getTable().getDefaultEditor(String.class));
                        column.setEditable(true);
                        gridColumnsForm.add(column);
                        break;
                        
                    case SModSysConsts.FIN_LAY_BANK_PREPAY:
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Proveedor", 200));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Clave proveedor", 50));
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Monto a pagar $", moGridPayments.getTable().getDefaultEditor(Double.class));
                        column.setEditable(true);
                        gridColumnsForm.add(column);
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda"));
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Cuenta/Convenio", 120, moTableCellEditorOptions);
                        column.setEditable(true);
                        column.setApostropheOnCsvRequired(true);
                        gridColumnsForm.add(column);
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Referencia", 120, moTableCellAgreementReference);
                        column.setEditable(isEditableGridReferenceConcept());
                        column.setApostropheOnCsvRequired(true);
                        gridColumnsForm.add(column);
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Concepto", 120, moGridPayments.getTable().getDefaultEditor(String.class));
                        column.setEditable(isEditableGridReferenceConcept());
                        gridColumnsForm.add(column);
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Mail", 100, moGridPayments.getTable().getDefaultEditor(String.class));
                        column.setEditable(true);
                        gridColumnsForm.add(column);
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "RFC", 100));
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Referencia", 100, moGridPayments.getTable().getDefaultEditor(String.class));
                        column.setEditable(true);
                        gridColumnsForm.add(column);
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Observaciones", 100, moGridPayments.getTable().getDefaultEditor(String.class));
                        column.setEditable(true);
                        gridColumnsForm.add(column);
                        break;
                        
                    default:
                }
                
                moGridPayments.getTable().getDefaultEditor(Double.class).addCellEditorListener(SFormBankLayout.this);
                moGridPayments.getTable().getDefaultEditor(Boolean.class).addCellEditorListener(SFormBankLayout.this);
                moGridPayments.getTable().getDefaultEditor(String.class).addCellEditorListener(SFormBankLayout.this);
                moTableCellEditorOptions.addCellEditorListener(SFormBankLayout.this);
                moTableCellAgreementReference.addCellEditorListener(SFormBankLayout.this);

                return gridColumnsForm;
            }
        };
       
        moGridPayments.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jckShowOnlyDocsDateDue);
        moGridPayments.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jckShowOnlyBenefsWithAccounts);
        moGridPayments.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExchangeRateReset);
        moGridPayments.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExchangeRateRefresh);
        moGridPayments.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGridRowsCheckAll);
        moGridPayments.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGridRowsUncheckAll);
        
        jpSettings.add(moGridPayments, BorderLayout.CENTER);
    }
    
    private boolean validateDebtsToPay() throws Exception {
        boolean found = false;
        int visibleRows = 0;
        ArrayList<SLayoutBankRow> layoutBankRows = new ArrayList<>();
        SLayoutBankRow layoutBankRow = null;
        
        updateLayoutRow();

        for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
            layoutBankRow = (SLayoutBankRow) rowAux;
            visibleRows++;
            
            for (int row = 0; row < mvLayoutRows.size(); row++) {
                if (SLibUtilities.compareKeys(layoutBankRow.getRowPrimaryKey(), mvLayoutRows.get(row).getRowPrimaryKey()) && mvLayoutRows.get(row).getIsForPayment()) {
                    if (mnBankPaymentType == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                        if (mvLayoutRows.get(row).getAgreement().isEmpty()) {
                            throw new Exception("No ha especificado el convenio para uno o más pagos.");
                        }
                        else if (mvLayoutRows.get(row).getAgreementReference().isEmpty()) {
                            throw new Exception("No ha especificado la referencia de convenio para uno o más pagos.");
                        }
                        else {
                            found = false;
                            
                            for (SLayoutBankRow layoutRow : layoutBankRows) {
                                if (SLibUtilities.compareKeys(new int[] { layoutRow.getBizPartnerId(), }, new int[] { mvLayoutRows.get(row).getBizPartnerId() }) &&
                                    layoutRow.getAccountCredit().compareTo(mvLayoutRows.get(row).getAccountCredit()) == 0) {
                                    found = true;
                                    layoutRow.setBalanceTotByBizPartner(layoutRow.getBalanceTotByBizPartner() + mvLayoutRows.get(row).getBalanceTotByBizPartner());
                                    break;
                                }
                            }
                            
                            if (!found) {
                                layoutBankRows.add(mvLayoutRows.get(row));
                            }
                        }
                    }
                    else {
                        if (mvLayoutRows.get(row).getAccountCredit().isEmpty()) {
                            throw new Exception("No ha especificado la referencia de convenio para uno o más pagos.");
                        }
                        else {
                            found = false;
                            
                            for (SLayoutBankRow layoutRow : layoutBankRows) {
                                if (SLibUtilities.compareKeys(new int[] { layoutRow.getBizPartnerId(), }, new int[] { mvLayoutRows.get(row).getBizPartnerId() }) &&
                                    layoutRow.getAccountCredit().compareTo(mvLayoutRows.get(row).getAccountCredit()) == 0) {
                                    found = true;
                                    layoutRow.setBalanceTotByBizPartner(layoutRow.getBalanceTotByBizPartner() + mvLayoutRows.get(row).getBalanceTotByBizPartner());
                                    break;
                                }
                            }
                            
                            if (!found) {
                                layoutBankRows.add(mvLayoutRows.get(row));
                            }
                        }
                    }
                    
                    if (mvLayoutRows.get(row).getBalanceTot().getExchangeRate() == 0) {
                        throw new Exception("No ha especificado el tipo de cambio el renglón : " + visibleRows);
                    }
                    if (SLibUtilities.textTrim(mvLayoutRows.get(row).getObservations()).isEmpty() && mnFormSubtype == SModSysConsts.FIN_LAY_BANK_PREPAY) {
                        throw new Exception("No ha especificado observaciones para el renglón : " + visibleRows);
                    }
                }
            }
        }
        
        // validate "alias" for payment with BanBajio Bank:
        
        if (mnLayoutBank == SFinConsts.LAY_BANK_BANBAJIO) {
            for (SLayoutBankRow layoutRow : layoutBankRows) {
                if (layoutRow.getBajioBankAlias().isEmpty()) {
                    throw new Exception("No se ha especificado el 'alias ' de la cuenta de abono '" + layoutRow.getAccountCredit() + "' del proveedor '" + layoutRow.getBizPartner() + "'.");
                }
            }
        }
        
        if (layoutBankRows.isEmpty()) {
            throw new Exception("No ha especificado ningún renglón para pago.");
        }
        
        return true;
    }
    
    private boolean validateRecord(final boolean isSelectedAll) throws Exception {
        SLayoutBankPaymentRow payRow = null;
        mnNumberRecordDistint = 0;
        
        if (moCurrentRecord == null) {
            jbPickRecord.requestFocus();
            throw new Exception(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlRecord) + "'.");
        }
        else if (isSelectedAll) {
            for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
                payRow = (SLayoutBankPaymentRow) rowAux;
                
                if (payRow.getIsForPayment()) {
                    if (payRow.getLayoutBankRecordKey() != null) {
                        if (!SLibUtils.compareKeys(payRow.getLayoutBankRecordKey().getPrimaryKey(), moCurrentRecord.getPrimaryKey())) {
                            mnNumberRecordDistint++;
                        }
                    }
                }
            }
            
            for (SLayoutBankRecord bankRecordRow : maLayoutBankRecords) {
                if (!SLibUtils.compareKeys(bankRecordRow.getLayoutBankRecordKey().getPrimaryKey(), moCurrentRecord.getPrimaryKey())) {
                    mnNumberRecordDistint++;
                }
            }
            
            if (mnNumberRecordDistint != 0) {
                if (miClient.showMsgBoxConfirm("Existen '" + mnNumberRecordDistint + "' transferencias en pólizas distintas, a las cuáles se la reemplazará la póliza por la '" + moCurrentRecord.getRecordPeriod() + "-" + moCurrentRecord.getRecordNumber() + "'.\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.NO_OPTION) {
                   throw new Exception("Existen '" + mnNumberRecordDistint + "' transferencias en pólizas distintas.");
                }
            }
        }
        return true;
    }
    
    private boolean isEditableConsecutive() {
        return mnLayoutBank == SFinConsts.LAY_BANK_BANBAJIO;
    }
    
    private boolean isEditableConcept() {
        boolean editable = false;
        
        switch (mnLayoutBank) {
            case SFinConsts.LAY_BANK_HSBC:
                editable = mnBankPaymentType == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD;
                break;
            case SFinConsts.LAY_BANK_SANTANDER:
                editable = true;
                break;
            case SFinConsts.LAY_BANK_BANBAJIO:
                break;
            case SFinConsts.LAY_BANK_BBVA:
                editable = false;
                break;
            case SFinConsts.LAY_BANK_BANAMEX:
                break;
            default:
        }

        return editable;
    }
    
    private boolean isEditableGridReferenceConcept() {
        return (mnBankPaymentType == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE);
    }
    
    private boolean isExchangeRateNotRequired() {
        return miClient.getSession().getSessionCustom().isLocalCurrency(moKeyDpsCurrency.getValue()) || SLibUtils.compareKeys(moKeyDpsCurrency.getValue(), moKeyBankCurrency.getValue());
    }
    
    private void enableFieldsForm(boolean enable) {
        switch (mnFormSubtype){
            case SModSysConsts.FIN_LAY_BANK_ACC:
                moDateDateLayout.setEditable(true);
                moKeyLayoutBank.setEnabled(false);
                moKeyBankLayoutType.setEnabled(false);
                moKeyBankCurrency.setEnabled(false);
                moKeyBankAccountCash.setEnabled(false);
                jbPickRecord.setEnabled(true);
                
                moIntConsecutive.setEditable(false);
                moTextConcept.setEditable(false);
                moKeyDpsCurrency.setEnabled(false);
                moDecExchangeRate.setEnabled(false);
                jbPickExchangeRate.setEnabled(false);
                moDateDateDue.setEditable(false);
                
                jbGridRowsShow.setEnabled(false);
                jbGridRowsClear.setEnabled(false);
                break;
                
            case SModSysConsts.FIN_LAY_BANK_PAY:
            case SModSysConsts.FIN_LAY_BANK_PREPAY:
                moDateDateLayout.setEditable(true);
                
                moKeyLayoutBank.setEnabled(enable);
                moKeyBankLayoutType.setEnabled(enable && moKeyLayoutBank.getSelectedIndex() > 0);
                moKeyBankCurrency.setEnabled(enable && moKeyBankLayoutType.getSelectedIndex() > 0);
                moKeyBankAccountCash.setEnabled(enable && moKeyBankCurrency.getSelectedIndex() > 0);
                jbPickRecord.setEnabled(false);
                
                moIntConsecutive.setEditable(enable && isEditableConsecutive());
                moTextConcept.setEditable(enable && isEditableConcept());
                moKeyDpsCurrency.setEnabled(enable && moKeyBankLayoutType.getSelectedIndex() > 0);
                moDecExchangeRate.setEnabled(enable && !isExchangeRateNotRequired());
                jbPickExchangeRate.setEnabled(enable && !isExchangeRateNotRequired());
                moDateDateDue.setEditable(enable);
                
                jbGridRowsShow.setEnabled(enable);
                jbGridRowsClear.setEnabled(!enable);
                break;
                
            default:
        }
    }

    private void enableFieldsGrid(final boolean enableFields, final boolean enableLayoutPathPicker) {
        jckShowOnlyDocsDateDue.setEnabled(enableFields);
        jckShowOnlyBenefsWithAccounts.setEnabled(enableFields);
        jbGridRowsCheckAll.setEnabled(enableFields);
        jbGridRowsUncheckAll.setEnabled(enableFields);
        jbExchangeRateReset.setEnabled(enableFields && !isExchangeRateNotRequired());
        jbExchangeRateRefresh.setEnabled(enableFields && !isExchangeRateNotRequired());
        
        jbPickLayoutPath.setEnabled(enableLayoutPathPicker);
    }

    @SuppressWarnings("unchecked")
    private void renderBankAccountCredit(SLayoutBankRow oRow, int nBizPartnerBranch, int nBankId) {
        String sql = "";
        ResultSet resultSet = null;
        Statement statementAux;
        SDataBizPartnerBranchBankAccount bizPartnerBranchBankAccount = null;
        HashSet<String> oAccountCreditAux;
        HashSet<String> oAgree;
        int idBpb = 0;
        int idBpbBankAcc = 0;
        String accountCredit = "";
        String agreement = "";

        msAccountCredit = "";
        msAgreement = "";
        msAgreementReference = "";
        msConceptCie = "";
        
        try {
            sql = "SELECT b.id_bpb, b.id_bank_acc, b.acc_num, b.acc_num_std, b.agree, b.ref, b.fid_bank, b.b_def, COALESCE(lay.fid_tp_pay_bank, 0) AS f_tp_pay, " +
                        "COALESCE(lay.id_tp_lay_bank, 0) AS f_tp_lay, bp.code_bank_san, bp.code_bank_baj, b.alias_baj " +
                        "FROM erp.bpsu_bank_acc AS b " +
                        "LEFT OUTER JOIN erp.bpsu_bank_acc_lay_bank AS l ON b.id_bpb = l.id_bpb AND b.id_bank_acc = l.id_bank_acc " +
                        "LEFT OUTER JOIN erp.finu_tp_lay_bank AS lay ON l.id_tp_lay_bank = lay.id_tp_lay_bank " +
                        "LEFT OUTER JOIN erp.bpsu_bp AS bp ON bp.id_bp = b.fid_bank " +
                        "WHERE b.b_del = 0 AND b.fid_cur = " + moKeyBankCurrency.getValue()[0] + " AND b.id_bpb = " + nBizPartnerBranch +
                        (mnBankPaymentType == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD || mnBankPaymentType == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE ? " AND b.fid_bank = " + nBankId : " AND b.fid_bank <> " + nBankId) + " AND lay.id_tp_lay_bank = " + mnBankLayoutType + " ";
            
            oAccountCreditAux = new HashSet<>();
            oAgree = new HashSet<>();
            maBranchBankAccountsCredit = new ArrayList<>();
            maAccountCredit = new ArrayList<>();
            maAgreements = new ArrayList<>();
            maAgreementsReference = new ArrayList<>();

            statementAux = miClient.getSession().getStatement().getConnection().createStatement();
            resultSet = statementAux.executeQuery(sql);
           
            if (mnBankPaymentType == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                while (resultSet.next()) {
                    idBpb = resultSet.getInt("b.id_bpb");
                    idBpbBankAcc = resultSet.getInt("b.id_bank_acc");
                    agreement = resultSet.getString("b.agree");
                    
                    msAccountCredit = resultSet.getString("b.acc_num");                    
                    msAgreement = agreement;
                    mhAgreeAgreeRef.put(agreement, erp.mod.fin.util.SFinUtils.getAgreementReferences(miClient, nBizPartnerBranch, agreement));
                    
                    accountCredit = mnBankPaymentType != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? resultSet.getString("b.acc_num_std") : resultSet.getString("b.acc_num");
                    oAgree.add(agreement);
                    oAccountCreditAux.add(agreement);
                    
                    bizPartnerBranchBankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BANK_ACC, new int[] { idBpb, idBpbBankAcc }, SLibConstants.EXEC_MODE_SILENT); 

                    maBranchBankAccountsCredit.add(bizPartnerBranchBankAccount);
                }
                
                maAgreementsReference.add(new SGuiItem(""));
            }
            else {
                while (resultSet.next()) {
                idBpb = resultSet.getInt("b.id_bpb");
                idBpbBankAcc = resultSet.getInt("b.id_bank_acc");                  

                    if (resultSet.getBoolean("b.b_def") && resultSet.getInt("f_tp_pay") == mnBankPaymentType) {
                        msAccountCredit = (mnBankPaymentType != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? resultSet.getString("b.acc_num_std") : resultSet.getString("b.acc_num"));
                    }
                    if (msAccountCredit.length() == 0 && resultSet.getInt("f_tp_pay") == mnBankPaymentType) {
                        msAccountCredit = (mnBankPaymentType != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? resultSet.getString("b.acc_num_std") : resultSet.getString("b.acc_num"));
                    }
                    accountCredit = mnBankPaymentType != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? resultSet.getString("b.acc_num_std") : resultSet.getString("b.acc_num");

                    if (resultSet.getInt("f_tp_pay") == mnBankPaymentType && resultSet.getInt("f_tp_lay") == mnBankLayoutType) {
                        oAccountCreditAux.add(accountCredit);
                    }
                    oRow.getCodeBankAccountCredits().put(mnBankPaymentType != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? resultSet.getString("b.acc_num_std") : resultSet.getString("b.acc_num"),
                                                    mnLayoutBank == SFinConsts.LAY_BANK_BANBAJIO ? resultSet.getString("bp.code_bank_baj") : resultSet.getString("bp.code_bank_san"));
                    oRow.getAliasBankAccountCredits().put(mnBankPaymentType != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? resultSet.getString("b.acc_num_std") : resultSet.getString("b.acc_num"), resultSet.getString("b.alias_baj"));

                    bizPartnerBranchBankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BANK_ACC, new int[] { idBpb, idBpbBankAcc }, SLibConstants.EXEC_MODE_SILENT); 

                    maBranchBankAccountsCredit.add(bizPartnerBranchBankAccount);
                }
            }
            for (String account : oAccountCreditAux) {
                maAccountCredit.add(new SGuiItem(new int[] { idBpb, idBpbBankAcc }, account));
            }
            
            for (String agree : oAgree) {
                maAgreements.add(new SGuiItem(new int[] { idBpb, idBpbBankAcc }, agree));
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    private boolean readRecord(Object key) {
        moCurrentRecord = (SDataRecord) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.FIN_REC, key, SLibConstants.EXEC_MODE_VERBOSE);
        return true;
    }

    private void renderRecord() {
        if (moCurrentRecord == null) {
            jtfRecordDate.setText("");
            jtfRecordBookkeepingCenter.setText("");
            jtfRecordCompanyBranch.setText("");
            jtfRecordNumber.setText("");
        }
        else {
            jtfRecordDate.setText(((SClientInterface) miClient).getSessionXXX().getFormatters().getDateFormat().format(moCurrentRecord.getDate()));
            jtfRecordBookkeepingCenter.setText(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.FIN_BKC, new int[] { moCurrentRecord.getPkBookkeepingCenterId() }, SLibConstants.DESCRIPTION_CODE));
            jtfRecordCompanyBranch.setText(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.BPSU_BPB, new int[] { moCurrentRecord.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
            jtfRecordNumber.setText(moCurrentRecord.getRecordNumber());
        }
    }

    private void renderBizPartner(int bizPartnerId) {
        moDataBizPartner = (SDataBizPartner) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BP, new int[] { bizPartnerId }, SLibConstants.EXEC_MODE_SILENT);

        if (moDataBizPartner != null) {
            msDebitFiscalId = moDataBizPartner.getFiscalId();
        }
    }
    
    /**
     * Puts desired exchange rate into current grid row, if any.
     * @param exchangeRate Desired exchange rate.
     */
    public void putExchangeRate(final double exchangeRate) {
        int index = moGridPayments.getTable().getSelectedRow();
        
        if (index != -1) {
            SLayoutBankRow row = (SLayoutBankRow) moGridPayments.getGridRow(index);
            
            if (row.getBalanceTot().getAmountOriginal() > 0) {
                row.setIsForPayment(true);
                row.setExchangeRate(exchangeRate);
            }
            else {
                row.setIsForPayment(false);
            }
            
            computeBalancePayment();
            moGridPayments.renderGridRows();
            moGridPayments.setSelectedGridRow(index);
        }
    }
    
    private void processLayoutBank() {
        if (moKeyLayoutBank.getSelectedIndex() <= 0) {
            mnLayoutBank = SLibConsts.UNDEFINED;
        }
        else {
            mnLayoutBank = moKeyLayoutBank.getValue()[0];
        }
        
        populateBankLayoutType();
    }
    
    private void processBankLayoutType() {
        if (moKeyBankLayoutType.getSelectedIndex() <= 0) {
            mnBankLayoutType = SLibConsts.UNDEFINED;
            mnBankPaymentType = SLibConsts.UNDEFINED;
            mnBizPartnerBank = SLibConsts.UNDEFINED;
        }
        else {
            mnBankLayoutType = moKeyBankLayoutType.getValue()[0];
            mnBankPaymentType = ((BankLayoutType) ((SGuiItem) moKeyBankLayoutType.getSelectedItem()).getComplement()).BankPaymentType;
            mnBizPartnerBank = ((BankLayoutType) ((SGuiItem) moKeyBankLayoutType.getSelectedItem()).getComplement()).BizPartnerBank;
        }
        
        renderSettingsBankLayoutType();
        populateBankCurrency();
    }
    
    private void processBankCurrency() {
        if (moKeyBankCurrency.getSelectedIndex() <= 0) {
            mnBankCurrency = SLibConsts.UNDEFINED;
            moKeyDpsCurrency.setEnabled(false);
        }
        else {
            mnBankCurrency = moKeyBankCurrency.getValue()[0];
            moKeyDpsCurrency.setEnabled(true);
        }

        moKeyDpsCurrency.setValue(moKeyBankCurrency.getValue());
        
        populateBankAccountCash();
    }
    
    private void processBankAccountCash() {
        renderSettingsBankAccountCash();
    }
    
    private void processDpsCurrency() {
        if (moKeyDpsCurrency.getSelectedIndex() <= 0) {
            mnDpsCurrency = SLibConsts.UNDEFINED;
        }
        else {
            mnDpsCurrency = moKeyDpsCurrency.getValue()[0];
        }
        
        renderSettingsDpsCurrency();
    }
    
    private void loadCatalogueBankLayoutType() {
        String sql = "";
        ResultSet resultSet = null;
        
        try {
           sql = "SELECT id_tp_lay_bank, tp_lay_bank, fid_tp_pay_bank, fid_bank, lay_bank " +
                "FROM erp.finu_tp_lay_bank " +
                "WHERE b_del = 0 " +
                "ORDER BY id_tp_lay_bank ";

            maBankLayoutTypes = new ArrayList<>();
            maBankLayoutTypes.add(new SGuiItem("(" + SUtilConsts.TXT_SELECT + " tipo layout bancario)"));

            resultSet = miClient.getSession().getStatement().executeQuery(sql);
            while (resultSet.next()) {
                BankLayoutType bankLayoutType = new BankLayoutType(resultSet.getInt("id_tp_lay_bank"), resultSet.getInt("fid_tp_pay_bank"), resultSet.getInt("lay_bank"), resultSet.getInt("fid_bank"));
                maBankLayoutTypes.add(new SGuiItem(new int[] { resultSet.getInt("id_tp_lay_bank") }, resultSet.getString("tp_lay_bank"), new int[] { resultSet.getInt("lay_bank") }, bankLayoutType));
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void populateLayoutBank() {
        Vector<SGuiItem> layouts = new Vector<>();

        layouts.add(new SGuiItem("(" + SUtilConsts.TXT_SELECT + " layout bancario)"));
        layouts.add(new SGuiItem(new int[] { SFinConsts.LAY_BANK_HSBC }, SFinConsts.TXT_LAY_BANK_HSBC));
        layouts.add(new SGuiItem(new int[] { SFinConsts.LAY_BANK_SANTANDER }, SFinConsts.TXT_LAY_BANK_SANTANDER));
        layouts.add(new SGuiItem(new int[] { SFinConsts.LAY_BANK_BANBAJIO }, SFinConsts.TXT_LAY_BANK_BANBAJIO));
        layouts.add(new SGuiItem(new int[] { SFinConsts.LAY_BANK_BBVA }, SFinConsts.TXT_LAY_BANK_BBVA));
        layouts.add(new SGuiItem(new int[] { SFinConsts.LAY_BANK_BANAMEX }, SFinConsts.TXT_LAY_BANK_BANAMEX));
        
        moKeyLayoutBank.removeAllItems();
        for (int i = 0; i < layouts.size(); i++) {
            moKeyLayoutBank.addItem(layouts.get(i));
        }
    }
    @SuppressWarnings("unchecked")
    private void populateBankLayoutType() {
        if (moKeyLayoutBank.getSelectedIndex() <= 0) {
            moKeyBankLayoutType.setEnabled(false);
            
            moKeyBankLayoutType.removeAllItems();
        }
        else {
            moKeyBankLayoutType.removeAllItems();
            
            moKeyBankLayoutType.addItem(maBankLayoutTypes.get(0));
            
            // iterate through bank layout types missing first item, because is useless:
            for (int index = 1; index < maBankLayoutTypes.size(); index++) {
                SGuiItem item = maBankLayoutTypes.get(index);
                if (SLibUtils.compareKeys(moKeyLayoutBank.getValue(), new int[] { ((BankLayoutType) item.getComplement()).LayoutBank })) {
                    moKeyBankLayoutType.addItem(item);
                }
            }
            
            moKeyBankLayoutType.setEnabled(true);
        }
    }
    
    private void populateBankCurrency() {
        if (moKeyBankLayoutType.getSelectedIndex() <= 0) {
            moKeyBankCurrency.setEnabled(false);
            
            moKeyBankCurrency.resetField();
        }
        else {
            moKeyBankCurrency.setEnabled(true);
        }
    }
    
    private void populateBankAccountCash() {
        if (moKeyBankCurrency.getSelectedIndex() <= 0) {
            moKeyBankAccountCash.setEnabled(false);
            
            moKeyBankAccountCash.removeAllItems();
        }
        else {
            miClient.getSession().populateCatalogue(moKeyBankAccountCash, SModConsts.FIN_ACC_CASH, SModConsts.FINX_ACC_CASH_BANK, new SGuiParams(new int[] { mnBankCurrency, mnBizPartnerBank }));
            moKeyBankAccountCash.setEnabled(true);
        }
    }
    
    private void renderSettingsBankLayoutType() {
        if (moKeyBankLayoutType.getSelectedIndex() <= 0) {
            moIntConsecutive.setEditable(false);
            moTextConcept.setEditable(false);
        }
        else {
            moIntConsecutive.setEditable(isEditableConsecutive());
            moTextConcept.setEditable(isEditableConcept());
        }
        
        if (!moIntConsecutive.isEditable()) {
            moIntConsecutive.resetField();
        }
        
        if (!moTextConcept.isEditable()) {
            moTextConcept.resetField();
        }
    }

    /**
     * Set the currency for the DPS to search 
     * - if the selected currency is the system local currency set´s the exchange rate to 1
     * - if the selected currency isn´t the local currency search for the excahnge rate of the system for the day otherwise is 0
     */
    private void renderSettingsDpsCurrency(){
        double exr = 0;
        boolean enable = false;
        
        if (moKeyDpsCurrency.getSelectedIndex() <= 0) {
            mnDpsCurrency = SLibConsts.UNDEFINED;
        }
        else {
            mnDpsCurrency = moKeyDpsCurrency.getValue()[0];
            
            // check if documents' currency is local or is equal than bank's currency:
            if (isExchangeRateNotRequired()) {
                exr = 1;
            }
            else {
                try {
                    exr = SDataUtilities.obtainExchangeRate((SClientInterface) miClient, moKeyDpsCurrency.getValue()[0], moDateDateLayout.getValue());
                    enable = true;
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
        
        moDecExchangeRate.setValue(exr);
        moDecExchangeRate.setEditable(enable);
        jbPickExchangeRate.setEnabled(enable);
        jbExchangeRateReset.setEnabled(enable);
        jbExchangeRateRefresh.setEnabled(enable);
    }
    
    private void renderSettingsBankAccountCash() {
        if (moKeyBankAccountCash.getSelectedIndex() <= 0) {
            moDataAccountCash = null;
            moDataBizPartnerBranchBankAccount = null;
            
            moParamsMap = null;
        }
        else {
            moDataAccountCash = (SDataAccountCash) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.FIN_ACC_CASH, moKeyBankAccountCash.getValue(), SLibConstants.EXEC_MODE_SILENT);
            moDataBizPartnerBranchBankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BANK_ACC, new int[] { moDataAccountCash.getFkBizPartnerBranchId_n(), moDataAccountCash.getFkBankAccountId_n() }, SLibConstants.EXEC_MODE_SILENT);
          
            moParamsMap = new HashMap<>();
            moParamsMap.put(SDataConstants.FIN_ACC_COB_ENT, moDataAccountCash.getPkCompanyBranchId());
            moParamsMap.put(SDataConstants.FIN_ACC_CASH, moDataAccountCash.getPkAccountCashId());
        }
    }

    private void itemStateChangedLayoutBank() {
        processLayoutBank();
    }
    
    private void itemStateChangedBankLayoutType() {
        processBankLayoutType();
    }

    private void itemStateChangedBankCurrency() {
        processBankCurrency();
    }
    
    private void itemStateChangedBankAccountCash() {
        processBankAccountCash();
    }
    
    private void itemStateChangedDpsCurrency() {
        processDpsCurrency();
    }
    
    private void itemStateChangedShowOnlyDocsDateDue() {
        loadLayoutRow();
    }

    private void itemStateChangedShowOnlyBenefsWithAccounts() {
        loadLayoutRow();
    }
    
    /*
     * Action performed methods
     */

    public void actionPickRecord() {
        Object key = null;
        String message = "";

        moDialogRecordPicker.formReset();
        moDialogRecordPicker.setFilterKey(moDateDateLayout.getValue());
        moDialogRecordPicker.setParams(moParamsMap);
        moDialogRecordPicker.formRefreshOptionPane();

        if (moCurrentRecord != null) {
            moDialogRecordPicker.setSelectedPrimaryKey(moCurrentRecord.getPrimaryKey());
        }

        moDialogRecordPicker.setFormVisible(true);

        if (moDialogRecordPicker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            key = moDialogRecordPicker.getSelectedPrimaryKey();

            // XXX set registry lock to accounting record

            if (readRecord(key)) {
                if (moCurrentRecord != null) {
                    if (moCurrentRecord.getIsSystem()) {
                        message = "No puede seleccionarse esta póliza contable porque es de sistema.";
                    }
                    else if (moCurrentRecord.getIsAudited()) {
                        message = "No puede seleccionarse esta póliza contable porque está auditada.";
                    }
                    else if (moCurrentRecord.getIsAuthorized()) {
                        message = "No puede seleccionarse esta póliza contable porque está autorizada.";
                    }
                    else if (!SDataUtilities.isPeriodOpen((SClientInterface) miClient, moCurrentRecord.getDate())) {
                        message = "No puede seleccionarse esta póliza contable porque su período contable correspondiente está cerrado.";
                    }

                    if (message.length() > 0) {
                        miClient.showMsgBoxWarning(message);
                        moCurrentRecord = null;
                    }
                    else {
                        renderRecord();
                    }
                }
            }
        }
    }
    
    private void actionPickLayoutPath() {
        String nameFile = "";
        SimpleDateFormat fileNameDatetimeFormat = new SimpleDateFormat("yyMMdd HHmm");
        
        try {
            nameFile = (moKeyBankLayoutType.getSelectedIndex() <= 0 ? "" : SLibUtils.textToAscii(SFinUtilities.getFileNameLayout(miClient.getSession(), moKeyBankLayoutType.getSelectedItem().getPrimaryKey()[0]).toLowerCase().replaceAll("/", " ")));
            nameFile = SLibUtils.validateSafePath(fileNameDatetimeFormat.format(new java.util.Date()) + " " + nameFile + ".txt");
        
            miClient.getFileChooser().setSelectedFile(new File(nameFile));
            if (miClient.getFileChooser().showSaveDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
                jtfLayoutPath.setText(miClient.getFileChooser().getSelectedFile().getAbsolutePath());
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPickExchangeRate() {
        double exchangeRate = ((SClientInterface) miClient).pickExchangeRate(moKeyDpsCurrency.getValue()[0], moDateDateLayout.getDefaultValue());

        if (exchangeRate != 0d) {
            moDecExchangeRate.setValue(exchangeRate);
        }
        else {
            moDecExchangeRate.setValue(0d);
        }
        
        jbPickExchangeRate.requestFocus();
    }
    
    private void actionGridRowsShow() {
        SGuiValidation validation = moFields.validateFields();
        
        if (!validation.isValid()) {
            SGuiUtils.computeValidation(miClient, validation);
        }
        else {
            jtfLayoutPath.setText("");  // clear path each time grid rows are shown
            
            enableFieldsForm(false);
            enableFieldsGrid(true, mnFormSubtype != SModSysConsts.FIN_LAY_BANK_ACC);
            
            switch (mnFormSubtype) {
                case SModSysConsts.FIN_LAY_BANK_PAY:
                    populateGridRowsWithDps(true);
                    jckShowOnlyDocsDateDue.setSelected(true);
                    jckShowOnlyBenefsWithAccounts.setSelected(true);
                    break;
                case SModSysConsts.FIN_LAY_BANK_PREPAY:
                    populateGridRowsWithBeneficiaries(true);
                    jckShowOnlyDocsDateDue.setEnabled(false);
                    jckShowOnlyBenefsWithAccounts.setEnabled(true);
                    break;
                default:
            }
        }
    }
    
    private void actionGridRowsClear() {
        enableFieldsForm(true);
        enableFieldsGrid(false, mnFormSubtype != SModSysConsts.FIN_LAY_BANK_ACC);
        jckShowOnlyDocsDateDue.setSelected(true);
        moGridPayments.clearGridRows();
    }

    /**
     * Resets current exchange rate in current grid row.
     */
    private void actionExchangeRateResetOriginal() {
        if (moGridPayments.getSelectedGridRow() != null) {
            putExchangeRate(((SLayoutBankRow) moGridPayments.getSelectedGridRow()).getBalance().getExchangeRate());
        }
    }

    /**
     * Set´s the exchange rate specified in the field 'exchange rate' for one DPS/row in the grid
     */
    private void actionExchangeRateSetCurrent() {
        putExchangeRate(moDecExchangeRate.getValue());
    }

    private void actionGridRowsCheckAll() {
        SLayoutBankRow layoutBankRow = null;
        SLayoutBankPaymentRow layoutBankPaymentRow = null;
        SLayoutBankRecordKey layoutBankRecordKey = null;
        SLayoutBankPayment layoutBankPayment = null;
        SLayoutBankPayment layoutBankPayment1Aux = null;
        SGuiValidation validation = moFields.validateFields();

        try {
            if (validation.isValid()) {
                if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_ACC) {
                    if (validateRecord(true)) {
                        for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
                            layoutBankPaymentRow = (SLayoutBankPaymentRow) rowAux;
                            layoutBankPayment1Aux = layoutBankPaymentRow.getLayoutBankPayment().clone();
                            layoutBankPayment = layoutBankPaymentRow.getLayoutBankPayment().clone();
                            
                            layoutBankPayment1Aux.setAction(SLayoutBankPayment.ACTION_PAY_REMOVE);
                            computeBankRecords(layoutBankPayment1Aux, layoutBankPaymentRow.getLayoutBankRecordKey());
                            
                            layoutBankPaymentRow.setIsForPayment(true);
                            layoutBankPaymentRow.setRecordPeriod(moCurrentRecord.getRecordPeriod());
                            layoutBankPaymentRow.setRecordBkc(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.FIN_BKC, new int[] { moCurrentRecord.getPkBookkeepingCenterId() }, SLibConstants.DESCRIPTION_CODE));
                            layoutBankPaymentRow.setRecordCob(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.BPSU_BPB, new int[] { moCurrentRecord.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
                            layoutBankPaymentRow.setRecordNumber(moCurrentRecord.getRecordNumber());
                            layoutBankPaymentRow.setRecordDate(moCurrentRecord.getDate());

                            layoutBankPayment.setAction(SLayoutBankPayment.ACTION_PAY_APPLY);
                            layoutBankRecordKey = new SLayoutBankRecordKey(moCurrentRecord.getPkYearId(), moCurrentRecord.getPkPeriodId(), moCurrentRecord.getPkBookkeepingCenterId(), moCurrentRecord.getPkRecordTypeId(), moCurrentRecord.getPkNumberId());
                            
                            layoutBankPaymentRow.setLayoutBankRecordKey(layoutBankRecordKey);
                            computeBankRecords(layoutBankPayment, layoutBankRecordKey);
                        }
                    }
                }
                else {
                    for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
                        layoutBankRow = (SLayoutBankRow) rowAux;
                        layoutBankRow.setIsForPayment(true);
                    }
                }
                
                computeBalancePayment();
                moGridPayments.renderGridRows();
                moGridPayments.setSelectedGridRow(0);
            }
            else {
                if (validation.getComponent() != null) {
                    validation.getComponent().requestFocus();
                }
                if (validation.getMessage().length() > 0) {
                    miClient.showMsgBoxWarning(validation.getMessage());
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private void actionGridRowsUncheckAll() {
        SLayoutBankRow layoutBankRow = null;
        SLayoutBankPaymentRow layoutBankPaymentRow = null;
        SLayoutBankRecordKey layoutBankRecordKey = null;
        SLayoutBankPayment layoutBankPayment = null;
        
        try {
            if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_ACC) {
                for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
                    layoutBankPaymentRow = (SLayoutBankPaymentRow) rowAux;
                    layoutBankPayment = layoutBankPaymentRow.getLayoutBankPayment().clone();

                    layoutBankPaymentRow.setIsForPayment(false);
                    layoutBankPaymentRow.setRecordPeriod("");
                    layoutBankPaymentRow.setRecordBkc("");
                    layoutBankPaymentRow.setRecordCob("");
                    layoutBankPaymentRow.setRecordNumber("");
                    layoutBankPaymentRow.setRecordDate(null);
                    layoutBankPaymentRow.setLayoutBankRecordKey(null);

                    layoutBankPayment.setAction(layoutBankPaymentRow.getIsToPayed() ? SLayoutBankPayment.ACTION_PAY_REMOVE : SLibConsts.UNDEFINED);
                    layoutBankRecordKey = layoutBankPaymentRow.getLayoutBankRecordKey();
                    
                    computeBankRecords(layoutBankPayment, layoutBankRecordKey);
                }
            }
            else {
                for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
                    layoutBankRow = (SLayoutBankRow) rowAux;
                    layoutBankRow.setIsForPayment(false);
                }
            }
            
            computeBalancePayment();
            moGridPayments.renderGridRows();
            moGridPayments.setSelectedGridRow(0);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void processEditingAppPayment() {
        SLayoutBankPaymentRow layoutBankPaymentRow = null;
        SLayoutBankRow layoutBankRow = null;
        SLayoutBankRecordKey layoutBankRecordKey = null;
        
        int index = moGridPayments.getTable().getSelectedRow();
        
        if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_ACC) {
            layoutBankPaymentRow = (SLayoutBankPaymentRow) moGridPayments.getGridRow(index);
        }
        else {
            layoutBankRow = (SLayoutBankRow) moGridPayments.getGridRow(index);
        }
        
        try {
            if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_ACC) {
                boolean addRecord = false;
                SLayoutBankPayment layoutBankPayment = layoutBankPaymentRow.getLayoutBankPayment().clone();

                if (layoutBankPaymentRow.getIsForPayment()) {
                    if (validateRecord(false)) {
                        layoutBankPaymentRow.setIsForPayment(true);
                        layoutBankPaymentRow.setRecordPeriod(moCurrentRecord.getRecordPeriod());
                        layoutBankPaymentRow.setRecordBkc(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.FIN_BKC, new int[] { moCurrentRecord.getPkBookkeepingCenterId() }, SLibConstants.DESCRIPTION_CODE));
                        layoutBankPaymentRow.setRecordCob(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.BPSU_BPB, new int[] { moCurrentRecord.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
                        layoutBankPaymentRow.setRecordNumber(moCurrentRecord.getRecordNumber());
                        layoutBankPaymentRow.setRecordDate(moCurrentRecord.getDate());

                        layoutBankPayment.setAction(SLayoutBankPayment.ACTION_PAY_APPLY);
                        layoutBankRecordKey = new SLayoutBankRecordKey(moCurrentRecord.getPkYearId(), moCurrentRecord.getPkPeriodId(), moCurrentRecord.getPkBookkeepingCenterId(), moCurrentRecord.getPkRecordTypeId(), moCurrentRecord.getPkNumberId());
                        addRecord = true;
                        layoutBankPaymentRow.setLayoutBankRecordKey(layoutBankRecordKey);
                    }
                }
                else {
                    layoutBankPaymentRow.setIsForPayment(false);
                    layoutBankPaymentRow.setRecordPeriod("");
                    layoutBankPaymentRow.setRecordBkc("");
                    layoutBankPaymentRow.setRecordCob("");
                    layoutBankPaymentRow.setRecordNumber("");
                    layoutBankPaymentRow.setRecordDate(null);
                    layoutBankPaymentRow.setLayoutBankRecordKey(null);

                    layoutBankPayment.setAction(layoutBankPaymentRow.getIsToPayed() ? SLayoutBankPayment.ACTION_PAY_REMOVE : SLibConsts.UNDEFINED);
                    layoutBankRecordKey = layoutBankPaymentRow.getLayoutBankRecordKey();
                    addRecord = true;
                }
                
                if (addRecord) {
                    computeBankRecords(layoutBankPayment, layoutBankRecordKey);
                }
            }
            else {
                if (layoutBankRow.getIsForPayment()) {
                    layoutBankRow.setIsForPayment(true);
                }
                else {
                    layoutBankRow.setIsForPayment(false);
                }
            }
            
            computeBalancePayment();
            moGridPayments.renderGridRows();
            moGridPayments.getTable().setRowSelectionInterval(index, index);
        }
        catch (Exception e) {
            if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_ACC) {
                layoutBankPaymentRow.setIsForPayment(false);
                layoutBankPaymentRow.setRecordPeriod("");
                layoutBankPaymentRow.setRecordBkc("");
                layoutBankPaymentRow.setRecordCob("");
                layoutBankPaymentRow.setRecordNumber("");
                layoutBankPaymentRow.setRecordDate(null);
            }
            else {
                layoutBankRow.setIsForPayment(false);
            }
            
            SLibUtils.showException(this, e);
        }
    }

    private void processEditingStoppedBalance() {
        int index = 0;
        SLayoutBankRow row = null;

        index = moGridPayments.getTable().getSelectedRow();
        row = (SLayoutBankRow) moGridPayments.getGridRow(index);
        
        if (row.getBalanceTot().getAmountOriginal() > 0) {
            row.setIsForPayment(true);
        }
        else {
            row.setIsForPayment(false);
        }
        computeBalancePayment();
        moGridPayments.renderGridRows();
        moGridPayments.getTable().setRowSelectionInterval(index, index);
    }
    
    private void processEditingAgreement(final int index) {
        SLayoutBankRow row = (SLayoutBankRow) moGridPayments.getGridRow(index);
        
        maAgreementsReference.clear();
        
        if (mhAgreeAgreeRef.get(row.getAgreement()) == null) {
            ArrayList<SGuiItem> empty = new ArrayList<>();
            empty.add(new SGuiItem(""));
            mltAgreementReference.set(index, empty);
        }
        else {
            mltAgreementReference.set(index, mhAgreeAgreeRef.get(row.getAgreement()));
        }
        
        moTableCellAgreementReference.setElements(mltAgreementReference);
        moGridPayments.renderGridRows();
    }
    
    private void createLayoutXml() {
        SLayoutBankRow row = null;
        SLayoutBankXmlRow xmlRow = null;
        
        updateLayoutRow();
        
        maXmlRows = new ArrayList<>();
        for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
            row = (SLayoutBankRow) rowAux;
            
            for (int i = 0; i < mvLayoutRows.size(); i++) {
                if (SLibUtilities.compareKeys(row.getRowPrimaryKey(), mvLayoutRows.get(i).getRowPrimaryKey()) && (mvLayoutRows.get(i).getIsForPayment() || mvLayoutRows.get(i).getIsToPayed())) {
                    xmlRow = new SLayoutBankXmlRow();

                    xmlRow.setLayoutXmlRowType(mvLayoutRows.get(i).getLayoutRowType());
                    xmlRow.setDpsYear(row.getPkYearId());
                    xmlRow.setDpsDoc(row.getPkDocId());
                    xmlRow.setAmount(row.getBalanceTot().getAmountLocal());
                    xmlRow.setAmountCy(row.getBalanceTot().getAmountOriginal());
                    xmlRow.setAmountPayed(row.getBalancePayed());
                    xmlRow.setCurrencyId(row.getBalanceTot().getCurrencyOriginalId());
                    xmlRow.setExchangeRate(row.getBalanceTot().getExchangeRate());
                    xmlRow.setIsToPayed(false);
                    xmlRow.setBizPartner(row.getBizPartnerId());
                    if (mnBankPaymentType == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                        if (!row.getAgreementReference().isEmpty()) {
                            xmlRow.setAgreement(row.getAgreement());
                            
                            if (row.getAgreementReference().length() >= SLayoutBankRow.LENGTH_MAX_REF && (row.getConceptCie().isEmpty())) {
                                xmlRow.setAgreementReference(row.getAgreementReference().substring(0, SLayoutBankRow.LENGTH_MAX_REF));
                                xmlRow.setConceptCie(row.getAgreementReference().substring(SLayoutBankRow.LENGTH_MAX_REF));
                            }
                            else {
                                xmlRow.setAgreementReference(row.getAgreementReference());
                                xmlRow.setConceptCie(row.getConceptCie());
                            }
                            
                            int[] keyBizPartnerBranchAccount = new int[2];
                            for (SGuiItem item : row.getAccountCredits()) {
                                if (item.getItem().trim().equals(row.getAgreement().trim())) {
                                    keyBizPartnerBranchAccount = item.getPrimaryKey();
                                    break;
                                }
                            }
                            xmlRow.setBizPartnerBranch(keyBizPartnerBranchAccount[0]);
                            xmlRow.setBizPartnerBranchAccount(keyBizPartnerBranchAccount[1]);
                        }
                    } 
                    else {
                        xmlRow.setBizPartnerBranch(row.getBranchBankAccountCreditId(row.getAccountCredit(), mnBankPaymentType)[0]);
                        xmlRow.setBizPartnerBranchAccount(row.getBranchBankAccountCreditId(row.getAccountCredit(), mnBankPaymentType)[1]);
                    }
                    xmlRow.setHsbcBankCode(row.getBankKey());
                    xmlRow.setHsbcAccountType(row.getAccType());
                    xmlRow.setHsbcFiscalIdDebit(row.getBizPartnerDebitFiscalId());
                    xmlRow.setHsbcFiscalIdCredit(row.getBizPartnerCreditFiscalId());
                    xmlRow.setHsbcFiscalVoucher(row.getCf());
                    xmlRow.setSantanderBankCode(row.getSantanderBankCode() == null ? "0" : row.getSantanderBankCode());
                    xmlRow.setConcept(row.getConcept());
                    xmlRow.setDescription(row.getDescription());
                    xmlRow.setReference(row.getReference());
                    xmlRow.setBajioBankCode(row.getBajioBankCode() == null ? "0" : row.getBajioBankCode());
                    xmlRow.setBajioBankNick(row.getBajioBankAlias() == null ? "0" : row.getBajioBankAlias());
                    xmlRow.setBankKey(row.getBankKey());
                    xmlRow.setRecYear(SLibConsts.UNDEFINED);
                    xmlRow.setRecPeriod(SLibConsts.UNDEFINED);
                    xmlRow.setRecBookkeepingCenter(SLibConsts.UNDEFINED);
                    xmlRow.setRecRecordType("");
                    xmlRow.setRecNumber(SLibConsts.UNDEFINED);
                    xmlRow.setBookkeepingYear(SLibConsts.UNDEFINED);
                    xmlRow.setBookkeepingNumber(SLibConsts.UNDEFINED);
                    xmlRow.setReferenceRecord(row.getReferenceRecord());
                    xmlRow.setObservations(row.getObservations());
                    
                    if (mvLayoutRows.get(i).getIsForPayment()) {
                        maXmlRows.add(xmlRow);
                    }
                }
            }
        }
    }
    
    private void loadPaymentsXml() {
        int idDpsYear = 0;
        int idDpsDoc = 0;
        double balancePayment = 0;
        double balanceDoc = 0;
        double balanceCyDoc = 0;
        double excRate = 0;
        String observation = "";
        String referenceRecord = "";
        SXmlBankLayoutPayment layoutPay = null;
        SXmlBankLayoutPaymentDoc layoutPayDoc = null;
        Vector<SGridRow> rows = new Vector<>();
        Vector<SLayoutBankPaymentRow> rowsAux = new Vector<>();
        SXmlBankLayout gridXml = new SXmlBankLayout();
        SDataRecord record = null;
        SDataBizPartnerBranch bizPartnerBranchCob = null;
        SDataBizPartnerBranchBankAccount branchBankAccount = null;
        SDataBizPartner bizPartner = null;
        SDataDps dps = null;
        SLayoutBankPaymentRow moRow = null;
        SLayoutBankPayment moPayment = null;
        SLayoutBankDps moDps = null;
        SLayoutBankXmlRow xmlRow = null;
        SLayoutBankRecordKey layoutBankRecordKey = null;
        
        try {
            gridXml.processXml(moRegistry.getLayoutXml());
            
            for (SXmlElement element : gridXml.getXmlElements()) {
                if (element instanceof SXmlBankLayoutPayment) {
                    
                    // Payment:

                    layoutPay = (SXmlBankLayoutPayment) element;
                    
                    bizPartnerBranchCob = (SDataBizPartnerBranch) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BPB, new int[] { (int) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).getValue() }, SLibConstants.EXEC_MODE_SILENT);
                    branchBankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BANK_ACC, new int[] { (int) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).getValue(), (int) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).getValue() }, SLibConstants.EXEC_MODE_SILENT);
                    bizPartner = (SDataBizPartner) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BP, new int[] { bizPartnerBranchCob.getFkBizPartnerId() }, SLibConstants.EXEC_MODE_SILENT);
                    balancePayment = (double) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).getValue();
                    
                    moRow = new SLayoutBankPaymentRow(miClient);

                    moRow.setLayoutRowSubType(mnBankPaymentType);
                    moRow.setBizPartnerId(bizPartner.getPkBizPartnerId());
                    moRow.setBizPartnerBranchId(branchBankAccount.getPkBizPartnerBranchId());
                    moRow.setBizPartnerBranchAccountId(branchBankAccount.getPkBankAccountId());
                    moRow.setBizPartner(bizPartner.getBizPartner());
                    moRow.setBizPartnerKey(bizPartner.getDbmsCategorySettingsSup().getKey());
                    moRow.setBalance(balancePayment);
                    moRow.setBalanceTot(0);
                    moRow.setCurrencyKey(branchBankAccount.getDbmsCurrencyKey());
                    moRow.setAccountCredit(mnBankPaymentType != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? branchBankAccount.getBankAccountNumberStd(): branchBankAccount.getBankAccountNumber());
                    moRow.setAgreement((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE).getValue());
                    moRow.setAgreementReference((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_REF).getValue());
                    moRow.setAgreementConcept((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_CON).getValue());
                    moRow.setIsForPayment((boolean) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).getValue());
                    moRow.setIsToPayed((boolean) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).getValue());
                    
                    if ((Integer) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR).getValue() != 0) {
                        
                        layoutBankRecordKey = new SLayoutBankRecordKey((Integer) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR).getValue(), (Integer) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_PER).getValue(),
                                        (Integer) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_BKC).getValue(), (String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_REC_TP).getValue(),
                                                        (Integer) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_NUM).getValue());
                        
                        moRow.setLayoutBankRecordKey(layoutBankRecordKey);
                        moRow.setLayoutBankRecordKeyOld(layoutBankRecordKey);
                        
                        record = (SDataRecord) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.FIN_REC, layoutBankRecordKey.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                        
                        moRow.setRecordPeriod(record.getRecordPeriod());
                        moRow.setRecordBkc(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.FIN_BKC, new int[] { record.getPkBookkeepingCenterId() }, SLibConstants.DESCRIPTION_CODE));
                        moRow.setRecordCob(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.BPSU_BPB, new int[] { record.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
                        moRow.setRecordNumber(record.getRecordNumber());
                        moRow.setRecordDate(record.getDate());
                    }
                    else {
                        layoutBankRecordKey = null;
                    }
                    
                    moPayment = new SLayoutBankPayment(SLibConsts.UNDEFINED, bizPartner.getPkBizPartnerId(), branchBankAccount.getPkBizPartnerBranchId(), branchBankAccount.getPkBankAccountId());
                    
                    moPayment.setAmount(new SMoney(balancePayment, branchBankAccount.getFkCurrencyId()));
                    moPayment.setFkBookkeepingYearId_n((Integer) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_YEAR).getValue());
                    moPayment.setFkBookkeepingNumberId_n((Integer) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_NUM).getValue());
                    moPayment.setAction(SLibConsts.UNDEFINED);
                    
                    // create layout bank document:
                    
                    for (SXmlElement elementDoc : layoutPay.getXmlElements()) {
                        if (elementDoc instanceof SXmlBankLayoutPaymentDoc) {
                            layoutPayDoc = (SXmlBankLayoutPaymentDoc) elementDoc;
                            
                            idDpsYear = (Integer) layoutPayDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR).getValue();
                            idDpsDoc = (Integer) layoutPayDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC).getValue();
                            dps = (SDataDps) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_DPS, new int[] { idDpsYear, idDpsDoc }, SLibConstants.EXEC_MODE_SILENT);
                            balanceCyDoc = (double) layoutPayDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT_CY).getValue();
                            balanceDoc = (double) layoutPayDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT).getValue();
                            excRate = (double) layoutPayDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EXT_RATE).getValue();
                            observation = (String) layoutPayDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_OBS).getValue();
                            referenceRecord = (String) layoutPayDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_REF_REC).getValue();
                            
                            if (dps != null) {
                                moDps = new SLayoutBankDps(idDpsYear, idDpsDoc, dps.getFkDpsCategoryId(), dps.getFkDpsClassId(), dps.getFkDpsTypeId(), dps.getFkCurrencyId(), balanceDoc, dps.getExchangeRate());
                            }
                            
                            if (idDpsYear != SLibConsts.UNDEFINED && idDpsDoc != SLibConsts.UNDEFINED) {
                                moPayment.setLayoutRowType(SModSysConsts.FIN_LAY_BANK_PAY);
                            }
                            else {
                                moPayment.setLayoutRowType(SModSysConsts.FIN_LAY_BANK_PREPAY);
                            }
                            
                            moPayment.getAmount().setExchangeRate(dps != null ? dps.getExchangeRate() : SLibConsts.UNDEFINED);
                            moPayment.getLayoutBankDps().add(moDps);
                            moPayment.setReferenceRecord(referenceRecord);
                            
                            xmlRow = new SLayoutBankXmlRow();

                            xmlRow.setLayoutXmlRowType(moPayment.getLayoutPaymentType());
                            xmlRow.setDpsYear(dps != null ? dps.getPkYearId() : SLibConsts.UNDEFINED);
                            xmlRow.setDpsDoc(dps != null ? dps.getPkDocId() : SLibConsts.UNDEFINED);
                            xmlRow.setAmount(balanceDoc);
                            xmlRow.setAmountCy(balanceCyDoc);
                            xmlRow.setAmountPayed(balanceDoc);
                            xmlRow.setExchangeRate(excRate);
                            xmlRow.setIsToPayed(moRow.getIsToPayed());
                            xmlRow.setBizPartner(bizPartner.getPkBizPartnerId());
                            xmlRow.setBizPartnerBranch(branchBankAccount.getPkBizPartnerBranchId());
                            xmlRow.setBizPartnerBranchAccount(branchBankAccount.getPkBankAccountId());
                            
                            xmlRow.setAgreement((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE).getValue());
                            xmlRow.setAgreementReference((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_REF).getValue());
                            xmlRow.setConceptCie((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_CON).getValue());
                            
                            xmlRow.setHsbcBankCode(SLibUtils.parseInt((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_BANK_CODE).getValue()));
                            xmlRow.setHsbcAccountType((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_ACC_TP).getValue());
                            xmlRow.setHsbcFiscalIdDebit((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_DBT).getValue());
                            xmlRow.setHsbcFiscalIdCredit((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_ID_CRD).getValue());
                            xmlRow.setHsbcFiscalVoucher(SLibUtils.parseInt((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_FIS_VOU).getValue()));
                            xmlRow.setSantanderBankCode((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_SAN_BANK_CODE).getValue());
                            xmlRow.setConcept((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_CPT).getValue());
                            xmlRow.setDescription((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_HSBC_DCRP).getValue());
                            xmlRow.setReference(dps != null ? ((dps.getNumberSeries().length() == 0 ? "" : dps.getNumberSeries() + "-") + dps.getNumber()) : "");
                            xmlRow.setBajioBankCode((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_BANK_CODE).getValue());
                            xmlRow.setBajioBankNick((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BAJIO_NICK).getValue());
                            xmlRow.setBankKey((int) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_KEY).getValue());
                            xmlRow.setReferenceRecord(referenceRecord);
                            xmlRow.setObservations(observation);
                            
                            if (layoutBankRecordKey != null) {
                                xmlRow.setRecYear(layoutBankRecordKey.getPkYearId());
                                xmlRow.setRecPeriod(layoutBankRecordKey.getPkPeriodId());
                                xmlRow.setRecBookkeepingCenter(layoutBankRecordKey.getPkBookkeepingCenterId());
                                xmlRow.setRecRecordType(layoutBankRecordKey.getPkRecordTypeId());
                                xmlRow.setRecNumber(layoutBankRecordKey.getPkNumberId());
                                xmlRow.setBookkeepingYear(moPayment.getFkBookkeepingYearId_n());
                                xmlRow.setBookkeepingNumber(moPayment.getFkBookkeepingNumberId_n());
                            }
                            else {
                                xmlRow.setRecYear(SLibConsts.UNDEFINED);
                                xmlRow.setRecPeriod(SLibConsts.UNDEFINED);
                                xmlRow.setRecBookkeepingCenter(SLibConsts.UNDEFINED);
                                xmlRow.setRecRecordType("");
                                xmlRow.setRecNumber(SLibConsts.UNDEFINED);
                                xmlRow.setBookkeepingYear(SLibConsts.UNDEFINED);
                                xmlRow.setBookkeepingNumber(SLibConsts.UNDEFINED);
                            }
                            maXmlRows.add(xmlRow);
                        }
                    }
                    moRow.setLayoutBankPayment(moPayment);
                    rowsAux.add(moRow);
                }
            }
            rows.addAll(rowsAux);
            
            moGridPayments.populateGrid(rows);
            moGridPayments.createGridColumns();
            
            moGridPayments.getTable().setColumnSelectionAllowed(false);
            moGridPayments.getTable().getTableHeader().setReorderingAllowed(false);
            moGridPayments.getTable().getTableHeader().setResizingAllowed(true);
            moGridPayments.getTable().setRowSorter(new TableRowSorter<>(moGridPayments.getModel()));
            moGridPayments.getTable().getTableHeader().setEnabled(false);
            
            if (moGridPayments.getTable().getRowCount() > 0) {
                moGridPayments.renderGridRows();
                moGridPayments.setSelectedGridRow(0);
                moTableCellEditorOptions.setElements(mltAccountCredit);
                moTableCellAgreementReference.setElements(mltAgreementReference);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void updateDocsXml() throws Exception {
        int[] key = new int[] { SLibConsts.UNDEFINED };
        int num = 0;
        int foundNum = 0;
        SXmlBankLayout gridXml = new SXmlBankLayout();
        
        gridXml.processXml(moRegistry.getLayoutXml());

        for (SXmlElement elementPay : gridXml.getXmlElements()) {
            
            if (elementPay instanceof SXmlBankLayoutPayment) {
                SXmlBankLayoutPayment layoutPay = (SXmlBankLayoutPayment) elementPay;
                
                for (SXmlElement elementDoc : layoutPay.getXmlElements()) {
                    if (elementDoc instanceof SXmlBankLayoutPaymentDoc) {
                        num++;
                        // Dps:

                        SXmlBankLayoutPaymentDoc layoutDps = (SXmlBankLayoutPaymentDoc) elementDoc;
                        
                        if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_PAY) {
                            key = new int[] { (Integer) layoutDps.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR).getValue(), (Integer) layoutDps.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC).getValue() };
                        }
                        else if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_PREPAY) {
                            key = new int[] { (int) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BP).getValue() };
                        }
                        
                        if (mvLayoutRows != null) {
                            for (int i = 0; i < mvLayoutRows.size(); i++) {
                                if (SLibUtilities.compareKeys(key, mvLayoutRows.get(i).getRowPrimaryKey())) {
                                    foundNum++; 
                                    mvLayoutRows.get(i).setIsForPayment(true);
                                    mvLayoutRows.get(i).setIsToPayed((boolean) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).getValue());
                                    mvLayoutRows.get(i).getBalanceTot().setAmountOriginal((double) layoutDps.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT_CY).getValue());
                                    if ((moKeyBankCurrency.getValue()[0] == moKeyDpsCurrency.getValue()[0]) && ((double) layoutDps.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EXT_RATE).getValue() == 0)){
                                        mvLayoutRows.get(i).setExchangeRate(1);
                                    }
                                    else {
                                        mvLayoutRows.get(i).setExchangeRate((double) layoutDps.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EXT_RATE).getValue() );
                                    }
                                    mvLayoutRows.get(i).setBalancePayed((double) layoutDps.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT).getValue());
                                    mvLayoutRows.get(i).setAccountCredit(mvLayoutRows.get(i).getBranchBankAccountCreditNumber(new int[] { (int) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).getValue(), (int) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).getValue() }, mnBankPaymentType));
                                    mvLayoutRows.get(i).setAgreement((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE).getValue());
                                    mvLayoutRows.get(i).setAgreementReference((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_REF).getValue());
                                    mvLayoutRows.get(i).setConceptCie((String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_CON).getValue());
                                    mvLayoutRows.get(i).setObservations((String) layoutDps.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_OBS).getValue());
                                    mvLayoutRows.get(i).setReferenceRecord((String) layoutDps.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_REF_REC).getValue());
                                }
                            }
                        }
                    }
                }
            }
        }

        if (foundNum != num) {
            if (miClient.showMsgBoxConfirm("No se encontraron todas las cuentas por pagar del layout.\n¿Desea cargar el layout sólo con las cuentas que sea posible?") == JOptionPane.NO_OPTION) {
                throw new Exception("¡No se encontraron todas las cuentas por pagar del layout!");
            }
        }
        moGridPayments.renderGridRows();
        moGridPayments.setSelectedGridRow(0);
    }
    
    private void computeBankRecords(SLayoutBankPayment layoutBankPayment, SLayoutBankRecordKey layoutBankRecordKey) throws Exception {
        boolean found = false;
        SDataRecord record = null;
        SLayoutBankRecord bankRecord = null;
        SSrvLock lock = null;
        
        if (layoutBankRecordKey == null) {
            for (SLayoutBankRecord bankRecordRow : maLayoutBankRecords) {
                bankRecordRow.removeLayoutBankPayment(layoutBankPayment.getBizPartnerId(), layoutBankPayment.getBizPartnerBranchId(), layoutBankPayment.getBizPartnerBranchAccountId());
            }
        }
        else {
            for (SLayoutBankRecord bankRecordRow : maLayoutBankRecords) {
                if (SLibUtils.compareKeys(bankRecordRow.getLayoutBankRecordKey().getPrimaryKey(), layoutBankRecordKey.getPrimaryKey())) {
                    bankRecordRow.getLayoutBankPayments().add(layoutBankPayment);
                    found = true;
                    break;
                }
            }

            if (!found) {
                record = (SDataRecord) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.FIN_REC, layoutBankRecordKey.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                lock = SSrvUtils.gainLock(miClient.getSession(), ((SClientInterface) miClient).getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.FIN_REC, layoutBankRecordKey.getPrimaryKey(), record.getRegistryTimeout());
                maLocks.add(lock);

                bankRecord = new SLayoutBankRecord(layoutBankRecordKey);
                bankRecord.getLayoutBankPayments().add(layoutBankPayment);
                maLayoutBankRecords.add(bankRecord);
            }
        }
    }

    private void computeBalancePayment() {
        double dBalanceTot = 0;
        double dBalancePayed = 0;
        SLayoutBankRow row = null;
        SLayoutBankPaymentRow payRow = null;
        
        mnNumberDocs = 0;
        jtfRows.setText(SLibUtils.DecimalFormatInteger.format(mnNumberDocs) + "/" + SLibUtils.DecimalFormatInteger.format(moGridPayments.getModel().getRowCount()));
        
        if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_ACC) {
            moDecBalanceTotPay.setValue(dBalancePayed);
            
            for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
                payRow = (SLayoutBankPaymentRow) rowAux;

                if (payRow.getIsForPayment()) {
                    if (payRow.getBalanceTot() == 0) {
                        payRow.setBalanceTot(payRow.getBalance());
                    }
                    dBalancePayed += payRow.getBalanceTot();
                    mnNumberDocs++;
                }
                else {
                    payRow.setBalanceTot(0d);
                    dBalancePayed -= payRow.getBalanceTot();
                }
                moDecBalanceTotPay.setValue(dBalancePayed);
                jtfRows.setText(SLibUtils.DecimalFormatInteger.format(mnNumberDocs) + "/" + SLibUtils.DecimalFormatInteger.format(moGridPayments.getModel().getRowCount()));
            }
        }
        else {
            for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
                row = (SLayoutBankRow) rowAux;

                if (row.getIsForPayment()) {
                    if (row.getBalanceTot().getAmountOriginal() == 0) {
                        row.getBalanceTot().setAmountOriginal(row.getBalance().getAmountOriginal());
                    }
                    dBalanceTot += row.getBalanceTot().getAmountLocal();
                    mnNumberDocs++;
                }
                else {
                    row.getBalanceTot().setAmountOriginal(0d);
                    dBalanceTot -= row.getBalanceTot().getAmountLocal();
                }
                jtfRows.setText(SLibUtils.DecimalFormatInteger.format(mnNumberDocs) + "/" + SLibUtils.DecimalFormatInteger.format(moGridPayments.getModel().getRowCount()));
            }
            moDecBalanceTot.setValue(dBalanceTot);
        }
    }

    private void updateLayoutRow() {
        SLayoutBankRow row = null;
        
        for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
            row = (SLayoutBankRow) rowAux;
            
            for (int i = 0; i < mvLayoutRows.size(); i++) {
                if (SLibUtilities.compareKeys(row.getRowPrimaryKey(), mvLayoutRows.get(i).getRowPrimaryKey())) {
                     mvLayoutRows.get(i).setIsForPayment(row.getIsForPayment());
                     mvLayoutRows.get(i).setAccountCredit(row.getAccountCredit());
                     mvLayoutRows.get(i).setAgreement(row.getAgreement());
                     mvLayoutRows.get(i).setAgreementReference(row.getAgreementReference());
                     mvLayoutRows.get(i).setConceptCie(row.getConceptCie());
                     mvLayoutRows.get(i).setBalanceTot(row.getBalanceTot());
                     mvLayoutRows.get(i).setBalanceTotByBizPartner(row.getBalanceTot().getAmountOriginal());
                     mvLayoutRows.get(i).setBankKey(SLibUtilities.parseInt((row.getAccountCredit().length() > 10 ? row.getAccountCredit().substring(0, 3) : "000")));
                     mvLayoutRows.get(i).setEmail(row.getEmail());
                     mvLayoutRows.get(i).setSantanderBankCode(mvLayoutRows.get(i).getCodeBankAccountCredits().get(row.getAccountCredit()));
                     mvLayoutRows.get(i).setBajioBankCode(mvLayoutRows.get(i).getCodeBankAccountCredits().get(row.getAccountCredit()));
                     mvLayoutRows.get(i).setBajioBankAlias(mvLayoutRows.get(i).getAliasBankAccountCredits().get(row.getAccountCredit()));
                     mvLayoutRows.get(i).setObservations(row.getObservations());
                }
            }
        }
    }

    private void loadLayoutRow() {
        updateLayoutRow();
        moGridPayments.clearGridRows();

        if (mvLayoutRows != null) {
            mltAccountCredit.clear();
            mltAgreement.clear();
            mltAgreementReference.clear();
            
            if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_PAY) {
                for (int i = 0; i < mvLayoutRows.size(); i++) {
                    
                    if (!jckShowOnlyDocsDateDue.isSelected()) {
                        if (!jckShowOnlyBenefsWithAccounts.isSelected()) {
                            mltAccountCredit.add(mvLayoutRows.get(i).getAccountCredits());
                            mltAgreementReference.add(mhAgreeAgreeRef.get(mvLayoutRows.get(i).getAgreement()));
                            moGridPayments.addGridRow(mvLayoutRows.get(i));
                        }
                        else {
                            if (mvLayoutRows.get(i).getAccountCredits().size() > 0) {
                                mltAccountCredit.add(mvLayoutRows.get(i).getAccountCredits());
                                mltAgreementReference.add(mhAgreeAgreeRef.get(mvLayoutRows.get(i).getAgreement()));
                                moGridPayments.addGridRow(mvLayoutRows.get(i));
                            }
                        }
                    }
                    else if (moDateDateDue.getValue().equals(mvLayoutRows.get(i).getDateMaturityRo())) {
                        if (!jckShowOnlyBenefsWithAccounts.isSelected()) {
                            mltAgreementReference.add(mhAgreeAgreeRef.get(mvLayoutRows.get(i).getAgreement()));
                            mltAccountCredit.add(mvLayoutRows.get(i).getAccountCredits());
                            moGridPayments.addGridRow(mvLayoutRows.get(i));
                        }
                        else {
                            if (mvLayoutRows.get(i).getAccountCredits().size() > 0) {
                                mltAgreementReference.add(mhAgreeAgreeRef.get(mvLayoutRows.get(i).getAgreement()));
                                mltAccountCredit.add(mvLayoutRows.get(i).getAccountCredits());
                                moGridPayments.addGridRow(mvLayoutRows.get(i));
                            }
                        }
                    }
                }
            }
            else if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_PREPAY) {
                for (int i = 0; i < mvLayoutRows.size(); i++) {

                    if (!jckShowOnlyBenefsWithAccounts.isSelected() || mvLayoutRows.get(i).getAccountCredits().size() > 0) {
                        mltAccountCredit.add(mvLayoutRows.get(i).getAccountCredits());
                        mltAgreementReference.add(mhAgreeAgreeRef.get(mvLayoutRows.get(i).getAgreement()));
                        moGridPayments.addGridRow(mvLayoutRows.get(i));
                    }
                }
            }
        }
        computeBalancePayment();
        moTableCellEditorOptions.setElements(mltAccountCredit);
        moTableCellAgreementReference.setElements(mltAgreementReference);
        moGridPayments.renderGridRows();
        moGridPayments.setSelectedGridRow(0);
    }
/* -->
    private void loadLayoutRow() {
        updateLayoutRow();
        moGridPayments.clearGridRows();
        
        if (mvLayoutRows != null) {
            mltAccountCredit.clear();
            mltAgreement.clear();
            mltAgreementReference.clear();
            
            if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_DPS) {
                for (int i = 0; i < mvLayoutRows.size(); i++) {
                    if (!jckShowOnlyDocsDateDue.isSelected()) {
                        if (!jckShowOnlyBenefsWithAccounts.isSelected()) {
                            mltAccountCredit.add(mvLayoutRows.get(i).getAccountCredits());
                            mltAgreementReference.add(mhAgreeAgreeRef.get(mvLayoutRows.get(i).getAgreement()));
                            moGridPayments.addGridRow(mvLayoutRows.get(i));
                        }
                        else {
                            if (mvLayoutRows.get(i).getAccountCredits().size() > 0) {
                                mltAccountCredit.add(mvLayoutRows.get(i).getAccountCredits());
                                mltAgreementReference.add(mhAgreeAgreeRef.get(mvLayoutRows.get(i).getAgreement()));
                                moGridPayments.addGridRow(mvLayoutRows.get(i));
                            }
                        }
                    }
                    else if (moDateDateDue.getValue().equals(mvLayoutRows.get(i).getDateMaturityRo())) {
                        if (!jckShowOnlyBenefsWithAccounts.isSelected()) {
                            mltAgreementReference.add(mhAgreeAgreeRef.get(mvLayoutRows.get(i).getAgreement()));
                            mltAccountCredit.add(mvLayoutRows.get(i).getAccountCredits());
                            moGridPayments.addGridRow(mvLayoutRows.get(i));
                        }
                        else {
                            if (mvLayoutRows.get(i).getAccountCredits().size() > 0) {
                                mltAgreementReference.add(mhAgreeAgreeRef.get(mvLayoutRows.get(i).getAgreement()));
                                mltAccountCredit.add(mvLayoutRows.get(i).getAccountCredits());
                                moGridPayments.addGridRow(mvLayoutRows.get(i));
                            }
                        }
                    }
                }
            }
            else if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_ADV) {
                for (int i = 0; i < mvLayoutRows.size(); i++) {
                    if (!jckShowOnlyBenefsWithAccounts.isSelected()) {
                        mltAccountCredit.add(mvLayoutRows.get(i).getAccountCredits());
                        moGridPayments.addGridRow(mvLayoutRows.get(i));
                    }
                    else {
                        if (mvLayoutRows.get(i).getAccountCredits().size() > 0) {
                            mltAccountCredit.add(mvLayoutRows.get(i).getAccountCredits());
                            moGridPayments.addGridRow(mvLayoutRows.get(i));
                        }
                    }
                }
            }
            else if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_ADV) {
                for (int i = 0; i < mvLayoutRows.size(); i++) {
                    if (!jckShowOnlyBenefsWithAccounts.isSelected()) { 
                        mltAccountCredit.add(mvLayoutRows.get(i).getAccountCredits());
                        moGridPayments.addGridRow(mvLayoutRows.get(i));
                    }
                    else {
                        if (mvLayoutRows.get(i).getAccountCredits().size() > 0) {
                            mltAccountCredit.add(mvLayoutRows.get(i).getAccountCredits());
                            moGridPayments.addGridRow(mvLayoutRows.get(i));
                        }
                    }
                }
            }
        }
        
        computeBalancePayment();
        moTableCellEditorOptions.setElements(mltAccountCredit);
        moTableCellAgreementReference.setElements(mltAgreementReference);
        moGridPayments.renderGridRows();
        moGridPayments.setSelectedGridRow(0);
    }
<-- */
    private String createSqlQuery() {
        String sql = "";
      
        if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_PAY) {
            sql = "SELECT b.id_bp, b.bp, b.fiscal_id, d.id_year, d.id_doc, d.dt AS dt, " +
                     "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                     "d.stot_r AS f_stot, d.stot_cur_r AS f_stot_cur, d.tax_charged_r AS f_tax, d.tax_charged_cur_r AS f_tax_cur, " +
                     "d.tax_retained_r AS f_ret, d.tax_retained_cur_r AS f_ret_cur, d.tot_r AS f_tot, d.tot_cur_r AS f_tot_cur, c.id_cur AS f_id_cur, " +
                     "SUM(re.credit - re.debit) AS f_bal, SUM(re.credit_cur - re.debit_cur) AS f_bal_cur, bcon.email_01, d.fid_bpb, bct.bp_key, " +
                     "dt.code, bpb.bpb, cob.code AS cob_code, c.cur_key, d.exc_rate AS f_ext_rate, " +
                     "COALESCE((SELECT SUM(tax.tax) FROM trn_dps_ety AS de " +
                     "INNER JOIN trn_dps_ety_tax AS tax ON de.id_year = tax.id_year AND de.id_doc = tax.id_doc AND de.id_ety = tax.id_ety AND tax.id_tax_bas = " + SDataConstantsSys.FINU_TAX_BAS_VAT + " " +
                     "WHERE d.id_year = de.id_year AND d.id_doc = de.id_doc AND de.b_del = 0), 0) AS f_iva ,"+
                     "COALESCE((SELECT SUM(tax.tax_cur) FROM trn_dps_ety AS de " +
                     "INNER JOIN trn_dps_ety_tax AS tax ON de.id_year = tax.id_year AND de.id_doc = tax.id_doc AND de.id_ety = tax.id_ety AND tax.id_tax_bas = " + SDataConstantsSys.FINU_TAX_BAS_VAT + " " +
                     "WHERE d.id_year = de.id_year AND d.id_doc = de.id_doc AND de.b_del = 0), 0) AS f_iva_cur, ADDDATE(d.dt_start_cred, d.days_cred) AS dt_mat " +
                     "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                     "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                     "r.id_year = " + SLibTimeUtilities.digestYear(moDateDateDue.getValue())[0] + " AND r.b_del = 0 AND re.b_del = 0 AND " +
                     "re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + " " +
                     "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " +
                     "INNER JOIN erp.bpsu_bp_ct AS bct ON re.fid_bp_nr = bct.id_bp AND bct.id_ct_bp = re.fid_tp_sys_mov_xxx " +
                     "INNER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc AND d.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
                     "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                     "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb " +
                     "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur AND c.id_cur = " + (moKeyDpsCurrency.getValue().length == 0 ? "0" : ((int []) moKeyDpsCurrency.getValue())[0]) + " " +
                     "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
                     "LEFT OUTER JOIN erp.bpsu_bpb_con AS bcon ON bpb.id_bpb = bcon.id_bpb AND bcon.id_con = " + SDataConstantsSys.BPSS_TP_CON_ADM + " " +
                     "WHERE EXISTS(SELECT * FROM erp.bpsu_bank_acc AS ac WHERE bpb.id_bpb = ac.id_bpb " + (mnBankPaymentType == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD || mnBankPaymentType == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE ? "AND ac.fid_bank = " + mnBizPartnerBank : "AND ac.fid_bank <> " + mnBizPartnerBank) + ") " +
                     "GROUP BY b.id_bp, b.bp, b.fiscal_id, d.id_year, d.id_doc, d.dt, d.stot_r, d.tax_charged_r, d.tax_retained_r, d.tot_r, c.id_cur, bcon.email_01, d.fid_bpb, bct.bp_key, dt.code, bpb.bpb, cob.code " +
                     "HAVING f_bal > 0 " +
                     "ORDER BY bp, id_bp, f_num, dt, id_year, id_doc; ";
        }
        else {
            sql = "SELECT b.id_bp, b.bp, bcon.email_01, b.fiscal_id, " +
                    "(SELECT cur_key FROM erp.cfgu_cur WHERE id_cur = (IF(bct.fid_cur_n IS NULL, (SELECT fid_cur FROM erp.cfg_param_erp), bct.fid_cur_n))) AS _cur, bct.bp_key, bpb.id_bpb " +
                    "FROM erp.bpsu_bp AS b " +
                    "INNER JOIN erp.bpsu_bp_ct AS bct ON  bct.id_bp = b.id_bp AND bct.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_SUP + " AND " +
                    (mnDpsCurrency == SLibConsts.UNDEFINED ? "bct.fid_cur_n = " + mnDpsCurrency + " " : "(bct.fid_cur_n IS NULL OR bct.fid_cur_n = " + mnDpsCurrency + ") ") +
                    "INNER JOIN erp.bpsu_bpb AS bpb ON bpb.fid_bp = b.id_bp AND bpb.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " +
                    "LEFT OUTER JOIN erp.bpsu_bpb_con AS bcon ON bpb.id_bpb = bcon.id_bpb AND bcon.id_con = " + SDataConstantsSys.BPSS_TP_CON_ADM + " " +
                    "WHERE EXISTS(SELECT * FROM erp.bpsu_bank_acc AS ac WHERE bpb.id_bpb = ac.id_bpb " + (mnBankPaymentType == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? "AND ac.fid_bank = " + mnBizPartnerBank : "AND ac.fid_bank <> " + mnBizPartnerBank) + ") " +
                    "AND b.b_del = 0 AND bct.b_del = 0 " +
                    "ORDER BY bp, id_bp; ";
        }
        
        return sql;
    }
    
    @SuppressWarnings("unchecked")
    public void populateGridRowsWithDps(final boolean isNeedValidateRows) {
        String sql = "";
        ResultSet resulSet = null;
        Cursor cursor = getCursor();
        Vector<SGridRow> rows = new Vector<>();
        
        mvLayoutRows = new Vector<>();
        mltAccountCredit = new ArrayList<>();
        mltAgreement= new ArrayList<>();
        mltAgreementReference= new ArrayList<>();
        mhAgreeAgreeRef = new HashMap<>();

        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            
            sql = createSqlQuery();
            resulSet = miClient.getSession().getStatement().executeQuery(sql);
            while (resulSet.next()) {
                SLayoutBankRow oRow = new SLayoutBankRow(miClient);
                oRow.setLayoutRowType(SModSysConsts.FIN_LAY_BANK_PAY);
                oRow.setLayoutRowSubType(mnBankPaymentType);
                oRow.setPkYearId(resulSet.getInt("id_year"));
                oRow.setPkDocId(resulSet.getInt("id_doc"));
                oRow.setBizPartnerId(resulSet.getInt("id_bp"));
                oRow.setBizPartner(resulSet.getString("bp"));
                oRow.setBizPartnerCreditFiscalId(resulSet.getString("fiscal_id"));
                oRow.setBizPartnerBranch(resulSet.getString("bpb"));
                oRow.setTypeDps(resulSet.getString("code"));
                oRow.setNumberSer(resulSet.getString("f_num"));
                oRow.setDate(resulSet.getDate("dt"));
                oRow.setBizPartnerBranchCob(resulSet.getString("cob_code"));
                oRow.setSubTotal(resulSet.getDouble("f_stot_cur"));
                oRow.setTaxCharged(resulSet.getDouble("f_tax_cur"));
                oRow.setTaxRetained(resulSet.getDouble("f_ret_cur"));
                oRow.setTotal(resulSet.getDouble("f_tot_cur"));
                oRow.setBalance(new SMoney(resulSet.getDouble("f_bal_cur"), resulSet.getInt("f_id_cur"), resulSet.getDouble("f_ext_rate"),((int []) miClient.getSession().getSessionCustom().getLocalCurrencyKey())[0] ));
                oRow.setBalanceTot(new SMoney(0, resulSet.getInt("f_id_cur"), moKeyBankCurrency.getValue()[0] == moKeyDpsCurrency.getValue()[0] ? 1 : 0, ((int []) miClient.getSession().getSessionCustom().getLocalCurrencyKey())[0] ));
                oRow.setBalanceTotByBizPartner(0);
                oRow.setCurrencyKey((moKeyBankCurrency.getValue().length == 0 ? SDataReadDescriptions.getCatalogueDescription(((SClientInterface) miClient), SDataConstants.CFGU_CUR, miClient.getSession().getSessionCustom().getLocalCurrencyKey(), SLibConstants.DESCRIPTION_CODE) : SDataReadDescriptions.getCatalogueDescription(((SClientInterface) miClient), SDataConstants.CFGU_CUR, moKeyBankCurrency.getValue(), SLibConstants.DESCRIPTION_CODE)));
                oRow.setCurrencyKeyCy(resulSet.getString("cur_key"));
                oRow.setTotalVat(resulSet.getDouble("f_iva_cur"));
                oRow.setDateMaturityRo(resulSet.getDate("dt_mat"));
                oRow.setCurrencyId(mnDpsCurrency);
                renderBankAccountCredit(oRow, resulSet.getInt("fid_bpb"), mnBizPartnerBank);
                renderBizPartner(miClient.getSession().getConfigCompany().getCompanyId());
                oRow.setAccountCredit(msAccountCredit);
                oRow.setAgreement(msAgreement);
                oRow.setAgreementReference(msAgreementReference);
                oRow.setConceptCie(msConceptCie);
                oRow.setEmail(resulSet.getString("email_01"));
                oRow.setCf(0);
                oRow.setApply(1);
                oRow.setAccountDebit(moDataBizPartnerBranchBankAccount.getBankAccountNumber());
                oRow.setBizPartnerDebitFiscalId(msDebitFiscalId);
                oRow.setIsForPayment(false);
                oRow.setIsToPayed(false);
                oRow.setReference(resulSet.getString("f_num"));//Alpha
                oRow.setAccType("CLA");
                oRow.setConcept(moTextConcept.getValue());
                oRow.setDescription(moTextConcept.getValue());
                oRow.setObservations("");
                
                oRow.getAccountCredits().addAll(maAccountCredit);
                oRow.getAgreementsReferences().addAll(maAgreementsReference);
                oRow.getBranchBankAccountCredits().addAll(maBranchBankAccountsCredit);
                
                if (moDateDateDue.getValue().equals(resulSet.getDate("dt_mat")) && maBranchBankAccountsCredit.size() > 0) {
                    rows.add(oRow);
                    mltAccountCredit.add(maAccountCredit);
                }
                if (moDateDateDue.getValue().equals(resulSet.getDate("dt_mat")) && !maAgreements.isEmpty()) {
                    mltAgreementReference.add(maAgreementsReference);
                }
                if (!maAgreements.isEmpty()) {
                    mltAgreementReference.add(mhAgreeAgreeRef.get(msAgreement));
                }
                
                mvLayoutRows.add(oRow);
            }
            moGridPayments.populateGrid(rows);
            moGridPayments.createGridColumns();
            
            moGridPayments.getTable().getTableHeader().setEnabled(false);
            moGridPayments.getTable().setColumnSelectionAllowed(false);
            moGridPayments.getTable().getTableHeader().setReorderingAllowed(false);
            moGridPayments.getTable().getTableHeader().setResizingAllowed(true);            
            moGridPayments.getTable().setRowSorter(new TableRowSorter<>(moGridPayments.getModel()));
            moGridPayments.getTable().getColumnModel().getColumn(COL_APP_PAY_BANK).setCellEditor(null);
            
            if (moKeyBankCurrency.getValue().length > 0 && moKeyDpsCurrency.getValue().length > 0) {
                ((SGridColumnForm) moGridPayments.getGridColumn(COL_EXC_RATE)).setEditable(moKeyBankCurrency.getValue()[0] != moKeyDpsCurrency.getValue()[0]);
            }
            
            if (moGridPayments.getTable().getRowCount() > 0) {
                moGridPayments.renderGridRows();
                moGridPayments.setSelectedGridRow(0);
                moTableCellEditorOptions.setElements(mltAccountCredit);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            if (isNeedValidateRows && moGridPayments.getTable().getRowCount() <= 0) {
                miClient.showMsgBoxInformation("No se encontró ningún documento para los parámetros especificados.");
            }
            
            setCursor(cursor);
        }
    }
    
    public void populateGridRowsWithBeneficiaries(final boolean isNeedValidateRows) {
        String sql = "";
        ResultSet resulSet = null;
        Vector<SGridRow> rows = new Vector<>();
        mvLayoutRows = new Vector<>();
        mltAccountCredit = new ArrayList<>();
        mltAgreement= new ArrayList<>();
        mltAgreementReference= new ArrayList<>();
        mhAgreeAgreeRef = new HashMap<>();
        Cursor cursor = getCursor();

        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            sql = createSqlQuery();

            resulSet = miClient.getSession().getStatement().executeQuery(sql);
            while (resulSet.next()) {
                SLayoutBankRow oRow = new SLayoutBankRow(miClient);

                oRow.setLayoutRowType(SModSysConsts.FIN_LAY_BANK_PREPAY);
                oRow.setBizPartnerId(resulSet.getInt("id_bp"));
                oRow.setBizPartner(resulSet.getString("bp"));
                oRow.setBizPartnerKey(resulSet.getString("bp_key"));
                oRow.setBizPartnerCreditFiscalId(resulSet.getString("fiscal_id"));
                oRow.setBalanceTot(new SMoney(0d, 0));
                oRow.setBalanceTotByBizPartner(0);
                oRow.setCurrencyKey(resulSet.getString("_cur"));
                oRow.setCurrencyId(mnDpsCurrency);
                renderBizPartner(miClient.getSession().getConfigCompany().getCompanyId());
                renderBankAccountCredit(oRow, resulSet.getInt("id_bpb"), mnBizPartnerBank);
                oRow.setAgreement(msAgreement);
                oRow.setAgreementReference(msAgreementReference);
                oRow.setConceptCie(msConceptCie);
                oRow.setEmail(resulSet.getString("email_01"));

                oRow.setCf(0);
                oRow.setApply(1);
                oRow.setAccountDebit(moDataBizPartnerBranchBankAccount.getBankAccountNumber());
                oRow.setBizPartnerDebitFiscalId(msDebitFiscalId);
                oRow.setIsForPayment(false);
                oRow.setIsToPayed(false);
                oRow.setReference(resulSet.getString(7));
                oRow.setAccType("CLA");
                oRow.setConcept(moTextConcept.getValue());
                oRow.setDescription(moTextConcept.getValue());
                oRow.setAccountCreditArray(maAccountCredit);
                oRow.setAgreementsReferencesArray(maAgreementsReference);
                oRow.setBranchBankAccountCreditArray(maBranchBankAccountsCredit);
                oRow.setObservations("");
                if (!maBranchBankAccountsCredit.isEmpty()) {
                    rows.add(oRow);
                    mltAccountCredit.add(maAccountCredit);
                }
                if (!maAgreements.isEmpty()) {
                    mltAgreementReference.add(mhAgreeAgreeRef.get(msAgreement));
                }
                
                mvLayoutRows.add(oRow);
            }
            moGridPayments.populateGrid(rows);
            moGridPayments.createGridColumns();
            
            moGridPayments.getTable().setColumnSelectionAllowed(false);
            moGridPayments.getTable().getTableHeader().setReorderingAllowed(false);
            moGridPayments.getTable().getTableHeader().setResizingAllowed(true);
            moGridPayments.getTable().setRowSorter(new TableRowSorter<>(moGridPayments.getModel()));
            moGridPayments.getTable().getTableHeader().setEnabled(false);
            
            if (moGridPayments.getTable().getRowCount() > 0) {
                moGridPayments.renderGridRows();
                moGridPayments.setSelectedGridRow(0);
                moTableCellEditorOptions.setElements(mltAccountCredit);
                moTableCellAgreementReference.setElements(mltAgreementReference);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            if (isNeedValidateRows && moGridPayments.getTable().getRowCount() <= 0) {
                miClient.showMsgBoxInformation("No se encontró ningún documento para los parámetros especificados.");
            }
            setCursor(cursor);
        }
    }
       
    @Override
    public void addAllListeners() {
        moKeyLayoutBank.addItemListener(this);
        moKeyBankLayoutType.addItemListener(this);
        moKeyBankAccountCash.addItemListener(this);
        moKeyBankCurrency.addItemListener(this);
        moKeyDpsCurrency.addItemListener(this);
        jckShowOnlyDocsDateDue.addItemListener(this);
        jckShowOnlyBenefsWithAccounts.addItemListener(this);
        moTableCellEditorOptions.addCellEditorListener(this);
        
        jbPickLayoutPath.addActionListener(this);
        jbGridRowsShow.addActionListener(this);
        jbGridRowsClear.addActionListener(this);
        jbGridRowsCheckAll.addActionListener(this);
        jbGridRowsUncheckAll.addActionListener(this);
        jbPickRecord.addActionListener(this);
        jbExchangeRateReset.addActionListener(this);
        jbExchangeRateRefresh.addActionListener(this);
        jbPickExchangeRate.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        moKeyLayoutBank.removeItemListener(this);
        moKeyBankLayoutType.removeItemListener(this);
        moKeyBankAccountCash.removeItemListener(this);
        jbPickLayoutPath.removeActionListener(this);
        jbGridRowsShow.removeActionListener(this);
        jbGridRowsClear.removeActionListener(this);
        jbGridRowsCheckAll.removeActionListener(this);
        jbGridRowsUncheckAll.removeActionListener(this);
        jbPickRecord.removeActionListener(this);
        jbExchangeRateReset.removeActionListener(this);
        jbExchangeRateRefresh.removeActionListener(this);
        jckShowOnlyDocsDateDue.removeItemListener(this);
        jckShowOnlyBenefsWithAccounts.removeItemListener(this);
        
        jbPickExchangeRate.removeActionListener(this);
        moKeyBankCurrency.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        loadCatalogueBankLayoutType();
        populateLayoutBank();
        miClient.getSession().populateCatalogue(moKeyBankCurrency, SModConsts.CFGU_CUR, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyDpsCurrency, SModConsts.CFGU_CUR, SLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbBankLayout) registry;
        
        removeAllListeners();
        reloadCatalogues();
        
        if (moRegistry.isRegistryNew()) {
            jckShowOnlyDocsDateDue.setSelected(true);
            
            moRegistry.setDateLayout(miClient.getSession().getCurrentDate());
            moRegistry.setDateDue(miClient.getSession().getCurrentDate());
            
            jtfNumber.setText("");
            jtfRegistryKey.setText("");
        }
        else {
            jckShowOnlyDocsDateDue.setSelected(false);
            
            jtfNumber.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));       // same value!
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));  // same value!
        }

        // set header data:

        moDateDateLayout.setValue(moRegistry.getDateLayout());

        moKeyLayoutBank.setValue(new int[] { moRegistry.getXtaLayoutBank() });
        processLayoutBank();

        moKeyBankLayoutType.setValue(new int[] { moRegistry.getFkBankLayoutTypeId() });
        processBankLayoutType();

        moKeyBankCurrency.setValue(new int[] { moRegistry.getXtaBankCurrencyId() });
        processBankCurrency();

        moKeyBankAccountCash.setValue(new int[] { moRegistry.getFkBankCompanyBranchId(), moRegistry.getFkBankAccountCashId() });
        processBankAccountCash();

        moIntConsecutive.setValue(moRegistry.getConsecutive());
        moTextConcept.setValue(moRegistry.getConcept());

        moKeyDpsCurrency.setValue(new int[] { moRegistry.getFkDpsCurrencyId()} );
        processDpsCurrency();

        moDateDateDue.setValue(moRegistry.getDateDue());

        moCurrentRecord = null;
        renderRecord();

        // set grid rows data:

        jckShowOnlyDocsDateDue.setSelected(mnFormSubtype != SModSysConsts.FIN_LAY_BANK_ACC && (moRegistry.isRegistryNew() || mnFormSubtype == SModSysConsts.FIN_LAY_BANK_PREPAY));
        jckShowOnlyBenefsWithAccounts.setSelected(true);

        maXmlRows = new ArrayList<>();
        maLayoutBankRecords = new ArrayList<>();
        maLocks = new ArrayList<>();

        switch (mnFormSubtype) {
            case SModSysConsts.FIN_LAY_BANK_ACC:
                loadPaymentsXml();
                moDecBalanceTot.setValue(moRegistry.getAmount());
                break;
            case SModSysConsts.FIN_LAY_BANK_PAY:
            case SModSysConsts.FIN_LAY_BANK_PREPAY:
                if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_PAY) {
                    populateGridRowsWithDps(!moRegistry.isRegistryNew());
                }
                else {
                    populateGridRowsWithBeneficiaries(!moRegistry.isRegistryNew());
                }

                if (!moRegistry.getLayoutXml().isEmpty()) {
                    updateDocsXml();
                    itemStateChangedShowOnlyDocsDateDue();
                }
                break;
            default:
                break;
        }

        jtfLayoutPath.setText("");          // value is not available

        computeBalancePayment();

        setFormEditable(true);

        if (moRegistry.isRegistryNew()) {
            // registry is being created:

            switch (mnFormSubtype) {
                case SModSysConsts.FIN_LAY_BANK_PAY:
                case SModSysConsts.FIN_LAY_BANK_PREPAY:
                    actionGridRowsClear();
                    break;
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        else {
            // registry is being modified:

            switch (mnFormSubtype) {
                case SModSysConsts.FIN_LAY_BANK_PAY:
                    enableFieldsForm(false);
                    enableFieldsGrid(true, true);
                    break;
                case SModSysConsts.FIN_LAY_BANK_PREPAY:
                    enableFieldsForm(false);
                    enableFieldsGrid(false, true);
                    break;
                case SModSysConsts.FIN_LAY_BANK_ACC:
                    enableFieldsForm(false);
                    enableFieldsGrid(false, false);
                    break;
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        
        addAllListeners();
    }

    @Override
    public SDbBankLayout getRegistry() throws Exception {
        SDbBankLayout registry = moRegistry.clone();

        if (mnFormSubtype != SModSysConsts.FIN_LAY_BANK_ACC) {
            // current form is for integrate a bank layout of payments or prepayments:
            
            //registry.setPkLayBankId();
            registry.setDateLayout(moDateDateLayout.getValue());
            registry.setDateDue(moDateDateDue.getValue());
            registry.setConcept(moTextConcept.getValue());
            registry.setConsecutive(moIntConsecutive.getValue());
            registry.setAmount(moDecBalanceTot.getValue());
            //registry.setAmountPayed(...);
            //registry.setTransfers(...);
            //registry.setTransfersPayed();
            registry.setDocs(mnNumberDocs);
            //registry.setDocsPayed();
            //registry.setLayoutStatus(...);
            //registry.setLayoutText(...);
            //registry.setLayoutXml(...);
            registry.setTransactionType(mnFormSubtype);
            //registry.setAuthorizationRequests(...);
            //registry.setClosedPayment(...);
            registry.setDeleted(false);
            registry.setFkBankLayoutTypeId(mnBankLayoutType);
            registry.setFkBankCompanyBranchId(moDataAccountCash.getPkCompanyBranchId());
            registry.setFkBankAccountCashId(moDataAccountCash.getPkAccountCashId());
            registry.setFkDpsCurrencyId(moKeyDpsCurrency.getValue()[0]);
            /*
            registry.setFkUserClosedPaymentId();
            registry.setFkUserInsertId();
            registry.setFkUserUpdateId();
            registry.setTsUserClosedPayment();
            registry.setTsUserInsert();
            registry.setTsUserUpdate();
            */

            registry.setAuxTitle(moKeyBankLayoutType.getSelectedItem().getItem().toLowerCase());
            registry.setAuxLayoutPath(jtfLayoutPath.getText());

            registry.setPostSaveTarget(registry);
            registry.setPostSaveMethod(registry.getClass().getMethod("writeLayout", SGuiClient.class));
            registry.setPostSaveMethodArgs(new Object[] { miClient });
        }
        
        registry.getLayoutBankXmlRows().addAll(maXmlRows);
         
        if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_ACC) {
            for (SGridRow row : moGridPayments.getModel().getGridRows()) {
                SLayoutBankPaymentRow paymentRow = (SLayoutBankPaymentRow) row;

                if (paymentRow.getLayoutBankRecordKey() != null || paymentRow.getLayoutBankRecordKeyOld() != null) {
                    registry.getLayoutBankPaymentRows().add(paymentRow);
                }
            }
            
            registry.setTransfers(moGridPayments.getModel().getGridRows().size());
        }
        
        registry.setExchangeRate(1d);
        registry.setXtaBankPaymentType(mnBankPaymentType);
        
        if (!maLocks.isEmpty()) {
            registry.getLocks().addAll(maLocks);
        }
        
        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            if (jtfLayoutPath.getText().isEmpty() && jbPickLayoutPath.isEnabled()) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlLayoutPath) + "'.");
                validation.setComponent(jbPickLayoutPath);
            }
            
            if (validation.isValid()) {
                try {
                    for (SSrvLock lock : maLocks) {
                        SSrvUtils.verifyLockStatus(miClient.getSession(), lock);
                    }
                }
                catch (Exception e) {
                    validation.setMessage("No fue posible validar el acceso exclusivo al registro de una de las pólizas seleccionadas." + e);
                    validation.setComponent(jbCancel);
                }
            }
        }

        return validation;
    }
    
    @Override
    public void actionSave() {
        try  {
            if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_ACC) {
                super.actionSave();
            }
            else {
                if (validateDebtsToPay()) {
                    createLayoutXml();
                    super.actionSave();
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    @Override
    public void actionCancel() {
        if (jbCancel.isEnabled()) {
            try {
                for (SSrvLock lock : maLocks) {
                    SSrvUtils.releaseLock(miClient.getSession(), lock);
                }
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
            mnFormResult = SGuiConsts.FORM_RESULT_CANCEL;
            dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbPickRecord) {
                actionPickRecord();
            }
            else if (button == jbPickLayoutPath) {
                actionPickLayoutPath();
            }
            else if (button == jbPickExchangeRate) {
                actionPickExchangeRate();
            }
            else if (button == jbGridRowsShow) {
                actionGridRowsShow();
            }
            else if (button == jbGridRowsClear) {
                actionGridRowsClear();
            }
            else if (button == jbExchangeRateReset) {
                actionExchangeRateResetOriginal();
            }
            else if (button == jbExchangeRateRefresh) {
                actionExchangeRateSetCurrent();
            }
            else if (button == jbGridRowsCheckAll) {
                actionGridRowsCheckAll();
            }
            else if (button == jbGridRowsUncheckAll) {
                actionGridRowsUncheckAll();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof SBeanFieldKey) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                SBeanFieldKey field = (SBeanFieldKey)  e.getSource();

                if (field == moKeyLayoutBank) {
                    itemStateChangedLayoutBank();
                }
                else if (field == moKeyBankLayoutType) {
                    itemStateChangedBankLayoutType();
                }
                else if (field == moKeyBankAccountCash) {
                    itemStateChangedBankAccountCash();
                }
                else if (field == moKeyBankCurrency) {
                    itemStateChangedBankCurrency();
                }
                else if (field == moKeyDpsCurrency) {
                    itemStateChangedDpsCurrency();
                }
            }
        }
        else if (e.getSource() instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox) e.getSource();

            if (checkBox == jckShowOnlyDocsDateDue) {
                itemStateChangedShowOnlyDocsDateDue();
            }
            else if (checkBox == jckShowOnlyBenefsWithAccounts) {
                itemStateChangedShowOnlyBenefsWithAccounts();
            }
        }
    }
    
    @Override
    public void editingStopped(ChangeEvent e) {
        switch (moGridPayments.getTable().getSelectedColumn()) {
            case COL_APP_PAY:
                if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_ACC) {
                    processEditingAppPayment();
                }
                else if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_PREPAY) {
                    processEditingStoppedBalance();
                }
                break;
            case COL_BANK_ACC_OR_AGREE:
                if (mnBankPaymentType == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                    processEditingAgreement(moGridPayments.getTable().getSelectedRow());
                }
                break;
            case COL_APP:
                if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_ACC || mnFormSubtype == SModSysConsts.FIN_LAY_BANK_PREPAY) {
                    processEditingAppPayment();
                }
                else if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_PREPAY) {
                    processEditingAgreement(moGridPayments.getTable().getSelectedRow());
                }
                break;
            case COL_BAL:
                processEditingStoppedBalance();
                break;
            case COL_EXC_RATE:
                processEditingStoppedBalance();
                break;
            case COL_AGREE:
            case COL_AGREE_REF:
                if (mnBankPaymentType == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                    processEditingAgreement(moGridPayments.getTable().getSelectedRow());
                }
                break;
            default:
        }
    }

    @Override
    public void editingCanceled(ChangeEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private class BankLayoutType {
        public int BankLayoutType;
        public int BankPaymentType;
        public int LayoutBank;
        public int BizPartnerBank;
        
        /**
         * Creates new BankLayoutType.
         * @param bankLayoutType Bank layout type ID, PK of erp.finu_tp_lay_bank, i.e., bank & layout payment type.
         * @param bankPaymentType Bank payment type ID, PK of erp.fins_tp_pay_bank, i.e., layout payment type.
         * @param layoutBank Layout bank ID, virtual PK defined on erp.fins_tp_pay_bank, i.e., fixed bank's ID for layout purposes.
         * @param bizPartnerBank Bank ID, PK of erp.bpsu_bp, i.e., bank's business partner ID.
         */
        public BankLayoutType(int bankLayoutType, int bankPaymentType, int layoutBank, int bizPartnerBank) {
            BankLayoutType = bankLayoutType;
            BankPaymentType = bankPaymentType;
            LayoutBank = layoutBank;
            BizPartnerBank = bizPartnerBank;
        }
    }
}

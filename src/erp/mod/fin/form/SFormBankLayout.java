/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.form;

import cfd.DCfdUtils;
import cfd.ver33.DCfdi33Consts;
import erp.SClientUtils;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.table.STableCellEditorOptions;
import erp.lib.table.STableConstants;
import erp.mbps.data.SDataBizPartnerBranchBankAccount;
import erp.mcfg.data.SCfgUtils;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SFinUtilities;
import erp.mfin.form.SDialogRecordPicker;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SDbBankLayout;
import erp.mod.fin.db.SFinConsts;
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
import erp.mod.fin.util.SBankLayoutConsts;
import erp.mtrn.data.SCfdUtilsHandler;
import erp.redis.SLockUtils;
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
import sa.lib.SLibTimeUtils;
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
import sa.lib.srv.SLock;
import sa.lib.xml.SXmlElement;

/**
 *
 * @author Juan Barajas, Uriel Castañeda, Alfredo Pérez, Sergio Flores, Isabel Servín, Adrián Avilés, Claudio Peña
 */
public class SFormBankLayout extends SBeanForm implements ActionListener, ItemListener, CellEditorListener {

    private static final int COL_BANK_ACC_CHECK = 2;
    
    private static final int COL_TRN_TP_PAY_CHECK = 5;
    private static final int COL_TRN_TP_PAY_PAY = 7;
    private static final int COL_TRN_TP_PAY_EXR = 9;
    private static final int COL_TRN_TP_PAY_AGREE = 12;
    private static final int COL_TRN_TP_PAY_AGREE_REF = 13;
    
    private static final int COL_TRN_TP_PREPAY_PREPAY = 2;
    private static final int COL_TRN_TP_PREPAY_AGREE = 4;
    private static final int COL_TRN_TP_PREPAY_AGREE_REF = 5;
    
    private JCheckBox jckShowOnlyDocsDateDue;
    private JCheckBox jckShowOnlyBenefsWithAccounts;
    private JButton jbExchangeRateReset;
    private JButton jbExchangeRateRefresh;
    private JButton jbGridRowsCheckAll;
    private JButton jbGridRowsUncheckAll;
    
    private STableCellEditorOptions moCellEditorOptions;
    private STableCellEditorOptions moCellEditorOptionsAgreementReference;

    private int mnLayoutBank;
    private int mnBankLayoutTypeId;
    private int mnBankPaymentTypeId;
    private int mnBizPartnerBankId;
    private int mnBankLayoutCurrencyId;
    private int mnDpsCurrencyId;
    
    private SDbBankLayout moRegistry;
    private SDataAccountCash moDataAccountCash;
    private SDataBizPartnerBranchBankAccount moDataBizPartnerBranchBankAccount;
    private SDataRecord moCurrentRecord;
    
    private ArrayList<SGuiItem> maBankLayoutTypes;
    private SGridPaneForm moGridPayments;
    private ArrayList<SLayoutBankRow> maAllLayoutBankRows; // for editing bank layouts
    private List<ArrayList<SGuiItem>> mltAccountCredits;
    private List<ArrayList<SGuiItem>> mltAgreementsReferences;

    private int mnCfgParamCfdRequired;
    private erp.mfin.form.SDialogRecordPicker moDialogRecordPicker;
    private ArrayList<SLayoutBankRecord> maLayoutBankRecords;
    
    private int mnSelectedRows;
    private int[] moBankAccPk;
    private String msAccountCredit;
    private String msAgreement;
    private String msAgreementReference;
    private String msConceptCie;
    private ArrayList<SGuiItem> maAgreementGuiItems;
    private ArrayList<SGuiItem> maAgreementsReferenceGuiItems;
    private ArrayList<SGuiItem> maBeneficiaryAccountGuiItems;
    private ArrayList<SDataBizPartnerBranchBankAccount> maBizPartnerBranchBankAccounts;
    private HashMap<String, ArrayList<SGuiItem>> moAgreementReferencesMap;
    /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
    private ArrayList<SSrvLock> maLocks;
    */
    /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
    private ArrayList<SRedisLock> maRedisLocks;
    */
    private ArrayList<SLock> maSLocks;
    private boolean mbShowConfirmCloseDialog;
    
    /**
     * Creates new form SFormLayoutBank
     * @param client GUI client.
     * @param formSubtype Options supported: SModSysConsts.FINX_LAY_BANK_ACC, SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY and SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY.
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

        jpRegistry = new javax.swing.JPanel();
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
        jlBankLayoutCurrency = new javax.swing.JLabel();
        moKeyBankLayoutCurrency = new sa.lib.gui.bean.SBeanFieldKey();
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
        jlBalanceTotal = new javax.swing.JLabel();
        moDecBalanceTotal = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlBalanceTotalPayed = new javax.swing.JLabel();
        moDecBalanceTotalPayed = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel17 = new javax.swing.JPanel();
        jlLayoutPath = new javax.swing.JLabel();
        jtfLayoutPath = new javax.swing.JTextField();
        jbPickLayoutPath = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jpRegistry.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpRegistry.setLayout(new java.awt.GridLayout(1, 2, 5, 0));

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

        jlBankLayoutCurrency.setText("Moneda origen:*");
        jlBankLayoutCurrency.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel8.add(jlBankLayoutCurrency);

        moKeyBankLayoutCurrency.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel8.add(moKeyBankLayoutCurrency);

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

        jpRegistry.add(jPanel1);

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

        jpRegistry.add(jPanel14);

        getContentPane().add(jpRegistry, java.awt.BorderLayout.NORTH);

        jpSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle:"));
        jpSettings.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfRows.setEditable(false);
        jtfRows.setText("000,000/000,000");
        jtfRows.setToolTipText("Renglón actual");
        jtfRows.setFocusable(false);
        jtfRows.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel12.add(jtfRows);

        jPanel2.add(jPanel12, java.awt.BorderLayout.WEST);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBalanceTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBalanceTotal.setText("Total layout:");
        jlBalanceTotal.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel15.add(jlBalanceTotal);

        moDecBalanceTotal.setEditable(false);
        jPanel15.add(moDecBalanceTotal);

        jlBalanceTotalPayed.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlBalanceTotalPayed.setText("Total pago:");
        jlBalanceTotalPayed.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel15.add(jlBalanceTotalPayed);

        moDecBalanceTotalPayed.setEditable(false);
        jPanel15.add(moDecBalanceTotalPayed);

        jPanel2.add(jPanel15, java.awt.BorderLayout.EAST);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLayoutPath.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlLayoutPath.setText("Ruta layout:*");
        jlLayoutPath.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel17.add(jlLayoutPath);

        jtfLayoutPath.setEditable(false);
        jtfLayoutPath.setFocusable(false);
        jtfLayoutPath.setPreferredSize(new java.awt.Dimension(400, 23));
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

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
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
    private javax.swing.JLabel jlBalanceTotal;
    private javax.swing.JLabel jlBalanceTotalPayed;
    private javax.swing.JLabel jlBankAccountCash;
    private javax.swing.JLabel jlBankLayoutCurrency;
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
    private javax.swing.JPanel jpRegistry;
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
    private sa.lib.gui.bean.SBeanFieldDecimal moDecBalanceTotal;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecBalanceTotalPayed;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecExchangeRate;
    private sa.lib.gui.bean.SBeanFieldInteger moIntConsecutive;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBankAccountCash;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBankLayoutCurrency;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBankLayoutType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyDpsCurrency;
    private sa.lib.gui.bean.SBeanFieldKey moKeyLayoutBank;
    private sa.lib.gui.bean.SBeanFieldText moTextConcept;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods:
     */

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 640);

        try {
            mnCfgParamCfdRequired = SLibUtils.parseInt(SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_FIN_BANK_LAYOUT_CFD_REQ));
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        moDialogRecordPicker = new SDialogRecordPicker((SClientInterface) miClient, SDataConstants.FINX_REC_USER);
        
        moDateDateLayout.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateLayout), true);
        moKeyLayoutBank.setKeySettings(miClient, SGuiUtils.getLabelName(jlLayoutBank), true);
        moKeyBankLayoutType.setKeySettings(miClient, SGuiUtils.getLabelName(jlBankLayoutType), true);
        moKeyBankLayoutCurrency.setKeySettings(miClient, SGuiUtils.getLabelName(jlBankLayoutCurrency), true);
        moKeyBankAccountCash.setKeySettings(miClient, SGuiUtils.getLabelName(jlBankAccountCash), true);
        moIntConsecutive.setIntegerSettings(SGuiUtils.getLabelName(jlConsecutive), SGuiConsts.GUI_TYPE_INT, true);
        moTextConcept.setTextSettings(SGuiUtils.getLabelName(jlConcept), 255);
        moKeyDpsCurrency.setKeySettings(miClient, SGuiUtils.getLabelName(jlDpsCurrency), true);
        moDecExchangeRate.setDecimalSettings(SGuiUtils.getLabelName(jlExchangeRate), SGuiConsts.GUI_TYPE_DEC_EXC_RATE, true);
        moDateDateDue.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateDue), true);
        moDecBalanceTotal.setDecimalSettings(SGuiUtils.getLabelName(jlBalanceTotal), SGuiConsts.GUI_TYPE_DEC_AMT, false);
        moDecBalanceTotalPayed.setDecimalSettings(SGuiUtils.getLabelName(jlBalanceTotalPayed), SGuiConsts.GUI_TYPE_DEC_AMT, false);
        
        moFields.addField(moDateDateLayout);
        moFields.addField(moKeyLayoutBank);
        moFields.addField(moKeyBankLayoutType);
        moFields.addField(moKeyBankLayoutCurrency);
        moFields.addField(moKeyBankAccountCash);
        moFields.addField(moIntConsecutive);
        moFields.addField(moTextConcept);
        moFields.addField(moKeyDpsCurrency);
        moFields.addField(moDecExchangeRate);
        moFields.addField(moDateDateDue);
        moFields.setFormButton(jbGridRowsShow);

        switch (mnFormSubtype) {
            case SModSysConsts.FINX_LAY_BANK_ACC:
                jbGridRowsShow.setText("Mostrar renglones");
                jbGridRowsClear.setText("Limpiar renglones");
                break;

            case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
                jbGridRowsShow.setText("Mostrar documentos");
                jbGridRowsClear.setText("Limpiar documentos");
                break;

            case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
                jbGridRowsShow.setText("Mostrar beneficiarios");
                jbGridRowsClear.setText("Limpiar beneficiarios");
                break;

            default:
        }

        jckShowOnlyDocsDateDue = new JCheckBox("Solo documentos del vecimiento",true);
        jckShowOnlyDocsDateDue.setPreferredSize(new Dimension(200, 23));
        
        jckShowOnlyBenefsWithAccounts = new JCheckBox("Solo cuentas bancarias configuradas");
        jckShowOnlyBenefsWithAccounts.setPreferredSize(new Dimension(200, 23));

        jbExchangeRateReset = new JButton("TC original");
        jbExchangeRateReset.setToolTipText("Poner TC original del documento al renglón");
        jbExchangeRateReset.setMargin(new Insets (2, 2, 2, 2));
        jbExchangeRateReset.setPreferredSize(new java.awt.Dimension(85, 23));

        jbExchangeRateRefresh = new JButton("TC actual");
        jbExchangeRateRefresh.setToolTipText("Poner TC actual al renglón");
        jbExchangeRateRefresh.setMargin(new Insets (2, 2, 2, 2));
        jbExchangeRateRefresh.setPreferredSize(new java.awt.Dimension(85, 23));

        jbGridRowsCheckAll = new JButton("Todo");
        jbGridRowsCheckAll.setToolTipText("Seleccionar todo");
        jbGridRowsCheckAll.setMargin(new Insets (2, 2, 2, 2));
        jbGridRowsCheckAll.setPreferredSize(new java.awt.Dimension(85, 23));

        jbGridRowsUncheckAll = new JButton("Nada");
        jbGridRowsUncheckAll.setToolTipText("Selecionar nada");
        jbGridRowsUncheckAll.setMargin(new Insets (2, 2, 2, 2));
        jbGridRowsUncheckAll.setPreferredSize(new java.awt.Dimension(85, 23));

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
                    case SModSysConsts.FINX_LAY_BANK_ACC:
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Proveedor",200));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Clave proveedor", 50));
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Aplicar pago", moGridPayments.getTable().getDefaultEditor(Boolean.class));
                        column.setEditable(true);
                        gridColumnsForm.add(column);
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Monto a pagar $"));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda"));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_EXC_RATE, "Tipo de cambio"));
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

                    case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
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
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Cuenta/Convenio", 120, moCellEditorOptions);
                        column.setEditable(true);
                        column.setApostropheOnCsvRequired(true);
                        gridColumnsForm.add(column);
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Referencia", 120, moCellEditorOptionsAgreementReference);
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

                    case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Proveedor", 200));
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Clave proveedor", 50));
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Monto a pagar $", moGridPayments.getTable().getDefaultEditor(Double.class));
                        column.setEditable(true);
                        gridColumnsForm.add(column);
                        gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda"));
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Cuenta/Convenio", 120, moCellEditorOptions);
                        column.setEditable(true);
                        column.setApostropheOnCsvRequired(true);
                        gridColumnsForm.add(column);
                        column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Referencia", 120, moCellEditorOptionsAgreementReference);
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
                moCellEditorOptions.addCellEditorListener(SFormBankLayout.this);
                moCellEditorOptionsAgreementReference.addCellEditorListener(SFormBankLayout.this);
                
                mbShowConfirmCloseDialog = true;

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
        
        moCellEditorOptions = new STableCellEditorOptions((SClientInterface) miClient);
        moCellEditorOptionsAgreementReference = new STableCellEditorOptions((SClientInterface) miClient, true);
    }
    
    private boolean isModeForAccounting() {
        return mnFormSubtype == SModSysConsts.FINX_LAY_BANK_ACC;
    }

    private boolean isModeForTransfers() {
        return isModeForTransfersOfPayments() || isModeForTransfersOfPrepayments();
    }

    private boolean isModeForTransfersOfPayments() {
        return mnFormSubtype == SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY;
    }

    private boolean isModeForTransfersOfPrepayments() {
        return mnFormSubtype == SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY;
    }

    private boolean isEditableConsecutive() {
        return mnLayoutBank == SFinConsts.LAY_BANK_BBAJ;
    }

    private boolean isEditableConcept() {
        boolean editable = false;

        switch (mnLayoutBank) {
            case SFinConsts.LAY_BANK_HSBC:
                editable = mnBankPaymentTypeId == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD;
                break;
            case SFinConsts.LAY_BANK_SANT:
                editable = true;
                break;
            case SFinConsts.LAY_BANK_BBAJ:
                break;
            case SFinConsts.LAY_BANK_BBVA:
                editable = false;
                break;
            case SFinConsts.LAY_BANK_CITI:
                break;
            default:
        }

        return editable;
    }

    private boolean isEditableGridReferenceConcept() {
        return (mnBankPaymentTypeId == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE);
    }

    private boolean isExchangeRateNotRequired() {
        return !(!miClient.getSession().getSessionCustom().isLocalCurrency(moKeyDpsCurrency.getValue()) || !miClient.getSession().getSessionCustom().isLocalCurrency(moKeyBankLayoutCurrency.getValue()));
    }

    private void processLayoutBank() {
        if (moKeyLayoutBank.getSelectedIndex() <= 0) {
            mnLayoutBank = 0;
        }
        else {
            mnLayoutBank = moKeyLayoutBank.getValue()[0];
        }
        
        loadBankLayoutTypes();
    }
    
    private void processBankLayoutType() {
        if (moKeyBankLayoutType.getSelectedIndex() <= 0) {
            mnBankLayoutTypeId = 0;
            mnBankPaymentTypeId = 0;
            mnBizPartnerBankId = 0;
            
            moKeyBankLayoutCurrency.setEnabled(false);
            moKeyBankLayoutCurrency.resetField();
        }
        else {
            BankLayoutType bankLayoutType = (BankLayoutType) ((SGuiItem) moKeyBankLayoutType.getSelectedItem()).getComplement();
            mnBankLayoutTypeId = bankLayoutType.BankLayoutType; // same as key of selected item in combo box for bank layout type
            mnBankPaymentTypeId = bankLayoutType.BankPaymentType;
            mnBizPartnerBankId = bankLayoutType.BizPartnerBankId;
            
            moKeyBankLayoutCurrency.setEnabled(true);
        }
        
        renderSettingsBankLayoutType();
        processBankLayoutCurrency();
    }
    
    private void processBankLayoutCurrency() {
        if (moKeyBankLayoutCurrency.getSelectedIndex() <= 0) {
            mnBankLayoutCurrencyId = 0;
            
            moKeyDpsCurrency.setEnabled(false);
            moKeyDpsCurrency.resetField();
        }
        else {
            mnBankLayoutCurrencyId = moKeyBankLayoutCurrency.getValue()[0];
            
            moKeyDpsCurrency.setEnabled(isModeForTransfersOfPayments());
            moKeyDpsCurrency.setValue(moKeyBankLayoutCurrency.getValue());
        }
        
        loadBankAccountCashs();
    }
    
    private void processBankAccountCash() {
        if (moKeyBankAccountCash.getSelectedIndex() <= 0) {
            moDataAccountCash = null;
            moDataBizPartnerBranchBankAccount = null;
        }
        else {
            moDataAccountCash = (SDataAccountCash) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.FIN_ACC_CASH, moKeyBankAccountCash.getValue(), SLibConstants.EXEC_MODE_SILENT);
            moDataBizPartnerBranchBankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BANK_ACC, new int[] { moDataAccountCash.getFkBizPartnerBranchId_n(), moDataAccountCash.getFkBankAccountId_n() }, SLibConstants.EXEC_MODE_SILENT);
        }
    }
    
    private void processDpsCurrency() {
        if (moKeyDpsCurrency.getSelectedIndex() <= 0) {
            mnDpsCurrencyId = 0;
        }
        else {
            mnDpsCurrencyId = moKeyDpsCurrency.getValue()[0];
        }
        
        renderSettingsDpsCurrency();
    }
    
    private void renderRecord() {
        if (moCurrentRecord == null) {
            jtfRecordDate.setText("");
            jtfRecordBookkeepingCenter.setText("");
            jtfRecordCompanyBranch.setText("");
            jtfRecordNumber.setText("");
        }
        else {
            jtfRecordDate.setText(SLibUtils.DateFormatDate.format(moCurrentRecord.getDate()));
            jtfRecordBookkeepingCenter.setText(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.FIN_BKC, new int[] { moCurrentRecord.getPkBookkeepingCenterId() }, SLibConstants.DESCRIPTION_CODE));
            jtfRecordCompanyBranch.setText(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.BPSU_BPB, new int[] { moCurrentRecord.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
            jtfRecordNumber.setText(moCurrentRecord.getRecordNumber());
        }
    }
    
    /*
     * Support methods for private methods:
     */

    @SuppressWarnings("unchecked")
    private void loadBankLayoutTypes() {
        moKeyBankLayoutType.setEnabled(false); // prevent item-state-changed events
        moKeyBankLayoutType.removeAllItems();
        
        if (moKeyLayoutBank.getSelectedIndex() > 0) {
            // add combo box items:
            for (int index = 0; index < maBankLayoutTypes.size(); index++) {
                SGuiItem item = maBankLayoutTypes.get(index);
                if (index == 0 || SLibUtils.compareKeys(moKeyLayoutBank.getValue(), new int[] { ((BankLayoutType) item.getComplement()).BankLayout })) {
                    moKeyBankLayoutType.addItem(item);
                }
            }
            
            moKeyBankLayoutType.setEnabled(true);
        }
    }
    
    private void loadBankAccountCashs() {
        moKeyBankAccountCash.setEnabled(false); // prevent item-state-changed events
        moKeyBankAccountCash.removeAllItems();
        
        if (moKeyBankLayoutCurrency.getSelectedIndex() > 0) {
            // add combo box items:
            miClient.getSession().populateCatalogue(moKeyBankAccountCash, SModConsts.FIN_ACC_CASH, SModConsts.FINX_ACC_CASH_BANK, new SGuiParams(new int[] { mnBankLayoutCurrencyId, mnBizPartnerBankId }));
            
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

    private void renderSettingsDpsCurrency(){
        double exr = 0;
        boolean enable = false;
        
        if (moKeyDpsCurrency.getSelectedIndex() <= 0) {
            exr = 0;
        }
        else if (moRegistry != null && moRegistry.getExchangeRateAcc() != 0) { 
            exr = moRegistry.getExchangeRateAcc();
        }
        else {
            if (isExchangeRateNotRequired()) {
                exr = 1; // document's currency is local or is equal to bank's currency
            }
            else {
                try {
                    exr = SDataUtilities.obtainExchangeRate((SClientInterface) miClient, mnDpsCurrencyId, moDateDateLayout.getValue());
                    enable = true;
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
        
        moDecExchangeRate.setValue(exr);
        moDecExchangeRate.setEditable(enable);
        jbPickExchangeRate.setEnabled(enable);
        jbExchangeRateReset.setEnabled(enable);
        jbExchangeRateRefresh.setEnabled(enable);
    }
    
    /*
     * Methods for bank layout accounting:
     */

    /**
     * Populate grid with payments from XML of bank layout.
     * Called only in method setRegistry().
     */
    private void populateGridWithPaymentsFromXml() {
        try {
            moRegistry.parseBankLayoutXml(miClient, false);
            
            moGridPayments.populateGrid(new Vector<>(moRegistry.getAuxLayoutBankPaymentRows()));
            moGridPayments.getTable().getTableHeader().setEnabled(false);
            
            if (moGridPayments.getTable().getRowCount() > 0) {
                moGridPayments.renderGridRows();
                moGridPayments.setSelectedGridRow(0);
                moCellEditorOptions.setElements(mltAccountCredits);
                moCellEditorOptionsAgreementReference.setElements(mltAgreementsReferences);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private boolean checkRecord(final boolean calledFromCheckAllEvent) throws Exception {
        SLayoutBankPaymentRow paymentRow = null;
        int distinctRecords = 0;

        if (moCurrentRecord == null) {
            jbPickRecord.requestFocus();
            throw new Exception(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlRecord) + "'.");
        }
        else if (calledFromCheckAllEvent) {
            for (SGridRow gridRow : moGridPayments.getModel().getGridRows()) {
                paymentRow = (SLayoutBankPaymentRow) gridRow;

                if (paymentRow.isForPayment()) {
                    if (paymentRow.getLayoutBankRecordKey() != null) {
                        if (!SLibUtils.compareKeys(paymentRow.getLayoutBankRecordKey().getPrimaryKey(), moCurrentRecord.getPrimaryKey())) {
                            distinctRecords++;
                        }
                    }
                }
            }

            for (SLayoutBankRecord bankRecordRow : maLayoutBankRecords) {
                if (!SLibUtils.compareKeys(bankRecordRow.getLayoutBankRecordKey().getPrimaryKey(), moCurrentRecord.getPrimaryKey())) {
                    distinctRecords++;
                }
            }

            if (distinctRecords != 0) {
                if (miClient.showMsgBoxConfirm("Existen '" + distinctRecords + "' transferencias en pólizas distintas, a las cuáles se la reemplazará la póliza por la '" + moCurrentRecord.getRecordPeriod() + "-" + moCurrentRecord.getRecordNumber() + "'.\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.NO_OPTION) {
                   throw new Exception("Existen '" + distinctRecords + "' transferencias en pólizas distintas.");
                }
            }
        }
        return true;
    }

    private void validatePayments() throws Exception {
        int payments = 0;
        for (SGridRow gridRow : moGridPayments.getModel().getGridRows()) {
            SLayoutBankPaymentRow layoutBankPaymentRow = (SLayoutBankPaymentRow) gridRow;
            if (layoutBankPaymentRow.isForPayment()) {
                payments++;
                if (!isModeForTransfersOfPrepayments()) {
                    if (!miClient.getSession().getSessionCustom().isLocalCurrency(moKeyBankLayoutCurrency.getValue()) || !miClient.getSession().getSessionCustom().isLocalCurrency(moKeyDpsCurrency.getValue())) {
                        miClient.showMsgBoxInformation("Puede ser necesario realizar ajustes por diferencia cambiaria por liquidación de saldos" +
                        (!miClient.getSession().getSessionCustom().isLocalCurrency(moKeyBankLayoutCurrency.getValue()) && miClient.getSession().getSessionCustom().isLocalCurrency(moKeyDpsCurrency.getValue()) ? 
                                ",\nel tipo de cambio " + SLibUtils.getDecimalFormatExchangeRate().format(moDecExchangeRate.getValue()) + " es diferente, al menos, "
                                + "a uno de los tipos cambiarios individuales de los pagos." : "."));
                        break;
                    }
                }
            }
        }

        if (payments == 0) {
            if (miClient.showMsgBoxConfirm("¿Está seguro que no desea aplicar ningún pago?") != JOptionPane.YES_OPTION) {
                throw new Exception("Se debería aplicar al menos un pago.");
            }
        }
    }

    /*
     * Methods for bank layout payments:
     */
    
    /**
     * Populate grid with rows of documents for payments.
     * @param showValidationOfExistingRows 
     */
    private void populateGridWithDocsFromDb(final boolean showValidationOfExistingRows) {
        Vector<SGridRow> gridRows = new Vector<>();

        maAllLayoutBankRows = new ArrayList<>();
        mltAccountCredits = new ArrayList<>();
        mltAgreementsReferences = new ArrayList<>();
        moAgreementReferencesMap = new HashMap<>();

        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            String sql = "SELECT b.id_bp, b.bp, b.fiscal_id, d.exc_rate AS f_ext_rate, bct.bp_key, bpb_con.email_01, cob.code AS cob_code, " +
                    "d.id_year, d.id_doc, d.dt AS dt, CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                    "d.stot_r AS f_stot, d.stot_cur_r AS f_stot_cur, " +
                    "d.tax_charged_r AS f_tax, d.tax_charged_cur_r AS f_tax_cur, " +
                    "d.tax_retained_r AS f_ret, d.tax_retained_cur_r AS f_ret_cur, " +
                    "d.tot_r AS f_tot, d.tot_cur_r AS f_tot_cur, d.fid_bpb, dt.code, c.id_cur AS f_id_cur, c.cur_key, " +
                    "SUM(re.credit - re.debit) AS f_bal, SUM(re.credit_cur - re.debit_cur) AS f_bal_cur, " +
                    "COALESCE((SELECT SUM(tax.tax) FROM trn_dps_ety AS de " +
                    "INNER JOIN trn_dps_ety_tax AS tax ON de.id_year = tax.id_year AND de.id_doc = tax.id_doc AND de.id_ety = tax.id_ety AND tax.id_tax_bas = " + SDataConstantsSys.FINU_TAX_BAS_VAT + " " +
                    "WHERE d.id_year = de.id_year AND d.id_doc = de.id_doc AND de.b_del = 0), 0) AS f_iva, "+
                    "COALESCE((SELECT SUM(tax.tax_cur) FROM trn_dps_ety AS de " +
                    "INNER JOIN trn_dps_ety_tax AS tax ON de.id_year = tax.id_year AND de.id_doc = tax.id_doc AND de.id_ety = tax.id_ety AND tax.id_tax_bas = " + SDataConstantsSys.FINU_TAX_BAS_VAT + " " +
                    "WHERE d.id_year = de.id_year AND d.id_doc = de.id_doc AND de.b_del = 0), 0) AS f_iva_cur, ADDDATE(d.dt_start_cred, d.days_cred) AS dt_mat, " +
                    "x.uuid AS xml_uuid, x.xml_rfc_emi, x.xml_rfc_rec, x.xml_tot, x.fid_tp_cfd AS xml_type, cx.doc_xml " +
                    "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                    "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                    "r.id_year = " + SLibTimeUtils.digestYear(moDateDateDue.getValue())[0] + " AND r.b_del = 0 AND re.b_del = 0 AND " +
                    "re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + " " +
                    "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " +
                    "INNER JOIN erp.bpsu_bp_ct AS bct ON re.fid_bp_nr = bct.id_bp AND bct.id_ct_bp = re.fid_tp_sys_mov_xxx " +
                    "INNER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc AND d.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
                    "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                    "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb " +
                    "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur AND d.fid_cur = " + mnDpsCurrencyId + " " + // this member is zero when associated combo box is clear, then result set will be empty
                    "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
                    "LEFT OUTER JOIN erp.bpsu_bpb_con AS bpb_con ON bpb.id_bpb = bpb_con.id_bpb AND bpb_con.id_con = " + SDataConstantsSys.BPSS_TP_CON_ADM + " " +
                    "LEFT OUTER JOIN trn_cfd AS x ON d.id_year = x.fid_dps_year_n AND d.id_doc = x.fid_dps_doc_n " +
                    "LEFT OUTER JOIN " + SClientUtils.getComplementaryDdName((SClientInterface) miClient) + ".trn_cfd AS cx ON x.id_cfd = cx.id_cfd " + 
                    "WHERE EXISTS (SELECT * FROM erp.bpsu_bank_acc AS ac WHERE bpb.id_bpb = ac.id_bpb AND ac.fid_bank " + 
                    (SLibUtils.belongsTo(mnBankPaymentTypeId, new int[] { SDataConstantsSys.FINS_TP_PAY_BANK_THIRD, SDataConstantsSys.FINS_TP_PAY_BANK_AGREE }) ? "= " : "<> ") + mnBizPartnerBankId + ") " +
                    "GROUP BY b.id_bp, b.bp, b.fiscal_id, d.exc_rate, bct.bp_key, bpb_con.email_01, cob.code, " +
                    "d.id_year, d.id_doc, d.dt, d.num_ser, d.num, " +
                    "d.stot_r, d.stot_cur_r, " +
                    "d.tax_charged_r, d.tax_charged_cur_r, " +
                    "d.tax_retained_r, d.tax_retained_cur_r, " +
                    "d.tot_r, d.tot_cur_r, d.fid_bpb, dt.code, c.id_cur, c.cur_key " +
                    "HAVING f_bal > 0 " +
                    "ORDER BY bp, id_bp, f_num, dt, id_year, id_doc;";
            
            try (ResultSet resulSet = miClient.getSession().getStatement().executeQuery(sql)) {
                while (resulSet.next()) {
                    SLayoutBankRow layoutBankRow = new SLayoutBankRow(miClient, SLayoutBankRow.MODE_FORM_EDITION);
                    
                    layoutBankRow.setTransactionType(SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY);
                    layoutBankRow.setPaymentType(mnBankPaymentTypeId);
                    layoutBankRow.setDpsYearId(resulSet.getInt("id_year"));
                    layoutBankRow.setDpsDocId(resulSet.getInt("id_doc"));
                    layoutBankRow.setBizPartnerId(resulSet.getInt("id_bp"));
                    layoutBankRow.setBizPartner(resulSet.getString("bp"));
                    layoutBankRow.setBizPartnerKey(resulSet.getString("bp_key"));
                    layoutBankRow.setBeneficiaryFiscalId(resulSet.getString("fiscal_id"));
                    
                    layoutBankRow.setDpsType(resulSet.getString("code"));
                    layoutBankRow.setDpsNumber(resulSet.getString("f_num"));
                    layoutBankRow.setDpsDate(resulSet.getDate("dt"));
                    layoutBankRow.setDpsCompanyBranchCode(resulSet.getString("cob_code"));
                    layoutBankRow.setSubTotal(resulSet.getDouble("f_stot_cur"));
                    layoutBankRow.setTaxCharged(resulSet.getDouble("f_tax_cur"));
                    layoutBankRow.setTaxRetained(resulSet.getDouble("f_ret_cur"));
                    layoutBankRow.setTotal(resulSet.getDouble("f_tot_cur"));
                    layoutBankRow.setMoneyDpsBalance(new SMoney(miClient.getSession(), resulSet.getDouble("f_bal_cur"), resulSet.getInt("f_id_cur"), resulSet.getDouble("f_ext_rate")));
                    layoutBankRow.setTotalVat(resulSet.getDouble("f_iva_cur"));
                    layoutBankRow.setDpsDateMaturity(resulSet.getDate("dt_mat"));
                    layoutBankRow.setDpsCurrencyKey(resulSet.getString("cur_key"));
                    
                    layoutBankRow.setMoneyPayment(new SMoney(miClient.getSession(), 0d, resulSet.getInt("f_id_cur"), mnBankLayoutCurrencyId == mnDpsCurrencyId ? 1d : 0d));
                    layoutBankRow.setBankCurrencyId(mnBankLayoutCurrencyId); 
                    layoutBankRow.setBalanceTotByBizPartner(0);
                    layoutBankRow.setPayerAccountCurrencyKey(moKeyBankLayoutCurrency.getSelectedIndex() <= 0 ?
                            SDataReadDescriptions.getCatalogueDescription(((SClientInterface) miClient), SDataConstants.CFGU_CUR, miClient.getSession().getSessionCustom().getLocalCurrencyKey(), SLibConstants.DESCRIPTION_CODE) : SDataReadDescriptions.getCatalogueDescription(((SClientInterface) miClient), SDataConstants.CFGU_CUR, moKeyBankLayoutCurrency.getValue(), SLibConstants.DESCRIPTION_CODE));
                    layoutBankRow.setCurrencyId(mnDpsCurrencyId);
                    
                    loadBankAccountCredits(layoutBankRow, resulSet.getInt("fid_bpb"), mnBizPartnerBankId); // must be called just before using member msBeneficiaryAccountNumber!... odd, but real!
                    layoutBankRow.setBeneficiaryAccountNumber(msAccountCredit); // must be used just after calling method loadBankAccountCredits()!... odd, but real!
                    
                    layoutBankRow.setAgreement(msAgreement);
                    layoutBankRow.setAgreementReference(msAgreementReference);
                    layoutBankRow.setConceptCie(msConceptCie);
                    layoutBankRow.setBeneficiaryEmail(resulSet.getString("email_01"));
                    
                    layoutBankRow.setFiscalVoucher(0);
                    layoutBankRow.setApply(1);
                    layoutBankRow.setAccountDebit(moDataBizPartnerBranchBankAccount.getBankAccountNumber());
                    layoutBankRow.setBizPartnerDebitFiscalId(((SClientInterface) miClient).getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId());
                    layoutBankRow.setForPayment(false);
                    layoutBankRow.setPayed(false);
                    layoutBankRow.setReference(resulSet.getString("f_num"));
                    layoutBankRow.setAccType("CLA");
                    layoutBankRow.setConcept(moTextConcept.getValue());
                    layoutBankRow.setDescription(moTextConcept.getValue());
                    layoutBankRow.setObservations("");
                    
                    if (resulSet.getObject("xml_uuid") != null && !resulSet.getString("xml_uuid").equals("")) {
                        layoutBankRow.setIsXml(true);
                        layoutBankRow.setXml(resulSet.getString("doc_xml"));
                        layoutBankRow.setXmlUuid(resulSet.getString("xml_uuid"));
                        layoutBankRow.setXmlRfcEmi(resulSet.getString("xml_rfc_emi"));
                        layoutBankRow.setXmlRfcRec(resulSet.getString("xml_rfc_rec"));
                        layoutBankRow.setXmlTotal(resulSet.getDouble("xml_tot"));
                        layoutBankRow.setXmlType(resulSet.getInt("xml_type"));
                    }
                    
                    layoutBankRow.getAccountCredits().addAll(maBeneficiaryAccountGuiItems);
                    layoutBankRow.getAgreementsReferences().addAll(maAgreementsReferenceGuiItems);
                    layoutBankRow.getBranchBankAccountCredits().addAll(maBizPartnerBranchBankAccounts);
                    
                    if (moDateDateDue.getValue().equals(resulSet.getDate("dt_mat")) && maBizPartnerBranchBankAccounts.size() > 0) {
                        gridRows.add(layoutBankRow);
                        mltAccountCredits.add(maBeneficiaryAccountGuiItems);
                    }
                    if (moDateDateDue.getValue().equals(resulSet.getDate("dt_mat")) && !maAgreementGuiItems.isEmpty()) {
                        mltAgreementsReferences.add(maAgreementsReferenceGuiItems);
                    }
                    if (!maAgreementGuiItems.isEmpty()) {
                        mltAgreementsReferences.add(moAgreementReferencesMap.get(msAgreement));
                    }
                    
                    maAllLayoutBankRows.add(layoutBankRow);
                }
            }
            
            moGridPayments.populateGrid(gridRows);
            moGridPayments.createGridColumns();

            moGridPayments.getTable().getTableHeader().setEnabled(false);
            moGridPayments.getTable().setColumnSelectionAllowed(false);
            moGridPayments.getTable().getTableHeader().setReorderingAllowed(false);
            moGridPayments.getTable().getTableHeader().setResizingAllowed(true);            
            moGridPayments.getTable().setRowSorter(new TableRowSorter<>(moGridPayments.getModel()));
            moGridPayments.getTable().getColumnModel().getColumn(COL_BANK_ACC_CHECK).setCellEditor(null);

            if (moKeyBankLayoutCurrency.getSelectedIndex() > 0 && moKeyDpsCurrency.getSelectedIndex() > 0) {
                ((SGridColumnForm) moGridPayments.getGridColumn(COL_TRN_TP_PAY_EXR)).setEditable(mnBankLayoutCurrencyId != mnDpsCurrencyId);
            }

            if (moGridPayments.getTable().getRowCount() > 0) {
                moGridPayments.renderGridRows();
                moGridPayments.setSelectedGridRow(0);
                moCellEditorOptions.setElements(mltAccountCredits);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            if (showValidationOfExistingRows && moGridPayments.getTable().getRowCount() <= 0) {
                miClient.showMsgBoxInformation("No se encontró ningún documento para los parámetros especificados.");
            }

            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /*
     * Methods for bank layout prepayments:
     */
    
    /**
     * Populate grid with rows of beneficiaries for prepayments.
     * @param showValidationOfExistingRows 
     */
    private void populateGridWithBenefsFromDb(final boolean showValidationOfExistingRows) {
        Vector<SGridRow> rows = new Vector<>();
        
        maAllLayoutBankRows = new ArrayList<>();
        mltAccountCredits = new ArrayList<>();
        mltAgreementsReferences = new ArrayList<>();
        moAgreementReferencesMap = new HashMap<>();
        
        Cursor cursor = getCursor();

        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            // Show beneficiaries that have bank accounts whose currency equals the requested one:
           
            String sql = "SELECT b.id_bp, b.bp, b.fiscal_id, bct.bp_key, bpb.id_bpb, bpb_con.email_01, " +
                    "'" + (mnDpsCurrencyId == 0 ? "" : miClient.getSession().getSessionCustom().getCurrencyCode(new int[] { mnDpsCurrencyId })) + "' AS _cur " +
                    "FROM erp.bpsu_bp AS b " +
                    "INNER JOIN erp.bpsu_bp_ct AS bct ON  bct.id_bp = b.id_bp AND bct.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_SUP + " " +
                    "INNER JOIN erp.bpsu_bpb AS bpb ON bpb.fid_bp = b.id_bp " + //AND bpb.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " + /* Se comentó esta línea ya que limitaba la consulta únicamente a las sucursales matriz 26/01/2021 */
                    "LEFT OUTER JOIN erp.bpsu_bpb_con AS bpb_con ON bpb.id_bpb = bpb_con.id_bpb AND bpb_con.id_con = " + SDataConstantsSys.BPSS_TP_CON_ADM + " " +
                    "WHERE EXISTS (SELECT * FROM erp.bpsu_bank_acc AS ac WHERE bpb.id_bpb = ac.id_bpb AND ac.fid_cur = " + mnDpsCurrencyId + " AND ac.fid_bank " + 
                    (SLibUtils.belongsTo(mnBankPaymentTypeId, new int[] { SDataConstantsSys.FINS_TP_PAY_BANK_THIRD, SDataConstantsSys.FINS_TP_PAY_BANK_AGREE }) ? "= " : "<> ") + mnBizPartnerBankId + ") " +
                    "AND NOT b.b_del AND NOT bct.b_del " +
                    "ORDER BY b.bp, b.id_bp; ";
            
            try (ResultSet resulSet = miClient.getSession().getStatement().executeQuery(sql)) {
                while (resulSet.next()) {
                    SLayoutBankRow layoutBankRow = new SLayoutBankRow(miClient, SLayoutBankRow.MODE_FORM_EDITION);
                    
                    layoutBankRow.setTransactionType(SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY);
                    layoutBankRow.setPaymentType(mnBankPaymentTypeId);
                    //layoutBankRow.setDpsYearId(...);
                    //layoutBankRow.setDpsDocId(...);
                    layoutBankRow.setBizPartnerId(resulSet.getInt("id_bp"));
                    layoutBankRow.setBizPartner(resulSet.getString("bp"));
                    layoutBankRow.setBizPartnerKey(resulSet.getString("bp_key"));
                    layoutBankRow.setBeneficiaryFiscalId(resulSet.getString("fiscal_id"));
                    layoutBankRow.setBizPartnerBranchId(resulSet.getInt("id_bpb"));
                    
                    layoutBankRow.setMoneyPayment(new SMoney(miClient.getSession(), 0d, mnDpsCurrencyId, 1d));
                    layoutBankRow.setBalanceTotByBizPartner(0);
                    layoutBankRow.setPayerAccountCurrencyKey(resulSet.getString("_cur"));
                    layoutBankRow.setCurrencyId(mnDpsCurrencyId);
                    
                    loadBankAccountCredits(layoutBankRow, resulSet.getInt("id_bpb"), mnBizPartnerBankId);
                    layoutBankRow.setBankAccPk(moBankAccPk);
                    
                    layoutBankRow.setAgreement(msAgreement);
                    layoutBankRow.setAgreementReference(msAgreementReference);
                    layoutBankRow.setConceptCie(msConceptCie);
                    layoutBankRow.setBeneficiaryEmail(resulSet.getString("email_01"));
                    
                    layoutBankRow.setFiscalVoucher(0);
                    layoutBankRow.setApply(1);
                    layoutBankRow.setAccountDebit(moDataBizPartnerBranchBankAccount.getBankAccountNumber());
                    layoutBankRow.setBizPartnerDebitFiscalId(((SClientInterface) miClient).getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId());
                    layoutBankRow.setForPayment(false);
                    layoutBankRow.setPayed(false);
                    layoutBankRow.setReference(resulSet.getString("id_bpb"));
                    layoutBankRow.setAccType("CLA");
                    layoutBankRow.setConcept(moTextConcept.getValue());
                    layoutBankRow.setDescription(moTextConcept.getValue());
                    layoutBankRow.setObservations("");
                    
                    layoutBankRow.setAccountCreditArray(maBeneficiaryAccountGuiItems);
                    layoutBankRow.setAgreementsReferencesArray(maAgreementsReferenceGuiItems);
                    layoutBankRow.setBranchBankAccountCreditArray(maBizPartnerBranchBankAccounts);
                    
                    if (!maBizPartnerBranchBankAccounts.isEmpty()) {
                        rows.add(layoutBankRow);
                        mltAccountCredits.add(maBeneficiaryAccountGuiItems);
                    }
                    
                    if (!maAgreementGuiItems.isEmpty()) {
                        mltAgreementsReferences.add(moAgreementReferencesMap.get(msAgreement));
                    }
                    
                    maAllLayoutBankRows.add(layoutBankRow);
                }
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
                moCellEditorOptions.setElements(mltAccountCredits);
                moCellEditorOptionsAgreementReference.setElements(mltAgreementsReferences);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            if (showValidationOfExistingRows && moGridPayments.getTable().getRowCount() <= 0) {
                miClient.showMsgBoxInformation("No se encontró ningún beneficiario para los parámetros especificados.");
            }
            setCursor(cursor);
        }
    }
    
    /*
     * Methods for bank layout payments and prepayments:
     */
    
    private void loadBankAccountCredits(SLayoutBankRow layoutBankRow, int bizPartnerBranchId, int bizPartnerBankId) {
        msAccountCredit = "";
        msAgreement = "";
        msAgreementReference = "";
        msConceptCie = "";
        
        maBizPartnerBranchBankAccounts = new ArrayList<>();
        maBeneficiaryAccountGuiItems = new ArrayList<>();
        maAgreementGuiItems = new ArrayList<>();
        maAgreementsReferenceGuiItems = new ArrayList<>();
        
        try {
            String sql = "SELECT b.id_bpb, b.id_bank_acc, b.acc_num, b.acc_num_std, b.agree, b.ref, b.fid_bank, b.b_def, COALESCE(lay.fid_tp_pay_bank, 0) AS f_tp_pay, " +
                        "COALESCE(lay.id_tp_lay_bank, 0) AS f_tp_lay, bp.code_bank_san, bp.code_bank_baj, b.alias_baj " +
                        "FROM erp.bpsu_bank_acc AS b " +
                        "LEFT OUTER JOIN erp.bpsu_bank_acc_lay_bank AS l ON b.id_bpb = l.id_bpb AND b.id_bank_acc = l.id_bank_acc " +
                        "LEFT OUTER JOIN erp.finu_tp_lay_bank AS lay ON l.id_tp_lay_bank = lay.id_tp_lay_bank " +
                        "LEFT OUTER JOIN erp.bpsu_bp AS bp ON bp.id_bp = b.fid_bank " +
                        "WHERE NOT b.b_del AND b.fid_cur = " + mnBankLayoutCurrencyId + " AND b.id_bpb = " + bizPartnerBranchId +
                        (mnBankPaymentTypeId == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD || mnBankPaymentTypeId == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE ? " AND b.fid_bank = " + bizPartnerBankId : " AND b.fid_bank <> " + bizPartnerBankId) + " AND lay.id_tp_lay_bank = " + mnBankLayoutTypeId + " ";

            HashSet<String> agreementsMap = new HashSet<>();
            HashSet<String> beneficiaryAccoountsMap = new HashSet<>();

            int bpbId = 0;
            int bpbBankAccountId = 0;
            String beneficiaryAccount = "";
            String agreement = "";
            SDataBizPartnerBranchBankAccount bizPartnerBranchBankAccount = null;

            Statement statementAux = miClient.getSession().getStatement().getConnection().createStatement();
            ResultSet resultSet = statementAux.executeQuery(sql);

            if (mnBankPaymentTypeId == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                while (resultSet.next()) {
                    bpbId = resultSet.getInt("b.id_bpb");
                    bpbBankAccountId = resultSet.getInt("b.id_bank_acc");
                    agreement = resultSet.getString("b.agree");

                    msAccountCredit = resultSet.getString("b.acc_num");                    
                    msAgreement = agreement;
                    moAgreementReferencesMap.put(agreement, SFinUtilities.getAgreementReferences(miClient, bizPartnerBranchId, agreement));

                    agreementsMap.add(agreement);
                    beneficiaryAccoountsMap.add(agreement);

                    bizPartnerBranchBankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BANK_ACC, new int[] { bpbId, bpbBankAccountId }, SLibConstants.EXEC_MODE_SILENT); 

                    maBizPartnerBranchBankAccounts.add(bizPartnerBranchBankAccount);
                }

                maAgreementsReferenceGuiItems.add(new SGuiItem(""));
            }
            else {
                while (resultSet.next()) {
                    bpbId = resultSet.getInt("b.id_bpb");
                    bpbBankAccountId = resultSet.getInt("b.id_bank_acc");                  

                    if (resultSet.getBoolean("b.b_def") && resultSet.getInt("f_tp_pay") == mnBankPaymentTypeId) {
                        msAccountCredit = (mnBankPaymentTypeId != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? resultSet.getString("b.acc_num_std") : resultSet.getString("b.acc_num"));
                    }
                    if (msAccountCredit.isEmpty() && resultSet.getInt("f_tp_pay") == mnBankPaymentTypeId) {
                        msAccountCredit = (mnBankPaymentTypeId != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? resultSet.getString("b.acc_num_std") : resultSet.getString("b.acc_num"));
                    }
                    beneficiaryAccount = mnBankPaymentTypeId != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? resultSet.getString("b.acc_num_std") : resultSet.getString("b.acc_num");

                    if (resultSet.getInt("f_tp_pay") == mnBankPaymentTypeId && resultSet.getInt("f_tp_lay") == mnBankLayoutTypeId) {
                        beneficiaryAccoountsMap.add(beneficiaryAccount);
                    }
                    layoutBankRow.getCodeBankAccountCredits().put(mnBankPaymentTypeId != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? resultSet.getString("b.acc_num_std") : resultSet.getString("b.acc_num"),
                                                    mnLayoutBank == SFinConsts.LAY_BANK_BBAJ ? resultSet.getString("bp.code_bank_baj") : resultSet.getString("bp.code_bank_san"));
                    layoutBankRow.getAliasBankAccountCredits().put(mnBankPaymentTypeId != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? resultSet.getString("b.acc_num_std") : resultSet.getString("b.acc_num"), resultSet.getString("b.alias_baj"));

                    bizPartnerBranchBankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BANK_ACC, new int[] { bpbId, bpbBankAccountId }, SLibConstants.EXEC_MODE_SILENT); 

                    maBizPartnerBranchBankAccounts.add(bizPartnerBranchBankAccount);
                }
            }

            for (String account : agreementsMap) {
                maAgreementGuiItems.add(new SGuiItem(new int[] { bpbId, bpbBankAccountId }, account));
            }

            for (String account : beneficiaryAccoountsMap) {
                maBeneficiaryAccountGuiItems.add(new SGuiItem(new int[] { bpbId, bpbBankAccountId }, account));
            }
            
            moBankAccPk = new int[] { bpbId, bpbBankAccountId };
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void refreshGridRows() {
        updateAllLayoutBankRowsFromGridRows();
        
        moGridPayments.clearGridRows();

        if (maAllLayoutBankRows != null) {
            mltAccountCredits.clear();
            mltAgreementsReferences.clear();

            if (isModeForTransfersOfPayments()) {
                for (SLayoutBankRow layoutBankRow : maAllLayoutBankRows) {
                    if (!jckShowOnlyDocsDateDue.isSelected() || moDateDateDue.getValue().equals(layoutBankRow.getDpsDateMaturity())) {
                        if (!jckShowOnlyBenefsWithAccounts.isSelected() || layoutBankRow.getAccountCredits().size() > 0) {
                            mltAccountCredits.add(layoutBankRow.getAccountCredits());
                            mltAgreementsReferences.add(moAgreementReferencesMap.get(layoutBankRow.getAgreement()));
                            moGridPayments.addGridRow(layoutBankRow);
                        }
                    }
                }
            }
            else if (isModeForTransfersOfPrepayments()) {
                for (SLayoutBankRow layoutBankRow : maAllLayoutBankRows) {
                    if (!jckShowOnlyBenefsWithAccounts.isSelected() || layoutBankRow.getAccountCredits().size() > 0) {
                        mltAccountCredits.add(layoutBankRow.getAccountCredits());
                        mltAgreementsReferences.add(moAgreementReferencesMap.get(layoutBankRow.getAgreement()));
                        moGridPayments.addGridRow(layoutBankRow);
                    }
                }
            }
        }
        
        computeBalance();
        
        moCellEditorOptions.setElements(mltAccountCredits);
        moCellEditorOptionsAgreementReference.setElements(mltAgreementsReferences);
        
        moGridPayments.renderGridRows();
        moGridPayments.setSelectedGridRow(0);
    }

    /**
     * Update member all layout bank rows from current values in grid rows.
     */
    private void updateAllLayoutBankRowsFromGridRows() {
        for (SGridRow gridRow : moGridPayments.getModel().getGridRows()) {
            SLayoutBankRow layoutBankRow = (SLayoutBankRow) gridRow;
            
            ArrayList<SDataBizPartnerBranchBankAccount> accounts = layoutBankRow.getBranchBankAccountCredits();
            for (SDataBizPartnerBranchBankAccount account : accounts) {
                if (!layoutBankRow.getBeneficiaryAccountNumber().isEmpty() && (
                        layoutBankRow.getBeneficiaryAccountNumber().equals(account.getBankAccountNumber()) || 
                        layoutBankRow.getBeneficiaryAccountNumber().equals(account.getBankAccountNumberStd()))) {
                    layoutBankRow.setBankAccPk(new int[] {account.getPkBizPartnerBranchId(), account.getPkBizPartnerBranchId()});
                }
            }
            
            for (SLayoutBankRow layoutBankRowToUpdate : maAllLayoutBankRows) {
                if (SLibUtils.compareKeys(layoutBankRow.getRowPrimaryKey(), layoutBankRowToUpdate.getRowPrimaryKey())) {
                     layoutBankRowToUpdate.setForPayment(layoutBankRow.isForPayment());
                     layoutBankRowToUpdate.setBeneficiaryAccountNumber(layoutBankRow.getBeneficiaryAccountNumber());
                     layoutBankRowToUpdate.setAgreement(layoutBankRow.getAgreement());
                     layoutBankRowToUpdate.setAgreementReference(layoutBankRow.getAgreementReference());
                     layoutBankRowToUpdate.setConceptCie(layoutBankRow.getConceptCie());
                     layoutBankRowToUpdate.setMoneyPayment(layoutBankRow.getMoneyPayment());
                     layoutBankRowToUpdate.setBalanceTotByBizPartner(layoutBankRow.getMoneyPayment().getOriginalAmount());
                     layoutBankRowToUpdate.setBankKey(SLibUtils.parseInt((layoutBankRow.getBeneficiaryAccountNumber().length() > 10 ? layoutBankRow.getBeneficiaryAccountNumber().substring(0, 3) : "000")));
                     layoutBankRowToUpdate.setBeneficiaryEmail(layoutBankRow.getBeneficiaryEmail());
                     layoutBankRowToUpdate.setSantanderBankCode(layoutBankRowToUpdate.getCodeBankAccountCredits().get(layoutBankRow.getBeneficiaryAccountNumber()));
                     layoutBankRowToUpdate.setBajioBankCode(layoutBankRowToUpdate.getCodeBankAccountCredits().get(layoutBankRow.getBeneficiaryAccountNumber()));
                     layoutBankRowToUpdate.setBajioBankAlias(layoutBankRowToUpdate.getAliasBankAccountCredits().get(layoutBankRow.getBeneficiaryAccountNumber()));
                     layoutBankRowToUpdate.setObservations(layoutBankRow.getObservations());
                }
            }
        }
    }

    /**
     * Update member all layout bank rows from values in XML.
     * @throws Exception 
     */
    private void updateAllLayoutBankRowsFromXml() throws Exception {
        int docsInXml = 0;
        int docsFound = 0;
        SXmlBankLayout xmlBankLayout = new SXmlBankLayout();
        
        xmlBankLayout.processXml(moRegistry.getLayoutXml());

        for (SXmlElement elementPay : xmlBankLayout.getXmlElements()) {
            if (elementPay instanceof SXmlBankLayoutPayment) {
                SXmlBankLayoutPayment xmlBankLayoutPayment = (SXmlBankLayoutPayment) elementPay;
                
                for (SXmlElement elementDoc : xmlBankLayoutPayment.getXmlElements()) {
                    if (elementDoc instanceof SXmlBankLayoutPaymentDoc) {
                        docsInXml++;
                        
                        SXmlBankLayoutPaymentDoc xmlBankLayoutPaymentDoc = (SXmlBankLayoutPaymentDoc) elementDoc;
                        
                        int[] key = null;
                        
                        if (isModeForTransfersOfPayments()) {
                            // DPS primary key:
                            key = new int[] {
                                (int) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC).getValue(),
                                (int) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR).getValue() 
                            };
                        }
                        else if (isModeForTransfersOfPrepayments()) {
                            // business parther primary key:
                            key = new int[] {
                                (int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BP).getValue()
                            };
                        }
                        
                        if (maAllLayoutBankRows != null) {
                            for (SLayoutBankRow layoutBankRowInArray : maAllLayoutBankRows) {
                                if (SLibUtils.compareKeys(key, new int[] { layoutBankRowInArray.getDpsDocId(), layoutBankRowInArray.getDpsYearId() }) || SLibUtils.compareKeys(key, new int[] { layoutBankRowInArray.getBizPartnerId() })) {
                                    docsFound++; 
                                    layoutBankRowInArray.setForPayment(true);
                                    layoutBankRowInArray.setPayed((boolean) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).getValue());
                                    layoutBankRowInArray.getMoneyPayment().setOriginalAmount((double) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT_CY).getValue());
                                    if ((mnBankLayoutCurrencyId == mnDpsCurrencyId) && ((double) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EXR).getValue() == 0)){
                                        layoutBankRowInArray.setExchangeRate(1);
                                    }
                                    else {
                                        layoutBankRowInArray.setExchangeRate((double) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EXR).getValue(), mnBankLayoutCurrencyId);
                                    }
                                    layoutBankRowInArray.getMoneyPayment().setOriginalCurrencyId((int) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_CUR).getValue());
                                    layoutBankRowInArray.setBalancePayed((double) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT).getValue());
                                    layoutBankRowInArray.setBeneficiaryAccountNumber(layoutBankRowInArray.getBranchBankAccountCreditNumber(new int[] { (int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BPB).getValue(), (int) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).getValue() }, mnBankPaymentTypeId));
                                    layoutBankRowInArray.setAgreement((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE).getValue());
                                    layoutBankRowInArray.setAgreementReference((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_REF).getValue());
                                    layoutBankRowInArray.setConceptCie((String) xmlBankLayoutPayment.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AGREE_CON).getValue());
                                    layoutBankRowInArray.setReferenceRecord((String) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_REF_REC).getValue());
                                    layoutBankRowInArray.setObservations((String) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_OBS).getValue());
                                    layoutBankRowInArray.setBeneficiaryEmail((String) xmlBankLayoutPaymentDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_EMAIL).getValue());
                                }
                            }
                        }
                    }
                }
            }
        }

        if (docsFound != docsInXml) {
            if (miClient.showMsgBoxConfirm("No se encontraron todas las cuentas por pagar del layout.\n¿Desea cargar el layout sólo con las cuentas que sea posible?") == JOptionPane.NO_OPTION) {
                throw new Exception("¡No se encontraron todas las cuentas por pagar del layout!");
            }
        }
        
        moGridPayments.renderGridRows();
        moGridPayments.setSelectedGridRow(0);
    }
    
    private void validateTransfers() throws Exception {
        int visibleRows = 0;
        ArrayList<SLayoutBankRow> layoutBankRows = new ArrayList<>();
        
        updateAllLayoutBankRowsFromGridRows();

        for (SGridRow gridRow : moGridPayments.getModel().getGridRows()) {
            visibleRows++;
            
            SLayoutBankRow layoutBankRowInGrid = (SLayoutBankRow) gridRow;

            for (SLayoutBankRow layoutBankRowInArray : maAllLayoutBankRows) {
                if (layoutBankRowInArray.isForPayment() && SLibUtils.compareKeys(layoutBankRowInGrid.getRowPrimaryKey(), layoutBankRowInArray.getRowPrimaryKey())) {
                    if (mnBankPaymentTypeId == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                        if (layoutBankRowInArray.getAgreement().isEmpty()) {
                            throw new Exception("No ha especificado el convenio para el pago #" + visibleRows + ".");
                        }
                        else if (layoutBankRowInArray.getAgreementReference().isEmpty()) {
                            throw new Exception("No ha especificado la referencia del convenio para el pago #" + visibleRows + ".");
                        }
                    }
                    else {
                        if (layoutBankRowInArray.getBeneficiaryAccountNumber().isEmpty()) {
                            throw new Exception("No ha especificado la cuenta beneficiaria para el pago #" + visibleRows + ".");
                        }
                    }

                    boolean found = false;

                    for (SLayoutBankRow row : layoutBankRows) {
                        if (SLibUtils.compareKeys(new int[] { row.getBizPartnerId(), }, new int[] { layoutBankRowInArray.getBizPartnerId() }) &&
                            row.getBeneficiaryAccountNumber().compareTo(layoutBankRowInArray.getBeneficiaryAccountNumber()) == 0 &&
                            SLibUtils.compareAmount(row.getExchangeRate() * 100, layoutBankRowInArray.getExchangeRate() * 100)) 
                        {
                            found = true;
                            row.setBalanceTotByBizPartner(row.getBalanceTotByBizPartner() + layoutBankRowInArray.getBalanceTotByBizPartner());
                            break;
                        }
                    }

                    if (!found) {
                        layoutBankRows.add(layoutBankRowInArray);
                    }
                    
                    if (layoutBankRowInArray.getMoneyPayment().getExchangeRate() == 0) {
                        throw new Exception("No ha especificado el tipo de cambio para el pago #" + visibleRows + ".");
                    }
                    else if (isModeForTransfersOfPrepayments() && SLibUtils.textTrim(layoutBankRowInArray.getObservations()).isEmpty()) {
                        throw new Exception("No han especificado observaciones para el pago #" + visibleRows + ".");
                    }
                }
            }
        }

        if (layoutBankRows.isEmpty()) {
            throw new Exception("No ha " + (isModeForTransfersOfPayments() ? "seleccionado" : "capturado") + " ningún renglón para el layout bancario.");
        }

        // validate "alias" for payment with BanBajio Bank:

        if (mnLayoutBank == SFinConsts.LAY_BANK_BBAJ) {
            for (SLayoutBankRow row : layoutBankRows) {
                if (row.getBajioBankAlias().isEmpty()) {
                    throw new Exception("No se ha especificado el 'alias ' de la cuenta de abono '" + row.getBeneficiaryAccountNumber() + "' del proveedor '" + row.getBizPartner() + "'.");
                }
            }
        }
    }

    private ArrayList<SLayoutBankXmlRow> createLayoutBankXmlRows() {
        ArrayList<SLayoutBankXmlRow> layoutBankXmlRows = new ArrayList<>();
        
        updateAllLayoutBankRowsFromGridRows();
        
        for (SGridRow gridRow : moGridPayments.getModel().getGridRows()) {
            SLayoutBankRow layoutBankRowInGrid = (SLayoutBankRow) gridRow;
            
            for (SLayoutBankRow layoutBankRowInArray : maAllLayoutBankRows) {
                if (layoutBankRowInArray.isForPayment()) {
                    System.out.println("");
                }
                if (layoutBankRowInArray.isForPayment() && SLibUtils.compareKeys(layoutBankRowInArray.getRowPrimaryKey(), layoutBankRowInGrid.getRowPrimaryKey())) {
                    SLayoutBankXmlRow layoutBankXmlRow = new SLayoutBankXmlRow();

                    layoutBankXmlRow.setTransactionType(layoutBankRowInArray.getTransactionType());
                    layoutBankXmlRow.setDpsYearId(layoutBankRowInGrid.getDpsYearId());
                    layoutBankXmlRow.setDpsDocId(layoutBankRowInGrid.getDpsDocId());
                    layoutBankXmlRow.setAmount(layoutBankRowInGrid.getMoneyPayment().getLocalAmount());
                    layoutBankXmlRow.setAmountCy(layoutBankRowInGrid.getMoneyPayment().getOriginalAmount());
                    layoutBankXmlRow.setAmountPayed(layoutBankRowInGrid.getBalancePayed());
                    layoutBankXmlRow.setCurrencyId(layoutBankRowInGrid.getMoneyPayment().getOriginalCurrencyId());
                    layoutBankXmlRow.setExchangeRate(layoutBankRowInGrid.getMoneyPayment().getExchangeRate());
                    layoutBankXmlRow.setPayed(false);
                    layoutBankXmlRow.setBizPartnerId(layoutBankRowInGrid.getBizPartnerId());
                    
                    if (mnBankPaymentTypeId == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                        if (!layoutBankRowInGrid.getAgreementReference().isEmpty()) {
                            layoutBankXmlRow.setAgreement(layoutBankRowInGrid.getAgreement());
                            
                            if (layoutBankRowInGrid.getAgreementReference().length() >= SLayoutBankRow.AGREEMENT_REFERENCE_MAX_LEN && (layoutBankRowInGrid.getConceptCie().isEmpty())) {
                                layoutBankXmlRow.setAgreementReference(layoutBankRowInGrid.getAgreementReference().substring(0, SLayoutBankRow.AGREEMENT_REFERENCE_MAX_LEN));
                                layoutBankXmlRow.setConceptCie(layoutBankRowInGrid.getAgreementReference().substring(SLayoutBankRow.AGREEMENT_REFERENCE_MAX_LEN));
                            }
                            else {
                                layoutBankXmlRow.setAgreementReference(layoutBankRowInGrid.getAgreementReference());
                                layoutBankXmlRow.setConceptCie(layoutBankRowInGrid.getConceptCie());
                            }
                            
                            int[] bizPartnerBranchAccountKey = new int[2];
                            
                            for (SGuiItem guiItem : layoutBankRowInGrid.getAccountCredits()) {
                                if (guiItem.getItem().trim().equals(layoutBankRowInGrid.getAgreement().trim())) {
                                    bizPartnerBranchAccountKey = guiItem.getPrimaryKey();
                                    break;
                                }
                            }
                            
                            layoutBankXmlRow.setBizPartnerBranchId(bizPartnerBranchAccountKey[0]);
                            layoutBankXmlRow.setBizPartnerBranchAccountId(bizPartnerBranchAccountKey[1]);
                        }
                    } 
                    else {
                        layoutBankXmlRow.setBizPartnerBranchId(layoutBankRowInGrid.getBranchBankAccountCreditId(layoutBankRowInGrid.getBeneficiaryAccountNumber(), mnBankPaymentTypeId)[0]);
                        layoutBankXmlRow.setBizPartnerBranchAccountId(layoutBankRowInGrid.getBranchBankAccountCreditId(layoutBankRowInGrid.getBeneficiaryAccountNumber(), mnBankPaymentTypeId)[1]);
                    }
                    
                    layoutBankXmlRow.setHsbcBankCode(layoutBankRowInGrid.getBankKey());
                    layoutBankXmlRow.setHsbcAccountType(layoutBankRowInGrid.getAccType());
                    layoutBankXmlRow.setHsbcFiscalIdDebit(layoutBankRowInGrid.getBizPartnerDebitFiscalId());
                    layoutBankXmlRow.setHsbcFiscalIdCredit(layoutBankRowInGrid.getBeneficiaryFiscalId());
                    layoutBankXmlRow.setHsbcFiscalVoucher(layoutBankRowInGrid.getFiscalVoucher());
                    layoutBankXmlRow.setSantanderBankCode(layoutBankRowInGrid.getSantanderBankCode() == null ? "0" : layoutBankRowInGrid.getSantanderBankCode());
                    layoutBankXmlRow.setConcept(layoutBankRowInGrid.getConcept());
                    layoutBankXmlRow.setDescription(layoutBankRowInGrid.getDescription());
                    layoutBankXmlRow.setReference(layoutBankRowInGrid.getReference());
                    layoutBankXmlRow.setBajioBankCode(layoutBankRowInGrid.getBajioBankCode() == null ? "0" : layoutBankRowInGrid.getBajioBankCode());
                    layoutBankXmlRow.setBajioBankNick(layoutBankRowInGrid.getBajioBankAlias() == null ? "0" : layoutBankRowInGrid.getBajioBankAlias());
                    layoutBankXmlRow.setBankKey(layoutBankRowInGrid.getBankKey());
                    layoutBankXmlRow.setRecYearId(0);
                    layoutBankXmlRow.setRecPeriodId(0);
                    layoutBankXmlRow.setRecBookkeepingCenterId(0);
                    layoutBankXmlRow.setRecRecordTypeId("");
                    layoutBankXmlRow.setRecNumberId(0);
                    layoutBankXmlRow.setBookkeepingYearId(0);
                    layoutBankXmlRow.setBookkeepingNumberId(0);
                    layoutBankXmlRow.setReferenceRecord(layoutBankRowInGrid.getReferenceRecord());
                    layoutBankXmlRow.setObservations(layoutBankRowInGrid.getObservations());
                    layoutBankXmlRow.setEmail(layoutBankRowInGrid.getBeneficiaryEmail());
                    
                    layoutBankXmlRows.add(layoutBankXmlRow);
                }
            }
        }
        
        return layoutBankXmlRows;
    }
    
    /*
     * General purpose private methods:
     */
    
    private void computeBalance() {
        mnSelectedRows = 0;
        moDecBalanceTotal.resetField();
        moDecBalanceTotalPayed.resetField();
        
        double balance = 0;
        double balancePayed = 0;
        
        switch (mnFormSubtype) {
            case SModSysConsts.FINX_LAY_BANK_ACC:
                for (SGridRow gridRow : moGridPayments.getModel().getGridRows()) {
                    SLayoutBankPaymentRow layoutBankPaymentRow = (SLayoutBankPaymentRow) gridRow;
                    
                    balance += layoutBankPaymentRow.getPayment();

                    if (layoutBankPaymentRow.isForPayment()) {
                        if (layoutBankPaymentRow.getPaymentCur() == 0) {
                            layoutBankPaymentRow.setPaymentCur(layoutBankPaymentRow.getPayment());
                        }
                        balancePayed += layoutBankPaymentRow.getPaymentCur();
                        mnSelectedRows++;
                    }
                    else {
                        layoutBankPaymentRow.setPaymentCur(0d);
                    }
                }
                
                moDecBalanceTotal.setValue(balance);
                moDecBalanceTotalPayed.setValue(balancePayed);
                break;
                
            case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
            case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
                for (SGridRow gridRow : moGridPayments.getModel().getGridRows()) {
                    SLayoutBankRow layoutBankRow = (SLayoutBankRow) gridRow;

                    if (layoutBankRow.isForPayment()) {
                        if (layoutBankRow.getMoneyPayment().getOriginalAmount() == 0) {
                            layoutBankRow.getMoneyPayment().setOriginalAmount(layoutBankRow.getMoneyDpsBalance().getOriginalAmount());
                        }
                        balance += layoutBankRow.getMoneyPayment().getLocalAmount();
                        mnSelectedRows++;
                    }
                    else {
                        layoutBankRow.getMoneyPayment().setOriginalAmount(0d);
                    }
                }
                
                moDecBalanceTotal.setValue(balance);
                break;
                
            default:
        }

        jtfRows.setText(SLibUtils.DecimalFormatInteger.format(mnSelectedRows) + "/" + SLibUtils.DecimalFormatInteger.format(moGridPayments.getModel().getRowCount()));
    }
    
    private void enableFieldsForm(boolean enable) {
        switch (mnFormSubtype) {
            case SModSysConsts.FINX_LAY_BANK_ACC:
                moDateDateLayout.setEditable(enable);
                moKeyLayoutBank.setEnabled(false);
                moKeyBankLayoutType.setEnabled(false);
                moKeyBankLayoutCurrency.setEnabled(false);
                moKeyBankAccountCash.setEnabled(false);
                jbPickRecord.setEnabled(true);

                moIntConsecutive.setEditable(false);
                moTextConcept.setEditable(false);
                moKeyDpsCurrency.setEnabled(false);
                moDecExchangeRate.setEnabled(enable && !isExchangeRateNotRequired());
                jbPickExchangeRate.setEnabled(enable && !isExchangeRateNotRequired());
                moDateDateDue.setEditable(false);

                jbGridRowsShow.setEnabled(false);
                jbGridRowsClear.setEnabled(false);
                break;

            case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
            case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
                moDateDateLayout.setEditable(true);
                moKeyLayoutBank.setEnabled(enable);
                moKeyBankLayoutType.setEnabled(enable && moKeyLayoutBank.getSelectedIndex() > 0);
                moKeyBankLayoutCurrency.setEnabled(enable && moKeyBankLayoutType.getSelectedIndex() > 0);
                moKeyBankAccountCash.setEnabled(enable && moKeyBankLayoutCurrency.getSelectedIndex() > 0);
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

    private void enableFieldsGrid(final boolean enableFields) {
        jckShowOnlyDocsDateDue.setEnabled(enableFields && isModeForTransfersOfPayments());
        jckShowOnlyBenefsWithAccounts.setEnabled(enableFields && isModeForTransfers());
        if (mnFormSubtype != SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY) { 
            jbExchangeRateReset.setEnabled(enableFields && !isExchangeRateNotRequired() && 
                    !(!miClient.getSession().getSessionCustom().isLocalCurrency(moKeyBankLayoutCurrency.getValue()) && 
                            !miClient.getSession().getSessionCustom().isLocalCurrency(moKeyDpsCurrency.getValue())));
            jbExchangeRateRefresh.setEnabled(enableFields && !isExchangeRateNotRequired() && 
                    !(!miClient.getSession().getSessionCustom().isLocalCurrency(moKeyBankLayoutCurrency.getValue()) && 
                            !miClient.getSession().getSessionCustom().isLocalCurrency(moKeyDpsCurrency.getValue())));
        }
        jbGridRowsCheckAll.setEnabled(enableFields && isModeForTransfersOfPayments());
        jbGridRowsUncheckAll.setEnabled(enableFields && isModeForTransfersOfPayments());
        
        jbPickLayoutPath.setEnabled(enableFields && isModeForTransfers());
    }

    /**
     * Set desired exchange rate into current grid row, if any.
     * @param exchangeRate Desired exchange rate.
     */
    private void setExchangeRate(final double exchangeRate) {
        int index = moGridPayments.getTable().getSelectedRow();
        
        if (index != -1) {
            SLayoutBankRow layoutBankRow = (SLayoutBankRow) moGridPayments.getSelectedGridRow();
            
            if (layoutBankRow.getMoneyPayment().getOriginalAmount() > 0) {
                layoutBankRow.setForPayment(true);
                layoutBankRow.setExchangeRate(exchangeRate);
            }
            else {
                layoutBankRow.setForPayment(false);
            }
            
            computeBalance();
            moGridPayments.renderGridRows();
            moGridPayments.setSelectedGridRow(index);
        }
    }
    
    private void computeBankRecords(SLayoutBankPayment layoutBankPayment, SLayoutBankRecordKey layoutBankRecordKey) throws Exception {
        boolean found = false;
        SDataRecord record = null;
        SLayoutBankRecord bankRecord = null;
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
        SSrvLock lock = null;
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        SRedisLock rlock = null;
        */
        SLock slock = null;
        
        if (layoutBankRecordKey == null) {
            for (SLayoutBankRecord bankRecordRow : maLayoutBankRecords) {
                bankRecordRow.removeLayoutBankPayment(layoutBankPayment.getBizPartnerId(), layoutBankPayment.getBizPartnerBranchId(), layoutBankPayment.getBizPartnerBranchBankAccountId());
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
                /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                lock = SSrvUtils.gainLock(miClient.getSession(), ((SClientInterface) miClient).getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.FIN_REC, layoutBankRecordKey.getPrimaryKey(), record.getRegistryTimeout());
                maLocks.add(lock);
                */ 
                /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                rlock = SRedisLockUtils.gainLock((SClientInterface) miClient, SDataConstants.FIN_REC, layoutBankRecordKey.getPrimaryKey(), record.getRegistryTimeout() / 1000);
                maRedisLocks.add(rlock);
                */
                slock = SLockUtils.gainLock((SClientInterface) miClient, SDataConstants.FIN_REC, layoutBankRecordKey.getPrimaryKey(), record.getRegistryTimeout());
                maSLocks.add(slock);
                
                bankRecord = new SLayoutBankRecord(layoutBankRecordKey);
                bankRecord.getLayoutBankPayments().add(layoutBankPayment);
                maLayoutBankRecords.add(bankRecord);
            }
        }
    }

    /*
     * Methods for action performed events:
     */

    private void actionPerformedPickRecord() {
        HashMap<Integer, Object> paramsMap = new HashMap<>();
        paramsMap.put(SDataConstants.FIN_ACC_COB_ENT, moDataAccountCash.getPkCompanyBranchId());
        paramsMap.put(SDataConstants.FIN_ACC_CASH, moDataAccountCash.getPkAccountCashId());
        
        moDialogRecordPicker.formReset();
        moDialogRecordPicker.setFilterKey(moDateDateLayout.getValue());
        moDialogRecordPicker.setParams(paramsMap);
        moDialogRecordPicker.formRefreshOptionPane();

        if (moCurrentRecord != null) {
            moDialogRecordPicker.setSelectedPrimaryKey(moCurrentRecord.getPrimaryKey());
        }

        moDialogRecordPicker.setFormVisible(true);

        if (moDialogRecordPicker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            // XXX set registry lock to accounting record

            moCurrentRecord = (SDataRecord) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.FIN_REC, moDialogRecordPicker.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
            
            if (moCurrentRecord != null) {
                String message = "";
                
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
    
    private void actionPerformedPickExchangeRate() {
        double exchangeRate = ((SClientInterface) miClient).pickExchangeRate(mnDpsCurrencyId, moDateDateLayout.getDefaultValue());

        if (exchangeRate != 0d) {
            moDecExchangeRate.setValue(exchangeRate);
        }
        else {
            moDecExchangeRate.setValue(0d);
        }
        
        jbPickExchangeRate.requestFocus();
    }
    
    private void actionPerformedPickLayoutPath() {
        String nameFile = "";
        SimpleDateFormat fileNameDatetimeFormat = new SimpleDateFormat("yyMMdd HHmm");
        
        try {
            nameFile = (moKeyBankLayoutType.getSelectedIndex() <= 0 ? "" : SLibUtils.textToAscii(SFinUtilities.getFileNameLayout(miClient.getSession(), moKeyBankLayoutType.getSelectedItem().getPrimaryKey()[0]).toLowerCase().replaceAll("/", " ")));
            nameFile = SLibUtils.validateSafePath(fileNameDatetimeFormat.format(new java.util.Date()) + " " + nameFile + ".txt");
        
            miClient.getFileChooser().setSelectedFile(new File(nameFile));
            if (miClient.getFileChooser().showSaveDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
                jtfLayoutPath.setText(miClient.getFileChooser().getSelectedFile().getAbsolutePath());
                jtfLayoutPath.setToolTipText(miClient.getFileChooser().getSelectedFile().getAbsolutePath());
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedGridRowsShow() {
        SGuiValidation validation = moFields.validateFields();
        
        if (!validation.isValid()) {
            SGuiUtils.computeValidation(miClient, validation);
        }
        else {
            // clear controls:
            jckShowOnlyDocsDateDue.setEnabled(false); // to prevent action in item-state-changed event handler
            jckShowOnlyDocsDateDue.setSelected(false);
            jckShowOnlyBenefsWithAccounts.setEnabled(false); // to prevent action in item-state-changed event handler
            jckShowOnlyBenefsWithAccounts.setSelected(false);
            jtfLayoutPath.setText(""); // clear path each time grid rows are shown
            jtfLayoutPath.setToolTipText(null); // clear path each time grid rows are shown
            
            enableFieldsForm(false);
            enableFieldsGrid(true);
            
            switch (mnFormSubtype) {
                case SModSysConsts.FINX_LAY_BANK_ACC:
                    break;
                case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
                    populateGridWithDocsFromDb(true);
                    jckShowOnlyDocsDateDue.setSelected(true); // select & trigger item-state-changed events
                    jckShowOnlyBenefsWithAccounts.setSelected(true); // select & trigger item-state-changed events
                    break;
                case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
                    populateGridWithBenefsFromDb(true);
                    jckShowOnlyDocsDateDue.setSelected(false); // does not apply, remains unselected
                    jckShowOnlyBenefsWithAccounts.setSelected(true); // select & trigger item-state-changed events
                    break;
                default:
            }
        }
    }
    
    private void actionPerformedGridRowsClear() {
        enableFieldsForm(true);
        enableFieldsGrid(false);
        
        moGridPayments.clearGridRows();
        
        jckShowOnlyDocsDateDue.setSelected(false); // already disabled; action of event handler will be omited!
        jckShowOnlyBenefsWithAccounts.setSelected(false); // already disabled; action of event handler will be omited!
    }

    /**
     * Resets current exchange rate in current grid row.
     */
    private void actionPerformedExchangeRateResetOriginal() {
        if (jbExchangeRateReset.isEnabled()) {
            if (moGridPayments.getSelectedGridRow() == null) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                setExchangeRate(((SLayoutBankRow) moGridPayments.getSelectedGridRow()).getMoneyDpsBalance().getExchangeRate());
            }
        }
    }

    /**
     * Set´s the exchange rate specified in the field 'exchange rate' for one DPS/row in the grid
     */
    private void actionPerformedExchangeRateSetCurrent() {
        if (jbExchangeRateRefresh.isEnabled()) {
            if (moGridPayments.getSelectedGridRow() == null) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                setExchangeRate(moDecExchangeRate.getValue());
            }
        }
    }

    private void actionPerformedGridRowsCheckAll() {
        SLayoutBankRow layoutBankRow = null;
        SLayoutBankPaymentRow layoutBankPaymentRow = null;
        SLayoutBankRecordKey layoutBankRecordKey = null;
        SLayoutBankPayment layoutBankPayment = null;
        SLayoutBankPayment layoutBankPayment1Aux = null;
        SGuiValidation validation = moFields.validateFields();

        try {
            if (validation.isValid()) {
                switch (mnFormSubtype) {
                    case SModSysConsts.FINX_LAY_BANK_ACC:
                        if (checkRecord(true)) {
                            for (SGridRow gridRow : moGridPayments.getModel().getGridRows()) {
                                layoutBankPaymentRow = (SLayoutBankPaymentRow) gridRow;
                                layoutBankPayment1Aux = layoutBankPaymentRow.getLayoutBankPayment().clone();
                                layoutBankPayment = layoutBankPaymentRow.getLayoutBankPayment().clone();

                                layoutBankPayment1Aux.setAction(SLayoutBankPayment.ACTION_PAY_REMOVE);
                                computeBankRecords(layoutBankPayment1Aux, layoutBankPaymentRow.getLayoutBankRecordKey());

                                layoutBankPaymentRow.setForPayment(true);
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
                        break;
                        
                    case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
                    case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
                        for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
                            layoutBankRow = (SLayoutBankRow) rowAux;
                            layoutBankRow.setForPayment(true);
                        }
                        break;
                        
                    default:
                }
                
                computeBalance();
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

    private void actionPerformedGridRowsUncheckAll() {
        SLayoutBankRow layoutBankRow = null;
        SLayoutBankPaymentRow layoutBankPaymentRow = null;
        SLayoutBankRecordKey layoutBankRecordKey = null;
        SLayoutBankPayment layoutBankPayment = null;
        
        try {
            switch (mnFormSubtype) {
                case SModSysConsts.FINX_LAY_BANK_ACC:
                    for (SGridRow gridRow : moGridPayments.getModel().getGridRows()) {
                        layoutBankPaymentRow = (SLayoutBankPaymentRow) gridRow;
                        layoutBankPayment = layoutBankPaymentRow.getLayoutBankPayment().clone();

                        layoutBankPaymentRow.setForPayment(false);
                        layoutBankPaymentRow.setRecordPeriod("");
                        layoutBankPaymentRow.setRecordBkc("");
                        layoutBankPaymentRow.setRecordCob("");
                        layoutBankPaymentRow.setRecordNumber("");
                        layoutBankPaymentRow.setRecordDate(null);
                        layoutBankPaymentRow.setLayoutBankRecordKey(null);

                        layoutBankPayment.setAction(layoutBankPaymentRow.isPayed() ? SLayoutBankPayment.ACTION_PAY_REMOVE : SLibConsts.UNDEFINED);
                        layoutBankRecordKey = layoutBankPaymentRow.getLayoutBankRecordKey();

                        computeBankRecords(layoutBankPayment, layoutBankRecordKey);
                    }
                    break;
                    
                case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
                case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
                    for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
                        layoutBankRow = (SLayoutBankRow) rowAux;
                        layoutBankRow.setForPayment(false);
                    }
                    break;
                    
                default:
            }
            
            computeBalance();
            moGridPayments.renderGridRows();
            moGridPayments.setSelectedGridRow(0);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    /*
     * Methods for item state changed events:
     */
    
    private void itemStateChangedLayoutBank() {
        processLayoutBank();
    }
    
    private void itemStateChangedBankLayoutType() {
        processBankLayoutType();
    }

    private void itemStateChangedBankLayoutCurrency() {
        processBankLayoutCurrency();
    }
    
    private void itemStateChangedBankAccountCash() {
        processBankAccountCash();
    }
    
    private void itemStateChangedDpsCurrency() {
        processDpsCurrency();
    }
    
    private void itemStateChangedShowOnlyDocsDateDue() {
        if (jckShowOnlyDocsDateDue.isEnabled()) {
            refreshGridRows();
        }
    }

    private void itemStateChangedShowOnlyBenefsWithAccounts() {
        if (jckShowOnlyBenefsWithAccounts.isEnabled()) {
            refreshGridRows();
        }
    }
    
    /*
     * Methods for cell editing events:
     */
    
    private void editingStoppedAccountingChecked() {
        if (isModeForAccounting()) {
            SLayoutBankPaymentRow layoutBankPaymentRow = (SLayoutBankPaymentRow) moGridPayments.getSelectedGridRow();
            try {
                layoutBankPaymentRow = (SLayoutBankPaymentRow) moGridPayments.getSelectedGridRow();
                SLayoutBankPayment layoutBankPayment = layoutBankPaymentRow.getLayoutBankPayment().clone();
                SLayoutBankRecordKey layoutBankRecordKey = null;
                boolean processRecord = false;

                if (layoutBankPaymentRow.isForPayment()) {
                    if (checkRecord(false)) {
                        layoutBankPaymentRow.setRecordPeriod(moCurrentRecord.getRecordPeriod());
                        layoutBankPaymentRow.setRecordBkc(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.FIN_BKC, new int[] { moCurrentRecord.getPkBookkeepingCenterId() }, SLibConstants.DESCRIPTION_CODE));
                        layoutBankPaymentRow.setRecordCob(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.BPSU_BPB, new int[] { moCurrentRecord.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
                        layoutBankPaymentRow.setRecordNumber(moCurrentRecord.getRecordNumber());
                        layoutBankPaymentRow.setRecordDate(moCurrentRecord.getDate());

                        layoutBankPayment.setAction(SLayoutBankPayment.ACTION_PAY_APPLY);
                        layoutBankRecordKey = new SLayoutBankRecordKey(moCurrentRecord.getPkYearId(), moCurrentRecord.getPkPeriodId(), moCurrentRecord.getPkBookkeepingCenterId(), moCurrentRecord.getPkRecordTypeId(), moCurrentRecord.getPkNumberId());

                        layoutBankPaymentRow.setLayoutBankRecordKey(layoutBankRecordKey);

                        processRecord = true;
                    }
                }
                else {
                    layoutBankPaymentRow.setRecordPeriod("");
                    layoutBankPaymentRow.setRecordBkc("");
                    layoutBankPaymentRow.setRecordCob("");
                    layoutBankPaymentRow.setRecordNumber("");
                    layoutBankPaymentRow.setRecordDate(null);
                    layoutBankPaymentRow.setLayoutBankRecordKey(null);

                    layoutBankPayment.setAction(layoutBankPaymentRow.isPayed() ? SLayoutBankPayment.ACTION_PAY_REMOVE : SLibConsts.UNDEFINED);
                    layoutBankRecordKey = layoutBankPaymentRow.getLayoutBankRecordKey();

                    processRecord = true;
                }

                if (processRecord) {
                    computeBankRecords(layoutBankPayment, layoutBankRecordKey);
                }

                int index = moGridPayments.getTable().getSelectedRow();
                
                computeBalance();
                moGridPayments.renderGridRows();
                moGridPayments.setSelectedGridRow(index);
            }
            catch (Exception e) {
                layoutBankPaymentRow.setForPayment(false);

                layoutBankPaymentRow.setRecordPeriod("");
                layoutBankPaymentRow.setRecordBkc("");
                layoutBankPaymentRow.setRecordCob("");
                layoutBankPaymentRow.setRecordNumber("");
                layoutBankPaymentRow.setRecordDate(null);
                layoutBankPaymentRow.setLayoutBankRecordKey(null);

                SLibUtils.showException(this, e);
            }
        }
    }

    private void editingStoppedPaymentChecked() {
        if (isModeForTransfersOfPayments()) {
            SLayoutBankRow layoutBankRow = (SLayoutBankRow) moGridPayments.getSelectedGridRow();
            
            if (layoutBankRow.isForPayment()) {
                if (mnCfgParamCfdRequired == SBankLayoutConsts.CFD_REQ_YES || mnCfgParamCfdRequired == SBankLayoutConsts.CFD_REQ_OPC) {
                    if (!layoutBankRow.isXml()) {
                        miClient.showMsgBoxWarning("Esta factura no tiene XML del CFD.");
                    }
                }
            }
            
            int index = moGridPayments.getTable().getSelectedRow();
           
            computeBalance();
            moGridPayments.renderGridRows();
            moGridPayments.setSelectedGridRow(index);
        }
    }

    private void editingStoppedPaymentEntered() {
        if (isModeForTransfers()) {
            SLayoutBankRow layoutBankRow = (SLayoutBankRow) moGridPayments.getSelectedGridRow();

            if (layoutBankRow.getMoneyPayment().getOriginalAmount() > 0) {
                layoutBankRow.setForPayment(true);
            }
            else {
                layoutBankRow.setForPayment(false);
            }

            if (layoutBankRow.isForPayment() && isModeForTransfersOfPayments()) {
                if (mnCfgParamCfdRequired == SBankLayoutConsts.CFD_REQ_YES || mnCfgParamCfdRequired == SBankLayoutConsts.CFD_REQ_OPC) {
                    if (!layoutBankRow.isXml()) {
                        miClient.showMsgBoxWarning("Esta factura no tiene XML del CFD.");
                    }
                }
            }
            
            int index = moGridPayments.getTable().getSelectedRow();

            computeBalance();
            moGridPayments.renderGridRows();
            moGridPayments.setSelectedGridRow(index);
        }
    }
    
    private void editingStoppedAgreement() {
        int index = moGridPayments.getTable().getSelectedRow();
        SLayoutBankRow layoutBankRow = (SLayoutBankRow) moGridPayments.getSelectedGridRow();
        
        maAgreementsReferenceGuiItems.clear();
        
        if (moAgreementReferencesMap.get(layoutBankRow.getAgreement()) == null) {
            ArrayList<SGuiItem> empty = new ArrayList<>();
            empty.add(new SGuiItem(""));
            mltAgreementsReferences.set(index, empty);
        }
        else {
            mltAgreementsReferences.set(index, moAgreementReferencesMap.get(layoutBankRow.getAgreement()));
        }
        
        moCellEditorOptionsAgreementReference.setElements(mltAgreementsReferences);
        moGridPayments.renderGridRows();
    }
    
    /*
     * Protected overriden methods:
     */
    
    @Override
    protected void windowActivated() {
        if (mbFirstActivation) {
            mbFirstActivation = false;
            
            if (isModeForAccounting() && moRegistry.getLayoutStatus() != SDbBankLayout.STATUS_APPROVED) {
                if (miClient.showMsgBoxConfirm("El layout bancario no tiene estatus '" + SDbBankLayout.STATUS_APPROVED_TEXT + "'.\n¿Está seguro que desea continuar con la aplicación de sus pagos?") != JOptionPane.YES_OPTION) {
                    mbCanShowForm = false;
                    mbShowConfirmCloseDialog = false;
                    msCanShowFormMessage = "El layout bancario debería tener estatus '" + SDbBankLayout.STATUS_APPROVED_TEXT + "' para la aplicación de sus pagos.";
                }
            }

            super.windowActivated();
        }
    }
    
    /*
     * Support methods for public overriden methods:
     */
    
    private void prepareBankLayoutTypes() throws Exception {
        String sql = "SELECT id_tp_lay_bank, tp_lay_bank, fid_tp_pay_bank, fid_bank, lay_bank " +
             "FROM " + SModConsts.TablesMap.get(SModConsts.FINU_TP_LAY_BANK) + " " +
             "WHERE NOT b_del " +
             "ORDER BY id_tp_lay_bank;";

         maBankLayoutTypes = new ArrayList<>();
         maBankLayoutTypes.add(new SGuiItem("(" + SUtilConsts.TXT_SELECT + " tipo layout bancario)"));

        try (ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                 // component for GUI item:
                BankLayoutType bankLayoutType = new BankLayoutType(
                        resultSet.getInt("lay_bank"), 
                        resultSet.getInt("id_tp_lay_bank"), 
                        resultSet.getInt("fid_tp_pay_bank"), 
                        resultSet.getInt("fid_bank"));
                
                // GUI item:
                maBankLayoutTypes.add(new SGuiItem(new int[] { resultSet.getInt("id_tp_lay_bank") }, resultSet.getString("tp_lay_bank"), new int[] { resultSet.getInt("lay_bank") }, bankLayoutType));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private void reloadBankLayouts() {
        Vector<SGuiItem> layouts = new Vector<>();

        layouts.add(new SGuiItem("(" + SUtilConsts.TXT_SELECT + " layout bancario)"));
        layouts.add(new SGuiItem(new int[] { SFinConsts.LAY_BANK_HSBC }, SFinConsts.TXT_LAY_BANK_HSBC));
        layouts.add(new SGuiItem(new int[] { SFinConsts.LAY_BANK_SANT }, SFinConsts.TXT_LAY_BANK_SANT));
        layouts.add(new SGuiItem(new int[] { SFinConsts.LAY_BANK_BBAJ }, SFinConsts.TXT_LAY_BANK_BBAJ));
        layouts.add(new SGuiItem(new int[] { SFinConsts.LAY_BANK_BBVA }, SFinConsts.TXT_LAY_BANK_BBVA));
        layouts.add(new SGuiItem(new int[] { SFinConsts.LAY_BANK_CITI }, SFinConsts.TXT_LAY_BANK_CITI));
        
        moKeyLayoutBank.removeAllItems();
        for (int i = 0; i < layouts.size(); i++) {
            moKeyLayoutBank.addItem(layouts.get(i));
        }
    }
    
    /**
     * Validar opcionalmente el estatus en el SAT de los CFDI del layout.
     * @return Cadena vacía si no hay errores, de lo contrario el mensaje conteniendo los errores encontrados.
     */
    @SuppressWarnings("deprecation")
    private String getCfdiSatStatus() {
        String message = "";
        
        if (miClient.showMsgBoxConfirm("Hay pagos relacionados con un CFDI.\n¿Desea validar el estatus SAT de los CFDI?") == JOptionPane.YES_OPTION) {
            try {
                for (int i = 0; i < moGridPayments.getTable().getRowCount(); i++) {
                    SLayoutBankRow row = (SLayoutBankRow) moGridPayments.getGridRow(i);
                    if (row.isXml() && !row.getXmlUuid().isEmpty()) {
                        cfd.ver33.DElementComprobante comprobante = DCfdUtils.getCfdi33(row.getXml());
                        cfd.ver33.DElementTimbreFiscalDigital tfd = comprobante.getEltOpcComplementoTimbreFiscalDigital();
                        
                        if (tfd == null) {
                            message += (message.isEmpty() ? "" : "\n");
                            message += "beneficiario: " + row.getBizPartner() + ": ";
                            message += "folio CFDI = " + row.getReference() + "; ";
                            message += "estatus CFDI = ¡Sin timbrar!.";
                        }
                        else if (!row.getXmlUuid().equals(tfd.getAttUUID().getString())) {
                            message += (message.isEmpty() ? "" : "\n");
                            message += "beneficiario: '" + row.getBizPartner() + "': ";
                            message += "folio CFDI = '" + row.getReference() + "'; ";
                            message += "estatus CFDI = ¡El CFDI relacionado con el del pago: UUID del pago = '" + row.getXmlUuid() + "', UUID del CFDI = '" + tfd.getAttUUID().getString() + "'!.";
                        }
                        
                        String cfdiStatus = new SCfdUtilsHandler((SClientInterface) miClient).getCfdiSatStatus(SDataConstantsSys.TRNS_TP_CFD_INV, comprobante).getCfdiStatus();
                        
                        if (!cfdiStatus.equals(DCfdi33Consts.CFDI_ESTATUS_VIG)) {
                            message += (message.isEmpty() ? "" : "\n");
                            message += row.getBizPartner() + ": ";
                            message += "folio CFDI = " + row.getReference() + "; ";
                            message += "estatus CFDI = " + cfdiStatus + ".";
                        }
                    }
                }
            }
            catch (Exception e) {
                miClient.showMsgBoxError(e.toString());
            }
        }
        
        return message;
    }
    
    /*
     * Public overriden methods
     */

    @Override
    public void addAllListeners() {
        jbPickRecord.addActionListener(this);
        jbPickExchangeRate.addActionListener(this);
        jbPickLayoutPath.addActionListener(this);
        jbGridRowsShow.addActionListener(this);
        jbGridRowsClear.addActionListener(this);
        jbExchangeRateReset.addActionListener(this);
        jbExchangeRateRefresh.addActionListener(this);
        jbGridRowsCheckAll.addActionListener(this);
        jbGridRowsUncheckAll.addActionListener(this);
        
        moKeyLayoutBank.addItemListener(this);
        moKeyBankLayoutType.addItemListener(this);
        moKeyBankLayoutCurrency.addItemListener(this);
        moKeyBankAccountCash.addItemListener(this);
        moKeyDpsCurrency.addItemListener(this);
        jckShowOnlyDocsDateDue.addItemListener(this);
        jckShowOnlyBenefsWithAccounts.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbPickRecord.removeActionListener(this);
        jbPickExchangeRate.removeActionListener(this);
        jbPickLayoutPath.removeActionListener(this);
        jbGridRowsShow.removeActionListener(this);
        jbGridRowsClear.removeActionListener(this);
        jbExchangeRateReset.removeActionListener(this);
        jbExchangeRateRefresh.removeActionListener(this);
        jbGridRowsCheckAll.removeActionListener(this);
        jbGridRowsUncheckAll.removeActionListener(this);
        
        moKeyLayoutBank.removeItemListener(this);
        moKeyBankLayoutType.removeItemListener(this);
        moKeyBankLayoutCurrency.removeItemListener(this);
        moKeyBankAccountCash.removeItemListener(this);
        moKeyDpsCurrency.removeItemListener(this);
        jckShowOnlyDocsDateDue.removeItemListener(this);
        jckShowOnlyBenefsWithAccounts.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        try {
            prepareBankLayoutTypes();
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        reloadBankLayouts();
        
        miClient.getSession().populateCatalogue(moKeyBankLayoutCurrency, SModConsts.CFGU_CUR, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyDpsCurrency, SModConsts.CFGU_CUR, SLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbBankLayout) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

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

            jtfNumber.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));       // the same value for both!
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));  // the same value for both!
        }

        // set header data:

        moDateDateLayout.setValue(moRegistry.getDateLayout());

        moKeyLayoutBank.setValue(new int[] { moRegistry.getXtaLayoutBank() });
        processLayoutBank();

        moKeyBankLayoutType.setValue(new int[] { moRegistry.getFkBankLayoutTypeId() });
        processBankLayoutType();

        moKeyBankLayoutCurrency.setValue(new int[] { moRegistry.getXtaBankCurrencyId() });
        processBankLayoutCurrency();

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

        jckShowOnlyDocsDateDue.setSelected(false);
        jckShowOnlyBenefsWithAccounts.setSelected(isModeForTransfers());

        maLayoutBankRecords = new ArrayList<>();
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
        maLocks = new ArrayList<>();
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        maRedisLocks = new ArrayList<>();
        */
        maSLocks = new ArrayList<>();
        
        switch (mnFormSubtype) {
            case SModSysConsts.FINX_LAY_BANK_ACC:
                populateGridWithPaymentsFromXml();
                break;
                
            case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
            case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
                if (isModeForTransfersOfPayments()) {
                    populateGridWithDocsFromDb(false);
                }
                else {
                    populateGridWithBenefsFromDb(false);
                }

                if (!moRegistry.getLayoutXml().isEmpty()) {
                    updateAllLayoutBankRowsFromXml();
                    refreshGridRows();
                }
                break;
                
            default:
        }

        jtfLayoutPath.setText(""); // clear path
        jtfLayoutPath.setToolTipText(null); // clear path

        computeBalance();

        setFormEditable(true);

        if (moRegistry.isRegistryNew()) {
            // registry is being created:

            switch (mnFormSubtype) {
                case SModSysConsts.FINX_LAY_BANK_ACC:
                    break;
                case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
                case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
                    actionPerformedGridRowsClear();
                    break;
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        else {
            // registry is being modified:

            switch (mnFormSubtype) {
                case SModSysConsts.FINX_LAY_BANK_ACC:
                    enableFieldsForm(true);
                    enableFieldsGrid(false); // disable all grid controls
                    jbGridRowsCheckAll.setEnabled(true); // but enable this one
                    jbGridRowsUncheckAll.setEnabled(true); // but enable this one
                    break;
                case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
                    enableFieldsForm(false);
                    enableFieldsGrid(true);
                    break;
                case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
                    enableFieldsForm(false);
                    enableFieldsGrid(true);
                    break;
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
            
            if (isModeForAccounting()) {
                if (!isExchangeRateNotRequired()) {
                    if (moDecExchangeRate.getValue() == 0) {
                        double exr = SDataUtilities.obtainExchangeRate((SClientInterface) miClient, mnDpsCurrencyId, moDateDateLayout.getValue());
                        moDecExchangeRate.setValue(exr);
                    }
                }
            }
        }

        addAllListeners();
    }

    @Override
    public SDbBankLayout getRegistry() throws Exception {
        SDbBankLayout registry = moRegistry.clone();

        if (isModeForTransfers()) {
            // current form is for integrate a bank layout of payments or prepayments:

            //registry.setPkLayBankId();
            registry.setDateLayout(moDateDateLayout.getValue());
            registry.setDateDue(moDateDateDue.getValue());
            registry.setConcept(moTextConcept.getValue());
            registry.setConsecutive(moIntConsecutive.getValue());
            registry.setAmount(moDecBalanceTotal.getValue());
            //registry.setAmountPayed(...);
            //registry.setTransfers(...);
            //registry.setTransfersPayed();
            registry.setDocs(mnSelectedRows);
            //registry.setDocsPayed();
            //registry.setLayoutStatus(...);
            //registry.setLayoutText(...);
            //registry.setLayoutXml(...);
            registry.setTransactionType(mnFormSubtype);
            //registry.setAuthorizationRequests(...);
            //registry.setClosedPayment(...);
            registry.setDeleted(false);
            registry.setFkBankLayoutTypeId(mnBankLayoutTypeId);
            registry.setFkBankCompanyBranchId(moDataAccountCash.getPkCompanyBranchId());
            registry.setFkBankAccountCashId(moDataAccountCash.getPkAccountCashId());
            registry.setFkDpsCurrencyId(mnDpsCurrencyId);
            /*
            registry.setFkUserClosedPaymentId();
            registry.setFkUserInsertId();
            registry.setFkUserUpdateId();
            registry.setTsUserClosedPayment();
            registry.setTsUserInsert();
            registry.setTsUserUpdate();
            */

            registry.setAuxLayoutPath(jtfLayoutPath.getText());

            registry.getAuxLayoutBankXmlRows().clear();
            registry.getAuxLayoutBankXmlRows().addAll(createLayoutBankXmlRows());
            
            registry.setPostSaveTarget(registry);
            registry.setPostSaveMethod(registry.getClass().getMethod("writeBankLayout", SGuiClient.class));
            registry.setPostSaveMethodArgs(new Object[] { miClient });
        }
        else if (isModeForAccounting()) {
            registry.getAuxLayoutBankPaymentRows().clear();
            
            for (SGridRow gridRow : moGridPayments.getModel().getGridRows()) {
                SLayoutBankPaymentRow layoutBankPaymentRow = (SLayoutBankPaymentRow) gridRow;

                if (layoutBankPaymentRow.getLayoutBankRecordKey() != null || layoutBankPaymentRow.getLayoutBankRecordKeyOld() != null) {
                    registry.getAuxLayoutBankPaymentRows().add(layoutBankPaymentRow);
                }
            }

            registry.setTransfers(moGridPayments.getModel().getGridRows().size());
        }
        registry.setExchangeRateAcc(!isExchangeRateNotRequired() ? moDecExchangeRate.getValue() : 1d);

        registry.setExchangeRate(1d);
        registry.setXtaBankPaymentTypeId(mnBankPaymentTypeId);
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
        if (!maLocks.isEmpty()) {
            registry.getLocks().addAll(maLocks);
        }
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        if (!maRedisLocks.isEmpty()) {
            registry.getRedisLocks().addAll(maRedisLocks);
        }
        */
        if (!maSLocks.isEmpty()) {
            registry.getSLocks().addAll(maSLocks);
        }
        
        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            if (isModeForTransfers()) {
                try {
                    validateTransfers();
                }
                catch (Exception e) {
                    validation.setMessage(e.getMessage());
                }
                
                if (validation.isValid()) {
                    if (jtfLayoutPath.getText().isEmpty()) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlLayoutPath) + "'.");
                        validation.setComponent(jbPickLayoutPath);
                    }
                    else {
                        int missingCfdCount = 0;
                        String missingCfdDpsNumbers = "";
                        boolean validateSatStatus = false;
                        
                        for (int i = 0; i < moGridPayments.getTable().getRowCount(); i++) {
                            SLayoutBankRow layoutBankRow = (SLayoutBankRow) moGridPayments.getGridRow(i);
                            
                            if (layoutBankRow.isForPayment()) {
                                
                                if (layoutBankRow.isXml()) {
                                    validateSatStatus = true;
                                }
                                else if (mnCfgParamCfdRequired == SBankLayoutConsts.CFD_REQ_YES || mnCfgParamCfdRequired == SBankLayoutConsts.CFD_REQ_OPC) {
                                    missingCfdCount++;
                                    
                                    if (missingCfdCount > 0 && missingCfdCount % 5 == 0) {
                                        missingCfdDpsNumbers += "\n";
                                    }
                                    
                                    missingCfdDpsNumbers += (missingCfdDpsNumbers.isEmpty() ? "" : ", ") + layoutBankRow.getDpsNumber();
                                }
                            }
                        }
                        
                        if (missingCfdCount > 0) {
                            String message = "¡Las siguientes facturas no tienen XML del CFD!:\n" + missingCfdDpsNumbers;
                            
                            switch (mnCfgParamCfdRequired) {
                                case SBankLayoutConsts.CFD_REQ_YES:
                                    validation.setMessage(message);
                                    break;
                                case SBankLayoutConsts.CFD_REQ_OPC:
                                    if (miClient.showMsgBoxConfirm(message + "\n" + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                                        validation.setMessage("Es necesario agregar el XML del CFD a las facturas:\n" + missingCfdDpsNumbers);
                                    }
                                    break;
                                default:
                                    // nothing
                            }
                        }
                        
                        if (validation.isValid()) {
                            if (validateSatStatus) {
                                String cfdiSatStatus = getCfdiSatStatus();
                                if (!cfdiSatStatus.isEmpty()) {
                                    validation.setMessage(cfdiSatStatus);
                                }
                            }
                        }
                    }
                }
            }
            else if (isModeForAccounting()) {
                try {
                    validatePayments();
                }
                catch (Exception e) {
                    validation.setMessage(e.getMessage());
                }
            }
            
            if (validation.isValid()) {
                try {
                    /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                    for (SSrvLock lock : maLocks) {
                        SSrvUtils.verifyLockStatus(miClient.getSession(), lock);
                    }
                    */
                    /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                    for (SRedisLock rlock : maRedisLocks) {
                        SRedisLockUtils.verifyLockStatus((SClientInterface) miClient, rlock);
                    }
                    */
                    for (SLock slock : maSLocks) {
                        SLockUtils.verifyLockStatus((SClientInterface)miClient, slock);
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
    public void actionCancel() {
        if (jbCancel.isEnabled()) {
            if (!mbShowConfirmCloseDialog) {
                close();
            }
            else if (miClient.showMsgBoxConfirm(SGuiConsts.MSG_CNF_FORM_CLS) == JOptionPane.YES_OPTION){
                close();
            }
        }
    }
    
    private void close() {
        try {
            /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
            for (SSrvLock lock : maLocks) {
                SSrvUtils.releaseLock(miClient.getSession(), lock);
            }
            */
            /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
            for (SRedisLock rlock : maRedisLocks) {
                SRedisLockUtils.releaseLock((SClientInterface) miClient, rlock);
            }
            */
            for (SLock slock : maSLocks) {
                SLockUtils.releaseLock((SClientInterface) miClient, slock);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }

        super.actionCancel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPickRecord) {
                actionPerformedPickRecord();
            }
            else if (button == jbPickExchangeRate) {
                actionPerformedPickExchangeRate();
            }
            else if (button == jbPickLayoutPath) {
                actionPerformedPickLayoutPath();
            }
            else if (button == jbGridRowsShow) {
                actionPerformedGridRowsShow();
            }
            else if (button == jbGridRowsClear) {
                actionPerformedGridRowsClear();
            }
            else if (button == jbExchangeRateReset) {
                actionPerformedExchangeRateResetOriginal();
            }
            else if (button == jbExchangeRateRefresh) {
                actionPerformedExchangeRateSetCurrent();
            }
            else if (button == jbGridRowsCheckAll) {
                actionPerformedGridRowsCheckAll();
            }
            else if (button == jbGridRowsUncheckAll) {
                actionPerformedGridRowsUncheckAll();
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
                else if (field == moKeyBankLayoutCurrency) {
                    itemStateChangedBankLayoutCurrency();
                }
                else if (field == moKeyBankAccountCash) {
                    itemStateChangedBankAccountCash();
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
        if (moGridPayments.getTable().getSelectedColumn() != -1) {
            switch (mnFormSubtype) {
                case SModSysConsts.FINX_LAY_BANK_ACC:
                    switch (moGridPayments.getTable().getSelectedColumn()) {
                        case COL_BANK_ACC_CHECK:
                            editingStoppedAccountingChecked();
                            break;
                        default:
                    }
                    break;

                case SModSysConsts.FINX_LAY_BANK_TRN_TP_PAY:
                    switch (moGridPayments.getTable().getSelectedColumn()) {
                        case COL_TRN_TP_PAY_CHECK:
                            editingStoppedPaymentChecked();
                            break;
                        case COL_TRN_TP_PAY_PAY:
                        case COL_TRN_TP_PAY_EXR:
                            editingStoppedPaymentEntered();
                            break;
                        case COL_TRN_TP_PAY_AGREE:
                        case COL_TRN_TP_PAY_AGREE_REF:
                            if (mnBankPaymentTypeId == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                                editingStoppedAgreement();
                            }
                            break;
                        default:
                    }
                    break;

                case SModSysConsts.FINX_LAY_BANK_TRN_TP_PREPAY:
                    switch (moGridPayments.getTable().getSelectedColumn()) {
                        case COL_TRN_TP_PREPAY_PREPAY:
                            editingStoppedPaymentEntered();
                            break;
                        case COL_TRN_TP_PREPAY_AGREE:
                        case COL_TRN_TP_PREPAY_AGREE_REF:
                            if (mnBankPaymentTypeId == SDataConstantsSys.FINS_TP_PAY_BANK_AGREE) {
                                editingStoppedAgreement();
                            }
                            break;
                        default:
                    }
                    break;

                default:
            }
        }
    }

    @Override
    public void editingCanceled(ChangeEvent e) {
        
    }

    private class BankLayoutType {
        public int BankLayout;
        public int BankLayoutType;
        public int BankPaymentType;
        public int BizPartnerBankId;

        /**
         * Creates new BankLayoutType.
         * @param bankLayoutType Bank layout type ID, PK of erp.finu_tp_lay_bank, i.e., bank & layout payment type.
         * @param bankPaymentType Bank payment type ID, PK of erp.fins_tp_pay_bank, i.e., layout payment type.
         * @param bankLayout Layout bank ID, virtual PK defined on erp.fins_tp_pay_bank, i.e., fixed bank's ID for layout purposes.
         * @param bizPartnerBankId Bank ID, PK of erp.bpsu_bp, i.e., bank's business partner ID.
         */
        public BankLayoutType(int bankLayout, int bankLayoutType, int bankPaymentType, int bizPartnerBankId) {
            BankLayout = bankLayout;
            BankLayoutType = bankLayoutType;
            BankPaymentType = bankPaymentType;
            BizPartnerBankId = bizPartnerBankId;
        }
    }
}

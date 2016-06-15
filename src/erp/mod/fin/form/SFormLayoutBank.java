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
import erp.mfin.form.SDialogRecordPicker;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SDbBankLayout;
import erp.mod.fin.db.SFinConsts;
import erp.mod.fin.db.SFinRecordLayout;
import erp.mod.fin.db.SLayoutBankDps;
import erp.mod.fin.db.SLayoutBankPayment;
import erp.mod.fin.db.SLayoutBankPaymentRow;
import erp.mod.fin.db.SLayoutBankRecord;
import erp.mod.fin.db.SLayoutBankRow;
import erp.mod.fin.db.SLayoutBankXmlRow;
import erp.mod.fin.db.SXmlBankLayout;
import erp.mod.fin.db.SXmlBankLayoutPayment;
import erp.mod.fin.db.SXmlBankLayoutPaymentDoc;
import erp.mtrn.data.SDataDps;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;
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
import sa.lib.gui.bean.SBeanForm;
import sa.lib.srv.SSrvLock;
import sa.lib.srv.SSrvUtils;
import sa.lib.xml.SXmlElement;

/**
 *
 * @author Juan Barajas
 */
public class SFormLayoutBank extends SBeanForm implements ActionListener, ItemListener, CellEditorListener {

    private static final int COL_APP_PAY = 2;
    private static final int COL_APP = 6;
    private static final int COL_BAL = 8;
    private static final int COL_BAL_BPS = 2;
    private static final int COL_ACC = 11;
    private static final int COL_EMAIL = 12;

    private boolean mbFirstTime;
    private int mnLayout;
    private int mnLayoutType;
    private int mnLayoutSubtype;
    private int mnLayoutBank;
    private int mnCurrencyId;
    private double mdBalanceTot;
    private double mdBalancePayed;
    private int mnNumberDocs;
    private int mnNumberTransfer;
    private int mnNumberRecordDistint;
    private String msLayoutText;
    private java.lang.String msAccountDebit;
    private java.lang.String msDebitFiscalId;
    private java.lang.String msAccountCredit;
    private ArrayList<SGuiItem> maAccountCredit;
    private ArrayList<SDataBizPartnerBranchBankAccount> maBranchBankAccountsCredit;
    private List<ArrayList<SGuiItem>> mltAccountCredit;
    private ArrayList<SLayoutBankXmlRow> maXmlRows = null;

    private erp.lib.table.STableCellEditorOptions moTableCellEditorOptions;
    private SLayoutBankRow moRow;
    private Vector<SLayoutBankRow> mvLayoutRows;
    private Vector<SGuiItem> mvLayoutTypes;
    private JButton jbSelectAll;
    private JButton jbCleanAll;
    private JCheckBox jckDateMaturityRo;
    private JCheckBox jckAccountAll;

    private SDbBankLayout moRegistry;
    private erp.mbps.data.SDataBizPartner moDataBizPartner;
    private erp.mfin.data.SDataAccountCash moDataAccountCash;
    private erp.mbps.data.SDataBizPartnerBranchBankAccount moDataBizPartnerBranchBankAccount;
    private erp.mfin.form.SDialogRecordPicker moDialogRecordPicker;
    private erp.mfin.data.SDataRecord moCurrentRecord;
    protected ArrayList<SLayoutBankRecord> maBankRecords;
    private SGridPaneForm moGridPayments;
    
    private HashMap<Integer, Object> moParamsMap;
    private ArrayList<SSrvLock> maLocks;

    /**
     * Creates new form SFormLayoutBank
     * @param client
     * @param formSubtype
     * @param title
     */
    public SFormLayoutBank(SGuiClient client, int formSubtype, String title) {
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

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jlDateDue = new javax.swing.JLabel();
        moDateDateDue = new sa.lib.gui.bean.SBeanFieldDate();
        jlDummy1 = new javax.swing.JLabel();
        jlDate = new javax.swing.JLabel();
        moDateDate = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel8 = new javax.swing.JPanel();
        jlPkLayouId = new javax.swing.JLabel();
        moKeyLayouId = new sa.lib.gui.bean.SBeanFieldKey();
        jlPkBankLayoutTypeId = new javax.swing.JLabel();
        moKeyBankLayoutType = new sa.lib.gui.bean.SBeanFieldKey();
        jlAccountDebit = new javax.swing.JLabel();
        moKeyAccountDebit = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel10 = new javax.swing.JPanel();
        jlConcept = new javax.swing.JLabel();
        moTextConcept = new sa.lib.gui.bean.SBeanFieldText();
        jlConsecutiveDay = new javax.swing.JLabel();
        moIntConsecutiveDay = new sa.lib.gui.bean.SBeanFieldInteger();
        jlRecord = new javax.swing.JLabel();
        jtfRecordDate = new javax.swing.JTextField();
        jtfRecordBranch = new javax.swing.JTextField();
        jtfRecordBkc = new javax.swing.JTextField();
        jtfRecordNumber = new javax.swing.JTextField();
        jbPickRecord = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jlLayoutPath = new javax.swing.JLabel();
        moTextLayoutPath = new sa.lib.gui.bean.SBeanFieldText();
        jbLayoutPath = new javax.swing.JButton();
        jlDummy2 = new javax.swing.JLabel();
        jbShowDocs = new javax.swing.JButton();
        jbCleanDocs = new javax.swing.JButton();
        jpSettings = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jtfRows = new javax.swing.JTextField();
        jlDummy = new javax.swing.JLabel();
        jlBalanceTot = new javax.swing.JLabel();
        moDecBalanceTot = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlBalanceTotPay = new javax.swing.JLabel();
        moDecBalanceTotPay = new sa.lib.gui.bean.SBeanFieldDecimal();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel23.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateDue.setText("Vencimiento:");
        jlDateDue.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel7.add(jlDateDue);
        jPanel7.add(moDateDateDue);

        jlDummy1.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel7.add(jlDummy1);

        jlDate.setText("Pago:");
        jlDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlDate);
        jPanel7.add(moDateDate);

        jPanel23.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPkLayouId.setText("Layout: *");
        jlPkLayouId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel8.add(jlPkLayouId);

        moKeyLayouId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel8.add(moKeyLayouId);

        jlPkBankLayoutTypeId.setText("Tipo layout: *");
        jlPkBankLayoutTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlPkBankLayoutTypeId);

        moKeyBankLayoutType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel8.add(moKeyBankLayoutType);

        jlAccountDebit.setText("Cuenta cargo: *");
        jlAccountDebit.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlAccountDebit);

        moKeyAccountDebit.setPreferredSize(new java.awt.Dimension(232, 23));
        jPanel8.add(moKeyAccountDebit);

        jPanel23.add(jPanel8);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlConcept.setText("Concepto/descripción:");
        jlConcept.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel10.add(jlConcept);

        moTextConcept.setText("sBeanFieldText1");
        moTextConcept.setPreferredSize(new java.awt.Dimension(309, 23));
        jPanel10.add(moTextConcept);

        jlConsecutiveDay.setText("Consecutivo día: *");
        jlConsecutiveDay.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlConsecutiveDay);

        moIntConsecutiveDay.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel10.add(moIntConsecutiveDay);

        jlRecord.setText("Póliza contable:");
        jlRecord.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlRecord);

        jtfRecordDate.setEditable(false);
        jtfRecordDate.setText("01/01/2000");
        jtfRecordDate.setToolTipText("Fecha de la póliza contable");
        jtfRecordDate.setFocusable(false);
        jtfRecordDate.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel10.add(jtfRecordDate);

        jtfRecordBranch.setEditable(false);
        jtfRecordBranch.setText("BRA");
        jtfRecordBranch.setToolTipText("Sucursal de la empresa");
        jtfRecordBranch.setFocusable(false);
        jtfRecordBranch.setPreferredSize(new java.awt.Dimension(32, 23));
        jPanel10.add(jtfRecordBranch);

        jtfRecordBkc.setEditable(false);
        jtfRecordBkc.setText("BKC");
        jtfRecordBkc.setToolTipText("Centro contable");
        jtfRecordBkc.setFocusable(false);
        jtfRecordBkc.setPreferredSize(new java.awt.Dimension(32, 23));
        jPanel10.add(jtfRecordBkc);

        jtfRecordNumber.setEditable(false);
        jtfRecordNumber.setText("TP-000001");
        jtfRecordNumber.setToolTipText("Número de póliza contable");
        jtfRecordNumber.setFocusable(false);
        jtfRecordNumber.setPreferredSize(new java.awt.Dimension(63, 23));
        jPanel10.add(jtfRecordNumber);

        jbPickRecord.setText("...");
        jbPickRecord.setToolTipText("Seleccionar póliza contable");
        jbPickRecord.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel10.add(jbPickRecord);

        jPanel23.add(jPanel10);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLayoutPath.setText("Ruta layout:*");
        jlLayoutPath.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jlLayoutPath);

        moTextLayoutPath.setEditable(false);
        moTextLayoutPath.setText("sBeanFieldText1");
        moTextLayoutPath.setPreferredSize(new java.awt.Dimension(309, 23));
        jPanel9.add(moTextLayoutPath);

        jbLayoutPath.setText("...");
        jbLayoutPath.setToolTipText("Seleccionar ruta de archivo layout...");
        jbLayoutPath.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel9.add(jbLayoutPath);

        jlDummy2.setPreferredSize(new java.awt.Dimension(272, 23));
        jPanel9.add(jlDummy2);

        jbShowDocs.setText("Mostar documentos");
        jbShowDocs.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbShowDocs.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel9.add(jbShowDocs);

        jbCleanDocs.setText("Limpiar documentos");
        jbCleanDocs.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbCleanDocs.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel9.add(jbCleanDocs);

        jPanel23.add(jPanel9);

        jPanel1.add(jPanel23, java.awt.BorderLayout.NORTH);

        jpSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Documentos con saldo:"));
        jpSettings.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jtfRows.setEditable(false);
        jtfRows.setText("000,000/000,000");
        jtfRows.setToolTipText("Renglón actual");
        jtfRows.setFocusable(false);
        jtfRows.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel2.add(jtfRows);

        jlDummy.setPreferredSize(new java.awt.Dimension(525, 23));
        jPanel2.add(jlDummy);

        jlBalanceTot.setText("Total layout:");
        jlBalanceTot.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jlBalanceTot);
        jPanel2.add(moDecBalanceTot);

        jlBalanceTotPay.setText("Total pago:");
        jlBalanceTotPay.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jlBalanceTotPay);
        jPanel2.add(moDecBalanceTotPay);

        jpSettings.add(jPanel2, java.awt.BorderLayout.SOUTH);

        jPanel1.add(jpSettings, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        actionCancel();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbCleanDocs;
    private javax.swing.JButton jbLayoutPath;
    private javax.swing.JButton jbPickRecord;
    private javax.swing.JButton jbShowDocs;
    private javax.swing.JLabel jlAccountDebit;
    private javax.swing.JLabel jlBalanceTot;
    private javax.swing.JLabel jlBalanceTotPay;
    private javax.swing.JLabel jlConcept;
    private javax.swing.JLabel jlConsecutiveDay;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlDateDue;
    private javax.swing.JLabel jlDummy;
    private javax.swing.JLabel jlDummy1;
    private javax.swing.JLabel jlDummy2;
    private javax.swing.JLabel jlLayoutPath;
    private javax.swing.JLabel jlPkBankLayoutTypeId;
    private javax.swing.JLabel jlPkLayouId;
    private javax.swing.JLabel jlRecord;
    private javax.swing.JPanel jpSettings;
    private javax.swing.JTextField jtfRecordBkc;
    private javax.swing.JTextField jtfRecordBranch;
    private javax.swing.JTextField jtfRecordDate;
    private javax.swing.JTextField jtfRecordNumber;
    private javax.swing.JTextField jtfRows;
    private sa.lib.gui.bean.SBeanFieldDate moDateDate;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateDue;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecBalanceTot;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecBalanceTotPay;
    private sa.lib.gui.bean.SBeanFieldInteger moIntConsecutiveDay;
    private sa.lib.gui.bean.SBeanFieldKey moKeyAccountDebit;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBankLayoutType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyLayouId;
    private sa.lib.gui.bean.SBeanFieldText moTextConcept;
    private sa.lib.gui.bean.SBeanFieldText moTextLayoutPath;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 640);

        moDialogRecordPicker = new SDialogRecordPicker((SClientInterface) miClient, SDataConstants.FINX_REC_USER);
        
        moDateDateDue.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateDue.getText()), true);
        moDateDate.setDateSettings(miClient, SGuiUtils.getLabelName(jlDate.getText()), true);
        moKeyLayouId.setKeySettings(miClient, SGuiUtils.getLabelName(jlPkLayouId.getText()), true);
        moKeyBankLayoutType.setKeySettings(miClient, SGuiUtils.getLabelName(jlPkBankLayoutTypeId.getText()), true);
        moKeyAccountDebit.setKeySettings(miClient, SGuiUtils.getLabelName(jlAccountDebit.getText()), true);
        moTextConcept.setTextSettings(SGuiUtils.getLabelName(jlConcept), 255);
        moIntConsecutiveDay.setIntegerSettings(SGuiUtils.getLabelName(jlConsecutiveDay), SGuiConsts.GUI_TYPE_INT, true);
        moTextLayoutPath.setTextSettings(SGuiUtils.getLabelName(jlLayoutPath), 255);
        moDecBalanceTot.setDecimalSettings(SGuiUtils.getLabelName(jlBalanceTot), SGuiConsts.GUI_TYPE_DEC_AMT, false);
        moDecBalanceTotPay.setDecimalSettings(SGuiUtils.getLabelName(jlBalanceTotPay), SGuiConsts.GUI_TYPE_DEC_AMT, false);
        
        moTableCellEditorOptions = new STableCellEditorOptions((SClientInterface) miClient);
        
        jbSelectAll = new JButton("Todo");
        jbSelectAll.setToolTipText("Pagar");
        jbSelectAll.setPreferredSize(new java.awt.Dimension(70, 23));

        jbCleanAll = new JButton("Nada");
        jbCleanAll.setToolTipText("Pagar");
        jbCleanAll.setPreferredSize(new java.awt.Dimension(70, 23));
        
        jckDateMaturityRo = new JCheckBox("Solo documentos del vecimiento especificado");
        jckDateMaturityRo.setPreferredSize(new Dimension(247, 23));
        jckAccountAll = new JCheckBox("Solo cuentas bancarias del layout especificado");
        jckAccountAll.setPreferredSize(new Dimension(247, 23));
        
        moFields.addField(moDateDateDue);
        moFields.addField(moDateDate);
        moFields.addField(moKeyLayouId);
        moFields.addField(moKeyBankLayoutType);
        moFields.addField(moKeyAccountDebit);
        moFields.addField(moTextConcept);
        moFields.addField(moIntConsecutiveDay);
        //moFields.addField(moTextLayoutPath); read only
        //moFields.addField(moDecBalanceTot); read only
        //moFields.addField(moDecBalanceTotPay); read only

        moFields.setFormButton(jbSave);

        moGridPayments = new SGridPaneForm(miClient, SModConsts.FIN_LAY_BANK, mnFormSubtype, "Documentos con saldo") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<SGridColumnForm>();
                SGridColumnForm column = null;
                
                if (mnFormSubtype == SModConsts.FIN_REC) {
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Proveedor", 300));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Clave proveedor", 50));
                    column = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Aplicar pago", moGridPayments.getTable().getDefaultEditor(Boolean.class));
                    column.setEditable(true);
                    gridColumnsForm.add(column);
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Monto pagar $"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda"));
                    column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "No. cuenta", 125);
                    column.setApostropheOnCsvRequired(true);
                    gridColumnsForm.add(column);
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_PER, "Período póliza"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CO, "Centro contable"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CO, "Sucursal empresa"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Folio póliza", 65));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha póliza"));
                }
                else if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_DPS) {
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Proveedor", 300));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Clave proveedor", 50));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_REG_NUM, "Tipo doc."));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Folio doc.", 100));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha doc."));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Sucursal empresa", STableConstants.WIDTH_CODE_COB));
                    column = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Aplicar pago", moGridPayments.getTable().getDefaultEditor(Boolean.class));
                    column.setEditable(true);
                    gridColumnsForm.add(column);
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Saldo $", STableConstants.WIDTH_VALUE_2X));
                    column = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Monto pagar $", moGridPayments.getTable().getDefaultEditor(Double.class));
                    column.setEditable(true);
                    gridColumnsForm.add(column);
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda"));
                    column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "No. cuenta", 125, moTableCellEditorOptions);
                    column.setEditable(true);
                    column.setApostropheOnCsvRequired(true);
                    gridColumnsForm.add(column);
                    column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "E-mail", 100, moGridPayments.getTable().getDefaultEditor(String.class));
                    column.setEditable(true);
                    gridColumnsForm.add(column);
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "RFC", 100));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Subtotal $"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Impuesto trasladado $"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Impuesto retenido $"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Total doc. $"));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha vencimiento"));
                }
                else {
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Proveedor", 300));
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Clave proveedor", 50));
                    column = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Monto pagar $", moGridPayments.getTable().getDefaultEditor(Double.class));
                    column.setEditable(true);
                    gridColumnsForm.add(column);
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda"));
                    column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "No. cuenta", 125, moTableCellEditorOptions);
                    column.setEditable(true);
                    column.setApostropheOnCsvRequired(true);
                    gridColumnsForm.add(column);
                    column = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "E-mail", 100, moGridPayments.getTable().getDefaultEditor(String.class));
                    column.setEditable(true);
                    gridColumnsForm.add(column);
                    gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "RFC", 100));
                }
                moGridPayments.getTable().getDefaultEditor(Double.class).addCellEditorListener(SFormLayoutBank.this);
                moGridPayments.getTable().getDefaultEditor(Boolean.class).addCellEditorListener(SFormLayoutBank.this);
                moGridPayments.getTable().getDefaultEditor(String.class).addCellEditorListener(SFormLayoutBank.this);
                moTableCellEditorOptions.addCellEditorListener(SFormLayoutBank.this);

                return gridColumnsForm;
            }
        };
        moGridPayments.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSelectAll);
        moGridPayments.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCleanAll);
        moGridPayments.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jckDateMaturityRo);
        moGridPayments.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jckAccountAll);
        
        //mvFormGrids.add(moGridAbpSettings);

        jpSettings.add(moGridPayments, BorderLayout.CENTER);
    }
    
    private boolean validateDebtsToPay() throws Exception {
        boolean isError = false;
        boolean isFound = false;
        double total = 0;
        Vector<SLayoutBankRow> vLayoutRows = new Vector<SLayoutBankRow>();
        SLayoutBankRow row = null;
        updateLayoutRow();

        for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
            row = (SLayoutBankRow) rowAux;
            
            for (int i = 0; i < mvLayoutRows.size(); i++) {
                if (SLibUtilities.compareKeys(row.getRowPrimaryKey(), mvLayoutRows.get(i).getRowPrimaryKey()) && mvLayoutRows.get(i).getIsForPayment()) {
                    if (mvLayoutRows.get(i).getAccountCredit().length() > 0) {
                        for (SLayoutBankRow layoutRow : vLayoutRows) {
                            isFound = false;

                            if (SLibUtilities.compareKeys(new int[] { layoutRow.getBizPartnerId(), }, new int[] { mvLayoutRows.get(i).getBizPartnerId() }) &&
                                   layoutRow.getAccountCredit().compareTo(mvLayoutRows.get(i).getAccountCredit()) == 0) {
                                total = layoutRow.getBalanceTotByBizPartner();
                                layoutRow.setBalanceTotByBizPartner(total + mvLayoutRows.get(i).getBalanceTotByBizPartner());
                                isFound = true;
                            }

                            if (isFound) {
                                break;
                            }
                        }

                        if (!isFound) {
                            vLayoutRows.add(mvLayoutRows.get(i));
                        }
                    }
                    else {
                        isError = true;
                        throw new Exception("No ha especificado la cuenta de abono para uno o mas pagos.");
                    }
                }
            }
            if (isError) {
                break;
            }
        }
        
        // Validate "alias" for payment with BanBajio Bank
        
        if (mnLayout == SFinConsts.LAY_BANK_BANBAJIO) {
            for (SLayoutBankRow layoutRow : vLayoutRows) {
                if (layoutRow.getBajioBankAlias().isEmpty()) {
                    isError = true;
                    throw new Exception("No se ha especificado el 'alias ' de la cuenta de abono '" + layoutRow.getAccountCredit() + "' del proveedor '" + layoutRow.getBizPartner() + "'.");
                }
            }
        }
        if (vLayoutRows.size() <= 0) {
            throw new Exception("No ha especificado ningún documento para pago.");
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
                    if (payRow.getFinRecordLayout() != null) {
                        if (!SLibUtils.compareKeys(payRow.getFinRecordLayout().getPrimaryKey(), moCurrentRecord.getPrimaryKey())) {
                            mnNumberRecordDistint++;
                        }
                    }
                }
            }
            
            for (SLayoutBankRecord bankRecordRow : maBankRecords) {
                if (!SLibUtils.compareKeys(bankRecordRow.getFinRecordLayout().getPrimaryKey(), moCurrentRecord.getPrimaryKey())) {
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
    
    private void enableFields(boolean enable) {
        moDateDateDue.setEditable(enable);
        moKeyLayouId.setEnabled(enable);
        moKeyBankLayoutType.setEnabled(enable);
        moKeyAccountDebit.setEnabled(enable);
        jbShowDocs.setEnabled(enable);
        jbCleanDocs.setEnabled(!enable);
        jckDateMaturityRo.setEnabled(!enable);
        jckAccountAll.setEnabled(!enable);
        
        if (mnFormSubtype == SModConsts.FIN_REC) {
            jbPickRecord.setEnabled(true);
            moDateDate.setEditable(false);
            moKeyAccountDebit.setEnabled(false);
            moTextConcept.setEditable(false);
            moIntConsecutiveDay.setEditable(false);
            jbLayoutPath.setEnabled(false);
            jbCleanDocs.setEnabled(false);
            jckDateMaturityRo.setEnabled(false);
            jckAccountAll.setEnabled(false);
        }
        else if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_ADV) {
            jckDateMaturityRo.setEnabled(false);
            jbSelectAll.setEnabled(false);
            jbCleanAll.setEnabled(false);
        }
    }

    private void renderBankLayoutSettings() {
        moTextConcept.setText("");

        if (moKeyBankLayoutType.getSelectedIndex() > 0) {
            mnLayout = ((int[]) ((SGuiItem) moKeyLayouId.getSelectedItem()).getPrimaryKey())[0];
            
            mnLayoutSubtype = ((int[]) ((SGuiItem) moKeyBankLayoutType.getSelectedItem()).getPrimaryKey())[0];
            mnLayoutType = ((int[]) ((SGuiItem) moKeyBankLayoutType.getSelectedItem()).getForeignKey())[0];
            mnLayoutBank = ((int[]) ((SGuiItem) moKeyBankLayoutType.getSelectedItem()).getForeignKey())[1];

            switch (mnLayoutType) {
                 case SDataConstantsSys.FINS_TP_PAY_BANK_THIRD:
                     moTextConcept.setEnabled(mnLayout == SFinConsts.LAY_BANK_SANTANDER);
                    break;
                case SDataConstantsSys.FINS_TP_PAY_BANK_TEF:
                     moTextConcept.setEnabled(mnLayout == SFinConsts.LAY_BANK_SANTANDER);
                    break;
                case SDataConstantsSys.FINS_TP_PAY_BANK_SPEI_FD_N:
                     moTextConcept.setEnabled(mnLayout == SFinConsts.LAY_BANK_SANTANDER || mnLayout == SFinConsts.LAY_BANK_HSBC);
                    break;
                case SDataConstantsSys.FINS_TP_PAY_BANK_SPEI_FD_Y:
                     moTextConcept.setEnabled(mnLayout == SFinConsts.LAY_BANK_SANTANDER);
                    break;
                default :
                    break;
            }
            moIntConsecutiveDay.setEnabled(mnLayout == SFinConsts.LAY_BANK_BANBAJIO);
        }
        else {
            moTextConcept.setEnabled(false);
            moIntConsecutiveDay.setEnabled(false);
        }
    }

    private void renderAccountSettings() {
        if (moKeyAccountDebit.getSelectedIndex() > 0) {
            moDataAccountCash = (SDataAccountCash) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.FIN_ACC_CASH, moKeyAccountDebit.getValue(), SLibConstants.EXEC_MODE_SILENT);

            moDataBizPartnerBranchBankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BANK_ACC, new int[] { moDataAccountCash.getFkBizPartnerBranchId_n(), moDataAccountCash.getFkBankAccountId_n() }, SLibConstants.EXEC_MODE_SILENT);
            mnCurrencyId = moDataBizPartnerBranchBankAccount.getFkCurrencyId();
            msAccountDebit = moDataBizPartnerBranchBankAccount.getBankAccountNumber();
            
            moParamsMap = new HashMap<Integer, Object>();
            moParamsMap.put(SDataConstants.FIN_ACC_COB_ENT, moDataAccountCash.getPkCompanyBranchId());
            moParamsMap.put(SDataConstants.FIN_ACC_CASH, moDataAccountCash.getPkAccountCashId());
        }
        else {
            moDataBizPartnerBranchBankAccount = null;
            mnCurrencyId = 0;
        }
    }

    @SuppressWarnings("unchecked")
    private void renderBankAccountCredit(int nBizPartnerBranch, int nBankId) {
        String sql = "";
        ResultSet resultSet = null;
        Statement statementAux;
        SDataBizPartnerBranchBankAccount bizPartnerBranchBankAccount = null;
        msAccountCredit = "";
        HashSet<String> oAccountCreditAux;
        String accountCredit = "";
        int bpbId = 0;
        int bpbBankAccId = 0;

        try {
            sql = "SELECT b.id_bpb, b.id_bank_acc, b.acc_num, b.acc_num_std, b.fid_bank, b.b_def, COALESCE(lay.fid_tp_pay_bank, 0) AS f_tp_pay, " +
                    "COALESCE(lay.id_tp_lay_bank, 0) AS f_tp_lay, bp.code_bank_san, bp.code_bank_baj, b.alias_baj " +
                    "FROM erp.bpsu_bank_acc AS b " +
                    "LEFT OUTER JOIN erp.bpsu_bank_acc_lay_bank AS l ON b.id_bpb = l.id_bpb AND b.id_bank_acc = l.id_bank_acc " +
                    "LEFT OUTER JOIN erp.finu_tp_lay_bank AS lay ON l.id_tp_lay_bank = lay.id_tp_lay_bank " +
                    "LEFT OUTER JOIN erp.bpsu_bp AS bp ON bp.id_bp = b.fid_bank " +
                    "WHERE b.b_del = 0 AND b.fid_cur = " + mnCurrencyId + " AND b.id_bpb = " + nBizPartnerBranch +
                    (mnLayoutType == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? " AND b.fid_bank = " + nBankId : " AND b.fid_bank <> " + nBankId) + " AND lay.id_tp_lay_bank = " + mnLayoutSubtype + " ";

            oAccountCreditAux = new HashSet<String>();
            maBranchBankAccountsCredit = new ArrayList<SDataBizPartnerBranchBankAccount>();
            maAccountCredit = new ArrayList<SGuiItem>();

            statementAux = miClient.getSession().getStatement().getConnection().createStatement();

            resultSet = statementAux.executeQuery(sql);
            while (resultSet.next()) {
                bpbId = resultSet.getInt(1);
                bpbBankAccId = resultSet.getInt(2);
                
                if (resultSet.getBoolean(6) && resultSet.getInt(7) == mnLayoutType) {
                    msAccountCredit = (mnLayoutType != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? resultSet.getString(4) : resultSet.getString(3));
                }
                if (msAccountCredit.length() == 0 && resultSet.getInt(7) == mnLayoutType) {
                    msAccountCredit = (mnLayoutType != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? resultSet.getString(4) : resultSet.getString(3));
                }
                accountCredit = mnLayoutType != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? resultSet.getString(4) : resultSet.getString(3);

                if (resultSet.getInt(7) == mnLayoutType && resultSet.getInt(8) == mnLayoutSubtype) {
                    oAccountCreditAux.add(accountCredit);
                }
                moRow.getCodeBankAccountCredit().put(mnLayoutType != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? resultSet.getString(4) : resultSet.getString(3), mnLayout == SFinConsts.LAY_BANK_BANBAJIO ? resultSet.getString(10) : resultSet.getString(9));
                moRow.getAliasBankAccountCredit().put(mnLayoutType != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? resultSet.getString(4) : resultSet.getString(3), resultSet.getString(11));
                
                bizPartnerBranchBankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BANK_ACC, new int[] { bpbId, bpbBankAccId }, SLibConstants.EXEC_MODE_SILENT); 
            
                maBranchBankAccountsCredit.add(bizPartnerBranchBankAccount);
            }
            
            for (String s : oAccountCreditAux) {
                maAccountCredit.add(new SGuiItem(new int[] { bpbId, bpbBankAccId }, s));
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
            jtfRecordBkc.setText("");
            jtfRecordBranch.setText("");
            jtfRecordNumber.setText("");
        }
        else {
            jtfRecordDate.setText(((SClientInterface) miClient).getSessionXXX().getFormatters().getDateFormat().format(moCurrentRecord.getDate()));
            jtfRecordBkc.setText(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.FIN_BKC, new int[] { moCurrentRecord.getPkBookkeepingCenterId() }, SLibConstants.DESCRIPTION_CODE));
            jtfRecordBranch.setText(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.BPSU_BPB, new int[] { moCurrentRecord.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
            jtfRecordNumber.setText(moCurrentRecord.getRecordNumber());
        }
    }

    private void renderBizPartner(int bizPartnerId) {
        moDataBizPartner = (SDataBizPartner) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BP, new int[] { bizPartnerId }, SLibConstants.EXEC_MODE_SILENT);

        if (moDataBizPartner != null) {
            msDebitFiscalId = moDataBizPartner.getFiscalId();
        }
    }
    
    private void renderLayoutTypeBank() {
        String sql = "";
        ResultSet resultSet = null;
        
        try {
           sql = "SELECT id_tp_lay_bank AS f_id_1, tp_lay_bank AS f_item, fid_tp_pay_bank AS f_fid_1, fid_bank AS f_fid_2, lay_bank AS f_fid_3 " +
                "FROM erp.finu_tp_lay_bank " +
                "WHERE b_del = 0 " +
                "ORDER BY tp_lay_bank, id_tp_lay_bank ";

            mvLayoutTypes = new Vector<SGuiItem>();
            mvLayoutTypes.add(new SGuiItem(new int[] { SLibConsts.UNDEFINED }, "(" + SUtilConsts.TXT_SELECT + " layout)"));

            resultSet = miClient.getSession().getStatement().executeQuery(sql);
            while (resultSet.next()) {
                mvLayoutTypes.add(new SGuiItem(new int[] { resultSet.getInt("f_id_1") }, resultSet.getString("f_item"), new int[] { resultSet.getInt("f_fid_1"), resultSet.getInt("f_fid_2") }, new int[] { resultSet.getInt("f_fid_3") }));
            }
            
            moKeyBankLayoutType.removeAllItems();
            for (int i = 0; i < mvLayoutTypes.size(); i++) {
                moKeyBankLayoutType.addItem(mvLayoutTypes.get(i));
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }
    
    private void populateLayoutType() {
        SGuiItem item = null;
        
        if (moKeyLayouId.getSelectedIndex() > 0) {
            moKeyBankLayoutType.removeAllItems();
            for (int i = 0; i < mvLayoutTypes.size(); i++) {
                item = mvLayoutTypes.get(i);
                if (SLibUtils.compareKeys(item.getPrimaryKey(), new int[] { SLibConsts.UNDEFINED }) || SLibUtils.compareKeys((int[]) item.getComplement(), new int[] { moKeyLayouId.getValue()[0] })) {
                    moKeyBankLayoutType.addItem(item);
                }
            }
            moKeyBankLayoutType.setEnabled(true);
        }
        else {
            moKeyBankLayoutType.setEnabled(false);
        }
    }
    
    private void populateLayoutBank() {
        Vector<SGuiItem> layouts = new Vector<SGuiItem>();

        layouts.add(new SGuiItem(new int[] { SLibConsts.UNDEFINED }, "(" + SUtilConsts.TXT_SELECT + " layout)"));
        layouts.add(new SGuiItem(new int[] { SFinConsts.LAY_BANK_HSBC }, SFinConsts.TXT_LAY_BANK_HSBC));
        layouts.add(new SGuiItem(new int[] { SFinConsts.LAY_BANK_SANTANDER }, SFinConsts.TXT_LAY_BANK_SANTANDER));
        layouts.add(new SGuiItem(new int[] { SFinConsts.LAY_BANK_BANBAJIO }, SFinConsts.TXT_LAY_BANK_BANBAJIO));
        layouts.add(new SGuiItem(new int[] { SFinConsts.LAY_BANK_BBVA }, SFinConsts.TXT_LAY_BANK_BBVA));
        
        moKeyLayouId.removeAllItems();
        for (int i = 0; i < layouts.size(); i++) {
            moKeyLayouId.addItem(layouts.get(i));
        }
    }

    private void itemStateChangedBankLayoutTypeId() {
        moTextLayoutPath.setText("");
        renderBankLayoutSettings();
    }

    private void itemStateChangedAccountDebit() {
        renderAccountSettings();
    }
    
    private void itemStateChangedLayout() {
        populateLayoutType();
    }
    
    private void actionSelectAll() {
        SLayoutBankRow row = null;
        SLayoutBankPaymentRow payRow = null;
        SFinRecordLayout recordLayout = null;
        SLayoutBankPayment bankPayment = null;
        SLayoutBankPayment bankPaymentAux = null;
        SGuiValidation validation = validateForm();
        
        try {
            if (validation.isValid()) {
                if (mnFormSubtype == SModConsts.FIN_REC) {
                    if (validateRecord(true)) {
                        for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
                            payRow = (SLayoutBankPaymentRow) rowAux;
                            bankPaymentAux = payRow.getLayoutBankPayment().clone();
                            bankPayment = payRow.getLayoutBankPayment().clone();
                            
                            bankPaymentAux.setAction(2); // 1: make payment, 2: remove payment
                            computeBankRecords(bankPaymentAux, payRow.getFinRecordLayout());
                            
                            payRow.setIsForPayment(true);
                            payRow.setRecordPeriod(moCurrentRecord.getRecordPeriod());
                            payRow.setRecordBkc(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.FIN_BKC, new int[] { moCurrentRecord.getPkBookkeepingCenterId() }, SLibConstants.DESCRIPTION_CODE));
                            payRow.setRecordCob(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.BPSU_BPB, new int[] { moCurrentRecord.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
                            payRow.setRecordNumber(moCurrentRecord.getRecordNumber());
                            payRow.setRecordDate(moCurrentRecord.getDate());

                            bankPayment.setAction(1); // 1: make payment, 2: remove payment
                            recordLayout = new SFinRecordLayout(moCurrentRecord.getPkYearId(), moCurrentRecord.getPkPeriodId(), moCurrentRecord.getPkBookkeepingCenterId(), moCurrentRecord.getPkRecordTypeId(), moCurrentRecord.getPkNumberId());
                            
                            payRow.setFinRecordLayout(recordLayout);
                            computeBankRecords(bankPayment, recordLayout);
                        }
                    }
                }
                else {
                    for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
                        row = (SLayoutBankRow) rowAux;
                        row.setIsForPayment(true);
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

    private void actionCleanAll() {
        SLayoutBankRow row = null;
        SLayoutBankPaymentRow payRow = null;
        SFinRecordLayout recordLayout = null;
        SLayoutBankPayment bankPayment = null;
        
        try {
            if (mnFormSubtype == SModConsts.FIN_REC) {
                for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
                    payRow = (SLayoutBankPaymentRow) rowAux;
                    bankPayment = payRow.getLayoutBankPayment().clone();

                    payRow.setIsForPayment(false);
                    payRow.setRecordPeriod("");
                    payRow.setRecordBkc("");
                    payRow.setRecordCob("");
                    payRow.setRecordNumber("");
                    payRow.setRecordDate(null);
                    payRow.setFinRecordLayout(null);

                    bankPayment.setAction(payRow.getIsToPayed() ? 2 : SLibConsts.UNDEFINED); // 1: make payment, 2: remove payment
                    recordLayout = payRow.getFinRecordLayout();
                    
                    computeBankRecords(bankPayment, recordLayout);
                }
            }
            else {
                for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
                    row = (SLayoutBankRow) rowAux;
                    row.setIsForPayment(false);
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
    
    private void actionLoadLayoutPath() {
        String nameFile = (moKeyBankLayoutType.getSelectedIndex() <= 0 ? "" : SLibUtils.textToAscii(moKeyBankLayoutType.getSelectedItem().getItem().toLowerCase().replaceAll("/", " ")));
        
        try {
            nameFile = SLibUtils.validateSafePath(((SClientInterface) miClient).getSessionXXX().getFormatters().getFileNameDatetimeFormat().format(new java.util.Date()) + " " + nameFile + ".txt");
        
            miClient.getFileChooser().setSelectedFile(new File(nameFile));
            if (miClient.getFileChooser().showSaveDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
                moTextLayoutPath.setValue(miClient.getFileChooser().getSelectedFile().getAbsolutePath());
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
    }
    
    private void actionShowDocs() {
        SGuiValidation validation = validateForm();
        
        if (validation.isValid()) {
            enableFields(false);
            if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_DPS) {
                showDps(true);
            }
            else {
                showSupplier(true);
            }
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
    
    private void actionCleanDocs() {
        enableFields(true);
        jckDateMaturityRo.setSelected(true);
        jckAccountAll.setSelected(true);
        jckDateMaturityRo.setEnabled(false);
        jckAccountAll.setEnabled(false);
        moGridPayments.clearGridRows();
    }

    public void actionPickRecord() {
        Object key = null;
        String message = "";

        moDialogRecordPicker.formReset();
        moDialogRecordPicker.setFilterKey(moDateDate.getValue());
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
    
    private void createLayoutXml() {
        SLayoutBankRow row = null;
        SLayoutBankXmlRow xmlRow = null;
        updateLayoutRow();
        
        maXmlRows = new ArrayList<SLayoutBankXmlRow>();
        for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
            row = (SLayoutBankRow) rowAux;
            
            for (int i = 0; i < mvLayoutRows.size(); i++) {
                if (SLibUtilities.compareKeys(row.getRowPrimaryKey(), mvLayoutRows.get(i).getRowPrimaryKey()) && (mvLayoutRows.get(i).getIsForPayment() || mvLayoutRows.get(i).getIsToPayed())) {
                    xmlRow = new SLayoutBankXmlRow();

                    xmlRow.setLayoutXmlRowType(mvLayoutRows.get(i).getLayoutRowType());
                    xmlRow.setDpsYear(row.getPkYearId());
                    xmlRow.setDpsDoc(row.getPkDocId());
                    xmlRow.setAmount(row.getBalanceTot());
                    xmlRow.setAmountPayed(row.getBalancePayed());
                    xmlRow.setIsToPayed(false);
                    xmlRow.setBizPartner(row.getBizPartnerId());
                    xmlRow.setBizPartnerBranch(row.getBranchBankAccountCreditId(row.getAccountCredit(), mnLayoutType)[0]);
                    xmlRow.setBizPartnerBranchAccount(row.getBranchBankAccountCreditId(row.getAccountCredit(), mnLayoutType)[1]);
                    xmlRow.setHsbcBankCode(row.getBankKey());
                    xmlRow.setHsbcAccountType(row.getAccType());
                    xmlRow.setHsbcFiscalIdDebit(row.getBizPartnerDebitFiscalId());
                    xmlRow.setHsbcFiscalIdCredit(row.getBizPartnerCreditFiscalId());
                    xmlRow.setHsbcFiscalVoucher(row.getCf());
                    xmlRow.setSantanderBankCode(row.getSantanderBankCode());
                    xmlRow.setConcept(row.getConcept());
                    xmlRow.setDescription(row.getDescription());
                    xmlRow.setReference(row.getReference());
                    xmlRow.setBajioBankCode(row.getBajioBankCode());
                    xmlRow.setBajioBankNick(row.getBajioBankAlias());
                    xmlRow.setBankKey(row.getBankKey());
                    xmlRow.setRecYear(SLibConsts.UNDEFINED);
                    xmlRow.setRecPeriod(SLibConsts.UNDEFINED);
                    xmlRow.setRecBookkeepingCenter(SLibConsts.UNDEFINED);
                    xmlRow.setRecRecordType("");
                    xmlRow.setRecNumber(SLibConsts.UNDEFINED);
                    xmlRow.setBookkeepingYear(SLibConsts.UNDEFINED);
                    xmlRow.setBookkeepingNumber(SLibConsts.UNDEFINED);
                    
                    
                    if (mvLayoutRows.get(i).getIsForPayment()) {
                        maXmlRows.add(xmlRow);
                    }
                }
            }
        }
    }
    
    private void loadPaymentsXml() {
        boolean found = false;
        double balancePayment = 0;
        double balanceDoc = 0;
        int dpsYearId = 0;
        int dpsDocId = 0;
        int recUserId = 0;
        SXmlBankLayoutPayment layoutPay = null;
        SXmlBankLayoutPaymentDoc layoutPayDoc = null;
        Vector<SGridRow> rows = new Vector<SGridRow>();
        Vector<SLayoutBankPaymentRow> rowsAux = new Vector<SLayoutBankPaymentRow>();
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
        SFinRecordLayout recordLayout = null;
        
        try {
            gridXml.processXml(moRegistry.getLayoutXml());
            
            for (SXmlElement element : gridXml.getXmlElements()) {
                if (element instanceof SXmlBankLayoutPayment) {
                    found = false;
                    
                    // Payment:

                    layoutPay = (SXmlBankLayoutPayment) element;
                    
                    bizPartnerBranchCob = (SDataBizPartnerBranch) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BPB, new int[] { (int) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).getValue() }, SLibConstants.EXEC_MODE_SILENT);
                    branchBankAccount = (SDataBizPartnerBranchBankAccount) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BANK_ACC, new int[] { (int) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).getValue(), (int) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).getValue() }, SLibConstants.EXEC_MODE_SILENT);
                    bizPartner = (SDataBizPartner) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BP, new int[] { bizPartnerBranchCob.getFkBizPartnerId() }, SLibConstants.EXEC_MODE_SILENT);
                    balancePayment = (double) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_AMT).getValue();
                    
                    moRow = new SLayoutBankPaymentRow(miClient);

                    moRow.setBizPartnerId(bizPartner.getPkBizPartnerId());
                    moRow.setBizPartnerBranchId(branchBankAccount.getPkBizPartnerBranchId());
                    moRow.setBizPartnerBranchAccountId(branchBankAccount.getPkBankAccountId());
                    moRow.setBizPartner(bizPartner.getBizPartner());
                    moRow.setBizPartnerKey(bizPartner.getDbmsCategorySettingsSup().getKey());
                    moRow.setBalance(balancePayment);
                    moRow.setBalanceTot(0);
                    moRow.setCurrencyKey(branchBankAccount.getDbmsCurrencyKey());
                    moRow.setAccountCredit(mnLayoutType != SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? branchBankAccount.getBankAccountNumberStd(): branchBankAccount.getBankAccountNumber());
                    moRow.setIsForPayment((boolean) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).getValue());
                    moRow.setIsToPayed((boolean) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).getValue());
                    
                    if ((Integer) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR).getValue() != 0) {
                        recordLayout = new SFinRecordLayout((Integer) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_YEAR).getValue(), (Integer) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_PER).getValue(),
                                         (Integer) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_BKC).getValue(), (String) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_REC_TP).getValue(), (Integer) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_REC_NUM).getValue());
                        moRow.setFinRecordLayout(recordLayout);
                        moRow.setFinRecordLayoutOld(recordLayout);
                        
                        record = (SDataRecord) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.FIN_REC, recordLayout.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                        
                        moRow.setRecordPeriod(record.getRecordPeriod());
                        moRow.setRecordBkc(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.FIN_BKC, new int[] { record.getPkBookkeepingCenterId() }, SLibConstants.DESCRIPTION_CODE));
                        moRow.setRecordCob(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.BPSU_BPB, new int[] { record.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
                        moRow.setRecordNumber(record.getRecordNumber());
                        moRow.setRecordDate(record.getDate());
                    }
                    else {
                        recordLayout = null;
                    }
                    
                    moPayment = new SLayoutBankPayment(SLibConsts.UNDEFINED, bizPartner.getPkBizPartnerId(), branchBankAccount.getPkBizPartnerBranchId(), branchBankAccount.getPkBankAccountId());
                    
                    moPayment.setCurrencyId(branchBankAccount.getFkCurrencyId());
                    moPayment.setAmount(balancePayment);
                    moPayment.setFkBookkeepingYearId_n((Integer) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_YEAR).getValue());
                    moPayment.setFkBookkeepingNumberId_n((Integer) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BKK_NUM).getValue());
                    moPayment.setAction(SLibConsts.UNDEFINED);
                    
                    // create layout bank document:
                    
                    for (SXmlElement elementDoc : layoutPay.getXmlElements()) {
                        if (elementDoc instanceof SXmlBankLayoutPaymentDoc) {
                            layoutPayDoc = (SXmlBankLayoutPaymentDoc) elementDoc;
                            
                            dpsYearId = (Integer) layoutPayDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR).getValue();
                            dpsDocId = (Integer) layoutPayDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC).getValue();
                            dps = (SDataDps) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_DPS, new int[] { dpsYearId, dpsDocId }, SLibConstants.EXEC_MODE_SILENT);
                            balanceDoc = (double) layoutPayDoc.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT).getValue();
                            
                            if (dps != null) {
                                moDps = new SLayoutBankDps(dpsYearId, dpsDocId, dps.getFkDpsCategoryId(), dps.getFkDpsClassId(), dps.getFkDpsTypeId(), dps.getFkCurrencyId(), recUserId, balanceDoc, dps.getExchangeRate());
                            }
                            
                            if (dpsYearId != SLibConsts.UNDEFINED && dpsDocId != SLibConsts.UNDEFINED) {
                                moPayment.setLayoutRowType(SModSysConsts.FIN_LAY_BANK_DPS);
                            }
                            else {
                                moPayment.setLayoutRowType(SModSysConsts.FIN_LAY_BANK_ADV);
                            }
                            
                            moPayment.setExcRate(dps != null ? dps.getExchangeRate() : SLibConsts.UNDEFINED);
                            moPayment.getLayoutBankDps().add(moDps);
                            
                            xmlRow = new SLayoutBankXmlRow();

                            xmlRow.setLayoutXmlRowType(moPayment.getLayoutPaymentType());
                            xmlRow.setDpsYear(dps != null ? dps.getPkYearId() : SLibConsts.UNDEFINED);
                            xmlRow.setDpsDoc(dps != null ? dps.getPkDocId() : SLibConsts.UNDEFINED);
                            xmlRow.setAmount(balanceDoc);
                            xmlRow.setAmountPayed(balanceDoc);
                            xmlRow.setIsToPayed(moRow.getIsToPayed());
                            xmlRow.setBizPartner(bizPartner.getPkBizPartnerId());
                            xmlRow.setBizPartnerBranch(branchBankAccount.getPkBizPartnerBranchId());
                            xmlRow.setBizPartnerBranchAccount(branchBankAccount.getPkBankAccountId());
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

                            if (recordLayout != null) {
                                xmlRow.setRecYear(recordLayout.getPkYearId());
                                xmlRow.setRecPeriod(recordLayout.getPkPeriodId());
                                xmlRow.setRecBookkeepingCenter(recordLayout.getPkBookkeepingCenterId());
                                xmlRow.setRecRecordType(recordLayout.getPkRecordTypeId());
                                xmlRow.setRecNumber(recordLayout.getPkNumberId());
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
            moGridPayments.getTable().setRowSorter(new TableRowSorter<AbstractTableModel>(moGridPayments.getModel()));
            moGridPayments.getTable().getTableHeader().setEnabled(false);
            
            if (moGridPayments.getTable().getRowCount() > 0) {
                moGridPayments.renderGridRows();
                moGridPayments.setSelectedGridRow(0);
                moTableCellEditorOptions.setAccounts(mltAccountCredit);
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
                        
                        if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_DPS) {
                            key = new int[] { (Integer) layoutDps.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_YEAR).getValue(), (Integer) layoutDps.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_DPS_DOC).getValue() };
                        }
                        else if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_ADV) {
                            key = new int[] { (int) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BP).getValue() };
                        }
                        
                        if (mvLayoutRows != null) {
                            for (int i = 0; i < mvLayoutRows.size(); i++) {
                                if (SLibUtilities.compareKeys(key, mvLayoutRows.get(i).getRowPrimaryKey())) {
                                    foundNum++; 
                                    mvLayoutRows.get(i).setIsForPayment(true);
                                    mvLayoutRows.get(i).setIsToPayed((boolean) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_APPLIED).getValue());
                                    mvLayoutRows.get(i).setBalanceTot((double) layoutDps.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT).getValue());
                                    mvLayoutRows.get(i).setBalancePayed((double) layoutDps.getAttribute(SXmlBankLayoutPaymentDoc.ATT_LAY_ROW_AMT).getValue());
                                    mvLayoutRows.get(i).setAccountCredit(mvLayoutRows.get(i).getBranchBankAccountCreditNumber(new int[] { (int) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BP).getValue(), (int) layoutPay.getAttribute(SXmlBankLayoutPayment.ATT_LAY_PAY_BANK_BANK).getValue() }, mnLayoutType));
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
    
    private void computeBankRecords(SLayoutBankPayment bankPayment, SFinRecordLayout recordLayout) throws Exception {
        boolean found = false;
        SDataRecord record = null;
        SLayoutBankRecord bankRecord = null;
        SSrvLock lock = null;
        
        if (recordLayout == null) {
            for (SLayoutBankRecord bankRecordRow : maBankRecords) {
                bankRecordRow.removeLayoutBankPayment(bankPayment.getBizPartnerId(), bankPayment.getBizPartnerBranchId(), bankPayment.getBizPartnerBranchAccountId());
            }
        }
        else {
            for (SLayoutBankRecord bankRecordRow : maBankRecords) {
                if (SLibUtils.compareKeys(bankRecordRow.getFinRecordLayout().getPrimaryKey(), recordLayout.getPrimaryKey())) {
                    bankRecordRow.getLayoutBankPayments().add(bankPayment);
                    found = true;
                    break;
                }
            }

            if (!found) {
                record = (SDataRecord) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.FIN_REC, recordLayout.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);
                lock = SSrvUtils.gainLock(miClient.getSession(), ((SClientInterface) miClient).getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.FIN_REC, recordLayout.getPrimaryKey(), record.getRegistryTimeout());
                maLocks.add(lock);

                bankRecord = new SLayoutBankRecord(recordLayout);
                bankRecord.getLayoutBankPayments().add(bankPayment);
                maBankRecords.add(bankRecord);
            }
        }
    }

    private void computeBalancePayment() {
        SLayoutBankRow row = null;
        SLayoutBankPaymentRow payRow = null;
        mdBalanceTot = 0;
        mdBalancePayed = 0;
        mnNumberDocs = 0;
        
        jtfRows.setText(SLibUtils.DecimalFormatInteger.format(mnNumberDocs) + "/" + SLibUtils.DecimalFormatInteger.format(moGridPayments.getModel().getRowCount()));
        if (mnFormSubtype == SModConsts.FIN_REC) {
            moDecBalanceTotPay.setValue(mdBalancePayed);
            
            for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
                payRow = (SLayoutBankPaymentRow) rowAux;

                if (payRow.getIsForPayment()) {
                    if (payRow.getBalanceTot() == 0) {
                        payRow.setBalanceTot(payRow.getBalance());
                    }
                    mdBalancePayed += payRow.getBalanceTot();
                    mnNumberDocs++;
                }
                else {
                    payRow.setBalanceTot(0d);
                    mdBalancePayed -= payRow.getBalanceTot();
                }
                moDecBalanceTotPay.setValue(mdBalancePayed);
                jtfRows.setText(SLibUtils.DecimalFormatInteger.format(mnNumberDocs) + "/" + SLibUtils.DecimalFormatInteger.format(moGridPayments.getModel().getRowCount()));
            }
        }
        else {
            moDecBalanceTot.setValue(mdBalanceTot);
            
            for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
                row = (SLayoutBankRow) rowAux;

                if (row.getIsForPayment()) {
                    if (row.getBalanceTot() == 0) {
                        row.setBalanceTot(row.getBalance());
                    }
                    mdBalanceTot += row.getBalanceTot();
                    mnNumberDocs++;
                }
                else {
                    row.setBalanceTot(0d);
                    mdBalanceTot -= row.getBalanceTot();
                }
                moDecBalanceTot.setValue(mdBalanceTot);
                jtfRows.setText(SLibUtils.DecimalFormatInteger.format(mnNumberDocs) + "/" + SLibUtils.DecimalFormatInteger.format(moGridPayments.getModel().getRowCount()));
            }
        }
    }

    private void processEditingAppPayment() {
        int index = -1;
        boolean addRecord = false;
        SLayoutBankRow row = null;
        SLayoutBankPaymentRow payRow = null;
        SFinRecordLayout recordLayout = null;
        SLayoutBankPayment bankPayment = null;
        
        index = moGridPayments.getTable().getSelectedRow();
        if (mnFormSubtype == SModConsts.FIN_REC) {
            payRow = (SLayoutBankPaymentRow) moGridPayments.getGridRow(index);
        }
        else {
            row = (SLayoutBankRow) moGridPayments.getGridRow(index);
        }
        
        try {
            if (mnFormSubtype == SModConsts.FIN_REC) {
                bankPayment = payRow.getLayoutBankPayment().clone();

                if (payRow.getIsForPayment()) {
                    if (validateRecord(false)) {
                        payRow.setIsForPayment(true);
                        payRow.setRecordPeriod(moCurrentRecord.getRecordPeriod());
                        payRow.setRecordBkc(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.FIN_BKC, new int[] { moCurrentRecord.getPkBookkeepingCenterId() }, SLibConstants.DESCRIPTION_CODE));
                        payRow.setRecordCob(SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.BPSU_BPB, new int[] { moCurrentRecord.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
                        payRow.setRecordNumber(moCurrentRecord.getRecordNumber());
                        payRow.setRecordDate(moCurrentRecord.getDate());

                        bankPayment.setAction(1); // 1: make payment, 2: remove payment
                        recordLayout = new SFinRecordLayout(moCurrentRecord.getPkYearId(), moCurrentRecord.getPkPeriodId(), moCurrentRecord.getPkBookkeepingCenterId(), moCurrentRecord.getPkRecordTypeId(), moCurrentRecord.getPkNumberId());
                        addRecord = true;
                        payRow.setFinRecordLayout(recordLayout);
                    }
                }
                else {
                    payRow.setIsForPayment(false);
                    payRow.setRecordPeriod("");
                    payRow.setRecordBkc("");
                    payRow.setRecordCob("");
                    payRow.setRecordNumber("");
                    payRow.setRecordDate(null);
                    payRow.setFinRecordLayout(null);

                    bankPayment.setAction(payRow.getIsToPayed() ? 2 : SLibConsts.UNDEFINED); // 1: make payment, 2: remove payment
                    recordLayout = payRow.getFinRecordLayout();
                    addRecord = true;
                }
                if (addRecord) {
                    computeBankRecords(bankPayment, recordLayout);
                }
            }
            else {
                if (row.getIsForPayment()) {
                    row.setIsForPayment(true);
                }
                else {
                    row.setIsForPayment(false);
                }
            }
            computeBalancePayment();
            moGridPayments.renderGridRows();
            //moGridAbpSettings.setSelectedGridRow(index);
            moGridPayments.getTable().setRowSelectionInterval(index, index);
        }
        catch (Exception e) {
            if (mnFormSubtype == SModConsts.FIN_REC) {
                payRow.setIsForPayment(false);
                payRow.setRecordPeriod("");
                payRow.setRecordBkc("");
                payRow.setRecordCob("");
                payRow.setRecordNumber("");
                payRow.setRecordDate(null);
            }
            else {
                row.setIsForPayment(false);
            }
            SLibUtils.showException(this, e);
        }
    }

    private void processEditingStoppedBalance() {
        int index = 0;
        mdBalanceTot = 0;
        SLayoutBankRow row = null;

        index = moGridPayments.getTable().getSelectedRow();
        row = (SLayoutBankRow) moGridPayments.getGridRow(index);
        
        if (row.getBalanceTot() > 0) {
            row.setIsForPayment(true);
        }
        else {
            row.setIsForPayment(false);
        }
        computeBalancePayment();
        moGridPayments.renderGridRows();
        //moGridAbpSettings.setSelectedGridRow(index);
        moGridPayments.getTable().setRowSelectionInterval(index, index);
    }

    private void updateLayoutRow() {
        SLayoutBankRow row = null;
        
        for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
            row = (SLayoutBankRow) rowAux;
            
            for (int i = 0; i < mvLayoutRows.size(); i++) {
                if (SLibUtilities.compareKeys(row.getRowPrimaryKey(), mvLayoutRows.get(i).getRowPrimaryKey())) {
                     mvLayoutRows.get(i).setIsForPayment(row.getIsForPayment());
                     mvLayoutRows.get(i).setAccountCredit(row.getAccountCredit());
                     mvLayoutRows.get(i).setBalanceTot(row.getBalanceTot());
                     mvLayoutRows.get(i).setBalanceTotByBizPartner(row.getBalanceTot());
                     mvLayoutRows.get(i).setBankKey(SLibUtilities.parseInt((row.getAccountCredit().length() > 0 ? row.getAccountCredit().substring(0, 3) : "000")));
                     mvLayoutRows.get(i).setEmail(row.getEmail());
                     mvLayoutRows.get(i).setSantanderBankCode(mvLayoutRows.get(i).getCodeBankAccountCredit().get(row.getAccountCredit()));
                     mvLayoutRows.get(i).setBajioBankCode(mvLayoutRows.get(i).getCodeBankAccountCredit().get(row.getAccountCredit()));
                     mvLayoutRows.get(i).setBajioBankAlias(mvLayoutRows.get(i).getAliasBankAccountCredit().get(row.getAccountCredit()));
                }
            }
        }
    }

    private void loadLayoutRow() {
        updateLayoutRow();
        moGridPayments.clearGridRows();
        
        if (mvLayoutRows != null) {
            mltAccountCredit.clear();
            
            if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_DPS) {
                for (int i = 0; i < mvLayoutRows.size(); i++) {
                    if (!jckDateMaturityRo.isSelected()) {
                        if (!jckAccountAll.isSelected()) {
                            mltAccountCredit.add(mvLayoutRows.get(i).getAccountCreditArray());
                            moGridPayments.addGridRow(mvLayoutRows.get(i));
                        }
                        else {
                            if (mvLayoutRows.get(i).getAccountCreditArray().size() > 0) {
                                mltAccountCredit.add(mvLayoutRows.get(i).getAccountCreditArray());
                                moGridPayments.addGridRow(mvLayoutRows.get(i));
                            }
                        }
                    }
                    else if (moDateDateDue.getValue().equals(mvLayoutRows.get(i).getDateMaturityRo())) {
                        if (!jckAccountAll.isSelected()) {
                            mltAccountCredit.add(mvLayoutRows.get(i).getAccountCreditArray());
                            moGridPayments.addGridRow(mvLayoutRows.get(i));
                        }
                        else {
                            if (mvLayoutRows.get(i).getAccountCreditArray().size() > 0) {
                                mltAccountCredit.add(mvLayoutRows.get(i).getAccountCreditArray());
                                moGridPayments.addGridRow(mvLayoutRows.get(i));
                            }
                        }
                    }

                }
            }
            else if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_ADV) {
                for (int i = 0; i < mvLayoutRows.size(); i++) {
                    if (!jckAccountAll.isSelected()) {
                        mltAccountCredit.add(mvLayoutRows.get(i).getAccountCreditArray());
                        moGridPayments.addGridRow(mvLayoutRows.get(i));
                    }
                    else {
                        if (mvLayoutRows.get(i).getAccountCreditArray().size() > 0) {
                            mltAccountCredit.add(mvLayoutRows.get(i).getAccountCreditArray());
                            moGridPayments.addGridRow(mvLayoutRows.get(i));
                        }
                    }
                }
            }
        }
        computeBalancePayment();
        moTableCellEditorOptions.setAccounts(mltAccountCredit);
        moGridPayments.renderGridRows();
        moGridPayments.setSelectedGridRow(0);
    }
    
    public void actionDateMaturityRo() {
        loadLayoutRow();
    }

    public void actionAccountAll() {
        loadLayoutRow();
    }

    @SuppressWarnings("unchecked")
    public void showDps(final boolean isNeedValidateRows) {
        String sql = "";
        ResultSet resulSet = null;
        Vector<SGridRow> rows = new Vector<SGridRow>();
        mvLayoutRows = new Vector<SLayoutBankRow>();
        mltAccountCredit = new ArrayList<>();
        Cursor cursor = getCursor();

        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            sql = createSqlQuery();

            resulSet = miClient.getSession().getStatement().executeQuery(sql);
            while (resulSet.next()) {
                moRow = new SLayoutBankRow(miClient);

                moRow.setLayoutRowType(SModSysConsts.FIN_LAY_BANK_DPS);
                moRow.setPkYearId(resulSet.getInt(4));
                moRow.setPkDocId(resulSet.getInt(5));
                moRow.setBizPartnerId(resulSet.getInt(1));
                moRow.setBizPartner(resulSet.getString(2));
                moRow.setBizPartnerKey(resulSet.getString(16));
                moRow.setBizPartnerCreditFiscalId(resulSet.getString(3));
                moRow.setBizPartnerBranch(resulSet.getString(18));
                moRow.setTypeDps(resulSet.getString(17));
                moRow.setNumberSer(resulSet.getString(7));
                moRow.setDate(resulSet.getDate(6));
                moRow.setBizPartnerBranchCob(resulSet.getString(19));
                moRow.setSubTotal(resulSet.getDouble(8));
                moRow.setTaxCharged(resulSet.getDouble(9));
                moRow.setTaxRetained(resulSet.getDouble(10));
                moRow.setTotal(resulSet.getDouble(11));
                moRow.setBalance(resulSet.getDouble(13));
                moRow.setBalanceTot(0);
                moRow.setBalanceTotByBizPartner(0);
                moRow.setCurrencyKey(resulSet.getString(20));
                moRow.setTotalVat(resulSet.getDouble(21));
                moRow.setDateMaturityRo(resulSet.getDate(22));
                moRow.setCurrencyId(mnCurrencyId);
                renderBankAccountCredit(resulSet.getInt(15), mnLayoutBank);
                renderBizPartner(miClient.getSession().getConfigCompany().getCompanyId());
                moRow.setAccountCredit(msAccountCredit);
                moRow.setEmail(resulSet.getString(14));

                moRow.setCf(0);
                moRow.setApply(1);
                moRow.setAccountDebit(msAccountDebit);
                moRow.setBizPartnerDebitFiscalId(msDebitFiscalId);
                moRow.setIsForPayment(false);
                moRow.setIsToPayed(false);
                moRow.setReference(resulSet.getString(7));
                moRow.setAccType("CLA");
                moRow.setConcept(moTextConcept.getValue());
                moRow.setDescription(moTextConcept.getValue());
                moRow.setAccountCreditArray(maAccountCredit);
                moRow.setBranchBankAccountCreditArray(maBranchBankAccountsCredit);

                if (moDateDateDue.getValue().equals(resulSet.getDate(22)) && maBranchBankAccountsCredit.size() > 0) {
                    rows.add(moRow);
                    mltAccountCredit.add(maAccountCredit);
                }
                mvLayoutRows.add(moRow);
            }
            moGridPayments.populateGrid(rows);
            moGridPayments.createGridColumns();
            
            moGridPayments.getTable().setColumnSelectionAllowed(false);
            moGridPayments.getTable().getTableHeader().setReorderingAllowed(false);
            moGridPayments.getTable().getTableHeader().setResizingAllowed(true);
            moGridPayments.getTable().setRowSorter(new TableRowSorter<AbstractTableModel>(moGridPayments.getModel()));
            moGridPayments.getTable().getTableHeader().setEnabled(false);
            
            if (moGridPayments.getTable().getRowCount() > 0) {
                moGridPayments.renderGridRows();
                moGridPayments.setSelectedGridRow(0);
                moTableCellEditorOptions.setAccounts(mltAccountCredit);
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
    
    public void showSupplier(final boolean isNeedValidateRows) {
        String sql = "";
        ResultSet resulSet = null;
        Vector<SGridRow> rows = new Vector<SGridRow>();
        mvLayoutRows = new Vector<SLayoutBankRow>();
        mltAccountCredit = new ArrayList<>();
        Cursor cursor = getCursor();

        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            sql = createSqlQuery();

            resulSet = miClient.getSession().getStatement().executeQuery(sql);
            while (resulSet.next()) {
                moRow = new SLayoutBankRow(miClient);

                moRow.setLayoutRowType(SModSysConsts.FIN_LAY_BANK_ADV);
                //moRow.setPkYearId(resulSet.getInt(4));
                //moRow.setPkDocId(resulSet.getInt(5));
                moRow.setBizPartnerId(resulSet.getInt(1));
                moRow.setBizPartner(resulSet.getString(2));
                moRow.setBizPartnerKey(resulSet.getString(6));
                moRow.setBizPartnerCreditFiscalId(resulSet.getString(4));
                /*
                moRow.setBizPartnerBranch(resulSet.getString(18));
                moRow.setTypeDps(resulSet.getString(17));
                moRow.setNumberSer(resulSet.getString(7));
                moRow.setDate(resulSet.getDate(6));
                moRow.setBizPartnerBranchCob(resulSet.getString(19));
                moRow.setSubTotal(resulSet.getDouble(8));
                moRow.setTaxCharged(resulSet.getDouble(9));
                moRow.setTaxRetained(resulSet.getDouble(10));
                moRow.setTotal(resulSet.getDouble(11));
                moRow.setBalance(resulSet.getDouble(13));
                */
                moRow.setBalanceTot(0);
                moRow.setBalanceTotByBizPartner(0);
                moRow.setCurrencyKey(resulSet.getString(5));
                /*
                moRow.setTotalVat(resulSet.getDouble(21));
                moRow.setDateMaturityRo(resulSet.getDate(22));
                */
                moRow.setCurrencyId(mnCurrencyId);
                renderBankAccountCredit(resulSet.getInt(7), mnLayoutBank);
                renderBizPartner(miClient.getSession().getConfigCompany().getCompanyId());
                moRow.setAccountCredit(msAccountCredit);
                moRow.setEmail(resulSet.getString(3));

                moRow.setCf(0);
                moRow.setApply(1);
                moRow.setAccountDebit(msAccountDebit);
                moRow.setBizPartnerDebitFiscalId(msDebitFiscalId);
                moRow.setIsForPayment(false);
                moRow.setIsToPayed(false);
                //moRow.setReference(resulSet.getString(7));
                moRow.setAccType("CLA");
                moRow.setConcept(moTextConcept.getValue());
                moRow.setDescription(moTextConcept.getValue());
                moRow.setAccountCreditArray(maAccountCredit);
                moRow.setBranchBankAccountCreditArray(maBranchBankAccountsCredit);

                if (maBranchBankAccountsCredit.size() > 0) {
                    rows.add(moRow);
                    mltAccountCredit.add(maAccountCredit);
                }
                
                mvLayoutRows.add(moRow);
            }
            moGridPayments.populateGrid(rows);
            moGridPayments.createGridColumns();
            
            moGridPayments.getTable().setColumnSelectionAllowed(false);
            moGridPayments.getTable().getTableHeader().setReorderingAllowed(false);
            moGridPayments.getTable().getTableHeader().setResizingAllowed(true);
            moGridPayments.getTable().setRowSorter(new TableRowSorter<AbstractTableModel>(moGridPayments.getModel()));
            moGridPayments.getTable().getTableHeader().setEnabled(false);
            
            if (moGridPayments.getTable().getRowCount() > 0) {
                moGridPayments.renderGridRows();
                moGridPayments.setSelectedGridRow(0);
                moTableCellEditorOptions.setAccounts(mltAccountCredit);
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

    private String createSqlQuery() {
        String sSql = "";
        String sCur = "";

        sCur = mnCurrencyId >= 2 ? "_cur" : "";

        if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_DPS) {
            sSql = "SELECT b.id_bp, b.bp, b.fiscal_id, d.id_year, d.id_doc, d.dt AS dt, " +  // 5
                     "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +  // 6
                     "d.stot" + sCur + "_r AS f_stot, d.tax_charged" + sCur + "_r AS f_tax, " +  // 8
                     "d.tax_retained" + sCur + "_r AS f_ret, d.tot" + sCur + "_r AS f_tot, c.id_cur AS f_id_cur, " +  // 11
                     "SUM(re.credit" + sCur + " - re.debit" + sCur + ") AS f_bal, bcon.email_01, d.fid_bpb, bct.bp_key, " +  // 15
                     "dt.code, bpb.bpb, cob.code, c.cur_key, " + // 19
                     "COALESCE((SELECT SUM(tax.tax" + sCur + ") FROM trn_dps_ety AS de " +
                     "INNER JOIN trn_dps_ety_tax AS tax ON de.id_year = tax.id_year AND de.id_doc = tax.id_doc AND de.id_ety = tax.id_ety AND tax.id_tax_bas = " + SDataConstantsSys.FINU_TAX_BAS_VAT + " " +
                     "WHERE d.id_year = de.id_year AND d.id_doc = de.id_doc AND de.b_del = 0), 0) AS f_iva, ADDDATE(d.dt_start_cred, d.days_cred) AS dt_mat " + // 21
                     "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                     "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                     "r.id_year = " + SLibTimeUtilities.digestYear(moDateDateDue.getValue())[0] + /*" AND r.dt <= '" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtilities.getEndOfYear(moDateDateDue.getValue())) + "'*/ " AND r.b_del = 0 AND re.b_del = 0 AND " +
                     "re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + " " +
                     "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " +
                     "INNER JOIN erp.bpsu_bp_ct AS bct ON re.fid_bp_nr = bct.id_bp AND bct.id_ct_bp = re.fid_tp_sys_mov_xxx " +
                     "INNER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc AND d.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
                     "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                     "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb " +
                     "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur AND c.id_cur = " + mnCurrencyId + " " +
                     "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
                     "LEFT OUTER JOIN erp.bpsu_bpb_con AS bcon ON bpb.id_bpb = bcon.id_bpb AND bcon.id_con = " + SDataConstantsSys.BPSS_TP_CON_ADM + " " +
                     "WHERE EXISTS(SELECT * FROM erp.bpsu_bank_acc AS ac WHERE bpb.id_bpb = ac.id_bpb " + (mnLayoutType == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? "AND ac.fid_bank = " + mnLayoutBank : "AND ac.fid_bank <> " + mnLayoutBank) + ") " +
                     "GROUP BY b.id_bp, b.bp, b.fiscal_id, d.id_year, d.id_doc, d.dt, d.stot_r, d.tax_charged_r, d.tax_retained_r, d.tot_r, c.id_cur, bcon.email_01, d.fid_bpb, bct.bp_key, dt.code, bpb.bpb, cob.code " +
                     "HAVING f_bal > 0 " +
                     "ORDER BY bp, id_bp, f_num, dt, id_year, id_doc; ";
        }
        else {
            sSql = "SELECT b.id_bp, b.bp, bcon.email_01, b.fiscal_id, " + // 4
                    "(SELECT cur_key FROM erp.cfgu_cur WHERE id_cur = (IF(bct.fid_cur_n IS NULL, (SELECT fid_cur FROM erp.cfg_param_erp), bct.fid_cur_n))) AS _cur, bct.bp_key, bpb.id_bpb " +
                    "FROM erp.bpsu_bp AS b " +
                    "INNER JOIN erp.bpsu_bp_ct AS bct ON  bct.id_bp = b.id_bp AND bct.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_SUP + " AND " +
                    (mnCurrencyId == SLibConsts.UNDEFINED ? "bct.fid_cur_n = " + mnCurrencyId + " " : "(bct.fid_cur_n IS NULL OR bct.fid_cur_n = " + mnCurrencyId + ") ") +
                    "INNER JOIN erp.bpsu_bpb AS bpb ON bpb.fid_bp = b.id_bp AND bpb.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " +
                    "LEFT OUTER JOIN erp.bpsu_bpb_con AS bcon ON bpb.id_bpb = bcon.id_bpb AND bcon.id_con = " + SDataConstantsSys.BPSS_TP_CON_ADM + " " +
                    "WHERE EXISTS(SELECT * FROM erp.bpsu_bank_acc AS ac WHERE bpb.id_bpb = ac.id_bpb " + (mnLayoutType == SDataConstantsSys.FINS_TP_PAY_BANK_THIRD ? "AND ac.fid_bank = " + mnLayoutBank : "AND ac.fid_bank <> " + mnLayoutBank) + ") " +
                    "AND b.b_del = 0 AND bct.b_del = 0 " +
                    "ORDER BY bp, id_bp; ";
        }

        return sSql;
    }

    @Override
    public void addAllListeners() {
        moKeyLayouId.addItemListener(this);
        moKeyBankLayoutType.addItemListener(this);
        moKeyAccountDebit.addItemListener(this);
        jbLayoutPath.addActionListener(this);
        jbShowDocs.addActionListener(this);
        jbCleanDocs.addActionListener(this);
        jbSelectAll.addActionListener(this);
        jbCleanAll.addActionListener(this);
        jckDateMaturityRo.addItemListener(this);
        jckAccountAll.addItemListener(this);
        jbPickRecord.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        moKeyLayouId.removeItemListener(this);
        moKeyBankLayoutType.removeItemListener(this);
        moKeyAccountDebit.removeItemListener(this);
        jbLayoutPath.removeActionListener(this);
        jbShowDocs.removeActionListener(this);
        jbCleanDocs.removeActionListener(this);
        jbSelectAll.removeActionListener(this);
        jbCleanAll.removeActionListener(this);
        jckDateMaturityRo.removeItemListener(this);
        jckAccountAll.removeItemListener(this);
        jbPickRecord.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyAccountDebit, SModConsts.FIN_ACC_CASH, SModConsts.FINX_ACC_CASH_BANK, new SGuiParams(miClient.getSession().getSessionCustom().getLocalCurrencyKey()));
        populateLayoutBank();
        renderLayoutTypeBank();
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbBankLayout) registry;
        
        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;
        jtfRows.setText("0/0");
        jckDateMaturityRo.setSelected(true);
        jckAccountAll.setSelected(true);
        mnNumberTransfer = 0;
        mnNumberDocs = 0;

        moCurrentRecord = null;
        maXmlRows = new ArrayList<SLayoutBankXmlRow>();
        maBankRecords = new ArrayList<SLayoutBankRecord>();
        maLocks = new ArrayList<SSrvLock>();
        renderRecord();

        removeAllListeners();
        reloadCatalogues();
        
        if (moRegistry.isRegistryNew()) {
            moRegistry.setDateLayout(miClient.getSession().getCurrentDate());
            moRegistry.setDateDue(miClient.getSession().getCurrentDate());
            mnCurrencyId = miClient.getSession().getSessionCustom().getLocalCurrencyKey()[0];
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }
        
        moDateDate.setValue(moRegistry.getDateLayout());
        moDateDateDue.setValue(moRegistry.getDateDue());
        moTextConcept.setValue(moRegistry.getConcept());
        moIntConsecutiveDay.setValue(moRegistry.getConsecutive());
        moKeyBankLayoutType.setValue(new int[] { moRegistry.getFkBankLayoutTypeId() });
        moKeyLayouId.setValue((int[]) moKeyBankLayoutType.getSelectedItem().getComplement());
        populateLayoutType();
        moKeyBankLayoutType.setValue(new int[] { moRegistry.getFkBankLayoutTypeId() });
        moKeyAccountDebit.setValue(new int[] { moRegistry.getFkBankCompanyBranchId(), moRegistry.getFkBankAccountCashId() });
        moTextLayoutPath.setValue("");
        
        renderBankLayoutSettings();
        renderAccountSettings();
        if (mnFormSubtype == SModConsts.FIN_REC) {
            loadPaymentsXml();
            moDecBalanceTot.setValue(moRegistry.getAmount());
        }
        else {
            if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_DPS) {
                showDps(!moRegistry.isRegistryNew());
            }
            else {
                showSupplier(!moRegistry.isRegistryNew());
            }
            
            if (!moRegistry.getLayoutXml().isEmpty()) {
                updateDocsXml();
            }
        }
        computeBalancePayment();
        
        setFormEditable(true);
        jbPickRecord.setEnabled(false);
        enableFields(moRegistry.isRegistryNew());
        moDecBalanceTot.setEditable(false);
        moDecBalanceTotPay.setEditable(false);

        addAllListeners();
    }

    @Override
    public SDbBankLayout getRegistry() throws Exception {
        SDbBankLayout registry = moRegistry.clone();
        ArrayList<SLayoutBankRecord> aBankRecords = null;
        SLayoutBankPaymentRow payRow = null;

        if (mnFormSubtype != SModConsts.FIN_REC) {
            //registry.setPkLayBankId();
            registry.setDateLayout(moDateDate.getValue());
            registry.setDateDue(moDateDateDue.getValue());
            registry.setConcept(moTextConcept.getValue());
            registry.setConsecutive(moIntConsecutiveDay.getValue());
            registry.setAmount(mdBalanceTot);
            //registry.setAmountPayed(mdBalancePayed);
            //registry.setTransfersPayed();
            registry.setDocs(mnNumberDocs);
            //registry.setDocsPayed();
            //registry.setLayoutText(msLayoutText);
            //registry.setLayoutXml(msLayoutText);
            registry.setDeleted(false);
            registry.setFkBankLayoutTypeId(mnLayoutSubtype);
            registry.setFkBankCompanyBranchId(moDataAccountCash.getPkCompanyBranchId());
            registry.setFkBankAccountCashId(moDataAccountCash.getPkAccountCashId());
            /*
            registry.setFkUserInsertId();
            registry.setFkUserUpdateId();
            registry.setTsUserInsert();
            registry.setTsUserUpdate();
            */

            registry.setAuxTitle(moKeyBankLayoutType.getSelectedItem().getItem().toLowerCase());
            registry.setAuxLayoutPath(moTextLayoutPath.getValue());

            registry.setPostSaveTarget(registry);
            registry.setPostSaveMethod(registry.getClass().getMethod("writeLayout", SGuiClient.class));
            registry.setPostSaveMethodArgs(new Object[] { miClient });
        }
        registry.getXmlRows().addAll(maXmlRows);
         
        if (mnFormSubtype != SModConsts.FIN_REC) {
            registry.setTransactionType(mnFormSubtype == SModSysConsts.FIN_LAY_BANK_DPS ? SFinConsts.LAY_BANK_TYPE_DPS : SFinConsts.LAY_BANK_TYPE_ADV);
        }

        if (mnFormSubtype == SModConsts.FIN_REC) {
            for (SGridRow rowAux : moGridPayments.getModel().getGridRows()) {
                payRow = (SLayoutBankPaymentRow) rowAux;

                if (payRow.getFinRecordLayout() != null || payRow.getFinRecordLayoutOld() != null) {
                    registry.getBankPaymentRows().add(payRow);
                }
            }
            registry.setTransfers(moGridPayments.getModel().getGridRows().size());
        }
        
        if (!maLocks.isEmpty()) {
            registry.getLocks().addAll(maLocks);
        }
        
        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            if (mnLayoutBank != moDataBizPartnerBranchBankAccount.getFkBankId()) {
                validation.setMessage("El valor para el campo '" + SGuiUtils.getLabelName(jlAccountDebit) + "', debe pertenecer al banco '" +
                        SDataReadDescriptions.getCatalogueDescription((SClientInterface) miClient, SDataConstants.BPSU_BP, new int[] { mnLayoutBank }, SLibConstants.DESCRIPTION_CODE) + "'.");
                validation.setComponent(moKeyAccountDebit);
            }
            
            if (validation.isValid()) {
                if (moTextLayoutPath.getValue().length() == 0 && mnFormSubtype != SModConsts.FIN_REC) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlLayoutPath) + "'.");
                    validation.setComponent(jbLayoutPath);
                }
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
            if (mnFormSubtype == SModConsts.FIN_REC) {
                super.actionSave();
            }
            else {
                if (validateDebtsToPay()) {
                    //createLayoutText();
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
            
            if (button == jbLayoutPath) {
                actionLoadLayoutPath();
            }
            else if (button == jbShowDocs) {
                actionShowDocs();
            }
            else if (button == jbCleanDocs) {
                actionCleanDocs();
            }
            else if (button == jbSelectAll) {
                actionSelectAll();
            }
            else if (button == jbCleanAll) {
                actionCleanAll();
            }
            else if (button == jbPickRecord) {
                actionPickRecord();
            }
        }
    }
    
    @Override
    public void editingStopped(ChangeEvent e) {
        switch (moGridPayments.getTable().getSelectedColumn()) {
            case COL_APP:
                processEditingAppPayment();
                break;
            case COL_APP_PAY:
                if (mnFormSubtype == SModConsts.FIN_REC) {
                    processEditingAppPayment();
                }
                else if (mnFormSubtype == SModSysConsts.FIN_LAY_BANK_ADV) {
                    processEditingStoppedBalance(); // Is for advances for payment
                }
                break;
            case COL_BAL:
                processEditingStoppedBalance();
                break;
            default:
                break;
        }
    }

    @Override
    public void editingCanceled(ChangeEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox && e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox comboBox = (JComboBox)  e.getSource();

            if (comboBox == moKeyLayouId) {
                itemStateChangedLayout();
            }
            else if (comboBox == moKeyBankLayoutType) {
                itemStateChangedBankLayoutTypeId();
            }
            else if (comboBox == moKeyAccountDebit) {
                itemStateChangedAccountDebit();
            }
        }
        else if (e.getSource() instanceof javax.swing.JCheckBox) {
            JCheckBox checkBox = (JCheckBox) e.getSource();

            if (checkBox == jckDateMaturityRo) {
                actionDateMaturityRo();
            }
            else if (checkBox == jckAccountAll) {
                actionAccountAll();
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mhrs.form;

import erp.SClient;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePaneGrid;
import erp.lib.table.STableRow;
import erp.mbps.data.SDataBizPartner;
import erp.mcfg.data.SCfgUtils;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.data.SDataTax;
import erp.mfin.form.SDialogRecordPicker;
import erp.mhrs.data.SDataFormerPayroll;
import erp.mhrs.data.SDataFormerPayrollEmp;
import erp.mhrs.data.SDataFormerPayrollMove;
import erp.mhrs.data.SRowEmployee;
import erp.mitm.data.SDataItem;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SDbBizPartner;
import erp.mod.fin.db.SFinUtils;
import erp.mod.hrs.db.SDbAccountingPayroll;
import erp.mod.hrs.db.SDbAccountingPayrollReceipt;
import erp.mod.hrs.db.SDbCfgAccountingDepartment;
import erp.mod.hrs.db.SDbCfgAccountingEmployeeDeduction;
import erp.mod.hrs.db.SDbCfgAccountingEmployeeEarning;
import erp.mod.hrs.db.SDbEmployee;
import erp.mod.hrs.db.SDbPackCostCenters;
import erp.mod.hrs.db.SDbPackExpenses;
import erp.mod.hrs.db.SDbPackExpensesItem;
import erp.mod.hrs.db.SDbPayroll;
import erp.mod.hrs.db.SDbPayrollReceipt;
import erp.mod.hrs.db.SHrsConsts;
import erp.mod.hrs.db.SHrsFinUtils;
import erp.mod.utils.SDialogMessages;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibProrationUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author Sergio Flores, Juan Barajas, Sergio Flores
 */
public class SDialogPayrollAccounting extends JDialog implements ActionListener {
    
    private static final int CONCEPT_TYPE_EAR = 1;
    private static final int CONCEPT_TYPE_DED = 2;
    private static final int JOURNAL_VOUCHER_COLS = 5;
    private static final int JOURNAL_VOUCHER_COLS_INDEX = 8;
    
    private int mnFormResult;
    private boolean mbFirstTime;

    private erp.client.SClientInterface miClient;
    private erp.lib.table.STablePane moTablePaneEmpAvailable;
    private erp.lib.table.STablePane moTablePaneEmpSelected;
    private erp.mfin.form.SDialogRecordPicker moDialogRecordPicker;
    private erp.mfin.data.SDataRecord moCurrentRecord;
    private erp.mhrs.data.SDataFormerPayroll moFormerPayroll;
    private java.util.ArrayList<RecordEmployees> maRecordEmployeeses;
    private int mnParamPayrollAccProcess;
    private String msParamAccountingDynamicEmployeeMode;
    private int mnNewMoveId;
    private int mnLastEntryId;
    private int mnLastSortingPosition;
    private double mdRecordEarnings;
    private double mdRecordDeductions;
    private double mdTotalDebit;
    private double mdTotalCredit;
    private double mdTotalNetSelected;
    
    private SDbPayroll moPayroll;
    private SDbAccountingPayroll moAccountingPayroll;
    
    /** Hash Map: key = numeric ID of account; value = string ID of account. */
    private HashMap<Integer, String> moAccountStrIdsMap;
    /** Hash Map: key = numeric ID of cost center; value = string ID of cost center. */
    private HashMap<Integer, String> moCostCenterStrIdsMap;
    /** Hash Map: key = string ID of account; value = account. */
    private HashMap<String, SDataAccount> moAccountsMap;
    /** Hash Map: key = string ID of ledger account; value = ledger account. */
    private HashMap<String, SDataAccount> moLedgerAccountsMap;
    
    /** Hash Map: key = ID of department; value = ID of pack of cost centers. */
    private HashMap<Integer, Integer> moDepartmentsPackCostCentersMap;
    /** Hash Map: key = ID of employee; value = ID of pack of cost centers. */
    private HashMap<Integer, Integer> moEmployeesPackCostCentersMap;
    /** Hash Map: key = ID of pack of cost centers; value = pack of cost centers. */
    private HashMap<Integer, SDbPackCostCenters> moPackCostCentersMap;
    /** Hash Map: key = ID of pack of expenses; value = pack of expenses. */
    private HashMap<Integer, SDbPackExpenses> moPackExpensesMap;
    /** Hash Map: key = ID of department; value = department. */
    private HashMap<Integer, Department> moDepartmentsMap;
    /** Hash Map: key = ID of employee; value = employee. */
    private HashMap<Integer, Employee> moEmployeesMap;
    /** Hash Map: key = ID of employee; value = ID of department. */
    private HashMap<Integer, Integer> moEmployeesDepartmentsMap;
    /** Hash Map: key = ID of department; value = configuration of accounting. */
    private HashMap<Integer, SDbCfgAccountingDepartment> moDepartmentsCfgAccountingMap;
    /** Hash Map: key = numeric ID of account; value = flag for cost center required. */
    private HashMap<Integer, Boolean> moAccountsCostCenterRequiredMap;
    /** Hash Map: key = numeric ID of account; value = flag for business partner required. */
    private HashMap<Integer, Boolean> moAccountsBizPartnerRequiredMap;
    /** Hash Map: key = PK of record as string; value = last entry ID for record. */
    private HashMap<String, Integer> moRecordsLastEntryIdMap;
    /** Hash Map: key = PK of record as string; value = last sorting position for record. */
    private HashMap<String, Integer> moRecordsLastSortingPositionMap;
    
    /** Creates new form SDialogPayrollAccounting
     * @param client
     * @param payroll
     */
    public SDialogPayrollAccounting(SClientInterface client, SDbPayroll payroll) {
        super(client.getFrame(), true);
        miClient = client;
        moPayroll = payroll;
        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpGrid = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlPayroll = new javax.swing.JLabel();
        jtfPayrollPeriod = new javax.swing.JTextField();
        jtfPayrollNumber = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jlPayrollDates = new javax.swing.JLabel();
        jtfPayrollDates = new javax.swing.JTextField();
        jlPayrollNet = new javax.swing.JLabel();
        jtfPayrollNet = new javax.swing.JTextField();
        jtfPayrollNetCur = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jlPayrollNotes = new javax.swing.JLabel();
        jtfPayrollNotes = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jpJournalVoucher = new javax.swing.JPanel();
        jpAccountingRecord = new javax.swing.JPanel();
        jlRecord = new javax.swing.JLabel();
        jtfRecordDate = new javax.swing.JTextField();
        jtfRecordBkc = new javax.swing.JTextField();
        jtfRecordBranch = new javax.swing.JTextField();
        jtfRecordNumber = new javax.swing.JTextField();
        jbPickRecord = new javax.swing.JButton();
        jlDummy3 = new javax.swing.JLabel();
        jpPaymentType = new javax.swing.JPanel();
        jlBankFilter = new javax.swing.JLabel();
        jcbBankFilter = new javax.swing.JComboBox();
        jlBankFilterHint = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jckAccountingGradual = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        jpEmployeesAvailable = new javax.swing.JPanel();
        jlTotalAvailables = new javax.swing.JLabel();
        jpEmployeesSelected = new javax.swing.JPanel();
        jlTotalSelected = new javax.swing.JLabel();
        jpControls = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jlDummy01 = new javax.swing.JLabel();
        jbAdd = new javax.swing.JButton();
        jbAddAll = new javax.swing.JButton();
        jbRemove = new javax.swing.JButton();
        jbRemoveAll = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Contabilización de nóminas");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpGrid.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de la nómina:"));
        jPanel1.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayroll.setText("Nómina:");
        jlPayroll.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlPayroll);

        jtfPayrollPeriod.setEditable(false);
        jtfPayrollPeriod.setText("2001-01");
        jtfPayrollPeriod.setFocusable(false);
        jtfPayrollPeriod.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel3.add(jtfPayrollPeriod);

        jtfPayrollNumber.setEditable(false);
        jtfPayrollNumber.setText("QNA. 1");
        jtfPayrollNumber.setFocusable(false);
        jtfPayrollNumber.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel3.add(jtfPayrollNumber);

        jPanel1.add(jPanel3);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayrollDates.setText("Período nómina:");
        jlPayrollDates.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlPayrollDates);

        jtfPayrollDates.setEditable(false);
        jtfPayrollDates.setText("01/01/2001 - 01/01/2001");
        jtfPayrollDates.setFocusable(false);
        jtfPayrollDates.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel5.add(jtfPayrollDates);

        jlPayrollNet.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlPayrollNet.setText("Total neto:");
        jlPayrollNet.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlPayrollNet);

        jtfPayrollNet.setEditable(false);
        jtfPayrollNet.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPayrollNet.setText("9,999,999.99");
        jtfPayrollNet.setFocusable(false);
        jtfPayrollNet.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel5.add(jtfPayrollNet);

        jtfPayrollNetCur.setEditable(false);
        jtfPayrollNetCur.setText("MXN");
        jtfPayrollNetCur.setFocusable(false);
        jtfPayrollNetCur.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel5.add(jtfPayrollNetCur);

        jPanel1.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPayrollNotes.setText("Comentarios:");
        jlPayrollNotes.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlPayrollNotes);

        jtfPayrollNotes.setEditable(false);
        jtfPayrollNotes.setText("PAYROLL NOTES");
        jtfPayrollNotes.setFocusable(false);
        jtfPayrollNotes.setPreferredSize(new java.awt.Dimension(650, 23));
        jPanel6.add(jtfPayrollNotes);

        jPanel1.add(jPanel6);

        jpGrid.add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalles de la contabilización de la nómina:"));
        jPanel4.setLayout(new java.awt.BorderLayout(5, 5));

        jPanel8.setLayout(new java.awt.BorderLayout());

        jpJournalVoucher.setLayout(new java.awt.GridLayout(2, 0, 0, 5));

        jpAccountingRecord.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRecord.setText("Póliza contable:");
        jlRecord.setPreferredSize(new java.awt.Dimension(100, 23));
        jpAccountingRecord.add(jlRecord);

        jtfRecordDate.setEditable(false);
        jtfRecordDate.setText("01/01/2000");
        jtfRecordDate.setToolTipText("Fecha de la póliza contable");
        jtfRecordDate.setFocusable(false);
        jtfRecordDate.setPreferredSize(new java.awt.Dimension(65, 23));
        jpAccountingRecord.add(jtfRecordDate);

        jtfRecordBkc.setEditable(false);
        jtfRecordBkc.setText("BKC");
        jtfRecordBkc.setToolTipText("Centro contable");
        jtfRecordBkc.setFocusable(false);
        jtfRecordBkc.setPreferredSize(new java.awt.Dimension(35, 23));
        jpAccountingRecord.add(jtfRecordBkc);

        jtfRecordBranch.setEditable(false);
        jtfRecordBranch.setText("BRA");
        jtfRecordBranch.setToolTipText("Sucursal de la empresa");
        jtfRecordBranch.setFocusable(false);
        jtfRecordBranch.setPreferredSize(new java.awt.Dimension(35, 23));
        jpAccountingRecord.add(jtfRecordBranch);

        jtfRecordNumber.setEditable(false);
        jtfRecordNumber.setText("TP-000001");
        jtfRecordNumber.setToolTipText("Número de póliza contable");
        jtfRecordNumber.setFocusable(false);
        jtfRecordNumber.setPreferredSize(new java.awt.Dimension(65, 23));
        jpAccountingRecord.add(jtfRecordNumber);

        jbPickRecord.setText("...");
        jbPickRecord.setToolTipText("Seleccionar póliza contable");
        jbPickRecord.setPreferredSize(new java.awt.Dimension(23, 23));
        jpAccountingRecord.add(jbPickRecord);

        jlDummy3.setPreferredSize(new java.awt.Dimension(122, 23));
        jpAccountingRecord.add(jlDummy3);

        jpJournalVoucher.add(jpAccountingRecord);

        jpPaymentType.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBankFilter.setText("Filtrar banco:");
        jlBankFilter.setPreferredSize(new java.awt.Dimension(100, 23));
        jpPaymentType.add(jlBankFilter);

        jcbBankFilter.setPreferredSize(new java.awt.Dimension(300, 23));
        jpPaymentType.add(jcbBankFilter);

        jlBankFilterHint.setForeground(java.awt.SystemColor.textInactiveText);
        jlBankFilterHint.setText("(El filtro sólo aplica al usar la opción agregar todos los recibos, '>>')");
        jlBankFilterHint.setPreferredSize(new java.awt.Dimension(375, 23));
        jpPaymentType.add(jlBankFilterHint);

        jpJournalVoucher.add(jpPaymentType);

        jPanel8.add(jpJournalVoucher, java.awt.BorderLayout.CENTER);

        jPanel9.setLayout(new java.awt.GridLayout(2, 1));

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckAccountingGradual.setText("Contabilizar gradualmente");
        jckAccountingGradual.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jckAccountingGradual.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jckAccountingGradual.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel10.add(jckAccountingGradual);

        jPanel9.add(jPanel10);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel9.add(jPanel11);

        jPanel8.add(jPanel9, java.awt.BorderLayout.EAST);

        jPanel4.add(jPanel8, java.awt.BorderLayout.NORTH);

        jpEmployeesAvailable.setBorder(javax.swing.BorderFactory.createTitledBorder("Recibos pendientes de contabilizar:"));
        jpEmployeesAvailable.setPreferredSize(new java.awt.Dimension(450, 100));
        jpEmployeesAvailable.setLayout(new java.awt.BorderLayout());

        jlTotalAvailables.setText("Recibos pendientes...");
        jlTotalAvailables.setPreferredSize(new java.awt.Dimension(100, 20));
        jpEmployeesAvailable.add(jlTotalAvailables, java.awt.BorderLayout.SOUTH);

        jPanel4.add(jpEmployeesAvailable, java.awt.BorderLayout.LINE_START);

        jpEmployeesSelected.setBorder(javax.swing.BorderFactory.createTitledBorder("Recibos seleccionados para contabilizar:"));
        jpEmployeesSelected.setPreferredSize(new java.awt.Dimension(475, 100));
        jpEmployeesSelected.setLayout(new java.awt.BorderLayout());

        jlTotalSelected.setText("Recibos seleccionados...");
        jlTotalSelected.setPreferredSize(new java.awt.Dimension(100, 20));
        jpEmployeesSelected.add(jlTotalSelected, java.awt.BorderLayout.SOUTH);

        jPanel4.add(jpEmployeesSelected, java.awt.BorderLayout.LINE_END);

        jpControls.setLayout(new java.awt.BorderLayout());

        jPanel7.setLayout(new java.awt.GridLayout(5, 1, 0, 5));
        jPanel7.add(jlDummy01);

        jbAdd.setText(">");
        jbAdd.setToolTipText("Agregar");
        jbAdd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel7.add(jbAdd);

        jbAddAll.setText(">>");
        jbAddAll.setToolTipText("Agregar todos");
        jbAddAll.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel7.add(jbAddAll);

        jbRemove.setText("<");
        jbRemove.setToolTipText("Remover");
        jbRemove.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel7.add(jbRemove);

        jbRemoveAll.setText("<<");
        jbRemoveAll.setToolTipText("Remover todos");
        jbRemoveAll.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel7.add(jbRemoveAll);

        jpControls.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jPanel4.add(jpControls, java.awt.BorderLayout.CENTER);

        jpGrid.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpGrid, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jbOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOkActionPerformed(evt);
            }
        });
        jPanel2.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelActionPerformed(evt);
            }
        });
        jPanel2.add(jbCancel);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(1056, 689));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jbOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOkActionPerformed
        actionOk();
    }//GEN-LAST:event_jbOkActionPerformed

    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        actionCancel();
    }//GEN-LAST:event_jbCancelActionPerformed

    private void initComponentsExtra() {
        int i = 0;
        STableColumnForm[] aoTableColumns = null;

        moTablePaneEmpAvailable = new STablePaneGrid(miClient);
        moTablePaneEmpAvailable.setDoubleClickAction(this, "actionAdd");
        jpEmployeesAvailable.add(moTablePaneEmpAvailable, BorderLayout.CENTER);

        moTablePaneEmpSelected = new STablePaneGrid(miClient);
        moTablePaneEmpSelected.setDoubleClickAction(this, "actionRemove");
        jpEmployeesSelected.add(moTablePaneEmpSelected, BorderLayout.CENTER);

        moDialogRecordPicker = new SDialogRecordPicker(miClient, SDataConstants.FINX_REC_USER);
        moFormerPayroll = null;
        moCurrentRecord = null;

        i = 0;
        aoTableColumns = new STableColumnForm[8];
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Nombre empleado", 150);
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Número empleado", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[i++].setCellRenderer(SGridUtils.CellRendererIntegerRaw);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Departamento", 100);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Código departamento", 50);
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Percepciones $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Deducciones $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Total neto $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Banco", 100);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePaneEmpAvailable.addTableColumn(aoTableColumns[i]);
        }

        i = 0;
        aoTableColumns = new STableColumnForm[13];
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Nombre empleado", 150);
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Número empleado", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[i++].setCellRenderer(SGridUtils.CellRendererIntegerRaw);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Departamento", 100);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Código departamento", 50);
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Percepciones $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Deducciones $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Total neto $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Banco", 100);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Período póliza", STableConstants.WIDTH_YEAR_PERIOD);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Centro contable", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE, "Fecha póliza", STableConstants.WIDTH_DATE);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePaneEmpSelected.addTableColumn(aoTableColumns[i]);
        }

        jbPickRecord.addActionListener(this);
        jbAdd.addActionListener(this);
        jbAddAll.addActionListener(this);
        jbRemove.addActionListener(this);
        jbRemoveAll.addActionListener(this);

        SFormUtilities.createActionMap(rootPane, this, "actionOk", "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionCancel", "cancel", KeyEvent.VK_ESCAPE, 0);
        
        try {
            mnParamPayrollAccProcess = SLibUtils.parseInt(SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_HRS_PAYROLL_ACC_PROCESS));
            msParamAccountingDynamicEmployeeMode = SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_HRS_PAYROLL_ACC_DYN_EMP_MODE);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;

            if (miClient.getSessionXXX().getCurrentCompanyBranchId() == 0) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_SESSION_BRANCH);
                actionCancel();
            }
            else {
                try {
                    showPayroll();
                    jbPickRecord.requestFocus();
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void computeTotals() {
        double amountAvailable = 0;
        double amountSelected = 0;
        
        for (int row = 0; row < moTablePaneEmpAvailable.getTableGuiRowCount(); row++) {
            amountAvailable = SLibUtils.roundAmount(amountAvailable + ((SRowEmployee) moTablePaneEmpAvailable.getTableRow(row)).getPayment());
        }
        
        for (int row = 0; row < moTablePaneEmpSelected.getTableGuiRowCount(); row++) {
            amountSelected = SLibUtils.roundAmount(amountSelected + ((SRowEmployee) moTablePaneEmpSelected.getTableRow(row)).getPayment());
        }
        
        jlTotalAvailables.setText("Recibos pendientes: " + SLibUtils.DecimalFormatInteger.format(moTablePaneEmpAvailable.getTableGuiRowCount()) + " | "
                + "Monto pendiente: $" + SLibUtils.getDecimalFormatAmount().format(amountAvailable) + " " + miClient.getSession().getSessionCustom().getLocalCurrencyCode());
        
        jlTotalSelected.setText("Recibos seleccionados:  " + SLibUtils.DecimalFormatInteger.format(moTablePaneEmpSelected.getTableGuiRowCount()) + " | "
                + "Monto seleccionado: $" + SLibUtils.getDecimalFormatAmount().format(amountSelected) + " " + miClient.getSession().getSessionCustom().getLocalCurrencyCode());
    }
    
    @SuppressWarnings("unchecked")
    private void showPayroll() {
        // Display payroll:

        jtfPayrollPeriod.setText(moPayroll.composePayrollPeriod());
        jtfPayrollNumber.setText(moPayroll.composePayrollNumber());
        jtfPayrollDates.setText(moPayroll.composePayrollDates());
        jtfPayrollNotes.setText(moPayroll.getNotes());
        jtfPayrollNet.setText(SLibUtils.getDecimalFormatAmount().format(moPayroll.getAuxTotalNet()));

        jtfPayrollPeriod.setCaretPosition(0);
        jtfPayrollNumber.setCaretPosition(0);
        jtfPayrollDates.setCaretPosition(0);
        jtfPayrollNotes.setCaretPosition(0);
        jtfPayrollNet.setCaretPosition(0);
        
        jckAccountingGradual.setEnabled(!moPayroll.isAccounting()); // once payroll is bookkept this setting cannot be changed
        jckAccountingGradual.setSelected(moPayroll.isAccountingGradual());

        try {
            // Prepare payroll registry:

            moFormerPayroll = new SDataFormerPayroll();
            moFormerPayroll.setPkPayrollId(moPayroll.getPkPayrollId());
            moFormerPayroll.setYear(moPayroll.getPeriodYear());
            moFormerPayroll.setPeriod(moPayroll.getPeriod());
            moFormerPayroll.setNumber(moPayroll.getNumber());
            moFormerPayroll.setType(moPayroll.getPaymentType());
            moFormerPayroll.setNote(SLibUtils.textLeft(moPayroll.getNotes(), 100));
            moFormerPayroll.setDateBegin(moPayroll.getDateStart());
            moFormerPayroll.setDateEnd(moPayroll.getDateEnd());
            moFormerPayroll.setDatePayment(moPayroll.getDateEnd());
            moFormerPayroll.setDebit_r(0);
            moFormerPayroll.setCredit_r(0);
            moFormerPayroll.setIsRegular(moPayroll.isPayrollNormal());
            moFormerPayroll.setIsClosed(moPayroll.isClosed());
            moFormerPayroll.setIsDeleted(false);
            moFormerPayroll.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
            moFormerPayroll.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
            moFormerPayroll.setFkUserDeleteId(miClient.getSession().getUser().getPkUserId());

            // Prepare bank filter:

            HashSet<String> banksSet = new HashSet<>();
            boolean isEmptyBankAdded = false;

            // Show pending payroll receipts:

            String sql = "SELECT bp.bp, emp.id_emp, CAST(emp.num AS UNSIGNED INTEGER) AS _emp_num, dep.name, dep.code, dep.id_dep, "
                    + "p.fk_tp_pay, pr.ear_r, pr.ded_r, pr.pay_r, pr.pay_day_r, pr.day_wrk, pr.day_not_wrk_r, pr.day_pad, "
                    + "tpsal.name, tpsal.code, tpemp.name, tpemp.code, tpwrk.name, tpwrk.code, COALESCE(bnk.name, '') AS _bank "
                    + "FROM hrs_pay AS p "
                    + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                    + "INNER JOIN erp.hrsu_emp AS emp ON emp.id_emp = pr.id_emp "
                    + "INNER JOIN erp.hrsu_dep AS dep ON dep.id_dep = pr.fk_dep "
                    + "INNER JOIN erp.bpsu_bp AS bp ON bp.id_bp = pr.id_emp "
                    + "INNER JOIN erp.hrss_tp_sal AS tpsal ON tpsal.id_tp_sal = pr.fk_tp_sal "
                    + "INNER JOIN erp.hrsu_tp_emp AS tpemp ON tpemp.id_tp_emp = pr.fk_tp_emp "
                    + "INNER JOIN erp.hrsu_tp_wrk AS tpwrk ON tpwrk.id_tp_wrk = pr.fk_tp_wrk "
                    + "LEFT OUTER JOIN erp.hrss_bank AS bnk ON emp.fk_bank_n = bnk.id_bank "
                    + "WHERE p.id_pay = " + moPayroll.getPkPayrollId() + " AND NOT pr.b_del AND "
                    + "pr.id_emp NOT IN (" // exclude payroll receipts already bookkept
                        + "SELECT apr.id_emp "
                        + "FROM hrs_acc_pay AS ap "
                        + "INNER JOIN hrs_acc_pay_rcp AS apr ON apr.id_pay = ap.id_pay AND apr.id_acc = ap.id_acc "
                        + "WHERE ap.id_pay = " + moPayroll.getPkPayrollId() + " AND NOT ap.b_del ORDER BY apr.id_emp) "
                    + "ORDER BY bp.bp, emp.id_emp ";

            try (Statement statement = miClient.getSession().getStatement()) {
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SRowEmployee row = new SRowEmployee();

                    row.setPrimaryKey(new int[] { resultSet.getInt("emp.id_emp") });
                    row.getValues().add(resultSet.getString("bp.bp")); // 0
                    row.getValues().add(resultSet.getInt("_emp_num")); // 1
                    row.getValues().add(resultSet.getString("dep.name")); // 2
                    row.getValues().add(resultSet.getString("dep.code")); // 3
                    row.getValues().add(resultSet.getDouble("pr.ear_r")); // 4
                    row.getValues().add(resultSet.getDouble("pr.ded_r")); // 5
                    row.getValues().add(resultSet.getDouble("pr.pay_r")); // 6
                    row.getValues().add(resultSet.getString("_bank")); // 7

                    row.setEmployeeCategory(resultSet.getString("tpwrk.code"));
                    row.setEmployeeType(resultSet.getString("tpemp.code"));
                    row.setSalaryType(SLibUtils.textLeft(resultSet.getString("tpsal.name"), 3)); // system's catalog, name can be truncated to length of 3
                    row.setBank(resultSet.getString("_bank"));
                    row.setSalary(resultSet.getDouble("pr.pay_day_r"));
                    row.setPayment(resultSet.getDouble("pr.pay_r"));
                    row.setDaysWorked(resultSet.getInt("pr.day_wrk"));
                    row.setDaysNotWorked(resultSet.getInt("pr.day_not_wrk_r"));
                    row.setDaysPayed(resultSet.getInt("pr.day_pad"));
                    row.setFkBizPartnerId(resultSet.getInt("emp.id_emp"));
                    row.setFkPaymentSystemTypeId(0); // attribute is obsolete!

                    moTablePaneEmpAvailable.addTableRow(row);

                    // Process bank filter:

                    if (row.getBank().isEmpty()) {
                        if (!isEmptyBankAdded) {
                            isEmptyBankAdded = true;
                            banksSet.add(SHrsConsts.EMPTY_BANK);
                        }
                    }
                    else {
                        banksSet.add(row.getBank());
                    }
                }

                moTablePaneEmpAvailable.renderTableRows();
                moTablePaneEmpAvailable.setTableRowSelection(0);

                // Set bank filter:

                Object[] banksArray = banksSet.toArray();
                Arrays.sort(banksArray);

                jcbBankFilter.removeAllItems();
                jcbBankFilter.addItem("- " + SUtilConsts.TXT_SELECT + " " + SGuiUtils.getLabelName(jlBankFilter) + " -");

                for (Object bank : banksArray) {
                    jcbBankFilter.addItem(bank);
                }
            }
            
            computeTotals();
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    @SuppressWarnings("unchecked")
    private void prepareRecordEmployeesForAccounting() throws Exception {
        mnNewMoveId = 0;
        mnLastEntryId = 0;
        mnLastSortingPosition = 0;
        mdTotalDebit = 0;
        mdTotalCredit = 0;
        mdTotalNetSelected = 0;
        
        moAccountStrIdsMap = new HashMap<>();
        moCostCenterStrIdsMap = new HashMap<>();
        moAccountsMap = new HashMap<>();
        moLedgerAccountsMap = new HashMap<>();
        
        moDepartmentsPackCostCentersMap = new HashMap<>();
        moEmployeesPackCostCentersMap = new HashMap<>();
        moPackCostCentersMap = new HashMap<>();
        moPackExpensesMap = new HashMap<>();
        moDepartmentsMap = new HashMap<>();
        moEmployeesMap = new HashMap<>();
        moEmployeesDepartmentsMap = new HashMap<>();
        moDepartmentsCfgAccountingMap = new HashMap<>();
        moAccountsCostCenterRequiredMap = new HashMap<>();
        moAccountsBizPartnerRequiredMap = new HashMap<>();
        moRecordsLastEntryIdMap = new HashMap<>();
        moRecordsLastSortingPositionMap = new HashMap<>();
        
        moFormerPayroll.getDbmsDataFormerPayrollEmps().clear();
        moFormerPayroll.getDbmsDataFormerPayrollMoves().clear();
        moFormerPayroll.getAuxDataRecords().clear();
        
        moAccountingPayroll = null;
        
        if (jckAccountingGradual.isSelected()) {
            moAccountingPayroll = SHrsFinUtils.getLastAccountingPayroll(miClient.getSession(), moPayroll.getPkPayrollId());
            
            if (moAccountingPayroll != null) {
                moAccountingPayroll.getChildReceipts().clear(); // preserve from being altered all payroll receipts already accounted!
            }
        }
        
        if (moAccountingPayroll == null) {
            moAccountingPayroll = new SDbAccountingPayroll();
            moAccountingPayroll.setPkPayrollId(moPayroll.getPkPayrollId());
            moAccountingPayroll.setAuxAccountingGradual(jckAccountingGradual.isSelected());
        }
        
        maRecordEmployeeses = new ArrayList<>();
        
        // iterate through all selected employees to be accounted:

        for (int i = 0; i < moTablePaneEmpSelected.getTableGuiRowCount(); i++) {
            SRowEmployee rowEmployee = (SRowEmployee) moTablePaneEmpSelected.getTableRow(i);
            Object[] employeeRecordKey = (Object[]) rowEmployee.getData(); // XXX record primary key is in Data member, WTF!
            boolean addReccord = true;
            ArrayList<Integer> recordEmployeeIds = null;

            for (RecordEmployees recordEmployees : maRecordEmployeeses) {
                if (SLibUtilities.compareKeys(recordEmployees.Record.getPrimaryKey(), employeeRecordKey)) {
                    addReccord = false;
                    recordEmployeeIds = recordEmployees.EmployeeIds;
                    break;
                }
            }
            
            if (addReccord) {
                SDataRecord record = (SDataRecord) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_REC, employeeRecordKey, SLibConstants.EXEC_MODE_VERBOSE);
                
                if (!jckAccountingGradual.isSelected()) {
                    for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
                        if (entry.getFkPayrollId_n() == moPayroll.getPkPayrollId() && !entry.getIsDeleted()) {
                            entry.setIsDeleted(true);
                            entry.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
                            entry.setFkUserDeleteId(miClient.getSession().getUser().getPkUserId());
                        }
                    }
                }

                recordEmployeeIds = new ArrayList<>();
                maRecordEmployeeses.add(new RecordEmployees(record, recordEmployeeIds));
            }
            
            recordEmployeeIds.add(((int[]) rowEmployee.getPrimaryKey())[0]);

            SDataFormerPayrollEmp formerPayrollEmp = new SDataFormerPayrollEmp();
            formerPayrollEmp.setPkPayrollId(moPayroll.getPkPayrollId());
            formerPayrollEmp.setPkEmployeeId(((int[]) rowEmployee.getPrimaryKey())[0]);
            formerPayrollEmp.setEmployee((String) rowEmployee.getValues().get(0));
            formerPayrollEmp.setDepartment((String) rowEmployee.getValues().get(2));
            formerPayrollEmp.setDepartmentKey((String) rowEmployee.getValues().get(3));
            formerPayrollEmp.setEmployeeCategory(rowEmployee.getEmployeeCategory());
            formerPayrollEmp.setEmployeeType(rowEmployee.getEmployeeType());
            formerPayrollEmp.setSalaryType(rowEmployee.getSalaryType());
            formerPayrollEmp.setDebit((Double) rowEmployee.getValues().get(4));
            formerPayrollEmp.setCredit((Double) rowEmployee.getValues().get(5));
            
            mdTotalNetSelected = SLibUtils.roundAmount(mdTotalNetSelected + (Double) rowEmployee.getValues().get(6));
            
            formerPayrollEmp.setSalary(rowEmployee.getSalary());
            formerPayrollEmp.setDaysNotWorked(rowEmployee.getDaysNotWorked());
            formerPayrollEmp.setDaysWorked(rowEmployee.getDaysWorked());
            formerPayrollEmp.setDaysPayed(rowEmployee.getDaysPayed());
            formerPayrollEmp.setNumberSeries(SHrsConsts.CFD_SERIES);
            formerPayrollEmp.setNumber(0);
            formerPayrollEmp.setIsDeleted(false);
            formerPayrollEmp.setFkBizPartnerId_n(rowEmployee.getFkBizPartnerId());
            formerPayrollEmp.setFkPaymentSystemTypeId(SDataConstantsSys.TRNU_TP_PAY_SYS_NA);
            formerPayrollEmp.setFkYearId((Integer) employeeRecordKey[0]);
            formerPayrollEmp.setFkPeriodId((Integer) employeeRecordKey[1]);
            formerPayrollEmp.setFkBookkeepingCenterId((Integer) employeeRecordKey[2]);
            formerPayrollEmp.setFkRecordTypeId((String) employeeRecordKey[3]);
            formerPayrollEmp.setFkNumberId((Integer) employeeRecordKey[4]);

            moFormerPayroll.getDbmsDataFormerPayrollEmps().add(formerPayrollEmp);
            
            moAccountingPayroll.getChildReceipts().add(rowEmployee.getAccountingPayrollReceipt());
        }
    }

    private String getAccountStrId(final int accountId) {
        String accountStrId = moAccountStrIdsMap.get(accountId);
        
        if (accountStrId == null) {
            accountStrId = SFinUtils.getAccountFormerIdXXX(miClient.getSession(), accountId);
            moAccountStrIdsMap.put(accountId, accountStrId);
        }
        
        return accountStrId;
    }
    
    private String getCostCenterStrId(final int costCenterId) {
        String costCenterStrId = moCostCenterStrIdsMap.get(costCenterId);
        
        if (costCenterStrId == null) {
            costCenterStrId = SFinUtils.getCostCenterFormerIdXXX(miClient.getSession(), costCenterId);
            moCostCenterStrIdsMap.put(costCenterId, costCenterStrId);
        }
        
        return costCenterStrId;
    }
    
    private SDataAccount getAccount(final String accountStrId) {
        SDataAccount account = moAccountsMap.get(accountStrId);
        
        if (account == null) {
            account = (SDataAccount) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC, new Object[] { accountStrId }, SLibConstants.EXEC_MODE_VERBOSE);
            moAccountsMap.put(accountStrId, account);
        }
        
        return account;
    }
    
    private SDataAccount getLedgerAccount(final SDataAccount account) {
        SDataAccount ledgerAccount = account.getDeep() == 1 ? account : moLedgerAccountsMap.get(account.getDbmsPkLedgerAccountIdXXX());
        
        if (ledgerAccount == null) {
            ledgerAccount = (SDataAccount) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC, new Object[] { account.getDbmsPkLedgerAccountIdXXX() }, SLibConstants.EXEC_MODE_VERBOSE);
            moLedgerAccountsMap.put(account.getDbmsPkLedgerAccountIdXXX(), ledgerAccount);
        }
        
        return ledgerAccount;
    }
            
    private int getPackCostCentersForDepartment(final int departmentId) throws Exception {
        Integer packCostCentersId = moDepartmentsPackCostCentersMap.get(departmentId);

        if (packCostCentersId == null) {
            packCostCentersId = SHrsFinUtils.getSuitablePackCostCentersIdForDepartment(miClient.getSession(), departmentId, moPayroll.getDateEnd());
            moDepartmentsPackCostCentersMap.put(departmentId, packCostCentersId);
        }
        
        return packCostCentersId;
    }
    
    private int getPackCostCentersForEmployee(final int employeeId) throws Exception {
        Integer packCostCentersId = moEmployeesPackCostCentersMap.get(employeeId);

        if (packCostCentersId == null) {
            packCostCentersId = SHrsFinUtils.getSuitablePackCostCentersIdForEmployee(miClient.getSession(), employeeId, moPayroll.getDateEnd());
            moEmployeesPackCostCentersMap.put(employeeId, packCostCentersId);
        }
        
        return packCostCentersId;
    }
    
    private SDbPackCostCenters getPackCostCenters(final int packCostCentersId) throws Exception {
        SDbPackCostCenters packCostCenters = moPackCostCentersMap.get(packCostCentersId);
        
        if (packCostCenters == null) {
            packCostCenters = (SDbPackCostCenters) miClient.getSession().readRegistry(SModConsts.HRS_PACK_CC, new int[] { packCostCentersId });
            moPackCostCentersMap.put(packCostCentersId, packCostCenters);
        }
        
        return packCostCenters;
    }
    
    private SDbPackExpenses getPackExpenses(final int packExpensesId) throws Exception {
        SDbPackExpenses packExpenses = moPackExpensesMap.get(packExpensesId);
        
        if (packExpenses == null) {
            packExpenses = (SDbPackExpenses) miClient.getSession().readRegistry(SModConsts.HRSU_PACK_EXP, new int[] { packExpensesId });
            moPackExpensesMap.put(packExpensesId, packExpenses);
        }
        
        return packExpenses;
    }
    
    private Department getDepartment(final int departmentId) throws Exception {
        Department department = null;
        
        if (departmentId != 0) {
            department = moDepartmentsMap.get(departmentId);

            if (department == null) {
                department = new Department(departmentId, 
                        (String) miClient.getSession().readField(SModConsts.HRSU_DEP, new int[] { departmentId }, SDbRegistry.FIELD_NAME), 
                        (String) miClient.getSession().readField(SModConsts.HRSU_DEP, new int[] { departmentId }, SDbRegistry.FIELD_CODE));
                moDepartmentsMap.put(departmentId, department);
            }
        }
        
        return department;
    }
    
    private Employee getEmployee(final int employeeId) throws Exception {
        Employee employee = null;
        
        if (employeeId != 0) {
            employee = moEmployeesMap.get(employeeId);

            if (employee == null) {
                employee = new Employee(employeeId, 
                        (String) miClient.getSession().readField(SModConsts.HRSU_EMP, new int[] { employeeId }, SDbRegistry.FIELD_NAME), 
                        (String) miClient.getSession().readField(SModConsts.HRSU_EMP, new int[] { employeeId }, SDbRegistry.FIELD_CODE), 
                        (int) miClient.getSession().readField(SModConsts.HRSU_EMP, new int[] { employeeId }, SDbEmployee.FIELD_BRANCH_HQ));
                moEmployeesMap.put(employeeId, employee);
            }
        }
        
        return employee;
    }
    
    private int getEmployeeDepartmentId(final int employeeId) throws Exception {
        Integer departmentId = 0;
        
        if (employeeId != 0) {
            departmentId = moEmployeesDepartmentsMap.get(employeeId);

            if (departmentId == null) {
                departmentId = moPayroll.getChildPayrollReceipt(employeeId).getFkDepartmentId();
                moEmployeesDepartmentsMap.put(employeeId, departmentId);
            }
        }
        
        return departmentId;
    }
    
    private SDbCfgAccountingDepartment getCfgAccountingForDepartment(final int departmentId) throws Exception {
        SDbCfgAccountingDepartment cfgAccounting = null;
        
        if (departmentId != 0) {
            cfgAccounting = moDepartmentsCfgAccountingMap.get(departmentId);

            if (cfgAccounting == null) {
                cfgAccounting = (SDbCfgAccountingDepartment) miClient.getSession().readRegistry(SModConsts.HRS_CFG_ACC_DEP, new int[] { departmentId });
                if (!cfgAccounting.isRegistryNew()) {
                    moDepartmentsCfgAccountingMap.put(departmentId, cfgAccounting);
                }
                else {
                    cfgAccounting = null;
                }
            }
        }
        
        return cfgAccounting;
    }
    
    private boolean isCostCenterRequiredForAccount(final int accountId) throws Exception {
        Boolean isRequired = false;
        
        if (accountId != 0) {
            isRequired = moAccountsCostCenterRequiredMap.get(accountId);

            if (isRequired == null) {
                isRequired = SHrsFinUtils.checkIfAccountRequiresCostCenter(miClient.getSession(), accountId);
                moAccountsCostCenterRequiredMap.put(accountId, isRequired);
            }
        }
        
        return isRequired;
    }
    
    private boolean isBizPartnerRequiredForAccount(final int accountId) throws Exception {
        Boolean isRequired = false;
        
        if (accountId != 0) {
            isRequired = moAccountsBizPartnerRequiredMap.get(accountId);

            if (isRequired == null) {
                isRequired = SHrsFinUtils.checkIfAccountRequiresBizPartner(miClient.getSession(), accountId);
                moAccountsBizPartnerRequiredMap.put(accountId, isRequired);
            }
        }
        
        return isRequired;
    }
    
    private int getRecordLastEntryId(final SDataRecord record) throws Exception {
        Integer lastEntryId = 0;
        
        if (record != null) {
            lastEntryId = moRecordsLastEntryIdMap.get(record.getRecordPrimaryKey());

            if (lastEntryId == null) {
                lastEntryId = record.getDbmsRecordEntries().size();
                moRecordsLastEntryIdMap.put(record.getRecordPrimaryKey(), lastEntryId);
            }
        }
        
        return lastEntryId;
    }
    
    private int getRecordLastSortingPosition(final SDataRecord record) throws Exception {
        Integer lastSortingPosition = 0;
        
        if (record != null) {
            lastSortingPosition = moRecordsLastSortingPositionMap.get(record.getRecordPrimaryKey());

            if (lastSortingPosition == null) {
                lastSortingPosition = record.getLastSortingPosition();
                moRecordsLastSortingPositionMap.put(record.getRecordPrimaryKey(), lastSortingPosition);
            }
        }
        
        return lastSortingPosition;
    }
    
    private void preserveRecordLastEntryId(final SDataRecord record) {
        moRecordsLastEntryIdMap.put(record.getRecordPrimaryKey(), mnLastEntryId);
    }
    
    private void preserveRecordLastSortingPosition(final SDataRecord record) {
        moRecordsLastSortingPositionMap.put(record.getRecordPrimaryKey(), mnLastSortingPosition);
    }
    
    /**
     * Get total of earning for required parameters.
     * @param conceptType Concept type: earning or deduction. (Constants declared in this class: CONCEPT_TYPE_...)
     * @param conceptId ID of earning or deduction.
     * @param statement DBMS statement.
     * @param sqlEmployeeIds SQL list of required employee IDs to be processed.
     * @param sqlEmployeeIdsToExclude SQL list of required employee IDs to be excluded.
     * @param departmentId ID of deparment. Can be omitted with zero value.
     * @param employeeId ID of employee. Can be omitted with zero value.
     * @return
     * @throws Exception 
     */
    private double getConceptAmount(final int conceptType, final int conceptId, final Statement statement, final String sqlEmployeeIds, final String sqlEmployeeIdsToExclude, final int departmentId, final int employeeId) throws Exception {
        double total = 0;
        String sql = "";
        
        switch (conceptType) {
            case CONCEPT_TYPE_EAR:
                sql = "SELECT COALESCE(SUM(pre.amt_r), 0.0) AS _amt_r "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                        + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del "
                        + "AND p.id_pay = " + moPayroll.getPkPayrollId() + " AND pr.id_emp IN (" + sqlEmployeeIds + ") AND pre.fk_ear = " + conceptId + " "
                        + (departmentId == 0 ? "" : "AND pr.fk_dep = " + departmentId + " ")
                        + (employeeId == 0 ? "" : "AND pr.id_emp = " + employeeId + " ")
                        + (sqlEmployeeIdsToExclude.isEmpty() ? "" : "AND pr.id_emp NOT IN (" + sqlEmployeeIdsToExclude + ")");
                break;
                
            case CONCEPT_TYPE_DED:
                sql = "SELECT COALESCE(SUM(prd.amt_r), 0.0) AS _amt_r "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp "
                        + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT prd.b_del "
                        + "AND p.id_pay = " + moPayroll.getPkPayrollId() + " AND pr.id_emp IN (" + sqlEmployeeIds + ") AND prd.fk_ded = " + conceptId + " "
                        + (departmentId == 0 ? "" : "AND pr.fk_dep = " + departmentId + " ")
                        + (employeeId == 0 ? "" : "AND pr.id_emp = " + employeeId + " ")
                        + (sqlEmployeeIdsToExclude.isEmpty() ? "" : "AND pr.id_emp NOT IN (" + sqlEmployeeIdsToExclude + ")");
                break;
                
            default:
                // nothing
        }
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                total = resultSet.getDouble(1);
            }
        }
        
        return total;
    }
    
    /**
     * Get a new list of IDs from intersection of the elements to check into the list of elements.
     * @param elementsToCheck
     * @param listOfElements
     * @return 
     */
    private ArrayList<Integer> getIntersection(final ArrayList<Integer> elementsToCheck, final ArrayList<Integer> listOfElements) {
        ArrayList<Integer> intersection = new ArrayList<>();
        
        for (int element : elementsToCheck) {
            if (listOfElements.contains(element)) {
                intersection.add(element);
            }
        }
        
        return intersection;
    }
    
    /**
     * Get for given employees the sum of their earnings and deductions in current payroll.
     * @param employeeIds
     * @return Sum of earnings and deductions as a <code>double[]</code>: index 0 = sum of earnings; index 1 = sum of deductions.
     */
    private double[] getEmployeesAmounts(final ArrayList<Integer> employeeIds) {
        double ear = 0;
        double ded = 0;
        
        for (Integer employeeId : employeeIds) {
            SDbPayrollReceipt receipt = moPayroll.getChildPayrollReceipt(employeeId);
            ear = SLibUtils.roundAmount(ear + receipt.getEarnings_r());
            ded = SLibUtils.roundAmount(ded + receipt.getDeductions_r());
        }
        
        return new double[] { ear, ded };
    }

    /**
     * Create record entry.
     * Increments current record's last entry ID.
     * @param recordPk
     * @param concept
     * @param debit
     * @param credit
     * @param accountId
     * @param costCenterId
     * @param itemId
     * @param bizPartnerId
     * @param bizPartnerBranchId
     * @param taxKey
     * @param sysAccountTypeKey
     * @param sysMoveTypeKey
     * @param sysMoveTypeKeyXXX
     * @return 
     */
    private SDataRecordEntry createRecordEntry(final Object recordPk, final String concept, final double debit, final double credit, final String accountId, final String costCenterId,
            final int itemId, final int bizPartnerId, final int bizPartnerBranchId, final int[] taxKey, final int[] sysAccountTypeKey, final int[] sysMoveTypeKey, final int[] sysMoveTypeKeyXXX) {
        SDataRecordEntry entry = new SDataRecordEntry();

        entry.setPkYearId((Integer) ((Object[]) recordPk)[0]);
        entry.setPkPeriodId((Integer) ((Object[]) recordPk)[1]);
        entry.setPkBookkeepingCenterId((Integer) ((Object[]) recordPk)[2]);
        entry.setPkRecordTypeId((String) ((Object[]) recordPk)[3]);
        entry.setPkNumberId((Integer) ((Object[]) recordPk)[4]);
        entry.setPkEntryId(0);
        entry.setConcept(concept);
        entry.setReference("");
        entry.setIsReferenceTax(false);
        entry.setDebit(debit);
        entry.setCredit(credit);
        entry.setExchangeRate(1);
        entry.setExchangeRateSystem(1);
        entry.setDebitCy(debit);
        entry.setCreditCy(credit);
        entry.setUnits(0);
        entry.setSortingPosition(++mnLastSortingPosition);
        entry.setOccasionalFiscalId("");
        entry.setIsExchangeDifference(false);
        entry.setIsSystem(true);
        entry.setIsDeleted(false);
        entry.setFkAccountIdXXX(accountId);
        entry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        entry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        entry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
        entry.setFkSystemMoveClassId(sysMoveTypeKey[0]);
        entry.setFkSystemMoveTypeId(sysMoveTypeKey[1]);
        entry.setFkSystemAccountClassId(sysAccountTypeKey[0]);
        entry.setFkSystemAccountTypeId(sysAccountTypeKey[1]);
        entry.setFkSystemMoveCategoryIdXXX(sysMoveTypeKeyXXX[0]);
        entry.setFkSystemMoveTypeIdXXX(sysMoveTypeKeyXXX[1]);
        entry.setFkCurrencyId(miClient.getSessionXXX().getParamsErp().getFkCurrencyId());
        entry.setFkCostCenterIdXXX_n(costCenterId);
        entry.setFkCheckWalletId_n(0);
        entry.setFkCheckId_n(0);
        entry.setFkBizPartnerId_nr(bizPartnerId);
        entry.setFkBizPartnerBranchId_n(bizPartnerBranchId);
        entry.setFkReferenceCategoryId_n(0);
        entry.setFkCompanyBranchId_n(0);
        entry.setFkEntityId_n(0);
        entry.setFkTaxBasicId_n(taxKey[0]);
        entry.setFkTaxId_n(taxKey[1]);
        entry.setFkYearId_n(0);
        entry.setFkDpsYearId_n(0);
        entry.setFkDpsDocId_n(0);
        entry.setFkDpsAdjustmentYearId_n(0);
        entry.setFkDpsAdjustmentDocId_n(0);
        entry.setFkDiogYearId_n(0);
        entry.setFkDiogDocId_n(0);
        entry.setFkPayrollFormerId_n(0);
        entry.setFkPayrollId_n(moPayroll.getPkPayrollId());
        entry.setFkItemId_n(itemId);
        entry.setFkItemAuxId_n(0);
        entry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        entry.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        
        mnLastEntryId++;

        return entry;
    }

    /**
     * Compute record entry.
     * NOTE: Invoke this method after computeFormerPayrollMove().
     * @param conceptAccountingType
     * @param record
     * @param accountId
     * @param costCenterId
     * @param itemId
     * @param bizPartnerId
     * @param bizPartnerBranchId
     * @param taxKey
     * @param conceptType
     * @param conceptName
     * @param referenceName
     * @param referenceCode
     * @param amount
     * @return 
     */
    private SDataRecordEntry computeRecordEntry(final int conceptAccountingType, final SDataRecord record, final int accountId, 
            final int costCenterId, final int itemId, final int bizPartnerId, final int bizPartnerBranchId, final int[] taxKey, 
            final int conceptType, final String conceptName, final String referenceName, final String referenceCode, final double amount) {
        double debit = 0;
        double credit = 0;
        String entryConcept = "";

        switch (conceptAccountingType) {
            case SModSysConsts.HRSS_TP_ACC_GBL: // global link
                entryConcept = moPayroll.composePayrollNumber() + "; " + conceptName;
                break;
            case SModSysConsts.HRSS_TP_ACC_DEP: // link by department
            case SModSysConsts.HRSS_TP_ACC_EMP: // link by employee
                entryConcept = moPayroll.composePayrollNumber() + "; " + conceptName;
                if (!referenceCode.isEmpty() && !referenceName.isEmpty()) {
                    entryConcept += "; " + referenceCode + ". " + referenceName;
                }
                break;
            default:
                // nothing
        }
        
        entryConcept = SLibUtilities.textLeft(entryConcept, SDataRecordEntry.LEN_CONCEPT);

        switch (conceptType) {
            case CONCEPT_TYPE_EAR:
                if (amount >= 0d) {
                    debit = amount;
                    credit = 0;
                }
                else {
                    debit = 0;
                    credit = -amount;
                }
                
                mdRecordEarnings = SLibUtils.roundAmount(mdRecordEarnings + amount);
                break;
                
            case CONCEPT_TYPE_DED:
                if (amount >= 0d) {
                    debit = 0;
                    credit = amount;
                }
                else {
                    debit = -amount;
                    credit = 0;
                }
                
                mdRecordDeductions = SLibUtils.roundAmount(mdRecordDeductions + amount);
                break;
                
            default:
                // nothing
        }


        mdTotalDebit = SLibUtils.roundAmount(mdTotalDebit + debit);
        mdTotalCredit = SLibUtils.roundAmount(mdTotalCredit + credit);

        // Set up account:

        String accountStrId = "";
        String costCenterStrId = "";
        int[] anSysAccountTypeKey = null;
        int[] anSysMoveTypeKey = null;
        int[] anSysMoveTypeKeyXXX = null;

        if (accountId != 0) {
            accountStrId = getAccountStrId(accountId);

            SDataAccount account = getAccount(accountStrId);
            SDataAccount ledgerAccount = getLedgerAccount(account);

            switch (ledgerAccount.getFkAccountSystemTypeId()) {
                case SDataConstantsSys.FINS_TP_ACC_SYS_SUP:
                    anSysAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL;
                    anSysMoveTypeKey = debit >= 0 ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_DEC_ADJ : SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_ADJ;
                    anSysMoveTypeKeyXXX = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP;
                    break;
                case SDataConstantsSys.FINS_TP_ACC_SYS_CUS:
                    anSysAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL;
                    anSysMoveTypeKey = debit >= 0 ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_ADJ : SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_DEC_ADJ;
                    anSysMoveTypeKeyXXX = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS;
                    break;
                case SDataConstantsSys.FINS_TP_ACC_SYS_CDR:
                    anSysAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL;
                    anSysMoveTypeKey = debit >= 0 ? SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_DEC_ADJ : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_ADJ;
                    anSysMoveTypeKeyXXX = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CDR;
                    break;
                case SDataConstantsSys.FINS_TP_ACC_SYS_DBR:
                    anSysAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL;
                    anSysMoveTypeKey = debit >= 0 ? SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_ADJ : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_DEC_ADJ;
                    anSysMoveTypeKeyXXX = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_DBR;
                    break;
                default:
                    anSysAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_NA_NA;
                    anSysMoveTypeKey = debit >= 0 ? SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT : SModSysConsts.FINS_TP_SYS_MOV_JOU_CDT;
                    anSysMoveTypeKeyXXX = SDataConstantsSys.FINS_TP_SYS_MOV_NA;
            }
        }
        else {
            anSysAccountTypeKey = SModSysConsts.FINS_TP_SYS_ACC_NA_NA;
            anSysMoveTypeKey = debit >= 0 ? SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT : SModSysConsts.FINS_TP_SYS_MOV_JOU_CDT;
            anSysMoveTypeKeyXXX = SDataConstantsSys.FINS_TP_SYS_MOV_NA;
        }

        // Set up cost center:

        if (costCenterId != 0) {
            costCenterStrId = getCostCenterStrId(costCenterId);
        }

        // Create record entry:

        SDataRecordEntry entry = createRecordEntry(record.getPrimaryKey(), entryConcept, debit, credit, accountStrId, costCenterStrId, 
                itemId, bizPartnerId, bizPartnerBranchId, taxKey, anSysAccountTypeKey, anSysMoveTypeKey, anSysMoveTypeKeyXXX);
        
        return entry;
    }

    /**
     * Compute former payroll movement.
     * Increments new move ID and adds one to current record's last entry ID.
     * NOTE: Invoke this method before computeRecordEntry().
     * @param conceptType
     * @param conceptId
     * @param concept
     * @param referenceId
     * @param referenceName
     * @param referenceCode
     * @param amount
     * @param record
     * @return 
     */
    private SDataFormerPayrollMove computeFormerPayrollMove(final int conceptType, final int conceptId, final String concept, 
            final int referenceId, final String referenceName, final String referenceCode, final double amount, final SDataRecord record) {
        SDataFormerPayrollMove move = new SDataFormerPayrollMove();
        
        move.setPkPayrollId(moPayroll.getPkPayrollId());
        move.setPkMoveId(++mnNewMoveId);
        move.setType(conceptType);
        move.setTransactionId(conceptId);
        move.setTransaction(concept);
        move.setReferenceId(referenceId);
        move.setReference(referenceName);
        move.setReferenceKey(referenceCode);
        move.setAmount(amount);
        move.setFkYearId(record.getPkYearId());
        move.setFkPeriodId(record.getPkPeriodId());
        move.setFkBookkeepingCenterId(record.getPkBookkeepingCenterId());
        move.setFkRecordTypeId(record.getPkRecordTypeId());
        move.setFkNumberId(record.getPkNumberId());
        move.setFkEntryId(mnLastEntryId + 1);
        
        return move;
    }

    /**
     * Compute concept for accounting dynamic.
     * @param conceptAccountingType Concept accounting types: global, department or employee. (Constants declared in class <code>SModSysConsts</code>: HRSS_TP_ACC_...)
     * @param conceptType Concept type: earning or deduction. (Constants declared in this class: CONCEPT_TYPE_...)
     * @param conceptDepartmentId Concept's department ID. Can be zero.
     * @param conceptEmployeeId Concept's employee ID. Can be zero.
     * @param statement DBMS statement.
     * @param statementAux DBMS auxiliar statement.
     * @param sql SQL select statement.
     * @param sqlEmployeeIds SQL list of required employee IDs to be processed.
     * @param employeeIdsWithPackCostCenters Array of employee IDs with pack of cost centers.
     * @param record Journal voucher.
     * @throws Exception 
     */
    private void computeConceptAccountingDynamic(final int conceptAccountingType, final int conceptType, final Statement statement, final Statement statementAux, 
            final String sql, final String sqlEmployeeIds, final ArrayList<Integer> employeeIdsWithPackCostCenters, final SDataRecord record) throws Exception {
        int conceptId = 0; // ID of current earning or deduction
        String conceptName = ""; // name of current earning or deduction
        String conceptNameAbbr = ""; // abbreviated name of current earning or deduction
        String sqlEmployeeIdsWithPackCostCenters = StringUtils.join(employeeIdsWithPackCostCenters, ",");
        
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            // process each earning or deduction:

            // retrieve earning or deduction info only once when needed:
            
            if (conceptId != resultSet.getInt("_cpt_id")) {
                conceptId = resultSet.getInt("_cpt_id"); // ID
                conceptName = resultSet.getString("_cpt_name"); // name
                conceptNameAbbr = resultSet.getString("_cpt_name_abbr"); // abbreviated name
            }
            
            // set effective department, employee, pack of cost centers and item as needed:
            
            int effDepartmentId = 0;
            int effEmployeeId = 0;
            int effPackCostCentersId = 0;
            int effItemId = 0;
            int[] effTaxKey = new int[] { resultSet.getInt("_tax_bas_id"), resultSet.getInt("_tax_tax_id") };
            
            switch (conceptAccountingType) {
                case SModSysConsts.HRSS_TP_ACC_GBL:
                    effDepartmentId = 0;
                    effEmployeeId = 0;
                    effPackCostCentersId = resultSet.getInt("_pack_cc_id"); // earning's or deductions's own pack
                    break;
                    
                case SModSysConsts.HRSS_TP_ACC_DEP:
                    effDepartmentId = resultSet.getInt("_dep_id");
                    effEmployeeId = 0;
                    effPackCostCentersId = getPackCostCentersForDepartment(effDepartmentId);
                    break;
                    
                case SModSysConsts.HRSS_TP_ACC_EMP:
                    effDepartmentId = resultSet.getInt("_dep_id");
                    effEmployeeId = resultSet.getInt("_emp_id");
                    effPackCostCentersId = getPackCostCentersForEmployee(effEmployeeId);
                    if (effPackCostCentersId == 0) {
                        effPackCostCentersId = getPackCostCentersForDepartment(effDepartmentId);
                    }
                    break;
                    
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
            
            // process accounting:

            int accountId = resultSet.getInt("_acc_id"); // bookkeeping account
            int effBizPartnerId = 0;
            int effBizPartnerBranchId = 0;
            boolean isCustomEffBizPartner = false;
            
            if (isBizPartnerRequiredForAccount(accountId)) {
                switch (conceptAccountingType) {
                    case SModSysConsts.HRSS_TP_ACC_GBL:
                    case SModSysConsts.HRSS_TP_ACC_DEP:
                        effBizPartnerId = resultSet.getInt("_bp_id");
                        effBizPartnerBranchId = resultSet.getInt("_bpb_id");
                        break;

                    case SModSysConsts.HRSS_TP_ACC_EMP:
                        // check if there is a custom setting for employee, to set business partner:
                        
                        switch (conceptType) {
                            case CONCEPT_TYPE_EAR:
                                SDbCfgAccountingEmployeeEarning caea = SHrsFinUtils.retrieveCfgAccountingEmployeeEarning(miClient.getSession(), effEmployeeId, conceptId);
                                if (caea != null) {
                                    SDbBizPartner bp = (SDbBizPartner) miClient.getSession().readRegistry(SModConsts.BPSU_BP, new int[] { caea.getFkBizPartnerId() });
                                    effBizPartnerId = bp.getPkBizPartnerId();
                                    effBizPartnerBranchId = bp.getRegHeadquarters().getPkBizPartnerBranchId();
                                    isCustomEffBizPartner = true;
                                }
                                break;
                                
                            case CONCEPT_TYPE_DED:
                                SDbCfgAccountingEmployeeDeduction caed = SHrsFinUtils.retrieveCfgAccountingEmployeeDeduction(miClient.getSession(), effEmployeeId, conceptId);
                                if (caed != null) {
                                    SDbBizPartner bp = (SDbBizPartner) miClient.getSession().readRegistry(SModConsts.BPSU_BP, new int[] { caed.getFkBizPartnerId() });
                                    effBizPartnerId = bp.getPkBizPartnerId();
                                    effBizPartnerBranchId = bp.getRegHeadquarters().getPkBizPartnerBranchId();
                                    isCustomEffBizPartner = true;
                                }
                                break;
                                
                            default:
                                // nothing
                        }
                        
                        // if yet neccesary, set business partner:
                        
                        if (!isCustomEffBizPartner) {
                            SDbCfgAccountingDepartment cfgAccountingDepartment = getCfgAccountingForDepartment(effDepartmentId);
                            if (cfgAccountingDepartment.getFkBizPartnerId_n() != 0) {
                                // configuration of department has been set with a business partner:
                                effBizPartnerId = resultSet.getInt("_bp_id");
                                effBizPartnerBranchId = resultSet.getInt("_bpb_id");
                            }
                            else {
                                // configuration of department does not have a business partner, by default use employee instead:
                                effBizPartnerId = resultSet.getInt("_emp_id");
                                effBizPartnerBranchId = resultSet.getInt("_empb_id");
                            }
                        }
                        break;

                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
            }
            
            ArrayList<PayrollAmount> payrollAmounts = new ArrayList<>();
            
            if (!isCostCenterRequiredForAccount(accountId)) {
                // account does not required cost center:

                double amount = getConceptAmount(conceptType, conceptId, statementAux, sqlEmployeeIds, "", effDepartmentId, effEmployeeId);
                if (amount != 0) {
                    payrollAmounts.add(new PayrollAmount(effDepartmentId, effEmployeeId, SModSysConsts.HRS_PACK_CC_NA, amount));
                }
            }
            else {
                // account requires cost center:

                // get total amount for employees who do not require a pack of cost centers:

                double amount = getConceptAmount(conceptType, conceptId, statementAux, sqlEmployeeIds, sqlEmployeeIdsWithPackCostCenters, effDepartmentId, effEmployeeId);
                if (amount != 0) {
                    payrollAmounts.add(new PayrollAmount(effDepartmentId, effEmployeeId, effPackCostCentersId, amount));
                }

                // get amount for each employee who has a pack of cost centers, only if it's needed:

                for (int employeeId : employeeIdsWithPackCostCenters) {
                    boolean proceedWithEmployeeAmount = 
                            conceptAccountingType == SModSysConsts.HRSS_TP_ACC_GBL || // in global type there is only one iteration per each earning or deduction, so proceed
                            conceptAccountingType == SModSysConsts.HRSS_TP_ACC_EMP || // in employee type there is only one iteration per employee, so proceed 
                            (conceptAccountingType == SModSysConsts.HRSS_TP_ACC_DEP && effDepartmentId == getEmployeeDepartmentId(employeeId)); // in department type proceed only if current iteration is of employee's department
                    
                    if (proceedWithEmployeeAmount) {
                        amount = getConceptAmount(conceptType, conceptId, statementAux, sqlEmployeeIds, "", effDepartmentId, employeeId);
                        if (amount != 0) {
                            payrollAmounts.add(new PayrollAmount(0, employeeId, getPackCostCentersForEmployee(employeeId), amount));
                        }
                    }
                }
                
                // effective item:
                
                int packExpensesId = resultSet.getInt("_pack_exp_id");
                
                if (packExpensesId != SModSysConsts.HRSU_PACK_EXP_NA) {
                    SDbPackExpenses packExpenses = getPackExpenses(packExpensesId);
                    SDbPackExpensesItem packExpensesItem = packExpenses.getChildItem(resultSet.getInt("_tp_exp_id"));
                    if (packExpensesItem != null) {
                        effItemId = packExpensesItem.getFkItemId();
                    }
                    else {
                        throw new Exception("No existe el tipo de gasto '" + (String) miClient.getSession().readField(SModConsts.HRSU_TP_EXP, new int[] { resultSet.getInt("_tp_exp_id") }, SDbRegistry.FIELD_NAME)+ "' "
                                + "para el paquete de gastos '" + packExpenses.getName() + "'.");
                    }
                }
            }
            
            // create payroll moves and journal voucher entries:
            
            double total = 0;
            double amount = resultSet.getDouble("_amt");
            System.out.println("- orig. " + conceptName + ": " + SLibUtils.getDecimalFormatAmount().format(amount));

            for (PayrollAmount payrollAmount : payrollAmounts) {
                int referenceId = 0;
                String referenceName = "";
                String referenceCode = "";
                String formerReferenceName = "";
                String formerReferenceCode = "";

                if (payrollAmount.isEmployeeWithSuitablePackCostCenters()) {
                    // employee with suitable pack of cost centers (notice that ID of department is zero!):
                    
                    Employee employee = getEmployee(payrollAmount.EmployeeId);
                    
                    if (msParamAccountingDynamicEmployeeMode.equals(SDataConstantsSys.CFG_PARAM_HRS_PAYROLL_ACC_DYN_EMP_MODE_DEP)) {
                        // Mode for concept for employee in dynamic accounting set to 'department':
                        
                        Department department = getDepartment(getEmployeeDepartmentId(payrollAmount.EmployeeId));

                        referenceId = department.DepartmentId;
                        referenceName = department.Name;
                        referenceCode = department.Code;
                    }
                    else {
                        // Default mode for concept for employee in dynamic accounting:
                        
                        referenceId = employee.EmployeeId;
                        referenceName = employee.Name;
                        referenceCode = employee.Number;
                    }
                    
                    effBizPartnerId = employee.EmployeeId;
                    effBizPartnerBranchId = employee.BranchId;
                }
                else {
                    // nominal case:
                    
                    switch (conceptAccountingType) {
                        case SModSysConsts.HRSS_TP_ACC_GBL:
                            // default values already set
                            break;

                        case SModSysConsts.HRSS_TP_ACC_DEP:
                            if (payrollAmount.DepartmentId != 0 && payrollAmount.DepartmentId == resultSet.getInt("_dep_id")) {
                                referenceId = payrollAmount.DepartmentId;
                                referenceName = formerReferenceName = resultSet.getString("_dep_name");
                                referenceCode = formerReferenceCode = resultSet.getString("_dep_code");
                            }
                            break;

                        case SModSysConsts.HRSS_TP_ACC_EMP:
                            if (payrollAmount.EmployeeId != 0 && payrollAmount.EmployeeId == resultSet.getInt("_emp_id")) {
                                referenceId = payrollAmount.EmployeeId;
                                referenceName = formerReferenceName = resultSet.getString("_emp_name");
                                referenceCode = formerReferenceCode = resultSet.getString("_emp_num");
                            }
                            break;

                        default:
                            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }
                }

                // payroll move:
                
                moFormerPayroll.getDbmsDataFormerPayrollMoves().add(computeFormerPayrollMove(conceptType, conceptId, conceptNameAbbr,
                        referenceId, formerReferenceName, formerReferenceCode, payrollAmount.Amount, record));

                // journal voucher:

                if (payrollAmount.PackCostCentersId == SModSysConsts.HRS_PACK_CC_NA) {
                    // direct amount, cost centers are not needed:

                    record.getDbmsRecordEntries().add(computeRecordEntry(conceptAccountingType, record, accountId,
                            0, 0, effBizPartnerId, effBizPartnerBranchId, effTaxKey,
                            conceptType, conceptNameAbbr, referenceName, referenceCode, payrollAmount.Amount));
                }
                else {
                    // prorate amount, cost centers may be needed:

                    SDbPackCostCenters packCostCenters = getPackCostCenters(payrollAmount.PackCostCentersId);
                    double[] proratedAmounts = SLibProrationUtils.prorateAmount(payrollAmount.Amount, packCostCenters.getProrationPercentages());

                    for (int i = 0; i < packCostCenters.getChildCostCenters().size(); i++) {
                        record.getDbmsRecordEntries().add(computeRecordEntry(conceptAccountingType, record, accountId,
                                packCostCenters.getChildCostCenters().get(i).getPkCostCenterId(), effItemId, effBizPartnerId, effBizPartnerBranchId, effTaxKey,
                                conceptType, conceptNameAbbr, referenceName, referenceCode, proratedAmounts[i]));
                    }
                }
                
                total = SLibUtils.roundAmount(total + payrollAmount.Amount);
            }
            
            System.out.println("  total " + conceptName + ": " + SLibUtils.getDecimalFormatAmount().format(total) + (SLibUtils.compareAmount(amount, total) ? "" : " !!!"));
        }
    }
    
    /**
     * Validate accounting, and complete by saving it at the end of the original or dynamic processing.
     * @throws Exception 
     */
    private void validateAndCompleteAccounting() throws Exception {
        // Validate payroll accounting:
        
        double totalNetToBeAccounted = SLibUtils.roundAmount(mdTotalDebit - mdTotalCredit);

        if (jckAccountingGradual.isSelected()) {
            if (!SLibUtils.compareAmount(totalNetToBeAccounted, mdTotalNetSelected)) {
                throw new Exception("¡Hay una diferencia entre el total neto a contabilizar, $" + SLibUtils.getDecimalFormatAmount().format(mdTotalNetSelected) +", y "
                        + "el monto neto de la afectación contable, $" + SLibUtils.getDecimalFormatAmount().format(totalNetToBeAccounted) +"!");
            }
        }
        else {
            if (!SLibUtils.compareAmount(totalNetToBeAccounted, moPayroll.getAuxTotalNet())) {
                throw new Exception("¡Hay una diferencia entre el total neto de la nómina, $" + SLibUtils.getDecimalFormatAmount().format(moPayroll.getAuxTotalNet()) +", y "
                        + "el monto neto de la afectación contable, $" + SLibUtils.getDecimalFormatAmount().format(totalNetToBeAccounted) +"!");
            }
        }

        // Save accounting records where each payroll receipt was registered:

        moAccountingPayroll.save(miClient.getSession());

        // Save payroll accounting:

        moFormerPayroll.setDebit_r(mdTotalDebit);
        moFormerPayroll.setCredit_r(mdTotalCredit);
        moFormerPayroll.setIsAuxAccountingGradual(jckAccountingGradual.isSelected());

        SServerRequest request = new SServerRequest(SServerConstants.REQ_DB_ACTION_SAVE);
        request.setPacket(moFormerPayroll);
        SServerResponse response = miClient.getSessionXXX().request(request);

        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            throw new Exception(response.getMessage());
        }
        else {
            int result = response.getResultType();
            if (result != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage()));
            }
        }

        // Notify GUI:

        miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(SDataConstants.FIN_REC);
        miClient.getGuiModule(SDataConstants.MOD_HRS).refreshCatalogues(SDataConstants.HRS_SIE_PAY);
        ((SClient) miClient).getSession().notifySuscriptors(SModConsts.HRS_SIE_PAY);

        miClient.showMsgBoxInformation("Los recibos de nómina seleccionados han sido contabilizados.");
    }

    private void saveAccountingOriginal() throws Exception {
        SDialogMessages messages = new SDialogMessages((SGuiClient) miClient, 
                "Inconvenientes y omisiones de configuración de contabilización", 
                "Lista de inconvenientes y omisiones de configuración de contabilización:");
        
        try (Statement statementCfg = miClient.getSession().getStatement().getConnection().createStatement(); Statement statementRec = miClient.getSession().getStatement().getConnection().createStatement()) {
            if (SHrsFinUtils.validateAccountingSettingsOriginal(miClient.getSession(), moPayroll.getPkPayrollId())) {
                prepareRecordEmployeesForAccounting();

                for (RecordEmployees recordEmployees : maRecordEmployeeses) {
                    SDataRecord record = recordEmployees.Record;
                    
                    // get last entry ID for current record:
                    mnLastEntryId = getRecordLastEntryId(record);
                    mnLastSortingPosition = getRecordLastSortingPosition(record);
                    
                    String sql;
                    String sqlRecordEmployeeIds = StringUtils.join(recordEmployees.EmployeeIds, ",");
                    String conceptTypeName = "";

                    // process first earnings, then deductions:
                    
                    /*
                    iteration #1: processing of perceptions;
                    iteration #2: processing of deductions.
                    */

                    for (int conceptType = CONCEPT_TYPE_EAR; conceptType <= CONCEPT_TYPE_DED; conceptType++) {
                        if (conceptType == CONCEPT_TYPE_EAR) {
                            /* Perception:
                             * Accountable link level:
                             * 1. Global
                             * 2. By department
                             * 3. By employee
                             */

                            conceptTypeName = "percepción";

                            sql = "SELECT " + SModSysConsts.HRSS_TP_ACC_GBL + " AS _tp_acc, c.id_ear AS _id_concept, 0 AS _id_ref, "
                                    + "c.code AS _concept_code, c.name AS _concept, c.name_abbr AS _concept_abbr, "
                                    + "ac.fk_acc, ac.fk_cc_n, ac.fk_item_n, ac.fk_bp_n, ac.fk_tax_bas_n, ac.fk_tax_tax_n, "
                                    + "'' AS _ref_code, '' AS _ref "
                                    + "FROM hrs_pay AS p "
                                    + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                                    + "INNER JOIN hrs_pay_rcp_ear AS prc ON prc.id_pay = pr.id_pay AND prc.id_emp = pr.id_emp "
                                    + "INNER JOIN hrs_ear AS c ON c.id_ear = prc.fk_ear AND c.fk_tp_acc_cfg = " + SModSysConsts.HRSS_TP_ACC_GBL + " "
                                    + "LEFT OUTER JOIN hrs_acc_ear AS ac ON ac.id_ear = prc.fk_ear AND ac.id_tp_acc = " + SModSysConsts.HRSS_TP_ACC_GBL + " AND ac.id_ref = 0 "
                                    + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT prc.b_del AND (ac.b_del IS NULL OR NOT ac.b_del) AND "
                                    + "p.id_pay = " + moPayroll.getPkPayrollId() + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ") "
                                    + "UNION "
                                    + "SELECT " + SModSysConsts.HRSS_TP_ACC_DEP + " AS _tp_acc, c.id_ear AS _id_concept, pr.fk_dep AS _id_ref, "
                                    + "c.code AS _concept_code, c.name AS _concept, c.name_abbr AS _concept_abbr, "
                                    + "ac.fk_acc, ac.fk_cc_n, ac.fk_item_n, ac.fk_bp_n, ac.fk_tax_bas_n, ac.fk_tax_tax_n, "
                                    + "d.code AS _ref_code, d.name AS _ref "
                                    + "FROM hrs_pay AS p "
                                    + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                                    + "INNER JOIN hrs_pay_rcp_ear AS prc ON prc.id_pay = pr.id_pay AND prc.id_emp = pr.id_emp "
                                    + "INNER JOIN hrs_ear AS c ON c.id_ear = prc.fk_ear AND c.fk_tp_acc_cfg = " + SModSysConsts.HRSS_TP_ACC_DEP + " "
                                    + "LEFT OUTER JOIN hrs_acc_ear AS ac ON ac.id_ear = prc.fk_ear AND ac.id_tp_acc = " + SModSysConsts.HRSS_TP_ACC_DEP + " AND ac.id_ref = pr.fk_dep "
                                    + "LEFT OUTER JOIN erp.hrsu_dep AS d ON d.id_dep = pr.fk_dep "
                                    + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT prc.b_del AND (ac.b_del IS NULL OR NOT ac.b_del) AND "
                                    + "p.id_pay = " + moPayroll.getPkPayrollId() + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ") "
                                    + "UNION "
                                    + "SELECT " + SModSysConsts.HRSS_TP_ACC_EMP + " AS _tp_acc, c.id_ear AS _id_concept, pr.id_emp AS _id_ref, "
                                    + "c.code AS _concept_code, c.name AS _concept, c.name_abbr AS _concept_abbr, "
                                    + "ac.fk_acc, ac.fk_cc_n, ac.fk_item_n, ac.fk_bp_n, ac.fk_tax_bas_n, ac.fk_tax_tax_n, "
                                    + "e.num AS _ref_code, b.bp AS _ref "
                                    + "FROM hrs_pay AS p "
                                    + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                                    + "INNER JOIN hrs_pay_rcp_ear AS prc ON prc.id_pay = pr.id_pay AND prc.id_emp = pr.id_emp "
                                    + "INNER JOIN hrs_ear AS c ON c.id_ear = prc.fk_ear AND c.fk_tp_acc_cfg = " + SModSysConsts.HRSS_TP_ACC_EMP + " "
                                    + "LEFT OUTER JOIN hrs_acc_ear AS ac ON ac.id_ear = prc.fk_ear AND ac.id_tp_acc = " + SModSysConsts.HRSS_TP_ACC_EMP + " AND ac.id_ref = pr.id_emp "
                                    + "LEFT OUTER JOIN erp.hrsu_emp AS e ON e.id_emp = pr.id_emp "
                                    + "LEFT OUTER JOIN erp.bpsu_bp AS b ON b.id_bp = pr.id_emp "
                                    + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT prc.b_del AND (ac.b_del IS NULL OR NOT ac.b_del) AND "
                                    + "p.id_pay = " + moPayroll.getPkPayrollId() + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ") "
                                    + "ORDER BY _tp_acc, _concept, _concept_code, _id_concept, _ref, _ref_code, _id_ref;";
                        }
                        else {
                            /* Deduction:
                             * Accountable link level:
                             * 1. Global
                             * 2. By department
                             * 3. By employee
                             */

                            conceptTypeName = "deducción";

                            sql = "SELECT " + SModSysConsts.HRSS_TP_ACC_GBL + " AS _tp_acc, c.id_ded AS _id_concept, 0 AS _id_ref, "
                                    + "c.code AS _concept_code, c.name AS _concept, c.name_abbr AS _concept_abbr, "
                                    + "ac.fk_acc, ac.fk_cc_n, ac.fk_item_n, ac.fk_bp_n, ac.fk_tax_bas_n, ac.fk_tax_tax_n, "
                                    + "'' AS _ref_code, '' AS _ref "
                                    + "FROM hrs_pay AS p "
                                    + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                                    + "INNER JOIN hrs_pay_rcp_ded AS prc ON prc.id_pay = pr.id_pay AND prc.id_emp = pr.id_emp "
                                    + "INNER JOIN hrs_ded AS c ON c.id_ded = prc.fk_ded AND c.fk_tp_acc_cfg = " + SModSysConsts.HRSS_TP_ACC_GBL + " "
                                    + "LEFT OUTER JOIN hrs_acc_ded AS ac ON ac.id_ded = prc.fk_ded AND ac.id_tp_acc = " + SModSysConsts.HRSS_TP_ACC_GBL + " AND ac.id_ref = 0 "
                                    + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT prc.b_del AND (ac.b_del IS NULL OR NOT ac.b_del) AND "
                                    + "p.id_pay = " + moPayroll.getPkPayrollId() + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ") "
                                    + "UNION "
                                    + "SELECT " + SModSysConsts.HRSS_TP_ACC_DEP + " AS _tp_acc, c.id_ded AS _id_concept, pr.fk_dep AS _id_ref, "
                                    + "c.code AS _concept_code, c.name AS _concept, c.name_abbr AS _concept_abbr, "
                                    + "ac.fk_acc, ac.fk_cc_n, ac.fk_item_n, ac.fk_bp_n, ac.fk_tax_bas_n, ac.fk_tax_tax_n, "
                                    + "d.code AS _ref_code, d.name AS _ref "
                                    + "FROM hrs_pay AS p "
                                    + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                                    + "INNER JOIN hrs_pay_rcp_ded AS prc ON prc.id_pay = pr.id_pay AND prc.id_emp = pr.id_emp "
                                    + "INNER JOIN hrs_ded AS c ON c.id_ded = prc.fk_ded AND c.fk_tp_acc_cfg = " + SModSysConsts.HRSS_TP_ACC_DEP + " "
                                    + "LEFT OUTER JOIN hrs_acc_ded AS ac ON ac.id_ded = prc.fk_ded AND ac.id_tp_acc = " + SModSysConsts.HRSS_TP_ACC_DEP + " AND ac.id_ref = pr.fk_dep "
                                    + "LEFT OUTER JOIN erp.hrsu_dep AS d ON d.id_dep = pr.fk_dep "
                                    + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT prc.b_del AND (ac.b_del IS NULL OR NOT ac.b_del) AND "
                                    + "p.id_pay = " + moPayroll.getPkPayrollId() + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ") "
                                    + "UNION "
                                    + "SELECT " + SModSysConsts.HRSS_TP_ACC_EMP + " AS _tp_acc, c.id_ded AS _id_concept, pr.id_emp AS _id_ref, "
                                    + "c.code AS _concept_code, c.name AS _concept, c.name_abbr AS _concept_abbr, "
                                    + "ac.fk_acc, ac.fk_cc_n, ac.fk_item_n, ac.fk_bp_n, ac.fk_tax_bas_n, ac.fk_tax_tax_n, "
                                    + "e.num AS _ref_code, b.bp AS _ref "
                                    + "FROM hrs_pay AS p "
                                    + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                                    + "INNER JOIN hrs_pay_rcp_ded AS prc ON prc.id_pay = pr.id_pay AND prc.id_emp = pr.id_emp "
                                    + "INNER JOIN hrs_ded AS c ON c.id_ded = prc.fk_ded AND c.fk_tp_acc_cfg = " + SModSysConsts.HRSS_TP_ACC_EMP + " "
                                    + "LEFT OUTER JOIN hrs_acc_ded AS ac ON ac.id_ded = prc.fk_ded AND ac.id_tp_acc = " + SModSysConsts.HRSS_TP_ACC_EMP + " AND ac.id_ref = pr.id_emp "
                                    + "LEFT OUTER JOIN erp.hrsu_emp AS e ON e.id_emp = pr.id_emp "
                                    + "LEFT OUTER JOIN erp.bpsu_bp AS b ON b.id_bp = pr.id_emp "
                                    + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT prc.b_del AND (ac.b_del IS NULL OR NOT ac.b_del) AND "
                                    + "p.id_pay = " + moPayroll.getPkPayrollId() + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ") "
                                    + "ORDER BY _tp_acc, _concept, _concept_code, _id_concept, _ref, _ref_code, _id_ref;";
                        }

                        ResultSet resultSetCfg = statementCfg.executeQuery(sql);
                        while (resultSetCfg.next()) {
                            int accountingType = resultSetCfg.getInt("_tp_acc");
                            int conceptId = resultSetCfg.getInt("_id_concept");
                            String conceptCode = resultSetCfg.getString("_concept_code");
                            String concept = resultSetCfg.getString("_concept");
                            String conceptAbbr = resultSetCfg.getString("_concept_abbr");
                            int referenceId = resultSetCfg.getInt("_id_ref");
                            String referenceCode = resultSetCfg.getString("_ref_code");
                            String referenceName = resultSetCfg.getString("_ref");
                            int accountId = resultSetCfg.getInt("fk_acc");
                            int costCenterId = resultSetCfg.getInt("fk_cc_n");
                            int itemId = resultSetCfg.getInt("fk_item_n");
                            int bizPartnerId = resultSetCfg.getInt("fk_bp_n");
                            int taxBasicId = resultSetCfg.getInt("fk_tax_bas_n");
                            int taxTaxId = resultSetCfg.getInt("fk_tax_tax_n");

                            String message = "La configuración de contabilización de la " + conceptTypeName + " código '" + conceptCode + "', '" + concept + "' ('" + conceptAbbr + "'), ";

                            switch (accountingType) {
                                case SModSysConsts.HRSS_TP_ACC_GBL: // global link
                                    message += "del ámbito global, tiene un problema:\n";
                                    break;
                                case SModSysConsts.HRSS_TP_ACC_DEP: // link by department
                                    message += "del departamento código '" + referenceCode + "', '" + referenceName + "', tiene un problema:\n";
                                    break;
                                case SModSysConsts.HRSS_TP_ACC_EMP: // link by employee
                                    message += "del empleado clave '" + referenceCode + "', '" + referenceName + "', tiene un problema:\n";
                                    break;
                                default:
                                    // nothing
                            }

                            // Validate account:

                            if (accountId == 0) {
                                messages.appendMessage(message + "La 'cuenta contable' no ha sido especificada aún.");
                            }
                            else {
                                try {
                                    // validates account and, if necessary, cost center:
                                    SHrsFinUtils.validateAccount(miClient.getSession(), accountId, costCenterId, bizPartnerId, itemId, taxBasicId, taxTaxId);
                                }
                                catch (Exception e) {
                                    messages.appendMessage(message + e.getMessage());
                                }
                            }

                            // Validate item:

                            SDataItem item = null;

                            if (itemId > 0) {
                                item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM,  new int[] { itemId }, SLibConstants.EXEC_MODE_VERBOSE);

                                if (item == null) {
                                    messages.appendMessage(message + "El registro del 'ítem' (ID: " + itemId + ") no existe.");
                                }
                                else if (item.getIsDeleted()) {
                                    messages.appendMessage(message + "El registro del 'ítem' (ID: " + itemId + ") está eliminado.");
                                }
                            }

                            // Validate business partner:

                            SDataBizPartner bizPartner = null;

                            if (bizPartnerId == 0) {
                                if (accountingType == SModSysConsts.HRSS_TP_ACC_EMP) {
                                    messages.appendMessage(message + "El 'asociado de negocios' no ha sido especificado aún.");
                                }
                            }
                            else {
                                bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { bizPartnerId }, SLibConstants.EXEC_MODE_VERBOSE);

                                if (bizPartner == null) {
                                    messages.appendMessage(message + "El registro del 'asociado de negocios' (ID: " + bizPartnerId + ") no existe.");
                                }
                                else if (bizPartner.getIsDeleted()) {
                                    messages.appendMessage(message + "El registro del 'asociado de negocios' (ID: " + bizPartnerId + ") está eliminado.");
                                }
                            }

                            // Validate tax:

                            SDataTax tax = null;

                            if (taxBasicId != 0 && taxTaxId != 0) {
                                tax = (SDataTax) SDataUtilities.readRegistry(miClient, SDataConstants.FINU_TAX, new int[] { taxBasicId, taxTaxId }, SLibConstants.EXEC_MODE_VERBOSE);

                                if (tax == null) {
                                    messages.appendMessage(message + "El registro del 'impuesto' (ID: " + taxBasicId + "-" + taxTaxId + ") no existe.");
                                }
                                else if (tax.getIsDeleted()) {
                                    messages.appendMessage(message + "El registro del 'impuesto' (ID: " + taxBasicId + "-" + taxTaxId + ") está eliminado.");
                                }
                            }

                            if (conceptType == CONCEPT_TYPE_EAR) {
                                /* Perception:
                                 * Accountable link level:
                                 * 1. Global
                                 * 2. By departatment
                                 * 3. By employee
                                 */

                                conceptTypeName = "percepción";

                                sql = "SELECT e.fk_tp_acc_rec AS f_tp_acc_rec, e.id_ear, e.name_abbr, 0 AS f_id_ref, '' AS f_ref, '' AS f_ref_cve, SUM(pre.amt_r) AS f_amt " +
                                        "FROM hrs_pay AS p " +
                                        "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay " +
                                        "INNER JOIN hrs_pay_rcp_ear AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp " +
                                        "INNER JOIN hrs_ear AS e ON e.id_ear = pre.fk_ear " +
                                        "WHERE p.b_del = 0 AND pr.b_del = 0 AND pre.b_del = 0 AND e.fk_tp_acc_rec = " + SModSysConsts.HRSS_TP_ACC_GBL + " AND p.id_pay = " + moPayroll.getPkPayrollId() + " " +
                                        "AND e.id_ear = " + conceptId + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ") " +
                                        "GROUP BY e.id_ear, e.name_abbr " +
                                        "UNION " +
                                        "SELECT e.fk_tp_acc_rec AS f_tp_acc_rec, e.id_ear, e.name_abbr, dep.id_dep AS f_id_ref, dep.name AS f_ref, dep.code AS f_ref_cve, SUM(pre.amt_r) AS f_amt " +
                                        "FROM hrs_pay AS p " +
                                        "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay " +
                                        "INNER JOIN hrs_pay_rcp_ear AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp " +
                                        "INNER JOIN hrs_ear AS e ON e.id_ear = pre.fk_ear " +
                                        "INNER JOIN erp.hrsu_dep AS dep ON dep.id_dep = pr.fk_dep " +
                                        "WHERE p.b_del = 0 AND pr.b_del = 0 AND pre.b_del = 0 AND e.fk_tp_acc_rec = " + SModSysConsts.HRSS_TP_ACC_DEP + " AND p.id_pay = " + moPayroll.getPkPayrollId() + " " +
                                        "AND e.id_ear = " + conceptId + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ")" +
                                        (accountingType == SModSysConsts.HRSS_TP_ACC_DEP ? (" AND dep.id_dep = " + referenceId + " ") : "") + 
                                        "GROUP BY e.id_ear, e.name_abbr, dep.id_dep, dep.name, dep.code " +
                                        "UNION " +
                                        "SELECT e.fk_tp_acc_rec AS f_tp_acc_rec, e.id_ear, e.name_abbr, bp.id_bp AS f_id_ref, bp.bp AS f_ref, '' AS f_ref_cve, SUM(pre.amt_r) AS f_amt " +
                                        "FROM hrs_pay AS p " +
                                        "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay " +
                                        "INNER JOIN hrs_pay_rcp_ear AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp " +
                                        "INNER JOIN hrs_ear AS e ON e.id_ear = pre.fk_ear " +
                                        "INNER JOIN erp.hrsu_emp AS emp ON emp.id_emp = pr.id_emp " +
                                        "INNER JOIN erp.bpsu_bp AS bp ON bp.id_bp = emp.id_emp " +
                                        "INNER JOIN erp.hrsu_dep AS dep ON dep.id_dep = pr.fk_dep " +
                                        "WHERE p.b_del = 0 AND pr.b_del = 0 AND pre.b_del = 0 AND e.fk_tp_acc_rec = " + SModSysConsts.HRSS_TP_ACC_EMP + " AND p.id_pay = " + moPayroll.getPkPayrollId() + " " +
                                        "AND e.id_ear = " + conceptId + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ") " + 
                                        ((accountingType == SModSysConsts.HRSS_TP_ACC_EMP) ? ("AND emp.id_emp = " + referenceId + " ") : (accountingType == SModSysConsts.HRSS_TP_ACC_DEP) ? ("AND dep.id_dep = " + referenceId + " ") : "") +
                                        "GROUP BY e.id_ear, e.name_abbr, bp.id_bp, bp.bp " +
                                        "ORDER BY f_tp_acc_rec, id_ear, f_ref;";
                            }
                            else {
                                /* Deduction:
                                 * Accountable link level:
                                 * 1. Global
                                 * 2. By departatment
                                 * 3. By employee
                                 */

                                conceptTypeName = "deducción";

                                sql = "SELECT d.fk_tp_acc_rec AS f_tp_acc_rec, d.id_ded, d.name_abbr, 0 AS f_id_ref, '' AS f_ref, '' AS f_ref_cve, SUM(prd.amt_r) AS f_amt " +
                                        "FROM hrs_pay AS p " +
                                        "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay " +
                                        "INNER JOIN hrs_pay_rcp_ded AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp " +
                                        "INNER JOIN hrs_ded AS d ON d.id_ded = prd.fk_ded " +
                                        "WHERE p.b_del = 0 AND pr.b_del = 0 AND prd.b_del = 0 AND d.fk_tp_acc_rec = " + SModSysConsts.HRSS_TP_ACC_GBL + " AND p.id_pay = " + moPayroll.getPkPayrollId() + " " +
                                        "AND d.id_ded = " + conceptId + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ") " +
                                        "GROUP BY d.id_ded, d.name_abbr " +
                                        "UNION " +
                                        "SELECT d.fk_tp_acc_rec AS f_tp_acc_rec, d.id_ded, d.name_abbr, dep.id_dep AS f_id_ref, dep.name AS f_ref, dep.code AS f_ref_cve, SUM(prd.amt_r) AS f_amt " +
                                        "FROM hrs_pay AS p " +
                                        "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay " +
                                        "INNER JOIN hrs_pay_rcp_ded AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp " +
                                        "INNER JOIN hrs_ded AS d ON d.id_ded = prd.fk_ded " +
                                        "INNER JOIN erp.hrsu_dep AS dep ON dep.id_dep = pr.fk_dep " +
                                        "WHERE p.b_del = 0 AND pr.b_del = 0 AND prd.b_del = 0 AND d.fk_tp_acc_rec = " + SModSysConsts.HRSS_TP_ACC_DEP + " AND p.id_pay = " + moPayroll.getPkPayrollId() + " " +
                                        "AND d.id_ded = " + conceptId + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ")" +
                                        (accountingType == SModSysConsts.HRSS_TP_ACC_DEP ? (" AND dep.id_dep = " + referenceId + " ") : "") +
                                        "GROUP BY d.id_ded, d.name_abbr, dep.id_dep, dep.name, dep.code " +
                                        "UNION " +
                                        "SELECT d.fk_tp_acc_rec AS f_tp_acc_rec, d.id_ded, d.name_abbr, bp.id_bp AS f_id_ref, bp.bp AS f_ref, emp.num AS f_ref_cve, SUM(prd.amt_r) AS f_amt " +
                                        "FROM hrs_pay AS p " +
                                        "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay " +
                                        "INNER JOIN hrs_pay_rcp_ded AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp " +
                                        "INNER JOIN hrs_ded AS d ON d.id_ded = prd.fk_ded " +
                                        "INNER JOIN erp.hrsu_emp AS emp ON emp.id_emp = pr.id_emp " +
                                        "INNER JOIN erp.bpsu_bp AS bp ON bp.id_bp = emp.id_emp " +
                                        "INNER JOIN erp.hrsu_dep AS dep ON dep.id_dep = pr.fk_dep " +
                                        "WHERE p.b_del = 0 AND pr.b_del = 0 AND prd.b_del = 0 AND d.fk_tp_acc_rec = " + SModSysConsts.HRSS_TP_ACC_EMP + " AND p.id_pay = " + moPayroll.getPkPayrollId() + " " +
                                        "AND d.id_ded = " + conceptId + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ") " +
                                        ((accountingType == SModSysConsts.HRSS_TP_ACC_EMP) ? ("AND emp.id_emp = " + referenceId + " ") : (accountingType == SModSysConsts.HRSS_TP_ACC_DEP) ? ("AND dep.id_dep = " + referenceId + " ") : "") +
                                        "GROUP BY d.id_ded, d.name_abbr, bp.id_bp, bp.bp " +
                                        "ORDER BY f_tp_acc_rec, id_ded, f_ref;";
                            }

                            ResultSet resultSetRec = statementRec.executeQuery(sql);
                            while (resultSetRec.next()) {
                                int conceptAccountingType = resultSetRec.getInt("f_tp_acc_rec");
                                referenceId = resultSetRec.getInt("f_id_ref");
                                referenceName = resultSetRec.getString("f_ref");
                                referenceCode = resultSetRec.getString("f_ref_cve");
                                double amount = resultSetRec.getDouble("f_amt");

                                // Create former payroll move:

                                moFormerPayroll.getDbmsDataFormerPayrollMoves().add(computeFormerPayrollMove(conceptType, conceptId, concept, 
                                        referenceId, referenceName, referenceCode, amount, record));
                                
                                // Create record entry:

                                int bpId = bizPartner == null ? 0 : bizPartner.getPkBizPartnerId();
                                int bpbId = bizPartner == null ? 0 : bizPartner.getDbmsBizPartnerBranches().get(0).getPkBizPartnerBranchId();

                                record.getDbmsRecordEntries().add(computeRecordEntry(conceptAccountingType, record, accountId, 
                                        costCenterId, itemId, bpId, bpbId, new int[] { taxBasicId, taxTaxId }, 
                                        conceptType, conceptAbbr, referenceName, referenceCode, amount));

                            } // end record
                        } // end configuration
                    } // end processing of perceptions and deductions
                    
                    // add record for further processing:
                    moFormerPayroll.getAuxDataRecords().add(record);
                    
                    // reserve last entry ID and sorting position for current record:
                    preserveRecordLastEntryId(record);
                    preserveRecordLastSortingPosition(record);
                }
            }
        }

        if (messages.getMessagesCount() > 0) {
            if (messages.getMessagesCount() == 1) {
                throw new Exception(messages.getMessages()); // throw exception with a simple message
            }
            else {
                messages.setVisible(true);
                throw new Exception("Favor de corregir los " + messages.getMessagesCount() + " inconvenientes u omisiones de configuración de contabilización de nóminas mencionados.");
            }
        }

        validateAndCompleteAccounting();
    }
    
    private void saveAccountingDynamic() throws Exception {
        ArrayList<Integer> employeeIds = new ArrayList<>();
        
        for (STableRow row : moTablePaneEmpSelected.getTableModel().getTableRows()) {
            employeeIds.add(((int[]) ((SRowEmployee) row).getPrimaryKey())[0]);
        }
        
        ArrayList<String> exceptions = SHrsFinUtils.validateAccountingSettingsDynamic(miClient.getSession(), moPayroll.getPkPayrollId(), moPayroll.getDateEnd(), ArrayUtils.toPrimitive(employeeIds.toArray(new Integer[] {})));
        
        if (exceptions.isEmpty()) {
            try (Statement statement = miClient.getSession().getStatement().getConnection().createStatement(); Statement statementAux = miClient.getSession().getStatement().getConnection().createStatement();) {
                prepareRecordEmployeesForAccounting(); // prepare journal voucher for employees for accounting

                ArrayList<Integer> employeeIdsWithPackCostCentersInPayroll = SHrsFinUtils.getEmployeeIdsWithSuitablePackCostCenters(miClient.getSession(), moPayroll.getPkPayrollId(), moPayroll.getDateEnd()); // get employees with suitable pack of cost centers
                
                // process all selected journal vouchers and its employees:
                
                for (RecordEmployees recordEmployees : maRecordEmployeeses) {
                    String sql = "";
                    String sqlRecordEmployeeIds = StringUtils.join(recordEmployees.EmployeeIds, ",");
                    SDataRecord record = recordEmployees.Record; // convenience variable
                    ArrayList<Integer> employeeIdsWithPackCostCentersInRecord = getIntersection(employeeIdsWithPackCostCentersInPayroll, recordEmployees.EmployeeIds);
                    
                    // get last entry ID for current record:
                    mnLastEntryId = getRecordLastEntryId(record);
                    mnLastSortingPosition = getRecordLastSortingPosition(record);
                    
                    // process first earnings, then deductions:
                    mdRecordEarnings = 0;
                    mdRecordDeductions = 0;
                    
                    /*
                    iteration #1: processing of perceptions;
                    iteration #2: processing of deductions.
                    */
                    
                    for (int conceptType = CONCEPT_TYPE_EAR; conceptType <= CONCEPT_TYPE_DED; conceptType++) {
                        /*
                        1. Process earnings and deductions whose accounting type for bookkeeping is GLOBAL.
                        NOTE: 1) An individual accounting setting for each earning or deduction must exist.
                        */

                        switch (conceptType) {
                            case CONCEPT_TYPE_EAR:
                                sql = "SELECT e.id_ear AS _cpt_id, e.code AS _cpt_code, e.name AS _cpt_name, e.name_abbr AS _cpt_name_abbr, "
                                        + "0 AS _dep_id, '' AS _dep_name, '' AS _dep_code, "
                                        + "0 AS _emp_id, '' AS _emp_name, '' AS _emp_num, 0 AS _empb_id, "
                                        + "cac.fk_acc AS _acc_id, "
                                        + "cac.fk_bp_n AS _bp_id, "
                                        + "bpbc.id_bpb AS _bpb_id, "
                                        + "cac.fk_tax_bas_n AS _tax_bas_id, "
                                        + "cac.fk_tax_tax_n AS _tax_tax_id, "
                                        + "1 AS _tp_exp_id, " // XXX 2024-01-02, Sergio Flores: Improve this! There is no way to define an expense type, because expenses types are defined in departments!
                                        + "cac.fk_pack_exp AS _pack_exp_id, " // XXX 2023-11-29, Sergio Flores: Improve this! There is no way to setup an expense type for GLOBAL accounting record type!
                                        + "cac.fk_pack_cc AS _pack_cc_id, " // XXX 2023-11-29, Sergio Flores: Improve this! There is no way to setup an expense type for GLOBAL accounting record type!
                                        + "SUM(pre.amt_r) AS _amt "
                                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS e ON e.id_ear = pre.fk_ear "
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_CFG_ACC_EAR) + " AS cac ON cac.id_ear = e.id_ear AND cac.fk_tp_acc_rec = e.fk_tp_acc_rec AND NOT cac.b_del " // exclude unmatching and deleted accounting settings
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpbc ON bpbc.fid_bp = cac.fk_bp_n AND bpbc.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " // restrict only to HQ
                                        + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del "
                                        + "AND p.id_pay = " + moPayroll.getPkPayrollId() + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ") "
                                        + "AND e.fk_tp_acc_rec = " + SModSysConsts.HRSS_TP_ACC_GBL + " "
                                        + "GROUP BY e.id_ear, e.code, e.name, e.name_abbr "
                                        + "ORDER BY e.code, e.name, e.name_abbr, e.id_ear;";
                                break;
                                
                            case CONCEPT_TYPE_DED:
                                sql = "SELECT d.id_ded AS _cpt_id, d.code AS _cpt_code, d.name AS _cpt_name, d.name_abbr AS _cpt_name_abbr, "
                                        + "0 AS _dep_id, '' AS _dep_name, '' AS _dep_code, "
                                        + "0 AS _emp_id, '' AS _emp_name, '' AS _emp_num, 0 AS _empb_id, "
                                        + "cac.fk_acc AS _acc_id, "
                                        + "cac.fk_bp_n AS _bp_id, "
                                        + "bpbc.id_bpb AS _bpb_id, "
                                        + "cac.fk_tax_bas_n AS _tax_bas_id, "
                                        + "cac.fk_tax_tax_n AS _tax_tax_id, "
                                        + "1 AS _tp_exp_id, " // XXX 2024-01-02, Sergio Flores: Improve this! There is no way to define an expense type, because expenses types are defined in departments!
                                        + "cac.fk_pack_exp AS _pack_exp_id, " // XXX 2023-11-29, Sergio Flores: Improve this! There is no way to setup an expense type for GLOBAL accounting record type!
                                        + "cac.fk_pack_cc AS _pack_cc_id, " // XXX 2023-11-29, Sergio Flores: Improve this! There is no way to setup an expense type for GLOBAL accounting record type!
                                        + "SUM(prd.amt_r) AS _amt "
                                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " AS d ON d.id_ded = prd.fk_ded "
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_CFG_ACC_DED) + " AS cac ON cac.id_ded = d.id_ded AND cac.fk_tp_acc_rec = d.fk_tp_acc_rec AND NOT cac.b_del " // exclude unmatching and deleted accounting settings
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpbc ON bpbc.fid_bp = cac.fk_bp_n AND bpbc.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " // restrict only to HQ
                                        + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT prd.b_del "
                                        + "AND p.id_pay = " + moPayroll.getPkPayrollId() + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ") "
                                        + "AND d.fk_tp_acc_rec = " + SModSysConsts.HRSS_TP_ACC_GBL + " "
                                        + "GROUP BY d.id_ded, d.code, d.name, d.name_abbr "
                                        + "ORDER BY d.code, d.name, d.name_abbr, d.id_ded;";
                                break;
                                
                            default:
                                // nothing
                        }
                        
                        computeConceptAccountingDynamic(SModSysConsts.HRSS_TP_ACC_GBL, conceptType, statement, statementAux,
                                sql, sqlRecordEmployeeIds, employeeIdsWithPackCostCentersInRecord, record);

                        /*
                        2. Process earnings and deductions whose accounting type for bookkeeping is DEPARTMENT.
                        NOTE: 1) An individual accounting setting for each department must exist.
                        */

                        switch (conceptType) {
                            case CONCEPT_TYPE_EAR:
                                sql = "SELECT e.id_ear AS _cpt_id, e.code AS _cpt_code, e.name AS _cpt_name, e.name_abbr AS _cpt_name_abbr, "
                                        + "dep.id_dep AS _dep_id, dep.name AS _dep_name, dep.code AS _dep_code, "
                                        + "0 AS _emp_id, '' AS _emp_name, '' AS _emp_num, 0 AS _empb_id, "
                                        + "CASE WHEN cac.fk_acc <> " + SDataConstantsSys.NA + " THEN cac.fk_acc WHEN cad.fk_acc <> " + SDataConstantsSys.NA + " THEN cad.fk_acc ELSE tea.fk_acc END AS _acc_id, "
                                        + "CASE WHEN cad.fk_bp_n IS NOT NULL THEN cad.fk_bp_n ELSE cac.fk_bp_n END AS _bp_id, "
                                        + "CASE WHEN cad.fk_bp_n IS NOT NULL THEN bpbd.id_bpb ELSE bpbc.id_bpb END AS _bpb_id, "
                                        + "CASE WHEN cad.fk_tax_bas_n IS NOT NULL THEN cad.fk_tax_bas_n ELSE cac.fk_tax_bas_n END AS _tax_bas_id, "
                                        + "CASE WHEN cad.fk_tax_bas_n IS NOT NULL THEN cad.fk_tax_tax_n ELSE cac.fk_tax_tax_n END AS _tax_tax_id, "
                                        + "cad.fk_tp_exp AS _tp_exp_id, "
                                        + "cac.fk_pack_exp AS _pack_exp_id, "
                                        + "cac.fk_pack_cc AS _pack_cc_id, "
                                        + "SUM(pre.amt_r) AS _amt "
                                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS e ON e.id_ear = pre.fk_ear "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON dep.id_dep = pr.fk_dep "
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_CFG_ACC_EAR) + " AS cac ON cac.id_ear = e.id_ear AND cac.fk_tp_acc_rec = e.fk_tp_acc_rec AND NOT cac.b_del " // exclude unmatching and deleted accounting settings
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpbc ON bpbc.fid_bp = cac.fk_bp_n AND bpbc.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " // restrict only to HQ
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_CFG_ACC_DEP) + " AS cad ON cad.id_dep = pr.fk_dep AND NOT cad.b_del " // exclude deleted accounting settings
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpbd ON bpbd.fid_bp = cad.fk_bp_n AND bpbd.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " // restrict only to HQ
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_TP_EXP_ACC) + " AS tea ON tea.id_tp_exp = cad.fk_tp_exp AND NOT tea.b_del " // exclude deleted accounting settings
                                        + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del "
                                        + "AND p.id_pay = " + moPayroll.getPkPayrollId() + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ") "
                                        + "AND e.fk_tp_acc_rec = " + SModSysConsts.HRSS_TP_ACC_DEP + " "
                                        + "GROUP BY e.id_ear, e.code, e.name, e.name_abbr, dep.id_dep, dep.name "
                                        + "ORDER BY e.code, e.name, e.name_abbr, e.id_ear, dep.name, dep.id_dep;";
                                break;
                                
                            case CONCEPT_TYPE_DED:
                                sql = "SELECT d.id_ded AS _cpt_id, d.code AS _cpt_code, d.name AS _cpt_name, d.name_abbr AS _cpt_name_abbr, "
                                        + "dep.id_dep AS _dep_id, dep.name AS _dep_name, dep.code AS _dep_code, "
                                        + "0 AS _emp_id, '' AS _emp_name, '' AS _emp_num, 0 AS _empb_id, "
                                        + "CASE WHEN cac.fk_acc <> " + SDataConstantsSys.NA + " THEN cac.fk_acc WHEN cad.fk_acc <> " + SDataConstantsSys.NA + " THEN cad.fk_acc ELSE tea.fk_acc END AS _acc_id, "
                                        + "CASE WHEN cad.fk_bp_n IS NOT NULL THEN cad.fk_bp_n ELSE cac.fk_bp_n END AS _bp_id, "
                                        + "CASE WHEN cad.fk_bp_n IS NOT NULL THEN bpbd.id_bpb ELSE bpbc.id_bpb END AS _bpb_id, "
                                        + "CASE WHEN cad.fk_tax_bas_n IS NOT NULL THEN cad.fk_tax_bas_n ELSE cac.fk_tax_bas_n END AS _tax_bas_id, "
                                        + "CASE WHEN cad.fk_tax_bas_n IS NOT NULL THEN cad.fk_tax_tax_n ELSE cac.fk_tax_tax_n END AS _tax_tax_id, "
                                        + "cad.fk_tp_exp AS _tp_exp_id, "
                                        + "cac.fk_pack_exp AS _pack_exp_id, "
                                        + "cac.fk_pack_cc AS _pack_cc_id, "
                                        + "SUM(prd.amt_r) AS _amt "
                                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " AS d ON d.id_ded = prd.fk_ded "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON dep.id_dep = pr.fk_dep "
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_CFG_ACC_DED) + " AS cac ON cac.id_ded = d.id_ded AND cac.fk_tp_acc_rec = d.fk_tp_acc_rec AND NOT cac.b_del " // exclude unmatching and deleted accounting settings
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpbc ON bpbc.fid_bp = cac.fk_bp_n AND bpbc.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " // restrict only to HQ
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_CFG_ACC_DEP) + " AS cad ON cad.id_dep = pr.fk_dep AND NOT cad.b_del " // exclude deleted accounting settings
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpbd ON bpbd.fid_bp = cad.fk_bp_n AND bpbd.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " // restrict only to HQ
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_TP_EXP_ACC) + " AS tea ON tea.id_tp_exp = cad.fk_tp_exp AND NOT tea.b_del " // exclude deleted accounting settings
                                        + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT prd.b_del "
                                        + "AND p.id_pay = " + moPayroll.getPkPayrollId() + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ") "
                                        + "AND d.fk_tp_acc_rec = " + SModSysConsts.HRSS_TP_ACC_DEP + " "
                                        + "GROUP BY d.id_ded, d.code, d.name, d.name_abbr, dep.id_dep, dep.name "
                                        + "ORDER BY d.code, d.name, d.name_abbr, d.id_ded, dep.name, dep.id_dep;";
                                break;
                                
                            default:
                                // nothing
                        }
                        
                        computeConceptAccountingDynamic(SModSysConsts.HRSS_TP_ACC_DEP, conceptType, statement, statementAux,
                                sql, sqlRecordEmployeeIds, employeeIdsWithPackCostCentersInRecord, record);

                        /*
                        3. Process earnings and deductions whose accounting type for bookkeeping is EMPLOYEE.
                        NOTE: 1) An individual accounting setting for each department must exist, as well as step 2.
                        */

                        switch (conceptType) {
                            case CONCEPT_TYPE_EAR:
                                sql = "SELECT e.id_ear AS _cpt_id, e.code AS _cpt_code, e.name AS _cpt_name, e.name_abbr AS _cpt_name_abbr, "
                                        + "dep.id_dep AS _dep_id, dep.name AS _dep_name, dep.code AS _dep_code, "
                                        + "emp.id_emp AS _emp_id, bp.bp AS _emp_name, emp.num AS _emp_num, bpbe.id_bpb AS _empb_id, "
                                        + "CASE WHEN cac.fk_acc <> " + SDataConstantsSys.NA + " THEN cac.fk_acc WHEN cad.fk_acc <> " + SDataConstantsSys.NA + " THEN cad.fk_acc ELSE tea.fk_acc END AS _acc_id, "
                                        + "CASE WHEN cad.fk_bp_n IS NOT NULL THEN cad.fk_bp_n ELSE cac.fk_bp_n END AS _bp_id, "
                                        + "CASE WHEN cad.fk_bp_n IS NOT NULL THEN bpbd.id_bpb ELSE bpbc.id_bpb END AS _bpb_id, "
                                        + "CASE WHEN cad.fk_tax_bas_n IS NOT NULL THEN cad.fk_tax_bas_n ELSE cac.fk_tax_bas_n END AS _tax_bas_id, "
                                        + "CASE WHEN cad.fk_tax_bas_n IS NOT NULL THEN cad.fk_tax_tax_n ELSE cac.fk_tax_tax_n END AS _tax_tax_id, "
                                        + "cad.fk_tp_exp AS _tp_exp_id, "
                                        + "cac.fk_pack_exp AS _pack_exp_id, "
                                        + "cac.fk_pack_cc AS _pack_cc_id, "
                                        + "SUM(pre.amt_r) AS _amt "
                                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS e ON e.id_ear = pre.fk_ear "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON dep.id_dep = pr.fk_dep "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp ON emp.id_emp = pr.id_emp "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON bp.id_bp = emp.id_emp "
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_CFG_ACC_EAR) + " AS cac ON cac.id_ear = e.id_ear AND cac.fk_tp_acc_rec = e.fk_tp_acc_rec AND NOT cac.b_del " // exclude unmatching and deleted accounting settings
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpbc ON bpbc.fid_bp = cac.fk_bp_n AND bpbc.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " // restrict only to HQ
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_CFG_ACC_DEP) + " AS cad ON cad.id_dep = pr.fk_dep AND NOT cad.b_del " // exclude deleted accounting settings
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpbd ON bpbd.fid_bp = cad.fk_bp_n AND bpbd.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " // restrict only to HQ
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_TP_EXP_ACC) + " AS tea ON tea.id_tp_exp = cad.fk_tp_exp AND NOT tea.b_del " // exclude deleted accounting settings
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpbe ON bpbe.fid_bp = emp.id_emp AND bpbe.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " // restrict only to HQ
                                        + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del "
                                        + "AND p.id_pay = " + moPayroll.getPkPayrollId() + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ") "
                                        + "AND e.fk_tp_acc_rec = " + SModSysConsts.HRSS_TP_ACC_EMP + " "
                                        + "GROUP BY e.id_ear, e.code, e.name, e.name_abbr, emp.id_emp, bp.bp "
                                        + "ORDER BY e.code, e.name, e.name_abbr, e.id_ear, bp.bp, emp.id_emp;";
                                break;
                                
                            case CONCEPT_TYPE_DED:
                                sql = "SELECT d.id_ded AS _cpt_id, d.code AS _cpt_code, d.name AS _cpt_name, d.name_abbr AS _cpt_name_abbr, "
                                        + "dep.id_dep AS _dep_id, dep.name AS _dep_name, dep.code AS _dep_code, "
                                        + "emp.id_emp AS _emp_id, bp.bp AS _emp_name, emp.num AS _emp_num, bpbe.id_bpb AS _empb_id, "
                                        + "CASE WHEN cac.fk_acc <> " + SDataConstantsSys.NA + " THEN cac.fk_acc WHEN cad.fk_acc <> " + SDataConstantsSys.NA + " THEN cad.fk_acc ELSE tea.fk_acc END AS _acc_id, "
                                        + "CASE WHEN cad.fk_bp_n IS NOT NULL THEN cad.fk_bp_n ELSE cac.fk_bp_n END AS _bp_id, "
                                        + "CASE WHEN cad.fk_bp_n IS NOT NULL THEN bpbd.id_bpb ELSE bpbc.id_bpb END AS _bpb_id, "
                                        + "CASE WHEN cad.fk_tax_bas_n IS NOT NULL THEN cad.fk_tax_bas_n ELSE cac.fk_tax_bas_n END AS _tax_bas_id, "
                                        + "CASE WHEN cad.fk_tax_bas_n IS NOT NULL THEN cad.fk_tax_tax_n ELSE cac.fk_tax_tax_n END AS _tax_tax_id, "
                                        + "cad.fk_tp_exp AS _tp_exp_id, "
                                        + "cac.fk_pack_exp AS _pack_exp_id, "
                                        + "cac.fk_pack_cc AS _pack_cc_id, "
                                        + "SUM(prd.amt_r) AS _amt "
                                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " AS d ON d.id_ded = prd.fk_ded "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON dep.id_dep = pr.fk_dep "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp ON emp.id_emp = pr.id_emp "
                                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON bp.id_bp = emp.id_emp "
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_CFG_ACC_DED) + " AS cac ON cac.id_ded = d.id_ded AND cac.fk_tp_acc_rec = d.fk_tp_acc_rec AND NOT cac.b_del " // exclude unmatching and deleted accounting settings
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpbc ON bpbc.fid_bp = cac.fk_bp_n AND bpbc.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " // restrict only to HQ
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_CFG_ACC_DEP) + " AS cad ON cad.id_dep = pr.fk_dep AND NOT cad.b_del " // exclude deleted accounting settings
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpbd ON bpbd.fid_bp = cad.fk_bp_n AND bpbd.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " // restrict only to HQ
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_TP_EXP_ACC) + " AS tea ON tea.id_tp_exp = cad.fk_tp_exp AND NOT tea.b_del " // exclude deleted accounting settings
                                        + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpbe ON bpbe.fid_bp = emp.id_emp AND bpbe.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " // restrict only to HQ
                                        + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT prd.b_del "
                                        + "AND p.id_pay = " + moPayroll.getPkPayrollId() + " AND pr.id_emp IN (" + sqlRecordEmployeeIds + ") "
                                        + "AND d.fk_tp_acc_rec = " + SModSysConsts.HRSS_TP_ACC_EMP + " "
                                        + "GROUP BY d.id_ded, d.code, d.name, d.name_abbr, emp.id_emp, bp.bp "
                                        + "ORDER BY d.code, d.name, d.name_abbr, d.id_ded, bp.bp, emp.id_emp;";
                                break;
                                
                            default:
                                // nothing
                        }
                        
                        computeConceptAccountingDynamic(SModSysConsts.HRSS_TP_ACC_EMP, conceptType, statement, statementAux,
                                sql, sqlRecordEmployeeIds, employeeIdsWithPackCostCentersInRecord, record);
                    }

                    // add record for further processing:
                    moFormerPayroll.getAuxDataRecords().add(record);
                    
                    // reserve last entry ID and sorting position for current record:
                    preserveRecordLastEntryId(record);
                    preserveRecordLastSortingPosition(record);
                    
                    // render record totals:
                    double[] amounts = getEmployeesAmounts(recordEmployees.EmployeeIds);
                    
                    System.out.println("Record " + record.getRecordPrimaryKey() + ":");
                    System.out.println("original earnings: " + SLibUtils.getDecimalFormatAmount().format(amounts[0]) + " deductions: " + SLibUtils.getDecimalFormatAmount().format(amounts[1]) + ";");
                    System.out.println("computed earnings: " + SLibUtils.getDecimalFormatAmount().format(mdRecordEarnings) + " deductions: " + SLibUtils.getDecimalFormatAmount().format(mdRecordDeductions) + ".");
                }
            }
            
            validateAndCompleteAccounting();
        }
        else {
            if (exceptions.size() == 1) {
                // throw exception with a simple message:
                throw new Exception(exceptions.get(0));
            }
            else {
                // show multiple exception:
                SDialogMessages messages = new SDialogMessages((SGuiClient) miClient, 
                        "Inconvenientes y omisiones de configuración de contabilización", 
                        "Lista de inconvenientes y omisiones de configuración de contabilización:");
                messages.appendMessages(exceptions);
                messages.setVisible(true);
                throw new Exception("Favor de resolver los " + exceptions.size() + " inconvenientes u omisiones mencionados de la configuración de contabilización de nóminas.");
            }
        }
    }

    private void renderCurrentRecord() {
        if (moCurrentRecord == null) {
            jtfRecordDate.setText("");
            jtfRecordBranch.setText("");
            jtfRecordNumber.setText("");
        }
        else {
            jtfRecordDate.setText(miClient.getSessionXXX().getFormatters().getDateFormat().format(moCurrentRecord.getDate()));
            jtfRecordBkc.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.FIN_BKC, new int[] { moCurrentRecord.getPkBookkeepingCenterId() }, SLibConstants.DESCRIPTION_CODE));
            jtfRecordBranch.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.BPSU_BPB, new int[] { moCurrentRecord.getFkCompanyBranchId() }, SLibConstants.DESCRIPTION_CODE));
            jtfRecordNumber.setText(moCurrentRecord.getPkRecordTypeId() + "-" + moCurrentRecord.getPkNumberId());
        }
    }

    public void actionPickRecord() {
        Object key = null;
        String message = "";

        moDialogRecordPicker.formReset();
        moDialogRecordPicker.setFilterKey(miClient.getSessionXXX().getWorkingDate());
        moDialogRecordPicker.formRefreshOptionPane();

        if (moCurrentRecord != null) {
            moDialogRecordPicker.setSelectedPrimaryKey(moCurrentRecord.getPrimaryKey());
        }

        moDialogRecordPicker.setFormVisible(true);

        if (moDialogRecordPicker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            key = moDialogRecordPicker.getSelectedPrimaryKey();

            // XXX set registry lock to accounting record

            moCurrentRecord = (SDataRecord) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_REC, key, SLibConstants.EXEC_MODE_VERBOSE);
            
            if (moCurrentRecord == null) {
                message = "No se encontró la póliza contable seleccionada.";
            }
            else if (moCurrentRecord.getIsSystem()) {
                message = "No puede seleccionarse esta póliza contable porque es de sistema.";
            }
            else if (moCurrentRecord.getIsAudited()) {
                message = "No puede seleccionarse esta póliza contable porque está auditada.";
            }
            else if (moCurrentRecord.getIsAuthorized()) {
                message = "No puede seleccionarse esta póliza contable porque está autorizada.";
            }
            else if (!SDataUtilities.isPeriodOpen(miClient, moCurrentRecord.getDate())) {
                message = "No puede seleccionarse esta póliza contable porque su período contable correspondiente está cerrado.";
            }

            if (message.length() > 0) {
                miClient.showMsgBoxWarning(message);
                moCurrentRecord = null;
            }
            else {
                renderCurrentRecord();
            }
        }
    }
    
    private SDbAccountingPayrollReceipt createAccountingPayrollReceipt(int employeeId) {
        SDbAccountingPayrollReceipt accountingPayrollReceipt = new SDbAccountingPayrollReceipt();
        
        accountingPayrollReceipt.setPkPayrollId(moPayroll.getPkPayrollId());
        //accountingPayrollEmployee.setPkAccountingId(int n);
        accountingPayrollReceipt.setPkEmployeeId(employeeId);
        accountingPayrollReceipt.setFkRecordYearId(moCurrentRecord.getPkYearId());
        accountingPayrollReceipt.setFkRecordPeriodId(moCurrentRecord.getPkPeriodId());
        accountingPayrollReceipt.setFkRecordBookkeepingCenterId(moCurrentRecord.getPkBookkeepingCenterId());
        accountingPayrollReceipt.setFkRecordRecordTypeId(moCurrentRecord.getPkRecordTypeId());
        accountingPayrollReceipt.setFkRecordNumberId(moCurrentRecord.getPkNumberId());
        
        return accountingPayrollReceipt;
    }

    public boolean actionAdd() {
        boolean error = true;
        int index = moTablePaneEmpAvailable.getTable().getSelectedRow();

        if (index == -1) {
            miClient.showMsgBoxWarning("Seleccionar uno de los " + SGuiUtils.getLabelName(((TitledBorder) jpEmployeesAvailable.getBorder()).getTitle()) + ".");
            moTablePaneEmpAvailable.getTable().requestFocusInWindow();
        }
        else if (moCurrentRecord == null) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlRecord.getText() + "'.");
            jbPickRecord.requestFocus();
        }
        else {
            SRowEmployee row = (SRowEmployee) moTablePaneEmpAvailable.getSelectedTableRow();
            row.setData(moCurrentRecord.getPrimaryKey());
            row.getValues().add(SLibUtils.DecimalFormatCalendarYear.format(moCurrentRecord.getPkYearId()) + "-" + SLibUtils.DecimalFormatCalendarMonth.format(moCurrentRecord.getPkPeriodId()));
            row.getValues().add(jtfRecordBkc.getText());
            row.getValues().add(jtfRecordBranch.getText());
            row.getValues().add(jtfRecordNumber.getText());
            row.getValues().add(moCurrentRecord.getDate());

            row.setAccountingPayrollReceipt(createAccountingPayrollReceipt(row.getFkBizPartnerId()));

            moTablePaneEmpAvailable.removeTableRow(index);
            moTablePaneEmpAvailable.renderTableRows();
            moTablePaneEmpAvailable.setTableRowSelection(index < moTablePaneEmpAvailable.getTableGuiRowCount() ? index : moTablePaneEmpAvailable.getTableGuiRowCount() - 1);

            moTablePaneEmpSelected.addTableRow(row);
            moTablePaneEmpSelected.renderTableRows();
            moTablePaneEmpSelected.setTableRowSelection(moTablePaneEmpSelected.getTableGuiRowCount() - 1);

            error = false;
        }
        
        computeTotals();

        return !error;
    }

    public void actionAddAll() {
        int rows = moTablePaneEmpAvailable.getTableModel().getRowCount();
        
        if (rows == 0) {
            miClient.showMsgBoxWarning("No hay " + SGuiUtils.getLabelName(((TitledBorder) jpEmployeesAvailable.getBorder()).getTitle()) + ".");
        }
        else if (moCurrentRecord == null) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlRecord.getText() + "'.");
            jbPickRecord.requestFocus();
        }
        else {
            String bank = "";
            boolean bankSelected = jcbBankFilter.getSelectedIndex() > 0;

            if (bankSelected) {
                bank = jcbBankFilter.getSelectedItem().toString();

                if (bank.equals(SHrsConsts.EMPTY_BANK)) {
                    bank = "";
                }
            }

            int from = 0;
            int employeesAdded = 0;

            for (int row = 0; row < rows; row++) {
                moTablePaneEmpAvailable.setTableRowSelection(from);

                if (bankSelected) {
                    if (!((SRowEmployee) moTablePaneEmpAvailable.getSelectedTableRow()).getBank().equals(bank)) {
                        from++;
                        continue;
                    }
                }

                if (actionAdd()) {
                    employeesAdded++;
                }
                else {
                    break;
                }
            }
            
            if (bankSelected && employeesAdded == 0) {
                miClient.showMsgBoxInformation("Ninguno de los recibos pendientes coincide con el banco: '" + (!bank.isEmpty() ? bank : SHrsConsts.EMPTY_BANK) + "'.");
            }
        }
    }

    public boolean actionRemove() {
        boolean error = true;
        int index = moTablePaneEmpSelected.getTable().getSelectedRow();
        
        if (index == -1) {
            miClient.showMsgBoxWarning("Seleccionar uno de los " + SGuiUtils.getLabelName(((TitledBorder) jpEmployeesSelected.getBorder()).getTitle()) + ".");
            moTablePaneEmpSelected.getTable().requestFocusInWindow();
        }
        else {
            SRowEmployee row = (SRowEmployee) moTablePaneEmpSelected.getSelectedTableRow();
            for (int i = 1; i <= JOURNAL_VOUCHER_COLS; i++) {
                row.getValues().remove(JOURNAL_VOUCHER_COLS_INDEX);
            }

            moTablePaneEmpSelected.removeTableRow(index);
            moTablePaneEmpSelected.renderTableRows();
            moTablePaneEmpSelected.setTableRowSelection(index < moTablePaneEmpSelected.getTableGuiRowCount() ? index : moTablePaneEmpSelected.getTableGuiRowCount() - 1);

            moTablePaneEmpAvailable.addTableRow(row);
            moTablePaneEmpAvailable.renderTableRows();
            moTablePaneEmpAvailable.setTableRowSelection(moTablePaneEmpAvailable.getTableGuiRowCount() - 1);

            error = false;
        }
        
        computeTotals();

        return !error;
    }

    public void actionRemoveAll() {
        int rows = moTablePaneEmpSelected.getTableModel().getRowCount();
        
        if (rows == 0) {
            miClient.showMsgBoxWarning("No hay " + SGuiUtils.getLabelName(((TitledBorder) jpEmployeesSelected.getBorder()).getTitle()) + ".");
        }
        else {
            while (moTablePaneEmpSelected.getTableGuiRowCount() > 0) {
                moTablePaneEmpSelected.setTableRowSelection(0);
                if (!actionRemove()) {
                    break;
                }
            }
        }
    }

    public void actionOk() {
        String msg = "";
        boolean compute = true;
        int empAvailable = moTablePaneEmpAvailable.getTableGuiRowCount();

        if (moTablePaneEmpSelected.getTableGuiRowCount() == 0) {
            miClient.showMsgBoxWarning("No hay recibos seleccionados para contabilizar.");
            moTablePaneEmpAvailable.requestFocus();
        }
        else if (empAvailable > 0 && (!jckAccountingGradual.isSelected() || miClient.showMsgBoxConfirm("Se optó por contabilizar gradualmente esta nómina.\n"
                + "¿Está seguro que se desea dejar para otra ocasión la contabilización "
                + (empAvailable == 1 ? "del recibo pendiente de ser seleccionado" : "de los " + empAvailable + " recibos pendientes de ser seleccionados") + "?") != JOptionPane.YES_OPTION)) {
            miClient.showMsgBoxWarning("Seleccionar " + (empAvailable == 1 ? "el recibo que queda pendiente" : "los " + empAvailable + " recibos que quedan pendientes") + " de contabilizar.");
            moTablePaneEmpAvailable.requestFocus();
        }
        else {
            try {
                if (moCurrentRecord.getPkYearId() != moPayroll.getPeriodYear()) {
                    msg = "El año de la nómina es diferente al año de la póliza contable.\n";
                    if (miClient.showMsgBoxConfirm(msg + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                        compute = false;
                        miClient.showMsgBoxWarning(msg);
                        moTablePaneEmpAvailable.requestFocus();
                    }
                }
                else if (moCurrentRecord.getPkPeriodId() != moPayroll.getPeriod()) {
                    msg = "El periodo de la nómina es diferente al periodo de la póliza contable.\n";
                    if (miClient.showMsgBoxConfirm(msg + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                        compute = false;
                        miClient.showMsgBoxWarning(msg);
                        moTablePaneEmpAvailable.requestFocus();
                    }
                }
                
                if (compute) {
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));

                    switch (mnParamPayrollAccProcess) {
                        case SHrsConsts.CFG_ACC_PROCESS_ORIGINAL:
                            saveAccountingOriginal();
                            break;
                        case SHrsConsts.CFG_ACC_PROCESS_DYNAMIC:
                            saveAccountingDynamic();
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
                    }

                    mnFormResult = SLibConstants.FORM_RESULT_OK;
                    setVisible(false);
                }
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
            finally {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    public void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbAddAll;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbPickRecord;
    private javax.swing.JButton jbRemove;
    private javax.swing.JButton jbRemoveAll;
    private javax.swing.JComboBox jcbBankFilter;
    private javax.swing.JCheckBox jckAccountingGradual;
    private javax.swing.JLabel jlBankFilter;
    private javax.swing.JLabel jlBankFilterHint;
    private javax.swing.JLabel jlDummy01;
    private javax.swing.JLabel jlDummy3;
    private javax.swing.JLabel jlPayroll;
    private javax.swing.JLabel jlPayrollDates;
    private javax.swing.JLabel jlPayrollNet;
    private javax.swing.JLabel jlPayrollNotes;
    private javax.swing.JLabel jlRecord;
    private javax.swing.JLabel jlTotalAvailables;
    private javax.swing.JLabel jlTotalSelected;
    private javax.swing.JPanel jpAccountingRecord;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpEmployeesAvailable;
    private javax.swing.JPanel jpEmployeesSelected;
    private javax.swing.JPanel jpGrid;
    private javax.swing.JPanel jpJournalVoucher;
    private javax.swing.JPanel jpPaymentType;
    private javax.swing.JTextField jtfPayrollDates;
    private javax.swing.JTextField jtfPayrollNet;
    private javax.swing.JTextField jtfPayrollNetCur;
    private javax.swing.JTextField jtfPayrollNotes;
    private javax.swing.JTextField jtfPayrollNumber;
    private javax.swing.JTextField jtfPayrollPeriod;
    private javax.swing.JTextField jtfRecordBkc;
    private javax.swing.JTextField jtfRecordBranch;
    private javax.swing.JTextField jtfRecordDate;
    private javax.swing.JTextField jtfRecordNumber;
    // End of variables declaration//GEN-END:variables

    public void resetForm() {
        mnFormResult = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        jtfPayrollPeriod.setText("");
        jtfPayrollNumber.setText("");
        jtfPayrollDates.setText("");
        jtfPayrollNet.setText("");
        jtfPayrollNetCur.setText(miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
        jtfPayrollNotes.setText("");

        jtfRecordDate.setText("");
        jtfRecordBkc.setText("");
        jtfRecordBranch.setText("");
        jtfRecordNumber.setText("");

        moCurrentRecord = null;
        moTablePaneEmpAvailable.createTable();
        moTablePaneEmpSelected.createTable();
    }

    public int getFormResult() {
        return mnFormResult;
    }
    
    public boolean isAccountingGradual() {
        return jckAccountingGradual.isSelected();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPickRecord) {
                actionPickRecord();
            }
            else if (button == jbAdd) {
                actionAdd();
            }
            else if (button == jbAddAll) {
                actionAddAll();
            }
            else if (button == jbRemove) {
                actionRemove();
            }
            else if (button == jbRemoveAll) {
                actionRemoveAll();
            }
        }
    }
    
    /**
     * Accounting record (journal voucher) and their list of employees' IDs.
     */
    private class RecordEmployees {
        
        SDataRecord Record;
        ArrayList<Integer> EmployeeIds;
        
        public RecordEmployees(final SDataRecord record, final ArrayList<Integer> employeeIds) {
            Record = record;
            EmployeeIds = employeeIds;
        }
    }
    
    /**
     * Payroll amount for a given department, employee and pack of cost centers.
     */
    private class PayrollAmount {
        
        int DepartmentId;
        int EmployeeId;
        int PackCostCentersId;
        double Amount;
        
        public PayrollAmount(final int departmentId, final int employeeId, final int packCostCentersId, final double amount) {
            DepartmentId = departmentId;
            EmployeeId = employeeId;
            PackCostCentersId = packCostCentersId;
            Amount = amount;
        }
        
        public boolean isEmployeeWithSuitablePackCostCenters() {
            return DepartmentId == 0 && EmployeeId != 0;
        }
    }
    
    /**
     * Lite department.
     */
    private class Department {
        
        int DepartmentId;
        String Name;
        String Code;
        
        public Department(final int departmentId, final String name, final String code) {
            DepartmentId = departmentId;
            Name = name;
            Code = code;
        }
    }
    
    /**
     * Lite employee.
     */
    private class Employee {
        
        int EmployeeId;
        String Name;
        String Number;
        int BranchId; // headquarters of employee as a business partter
        
        public Employee(final int employeeId, final String name, final String number, final int branchId) {
            EmployeeId = employeeId;
            Name = name;
            Number = number;
            BranchId = branchId;
        }
    }
}

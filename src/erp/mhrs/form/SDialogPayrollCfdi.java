/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mhrs.form;

import cfd.ver3.DCfdVer3Consts;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePaneGrid;
import erp.lib.table.STableRow;
import erp.mhrs.data.SHrsPayrollEmployeeReceipt;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbConfig;
import erp.mod.hrs.db.SDbPayrollReceiptIssue;
import erp.mtrn.data.SDataCfd;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Sergio Flores, Juan Barajas
 */
public class SDialogPayrollCfdi extends JDialog implements ActionListener, ListSelectionListener {

    private int mnFormResult;
    private boolean mbFirstTime;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;

    private final erp.client.SClientInterface miClient;
    private erp.lib.form.SFormField moFieldDateIssue;
    private erp.lib.form.SFormField moFieldDatePayment;
    private erp.lib.form.SFormField moFieldCfdiRelatedUuid;
    private erp.lib.table.STablePaneGrid moTablePaneReceiptAvailable;
    private erp.lib.table.STablePaneGrid moTablePaneReceiptSelected;
    private final ArrayList<SHrsPayrollEmployeeReceipt> maHrsPayrollEmployeeReceipt;
    private erp.mod.hrs.db.SDbConfig moConfig;
    private java.util.ArrayList<int[]> manPayrollEmployeeReceipts;
    private erp.mhrs.form.SDialogPayrollCfdiPicker moPayrollCfdiPicker;

    /** Creates new form SDialogPayrollCfdi
     * @param client
     * @param hrsPayrollEmployeeReceipt */
    public SDialogPayrollCfdi(erp.client.SClientInterface client, ArrayList<SHrsPayrollEmployeeReceipt> hrsPayrollEmployeeReceipt) {
        super(client.getFrame(), true);
        miClient = client;
        maHrsPayrollEmployeeReceipt = hrsPayrollEmployeeReceipt;
        manPayrollEmployeeReceipts = new ArrayList<>();
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
        jPanel6 = new javax.swing.JPanel();
        jlPayrollNotes = new javax.swing.JLabel();
        jtfPayrollNotes = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jpAccountingRecord = new javax.swing.JPanel();
        jlDateIssue = new javax.swing.JLabel();
        jtfDateIssue = new javax.swing.JFormattedTextField();
        jbDateIssue = new javax.swing.JButton();
        jpPaymentType = new javax.swing.JPanel();
        jlDatePayment = new javax.swing.JLabel();
        jtfDatePayment = new javax.swing.JFormattedTextField();
        jbDatePayment = new javax.swing.JButton();
        jlCfdiRelatedUuid = new javax.swing.JLabel();
        jtfCfdiRelatedUuid = new javax.swing.JTextField();
        jbCfdiRelatedPick = new javax.swing.JButton();
        jlCfdiRelatedHint = new javax.swing.JLabel();
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
        setTitle("Generación de CFDI de nóminas");
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

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalles de la generación y timbrado de CFDI de la nómina:"));
        jPanel4.setLayout(new java.awt.BorderLayout(5, 5));

        jPanel8.setLayout(new java.awt.GridLayout(2, 0, 0, 5));

        jpAccountingRecord.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateIssue.setText("Fecha emisión:*");
        jlDateIssue.setPreferredSize(new java.awt.Dimension(100, 23));
        jpAccountingRecord.add(jlDateIssue);

        jtfDateIssue.setText("dd/mm/yyyy");
        jtfDateIssue.setPreferredSize(new java.awt.Dimension(75, 23));
        jpAccountingRecord.add(jtfDateIssue);

        jbDateIssue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateIssue.setToolTipText("Seleccionar fecha");
        jbDateIssue.setFocusable(false);
        jbDateIssue.setPreferredSize(new java.awt.Dimension(23, 23));
        jpAccountingRecord.add(jbDateIssue);

        jPanel8.add(jpAccountingRecord);

        jpPaymentType.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDatePayment.setText("Fecha pago:*");
        jlDatePayment.setPreferredSize(new java.awt.Dimension(100, 23));
        jpPaymentType.add(jlDatePayment);

        jtfDatePayment.setText("dd/mm/yyyy");
        jtfDatePayment.setPreferredSize(new java.awt.Dimension(75, 23));
        jpPaymentType.add(jtfDatePayment);

        jbDatePayment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDatePayment.setToolTipText("Seleccionar fecha");
        jbDatePayment.setFocusable(false);
        jbDatePayment.setPreferredSize(new java.awt.Dimension(23, 23));
        jpPaymentType.add(jbDatePayment);

        jlCfdiRelatedUuid.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlCfdiRelatedUuid.setText("CFDI relacionado:");
        jlCfdiRelatedUuid.setPreferredSize(new java.awt.Dimension(125, 23));
        jpPaymentType.add(jlCfdiRelatedUuid);

        jtfCfdiRelatedUuid.setText("402A8A38-B980-412A-9485-29222D7095C4");
        jtfCfdiRelatedUuid.setToolTipText("UUID");
        jtfCfdiRelatedUuid.setPreferredSize(new java.awt.Dimension(250, 23));
        jpPaymentType.add(jtfCfdiRelatedUuid);

        jbCfdiRelatedPick.setText("...");
        jbCfdiRelatedPick.setToolTipText("Seleccionar CFDI relacionado");
        jbCfdiRelatedPick.setPreferredSize(new java.awt.Dimension(23, 23));
        jpPaymentType.add(jbCfdiRelatedPick);

        jlCfdiRelatedHint.setForeground(java.awt.Color.gray);
        jlCfdiRelatedHint.setText("(Para la sustitución de CFDI previos)");
        jlCfdiRelatedHint.setPreferredSize(new java.awt.Dimension(200, 23));
        jpPaymentType.add(jlCfdiRelatedHint);

        jPanel8.add(jpPaymentType);

        jPanel4.add(jPanel8, java.awt.BorderLayout.NORTH);

        jpEmployeesAvailable.setBorder(javax.swing.BorderFactory.createTitledBorder("Recibos disponibles:"));
        jpEmployeesAvailable.setPreferredSize(new java.awt.Dimension(350, 100));
        jpEmployeesAvailable.setLayout(new java.awt.BorderLayout());

        jlTotalAvailables.setText("n");
        jlTotalAvailables.setPreferredSize(new java.awt.Dimension(100, 20));
        jpEmployeesAvailable.add(jlTotalAvailables, java.awt.BorderLayout.SOUTH);

        jPanel4.add(jpEmployeesAvailable, java.awt.BorderLayout.LINE_START);

        jpEmployeesSelected.setBorder(javax.swing.BorderFactory.createTitledBorder("Recibos seleccionados:"));
        jpEmployeesSelected.setPreferredSize(new java.awt.Dimension(475, 100));
        jpEmployeesSelected.setLayout(new java.awt.BorderLayout());

        jlTotalSelected.setText("n");
        jlTotalSelected.setPreferredSize(new java.awt.Dimension(100, 20));
        jpEmployeesSelected.add(jlTotalSelected, java.awt.BorderLayout.PAGE_END);

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

        setSize(new java.awt.Dimension(976, 638));
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

        mvFields = new Vector<>();
        moConfig = (SDbConfig) miClient.getSession().readRegistry(SModConsts.HRS_CFG, new int[] { SUtilConsts.BPR_CO_ID });

        moFieldDateIssue = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jtfDateIssue, jlDateIssue);
        moFieldDateIssue.setPickerButton(jbDateIssue);
        moFieldDatePayment = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jtfDatePayment, jlDatePayment);
        moFieldDatePayment.setPickerButton(jbDatePayment);
        moFieldCfdiRelatedUuid = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfCfdiRelatedUuid, jlCfdiRelatedUuid);

        mvFields.add(moFieldDateIssue);
        mvFields.add(moFieldDatePayment);
        mvFields.add(moFieldCfdiRelatedUuid);

        moTablePaneReceiptAvailable = new STablePaneGrid(miClient);
        moTablePaneReceiptAvailable.setDoubleClickAction(this, "actionAdd");
        jpEmployeesAvailable.add(moTablePaneReceiptAvailable, BorderLayout.CENTER);

        moTablePaneReceiptSelected = new STablePaneGrid(miClient);
        moTablePaneReceiptSelected.setDoubleClickAction(this, "actionRemove");
        jpEmployeesSelected.add(moTablePaneReceiptSelected, BorderLayout.CENTER);

        i = 0;
        aoTableColumns = new STableColumnForm[9];
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Empleado", 150);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Clave", 75);
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Total percepciones $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Total deducciones $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Total neto $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Número serie", 75);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE, "Fecha emisión", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE, "Fecha pago", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Método pago", 100);
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePaneReceiptAvailable.addTableColumn(aoTableColumns[i]);
        }

        i = 0;
        aoTableColumns = new STableColumnForm[10];
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Empleado", 150);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Clave", 75);
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Total percepciones $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Total deducciones $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Total neto $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Número serie", 75);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE, "Fecha emisión", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE, "Fecha pago", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Método pago", 100);
        aoTableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "CFDI relacionado", 150);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePaneReceiptSelected.addTableColumn(aoTableColumns[i]);
        }

        jbAdd.addActionListener(this);
        jbAddAll.addActionListener(this);
        jbRemove.addActionListener(this);
        jbRemoveAll.addActionListener(this);
        jbDateIssue.addActionListener(this);
        jbDatePayment.addActionListener(this);
        jbCfdiRelatedPick.addActionListener(this);
        
        moPayrollCfdiPicker = new SDialogPayrollCfdiPicker(miClient);

        SFormUtilities.createActionMap(rootPane, this, "actionOk", "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionCancel", "cancel", KeyEvent.VK_ESCAPE, 0);
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
                    if (maHrsPayrollEmployeeReceipt != null) {
                        populatePayroll();
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void computeTotals() {
        jlTotalAvailables.setText("Recibos disponibles: " + moTablePaneReceiptAvailable.getTableGuiRowCount());
        jlTotalSelected.setText("Recibos seleccionados: " + moTablePaneReceiptSelected.getTableGuiRowCount());
    }
    
    private void actionDateIssue() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateIssue.getDate(), moFieldDateIssue);
    }

    private void actionDatePayment() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDatePayment.getDate(), moFieldDatePayment);
    }

    private void actionCfdiRelatedPick() {
        int index = moTablePaneReceiptAvailable.getTable().getSelectedRow();

        if (index != -1) {
            SHrsPayrollEmployeeReceipt employeeReceipt = (SHrsPayrollEmployeeReceipt) moTablePaneReceiptAvailable.getSelectedTableRow();
            
            moPayrollCfdiPicker.formReset();
            moPayrollCfdiPicker.setFilterKey(new Object[] { employeeReceipt.getPkEmployeeId(), SDataConstantsSys.TRNS_TP_CFD_PAYROLL, SDataConstantsSys.TRNS_ST_DPS_ANNULED });
            moPayrollCfdiPicker.formRefreshOptionPane();
            moPayrollCfdiPicker.setFormVisible(true);

            if (moPayrollCfdiPicker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_CFD, moPayrollCfdiPicker.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
                jtfCfdiRelatedUuid.setText(cfd.getUuid());
                jtfCfdiRelatedUuid.setCaretPosition(0);
                jtfCfdiRelatedUuid.requestFocusInWindow();
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private void populatePayroll() {
        ArrayList<SHrsPayrollEmployeeReceipt> rowsSelected = null;
        
        if (!maHrsPayrollEmployeeReceipt.isEmpty()) {
            jtfPayrollPeriod.setText(maHrsPayrollEmployeeReceipt.get(0).getPeriodYear() + "-" + (maHrsPayrollEmployeeReceipt.get(0).getPeriod() >= 10 ? "" : "0" ) + maHrsPayrollEmployeeReceipt.get(0).getPeriod());
            jtfPayrollNumber.setText((maHrsPayrollEmployeeReceipt.get(0).getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_WEE ? "SEM " : "QNA " ) + maHrsPayrollEmployeeReceipt.get(0).getPayrollNumber());
            jtfPayrollDates.setText(miClient.getSessionXXX().getFormatters().getDateFormat().format(maHrsPayrollEmployeeReceipt.get(0).getDateStart()) + " - " +
                    miClient.getSessionXXX().getFormatters().getDateFormat().format(maHrsPayrollEmployeeReceipt.get(0).getDateEnd()));
            jtfPayrollNotes.setText(maHrsPayrollEmployeeReceipt.get(0).getNotes());
            
            jtfPayrollPeriod.setCaretPosition(0);
            jtfPayrollNumber.setCaretPosition(0);
            jtfPayrollDates.setCaretPosition(0);
            jtfPayrollNotes.setCaretPosition(0);

            try {
                rowsSelected = new ArrayList<>();
                
                for (SHrsPayrollEmployeeReceipt row : maHrsPayrollEmployeeReceipt) {
                    row.prepareTableRow();
                    Object field = SDataReadDescriptions.getField(miClient, SDataConstants.TRNU_TP_PAY_SYS, new int[] { row.getPaymentTypeSysId() }, SLibConstants.FIELD_TYPE_TEXT);

                    row.setPaymentTypeSys((String) field);
                    if (row.getPaymentTypeSysId() == SDataConstantsSys.TRNU_TP_PAY_SYS_NA) {
                        moTablePaneReceiptAvailable.addTableRow(row);   // receipt has not been added yet (2019-03-12, Sergio Flores: WTF! A bizarre solution!)
                    }
                    else {
                        rowsSelected.add(row);                          // receipt had been added already
                    }
                }
                moTablePaneReceiptAvailable.renderTableRows();
                moTablePaneReceiptAvailable.setTableRowSelection(0);

                if (!rowsSelected.isEmpty()) {
                    for (SHrsPayrollEmployeeReceipt row : rowsSelected) {
                        row.prepareTableRow();
                        moTablePaneReceiptSelected.addTableRow(row);
                    }
                    moTablePaneReceiptSelected.renderTableRows();
                    moTablePaneReceiptSelected.setTableRowSelection(moTablePaneReceiptSelected.getTableGuiRowCount() - 1);
                }
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
        computeTotals();
    }
    
    @SuppressWarnings("unchecked")
    private void computePayroll() throws java.lang.Exception {
        SDbPayrollReceiptIssue receiptIssue = new SDbPayrollReceiptIssue();
        
        for (STableRow row : moTablePaneReceiptSelected.getGridRows()) {
            SHrsPayrollEmployeeReceipt receipt = (SHrsPayrollEmployeeReceipt) row;
            int[] receiptKey = receipt.getRowPrimaryKey();
            
            receiptIssue.saveField(miClient.getSession().getStatement(), receiptKey, SDbPayrollReceiptIssue.FIELD_NUMBER_SERIES, receipt.getNumberSeries());
            receiptIssue.saveField(miClient.getSession().getStatement(), receiptKey, SDbPayrollReceiptIssue.FIELD_DATE_ISSUE, receipt.getDateIssue());
            receiptIssue.saveField(miClient.getSession().getStatement(), receiptKey, SDbPayrollReceiptIssue.FIELD_DATE_PAYMENT, receipt.getDatePayment());
            receiptIssue.saveField(miClient.getSession().getStatement(), receiptKey, SDbPayrollReceiptIssue.FIELD_TYPE_PAYMENT_SYS, receipt.getPaymentTypeSysId());
            receiptIssue.saveField(miClient.getSession().getStatement(), receiptKey, SDbPayrollReceiptIssue.FIELD_TYPE_UUID_RELATED, receipt.getUuidToSubstitute());
            manPayrollEmployeeReceipts.add(receiptKey);
        }
    }

    /**
     * Adds a recipt to be emitted.
     * Please note that must be public in order to be invoked by double-clicking grid of available receipts.
     * @return 
     */
    public boolean actionAdd() {
        boolean error = true;
        SHrsPayrollEmployeeReceipt row = null;

        if (moFieldDateIssue.getDate() == null) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDateIssue.getText() + "'.");
            jtfDateIssue.requestFocusInWindow();
        }
        else if (moFieldDatePayment.getDate() == null) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDatePayment.getText() + "'.");
            jtfDatePayment.requestFocusInWindow();
        }
        else if (!moFieldCfdiRelatedUuid.getString().isEmpty() && moFieldCfdiRelatedUuid.getString().length() != DCfdVer3Consts.LEN_UUID) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlCfdiRelatedUuid.getText() + "':\n"
                    + "debe ser de " + DCfdVer3Consts.LEN_UUID + " caracteres.");
            jtfCfdiRelatedUuid.requestFocusInWindow();
        }
        else {
            int index = moTablePaneReceiptAvailable.getTable().getSelectedRow();
            
            if (index != -1) {
                int xmlType = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdTypeXmlTypes().get(SDataConstantsSys.TRNS_TP_CFD_PAYROLL);
                int paymentType = 0;

                if (xmlType == SDataConstantsSys.TRNS_TP_XML_CFDI_32) {
                    paymentType = SDataConstantsSys.TRNU_TP_PAY_SYS_NA;
                }
                else if (xmlType == SDataConstantsSys.TRNS_TP_XML_CFDI_33) {
                    paymentType = SDataConstantsSys.TRNU_TP_PAY_SYS_OTHER;
                }
                
                Object field = SDataReadDescriptions.getField(miClient, SDataConstants.TRNU_TP_PAY_SYS, new int[] { paymentType }, SLibConstants.FIELD_TYPE_TEXT);
                
                row = (SHrsPayrollEmployeeReceipt) moTablePaneReceiptAvailable.getSelectedTableRow();
                row.setNumberSeries(moConfig.getNumberSeries());
                row.setDateIssue(moFieldDateIssue.getDate());
                row.setDatePayment(moFieldDatePayment.getDate());
                row.setPaymentTypeSysId(paymentType);
                row.setPaymentTypeSys((String) field);
                row.setUuidToSubstitute(moFieldCfdiRelatedUuid.getString());
                row.prepareTableRow();

                moTablePaneReceiptAvailable.removeTableRow(index);
                moTablePaneReceiptAvailable.renderTableRows();
                moTablePaneReceiptAvailable.setTableRowSelection(index < moTablePaneReceiptAvailable.getTableGuiRowCount() ? index : moTablePaneReceiptAvailable.getTableGuiRowCount() - 1);

                moTablePaneReceiptSelected.addTableRow(row);
                moTablePaneReceiptSelected.renderTableRows();
                moTablePaneReceiptSelected.setTableRowSelection(moTablePaneReceiptSelected.getTableGuiRowCount() - 1);

                error = false;
            }
            
            computeTotals();
        }

        return !error;
    }

    /**
     * Adds all recipts to be emitted.
     * @return 
     */
    private void actionAddAll() {
        while (moTablePaneReceiptAvailable.getTableGuiRowCount() > 0) {
            moTablePaneReceiptAvailable.setTableRowSelection(0);
            if (!actionAdd()) {
                break;
            }
        }
    }

    /**
     * Removes a recipt about to be emitted.
     * Please note that must be public in order to be invoked by double-clicking grid of selected receipts.
     * @return 
     */
    public boolean actionRemove() {
        boolean error = true;
        SHrsPayrollEmployeeReceipt row = null;

        int index = moTablePaneReceiptSelected.getTable().getSelectedRow();
        
        if (index != -1) {
            row = (SHrsPayrollEmployeeReceipt) moTablePaneReceiptSelected.getSelectedTableRow();
            for (int i = 1; i <= 5; i++) {
                row.getValues().remove(4);
            }

            moTablePaneReceiptSelected.removeTableRow(index);
            moTablePaneReceiptSelected.renderTableRows();
            moTablePaneReceiptSelected.setTableRowSelection(index < moTablePaneReceiptSelected.getTableGuiRowCount() ? index : moTablePaneReceiptSelected.getTableGuiRowCount() - 1);

            row.prepareTableRow();
            moTablePaneReceiptAvailable.addTableRow(row);
            moTablePaneReceiptAvailable.renderTableRows();
            moTablePaneReceiptAvailable.setTableRowSelection(moTablePaneReceiptAvailable.getTableGuiRowCount() - 1);

            error = false;
        }
        
        computeTotals();

        return !error;
    }

    /**
     * Removes all recipts about to be emitted.
     * @return 
     */
    private void actionRemoveAll() {
        while (moTablePaneReceiptSelected.getTableGuiRowCount() > 0) {
            moTablePaneReceiptSelected.setTableRowSelection(0);
            if (!actionRemove()) {
                break;
            }
        }
    }
    
    public void actionOk() {
        Cursor cursor = null;

        if (moTablePaneReceiptSelected.getTableGuiRowCount() == 0) {
            miClient.showMsgBoxWarning("No hay empleados seleccionados.");
            moTablePaneReceiptAvailable.requestFocusInWindow();
        }
        else {
            try {
                cursor = miClient.getFrame().getCursor();
                miClient.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));

                computePayroll();

                mnFormResult = SLibConstants.FORM_RESULT_OK;
                setVisible(false);
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
            finally {
                miClient.getFrame().setCursor(cursor);
            }
        }
    }

    public void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbAddAll;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbCfdiRelatedPick;
    private javax.swing.JButton jbDateIssue;
    private javax.swing.JButton jbDatePayment;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbRemove;
    private javax.swing.JButton jbRemoveAll;
    private javax.swing.JLabel jlCfdiRelatedHint;
    private javax.swing.JLabel jlCfdiRelatedUuid;
    private javax.swing.JLabel jlDateIssue;
    private javax.swing.JLabel jlDatePayment;
    private javax.swing.JLabel jlDummy01;
    private javax.swing.JLabel jlPayroll;
    private javax.swing.JLabel jlPayrollDates;
    private javax.swing.JLabel jlPayrollNotes;
    private javax.swing.JLabel jlTotalAvailables;
    private javax.swing.JLabel jlTotalSelected;
    private javax.swing.JPanel jpAccountingRecord;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpEmployeesAvailable;
    private javax.swing.JPanel jpEmployeesSelected;
    private javax.swing.JPanel jpGrid;
    private javax.swing.JPanel jpPaymentType;
    private javax.swing.JTextField jtfCfdiRelatedUuid;
    private javax.swing.JFormattedTextField jtfDateIssue;
    private javax.swing.JFormattedTextField jtfDatePayment;
    private javax.swing.JTextField jtfPayrollDates;
    private javax.swing.JTextField jtfPayrollNotes;
    private javax.swing.JTextField jtfPayrollNumber;
    private javax.swing.JTextField jtfPayrollPeriod;
    // End of variables declaration//GEN-END:variables

    public void resetForm() {
        mnFormResult = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        jtfPayrollPeriod.setText("");
        jtfPayrollNumber.setText("");
        jtfPayrollDates.setText("");
        jtfPayrollNotes.setText("");

        moFieldDateIssue.setDate(miClient.getSession().getCurrentDate());
        moFieldDatePayment.setDate(null);
        moFieldCfdiRelatedUuid.setString("");

        moTablePaneReceiptAvailable.createTable(this);
        moTablePaneReceiptAvailable.clearTableRows();
        moTablePaneReceiptSelected.createTable();
        moTablePaneReceiptSelected.clearTableRows();
    }

    public int getFormResult() {
        return mnFormResult;
    }

    public ArrayList<int[]> getPayrollEmployeeReceipts() {
        return manPayrollEmployeeReceipts;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbAdd) {
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
            else if (button == jbDateIssue) {
                actionDateIssue();
            }
            else if (button == jbDatePayment) {
                actionDatePayment();
            }
            else if (button == jbCfdiRelatedPick) {
                actionCfdiRelatedPick();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() instanceof DefaultListSelectionModel) {
            jtfCfdiRelatedUuid.setText("");
        }
    }
}

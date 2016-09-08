/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbEarning;
import erp.mod.hrs.db.SDbPayrollReceiptEarning;
import erp.mod.hrs.db.SHrsPayrollReceipt;
import erp.mod.hrs.db.SHrsPayrollReceiptEarning;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.grid.SGridPaneFormOwner;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiItem;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFormDialog;

/**
 *
 * @author Juan Barajas
 */
public class SDialogPayrollEarnings extends SBeanFormDialog implements SGridPaneFormOwner, ItemListener, ActionListener, CellEditorListener, KeyListener {

    protected static final int COL_BAL_ALL = 1;
    protected static final int COL_BAL = 4;
    protected static final int COL_APP = 7;
    
    protected ArrayList<SHrsPayrollReceipt> maReceipts;
    protected HashMap<Integer, SDbEarning> moEarnigs;
    protected SGridPaneForm moGridEmployeeRow;

    /**
     * Creates new form SDialogPayrollEarnings
     */
    public SDialogPayrollEarnings(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.HRSX_PAY_REC_EAR, SLibConsts.UNDEFINED, title);
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jlEarning = new javax.swing.JLabel();
        moKeyEarning = new sa.lib.gui.bean.SBeanFieldKey();
        jpEmployee = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlValue = new javax.swing.JLabel();
        moComEarningValue = new sa.lib.gui.bean.SBeanCompoundField();
        jbEarningAdd = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jbClean = new javax.swing.JButton();
        jbCleanAll = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlEarning.setText("Percepción:");
        jlEarning.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel12.add(jlEarning);

        moKeyEarning.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel12.add(moKeyEarning);

        jPanel1.add(jPanel12, java.awt.BorderLayout.NORTH);

        jpEmployee.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlValue.setText("Valor:");
        jlValue.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jlValue);

        moComEarningValue.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel2.add(moComEarningValue);

        jbEarningAdd.setText("Agregar");
        jPanel2.add(jbEarningAdd);

        jPanel4.add(jPanel2, java.awt.BorderLayout.WEST);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbClean.setText("Limpiar");
        jbClean.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbClean.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel3.add(jbClean);

        jbCleanAll.setText("Limpiar todo");
        jbCleanAll.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbCleanAll.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel3.add(jbCleanAll);

        jPanel4.add(jPanel3, java.awt.BorderLayout.EAST);

        jpEmployee.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel1.add(jpEmployee, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton jbClean;
    private javax.swing.JButton jbCleanAll;
    private javax.swing.JButton jbEarningAdd;
    private javax.swing.JLabel jlEarning;
    private javax.swing.JLabel jlValue;
    private javax.swing.JPanel jpEmployee;
    private sa.lib.gui.bean.SBeanCompoundField moComEarningValue;
    private sa.lib.gui.bean.SBeanFieldKey moKeyEarning;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 560, 350);
        
        jbSave.setText("Aceptar");

        moKeyEarning.setKeySettings(miClient, SGuiUtils.getLabelName(jlEarning.getText()), false);
        moComEarningValue.setCompoundFieldSettings(miClient);
        moComEarningValue.getField().setDecimalSettings("Monto:", SGuiConsts.GUI_TYPE_DEC_QTY, false);
        moComEarningValue.getField().setValue(0d);
        moComEarningValue.setCompoundText("");
        
        moFields.addField(moKeyEarning);
        
        moFields.setFormButton(jbSave);
        
        moGridEmployeeRow = new SGridPaneForm(miClient, SModConsts.HRSX_PAY_REC_EAR, SLibConsts.UNDEFINED, "Empleados") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false, false, false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                SGridColumnForm columnForm = null;
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<SGridColumnForm>();

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "Empleado"));
                columnForm = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Valor", moGridEmployeeRow.getTable().getDefaultEditor(Double.class));
                columnForm.setEditable(true);
                gridColumnsForm.add(columnForm);
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT_UNIT, "Valor unitario $"));
                columnForm = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Monto $", moGridEmployeeRow.getTable().getDefaultEditor(Double.class));
                columnForm.setEditable(true);
                gridColumnsForm.add(columnForm);
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Valor ajustado"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad"));
                columnForm = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Pago", moGridEmployeeRow.getTable().getDefaultEditor(Boolean.class));
                columnForm.setEditable(true);
                gridColumnsForm.add(columnForm);
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Crédito/Préstamo"));
                
                moGridEmployeeRow.getTable().getDefaultEditor(Boolean.class).addCellEditorListener(SDialogPayrollEarnings.this);
                moGridEmployeeRow.getTable().getDefaultEditor(Double.class).addCellEditorListener(SDialogPayrollEarnings.this);
                
                return gridColumnsForm;
            }
        };

        moGridEmployeeRow.setForm(null);
        moGridEmployeeRow.setPaneFormOwner(this);
        jpEmployee.add(moGridEmployeeRow, BorderLayout.CENTER);
        //mvFormGrids.add(moGridEmployeeRow);
        
        reloadCatalogues();
        addAllListeners();
        
        enableFields(false);
    }
    
    private void enableFields(final boolean enable) {
        moComEarningValue.getField().setEditable(enable);
        jbEarningAdd.setEnabled(enable);
    }
    
    private void validateCellEditor() {
        if (moKeyEarning.getValue()[0] > 0) {
            if (!(moEarnigs.get(moKeyEarning.getValue()[0]).getFkEarningComputationTypeId() != SModSysConsts.HRSS_TP_EAR_COMP_AMT &&
                            (moEarnigs.get(moKeyEarning.getValue()[0]).getFkAbsenceClassId_n() == SLibConsts.UNDEFINED && moEarnigs.get(moKeyEarning.getValue()[0]).getFkAbsenceTypeId_n() == SLibConsts.UNDEFINED))) {
                miClient.showMsgBoxWarning("No se puede modificar el campo 'Valor' para la percepción '" + moEarnigs.get(moKeyEarning.getValue()[0]).getName() + "', solo el campo 'Monto $'");
            }
            
        }
    }
    
    private void processEditingAppPayment() {
        itemStateEarning();
        moGridEmployeeRow.renderGridRows();
        moGridEmployeeRow.setSelectedGridRow(0);
    }
    
    private SDbPayrollReceiptEarning createReceipEarning(SHrsPayrollReceipt payrollReceipt, SHrsPayrollReceiptEarning row) {
        double amount = 0;
        SDbPayrollReceiptEarning receiptEarning = null;
        
        receiptEarning = new SDbPayrollReceiptEarning();
        
        receiptEarning.setPkPayrollId(payrollReceipt.getReceipt().getPkPayrollId());
        receiptEarning.setPkEmployeeId(payrollReceipt.getHrsEmployee().getEmployee().getPkEmployeeId());
        receiptEarning.setPkMoveId(row.getPkMoveId());
        receiptEarning.setAutomatic(false);
        receiptEarning.setAlternativeTaxCalculation(row.getEarning().isAlternativeTaxCalculation());// XXX (jbarajas, 2016-04-06) articule 174 RLISR
        
        // Unit:
        
        if (row.getEarning().getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT) {
            receiptEarning.setUnitsAlleged(1d);
            receiptEarning.setUnits(1d);
        }
        else {
            receiptEarning.setUnitsAlleged(row.getXtaValueAlleged());
            receiptEarning.setUnits(row.getXtaValue());
        }
        
        // Amount unitary:
        
        if (row.getEarning().getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT) {
            receiptEarning.setAmountUnitary(row.getXtaValue());
        }
        else if (row.getEarning().getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_DAY) {
            receiptEarning.setAmountUnitary(payrollReceipt.getReceipt().getPaymentDaily());
        }
        else if (row.getEarning().getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_HRS) {
            receiptEarning.setAmountUnitary(payrollReceipt.getReceipt().getPaymentHourly());
        }
        else if (row.getEarning().getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_PER_DAY) {
            receiptEarning.setAmountUnitary(payrollReceipt.getReceipt().getPaymentDaily() * row.getEarning().getPayPercentage());
        }
        else if (row.getEarning().getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_PER_HRS) {
            receiptEarning.setAmountUnitary(payrollReceipt.getReceipt().getPaymentHourly() * row.getEarning().getPayPercentage());
        }
        receiptEarning.setFactorAmount(1);
        
        amount = SLibUtils.round((receiptEarning.getUnits() * receiptEarning.getAmountUnitary() * row.getEarning().getUnitsFactor()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
        receiptEarning.setAmountSystem_r(amount);
        
        receiptEarning.setAmount_r(amount);
        receiptEarning.setFkEarningTypeId(row.getEarning().getFkEarningTypeId());
        receiptEarning.setFkEarningId(row.getEarning().getPkEarningId());
        receiptEarning.setFkBenefitTypeId(row.getEarning().getFkBenefitTypeId());
        
        return receiptEarning;
    }
    
    private void initEmployee() {
        SHrsPayrollReceiptEarning hrsReceiptEarningRow = null;
        
        for (SGridRow row : moGridEmployeeRow.getModel().getGridRows()) { // read grid
            hrsReceiptEarningRow = (SHrsPayrollReceiptEarning) row;
            
            hrsReceiptEarningRow.setXtaValueAlleged(0d);
            hrsReceiptEarningRow.setXtaValue(0d);
            hrsReceiptEarningRow.setPayment(false);
            if (hrsReceiptEarningRow.getEarning().getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT) {
                hrsReceiptEarningRow.getReceiptEarning().setAmountUnitary(0d);
            }
            else {
                hrsReceiptEarningRow.getReceiptEarning().setUnitsAlleged(0d);
                hrsReceiptEarningRow.getReceiptEarning().setUnits(0d);
            }
            hrsReceiptEarningRow.getReceiptEarning().setAmountSystem_r(0d);
            hrsReceiptEarningRow.getReceiptEarning().setAmount_r(0d);
            // hrsReceiptEarningRow.computeEarning(); XXX jbarajas 15/04/2015
                
            hrsReceiptEarningRow.getReceiptEarning().setFkLoanEmployeeId_n(SLibConsts.UNDEFINED);
            hrsReceiptEarningRow.getReceiptEarning().setFkLoanLoanId_n(SLibConsts.UNDEFINED);
            hrsReceiptEarningRow.getReceiptEarning().setFkLoanTypeId_n(SLibConsts.UNDEFINED);
        }
    }
    
    private void updateReceiptsEarningRows(final boolean addAll) {
        double amount = 0;
        boolean found = false;
        SHrsPayrollReceiptEarning hrsReceiptEarningRow = null;
        
        for (SGridRow row : moGridEmployeeRow.getModel().getGridRows()) { // read grid
            hrsReceiptEarningRow = (SHrsPayrollReceiptEarning) row;
            
            hrsReceiptEarningRow.setEarning(moEarnigs.get(hrsReceiptEarningRow.getRowPrimaryKey()[0]));
            
            /* Unnecessary upgrade can not change the loan
            if (hrsReceiptEarning.getReceiptEarning() != null && hrsReceiptEarning.getReceiptEarning().getFkLoanEmployeeId_n() != SLibConsts.UNDEFINED) {
                hrsReceiptEarning.setXtaLoan(hrsReceiptEarning.getHrsReceipt().getHrsEmployee().getLoanIdentificator(hrsReceiptEarning.getReceiptEarning().getFkLoanLoanId_n()).getLoanIdentificator());
            }
            */
            
            for (SHrsPayrollReceipt hrsReceipt : maReceipts) { // read receipt
                found = false;
                
                if (addAll) {
                    hrsReceiptEarningRow.setXtaValueAlleged(moComEarningValue.getField().getValue());
                    hrsReceiptEarningRow.setXtaValue(!hrsReceiptEarningRow.getEarning().isDaysAdjustment() ? moComEarningValue.getField().getValue() * hrsReceipt.getHrsEmployee().getEmployeeDays().getFactorCalendar() : (moComEarningValue.getField().getValue() * hrsReceipt.getHrsEmployee().getEmployeeDays().getFactorCalendar() * hrsReceipt.getHrsEmployee().getEmployeeDays().getFactorDaysPaid()));
                }
                
                if (SLibUtils.compareKeys(new int[] { hrsReceipt.getHrsEmployee().getEmployee().getPkEmployeeId() }, new int[] { hrsReceiptEarningRow.getHrsReceipt().getHrsEmployee().getEmployee().getPkEmployeeId() })) { // recibo del empleado en el grid
                    hrsReceiptEarningRow.setHrsReceipt(hrsReceipt);

                    for (SHrsPayrollReceiptEarning earningRow : hrsReceipt.getHrsEarnings()) { // read array list ear/ded
                        found = true;

                        if (SLibUtils.compareKeys(earningRow.getRowPrimaryKey(), hrsReceiptEarningRow.getRowPrimaryKey())) {  // exists ear/ded
                            earningRow.setXtaValueAlleged(hrsReceiptEarningRow.getXtaValueAlleged());  // update value alleged
                            earningRow.setXtaValue(hrsReceiptEarningRow.getXtaValue());  // update value
                            earningRow.setPayment(hrsReceiptEarningRow.isPayment());
                            
                            if (addAll) {
                                if (hrsReceiptEarningRow.getEarning().getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT) {
                                    earningRow.getReceiptEarning().setAmountUnitary(hrsReceiptEarningRow.getXtaValue());
                                }
                                else {
                                    earningRow.getReceiptEarning().setUnitsAlleged(hrsReceiptEarningRow.getXtaValueAlleged());
                                    earningRow.getReceiptEarning().setUnits(hrsReceiptEarningRow.getXtaValue());
                                }
                                amount = SLibUtils.round((earningRow.getReceiptEarning().getUnits() * earningRow.getReceiptEarning().getAmountUnitary() * earningRow.getEarning().getUnitsFactor()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
                                earningRow.getReceiptEarning().setAmountSystem_r(amount);

                                earningRow.getReceiptEarning().setAmount_r(amount);
                            }

                            hrsReceipt.replaceEarning(earningRow.getPkMoveId(), hrsReceiptEarningRow);

                            if (!earningRow.getReceiptEarning().isAutomatic() && earningRow.getXtaValue() == 0) { 
                                hrsReceipt.removeEarning(earningRow.getPkMoveId());
                            }
                            break;
                        }
                        else {
                            found = false;
                        }
                    }

                    if (!found) { // not exists
                        if ((hrsReceiptEarningRow.getEarning().getFkEarningComputationTypeId() != SModSysConsts.HRSS_TP_EAR_COMP_AMT &&
                                hrsReceiptEarningRow.getXtaValueAlleged() != 0) ||
                                (hrsReceiptEarningRow.getEarning().getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT &&
                                (addAll ? hrsReceiptEarningRow.getXtaValue() != 0 : hrsReceiptEarningRow.getReceiptEarning().getAmountUnitary() != 0))) {  // not is 0 o nulo
                            if (addAll) {
                                hrsReceiptEarningRow.setReceiptEarning(createReceipEarning(hrsReceipt, hrsReceiptEarningRow));
                            }
                            hrsReceipt.addEarning(hrsReceiptEarningRow); // add ear/ded to array list
                        }
                    }
                }
            }
        }
    }
    
    private void itemStateEarning() {
        boolean found = false;
        SHrsPayrollReceiptEarning hrsReceiptEarningRow = null;
        Vector<SGridRow> rows = new Vector<SGridRow>();
        
        updateReceiptsEarningRows(false);

        moGridEmployeeRow.getModel().clearGridRows();
        moGridEmployeeRow.getModel().clearGrid();

        if (moKeyEarning.getValue()[0] > 0) { // read ear/ded
            moComEarningValue.setCompoundText((String) miClient.getSession().readField(SModConsts.HRSS_TP_EAR_COMP, new int[] { (int) moKeyEarning.getSelectedItem().getComplement() }, SDbRegistry.FIELD_CODE));
            
            for (SHrsPayrollReceipt hrsReceipt : maReceipts) { // read receipt
                found = false;

                for (SHrsPayrollReceiptEarning earning : hrsReceipt.getHrsEarnings()) { // read array list ear/ded
                    if (SLibUtils.compareKeys(earning.getHrsReceipt().getHrsEmployee().getEmployee().getPrimaryKey(), hrsReceipt.getHrsEmployee().getEmployee().getPrimaryKey())) {
                        if (SLibUtils.compareKeys(new int[] { earning.getEarning().getPkEarningId() }, new int[] { moKeyEarning.getValue()[0] })) {  // exists ear/ded
                            hrsReceiptEarningRow = new SHrsPayrollReceiptEarning();

                            hrsReceiptEarningRow.setEarning(earning.getEarning());
                            hrsReceiptEarningRow.setHrsReceipt(hrsReceipt);
                            hrsReceiptEarningRow.setReceiptEarning(earning.getReceiptEarning());
                            hrsReceiptEarningRow.setRowType(SHrsPayrollReceiptEarning.BY_EAR);
                            hrsReceiptEarningRow.setPkMoveId(earning.getPkMoveId());
                            hrsReceiptEarningRow.setXtaEmployee(hrsReceipt.getHrsEmployee().getEmployee().getAuxEmployee());
                            hrsReceiptEarningRow.setXtaValueAlleged(earning.getReceiptEarning().getUnitsAlleged());
                            hrsReceiptEarningRow.setXtaValue(earning.getReceiptEarning().getUnits());
                            hrsReceiptEarningRow.setXtaUnit((String) miClient.getSession().readField(SModConsts.HRSS_TP_EAR_COMP, new int[] { earning.getEarning().getFkEarningComputationTypeId() }, SDbRegistry.FIELD_CODE));

                            if (earning.getReceiptEarning() != null && earning.getReceiptEarning().getFkLoanEmployeeId_n() != SLibConsts.UNDEFINED) {
                                hrsReceiptEarningRow.setXtaLoan(earning.getHrsReceipt().getHrsEmployee().getLoan(earning.getReceiptEarning().getFkLoanLoanId_n()).getLoanIdentificator());
                            }
                            rows.add(hrsReceiptEarningRow);
                            found = true;
                        }
                    }
                }

                if (!found) { // not exists
                    if (moEarnigs.get(moKeyEarning.getValue()[0]).getFkLoanTypeId() == SModSysConsts.HRSS_TP_LOAN_NON) {  // not is of loan type
                        hrsReceiptEarningRow = new SHrsPayrollReceiptEarning();

                        hrsReceiptEarningRow.setEarning(moEarnigs.get(moKeyEarning.getValue()[0]));
                        hrsReceiptEarningRow.setHrsReceipt(hrsReceipt);

                        hrsReceiptEarningRow.setRowType(SHrsPayrollReceiptEarning.BY_EAR);
                        hrsReceiptEarningRow.setPkMoveId(hrsReceipt.getHrsEarnings().size() + 1);
                        hrsReceiptEarningRow.setXtaEmployee(hrsReceipt.getHrsEmployee().getEmployee().getAuxEmployee());
                        hrsReceiptEarningRow.setXtaValueAlleged(0d);
                        hrsReceiptEarningRow.setXtaValue(0d);
                        hrsReceiptEarningRow.setXtaUnit((String) miClient.getSession().readField(SModConsts.HRSS_TP_EAR_COMP, new int[] { moEarnigs.get(moKeyEarning.getValue()[0]).getFkEarningComputationTypeId() }, SDbRegistry.FIELD_CODE));
                        hrsReceiptEarningRow.setPayment(false);
                        hrsReceiptEarningRow.setXtaLoan("");
                        hrsReceiptEarningRow.setReceiptEarning(createReceipEarning(hrsReceipt, hrsReceiptEarningRow));

                        rows.add(hrsReceiptEarningRow);
                    }
                }
            }
        }
        enableFields(rows.size() > 0);
        
        moGridEmployeeRow.populateGrid(rows);
        moGridEmployeeRow.clearSortKeys();
        moGridEmployeeRow.setSelectedGridRow(0);
    }
    
    private void actionEarningAdd() {
        if (moComEarningValue.getField().getValue() <= 0) {
            miClient.showMsgBoxWarning(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + SGuiUtils.getLabelName(jlValue) + "'.");
            moComEarningValue.getField().getComponent().requestFocus();
        }
        else {
            updateReceiptsEarningRows(true);
            itemStateEarning();
            moComEarningValue.getField().setValue(0d);            
        }
    }
    
    public void actionClean() {
        SHrsPayrollReceiptEarning hrsReceiptEarningRow = null;
        
        if (jbClean.isEnabled()) {
            if (moGridEmployeeRow.getTable().getSelectedRowCount() == 1) {
                SGridRow gridRow = moGridEmployeeRow.getSelectedGridRow();

                hrsReceiptEarningRow = (SHrsPayrollReceiptEarning) gridRow;
                
                hrsReceiptEarningRow.setXtaValueAlleged(0d);
                hrsReceiptEarningRow.setXtaValue(0d);
                hrsReceiptEarningRow.setPayment(false);
                if (hrsReceiptEarningRow.getEarning().getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT) {
                    hrsReceiptEarningRow.getReceiptEarning().setAmountUnitary(0d);
                }
                else {
                    hrsReceiptEarningRow.getReceiptEarning().setUnitsAlleged(0d);
                    hrsReceiptEarningRow.getReceiptEarning().setUnits(0d);
                }
                hrsReceiptEarningRow.getReceiptEarning().setAmountSystem_r(0d);
                hrsReceiptEarningRow.getReceiptEarning().setAmount_r(0d);
                // hrsReceiptEarningRow.computeEarning(); XXX jbarajas 15/04/2015
                
                hrsReceiptEarningRow.getReceiptEarning().setFkLoanEmployeeId_n(SLibConsts.UNDEFINED);
                hrsReceiptEarningRow.getReceiptEarning().setFkLoanLoanId_n(SLibConsts.UNDEFINED);
                hrsReceiptEarningRow.getReceiptEarning().setFkLoanTypeId_n(SLibConsts.UNDEFINED);
                
                moGridEmployeeRow.renderGridRows();
                moGridEmployeeRow.setSelectedGridRow(moGridEmployeeRow.getModel().getGridRows().indexOf(gridRow));
            }
        }
    }

    public void actionCleanAll() {
        if (jbCleanAll.isEnabled()) {
            if (moGridEmployeeRow.getTable().getRowCount() > 0) {
                if (miClient.showMsgBoxConfirm("¿Está seguro que desea limpiar todas las capturas?") == JOptionPane.YES_OPTION) {
                    initEmployee();

                    moGridEmployeeRow.renderGridRows();
                    moGridEmployeeRow.getTable().requestFocus();
                    moGridEmployeeRow.setSelectedGridRow(0);
                }
            }
        }
    }
    
    private void populateEarnings(ArrayList<SDbEarning> earnings) {
        Vector<SGuiItem> items = new Vector<SGuiItem>();
        boolean add = true;
        
        try {
            items.add(new SGuiItem(new int[] { 0 }, "- Percepción -"));
            
            for (SDbEarning earning : earnings) {
                add = true;
                
                if ((earning.getFkAbsenceClassId_n() != SLibConsts.UNDEFINED && earning.getFkAbsenceTypeId_n() != SLibConsts.UNDEFINED) || earning.getFkBenefitTypeId() != SModSysConsts.HRSS_TP_BEN_NON) {
                    add = false;
                }
                
                if (add) {
                    items.add(new SGuiItem(earning.getPrimaryKey(), (earning.getCode() + " - " + earning.getName()), earning.getFkEarningComputationTypeId()));
                }
            }
            
            moKeyEarning.removeAllItems();
            for (SGuiItem item : items) {
                moKeyEarning.addItem(item);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
    }
    
    @Override
    public void addAllListeners() {
        moKeyEarning.addItemListener(this);
        moComEarningValue.getField().getComponent().addKeyListener(this);
        jbEarningAdd.addActionListener(this);
        jbClean.addActionListener(this);
        jbCleanAll.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        moKeyEarning.removeItemListener(this);
        moComEarningValue.getField().getComponent().removeKeyListener(this);
        jbEarningAdd.removeActionListener(this);
        jbClean.removeActionListener(this);
        jbCleanAll.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        return validation;
    }

    @Override
    public void setValue(final int type, final Object value) {
        try {
            switch (type) {
                case SModConsts.HRS_PAY_RCP:
                    maReceipts = (ArrayList<SHrsPayrollReceipt>) value;
                    break;
                case SModConsts.HRS_EAR:
                    moEarnigs = new HashMap<Integer, SDbEarning>();
                    populateEarnings((ArrayList<SDbEarning>) value);

                    for (SDbEarning ear : (ArrayList<SDbEarning>) value) {
                        moEarnigs.put(ear.getPkEarningId(), ear);
                    }
                    break;
                default:
                    break;
            }
        }
        catch (Exception e ) {
            SLibUtils.printException(this, e);
        }
    }

    @Override
    public Object getValue(final int type) {
        Object value = null;
        
        switch (type) {
            case SModConsts.HRS_PAY_RCP:
                value = maReceipts;
                break;
            default:
                break;
        }

        return value;
    }
    
    @Override
    public void actionSave() {
        updateReceiptsEarningRows(false);
        super.actionSave();
    }

    @Override
    public void notifyRowNew(int gridType, int gridSubtype, int row, SGridRow gridRow) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void notifyRowEdit(int gridType, int gridSubtype, int row, SGridRow gridRow) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void notifyRowDelete(int gridType, int gridSubtype, int row, SGridRow gridRow) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox && e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox comboBox = (JComboBox)  e.getSource();

            if (comboBox == moKeyEarning) {
                itemStateEarning();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbClean) {
                actionClean();
            }
            else if (button == jbCleanAll) {
                actionCleanAll();
            }
            else if (button == jbEarningAdd) {
                actionEarningAdd();
            }
        }
    }

    @Override
    public void editingStopped(ChangeEvent e) {
        switch (moGridEmployeeRow.getTable().getSelectedColumn()) {
            case COL_BAL_ALL:
                validateCellEditor();
                processEditingAppPayment();
                break;
            case COL_BAL:
            case COL_APP:
                processEditingAppPayment();
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
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        if (evt.getSource() instanceof JTextField) {
            JTextField textField = (JTextField) evt.getSource();

            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                if (textField == moComEarningValue.getField().getComponent()) {
                    jbEarningAdd.requestFocus();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

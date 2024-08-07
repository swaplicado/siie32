/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbEarning;
import erp.mod.hrs.db.SDbPayrollReceiptEarning;
import erp.mod.hrs.db.SHrsPayroll;
import erp.mod.hrs.db.SHrsReceipt;
import erp.mod.hrs.db.SHrsReceiptEarning;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
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
 * @author Juan Barajas, Sergio Flores
 */
public class SDialogPayrollEarnings extends SBeanFormDialog implements SGridPaneFormOwner, ActionListener, ItemListener, CellEditorListener {

    private static final int COL_VAL = 1;
    private static final int COL_AMT_UNT = 3;
    private static final int COL_SET_FLAG = 7;
    
    private SHrsPayroll moHrsPayroll;
    private ArrayList<SHrsReceipt> maHrsReceipts;
    private SDbEarning moEarning;
    private HashMap<Integer, SDbEarning> moEarningsMap;
    private SGridPaneForm moGridEmployeeRow;

    /**
     * Creates new form SDialogPayrollEarnings
     * @param client
     * @param title
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

        jpMain = new javax.swing.JPanel();
        jpSettings = new javax.swing.JPanel();
        jpEarning = new javax.swing.JPanel();
        jlEarning = new javax.swing.JLabel();
        moKeyEarning = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel1 = new javax.swing.JPanel();
        jlOtherPayment = new javax.swing.JLabel();
        moKeyOtherPayment = new sa.lib.gui.bean.SBeanFieldKey();
        jpEmployee = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlValue = new javax.swing.JLabel();
        moCompValue = new sa.lib.gui.bean.SBeanCompoundField();
        jbSetAll = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jbClear = new javax.swing.JButton();
        jbClearAll = new javax.swing.JButton();
        jpTotal = new javax.swing.JPanel();
        jlTotal = new javax.swing.JLabel();
        moCurTotal = new sa.lib.gui.bean.SBeanCompoundFieldCurrency();
        jlTotalHelp = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jpMain.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpMain.setLayout(new java.awt.BorderLayout());

        jpSettings.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jpEarning.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        jlEarning.setText("Percepción:*");
        jlEarning.setPreferredSize(new java.awt.Dimension(100, 23));
        jpEarning.add(jlEarning);

        moKeyEarning.setPreferredSize(new java.awt.Dimension(400, 23));
        jpEarning.add(moKeyEarning);

        jpSettings.add(jpEarning);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlOtherPayment.setText("Tipo otro pago:*");
        jlOtherPayment.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel1.add(jlOtherPayment);

        moKeyOtherPayment.setToolTipText("Tipo otro pago");
        moKeyOtherPayment.setPreferredSize(new java.awt.Dimension(575, 23));
        jPanel1.add(moKeyOtherPayment);

        jpSettings.add(jPanel1);

        jpMain.add(jpSettings, java.awt.BorderLayout.NORTH);

        jpEmployee.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlValue.setText("Cantidad/monto:");
        jlValue.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel2.add(jlValue);

        moCompValue.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel2.add(moCompValue);

        jbSetAll.setText("Asignar a todos");
        jbSetAll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbSetAll.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel2.add(jbSetAll);

        jPanel4.add(jPanel2, java.awt.BorderLayout.WEST);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbClear.setText("Limpiar");
        jbClear.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbClear.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel3.add(jbClear);

        jbClearAll.setText("Limpiar todo");
        jbClearAll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbClearAll.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel3.add(jbClearAll);

        jPanel4.add(jPanel3, java.awt.BorderLayout.EAST);

        jpEmployee.add(jPanel4, java.awt.BorderLayout.NORTH);

        jpMain.add(jpEmployee, java.awt.BorderLayout.CENTER);

        jpTotal.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTotal.setText("Total asignado:");
        jlTotal.setPreferredSize(new java.awt.Dimension(100, 23));
        jpTotal.add(jlTotal);

        moCurTotal.setEditable(false);
        jpTotal.add(moCurTotal);

        jlTotalHelp.setForeground(java.awt.SystemColor.textInactiveText);
        jlTotalHelp.setText("(Monto total de la percepción actual en esta nómina)");
        jlTotalHelp.setPreferredSize(new java.awt.Dimension(300, 23));
        jpTotal.add(jlTotalHelp);

        jpMain.add(jpTotal, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jpMain, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton jbClear;
    private javax.swing.JButton jbClearAll;
    private javax.swing.JButton jbSetAll;
    private javax.swing.JLabel jlEarning;
    private javax.swing.JLabel jlOtherPayment;
    private javax.swing.JLabel jlTotal;
    private javax.swing.JLabel jlTotalHelp;
    private javax.swing.JLabel jlValue;
    private javax.swing.JPanel jpEarning;
    private javax.swing.JPanel jpEmployee;
    private javax.swing.JPanel jpMain;
    private javax.swing.JPanel jpSettings;
    private javax.swing.JPanel jpTotal;
    private sa.lib.gui.bean.SBeanCompoundField moCompValue;
    private sa.lib.gui.bean.SBeanCompoundFieldCurrency moCurTotal;
    private sa.lib.gui.bean.SBeanFieldKey moKeyEarning;
    private sa.lib.gui.bean.SBeanFieldKey moKeyOtherPayment;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 720, 450);
        
        jbSave.setText("Cerrar");
        jbCancel.setEnabled(false);

        moKeyEarning.setKeySettings(miClient, SGuiUtils.getLabelName(jlEarning), true);
        moKeyOtherPayment.setKeySettings(miClient, SGuiUtils.getLabelName(moKeyOtherPayment.getToolTipText()), true);
        moCompValue.setCompoundFieldSettings(miClient);
        moCompValue.getField().setDecimalSettings(SGuiUtils.getLabelName(jlValue), SGuiConsts.GUI_TYPE_DEC_QTY, true);
        moCompValue.getField().setNextButton(jbSetAll);
        
        moFields.addField(moKeyEarning);
        moFields.addField(moKeyOtherPayment);
        moFields.addField(moCompValue.getField());
        moFields.setFormButton(jbSave);

        // read-only field:
        moCurTotal.setCompoundFieldSettings(miClient);
        moCurTotal.getField().setDecimalSettings(SGuiUtils.getLabelName(jlTotal), SGuiConsts.GUI_TYPE_DEC_AMT, false);
        
        moGridEmployeeRow = new SGridPaneForm(miClient, SModConsts.HRSX_PAY_REC_EAR, SLibConsts.UNDEFINED, "Empleados") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false, false, false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                SGridColumnForm columnForm;
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "Empleado"));
                columnForm = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Cant.", 55);
                columnForm.setEditable(true);
                gridColumnsForm.add(columnForm);
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unid.", 35));
                columnForm = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Monto unit. $", 80);
                columnForm.setEditable(true);
                gridColumnsForm.add(columnForm);
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Monto $", 80));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Cant. ajustada", 55));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unid.", 35));
                columnForm = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Pago");
                columnForm.setEditable(true);
                gridColumnsForm.add(columnForm);
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Exento $", 80));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_AMT, "Gravado $", 80));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Crédito/préstamo"));
                
                return gridColumnsForm;
            }
        };

        moGridEmployeeRow.setForm(null);
        moGridEmployeeRow.setPaneFormOwner(this);
        jpEmployee.add(moGridEmployeeRow, BorderLayout.CENTER);
        
        reloadCatalogues();
        addAllListeners();
        
        enableFields(false);
    }
    
    @SuppressWarnings("unchecked")
    private void setPayroll(SHrsPayroll payroll) {
        moHrsPayroll = payroll;

        // populate map of earnings & combo box at the same time:
        
        moEarningsMap = new HashMap<>();
        
        moKeyEarning.removeAllItems();
        moKeyEarning.addItem(new SGuiItem(new int[] { 0 }, "- Percepción -"));

        for (SDbEarning earning : moHrsPayroll.getEarnings()) {
            if (earning.isMassAsignable()) {
                moEarningsMap.put(earning.getPkEarningId(), earning);
                
                moKeyEarning.addItem(new SGuiItem(earning.getPrimaryKey(), (earning.getCode() + " - " + earning.getName()), earning.getFkEarningComputationTypeId()));
            }
        }
    }
    
    private void enableFields(final boolean enable) {
        moCompValue.getField().setEditable(enable);
        jbSetAll.setEnabled(enable);
    }
    
    private SDbPayrollReceiptEarning createPayrollReceipEarning(final SHrsReceipt hrsReceipt, final SHrsReceiptEarning hrsReceiptEarning) {
        double unitsAlleged;
        double amountUnitAlleged;
        int moveId;
        
        if (hrsReceiptEarning.getPayrollReceiptEarning() != null) {
            unitsAlleged = hrsReceiptEarning.getPayrollReceiptEarning().getUnitsAlleged();
            amountUnitAlleged = hrsReceiptEarning.getPayrollReceiptEarning().getAmountUnitary();
            moveId = hrsReceiptEarning.getPayrollReceiptEarning().getPkMoveId();
        }
        else {
            if (hrsReceiptEarning.getEarning().isBasedOnUnits()) {
                unitsAlleged = 0;
                amountUnitAlleged = 0;
            }
            else {
                unitsAlleged = 1;
                amountUnitAlleged = 0;
            }
            moveId = hrsReceipt.getHrsReceiptEarnings().size() + 1;
        }

        SDbPayrollReceiptEarning earning = hrsReceipt.getHrsPayroll().createPayrollReceiptEarning(
                hrsReceipt, hrsReceiptEarning.getEarning(), null, 
                unitsAlleged, amountUnitAlleged, false, 
                0, 0, moveId);
        
        if (moKeyOtherPayment.isEnabled() && moKeyOtherPayment.getSelectedIndex() > 0) {
            earning.setFkOtherPaymentTypeId(moKeyOtherPayment.getValue()[0]);
        }
        
        return earning;
    }
    
    private void refreshGridRows() {
        int row = moGridEmployeeRow.getTable().getSelectedRow();
        moGridEmployeeRow.renderGridRows();
        moGridEmployeeRow.setSelectedGridRow(row);
        computeTotal();
    }
    
    private void computeTotal() {
        double total = 0;
        
        if (moKeyEarning.getSelectedIndex() > 0) {
            for (SHrsReceipt hrsReceipt : maHrsReceipts) {
                for (SHrsReceiptEarning hrsReceiptEarning : hrsReceipt.getHrsReceiptEarnings()) {
                    if (hrsReceiptEarning.getEarning().getPkEarningId() == moEarning.getPkEarningId()) {
                        total = SLibUtils.roundAmount(total + hrsReceiptEarning.getPayrollReceiptEarning().getAmount_r());
                    }
                }
            }
        }
        
        moCurTotal.getField().setValue(total);
    }
    
    private void computeSetAll() throws Exception {
        double unitsAlleged;
        double amountUnitAlleged;
        
        if (moKeyEarning.getSelectedIndex() > 0) {
            if (moEarning.isBasedOnUnits()) {
                unitsAlleged = moCompValue.getField().getValue();
                amountUnitAlleged = 0;
            }
            else {
                unitsAlleged = 1;
                amountUnitAlleged = moCompValue.getField().getValue();
            }
            
            for (SGridRow gridRow : moGridEmployeeRow.getModel().getGridRows()) { // process grid
                SHrsReceiptEarning hrsReceiptEarningCopy = (SHrsReceiptEarning) gridRow;
                SHrsReceipt hrsReceipt = hrsReceiptEarningCopy.getHrsReceipt();
                
                double units = moEarning.computeEarningUnits(unitsAlleged, moHrsPayroll.getPayroll());
                hrsReceiptEarningCopy.getPayrollReceiptEarning().setUnitsAlleged(unitsAlleged);
                hrsReceiptEarningCopy.getPayrollReceiptEarning().setUnits(units);

                double amount;
                if (moEarning.isBasedOnUnits()) {
                    amount = moEarning.computeEarningAmount(units, hrsReceiptEarningCopy.getPayrollReceiptEarning().getAmountUnitary());
                }
                else {
                    hrsReceiptEarningCopy.getPayrollReceiptEarning().setAmountUnitary(amountUnitAlleged);
                    amount = moEarning.computeEarningAmount(units, amountUnitAlleged);
                }
                
                hrsReceiptEarningCopy.getPayrollReceiptEarning().setAmountSystem_r(amount);
                hrsReceiptEarningCopy.getPayrollReceiptEarning().setAmount_r(amount);

                hrsReceiptEarningCopy.getPayrollReceiptEarning().setUserEdited(false);
                hrsReceiptEarningCopy.getPayrollReceiptEarning().setAutomatic(false);
                
                if (hrsReceipt.getHrsReceiptEarning(hrsReceiptEarningCopy.getPayrollReceiptEarning().getPkMoveId()) == null) {
                    hrsReceipt.addHrsReceiptEarning(hrsReceiptEarningCopy);
                }
                else {
                    hrsReceipt.replaceHrsReceiptEarning(hrsReceiptEarningCopy.getPayrollReceiptEarning().getPkMoveId(), hrsReceiptEarningCopy);
                }
            }
            
            refreshGridRows();
        }
    }
    
    private void actionPerformedSetAll() {
        try {
            SGuiValidation validation = moFields.validateFields();
            if (!SGuiUtils.computeValidation(miClient, validation)) {
                return;
            }

            computeSetAll();

            moCompValue.getField().resetField();
            moKeyEarning.requestFocusInWindow();
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedClear() {
        SHrsReceiptEarning hrsReceiptEarningCopy = (SHrsReceiptEarning) moGridEmployeeRow.getSelectedGridRow();

        if (hrsReceiptEarningCopy != null) {
            hrsReceiptEarningCopy.clearAmount();
            
            SHrsReceipt hrsReceipt = hrsReceiptEarningCopy.getHrsReceipt();
            hrsReceipt.replaceHrsReceiptEarning(hrsReceiptEarningCopy.getPayrollReceiptEarning().getPkMoveId(), hrsReceiptEarningCopy);

            refreshGridRows();
        }
    }

    private void actionPerformedClearAll() {
        if (miClient.showMsgBoxConfirm("¿Está seguro que desea limpiar todas las capturas?") == JOptionPane.YES_OPTION) {
            for (SGridRow gridRow : moGridEmployeeRow.getModel().getGridRows()) { // process grid
                SHrsReceiptEarning hrsReceiptEarningCopy = (SHrsReceiptEarning) gridRow;
                
                hrsReceiptEarningCopy.clearAmount();

                SHrsReceipt hrsReceipt = hrsReceiptEarningCopy.getHrsReceipt();
                hrsReceipt.replaceHrsReceiptEarning(hrsReceiptEarningCopy.getPayrollReceiptEarning().getPkMoveId(), hrsReceiptEarningCopy);
            }
            
            refreshGridRows();
        }
    }
    
    private void itemStateChangedEarning() {
        Vector<SGridRow> rows = new Vector<>();
        
        moKeyOtherPayment.setEnabled(false);
        moKeyOtherPayment.resetField();
        
        if (moKeyEarning.getSelectedIndex() <= 0) {
            moEarning = null;
        }
        else {
            moEarning = moEarningsMap.get(moKeyEarning.getValue()[0]);
            moCompValue.setCompoundText(moHrsPayroll.getEarningComputationTypesMap().get(moEarning.getFkEarningComputationTypeId()));
            
            moKeyOtherPayment.setValue(new int[] { moEarning.getFkOtherPaymentTypeId() });
            if (moEarning.getFkEarningTypeId() == SModSysConsts.HRSS_TP_EAR_OTH) {
                moKeyOtherPayment.setEnabled(true);
            }
            else {
                moKeyOtherPayment.setEnabled(false);
            }
            
            // prepare grid rows:
            
            for (SHrsReceipt hrsReceipt : maHrsReceipts) {
                SDbPayrollReceiptEarning payrollReceiptEarning = null;

                for (SHrsReceiptEarning hrsReceiptEarning : hrsReceipt.getHrsReceiptEarnings()) {
                    if (hrsReceiptEarning.getEarning().getPkEarningId() == moEarning.getPkEarningId()) {
                        payrollReceiptEarning = hrsReceiptEarning.getPayrollReceiptEarning();
                        break;
                    }
                }

                // add HRS receipt earning as a copy:
                
                SHrsReceiptEarning hrsReceiptEarningCopy = new SHrsReceiptEarning(SHrsReceiptEarning.INPUT_BY_EAR);
                hrsReceiptEarningCopy.setHrsReceipt(hrsReceipt);
                hrsReceiptEarningCopy.setEarning(moEarning);
                
                if (payrollReceiptEarning != null) {
                    hrsReceiptEarningCopy.setPayrollReceiptEarning(payrollReceiptEarning);
                }
                else {
                    hrsReceiptEarningCopy.setPayrollReceiptEarning(createPayrollReceipEarning(hrsReceipt, hrsReceiptEarningCopy));
                }

                rows.add(hrsReceiptEarningCopy);
            }
        }
        
        enableFields(!rows.isEmpty());
        
        moGridEmployeeRow.populateGrid(rows);
        moGridEmployeeRow.getTable().getDefaultEditor(Boolean.class).addCellEditorListener(this);
        moGridEmployeeRow.getTable().getDefaultEditor(Double.class).addCellEditorListener(this);
        moGridEmployeeRow.setSelectedGridRow(0);
        computeTotal();
    }
    
    private void itemStateChangedEarningOtherPayment() {
        int otherPaymentType;
        
        if (moKeyOtherPayment.getSelectedIndex() <= 0) {
            otherPaymentType = SModSysConsts.HRSS_TP_OTH_PAY_NA;
        }
        else {
            otherPaymentType = moKeyOtherPayment.getValue()[0];
        }
        
        for (SGridRow row : moGridEmployeeRow.getModel().getGridRows()) {
            SHrsReceiptEarning hrsReceiptEarning = (SHrsReceiptEarning) row;
            hrsReceiptEarning.getPayrollReceiptEarning().setFkOtherPaymentTypeId(otherPaymentType);
        }
    }
    
    private void processCellEdition() {
        boolean refresh = false;
        SHrsReceiptEarning hrsReceiptEarningCopy = (SHrsReceiptEarning) moGridEmployeeRow.getSelectedGridRow(); // get copy of HRS receipt earning
        
        if (hrsReceiptEarningCopy != null) {
            switch (moGridEmployeeRow.getTable().getSelectedColumn()) {
                case COL_VAL:
                    if (hrsReceiptEarningCopy.isEditableValueAlleged()) {
                        refresh = true;
                    }
                    else {
                        miClient.showMsgBoxWarning("No se puede modificar la 'Cantidad' de la percepción '" + hrsReceiptEarningCopy.getEarning().getName() + "'.");
                    }
                    break;
                case COL_AMT_UNT:
                    if (hrsReceiptEarningCopy.isEditableAmountUnitary(hrsReceiptEarningCopy.getAmountBeingEdited())) {
                        refresh = true;
                    }
                    else {
                        miClient.showMsgBoxWarning("No se puede modificar el 'Monto unitario' de la percepción '" + hrsReceiptEarningCopy.getEarning().getName() + "'."
                                + (!hrsReceiptEarningCopy.getEarning().isBenefit() ? "" : "El monto capturado no puede ser menor que " + SLibUtils.getDecimalFormatAmount().format(hrsReceiptEarningCopy.getAmountOriginal()) + "."));
                    }
                    break;
                case COL_SET_FLAG:
                    refresh = true;
                    break;
                default:
            }
            
            if (refresh) {
                SHrsReceipt hrsReceipt = hrsReceiptEarningCopy.getHrsReceipt(); // convenience variable
                
                if (hrsReceipt.getHrsReceiptEarning(hrsReceiptEarningCopy.getPayrollReceiptEarning().getPkMoveId()) == null) {
                    hrsReceipt.addHrsReceiptEarning(hrsReceiptEarningCopy);
                }
                else {
                    hrsReceipt.replaceHrsReceiptEarning(hrsReceiptEarningCopy.getPayrollReceiptEarning().getPkMoveId(), hrsReceiptEarningCopy);
                }
                
                refreshGridRows();
            }
        }
    }
    
    @Override
    public void actionSave() {
        // remove unused earnings:

        for (SHrsReceipt receipt : maHrsReceipts) {
            ArrayList<SHrsReceiptEarning> removableHrsReceiptEarnings = new ArrayList<>();
            
            for (SHrsReceiptEarning receiptEarning : receipt.getHrsReceiptEarnings()) {
                if (!receiptEarning.isApplying()) {
                    removableHrsReceiptEarnings.add(receiptEarning);
                }
            }
            
            for (SHrsReceiptEarning removableHrsReceiptEarning : removableHrsReceiptEarnings) {
                receipt.removeHrsReceiptEarning(removableHrsReceiptEarning.getPayrollReceiptEarning().getPkMoveId());
            }
        }
        
        super.actionSave();
    }

    @Override
    public void actionCancel() {
        mnFormResult = SGuiConsts.FORM_RESULT_CANCEL;
        dispose();
    }
    
    @Override
    public void addAllListeners() {
        jbSetAll.addActionListener(this);
        jbClear.addActionListener(this);
        jbClearAll.addActionListener(this);
        moKeyEarning.addItemListener(this);
        moKeyOtherPayment.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbSetAll.removeActionListener(this);
        jbClear.removeActionListener(this);
        jbClearAll.removeActionListener(this);
        moKeyEarning.removeItemListener(this);
        moKeyOtherPayment.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyOtherPayment, SModConsts.HRSS_TP_OTH_PAY, 0, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = new SGuiValidation();
        
        if (moKeyOtherPayment.isEnabled()) {
            if (moKeyOtherPayment.getSelectedIndex() <= 0) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(moKeyOtherPayment.getToolTipText()) + "'.");
                validation.setComponent(moKeyOtherPayment);
            }
            else if (moKeyOtherPayment.getValue()[0] == SModSysConsts.HRSS_TP_OTH_PAY_NA) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_DIF + "'" + SGuiUtils.getLabelName(moKeyOtherPayment.getToolTipText()) + "'.");
                validation.setComponent(moKeyOtherPayment);
            }
        }
        
        return new SGuiValidation();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(final int type, final Object value) {
        try {
            switch (type) {
                case SModConsts.HRS_PAY:
                    setPayroll((SHrsPayroll) value);
                    break;
                case SModConsts.HRS_PAY_RCP:
                    maHrsReceipts = (ArrayList<SHrsReceipt>) value;
                    break;
                default:
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
                value = maHrsReceipts;
                break;
            default:
        }

        return value;
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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbSetAll) {
                actionPerformedSetAll();
            }
            else if (button == jbClear) {
                actionPerformedClear();
            }
            else if (button == jbClearAll) {
                actionPerformedClearAll();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox && e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox comboBox = (JComboBox)  e.getSource();

            if (comboBox == moKeyEarning) {
                itemStateChangedEarning();
            }
            else if (comboBox == moKeyOtherPayment) {
                itemStateChangedEarningOtherPayment();
            }
        }
    }

    @Override
    public void editingStopped(ChangeEvent e) {
        processCellEdition();
    }

    @Override
    public void editingCanceled(ChangeEvent e) {
        
    }
}

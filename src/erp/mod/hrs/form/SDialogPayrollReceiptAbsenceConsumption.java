/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbAbsence;
import erp.mod.hrs.db.SDbAbsenceConsumption;
import erp.mod.hrs.db.SHrsBenefit;
import erp.mod.hrs.db.SHrsReceipt;
import erp.mod.hrs.db.SHrsUtils;
import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.JFormattedTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFormDialog;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDialogPayrollReceiptAbsenceConsumption extends SBeanFormDialog implements FocusListener, ListSelectionListener  {

    protected SHrsReceipt moHrsReceipt;
    protected SGridPaneForm moGridAbsences;
    protected SDbAbsence moCurrentAbsence;
    
    /**
     * Creates new form SDialogPayrollReceiptAbsence
     * @param client
     * @param title
     */
    public SDialogPayrollReceiptAbsenceConsumption(SGuiClient client, String title) {
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
        jpAbsences = new javax.swing.JPanel();
        jpConsumption = new javax.swing.JPanel();
        jpBenefit = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlAnniversary = new javax.swing.JLabel();
        moIntBenefitAnniversary = new sa.lib.gui.bean.SBeanFieldInteger();
        moIntBenefitYear = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel5 = new javax.swing.JPanel();
        jlBenefitDays = new javax.swing.JLabel();
        moDecBenefitDays = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel11 = new javax.swing.JPanel();
        jlPaidDays = new javax.swing.JLabel();
        moDecPaidDays = new sa.lib.gui.bean.SBeanFieldDecimal();
        jpAbsenceConsumption = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jlDateLastConsumption = new javax.swing.JLabel();
        moDateLastConsumption = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel8 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moDateDateStart = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel9 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        moDateDateEnd = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel10 = new javax.swing.JPanel();
        jlEffectiveDays = new javax.swing.JLabel();
        moIntEffectiveDays = new sa.lib.gui.bean.SBeanFieldInteger();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jpAbsences.setBorder(javax.swing.BorderFactory.createTitledBorder("Incidencias:"));
        jpAbsences.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jpAbsences, java.awt.BorderLayout.CENTER);

        jpConsumption.setLayout(new java.awt.BorderLayout());

        jpBenefit.setBorder(javax.swing.BorderFactory.createTitledBorder("Consumo prestación:"));
        jpBenefit.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.GridLayout(4, 0, 0, 5));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAnniversary.setText("Aniversario:");
        jlAnniversary.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlAnniversary);

        moIntBenefitAnniversary.setEditable(false);
        moIntBenefitAnniversary.setToolTipText("Año aniversario");
        moIntBenefitAnniversary.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(moIntBenefitAnniversary);

        moIntBenefitYear.setEditable(false);
        moIntBenefitYear.setToolTipText("Año aniversario");
        moIntBenefitYear.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(moIntBenefitYear);

        jPanel6.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBenefitDays.setText("Días por pagar:");
        jlBenefitDays.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlBenefitDays);

        moDecBenefitDays.setEditable(false);
        moDecBenefitDays.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel5.add(moDecBenefitDays);

        jPanel6.add(jPanel5);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPaidDays.setText("Días pagados:");
        jlPaidDays.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlPaidDays);

        moDecPaidDays.setEditable(false);
        moDecPaidDays.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel11.add(moDecPaidDays);

        jPanel6.add(jPanel11);

        jpBenefit.add(jPanel6, java.awt.BorderLayout.CENTER);

        jpConsumption.add(jpBenefit, java.awt.BorderLayout.WEST);

        jpAbsenceConsumption.setBorder(javax.swing.BorderFactory.createTitledBorder("Consumo incidencia:"));
        jpAbsenceConsumption.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateLastConsumption.setText("Último consumo:");
        jlDateLastConsumption.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlDateLastConsumption);

        moDateLastConsumption.setEditable(false);
        moDateLastConsumption.setFocusable(false);
        jPanel12.add(moDateLastConsumption);

        jPanel2.add(jPanel12);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial:");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlDateStart);

        moDateDateStart.setEditable(false);
        moDateDateStart.setFocusable(false);
        jPanel8.add(moDateDateStart);

        jPanel2.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha final:*");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlDateEnd);
        jPanel9.add(moDateDateEnd);

        jPanel2.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlEffectiveDays.setText("Días efectivos:*");
        jlEffectiveDays.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlEffectiveDays);

        moIntEffectiveDays.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(moIntEffectiveDays);

        jPanel2.add(jPanel10);

        jpAbsenceConsumption.add(jPanel2, java.awt.BorderLayout.NORTH);

        jpConsumption.add(jpAbsenceConsumption, java.awt.BorderLayout.CENTER);

        jPanel1.add(jpConsumption, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel jlAnniversary;
    private javax.swing.JLabel jlBenefitDays;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateLastConsumption;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlEffectiveDays;
    private javax.swing.JLabel jlPaidDays;
    private javax.swing.JPanel jpAbsenceConsumption;
    private javax.swing.JPanel jpAbsences;
    private javax.swing.JPanel jpBenefit;
    private javax.swing.JPanel jpConsumption;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateStart;
    private sa.lib.gui.bean.SBeanFieldDate moDateLastConsumption;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecBenefitDays;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecPaidDays;
    private sa.lib.gui.bean.SBeanFieldInteger moIntBenefitAnniversary;
    private sa.lib.gui.bean.SBeanFieldInteger moIntBenefitYear;
    private sa.lib.gui.bean.SBeanFieldInteger moIntEffectiveDays;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 720, 450);
        
        moIntBenefitAnniversary.setIntegerSettings(SGuiUtils.getLabelName(jlAnniversary), SGuiConsts.GUI_TYPE_INT, false);
        moIntBenefitYear.setIntegerSettings(SGuiUtils.getLabelName(jlAnniversary), SGuiConsts.GUI_TYPE_INT_CAL_YEAR, false);
        moDecBenefitDays.setDecimalSettings(SGuiUtils.getLabelName(jlBenefitDays), SGuiConsts.GUI_TYPE_DEC_QTY, false);
        moDecPaidDays.setDecimalSettings(SGuiUtils.getLabelName(jlPaidDays), SGuiConsts.GUI_TYPE_DEC_QTY, false);
        moDateLastConsumption.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateLastConsumption), false);
        moDateDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateStart), true);
        moDateDateEnd.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateEnd), true);
        moIntEffectiveDays.setIntegerSettings(SGuiUtils.getLabelName(jlEffectiveDays), SGuiConsts.GUI_TYPE_INT, true);
        
        //moFields.addField(moIntBenefitAnniversary);
        //moFields.addField(moIntBenefitYear);
        //moFields.addField(moDecBenefitDays);
        //moFields.addField(moDecPaidDays);
        //moFields.addField(moDateLastConsumption);
        //moFields.addField(moDateDateStart);
        moFields.addField(moDateDateEnd);
        moFields.addField(moIntEffectiveDays);
        
        moFields.setFormButton(jbSave);
        
        moGridAbsences = new SGridPaneForm(miClient, SModConsts.HRS_ABS, SLibConsts.UNDEFINED, "Incidencias") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Clase incidencia"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Tipo incidencia"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Folio"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha inicial"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha final"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_4B, "Días efectivos"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_4B, "Días pendientes"));
                
                return gridColumnsForm;
            }
        };

        moGridAbsences.setForm(null);
        moGridAbsences.setPaneFormOwner(null);
        //mvFormGrids.add(moGridAbsenceRow);
        jpAbsences.add(moGridAbsences, BorderLayout.CENTER);
        
        reloadCatalogues();
        addAllListeners();
    }
    
    private void populateAbsences() {
        Vector<SGridRow> rows = new Vector<>();

        for (SDbAbsence absence : moHrsReceipt.getHrsEmployee().getAbsences()) {
            if (!absence.isClosed()) {
                int consumedDays = absence.getActualConsumedDays(moHrsReceipt.getHrsEmployee());
                int remainingDays = absence.getEffectiveDays() - consumedDays;

                if (remainingDays > 0) {
                    absence.setAuxPendingDays(remainingDays);
                    rows.add(absence);
                }
            }
        }
        
        moGridAbsences.populateGrid(rows, this);
        moGridAbsences.setSelectedGridRow(0);
        moGridAbsences.getTable().requestFocusInWindow();
    }
    
    private void showCurrentAbsence() {
        // reset fields for benefits:
        
        moIntBenefitAnniversary.resetField();
        moIntBenefitYear.resetField();
        moDecBenefitDays.resetField();
        moDecPaidDays.resetField();
        
        // reset fields for absence:
        
        moDateLastConsumption.resetField();
        moDateDateStart.resetField();
        
        moDateDateEnd.resetField();
        moIntEffectiveDays.resetField();

        moDateDateEnd.setEditable(false);
        moIntEffectiveDays.setEditable(false);
        
        // show selected absence:
        
        if (moGridAbsences.getSelectedGridRow() == null) {
            moCurrentAbsence = null;
        }
        else {
            moCurrentAbsence = (SDbAbsence) moGridAbsences.getSelectedGridRow();
            
            /*
             * START of algorithm #ABS001. IMPORTANT: Please sync up any changes to this algorithm in all occurrences in proyect!
             */
            
            // estimate absence remaining-days to be consumed:

            int consumedDays = moCurrentAbsence.getActualConsumedDays(moHrsReceipt.getHrsEmployee());
            int remainingDays = moCurrentAbsence.getEffectiveDays() - consumedDays;
            
            if (remainingDays > 0) {
                Date lastConsumptionDate = consumedDays == 0 ? null : moCurrentAbsence.getActualLastConsumptionDate(moHrsReceipt.getHrsEmployee());
                Date consumptionStart = lastConsumptionDate == null ? moCurrentAbsence.getDateStart() : SLibTimeUtils.addDate(lastConsumptionDate, 0, 0, 1);

                // adjust consumption start-date, if neccesary:
                
                if (!moCurrentAbsence.consumesCalendarDays() && consumedDays > 0) { // preserve original absence start-date when there aren't previous consumptions
                    while (moHrsReceipt.getHrsPayroll().isNonWorkingDay(consumptionStart)) {
                        consumptionStart = SLibTimeUtils.addDate(consumptionStart, 0, 0, 1); // move to next day
                    }
                }

                /*
                 * END of algorithm #ABS001. IMPORTANT: Please sync up any changes to this algorithm in all occurrences in proyect!
                 */
                
                moDateLastConsumption.setValue(lastConsumptionDate);
                moDateDateStart.setValue(consumptionStart);
                
                if (remainingDays == 1) {
                    moDateDateEnd.setValue(moCurrentAbsence.getDateEnd());
                    moIntEffectiveDays.setValue(1);
                }
                
                moDateDateEnd.setEditable(true);
                moIntEffectiveDays.setEditable(true);
            }
            
            if (moCurrentAbsence.isVacation()) {
                try {
                    SHrsBenefit benefit = moHrsReceipt.getHrsBenefitsManager().getBenefitPaid(SModSysConsts.HRSS_TP_BEN_VAC, moCurrentAbsence.getBenefitsAnniversary(), moCurrentAbsence.getBenefitsYear());
                    
                    if (benefit != null) {
                        moIntBenefitAnniversary.setValue(benefit.getBenefitAnniversary());
                        moIntBenefitYear.setValue(benefit.getBenefitYear());
                        moDecBenefitDays.setValue(benefit.getBenefitDays());
                        moDecPaidDays.setValue(benefit.getPaidDays());
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void calculateEffectiveDays() {
        int effectiveDays = 0;
        
        if (moDateDateStart.getValue() != null && moDateDateEnd.getValue() != null) {
            if (moCurrentAbsence.consumesCalendarDays()) {
                effectiveDays = SLibTimeUtils.countPeriodDays(moDateDateStart.getValue(), moDateDateEnd.getValue());
            }
            else {
                effectiveDays = SHrsUtils.countBusinessDays(moDateDateStart.getValue(), moDateDateEnd.getValue(), moHrsReceipt.getHrsPayroll().getWorkingDaySettings(), moHrsReceipt.getHrsPayroll().getHolidays());
            }
        }

        moIntEffectiveDays.setValue(effectiveDays);
    }
    
    private SDbAbsenceConsumption createAbsenceConsumption() {
        SDbAbsenceConsumption absenceConsumption = new SDbAbsenceConsumption();
        
        //absenceConsumption.setPkEmployeeId(...);
        //absenceConsumption.setPkAbsenceId(...);
        //absenceConsumption.setPkConsumptionId(...);
        absenceConsumption.setDateStart(moDateDateStart.getValue());
        absenceConsumption.setDateEnd(moDateDateEnd.getValue());
        absenceConsumption.setEffectiveDays(moIntEffectiveDays.getValue());
        
        absenceConsumption.setParentAbsence(moCurrentAbsence);
        
        return absenceConsumption;
    }
    
    @Override
    public void addAllListeners() {
        moDateDateEnd.getComponent().addFocusListener(this);
    }

    @Override
    public void removeAllListeners() {
        moDateDateEnd.getComponent().removeFocusListener(this);
    }

    @Override
    public void reloadCatalogues() {
        
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
            SGuiValidation validation = moFields.validateFields();
        
        try {
            if (validation.isValid()) {
                if (moCurrentAbsence == null) {
                    validation.setMessage(SGridConsts.MSG_SELECT_ROW);
                    validation.setComponent(moGridAbsences.getTable());
                }
                else if (moCurrentAbsence.isVacation() && moIntEffectiveDays.getValue() > moCurrentAbsence.getAuxPendingDays()) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + moIntEffectiveDays.getFieldName() + "'" + SGuiConsts.ERR_MSG_FIELD_VAL_LESS_EQUAL + moCurrentAbsence.getAuxPendingDays() + ".");
                    validation.setComponent(moIntEffectiveDays);
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        return validation;
    }

    @Override
    public void setValue(final int type, final Object value) {
        switch (type) {
            case SModConsts.HRS_PAY_RCP:
                moHrsReceipt = (SHrsReceipt) value;
                populateAbsences();
                break;
            default:
                miClient.showMsgBoxWarning(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
    }

    @Override
    public Object getValue(final int type) {
        Object value = null;
        
        switch (type) {
            case SModConsts.HRS_ABS_CNS:
                value = createAbsenceConsumption();
                break;
            default:
                miClient.showMsgBoxWarning(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return value;
    }
    
    @Override
    public void actionSave() {
        if (SGuiUtils.computeValidation(miClient, validateForm())) {
            try {
                SDbAbsenceConsumption absenceConsumption = moHrsReceipt.createAbsenceConsumption(moCurrentAbsence, moDateDateStart.getValue(), moDateDateEnd.getValue(), moIntEffectiveDays.getValue());

                moHrsReceipt.updateHrsReceiptEarningAbsence(absenceConsumption, true);

                mnFormResult = SGuiConsts.FORM_RESULT_OK;
                dispose();
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof JFormattedTextField) {
            JFormattedTextField field = (JFormattedTextField) e.getSource();

            if (field == moDateDateEnd.getComponent()) {
                calculateEffectiveDays();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (moGridAbsences.getTable().getSelectedRowCount() != -1) {
                showCurrentAbsence();
            }
        }
    }
}

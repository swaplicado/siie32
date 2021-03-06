/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbAbsence;
import erp.mod.hrs.db.SDbAbsenceConsumption;
import erp.mod.hrs.db.SDbBenefitTable;
import erp.mod.hrs.db.SHrsBenefit;
import erp.mod.hrs.db.SHrsBenefitTableAnniversary;
import erp.mod.hrs.db.SHrsReceipt;
import erp.mod.hrs.db.SHrsUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
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
public class SDialogPayrollReceiptAbsence extends SBeanFormDialog implements ActionListener, FocusListener, ListSelectionListener  {

    protected SDbAbsence moAbsence;
    
    protected SHrsReceipt moHrsReceipt;
    protected SGridPaneForm moGridAbsences;
    
    private SDbBenefitTable moBenefitTable;
    private SHrsBenefit moHrsBenefit;
    private ArrayList<SHrsBenefit> maHrsBenefits;
    private ArrayList<SHrsBenefitTableAnniversary> maBenefitTableAnniversaries;
    
    /**
     * Creates new form SDialogPayrollReceiptAbsence
     * @param client
     * @param title
     */
    public SDialogPayrollReceiptAbsence(SGuiClient client, String title) {
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
        jlDaysToPaidTable = new javax.swing.JLabel();
        moIntDaysToPaidTable = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel11 = new javax.swing.JPanel();
        jlDaysPayed = new javax.swing.JLabel();
        moDecDaysPayed = new sa.lib.gui.bean.SBeanFieldDecimal();
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
        jlAnniversary.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(jlAnniversary);

        moIntBenefitAnniversary.setToolTipText("Año aniversario");
        moIntBenefitAnniversary.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(moIntBenefitAnniversary);

        moIntBenefitYear.setToolTipText("Año aniversario");
        moIntBenefitYear.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(moIntBenefitYear);

        jPanel6.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDaysToPaidTable.setText("Días:");
        jlDaysToPaidTable.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel5.add(jlDaysToPaidTable);

        moIntDaysToPaidTable.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel5.add(moIntDaysToPaidTable);

        jPanel6.add(jPanel5);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDaysPayed.setText("Días pagados:");
        jlDaysPayed.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel11.add(jlDaysPayed);

        moDecDaysPayed.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel11.add(moDecDaysPayed);

        jPanel6.add(jPanel11);

        jpBenefit.add(jPanel6, java.awt.BorderLayout.CENTER);

        jpConsumption.add(jpBenefit, java.awt.BorderLayout.WEST);

        jpAbsenceConsumption.setBorder(javax.swing.BorderFactory.createTitledBorder("Consumo incidencia:"));
        jpAbsenceConsumption.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateLastConsumption.setText("Último consumo:*");
        jlDateLastConsumption.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlDateLastConsumption);

        moDateLastConsumption.setFocusable(false);
        jPanel12.add(moDateLastConsumption);

        jPanel2.add(jPanel12);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial:*");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlDateStart);

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
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateLastConsumption;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlDaysPayed;
    private javax.swing.JLabel jlDaysToPaidTable;
    private javax.swing.JLabel jlEffectiveDays;
    private javax.swing.JPanel jpAbsenceConsumption;
    private javax.swing.JPanel jpAbsences;
    private javax.swing.JPanel jpBenefit;
    private javax.swing.JPanel jpConsumption;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateStart;
    private sa.lib.gui.bean.SBeanFieldDate moDateLastConsumption;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecDaysPayed;
    private sa.lib.gui.bean.SBeanFieldInteger moIntBenefitAnniversary;
    private sa.lib.gui.bean.SBeanFieldInteger moIntBenefitYear;
    private sa.lib.gui.bean.SBeanFieldInteger moIntDaysToPaidTable;
    private sa.lib.gui.bean.SBeanFieldInteger moIntEffectiveDays;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 720, 450);
        
        moIntBenefitAnniversary.setIntegerSettings(SGuiUtils.getLabelName(jlAnniversary), SGuiConsts.GUI_TYPE_INT, false);
        moIntBenefitYear.setIntegerSettings(SGuiUtils.getLabelName(jlAnniversary), SGuiConsts.GUI_TYPE_INT_CAL_YEAR, false);
        moIntDaysToPaidTable.setIntegerSettings(SGuiUtils.getLabelName(jlDaysToPaidTable), SGuiConsts.GUI_TYPE_INT, false);
        moDecDaysPayed.setDecimalSettings(SGuiUtils.getLabelName(jlDaysPayed), SGuiConsts.GUI_TYPE_DEC_QTY, false);
        moDateLastConsumption.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateLastConsumption), false);
        moDateDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateStart), true);
        moDateDateEnd.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateEnd), true);
        moIntEffectiveDays.setIntegerSettings(SGuiUtils.getLabelName(jlEffectiveDays), SGuiConsts.GUI_TYPE_INT, true);
        
        moFields.addField(moIntBenefitAnniversary);
        moFields.addField(moIntBenefitYear);
        moFields.addField(moIntDaysToPaidTable);
        moFields.addField(moDecDaysPayed);
        moFields.addField(moDateLastConsumption);
        moFields.addField(moDateDateStart);
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
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<SGridColumnForm>();

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Clase incidencia"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Tipo incidencia"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "Folio"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha inicial"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha final"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_RAW, "Días efectivos"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_RAW, "Días pendientes"));
                
                return gridColumnsForm;
            }
        };

        moGridAbsences.setForm(null);
        moGridAbsences.setPaneFormOwner(null);
        //mvFormGrids.add(moGridAbsenceRow);
        jpAbsences.add(moGridAbsences, BorderLayout.CENTER);
        
        reloadCatalogues();
        addAllListeners();
        
        moIntBenefitAnniversary.setEditable(false);
        moIntBenefitYear.setEditable(false);
        moIntDaysToPaidTable.setEditable(false);
        moDecDaysPayed.setEditable(false);
        moDateLastConsumption.setEditable(false);
        moDateDateStart.setEditable(false);
    }
    
    private void loadBenefitTables() throws Exception {
        ArrayList<SDbBenefitTable> aBenefitTables = new ArrayList<>();
        
        if (moHrsReceipt.getHrsPayroll().getConfig().getFkEarningVacationId_n() == SLibConsts.UNDEFINED) {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración vacaciones)");
        }
        
        moBenefitTable = SHrsUtils.getBenefitTableByEarning(miClient.getSession(), 
                moHrsReceipt.getHrsPayroll().getConfig().getFkEarningVacationId_n(), 
                moHrsReceipt.getHrsEmployee().getEmployee().getFkPaymentTypeId(), 
                moHrsReceipt.getHrsPayroll().getPayroll().getDateEnd());
        
        aBenefitTables.add(moBenefitTable);
        
        maBenefitTableAnniversaries = SHrsUtils.createBenefitTablesAnniversaries(aBenefitTables);
    }
    
    private void readHrsBenefitAcummulate(int seniority, int benefitYear) {
        try {
            loadBenefitTables();
            
            maHrsBenefits = SHrsUtils.readHrsBenefits(miClient.getSession(), moHrsReceipt.getHrsEmployee().getEmployee(), SModSysConsts.HRSS_TP_BEN_VAC, seniority, benefitYear, moHrsReceipt.getHrsPayroll().getPayroll().getPkPayrollId(), maBenefitTableAnniversaries, null, moHrsReceipt.getPayrollReceipt().getPaymentDaily());
            
            moIntBenefitAnniversary.setValue(seniority);
            moIntBenefitYear.setValue(benefitYear);
            
            for (SHrsBenefit hrsBenefit : maHrsBenefits) {
                if (SLibUtils.compareKeys(hrsBenefit.getBenefitKey(), new int[] { SModSysConsts.HRSS_TP_BEN_VAC, seniority, benefitYear })) {
                    hrsBenefit.setValuePayedReceipt(moHrsReceipt.getBenefitValue(SModSysConsts.HRSS_TP_BEN_VAC, hrsBenefit.getBenefitAnn(), hrsBenefit.getBenefitYear()));
                    hrsBenefit.setAmountPayedReceipt(moHrsReceipt.getBenefitAmount(SModSysConsts.HRSS_TP_BEN_VAC, hrsBenefit.getBenefitAnn(), hrsBenefit.getBenefitYear()));
                    moIntDaysToPaidTable.setValue((int) hrsBenefit.getValue());
                    moDecDaysPayed.setValue(hrsBenefit.getValuePayed() + hrsBenefit.getValuePayedReceipt());
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void createHrsBenefit() {
        moHrsBenefit = new SHrsBenefit(mnFormType,  moIntBenefitAnniversary.getValue(), moIntBenefitYear.getValue());
        
        for (SHrsBenefit hrsBenefit : maHrsBenefits) {
            if (SLibUtils.compareKeys(hrsBenefit.getBenefitKey(), new int[] { SModSysConsts.HRSS_TP_BEN_VAC, moIntBenefitAnniversary.getValue(), moIntBenefitYear.getValue() })) {
                moHrsBenefit.setValue(hrsBenefit.getValue());
                moHrsBenefit.setValuePayed(hrsBenefit.getValuePayed());
                moHrsBenefit.setAmount(hrsBenefit.getAmount());
                moHrsBenefit.setAmountPayed(hrsBenefit.getAmountPayed());
            }
        }
        
        double amount = SLibUtils.roundAmount(moIntEffectiveDays.getValue() * moHrsReceipt.getPayrollReceipt().getPaymentDaily());
        
        moHrsBenefit.setValuePayedReceipt(moIntEffectiveDays.getValue());
        moHrsBenefit.setAmountPayedReceipt(amount);
        moHrsBenefit.setAmountPayedReceiptSys(amount);
    }
    
    private void populateAbsence() {
        Vector<SGridRow> rows = new Vector<>();

        for (SDbAbsence absence : moHrsReceipt.getHrsEmployee().getAbsences()) {
            if (!absence.isClosed()) {
                int absenceConsumedDays = absence.getActualConsumedDays(moHrsReceipt.getHrsEmployee());
                int absenceRemainingDays = absence.getEffectiveDays() - absenceConsumedDays;

                if (absenceRemainingDays > 0) {
                    absence.setAuxPendingDays(absenceRemainingDays);
                    rows.add(absence);
                }
            }
        }
        
        moGridAbsences.populateGrid(rows, this);
        moGridAbsences.setSelectedGridRow(0);
        moGridAbsences.getTable().requestFocusInWindow();
    }
    
    private void loadAbsenceSelected() {
        if (moGridAbsences.getSelectedGridRow() == null) {
            moAbsence = null;
            miClient.showMsgBoxWarning(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            moAbsence = (SDbAbsence) moGridAbsences.getSelectedGridRow();
            
            moDateDateEnd.resetField();
            moIntEffectiveDays.resetField();
            
            /*
             * START of algorithm #ABS001. IMPORTANT: Please find in proyect this code to sync up any changes!
             */
            
            // estimate absence remaining-days to be consumed:

            int absenceConsumedDays = moAbsence.getActualConsumedDays(moHrsReceipt.getHrsEmployee());
            int absenceRemainingDays = moAbsence.getEffectiveDays() - absenceConsumedDays;
            
            if (absenceRemainingDays > 0) {
                Date lastConsumptionDate = absenceConsumedDays == 0 ? null : moAbsence.getActualLastConsumptionDate(moHrsReceipt.getHrsEmployee());
                Date consumptionStart = lastConsumptionDate == null ? moAbsence.getDateStart() : SLibTimeUtils.addDate(lastConsumptionDate, 0, 0, 1);

                // adjust consumption start-date, if neccesary:
                
                if (!moAbsence.consumesCalendarDays() && absenceConsumedDays > 0) { // preserve original absence start-date when there aren't previous consumptions
                    while (moHrsReceipt.getHrsPayroll().isNonWorkingDay(consumptionStart)) {
                        consumptionStart = SLibTimeUtils.addDate(consumptionStart, 0, 0, 1); // move to next day
                    }
                }

                /*
                 * END of algorithm #ABS001. IMPORTANT: Please find in proyect this code to sync up any changes!
                 */
                
                moDateLastConsumption.setValue(lastConsumptionDate);
                moDateDateStart.setValue(consumptionStart);
                
                moDateDateEnd.setEditable(true);
                moIntEffectiveDays.setEditable(true);
                
                if (absenceRemainingDays == 1) {
                    moDateDateEnd.setValue(moAbsence.getDateEnd());
                    moIntEffectiveDays.setValue(1);
                }
                
                moDateDateEnd.requestFocusInWindow();
            }
            else {
                moDateLastConsumption.resetField();
                moDateDateStart.resetField();
                
                moDateDateEnd.setEditable(false);
                moIntEffectiveDays.setEditable(false);
            }
            
            if (moAbsence.isVacation()) {
                readHrsBenefitAcummulate(moAbsence.getBenefitsAnniversary(), moAbsence.getBenefitsYear());
            }
            else {
                moIntBenefitAnniversary.resetField();
                moIntBenefitYear.resetField();
                moIntDaysToPaidTable.resetField();
                moDecDaysPayed.resetField();
            }
        }
    }
    
    private void calculateEffectiveDays() {
        int effectiveDays = 0;
        
        if (moDateDateStart.getValue() != null && moDateDateEnd.getValue() != null) {
            if (moAbsence.consumesCalendarDays()) {
                effectiveDays = SLibTimeUtils.countPeriodDays(moDateDateStart.getValue(), moDateDateEnd.getValue());
            }
            else {
                effectiveDays = SHrsUtils.countBusinessDays(moDateDateStart.getValue(), moDateDateEnd.getValue(), moHrsReceipt.getHrsPayroll().getWorkingDaySettings(), moHrsReceipt.getHrsPayroll().getHolidays());
            }
        }

        moIntEffectiveDays.setValue(effectiveDays);
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
        String msg = "";
        SGuiValidation validation = moFields.validateFields();
        
        try {
            if (validation.isValid()) {
                if (moAbsence == null) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(((TitledBorder) jpAbsences.getBorder()).getTitle()) + "'.");
                    validation.setComponent(moGridAbsences.getTable());
                }
                else if (moAbsence.isVacation()) {
                    createHrsBenefit();

                    msg = moHrsBenefit.validate(SHrsBenefit.VALID_DAYS_TO_PAY, SHrsBenefit.VALIDATION_ABSENCE_TYPE);
                    if (!msg.isEmpty()) {
                        validation.setMessage(msg);
                        validation.setComponent(moIntEffectiveDays);
                    }
                    
                    if (validation.isValid()) {
                        msg = moHrsBenefit.validate(SHrsBenefit.VALID_DAYS_TO_PAY_TOTAL, SHrsBenefit.VALIDATION_ABSENCE_TYPE);
                        
                        if (!msg.isEmpty()) {
                            if (miClient.showMsgBoxConfirm(msg + "\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.NO_OPTION) {
                                validation.setMessage(msg);
                                validation.setComponent(moIntEffectiveDays);
                            }
                        }
                    }
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
                populateAbsence();
                break;
            default:
                break;
        }
    }

    @Override
    public Object getValue(final int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void actionSave() {
        if (SGuiUtils.computeValidation(miClient, validateForm())) {
            try {
                SDbAbsenceConsumption absenceConsumption = moHrsReceipt.createAbsenceConsumption(moAbsence, moDateDateStart.getValue(), moDateDateEnd.getValue(), moIntEffectiveDays.getValue());

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
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
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
                loadAbsenceSelected();
            }
        }
    }
}

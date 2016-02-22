/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.mod.SModConsts;
import erp.mod.hrs.db.SHrsConsts;
import java.awt.BorderLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanDialogReport;
import sa.lib.gui.bean.SBeanFieldRadio;

/**
 *
 * @author Juan Barajas
 */
public class SDialogRepHrsAux extends SBeanDialogReport implements ChangeListener {
    
    protected SPanelHrsDepartaments moPanelHrsDepartaments;
    
    /**
     * Creates new form SDialogRepHrsAux
     * @param client
     * @param type
     * @param title
     */
    public SDialogRepHrsAux(SGuiClient client, int type, String title) {
        setFormSettings(client, type, SLibConsts.UNDEFINED, title);
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

        moRadGroupFilterType = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        moRadFilterTypePeriod = new sa.lib.gui.bean.SBeanFieldRadio();
        moRadFilterTypeDate = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel35 = new javax.swing.JPanel();
        jlYear = new javax.swing.JLabel();
        moIntPeriodYear = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel36 = new javax.swing.JPanel();
        jlPeriodStart = new javax.swing.JLabel();
        moIntPeriodStart = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel37 = new javax.swing.JPanel();
        jlPeriodEnd = new javax.swing.JLabel();
        moIntPeriodEnd = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel11 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moDateDateStart = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel12 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        moDateDateEnd = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel13 = new javax.swing.JPanel();
        jlEmployee = new javax.swing.JLabel();
        moKeyEmployee = new sa.lib.gui.bean.SBeanFieldKey();
        moBoolAllEmployee = new sa.lib.gui.bean.SBeanFieldBoolean();
        jPanel15 = new javax.swing.JPanel();
        jlEarningDeduction = new javax.swing.JLabel();
        moKeyEarningDeduction = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel14 = new javax.swing.JPanel();
        jlPaymentType = new javax.swing.JLabel();
        moKeyPaymentType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel3 = new javax.swing.JPanel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.GridLayout(9, 1, 0, 5));

        jPanel4.setLayout(new java.awt.BorderLayout());

        moRadGroupFilterType.add(moRadFilterTypePeriod);
        moRadFilterTypePeriod.setText("Por periodo");
        jPanel4.add(moRadFilterTypePeriod, java.awt.BorderLayout.WEST);

        moRadGroupFilterType.add(moRadFilterTypeDate);
        moRadFilterTypeDate.setText("Por rango de fechas");
        jPanel4.add(moRadFilterTypeDate, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel4);

        jPanel35.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlYear.setText("Ejercicio:*");
        jlYear.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel35.add(jlYear);

        moIntPeriodYear.setPreferredSize(new java.awt.Dimension(103, 23));
        jPanel35.add(moIntPeriodYear);

        jPanel5.add(jPanel35);

        jPanel36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPeriodStart.setText("Período inicial:*");
        jlPeriodStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel36.add(jlPeriodStart);

        moIntPeriodStart.setPreferredSize(new java.awt.Dimension(103, 23));
        jPanel36.add(moIntPeriodStart);

        jPanel5.add(jPanel36);

        jPanel37.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPeriodEnd.setText("Período final:*");
        jlPeriodEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel37.add(jlPeriodEnd);

        moIntPeriodEnd.setPreferredSize(new java.awt.Dimension(103, 23));
        jPanel37.add(moIntPeriodEnd);

        jPanel5.add(jPanel37);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial:*");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlDateStart);
        jPanel11.add(moDateDateStart);

        jPanel5.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha final:*");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlDateEnd);
        jPanel12.add(moDateDateEnd);

        jPanel5.add(jPanel12);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlEmployee.setText("Empleado:");
        jlEmployee.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel13.add(jlEmployee);

        moKeyEmployee.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel13.add(moKeyEmployee);

        moBoolAllEmployee.setText("Ver todos");
        moBoolAllEmployee.setPreferredSize(new java.awt.Dimension(75, 23));
        moBoolAllEmployee.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                moBoolAllEmployeeItemStateChanged(evt);
            }
        });
        jPanel13.add(moBoolAllEmployee);

        jPanel5.add(jPanel13);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlEarningDeduction.setText("Percepción:");
        jlEarningDeduction.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel15.add(jlEarningDeduction);

        moKeyEarningDeduction.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel15.add(moKeyEarningDeduction);

        jPanel5.add(jPanel15);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPaymentType.setText("Periodo pago:");
        jlPaymentType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlPaymentType);

        moKeyPaymentType.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel14.add(moKeyPaymentType);

        jPanel5.add(jPanel14);

        jPanel2.add(jPanel5, java.awt.BorderLayout.WEST);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void moBoolAllEmployeeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_moBoolAllEmployeeItemStateChanged
        populateEmployee();
    }//GEN-LAST:event_moBoolAllEmployeeItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlEarningDeduction;
    private javax.swing.JLabel jlEmployee;
    private javax.swing.JLabel jlPaymentType;
    private javax.swing.JLabel jlPeriodEnd;
    private javax.swing.JLabel jlPeriodStart;
    private javax.swing.JLabel jlYear;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolAllEmployee;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateStart;
    private sa.lib.gui.bean.SBeanFieldInteger moIntPeriodEnd;
    private sa.lib.gui.bean.SBeanFieldInteger moIntPeriodStart;
    private sa.lib.gui.bean.SBeanFieldInteger moIntPeriodYear;
    private sa.lib.gui.bean.SBeanFieldKey moKeyEarningDeduction;
    private sa.lib.gui.bean.SBeanFieldKey moKeyEmployee;
    private sa.lib.gui.bean.SBeanFieldKey moKeyPaymentType;
    private sa.lib.gui.bean.SBeanFieldRadio moRadFilterTypeDate;
    private sa.lib.gui.bean.SBeanFieldRadio moRadFilterTypePeriod;
    private javax.swing.ButtonGroup moRadGroupFilterType;
    // End of variables declaration//GEN-END:variables

    public void actionEnableFields() {
        if (moRadFilterTypePeriod.isSelected()) {
            moIntPeriodYear.setEnabled(true);
            moIntPeriodStart.setEnabled(true);    
            moIntPeriodEnd.setEnabled(true);
            moDateDateStart.setEnabled(false);
            moDateDateEnd.setEnabled(false);
        }
        else if (moRadFilterTypeDate.isSelected()) {
            moIntPeriodYear.setEnabled(false);
            moIntPeriodStart.setEnabled(false);    
            moIntPeriodEnd.setEnabled(false);
            moDateDateStart.setEnabled(true);
            moDateDateEnd.setEnabled(true);
        }
    }
    
    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 800, 500);
        
        moPanelHrsDepartaments = new SPanelHrsDepartaments(miClient);

        moRadFilterTypePeriod.setBooleanSettings(SGuiUtils.getLabelName(moRadFilterTypePeriod.getText()), true);
        moRadFilterTypeDate.setBooleanSettings(SGuiUtils.getLabelName(moRadFilterTypeDate.getText()), false);
        moIntPeriodYear.setIntegerSettings(SGuiUtils.getLabelName(jlYear.getText()), SGuiConsts.GUI_TYPE_INT_CAL_YEAR, true);
        moIntPeriodYear.setMinInteger(2000);
        moIntPeriodYear.setMaxInteger(2100);
        moIntPeriodStart.setIntegerSettings(SGuiUtils.getLabelName(jlPeriodStart.getText()), SGuiConsts.GUI_TYPE_INT_CAL_MONTH, true);
        moIntPeriodStart.setMaxInteger(SHrsConsts.YEAR_MONTHS);
        moIntPeriodEnd.setIntegerSettings(SGuiUtils.getLabelName(jlPeriodEnd.getText()), SGuiConsts.GUI_TYPE_INT_CAL_MONTH, true);
        moIntPeriodEnd.setMaxInteger(SHrsConsts.YEAR_MONTHS);
        moDateDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateStart.getText()), true);
        moDateDateEnd.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateEnd.getText()), true);
        moKeyEmployee.setKeySettings(miClient, SGuiUtils.getLabelName(jlEmployee.getText()), false);
        moKeyEarningDeduction.setKeySettings(miClient, SGuiUtils.getLabelName(jlEarningDeduction.getText()), false);
        moKeyPaymentType.setKeySettings(miClient, SGuiUtils.getLabelName(jlPaymentType.getText()), false);

        jPanel3.add(moPanelHrsDepartaments, BorderLayout.CENTER);

        moFields.addField(moRadFilterTypePeriod);
        moFields.addField(moRadFilterTypeDate);
        
        moFields.addField(moIntPeriodYear);
        moFields.addField(moIntPeriodStart);
        moFields.addField(moIntPeriodEnd);
        moFields.addField(moDateDateStart);
        moFields.addField(moDateDateEnd);
        moFields.addField(moKeyEmployee);
        moFields.addField(moKeyEarningDeduction);
        moFields.addField(moKeyPaymentType);

        moFields.setFormButton(jbPrint);

        moRadFilterTypePeriod.addChangeListener(this);
        moRadFilterTypeDate.addChangeListener(this);
        
        moRadFilterTypePeriod.setSelected(true);
        moDateDateStart.setValue(SLibTimeUtils.getBeginOfYear(miClient.getSession().getCurrentDate()));
        moDateDateEnd.setValue(SLibTimeUtils.getEndOfYear(miClient.getSession().getCurrentDate()));
        
        jlEarningDeduction.setText(mnFormType == SModConsts.HRSR_AUX_EAR || mnFormType == SModConsts.HRSR_AUX_EAR_EMP ? "Percepción:" : "Deducción:");
        
        moKeyEmployee.setEnabled(mnFormType == SModConsts.HRSR_AUX_EAR_EMP || mnFormType == SModConsts.HRSR_AUX_DED_EMP);
        moBoolAllEmployee.setEnabled(mnFormType == SModConsts.HRSR_AUX_EAR_EMP || mnFormType == SModConsts.HRSR_AUX_DED_EMP);
        
        reloadCatalogues();
        actionEnableFields();
    }

    private void populateEmployee() {
        if (moBoolAllEmployee.getValue()) {
            miClient.getSession().populateCatalogue(moKeyEmployee, erp.mod.SModConsts.HRSU_EMP, SLibConsts.UNDEFINED, new SGuiParams(SGuiConsts.PARAM_BPR_TP));
        }
        else {
            miClient.getSession().populateCatalogue(moKeyEmployee, erp.mod.SModConsts.HRSU_EMP, SLibConsts.UNDEFINED, null);
        }
    }

    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyEmployee, erp.mod.SModConsts.HRSU_EMP, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyEarningDeduction, mnFormType == SModConsts.HRSR_AUX_EAR || mnFormType == SModConsts.HRSR_AUX_EAR_EMP ? SModConsts.HRS_EAR : SModConsts.HRS_DED, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyPaymentType, SModConsts.HRSS_TP_PAY, SLibConsts.UNDEFINED, null);
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            if (moRadFilterTypePeriod.isSelected()) {
                if (moIntPeriodStart.getValue() > moIntPeriodEnd.getValue()) {
                    validation.setMessage(SGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + SGuiUtils.getLabelName(jlPeriodEnd.getText()) + "'" + SGuiConsts.ERR_MSG_FIELD_VAL_GREAT_EQUAL + "'" + SGuiUtils.getLabelName(jlPeriodStart.getText()) + "'.");
                    validation.setComponent(moIntPeriodEnd);
                }
            }
            else if (moRadFilterTypeDate.isSelected()) {
                validation = SGuiUtils.validateDateRange(moDateDateStart, moDateDateEnd);
            }
            
            if (validation.isValid()) {
                validation = moPanelHrsDepartaments.validatePanel();
            }
        }

        return validation;
    }

    @Override
    public void createParamsMap() {
        String sDepartamentsId = "";
        String sDepartamentsName = "";
        
        moParamsMap = miClient.createReportParams();
        
        sDepartamentsId = (String) moPanelHrsDepartaments.getValue(SGuiConsts.PARAM_KEY);
        sDepartamentsName = (String) moPanelHrsDepartaments.getValue(SGuiConsts.PARAM_ITEM);
        moParamsMap.put("sSqlWhereDepartaments", sDepartamentsId.isEmpty() ? "" : " AND dep.id_dep IN(" + sDepartamentsId + ") ");
        moParamsMap.put("sDepartaments", sDepartamentsName.isEmpty() || (boolean) moPanelHrsDepartaments.getValue(SGuiConsts.PARAM_ROWS) ? "(TODOS)" : sDepartamentsName + " ");
        
        if (moRadFilterTypePeriod.isSelected()) {
            moParamsMap.put("bByPeriod", true);
            moParamsMap.put("nPeriodYear", moIntPeriodYear.getValue());
            moParamsMap.put("nPeriodStart", moIntPeriodStart.getValue());
            moParamsMap.put("nPeriodEnd", moIntPeriodEnd.getValue());
            moParamsMap.put("sSqlWhere", " AND p.per_year >= " + moIntPeriodYear.getValue() + " AND p.per >= " + moIntPeriodStart.getValue() + " AND p.per <= " + moIntPeriodEnd.getValue() + " ");
        }
        else if (moRadFilterTypeDate.isSelected()) {
            moParamsMap.put("bByPeriod", false);
            moParamsMap.put("tDateStart", moDateDateStart.getValue());
            moParamsMap.put("tDateEnd", moDateDateEnd.getValue());
            moParamsMap.put("sSqlWhere", " AND p.dt_sta >= '" + SLibUtils.DbmsDateFormatDate.format(moDateDateStart.getValue()) + "' AND p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(moDateDateEnd.getValue()) + "' ");
        }
        
        moParamsMap.put("sTitle", mnFormType == SModConsts.HRSR_AUX_EAR_EMP ? "PERCEPCIONES POR EMPLEADO POR PERIODO" : mnFormType == SModConsts.HRSR_AUX_DED_EMP ? 
                "DEDUCCIONES POR EMPLEADO POR PERIODO" : mnFormType == SModConsts.HRSR_AUX_EAR ? "PERCEPCIONES POR PERIODO" : "DEDUCCIONES POR PERIODO");
        moParamsMap.put("bByEmployee", mnFormType == SModConsts.HRSR_AUX_EAR_EMP || mnFormType == SModConsts.HRSR_AUX_DED_EMP);
        moParamsMap.put("sEmployee", moKeyEmployee.getSelectedIndex() > 0 ? moKeyEmployee.getSelectedItem() : "(TODOS)");
        moParamsMap.put("sEarningDeduction", mnFormType == SModConsts.HRSR_AUX_EAR || mnFormType == SModConsts.HRSR_AUX_EAR_EMP ? "PERCEPCIÓN" : "DEDUCCIÓN");
        moParamsMap.put("sEarningDeductionFilter", moKeyEarningDeduction.getSelectedIndex() > 0 ? moKeyEarningDeduction.getSelectedItem() : "(TODAS)");
        moParamsMap.put("sSqlOrderBy", mnFormType == SModConsts.HRSR_AUX_EAR_EMP ? "ORDER BY bp.bp, bp.id_bp, ear.code, ear.name, ear.id_ear, dep.name, dep.id_dep, p.dt_sta " : 
                mnFormType == SModConsts.HRSR_AUX_DED_EMP ? "ORDER BY bp.bp, bp.id_bp, ded.code, ded.name, ded.id_ded, dep.name, dep.id_dep, p.dt_sta " : 
                mnFormType == SModConsts.HRSR_AUX_EAR ? "ORDER BY ear.code, ear.name, ear.id_ear, dep.name, dep.id_dep, p.dt_sta " : "ORDER BY ded.code, ded.name, ded.id_ded, dep.name, dep.id_dep, p.dt_sta ");
        
        moParamsMap.put("sSqlWhereEmployee", moKeyEmployee.getSelectedIndex() > 0 ? " AND emp.id_emp = " + moKeyEmployee.getValue()[0] : "");
        if (mnFormType == SModConsts.HRSR_AUX_EAR_EMP || mnFormType == SModConsts.HRSR_AUX_EAR) {
            moParamsMap.put("sSqlWhereEarningDeduction", moKeyEarningDeduction.getSelectedIndex() > 0 ? " AND ear.id_ear = " +  moKeyEarningDeduction.getValue()[0] : "");
        }
        else {
            moParamsMap.put("sSqlWhereEarningDeduction", moKeyEarningDeduction.getSelectedIndex() > 0 ? " AND ded.id_ded = " +  moKeyEarningDeduction.getValue()[0] : "");
        }
        moParamsMap.put("bWithEmployee", mnFormType == SModConsts.HRSR_AUX_EAR || mnFormType == SModConsts.HRSR_AUX_DED);
        moParamsMap.put("sPaymentType", moKeyPaymentType.getSelectedIndex() > 0 ? moKeyPaymentType.getSelectedItem() : "(TODOS)");
        moParamsMap.put("sSqlWherePaymentType", moKeyPaymentType.getSelectedIndex() > 0 ? " AND tp_pay.id_tp_pay = " +  moKeyPaymentType.getValue()[0] : "");
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof SBeanFieldRadio) {
            if ((SBeanFieldRadio) e.getSource() == moRadFilterTypePeriod ||
                    (SBeanFieldRadio) e.getSource() == moRadFilterTypeDate) {
                actionEnableFields();
            }
            
        }
    }
}

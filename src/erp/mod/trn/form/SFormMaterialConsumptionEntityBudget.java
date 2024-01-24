/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.form;

import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.trn.db.SDbMaterialConsumptionEntityBudget;
import java.sql.ResultSet;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;

/**
 *
 * @author Isabel Servín
 * 
 */
public class SFormMaterialConsumptionEntityBudget extends sa.lib.gui.bean.SBeanForm {

    private SDbMaterialConsumptionEntityBudget moRegistry;
    
    /**
     * Creates new form SFormCostCenterGroup
     * @param client
     * @param title
     */
    public SFormMaterialConsumptionEntityBudget(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.TRN_MAT_CONS_ENT_BUDGET, SLibConstants.UNDEFINED, title);
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
        jPanel22 = new javax.swing.JPanel();
        jlConsumption = new javax.swing.JLabel();
        moKeyConsumption = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel24 = new javax.swing.JPanel();
        jlYear = new javax.swing.JLabel();
        moCalYear = new sa.lib.gui.bean.SBeanFieldCalendarYear();
        jPanel21 = new javax.swing.JPanel();
        jlPeriod = new javax.swing.JLabel();
        moIntPeriod = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel25 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moDateStart = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel26 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        moDateEnd = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel27 = new javax.swing.JPanel();
        jlBudget = new javax.swing.JLabel();
        moDecBudget = new sa.lib.gui.bean.SBeanFieldDecimal();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setPreferredSize(new java.awt.Dimension(750, 450));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel23.setLayout(new java.awt.GridLayout(6, 1, 0, 5));

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlConsumption.setText("Centro consumo:*");
        jlConsumption.setPreferredSize(new java.awt.Dimension(140, 23));
        jPanel22.add(jlConsumption);

        moKeyConsumption.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel22.add(moKeyConsumption);

        jPanel23.add(jPanel22);

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlYear.setText("Año:");
        jlYear.setPreferredSize(new java.awt.Dimension(140, 23));
        jPanel24.add(jlYear);
        jPanel24.add(moCalYear);

        jPanel23.add(jPanel24);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPeriod.setText("Periodo:*");
        jlPeriod.setPreferredSize(new java.awt.Dimension(140, 23));
        jPanel21.add(jlPeriod);
        jPanel21.add(moIntPeriod);

        jPanel23.add(jPanel21);

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial:*");
        jlDateStart.setPreferredSize(new java.awt.Dimension(140, 23));
        jPanel25.add(jlDateStart);
        jPanel25.add(moDateStart);

        jPanel23.add(jPanel25);

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha final:*");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(140, 23));
        jPanel26.add(jlDateEnd);
        jPanel26.add(moDateEnd);

        jPanel23.add(jPanel26);

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBudget.setText("Presupuesto:*");
        jlBudget.setPreferredSize(new java.awt.Dimension(140, 23));
        jPanel27.add(jlBudget);
        jPanel27.add(moDecBudget);

        jPanel23.add(jPanel27);

        jPanel1.add(jPanel23, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel jlBudget;
    private javax.swing.JLabel jlConsumption;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlPeriod;
    private javax.swing.JLabel jlYear;
    private sa.lib.gui.bean.SBeanFieldCalendarYear moCalYear;
    private sa.lib.gui.bean.SBeanFieldDate moDateEnd;
    private sa.lib.gui.bean.SBeanFieldDate moDateStart;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecBudget;
    private sa.lib.gui.bean.SBeanFieldInteger moIntPeriod;
    private sa.lib.gui.bean.SBeanFieldKey moKeyConsumption;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 640, 400);
        
        moKeyConsumption.setKeySettings(miClient, SGuiUtils.getLabelName(jlConsumption), mbCanShowForm);
        moCalYear.setCalendarSettings(SGuiUtils.getLabelName(jlYear));
        moIntPeriod.setIntegerSettings(SGuiUtils.getLabelName(jlPeriod), SGuiConsts.GUI_TYPE_INT_CAL_YEAR, true);
        moDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateStart), true);
        moDateEnd.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateEnd), true);
        moDecBudget.setDecimalSettings(SGuiUtils.getLabelName(jlBudget), SGuiConsts.GUI_TYPE_DEC_AMT, true);
        
        moFields.addField(moCalYear);
        moFields.addField(moIntPeriod);
        moFields.addField(moDateStart);
        moFields.addField(moDateEnd);
        moFields.addField(moDecBudget);
        
        moFields.setFormButton(jbSave);
    }
    
    private boolean validatePrimaryKey() {
        boolean val = false;
        
        try {
            String sql = "SELECT * FROM trn_mat_cons_ent_budget WHERE id_mat_cons_ent = " + moKeyConsumption.getValue()[0] + " " + 
                    "AND id_year = " + moCalYear.getValue() + " AND id_period = " + moIntPeriod.getValue();
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                val = true;
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
        
        return val;
    }
    
    @Override
    public void addAllListeners() {
        
    }

    @Override
    public void removeAllListeners() {
        
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyConsumption, SModConsts.TRN_MAT_CONS_ENT, SLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbMaterialConsumptionEntityBudget) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            jtfRegistryKey.setText("");
            moDateStart.setValue(miClient.getSession().getSystemDate());
            moDateEnd.setValue(miClient.getSession().getSystemDate());
            moCalYear.setValue(miClient.getSession().getSystemYear());
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
            moDateStart.setValue(moRegistry.getDateStart());
            moDateEnd.setValue(moRegistry.getDateEnd());
            moCalYear.setValue(moRegistry.getPkYearId());
        }

        moKeyConsumption.setValue(new int[] { moRegistry.getPkMatConsumptionEntityId() });
        moIntPeriod.setValue(moRegistry.getPkPeriodId());
        moDecBudget.setValue(moRegistry.getBudget());
        
        setFormEditable(true);

        if (moRegistry.isRegistryNew()) { }

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbMaterialConsumptionEntityBudget registry = (SDbMaterialConsumptionEntityBudget) moRegistry.clone();

        if (registry.isRegistryNew()) {}
        
        moRegistry.setPkMatConsumptionEntityId(moKeyConsumption.getValue()[0]);
        moRegistry.setPkYearId(moCalYear.getValue());
        moRegistry.setPkPeriodId(moIntPeriod.getValue());
        moRegistry.setDateStart(moDateStart.getValue());
        moRegistry.setDateEnd(moDateEnd.getValue());
        moRegistry.setBudget(moDecBudget.getValue());
        
        
        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        if (validation.isValid()) {
            if (validatePrimaryKey()) {
                validation.setMessage("No es posible crear el registro debido a que ya hay un presupuesto para el centro de consumo, año y periodo indicados.");
                validation.setComponent(moKeyConsumption);
            }
        }
        
        return validation;
    }
}

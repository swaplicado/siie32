/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbCfgAccountingEmployeePackCostCenters;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;

/**
 * Configuración de los paquetes de gastos para la contabilización de empleados.
 * Aplica para la modalidad de configuración de contabilización 'dinámica'.
 * @author Sergio Flores
 */
public class SFormCfgAccountingEmployeePackCostCenters extends SBeanForm {

    private SDbCfgAccountingEmployeePackCostCenters moRegistry;

    /**
     * Creates new form SFormCfgAccountingEmployeePackCostCenters.
     * @param client GUI client.
     * @param title Form title.
     */
    public SFormCfgAccountingEmployeePackCostCenters(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.HRS_CFG_ACC_EMP_PACK_CC, 0, title);
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
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jlEmployee = new javax.swing.JLabel();
        moKeyEmployee = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel6 = new javax.swing.JPanel();
        jlPackCostCenters = new javax.swing.JLabel();
        moKeyPackCostCenters = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel13 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moDateStart = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel14 = new javax.swing.JPanel();
        jlDateEnd_n = new javax.swing.JLabel();
        moDateEnd_n = new sa.lib.gui.bean.SBeanFieldDate();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel2.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlEmployee.setForeground(java.awt.Color.blue);
        jlEmployee.setText("Empleado:*");
        jlEmployee.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlEmployee);

        moKeyEmployee.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel5.add(moKeyEmployee);

        jPanel2.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPackCostCenters.setText("Paquete CC:*");
        jlPackCostCenters.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlPackCostCenters);

        moKeyPackCostCenters.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel6.add(moKeyPackCostCenters);

        jPanel2.add(jPanel6);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Inicio vigencia:*");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel13.add(jlDateStart);
        jPanel13.add(moDateStart);

        jPanel2.add(jPanel13);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd_n.setText("Fin vigencia:");
        jlDateEnd_n.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlDateEnd_n);
        jPanel14.add(moDateEnd_n);

        jPanel2.add(jPanel14);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel jlDateEnd_n;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlEmployee;
    private javax.swing.JLabel jlPackCostCenters;
    private sa.lib.gui.bean.SBeanFieldDate moDateEnd_n;
    private sa.lib.gui.bean.SBeanFieldDate moDateStart;
    private sa.lib.gui.bean.SBeanFieldKey moKeyEmployee;
    private sa.lib.gui.bean.SBeanFieldKey moKeyPackCostCenters;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 560, 350);
        
        moKeyEmployee.setKeySettings(miClient, SGuiUtils.getLabelName(jlEmployee), true);
        moKeyPackCostCenters.setKeySettings(miClient, SGuiUtils.getLabelName(jlPackCostCenters), true);
        moDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateStart), true);
        moDateEnd_n.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateEnd_n), false);

        moFields.addField(moKeyEmployee);
        moFields.addField(moKeyPackCostCenters);
        moFields.addField(moDateStart);
        moFields.addField(moDateEnd_n);

        moFields.setFormButton(jbSave);
    }

    @Override
    public void addAllListeners() {

    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyEmployee, SModConsts.HRSU_EMP, 0, null);
        miClient.getSession().populateCatalogue(moKeyPackCostCenters, SModConsts.HRS_PACK_CC, 0, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbCfgAccountingEmployeePackCostCenters) registry;

        mnFormResult = 0;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            moRegistry.setSystem(false); // all editable registries are non-system
            
            moRegistry.setDateStart(miClient.getSession().getCurrentDate());
            
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moKeyEmployee.setValue(new int[] { moRegistry.getPkEmployeeId()});
        moKeyPackCostCenters.setValue(new int[] { moRegistry.getFkPackCostCentersId()});
        moDateStart.setValue(moRegistry.getDateStart());
        moDateEnd_n.setValue(moRegistry.getDateEnd_n());
        
        // finish setting this form:

        setFormEditable(true);
        
        if (moRegistry.isRegistryNew()) {
            moKeyEmployee.setEnabled(true);
        }
        else {
            moKeyEmployee.setEnabled(false);
        }

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbCfgAccountingEmployeePackCostCenters registry = moRegistry.clone();

        if (registry.isRegistryNew()) {
            registry.setPkEmployeeId(moKeyEmployee.getValue()[0]);
        }

        registry.setDateStart(moDateStart.getValue());
        registry.setDateEnd_n(moDateEnd_n.getValue());
        registry.setFkPackCostCentersId(moKeyPackCostCenters.getValue()[0]);

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        return validation;
    }
}

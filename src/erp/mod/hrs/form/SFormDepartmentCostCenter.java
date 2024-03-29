/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbDepartmentCostCenter;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;

/**
 *
 * @author Claudio Peña, Sergio Flores
 */
public class SFormDepartmentCostCenter extends SBeanForm {

    private SDbDepartmentCostCenter moRegistry;

    /**
     * Creates new form SFormDepartmentCostCenter
     */
    public SFormDepartmentCostCenter(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.HRS_DEP_CC, SLibConsts.UNDEFINED, title);
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
        jPanel6 = new javax.swing.JPanel();
        jlDepartment = new javax.swing.JLabel();
        moKeyDepartment = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel7 = new javax.swing.JPanel();
        jlCenterCost = new javax.swing.JLabel();
        moKeyCenterCost = new sa.lib.gui.bean.SBeanFieldKey();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDepartment.setText("Departamento:*");
        jlDepartment.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlDepartment);

        moKeyDepartment.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel6.add(moKeyDepartment);

        jPanel2.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCenterCost.setText("Centro de costo:*");
        jlCenterCost.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlCenterCost);

        moKeyCenterCost.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel7.add(moKeyCenterCost);

        jPanel2.add(jPanel7);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel jlCenterCost;
    private javax.swing.JLabel jlDepartment;
    private sa.lib.gui.bean.SBeanFieldKey moKeyCenterCost;
    private sa.lib.gui.bean.SBeanFieldKey moKeyDepartment;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 480, 300);

        moKeyDepartment.setKeySettings(miClient, SGuiUtils.getLabelName(jlDepartment), true);
        moKeyCenterCost.setKeySettings(miClient, SGuiUtils.getLabelName(jlCenterCost), true);

        moFields.addField(moKeyDepartment);
        moFields.addField(moKeyCenterCost);


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
        miClient.getSession().populateCatalogue(moKeyDepartment, SModConsts.HRSU_DEP, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyCenterCost, SModConsts.FIN_CC, SLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbDepartmentCostCenter) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            moRegistry.setSystem(false);    // all editable registries are non-system
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moKeyDepartment.setValue(new int[] { moRegistry.getPkDepartmentId() });
        moKeyCenterCost.setValue(new int[] { moRegistry.getFkCostCenterId() });

        setFormEditable(true);
        
        moKeyDepartment.setEnabled(false);
        
        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbDepartmentCostCenter registry = moRegistry.clone();

        if (registry.isRegistryNew()) { }

        registry.setPkDepartmentId(moKeyDepartment.getValue()[0]);
        registry.setFkCostCenterId(moKeyCenterCost.getValue()[0]);


        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        return validation;
    }
}

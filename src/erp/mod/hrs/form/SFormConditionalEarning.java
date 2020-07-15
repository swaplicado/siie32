/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbConditionalEarning;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;

/**
 *
 * @author Edwin Carmona
 */
public class SFormConditionalEarning extends SBeanForm implements ActionListener, ItemListener {

    private SDbConditionalEarning moRegistry;

    /**
     * Creates new form SFormAutomaticEarnings
     * @param client
     * @param title
     */
    public SFormConditionalEarning(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.HRS_COND_EAR, 0, title);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel9 = new javax.swing.JPanel();
        jpRow = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jlEarning = new javax.swing.JLabel();
        moKeyEarning = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel6 = new javax.swing.JPanel();
        jlBonus = new javax.swing.JLabel();
        moKeyBonus = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel14 = new javax.swing.JPanel();
        jlScopeId = new javax.swing.JLabel();
        moKeyScope = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel13 = new javax.swing.JPanel();
        jlReference = new javax.swing.JLabel();
        moKeyReference = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel24 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        moDateDateStart = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel27 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        moAmount = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel28 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        moPercentage = new sa.lib.gui.bean.SBeanFieldDecimal();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel9.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel9, java.awt.BorderLayout.NORTH);

        jpRow.setBorder(javax.swing.BorderFactory.createTitledBorder("Percepciones:"));
        jpRow.setLayout(new java.awt.BorderLayout());

        jPanel25.setLayout(new java.awt.GridLayout(7, 1));

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jlEarning.setText("Percepción:");
        jlEarning.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlEarning);

        moKeyEarning.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel5.add(moKeyEarning);

        jPanel25.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jlBonus.setText("Bono:");
        jlBonus.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlBonus);

        moKeyBonus.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel6.add(moKeyBonus);

        jPanel25.add(jPanel6);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlScopeId.setText("Alcance:*");
        jlScopeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlScopeId);

        moKeyScope.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel14.add(moKeyScope);

        jPanel25.add(jPanel14);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReference.setText("Referencia:*");
        jlReference.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel13.add(jlReference);

        moKeyReference.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel13.add(moKeyReference);

        jPanel25.add(jPanel13);

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jlDateStart.setText("Fecha inicio*:");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel24.add(jlDateStart);

        moDateDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel24.add(moDateDateStart);

        jPanel25.add(jPanel24);

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabel2.setText("Monto:");
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel27.add(jLabel2);
        jPanel27.add(moAmount);

        jPanel25.add(jPanel27);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabel3.setText("Pocentaje:");
        jLabel3.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel28.add(jLabel3);
        jPanel28.add(moPercentage);

        jPanel25.add(jPanel28);

        jpRow.add(jPanel25, java.awt.BorderLayout.NORTH);

        getContentPane().add(jpRow, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        super.windowActivated();
    }//GEN-LAST:event_formWindowActivated

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel jlBonus;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlEarning;
    private javax.swing.JLabel jlReference;
    private javax.swing.JLabel jlScopeId;
    private javax.swing.JPanel jpRow;
    private sa.lib.gui.bean.SBeanFieldDecimal moAmount;
    private sa.lib.gui.bean.SBeanFieldDate moDateDateStart;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBonus;
    private sa.lib.gui.bean.SBeanFieldKey moKeyEarning;
    private sa.lib.gui.bean.SBeanFieldKey moKeyReference;
    private sa.lib.gui.bean.SBeanFieldKey moKeyScope;
    private sa.lib.gui.bean.SBeanFieldDecimal moPercentage;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 660, 400);

        moKeyEarning.setKeySettings(miClient, SGuiUtils.getLabelName(jlEarning.getText()), true);
        moKeyBonus.setKeySettings(miClient, SGuiUtils.getLabelName(jlBonus.getText()), true);
        moKeyScope.setKeySettings(miClient, SGuiUtils.getLabelName(jlScopeId.getText()), true);
        moKeyReference.setKeySettings(miClient, SGuiUtils.getLabelName(jlReference.getText()), true);
        moDateDateStart.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateStart), false);

        moFields.addField(moKeyEarning);
        moFields.addField(moKeyBonus);
        moFields.addField(moKeyScope);
        moFields.addField(moKeyReference);
        moFields.addField(moDateDateStart);
    }

    private void itemStateChangedScope() {
        switch (moKeyScope.getSelectedItem().getPrimaryKey()[0]) {
            case 2:
                miClient.getSession().populateCatalogue(moKeyReference, SModConsts.HRSU_DEP, SLibConsts.UNDEFINED, null);
                break;
            case 3:
                miClient.getSession().populateCatalogue(moKeyReference, SModConsts.HRSU_EMP, SLibConsts.UNDEFINED, null);
                break;
            default:
                moKeyReference.setSelectedItem(new int[] { 0 });
                moKeyReference.setEnabled(false);
                break;
        }
    }
    
    private void resetFields() {
        moDateDateStart.setValue(SLibTimeUtils.getBeginOfMonth(miClient.getSession().getCurrentDate()));
        moKeyScope.setSelectedItem(null);
        moKeyEarning.setValue(null);
        moKeyBonus.setValue(null);
        moAmount.setValue(0d);
        moPercentage.setValue(0d);
    }
    
    @Override
    public void addAllListeners() {
        moKeyScope.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        moKeyScope.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        try {
            miClient.getSession().populateCatalogue(moKeyEarning, SModConsts.HRS_EAR, 0, null);
            miClient.getSession().populateCatalogue(moKeyBonus, SModConsts.HRSS_BONUS, 0, null);
            miClient.getSession().populateCatalogue(moKeyScope, SModConsts.HRSS_TP_ACC, SLibConsts.UNDEFINED, null);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbConditionalEarning) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;
        removeAllListeners();
        resetFields();
        reloadCatalogues();
        
        if (moRegistry.isRegistryNew()) {
            moKeyScope.setValue(new int[] { 2 });
        }
        else {
            moKeyEarning.setValue(new int[] { moRegistry.getFkEarningId()});
            moKeyBonus.setValue(new int[] { moRegistry.getFkBonusId()});
            moKeyScope.setValue(new int[] { moRegistry.getFkScopeId() });
            moDateDateStart.setValue(moRegistry.getStartDate());
            moAmount.setValue(moRegistry.getAmount());
            moPercentage.setValue(moRegistry.getPercentage());
        }
        
        itemStateChangedScope();
        moKeyReference.setValue(new int[] { moRegistry.getFkReferenceId() });
        
        jtfRegistryKey.setText(moRegistry.getPrimaryKey()[0] + "");

        setFormEditable(true);

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbConditionalEarning registry = moRegistry.clone();

        if (registry.isRegistryNew()) {}
        
        registry.setAmount(moAmount.getValue());
        registry.setPercentage(moPercentage.getValue());
        registry.setFkEarningId(moKeyEarning.getValue()[0]);
        registry.setFkBonusId(moKeyBonus.getValue()[0]);
        registry.setFkScopeId(moKeyScope.getValue()[0]);
        registry.setFkReferenceId(moKeyReference.getValue().length == 0 ? 0 : moKeyReference.getValue()[0]);
        registry.setStartDate(moDateDateStart.getValue());
        
        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
        }
        else if (e.getSource() instanceof JTextField) {
            JTextField textField = (JTextField) e.getSource();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox && e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox comboBox = (JComboBox)  e.getSource();

            if (comboBox == moKeyScope) {
                itemStateChangedScope();
            }
        }
    }
}

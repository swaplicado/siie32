/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.data.SDataConstantsSys;
import erp.gui.account.SAccount;
import erp.gui.account.SAccountConsts;
import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbExpenseTypeAccount;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFieldKey;
import sa.lib.gui.bean.SBeanForm;

/**
 * Configuración para definir la cuenta contable base de los tipos de gasto.
 * Aplica para la modalidad de configuración de contabilización 'dinámica'.
 * @author Sergio Flores
 */
public class SFormExpenseTypeAccount extends SBeanForm implements ItemListener {

    private SDbExpenseTypeAccount moRegistry;

    /**
     * Creates new form SFormExpenseTypeAccount.
     * @param client GUI client.
     * @param title Form title.
     */
    public SFormExpenseTypeAccount(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.HRS_TP_EXP_ACC, 0, title);
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlExpenseType = new javax.swing.JLabel();
        moKeyExpenseType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel10 = new javax.swing.JPanel();
        moPanelAccount = new erp.gui.account.SBeanPanelAccount();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel12.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlExpenseType.setForeground(java.awt.Color.blue);
        jlExpenseType.setText("Tipo gasto:*");
        jlExpenseType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlExpenseType);

        moKeyExpenseType.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel6.add(moKeyExpenseType);

        jPanel12.add(jPanel6);

        jPanel2.add(jPanel12, java.awt.BorderLayout.NORTH);

        jPanel10.setLayout(new java.awt.BorderLayout());
        jPanel10.add(moPanelAccount, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel10, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel jlExpenseType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyExpenseType;
    private erp.gui.account.SBeanPanelAccount moPanelAccount;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 640, 400);

        moKeyExpenseType.setKeySettings(miClient, SGuiUtils.getLabelName(jlExpenseType), true);
        moPanelAccount.setPanelSettings((SGuiClient) miClient, SAccountConsts.TYPE_ACCOUNT, true, true, true);

        moPanelAccount.setAccountNameWidth(500);

        moPanelAccount.setComponentPrevious(moKeyExpenseType);
        moPanelAccount.setComponentNext(jbSave);
        
        moPanelAccount.initPanel();
        
        moFields.addField(moKeyExpenseType);

        moFields.setFormButton(jbSave);
    }
    
    private void updateAccountComplements() {
        boolean enable = moKeyExpenseType.getSelectedIndex() > 0;
        
        moPanelAccount.setPanelEditable(enable);
    }
    
    private void itemStateChangedTypeExpense() {
        moPanelAccount.setPanelEditable(false);
        moPanelAccount.setSelectedAccount(new SAccount(moRegistry.getFkAccountId(), (String) miClient.getSession().readField(SModConsts.FIN_ACC, new int[] { moRegistry.getFkAccountId() }, SDbRegistry.FIELD_CODE), "", false, 0, 0));
            
        updateAccountComplements();
    }
    
    @Override
    public void addAllListeners() {
        moKeyExpenseType.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        moKeyExpenseType.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyExpenseType, SModConsts.HRSU_TP_EXP, 0, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbExpenseTypeAccount) registry;
        
        mnFormResult = 0;
        mbFirstActivation = true;
        
        removeAllListeners();
        reloadCatalogues();
        
        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            
            moRegistry.setFkAccountId(SDataConstantsSys.NA);
            
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }
        
        moKeyExpenseType.setValue(new int[] { moRegistry.getPkExpenseTypeId()});
        itemStateChangedTypeExpense();
        moPanelAccount.setSelectedAccount(new SAccount(moRegistry.getFkAccountId(), (String) miClient.getSession().readField(SModConsts.FIN_ACC, new int[] { moRegistry.getFkAccountId() }, SDbRegistry.FIELD_CODE), "", false, 0, 0));
        
        setFormEditable(true);
        updateAccountComplements();
        
        if (moRegistry.isRegistryNew()) {
            moKeyExpenseType.setEnabled(true);
        }
        else {
            moKeyExpenseType.setEnabled(false);
        }
        
        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbExpenseTypeAccount registry = moRegistry.clone();

        if (registry.isRegistryNew()) {
            registry.setPkExpenseTypeId(moKeyExpenseType.getValue()[0]);
        }
        
        registry.setFkAccountId(moPanelAccount.getSelectedAccount() != null ? moPanelAccount.getSelectedAccount().getAccountId() : SDataConstantsSys.NA);

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            if (moRegistry.isRegistryNew()) {
                try {
                    int id = moKeyExpenseType.getValue()[0]; // convenience variable
                    
                    if (SDbExpenseTypeAccount.countExistingRegistries(miClient.getSession(), id) > 0) {
                        throw new Exception("Ya existe un registro para el tipo de gasto '" + miClient.getSession().readField(SModConsts.HRSU_TP_EXP, new int[] { id }, SDbRegistry.FIELD_NAME) + "'."
                                + "\nSi no visualiza el registro existente en la vista, busque entre los registros eliminados.");
                    }
                }
                catch (Exception e) {
                    validation.setMessage(e.getMessage());
                    validation.setComponent(moKeyExpenseType);
                }
            }
            
            if (validation.isValid()) {
                validation = moPanelAccount.validatePanel();
            }
        }
            
        return validation;
    }
    
    @Override
    public void setValue(final int type, final Object value) {
        switch (type) {
            case SModConsts.HRSU_TP_EXP:
                moKeyExpenseType.setValue(new int[] { (int) value });
                moKeyExpenseType.setEnabled(false);
                break;
                
            default:
                // nothing
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof SBeanFieldKey && e.getStateChange() == ItemEvent.SELECTED) {
            SBeanFieldKey field = (SBeanFieldKey) e.getSource();
            
            if (field == moKeyExpenseType) {
                itemStateChangedTypeExpense();
            }
        }
    }
}
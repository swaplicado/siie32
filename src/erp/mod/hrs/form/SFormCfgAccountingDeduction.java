/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.data.SDataConstantsSys;
import erp.gui.account.SAccount;
import erp.gui.account.SAccountConsts;
import erp.mod.SModConsts;
import erp.mod.SModDataConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbCfgAccountingDeduction;
import erp.mod.hrs.db.SDbDeduction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiOptionPicker;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFieldKey;
import sa.lib.gui.bean.SBeanForm;

/**
 * Configuración de la contabilización de las percepciones que lo requieran.
 * Aplica para la modalidad de configuración de contabilización 'dinámica'.
 * @author Sergio Flores
 */
public class SFormCfgAccountingDeduction extends SBeanForm implements ActionListener, ItemListener {

    private SDbCfgAccountingDeduction moRegistry;
    private SDbDeduction moDeduction;

    /**
     * Creates new form SFormCfgAccountingDeduction.
     * @param client GUI client.
     * @param title Form title.
     */
    public SFormCfgAccountingDeduction(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.HRS_CFG_ACC_DED, 0, title);
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
        jlDeduction = new javax.swing.JLabel();
        moKeyDeduction = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel8 = new javax.swing.JPanel();
        jlAccountingRecordType = new javax.swing.JLabel();
        jtfAccountingRecordType = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jlPackExpenses = new javax.swing.JLabel();
        moKeyPackExpenses = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel3 = new javax.swing.JPanel();
        jlAccountingRecordTypeHelp = new javax.swing.JLabel();
        jlAccountingRecordTypeHelpText = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        moPanelAccount = new erp.gui.account.SBeanPanelAccount();
        jPanel14 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jlPackCostCenters = new javax.swing.JLabel();
        moKeyPackCostCenters = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel5 = new javax.swing.JPanel();
        jlBizPartner = new javax.swing.JLabel();
        moKeyBizPartner = new sa.lib.gui.bean.SBeanFieldKey();
        jbPickBizPartner = new javax.swing.JButton();
        jbClearBizPartner = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jlTax = new javax.swing.JLabel();
        moKeyTax = new sa.lib.gui.bean.SBeanFieldKey();
        jbPickTax = new javax.swing.JButton();
        jbClearTax = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel12.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDeduction.setForeground(java.awt.Color.blue);
        jlDeduction.setText("Deducción:*");
        jlDeduction.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlDeduction);

        moKeyDeduction.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel6.add(moKeyDeduction);

        jPanel12.add(jPanel6);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAccountingRecordType.setText("Registro contable:");
        jlAccountingRecordType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlAccountingRecordType);

        jtfAccountingRecordType.setEditable(false);
        jtfAccountingRecordType.setFocusable(false);
        jtfAccountingRecordType.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel8.add(jtfAccountingRecordType);

        jPanel12.add(jPanel8);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPackExpenses.setText("Paquete gastos:*");
        jlPackExpenses.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlPackExpenses);

        moKeyPackExpenses.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel4.add(moKeyPackExpenses);

        jPanel12.add(jPanel4);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAccountingRecordTypeHelp.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlAccountingRecordTypeHelp);

        jlAccountingRecordTypeHelpText.setPreferredSize(new java.awt.Dimension(500, 23));
        jPanel3.add(jlAccountingRecordTypeHelpText);

        jPanel12.add(jPanel3);

        jPanel2.add(jPanel12, java.awt.BorderLayout.NORTH);

        jPanel10.setLayout(new java.awt.BorderLayout());
        jPanel10.add(moPanelAccount, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel10, java.awt.BorderLayout.CENTER);

        jPanel14.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPackCostCenters.setText("Paquete CC:*");
        jlPackCostCenters.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlPackCostCenters);

        moKeyPackCostCenters.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel7.add(moKeyPackCostCenters);

        jPanel14.add(jPanel7);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner.setText("Asociado negocios:");
        jlBizPartner.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlBizPartner);

        moKeyBizPartner.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel5.add(moKeyBizPartner);

        jbPickBizPartner.setText("...");
        jbPickBizPartner.setToolTipText("Seleccionar asociado de negocios");
        jbPickBizPartner.setFocusable(false);
        jbPickBizPartner.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbPickBizPartner);

        jbClearBizPartner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/gui/img/icon_del.gif"))); // NOI18N
        jbClearBizPartner.setToolTipText("Limpiar asociado de negocios");
        jbClearBizPartner.setFocusable(false);
        jbClearBizPartner.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbClearBizPartner);

        jPanel14.add(jPanel5);

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTax.setText("Impuesto:");
        jlTax.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel19.add(jlTax);

        moKeyTax.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel19.add(moKeyTax);

        jbPickTax.setText("...");
        jbPickTax.setToolTipText("Seleccionar impuesto");
        jbPickTax.setFocusable(false);
        jbPickTax.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel19.add(jbPickTax);

        jbClearTax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/gui/img/icon_del.gif"))); // NOI18N
        jbClearTax.setToolTipText("Limpiar impuesto");
        jbClearTax.setFocusable(false);
        jbClearTax.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel19.add(jbClearTax);

        jPanel14.add(jPanel19);

        jPanel2.add(jPanel14, java.awt.BorderLayout.SOUTH);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbClearBizPartner;
    private javax.swing.JButton jbClearTax;
    private javax.swing.JButton jbPickBizPartner;
    private javax.swing.JButton jbPickTax;
    private javax.swing.JLabel jlAccountingRecordType;
    private javax.swing.JLabel jlAccountingRecordTypeHelp;
    private javax.swing.JLabel jlAccountingRecordTypeHelpText;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlDeduction;
    private javax.swing.JLabel jlPackCostCenters;
    private javax.swing.JLabel jlPackExpenses;
    private javax.swing.JLabel jlTax;
    private javax.swing.JTextField jtfAccountingRecordType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBizPartner;
    private sa.lib.gui.bean.SBeanFieldKey moKeyDeduction;
    private sa.lib.gui.bean.SBeanFieldKey moKeyPackCostCenters;
    private sa.lib.gui.bean.SBeanFieldKey moKeyPackExpenses;
    private sa.lib.gui.bean.SBeanFieldKey moKeyTax;
    private erp.gui.account.SBeanPanelAccount moPanelAccount;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 640, 400);

        moKeyDeduction.setKeySettings(miClient, SGuiUtils.getLabelName(jlDeduction), true);
        moKeyPackExpenses.setKeySettings(miClient, SGuiUtils.getLabelName(jlPackExpenses), true);
        moPanelAccount.setPanelSettings((SGuiClient) miClient, SAccountConsts.TYPE_ACCOUNT, false, true, true);
        moKeyPackCostCenters.setKeySettings(miClient, SGuiUtils.getLabelName(jlPackCostCenters), true);
        moKeyBizPartner.setKeySettings(miClient, SGuiUtils.getLabelName(jlBizPartner), false);
        moKeyTax.setKeySettings(miClient, SGuiUtils.getLabelName(jlTax), false);

        moPanelAccount.setAccountNameWidth(500);

        moPanelAccount.setComponentPrevious(moKeyPackExpenses);
        moPanelAccount.setComponentNext(moKeyPackCostCenters, moKeyBizPartner);
        
        moPanelAccount.initPanel();
        
        moFields.addField(moKeyDeduction);
        moFields.addField(moKeyPackExpenses);
        moFields.addField(moKeyPackCostCenters);
        moFields.addField(moKeyBizPartner);
        moFields.addField(moKeyTax);

        moFields.setFormButton(jbSave);
    }
    
    private void actionPickBizPartner() {
        SGuiOptionPicker picker = miClient.getSession().getModule(SModConsts.MOD_BPS_N).getOptionPicker(SModConsts.BPSU_BP, 0, null);
        picker.resetPicker();
        picker.setPickerVisible(true);

        if (picker.getPickerResult() == SGuiConsts.FORM_RESULT_OK) {
            int[] key = (int[]) picker.getOption();

            if (key != null) {
                if (key[0] != 0) {
                    moKeyBizPartner.setValue(new int[] { key[0] });
                    moKeyBizPartner.requestFocusInWindow();
                }
            }
        }
    }
    
    private void actionPickTax() {
        SGuiOptionPicker picker = miClient.getSession().getModule(SModConsts.MOD_FIN_N).getOptionPicker(SModConsts.FINU_TAX, 0, null);
        picker.resetPicker();
        picker.setPickerVisible(true);

        if (picker.getPickerResult() == SGuiConsts.FORM_RESULT_OK) {
            int[] key = (int[]) picker.getOption();

            if (key != null) {
                if (key[0] != 0) {
                    moKeyTax.setValue(new int[] { key[0], key[1] });
                    moKeyTax.requestFocusInWindow();
                }
            }
        }
    }
    
    private void actionClearBizPartner() {
        moKeyBizPartner.setSelectedIndex(0);
        moKeyBizPartner.requestFocus();
    }

    private void actionClearTax() {
        moKeyTax.setSelectedIndex(0);
        moKeyTax.requestFocus();
    }
    
    private void updateAccountComplements() {
        boolean enable = moDeduction != null && moDeduction.getFkAccountingRecordTypeId() == SModSysConsts.HRSS_TP_ACC_GBL;
        
        moPanelAccount.setPanelEditable(true);
        moKeyPackCostCenters.setEnabled(enable);
        moKeyBizPartner.setEnabled(true);
        jbPickBizPartner.setEnabled(true);
        jbClearBizPartner.setEnabled(true);
        moKeyTax.setEnabled(true);
        jbPickTax.setEnabled(true);
        jbClearTax.setEnabled(true);
    }
    
    private void itemStateChangedDeduction() {
        moDeduction = null;
        
        moPanelAccount.setPanelEditable(false);
        moPanelAccount.setSelectedAccount(new SAccount(moRegistry.getFkAccountId(), (String) miClient.getSession().readField(SModConsts.FIN_ACC, new int[] { moRegistry.getFkAccountId() }, SDbRegistry.FIELD_CODE), "", false, 0, 0));
        jtfAccountingRecordType.setText("");
        jlAccountingRecordTypeHelpText.setText("");
        moKeyPackCostCenters.setEnabled(false);
        moKeyPackCostCenters.setValue(new int[] { SDataConstantsSys.NA });
        moKeyBizPartner.setEnabled(false);
        moKeyBizPartner.resetField();
        jbPickBizPartner.setEnabled(false);
        jbClearBizPartner.setEnabled(false);
        moKeyTax.setEnabled(false);
        moKeyTax.resetField();
        jbPickTax.setEnabled(false);
        jbClearTax.setEnabled(false);
        
        if (moKeyDeduction.getSelectedIndex() > 0) {
            moDeduction = (SDbDeduction) miClient.getSession().readRegistry(SModConsts.HRS_DED, moKeyDeduction.getValue());
        }
        
        if (moDeduction != null) {
            jtfAccountingRecordType.setText((String) miClient.getSession().readField(SModConsts.HRSS_TP_ACC, new int[] { moDeduction.getFkAccountingRecordTypeId() }, SDbRegistry.FIELD_NAME));
            jtfAccountingRecordType.setCaretPosition(0);
            
            if (moDeduction.getFkAccountingRecordTypeId() == SModSysConsts.HRSS_TP_ACC_GBL) {
                jlAccountingRecordTypeHelpText.setText("NOTA: ¡Se debe especificar una cuenta contable para contabilizar esta deducción!");
            }
            else {
                jlAccountingRecordTypeHelpText.setText("NOTA: Si se requiere, se puede especificar una cuenta contable para contabilizar esta deducción.");
            }
            
            updateAccountComplements();
        }
    }
    
    @Override
    public void addAllListeners() {
        jbPickBizPartner.addActionListener(this);
        jbPickTax.addActionListener(this);
        jbClearBizPartner.addActionListener(this);
        jbClearTax.addActionListener(this);
        moKeyDeduction.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbPickBizPartner.removeActionListener(this);
        jbPickTax.removeActionListener(this);
        jbClearBizPartner.removeActionListener(this);
        jbClearTax.removeActionListener(this);
        moKeyDeduction.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyDeduction, SModConsts.HRS_DED, 0, null);
        miClient.getSession().populateCatalogue(moKeyPackCostCenters, SModConsts.HRS_PACK_CC, SModDataConsts.OPC_ALL, null);
        miClient.getSession().populateCatalogue(moKeyPackExpenses, SModConsts.HRSU_PACK_EXP, SModDataConsts.OPC_ALL, null);
        miClient.getSession().populateCatalogue(moKeyBizPartner, SModConsts.BPSU_BP, 0, null);
        miClient.getSession().populateCatalogue(moKeyTax, SModConsts.FINU_TAX, 0, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbCfgAccountingDeduction) registry;
        
        mnFormResult = 0;
        mbFirstActivation = true;
        
        removeAllListeners();
        reloadCatalogues();
        
        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            
            moRegistry.setFkAccountId(SDataConstantsSys.NA);
            moRegistry.setFkPackExpensesId(SDataConstantsSys.NA);
            moRegistry.setFkPackCostCentersId(SDataConstantsSys.NA);
            
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }
        
        moKeyDeduction.setValue(new int[] { moRegistry.getPkDeductionId() });
        itemStateChangedDeduction();
        moKeyPackCostCenters.setValue(new int[] { moRegistry.getFkPackCostCentersId() });
        moPanelAccount.setSelectedAccount(new SAccount(moRegistry.getFkAccountId(), (String) miClient.getSession().readField(SModConsts.FIN_ACC, new int[] { moRegistry.getFkAccountId() }, SDbRegistry.FIELD_CODE), "", false, 0, 0));
        moKeyPackExpenses.setValue(new int[] { moRegistry.getFkPackExpensesId() });
        moKeyBizPartner.setValue(new int[] { moRegistry.getFkBizPartnerId_n() });
        moKeyTax.setValue(new int[] { moRegistry.getFkTaxBasicId_n(), moRegistry.getFkTaxTaxId_n() });
        
        setFormEditable(true);
        updateAccountComplements();
        
        if (moRegistry.isRegistryNew()) {
            moKeyDeduction.setEnabled(true);
        }
        else {
            moKeyDeduction.setEnabled(false);
        }
        
        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbCfgAccountingDeduction registry = moRegistry.clone();

        if (registry.isRegistryNew()) {
            registry.setPkDeductionId(moKeyDeduction.getValue()[0]);
        }
        
        registry.setFkAccountId(moPanelAccount.getSelectedAccount() != null ? moPanelAccount.getSelectedAccount().getAccountId() : SDataConstantsSys.NA);
        registry.setFkBizPartnerId_n(moKeyBizPartner.getSelectedIndex() == 0 ? 0 : moKeyBizPartner.getValue()[0]);
        registry.setFkTaxBasicId_n(moKeyTax.getSelectedIndex() == 0 ? 0 : moKeyTax.getValue()[0]);
        registry.setFkTaxTaxId_n(moKeyTax.getSelectedIndex() == 0 ? 0 : moKeyTax.getValue()[1]);
        registry.setFkPackExpensesId(moKeyPackExpenses.getValue()[0]);
        registry.setFkPackCostCentersId(moKeyPackCostCenters.getValue()[0]);
        registry.setFkAccountingRecordTypeId(moDeduction.getFkAccountingRecordTypeId());

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            if (moRegistry.isRegistryNew()) {
                try {
                    int id = moKeyDeduction.getValue()[0]; // convenience variable
                    
                    if (SDbCfgAccountingDeduction.countExistingRegistries(miClient.getSession(), id) > 0) {
                        throw new Exception("Ya existe un registro para la deducción '" + miClient.getSession().readField(SModConsts.HRS_DED, new int[] { id }, SDbRegistry.FIELD_NAME) + "'."
                                + "\nSi no visualiza el registro existente en la vista, busque entre los registros eliminados.");
                    }
                }
                catch (Exception e) {
                    validation.setMessage(e.getMessage());
                    validation.setComponent(moKeyDeduction);
                }
            }
            
            if (validation.isValid()) {
                validation = moPanelAccount.validatePanel();
            }
            
            if (validation.isValid()) {
                boolean isAccountNonSet = moPanelAccount.getSelectedAccount() == null || moPanelAccount.getSelectedAccount().getAccountId() == SDataConstantsSys.NA;
                
                if (isAccountNonSet) {
                    if (moKeyPackCostCenters.getSelectedIndex() > 0 && moKeyPackCostCenters.getValue()[0] != SModSysConsts.HRS_PACK_CC_NA) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ_NOT + "'" + moKeyPackCostCenters.getFieldName() + "'.");
                        validation.setComponent(moKeyPackCostCenters);
                    }
                    else if (moKeyBizPartner.getSelectedIndex() > 0) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ_NOT + "'" + moKeyBizPartner.getFieldName() + "'.");
                        validation.setComponent(moKeyBizPartner);
                    }
                    else if (moKeyTax.getSelectedIndex() > 0) {
                        validation.setMessage(SGuiConsts.ERR_MSG_FIELD_REQ_NOT + "'" + moKeyTax.getFieldName() + "'.");
                        validation.setComponent(moKeyTax);
                    }
                }
            }
        }
            
        return validation;
    }

    @Override
    public void setValue(final int type, final Object value) {
        switch (type) {
            case SModConsts.HRS_CFG_ACC_DED:
                moKeyDeduction.setValue(new int[] { (int) value });
                moKeyDeduction.setEnabled(false);
                break;
                
            default:
                // nothing
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPickBizPartner) {
                actionPickBizPartner();
            }
            else if (button == jbPickTax) {
                actionPickTax();
            }
            else if (button == jbClearBizPartner) {
                actionClearBizPartner();
            }
            else if (button == jbClearTax) {
                actionClearTax();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof SBeanFieldKey && e.getStateChange() == ItemEvent.SELECTED) {
            SBeanFieldKey field = (SBeanFieldKey) e.getSource();
            
            if (field == moKeyDeduction) {
                itemStateChangedDeduction();
            }
        }
    }
}

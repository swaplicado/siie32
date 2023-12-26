/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.form;

import erp.gui.account.SAccount;
import erp.gui.account.SAccountConsts;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbAccountingDeduction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiOptionPicker;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;

/**
 * Configuración de la contabilización de deducciones según su nivel de configuración.
 * Aplica para la modalidad de configuración de contabilización 'original'.
 * @author Juan Barajas, Sergio Flores
 */
public class SFormAccountingDeduction extends SBeanForm implements ActionListener {

    private SDbAccountingDeduction moRegistry;

    /**
     * Creates new form SFormAccountingDeduction
     */
    public SFormAccountingDeduction(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.HRS_ACC_DED, SLibConsts.UNDEFINED, title);
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
        jPanel9 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlDeduction = new javax.swing.JLabel();
        moTextDeduction = new sa.lib.gui.bean.SBeanFieldText();
        jPanel4 = new javax.swing.JPanel();
        jlReference = new javax.swing.JLabel();
        moTextReference = new sa.lib.gui.bean.SBeanFieldText();
        jPanel10 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        moPanelAccount = new erp.gui.account.SBeanPanelAccount();
        jPanel3 = new javax.swing.JPanel();
        moPanelCostCenter = new erp.gui.account.SBeanPanelAccount();
        jPanel11 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jlBizPartner = new javax.swing.JLabel();
        moKeyBizPartner = new sa.lib.gui.bean.SBeanFieldKey();
        jbPickBizPartner = new javax.swing.JButton();
        jbClearBizPartner = new javax.swing.JButton();
        jbSameEmploye = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jlItem = new javax.swing.JLabel();
        moKeyItem = new sa.lib.gui.bean.SBeanFieldKey();
        jbPickItem = new javax.swing.JButton();
        jbClearItem = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jlTax = new javax.swing.JLabel();
        moKeyTax = new sa.lib.gui.bean.SBeanFieldKey();
        jbPickTax = new javax.swing.JButton();
        jbClearTax = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel12.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDeduction.setText("Deducción:");
        jlDeduction.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlDeduction);

        moTextDeduction.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel6.add(moTextDeduction);

        jPanel12.add(jPanel6);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReference.setText("Referencia:");
        jlReference.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlReference);

        moTextReference.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel4.add(moTextReference);

        jPanel12.add(jPanel4);

        jPanel9.add(jPanel12, java.awt.BorderLayout.PAGE_START);

        jPanel2.add(jPanel9, java.awt.BorderLayout.NORTH);

        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel13.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel8.setLayout(new java.awt.BorderLayout());
        jPanel8.add(moPanelAccount, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel8);

        jPanel3.setLayout(new java.awt.BorderLayout());
        jPanel3.add(moPanelCostCenter, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel3);

        jPanel10.add(jPanel13, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel10, java.awt.BorderLayout.CENTER);

        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel14.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

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

        jbSameEmploye.setText("Mismo empleado");
        jbSameEmploye.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jbSameEmploye.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jbSameEmploye);

        jPanel14.add(jPanel5);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItem.setText("Ítem:");
        jlItem.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlItem);

        moKeyItem.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel7.add(moKeyItem);

        jbPickItem.setText("...");
        jbPickItem.setToolTipText("Seleccionar ítem");
        jbPickItem.setFocusable(false);
        jbPickItem.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel7.add(jbPickItem);

        jbClearItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/gui/img/icon_del.gif"))); // NOI18N
        jbClearItem.setToolTipText("Limpiar ítem");
        jbClearItem.setFocusable(false);
        jbClearItem.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel7.add(jbClearItem);

        jPanel14.add(jPanel7);

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

        jPanel11.add(jPanel14, java.awt.BorderLayout.PAGE_START);

        jPanel2.add(jPanel11, java.awt.BorderLayout.SOUTH);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbClearBizPartner;
    private javax.swing.JButton jbClearItem;
    private javax.swing.JButton jbClearTax;
    private javax.swing.JButton jbPickBizPartner;
    private javax.swing.JButton jbPickItem;
    private javax.swing.JButton jbPickTax;
    private javax.swing.JButton jbSameEmploye;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlDeduction;
    private javax.swing.JLabel jlItem;
    private javax.swing.JLabel jlReference;
    private javax.swing.JLabel jlTax;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBizPartner;
    private sa.lib.gui.bean.SBeanFieldKey moKeyItem;
    private sa.lib.gui.bean.SBeanFieldKey moKeyTax;
    private erp.gui.account.SBeanPanelAccount moPanelAccount;
    private erp.gui.account.SBeanPanelAccount moPanelCostCenter;
    private sa.lib.gui.bean.SBeanFieldText moTextDeduction;
    private sa.lib.gui.bean.SBeanFieldText moTextReference;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 640, 400);

        moTextDeduction.setTextSettings(SGuiUtils.getLabelName(jlDeduction), 10);
        moTextReference.setTextSettings(SGuiUtils.getLabelName(jlReference), 100);
        moPanelAccount.setPanelSettings((SGuiClient) miClient, SAccountConsts.TYPE_ACCOUNT, true, true, true);
        moPanelCostCenter.setPanelSettings((SGuiClient) miClient, SAccountConsts.TYPE_COST_CENTER, false, true, true);
        moKeyBizPartner.setKeySettings(miClient, SGuiUtils.getLabelName(jlBizPartner), false);
        moKeyItem.setKeySettings(miClient, SGuiUtils.getLabelName(jlItem), false);
        moKeyTax.setKeySettings(miClient, SGuiUtils.getLabelName(jlTax), false);

        moPanelAccount.setAccountNameWidth(500);
        moPanelCostCenter.setAccountNameWidth(500);

        moPanelAccount.setComponentPrevious(moTextReference);
        moPanelAccount.setComponentNext(moPanelCostCenter.getTextNumberFirst());
        moPanelCostCenter.setComponentPrevious(moPanelAccount.getTextNumberFirst());
        moPanelCostCenter.setComponentNext(moKeyBizPartner);
        
        moPanelAccount.initPanel();
        moPanelCostCenter.initPanel();
        
        moFields.addField(moTextDeduction);
        moFields.addField(moTextReference);
        moFields.addField(moKeyBizPartner);
        moFields.addField(moKeyItem);
        moFields.addField(moKeyTax);

        moFields.setFormButton(jbSave);
    }
    
    @Override
    protected void windowActivated() {
        if (mbFirstActivation) {
            mbFirstActivation = false;
            moPanelAccount.getTextNumberFirst().requestFocus();
        }
    }

    private void actionPickBizPartner() {
        int[] key = null;
        SGuiOptionPicker picker = null;

        picker = miClient.getSession().getModule(SModConsts.MOD_BPS_N).getOptionPicker(SModConsts.BPSU_BP, 0, null);
        picker.resetPicker();
        picker.setPickerVisible(true);

        if (picker.getPickerResult() == SGuiConsts.FORM_RESULT_OK) {
            key = (int[]) picker.getOption();

            if (key != null) {
                if (key[0] != SLibConsts.UNDEFINED) {
                    moKeyBizPartner.setValue(new int[] { key[0] });
                    moKeyBizPartner.requestFocusInWindow();
                }
            }
        }
    }
    
    private void actionPickItem() {
        int[] key = null;
        SGuiOptionPicker picker = null;

        picker = miClient.getSession().getModule(SModConsts.MOD_ITM_N).getOptionPicker(SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, null);
        picker.resetPicker();
        picker.setPickerVisible(true);

        if (picker.getPickerResult() == SGuiConsts.FORM_RESULT_OK) {
            key = (int[]) picker.getOption();

            if (key != null) {
                if (key[0] != SLibConsts.UNDEFINED) {
                    moKeyItem.setValue(new int[] { key[0] });
                    moKeyItem.requestFocusInWindow();
                }
            }
        }
    }

    private void actionPickTax() {
        int[] key = null;
        SGuiOptionPicker picker = null;

        picker = miClient.getSession().getModule(SModConsts.MOD_FIN_N).getOptionPicker(SModConsts.FINU_TAX, SLibConsts.UNDEFINED, null);
        picker.resetPicker();
        picker.setPickerVisible(true);

        if (picker.getPickerResult() == SGuiConsts.FORM_RESULT_OK) {
            key = (int[]) picker.getOption();

            if (key != null) {
                if (key[0] != SLibConsts.UNDEFINED) {
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

    private void actionClearItem() {
        moKeyItem.setSelectedIndex(0);
        moKeyItem.requestFocus();
    }

    private void actionClearTax() {
        moKeyTax.setSelectedIndex(0);
        moKeyTax.requestFocus();
    }
    
    private void actionSameEmployee() {
        moKeyBizPartner.setValue(new int[] { moRegistry.getPkReferenceId() });
    }

    @Override
    public void addAllListeners() {
        jbPickBizPartner.addActionListener(this);
        jbPickItem.addActionListener(this);
        jbPickTax.addActionListener(this);
        jbClearBizPartner.addActionListener(this);
        jbClearItem.addActionListener(this);
        jbClearTax.addActionListener(this);
        jbSameEmploye.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbPickBizPartner.removeActionListener(this);
        jbPickItem.removeActionListener(this);
        jbPickTax.removeActionListener(this);
        jbClearBizPartner.removeActionListener(this);
        jbClearItem.removeActionListener(this);
        jbClearTax.removeActionListener(this);
        jbSameEmploye.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyBizPartner, SModConsts.BPSU_BP, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyItem, SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyTax, SModConsts.FINU_TAX, SLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbAccountingDeduction) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moTextDeduction.setValue(moRegistry.getAuxDeduction());
        moTextReference.setValue(moRegistry.getAuxReference());
        moPanelAccount.setSelectedAccount(new SAccount(moRegistry.getFkAccountId(), (String) miClient.getSession().readField(SModConsts.FIN_ACC, new int[] { moRegistry.getFkAccountId() }, SDbRegistry.FIELD_CODE), "", false, SLibConstants.UNDEFINED, SLibConstants.UNDEFINED));
        if (moRegistry.getFkCostCenterId_n() != SLibConsts.UNDEFINED) {
            moPanelCostCenter.setSelectedAccount(new SAccount(moRegistry.getFkCostCenterId_n(), (String) miClient.getSession().readField(SModConsts.FIN_CC, new int[] { moRegistry.getFkCostCenterId_n() }, SDbRegistry.FIELD_CODE), "", false, SLibConstants.UNDEFINED, SLibConstants.UNDEFINED));
        }
        else {
            moPanelCostCenter.setSelectedAccount(null);
        }
        moKeyBizPartner.setValue(new int[] { moRegistry.getFkBizPartnerId_n() });
        moKeyItem.setValue(new int[] { moRegistry.getFkItemId_n() });
        moKeyTax.setValue(new int[] { moRegistry.getFkTaxBasicId_n(), moRegistry.getFkTaxTaxId_n() });

        setFormEditable(true);
        
        moTextDeduction.setEditable(false);
        moTextReference.setEditable(false);
        
        jbSameEmploye.setEnabled(moRegistry.getPkAccountingTypeId() == SModSysConsts.HRSS_TP_ACC_EMP);

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbAccountingDeduction registry = moRegistry.clone();

        if (registry.isRegistryNew()) {}
        
        registry.setFkAccountId(moPanelAccount.getSelectedAccount().getAccountId());
        registry.setFkCostCenterId_n(moPanelCostCenter.getSelectedAccount() == null ? SLibConsts.UNDEFINED : moPanelCostCenter.getSelectedAccount().getAccountId());
        registry.setFkBizPartnerId_n(moKeyBizPartner.getSelectedIndex() == 0 ? SLibConsts.UNDEFINED : moKeyBizPartner.getValue()[0]);
        registry.setFkItemId_n(moKeyItem.getSelectedIndex() == 0 ? SLibConsts.UNDEFINED : moKeyItem.getValue()[0]);
        registry.setFkTaxBasicId_n(moKeyTax.getSelectedIndex() == 0 ? SLibConsts.UNDEFINED : moKeyTax.getValue()[0]);
        registry.setFkTaxTaxId_n(moKeyTax.getSelectedIndex() == 0 ? SLibConsts.UNDEFINED : moKeyTax.getValue()[1]);

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            validation = moPanelAccount.validatePanel();
        }
            
        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPickBizPartner) {
                actionPickBizPartner();
            }
            else if (button == jbPickItem) {
                actionPickItem();
            }
            else if (button == jbPickTax) {
                actionPickTax();
            }
            else if (button == jbClearBizPartner) {
                actionClearBizPartner();
            }
            else if (button == jbClearItem) {
                actionClearItem();
            }
            else if (button == jbClearTax) {
                actionClearTax();
            }
            else if (button == jbSameEmploye) {
                actionSameEmployee();
            }
        }
    }
}

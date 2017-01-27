/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.qty.form;

import erp.SClient;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mitm.data.SDataItem;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.qty.db.SDbQualityLotApr;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import sa.lib.SLibConsts;
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
 *
 * @author Uriel Castañeda
 */
public class SFormQltLotAprove extends SBeanForm implements ActionListener, ItemListener {

    protected SDbQualityLotApr moRegistry;
  
    /**
     * Creates new form SFormQtyLot
     * @param client
     * @param title
     */
    public SFormQltLotAprove(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.QTLY_LOT, SLibConsts.UNDEFINED, title);
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
        jPanel14 = new javax.swing.JPanel();
        jlItem = new javax.swing.JLabel();
        moKeyItem = new sa.lib.gui.bean.SBeanFieldKey();
        jbItem = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jlUnit = new javax.swing.JLabel();
        moKeyUnit = new sa.lib.gui.bean.SBeanFieldKey();
        jbUnit = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jlBizPartner = new javax.swing.JLabel();
        moKeyBizPartner = new sa.lib.gui.bean.SBeanFieldKey();
        jbBizPartner = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jlLot = new javax.swing.JLabel();
        moTextLot = new sa.lib.gui.bean.SBeanFieldText();
        jPanel8 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        moDate = new sa.lib.gui.bean.SBeanFieldDate();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(15, 1, 0, 5));

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItem.setForeground(new java.awt.Color(0, 0, 255));
        jlItem.setText("Item:*");
        jlItem.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlItem);

        moKeyItem.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel14.add(moKeyItem);

        jbItem.setText("...");
        jbItem.setFocusable(false);
        jbItem.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel14.add(jbItem);

        jPanel2.add(jPanel14);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlUnit.setForeground(new java.awt.Color(0, 0, 255));
        jlUnit.setText("Unidad:*");
        jlUnit.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlUnit);

        moKeyUnit.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel5.add(moKeyUnit);

        jbUnit.setText("...");
        jbUnit.setFocusable(false);
        jbUnit.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbUnit);

        jPanel2.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner.setForeground(new java.awt.Color(0, 0, 255));
        jlBizPartner.setText("Proveedor:*");
        jlBizPartner.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlBizPartner);

        moKeyBizPartner.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel6.add(moKeyBizPartner);

        jbBizPartner.setText("...");
        jbBizPartner.setFocusable(false);
        jbBizPartner.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbBizPartner);

        jPanel2.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLot.setText("Lote:*");
        jlLot.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlLot);

        moTextLot.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel7.add(moTextLot);

        jPanel2.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDate.setText("Fecha lote:*");
        jlDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlDate);
        jPanel8.add(moDate);

        jPanel2.add(jPanel8);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbBizPartner;
    private javax.swing.JButton jbItem;
    private javax.swing.JButton jbUnit;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlItem;
    private javax.swing.JLabel jlLot;
    private javax.swing.JLabel jlUnit;
    private sa.lib.gui.bean.SBeanFieldDate moDate;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBizPartner;
    private sa.lib.gui.bean.SBeanFieldKey moKeyItem;
    private sa.lib.gui.bean.SBeanFieldKey moKeyUnit;
    private sa.lib.gui.bean.SBeanFieldText moTextLot;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 480, 300);
                
        moKeyItem.setKeySettings(miClient, SGuiUtils.getLabelName(jlItem.getText()), true);
        moKeyUnit.setKeySettings(miClient, SGuiUtils.getLabelName(jlUnit), true);
        moKeyBizPartner.setKeySettings(miClient, SGuiUtils.getLabelName(jlBizPartner.getText()), true);
        moTextLot.setTextSettings(SGuiUtils.getLabelName(jlLot.getText()), 25);
        moDate.setDateSettings(miClient, SGuiUtils.getLabelName(jlDate.getText()), true);

        moFields.addField(moKeyItem);
        moFields.addField(moKeyUnit);
        moFields.addField(moKeyBizPartner);
        moFields.addField(moTextLot);
        moFields.addField(moDate);
        
        moFields.setFormButton(jbSave);
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
                }
            }
        }
    }
    
    private void actionPickUnit() {
         int[] key = null;
        SGuiOptionPicker picker = null;

        picker = miClient.getSession().getModule(SModConsts.MOD_ITM_N).getOptionPicker(SModConsts.ITMU_UNIT, SLibConsts.UNDEFINED, null);
        picker.resetPicker();
        picker.setPickerVisible(true);

        if (picker.getPickerResult() == SGuiConsts.FORM_RESULT_OK) {
            key = (int[]) picker.getOption();

            if (key != null) {
                if (key[0] != SLibConsts.UNDEFINED) {
                    moKeyUnit.setValue(new int[] { key[0] });
                }
            }
        }
    }
    
    private void actionPickBizPartner() {
        int[] key = null;
        SGuiOptionPicker picker = null;

        picker = miClient.getSession().getModule(SModConsts.MOD_BPS_N).getOptionPicker(SModConsts.BPSU_BP, SModSysConsts.BPSS_CT_BP_SUP, null);
        picker.resetPicker();
        picker.setPickerVisible(true);

        if (picker.getPickerResult() == SGuiConsts.FORM_RESULT_OK) {
            key = (int[]) picker.getOption();

            if (key != null) {
                if (key[0] != SLibConsts.UNDEFINED) {
                    moKeyBizPartner.setValue(new int[] { key[0] });
                }
            }
        }
    }
    
    private void itemStateKeyItem() {
        if (moKeyItem.getSelectedIndex() != 0) {
            SDataItem item = (SDataItem) SDataUtilities.readRegistry((SClient) miClient, SDataConstants.ITMU_ITEM, new int[] { moKeyItem.getSelectedItem().getPrimaryKey()[0] }, SLibConstants.EXEC_MODE_VERBOSE);
            
            moKeyUnit.setEnabled(true);
            moKeyUnit.resetField();
            
            miClient.getSession().populateCatalogue(moKeyUnit, SModConsts.ITMU_UNIT, item.getDbmsDataUnit().getFkUnitTypeId() , null);
            
            moKeyUnit.setValue(new int[] { item.getFkUnitId() });
        }
        else {
            moKeyUnit.setEnabled(false);
            moKeyUnit.resetField();
        }
    }
    
    private void enabledFields(boolean enable) {
        moKeyItem.setEnabled(enable);
        jbItem.setEnabled(enable);
        
        if (moRegistry.isRegistryNew()) {
            moKeyUnit.setEnabled(!enable);
            jbUnit.setEnabled(!enable);
        }
        else {
            moKeyUnit.setEnabled(enable);
            jbUnit.setEnabled(enable);
        }
        
        moKeyBizPartner.setEnabled(enable);
        jbBizPartner.setEnabled(enable);
        moTextLot.setEnabled(enable);
        moDate.setEditable(enable);
        
    }
    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

    @Override
    public void addAllListeners() {
        moKeyItem.addItemListener(this);
        jbItem.addActionListener(this);
        jbUnit.addActionListener(this);
        jbBizPartner.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        moKeyItem.removeItemListener(this);
        jbItem.removeActionListener(this);
        jbUnit.removeActionListener(this);
        jbBizPartner.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyBizPartner, SModConsts.BPSU_BP, SModSysConsts.BPSS_CT_BP_SUP, null);   
        miClient.getSession().populateCatalogue(moKeyItem, SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, null);       
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {


        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;
        
        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            moRegistry.setDate(miClient.getSession().getCurrentDate());
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moKeyItem.setValue(new int[] { moRegistry.getFkItemId() });
        moKeyUnit.setValue(new int[] { moRegistry.getFkUnitId()});
        moKeyBizPartner.setValue(new int[] {moRegistry.getFkBizPartnerId() });
        moTextLot.setValue(moRegistry.getLot());
        moDate.setValue(moRegistry.getDate());

        setFormEditable(true);
        
        enabledFields(moRegistry.isRegistryNew());

        addAllListeners();
    }

    @Override
    public SDbQualityLotApr getRegistry() throws Exception {
        SDbQualityLotApr registry = moRegistry.clone();
        
        registry.setFkItemId(moKeyItem.getSelectedItem().getPrimaryKey()[0]);
        registry.setFkUnitId(moKeyUnit.getSelectedItem().getPrimaryKey()[0]);
        registry.setFkBizPartnerId(moKeyBizPartner.getSelectedItem().getPrimaryKey()[0]);
        registry.setLot(moTextLot.getText());
        
        return registry;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbItem) {
                actionPickItem();
            }
            else if (button == jbUnit) {
                actionPickUnit();
            }
            else if (button == jbBizPartner) {
                actionPickBizPartner();
            }
        }
    }
    
    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        return validation;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
         if (e.getSource() instanceof SBeanFieldKey) {
            SBeanFieldKey field = (SBeanFieldKey) e.getSource();

            if (field == moKeyItem) {
                itemStateKeyItem();
            }           
        }
    }
}

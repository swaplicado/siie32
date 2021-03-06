/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.form;

import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.log.db.SDbRate;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiFieldKeyGroup;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;

/**
 *
 * @author Néstor Ávalos, Isabel Servín
 */
public class SFormRate extends sa.lib.gui.bean.SBeanForm implements ItemListener {

    private SDbRate moRegistry;
    private SGuiFieldKeyGroup moFieldKeySourceGroup;
    private SGuiFieldKeyGroup moFieldKeyDestinyGroup;

    /**
     * Creates new form SFormRate
     * @param client
     * @param title
     */
    public SFormRate(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.LOGU_SPOT, SLibConstants.UNDEFINED, title);
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
        dBeanCompoundFieldCurrency1 = new sa.lib.gui.bean.SBeanCompoundFieldCurrency();
        jPanel1 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jlSourceSpotType = new javax.swing.JLabel();
        moKeySourceSpotType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel25 = new javax.swing.JPanel();
        jlSourceSpot = new javax.swing.JLabel();
        moKeySourceSpot = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel26 = new javax.swing.JPanel();
        jlDestinySpotType = new javax.swing.JLabel();
        moKeyDestinySpotType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel27 = new javax.swing.JPanel();
        jlDestinySpot = new javax.swing.JLabel();
        moKeyDestinySpot = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel28 = new javax.swing.JPanel();
        jlCarrier = new javax.swing.JLabel();
        moKeyCarrier = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel29 = new javax.swing.JPanel();
        jlVehicleType = new javax.swing.JLabel();
        moKeyVehicleType = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel30 = new javax.swing.JPanel();
        jlCurrency = new javax.swing.JLabel();
        moKeyCurrency = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel22 = new javax.swing.JPanel();
        jlRate = new javax.swing.JLabel();
        moCurRate = new sa.lib.gui.bean.SBeanCompoundFieldCurrency();
        jPanel24 = new javax.swing.JPanel();
        moBoolConsolidated = new sa.lib.gui.bean.SBeanFieldBoolean();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel23.setLayout(new java.awt.GridLayout(10, 1, 0, 5));

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSourceSpotType.setText("Tipo lugar origen:*");
        jlSourceSpotType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel21.add(jlSourceSpotType);

        moKeySourceSpotType.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel21.add(moKeySourceSpotType);

        jPanel23.add(jPanel21);

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSourceSpot.setText("Lugar origen:*");
        jlSourceSpot.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel25.add(jlSourceSpot);

        moKeySourceSpot.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel25.add(moKeySourceSpot);

        jPanel23.add(jPanel25);

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDestinySpotType.setText("Tipo lugar destino:*");
        jlDestinySpotType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel26.add(jlDestinySpotType);

        moKeyDestinySpotType.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel26.add(moKeyDestinySpotType);

        jPanel23.add(jPanel26);

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDestinySpot.setText("Lugar destino:*");
        jlDestinySpot.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel27.add(jlDestinySpot);

        moKeyDestinySpot.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel27.add(moKeyDestinySpot);

        jPanel23.add(jPanel27);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCarrier.setText("Transportista:");
        jlCarrier.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel28.add(jlCarrier);
        jlCarrier.getAccessibleContext().setAccessibleName("Transportista:");

        moKeyCarrier.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel28.add(moKeyCarrier);

        jPanel23.add(jPanel28);

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVehicleType.setText("Tipo vehículo:");
        jlVehicleType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel29.add(jlVehicleType);

        moKeyVehicleType.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel29.add(moKeyVehicleType);

        jPanel23.add(jPanel29);

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCurrency.setText("Moneda:*");
        jlCurrency.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel30.add(jlCurrency);

        moKeyCurrency.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel30.add(moKeyCurrency);

        jPanel23.add(jPanel30);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRate.setText("Tarifa:*");
        jlRate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel22.add(jlRate);
        jPanel22.add(moCurRate);

        jPanel23.add(jPanel22);

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolConsolidated.setText("Consolidado");
        jPanel24.add(moBoolConsolidated);

        jPanel23.add(jPanel24);

        jPanel1.add(jPanel23, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private sa.lib.gui.bean.SBeanCompoundFieldCurrency dBeanCompoundFieldCurrency1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JLabel jlCarrier;
    private javax.swing.JLabel jlCurrency;
    private javax.swing.JLabel jlDestinySpot;
    private javax.swing.JLabel jlDestinySpotType;
    private javax.swing.JLabel jlRate;
    private javax.swing.JLabel jlSourceSpot;
    private javax.swing.JLabel jlSourceSpotType;
    private javax.swing.JLabel jlVehicleType;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolConsolidated;
    private sa.lib.gui.bean.SBeanCompoundFieldCurrency moCurRate;
    private sa.lib.gui.bean.SBeanFieldKey moKeyCarrier;
    private sa.lib.gui.bean.SBeanFieldKey moKeyCurrency;
    private sa.lib.gui.bean.SBeanFieldKey moKeyDestinySpot;
    private sa.lib.gui.bean.SBeanFieldKey moKeyDestinySpotType;
    private sa.lib.gui.bean.SBeanFieldKey moKeySourceSpot;
    private sa.lib.gui.bean.SBeanFieldKey moKeySourceSpotType;
    private sa.lib.gui.bean.SBeanFieldKey moKeyVehicleType;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 560, 350);

        moFieldKeySourceGroup = new SGuiFieldKeyGroup(miClient);
        moFieldKeyDestinyGroup = new SGuiFieldKeyGroup(miClient);

        moKeySourceSpotType.setKeySettings(miClient, SGuiUtils.getLabelName(jlSourceSpotType.getText()), true);
        moKeySourceSpot.setKeySettings(miClient, SGuiUtils.getLabelName(jlSourceSpot.getText()), true);
        moKeyDestinySpotType.setKeySettings(miClient, SGuiUtils.getLabelName(jlDestinySpotType.getText()), true);
        moKeyDestinySpot.setKeySettings(miClient, SGuiUtils.getLabelName(jlDestinySpot.getText()), true);
        moKeyCarrier.setKeySettings(miClient, SGuiUtils.getLabelName(jlCarrier.getText()), true);
        moKeyVehicleType.setKeySettings(miClient, SGuiUtils.getLabelName(jlVehicleType.getText()), true);
        moKeyCurrency.setKeySettings(miClient, SGuiUtils.getLabelName(jlCurrency.getText()), true);
        moCurRate.setCompoundFieldSettings(miClient);
        moCurRate.getField().setDecimalSettings(SGuiUtils.getLabelName(jlRate.getText()), SGuiConsts.GUI_TYPE_DEC_AMT, false);
        moBoolConsolidated.setBooleanSettings("Consolidado", false);

        moFields.addField(moKeySourceSpotType);
        moFields.addField(moKeySourceSpot);
        moFields.addField(moKeyDestinySpotType);
        moFields.addField(moKeyDestinySpot);
        moFields.addField(moKeyCurrency);
        moFields.addField(moCurRate.getField());
        moFields.addField(moBoolConsolidated);

        moFields.setFormButton(jbSave);
    }

    private void actionChangedFkCurrencyId() {
        if (moKeyCurrency.getSelectedIndex() > 0) {
            moCurRate.setCurrencyKey(moKeyCurrency.getValue());
        }
        else {
            moCurRate.setCurrencyKey(null);
        }
    }

    @Override
    public void addAllListeners() {
        moKeyCurrency.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        moKeyCurrency.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        moFieldKeySourceGroup.initGroup();
        moFieldKeySourceGroup.addFieldKey(moKeySourceSpotType, SModConsts.LOGS_TP_SPOT, SLibConsts.UNDEFINED, null);
        moFieldKeySourceGroup.addFieldKey(moKeySourceSpot, SModConsts.LOGU_SPOT, SLibConsts.UNDEFINED, null);
        moFieldKeySourceGroup.populateCatalogues();

        moFieldKeyDestinyGroup.initGroup();
        moFieldKeyDestinyGroup.addFieldKey(moKeyDestinySpotType, SModConsts.LOGS_TP_SPOT, SLibConsts.UNDEFINED, null);
        moFieldKeyDestinyGroup.addFieldKey(moKeyDestinySpot, SModConsts.LOGU_SPOT, SLibConsts.UNDEFINED, null);
        moFieldKeyDestinyGroup.populateCatalogues();

        miClient.getSession().populateCatalogue(moKeyCarrier, SModConsts.BPSX_BP_ATT_CARR, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyVehicleType, SModConsts.LOGU_TP_VEH, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyCurrency, SModConsts.CFGU_CUR, SLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbRate) registry;

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

        moKeySourceSpotType.setValue(new int[] { moRegistry.getFkSourceSpotTypeId() });
        moKeySourceSpot.setValue(new int[] { moRegistry.getFkSourceSpotId() });
        moKeyDestinySpotType.setValue(new int[] { moRegistry.getFkDestinySpotTypeId() });
        moKeyDestinySpot.setValue(new int[] { moRegistry.getFkDestinySpotId() });
        moKeyCarrier.setValue(new int[] { moRegistry.getFkCarrierId() });
        moKeyVehicleType.setValue(new int[] { moRegistry.getFkVehicleTypeId() });
        moCurRate.getField().setValue(moRegistry.getRate());
        moCurRate.setCurrencyKey(moRegistry.getFkCurrencyId() == SLibConsts.UNDEFINED ? null : new int[] { moRegistry.getFkCurrencyId() });
        moBoolConsolidated.setValue(moRegistry.isConsolidated());

        setFormEditable(true);

        if (moRegistry.isRegistryNew() && moRegistry.getFkSourceSpotTypeId() == SLibConsts.UNDEFINED) {
            moFieldKeySourceGroup.resetGroup();
            moFieldKeyDestinyGroup.resetGroup();
        }

        addAllListeners();

        moKeyCurrency.setValue(new int[] { moRegistry.getFkCurrencyId() });
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbRate registry = moRegistry.clone();

        if (registry.isRegistryNew()) {}

        moRegistry.setFkSourceSpotTypeId(moKeySourceSpotType.getValue()[0]);
        moRegistry.setFkSourceSpotId(moKeySourceSpot.getValue()[0]);
        moRegistry.setFkDestinySpotTypeId(moKeyDestinySpotType.getValue()[0]);
        moRegistry.setFkDestinySpotId(moKeyDestinySpot.getValue()[0]);
        moRegistry.setFkCarrierId(moKeyCarrier.getSelectedIndex() == 0 ? 0 : moKeyCarrier.getValue()[0]);
        moRegistry.setFkVehicleTypeId(moKeyVehicleType.getSelectedIndex() == 0 ? 0 : moKeyVehicleType.getValue()[0]);
        moRegistry.setFkCurrencyId(moKeyCurrency.getValue()[0]);
        moRegistry.setRate(moCurRate.getField().getValue());
        moRegistry.setConsolidated(moBoolConsolidated.getValue());

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        if (validation.isValid()) {
            boolean showConfirmBox = false;
            String message = "No selecciono ninguna opción en los siguientes campos:\n";
            JComponent component = null;
            if (moKeyCarrier.getSelectedIndex() == 0) {
                showConfirmBox = true;
                message += SGuiUtils.getLabelName(jlCarrier) + "\n";
                component = component == null ? moKeyCarrier : component;
            }
            if (moKeyVehicleType.getSelectedIndex() == 0) {
                showConfirmBox = true;
                message += SGuiUtils.getLabelName(jlVehicleType) + "\n";
                component = component == null ? moKeyVehicleType : component;
            }
            if (showConfirmBox) {
                if (miClient.showMsgBoxConfirm(message + SGuiConsts.MSG_CNF_CONT) != JOptionPane.OK_OPTION){
                    validation.setMessage("Favor de llenar los campos faltantes.");
                    validation.setComponent(component);
                }
            }
        }

        return validation;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof javax.swing.JComboBox && e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox comboBox = (JComboBox)  e.getSource();

            if (comboBox == moKeyCurrency) {
                actionChangedFkCurrencyId();
            }
        }
    }
}

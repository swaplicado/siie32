/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormOptionPickerInterface;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mtrn.data.SDataStockConfigItem;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;

/**
 *
 * @author  Juan Barajas
 */
public class SFormStockConfigItem extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mtrn.data.SDataStockConfigItem moDataStockConfigItem;
    private erp.lib.form.SFormField moFieldPkLinkTypeId;
    private erp.lib.form.SFormField moFieldPkReferenceId;
    private erp.lib.form.SFormField moFieldPkCompanyBranchId;
    private erp.lib.form.SFormField moFieldPkWarehouseId;
    private erp.lib.form.SFormField moFieldIsDeleted;

    /** Creates new form SFormStockConfigItem
     * @param client ERP Client interface.
     */
    public SFormStockConfigItem(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jpSettings = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlPkLinkTypeId = new javax.swing.JLabel();
        jcbPkLinkTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jbPkLinkTypeId = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jlPkReferenceId = new javax.swing.JLabel();
        jcbPkReferenceId = new javax.swing.JComboBox<SFormComponentItem>();
        jbPkReferenceId = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jlPkCompanyBranchId = new javax.swing.JLabel();
        jcbPkCompanyBranchId = new javax.swing.JComboBox<SFormComponentItem>();
        jbPkCompanyBranchId = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jlPkWarehouseId = new javax.swing.JLabel();
        jcbPkWarehouseId = new javax.swing.JComboBox<SFormComponentItem>();
        jbPkWarehouseId = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jckIsDeleted = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configuración de ítems"); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jpSettings.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPkLinkTypeId.setForeground(java.awt.Color.blue);
        jlPkLinkTypeId.setText("Tipo de referencia: *");
        jlPkLinkTypeId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel6.add(jlPkLinkTypeId);

        jcbPkLinkTypeId.setMaximumRowCount(12);
        jcbPkLinkTypeId.setPreferredSize(new java.awt.Dimension(350, 23));
        jcbPkLinkTypeId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbPkLinkTypeIdItemStateChanged(evt);
            }
        });
        jPanel6.add(jcbPkLinkTypeId);

        jbPkLinkTypeId.setText("...");
        jbPkLinkTypeId.setToolTipText("Seleccionar tipo de referencia");
        jbPkLinkTypeId.setFocusable(false);
        jbPkLinkTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbPkLinkTypeId);

        jpSettings.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPkReferenceId.setForeground(java.awt.Color.blue);
        jlPkReferenceId.setText("Referencia: *");
        jlPkReferenceId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel7.add(jlPkReferenceId);

        jcbPkReferenceId.setMaximumRowCount(16);
        jcbPkReferenceId.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel7.add(jcbPkReferenceId);

        jbPkReferenceId.setText("...");
        jbPkReferenceId.setToolTipText("Seleccionar referencia");
        jbPkReferenceId.setFocusable(false);
        jbPkReferenceId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel7.add(jbPkReferenceId);

        jpSettings.add(jPanel7);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPkCompanyBranchId.setForeground(java.awt.Color.blue);
        jlPkCompanyBranchId.setText("Sucursal de la empresa: *");
        jlPkCompanyBranchId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel1.add(jlPkCompanyBranchId);

        jcbPkCompanyBranchId.setPreferredSize(new java.awt.Dimension(350, 23));
        jcbPkCompanyBranchId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbPkCompanyBranchIdItemStateChanged(evt);
            }
        });
        jPanel1.add(jcbPkCompanyBranchId);

        jbPkCompanyBranchId.setText("...");
        jbPkCompanyBranchId.setToolTipText("Seleccionar sucursal");
        jbPkCompanyBranchId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel1.add(jbPkCompanyBranchId);

        jpSettings.add(jPanel1);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPkWarehouseId.setForeground(java.awt.Color.blue);
        jlPkWarehouseId.setText("Almacén: *");
        jlPkWarehouseId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel9.add(jlPkWarehouseId);

        jcbPkWarehouseId.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel9.add(jcbPkWarehouseId);

        jbPkWarehouseId.setText("...");
        jbPkWarehouseId.setToolTipText("Seleccionar almacén");
        jbPkWarehouseId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel9.add(jbPkWarehouseId);

        jpSettings.add(jPanel9);

        jPanel3.add(jpSettings, java.awt.BorderLayout.PAGE_START);

        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel4.add(jPanel5);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jckIsDeleted.setText("Registro eliminado");
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel8.add(jckIsDeleted);

        jPanel4.add(jPanel8);

        jPanel10.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel3.add(jPanel10, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar"); // NOI18N
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jbOk);

        jbCancel.setText("Cancelar"); // NOI18N
        jbCancel.setToolTipText("[Escape]");
        jPanel2.add(jbCancel);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-600)/2, (screenSize.height-343)/2, 600, 343);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jcbPkLinkTypeIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbPkLinkTypeIdItemStateChanged
        if (!mbResetingForm) {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                itemStateChangedPkLinkTypeId();
            }
        }
    }//GEN-LAST:event_jcbPkLinkTypeIdItemStateChanged

    private void jcbPkCompanyBranchIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbPkCompanyBranchIdItemStateChanged
        itemStateChangedCompanyBranch();
    }//GEN-LAST:event_jcbPkCompanyBranchIdItemStateChanged

    private void initComponentsExtra() {
        moFieldPkLinkTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPkLinkTypeId, jlPkLinkTypeId);
        moFieldPkLinkTypeId.setPickerButton(jbPkLinkTypeId);
        moFieldPkReferenceId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPkReferenceId, jlPkReferenceId);
        moFieldPkReferenceId.setPickerButton(jbPkReferenceId);
        moFieldPkCompanyBranchId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPkCompanyBranchId, jlPkCompanyBranchId);
        moFieldPkCompanyBranchId.setPickerButton(jbPkCompanyBranchId);
        moFieldPkWarehouseId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPkWarehouseId, jlPkWarehouseId);
        moFieldPkWarehouseId.setPickerButton(jbPkWarehouseId);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsDeleted);

        mvFields = new Vector<SFormField>();
        mvFields.add(moFieldPkLinkTypeId);
        mvFields.add(moFieldPkReferenceId);
        mvFields.add(moFieldPkCompanyBranchId);
        mvFields.add(moFieldPkWarehouseId);
        mvFields.add(moFieldIsDeleted);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbPkLinkTypeId.addActionListener(this);
        jbPkReferenceId.addActionListener(this);
        jbPkCompanyBranchId.addActionListener(this);
        jbPkWarehouseId.addActionListener(this);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionCancel = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionCancel, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            if (jcbPkLinkTypeId.isEnabled()) {
                jcbPkLinkTypeId.requestFocus();
            }
            else {
                jckIsDeleted.requestFocus();
            }
        }
    }

    private void actionPkLinkTypeId() {
        miClient.pickOption(SDataConstants.TRNS_TP_LINK, moFieldPkLinkTypeId, null);
    }

    private void actionPkReferenceId() {
        SFormOptionPickerInterface picker = null;

        try {
            switch (moFieldPkLinkTypeId.getKeyAsIntArray()[0]) {
                case SDataConstantsSys.TRNS_TP_LINK_CT_ITEM:
                    picker = miClient.getOptionPicker(SDataConstants.ITMS_CT_ITEM);
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_CL_ITEM:
                    picker = miClient.getOptionPicker(SDataConstants.ITMS_CL_ITEM);
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_TP_ITEM:
                    picker = miClient.getOptionPicker(SDataConstants.ITMS_TP_ITEM);
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_IFAM:
                    picker = miClient.getOptionPicker(SDataConstants.ITMU_IFAM);
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_IGRP:
                    picker = miClient.getOptionPicker(SDataConstants.ITMU_IGRP);
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_IGEN:
                    picker = miClient.getOptionPicker(SDataConstants.ITMU_IGEN);
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_LINE:
                    picker = miClient.getOptionPicker(SDataConstants.ITMU_LINE);
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_BRD:
                    picker = miClient.getOptionPicker(SDataConstants.ITMU_BRD);
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_MFR:
                    picker = miClient.getOptionPicker(SDataConstants.ITMU_MFR);
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_ITEM:
                    picker = miClient.getOptionPicker(SDataConstants.ITMU_ITEM);
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_FORM_PICK);
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        picker.formReset();
        picker.formRefreshOptionPane();
        picker.setSelectedPrimaryKey(moFieldPkReferenceId.getKeyAsIntArray());
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldPkReferenceId.setFieldValue(picker.getSelectedPrimaryKey());
            jcbPkReferenceId.requestFocus();
        }
    }

    private void itemStateChangedPkLinkTypeId() {
        boolean enable = true;

        jcbPkReferenceId.removeAllItems();
        jcbPkReferenceId.setEnabled(false);
        jbPkReferenceId.setEnabled(false);

        if (jcbPkLinkTypeId.getSelectedIndex() > 0) {
            try {
                switch (moFieldPkLinkTypeId.getKeyAsIntArray()[0]) {
                    case SDataConstantsSys.TRNS_TP_LINK_ALL:
                        enable = false;
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_CT_ITEM:
                        SFormUtilities.populateComboBox(miClient, jcbPkReferenceId, SDataConstants.ITMS_CT_ITEM);
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_CL_ITEM:
                        SFormUtilities.populateComboBox(miClient, jcbPkReferenceId, SDataConstants.ITMS_CL_ITEM);
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_TP_ITEM:
                        SFormUtilities.populateComboBox(miClient, jcbPkReferenceId, SDataConstants.ITMS_TP_ITEM);
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_IFAM:
                        SFormUtilities.populateComboBox(miClient, jcbPkReferenceId, SDataConstants.ITMU_IFAM);
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_IGRP:
                        SFormUtilities.populateComboBox(miClient, jcbPkReferenceId, SDataConstants.ITMU_IGRP);
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_IGEN:
                        SFormUtilities.populateComboBox(miClient, jcbPkReferenceId, SDataConstants.ITMU_IGEN);
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_LINE:
                        SFormUtilities.populateComboBox(miClient, jcbPkReferenceId, SDataConstants.ITMU_LINE);
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_BRD:
                        SFormUtilities.populateComboBox(miClient, jcbPkReferenceId, SDataConstants.ITMU_BRD);
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_MFR:
                        SFormUtilities.populateComboBox(miClient, jcbPkReferenceId, SDataConstants.ITMU_MFR);
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_ITEM:
                        SFormUtilities.populateComboBox(miClient, jcbPkReferenceId, SDataConstants.ITMU_ITEM);
                        break;
                    default:
                        enable = false;
                        throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_FORM_PICK);
                }
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }

            if (enable) {
                jcbPkReferenceId.setSelectedIndex(0);
                jcbPkReferenceId.setEnabled(true);
                jbPkReferenceId.setEnabled(true);
            }
        }
    }

    private void actionEdit(boolean edit) {

    }

    private void actionOk() {
        SFormValidation validation = formValidate();

        if (validation.getIsError()) {
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocus();
            }
            if (validation.getMessage().length() > 0) {
                miClient.showMsgBoxWarning(validation.getMessage());
            }
        }
        else {
            mnFormResult = SLibConstants.FORM_RESULT_OK;
            setVisible(false);
        }
    }

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    private void renderComboBoxWarehouse() {
        if (moFieldPkCompanyBranchId.getKeyAsIntArray()[0] <= 0) {
            jcbPkWarehouseId.setEnabled(false);
            jbPkWarehouseId.setEnabled(false);
        }
        else {
            jcbPkWarehouseId.setEnabled(true);
            jbPkWarehouseId.setEnabled(true);
        }
    }

    private void populateComboBoxWarehouse() {
        jcbPkWarehouseId.removeAllItems();

        if (moFieldPkCompanyBranchId.getKeyAsIntArray()[0] > 0) {
            SFormUtilities.populateComboBox(miClient, jcbPkWarehouseId, SDataConstants.CFGX_COB_ENT_WH, new int[] { moFieldPkCompanyBranchId.getKeyAsIntArray()[0] });
        }

        renderComboBoxWarehouse();
    }

    private void itemStateChangedCompanyBranch() {
        if (!mbResetingForm) {
            populateComboBoxWarehouse();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbPkCompanyBranchId;
    private javax.swing.JButton jbPkLinkTypeId;
    private javax.swing.JButton jbPkReferenceId;
    private javax.swing.JButton jbPkWarehouseId;
    private javax.swing.JComboBox<SFormComponentItem> jcbPkCompanyBranchId;
    private javax.swing.JComboBox<SFormComponentItem> jcbPkLinkTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbPkReferenceId;
    private javax.swing.JComboBox<SFormComponentItem> jcbPkWarehouseId;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JLabel jlPkCompanyBranchId;
    private javax.swing.JLabel jlPkLinkTypeId;
    private javax.swing.JLabel jlPkReferenceId;
    private javax.swing.JLabel jlPkWarehouseId;
    private javax.swing.JPanel jpSettings;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
         moDataStockConfigItem = null;

        jcbPkLinkTypeId.setEnabled(true);
        jcbPkReferenceId.setEnabled(true);
        jcbPkCompanyBranchId.setEnabled(true);
        jcbPkWarehouseId.setEnabled(true);
        jbPkLinkTypeId.setEnabled(true);
        jbPkReferenceId.setEnabled(true);
        jbPkCompanyBranchId.setEnabled(true);
        jbPkWarehouseId.setEnabled(true);
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        moDataStockConfigItem = null;

        for (int i = 0; i < mvFields.size(); i++) {
            mvFields.get(i).resetField();
        }

        jcbPkWarehouseId.removeAllItems();
        jcbPkLinkTypeId.setEnabled(true);
        jbPkLinkTypeId.setEnabled(true);
        jcbPkCompanyBranchId.setEnabled(true);
        jbPkCompanyBranchId.setEnabled(true);
        jcbPkWarehouseId.setEnabled(false);
        jbPkWarehouseId.setEnabled(false);
        jckIsDeleted.setEnabled(false);

        itemStateChangedPkLinkTypeId();
        mbResetingForm = false;
    }

    @Override
    public void formRefreshCatalogues() {
        mbResetingForm = true;
        SFormUtilities.populateComboBox(miClient, jcbPkLinkTypeId, SDataConstants.TRNS_TP_LINK);
        SFormUtilities.populateComboBox(miClient, jcbPkCompanyBranchId, SDataConstants.BPSU_BPB, new int[] { miClient.getSessionXXX().getCurrentCompany().getPkCompanyId() });
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(mvFields.get(i).getComponent());
                break;
            }
        }

        return validation;
    }

    @Override
    public void setFormStatus(int status) {
        mnFormStatus = status;
    }

    @Override
    public void setFormVisible(boolean visible) {
        setVisible(visible);
    }

    @Override
    public int getFormStatus() {
        return mnFormStatus;
    }

    @Override
    public int getFormResult() {
        return mnFormResult;
    }

    @Override
    public void setRegistry(erp.lib.data.SDataRegistry registry) {
        mbResetingForm = true;

        moDataStockConfigItem = (SDataStockConfigItem) registry;

        moFieldPkLinkTypeId.setFieldValue(new int[] { moDataStockConfigItem.getPkLinkTypeId() });
        itemStateChangedPkLinkTypeId();

        switch (moFieldPkLinkTypeId.getKeyAsIntArray()[0]) {
            case SDataConstantsSys.TRNS_TP_LINK_ALL:
                moFieldPkReferenceId.setFieldValue(new int[] { SLibConstants.UNDEFINED });  // Just for consistency, but not needed
                break;
            case SDataConstantsSys.TRNS_TP_LINK_CT_ITEM:
            case SDataConstantsSys.TRNS_TP_LINK_CL_ITEM:
            case SDataConstantsSys.TRNS_TP_LINK_TP_ITEM:
                SFormUtilities.locateComboBoxItemByComplement(jcbPkReferenceId, moDataStockConfigItem.getPkReferenceId());
                break;
            default:
                moFieldPkReferenceId.setFieldValue(new int[] { moDataStockConfigItem.getPkReferenceId() });
        }

        moFieldPkCompanyBranchId.setFieldValue(new int[] { moDataStockConfigItem.getPkCompanyBranchId() });
        populateComboBoxWarehouse();
        moFieldPkWarehouseId.setFieldValue(new int[] { moDataStockConfigItem.getPkCompanyBranchId(), moDataStockConfigItem.getPkWarehouseId() });
        moFieldIsDeleted.setFieldValue(moDataStockConfigItem.getIsDeleted());

        if (!moDataStockConfigItem.getIsRegistryNew()) {
            jcbPkLinkTypeId.setEnabled(false);
            jbPkLinkTypeId.setEnabled(false);
            jcbPkReferenceId.setEnabled(false);
            jbPkReferenceId.setEnabled(false);
            jcbPkCompanyBranchId.setEnabled(false);
            jcbPkWarehouseId.setEnabled(false);
            jbPkCompanyBranchId.setEnabled(false);
            jbPkWarehouseId.setEnabled(false);
        }

        jckIsDeleted.setEnabled(!moDataStockConfigItem.getIsRegistryNew());

        mbResetingForm = false;
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (moDataStockConfigItem == null) {
            moDataStockConfigItem = new SDataStockConfigItem();
            moDataStockConfigItem.setPkLinkTypeId(moFieldPkLinkTypeId.getKeyAsIntArray()[0]);

            switch (moFieldPkLinkTypeId.getKeyAsIntArray()[0]) {
                case SDataConstantsSys.TRNS_TP_LINK_ALL:
                    moDataStockConfigItem.setPkReferenceId(SLibConstants.UNDEFINED);
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_CT_ITEM:
                case SDataConstantsSys.TRNS_TP_LINK_CL_ITEM:
                case SDataConstantsSys.TRNS_TP_LINK_TP_ITEM:
                    moDataStockConfigItem.setPkReferenceId((Integer) ((SFormComponentItem) jcbPkReferenceId.getSelectedItem()).getComplement());
                    break;
                default:
                    moDataStockConfigItem.setPkReferenceId(moFieldPkReferenceId.getKeyAsIntArray()[0]);
            }

            moDataStockConfigItem.setPkCompanyBranchId(moFieldPkWarehouseId.getKeyAsIntArray()[0]);
            moDataStockConfigItem.setPkWarehouseId(moFieldPkWarehouseId.getKeyAsIntArray()[1]);
            moDataStockConfigItem.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moDataStockConfigItem.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moDataStockConfigItem.setIsDeleted(moFieldIsDeleted.getBoolean());

        return moDataStockConfigItem;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public java.lang.Object getValue(int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public javax.swing.JLabel getTimeoutLabel() {
        return null;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbPkLinkTypeId) {
                actionPkLinkTypeId();
            }
            else if (button == jbPkReferenceId) {
                actionPkReferenceId();
            }
            else if (button == jbPkCompanyBranchId) {
                miClient.pickOption(SDataConstants.BPSU_BPB, moFieldPkCompanyBranchId, new int[] { miClient.getSessionXXX().getCompany().getPkCompanyId() });
            }
            else if (button == jbPkWarehouseId) {
                miClient.pickOption(SDataConstants.CFGU_COB_ENT, moFieldPkWarehouseId, new int[] { moFieldPkCompanyBranchId.getKeyAsIntArray()[0], SDataConstantsSys.CFGS_CT_ENT_WH });
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogPickerProdOrder.java
 *
 * Created on 8/02/2012, 08:38:22 AM
 */

package erp.mtrn.form;

import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mmfg.data.SDataProductionOrder;
import erp.mtrn.data.STrnUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JToggleButton;

/**
 *
 * @author Sergio Flores
 */
public class SDialogPickerProdOrder extends javax.swing.JDialog implements ActionListener {

    private int mnFormMode;
    private int mnFormResult;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private erp.client.SClientInterface miClient;

    private int[] manParamProdOrderSourceKey;
    private erp.lib.form.SFormField moFieldProdOrder;
    private erp.mmfg.data.SDataProductionOrder moProdOrder;
    private erp.mitm.data.SDataItem moItem;
    private erp.mitm.data.SDataUnit moUnit;

    /** Creates new form SDialogPickerProdOrder.
     * @param client GUI client interface.
     * @param formMode Form mode for production order (as source or destiny). Constants defined in SLibConstants (i.e. MODE_AS_SRC, MODE_AS_DES).
     */
    public SDialogPickerProdOrder(erp.client.SClientInterface client, int formMode) {
        super(client.getFrame(), true);

        miClient = client;
        mnFormMode = formMode;

        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpProdOrders = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jpProductionOrderItem1 = new javax.swing.JPanel();
        jlMode = new javax.swing.JLabel();
        jtfMode = new javax.swing.JTextField();
        jpProductionOrder = new javax.swing.JPanel();
        jlProdOrder = new javax.swing.JLabel();
        jcbProdOrder = new javax.swing.JComboBox();
        jbProdOrder = new javax.swing.JButton();
        jtbSwitchProdOrder = new javax.swing.JToggleButton();
        jpProductionOrderItem = new javax.swing.JPanel();
        jlItem = new javax.swing.JLabel();
        jtfItemCode = new javax.swing.JTextField();
        jtfItem = new javax.swing.JTextField();
        jtfQuantity = new javax.swing.JTextField();
        jtfQuantityUnit = new javax.swing.JTextField();
        jpControls = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Órdenes de producción disponibles");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpProdOrders.setBorder(javax.swing.BorderFactory.createTitledBorder("Órdenes de producción:"));
        jpProdOrders.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel3.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jpProductionOrderItem1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMode.setText("Modalidad:");
        jlMode.setFocusable(false);
        jlMode.setPreferredSize(new java.awt.Dimension(100, 23));
        jpProductionOrderItem1.add(jlMode);

        jtfMode.setEditable(false);
        jtfMode.setFont(new java.awt.Font("Tahoma", 1, 11));
        jtfMode.setText("TEXT");
        jtfMode.setFocusable(false);
        jtfMode.setPreferredSize(new java.awt.Dimension(200, 23));
        jpProductionOrderItem1.add(jtfMode);

        jPanel3.add(jpProductionOrderItem1);

        jpProductionOrder.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProdOrder.setText("Ord. prod.:");
        jlProdOrder.setPreferredSize(new java.awt.Dimension(100, 23));
        jpProductionOrder.add(jlProdOrder);

        jcbProdOrder.setMaximumRowCount(16);
        jcbProdOrder.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbProdOrder.setPreferredSize(new java.awt.Dimension(329, 23));
        jcbProdOrder.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbProdOrderItemStateChanged(evt);
            }
        });
        jpProductionOrder.add(jcbProdOrder);

        jbProdOrder.setText("...");
        jbProdOrder.setToolTipText("Seleccionar orden de producción");
        jbProdOrder.setFocusable(false);
        jbProdOrder.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProductionOrder.add(jbProdOrder);

        jtbSwitchProdOrder.setText("+");
        jtbSwitchProdOrder.setToolTipText("Mostrar otras órdenes de producción");
        jtbSwitchProdOrder.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jtbSwitchProdOrder.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProductionOrder.add(jtbSwitchProdOrder);

        jPanel3.add(jpProductionOrder);

        jpProductionOrderItem.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItem.setText("Producto:");
        jlItem.setFocusable(false);
        jlItem.setPreferredSize(new java.awt.Dimension(100, 23));
        jpProductionOrderItem.add(jlItem);

        jtfItemCode.setEditable(false);
        jtfItemCode.setText("CODE");
        jtfItemCode.setFocusable(false);
        jtfItemCode.setPreferredSize(new java.awt.Dimension(75, 23));
        jpProductionOrderItem.add(jtfItemCode);

        jtfItem.setEditable(false);
        jtfItem.setText("ITEM");
        jtfItem.setFocusable(false);
        jtfItem.setPreferredSize(new java.awt.Dimension(185, 23));
        jpProductionOrderItem.add(jtfItem);

        jtfQuantity.setEditable(false);
        jtfQuantity.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfQuantity.setText("0.00");
        jtfQuantity.setFocusable(false);
        jtfQuantity.setPreferredSize(new java.awt.Dimension(75, 23));
        jpProductionOrderItem.add(jtfQuantity);

        jtfQuantityUnit.setEditable(false);
        jtfQuantityUnit.setText("UNIT");
        jtfQuantityUnit.setFocusable(false);
        jtfQuantityUnit.setPreferredSize(new java.awt.Dimension(35, 23));
        jpProductionOrderItem.add(jtfQuantityUnit);

        jPanel3.add(jpProductionOrderItem);

        jpProdOrders.add(jPanel3, java.awt.BorderLayout.NORTH);

        getContentPane().add(jpProdOrders, java.awt.BorderLayout.CENTER);

        jpControls.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jPanel1.add(jbCancel);

        jpControls.add(jPanel1, java.awt.BorderLayout.EAST);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jpControls.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpControls, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-528)/2, (screenSize.height-359)/2, 528, 359);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jcbProdOrderItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbProdOrderItemStateChanged
        if (!mbResetingForm) {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                itemStateChangedProdOrder();
            }
        }
}//GEN-LAST:event_jcbProdOrderItemStateChanged

    private void initComponentsExtra() {
        switch (mnFormMode) {
            case SLibConstants.MODE_AS_SRC:
                jtfMode.setText("ORDEN DE PRODUCCIÓN ORIGEN");
                jlProdOrder.setText("Ord. prod. origen:");
                jlItem.setText("Producto origen:");
                jbProdOrder.setToolTipText("Seleccionar orden de producción origen");
                jtbSwitchProdOrder.setEnabled(false);
                break;
            case SLibConstants.MODE_AS_DES:
                jtfMode.setText("ORDEN DE PRODUCCIÓN DESTINO");
                jlProdOrder.setText("Ord. prod. destino:");
                jlItem.setText("Producto destino:");
                jbProdOrder.setToolTipText("Seleccionar orden de producción destino");
                jtbSwitchProdOrder.setEnabled(true);
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        moFieldProdOrder = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbProdOrder, jlProdOrder);
        moFieldProdOrder.setPickerButton(jbProdOrder);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbProdOrder.addActionListener(this);
        jtbSwitchProdOrder.addActionListener(this);

        SFormUtilities.createActionMap(rootPane, this, "actionOk", "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionCancel", "cancel", KeyEvent.VK_ESCAPE, SLibConstants.UNDEFINED);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jcbProdOrder.requestFocus();
        }
    }

    private void itemStateChangedProdOrder() {
        if (jcbProdOrder.getSelectedIndex() <= 0) {
            moProdOrder = null;
            moItem = null;
            moUnit = null;

            jtfItem.setText("");
            jtfItemCode.setText("");
            jtfQuantity.setText("");
            jtfQuantityUnit.setText("");
        }
        else {
            moProdOrder = (SDataProductionOrder) SDataUtilities.readRegistry(miClient, SDataConstants.MFG_ORD, moFieldProdOrder.getKeyAsIntArray(), SLibConstants.EXEC_MODE_VERBOSE);
            moItem = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { moProdOrder.getFkItemId_r() }, SLibConstants.EXEC_MODE_VERBOSE);
            moUnit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { moProdOrder.getFkUnitId_r() }, SLibConstants.EXEC_MODE_VERBOSE);

            jtfItem.setText(moItem.getItem());
            jtfItemCode.setText(moItem.getKey());
            jtfQuantity.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(moProdOrder.getQuantity()));
            jtfQuantityUnit.setText(moUnit.getSymbol());

            jtfItem.setCaretPosition(0);
            jtfItemCode.setCaretPosition(0);
            jtfQuantity.setCaretPosition(0);
            jtfQuantityUnit.setCaretPosition(0);
        }
    }

    private void actionProdOrder() {
        miClient.pickOption(SDataConstants.MFGX_ORD, moFieldProdOrder,
                (manParamProdOrderSourceKey == null || jtbSwitchProdOrder.isSelected()) ?
                    new Object[] { "" + STrnUtilities.getProdOrderActiveStatus() } :
                    new Object[] { "" + STrnUtilities.getProdOrderActiveStatus(), manParamProdOrderSourceKey });
    }

    public void actionSwitchProdOrder() {
        formRefreshCatalogues();
        jcbProdOrder.requestFocus();
    }

    public void actionOk() {
        SFormValidation validation = formValidate();

        if (validation.getIsError()) {
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocus();
            }
            miClient.showMsgBoxWarning(validation.getMessage());
        }
        else {
            mnFormResult = SLibConstants.FORM_RESULT_OK;
            setVisible(false);
        }
    }

    public void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbProdOrder;
    private javax.swing.JComboBox jcbProdOrder;
    private javax.swing.JLabel jlItem;
    private javax.swing.JLabel jlMode;
    private javax.swing.JLabel jlProdOrder;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpProdOrders;
    private javax.swing.JPanel jpProductionOrder;
    private javax.swing.JPanel jpProductionOrderItem;
    private javax.swing.JPanel jpProductionOrderItem1;
    private javax.swing.JToggleButton jtbSwitchProdOrder;
    private javax.swing.JTextField jtfItem;
    private javax.swing.JTextField jtfItemCode;
    private javax.swing.JTextField jtfMode;
    private javax.swing.JTextField jtfQuantity;
    private javax.swing.JTextField jtfQuantityUnit;
    // End of variables declaration//GEN-END:variables

    public void setFormParams(final int[] prodOrderSourceKey) {
        manParamProdOrderSourceKey = prodOrderSourceKey;
    }

    public int getFormResult() {
        return mnFormResult;
    }

    public int[] getSelectedProdOrder() {
        return (int[]) ((SFormComponentItem) jcbProdOrder.getSelectedItem()).getPrimaryKey();
    }

    public boolean isSwitchProdOrderSelected() {
        return jtbSwitchProdOrder.isSelected();
    }

    public void formReset() {
        mbResetingForm = true;

        mnFormResult = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        manParamProdOrderSourceKey = null;
        jtbSwitchProdOrder.setSelected(false);

        mbResetingForm = false;
    }

    @SuppressWarnings("unchecked")
    public void formRefreshCatalogues() {
        mbResetingForm = true;

        SFormUtilities.populateComboBox(miClient, jcbProdOrder, SDataConstants.MFGX_ORD,
                (manParamProdOrderSourceKey == null || jtbSwitchProdOrder.isSelected()) ?
                    new Object[] { STrnUtilities.getProdOrderActiveStatus() } :
                    new Object[] { STrnUtilities.getProdOrderActiveStatus(), manParamProdOrderSourceKey });

        itemStateChangedProdOrder();

        mbResetingForm = false;
    }

    public erp.lib.form.SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();

        if (jcbProdOrder.getSelectedIndex() <= 0) {
            validation.setComponent(jcbProdOrder);
            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlProdOrder.getText() + "'.");
        }
        else if (manParamProdOrderSourceKey != null && SLibUtilities.compareKeys(manParamProdOrderSourceKey, ((SFormComponentItem) jcbProdOrder.getSelectedItem()).getPrimaryKey())) {
            validation.setComponent(jcbProdOrder);
            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlProdOrder.getText() + "'.");
        }

        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbProdOrder) {
                actionProdOrder();
            }
        }
        else if (e.getSource() instanceof JToggleButton) {
            if (!mbResetingForm) {
                JToggleButton toggleButton = (JToggleButton) e.getSource();

                if (toggleButton == jtbSwitchProdOrder) {
                    actionSwitchProdOrder();
                }
            }
        }
    }
}

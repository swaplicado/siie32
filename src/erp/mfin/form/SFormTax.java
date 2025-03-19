/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormTax.java
 *
 * Created on 19/10/2009, 05:11:41 PM
 */

package erp.mfin.form;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mfin.data.SDataTax;
import erp.mfin.data.diot.SDiotConsts;
import erp.mod.SModSysConsts;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SFormTax extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mfin.data.SDataTax moTax;
    private erp.lib.form.SFormField moFieldFkTaxTypeId;
    private erp.lib.form.SFormField moFieldFkTaxCalculationTypeId;
    private erp.lib.form.SFormField moFieldFkTaxApplicationTypeId;
    private erp.lib.form.SFormField moFieldTax;
    private erp.lib.form.SFormField moFieldPercentage;
    private erp.lib.form.SFormField moFieldValueUnitary;
    private erp.lib.form.SFormField moFieldValue;
    private erp.lib.form.SFormField moFieldVatType;
    private erp.lib.form.SFormField moFieldIsDeleted;

    /** Creates new form SFormTax */
    public SFormTax(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.FINU_TAX;

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

        jPanel1 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlTax = new javax.swing.JLabel();
        jtfTax = new javax.swing.JTextField();
        jlFkTaxTypeId = new javax.swing.JLabel();
        jcbFkTaxTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jlFkTaxCalculationTypeId = new javax.swing.JLabel();
        jcbFkTaxCalculationTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jlFkTaxApplicationTypeId = new javax.swing.JLabel();
        jcbTaxApplicationType = new javax.swing.JComboBox<SFormComponentItem>();
        jlPercentage = new javax.swing.JLabel();
        jtfPercentage = new javax.swing.JTextField();
        jlValueUnitary = new javax.swing.JLabel();
        jtfValueUnitary = new javax.swing.JTextField();
        jlValue = new javax.swing.JLabel();
        jtfValue = new javax.swing.JTextField();
        jlVatType = new javax.swing.JLabel();
        jcbVatType = new javax.swing.JComboBox<SFormComponentItem>();
        jckIsDeleted = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Impuesto");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(392, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jPanel1.add(jbCancel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel2.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel3.setLayout(new java.awt.GridLayout(9, 2, 5, 3));

        jlTax.setText("Impuesto: *");
        jPanel3.add(jlTax);

        jtfTax.setText("IMP");
        jPanel3.add(jtfTax);

        jlFkTaxTypeId.setText("Tipo de impuesto: *");
        jPanel3.add(jlFkTaxTypeId);

        jcbFkTaxTypeId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel3.add(jcbFkTaxTypeId);

        jlFkTaxCalculationTypeId.setText("Tipo de cálculo del impuesto: *");
        jPanel3.add(jlFkTaxCalculationTypeId);

        jcbFkTaxCalculationTypeId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkTaxCalculationTypeId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbFkTaxCalculationTypeIdItemStateChanged(evt);
            }
        });
        jPanel3.add(jcbFkTaxCalculationTypeId);

        jlFkTaxApplicationTypeId.setText("Tipo de aplicación del impuesto: *");
        jPanel3.add(jlFkTaxApplicationTypeId);

        jcbTaxApplicationType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel3.add(jcbTaxApplicationType);

        jlPercentage.setText("Tasa:");
        jPanel3.add(jlPercentage);

        jtfPercentage.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPercentage.setText("0.00 %");
        jPanel3.add(jtfPercentage);

        jlValueUnitary.setText("Monto fijo unitario:");
        jPanel3.add(jlValueUnitary);

        jtfValueUnitary.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfValueUnitary.setText("0.0000");
        jPanel3.add(jtfValueUnitary);

        jlValue.setText("Monto fijo:");
        jPanel3.add(jlValue);

        jtfValue.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfValue.setText("0.00");
        jPanel3.add(jtfValue);

        jlVatType.setText("Tipo de IVA:");
        jPanel3.add(jlVatType);

        jcbVatType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel3.add(jcbVatType);

        jckIsDeleted.setText("Registro eliminado");
        jPanel3.add(jckIsDeleted);

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(496, 339));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jcbFkTaxCalculationTypeIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbFkTaxCalculationTypeIdItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            itemStateChangedTaxCalculation();
        }
    }//GEN-LAST:event_jcbFkTaxCalculationTypeIdItemStateChanged

    @SuppressWarnings("deprecation")
    private void initComponentsExtra() {
        moFieldTax = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfTax, jlTax);
        moFieldTax.setLengthMax(50);
        moFieldFkTaxTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkTaxTypeId, jlFkTaxTypeId);
        moFieldFkTaxCalculationTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkTaxCalculationTypeId, jlFkTaxCalculationTypeId);
        moFieldFkTaxApplicationTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbTaxApplicationType, jlFkTaxApplicationTypeId);
        moFieldPercentage = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfPercentage, jlPercentage);
        moFieldPercentage.setIsPercent(true);
        moFieldPercentage.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsPercentageFormat());
        moFieldValueUnitary = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfValueUnitary, jlValueUnitary);
        moFieldValueUnitary.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormat());
        moFieldValue = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfValue, jlValue);
        moFieldValue.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat());
        moFieldVatType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbVatType, jlVatType);
        moFieldIsDeleted = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsDeleted);

        mvFields = new Vector<>();
        mvFields.add(moFieldTax);
        mvFields.add(moFieldFkTaxTypeId);
        mvFields.add(moFieldFkTaxCalculationTypeId);
        mvFields.add(moFieldFkTaxApplicationTypeId);
        mvFields.add(moFieldPercentage);
        mvFields.add(moFieldValueUnitary);
        mvFields.add(moFieldValue);
        mvFields.add(moFieldVatType);
        mvFields.add(moFieldIsDeleted);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        
        jcbVatType.removeAllItems();
        jcbVatType.addItem(new SFormComponentItem("", "(Seleccionar tipo IVA)"));
        jcbVatType.addItem(new SFormComponentItem(SDiotConsts.VAT_TYPE_EXEMPT, SDiotConsts.Vats.get(SDiotConsts.VAT_TYPE_EXEMPT)));
        jcbVatType.addItem(new SFormComponentItem(SDiotConsts.VAT_TYPE_RATE_0, SDiotConsts.Vats.get(SDiotConsts.VAT_TYPE_RATE_0)));
        jcbVatType.addItem(new SFormComponentItem(SDiotConsts.VAT_TYPE_GENERAL, SDiotConsts.Vats.get(SDiotConsts.VAT_TYPE_GENERAL)));
        jcbVatType.addItem(new SFormComponentItem(SDiotConsts.VAT_TYPE_BORDER, SDiotConsts.Vats.get(SDiotConsts.VAT_TYPE_BORDER)));
        jcbVatType.addItem(new SFormComponentItem(SDiotConsts.VAT_TYPE_BORDER_NORTH, SDiotConsts.Vats.get(SDiotConsts.VAT_TYPE_BORDER_NORTH)));
        jcbVatType.addItem(new SFormComponentItem(SDiotConsts.VAT_TYPE_BORDER_N, SDiotConsts.Vats.get(SDiotConsts.VAT_TYPE_BORDER_N)));
        jcbVatType.addItem(new SFormComponentItem(SDiotConsts.VAT_TYPE_BORDER_S, SDiotConsts.Vats.get(SDiotConsts.VAT_TYPE_BORDER_S)));

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
            jtfTax.requestFocus();
        }
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

    private void renderCalculationSettings() {
        jtfPercentage.setEnabled(false);
        jtfValueUnitary.setEnabled(false);
        jtfValue.setEnabled(false);

        switch (moFieldFkTaxCalculationTypeId.getKeyAsIntArray()[0]) {
            case SModSysConsts.FINS_TP_TAX_CAL_RATE:
                jtfPercentage.setEnabled(true);
                break;
            case SModSysConsts.FINS_TP_TAX_CAL_AMT_FIX_U:
                jtfValueUnitary.setEnabled(true);
                break;
            case SModSysConsts.FINS_TP_TAX_CAL_AMT_FIX:
                jtfValue.setEnabled(true);
                break;
            default:
        }
    }

    private void itemStateChangedTaxCalculation() {
        renderCalculationSettings();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkTaxCalculationTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkTaxTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbTaxApplicationType;
    private javax.swing.JComboBox<SFormComponentItem> jcbVatType;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JLabel jlFkTaxApplicationTypeId;
    private javax.swing.JLabel jlFkTaxCalculationTypeId;
    private javax.swing.JLabel jlFkTaxTypeId;
    private javax.swing.JLabel jlPercentage;
    private javax.swing.JLabel jlTax;
    private javax.swing.JLabel jlValue;
    private javax.swing.JLabel jlValueUnitary;
    private javax.swing.JLabel jlVatType;
    private javax.swing.JTextField jtfPercentage;
    private javax.swing.JTextField jtfTax;
    private javax.swing.JTextField jtfValue;
    private javax.swing.JTextField jtfValueUnitary;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        moTax = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        jckIsDeleted.setEnabled(false);
        renderCalculationSettings();
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbFkTaxTypeId, SDataConstants.FINS_TP_TAX);
        SFormUtilities.populateComboBox(miClient, jcbFkTaxCalculationTypeId, SDataConstants.FINS_TP_TAX_CAL);
        SFormUtilities.populateComboBox(miClient, jcbTaxApplicationType, SDataConstants.FINS_TP_TAX_APP);
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(((erp.lib.form.SFormField) mvFields.get(i)).getComponent());
                break;
            }
        }

        if (!validation.getIsError()) {
            // please note that percentage can be zero for rate-calculus type!
            
            // validate other calculus types:
            
            if (moFieldFkTaxCalculationTypeId.getKeyAsIntArray()[0] == SModSysConsts.FINS_TP_TAX_CAL_AMT_FIX_U && moFieldValueUnitary.getDouble() == 0) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlValueUnitary.getText() + "'.");
                validation.setComponent(jtfValueUnitary);
            }
            else if (moFieldFkTaxCalculationTypeId.getKeyAsIntArray()[0] == SModSysConsts.FINS_TP_TAX_CAL_AMT_FIX && moFieldValue.getDouble() == 0) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlValue.getText() + "'.");
                validation.setComponent(jtfValue);
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
        moTax = (SDataTax) registry;

        moFieldFkTaxTypeId.setFieldValue(new int[] { moTax.getFkTaxTypeId() });
        moFieldFkTaxCalculationTypeId.setFieldValue(new int[] { moTax.getFkTaxCalculationTypeId() });
        moFieldFkTaxApplicationTypeId.setFieldValue(new int[] { moTax.getFkTaxApplicationTypeId() });
        moFieldTax.setFieldValue(moTax.getTax());
        moFieldPercentage.setFieldValue(moTax.getPercentage());
        moFieldValueUnitary.setFieldValue(moTax.getValueUnitary());
        moFieldValue.setFieldValue(moTax.getValue());
        moFieldVatType.setFieldValue(moTax.getVatType());
        moFieldIsDeleted.setFieldValue(moTax.getIsDeleted());

        renderCalculationSettings();
        jckIsDeleted.setEnabled(true);
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (moTax == null) {
            moTax = new SDataTax();
            moTax.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moTax.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moTax.setFkTaxTypeId(moFieldFkTaxTypeId.getKeyAsIntArray()[0]);
        moTax.setFkTaxCalculationTypeId(moFieldFkTaxCalculationTypeId.getKeyAsIntArray()[0]);
        moTax.setFkTaxApplicationTypeId(moFieldFkTaxApplicationTypeId.getKeyAsIntArray()[0]);
        moTax.setTax(moFieldTax.getString());
        if (moFieldFkTaxCalculationTypeId.getKeyAsIntArray()[0] == SModSysConsts.FINS_TP_TAX_CAL_RATE) {
            moTax.setPercentage(moFieldPercentage.getDouble());
            moTax.setValueUnitary(0);
            moTax.setValue(0);
        }
        else if (moFieldFkTaxCalculationTypeId.getKeyAsIntArray()[0] == SModSysConsts.FINS_TP_TAX_CAL_AMT_FIX_U) {
            moTax.setPercentage(0);
            moTax.setValueUnitary(moFieldValueUnitary.getDouble());
            moTax.setValue(0);
        }
        else if (moFieldFkTaxCalculationTypeId.getKeyAsIntArray()[0] == SModSysConsts.FINS_TP_TAX_CAL_AMT_FIX) {
            moTax.setPercentage(0);
            moTax.setValueUnitary(0);
            moTax.setValue(moFieldValue.getDouble());
        }
        else {
            moTax.setPercentage(0);
            moTax.setValueUnitary(0);
            moTax.setValue(0);
        }

        moTax.setVatType((String) moFieldVatType.getFieldValue());
        moTax.setIsDeleted(moFieldIsDeleted.getBoolean());

        moTax.setDbmsTaxType(jcbFkTaxTypeId.getSelectedItem().toString());
        moTax.setDbmsTaxCalculationType(jcbFkTaxCalculationTypeId.getSelectedItem().toString());
        moTax.setDbmsTaxApplicationType(jcbTaxApplicationType.getSelectedItem().toString());

        return moTax;
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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
        }
    }
}

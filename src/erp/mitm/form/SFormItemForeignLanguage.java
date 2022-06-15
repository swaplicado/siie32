/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormItemForeignLanguage.java
 *
 * Created on 23/06/2010, 12:40:17 PM
 */

package erp.mitm.form;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mitm.data.SDataItemForeignLanguage;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;

/**
 *
 * @author Alfonso Flores
 */
public class SFormItemForeignLanguage extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mitm.data.SDataItemForeignLanguage moItemForeignLanguageDescription;
    private erp.lib.form.SFormField moFieldPkLanguageId;
    private erp.lib.form.SFormField moFieldItem;
    private erp.lib.form.SFormField moFieldItemShort;
    private erp.lib.form.SFormField moFieldIsDeleted;

    private boolean mbParamIsItemShortEnable;
    private java.util.Vector<Integer> mvParamLanguageIds;

    /** Creates new form SFormItemForeignLanguage */
    public SFormItemForeignLanguage(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.ITMU_CFG_ITEM_LAN;

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
        jlPkLanguageId = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jbPkLanguageId = new javax.swing.JButton();
        jcbPkLanguageId = new javax.swing.JComboBox();
        jlItem = new javax.swing.JLabel();
        jtfItem = new javax.swing.JTextField();
        jlItemShort = new javax.swing.JLabel();
        jtfItemShort = new javax.swing.JTextField();
        jckIsDeleted = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Descripción de ítem");
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
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.GridLayout(4, 2, 0, 1));

        jlPkLanguageId.setForeground(java.awt.Color.blue);
        jlPkLanguageId.setText("Idioma: *");
        jlPkLanguageId.setPreferredSize(new java.awt.Dimension(45, 23));
        jPanel3.add(jlPkLanguageId);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jbPkLanguageId.setText("jButton3");
        jbPkLanguageId.setToolTipText("Seleccionar idioma");
        jbPkLanguageId.setFocusable(false);
        jbPkLanguageId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbPkLanguageId, java.awt.BorderLayout.LINE_END);

        jcbPkLanguageId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel4.add(jcbPkLanguageId, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel4);

        jlItem.setText("Nombre ítem: *");
        jPanel3.add(jlItem);

        jtfItem.setText("ITEM DESCRIPTION");
        jPanel3.add(jtfItem);

        jlItemShort.setText("Nombre corto: *");
        jPanel3.add(jlItemShort);

        jtfItemShort.setText("ITEM SHORT");
        jPanel3.add(jtfItemShort);

        jckIsDeleted.setText("Registro eliminado");
        jPanel3.add(jckIsDeleted);

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-400)/2, (screenSize.height-300)/2, 400, 300);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        mvFields = new Vector<SFormField>();
        
        mvParamLanguageIds = new Vector<Integer>();

        moFieldPkLanguageId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPkLanguageId, jlPkLanguageId);
        moFieldPkLanguageId.setPickerButton(jbPkLanguageId);
        moFieldItem = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfItem, jlItem);
        moFieldItem.setLengthMax(255);
        moFieldItemShort = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfItemShort, jlItemShort);
        moFieldItemShort.setLengthMax(130);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsDeleted);

        mvFields.add(moFieldPkLanguageId);
        mvFields.add(moFieldItem);
        mvFields.add(moFieldItemShort);
        mvFields.add(moFieldIsDeleted);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbPkLanguageId.addActionListener(this);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), action, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            if (moItemForeignLanguageDescription != null ) {
                jtfItem.requestFocus();
            }
            else {
                jcbPkLanguageId.requestFocus();
            }
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

    private void actionPkLanguageId() {
        miClient.pickOption(SDataConstants.CFGU_LAN, moFieldPkLanguageId, null);
    }

    private void renderTextFieldItemShort() {
        if (mbParamIsItemShortEnable) {
            jtfItemShort.setEnabled(true);
        }
        else {
            jtfItemShort.setEnabled(false);
        }
    }

    private void renderComboBoxLanguage() {
        if (moItemForeignLanguageDescription.getPkItemId() > 0) {
            jcbPkLanguageId.setEnabled(false);
            jbPkLanguageId.setEnabled(false);
        }
        else {
            jcbPkLanguageId.setEnabled(true);
            jbPkLanguageId.setEnabled(true);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbPkLanguageId;
    private javax.swing.JComboBox jcbPkLanguageId;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JLabel jlItem;
    private javax.swing.JLabel jlItemShort;
    private javax.swing.JLabel jlPkLanguageId;
    private javax.swing.JTextField jtfItem;
    private javax.swing.JTextField jtfItemShort;
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

        moItemForeignLanguageDescription = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        mbParamIsItemShortEnable = false;
        jcbPkLanguageId.setEnabled(true);
        jbPkLanguageId.setEnabled(true);
        jckIsDeleted.setEnabled(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbPkLanguageId, SDataConstants.CFGU_LAN);
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
            if (mbParamIsItemShortEnable && jtfItemShort.getText().length() == 0) {
                validation.setMessage("La longitud del campo '" + jlItemShort.getText() + "' no puede ser menor a 1.");
                validation.setComponent(jtfItemShort);
            }
            else {
                for (int j = 0; j < mvParamLanguageIds.size(); j++) {
                    if (moFieldPkLanguageId.getKeyAsIntArray()[0] == mvParamLanguageIds.get(j) && moItemForeignLanguageDescription == null) {
                        validation.setMessage("Ya existe una configuración para el idioma seleccionado.");
                        validation.setComponent(jcbPkLanguageId);
                    }
                }
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
        moItemForeignLanguageDescription = (SDataItemForeignLanguage) registry;

        moFieldPkLanguageId.setFieldValue(new int[] { moItemForeignLanguageDescription.getPkLanguageId() });
        moFieldItem.setFieldValue(moItemForeignLanguageDescription.getItem());
        moFieldItemShort.setFieldValue(moItemForeignLanguageDescription.getItemShort());
        moFieldIsDeleted.setFieldValue(moItemForeignLanguageDescription.getIsDeleted());

        jckIsDeleted.setEnabled(true);
        renderTextFieldItemShort();
        renderComboBoxLanguage();
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (moItemForeignLanguageDescription == null) {
            moItemForeignLanguageDescription = new SDataItemForeignLanguage();
        }

        moItemForeignLanguageDescription.setPkLanguageId(moFieldPkLanguageId.getKeyAsIntArray()[0]);
        moItemForeignLanguageDescription.setItem(moFieldItem.getString());
        moItemForeignLanguageDescription.setItemShort(moFieldItemShort.getString());
        moItemForeignLanguageDescription.setDbmsLanguage(jcbPkLanguageId.getSelectedItem().toString());
        moItemForeignLanguageDescription.setIsDeleted(moFieldIsDeleted.getBoolean());

        return moItemForeignLanguageDescription;
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
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbPkLanguageId) {
                actionPkLanguageId();
            }
        }
    }

    public void setParamIsItemShortEnable(boolean b)  { mbParamIsItemShortEnable = b; renderTextFieldItemShort(); }
    public void setParamLanguageIds(java.util.Vector<Integer> v) { mvParamLanguageIds = v; }
}

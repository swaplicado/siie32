/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormElement.java
 *
 * Created on 24/08/2009, 10:29:02 AM
 */

package erp.mitm.form;

import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mitm.data.SDataElement;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 *
 * @author Alfonso Flores
 */
public class SFormElement extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mitm.data.SDataElement moElement;
    private erp.lib.form.SFormField moFieldFkElementTypeId;
    private erp.lib.form.SFormField moFieldElement;
    private erp.lib.form.SFormField moFieldCode;
    private erp.lib.form.SFormField moFieldIsDeleted;

    /** Creates new form SFormElement */
    public SFormElement(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.ITMU_EMT;

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

        jpRegistry = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlFkElementTypeId = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jbFkElementTypeId = new javax.swing.JButton();
        jcbFkElementTypeId = new javax.swing.JComboBox();
        jlElement = new javax.swing.JLabel();
        jtfElement = new javax.swing.JTextField();
        jlCode = new javax.swing.JLabel();
        jtfCode = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jckIsDeleted = new javax.swing.JCheckBox();
        jpCommand = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Elemento");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpRegistry.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridLayout(5, 2, 5, 5));

        jlFkElementTypeId.setText("Tipo de elemento: *");
        jPanel4.add(jlFkElementTypeId);

        jPanel5.setLayout(new java.awt.BorderLayout(5, 0));

        jbFkElementTypeId.setText("jButton1");
        jbFkElementTypeId.setToolTipText("Seleccionar tipo de elemento");
        jbFkElementTypeId.setFocusable(false);
        jbFkElementTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbFkElementTypeId, java.awt.BorderLayout.EAST);

        jcbFkElementTypeId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel5.add(jcbFkElementTypeId, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel5);

        jlElement.setText("Nombre de elemento: *");
        jPanel4.add(jlElement);

        jtfElement.setText("TEXT");
        jPanel4.add(jtfElement);

        jlCode.setText("Código de elemento: *");
        jPanel4.add(jlCode);

        jtfCode.setText("TEXT");
        jPanel4.add(jtfCode);
        jPanel4.add(jLabel1);
        jPanel4.add(jLabel2);
        jPanel4.add(jLabel3);

        jckIsDeleted.setForeground(new java.awt.Color(204, 0, 0));
        jckIsDeleted.setText("Registro eliminado");
        jPanel4.add(jckIsDeleted);

        jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

        jpRegistry.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpRegistry, java.awt.BorderLayout.CENTER);

        jpCommand.setPreferredSize(new java.awt.Dimension(400, 33));
        jpCommand.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpCommand.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jpCommand.add(jbCancel);

        getContentPane().add(jpCommand, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(400, 250));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        mvFields = new Vector<SFormField>();

        moFieldFkElementTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkElementTypeId, jlFkElementTypeId);
        moFieldFkElementTypeId.setPickerButton(jbFkElementTypeId);
        moFieldElement = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfElement, jlElement);
        moFieldElement.setLengthMax(50);
        moFieldCode = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfCode, jlCode);
        moFieldCode.setLengthMax(5);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsDeleted);

        mvFields.add(moFieldFkElementTypeId);
        mvFields.add(moFieldElement);
        mvFields.add(moFieldCode);
        mvFields.add(moFieldIsDeleted);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbFkElementTypeId.addActionListener(this);

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
            jcbFkElementTypeId.requestFocus();
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

    private void actionFkElementTypeId() {
        miClient.pickOption(SDataConstants.ITMU_TP_EMT, moFieldFkElementTypeId, null); // Check
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbFkElementTypeId;
    private javax.swing.JButton jbOk;
    private javax.swing.JComboBox jcbFkElementTypeId;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlElement;
    private javax.swing.JLabel jlFkElementTypeId;
    private javax.swing.JPanel jpCommand;
    private javax.swing.JPanel jpRegistry;
    private javax.swing.JTextField jtfCode;
    private javax.swing.JTextField jtfElement;
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

        moElement = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        jckIsDeleted.setEnabled(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbFkElementTypeId, SDataConstants.ITMU_TP_EMT);
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
            Object[] oParamsIn = new Object[] { moElement == null ? 0 : moElement.getPkElementId(), moFieldFkElementTypeId.getKeyAsIntArray()[0], moFieldElement.getString() };

            if (SDataUtilities.callProcedureVal(miClient, SProcConstants.ITMU_ELT_VAL, oParamsIn, SLibConstants.EXEC_MODE_VERBOSE) > 0) {
                if (miClient.showMsgBoxConfirm("El valor del campo '" + jlElement.getText() + "' ya existe, ¿desea conservalo? ") == JOptionPane.NO_OPTION) {
                    validation.setComponent(jtfElement);
                    validation.setIsError(true);
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
        moElement = (SDataElement) registry;

        moFieldFkElementTypeId.setFieldValue(new int[] { moElement.getFkElementTypeId() });
        moFieldElement.setFieldValue(moElement.getElement());
        moFieldCode.setFieldValue(moElement.getCode());
        moFieldIsDeleted.setFieldValue(moElement.getIsDeleted());

        if (moElement.getIsCanDelete()) {
            jckIsDeleted.setEnabled(true);
        }
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (moElement == null) {
            moElement = new SDataElement();
            moElement.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moElement.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moElement.setFkElementTypeId(moFieldFkElementTypeId.getKeyAsIntArray()[0]);
        moElement.setElement(moFieldElement.getString());
        moElement.setCode(moFieldCode.getString());
        moElement.setIsCanEdit(true);
        moElement.setIsCanDelete(true);
        moElement.setIsDeleted(moFieldIsDeleted.getBoolean());

        return moElement;
    }

    @Override
    public void setValue(int type, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getValue(int type) {
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
            else if (button == jbFkElementTypeId) {
                actionFkElementTypeId();
            }
        }
    }
}

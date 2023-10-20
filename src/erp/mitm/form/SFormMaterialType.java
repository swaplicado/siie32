/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormMaterialType.java
 *
 * Created on 16/10/2023, 10:29:02 AM
 */

package erp.mitm.form;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mitm.data.SDataMaterialType;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;

/**
 *
 * @author Edwin Carmona
 */
public class SFormMaterialType extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mitm.data.SDataMaterialType moMaterialType;
    private erp.lib.form.SFormField moFieldName;
    private erp.lib.form.SFormField moFieldCode;
    private erp.lib.form.SFormField moFieldPrefix;
    private erp.lib.form.SFormField moFieldIsDeleted;

    /** Creates new form SFormElement
     * @param client */
    public SFormMaterialType(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.ITMU_TP_MAT;

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
        jlName = new javax.swing.JLabel();
        jtfName = new javax.swing.JTextField();
        jlCode = new javax.swing.JLabel();
        jtfCode = new javax.swing.JTextField();
        jlPrefix = new javax.swing.JLabel();
        jtfPrefix = new javax.swing.JTextField();
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

        jlName.setText("Nombre de elemento: *");
        jPanel4.add(jlName);

        jtfName.setText("TEXT");
        jPanel4.add(jtfName);

        jlCode.setText("Código de elemento: *");
        jPanel4.add(jlCode);

        jtfCode.setText("TEXT");
        jPanel4.add(jtfCode);

        jlPrefix.setText("Prefijo:");
        jPanel4.add(jlPrefix);

        jtfPrefix.setText("TEXT");
        jPanel4.add(jtfPrefix);
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
        
        moFieldName = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfName, jlName);
        moFieldName.setLengthMax(50);
        moFieldCode = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfCode, jlCode);
        moFieldCode.setLengthMax(5);
        moFieldPrefix = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfPrefix, jlPrefix);
        moFieldPrefix.setLengthMax(50);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsDeleted);

        mvFields.add(moFieldName);
        mvFields.add(moFieldCode);
        mvFields.add(moFieldPrefix);
        mvFields.add(moFieldIsDeleted);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);

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
            jtfName.requestFocus();
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlName;
    private javax.swing.JLabel jlPrefix;
    private javax.swing.JPanel jpCommand;
    private javax.swing.JPanel jpRegistry;
    private javax.swing.JTextField jtfCode;
    private javax.swing.JTextField jtfName;
    private javax.swing.JTextField jtfPrefix;
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

        moMaterialType = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        jckIsDeleted.setEnabled(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void formRefreshCatalogues() {
        
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
        moMaterialType = (SDataMaterialType) registry;

        moFieldName.setFieldValue(moMaterialType.getName());
        moFieldCode.setFieldValue(moMaterialType.getCode());
        moFieldPrefix.setFieldValue(moMaterialType.getPrefix());
        moFieldIsDeleted.setFieldValue(moMaterialType.getIsDeleted());

        if (moMaterialType.isIsCanDelete()) {
            jckIsDeleted.setEnabled(true);
        }
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (moMaterialType == null) {
            moMaterialType = new SDataMaterialType();
            moMaterialType.setFkUserInsertId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moMaterialType.setFkUserUpdateId(miClient.getSession().getUser().getPkUserId());
        }

        moMaterialType.setName(moFieldName.getString());
        moMaterialType.setCode(moFieldCode.getString());
        moMaterialType.setPrefix(moFieldPrefix.getString());
        moMaterialType.setIsCanEdit(true);
        moMaterialType.setIsCanDelete(true);
        moMaterialType.setIsDeleted(moFieldIsDeleted.getBoolean());

        return moMaterialType;
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
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogFilterBizPartner.java
 *
 * Created on 11/03/2010, 08:20:22 AM
 */

package erp.musr.form;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 *
 * @author Juan Barajas
 */
public class SDialogFilterUser extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.lib.form.SFormField moFieldUserId;

    private int mnUserId;
    private int mnOptionPickerType;

    /** Creates new form SDialogFilterUser */
    public SDialogFilterUser(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnOptionPickerType = SDataConstants.USRU_USR;

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
        jckSelectAll = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jlUser = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jbUser = new javax.swing.JButton();
        jcbUser = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Usuarios");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(492, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Cancelar]");
        jPanel1.add(jbCancel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Opciones disponibles:"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.GridLayout(2, 1, 5, 5));

        jckSelectAll.setText("Todos los usuarios");
        jckSelectAll.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jckSelectAllItemStateChanged(evt);
            }
        });
        jPanel3.add(jckSelectAll);

        jPanel4.setLayout(new java.awt.BorderLayout(5, 0));

        jlUser.setText("Usuario: ");
        jlUser.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlUser, java.awt.BorderLayout.LINE_START);

        jPanel5.setLayout(new java.awt.BorderLayout(5, 0));

        jbUser.setText("jButton1");
        jbUser.setToolTipText("Seleccionar usuario");
        jbUser.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbUser, java.awt.BorderLayout.EAST);

        jcbUser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel5.add(jcbUser, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel4);

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-428)/2, (screenSize.height-334)/2, 428, 334);
    }// </editor-fold>//GEN-END:initComponents

    private void jckSelectAllItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jckSelectAllItemStateChanged
        if (!mbResetingForm) {
            itemStateChangedSelectAll();
        }
    }//GEN-LAST:event_jckSelectAllItemStateChanged

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        mvFields = new Vector<SFormField>();

        moFieldUserId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbUser, jlUser);
        moFieldUserId.setPickerButton(jbUser);

        mvFields.add(moFieldUserId);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbUser.addActionListener(this);

        SFormUtilities.createActionMap(getRootPane(), this, "actionOk", "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(getRootPane(), this, "actionCancel", "cancel", KeyEvent.VK_ESCAPE, SLibConstants.UNDEFINED);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;

            if (jcbUser.isEnabled()) {
                jcbUser.requestFocus();
            }
            else {
                jckSelectAll.requestFocus();
            }
        }
    }

    private void itemStateChangedSelectAll() {
        if (jckSelectAll.isSelected()) {
            jcbUser.setEnabled(false);
            jbUser.setEnabled(false);
            moFieldUserId.resetField();
        }
        else {
            jcbUser.setEnabled(true);
            jbUser.setEnabled(true);
        }
    }

    private void actionUser() {
        miClient.pickOption(mnOptionPickerType, moFieldUserId, null);
    }

    public void actionOk() {
        if (!jckSelectAll.isSelected() && jcbUser.getSelectedIndex() <= 0) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlUser.getText() + "'.");
            jcbUser.requestFocus();
        }
        else {
            mnUserId = jckSelectAll.isSelected() ? SLibConstants.UNDEFINED : moFieldUserId.getKeyAsIntArray()[0];

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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbUser;
    private javax.swing.JComboBox jcbUser;
    private javax.swing.JCheckBox jckSelectAll;
    private javax.swing.JLabel jlUser;
    // End of variables declaration//GEN-END:variables

    public void setUserId(int id) {
        mbResetingForm = true;

        mnUserId = id;
        jckSelectAll.setSelected(mnUserId == SLibConstants.UNDEFINED);
        moFieldUserId.setKey(mnUserId == SLibConstants.UNDEFINED ? null : new int[] { mnUserId });
        itemStateChangedSelectAll();

        mbResetingForm = false;
    }

    public int getUserId() {
        return mnUserId;
    }

    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        mnUserId = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbUser, SDataConstants.USRU_USR);
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
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
            else if (button == jbUser) {
                actionUser();
            }
        }
    }
}

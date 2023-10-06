/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogExportUser.java
 *
 * Created on 26/12/2009, 10:16:46 AM
 */

package erp.musr.form;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STablePane;
import erp.musr.data.SDataUserPrivilegeCompany;
import erp.musr.data.SDataUserPrivilegeCompanyRow;
import erp.musr.data.SDataUserPrivilegeRow;
import erp.musr.data.SDataUserPrivilegeUser;
import erp.musr.data.SDataUserRoleCompany;
import erp.musr.data.SDataUserRoleCompanyRow;
import erp.musr.data.SDataUserRoleRow;
import erp.musr.data.SDataUserRoleUser;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;

/**
 *
 * @author Adrián Avilés
 */
public class SDialogExportUser extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.musr.data.SDataUser moUser;
    SFormUserRightEntry moFormUserRightEntry;

    private erp.lib.table.STablePane moUserRolesPane;

    /** Creates new form SFormUserRight */
    public SDialogExportUser(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.USRX_RIGHT;

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
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jpUserRoles = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jlUser = new javax.swing.JLabel();
        jtfUser = new javax.swing.JTextField();
        jlAux = new javax.swing.JLabel();
        jlEmail = new javax.swing.JLabel();
        jtfEmail = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Derechos de usuario");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(792, 33));
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

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Acceso a aplicaciones:"));
        jPanel8.setPreferredSize(new java.awt.Dimension(771, 220));
        jPanel8.setLayout(new java.awt.BorderLayout(0, 5));

        jpUserRoles.setLayout(new java.awt.BorderLayout());
        jPanel8.add(jpUserRoles, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel8, java.awt.BorderLayout.NORTH);
        jPanel8.getAccessibleContext().setAccessibleName("aplicaciones asignadas");
        jPanel8.getAccessibleContext().setAccessibleDescription("");

        jPanel4.add(jPanel7, java.awt.BorderLayout.CENTER);
        jPanel7.getAccessibleContext().setAccessibleName("");

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel5.setPreferredSize(new java.awt.Dimension(776, 33));
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlUser.setText("Usuario:");
        jlUser.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel5.add(jlUser);

        jtfUser.setText("USER");
        jtfUser.setEnabled(false);
        jtfUser.setFocusable(false);
        jtfUser.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jtfUser);

        jlAux.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel5.add(jlAux);

        jlEmail.setText("Email:");
        jlEmail.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel5.add(jlEmail);

        jtfEmail.setEditable(false);
        jtfEmail.setText("EMAIL");
        jtfEmail.setFocusable(false);
        jtfEmail.setPreferredSize(new java.awt.Dimension(100, 23));
        jtfEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfEmailActionPerformed(evt);
            }
        });
        jPanel5.add(jtfEmail);

        jPanel2.add(jPanel5, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(656, 439));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jtfEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfEmailActionPerformed

    private void initComponentsExtra() {
        mvFields = new Vector<SFormField>();
        int i;

        moFormUserRightEntry = new SFormUserRightEntry(miClient);

        erp.lib.table.STableColumnForm tableColumnsUserRoles[];
        erp.lib.table.STableColumnForm tableColumnsUserPrivileges[];
        erp.lib.table.STableColumnForm tableColumnsCompanyRoles[];
        erp.lib.table.STableColumnForm tableColumnsCompanyPrivileges[];

        moUserRolesPane = new STablePane(miClient);
        moUserRolesPane.setDoubleClickAction(this, "publicActionModifyRole");
        jpUserRoles.add(moUserRolesPane, BorderLayout.CENTER);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);

        i = 0;
        tableColumnsUserRoles = new STableColumnForm[3];
        tableColumnsUserRoles[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo Rol", 300);
        tableColumnsUserRoles[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Rol", 300);
        tableColumnsUserRoles[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Nivel de acceso", 100);

        for (i = 0; i < tableColumnsUserRoles.length; i++) {
            moUserRolesPane.addTableColumn(tableColumnsUserRoles[i]);
        }

        SFormUtilities.createActionMap(rootPane, this, "publicActionAddRole", "delete", KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionModifyRole", "addDps", KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionDeleteRole", "removeDps", KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionAddPrivilege", "addNote", KeyEvent.VK_N, KeyEvent.SHIFT_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionModifyPrivilege", "modifyNote", KeyEvent.VK_M, KeyEvent.SHIFT_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionDeletePrivilege", "deleteNote", KeyEvent.VK_D, KeyEvent.SHIFT_DOWN_MASK);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), action, "esc", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        
    }

    private void actionOk() {
        erp.lib.form.SFormValidation validation = formValidate();

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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JLabel jlAux;
    private javax.swing.JLabel jlEmail;
    private javax.swing.JLabel jlUser;
    private javax.swing.JPanel jpUserRoles;
    private javax.swing.JTextField jtfEmail;
    private javax.swing.JTextField jtfUser;
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

        moUser = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        moUserRolesPane.createTable(null);
        moUserRolesPane.clearTableRows();
    }

    @Override
    public void formRefreshCatalogues() {

    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        erp.lib.form.SFormValidation validation = new SFormValidation();

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
        moUser = (erp.musr.data.SDataUser) registry;
        SDataUserRoleRow roleRow = null;
        SDataUserRoleCompanyRow roleCompanyRow = null;
        SDataUserPrivilegeRow privilegeRow = null;
        SDataUserPrivilegeCompanyRow privilegeCompanyRow = null;
        int i = 0;

        jtfUser.setText(moUser.getUser());

        // Read the user roles:

        for (i = 0; i < moUser.getDbmsUserRolesUser().size(); i++) {
            roleRow = new SDataUserRoleRow(moUser.getDbmsUserRolesUser().get(i));
            moUserRolesPane.addTableRow(roleRow);
        }
        if (i >= 1) {
            moUserRolesPane.setTableRowSelection(0);
        }
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        SDataUserRoleUser role = null;
        SDataUserRoleCompany roleCompany = null;
        SDataUserPrivilegeUser privilege = null;
        SDataUserPrivilegeCompany privilegeCompany = null;
        int i = 0;

        // Read the user roles:

        moUser.getDbmsUserRolesUser().clear();
        for (i = 0; i < moUserRolesPane.getTableGuiRowCount(); i++) {
            role = new SDataUserRoleUser();
            role = (SDataUserRoleUser) moUserRolesPane.getTableRow(i).getData();
            role.setPkUserId(moUser.getPkUserId());
            moUser.getDbmsUserRolesUser().add(role);
        }

        return moUser;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        
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
        }
    }
}
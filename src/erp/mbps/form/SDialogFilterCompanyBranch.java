/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogFilterCompanyBranch.java
 *
 * Created on 5/02/2010, 08:37:29 AM
 */

package erp.mbps.form;

import erp.lib.SLibConstants;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDialogFilterCompanyBranch extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.lib.form.SFormField moFieldCompanyBranchId;

    private int mnCompanyBranchId;

    /** Creates new form SDialogFilterCompanyBranch */
    public SDialogFilterCompanyBranch(erp.client.SClientInterface client) {
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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpOptions = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jckAllCompanyBranch = new javax.swing.JCheckBox();
        jckUniversalCompany = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jlCompanyBranch = new javax.swing.JLabel();
        jcbCompanyBranch = new javax.swing.JComboBox();
        jpControls = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jbSelectSession = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sucursales");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpOptions.setBorder(javax.swing.BorderFactory.createTitledBorder("Opciones disponibles:"));
        jpOptions.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel6.setLayout(new java.awt.BorderLayout());

        jckAllCompanyBranch.setText("Todas las sucursales");
        jckAllCompanyBranch.setPreferredSize(new java.awt.Dimension(125, 23));
        jckAllCompanyBranch.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jckAllCompanyBranchItemStateChanged(evt);
            }
        });
        jPanel6.add(jckAllCompanyBranch, java.awt.BorderLayout.CENTER);

        jckUniversalCompany.setText("Acceso universal");
        jckUniversalCompany.setEnabled(false);
        jckUniversalCompany.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel6.add(jckUniversalCompany, java.awt.BorderLayout.EAST);

        jPanel1.add(jPanel6);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jlCompanyBranch.setText("Sucursal de la empresa: *");
        jlCompanyBranch.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel3.add(jlCompanyBranch, java.awt.BorderLayout.WEST);

        jcbCompanyBranch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbCompanyBranch.setPreferredSize(new java.awt.Dimension(56, 23));
        jPanel3.add(jcbCompanyBranch, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3);

        jpOptions.add(jPanel1, java.awt.BorderLayout.NORTH);

        getContentPane().add(jpOptions, java.awt.BorderLayout.CENTER);

        jpControls.setPreferredSize(new java.awt.Dimension(392, 33));
        jpControls.setLayout(new java.awt.GridLayout(1, 2));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jbSelectSession.setText("Sesión");
        jbSelectSession.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jbSelectSession);

        jpControls.add(jPanel2);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jPanel4.add(jbCancel);

        jpControls.add(jPanel4);

        getContentPane().add(jpControls, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-488)/2, (screenSize.height-334)/2, 488, 334);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jckAllCompanyBranchItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jckAllCompanyBranchItemStateChanged
        if (!mbResetingForm) {
            if (jckAllCompanyBranch.isSelected()) {
                jcbCompanyBranch.setEnabled(false);
                jcbCompanyBranch.setSelectedIndex(0);
            } else {
                jcbCompanyBranch.setEnabled(true);
            }
        }
}//GEN-LAST:event_jckAllCompanyBranchItemStateChanged

    private void initComponentsExtra() {
        jckUniversalCompany.setSelected(miClient.getSessionXXX().getIsUniversal() || miClient.getSessionXXX().getIsUniversalCompany(miClient.getSessionXXX().getCompany().getPkCompanyId()));
        jckAllCompanyBranch.setEnabled(jckUniversalCompany.isSelected());

        moFieldCompanyBranchId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCompanyBranch, jlCompanyBranch);

        mvFields = new Vector<SFormField>();
        mvFields.add(moFieldCompanyBranchId);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbSelectSession.addActionListener(this);

        SFormUtilities.createActionMap(getRootPane(), this, "actionOk", "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(getRootPane(), this, "actionCancel", "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;

            if (jcbCompanyBranch.isEnabled()) {
                jcbCompanyBranch.requestFocus();
            }
            else {
                jbOk.requestFocus();
            }
        }
    }

    private boolean isUniversalCompany() {
        return jckUniversalCompany.isSelected();
    }

    public void actionOk() {
        if (!jckAllCompanyBranch.isSelected() && jcbCompanyBranch.getSelectedIndex() <= 0) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlCompanyBranch.getText() + "'.");
            jcbCompanyBranch.requestFocus();
        }
        else {
            mnCompanyBranchId = jckAllCompanyBranch.isSelected() ? SLibConstants.UNDEFINED : moFieldCompanyBranchId.getKeyAsIntArray()[0];

            mnFormResult = SLibConstants.FORM_RESULT_OK;
            setVisible(false);
        }
    }

    public void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    public void actionSelectSession() {
        setCompanyBranchId(miClient.getSessionXXX().getCurrentCompanyBranchId());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbSelectSession;
    private javax.swing.JComboBox jcbCompanyBranch;
    private javax.swing.JCheckBox jckAllCompanyBranch;
    private javax.swing.JCheckBox jckUniversalCompany;
    private javax.swing.JLabel jlCompanyBranch;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpOptions;
    // End of variables declaration//GEN-END:variables

    public void setCompanyBranchId(int id) {
        mnCompanyBranchId = id;

        jckAllCompanyBranch.setSelected(mnCompanyBranchId == SLibConstants.UNDEFINED && isUniversalCompany());
        jckAllCompanyBranchItemStateChanged(null);

        moFieldCompanyBranchId.setKey(mnCompanyBranchId == SLibConstants.UNDEFINED ? null : new int[] { mnCompanyBranchId });
    }

    public int getCompanyBranchId() {
        return mnCompanyBranchId;
    }

    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void formReset() {
        mbResetingForm = true;

        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        mnCompanyBranchId = SLibConstants.UNDEFINED;

        jckAllCompanyBranch.setSelected(false);

        mbResetingForm = false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void formRefreshCatalogues() {
        Vector<SFormComponentItem> items = null;

        if (isUniversalCompany()) {
            items = miClient.getSessionXXX().getAllCompanyBranchesAsComponentItems(true);
        }
        else {
            items = miClient.getSessionXXX().getUserCompanyBranchesAsComponentItems(true);
        }

        jcbCompanyBranch.removeAllItems();
        for (SFormComponentItem item : items) {
            jcbCompanyBranch.addItem(item);
        }
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
            else if (button == jbSelectSession) {
                actionSelectSession();
            }
        }
    }
}

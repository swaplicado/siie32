/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SPanelAccount.java
 *
 * Created on 5/02/2010, 01:11:08 PM
 */

package erp.mfin.form;

import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.form.SFormFieldAccountId;
import erp.form.SFormFieldCostCenterId;
import erp.lib.SLibConstants;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataCostCenter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;

/**
 *
 * @author Sergio Flores
 */
public class SPanelAccount extends javax.swing.JPanel implements java.awt.event.ActionListener, java.awt.event.KeyListener {

    private erp.client.SClientInterface miClient;
    private erp.form.SFormFieldAccount moFieldAccount;

    private erp.mfin.data.SDataAccount moAccount;
    private erp.mfin.data.SDataAccount moAccountMajor;
    private erp.mfin.data.SDataCostCenter moCostCenter;
    private erp.mfin.data.SDataCostCenter moCostCenterMajor;
    private java.lang.String msEmptyAccountId;
    private java.util.Vector<Integer> mvAccountLevels;

    /**
     * Creates new form SPanelAccount
     *
     * @param client Client interface.
     * @param accountType Account type can be SDataConstants.FIN_ACC or SDataConstants.FIN_CC.
     * @param isForNewAccounts Flag that defines if panel is pretended for new account ID's.
     * @param isMandatory Is account mandatory?
     * @param isNonEditableColorLabel Set field label in "non-editable" representing color.
     */
    public SPanelAccount(erp.client.SClientInterface client, int accountType, boolean isForNewAccounts, boolean isMandatory, boolean isNonEditableColorLabel) throws java.lang.Exception {
        miClient = client;
        initComponents();
        initComponentsExtra();

        switch (accountType) {
            case SDataConstants.FIN_ACC:
                jlAccountId.setText("No. cuenta contable:");
                jlAccount.setText("Cuenta contable:");
                jbAccountId.setToolTipText("Seleccionar cuenta contable");
                jftAccountId.setToolTipText("[F5: Seleccionar cuenta contable]");
                moFieldAccount = new SFormFieldAccountId(miClient, miClient.getSessionXXX().getParamsErp().getDeepAccounts(), jftAccountId, isForNewAccounts);
                msEmptyAccountId = SDataUtilities.createNewFormattedAccountId(miClient, miClient.getSessionXXX().getParamsErp().getDeepAccounts());
                break;

            case SDataConstants.FIN_CC:
                jlAccountId.setText("No. centro costo:");
                jlAccount.setText("Centro costo:");
                jbAccountId.setToolTipText("Seleccionar centro costo");
                jftAccountId.setToolTipText("[F5: Seleccionar centro costo]");
                moFieldAccount = new SFormFieldCostCenterId(miClient, miClient.getSessionXXX().getParamsErp().getDeepCostCenters(), jftAccountId, isForNewAccounts);
                msEmptyAccountId = SDataUtilities.createNewFormattedCostCenterId(miClient, miClient.getSessionXXX().getParamsErp().getDeepCostCenters());
                break;

            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        if (isMandatory) {
            jlAccountId.setText(jlAccountId.getText() + " *");
        }

        if (isNonEditableColorLabel) {
            jlAccountId.setForeground(Color.blue);
        }

        //moChooserAccount = new SChooserAccount(miClient, this, 8);
        mvAccountLevels = SDataUtilities.getAccountLevels(msEmptyAccountId);
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
        jPanel2 = new javax.swing.JPanel();
        jlAccountId = new javax.swing.JLabel();
        jftAccountId = new javax.swing.JFormattedTextField();
        jbAccountId = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jlAccount = new javax.swing.JLabel();
        jtfAccount = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout(2, 1, 0, 1));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlAccountId.setText("No. cuenta:");
        jlAccountId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel2.add(jlAccountId);

        jftAccountId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jftAccountId.setText("9999-999-999-99-99");
        jftAccountId.setToolTipText("[F5: Seleccionar cuenta]");
        jftAccountId.setMinimumSize(new java.awt.Dimension(6, 23));
        jftAccountId.setPreferredSize(new java.awt.Dimension(175, 23));
        jftAccountId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jftAccountIdFocusLost(evt);
            }
        });
        jftAccountId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jftAccountIdKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jftAccountIdKeyTyped(evt);
            }
        });
        jPanel2.add(jftAccountId);

        jbAccountId.setText("...");
        jbAccountId.setToolTipText("Seleccionar cuenta");
        jbAccountId.setFocusable(false);
        jbAccountId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel2.add(jbAccountId);

        jPanel1.add(jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 2, 0, 0));
        jPanel3.setLayout(new java.awt.BorderLayout(2, 0));

        jlAccount.setText("Cuenta:");
        jlAccount.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel3.add(jlAccount, java.awt.BorderLayout.WEST);

        jtfAccount.setEditable(false);
        jtfAccount.setText("MAJOR ACCOUNT");
        jtfAccount.setFocusable(false);
        jtfAccount.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel3.add(jtfAccount, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void jftAccountIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jftAccountIdFocusLost
        updateAccount();
    }//GEN-LAST:event_jftAccountIdFocusLost

    private void jftAccountIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jftAccountIdKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            actionAccountId();
        }
        /*
        else if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_KP_DOWN) {
            if (moChooserAccount.isVisible()) {
                moChooserAccount.requestFocus();
            }
        }
        else {
            Rectangle rectangle = jftAccountId.getBounds();
            String filter = jftAccountId.getText().substring(0, jftAccountId.getCaretPosition());

            if (SLibUtilities.parseLong(filter.replace("-", "")) > 0) {
                moChooserAccount.showChooser(rectangle.x, rectangle.y + rectangle.height, filter, null);
            }
        }
        */
    }//GEN-LAST:event_jftAccountIdKeyReleased

    private void jftAccountIdKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jftAccountIdKeyTyped
        /**/
    }//GEN-LAST:event_jftAccountIdKeyTyped

    private void initComponentsExtra() {
        jbAccountId.addActionListener(this);
        jftAccountId.addActionListener(this);
    }

    private void updateAccount() {
        Object[] accountsAndDescription = null;

        moAccountMajor = null;
        moAccount = null;
        moCostCenterMajor = null;
        moCostCenter = null;

        jtfAccount.setText("");

        if (msEmptyAccountId.compareTo(moFieldAccount.getString()) != 0) {
            if (moFieldAccount.getAccountType() == SDataConstants.FIN_ACC) {
                accountsAndDescription = SDataUtilities.getInputAccountsAndDescription(miClient, moFieldAccount.getString(), mvAccountLevels);
                moAccountMajor = (SDataAccount) accountsAndDescription[0];
                moAccount = (SDataAccount) accountsAndDescription[1];
                jtfAccount.setText((String) accountsAndDescription[2]);
            }
            else {
                accountsAndDescription = SDataUtilities.getInputCostCentersAndDescription(miClient, moFieldAccount.getString(), mvAccountLevels);
                moCostCenterMajor = (SDataCostCenter) accountsAndDescription[0];
                moCostCenter = (SDataCostCenter) accountsAndDescription[1];
                jtfAccount.setText((String) accountsAndDescription[2]);
            }
        }

        jtfAccount.setCaretPosition(0);
        jtfAccount.setToolTipText(jtfAccount.getText().length() == 0 ? null : jtfAccount.getText());
    }

    private void actionAccountId() {
        if (miClient.pickOption(moFieldAccount.getAccountType(), moFieldAccount, null) == SLibConstants.FORM_RESULT_OK) {
            updateAccount();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton jbAccountId;
    private javax.swing.JFormattedTextField jftAccountId;
    private javax.swing.JLabel jlAccount;
    private javax.swing.JLabel jlAccountId;
    private javax.swing.JTextField jtfAccount;
    // End of variables declaration//GEN-END:variables

    public void setReadOnly(final boolean readOnly) {
        jftAccountId.setEditable(!readOnly);
        jftAccountId.setFocusable(!readOnly);
        jbAccountId.setEnabled(!readOnly);
        jbAccountId.setVisible(!readOnly);
    }
    
    public void setLabelsWidth(final int width) {
        jlAccount.setPreferredSize(new Dimension(width, 23));
        jlAccountId.setPreferredSize(new Dimension(width, 23));
    }

    public erp.form.SFormFieldAccount getFieldAccount() { return moFieldAccount; }
    public erp.mfin.data.SDataAccount getDataAccountMajor() { return moAccountMajor; }
    public erp.mfin.data.SDataCostCenter getDataCostCenterMajor() { return moCostCenterMajor; }
    public java.lang.String getEmptyAccountId() { return msEmptyAccountId; }
    public java.lang.String getAccountDescription() { return jtfAccount.getText(); }
    public javax.swing.JLabel getFieldAccountLabel() { return jlAccountId; }

    public boolean isEmptyAccountId() {
        return msEmptyAccountId.compareTo(moFieldAccount.getString()) == 0;
    }

    public void resetPanel() {
        moFieldAccount.setFieldValue(msEmptyAccountId);
        updateAccount();
    }

    public void refreshPanel() {
        updateAccount();
    }

    public void enableFields(boolean enable) {
        jftAccountId.setEnabled(enable);
        jbAccountId.setEnabled(enable);
    }

    /**
     * SPanelAccount has two SDataAccount objects:
     *
     * a) account,
     * b) major account.
     *
     * In some cases, real input is done at major account if deep of this account is one.
     * @return If exists, account, otherwise major account.
     */
    public erp.mfin.data.SDataAccount getCurrentInputAccount() {
        return moAccount != null ? moAccount : moAccountMajor;
    }

    /**
     * SPanelAccount has two SDataCostCenter objects:
     *
     * a) cost center,
     * b) major cost center.
     *
     * In some cases, real input is done at major cost center if deep of this cost center is one.
     * @return If exists, cost center, otherwise major cost center.
     */
    public erp.mfin.data.SDataCostCenter getCurrentInputCostCenter() {
        return moCostCenter != null ? moCostCenter : moCostCenterMajor;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbAccountId) {
                actionAccountId();
            }
        }
        else if (e.getSource() instanceof javax.swing.JFormattedTextField) {
            JFormattedTextField formattedTextField = (JFormattedTextField) e.getSource();

            if (formattedTextField == jftAccountId) {
                updateAccount();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

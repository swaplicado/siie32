/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.form;

import erp.client.SClientInterface;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.trn.db.SDbMaintUserSupervisor;
import erp.mtrn.data.STrnMaintConstants;
import erp.mtrn.data.STrnMaintUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;

/**
 *
 * @author Gil De Jesús, Sergio Flores
 */
public class SFormMaintUserSupervisor extends SBeanForm implements ActionListener {

    private SDbMaintUserSupervisor moRegistry;
    private byte[] maFingerprintBytes;

    /**
     * Creates new form SFormMaintUserSupv
     */
    public SFormMaintUserSupervisor(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.TRN_MAINT_USER_SUPV, SLibConsts.UNDEFINED, title);
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlMaintUserContractor = new javax.swing.JLabel();
        moKeyMaintUserContractor = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel5 = new javax.swing.JPanel();
        jlName = new javax.swing.JLabel();
        moTextName = new sa.lib.gui.bean.SBeanFieldText();
        jPanel6 = new javax.swing.JPanel();
        jbFingerprintEnroll = new javax.swing.JButton();
        jtfFingerprintStatus = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jbFingerprintVerify = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jbFingerprintDelete = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMaintUserContractor.setText("Contratista:*");
        jlMaintUserContractor.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlMaintUserContractor);

        moKeyMaintUserContractor.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel4.add(moKeyMaintUserContractor);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlName.setText("Residente:*");
        jlName.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlName);

        moTextName.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel5.add(moTextName);

        jPanel2.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbFingerprintEnroll.setText("Capturar huella digital");
        jbFingerprintEnroll.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel6.add(jbFingerprintEnroll);

        jtfFingerprintStatus.setEditable(false);
        jtfFingerprintStatus.setFocusable(false);
        jtfFingerprintStatus.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel6.add(jtfFingerprintStatus);

        jPanel2.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbFingerprintVerify.setText("Verificar huella digital");
        jbFingerprintVerify.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel7.add(jbFingerprintVerify);

        jPanel2.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbFingerprintDelete.setText("Borrar huella digital");
        jbFingerprintDelete.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel8.add(jbFingerprintDelete);

        jPanel2.add(jPanel8);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbFingerprintDelete;
    private javax.swing.JButton jbFingerprintEnroll;
    private javax.swing.JButton jbFingerprintVerify;
    private javax.swing.JLabel jlMaintUserContractor;
    private javax.swing.JLabel jlName;
    private javax.swing.JTextField jtfFingerprintStatus;
    private sa.lib.gui.bean.SBeanFieldKey moKeyMaintUserContractor;
    private sa.lib.gui.bean.SBeanFieldText moTextName;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 480, 300);
        
        moKeyMaintUserContractor.setKeySettings(miClient, SGuiUtils.getLabelName(jlMaintUserContractor), true);
        moTextName.setTextSettings(SGuiUtils.getLabelName(jlName.getText()), 100);

        moFields.addField(moKeyMaintUserContractor);
        moFields.addField(moTextName);

        moFields.setFormButton(jbSave);
    }

    private void showFingerprintStatus() {
        jtfFingerprintStatus.setText(maFingerprintBytes != null ? STrnMaintConstants.FINGERPRINT_WITH : STrnMaintConstants.FINGERPRINT_WITHOUT);
        jtfFingerprintStatus.setCaretPosition(0);
        
        jbFingerprintEnroll.setEnabled(maFingerprintBytes == null);
        jbFingerprintVerify.setEnabled(maFingerprintBytes != null);
        jbFingerprintDelete.setEnabled(maFingerprintBytes != null);
    }
    
    private void actionFingerprintEnroll() {
        maFingerprintBytes = STrnMaintUtilities.enrollFingerprint((SClientInterface) miClient);
        showFingerprintStatus();
        jbFingerprintVerify.requestFocus();
    }

    private void actionFingerprintVerify() {
        if (STrnMaintUtilities.verifyFingerprint((SClientInterface) miClient, maFingerprintBytes)) {
            miClient.showMsgBoxInformation(STrnMaintConstants.VERIFIED);
        }
        else {
            miClient.showMsgBoxError(STrnMaintConstants.NONVERIFIED);
        }
    }

    private void actionFingerprintDelete() {
        if (miClient.showMsgBoxConfirm("¿Borrar huella digital?") == JOptionPane.YES_OPTION) {
            maFingerprintBytes = null;
            showFingerprintStatus();
            jbFingerprintEnroll.requestFocus();
        }
    }

    @Override
    public void addAllListeners() {
        jbFingerprintEnroll.addActionListener(this);
        jbFingerprintVerify.addActionListener(this);
        jbFingerprintDelete.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbFingerprintEnroll.removeActionListener(this);
        jbFingerprintVerify.removeActionListener(this);
        jbFingerprintDelete.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyMaintUserContractor, SModConsts.TRN_MAINT_USER, SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbMaintUserSupervisor) registry;
        
        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }
        
        moKeyMaintUserContractor.setValue(new int[] { moRegistry.getFkMaintUserId_n() });
        moTextName.setValue(moRegistry.getName());
        maFingerprintBytes = moRegistry.getFingerprint_n() == null ? null : moRegistry.getFingerprint_n().getBytes(1, (int) moRegistry.getFingerprint_n().length());
        showFingerprintStatus();

        setFormEditable(true);
        
        if (moRegistry.isRegistryNew()) {
            
        }
        else {
            
        }

        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbMaintUserSupervisor registry = moRegistry.clone();

        if (registry.isRegistryNew()) { }

        registry.setName(moTextName.getValue());
        //registry.setFingerprint_n(...);
        registry.setFkMaintUserId_n(moKeyMaintUserContractor.getValue()[0]);
        registry.setAuxFingerprintBytes(maFingerprintBytes);

        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbFingerprintEnroll) {
                actionFingerprintEnroll();
            }
            else if (button == jbFingerprintVerify) {
                actionFingerprintVerify();
            }
            else if (button == jbFingerprintDelete) {
                actionFingerprintDelete();
            }
        }
    }
}
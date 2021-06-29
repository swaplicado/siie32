/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormElement.java
 *
 * Created on 12/11/2010
 */

package erp.mtrn.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mtrn.data.SDataBizPartnerBlocking;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;

/**
 *
 * @author Néstor Ávalos
 */
public class SFormBizPartnerBlocking extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mtrn.data.SDataBizPartnerBlocking moBizPartnerBlocking;
    private erp.lib.form.SFormField moFieldFkBizPartnerId;
    private erp.lib.form.SFormField moFieldIsBlocked;
    private erp.lib.form.SFormField moFieldIsDeleted;

    private int mnParamCategoryBpId;

    /** Creates new form SFormElement */
    public SFormBizPartnerBlocking(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient =  client;
        mnFormType = SDataConstants.TRN_BP_BLOCK;

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
        jbOK = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel98 = new javax.swing.JPanel();
        jlFkBizPartnerId = new javax.swing.JLabel();
        jcbFkBizPartnerId = new javax.swing.JComboBox();
        jbFkBizPartnerId = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jckIsBlocked = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jckIsDeleted = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(400, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOK.setText("Aceptar");
        jbOK.setToolTipText("[Ctrl + Enter]");
        jbOK.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbOK);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jPanel1.add(jbCancel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel2.setLayout(new java.awt.GridLayout(5, 2));

        jPanel98.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkBizPartnerId.setText("[Asoc. de neg.:]");
        jlFkBizPartnerId.setPreferredSize(new java.awt.Dimension(80, 14));
        jPanel98.add(jlFkBizPartnerId);

        jcbFkBizPartnerId.setPreferredSize(new java.awt.Dimension(315, 23));
        jPanel98.add(jcbFkBizPartnerId);

        jbFkBizPartnerId.setText("...");
        jbFkBizPartnerId.setToolTipText("Seleccionar proveedor");
        jbFkBizPartnerId.setFocusable(false);
        jbFkBizPartnerId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel98.add(jbFkBizPartnerId);

        jPanel2.add(jPanel98);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jckIsBlocked.setText("Bloqueado");
        jPanel5.add(jckIsBlocked);

        jPanel2.add(jPanel5);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jckIsDeleted.setText("Registro eliminado");
        jPanel7.add(jckIsDeleted);

        jPanel2.add(jPanel7);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-449)/2, (screenSize.height-249)/2, 449, 249);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        mvFields = new Vector<SFormField>();

        moFieldFkBizPartnerId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkBizPartnerId, jlFkBizPartnerId);
        moFieldFkBizPartnerId.setPickerButton(jbFkBizPartnerId);
        moFieldIsBlocked = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsBlocked);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsDeleted);

        mvFields.add(moFieldFkBizPartnerId);
        mvFields.add(moFieldIsBlocked);
        mvFields.add(moFieldIsDeleted);

        jbFkBizPartnerId.addActionListener(this);
        jbOK.addActionListener(this);
        jbCancel.addActionListener(this);

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
            jcbFkBizPartnerId.requestFocus();
        }
    }

    private void actionFkBizPartnerId() {
        miClient.pickOption(mnParamCategoryBpId == SDataConstantsSys.BPSS_CT_BP_SUP ? SDataConstants.BPSX_BP_SUP : SDataConstants.BPSX_BP_CUS, moFieldFkBizPartnerId, moFieldFkBizPartnerId.getKey());
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel98;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbFkBizPartnerId;
    private javax.swing.JButton jbOK;
    private javax.swing.JComboBox jcbFkBizPartnerId;
    private javax.swing.JCheckBox jckIsBlocked;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JLabel jlFkBizPartnerId;
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

        moBizPartnerBlocking = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        jckIsDeleted.setEnabled(false);
        jckIsBlocked.setSelected(true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbFkBizPartnerId, mnParamCategoryBpId == SDataConstantsSys.BPSS_CT_BP_SUP ? SDataConstants.BPSX_BP_SUP : SDataConstants.BPSX_BP_CUS);
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
        moBizPartnerBlocking = (SDataBizPartnerBlocking) registry;

        moFieldFkBizPartnerId.setKey(new int[] { moBizPartnerBlocking.getFkBizPartnerId() });
        moFieldIsBlocked.setFieldValue(moBizPartnerBlocking.getIsBlocked());
        moFieldIsDeleted.setFieldValue(moBizPartnerBlocking.getIsDeleted());

        jckIsDeleted.setEnabled(true);
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (moBizPartnerBlocking == null) {
            moBizPartnerBlocking = new SDataBizPartnerBlocking();
            moBizPartnerBlocking.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moBizPartnerBlocking.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moBizPartnerBlocking.setFkBizPartnerId(moFieldFkBizPartnerId.getKeyAsIntArray()[0]);
        moBizPartnerBlocking.setFkBizPartnerCategoryId(mnParamCategoryBpId);
        moBizPartnerBlocking.setIsBlocked(moFieldIsBlocked.getBoolean());
        moBizPartnerBlocking.setIsDeleted(moFieldIsDeleted.getBoolean());

        return moBizPartnerBlocking;
    }

    @Override
    public void setValue(int type, Object value) {
        switch (type) {
            case 1:
                mnParamCategoryBpId = (Integer) value;
                jlFkBizPartnerId.setText((mnParamCategoryBpId == SDataConstantsSys.BPSS_CT_BP_SUP ? "Proveedor: * " : mnParamCategoryBpId == SDataConstantsSys.BPSS_CT_BP_CUS ? "Cliente: * " : ""));
                this.setTitle((mnParamCategoryBpId == SDataConstantsSys.BPSS_CT_BP_SUP ? "Bloqueo de proveedor" : mnParamCategoryBpId == SDataConstantsSys.BPSS_CT_BP_CUS ?  "Bloqueo de cliente" : "" ));
                jbFkBizPartnerId.setToolTipText((mnParamCategoryBpId == SDataConstantsSys.BPSS_CT_BP_SUP ? "Seleccionar proveedor" : mnParamCategoryBpId == SDataConstantsSys.BPSS_CT_BP_CUS ? "Seleccionar cliente" : ""));
                break;
        }
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

            if (button == jbOK) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbFkBizPartnerId) {
                actionFkBizPartnerId();
            }
        }
    }
}

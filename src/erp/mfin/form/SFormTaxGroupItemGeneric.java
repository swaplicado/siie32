/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormTaxGroupItemGeneric.java
 *
 * Created on 15/10/2009, 11:46:37 AM
 */

package erp.mfin.form;

import erp.data.SDataConstants;
import erp.data.SDataReadDescriptions;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mfin.data.SDataTaxGroupItemGeneric;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;

/**
 *
 * @author Sergio Flores
 */
public class SFormTaxGroupItemGeneric extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mfin.data.SDataTaxGroupItemGeneric moTaxGroupItemGeneric;
    private erp.lib.form.SFormField moFieldPkTaxRegionId;
    private erp.lib.form.SFormField moFieldPkDateStart;
    private erp.lib.form.SFormField moFieldFkTaxGroupId;
    private erp.lib.form.SFormField moFieldIsDeleted;

    private int mnParamItemGenericId;

    /** Creates new form SFormTaxRegion */
    public SFormTaxGroupItemGeneric(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.FIN_TAX_GRP_IGEN;

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

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlItemGeneric = new javax.swing.JLabel();
        jtfItemGeneric = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jlPkTaxRegionId = new javax.swing.JLabel();
        jcbPkTaxRegionId = new javax.swing.JComboBox();
        jbPkTaxRegionId = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jlPkDateStart = new javax.swing.JLabel();
        jftPkDateStart = new javax.swing.JFormattedTextField();
        jbPkDateStart = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jlFkTaxGroupId = new javax.swing.JLabel();
        jcbFkTaxGroupId = new javax.swing.JComboBox();
        jbFkTaxGroupId = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jlDummy01 = new javax.swing.JLabel();
        jckIsDeleted = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Región de impuestos y grupo de impuestos");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.GridLayout(6, 1, 0, 1));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlItemGeneric.setText("Ítem genérico:");
        jlItemGeneric.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel4.add(jlItemGeneric);

        jtfItemGeneric.setEditable(false);
        jtfItemGeneric.setText("NAME");
        jtfItemGeneric.setFocusable(false);
        jtfItemGeneric.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel4.add(jtfItemGeneric);

        jPanel3.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlPkTaxRegionId.setForeground(java.awt.Color.blue);
        jlPkTaxRegionId.setText("Región de impuestos: *");
        jlPkTaxRegionId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel5.add(jlPkTaxRegionId);

        jcbPkTaxRegionId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbPkTaxRegionId.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel5.add(jcbPkTaxRegionId);

        jbPkTaxRegionId.setText("...");
        jbPkTaxRegionId.setToolTipText("Seleccionar región de impuestos");
        jbPkTaxRegionId.setFocusable(false);
        jbPkTaxRegionId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbPkTaxRegionId);

        jPanel3.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlPkDateStart.setForeground(java.awt.Color.blue);
        jlPkDateStart.setText("Fecha inicial vigencia: *");
        jlPkDateStart.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel6.add(jlPkDateStart);

        jftPkDateStart.setText("yyyy/mm/dd");
        jftPkDateStart.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel6.add(jftPkDateStart);

        jbPkDateStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbPkDateStart.setToolTipText("Seleccionar fecha");
        jbPkDateStart.setFocusable(false);
        jbPkDateStart.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbPkDateStart);

        jPanel3.add(jPanel6);
        jPanel3.add(jPanel9);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkTaxGroupId.setText("Grupo de impuestos: *");
        jlFkTaxGroupId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel7.add(jlFkTaxGroupId);

        jcbFkTaxGroupId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkTaxGroupId.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel7.add(jcbFkTaxGroupId);

        jbFkTaxGroupId.setText("...");
        jbFkTaxGroupId.setToolTipText("Seleccionar grupo de impuestos");
        jbFkTaxGroupId.setFocusable(false);
        jbFkTaxGroupId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel7.add(jbFkTaxGroupId);

        jPanel3.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlDummy01.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel8.add(jlDummy01);

        jckIsDeleted.setText("Registro eliminado");
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel8.add(jckIsDeleted);

        jPanel3.add(jPanel8);

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

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

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-600)/2, (screenSize.height-400)/2, 600, 400);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        moFieldPkTaxRegionId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPkTaxRegionId, jlPkTaxRegionId);
        moFieldPkTaxRegionId.setPickerButton(jbPkTaxRegionId);
        moFieldPkDateStart = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftPkDateStart, jlPkDateStart);
        moFieldPkDateStart.setPickerButton(jbPkDateStart);
        moFieldFkTaxGroupId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkTaxGroupId, jlFkTaxGroupId);
        moFieldFkTaxGroupId.setPickerButton(jbFkTaxGroupId);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsDeleted);

        mvFields = new Vector<SFormField>();
        mvFields.add(moFieldPkTaxRegionId);
        mvFields.add(moFieldPkDateStart);
        mvFields.add(moFieldFkTaxGroupId);
        mvFields.add(moFieldIsDeleted);

        jbPkTaxRegionId.addActionListener(this);
        jbPkDateStart.addActionListener(this);
        jbFkTaxGroupId.addActionListener(this);
        jbOk.addActionListener(this);
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
            if (jcbPkTaxRegionId.isEnabled()) jcbPkTaxRegionId.requestFocus(); else jcbFkTaxGroupId.requestFocus();
        }
    }

    private void actionPkTaxRegionId() {
        miClient.pickOption(SDataConstants.FINU_TAX_REG, moFieldPkTaxRegionId, null);
    }

    private void actionPkDateStart() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldPkDateStart.getDate(), moFieldPkDateStart);
    }

    private void actionFkTaxGroupId() {
        miClient.pickOption(SDataConstants.FIN_TAX_GRP, moFieldFkTaxGroupId, null);
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbFkTaxGroupId;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbPkDateStart;
    private javax.swing.JButton jbPkTaxRegionId;
    private javax.swing.JComboBox jcbFkTaxGroupId;
    private javax.swing.JComboBox jcbPkTaxRegionId;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JFormattedTextField jftPkDateStart;
    private javax.swing.JLabel jlDummy01;
    private javax.swing.JLabel jlFkTaxGroupId;
    private javax.swing.JLabel jlItemGeneric;
    private javax.swing.JLabel jlPkDateStart;
    private javax.swing.JLabel jlPkTaxRegionId;
    private javax.swing.JTextField jtfItemGeneric;
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

        moTaxGroupItemGeneric = null;
        mnParamItemGenericId = 0;
        jtfItemGeneric.setText("");

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        moFieldPkDateStart.setFieldValue(SLibTimeUtilities.getBeginOfYear(miClient.getSessionXXX().getWorkingDate()));

        jcbPkTaxRegionId.setEnabled(true);
        jbPkTaxRegionId.setEnabled(true);
        jftPkDateStart.setEnabled(true);
        jbPkDateStart.setEnabled(true);
        jckIsDeleted.setEnabled(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbPkTaxRegionId, SDataConstants.FINU_TAX_REG);
        SFormUtilities.populateComboBox(miClient, jcbFkTaxGroupId, SDataConstants.FIN_TAX_GRP);
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
        moTaxGroupItemGeneric = (SDataTaxGroupItemGeneric) registry;

        moFieldPkTaxRegionId.setFieldValue(new int[] { moTaxGroupItemGeneric.getPkTaxRegionId() });
        moFieldPkDateStart.setFieldValue(moTaxGroupItemGeneric.getPkDateStartId());
        moFieldFkTaxGroupId.setFieldValue(new int[] { moTaxGroupItemGeneric.getFkTaxGroupId() });
        moFieldIsDeleted.setFieldValue(moTaxGroupItemGeneric.getIsDeleted());

        if (!moTaxGroupItemGeneric.getIsRegistryNew()) {
            jcbPkTaxRegionId.setEnabled(false);
            jbPkTaxRegionId.setEnabled(false);
            jftPkDateStart.setEnabled(false);
            jbPkDateStart.setEnabled(false);
            jckIsDeleted.setEnabled(true);
        }
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (moTaxGroupItemGeneric == null) {
            moTaxGroupItemGeneric = new SDataTaxGroupItemGeneric();
            moTaxGroupItemGeneric.setPkItemGenericId(mnParamItemGenericId);
            moTaxGroupItemGeneric.setPkTaxRegionId(moFieldPkTaxRegionId.getKeyAsIntArray()[0]);
            moTaxGroupItemGeneric.setPkDateStartId(moFieldPkDateStart.getDate());
            moTaxGroupItemGeneric.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moTaxGroupItemGeneric.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moTaxGroupItemGeneric.setFkTaxGroupId(moFieldFkTaxGroupId.getKeyAsIntArray()[0]);
        moTaxGroupItemGeneric.setIsDeleted(moFieldIsDeleted.getBoolean());

        moTaxGroupItemGeneric.setDbmsTaxRegion(((SFormComponentItem) jcbPkTaxRegionId.getSelectedItem()).getItem());
        moTaxGroupItemGeneric.setDbmsTaxGroup(((SFormComponentItem) jcbFkTaxGroupId.getSelectedItem()).getItem());

        return moTaxGroupItemGeneric;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case SDataConstants.ITMU_IGEN:
                mnParamItemGenericId = ((int[]) value)[0];
                jtfItemGeneric.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.ITMU_IGEN, new int[] { mnParamItemGenericId }));
                jtfItemGeneric.setCaretPosition(0);
                break;
            default:
        }
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

            if (button == jbPkTaxRegionId) {
                actionPkTaxRegionId();
            }
            else if (button == jbPkDateStart) {
                actionPkDateStart();
            }
            else if (button == jbFkTaxGroupId) {
                actionFkTaxGroupId();
            }
            else if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
        }
    }
}

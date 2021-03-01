/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.form;

import erp.lib.SLibConstants;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 *
 * @author Juan Barajas
 */
public class SDialogFilterFunctionalArea extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private int mnDataType;
    private int[] manDataFilterKey;
    private int mnFunctionalAreaId;
    private erp.lib.form.SFormField moFieldFunctionalArea;

    /** Creates new form SDialogFilterFunctionalArea
     * @param client GUI client.
     * @param dataType Constants defined in SDataConstants.
     */
    public SDialogFilterFunctionalArea(erp.client.SClientInterface client, int dataType) {
        this(client, dataType, null);
    }

    /** Creates new form SDialogFilterDocumentNature
     * @param client GUI client.
     * @param dataType Constants defined in SDataConstants.
     * @param filter Filter to be used in options combo box.
     */
    public SDialogFilterFunctionalArea(erp.client.SClientInterface client, int dataType, int[] filter) {
        super(client.getFrame(), true);
        miClient =  client;
        mnDataType = dataType;
        manDataFilterKey = filter;

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
        jckSelectedAll = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jlFunctionalArea = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jcbFunctionalArea = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Áreas funcionales");
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
        jbCancel.setToolTipText("[Escape]");
        jPanel1.add(jbCancel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Opciones disponibles:"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jckSelectedAll.setText("Todas las áreas funcionales");
        jckSelectedAll.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jckSelectedAllItemStateChanged(evt);
            }
        });
        jPanel3.add(jckSelectedAll);

        jPanel4.setLayout(new java.awt.BorderLayout(5, 0));

        jlFunctionalArea.setText("Área funcional del documento:");
        jlFunctionalArea.setPreferredSize(new java.awt.Dimension(150, 14));
        jPanel4.add(jlFunctionalArea, java.awt.BorderLayout.LINE_START);

        jPanel5.setLayout(new java.awt.BorderLayout(5, 0));

        jcbFunctionalArea.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel5.add(jcbFunctionalArea, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel4);

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(546, 334));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jckSelectedAllItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jckSelectedAllItemStateChanged
        if (!mbResetingForm) {
            itemStateChangedSelectedAll();
        }
    }//GEN-LAST:event_jckSelectedAllItemStateChanged

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        mvFields = new Vector<>();

        moFieldFunctionalArea = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFunctionalArea, jlFunctionalArea);

        mvFields.add(moFieldFunctionalArea);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);

        SFormUtilities.createActionMap(rootPane, this, "actionOk", "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionCancel", "cancel", KeyEvent.VK_ESCAPE, SLibConstants.UNDEFINED);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;

            if (jcbFunctionalArea.isEnabled()) {
                jcbFunctionalArea.requestFocus();
            }
            else {
                jckSelectedAll.requestFocus();
            }
        }
    }

    private void itemStateChangedSelectedAll() {
        if (jckSelectedAll.isSelected()) {
            jcbFunctionalArea.setEnabled(false);
            moFieldFunctionalArea.resetField();
        }
        else {
            jcbFunctionalArea.setEnabled(true);
        }
    }
    
    public void actionOk() {
        if (!jckSelectedAll.isSelected() && moFieldFunctionalArea.getKeyAsIntArray()[0] == 0) {
            jcbFunctionalArea.requestFocus();
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFunctionalArea.getText() + "'.");
        }
        else {
            mnFunctionalAreaId = jckSelectedAll.isSelected() ? SLibConstants.UNDEFINED : moFieldFunctionalArea.getKeyAsIntArray()[0];

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
    private javax.swing.JComboBox jcbFunctionalArea;
    private javax.swing.JCheckBox jckSelectedAll;
    private javax.swing.JLabel jlFunctionalArea;
    // End of variables declaration//GEN-END:variables

    public void setDataFilterKey(final int[] key) {
        manDataFilterKey = key;
    }

    public void setFunctionalAreaId(final int id) {
        mbResetingForm = true;

        mnFunctionalAreaId = id;
        jckSelectedAll.setSelected(mnFunctionalAreaId == SLibConstants.UNDEFINED);
        moFieldFunctionalArea.setKey(mnFunctionalAreaId == SLibConstants.UNDEFINED ? null : new int[] { mnFunctionalAreaId });
        itemStateChangedSelectedAll();

        mbResetingForm = false;
    }

    public int getFunctionalAreaId() {
        return mnFunctionalAreaId;
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

        setFunctionalAreaId(SLibConstants.UNDEFINED);
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbFunctionalArea, mnDataType, manDataFilterKey);
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

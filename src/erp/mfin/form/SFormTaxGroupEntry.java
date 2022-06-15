/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormTaxGroupEntry.java
 *
 * Created on 21/10/2009, 04:20:33 PM
 */

package erp.mfin.form;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.form.SFormComboBoxGroup;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mfin.data.SDataTaxGroupEntry;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import sa.lib.SLibTimeUtils;

/**
 *
 * @author Alfonso Flores
 */
public class SFormTaxGroupEntry extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mfin.data.SDataTaxGroupEntry moTaxGroupEntry;
    private erp.lib.form.SFormComboBoxGroup moComboBoxGroup;
    private erp.lib.form.SFormField moFieldPkTaxIdentityEmisorTypeId;
    private erp.lib.form.SFormField moFieldPkTaxIdentityReceptorTypeId;
    private erp.lib.form.SFormField moFieldFkTaxBasicId;
    private erp.lib.form.SFormField moFieldFkTaxId;
    private erp.lib.form.SFormField moFieldApplicationOrder;
    private erp.lib.form.SFormField moFieldDateStart;
    private erp.lib.form.SFormField moFieldDateEnd_n;

    /** Creates new form SFormTaxGroupEntry */
    public SFormTaxGroupEntry(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.FIN_TAX_GRP_ETY;

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
        jPanel8 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jlPkTaxIdentityEmisorTypeId = new javax.swing.JLabel();
        jcbPkTaxIdentityEmisorTypeId = new javax.swing.JComboBox();
        jbPkTaxIdentityEmisorTypeId = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jlPkTaxIdentityReceptorTypeId = new javax.swing.JLabel();
        jcbPkTaxIdentityReceptorTypeId = new javax.swing.JComboBox();
        jbPkTaxIdentityReceptorTypeId = new javax.swing.JButton();
        jlDummy01 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jlFkTaxBasicId = new javax.swing.JLabel();
        jcbFkTaxBasicId = new javax.swing.JComboBox();
        jbFkTaxBasicId = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        jlFkTaxId = new javax.swing.JLabel();
        jcbFkTaxId = new javax.swing.JComboBox();
        jbFkTaxId = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jlApplicationOrder = new javax.swing.JLabel();
        jtfApplicationOrder = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        jftDateStart = new javax.swing.JFormattedTextField();
        jbDateStart = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jlDateEnd_n = new javax.swing.JLabel();
        jftDateEnd_n = new javax.swing.JFormattedTextField();
        jbDateEnd_n = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Detalle de grupo de impuestos");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel2.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel8.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel3.setLayout(new java.awt.GridLayout(8, 2, 5, 1));

        jPanel16.setLayout(new java.awt.FlowLayout(0, 2, 0));

        jlPkTaxIdentityEmisorTypeId.setForeground(java.awt.Color.blue);
        jlPkTaxIdentityEmisorTypeId.setText("Tipo de identidad de impuestos del emisor: *");
        jlPkTaxIdentityEmisorTypeId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel16.add(jlPkTaxIdentityEmisorTypeId);

        jcbPkTaxIdentityEmisorTypeId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbPkTaxIdentityEmisorTypeId.setPreferredSize(new java.awt.Dimension(275, 23));
        jPanel16.add(jcbPkTaxIdentityEmisorTypeId);

        jbPkTaxIdentityEmisorTypeId.setText("jButton1");
        jbPkTaxIdentityEmisorTypeId.setToolTipText("Seleccionar tipo de identidad de impuestos");
        jbPkTaxIdentityEmisorTypeId.setFocusable(false);
        jbPkTaxIdentityEmisorTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel16.add(jbPkTaxIdentityEmisorTypeId);

        jPanel3.add(jPanel16);

        jPanel17.setLayout(new java.awt.FlowLayout(0, 2, 0));

        jlPkTaxIdentityReceptorTypeId.setForeground(java.awt.Color.blue);
        jlPkTaxIdentityReceptorTypeId.setText("Tipo de identidad de impuestos del receptor: *");
        jlPkTaxIdentityReceptorTypeId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel17.add(jlPkTaxIdentityReceptorTypeId);

        jcbPkTaxIdentityReceptorTypeId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbPkTaxIdentityReceptorTypeId.setPreferredSize(new java.awt.Dimension(275, 23));
        jPanel17.add(jcbPkTaxIdentityReceptorTypeId);

        jbPkTaxIdentityReceptorTypeId.setText("jButton2");
        jbPkTaxIdentityReceptorTypeId.setToolTipText("Seleccionar tipo de identidad de impuestos");
        jbPkTaxIdentityReceptorTypeId.setFocusable(false);
        jbPkTaxIdentityReceptorTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel17.add(jbPkTaxIdentityReceptorTypeId);

        jPanel3.add(jPanel17);
        jPanel3.add(jlDummy01);

        jPanel21.setLayout(new java.awt.FlowLayout(0, 2, 0));

        jlFkTaxBasicId.setText("Impuesto básico: *");
        jlFkTaxBasicId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel21.add(jlFkTaxBasicId);

        jcbFkTaxBasicId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkTaxBasicId.setPreferredSize(new java.awt.Dimension(375, 23));
        jPanel21.add(jcbFkTaxBasicId);

        jbFkTaxBasicId.setText("jButton3");
        jbFkTaxBasicId.setToolTipText("Seleccionar impuesto básico");
        jbFkTaxBasicId.setFocusable(false);
        jbFkTaxBasicId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel21.add(jbFkTaxBasicId);

        jPanel3.add(jPanel21);

        jPanel22.setLayout(new java.awt.FlowLayout(0, 2, 0));

        jlFkTaxId.setText("Impuesto: *");
        jlFkTaxId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel22.add(jlFkTaxId);

        jcbFkTaxId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkTaxId.setPreferredSize(new java.awt.Dimension(375, 23));
        jPanel22.add(jcbFkTaxId);

        jbFkTaxId.setText("jButton4");
        jbFkTaxId.setToolTipText("Seleccionar impuesto");
        jbFkTaxId.setFocusable(false);
        jbFkTaxId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel22.add(jbFkTaxId);

        jPanel3.add(jPanel22);

        jPanel18.setLayout(new java.awt.FlowLayout(0, 2, 0));

        jlApplicationOrder.setText("Orden de aplicación: *");
        jlApplicationOrder.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel18.add(jlApplicationOrder);

        jtfApplicationOrder.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfApplicationOrder.setText("0");
        jtfApplicationOrder.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel18.add(jtfApplicationOrder);

        jPanel3.add(jPanel18);

        jPanel19.setLayout(new java.awt.FlowLayout(0, 2, 0));

        jlDateStart.setText("Fecha inicial vigencia: *");
        jlDateStart.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel19.add(jlDateStart);

        jftDateStart.setText("dd/mm/yyyy");
        jftDateStart.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel19.add(jftDateStart);

        jbDateStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_date_day.gif"))); // NOI18N
        jbDateStart.setToolTipText("Seleccionar fecha");
        jbDateStart.setFocusable(false);
        jbDateStart.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel19.add(jbDateStart);

        jPanel3.add(jPanel19);

        jPanel20.setLayout(new java.awt.FlowLayout(0, 2, 0));

        jlDateEnd_n.setText("Fecha final vigencia: ");
        jlDateEnd_n.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel20.add(jlDateEnd_n);

        jftDateEnd_n.setText("dd/mm/yyyy");
        jftDateEnd_n.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel20.add(jftDateEnd_n);

        jbDateEnd_n.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_date_day.gif"))); // NOI18N
        jbDateEnd_n.setToolTipText("Seleccionar fecha");
        jbDateEnd_n.setFocusable(false);
        jbDateEnd_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel20.add(jbDateEnd_n);

        jPanel3.add(jPanel20);

        jPanel8.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel2.add(jPanel8, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setPreferredSize(new java.awt.Dimension(392, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(2));

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
        moComboBoxGroup = new SFormComboBoxGroup(miClient);

        moFieldPkTaxIdentityEmisorTypeId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPkTaxIdentityEmisorTypeId, jlPkTaxIdentityEmisorTypeId);
        moFieldPkTaxIdentityEmisorTypeId.setPickerButton(jbPkTaxIdentityEmisorTypeId);
        moFieldPkTaxIdentityReceptorTypeId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPkTaxIdentityReceptorTypeId, jlPkTaxIdentityReceptorTypeId);
        moFieldPkTaxIdentityReceptorTypeId.setPickerButton(jbPkTaxIdentityReceptorTypeId);
        moFieldFkTaxBasicId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkTaxBasicId, jlFkTaxBasicId);
        moFieldFkTaxBasicId.setPickerButton(jbFkTaxBasicId);
        moFieldFkTaxId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkTaxId, jlFkTaxId);
        moFieldFkTaxId.setPickerButton(jbFkTaxId);
        moFieldApplicationOrder = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfApplicationOrder, jlApplicationOrder);
        moFieldDateStart = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateStart, jlDateStart);
        moFieldDateStart.setPickerButton(jbDateStart);
        moFieldDateEnd_n = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftDateEnd_n, jlDateEnd_n);
        moFieldDateEnd_n.setPickerButton(jbDateEnd_n);

        mvFields = new Vector<SFormField>();
        mvFields.add(moFieldPkTaxIdentityEmisorTypeId);
        mvFields.add(moFieldPkTaxIdentityReceptorTypeId);
        mvFields.add(moFieldFkTaxBasicId);
        mvFields.add(moFieldFkTaxId);
        mvFields.add(moFieldApplicationOrder);
        mvFields.add(moFieldDateStart);
        mvFields.add(moFieldDateEnd_n);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbPkTaxIdentityEmisorTypeId.addActionListener(this);
        jbPkTaxIdentityReceptorTypeId.addActionListener(this);
        jbFkTaxBasicId.addActionListener(this);
        jbFkTaxId.addActionListener(this);
        jbDateStart.addActionListener(this);
        jbDateEnd_n.addActionListener(this);

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
            if (jcbPkTaxIdentityEmisorTypeId.isEnabled()) jcbPkTaxIdentityEmisorTypeId.requestFocus(); else jcbFkTaxBasicId.requestFocus();
        }
    }

    private void actionFkTaxIdentityEmisorId() {
        miClient.pickOption(SDataConstants.FINU_TAX_IDY, moFieldPkTaxIdentityEmisorTypeId, null);
    }

    private void actionFkTaxIdentityReceptorId() {
        miClient.pickOption(SDataConstants.FINU_TAX_IDY, moFieldPkTaxIdentityReceptorTypeId, null);
    }

    private void actionFkTaxBasicId() {
        miClient.pickOption(SDataConstants.FINU_TAX_BAS, moFieldFkTaxBasicId, null);
    }

    private void actionFkTaxId() {
        miClient.pickOption(SDataConstants.FINU_TAX, moFieldFkTaxId, moFieldFkTaxBasicId.getKeyAsIntArray());
    }

    private void actionDateStart() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateStart.getDate(), moFieldDateStart);
    }

    private void actionDateEnd_n() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateEnd_n.getDate(), moFieldDateEnd_n);
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
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDateEnd_n;
    private javax.swing.JButton jbDateStart;
    private javax.swing.JButton jbFkTaxBasicId;
    private javax.swing.JButton jbFkTaxId;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbPkTaxIdentityEmisorTypeId;
    private javax.swing.JButton jbPkTaxIdentityReceptorTypeId;
    private javax.swing.JComboBox jcbFkTaxBasicId;
    private javax.swing.JComboBox jcbFkTaxId;
    private javax.swing.JComboBox jcbPkTaxIdentityEmisorTypeId;
    private javax.swing.JComboBox jcbPkTaxIdentityReceptorTypeId;
    private javax.swing.JFormattedTextField jftDateEnd_n;
    private javax.swing.JFormattedTextField jftDateStart;
    private javax.swing.JLabel jlApplicationOrder;
    private javax.swing.JLabel jlDateEnd_n;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlDummy01;
    private javax.swing.JLabel jlFkTaxBasicId;
    private javax.swing.JLabel jlFkTaxId;
    private javax.swing.JLabel jlPkTaxIdentityEmisorTypeId;
    private javax.swing.JLabel jlPkTaxIdentityReceptorTypeId;
    private javax.swing.JTextField jtfApplicationOrder;
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

        moTaxGroupEntry = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        moFieldDateStart.setFieldValue(SLibTimeUtils.getBeginOfYear(miClient.getSessionXXX().getWorkingDate()));
        jcbPkTaxIdentityEmisorTypeId.setEnabled(true);
        jcbPkTaxIdentityReceptorTypeId.setEnabled(true);
        jbPkTaxIdentityEmisorTypeId.setEnabled(true);
        jbPkTaxIdentityReceptorTypeId.setEnabled(true);

        moComboBoxGroup.reset();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void formRefreshCatalogues() {
        moComboBoxGroup.clear();
        moComboBoxGroup.addComboBox(SDataConstants.FINU_TAX_BAS, jcbFkTaxBasicId, jbFkTaxBasicId);
        moComboBoxGroup.addComboBox(SDataConstants.FINU_TAX, jcbFkTaxId, jbFkTaxId);

        SFormUtilities.populateComboBox(miClient, jcbPkTaxIdentityEmisorTypeId, SDataConstants.FINU_TAX_IDY);
        SFormUtilities.populateComboBox(miClient, jcbPkTaxIdentityReceptorTypeId, SDataConstants.FINU_TAX_IDY);
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
            if (moFieldDateEnd_n.getDate() != null && moFieldDateEnd_n.getDate().before(moFieldDateStart.getDate())) {
                validation.setMessage("La fecha del campo '" + jlDateEnd_n.getText() + "' no puede ser anterior a la fecha del campo '" + jlDateStart.getText() + "'.");
                validation.setComponent(jftDateEnd_n);
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
        moTaxGroupEntry = (SDataTaxGroupEntry) registry;

        moFieldPkTaxIdentityEmisorTypeId.setKey(new int[] { moTaxGroupEntry.getPkTaxIdentityEmisorTypeId() });
        moFieldPkTaxIdentityReceptorTypeId.setKey(new int[] { moTaxGroupEntry.getPkTaxIdentityReceptorTypeId() });
        moFieldFkTaxBasicId.setKey(new int[] { moTaxGroupEntry.getFkTaxBasicId() });
        moFieldFkTaxId.setKey(new int[] { moTaxGroupEntry.getFkTaxBasicId(), moTaxGroupEntry.getFkTaxId() });
        moFieldApplicationOrder.setFieldValue(moTaxGroupEntry.getApplicationOrder());
        moFieldDateStart.setFieldValue(moTaxGroupEntry.getDateStart());
        moFieldDateEnd_n.setFieldValue(moTaxGroupEntry.getDateEnd_n());

        if (!moTaxGroupEntry.getIsRegistryNew()) {
            jcbPkTaxIdentityEmisorTypeId.setEnabled(false);
            jcbPkTaxIdentityReceptorTypeId.setEnabled(false);
            jbPkTaxIdentityEmisorTypeId.setEnabled(false);
            jbPkTaxIdentityReceptorTypeId.setEnabled(false);
        }
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (moTaxGroupEntry == null) {
            moTaxGroupEntry = new SDataTaxGroupEntry();
        }

        moTaxGroupEntry.setPkTaxIdentityEmisorTypeId(moFieldPkTaxIdentityEmisorTypeId.getKeyAsIntArray()[0]);
        moTaxGroupEntry.setPkTaxIdentityReceptorTypeId(moFieldPkTaxIdentityReceptorTypeId.getKeyAsIntArray()[0]);
        moTaxGroupEntry.setFkTaxBasicId(moFieldFkTaxId.getKeyAsIntArray()[0]);
        moTaxGroupEntry.setFkTaxId(moFieldFkTaxId.getKeyAsIntArray()[1]);
        moTaxGroupEntry.setApplicationOrder(moFieldApplicationOrder.getInteger());
        moTaxGroupEntry.setDateStart(moFieldDateStart.getDate());
        moTaxGroupEntry.setDateEnd_n(moFieldDateEnd_n.getDate());

        moTaxGroupEntry.setDbmsTaxIdentityEmisorType(jcbPkTaxIdentityEmisorTypeId.getSelectedItem().toString());
        moTaxGroupEntry.setDbmsTaxIdentityReceptorType(jcbPkTaxIdentityReceptorTypeId.getSelectedItem().toString());
        moTaxGroupEntry.setDbmsTax(jcbFkTaxId.getSelectedItem().toString());

        return moTaxGroupEntry;
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
            else if (button == jbPkTaxIdentityEmisorTypeId) {
                actionFkTaxIdentityEmisorId();
            }
            else if (button == jbPkTaxIdentityReceptorTypeId) {
                actionFkTaxIdentityReceptorId();
            }
            else if (button == jbFkTaxBasicId) {
                actionFkTaxBasicId();
            }
            else if (button == jbFkTaxId) {
                actionFkTaxId();
            }
            else if (button == jbDateStart) {
                actionDateStart();
            }
            else if (button == jbDateEnd_n) {
                actionDateEnd_n();
            }
        }
    }
}

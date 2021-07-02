/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormStockLot.java
 *
 * Created on 23/10/2009, 08:48:14 AM
 */

package erp.mtrn.form;

import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mitm.data.SDataItem;
import erp.mtrn.data.SDataStockLot;
import erp.mtrn.data.STrnUtilities;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.JComboBox;

/**
 *
 * @author Sergio Flores
 */
public class SFormStockLot extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.ItemListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mtrn.data.SDataStockLot moStockLot;
    private erp.lib.form.SFormField moFieldPkItemId;
    private erp.lib.form.SFormField moFieldPkUnitId;
    private erp.lib.form.SFormField moFieldLot;
    private erp.lib.form.SFormField moFieldDateExpiration_n;

    /** Creates new form SFormStockLot */
    public SFormStockLot(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.TRN_LOT;

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
        jPanel21 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jlPkItemId = new javax.swing.JLabel();
        jcbPkItemId = new javax.swing.JComboBox();
        jbPkItemId = new javax.swing.JButton();
        jtfItemKey = new javax.swing.JTextField();
        jPanel23 = new javax.swing.JPanel();
        jlPkUnitId = new javax.swing.JLabel();
        jcbPkUnitId = new javax.swing.JComboBox();
        jbPkUnitId = new javax.swing.JButton();
        jtfUnitSymbol = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        jlLot = new javax.swing.JLabel();
        jtfLot = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jlDateExpiration_n = new javax.swing.JLabel();
        jftDateExpiration_n = new javax.swing.JFormattedTextField();
        jbDateExpiration_n = new javax.swing.JButton();
        jckIsBlocked = new javax.swing.JCheckBox();
        jckIsDeleted = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jpCommands1 = new javax.swing.JPanel();
        jpCommands2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lote");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel2.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel21.setLayout(new java.awt.BorderLayout(5, 0));

        jPanel8.setLayout(new java.awt.GridLayout(6, 1, 0, 5));

        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPkItemId.setForeground(java.awt.Color.blue);
        jlPkItemId.setText("Ítem: *");
        jlPkItemId.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel20.add(jlPkItemId);

        jcbPkItemId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbPkItemId.setPreferredSize(new java.awt.Dimension(320, 23));
        jPanel20.add(jcbPkItemId);

        jbPkItemId.setText("...");
        jbPkItemId.setToolTipText("Seleccionar ítem");
        jbPkItemId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel20.add(jbPkItemId);

        jtfItemKey.setEditable(false);
        jtfItemKey.setText("CODE");
        jtfItemKey.setFocusable(false);
        jtfItemKey.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel20.add(jtfItemKey);

        jPanel8.add(jPanel20);

        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPkUnitId.setForeground(java.awt.Color.blue);
        jlPkUnitId.setText("Unidad: *");
        jlPkUnitId.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel23.add(jlPkUnitId);

        jcbPkUnitId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbPkUnitId.setPreferredSize(new java.awt.Dimension(320, 23));
        jPanel23.add(jcbPkUnitId);

        jbPkUnitId.setText("...");
        jbPkUnitId.setToolTipText("Seleccionar unidad");
        jbPkUnitId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel23.add(jbPkUnitId);

        jtfUnitSymbol.setEditable(false);
        jtfUnitSymbol.setText("CODE");
        jtfUnitSymbol.setFocusable(false);
        jtfUnitSymbol.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel23.add(jtfUnitSymbol);

        jPanel8.add(jPanel23);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLot.setText("Lote: *");
        jlLot.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel28.add(jlLot);

        jtfLot.setText("TEXT");
        jtfLot.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel28.add(jtfLot);

        jPanel8.add(jPanel28);

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateExpiration_n.setText("Caducidad: *");
        jlDateExpiration_n.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel19.add(jlDateExpiration_n);

        jftDateExpiration_n.setText("dd/mm/yyyy");
        jftDateExpiration_n.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        jftDateExpiration_n.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel19.add(jftDateExpiration_n);

        jbDateExpiration_n.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_date_day.gif"))); // NOI18N
        jbDateExpiration_n.setToolTipText("Seleccionar fecha");
        jbDateExpiration_n.setFocusable(false);
        jbDateExpiration_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel19.add(jbDateExpiration_n);

        jPanel8.add(jPanel19);

        jckIsBlocked.setText("Lote bloqueado");
        jPanel8.add(jckIsBlocked);

        jckIsDeleted.setText("Registro eliminado");
        jPanel8.add(jckIsDeleted);

        jPanel21.add(jPanel8, java.awt.BorderLayout.NORTH);

        jPanel2.add(jPanel21, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setPreferredSize(new java.awt.Dimension(592, 33));
        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        jpCommands1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jPanel1.add(jpCommands1);

        jpCommands2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpCommands2.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jpCommands2.add(jbCancel);

        jPanel1.add(jpCommands2);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(568, 384));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
       windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {

        moFieldPkItemId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPkItemId, jlPkItemId);
        moFieldPkItemId.setPickerButton(jbPkItemId);
        moFieldPkUnitId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPkUnitId, jlPkUnitId);
        moFieldPkUnitId.setPickerButton(jbPkUnitId);
        moFieldLot = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfLot, jlLot);
        moFieldLot.setLengthMax(25);
        moFieldDateExpiration_n = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftDateExpiration_n, jlDateExpiration_n);
        moFieldDateExpiration_n.setPickerButton(jbDateExpiration_n);

        mvFields = new Vector<SFormField>();
        mvFields.add(moFieldPkItemId);
        mvFields.add(moFieldPkUnitId);
        mvFields.add(moFieldLot);
        mvFields.add(moFieldDateExpiration_n);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbPkItemId.addActionListener(this);
        jbPkUnitId.addActionListener(this);
        jbDateExpiration_n.addActionListener(this);
        jcbPkItemId.addItemListener(this);
        jcbPkUnitId.addItemListener(this);

        SFormUtilities.createActionMap(rootPane, this, "actionOk", "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionCancel", "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;

            if (jcbPkItemId.isEnabled()) {
                jcbPkItemId.requestFocus();
            }
            else if (jtfLot.isEnabled()) {
                jtfLot.requestFocus();
            }
            else {
                jbCancel.requestFocus();
            }
        }
    }

    public void itemStateFieldPkItemId() {
        SDataItem item = null;

        if (jcbPkItemId.getSelectedIndex() <= 0) {
            jtfItemKey.setText("");

            jcbPkUnitId.setEnabled(false);
            jbPkUnitId.setEnabled(false);
            moFieldPkUnitId.setFieldValue(null);
        }
        else {
            item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, moFieldPkItemId.getKeyAsIntArray(), SLibConstants.EXEC_MODE_VERBOSE);

            jtfItemKey.setText(item.getKey());

            jcbPkUnitId.setEnabled(true);
            jbPkUnitId.setEnabled(true);
            moFieldPkUnitId.setFieldValue(new int[] { item.getFkUnitId() });
        }

        itemStateFieldPkUnitId();
    }

    public void itemStateFieldPkUnitId() {
        if (jcbPkUnitId.getSelectedIndex() <= 0) {
            jtfUnitSymbol.setText("");
        }
        else {
            jtfUnitSymbol.setText(((SFormComponentItem) jcbPkUnitId.getSelectedItem()).getComplement().toString());
        }
    }

    public void actionOk() {
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

    public void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDateExpiration_n;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbPkItemId;
    private javax.swing.JButton jbPkUnitId;
    private javax.swing.JComboBox jcbPkItemId;
    private javax.swing.JComboBox jcbPkUnitId;
    private javax.swing.JCheckBox jckIsBlocked;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JFormattedTextField jftDateExpiration_n;
    private javax.swing.JLabel jlDateExpiration_n;
    private javax.swing.JLabel jlLot;
    private javax.swing.JLabel jlPkItemId;
    private javax.swing.JLabel jlPkUnitId;
    private javax.swing.JPanel jpCommands1;
    private javax.swing.JPanel jpCommands2;
    private javax.swing.JTextField jtfItemKey;
    private javax.swing.JTextField jtfLot;
    private javax.swing.JTextField jtfUnitSymbol;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        moStockLot = null;

        jcbPkItemId.setEnabled(true);
        jcbPkUnitId.setEnabled(true);
        jbPkItemId.setEnabled(true);
        jbPkUnitId.setEnabled(true);
    }

    @Override
    public void formReset() {
        mbResetingForm = true;

        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        moStockLot = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        jtfLot.setEnabled(true);
        jftDateExpiration_n.setEnabled(true);
        jbDateExpiration_n.setEnabled(true);
        jckIsDeleted.setEnabled(false);

        jcbPkItemId.setEnabled(true);
        jcbPkUnitId.setEnabled(false);
        jbPkItemId.setEnabled(true);
        jbPkUnitId.setEnabled(false);

        mbResetingForm = false;
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbPkItemId, SDataConstants.ITMU_ITEM);
        SFormUtilities.populateComboBox(miClient, jcbPkUnitId, SDataConstants.ITMU_UNIT);
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        String message = "";
        SFormValidation validation = new SFormValidation();

        try {
            for (int i = 0; i < mvFields.size(); i++) {
                if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                    validation.setIsError(true);
                    validation.setComponent(((erp.lib.form.SFormField) mvFields.get(i)).getComponent());
                    break;
                }
            }

            if (!validation.getIsError()) {
                message = STrnUtilities.validateLot(miClient,
                        moStockLot != null && !moStockLot.getIsRegistryNew() ? (int[]) moStockLot.getPrimaryKey() :
                        new int[] { moFieldPkItemId.getKeyAsIntArray()[0], moFieldPkUnitId.getKeyAsIntArray()[0], 0 },
                        moFieldLot.getString());
                if (message.length() > 0) {
                    validation.setMessage(message);
                    validation.setComponent(jtfLot);
                }
            }

            if (!validation.getIsError()) {
                if (jckIsDeleted.isSelected() && STrnUtilities.hasStock(miClient, miClient.getSession().getSystemYear(), moStockLot.getPkItemId(), moStockLot.getPkLotId(), SLibTimeUtilities.getEndOfYear(miClient.getSession().getSystemDate()))) {
                    validation.setMessage("El lote '" + jtfLot.getText() + "' no puede ser eliminado\n debido a que tiene existencias en los inventarios.");
                    validation.setComponent(jckIsDeleted);
                }
                if (moFieldDateExpiration_n.getDate() == null) {
                    validation.setMessage("La fecha para el campo '" + jlDateExpiration_n.getText() + "' se debe expresar en el formato dd/mm/yyy.");
                    validation.setComponent(jbDateExpiration_n);
                }
            }
        }
        catch (Exception e) {
            validation.setMessage(e.toString());
            SLibUtilities.printOutException(this, e);
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
        boolean isEditable = false;

        moStockLot = (SDataStockLot) registry;

        moFieldPkItemId.setFieldValue(new int[] { moStockLot.getPkItemId() });
        moFieldPkUnitId.setFieldValue(new int[] { moStockLot.getPkUnitId() });
        moFieldLot.setFieldValue(moStockLot.getLot());
        moFieldDateExpiration_n.setFieldValue(moStockLot.getDateExpiration_n());
        jckIsBlocked.setSelected(moStockLot.getIsBlocked());
        jckIsDeleted.setSelected(moStockLot.getIsDeleted());

        isEditable = moStockLot.getLot().length() > 0;

        jtfLot.setEnabled(isEditable);
        jftDateExpiration_n.setEnabled(isEditable);
        jbDateExpiration_n.setEnabled(isEditable);
        jckIsDeleted.setEnabled(isEditable);

        jcbPkItemId.setEnabled(false);
        jcbPkUnitId.setEnabled(false);
        jbPkItemId.setEnabled(false);
        jbPkUnitId.setEnabled(false);
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (moStockLot == null) {
            moStockLot = new SDataStockLot();
            moStockLot.setPkItemId(moFieldPkItemId.getKeyAsIntArray()[0]);
            moStockLot.setPkUnitId(moFieldPkUnitId.getKeyAsIntArray()[0]);
            moStockLot.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moStockLot.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moStockLot.setLot(moFieldLot.getString());
        moStockLot.setDateExpiration_n(moFieldDateExpiration_n.getDate());
        moStockLot.setIsBlocked(jckIsBlocked.isSelected());
        moStockLot.setIsDeleted(jckIsDeleted.isSelected());

        return moStockLot;
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
            else if (button == jbDateExpiration_n) {
                miClient.getGuiDatePickerXXX().pickDate(moFieldDateExpiration_n.getDate(), moFieldDateExpiration_n);
            }
            else if (button == jbPkItemId) {
                miClient.pickOption(SDataConstants.ITMX_ITEM_IOG, moFieldPkItemId, null);
            }
            else if (button == jbPkUnitId) {
                miClient.pickOption(SDataConstants.ITMU_UNIT, moFieldPkUnitId, null);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (!mbResetingForm) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JComboBox comboBox = (JComboBox) e.getSource();

                if (comboBox == jcbPkItemId) {
                    itemStateFieldPkItemId();
                }
                else if (comboBox == jcbPkUnitId) {
                    itemStateFieldPkUnitId();
                }
            }
        }
    }
}

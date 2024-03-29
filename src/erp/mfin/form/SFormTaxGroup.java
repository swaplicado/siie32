/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormTaxGroup.java
 *
 * Created on 21/10/2009, 01:40:11 PM
 */

package erp.mfin.form;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.mfin.data.SDataTaxGroup;
import erp.mfin.data.SDataTaxGroupEntry;
import erp.mfin.data.SDataTaxGroupEntryRow;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 *
 * @author Alfonso Flores
 */
public class SFormTaxGroup extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mfin.data.SDataTaxGroup moTaxGroup;
    private erp.lib.form.SFormField moFieldTaxGroup;
    private erp.lib.form.SFormField moFieldIsDeleted;

    private erp.lib.table.STablePane moEntriesPane;
    private erp.mfin.form.SFormTaxGroupEntry moFormTaxGroupEntry;

    /** Creates new form SFormTaxGroup */
    public SFormTaxGroup(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.FIN_TAX_GRP;

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
        jPanel6 = new javax.swing.JPanel();
        jlTaxGroup = new javax.swing.JLabel();
        jtfTaxGroup = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jlDummy = new javax.swing.JLabel();
        jckIsDeleted = new javax.swing.JCheckBox();
        jpEntries = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jbEntryCreate = new javax.swing.JButton();
        jbEntryModify = new javax.swing.JButton();
        jbEntryDelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Grupo de impuestos");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

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

        jPanel2.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel3.setLayout(new java.awt.GridLayout(2, 1, 5, 1));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTaxGroup.setText("Grupo de impuestos: *");
        jlTaxGroup.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel6.add(jlTaxGroup);

        jtfTaxGroup.setText("GPO IMP");
        jtfTaxGroup.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel6.add(jtfTaxGroup);

        jPanel3.add(jPanel6);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDummy.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel5.add(jlDummy);

        jckIsDeleted.setText("Registo eliminado");
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel5.add(jckIsDeleted);

        jPanel3.add(jPanel5);

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jpEntries.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalles del grupo de impuestos:"));
        jpEntries.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(476, 23));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 0));

        jbEntryCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbEntryCreate.setToolTipText("Crear detalle");
        jbEntryCreate.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbEntryCreate);

        jbEntryModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEntryModify.setToolTipText("Modificar detalle");
        jbEntryModify.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbEntryModify);

        jbEntryDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbEntryDelete.setToolTipText("Eliminar detalle");
        jbEntryDelete.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbEntryDelete);

        jpEntries.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel2.add(jpEntries, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-800)/2, (screenSize.height-600)/2, 800, 600);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        int i = 0;
        erp.lib.table.STableColumnForm tableColumnsEntries[];

        moEntriesPane = new STablePane(miClient);
        moEntriesPane.setDoubleClickAction(this, "publicActionModifyTaxGroupEntry");
        jpEntries.add(moEntriesPane, BorderLayout.CENTER);

        moFieldTaxGroup = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfTaxGroup, jlTaxGroup);
        moFieldTaxGroup.setLengthMax(50);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsDeleted);

        mvFields = new Vector<SFormField>();
        mvFields.add(moFieldTaxGroup);
        mvFields.add(moFieldIsDeleted);

        moFormTaxGroupEntry = new SFormTaxGroupEntry(miClient);

        i = 0;
        tableColumnsEntries = new STableColumnForm[6];
        tableColumnsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo ident. imp. emisor", 150);
        tableColumnsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo ident. imp. receptor", 150);
        tableColumnsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE, "Ini. vigencia", STableConstants.WIDTH_DATE);
        tableColumnsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE, "Fin. vigencia", STableConstants.WIDTH_DATE);
        tableColumnsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Impuesto", 150);
        tableColumnsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "Orden aplicación", STableConstants.WIDTH_NUM_INTEGER);

        for (i = 0; i < tableColumnsEntries.length; i++) {
            moEntriesPane.addTableColumn(tableColumnsEntries[i]);
        }

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbEntryCreate.addActionListener(this);
        jbEntryModify.addActionListener(this);
        jbEntryDelete.addActionListener(this);

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
            jtfTaxGroup.requestFocus();
        }
    }

    private void actionEntryCreate() {
        moFormTaxGroupEntry.formReset();
        moFormTaxGroupEntry.setVisible(true);

        if (moFormTaxGroupEntry.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
            moEntriesPane.addTableRow(new SDataTaxGroupEntryRow(moFormTaxGroupEntry.getRegistry()));
            moEntriesPane.renderTableRows();
            moEntriesPane.setTableRowSelection(moEntriesPane.getTableGuiRowCount() - 1);
        }
    }

    private void actionEntryModify() {
        int index = moEntriesPane.getTable().getSelectedRow();

        if (index != -1) {
            moFormTaxGroupEntry.formReset();
            moFormTaxGroupEntry.setRegistry((SDataTaxGroupEntry) moEntriesPane.getTableRow(index).getData());
            moFormTaxGroupEntry.setVisible(true);

            if (moFormTaxGroupEntry.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                moEntriesPane.getTableModel().getTableRows().set(index, new SDataTaxGroupEntryRow(moFormTaxGroupEntry.getRegistry()));
                moEntriesPane.renderTableRows();
                moEntriesPane.setTableRowSelection(index);
            }
        }
    }

    private void actionEntryDelete() {
        int index = moEntriesPane.getTable().getSelectedRow();

        if (index != -1 && miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
            moEntriesPane.removeTableRow(index);
            moEntriesPane.renderTableRows();
            moEntriesPane.setTableRowSelection(index < moEntriesPane.getTableGuiRowCount() ? index : index - 1);
        }
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

    public void publicActionModifyTaxGroupEntry() {
        actionEntryModify();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbEntryCreate;
    private javax.swing.JButton jbEntryDelete;
    private javax.swing.JButton jbEntryModify;
    private javax.swing.JButton jbOk;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JLabel jlDummy;
    private javax.swing.JLabel jlTaxGroup;
    private javax.swing.JPanel jpEntries;
    private javax.swing.JTextField jtfTaxGroup;
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

        moTaxGroup = null;
        moEntriesPane.createTable();
        moEntriesPane.clearTableRows();

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        jckIsDeleted.setEnabled(false);
    }

    @Override
    public void formRefreshCatalogues() {
        moFormTaxGroupEntry.formRefreshCatalogues();
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
        int i = 0;
        moTaxGroup = (SDataTaxGroup) registry;
        SDataTaxGroupEntryRow entryRow = null;

        moFieldTaxGroup.setFieldValue(moTaxGroup.getTaxGroup());
        moFieldIsDeleted.setFieldValue(moTaxGroup.getIsDeleted());

        for (i = 0; i < moTaxGroup.getDbmsTaxGroupEntries().size(); i++) {
            entryRow = new SDataTaxGroupEntryRow(moTaxGroup.getDbmsTaxGroupEntries().get(i));
            moEntriesPane.addTableRow(entryRow);
        }

        jckIsDeleted.setEnabled(true);
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        SDataTaxGroupEntry taxGroupEntry = null;

        if (moTaxGroup == null) {
            moTaxGroup = new SDataTaxGroup();
            moTaxGroup.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moTaxGroup.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moTaxGroup.setTaxGroup(moFieldTaxGroup.getString());
        moTaxGroup.setIsDeleted(moFieldIsDeleted.getBoolean());

        moTaxGroup.getDbmsTaxGroupEntries().clear();
        for (int i = 0; i < moEntriesPane.getTableGuiRowCount(); i++) {
            taxGroupEntry = new SDataTaxGroupEntry();
            taxGroupEntry = (erp.mfin.data.SDataTaxGroupEntry) moEntriesPane.getTableRow(i).getData();
            moTaxGroup.getDbmsTaxGroupEntries().add(taxGroupEntry);
        }

        return moTaxGroup;
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
            else if (button == jbEntryCreate) {
                actionEntryCreate();
            }
            else if (button == jbEntryModify) {
                actionEntryModify();
            }
            else if (button == jbEntryDelete) {
                actionEntryDelete();
            }
        }
    }
}

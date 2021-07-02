/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormBizPartnerDescription.java
 *
 * Created on 24/06/2010, 08:35:46 AM
 */

package erp.mitm.form;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.mitm.data.SDataBizPartnerItemDescription;
import erp.mitm.data.SDataItemBizPartnerDescription;
import erp.mitm.data.SDataItemBizPartnerDescriptionRow;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;

/**
 *
 * @author Alfonso Flores
 */
public class SFormBizPartnerDescription extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mitm.data.SDataBizPartnerItemDescription moBizPartnerItemDescription;
    private erp.lib.form.SFormField moFieldPkBizPartnerId;

    private erp.lib.table.STablePane moItemBizPartnerDescriptionPane;
    private erp.mitm.form.SFormItemDescription moFormItemDescription;

    /** Creates new form SFormBizPartnerDescription */
    public SFormBizPartnerDescription(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.ITMU_CFG_ITEM_BP;

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
        jlPkBizPartnerId = new javax.swing.JLabel();
        jcbPkBizPartnerId = new javax.swing.JComboBox();
        jbPkBizPartnerId = new javax.swing.JButton();
        jpItemDescriptions = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jbAddItemDescription = new javax.swing.JButton();
        jbModifyItemDescription = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Descripción de ítems para asoc. de negocios");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(592, 33));
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
        jPanel2.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPkBizPartnerId.setForeground(java.awt.Color.blue);
        jlPkBizPartnerId.setText("Asociado de negocios: *");
        jlPkBizPartnerId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel3.add(jlPkBizPartnerId);

        jcbPkBizPartnerId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbPkBizPartnerId.setPreferredSize(new java.awt.Dimension(388, 23));
        jPanel3.add(jcbPkBizPartnerId);

        jbPkBizPartnerId.setText("jButton1");
        jbPkBizPartnerId.setToolTipText("Seleccionar asociado de negocios");
        jbPkBizPartnerId.setFocusable(false);
        jbPkBizPartnerId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel3.add(jbPkBizPartnerId);

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        jpItemDescriptions.setBorder(javax.swing.BorderFactory.createTitledBorder("Descripciones:"));
        jpItemDescriptions.setLayout(new java.awt.BorderLayout());

        jPanel5.setPreferredSize(new java.awt.Dimension(560, 23));
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 0));

        jbAddItemDescription.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbAddItemDescription.setToolTipText("Crear [Ctrl+N]");
        jbAddItemDescription.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbAddItemDescription);

        jbModifyItemDescription.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbModifyItemDescription.setToolTipText("Modificar [Ctrl+M]");
        jbModifyItemDescription.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbModifyItemDescription);

        jpItemDescriptions.add(jPanel5, java.awt.BorderLayout.NORTH);

        jPanel2.add(jpItemDescriptions, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-600)/2, (screenSize.height-400)/2, 600, 400);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        int i;
        mvFields = new Vector<SFormField>();

        erp.lib.table.STableColumnForm tableColumnsItemBizPartnerDescription[];

        moItemBizPartnerDescriptionPane = new STablePane(miClient);
        moItemBizPartnerDescriptionPane.setDoubleClickAction(this, "publicActionModifyItemDescription");
        jpItemDescriptions.add(moItemBizPartnerDescriptionPane, BorderLayout.CENTER);

        moFieldPkBizPartnerId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPkBizPartnerId, jlPkBizPartnerId);
        moFieldPkBizPartnerId.setPickerButton(jbPkBizPartnerId);

        mvFields.add(moFieldPkBizPartnerId);

        moFormItemDescription = new SFormItemDescription(miClient);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbPkBizPartnerId.addActionListener(this);
        jbAddItemDescription.addActionListener(this);
        jbModifyItemDescription.addActionListener(this);

        i = 0;
        tableColumnsItemBizPartnerDescription = new STableColumnForm[6];
        tableColumnsItemBizPartnerDescription[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Clave", STableConstants.WIDTH_ITEM_KEY);
        tableColumnsItemBizPartnerDescription[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Ítem", 200);
        tableColumnsItemBizPartnerDescription[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Ítem corto", 100);
        tableColumnsItemBizPartnerDescription[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        tableColumnsItemBizPartnerDescription[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Uso CFDI", 100);
        tableColumnsItemBizPartnerDescription[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Eliminado", STableConstants.WIDTH_BOOLEAN);

        for (i = 0; i < tableColumnsItemBizPartnerDescription.length; i++) {
            moItemBizPartnerDescriptionPane.addTableColumn(tableColumnsItemBizPartnerDescription[i]);
        }

        SFormUtilities.createActionMap(rootPane, this, "publicActionAddItemDescription", "addBarcode", KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionModifyItemDescription", "modifyBarcode", KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), action, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            if (moBizPartnerItemDescription == null) {
                jcbPkBizPartnerId.requestFocus();
            }
            else {
                moItemBizPartnerDescriptionPane.requestFocus();
            }
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

    private void actionPkBizPartnerId() {
        miClient.pickOption(SDataConstants.BPSU_BP, moFieldPkBizPartnerId, null);
    }

    private void actionAddItemDescription() {
        if (jbAddItemDescription.isEnabled()) {
            moFormItemDescription.formRefreshCatalogues();
            moFormItemDescription.formReset();
            moFormItemDescription.setFormVisible(true);

            if (moFormItemDescription.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                moItemBizPartnerDescriptionPane.addTableRow(new SDataItemBizPartnerDescriptionRow(moFormItemDescription.getRegistry()));
                moItemBizPartnerDescriptionPane.renderTableRows();
                moItemBizPartnerDescriptionPane.setTableRowSelection(moItemBizPartnerDescriptionPane.getTableGuiRowCount() - 1);
            }
        }
    }

    private void actionModifyItemDescription() {
        int index = moItemBizPartnerDescriptionPane.getTable().getSelectedRow();

        if (jbModifyItemDescription.isEnabled()) {
            if (index != -1) {
                moFormItemDescription.formRefreshCatalogues();
                moFormItemDescription.formReset();
                moFormItemDescription.setRegistry((SDataItemBizPartnerDescription) moItemBizPartnerDescriptionPane.getTableRow(index).getData());
                moFormItemDescription.setVisible(true);

                if (moFormItemDescription.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                    moItemBizPartnerDescriptionPane.getTableModel().getTableRows().set(index, new SDataItemBizPartnerDescriptionRow(moFormItemDescription.getRegistry()));
                    moItemBizPartnerDescriptionPane.renderTableRows();
                    moItemBizPartnerDescriptionPane.setTableRowSelection(index);
                }
            }
        }
    }

    private void renderComboBoxBizPartner(boolean b) {
        jcbPkBizPartnerId.setEnabled(b);
        jbPkBizPartnerId.setEnabled(b);
    }

    public void publicActionAddItemDescription() {
        if (jbAddItemDescription.isEnabled()) {
            actionAddItemDescription();
        }
    }

    public void publicActionModifyItemDescription() {
        if (jbModifyItemDescription.isEnabled()) {
            actionModifyItemDescription();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton jbAddItemDescription;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbModifyItemDescription;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbPkBizPartnerId;
    private javax.swing.JComboBox jcbPkBizPartnerId;
    private javax.swing.JLabel jlPkBizPartnerId;
    private javax.swing.JPanel jpItemDescriptions;
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

        moBizPartnerItemDescription = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        moItemBizPartnerDescriptionPane.createTable(null);
        moItemBizPartnerDescriptionPane.clearTableRows();
        renderComboBoxBizPartner(true);
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbPkBizPartnerId, SDataConstants.BPSU_BP);
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
            if (moItemBizPartnerDescriptionPane.getTable().getRowCount() == 0) {
                validation.setMessage("Se debe ingresar al menos una descripción para un ítem en particular.");
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
        int i;
        moBizPartnerItemDescription = (SDataBizPartnerItemDescription) registry;
        SDataItemBizPartnerDescriptionRow itemBizPartnerDescriptionRow = null;

        moFieldPkBizPartnerId.setFieldValue(new int[] { moBizPartnerItemDescription.getPkBizPartnerId() });

        for (i = 0; i < moBizPartnerItemDescription.getDbmsItemBizPartnerDescriptions().size(); i++) {
            itemBizPartnerDescriptionRow = new SDataItemBizPartnerDescriptionRow(moBizPartnerItemDescription.getDbmsItemBizPartnerDescriptions().get(i));
            moItemBizPartnerDescriptionPane.addTableRow(itemBizPartnerDescriptionRow);
        }

        moItemBizPartnerDescriptionPane.renderTableRows();
        moItemBizPartnerDescriptionPane.setTableRowSelection(0);

        renderComboBoxBizPartner(false);
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        SDataItemBizPartnerDescription itemBizPartnerDescription = null;
        int i = 0;

        if (moBizPartnerItemDescription == null) {
            moBizPartnerItemDescription = new SDataBizPartnerItemDescription();
        }

        moBizPartnerItemDescription.setPkBizPartnerId(moFieldPkBizPartnerId.getKeyAsIntArray()[0]);

        moBizPartnerItemDescription.getDbmsItemBizPartnerDescriptions().clear();
        for (i = 0; i < moItemBizPartnerDescriptionPane.getTable().getRowCount(); i++) {
            itemBizPartnerDescription = new SDataItemBizPartnerDescription();
            itemBizPartnerDescription = (erp.mitm.data.SDataItemBizPartnerDescription) moItemBizPartnerDescriptionPane.getTableRow(i).getData();
            moBizPartnerItemDescription.getDbmsItemBizPartnerDescriptions().add(itemBizPartnerDescription);
        }

        return moBizPartnerItemDescription;
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
            else if (button == jbPkBizPartnerId) {
                actionPkBizPartnerId();
            }
            else if (button == jbAddItemDescription) {
                actionAddItemDescription();
            }
            else if (button == jbModifyItemDescription) {
                actionModifyItemDescription();
            }
        }
    }
}

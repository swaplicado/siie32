/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mitm.form;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STablePane;
import erp.mitm.data.SDataAttributeRow;
import erp.mitm.data.SDataMaterialAttribute;
import erp.mitm.data.SMaterialUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 *
 * @author Edwin Carmona
 */
public class SFormMaterialTypeVsAttributes extends javax.swing.JDialog implements java.awt.event.ActionListener {

    private int mnFormType;
    private boolean mbFirstTime;
    private int mnMaterialTypeId;
    private erp.client.SClientInterface miClient;

    private erp.lib.table.STablePane moPanelAvailableAttributes;
    private erp.lib.table.STablePane moPanelSelectedAttributes;

    /** Creates new form SFormMaterialTypeVsAttributes
     * @param client */
    public SFormMaterialTypeVsAttributes(erp.client.SClientInterface client) {
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
        jtfMaterialType = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jpAvailableAttributes = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jbAdd = new javax.swing.JButton();
        jbRemove = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jpSelectedAttributes = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jbUp = new javax.swing.JButton();
        jbDown = new javax.swing.JButton();

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
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPkBizPartnerId.setText("Tipo de material: *");
        jlPkBizPartnerId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel3.add(jlPkBizPartnerId);

        jtfMaterialType.setEditable(false);
        jtfMaterialType.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel3.add(jtfMaterialType);

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel10.setLayout(new java.awt.GridLayout(1, 2));

        jPanel4.setPreferredSize(new java.awt.Dimension(350, 100));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jpAvailableAttributes.setBorder(javax.swing.BorderFactory.createTitledBorder("Atributos disponibles:"));
        jpAvailableAttributes.setLayout(new java.awt.BorderLayout());
        jPanel4.add(jpAvailableAttributes, java.awt.BorderLayout.CENTER);

        jPanel6.setLayout(new java.awt.GridLayout(15, 1));

        jbAdd.setText(">");
        jPanel6.add(jbAdd);

        jbRemove.setText("<");
        jbRemove.setHideActionText(true);
        jPanel6.add(jbRemove);

        jPanel4.add(jPanel6, java.awt.BorderLayout.EAST);

        jPanel10.add(jPanel4);

        jPanel7.setPreferredSize(new java.awt.Dimension(350, 100));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jpSelectedAttributes.setBorder(javax.swing.BorderFactory.createTitledBorder("Atributos asignados:"));
        jpSelectedAttributes.setLayout(new java.awt.BorderLayout());
        jPanel7.add(jpSelectedAttributes, java.awt.BorderLayout.CENTER);

        jPanel9.setLayout(new java.awt.GridLayout(15, 1));

        jbUp.setText("Arriba");
        jPanel9.add(jbUp);

        jbDown.setText("Abajo");
        jPanel9.add(jbDown);

        jPanel7.add(jPanel9, java.awt.BorderLayout.EAST);

        jPanel10.add(jPanel7);

        jPanel2.add(jPanel10, java.awt.BorderLayout.LINE_END);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(736, 489));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        int i;

        erp.lib.table.STableColumnForm tableColumnsAttributes[];

        moPanelAvailableAttributes = new STablePane(miClient);
        jpAvailableAttributes.add(moPanelAvailableAttributes, BorderLayout.CENTER);
        
        moPanelSelectedAttributes = new STablePane(miClient);
        jpSelectedAttributes.add(moPanelSelectedAttributes, BorderLayout.CENTER);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbAdd.addActionListener(this);
        jbRemove.addActionListener(this);
        jbUp.addActionListener(this);
        jbDown.addActionListener(this);

        i = 0;
        tableColumnsAttributes = new STableColumnForm[1];
        tableColumnsAttributes[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Atributo", 300);

        for (i = 0; i < tableColumnsAttributes.length; i++) {
            moPanelAvailableAttributes.addTableColumn(tableColumnsAttributes[i]);
        }
        
        for (i = 0; i < tableColumnsAttributes.length; i++) {
            moPanelSelectedAttributes.addTableColumn(tableColumnsAttributes[i]);
        }

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
        }
    }
    
    public void setMaterialType(String materialType, int idMaterialType) {
        jtfMaterialType.setText(materialType);
        mnMaterialTypeId = idMaterialType;
        setAttributes();
    }
    
    private void setAttributes() {
        ArrayList<SDataMaterialAttribute> lAllAttributes = SMaterialUtils.getAllAttributes(miClient.getSession().getStatement());
        ArrayList<SDataMaterialAttribute> lSelectedAttributes = SMaterialUtils.getAttributesOfType(miClient.getSession().getStatement(), mnMaterialTypeId);
        
        ArrayList<SDataMaterialAttribute> lAvailableAttributes = new ArrayList<>();
        
        for (SDataMaterialAttribute oAttA : lAllAttributes) {
            boolean bFound = false;
            for (SDataMaterialAttribute oAttB : lSelectedAttributes) {
                if (oAttA.getPkMaterialAttributeId() == oAttB.getPkMaterialAttributeId()) {
                    bFound = true;
                    break;
                }
            }
            if (!bFound) {
                lAvailableAttributes.add(oAttA);
            }
        }
        
        moPanelAvailableAttributes.createTable(null);
        moPanelAvailableAttributes.clearTableRows();
        
        for (SDataMaterialAttribute oAttAvailable : lAvailableAttributes) {
            moPanelAvailableAttributes.addTableRow(new SDataAttributeRow(oAttAvailable));
        }
        
        moPanelAvailableAttributes.renderTableRows();
        moPanelAvailableAttributes.setTableRowSelection(moPanelAvailableAttributes.getTableGuiRowCount() - 1);
        
        moPanelSelectedAttributes.createTable(null);
        moPanelSelectedAttributes.clearTableRows();
        
        for (SDataMaterialAttribute oAttSelected : lSelectedAttributes) {
            moPanelSelectedAttributes.addTableRow(new SDataAttributeRow(oAttSelected));
        }
        
        moPanelSelectedAttributes.renderTableRows();
        moPanelSelectedAttributes.setTableRowSelection(moPanelSelectedAttributes.getTableGuiRowCount() - 1);
    }
    
    private void actionAdd() {
        SDataMaterialAttribute selected = (SDataMaterialAttribute) moPanelAvailableAttributes.getSelectedTableRow().getData();
        moPanelSelectedAttributes.addTableRow(new SDataAttributeRow(selected));
        moPanelAvailableAttributes.removeTableRow(moPanelAvailableAttributes.getTable().getSelectedRow());
        
        moPanelAvailableAttributes.renderTableRows();
        moPanelSelectedAttributes.renderTableRows();
    }
    
    private void actionRemove() {
        SDataMaterialAttribute selected = (SDataMaterialAttribute) moPanelSelectedAttributes.getSelectedTableRow().getData();
        moPanelAvailableAttributes.addTableRow(new SDataAttributeRow(selected));
        moPanelSelectedAttributes.removeTableRow(moPanelSelectedAttributes.getTable().getSelectedRow());
        
        moPanelSelectedAttributes.renderTableRows();
        moPanelAvailableAttributes.renderTableRows();
    }
    
    private void actionMoveUp() {
        int index = moPanelSelectedAttributes.getTable().getSelectedRow();
        
        if (index == 0 || moPanelSelectedAttributes.getTable().getRowCount() <= 1) {
            return;
        }
        
        SDataMaterialAttribute current = (SDataMaterialAttribute) moPanelSelectedAttributes.getSelectedTableRow().getData();
        SDataMaterialAttribute upAtt = (SDataMaterialAttribute) moPanelSelectedAttributes.getTableRow(index - 1).getData();
        
        moPanelSelectedAttributes.getTableModel().getTableRows().set(index, new SDataAttributeRow(upAtt));
        moPanelSelectedAttributes.getTableModel().getTableRows().set(index - 1, new SDataAttributeRow(current));
        
        moPanelSelectedAttributes.renderTableRows();
        moPanelSelectedAttributes.setTableRowSelection(index - 1);
    }
    
    private void actionMoveDown() {
        int index = moPanelSelectedAttributes.getTable().getSelectedRow();
        
        if (index + 1 == moPanelSelectedAttributes.getTable().getRowCount() || moPanelSelectedAttributes.getTable().getRowCount() <= 1) {
            return;
        }
        
        SDataMaterialAttribute current = (SDataMaterialAttribute) moPanelSelectedAttributes.getSelectedTableRow().getData();
        SDataMaterialAttribute downAtt = (SDataMaterialAttribute) moPanelSelectedAttributes.getTableRow(index + 1).getData();
        
        moPanelSelectedAttributes.getTableModel().getTableRows().set(index, new SDataAttributeRow(downAtt));
        moPanelSelectedAttributes.getTableModel().getTableRows().set(index + 1, new SDataAttributeRow(current));
        
        moPanelSelectedAttributes.renderTableRows();
        moPanelSelectedAttributes.setTableRowSelection(index + 1);
    }

    private void actionOk() {
        ArrayList<SDataMaterialAttribute> lSelectedAttributes = new ArrayList<>();
        
        SDataMaterialAttribute obj;
        for (int i = 0; i < moPanelSelectedAttributes.getTable().getRowCount(); i++) {
            obj = (erp.mitm.data.SDataMaterialAttribute) moPanelSelectedAttributes.getTableRow(i).getData();
            lSelectedAttributes.add(obj);
        }
        
        if (moPanelSelectedAttributes.getTable().getRowCount() == 0) {
            if (miClient.showMsgBoxConfirm("¿Desea dejar la lista de atributos vacía?") != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        SMaterialUtils.saveSelectedAttributes(miClient.getSession(), lSelectedAttributes, mnMaterialTypeId);
        setVisible(false);
    }

    private void actionCancel() {
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDown;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbRemove;
    private javax.swing.JButton jbUp;
    private javax.swing.JLabel jlPkBizPartnerId;
    private javax.swing.JPanel jpAvailableAttributes;
    private javax.swing.JPanel jpSelectedAttributes;
    private javax.swing.JTextField jtfMaterialType;
    // End of variables declaration//GEN-END:variables

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
            else if (button == jbAdd) {
                actionAdd();
            }
            else if (button == jbRemove) {
                actionRemove();
            }
            else if (button == jbUp) {
                actionMoveUp();
            }
            else if (button == jbDown) {
                actionMoveDown();
            }
        }
    }
}

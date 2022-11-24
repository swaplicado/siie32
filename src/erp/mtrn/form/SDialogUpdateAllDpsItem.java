/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STablePaneGrid;
import erp.mtrn.data.STrnUtilities;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsEntry;
import static erp.mtrn.data.STrnUtilities.isServiceOrder;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Claudio Peña
 */
public class SDialogUpdateAllDpsItem extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.ItemListener {
    
    public static final int CONCEPT_LENGTH_MAX = 130;
    
    private erp.client.SClientInterface miClient;
    private int mnFormResult;
    private int mnFormStatus;
    private int mnRegistryType;
    private boolean mbFirstTime;
    private erp.mtrn.data.SDataDps moDps;
    private erp.mtrn.data.SDataDpsEntry moDpsEntry;
    private erp.mtrn.form.SPanelDps moPanelDps;
    private erp.lib.table.STablePaneGrid moPaneGrid;
    
    private erp.lib.form.SFormField moFieldPkItemId;
    /** Creates new form SDialogUpdateDpsAccountCenterCost
     * @param client 
    */
    public SDialogUpdateAllDpsItem(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpDps = new javax.swing.JPanel();
        jlPanelDps = new javax.swing.JLabel();
        jpDpsComms = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jlItem = new javax.swing.JLabel();
        jtfItem = new javax.swing.JTextField();
        jlDummy = new javax.swing.JLabel();
        jlOriginalQuantity = new javax.swing.JLabel();
        jtfOriginalQuantity = new javax.swing.JTextField();
        jtfOriginalUnitSymbolRo = new javax.swing.JTextField();
        jPanel51 = new javax.swing.JPanel();
        jlItemOld = new javax.swing.JLabel();
        jtfItemOld = new javax.swing.JTextField();
        jlDummy1 = new javax.swing.JLabel();
        jlItemNew = new javax.swing.JLabel();
        jcbFkItemId = new javax.swing.JComboBox();
        jbFkItemId = new javax.swing.JButton();
        jpControls = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Modificación de contabilización");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpDps.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpDps.setLayout(new java.awt.BorderLayout());

        jlPanelDps.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlPanelDps.setText("[Panel de documento de compras-ventas]");
        jlPanelDps.setPreferredSize(new java.awt.Dimension(100, 200));
        jpDps.add(jlPanelDps, java.awt.BorderLayout.NORTH);

        jpDpsComms.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos adicionales:"));
        jpDpsComms.setLayout(new java.awt.BorderLayout(0, 1));

        jPanel1.setPreferredSize(new java.awt.Dimension(108, 150));
        jPanel1.setLayout(new java.awt.GridLayout(5, 1, 5, 1));

        jPanel48.setPreferredSize(new java.awt.Dimension(108, 23));
        jPanel48.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlItem.setText("Ítem:");
        jlItem.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel48.add(jlItem);

        jtfItem.setEditable(false);
        jtfItem.setText("ITEM");
        jtfItem.setFocusable(false);
        jtfItem.setPreferredSize(new java.awt.Dimension(277, 23));
        jPanel48.add(jtfItem);

        jlDummy.setPreferredSize(new java.awt.Dimension(5, 23));
        jPanel48.add(jlDummy);

        jlOriginalQuantity.setText("Cantidad:");
        jlOriginalQuantity.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel48.add(jlOriginalQuantity);

        jtfOriginalQuantity.setEditable(false);
        jtfOriginalQuantity.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfOriginalQuantity.setText("0.0000");
        jtfOriginalQuantity.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel48.add(jtfOriginalQuantity);

        jtfOriginalUnitSymbolRo.setEditable(false);
        jtfOriginalUnitSymbolRo.setText("UN");
        jtfOriginalUnitSymbolRo.setFocusable(false);
        jtfOriginalUnitSymbolRo.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel48.add(jtfOriginalUnitSymbolRo);

        jPanel1.add(jPanel48);

        jPanel51.setPreferredSize(new java.awt.Dimension(108, 23));
        jPanel51.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlItemOld.setText("Ítem actual:");
        jlItemOld.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel51.add(jlItemOld);

        jtfItemOld.setEditable(false);
        jtfItemOld.setText("ITEM REFERENCIA ACTUAL");
        jtfItemOld.setFocusable(false);
        jtfItemOld.setPreferredSize(new java.awt.Dimension(277, 23));
        jtfItemOld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfItemOldActionPerformed(evt);
            }
        });
        jPanel51.add(jtfItemOld);

        jlDummy1.setPreferredSize(new java.awt.Dimension(5, 23));
        jPanel51.add(jlDummy1);

        jlItemNew.setText("Ítem nuevo:");
        jlItemNew.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel51.add(jlItemNew);

        jcbFkItemId.setMaximumRowCount(16);
        jcbFkItemId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkItemId.setPreferredSize(new java.awt.Dimension(315, 23));
        jPanel51.add(jcbFkItemId);

        jbFkItemId.setText("...");
        jbFkItemId.setToolTipText("Seleccionar ítem");
        jbFkItemId.setFocusable(false);
        jbFkItemId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel51.add(jbFkItemId);

        jPanel1.add(jPanel51);

        jpDpsComms.add(jPanel1, java.awt.BorderLayout.NORTH);

        jpDps.add(jpDpsComms, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpDps, java.awt.BorderLayout.CENTER);

        jpControls.setPreferredSize(new java.awt.Dimension(392, 33));
        jpControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jpControls.add(jbCancel);

        getContentPane().add(jpControls, java.awt.BorderLayout.PAGE_END);

        getAccessibleContext().setAccessibleName("Modificación de item referencia y conceptos ");

        setSize(new java.awt.Dimension(906, 372));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jtfItemOldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfItemOldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfItemOldActionPerformed

    private void initComponentsExtra() {
        int i = 0;
                
        moFieldPkItemId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkItemId, jlItemNew);
        moFieldPkItemId.setPickerButton(jbFkItemId);
       
        try {
            moPanelDps = new SPanelDps(miClient, "");
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        
        moFieldPkItemId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkItemId, jlItemNew);
        moFieldPkItemId.setPickerButton(jbFkItemId);
        
        jpDps.remove(jlPanelDps);
        jpDps.add(moPanelDps, BorderLayout.NORTH);
        moPaneGrid = new STablePaneGrid(miClient);
        
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbFkItemId.addActionListener(this);
                
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
        }
    }
    
    private void renderItem() {
        jtfItem.setText(moDpsEntry.getDbmsItem());
        jtfOriginalQuantity.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(moDpsEntry.getOriginalQuantity()));
        jtfOriginalUnitSymbolRo.setText(moDpsEntry.getDbmsOriginalUnitSymbol());
        
        jtfItemOld.setText(moDpsEntry.getDbmsItem());
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
            try {
                if (miClient.showMsgBoxConfirm("¿Esta seguro que desea modificar el ítem en todos los documentos relacionados? \n Esta función es solo para ordenes de servicio") == JOptionPane.OK_OPTION) {
                    if(isServiceOrder(miClient, moDps.getFkSourceYearId_n(), moDps.getFkSourceDocId_n())) {
                        if ((jcbFkItemId.getSelectedIndex() > 0)) {
                            int itemNew = (Integer) (moFieldPkItemId.getKeyAsIntArray())[0];
                            moDpsEntry.getPkYearId();
                            moDpsEntry.getPkDocId();
                            STrnUtilities.updateDpsItemAll(miClient.getSession().getStatement().getConnection(), miClient, moDps.getPkYearId(), moDps.getPkDocId(), moDpsEntry, itemNew );                             
                        }
                        else {
                            miClient.showMsgBoxInformation("No se editó ningún campo para el reglón" );
                        }
                        miClient.showMsgBoxInformation("El proceso ha terminado.");
                        mnFormResult = SGuiConsts.FORM_RESULT_OK;
                        dispose();
                    }
                    else {
                        miClient.showMsgBoxInformation("Esta factura corresponde a una orden de compra");
                        this.setVisible(false);
                    }
                }
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
    }

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbFkItemId;
    private javax.swing.JButton jbOk;
    private javax.swing.JComboBox jcbFkItemId;
    private javax.swing.JLabel jlDummy;
    private javax.swing.JLabel jlDummy1;
    private javax.swing.JLabel jlItem;
    private javax.swing.JLabel jlItemNew;
    private javax.swing.JLabel jlItemOld;
    private javax.swing.JLabel jlOriginalQuantity;
    private javax.swing.JLabel jlPanelDps;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpDps;
    private javax.swing.JPanel jpDpsComms;
    private javax.swing.JTextField jtfItem;
    private javax.swing.JTextField jtfItemOld;
    private javax.swing.JTextField jtfOriginalQuantity;
    private javax.swing.JTextField jtfOriginalUnitSymbolRo;
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

        moDps = null;
        moPanelDps.setDps(null, null);
        moFieldPkItemId.resetField();

        SFormUtilities.populateComboBox(miClient, jcbFkItemId, SDataConstants.ITMU_ITEM);

    }

    @Override
    public void formRefreshCatalogues() {
          SFormUtilities.populateComboBox(miClient, jcbFkItemId, SDataConstants.ITMU_ITEM);

    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();
        String message = "";

        if (jcbFkItemId.getSelectedIndex() <= 0) {
                validation.setIsError(true);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case SDataConstants.TRN_DPS:
                moDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, value, SLibConstants.EXEC_MODE_VERBOSE);
                moPanelDps.setDps(moDps, null);
                break;
            case SDataConstants.TRN_DPS_ETY:
                moDpsEntry = (SDataDpsEntry) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS_ETY, value, SLibConstants.EXEC_MODE_VERBOSE);
                renderItem();
                break;
            default:
        }
    }

    public void setRegistryType(int registryType){
        mnRegistryType = registryType;
    }
    
    @Override
    public java.lang.Object getValue(int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public javax.swing.JLabel getTimeoutLabel() {
        return null;
    }
    
    private void actionFkItemId() {
        if (miClient.pickOption(SDataConstants.ITMX_ITEM_IOG, moFieldPkItemId, SDataConstantsSys.ITMS_CL_ITEM_SAL_PRO) == SLibConstants.FORM_RESULT_OK) {
            itemStateChangedPkLinkTypeId();
        }
    }
    
    private void itemStateChangedPkLinkTypeId() {
        boolean enable = true;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbFkItemId) {
                actionFkItemId();
            }
        }
    }
    
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof javax.swing.JComboBox && e.getStateChange() == ItemEvent.SELECTED) {
                JComboBox comboBox = (JComboBox)  e.getSource();
            }
        }

}

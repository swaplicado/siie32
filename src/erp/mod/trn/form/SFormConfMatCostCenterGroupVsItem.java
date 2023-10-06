/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.form;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.trn.db.SDbConfCostCenterGroupVsItem;
import erp.mod.trn.db.SDbMaterialCostCenterGroupItem;
import erp.mod.trn.db.SRowCCGroupVsItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import sa.lib.SLibConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;

/**
 *
 * @author Isabel Servín
 */
public class SFormConfMatCostCenterGroupVsItem extends SBeanForm implements ActionListener, ItemListener {
    
    private SDbConfCostCenterGroupVsItem moRegistry;
    
    private SGridPaneForm moGrid;
    
    private ArrayList<SRowCCGroupVsItem> maRows;
    
    /**
     * Creates new form SFormConfCostCenterVsItem
     * @param client
     * @param title
     */
    public SFormConfMatCostCenterGroupVsItem(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.TRNX_CONF_CC_GRP_VS_ITM, 0, title);
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

        jpRegistry = new javax.swing.JPanel();
        jpCostCenter = new javax.swing.JPanel();
        jpWarehouse = new javax.swing.JPanel();
        jlWarehouse = new javax.swing.JLabel();
        moTextCCGroup = new sa.lib.gui.bean.SBeanFieldText();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        moKeyLink = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        moKeyReference = new sa.lib.gui.bean.SBeanFieldKey();
        jbAdd = new javax.swing.JButton();
        jbDelete = new javax.swing.JButton();
        jpGrid = new javax.swing.JPanel();

        jpRegistry.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpRegistry.setLayout(new java.awt.BorderLayout());

        jpCostCenter.setLayout(new java.awt.BorderLayout());

        jpWarehouse.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlWarehouse.setText("Grupo de centro de costo:");
        jlWarehouse.setPreferredSize(new java.awt.Dimension(150, 23));
        jpWarehouse.add(jlWarehouse);

        moTextCCGroup.setEditable(false);
        moTextCCGroup.setEnabled(false);
        moTextCCGroup.setPreferredSize(new java.awt.Dimension(400, 23));
        jpWarehouse.add(moTextCCGroup);

        jpCostCenter.add(jpWarehouse, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(2, 0));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("Tipo de referencia:");
        jLabel1.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel4.add(jLabel1);

        moKeyLink.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel4.add(moKeyLink);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel2.setText("Referencia:");
        jLabel2.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel5.add(jLabel2);

        moKeyReference.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel5.add(moKeyReference);

        jbAdd.setText("Agregar");
        jbAdd.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel5.add(jbAdd);

        jbDelete.setText("Eliminar");
        jbDelete.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel5.add(jbDelete);

        jPanel2.add(jPanel5);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        jpGrid.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jpGrid, java.awt.BorderLayout.CENTER);

        jpCostCenter.add(jPanel1, java.awt.BorderLayout.CENTER);

        jpRegistry.add(jpCostCenter, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpRegistry, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbDelete;
    private javax.swing.JLabel jlWarehouse;
    private javax.swing.JPanel jpCostCenter;
    private javax.swing.JPanel jpGrid;
    private javax.swing.JPanel jpRegistry;
    private javax.swing.JPanel jpWarehouse;
    private sa.lib.gui.bean.SBeanFieldKey moKeyLink;
    private sa.lib.gui.bean.SBeanFieldKey moKeyReference;
    private sa.lib.gui.bean.SBeanFieldText moTextCCGroup;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 800, 500);
        
        moTextCCGroup.setTextSettings("Grupo de centro de costo", 250);
        
        moGrid = new SGridPaneForm(miClient, SModConsts.TRN_MAT_CC_GRP_ITEM, 0, "Grupo de centro de costo vs. ítem") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> columns = new ArrayList<>();

                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "Tipo referencia"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Referencia"));

                return columns;
            }
        };

        jpGrid.add(moGrid);
        mvFormGrids.add(moGrid);
        
        jpCommandRight.remove(jbEdit);
        jpCommandRight.remove(jbReadInfo);
    }
    
    private void populateGrid() {
        Vector<SGridRow> vRows = new Vector<>();
        if (maRows.size() > 0) {
            vRows.addAll(maRows);
        }
        moGrid.populateGrid(vRows);
    }
    
    private void actionAdd() {
        try {
            SRowCCGroupVsItem row = new SRowCCGroupVsItem();
            row.setLinkId(moKeyLink.getValue()[0]);
            row.setReferenceId(moKeyReference.getValue()[0]);
            row.readReference(miClient.getSession());
            maRows.add(row);
            populateGrid();
        }
        catch (Exception e){
            miClient.showMsgBoxError(e.getMessage());
        }
    }

    private void actionDelete() {
        SRowCCGroupVsItem selectedRow = (SRowCCGroupVsItem) moGrid.getSelectedGridRow();
        for (SRowCCGroupVsItem row : maRows) {
            if (SLibUtilities.compareKeys(row.getRowPrimaryKey(), selectedRow.getRowPrimaryKey())) {
                maRows.remove(row);
                break;
            }
        }
        populateGrid();
    }
    
    private void itemChangeKeyLink() {
        boolean enable = true;

        moKeyReference.removeAllItems();
        moKeyReference.setEnabled(false);
        
        try {
            if (moKeyLink.getSelectedIndex() > 0) {
                switch (moKeyLink.getValue()[0]) {
                    case SModSysConsts.ITMS_LINK_ALL:
                        enable = false;
                        break;
                    case SModSysConsts.ITMS_LINK_CT_ITEM:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMS_CT_ITEM, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_CL_ITEM:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMS_CL_ITEM, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_TP_ITEM:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMS_TP_ITEM, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_IFAM:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMU_IFAM, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_IGRP:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMU_IGRP, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_IGEN:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMU_IGEN, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_LINE:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMU_LINE, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_BRD:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMU_BRD, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_MFR:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMU_MFR, SLibConsts.UNDEFINED, null);
                        break;
                    case SModSysConsts.ITMS_LINK_ITEM:
                        miClient.getSession().populateCatalogue(moKeyReference, SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, null);
                        break;
                    default:
                        enable = false;
                        throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_FORM_PICK);
                }
                    
                if (enable) {
                    moKeyReference.setSelectedIndex(0);
                    moKeyReference.setEnabled(true);
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }
    
    @Override
    public void addAllListeners() {
        jbAdd.addActionListener(this);
        jbDelete.addActionListener(this);
        moKeyLink.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbAdd.removeActionListener(this);
        jbDelete.removeActionListener(this);
        moKeyLink.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        moTextCCGroup.setValue("");
        
        maRows = new ArrayList<>();
        
        miClient.getSession().populateCatalogue(moKeyLink, SModConsts.ITMS_LINK, SModConsts.TRN_MAT_CC_GRP_ITEM, null);
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbConfCostCenterGroupVsItem) registry;
        
        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;
        
        removeAllListeners();
        reloadCatalogues();
        
        moTextCCGroup.setValue(moRegistry.getAuxCCGrp().getName());
        
        for (SDbMaterialCostCenterGroupItem gi : moRegistry.getCostCenterGroupItem()) {
            SRowCCGroupVsItem row = new SRowCCGroupVsItem();
            row.setLinkId(gi.getPkLinkId());
            row.setReferenceId(gi.getPkReferenceId());
            row.readReference(miClient.getSession());
            maRows.add(row);
        }
            
        populateGrid();
        
        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbConfCostCenterGroupVsItem registry = moRegistry.clone();
        
        if (registry.isRegistryNew()) { }
        
        registry.getCostCenterGroupItem().clear();
        for (SRowCCGroupVsItem row : maRows) {
            SDbMaterialCostCenterGroupItem gi = new SDbMaterialCostCenterGroupItem();
            gi.setPkLinkId(row.getLinkId());
            gi.setPkReferenceId(row.getReferenceId());
            registry.getCostCenterGroupItem().add(gi);
        }
        
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
            
            if (button == jbAdd) {
                actionAdd();
            }
            else if (button == jbDelete) {
                actionDelete();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox) {
            JComboBox comboBox = (JComboBox) e.getSource();
            
            if (comboBox == moKeyLink) {
                itemChangeKeyLink();
            }
        }
    }
}
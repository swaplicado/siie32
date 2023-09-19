/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.form;

import erp.mod.SModConsts;
import erp.mod.trn.db.SDbConfMatConsSubentityCCVsCostCenterGroup;
import erp.mod.trn.db.SDbMaterialConsumptionSubentityCostCenterCCGroup;
import erp.mod.trn.db.SDbMaterialCostCenterGroup;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
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
public class SFormConfMatConsSubentityCCVsCostCenterGroup extends SBeanForm {
    
    private SDbConfMatConsSubentityCCVsCostCenterGroup moRegistry;
    
    private SGridPaneForm moGridCostCenterGroup;
    
    private ArrayList<SDbMaterialCostCenterGroup> maMatCostCenterGroup;
    private ArrayList<SDbMaterialConsumptionSubentityCostCenterCCGroup> maMatConsumptionSubCCGrp;

    /**
     * Creates new form SFormConfEmployeeVsEntity
     * @param client
     * @param title
     */
    public SFormConfMatConsSubentityCCVsCostCenterGroup(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.TRNX_CONF_SUBENT_VS_CC_GRP, 0, title);
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
        jpConsumption = new javax.swing.JPanel();
        jpEntity = new javax.swing.JPanel();
        jpEntityData = new javax.swing.JPanel();
        jlEntity = new javax.swing.JLabel();
        moTextEntity = new sa.lib.gui.bean.SBeanFieldText();
        jlSubentity = new javax.swing.JLabel();
        moTextSubentity = new sa.lib.gui.bean.SBeanFieldText();
        jPanel2 = new javax.swing.JPanel();
        jlEntity1 = new javax.swing.JLabel();
        moTextCodeCC = new sa.lib.gui.bean.SBeanFieldText();
        jlSubentity1 = new javax.swing.JLabel();
        moTextCC = new sa.lib.gui.bean.SBeanFieldText();
        jpGrid = new javax.swing.JPanel();

        jpRegistry.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpRegistry.setLayout(new java.awt.BorderLayout());

        jpConsumption.setLayout(new java.awt.BorderLayout());

        jpEntity.setLayout(new java.awt.GridLayout(2, 0));

        jpEntityData.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlEntity.setText("Entidad:");
        jlEntity.setPreferredSize(new java.awt.Dimension(100, 16));
        jpEntityData.add(jlEntity);

        moTextEntity.setEditable(false);
        moTextEntity.setEnabled(false);
        moTextEntity.setPreferredSize(new java.awt.Dimension(250, 23));
        jpEntityData.add(moTextEntity);

        jlSubentity.setText("Subentidad:");
        jlSubentity.setPreferredSize(new java.awt.Dimension(100, 16));
        jpEntityData.add(jlSubentity);

        moTextSubentity.setEditable(false);
        moTextSubentity.setEnabled(false);
        moTextSubentity.setPreferredSize(new java.awt.Dimension(250, 23));
        jpEntityData.add(moTextSubentity);

        jpEntity.add(jpEntityData);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlEntity1.setText("Código CC:");
        jlEntity1.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel2.add(jlEntity1);

        moTextCodeCC.setEditable(false);
        moTextCodeCC.setEnabled(false);
        moTextCodeCC.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel2.add(moTextCodeCC);

        jlSubentity1.setText("Centro de costo:");
        jlSubentity1.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel2.add(jlSubentity1);

        moTextCC.setEditable(false);
        moTextCC.setEnabled(false);
        moTextCC.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel2.add(moTextCC);

        jpEntity.add(jPanel2);

        jpConsumption.add(jpEntity, java.awt.BorderLayout.NORTH);

        jpGrid.setPreferredSize(new java.awt.Dimension(100, 400));
        jpGrid.setLayout(new java.awt.BorderLayout());
        jpConsumption.add(jpGrid, java.awt.BorderLayout.CENTER);

        jpRegistry.add(jpConsumption, java.awt.BorderLayout.NORTH);

        getContentPane().add(jpRegistry, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel jlEntity;
    private javax.swing.JLabel jlEntity1;
    private javax.swing.JLabel jlSubentity;
    private javax.swing.JLabel jlSubentity1;
    private javax.swing.JPanel jpConsumption;
    private javax.swing.JPanel jpEntity;
    private javax.swing.JPanel jpEntityData;
    private javax.swing.JPanel jpGrid;
    private javax.swing.JPanel jpRegistry;
    private sa.lib.gui.bean.SBeanFieldText moTextCC;
    private sa.lib.gui.bean.SBeanFieldText moTextCodeCC;
    private sa.lib.gui.bean.SBeanFieldText moTextEntity;
    private sa.lib.gui.bean.SBeanFieldText moTextSubentity;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 880, 550);
        
        moTextEntity.setTextSettings("Entidad", 250);
        moTextSubentity.setTextSettings("Subentidad", 250);
        moTextCodeCC.setTextSettings("Codigo CC", 250);
        moTextCC.setTextSettings("Centro de costo", 250);
        
        moGridCostCenterGroup = new SGridPaneForm(miClient, SModConsts.TRN_MAT_CONS_SUBENT_CC_CC_GRP, 0, "Grupo centro de costo") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> columns = new ArrayList<>();

                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Grupo centro de costo"));
                SGridColumnForm col = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_M, "Seleccionado");
                col.setEditable(true);
                columns.add(col);

                return columns;
            }
        };

        jpGrid.add(moGridCostCenterGroup);
        mvFormGrids.add(moGridCostCenterGroup);
        
        jpCommandRight.remove(jbEdit);
        jpCommandRight.remove(jbReadInfo);
    }
    
    private void readCostCenterGroups() {
        try {
            Statement statement = miClient.getSession().getDatabase().getConnection().createStatement();
            String sql = "SELECT id_mat_cc_grp FROM trn_mat_cc_grp WHERE NOT b_del ORDER BY id_mat_cc_grp";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                boolean found = false;
                for (SDbMaterialConsumptionSubentityCostCenterCCGroup ee : maMatConsumptionSubCCGrp) {
                    if (resultSet.getInt(1) == ee.getPkMaterialCostCenterGroupId()) {
                        found = true;
                        break;
                    }
                }
                SDbMaterialCostCenterGroup row = new SDbMaterialCostCenterGroup();
                row.read(miClient.getSession(), new int[] { resultSet.getInt(1) });
                row.setRowSelected(found);
                maMatCostCenterGroup.add(row);
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void populateGrid() {
        Vector<SGridRow> vRows = new Vector<>();
        if (maMatCostCenterGroup.size() > 0) {
            vRows.addAll(maMatCostCenterGroup);
        }
        moGridCostCenterGroup.populateGrid(vRows);
    }
    
    @Override
    public void addAllListeners() {
    
    }

    @Override
    public void removeAllListeners() {
    
    }

    @Override
    public void reloadCatalogues() {
        moTextEntity.setValue("");
        moTextSubentity.setValue("");
        moTextCodeCC.setValue("");
        moTextCC.setValue("");
        
        maMatCostCenterGroup = new ArrayList<>();
        maMatConsumptionSubCCGrp = new ArrayList<>();
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbConfMatConsSubentityCCVsCostCenterGroup) registry;
        
        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;
        
        removeAllListeners();
        reloadCatalogues();
        
        moTextEntity.setValue(moRegistry.getAuxMatConsumptionSubentity().getXtaConsumptionEntityName());
        moTextSubentity.setValue(moRegistry.getAuxMatConsumptionSubentity().getName());
        moTextCodeCC.setValue(moRegistry.getAuxCostCenter().getPkCostCenterIdXXX());
        moTextCC.setValue(moRegistry.getAuxCostCenter().getCostCenter());
        
        for (SDbMaterialConsumptionSubentityCostCenterCCGroup ee : moRegistry.getConsSubCostCenterCCGrp()) {
            maMatConsumptionSubCCGrp.add(ee);
        }
        
        readCostCenterGroups();
        
        populateGrid();
        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbConfMatConsSubentityCCVsCostCenterGroup registry = moRegistry.clone();
        
        if (registry.isRegistryNew()) { }
        
        registry.getConsSubCostCenterCCGrp().clear();
        for (SDbMaterialCostCenterGroup cc : maMatCostCenterGroup) {
            if (cc.getIsRowSelected()) {
                SDbMaterialConsumptionSubentityCostCenterCCGroup mcscc = new SDbMaterialConsumptionSubentityCostCenterCCGroup();
                mcscc.setPkMaterialCostCenterGroupId(cc.getRowPrimaryKey()[0]);
                registry.getConsSubCostCenterCCGrp().add(mcscc);
            }
        }
        
        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        return validation;
    }
}

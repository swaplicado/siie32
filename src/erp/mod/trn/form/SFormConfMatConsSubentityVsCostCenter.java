/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.form;

import erp.mod.SModConsts;
import erp.mod.trn.db.SDbConfMatConsSubentityVsCostCenter;
import erp.mod.trn.db.SDbMaterialConsumptionSubentityCostCenter;
import erp.mod.trn.db.SRowMatCostCenter;
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
public class SFormConfMatConsSubentityVsCostCenter extends SBeanForm {
    
    private SDbConfMatConsSubentityVsCostCenter moRegistry;
    
    private SGridPaneForm moGridCostCenter;
    
    private ArrayList<SRowMatCostCenter> maMatCostCenter;
    private ArrayList<SDbMaterialConsumptionSubentityCostCenter> maMatConsumptionSubCC;

    /**
     * Creates new form SFormConfEmployeeVsEntity
     * @param client
     * @param title
     */
    public SFormConfMatConsSubentityVsCostCenter(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.TRNX_CONF_SUBENT_VS_CC, 0, title);
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
        jlEntity = new javax.swing.JLabel();
        moTextEntity = new sa.lib.gui.bean.SBeanFieldText();
        jlSubentity = new javax.swing.JLabel();
        moTextSubentity = new sa.lib.gui.bean.SBeanFieldText();
        jpGrid = new javax.swing.JPanel();

        jpRegistry.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpRegistry.setLayout(new java.awt.BorderLayout());

        jpConsumption.setLayout(new java.awt.BorderLayout());

        jpEntity.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlEntity.setText("Centro:");
        jlEntity.setPreferredSize(new java.awt.Dimension(70, 16));
        jpEntity.add(jlEntity);

        moTextEntity.setEditable(false);
        moTextEntity.setEnabled(false);
        moTextEntity.setPreferredSize(new java.awt.Dimension(250, 23));
        jpEntity.add(moTextEntity);

        jlSubentity.setText("Subcentro:");
        jlSubentity.setPreferredSize(new java.awt.Dimension(70, 16));
        jpEntity.add(jlSubentity);

        moTextSubentity.setEditable(false);
        moTextSubentity.setEnabled(false);
        moTextSubentity.setPreferredSize(new java.awt.Dimension(250, 23));
        jpEntity.add(moTextSubentity);

        jpConsumption.add(jpEntity, java.awt.BorderLayout.NORTH);

        jpGrid.setPreferredSize(new java.awt.Dimension(100, 430));
        jpGrid.setLayout(new java.awt.BorderLayout());
        jpConsumption.add(jpGrid, java.awt.BorderLayout.CENTER);

        jpRegistry.add(jpConsumption, java.awt.BorderLayout.NORTH);

        getContentPane().add(jpRegistry, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jlEntity;
    private javax.swing.JLabel jlSubentity;
    private javax.swing.JPanel jpConsumption;
    private javax.swing.JPanel jpEntity;
    private javax.swing.JPanel jpGrid;
    private javax.swing.JPanel jpRegistry;
    private sa.lib.gui.bean.SBeanFieldText moTextEntity;
    private sa.lib.gui.bean.SBeanFieldText moTextSubentity;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 880, 550);
        
        moTextEntity.setTextSettings("Centro", 250);
        moTextSubentity.setTextSettings("Subcentro", 250);
        
        moGridCostCenter = new SGridPaneForm(miClient, SModConsts.TRN_MAT_CONS_SUBENT_CC, 0, "Centros de costo") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> columns = new ArrayList<>();

                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Clave centro de costo"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Centro de costo"));
                SGridColumnForm col = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_M, "Seleccionado");
                col.setEditable(true);
                columns.add(col);

                return columns;
            }
        };

        jpGrid.add(moGridCostCenter);
        mvFormGrids.add(moGridCostCenter);
        
        jpCommandRight.remove(jbEdit);
        jpCommandRight.remove(jbReadInfo);
    }
    
    private void readCostCenters() {
        try {
            Statement statement = miClient.getSession().getDatabase().getConnection().createStatement();
            String sql = "SELECT pk_cc, id_cc FROM fin_cc WHERE (lev = (SELECT MAX(lev) FROM fin_cc) AND NOT b_del) OR pk_cc = 1 ORDER BY id_cc";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                boolean found = false;
                for (SDbMaterialConsumptionSubentityCostCenter ee : maMatConsumptionSubCC) {
                    if (resultSet.getInt("pk_cc") == ee.getPkCostCenterId()) {
                        found = true;
                        break;
                    }
                }
                SRowMatCostCenter row = new SRowMatCostCenter();
                row.readDataCostCenter(resultSet.getString("id_cc"), miClient.getSession());
                row.setSelected(found);
                maMatCostCenter.add(row);
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void populateGrid() {
        Vector<SGridRow> vRows = new Vector<>();
        if (maMatCostCenter.size() > 0) {
            vRows.addAll(maMatCostCenter);
        }
        moGridCostCenter.populateGrid(vRows);
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
        
        maMatCostCenter = new ArrayList<>();
        maMatConsumptionSubCC = new ArrayList<>();
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbConfMatConsSubentityVsCostCenter) registry;
        
        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;
        
        removeAllListeners();
        reloadCatalogues();
        
        moTextEntity.setValue(moRegistry.getAuxMatConsumptionSubentity().getXtaConsumptionEntityName());
        moTextSubentity.setValue(moRegistry.getAuxMatConsumptionSubentity().getName());
        
        for (SDbMaterialConsumptionSubentityCostCenter ee : moRegistry.getConsSubCC()) {
            maMatConsumptionSubCC.add(ee);
        }
        
        readCostCenters();
        
        populateGrid();
        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbConfMatConsSubentityVsCostCenter registry = moRegistry.clone();
        
        if (registry.isRegistryNew()) { }
        
        registry.getConsSubCC().clear();
        for (SRowMatCostCenter cc : maMatCostCenter) {
            if (cc.getIsSelected()) {
                SDbMaterialConsumptionSubentityCostCenter mcscc = new SDbMaterialConsumptionSubentityCostCenter();
                mcscc.setPkCostCenterId(cc.getRowPrimaryKey()[0]);
                registry.getConsSubCC().add(mcscc);
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

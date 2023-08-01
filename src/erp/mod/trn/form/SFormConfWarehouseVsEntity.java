/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.form;

import erp.mod.SModConsts;
import erp.mod.trn.db.SDbConfWarehouseVsEntity;
import erp.mod.trn.db.SDbMaterialProvisionEntity;
import erp.mod.trn.db.SDbMaterialProvisionEntityWarehouse;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
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
public class SFormConfWarehouseVsEntity extends SBeanForm implements ActionListener {
    
    private SDbConfWarehouseVsEntity moRegistry;
    
    private SGridPaneForm moGridMatProvEnt;
    private SGridPaneForm moGridMatProvEntSelected;
    
    private ArrayList<SDbMaterialProvisionEntity> maMatProvEnt;
    private ArrayList<SDbMaterialProvisionEntityWarehouse> maMatProvEntSelected;
    

    /**
     * Creates new form SFormConfEmployeeVsEntity
     * @param client
     * @param title
     */
    public SFormConfWarehouseVsEntity(SGuiClient client, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.TRNX_CONF_WHS_VS_ENT, 0, title);
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
        jpProvision = new javax.swing.JPanel();
        jpWarehouse = new javax.swing.JPanel();
        moTextWarehouse = new sa.lib.gui.bean.SBeanFieldText();
        jpProvAvailable = new javax.swing.JPanel();
        jpProvAvailableLabel = new javax.swing.JPanel();
        jlProvAvailable = new javax.swing.JLabel();
        jpGridProvAva = new javax.swing.JPanel();
        jpProvButtons = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jbProvAdd = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jbProvAddAll = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jbProvRemove = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jbProvRemoveAll = new javax.swing.JButton();
        jpProvSelected = new javax.swing.JPanel();
        jpProvSelectedLabel = new javax.swing.JPanel();
        jlProvSelected = new javax.swing.JLabel();
        jpGridProvSel = new javax.swing.JPanel();

        jpRegistry.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpRegistry.setLayout(new java.awt.BorderLayout());

        jpProvision.setLayout(new java.awt.BorderLayout());

        jpWarehouse.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        moTextWarehouse.setEditable(false);
        moTextWarehouse.setEnabled(false);
        moTextWarehouse.setPreferredSize(new java.awt.Dimension(400, 23));
        jpWarehouse.add(moTextWarehouse);

        jpProvision.add(jpWarehouse, java.awt.BorderLayout.NORTH);

        jpProvAvailable.setLayout(new java.awt.BorderLayout());

        jpProvAvailableLabel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlProvAvailable.setText("Entidades de suministro disponibles:");
        jpProvAvailableLabel.add(jlProvAvailable);

        jpProvAvailable.add(jpProvAvailableLabel, java.awt.BorderLayout.NORTH);

        jpGridProvAva.setPreferredSize(new java.awt.Dimension(350, 330));
        jpGridProvAva.setLayout(new java.awt.BorderLayout());
        jpProvAvailable.add(jpGridProvAva, java.awt.BorderLayout.CENTER);

        jpProvButtons.setLayout(new java.awt.GridLayout(10, 0));
        jpProvButtons.add(jPanel1);

        jbProvAdd.setText(">");
        jbProvAdd.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel2.add(jbProvAdd);

        jpProvButtons.add(jPanel2);

        jbProvAddAll.setText(">>");
        jbProvAddAll.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel3.add(jbProvAddAll);

        jpProvButtons.add(jPanel3);

        jbProvRemove.setText("<");
        jbProvRemove.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel4.add(jbProvRemove);

        jpProvButtons.add(jPanel4);

        jbProvRemoveAll.setText("<<");
        jbProvRemoveAll.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel5.add(jbProvRemoveAll);

        jpProvButtons.add(jPanel5);

        jpProvAvailable.add(jpProvButtons, java.awt.BorderLayout.EAST);

        jpProvision.add(jpProvAvailable, java.awt.BorderLayout.CENTER);

        jpProvSelected.setLayout(new java.awt.BorderLayout());

        jpProvSelectedLabel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlProvSelected.setText("Entidades de suministro seleccionadas:");
        jlProvSelected.setPreferredSize(new java.awt.Dimension(370, 16));
        jpProvSelectedLabel.add(jlProvSelected);

        jpProvSelected.add(jpProvSelectedLabel, java.awt.BorderLayout.NORTH);

        jpGridProvSel.setPreferredSize(new java.awt.Dimension(380, 330));
        jpGridProvSel.setLayout(new java.awt.BorderLayout());
        jpProvSelected.add(jpGridProvSel, java.awt.BorderLayout.CENTER);

        jpProvision.add(jpProvSelected, java.awt.BorderLayout.EAST);

        jpRegistry.add(jpProvision, java.awt.BorderLayout.NORTH);

        getContentPane().add(jpRegistry, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton jbProvAdd;
    private javax.swing.JButton jbProvAddAll;
    private javax.swing.JButton jbProvRemove;
    private javax.swing.JButton jbProvRemoveAll;
    private javax.swing.JLabel jlProvAvailable;
    private javax.swing.JLabel jlProvSelected;
    private javax.swing.JPanel jpGridProvAva;
    private javax.swing.JPanel jpGridProvSel;
    private javax.swing.JPanel jpProvAvailable;
    private javax.swing.JPanel jpProvAvailableLabel;
    private javax.swing.JPanel jpProvButtons;
    private javax.swing.JPanel jpProvSelected;
    private javax.swing.JPanel jpProvSelectedLabel;
    private javax.swing.JPanel jpProvision;
    private javax.swing.JPanel jpRegistry;
    private javax.swing.JPanel jpWarehouse;
    private sa.lib.gui.bean.SBeanFieldText moTextWarehouse;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 800, 500);
        
        moTextWarehouse.setTextSettings("Almacén", 250);
        
        // Entidades de consumo disponibles
        
        moGridMatProvEnt = new SGridPaneForm(miClient, SModConsts.TRN_MAT_PROV_ENT, 0, "Entidades de suministro") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> columns = new ArrayList<>();

                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Entidad de suministro"));

                return columns;
            }
        };

        jpGridProvAva.add(moGridMatProvEnt);
        mvFormGrids.add(moGridMatProvEnt);
        
        // Entidades de consumo seleccionadas
        
        moGridMatProvEntSelected = new SGridPaneForm(miClient, SModConsts.TRN_MAT_PROV_ENT_WHS, 0, "Entidades de suministro seleccionadas") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> columns = new ArrayList<>();

                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Entidad de suministro"));
                //SGridColumnForm col = new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_M, "Predeterminado");
                //col.setEditable(true);
                //columns.add(col);

                return columns;
            }
        };

        jpGridProvSel.add(moGridMatProvEntSelected);
        mvFormGrids.add(moGridMatProvEntSelected);
        
        jpCommandRight.remove(jbEdit);
        jpCommandRight.remove(jbReadInfo);
    }
    
    private void readMaterialProvisionEntities() {
        try {
            Statement statement = miClient.getSession().getDatabase().getConnection().createStatement();
            String sql = "SELECT id_mat_prov_ent FROM trn_mat_prov_ent";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                boolean found = false;
                for (SDbMaterialProvisionEntityWarehouse ew : maMatProvEntSelected) {
                    if (resultSet.getInt(1) == ew.getPkMatProvisionEntityId()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    SDbMaterialProvisionEntity pe = new SDbMaterialProvisionEntity();
                    pe.read(miClient.getSession(), new int[] { resultSet.getInt(1) });
                    maMatProvEnt.add(pe);
                }
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void populateGridMatProvEnt() {
        Vector<SGridRow> vRows = new Vector<>();
        if (maMatProvEnt.size() > 0) {
            vRows.addAll(maMatProvEnt);
        }
        moGridMatProvEnt.populateGrid(vRows);
    }
    
    private void populateGridMatProvEntSelected() {
        Vector<SGridRow> vRows = new Vector<>();
        if (maMatProvEntSelected.size() > 0) {
            vRows.addAll(maMatProvEntSelected);
        }
        moGridMatProvEntSelected.populateGrid(vRows);
    }
    
    private void actionProvAdd() {
        try {
            int provId = ((SDbMaterialProvisionEntity) moGridMatProvEnt.getSelectedGridRow()).getPkMatProvisionEntityId();
            if (maMatProvEnt.remove((SDbMaterialProvisionEntity) moGridMatProvEnt.getSelectedGridRow()) ) {
                SDbMaterialProvisionEntityWarehouse ew = new SDbMaterialProvisionEntityWarehouse();
                ew.setPkMatProvisionEntityId(provId);
                ew.readAuxMatProvEnt(miClient.getSession());
                maMatProvEntSelected.add(ew);

                populateGridMatProvEnt();
                populateGridMatProvEntSelected();
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }

    private void actionProvAddAll() {
        try {
            for (SDbMaterialProvisionEntity pe : maMatProvEnt) {
                SDbMaterialProvisionEntityWarehouse ew = new SDbMaterialProvisionEntityWarehouse();
                ew.setPkMatProvisionEntityId(pe.getPkMatProvisionEntityId());
                ew.readAuxMatProvEnt(miClient.getSession());
                maMatProvEntSelected.add(ew);
            }
            maMatProvEnt.clear();
            populateGridMatProvEnt();
            populateGridMatProvEntSelected();
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionProvRemove() {
        maMatProvEnt.add(((SDbMaterialProvisionEntityWarehouse) moGridMatProvEntSelected.getSelectedGridRow()).getAuxMatProvEnt());
        if (maMatProvEntSelected.remove(((SDbMaterialProvisionEntityWarehouse) moGridMatProvEntSelected.getSelectedGridRow()))) {
            populateGridMatProvEnt();
            populateGridMatProvEntSelected();
        }
    }

    private void actionProvRemoveAll() {
        for (SDbMaterialProvisionEntityWarehouse ew : maMatProvEntSelected) {
            maMatProvEnt.add(ew.getAuxMatProvEnt());
        }
        maMatProvEntSelected.clear();
        populateGridMatProvEnt();
        populateGridMatProvEntSelected();
    }
    
    @Override
    public void addAllListeners() {
        jbProvAdd.addActionListener(this);
        jbProvAddAll.addActionListener(this);
        jbProvRemove.addActionListener(this);
        jbProvRemoveAll.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbProvAdd.removeActionListener(this);
        jbProvAddAll.removeActionListener(this);
        jbProvRemove.removeActionListener(this);
        jbProvRemoveAll.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {
        moTextWarehouse.setValue("");
        
        maMatProvEnt = new ArrayList<>();
        maMatProvEntSelected = new ArrayList<>();
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbConfWarehouseVsEntity) registry;
        
        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;
        
        removeAllListeners();
        reloadCatalogues();
        
        moTextWarehouse.setValue(moRegistry.getAuxCompBrEnt().getEntity());
        
        for (SDbMaterialProvisionEntityWarehouse ew : moRegistry.getProvEntWhs()) {
            maMatProvEntSelected.add(ew);
        }
        
        readMaterialProvisionEntities();
        
        populateGridMatProvEnt();
        populateGridMatProvEntSelected();
        
        addAllListeners();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbConfWarehouseVsEntity registry = moRegistry.clone();
        
        if (registry.isRegistryNew()) { }
        
        registry.getProvEntWhs().clear();
        for (SDbMaterialProvisionEntityWarehouse ew : maMatProvEntSelected) {
            registry.getProvEntWhs().add(ew);
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
            
            if (button == jbProvAdd) {
                actionProvAdd();
            }
            else if (button == jbProvAddAll) {
                actionProvAddAll();
            }
            else if (button == jbProvRemove) {
                actionProvRemove();
            }
            else if (button == jbProvRemoveAll) {
                actionProvRemoveAll();
            }
        }
    }
}

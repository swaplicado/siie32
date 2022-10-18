/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.form;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mod.SModConsts;
import erp.mod.log.db.SDbBillOfLading;
import erp.mod.log.db.SDbBolLocation;
import erp.mod.log.db.SDbBolMerchandiseQuantity;
import erp.mod.log.db.SRowBillOfLading;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.grid.SGridPaneFormOwner;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiFieldKeyGroup;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanForm;

/**
 *
 * @author Isabel Servín
 */
public class SFormBolLocation extends SBeanForm implements SGridPaneFormOwner, ActionListener, ItemListener {
    
    public static final int FIRST_LOCATION_PARAMETER = 1;
    
    public static final int MERCHANDISE_DISCHARGED = 1;
    public static final int MERCHANDISE_CHARGED = 2;
    public static final int GRID_SUBTYPE_MERCHANDISE_CHARGED = 10;
    public static final int GRID_SUBTYPE_MERCHANDISE_DISCHARGED = 20;
    public static final int GRID_SUBTYPE_MERCHANDISE_PRECHARGE = 30;
    public static final int GRID_SUBTYPE_MERCHANDISE_CURRENTCHARGE = 40;
    
    private SDbBolLocation moRegistry;
    private SDbBillOfLading moBillOfLading;
    
    private SGridPaneForm moGridPrecharge;
    private SGridPaneForm moGridDischarge;
    private SGridPaneForm moGridCharge;
    private SGridPaneForm moGridCurrentCharge;
    private SGuiFieldKeyGroup moFieldKeyBizPartnerGroup;
    
    private SFormBolMerchandise moFormMerchandiseCharged;
    private SFormBolMerchandise moFormMerchandiseDischarged;
    
    private int[] moLocationStartKey;

    /**
     * Creates new form SFormBolLocation
     * @param client
     * @param bol
     * @param title
     */
    public SFormBolLocation(SGuiClient client, SDbBillOfLading bol, String title) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.LOG_BOL_LOCATION, SLibConstants.UNDEFINED, title);
        moBillOfLading = bol;
        initComponents();
        initComponentsCustom();
    }
    
    private void updateMerchandises(SGridRow gridRow) {
        SDbBolMerchandiseQuantity merchQty = (SDbBolMerchandiseQuantity) gridRow;
        if(merchQty.getParentMerchandise().getChildBolMerchandiseQuantities().size() <= 0){
            merchQty.getParentMerchandise().getChildBolMerchandiseQuantities().add(merchQty);
        }
        else {
            merchQty.getParentMerchandise().getChildBolMerchandiseQuantities().set(0, merchQty);
        }
        moBillOfLading.addMerchandise(merchQty.getParentMerchandise());
        updateCurrentChargeData((SDbBolMerchandiseQuantity)gridRow);
        populateCurrentCharge();
    }
    
    public void deleteMerchandises(SGridRow gridRow) {
        SDbBolMerchandiseQuantity merchQty = (SDbBolMerchandiseQuantity) gridRow;
        moBillOfLading.removeMerchandise(merchQty.getParentMerchandise());
        populateCurrentCharge();
    }
    
    private void actionItemStateChangeKeyCustomerBranchAddress() {
        try {
            
            // Calcular la distancia:
            if (moLocationStartKey != null) {
                String sql = "SELECT distance FROM log_dist_location "
                        + "WHERE (id_bpb_add_1 = " + moLocationStartKey[0] + " AND id_add_add_1 = " + moLocationStartKey[1] + " "
                        + "AND id_bpb_add_2 = " + moKeyBizPartnerBranchAddress.getValue()[0] + " AND id_add_add_2 = " + moKeyBizPartnerBranchAddress.getValue()[1] + ") OR ("
                        + "id_bpb_add_1 = " + moKeyBizPartnerBranchAddress.getValue()[0] + " AND id_add_add_1 = " + moKeyBizPartnerBranchAddress.getValue()[1] + " "
                        + "AND id_bpb_add_2 = " + moLocationStartKey[0] + " AND id_add_add_2 = " + moLocationStartKey[1] + ")"; 
                ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
                if (resultSet.next()) {
                    moDecimalDistance.setValue(resultSet.getDouble(1));
                }
            }
            
            // Obtener las colonias a partir del CP:
            
            populateNeighborhoodComboBox(moKeyBizPartnerBranchAddress.getValue()[0], moKeyBizPartnerBranchAddress.getValue()[1]);
            
            // Asignar colonia si es que ya tiene una asignada previamente:
        
            String sql = "SELECT fk_nei_zip_code FROM erp.logu_bpb_add_nei "
                    + "WHERE id_bpb_add = " + moKeyBizPartnerBranchAddress.getValue()[0] + " "
                    + "AND id_add_add = " + moKeyBizPartnerBranchAddress.getValue()[1];
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                moKeyBizPartnerBranchNeighborhood.setValue(new int[] { resultSet.getInt(1) });
            }
            
        }
        catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    private void populateNeighborhoodComboBox(int bizPartnerAddress, int addressAddress) throws Exception {
        
        // Obtener las colonias a partir del CP:
        
        SDataBizPartnerBranchAddress add = new SDataBizPartnerBranchAddress();
        add.read(new int[] { bizPartnerAddress, addressAddress } , miClient.getSession().getStatement());
        
        SGuiParams params = new SGuiParams();
        int[] key = new int[] { SLibUtils.parseInt(add.getZipCode()) };
        params.setKey(key);
        miClient.getSession().populateCatalogue(moKeyBizPartnerBranchNeighborhood, SModConsts.LOCS_BOL_NEI_ZIP_CODE, SLibConsts.UNDEFINED, params);
            
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        bgLocationsType = new javax.swing.ButtonGroup();
        bgLocations = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        moRadioStart = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel25 = new javax.swing.JPanel();
        moRadioMedium = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel26 = new javax.swing.JPanel();
        moRadioEnd = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel22 = new javax.swing.JPanel();
        moRadioOrigin = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel24 = new javax.swing.JPanel();
        moRadioDestination = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jlBizPartner = new javax.swing.JLabel();
        moKeyBizPartner = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel28 = new javax.swing.JPanel();
        jlBizPartnerBranch = new javax.swing.JLabel();
        moKeyBizPartnerBranch = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel29 = new javax.swing.JPanel();
        jlBizPartnerBranchAddress = new javax.swing.JLabel();
        moKeyBizPartnerBranchAddress = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel33 = new javax.swing.JPanel();
        jlBizPartnerBranchNeighborhood = new javax.swing.JLabel();
        moKeyBizPartnerBranchNeighborhood = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel32 = new javax.swing.JPanel();
        jlDistance = new javax.swing.JLabel();
        moDecimalDistance = new sa.lib.gui.bean.SBeanFieldDecimal();
        jPanel6 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jlArrival = new javax.swing.JLabel();
        moDateArrival = new sa.lib.gui.bean.SBeanFieldDatetime();
        jPanel31 = new javax.swing.JPanel();
        jlDeparture = new javax.swing.JLabel();
        moDateDeparture = new sa.lib.gui.bean.SBeanFieldDatetime();
        jPanel4 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        moRadioMyLocations = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel36 = new javax.swing.JPanel();
        moRadioCustomerLocations = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel37 = new javax.swing.JPanel();
        moRadioSupplierLocations = new sa.lib.gui.bean.SBeanFieldRadio();
        jpPanel10 = new javax.swing.JPanel();
        jpPrecharge = new javax.swing.JPanel();
        jpDischarge = new javax.swing.JPanel();
        jpCharge = new javax.swing.JPanel();
        jpCurrentCharge = new javax.swing.JPanel();

        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de ubicación:"));
        jPanel23.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgLocations.add(moRadioStart);
        moRadioStart.setSelected(true);
        moRadioStart.setText("Inicial");
        moRadioStart.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel21.add(moRadioStart);

        jPanel23.add(jPanel21);

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgLocations.add(moRadioMedium);
        moRadioMedium.setText("Intermedio");
        moRadioMedium.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel25.add(moRadioMedium);

        jPanel23.add(jPanel25);

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgLocations.add(moRadioEnd);
        moRadioEnd.setText("Final");
        moRadioEnd.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel26.add(moRadioEnd);

        jPanel23.add(jPanel26);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moRadioOrigin.setSelected(true);
        moRadioOrigin.setText("Origen");
        moRadioOrigin.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel22.add(moRadioOrigin);

        jPanel23.add(jPanel22);

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moRadioDestination.setText("Destino");
        moRadioDestination.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel24.add(moRadioDestination);

        jPanel23.add(jPanel24);

        jPanel1.add(jPanel23, java.awt.BorderLayout.WEST);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Ubicación:"));
        jPanel2.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner.setText("Asoc. de negocios:*");
        jlBizPartner.setPreferredSize(new java.awt.Dimension(130, 23));
        jPanel27.add(jlBizPartner);

        moKeyBizPartner.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel27.add(moKeyBizPartner);

        jPanel2.add(jPanel27);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerBranch.setText("Sucursal:*");
        jlBizPartnerBranch.setPreferredSize(new java.awt.Dimension(130, 23));
        jPanel28.add(jlBizPartnerBranch);

        moKeyBizPartnerBranch.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel28.add(moKeyBizPartnerBranch);

        jPanel2.add(jPanel28);

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerBranchAddress.setText("Domicilio entrega:*");
        jlBizPartnerBranchAddress.setPreferredSize(new java.awt.Dimension(130, 23));
        jPanel29.add(jlBizPartnerBranchAddress);

        moKeyBizPartnerBranchAddress.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel29.add(moKeyBizPartnerBranchAddress);

        jPanel2.add(jPanel29);

        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerBranchNeighborhood.setText("Colonia:*");
        jlBizPartnerBranchNeighborhood.setPreferredSize(new java.awt.Dimension(130, 23));
        jPanel33.add(jlBizPartnerBranchNeighborhood);

        moKeyBizPartnerBranchNeighborhood.setEnabled(false);
        moKeyBizPartnerBranchNeighborhood.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel33.add(moKeyBizPartnerBranchNeighborhood);

        jPanel2.add(jPanel33);

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDistance.setText("Distancia recorrida:*");
        jlDistance.setPreferredSize(new java.awt.Dimension(130, 23));
        jPanel32.add(jlDistance);
        jPanel32.add(moDecimalDistance);

        jPanel2.add(jPanel32);

        jPanel5.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Fecha hora de llegada y salida:"));
        jPanel6.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlArrival.setText("Llegada:*");
        jlArrival.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel30.add(jlArrival);
        jPanel30.add(moDateArrival);

        jPanel6.add(jPanel30);

        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDeparture.setText("Salida:*");
        jlDeparture.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel31.add(jlDeparture);
        jPanel31.add(moDateDeparture);

        jPanel6.add(jPanel31);

        jPanel5.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Ubicaciones de: "));
        jPanel4.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jPanel35.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgLocationsType.add(moRadioMyLocations);
        moRadioMyLocations.setSelected(true);
        moRadioMyLocations.setText("Propias");
        moRadioMyLocations.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel35.add(moRadioMyLocations);

        jPanel4.add(jPanel35);

        jPanel36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgLocationsType.add(moRadioCustomerLocations);
        moRadioCustomerLocations.setText("Clientes");
        moRadioCustomerLocations.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel36.add(moRadioCustomerLocations);

        jPanel4.add(jPanel36);

        jPanel37.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgLocationsType.add(moRadioSupplierLocations);
        moRadioSupplierLocations.setText("Proveedores");
        moRadioSupplierLocations.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel37.add(moRadioSupplierLocations);

        jPanel4.add(jPanel37);

        jPanel1.add(jPanel4, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jpPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Partidas:"));
        jpPanel10.setLayout(new java.awt.GridLayout(1, 4));

        jpPrecharge.setBorder(javax.swing.BorderFactory.createTitledBorder("Carga previa:"));
        jpPrecharge.setLayout(new java.awt.BorderLayout());
        jpPanel10.add(jpPrecharge);

        jpDischarge.setBorder(javax.swing.BorderFactory.createTitledBorder("Descarga:"));
        jpDischarge.setLayout(new java.awt.BorderLayout());
        jpPanel10.add(jpDischarge);

        jpCharge.setBorder(javax.swing.BorderFactory.createTitledBorder("Carga:"));
        jpCharge.setLayout(new java.awt.BorderLayout());
        jpPanel10.add(jpCharge);

        jpCurrentCharge.setBorder(javax.swing.BorderFactory.createTitledBorder("Carga actual:"));
        jpCurrentCharge.setLayout(new java.awt.BorderLayout());
        jpPanel10.add(jpCurrentCharge);

        getContentPane().add(jpPanel10, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgLocations;
    private javax.swing.ButtonGroup bgLocationsType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel jlArrival;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlBizPartnerBranch;
    private javax.swing.JLabel jlBizPartnerBranchAddress;
    private javax.swing.JLabel jlBizPartnerBranchNeighborhood;
    private javax.swing.JLabel jlDeparture;
    private javax.swing.JLabel jlDistance;
    private javax.swing.JPanel jpCharge;
    private javax.swing.JPanel jpCurrentCharge;
    private javax.swing.JPanel jpDischarge;
    private javax.swing.JPanel jpPanel10;
    private javax.swing.JPanel jpPrecharge;
    private sa.lib.gui.bean.SBeanFieldDatetime moDateArrival;
    private sa.lib.gui.bean.SBeanFieldDatetime moDateDeparture;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecimalDistance;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBizPartner;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBizPartnerBranch;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBizPartnerBranchAddress;
    private sa.lib.gui.bean.SBeanFieldKey moKeyBizPartnerBranchNeighborhood;
    private sa.lib.gui.bean.SBeanFieldRadio moRadioCustomerLocations;
    private sa.lib.gui.bean.SBeanFieldRadio moRadioDestination;
    private sa.lib.gui.bean.SBeanFieldRadio moRadioEnd;
    private sa.lib.gui.bean.SBeanFieldRadio moRadioMedium;
    private sa.lib.gui.bean.SBeanFieldRadio moRadioMyLocations;
    private sa.lib.gui.bean.SBeanFieldRadio moRadioOrigin;
    private sa.lib.gui.bean.SBeanFieldRadio moRadioStart;
    private sa.lib.gui.bean.SBeanFieldRadio moRadioSupplierLocations;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 960, 600);

        moFieldKeyBizPartnerGroup = new SGuiFieldKeyGroup(miClient);
        
        moKeyBizPartner.setKeySettings(miClient, SGuiUtils.getLabelName(jlBizPartner), true);
        moKeyBizPartnerBranch.setKeySettings(miClient, SGuiUtils.getLabelName(jlBizPartnerBranch), true);
        moKeyBizPartnerBranchAddress.setKeySettings(miClient, SGuiUtils.getLabelName(jlBizPartnerBranchAddress), true);
        moKeyBizPartnerBranchNeighborhood.setKeySettings(miClient, SGuiUtils.getLabelName(jlBizPartnerBranchNeighborhood), false);
        moDecimalDistance.setDecimalSettings(SGuiUtils.getLabelName(jlDistance), SGuiConsts.GUI_TYPE_DEC_QTY, true);
        moDateArrival.setDateSettings(miClient, SGuiUtils.getLabelName(jlArrival), false);
        moDateDeparture.setDateSettings(miClient, SGuiUtils.getLabelName(jlDeparture), false);
        
        moFields.addField(moKeyBizPartner);
        moFields.addField(moKeyBizPartnerBranch);
        moFields.addField(moKeyBizPartnerBranchAddress);
        moFields.addField(moKeyBizPartnerBranchNeighborhood);
        moFields.addField(moDecimalDistance);
        moFields.addField(moDateArrival);
        moFields.addField(moDateDeparture);
        
        moFields.setFormButton(jbSave);

        moGridCharge = new SGridPaneForm(miClient, SModConsts.LOG_BOL_MERCH_QTY, GRID_SUBTYPE_MERCHANDISE_CHARGED, "Carga") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(true, true, true);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                int col = 0;
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();
                SGridColumnForm[] columns = new SGridColumnForm[3];

                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "Ítem");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_4D, "Cantidad");
                columns[col++] = new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad");
                
                gridColumnsForm.addAll(Arrays.asList((SGridColumnForm[]) columns));

                return gridColumnsForm;
            }
            
            @Override
            public void actionRowNew() {
                SGuiParams params = new SGuiParams();
                
                if (moKeyBizPartner.getSelectedIndex() <= 0) {
                    miClient.showMsgBoxInformation(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlBizPartner) + "'.");
                    moKeyBizPartner.requestFocus();
                }
                else if (moKeyBizPartnerBranch.getSelectedIndex() <= 0) {
                    miClient.showMsgBoxInformation(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlBizPartnerBranch) + "'.");
                    moKeyBizPartnerBranch.requestFocus();
                }
                else if (moKeyBizPartnerBranchAddress.getSelectedIndex() <= 0) {
                    miClient.showMsgBoxInformation(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlBizPartnerBranchAddress) + "'.");
                    moKeyBizPartnerBranchAddress.requestFocus();
                }
                else if (moKeyBizPartnerBranchNeighborhood.getComponent().getItemCount() > 1 && moKeyBizPartnerBranchNeighborhood.getSelectedIndex() <= 0) {
                    miClient.showMsgBoxInformation(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlBizPartnerBranchNeighborhood) + "'.");
                    moKeyBizPartnerBranchNeighborhood.requestFocus();
                }
                else {
                    params.getParamsMap().put(SModConsts.LOG_BOL_LOCATION, moKeyBizPartnerBranchAddress.getValue() );
                    params.getParamsMap().put(SModConsts.LOG_BOL, moBillOfLading);
                    params.getParamsMap().put(SFormBolMerchandise.ENABLE_DISABLE_LIST, true);
                    moFormParams = params;
                    super.actionRowNew();
                }
            }
            
            @Override
            public void actionRowEdit() {
                SGuiParams params = new SGuiParams();
                params.getParamsMap().put(SModConsts.LOG_BOL, moBillOfLading);
                params.getParamsMap().put(SFormBolMerchandise.ENABLE_DISABLE_LIST, false);
                moFormParams = params;
                super.actionRowEdit();
            }

            @Override
            public void actionRowDelete() {

                if (jtTable.getSelectedRowCount() != 1) {
                    miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
                }
                else {
                    SGridRow gridRow = moGridCharge.getSelectedGridRow(); //getGridRows().get(moGridStockDays.getTable().getSelectedRow());

                    if (gridRow.isRowSystem()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                    }
                    else if (!gridRow.isRowDeletable()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_DELETABLE);
                    }
                    else {
                        super.actionRowDelete();
                    }
                }
            }
        };
        
        moGridDischarge = new SGridPaneForm(miClient, SModConsts.LOG_BOL_MERCH_QTY, GRID_SUBTYPE_MERCHANDISE_DISCHARGED, "Descarga") {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(true, true, true);
            }
            
            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "Ítem"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_4D, "Cantidad"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad"));
                
                return gridColumnsForm;
            }
            
            @Override
            public void actionRowNew() {
                SGuiParams params = new SGuiParams();
                
                if (moKeyBizPartner.getSelectedIndex() <= 0) {
                    miClient.showMsgBoxInformation(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlBizPartner) + "'.");
                    moKeyBizPartner.requestFocus();
                }
                else if (moKeyBizPartnerBranch.getSelectedIndex() <= 0) {
                    miClient.showMsgBoxInformation(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlBizPartnerBranch) + "'.");
                    moKeyBizPartnerBranch.requestFocus();
                }
                else if (moKeyBizPartnerBranchAddress.getSelectedIndex() <= 0) {
                    miClient.showMsgBoxInformation(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlBizPartnerBranchAddress) + "'.");
                    moKeyBizPartnerBranchAddress.requestFocus();
                }
                else if (moKeyBizPartnerBranchNeighborhood.getComponent().getItemCount() > 1 && moKeyBizPartnerBranchNeighborhood.getSelectedIndex() <= 0) {
                    miClient.showMsgBoxInformation(SGuiConsts.ERR_MSG_FIELD_REQ + "'" + SGuiUtils.getLabelName(jlBizPartnerBranchNeighborhood) + "'.");
                    moKeyBizPartnerBranchNeighborhood.requestFocus();
                }
                else {
                    params.getParamsMap().put(SModConsts.LOG_BOL_LOCATION, moKeyBizPartnerBranchAddress.getValue() );
                    params.getParamsMap().put(SModConsts.LOG_BOL, moBillOfLading);
                    params.getParamsMap().put(SFormBolMerchandise.ENABLE_DISABLE_LIST, true);
                    moFormParams = params;
                    super.actionRowNew();
                }
            }
            
            @Override
            public void actionRowEdit() {
                SGuiParams params = new SGuiParams();
                params.getParamsMap().put(SModConsts.LOG_BOL, moBillOfLading);
                params.getParamsMap().put(SFormBolMerchandise.ENABLE_DISABLE_LIST, false);
                moFormParams = params;
                super.actionRowEdit();
            }
        };
        
        moGridPrecharge = new SGridPaneForm(miClient, SModConsts.LOG_BOL_MERCH_QTY, GRID_SUBTYPE_MERCHANDISE_PRECHARGE, "Carga previa") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false, false, false);
            }
            
            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "Ítem"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_4D, "Cantidad"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad"));
                
                return gridColumnsForm;
            }
        };
        
        moGridCurrentCharge = new SGridPaneForm(miClient, SModConsts.LOG_BOL_MERCH_QTY, GRID_SUBTYPE_MERCHANDISE_CURRENTCHARGE, "Carga actual") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false, false, false);
            }
            
            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> gridColumnsForm = new ArrayList<>();

                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "Ítem"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_4D, "Cantidad"));
                gridColumnsForm.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad"));
                
                return gridColumnsForm;
            }
        };
        
        moFormMerchandiseCharged = new SFormBolMerchandise(miClient, MERCHANDISE_CHARGED, "Carga");
        moGridCharge.setForm(moFormMerchandiseCharged);
        moGridCharge.setPaneFormOwner(this);
        
        jpCharge.add(moGridCharge, BorderLayout.CENTER);
        
        moFormMerchandiseDischarged = new SFormBolMerchandise(miClient, MERCHANDISE_DISCHARGED, "Descarga");
        moGridDischarge.setForm(moFormMerchandiseDischarged);
        moGridDischarge.setPaneFormOwner(this);
        
        jpDischarge.add(moGridDischarge, BorderLayout.CENTER);
        
        jpPrecharge.add(moGridPrecharge, BorderLayout.CENTER);
        
        jpCurrentCharge.add(moGridCurrentCharge, BorderLayout.CENTER);
        
    }
    
    /*
     * Private methods:
     */

    private void populateCharge() throws SQLException {
        try {
            Vector<SGridRow> vRows = new Vector<>();
            ArrayList<SDbBolMerchandiseQuantity> aRows = new ArrayList<>();
            for (SDbBolMerchandiseQuantity merch : moRegistry.getBolMerchandiseQuantityCharges()) {
                aRows.add(merch);
            }
            for (SDbBolMerchandiseQuantity row : aRows) {
                vRows.add(row);
            }
            moGridCharge.populateGrid(vRows);
            moGridCharge.clearSortKeys();
            moGridCharge.setSelectedGridRow(0);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
     
    private void populateDischarge() throws SQLException {
        try {
            Vector<SGridRow> vRows = new Vector<>();
            ArrayList<SDbBolMerchandiseQuantity> aRows = new ArrayList<>();
            for (SDbBolMerchandiseQuantity merch : moRegistry.getBolMerchandiseQuantityDischarges()) {
                aRows.add(merch);
            }
            for (SDbBolMerchandiseQuantity row : aRows) {
                vRows.add(row);
            }
            moGridDischarge.populateGrid(vRows);
            moGridDischarge.clearSortKeys();
            moGridDischarge.setSelectedGridRow(0);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void populatePrecharge() {
        try {
            Vector<SGridRow> vRows = new Vector<>();
            ArrayList<SRowBillOfLading> aRows = new ArrayList<>();
            for (SRowBillOfLading bol : moRegistry.getRowBolPrecharges()) {
                aRows.add(bol);
            }
            for (SRowBillOfLading row : aRows) {
                vRows.add(row);
            }
            moGridPrecharge.populateGrid(vRows);
            moGridPrecharge.clearSortKeys();
            moGridPrecharge.setSelectedGridRow(0);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void populateCurrentCharge() {
        try {
            moRegistry.inicializeRowsCurrentCharge(moRegistry.getRowBolPrecharges());
            for (SGridRow row : moGridCharge.getModel().getGridRows()){
                updateCurrentChargeData((SDbBolMerchandiseQuantity)row);
            }
            for (SGridRow row : moGridDischarge.getModel().getGridRows()){
                updateCurrentChargeData((SDbBolMerchandiseQuantity)row);
            }
            Vector<SGridRow> vRows = new Vector<>();
            ArrayList<SRowBillOfLading> aRows = new ArrayList<>();
            for (SRowBillOfLading bol : moRegistry.getRowBolCurrentCharges()) {
                aRows.add(bol);
            }
            for (SRowBillOfLading row : aRows) {
                vRows.add(row);
            }
            moGridCurrentCharge.clearGridRows();
            moGridCurrentCharge.populateGrid(vRows);
            moGridCurrentCharge.clearSortKeys();
            moGridCurrentCharge.setSelectedGridRow(0);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void updatePrechargedData(Vector<SGridRow> rows) {
        moRegistry.getRowBolPrecharges().clear();
        for (SGridRow row : rows) {
            SDbBolLocation bol = (SDbBolLocation) row;
            moRegistry.updateRowsPrecharged(bol);
            if (bol.getLocationType() == 1) { // Inicial
                moLocationStartKey = new int[] { bol.getFkOriginBizPartnerAddress_n(), bol.getFkOriginAddressAddress_n() };
            }
        }
    }
    
    private void updateCurrentChargeData(SDbBolMerchandiseQuantity qty) {
        moRegistry.updateRowsCurrentCharge(qty);
    }
    
    private void actionLocations() {
      if (moRadioMyLocations.isSelected()) {
            moKeyBizPartner.removeAllItems();
            miClient.getSession().populateCatalogue(moKeyBizPartner, SModConsts.BPSU_BP, SLibConsts.UNDEFINED, null);
            moKeyBizPartner.setValue(new int[] { miClient.getSession().getConfigCompany().getCompanyId() });
            moKeyBizPartner.setEnabled(false);
        }
        else if (moRadioCustomerLocations.isSelected()) {
            moKeyBizPartner.removeAllItems();
            miClient.getSession().populateCatalogue(moKeyBizPartner, SModConsts.BPSU_BP, SDataConstantsSys.BPSS_CT_BP_CUS, null);
            moKeyBizPartner.setEnabled(true);
        }
        else if (moRadioSupplierLocations.isSelected()) {
            moKeyBizPartner.removeAllItems();
            miClient.getSession().populateCatalogue(moKeyBizPartner, SModConsts.BPSU_BP, SDataConstantsSys.BPSS_CT_BP_SUP, null);
            moKeyBizPartner.setEnabled(true);
        }
    }
    
    private void actionOriginDestination() {
        moGridCharge.setEnabled(moRadioOrigin.isSelected());
        moGridDischarge.setEnabled(moRadioDestination.isSelected());
        moDecimalDistance.setEnabled(moRadioDestination.isSelected());
        moDateArrival.setEnabled(moRadioDestination.isSelected());
        moDateDeparture.setEnabled(moRadioOrigin.isSelected());
    }
    
    private void actionLocationType() {
        if (moRadioStart.isSelected()) {
            moRadioOrigin.setSelected(true);
            moRadioOrigin.setEnabled(false);
            moRadioDestination.setSelected(false);
            moRadioDestination.setEnabled(false);
            moDateArrival.setEnabled(false);
            moDateDeparture.setEnabled(true);
            moDecimalDistance.setValue(0.0);
            moDecimalDistance.setEnabled(false);
        }
        else if (moRadioMedium.isSelected()) {
            moRadioOrigin.setEnabled(true);
            moRadioDestination.setEnabled(true);
            moDateArrival.setEnabled(true);
            moDateDeparture.setEnabled(true);
            moDecimalDistance.setEnabled(true);
        }
        else if (moRadioEnd.isSelected()) {
            moRadioOrigin.setSelected(false);
            moRadioOrigin.setEnabled(false);
            moRadioDestination.setSelected(true);
            moRadioDestination.setEnabled(false);
            moDateArrival.setEnabled(true);
            moDateDeparture.setEnabled(false);
            moDecimalDistance.setEnabled(true);
        }
        actionOriginDestination();
    }
    
     /*
     * Public methods:
     */


    @Override
    public void addAllListeners() {
        moRadioStart.addItemListener(this);
        moRadioMedium.addItemListener(this);
        moRadioEnd.addItemListener(this);
        moRadioOrigin.addItemListener(this);
        moRadioDestination.addItemListener(this);
        moKeyBizPartner.addItemListener(this);
        moKeyBizPartnerBranch.addItemListener(this);
        moKeyBizPartnerBranchAddress.addItemListener(this);
        moRadioMyLocations.addItemListener(this);
        moRadioCustomerLocations.addItemListener(this);
        moRadioSupplierLocations.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        moRadioStart.removeItemListener(this);
        moRadioMedium.removeItemListener(this);
        moRadioEnd.removeItemListener(this);
        moRadioOrigin.removeItemListener(this);
        moRadioDestination.removeItemListener(this);
        moKeyBizPartner.removeItemListener(this);
        moKeyBizPartnerBranch.removeItemListener(this);
        moKeyBizPartnerBranchAddress.removeItemListener(this);
        moRadioMyLocations.removeItemListener(this);
        moRadioCustomerLocations.removeItemListener(this);
        moRadioSupplierLocations.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        try {
            moFieldKeyBizPartnerGroup.initGroup();
            moFieldKeyBizPartnerGroup.addFieldKey(moKeyBizPartner, SModConsts.BPSU_BP, SLibConsts.UNDEFINED, null);
            moFieldKeyBizPartnerGroup.addFieldKey(moKeyBizPartnerBranch, SModConsts.BPSU_BPB, SLibConsts.UNDEFINED, null);
            moFieldKeyBizPartnerGroup.addFieldKey(moKeyBizPartnerBranchAddress, SModConsts.BPSU_BPB_ADD, SLibConsts.UNDEFINED, null);
            moFieldKeyBizPartnerGroup.populateCatalogues();
            
            populateCharge();
            populateDischarge();
        } catch (SQLException e) {
            SLibUtils.showException(this, e);
        }
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbBolLocation) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();        

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
        }
        
        if (moRegistry.getFkOriginAddressAddress_n() == 0 && moRegistry.getFkOriginBizPartnerAddress_n() == 0) {
            populateNeighborhoodComboBox(moRegistry.getFkDestinationBizPartnerAddress_n(), moRegistry.getFkDestinationAddressAddress_n());
            moKeyBizPartner.setValue(new int[] { moRegistry.getFkDestinationBizPartner_n() });
            moKeyBizPartnerBranch.setValue(new int[] { moRegistry.getFkDestinationBizPartnerAddress_n() });
            moKeyBizPartnerBranchAddress.setValue(new int[] { moRegistry.getFkDestinationBizPartnerAddress_n(), moRegistry.getFkDestinationAddressAddress_n() });
            moKeyBizPartnerBranchNeighborhood.setValue(new int[] { moRegistry.getFkDestinationNeighborhoodZipCode_n() });
            moRadioOrigin.setSelected(false);
            moRadioDestination.setSelected(true);
        }
        else {
            if (moRegistry.getFkDestinationAddressAddress_n()!= 0 && moRegistry.getFkDestinationBizPartnerAddress_n() != 0) {
                moRadioDestination.setSelected(true);
            }
            else {
                moRadioDestination.setSelected(false);
            }
            populateNeighborhoodComboBox(moRegistry.getFkOriginBizPartnerAddress_n(), moRegistry.getFkOriginAddressAddress_n());
            moKeyBizPartner.setValue(new int[] { moRegistry.getFkOriginBizPartner_n()});
            moKeyBizPartnerBranch.setValue(new int[] { moRegistry.getFkOriginBizPartnerAddress_n()});
            moKeyBizPartnerBranchAddress.setValue(new int[] { moRegistry.getFkOriginBizPartnerAddress_n(), moRegistry.getFkOriginAddressAddress_n() });
            moKeyBizPartnerBranchNeighborhood.setValue(new int[] { moRegistry.getFkOriginNeighborhoodZipCode_n() });
            moRadioOrigin.setSelected(true);
        }

        switch (moRegistry.getLocationType()) {
            case 1: moRadioStart.setSelected(true); break;
            case 2: moRadioMedium.setSelected(true); break;
            case 3: moRadioEnd.setSelected(true); break;
            default: moRadioStart.setSelected(true); break;
        }
        
        moDecimalDistance.setValue(moRegistry.getDistance()); 
        
        setFormEditable(true);
        if (moRegistry.isRegistryNew()) {
            moDateArrival.setValue(miClient.getSession().getCurrentDate());
            moDateDeparture.setValue(miClient.getSession().getCurrentDate());
        }
        else {
            if (moRegistry.getDateArrival_n() != null) {
                moDateArrival.setValue(moRegistry.getDateArrival_n());
            }
            if (moRegistry.getDateDeparture_n() != null) {
                moDateDeparture.setValue(moRegistry.getDateDeparture_n());
            }
        }
        
        addAllListeners();
        actionLocationType();
        actionOriginDestination();
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbBolLocation registry =(SDbBolLocation) moRegistry.clone();
        
        if (registry.isRegistryNew()) {}
        SDataBizPartner bp = new SDataBizPartner();
        bp.read(moKeyBizPartner.getValue(), miClient.getSession().getStatement());
        SDataBizPartnerBranch bpb = new SDataBizPartnerBranch();
        bpb.read(moKeyBizPartnerBranch.getValue(), miClient.getSession().getStatement());
        SDataBizPartnerBranchAddress bpba = new SDataBizPartnerBranchAddress();
        bpba.read(moKeyBizPartnerBranchAddress.getValue(), miClient.getSession().getStatement());
        
        registry.setDistance(moDecimalDistance.isEnabled() ? moDecimalDistance.getValue() : 0.0);
        registry.setDateDeparture_n(moDateDeparture.isEnabled() ? moDateDeparture.getValue() : null);
        registry.setDateArrival_n(moDateArrival.isEnabled() ? moDateArrival.getValue() : null);
        registry.setLocationType(moRadioStart.isSelected() ? 1 : moRadioMedium.isSelected() ? 2 : moRadioEnd.isSelected() ? 3 : 0);
        registry.setXtaLocationType(moRadioStart.isSelected() ? "Inicial" : moRadioMedium.isSelected() ? "Intermedia" : moRadioEnd.isSelected() ? "Final" : ""); 
        registry.setDataBizPartner(bp);
        registry.setDataBizPartnerBranch(bpb);
        registry.setDataBizPartnerBranchAddress(bpba);
        if (moRadioOrigin.isSelected()) {
            registry.setFkOriginBizPartner_n(moKeyBizPartner.getValue()[0]);
            registry.setFkOriginBizPartnerAddress_n(moKeyBizPartnerBranch.getValue()[0]);
            registry.setFkOriginAddressAddress_n(moKeyBizPartnerBranchAddress.getValue()[1]);
            registry.setFkOriginNeighborhoodZipCode_n(moKeyBizPartnerBranchNeighborhood.getValue()[0]);
            registry.setXtaIsOrigin(true);
        }
        else {
            registry.setFkOriginBizPartner_n(0);
            registry.setFkOriginBizPartnerAddress_n(0);
            registry.setFkOriginAddressAddress_n(0);
            registry.setXtaIsOrigin(false);
        }
        if (moRadioDestination.isSelected()) {
            registry.setFkDestinationBizPartner_n(moKeyBizPartner.getValue()[0]);
            registry.setFkDestinationBizPartnerAddress_n(moKeyBizPartnerBranch.getValue()[0]);
            registry.setFkDestinationAddressAddress_n(moKeyBizPartnerBranchAddress.getValue()[1]);
            registry.setFkDestinationNeighborhoodZipCode_n(moKeyBizPartnerBranchNeighborhood.getValue()[0]);
            registry.setXtaIsDestination(true);
        }
        else {
            registry.setFkDestinationBizPartner_n(0);
            registry.setFkDestinationBizPartnerAddress_n(0);
            registry.setFkDestinationAddressAddress_n(0);
            registry.setXtaIsDestination(false);
        }
        
        registry.getBolMerchandiseQuantityCharges().clear();
        for (SGridRow row : moGridCharge.getModel().getGridRows()) {
            registry.getBolMerchandiseQuantityCharges().add((SDbBolMerchandiseQuantity) row);
        }
        
        registry.getBolMerchandiseQuantityDischarges().clear();
        for (SGridRow row : moGridDischarge.getModel().getGridRows()) {
            registry.getBolMerchandiseQuantityDischarges().add((SDbBolMerchandiseQuantity) row);
        }
        
        return registry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(int type, java.lang.Object value) {

       switch (type) {
            case SModConsts.BPSU_BP:
                moKeyBizPartner.setValue((int[]) value);
                break;
            case SModConsts.LOGX_LOCATION_TP:
                switch((int) value){
                    case 1: moRadioMyLocations.setSelected(true); break;
                    case 2: moRadioCustomerLocations.setSelected(true); break;
                    case 3: moRadioSupplierLocations.setSelected(true); break;
                }
                actionLocations();
                break;
            case FIRST_LOCATION_PARAMETER:
                if ((boolean) value) {
                    moRadioStart.setSelected(true);
                    moRadioStart.setEnabled(false);
                    moRadioMedium.setEnabled(false);
                    moRadioEnd.setEnabled(false);
                    actionLocationType();
                }
                else {
                    moRadioStart.setSelected(true);
                    moRadioStart.setEnabled(true);
                    moRadioMedium.setEnabled(true);
                    moRadioEnd.setEnabled(true);
                    actionLocationType();
                }
            break;
            case SModConsts.LOG_BOL:
                moBillOfLading = (SDbBillOfLading) value;
                break;
            case SModConsts.LOG_BOL_LOCATION:
                Vector<SGridRow> rows = (Vector<SGridRow>) value;
                updatePrechargedData(rows);
                populatePrecharge();
                populateCurrentCharge();
            default:
        }
    }

    @Override
    public Object getValue(int type) {
        Object value = null;
        return value;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        if (moRadioOrigin.isSelected() && moGridCharge.getModel().getRowCount() <= 0) {
            validation.setMessage("El tipo de ubicación está marcado como origen, debe existir al menos un elemento de carga");
        }
        else if (!moRadioOrigin.isSelected() && moGridCharge.getModel().getRowCount() > 0) {
            validation.setMessage("El tipo de ubicación no está marcado como origen, no deben existir elementos de carga");
        }
        
        if (moRadioDestination.isSelected() && moGridDischarge.getModel().getRowCount() <= 0) {
            validation.setMessage("El tipo de ubicación está marcado como destino, debe existir al menos un elemento de descarga");
        }
        else if (!moRadioDestination.isSelected() && moGridDischarge.getModel().getRowCount() > 0) {
            validation.setMessage("El tipo de ubicación no está marcado como destino, no deben existir elementos de descarga");
        }
        
        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();            
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
       if (e.getSource() instanceof javax.swing.JComboBox && e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox comboBox = (JComboBox)  e.getSource();
            if (comboBox == moKeyBizPartner) {
            }
            else if (comboBox == moKeyBizPartnerBranch) {
            }
            else if (comboBox == moKeyBizPartnerBranchAddress) {
                actionItemStateChangeKeyCustomerBranchAddress();
            }
         }
       
       if (e.getSource() instanceof javax.swing.JRadioButton) {
            JRadioButton radioButton = (JRadioButton) e.getSource();
            if (radioButton == moRadioStart) {
                actionLocationType();
            }
            else if (radioButton == moRadioMedium) {
                actionLocationType();
            }
            else if (radioButton == moRadioEnd) {
                actionLocationType();
            }
            else if (radioButton == moRadioMyLocations) {
                actionLocations();
            }
            else if (radioButton == moRadioOrigin){
                actionOriginDestination();
            }
            else if (radioButton == moRadioDestination){
                actionOriginDestination();
            }
            else if (radioButton == moRadioCustomerLocations) {
                actionLocations();
            }
            else if (radioButton == moRadioSupplierLocations) {
                actionLocations();
            }
        }
    }

    @Override
    public void actionCancel() {
        if (jbCancel.isEnabled()) {
            mnFormResult = SGuiConsts.FORM_RESULT_CANCEL;
            dispose();
        }
    }

    @Override
    public void notifyRowNew(int gridType, int gridSubtype, int row, SGridRow gridRow) {
        updateMerchandises(gridRow);
    }

    @Override
    public void notifyRowEdit(int gridType, int gridSubtype, int row, SGridRow gridRow) {
        updateMerchandises(gridRow);
    }

    @Override
    public void notifyRowDelete(int gridType, int gridSubtype, int row, SGridRow gridRow) {
        deleteMerchandises(gridRow);
    }
}

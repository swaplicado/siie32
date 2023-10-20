/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.form;

import erp.mod.SModConsts;
import erp.mod.trn.db.SRowMaterialRequestItemSupply;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFormDialog;

/**
 *
 * @author Isabel Servin
 */
public class SDialogMaterialRequestItemSupply extends SBeanFormDialog implements ActionListener, ItemListener {
    
    public static int PARAM_CONS_ENT_ID = 1;
    public static int PARAM_WAH_ID = 2;
    public static int PARAM_ARR_ITM_SELECTED = 3;
    
    private boolean mbIsCaptureMode;
    
    private SGridPaneForm moGridItems;
    private SGridPaneForm moGridItemsSelected;
    
    private ArrayList<SRowMaterialRequestItemSupply> maAllItems;
    private ArrayList<SRowMaterialRequestItemSupply> maGridItems;
    private ArrayList<SRowMaterialRequestItemSupply> maGridItemsSelected;
    
    private int mnConsEntId;
    private int[] moWahId;
    
    /**
     * Creates new form SDialogMaterialRequestItemSupply
     * @param client
     * @param title
     */
    public SDialogMaterialRequestItemSupply(SGuiClient client, String title) { //TRNX_MAT_REQ_ITM_SUP
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.TRNX_MAT_REQ_ITM_SUP, SLibConsts.UNDEFINED, title);
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

        jbgItems = new javax.swing.ButtonGroup();
        jpContent = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlWhs = new javax.swing.JLabel();
        moKeyWhs = new sa.lib.gui.bean.SBeanFieldKey();
        jbContinue = new javax.swing.JButton();
        jbRestart = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jIGen = new javax.swing.JLabel();
        moKeyIGen = new sa.lib.gui.bean.SBeanFieldKey();
        jbShow = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jrbDownMin = new javax.swing.JRadioButton();
        jrbUpMin = new javax.swing.JRadioButton();
        jrbDownPo = new javax.swing.JRadioButton();
        jrbUpPo = new javax.swing.JRadioButton();
        jrbDownMax = new javax.swing.JRadioButton();
        jrbUpMax = new javax.swing.JRadioButton();
        jrbAllItems = new javax.swing.JRadioButton();
        jpGridContainer = new javax.swing.JPanel();
        jpGridAllItems = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jbAdd = new javax.swing.JButton();
        jbAddAll = new javax.swing.JButton();
        jpGridItemSelected = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jbRemove = new javax.swing.JButton();
        jbRemoveAll = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jbMinSupply = new javax.swing.JButton();
        jbPoSupply = new javax.swing.JButton();
        jbMaxSupply = new javax.swing.JButton();
        moDecQtySup = new sa.lib.gui.bean.SBeanFieldDecimal();
        jbFreeSupply = new javax.swing.JButton();
        jbErase = new javax.swing.JButton();
        jpControls = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jpContent.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout(3, 0));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        jlWhs.setText("Almacén:*");
        jlWhs.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel2.add(jlWhs);

        moKeyWhs.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel2.add(moKeyWhs);

        jbContinue.setText("Continuar");
        jbContinue.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel2.add(jbContinue);

        jbRestart.setText("Reiniciar");
        jbRestart.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel2.add(jbRestart);

        jPanel1.add(jPanel2);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jIGen.setText("Ítem genérico:");
        jIGen.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jIGen);

        moKeyIGen.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel3.add(moKeyIGen);

        jbShow.setText("Mostrar");
        jbShow.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel3.add(jbShow);

        jPanel1.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbgItems.add(jrbDownMin);
        jrbDownMin.setText("<= mínimo");
        jrbDownMin.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel4.add(jrbDownMin);

        jbgItems.add(jrbUpMin);
        jrbUpMin.setText("> mínimo");
        jrbUpMin.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel4.add(jrbUpMin);

        jbgItems.add(jrbDownPo);
        jrbDownPo.setText("<= pto. reorden");
        jrbDownPo.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel4.add(jrbDownPo);

        jbgItems.add(jrbUpPo);
        jrbUpPo.setText("> pto. reorden");
        jrbUpPo.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel4.add(jrbUpPo);

        jbgItems.add(jrbDownMax);
        jrbDownMax.setText("<= máximo");
        jrbDownMax.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel4.add(jrbDownMax);

        jbgItems.add(jrbUpMax);
        jrbUpMax.setText("> máximo");
        jrbUpMax.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel4.add(jrbUpMax);

        jbgItems.add(jrbAllItems);
        jrbAllItems.setText("Todo");
        jrbAllItems.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel4.add(jrbAllItems);

        jPanel1.add(jPanel4);

        jpContent.add(jPanel1, java.awt.BorderLayout.NORTH);

        jpGridContainer.setLayout(new java.awt.BorderLayout());

        jpGridAllItems.setBorder(javax.swing.BorderFactory.createTitledBorder("Ítems disponibles para solicitar"));
        jpGridAllItems.setPreferredSize(new java.awt.Dimension(914, 280));
        jpGridAllItems.setVerifyInputWhenFocusTarget(false);
        jpGridAllItems.setLayout(new java.awt.BorderLayout());

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jbAdd.setText("Agregar");
        jbAdd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel19.add(jbAdd);

        jbAddAll.setText("Agregar todo");
        jbAddAll.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel19.add(jbAddAll);

        jpGridAllItems.add(jPanel19, java.awt.BorderLayout.SOUTH);

        jpGridContainer.add(jpGridAllItems, java.awt.BorderLayout.NORTH);

        jpGridItemSelected.setBorder(javax.swing.BorderFactory.createTitledBorder("Ítems seleccionados para solicitar"));
        jpGridItemSelected.setLayout(new java.awt.BorderLayout());

        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jbRemove.setText("Quitar");
        jbRemove.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel20.add(jbRemove);

        jbRemoveAll.setText("Quitar todo");
        jbRemoveAll.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel20.add(jbRemoveAll);

        jLabel1.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel20.add(jLabel1);

        jbMinSupply.setText(" =  al mínimo");
        jbMinSupply.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel20.add(jbMinSupply);

        jbPoSupply.setText("= pto. reorden");
        jbPoSupply.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel20.add(jbPoSupply);

        jbMaxSupply.setText("= máximo");
        jbMaxSupply.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel20.add(jbMaxSupply);

        moDecQtySup.setText("0.0000");
        moDecQtySup.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel20.add(moDecQtySup);

        jbFreeSupply.setText("= cantidad");
        jbFreeSupply.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel20.add(jbFreeSupply);

        jbErase.setText("Borrar todo");
        jbErase.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel20.add(jbErase);

        jpGridItemSelected.add(jPanel20, java.awt.BorderLayout.SOUTH);

        jpGridContainer.add(jpGridItemSelected, java.awt.BorderLayout.CENTER);

        jpContent.add(jpGridContainer, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpContent, java.awt.BorderLayout.CENTER);

        jpControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setPreferredSize(new java.awt.Dimension(80, 23));
        jpControls.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setPreferredSize(new java.awt.Dimension(80, 23));
        jpControls.add(jbCancel);

        getContentPane().add(jpControls, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jIGen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbAddAll;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbContinue;
    private javax.swing.JButton jbErase;
    private javax.swing.JButton jbFreeSupply;
    private javax.swing.JButton jbMaxSupply;
    private javax.swing.JButton jbMinSupply;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbPoSupply;
    private javax.swing.JButton jbRemove;
    private javax.swing.JButton jbRemoveAll;
    private javax.swing.JButton jbRestart;
    private javax.swing.JButton jbShow;
    private javax.swing.ButtonGroup jbgItems;
    private javax.swing.JLabel jlWhs;
    private javax.swing.JPanel jpContent;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpGridAllItems;
    private javax.swing.JPanel jpGridContainer;
    private javax.swing.JPanel jpGridItemSelected;
    private javax.swing.JRadioButton jrbAllItems;
    private javax.swing.JRadioButton jrbDownMax;
    private javax.swing.JRadioButton jrbDownMin;
    private javax.swing.JRadioButton jrbDownPo;
    private javax.swing.JRadioButton jrbUpMax;
    private javax.swing.JRadioButton jrbUpMin;
    private javax.swing.JRadioButton jrbUpPo;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecQtySup;
    private sa.lib.gui.bean.SBeanFieldKey moKeyIGen;
    private sa.lib.gui.bean.SBeanFieldKey moKeyWhs;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 670);
        
        moKeyWhs.setKeySettings(miClient, SGuiUtils.getLabelName(jlWhs), true);
        moKeyIGen.setKeySettings(miClient, SGuiUtils.getLabelName(jIGen), false);
        moDecQtySup.setDecimalSettings(SGuiUtils.getLabelName("Surtir esta cantidad"), SGuiConsts.GUI_TYPE_DEC_QTY, false);
        
        // Items disponibles
        
        moGridItems = new SGridPaneForm(miClient, SModConsts.TRNX_MAT_REQ_ITM_SUP, 0, "Items disponibles") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> columns = new ArrayList<>();
                
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "Clave"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Ítem", 350));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Mínimo", 70));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Pto. reorden", 70));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Máximo", 70));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Existencias", 70));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Segregado", 70));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Disponible", 70));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Requisitado", 70));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad"));
                
                return columns;
            }
        };
        
        mvFormGrids.add(moGridItems);
        jpGridAllItems.add(moGridItems, BorderLayout.CENTER);
        
        //Items seleccionados
        
        moGridItemsSelected = new SGridPaneForm(miClient, SModConsts.TRNX_MAT_REQ_ITM_SUP_SEL, 0, "Items seleccionados") {

            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> columns = new ArrayList<>();
            
                SGridColumnForm col = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Cantidad", 70);
                col.setEditable(true);
                
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "Clave"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "Ítem", 290));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Mínimo", 70));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Pto. reorden", 70));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Máximo", 70));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Existencias", 70));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Segregado", 70));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Disponible", 70));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_QTY, "Requisitado", 70));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad"));
                columns.add(col);
                
                return columns;
            }
            
        };
        
        mvFormGrids.add(moGridItemsSelected);
        jpGridItemSelected.add(moGridItemsSelected, BorderLayout.CENTER);
    }
    
    private String getSql() {
        String sqlWhere = "";
        String operation = "stk.f_stk_avble - stk.f_stk_seg + COALESCE(req.pen_sumi_qty, 0)";
        
        int year = miClient.getSession().getSystemYear();
        int cob = moKeyWhs.getValue()[0];
        
        if (moKeyIGen.getSelectedIndex() > 0) {
            sqlWhere += "AND stk.igen = " + moKeyIGen.getValue()[0] + " ";
        }
        
        if (jrbDownMin.isSelected()) {
            sqlWhere += "AND " + operation + " <= stk.qty_min ";
        }
        else if (jrbUpMin.isSelected()) {
            sqlWhere += "AND " + operation + " > stk.qty_min ";
        }
        else if (jrbDownPo.isSelected()) {
            sqlWhere += "AND " + operation + " <= stk.rop ";
        }
        else if (jrbUpPo.isSelected()) {
            sqlWhere += "AND " + operation + " > stk.rop ";
        }
        else if (jrbDownMax.isSelected()) {
            sqlWhere += "AND " + operation + " <= stk.qty_max ";
        }
        else if (jrbUpMax.isSelected()) {
            sqlWhere += "AND " + operation + " > stk.qty_max ";
        }
        
        String sql = "SELECT * FROM( " +
                "SELECT s.id_item, s.id_unit, i.item_key, i.item, i.fid_igen AS igen, u.symbol, i.part_num, s.id_cob, s.id_wh, bpb.code, ent.code AS ent, sc.qty_min, sc.qty_max, sc.rop, " +
                "SUM(s.mov_in - s.mov_out) AS f_stk, ( " +
                "SELECT COALESCE(SUM(wety.qty_inc - wety.qty_dec), 0) " +
                "FROM trn_stk_seg_whs AS swhs " +
                "INNER JOIN trn_stk_seg_whs_ety AS wety ON swhs.id_stk_seg = wety.id_stk_seg AND swhs.id_cob = wety.id_cob AND swhs.id_whs = wety.id_whs " +
                "WHERE fid_year = " + year + " AND fid_item = i.id_item AND fid_unit = u.id_unit AND swhs.id_cob = " + cob + " AND wety.id_whs = ent.id_ent ) AS f_stk_seg, (SUM(s.mov_in - s.mov_out) - ( " +
                "SELECT COALESCE(SUM(wety.qty_inc - wety.qty_dec), 0) " +
                "FROM trn_stk_seg_whs AS swhs " +
                "INNER JOIN trn_stk_seg_whs_ety AS wety ON swhs.id_stk_seg = wety.id_stk_seg AND swhs.id_cob = wety.id_cob AND swhs.id_whs = wety.id_whs " +
                "WHERE fid_year = " + year + " AND fid_item = i.id_item AND fid_unit = u.id_unit AND swhs.id_cob = " + cob + " AND wety.id_whs = ent.id_ent )) AS f_stk_avble " +
                "FROM trn_stk AS s " +
                "INNER JOIN erp.itmu_item AS i ON s.id_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON s.id_unit = u.id_unit " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON s.id_cob = bpb.id_bpb " +
                "INNER JOIN erp.cfgu_cob_ent AS ent ON s.id_cob = ent.id_cob AND s.id_wh = ent.id_ent " +
                "INNER JOIN trn_stk_cfg AS sc ON sc.id_item = i.id_item AND sc.id_unit = u.id_unit AND sc.id_cob = bpb.id_bpb AND sc.id_wh = ent.id_ent " +
                "WHERE s.b_del = 0 AND s.id_year = " + year + " AND s.dt <= '" + year + "-12-31' AND s.id_cob = " + cob + " " +
                "GROUP BY s.id_item, s.id_unit, s.id_cob, s.id_wh, bpb.code, ent.code, i.item_key, i.item, i.fid_igen, u.symbol " +
                "HAVING f_stk <> 0 " +
                "ORDER BY i.item_key, i.item, s.id_item, u.symbol, i.part_num, s.id_unit , bpb.code, ent.code, s.id_cob, s.id_wh) AS stk " +
                "LEFT JOIN( " +
                "SELECT i.id_item, u.id_unit, COALESCE(SUM(ve.qty) - de.sumi_qty, SUM(ve.qty)) AS pen_sumi_qty, COALESCE(SUM(de.sumi_qty), 0) / SUM(ve.qty) AS per " +
                "FROM trn_mat_req AS v " +
                "LEFT JOIN trn_mat_req_ety AS ve ON v.id_mat_req = ve.id_mat_req " +
                "LEFT JOIN " +
                "  (SELECT de.fid_mat_req_n, de.fid_mat_req_ety_n, SUM(de.qty * IF(d.fid_ct_iog = 1, -1, 1)) AS sumi_qty " +
                "  FROM trn_diog AS d " +
                "  INNER JOIN trn_diog_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                "  INNER JOIN trn_mat_req AS v ON de.fid_mat_req_n = v.id_mat_req " +
                "  WHERE de.fid_mat_req_n IS NOT NULL AND de.fid_mat_req_ety_n IS NOT NULL AND NOT de.b_del " +
                "  AND NOT d.b_del AND d.fid_ct_iog IN (1, 2) AND d.fid_cl_iog = 7 AND d.fid_tp_iog = 1 " +
                "  GROUP BY de.fid_mat_req_n, de.fid_mat_req_ety_n  ORDER BY de.fid_mat_req_n, de.fid_mat_req_ety_n ) AS de " +
                "ON ve.id_mat_req = de.fid_mat_req_n AND ve.id_ety = de.fid_mat_req_ety_n " +
                "INNER JOIN erp.itmu_item AS i ON ve.fk_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON ve.fk_unit = u.id_unit " +
                "LEFT JOIN erp.trnu_mat_req_pty AS rpe ON ve.fk_mat_req_pty_n = rpe.id_mat_req_pty " +
                "WHERE v.b_del = 0 " +
                "AND cfg_get_st_authorn(1, 'trn_mat_req', v.id_mat_req, NULL, NULL, NULL, NULL) != 5 " +
                "AND NOT v.b_clo_prov " +
                "AND v.tp_req = 'C' " +
                "GROUP BY i.id_item, u.id_unit HAVING per < 1 " +
                "ORDER BY i.id_item, u.id_unit ) AS req " +
                "ON stk.id_item = req.id_item AND stk.id_unit = req.id_unit " +
                "WHERE stk.id_cob = " + moKeyWhs.getValue()[0] + " AND stk.id_wh = " + moKeyWhs.getValue()[1] + " " + 
                sqlWhere;
        return sql;
    }
    
    private void reloadItemList() {
        try {
            maGridItems = new ArrayList<>();
            Statement statement = miClient.getSession().getStatement();
            String sql = getSql();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                maGridItems.add(getItemSupplyRow(resultSet));
            }
            populateGridItems();
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void readAllItems(){
        try {
            moKeyIGen.setSelectedIndex(0);
            jrbAllItems.setSelected(true);
            if (maAllItems == null) {
                maAllItems = new ArrayList<>();
                Statement statement = miClient.getSession().getStatement();
                String sql = getSql();
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    maAllItems.add(getItemSupplyRow(resultSet));
                }
            }
            maGridItems = maAllItems;
            populateGridItems();
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private SRowMaterialRequestItemSupply getItemSupplyRow(ResultSet resultSet) throws Exception {
        SRowMaterialRequestItemSupply row = new SRowMaterialRequestItemSupply();
        row.mnItemId = resultSet.getInt("id_item");
        row.mnUnitId = resultSet.getInt("id_unit");
        row.mnIGenId = resultSet.getInt("igen");
        row.msItemKey = resultSet.getString("item_key");
        row.msItem = resultSet.getString("item");
        row.mdMin = resultSet.getDouble("qty_min");
        row.mdPo = resultSet.getDouble("rop");
        row.mdMax = resultSet.getDouble("qty_max");
        row.mdStk = resultSet.getDouble("f_stk");
        row.mdStkSeg = resultSet.getDouble("f_stk_seg");
        row.mdStkDisp = resultSet.getDouble("f_stk_avble");
        row.mdReq = resultSet.getDouble("pen_sumi_qty");
        row.msUnit = resultSet.getString("symbol");
        row.mdQty = 0.0;
        return row;
    }
    
    private void setWah() {
        if (SLibUtils.compareKeys(new int[] { 0 , 0 }, moWahId)) {
            mbIsCaptureMode = false;
        }
        else {
            moKeyWhs.setValue(moWahId);
            mbIsCaptureMode = true;
        }
    }
    
    private void setEnabledComponets(boolean enable) {
        moKeyWhs.setEnabled(!enable);
        jbContinue.setEnabled(!enable);
        jbRestart.setEnabled(enable);
        jbShow.setEnabled(false);
        jbAdd.setEnabled(enable);
        jbAddAll.setEnabled(enable);
        jbRemove.setEnabled(enable);
        jbRemoveAll.setEnabled(enable);
        jbMinSupply.setEnabled(enable);
        jbPoSupply.setEnabled(enable);
        jbMaxSupply.setEnabled(enable);
        moDecQtySup.setEnabled(enable);
        jbFreeSupply.setEnabled(enable);
        jbErase.setEnabled(enable);
        
        jrbDownMin.setEnabled(enable);
        jrbDownPo.setEnabled(enable);
        jrbDownMax.setEnabled(enable);
        jrbAllItems.setEnabled(enable);
        jrbUpMin.setEnabled(enable);
        jrbUpPo.setEnabled(enable);
        jrbUpMax.setEnabled(enable);
        
        jbOk.setEnabled(enable);
    }
    
    private boolean meetsFilter(SRowMaterialRequestItemSupply row) {
        if (moKeyIGen.getSelectedIndex() > 0) {
            if (moKeyIGen.getValue()[0] != row.mnIGenId) {
                return false;
            }
            else {
                return validateMaxMinFiler(row);
            }
        }
        else {
            return validateMaxMinFiler(row);
        }
    }
    
    private boolean validateMaxMinFiler(SRowMaterialRequestItemSupply row) {
        if (jrbDownMin.isSelected()) {
            return (row.mdStk - row.mdStkSeg + row.mdReq) <= row.mdMin;
        }
        else if (jrbUpMin.isSelected()) {
            return (row.mdStk - row.mdStkSeg + row.mdReq) > row.mdMin;
        }
        else if (jrbDownPo.isSelected()) {
            return (row.mdStk - row.mdStkSeg + row.mdReq) <= row.mdPo;
        }
        else if (jrbUpPo.isSelected()) {
            return (row.mdStk - row.mdStkSeg + row.mdReq) > row.mdPo;
        }
        else if (jrbDownMax.isSelected()) {
            return (row.mdStk - row.mdStkSeg + row.mdReq) <= row.mdMax;
        }
        else if (jrbUpMax.isSelected()) {
            return (row.mdStk - row.mdStkSeg + row.mdReq) > row.mdMax;
        }
        return true;
    }
    
    private void populateGridItems() {
        Vector<SGridRow> vRows = new Vector<>();
        HashSet<SRowMaterialRequestItemSupply> hs = new HashSet<>();
        
        for (SRowMaterialRequestItemSupply selected : maGridItemsSelected) {
            for (SRowMaterialRequestItemSupply avb : maGridItems) {
                if (SLibUtils.compareKeys(selected.getRowPrimaryKey(), avb.getRowPrimaryKey())) {
                    hs.add(avb);
                }
            }
        }
        
        for (SRowMaterialRequestItemSupply i : hs) {
            maGridItems.remove(i);
        }
        
        if (maGridItems.size() > 0) {
            vRows.addAll(maGridItems);
        }
        moGridItems.populateGrid(vRows);
    }
    
    private void populateGridItemsSelected() {
        Vector<SGridRow> vRows = new Vector<>();
        if (maGridItemsSelected.size() > 0) {
            vRows.addAll(maGridItemsSelected);
        }
        moGridItemsSelected.populateGrid(vRows);
    }
    
    private void actionContinue() {
        if (moKeyWhs.getSelectedIndex() < 1) {
            miClient.showMsgBoxInformation("Debe seleccionar un almacén");
        }
        else {
            readAllItems();
            mbIsCaptureMode = true;
            setEnabledComponets(mbIsCaptureMode);
        }
    }
    
    private void actionShow() {
        if (jrbAllItems.isSelected() && moKeyIGen.getSelectedIndex() < 1) {
            readAllItems();
        }
        else {
            reloadItemList();
        }
        
        jbShow.setEnabled(false);
    }
    
    private void actionAdd() {
        for(SGridRow row : moGridItems.getSelectedGridRows()) {
            maGridItemsSelected.add((SRowMaterialRequestItemSupply) row);
            maGridItems.remove((SRowMaterialRequestItemSupply) row);
        }
        populateGridItems();
        populateGridItemsSelected();
    }
    
    private void actionAddAll() {
        maGridItems.stream().forEach((is) -> {
            maGridItemsSelected.add(is);
        });
        maGridItems.clear();
        populateGridItems();
        populateGridItemsSelected();
    }
    
    private void actionRemove() {
        for (SGridRow row : moGridItemsSelected.getSelectedGridRows()) {
            if (meetsFilter((SRowMaterialRequestItemSupply) row)) {
                maGridItems.add((SRowMaterialRequestItemSupply) row);
            }
            maGridItemsSelected.remove((SRowMaterialRequestItemSupply) row);
        }
        populateGridItems();
        populateGridItemsSelected();
    }
    
    private void actionRemoveAll() {
        maGridItemsSelected.stream().filter((is) -> (meetsFilter(is))).forEach((is) -> {
            maGridItems.add(is);
        });
        maGridItemsSelected.clear();
        populateGridItems();
        populateGridItemsSelected();
    }
    
    private void actionMinSupply() {
        for (SRowMaterialRequestItemSupply is : maGridItemsSelected) {
            double disp = is.mdStk - is.mdStkSeg + is.mdReq;
            if (disp < is.mdMin) {
                is.mdQty = is.mdMin - disp;
            }
            else {
                is.mdQty = 0.0;
            }
        }
        populateGridItemsSelected();
    }
    
    private void actionPoSupply() {
        for (SRowMaterialRequestItemSupply is : maGridItemsSelected) {
            double disp = is.mdStk - is.mdStkSeg + is.mdReq;
            if (disp < is.mdPo) {
                is.mdQty = is.mdPo - disp;
            }
            else {
                is.mdQty = 0.0;
            }
        }
        populateGridItemsSelected();
    }
    
    private void actionMaxSupply() {
        for (SRowMaterialRequestItemSupply is : maGridItemsSelected) {
            double disp = is.mdStk - is.mdStkSeg + is.mdReq;
            if (disp < is.mdMax) {
                is.mdQty = is.mdMax - disp;
            }
            else {
                is.mdQty = 0.0;
            }
        }
        populateGridItemsSelected();
    }
    
    private void actionFreeSupply() {
        for (SRowMaterialRequestItemSupply is : maGridItemsSelected) {
            is.mdQty = moDecQtySup.getValue();
        }
        populateGridItemsSelected();
    }
    
    private void actionErase() {
        for (SRowMaterialRequestItemSupply is : maGridItemsSelected) {
            is.mdQty = 0.0;
        }
        populateGridItemsSelected();
    }
    
    private void actionOk() {
        mnFormResult = SGuiConsts.FORM_RESULT_OK;
        dispose();
    }
    
    public void prepareDialog() {
        removeAllListeners();
        reloadCatalogues();
        populateGridItems();
        populateGridItemsSelected();
        mbIsCaptureMode = false;
        setWah();
        setEnabledComponets(mbIsCaptureMode);
        if (mbIsCaptureMode) {
            readAllItems();
        }
        addAllListeners();
    }
    
    /**
     *
     * @param param
     * @param value
     */
    @Override
    public void setValue(int param, Object value) {
        switch (param) {
            case 1: 
                mnConsEntId = (int) value; 
                break;
            case 2:
                moWahId = (int[]) value;
                break;
        }
    }
    
    @Override
    public Object getValue(int param) {
        Object value = null;
        
        switch (param) {
            case 2:
                value = moKeyWhs.getValue(); 
                break;
            case 3:
                value = maGridItemsSelected;
                break;
        }
        
        return value;
    }
    
    @Override
    public void addAllListeners() {
        jbContinue.addActionListener(this);
        jbRestart.addActionListener(this);
        jbShow.addActionListener(this);
        jbAdd.addActionListener(this);
        jbAddAll.addActionListener(this);
        jbRemove.addActionListener(this);
        jbRemoveAll.addActionListener(this);
        jbMinSupply.addActionListener(this);
        jbPoSupply.addActionListener(this);
        jbMaxSupply.addActionListener(this);
        jbFreeSupply.addActionListener(this);
        jbErase.addActionListener(this);
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        
        jrbDownMin.addItemListener(this);
        jrbDownPo.addItemListener(this);
        jrbDownMax.addItemListener(this);
        jrbAllItems.addItemListener(this);
        jrbUpMin.addItemListener(this);
        jrbUpPo.addItemListener(this);
        jrbUpMax.addItemListener(this);
        moKeyIGen.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbContinue.removeActionListener(this);
        jbRestart.removeActionListener(this);
        jbShow.removeActionListener(this);
        jbAdd.removeActionListener(this);
        jbAddAll.removeActionListener(this);
        jbRemove.removeActionListener(this);
        jbRemoveAll.removeActionListener(this);
        jbMinSupply.removeActionListener(this);
        jbPoSupply.removeActionListener(this);
        jbMaxSupply.removeActionListener(this);
        jbFreeSupply.removeActionListener(this);
        jbErase.removeActionListener(this);
        jbOk.removeActionListener(this);
        jbCancel.removeActionListener(this);
        
        jrbDownMin.removeItemListener(this);
        jrbDownPo.removeItemListener(this);
        jrbDownMax.removeItemListener(this);
        jrbAllItems.removeItemListener(this);
        jrbUpMin.removeItemListener(this);
        jrbUpPo.removeItemListener(this);
        jrbUpMax.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        SGuiParams params = new SGuiParams(mnConsEntId);
        miClient.getSession().populateCatalogue(moKeyWhs, SModConsts.CFGU_COB_ENT, SLibConsts.UNDEFINED, params);
        miClient.getSession().populateCatalogue(moKeyIGen, SModConsts.ITMU_IGEN, SModConsts.ITMX_IGEN_INV, null);
        
        maGridItems = new ArrayList<>();
        maGridItemsSelected = new ArrayList<>();
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiValidation validateForm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbContinue) {
                actionContinue();
            }
            else if (button == jbRestart) {
                prepareDialog();
            }
            else if (button == jbShow) {
                actionShow();
            }
            else if (button == jbAdd) {
                actionAdd();
            }
            else if (button == jbAddAll) {
                actionAddAll();
            }
            else if (button == jbRemove) {
                actionRemove();
            }
            else if (button == jbRemoveAll) {
                actionRemoveAll();
            }
            else if (button == jbMinSupply) {
                actionMinSupply();
            }
            else if (button == jbPoSupply) {
                actionPoSupply();
            }
            else if (button == jbMaxSupply) {
                actionMaxSupply();
            }
            else if (button == jbFreeSupply) {
                actionFreeSupply();
            }
            else if (button == jbErase) {
                actionErase();
            }
            else if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        jbShow.setEnabled(true);
    }
}

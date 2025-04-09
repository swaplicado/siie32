/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.trn.form.SDialogDpsTankCarCardex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRow;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Isabel Servín
 */
public class SViewDpsTankCar extends SGridPaneView implements ActionListener, ItemListener {
    
    private JCheckBox jcbInCatalog;
    private JButton jbCardex;
    
    private SDialogDpsTankCarCardex moDialogCardex;

    public SViewDpsTankCar(SGuiClient client, int subType, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_DPS_TANK_CAR, subType, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        jcbInCatalog = new JCheckBox("Ver otros carrotanques");
        jcbInCatalog.setToolTipText("Ver otros carrotanques que no estan en el catálogo");
        jcbInCatalog.addItemListener(this);
        
        jbCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Ver cárdex de facturas", this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jcbInCatalog);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCardex);
        
        moDialogCardex = new SDialogDpsTankCarCardex(miClient, "Cardex de facturas");
    }
    
    private void actionCardex() {
        String plate;
        
        if (jbCardex.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                    if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else {
                        plate = (String) gridRow.getRowValueAt(0);
                    
                        moDialogCardex.setValue(SDialogDpsTankCarCardex.TANK_CAR_PLATE, plate);
                        moDialogCardex.setValue(SDialogDpsTankCarCardex.DPS_TYPE, mnGridSubtype);
                        moDialogCardex.setVisible(true);
                    }
                }
                catch (Exception ex) {
                    Logger.getLogger(SViewMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    @Override
    public void computeGridData() {
        try {
            String plate;
            for (SGridRow row : moModel.getGridRows()) {
                plate = (String) row.getRowValueAt(0);
                
                String sql = "SELECT "
                        + "d.id_year, "
                        + "d.id_doc, "
                        + "dt.code, "
                        + "IF(d.num_ser <> '', CONCAT(d.num_ser, '-', d.num), d.num) AS num, "
                        + "d.num_ref, "
                        + "d.dt, "
                        + "bp.bp, "
                        + "CONCAT(ad.street, ' ', ad.street_num_ext, IF(ad.street_num_int <> '', CONCAT('-', ad.street_num_int), '')) AS street, "
                        + "ad.neighborhood AS nei, "
                        + "ad.zip_code, "
                        + "ad.locality, "
                        + "ad.county, "
                        + "ad.state, "
                        + "IF(c.cty IS NULL, csys.cty, c.cty) AS cty "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON "
                        + "d.id_year = de.id_year AND d.id_doc = de.id_doc "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON "
                        + "d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                        + "d.fid_bp_r = bp.id_bp "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB_ADD) + " AS ad ON "
                        + "d.fid_bpb = ad.id_bpb AND d.fid_add = ad.id_add "
                        + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.LOCU_CTY) + " AS c ON "
                        + "ad.fid_cty_n = c.id_cty "
                        + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.LOCU_CTY) + " AS csys ON "
                        + "csys.id_cty = (SELECT fid_cty FROM " + SModConsts.TablesMap.get(SModConsts.CFG_PARAM_ERP) + ") "
                        + "WHERE FIND_IN_SET('" + plate + "', de.tank_car) "
                        + (mnGridSubtype == SModConsts.MOD_TRN_SAL_N ?
                        "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0] + " AND fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1] + " AND fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2] + " " :
                        "AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] + " AND fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] + " AND fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2] + " ")
                        + "ORDER BY de.ts_new DESC LIMIT 1;";
                ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
                if (resultSet.next()) {
                    row.setRowValueAt(resultSet.getString("code"), 3);
                    row.setRowValueAt(resultSet.getString("num"), 4);
                    row.setRowValueAt(resultSet.getString("num_ref"), 5);
                    row.setRowValueAt(resultSet.getDate("dt"), 6);
                    row.setRowValueAt(resultSet.getString("bp"), 7);
                    row.setRowValueAt(resultSet.getString("street"), 8);
                    row.setRowValueAt(resultSet.getString("nei"), 9);
                    row.setRowValueAt(resultSet.getString("zip_code"), 10);
                    row.setRowValueAt(resultSet.getString("locality"), 11);
                    row.setRowValueAt(resultSet.getString("county").toUpperCase(), 12);
                    row.setRowValueAt(resultSet.getString("state").toUpperCase(), 13);
                    row.setRowValueAt(resultSet.getString("cty").toUpperCase(), 14);
                }
            }
        } catch (SQLException e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setDisabledApplying(false);
        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setDateApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "b.b_del = 0 ";
        }
        
        if(!jcbInCatalog.isSelected()) {
            sql += (sql.isEmpty() ? "" : "AND ") + "b.b_cat = 1 ";
        }
        
        msSql = "SELECT * FROM("
            + "SELECT "
            + "t.id_tank_car AS " + SDbConsts.FIELD_ID + "1, "
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "t.plate AS " + SDbConsts.FIELD_NAME + ", "
            + "'' AS doc_type, "
            + "'' AS inv, "
            + "'' AS ref, "
            + "NULL AS date, "
            + "'' AS bp, "
            + "'' AS street, "
            + "'' AS nei, "
            + "'' AS zip_code, "
            + "'' AS locality, "
            + "'' AS county, "
            + "'' AS state, "
            + "'' AS cty, "
            + "1 AS b_cat, "
            + "t.b_del AS " + SDbConsts.FIELD_IS_DEL + " "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_TANK_CAR) + " AS t "
            + "UNION "
            + "SELECT "
            + "0 AS " + SDbConsts.FIELD_ID + "1, "
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "plate AS " + SDbConsts.FIELD_NAME + ", "
            + "'' AS doc_type, "
            + "'' AS inv, "
            + "'' AS ref, "
            + "NULL AS date, "
            + "'' AS bp, "
            + "'' AS street, "
            + "'' AS nei, "
            + "'' AS zip_code, "
            + "'' AS locality, "
            + "'' AS county, "
            + "'' AS state, "
            + "'' AS cty, "
            + "0 AS b_cat, "
            + "0 AS " + SDbConsts.FIELD_IS_DEL + " "
            + "FROM("
            + "SELECT DISTINCT SUBSTRING_INDEX(SUBSTRING_INDEX(de.tank_car, ',', n.n), ',', -1) AS plate "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de "
            + "JOIN(SELECT 1 AS n UNION ALL SELECT 2) n ON " // se debe ajustar de acuerdo a la cantidad de posibles matriculas en el campo
            + "n.n <= (LENGTH(de.tank_car) - LENGTH(REPLACE(de.tank_car, ',', '')) + 1) "
            + "WHERE de.tank_car <> '') AS a "
            + "WHERE a.plate NOT IN "
            + "(SELECT t.plate FROM " + SModConsts.TablesMap.get(SModConsts.LOG_TANK_CAR) + " AS t)) AS b "
            + (sql.isEmpty() ? "" : "WHERE " + sql)
            + "ORDER BY b.f_name;";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[15];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, SDbConsts.FIELD_NAME, "Matricula");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_cat", "Catalogo carrotanques");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "doc_type", "Tipo documento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "inv", "Folio documento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "ref", "Referencia documento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "date", "Fecha documento");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp", "Asociado negocios");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "street", "Calle", 250);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "nei", "Colonia", 150);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "zip_code", "Código postal", 50);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "locality", "Localidad", 100);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "county", "Municipio", 100);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "state", "Estado", 100);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "cty", "País", 100);
        gridColumnsViews.addAll(Arrays.asList((SGridColumnView[]) columns));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.LOG_TANK_CAR);
        moSuscriptionsSet.add(SModConsts.TRN_DPS);
        moSuscriptionsSet.add(SModConsts.TRN_DPS_ETY);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbCardex) {
                actionCardex();
            }
        }
    }
    
    @Override
    public void itemStateChanged(ItemEvent e) {
        actionGridReload();
    }
}

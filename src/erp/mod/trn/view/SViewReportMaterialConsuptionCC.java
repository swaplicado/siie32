/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.mod.SModConsts;
import java.util.ArrayList;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Isabel Servin
 */
public class SViewReportMaterialConsuptionCC extends SGridPaneView {
    
    private SGridFilterDatePeriod moFilterDatePeriod;

    public SViewReportMaterialConsuptionCC(SGuiClient client, int subType, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_MAT_CONS_CC, subType, title);
        initComponents();
    }
    
    private void initComponents() {
        setRowButtonsEnabled(false);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }

    @Override
    public void prepareSqlQuery() {
        Object filter;
        String where = "";
        String select;
        String groupBy = "";
        
        moPaneSettings = new SGridPaneSettings(0);

        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        if (filter != null) {
            where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("d.dt", (SGuiDate) filter);
        }
        
        if (getGridSubtype() == SModConsts.TRNX_MAT_CONS_CC_R) {
            select = "SUM(IF(de.qty < 0, de.qty * -1, 0)) AS c_salida, " 
                    + "SUM(IF(de.qty > 0, de.qty, 0)) AS c_entrada, " 
                    + "SUM(IF(de.qty < 0, de.val, 0)) AS p_salida, " 
                    + "SUM(IF(de.qty > 0, de.val, 0)) AS p_entrada";
            groupBy = "GROUP BY e.id_mat_cons_ent, s.id_mat_cons_ent, s.id_mat_cons_subent, cc.pk_cc, i.id_item, u.id_unit";
        }
        else {
            select = "IF(de.qty < 0, de.qty * -1, 0) AS c_salida, " 
                    + "IF(de.qty > 0, de.qty, 0) AS c_entrada, " 
                    + "IF(de.qty < 0, de.val, 0) AS p_salida, " 
                    + "IF(de.qty > 0, de.val, 0) AS p_entrada";
        }
        
        msSql = "SELECT " 
                + "e.id_mat_cons_ent AS " + SDbConsts.FIELD_ID + "1, " 
                + "s.id_mat_cons_ent, s.id_mat_cons_subent, cc.pk_cc, i.id_item, u.id_unit, "
                + "e.code AS c_centro, " 
                + "e.code AS " + SDbConsts.FIELD_CODE + ", " 
                + "e.name AS centro, " 
                + "e.name AS " + SDbConsts.FIELD_NAME + ", " 
                + "s.code AS c_subc, " 
                + "s.name AS subc, " 
                + "cc.id_cc, " 
                + "cc.cc, " 
                + "i.item, " 
                + "u.symbol, " 
                + "de.val_u, " 
                + select + " " 
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON " 
                + "d.id_year = de.id_year AND d.id_doc = de.id_doc " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " 
                + "de.fid_item = i.id_item " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON " 
                + "de.fid_unit = u.id_unit " 
                + "INNER JOIN trn_diog_ety_cons_ent_cc AS ce ON " 
                + "de.id_year = ce.fid_diog_doc AND de.id_doc = ce.fid_diog_year AND de.id_ety = ce.fid_diog_ety " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT) + " AS e ON " 
                + "ce.fid_mat_sub_cons_ent = e.id_mat_cons_ent " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT) + " AS s ON " 
                + "ce.fid_mat_sub_cons_ent = s.id_mat_cons_ent AND ce.fid_mat_sub_cons_sub_ent = s.id_mat_cons_subent " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cc ON " 
                + "ce.fid_cc = cc.pk_cc " 
                + (where.isEmpty() ? "" : "WHERE " + where) + " "
                + groupBy + " " 
                + "ORDER BY e.code, e.name, s.code, s.name, cc.id_cc "
                ;        
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();
        
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "c_centro", "Código centro de consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "centro", "Centro de consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "c_subc", "Código subcentro de consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "subc", "Subcentro de consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "id_cc", "Centro de costo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "cc", "Nombre CC"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "item", "Ítem"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "symbol", "Unidad"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "val_u", "Precio u. $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "c_salida", "Cantidad salida"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "c_entrada", "Cantidad entrada"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "p_salida", "Precio salida $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "p_entrada", "Precio entrada $"));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.ITMU_UNIT);
        moSuscriptionsSet.add(SModConsts.TRN_DIOG);
        moSuscriptionsSet.add(SModConsts.TRN_DIOG_ETY);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_ENT);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_SUBENT);
        moSuscriptionsSet.add(SModConsts.FIN_CC);
    }
}

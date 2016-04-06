/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.mcfg.data.SDataParamsErp;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridFilterYear;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Sergio Flores
 */
public class SViewInventoryMfgCost extends SGridPaneView {
    
    private SGridFilterYear moFilterYear;
    
    public SViewInventoryMfgCost(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRN_INV_MFG_CST, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
        
        moFilterYear = new SGridFilterYear(client, this);
        moFilterYear.initFilter(SLibTimeUtils.digestYear(miClient.getSession().getCurrentDate()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterYear);
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(false, false, false, false, false);
    }
    
    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(4);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_YEAR)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.id_year = " + ((int[]) filter)[0] + " ";
        }

        msSql = "SELECT "
                + "v.id_year AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_per AS " + SDbConsts.FIELD_ID + "2, "
                + "v.id_item AS " + SDbConsts.FIELD_ID + "3, "
                + "v.id_unit AS " + SDbConsts.FIELD_ID + "4, "
                + "i.item_key AS " + SDbConsts.FIELD_CODE + ", "
                + "i.item AS " + SDbConsts.FIELD_NAME + ", "
                + "u.symbol, "
                + "u.unit, "
                + "v.ord, "
                + "v.ord_sta, "
                + "v.ord_fin, "
                + "v.qty_sta, "
                + "v.qty_fin, "
                + "v.qty_fin_per, "
                + "v.qty_fin_eff_per, "
                + "v.cst, "
                + "v.cst_wip, "
                + "v.cst_fin, "
                + "v.cst_u_wip, "
                + "v.cst_u_fin, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_INV_MFG_CST) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON "
                + "v.id_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON "
                + "v.id_unit = u.id_unit "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.id_year, v.id_per, "
                + (((SDataParamsErp) miClient.getSession().getConfigSystem()).getFkSortingItemTypeId() == SModSysConsts.CFGS_TP_SORT_KEY_NAME ? "i.item, i.item_key, " : "i.item_key, i.item, ")
                + "v.id_item, u.unit, u.id_unit ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<SGridColumnView>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, SDbConsts.FIELD_ID + "1", "Año"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_MONTH, SDbConsts.FIELD_ID + "2", "Mes"));
        
        if (((SDataParamsErp) miClient.getSession().getConfigSystem()).getFkSortingItemTypeId() == SModSysConsts.CFGS_TP_SORT_KEY_NAME) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME + " producto"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE + " producto"));
        }
        else {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE + " producto"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME + " producto"));
        }
        
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "v.ord", "Órds"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "v.ord_sta", "Órds iniciadas"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "v.ord_fin", "Órds terminadas"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "v.qty_sta", "Unids iniciadas"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "v.qty_fin", "Unids terminadas"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "u.symbol", "Unidad"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "v.qty_fin_per", "% unids terminadas"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "v.qty_fin_eff_per", "% efvo unids terminadas"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "v.cst", "Ctos asignados totales $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "v.cst_wip", "Ctos en proceso $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT_UNIT, "v.cst_u_wip", "Cto u producto en proceso $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "v.cst_fin", "Ctos terminados $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT_UNIT, "v.cst_u_fin", "Cto u producto terminado $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.ITMU_UNIT);
        moSuscriptionsSet.add(SModConsts.TRN_INV_VAL);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

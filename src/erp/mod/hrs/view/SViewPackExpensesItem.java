/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mod.SModConsts;
import java.util.ArrayList;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Sergio Flores
 */
public class SViewPackExpensesItem extends SGridPaneView {

    public SViewPackExpensesItem(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRSU_PACK_EXP_ITEM, 0, title);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "NOT pe.b_del ";
        }

        msSql = "SELECT "
                + "pei.id_pack_exp AS " + SDbConsts.FIELD_ID + "1, "
                + "pei.id_tp_exp AS " + SDbConsts.FIELD_ID + "2, "
                + "pe.code AS " + SDbConsts.FIELD_CODE + ", "
                + "pe.name AS " + SDbConsts.FIELD_NAME + ", "
                + "te.code, "
                + "te.name, "
                + "i.item_key, "
                + "i.item, "
                + "pe.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "pe.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "pe.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "pe.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "pe.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "pe.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_PACK_EXP_ITEM) + " AS pei "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_PACK_EXP) + " AS pe ON "
                + "pei.id_pack_exp = pe.id_pack_exp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_EXP) + " AS te ON "
                + "pei.id_tp_exp = te.id_tp_exp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON "
                + "pei.fk_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "pe.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "pe.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY pe.name, pe.code, te.name, te.code, pei.id_pack_exp, pei.id_tp_exp ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME + " paquete gastos"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE + " paquete gastos"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "te.name", SGridConsts.COL_TITLE_NAME + " tipo gasto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "te.code", SGridConsts.COL_TITLE_CODE + " tipo gasto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.item", SGridConsts.COL_TITLE_NAME + " ítem"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "i.item_key", SGridConsts.COL_TITLE_CODE + " ítem"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.HRSU_PACK_EXP);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mod.SModConsts;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Juan Barajas
 */
public class SViewSsContributionTableRow extends SGridPaneView {

    public SViewSsContributionTableRow(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_SSC_ROW, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "t.b_del = 0 ";
        }

        msSql = "SELECT "
                + "tr.id_ssc AS " + SDbConsts.FIELD_ID + "1, "
                + "tr.id_row AS " + SDbConsts.FIELD_ID + "2, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + "t.dt_sta, "
                + "tr.id_row, "
                + "tr.con, "
                + "tr.sub_con, "
                + "tr.wrk_per, "
                + "tr.com_per, "
                + "tr.low_lim_mwz_ref, "
                + "tr.lim_mwz_ref, "
                + "t.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "t.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "t.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "t.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "t.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_SSC) + " AS t "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_SSC_ROW) + " AS tr ON "
                + "t.id_ssc = tr.id_ssc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "t.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "t.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY t.dt_sta, tr.id_ssc, tr.id_row ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "t.dt_sta", "Inicio vigencia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "tr.id_row", "#"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "tr.con", "Concepto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "tr.sub_con", "Subconcepto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_TAX, "tr.wrk_per", "% obrero"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_TAX, "tr.com_per", "% patrón"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "tr.low_lim_mwz_ref", "Límite inf. SMAR"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "tr.lim_mwz_ref", "Tope SMAR"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.HRS_SSC);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

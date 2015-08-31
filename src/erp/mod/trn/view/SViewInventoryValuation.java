/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.data.SDataConstants;
import erp.mod.SModConsts;
import java.sql.SQLException;
import java.util.ArrayList;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Néstor Ávalos
 */
public class SViewInventoryValuation extends SGridPaneView {

    private SGridFilterDatePeriod moFilterDatePeriod;

    public SViewInventoryValuation(SGuiClient client, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRN_INV_VAL, gridSubtype, title, params);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(true, false, true, false, true);

        moPaneSettings = new SGridPaneSettings(1);

        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, miClient.getSession().getCurrentDate().getTime()));

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }

    @Override
    public void prepareSqlQuery() {
        try {
            String sql = "";
            Object filter = null;

            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            if (filter != null) {

                switch (((SGuiDate) filter).getGuiType()) {
                    case SGuiConsts.GUI_DATE_DATE:
                    case SGuiConsts.GUI_DATE_MONTH:
                        sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_year_year = " + SLibTimeUtils.digestMonth((SGuiDate) filter)[0] + " AND " +
                            "v.fk_year_per = " + SLibTimeUtils.digestMonth((SGuiDate) filter)[1] + " ";
                        break;

                    case SGuiConsts.GUI_DATE_YEAR:
                        sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_year_year = " + SLibTimeUtils.digestMonth((SGuiDate) filter)[0] + " ";
                        break;

                    default:
                }
            }

            filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
            if ((Boolean) filter) {
                sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
            }

            miClient.getSession().getStatement().execute("SET @estatus := 1;");

                msSql = "SELECT "
                    + "v.id_inv_val AS " + SDbConsts.FIELD_ID + "1, "
                    + "v.fk_year_year, "
                    + "v.fk_year_per, "
                    + "CONCAT(v.fk_year_year, '-', v.fk_year_per) AS " + SDbConsts.FIELD_CODE + ", "
                    + "CONCAT(v.fk_year_year, '-', v.fk_year_per) AS " + SDbConsts.FIELD_NAME + ", "
                    + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                    + "'PROMEDIOS' AS f_met_val, "
                    + "IF(@estatus = 1, "
                    + "@estatus := IF ("
                    + "(SELECT MAX(ts_edit) "
                    + "FROM trn_diog "
                    + "WHERE b_del = 0 AND id_year <= v.fk_year_year AND (MONTH(dt)) <= v.fk_year_per) > v.ts_usr_upd, "
                    + "0, 1), 0) AS f_estatus, "
                    + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                    + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                    + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                    + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                    + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                    + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                    + "FROM " + SModConsts.TablesMap.get(mnGridType) + " AS v "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                    + "v.fk_usr_ins = ui.id_usr "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                    + "v.fk_usr_upd = uu.id_usr "
                    + (sql.isEmpty() ? "" : "WHERE " + sql) + " "
                    + "ORDER BY v.fk_year_year, v.fk_year_per, v.id_inv_val ";
        } catch (SQLException e) {
            SLibUtils.showException(this, e);
        }
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, "v.fk_year_year", "Año contable"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_MONTH, "v.fk_year_per", "Período"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_met_val", "Método valuación"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "f_estatus", "¿Consistente?"));
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
        moSuscriptionsSet.add(SModConsts.TRNX_STK_DIOG_TP);
        moSuscriptionsSet.add(SModConsts.TRN_STK_COST);
        moSuscriptionsSet.add(SDataConstants.FIN_YEAR);
        moSuscriptionsSet.add(SDataConstants.FIN_YEAR_PER);
        moSuscriptionsSet.add(SDataConstants.USRU_USR);
    }
}

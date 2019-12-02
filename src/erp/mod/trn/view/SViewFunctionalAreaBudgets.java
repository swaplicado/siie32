/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

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
public class SViewFunctionalAreaBudgets extends SGridPaneView {

    public SViewFunctionalAreaBudgets(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_FUNC_BUDGETS, 0, title);
        setRowButtonsEnabled(true, true, true, false, false);
        jtbFilterDeleted.setEnabled(false);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT "
                + "fb.period_year AS " + SDbConsts.FIELD_ID + "1, "
                + "fb.period_year AS " + SDbConsts.FIELD_CODE + ", "
                + "fb.period_year AS " + SDbConsts.FIELD_NAME + ", "
                + "MAX(fb.ts_usr_ins) AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "MAX(fb.ts_usr_upd) AS " + SDbConsts.FIELD_USER_UPD_TS + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_FUNC_BUDGET) + " AS fb "
                + "WHERE NOT fb.b_del "
                + "GROUP BY fb.period_year "
                + "ORDER BY fb.period_year;";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, SDbConsts.FIELD_CODE, "Año"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
    }
}

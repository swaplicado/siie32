/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.mod.SModConsts;
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
public class SViewIdentifiedCostCalculation extends SGridPaneView {
    
    private SGridFilterYear moFilterYear;
    
    public SViewIdentifiedCostCalculation(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRN_COST_IDENT_CALC, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
        
        moFilterYear = new SGridFilterYear(client, this);
        moFilterYear.initFilter(SLibTimeUtils.digestYear(miClient.getSession().getCurrentDate()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterYear);
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(true, false, false, false, false);
    }
    
    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setDateApplying(true);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_YEAR)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "YEAR(v.dt_sta) = " + ((int[]) filter)[0] + " ";
        }
        
        msSql = "SELECT "
                + "v.id_calc AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_calc AS " + SDbConsts.FIELD_CODE + ", "
                + "v.id_calc AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt_sta AS " + SDbConsts.FIELD_DATE + ", "
                + "v.dt_sta, "
                + "v.dt_end, "
                + "v.b_recalc, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_COST_IDENT_CALC) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.dt_sta, v.dt_end, v.id_calc ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, SDbConsts.FIELD_CODE, "Folio"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_sta", "Inicio"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_end", "Fin"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_recalc", "Recalcular"));
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
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

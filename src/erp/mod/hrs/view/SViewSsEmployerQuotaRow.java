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

public class SViewSsEmployerQuotaRow extends SGridPaneView {

    public SViewSsEmployerQuotaRow(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_EMPL_QUO_ROW, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false, false, false, false, false);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = moFiltersMap.get(SGridConsts.FILTER_DELETED) != null ? moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue() : null;

        moPaneSettings = new SGridPaneSettings(0);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        if (filter != null && filter instanceof Boolean) {
            sql += (sql.isEmpty() ? "" : "AND ") + "s.b_del = 0 ";

        }

        msSql = "SELECT "
                + "s.id_empr_ssc AS " + SDbConsts.FIELD_ID + "1, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + " r.low_lim AS 'Base', "
                + " r.low_lim_type AS 'TipoUnidad', "
                + " MAX(CASE WHEN tbl_year = 2023 THEN empr_rate END) AS '2023', "
                + " MAX(CASE WHEN tbl_year = 2024 THEN empr_rate END) AS '2024', "
                + " MAX(CASE WHEN tbl_year = 2025 THEN empr_rate END) AS '2025', "
                + " MAX(CASE WHEN tbl_year = 2026 THEN empr_rate END) AS '2026', "
                + " MAX(CASE WHEN tbl_year = 2027 THEN empr_rate END) AS '2027', "
                + " MAX(CASE WHEN tbl_year = 2028 THEN empr_rate END) AS '2028', "
                + " MAX(CASE WHEN tbl_year = 2029 THEN empr_rate END) AS '2029', "
                + " MAX(CASE WHEN tbl_year = 2030 THEN empr_rate END) AS '2030',"
                + "s.tbl_year, s.b_del AS " + SDbConsts.FIELD_IS_DEL + ", s.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "s.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", s.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", s.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", " 
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMPL_QUO) + " AS s "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EMPL_QUO_ROW) + "  AS r ON r.id_empr_ssc = s.id_empr_ssc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON s.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON s.fk_usr_upd = uu.id_usr "
                + (!sql.isEmpty() ? "WHERE " + sql : "")
                + " GROUP BY low_lim, low_lim_type "
                + " ORDER BY low_lim ";

    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "Base", "Base"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "TipoUnidad", "Tipo Unidad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_TAX, "2023", "Factor 2023"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_TAX, "2024", "Factor 2024"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_TAX, "2025", "Factor 2025"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_TAX, "2026", "Factor 2026"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_TAX, "2027", "Factor 2027"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_TAX, "2028", "Factor 2028"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_TAX, "2029", "Factor 2029"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_TAX, "2030", "Factor 2030"));
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
        moSuscriptionsSet.add(SModConsts.HRSU_EMP);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

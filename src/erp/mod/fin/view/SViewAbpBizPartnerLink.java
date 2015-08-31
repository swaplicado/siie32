/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
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
public class SViewAbpBizPartnerLink extends SGridPaneView {

    public SViewAbpBizPartnerLink(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.FIN_ABP_BP_LINK, SLibConstants.UNDEFINED, title);
        setRowButtonsEnabled(true, true, true, false, true);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(3);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT "
                + "v.id_abp_bp AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_link AS " + SDbConsts.FIELD_ID + "2, "
                + "v.id_ref AS " + SDbConsts.FIELD_ID + "3, "
                + "' ' AS " + SDbConsts.FIELD_CODE + ", "
                + "vb.name AS " + SDbConsts.FIELD_NAME + ", "
                + "vl.name, "
                + "CASE v.id_link "
                + "WHEN " + SModSysConsts.BPSS_LINK_ALL + " THEN "
                + "('(TODOS)') "
                + "WHEN " + SModSysConsts.BPSS_LINK_BP_TP + " THEN "
                + "(SELECT r.tp_bp FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_TP_BP) + " AS r WHERE v.id_ref = r.tp_idx) "
                + "WHEN " + SModSysConsts.BPSS_LINK_BP + " THEN "
                + "(SELECT r.bp FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS r WHERE v.id_ref = r.id_bp) "
                + "ELSE '?' END AS f_ref, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ABP_BP_LINK) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ABP_BP) + " AS vb ON "
                + "v.id_abp_bp = vb.id_abp_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSS_LINK) + " AS vl ON "
                + "v.id_link = vl.id_link "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY vb.name, vl.name, f_ref, v.id_abp_bp, v.id_link, v.id_ref ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "vl.name", "Tipo referencia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_ref", "Referencia"));
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
        moSuscriptionsSet.add(SModConsts.BPSU_TP_BP);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.FIN_ABP_BP);
        moSuscriptionsSet.add(SModConsts.BPSS_LINK);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

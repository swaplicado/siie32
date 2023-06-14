/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.mod.SModConsts;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Isabel Servín
 */
public class SViewMaterialRequest extends SGridPaneView {

    private final SGridFilterDatePeriod moFilterDatePeriod;
    
    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     */
    public SViewMaterialRequest(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRN_MAT_REQ, SLibConsts.UNDEFINED, title, null);
        setRowButtonsEnabled(true);

        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;

        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        if (filter != null) {
            where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
        }
        
        msSql = "SELECT v.id_mat_req AS " + SDbConsts.FIELD_ID + "1, "
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "v.dt_req_n, "
                + "v.ref, "
                + "v.b_ext_sys, "
                + "v.b_clo_prov, "
                + "pe.name AS prov_ent, "
                + "rp.name AS req_pty, "
                + "srm.name AS status, "
                + "ur.usr AS usr_req, "
                + "bmu.bp AS contractor, "
                + "ce.name AS cons_ent, "
                + "cse.name AS cons_subent, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_clo_prov, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_clo_prov, "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "uc.usr AS usr_clo, "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT) + " AS pe ON "
                + "v.fk_mat_prov_ent = pe.id_mat_prov_ent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_MAT_REQ_PTY) + " AS rp ON "
                + "v.fk_mat_req_pty = rp.id_mat_req_pty "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_MAT_REQ) + " AS smr ON "
                + "v.fk_st_mat_req = smr.id_st_mat_req "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ur ON "
                + "v.fk_usr_req = ur.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAINT_USER) + " AS mu ON "
                + "v.fk_contractor_n = mu.id_maint_user "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bmu ON "
                + "mu.id_maint_user = bmu.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT) + " AS ce ON "
                + "v.fk_ent_mat_cons_ent = ce.id_mat_cons_ent "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT) + " AS cse ON "
                + "v.fk_subent_mat_cons_ent = cse.id_mat_cons_ent AND v.fk_subent_mat_cons_sunent = cse.id_mat_cons_subent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON "
                + "v.fk_usr_clo_prov = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "ORDER BY v.num, v.id_dvy ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "prov_ent", "Ent. Suministro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_NAME, "Folio"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_req", "Solicitante"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "contractor", "Contratista"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "ref", "Referencia fac."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "cons_ent", "Ent. consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "cons_subent", "Subent. consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_req_n", "Fecha requerida"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "req_pty", "Prioridad req."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_clo_prov", SGridConsts.COL_TITLE_IS_DEL));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "status", "Estatus"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_clo", "Usr cerrado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_usr_clo_prov", "Usr TS cerrado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRNU_MAT_REQ_PTY);
        moSuscriptionsSet.add(SModConsts.TRNS_ST_MAT_REQ);
        moSuscriptionsSet.add(SModConsts.TRN_MAINT_USER);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_PROV_ENT);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_ENT);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_SUBENT);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

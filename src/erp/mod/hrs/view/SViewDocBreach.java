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
public class SViewDocBreach extends SGridPaneView {

    public SViewDocBreach(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_DOC_BREACH, 0, title);
        setRowButtonsEnabled(true, true, true, false, true);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "db.b_del = 0 ";
        }

        msSql = "SELECT "
                + "db.id_doc_breach AS " + SDbConsts.FIELD_ID + "1, "
                + "db.num, "
                + "db.num AS " + SDbConsts.FIELD_CODE + ", "
                + "db.num AS " + SDbConsts.FIELD_NAME + ", "
                + "db.breach_ts, "
                + "db.breach_abstract, "
                + "db.filevault_id, "
                + "db.filevault_ts_n, "
                + "db.b_offender_uni, "
                + "cob.code AS _cob_code, "
                + "ea.bp AS _emp_author, "
                + "eo.bp AS _emp_offender, "
                + "eb.bp AS _emp_boss, "
                + "eu.bp AS _emp_union_n, "
                + "dep.name AS _dep_name, "
                + "pos.name AS _pos_name, "
                + "db.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "db.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "db.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "db.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "db.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DOC_BREACH) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cob ON cob.id_bpb = db.fk_cob "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS ea ON ea.id_emp = db.fk_emp_author "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS eo ON eo.id_emp = db.fk_emp_offender "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS eb ON eb.id_emp = db.fk_emp_boss "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON dep.id_dep = db.fk_offender_dep "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_POS) + " AS pos ON pos.id_pos = db.fk_offender_pos "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON db.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON db.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS eu ON eu.id_emp = db.fk_emp_union_n "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY db.num, db.id_doc_breach ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "db.num", "Folio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "cob.code", "Sucursal"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "db.breach_ts", "Fecha-hora infracción"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "db.breach_abstract", "Infracción"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "_emp_offender", "Infractor"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_dep_name", "Departamento infractor"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_pos_name", "Puesto infractor"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "db.b_offender_uni", "Sindicalizado infractor"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "_emp_boss", "Jefe inmediato"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "_emp_autor", "Reportó"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "_emp_union_n", "Sindicato"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "db.filevault_id", "ID documento"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "db.filevault_ts_n", "TS documento"));
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
        moSuscriptionsSet.add(SModConsts.HRSU_DEP);
        moSuscriptionsSet.add(SModConsts.HRSU_POS);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

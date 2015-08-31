/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
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
public class SViewAccountItemLink extends SGridPaneView {

    public SViewAccountItemLink(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.FIN_ACC_ITEM_LINK, SLibConsts.UNDEFINED, title);
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
                + "v.id_acc AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_link AS " + SDbConsts.FIELD_ID + "2, "
                + "v.id_ref AS " + SDbConsts.FIELD_ID + "3, "
                + "' ' AS " + SDbConsts.FIELD_CODE + ", "
                + "vi.acc AS " + SDbConsts.FIELD_NAME + ", "
                + "vl.name,"
                + "CASE v.id_link "
                + "WHEN " + SModSysConsts.ITMS_LINK_ALL + " THEN "
                + "('(TODOS)') "
                + "WHEN " + SModSysConsts.ITMS_LINK_CT_ITEM + " THEN "
                + "(SELECT r.ct_item FROM " + SModConsts.TablesMap.get(SModConsts.ITMS_CT_ITEM) + " AS r WHERE v.id_ref = r.ct_idx) "
                + "WHEN " + SModSysConsts.ITMS_LINK_CL_ITEM + " THEN "
                + "(SELECT r.cl_item FROM " + SModConsts.TablesMap.get(SModConsts.ITMS_CL_ITEM) + " AS r WHERE v.id_ref = r.cl_idx) "
                + "WHEN " + SModSysConsts.ITMS_LINK_TP_ITEM + " THEN "
                + "(SELECT r.tp_item FROM " + SModConsts.TablesMap.get(SModConsts.ITMS_TP_ITEM) + " AS r WHERE v.id_ref = r.tp_idx) "
                + "WHEN " + SModSysConsts.ITMS_LINK_IFAM + " THEN "
                + "(SELECT r.ifam FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_IFAM) + " AS r WHERE v.id_ref = r.id_ifam) "
                + "WHEN " + SModSysConsts.ITMS_LINK_IGRP + " THEN "
                + "(SELECT r.igrp FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_IGRP) + " AS r WHERE v.id_ref = r.id_igrp) "
                + "WHEN " + SModSysConsts.ITMS_LINK_IGEN + " THEN "
                + "(SELECT CONCAT(r.igen, ' (', r.code, ')') FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS r WHERE v.id_ref = r.id_igen) "
                + "WHEN " + SModSysConsts.ITMS_LINK_LINE + " THEN "
                + "(SELECT CONCAT(r.line, ' (', r.code, ')') FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_LINE) + " AS r WHERE v.id_ref = r.id_line) "
                + "WHEN " + SModSysConsts.ITMS_LINK_BRD + " THEN "
                + "(SELECT CONCAT(r.brd, ' (', r.code, ')') FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_BRD) + " AS r WHERE v.id_ref = r.id_brd) "
                + "WHEN " + SModSysConsts.ITMS_LINK_MFR + " THEN "
                + "(SELECT CONCAT(r.mfr, ' (', r.code, ')') FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_MFR) + " AS r WHERE v.id_ref = r.id_mfr) "
                + "WHEN " + SModSysConsts.ITMS_LINK_ITEM + " THEN "
                + "(SELECT CONCAT(r.item, ' (', r.item_key, ')') FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS r WHERE v.id_ref = r.id_item) "
                + "ELSE '?' END AS f_ref, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ACC_ITEM_LINK) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS vi ON "
                + "v.id_acc = vi.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMS_LINK) + " AS vl ON "
                + "v.id_link = vl.id_link "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY vi.acc, vl.name, f_ref, v.id_acc, v.id_link, v.id_ref ";
        
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, "Cuenta contable"));
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
        moSuscriptionsSet.add(SModConsts.ITMS_CT_ITEM);
        moSuscriptionsSet.add(SModConsts.ITMS_CL_ITEM);
        moSuscriptionsSet.add(SModConsts.ITMS_TP_ITEM);
        moSuscriptionsSet.add(SModConsts.ITMU_IFAM);
        moSuscriptionsSet.add(SModConsts.ITMU_IGRP);
        moSuscriptionsSet.add(SModConsts.ITMU_IGEN);
        moSuscriptionsSet.add(SModConsts.ITMU_LINE);
        moSuscriptionsSet.add(SModConsts.ITMU_BRD);
        moSuscriptionsSet.add(SModConsts.ITMU_MFR);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.FIN_ACC);
        moSuscriptionsSet.add(SModConsts.ITMS_LINK);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.qlt.view;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Edwin Carmona
 */
public class SViewDatasheetTemplateLink extends SGridPaneView implements ActionListener {
    public SViewDatasheetTemplateLink(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.QLT_DATASHEET_TEMPLATE_LINK, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(true, false, true, false, true);
        initComponetsCustom();
    }

    private void initComponetsCustom() {
        
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
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT "
                + "v.id_datasheet_template_link AS " + SDbConsts.FIELD_ID + "1, "
                + "v.cfg_name, "
                + "dtemp.template_version AS " + SDbConsts.FIELD_CODE + ", "
                + "dtemp.template_name AS " + SDbConsts.FIELD_NAME + ", "
                + "dtemp.dt_date AS " + SDbConsts.FIELD_DATE + ", "
                + "dtemp.template_standard, "
                + "tl.tp_link AS f_link, "
                + "CASE v.fk_tp_link "
                + "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ALL + " THEN "
                + "('" + SDataConstantsSys.TXT_TRNS_TP_LINK_ALL + "') "
                + "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CT_ITEM + " THEN ("
                + "SELECT c.ct_item FROM erp.itms_ct_item c WHERE c.ct_idx = v.fk_ref) "
                + "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CL_ITEM + " THEN ("
                + "SELECT c.cl_item FROM erp.itms_cl_item c WHERE c.cl_idx = v.fk_ref) "
                + "WHEN " + SDataConstantsSys.TRNS_TP_LINK_TP_ITEM + " THEN ("
                + "SELECT tp.tp_item FROM erp.itms_tp_item tp WHERE tp.tp_idx = v.fk_ref) "
                + "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IFAM + " THEN ("
                + "SELECT ifam.ifam FROM erp.itmu_ifam ifam WHERE ifam.id_ifam = v.fk_ref) "
                + "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGRP + " THEN ("
                + "SELECT igrp.igrp FROM erp.itmu_igrp igrp WHERE igrp.id_igrp = v.fk_ref) "
                + "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGEN + " THEN ("
                + "SELECT CONCAT(igen.igen, ' (', igen.code, ')') FROM erp.itmu_igen igen WHERE igen.id_igen = v.fk_ref) "
                + "WHEN " + SDataConstantsSys.TRNS_TP_LINK_LINE + " THEN ("
                + "SELECT line.line FROM erp.itmu_line line WHERE line.id_line = v.fk_ref) "
                + "WHEN " + SDataConstantsSys.TRNS_TP_LINK_BRD + " THEN ("
                + "SELECT CONCAT(brd.brd, ' (', brd.code, ')') FROM erp.itmu_brd brd WHERE brd.id_brd = v.fk_ref) "
                + "WHEN " + SDataConstantsSys.TRNS_TP_LINK_MFR + " THEN ("
                + "SELECT CONCAT(mfr.mfr, ' (', mfr.code, ')') FROM erp.itmu_mfr mfr WHERE mfr.id_mfr = v.fk_ref) "
                + "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ITEM + " THEN ("
                + "SELECT CONCAT(item.item_key, ' - ', item.item) "
                + "FROM erp.itmu_item item WHERE item.id_item = v.fk_ref) "
                + "ELSE 'No existe tipo' "
                + "END AS f_ref, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.QLT_DATASHEET_TEMPLATE_LINK) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.QLT_DATASHEET_TEMPLATE) + " AS dtemp ON "
                + "v.fk_datasheet_template = dtemp.id_datasheet_template "
                + "INNER JOIN erp.trns_tp_link AS tl ON "
                + "v.fk_tp_link = tl.id_tp_link "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY dtemp.template_name ASC, dtemp.dt_date ASC ";
    }
    
    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, "Nombre ficha técnica"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, "Version ficha técnica"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "dtemp.template_standard", "Estandard"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "cfg_name", "Nombre configuración"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_link", "Tipo configuración"));
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
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

        }
    }
}

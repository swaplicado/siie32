/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.view;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import java.util.ArrayList;
import java.util.Arrays;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Néstor Ávalos, Isabel Servín, Sergio Flores
 */
public class SViewSpot extends SGridPaneView {

    public SViewSpot(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.LOGU_SPOT, 0, title);
        setButtonsEnabledByPrivilege();
    }
    
    private void setButtonsEnabledByPrivilege() {
        if (miClient.getSession().getUser().hasPrivilege(SDataConstantsSys.PRV_LOG_RATE)){
            setButtonsEnabled(miClient.getSession().getUser().getPrivilegeLevel(SDataConstantsSys.PRV_LOG_RATE));
        }
        else if (miClient.getSession().getUser().hasPrivilege(SDataConstantsSys.PRV_LOG_MISC)){
            setButtonsEnabled(miClient.getSession().getUser().getPrivilegeLevel(SDataConstantsSys.PRV_LOG_MISC));
        }
    }
    private void setButtonsEnabled (int level) {
        switch (level) {
            case SUtilConsts.LEV_READ:
                setRowButtonsEnabled(false);
                break;
            case SUtilConsts.LEV_CAPTURE:
            case SUtilConsts.LEV_AUTHOR:
                setRowButtonsEnabled(true, true, false, false, false);
                break;
            case SUtilConsts.LEV_EDITOR:
                setRowButtonsEnabled(true, true, true, false, false);
                break;
            case SUtilConsts.LEV_MANAGER:
                setRowButtonsEnabled(true, true, true, false, true);
                break;
            default:
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        moPaneSettings.setDisableableApplying(false);

        jbRowDisable.setEnabled(false);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT "
                + "v.id_spot AS " + SDbConsts.FIELD_ID + "1, "
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.district, "
                + "v.reference, "
                + "v.locality_code, "
                + "v.locality, "
                + "v.county_code, "
                + "v.county, "
                + "v.state, "
                + "v.zip_code, "
                + "v.b_gen, "
                + "tp.code AS f_tp_code, "
                + "dly.code AS f_tp_dly, "
                + "cty.cty AS _cty, "
                + "COALESCE(sta.sta, v.state) AS _sta, "
                + "sta.sta_code AS _sta_code, "
                + "nzc.nei_code, "
                + "nzc.zip_code, "
                + "nzc.description, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_SPOT) + " AS tp ON v.fk_tp_spot = tp.id_tp_spot "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_DLY) + " AS dly ON v.fk_tp_dly = dly.id_tp_dly "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON v.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOCU_CTY) + " AS cty ON cty.id_cty = v.fk_cty_n "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOCU_STA) + " AS sta ON sta.id_sta = v.fk_sta_n "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOCS_BOL_NEI_ZIP_CODE) + " AS nzc ON nzc.id_nei_zip_code = v.fk_nei_zip_code_n "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.name, v.code, v.id_spot ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[21];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_tp_dly", "Tipo entrega");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_tp_code", "Tipo lugar");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_gen", "Genérico");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "v.district", "Colonia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.zip_code", "Código postal");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.locality", "Localidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.county", "Municipio");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.reference", "Referencia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_sta", "Estado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_cty", "País");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "nzc.nei_code", "Colonia SAT");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.locality_code", "Localidad SAT");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.county_code", "Municipio SAT");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_sta_code", "Estado SAT");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);

        gridColumnsViews.addAll(Arrays.asList((SGridColumnView[]) columns));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

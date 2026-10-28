/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.view;

import erp.mod.SModConsts;
import java.util.ArrayList;
import java.util.Arrays;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Isabel Servín, Sergio Flores
 */
public class SViewBolPerson extends SGridPaneView {

    public SViewBolPerson(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.LOG_BOL_PERSON, 0, title);
        setRowButtonsEnabled(true, false, true, false, true);
        
        initComponetsCustom();
    }

    private void initComponetsCustom() {
        
    }

    /*
     * Public methods:
     */

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDateApplying(false);

        jbRowDisable.setEnabled(false);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "p.b_del = 0 ";
        }

        msSql = "SELECT "
                + "p.id_bol_person AS " + SDbConsts.FIELD_ID + "1, " 
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "p.name AS " + SDbConsts.FIELD_NAME + ", "
                + "tp.name AS person_tp, "
                + "p.fiscal_id, "
                + "p.fiscal_frg_id, "
                + "p.driver_lic, "
                + "p.driver_lic_exp_dt_n, "
                + "p.telephone, "
                + "p.street, "
                + "p.street_num_ext, "
                + "p.street_num_int, "
                + "p.neighborhood, "
                + "p.reference, "
                + "p.locality, "
                + "p.state, "
                + "p.zip_code, "
                + "cty.cty, "
                + "sta.sta, "
                + "car.bp, "
                + "p.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " 
                + "p.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", " 
                + "p.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", " 
                + "p.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", " 
                + "p.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", " 
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_PERSON) + " AS p " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_BOL_PERSON) + " AS tp ON p.fk_tp_bol_person = tp.id_tp_bol_person "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON p.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON p.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOCU_CTY) + " AS cty ON cty.id_cty = p.fk_cty_n "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOCU_STA) + " AS sta ON sta.id_sta = p.fk_sta_n "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_DRIVER_BP_CAR) + " AS drvcar ON drvcar.id_driver = p.id_bol_person AND NOT drvcar.b_del "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS car ON car.id_bp = drvcar.id_driver "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY p.name, p.id_bol_person ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[23];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, "Nombre");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "person_tp", "Tipo figura");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "p.fiscal_id", "RFC");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "p.fiscal_frg_id", "ID fiscal");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "p.driver_lic", "Licencia chofer");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "p.driver_lic_exp_dt_n", "Vencimiento licencia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "car.bp", "Transportista");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "p.telephone", "Teléfono");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "p.street", "Calle");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "p.street_num_ext", "No. exterior");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "p.street_num_int", "No. interior");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "p.neighborhood", "Colonia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "p.reference", "Referencia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "p.locality", "Localidad");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "p.state", "Estado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "p.zip_code", "Código postal");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "sta.sta", "Estado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "cty.cty", "País");
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
        moSuscriptionsSet.add(SModConsts.LOG_BOL_PERSON);
        moSuscriptionsSet.add(SModConsts.LOGS_TP_BOL_PERSON);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

}

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
 * @author Sergio Flores
 */
public class SViewDriverCarrier extends SGridPaneView {

    public SViewDriverCarrier(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.LOG_DRIVER_BP_CAR, 0, title);
        initComponetsCustom();
    }

    private void initComponetsCustom() {
        setRowButtonsEnabled(false, false, true, false, false);
        jtbFilterDeleted.setEnabled(false);
    }

    /*
     * Public methods:
     */

    @Override
    public void prepareSqlQuery() {
        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        msSql = "SELECT "
                + "dc.id_driver AS " + SDbConsts.FIELD_ID + "1, " 
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "p.name AS " + SDbConsts.FIELD_NAME + ", "
                + "p.fiscal_id, "
                + "p.fiscal_frg_id, "
                + "p.driver_lic, "
                + "p.driver_lic_exp_dt_n, "
                + "p.telephone, "
                + "car.bp, "
                + "car.bp_comm, "
                + "car.fiscal_id, "
                + "car.fiscal_frg_id, "
                + "dc.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " 
                + "dc.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", " 
                + "dc.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", " 
                + "dc.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", " 
                + "dc.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", " 
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_DRIVER_BP_CAR) + " AS dc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_PERSON) + " AS p ON p.id_bol_person = dc.id_driver "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS car ON car.id_bp = dc.id_car "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON dc.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON dc.fk_usr_upd = uu.id_usr "
                + "WHERE NOT dc.b_del "
                + "ORDER BY car.bp, dc.id_car, p.name, dc.id_driver;";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[15];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "car.bp", "Transportista");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "car.bp_comm", "Nombre comercial ransportista");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "p.fiscal_id", "RFC transportista");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "p.fiscal_frg_id", "ID fiscal transportista");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, "Chofer");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "p.fiscal_id", "RFC chofer");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "p.fiscal_frg_id", "ID fiscal chofer");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "p.driver_lic", "Licencia chofer");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "p.driver_lic_exp_dt_n", "Vencimiento licencia");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "p.telephone", "Teléfono");
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
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

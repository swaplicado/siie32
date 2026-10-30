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
public class SViewVehicleCarrier extends SGridPaneView {

    public SViewVehicleCarrier(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.LOG_VEH_BP_CAR, 0, title);
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
                + "vc.id_veh AS " + SDbConsts.FIELD_ID + "1, " 
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "v.plate, " 
                + "vt.name AS _type, "
                + "car.bp, "
                + "car.bp_comm, "
                + "car.fiscal_id, "
                + "car.fiscal_frg_id, "
                + "vc.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " 
                + "vc.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", " 
                + "vc.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", " 
                + "vc.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", " 
                + "vc.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", " 
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_VEH_BP_CAR) + " AS vc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_VEH) + " AS v ON v.id_veh = vc.id_veh "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_TP_VEH) + " AS vt ON vt.id_tp_veh = v.fk_tp_veh "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS car ON car.id_bp = vc.id_car "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON vc.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON vc.fk_usr_upd = uu.id_usr "
                + "WHERE NOT vc.b_del "
                + "ORDER BY car.bp, vc.id_car, v.name, vc.id_veh;";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[13];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "car.bp", "Transportista");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "car.bp_comm", "Nombre comercial ransportista");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "car.fiscal_id", "RFC transportista");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "car.fiscal_frg_id", "ID fiscal transportista");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME + " vehículo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE + " vehículo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_type", SGridConsts.COL_TITLE_TYPE + " vehículo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.plate", "Placas");
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
        moSuscriptionsSet.add(SModConsts.LOGU_TP_VEH);
        moSuscriptionsSet.add(SModConsts.LOG_VEH);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

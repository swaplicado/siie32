/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.view;

import erp.mod.SModConsts;
import erp.mod.log.db.SDbFreightTariff;
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
public class SViewFreightTariff extends SGridPaneView {

    public SViewFreightTariff(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.LOG_FREIGHT_TARIFF, 0, title);
        setRowButtonsEnabled(true, true, true, false, true);
        
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
            sql += (sql.isEmpty() ? "" : "AND ") + "ft.b_del = 0 ";
        }

        msSql = "SELECT " 
                + "ft.id_freight_tariff AS " + SDbConsts.FIELD_ID + "1, "
                + "LPAD(ft.id_freight_tariff, " + SDbFreightTariff.ID_LEN + ", '0') AS " + SDbConsts.FIELD_CODE + ", "
                + "LPAD(ft.id_freight_tariff, " + SDbFreightTariff.ID_LEN + ", '0') AS " + SDbConsts.FIELD_NAME + ", "
                + "CASE "
                + "WHEN ft.tariff_tp = '" + SDbFreightTariff.TYPE_BASE + "' THEN '" + SDbFreightTariff.Types.get(SDbFreightTariff.TYPE_BASE) + "' "
                + "WHEN ft.tariff_tp = '" + SDbFreightTariff.TYPE_CARRIER + "' THEN '" + SDbFreightTariff.Types.get(SDbFreightTariff.TYPE_CARRIER) + "' "
                + "WHEN ft.tariff_tp = '" + SDbFreightTariff.TYPE_VEHICLE + "' THEN '" + SDbFreightTariff.Types.get(SDbFreightTariff.TYPE_VEHICLE) + "' "
                + "ELSE '?' END AS _tariff_tp, "
                + "ts.code, "
                + "s.name, "
                + "td.name, "
                + "so.name, "
                + "so.code, "
                + "sd.name, "
                + "sd.code, "
                + "mt.name, "
                + "ss.name, "
                + "tv.name, "
                + "fc.name, "
                + "c.cur_key, "
                + "car.bp, "
                + "veh.name, "
                + "veh.code, "
                + "tveh.name, "
                + "tveh.code, "
                + "ft.b_by_base, "
                + "IF(ft.b_by_base, bft.tariff, ft.tariff) AS _tariff, "
                + "IF(ft.b_by_base, bft.start, ft.start) AS _start, "
                + "LPAD(bft.id_freight_tariff, " + SDbFreightTariff.ID_LEN + ", '0') AS _base_tariff, "
                + "ft.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " 
                + "ft.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", " 
                + "ft.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", " 
                + "ft.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", " 
                + "ft.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", " 
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_FREIGHT_TARIFF) + " AS ft " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SERVICE) + " AS s ON s.id_service = ft.fk_service "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_SERVICE) + " AS ts ON ts.id_tp_service = s.fk_tp_service "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_DLY) + " AS td ON td.id_tp_dly = ft.fk_tp_dly "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS so ON so.id_spot = ft.fk_src_spot "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_MOT) + " AS mt ON mt.id_tp_mot = ft.fk_tp_mot "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_SHIP_SIZE) + " AS ss ON ss.id_tp_ship_size = ft.fk_tp_ship_size "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_FREIGHT_CLASS) + " AS fc ON fc.id_freight_class = ft.fk_freight_class "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON c.id_cur = ft.fk_cur "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON ui.id_usr = ft.fk_usr_ins "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON uu.id_usr = ft.fk_usr_upd "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS sd ON sd.id_spot = ft.fk_des_spot_n "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_TP_VEH) + " AS tv ON tv.id_tp_veh = ft.fk_tp_veh_n "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS car ON car.id_bp = ft.fk_car_n "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_VEH) + " AS veh ON veh.id_veh = ft.fk_veh_n "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_TP_VEH) + " AS tveh ON tveh.id_tp_veh = veh.fk_tp_veh "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_FREIGHT_TARIFF) + " AS bft ON bft.id_freight_tariff = ft.fk_base_tariff_n "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY ts.code, s.name, td.name, so.name, sd.name, mt.name, ss.name, tv.name, fc.name, car.bp, veh.name, _tariff, ft.id_freight_tariff ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[26];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "ts.code", SGridConsts.COL_TITLE_TYPE + " servicio", 35);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "s.name", "Servicio");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "td.name", SGridConsts.COL_TITLE_TYPE + " entrega", 75);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "so.name", "Origen", 150);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "so.code", SGridConsts.COL_TITLE_CODE + " origen");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "sd.name", "Destino", 150);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sd.code", SGridConsts.COL_TITLE_CODE + " destino");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "mt.name", "Modo transporte", 75);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ss.name", "Tamaño carga");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tv.name", SGridConsts.COL_TITLE_TYPE + " vehículo tarifa");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "fc.name", SGridConsts.COL_TITLE_CLASS + " flete tarifa", 75);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_tariff_tp", SGridConsts.COL_TITLE_TYPE + " tarifa", 50);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE + " tarifa", 75);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_tariff", "Tarifa");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "c.cur_key", "Moneda");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_start", "Inicio tarifa");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "car.bp", "Transportista", 150);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "veh.name", "Vehículo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tveh.name", SGridConsts.COL_TITLE_TYPE + " vehículo transportista");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "ft.b_by_base", "Aplica tarifa base");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "_base_tariff", SGridConsts.COL_TITLE_CODE + " tarifa base");
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
        moSuscriptionsSet.add(SModConsts.LOG_INSURER);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

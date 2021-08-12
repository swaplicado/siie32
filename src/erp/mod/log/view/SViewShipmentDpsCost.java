/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.view;

import erp.gui.grid.SGridFilterPanelFunctionalArea;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.table.SFilterConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateCutOff;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SViewShipmentDpsCost extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private SGridFilterDateCutOff moFilterDateCutOff;
    private SGridFilterPanelFunctionalArea moFilterFunctionalArea;

    public SViewShipmentDpsCost(SGuiClient client, int gridType, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, gridType, gridSubtype, title, params);
        initComponents();
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
    }

    private void initComponents() {

        if (mnGridSubtype == SModConsts.VIEW_ST_DONE) {
            moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
        else {
            moFilterDateCutOff = new SGridFilterDateCutOff(miClient, this);
            moFilterDateCutOff.initFilter(null);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateCutOff);
        }
        
        moFilterFunctionalArea = new SGridFilterPanelFunctionalArea(miClient, this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterFunctionalArea);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String sqlTotalWeightGross = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        moPaneSettings.setDisableableApplying(false);

        getComplementsMap().put(SModConsts.VIEW_ST_REG, SGridConsts.COL_TYPE_BOOL_M);

        if (mnGridSubtype == SModConsts.VIEW_ST_PEND) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();

            if (filter != null) {
                sql += (sql.isEmpty() ? "" : "AND ") + " v.dt <= '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
            }
        }
        else if (mnGridSubtype == SModConsts.VIEW_ST_DONE) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
        }
        
        filter = (String) (moFiltersMap.get(SFilterConstants.SETTING_FILTER_FUNC_AREA) == null ? null : moFiltersMap.get(SFilterConstants.SETTING_FILTER_FUNC_AREA).getValue());
        if (filter != null && !((String) filter).isEmpty()) {
            sql += (sql.isEmpty() ? "" : "AND ") + " d.fid_func IN ( " + filter + ") ";
        }

        sqlTotalWeightGross = "(SELECT SUM(sde.weight_gross) FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " AS s "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " AS sd ON s.id_ship = sd.id_ship "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS sde ON sd.id_ship = sde.id_ship AND sd.id_dest = sde.id_dest "
            + "WHERE s.id_ship = v.id_ship "
            + "GROUP BY s.id_ship)";

        msSql = "SELECT "
            + "v.id_ship AS " + SDbConsts.FIELD_ID + "1, "
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "CONCAT(dly.code, '-', v.num) AS " + SDbConsts.FIELD_NAME + ", "
            + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
            + "v.b_bol AS " + SDbConsts.FIELD_COMP + "1, "
            + "d.id_year, "
            + "d.id_doc, "
            + "CONCAT(d.num_ser, IF(LENGTH(d.num_ser) > 0, '-', ''), d.num) AS f_sal_num, "
            + "d.dt, "
            + "bps.bp, "
            + "cts.bp_key, "
            + "v.cap_vol, "
            + "v.cap_vol_used_r, "
            + "IF(v.cap_vol = 0, 0, v.cap_vol_used_r / v.cap_vol) AS f_per_cap_vol, "
            + "v.cap_mass, "
            + "v.cap_mass_used_r, "
            + "IF(v.cap_mass = 0, 0, v.cap_mass_used_r / v.cap_mass) AS f_per_cap_mass, "
            + "CONCAT(ord.num_ser, IF(LENGTH(ord.num_ser) > 0, '-', ''), ord.num) AS f_ord_num, "
            + "CONCAT(dps.num_ser, IF(LENGTH(dps.num_ser) > 0, '-', ''), dps.num) AS f_sup_num, "
            + "v.cost_cur_r, "
            + "dps.stot_cur_r, "
            + "cur.cur_key, "
            + "SUM(sde.weight_gross), "
            + "IF(" + sqlTotalWeightGross + " > 0, dps.stot_cur_r / " + sqlTotalWeightGross + ", 0) AS f_real_cost_kg, "
            + "v.b_del, "
            + "dly.code, "
            + "sup.id_des_year, "
            + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
            + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
            + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
            + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
            + "(SELECT cob.code FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cob WHERE v.fk_cob = cob.id_bpb) AS f_cob_code, "
            + "(SELECT ship.code FROM " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_SHIP) + " AS ship WHERE v.fk_tp_ship = ship.id_tp_ship) AS f_ship_code, "
            + "(SELECT inc.code FROM " + SModConsts.TablesMap.get(SModConsts.LOGS_INC) + " AS inc WHERE v.fk_inc = inc.id_inc) AS f_inc_code, "
            + "(SELECT src.code FROM " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS src WHERE v.fk_spot_src = src.id_spot) AS f_src_code, "
            + "(SELECT des.code FROM " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS des WHERE v.fk_spot_des = des.id_spot) AS f_des_code, "
            + "(SELECT aut.st_dps_authorn FROM " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_DPS_AUTHORN) + " AS aut WHERE v.fk_st_ship_authorn = aut.id_st_dps_authorn) AS f_st_dps_authorn, "
            + "(SELECT code FROM " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_CAR) + " WHERE v.fk_tp_car = id_tp_car) AS f_car_code, "
            + "(SELECT car.bp FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS car WHERE v.fk_car = car.id_bp) AS car_bp, "
            + "(SELECT ct.bp_key FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS ct WHERE v.fk_car = ct.id_bp AND ct.id_ct_bp = " + SModSysConsts.BPSS_CT_BP_SUP + ") AS car_bp_key, "
            + "(SELECT code FROM " + SModConsts.TablesMap.get(SModConsts.LOGU_TP_VEH) + " WHERE v.fk_tp_veh = id_tp_veh) AS f_tp_code, "
            + "(SELECT code FROM " + SModConsts.TablesMap.get(SModConsts.LOG_VEH) + " WHERE v.fk_veh_n = id_veh) AS f_veh_code, "
            + "(SELECT scur.cur_key FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS scur WHERE v.fk_cur = scur.id_cur) AS scur_cur_key, "
            + "(SELECT symbol FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " WHERE v.fk_unit_cap_vol = id_unit) AS f_uvol_symbol,  "
            + "(SELECT symbol FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " WHERE v.fk_unit_cap_mass = id_unit) AS f_umas_symbol, "
            + "IF(" + sqlTotalWeightGross + " > 0, v.cost_cur_r / " + sqlTotalWeightGross + ", 0) AS f_cost_kg "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " AS v "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " AS sd ON v.id_ship = sd.id_ship "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS sde ON sd.id_ship = sde.id_ship AND sd.id_dest = sde.id_dest "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON sde.fk_dps_year_n = de.id_year AND sde.fk_dps_doc_n = de.id_doc AND "
            + "sde.fk_dps_ety_n = de.id_ety "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON de.id_year = d.id_year AND de.id_doc = d.id_doc "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bps ON d.fid_bp_r = bps.id_bp "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS cts ON d.fid_bp_r = cts.id_bp AND cts.id_ct_bp = " + SModSysConsts.BPSS_CT_BP_CUS + " "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_DLY) + " AS dly ON v.fk_tp_dly = dly.id_tp_dly "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON v.fk_usr_ins = ui.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON v.fk_usr_upd = uu.id_usr "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_VEH) + " AS veh ON v.fk_veh_n = veh.id_veh "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS ord ON v.fk_ord_year_n = ord.id_year AND v.fk_ord_doc_n = ord.id_doc "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " AS sup ON sup.id_src_year = ord.id_year AND sup.id_src_doc = ord.id_doc "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS dps ON sup.id_des_year = dps.id_year AND sup.id_des_doc = dps.id_doc "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS cur ON dps.fid_cur = cur.id_cur "
            + "WHERE v.b_del = 0 "
            + (mnGridSubtype == SModConsts.VIEW_ST_PEND ? "AND (IF(sup.id_des_year IS NULL, v.b_bol = 0, sup.id_des_year IS NULL)) " : "AND (IF(sup.id_des_year IS NULL, v.b_bol = 1, sup.id_des_year IS NOT NULL)) ")
            + (sql.isEmpty() ? "" : "AND " + sql) + " "
            + "GROUP BY v.id_ship, d.id_year, d.id_doc, dps.id_year, dps.id_doc " //, v.num, v.dt, " + SDbConsts.FIELD_ID + "1 "
            + "ORDER BY v.num, v.dt, " + SDbConsts.FIELD_ID + "1 ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[mnGridSubtype == SModConsts.VIEW_ST_PEND ? 34 : 40];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_NAME, "Folio embarque");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE + " embarque");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_st_dps_authorn", "Estado aut embarque");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_cob_code", "Sucursal embarque");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_ship_code", "Tipo embarque");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "dly.code", "Tipo entrega");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_inc_code", "Entrega (Incoterm)");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_src_code", "Lugar origen");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_des_code", "Lugar destino");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_sal_num", "Folio fac vta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE + " fac vta");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "bps.bp", "Cliente");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "cts.bp_key", "Clave cliente");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_car_code", "Tipo trans");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "car_bp", "Transportista");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "car_bp_key", "Clave transportista");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_tp_code", "Tipo vehículo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_veh_code", "Vehículo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.cap_vol", "Cap vol");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_uvol_symbol", "Unidad cap vol");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "f_per_cap_vol", "Cap vol util");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.cap_mass", "Cap masa");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_umas_symbol", "Unidad cap masa");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "f_per_cap_mass", "Cap masa util");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.cost_cur_r", "Total estimado embarque $");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "scur_cur_key", "Moneda");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_cost_kg", "Costo estimado x kg");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "scur_cur_key", "Moneda");

        if (mnGridSubtype == SModConsts.VIEW_ST_DONE) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_ord_num", "Orden compra");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_sup_num", "Factura transportista");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "dps.stot_cur_r", "Subtotal fac trans");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "cur.cur_key", "Moneda");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_real_cost_kg", "Costo real x kg");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "cur.cur_key", "Moneda");
        }

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_COMP + "1", "Facturado");
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
        moSuscriptionsSet.add(mnGridSubtype);
        moSuscriptionsSet.add(mnGridMode);
        moSuscriptionsSet.add(SModConsts.BPSU_BPB);
        moSuscriptionsSet.add(SModConsts.LOGU_SPOT);
        moSuscriptionsSet.add(SModConsts.ITMU_UNIT);
        moSuscriptionsSet.add(SModConsts.LOGU_TP_VEH);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.LOG_VEH);
        moSuscriptionsSet.add(SModConsts.TRN_DPS);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

        }
    }
}

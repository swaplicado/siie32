/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.log.db.SDbShipment;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateCutOff;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Juan Barajas
 */
public class SViewShipmentBill extends SGridPaneView implements ActionListener {

    private SClientInterface miClient_xxx;
    private JButton mjbCloseBilling;
    private JButton mjbOpenBilling;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SGridFilterDateCutOff moFilterDateCutOff;

    public SViewShipmentBill(SGuiClient client, SClientInterface client_xxx, int gridType, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, gridType, gridSubtype, title, params);
        initComponents();
        miClient_xxx = client_xxx;

        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
    }

    private void initComponents() {
        mjbCloseBilling = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_CLOSE));
        mjbOpenBilling = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_OPEN));

        mjbCloseBilling.setPreferredSize(new Dimension(23, 23));
        mjbOpenBilling.setPreferredSize(new Dimension(23, 23));

        mjbCloseBilling.addActionListener(this);
        mjbOpenBilling.addActionListener(this);

        mjbCloseBilling.setToolTipText("Cerrar para facturación");
        mjbOpenBilling.setToolTipText("Abrir para facturación");

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
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbOpenBilling);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbCloseBilling);

        mjbOpenBilling.setEnabled(mnGridSubtype == SModConsts.VIEW_ST_DONE);
        mjbCloseBilling.setEnabled(mnGridSubtype != SModConsts.VIEW_ST_DONE);
    }

    private void actionCloseBill() {
        SDbShipment shipment = new SDbShipment();

        if (mjbCloseBilling.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else {
                    try {
                        if (miClient.showMsgBoxConfirm("El embarque quedará 'cerrado para facturación',\n "+ SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION) {

                            shipment.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                            if (shipment.getQueryResultId() == SDbConsts.READ_ERROR) {

                                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + shipment.getQueryResult());
                            }
                            else {
                                if (!shipment.canSave(miClient.getSession())) {

                                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + shipment.getQueryResult());
                                }
                                else {

                                    shipment.setFkUserUpdateId(miClient.getSession().getUser().getPkUserId());
                                    shipment.saveField(miClient.getSession().getStatement(), gridRow.getRowPrimaryKey(), SDbShipment.FIELD_BILL_OF_LADING, true);
                                }
                            }

                            miClient.getSession().notifySuscriptors(mnGridType);
                            miClient.getSession().notifySuscriptors(mnGridSubtype);
                            miClient.getSession().notifySuscriptors(mnGridMode);
                        }
                    }
                    catch (SQLException e) {
                        SLibUtils.showException(this, e);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    private void actionOpenBill() {
        SDbShipment shipment = new SDbShipment();

        if (mjbOpenBilling.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else {
                    try {
                        if (!(boolean) gridRow.getComplementsMap().get(SModConsts.VIEW_ST_REG)) {
                            miClient.showMsgBoxInformation("No se puede abrir un embarque que no ha sido cerrado de forma manual.");
                        }
                        else {
                            if (miClient.showMsgBoxConfirm("El embarque quedará 'abierto para facturación',\n "+ SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION) {

                                shipment.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                                if (shipment.getQueryResultId() == SDbConsts.READ_ERROR) {

                                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + shipment.getQueryResult());
                                }
                                else {
                                    if (!shipment.canSave(miClient.getSession())) {

                                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + shipment.getQueryResult());
                                    }
                                    else {

                                        shipment.setFkUserUpdateId(miClient.getSession().getUser().getPkUserId());
                                        shipment.saveField(miClient.getSession().getStatement(), gridRow.getRowPrimaryKey(), SDbShipment.FIELD_BILL_OF_LADING, false);
                                    }
                                }

                                miClient.getSession().notifySuscriptors(mnGridType);
                                miClient.getSession().notifySuscriptors(mnGridSubtype);
                                miClient.getSession().notifySuscriptors(mnGridMode);
                            }
                        }
                    }
                    catch (SQLException e) {
                        SLibUtils.showException(this, e);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
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
            + "IF(" + sqlTotalWeightGross + " > 0, dps.stot_cur_r / " + sqlTotalWeightGross + ", 0) AS f_real_cost_kg, "
            + "scur.cur_key, "
            + "v.b_del, "
            + "cob.code, "
            + "ship.code, "
            + "dly.code, "
            + "inc.code, "
            + "src.code, "
            + "des.code, "
            + "sup.id_des_year, "
            + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
            + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
            + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
            + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
            + "(SELECT aut.st_dps_authorn FROM " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_DPS_AUTHORN) + " AS aut WHERE v.fk_st_ship_authorn = aut.id_st_dps_authorn) AS f_st_dps_authorn, "
            + "(SELECT code FROM " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_CAR) + " WHERE v.fk_tp_car = id_tp_car) AS f_car_code, "
            + "(SELECT car.bp FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS car WHERE v.fk_car = car.id_bp) AS car_bp, "
            + "(SELECT ct.bp_key FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS ct WHERE v.fk_car = ct.id_bp AND ct.id_ct_bp = " + SModSysConsts.BPSS_CT_BP_SUP + ") AS car_bp_key, "
            + "(SELECT code FROM " + SModConsts.TablesMap.get(SModConsts.LOGU_TP_VEH) + " WHERE v.fk_tp_veh = id_tp_veh) AS f_tp_code, "
            + "(SELECT code FROM " + SModConsts.TablesMap.get(SModConsts.LOG_VEH) + " WHERE v.fk_veh_n = id_veh) AS f_veh_code, "
            + "(SELECT symbol FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " WHERE v.fk_unit_cap_vol = id_unit) AS f_uvol_symbol,  "
            + "(SELECT symbol FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " WHERE v.fk_unit_cap_mass = id_unit) AS f_umas_symbol, "
            + "IF(" + sqlTotalWeightGross + " > 0, v.cost_cur_r / " + sqlTotalWeightGross + ", 0) AS f_cost_kg "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " AS v "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cob ON v.fk_cob = cob.id_bpb "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_SHIP) + " AS ship ON v.fk_tp_ship = ship.id_tp_ship "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_DLY) + " AS dly ON v.fk_tp_dly = dly.id_tp_dly "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_INC) + " AS inc ON v.fk_inc = inc.id_inc "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS src ON v.fk_spot_src = src.id_spot "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS des ON v.fk_spot_des = des.id_spot "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS scur ON v.fk_cur = scur.id_cur "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON v.fk_usr_ins = ui.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON v.fk_usr_upd = uu.id_usr "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_VEH) + " AS veh ON v.fk_veh_n = veh.id_veh "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS ord ON v.fk_ord_year_n = ord.id_year AND v.fk_ord_doc_n = ord.id_doc "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " AS sup ON sup.id_src_year = ord.id_year AND sup.id_src_doc = ord.id_doc "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS dps ON sup.id_des_year = dps.id_year AND sup.id_des_doc = dps.id_doc "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS cur ON dps.fid_cur = cur.id_cur "
            + "WHERE v.b_del = 0 "
            + (mnGridSubtype == SModConsts.VIEW_ST_PEND ? "AND (IF(sup.id_des_year IS NULL, v.b_bol = 0, sup.id_des_year IS NULL)) " : "AND (IF(sup.id_des_year IS NULL, v.b_bol = 1, sup.id_des_year IS NOT NULL)) ")
            + (sql.isEmpty() ? "" : "AND " + sql)
            + "ORDER BY v.num, v.dt, " + SDbConsts.FIELD_ID + "1 ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();
        SGridColumnView[] columns = new SGridColumnView[mnGridSubtype == SModConsts.VIEW_ST_PEND ? 30 : 36];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_NAME, "Folio");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_st_dps_authorn", "Estado aut");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "cob.code", "Sucursal");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "ship.code", "Tipo embarque");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "dly.code", "Tipo entrega");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "inc.code", "Incoterm");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "src.code", "Lugar origen");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "des.code", "Lugar destino");
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
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "scur.cur_key", "Moneda");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_cost_kg", "Costo estimado x kg");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "scur.cur_key", "Moneda");

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

            if (button == mjbCloseBilling) {
                actionCloseBill();
            }
            else if (button == mjbOpenBilling) {
                actionOpenBill();
            }
        }
    }
}

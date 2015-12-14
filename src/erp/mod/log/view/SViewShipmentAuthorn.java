/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.view;

import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.log.db.SDbShipment;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
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
 * @author Néstor Ávalos
 */
public class SViewShipmentAuthorn extends SGridPaneView implements ActionListener {

    private JButton mjbPrint;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private JButton mjbAuthorn;
    private JButton mjbReject;

    public SViewShipmentAuthorn(SGuiClient client, int gridType, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, gridType, gridSubtype, title, params);
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
        initComponetsCustom();
    }

    private void initComponetsCustom() {

        mjbPrint = new JButton(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_print.gif")));
        mjbPrint.setPreferredSize(new Dimension(23, 23));
        mjbPrint.addActionListener(this);
        mjbPrint.setToolTipText(SUtilConsts.TXT_PRINT + " embarque");

        if (mnGridSubtype == SModSysConsts.TRNS_ST_DPS_AUTHORN_AUTHORN && mnGridMode == SModConsts.VIEW_SC_SUM) {
            mjbPrint.setEnabled(true);
        }
        else {
            mjbPrint.setEnabled(false);
        }

        mjbAuthorn = new JButton(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_thumbs_up.gif")));
        mjbAuthorn.setPreferredSize(new Dimension(23, 23));
        mjbAuthorn.addActionListener(this);
        mjbAuthorn.setToolTipText("Autorizar embarque");

        mjbAuthorn.setEnabled(false);
        //if (mnGridMode == SModConsts.VIEW_SC_SUM) {
            if (mnGridSubtype == SModSysConsts.TRNS_ST_DPS_AUTHORN_PENDING || mnGridSubtype == SModSysConsts.TRNS_ST_DPS_AUTHORN_REJECT) {
                mjbAuthorn.setEnabled(true);
            }
        //}

        mjbReject = new JButton(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_thumbs_down.gif")));
        mjbReject.setPreferredSize(new Dimension(23, 23));
        mjbReject.addActionListener(this);
        mjbReject.setToolTipText("Rechazar embarque");

        mjbReject.setEnabled(false);
        //if (mnGridMode == SModConsts.VIEW_SC_SUM) {
            if (mnGridSubtype == SModSysConsts.TRNS_ST_DPS_AUTHORN_AUTHORN) {
                mjbReject.setEnabled(true);
            }
        //}

        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbAuthorn);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbReject);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbPrint);
    }

    private void actionPrint() {
        SDbShipment shipment = null;

        if (mjbPrint.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    HashMap<String, Object> map = null;

                    shipment = new SDbShipment();
                    shipment.read(miClient.getSession(), getSelectedGridRow().getRowPrimaryKey());
                    if (shipment.getFkShipmentAuthorizationStatusId() != SModSysConsts.TRNS_ST_DPS_AUTHORN_AUTHORN) {
                        miClient.showMsgBoxInformation("El embarque no se puede imprimir porque no está autorizado.");
                    }
                    else {
                        map = miClient.createReportParams();
                        map.put("nIdShip", shipment.getPkShipmentId());
                        map.put("nIdCt", SModSysConsts.BPSS_CT_BP_CUS);

                        miClient.getSession().printReport(SModConsts.LOGR_SHIP, SLibConsts.UNDEFINED, null, map);
                    }
                } catch (SQLException e) {
                    SLibUtils.showException(this, e);
                } catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionAuthornShipment(int nStatusAuthornId) {
        SDbShipment shipment = new SDbShipment();

        if ((nStatusAuthornId == SModSysConsts.TRNS_ST_DPS_AUTHORN_AUTHORN && mjbAuthorn.isEnabled()) ||
                (nStatusAuthornId == SModSysConsts.TRNS_ST_DPS_AUTHORN_REJECT && mjbReject.isEnabled())) {
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
                    if (miClient.showMsgBoxConfirm("El embarque será: '" +
                        (nStatusAuthornId == SModSysConsts.TRNS_ST_DPS_AUTHORN_AUTHORN ? "Autorizado" : "Rechazado") + "' "+ SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION) {
                        try {
                            shipment.saveField(miClient.getSession().getStatement(), gridRow.getRowPrimaryKey(), SDbShipment.FIELD_ST_DPS_AUTHORN, nStatusAuthornId);

                            miClient.getSession().notifySuscriptors(mnGridType);
                            miClient.getSession().notifySuscriptors(mnGridSubtype);
                            miClient.getSession().notifySuscriptors(mnGridMode);
                            miClient.getSession().notifySuscriptors(SModConsts.LOG_SHIP);
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
    }

    /*
     * Public methods:
     */

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

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter) + " ";

        sql += "AND v.fk_st_ship_authorn = " + mnGridSubtype + " ";

        msSql = "SELECT "
            + "v.id_ship AS " + SDbConsts.FIELD_ID + "1, "
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "CONCAT(td.code, '-', v.num) AS " + SDbConsts.FIELD_NAME + ", "
            + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
            + (mnGridMode == SModConsts.VIEW_SC_SUM ?
            "v.cust_exp, "
            + "v.carr_exp_frei, "
            + "v.carr_exp_stay, "
            + "v.carr_exp_oth, "
            + "v.upld_exp, "
            + "v.load_exp, "
            + "v.carr, "
            + "v.upld_imp, "
            + "v.insu, "
            + "v.load_imp, "
            + "v.carr_imp_frei, "
            + "v.carr_imp_stay, "
            + "v.carr_imp_oth, "
            + "v.cust_imp, "
            + "v.tari, "
            + "v.cost_r, "
            + "v.exc_rate, "
            + "v.cost_cur_r, " : "")
            + "v.cap_vol, "
            + "v.cap_vol_used_r, "
            + "IF(v.cap_vol = 0, 0, v.cap_vol_used_r / v.cap_vol) AS f_per_cap_vol, "
            + "v.cap_mass, "
            + "v.cap_mass_used_r, "
            + "IF(v.cap_mass = 0, 0, v.cap_mass_used_r / v.cap_mass) AS f_per_cap_mass, "
            + "v.driver, "
            + "v.plate, "
            + "v.b_con, "
            + "v.b_bol, "
            + "v.b_del, "
            + "v.rate, "
            + "IF (v.rate > 0, 1, 0) AS f_rate, "
            + "v.driver, "
            + "v.plate, "
            + "cob.code, "
            + "ts.code, "
            + "td.code, "
            + "inc.code, "
            + (mnGridMode == SModConsts.VIEW_SC_DET ?
            "tds.code, "
            + "tsp.code, "
            + "sp.code, " : "")
            + "'" + miClient.getSession().getSessionCustom().getLocalCurrencyCode() + "' AS f_cur_key_loc, "
            + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
            + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
            + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
            + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
            + "(SELECT code FROM " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " WHERE v.fk_spot_src = id_spot) AS f_src_code, "
            + "(SELECT code FROM " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " WHERE v.fk_spot_des = id_spot) AS f_des_code, "
            + "(SELECT code FROM " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_MOT) + " WHERE v.fk_tp_mot = id_tp_mot) AS f_mot_code, "
            + "(SELECT code FROM " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_CAR) + " WHERE v.fk_tp_car = id_tp_car) AS f_car_code, "
            + "(SELECT st_dps_authorn FROM " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_DPS_AUTHORN) + " WHERE v.fk_st_ship_authorn = id_st_dps_authorn) AS f_st_dps_authorn, "
            + "(SELECT symbol FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " WHERE v.fk_unit_cap_vol = id_unit) AS f_uvol_symbol,  "
            + "(SELECT symbol FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " WHERE v.fk_unit_cap_mass = id_unit) AS f_umas_symbol, "
            + "(SELECT code FROM " + SModConsts.TablesMap.get(SModConsts.LOGU_TP_VEH) + " WHERE v.fk_tp_veh = id_tp_veh) AS f_tp_code, "
            + "(SELECT bp FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " WHERE v.fk_car = id_bp) AS f_car, "
            + "(SELECT bp_key FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc WHERE v.fk_car = bc.id_bp AND bc.id_ct_bp = " + SModSysConsts.BPSS_CT_BP_SUP + " " + ") AS f_car_key, "
            + "(SELECT cur_key FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS cur WHERE v.fk_cur = cur.id_cur) AS f_cur_key, "
            + "(SELECT code FROM " + SModConsts.TablesMap.get(SModConsts.LOG_VEH) + " WHERE v.fk_veh_n = id_veh) AS f_veh_code, "
            + "(SELECT CONCAT(num_ser, IF(LENGTH(num_ser) > 0, ' - ', ''), num) FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " WHERE v.fk_ord_year_n = id_year AND v.fk_ord_doc_n = id_doc) AS f_ord_num "
            + (mnGridMode == SModConsts.VIEW_SC_DET ?
            ", (SELECT bp FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " WHERE vd.fk_cus_n = id_bp) AS f_cus, "
            + "(SELECT bpb FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " WHERE vd.fk_cus_bpb_n = id_bpb) AS f_bpb, "
            + "(SELECT CONCAT(bpb_add, IF(b_def, ' (P)', ''), ' - ', street, ' ', RTRIM(CONCAT(street_num_ext, ' ', "
            + "street_num_int)), '; CP ', zip_code, '; ', locality, ', ', state) AS f_add FROM "
            + SModConsts.TablesMap.get(SModConsts.BPSU_BPB_ADD) + " WHERE vd.fk_cus_bpb_n = id_bpb AND vd.fk_cus_add_n = id_add) AS f_add, "
            + "(SELECT bpb FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " WHERE vd.fk_wah_cob_n = id_bpb) AS f_ent_cob, "
            + "(SELECT ent FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " WHERE vd.fk_wah_cob_n = id_cob AND vd.fk_wah_ent_n = id_ent) AS f_ent " : "")
            + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " AS v "
            + (mnGridMode == SModConsts.VIEW_SC_DET ?
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " AS vd ON v.id_ship = vd.id_ship "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS vde ON vd.id_ship = vde.id_ship AND vd.id_dest = vde.id_dest " : "")
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cob ON v.fk_cob = cob.id_bpb "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_SHIP) + " AS ts ON v.fk_tp_ship = ts.id_tp_ship "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_DLY) + " AS td ON v.fk_tp_dly = td.id_tp_dly "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_INC) + " AS inc ON v.fk_inc = inc.id_inc "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON v.fk_usr_ins = ui.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON v.fk_usr_upd = uu.id_usr "
            + (mnGridMode == SModConsts.VIEW_SC_DET ?
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_DOC_SHIP) + " AS tds ON vd.fk_tp_doc_ship = tds.id_tp_doc_ship "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_SPOT) + " AS tsp ON vd.fk_tp_spot = tsp.id_tp_spot "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS sp ON vd.fk_spot = sp.id_spot " : "")
            + (sql.isEmpty() ? "" : "WHERE " + sql)
            + "ORDER BY v.num, v.dt ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[mnGridMode == SModConsts.VIEW_SC_SUM ? 51 : 17];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_NAME, "Folio");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "f_st_dps_authorn", "Estado aut");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "cob.code", "Sucursal");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "ts.code", "Tipo embarque");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "td.code", "Tipo entrega");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "inc.code", "Incoterm");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_src_code", "Lugar origen");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_des_code", "Lugar destino");

        if (mnGridMode == SModConsts.VIEW_SC_SUM) {

            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_car_code", "Tipo trans");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_car", "Transportista");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_car_key", "Clave transportista");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_tp_code", "Tipo vehículo");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_veh_code", "Vehículo");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.driver", "Chofer");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.plate", "Placas");
            //columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "f_rate", "Flete autorizado");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.rate", "Flete autorizado $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.cust_exp", "Declar exp aduana $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.carr_exp_frei", "Trans pto exp flete $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.carr_exp_stay", "Trans pto exp est $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.carr_exp_oth", "Trans pto exp otros $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.upld_exp", "Descarga pto exp $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.load_exp", "Carga pto exp $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.carr", "Trans pto imp mar/ae $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.upld_imp", "Descarga pto imp $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.insu", "Seguro $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.load_imp", "Carga pto imp. $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.carr_imp_frei", "Trans destino flete $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.carr_imp_stay", "Trans destino est $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.carr_imp_oth", "Trans destino otros $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.cust_imp", "Imp despacho aduana $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.tari", "Aranceles $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.cost_cur_r", "Total mon $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "f_cur_key", "Moneda");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.exc_rate", "T cambio");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.cost_r", "Total embarque $");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "f_cur_key_loc", "Moneda local");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.cap_vol", "Cap vol");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_uvol_symbol", "Unidad cap vol");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "f_per_cap_vol", "Cap vol util");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.cap_mass", "Cap masa");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_umas_symbol", "Unidad cap masa");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "f_per_cap_mass", "Cap masa util");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_ord_num", "Orden compra");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_con", "Consolidado");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_bol", "Facturado");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);
        }

        if (mnGridMode == SModConsts.VIEW_SC_DET) {

            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "tds.code", "Tipo doc emb");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "tsp.code", "Tipo lugar");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sp.code", "Lugar");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_cus", "Asociado negocios");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_bpb", "Sucursal asoc neg");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "f_add", "Domicilio entrega");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_ent_cob", "Sucursal mov inv");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_ent", "Almacén mov inv");
        }

        gridColumnsViews.addAll(Arrays.asList((SGridColumnView[]) columns));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.LOGU_TP_VEH);
        moSuscriptionsSet.add(SModConsts.LOGU_SPOT);
        moSuscriptionsSet.add(SModConsts.ITMU_UNIT);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.LOG_VEH);
        moSuscriptionsSet.add(SModConsts.TRN_DPS);
        moSuscriptionsSet.add(SModConsts.BPSU_BPB);
        moSuscriptionsSet.add(SModConsts.BPSU_BPB_ADD);
        moSuscriptionsSet.add(SModConsts.CFGU_COB_ENT);
        moSuscriptionsSet.add(SModConsts.LOG_SHIP);
        moSuscriptionsSet.add(SModConsts.LOG_SHIP_DEST);
        moSuscriptionsSet.add(SModConsts.LOG_SHIP_DEST_ETY);
        moSuscriptionsSet.add(SModConsts.LOGX_SHIP_DIOG);
        moSuscriptionsSet.add(SModConsts.LOGX_SHIP_DLY);
        moSuscriptionsSet.add(SModConsts.LOGX_SHIP_DPS);
        moSuscriptionsSet.add(SModConsts.LOGX_SHIP_DPS_ADJ);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjbPrint) {
                actionPrint();
            }
            else if (button == mjbAuthorn) {
                actionAuthornShipment(SModSysConsts.TRNS_ST_DPS_AUTHORN_AUTHORN);
            }
            else if (button == mjbReject) {
                actionAuthornShipment(SModSysConsts.TRNS_ST_DPS_AUTHORN_REJECT);
            }
        }
    }
}

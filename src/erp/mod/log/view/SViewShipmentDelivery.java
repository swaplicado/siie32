/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.view;

import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.log.db.SDbShipmentDestiny;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
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
public class SViewShipmentDelivery extends SGridPaneView implements ActionListener {

    private JButton mjbDelivery;
    private JButton mjbRevertDelivery;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SGridFilterDateCutOff moFilterDateCutOff;

    public SViewShipmentDelivery(SGuiClient client, int gridType, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, gridType, gridSubtype, title, params);
        initComponents();
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
    }

    private void initComponents() {
        mjbDelivery = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_CLOSE));
        mjbRevertDelivery = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_OPEN));

        mjbDelivery.setPreferredSize(new Dimension(23, 23));
        mjbRevertDelivery.setPreferredSize(new Dimension(23, 23));

        mjbDelivery.addActionListener(this);
        mjbRevertDelivery.addActionListener(this);

        mjbDelivery.setToolTipText("Entregar");
        mjbRevertDelivery.setToolTipText("Revertir entrega");

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
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbRevertDelivery);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbDelivery);

        mjbRevertDelivery.setEnabled(mnGridSubtype == SModConsts.VIEW_ST_DONE && mnGridMode == SModConsts.VIEW_SC_DET);
        mjbDelivery.setEnabled(mnGridSubtype != SModConsts.VIEW_ST_DONE && mnGridMode == SModConsts.VIEW_SC_DET);
    }

    private void actionDeliveryShipment() {
        if (mjbDelivery.isEnabled()) {
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
                    if (miClient.showMsgBoxConfirm("El destino quedará 'cerrado para entrega',\n " + SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION) {
                        miClient.getSession().getModule(SModConsts.MOD_LOG_N, SLibConsts.UNDEFINED).showForm(SModConsts.LOGX_SHIP_DLY, SLibConsts.UNDEFINED, new SGuiParams(SModConsts.LOG_SHIP, gridRow.getRowPrimaryKey()));
                    }
                }
            }
        }
    }

    private void actionRevertDelivery() {
        SDbShipmentDestiny destiny = new SDbShipmentDestiny();

        if (mjbRevertDelivery.isEnabled()) {
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
                    if (miClient.showMsgBoxConfirm("El destino quedará 'abierto para entrega',\n " + SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION) {
                        try {
                            destiny = (SDbShipmentDestiny) miClient.getSession().readRegistry(SModConsts.LOG_SHIP_DEST, gridRow.getRowPrimaryKey());

                            destiny.saveField(miClient.getSession().getStatement(), gridRow.getRowPrimaryKey(), SDbShipmentDestiny.FIELD_DELIVERY, false);
                            destiny.saveField(miClient.getSession().getStatement(), gridRow.getRowPrimaryKey(), SDbShipmentDestiny.FIELD_DATE_DELIVERY_REAL, destiny.getDateDelivery());

                            miClient.getSession().notifySuscriptors(mnGridType);
                            miClient.getSession().notifySuscriptors(mnGridSubtype);
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

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        moPaneSettings.setDisableableApplying(false);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        if (mnGridSubtype == SModConsts.VIEW_ST_PEND) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();

            if (filter != null) {
                sql += (sql.isEmpty() ? "" : "AND ") + " v.dt <= '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
            }

            sql += (sql.isEmpty() ? "" : "AND ") + "d.b_dly = 0 ";
        }
        else if (mnGridSubtype == SModConsts.VIEW_ST_DONE) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);

            sql += (sql.isEmpty() ? "" : "AND ") + "d.b_dly = 1 ";
        }

        msSql = "SELECT "
            + "v.id_ship AS " + SDbConsts.FIELD_ID + "1, "
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "CONCAT(dly.code, '-', v.num) AS " + SDbConsts.FIELD_NAME + ", "
            + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
            + "d.id_dest AS " + SDbConsts.FIELD_ID + "2, "
            + "v.b_bol, "
            + "v.b_del, "
            + "cob.code, "
            + "ship.code, "
            + "dly.code, "
            + "inc.code, "
            + "src.code, "
            + "des.code, "
            + "aut.st_dps_authorn, "
            + "d.dt_dly, "
            + "d.dt_dly_real, "
            + "d.appt_num, "
            + "d.appt_time, "
            + "d.appt_per, "
            + "ds.code, "
            + "db.bp, "
            + "dc.bpb, "
            + "CONCAT(dca.bpb_add, IF(dca.b_def, ' (P)', ''), ' - ', dca.street, ' ', RTRIM(CONCAT(dca.street_num_ext, ' ', dca.street_num_int)), '; CP ', dca.zip_code, '; ', dca.locality, ', ', dca.state) AS home, "
            + "wh.code, "
            + "cb.code, "
            + "d.appt_per, "
            + "d.b_dly, "
            + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
            + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
            + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
            + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
            + "(SELECT code FROM " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_CAR) + " WHERE v.fk_tp_car = id_tp_car) AS f_car_code, "
            + "(SELECT car.bp FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS car WHERE v.fk_car = car.id_bp) AS car_bp, "
            + "(SELECT ct.bp_key FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS ct WHERE v.fk_car = ct.id_bp AND ct.id_ct_bp = " + SModSysConsts.BPSS_CT_BP_SUP + ") AS car_bp_key "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " AS v "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " AS d ON v.id_ship = d.id_ship "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cob ON v.fk_cob = cob.id_bpb "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_SHIP) + " AS ship ON v.fk_tp_ship = ship.id_tp_ship "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_DLY) + " AS dly ON v.fk_tp_dly = dly.id_tp_dly "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_INC) + " AS inc ON v.fk_inc = inc.id_inc "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS src ON v.fk_spot_src = src.id_spot "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS des ON v.fk_spot_des = des.id_spot "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS ds ON d.fk_spot = ds.id_spot "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_DPS_AUTHORN) + " AS aut ON v.fk_st_ship_authorn = aut.id_st_dps_authorn "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON v.fk_usr_ins = ui.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON v.fk_usr_upd = uu.id_usr "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS db ON d.fk_cus_n = db.id_bp "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS dc ON d.fk_cus_bpb_n = dc.id_bpb "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB_ADD) + " AS dca ON d.fk_cus_bpb_n = dca.id_bpb AND dca.id_add = IF(d.fk_cus_add_n IS NULL, 1, d.fk_cus_add_n) AND dca.b_del = 0 "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS wh ON d.fk_wah_cob_n = wh.id_cob AND d.fk_wah_ent_n = wh.id_ent "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cb ON wh.id_cob = cb.id_bpb "
            + "WHERE v.b_del = 0 "
            + (sql.isEmpty() ? "" : "AND " + sql);

        if (mnGridMode == SModConsts.VIEW_SC_SUM) {
            msSql += "GROUP BY " + SDbConsts.FIELD_ID + "1, v.num, v.dt "
                    + "ORDER BY " + SDbConsts.FIELD_ID + "1, v.num, v.dt ";
        }
        else if (mnGridMode == SModConsts.VIEW_SC_DET) {
            msSql += "GROUP BY " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, v.num, v.dt "
                    + "ORDER BY v.num, v.dt, " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2 ";
        }
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        int cols = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();
        SGridColumnView[] columns = null;

        cols = mnGridMode == SModConsts.VIEW_SC_DET ? 30 : 18;

        columns = new SGridColumnView[cols];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_NAME, "Folio");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "aut.st_dps_authorn", "Estado aut");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "cob.code", "Sucursal");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "ship.code", "Tipo embarque");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "dly.code", "Tipo entrega");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "inc.code", "Incoterm");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "src.code", "Lugar origen");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "des.code", "Lugar destino");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_car_code", "Tipo trans");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "car_bp", "Transportista");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "car_bp_key", "Clave transportista");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_bol", "Facturado");
        if (mnGridMode == SModConsts.VIEW_SC_DET) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "d.dt_dly", "Fecha entrega");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "d.dt_dly_real", "Fecha entrega real");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ds.code", "Destino");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "db.bp", "Cliente");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "dc.bpb", "Sucursal");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "home", "Domicilio");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "wh.code", "Sucursal");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "cb.code", "Almacén");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "d.appt_num", "Número cita");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "d.appt_time", "Fecha-hora cita");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "d.appt_per", "Contacto");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "d.b_dly", "Entregado");
        }
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
        moSuscriptionsSet.add(SModConsts.LOG_SHIP_DEST);
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

            if (button == mjbDelivery) {
                actionDeliveryShipment();
            }
            else if (button == mjbRevertDelivery) {
                actionRevertDelivery();
            }
        }
    }
}

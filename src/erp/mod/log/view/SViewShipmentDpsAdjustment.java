/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.view;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.gui.grid.SGridFilterPanelFunctionalArea;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.log.db.SParamsShipment;
import erp.mtrn.data.SDataDps;
import erp.table.SFilterConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;
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
 * @author Néstor Ávalos, Sergio Flores
 */
public class SViewShipmentDpsAdjustment extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private SGridFilterDateCutOff moFilterDateCutOff;
    private SGridFilterPanelFunctionalArea moFilterFunctionalArea;

    private JButton moButtonShipmentNational;
    private JButton moButtonShipmentInternational;
    private JButton moButtonShipmentClose;
    private JButton moButtonShipmentOpen;

    public SViewShipmentDpsAdjustment(SGuiClient client, int gridType, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, gridType, gridSubtype, title, params);
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
        initComponetsCustom();
    }

    /*
     * Private methods:
     */

    private void initComponetsCustom() {

        if (mnGridSubtype == SModConsts.VIEW_ST_PEND) {
            moFilterDateCutOff = new SGridFilterDateCutOff(miClient, this);
            moFilterDateCutOff.initFilter((Date) null);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateCutOff);
        }
        else {
            moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }

        moButtonShipmentNational = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ship_nac_ret.gif")), "Embarcar dev. venta (Nacional)", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moButtonShipmentNational);

        moButtonShipmentInternational = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ship_int_ret.gif")), "Embarcar dev. venta (Internacional)", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moButtonShipmentInternational);

        moButtonShipmentClose = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_close.gif")), "Cerrar para embarque", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moButtonShipmentClose);

        moButtonShipmentOpen = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_open.gif")), "Abrir para embarque", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moButtonShipmentOpen);
        
        moFilterFunctionalArea = new SGridFilterPanelFunctionalArea(miClient, this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterFunctionalArea);

        switch (mnGridMode) {
            case  SModConsts.VIEW_SC_SUM:
            case SModConsts.VIEW_SC_DET:
                switch (mnGridSubtype) {
                    case SModConsts.VIEW_ST_PEND:
                        moButtonShipmentNational.setEnabled(true);
                        moButtonShipmentInternational.setEnabled(true);
                        moButtonShipmentClose.setEnabled(true);
                        moButtonShipmentOpen.setEnabled(false);
                        break;

                    case SModConsts.VIEW_ST_DONE:
                        moButtonShipmentNational.setEnabled(false);
                        moButtonShipmentInternational.setEnabled(false);
                        moButtonShipmentClose.setEnabled(false);
                        moButtonShipmentOpen.setEnabled(true);
                        break;

                    default:
                        // do nothing
                }
                break;

            default:
                // do nothing
        }
    }

    private void actionShipment(int paramDeliveryType) {
        SGuiParams params = null;
        SParamsShipment paramsShipment = null;
        SDataDps dps = null;

        if ((moButtonShipmentNational.isEnabled() && paramDeliveryType == SModSysConsts.LOGS_TP_DLY_DOM) ||
                (moButtonShipmentInternational.isEnabled() && paramDeliveryType == SModSysConsts.LOGS_TP_DLY_INT)) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView row = (SGridRowView) getSelectedGridRow();

                if (row.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (row.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + row.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else {
                    dps = new SDataDps();
                    dps.read(row.getRowPrimaryKey(), miClient.getSession().getStatement());

                    if (dps != null && dps.getIsSystem()) {

                        miClient.showMsgBoxInformation(SDbConsts.MSG_REG_ + row.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                    }
                    else {
                        params = new SGuiParams();
                        paramsShipment = new SParamsShipment();

                        paramsShipment.setFkShipmentTypeId(SModSysConsts.LOGS_TP_SHIP_REC);
                        paramsShipment.setFkDeliveryTypeId(paramDeliveryType);
                        paramsShipment.setFkDocShipmentTypeId(SModSysConsts.LOGS_TP_DOC_SHIP_DPS);
                        paramsShipment.setFkDpsYearId(row.getRowPrimaryKey()[0]);
                        paramsShipment.setFkDpsDocId(row.getRowPrimaryKey()[1]);
                        params.getParamsMap().put(SModConsts.LOGX_SHIP_DPS, paramsShipment);
                        miClient.getSession().getModule(SModConsts.MOD_LOG_N, SLibConsts.UNDEFINED).showForm(SModConsts.LOG_SHIP, SLibConsts.UNDEFINED, params);
                    }

                    miClient.getSession().notifySuscriptors(mnGridType);
                    miClient.getSession().notifySuscriptors(SModConsts.LOG_SHIP);
                    miClient.getSession().notifySuscriptors(SModConsts.LOGX_SHIP_AUTH);
                }
            }
        }
    }

    public void actionCloseDpsShipment(boolean close) {
        if (moButtonShipmentClose.isEnabled() || moButtonShipmentOpen.isEnabled()) {
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
                else if (!close && !(boolean) gridRow.getComplementsMap().get(SModConsts.VIEW_ST_REG)) {
                    miClient.showMsgBoxInformation("No se puede abrir un documento de ventas que no ha sido cerrado de forma manual.");
                }
                else if (miClient.showMsgBoxConfirm("El documento de ventas quedará " + (!close ? "abierto" : "cerrado") + " para embarcar. "+ SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION) {
                    Vector<Object> params = new Vector<Object>();

                    params.add(gridRow.getRowPrimaryKey()[0]);
                    params.add(gridRow.getRowPrimaryKey()[1]);
                    params.add(SDataConstantsSys.UPD_DPS_FL_SHIP);
                    params.add(close ? 1 : 0);
                    params.add(miClient.getSession().getUser().getPkUserId());
                    params = SDataUtilities.callProcedure((SClientInterface) miClient, SProcConstants.TRN_DPS_UPD, params, SLibConstants.EXEC_MODE_SILENT);

                    if (params.get(0).toString().compareTo("0") != 0) {
                        miClient.showMsgBoxInformation(params.get(1).toString());
                    }

                    miClient.getSession().notifySuscriptors(mnGridType);
                    miClient.getSession().notifySuscriptors(mnGridSubtype);
                    miClient.getSession().notifySuscriptors(mnGridMode);
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
        String sqlOrderByDoc = "";
        String sqlOrderByDocEty = "";

        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        moPaneSettings.setDisableableApplying(false);

        getComplementsMap().put(SModConsts.VIEW_ST_REG, SGridConsts.COL_TYPE_BOOL_M);

        jbRowDisable.setEnabled(false);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "d.b_del = 0 ";
        }

        if (mnGridSubtype == SModConsts.VIEW_ST_PEND) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();

            if (filter != null) {
                sql += (sql.isEmpty() ? "" : "AND ") + " d.dt <= '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
            }
        }
        else if (mnGridSubtype == SModConsts.VIEW_ST_DONE) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("d.dt", (SGuiDate) filter);
        }
        
        filter = (String) (moFiltersMap.get(SFilterConstants.SETTING_FILTER_FUNC_AREA) == null ? null : moFiltersMap.get(SFilterConstants.SETTING_FILTER_FUNC_AREA).getValue());
        if (filter != null && ! ((String) filter).isEmpty()) {
            sql += (sql.isEmpty() ? "" : "AND ") + " d.fid_func IN ( " + filter + ") ";
        }

        sqlOrderByDoc += "num_ser, CAST(num AS UNSIGNED INTEGER), num, f_dt_code, dt, bp_key, bp, id_bp ";
        sqlOrderByDocEty += "d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, f_dt_code, d.dt, bp_key, bp, id_bp ";

        if (mnGridMode == SModConsts.VIEW_SC_DET) {
            sqlOrderByDocEty += ", item, item_key, ";
            sqlOrderByDocEty += "fid_item, f_orig_unit, fid_orig_unit, f_orig_qty ";
        }

        if (mnGridMode == SModConsts.VIEW_SC_DET) {
            msSql = "";
        }
        else {
            msSql = "SELECT " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, " +
                    "dt, num_ser, num, num_ref, tot_r, tot_cur_r, b_ship, cur_key, " + SDbConsts.FIELD_NAME + ", " +
                    "'' AS " + SDbConsts.FIELD_CODE + ", f_dt_code, f_cb_code, id_bp, bp, bp_key, bpb, " +
                    "SUM(f_orig_qty) AS f_orig_qty, " +
                    "COALESCE(SUM(f_ship_qty), 0) AS f_ship_qty, " +
                    SDbConsts.FIELD_USER_INS_ID + ", " + SDbConsts.FIELD_USER_INS_TS + ", " + SDbConsts.FIELD_USER_INS_NAME + ", " +
                    SDbConsts.FIELD_USER_UPD_ID + ", " + SDbConsts.FIELD_USER_UPD_TS + ", " + SDbConsts.FIELD_USER_UPD_NAME + ", " +
                    SDbConsts.FIELD_COMP + "1 " +
                    "FROM (";
        }

        msSql += "SELECT de.id_year AS " + SDbConsts.FIELD_ID + "1, de.id_doc AS " + SDbConsts.FIELD_ID + "2, de.id_ety, " +
                "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_ship, c.cur_key, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS " + SDbConsts.FIELD_NAME + ", " +
                "'' AS " + SDbConsts.FIELD_CODE + ", dt.code AS f_dt_code, cb.code AS f_cb_code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                "de.fid_item, de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol AS f_unit, uo.symbol AS f_orig_unit, " +
                "de.orig_qty AS f_orig_qty, " +
                "COALESCE((SELECT SUM(sde.qty) FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS sde, " +
                SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " AS sd, " +
                SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " AS s " +
                "WHERE sde.fk_dps_year_n = de.id_year AND sde.fk_dps_doc_n = de.id_doc AND sde.fk_dps_ety_n = de.id_ety AND " +
                "sde.id_ship = sd.id_ship AND sde.id_dest = sd.id_dest AND sd.id_ship = s.id_ship AND s.b_del = 0), 0) AS f_ship_qty, " +
                "d.b_ship AS " + SDbConsts.FIELD_COMP + "1, " +
                "0 AS " + SDbConsts.FIELD_USER_INS_ID + ", " +
                "d.ts_new AS " + SDbConsts.FIELD_USER_INS_TS + ", " +
                "'' AS " + SDbConsts.FIELD_USER_INS_NAME + ", "   +
                "0 AS " + SDbConsts.FIELD_USER_UPD_ID + ", " +
                "d.ts_edit AS " + SDbConsts.FIELD_USER_UPD_TS + ", " +
                "'' AS " + SDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND " +
                "d.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND " +
                "d.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_SAL_CN[0] + " AND d.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_SAL_CN[1] + " " + // sqlFilter +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cb ON d.fid_cob = cb.id_bpb " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS bc ON d.fid_bp_r = bc.id_bp AND bc.id_ct_bp = " + SModSysConsts.BPSS_CT_BP_CUS + " " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bb ON d.fid_bpb = bb.id_bpb " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc AND " +
                "de.b_del = 0 AND de.b_inv = 1 AND de.qty > 0 AND de.orig_qty > 0 " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON de.fid_item = i.id_item " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON de.fid_unit = u.id_unit " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS uo ON de.fid_orig_unit = uo.id_unit " +
                (sql.isEmpty() ? "" : "WHERE " + sql) +
                "GROUP BY " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, de.id_ety, " +
                "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_ship, c.cur_key, " +
                "dt.code, cb.code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                "de.fid_item, de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol, uo.symbol, " +
                "de.qty, de.orig_qty ";

        if (mnGridSubtype == SModConsts.VIEW_ST_PEND) {
            msSql += "HAVING (f_orig_qty - f_ship_qty) <> 0 AND d.b_ship = 0 ";
        }
        else {
            msSql += "HAVING (f_orig_qty - f_ship_qty) = 0 OR d.b_ship = 1 ";
        }

        if (mnGridMode == SModConsts.VIEW_SC_DET) {
            msSql += "ORDER BY " + sqlOrderByDocEty + "; ";
        }
        else {
            msSql += ") AS DPS_ETY_TMP " +  // derived table
                    "GROUP BY " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, " +
                    "dt, num_ser, num, num_ref, tot_r, tot_cur_r, b_ship, cur_key, " + SDbConsts.FIELD_NAME + ", " +
                    "f_dt_code, f_cb_code, id_bp, bp, bp_key, bpb " +
                    "ORDER BY " + sqlOrderByDoc + "; ";
        }

        /*
        msSql = "SELECT "
            + "d.id_year AS " + SDbConsts.FIELD_ID + "1, "
            + "d.id_doc AS " + SDbConsts.FIELD_ID + "2, "
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "CONCAT(d.num_ser, IF(d.num_ser = '', '', '-'), d.num) AS " + SDbConsts.FIELD_NAME + ", "
            + "de.id_year, "
            + "de.id_doc, "
            + "de.id_ety, "
            + "COALESCE(SUM(COALESCE(de.qty,0) - COALESCE(sde.qty,0)), -1) AS f_qty, "
            + "d.b_ship, "
            + "d.dt, "
            + "tp.tp_dps, "
            + "tp.code, "
            + "ct.bp_key, "
            + "bpb.bpb, "
            + "d.num_ref, "
            + "bp.bp, "
            + "bpb.code, "
            + "d.b_ship, "
            + (mnGridMode == SModConsts.VIEW_SC_SUM ?
            "d.tot_cur_r, "
            + "c.cur_key, "
            + "COALESCE(sd.b_dly, 0) AS f_dly, " : "")
            + (mnGridMode == SModConsts.VIEW_SC_DET ?
            "i.item_key, "
            + "i.item, "
            + "u.symbol, "
            + "de.qty, "
            + "COALESCE((SELECT SUM(sde.qty) FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS sde "
            + "WHERE sde.fk_dps_year_n = de.id_year AND sde.fk_dps_doc_n = de.id_doc AND sde.fk_dps_ety_n = de.id_ety), 0) AS f_ship_qty, "
            + "(de.qty - COALESCE((SELECT SUM(sde.qty) FROM " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS sde "
            + "WHERE sde.fk_dps_year_n = de.id_year AND sde.fk_dps_doc_n = de.id_doc AND sde.fk_dps_ety_n = de.id_ety), 0)) AS f_ship_qty_pend, " : "")
            + "d.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
            + "s.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "s.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
            + "s.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "s.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
            + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
            + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_TP_DPS) + " AS tp ON d.fid_ct_dps = tp.id_ct_dps AND d.fid_cl_dps = tp.id_cl_dps "
            + "AND d.fid_tp_dps = tp.id_tp_dps "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON d.fid_bp_r = bp.id_bp "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP_CT) + " AS ct ON bp.id_bp = ct.id_bp AND ct.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_CUS + " "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpb ON d.fid_cob = bpb.id_bpb "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON d.fid_cur = c.id_cur "
            + (mnGridMode == SModConsts.VIEW_SC_DET ?
            "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item "
            + "INNER JOIN erp.itmu_unit AS u ON de.fid_unit = u.id_unit " : "")
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS sde ON de.id_year = sde.fk_dps_year_n AND de.id_doc = sde.fk_dps_doc_n AND de.id_ety = sde.id_ety "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST) + " AS sd ON sde.id_ship = sd.id_ship AND sde.id_dest = sd.id_dest "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP) + " AS s ON sd.id_ship = s.id_ship "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON s.fk_usr_ins = ui.id_usr "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON s.fk_usr_upd = uu.id_usr "
            + "WHERE  d.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_SAL_CN[0]  + " AND d.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_SAL_CN[1]  + " AND "
            + "d.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_SAL_CN[2]  + " AND d.fid_st_dps = " + SModSysConsts.TRNS_ST_DPS_EMITED + " AND "
            + "d.fid_st_dps_val = " + SModSysConsts.TRNS_ST_DPS_VAL_EFF +  " " + (mnGridSubtype == SModConsts.VIEW_ST_PEND ? "AND d.b_ship = 0 " : "") + " "
            + "GROUP BY " + (mnGridMode == SModConsts.VIEW_SC_SUM ? "d.id_year, d.id_doc " : "de.id_year, de.id_doc, de.id_ety ") + " "
            + "HAVING " + (mnGridSubtype == SModConsts.VIEW_ST_PEND ? "f_qty <> 0 " : "f_qty = 0 OR d.b_ship = 1 ")
            + "ORDER BY d.num_ser, d.num, d.dt_doc DESC " + (mnGridMode == SModConsts.VIEW_SC_DET ? ", i.item, i.item_key " :  "") + " ";
        */
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[mnGridMode == SModConsts.VIEW_SC_SUM ? 8 : 14];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_dt_code", "Tipo doc");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_NAME, "Folio doc");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "num_ref", "Referencia doc");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha doc");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bpb", "Sucursal asoc neg");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "bp", "Asociado negocios");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "bp_key", "Clave asoc neg");

        if (mnGridMode == SModConsts.VIEW_SC_DET) {

            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "i.item", "Ítem");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "i.item_key", "Ítem clave");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_orig_qty", "Cant original");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_ship_qty", "Cant embarcada");
            columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "", "Cant pendiente");
            columns[col].getRpnArguments().add(new SLibRpnArgument("f_orig_qty", SLibRpnArgumentType.OPERAND));
            columns[col].getRpnArguments().add(new SLibRpnArgument("f_ship_qty", SLibRpnArgumentType.OPERAND));
            columns[col++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_unit", "Unidad");
        }

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "b_ship", "Embarcado");

        gridColumnsViews.addAll(Arrays.asList((SGridColumnView[]) columns));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.LOG_SHIP);
        moSuscriptionsSet.add(SModConsts.LOGX_SHIP_AUTH);
        moSuscriptionsSet.add(SModConsts.TRN_DPS);
        moSuscriptionsSet.add(SModConsts.CFGU_CUR);
        moSuscriptionsSet.add(SModConsts.BPSU_BPB);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.BPSU_BP_CT);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.ITMU_UNIT);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == moButtonShipmentInternational) {
                actionShipment(SModSysConsts.LOGS_TP_DLY_INT);
            }
            else if (button == moButtonShipmentNational) {
                actionShipment(SModSysConsts.LOGS_TP_DLY_DOM);
            }
            else if (button == moButtonShipmentClose) {
                actionCloseDpsShipment(true);
            }
            else if (button == moButtonShipmentOpen) {
                actionCloseDpsShipment(false);
            }
        }
    }
}

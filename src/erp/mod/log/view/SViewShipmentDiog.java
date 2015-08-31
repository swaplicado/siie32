/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.view;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.log.db.SParamsShipment;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
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
 * @author Juan Barajas
 */
public class SViewShipmentDiog extends SGridPaneView implements ActionListener {

    private JButton mjbShip;
    private JButton mjbClose;
    private JButton mjbOpen;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SGridFilterDateCutOff moFilterDateCutOff;

    public SViewShipmentDiog(SGuiClient client, int gridType, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, gridType, gridSubtype, title, params);
        initComponents();
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
    }

    private void initComponents() {
        mjbShip = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ship_nat_del.gif")));
        mjbClose = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_CLOSE));
        mjbOpen = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_OPEN));

        mjbShip.setPreferredSize(new Dimension(23, 23));
        mjbClose.setPreferredSize(new Dimension(23, 23));
        mjbOpen.setPreferredSize(new Dimension(23, 23));

        mjbShip.addActionListener(this);
        mjbClose.addActionListener(this);
        mjbOpen.addActionListener(this);

        mjbShip.setToolTipText("Embarcar");
        mjbClose.setToolTipText("Cerrar para embarque");
        mjbOpen.setToolTipText("Abrir para embarque");

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
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbShip);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbOpen);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbClose);

        mjbShip.setEnabled(mnGridSubtype != SModConsts.VIEW_ST_DONE && mnGridMode == SModConsts.VIEW_SC_SUM);
        mjbOpen.setEnabled(mnGridSubtype == SModConsts.VIEW_ST_DONE && mnGridMode == SModConsts.VIEW_SC_SUM);
        mjbClose.setEnabled(mnGridSubtype != SModConsts.VIEW_ST_DONE && mnGridMode == SModConsts.VIEW_SC_SUM);
    }

    private void actionShipment() {
        SGuiParams params = null;
        SParamsShipment paramsShipment = null;

        if (mjbShip.isEnabled()) {
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
                    params = new SGuiParams();
                    paramsShipment = new SParamsShipment();

                    paramsShipment.setFkShipmentTypeId(SModSysConsts.LOGS_TP_SHIP_SHI);
                    paramsShipment.setFkDeliveryTypeId(SModSysConsts.LOGS_TP_DLY_DOM);
                    paramsShipment.setFkDocShipmentTypeId(SModSysConsts.LOGS_TP_DOC_SHIP_IOG);
                    paramsShipment.setFkDiogYearId(gridRow.getRowPrimaryKey()[0]);
                    paramsShipment.setFkDiogDocId(gridRow.getRowPrimaryKey()[1]);
                    params.getParamsMap().put(SModConsts.LOGX_SHIP_DIOG, paramsShipment);
                    
                    miClient.getSession().getModule(SModConsts.MOD_LOG_N, SLibConsts.UNDEFINED).showForm(SModConsts.LOG_SHIP, SLibConsts.UNDEFINED,  params);
                    miClient.getSession().notifySuscriptors(mnGridType);
                    miClient.getSession().notifySuscriptors(SModConsts.LOG_SHIP);
                }
            }
        }
    }

    private void actionCloseShipDiog() {
        if (mjbClose.isEnabled()) {
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
                    if (miClient.showMsgBoxConfirm("El traspaso de inventarios quedará 'cerrado para embarque',\n " + SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION) {
                        Vector<Object> params = new Vector<Object>();

                        params.add(gridRow.getRowPrimaryKey()[0]);
                        params.add(gridRow.getRowPrimaryKey()[1]);
                        params.add(SDataConstantsSys.UPD_DIOG_FL_SHIP);
                        params.add(1);
                        params.add(miClient.getSession().getUser().getPkUserId());
                        params = SDataUtilities.callProcedure((SClientInterface) miClient, SProcConstants.TRN_DIOG_UPD, params, SLibConstants.EXEC_MODE_SILENT);

                        miClient.getSession().notifySuscriptors(mnGridType);
                        miClient.getSession().notifySuscriptors(mnGridSubtype);
                        miClient.getSession().notifySuscriptors(mnGridMode);
                    }
                }
            }
        }
    }

    private void actionOpenShipDiog() {
        if (mjbOpen.isEnabled()) {
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
                    if (!(boolean) gridRow.getComplementsMap().get(SModConsts.VIEW_ST_REG)) {
                        miClient.showMsgBoxInformation("No se puede abrir un traspaso de inventarios que no ha sido cerrado de forma manual.");
                    }
                    else {
                        if (miClient.showMsgBoxConfirm("El traspaso de inventarios quedará 'abierto para embarque',\n " + SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION) {
                            Vector<Object> params = new Vector<Object>();

                            params.add(gridRow.getRowPrimaryKey()[0]);
                            params.add(gridRow.getRowPrimaryKey()[1]);
                            params.add(SDataConstantsSys.UPD_DIOG_FL_SHIP);
                            params.add(0);
                            params.add(miClient.getSession().getUser().getPkUserId());
                            params = SDataUtilities.callProcedure((SClientInterface) miClient, SProcConstants.TRN_DIOG_UPD, params, SLibConstants.EXEC_MODE_SILENT);

                            miClient.getSession().notifySuscriptors(mnGridType);
                            miClient.getSession().notifySuscriptors(mnGridSubtype);
                            miClient.getSession().notifySuscriptors(mnGridMode);
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

        getComplementsMap().put(SModConsts.VIEW_ST_REG, SGridConsts.COL_TYPE_BOOL_M);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "g.b_del = 0 ";
        }

        if (mnGridSubtype == SModConsts.VIEW_ST_DONE) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("g.dt", (SGuiDate) filter);
        }
        else if (mnGridSubtype == SModConsts.VIEW_ST_PEND) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();

            if (filter != null) {
                sql += (sql.isEmpty() ? "" : "AND ") + " g.dt <= '" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "' ";
            }
        }

        if (mnGridMode == SModConsts.VIEW_SC_DET) {
            msSql = "";
        }
        else {
            msSql = "SELECT " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, '', "
                    + SDbConsts.FIELD_NAME + ", " + SDbConsts.FIELD_DATE + ", "
                    + "'' AS " + SDbConsts.FIELD_CODE + ", "
                    + "f_bpb, f_ent, f_comp1, " + SDbConsts.FIELD_USER_INS_ID + ", " + SDbConsts.FIELD_USER_INS_TS + ", " + SDbConsts.FIELD_USER_INS_NAME + ", "
                    + "SUM(f_qty) AS f_qty, "
                    + "SUM(f_orig_qty) AS f_orig_qty, "
                    + "COALESCE(SUM(f_ship_qty), 0) AS f_ship_qty "
                    + "FROM (";
        }

        msSql += "SELECT "
            + "ge.id_year AS " + SDbConsts.FIELD_ID + "1, "
            + "ge.id_doc AS " + SDbConsts.FIELD_ID + "2, "
            + "ge.id_ety, "
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "CONCAT(g.num_ser, IF(LENGTH(g.num_ser) = 0, '', '-'), erp.lib_fix_int(g.num, " + SDataConstantsSys.NUM_LEN_IOG + ")) AS " + SDbConsts.FIELD_NAME + ", "
            + "g.dt AS " + SDbConsts.FIELD_DATE + ", "
            + "bpb.code AS f_bpb, "
            + "ent.code AS f_ent, "
            + "i.item_key, "
            + "i.item, "
            + "u.symbol AS f_unit, "
            + "g.b_ship AS " + SDbConsts.FIELD_COMP + "1, "
            + "ge.qty AS f_qty, "
            + "ge.orig_qty AS f_orig_qty, "
            + "COALESCE(SUM(de.qty), 0) AS f_ship_qty, "
            + "g.fid_usr_ship AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "g.ts_ship AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + " "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS g "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS ge ON g.id_year = ge.id_year AND g.id_doc = ge.id_doc "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpb ON g.fid_cob = bpb.id_bpb "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS ent ON g.fid_cob = ent.id_cob AND g.fid_wh = ent.id_ent "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON ge.fid_item = i.id_item "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON ge.fid_unit = u.id_unit "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON g.fid_usr_ship = ui.id_usr "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_SHIP_DEST_ETY) + " AS de ON ge.id_year = de.fk_diog_year_n AND ge.id_doc = de.fk_diog_doc_n AND ge.id_ety = de.fk_diog_ety_n AND de.id_ship = (SELECT id_ship FROM log_ship WHERE de.id_ship = id_ship AND b_del = 0) "
            + "WHERE g.b_del = 0 AND ge.b_del = 0 AND g.b_ship_req = 1 AND "
            + "g.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_INT_TRA[0] + " AND g.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_INT_TRA[1] + " AND g.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_INT_TRA[2] + " "
            + (sql.isEmpty() ? "" : "AND " + sql)
            + "GROUP BY " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, ge.id_ety, g.dt, g.num_ser, g.num, ge.fid_item, ge.fid_unit, i.item_key, i.item, u.symbol, ge.qty, ge.orig_qty ";

        if (mnGridMode == SModConsts.VIEW_SC_DET) {
            if (mnGridSubtype == SModConsts.VIEW_ST_PEND) {
                msSql += "HAVING (f_orig_qty - f_ship_qty) <> 0 AND g.b_ship = 0 ";
            }
            else {
                msSql += "HAVING (f_orig_qty - f_ship_qty) = 0 OR g.b_ship = 1 ";
            }

            msSql += "ORDER BY " + SDbConsts.FIELD_NAME + ", " + SDbConsts.FIELD_DATE + ", " +  SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, ge.id_ety ";
        }
        else {
            msSql += ") AS DIOG_ETY_TMP " +  // derived table
                    "GROUP BY " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2, " + SDbConsts.FIELD_NAME + ", " + SDbConsts.FIELD_DATE + " ";

            if (mnGridSubtype == SModConsts.VIEW_ST_PEND) {
                msSql += "HAVING (f_orig_qty - f_ship_qty) <> 0 AND f_comp1 = 0 ";
            }
            else {
                msSql += "HAVING (f_orig_qty - f_ship_qty) = 0 OR f_comp1 = 1 ";
            }

            msSql += "ORDER BY " + SDbConsts.FIELD_NAME + ", " + SDbConsts.FIELD_DATE + ", " + SDbConsts.FIELD_ID + "1, " + SDbConsts.FIELD_ID + "2 ";
        }

    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        int cols = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();
        SGridColumnView[] columns = null;

        cols = mnGridMode == SModConsts.VIEW_SC_DET ? 13 : 10;

        columns = new SGridColumnView[cols];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_NAME, "Folio doc");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha doc");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_bpb", "Sucursal empresa");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_ent", "Almacén");
        if (mnGridMode == SModConsts.VIEW_SC_DET) {
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.item", "Ítem");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "i.item_key", "Ítem código");
            columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "f_unit", "Unidad");
        }
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_orig_qty", "Cant original");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_ship_qty", "Cant embarcada");
        columns[col] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "", "Cant pendiente");
        columns[col].getRpnArguments().add(new SLibRpnArgument("f_orig_qty", SLibRpnArgumentType.OPERAND));
        columns[col].getRpnArguments().add(new SLibRpnArgument("f_ship_qty", SLibRpnArgumentType.OPERAND));
        columns[col++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, SDbConsts.FIELD_COMP + "1", "Embarcado");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, "Usr embarque");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, "Usr TS embarque");

        gridColumnsViews.addAll(Arrays.asList((SGridColumnView[]) columns));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(mnGridSubtype);
        moSuscriptionsSet.add(mnGridMode);
        moSuscriptionsSet.add(SModConsts.TRN_DIOG_ETY);
        moSuscriptionsSet.add(SModConsts.BPSU_BPB);
        moSuscriptionsSet.add(SModConsts.CFGU_COB_ENT);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.ITMU_UNIT);
        moSuscriptionsSet.add(SModConsts.LOG_SHIP_DEST_ETY);
        moSuscriptionsSet.add(SModConsts.LOG_SHIP_DEST);
        moSuscriptionsSet.add(SModConsts.LOG_SHIP);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == mjbShip) {
                actionShipment();
            }
            else if (button == mjbClose) {
                actionCloseShipDiog();
            }
            else if (button == mjbOpen) {
                actionOpenShipDiog();
            }
        }
    }
}

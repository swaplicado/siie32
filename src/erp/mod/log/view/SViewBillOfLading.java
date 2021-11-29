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
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Isabel Servín
 */
public class SViewBillOfLading extends SGridPaneView implements ActionListener {

    private JButton jbPrint;
    private SGridFilterDatePeriod moFilterDatePeriod;

    public SViewBillOfLading(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.LOG_BOL, SLibConstants.UNDEFINED, title);
        setRowButtonsEnabled(true, false, true, false, true);
        
        initComponetsCustom();
    }

    private void initComponetsCustom() {

        jbPrint = new JButton(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_print.gif")));
        jbPrint.setPreferredSize(new Dimension(23, 23));
        jbPrint.addActionListener(this);
        jbPrint.setToolTipText(SUtilConsts.TXT_PRINT + " embarque");

        if (mnGridMode == SModConsts.VIEW_SC_SUM) {
            jbPrint.setEnabled(true);
        }
        else {
            jbPrint.setEnabled(false);
        }

        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrint);
    }

    private void actionPrint() {
        SDbShipment shipment = null;

        if (jbPrint.isEnabled()) {
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
                        map.put("sCurCode", miClient.getSession().getSessionCustom().getLocalCurrencyCode());

                        miClient.getSession().printReport(SModConsts.LOGR_SHIP, SLibConsts.UNDEFINED, null, map);
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
            sql += (sql.isEmpty() ? "" : "AND ") + "b.b_del = 0 ";
        }

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("b.dt", (SGuiDate) filter);

        msSql = "SELECT " 
            + "b.id_bol AS " + SDbConsts.FIELD_ID + "1, " 
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "IF(b.ser <> '', CONCAT(b.ser, '-', b.num), b.num) AS " + SDbConsts.FIELD_NAME + ", "
            + "b.dt AS " + SDbConsts.FIELD_DATE + ", " 
            + "b.int_bol, " 
            + "b.input_output_bol, " 
            + "b.input_output_way_key, " 
            + "b.distance_tot, " 
            + "v.name, " 
            + "v.plate, " 
            + "tra1.plate, " 
            + "tra1.name, " 
            + "tra2.plate, " 
            + "tra2.name, " 
            + "b.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " 
            + "b.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", " 
            + "b.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", " 
            + "b.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", " 
            + "b.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", " 
            + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
            + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL) + " AS b " 
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_TRANSP_MODE) + " AS t ON b.id_bol = t.id_bol " 
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON b.fk_usr_ins = ui.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON b.fk_usr_upd = uu.id_usr "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_VEH) + " AS v ON t.fk_veh_n = v.id_veh " 
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_TRAILER) + " AS tra1 ON t.fk_veh_trailer_1_n = tra1.id_trailer " 
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_TRAILER) + " AS tra2 ON t.fk_veh_trailer_2_n = tra2.id_trailer "
            + (sql.isEmpty() ? "" : "WHERE " + sql)
            + "ORDER BY b.num, b.dt ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[17];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_NAME, "Folio");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_ico_xml", "CFD");
//        columns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b.int_bol", "Transporte internacional");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "b.input_output_bol", "E/S");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "b.input_output_way_key", "Vía E/S");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "b.distance_tot", "Total distancia recorrida");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "v.name", "Vehiculo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_PER, "v.plate", "Placa vehiculo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "tra1.name", "Remolque 1");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_PER, "tra1.plate", "Placa remolque 1");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "tra2.name", "Remolque 2");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_PER, "tra2.plate", "Placa remolque 2");
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
        moSuscriptionsSet.add(SModConsts.LOG_BOL);
        moSuscriptionsSet.add(SModConsts.LOG_BOL_TRANSP_MODE);
        moSuscriptionsSet.add(SModConsts.LOG_VEH);
        moSuscriptionsSet.add(SModConsts.LOG_TRAILER);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPrint) {
                actionPrint();
            }
        }
    }
}

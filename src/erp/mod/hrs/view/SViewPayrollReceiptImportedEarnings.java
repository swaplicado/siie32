/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mod.SModConsts;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;
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
 * @author Edwin Carmona, Sergio Flores
 */
public class SViewPayrollReceiptImportedEarnings extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;

    private JButton jbPrintReceipt;
    private JButton jbSendReceipt;

    public SViewPayrollReceiptImportedEarnings(SGuiClient client, String title, int subtype) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_PAY_RCP_IMPORT, subtype, title);
        initComponentCustom();
    }

    /*
     * Private methods
     */

    private void initComponentCustom() {
        setRowButtonsEnabled(false);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }
    
    /*
     * Public methods
     */

    @Override
    public void prepareSqlQuery() {
        String sql = "p.fk_tp_pay = " + mnGridSubtype + " ";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("p.dt_end", (SGuiDate) filter);

        msSql = "SELECT "
                + "pr.id_pay AS " + SDbConsts.FIELD_ID + "1, "
                + "pr.id_emp AS " + SDbConsts.FIELD_ID + "2, "
                + "e.num AS " + SDbConsts.FIELD_CODE + ", "
                + "bp.bp AS " + SDbConsts.FIELD_NAME + ", "
                + "p.num, "
                + "p.dt_sta, "
                + "p.dt_end, "
                + "p.nts, "
                + "tpsc.code, "
                + "tps.name, "
                + "pr.ear_r, "
                + "pr.ded_r, "
                + "ear.name AS ear_name, "
                + "bon.name AS bonus_name, "
                + "pre.b_time_clock, "
                + "pr.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "p.b_clo, "
                + "p.fk_usr_clo, "
                + "p.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "p.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "p.ts_usr_clo, "
                + "p.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "p.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "uc.usr AS _usr_close, "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_PAY_SHT_CUS) + " AS tpsc ON p.fk_tp_pay_sht_cus = tpsc.id_tp_pay_sht_cus "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY_SHT) + " AS tps ON p.fk_tp_pay_sht = tps.id_tp_pay_sht "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pr.id_pay = pre.id_pay AND pr.id_emp = pre.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS ear ON pre.fk_ear = ear.id_ear "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_BONUS) + " AS bon ON pre.fk_bonus = bon.id_bonus "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON pr.id_emp = e.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON pr.id_emp = bp.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON p.fk_usr_clo = uc.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON p.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON p.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE NOT p.b_del AND NOT pr.b_del AND (pre.fk_bonus > 1 OR PRE.b_time_clock = TRUE) AND " + sql)
                + "ORDER BY p.num, tps.name, bp.bp, pr.id_pay, pr.id_emp ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "p.num", "Núm nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "p.dt_sta", "F inicial nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "p.dt_end", "F final nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "tpsc.code", "Tipo nómina empresa"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tps.name", "Tipo nómina"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, SDbConsts.FIELD_NAME, "Empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, SDbConsts.FIELD_CODE, "Clave"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "pr.ear_r", "Percepciones $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "pr.ded_r", "Deducciones $"));
        
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "", "Total neto $");
        column.getRpnArguments().add(new SLibRpnArgument("pr.ear_r", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument("pr.ded_r", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        gridColumnsViews.add(column);
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ear_name", "Percepción"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bonus_name", "Bono"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "pre.b_time_clock", "Checador"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "p.nts", "Notas nómina", 200));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "p.b_clo", "Cerrada"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "_usr_close", "Usr cierre nómina"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "p.ts_usr_clo", "Usr TS cierre nómina"));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.HRS_PAY);
        moSuscriptionsSet.add(SModConsts.HRS_PAY_RCP_ISS);
        moSuscriptionsSet.add(SModConsts.HRS_SIE_PAY);
        moSuscriptionsSet.add(SModConsts.TRN_CFD);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPrintReceipt) {
//                actionPrintReceipt();
            }
            else if (button == jbSendReceipt) {
//                actionSendReceipt();
            }
        }
    }
}

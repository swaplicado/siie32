/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mod.SModConsts;
import java.util.ArrayList;
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
 * @author Juan Barajas
 */
public class SViewPayrollLoanDeductionComplement extends SGridPaneView {

    private SGridFilterDatePeriod moFilterDatePeriod;

    public SViewPayrollLoanDeductionComplement(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_PAY_RCP_DED, SModConsts.HRS_LOAN, title);
        initComponentCustom();
    }

    /*
     * Private methods
     */

    private void initComponentCustom() {
        setRowButtonsEnabled(true, true, true, false, true);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(3);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "vd.b_del = 0 ";
        }
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);

        msSql = "SELECT "
                + "vd.id_pay AS " + SDbConsts.FIELD_ID + "1, "
                + "vd.id_emp AS " + SDbConsts.FIELD_ID + "2, "
                + "vd.id_mov AS " + SDbConsts.FIELD_ID + "3, "
                + "ded.code AS " + SDbConsts.FIELD_CODE + ", "
                + "ded.name AS " + SDbConsts.FIELD_NAME + ", "
                + "(SELECT bp.bp FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp WHERE bp.id_bp = v.id_emp) AS " + SDbConsts.FIELD_NAME + "_emp" + ", "
                + "CONCAT(tl.name,',', l.num) AS f_loan, "
                + "vd.amt_r, "
                + "v.dt, "
                + "v.nts, "
                + "vd.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "vd.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED_CMP) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS vd ON "
                + "v.id_pay = vd.id_pay AND v.id_emp = vd.id_emp AND v.id_mov = vd.id_mov "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " AS ded ON "
                + "vd.fk_ded = ded.id_ded "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_LOAN) + " AS l ON "
                + "vd.fk_loan_emp_n = l.id_emp AND vd.fk_loan_loan_n = l.id_loan "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_LOAN) + " AS tl ON "
                + "vd.fk_tp_loan_n = tl.id_tp_loan "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY ded.name, f_name_emp, vd.id_pay , vd.id_emp, vd.id_mov ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME + " deducción"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE + " deducción"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME + "_emp", "Empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_loan", "Crédito/préstamo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt", "Fecha"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "vd.amt_r", "Monto $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.nts", "Notas"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.HRS_DED);
        moSuscriptionsSet.add(SModConsts.HRSU_EMP);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SHrsConsts;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Edwin Carmona
 */
public class SViewBenAnnualBon extends SGridPaneView {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private int mnDaysOfMonth;
    
    public SViewBenAnnualBon(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRSX_BEN_ANN_BON, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }

    /*
     * Private methods
     */
    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }

    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

    @Override
    public void actionMouseClicked() {
        
    }
    
    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String where = "";
        String cutoff = "";
        Object filter = null;
        Date dtYear = new Date();
        mnDaysOfMonth = 30;
        int benefitTypeId = SModSysConsts.HRSS_TP_BEN_ANN_BON;
        
        moPaneSettings = new SGridPaneSettings(1);
        Calendar dt = Calendar.getInstance();
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        int[]aDt = SLibTimeUtils.digestMonth((SGuiDate) filter);
        dtYear = SLibTimeUtils.createDate(aDt[0], aDt[1]);
        dt.set(Calendar.YEAR, aDt[0]);
        dt.set(Calendar.MONTH, aDt[1] - 1);
        mnDaysOfMonth = dt.getActualMaximum(Calendar.DAY_OF_MONTH);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "NOT e.b_del ";
        }
        
        cutoff = "'" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getEndOfYear(dtYear)) + "'";
        String sqlDateBoyCurr = "'" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getBeginOfYear(dtYear)) + "'";
       
        msSql = "SELECT  " +
            "    cc.id_cc AS " + SDbConsts.FIELD_ID + "1, " +
            "    cc.cc AS " + SDbConsts.FIELD_CODE + ", " +
            "    cc.cc AS " + SDbConsts.FIELD_NAME + ", " +
            "    COALESCE(cc.id_cc, '--') AS _id_cc, " +
            "    COALESCE(cc.cc, 'SIN CC') AS _nm_cc, " +
            "    SUM(ROUND((ROUND(IF(e.fk_tp_pay = " + SModSysConsts.HRSS_TP_PAY_WEE + ", " +
            "                        e.sal, " +
            "                        e.wage * " + SHrsConsts.YEAR_MONTHS + " / " + SHrsConsts.YEAR_DAYS + "), 2) * (COALESCE((SELECT  " +
            "                            br.ben_day " +
            "                        FROM " +
            "                            " + SModConsts.TablesMap.get(SModConsts.HRS_BEN_ROW) + " AS br " +
            "                        WHERE " +
            "                            br.id_ben = COALESCE((SELECT  " +
            "                                            b.id_ben " +
            "                                        FROM " +
            "                                            " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " AS b " +
            "                                        WHERE " +
            "                                            NOT b.b_del " +
            "                                                AND b.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_ANN_BON + " " +
            "                                                AND b.dt_sta <= IF(e.b_act, " + cutoff + ", e.dt_dis_n) " +
            "                                                AND b.fk_tp_pay_n = e.fk_tp_pay " +
            "                                                AND b.uni = e.b_uni " +
            "                                        ORDER BY b.dt_sta DESC , b.id_ben " +
            "                                        LIMIT 1), " +
            "                                    (SELECT  " +
            "                                            b.id_ben " +
            "                                        FROM " +
            "                                            " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " AS b " +
            "                                        WHERE " +
            "                                            NOT b.b_del " +
            "                                                AND b.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_ANN_BON + " " +
            "                                                AND b.dt_sta <= IF(e.b_act, " + cutoff + ", e.dt_dis_n) " +
            "                                                AND b.fk_tp_pay_n IS NULL " +
            "                                                AND b.uni = e.b_uni " +
            "                                        ORDER BY b.dt_sta DESC , b.id_ben " +
            "                                        LIMIT 1)) " +
            "                                AND TIMESTAMPDIFF(YEAR, " +
            "                                e.dt_ben, " +
            "                                IF(e.b_act, " + cutoff + ", e.dt_dis_n)) <= (br.mon / " + SLibTimeConsts.MONTHS + ") " +
            "                        ORDER BY br.id_row " +
            "                        LIMIT 1), " +
            "                    0) * (DATEDIFF(IF(e.b_act, " + cutoff + ", e.dt_dis_n), " +
            "                    IF(" + benefitTypeId + " = " + SModSysConsts.HRSS_TP_BEN_ANN_BON + ", " +
            "                        IF(e.dt_ben < " + sqlDateBoyCurr + ", " + sqlDateBoyCurr + ", e.dt_ben), " +
            "                        ADDDATE(e.dt_ben, " +
            "                            INTERVAL TIMESTAMPDIFF(YEAR, " +
            "                                e.dt_ben, " +
            "                                IF(e.b_act, " + cutoff + ", e.dt_dis_n)) YEAR))) / " + SHrsConsts.YEAR_DAYS + ")) / " + SHrsConsts.YEAR_DAYS + ") * " + mnDaysOfMonth + ", " +
            "            2)) AS _provi " +
            "FROM " +
            "    " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp " +
            "        INNER JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON e.id_emp = bp.id_bp " +
            "        INNER JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " AS em ON em.id_emp = bp.id_bp " +
            "        INNER JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS tp ON e.fk_tp_pay = tp.id_tp_pay " +
            "        INNER JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON e.fk_dep = dep.id_dep " +
            "        LEFT OUTER JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.HRS_DEP_CC) + " AS depcc ON depcc.id_dep = dep.id_dep " +
            "        LEFT OUTER JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cc ON cc.pk_cc = depcc.fk_cc " +
            "WHERE " +
            "    e.dt_ben <= " + cutoff + " " +
            "        AND (e.b_act " +
            "        OR (NOT e.b_act " +
            "        AND e.dt_dis_n <= " + cutoff + ")) " +
            (where.isEmpty() ? "" : "AND ") + where +
            "        AND e.b_act " +
            "GROUP BY cc.id_cc " +
            "ORDER BY bp.bp , bp.id_bp;";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_id_cc", "No. centro de costo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "_nm_cc", "Centro de costo"));
        SGridColumnView column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_provi", "$ provisión mensual");
        column.setSumApplying(true);
        gridColumnsViews.add(column);
        
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
    }
}

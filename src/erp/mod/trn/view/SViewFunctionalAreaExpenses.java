/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import java.util.ArrayList;
import java.util.Calendar;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterYear;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Sergio Flores
 */
public class SViewFunctionalAreaExpenses extends SGridPaneView {

    private SGridFilterYear moFilterYear;
    
    public SViewFunctionalAreaExpenses(SGuiClient client, int dpsCategory, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_FUNC_EXPENSES, dpsCategory, title);
        setRowButtonsEnabled(false);
        initComponetsCustom();
    }

    private void initComponetsCustom() {
        moFilterYear = new SGridFilterYear(miClient, this);
        moFilterYear.initFilter(new int[] { SLibTimeUtils.digestYear(miClient.getSession().getCurrentDate())[0] });
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterYear);
    }

    @Override
    public void prepareSqlQuery() {
        int year;
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(2);

        filter = (int[]) moFiltersMap.get(SGridConsts.FILTER_YEAR).getValue();
        year = ((int[]) filter)[0];
        
        String dpsClass = "";
        int dpsClassOrd = SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[1];
        int dpsClassDoc = SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1];
        int dpsClassAdj = SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1];
        
        switch (mnGridSubtype) {
            case SDataConstantsSys.TRNS_CT_DPS_PUR:
                dpsClass = "GASTOS Y COMPRAS ";
                break;
            case SDataConstantsSys.TRNS_CT_DPS_SAL:
                dpsClass = "VENTAS ";
                break;
            default:
        }

        msSql = "SELECT "
                + "t._func AS " + SDbConsts.FIELD_ID + "1, "
                + "t._dps_cl AS " + SDbConsts.FIELD_ID + "2, "
                + "f.code AS " + SDbConsts.FIELD_CODE + ", "
                + "f.name AS " + SDbConsts.FIELD_NAME + ", "
                + "t._dps_cl, COALESCE(CONCAT('" + dpsClass + "', dcl.cl_dps), 'PRESUPUESTO MENSUAL') AS _dps_cl_name, t._type, "
                + "t._01, t._02, t._03, t._04, t._05, t._06, "
                + "t._07, t._08, t._09, t._10, t._11, t._12, "
                + "IF(t._dps_cl = 0, NULL, "
                + "_01 + _02 + _03 + _04 + _05 + _06 + "
                + "_07 + _08 + _09 + _10 + _11 + _12) AS _year "
                + "FROM ( "
                + "SELECT _func, _dps_cl, 2 AS _type, "
                + "SUM(_01) AS _01, SUM(_02) AS _02, SUM(_03) AS _03, SUM(_04) AS _04, SUM(_05) AS _05, SUM(_06) AS _06, "
                + "SUM(_07) AS _07, SUM(_08) AS _08, SUM(_09) AS _09, SUM(_10) AS _10, SUM(_11) AS _11, SUM(_12) AS _12 "
                + "FROM ( "
                + "SELECT d.fid_func AS _func, IF(d.fid_cl_dps = " + dpsClassOrd + ", " + dpsClassOrd + ", " + dpsClassDoc + ") _dps_cl, "
                + "SUM(IF(MONTH(d.dt) = 1, IF(d.fid_cl_dps = " + dpsClassAdj + ", -1, 1) * d.stot_r, 0.0)) AS _01, "
                + "SUM(IF(MONTH(d.dt) = 2, IF(d.fid_cl_dps = " + dpsClassAdj + ", -1, 1) * d.stot_r, 0.0)) AS _02, "
                + "SUM(IF(MONTH(d.dt) = 3, IF(d.fid_cl_dps = " + dpsClassAdj + ", -1, 1) * d.stot_r, 0.0)) AS _03, "
                + "SUM(IF(MONTH(d.dt) = 4, IF(d.fid_cl_dps = " + dpsClassAdj + ", -1, 1) * d.stot_r, 0.0)) AS _04, "
                + "SUM(IF(MONTH(d.dt) = 5, IF(d.fid_cl_dps = " + dpsClassAdj + ", -1, 1) * d.stot_r, 0.0)) AS _05, "
                + "SUM(IF(MONTH(d.dt) = 6, IF(d.fid_cl_dps = " + dpsClassAdj + ", -1, 1) * d.stot_r, 0.0)) AS _06, "
                + "SUM(IF(MONTH(d.dt) = 7, IF(d.fid_cl_dps = " + dpsClassAdj + ", -1, 1) * d.stot_r, 0.0)) AS _07, "
                + "SUM(IF(MONTH(d.dt) = 8, IF(d.fid_cl_dps = " + dpsClassAdj + ", -1, 1) * d.stot_r, 0.0)) AS _08, "
                + "SUM(IF(MONTH(d.dt) = 9, IF(d.fid_cl_dps = " + dpsClassAdj + ", -1, 1) * d.stot_r, 0.0)) AS _09, "
                + "SUM(IF(MONTH(d.dt) = 10, IF(d.fid_cl_dps = " + dpsClassAdj + ", -1, 1) * d.stot_r, 0.0)) AS _10, "
                + "SUM(IF(MONTH(d.dt) = 11, IF(d.fid_cl_dps = " + dpsClassAdj + ", -1, 1) * d.stot_r, 0.0)) AS _11, "
                + "SUM(IF(MONTH(d.dt) = 12, IF(d.fid_cl_dps = " + dpsClassAdj + ", -1, 1) * d.stot_r, 0.0)) AS _12 "
                + "FROM trn_dps AS d "
                + "WHERE NOT d.b_del AND d.id_year = " + year + " AND d.fid_ct_dps = " + mnGridSubtype + " AND d.fid_cl_dps IN (" + dpsClassOrd + ", " + dpsClassDoc + ", " + dpsClassAdj + ") "
                + "GROUP BY d.fid_func, IF(d.fid_cl_dps = " + dpsClassOrd + ", " + dpsClassOrd + ", " + dpsClassDoc + "), MONTH(d.dt) "
                + "ORDER BY d.fid_func, IF(d.fid_cl_dps = " + dpsClassOrd + ", " + dpsClassOrd + ", " + dpsClassDoc + "), MONTH(d.dt)) AS t "
                + " "
                + "GROUP BY _func, _dps_cl "
                + " "
                + "UNION "
                + " "
                + "SELECT _func, 0 AS _dps_cl, 1 AS _type, "
                + "SUM(_01) AS _01, SUM(_02) AS _02, SUM(_03) AS _03, SUM(_04) AS _04, SUM(_05) AS _05, SUM(_06) AS _06, "
                + "SUM(_07) AS _07, SUM(_08) AS _08, SUM(_09) AS _09, SUM(_10) AS _10, SUM(_11) AS _11, SUM(_12) AS _12 "
                + "FROM ( "
                + "SELECT fb.fk_func AS _func, "
                + "IF(fb.period_month=1, fb.budget, NULL) AS _01, "
                + "IF(fb.period_month=2, fb.budget, NULL) AS _02, "
                + "IF(fb.period_month=3, fb.budget, NULL) AS _03, "
                + "IF(fb.period_month=4, fb.budget, NULL) AS _04, "
                + "IF(fb.period_month=5, fb.budget, NULL) AS _05, "
                + "IF(fb.period_month=6, fb.budget, NULL) AS _06, "
                + "IF(fb.period_month=7, fb.budget, NULL) AS _07, "
                + "IF(fb.period_month=8, fb.budget, NULL) AS _08, "
                + "IF(fb.period_month=9, fb.budget, NULL) AS _09, "
                + "IF(fb.period_month=10, fb.budget, NULL) AS _10, "
                + "IF(fb.period_month=11, fb.budget, NULL) AS _11, "
                + "IF(fb.period_month=12, fb.budget, NULL) AS _12 "
                + "FROM trn_func_budget AS fb "
                + "WHERE NOT fb.b_del AND fb.period_year = " + year + " "
                + "ORDER BY fb.fk_func, fb.period_month) AS t "
                + " "
                + "GROUP BY _func, _dps_cl "
                + " "
                + "ORDER BY _func, _type, _dps_cl) AS t "
                + " "
                + "INNER JOIN cfgu_func AS f ON t._func = f.id_func "
                + "LEFT OUTER JOIN erp.trns_cl_dps AS dcl ON t._dps_cl = dcl.id_cl_dps AND dcl.id_ct_dps = " + mnGridSubtype + " "
                + "ORDER BY f.name, t._func, t._dps_cl, t._type; ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, "Área funcional"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "_dps_cl", "Clase ID"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "_dps_cl_name", "Clase"));
        
        String[] months = SLibTimeUtils.createMonthsOfYearStd(Calendar.LONG);
        for (int month = 1; month <= months.length; month++) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_" + SLibUtils.DecimalFormatCalendarMonth.format(month), months[month - 1]));
        }
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_year", "Total anual"));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CFGU_FUNC);
        moSuscriptionsSet.add(SModConsts.TRN_FUNC_BUDGET);
        moSuscriptionsSet.add(SModConsts.TRN_DPS);
    }
}

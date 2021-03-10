/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.data.SDataConstants;
import erp.gui.grid.SGridFilterPanelEmployee;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Sergio Flores
 */
public class SViewPtu extends SGridPaneView {
    
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SGridFilterPanelEmployee moFilterEmployee;
    
    public SViewPtu(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRSX_PTU, 0, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        
        moFilterEmployee = new SGridFilterPanelEmployee(miClient, this, SModConsts.HRSS_TP_PAY, SModConsts.HRSU_DEP, SGridFilterPanelEmployee.EMP_STATUS_ALL);
        moFilterEmployee.initFilter(null);
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterEmployee);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        
        String start = "";
        String end = "";
        SGuiDate guiDate = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();

        switch (guiDate.getGuiType()) {
            case SGuiConsts.GUI_DATE_DATE:
                start = "'" + SLibUtils.DbmsDateFormatDate.format(guiDate) + "' ";
                end = "'" + SLibUtils.DbmsDateFormatDate.format(guiDate) + "' ";
                break;
            case SGuiConsts.GUI_DATE_MONTH:
                start = "'" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getBeginOfMonth(guiDate)) + "' ";
                end = "'" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getEndOfMonth(guiDate)) + "' ";
                break;
            case SGuiConsts.GUI_DATE_YEAR:
                start = "'" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getBeginOfYear(guiDate)) + "' ";
                end = "'" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getEndOfYear(guiDate)) + "' ";
                break;
            default:
                // do nothing!
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSS_TP_PAY)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "e.fk_tp_pay = " + ((int[]) filter)[0] + " ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSU_DEP)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "e.fk_dep = " + ((int[]) filter)[0] + " ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SGridFilterPanelEmployee.EMP_STATUS)).getValue();
        if (filter != null && ((int) filter) != SLibConsts.UNDEFINED) {
            if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ACT) {
                sql += (sql.isEmpty() ? "" : "AND ") + "e.b_act = 1 ";
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_INA) {
                sql += (sql.isEmpty() ? "" : "AND ") + "e.b_act = 0 ";
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ALL) {
            }
        }

        msSql = "SELECT "
                + "id_emp AS " + SDbConsts.FIELD_ID + "1, "
                + "_emp_name AS " + SDbConsts.FIELD_NAME + ", "
                + "_emp_num AS " + SDbConsts.FIELD_CODE + ", "
                + "b_act, "
                + "tpay.name AS _tp_name, "
                + "dep.name AS _dep_name, "
                + "pos.name AS _pos_name, "
                + "_cur_sal, "
                + "_cur_wage, "
                + "SUM(_days) AS _days, "
                + "COALESCE((SELECT SUM("
                + "DATEDIFF(IF(abs.dt_end >= " + end + ", " + end + ", abs.dt_end), "
                + "IF(abs.dt_sta <= " + start + ", " + start + ", abs.dt_sta)) + 1) "
                + "FROM hrs_abs AS abs "
                + "INNER JOIN erp.hrsu_tp_abs AS tabs ON tabs.id_cl_abs = abs.fk_cl_abs AND tabs.id_tp_abs = abs.fk_tp_abs "
                + "WHERE NOT abs.b_del AND abs.id_emp = t.id_emp AND ("
                + "(abs.dt_sta BETWEEN " + start + " AND " + end + ") OR "
                + "(abs.dt_end BETWEEN " + start + " AND " + end + ") OR "
                + "(abs.dt_sta < " + start + " AND abs.dt_end > " + end + ")) AND "
                + "tabs.id_cl_abs = " + SModSysConsts.HRSU_CL_ABS_ABS + " AND NOT tabs.b_pay), 0) AS _abs_abs_not_pay, "
                + "COALESCE((SELECT SUM("
                + "DATEDIFF(IF(abs.dt_end >= " + end + ", " + end + ", abs.dt_end), "
                + "IF(abs.dt_sta <= " + start + ", " + start + ", abs.dt_sta)) + 1) "
                + "FROM hrs_abs AS abs "
                + "WHERE NOT abs.b_del AND abs.id_emp = t.id_emp AND ("
                + "(abs.dt_sta BETWEEN " + start + " AND " + end + ") OR "
                + "(abs.dt_end BETWEEN " + start + " AND " + end + ") OR "
                + "(abs.dt_sta < " + start + " AND abs.dt_end > " + end + ")) AND "
                + "abs.fk_cl_abs = " + SModSysConsts.HRSU_TP_ABS_DIS_DIS[0] + " AND abs.fk_tp_abs = " + SModSysConsts.HRSU_TP_ABS_DIS_DIS[1] + "), 0) AS _abs_dis_dis, "
                + "(SELECT elw.sal "
                + "FROM hrs_emp_log_wage AS elw "
                + "WHERE NOT elw.b_del AND elw.id_emp = t.id_emp AND dt <= " + end + " "
                + "ORDER BY elw.dt DESC, elw.id_log LIMIT 1) AS _last_sal, "
                + "(SELECT elw.wage "
                + "FROM hrs_emp_log_wage AS elw "
                + "WHERE NOT elw.b_del AND elw.id_emp = t.id_emp AND dt <= " + end + " "
                + "ORDER BY elw.dt DESC, elw.id_log LIMIT 1) AS _last_wage "
                + "FROM ("
                + "SELECT b.bp AS _emp_name, e.num AS _emp_num, e.id_emp, e.b_act, e.dt_hire AS _e_dt_hire, e.dt_dis_n AS _e_dt_dis_n, "
                + "elh.id_log, elh.dt_hire AS _elh_dt_hire, elh.dt_dis_n AS _elh_dt_dis_n, elh.b_hire, "
                + "IF(elh.dt_hire <= " + start + ", " + start + ", elh.dt_hire) AS _start, "
                + "IF(elh.dt_dis_n >= " + end + " OR elh.dt_dis_n IS NULL, " + end + ", elh.dt_dis_n) AS _end, "
                + "DATEDIFF(IF(elh.dt_dis_n >= " + end + " OR elh.dt_dis_n IS NULL, " + end + ", elh.dt_dis_n), "
                + "IF(elh.dt_hire <= " + start + ", " + start + ", elh.dt_hire)) + 1 AS _days, "
                + "e.fk_tp_pay, e.fk_dep, e.fk_pos, e.sal AS _cur_sal, e.wage AS _cur_wage "
                + "FROM erp.hrsu_emp AS e "
                + "INNER JOIN erp.bpsu_bp AS b ON b.id_bp = e.id_emp "
                + "INNER JOIN hrs_emp_log_hire AS elh ON elh.id_emp = e.id_emp "
                + "WHERE NOT elh.b_del AND ("
                + "(elh.dt_hire <= " + end + " AND (elh.dt_dis_n IS NULL OR elh.dt_dis_n >= " + start + ")) OR "
                + "(elh.dt_hire >= " + start + " AND elh.dt_hire <= " + end + ")) " + (sql.isEmpty() ? "" : "AND " + sql)
                + "ORDER BY b.bp, e.num, e.id_emp, elh.dt_hire, elh.dt_dis_n, elh.id_log "
                + ") AS t "
                + "INNER JOIN erp.hrss_tp_pay AS tpay ON tpay.id_tp_pay = t.fk_tp_pay "
                + "INNER JOIN erp.hrsu_dep AS dep ON dep.id_dep = t.fk_dep "
                + "INNER JOIN erp.hrsu_pos AS pos ON pos.id_pos = t.fk_pos "
                + "GROUP BY id_emp, _emp_name, _emp_num "
                + "ORDER BY _emp_name, _emp_num, id_emp;";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, SDbConsts.FIELD_NAME, "Empleado", 250));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, SDbConsts.FIELD_CODE, "Clave", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "b_act", "Activo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_tp_name", "Período pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_dep_name", "Departamento"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_pos_name", "Puesto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_last_sal", "Salario al corte $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_cur_sal", "Salario actual $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_last_wage", "Sueldo al corte $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_cur_wage", "Sueldo actual $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "_days", "Días activo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "_abs_abs_not_pay", "Inasistencias no remunerados"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "_abs_dis_dis", "Incapacidades enfermedad general"));
        
        SGridColumnView column = new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "", "Días laborados para PTU");
        column.getRpnArguments().add(new SLibRpnArgument("_days", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument("_abs_abs_not_pay", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument("_abs_dis_dis", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.ADDITION, SLibRpnArgumentType.OPERATOR));
        column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        gridColumnsViews.add(column);

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.HRSU_TP_EMP_DIS);
        moSuscriptionsSet.add(SDataConstants.BPSX_BP_EMP);
    }
}

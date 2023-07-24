/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.gui.grid.SGridFilterPanelEmployee;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.form.SDialogBenefitVacationCardex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDate;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Sergio Flores
 */
public class SViewBenefitVacationStatus extends SGridPaneView implements ActionListener {

    private Date mtDateCutOff;
    private SGridFilterDate moFilterDate;
    private SGridFilterPanelEmployee moFilterEmployee;
    private JButton jbShowCardex;
    private SDialogBenefitVacationCardex moDialogBenefitVacationCardex;
    
    public SViewBenefitVacationStatus(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRSX_BEN_VAC_STAT, 0, title);
        initComponentsCustom();
    }

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        mtDateCutOff = null;
        
        moFilterDate = new SGridFilterDate(miClient, this);
        moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, miClient.getSession().getCurrentDate().getTime()));
        
        jbShowCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Ver movimientos", this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDate);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbShowCardex);
        
        moFilterEmployee = new SGridFilterPanelEmployee(miClient, this);
        moFilterEmployee.initFilter(SGridFilterPanelEmployee.EMP_STATUS_ACT);
        
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterEmployee);
        
        moDialogBenefitVacationCardex = new SDialogBenefitVacationCardex(miClient, SModSysConsts.HRSS_TP_BEN_VAC, "Tarjeta auxiliar de vacaciones");
    }
    
    private void actionShowCardex() {
        if (jbShowCardex.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                
                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    moDialogBenefitVacationCardex.setFormParams(gridRow.getRowPrimaryKey()[0], mtDateCutOff);
                    moDialogBenefitVacationCardex.setVisible(true);
                }
            }
        }
    }
    
    /*
     * Public methods
     */

    /*
     * Overriden methods
     */
    
    @Override
    public void actionMouseClicked() {
        actionShowCardex();
    }
    
    @Override
    public void prepareSqlQuery() {
        String sqlWhere = "";
        String sqlCutoff = "";
        Object filter = null;
        
        moPaneSettings = new SGridPaneSettings(1);

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();
        if (filter != null) {
            mtDateCutOff = (SGuiDate) filter;
            sqlCutoff = "'" + SLibUtils.DbmsDateFormatDate.format(mtDateCutOff) + "'";
        }

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "NOT e.b_del ";
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSS_TP_PAY)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "e.fk_tp_pay = " + ((int[]) filter)[0] + " ";
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSU_DEP)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "e.fk_dep = " + ((int[]) filter)[0] + " ";
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SGridFilterPanelEmployee.EMP_STATUS)).getValue();
        if (filter != null && ((int) filter) != SLibConsts.UNDEFINED) {
            if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ACT) {
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "e.b_act ";
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_INA) {
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "NOT e.b_act ";
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ALL) {
                // all
            }
        }
        
        /*
        NOTE (due to former functionality):
        Meaning of row-view's primary key (required so for cardex dialog):
        ID_1 = employee's ID
        ID_2 = employee's anniversary (starting from 0, 1, 2 and so over)
        ID_3 = days elapsed in employee's current anniversary
        ID_4 = benefit-table's ID
        */
        
        msSql = "SELECT "
                + "e.id_emp AS " + SDbConsts.FIELD_ID + "1, "
                + "b.bp AS " + SDbConsts.FIELD_NAME + ", "
                + "e.num AS " + SDbConsts.FIELD_CODE + ", "
                + "e.b_act AS _act, "
                + "e.dt_hire AS _hire_date, "
                + "e.dt_dis_n AS _dis_date, "
                + "tp.name AS _tp_pay, "
                + "d.name AS _dep, "
                + "/*** Employee's seniority and current anniversary computation (start) ****/ "
                + "" + sqlCutoff + " AS _cur_date, "
                + "YEAR(" + sqlCutoff + ") AS _cur_year, "
                + "@ben_date := e.dt_ben AS _ben_date, "
                + "@ben_year := YEAR(e.dt_ben) AS _ben_year, "
                + "@elap_years := YEAR(" + sqlCutoff + ") - @ben_year AS _elap_years, "
                + "@anniv_prev := @ben_date + INTERVAL (@elap_years - 1) YEAR AS _anniv_prev, "
                + "@anniv_cur := @ben_date + INTERVAL (@elap_years) YEAR AS _anniv_cur, "
                + "@anniv_next := @ben_date + INTERVAL (@elap_years + 1) YEAR AS _anniv_next, "
                + "@anniv_turn := @anniv_cur <= " + sqlCutoff + " _anniv_turn, "
                + "@years := IF(@anniv_turn, @elap_years, @elap_years - 1) AS _years, " // years for seniority
                + "@days := DATEDIFF(" + sqlCutoff + ", IF(@anniv_turn, @anniv_cur, @anniv_prev)) AS _days, " // aritmethic days
                + "@days_x := @days + 1 AS _days_x, " // days for seniority
                + "@anniv_prev_days := DATEDIFF(@anniv_cur, @anniv_prev) AS _anniv_prev_days, "
                + "@anniv_next_days := DATEDIFF(@anniv_next, @anniv_cur) AS _anniv_next_days, "
                + "@anniv := @years + 1 AS _anniv, " // current anniversary
                + "@prop := @days / IF(@anniv_turn, @anniv_next_days, @anniv_prev_days) AS _prop, " //aritmethic proportional year
                + "@prop_x := @days_x / IF(@anniv_turn, @anniv_next_days, @anniv_prev_days) AS _prop_x, " // proportionaly year for seniority
                + "/*** Employee's seniority and current anniversary computation (end) ****/ "
                + "@vac_gain := ("
                + " SELECT COALESCE(SUM(eba.ben_day), 0.0) "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_BEN_ANN) + " AS eba "
                + " WHERE eba.id_emp = e.id_emp AND eba.id_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND eba.id_ann < @anniv) AS _vac_gain, " // vacation gained
                + "@vac_next := COALESCE(("
                + " SELECT eba.ben_day "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_BEN_ANN) + " AS eba "
                + " WHERE eba.id_emp = e.id_emp AND eba.id_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND eba.id_ann = @anniv), 0.0) AS _vac_next, " // vacation next to be gained
                + "@vac_ear := ("
                + " SELECT COALESCE(SUM(pre.unt_all), 0.0) "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                + " WHERE pr.id_emp = e.id_emp AND pre.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND pre.ben_year >= YEAR(e.dt_ben) "
                + " AND ((p.dt_end >= e.dt_ben AND YEAR(p.dt_end) <= YEAR(" + sqlCutoff + ")) OR p.id_pay = 0) AND "
                + " NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del) AS _vac_ear, " // vacation as earnings
                + "@vac_ded := ("
                + " SELECT COALESCE(SUM(prd.unt_all), 0.0) "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp "
                + " WHERE pr.id_emp = e.id_emp AND prd.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND prd.ben_year >= YEAR(e.dt_ben) "
                + " AND ((p.dt_end >= e.dt_ben AND YEAR(p.dt_end) <= YEAR(" + sqlCutoff + ")) OR p.id_pay = 0) AND "
                + " NOT p.b_del AND NOT pr.b_del AND NOT prd.b_del) AS _vac_ded, " // vacation as deductions
                + "(@vac_ear + @vac_ded) AS _vac_cons, " // vacation as deductions are negative
                + "@vac_gain - (@vac_ear + @vac_ded) AS _vac_pend, " // vacation as deductions are negative
                + "@vac_next * @prop AS _vac_prop, "
                + "@vac_next * @prop_x AS _vac_prop_x "
                + ""
                + "FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON e.id_emp = b.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " AS em ON em.id_emp = e.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS tp ON tp.id_tp_pay = e.fk_tp_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS d ON d.id_dep = e.fk_dep "
                + ""
                + (sqlWhere.isEmpty() ? "" : "WHERE " + sqlWhere)
                + "ORDER BY b.bp, e.num;";
    }
    
    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, "Nombre empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, SDbConsts.FIELD_CODE, "Número empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "_act", "Activo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_tp_pay", "Período pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "_dep", "Departamento"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_hire_date", "Última alta"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_dis_date", "Última baja"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_ben_date", "Inicio prestaciones"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_cur_date", "Fecha corte"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_anniv_cur", "Aniv. año corte"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "_anniv_turn", "Aniv. año corte cumplido"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_years", "Años antigüedad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_days_x", "Días antigüedad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_1D, "_vac_gain", "Vac. ganadas (acumuladas)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_1D, "_vac_ear", "Vac. pagadas (acumuladas)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_1D, "_vac_ded", "Vac. deducidas (acumuladas)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_1D, "_vac_cons", "Vac. gozadas (acumuladas)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_1D, "_vac_pend", "Vac. pendientes (acumuladas)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_anniv", "Aniv. actual (en curso)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_8D, "_prop_x", "Parte prop. (aniv. actual)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_1D, "_vac_next", "Vac. x ganar (aniv. actual)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_3D, "_vac_prop_x", "Vac. prop. x ganar (aniv. actual)"));
        
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.HRS_BEN);
        moSuscriptionsSet.add(SModConsts.HRS_EAR);
        moSuscriptionsSet.add(SModConsts.HRS_DED);
        moSuscriptionsSet.add(SModConsts.HRS_PAY);
        moSuscriptionsSet.add(SModConsts.HRS_PAY_RCP_EAR);
        moSuscriptionsSet.add(SModConsts.HRS_PAY_RCP_DED);
        moSuscriptionsSet.add(SModConsts.HRS_EMP_BEN);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbShowCardex) {
                actionShowCardex();
            }
        }
    }
}

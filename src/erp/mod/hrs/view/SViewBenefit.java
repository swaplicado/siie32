/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.gui.grid.SGridFilterPanelEmployee;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SHrsConsts;
import erp.mod.hrs.form.SDialogBenefitCardex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
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
 * @author Juan Barajas, Sergio Flores, Claudio Peña, Sergio Flores
 */
public class SViewBenefit extends SGridPaneView implements ActionListener {

    private Date mtDateCutOff;
    private SGridFilterDate moFilterDate;
    private SGridFilterPanelEmployee moFilterEmployee;
    private JButton jbShowCardex;
    private SDialogBenefitCardex moDialogBenefitCardex;
    
    public SViewBenefit(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRSX_BEN_MOV, gridSubtype, title);
        initComponentsCustom();
    }

    /*
     * Private methods
     */
    
    private boolean isAnnualBonus() {
        return mnGridSubtype == SModSysConsts.HRSS_TP_BEN_ANN_BON;
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        mtDateCutOff = null;
        
        moFilterDate = new SGridFilterDate(miClient, this);
        if (isAnnualBonus()) {
            moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, SLibTimeUtils.getEndOfYear(miClient.getSession().getSystemDate()).getTime()));
        }
        else {
            moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, miClient.getSession().getSystemDate().getTime()));
        }
        
        moFilterEmployee = new SGridFilterPanelEmployee(miClient, this);
        moFilterEmployee.initFilter(SGridFilterPanelEmployee.EMP_STATUS_ACT);
        
        jbShowCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Ver movimientos", this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDate);
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterEmployee);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbShowCardex);
        
        moDialogBenefitCardex = new SDialogBenefitCardex(miClient, mnGridSubtype, "Control de la prestación");
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
                    int[] key = (int[]) gridRow.getRowPrimaryKey();
                    
                    moDialogBenefitCardex.setFormParams(key[0], key[1], key[2], key[3], mtDateCutOff);
                    moDialogBenefitCardex.setVisible(true);
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
        String sql = "";
        String where = "";
        String cutoff = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(4);

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();
        if (filter != null) {
            mtDateCutOff = (SGuiDate) filter;
            cutoff = "'" + SLibUtils.DbmsDateFormatDate.format(mtDateCutOff) + "'";
        }

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "NOT e.b_del ";
            where += (where.isEmpty() ? "" : "AND ") + "NOT e.b_del ";
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
                sql += (sql.isEmpty() ? "" : "AND ") + "e.b_act ";
                where += (where.isEmpty() ? "" : "AND ") + "e.b_act ";
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_INA) {
                sql += (sql.isEmpty() ? "" : "AND ") + "NOT e.b_act ";
                where += (where.isEmpty() ? "" : "AND ") + "NOT e.b_act ";
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ALL) {
                // all
            }
        }
        
        int benefitTypeId = mnGridSubtype;
        String sqlDateBoyCurr = "'" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getBeginOfYear(mtDateCutOff)) + "'";
        String sqlDateBoyPrev = "'" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getBeginOfYear(SLibTimeUtils.addDate(mtDateCutOff, -1, 0, 0))) + "'";
        
        /*
        NOTE:
        Shows requested benefit type estimations and payments of days and amounts, 
        from current year and previous one, according as well to cut off date.
        */
        
        /*
        NOTE (due to former functionality):
        Meaning of row-view's primary key (required so for cardex dialog):
        ID_1 = employee's ID
        ID_2 = employee's anniversary (starting from 0, 1, 2 and so over)
        ID_3 = days elapsed in employee's current anniversary
        ID_4 = benefit-table's ID
        */
        
        // # employee's general info:
        msSql = "SELECT "
                + "bp.id_bp AS _emp_id, bp.bp AS _emp_name, CAST(e.num AS UNSIGNED INTEGER) AS _emp_num, e.b_act AS _emp_act, "
                + "e.fk_tp_pay AS _pay_tp_id, tp.name AS _pay_tp_name, "
                + "e.dt_ben AS _emp_dt_ben, e.dt_dis_n AS _emp_dt_dis_n, cc.id_cc AS _id_cc, ";
        
        // # cut off date:
        String sqlCutoff = "IF(e.b_act, " + cutoff + ", e.dt_dis_n)";
        
        msSql += sqlCutoff + " AS _p_dt_cutoff, ";
        
        // # employee's seniority:
        String sqlSenRaw = "ROUND(DATEDIFF(" + sqlCutoff + ", e.dt_ben) / " + SHrsConsts.YEAR_DAYS + ", 4)";
        String sqlSenAsYears = "TIMESTAMPDIFF(YEAR, e.dt_ben, " + sqlCutoff + ")";
        String sqlSenAsMonths = "TIMESTAMPDIFF(MONTH, e.dt_ben, " + sqlCutoff + ")";
        
        msSql += sqlSenRaw + " AS _sen_raw, "
                + sqlSenAsYears + " AS _sen_as_years, "
                + sqlSenAsMonths + " AS _sen_as_months, ";
                
        // # employee's salary per day:
        String sqlCurrSalDay = "ROUND(IF(e.fk_tp_pay = " + SModSysConsts.HRSS_TP_PAY_WEE + ", e.sal, e.wage * " + SHrsConsts.YEAR_MONTHS + " / " + SHrsConsts.YEAR_DAYS + "), 2)";
        
        msSql += sqlCurrSalDay + " AS _curr_sal_day, ";
        
        // # employee's anniversary and benefit's year:
        String sqlCurrBenAnniv = isAnnualBonus() ? "IF(" + sqlSenAsYears + " = 0, 1, " + sqlSenAsYears + ")" : "(" + sqlSenAsYears + " + 1)";
        String sqlCurrBenYear = "YEAR(ADDDATE(e.dt_ben, INTERVAL " + sqlSenAsYears + " YEAR))";
        String sqlPrevBenAnniv = isAnnualBonus() ? "IF(" + sqlSenAsYears + " = 0, NULL, " + sqlSenAsYears + " - 1)" : "IF(" + sqlSenAsYears + " = 0, NULL, " + sqlSenAsYears + ")";
        String sqlPrevBenYear = "IF(" + sqlSenAsYears + " = 0, NULL, " + sqlCurrBenYear + " - 1)";
        
        msSql += sqlCurrBenAnniv + " AS _curr_ben_anniv, "
                + sqlCurrBenYear + " AS _curr_ben_year, "
                + sqlPrevBenAnniv + " AS _prev_ben_anniv, "
                + sqlPrevBenYear + " AS _prev_ben_year, ";
        
        // # employee's benefit's base date, elapsed days and proportional factor:
        String sqlCurrDtBase = "IF(" + benefitTypeId + " = " + SModSysConsts.HRSS_TP_BEN_ANN_BON + ", IF(e.dt_ben < " + sqlDateBoyCurr + ", " + sqlDateBoyCurr + ", e.dt_ben), ADDDATE(e.dt_ben, INTERVAL " + sqlSenAsYears + " YEAR))";
        String sqlCurrDaysElapsed = "DATEDIFF(" + sqlCutoff + ", " + sqlCurrDtBase + ")";
        String sqlCurrProp = "(" + sqlCurrDaysElapsed + " / " + SHrsConsts.YEAR_DAYS + ")";
        String sqlPrevDtBase = "IF(" + sqlSenAsYears + " = 0, NULL, IF(" + benefitTypeId + " = " + SModSysConsts.HRSS_TP_BEN_ANN_BON + ", IF(e.dt_ben < " + sqlDateBoyPrev + ", " + sqlDateBoyPrev + ", e.dt_ben), ADDDATE(e.dt_ben, INTERVAL " + sqlSenAsYears + " - 1 YEAR)))";
        String sqlPrevDaysElapsed = "IF(" + sqlSenAsYears + " = 0, NULL, DATEDIFF(" + sqlCutoff + ", " + sqlPrevDtBase + ") - " + sqlCurrDaysElapsed + ")";
        String sqlPrevProp = "IF(" + sqlSenAsYears + " = 0, NULL, " + sqlPrevDaysElapsed + " / " + SHrsConsts.YEAR_DAYS + ")";
        
        msSql += sqlCurrDtBase + " AS _curr_dt_base, "
                + sqlCurrDaysElapsed + " AS _curr_days_elapsed, "
                + sqlCurrProp + " AS _curr_prop, "
                + sqlPrevDtBase + " AS _prev_dt_base, "
                + sqlPrevDaysElapsed + " AS _prev_days_elapsed, "
                + sqlPrevProp + " AS _prev_prop, ";
        
        // # employee's benefit table and bonus benefit table (if applicable):
        String sqlBenDayId = "COALESCE("
                + "(SELECT b.id_ben FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " AS b "
                + "WHERE NOT b.b_del AND b.uni = e.b_uni AND b.fk_tp_ben = IF(" + benefitTypeId + " = " + SModSysConsts.HRSS_TP_BEN_VAC_BON + ", " + SModSysConsts.HRSS_TP_BEN_VAC + ", " + benefitTypeId + ") AND b.dt_sta <= " + sqlCutoff + " AND b.fk_tp_pay_n = e.fk_tp_pay "
                + "ORDER BY b.dt_sta DESC, b.id_ben LIMIT 1), "
                + "(SELECT b.id_ben FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " AS b "
                + "WHERE NOT b.b_del AND b.uni = e.b_uni AND b.fk_tp_ben = IF(" + benefitTypeId + " = " + SModSysConsts.HRSS_TP_BEN_VAC_BON + ", " + SModSysConsts.HRSS_TP_BEN_VAC + ", " + benefitTypeId + ") AND b.dt_sta <= " + sqlCutoff + " AND b.fk_tp_pay_n IS NULL "
                + "ORDER BY b.dt_sta DESC, b.id_ben LIMIT 1))";
        String sqlBenBonId = "COALESCE("
                + "IF(" + benefitTypeId + " <> " + SModSysConsts.HRSS_TP_BEN_VAC_BON + ", 0, "
                + "(SELECT b.id_ben FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " AS b "
                + "WHERE NOT b.b_del AND b.uni = e.b_uni AND b.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC_BON + " AND b.dt_sta <= " + sqlCutoff + " AND b.fk_tp_pay_n = e.fk_tp_pay "
                + "ORDER BY b.dt_sta DESC, b.id_ben LIMIT 1)), "
                + "IF(" + benefitTypeId + " <> " + SModSysConsts.HRSS_TP_BEN_VAC_BON + ", 0, "
                + "(SELECT b.id_ben FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " AS b "
                + "WHERE NOT b.b_del AND b.uni = e.b_uni AND b.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC_BON + " AND b.dt_sta <= " + sqlCutoff + " AND b.fk_tp_pay_n IS NULL "
                + "ORDER BY b.dt_sta DESC, b.id_ben LIMIT 1)))";
        
        msSql += "(SELECT b.name FROM hrs_ben AS b WHERE b.id_ben = " + sqlBenDayId + ") AS _ben_day_name, ";
        
        msSql += "(SELECT b.name FROM hrs_ben AS b WHERE b.id_ben = " + sqlBenBonId + ") AS _ben_bon_name, ";
        
        // # estimated current-year benefit:
        String sqlCurrBenDays = "COALESCE((SELECT br.ben_day FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN_ROW) + " AS br "
                + "WHERE br.id_ben = " + sqlBenDayId + " AND " + (isAnnualBonus() ? sqlSenAsYears : sqlSenRaw) + " <= (br.mon / " + SLibTimeConsts.MONTHS + ") ORDER BY br.id_row LIMIT 1), 0)";
        String sqlCurrBenDaysProp = "(" + sqlCurrBenDays + " * " + sqlCurrProp + ")";
        String sqlCurrBenAmtProp = "ROUND(" + sqlCurrSalDay + " * " + sqlCurrBenDaysProp + ", 2)";
        String sqlCurrBenBonPerc = "COALESCE((SELECT br.ben_bon_per FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN_ROW) + " AS br "
                + "WHERE br.id_ben = " + sqlBenBonId + " AND " + (isAnnualBonus() ? sqlSenAsYears : sqlSenRaw) + " <= (br.mon / " + SLibTimeConsts.MONTHS + ") ORDER BY br.id_row LIMIT 1), 0.0)";
        String sqlCurrBenBonAmtProp = "ROUND(" + sqlCurrBenAmtProp + " * " + sqlCurrBenBonPerc + ", 2)";
        
        msSql += sqlCurrBenDays + " AS _curr_ben_days, "
                + sqlCurrBenDaysProp + " AS _curr_ben_days_prop, "
                + sqlCurrBenAmtProp + " AS _curr_ben_amt_prop, "
                + sqlCurrBenBonPerc + " _curr_ben_bon_perc, "
                + sqlCurrBenBonAmtProp + " AS _curr_ben_bon_amt_prop, ";
        
        // # estimated previous-year benefit:
        String sqlPevBenDays = "IF(" + sqlSenAsYears + " < 1, 0, "
                + "COALESCE((SELECT br.ben_day FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN_ROW) + " AS br "
                + "WHERE br.id_ben = " + sqlBenDayId + " AND (" + (isAnnualBonus() ? sqlSenAsYears : sqlSenRaw) + " - 1.0) <= (br.mon / " + SLibTimeConsts.MONTHS + ") ORDER BY br.id_row LIMIT 1), 0))";
        String sqlPrevBenDaysProp = "(" + sqlPevBenDays + " * " + sqlPrevProp + ")";
        String sqlPrevBenAmtProp = "ROUND(" + sqlCurrSalDay + " * " + sqlPrevBenDaysProp + ", 2)";
        String sqlPrevBenBonPerc = "IF(" + sqlSenAsYears + " < 1, 0.0, "
                + "COALESCE((SELECT br.ben_bon_per FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN_ROW) + " AS br "
                + "WHERE br.id_ben = " + sqlBenBonId + " AND (" + (isAnnualBonus() ? sqlSenAsYears : sqlSenRaw) + " - 1.0) <= (br.mon / " + SLibTimeConsts.MONTHS + ") ORDER BY br.id_row LIMIT 1), 0.0))";
        String sqlPrevBenBonAmtProp = "ROUND(" + sqlPrevBenAmtProp + " * " + sqlPrevBenBonPerc + ", 2)";
        
        msSql += sqlPevBenDays + " AS _prev_ben_days, "
                + sqlPrevBenDaysProp + " AS _prev_ben_days_prop, "
                + sqlPrevBenAmtProp + " AS _prev_ben_amt_prop, "
                + sqlPrevBenBonPerc + " AS _prev_ben_bon_perc, "
                + sqlPrevBenBonAmtProp + " AS _prev_ben_bon_amt_prop, ";
                
        // # payed current-year benefit:
        String sqlCurrPayDays = "COALESCE(tcur.ben_unt, 0.0)";
        String sqlCurrPayAmt = "COALESCE(tcur.ben_amt, 0.0)";
        
        msSql += sqlCurrPayDays + " AS _curr_pay_days, "
                + sqlCurrPayAmt + " AS _curr_pay_amt, ";
                        
        // # payed previous-year benefit:
        String sqlPrevPayDays = "COALESCE(tprev.ben_unt, 0.0)";
        String sqlPrevPayAmt = "COALESCE(tprev.ben_amt, 0.0)";
        
        msSql += sqlPrevPayDays + " AS _prev_pay_days, "
                + sqlPrevPayAmt + " AS _prev_pay_amt, ";
                
        // # diff estimated vs. payed current-year benefit:
        msSql +=
                "COALESCE(" + sqlCurrBenDaysProp + ", 0.0) - " + sqlCurrPayDays + " AS _diff_curr_pay_days, "
                + "COALESCE(IF(" + benefitTypeId + " = " + SModSysConsts.HRSS_TP_BEN_VAC_BON + ", " + sqlCurrBenBonAmtProp + ", " + sqlCurrBenAmtProp + "), 0.0) - " + sqlCurrPayAmt + " AS _diff_curr_pay_amt, ";
        
        // # diff estimated vs. payed previous-year benefit:
        msSql +=
                "COALESCE(" + sqlPrevBenDaysProp+ ", 0.0) - " + sqlPrevPayDays + " AS _diff_prev_pay_days, "
                + "COALESCE(IF(" + benefitTypeId + " = " + SModSysConsts.HRSS_TP_BEN_VAC_BON + ", " + sqlPrevBenBonAmtProp + ", " + sqlPrevBenAmtProp + "), 0.0) - " + sqlPrevPayAmt + " AS _diff_prev_pay_amt, ";
        
        msSql +=
                /********************************************************************************/
                "bp.id_bp AS " + SDbConsts.FIELD_ID + "1, "
                + "" + sqlSenAsYears + " AS " + SDbConsts.FIELD_ID + "2, "
                + "" + sqlCurrDaysElapsed + " AS " + SDbConsts.FIELD_ID + "3, "
                + "IF(" + benefitTypeId + " = " + SModSysConsts.HRSS_TP_BEN_VAC_BON + ", " + sqlBenBonId + ", " + sqlBenDayId + ") AS " + SDbConsts.FIELD_ID + "4, "
                + "bp.bp AS " + SDbConsts.FIELD_NAME + ", "
                + "e.num AS " + SDbConsts.FIELD_CODE + " "
                /********************************************************************************/
                // # main query's source tables:
                + "FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON e.id_emp = bp.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " AS em ON em.id_emp = bp.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS tp ON e.fk_tp_pay = tp.id_tp_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON e.fk_dep = dep.id_dep "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_DEP_CC) + " AS depcc ON depcc.id_dep = dep.id_dep "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cc ON cc.pk_cc = depcc.fk_cc "
                + ""
                // # retrieve current benefit payed:
                + "LEFT OUTER JOIN ("
                + " SELECT t.id_emp, t.ben_year, t.ben_ann, SUM(t.ben_unt) AS ben_unt, SUM(t.ben_amt) AS ben_amt "
                + " FROM ("
                //   # benefit payed to employee as earning:
                + "  SELECT pre.id_emp, pre.ben_year, pre.ben_ann, SUM(pre.unt_all) AS ben_unt, SUM(pre.amt_r) AS ben_amt "
                + "  FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON pr.id_emp = e.id_emp "
                + "  WHERE pre.fk_tp_ben = " + benefitTypeId + " " + (where.isEmpty() ? "" : " AND " + where) + "AND "
                + "  pre.ben_year = " + sqlCurrBenYear + " AND pre.ben_ann = " + sqlCurrBenAnniv + " AND "
                + "  NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del "
                + "  GROUP BY pre.id_emp, pre.ben_year, pre.ben_ann "
                + "  UNION "
                //   # benefit refund to employer as deduction:
                + "  SELECT prd.id_emp, prd.ben_year, prd.ben_ann, -SUM(prd.unt_all) AS ben_unt, -SUM(prd.amt_r) AS ben_amt "
                + "  FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON pr.id_emp = e.id_emp "
                + "  WHERE prd.fk_tp_ben = " + benefitTypeId + " " + (where.isEmpty() ? "" : " AND " + where) + "AND "
                + "  prd.ben_year = " + sqlCurrBenYear + " AND prd.ben_ann = " + sqlCurrBenAnniv + " AND "
                + "  NOT p.b_del AND NOT pr.b_del AND NOT prd.b_del "
                + "  GROUP BY prd.id_emp, prd.ben_year, prd.ben_ann "
                + "  ORDER BY id_emp, ben_year, ben_ann) AS t "
                + " GROUP BY id_emp, ben_year, ben_ann "
                + " ORDER BY id_emp, ben_year, ben_ann) AS tcur ON tcur.id_emp = bp.id_bp "
                + ""
                // # retrieve previous benefit payed:
                + "LEFT OUTER JOIN ("
                + " SELECT t.id_emp, t.ben_year, t.ben_ann, SUM(t.ben_unt) AS ben_unt, SUM(t.ben_amt) AS ben_amt "
                + " FROM ("
                //   # benefit payed to employee as earning:
                + "  SELECT pre.id_emp, pre.ben_year, pre.ben_ann, SUM(pre.unt_all) AS ben_unt, SUM(pre.amt_r) AS ben_amt "
                + "  FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON pr.id_emp = e.id_emp "
                + "  WHERE pre.fk_tp_ben = " + benefitTypeId + " " + (where.isEmpty() ? "" : " AND " + where) + "AND "
                + "  pre.ben_year = " + sqlPrevBenYear + " AND pre.ben_ann = " + sqlPrevBenAnniv + " AND "
                + "  NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del "
                + "  GROUP BY pre.id_emp, pre.ben_year, pre.ben_ann "
                + "  UNION "
                //   # benefit refund to employer as deduction:
                + "  SELECT prd.id_emp, prd.ben_year, prd.ben_ann, -SUM(prd.unt_all) AS ben_unt, -SUM(prd.amt_r) AS ben_amt "
                + "  FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON pr.id_emp = e.id_emp "
                + "  WHERE prd.fk_tp_ben = " + benefitTypeId + " " + (where.isEmpty() ? "" : " AND " + where) + "AND "
                + "  prd.ben_year = " + sqlPrevBenYear + " AND prd.ben_ann = " + sqlPrevBenAnniv + " AND "
                + "  NOT p.b_del AND NOT pr.b_del AND NOT prd.b_del "
                + "  GROUP BY prd.id_emp, prd.ben_year, prd.ben_ann "
                + "  ORDER BY id_emp, ben_year, ben_ann) AS t "
                + " GROUP BY id_emp, ben_year, ben_ann "
                + " ORDER BY id_emp, ben_year, ben_ann) AS tprev ON tprev.id_emp = bp.id_bp "
                + ""
                + "WHERE e.dt_ben <= " + cutoff + " AND (e.b_act OR (NOT e.b_act AND e.dt_dis_n <= " + cutoff + ")) " + (sql.isEmpty() ? "" : "AND " + sql)
                + ""
                + "ORDER BY bp.bp, bp.id_bp ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        String benefit = "?";
        switch (mnGridSubtype) {
            case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                benefit = "gratificación anual";
                break;
            case SModSysConsts.HRSS_TP_BEN_VAC:
            case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                benefit = "vacaciones";
                break;
            default:
        }
        
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, "Nombre empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "_emp_num", "Número empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "_emp_act", "Activo empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_pay_tp_name", "Período pago", 75));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_curr_sal_day", "Salario diario $", 75));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_emp_dt_ben", "Inicio prestaciones"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_emp_dt_dis_n", "Última baja"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_p_dt_cutoff", "Corte"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_sen_as_months", "Antigüedad meses", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_sen_raw", "Antigüedad años", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_ben_day_name", "Tabla " + benefit));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_id_cc", "No. centro de costo"));
        
        if (mnGridSubtype == SModSysConsts.HRSS_TP_BEN_VAC_BON) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_ben_bon_name", "Tabla prima vacacional"));
        }
        
        // benefit of current year:
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, "_curr_ben_year", "Año " + benefit + " actual"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "_curr_ben_anniv", "Aniv " + benefit + " actual", 35));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_curr_dt_base", "Base actual"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_curr_days_elapsed", "D transcurridos actual"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "_curr_prop", "Parte prop actual"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_curr_ben_days", "D " + benefit + " actual"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_curr_ben_days_prop", "D prop " + benefit + " actual"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_curr_ben_amt_prop", "$ prop " + benefit + " actual"));
        
        if (mnGridSubtype == SModSysConsts.HRSS_TP_BEN_VAC_BON) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_DISC, "_curr_ben_bon_perc", "% prima vac actual"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_curr_ben_bon_amt_prop", "$ prima vac actual"));
        }
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_curr_pay_days", "Acum D " + benefit + " actual"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_curr_pay_amt", "Acum $ " + benefit + " actual"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_diff_curr_pay_days", "Rezago D " + benefit + " actual"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_diff_curr_pay_amt", "Rezago $ " + benefit + " actual"));
        
        // benefit of previous year:
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, "_prev_ben_year", "Año " + benefit + " prev"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "_prev_ben_anniv", "Aniv " + benefit + " prev", 35));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_prev_dt_base", "Base prev"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_prev_days_elapsed", "D transcurridos prev"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "_prev_prop", "Parte prop prev"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_prev_ben_days", "D " + benefit + " prev"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_prev_ben_days_prop", "D prop " + benefit + " prev"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_prev_ben_amt_prop", "$ prop " + benefit + " prev"));
        
        if (mnGridSubtype == SModSysConsts.HRSS_TP_BEN_VAC_BON) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_DISC, "_prev_ben_bon_perc", "% prima vac prev"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_prev_ben_bon_amt_prop", "$ prima vac prev"));
        }
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_prev_pay_days", "Acum D " + benefit + " prev"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_prev_pay_amt", "Acum $ " + benefit + " prev"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "_diff_prev_pay_days", "Rezago D " + benefit + " prev"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_diff_prev_pay_amt", "Rezago $ " + benefit + " prev"));
        
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

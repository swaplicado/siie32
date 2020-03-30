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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibConsts;
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
 * @author Sergio Flores
 */
public class SViewBenefitVacationPending extends SGridPaneView implements ActionListener {

    private Date mtDateCutOff;
    private SGridFilterDate moFilterDate;
    private SGridFilterPanelEmployee moFilterEmployee;
    private JButton jbShowCardex;
    private SDialogBenefitCardex moDialogBenefitCardex;
    
    public SViewBenefitVacationPending(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRSX_BEN_VAC_PEND, 0, title);
        initComponentsCustom();
    }

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        mtDateCutOff = null;
        
        moFilterDate = new SGridFilterDate(miClient, this);
        if (mnGridSubtype == SModSysConsts.HRSS_TP_BEN_ANN_BON) {
            moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, SLibTimeUtils.getEndOfYear(miClient.getSession().getSystemDate()).getTime()));
        }
        else {
            moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, miClient.getSession().getSystemDate().getTime()));
        }
        
        moFilterEmployee = new SGridFilterPanelEmployee(miClient, this, SModConsts.HRSS_TP_PAY, SModConsts.HRSU_DEP);
        moFilterEmployee.initFilter(null);
        
        jbShowCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Ver movimientos", this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDate);
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterEmployee);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbShowCardex);
        
        moDialogBenefitCardex = new SDialogBenefitCardex(miClient, SModSysConsts.HRSS_TP_BEN_VAC, "Control de la prestación");
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
    
    /**
     * Populate database table of benefit row auxiliars.
     * That are an explode of benefit rows to fill up a registry for every year of seniority.
     * @throws Exception 
     */
    private void populateBenefitRowAuxiliars() throws Exception {
        String sql = "SELECT br.id_ben, br.id_row, br.mon, br.ben_day "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " AS b "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_BEN_ROW) + " AS br ON br.id_ben = b.id_ben "
                + "WHERE NOT b.b_del AND b.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND "
                + "b.fk_ear = (SELECT id_ear FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " "
                + " WHERE fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_EAR + " AND fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND "
                + " fk_cl_abs_n IS NOT NULL ORDER BY id_ear LIMIT 1) "
                + "ORDER BY br.id_ben, br.id_row;";
        
        try (Statement statement = miClient.getSession().getStatement().getConnection().createStatement()) {
            int year = 0;
            int benefitId = 0;
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                if (benefitId != resultSet.getInt("br.id_ben")) {
                    benefitId = resultSet.getInt("br.id_ben");
                    sql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN_ROW_AUX) + " "
                            + "WHERE id_ben = " + benefitId + ";";
                    miClient.getSession().getStatement().execute(sql);
                    
                    year = 1; // reset
                }
                
                int benefitRowId = resultSet.getInt("br.id_row");
                int benefitYear = resultSet.getInt("br.mon") / SHrsConsts.YEAR_MONTHS;
                int benefitDays = resultSet.getInt("br.ben_day");
                int auxId = 1;
                
                while (year <= benefitYear) {
                    sql = "INSERT INTO " + SModConsts.TablesMap.get(SModConsts.HRS_BEN_ROW_AUX) + " VALUES ("
                            + benefitId + ", " + benefitRowId + ", " + auxId++ + ", " + year++ + ", " + benefitDays + ", 0);";
                    miClient.getSession().getStatement().execute(sql);
                }
            }
        }
    }
    
    /**
     * Create map of ID of benefit that corresponds to each payment type, including de "default" table of benefits.
     * @return Map: key = ID of payment type; value = ID of benefit.
     * @throws Exception 
     */
    private HashMap<Integer, Integer> createPaymentTypeBenefitVacationMap() throws Exception {
        HashMap<Integer, Integer> map = new HashMap<>();
        
        String sql = "SELECT tp.id_tp_pay, b.id_ben "
                + "FROM hrs_ben AS b "
                + "RIGHT OUTER JOIN erp.hrss_tp_pay AS tp ON b.fk_tp_pay_n = tp.id_tp_pay OR b.fk_tp_pay_n IS NULL "
                + "WHERE NOT b.b_del AND b.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND "
                + "b.fk_ear = (SELECT id_ear FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " "
                + " WHERE fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_EAR + " AND fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND "
                + " fk_cl_abs_n IS NOT NULL ORDER BY id_ear LIMIT 1) "
                + "UNION "
                + "SELECT NULL AS id_tp_pay, b.id_ben "
                + "FROM hrs_ben AS b "
                + "WHERE NOT b.b_del AND b.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND "
                + "b.fk_ear = (SELECT id_ear FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " "
                + " WHERE fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_EAR + " AND fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND "
                + " fk_cl_abs_n IS NOT NULL ORDER BY id_ear LIMIT 1) "
                + "ORDER BY id_tp_pay;";
        
        try (Statement statement = miClient.getSession().getStatement().getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                map.put(resultSet.getInt("id_tp_pay"), resultSet.getInt("id_ben"));
            }
        }
        
        return map;
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
        String sqlCutOff = "";
        String sqlBenefit = "";
        Object filter = null;
        
        try {
            // prepare table of benefit row auxiliars:
            populateBenefitRowAuxiliars();
            
            // prepare SQL filter for table of benefits:
            
            HashMap<Integer, Integer> paymentTypeBenefitVacationMap = createPaymentTypeBenefitVacationMap();
            Integer defaultBenefitId = paymentTypeBenefitVacationMap.get(0); // default benefit
            Integer benefitId;
            
            sqlBenefit = "CASE ";
            
            benefitId = paymentTypeBenefitVacationMap.get(SModSysConsts.HRSS_TP_PAY_WEE);
            sqlBenefit += "WHEN e.fk_tp_pay = " + SModSysConsts.HRSS_TP_PAY_WEE + " THEN " + (benefitId == null ? defaultBenefitId : benefitId) + " ";
            
            benefitId = paymentTypeBenefitVacationMap.get(SModSysConsts.HRSS_TP_PAY_FOR);
            sqlBenefit += "WHEN e.fk_tp_pay = " + SModSysConsts.HRSS_TP_PAY_FOR + " THEN " + (benefitId == null ? defaultBenefitId : benefitId) + " ";
            
            sqlBenefit += "ELSE " + (defaultBenefitId == null ? 0 : defaultBenefitId) + " ";
            
            sqlBenefit += "END";
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }

        moPaneSettings = new SGridPaneSettings(4);

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();
        if (filter != null) {
            mtDateCutOff = (SGuiDate) filter;
            sqlCutOff = "'" + SLibUtils.DbmsDateFormatDate.format(mtDateCutOff) + "'";
        }

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "NOT e.b_del ";
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
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_INA) {
                sql += (sql.isEmpty() ? "" : "AND ") + "NOT e.b_act ";
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
        
        msSql = "SELECT b.id_bp AS _employee_id, b.bp AS _employee, e.num AS _employee_number, d.name AS _department, "
                + "e.dt_ben AS _benefits, e.dt_dis_n AS _dismiss, e.b_act AS _active, tp.name AS _payment_type, "
                + "@cut_off := IF(e.b_act, " + sqlCutOff + ", e.dt_dis_n) AS _cut_off, "
                + "@seniority := TIMESTAMPDIFF(YEAR, e.dt_ben, @cut_off) AS _seniority, "
                + "@vac_right := (SELECT COALESCE(SUM(bra.ben_day), 0) "
                + "  FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN_ROW_AUX) + " AS bra "
                + "  WHERE bra.id_ben = " + sqlBenefit + " AND "
                + "  bra.ann <= TIMESTAMPDIFF(YEAR, e.dt_ben, @cut_off)) AS _vac_right, "
                + "@vac_ear := (SELECT COALESCE(SUM(pre.unt_all), 0.0) "
                + "  FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                + "  WHERE pre.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del AND "
                + "  pre.ben_year > 0 AND pre.ben_year >= YEAR(e.dt_ben) AND "
                + "  pr.id_emp = e.id_emp AND ((p.dt_end >= e.dt_ben AND p.dt_end <= @cut_off) OR p.id_pay = 0)) AS _vac_ear, "
                + "@vac_ded := (SELECT COALESCE(SUM(prd.unt_all), 0.0) "
                + "  FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp "
                + "  WHERE prd.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND NOT p.b_del AND NOT pr.b_del AND NOT prd.b_del AND "
                + "  prd.ben_year > 0 AND prd.ben_year >= YEAR(e.dt_ben) AND "
                + "  pr.id_emp = e.id_emp AND ((p.dt_end >= e.dt_ben AND p.dt_end <= @cut_off) OR p.id_pay = 0)) AS _vac_ded, "
                + "@vac_right - (@vac_ear + @vac_ded) AS _vac_pend, "
                + "b.id_bp AS " + SDbConsts.FIELD_ID + "1, "
                + "@seniority AS " + SDbConsts.FIELD_ID + "2, "
                + "DATEDIFF(@cut_off, DATE_ADD(e.dt_ben, INTERVAL @seniority YEAR)) AS " + SDbConsts.FIELD_ID + "3, "
                + sqlBenefit + " AS " + SDbConsts.FIELD_ID + "4, "
                + "b.bp AS " + SDbConsts.FIELD_NAME + ", "
                + "e.num AS " + SDbConsts.FIELD_CODE + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON b.id_bp = e.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " AS em ON b.id_bp = em.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS d ON e.fk_dep = d.id_dep "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS tp ON e.fk_tp_pay = tp.id_tp_pay "
                + "WHERE " + sql
                + "ORDER BY b.bp, b.id_bp;";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "_employee", "Empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "_employee_number", "Clave"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "_active", "Activo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_payment_type", "Período pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "_department", "Departamento"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_benefits", "Inicio beneficios"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_dismiss", "Última baja"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_cut_off", "Corte"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "_seniority", "Antigüedad años", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_4B, "_vac_right", "Total vacaciones"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "_vac_ear", "Vac pagadas"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "_vac_ded", "Vac deducidas"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "_vac_pend", "Vac pendientes"));
        
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

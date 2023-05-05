/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.gui.grid.SGridFilterPanelEmployee;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.form.SDialogBenefitTablesMassiveAssignation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDate;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Sergio Flores
 */
public class SViewEmployeeBenefitTables extends SGridPaneView implements ActionListener {

    private SGridFilterDate moFilterDate;
    private SGridFilterPanelEmployee moFilterEmployee;
    private JButton jbShowMassiveAssignation;
    
    public SViewEmployeeBenefitTables(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_EMP_BEN, 0, title);
        initComponentsCustom();
    }

    /*
     * Private methods
     */
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(false);

        moFilterDate = new SGridFilterDate(miClient, this);
        moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, miClient.getSession().getSystemDate().getTime()));

        moFilterEmployee = new SGridFilterPanelEmployee(miClient, this);
        moFilterEmployee.initFilter(SGridFilterPanelEmployee.EMP_STATUS_ACT);

        jbShowMassiveAssignation = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_link.gif")), "Actualización masiva de prestaciones", this);

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDate);
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterEmployee);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbShowMassiveAssignation);
    }

    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

    @Override
    public void prepareSqlQuery() {
        String where = "";
        String cutoff = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();
        if (filter != null) {
            cutoff = "'" + SLibUtils.DbmsDateFormatDate.format((SGuiDate) filter) + "'";
        }

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "NOT e.b_del AND NOT b.b_del ";
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSS_TP_PAY)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            where += (where.isEmpty() ? "" : "AND ") + "e.fk_tp_pay = " + ((int[]) filter)[0] + " ";
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSU_DEP)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            where += (where.isEmpty() ? "" : "AND ") + "e.fk_dep = " + ((int[]) filter)[0] + " ";
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SGridFilterPanelEmployee.EMP_STATUS)).getValue();
        if (filter != null && ((int) filter) != SLibConsts.UNDEFINED) {
            if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ACT) {
                where += (where.isEmpty() ? "" : "AND ") + "e.b_act ";
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_INA) {
                where += (where.isEmpty() ? "" : "AND ") + "NOT e.b_act ";
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ALL) {
                // all
            }
        }
        
        if (cutoff.isEmpty()) {
            cutoff = "'" + SLibUtils.DbmsDateFormatDate.format(miClient.getSession().getSystemDate()) + "'";
        }
        
        msSql = "SELECT "
                + "e.id_emp AS " + SDbConsts.FIELD_ID + "1, "
                + "b.bp AS " + SDbConsts.FIELD_NAME + ", "
                + "e.num AS " + SDbConsts.FIELD_CODE + ", "
                + "e.dt_ben, "
                + "e.dt_dis_n, "
                + "e.b_uni, "
                + "e.b_act, "
                + "e.b_del, "
                + "tpay.code, "
                + "tpay.name, "
                + "dep.code, "
                + "dep.name, "
                + "" + cutoff + " AS _cur_date, "
                + "YEAR(" + cutoff + ") AS _cur_year, "
                + "@ben_date := e.dt_ben AS _ben_date, "
                + "@ben_year := YEAR(e.dt_ben) AS _ben_year, "
                + "@elap_years := YEAR(" + cutoff + ") - @ben_year AS _elap_years, "
                + "@anniv_prev := @ben_date + INTERVAL (@elap_years - 1) YEAR AS _anniv_prev, "
                + "@anniv_cur := @ben_date + INTERVAL (@elap_years) YEAR AS _anniv_cur, "
                + "@anniv_next := @ben_date + INTERVAL (@elap_years + 1) YEAR AS _anniv_next, "
                + "@anniv_turn := @anniv_cur <= " + cutoff + " _anniv_turn, "
                + "@years := IF(@anniv_turn, @elap_years, @elap_years - 1) AS _years, "
                + "@days := DATEDIFF(" + cutoff + ", IF(@anniv_turn, @anniv_cur, @anniv_prev)) AS _days, "
                + "@days_x := @days + 1 AS _days_x, "
                + "@anniv_prev_days := DATEDIFF(@anniv_cur, @anniv_prev) AS _anniv_prev_days, "
                + "@anniv_next_days := DATEDIFF(@anniv_next, @anniv_cur) AS _anniv_next_days, "
                + "@anniv := @years + 1 AS _anniv, "
                + "@prop := @days / IF(@anniv_turn, @anniv_next_days, @anniv_prev_days) AS _prop, "
                + "@prop_x := @days_x / IF(@anniv_turn, @anniv_next_days, @anniv_prev_days) AS _prop_x, "
                + "eb.sta_ann_bon, "
                + "eb.sta_vac, "
                + "eb.sta_vac_bon, "
                + "bab.name, "
                + "bab.code, "
                + "bv.name, "
                + "bv.code, "
                + "bvb.name, "
                + "bvb.code, "
                + "uab.usr, "
                + "eb.ts_usr_upd_ann_bon, "
                + "uv.usr, "
                + "eb.ts_usr_upd_vac, "
                + "uvb.usr, "
                + "eb.ts_usr_upd_vac_bon, "
                + "eb.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "eb.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "eb.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "eb.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM "
                + "" + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " AS em ON em.id_emp = e.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = e.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS tpay ON tpay.id_tp_pay = e.fk_tp_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON dep.id_dep = e.fk_dep "
                + "LEFT OUTER JOIN ( "
                + "SELECT id_emp, MAX(id_ben) AS id_ben "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_BEN) + " "
                + "WHERE NOT b_del AND NOT b_obs "
                + "GROUP BY id_emp "
                + "ORDER BY id_emp) AS t ON t.id_emp = e.id_emp "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_BEN) + " AS eb ON eb.id_emp = t.id_emp AND eb.id_ben = t.id_ben "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " AS bab ON bab.id_ben = eb.fk_ben_ann_bon "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " AS bv ON bv.id_ben = eb.fk_ben_vac "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " AS bvb ON bvb.id_ben = eb.fk_ben_vac_bon "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uab ON uab.id_usr = eb.fk_usr_upd_ann_bon "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uv ON uv.id_usr = eb.fk_usr_upd_vac "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uvb ON uvb.id_usr = eb.fk_usr_upd_vac_bon "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON ui.id_usr = eb.fk_usr_ins "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON uu.id_usr = eb.fk_usr_upd "
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "ORDER BY "
                + "b.bp, e.num, e.id_emp;";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        
        String annualBonus = ((String) miClient.getSession().readField(SModConsts.HRSS_TP_BEN, new int[] { SModSysConsts.HRSS_TP_BEN_ANN_BON }, SDbRegistry.FIELD_NAME)).toLowerCase();
        String vacation = ((String) miClient.getSession().readField(SModConsts.HRSS_TP_BEN, new int[] { SModSysConsts.HRSS_TP_BEN_VAC }, SDbRegistry.FIELD_NAME)).toLowerCase();
        String vacationBonus = ((String) miClient.getSession().readField(SModConsts.HRSS_TP_BEN, new int[] { SModSysConsts.HRSS_TP_BEN_VAC_BON }, SDbRegistry.FIELD_NAME)).toLowerCase();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, "Nombre empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, SDbConsts.FIELD_CODE, "Clave empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "e.b_act", "Activo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "e.b_uni", "Sindicalizado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tpay.name", "Período pago", 75));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "dep.name", "Departamento", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "e.dt_ben", "Inicio prestaciones"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "e.dt_dis_n", "Última baja"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_cur_date", "Corte"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "_anniv_cur", "Aniv. actual"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "_anniv_turn", "Aniv. actual cumplido"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "_years", "Años antigüedad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "_days_x", "Días antigüedad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "_anniv", "Aniv. actual"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_8D, "_prop_x", "Aniv. actual proporcional", 100));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "bab.name", "Tabla " + annualBonus));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "eb.sta_ann_bon", "Aniv. inicial " + annualBonus));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "uab.usr", SGridConsts.COL_TITLE_USER_USR_NAME + " " + annualBonus));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "eb.ts_usr_upd_ann_bon", SGridConsts.COL_TITLE_USER_USR_NAME + " " + annualBonus));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "bv.name", "Tabla " + vacation));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "eb.sta_vac", "Aniv. inicial " + vacation));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "uv.usr", SGridConsts.COL_TITLE_USER_USR_NAME + " " + vacation));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "eb.ts_usr_upd_vac", SGridConsts.COL_TITLE_USER_USR_NAME + " " + vacation));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "bvb.name", "Tabla " + vacationBonus));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "eb.sta_vac_bon", "Aniv. inicial " + vacationBonus));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "uvb.usr", SGridConsts.COL_TITLE_USER_USR_NAME + " " + vacationBonus));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "eb.ts_usr_upd_vac_bon", SGridConsts.COL_TITLE_USER_USR_NAME + " " + vacationBonus));
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        
        return gridColumnsViews;
    }
    
    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.HRSU_EMP);
        moSuscriptionsSet.add(SModConsts.HRS_BEN);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
    
    private void actionPerformedShowMassiveAssignation() {
        if (jbShowMassiveAssignation.isEnabled()) {
            SDialogBenefitTablesMassiveAssignation dialog = new SDialogBenefitTablesMassiveAssignation((SGuiClient) miClient);
            dialog.initForm();
            dialog.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbShowMassiveAssignation) {
                actionPerformedShowMassiveAssignation();
            }
        }
    }
}

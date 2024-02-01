/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.mod.SModConsts;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
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
 * @author Isabel Servin
 */
public class SViewReportBudgetSummary extends SGridPaneView {
    
    private SGridFilterYear moFilterYear;

    public SViewReportBudgetSummary(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_MAT_BUDGET_SUM, SLibConsts.UNDEFINED, title);
        initComponents();
    }
    
    private void initComponents() {
        setRowButtonsEnabled(false);
        
        moFilterYear = new SGridFilterYear(miClient, this);
        moFilterYear.initFilter(new int[] { SLibTimeUtils.digestYear(miClient.getSession().getCurrentDate())[0] });
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterYear);
    }

    @Override
    public void prepareSqlQuery() {
        Object filter;
        String where = "";
        
        moPaneSettings = new SGridPaneSettings(1);

        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);
        
        filter = (int[]) moFiltersMap.get(SGridConsts.FILTER_YEAR).getValue();
        int year = ((int[]) filter)[0];
        
        where += "b.id_year = " + year + " AND r.id_year = " + year + " ";
        
        msSql = "SELECT c.id_mat_cons_ent AS " + SDbConsts.FIELD_ID + "1, " 
                + "c.name AS " + SDbConsts.FIELD_NAME + ", " 
                + "b.id_year, "
                + "b.id_period AS " + SDbConsts.FIELD_CODE + ", " 
                + "b.id_period, "
                + "b.dt_start, "
                + "b.dt_end, "
                + "b.budget, "
                + "SUM(re.debit), "
                + "SUM(re.credit), "
                + "SUM(re.debit - re.credit) AS gasto " 
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_BUDGET) + " AS b " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT) + " AS c ON "
                + "b.id_mat_cons_ent = c.id_mat_cons_ent " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT) + " AS cs ON "
                + "b.id_mat_cons_ent = cs.id_mat_cons_ent " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT_CC) + " AS csc ON "
                + "cs.id_mat_cons_ent = csc.id_mat_cons_ent AND cs.id_mat_cons_subent = csc.id_mat_cons_subent " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS re ON "
                + "csc.id_cc = re.fk_cc_n " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON "
                + "re.fid_acc = a.id_acc " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r ON "
                + "re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num " 
                + (where.isEmpty() ? "" : "WHERE " + where) + " "
                + "AND r.dt BETWEEN b.dt_start AND b.dt_end AND NOT r.b_del AND NOT re.b_del AND a.fid_tp_acc_r = 3 " 
                + "GROUP BY c.id_mat_cons_ent, c.name, b.id_year, b.id_period, b.dt_start, b.dt_end, b.budget " 
                + "ORDER BY c.id_mat_cons_ent, c.name, b.id_year, b.id_period, b.dt_start, b.dt_end, b.budget ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();
        
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, "Centro de consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, "id_year", "Año"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_MONTH, "id_period", "Periodo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_start", "Fecha inicial"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_end", "Fecha final"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "budget", "Presupuesto $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "gasto", "Gasto $"));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_ENT_BUDGET);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_ENT);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_SUBENT);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_SUBENT_CC);
        moSuscriptionsSet.add(SModConsts.FIN_REC_ETY);
        moSuscriptionsSet.add(SModConsts.FIN_ACC);
        moSuscriptionsSet.add(SModConsts.FIN_REC);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.mod.SModConsts;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
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
 * @author Isabel Servín
 */
public class SViewMaterialConsumptionEntityBudget extends SGridPaneView implements ActionListener {

    private SGridFilterYear moFilterYear;
    
    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     */
    public SViewMaterialConsumptionEntityBudget(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRN_MAT_CONS_ENT_BUDGET, SLibConsts.UNDEFINED, title, null);
        setRowButtonsEnabled(true, true, true, false, false);
        initComponents();
    }
    
    private void initComponents() {
        moFilterYear = new SGridFilterYear(miClient, this);
        moFilterYear.initFilter(new int[] { SLibTimeUtils.digestYear(miClient.getSession().getCurrentDate())[0] });
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterYear);
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(3);

        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        
        filter = (int[]) moFiltersMap.get(SGridConsts.FILTER_YEAR).getValue();
        int year = ((int[]) filter)[0];
        
        where += "v.id_year = " + year + " ";

        msSql = "SELECT v.id_mat_cons_ent AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_year AS " + SDbConsts.FIELD_ID + "2, "
                + "v.id_period AS " + SDbConsts.FIELD_ID + "3, "
                + "v.id_year AS " + SDbConsts.FIELD_CODE + ", "
                + "v.id_period AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt_start, "
                + "v.dt_end, "
                + "v.budget, "
                + "e.code, "
                + "e.name, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_BUDGET) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT) + " AS e ON "
                + "v.id_mat_cons_ent = e.id_mat_cons_ent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "ORDER BY v.id_mat_cons_ent, e.name "; 
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "e.code", "Código"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "name", "Centro de consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, SDbConsts.FIELD_CODE, "Año"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_MONTH, SDbConsts.FIELD_NAME, "Período"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_start", "Fecha inicial"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_end", "Fecha final"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "budget", "Presupuesto $"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_ENT);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            
        }
    }
}

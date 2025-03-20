/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mod.SModConsts;
import erp.mod.hrs.form.SFormCopyHolidays;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterYear;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Juan Barajas, Claudio Peña, Sergio Flores
 */
public class SViewHoliday extends SGridPaneView implements ActionListener {
    private JButton jbCopyHolidays;
    private SFormCopyHolidays moFormCopyHolidays;
    private SGridFilterYear moFilterYear;
    
    public SViewHoliday(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_HOL, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(true, true, true, false, true);
        initComponetsCustom();
    }

    private void initComponetsCustom() {
        moFilterYear = new SGridFilterYear(miClient, this);
        moFilterYear.initFilter(new int[] { SLibTimeUtils.digestYear(miClient.getSession().getCurrentDate())[0] });
        
        jbCopyHolidays = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_dps_link_rev.gif")), "Copiar días de años anteriores", this);

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCopyHolidays);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterYear);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }
    
        filter = (int[]) moFiltersMap.get(SGridConsts.FILTER_YEAR).getValue();
        sql += (sql.isEmpty() ? "" : " AND ") + ("v.id_hdy = " + ((int[]) filter)[0]) + " ";

        msSql = "SELECT "
                + "v.id_hdy AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_hol AS " + SDbConsts.FIELD_ID + "2, "
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "v.id_hdy, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_HOL) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.id_hdy, v.dt, v.name, v.id_hol ";
    }

    private void actionCopyHolidays() {
       if (jbCopyHolidays.isEnabled()) {
            try {
                moFormCopyHolidays = new SFormCopyHolidays(miClient, "Copia de días festivos y feriados");
                moFormCopyHolidays.setVisible(true);
                prepareSqlQuery();
                readGridData();
                
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }    
    }
    
    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, "v.id_hdy", "Año"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Día"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbCopyHolidays) {
                actionCopyHolidays();
            }
        }
    }
}

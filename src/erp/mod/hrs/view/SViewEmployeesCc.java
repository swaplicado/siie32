/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mod.SModConsts;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Edwin Carmona
 */
public class SViewEmployeesCc extends SGridPaneView {
    
    public SViewEmployeesCc(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRSX_EMP_CC, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }

    /*
     * Private methods
     */
    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
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
        String where = "";
        String cutoff = "";
        Object filter = null;
        Date dtYear = new Date();
        
        moPaneSettings = new SGridPaneSettings(1);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "NOT e.b_del ";
        }
        
        cutoff = "'" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getEndOfYear(dtYear)) + "'";
       
        msSql = "SELECT  " +
                "    e.id_emp AS f_id_1, " +
                "    e.num AS f_code, " +
                "    bp.bp AS f_name, " +
                "    bp.fiscal_id, " +
                "    bp.bp, " +
                "    e.num, " +
                "    dep.name, " +
                "    cc.id_cc, " +
                "    cc.cc, " +
                "    e.b_del AS " + SDbConsts.FIELD_IS_DEL + " " +
            "FROM " +
            "    " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp " +
            "        INNER JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON e.id_emp = bp.id_bp " +
            "        INNER JOIN " +
            "    " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " AS em ON em.id_emp = bp.id_bp " +
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
            "ORDER BY bp.bp , bp.id_bp;";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "fiscal_id", "RFC empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "bp.bp", "Nombre empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "e.num", "Número empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "dep.name", "Departamento"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "id_cc", "No. centro de costo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "cc", "Centro de costo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
    }
}

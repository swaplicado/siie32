/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterYearMonth;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Juan Barajas
 */
public class SViewOrderLimitMonth extends SGridPaneView {

    private SGridFilterYearMonth moFilterYearMonth;
    
    /**
     * View amount accumulated by user or by functional area.
     * @param client
     * @param title
     * @param gridSubtype mode view user (SModConsts.USRU_USR) or functional area (SModConsts.CFGU_FUNC)
     * @param params if is purchases or sales mnGridMode = SModSysConsts.TRNS_CT_DPS_PUR or SModSysConsts.TRNS_CT_DPS_SAL
     */
    public SViewOrderLimitMonth(SGuiClient client, String title, int gridSubtype, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_ORD_LIM_MAX, gridSubtype, title, params);
        initComponentsCustom();
        setRowButtonsEnabled(false);
    }

    /*
     * Private methods
    */

    private void initComponentsCustom() {
        moFilterYearMonth = new SGridFilterYearMonth(miClient, this);
        moFilterYearMonth.initFilter(SLibTimeUtils.digestMonth(miClient.getSession().getCurrentDate()));
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterYearMonth);
    }
    
    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Date dateAux = null;
        Date dateStart = null;
        Date dateEnd = null;
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }
        
        filter = (int[]) moFiltersMap.get(SGridConsts.FILTER_YEAR_MONTH).getValue();
        if (filter != null) {
            dateAux = SLibTimeUtils.createDate(((int[]) filter)[0], ((int[]) filter)[1]);
            dateStart = SLibTimeUtils.getBeginOfMonth(dateAux);
            dateEnd = SLibTimeUtils.getEndOfMonth(dateAux);
        }
        
        msSql = "SELECT "
                + (mnGridSubtype == SModConsts.USRU_USR ? "v.id_usr" : "v.id_func") + " AS " + SDbConsts.FIELD_ID + "1, "
                + (mnGridSubtype == SModConsts.USRU_USR ? "d.fid_usr_new" : "d.fid_func") + " AS _ref, "
                + "' ' AS " + SDbConsts.FIELD_CODE + ", "
                + (mnGridSubtype == SModConsts.USRU_USR ? "v.usr" : "v.name") + " AS " + SDbConsts.FIELD_NAME + ", "
                + "COALESCE(SUM(de.stot_r), 0.0) AS _amt_accum, "
                + "COALESCE(" + (mnGridSubtype == SModConsts.USRU_USR ? (mnGridMode == SModSysConsts.TRNS_CT_DPS_PUR ? "tu.pur_ord_lim_mon_n " :"tu.sal_ord_lim_mon_n ") : "v.exp_mon ") + ", 0.0) AS _amt_mon_lim ";
        
        if (mnGridSubtype == SModConsts.USRU_USR) {
            msSql += "FROM " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS v "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_USR_CFG) + " AS tu ON tu.id_usr = v.id_usr ";
        }
        else {
            msSql += "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS v ";
        }
        
        msSql += "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON " +
                (mnGridSubtype == SModConsts.USRU_USR ? " v.id_usr = d.fid_usr_new " : " v.id_func = d.fid_func ") 
                + "AND NOT d.b_del AND d.dt >= '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND " +
                "d.dt <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
                (mnGridMode == SModSysConsts.TRNS_CT_DPS_PUR ?
                        "AND d.fid_ct_dps = " + SModSysConsts.TRNS_CL_DPS_PUR_ORD[0] + " AND d.fid_cl_dps = " + SModSysConsts.TRNS_CL_DPS_PUR_ORD[1] + " " :
                        "AND d.fid_ct_dps = " + SModSysConsts.TRNS_CL_DPS_SAL_ORD[0] + " AND d.fid_cl_dps = " + SModSysConsts.TRNS_CL_DPS_SAL_ORD[1] + " ") + " "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc AND NOT de.b_del "
                + "GROUP BY _ref, " + SDbConsts.FIELD_NAME + " "
                + "ORDER BY " + SDbConsts.FIELD_NAME + ", _ref ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, (mnGridSubtype == SModConsts.USRU_USR ? "Usuario" : "Área funcional") ));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_amt_mon_lim", "Límite mensual $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_amt_accum", "Monto acumulado $"));
        
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridSubtype);
        moSuscriptionsSet.add(SModConsts.CFGU_FUNC);
        moSuscriptionsSet.add(SModConsts.TRN_USR_CFG);
        moSuscriptionsSet.add(SModConsts.TRN_DPS);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

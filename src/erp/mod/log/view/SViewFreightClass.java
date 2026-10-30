/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.view;

import erp.mod.SModConsts;
import erp.mod.log.db.SDbFreightClass;
import java.util.ArrayList;
import java.util.Arrays;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Sergio Flores
 */
public class SViewFreightClass extends SGridPaneView {

    public SViewFreightClass(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.LOG_FREIGHT_CLASS, 0, title);
        setRowButtonsEnabled(true, false, true, false, true);
        
        initComponetsCustom();
    }

    private void initComponetsCustom() {
        
    }

    /*
     * Public methods:
     */

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDateApplying(false);

        jbRowDisable.setEnabled(false);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "fc.b_del = 0 ";
        }

        msSql = "SELECT "
                + "fc.id_freight_class AS " + SDbConsts.FIELD_ID + "1, "
                + "fc.code AS " + SDbConsts.FIELD_CODE + ", "
                + "fc.name AS " + SDbConsts.FIELD_NAME + ", "
                + "CASE "
                + "WHEN fc.class_tp = '" + SDbFreightClass.TYPE_HIGH + "' THEN '" + SDbFreightClass.Types.get(SDbFreightClass.TYPE_HIGH) + "' "
                + "WHEN fc.class_tp = '" + SDbFreightClass.TYPE_MIDDLE + "' THEN '" + SDbFreightClass.Types.get(SDbFreightClass.TYPE_MIDDLE) + "' "
                + "WHEN fc.class_tp = '" + SDbFreightClass.TYPE_LOW + "' THEN '" + SDbFreightClass.Types.get(SDbFreightClass.TYPE_LOW) + "' "
                + "ELSE '?' END AS _class_tp, "
                + "fc.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " 
                + "fc.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", " 
                + "fc.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", " 
                + "fc.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", " 
                + "fc.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", " 
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_FREIGHT_CLASS) + " AS fc " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON fc.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON fc.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY fc.name, fc.code, fc.id_freight_class ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[8];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_CODE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_class_tp", SGridConsts.COL_TITLE_TYPE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);
        gridColumnsViews.addAll(Arrays.asList((SGridColumnView[]) columns));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

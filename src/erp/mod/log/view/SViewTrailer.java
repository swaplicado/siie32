/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.view;

import erp.lib.SLibConstants;
import erp.mod.SModConsts;
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
 * @author Isabel Servín
 */
public class SViewTrailer extends SGridPaneView {

    public SViewTrailer(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.LOG_TRAILER, SLibConstants.UNDEFINED, title);
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
            sql += (sql.isEmpty() ? "" : "AND ") + "t.b_del = 0 ";
        }

        msSql = "SELECT " 
            + "t.id_trailer AS " + SDbConsts.FIELD_ID + "1, " 
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "t.name AS " + SDbConsts.FIELD_NAME + ", "
            + "t.plate, "
            + "t.trailer_subtype, "
            + "t.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " 
            + "t.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", " 
            + "t.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", " 
            + "t.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", " 
            + "t.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", " 
            + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
            + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_TRAILER) + " AS t " 
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON t.fk_usr_ins = ui.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON t.fk_usr_upd = uu.id_usr "
            + (sql.isEmpty() ? "" : "WHERE " + sql)
            + "ORDER BY t.name ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[8];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, "Remolque");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "plate", "Placa");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "trailer_subtype", "Subtipo remolque");
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
        moSuscriptionsSet.add(SModConsts.LOG_TRAILER);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

}

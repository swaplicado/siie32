/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mod.SModConsts;
import java.util.ArrayList;
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
public class SViewPackCostCentersCostCenter extends SGridPaneView {

    public SViewPackCostCentersCostCenter(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_PACK_CC_CC, 0, title);
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "NOT pc.b_del ";
        }

        msSql = "SELECT "
                + "pcc.id_pack_cc AS " + SDbConsts.FIELD_ID + "1, "
                + "pcc.id_cc AS " + SDbConsts.FIELD_ID + "2, "
                + "pc.code AS " + SDbConsts.FIELD_CODE + ", "
                + "pc.name AS " + SDbConsts.FIELD_NAME + ", "
                + "cc.id_cc, "
                + "cc.cc, "
                + "pcc.prorat_per, "
                + "pc.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "pc.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "pc.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "pc.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "pc.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "pc.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PACK_CC_CC) + " AS pcc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PACK_CC) + " AS pc ON "
                + "pcc.id_pack_cc = pc.id_pack_cc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cc ON "
                + "pcc.id_cc = cc.pk_cc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "pc.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "pc.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY pc.name, pc.code, cc.id_cc, cc.cc, pcc.id_pack_cc, pcc.id_cc ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME + " paquete centros costo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE + " paquete centros costo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "cc.id_cc", "No. centro costo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "cc.cc", "Centro costo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_DISC, "pcc.prorat_per", "% prorrateo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.HRS_PACK_CC);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

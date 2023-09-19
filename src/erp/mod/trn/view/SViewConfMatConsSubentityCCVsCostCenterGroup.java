/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.mod.SModConsts;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Isabel Servín
 */
public class SViewConfMatConsSubentityCCVsCostCenterGroup extends SGridPaneView {

    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     */
    public SViewConfMatConsSubentityCCVsCostCenterGroup(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_CONF_SUBENT_VS_CC_GRP, SLibConsts.UNDEFINED, title, null);
        initComponents();
    }
    
    private void initComponents() {
        setRowButtonsEnabled(false, false, true, false, false);
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(3);

        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(false);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "v.b_del = 0 AND c.b_del = 0 AND cc.b_del = 0 ";
        }

        msSql = "SELECT DISTINCT v.id_mat_cons_ent AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_mat_cons_subent AS " + SDbConsts.FIELD_ID + "2, "
                + "cc.pk_cc AS " + SDbConsts.FIELD_ID + "3, "
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "c.name AS entidad, "
                + "cc.id_cc, "
                + "cc.cc, "
                + "IF(g.id_mat_cc_grp IS NULL, FALSE, TRUE) AS conf, "
                + "g.fk_usr AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "g.ts_usr AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT) + " AS c ON "
                + "v.id_mat_cons_ent = c.id_mat_cons_ent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT_CC) + " AS sc ON "
                + "v.id_mat_cons_ent = sc.id_mat_cons_ent AND v.id_mat_cons_subent = sc.id_mat_cons_subent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cc ON " 
                + "sc.id_cc = cc.pk_cc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT_CC_CC_GRP) + " AS g ON " 
                + "v.id_mat_cons_ent = g.id_mat_cons_ent AND v.id_mat_cons_subent = g.id_mat_cons_subent AND sc.id_cc = g.id_cc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "g.fk_usr = ui.id_usr "
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "ORDER BY c.name, v.name, v.id_mat_cons_subent "; 
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, "Código"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, "Subentidad de consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "entidad", "Entidad de consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "id_cc", "Código centro de costo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "cc", "Centro de costo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "conf", "Configurado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_ENT);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_SUBENT);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_SUBENT_CC);
        moSuscriptionsSet.add(SModConsts.FIN_CC);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_SUBENT_CC_CC_GRP);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.data.SDataConstantsSys;
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
public class SViewConfUserVsEntity extends SGridPaneView {

    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     */
    public SViewConfUserVsEntity(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_CONF_USR_VS_ENT, SLibConsts.UNDEFINED, title, null);
        initComponents();
    }
    
    private void initComponents() {
        setRowButtonsEnabled(false, false, true, false, false);
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(1);

        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }
        
        where += (where.isEmpty() ? "" : "AND ") + "(pu.id_prv = " + SDataConstantsSys.PRV_INV_REQ_MAT_REQ + " "
                + "OR pu.id_prv = " + SDataConstantsSys.PRV_INV_REQ_MAT_REV + ") ";

        msSql = "SELECT DISTINCT v.id_usr AS " + SDbConsts.FIELD_ID + "1, "
                + "v.usr AS " + SDbConsts.FIELD_CODE + ", "
                + "v.usr AS " + SDbConsts.FIELD_NAME + ", "
                + "IF(ceu.id_usr IS NULL, 0, 1) AS conf_cons, "
                + "IF(peu.id_usr IS NULL, 0, 1) AS conf_prov, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "ceu.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + "_cons, "
                + "peu.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + "_prov, "
                + "ceu.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + "_cons, "
                + "peu.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + "_prov, "
                + "v.ts_edit AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "uic.usr AS " + SDbConsts.FIELD_USER_INS_NAME + "_cons, "
                + "uip.usr AS " + SDbConsts.FIELD_USER_INS_NAME + "_prov "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS v "
                + "INNER JOIN erp.usru_prv_usr AS pu ON "
                + "v.id_usr = pu.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_USR) + " AS ceu ON "
                + "v.id_usr = ceu.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT_USR) + " AS peu ON "
                + "v.id_usr = peu.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uic ON "
                + "ceu.fk_usr_ins = uic.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uip ON "
                + "peu.fk_usr_ins = uip.id_usr "
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "ORDER BY v.usr, v.id_usr "; 
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_NAME, "Usuario"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "conf_prov", "Conf. suministro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME + "_prov", SGridConsts.COL_TITLE_USER_INS_NAME + " sum"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS + "_prov", SGridConsts.COL_TITLE_USER_INS_TS + " sum"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "conf_cons", "Conf. consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME + "_cons", SGridConsts.COL_TITLE_USER_INS_NAME + " cons"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS + "_cons", SGridConsts.COL_TITLE_USER_INS_TS + " cons"));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_PROV_ENT_USR);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_ENT_USR);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

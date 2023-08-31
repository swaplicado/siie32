/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
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
public class SViewConfEmployeeVsEntity extends SGridPaneView {

    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     */
    public SViewConfEmployeeVsEntity(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_CONF_EMP_VS_ENT, SLibConsts.UNDEFINED, title, null);
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

        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(false);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }
        
        where += (where.isEmpty() ? "" : "AND ") + "v.b_employee AND em.b_act AND NOT bp.b_del AND NOT em.b_del ";

        msSql = "SELECT DISTINCT v.id_maint_user AS " + SDbConsts.FIELD_ID + "1, "
                + "bp.bp AS " + SDbConsts.FIELD_CODE + ", "
                + "bp.bp AS " + SDbConsts.FIELD_NAME + ", "
                + "IF(ee.id_ref IS NULL, 0, 1) AS conf, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "ee.fk_usr AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "ee.ts_usr AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAINT_USER) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                + "v.id_maint_user = bp.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS em ON "
                + "v.id_maint_user = em.id_emp "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_USR) + " AS ee ON "
                + "ee.id_link = " + SModSysConsts.USRS_LINK_EMP + " AND v.id_maint_user = ee.id_ref "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "ee.fk_usr = ui.id_usr "
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "ORDER BY bp.bp, v.id_maint_user "; 
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, SDbConsts.FIELD_NAME, "Empleado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "conf", "Configurado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_MAINT_USER);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.HRSU_EMP);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_ENT_USR);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

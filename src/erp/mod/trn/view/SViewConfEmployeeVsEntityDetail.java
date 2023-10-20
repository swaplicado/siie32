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
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Isabel Servín
 */
public class SViewConfEmployeeVsEntityDetail extends SGridPaneView {
    
    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     */
    public SViewConfEmployeeVsEntityDetail(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_DET_EMP_VS_ENT, SLibConsts.UNDEFINED, title, null);
        initComponents();
    }
    
    private void initComponents() {
        setRowButtonsEnabled(false);
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "v.id_link = " + SModSysConsts.USRS_LINK_EMP + " ";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(2);

        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(false);
        
        msSql = "SELECT v.id_mat_cons_ent AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_ref AS " + SDbConsts.FIELD_ID + "2, "
                + "v.b_default, "
                + "j.name AS entidad, "
                + "b.bp AS " + SDbConsts.FIELD_CODE + ", "
                + "b.bp AS " + SDbConsts.FIELD_NAME + ", "
                + "v.fk_usr AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.ts_usr AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_USR) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT) + " AS j ON "
                + "v.id_mat_cons_ent = j.id_mat_cons_ent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON " 
                + "v.id_ref = b.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr = ui.id_usr "
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "ORDER BY b.bp, j.name "; 
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, SDbConsts.FIELD_NAME, "Empleado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "entidad", "Centro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "b_default", "Predeterminado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_ENT_USR);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_ENT);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

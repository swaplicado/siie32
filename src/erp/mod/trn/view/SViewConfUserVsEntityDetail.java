/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
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
public class SViewConfUserVsEntityDetail extends SGridPaneView {
    
    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     * @param subType
     */
    public SViewConfUserVsEntityDetail(SGuiClient client, int subType, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_DET_USR_VS_ENT, subType, title, null);
        initComponents();
    }
    
    private void initComponents() {
        setRowButtonsEnabled(false);
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(2);

        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(false);
        
        switch (mnGridSubtype) {
            case SModConsts.TRN_MAT_CONS_ENT_USR:
                msSql = "SELECT v.id_mat_cons_ent AS " + SDbConsts.FIELD_ID + "1, "
                    + "v.id_ref AS " + SDbConsts.FIELD_ID + "2, "
                    + "v.b_default, "
                    + "j.name AS entidad, "
                    + "u.usr AS " + SDbConsts.FIELD_CODE + ", "
                    + "u.usr AS " + SDbConsts.FIELD_NAME + ", "
                    + "v.fk_usr AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                    + "v.ts_usr AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                    + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + " "
                    + "FROM " + SModConsts.TablesMap.get(mnGridSubtype) + " AS v "
                    + "INNER JOIN erp.usru_usr AS u ON " 
                    + "v.id_link = " + SModSysConsts.USRS_LINK_USR + " AND v.id_ref = u.id_usr "
                    + "INNER JOIN trn_mat_cons_ent AS j ON " 
                    + "v.id_mat_cons_ent = j.id_mat_cons_ent "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                    + "v.fk_usr = ui.id_usr "
                    + (where.isEmpty() ? "" : "WHERE " + where)
                    + "ORDER BY u.usr, j.name "; 
                
                break;
            case SModConsts.TRN_MAT_PROV_ENT_USR:
                msSql = "SELECT v.id_mat_prov_ent AS " + SDbConsts.FIELD_ID + "1, "
                    + "v.id_usr AS " + SDbConsts.FIELD_ID + "2, "
                    + "v.b_default, "
                    + "j.name AS entidad, "
                    + "u.usr AS " + SDbConsts.FIELD_CODE + ", "
                    + "u.usr AS " + SDbConsts.FIELD_NAME + ", "
                    + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                    + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                    + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + " "
                    + "FROM " + SModConsts.TablesMap.get(mnGridSubtype) + " AS v "
                    + "INNER JOIN erp.usru_usr AS u ON " 
                    + "v.id_usr = u.id_usr "
                    + "INNER JOIN trn_mat_prov_ent AS j ON " 
                    + "v.id_mat_prov_ent = j.id_mat_prov_ent " 
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                    + "v.fk_usr_ins = ui.id_usr "
                    + (where.isEmpty() ? "" : "WHERE " + where)
                    + "ORDER BY u.usr, j.name "; 
                break;
        }
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_NAME, "Usuario"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Entidad", "Entidad"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "b_default", "Predeterminado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(mnGridSubtype);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

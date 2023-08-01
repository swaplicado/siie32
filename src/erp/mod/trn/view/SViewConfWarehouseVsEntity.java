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
public class SViewConfWarehouseVsEntity extends SGridPaneView {

    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     */
    public SViewConfWarehouseVsEntity(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_CONF_WHS_VS_ENT, SLibConsts.UNDEFINED, title, null);
        initComponents();
    }
    
    private void initComponents() {
        setRowButtonsEnabled(false, false, true, false, false);
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(2);

        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(false);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }
        
        where += (where.isEmpty() ? "" : "AND ") + "v.fid_ct_ent = " + SDataConstantsSys.CFGS_CT_ENT_WH + " "
                + "AND bpb.fid_bp = " + miClient.getSession().getConfigCompany().getCompanyId() + " " 
                + "AND v.b_act ";

        msSql = "SELECT DISTINCT v.id_cob AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_ent AS " + SDbConsts.FIELD_ID + "2, "
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.ent AS " + SDbConsts.FIELD_NAME + ", "
                + "te.tp_ent, "
                + "bpb.bpb, "
                + "IF(ew.id_cob IS NULL, 0, 1) AS conf, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "ew.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "ew.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGS_TP_ENT) + " AS te ON "
                + "v.fid_ct_ent = te.id_ct_ent AND v.fid_tp_ent = te.id_tp_ent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS bpb ON "
                + "v.id_cob = bpb.id_bpb "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT_WHS) + " AS ew ON "
                + "v.id_cob = ew.id_cob AND v.id_ent = ew.id_whs "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "ew.fk_usr_ins = ui.id_usr "
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "ORDER BY v.ent, v.id_cob, v.id_ent "; 
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bpb.bpb", "Sucursal"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, "Almacén"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "te.tp_ent", "Tipo de almacén"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "conf", "Configurado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.CFGU_COB_ENT);
        moSuscriptionsSet.add(SModConsts.CFGS_TP_ENT);
        moSuscriptionsSet.add(SModConsts.BPSU_BPB);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_PROV_ENT_WHS);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

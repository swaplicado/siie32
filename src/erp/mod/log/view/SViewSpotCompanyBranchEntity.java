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
 * @author Juan Barajas
 */
public class SViewSpotCompanyBranchEntity extends SGridPaneView {

    public SViewSpotCompanyBranchEntity(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.LOGU_SPOT_COB_ENT, SLibConstants.UNDEFINED, title);
        setRowButtonsEnabled(true, true, true, false, true);        
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;
        
        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        moPaneSettings.setDisableableApplying(false);

        jbRowDisable.setEnabled(false);
        
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT " 
                + "v.id_cob AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_ent AS " + SDbConsts.FIELD_ID + "2, "
                + "ent.ent AS " + SDbConsts.FIELD_NAME + ", "
                + "ent.code AS " + SDbConsts.FIELD_CODE + ", "
                + "cob.bpb AS f_cob_name, "
                + "cob.code AS f_cob_code, "
                + "vs.name AS f_spot_name, "
                + "vs.code AS f_spot_code, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT_COB_ENT) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BPB) + " AS cob ON v.id_cob = cob.id_bpb AND cob.fid_bp = " + miClient.getSession().getConfigCompany().getCompanyId() + " "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS ent ON v.id_cob = ent.id_cob AND v.id_ent = ent.id_ent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS vs ON v.fk_spot = vs.id_spot "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY ent.ent, ent.code, v.id_cob, v.id_ent ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();
        SGridColumnView[] columns = new SGridColumnView[11];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME + " entidad empresa");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE + " entidad empresa");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_cob_name", SGridConsts.COL_TITLE_NAME + " sucursal empresa");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_cob_code", SGridConsts.COL_TITLE_CODE + " sucursal empresa");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_spot_name", SGridConsts.COL_TITLE_NAME + " lugar");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_spot_code", SGridConsts.COL_TITLE_CODE + " lugar");
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
        moSuscriptionsSet.add(SModConsts.BPSU_BPB);
        moSuscriptionsSet.add(SModConsts.CFGU_COB_ENT);
        moSuscriptionsSet.add(SModConsts.LOGU_SPOT);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

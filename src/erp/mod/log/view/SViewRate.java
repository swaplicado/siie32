/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.view;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import java.util.ArrayList;
import java.util.Arrays;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Néstor Ávalos, Isabel Servín
 */
public class SViewRate extends SGridPaneView {

    public SViewRate(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.LOG_RATE, SLibConstants.UNDEFINED, title);
        setButtonsEnabled();      
    }
    
    private void setButtonsEnabled () {
        switch (miClient.getSession().getUser().getPrivilegeLevel(SDataConstantsSys.PRV_LOG_RATE)) {
            case SUtilConsts.LEV_READ:
                setRowButtonsEnabled(false);
                break;
            case SUtilConsts.LEV_CAPTURE:
            case SUtilConsts.LEV_AUTHOR:
                setRowButtonsEnabled(true, true, false, false, false);
                break;
            case SUtilConsts.LEV_EDITOR:
                setRowButtonsEnabled(true, true, true, false, false);
                break;
            case SUtilConsts.LEV_MANAGER:
                setRowButtonsEnabled(true, true, true, false, true);
                break;
            default:
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        moPaneSettings.setDisableableApplying(false);

        jbRowDisable.setEnabled(false);
        
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT "
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "'' AS " + SDbConsts.FIELD_NAME + ", "    
            + "v.id_rate AS " + SDbConsts.FIELD_ID + "1, "
            + "v.rate, "
            + "v.b_con, "
            + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
            + "tps.name AS f_tp_spot_src, "
            + "sps.name AS f_spot_src, "
            + "tpd.name AS f_tp_spot_des, "
            + "spd.name AS f_spot_des, "
            + "bp.bp AS f_bp, "
            + "tpv.name AS f_tp_veh, "
            + "cu.cur_key AS f_cur, "    
            + "v.fk_cur, "
            + "v.fk_src_tp_spot, "
            + "v.fk_src_spot, "
            + "v.fk_des_tp_spot, "
            + "v.fk_des_spot, "
            + "v.fk_car_n, "
            + "v.fk_tp_veh_n, "
            + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
            + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
            + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
            + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
            + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
            + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_RATE) + " AS v "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_SPOT) + " AS tps ON v.fk_src_tp_spot = tps.id_tp_spot "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS sps ON v.fk_src_spot = sps.id_spot "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGS_TP_SPOT) + " AS tpd ON v.fk_des_tp_spot = tpd.id_tp_spot "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_SPOT) + " AS spd ON v.fk_des_spot = spd.id_spot "    
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS cu ON v.fk_cur = cu.id_cur "            
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON v.fk_usr_ins = ui.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON v.fk_usr_upd = uu.id_usr "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON v.fk_car_n = bp.id_bp "        
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_TP_VEH) + " AS tpv ON v.fk_tp_veh_n = tpv.id_tp_veh "        
            + (sql.isEmpty() ? "" : "WHERE " + sql)
            + "ORDER BY tps.name, sps.name, tpd.name, spd.name, bp.bp ";        
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[14];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_tp_spot_src", "Tipo lugar origen");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_spot_src", "Lugar origen");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_tp_spot_des", "Tipo lugar destino");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_spot_des", "Lugar destino");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_bp", "Transportista");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_tp_veh", "Tipo vehículo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.rate", "Tarifa");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_cur", "Moneda");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_con", "Consolidado");                
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
        moSuscriptionsSet.add(SModConsts.LOGS_TP_SPOT);
        moSuscriptionsSet.add(SModConsts.LOGU_SPOT);
        moSuscriptionsSet.add(SModConsts.LOGS_TP_SPOT);
        moSuscriptionsSet.add(SModConsts.LOGU_SPOT);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.LOGU_TP_VEH);
        moSuscriptionsSet.add(SModConsts.CFGU_CUR);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

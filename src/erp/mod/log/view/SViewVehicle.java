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
public class SViewVehicle extends SGridPaneView {

    public SViewVehicle(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.LOG_VEH, SLibConstants.UNDEFINED, title);
        setButtonsEnabledByPrivilege();
    }
    
    private void setButtonsEnabledByPrivilege() {
        if (miClient.getSession().getUser().hasPrivilege(SDataConstantsSys.PRV_LOG_RATE)){
            setButtonsEnabled(miClient.getSession().getUser().getPrivilegeLevel(SDataConstantsSys.PRV_LOG_RATE));
        }
        else if (miClient.getSession().getUser().hasPrivilege(SDataConstantsSys.PRV_LOG_MISC)){
            setButtonsEnabled(miClient.getSession().getUser().getPrivilegeLevel(SDataConstantsSys.PRV_LOG_MISC));
        }
    }
    private void setButtonsEnabled (int level) {
        switch (level) {
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
        Object filter = null;
        
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
                + "v.id_veh AS " + SDbConsts.FIELD_ID + "1, "
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "v.plate, " 
                + "v.veh_year, "
                + "v.veh_conf, "
                + "v.gross_weight, "
                + "v.perm_sct_tp, "
                + "v.perm_sct_num, "
                + "v.insurance_policy, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "tp.code AS f_tp_code, "
                + "ins.name AS insurer, "
                + "v.fk_tp_veh, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_VEH) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOGU_TP_VEH) + " AS tp ON v.fk_tp_veh = tp.id_tp_veh "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON v.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_INSURER) + " AS ins ON v.fk_insurer_n = ins.id_insurer "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.name, v.code, v.id_veh ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[16];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_tp_code", SGridConsts.COL_TITLE_CODE + " tipo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.plate", "Placa");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_0D, "v.veh_year", "Año modelo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.gross_weight", "Peso bruto");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.veh_conf", "Configuración vehicular");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.perm_sct_tp", "Tipo de permiso SCT");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.perm_sct_num", "Número de permiso SCT");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.insurance_policy", "Póliza de seguro");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "insurer", "Aseguradora");
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
        moSuscriptionsSet.add(SModConsts.LOGU_TP_VEH);
        moSuscriptionsSet.add(SModConsts.LOG_INSURER);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

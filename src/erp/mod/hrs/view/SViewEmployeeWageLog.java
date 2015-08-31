/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mod.SModConsts;
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
 * @author Juan Barajas
 */
public class SViewEmployeeWageLog extends SGridPaneView {

    public SViewEmployeeWageLog(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_EMP_LOG_WAGE, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false, false, false, false, false);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT "
                + "v.id_emp AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_log AS " + SDbConsts.FIELD_ID + "2, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "v.sal, "
                + "v.wage, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "bp.bp, "
                + "pay.name, "
                + "sal.name, "
                + "emp.name, "
                + "wrk.name, "
                + "mwz.name, "
                + "dep.name, "
                + "pos.name, "
                + "sht.name, "
                + "rshe.name, "
                + "risk.name, "
                + "bank.name, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_WAGE) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS pay ON v.fk_tp_pay = pay.id_tp_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_SAL) + " AS sal ON v.fk_tp_sal = sal.id_tp_sal "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_EMP) + " AS emp ON v.fk_tp_emp = emp.id_tp_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_WRK) + " AS wrk ON v.fk_tp_wrk = wrk.id_tp_wrk "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_MWZ) + " AS mwz ON v.fk_tp_mwz = mwz.id_tp_mwz "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON v.fk_dep = dep.id_dep "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_POS) + " AS pos ON v.fk_pos = pos.id_pos "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_SHT) + " AS sht ON v.fk_sht = sht.id_sht "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + "  AS rshe ON v.fk_tp_rec_sche = rshe.id_tp_rec_sche "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_POS_RISK) + "  AS risk ON v.fk_tp_pos_risk = risk.id_tp_pos_risk "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                + "v.id_emp = bp.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN erp.hrss_bank AS bank ON v.fk_bank_n = bank.id_bank "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY bp.bp, v.id_emp, v.id_log ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();
            
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp.bp", "Empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.sal", "Salario $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.wage", "Sueldo $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "pay.name", "Periodo pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "sal.name", "Tipo salario"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "emp.name", "Tipo empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "wrk.name", "Tipo obrero"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "dep.name", "Departamento"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "pos.name", "Puesto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "sht.name", "Turno"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "rshe.name", "Régimen contratación"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "risk.name", "Riesgo trabajo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "mwz.name", "Área geográfica"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bank.name", "Banco"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.HRSU_TP_EMP);
        moSuscriptionsSet.add(SModConsts.HRSU_TP_WRK);
        moSuscriptionsSet.add(SModConsts.HRSU_TP_MWZ);
        moSuscriptionsSet.add(SModConsts.HRSU_DEP);
        moSuscriptionsSet.add(SModConsts.HRSU_POS);
        moSuscriptionsSet.add(SModConsts.HRSU_SHT);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

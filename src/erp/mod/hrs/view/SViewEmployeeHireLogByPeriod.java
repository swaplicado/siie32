/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.gui.grid.SGridFilterPanelEmployee;
import erp.mod.SModConsts;
import java.util.ArrayList;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Juan Barajas, Claudio Peña, Sergio Flores
 */
public class SViewEmployeeHireLogByPeriod extends SGridPaneView {
    
    public static final int GRID_SUBTYPE_HIRE = 1;
    public static final int GRID_SUBTYPE_DISMISS = 2;
    
    private boolean mbHasRightEmpWage;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SGridFilterPanelEmployee moFilterEmployee;
    
    public SViewEmployeeHireLogByPeriod(SGuiClient client, String title, int mode) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRSX_EMP_LOG_HIRE_BY_PER, mode, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        mbHasRightEmpWage = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_HRS_CAT_EMP_WAGE).HasRight;
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, miClient.getSession().getCurrentDate().getTime()));
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        
        moFilterEmployee = new SGridFilterPanelEmployee(miClient, this);
        moFilterEmployee.initFilter(0); // status filter not required, but type-of-payment filter remains available
        
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterEmployee);
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(2);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "NOT v.b_del ";
        }
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        
        switch (mnGridSubtype) {
            case GRID_SUBTYPE_HIRE:
                where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt_hire", (SGuiDate) filter);
                break;
            case GRID_SUBTYPE_DISMISS:
                where += (where.isEmpty() ? "" : "AND ") + "v.dt_dis_n IS NOT NULL AND " + SGridUtils.getSqlFilterDate("v.dt_dis_n", (SGuiDate) filter);
                break;
            default:
                // do nothing
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSS_TP_PAY)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            where += (where.isEmpty() ? "" : "AND ") + "emp.fk_tp_pay = " + ((int[]) filter)[0] + " ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSU_DEP)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            where += (where.isEmpty() ? "" : "AND ") + "emp.fk_dep = " + ((int[]) filter)[0] + " ";
        }
        
        /*
        SGridFilterPanelEmployee instances can handle filter status, but is not implemented by now:
        
        filter = ((SGridFilterValue) moFiltersMap.get(SGridFilterPanelEmployee.EMP_STATUS)).getValue();
        if (filter != null && ((int) filter) != SLibConsts.UNDEFINED) {
            if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ACT) {
                sql += (sql.isEmpty() ? "" : "AND ") + "(emp.b_act AND v.id_emp IN (SELECT mem.id_emp FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " AS mem WHERE NOT mem.b_del)) ";
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_INA) {
                sql += (sql.isEmpty() ? "" : "AND ") + "(NOT emp.b_act OR v.id_emp NOT IN (SELECT mem.id_emp FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " AS mem WHERE NOT mem.b_del)) ";
            }
            else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ALL) {
            }
        }
        */
        
        msSql = "SELECT "
                + "v.id_emp AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_log AS " + SDbConsts.FIELD_ID + "2, "
                + "emp.num AS " + SDbConsts.FIELD_CODE + ", "
                + "bp.bp AS " + SDbConsts.FIELD_NAME + ", "
                + "bp.fiscal_id, "
                + "bp.alt_id, "
                + "emp.ssn, "
                + "emp.dt_bir, "
                + "emp.dt_ben, "
                + "emp.dt_hire, "
                + "emp.dt_dis_n, "
                + "TIMESTAMPDIFF(YEAR, emp.dt_bir, NOW()) AS _age, "
                + "TIMESTAMPDIFF(YEAR, emp.dt_ben, NOW()) AS _seniority, "
                + "emp.b_uni, "
                + "emp.b_act, "
                + (mbHasRightEmpWage ? "emp.sal" : "NULL") + " AS _sal, "
                + (mbHasRightEmpWage ? "emp.wage" : "NULL") + " AS _wage, ";
        
        switch (mnGridSubtype) {
            case GRID_SUBTYPE_HIRE:
                msSql += "v.dt_hire AS " + SDbConsts.FIELD_DATE + ", "
                        + "v.nts_hire AS _notes, ";
                break;
            case GRID_SUBTYPE_DISMISS:
                msSql += "v.dt_dis_n AS " + SDbConsts.FIELD_DATE + ", "
                        + "v.nts_dis AS _notes, ";
                break;
            default:
                // do nothing
        }
        
        msSql += "tdis.name, "
                + "tpay.name, "
                + "tcon.name, "
                + "trec.name, "
                + "csex.name, "
                + "dep.name, "
                + "pos.name, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_HIRE) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_EMP_DIS) + " AS tdis ON "
                + "v.fk_tp_emp_dis = tdis.id_tp_emp_dis "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp ON "
                + "v.id_emp = emp.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                + "v.id_emp = bp.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS tpay ON "
                + "emp.fk_tp_pay = tpay.id_tp_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_CON) + " AS tcon ON "
                + "emp.fk_tp_con = tcon.id_tp_con "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS trec ON "
                + "emp.fk_tp_rec_sche = trec.id_tp_rec_sche "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_HRS_CAT) + " AS csex ON "
                + "emp.fk_cl_cat_sex = csex.id_cl_hrs_cat AND emp.fk_tp_cat_sex = csex.id_tp_hrs_cat "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON "
                + "emp.fk_dep = dep.id_dep "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_POS) + " AS pos ON "
                + "emp.fk_pos = pos.id_pos "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "ORDER BY " + SDbConsts.FIELD_DATE + ", bp.bp, v.id_emp, v.id_log ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        switch (mnGridSubtype) {
            case GRID_SUBTYPE_HIRE:
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha alta"));
                break;
            case GRID_SUBTYPE_DISMISS:
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha baja"));
                break;
            default:
                // do nothing
        }
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, SDbConsts.FIELD_NAME, "Nombre empleado", 250));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, SDbConsts.FIELD_CODE, "Número empleado", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tpay.name", "Tipo pago", 60));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "emp.b_act", "Activo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "emp.dt_hire", "Última alta"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "emp.dt_dis_n", "Última baja"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "emp.dt_ben", "Inicio prestaciones"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "_seniority", "Antigüedad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "bp.fiscal_id", "RFC", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "bp.alt_id", "CURP", 150));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "emp.ssn", "NSS", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "emp.dt_bir", "Nacimiento"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "_age", "Edad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "csex.name", "Sexo", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "dep.name", "Departamento", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "pos.name", "Puesto", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tcon.name", "Tipo contrato", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "trec.name", "Tipo régimen", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "emp.b_uni", "Sindicalizado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_sal", "Salario $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_wage", "Sueldo $"));
        
        switch (mnGridSubtype) {
            case GRID_SUBTYPE_HIRE:
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_notes", "Notas alta", 200));
                break;
            case GRID_SUBTYPE_DISMISS:
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "tdis.name", "Motivo baja"));
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_notes", "Notas baja", 200));
                break;
            default:
                // do nothing
        }
        
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
        moSuscriptionsSet.add(SModConsts.HRSU_TP_EMP_DIS);
        moSuscriptionsSet.add(SDataConstants.BPSX_BP_EMP);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

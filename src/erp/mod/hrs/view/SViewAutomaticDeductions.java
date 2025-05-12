/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.gui.grid.SGridFilterPanelEmployee;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SViewAutomaticDeductions extends SGridPaneView implements ActionListener {

    private boolean mbShowDetailForEmployees;
    private SGridFilterPanelEmployee moFilterEmployee;

    /**
     * 
     * @param client GUI client.
     * @param gridSubtype Options defined as constants in SModSysConsts: HRS_AUT_GBL & HRS_AUT_EMP.
     * @param title View's tab title.
     */
    public SViewAutomaticDeductions(SGuiClient client, int gridSubtype, String title) {
        this(client, gridSubtype, title, null);
    }

    /**
     * 
     * @param client GUI client.
     * @param gridSubtype Options defined as constants in SModSysConsts: HRS_AUT_GBL & HRS_AUT_EMP.
     * @param title View's tab title.
     * @param params GUI params for defining if view is in detail by providing a param type as SUtilConsts.QRY_DET when subtype is HRS_AUT_EMP.
     */
    public SViewAutomaticDeductions(SGuiClient client, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRSX_AUT_DED, gridSubtype, title, params);
        mbShowDetailForEmployees = params == null ? false : params.getType() == SUtilConsts.QRY_DET;
        initComponentsCustom();
    }

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        setRowButtonsEnabled(true, false, true, false, false);
        
        if (mnGridSubtype == SModSysConsts.HRS_AUT_EMP) {
            moFilterEmployee = new SGridFilterPanelEmployee(miClient, this);
            moFilterEmployee.initFilter(SGridFilterPanelEmployee.EMP_STATUS_ACT);
            
            getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterEmployee);
        }
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
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSS_TP_PAY)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "emp.fk_tp_pay = " + ((int[]) filter)[0] + " ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSU_DEP)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "emp.fk_dep = " + ((int[]) filter)[0] + " ";
        }
        
        if (mnGridSubtype == SModSysConsts.HRS_AUT_EMP) {
            filter = ((SGridFilterValue) moFiltersMap.get(SGridFilterPanelEmployee.EMP_STATUS)).getValue();
            if (filter != null && ((int) filter) != SLibConsts.UNDEFINED) {
                if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ACT) {
                    sql += (sql.isEmpty() ? "" : "AND ") + "emp.b_act = 1 ";
                }
                else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_INA) {
                    sql += (sql.isEmpty() ? "" : "AND ") + "emp.b_act = 0 ";
                }
                else if ((int)filter == SGridFilterPanelEmployee.EMP_STATUS_ALL) {
                }
            }
        }

        msSql = "SELECT "
                + (mnGridSubtype == SModSysConsts.HRS_AUT_GBL ? "v.id_aut AS " : "0 AS ") + SDbConsts.FIELD_ID + "1, "
                + (mnGridSubtype == SModSysConsts.HRS_AUT_GBL ? "0 AS " : "v.fk_emp_n AS ") + SDbConsts.FIELD_ID + "2, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt_sta AS " + SDbConsts.FIELD_DATE + ", "
                + "v.dt_end_n AS " + SDbConsts.FIELD_DATE + "1, "
                + "v.unt, "
                + "v.amt_unt, "
                + "v.amt_r, "
                + "vt.code, "
                + "vtc.code, "
                + "ded.code, "
                + "ded.name, "
                + "bp.bp, "
                + "emp.b_act, "
                + "tp.name, "
                + "l.num, "
                + "tl.code, "
                + "(SELECT name FROM " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY_SHT) + " WHERE id_tp_pay_sht = v.fk_tp_pay_sht) AS tp_pay_sht, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_DED) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " AS ded ON "
                + "v.id_ded = ded.id_ded "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_DED) + " AS vt ON "
                + "v.fk_tp_ded = vt.id_tp_ded "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_DED_COMP) + " AS vtc ON "
                + "ded.fk_tp_ded_comp = vtc.id_tp_ded_comp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                + "v.fk_emp_n = bp.id_bp "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp ON "
                + "v.fk_emp_n = emp.id_emp "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS tp ON "
                + "emp.fk_tp_pay = tp.id_tp_pay "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_LOAN) + " AS l ON "
                + "v.fk_loan_emp_n = l.id_emp AND v.fk_loan_loan_n = l.id_loan "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_LOAN) + " AS tl ON "
                + "v.fk_tp_loan_n = tl.id_tp_loan "
                + "WHERE "+ (mnGridSubtype == SModSysConsts.HRS_AUT_GBL ? "v.fk_emp_n IS NULL " : "v.fk_emp_n IS NOT NULL ")
                + (sql.isEmpty() ? "" : " AND " + sql)
                + (mnGridSubtype == SModSysConsts.HRS_AUT_GBL || mbShowDetailForEmployees ? "" : "GROUP BY v.fk_emp_n ")    // very weird and UGLY way of making query as summary!
                + (mnGridSubtype == SModSysConsts.HRS_AUT_GBL ? "ORDER BY ded.code, v.id_ded, v.id_aut " : "ORDER BY bp.bp, v.fk_emp_n" + (!mbShowDetailForEmployees ? "" : ", ded.code, v.id_ded, v.id_aut") + " ");
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        if (mnGridSubtype == SModSysConsts.HRS_AUT_GBL || mbShowDetailForEmployees) {
            if (mbShowDetailForEmployees) {
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp.bp", "Nombre empleado"));
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "emp.b_act", "Activo empleado"));
                gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tp.name", "Período pago"));
            }
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "ded.code", SGridConsts.COL_TITLE_CODE + " deducción"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "ded.name", SGridConsts.COL_TITLE_NAME + " deducción"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "v.unt", "Unidades"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "vtc.code", "Unidad"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT_UNIT, "v.amt_unt", "Valor unitario $"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "v.amt_r", "Valor $"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha inicial"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE + "1", "Fecha final"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "l.num", "Crédito/préstamo"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "tp_pay_sht", "Tipo nómina"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        }
        else {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp.bp", "Nombre empleado"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "emp.b_act", "Activo empleado"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tp.name", "Período pago"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        }

        return gridColumnsViews;
    }
    
    @Override
    public void actionRowNew() {
        if (moModel.getGridRows().isEmpty() || mnGridSubtype != SModSysConsts.HRS_AUT_GBL) {
            super.actionRowNew();
        }
        else {
            super.actionRowEdit();
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(mnGridSubtype);
        moSuscriptionsSet.add(SModConsts.HRS_DED);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.HRS_LOAN);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

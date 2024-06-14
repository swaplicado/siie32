/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.data.SDataConstants;
import erp.gui.grid.SGridFilterPanelEmployee;
import erp.mod.SModConsts;
import erp.mod.hrs.form.SDialogLayoutEmployee;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Juan Barajas, Claudio Peña, Sergio Flores
 */
public class SViewEmployeeHireLog extends SGridPaneView implements ActionListener {
    
    private SGridFilterPanelEmployee moFilterEmployee;
    private JButton jbLayoutEmployeeHire;
    private JButton jbLayoutEmployeeEntry;
    private JButton jbLayoutEmployeeDismiss;
    private JButton jbLayoutEmployeeAfi;
    
    private SDialogLayoutEmployee moDialogLayoutEmployee;

    public SViewEmployeeHireLog(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_EMP_LOG_HIRE, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        moFilterEmployee = new SGridFilterPanelEmployee(miClient, this);
        moFilterEmployee.initFilter(SGridFilterPanelEmployee.EMP_STATUS_ACT);
        
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterEmployee);
        
        jbLayoutEmployeeHire = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif")), "Layout alta empleados", this);
        jbLayoutEmployeeEntry = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif")), "Layout re-ingreso empleados", this);
        jbLayoutEmployeeDismiss = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif")), "Layout baja empleados", this);
        jbLayoutEmployeeAfi = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif")), "Layout movimientos afiliatorios empleados", this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbLayoutEmployeeHire);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbLayoutEmployeeEntry);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbLayoutEmployeeDismiss);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbLayoutEmployeeAfi);
    }
    
    private void actionLayoutEmployeeHire() {
        if (jbLayoutEmployeeHire.isEnabled()) {
            moDialogLayoutEmployee = new SDialogLayoutEmployee(miClient, "Layout empleados", SModConsts.HRSX_LAYOUT_SUA_HIRE);
            moDialogLayoutEmployee.resetForm();
            moDialogLayoutEmployee.setVisible(true);
        }
    }
    
    private void actionLayoutEmployeeEntry() {
        if (jbLayoutEmployeeEntry.isEnabled()) {
            moDialogLayoutEmployee = new SDialogLayoutEmployee(miClient, "Layout empleados", SModConsts.HRSX_LAYOUT_SUA_ENTRY);
            moDialogLayoutEmployee.resetForm();
            moDialogLayoutEmployee.setVisible(true);
        }
    }
    
    private void actionLayoutEmployeeDismiss(){
        if (jbLayoutEmployeeDismiss.isEnabled()) {
            moDialogLayoutEmployee = new SDialogLayoutEmployee(miClient, "Layout empleados", SModConsts.HRSX_LAYOUT_SUA_DISMISS);
            moDialogLayoutEmployee.resetForm();
            moDialogLayoutEmployee.setVisible(true);  
        }
    }
    
    private void actionLayoutEmployeeAfil(){
        if (jbLayoutEmployeeAfi.isEnabled()) {
            moDialogLayoutEmployee = new SDialogLayoutEmployee(miClient, "Layout empleados", SModConsts.HRSX_LAYOUT_SUA_AFI);
            moDialogLayoutEmployee.resetForm();
            moDialogLayoutEmployee.setVisible(true);  
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
            sql += (sql.isEmpty() ? "" : "AND ") + "NOT v.b_del ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSS_TP_PAY)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "emp.fk_tp_pay = " + ((int[]) filter)[0] + " ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSU_DEP)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "emp.fk_dep = " + ((int[]) filter)[0] + " ";
        }
        
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
        
        msSql = "SELECT "
                + "v.id_emp AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_log AS " + SDbConsts.FIELD_ID + "2, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt_hire AS " + SDbConsts.FIELD_DATE + ", "
                + "v.nts_hire, "
                + "v.dt_dis_n, "
                + "v.nts_dis, "
                + "v.b_hire, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "vt.name, "
                + "vtrs.name, "
                + "bp.bp, "
                + "emp.num, "
                + "emp.b_act, "
                + "(SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " AS mem WHERE mem.id_emp = v.id_emp AND NOT mem.b_del) AS _member, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_HIRE) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_EMP_DIS) + " AS vt ON "
                + "v.fk_tp_emp_dis = vt.id_tp_emp_dis "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS vtrs ON "
                + "v.fk_tp_rec_sche = vtrs.id_tp_rec_sche "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp ON "
                + "v.id_emp = emp.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                + "v.id_emp = bp.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY bp.bp, v.id_emp, v.id_log ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "bp.bp", "Empleado", 250));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "emp.num", "Clave", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "emp.b_act", "Activo", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "_member", "Membresía", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha alta"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.nts_hire", "Notas alta", 200));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_dis_n", "Fecha baja"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.nts_dis", "Notas baja", 200));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "vt.name", "Motivo baja"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vtrs.name", "Tipo régimen"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_hire", "Alta"));
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbLayoutEmployeeHire) {
                actionLayoutEmployeeHire();
            }
            else if (button == jbLayoutEmployeeEntry) {
                actionLayoutEmployeeEntry();
            }
            else if (button == jbLayoutEmployeeDismiss) {
                actionLayoutEmployeeDismiss();
            }
            else if (button == jbLayoutEmployeeAfi) {
                actionLayoutEmployeeAfil();
            }
        }
    }
}

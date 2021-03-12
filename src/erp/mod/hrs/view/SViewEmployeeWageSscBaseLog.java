/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.gui.grid.SGridFilterPanelEmployee;
import erp.mod.SModConsts;
import erp.mod.hrs.form.SDialogLayoutEmployee;
import erp.mod.hrs.form.SDialogMassiveUpdateSscPeriod;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
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
 * @author Juan Barajas, Claudio Peña
 */
public class SViewEmployeeWageSscBaseLog extends SGridPaneView implements ActionListener{

    private SGridFilterPanelEmployee moFilterEmployee;
    private JButton jbLayoutEmployeeSBC;
    private JButton jbEmployeesSscPeriod;
    private SDialogLayoutEmployee moDialogLayoutEmployee;
    private SDialogMassiveUpdateSscPeriod moDialogMassiveUpdateSscPeriod;

    public SViewEmployeeWageSscBaseLog(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_EMP_LOG_SAL_SSC, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        moFilterEmployee = new SGridFilterPanelEmployee(miClient, this, SModConsts.HRSS_TP_PAY, SModConsts.HRSU_DEP);
        moFilterEmployee.initFilter(null);
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterEmployee);
        
        jbLayoutEmployeeSBC = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif")), "Layout modificación SBC de empleados", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbLayoutEmployeeSBC);
        
        jbEmployeesSscPeriod = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_wizard.gif")), "Actualización de SBC de empleados", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbEmployeesSscPeriod);

    }

    private void actionLayoutEmployeeSBC() {
        if (jbLayoutEmployeeSBC.isEnabled()) {
            try {
                moDialogLayoutEmployee = new SDialogLayoutEmployee(miClient, "Layout empleados", SModConsts.HRSX_LAYOUT_SUA_SSC);
                moDialogLayoutEmployee.resetForm();
                moDialogLayoutEmployee.setVisible(true);
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
    }
 
    private void actionEmployeesSscPeriod() {
        if (jbEmployeesSscPeriod.isEnabled()) {
            try {
                if (moDialogMassiveUpdateSscPeriod == null) {
                    moDialogMassiveUpdateSscPeriod = new SDialogMassiveUpdateSscPeriod(miClient, "Actualización de SBC de empleados");
                }
                moDialogMassiveUpdateSscPeriod.resetForm();
                moDialogMassiveUpdateSscPeriod.setVisible(true);
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
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

        msSql = "SELECT "
                + "v.id_emp AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_log AS " + SDbConsts.FIELD_ID + "2, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "v.sal_ssc, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "bp.bp, "
                + "emp.num, "
                + "emp.b_act, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_SAL_SSC) + " AS v "
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

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp.bp", "Empleado", 250));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, "emp.num", "Clave", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "emp.b_act", "Activo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.sal_ssc", "SBC $"));
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
        moSuscriptionsSet.add(SModConsts.HRSU_EMP);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
       
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbLayoutEmployeeSBC) {
                actionLayoutEmployeeSBC();
            }            
            else if (button == jbEmployeesSscPeriod) {
                actionEmployeesSscPeriod();
            }
        }
    }
}
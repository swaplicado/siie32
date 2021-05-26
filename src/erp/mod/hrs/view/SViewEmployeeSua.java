/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.gui.grid.SGridFilterPanelLoan;
import erp.mod.SModConsts;
import erp.mod.hrs.form.SDialogLayoutEmployee;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
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
 * @author Claudio Peña
 */
public class SViewEmployeeSua extends SGridPaneView implements ActionListener{

    private SGridFilterDatePeriod moFilterDatePeriod;    
    private JButton jbLayoutEmployeeHire;
    private JButton jbLayoutEmployeeEntry;
    private JButton jbLayoutEmployeeDismiss;
    private JButton jbLayoutEmployeeAfi;
    private SDialogLayoutEmployee moDialogLayoutEmployee;
    private SGridFilterPanelLoan moFilterTypeIdse;


    public SViewEmployeeSua(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_EMP_LOG_SUA, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(false);

        moFilterTypeIdse = new SGridFilterPanelLoan(miClient, this, SModConsts.HRSU_EMP_SUA);
        moFilterTypeIdse.initFilter(null);
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterTypeIdse);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);

        jbLayoutEmployeeHire = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif")), "Layout alta empleados", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbLayoutEmployeeHire);
        jbLayoutEmployeeEntry = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif")), "Layout re-ingreso empleados", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbLayoutEmployeeEntry);
        jbLayoutEmployeeDismiss = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif")), "Layout baja empleados", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbLayoutEmployeeDismiss);
        jbLayoutEmployeeAfi = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif")), "Layout movimientos afiliatorios empleados", this);
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
        
        moPaneSettings = new SGridPaneSettings(1);
        
        String start = "";
        String end = "";
        SGuiDate guiDate = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();

        switch (guiDate.getGuiType()) {
            case SGuiConsts.GUI_DATE_DATE:
                start = "'" + SLibUtils.DbmsDateFormatDate.format(guiDate) + "' ";
                end = "'" + SLibUtils.DbmsDateFormatDate.format(guiDate) + "' ";
                break;
            case SGuiConsts.GUI_DATE_MONTH:
                start = "'" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getBeginOfMonth(guiDate)) + "' ";
                end = "'" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getEndOfMonth(guiDate)) + "' ";
                break;
            case SGuiConsts.GUI_DATE_YEAR:
                start = "'" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getBeginOfYear(guiDate)) + "' ";
                end = "'" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getEndOfYear(guiDate)) + "' ";
                break;
            default:
                // do nothing!
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSU_EMP_SUA)).getValue();
        if (filter == null) {
            sql += "SELECT bp.firstname AS Nombre, bp.alt_id AS CURP, bp.fiscal_id as RFC , emp.lastname1 AS ApellidoP, emp.lastname2 AS ApellidoM, emp.num AS ClaveTrab, " 
                    + "hire.id_emp AS " + SDbConsts.FIELD_ID + "1, hire.id_emp AS " + SDbConsts.FIELD_ID + "2, emp.num AS " + SDbConsts.FIELD_CODE  + "," 
                    + "CONCAT(bp.firstname, ' ',  emp.lastname1, ' ', emp.lastname2) AS " + SDbConsts.FIELD_NAME + ", " 
                    + "emp.ssn AS SSN, emp.sal_ssc AS Salario, e.id_tp_emp AS TpTrabajador, sal.id_tp_sal AS TpSalario, wrktp.id_tp_work_day AS Jornada, cfg.ss_subbra AS Guia, emp.umf as UMF, " 
                    + "hire.dt_hire AS DateApplication, par.reg_ss AS Param " 
                    + "FROM erp.HRSU_EMP AS emp " 
                    + "INNER JOIN erp.BPSU_BP AS bp ON bp.id_bp = emp.id_emp " 
                    + "INNER JOIN erp.HRSU_TP_EMP AS e ON e.id_tp_emp = emp.fk_tp_emp " 
                    + "INNER JOIN erp.hrss_tp_sal AS sal ON sal.id_tp_sal = emp.fk_tp_sal " 
                    + "INNER JOIN erp.hrss_tp_work_day AS wrktp ON emp.fk_tp_work_day = wrktp.id_tp_work_day " 
                    + "INNER JOIN hrs_emp_log_hire AS hire ON hire.id_emp = emp.id_emp " 
                    + "INNER JOIN cfg_param_co AS par " 
                    + "INNER JOIN hrs_cfg AS cfg " 
                    + "WHERE hire.b_hire = 1 AND not hire.b_del AND hire.dt_hire >= " + start + " AND hire.dt_hire <= " + end + " ; ";
        }
        else {
            if (((int[]) filter)[0] == 0) { // alta
                sql += "SELECT bp.firstname AS Nombre, bp.alt_id AS CURP, bp.fiscal_id as RFC , emp.lastname1 AS ApellidoP, emp.lastname2 AS ApellidoM, emp.num AS ClaveTrab, " 
                       + "hire.id_emp AS " + SDbConsts.FIELD_ID + "1, hire.id_emp AS " + SDbConsts.FIELD_ID + "2, emp.num AS " + SDbConsts.FIELD_CODE  + "," 
                       + "CONCAT(bp.firstname, ' ',  emp.lastname1, ' ', emp.lastname2) AS " + SDbConsts.FIELD_NAME + ", " 
                       + "emp.ssn AS SSN, emp.sal_ssc AS Salario, e.id_tp_emp AS TpTrabajador, sal.id_tp_sal AS TpSalario, wrktp.id_tp_work_day AS Jornada, cfg.ss_subbra AS Guia, emp.umf as UMF,  " 
                       + "hire.dt_hire AS DateApplication, par.reg_ss AS Param " 
                       + "FROM erp.HRSU_EMP AS emp " 
                       + "INNER JOIN erp.BPSU_BP AS bp ON bp.id_bp = emp.id_emp " 
                       + "INNER JOIN erp.HRSU_TP_EMP AS e ON e.id_tp_emp = emp.fk_tp_emp " 
                       + "INNER JOIN erp.hrss_tp_sal AS sal ON sal.id_tp_sal = emp.fk_tp_sal " 
                       + "INNER JOIN erp.hrss_tp_work_day AS wrktp ON emp.fk_tp_work_day = wrktp.id_tp_work_day " 
                       + "INNER JOIN hrs_emp_log_hire AS hire ON hire.id_emp = emp.id_emp " 
                       + "INNER JOIN cfg_param_co AS par " 
                       + "INNER JOIN hrs_cfg AS cfg " 
                       + "WHERE hire.b_hire = 1 AND not hire.b_del AND hire.dt_hire >= " + start + " AND hire.dt_hire <= " + end + " ; ";
            }
            if (((int[]) filter)[0] == 0) { // alta
                sql += "SELECT bp.firstname AS Nombre, bp.alt_id AS CURP, bp.fiscal_id as RFC , emp.lastname1 AS ApellidoP, emp.lastname2 AS ApellidoM, emp.num AS ClaveTrab, " 
                       + "hire.id_emp AS " + SDbConsts.FIELD_ID + "1, hire.id_emp AS " + SDbConsts.FIELD_ID + "2, emp.num AS " + SDbConsts.FIELD_CODE  + "," 
                       + "CONCAT(bp.firstname, ' ',  emp.lastname1, ' ', emp.lastname2) AS " + SDbConsts.FIELD_NAME + ", " 
                       + "emp.ssn AS SSN, emp.sal_ssc AS Salario, e.id_tp_emp AS TpTrabajador, sal.id_tp_sal AS TpSalario, wrktp.id_tp_work_day AS Jornada, cfg.ss_subbra AS Guia, emp.umf as UMF,  " 
                       + "hire.dt_hire AS DateApplication, par.reg_ss AS Param " 
                       + "FROM erp.HRSU_EMP AS emp " 
                       + "INNER JOIN erp.BPSU_BP AS bp ON bp.id_bp = emp.id_emp " 
                       + "INNER JOIN erp.HRSU_TP_EMP AS e ON e.id_tp_emp = emp.fk_tp_emp " 
                       + "INNER JOIN erp.hrss_tp_sal AS sal ON sal.id_tp_sal = emp.fk_tp_sal " 
                       + "INNER JOIN erp.hrss_tp_work_day AS wrktp ON emp.fk_tp_work_day = wrktp.id_tp_work_day " 
                       + "INNER JOIN hrs_emp_log_hire AS hire ON hire.id_emp = emp.id_emp " 
                       + "INNER JOIN cfg_param_co AS par " 
                       + "INNER JOIN hrs_cfg AS cfg " 
                       + "WHERE hire.b_hire = 1 AND not hire.b_del AND hire.dt_hire >= " + start + " AND hire.dt_hire <= " + end + " ; ";
            }
            else if (((int[]) filter)[0] == 1) { // alta
                sql += "SELECT bp.firstname AS Nombre, bp.alt_id AS CURP, bp.fiscal_id as RFC , emp.lastname1 AS ApellidoP, emp.lastname2 AS ApellidoM, emp.num AS ClaveTrab, " 
                       + "hire.id_emp AS " + SDbConsts.FIELD_ID + "1, hire.id_emp AS " + SDbConsts.FIELD_ID + "2, emp.num AS " + SDbConsts.FIELD_CODE  + "," 
                       + "CONCAT(bp.firstname, ' ',  emp.lastname1, ' ', emp.lastname2) AS " + SDbConsts.FIELD_NAME + ", " 
                       + "emp.ssn AS SSN, emp.sal_ssc AS Salario, e.id_tp_emp AS TpTrabajador, sal.id_tp_sal AS TpSalario, wrktp.id_tp_work_day AS Jornada, cfg.ss_subbra AS Guia, emp.umf as UMF, " 
                       + "hire.dt_hire AS DateApplication, par.reg_ss AS Param " 
                       + "FROM erp.HRSU_EMP AS emp " 
                       + "INNER JOIN erp.BPSU_BP AS bp ON bp.id_bp = emp.id_emp " 
                       + "INNER JOIN erp.HRSU_TP_EMP AS e ON e.id_tp_emp = emp.fk_tp_emp " 
                       + "INNER JOIN erp.hrss_tp_sal AS sal ON sal.id_tp_sal = emp.fk_tp_sal " 
                       + "INNER JOIN erp.hrss_tp_work_day AS wrktp ON emp.fk_tp_work_day = wrktp.id_tp_work_day " 
                       + "INNER JOIN hrs_emp_log_hire AS hire ON hire.id_emp = emp.id_emp " 
                       + "INNER JOIN cfg_param_co AS par " 
                       + "INNER JOIN hrs_cfg AS cfg " 
                       + "WHERE hire.b_hire = 1 AND not hire.b_del AND hire.dt_hire >= " + start + " AND hire.dt_hire <= " + end + " ; ";
            }
            else if (((int[]) filter)[0] == 2) { // reingreso
                sql += "SELECT bp.firstname AS Nombre, bp.alt_id AS CURP, bp.fiscal_id as RFC , emp.lastname1 AS ApellidoP, emp.lastname2 AS ApellidoM, emp.num AS ClaveTrab, "
                       + "hire.id_emp AS " + SDbConsts.FIELD_ID + "1, hire.id_emp AS " + SDbConsts.FIELD_ID + "2, emp.num AS " + SDbConsts.FIELD_CODE  + "," 
                       + "CONCAT(bp.firstname, ' ',  emp.lastname1, ' ', emp.lastname2) AS " + SDbConsts.FIELD_NAME + ", " 
                       + "emp.ssn AS SSN, emp.sal_ssc AS Salario, e.id_tp_emp AS TpTrabajador, sal.id_tp_sal AS TpSalario, wrktp.id_tp_work_day AS Jornada, cfg.ss_subbra AS Guia, emp.umf as UMF, "
                       + "hire.dt_hire AS DateApplication, par.reg_ss AS Param "
                       + "FROM erp.HRSU_EMP AS emp "
                       + "INNER JOIN erp.BPSU_BP AS bp ON bp.id_bp = emp.id_emp "
                       + "INNER JOIN erp.HRSU_TP_EMP AS e ON e.id_tp_emp = emp.fk_tp_emp "
                       + "INNER JOIN erp.hrss_tp_sal AS sal ON sal.id_tp_sal = emp.fk_tp_sal "
                       + "INNER JOIN erp.hrss_tp_work_day AS wrktp ON emp.fk_tp_work_day = wrktp.id_tp_work_day "
                       + "INNER JOIN hrs_emp_log_hire AS hire ON hire.id_emp = emp.id_emp "
                       + "INNER JOIN cfg_param_co AS par "
                       + "INNER JOIN hrs_cfg AS cfg "
                       + "WHERE hire.b_hire = 1 AND not hire.b_del AND hire.dt_hire >= " + start + " AND hire.dt_hire <= " + end + " and hire.id_log != 1; ";
            }
            else if (((int[]) filter)[0] == 3) { // baja
                sql += "SELECT bp.firstname AS Nombre, bp.alt_id AS CURP, bp.fiscal_id as RFC , emp.lastname1 AS ApellidoP, emp.lastname2 AS ApellidoM, emp.num AS ClaveTrab, "
                       + "emp.ssn AS SSN, emp.sal_ssc AS Salario, e.id_tp_emp AS TpTrabajador, sal.id_tp_sal AS TpSalario, wrktp.id_tp_work_day AS Jornada, cfg.ss_subbra AS Guia, emp.umf as UMF, "
                       + "hire.id_emp AS " + SDbConsts.FIELD_ID + "1, hire.id_emp AS " + SDbConsts.FIELD_ID + "2, emp.num AS " + SDbConsts.FIELD_CODE  + "," 
                       + "CONCAT(bp.firstname, ' ',  emp.lastname1, ' ', emp.lastname2) AS " + SDbConsts.FIELD_NAME + ", " 
                       + "hire.dt_hire AS DateApplication, par.reg_ss AS Param "
                       + "FROM erp.HRSU_EMP AS emp "
                       + "INNER JOIN erp.BPSU_BP AS bp ON bp.id_bp = emp.id_emp "
                       + "INNER JOIN erp.HRSU_TP_EMP AS e ON e.id_tp_emp = emp.fk_tp_emp "
                       + "INNER JOIN erp.hrss_tp_sal AS sal ON sal.id_tp_sal = emp.fk_tp_sal "
                       + "INNER JOIN erp.hrss_tp_work_day AS wrktp ON emp.fk_tp_work_day = wrktp.id_tp_work_day "
                       + "INNER JOIN hrs_emp_log_hire AS hire ON hire.id_emp = emp.id_emp "
                       + "INNER JOIN cfg_param_co AS par "
                       + "INNER JOIN hrs_cfg AS cfg "
                       + "WHERE hire.b_hire = 0 AND not hire.b_del AND hire.dt_dis_n >= " + start + " AND hire.dt_dis_n <= " + end + "; ";
            }
            else if (((int[]) filter)[0] == 4) { // afiliatorios
                sql += "SELECT bp.firstname AS Nombre, bp.alt_id AS CURP, bp.fiscal_id as RFC , emp.lastname1 AS ApellidoP, emp.lastname2 AS ApellidoM, emp.num AS ClaveTrab, "
                        + "emp.id_emp AS " + SDbConsts.FIELD_ID + "1, emp.id_emp AS " + SDbConsts.FIELD_ID + "2, emp.num AS " + SDbConsts.FIELD_CODE  + "," 
                        + "CONCAT(bp.firstname, ' ',  emp.lastname1, ' ', emp.lastname2) AS " + SDbConsts.FIELD_NAME + ", " 
                        + "emp.ssn AS SSN, emp.sal_ssc AS Salario, e.id_tp_emp AS TpTrabajador, emp.dt_bir AS Birth, cat.code AS Sex, emp.wrk_hrs_day AS HrsWork, sal.id_tp_sal AS TpSalario, emp.umf as UMF, "
                        + "wrktp.id_tp_work_day AS Jornada, cfg.ss_subbra AS Guia, hire.dt_hire AS DateApplication, par.reg_ss AS Param, emp.place_bir, emp.umf, a.zip_code, pos.name "
                        + "FROM erp.HRSU_EMP AS emp "
                        + "INNER JOIN erp.BPSU_BP AS bp ON bp.id_bp = emp.id_emp "
                        + "INNER JOIN erp.HRSU_TP_EMP AS e ON e.id_tp_emp = emp.fk_tp_emp "
                        + "INNER JOIN erp.hrss_tp_sal AS sal ON sal.id_tp_sal = emp.fk_tp_sal "
                        + "INNER JOIN erp.hrss_tp_work_day AS wrktp ON emp.fk_tp_work_day = wrktp.id_tp_work_day "
                        + "INNER JOIN erp.bpsu_bpb AS bpb ON bpb.fid_bp = bp.id_bp "
                        + "INNER JOIN erp.bpsu_bpb_add AS a ON a.id_bpb = bpb.fid_bp "
                        + "INNER JOIN erp.HRSU_POS AS pos ON pos.id_pos = emp.fk_pos "
                        + "INNER JOIN hrs_emp_log_hire AS hire ON hire.id_emp = emp.id_emp "
                        + "INNER JOIN cfg_param_co AS par "
                        + "INNER JOIN hrs_cfg AS cfg "
                        + "INNER JOIN erp.HRSS_TP_HRS_CAT AS cat on emp.fk_cl_cat_sex = cat.id_cl_hrs_cat AND emp.fk_tp_cat_sex = cat.id_tp_hrs_cat "
                        + "WHERE hire.b_hire = 0 AND not hire.b_del AND hire.dt_hire >= " + start + " AND hire.dt_hire <= " + end + " ";
            }
            else if (((int[]) filter)[0] == 5) { // incapacidad
                 sql += "SELECT emp.ssn AS SSN, v.num, bp.alt_id AS CURP, v.dt, v.dt_sta as DateApplication, v.dt_end, v.eff_day, par.reg_ss AS Param, tabs.id_tp_abs, emp.umf as UMF,  emp.sal_ssc AS Salario,  e.id_tp_emp AS TpTrabajador, sal.id_tp_sal AS TpSalario, wrktp.id_tp_work_day AS Jornada, 0 AS Guia,  "
                         
                        + "emp.id_emp AS " + SDbConsts.FIELD_ID + "1, emp.id_emp AS " + SDbConsts.FIELD_ID + "2, emp.num AS " + SDbConsts.FIELD_CODE  + "," 
                        + "CONCAT(bp.firstname, ' ',  emp.lastname1, ' ', emp.lastname2) AS " + SDbConsts.FIELD_NAME + ", " 
                         
                        + "(SELECT COALESCE(SUM(ac.eff_day), 0.0) "
                        + "FROM hrs_abs AS a "
                        + "INNER JOIN hrs_abs_cns AS ac ON ac.id_emp = a.id_emp AND ac.id_abs = a.id_abs "
                        + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = ac.fk_rcp_pay AND pr.id_emp = ac.fk_rcp_emp "
                        + "INNER JOIN hrs_pay AS p ON p.id_pay = pr.id_pay "
                        + "WHERE NOT a.b_del AND NOT ac.b_del AND NOT pr.b_del AND NOT p.b_del AND ac.id_emp = v.id_emp AND ac.id_abs = v.id_abs) AS f_app_days, v.ben_ann, v.ben_year "
                        + "FROM hrs_abs AS v INNER JOIN erp.bpsu_bp AS bp ON v.id_emp = bp.id_bp "
                        + "INNER JOIN erp.hrsu_emp AS emp ON v.id_emp = emp.id_emp "
                        + "INNER JOIN erp.HRSU_TP_EMP AS e ON e.id_tp_emp = emp.fk_tp_emp "
                        + "INNER JOIN erp.hrsu_cl_abs AS cabs ON v.fk_cl_abs = cabs.id_cl_abs "
                        + "INNER JOIN erp.hrsu_tp_abs AS tabs ON v.fk_cl_abs = tabs.id_cl_abs AND v.fk_tp_abs = tabs.id_tp_abs "
                        + "INNER JOIN erp.usru_usr AS uc ON v.fk_usr_clo = uc.id_usr "
                        + "INNER JOIN erp.usru_usr AS ui ON v.fk_usr_ins = ui.id_usr "
                        + "INNER JOIN erp.usru_usr AS uu ON v.fk_usr_upd = uu.id_usr "
                        + "INNER JOIN erp.hrss_tp_sal AS sal ON sal.id_tp_sal = emp.fk_tp_sal "
                        + "INNER JOIN erp.hrss_tp_work_day AS wrktp ON emp.fk_tp_work_day = wrktp.id_tp_work_day "
                        + "INNER JOIN cfg_param_co AS par WHERE v.b_del = 0 AND v.dt >= " + start + " AND v.dt <= " + end + " AND emp.b_act = 2 "
                        + "ORDER BY bp.bp , v.id_emp , v.dt , cabs.name , tabs.name , v.id_abs ";
            }
            else if (((int[]) filter)[0] == 6) { // ausentismo
                 sql += "SELECT emp.ssn AS SSN, v.num, bp.alt_id AS CURP, v.dt , v.dt_sta as DateApplication, v.dt_end, v.eff_day, par.reg_ss AS Param, tabs.id_tp_abs, emp.umf as UMF,  emp.sal_ssc AS Salario,  e.id_tp_emp AS TpTrabajador, sal.id_tp_sal AS TpSalario, wrktp.id_tp_work_day AS Jornada, 0 AS Guia, "
                        + "emp.id_emp AS " + SDbConsts.FIELD_ID + "1, emp.id_emp AS " + SDbConsts.FIELD_ID + "2, emp.num AS " + SDbConsts.FIELD_CODE  + ","
                        + "CONCAT(bp.firstname, ' ',  emp.lastname1, ' ', emp.lastname2) AS " + SDbConsts.FIELD_NAME + ", "
                        + "(SELECT COALESCE(SUM(ac.eff_day), 0.0) "
                        + "FROM hrs_abs AS a "
                        + "INNER JOIN hrs_abs_cns AS ac ON ac.id_emp = a.id_emp AND ac.id_abs = a.id_abs "
                        + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = ac.fk_rcp_pay AND pr.id_emp = ac.fk_rcp_emp "
                        + "INNER JOIN hrs_pay AS p ON p.id_pay = pr.id_pay "
                        + "WHERE NOT a.b_del AND NOT ac.b_del AND NOT pr.b_del AND NOT p.b_del AND ac.id_emp = v.id_emp AND ac.id_abs = v.id_abs) AS f_app_days, v.ben_ann, v.ben_year "
                        + "FROM hrs_abs AS v INNER JOIN erp.bpsu_bp AS bp ON v.id_emp = bp.id_bp "
                        + "INNER JOIN erp.hrsu_emp AS emp ON v.id_emp = emp.id_emp "
                        + "INNER JOIN erp.HRSU_TP_EMP AS e ON e.id_tp_emp = emp.fk_tp_emp "
                        + "INNER JOIN erp.hrsu_cl_abs AS cabs ON v.fk_cl_abs = cabs.id_cl_abs "
                        + "INNER JOIN erp.hrsu_tp_abs AS tabs ON v.fk_cl_abs = tabs.id_cl_abs AND v.fk_tp_abs = tabs.id_tp_abs "
                        + "INNER JOIN erp.usru_usr AS uc ON v.fk_usr_clo = uc.id_usr "
                        + "INNER JOIN erp.usru_usr AS ui ON v.fk_usr_ins = ui.id_usr "
                        + "INNER JOIN erp.usru_usr AS uu ON v.fk_usr_upd = uu.id_usr "
                        + "INNER JOIN erp.hrss_tp_sal AS sal ON sal.id_tp_sal = emp.fk_tp_sal "
                        + "INNER JOIN erp.hrss_tp_work_day AS wrktp ON emp.fk_tp_work_day = wrktp.id_tp_work_day "
                        + "INNER JOIN cfg_param_co AS par WHERE v.b_del = 0 AND v.dt >= " + start + " AND v.dt <= " + end + " AND emp.b_act = 1 "
                        + "ORDER BY bp.bp , v.id_emp , v.dt , cabs.name , tabs.name , v.id_abs ";
            }
        }
            
        msSql = " " +  sql ;
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "Param", "Registro patronal", 95));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_RAW, SDbConsts.FIELD_CODE , "Clave", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "SSN", "NNS", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S,  SDbConsts.FIELD_NAME, "Empleado", 250));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "Salario", "SBC",50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "TpTrabajador", "Tipo de trabajador", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "TpSalario", "Tipo de salario", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "Jornada", "Semana o jornada", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "DateApplication", "Fecha inicio labores", 75));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "Guia", "Guia", 150));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "CURP", "Curp", 150));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "UMF", "UMF", 150));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "UMF", "Tipo incidencia", 150));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "UMF", "Rama incapacidad", 150));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "UMF", "Tipo riesgo", 150));
        


        return gridColumnsViews;
    }
    
    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);

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
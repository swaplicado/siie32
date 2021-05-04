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
public class SViewEmployeeIdse extends SGridPaneView implements ActionListener{

    private SGridFilterDatePeriod moFilterDatePeriod;    
    private JButton jbLayoutEmployeeHireIdse;
    private JButton jbLayoutEmployeeSbcIdse;
    private JButton jbLayoutEmployeeDismissIdse;
    private SDialogLayoutEmployee moDialogLayoutEmployee;
    private SGridFilterPanelLoan moFilterTypeIdse;


    public SViewEmployeeIdse(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_EMP_LOG_IDSE, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(false);

        moFilterTypeIdse = new SGridFilterPanelLoan(miClient, this, SModConsts.HRSU_EMP_IDSE);
        moFilterTypeIdse.initFilter(null);
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterTypeIdse);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);

        jbLayoutEmployeeHireIdse = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif")), "Layout alta empleados IDSE", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbLayoutEmployeeHireIdse);
        jbLayoutEmployeeSbcIdse = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif")), "Layout modificación SBC de empleados IDSE", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbLayoutEmployeeSbcIdse);
        jbLayoutEmployeeDismissIdse = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_save.gif")), "Layout baja empleados IDSE", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbLayoutEmployeeDismissIdse);
      
    }
    
    private void actionLayoutEmployeeHireIdse() {
        if (jbLayoutEmployeeHireIdse.isEnabled()) {
            try {
                moDialogLayoutEmployee = new SDialogLayoutEmployee(miClient, "Layout empleados", SModConsts.HRSX_LAYOUT_IDSE_HIRE);
                moDialogLayoutEmployee.resetForm();
                moDialogLayoutEmployee.setVisible(true);
              }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
    }
    
    private void actionLayoutEmployeeSbcIdse() {
        if (jbLayoutEmployeeSbcIdse.isEnabled()) {
            try {
                moDialogLayoutEmployee = new SDialogLayoutEmployee(miClient, "Layout empleados", SModConsts.HRSX_LAYOUT_IDSE_SSC);
                moDialogLayoutEmployee.resetForm();
                moDialogLayoutEmployee.setVisible(true);
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
    }
    
    private void actionLayoutEmployeeDismissIdse(){
            if (jbLayoutEmployeeDismissIdse.isEnabled()) {
                try {
                    moDialogLayoutEmployee = new SDialogLayoutEmployee(miClient, "Layout empleados", SModConsts.HRSX_LAYOUT_IDSE_DISMISS);
                    moDialogLayoutEmployee.resetForm();
                    moDialogLayoutEmployee.setVisible(true);
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

        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSU_EMP_IDSE)).getValue();
        if (filter == null) {
            sql += " INNER JOIN hrs_emp_log_hire AS hire ON hire.id_emp = emp.id_emp " +
                    "INNER JOIN cfg_param_co AS par " +
                    "INNER JOIN hrs_cfg AS cfg " +
                    "WHERE NOT hire.b_del  AND hire.b_hire = 1 AND id_log = 1 " + 
                    "AND hire.dt_hire >= " + start + " AND hire.dt_hire <= " + end + " ORDER BY bp.id_bp; ";
        }
        else {
            if (((int[]) filter)[0] == 0) {
                sql += " INNER JOIN hrs_emp_log_hire AS hire ON hire.id_emp = emp.id_emp " +
                        "INNER JOIN cfg_param_co AS par " +
                        "INNER JOIN hrs_cfg AS cfg " +
                        "WHERE NOT hire.b_del  AND hire.b_hire = 1 AND id_log = 1 " + 
                        "AND hire.dt_hire >= " + start + " AND hire.dt_hire <= " + end + " ORDER BY bp.id_bp; ";
            }
            else if (((int[]) filter)[0] == 1) {
                sql += " INNER JOIN hrs_emp_log_hire AS hire ON hire.id_emp = emp.id_emp " +
                        "INNER JOIN cfg_param_co AS par " +
                        "INNER JOIN hrs_cfg AS cfg " +
                        "WHERE NOT hire.b_del  AND hire.b_hire = 1 AND id_log = 1 " + 
                        "AND hire.dt_hire >= " + start + " AND hire.dt_hire <= " + end + " ORDER BY bp.id_bp; ";
            }
            else if (((int[]) filter)[0] == 2) {
                 sql += " INNER JOIN hrs_emp_log_hire AS hire ON hire.id_emp = emp.id_emp " +
                        "INNER JOIN cfg_param_co AS par " +
                        "INNER JOIN hrs_cfg AS cfg " +
                        "WHERE NOT hire.b_del AND hire.b_hire = 0 " +
                        "AND hire.dt_dis_n >= " + start + " AND hire.dt_dis_n <= " + end + " ORDER BY bp.id_bp; ";
            }
            else if (((int[]) filter)[0] == 3) {
                 sql += " INNER JOIN HRS_EMP_LOG_HIRE AS hire ON hire.id_emp = emp.id_emp " +
                        "INNER JOIN HRS_EMP_LOG_SAL_SSC AS ssc ON ssc.id_emp = emp.id_emp " +
                        "INNER JOIN cfg_param_co AS par " +
                        "INNER JOIN hrs_cfg AS cfg " +
                        "WHERE ssc.dt >= " + start + " AND ssc.dt <= " + end + " AND NOT ssc.b_del GROUP BY bp.id_bp ORDER BY bp.id_bp; ";
            }
        }
       
        
        msSql = "SELECT hire.id_emp AS " + SDbConsts.FIELD_ID + "1, hire.id_emp AS " + SDbConsts.FIELD_ID + "2, emp.num AS " + SDbConsts.FIELD_CODE  + ", bp.firstname AS Nombre, bp.alt_id AS CURP, emp.lastname1 AS ApellidoP, emp.lastname2 AS ApellidoM, " +
                "CONCAT(bp.firstname, ' ',  emp.lastname1, ' ', emp.lastname2) AS " + SDbConsts.FIELD_NAME + ", 08 as TypeMov, " +
                "emp.num AS ClaveTrab, emp.ssn AS SSN, emp.sal_ssc AS Salario, e.id_tp_emp AS TpTrabajador, " +
                "sal.id_tp_sal AS TpSalario, wrktp.id_tp_work_day AS Jornada, cfg.ss_subbra AS Guia, hire.dt_hire AS DateApplication, par.reg_ss AS Param " +
                "FROM erp.HRSU_EMP AS emp " +
                "INNER JOIN erp.BPSU_BP AS bp ON bp.id_bp = emp.id_emp " +
                "INNER JOIN erp.HRSU_TP_EMP AS e ON e.id_tp_emp = emp.fk_tp_emp " +
                "INNER JOIN erp.hrss_tp_sal AS sal ON sal.id_tp_sal = emp.fk_tp_sal " +
                "INNER JOIN erp.hrss_tp_work_day AS wrktp ON emp.fk_tp_work_day = wrktp.id_tp_work_day " +  sql ;
//                "INNER JOIN erp.hrss_tp_work_day AS wrktp ON emp.fk_tp_work_day = wrktp.id_tp_work_day " + // alta
//                "INNER JOIN hrs_emp_log_hire AS hire ON hire.id_emp = emp.id_emp " +
//                "INNER JOIN cfg_param_co AS par " +
//                "INNER JOIN hrs_cfg AS cfg " +
//                "WHERE NOT hire.b_del  AND hire.b_hire = 1 AND id_log = 1 " + 
//                "AND hire.dt_hire >= " + start + " AND hire.dt_hire <= " + end + " ORDER BY bp.id_bp; " ;
                 
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
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "TypeMov", "Tipo movimiento", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "Guia", "Guia", 150));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "CURP", "Curp", 150));

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

            if (button == jbLayoutEmployeeHireIdse) {
                actionLayoutEmployeeHireIdse();
            }
            else if (button == jbLayoutEmployeeSbcIdse) {
                actionLayoutEmployeeSbcIdse();
            }
            else if (button == jbLayoutEmployeeDismissIdse) {
                actionLayoutEmployeeDismissIdse();
            }
        }
    }
}
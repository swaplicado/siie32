/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.gui.grid.SGridFilterPanel;
import erp.gui.grid.SGridFilterPanelEmployee;
import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbAbsence;
import erp.mod.hrs.form.SDialogAbsenceMovesCardex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Juan Barajas
 */
public class SViewAbsence extends SGridPaneView implements ActionListener {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private JButton jbCloseAbsence;
    private JButton jbCardex;
    private SGridFilterPanelEmployee moFilterEmployee;
    private SGridFilterPanel moFilterAbsenceClass;
    private SGridFilterPanel moFilterBusinessPartner;
    
    private SDialogAbsenceMovesCardex moDialogAbsenceMovesCardex;

    public SViewAbsence(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_ABS, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
        setRowButtonsEnabled(true, true, true, false, true);
    }

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        
        jbCloseAbsence = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif")), "Cerrar incidencia", this);
        jbCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Ver movimientos", this);
        
        moFilterEmployee = new SGridFilterPanelEmployee(miClient, this, SModConsts.HRSS_TP_PAY, SModConsts.HRSU_DEP);
        moFilterEmployee.initFilter(null);
        
        moFilterAbsenceClass = new SGridFilterPanel(miClient, this, SModConsts.HRSU_CL_ABS, SLibConsts.UNDEFINED);
        moFilterAbsenceClass.initFilter(null);
        
        moFilterBusinessPartner = new SGridFilterPanel(miClient, this, SModConsts.HRSU_EMP, SLibConsts.UNDEFINED);
        moFilterBusinessPartner.initFilter(null);
        
        moDialogAbsenceMovesCardex = new SDialogAbsenceMovesCardex(miClient, "Movimientos de la incidencia");
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCloseAbsence);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCardex);
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterEmployee);
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterAbsenceClass);
        getPanelCommandsCustom(SGuiConsts.PANEL_LEFT).add(moFilterBusinessPartner);
    }

    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

    /*
     * Private methods
     */

    private void actionCloseAbsence() {
        SDbAbsence absence = null;

        if (jbCloseAbsence.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                    absence = (SDbAbsence) miClient.getSession().readRegistry(SModConsts.HRS_ABS, gridRow.getRowPrimaryKey());

                    if (miClient.showMsgBoxConfirm("Está por cerrar la incidencia '" + absence.getNumber() + "'.\n" + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                        absence.setClosed(!absence.isClosed());
                        absence.setFkUserClosedId(miClient.getSession().getUser().getPkUserId());
                        if (absence.canSave(miClient.getSession())) {
                            absence.save(miClient.getSession());
                        }
                        else {
                            miClient.showMsgBoxWarning(absence.getQueryResult());
                        }

                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                }
                catch (SQLException e) {
                    SLibUtils.showException(this, e);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionShowCardex() {
        SDbAbsence absence = null;
        
        if (jbCardex.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    absence = (SDbAbsence) miClient.getSession().readRegistry(SModConsts.HRS_ABS, (int[]) gridRow.getRowPrimaryKey());
                    
                    moDialogAbsenceMovesCardex.setValue(SModConsts.HRS_ABS, absence);
                    moDialogAbsenceMovesCardex.setVisible(true);
                }
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

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
        
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
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSU_CL_ABS)) == null ? null : ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSU_CL_ABS)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_cl_abs = " + ((int[]) filter)[0] + " ";
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSU_EMP)) == null ? null : ((SGridFilterValue) moFiltersMap.get(SModConsts.HRSU_EMP)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.id_emp = " + ((int[]) filter)[0] + " ";
        }

        msSql = "SELECT "
                + "v.id_emp AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_abs AS " + SDbConsts.FIELD_ID + "2, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "bp.bp AS " + SDbConsts.FIELD_NAME + ", "
                + "v.num, "
                + "v.dt, "
                + "v.dt_sta, "
                + "v.dt_end, "
                + "v.eff_day, "
                + "(SELECT COALESCE(SUM(eff_day), 0) FROM hrs_abs_cns WHERE b_del = 0 AND id_emp = v.id_emp AND id_abs = v.id_abs) AS f_app_days, "
                + "v.ben_ann, "
                + "v.ben_year, "
                + "v.nts, "
                + "bp.bp, "
                + "cabs.name, "
                + "tabs.name, "
                + "v.b_clo, "
                + "v.fk_usr_clo, "
                + "v.ts_usr_clo, "
                + "uc.usr, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ABS) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                + "v.id_emp = bp.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp ON "
                + "v.id_emp = emp.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_CL_ABS) + " AS cabs ON "
                + "v.fk_cl_abs = cabs.id_cl_abs "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_ABS) + " AS tabs ON "
                + "v.fk_cl_abs = tabs.id_cl_abs AND v.fk_tp_abs = tabs.id_tp_abs "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON "
                + "v.fk_usr_clo = uc.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY bp.bp, v.id_emp, v.dt, cabs.name, tabs.name, v.id_abs ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "bp.bp", "Empleado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt", "Fecha registro"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "cabs.name", "Clase incidencia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "tabs.name", "Tipo incidencia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.num", "Número/folio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_sta", "Fecha inicial"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_end", "Fecha final"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "v.eff_day", "Días efectivos"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "f_app_days", "Días aplicados"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "v.ben_ann", "Aniversario"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, "v.ben_year", "Año aniversario"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_clo", "Cerrado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "uc.usr", "Usuario cerrado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_usr_clo", "Usr TS cerrado"));
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
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbCloseAbsence) {
                actionCloseAbsence();
            }
            else if (button == jbCardex) {
                actionShowCardex();
            }
        }
    }
}

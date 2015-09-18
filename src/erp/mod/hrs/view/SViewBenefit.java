/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SHrsConsts;
import erp.mod.hrs.form.SDialogBenefitCardex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDate;
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
public class SViewBenefit extends SGridPaneView implements ActionListener {

    private SGridFilterDate moFilterDate;
    private JButton jbCardex;
    private Date mtDateCut;
    
    private SDialogBenefitCardex moDialogBenefitCardex;
    
    public SViewBenefit(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRSX_BEN, gridSubtype, title);
        initComponentsCustom();
        setRowButtonsEnabled(false);
    }

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        moFilterDate = new SGridFilterDate(miClient, this);
        mtDateCut = null;
        
        if (mnGridSubtype == SModSysConsts.HRSS_TP_BEN_ANN_BON) {
            moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, SLibTimeUtils.getEndOfYear(miClient.getSession().getCurrentDate()).getTime()));
        }
        else {
            moFilterDate.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, miClient.getSession().getCurrentDate().getTime()));
        }
        jbCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Ver movimientos", this);
        
        moDialogBenefitCardex = new SDialogBenefitCardex(miClient, mnGridSubtype, "Control de la prestación");
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDate);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbCardex);
    }

    private void actionShowCardex() {
        int[] key = null;
        
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
                    key = (int[]) gridRow.getRowPrimaryKey();
                    
                    moDialogBenefitCardex.setFormParams(key[0], key[1], key[2], key[3], mtDateCut);
                    moDialogBenefitCardex.setVisible(true);
                }
            }
        }
    }

    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        String dateCut = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(4);

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE).getValue();
        if (filter != null) {
            mtDateCut = (SGuiDate) filter;
            dateCut = "'" + SLibUtils.DbmsDateFormatDate.format(mtDateCut) + "' ";
        }
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? " AND " : "") + "emp.b_act = 1 ";
        }

        msSql = "SELECT "
                + "bp.id_bp AS " + SDbConsts.FIELD_ID + "1, "
                + "IF(!emp.b_act, 0, TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ")) AS " + SDbConsts.FIELD_ID + "2, "
                + "IF(!emp.b_act, 0, TIMESTAMPDIFF(DAY,ADDDATE(emp.dt_ben, INTERVAL TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") YEAR)," + dateCut + ")) AS " + SDbConsts.FIELD_ID + "3, "
                + "v.id_ben AS " + SDbConsts.FIELD_ID + "4, "
                + "bp.bp AS " + SDbConsts.FIELD_CODE + ", "
                + "bp.bp AS " + SDbConsts.FIELD_NAME + ", "
                + "emp.dt_ben, "
                + "emp.b_act, "
                + "(ADDDATE(emp.dt_ben, INTERVAL TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") YEAR)) AS f_dt_base, "
                + "IF(!emp.b_act, 0, TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ")) AS f_sen, "
                + "IF(!emp.b_act, 0, TIMESTAMPDIFF(DAY,ADDDATE(emp.dt_ben, INTERVAL TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") YEAR)," + dateCut + ")) AS f_sen_day, "
                + "IF(!emp.b_act OR TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") = 0, 0, TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ")) AS f_ann, "
                + "IF(!emp.b_act OR TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") = 0, 0, (IF(YEAR(ADDDATE(emp.dt_ben, INTERVAL TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") YEAR)) > YEAR(emp.dt_ben), YEAR(ADDDATE(emp.dt_ben, INTERVAL TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") YEAR)) - 1, YEAR(emp.dt_ben)))) AS f_ann_ano, "
                + "IF(!emp.b_act OR TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") = 0, 0, IF(emp.b_act = 0, 0, vr.ben_day)) AS f_ben_day, "
                + "IF(v.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + ", 'día', '$') AS f_unit, "
                + "IF(!emp.b_act OR TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") = 0, 0, (SELECT br.ben_day FROM hrs_ben AS b INNER JOIN hrs_ben_row AS br ON b.id_ben = br.id_ben "
                + "WHERE fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND dt_sta <= " + dateCut + " AND br.id_row = ("
                + "SELECT br.id_row "
                + "FROM hrs_ben AS b "
                + "INNER JOIN hrs_ben_row AS br ON b.id_ben = br.id_ben "
                + "WHERE fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND dt_sta <= " + dateCut + " AND "
                + "mon >= TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") * " + SHrsConsts.YEAR_MONTHS + " "
                + "ORDER BY dt_sta DESC, b.id_ben LIMIT 1) "
                + "ORDER BY dt_sta DESC, b.id_ben LIMIT 1)) AS f_day_bon, "
                + "IF(!emp.b_act OR TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") = 0, 0, IF(vr.ben_bon_per = 0, 1, vr.ben_bon_per)) AS f_bon_per, "
                + "IF(!emp.b_act, 0, COALESCE(IF(emp.fk_tp_pay = " + SModSysConsts.HRSS_TP_PAY_FOR + ", emp.wage * " + SHrsConsts.YEAR_MONTHS + " / " + SHrsConsts.YEAR_DAYS + ", emp.sal), 0)) AS f_pay_day, "
                + "IF(!emp.b_act, 0, COALESCE((IF(emp.fk_tp_pay = " + SModSysConsts.HRSS_TP_PAY_FOR + ", emp.wage * " + SHrsConsts.YEAR_MONTHS + " / " + SHrsConsts.YEAR_DAYS + ", emp.sal) / emp.wrk_hrs_day), 0)) AS f_pay_hr, "
                + "IF(!emp.b_act OR TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") = 0, 0, IF(ear.ben_ann = TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + "), SUM(ear.unt), 0)) AS f_payed_unt, "
                + "IF(!emp.b_act OR TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") = 0, 0, IF(ear.ben_ann = TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + "), SUM(ear.amt_r), 0))  AS f_payed_amt, "
                + "IF(!emp.b_act OR TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") = 0, 0, IF(ear.ben_ann <> TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + "), SUM(ear.unt), 0)) AS f_payed_unt_oth, "
                + "IF(!emp.b_act OR TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") = 0, 0, IF(ear.ben_ann <> TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + "), SUM(ear.amt_r), 0))  AS f_payed_amt_oth "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                + "bp.id_bp = emp.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_BEN_ROW) + " AS vr ON "
                + "v.id_ben = vr.id_ben "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS rcp ON "
                + "rcp.id_emp = emp.id_emp AND rcp.b_del = 0 "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS ear ON "
                + "ear.id_pay = rcp.id_pay AND ear.id_emp = rcp.id_emp AND ear.fk_tp_ben = v.fk_tp_ben AND ear.b_del = 0 "
                + "WHERE v.id_ben = ( "
                + "SELECT id_ben FROM hrs_ben WHERE fk_tp_ben = " + mnGridSubtype + " AND dt_sta <= " + dateCut + " "
                + "ORDER BY dt_sta DESC, id_ben LIMIT 1) AND "
                + "vr.id_row = ( "
                + "SELECT br.id_row "
                + "FROM hrs_ben AS b "
                + "INNER JOIN hrs_ben_row AS br ON b.id_ben = br.id_ben "
                + "WHERE fk_tp_ben = " + mnGridSubtype + " AND dt_sta <= " + dateCut + " AND "
                + "mon >= TIMESTAMPDIFF(YEAR,emp.dt_ben," + dateCut + ") * " + SHrsConsts.YEAR_MONTHS + " "
                + "ORDER BY dt_sta DESC, b.id_ben LIMIT 1) " + sql + " "
                + "GROUP BY emp.id_emp "
                + "ORDER BY bp.bp, bp.id_bp ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "emp.dt_ben", "Fecha beneficios"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "f_dt_base", "Fecha base"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "f_sen", "Antigüedad años"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "f_sen_day", "Antigüedad días"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "f_ann", "Aniversario"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_CAL_YEAR, "f_ann_ano", "Aniversario año"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_8D, "f_pay_day", "Salario diario"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, (mnGridSubtype == SModSysConsts.HRSS_TP_BEN_VAC_BON ? "f_day_bon" : "f_ben_day"), "Días"));
        
        if (mnGridSubtype == SModSysConsts.HRSS_TP_BEN_VAC) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "f_payed_unt", "Pagado"));
            column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_4D, "", "Por pagar");
            column.getRpnArguments().add(new SLibRpnArgument("f_ben_day", SLibRpnArgumentType.OPERAND));
            column.getRpnArguments().add(new SLibRpnArgument("f_payed_unt", SLibRpnArgumentType.OPERAND));
            column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        }
        else if (mnGridSubtype == SModSysConsts.HRSS_TP_BEN_VAC_BON) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "f_bon_per", "Prima"));
            
            column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "", "Monto $");
            column.getRpnArguments().add(new SLibRpnArgument("f_day_bon", SLibRpnArgumentType.OPERAND));
            column.getRpnArguments().add(new SLibRpnArgument("f_bon_per", SLibRpnArgumentType.OPERAND));
            column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.MULTIPLICATION, SLibRpnArgumentType.OPERATOR));
            column.getRpnArguments().add(new SLibRpnArgument("f_pay_day", SLibRpnArgumentType.OPERAND));
            column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.MULTIPLICATION, SLibRpnArgumentType.OPERATOR));
            column.setSumApplying(true);
            gridColumnsViews.add(column);
            
            column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_payed_amt", "Pagado $");
            column.setSumApplying(true);
            gridColumnsViews.add(column);
            
            column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "", "Por pagar $");
            column.setSumApplying(true);
            column.getRpnArguments().add(new SLibRpnArgument("f_day_bon", SLibRpnArgumentType.OPERAND));
            column.getRpnArguments().add(new SLibRpnArgument("f_bon_per", SLibRpnArgumentType.OPERAND));
            column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.MULTIPLICATION, SLibRpnArgumentType.OPERATOR));
            column.getRpnArguments().add(new SLibRpnArgument("f_pay_day", SLibRpnArgumentType.OPERAND));
            column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.MULTIPLICATION, SLibRpnArgumentType.OPERATOR));
            column.getRpnArguments().add(new SLibRpnArgument("f_payed_amt", SLibRpnArgumentType.OPERAND));
            column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        }
        else {
            column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "", "Monto $");
            column.getRpnArguments().add(new SLibRpnArgument("f_ben_day", SLibRpnArgumentType.OPERAND));
            column.getRpnArguments().add(new SLibRpnArgument("f_pay_day", SLibRpnArgumentType.OPERAND));
            column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.MULTIPLICATION, SLibRpnArgumentType.OPERATOR));
            column.setSumApplying(true);
            gridColumnsViews.add(column);
            
            column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "f_payed_amt", "Pagado $");
            column.setSumApplying(true);
            gridColumnsViews.add(column);
            
            column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "", "Por pagar $");
            column.setSumApplying(true);
            column.getRpnArguments().add(new SLibRpnArgument("f_ben_day", SLibRpnArgumentType.OPERAND));
            column.getRpnArguments().add(new SLibRpnArgument("f_pay_day", SLibRpnArgumentType.OPERAND));
            column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.MULTIPLICATION, SLibRpnArgumentType.OPERATOR));
            column.getRpnArguments().add(new SLibRpnArgument("f_payed_amt", SLibRpnArgumentType.OPERAND));
            column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        }
        gridColumnsViews.add(column);
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "f_unit", "Unidad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "emp.b_act", "Activo"));
        
        // Proportional share:
        /*
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "", "Parte prop");
        column.getRpnArguments().add(new SLibRpnArgument("f_tot", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument("f_wei_des", SLibRpnArgumentType.OPERAND));
        column.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.DIVISION, SLibRpnArgumentType.OPERATOR));
        gridColumnsViews.add(column);
        */

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.HRS_EAR);
        moSuscriptionsSet.add(SModConsts.HRS_DED);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbCardex) {
                actionShowCardex();
            }
        }
    }
}

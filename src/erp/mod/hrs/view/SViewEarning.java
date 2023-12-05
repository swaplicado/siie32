/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbCfgAccountingEarning;
import erp.mod.hrs.db.SHrsConsts;
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
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SViewEarning extends SGridPaneView implements ActionListener {

    private int mnParamPayrollAccProcess;
    private JButton mjCfgAccounting;
    
    public SViewEarning(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_EAR, SLibConsts.UNDEFINED, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(true, true, true, false, true);
        
        try {
            mnParamPayrollAccProcess = SLibUtils.parseInt(SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_HRS_PAYROLL_ACC_PROCESS));
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        if (mnParamPayrollAccProcess == SHrsConsts.CFG_ACC_PROCESS_DYNAMIC) {
            mjCfgAccounting = SGridUtils.createButton(new ImageIcon(getClass().getResource("/sa/lib/img/cmd_std_lot.gif")), "Configurar contabilización", this);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjCfgAccounting);
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setSystemApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT "
                + "v.id_ear AS " + SDbConsts.FIELD_ID + "1, "
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "v.name_abbr, "
                + "v.exem_mwz, "
                + "v.exem_sal_equ_mwz_per, "
                + "v.exem_sal_equ_mwz_lim, "
                + "v.exem_sal_grt_mwz_per, "
                + "v.exem_sal_grt_mwz_lim, "
                + "v.exem_mwz_year, "
                + "v.pay_per, "
                + "v.unt_max_wee, "
                + "v.unt_fac, "
                + "CONCAT(vt.code, ' - ', vt.name) AS _ear_tp, "
                + "ec.code, "
                + "ec.name, "
                + "ee.name, "
                + "eey.name, "
                + "CONCAT(op.code, ' - ', op.name) AS _op_tp, "
                + "l.name, "
                + "b.name, "
                + "ac.name, "
                + "ar.name, "
                + "ca.name, "
                + "ta.name, "
                + "v.b_wel, "
                + "v.b_day_adj, "
                + "v.b_day_abs, "
                + "v.b_day_wrk, "
                + "v.b_day_wrk_bas, "
                + "v.b_who, "
                + "v.b_pay_tax, "
                + "v.b_alt_tax, "
                + "IF(cacc.id_ear IS NULL, " + SGridConsts.ICON_WARN + ", " + SGridConsts.ICON_OK + ") AS _ico_cfg, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_EAR) + " AS vt ON "
                + "v.fk_tp_ear = vt.id_tp_ear "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_EAR_COMP) + " AS ec ON "
                + "v.fk_tp_ear_comp = ec.id_tp_ear_comp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_EAR_EXEM) + " AS ee ON "
                + "v.fk_tp_ear_exem = ee.id_tp_ear_exem "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_EAR_EXEM) + " AS eey ON "
                + "v.fk_tp_ear_exem_year = eey.id_tp_ear_exem "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_OTH_PAY) + " AS op ON "
                + "v.fk_tp_oth_pay = op.id_tp_oth_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_LOAN) + " AS l ON "
                + "v.fk_tp_loan = l.id_tp_loan "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_BEN) + " AS b ON "
                + "v.fk_tp_ben = b.id_tp_ben "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_ACC) + " AS ac ON "
                + "v.fk_tp_acc_cfg = ac.id_tp_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_ACC) + " AS ar ON "
                + "v.fk_tp_acc_rec = ar.id_tp_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_CL_ABS) + " AS ca ON "
                + "v.fk_cl_abs_n = ca.id_cl_abs "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_ABS) + " AS ta ON "
                + "v.fk_cl_abs_n = ta.id_cl_abs AND v.fk_tp_abs_n = ta.id_tp_abs "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_CFG_ACC_EAR) + " AS cacc ON "
                + "v.id_ear = cacc.id_ear "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.code, v.name, v.id_ear ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.name_abbr", SGridConsts.COL_TITLE_NAME + " corto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "_ear_tp", "Tipo percepción"));
        if (mnParamPayrollAccProcess == SHrsConsts.CFG_ACC_PROCESS_DYNAMIC) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_ico_cfg", "Configuración contabilización"));
        }
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "ec.code", "Tipo cálculo percepción"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ee.name", "Tipo exención percepción (base)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_8D, "v.exem_mwz", "UMA exentas (base)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "eey.name", "Tipo exención percepción (adicional)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_8D, "v.exem_mwz_year", "UMA exentas (adicional)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "v.exem_sal_equ_mwz_per", "% exención si SB = SMA"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_8D, "v.exem_sal_equ_mwz_lim", "Tope exención si SB = SMA"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "v.exem_sal_grt_mwz_per", "% exención si SB > SMA"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_8D, "v.exem_sal_grt_mwz_lim", "Tope exención si SB > SMA"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "v.pay_per", "% percención"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT_UNIT, "v.unt_max_wee", "Unidades máximas semana"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_8D, "v.unt_fac", "Factor cálculo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_op_tp", "Tipo otro pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "l.name", "Tipo crédito/préstamo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "b.name", "Tipo prestación"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ac.name", "Tipo configuración contable"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ar.name", "Tipo registro contable"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ca.name", "Clase incidencia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ta.name", "Tipo incidencia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_day_adj", "Aplica ajuste días"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_day_abs", "Aplica días incidencia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_day_wrk", "Cuenta como días trabajados"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_day_wrk_bas", "En base a días trabajados"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_wel", "Previsión social"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_who", "Retención ley"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_pay_tax", "Aplica impuesto nóminas"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "v.b_alt_tax", "Usar cálculo alterno impto. (Art 174 RLISR)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS));
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
        moSuscriptionsSet.add(SModConsts.HRSU_CL_ABS);
        moSuscriptionsSet.add(SModConsts.HRSU_TP_ABS);
        
        // suscriptions when dynamic processing of accounting has been set in configuration:
        moSuscriptionsSet.add(SModConsts.HRS_CFG_ACC_EAR);
    }
    
    private void actionPerformedCfgAccounting() {
        if (mjCfgAccounting.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    try {
                        SGuiParams params;
                        int existingRegistries = SDbCfgAccountingEarning.countExistingRegistries(miClient.getSession(), gridRow.getRowPrimaryKey()[0]);

                        if (existingRegistries == 0) {
                            params = new SGuiParams();
                            params.getParamsMap().put(SModConsts.HRS_CFG_ACC_EAR, gridRow.getRowPrimaryKey()[0]);
                        }
                        else {
                            params = new SGuiParams(gridRow.getRowPrimaryKey());
                        }

                        miClient.getSession().showForm(SModConsts.HRS_CFG_ACC_EAR, 0, params);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == mjCfgAccounting) {
                actionPerformedCfgAccounting();
            }
        }
    }
}

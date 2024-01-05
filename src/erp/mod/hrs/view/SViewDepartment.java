/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mcfg.data.SDataParamsCompany;
import erp.mod.SModConsts;
import erp.mod.hrs.db.SHrsConsts;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Juan Barajas, Edwin Carmona, Sergio Flores
 */
public class SViewDepartment extends SGridPaneView {

    private int mnParamPayrollAccProcess;
    
    public SViewDepartment(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRSU_DEP, SLibConsts.UNDEFINED, title);
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
                + "v.id_dep AS " + SDbConsts.FIELD_ID + "1, "
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "COALESCE(d.name, '') AS _dep_sup, "
                + "COALESCE(bp.bp, '') AS _emp_head, "
                + "IF(caccd.id_dep IS NULL, " + SGridConsts.ICON_WARN + ", " + SGridConsts.ICON_OK + ") AS _ico_cfg, "
                + "cte.name AS _cte_name, "
                + "f_acc_usr(" + ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskAccount() + ", acc.code) AS _acc, "
                + "acc.acc, "
                + "cbp.bp AS _cbp_name, "
                + "ctax.tax AS _ctax_name, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS d ON "
                + "v.fk_dep_sup_n = d.id_dep "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON "
                + "v.fk_emp_head_n = bp.id_bp "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_CFG_ACC_DEP) + " AS caccd ON "
                + "v.id_dep = caccd.id_dep "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_EXP) + " AS cte ON "
                + "caccd.fk_tp_exp = cte.id_tp_exp "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS acc ON "
                + "caccd.fk_acc = acc.pk_acc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS cbp ON "
                + "caccd.fk_bp_n = cbp.id_bp "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.FINU_TAX) + " AS ctax ON "
                + "caccd.fk_tax_bas_n = ctax.id_tax_bas AND caccd.fk_tax_tax_n = ctax.id_tax "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.name, v.id_dep ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "_dep_sup", "Depto. superior"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "_emp_head", "Titular depto."));
        
        if (mnParamPayrollAccProcess == SHrsConsts.CFG_ACC_PROCESS_DYNAMIC) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_ico_cfg", "Configuración contabilización"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "_cte_name", "Tipo gasto"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "_acc", "No. cuenta contable"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "acc.acc", "Cuenta contable"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "_cbp_name", "Asociado negocios"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "_ctax_name", "Impuesto"));
        }
        
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
        moSuscriptionsSet.add(SModConsts.USRU_USR);
        
        // suscriptions when dynamic processing of accounting has been set in configuration:
        moSuscriptionsSet.add(SModConsts.HRSU_TP_EXP);
        moSuscriptionsSet.add(SModConsts.HRS_CFG_ACC_DEP);
        moSuscriptionsSet.add(SModConsts.FIN_ACC);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.FINU_TAX);
    }
}

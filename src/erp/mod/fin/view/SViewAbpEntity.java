/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.data.SDataConstantsSys;
import erp.mcfg.data.SDataParamsCompany;
import erp.mod.SModConsts;
import java.util.ArrayList;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Juan Barajas
 */
public class SViewAbpEntity extends SGridPaneView {

    public SViewAbpEntity(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.FIN_ABP_ENT, gridSubtype, title);
        setRowButtonsEnabled(true, true, true, false, true);
    }

    @Override
    public void prepareSqlQuery() {
        int maskAccount = ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskAccount();
        int maskCostCenter = ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter();
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT "
                + "v.id_abp_ent AS " + SDbConsts.FIELD_ID + "1, "
                + "' ' AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "f_acc_usr(" + maskAccount + ", a.code) AS f_a_code, "
                + "a.acc, "
                + "f_acc_usr(" + maskAccount + ", ai.code) AS f_ai_code, "
                + "ai.acc, "
                + "f_acc_usr(" + maskAccount + ", aie.code) AS f_aie_code, "
                + "aie.acc, "
                + "f_acc_usr(" + maskAccount + ", aix.code) AS f_aix_code, "
                + "aix.acc, "
                + "f_acc_usr(" + maskAccount + ", aia.code) AS f_aia_code, "
                + "aia.acc, "
                + "f_acc_usr(" + maskAccount + ", aif.code) AS f_aif_code, "
                + "aif.acc, "
                + "f_acc_usr(" + maskAccount + ", aoe.code) AS f_aoe_code, "
                + "aoe.acc, "
                + "f_acc_usr(" + maskAccount + ", aox.code) AS f_aox_code, "
                + "aox.acc, "
                + "f_acc_usr(" + maskAccount + ", aoa.code) AS f_aoa_code, "
                + "aoa.acc, "
                + "f_acc_usr(" + maskAccount + ", aof.code) AS f_aof_code, "
                + "aof.acc, "
                + "f_acc_usr(" + maskAccount + ", cix.code) AS f_cix_code, "
                + "cix.cc, "
                + "f_acc_usr(" + maskAccount + ", cia.code) AS f_cia_code, "
                + "cia.cc, "
                + "f_acc_usr(" + maskAccount + ", cif.code) AS f_cif_code, "
                + "cif.cc, "
                + "f_acc_usr(" + maskAccount + ", cox.code) AS f_cox_code, "
                + "cox.cc, "
                + "f_acc_usr(" + maskAccount + ", coa.code) AS f_coa_code, "
                + "coa.cc, "
                + "f_acc_usr(" + maskAccount + ", cof.code) AS f_cof_code, "
                + "cof.cc, "
                + "f_acc_usr(" + maskAccount + ", awh.code) AS f_awh_code, "
                + "awh.acc, "
                + "f_acc_usr(" + maskAccount + ", apwh.code) AS f_apwh_code, "
                + "apwh.acc, "
                + "f_acc_usr(" + maskAccount + ", apwp.code) AS f_apwp_code, "
                + "apwp.acc, "
                + "f_acc_usr(" + maskAccount + ", apr.code) AS f_apr_code, "
                + "apr.acc, "
                + "f_acc_usr(" + maskAccount + ", aoh.code) AS f_aoh_code, "
                + "aoh.acc, "
                + "f_acc_usr(" + maskAccount + ", cpwp.code) AS f_cpwp_code, "
                + "cpwp.cc, "
                + "f_acc_usr(" + maskAccount + ", cpr.code) AS f_cpr_code, "
                + "cpr.cc, "
                + "f_acc_usr(" + maskAccount + ", coh.code) AS f_coh_code, "
                + "coh.cc, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ABP_ENT) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGS_CT_ENT) + " AS vc ON "
                + "v.fk_ct_ent = vc.id_ct_ent AND vc.id_ct_ent = " + mnGridSubtype + " "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS a ON "
                + "v.fk_cash_acc = a.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS ai ON "
                + "v.fk_cash_acc_inv = ai.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS aie ON "
                + "v.fk_cash_acc_in_eqy = aie.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS aix ON "
                + "v.fk_cash_acc_in_exc_rate = aix.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS aia ON "
                + "v.fk_cash_acc_in_adj = aia.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS aif ON "
                + "v.fk_cash_acc_in_fin = aif.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS aoe ON "
                + "v.fk_cash_acc_out_eqy = aoe.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS aox ON "
                + "v.fk_cash_acc_out_exc_rate = aox.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS aoa ON "
                + "v.fk_cash_acc_out_adj = aoa.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS aof ON "
                + "v.fk_cash_acc_out_fin = aof.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cix ON "
                + "v.fk_cash_cc_in_exc_rate = cix.pk_cc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cia ON "
                + "v.fk_cash_cc_in_adj = cia.pk_cc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cif ON "
                + "v.fk_cash_cc_in_fin = cif.pk_cc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cox ON "
                + "v.fk_cash_cc_out_exc_rate = cox.pk_cc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS coa ON "
                + "v.fk_cash_cc_out_adj = coa.pk_cc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cof ON "
                + "v.fk_cash_cc_out_fin = cof.pk_cc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS awh ON "
                + "v.fk_wh_acc = awh.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS apwh ON "
                + "v.fk_pla_acc_wh = apwh.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS apwp ON "
                + "v.fk_pla_acc_wp = apwp.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS apr ON "
                + "v.fk_pla_acc_exp_pr = apr.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS aoh ON "
                + "v.fk_pla_acc_exp_oh = aoh.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cpwp ON "
                + "v.fk_pla_cc_wp = cpwp.pk_cc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cpr ON "
                + "v.fk_pla_cc_exp_pr = cpr.pk_cc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS coh ON "
                + "v.fk_pla_cc_exp_oh = coh.pk_cc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.name, v.id_abp_ent ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME));

        if (mnGridSubtype == SDataConstantsSys.CFGS_CT_ENT_CASH) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_a_code", "Núm cta Activo"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "a.acc", "Activo"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_ai_code", "Núm cta Inversiones"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "ai.acc", "Inversiones"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_aie_code", "Núm cta Ingreso socios"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "aie.acc", "Ingreso socios"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_aix_code", "Núm cta Ingreso diferencias cambiarias"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "aix.acc", "Ingreso diferencias cambiarias"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_cix_code", "Núm CC ingreso diferencias cambiarias"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "cix.cc", "CC ingreso diferencias cambiarias"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_aia_code", "Núm cta Ingreso ajustes"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "aia.acc", "Ingreso ajustes"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_cia_code", "Núm CC ingreso ajustes"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "cia.cc", "CC ingreso ajustes"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_aif_code", "Núm cta Ingreso financieros"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "aif.acc", "Ingreso financieros"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_cif_code", "Núm CC ingreso financieros"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "cif.cc", "CC ingreso financieros"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_aoe_code", "Núm cta Egreso socios"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "aoe.acc", "Egreso socios"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_aox_code", "Núm cta Egreso diferencias cambiarias"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "aox.acc", "Egreso diferencias cambiarias"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_cox_code", "Núm CC egreso diferencias cambiarias"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "cox.cc", "CC egreso diferencias cambiarias"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_aoa_code", "Núm cta Egreso ajustes"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "aoa.acc", "Egreso ajustes"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_coa_code", "Núm CC egreso ajustes"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "coa.cc", "CC egreso ajustes"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_aof_code", "Núm cta Egreso financieros"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "aof.acc", "Egreso financieros"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_cof_code", "Núm CC egreso financieros"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "cof.cc", "CC egreso financieros"));
        }
        else if (mnGridSubtype == SDataConstantsSys.CFGS_CT_ENT_WH) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_awh_code", "Núm cta Inventarios"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "awh.acc", "Inventarios"));
        }
        else if (mnGridSubtype == SDataConstantsSys.CFGS_CT_ENT_PLANT) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_apwh_code", "Núm cta Inventarios PT"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "apwh.acc", "Inventarios PT"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_apwp_code", "Núm cta Producción proceso"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "apwp.acc", "Producción proceso"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_cpwp_code", "Núm CC producción proceso"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "cpwp.cc", "CC producción proceso"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_apr_code", "Núm cta MO"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "apr.acc", "MO"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_cpr_code", "Núm CC MO"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "cpr.cc", "CC MO"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_aoh_code", "Núm cta GIF"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "aoh.acc", "GIF"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_coh_code", "Núm CC GIF"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "coh.cc", "CC GIF"));
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
        moSuscriptionsSet.add(SModConsts.FIN_ACC);
        moSuscriptionsSet.add(SModConsts.FIN_CC);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

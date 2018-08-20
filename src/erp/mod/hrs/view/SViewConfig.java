/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mod.SModConsts;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SViewConfig extends SGridPaneView {

    public SViewConfig(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_CFG, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false, false, true, false, false);
    }

    @Override
    public void prepareSqlQuery() {
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
                + "v.id_cfg AS " + SDbConsts.FIELD_ID + "1, "
                + "'' AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt_ops, "
                + "v.fst_day_wee, "
                + "v.lim_mwz_ref, "
                + "v.num_ser, "
                + "v.ss_subbra, "
                + "v.baj_aff_grp, "
                + "v.pay_tax_rate, "
                + "v.b_for_std, "
                + "v.b_auto_vac_bon, "
                + "v.b_tax_sub_ear, "
                + "v.b_tax_net, "
                + "v.b_bank_acc_use, "
                + "tmwz.name, "
                + "tmwz_ref.name, "
                + "ttax_comp.name, "
                + "bank.name, "
                + "eear.name, "
                + "evac.name, "
                + "etax.name, "
                + "etax_sub.name, "
                + "essc.name, "
                + "dtax.name, "
                + "dtax_sub.name, "
                + "dssc.name, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_CFG) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_MWZ) + " AS tmwz ON "
                + "v.fk_tp_mwz = tmwz.id_tp_mwz "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_MWZ) + " AS tmwz_ref ON "
                + "v.fk_tp_mwz_ref = tmwz_ref.id_tp_mwz "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_TAX_COMP) + " AS ttax_comp ON "
                + "v.fk_tp_tax_comp = ttax_comp.id_tp_tax_comp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_BANK) + " AS bank ON "
                + "v.fk_bank = bank.id_bank "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS eear ON "
                + "v.fk_ear_ear_n = eear.id_ear "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS evac ON "
                + "v.fk_ear_vac_n = evac.id_ear "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS etax ON "
                + "v.fk_ear_tax_n = etax.id_ear "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS etax_sub ON "
                + "v.fk_ear_tax_sub_n = etax_sub.id_ear "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS essc ON "
                + "v.fk_ear_ssc_n = essc.id_ear "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " AS dtax ON "
                + "v.fk_ded_tax_n = dtax.id_ded "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " AS dtax_sub ON "
                + "v.fk_ded_tax_sub_n = dtax_sub.id_ded "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " AS dssc ON "
                + "v.fk_ded_ssc_n = dssc.id_ded "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.id_cfg ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_ops", "Inicio operaciones"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "v.fst_day_wee", "1er día semana"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "v.lim_mwz_ref", "Tope cot SMAR"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_4D, "v.pay_tax_rate", "Imp sobre nóminas"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_for_std", "Quincenas fijas 15 días"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_auto_vac_bon", "Pagao auto prima vacacional"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_tax_sub_ear", "Sub impto percepción"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_tax_net", "Impto neto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tmwz.name", "Área geo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tmwz_ref.name", "Área geo referencia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ttax_comp.name", "Cálculo impto default"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.num_ser", "Serie CFDI"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.ss_subbra", "Subdelegación SS"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "bank.name", "Banco default"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "v.baj_aff_grp", "Grupo afinidad (BanBajío)"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "eear.name", "Percep normal"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "evac.name", "Percep vacaciones"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "etax.name", "Percep impto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "etax_sub.name", "Percep sub impto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "essc.name", "Percep ret SS"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "dtax.name", "Deduc impto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "dtax_sub.name", "Deduc sub impto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "dssc.name", "Deduc ret SS"));
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
        moSuscriptionsSet.add(SModConsts.HRS_EAR);
        moSuscriptionsSet.add(SModConsts.HRS_DED);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

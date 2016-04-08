/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.gui.grid.SGridFilterPanel;
import erp.mcfg.data.SDataParamsCompany;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Juan Barajas
 */
public class SViewAccountingDeduction extends SGridPaneView implements ActionListener {

    private SGridFilterPanel moFilterDual;
    private SGridFilterPanel moFilterDeduction;

    public SViewAccountingDeduction(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_ACC_DED, gridSubtype, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(false, false, true, false, false);
        
        moFilterDual = new SGridFilterPanel(miClient, this, (mnGridSubtype == SModSysConsts.HRSS_TP_ACC_DEP ? SModConsts.HRSU_DEP : SModConsts.HRSU_EMP), SLibConsts.UNDEFINED);
        moFilterDual.initFilter(null);
        
        moFilterDeduction = new SGridFilterPanel(miClient, this, SModConsts.HRS_DED, SLibConsts.UNDEFINED);
        moFilterDeduction.initFilter(null);

        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDeduction);
        switch (mnGridSubtype) {
            case SModSysConsts.HRSS_TP_ACC_DEP:
            case SModSysConsts.HRSS_TP_ACC_EMP:
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDual);
                break;
            default:
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(3);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "") + "v.b_del = 0 ";
        }

        switch (mnGridSubtype) {
            case SModSysConsts.HRSS_TP_ACC_DEP:
            case SModSysConsts.HRSS_TP_ACC_EMP:
                filter = ((SGridFilterValue) moFiltersMap.get((mnGridSubtype == SModSysConsts.HRSS_TP_ACC_DEP ? SModConsts.HRSU_DEP : SModConsts.HRSU_EMP))).getValue();
                if (filter != null && ((int[]) filter).length == 1) {
                    sql += (sql.isEmpty() ? "" : "AND ") + "v.id_ref = " + ((int[]) filter)[0] + " ";
                }
                break;
            default:
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.HRS_DED)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.id_ded = " + ((int[]) filter)[0] + " ";
        }

        msSql = "SELECT "
                + "v.id_ded AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_tp_acc AS " + SDbConsts.FIELD_ID + "2, "
                + "v.id_ref AS " + SDbConsts.FIELD_ID + "3, "
                + "ded.name AS " + SDbConsts.FIELD_CODE + ", "
                + "ded.name AS " + SDbConsts.FIELD_NAME + ", "
                + "vt.name, "
                + (mnGridSubtype == SModSysConsts.HRSS_TP_ACC_DEP ? "ref.name" : mnGridSubtype == SModSysConsts.HRSS_TP_ACC_EMP ? "ref.bp" : "''") + " AS f_ref, "
                + "f_acc_usr(" + ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskAccount() + ", ac.code) AS f_acc, "
                + "ac.acc, "
                + "cc.id_cc, "
                + "cc.cc, "
                + "bb.bp, "
                + "i.item, "
                + "i.item_key, "
                + "tax.tax, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_DED) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " AS ded ON "
                + "v.id_ded = ded.id_ded "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_ACC) + " AS vt ON "
                + "v.id_tp_acc = vt.id_tp_acc ";
        if (mnGridSubtype == SModSysConsts.HRSS_TP_ACC_DEP) {
            msSql += "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS ref ON "
                + "v.id_ref = ref.id_dep ";
        }
        else if (mnGridSubtype == SModSysConsts.HRSS_TP_ACC_EMP) {
            msSql += "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS ref ON "
                + "v.id_ref = ref.id_bp ";
        }
        msSql += "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS ac ON "
                + "v.fk_acc = ac.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cc ON "
                + "v.fk_cc_n = cc.pk_cc "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bb ON "
                + "v.fk_bp_n = bb.id_bp "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON "
                + "v.fk_item_n = i.id_item "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FINU_TAX) + " AS tax ON "
                + "v.fk_tax_bas_n = tax.id_tax_bas AND v.fk_tax_tax_n = tax.id_tax "
                + "WHERE v.id_tp_acc = " + mnGridSubtype + (sql.isEmpty() ? "" : " AND " + sql) + " "
                + "ORDER BY ded.name, v.id_ded, v.id_tp_acc ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, "Deducción"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "vt.name", "Tipo configuración contable"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "f_ref", "Referencia"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "f_acc", "No. cuenta contable"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "ac.acc", "Cuenta contable"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "cc.id_cc", "No. centro costos"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "cc.cc", "Centro costos"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bb.bp", "Asociado negocios"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "i.item_key", "Clave ítem"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.item", "Ítem"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tax.tax", "Impuesto"));
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
        moSuscriptionsSet.add(SModConsts.HRS_DED);
        moSuscriptionsSet.add(SModConsts.HRSU_DEP);
        moSuscriptionsSet.add(SModConsts.HRSU_EMP);
        moSuscriptionsSet.add(SModConsts.FIN_ACC);
        moSuscriptionsSet.add(SModConsts.FIN_CC);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.FINU_TAX);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

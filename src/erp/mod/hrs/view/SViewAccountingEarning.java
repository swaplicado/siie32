/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mcfg.data.SDataParamsCompany;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class SViewAccountingEarning extends SGridPaneView implements ActionListener {

    public SViewAccountingEarning(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_ACC_EAR, gridSubtype, title);
        setRowButtonsEnabled(false, false, true, false, false);
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

        msSql = "SELECT "
                + "v.id_ear AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_tp_acc AS " + SDbConsts.FIELD_ID + "2, "
                + "v.id_ref AS " + SDbConsts.FIELD_ID + "3, "
                + "ear.name AS " + SDbConsts.FIELD_CODE + ", "
                + "ear.name AS " + SDbConsts.FIELD_NAME + ", "
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
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_EAR) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS ear ON "
                + "v.id_ear = ear.id_ear "
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
                + "ORDER BY ear.name, v.id_ear, v.id_tp_acc ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, "Percepción"));
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
        moSuscriptionsSet.add(SModConsts.HRS_EAR);
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

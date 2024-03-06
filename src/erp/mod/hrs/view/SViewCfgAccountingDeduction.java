/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mcfg.data.SDataParamsCompany;
import erp.mod.SModConsts;
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
 * @author Sergio Flores
 */
public class SViewCfgAccountingDeduction extends SGridPaneView implements ActionListener {

    public SViewCfgAccountingDeduction(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_CFG_ACC_DED, 0, title);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        setRowButtonsEnabled(true, true, true, false, true);
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "") + "NOT ca.b_del AND NOT ded.b_del ";
        }

        msSql = "SELECT "
                + "ca.id_ded AS " + SDbConsts.FIELD_ID + "1, "
                + "ded.code AS " + SDbConsts.FIELD_CODE + ", "
                + "ded.name AS " + SDbConsts.FIELD_NAME + ", "
                + "ded.b_del, "
                + "f_acc_usr(" + ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskAccount() + ", acc.code) AS _acc, "
                + "acc.acc, "
                + "pe.code, "
                + "pe.name, "
                + "pcc.code, "
                + "pcc.name, "
                + "ta.name, "
                + "IF(ca.fk_tp_acc_rec = ded.fk_tp_acc_rec, " + SGridConsts.ICON_OK + ", " + SGridConsts.ICON_WARN + ") AS _ta_ok, "
                + "bb.bp, "
                + "tax.tax, "
                + "ca.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "ca.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "ca.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "ca.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "ca.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_CFG_ACC_DED) + " AS ca "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " AS ded ON ca.id_ded = ded.id_ded "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS acc ON ca.fk_acc = acc.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_PACK_EXP) + " AS pe ON ca.fk_pack_exp = pe.id_pack_exp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PACK_CC) + " AS pcc ON ca.fk_pack_cc = pcc.id_pack_cc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_ACC) + " AS ta ON ca.fk_tp_acc_rec = ta.id_tp_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON ca.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON ca.fk_usr_upd = uu.id_usr "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bb ON ca.fk_bp_n = bb.id_bp "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.FINU_TAX) + " AS tax ON ca.fk_tax_bas_n = tax.id_tax_bas AND ca.fk_tax_tax_n = tax.id_tax "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY ded.code, ded.name, ca.id_ded ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE + " deducción"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME + " deducción"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "ta.name", SGridConsts.COL_TITLE_TYPE + " registro contable"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_ta_ok", "Coincidencia registro contable deducción"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "pe.name", SGridConsts.COL_TITLE_NAME + " paquete gastos"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "pe.code", SGridConsts.COL_TITLE_CODE + " paquete gastos"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "_acc", "No. cuenta contable"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "acc.acc", "Cuenta contable"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "pcc.name", SGridConsts.COL_TITLE_NAME + " paquete centros costo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "pcc.code", SGridConsts.COL_TITLE_CODE + " paquete centros costo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bb.bp", "Asociado negocios"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "tax.tax", "Impuesto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "ded.b_del", SGridConsts.COL_TITLE_IS_DEL + " deducción"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.HRS_DED);
        moSuscriptionsSet.add(SModConsts.HRSU_PACK_EXP);
        moSuscriptionsSet.add(SModConsts.FIN_ACC);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.FINU_TAX);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

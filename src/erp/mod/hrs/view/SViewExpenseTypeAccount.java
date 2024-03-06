/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

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
 * @author Sergio Flores
 */
public class SViewExpenseTypeAccount extends SGridPaneView {

    public SViewExpenseTypeAccount(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_TP_EXP_ACC, 0, title);
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
            sql += (sql.isEmpty() ? "" : "") + "NOT tea.b_del AND NOT te.b_del ";
        }

        msSql = "SELECT "
                + "tea.id_tp_exp AS " + SDbConsts.FIELD_ID + "1, "
                + "te.code AS " + SDbConsts.FIELD_CODE + ", "
                + "te.name AS " + SDbConsts.FIELD_NAME + ", "
                + "te.b_del, "
                + "f_acc_usr(" + ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskAccount() + ", acc.code) AS _acc, "
                + "acc.acc, "
                + "tea.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "tea.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "tea.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "tea.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "tea.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_TP_EXP_ACC) + " AS tea "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_EXP) + " AS te ON te.id_tp_exp = tea.id_tp_exp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " AS acc ON tea.fk_acc = acc.pk_acc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON tea.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON tea.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY te.name, te.code, tea.id_tp_exp ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, SGridConsts.COL_TITLE_NAME + " tipo gasto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, SGridConsts.COL_TITLE_CODE + " tipo gasto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "_acc", "No. cuenta contable"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "acc.acc", "Cuenta contable"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "te.b_del", SGridConsts.COL_TITLE_IS_DEL + " tipo gasto"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.HRSU_TP_EXP);
        moSuscriptionsSet.add(SModConsts.FIN_ACC);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.mod.SModConsts;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRow;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Edwin Carmona
 */
public class SViewImportFile extends SGridPaneView {

    private SGridFilterDatePeriod moFilterDatePeriod;
    
    public SViewImportFile(SGuiClient client, int gridSubtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.FIN_LAY_BANK_DEP, gridSubtype, title);
        setRowButtonsEnabled(true, false, true, false, true);
        initComponentsCustom();
    }
    
    private void initComponentsCustom() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
    
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
    }
    
    @Override
    public void actionRowEdit(boolean showSystemRegistries) {
        if (jbRowEdit.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                SGuiParams params = null;

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else if (!showSystemRegistries && gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else if (moPaneSettings.isUserInsertApplying() && mnUserLevelAccess == SUtilConsts.LEV_AUTHOR && gridRow.getFkUserInsertId() != miClient.getSession().getUser().getPkUserId()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_DENIED_RIGHT);
                }
                else {
                    params = moFormParams != null ? moFormParams : new SGuiParams();
                    params.setKey(gridRow.getRowPrimaryKey());

                    miClient.getSession().getModule(mnModuleType, mnModuleSubtype).showForm(mnGridType, mnGridSubtype, params);
                    moFormParams = null;
                }
            }
        }
    }
    
    @Override
    public void actionRowDelete() {
        if (jbRowDelete.isEnabled()) {
            if (jtTable.getSelectedRowCount() == 0) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROWS);
            }
            else if (miClient.showMsgBoxConfirm(SGridConsts.MSG_CONFIRM_REG_DEL) == JOptionPane.YES_OPTION) {
                SGridRow[] gridRows = getSelectedGridRows();

                for (SGridRow gridRow : gridRows) {
                    if (((SGridRowView) gridRow).getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else if (((SGridRowView) gridRow).isRowSystem()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                    }
                    else if (!((SGridRowView) gridRow).isDeletable()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_DELETABLE);
                    }
                    else {
                        if (miClient.getSession().getModule(mnModuleType, mnModuleSubtype).deleteRegistry(mnGridType, gridRow.getRowPrimaryKey()) == SDbConsts.SAVE_OK) {
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
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
            sql += (sql.isEmpty() ? "" : "AND ") + "lbd.b_del = 0 ";
        }
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("lbd.dep_dt", (SGuiDate) filter);
        
        msSql = "SELECT "
                + "lbd.id_lay_bank_dep AS " + SDbConsts.FIELD_ID + "1, "
                + "lbda.id_usr_ana AS " + SDbConsts.FIELD_ID + "2, "
                + "' ' AS " + SDbConsts.FIELD_CODE + ", "
                + "lbd.src_name AS " + SDbConsts.FIELD_NAME + ", "
                + "lbd.dep_dt, lbd.src_name, ba.bank_acc, c.cur_key, lbd.src_movs, lbd.src_amt, u.usr, lbda.src_movs, lbda.src_amt, " 
                + "lbda.imp_movs, lbda.imp_amt, lbda.b_del, "
                + "lbd.fk_usr_ins, ui.usr AS f_usr_ins, lbd.fk_usr_ins, lbd.ts_usr_ins, lbd.fk_usr_upd, uu.usr AS f_usr_upd, lbd.fk_usr_upd, lbd.ts_usr_upd "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_LAY_BANK_DEP) + " AS lbd "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_LAY_BANK_DEP_ANA) + " AS lbda ON (lbd.id_lay_bank_dep = lbda.id_lay_bank_dep) "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ACC_CASH) + " AS ac ON (lbd.fk_bank_cob = ac.id_cob AND lbd.fk_bank_acc_cash = ac.id_acc_cash) " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BANK_ACC) + " AS ba ON (ac.fid_bpb_n = ba.id_bpb AND ac.fid_bank_acc_n = ba.id_bank_acc) "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS u ON (lbda.id_usr_ana = u.id_usr) "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON (lbda.fk_usr_ins = ui.id_usr) "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON (lbda.fk_usr_upd = uu.id_usr) "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON (lbd.fk_cur = c.id_cur) "
                + "WHERE NOT lbd.b_del AND NOT lbda.b_del AND NOT ba.b_del;";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();
        
	gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, SDbConsts.FIELD_ID + "1", "Folio", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "lbd.dep_dt", "Fecha importación", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "lbd.src_name", "Nombre archivo", 200));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "ba.bank_acc", "Cuenta bancaria", 250));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "c.cur_key", "Moneda", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "lbd.src_movs", "Pagos archivo", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "lbd.src_amt", "Monto archivo $", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "u.usr", "Analista", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "lbda.src_movs", "Pagos analista", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "lbda.src_amt", "Monto analista $", 100));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_1B, "lbda.imp_movs", "Pagos importados", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "lbda.imp_amt", "Monto importado $", 100));
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
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.trn.db.SDbScaleTicket;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRow;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Isabel Servín
 */
public class SViewScaleTicket extends SGridPaneView {

    private SGridFilterDatePeriod moFilterDatePeriod;
    private int levelRight;
    
    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     * @param subType
     */
    public SViewScaleTicket(SGuiClient client, String title, int subType) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNU_SCA_TIC, subType, title, null);
        initComponents();
    }
    
    private void initComponents() {
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);        
        
        levelRight = ((SClientInterface)miClient).getSessionXXX().getUser().hasRight((SClientInterface)miClient, SDataConstantsSys.PRV_SAL_SCA_TIC).Level;
    
        jbRowNew.setEnabled(levelRight > SUtilConsts.LEV_READ);
        jbRowCopy.setEnabled(levelRight > SUtilConsts.LEV_READ);
        jbRowDisable.setEnabled(false);
    }

    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        moPaneSettings.setUserApplying(false);
        moPaneSettings.setUpdatableApplying(false);
        moPaneSettings.setSystemApplying(true);
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        where += SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
        
        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        msSql = "SELECT " 
                + "v.id_sca_tic AS " + SDbConsts.FIELD_ID + "1, "
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.plate, " 
                + "v.plate_cage, " 
                + "v.driver, " 
                + "v.ts_arr, " 
                + "v.ts_dep, " 
                + "v.wei_arr, " 
                + "v.wei_dep, " 
                + "v.wei_net_r, "
                + "i.item, "
                + "b.bp, "
                + "s.sca_key, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRNU_SCA_TIC) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_SCA) + " AS s ON "
                + "v.fk_sca = s.id_sca "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON "
                + "v.fk_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON "
                + "v.fk_bp = b.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "WHERE fk_ct_iog = " + mnGridSubtype + " " 
                + "AND fk_co = " + miClient.getSession().getConfigCompany().getCompanyId() + " "
                + (where.isEmpty() ? "" : "AND " + where) + " "
                + "ORDER BY s.sca_key, v.num, v.id_sca_tic, b.bp";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "sca_key", "Báscula"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_8B, SDbConsts.FIELD_NAME, "Boleto"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "b.bp", "Asociado negocios"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.item", "Ítem"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "plate", "Placa"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "plate_cage", "Placa remolque"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "driver", "Chofer"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "wei_arr", "Peso entrada(kg)"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "wei_dep", "Peso salida(kg)"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "wei_net_r", "Peso neto(kg)"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_arr", "Fecha-hora entrada"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_dep", "Fecha-hora salida"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.CFGU_SCA);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }
    
    @Override
    public void actionRowDelete() {
        if (jbRowDelete.isEnabled()) {
            if (jtTable.getSelectedRowCount() == 0) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROWS);
            }
            else if (miClient.showMsgBoxConfirm(SGridConsts.MSG_CONFIRM_REG_DEL) == JOptionPane.YES_OPTION) {
                boolean updates = false;
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
                    else if (levelRight < SUtilConsts.LEV_EDITOR) {
                        miClient.showMsgBoxWarning("El boleto no se puede eliminar debido a que no tienes los permisos suficientes.");
                    }
                    else {
                        try {
                            SDbScaleTicket tic = new SDbScaleTicket();
                            tic.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                            if (tic.canDelete(miClient.getSession())) {
                                tic.delete(miClient.getSession());
                                updates = true;
                            }
                            else {
                                throw new Exception("El boleto no se puede elminar debido a que ya esta asociado a un documento.");
                            }
                        }
                        catch (Exception e) {
                            SLibUtils.showException(this, e);
                        }
                    }
                }

                if (updates) {
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.view;

import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbDepartmentCostCenter;
import java.util.ArrayList;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Claudio Peña, Sergio Flores
 */
public class SViewDepartmentCostCenter extends SGridPaneView {

    public SViewDepartmentCostCenter(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.HRS_DEP_CC, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(true, true, true, false, true);
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
            sql += (sql.isEmpty() ? "" : "AND ") + "dep.b_del = 0 ";
        }

        msSql = "SELECT "
                + "dep.id_dep AS " + SDbConsts.FIELD_ID + "1, "
                + "dep.name AS " + SDbConsts.FIELD_NAME + ", "
                + "dep.code aS " + SDbConsts.FIELD_CODE + ", "
                + "dep.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "dep.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "fic.id_cc, "
                + "fic.cc, "
                + "fic.pk_cc, "
                + "cc.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "cc.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "cc.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "cc.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM erp.hrsu_dep AS dep " 
                + "LEFT OUTER JOIN hrs_dep_cc AS cc  ON cc.id_dep = dep.id_dep " 
                + "LEFT OUTER JOIN fin_cc AS fic ON fic.pk_cc = cc.fk_cc " 
                + "LEFT OUTER JOIN erp.usru_usr AS ui ON cc.fk_usr_ins = ui.id_usr " 
                + "LEFT OUTER JOIN erp.usru_usr AS uu ON cc.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY dep.name, dep.id_dep; ";
        }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, SDbConsts.FIELD_NAME, "Nombre departamento"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, "Código departamento"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "fic.id_cc", "No. centro costo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "fic.cc", "entro costo"));
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
        moSuscriptionsSet.add(SModConsts.HRSU_DEP);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionRowCopy() {
        if (jbRowCopy.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                SGuiParams params = null;

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    SDbDepartmentCostCenter dcc = (SDbDepartmentCostCenter) miClient.getSession().readRegistry(SModConsts.HRS_DEP_CC, gridRow.getRowPrimaryKey(), SDbConsts.MODE_STEALTH);
                    
                    if (dcc.isRegistryNew()) {
                        miClient.showMsgBoxInformation("No se puede copiar el registro porque no existe aún.");
                    }
                    else {
                        params = new SGuiParams(getSelectedGridRow().getRowPrimaryKey(), true);
                        miClient.getSession().getModule(mnModuleType, mnModuleSubtype).showForm(mnGridType, mnGridSubtype, params);
                    }
                }
            }
        }
    }
    
    @Override
    public void actionRowEdit() {
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
                else if (gridRow.isRowSystem()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                }
                else if (!gridRow.isUpdatable()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                }
                else if (moPaneSettings.isUserInsertApplying() && mnUserLevelAccess == SUtilConsts.LEV_AUTHOR && gridRow.getFkUserInsertId() != miClient.getSession().getUser().getPkUserId()) {
                    miClient.showMsgBoxWarning(SDbConsts.MSG_REG_DENIED_RIGHT);
                }
                else {
                    try {
                        SDbDepartmentCostCenter dcc = (SDbDepartmentCostCenter) miClient.getSession().readRegistry(SModConsts.HRS_DEP_CC, gridRow.getRowPrimaryKey(), SDbConsts.MODE_STEALTH);
                        
                        if (dcc.isRegistryNew()) {
                            dcc.setPkDepartmentId(gridRow.getRowPrimaryKey()[0]);
                            dcc.setFkCostCenterId(1);
                            dcc.save(miClient.getSession());
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                    
                    params = moFormParams != null ? moFormParams : new SGuiParams();
                    params.setKey(gridRow.getRowPrimaryKey());

                    miClient.getSession().getModule(mnModuleType, mnModuleSubtype).showForm(mnGridType, mnGridSubtype, params);
                    moFormParams = null;
                }
            }
        }
    }
}

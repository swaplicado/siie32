/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Isabel Servín
 */
public class SViewConfUserVsEntity extends SGridPaneView implements ActionListener {
    
    private JButton jbConfig;

    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     */
    public SViewConfUserVsEntity(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_CONF_USR_VS_ENT, SLibConsts.UNDEFINED, title, null);
        setRowButtonsEnabled(false);
        initComponents();
    }
    
    private void initComponents() {
        jbConfig = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_link.gif")), "Configurar", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbConfig);
    }
    
    private void actionConfig() {
        super.actionRowEdit();
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
            SGuiParams params;

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
                params = moFormParams != null ? moFormParams : new SGuiParams();
                params.setKey(gridRow.getRowPrimaryKey());

                miClient.getSession().getModule(mnModuleType, mnModuleSubtype).showForm(mnGridType, mnGridSubtype, params);
                moFormParams = null;
            }
        }
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(1);

        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }
        
        where += (where.isEmpty() ? "" : "AND ") + "(pu.id_prv = " + SDataConstantsSys.PRV_INV_REQ_MAT_REQ + " "
                + "OR pu.id_prv = " + SDataConstantsSys.PRV_INV_REQ_MAT_REV + " OR pu.id_prv = " + SDataConstantsSys.PRV_INV_REQ_MAT_PROV + " "
                + "OR pu.id_prv = " + SDataConstantsSys.PRV_INV_REQ_MAT_PUR + ") ";

        msSql = "SELECT DISTINCT v.id_usr AS " + SDbConsts.FIELD_ID + "1, "
                + "v.usr AS " + SDbConsts.FIELD_CODE + ", "
                + "v.usr AS " + SDbConsts.FIELD_NAME + ", "
                + "IF(ceu.id_ref IS NULL, 0, 1) AS conf_cons, "
                + "IF(peu.id_usr IS NULL, 0, 1) AS conf_prov, "
                + "IF(csu.id_ref IS NULL, 0, 1) AS conf_sub, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "ceu.fk_usr AS " + SDbConsts.FIELD_USER_INS_ID + "_cons, "
                + "peu.fk_usr AS " + SDbConsts.FIELD_USER_INS_ID + "_prov, "
                + "csu.fk_usr AS " + SDbConsts.FIELD_USER_INS_ID + "_sub, "
                + "v.ts_edit AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "uic.usr AS " + SDbConsts.FIELD_USER_INS_NAME + "_cons, "
                + "uip.usr AS " + SDbConsts.FIELD_USER_INS_NAME + "_prov, "
                + "uis.usr AS " + SDbConsts.FIELD_USER_INS_NAME + "_sub "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS v "
                + "INNER JOIN erp.usru_prv_usr AS pu ON "
                + "v.id_usr = pu.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_USR) + " AS ceu ON "
                + "ceu.id_link = " + SModSysConsts.USRS_LINK_USR + " AND v.id_usr = ceu.id_ref "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT_USR) + " AS csu ON "
                + "csu.id_link = " + SModSysConsts.USRS_LINK_USR + " AND v.id_usr = csu.id_ref "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT_USR) + " AS peu ON "
                + "v.id_usr = peu.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uic ON "
                + "ceu.fk_usr = uic.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uis ON "
                + "csu.fk_usr = uis.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uip ON "
                + "peu.fk_usr = uip.id_usr "
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "ORDER BY v.usr, v.id_usr "; 
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_NAME, "Usuario"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "conf_cons", "Configurado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME + "_cons", SGridConsts.COL_TITLE_USER_INS_NAME));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_PROV_ENT_USR);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_ENT_USR);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbConfig) {
                actionConfig();
            }
        }
    }
}

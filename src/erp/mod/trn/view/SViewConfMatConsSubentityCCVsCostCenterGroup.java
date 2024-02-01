/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.mod.SModConsts;
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
public class SViewConfMatConsSubentityCCVsCostCenterGroup extends SGridPaneView implements ActionListener {

    private JButton jbConfig;
    
    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     */
    public SViewConfMatConsSubentityCCVsCostCenterGroup(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_CONF_SUBENT_VS_CC_GRP, SLibConsts.UNDEFINED, title, null);
        setRowButtonsEnabled(false);
        initComponents();
    }
    
    private void initComponents() {
        jbConfig = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_link.gif")), "Configurar", this);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbConfig);
    }
    
    private void actionConfig() {
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
        
        moPaneSettings = new SGridPaneSettings(3);

        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "v.b_del = 0 AND c.b_del = 0 AND cc.b_del = 0 ";
        }

        msSql = "SELECT DISTINCT v.id_mat_cons_ent AS " + SDbConsts.FIELD_ID + "1, "
                + "v.id_mat_cons_subent AS " + SDbConsts.FIELD_ID + "2, "
                + "cc.pk_cc AS " + SDbConsts.FIELD_ID + "3, "
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "c.code AS entidad_c, "
                + "c.name AS entidad, "
                + "cc.id_cc, "
                + "cc.cc, "
                + "IF(g.id_mat_cc_grp IS NULL, FALSE, TRUE) AS conf, "
                + "g.fk_usr AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT) + " AS c ON "
                + "v.id_mat_cons_ent = c.id_mat_cons_ent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT_CC) + " AS sc ON "
                + "v.id_mat_cons_ent = sc.id_mat_cons_ent AND v.id_mat_cons_subent = sc.id_mat_cons_subent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS cc ON " 
                + "sc.id_cc = cc.pk_cc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT_CC_CC_GRP) + " AS g ON " 
                + "v.id_mat_cons_ent = g.id_mat_cons_ent AND v.id_mat_cons_subent = g.id_mat_cons_subent AND sc.id_cc = g.id_cc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "g.fk_usr = ui.id_usr "
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "ORDER BY c.name, v.name, v.id_mat_cons_subent "; 
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, "Código"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, "Subcentro de consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "entidad_c", "Código centro de consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "entidad", "Centro de consumo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "id_cc", "Número centro de costo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, "cc", "Centro de costo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_M, "conf", "Configurado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_ENT);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_SUBENT);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_SUBENT_CC);
        moSuscriptionsSet.add(SModConsts.FIN_CC);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_SUBENT_CC_CC_GRP);
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

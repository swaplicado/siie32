/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.mod.SModConsts;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Isabel Servín
 */
public class SViewConfMatCostCenterGroupItem extends SGridPaneView implements ActionListener {

    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     */
    public SViewConfMatCostCenterGroupItem(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_CONF_CC_GRP_VS_ITM, SLibConsts.UNDEFINED, title, null);
        initComponents();
    }
    
    private void initComponents() {
        setRowButtonsEnabled(false, false, true, false, false);
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(1);

        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(false);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "v.b_del = 0  ";
        }

        msSql = "SELECT DISTINCT v.id_mat_cc_grp AS " + SDbConsts.FIELD_ID + "1, "
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "IF(gi.id_link IS NULL, FALSE, TRUE) AS conf, "
                + "gi.fk_usr AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "gi.ts_usr AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CC_GRP) + " AS v "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CC_GRP_ITEM) + " AS gi ON "
                + "v.id_mat_cc_grp = gi.id_mat_cc_grp "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "gi.fk_usr = ui.id_usr "
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "ORDER BY v.name, v.id_mat_cc_grp "; 
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, "Código"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, "Grupo de centro de costo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "conf", "Configurado"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CC_GRP);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CC_GRP_ITEM);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

        }
    }
}

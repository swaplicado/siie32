/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
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
public class SViewConfMatCostCenterGroupItemDetail extends SGridPaneView implements ActionListener {

    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     */
    public SViewConfMatCostCenterGroupItemDetail(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_DET_CC_GRP_VS_ITM, SLibConsts.UNDEFINED, title, null);
        initComponents();
    }
    
    private void initComponents() {
        setRowButtonsEnabled(false);
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(1);

        moPaneSettings.setDeletedApplying(false);
        moPaneSettings.setSystemApplying(false);
        moPaneSettings.setUserInsertApplying(false);
        moPaneSettings.setUserUpdateApplying(false);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "v.b_del = 0  ";
        }

        msSql = "SELECT v.id_mat_cc_grp AS " + SDbConsts.FIELD_ID + "1, "
                + "v.code AS " + SDbConsts.FIELD_CODE + ", "
                + "v.name AS " + SDbConsts.FIELD_NAME + ", "
                + "l.name AS link, "
                + "COALESCE(igen.igen, i.item) AS item "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CC_GRP) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CC_GRP_ITEM) + " AS gi ON "
                + "v.id_mat_cc_grp = gi.id_mat_cc_grp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMS_LINK) + " AS l ON " 
                + "gi.id_link = l.id_link "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS igen ON "
                + "gi.id_link = " + SModSysConsts.ITMS_LINK_IGEN + " AND gi.id_ref = igen.id_igen "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON "
                + "gi.id_link = " + SModSysConsts.ITMS_LINK_ITEM + " AND gi.id_ref = i.id_item "
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "ORDER BY v.name, v.id_mat_cc_grp "; 
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, SDbConsts.FIELD_CODE, "Código"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, "Grupo de centro de costo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "link", "Tipo referencia"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "item", "Referencia"));
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CC_GRP);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CC_GRP_ITEM);
        moSuscriptionsSet.add(SModConsts.ITMU_IGEN);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

        }
    }
}

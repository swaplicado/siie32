/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mqlt.view;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.mod.SModConsts;
import erp.mtrn.form.SDialogAnalysisItemKardex;
import java.awt.Dimension;
import javax.swing.JButton;

/**
 *
 * @author Edwin Carmona
 */
public class SViewItemAnalysis extends erp.lib.table.STableTab implements java.awt.event.ActionListener {
    
    private JButton jbAnalysisKardex;
    private SDialogAnalysisItemKardex moDialogStockCardexHistoric;

    public SViewItemAnalysis(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.QLTX_ITEM_ANALYSIS);
        initComponents();
    }

    private void initComponents() {
        
        addTaskBarUpperSeparator();
        
        jbAnalysisKardex = new JButton(miClient.getImageIcon(SLibConstants.ICON_KARDEX));
        jbAnalysisKardex.setPreferredSize(new Dimension(23, 23));
        jbAnalysisKardex.setToolTipText("Ver análisis configurados");
        jbAnalysisKardex.addActionListener(this);
        addTaskBarUpperComponent(jbAnalysisKardex);
        
        moDialogStockCardexHistoric = new SDialogAnalysisItemKardex(miClient);
        
        erp.lib.table.STableField[] aoKeyFields = new STableField[1];

        int i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "v.id_item");
        
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }
        
        erp.lib.table.STableColumn[] aoTableColumns = new STableColumn[3];

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_code", "Código", STableConstants.WIDTH_ITEM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_name", "Ítem", STableConstants.WIDTH_ITEM_3X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }
    
    public void actionViewAnalysisKardex() {
        if (jbAnalysisKardex.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                int[] rowKey = (int[]) moTablePane.getSelectedTableRow().getPrimaryKey();
                int itemId = rowKey[0];

                moDialogStockCardexHistoric.formReset();
                moDialogStockCardexHistoric.setFormParams(itemId);
                moDialogStockCardexHistoric.setVisible(true);
            }
        }
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_QLT).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_QLT).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_QLT ).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_QLT).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }

    @Override
    public void createSqlQuery() {
        java.lang.String sqlWhere = "";
        
        msSql = "SELECT  "
                + "    v.id_item, "
                + "    v.item, "
                + "    v.item_key, "
                + "    v.item_short, "
                + "    v.item_name, "
                + "    v.item_code, "
                + "    v.name_short, "
                + "    v.b_del, "
                + "    v.fid_usr_new, "
                + "    v.fid_usr_edit, "
                + "    v.fid_usr_del, "
                + "    v.ts_new, "
                + "    v.ts_edit, "
                + "    v.ts_del, "
                + "    v.usr_ins, "
                + "    v.usr_upd, "
                + "    v.usr_del "
                + " FROM "
                + "    (SELECT DISTINCT "
                + "        i.id_item, "
                + "            i.item, "
                + "            i.item_key, "
                + "            i.item_short, "
                + "            i.name AS item_name, "
                + "            i.code AS item_code, "
                + "            i.name_short, "
                + "            qai.b_del, "
                + "            qai.fid_usr_new, "
                + "            qai.fid_usr_edit, "
                + "            qai.fid_usr_del, "
                + "            qai.ts_new, "
                + "            qai.ts_edit, "
                + "            qai.ts_del, "
                + "            un.usr AS usr_ins, "
                + "            ue.usr AS usr_upd, "
                + "            ud.usr AS usr_del "
                + "    FROM " + SModConsts.TablesMap.get(SModConsts.QLT_ANALYSIS_ITEM) + " AS qai "
                + "    INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON qai.id_item = i.id_item "
                + "    INNER JOIN " + SModConsts.TablesMap.get(SModConsts.QLT_ANALYSIS) + " AS a ON qai.id_analysis = a.id_analysis "
                + "    INNER JOIN " + SModConsts.TablesMap.get(SModConsts.QLT_TP_ANALYSIS) + " AS ta ON a.fk_tp_analysis_id = ta.id_analysis_type "
                + "    INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS un ON qai.fid_usr_new = un.id_usr "
                + "    INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ue ON qai.fid_usr_edit = ue.id_usr "
                + "    INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ud ON qai.fid_usr_del = ud.id_usr "
                + "    WHERE "
                + " qai.b_del = FALSE AND i.b_del = FALSE) AS v "
                + (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere);
    }
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();
            if (button == jbAnalysisKardex) {
                actionViewAnalysisKardex();
            }
        }
    }
}
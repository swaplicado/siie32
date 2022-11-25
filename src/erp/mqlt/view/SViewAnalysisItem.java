/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mqlt.view;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.mod.SModConsts;

/**
 *
 * @author Edwin Carmona
 */
public class SViewAnalysisItem extends erp.lib.table.STableTab {
    
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewAnalysisItem(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.QLT_ANALYSIS_ITEM);
        initComponents();
    }

    private void initComponents() {
        moTabFilterDeleted = new STabFilterDeleted(this);
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        
        erp.lib.table.STableField[] aoKeyFields = new STableField[2];

        int i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "v.id_analysis");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "v.id_item");
        
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }
        
        erp.lib.table.STableColumn[] aoTableColumns = new STableColumn[12];

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_code", "Código", STableConstants.WIDTH_ITEM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_name", "Ítem", STableConstants.WIDTH_ITEM_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "analysis_name", "Análisis", STableConstants.WIDTH_ITEM_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "type_name", "Tipo análisis", STableConstants.WIDTH_ITEM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "unit_symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "unit_name", "Nombre unidad", STableConstants.WIDTH_ITEM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_min", "Aplica mín", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "min_value", "Mínimo", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_max", "Aplica máx", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "max_value", "Máximo", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_required", "Requerido", STableConstants.WIDTH_BOOLEAN);
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        jbNew.setEnabled(true);
        jbEdit.setEnabled(true);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
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
        erp.lib.table.STableSetting setting = null;
        
        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "v.b_del = FALSE ";
            }
        }

        msSql = "SELECT  "
                + "    v.id_analysis, "
                + "    v.id_item, "
                + "    a.analysis_short_name, "
                + "    a.analysis_name, "
                + "    a.unit_symbol, "
                + "    a.unit_name, "
                + "    v.sort_pos, "
                + "    v.min_value, "
                + "    v.max_value, "
                + "    v.b_min, "
                + "    v.b_max, "
                + "    v.b_required, "
                + "    CONCAT(ta.type_code, '-', ta.type_name) AS analysis_type, "
                + "    i.id_item, "
                + "    i.item, "
                + "    i.item_key, "
                + "    i.item_short, "
                + "    i.name AS item_name, "
                + "    i.code AS item_code, "
                + "    i.name_short, "
                + "    ta.type_name, "
                + "    v.b_del, "
                + "    v.fid_usr_new, "
                + "    v.fid_usr_edit, "
                + "    v.fid_usr_del, "
                + "    v.ts_new, "
                + "    v.ts_edit, "
                + "    v.ts_del, "
                + "    un.usr AS usr_ins, "
                + "    ue.usr AS usr_upd, "
                + "    ud.usr AS usr_del "
                + " FROM "
                + "    " + SModConsts.TablesMap.get(SModConsts.QLT_ANALYSIS_ITEM) + " AS v "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON v.id_item = i.id_item "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.QLT_ANALYSIS) + " AS a ON v.id_analysis = a.id_analysis "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.QLT_TP_ANALYSIS) + " AS ta ON a.fk_tp_analysis_id = ta.id_analysis_type "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS un ON v.fid_usr_new = un.id_usr "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ue ON v.fid_usr_edit = ue.id_usr "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ud ON v.fid_usr_del = ud.id_usr "
                + (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere);
    }
}
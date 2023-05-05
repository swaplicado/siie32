/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mqlt.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;

/**
 *
 * @author Edwin Carmona
 */
public class SViewDpsEntryAnalysisQlt extends erp.lib.table.STableTab {
    
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewDpsEntryAnalysisQlt(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.QLTX_DPS_ETY_ANALYSIS);
        initComponents();
    }

    private void initComponents() {
        moTabFilterDeleted = new STabFilterDeleted(this);
        
        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        
        addTaskBarUpperComponent(moTabFilterDeleted);
        
        erp.lib.table.STableColumn[] aoTableColumns = new STableColumn[28];

        int i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo Doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num", "Número", STableConstants.WIDTH_ITEM_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ety.concept_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ety.concept", "Concepto", STableConstants.WIDTH_ITEM_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "ety.orig_qty", "Cantidad", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.unit", "Nombre unidad", STableConstants.WIDTH_ITEM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "qta.name", "Tipo análisis", STableConstants.WIDTH_ITEM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "qa.analysis_name", "Análisis", STableConstants.WIDTH_ITEM_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "qa.unit_symbol", "Un. ana.", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "qa.unit_name", "Nom. un. ana.", STableConstants.WIDTH_ITEM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "v.b_min", "Aplica mín", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ai.min_value", "Mínimo normativo", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "v.min_value", "Mínimo", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "v.b_max", "Aplica máx", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ai.max_value", "Máximo normativo", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "v.max_value", "Máximo", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "v.b_required", "Requerido", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "v.b_lim_mod", "Param mod", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "v.b_req_mod", "Req. mod", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "v.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "usr_ins", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "v.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "usr_upd", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "v.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "usr_del", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "v.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        
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
        
    }

    @Override
    public void actionEdit() {
        
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

        msSql = "SELECT "
                + "    d.id_year, "
                + "    d.id_doc, "
                + "    d.dt, "
                + "    d.dt_doc, "
                + "    d.num, "
                + "    d.num_ser, "
                + "    d.num_ref, "
                + "    dt.code, "
                + "    ety.concept_key, "
                + "    ety.concept, "
                + "    ety.orig_qty, "
                + "    u.unit, "
                + "    u.symbol, "
                + "    v.b_min, "
                + "    v.b_max, "
                + "    v.b_required, "
                + "    v.b_max, "
                + "    ai.min_value, "
                + "    v.min_value, "
                + "    ai.max_value, "
                + "    v.max_value, "
                + "    v.sort_pos, "
                + "    v.b_lim_mod, "
                + "    v.b_req_mod, "
                + "    v.b_del, "
                + "    v.ts_new, "
                + "    v.ts_edit, "
                + "    v.ts_del, "
                + "    qa.analysis_name, "
                + "    qa.unit_symbol, "
                + "    qa.unit_name, "
                + "    qta.name, "
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
                + "FROM "
                + "    " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS) + " d "
                + "        INNER JOIN "
                + "    " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_ETY) + " ety ON d.id_year = ety.id_year "
                + "        AND d.id_doc = ety.id_doc "
                + "        INNER JOIN "
                + "    " + SDataConstants.TablesMap.get(SDataConstants.ITMU_UNIT) + " u ON ety.fid_unit = u.id_unit "
                + "        INNER JOIN "
                + "    " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_ETY_ANALYSIS) + " v ON d.id_year = v.fid_dps_year_n "
                + "        AND d.id_doc = v.fid_dps_doc_n "
                + "        AND ety.id_ety = v.fid_dps_ety_n "
                + "         INNER JOIN " 
                + "    " + SDataConstants.TablesMap.get(SDataConstants.TRNU_TP_DPS) + " AS dt ON d.fid_ct_dps = dt.id_ct_dps "
                + "         AND d.fid_cl_dps = dt.id_cl_dps "
                + "         AND d.fid_tp_dps = dt.id_tp_dps "
                + "         AND d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_EST[0] + " "
                + "         AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1] + " "
                + "         AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CON[2] + " "
                + "        INNER JOIN "
                + "    " + SDataConstants.TablesMap.get(SDataConstants.QLT_ANALYSIS_ITEM) + " ai ON v.fid_analysis_id = ai.id_analysis "
                + "        AND v.fid_item_id = ai.id_item "
                + "        INNER JOIN "
                + "    " + SDataConstants.TablesMap.get(SDataConstants.QLT_ANALYSIS) + " qa ON v.fid_analysis_id = qa.id_analysis "
                + "        INNER JOIN "
                + "    " + SDataConstants.TablesMap.get(SDataConstants.QLT_TP_ANALYSIS) + " qta ON qa.fk_tp_analysis_id = qta.id_analysis_type "
                + "        INNER JOIN "
                + "    " + SDataConstants.TablesMap.get(SDataConstants.USRU_USR) + " AS un ON v.fid_usr_new = un.id_usr "
                + "        INNER JOIN "
                + "    " + SDataConstants.TablesMap.get(SDataConstants.USRU_USR) + " AS ue ON v.fid_usr_edit = ue.id_usr "
                + "        INNER JOIN "
                + "    " + SDataConstants.TablesMap.get(SDataConstants.USRU_USR) + " AS ud ON v.fid_usr_del = ud.id_usr "
                + (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere);
    }
}
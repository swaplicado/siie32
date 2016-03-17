/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableSetting;
import erp.mitm.form.SPanelFilterItem;

/**
 *
 * @author Juan Barajas
 */
public class SViewBomItems extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STableColumn[] aoTableColumns;
    private erp.mitm.form.SPanelFilterItem moPanelFilterÍtem;


    public SViewBomItems(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.MFGX_BOM_ITEMS);
        initComponents();
    }

    private void initComponents() {
        aoTableColumns = null;

        moPanelFilterÍtem = new SPanelFilterItem(miClient, this);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moPanelFilterÍtem);

        renderTableColumns();
        setIsSummaryApplying(true);

        populateTable();
    }

    private void renderTableColumns() {
        int i = 0;

        moTablePane.reset();

        aoTableColumns = new STableColumn[6];

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal empresa", 150);

        if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "it.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "it.item", "Producto", 300);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "it.item", "Producto", 300);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "it.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
        }

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bom.bom", "Referencia", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bom.qty", "Cantidad insumo", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "unit", "Unidad insumo", STableConstants.WIDTH_UNIT_SYMBOL);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }
        moTablePane.createTable(this);
    }

    @Override
    public void createSqlQuery() {
        String idItem = "";
        STableSetting setting = null;


        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_ITM_ITEM) {
                if (((int[]) setting.getSetting())[0] > 0) {
                    idItem = "" + ((int[]) setting.getSetting())[0] + " ";
                }
            }
        }

        if (idItem.isEmpty()) {
            idItem = "0";
        }

        msSql = "SELECT bpb.bpb, it.item_key, it.item, bom.bom, bom.qty, (SELECT u.symbol FROM erp.itmu_item AS it " +
                "INNER JOIN erp.itmu_unit AS u ON it.fid_unit = u.id_unit " +
                "WHERE it.id_item = " +  idItem + ") AS unit " +
                "FROM mfg_bom AS bom " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON bom.fid_cob = bpb.id_bpb " +
                "INNER JOIN erp.itmu_item AS it ON bom.root= it.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON it.fid_unit = u.id_unit " +
                "WHERE bom.id_bom IN (SELECT id_bom " +
                "FROM mfg_bom WHERE fid_item = " + idItem + " AND b_del = 0) " +
                "AND bom.b_del = 0 " +
                "GROUP BY bpb.bpb, it.item_key, it.item, bom.bom, bom.qty " +
                "ORDER BY bpb.bpb, " + (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                "it.item_key, it.item, " : "it.item, it.item_key, ") + "bom.bom, bom.qty ";
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {

        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {

        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }
}

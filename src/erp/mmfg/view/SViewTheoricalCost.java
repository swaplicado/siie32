package erp.mmfg.view;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author César Orozco
 */
public class SViewTheoricalCost extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    public SViewTheoricalCost(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.MFGX_BOM_COST);
        initComponents();
    }
    private void initComponents() {
        int i;

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        erp.lib.table.STableField[] aoKeyFields = new STableField[1];
        erp.lib.table.STableColumn[] aoTableColumns = new STableColumn[5];
        
        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "b.id_bom");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }
        
         i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal empresa", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Producto", 250);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bom", "Referencia", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "cost_prod", "Costo teórico fórmula $", STableConstants.WIDTH_QUANTITY);
         for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }
         mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }
    @Override
    public void createSqlQuery() {
        msSql = "SELECT b.id_bom, b.lev, b.bom, b.per, b.root, b.fid_item, b.fid_item_n, i.item_key, i.item, i.prod_cost, SUM(prod_cost * b.per) as cost_prod, bpb.bpb "+
                "FROM mfg_bom as b " +
                "INNER JOIN erp.itmu_item AS i ON b.fid_item = i.id_item " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON b.fid_cob = bpb.id_bpb " +
                "WHERE b.b_del =FALSE " +
                "GROUP BY root " +
                "ORDER BY lev ";
    }

    @Override
    public void actionNew() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionDelete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

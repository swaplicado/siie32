/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mitm.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;

/**
 *
 * @author Isabel Servín
 */
public class SViewScaleItemDetail extends erp.lib.table.STableTab {

    public SViewScaleItemDetail(SClientInterface client, String tabTitle) {
        super(client, tabTitle, SDataConstants.ITMX_SCA_ITEM_DET);
        initComponents();
    }
    
    private void initComponents() {
        int i;
        
        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[7];

        i = 0;

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sca", "Báscula", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sca_key", "Clave báscula", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_sca", "Producto báscula", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_siie", "Ítem", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_def", "Predeterminado", STableConstants.WIDTH_BOOLEAN_3X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ts", "Creación", STableConstants.WIDTH_DATE_TIME);
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }
        
        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);
        
        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.ITMU_SCA_ITEM);
        mvSuscriptors.add(SDataConstants.ITMU_SCA_ITEM_MAP);
        mvSuscriptors.add(SDataConstants.CFGU_SCA);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void createSqlQuery() {
        msSql = "SELECT cs.sca_key, "
                + "cs.sca, "
                + "si.name AS item_sca, "
                + "i.item AS item_siie, "
                + "b_def, "
                + "u.usr, "
                + "ts "
                + "FROM erp.itmu_sca_item AS si " 
                + "INNER JOIN erp.itmu_sca_item_map AS sim ON " 
                + "si.id_sca = sim.id_sca AND si.id_sca_item = sim.id_sca_item " 
                + "INNER JOIN erp.cfgu_sca AS cs ON " 
                + "si.id_sca = cs.id_sca " 
                + "INNER JOIN erp.itmu_item AS i ON " 
                + "sim.id_item = i.id_item "
                + "INNER JOIN erp.usru_usr AS u ON " 
                + "sim.fid_usr = u.id_usr "
                + "ORDER BY cs.sca, cs.sca_key, si.name, i.item"; 
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

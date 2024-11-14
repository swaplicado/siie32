/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mbps.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.mcfg.data.SDataScaleUtils;

/**
 *
 * @author Isabel Servín
 */
public class SViewScaleBizPartner extends erp.lib.table.STableTab {

    public SViewScaleBizPartner(SClientInterface client, String tabTitle, int subType) {
        super(client, tabTitle, SDataConstants.BPSU_SCA_BP, subType);
        initComponents();
    }
    
    private void initComponents() {
        int i;
        
        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[9];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_sca");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sca", "Báscula", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sca_key", "Clave báscula", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }
        
        jbNew.setEnabled(false);
        jbEdit.setEnabled(true);
        jbDelete.setEnabled(false);
        
        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void createSqlQuery() {
        msSql = "SELECT " +
                "id_sca, " +
                "sca_key, " +
                "sca, " +
                "s.b_del, " +
                "un.usr, " +
                "s.ts_new, " +
                "ue.usr, " +
                "s.ts_edit, " +
                "ud.usr, " +
                "s.ts_del " +
                "FROM erp.cfgu_sca s " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "s.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "s.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "s.fid_usr_del = ud.id_usr " +
                "WHERE " + SDataScaleUtils.getSqlWhereScalesWithConnection();
    }

    @Override
    public void actionNew() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showForm(mnTabType, mnTabTypeAux01, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    @Override
    public void actionDelete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

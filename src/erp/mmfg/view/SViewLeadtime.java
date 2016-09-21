/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.view;

import javax.swing.JButton;

import erp.data.SDataConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.SLibConstants;

/**
 *
 * @author Néstor Ávalos
 */
public class SViewLeadtime extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewLeadtime(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.MFGX_LT, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        jbNew.setEnabled(true);
        jbEdit.setEnabled(true);
        //jbDelete.setEnabled(false);

        erp.lib.table.STableField[] aoKeyFields = null;
        erp.lib.table.STableColumn[] aoTableColumns = null;

        switch (mnTabTypeAux01) {
            case SDataConstants.TRN_SUP_LT_CO:
                aoKeyFields = new STableField[2];
                aoTableColumns = new STableColumn[8];

                i = 0;
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_sup");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_ety");
                for (i = 0; i < aoKeyFields.length; i++) {
                    moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
                }

                i = 0;
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Proveedor", 300);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "t.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "t.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "t.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "t.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
                break;
            case SDataConstants.TRN_SUP_LT_COB:
                aoKeyFields = new STableField[3];
                aoTableColumns = new STableColumn[9];

                i = 0;
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_cob");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_sup");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_ety");
                for (i = 0; i < aoKeyFields.length; i++) {
                    moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
                }

                i = 0;
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal empresa", 150);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Proveedor", 300);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "t.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "t.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "t.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "t.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
                break;
        }

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.BPSX_BP_SUP);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_MFG).showForm(mnTabType, mnTabTypeAux01, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_MFG ).showForm(mnTabType, mnTabTypeAux01, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
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
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "t.b_del = FALSE ";
            }
        }

        switch (mnTabTypeAux01) {
            case SDataConstants.TRN_SUP_LT_CO:
                msSql = "SELECT DISTINCT t.*, b.bp, un.usr, ue.usr, ud.usr " +
                    "FROM trn_sup_lt_co t " +
                    "INNER JOIN erp.bpsu_bp b ON t.id_sup = b.id_bp " +
                    "INNER JOIN erp.usru_usr AS un ON t.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON t.fid_usr_edit = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON t.fid_usr_del = ud.id_usr " +
                    (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                    "GROUP BY t.id_sup " +
                    "ORDER BY b.bp, t.id_ety ";
                break;
             case SDataConstants.TRN_SUP_LT_COB:
                 msSql = "SELECT DISTINCT t.*, b.bp, bpb.bpb, un.usr, ue.usr, ud.usr " +
                    "FROM trn_sup_lt_cob t " +
                    "INNER JOIN erp.bpsu_bp b ON t.id_sup = b.id_bp " +
                    "INNER JOIN erp.bpsu_bpb bpb ON t.id_cob = bpb.id_bpb " +
                    "INNER JOIN erp.usru_usr AS un ON t.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON t.fid_usr_edit = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON t.fid_usr_del = ud.id_usr " +
                    (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                    "GROUP BY t.id_cob, t.id_sup " +
                    "ORDER BY b.bp, t.id_ety ";
                 break;
        }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

        }
    }
}

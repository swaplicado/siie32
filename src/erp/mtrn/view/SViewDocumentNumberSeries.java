/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import javax.swing.JButton;

/**
 *
 * @author Alfonso Flores
 */
public class SViewDocumentNumberSeries extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewDocumentNumberSeries(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType) {
        super(client, tabTitle, auxType);
        mnTabTypeAux01 = auxType;
        initComponents();
    }

    private void initComponents() {
        int i;
        STableColumn[] aoTableColumns = null;

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        jbNew.setEnabled(true);
        jbEdit.setEnabled(true);
        jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[1];
        switch (mnTabTypeAux01) {
            case SDataConstants.TRN_DNS_DPS:
                aoTableColumns = new STableColumn[11];
                break;
            case SDataConstants.TRN_DNS_DIOG:
                aoTableColumns = new STableColumn[10];
                break;
            default:
        }

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_dns");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        switch (mnTabTypeAux01) {
            case SDataConstants.TRN_DNS_DPS:
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.ct_dps", "Categoría documento", 150);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "l.cl_dps", "Clase documento", 150);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "t.tp_dps", "Tipo documento", 150);
                break;
            case SDataConstants.TRN_DNS_DIOG:
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.ct_iog", "Categoría documento", 150);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "l.cl_iog", "Clase documento", 150);
                break;
            default:
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.dns", "Serie folios", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        String table = "";
        String columns = "";
        String innerJoin = "";
        String orderBy = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.b_del = FALSE ";
            }
        }

        switch (mnTabTypeAux01) {
            case SDataConstants.TRN_DNS_DPS:
                table = "trn_dns_dps ";
                columns = "c.ct_dps, l.cl_dps, t.tp_dps, ";
                innerJoin = "INNER JOIN erp.trns_ct_dps AS c ON " +
                        "d.fid_ct_dps = c.id_ct_dps " +
                        "INNER JOIN erp.trns_cl_dps AS l ON " +
                        "d.fid_ct_dps = l.id_ct_dps AND d.fid_cl_dps = l.id_cl_dps " +
                        "INNER JOIN erp.trnu_tp_dps AS t ON " +
                        "d.fid_ct_dps = t.id_ct_dps AND d.fid_cl_dps = t.id_cl_dps AND d.fid_tp_dps = t.id_tp_dps ";
                orderBy = "ORDER BY c.id_ct_dps, c.ct_dps, l.id_cl_dps, l.cl_dps, t.id_tp_dps, t.tp_dps, d.dns, d.id_dns ";
                break;
            case SDataConstants.TRN_DNS_DIOG:
                table = "trn_dns_diog ";
                columns = "c.ct_iog, l.cl_iog, ";
                innerJoin = "INNER JOIN erp.trns_ct_iog AS c ON " +
                        "d.fid_ct_iog = c.id_ct_iog " +
                        "INNER JOIN erp.trns_cl_iog AS l ON " +
                        "d.fid_ct_iog = l.id_ct_iog AND d.fid_cl_iog = l.id_cl_iog ";
                orderBy = "ORDER BY c.id_ct_iog, c.ct_iog, l.id_cl_iog, l.cl_iog, d.dns, d.id_dns ";
                break;
            default:
        }

        msSql = "SELECT d.id_dns, d.dns, d.b_del, " + columns +
                "d.ts_new, d.ts_edit, d.ts_del, un.usr, ue.usr, ud.usr " +
                "FROM " +  table  + "AS d " + innerJoin +
                "INNER JOIN erp.usru_usr AS un ON d.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON d.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON d.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) + orderBy;
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_CFG).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_CFG).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_CFG).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_CFG).refreshCatalogues(mnTabType);
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
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();
        }
    }
}

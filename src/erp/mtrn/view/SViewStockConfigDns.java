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
 * @author Néstor Ávalos
 */
public class SViewStockConfigDns extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewStockConfigDns(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.TRN_STK_CFG_DNS);
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        jbNew.setEnabled(true);
        jbEdit.setEnabled(true);
        jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[3];
        STableColumn[] aoTableColumns = new STableColumn[15];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_cob");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_wh");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_dns");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bpb", "Sucursal", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.code", "Clave", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.ent", "Almacén", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.code", "Clave", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "t.tp_dps", "Tipo doc.", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dn.dns", "Serie", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "e.b_del", "Almacén eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "dn.b_del", "Serie eliminada", STableConstants.WIDTH_BOOLEAN);
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
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.CFGU_COB_ENT);
        mvSuscriptors.add(SDataConstants.TRNU_TP_DPS);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_INV).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(mnTabType);
            }
	}
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_INV).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(mnTabType);
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
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.b_del = FALSE AND dn.b_del = FALSE AND e.b_del = FALSE ";
            }
        }

        msSql = "SELECT b.bpb, b.code, e.ent, e.code, e.b_del, dn.dns, dn.b_del, t.tp_dps, un.usr, ue.usr, ud.usr, d.* " +
                "FROM trn_stk_cfg_dns AS d " +
                "INNER JOIN erp.bpsu_bpb AS b ON d.id_cob = b.id_bpb " +
                "INNER JOIN erp.cfgu_cob_ent AS e ON d.id_cob = e.id_cob AND d.id_wh = e.id_ent " +
                "INNER JOIN trn_dns_dps AS dn ON d.id_dns = dn.id_dns " +
                "INNER JOIN erp.trnu_tp_dps AS t ON dn.fid_ct_dps = t.id_ct_dps AND dn.fid_cl_dps = t.id_cl_dps AND dn.fid_tp_dps = t.id_tp_dps " +
                "INNER JOIN erp.usru_usr AS un ON d.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON d.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON d.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY b.bpb, b.code, e.ent, e.code, dn.dns ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

        }
    }
}

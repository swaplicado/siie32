/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.view;

import javax.swing.JButton;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableField;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableSetting;
import sa.gui.util.SUtilConsts;
/**
 *
 * @author Alfonso Flores
 */
public class SViewItemGroup extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewItemGroup(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.ITMU_IGRP);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_ITM_ITEM).Level;

        jbNew.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbEdit.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[15];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "igrp.id_igrp");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ifam.ifam", "Familia ítems", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "igrp.igrp", "Grupo ítems", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "igrp.b_free_disc_u", "S/desc. u.", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "igrp.b_free_disc_ety", "S/desc. par.", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "igrp.b_free_disc_doc", "S/desc. doc.", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "igrp.b_free_price", "S/precio", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "igrp.b_free_disc", "S/desc.", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "igrp.b_free_comms", "S/comisión", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "igrp.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "igrp.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "igrp.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "igrp.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_IFAM);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                        miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).refreshCatalogues(mnTabType);
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
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "igrp.b_del = FALSE ";
            }
        }

        msSql = "SELECT igrp.id_igrp, igrp.igrp, igrp.b_free_price, igrp.b_free_disc, igrp.b_free_disc_u, igrp.b_free_disc_ety, igrp.b_free_disc_doc, " +
                "igrp.b_free_comms, igrp.b_del, " +
                "ifam.id_ifam, ifam.ifam, igrp.fid_usr_new, igrp.fid_usr_edit, igrp.fid_usr_del, igrp.ts_new, igrp.ts_edit, igrp.ts_del, un.usr, ue.usr, ud.usr " +
                "FROM erp.itmu_igrp AS igrp " +
                "INNER JOIN erp.itmu_ifam AS ifam ON " +
                "igrp.fid_ifam = ifam.id_ifam " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "igrp.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "igrp.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "igrp.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) + " " +
                "ORDER BY ifam.ifam, ifam.id_ifam, igrp.igrp, igrp.id_igrp ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

        }
    }
}

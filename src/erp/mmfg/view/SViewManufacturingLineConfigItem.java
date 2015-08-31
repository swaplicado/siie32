/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
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
public class SViewManufacturingLineConfigItem extends erp.lib.table.STableTab {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewManufacturingLineConfigItem(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.MFGU_LINE_CFG_ITEM);
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
        STableColumn[] aoTableColumns = new STableColumn[10];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_tp_link");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_ref");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_line");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_link", "Tipo referencia", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ref", "Referencia", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "l.line", "Línea producción", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "t.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "t.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "t.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "t.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.MFGU_LINE);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_MFG).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
            }
	}
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_MFG).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
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
        int[] period = null;
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "t.b_del = 0 ";
            }
        }

        msSql = "SELECT t.*, tl.tp_link AS f_link, l.line, un.usr, ue.usr, ud.usr, " +
                "CASE t.id_tp_link " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ALL + " THEN " +
                    "('" + SDataConstantsSys.TXT_TRNS_TP_LINK_ALL + "') " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CT_ITEM + " THEN (" +
                    "SELECT c.ct_item FROM erp.itms_ct_item c WHERE c.ct_idx = t.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CL_ITEM + " THEN (" +
                    "SELECT c.cl_item FROM erp.itms_cl_item c WHERE c.cl_idx = t.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_TP_ITEM + " THEN (" +
                    "SELECT tp.tp_item FROM erp.itms_tp_item tp WHERE tp.tp_idx = t.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IFAM + " THEN (" +
                    "SELECT ifam.ifam FROM erp.itmu_ifam ifam WHERE ifam.id_ifam = t.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGRP + " THEN (" +
                    "SELECT igrp.igrp FROM erp.itmu_igrp igrp WHERE igrp.id_igrp = t.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGEN + " THEN (" +
                    "SELECT CONCAT(igen.igen, ' (', igen.code, ')') FROM erp.itmu_igen igen WHERE igen.id_igen = t.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_LINE + " THEN (" +
                    "SELECT line.line FROM erp.itmu_line line WHERE line.id_line = t.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_BRD + " THEN (" +
                    "SELECT CONCAT(brd.brd, ' (', brd.code, ')') FROM erp.itmu_brd brd WHERE brd.id_brd = t.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_MFR + " THEN (" +
                    "SELECT CONCAT(mfr.mfr, ' (', mfr.code, ')') FROM erp.itmu_mfr mfr WHERE mfr.id_mfr = t.id_ref) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ITEM + " THEN (" +
                    "SELECT CONCAT(item.item_key, ' - ', item.item) " +
                    "FROM erp.itmu_item item WHERE item.id_item = t.id_ref) " +
                "ELSE 'No existe tipo' " +
                "END AS f_ref " +
                "FROM mfgu_line_cfg_item AS t " +
                "INNER JOIN erp.trns_tp_link AS tl ON " +
                "t.id_tp_link = tl.id_tp_link " +
                "INNER JOIN mfgu_line AS l ON " +
                "t.id_line = l.id_line " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "t.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "t.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "t.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY tl.tp_link, f_ref, l.line, t.id_tp_link, t.id_ref, t.id_line ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import javax.swing.JButton;

/**
 *
 * @author Sergio Flores
 */
public class SViewAccountItemQuery extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewAccountItemQuery(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.FINX_ACC_ITEM_QRY);
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

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[14];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_acc_item");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tl.tp_link", "Tipo referencia", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ref", "Referencia", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "f_b_del_ref", "Referencia eliminada", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ai.acc_item", "Paquete configuración", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ai.b_del", "Paquete configuración eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bkc.code", "Centro contable", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "aii.id_dt_start", "Ini. vigencia", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "aii.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "aii.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "aii.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "aii.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.ITMU_MFR);
        mvSuscriptors.add(SDataConstants.ITMU_BRD);
        mvSuscriptors.add(SDataConstants.ITMU_LINE);
        mvSuscriptors.add(SDataConstants.ITMU_IGEN);
        mvSuscriptors.add(SDataConstants.ITMU_IGRP);
        mvSuscriptors.add(SDataConstants.ITMU_IFAM);
        mvSuscriptors.add(SDataConstants.ITMS_TP_ITEM);
        mvSuscriptors.add(SDataConstants.ITMS_CL_ITEM);
        mvSuscriptors.add(SDataConstants.ITMS_CT_ITEM);
        mvSuscriptors.add(SDataConstants.FIN_ACC_ITEM);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(SDataConstants.FIN_ACC_ITEM, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(SDataConstants.FIN_ACC_ITEM);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(SDataConstants.FIN_ACC_ITEM, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(SDataConstants.FIN_ACC_ITEM);
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
        java.lang.String sqlHaving= "";
        erp.lib.table.STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "aii.b_del = FALSE AND ai.b_del = FALSE ";
                sqlHaving += (sqlHaving.length() == 0 ? "" : "AND ") + "f_b_del_ref = FALSE ";
            }
        }

        msSql = "SELECT aii.id_tp_link, aii.id_ref, aii.id_bkc, aii.id_acc_item, aii.id_dt_start, aii.b_del, " +
                "tl.id_tp_link, tl.tp_link, bkc.id_bkc, bkc.code, ai.id_acc_item, ai.acc_item, ai.b_del, " +
                "aii.ts_new, aii.ts_edit, aii.ts_del, un.usr, ue.usr, ud.usr, " +
                "CASE aii.id_tp_link " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ALL + " THEN " +
                "true " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CT_ITEM + " THEN " +
                "(SELECT r.b_del FROM erp.itms_ct_item AS r WHERE aii.id_ref = r.ct_idx) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CL_ITEM + " THEN " +
                "(SELECT r.b_del FROM erp.itms_cl_item AS r WHERE aii.id_ref = r.cl_idx) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_TP_ITEM + " THEN " +
                "(SELECT r.b_del FROM erp.itms_tp_item AS r WHERE aii.id_ref = r.tp_idx) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IFAM + " THEN " +
                "(SELECT r.b_del FROM erp.itmu_ifam AS r WHERE aii.id_ref = r.id_ifam) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGRP + " THEN " +
                "(SELECT r.b_del FROM erp.itmu_igrp AS r WHERE aii.id_ref = r.id_igrp) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGEN + " THEN " +
                "(SELECT r.b_del FROM erp.itmu_igen AS r WHERE aii.id_ref = r.id_igen) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_LINE + " THEN " +
                "(SELECT r.b_del FROM erp.itmu_line AS r WHERE aii.id_ref = r.id_line) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_BRD + " THEN " +
                "(SELECT r.b_del FROM erp.itmu_brd AS r WHERE aii.id_ref = r.id_brd) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_MFR + " THEN " +
                "(SELECT r.b_del FROM erp.itmu_mfr AS r WHERE aii.id_ref = r.id_mfr) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ITEM + " THEN " +
                "(SELECT r.b_del FROM erp.itmu_item AS r WHERE aii.id_ref = r.id_item) " +
                "ELSE false END AS f_b_del_ref, " +
                "CASE aii.id_tp_link " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ALL + " THEN " +
                "('" + SDataConstantsSys.TXT_TRNS_TP_LINK_ALL + "') " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CT_ITEM + " THEN " +
                "(SELECT r.ct_item FROM erp.itms_ct_item AS r WHERE aii.id_ref = r.ct_idx) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CL_ITEM + " THEN " +
                "(SELECT r.cl_item FROM erp.itms_cl_item AS r WHERE aii.id_ref = r.cl_idx) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_TP_ITEM + " THEN " +
                "(SELECT r.tp_item FROM erp.itms_tp_item AS r WHERE aii.id_ref = r.tp_idx) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IFAM + " THEN " +
                "(SELECT r.ifam FROM erp.itmu_ifam AS r WHERE aii.id_ref = r.id_ifam) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGRP + " THEN " +
                "(SELECT r.igrp FROM erp.itmu_igrp AS r WHERE aii.id_ref = r.id_igrp) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGEN + " THEN " +
                "(SELECT CONCAT(r.igen, ' (', r.code, ')') FROM erp.itmu_igen AS r WHERE aii.id_ref = r.id_igen) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_LINE + " THEN " +
                "(SELECT CONCAT(r.line, ' (', r.code, ')') FROM erp.itmu_line AS r WHERE aii.id_ref = r.id_line) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_BRD + " THEN " +
                "(SELECT CONCAT(r.brd, ' (', r.code, ')') FROM erp.itmu_brd AS r WHERE aii.id_ref = r.id_brd) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_MFR + " THEN " +
                "(SELECT CONCAT(r.mfr, ' (', r.code, ')') FROM erp.itmu_mfr AS r WHERE aii.id_ref = r.id_mfr) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ITEM + " THEN " +
                "(SELECT CONCAT(r.item, ' (', r.item_key, ')') FROM erp.itmu_item AS r WHERE aii.id_ref = r.id_item) " +
                "ELSE '?' END AS f_ref " +
                "FROM fin_acc_item_item AS aii " +
                "INNER JOIN fin_acc_item AS ai ON aii.id_acc_item = ai.id_acc_item " +
                "INNER JOIN erp.trns_tp_link AS tl ON aii.id_tp_link = tl.id_tp_link " +
                "INNER JOIN fin_bkc AS bkc ON aii.id_bkc = bkc.id_bkc " +
                "INNER JOIN erp.usru_usr AS un ON aii.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON aii.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON aii.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                (sqlHaving.length() == 0 ? "" : "HAVING " + sqlHaving) +
                "ORDER BY aii.id_tp_link, bkc.code, bkc.id_bkc, f_ref, ai.acc_item, ai.id_acc_item, aii.id_dt_start ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
        }
    }
}

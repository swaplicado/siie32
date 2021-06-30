/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Juan Barajas
 */
public class SViewStockConfigItem extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewStockConfigItem(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.TRN_STK_CFG_ITEM);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightInAdjustment = SDataConstantsSys.UNDEFINED;
        int levelRightOutAdjustment = SDataConstantsSys.UNDEFINED;
        int levelRightOutOtherInt = SDataConstantsSys.UNDEFINED;

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        STableField[] aoKeyFields = new STableField[4];
        STableColumn[] aoTableColumns = new STableColumn[14];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "stk_it.id_tp_link");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "stk_it.id_ref");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "stk_it.id_cob");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "stk_it.id_wh");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tl.tp_link", "Tipo de referencia", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ref", "Referencia", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "wh.code", "Código almacén", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "wh.ent", "Almacén", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "f_b_del_ref", "Referencia eliminada", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "wh.b_del", "Almacén eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "stk_it.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "stk_it.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "stk_it.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "stk_it.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        levelRightInAdjustment = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_IN_ADJ).Level;
        levelRightOutAdjustment = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_OUT_ADJ).Level;
        levelRightOutOtherInt = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_OUT_INT).Level;

        jbNew.setEnabled(levelRightInAdjustment >= SUtilConsts.LEV_AUTHOR || levelRightOutAdjustment >= SUtilConsts.LEV_AUTHOR || levelRightOutOtherInt >= SUtilConsts.LEV_AUTHOR);
        jbEdit.setEnabled(levelRightInAdjustment >= SUtilConsts.LEV_AUTHOR || levelRightOutAdjustment >= SUtilConsts.LEV_AUTHOR || levelRightOutOtherInt >= SUtilConsts.LEV_AUTHOR);
        jbDelete.setEnabled(false);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_IFAM);
        mvSuscriptors.add(SDataConstants.ITMU_IGRP);
        mvSuscriptors.add(SDataConstants.ITMU_IGEN);
        mvSuscriptors.add(SDataConstants.ITMU_LINE);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.ITMU_BRD);
        mvSuscriptors.add(SDataConstants.ITMU_MFR);
        mvSuscriptors.add(SDataConstants.CFGU_COB_ENT);
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
        String sqlHaving= "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "stk_it.b_del = FALSE AND wh.b_del = FALSE ";
                sqlHaving += (sqlHaving.length() == 0 ? "" : "AND ") + "f_b_del_ref = FALSE ";
            }
        }

        msSql = "SELECT stk_it.id_tp_link, stk_it.id_ref, stk_it.id_cob, stk_it.id_wh, stk_it.b_del, tl.id_tp_link, tl.tp_link, bpb.code, wh.code, wh.ent, wh.b_del, " +
                "stk_it.ts_new, stk_it.ts_edit, stk_it.ts_del, un.usr, ue.usr, ud.usr, " +
                "CASE stk_it.id_tp_link " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ALL + " THEN " +
                "true " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CT_ITEM + " THEN " +
                "(SELECT r.b_del FROM erp.itms_ct_item AS r WHERE stk_it.id_ref = r.ct_idx) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CL_ITEM + " THEN " +
                "(SELECT r.b_del FROM erp.itms_cl_item AS r WHERE stk_it.id_ref = r.cl_idx) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_TP_ITEM + " THEN " +
                "(SELECT r.b_del FROM erp.itms_tp_item AS r WHERE stk_it.id_ref = r.tp_idx) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IFAM + " THEN " +
                "(SELECT r.b_del FROM erp.itmu_ifam AS r WHERE stk_it.id_ref = r.id_ifam) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGRP + " THEN " +
                "(SELECT r.b_del FROM erp.itmu_igrp AS r WHERE stk_it.id_ref = r.id_igrp) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGEN + " THEN " +
                "(SELECT r.b_del FROM erp.itmu_igen AS r WHERE stk_it.id_ref = r.id_igen) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_LINE + " THEN " +
                "(SELECT r.b_del FROM erp.itmu_line AS r WHERE stk_it.id_ref = r.id_line) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_BRD + " THEN " +
                "(SELECT r.b_del FROM erp.itmu_brd AS r WHERE stk_it.id_ref = r.id_brd) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_MFR + " THEN " +
                "(SELECT r.b_del FROM erp.itmu_mfr AS r WHERE stk_it.id_ref = r.id_mfr) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ITEM + " THEN " +
                "(SELECT r.b_del FROM erp.itmu_item AS r WHERE stk_it.id_ref = r.id_item) " +
                "ELSE false END AS f_b_del_ref, " +
                "CASE stk_it.id_tp_link " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ALL + " THEN " +
                "('" + SDataConstantsSys.TXT_TRNS_TP_LINK_ALL + "') " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CT_ITEM + " THEN " +
                "(SELECT r.ct_item FROM erp.itms_ct_item AS r WHERE stk_it.id_ref = r.ct_idx) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_CL_ITEM + " THEN " +
                "(SELECT r.cl_item FROM erp.itms_cl_item AS r WHERE stk_it.id_ref = r.cl_idx) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_TP_ITEM + " THEN " +
                "(SELECT r.tp_item FROM erp.itms_tp_item AS r WHERE stk_it.id_ref = r.tp_idx) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IFAM + " THEN " +
                "(SELECT r.ifam FROM erp.itmu_ifam AS r WHERE stk_it.id_ref = r.id_ifam) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGRP + " THEN " +
                "(SELECT r.igrp FROM erp.itmu_igrp AS r WHERE stk_it.id_ref = r.id_igrp) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_IGEN + " THEN " +
                "(SELECT CONCAT(r.igen, ' (', r.code, ')') FROM erp.itmu_igen AS r WHERE stk_it.id_ref = r.id_igen) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_LINE + " THEN " +
                "(SELECT CONCAT(r.line, ' (', r.code, ')') FROM erp.itmu_line AS r WHERE stk_it.id_ref = r.id_line) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_BRD + " THEN " +
                "(SELECT CONCAT(r.brd, ' (', r.code, ')') FROM erp.itmu_brd AS r WHERE stk_it.id_ref = r.id_brd) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_MFR + " THEN " +
                "(SELECT CONCAT(r.mfr, ' (', r.code, ')') FROM erp.itmu_mfr AS r WHERE stk_it.id_ref = r.id_mfr) " +
                "WHEN " + SDataConstantsSys.TRNS_TP_LINK_ITEM + " THEN " +
                "(SELECT CONCAT(r.item, ' (', r.item_key, ')') FROM erp.itmu_item AS r WHERE stk_it.id_ref = r.id_item) " +
                "ELSE '?' END AS f_ref " +
                "FROM trn_stk_cfg_item AS stk_it " +
                "INNER JOIN erp.trns_tp_link AS tl ON stk_it.id_tp_link = tl.id_tp_link " +
                "INNER JOIN erp.cfgu_cob_ent AS wh ON stk_it.id_cob = wh.id_cob AND stk_it.id_wh = wh.id_ent " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON stk_it.id_cob = bpb.id_bpb " +
                "INNER JOIN erp.usru_usr AS un ON stk_it.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON stk_it.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON stk_it.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                (sqlHaving.length() == 0 ? "" : "HAVING " + sqlHaving) +
                "ORDER BY stk_it.id_tp_link, f_ref ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();
        }
    }
}

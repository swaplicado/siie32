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
import erp.mitm.form.SPanelFilterItemGeneric;
import erp.table.SFilterConstants;
import erp.table.STabFilterCompanyBranchEntity;
import java.awt.Dimension;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Sergio Flores
 */
public class SViewStockConfig extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbCopy;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.table.STabFilterCompanyBranchEntity moTabFilterCompanyBranchEntity;
    private erp.mitm.form.SPanelFilterItemGeneric moPanelFilterItemGeneric;

    public SViewStockConfig(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.TRN_STK_CFG);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightInAdjustment = SDataConstantsSys.UNDEFINED;
        int levelRightOutAdjustment = SDataConstantsSys.UNDEFINED;
        int levelRightOutOtherInt = SDataConstantsSys.UNDEFINED;

        jbCopy = new JButton(miClient.getImageIcon(SLibConstants.ICON_COPY));
        jbCopy.setPreferredSize(new Dimension(23, 23));
        jbCopy.setToolTipText("Copiar");
        jbCopy.addActionListener(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbCopy);

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterCompanyBranchEntity = new STabFilterCompanyBranchEntity(miClient, this, SDataConstantsSys.CFGS_CT_ENT_WH);
        moPanelFilterItemGeneric = new SPanelFilterItemGeneric(miClient, this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranchEntity);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moPanelFilterItemGeneric);

        levelRightInAdjustment = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_IN_ADJ).Level;
        levelRightOutAdjustment = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_OUT_ADJ).Level;
        levelRightOutOtherInt = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_OUT_INT).Level;

        jbNew.setEnabled(levelRightInAdjustment >= SUtilConsts.LEV_AUTHOR || levelRightOutAdjustment >= SUtilConsts.LEV_AUTHOR || levelRightOutOtherInt >= SUtilConsts.LEV_AUTHOR);
        jbEdit.setEnabled(levelRightInAdjustment >= SUtilConsts.LEV_AUTHOR || levelRightOutAdjustment >= SUtilConsts.LEV_AUTHOR || levelRightOutOtherInt >= SUtilConsts.LEV_AUTHOR);
        jbCopy.setEnabled(levelRightInAdjustment >= SUtilConsts.LEV_AUTHOR || levelRightOutAdjustment >= SUtilConsts.LEV_AUTHOR || levelRightOutOtherInt >= SUtilConsts.LEV_AUTHOR);
        jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[4];
        STableColumn[] aoTableColumns = new STableColumn[19];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "sc.id_item");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "sc.id_unit");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "sc.id_cob");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "sc.id_wh");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 300);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 300);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bb.bpb", "Sucursal", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.ent", "Almacén", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.code", "Código almacén", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "sc.qty_min", "Mínimo", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "sc.rop", "Pto. reorden", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "sc.qty_max", "Máximo", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ig.igen", "Ítem genérico", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "il.line", "Línea ítems", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "sc.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_del", "Ítem eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "sc.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "sc.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "sc.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.ITMU_UNIT);
        mvSuscriptors.add(SDataConstants.ITMU_IGEN);
        mvSuscriptors.add(SDataConstants.ITMU_LINE);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void createSqlQuery() {
        int[] key = null;
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "sc.b_del = 0 AND i.b_del = 0 AND u.b_del = 0 AND bb.b_del = 0 AND e.b_del = 0 ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_ITM_ITEM_GENERIC) {
                if (((int[]) setting.getSetting())[0] != SLibConstants.UNDEFINED) {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "i.fid_igen = " + ((int[]) setting.getSetting())[0] + " ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB_ENT) {
                key = (int[]) setting.getSetting();
                if (key != null) {
                    if (key[0] != SLibConstants.UNDEFINED) {
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "sc.id_cob = " + key[0] + " ";
                    }
                    if (key[1] != SLibConstants.UNDEFINED) {
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "sc.id_wh = " + key[1] + " ";
                    }
                }
            }
        }

        msSql = "SELECT sc.id_item, sc.id_unit, sc.id_cob, sc.id_wh, sc.qty_min, sc.qty_max, sc.rop, sc.b_del, " +
                "i.item, i.item_key, i.b_del, ig.igen, il.line, u.symbol, bb.bpb, e.ent, e.code, " +
                "sc.ts_new, sc.ts_edit, sc.ts_del, un.usr, ue.usr, ud.usr " +
                "FROM trn_stk_cfg AS sc " +
                "INNER JOIN erp.itmu_item AS i ON sc.id_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON sc.id_unit = u.id_unit " +
                "INNER JOIN erp.bpsu_bpb AS bb ON sc.id_cob = bb.id_bpb " +
                "INNER JOIN erp.cfgu_cob_ent AS e ON sc.id_cob = e.id_cob AND sc.id_wh = e.id_ent " +
                "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " +
                "INNER JOIN erp.usru_usr AS un ON sc.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON sc.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON sc.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN erp.itmu_line AS il ON i.fid_line_n = il.id_line " +
                (sqlWhere.length() == 0 ? "" : " WHERE " + sqlWhere) +
                "ORDER BY " + (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                    "i.item_key, i.item, " :
                    "i.item, i.item_key, ") + "sc.id_item, u.symbol, sc.id_unit, " +
                    "bb.bpb, bb.code, sc.id_cob, e.ent, e.code, sc.id_wh ";
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_INV).showForm(SDataConstants.TRN_STK_CFG, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_INV).showForm(SDataConstants.TRN_STK_CFG, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
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

    public void actionCopy() {
        if (jbCopy.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_INV).showFormForCopy(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbCopy) {
                actionCopy();
            }
        }
    }
}

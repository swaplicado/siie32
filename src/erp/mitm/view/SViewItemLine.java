/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.view;

import java.awt.Dimension;
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
 * @author Alfonso Flores, Sergio Flores
 */
public class SViewItemLine extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbCopy;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewItemLine(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.ITMU_LINE);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;

        jbCopy = new JButton(miClient.getImageIcon(SLibConstants.ICON_COPY));
        jbCopy.setPreferredSize(new Dimension(23, 23));
        jbCopy.setToolTipText("Copiar");
        jbCopy.addActionListener(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbCopy);

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_ITM_ITEM).Level;

        jbNew.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbEdit.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbCopy.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[20];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "l.id_line");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ig.igen", "Ítem genérico", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "l.line", "Línea ítems", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "l.code", "Código", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.brd", "Marca", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "l.b_fix_brd", "Marca fija", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "m.mfr", "Fabricante", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "l.b_fix_mfr", "Fabricante fijo", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.emt", "Elemento", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "l.b_fix_emt", "Elemento fijo", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "t1.tp_var", "Tipo variedad 1", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "t2.tp_var", "Tipo variedad 2", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "t3.tp_var", "Tipo variedad 3", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "l.line_short", "Línea ítems (corto)", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "l.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "l.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "l.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "l.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
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
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "l.b_del = FALSE ";
            }
        }

        msSql = "SELECT l.id_line, l.line, l.line_short, l.code, l.b_fix_brd, l.b_fix_mfr, l.b_fix_emt, l.b_del, " +
                "ig.igen, b.brd, m.mfr, e.emt, t1.tp_var, t2.tp_var, t3.tp_var, " +
                "l.ts_new, l.ts_edit, l.ts_del, un.usr, ue.usr, ud.usr " +
                "FROM erp.itmu_line AS l " +
                "INNER JOIN erp.itmu_igen AS ig ON " +
                "l.fid_igen = ig.id_igen " +
                "INNER JOIN erp.itmu_brd AS b ON " +
                "l.fid_brd = b.id_brd " +
                "INNER JOIN erp.itmu_mfr AS m ON " +
                "l.fid_mfr = m.id_mfr " +
                "INNER JOIN erp.itmu_emt AS e ON " +
                "l.fid_emt = e.id_emt " +
                "INNER JOIN erp.itmu_tp_var AS t1 ON " +
                "l.fid_tp_var_01 = t1.id_tp_var " +
                "INNER JOIN erp.itmu_tp_var AS t2 ON " +
                "l.fid_tp_var_02 = t2.id_tp_var " +
                "INNER JOIN erp.itmu_tp_var AS t3 ON " +
                "l.fid_tp_var_03 = t3.id_tp_var " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "l.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "l.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "l.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY ig.igen, ig.id_igen, l.line, l.id_line ";
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

    public void actionCopy() {
        if (jbCopy.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).showFormForCopy(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).refreshCatalogues(mnTabType);
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

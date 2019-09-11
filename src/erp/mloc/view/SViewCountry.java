/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mloc.view;

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
 * @author Alfonso Flores, Sergio Flores
 */
public class SViewCountry extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    erp.mcfg.data.SDataParamsErp paramsErp = new erp.mcfg.data.SDataParamsErp();

    public SViewCountry(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.LOCU_CTY);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        jbNew.setEnabled(true);
        jbEdit.setEnabled(true);
        jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[13];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "cty.id_cty");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        if (miClient.getSessionXXX().getParamsErp().getFkSortingLocalityTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cty.cty_key", "Clave", 75);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cty.cty", "País", 200);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cty.cty", "País", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cty.cty_key", "Clave", 75);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cty.cty_abbr", "Abr.", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cty.cty_code", "Código país", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cty.cty_group", "Agrupación SAT CFDI", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cty.diot_code", "Código DIOT", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "cty.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "cty.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "cty.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "cty.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_LOC).Level;

        jbNew.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbEdit.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbDelete.setEnabled(false);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_LOC).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.GLOBAL_CAT_LOC).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_LOC).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_LOC).refreshCatalogues(mnTabType);
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
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "cty.b_del = FALSE ";
            }
        }

        msSql = "SELECT cty.id_cty, cty.cty_key, cty.cty, cty.cty_abbr, cty.cty_code, cty.cty_group, cty.diot_code, cty.b_del, " +
                "cty.fid_usr_new, cty.fid_usr_edit, cty.fid_usr_del, cty.ts_new, cty.ts_edit, cty.ts_del, un.usr, ue.usr, ud.usr " +
                "FROM erp.locu_cty AS cty " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "cty.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "cty.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "cty.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY " + (miClient.getSessionXXX().getParamsErp().getFkSortingLocalityTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "cty.cty_key, cty.cty, " :
                    "cty.cty, cty.cty_key, ") + "cty.id_cty ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
        }
    }
}

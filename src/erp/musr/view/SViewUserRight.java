/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Alfonso Flores
 */
public class SViewUserRight extends erp.lib.table.STableTab {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewUserRight(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.USRX_RIGHT);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        //jbDelete.setEnabled(false);

        erp.lib.table.STableField[] aoKeyFields = new STableField[1];
        erp.lib.table.STableColumn[] aoTableColumns = new STableColumn[5];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "u.id_usr");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.usr", "Usuario", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_config_rol_usr", "Roles usr.", STableConstants.WIDTH_BOOLEAN_3X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_config_prv_usr", "Privilegios usr.", STableConstants.WIDTH_BOOLEAN_3X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_config_rol_co", "Roles empresa", STableConstants.WIDTH_BOOLEAN_3X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_config_prv_co", "Privilegios empresa", STableConstants.WIDTH_BOOLEAN_3X);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_USR).Level;

        jbNew.setEnabled(false);
        jbEdit.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {

        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_USR).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_USR).refreshCatalogues(mnTabType);
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
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "u.b_del = FALSE ";
            }
        }

        msSql = "SELECT u.id_usr, u.usr, erp.usr_validate_config_rol_usr(u.id_usr) AS b_config_rol_usr, " +
                "erp.usr_validate_config_prv_usr(u.id_usr) AS b_config_prv_usr, " +
                "erp.usr_validate_config_rol_co(u.id_usr) AS b_config_rol_co, " +
                "erp.usr_validate_config_prv_co(u.id_usr) AS b_config_prv_co " +
                "FROM erp.usru_usr AS u " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY u.usr, u.id_usr ";
    }
}

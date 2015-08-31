/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.view;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;

/**
 *
 * @author Sergio Flores
 */
public class SViewUserRightPrivilege extends erp.lib.table.STableTab {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewUserRightPrivilege(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.USRX_RIGHT_PRV);
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        //jbDelete.setEnabled(false);

        erp.lib.table.STableField[] aoKeyFields = new STableField[1];
        erp.lib.table.STableColumn[] aoTableColumns = new STableColumn[7];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_usr");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "usr", "Usuario", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_co", "Empresa", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "f_co_b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_prv", "Tipo privilegio", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "prv", "Privilegio", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_lev", "Nivel de acceso", 100);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.USRU_USR);
        mvSuscriptors.add(SDataConstants.USRX_RIGHT);

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
        java.lang.String sqlWhereCo = "";
        erp.lib.table.STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "(u.b_del = FALSE) ";
                sqlWhereCo += (sqlWhereCo.length() == 0 ? "" : "AND ") + "(u.b_del = FALSE AND c.b_del = FALSE) ";
            }
        }

        msSql = "SELECT pu.id_usr, pu.id_prv, u.usr, u.b_del, p.prv, pt.tp_prv, pl.tp_lev, " +
                "0 AS f_id_co, '' AS f_co, 0 AS f_co_b_del, 0 AS f_id_bpb, '' AS f_bpb, 0 AS f_bpb_b_del " +
                "FROM erp.usru_prv_usr AS pu " +
                "INNER JOIN erp.usru_usr AS u ON " +
                "pu.id_usr = u.id_usr " +
                "INNER JOIN erp.usrs_prv AS p ON " +
                "pu.id_prv = p.id_prv " +
                "INNER JOIN erp.usrs_tp_prv AS pt ON " +
                "p.fid_tp_prv = pt.id_tp_prv " +
                "INNER JOIN erp.usrs_tp_lev AS pl ON " +
                "pu.fid_tp_lev = pl.id_tp_lev " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "UNION " +
                "SELECT pu.id_usr, pu.id_prv, u.usr, u.b_del, p.prv, pt.tp_prv, pl.tp_lev, " +
                "c.id_co AS f_id_co, c.co AS f_co, c.b_del AS f_co_b_del, 0 AS f_id_bpb, '' AS f_bpb, 0 AS f_bpb_b_del " +
                "FROM erp.usru_prv_co AS pu " +
                "INNER JOIN erp.usru_usr AS u ON " +
                "pu.id_usr = u.id_usr " +
                "INNER JOIN erp.cfgu_co AS c ON " +
                "pu.id_co = c.id_co " +
                "INNER JOIN erp.usrs_prv AS p ON " +
                "pu.id_prv = p.id_prv " +
                "INNER JOIN erp.usrs_tp_prv AS pt ON " +
                "p.fid_tp_prv = pt.id_tp_prv " +
                "INNER JOIN erp.usrs_tp_lev AS pl ON " +
                "pu.fid_tp_lev = pl.id_tp_lev " +
                (sqlWhereCo.length() == 0 ? "" : "WHERE " + sqlWhereCo) +
                "ORDER BY usr, id_usr, f_co, f_id_co, id_prv ";
    }
}

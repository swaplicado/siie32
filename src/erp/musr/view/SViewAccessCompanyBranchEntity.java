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
import javax.swing.JButton;

/**
 *
 * @author Sergio Flores
 */
public class SViewAccessCompanyBranchEntity extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewAccessCompanyBranchEntity(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.USRU_ACCESS_COB_ENT);
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

        erp.lib.table.STableField[] aoKeyFields = new STableField[3];
        erp.lib.table.STableColumn[] aoTableColumns = new STableColumn[12];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "a.id_usr");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "a.id_cob");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "a.id_ent");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.co", "Empresa", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "c.b_del", "Eliminado empresa", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bpb", "Sucursal empresa", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b.b_del", "Eliminado sucursal", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ec.ct_ent", "Categoría entidad", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "et.tp_ent", "Tipo entidad", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.ent", "Entidad", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.code", "Código", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "e.b_del", "Eliminado sucursal", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.usr", "Usuario", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "u.b_del", "Eliminado usuario", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "a.b_def", "Acceso predeterminado", STableConstants.WIDTH_BOOLEAN_3X);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.CFGU_CO);
        mvSuscriptors.add(SDataConstants.CFGU_COB_ENT);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.USRU_USR);
        mvSuscriptors.add(SDataConstants.USRX_RIGHT);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_USR).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.GLOBAL_CAT_USR).refreshCatalogues(mnTabType);
            }
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
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "(u.b_del = FALSE AND c.b_del = FALSE AND b.b_del = FALSE AND e.b_del = FALSE) ";
            }
        }

        msSql = "SELECT a.id_usr, a.id_cob, a.id_ent, a.b_def, " +
                "u.usr, u.b_del, c.co, c.b_del, b.bpb, b.b_del, " +
                "ec.id_ct_ent, ec.ct_ent, et.id_tp_ent, et.tp_ent, e.ent, e.code, e.b_del " +
                "FROM erp.usru_access_cob_ent AS a " +
                "INNER JOIN erp.usru_usr AS u ON " +
                "a.id_usr = u.id_usr " +
                "INNER JOIN erp.bpsu_bpb AS b ON " +
                "a.id_cob = b.id_bpb " +
                "INNER JOIN erp.cfgu_co AS c ON " +
                "b.fid_bp = c.id_co " +
                "INNER JOIN erp.cfgu_cob_ent AS e ON " +
                "a.id_cob = e.id_cob AND a.id_ent = e.id_ent " +
                "INNER JOIN erp.cfgs_ct_ent AS ec ON " +
                "e.fid_ct_ent = ec.id_ct_ent " +
                "INNER JOIN erp.cfgs_tp_ent AS et ON " +
                "e.fid_ct_ent = et.id_ct_ent AND e.fid_tp_ent = et.id_tp_ent " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY c.co, c.id_co, b.bpb, a.id_cob, ec.id_ct_ent, et.id_tp_ent, e.ent, a.id_ent, u.usr, a.id_usr ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
        }
    }
}

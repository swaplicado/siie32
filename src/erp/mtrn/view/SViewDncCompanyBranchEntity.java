/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import javax.swing.JButton;

/**
 *
 * @author Alfonso Flores
 */
public class SViewDncCompanyBranchEntity extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewDncCompanyBranchEntity(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType) {
        super(client, tabTitle, auxType);
        mnTabTypeAux01 = auxType;
        initComponents();
    }

    private void initComponents() {
        int i;
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        jbNew.setEnabled(true);
        jbEdit.setEnabled(true);
        jbDelete.setEnabled(false);

        if (mnTabTypeAux01 == SDataConstants.TRN_DNC_DPS_COB || mnTabTypeAux01 == SDataConstants.TRN_DNC_DIOG_COB) {
            aoKeyFields = new STableField[2];
            aoTableColumns = new STableColumn[10];
        }
        else {
            aoKeyFields = new STableField[3];
            aoTableColumns = new STableColumn[11];
        }

        i = 0;
        if (mnTabTypeAux01 == SDataConstants.TRN_DNC_DPS_COB || mnTabTypeAux01 == SDataConstants.TRN_DNC_DIOG_COB) {
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_cob");
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_dnc");
        }
        else {
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_cob");
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_ent");
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_dnc");
        }
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "co.co", "Empresa", 200);
        if (mnTabTypeAux01 == SDataConstants.TRN_DNC_DPS_COB || mnTabTypeAux01 == SDataConstants.TRN_DNC_DIOG_COB) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bpb", "Sucursal empresa", 150);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.dnc", "Centro foliado", 200);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bpb", "Sucursal empresa", 150);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.ent", "Entidad", 250);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.dnc", "Centro foliado", 200);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.USRU_USR);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.CFGU_COB_ENT);
        mvSuscriptors.add(SDataConstants.TRN_DNC_DPS);
        mvSuscriptors.add(SDataConstants.TRN_DNC_DIOG);

        populateTable();
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        String table = "";
        String columns = "";
        String innerJoin = "";
        String orderBy = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.b_del = FALSE ";
            }
        }

        switch (mnTabTypeAux01) {
            case SDataConstants.TRN_DNC_DPS_COB:
                table = "trn_dnc_dps_cob ";
                columns = "d.id_cob, d.id_dnc ";
                innerJoin = "INNER JOIN trn_dnc_dps AS c ON " +
                        "d.id_dnc = c.id_dnc ";
                orderBy = "co.co, b.bpb, c.dnc ";
                break;
            case SDataConstants.TRN_DNC_DIOG_COB:
                table = "trn_dnc_diog_cob ";
                columns = "d.id_cob, d.id_dnc ";
                innerJoin = "INNER JOIN trn_dnc_diog AS c ON " +
                        "d.id_dnc = c.id_dnc ";
                orderBy = "co.co, b.bpb, c.dnc ";
                break;
            case SDataConstants.TRN_DNC_DPS_COB_ENT:
                table = "trn_dnc_dps_cob_ent ";
                columns = "d.id_cob, d.id_ent, d.id_dnc, e.ent ";
                innerJoin = "INNER JOIN trn_dnc_dps AS c ON " +
                        "d.id_dnc = c.id_dnc " +
                        "INNER JOIN erp.cfgu_cob_ent AS e ON " +
                        "d.id_cob = e.id_cob AND d.id_ent = e.id_ent ";
                orderBy = "co.co, b.bpb, e.ent, c.dnc ";
                break;
            case SDataConstants.TRN_DNC_DIOG_COB_ENT:
                table = "trn_dnc_diog_cob_ent ";
                columns = "d.id_cob, d.id_ent, d.id_dnc, e.ent ";
                innerJoin = "INNER JOIN trn_dnc_diog AS c ON " +
                        "d.id_dnc = c.id_dnc " +
                        "INNER JOIN erp.cfgu_cob_ent AS e ON " +
                        "d.id_cob = e.id_cob AND d.id_ent = e.id_ent ";
                orderBy = "co.co, b.bpb, e.ent, c.dnc ";
                break;
            default:
        }

        msSql = "SELECT " + columns + ", d.b_del, b.bpb, c.dnc, co.co, " +
                "d.ts_new, d.ts_edit, d.ts_del, un.usr, ue.usr, ud.usr " +
                "FROM " +  table  + "AS d " +
                innerJoin +
                "INNER JOIN erp.bpsu_bpb AS b ON " +
                "d.id_cob = b.id_bpb " +
                "INNER JOIN erp.cfgu_co AS co ON " +
                "b.fid_bp = co.id_co " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "d.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "d.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "d.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY " + orderBy + " ";
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_CFG).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_CFG).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_CFG).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_CFG).refreshCatalogues(mnTabType);
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
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();
        }
    }
}

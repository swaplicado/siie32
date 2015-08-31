/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import javax.swing.JButton;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.lib.SLibConstants;

/**
 *
 * @author Néstor Ávalos
 */
public class SViewBizPartnerBlocking extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewBizPartnerBlocking(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRN_BP_BLOCK, auxType01);
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

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[9];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_bp");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_ct_bp");

        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", mnTabTypeAux01 == SDataConstantsSys.BPSS_CT_BP_SUP ? "Proveedor" : "Cliente", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "t.b_block", "Bloqueado", STableConstants.WIDTH_BOOLEAN_2X);
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
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(mnTabTypeAux01 == SDataConstantsSys.BPSS_CT_BP_SUP ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(mnTabTypeAux01 == SDataConstantsSys.BPSS_CT_BP_SUP ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
            }
	}
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(mnTabTypeAux01 == SDataConstantsSys.BPSS_CT_BP_SUP ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(mnTabTypeAux01 == SDataConstantsSys.BPSS_CT_BP_SUP ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
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
                sqlWhere += "AND t.b_del = FALSE ";
            }
        }

        msSql = "SELECT t.*, b.bp, un.usr, ue.usr, ud.usr " +
                "FROM trn_bp_block AS t " +
                "INNER JOIN erp.bpsu_bp_ct AS c ON t.id_bp = c.id_bp AND t.id_ct_bp = c.id_ct_bp " +
                "INNER JOIN erp.bpsu_bp AS b ON c.id_bp = b.id_bp " +
                "INNER JOIN erp.usru_usr AS un ON t.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON t.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON t.fid_usr_del = ud.id_usr " +
                "WHERE t.id_ct_bp = " + mnTabTypeAux01 + " " + (sqlWhere.length() == 0 ? "" : sqlWhere) + " " +
                "ORDER BY b.bp, t.id_bp ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

        }
    }
}

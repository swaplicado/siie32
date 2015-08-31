/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.view;

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
 * @author Alfonso Flores
 */
public class SViewBizPartnerType extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewBizPartnerType(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.BPSU_TP_BP);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[16];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bpsu_tp_bp.id_ct_bp");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bpsu_tp_bp.id_tp_bp");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpss_ct_bp.ct_bp", "Categoría asociado negocios", 125);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpsu_tp_bp.tp_bp", "Tipo asociado negocios", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tc.tp_cred", "Tipo crédito", 150);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "bpsu_tp_bp.cred_lim", "Límite crédito", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererNumberDouble());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "bpsu_tp_bp.days_cred", "Días créd.", STableConstants.WIDTH_NUM_INTEGER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "bpsu_tp_bp.days_grace", "Días gracia", STableConstants.WIDTH_NUM_INTEGER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tr.name", "Tipo riesgo", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bpsu_tp_bp.b_can_edit", "Modificable", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bpsu_tp_bp.b_can_del", "Eliminable", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bpsu_tp_bp.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bpsu_tp_bp.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bpsu_tp_bp.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bpsu_tp_bp.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_TP_BP).Level;

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
            if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (!moTablePane.getSelectedTableRow().getIsEditable()) {
                    miClient.showMsgBoxWarning(STableConstants.MSG_WAR_REGISTRY_NO_EDITABLE);
                }
                else {
                    if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                        miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabType);
                    }
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
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "bpsu_tp_bp.b_del = FALSE ";
            }
        }

        msSql = "SELECT bpsu_tp_bp.id_ct_bp, bpsu_tp_bp.id_tp_bp, bpsu_tp_bp.tp_bp, bpsu_tp_bp.b_can_edit, bpsu_tp_bp.b_can_del, bpsu_tp_bp.b_del, " +
                "bpsu_tp_bp.cred_lim, bpsu_tp_bp.days_cred, bpsu_tp_bp.days_grace, tr.name,  bpsu_tp_bp.b_can_edit AS " + STableConstants.FIELD_IS_EDITABLE + ", tc.tp_cred, " +
                "bpss_ct_bp.ct_bp, bpsu_tp_bp.fid_usr_new, bpsu_tp_bp.fid_usr_edit, bpsu_tp_bp.fid_usr_del, bpsu_tp_bp.ts_new, bpsu_tp_bp.ts_edit, bpsu_tp_bp.ts_del, un.usr, ue.usr, ud.usr " +
                "FROM erp.bpsu_tp_bp AS bpsu_tp_bp " +
                "INNER JOIN erp.bpss_ct_bp AS bpss_ct_bp ON " +
                "bpsu_tp_bp.id_ct_bp = bpss_ct_bp.id_ct_bp " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "bpsu_tp_bp.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "bpsu_tp_bp.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "bpsu_tp_bp.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN erp.bpss_tp_cred AS tc ON " +
                "bpsu_tp_bp.fid_tp_cred = tc.id_tp_cred " +
                "LEFT OUTER JOIN erp.bpss_risk AS tr ON " +
                "bpsu_tp_bp.fid_tp_risk = tr.id_risk " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY bpsu_tp_bp.id_ct_bp, bpss_ct_bp.ct_bp, bpsu_tp_bp.tp_bp, bpsu_tp_bp.id_tp_bp ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
        }
    }
}

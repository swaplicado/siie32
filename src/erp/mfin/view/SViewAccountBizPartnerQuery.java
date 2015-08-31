/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.view;

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
public class SViewAccountBizPartnerQuery extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewAccountBizPartnerQuery(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.FINX_ACC_BP_QRY);
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        jbNew.setEnabled(true);
        jbEdit.setEnabled(true);
        //jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[15];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_acc_bp");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "code", "Centro contable", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_stype", "Tipo referencia", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct_bp", "Categoría asociado negocios", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ref", "Referencia", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "f_b_del_ref", "Referencia eliminada", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "acc_bp", "Paquete configuración", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "f_b_del_cab", "Paquete configuración eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "id_dt_start", "Ini. vigencia", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.FIN_ACC_BP);
        mvSuscriptors.add(SDataConstants.BPSU_TP_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSX_BP_CO);
        mvSuscriptors.add(SDataConstants.BPSX_BP_SUP);
        mvSuscriptors.add(SDataConstants.BPSX_BP_CUS);
        mvSuscriptors.add(SDataConstants.BPSX_BP_CDR);
        mvSuscriptors.add(SDataConstants.BPSX_BP_DBR);
        mvSuscriptors.add(SDataConstants.BPSX_BP_EMP);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(SDataConstants.FIN_ACC_BP, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(SDataConstants.FIN_ACC_BP);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(SDataConstants.FIN_ACC_BP, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(SDataConstants.FIN_ACC_BP);
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
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "(r.b_del = FALSE AND cab.b_del = FALSE AND ab.b_del = FALSE) ";
            }
        }

        msSql = "SELECT 1 AS f_ntype, 'ASOCIADO DE NEGOCIOS' AS f_stype, ab.id_ct_bp, ab.id_bp AS f_id_ref, " +
                "ab.id_bkc, ab.id_acc_bp, ab.id_dt_start, bkc.code, cb.ct_bp, " +
                "r.bp AS f_ref, r.b_del AS f_b_del_ref, cab.acc_bp, cab.b_del AS f_b_del_cab, " +
                "ab.b_del, ab.ts_new, ab.ts_edit, ab.ts_del, un.usr AS un, ue.usr AS ue, ud.usr AS ud " +
                "FROM fin_acc_bp_bp AS ab " +
                "INNER JOIN fin_acc_bp AS cab ON ab.id_acc_bp = cab.id_acc_bp " +
                "INNER JOIN erp.bpss_ct_bp AS cb ON ab.id_ct_bp = cb.id_ct_bp " +
                "INNER JOIN erp.bpsu_bp AS r ON ab.id_bp = r.id_bp " +
                "INNER JOIN fin_bkc AS bkc ON ab.id_bkc = bkc.id_bkc " +
                "INNER JOIN erp.usru_usr AS un ON ab.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON ab.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON ab.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "UNION " +
                "SELECT 2 AS f_ntype, 'TIPO ASOCIADO DE NEGOCIOS' AS f_stype, ab.id_ct_bp, ab.id_tp_bp AS f_id_ref, " +
                "ab.id_bkc, ab.id_acc_bp, ab.id_dt_start, bkc.code, cb.ct_bp, " +
                "r.tp_bp AS f_ref, r.b_del AS f_b_del_ref, cab.acc_bp, cab.b_del AS f_b_del_cab, " +
                "ab.b_del, ab.ts_new, ab.ts_edit, ab.ts_del, un.usr AS un, ue.usr AS ue, ud.usr AS ud " +
                "FROM fin_acc_bp_tp_bp AS ab " +
                "INNER JOIN fin_acc_bp AS cab ON ab.id_acc_bp = cab.id_acc_bp " +
                "INNER JOIN erp.bpss_ct_bp AS cb ON ab.id_ct_bp = cb.id_ct_bp " +
                "INNER JOIN erp.bpsu_tp_bp AS r ON ab.id_ct_bp = r.id_ct_bp AND ab.id_tp_bp = r.id_tp_bp " +
                "INNER JOIN fin_bkc AS bkc ON ab.id_bkc = bkc.id_bkc " +
                "INNER JOIN erp.usru_usr AS un ON ab.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON ab.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON ab.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY f_ntype, code, id_bkc, id_ct_bp, f_ref, f_id_ref, acc_bp, id_acc_bp, id_dt_start ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
        }
    }
}

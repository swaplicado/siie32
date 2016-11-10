/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
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
public class SViewBizPartnerCategory extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewBizPartnerCategory(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.BPSU_BP_CT, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i;

        jbNew.setEnabled(false);
        jbEdit.setEnabled(true);
        jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[16];

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bct.id_bp");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bct.id_ct_bp");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Asociado negocios", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bct.bp_key", "Clave", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b.b_del", "Eliminado (asociado negocios)", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bct.b_cred_usr", "Config. manual crédito", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_tp_cred", "Tipo crédito", 150);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_cred_lim", "Límite crédito $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererNumberDouble());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_days_cred", "Días créd.", STableConstants.WIDTH_NUM_INTEGER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_days_grace", "Días gracia", STableConstants.WIDTH_NUM_INTEGER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_tp_risk", "Tipo riesgo", 150);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "bct.garnt", "Monto garantía $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererNumberDouble());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bct.b_garnt_prc", "En trámite (garantía)", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "bct.insur", "Monto seguro $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererNumberDouble());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bct.b_insur_prc", "En trámite (seguro)", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ta.tp_cfd_add", "Tipo addenda CFD", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "btp.tp_bp", "Tipo asoc. neg.", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bct.b_del", "Eliminado (categoría)", STableConstants.WIDTH_BOOLEAN);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSX_BP_SUP);
        mvSuscriptors.add(SDataConstants.BPSX_BP_CUS);

        populateTable();
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : " AND ") + "b.b_del = 0 AND bct.b_del = 0 ";
            }
        }

        msSql = "SELECT bct.id_bp, bct.id_ct_bp, bct.bp_key, bct.b_cred_usr, bct.b_del, b.id_bp, b.bp, b.b_del, " +
                "IF(bct.b_cred_usr, bct.cred_lim, btp.cred_lim) AS f_cred_lim, " +
                "IF(bct.b_cred_usr, bct.days_cred, btp.days_cred) AS f_days_cred, " +
                "IF(bct.b_cred_usr, bct.days_grace, btp.days_grace) AS f_days_grace, " +
                "IF(bct.b_cred_usr, tcct.tp_cred, tctp.tp_cred) AS f_tp_cred, " +
                "IF(bct.b_cred_usr, trct.name, trtp.name) AS f_tp_risk, " +
                "bct.garnt, bct.insur, bct.b_garnt_prc, bct.b_insur_prc, " +
                "btp.id_ct_bp, btp.id_tp_bp, btp.tp_bp, " +
                "ta.id_tp_cfd_add, ta.tp_cfd_add " +
                "FROM erp.bpsu_bp AS b " +
                "INNER JOIN erp.bpsu_bp_ct AS bct ON " +
                "b.id_bp = bct.id_bp AND bct.id_ct_bp = " + mnTabTypeAux01 + " " +
                "INNER JOIN erp.bpss_tp_cfd_add AS ta ON " +
                "bct.fid_tp_cfd_add = ta.id_tp_cfd_add " +
                "INNER JOIN erp.bpsu_tp_bp AS btp ON " +
                "bct.fid_ct_bp = btp.id_ct_bp AND bct.fid_tp_bp = btp.id_tp_bp " +
                "INNER JOIN erp.bpss_tp_cred AS tctp ON " +
                "btp.fid_tp_cred = tctp.id_tp_cred " +
                "INNER JOIN erp.bpss_risk AS trtp ON " +
                "btp.fid_risk = trtp.id_risk " +
                "LEFT OUTER JOIN erp.bpss_tp_cred AS tcct ON " +
                "bct.fid_tp_cred_n = tcct.id_tp_cred " +
                "LEFT OUTER JOIN erp.bpss_risk AS trct ON " +
                "bct.fid_risk_n = trct.id_risk " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY b.bp, bct.id_bp, bct.id_ct_bp ";
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
                if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showForm(mnTabTypeAux01 == SDataConstantsSys.BPSS_CT_BP_SUP ? SDataConstants.BPSX_BP_SUP : SDataConstants.BPSX_BP_CUS,
                        new int[] { ((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[0] }) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabType);
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

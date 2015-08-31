/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

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
 * @author Alfonso Flores, Sergio Flores
 */
public class SViewDncDocumentNumberSeries extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewDncDocumentNumberSeries(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, auxType01, auxType02);
        mnTabTypeAux01 = auxType01;
        mnTabTypeAux02 = auxType02;
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

        STableField[] aoKeyFields = new STableField[3];
        STableColumn[] aoTableColumns = null;

        if (mnTabTypeAux01 == SDataConstants.TRN_DNC_DPS_DNS) {
            aoTableColumns = new STableColumn[17];
        }
        else {
            aoTableColumns = new STableColumn[13];
        }

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_dnc");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_dns");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_num");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dnc.dnc", "Centro foliado", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cl", "Clase documento", 150);
        if (mnTabTypeAux01 == SDataConstants.TRN_DNC_DPS_DNS) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_tp", "Tipo documento", 150);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "s.dns", "Serie folios", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "s.b_del", "Eliminado (serie folios)", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "d.num_start", "Folio inicial", STableConstants.WIDTH_NUM_INTEGER);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererSimpleInteger());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "d.num_end_n", "Folio final", STableConstants.WIDTH_NUM_INTEGER);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererSimpleInteger());
        if (mnTabTypeAux01 == SDataConstants.TRN_DNC_DPS_DNS) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.approve_dt_n", "Fecha aprobación folios", STableConstants.WIDTH_DATE);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "d.approve_year_n", "Año aprobación folios", STableConstants.WIDTH_NUM_INTEGER);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererSimpleInteger());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "d.approve_num_n", "No. aprobación folios", STableConstants.WIDTH_NUM_INTEGER);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererSimpleInteger());
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
        mvSuscriptors.add(SDataConstants.TRN_DNC_DPS);
        mvSuscriptors.add(SDataConstants.TRN_DNC_DIOG);
        mvSuscriptors.add(SDataConstants.TRN_DNS_DPS);
        mvSuscriptors.add(SDataConstants.TRN_DNS_DIOG);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    private boolean isPurchase() {
        boolean isPurchase = false;

        if (mnTabTypeAux01 == SDataConstants.TRN_DNC_DPS_DNS && mnTabTypeAux02 == SDataConstantsSys.TRNS_CT_DPS_PUR ||
            mnTabTypeAux01 == SDataConstants.TRN_DNC_DIOG_DNS && mnTabTypeAux02 == SDataConstantsSys.TRNS_CT_IOG_IN) {
            isPurchase = true;
        }

        return isPurchase;
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        String fieldClass = "";
        String fieldClassId = "";
        String fieldType = "";
        String fieldTypeId = "";
        String fieldApprove = "";
        String table = "";
        String innerJoin = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.b_del = FALSE ";
            }
        }

        switch (mnTabTypeAux01) {
            case SDataConstants.TRN_DNC_DPS_DNS:
                fieldClass = "c.cl_dps";
                fieldClassId = "c.id_cl_dps";
                fieldType = "t.tp_dps";
                fieldTypeId = "t.id_tp_dps";
                fieldApprove = "d.approve_year_n, d.approve_num_n, d.approve_dt_n, ";
                table = "trn_dnc_dps_dns ";
                innerJoin = "INNER JOIN trn_dnc_dps AS dnc ON d.id_dnc = dnc.id_dnc " +
                        "INNER JOIN trn_dns_dps AS s ON d.id_dns = s.id_dns " +
                        "INNER JOIN erp.trnu_tp_dps AS t ON s.fid_ct_dps = t.id_ct_dps AND s.fid_cl_dps = t.id_cl_dps AND s.fid_tp_dps = t.id_tp_dps AND " +
                        "t.id_ct_dps = " + (isPurchase() ? SDataConstantsSys.TRNS_CT_DPS_PUR : SDataConstantsSys.TRNS_CT_DPS_SAL) + " " +
                        "INNER JOIN erp.trns_cl_dps AS c ON t.id_ct_dps = c.id_ct_dps AND t.id_cl_dps = c.id_cl_dps ";
                break;

            case SDataConstants.TRN_DNC_DIOG_DNS:
                fieldClass = "c.cl_iog";
                fieldClassId = "c.id_cl_iog";
                table = "trn_dnc_diog_dns ";
                innerJoin = "INNER JOIN trn_dnc_diog AS dnc ON d.id_dnc = dnc.id_dnc " +
                        "INNER JOIN trn_dns_diog AS s ON d.id_dns = s.id_dns " +
                        "INNER JOIN erp.trns_cl_iog AS c ON s.fid_ct_iog = c.id_ct_iog AND s.fid_cl_iog = c.id_cl_iog AND " +
                        "c.id_ct_iog = " +  (isPurchase() ? SDataConstantsSys.TRNS_CT_IOG_IN : SDataConstantsSys.TRNS_CT_IOG_OUT) + " ";
                break;

            default:
        }

        msSql = "SELECT d.id_dnc, d.id_dns, d.id_num, d.num_start, d.num_end_n, d.b_del, " +
                fieldApprove + "dnc.dnc, s.dns, s.b_del, " +
                fieldClassId + " AS f_id_cl, " + fieldClass + " AS f_cl, " +
                (mnTabTypeAux01 != SDataConstants.TRN_DNC_DPS_DNS ? "" : fieldTypeId + " AS f_id_tp, " + fieldType + " AS f_tp, ") +
                "d.ts_new, d.ts_edit, d.ts_del, un.usr, ue.usr, ud.usr " +
                "FROM " + table + "AS d " + innerJoin +
                "INNER JOIN erp.usru_usr AS un ON " +
                "d.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "d.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "d.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY dnc.dnc, dnc.id_dnc, f_id_cl, f_cl, " +
                (mnTabTypeAux01 != SDataConstants.TRN_DNC_DPS_DNS ? "" : "f_id_tp, f_tp, ") +
                "s.dns ";
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(isPurchase() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(isPurchase() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(isPurchase() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(isPurchase() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
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

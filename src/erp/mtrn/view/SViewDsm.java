/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import javax.swing.JButton;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.lib.SLibConstants;
import erp.lib.data.SDataSqlUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SViewDsm extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;

    public SViewDsm(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRN_DSM, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);

        jbNew.setEnabled(true);
        jbEdit.setEnabled(true);
        //jbDelete.setEnabled(true);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[18];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bpb", "Sucursal empresa", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.ct_bp", "Categoría", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.concept", "Concepto", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_audit", "Auditado", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_authorn", "Autorizado", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_sys", "Sistema", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uad.usr", "Usr. auditoria", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_audit", "Auditoria", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uad.usr", "Usr. autorizado", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_authorn", "Autorizado", STableConstants.WIDTH_DATE_TIME);
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
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(
                    (mnTabTypeAux01 == SDataConstantsSys.BPSS_CT_BP_SUP ? SDataConstants.MOD_PUR :
                    mnTabTypeAux01 == SDataConstantsSys.BPSS_CT_BP_CUS ? SDataConstants.MOD_SAL
                    : SDataConstants.MOD_FIN)).showForm(mnTabType, mnTabTypeAux01, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(
                    (mnTabTypeAux01 == SDataConstantsSys.BPSS_CT_BP_SUP ? SDataConstants.MOD_PUR :
                    mnTabTypeAux01 == SDataConstantsSys.BPSS_CT_BP_CUS ? SDataConstants.MOD_SAL
                    : SDataConstants.MOD_FIN)).refreshCatalogues(mnTabType);
            }
	}
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (miClient.getGuiModule(
                    (mnTabTypeAux01 == SDataConstantsSys.BPSS_CT_BP_SUP ? SDataConstants.MOD_PUR :
                    mnTabTypeAux01 == SDataConstantsSys.BPSS_CT_BP_CUS ? SDataConstants.MOD_SAL
                    : SDataConstants.MOD_FIN)).showForm(mnTabType, mnTabTypeAux01, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {

                    miClient.getGuiModule(
                        (mnTabTypeAux01 == SDataConstantsSys.BPSS_CT_BP_SUP ? SDataConstants.MOD_PUR :
                        mnTabTypeAux01 == SDataConstantsSys.BPSS_CT_BP_CUS ? SDataConstants.MOD_SAL
                        : SDataConstants.MOD_FIN)).refreshCatalogues(mnTabType);
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
        String sqlWhere = "d.fid_ct_dsm = " + mnTabTypeAux01 + " ";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.b_del = FALSE ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "d.dt");
            }
        }

        msSql = "SELECT d.*, c.ct_bp, b.bpb, un.usr, ue.usr, ud.usr, uad.usr, uat.usr " +
                "FROM trn_dsm AS d " +
                    "INNER JOIN erp.bpss_ct_bp AS c ON " +
                        "d.fid_ct_dsm = c.id_ct_bp " +
                    "INNER JOIN erp.bpsu_bpb AS b ON " +
                        "d.fid_cob = b.id_bpb " +
                    "INNER JOIN erp.usru_usr AS uad ON " +
                        "d.fid_usr_audit = uad.id_usr " +
                    "INNER JOIN erp.usru_usr AS uat ON " +
                        "d.fid_usr_audit = uat.id_usr " +
                    "INNER JOIN erp.usru_usr AS un ON " +
                        "d.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON " +
                        "d.fid_usr_edit = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON " +
                        "d.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY b.bpb, c.ct_bp, id_year, id_doc ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

        }
    }
}

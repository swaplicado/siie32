/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SViewCustomerConfiguration extends erp.lib.table.STableTab {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewCustomerConfiguration(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.MKT_CFG_CUS);
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[19];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_cus");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp1.bp", "Cliente", 250);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp.tp_cus", "Tipo cliente", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sg.mkt_segm", "Segmento mercado", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "su.mkt_segm_sub", "Subsegmento mercado", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dis.dist_chan", "Canal distribución", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uan.usr", "Analista AN", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Agente ventas", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bps.bp", "Supervisor ventas", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "c.b_sign_restrict", "Restringido al timbrar CFDI", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "c.b_free_disc_doc", "S/descuento", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "c.b_free_comms", "S/comisiones", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "bp1.b_del", "Asoc. negocios eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ct.b_del", "Categoría eliminada", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSX_BP_CUS);
        mvSuscriptors.add(SDataConstants.MKTU_TP_CUS);
        mvSuscriptors.add(SDataConstants.MKTU_MKT_SEGM);
        mvSuscriptors.add(SDataConstants.MKTU_MKT_SEGM_SUB);
        mvSuscriptors.add(SDataConstants.MKTU_DIST_CHAN);
        mvSuscriptors.add(SDataConstants.BPSX_BP_ATT_SAL_AGT);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_MKT).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_MKT).refreshCatalogues(mnTabType);
            }
	}

    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_MKT).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_MKT).refreshCatalogues(mnTabType);
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
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "bp1.b_del = FALSE AND ct.b_del = FALSE ";
            }
        }

        msSql = "SELECT bp1.bp, c.id_cus, c.b_sign_restrict, c.b_free_disc_doc, c.b_free_comms, bp1.b_del, ct.b_del, ct.fid_usr_ana_n, " +
                    "tp.tp_cus, bp.bp, bps.bp, sg.mkt_segm, su.mkt_segm_sub, dis.dist_chan, c.fid_usr_edit, c.ts_new, c.ts_edit, c.ts_del, " +
                    "un.usr, ue.usr, ud.usr, uan.usr " +
                    "FROM mkt_cfg_cus AS c " +
                    "INNER JOIN erp.bpsu_bp AS bp1 ON c.id_cus = bp1.id_bp " +
                    "INNER JOIN erp.bpsu_bp_ct AS ct ON bp1.id_bp = ct.id_bp AND ct.fid_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_CUS + " " +
                    "INNER JOIN mktu_tp_cus AS tp ON c.fid_tp_cus = tp.id_tp_cus " +
                    "INNER JOIN mktu_mkt_segm AS sg ON c.fid_mkt_segm = sg.id_mkt_segm " +
                    "INNER JOIN mktu_mkt_segm_sub AS su ON c.fid_mkt_segm = su.id_mkt_segm AND c.fid_mkt_sub = su.id_mkt_sub " +
                    "INNER JOIN mktu_dist_chan AS dis ON c.fid_dist_chan = dis.id_dist_chan " +
                    "INNER JOIN erp.usru_usr AS un ON c.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON c.fid_usr_edit = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON c.fid_usr_del = ud.id_usr " +
                    "LEFT OUTER JOIN erp.usru_usr AS uan ON ct.fid_usr_ana_n = uan.id_usr " +
                    "LEFT OUTER JOIN erp.bpsu_bp AS bp ON c.fid_sal_agt_n = bp.id_bp " +
                    "LEFT OUTER JOIN erp.bpsu_bp AS bps ON c.fid_sal_sup_n = bps.id_bp " +
                (sqlWhere.length() == 0 ? "" : "WHERE bp1.b_del = 0 AND " + sqlWhere) +
                "ORDER BY bp1.bp, tp.tp_cus; ";
    }
}

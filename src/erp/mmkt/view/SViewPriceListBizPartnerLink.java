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
import erp.mod.SModSysConsts;
import javax.swing.JButton;

/**
 *
 * @author Néstor Ávalos, Uriel Castañeda
 */
public class SViewPriceListBizPartnerLink extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewPriceListBizPartnerLink(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.MKT_PLIST_CUS, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        jbNew.setEnabled(true);
        jbEdit.setEnabled(true);
        jbDelete.setEnabled(false);

        int cols = mnTabTypeAux01 == SModSysConsts.BPSS_LINK_BPB ? 13 : 12;
        STableField[] aoKeyFields = new STableField[4];
        STableColumn[] aoTableColumns = new STableColumn[cols];    

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_link");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_ref_1");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_ref_2");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.id_dt_start");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_link",
           (mnTabTypeAux01 == SModSysConsts.BPSS_LINK_BP || mnTabTypeAux01 == SModSysConsts.BPSS_LINK_BPB ? (mnTabTypeAux02 == SDataConstantsSys.BPSS_CT_BP_CUS ? "Cliente" : "Proveedor") :
            mnTabTypeAux01 == SModSysConsts.BPSS_LINK_CUS_MKT_TP ? "Tipo cliente" :
            mnTabTypeAux01 == SModSysConsts.BPSS_LINK_BP_TP ? "Tipo asoc. negocios" : ""), 300);
        if (mnTabTypeAux01 == SModSysConsts.BPSS_LINK_BPB) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal asociado", 150);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "c.id_dt_start", "Ini. vigencia", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "p.plist", "Lista precios", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tpd.tp_disc_app", "Tipo descuento", 150);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "c.disc_per", "Descuento", STableConstants.WIDTH_PERCENTAGE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "c.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSX_BP_CUS);
        mvSuscriptors.add(SDataConstants.MKT_PLIST);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            miClient.getGuiModule(SDataConstants.MOD_MKT).setFormComplement(mnTabTypeAux02);
            if (miClient.getGuiModule(SDataConstants.MOD_MKT).showForm(mnTabType, mnTabTypeAux01, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_MKT).refreshCatalogues(mnTabType);
            }
	}
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            miClient.getGuiModule(SDataConstants.MOD_MKT).setFormComplement(mnTabTypeAux02);
            if (miClient.getGuiModule(SDataConstants.MOD_MKT).showForm(mnTabType, mnTabTypeAux01, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
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
        String sqlWhere =  (mnTabTypeAux01 == SModSysConsts.BPSS_LINK_BP || mnTabTypeAux01 == SModSysConsts.BPSS_LINK_BPB ? "bct.id_ct_bp = " + mnTabTypeAux02 + " AND " : "") +
                "c.id_link = " + mnTabTypeAux01 + " ";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "c.b_del = FALSE ";
            }
        }

        msSql = "SELECT c.*, " + (
                (mnTabTypeAux01 == SModSysConsts.BPSS_LINK_CUS_MKT_TP) ? "ct.tp_cus" :
                (mnTabTypeAux01 == SModSysConsts.BPSS_LINK_BP_TP) ? "tp.tp_bp" :
                (mnTabTypeAux01 == SModSysConsts.BPSS_LINK_BP || mnTabTypeAux01 == SModSysConsts.BPSS_LINK_BPB ) ? "bp.bp" : "") + 
                " AS f_link, " + (mnTabTypeAux01 == SModSysConsts.BPSS_LINK_BPB ? "bpb.bpb, " : "") + "p.plist, tpd.tp_disc_app, un.usr, ue.usr, ud.usr " +
                "FROM mkt_plist_bp_link AS c " +
                (mnTabTypeAux01 == SModSysConsts.BPSS_LINK_CUS_MKT_TP ?
                "INNER JOIN mktu_tp_cus AS ct ON " +
                "c.id_ref_1 = ct.id_tp_cus " :
                mnTabTypeAux01 == SModSysConsts.BPSS_LINK_BP_TP ?
                "INNER JOIN erp.bpsu_tp_bp AS tp ON " +
                "c.id_ref_1 = tp.id_ct_bp AND c.id_ref_2 = tp.id_tp_bp " :
                mnTabTypeAux01 == SModSysConsts.BPSS_LINK_BP ?
                "INNER JOIN erp.bpsu_bp AS bp ON " +
                "c.id_ref_1 = bp.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bct ON " +
                "bp.id_bp = bct.id_bp AND bct.id_ct_bp = " + mnTabTypeAux02 + " " :
                mnTabTypeAux01 == SModSysConsts.BPSS_LINK_BPB ? 
                "INNER JOIN erp.bpsu_bpb AS bpb ON c.id_ref_2 = bpb.id_bpb " +
                "INNER JOIN erp.bpsu_bp AS bp ON c.id_ref_1 = bp.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bct ON bp.id_bp = bct.id_bp " : "") + " " +
                "INNER JOIN mkt_plist AS p ON " +
                "c.fid_plist = p.id_plist AND p.fid_ct_dps = " + (mnTabTypeAux02 == SDataConstantsSys.BPSS_CT_BP_CUS ? SDataConstantsSys.TRNS_CT_DPS_SAL : SDataConstantsSys.TRNS_CT_DPS_PUR) + " " +
                "INNER JOIN erp.mkts_tp_disc_app tpd ON " +
                "c.fid_tp_disc_app = tpd.id_tp_disc_app " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "c.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "c.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "c.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY f_link, c.id_dt_start ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.mod.cfg.db.SSyncType;
import erp.mod.cfg.swap.SHttpConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.utils.SExportUtils;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
import sa.lib.grid.SGridUtils;

/**
 *
 * @author Claudio Peña, Sergio Flores
 */
public class SViewBizPartnerUpdate extends erp.lib.table.STableTab implements java.awt.event.ActionListener {
    
    private javax.swing.JButton jbExportDataToSwapServices;
    
    private int mnBizPartnerCategory;
    private java.lang.String msOrderKey;
    
    private boolean mbSwapServicesLinkUp;
    
    public SViewBizPartnerUpdate(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.BPSX_BP_UPD, auxType01);
        initComponents();
    }

    private void initComponents() {
        boolean canEditSupplier = false;
        int levelRightEditCategory = 0;

        switch (mnTabTypeAux01) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_SUP;
                canEditSupplier = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD).HasRight;
                levelRightEditCategory = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD).Level;
                break;
            default:
                // nothing
        }
                
        jbNew.setEnabled(false);
        jbEdit.setEnabled(canEditSupplier && levelRightEditCategory >= SUtilConsts.LEV_AUTHOR);
        jbDelete.setEnabled(false);
        
        // Enable SWAP Services:
        mbSwapServicesLinkUp = (boolean) miClient.getSwapServicesSetting(SSwapConsts.CFG_NVP_LINK_UP);
        if (mbSwapServicesLinkUp) {
            jbExportDataToSwapServices = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_up_ind.gif")), "Exportar proveedores a " + SSwapConsts.SWAP_SERVICES, this);

            addTaskBarUpperSeparator();
            addTaskBarUpperComponent(jbExportDataToSwapServices);
        }
             
        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[mbSwapServicesLinkUp ? 14 : 11];

        int i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bp.id_bp");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bp_ct.id_ct_bp");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        
        if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.bp_key", "Clave", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm", "Nombre comercial", 200);
            msOrderKey = "bp_ct.bp_key, bp.bp_comm, bp.bp, bp.id_bp ";
        }
        else if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_NAME_KEY) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.bp_key", "Clave", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm", "Nombre comercial", 200);
            msOrderKey = "bp.bp, bp_ct.bp_key, bp.bp_comm, bp.id_bp ";
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm", "Nombre comercial", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.bp_key", "Clave", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
            msOrderKey = "bp.bp_comm, bp_ct.bp_key, bp.bp, bp.id_bp ";
        }
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.co_key", "Clave empresa", 100);
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.fiscal_id", "RFC", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.fiscal_frg_id", "ID fiscal", 100);
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpbc.email_01", "Cuentas(s) correo-e", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.lead_time", "Plazo entrega (días)", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.tax_regime", "Régimen fiscal CFDI", 50);
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ulog.usr", "Usr. actualización", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "tlog.ts_usr_upd", "Actualización", STableConstants.WIDTH_DATE_TIME);
        
        if (mbSwapServicesLinkUp) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "_ss_is_exp", SSwapConsts.SWAP_SERVICES + " exportado",  STableConstants.WIDTH_BOOLEAN_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tss._ss_usr", SSwapConsts.SWAP_SERVICES + " usr. últ. exportación", STableConstants.WIDTH_USER);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "tss._ss_resp_ts", SSwapConsts.SWAP_SERVICES + " últ. exportación", STableConstants.WIDTH_DATE_TIME);
        }

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        if (mnBizPartnerCategory == SDataConstantsSys.BPSS_CT_BP_SUP) {
            mvSuscriptors.add(SDataConstants.BPSX_BP_SUP);
        }

        populateTable();
    }
    
    private void actionExportDataToSwapServices() {
        if (jbExportDataToSwapServices != null && jbExportDataToSwapServices.isEnabled()) {
            try {
                String response = SExportUtils.exportData(miClient.getSession(), SSyncType.PARTNER_SUPPLIER);
                
                if (response.isEmpty()) {
                    miClient.showMsgBoxInformation("Los proveedores fueron exportados correctamente a " + SSwapConsts.SWAP_SERVICES + ".");
                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabType);
                }
                else {
                    miClient.showMsgBoxInformation("Ocurrió un problema al exportar los provedores a " + SSwapConsts.SWAP_SERVICES + ":\n" + response);
                }
            }
            catch (Exception e) {
                SLibUtilities.printOutException(this, e);
            }
        }
    }

    @Override
    public void createSqlQuery() {
        msSql = "SELECT bp.id_bp, bp.bp, bp.bp_comm, bp.fiscal_id, bp.fiscal_frg_id, " +
                "bp_ct.id_ct_bp, bp_ct.bp_key, bp_ct.co_key, bp_ct.lead_time, bp_ct.tax_regime, bpbc.email_01, " +
                "ulog.usr, tlog.ts_usr_upd" +
                (!mbSwapServicesLinkUp ? "" : ", tss.reference_id IS NOT NULL AS _ss_is_exp, tss._ss_usr, tss._ss_resp_ts") + " " +
                "FROM erp.bpsu_bp AS bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bp_ct ON bp_ct.id_bp = bp.id_bp " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON bpb.fid_bp = bp.id_bp AND bpb.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " " +
                "INNER JOIN erp.bpsu_bpb_con AS bpbc ON bpbc.id_bpb = bpb.id_bpb AND bpbc.id_con = " + SUtilConsts.BRA_CON_ID + " " +
                "/* Business Partner Update Log: */ " +
                "LEFT OUTER JOIN (" +
                    "SELECT bul.id_bp, bul.fk_usr_upd, bul.ts_usr_upd " +
                    "FROM erp.bpsu_bp_upd_log AS bul " +
                    "INNER JOIN (" +
                        "SELECT id_bp, MAX(id_log) AS id_log " +
                        "FROM erp.bpsu_bp_upd_log " +
                        "GROUP BY id_bp ORDER BY id_bp" +
                    ") AS tlogx ON tlogx.id_bp = bul.id_bp AND tlogx.id_log = bul.id_log " +
                ") AS tlog ON tlog.id_bp = bp.id_bp " +
                "LEFT OUTER JOIN erp.usru_usr AS ulog ON ulog.id_usr = tlog.fk_usr_upd " +
                (!mbSwapServicesLinkUp ? "" : "/* SWAP Services Sync Log: */ " +
                "LEFT OUTER JOIN (SELECT sle.reference_id, u.usr AS _ss_usr, MAX(sl.response_timestamp) AS _ss_resp_ts " +
                "FROM erp.cfg_sync_log AS sl " +
                "INNER JOIN erp.cfg_sync_log_ety AS sle ON sle.id_sync_log = sl.id_sync_log " +
                "INNER JOIN erp.usru_usr AS u ON u.id_usr = sl.fk_usr " +
                "WHERE sl.sync_type = '" + SSyncType.PARTNER_SUPPLIER + "' " +
                "AND (sle.response_code = '" + SHttpConsts.RSC_SUCC_OK + "' OR sle.response_code = '" + SHttpConsts.RSC_SUCC_CREATED + "') " +
                "GROUP BY sle.reference_id, u.usr " +
                "ORDER BY CONVERT(sle.reference_id, UNSIGNED)) AS tss ON tss.reference_id = CONVERT(bp.id_bp, CHAR) ") +
                "WHERE NOT bp.b_del AND NOT bp_ct.b_del " +
                "AND bp_ct.id_ct_bp = " + mnBizPartnerCategory + " "+ 
                "ORDER BY " + msOrderKey;
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
                if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showForm(mnTabType, mnTabTypeAux01, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
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
            if (button == jbExportDataToSwapServices) {
                actionExportDataToSwapServices();
            }
        }
    }
}

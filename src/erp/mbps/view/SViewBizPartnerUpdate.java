/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.mcfg.data.SCfgUtils;
import erp.mod.cfg.db.SSyncType;
import erp.mod.cfg.swapms.utils.SExportUtils;
import erp.mod.cfg.swapms.utils.SSwapConsts;
import erp.musr.view.SViewUser;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
import sa.lib.grid.SGridUtils;

/**
 *
 * @author Claudio Peña
 */
public class SViewBizPartnerUpdate extends erp.lib.table.STableTab implements java.awt.event.ActionListener {
    private int formCancel = 0;
    private int supplierMatrix = 1;
    private int supplierType = 2;
    private javax.swing.JButton jbExportDataToSwapServices;
    
    public SViewBizPartnerUpdate(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.BPSU_BP_DT, auxType01);
        initComponents();
    }

    private void initComponents() {
        boolean canEditSupplier = false;
        int levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP).Level;
        int levelRightEditCategory = 0;

        switch (mnTabTypeAux01) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
                canEditSupplier = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD).HasRight;
                levelRightEditCategory = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD).Level;
                break;
            default:
        }
                
        jbNew.setEnabled(false);
        jbEdit.setEnabled(canEditSupplier && (levelRightEditCategory >= SUtilConsts.LEV_AUTHOR || levelRightEdit >= SUtilConsts.LEV_AUTHOR) && mnTabTypeAux01 != SDataConstants.BPSU_BP);
        jbDelete.setEnabled(false);
        
        try {
            String paramValue = SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG);
            
            if (!paramValue.isEmpty()) {
                jbExportDataToSwapServices = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_up_ora.gif")), "Exportar proveedores a " + SSwapConsts.SWAP_SERVICES, this);

                addTaskBarUpperSeparator();
                addTaskBarUpperComponent(jbExportDataToSwapServices);
            }
        }
        catch (Exception e) {
            Logger.getLogger(SViewUser.class.getName()).log(Level.SEVERE, null, e);
        }
             
        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[11];

        int i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "b.id_bp");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bct.id_ct_bp");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Asociado negocios", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.id_bp", "Clave", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.fiscal_id", "RFC", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp_comm", "Nombre comercial", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "con.email_01", "Cuentas(s) correo-e", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bct.tax_regime", "Régimen fiscal", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bct.lead_time", "Plazo entrega (Días)", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "sync_status", "Exportado a portal)",  STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "sync_timestamp", "Fecha de exportación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.usr", "Usr. actualización", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "lg.ts_usr_upd", "Actualización", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSX_BP_SUP);
        mvSuscriptors.add(SDataConstants.BPSX_BP_CUS);

        populateTable();
    }
    
    private void actionExportDataToSwapServices() {
        if (jbExportDataToSwapServices != null && jbExportDataToSwapServices.isEnabled()) {
            boolean syncAll = false;
            
            try {
                String response = SExportUtils.exportJsonData(miClient.getSession(), SSyncType.PARTNER_SUPPLIER, syncAll);
                
                if (response.isEmpty()) {
                    miClient.showMsgBoxInformation("Los proveedores fueron exportados correctamente a " + SSwapConsts.SWAP_SERVICES + ".");
                }
                else {
                    miClient.showMsgBoxInformation("Ocurrió un problema al exportar los provedores " + SSwapConsts.SWAP_SERVICES + ":\n" + response);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void createSqlQuery() {
        msSql = "SELECT bct.id_ct_bp, b.id_bp, b.bp, b.fiscal_id, b.bp_comm, " +
                "con.email_01, bct.tax_regime, bct.lead_time, u.usr, lg.ts_usr_upd, " +
                "syl.ts_sync AS sync_timestamp, " +
                "CASE WHEN syl.reference_id IS NOT NULL THEN 1 " +
                "ELSE 0 END AS sync_status " +
                "FROM erp.bpsu_bp AS b " +
                "INNER JOIN erp.bpsu_bpb AS bp ON bp.fid_bp = b.id_bp " +
                "INNER JOIN erp.BPSU_BPB_CON AS con ON con.id_bpb = bp.id_bpb " +
                "INNER JOIN erp.BPSU_BP_CT AS bct ON bct.id_bp = b.id_bp " +
                "LEFT JOIN ( SELECT l1.* FROM erp.bpsu_bp_upd_log l1 " +
                "INNER JOIN (SELECT id_bp, MAX(ts_usr_upd) AS ts_usr_upd " +
                "FROM erp.bpsu_bp_upd_log GROUP BY id_bp ) l2 ON l1.id_bp = l2.id_bp AND l1.ts_usr_upd = l2.ts_usr_upd " +
                ") AS lg ON lg.id_bp = b.id_bp " +
                "LEFT OUTER JOIN erp.usru_usr AS u ON u.id_usr = lg.fk_usr_upd " +
                "LEFT JOIN (SELECT syl.reference_id, syl.ts_sync " +
                "FROM erp.cfg_sync_log_ety AS syl " +
                "INNER JOIN erp.cfg_sync_log AS sy ON sy.id_sync_log = syl.id_sync_log " +
                "WHERE syl.response_code IN ('200', '201') AND sy.id_sync_log = " + supplierType + " " + 
                ") AS syl ON syl.reference_id = b.id_bp " +
                "WHERE NOT b.b_del " +
                "AND bp.fid_tp_bpb = " + supplierMatrix + " " +
                "AND con.id_con = " + supplierMatrix + " "+
                "AND bct.id_ct_bp = " + supplierType + " "+ 
                "GROUP BY b.id_bp " +
                "ORDER BY b.id_bp ";
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
                int result = miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).showForm(
                    mnTabTypeAux01 == SDataConstantsSys.BPSS_CT_BP_SUP ? SDataConstants.BPSX_BP_CUS_SUP : SDataConstants.BPSX_BP_CUS_SUP,
                    new int[] { ((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0] }
                );
                
                if (result == formCancel) {
                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).refreshCatalogues(mnTabType);
                    populateTable();
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

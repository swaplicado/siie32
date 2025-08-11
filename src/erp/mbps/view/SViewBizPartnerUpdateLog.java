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
import javax.swing.JButton;

/**
 *
 * @author Claudio Peña, Sergio Flores
 */
public class SViewBizPartnerUpdateLog extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private int mnBizPartnerCategory;
    private java.lang.String msOrderKey;
    
    public SViewBizPartnerUpdateLog(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.BPSU_BP_UPD_LOG, auxType01);
        initComponents();
    }

    private void initComponents() {
        switch (mnTabTypeAux01) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_SUP;
                break;
            default:
                // nothing
        }
                
        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[13];

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
            msOrderKey = "bp_ct.bp_key, bp.bp_comm, bp.bp, bp.id_bp, bul.id_log ";
        }
        else if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_NAME_KEY) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.bp_key", "Clave", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm", "Nombre comercial", 200);
            msOrderKey = "bp.bp, bp_ct.bp_key, bp.bp_comm, bp.id_bp, bul.id_log ";
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm", "Nombre comercial", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.bp_key", "Clave", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 250);
            msOrderKey = "bp.bp_comm, bp_ct.bp_key, bp.bp, bp.id_bp, bul.id_log ";
        }
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_ct.co_key", "Clave empresa", 100);
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.fiscal_id", "RFC", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.fiscal_frg_id", "ID fiscal", 100);
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "bul.id_log", "No. actualización", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bul.bp_comm", "Nombre comercial bitácora", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bul.email_01", "Cuentas(s) correo-e bitácora", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bul.lead_time", "Plazo entrega (días) bitácora", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bul.tax_regime", "Régimen fiscal CFDI bitácora", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ulog.usr", "Usr. actualización", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "bul.ts_usr_upd", "Actualización", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSX_BP_UPD);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        if (mnBizPartnerCategory == SDataConstantsSys.BPSS_CT_BP_SUP) {
            mvSuscriptors.add(SDataConstants.BPSX_BP_SUP);
        }

        populateTable();
    }

    @Override
    public void createSqlQuery() {
        msSql = "SELECT bp.id_bp, bp.bp, bp.bp_comm, bp.fiscal_id, bp.fiscal_frg_id, " +
                "bp_ct.id_ct_bp, bp_ct.bp_key, bp_ct.co_key, " +
                "bul.id_log, bul.bp_comm, bul.email_01, bul.tax_regime, bul.lead_time, " +
                "bul.ts_usr_upd, ulog.usr " +
                "FROM erp.bpsu_bp AS bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bp_ct ON bp_ct.id_bp = bp.id_bp " +
                "/* Business Partner Update Log Entries: */ " +
                "INNER JOIN erp.bpsu_bp_upd_log AS bul ON bul.id_bp = bp.id_bp " +
                "INNER JOIN erp.usru_usr AS ulog ON ulog.id_usr = bul.fk_usr_upd " +
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

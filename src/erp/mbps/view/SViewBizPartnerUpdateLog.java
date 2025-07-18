/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.view;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import javax.swing.JButton;

/**
 *
 * @author Claudio Peña
 */
public class SViewBizPartnerUpdateLog extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    public SViewBizPartnerUpdateLog(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.BPSU_BP_DT_RE, auxType01);
        initComponents();
    }

    private void initComponents() {
                
        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[10];

        int i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "b.id_bp");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "l.id_log");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Asociado negocios", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.id_bp", "Clave", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.fiscal_id", "RFC", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp_comm", "Nombre comercial", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "l.id_log", "No. actulización", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "l.email_01", "Cuentas(s) correo-e", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "l.tax_regime", "Uso de CFDI", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "l.lead_time", "Plazo entrega (Días)", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.usr", "Usr. actualización", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "l.ts_usr_upd", "Actualización", STableConstants.WIDTH_DATE_TIME);
        
        

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

        msSql = "SELECT l.*, b.bp, b.id_bp, b.fiscal_id, b.bp_comm, u.usr " +
                "FROM erp.bpsu_bp_upd_log AS l " +
                "INNER JOIN erp.bpsu_bp AS b ON l.id_bp = b.id_bp " +
                "INNER JOIN ERP.USRU_USR AS u ON l.fk_usr_upd = u.id_usr " +
                "WHERE b.b_del = 0 ";
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

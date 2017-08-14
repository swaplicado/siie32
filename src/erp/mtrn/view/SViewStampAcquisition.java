/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import javax.swing.JButton;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SViewStampAcquisition extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STableColumn[] aoTableColumns;
    private erp.lib.table.STableField[] aoKeyFields;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;

    public SViewStampAcquisition(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.TRN_SIGN);
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR);
        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);

        jbNew.setEnabled(true);
        jbEdit.setEnabled(true);

        aoKeyFields = new STableField[3];
        aoTableColumns = new STableColumn[5];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "xs.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "xs.id_pac");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "xs.id_mov");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "xs.dt", "Fecha", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "vt.pac", "PAC", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp.tp_xml", "Tipo movimiento", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "xs.mov_in", "Cantidad", STableConstants.WIDTH_NUM_INTEGER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "xs.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_SAL).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_SAL).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
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
        String sqlDatePeriod = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlDatePeriod += (sqlDatePeriod.length() == 0 ? "" : "AND ") + "xs.b_del = FALSE ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlDatePeriod += (sqlDatePeriod.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "xs.dt");
            }
        }

        msSql = "SELECT xs.dt, xs.id_year, xs.id_pac, xs.id_mov, vt.pac, tp.tp_xml, xs.mov_in, xs.b_del " +
                "FROM trn_sign AS xs " +
                "INNER JOIN erp.trns_tp_sign AS tp ON xs.fid_ct_sign = tp.id_ct_sign  AND xs.fid_tp_sign = tp.id_tp_sign " +
                "INNER JOIN trn_pac AS vt ON xs.id_pac = vt.id_pac " +
                "WHERE tp.id_ct_sign = " + SDataConstantsSys.TRNS_TP_SIGN_IN_ACQUIRED[0] + " AND tp.id_tp_sign = " + SDataConstantsSys.TRNS_TP_SIGN_IN_ACQUIRED[1] + " AND " + sqlDatePeriod +
                "ORDER BY xs.dt, xs.id_year, xs.id_pac, xs.id_mov, vt.pac, tp.tp_xml, xs.mov_in, xs.b_del";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

        }
    }
}

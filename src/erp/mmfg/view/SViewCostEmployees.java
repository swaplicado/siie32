/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.view;

import java.util.Date;
import javax.swing.JButton;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.table.STabFilterDatePeriodRange;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.lib.SLibConstants;

/**
 *
 * @author Néstor Ávalos
 */
public class SViewCostEmployees extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;

    public SViewCostEmployees(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.MFGX_COST_EMP);
        initComponents();
    }

    private void initComponents() {
        int i=0;

        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);
        moTabFilterDeleted = new STabFilterDeleted(this);

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriodRange);

        STableField[] aoKeyFields = new STableField[3];
        STableColumn[] aoTableColumns = new STableColumn[7];

        i=0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_tp_cost_obj");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_bp");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i=0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Empleado", 250);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_hr_n", "Hr. normal pend. cierre", 120);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_hr_d", "Hr. doble pend. cierre", 120);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_hr_t", "Hr. triple pend. cierre", 120);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_hr_nc", "Hr. normal cerrada", 120);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_hr_dc", "Hr. doble cerrada", 120);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_hr_tc", "Hr. triple cerrada", 120);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.MFG_COST);
        mvSuscriptors.add(SDataConstants.BPSU_BP);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_MFG).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
            }
	}
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_MFG).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
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
        Date[] range = null;
        int[] period = null;
        String sDateStart = "";
        String sDateEnd = "";
        String sSqlDatePeriod = "";
        String sSqlWhere = "";
        STableSetting oSetting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            oSetting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (oSetting.getType() == STableConstants.SETTING_FILTER_DELETED && oSetting.getStatus() == STableConstants.STATUS_ON) {
                sSqlWhere += (sSqlWhere.length() == 0 ? "" : "AND ") + "c.b_del = FALSE ";
            }
            else if (oSetting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                range = (java.util.Date[])oSetting.getSetting();
                sDateStart = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0]);
                sDateEnd = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]);
                sSqlDatePeriod += " AND c.id_dt BETWEEN '" + sDateStart + "' AND '" + sDateEnd + "' ";
            }
        }

        msSql = "SELECT b.bp, " +
            "(SELECT SUM(cn.qty) FROM mfg_cost AS cn " +
            "WHERE cn.id_bp = c.id_bp AND cn.id_dt BETWEEN '" + sDateStart + "' AND '" + sDateEnd + "' AND cn.id_tp_hr = " + SDataConstantsSys.MFGS_TP_HOUR_NML + " AND cn.b_del = 0 AND cn.b_acc = 0 " +
            "GROUP BY cn.id_bp) AS f_hr_n, " +
            "(SELECT SUM(cd.qty) FROM mfg_cost AS cd " +
            "WHERE cd.id_bp = c.id_bp AND cd.id_dt BETWEEN '" + sDateStart + "' AND '" + sDateEnd + "' AND cd.id_tp_hr = " + SDataConstantsSys.MFGS_TP_HOUR_DBL + " AND cd.b_del = 0 AND cd.b_acc = 0 " +
            "GROUP BY cd.id_bp) AS f_hr_d, " +
            "(SELECT SUM(ct.qty) FROM mfg_cost AS ct " +
            "WHERE ct.id_bp = c.id_bp AND ct.id_dt BETWEEN '" + sDateStart + "' AND '" + sDateEnd + "' AND ct.id_tp_hr = " + SDataConstantsSys.MFGS_TP_HOUR_TPL + " AND ct.b_del = 0 AND ct.b_acc = 0 " +
            "GROUP BY ct.id_bp) AS f_hr_t, " +
            "(SELECT SUM(cnc.qty) FROM mfg_cost AS cnc " +
            "WHERE cnc.id_bp = c.id_bp AND cnc.id_dt BETWEEN '" + sDateStart + "' AND '" + sDateEnd + "' AND cnc.id_tp_hr = " + SDataConstantsSys.MFGS_TP_HOUR_NML + " AND cnc.b_del = 0 AND cnc.b_acc = 1 " +
            "GROUP BY cnc.id_bp) AS f_hr_nc, " +
            "(SELECT SUM(cdc.qty) FROM mfg_cost AS cdc " +
            "WHERE cdc.id_bp = c.id_bp AND cdc.id_dt BETWEEN '" + sDateStart + "' AND '" + sDateEnd + "' AND cdc.id_tp_hr = " + SDataConstantsSys.MFGS_TP_HOUR_DBL + " AND cdc.b_del = 0 AND cdc.b_acc = 1 " +
            "GROUP BY cdc.id_bp) AS f_hr_dc, " +
            "(SELECT SUM(ctc.qty) FROM mfg_cost AS ctc " +
            "WHERE ctc.id_bp = c.id_bp AND ctc.id_dt BETWEEN '" + sDateStart + "' AND '" + sDateEnd + "' AND ctc.id_tp_hr = " + SDataConstantsSys.MFGS_TP_HOUR_TPL + " AND ctc.b_del = 0 AND ctc.b_acc = 1 " +
            "GROUP BY ctc.id_bp) AS f_hr_tc, c.* " +
            "FROM mfg_cost AS c " +
            "INNER JOIN erp.bpsu_bp AS b ON c.id_bp = b.id_bp " +
            "WHERE c.id_dt BETWEEN '" + sDateStart + "' AND '" + sDateEnd + "' AND c.b_del = 0 " +
            "GROUP BY b.id_bp " +
            "ORDER BY b.bp ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

        }
    }
}

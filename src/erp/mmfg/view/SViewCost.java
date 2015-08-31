/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.view;

import java.awt.Dimension;
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
public class SViewCost extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;

    private javax.swing.JButton jbClosePeriod;

    public SViewCost(erp.client.SClientInterface client, java.lang.String tabTitle, int nAuxType1) {
        super(client, tabTitle, SDataConstants.MFG_COST, nAuxType1);
        initComponents();
    }

    private void initComponents() {
        int i=0;

        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);
        moTabFilterDeleted = new STabFilterDeleted(this);

        jbClosePeriod = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_CLOSE));
        jbClosePeriod.setPreferredSize(new Dimension(23, 23));
        jbClosePeriod.addActionListener(this);
        jbClosePeriod.setToolTipText("Cerrar período");
        jbClosePeriod.setEnabled(mnTabTypeAux01 == SDataConstants.MFGX_COST_CLS_PER || mnTabTypeAux01 == SDataConstants.MFGX_COST_EMP ? false : true);

        jbNew.setEnabled(mnTabTypeAux01 == SDataConstants.MFGX_COST_CLS_PER || mnTabTypeAux01 == SDataConstants.MFGX_COST_EMP ? false : true);
        jbEdit.setEnabled(mnTabTypeAux01 == SDataConstants.MFGX_COST_CLS_PER ? false : true);
        jbDelete.setEnabled(false);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriodRange);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbClosePeriod);

        STableField[] aoKeyFields = new STableField[9];
        STableColumn[] aoTableColumns = null;

        i=0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "f_cost_tp");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_tp_cost_obj");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_ref_cob");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_ref_ref");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_ref_ent");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_bp");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "f_dt_start");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "f_dt_end");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i=0;
        if (mnTabTypeAux01 == SDataConstants.MFG_COST || mnTabTypeAux01 == SDataConstants.MFGX_COST_CLS_PER) {
            aoTableColumns = new STableColumn[5];
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_dt_start", "F. inicio", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_dt_end", "F. fin", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bpb", "Sucursal", 150);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "t.tp_cost_obj", "Tipo objeto costo", 150);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cost_obj", "Objeto costo", 250);
        }
        else {
            aoTableColumns = new STableColumn[7];
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Empleado", 250);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_hr_n", "Hr. normal pend. cierre", 120);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_hr_d", "Hr. doble pend. cierre", 120);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_hr_t", "Hr. triple pend. cierre", 120);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_hr_nc", "Hr. normal cerrada", 120);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_hr_dc", "Hr. doble cerrada", 120);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_hr_tc", "Hr. triple cerrada", 120);
        }

        for (i=0; i<aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.MFGX_COST_EMP);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);

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

    public void actionClosePeriod() {
        if (jbClosePeriod.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_MFG).showForm(SDataConstants.MFGX_COST_CLS_PER, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    @Override
    public void createSqlQuery() {
        Date[] range = null;
        int[] period = null;
        String sDateStart = "";
        String sDateEnd = "";
        String sqlDatePeriod = "";
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "c.b_del = FALSE ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                range = (java.util.Date[])setting.getSetting();
                sDateStart = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0]);
                sDateEnd = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]);
                sqlDatePeriod += " AND c.id_dt BETWEEN '" + sDateStart + "' AND '" + sDateEnd + "' ";
            }
        }

        if (mnTabTypeAux01 == SDataConstants.MFG_COST || mnTabTypeAux01 == SDataConstants.MFGX_COST_CLS_PER) {
            msSql = "SELECT MIN(c.id_dt) AS f_dt_start, MAX(c.id_dt) AS f_dt_end, b.bpb, t.tp_cost_obj, " +
                "CASE c.id_tp_cost_obj " +
                "WHEN " + SDataConstantsSys.MFGS_TP_COST_ORD + " THEN o.ref " +
                "WHEN " + SDataConstantsSys.MFGS_TP_COST_LINE + " THEN l.line " +
                "WHEN " + SDataConstantsSys.MFGS_TP_COST_PLT + " THEN e.ent " +
                "WHEN " + SDataConstantsSys.MFGS_TP_COST_COB + " THEN b.bpb " +
                "END AS f_cost_obj, " + SDataConstants.MFG_COST + " AS f_cost_tp, c.* " +
                "FROM mfg_cost AS c " +
                "INNER JOIN erp.mfgs_tp_cost_obj AS t ON c.id_tp_cost_obj = t.id_tp_cost_obj " +
                "INNER JOIN erp.bpsu_bpb AS b ON c.id_ref_cob = b.id_bpb " +
                "LEFT OUTER JOIN mfg_ord AS o ON c.id_ref_ref = o.id_year AND c.id_ref_ent = o.id_ord " +
                "LEFT OUTER JOIN mfg_line AS l ON c.id_ref_cob = l.id_cob AND c.id_ref_ref = l.id_ent AND c.id_ref_ent = l.id_line " +
                "LEFT OUTER JOIN erp.cfgu_cob_ent AS e ON c.id_ref_ref = e.id_cob AND c.id_ref_ent = e.id_ent " +
                "WHERE c.b_acc = " + (mnTabTypeAux01 == SDataConstants.MFGX_COST_CLS_PER ? "1 " : "0 ") + "AND " + sqlWhere + sqlDatePeriod + " " +
                // "c.id_dt BETWEEN '2011-12-13' AND '2011-12-19' AND c.b_del = 0 " +
                "GROUP BY c.id_year, c.id_tp_cost_obj, c.id_ref_cob, c.id_ref_ref, c.id_ref_ent " +
                "ORDER BY c.id_year, f_dt_start, f_dt_end, b.bpb, t.tp_cost_obj, f_cost_obj ";

                /*
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY g.num, g.des ";
                 */
        }
        else {
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
                "GROUP BY ctc.id_bp) AS f_hr_tc, " + SDataConstants.MFGX_COST_EMP + " AS f_cost_tp, '" + sDateStart + "' AS f_dt_start, '" + sDateEnd + "' AS f_dt_end, c.* " +
                "FROM mfg_cost AS c " +
                "INNER JOIN erp.bpsu_bp AS b ON c.id_bp = b.id_bp " +
                "WHERE c.id_dt BETWEEN '" + sDateStart + "' AND '" + sDateEnd + "' AND c.b_del = 0 " +
                "GROUP BY b.id_bp " +
                "ORDER BY b.bp ";
        }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbClosePeriod) {
                actionClosePeriod();
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDatePeriodRange;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableSetting;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Juan Barajas
 */
public class SViewDpsPayment extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STableColumn[] aoTableColumns;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;

    public SViewDpsPayment(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_PAYS, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        aoTableColumns = null;

        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        addTaskBarUpperComponent(moTabFilterDatePeriodRange);

        renderTableColumns();
        setIsSummaryApplying(true);

        populateTable();
    }
    
    private boolean isSummary() {
        return mnTabTypeAux02 == SUtilConsts.QRY_SUM;
    }

    private void renderTableColumns() {
        int i = 0;
        moTablePane.reset();

        if (mnTabTypeAux01 != SDataConstantsSys.TRNS_CT_DPS_PUR) {
            aoTableColumns = new STableColumn[isSummary() ? 12 : 23];
        }
        else {
            aoTableColumns = new STableColumn[isSummary() ? 10 : 21];
        }

        if (mnTabTypeAux01 != SDataConstantsSys.TRNS_CT_DPS_PUR) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "agt.bp", "Agente ventas", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "agt.id_bp", "Clave Agt", 50);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Asociado negocios", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.fiscal_id", "RFC", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave AN", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal AN", 100);
        if (!isSummary()) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cb.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        }
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot", "Subtotal $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_tax_charged", "Imp tras $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_tax_retained", "Imp ret $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_tot", "Total $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setSumApplying(true);
        
        if (!isSummary()) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_per", "Período póliza", STableConstants.WIDTH_YEAR_PERIOD);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bkc.code", "Centro contable", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cob.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rec_num", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "r.dt", "Fecha póliza", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "re.concept", "Concepto", 200);
        }
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_debit", "Cargos $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_credit", "Abonos $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setSumApplying(true);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }
        moTablePane.createTable(this);
    }

    @Override
    public void createSqlQuery() {
        java.util.Date[] range = null;
        String sqlDatePeriod = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                range = (java.util.Date[])setting.getSetting();
                sqlDatePeriod += "r.dt BETWEEN '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0])
                                    + "' AND '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]) + "' ";
            }
        }
        
//        renderTableColumns();
        
        msSql = "SELECT b.id_bp, b.bp, b.fiscal_id, bc.bp_key, bpb.bpb, re.ref, SUM(re.debit) AS f_debit, SUM(re.credit) AS f_credit, dt.code, cb.code, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, d.num_ref, d.dt, SUM(d.stot_r) AS f_stot, SUM(d.tax_charged_r) AS f_tax_charged, SUM(d.tax_retained_r) AS f_tax_retained, SUM(d.tot_r) AS f_tot, " +
                "agt.id_bp, agt.bp, r.id_bkc, r.id_tp_rec, r.id_num, r.dt, re.concept, bkc.code, cob.code, " +
                "CAST(CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, 2)) AS CHAR) as f_per, " +
                "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num," + SDataConstantsSys.NUM_LEN_FIN_REC + ")) as f_rec_num " +
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND " +
                "r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                "r.b_del = 0 AND re.b_del = 0 AND r.id_year = YEAR('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0]) + "') AND " + sqlDatePeriod + " " +
                "AND (r.id_tp_rec = '" + SDataConstantsSys.FINU_TP_REC_CASH_BANK + "' OR r.id_tp_rec = '" + SDataConstantsSys.FINU_TP_REC_JOURNAL + "') " +
                "AND re.fid_ct_sys_mov_xxx = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] : SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[0]) + " " +
                "AND re.fid_tp_sys_mov_xxx = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] : SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1]) + " AND re.b_exc_diff = 0 " +
                "INNER JOIN fin_bkc AS bkc ON r.id_bkc = bkc.id_bkc " +
                "INNER JOIN erp.bpsu_bpb AS cob ON r.fid_cob = cob.id_bpb " +
                "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON re.fid_bpb_n = bpb.id_bpb " +
                "INNER JOIN erp.bpsu_bp_ct AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
                "LEFT OUTER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc " +
                //"LEFT OUTER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc AND de.b_del = 0 " +
                "LEFT OUTER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                "LEFT OUTER JOIN erp.bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
                //"LEFT OUTER JOIN trn_dps_ety_tax AS det ON de.id_year = det.id_year AND de.id_doc = det.id_doc AND de.id_ety = det.id_ety " +
                "LEFT OUTER JOIN erp.bpsu_bp AS agt ON d.fid_sal_agt_n = agt.id_bp ";
        
                if (isSummary()) {
                    msSql += "GROUP BY b.id_bp, b.bp ";
                }
                else {
                    msSql += "GROUP BY b.id_bp, b.bp, bc.bp_key, re.ref, re.debit, re.credit, " +
                            "dt.code, cb.code, " +
                            "d.num_ser, d.num, d.dt, d.stot_r, d.tax_charged_r, d.tax_retained_r, d.tot_r, " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? "" : "agt.bp, agt.id_bp, ") +
                            "r.id_bkc, r.id_tp_rec, r.id_num, r.dt, re.concept, bkc.code, cob.code ";
                }
        msSql += "ORDER BY " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? "" : "agt.bp, agt.id_bp, ") + "b.bp, bc.bp_key, b.id_bp, re.ref, re.debit, re.credit, dt.code, cb.code, d.num_ser, d.num, " +
                "d.dt, d.stot_r, d.tax_charged_r, d.tax_retained_r, d.tot_r, r.dt, r.id_bkc, r.id_tp_rec, r.id_num, r.dt, re.concept, bkc.code, cob.code";
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
}

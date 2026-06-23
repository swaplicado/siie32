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
import erp.mod.SModSysConsts;
import java.awt.event.ActionEvent;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;

/**
 * Consulta de pagos y cobros por período, en resumen y a detalle.
 * 2026-05-27, Sergio Flores: Se usa actualmente para resumen y detalle de ventas y resumen de comrpas. Para detalle de compras se usa la clase erp.mod.fin.view.SViewDpsPayment.
 * @author Juan Barajas, Sergio Flores, Claudio Peña
 */
public class SViewDpsPayment extends erp.lib.table.STableTab {
    
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;
    private javax.swing.JToggleButton mjCurrency;
    private erp.lib.table.STableColumn[] aoTableColumns;

    public SViewDpsPayment(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_PAYS, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        addTaskBarUpperComponent(moTabFilterDatePeriodRange);
        
        addTaskBarUpperSeparator();
        
        mjCurrency = new javax.swing.JToggleButton();
        mjCurrency.setPreferredSize(new java.awt.Dimension(23, 23));
        mjCurrency.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_money_off.gif")));
        mjCurrency.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/switch_money_on.gif")));
        mjCurrency.addActionListener(this);
        updateCurrencyToolTip();
        addTaskBarUpperComponent(mjCurrency);

        int cols = isSummary() ? 11 : 24;
        
        if (!isPurchases()) {
            // columns for sales only:
            cols += 2; // for sales agent name and code
        }
        
        aoTableColumns = new STableColumn[cols];
        
        int i = 0;

        if (!isPurchases()) {
            // columns for sales only:
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_agt", "Agente", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "agt.id_bp", "Clave agente", 50);
        }
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Asociado negocios", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.fiscal_id", "RFC AN", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave AN", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bb.bpb", "Sucursal AN", 100);
        
        if (!isSummary()) {
            // columns for detail only:
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_dt_code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_dcob_code", "Sucursal empresa doc.", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot", "Subtotal doc. $", STableConstants.WIDTH_VALUE_2X);
            aoTableColumns[i++].setSumApplying(true);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_tax_charged", "Impto. tras. doc. $", STableConstants.WIDTH_VALUE);
            aoTableColumns[i++].setSumApplying(true);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_tax_retained", "Impto. ret. doc. $", STableConstants.WIDTH_VALUE);
            aoTableColumns[i++].setSumApplying(true);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_tot", "Total doc. $", STableConstants.WIDTH_VALUE_2X);
            aoTableColumns[i++].setSumApplying(true);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rec_per", "Período póliza", STableConstants.WIDTH_YEAR_PERIOD);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_bkc_code", "Centro contable póliza", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rcob_code", "Sucursal empresa póliza", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rec_num", "Número póliza", STableConstants.WIDTH_RECORD_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "r.dt", "Fecha póliza", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "re.sort_pos", "# partida póliza", STableConstants.WIDTH_NUM_TINYINT); // as String to prevent NaN to be displayed in totals row
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "re.concept", "Concepto partida póliza", 200);
        }
        
        if (isSummary()) {
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot", "Subtotal $", STableConstants.WIDTH_VALUE_2X);
            aoTableColumns[i++].setSumApplying(true);

            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_tax_charged", "Imp tras $", STableConstants.WIDTH_VALUE);
            aoTableColumns[i++].setSumApplying(true);

            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_tax_retained", "Imp ret $", STableConstants.WIDTH_VALUE);
            aoTableColumns[i++].setSumApplying(true);

            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_tot", "Total $", STableConstants.WIDTH_VALUE_2X);
            aoTableColumns[i++].setSumApplying(true);
        }
        
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_debit", "Cargos $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_credit", "Abonos $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);

        moTablePane.reset();
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }
        
        moTablePane.createTable(this);
        
        setIsSummaryApplying(true);

        populateTable();
    }
    
    private void updateCurrencyToolTip() {
        mjCurrency.setToolTipText("<html>Mostrar moneda local o moneda de transacción (mostrando: <b>" + (!mjCurrency.isSelected() ? "moneda local" : "moneda de transacción") + "</b>)</html>");
    }

    private boolean isPurchases() {
        return mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR;
    }
    
    private boolean isSummary() {
        return mnTabTypeAux02 == SUtilConsts.QRY_SUM;
    }
    
    @Override
    public void createSqlQuery() {
        java.util.Date[] range = null;
        String sqlPeriod = "";
        STableSetting setting = null;
        boolean isLocalCurrency = !mjCurrency.isSelected();

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                range = (java.util.Date[])setting.getSetting();
                sqlPeriod += "r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(range[0]) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(range[1]) + "'";
            }
        }
        
        msSql = "SELECT ";
        
        if (!isPurchases()) {
            // columns for sales only:
            msSql += "agt.id_bp, COALESCE(agt.bp, '-SIN AGENTE-') AS f_agt, ";
        }
        
        msSql += "b.id_bp, b.bp, b.fiscal_id, bc.bp_key, bb.bpb, ";
        
        if (!isSummary()) {
            // columns for detail only:
            msSql += "d.id_year, d.id_doc, COALESCE(CONCAT(d.num_ser, IF(d.num_ser = '', '', '-'), d.num), 'ANTICIPO') AS f_num, d.num_ref, d.dt, "
                    + (isLocalCurrency ? "d.stot_r AS f_stot, d.tax_charged_r AS f_tax_charged, d.tax_retained_r AS f_tax_retained, d.tot_r AS f_tot, " :
                    "d.stot_cur_r AS f_stot, d.tax_charged_cur_r AS f_tax_charged, d.tax_retained_cur_r AS f_tax_retained, d.tot_cur_r AS f_tot, ")
                    + "COALESCE(dt.code, '(N/A)') AS f_dt_code, dcob.code AS f_dcob_code, "
                    + "CONCAT(r.id_year, '-', LPAD(r.id_per, 2, '0')) as f_rec_per, "
                    + "CONCAT(r.id_tp_rec, '-', LPAD(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ", '0')) as f_rec_num, "
                    + "r.id_bkc, rbkc.code AS f_bkc_code, r.fid_cob, rcob.code AS f_rcob_code, r.dt, re.id_ety, re.sort_pos, re.concept, ";
        }
        
        if (isLocalCurrency) {
            msSql += "SUM(d.stot_r) AS f_stot, "
                    + "SUM(d.tax_charged_r) AS f_tax_charged, "
                    + "SUM(d.tax_retained_r) AS f_tax_retained, "
                    + "SUM(d.tot_r) AS f_tot, "
                    + "SUM(re.debit) AS f_debit, "
                    + "SUM(re.credit) AS f_credit, "
                    + SModSysConsts.CFGU_CUR_MXN + " AS f_id_cur, '"
                    + miClient.getSession().getSessionCustom().getLocalCurrencyCode()
                    + "' AS f_cur ";
        }
        else {
            msSql += "SUM(d.stot_cur_r) AS f_stot, "
                    + "SUM(d.tax_charged_cur_r) AS f_tax_charged, "
                    + "SUM(d.tax_retained_cur_r) AS f_tax_retained, "
                    + "SUM(d.tot_cur_r) AS f_tot, "
                    + "SUM(re.debit_cur) AS f_debit, "
                    + "SUM(re.credit_cur) AS f_credit, "
                    + "re.fid_cur AS f_id_cur, c.cur_key AS f_cur ";
        }
                
        msSql += "FROM "
                + "fin_rec AS r "
                + "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "INNER JOIN fin_bkc AS rbkc ON r.id_bkc = rbkc.id_bkc "
                + "INNER JOIN erp.bpsu_bpb AS rcob ON r.fid_cob = rcob.id_bpb "
                + "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp "
                + "INNER JOIN erp.bpsu_bpb AS bb ON re.fid_bpb_n = bb.id_bpb "
                + "INNER JOIN erp.bpsu_bp_ct AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = " + (isPurchases() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " "
                + (isLocalCurrency ? "" : "INNER JOIN erp.cfgu_cur AS c ON re.fid_cur = c.id_cur ")
                + "LEFT OUTER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc "
                + "LEFT OUTER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps "
                + "LEFT OUTER JOIN erp.bpsu_bpb AS dcob ON d.fid_cob = dcob.id_bpb "
                + "LEFT OUTER JOIN erp.bpsu_bp AS agt ON d.fid_sal_agt_n = agt.id_bp ";
        
        msSql += "WHERE "
                + "NOT r.b_del AND NOT re.b_del AND NOT re.b_exc_diff AND r.id_year = " + SLibTimeUtils.digestYear(range[0])[0] + " AND " + sqlPeriod + " "
                + "AND (r.id_tp_rec = '" + SDataConstantsSys.FINU_TP_REC_CASH_BANK + "' OR r.id_tp_rec = '" + SDataConstantsSys.FINU_TP_REC_JOURNAL + "') "
                + "AND re.fid_ct_sys_mov_xxx = " + (isPurchases() ? SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] : SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[0]) + " "
                + "AND re.fid_tp_sys_mov_xxx = " + (isPurchases() ? SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] : SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1]) + " ";
        
        msSql += "GROUP BY ";
        
        if (!isPurchases()) {
            // columns for sales only:
            msSql += "agt.id_bp, agt.bp, ";
        }
        
        msSql += "b.id_bp, b.bp, b.fiscal_id, bc.bp_key, bb.bpb";
        
        if (!isSummary()) {
            // columns for detail only:
            msSql += ", d.id_year, d.id_doc, d.num_ser, d.num, d.num_ref, d.dt, "
                    + "dt.code, dcob.code, "
                    + "r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num, rbkc.code, "
                    + "r.fid_cob, rcob.code, r.dt, re.id_ety, re.sort_pos, re.concept"
                    + (isLocalCurrency ? "" : ", c.id_cur, c.cur_key");
        }
        
        msSql += " "
                + "ORDER BY ";
        
        if (!isPurchases()) {
            // columns for sales only:
            msSql += "agt.id_bp, agt.bp, ";
        }
        
        msSql += "b.bp, b.fiscal_id, bc.bp_key, bb.bpb, b.id_bp";
        
        if (!isSummary()) {
            // columns for detail only:
            msSql += ", dt.code, f_num, d.num_ref, d.dt, d.id_year, d.id_doc, dcob.code, "
                    + "f_rec_per, f_rec_num, rbkc.code, r.id_bkc, rcob.code, r.fid_cob, r.dt, re.sort_pos, re.concept"
                    + (isLocalCurrency ? "" : ", c.cur_key, c.id_cur");
        }
        
        msSql += ";";
        
        setIsSummaryApplying(isLocalCurrency); // show totals row only when local currency is selected!
    }
    
    private void actionCurrency() {
        populateTable();
        updateCurrencyToolTip();
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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mjCurrency) {
            actionCurrency();
        }
        else {
            super.actionPerformed(e);
        }
    }
}

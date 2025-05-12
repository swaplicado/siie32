/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.cfd.SCfdConsts;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.table.SFilterConstants;
import erp.table.STabFilterBizPartner;
import erp.table.STabFilterCompanyBranch;
import erp.table.STabFilterFunctionalArea;
import javax.swing.JButton;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SViewCfdSendingLog extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;
    private erp.table.STabFilterCompanyBranch moTabFilterCompanyBranch;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;

    /**
     * View to sending log.
     * @param client GUI client interface.
     * @param tabTitle View tab title.
     * @param auxType01
     */
    public SViewCfdSendingLog(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRN_CFD_SND_LOG, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i;
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;

        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);
        
        moTabFilterCompanyBranch = new STabFilterCompanyBranch(miClient, this);
        moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, isCfdiPayroll() ? SDataConstants.BPSX_BP_EMP : SDataConstantsSys.BPSS_CT_BP_CUS);
        
        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        
        if (!isCfdiPayroll()) {
            addTaskBarUpperSeparator();
            addTaskBarUpperComponent(moTabFilterCompanyBranch);
        }
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterBizPartner);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterFunctionalArea);

        aoKeyFields = new STableField[1];
        aoTableColumns = new STableColumn[isCfdiPayroll() ? 12 : 14];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "dx.id_cfd");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_cfd", "Tipo CFD", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);

        if (!isCfdiPayroll()) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt_doc_delivery_n", "Entrega programada", STableConstants.WIDTH_DATE);
            if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave cliente", 50);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 200);
            }
            else {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 200);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave cliente", 50);
            }
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bb.bpb", "Sucursal cliente", 75);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Empleado", 250);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_debit", "Percepciones $", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_credit", "Deducciones $", 100);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_balance", "Total neto $", 100);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "snd.dt", "Fecha envío", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "snd.snd_to", "Receptor", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "us.usr", "Usr. envío", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "snd.ts", "Envío", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.TRN_DPS);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_SEND_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_SENT);
        mvSuscriptors.add(SDataConstants.USRU_USR);
        
        populateTable();
    }
    
    private boolean isCfdiPayroll() {
        return mnTabTypeAux01 == SCfdConsts.CFDI_PAYROLL_VER_OLD || mnTabTypeAux01 == SCfdConsts.CFDI_PAYROLL_VER_CUR;
    }
    
    private boolean isCfdiPayrollVersionOld() {
        return mnTabTypeAux01 == SCfdConsts.CFDI_PAYROLL_VER_OLD;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        String sqlDatePeriod = "";
        java.lang.String sqlFunctAreas = "";
        String sqlCompanyBranch = "";
        String sqlBizPartner = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlDatePeriod = "AND " + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), (isCfdiPayroll() ? "p.dt_end" : "d.dt"));
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (!((String) setting.getSetting()).isEmpty()) {
                    sqlFunctAreas += (sqlFunctAreas.isEmpty() ? "" : "AND ") + "d.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB) {
                sqlCompanyBranch = ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "AND d.fid_cob = " + (Integer) setting.getSetting() + " ");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                sqlBizPartner = ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : (isCfdiPayroll() ? (isCfdiPayrollVersionOld() ? "AND pe.fid_bpr_n = " : "AND pe.id_emp = ") : "AND d.fid_bp_r = ") + (Integer) setting.getSetting() + " ");
            }
        }
        
        if (isCfdiPayroll()) {
            msSql = "SELECT dx.id_cfd, p.id_pay, p.dt_end AS f_dt, pe.id_emp, " +
                    (isCfdiPayrollVersionOld() ? "pe.num_ser, pe.num, p.pay_num, p.pay_year, p.pay_per, p.pay_type, p.dt_beg, pe.fid_bpr_n, " : 
                    "pes.num_ser, pes.num, p.num, p.per_year, p.per, p.dt_sta, ") +
                    "b.bp, '' AS f_cob_code, snd.dt, snd.snd_to, snd.ts, us.usr, " +
                    "CONCAT("+ (isCfdiPayrollVersionOld() ? "pe.num_ser" : "pes.num_ser") + ", IF(length("+ (isCfdiPayrollVersionOld() ? "pe.num_ser" : "pes.num_ser") + ") = 0, '', '-'), "+ (isCfdiPayrollVersionOld() ? "pe.num" : "pes.num") + ") AS f_num, tp.tp_cfd, " +
                    "SUM(" + (isCfdiPayrollVersionOld() ? "pe.debit " : "pe.ear_r ") + ") AS f_debit, " +
                    "SUM(" + (isCfdiPayrollVersionOld() ? "pe.credit " : "pe.ded_r ") + ") AS f_credit, " +
                    "SUM(" + (isCfdiPayrollVersionOld() ? "pe.debit - pe.credit " : "pe.pay_r ") + ") AS f_balance " +
                    "FROM trn_cfd_snd_log AS snd " +
                    "INNER JOIN trn_cfd AS dx ON dx.id_cfd = snd.id_cfd " +
                    (isCfdiPayrollVersionOld() ? 
                    "INNER JOIN hrs_sie_pay_emp AS pe ON pe.id_pay = dx.fid_pay_pay_n AND pe.id_emp = dx.fid_pay_emp_n AND pe.fid_bpr_n = dx.fid_pay_bpr_n AND pe.b_del = FALSE " +
                    "INNER JOIN hrs_sie_pay AS p ON p.id_pay = pe.id_pay " :
                    "INNER JOIN hrs_pay_rcp AS pe ON pe.id_pay = dx.fid_pay_rcp_pay_n AND pe.id_emp = dx.fid_pay_rcp_emp_n " +
                    "INNER JOIN hrs_pay AS p ON p.id_pay = pe.id_pay " +
                    "INNER JOIN hrs_pay_rcp_iss AS pes ON pes.id_pay = dx.fid_pay_rcp_pay_n AND pes.id_emp = dx.fid_pay_rcp_emp_n AND pes.id_iss = dx.fid_pay_rcp_iss_n ") +
                    "INNER JOIN erp.bpsu_bp AS b ON " + (isCfdiPayrollVersionOld() ? "pe.fid_bpr_n = b.id_bp " : "pe.id_emp = b.id_bp ") +
                    "INNER JOIN erp.trns_tp_cfd AS tp ON dx.fid_tp_cfd = tp.id_tp_cfd " +
                    "INNER JOIN erp.usru_usr AS us ON snd.fid_usr = us.id_usr " +
                    "WHERE pe.b_del = 0 AND dx.fid_tp_cfd = " + (isCfdiPayroll() ? SDataConstantsSys.TRNS_TP_CFD_PAYROLL : SDataConstantsSys.TRNS_TP_CFD_INV) + " " + sqlDatePeriod + sqlBizPartner +
                    "GROUP BY id_cfd, tp.tp_cfd, " + (isCfdiPayrollVersionOld() ? "pe.num_ser, pe.num, " : "pes.num_ser, pes.num, ") + "p.dt_end, b.bp, " + (isCfdiPayrollVersionOld() ? "pe.fid_bpr_n, " : "pe.id_emp, ") + " snd.id_snd " +
                    "HAVING id_cfd <> 0 " +
                    "ORDER BY id_cfd, tp.tp_cfd, " + (isCfdiPayrollVersionOld() ? "pe.num_ser, pe.num, " : "pes.num_ser, pes.num, ") + "p.dt_end, b.bp, " + (isCfdiPayrollVersionOld() ? "pe.fid_bpr_n, " : "pe.id_emp, ") + " snd.id_snd ";
        }
        else {
            msSql = "SELECT dx.id_cfd, d.id_year, d.id_doc, d.dt AS f_dt, d.dt_doc_delivery_n, d.b_close, d.b_del, d.ts_close, " +
                    "d.num_ser, d.num, d.num_ref, CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, tp.tp_cfd, " +
                    "d.ts_audit, d.tot_cur_r, dt.code, c.cur_key, b.id_bp, b.bp, bc.bp_key, bb.id_bpb, bb.bpb, cb.code AS f_cob_code, ua.usr, " +
                    "snd.dt, snd.snd_to, snd.ts, us.usr " +
                    "FROM trn_cfd_snd_log AS snd " +
                    "INNER JOIN trn_cfd AS dx ON dx.id_cfd = snd.id_cfd " +
                    "INNER JOIN trn_dps AS d ON d.id_year = dx.fid_dps_year_n AND d.id_doc = dx.fid_dps_doc_n " +
                    "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                    "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                    "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                    "INNER JOIN erp.bpsu_bp_ct AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_CUS + " " +
                    "INNER JOIN erp.bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
                    "INNER JOIN erp.bpsu_bpb AS bb ON d.fid_bpb = bb.id_bpb " +
                    "INNER JOIN erp.usru_usr AS ua ON d.fid_usr_audit = ua.id_usr " +
                    "INNER JOIN erp.trns_tp_cfd AS tp ON dx.fid_tp_cfd = tp.id_tp_cfd " +
                    "INNER JOIN erp.usru_usr AS us ON snd.fid_usr = us.id_usr " +
                    "WHERE d.b_del = 0 AND dx.fid_tp_cfd = " + (isCfdiPayroll() ? SDataConstantsSys.TRNS_TP_CFD_PAYROLL : SDataConstantsSys.TRNS_TP_CFD_INV) + " " + sqlDatePeriod + sqlFunctAreas + sqlCompanyBranch + sqlBizPartner +
                    "HAVING id_cfd <> 0 " +
                    "ORDER BY tp.tp_cfd, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt, b.bp, bc.bp_key, b.id_bp, bb.bpb, bb.id_bpb, snd.id_snd ";
        }
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

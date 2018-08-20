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
import erp.mcfg.data.SDataParamsCompany;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

/**
 *
 * @author Juan Barajas
 */
public class SViewRecordDps extends erp.lib.table.STableTab {

    private erp.lib.table.STableColumn[] aoTableColumns;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;

    private JToggleButton jtbRecordAdjYearEnd;
    private JToggleButton jtbRecordAdjAudit;

    private boolean mbShowRecordAdjYearEnd;
    private boolean mbShowRecordAdjAudit;

    public SViewRecordDps(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType) {
        super(client, tabTitle, auxType);
        mnTabType = SDataConstants.FINX_REC_DPS;
        mnTabTypeAux01 = auxType;
        initComponents();
    }

    private void initComponents() {
        aoTableColumns = null;
        mbShowRecordAdjYearEnd = true;
        mbShowRecordAdjAudit = true;

        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        addTaskBarUpperComponent(moTabFilterDatePeriodRange);

        jtbRecordAdjYearEnd = new JToggleButton(new ImageIcon(getClass().getResource("/erp/img/switch_adj_year_off.gif")));
        jtbRecordAdjYearEnd.setSelectedIcon(new ImageIcon(getClass().getResource("/erp/img/switch_adj_year_on.gif")));
        jtbRecordAdjYearEnd.setPreferredSize(new Dimension(23, 23));
        jtbRecordAdjYearEnd.setToolTipText("Incluir ajustes de cierre");
        jtbRecordAdjYearEnd.addActionListener(this);
        jtbRecordAdjYearEnd.setSelected(true);
        addTaskBarUpperComponent(jtbRecordAdjYearEnd);

        jtbRecordAdjAudit = new JToggleButton(new ImageIcon(getClass().getResource("/erp/img/switch_adj_audit_off.gif")));
        jtbRecordAdjAudit.setSelectedIcon(new ImageIcon(getClass().getResource("/erp/img/switch_adj_audit_on.gif")));
        jtbRecordAdjAudit.setPreferredSize(new Dimension(23, 23));
        jtbRecordAdjAudit.setToolTipText("Incluir ajustes de auditoria");
        jtbRecordAdjAudit.addActionListener(this);
        jtbRecordAdjAudit.setSelected(true);
        addTaskBarUpperComponent(jtbRecordAdjAudit);

        renderTableColumns();
        setIsSummaryApplying(true);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.TRN_DPS);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_PAYED);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_PAY_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_CFD_PAY_REC);

        populateTable();
    }

    private void renderTableColumns() {
        int i = 0;

        moTablePane.reset();

        aoTableColumns = new STableColumn[31];

        switch (mnTabTypeAux01) {
            case SDataConstantsSys.BPSS_CT_BP_CUS:
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Cliente", 200);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave cliente", 50);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb", "Sucursal cliente", 75);
                break;
            case SDataConstantsSys.BPSS_CT_BP_SUP:
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Proveedor", 200);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave proveedor", 50);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb", "Sucursal proveedor", 75);
                break;
            default:
                break;
        }

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt_code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d_num", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d_dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d_stot_cur", "Subtotal mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d_tax_charged_cur", "Imp tras mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d_tax_retained_cur", "Imp ret mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d_tot_cur", "Total mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d_exc_rate", "T cambio", STableConstants.WIDTH_EXCHANGE_RATE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererExchangeRate());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d_stot", "Subtotal $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d_tax_charged", "Imp tras $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d_tax_retained", "Imp ret $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d_tot", "Total $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_per", "Período póliza", STableConstants.WIDTH_YEAR_PERIOD);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bkc", "Centro contable", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cob", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING,  "f_rec_num", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "r_dt", "Fecha póliza", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cls_acc_mov","Subclase de movimiento", STableConstants.WIDTH_ACCOUNT_ID);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_acc","No. cuenta contable", STableConstants.WIDTH_ACCOUNT_ID);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "acc", "Cuenta contable", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "concept", "Concepto", 175);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "debit", "Debe $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "credit", "Haber $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "r_exc_rate", "T cambio", STableConstants.WIDTH_EXCHANGE_RATE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererExchangeRate());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "debit_cur", "Debe mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "credit_cur", "Haber mon $", STableConstants.WIDTH_VALUE);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        moTablePane.createTable(this);
    }

    private void showRecordAdjustment() {
        mbShowRecordAdjYearEnd = jtbRecordAdjYearEnd.isSelected();
        mbShowRecordAdjAudit = jtbRecordAdjAudit.isSelected();
        createSqlQuery();
        populateTable();
    }

    @Override
    public void createSqlQuery() {
        java.util.Date[] range = null;
        String sqlWhere = "";
        String sqlDatePeriod = "";
        String dateInit = "";
        String dateEnd = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                range = (java.util.Date[])setting.getSetting();
                dateInit = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0]);
                dateEnd = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]);
                sqlDatePeriod += "d.dt BETWEEN '" + dateInit + "' AND '" + dateEnd + "' ";
            }
        }

        sqlWhere += (!mbShowRecordAdjYearEnd ? " AND r.b_adj_year = 0 " : "") +
                    (!mbShowRecordAdjAudit ? " AND  r.b_adj_audit = 0 " : "");

//        renderTableColumns();

        msSql = "SELECT b.id_bp AS id_bp, b.bp AS bp, bc.bp_key AS bp_key, bpb.bpb AS bpb, dt.code AS dt_code, cb.code AS cb_code, c.cur_key AS cur_key, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, d.num_ref AS d_num, d.dt AS d_dt, d.stot_r AS d_stot, " +
                "d.tax_charged_r AS d_tax_charged, d.tax_retained_r AS d_tax_retained, d.tot_r AS d_tot, " +
                "d.stot_cur_r AS d_stot_cur, d.tax_charged_cur_r AS d_tax_charged_cur, d.tax_retained_cur_r AS d_tax_retained_cur, d.tot_cur_r AS d_tot_cur, d.exc_rate AS d_exc_rate, " +
                "CAST(CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, 2)) AS CHAR) as f_per, bkc.code AS bkc, cob.code AS cob, " +
                "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num,6)) as f_rec_num, r.dt AS r_dt, mcls.cls_acc_mov AS cls_acc_mov, a.id_acc AS id_acc, a.acc AS acc, " +
                "f_acc_usr(" + ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskAccount() + ", a.code) AS f_acc, " +
                "re.concept AS concept, re.debit AS debit, re.credit AS credit, re.exc_rate AS r_exc_rate, re.debit_cur AS debit_cur, re.credit_cur AS credit_cur " +
                "FROM trn_dps AS d " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                "INNER JOIN erp.cfgu_cur AS c  ON d.fid_cur = c.id_cur " +
                "INNER JOIN erp.bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
                "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb " +
                "INNER JOIN erp.bpsu_bp_ct AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = " + mnTabTypeAux01 + " " +
                "LEFT OUTER JOIN fin_rec_ety AS re ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc AND re.fid_bp_nr = b.id_bp " +
                "LEFT OUTER JOIN fin_rec AS r ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND " +
                "r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND re.b_exc_diff = 0 " +
                "LEFT OUTER JOIN fin_acc AS a ON re.fid_acc = a.id_acc " +
                "LEFT OUTER JOIN erp.fins_tp_acc_mov AS mtp ON re.fid_tp_acc_mov = mtp.id_tp_acc_mov " +
                "LEFT OUTER JOIN erp.fins_cl_acc_mov AS mcl ON re.fid_tp_acc_mov = mcl.id_tp_acc_mov AND re.fid_cl_acc_mov = mcl.id_cl_acc_mov " +
                "LEFT OUTER JOIN erp.fins_cls_acc_mov AS mcls ON re.fid_tp_acc_mov = mcls.id_tp_acc_mov AND re.fid_cl_acc_mov = mcls.id_cl_acc_mov AND  re.fid_cls_acc_mov = mcls.id_cls_acc_mov " +
                "LEFT OUTER JOIN fin_bkc AS bkc ON r.id_bkc = bkc.id_bkc " +
                "LEFT OUTER JOIN erp.bpsu_bpb AS cob ON r.fid_cob = cob.id_bpb " +
                "WHERE d.b_del = 0 AND r.b_del = 0 AND re.b_del = 0 AND " +
                (mnTabTypeAux01 == SDataConstantsSys.BPSS_CT_BP_SUP ? "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1] + " " :
                "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] + " ") +
                ("AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " AND ") +
                sqlDatePeriod + sqlWhere + " " +
                "ORDER BY id_bp, bp, bp_key, bpb, d_dt, f_num, d_num, r_dt, re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.id_ety ";
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
            JButton button = (JButton) e.getSource();
        }
        else if (e.getSource() instanceof javax.swing.JToggleButton) {
            JToggleButton toggleButton = (JToggleButton) e.getSource();

            if (toggleButton == jtbRecordAdjYearEnd) {
                showRecordAdjustment();
            }
            else if (toggleButton == jtbRecordAdjAudit) {
                showRecordAdjustment();
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableSetting;
import erp.lib.table.STableTab;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

/**
 *
 * @author Isabel Servín
 */
public class SViewShortTermDocumentsDetail extends STableTab {

    public static final String WITHOUT_REFERENCE = "(SIN REFERENCIA)";
    
    private STableColumn[] aoTableColumns;
    private STabFilterDatePeriod moTabFilterDatePeriod;
    
    private JToggleButton jtbRecordAdjYearEnd;
    private JToggleButton jtbRecordAdjAudit;
    
    private boolean mbShowRecordAdjYearEnd;
    private boolean mbShowRecordAdjAudit;
    
    public SViewShortTermDocumentsDetail(SClientInterface client, String tabTitle, int auxType) {
        super(client, tabTitle, auxType);
        mnTabType = SDataConstants.FINX_SHORT_TERM_DOCS_DET;
        mnTabTypeAux01 = auxType;
        initComponents();
    }
    
    private void initComponents() {
        aoTableColumns = null;
        mbShowRecordAdjYearEnd = true;
        mbShowRecordAdjAudit = true;
        
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR);
        
        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        
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
        setIsSummaryApplying(false);

        populateTable();
    }
    
    private void renderTableColumns() {
        int i = 0;
        
        moTablePane.reset();
        
        aoTableColumns = new STableColumn[26];
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "id_acc", "No. cuenta contable", STableConstants.WIDTH_ACCOUNT_ID);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "acc", "Cuenta contable", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ref", "Referencia contable", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "r.dt", "Fecha póliza", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "period", "Período póliza", STableConstants.WIDTH_YEAR_PERIOD);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bkc", "Centro contable", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cob", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING,  "num", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "re.concept", "Concepto", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "debit_cur", "Debe mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "credit_cur", "Haber mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "exc_rate", "T cambio", STableConstants.WIDTH_EXCHANGE_RATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "debit", "Debe $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "credit", "Haber $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "mon_loc", "Moneda local", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "re.b_sys", "Sistema", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "re.b_exc_diff", "Diferencia cambiaria", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cl.cls_acc_mov", "Subclase movimiento", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "re.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "re.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "re.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "re.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);

    
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
        String sqlWhere = "";
        String sFormatAccountId = miClient.getSessionXXX().getParamsErp().getFormatAccountId().replace('9', '0');
        ArrayList<Integer> vLevels = SDataUtilities.getArrayAccountLevels(sFormatAccountId);
        for (STableSetting setting : mvTableSettings) {
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlWhere += SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "r.dt");                
            }
        }
        
        sqlWhere += (!mbShowRecordAdjYearEnd ? " AND r.b_adj_year = 0 " : "") +
                    (!mbShowRecordAdjAudit ? " AND r.b_adj_audit = 0 " : "");
        
        msSql = "SELECT acc.id_acc, acc.acc, " +
                "IF(re.ref = '', '" + WITHOUT_REFERENCE + "', re.ref) AS ref, " +
                "r.dt, " +
                "CONCAT(r.id_year, '-', LPAD(r.id_per, 2, 0)) AS period, " +
                "bkc.code AS bkc, " +
                "cob.code AS cob, " +
                "CONCAT(r.id_tp_rec, '-', LPAD(r.id_num, 6, 0)) AS num, " +
                "re.concept, " +
                "re.debit_cur AS debit_cur, " +
                "re.credit_cur AS credit_cur, " +
                "c.cur_key, re.exc_rate, " +
                "re.debit AS debit, " +
                "re.credit AS credit, " +
                "'MXN' AS mon_loc, " +
                "re.b_sys, " +
                "re.b_exc_diff, " +
                "cl.cls_acc_mov, " +
                "re.b_del, " +
                "un.usr, " +
                "re.ts_new, " +
                "ue.usr, " +
                "re.ts_edit, " +
                "ud.usr, " +
                "re.ts_del " +
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety AS re ON " +
                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
                "INNER JOIN fin_bkc AS bkc ON " +
                "re.id_bkc = bkc.id_bkc " +
                "INNER JOIN erp.bpsu_bpb AS cob ON " +
                "r.fid_cob = cob.id_bpb " +
                "INNER JOIN fin_acc AS m_acc ON " +
                "CONCAT(LEFT(re.fid_acc, " + (vLevels.get(1) - 1) + "), '" + sFormatAccountId.substring(vLevels.get(1) - 1) + "') = m_acc.id_acc " +
                "INNER JOIN fin_acc AS acc ON " +
                "re.fk_acc = acc.pk_acc " +
                "INNER JOIN erp.fins_tp_acc_spe AS spe ON " +
                "m_acc.fid_tp_acc_spe = spe.id_tp_acc_spe " +
                "INNER JOIN erp.fins_cls_acc_mov AS cl ON " +
                "re.fid_tp_acc_mov = cl.id_tp_acc_mov AND re.fid_cl_acc_mov = cl.id_cl_acc_mov AND re.fid_cls_acc_mov = cl.id_cls_acc_mov " +
                "INNER JOIN erp.cfgu_cur AS c ON " +
                "re.fid_cur = c.id_cur " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "re.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "re.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "re.fid_usr_del = ud.id_usr " +
                "WHERE " + sqlWhere +
                "AND spe.id_tp_acc_spe = " + mnTabTypeAux01 + " " +
                "AND NOT r.b_del AND NOT re.b_del " +
                "ORDER BY acc.id_acc, acc.acc, re.ref, r.dt, bkc.code, cob.code, r.id_tp_rec, r.id_num;";
    }

    @Override
    public void actionNew() {}

    @Override
    public void actionEdit() {}

    @Override
    public void actionDelete() {}
    
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.view;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDatePeriodRange;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableSetting;
import erp.mcfg.data.SDataParamsCompany;
import erp.mfin.data.SDataAccount;
import erp.mfin.form.SPanelFilterAccount;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

/**
 *
 * @author Claudio Peña
 */
public class SViewAuxDetaill extends erp.lib.table.STableTab {

    private erp.lib.table.STableColumn[] aoTableColumns;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;
    private erp.mfin.form.SPanelFilterAccount moPanelFilterAccount;
    private java.lang.String msAccountId;
    private java.lang.Integer mnLevel;
    private erp.mfin.data.SDataAccount moAccount;

    private JToggleButton jtbRecordAdjYearEnd;
    private JToggleButton jtbRecordAdjAudit;

    private boolean mbShowRecordAdjYearEnd;
    private boolean mbShowRecordAdjAudit;

    public SViewAuxDetaill(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType) {
        super(client, tabTitle, auxType);
        mnTabType = SDataConstants.FINX_ACCOUNTING;
        mnTabTypeAux01 = auxType;
        initComponents();
    }

    private void initComponents() {
        aoTableColumns = null;
        mbShowRecordAdjYearEnd = true;
        mbShowRecordAdjAudit = true;

        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);

        moPanelFilterAccount = new SPanelFilterAccount(miClient, this);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        addTaskBarUpperComponent(moTabFilterDatePeriodRange);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moPanelFilterAccount);

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

        populateTable();
    }

    private void renderTableColumns() {
        int i = 0;

        moTablePane.reset();

        aoTableColumns = new STableColumn[9];

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "CentrosCosto", "Centro de costo ", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "Gastos", "Gastos", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "CtaContable", "Cta. contable", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING,  "item", "Item", STableConstants.WIDTH_ITEM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "concepto", "Concepto", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "fecha","Fecha", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "poliza", "Poliza", STableConstants.WIDTH_RECORD_NUM);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "re.credit", "Haber $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "re.debit", "Debe $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
       


        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }
        moTablePane.createTable(this);
    }

    private String getAccount(String saccount,int nLevel) {
        if(nLevel == 1 ) {
            msAccountId = saccount.substring(0, 4) + "%";
        }
        return msAccountId;
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
                sqlDatePeriod += "r.dt BETWEEN '" + dateInit + "' AND '" + dateEnd + "' ";
            }  
            else if (setting.getType() == STableConstants.SETTING_FILTER_ACCOUNT) {
               moAccount = (SDataAccount)setting.getSetting();
               msAccountId = moAccount.getPkAccountIdXXX();
               mnLevel = moAccount.getLevel();
               sqlWhere += " AND re.fid_acc LIKE '" + getAccount(moAccount.getPkAccountIdXXX(), mnLevel) + "' ";
           }
        }

        if (sqlWhere.equalsIgnoreCase("")) {
            sqlWhere += " AND re.fid_acc LIKE 'null' ";
        }

        sqlWhere += (!mbShowRecordAdjYearEnd ? " AND r.b_adj_year = 0 " : "") +
                    (!mbShowRecordAdjAudit ? " AND  r.b_adj_audit = 0 " : "");

//        renderTableColumns();

        msSql = "SELECT " +
                "CONCAT_WS(' - ',cc.id_cc, cc.cc) AS CentrosCosto, " +
                "SUBSTRING(f.acc ,7 )AS Gastos,  f_acc_usr(" + ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskAccount() + ", f.code) AS CtaContable, " +
                "CONCAT_WS(' - ',i.item_key, i.item) AS item, " +
                "re.concept AS concepto, r.dt AS fecha, " +
                "CONCAT_WS (' ',bkc.code, cob.code, CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, 6)) ) AS poliza, re.credit, re.debit " +
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
                "INNER JOIN fin_acc AS f ON re.fid_acc = f.id_acc " +
                "INNER JOIN erp.bpsu_bpb AS cob ON r.fid_cob = cob.id_bpb " +
                "INNER JOIN fin_bkc AS bkc ON re.id_bkc = bkc.id_bkc " +
                "INNER JOIN erp.cfgu_cur AS c  ON re.fid_cur = c.id_cur " +
                "INNER JOIN erp.usru_usr AS un ON re.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON re.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON re.fid_usr_del = ud.id_usr " +
                "INNER JOIN erp.fins_cls_acc_mov AS cl ON re.fid_tp_acc_mov = cl.id_tp_acc_mov AND re.fid_cl_acc_mov = cl.id_cl_acc_mov AND re.fid_cls_acc_mov = cl.id_cls_acc_mov " +
                "LEFT OUTER JOIN fin_cc AS cc ON re.fid_cc_n = cc.id_cc " +
                "LEFT OUTER JOIN erp.itmu_item AS i ON re.fid_item_n = i.id_item " +
                "WHERE " + sqlDatePeriod + sqlWhere + "  AND r.b_del = 0 AND re.b_del = 0 " +
                "ORDER by cc.id_cc, cc.cc;";
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

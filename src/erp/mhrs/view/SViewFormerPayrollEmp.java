/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mhrs.view;

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

/**
 *
 * @author Sergio Flores
 */
public class SViewFormerPayrollEmp extends erp.lib.table.STableTab {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;

    public SViewFormerPayrollEmp(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.HRS_FORMER_PAYR_EMP);
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[17];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "p.id_pay");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "p.pay_year", "Ejercicio", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererYear());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "p.pay_per", "Período", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererMonth());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "p.pay_type", "Tipo nómina", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "p.pay_num", "No. nómina", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "p.dt_beg", "F. inicial", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "p.dt_end", "F. final", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "p.b_reg", "Es normal", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "p.b_cls", "Cerrada", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "p.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_per", "Período póliza", STableConstants.WIDTH_YEAR_PERIOD);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bkc.code", "Centro contable póliza", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cob.code", "Sucursal empresa póliza", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "r.dt", "Fecha póliza", STableConstants.WIDTH_DATE);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_debit", "Percepciones $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_credit", "Deducciones $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_balance", "Alcance neto $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.HRS_FORMER_PAYR);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
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
    public void createSqlQuery() {
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "p.b_del = FALSE ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "p.dt_end");
            }
        }

        msSql = "SELECT p.id_pay, r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num, " +
                "p.pay_year, p.pay_per, p.pay_num, p.pay_type, p.dt_beg, p.dt_end, " +
                "p.b_reg, p.b_cls, p.b_del, r.dt, bkc.code, cob.code, " +
                "CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, 2)) as f_per, " +
                "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) as f_num, " +
                "SUM(pe.debit) AS f_debit, SUM(pe.credit) AS f_credit, SUM(pe.debit - pe.credit) AS f_balance " +
                "FROM hrs_sie_pay AS p " +
                "INNER JOIN hrs_sie_pay_emp AS pe ON p.id_pay = pe.id_pay AND pe.b_del = FALSE " +
                "INNER JOIN fin_rec AS r ON pe.fid_year = r.id_year AND pe.fid_per = r.id_per AND pe.fid_bkc = r.id_bkc AND pe.fid_tp_rec = r.id_tp_rec AND pe.fid_num = r.id_num " +
                "INNER JOIN fin_bkc AS bkc ON r.id_bkc = bkc.id_bkc " +
                "INNER JOIN erp.bpsu_bpb AS cob ON r.fid_cob = cob.id_bpb " +
                "INNER JOIN erp.usru_usr AS un ON p.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON p.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON p.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "GROUP BY p.id_pay, r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num, " +
                "p.pay_year, p.pay_per, p.pay_num, p.pay_type, p.dt_beg, p.dt_end, " +
                "p.b_reg, p.b_cls, p.b_del, r.dt, bkc.code, cob.code " +
                "ORDER BY p.pay_year, p.pay_per, p.pay_type, p.pay_num, p.id_pay, " +
                "r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num ";
    }
}

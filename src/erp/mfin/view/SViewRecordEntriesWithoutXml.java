/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.view;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableSetting;

/**
 *
 * @author Isabel Servín
 */
public class SViewRecordEntriesWithoutXml extends erp.lib.table.STableTab {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.mfin.view.SPanelFilterRecordType moPanelFilterRecordType;
    
    public SViewRecordEntriesWithoutXml(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.FINX_REC_W_XML);
        initComponents();
    }

    private void initComponents() {
        int i;
        
        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moPanelFilterRecordType = new SPanelFilterRecordType(miClient, this);
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moPanelFilterRecordType);
        
        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);
        
        STableColumn[] aoTableColumns = new STableColumn[22];

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_per", "Período póliza", STableConstants.WIDTH_YEAR_PERIOD);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_bkc_code", "Centro contable", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_num", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt", "Fecha póliza", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "re.sort_pos", "Renglón", 35);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "id_acc", "No. cuenta contable", STableConstants.WIDTH_ACCOUNT_ID);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "acc", "Cuenta contable", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "concept", "Concepto póliza", 175);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "debit", "Debe $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "credit", "Haber $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "exc_rate", "T cambio", STableConstants.WIDTH_EXCHANGE_RATE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererExchangeRate());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "debit_cur", "Debe mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "credit_cur", "Haber mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_usr_new", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_usr_edit", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_usr_del", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        setIsSummaryApplying(true);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.FIN_REC);
        mvSuscriptors.add(SDataConstants.FIN_ACC);
        mvSuscriptors.add(SDataConstants.CFGU_COB_ENT);
        mvSuscriptors.add(SDataConstants.CFGU_CUR);
        mvSuscriptors.add(SDataConstants.FIN_BKC);
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
        STableSetting setting;

        for (STableSetting mvTableSetting : mvTableSettings) {
            setting = (erp.lib.table.STableSetting) mvTableSetting;
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += "AND re.b_del = FALSE ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlWhere += "AND " + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "r.dt");
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_FIN_REC_TYPE) {
                if (((String) setting.getSetting()).length() > 0) {
                    sqlWhere += "AND re.id_tp_rec = '" + (String) setting.getSetting() + "' ";
                }
            }
        }
        
        msSql = "SELECT re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.id_ety, re.sort_pos, r.dt, re.concept, bkc.code AS _bkc_code, cob.code AS _cob_code, am.id_acc, am.acc, " +
                "re.debit, re.credit, re.exc_rate, re.debit_cur, re.credit_cur, cur.cur_key, CONCAT(re.id_year, '-', erp.lib_fix_int(re.id_per, 2)) AS _per, " +
                "CONCAT(re.id_tp_rec, '-', erp.lib_fix_int(re.id_num, 6)) AS _num, re.b_del, un.usr AS _usr_new, ue.usr AS _usr_edit, ud.usr AS _usr_del, re.ts_new, re.ts_edit, re.ts_del " +
                "FROM fin_rec_ety AS re " +
                "INNER JOIN fin_rec AS r ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num " +
                "INNER JOIN fin_acc AS a ON re.fid_acc = a.id_acc " +
                "INNER JOIN fin_acc AS am ON am.code = CONCAT(LEFT(a.code, 6 * 1), REPEAT('0', 42)) AND (am.fid_tp_acc_sys = 4 OR am.fid_tp_acc_sys = 12 OR am.fid_tp_acc_sys = 13) " +
                "INNER JOIN fin_bkc AS bkc ON r.id_bkc = bkc.id_bkc " +
                "INNER JOIN erp.bpsu_bpb AS cob ON r.fid_cob = cob.id_bpb " +
                "INNER JOIN erp.cfgu_cur AS cur ON re.fid_cur = cur.id_cur " +
                "INNER JOIN erp.usru_usr AS un ON re.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON re.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON re.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN trn_cfd as cfd ON re.id_year = cfd.fid_rec_year_n AND re.id_per = cfd.fid_rec_per_n AND re.id_bkc = cfd.fid_rec_bkc_n AND re.id_tp_rec = cfd.fid_rec_tp_rec_n AND re.id_num = cfd.fid_rec_num_n AND re.id_ety = cfd.fid_rec_ety_n " +
                "WHERE ((re.fid_dps_year_n IS NULL AND re.fid_dps_doc_n IS NULL) OR (CONCAT(re.fid_dps_year_n, '_' , re.fid_dps_doc_n) NOT IN " +
                "(SELECT CONCAT(c.fid_dps_year_n, '_', c.fid_dps_doc_n) FROM trn_cfd AS c WHERE c.fid_dps_year_n = re.fid_dps_year_n AND c.fid_dps_doc_n = re.fid_dps_doc_n))) " +
                "AND re.fid_cfd_n IS NULL AND cfd.fid_rec_year_n IS NULL AND cfd.fid_rec_per_n IS NULL AND cfd.fid_rec_bkc_n IS NULL AND cfd.fid_rec_tp_rec_n IS NULL AND cfd.fid_rec_num_n IS NULL AND cfd.fid_rec_ety_n IS NULL " +
                "AND NOT r.b_del AND NOT a.b_del AND NOT am.b_del " + sqlWhere + " " + 
                "ORDER BY re.id_year, re.id_per, _bkc_code, re.id_bkc, re.id_tp_rec, re.id_num, re.sort_pos, re.id_ety;";
    }
}

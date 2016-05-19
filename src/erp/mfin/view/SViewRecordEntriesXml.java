/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import java.awt.Dimension;
import javax.swing.JButton;

/**
 *
 * @author Juan Barajas
 */
public class SViewRecordEntriesXml extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbGetXml;
    
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.mfin.view.SPanelFilterRecordType moPanelFilterRecordType;

    public SViewRecordEntriesXml(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.FIN_REC_ETY, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i;
        
        jbGetXml = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML));
        jbGetXml.setPreferredSize(new Dimension(23, 23));
        jbGetXml.addActionListener(this);
        jbGetXml.setToolTipText("Obtener XML");
        
        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moPanelFilterRecordType = new SPanelFilterRecordType(miClient, this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moPanelFilterRecordType);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbGetXml);

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);
        jbGetXml.setEnabled(true);

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[28];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_cfd");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_per", "Período póliza", STableConstants.WIDTH_YEAR_PERIOD);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "code_bkc", "Centro contable", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "code_cob", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt", "Fecha póliza", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_acc", "No. cuenta contable", STableConstants.WIDTH_ACCOUNT_ID);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_acc_name", "Cuenta contable", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "concept", "Concepto", 175);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "debit", "Debe $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "credit", "Haber $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "exc_rate", "T cambio", STableConstants.WIDTH_EXCHANGE_RATE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererExchangeRate());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "debit_cur", "Debe mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "credit_cur", "Haber mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_xml_emisor_nombre", "Nombre emisor", STableConstants.WIDTH_ITEM_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_xml_emisor_rfc", "RFC emisor", STableConstants.WIDTH_ITEM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_xml_receptor_nombre", "Nombre receptor", STableConstants.WIDTH_ITEM_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_xml_receptor_rfc", "RFC receptor", STableConstants.WIDTH_ITEM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_xml_total", "Total $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "_xml_fecha_timbrado", "Fecha timbrado", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_xml_uuid", "UUID", STableConstants.WIDTH_ITEM_3X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "usr_un", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "usr_ue", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "usr_ud", "Usr. eliminación", STableConstants.WIDTH_USER);
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

    private void actionGetXml() {
        if (jbGetXml.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null && !moTablePane.getSelectedTableRow().getIsSummary()) {
                try {
                    SCfdUtils.getXmlCfd((SClientInterface) miClient, (SDataCfd) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_CFD, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT));
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "re.b_del = FALSE ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "r.dt");
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_FIN_REC_TYPE) {
                if (((String) setting.getSetting()).length() > 0) {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "re.id_tp_rec = '" + (String) setting.getSetting() + "' ";
                }
            }
        }
       

        msSql = "SELECT * FROM " +
                "(SELECT re.id_year AS id_year, re.id_per AS id_per, re.id_bkc AS id_bkc, re.id_tp_rec AS id_tp_rec, re.id_num AS id_num, re.id_ety AS id_ety, cfd.id_cfd AS id_cfd, r.dt AS dt, re.concept AS concept, re.b_del AS b_del, " +
                "re.ts_new AS ts_new, re.ts_edit AS ts_edit, re.ts_del AS ts_del, acc.id_acc AS f_acc, acc.acc AS f_acc_name, cur.cur_key AS f_cur, bkc.code AS code_bkc, cob.code AS code_cob, e.ent AS ent, un.usr AS usr_un, ue.usr AS usr_ue, " +
                "ud.usr AS usr_ud, CONCAT(re.id_year, '-', erp.lib_fix_int(re.id_per, 2)) as f_per, CONCAT(re.id_tp_rec, '-', erp.lib_fix_int(re.id_num, 6)) as f_num, re.exc_rate AS exc_rate, re.debit AS debit, " +
                "re.credit AS credit, re.debit_cur AS debit_cur, re.credit_cur AS credit_cur, re.debit - re.credit AS f_balance, " +
                "f_get_xml_atr('<cfdi:Emisor', 'nombre=', cfd.doc_xml) AS _xml_emisor_nombre, " +
                "f_get_xml_atr('<cfdi:Emisor', 'rfc=', cfd.doc_xml) AS _xml_emisor_rfc, " +
                "f_get_xml_atr('<cfdi:Receptor', 'nombre=', cfd.doc_xml) AS _xml_receptor_nombre, " +
                "f_get_xml_atr('<cfdi:Receptor', 'rfc=', cfd.doc_xml) AS _xml_receptor_rfc, " +
                "f_get_xml_atr('<cfdi:Comprobante', 'total=', cfd.doc_xml) AS _xml_total, " +
                "CAST(REPLACE(f_get_xml_atr('<cfdi:Complemento', 'FechaTimbrado=', cfd.doc_xml), 'T', ' ') AS DATETIME) AS _xml_fecha_timbrado, " +
                "f_get_xml_atr('<cfdi:Complemento', 'UUID=', cfd.doc_xml) AS _xml_uuid " +
                "FROM trn_cfd AS cfd " +
                "INNER JOIN fin_rec_ety AS re ON re.id_year = cfd.fid_rec_year_n AND re.id_per = cfd.fid_rec_per_n AND re.id_bkc = cfd.fid_rec_bkc_n AND re.id_tp_rec = cfd.fid_rec_tp_rec_n AND re.id_num = cfd.fid_rec_num_n AND re.id_ety = cfd.fid_rec_ety_n AND cfd.doc_xml <> '' " +
                "INNER JOIN fin_rec AS r ON r.id_year = re.id_year AND r.id_num = re.id_num AND  r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec " +
                "INNER JOIN fin_acc AS acc ON re.fid_acc = acc.id_acc " +
                "INNER JOIN erp.cfgu_cur AS cur ON re.fid_cur = cur.id_cur " +
                "INNER JOIN fin_bkc AS bkc ON r.id_bkc = bkc.id_bkc " +
                "INNER JOIN erp.bpsu_bpb AS cob ON r.fid_cob = cob.id_bpb " +
                "INNER JOIN erp.usru_usr AS uaud ON r.fid_usr_audit = uaud.id_usr " +
                "INNER JOIN erp.usru_usr AS uaut ON r.fid_usr_authorn = uaut.id_usr " +
                "INNER JOIN erp.usru_usr AS un ON r.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON r.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON r.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN fin_acc_cash AS ac ON r.fid_cob_n = ac.id_cob AND r.fid_acc_cash_n = ac.id_acc_cash " +
                "LEFT OUTER JOIN erp.cfgu_cob_ent AS e ON ac.id_cob = e.id_cob AND ac.id_acc_cash = e.id_ent " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? 
                "AND f_get_xml_atr('<cfdi:Emisor', 'rfc=',cfd.doc_xml) NOT LIKE '" + miClient.getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId() + "' " :
                "AND f_get_xml_atr('<cfdi:Emisor', 'rfc=',cfd.doc_xml) LIKE '" + miClient.getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId() + "' ") +
                "UNION " +
                "SELECT re.id_year AS id_year, re.id_per AS id_per, re.id_bkc AS id_bkc, re.id_tp_rec AS id_tp_rec, re.id_num AS id_num, re.id_ety AS id_ety, cfd.id_cfd AS id_cfd, r.dt AS dt, re.concept AS concept, re.b_del AS b_del, " +
                "re.ts_new AS ts_new, re.ts_edit AS ts_edit, re.ts_del AS ts_del, acc.id_acc AS f_acc, acc.acc AS f_acc_name, cur.cur_key AS f_cur, bkc.code AS code_bkc, cob.code AS code_cob, e.ent AS ent, un.usr AS usr_un, ue.usr AS usr_ue, " +
                "ud.usr AS usr_ud, CONCAT(re.id_year, '-', erp.lib_fix_int(re.id_per, 2)) as f_per, CONCAT(re.id_tp_rec, '-', erp.lib_fix_int(re.id_num, 6)) as f_num, re.exc_rate AS exc_rate, re.debit AS debit, " +
                "re.credit AS credit, re.debit_cur AS debit_cur, re.credit_cur AS credit_cur, re.debit - re.credit AS f_balance, " +
                "f_get_xml_atr('<cfdi:Emisor', 'nombre=', cfd.doc_xml) AS _xml_emisor_nombre, " +
                "f_get_xml_atr('<cfdi:Emisor', 'rfc=', cfd.doc_xml) AS _xml_emisor_rfc, " +
                "f_get_xml_atr('<cfdi:Receptor', 'nombre=', cfd.doc_xml) AS _xml_receptor_nombre, " +
                "f_get_xml_atr('<cfdi:Receptor', 'rfc=', cfd.doc_xml) AS _xml_receptor_rfc, " +
                "f_get_xml_atr('<cfdi:Comprobante', 'total=', cfd.doc_xml) AS _xml_total, " +
                "CAST(REPLACE(f_get_xml_atr('<cfdi:Complemento', 'FechaTimbrado=', cfd.doc_xml), 'T', ' ') AS DATETIME) AS _xml_fecha_timbrado, " +
                "f_get_xml_atr('<cfdi:Complemento', 'UUID=', cfd.doc_xml) AS _xml_uuid " +
                "FROM trn_cfd AS cfd " +
                "INNER JOIN trn_dps AS dps ON cfd.fid_dps_year_n = dps.id_year AND cfd.fid_dps_doc_n = dps.id_doc " +
                "INNER JOIN fin_rec_ety AS re ON re.fid_dps_year_n = dps.id_year AND re.fid_dps_doc_n = dps.id_doc " +
                "INNER JOIN fin_rec AS r ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
                "INNER JOIN fin_acc AS acc ON re.fid_acc = acc.id_acc " +
                "INNER JOIN erp.cfgu_cur AS cur ON re.fid_cur = cur.id_cur " +
                "INNER JOIN fin_bkc AS bkc ON r.id_bkc = bkc.id_bkc " +
                "INNER JOIN erp.bpsu_bpb AS cob ON r.fid_cob = cob.id_bpb " +
                "INNER JOIN erp.usru_usr AS uaud ON r.fid_usr_audit = uaud.id_usr " +
                "INNER JOIN erp.usru_usr AS uaut ON r.fid_usr_authorn = uaut.id_usr " +
                "INNER JOIN erp.usru_usr AS un ON r.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON r.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON r.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN fin_acc_cash AS ac ON r.fid_cob_n = ac.id_cob AND r.fid_acc_cash_n = ac.id_acc_cash " +
                "LEFT OUTER JOIN erp.cfgu_cob_ent AS e ON ac.id_cob = e.id_cob AND ac.id_acc_cash = e.id_ent " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "AND dps.fid_ct_dps = " + mnTabTypeAux01 +
                ") AS query GROUP BY id_year, id_per, id_bkc, id_tp_rec, id_num, id_ety, id_cfd, dt, concept, b_del, ts_new, " +
                "ts_edit, ts_del, code_bkc, code_cob, ent, usr_un, usr_ue, usr_ud ORDER BY id_year, id_per, code_bkc, id_bkc, id_tp_rec, id_num, id_ety, dt, id_cfd";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbGetXml) {
                actionGetXml();
            }
        }
    }
}

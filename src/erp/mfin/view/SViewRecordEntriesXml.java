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
import erp.lib.table.STabFilterSwitch;
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
 * @author Juan Barajas, Gerardo Hernández, Sergio Flores
 */
public class SViewRecordEntriesXml extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbGetXml;
    
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.mfin.view.SPanelFilterRecordType moPanelFilterRecordType;
    private erp.lib.table.STabFilterSwitch moTabFilterSwitch;

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
        moTabFilterSwitch = new STabFilterSwitch(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moPanelFilterRecordType);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterSwitch);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbGetXml);

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);
        jbGetXml.setEnabled(true);

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[29];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_cfd");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_per", "Período póliza", STableConstants.WIDTH_YEAR_PERIOD);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_bkc_code", "Centro contable", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_num", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt", "Fecha póliza", STableConstants.WIDTH_DATE);
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
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_xml_emisor", "XML emisor", STableConstants.WIDTH_ITEM_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_xml_emisor_rfc", "XML RFC emisor", STableConstants.WIDTH_ITEM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_xml_receptor", "XML receptor", STableConstants.WIDTH_ITEM_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_xml_receptor_rfc", "XML RFC receptor", STableConstants.WIDTH_ITEM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_xml_total", "XML total $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_xml_moneda", "XML moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "_xml_timbrado", "XML timbrado", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_xml_uuid", "XML UUID", STableConstants.WIDTH_ITEM_3X);
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
        String sql1 = "";
        String sql2 = "";
        String sqlWhere = "";
        String sqlSwitch = "";
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
            else if (setting.getType() == STableConstants.SETTING_OPTION_SWITCH && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlSwitch = "AND re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_CT_SYS_MOV_BPS + " ";
            }
        }

        sql1 = "SELECT re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.id_ety, cfd.id_cfd, r.dt, re.concept, re.b_del, " +
                "bkc.code AS _bkc_code, cob.code AS _cob_code, e.ent, acc.id_acc, acc.acc, " +
                "re.debit, re.credit, re.exc_rate, re.debit_cur, re.credit_cur, cur.cur_key, " +
                "CONCAT(re.id_year, '-', erp.lib_fix_int(re.id_per, 2)) as _per, " +
                "CONCAT(re.id_tp_rec, '-', erp.lib_fix_int(re.id_num, 6)) as _num, " +
                "un.usr AS _usr_new, ue.usr AS _usr_edit, ud.usr AS _usr_del, re.ts_new, re.ts_edit, re.ts_del, " +
                "erp.f_get_xml_atr('<cfdi:Emisor', 'nombre=', cfd.doc_xml) AS _xml_emisor, " +
                "erp.f_get_xml_atr('<cfdi:Emisor', 'rfc=', cfd.doc_xml) AS _xml_emisor_rfc, " +
                "erp.f_get_xml_atr('<cfdi:Receptor', 'nombre=', cfd.doc_xml) AS _xml_receptor, " +
                "erp.f_get_xml_atr('<cfdi:Receptor', 'rfc=', cfd.doc_xml) AS _xml_receptor_rfc, " +
                "erp.f_get_xml_atr('<cfdi:Comprobante', 'total=', cfd.doc_xml) AS _xml_total, " +
                "erp.f_get_xml_atr('<cfdi:Comprobante', 'moneda=', cfd.doc_xml) AS _xml_moneda, " +
                "CAST(REPLACE(erp.f_get_xml_atr('<cfdi:Complemento', 'FechaTimbrado=', cfd.doc_xml), 'T', ' ') AS DATETIME) AS _xml_timbrado, " +
                "erp.f_get_xml_atr('<cfdi:Complemento', 'UUID=', cfd.doc_xml) AS _xml_uuid " +
                "FROM trn_cfd AS cfd ";
        
        sql2 = "INNER JOIN fin_rec AS r ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num " +
                "INNER JOIN fin_acc AS acc ON re.fid_acc = acc.id_acc " +
                "INNER JOIN erp.cfgu_cur AS cur ON re.fid_cur = cur.id_cur " +
                "INNER JOIN fin_bkc AS bkc ON r.id_bkc = bkc.id_bkc " +
                "INNER JOIN erp.bpsu_bpb AS cob ON r.fid_cob = cob.id_bpb " +
                "INNER JOIN erp.usru_usr AS un ON re.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON re.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON re.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN erp.cfgu_cob_ent AS e ON r.fid_cob_n = e.id_cob AND r.fid_acc_cash_n = e.id_ent " +
                "WHERE " + sqlWhere;
        
        msSql = "SELECT * FROM (" +
                sql1 + 
                "INNER JOIN fin_rec_ety AS re ON re.id_year = cfd.fid_rec_year_n AND re.id_per = cfd.fid_rec_per_n AND re.id_bkc = cfd.fid_rec_bkc_n AND re.id_tp_rec = cfd.fid_rec_tp_rec_n AND re.id_num = cfd.fid_rec_num_n AND re.id_ety = cfd.fid_rec_ety_n AND cfd.doc_xml <> '' " +
                sql2 +
                "AND " +
                (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? 
                "erp.f_get_xml_atr('<cfdi:Emisor', 'rfc=', cfd.doc_xml) <> '" + miClient.getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId() + "' " :
                "erp.f_get_xml_atr('<cfdi:Emisor', 'rfc=', cfd.doc_xml) = '" + miClient.getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId() + "' ") +
                "UNION " +
                sql1 + 
                "INNER JOIN trn_dps AS dps ON cfd.fid_dps_year_n = dps.id_year AND cfd.fid_dps_doc_n = dps.id_doc " +
                "INNER JOIN fin_rec_ety AS re ON re.fid_dps_year_n = dps.id_year AND re.fid_dps_doc_n = dps.id_doc " +
                sql2 +
                "AND " +
                "dps.fid_ct_dps = " + mnTabTypeAux01 + " " + sqlSwitch +
                ") AS query " +
                "ORDER BY id_year, id_per, _bkc_code, id_bkc, id_tp_rec, id_num, id_ety, dt, id_cfd ";
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

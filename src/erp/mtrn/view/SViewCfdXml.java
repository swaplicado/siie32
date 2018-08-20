/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.cfd.SCfdConsts;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.table.STabFilterDateCutOff;
import erp.lib.table.STabFilterDatePeriodRange;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableSetting;
import erp.mod.SModConsts;
import sa.lib.grid.SGridConsts;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SViewCfdXml extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STableColumn[] aoTableColumns;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;
    private erp.lib.table.STabFilterDateCutOff moTabFilterDateCutOff;

    public SViewCfdXml(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRN_CFD, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        int i = 0;

        if (!isCfdiSignPending()) {
            moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);
            moTabFilterDateCutOff = null;
        }
        else {
            moTabFilterDatePeriodRange = null;
            moTabFilterDateCutOff = new STabFilterDateCutOff(miClient, this, SLibTimeUtilities.getEndOfYear(miClient.getSessionXXX().getWorkingDate()));
        }

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        addTaskBarUpperComponent(!isCfdiSignPending() ? moTabFilterDatePeriodRange : moTabFilterDateCutOff);

        aoTableColumns = new STableColumn[!isCfdiSignPending() ? 9 : 8];

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_cfd", "Tipo CFD", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_tp_doc", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uuid", "UUID", 250);
        if (!isCfdiSignPending()) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_pac", "PAC", 150);
        }
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico", "Estatus", STableConstants.WIDTH_ICON);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico_xml", "CFD", STableConstants.WIDTH_ICON);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }
        setIsSummaryApplying(true);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.TRN_DPS);
        mvSuscriptors.add(SModConsts.HRS_SIE_PAY);

        populateTable();
    }

    private boolean isCfdiSignPending() {
        return mnTabTypeAux01 == SDataConstants.TRNX_STAMP_SIGN_PEND;
    }
    
    private boolean isCfdiPayroll() {
        return mnTabTypeAux02 == SCfdConsts.CFDI_PAYROLL_VER_OLD || mnTabTypeAux02 == SCfdConsts.CFDI_PAYROLL_VER_CUR;
    }
    
    private boolean isCfdiPayrollVersionOld() {
        return mnTabTypeAux02 == SCfdConsts.CFDI_PAYROLL_VER_OLD;
    }

    @Override
    public void createSqlQuery() {
        java.util.Date[] range = null;
        String sqlDatePeriod = "";
        String sqlDatePeriodPayroll = "";
        String dateInit = "";
        String dateEnd = "";
        STableSetting setting = null;
        
        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                if (!isCfdiSignPending()) {
                    range = (java.util.Date[])setting.getSetting();
                    dateInit = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0]);
                    dateEnd = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]);
                    sqlDatePeriod += " AND d.dt BETWEEN '" + dateInit + "' AND '" + dateEnd + "' ";
                    sqlDatePeriodPayroll += " AND dx.ts BETWEEN '" + dateInit + "' AND '" + dateEnd + "' ";
                }
                else {
                    sqlDatePeriod = setting.getSetting() == null ? "" : " AND d.dt <= '" + (new java.sql.Date(((java.util.Date) setting.getSetting()).getTime())) + "' ";
                    sqlDatePeriodPayroll = setting.getSetting() == null ? "" : " AND dx.ts <= '" + (new java.sql.Date(((java.util.Date) setting.getSetting()).getTime())) + "' ";
                }
            }
        }

         msSql = "SELECT d.dt AS f_dt, dt.code AS f_tp_doc, tp.tp_cfd, " + (!isCfdiSignPending() ? "vt.pac AS f_pac, " : "") +
                 "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, cob.code AS f_cob, dx.uuid, " +
                 "IF(d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", " + STableConstants.ICON_ST_ANNUL + ", " + STableConstants.ICON_NULL + ") AS f_ico, " +
                 //"IF(dx.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " OR LENGTH(dx.uuid) = 0, " + STableConstants.ICON_XML_PEND + ", " + STableConstants.ICON_XML_SIGN + ") AS f_ico_xml " +
                 "IF(dx.ts IS NULL OR dx.doc_xml = '', " + STableConstants.ICON_NULL  + ", " + /* not is CFD not is CFDI */
                 "IF(dx.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_CFD + " OR dx.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_NA + ", " + STableConstants.ICON_XML + ", " + /* is CFD */
                 "IF(dx.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " OR LENGTH(dx.uuid) = 0, " + STableConstants.ICON_XML_PEND + ", " + /* CFDI pending sign */
                 "IF(LENGTH(dx.ack_can_xml) = 0 AND dx.ack_can_pdf_n IS NULL, " + STableConstants.ICON_XML_SIGN  + ", " + /* CFDI signed, canceled only SIIE */
                 "IF(LENGTH(dx.ack_can_xml) != 0, " + STableConstants.ICON_XML_CANC_XML + ", " + /* CFDI canceled with cancellation acknowledgment in XML format */
                 "IF(dx.ack_can_pdf_n IS NOT NULL, " + STableConstants.ICON_XML_CANC_PDF + ", " + /* CFDI canceled with cancellation acknowledgment in PDF format */
                 STableConstants.ICON_XML_SIGN + " " + /* CFDI signed, canceled only SIIE */
                 ")))))) AS f_ico_xml " +
                 "FROM trn_dps AS d " +
                 "INNER JOIN trn_cfd AS dx ON d.id_year = dx.fid_dps_year_n AND d.id_doc = dx.fid_dps_doc_n ";
         
         if (!isCfdiSignPending()) {
             msSql += "INNER JOIN trn_sign AS xs ON dx.id_cfd = xs.fid_cfd_n " +
                        "INNER JOIN trn_pac AS vt ON xs.id_pac = vt.id_pac ";
         }
                         
        msSql += "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb " +
                 "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
                 "INNER JOIN erp.trns_tp_cfd AS tp ON dx.fid_tp_cfd = tp.id_tp_cfd " +
                 "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                 "WHERE dx.fid_tp_cfd = " + (isCfdiPayroll() ? SDataConstantsSys.TRNS_TP_CFD_PAYROLL : SDataConstantsSys.TRNS_TP_CFD_INV) + " AND dx.fid_tp_xml IN (" + SDataConstantsSys.TRNS_TP_XML_CFDI_32 + ", " + SDataConstantsSys.TRNS_TP_XML_CFDI_33 + ") AND d.b_del = 0 " + 
                 "AND NOT (dx.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " AND dx.b_con = 0) " + (isCfdiSignPending() ? " AND LENGTH(dx.uuid) = 0 " : " AND LENGTH(dx.uuid) <> 0 ") + sqlDatePeriod + " " +
                 
                 "UNION " +
                 
                 "SELECT dx.ts AS f_dt, '' AS f_tp_doc, tp.tp_cfd, " + (!isCfdiSignPending() ? "vt.pac AS f_pac, " : "") +
                 "CONCAT(hr.num_ser, IF(length(hr.num_ser) = 0, '', '-'), hr.num) AS f_num, '' AS f_cob, dx.uuid, " +
                 (isCfdiPayrollVersionOld() ? "IF(dx.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", " + STableConstants.ICON_ST_ANNUL + ", " + STableConstants.ICON_NULL + ") AS f_ico, " +
                 "IF(dx.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + ", " + STableConstants.ICON_XML_PEND + ", " + STableConstants.ICON_XML_SIGN + ") AS f_ico_xml " :
                 "IF(hr.fk_st_rcp = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", " + STableConstants.ICON_ST_ANNUL + ", " + STableConstants.ICON_NULL + ") AS f_ico, " +
                 "IF(dx.ts IS NULL OR dx.doc_xml = '', " + STableConstants.ICON_NULL  + ", " /* without icon (not have CFDI associated) */ +
                 "IF(dx.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " OR LENGTH(dx.uuid) = 0, " + STableConstants.ICON_XML_PEND + ", " /* CFDI pending sign */ +
                 "IF(LENGTH(dx.ack_can_xml) = 0 AND dx.ack_can_pdf_n IS NULL, " + STableConstants.ICON_XML_SIGN + ", " /* CFDI signed, canceled only SIIE */ +
                 "IF(LENGTH(dx.ack_can_xml) != 0, " + STableConstants.ICON_XML_CANC_XML + ", " /* CFDI canceled with cancellation acknowledgment in XML format */ +
                 "IF(dx.ack_can_pdf_n IS NOT NULL, " + STableConstants.ICON_XML_CANC_PDF + ", " /* CFDI canceled with cancellation acknowledgment in PDF format */ +
                 SGridConsts.ICON_XML_ISSU + "" /* CFDI signed, canceled only SIIE */ +
                 "))))) AS f_ico_xml ") +
                 "FROM trn_cfd AS dx ";
         
         if (!isCfdiSignPending()) {
             msSql += "INNER JOIN trn_sign AS xs ON dx.id_cfd = xs.fid_cfd_n " +
                        "INNER JOIN trn_pac AS vt ON xs.id_pac = vt.id_pac ";
         }
                         
        msSql += (isCfdiPayrollVersionOld() ? "INNER JOIN hrs_sie_pay_emp AS hr ON dx.fid_pay_pay_n = hr.id_pay AND dx.fid_pay_emp_n = hr.id_emp AND dx.fid_pay_bpr_n = hr.fid_bpr_n AND hr.b_del = FALSE " : 
                 "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS r ON dx.fid_pay_rcp_pay_n = r.id_pay AND dx.fid_pay_rcp_emp_n = r.id_emp " +
                 "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS hr ON " +
                 "r.id_pay = hr.id_pay AND r.id_emp = hr.id_emp AND hr.b_del = 0 AND hr.id_iss = dx.fid_pay_rcp_iss_n ") +
                 "INNER JOIN erp.trns_tp_cfd AS tp ON dx.fid_tp_cfd = tp.id_tp_cfd " +
                 "WHERE dx.fid_tp_cfd = " + (isCfdiPayroll() ? SDataConstantsSys.TRNS_TP_CFD_PAYROLL : SDataConstantsSys.TRNS_TP_CFD_INV)+ " AND dx.fid_tp_xml IN (" + SDataConstantsSys.TRNS_TP_XML_CFDI_32 + ", " + SDataConstantsSys.TRNS_TP_XML_CFDI_33 + ") AND hr.b_del = 0 " + 
                 "AND NOT (dx.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " AND dx.b_con = 0) " + (isCfdiSignPending() ? " AND LENGTH(dx.uuid) = 0 " : " AND LENGTH(dx.uuid) <> 0 ") + sqlDatePeriodPayroll + " " +
                 "ORDER BY tp_cfd, f_tp_doc, f_dt, f_num, f_cob, uuid, f_ico, f_ico_xml";
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

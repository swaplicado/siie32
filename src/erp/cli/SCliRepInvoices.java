/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cli;

import erp.SClient;
import erp.data.SDataConstantsSys;
import erp.mod.trn.db.STrnUtils;
import erp.mtrn.data.STrnUtilities;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbDatabase;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import sa.lib.mail.SMailUtils;

/**
 *
 * @author Sergio Flores
 */
public class SCliRepInvoices {
    
    private static final int ARGS_REQ = 6;
    private static final int ARG_IDX_DPS_CATEGORY = 2; // required
    private static final int ARG_IDX_PERIOD_START = 3; // required
    private static final int ARG_IDX_PERIOD_END = 4; // required
    private static final int ARG_IDX_MAILS_TO = 5; // required
    private static final int ARG_IDX_MAILS_BCC = 6; // optional
    private static final int DOC_CLASS_INV_ID = 3;
    private static final int DOC_CLASS_CN_ID = 5;
    private static final String DPS_CATEGORY_SAL = "S";
    private static final String DPS_CATEGORY_PUR = "P";
    private static final String PERIOD_TODAY = "TODAY";
    private static final String ZERO = "&nbsp;&nbsp;-&nbsp;&nbsp;";
    private static final int ROW_GRP_HDR = 1;
    private static final int ROW_GRP_FTR = 2;
    private static final int ROW_ROW = 3;
    private static final int ROW_TOT_CUR = 4;
    private static final int ROW_TOT_LOC_CUR = 5;
    private static final DecimalFormat FormatInteger = new DecimalFormat("#,##0;(#,##0)");
    private static final DecimalFormat FormatAmount = new DecimalFormat("#,##0.00;(#,##0.00)");
    private static final DecimalFormat FormatAmountUnit = new DecimalFormat("#,##0.0000;(#,##0.0000)");
    private static final DecimalFormat FormatQuantity = new DecimalFormat("#,##0.000;(#,##0.000)");
    private static HashMap<Integer, String> DocClasses = new HashMap<>();
    
    static {
        DocClasses.put(SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1], "Factura");
        DocClasses.put(SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1], "Nota de crédito");
    }
    
    private String[] maArgs;
    private SDbDatabase moDatabase;
    
    private String msArgDpsCategory;
    private String msArgPeriodStart;
    private String msArgPeriodEnd;
    private String msArgMailsTo;
    private String msArgMailsBcc;

    private int[] manDpsClassKeyInv;
    private int[] manDpsClassKeyCn;
    private String msDpsCategoryName;
    private Date mtPeriodStart;
    private Date mtPeriodEnd;
    private ArrayList<String> maRecipientsTo;
    private ArrayList<String> maRecipientsBcc;
    
    private String msCompanyName;
    private int mnLocalCurrencyId;
    private String msLocalCurrencyCode;
    private Statement miStatement;
    
    /**
     * Create report of summary invoices of items and business partners.
     * When period start and end dates are from the same month, a monthly accumulated summary is appended.
     * @param args Expected arguments:
     * index 0: mailer option ('I');
     * index 1: category: sales ('S') or purchases ('P');
     * index 2: period start date in format "yyyy-MM-dd" or TODAY[{+|-}n];
     * index 3: period end date in format "yyyy-MM-dd" or TODAY[{+|-}n];
     * index 4: TO recepients separated by semicolon;
     * index 5: (optional) BCC recepients separated by semicolon.
     * @param database Database.
     * @throws java.lang.Exception
     */
    public SCliRepInvoices(final String[] args, final SDbDatabase database) throws Exception {
        maArgs = args;
        moDatabase = database;
        initMembers();
    }
    
    /**
     * Get shift days in dynamic date.
     * @param date Dynamic date in format TODAY{+|-}n
     * @return Shift days in dynamic date.
     */
    private int getShiftDays(final String date) {
        int shiftDays = 0;
        int posPlus = date.indexOf('+');
        int posMinus = date.indexOf('-');
        
        if (posPlus != -1) {
            shiftDays = SLibUtils.parseInt(date.substring(posPlus + 1, date.length()));
        }
        else if (posMinus != -1) {
            shiftDays = -SLibUtils.parseInt(date.substring(posMinus + 1, date.length()));
        }
        
        return shiftDays;
    }
    
    /**
     * Initialize members.
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    private void initMembers() throws Exception {
        if (maArgs.length < ARGS_REQ) {
            throw new Exception(SCliMailer.ERR_ARGS_INVALID);
        }
        else {
            msArgDpsCategory = maArgs[ARG_IDX_DPS_CATEGORY].toUpperCase();
            msArgPeriodStart = maArgs[ARG_IDX_PERIOD_START].toUpperCase();
            msArgPeriodEnd = maArgs[ARG_IDX_PERIOD_END].toUpperCase();
            msArgMailsTo = maArgs[ARG_IDX_MAILS_TO];
            
            if (maArgs.length > ARGS_REQ) {
                msArgMailsBcc = maArgs[ARG_IDX_MAILS_BCC];
            }
            else {
                msArgMailsBcc = "";
            }
            
            switch (msArgDpsCategory) {
                case DPS_CATEGORY_SAL:
                    manDpsClassKeyInv = SDataConstantsSys.TRNS_CL_DPS_SAL_DOC;
                    manDpsClassKeyCn = SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ;
                    msDpsCategoryName = STrnUtils.getDpsCategoryName(SDataConstantsSys.TRNS_CT_DPS_SAL, SUtilConsts.NUM_PLR);
                    break;
                case DPS_CATEGORY_PUR:
                    manDpsClassKeyInv = SDataConstantsSys.TRNS_CL_DPS_PUR_DOC;
                    manDpsClassKeyCn = SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ;
                    msDpsCategoryName = STrnUtils.getDpsCategoryName(SDataConstantsSys.TRNS_CT_DPS_PUR, SUtilConsts.NUM_PLR);
                    break;
                default:
                    throw new Exception(SCliMailer.ERR_ARGS_INVALID);
            }
            
            Date today = null;
            
            if (msArgPeriodStart.contains(PERIOD_TODAY)) {
                today = SLibTimeUtils.convertToDateOnly(new Date());
                mtPeriodStart = SLibTimeUtils.addDate(today, 0, 0, getShiftDays(msArgPeriodStart));
            }
            else {
                mtPeriodStart = SLibUtils.DbmsDateFormatDate.parse(msArgPeriodStart);
            }
            
            if (msArgPeriodEnd.contains(PERIOD_TODAY)) {
                if (today == null) {
                    today = SLibTimeUtils.convertToDateOnly(new Date());
                }
                mtPeriodEnd = SLibTimeUtils.addDate(today, 0, 0, getShiftDays(msArgPeriodEnd));
            }
            else {
                mtPeriodEnd = SLibUtils.DbmsDateFormatDate.parse(msArgPeriodEnd);
            }

            maRecipientsTo = new ArrayList<>(Arrays.asList(SLibUtils.textExplode(msArgMailsTo, ";")));
            
            if (!msArgMailsBcc.isEmpty()) {
                maRecipientsBcc = new ArrayList<>(Arrays.asList(SLibUtils.textExplode(msArgMailsBcc, ";")));
            }
            
            String sql;
            miStatement = moDatabase.getConnection().createStatement();
            
            // company info
            
            sql = "SELECT co "
                    + "FROM erp.cfgu_co "
                    + "WHERE id_co = " + maArgs[SCliMailer.ARG_IDX_COMPANY_ID] + ";";
            
            try (ResultSet resultSet = miStatement.executeQuery(sql)) {
                if (resultSet.next()) {
                    msCompanyName = resultSet.getString("co");
                }
            }
            
            // local currency info
                    
            sql = "SELECT cur.id_cur, cur.cur_key "
                    + "FROM erp.cfg_param_erp AS cfg "
                    + "INNER JOIN erp.cfgu_cur AS cur ON cfg.fid_cur = cur.id_cur;";
            
            try (ResultSet resultSet = miStatement.executeQuery(sql)) {
                if (resultSet.next()) {
                    mnLocalCurrencyId = resultSet.getInt("id_cur");
                    msLocalCurrencyCode = resultSet.getString("cur_key");
                }
            }
        }
    }
    
    /**
     * Compose SQL sentence for report.
     * @param manDpsClassKeyInv Key of class of invoices.
     * @param manDpsClassKeyCn Key of class of credit notes.
     * @param mtPeriodStart Period start.
     * @param mtPeriodEnd Period end.
     * @return 
     */
    private String composeSql() {
        String sql = "SELECT "
                + "item_key, item, id_item, unit_symbol, id_unit, "
                + "bp, bp_comm, id_bp, cur_key, id_cur, _price, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_NA + " THEN _qty ELSE 0.00 END) AS _qty_inv, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_NA + " THEN _stot_cur_r ELSE 0.00 END) AS _stot_cur_r_inv, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_NA + " THEN _stot_r ELSE 0.00 END) AS _stot_r_inv, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " THEN _qty ELSE 0.00 END) AS _qty_cn_ret, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " THEN _stot_cur_r ELSE 0.00 END) AS _stot_cur_r_cn_ret, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " THEN _stot_r ELSE 0.00 END) AS _stot_r_cn_ret, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC + " THEN _qty ELSE 0.00 END) AS _qty_cn_disc, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC + " THEN _stot_cur_r ELSE 0.00 END) AS _stot_cur_r_cn_disc, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC + " THEN _stot_r ELSE 0.00 END) AS _stot_r_cn_disc, "
                + "SUM(_qty) AS _qty_net, SUM(_stot_cur_r) AS _stot_cur_r_net, SUM(_stot_r) AS _stot_r_net "
                + "FROM ("
                + "SELECT "
                + "i.item_key, i.item, i.id_item, u.symbol AS unit_symbol, u.id_unit, "
                + "bp.bp, bp.bp_comm, bp.id_bp, c.cur_key, c.id_cur, de.fid_tp_dps_adj, de.price_u_cur AS _price, "
                + "SUM(de.qty) AS _qty, "
                + "SUM(de.stot_cur_r) AS _stot_cur_r, SUM(de.stot_r) AS _stot_r "
                + "FROM "
                + "trn_dps AS d "
                + "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc "
                + "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur "
                + "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp "
                + "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item "
                + "INNER JOIN erp.itmu_unit AS u ON de.fid_unit = u.id_unit "
                + "WHERE NOT d.b_del AND NOT de.b_del AND "
                + "d.fid_ct_dps = " + manDpsClassKeyInv[0] + " AND d.fid_cl_dps = " + manDpsClassKeyInv[1] + " AND "
                + "d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                + "d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND de.fid_tp_dps_ety <> " + SDataConstantsSys.TRNS_TP_DPS_ETY_VIRT + " "
                + "GROUP BY "
                + "i.item_key, i.item, i.id_item, u.symbol, u.id_unit, "
                + "bp.bp, bp.bp_comm, bp.id_bp, c.cur_key, c.id_cur, de.fid_tp_dps_adj, de.price_u_cur "
                + "UNION ALL "
                + "SELECT "
                + "i.item_key, i.item, i.id_item, u.symbol AS unit_symbol, u.id_unit, "
                + "bp.bp, bp.bp_comm, bp.id_bp, c.cur_key, c.id_cur, de.fid_tp_dps_adj, de.price_u_cur AS _price, "
                + "SUM(CASE WHEN de.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " THEN -de.qty ELSE 0.0 END) AS _qty, "
                + "SUM(-de.stot_cur_r) AS _stot_cur_r, SUM(-de.stot_r) AS _stot_r "
                + "FROM "
                + "trn_dps AS d "
                + "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc "
                + "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur "
                + "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp "
                + "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item "
                + "INNER JOIN erp.itmu_unit AS u ON de.fid_unit = u.id_unit "
                + "WHERE "
                + "NOT d.b_del AND NOT de.b_del AND d.fid_ct_dps = " + manDpsClassKeyCn[0] + " AND d.fid_cl_dps = " + manDpsClassKeyCn[1] + " AND "
                + "d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                + "d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND de.fid_tp_dps_ety <> " + SDataConstantsSys.TRNS_TP_DPS_ETY_VIRT + " "
                + "GROUP BY "
                + "i.item_key, i.item, i.id_item, u.symbol, u.id_unit, "
                + "bp.bp, bp.bp_comm, bp.id_bp, c.cur_key, c.id_cur, de.fid_tp_dps_adj, de.price_u_cur "
                + "ORDER BY "
                + "item_key, item, id_item, unit_symbol, id_unit, "
                + "bp, bp_comm, id_bp, cur_key, id_cur, fid_tp_dps_adj, _price "
                + ") AS t "
                + "GROUP BY "
                + "item_key, item, id_item, unit_symbol, id_unit, "
                + "bp, bp_comm, id_bp, cur_key, id_cur, _price "
                + "ORDER BY "
                + "item_key, item, id_item, unit_symbol, id_unit, "
                + "bp, bp_comm, id_bp, cur_key, id_cur, _price;";
        
        return sql;
    }
    
    private String composeMailSubject() throws Exception {
        String subject = "[" + SClient.APP_NAME + "] " + msDpsCategoryName + " " + SLibUtils.DateFormatDate.format(mtPeriodStart);
        
        if (!mtPeriodStart.equals(mtPeriodEnd)) {
            subject += " - " + SLibUtils.DateFormatDate.format(mtPeriodEnd);
        }
        
        return SMailUtils.encodeSubjectUtf8(subject);
    }
    
    private String formatInteger(final int integer) {
        return integer == 0 ? ZERO : FormatInteger.format(integer);
    }
    
    private String formatAmount(final double amount) {
        return amount == 0d ? ZERO : FormatAmount.format(amount);
    }
    
    private String formatAmountUnit(final double amountUnit) {
        return amountUnit == 0d ? ZERO : FormatAmountUnit.format(amountUnit);
    }
    
    private String formatQuantity(final double quantity) {
        return quantity == 0d ? ZERO : FormatQuantity.format(quantity);
    }
    
    private String composeMailBodyHtmlHead() throws Exception {
        // start of head
        String head = "<head>\n";
        
        head += "<style>\n";
        
        head += ".number {"
                + " text-align: right;"
                + "} "
                + ".center {"
                + " text-align: center;"
                + "} "
                + ".warning {"
                + " font-size: 0.75em;"
                + "} "
                + "body {"
                + " font-size: 100%;"
                + " font-family: sans-serif;"
                + "} "
                + "h1 {"
                + " font-size: 2.00em;"
                + "} "
                + "h2 {"
                + " font-size: 1.75em;"
                + "} "
                + "h3 {"
                + " font-size: 1.50em;"
                + "} "
                + "h4 {"
                + " font-size: 1.25em;"
                + "} "
                + "p {"
                + " font-size: 0.875em;"
                + "} "
                + "table {"
                + " font-size: 0.875em;"
                + "} "
                + "table, th, td {"
                + " border: 1px solid black;"
                + " border-collapse: collapse;"
                + "} "
                + "th {"
                + " padding: 2px;"
                + " text-align: center;"
                + " background-color: Gainsboro;"
                + " white-space: nowrap;"
                + "} "
                + "td {"
                + " padding: 2px;"
                + "} "
                + "td.grphdr {"
                + " padding-top: 15px;"
                + " font-size: 1.5em;"
                + " background-color: rgb(250, 250, 250);"
                + "} "
                + "td.grpftr {"
                + " padding-top: 5px;"
                + " padding-bottom: 10px;"
                + " font-weight: bold;"
                + "} "
                + "td.grpftrnumber {"
                + " padding-top: 5px;"
                + " padding-bottom: 5px;"
                + " font-weight: bold;"
                + " text-align: right;"
                + " white-space: nowrap;"
                + "} "
                + "td.totcur {"
                + " padding-top: 10px;"
                + " font-weight: bold;"
                + "} "
                + "td.totcurnumber {"
                + " padding-top: 10px;"
                + " font-weight: bold;"
                + " text-align: right;"
                + " white-space: nowrap;"
                + "} "
                + "td.totloccur {"
                + " padding-top: 10px;"
                + " font-weight: bold;"
                + "} "
                + "td.totloccurnumber {"
                + " padding-top: 10px;"
                + " font-weight: bold;"
                + " text-align: right;"
                + " white-space: nowrap;"
                + "}\n";
        
        head += "</style>\n";
        
        // end of head
        head += "</head>\n";
        
        return head;
    }
    
    private String composeHtmlTableRow(final Row row, final int rowLevel) {
        String html = "";
        String ccsClass = "";
        
        switch (rowLevel) {
            case ROW_GRP_FTR:
                ccsClass = "grpftr";
                break;
            case ROW_TOT_CUR:
                ccsClass = "totcur";
                break;
            case ROW_TOT_LOC_CUR:
                ccsClass = "totloccur";
                break;
            default:
        }
        
        html += "<tr>";
        
        if (rowLevel == ROW_ROW) {
            html += "<td class=\"" + ccsClass + "\">&nbsp;&nbsp;&nbsp;</td>";
            html += "<td class=\"" + ccsClass + "\">" + SLibUtils.textToHtml(row.Concept) + "</td>";
        }
        else {
            html += "<td class=\"" + ccsClass + "\" colspan=\"2\">" + SLibUtils.textToHtml(row.Concept) + "</td>";
        }
        
        html += "<td class=\"" + ccsClass + "number\">" + formatAmount(row.TotGross) + "</td>";
        html += "<td class=\"" + ccsClass + "number\">" + formatAmount(row.TotReturns) + "</td>";
        html += "<td class=\"" + ccsClass + "number\">" + formatAmount(row.TotDiscounts) + "</td>";
        html += "<td class=\"" + ccsClass + "number\">" + formatAmount(row.getTotNet()) + "</td>";
        html += "<td class=\"" + ccsClass + "\">" + SLibUtils.textToHtml(row.Currency) + "</td>";
        html += "<td class=\"" + ccsClass + "number\">" + formatQuantity(row.QtyGross) + "</td>";
        html += "<td class=\"" + ccsClass + "number\">" + formatQuantity(row.QtyReturns) + "</td>";
        html += "<td class=\"" + ccsClass + "number\">" + formatQuantity(row.getQtyNet()) + "</td>";
        html += "<td class=\"" + ccsClass + "\">" + SLibUtils.textToHtml(row.Unit) + "</td>";
        html += "<td class=\"" + ccsClass + "number\">" + (row.UnitId == 0 ? ZERO : formatAmountUnit(rowLevel == ROW_ROW ? row.Price : row.getAvgPrice())) + "</td>";
        
        html += "</tr>\n";
        
        return html;
    }
    
    private String composeHtmlDocsInfo() throws Exception {
        DocClass docClassInv = new DocClass(DOC_CLASS_INV_ID);
        DocClass docClassCn = new DocClass(DOC_CLASS_CN_ID);
        
        String sql = "SELECT "
                + "d.fid_cl_dps, d.fid_st_dps, dst.st_dps, c.fid_st_xml, cst.st_dps, COUNT(*) AS _count "
                + "FROM trn_dps AS d "
                + "INNER JOIN trn_cfd AS c ON c.fid_dps_year_n = d.id_year AND c.fid_dps_doc_n = d.id_doc "
                + "INNER JOIN erp.trns_st_dps AS dst ON dst.id_st_dps = d.fid_st_dps "
                + "INNER JOIN erp.trns_st_dps AS cst ON cst.id_st_dps = c.fid_st_xml "
                + "WHERE NOT d.b_del AND d.fid_ct_dps = " + manDpsClassKeyInv[0] + " AND "
                + "d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' "
                + "GROUP BY d.fid_cl_dps, d.fid_st_dps, c.fid_st_xml "
                + "ORDER BY d.fid_cl_dps, d.fid_st_dps, c.fid_st_xml;";
        
        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                DocClass docClass = null;
                
                switch (resultSet.getInt("d.fid_cl_dps")) {
                    case DOC_CLASS_INV_ID:
                        docClass = docClassInv;
                        break;
                        
                    case DOC_CLASS_CN_ID:
                        docClass = docClassCn;
                        break;
                        
                    default:
                }
                
                switch (resultSet.getInt("d.fid_st_dps")) {
                    case SDataConstantsSys.TRNS_ST_DPS_NEW:
                    case SDataConstantsSys.TRNS_ST_DPS_EMITED:
                        switch (resultSet.getInt("c.fid_st_xml")) {
                            case SDataConstantsSys.TRNS_ST_DPS_NEW:
                                docClass.DocsNew = resultSet.getInt("_count");
                                break;
                            case SDataConstantsSys.TRNS_ST_DPS_EMITED:
                                docClass.DocsEmited = resultSet.getInt("_count");
                                break;
                            default:
                        }
                        break;
                        
                    case SDataConstantsSys.TRNS_ST_DPS_ANNULED:
                        docClass.DocsAnnulled = resultSet.getInt("_count");
                        break;
                        
                    default:
                }
            }
        }
        
        String html = "<br>\n";
        
        html += "<h3>Comprobantes</h3>\n";
        
        html += "<table>\n";
        html += "<tr>"
                + "<th>Comprobante</th>"
                + "<th>S/timbre</th>"
                + "<th>C/timbre</th>"
                + "<th>Cancelados</th>"
                + "</tr>\n";
        html += "<tr>"
                + "<td>" + SLibUtils.textToHtml(DocClasses.get(DOC_CLASS_INV_ID)) + "</td>"
                + "<td class=\"center\">" + formatInteger(docClassInv.DocsNew) + "</td>"
                + "<td class=\"center\">" + formatInteger(docClassInv.DocsEmited) + "</td>"
                + "<td class=\"center\">" + formatInteger(docClassInv.DocsAnnulled) + "</td>"
                + "</tr>\n";
        html += "<tr>"
                + "<td>" + SLibUtils.textToHtml(DocClasses.get(DOC_CLASS_CN_ID)) + "</td>"
                + "<td class=\"center\">" + formatInteger(docClassCn.DocsNew) + "</td>"
                + "<td class=\"center\">" + formatInteger(docClassCn.DocsEmited) + "</td>"
                + "<td class=\"center\">" + formatInteger(docClassCn.DocsAnnulled) + "</td>"
                + "</tr>\n";
        html += "</table>\n";
        
        return html;
    }
    
    private String composeMailBodyHtmlBody() throws Exception {
        // start of body
        String body = "<body>\n";
        
        // title
        
        body += "<h1>" + SLibUtils.textToHtml(msCompanyName) + "</h1>\n";
        body += "<h2>" + msDpsCategoryName + " " + SLibUtils.DateFormatDate.format(mtPeriodStart);
        if (!mtPeriodStart.equals(mtPeriodEnd)) {
            body += " - " + SLibUtils.DateFormatDate.format(mtPeriodEnd);
        }
        body += "</h2>\n";
        
        // table
        
        // header of table
        
        body += "<table>\n";
        body += "<tr>";
        body += "<th colspan=\"2\">Asociado negocios</th>";
        body += "<th>Total $</th>";
        body += "<th>Devs. $</th>";
        body += "<th>Descs. $</th>";
        body += "<th>Total neto $</th>";
        body += "<th>Moneda</th>";
        body += "<th>Cant.</th>";
        body += "<th>Cant. devs.</th>";
        body += "<th>Cant. neta</th>";
        body += "<th>Unidad</th>";
        body += "<th>Precio $</th>";
        body += "</tr>\n";
        
        // rows of table
        
        int curItemId = 0;
        String curItemText = "";
        int curCurrencyId = 0;
        int curUnitId = 0;
        boolean dataFound = false;
        HashMap<Integer, String> mapCurrencies = new HashMap<>();
        HashMap<Integer, String> mapUnits = new HashMap<>();
        HashMap<Integer, Row> mapRowSubtotals = new HashMap<>(); // key is currency ID
        HashMap<Integer, Row> mapRowTotals = new HashMap<>(); // key is currency ID
        HashSet<Integer> setItemUnits = new HashSet<>(); // element is unit ID
        Row rowLocalTotal = new Row("TOTAL MONEDA LOCAL", mnLocalCurrencyId, msLocalCurrencyCode, 0, "", 0);
        
        mapCurrencies.put(mnLocalCurrencyId, msLocalCurrencyCode);
        
        String sql = composeSql();
        
        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                dataFound = true;
                if (curItemId != resultSet.getInt("id_item")) {
                    if (curItemId != 0) {
                        // render subtotals of last item
                        
                        for (Row row : mapRowSubtotals.values()) {
                            if (setItemUnits.size() == 1) { // check if there is only one unit for current item
                                row.UnitId = (Integer) setItemUnits.toArray()[0];
                                row.Unit = mapUnits.get(row.UnitId);
                            }
                            
                            body += composeHtmlTableRow(row, ROW_GRP_FTR);
                        }
                    }
                    
                    curItemId = resultSet.getInt("id_item");
                    curItemText = resultSet.getString("item_key") + " - " + resultSet.getString("item");
                    mapRowSubtotals.clear();
                    setItemUnits.clear();
                    
                    body += "<tr><td class=\"grphdr\" colspan=\"12\">" + SLibUtils.textToHtml(curItemText) + "</td></tr>\n";
                }
                
                curCurrencyId = resultSet.getInt("id_cur");
                if (mapCurrencies.get(curCurrencyId) == null) {
                    mapCurrencies.put(curCurrencyId, resultSet.getString("cur_key"));
                }
                
                curUnitId = resultSet.getInt("id_unit");
                if (mapUnits.get(curUnitId) == null) {
                    mapUnits.put(curUnitId, resultSet.getString("unit_symbol"));
                }
                
                setItemUnits.add(curUnitId);
                
                Row row = new Row(resultSet.getString("bp"), curCurrencyId, mapCurrencies.get(curCurrencyId), curUnitId, mapUnits.get(curUnitId), resultSet.getDouble("_price"));
                row.TotGross = resultSet.getDouble("_stot_cur_r_inv");
                row.TotReturns = resultSet.getDouble("_stot_cur_r_cn_ret");
                row.TotDiscounts = resultSet.getDouble("_stot_cur_r_cn_disc");
                row.QtyGross = resultSet.getDouble("_qty_inv");
                row.QtyReturns = resultSet.getDouble("_qty_cn_ret");
                
                body += composeHtmlTableRow(row, ROW_ROW);
                
                Row rowSubtotal = mapRowSubtotals.get(curCurrencyId);
                if (rowSubtotal == null) {
                    rowSubtotal = new Row(curItemText + " " + mapCurrencies.get(curCurrencyId), curCurrencyId, mapCurrencies.get(curCurrencyId), 0, "", 0);
                    mapRowSubtotals.put(curCurrencyId, rowSubtotal);
                }
                rowSubtotal.add(resultSet.getDouble("_stot_cur_r_inv"), resultSet.getDouble("_stot_cur_r_cn_ret"), resultSet.getDouble("_stot_cur_r_cn_disc"), 
                        resultSet.getDouble("_qty_inv"), resultSet.getDouble("_qty_cn_ret"));
                
                Row rowTotal = mapRowTotals.get(curCurrencyId);
                if (rowTotal == null) {
                    rowTotal = new Row("TOTAL " + mapCurrencies.get(curCurrencyId), curCurrencyId, mapCurrencies.get(curCurrencyId), 0, "", 0);
                    mapRowTotals.put(curCurrencyId, rowTotal);
                }
                rowTotal.add(resultSet.getDouble("_stot_cur_r_inv"), resultSet.getDouble("_stot_cur_r_cn_ret"), resultSet.getDouble("_stot_cur_r_cn_disc"), 
                        resultSet.getDouble("_qty_inv"), resultSet.getDouble("_qty_cn_ret"));
                
                rowLocalTotal.add(resultSet.getDouble("_stot_r_inv"), resultSet.getDouble("_stot_r_cn_ret"), resultSet.getDouble("_stot_r_cn_disc"), 
                        resultSet.getDouble("_qty_inv"), resultSet.getDouble("_qty_cn_ret"));
            }
            
            // render subtotals of last item
            
            for (Row row : mapRowSubtotals.values()) {
                if (setItemUnits.size() == 1) { // check if there is only one unit for current item
                    row.UnitId = (Integer) setItemUnits.toArray()[0];
                    row.Unit = mapUnits.get(row.UnitId);
                }
                
                body += composeHtmlTableRow(row, ROW_GRP_FTR);
            }
            
            if (dataFound) {
                // render totals

                body += "<tr><td class=\"grphdr\" colspan=\"12\">" + SLibUtils.textToHtml("TOTALES") + "</td></tr>\n";

                for (Row row : mapRowTotals.values()) {
                    body += composeHtmlTableRow(row, ROW_TOT_CUR);
                }

                if (mapRowTotals.size() > 1 || !mapRowTotals.containsKey(mnLocalCurrencyId)) {
                    body += composeHtmlTableRow(rowLocalTotal, ROW_TOT_LOC_CUR);
                }
            }
        }

        // end of table
        body += "</table>";
        
        if (dataFound) {
            body += composeHtmlDocsInfo();
        }
        else {
            body += "<p><strong>" + SLibUtils.textToHtml("¡No se encontró información para " + (mtPeriodStart.equals(mtPeriodEnd) ? "la fecha solicitada" : "el período solicitado") + "!") + "</strong></p>\n";
        }
        
        body += "<br>";
        body += STrnUtilities.composeMailFooter("warning");
        
        // end of body
        body += "</body>\n";
        
        return body;
    }
    
    /**
     * Send report by mail.
     * @throws Exception 
     */
    public void sendMail() throws Exception {
        String html = "<html>\n";
        html += composeMailBodyHtmlHead();
        html += composeMailBodyHtmlBody();
        html += "</html>";
        
        String subject = composeMailSubject();
        
        SMailSender sender = new SMailSender("mail.swaplicado.com.mx", "26", "smtp", false, true, "siie@swaplicado.com.mx", "s11E2020!+", "siie@swaplicado.com.mx");
        //SMailSender sender = new SMailSender("mail.swaplicado.com.mx", "26", "smtp", false, true, "cap@swaplicado.com.mx", "C492020*&", "cap@swaplicado.com.mx");
        //SMailSender sender = new SMailSender("mail.swaplicado.com.mx", "26", "smtp", false, true, "sflores@swaplicado.com.mx", "Ch3c0m4n", "sflores@swaplicado.com.mx");
        //SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "som@aeth.mx", "AETHSOM", "som@aeth.mx");

        SMail mail = new SMail(sender, subject, html, maRecipientsTo);
        if (maRecipientsBcc != null && !maRecipientsBcc.isEmpty()) {
            mail.getBccRecipients().addAll(maRecipientsBcc);
        }
        mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
        mail.send();
        System.out.println("Mail sent!");
    }
    
    private class Row {
        
        public String Concept;
        public int CurrencyId;
        public String Currency;
        public int UnitId;
        public String Unit;
        public double Price;
        public double TotGross;
        public double TotReturns;
        public double TotDiscounts;
        public double QtyGross;
        public double QtyReturns;
        
        public Row(final String concept, final int currencyId, final String currency, final int unitId, final String unit, final double price) {
            Concept = concept;
            CurrencyId = currencyId;
            Currency = currency;
            UnitId = unitId;
            Unit = unit;
            Price = price;
            TotGross = 0;
            TotReturns = 0;
            TotDiscounts = 0;
            QtyGross = 0;
            QtyReturns = 0;
        }
        
        public void add(final double totGross, final double totReturns, final double totDiscounts, final double qtyGross, final double qtyReturns) {
            TotGross = SLibUtils.roundAmount(TotGross + totGross);
            TotReturns = SLibUtils.roundAmount(TotReturns + totReturns);
            TotDiscounts = SLibUtils.roundAmount(TotDiscounts + totDiscounts);
            QtyGross += qtyGross;
            QtyReturns += qtyReturns;
        }
        
        public double getTotNet() {
            // returns and discounts are negative values:
            return SLibUtils.roundAmount(TotGross + TotReturns + TotDiscounts);
        }
        
        public double getQtyNet() {
            // returns are negative values:
            return QtyGross + QtyReturns;
        }
        
        public double getAvgPrice() {
            return getQtyNet() == 0d ? 0d : getTotNet() / getQtyNet();
        }
    }
    
    private class DocClass {
        public int DocClassId;
        public int DocsNew;
        public int DocsEmited;
        public int DocsAnnulled;
        
        public DocClass(final int docClassId) {
            DocClassId = docClassId;
            DocsNew = 0;
            DocsEmited = 0;
            DocsAnnulled = 0;
        }
    }
}

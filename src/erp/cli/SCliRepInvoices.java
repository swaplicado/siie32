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
    private static final String DPS_CATEGORY_SAL = "S";
    private static final String DPS_CATEGORY_PUR = "P";
    private static final String PERIOD_TODAY = "TODAY";
    private static final String PERIOD_YESTERDAY = "YESTERDAY";
    private static final int ROW_GRP_HDR = 1;
    private static final int ROW_GRP_FTR = 2;
    private static final int ROW_ROW = 3;
    private static final int ROW_TOT_CUR = 4;
    private static final int ROW_TOT_LOC_CUR = 5;
    
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
     * @param args Expected arguments:
     * index 0: mailer option ('I');
     * index 1: category: sales ('S') or purchases ('P');
     * index 2: period start date in format "yyyy-MM-dd";
     * index 3: period end date in format "yyyy-MM-dd";
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
            Date yesterday = null;
            
            switch (msArgPeriodStart) {
                case PERIOD_TODAY:
                    today = SLibTimeUtils.convertToDateOnly(new Date());
                    mtPeriodStart = today;
                    break;
                case PERIOD_YESTERDAY:
                    yesterday = SLibTimeUtils.addDate(new Date(), 0, 0, -1);
                    mtPeriodStart = yesterday;
                    break;
                default:
                    mtPeriodStart = SLibUtils.DbmsDateFormatDate.parse(msArgPeriodStart);
            }
            
            switch (msArgPeriodEnd) {
                case PERIOD_TODAY:
                    if (today == null) {
                        today = SLibTimeUtils.convertToDateOnly(new Date());
                    }
                    mtPeriodEnd = today;
                    break;
                case PERIOD_YESTERDAY:
                    if (yesterday == null) {
                        yesterday = SLibTimeUtils.addDate(new Date(), 0, 0, -1);
                    }
                    mtPeriodEnd = yesterday;
                    break;
                default:
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
                + "bp, bp_comm, id_bp, cur_key, id_cur, "
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
                + "bp.bp, bp.bp_comm, bp.id_bp, c.cur_key, c.id_cur, de.fid_tp_dps_adj, "
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
                + "d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND "
                + "d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' "
                + "GROUP BY "
                + "i.item_key, i.item, i.id_item, u.symbol, u.id_unit, bp.bp, bp.bp_comm, bp.id_bp, c.cur_key, c.id_cur, de.fid_tp_dps_adj "
                + "UNION ALL "
                + "SELECT "
                + "i.item_key, i.item, i.id_item, u.symbol AS unit_symbol, u.id_unit, "
                + "bp.bp, bp.bp_comm, bp.id_bp, c.cur_key, c.id_cur, de.fid_tp_dps_adj, "
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
                + "d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND "
                + "d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' "
                + "GROUP BY "
                + "i.item_key, i.item, i.id_item, u.symbol, u.id_unit, bp.bp, bp.bp_comm, bp.id_bp, c.cur_key, c.id_cur, de.fid_tp_dps_adj "
                + "ORDER BY "
                + "item_key, item, id_item, unit_symbol, id_unit, bp, bp_comm, id_bp, cur_key, id_cur, fid_tp_dps_adj "
                + ") AS t "
                + "GROUP BY "
                + "item_key, item, id_item, unit_symbol, id_unit, bp, bp_comm, id_bp, cur_key, id_cur "
                + "ORDER BY "
                + "item_key, item, id_item, unit_symbol, id_unit, bp, bp_comm, id_bp, cur_key, id_cur;";
        
        return sql;
    }
    
    private String composeMailSubject() throws Exception {
        String subject = "[" + SClient.APP_NAME + "] " + msDpsCategoryName + " " + SLibUtils.DateFormatDate.format(mtPeriodStart);
        
        if (!mtPeriodStart.equals(mtPeriodEnd)) {
            subject += "-" + SLibUtils.DateFormatDate.format(mtPeriodEnd);
        }
        
        return SMailUtils.encodeSubjectUtf8(subject);
    }
    
    private String formatAmount(final double amount) {
        return amount == 0d ? "-" : SLibUtils.getDecimalFormatAmount().format(amount);
    }
    
    private String formatAmountUnit(final double amountUnit) {
        return amountUnit == 0d ? "-" : SLibUtils.getDecimalFormatAmountUnitary().format(amountUnit);
    }
    
    private String formatQuantity(final double quantity) {
        return quantity == 0d ? "-" : SLibUtils.getDecimalFormatQuantity().format(quantity);
    }
    
    private String composeMailBodyHtmlHead() throws Exception {
        // start of head
        String head = "<head>\n";
        
        head += "<style>\n";
        
        head += ".right {"
                + "text-align: right;"
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
                + "td.grpftrright {"
                + " padding-top: 5px;"
                + " padding-bottom: 5px;"
                + " font-weight: bold;"
                + " text-align: right;"
                + "} "
                + "td.totcur {"
                + " padding-top: 10px;"
                + " font-weight: bold;"
                + "} "
                + "td.totcurright {"
                + " padding-top: 10px;"
                + " font-weight: bold;"
                + " text-align: right;"
                + "} "
                + "td.totloccur {"
                + " padding-top: 10px;"
                + " font-weight: bold;"
                + "} "
                + "td.totloccurright {"
                + " padding-top: 10px;"
                + " font-weight: bold;"
                + " text-align: right;"
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
        
        html += "<td class=\"" + ccsClass + "right\">" + formatAmount(row.TotGross) + "</td>";
        html += "<td class=\"" + ccsClass + "right\">" + formatAmount(row.TotReturns) + "</td>";
        html += "<td class=\"" + ccsClass + "right\">" + formatAmount(row.TotDiscounts) + "</td>";
        html += "<td class=\"" + ccsClass + "right\">" + formatAmount(row.getTotNet()) + "</td>";
        html += "<td class=\"" + ccsClass + "\">" + SLibUtils.textToHtml(row.Currency) + "</td>";
        html += "<td class=\"" + ccsClass + "right\">" + formatQuantity(row.QtyGross) + "</td>";
        html += "<td class=\"" + ccsClass + "right\">" + formatQuantity(row.QtyReturns) + "</td>";
        html += "<td class=\"" + ccsClass + "right\">" + formatQuantity(row.getQtyNet()) + "</td>";
        html += "<td class=\"" + ccsClass + "\">" + SLibUtils.textToHtml(row.Unit) + "</td>";
        html += "<td class=\"" + ccsClass + "right\">" + (row.UnitId == 0 ? "-" : formatAmountUnit(row.getAvgPrice())) + "</td>";
        
        html += "</tr>\n";
        
        return html;
    }
    
    private String composeHtmlDocsInfo() throws Exception {
        String html = "";
        
        /*
        Comprobante   Cantidad Vigentes          Cancelados
                               C/timbre S/timbre C/timbre S/timbre
        Facturas          9        9        9        9        9
        Notas crédito     9        9        9        9        9
        */
        
        return html;
    }
    
    private String composeMailBodyHtmlBody() throws Exception {
        // start of body
        String body = "<body>\n";
        
        // title
        
        body += "<h1>" + SLibUtils.textToHtml(msCompanyName) + "</h1>\n";
        body += "<h2>" + msDpsCategoryName + " " + SLibUtils.DateFormatDate.format(mtPeriodStart);
        if (!mtPeriodStart.equals(mtPeriodEnd)) {
            body += "-" + SLibUtils.DateFormatDate.format(mtPeriodEnd);
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
        body += "<th>Precio prom. $</th>";
        body += "</tr>\n";
        
        // rows of table
        
        int curItemId = 0;
        String curItemText = "";
        int curCurrencyId = 0;
        int curUnitId = 0;
        HashMap<Integer, String> mapCurrencies = new HashMap<>();
        HashMap<Integer, String> mapUnits = new HashMap<>();
        HashMap<Integer, Row> mapRowSubtotals = new HashMap<>(); // key is currency ID
        HashMap<Integer, Row> mapRowTotals = new HashMap<>(); // key is currency ID
        HashSet<Integer> setItemUnits = new HashSet<>(); // element is unit ID
        Row rowLocalTotal = new Row("TOTAL MONEDA LOCAL", mnLocalCurrencyId, msLocalCurrencyCode, 0, "");
        
        mapCurrencies.put(mnLocalCurrencyId, msLocalCurrencyCode);
        
        String sql = composeSql();
        
        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
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
                
                Row row = new Row(resultSet.getString("bp"), curCurrencyId, mapCurrencies.get(curCurrencyId), curUnitId, mapUnits.get(curUnitId));
                row.TotGross = resultSet.getDouble("_stot_cur_r_inv");
                row.TotReturns = resultSet.getDouble("_stot_cur_r_cn_ret");
                row.TotDiscounts = resultSet.getDouble("_stot_cur_r_cn_disc");
                row.QtyGross = resultSet.getDouble("_qty_inv");
                row.QtyReturns = resultSet.getDouble("_qty_cn_ret");
                
                body += composeHtmlTableRow(row, ROW_ROW);
                
                Row rowSubtotal = mapRowSubtotals.get(curCurrencyId);
                if (rowSubtotal == null) {
                    rowSubtotal = new Row(curItemText + " " + mapCurrencies.get(curCurrencyId), curCurrencyId, mapCurrencies.get(curCurrencyId), 0, "");
                    mapRowSubtotals.put(curCurrencyId, rowSubtotal);
                }
                rowSubtotal.add(resultSet.getDouble("_stot_cur_r_inv"), resultSet.getDouble("_stot_cur_r_cn_ret"), resultSet.getDouble("_stot_cur_r_cn_disc"), 
                        resultSet.getDouble("_qty_inv"), resultSet.getDouble("_qty_cn_ret"));
                
                Row rowTotal = mapRowTotals.get(curCurrencyId);
                if (rowTotal == null) {
                    rowTotal = new Row("TOTAL " + mapCurrencies.get(curCurrencyId), curCurrencyId, mapCurrencies.get(curCurrencyId), 0, "");
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
            
            // render totals
            
            body += "<tr><td class=\"grphdr\" colspan=\"12\">" + SLibUtils.textToHtml("TOTALES") + "</td></tr>\n";
            
            for (Row row : mapRowTotals.values()) {
                body += composeHtmlTableRow(row, ROW_TOT_CUR);
            }
            
            if (mapRowTotals.size() > 1 || !mapRowTotals.containsKey(mnLocalCurrencyId)) {
                body += composeHtmlTableRow(rowLocalTotal, ROW_TOT_LOC_CUR);
            }
        }

        // end of table
        body += "</table>";
        
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
        public double TotGross;
        public double TotReturns;
        public double TotDiscounts;
        public double QtyGross;
        public double QtyReturns;
        
        public Row(final String concept, final int currencyId, final String currency, final int unitId, final String unit) {
            Concept = concept;
            CurrencyId = currencyId;
            Currency = currency;
            UnitId = unitId;
            Unit = unit;
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
            return SLibUtils.roundAmount(TotGross - TotReturns - TotDiscounts);
        }
        
        public double getQtyNet() {
            return QtyGross - QtyReturns;
        }
        
        public double getAvgPrice() {
            return getQtyNet() == 0d ? 0d : getTotNet() / getQtyNet();
        }
    }
}

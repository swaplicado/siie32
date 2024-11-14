/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cli;

import erp.SClient;
import erp.SParamsApp;
import static erp.cli.SCliMailer.ARG_IDX_COMPANY_ID;
import erp.data.SDataConstantsSys;
import erp.mod.SModSysConsts;
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
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import sa.lib.mail.SMailUtils;

/**
 *
 * @author Sergio Flores
 */
public class SCliRepInvoicesMultiCompanies {
    
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
    private static final String TXT_TOT_LOC_CUR = "TOTAL MONEDA LOCAL";
    private static final String TXT_MONTH = "Acumulado mensual";
    private static final String TXT_YEAR = "Acumulado anual";
    private static final int ROW_GRP_HDR = 11;
    private static final int ROW_GRP_FTR = 12;
    private static final int ROW_ROW_REP = 21; // row for report section
    private static final int ROW_ROW_ACC = 22; // for for accumulated section (monthly or yearly)
    private static final int ROW_SUB_TOT = 31;
    private static final int ROW_TOT = 32;
    /** Query for detail: by item, unit, busienss partner, currency & price. */
    private static final int QUERY_DETAIL = 1;
    /** Query for summary: by currency, item & unit. */
    private static final int QUERY_SUMMARY = 2;
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
    private SDbDatabase moErpDatabase;
    private SDbDatabase moCompanyDatabase;
    
    private String msHtml;
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
    
    private String msCompanyCode;
    private String msCompanyName;
    private int mnLocalCurrencyId;
    private String msLocalCurrencyCode;
    private String msLocalCurrencyName;
    private Statement miStatement;
    
    private SMailSender moMailSender;
    
    /**
     * Create report of summary invoices of items and business partners.
     * When period start and end dates are from the same month, a monthly accumulated summary is appended.
     * @param dbErp
     * @param args Expected arguments:
     * index 0: mailer option ('I');
     * index 1: category: sales ('S') or purchases ('P');
     * index 2: period start date in format "yyyy-MM-dd" or TODAY[{+|-}n];
     * index 3: period end date in format "yyyy-MM-dd" or TODAY[{+|-}n];
     * index 4: TO recepients separated by semicolon;
     * index 5: (optional) BCC recepients separated by semicolon.
     * @throws java.lang.Exception
     */
    public SCliRepInvoicesMultiCompanies(final SDbDatabase dbErp, final String[] args) throws Exception {
        maArgs = args;
        moErpDatabase = dbErp;
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
            
            moMailSender = null;
            
            // connect to company database:
            SParamsApp paramsApp = new SParamsApp();
            
            if (!paramsApp.read()) {
                throw new Exception(erp.SClient.ERR_PARAMS_APP_READING);
            }
            
            String companyDatabase = "";
            String companiesCodes[] = maArgs[ARG_IDX_COMPANY_ID].split(";");
            
            msHtml = "<html>\n";
            msHtml += composeHtmlMailHead();
            msHtml += "<body>\n";
            for (String companyCode : companiesCodes) {
            
                String sql = "SELECT bd, co_key, co "
                        + "FROM erp.cfgu_co "
                        + "WHERE id_co = " + companyCode + ";";
                try (ResultSet resultSet = moErpDatabase.getConnection().createStatement().executeQuery(sql)) {
                    if (resultSet.next()) {
                        companyDatabase = resultSet.getString("bd");
                        msCompanyCode = resultSet.getString("co_key");
                        msCompanyName = resultSet.getString("co");
                    }
                }

                moCompanyDatabase = new SDbDatabase(SDbConsts.DBMS_MYSQL);
                int result = moCompanyDatabase.connect(paramsApp.getDatabaseHostClt(), paramsApp.getDatabasePortClt(), 
                        companyDatabase, paramsApp.getDatabaseUser(), paramsApp.getDatabasePswd());

                if (result != SDbConsts.CONNECTION_OK) {
                    throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION);
                }
                miStatement = moCompanyDatabase.getConnection().createStatement();

                // local currency info

                sql = "SELECT cur.id_cur, cur.cur, cur.cur_key "
                        + "FROM erp.cfg_param_erp AS cfg "
                        + "INNER JOIN erp.cfgu_cur AS cur ON cfg.fid_cur = cur.id_cur;";

                try (ResultSet resultSet = miStatement.executeQuery(sql)) {
                    if (resultSet.next()) {
                        mnLocalCurrencyId = resultSet.getInt("id_cur");
                        msLocalCurrencyCode = resultSet.getString("cur_key");
                        msLocalCurrencyName = resultSet.getString("cur");
                    }
                }
                
                msHtml += composeHtmlMailBody();
                msHtml += "<hr>\n";
                
                if (moMailSender == null) {
                    sql = "SELECT * FROM cfg_mms WHERE fk_tp_mms = " + SModSysConsts.CFGS_TP_MMS_MAIL_REPS;
                    ResultSet resultSet = moCompanyDatabase.getConnection().createStatement().executeQuery(sql);

                    if (resultSet.next()) {
                        moMailSender = new SMailSender(resultSet.getString("host"), resultSet.getString("port"), resultSet.getString("prot"), 
                                resultSet.getBoolean("b_tls"), resultSet.getBoolean("b_auth"), resultSet.getString("usr"), resultSet.getString("usr_pswd"), resultSet.getString("arb_email"));
                    }
                }
                
                moCompanyDatabase.disconnect();
            }
            msHtml += STrnUtilities.composeMailFooter("warning");
            msHtml += "</body>\n";
            msHtml += "</html>";
        }
    }
    
    /**
     * Compose SQL sentence for report.
     * @param start Period start.
     * @param end Period end.
     * @param queryLevel Query level: detail (QUERY_DETAIL; by item, unit, busienss partner, currency & price); or summary (QUERY_SUMMARY; by currency, item & unit).
     * @return 
     */
    private String composeSql(final Date start, final Date end, final int queryLevel) throws Exception {
        String orderBy = "";
        switch (queryLevel) {
            case QUERY_DETAIL: // by item, unit, busienss partner, currency & price
                orderBy = "item_key, item, id_item, _unit_symbol, id_unit, "
                        + "bp, bp_comm, id_bp, "
                        + "id_cur, cur, cur_key, " // currency by ID, name and code
                        + "_price";
                break;
            case QUERY_SUMMARY: // by currency, item & unit
                orderBy = "id_cur, cur, cur_key, " // currency by ID, name and code
                        + "item_key, item, id_item, _unit_symbol, id_unit";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        String sql = "SELECT "
                + "item_key, item, id_item, _unit_symbol, id_unit, "
                + "id_cur, cur, cur_key" + (queryLevel == QUERY_DETAIL ? ", bp, bp_comm, id_bp, _price" : "") + ", "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_NA + " THEN _qty ELSE 0.00 END) AS _qty_inv, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_NA + " THEN _stot_cur_r ELSE 0.00 END) AS _stot_cur_r_inv, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_NA + " THEN _stot_r ELSE 0.00 END) AS _stot_r_inv, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " THEN _qty ELSE 0.00 END) AS _qty_cn_ret, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " THEN _stot_cur_r ELSE 0.00 END) AS _stot_cur_r_cn_ret, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " THEN _stot_r ELSE 0.00 END) AS _stot_r_cn_ret, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC + " THEN _qty ELSE 0.00 END) AS _qty_cn_disc, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC + " THEN _stot_cur_r ELSE 0.00 END) AS _stot_cur_r_cn_disc, "
                + "SUM(CASE WHEN fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC + " THEN _stot_r ELSE 0.00 END) AS _stot_r_cn_disc, "
                + "SUM(_qty) AS _qty_net, "
                + "SUM(_stot_cur_r) AS _stot_cur_r_net, "
                + "SUM(_stot_r) AS _stot_r_net "
                + "FROM ("
                + "SELECT "
                + "i.item_key, i.item, i.id_item, u.symbol AS _unit_symbol, u.id_unit, "
                + "bp.bp, bp.bp_comm, bp.id_bp, c.id_cur, c.cur, c.cur_key, de.price_u_cur AS _price, de.fid_tp_dps_adj, "
                + "SUM(CASE WHEN d.fid_cl_dps = " + manDpsClassKeyCn[1] + " THEN (CASE WHEN de.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " THEN -de.qty ELSE 0.0 END) ELSE de.qty END) AS _qty, "
                + "SUM(CASE WHEN d.fid_cl_dps = " + manDpsClassKeyCn[1] + " THEN -de.stot_cur_r ELSE de.stot_cur_r END) AS _stot_cur_r, "
                + "SUM(CASE WHEN d.fid_cl_dps = " + manDpsClassKeyCn[1] + " THEN -de.stot_r ELSE de.stot_r END) AS _stot_r "
                + "FROM "
                + "trn_dps AS d "
                + "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc "
                + "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur "
                + "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp "
                + "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item "
                + "INNER JOIN erp.itmu_unit AS u ON de.fid_unit = u.id_unit "
                + "WHERE NOT d.b_del AND NOT de.b_del AND "
                + "d.fid_ct_dps = " + manDpsClassKeyInv[0] + " AND d.fid_cl_dps IN (" + manDpsClassKeyInv[1] + ", " + manDpsClassKeyCn[1] + ") AND "
                + "d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(start) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(end) + "' AND "
                + "d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND de.fid_tp_dps_ety <> " + SDataConstantsSys.TRNS_TP_DPS_ETY_VIRT + " "
                + "GROUP BY "
                + "i.item_key, i.item, i.id_item, u.symbol, u.id_unit, "
                + "bp.bp, bp.bp_comm, bp.id_bp, c.id_cur, c.cur, c.cur_key, de.price_u_cur, de.fid_tp_dps_adj "
                + "ORDER BY "
                + "i.item_key, i.item, i.id_item, u.symbol, u.id_unit, "
                + "bp.bp, bp.bp_comm, bp.id_bp, c.id_cur, c.cur, c.cur_key, de.price_u_cur, de.fid_tp_dps_adj "
                + ") AS t "
                + "GROUP BY "
                + "item_key, item, id_item, _unit_symbol, id_unit, "
                + "id_cur, cur, cur_key" + (queryLevel == QUERY_DETAIL ? ", bp, bp_comm, id_bp, _price" : "") + " "
                + "ORDER BY "
                + orderBy + ";";
        
        return sql;
    }
    
    private String composeSalesBackorderSql() {
        return "SELECT de.id_year, de.id_doc, de.id_ety, d.dt, d.dt_doc_delivery_n, d.dt_doc_lapsing_n, d.num_ref, d.b_link, d.ts_link, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, d.fid_cob, d.fid_bpb, d.fid_bp_r, d.fid_usr_link, " +
                "dt.code AS f_dt_code, dn.code AS f_dn_code, cob.code AS f_cob_code, bb.bpb, b.bp, bc.bp_key, c.cur_key, ul.usr, de.fid_item, " +
                "de.fid_unit, de.fid_orig_unit, de.surplus_per, de.qty AS f_qty, " +
                "de.orig_qty AS f_orig_qty, CASE WHEN de.qty = 0 THEN 0 ELSE de.stot_cur_r / de.qty END AS f_price_u, CASE WHEN de.orig_qty = 0 THEN 0 ELSE de.stot_cur_r / de.orig_qty END AS f_orig_price_u, " +
                "de.sales_price_u_cur, de.sales_freight_u_cur, i.item_key, i.item, ig.igen, u.symbol AS f_unit, uo.symbol AS f_orig_unit, " +
                "COALESCE((SELECT SUM(ds.qty) FROM trn_dps_dps_supply AS ds, trn_dps_ety AS xde, trn_dps AS xd " +
                "WHERE ds.id_src_year = de.id_year AND ds.id_src_doc = de.id_doc AND ds.id_src_ety = de.id_ety AND ds.id_des_year = xde.id_year " +
                "AND ds.id_des_doc = xde.id_doc AND ds.id_des_ety = xde.id_ety AND xde.id_year = xd.id_year AND xde.id_doc = xd.id_doc " +
                "AND xde.b_del = 0 AND xd.b_del = 0 AND xd.fid_st_dps = 2), 0) AS f_link_qty, " +
                "COALESCE((SELECT SUM(ds.orig_qty) FROM trn_dps_dps_supply AS ds, trn_dps_ety AS xde, trn_dps AS xd " +
                "WHERE ds.id_src_year = de.id_year AND ds.id_src_doc = de.id_doc AND ds.id_src_ety = de.id_ety AND ds.id_des_year = xde.id_year " +
                "AND ds.id_des_doc = xde.id_doc AND ds.id_des_ety = xde.id_ety AND xde.id_year = xd.id_year AND xde.id_doc = xd.id_doc AND xde.b_del = 0 " +
                "AND xd.b_del = 0 AND xd.fid_st_dps = 2), 0) AS f_link_orig_qty, u_new.usr AS _usr_new " +
                "FROM trn_dps AS d " +
                "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc AND d.b_del = 0 AND de.b_del = 0 AND d.fid_st_dps = 2 " +
                "AND d.fid_ct_dps = 2 AND d.fid_cl_dps = 2 AND d.fid_tp_dps = 1 AND d.fid_cob = 2890 " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                "INNER JOIN erp.trnu_dps_nat AS dn ON d.fid_dps_nat = dn.id_dps_nat " +
                "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
                "INNER JOIN erp.bpsu_bpb AS bb ON d.fid_bpb = bb.id_bpb " +
                "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bc ON d.fid_bp_r = bc.id_bp AND bc.id_ct_bp = 3 " +
                "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN erp.usru_usr AS ul ON d.fid_usr_link = ul.id_usr " +
                "INNER JOIN erp.usru_usr AS u_new ON d.fid_usr_new = u_new.id_usr " + 
                "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item " +
                "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " +
                "INNER JOIN erp.itmu_unit AS u ON de.fid_unit = u.id_unit " +
                "INNER JOIN erp.itmu_unit AS uo ON de.fid_orig_unit = uo.id_unit " +
                "GROUP BY de.id_year, de.id_doc, de.id_ety, d.dt, d.dt_doc_delivery_n, d.dt_doc_lapsing_n, d.num_ref, d.b_link, d.ts_link, d.num_ser, d.num, " +
                "d.fid_cob, d.fid_bpb, d.fid_bp_r, d.fid_usr_link, dt.code, dn.code, cob.code, bb.bpb, b.bp, bc.bp_key, c.cur_key, ul.usr, de.fid_item, " +
                "de.fid_unit, de.fid_orig_unit, de.surplus_per, de.qty, de.orig_qty, de.stot_cur_r, i.item_key, i.item, ig.igen, u.symbol, uo.symbol " +
                "HAVING f_link_orig_qty < de.orig_qty AND d.b_link = 0 ORDER BY dt.code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt, " +
                "de.id_year, de.id_doc, b.bp, bc.bp_key, d.fid_bp_r, bb.bpb, d.fid_bpb, i.item_key, i.item, de.fid_item, uo.symbol, de.fid_orig_unit";
    }
    
    private String composeMailSubject() throws Exception {
        String subject = "[" + SClient.APP_NAME + "]: " + msDpsCategoryName + " " + SLibUtils.DateFormatDate.format(mtPeriodStart);
        
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
    
    /**
     * Compose text for given period. When start and end are the same, only one date appears. Otherwise both dates appear spaced by a hyphen.
     * @param start
     * @param end
     * @return 
     */
    private String composeTextPeriod(final Date start, final Date end) {
        String period = SLibUtils.DateFormatDate.format(start);
        
        if (!start.equals(end)) {
            period += " - " + SLibUtils.DateFormatDate.format(end);
        }
        
        return period;
    }
    
    private String composeHtmlMailHead() throws Exception {
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
                + " background-color: turquoise"
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
                + " padding-bottom: 10px;"
                + " font-weight: bold;"
                + " text-align: right;"
                + " white-space: nowrap;"
                + "} "
                + "td.subtot {"
                + " font-weight: bold;"
                + "} "
                + "td.subtotnumber {"
                + " font-weight: bold;"
                + " text-align: right;"
                + " white-space: nowrap;"
                + "} "
                + "td.tot {"
                + " font-size: 1.0em;"
                + " padding-top: 10px;"
                + " font-weight: bold;"
                + "} "
                + "td.totnumber {"
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
            case ROW_GRP_HDR:
                ccsClass = "grphdr";
                break;
            case ROW_GRP_FTR:
                ccsClass = "grpftr";
                break;
            case ROW_SUB_TOT:
                ccsClass = "subtot";
                break;
            case ROW_TOT:
                ccsClass = "tot";
                break;
            default:
        }
        
        html += "<tr>";
        
        if (rowLevel == ROW_ROW_REP || rowLevel == ROW_ROW_ACC) {
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
        html += "<td class=\"" + ccsClass + "number\">" + (row.UnitId == 0 ? ZERO : formatAmountUnit(rowLevel == ROW_ROW_REP ? row.Price : row.getAvgPrice())) + "</td>"; // specific price available only in rows of report section
        
        html += "</tr>\n";
        
        return html;
    }
    
    private String composeHtmlDocsInfoReport(final Date start, final Date end) throws Exception {
        DocClass docClassInv = new DocClass(DOC_CLASS_INV_ID);
        DocClass docClassCn = new DocClass(DOC_CLASS_CN_ID);
        
        String sql = "SELECT "
                + "d.fid_cl_dps, d.fid_st_dps, dst.st_dps, c.fid_st_xml, cst.st_dps, COUNT(*) AS _count "
                + "FROM trn_dps AS d "
                + "INNER JOIN trn_cfd AS c ON c.fid_dps_year_n = d.id_year AND c.fid_dps_doc_n = d.id_doc "
                + "INNER JOIN erp.trns_st_dps AS dst ON dst.id_st_dps = d.fid_st_dps "
                + "INNER JOIN erp.trns_st_dps AS cst ON cst.id_st_dps = c.fid_st_xml "
                + "WHERE NOT d.b_del AND d.fid_ct_dps = " + manDpsClassKeyInv[0] + " AND "
                + "d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(start) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(end) + "' "
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
        
        html += "<h3>Comprobantes " + composeTextPeriod(start, end) + "</h3>\n";
        
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
        
        html += "<br>\n";
        
        return html;
    }
    
    private String composeHtmlSalesBackorder() throws Exception {
        String sql = composeSalesBackorderSql();
        String html = "<br>\n";
        
        html += "<h2>Backorder de pedidos de ventas " + composeTextPeriod(mtPeriodStart, mtPeriodEnd) + "</h2>\n";
        
        html += "<table>\n";
        html += "<tr>"
                + "<th>Pedido</th>"
                + "<th>Fecha</th>"
                + "<th>Cliente</th>"
                + "<th>Concepto</th>"
                + "<th>Cant. pedida</th>"
                + "<th>Cant. procesada</th>"
                + "<th>Cant. pendiente</th>"
                + "<th>Unidad</th>"
                + "<th>Usuario</th>"
                + "</tr>\n";
        
        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                double origQty = resultSet.getDouble("f_orig_qty");
                double linkQty = resultSet.getDouble("f_link_orig_qty");
                double toLinkQty = origQty - linkQty;
                html += "<tr>"
                        + "<td>" + SLibUtils.textToHtml(resultSet.getString("f_num")) + "</td>"
                        + "<td class=\"center\">" + SLibUtils.DateFormatDate.format(resultSet.getDate("d.dt")) + "</td>"
                        + "<td>" + SLibUtils.textToHtml(resultSet.getString("b.bp")) + "</td>"
                        + "<td>" + SLibUtils.textToHtml(resultSet.getString("i.item_key") + " - " + resultSet.getString("i.item")) + "</td>"
                        + "<td class=\"number\">" + formatQuantity(origQty) + "</td>"
                        + "<td class=\"number\">" + formatQuantity(linkQty) + "</td>"
                        + "<td class=\"number\">" + formatQuantity(toLinkQty) + "</td>"
                        + "<td>" + SLibUtils.textToHtml(resultSet.getString("f_orig_unit")) + "</td>"
                        + "<td>" + SLibUtils.textToHtml(resultSet.getString("_usr_new")) + "</td>"
                        + "</tr>\n"; 
            }
        }
        html += "</table>\n";
        html += "<br>\n";
        
        return html;
    }
    
    private String composeHtmlMailBodyReport() throws Exception {
        String html = "";
        
        // subtitle
        
        html += "<h2>" + msDpsCategoryName + " " + composeTextPeriod(mtPeriodStart, mtPeriodEnd) + "</h2>\n";
        
        // table
        
        // header of table
        
        html += "<table>\n";
        html += "<tr>";
        html += "<th colspan=\"2\">Asociado negocios</th>";
        html += "<th>Total $</th>";
        html += "<th>Devs. $</th>";
        html += "<th>Descs. $</th>";
        html += "<th>Total neto $</th>";
        html += "<th>Moneda</th>";
        html += "<th>Cant.</th>";
        html += "<th>Cant. devs.</th>";
        html += "<th>Cant. neta</th>";
        html += "<th>Unidad</th>";
        html += "<th>Precio $</th>";
        html += "</tr>\n";
        
        // rows of table
        
        int curItemId = 0;
        int curUnitId = 0;
        int curCurrencyId = 0;
        String curItemText = "";
        boolean dataFound = false;
        HashMap<Integer, String> mapCurrencyCodes = new HashMap<>(); // key: ID; value: code
        HashMap<Integer, String> mapCurrencyNames = new HashMap<>(); // key: ID; value: name
        HashMap<Integer, String> mapUnitSymbols = new HashMap<>(); // key: ID; value: symbol
        HashMap<Integer, Row> mapCurItemSubtotalRows = new HashMap<>(); // key is currency ID
        HashMap<Integer, Row> mapReportTotalRows = new HashMap<>(); // key is currency ID
        HashSet<Integer> setCurItemUnits = new HashSet<>(); // element is unit ID
        HashSet<Integer> setReportUnits = new HashSet<>(); // element is unit ID
        Row rowLocalCurrencyTotal = new Row(TXT_TOT_LOC_CUR, mnLocalCurrencyId, msLocalCurrencyCode, 0, "", 0);
        
        mapCurrencyCodes.put(mnLocalCurrencyId, msLocalCurrencyCode);
        mapCurrencyNames.put(mnLocalCurrencyId, msLocalCurrencyName);
        
        // BODY
        
        // query sorted by item for requested period (may be a single day):
        String sql = composeSql(mtPeriodStart, mtPeriodEnd, QUERY_DETAIL);
        
        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                dataFound = true;
                
                if (curItemId != resultSet.getInt("id_item")) { // another item?
                    if (curItemId != 0) {
                        // render subtotals of last item
                        
                        for (Row row : mapCurItemSubtotalRows.values()) {
                            if (setCurItemUnits.size() == 1) { // check if there is only one unit for current item
                                row.UnitId = (Integer) setCurItemUnits.toArray()[0];
                                row.Unit = mapUnitSymbols.get(row.UnitId);
                            }
                            
                            html += composeHtmlTableRow(row, ROW_GRP_FTR);
                        }
                    }
                    
                    curItemId = resultSet.getInt("id_item");
                    curItemText = resultSet.getString("item_key") + " - " + resultSet.getString("item");
                    mapCurItemSubtotalRows.clear();
                    setCurItemUnits.clear();
                    
                    html += "<tr><td class=\"grphdr\" colspan=\"12\">" + SLibUtils.textToHtml(curItemText) + "</td></tr>\n";
                }
                
                curCurrencyId = resultSet.getInt("id_cur");
                if (mapCurrencyCodes.get(curCurrencyId) == null) {
                    mapCurrencyCodes.put(curCurrencyId, resultSet.getString("cur_key")); // preserve currency codes
                }
                if (mapCurrencyNames.get(curCurrencyId) == null) {
                    mapCurrencyNames.put(curCurrencyId, resultSet.getString("cur")); // preserve currency names
                }
                
                curUnitId = resultSet.getInt("id_unit");
                if (mapUnitSymbols.get(curUnitId) == null) {
                    mapUnitSymbols.put(curUnitId, resultSet.getString("_unit_symbol")); // preserve unit codes
                }
                
                setCurItemUnits.add(curUnitId); // update set of units for current item
                setReportUnits.add(curUnitId); // update set of units for this report
                
                Row row = new Row(resultSet.getString("bp"), curCurrencyId, mapCurrencyCodes.get(curCurrencyId), curUnitId, mapUnitSymbols.get(curUnitId), resultSet.getDouble("_price"));
                row.TotGross = resultSet.getDouble("_stot_cur_r_inv");
                row.TotReturns = resultSet.getDouble("_stot_cur_r_cn_ret");
                row.TotDiscounts = resultSet.getDouble("_stot_cur_r_cn_disc");
                row.QtyGross = resultSet.getDouble("_qty_inv");
                row.QtyReturns = resultSet.getDouble("_qty_cn_ret");
                
                html += composeHtmlTableRow(row, ROW_ROW_REP); // append current row
                
                // update current item subtotal
                Row rowCurItemSubtotal = mapCurItemSubtotalRows.get(curCurrencyId);
                if (rowCurItemSubtotal == null) {
                    rowCurItemSubtotal = new Row(curItemText + " " + mapCurrencyCodes.get(curCurrencyId), curCurrencyId, mapCurrencyCodes.get(curCurrencyId), 0, "", 0);
                    mapCurItemSubtotalRows.put(curCurrencyId, rowCurItemSubtotal);
                }
                rowCurItemSubtotal.add(resultSet.getDouble("_stot_cur_r_inv"), resultSet.getDouble("_stot_cur_r_cn_ret"), resultSet.getDouble("_stot_cur_r_cn_disc"), 
                        resultSet.getDouble("_qty_inv"), resultSet.getDouble("_qty_cn_ret"));
                
                // update currency total
                Row rowReportTotal = mapReportTotalRows.get(curCurrencyId);
                if (rowReportTotal == null) {
                    rowReportTotal = new Row(mapCurrencyNames.get(curCurrencyId), curCurrencyId, mapCurrencyCodes.get(curCurrencyId), 0, "", 0);
                    mapReportTotalRows.put(curCurrencyId, rowReportTotal);
                }
                rowReportTotal.add(resultSet.getDouble("_stot_cur_r_inv"), resultSet.getDouble("_stot_cur_r_cn_ret"), resultSet.getDouble("_stot_cur_r_cn_disc"), 
                        resultSet.getDouble("_qty_inv"), resultSet.getDouble("_qty_cn_ret"));
                
                // update local currency
                rowLocalCurrencyTotal.add(resultSet.getDouble("_stot_r_inv"), resultSet.getDouble("_stot_r_cn_ret"), resultSet.getDouble("_stot_r_cn_disc"), 
                        resultSet.getDouble("_qty_inv"), resultSet.getDouble("_qty_cn_ret"));
            }
            
            if (dataFound) {
                // render subtotals of last item

                for (Row row : mapCurItemSubtotalRows.values()) {
                    if (setCurItemUnits.size() == 1) { // check if there is one single unit for current item
                        row.UnitId = (Integer) setCurItemUnits.toArray()[0];
                        row.Unit = mapUnitSymbols.get(row.UnitId);
                    }

                    html += composeHtmlTableRow(row, ROW_GRP_FTR);
                }

                // render totals

                html += "<tr><td class=\"grphdr\" colspan=\"12\">" + SLibUtils.textToHtml("TOTALES POR MONEDA") + "</td></tr>\n";

                for (Row row : mapReportTotalRows.values()) {
                    if (setReportUnits.size() == 1) { // check if there is one single unit for whole report
                        row.UnitId = (Integer) setReportUnits.toArray()[0];
                        row.Unit = mapUnitSymbols.get(row.UnitId);
                    }

                    html += composeHtmlTableRow(row, mapReportTotalRows.size() == 1 ? ROW_TOT : ROW_SUB_TOT);
                }

                if (mapReportTotalRows.size() > 1 || !mapReportTotalRows.containsKey(mnLocalCurrencyId)) {
                    html += composeHtmlTableRow(rowLocalCurrencyTotal, ROW_TOT);
                }
            }
        }

        // end of table
        html += "</table>";
        
        if (dataFound) {
            html += composeHtmlDocsInfoReport(mtPeriodStart, mtPeriodEnd);
        }
        else {
            html += "<p><strong>" + SLibUtils.textToHtml("¡No se encontró información para " +
                    (mtPeriodStart.equals(mtPeriodEnd) ? "la fecha solicitada" : "el período solicitado") + " " + composeTextPeriod(mtPeriodStart, mtPeriodEnd) + "!") +
                    "</strong></p>\n";
        }
        
        return html;
    }
    
    private String composeHtmlMailBodyAccum() throws Exception {
        String html = "";
        String subtitle = "";
        Date accumStart = null;
        Date accumEnd = null;
        int[] anStart = SLibTimeUtils.digestDate(mtPeriodStart);
        int[] anEnd = SLibTimeUtils.digestDate(mtPeriodEnd);
        
        // subtitle
        
        if (anStart[0] == anEnd[0]) { // same year?
            if (anStart[1] == anEnd[1]) { // same month?
                subtitle = TXT_MONTH + " ";
                accumStart = SLibTimeUtils.getBeginOfMonth(mtPeriodStart);
            }
            else {
                subtitle = TXT_YEAR + " ";
                accumStart = SLibTimeUtils.getBeginOfYear(mtPeriodStart);
            }
            accumEnd = mtPeriodEnd;
            
            subtitle += msDpsCategoryName.toLowerCase() + " " + composeTextPeriod(accumStart, accumEnd);
            
            html += "<h2>" + subtitle + "</h2>\n";

            // table

            // header of table

            html += "<table>\n";
            html += "<tr>";
            html += "<th colspan=\"2\">Concepto</th>";
            html += "<th>Total $</th>";
            html += "<th>Devs. $</th>";
            html += "<th>Descs. $</th>";
            html += "<th>Total neto $</th>";
            html += "<th>Moneda</th>";
            html += "<th>Cant.</th>";
            html += "<th>Cant. devs.</th>";
            html += "<th>Cant. neta</th>";
            html += "<th>Unidad</th>";
            html += "<th>Precio prom. $</th>";
            html += "</tr>\n";

            // rows of table

            int curUnitId = 0;
            int curCurrencyId = 0;
            String curCurrencyText = "";
            boolean dataFound = false;
            boolean localCurrencyFound = false;
            HashMap<Integer, String> mapCurrencyCodes = new HashMap<>(); // key: ID; value: code
            HashMap<Integer, String> mapCurrencyNames = new HashMap<>(); // key: ID; value: name
            HashMap<Integer, String> mapUnitSymbols = new HashMap<>(); // key: ID; value: symbol
            HashMap<String, Row> mapReportTotalRows = new HashMap<>(); // key is item ID + "-" + unit ID
            HashSet<Integer> setCurCurrencyUnits = new HashSet<>(); // element is unit ID
            HashSet<Integer> setReportUnits = new HashSet<>(); // element is unit ID
            Row rowCurCurrencySubtotal = null;
            Row rowLocalCurrencyTotal = new Row(TXT_TOT_LOC_CUR, mnLocalCurrencyId, msLocalCurrencyCode, 0, "", 0);

            mapCurrencyCodes.put(mnLocalCurrencyId, msLocalCurrencyCode);
            mapCurrencyNames.put(mnLocalCurrencyId, msLocalCurrencyName);

            // BODY

            // query sorted by currency for requested period (may be only one day):
            String sql = composeSql(accumStart, accumEnd, QUERY_SUMMARY);

            try (ResultSet resultSet = miStatement.executeQuery(sql)) {
                while (resultSet.next()) {
                    dataFound = true;

                    if (curCurrencyId != resultSet.getInt("id_cur")) { // another currency?
                        if (curCurrencyId != 0) {
                            // render subtotal of last currency

                            if (setCurCurrencyUnits.size() == 1) { // check if there is only one unit for current currency
                                rowCurCurrencySubtotal.UnitId = (Integer) setCurCurrencyUnits.toArray()[0];
                                rowCurCurrencySubtotal.Unit = mapUnitSymbols.get(rowCurCurrencySubtotal.UnitId);
                            }

                            html += composeHtmlTableRow(rowCurCurrencySubtotal, ROW_GRP_FTR);
                        }

                        curCurrencyId = resultSet.getInt("id_cur");
                        curCurrencyText = resultSet.getString("cur");
                        setCurCurrencyUnits.clear();
                        rowCurCurrencySubtotal = null;

                        html += "<tr><td class=\"grphdr\" colspan=\"12\">" + SLibUtils.textToHtml(curCurrencyText) + "</td></tr>\n";
                        
                        if (!localCurrencyFound && curCurrencyId == mnLocalCurrencyId) {
                            localCurrencyFound = true;
                        }
                    }

                    curCurrencyId = resultSet.getInt("id_cur");
                    if (mapCurrencyCodes.get(curCurrencyId) == null) {
                        mapCurrencyCodes.put(curCurrencyId, resultSet.getString("cur_key")); // preserve currency codes
                    }
                    if (mapCurrencyNames.get(curCurrencyId) == null) {
                        mapCurrencyNames.put(curCurrencyId, resultSet.getString("cur")); // preserve currency names
                    }

                    curUnitId = resultSet.getInt("id_unit");
                    if (mapUnitSymbols.get(curUnitId) == null) {
                        mapUnitSymbols.put(curUnitId, resultSet.getString("_unit_symbol")); // preserve unit codes
                    }

                    setCurCurrencyUnits.add(curUnitId); // update set of units for current currency
                    setReportUnits.add(curUnitId); // update set of units for this report

                    Row row = new Row(resultSet.getString("item_key") + " - " + resultSet.getString("item"), curCurrencyId, mapCurrencyCodes.get(curCurrencyId), curUnitId, mapUnitSymbols.get(curUnitId), 0);
                    row.TotGross = resultSet.getDouble("_stot_cur_r_inv");
                    row.TotReturns = resultSet.getDouble("_stot_cur_r_cn_ret");
                    row.TotDiscounts = resultSet.getDouble("_stot_cur_r_cn_disc");
                    row.QtyGross = resultSet.getDouble("_qty_inv");
                    row.QtyReturns = resultSet.getDouble("_qty_cn_ret");

                    html += composeHtmlTableRow(row, ROW_ROW_ACC); // append current row

                    // update current currency subtotal
                    
                    if (rowCurCurrencySubtotal == null) {
                        rowCurCurrencySubtotal = new Row(curCurrencyText, curCurrencyId, mapCurrencyCodes.get(curCurrencyId), 0, "", 0);
                    }
                    rowCurCurrencySubtotal.add(resultSet.getDouble("_stot_cur_r_inv"), resultSet.getDouble("_stot_cur_r_cn_ret"), resultSet.getDouble("_stot_cur_r_cn_disc"), 
                            resultSet.getDouble("_qty_inv"), resultSet.getDouble("_qty_cn_ret"));

                    // update item-unit total
                    Row rowReportTotal = mapReportTotalRows.get(resultSet.getInt("id_item") + "-" + curUnitId);
                    if (rowReportTotal == null) {
                        rowReportTotal = new Row(resultSet.getString("item_key") + " - " + resultSet.getString("item"), mnLocalCurrencyId, msLocalCurrencyCode, curUnitId, mapUnitSymbols.get(curUnitId), 0);
                        mapReportTotalRows.put(resultSet.getInt("id_item") + "-" + curUnitId, rowReportTotal);
                    }
                    rowReportTotal.add(resultSet.getDouble("_stot_r_inv"), resultSet.getDouble("_stot_r_cn_ret"), resultSet.getDouble("_stot_r_cn_disc"), 
                            resultSet.getDouble("_qty_inv"), resultSet.getDouble("_qty_cn_ret"));

                    // update local currency total
                    rowLocalCurrencyTotal.add(resultSet.getDouble("_stot_r_inv"), resultSet.getDouble("_stot_r_cn_ret"), resultSet.getDouble("_stot_r_cn_disc"), 
                            resultSet.getDouble("_qty_inv"), resultSet.getDouble("_qty_cn_ret"));
                }

                if (dataFound) {
                    // render subtotal of last item

                    if (setCurCurrencyUnits.size() == 1) { // check if there is one single unit for current currency
                        rowCurCurrencySubtotal.UnitId = (Integer) setCurCurrencyUnits.toArray()[0];
                        rowCurCurrencySubtotal.Unit = mapUnitSymbols.get(rowCurCurrencySubtotal.UnitId);
                    }

                    html += composeHtmlTableRow(rowCurCurrencySubtotal, ROW_GRP_FTR);
                    // render totals

                    html += "<tr><td class=\"grphdr\" colspan=\"12\">" + SLibUtils.textToHtml("TOTALES POR CONCEPTO") + "</td></tr>\n";

                    for (Row row : mapReportTotalRows.values()) {
                        if (setReportUnits.size() == 1) { // check if there is one single unit for whole report
                            row.UnitId = (Integer) setReportUnits.toArray()[0];
                            row.Unit = mapUnitSymbols.get(row.UnitId);
                        }

                        html += composeHtmlTableRow(row, mapReportTotalRows.size() == 1 ? ROW_TOT : ROW_ROW_ACC);
                    }

                    if (mapReportTotalRows.size() > 1 || !localCurrencyFound) {
                        html += composeHtmlTableRow(rowLocalCurrencyTotal, ROW_TOT);
                    }
                }
            }

            // end of table
            html += "</table>";

            if (dataFound) {
                html += composeHtmlDocsInfoReport(accumStart, accumEnd);
            }
            else {
                html += "<p><strong>" + SLibUtils.textToHtml("¡No se encontró información para " +
                        (accumStart.equals(accumEnd) ? "la fecha solicitada" : "el período solicitado") + " " + composeTextPeriod(accumStart, accumEnd) + "!") +
                        "</strong></p>\n";
            }
        }
        
        return html;
    }
    
    private String composeHtmlMailBody() throws Exception {
        String body = ""; 
        
        body += "<h1>" + SLibUtils.textToHtml(msCompanyName) + "</h1>\n";
        
        body += composeHtmlMailBodyReport();
        body += composeHtmlMailBodyAccum();
        body += composeHtmlSalesBackorder();
        
        return body;
    }
    
    /**
     * Send report by mail.
     * @throws Exception 
     */
    public void sendMail() throws Exception {
        String subject = composeMailSubject();
        
        //SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "facturacion@aeth.mx", "-1WXGFygX*d}", "facturacion@aeth.mx");
        //SMailSender sender = new SMailSender("mail.swaplicado.com.mx", "26", "smtp", false, true, "siie@swaplicado.com.mx", "s11E2020!+", "siie@swaplicado.com.mx");
        //SMailSender sender = new SMailSender("mail.swaplicado.com.mx", "26", "smtp", false, true, "cap@swaplicado.com.mx", "C492020*&", "cap@swaplicado.com.mx");
        //SMailSender sender = new SMailSender("mail.swaplicado.com.mx", "26", "smtp", false, true, "sflores@swaplicado.com.mx", "Ch3c0m4n", "sflores@swaplicado.com.mx");
        //SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "som@aeth.mx", "AETHSOM", "som@aeth.mx");

        SMail mail = new SMail(moMailSender, subject, msHtml, maRecipientsTo);
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
            // consider that returns and discounts are both already negative!:
            return SLibUtils.roundAmount(TotGross + TotReturns + TotDiscounts);
        }
        
        public double getQtyNet() {
            // consider that returns are already negative!:
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cli;

import erp.SClient;
import erp.SParamsApp;
import erp.lib.SLibUtilities;
import erp.mtrn.data.STrnUtilities;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import sa.lib.mail.SMailUtils;

/**
 *
 * @author Isabel Servín
 */
public class SCliRepSalesPurchases {
    public static final String ERR_ARGS_INVALID = "Argumentos inválidos.";
    
    // Entero devuelto con la función weekday() de MySql
    private static final int SQL_SUNDAY = 6;
    private static final int SQL_MONDAY = 0;
    
    private static final int ARG_ID_COMPANIES_IDS = 0;
    private static final int ARG_MAIL_TO = 1;
    private static final int ARG_MAIL_BCC = 2;
    private static final String DEF_ID_COMPANIES_IDS = "2852;2217";
    private static final String DEF_MAIL_TO = "gortiz@aeth.mx;sflores@swaplicado.com.mx;isabel.garcia@swaplicado.com.mx";
    private static final String DEF_MAIL_BCC = "";
    
    private static String msSubject;
    private static String msBody;
    private static String msCompaniesNames;
    private static String msSqlWhere;
    private static String msSqlWhereParam;
    
    /**
     * @param args Los argumentos de la interfaz de línea de comandos (CLI).
     * Argumentos esperados:
     * 1: ID de las empresas que se incluirán
     * 2: Buzones correo-e destinatarios (TO) (separados con punto y coma, sin espacios en blanco entre ellos, obviamente.)
     * 3: Buzones correo-e de copia oculta (BCC) (separados con punto y coma, sin espacios en blanco entre ellos, obviamente.)
     */
    public static void main(String[] args) {
        try {
            SParamsApp paramsApp;
            SDbDatabase dbErp;
            
            String companiesIds = DEF_ID_COMPANIES_IDS;
            String mailTo = DEF_MAIL_TO;
            String mailBcc = DEF_MAIL_BCC;

            if (args.length >= 1) {
                companiesIds = args[ARG_ID_COMPANIES_IDS];
            }
            if (args.length >= 2) {
                mailTo = args[ARG_MAIL_TO];
            }
            if (args.length >= 3) {
                mailBcc = args[ARG_MAIL_BCC];
            }
            
            paramsApp = new SParamsApp();
            
            if (!paramsApp.read()) {
                throw new Exception(erp.SClient.ERR_PARAMS_APP_READING);
            }
            
            dbErp = new SDbDatabase(SDbConsts.DBMS_MYSQL);
            int result = dbErp.connect(paramsApp.getDatabaseHostClt(), paramsApp.getDatabasePortClt(), 
                    paramsApp.getDatabaseName(), paramsApp.getDatabaseUser(), paramsApp.getDatabasePswd());
            
            if (result != SDbConsts.CONNECTION_OK) {
                throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION);
            }
            
            createParamsValues(dbErp, new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(companiesIds, ";"))));
            
            if (sendMail(dbErp)) {
                msSubject = "[SIIE] Ventas netas item-cliente ";
                File report = createPdfReport(dbErp); 

                SMailSender sender = new SMailSender("mail.tron.com.mx", "26", "smtp", false, true, "facturacion@aeth.mx", "NGkeu-wR9z*D", "facturacion@aeth.mx");

                ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mailTo, ";")));

                ArrayList<String> recipientsBcc = null;
                if (!mailBcc.isEmpty()) {
                    recipientsBcc = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mailBcc, ";")));
                }

                SMail mail = new SMail(sender, SMailUtils.encodeSubjectUtf8(msSubject), composeMailBody(), recipientsTo);
                if (recipientsBcc != null) {
                    mail.getBccRecipients().addAll(recipientsBcc);
                }

                mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
                mail.getAttachments().add(report);
                mail.send();
                System.out.println("Mail sent!");

                dbErp.disconnect();
            }
        }
        catch (Exception e) {
            SLibUtils.printException(SCliMailer.class.getName(), e);
            System.exit(-1);
        }
    }
    
    private static boolean sendMail(SDbDatabase db) throws Exception {
        String sql;
        String sqlBody = "";
        ResultSet resultSet;
        ResultSet resultSetDay;
        Date date = null;
        
        msBody = "";
        
        sql = "SELECT bd, " +
                "(SELECT ADDDATE(NOW(), INTERVAL -1 DAY)) AS ini " +
                "FROM erp.cfgu_co " + msSqlWhere;
        resultSet = db.getConnection().createStatement().executeQuery(sql);
        while (resultSet.next()) {
            sqlBody += (sqlBody.isEmpty() ? "" : "UNION ") +
                    composeSqlBody(false, resultSet.getString("bd"), resultSet.getDate("ini"), null);
            date = resultSet.getDate("ini");
        }
        
        sql = "SELECT co, id_bp, bp, agt, igen, item_key, item, id_tp_bp, tp_bp, " +
                "item AS f_group, " +
                "SUM(f_stot_r) AS f_stot_r, SUM(f_adj_r) AS f_adj_r, SUM(f_adj_d) AS f_adj_d, SUM(f_stot_net) AS f_stot_net, " +
                "SUM(f_qty) AS f_qty, SUM(f_qty_r) AS f_qty_r, SUM(f_qty_net) AS f_qty_net, COALESCE(SUM(f_stot_net) / SUM(f_qty_net), 0.0) AS f_avg_price, " +
                "SUM(_st_xml) AS _st_xml " +
                "FROM (" +
                sqlBody + ") " + 
                "AS t " + msSqlWhereParam + " " + 
                "GROUP BY co, item, bp " +
                "ORDER BY co, item, bp";
        resultSet = db.getConnection().createStatement().executeQuery(sql);
        if (resultSet.next()) {
            return true;
        }
        else {
            sql = "SELECT WEEKDAY(NOW());";
            resultSetDay = db.getConnection().createStatement().executeQuery(sql);
            if (resultSetDay.next()) {
                if (resultSetDay.getInt(1) != SQL_SUNDAY && resultSetDay.getInt(1) != SQL_MONDAY) {
                    msBody = SLibUtils.textToHtml("No hubo facturas ni notas de crédito en la fecha " + SLibUtils.DateFormatDate.format(date));
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static File createPdfReport(SDbDatabase db) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        JasperPrint jasperPrint;
        String sqlParam = "";
        String sql;
        ResultSet resultSet;
        Date dtInitial = null;
        Date dtEnd = null;
        
        sql = "SELECT bd, " +
                "(SELECT DATE_ADD(DATE_ADD(LAST_DAY(ADDDATE(NOW(), INTERVAL -1 DAY)), INTERVAL 1 DAY),INTERVAL -1 MONTH)) AS ini, " +
                "(SELECT ADDDATE(NOW(), INTERVAL -1 DAY)) AS fin " +
                "FROM erp.cfgu_co " + msSqlWhere;
        resultSet = db.getConnection().createStatement().executeQuery(sql);
        while (resultSet.next()) {
            sqlParam += (sqlParam.isEmpty() ? "" : "UNION ") +
                    composeSqlBody(true, resultSet.getString("bd"), resultSet.getDate("ini"), resultSet.getDate("fin"));
            dtInitial = resultSet.getDate("ini");
            dtEnd = resultSet.getDate("fin");
        }
        
        msSubject += "del " + SLibUtils.DateFormatDateShort.format(dtInitial) + " al " + SLibUtils.DateFormatDateShort.format(dtEnd);
        
        map.put("sCompanyName", msCompaniesNames.toUpperCase());
        map.put("sUserName", "N/A");
        map.put("sVendorLabel", SClient.VENDOR_COPYRIGHT);
        map.put("sVendorWebsite", SClient.VENDOR_WEBSITE);
        map.put("bShowDetailBackground", false);
        map.put("oDateFormat", SLibUtils.DateFormatDate);
        map.put("oDatetimeFormat", SLibUtils.DateFormatDatetime);
        map.put("oTimeFormat", SLibUtils.DateFormatTime);
        map.put("oValueFormat", SLibUtils.getDecimalFormatAmount());
        map.put("sTitle", "REPORTE DE VENTAS NETAS");
        map.put("sFilter", "POR ÍTEM - CLIENTE");
        map.put("sSqlParam", sqlParam);
        map.put("sSqlWhereParam", msSqlWhereParam);
        map.put("tDtInitial", dtInitial);
        map.put("tDtEnd", dtEnd);
        
        File fileTemplate = new File("reps/trn_ps_item_bp_unit.jasper");
            
        JasperReport relatoriosJasper = (JasperReport)JRLoader.loadObject(fileTemplate);
        jasperPrint = JasperFillManager.fillReport(relatoriosJasper, map, db.getConnection());
        
        File report = File.createTempFile("Ventas netas item - cliente ", ".pdf");
        OutputStream output = new FileOutputStream(report); 
        JasperExportManager.exportReportToPdfStream(jasperPrint, output); 
        
        return report;
    }
    
    private static void createParamsValues(SDbDatabase db, ArrayList<String> ids) throws Exception {
        String sql;
        ResultSet resultSet;
        
        msCompaniesNames = "";
        msSqlWhere = "";
        msSqlWhereParam = "";
        
        for (String id : ids) {
            msSqlWhere += (msSqlWhere.isEmpty() ? "WHERE " : "OR ") + "id_co = " + id + " ";
            msSqlWhereParam += (msSqlWhereParam.isEmpty() ? "WHERE " : "AND ") + "id_bp <> " + id + " ";
            sql = "SELECT co_key FROM erp.cfgu_co WHERE id_co = " + id;
            resultSet = db.getConnection().createStatement().executeQuery(sql);
            if (resultSet.next()) {
                msCompaniesNames += (msCompaniesNames.isEmpty() ? "" : " + ") + resultSet.getString("co_key") + "";
            }
        }
    }
    
    private static String composeSqlBody(boolean period, String dbName, Date dtIni, Date dtFin) {
        return //"#facturas: " +
                "(SELECT 1 AS co, b.id_bp, b.bp, agt.bp AS agt, ig.igen, i.item_key, i.item, btp.id_tp_bp, btp.tp_bp, " +
                "item AS f_group, " +
                "COALESCE(SUM(de.stot_r), 0.0) AS f_stot_r, 0.0 AS f_adj_r, 0.0 AS f_adj_d, COALESCE(+SUM(de.stot_r), 0.0) AS f_stot_net, " +
                "COALESCE(SUM(de.qty), 0.0) AS f_qty, 0.0 AS f_qty_r, COALESCE(+SUM(de.qty), 0.0) AS f_qty_net, COALESCE(+SUM(de.stot_r) / +SUM(de.qty), 0.0) AS f_avg_price, " +
                "(SELECT SUM(cfd.fid_st_xml) FROM " + dbName + ".trn_cfd AS cfd WHERE cfd.fid_dps_year_n = d.id_year AND cfd.fid_dps_doc_n = d.id_doc AND COALESCE(cfd.fid_st_xml, 1) = 1 ) AS _st_xml " +
                "FROM " + dbName + ".trn_dps_ety AS de " +
                "INNER JOIN " + dbName + ".trn_dps AS d ON de.id_year = d.id_year AND de.id_doc = d.id_doc " +
                "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bct ON b.id_bp = bct.id_bp AND bct.id_ct_bp = 3 " +
                "INNER JOIN erp.bpsu_tp_bp AS btp ON bct.fid_tp_bp = btp.id_tp_bp AND btp.id_ct_bp = 3 " +
                "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item " +
                "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " +
                "LEFT OUTER JOIN erp.bpsu_bp AS agt ON d.fid_sal_agt_n = agt.id_bp " +
                "WHERE NOT de.b_del AND NOT d.b_del " +
                "AND d.fid_ct_dps = 2 AND d.fid_cl_dps = 3 AND d.fid_tp_dps = 1 AND d.fid_st_dps = 2 AND d.fid_st_dps_val = 1 " +
                (period ? "AND d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dtIni) + "' " +
                "AND '" + SLibUtils.DbmsDateFormatDate.format(dtFin) + "' " : 
                "AND d.dt = '" + SLibUtils.DbmsDateFormatDate.format(dtIni) + "' ") +
                "GROUP BY item, bp " +
                "ORDER BY item, bp) " +
                "UNION " +
                //"#notas de crédito de devoluciones (la cantidad SÍ importa) " +
                "(SELECT 1 AS co, b.id_bp, b.bp, agt.bp AS agt, ig.igen, i.item_key, i.item, btp.id_tp_bp, btp.tp_bp, " +
                "item AS f_group, " +
                "0.0 AS f_stot_r, COALESCE(SUM(de.stot_r), 0.0) AS f_adj_r, 0.0 AS f_adj_d, COALESCE(-SUM(de.stot_r), 0.0) AS f_stot_net, " +
                "0.0 AS f_qty, COALESCE(SUM(de.qty), 0.0) AS f_qty_r, COALESCE(-SUM(de.qty), 0.0) AS f_qty_net, COALESCE(-SUM(de.stot_r) / -SUM(de.qty), 0.0) AS f_avg_price, " +
                "(SELECT SUM(cfd.fid_st_xml) FROM " + dbName + ".trn_cfd AS cfd WHERE cfd.fid_dps_year_n = d.id_year AND cfd.fid_dps_doc_n = d.id_doc AND COALESCE(cfd.fid_st_xml, 1) = 1 ) AS _st_xml " +
                "FROM " + dbName + ".trn_dps_ety AS de " +
                "INNER JOIN " + dbName + ".trn_dps AS d ON de.id_year = d.id_year AND de.id_doc = d.id_doc " +
                "INNER JOIN " + dbName + ".trn_dps_dps_adj AS adj ON de.id_year = adj.id_adj_year AND de.id_doc = adj.id_adj_doc AND de.id_ety = adj.id_adj_ety " +
                "INNER JOIN " + dbName + ".trn_dps_ety AS ded ON adj.id_dps_year = ded.id_year AND adj.id_dps_doc = ded.id_doc AND adj.id_dps_ety = ded.id_ety " +
                "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bct ON b.id_bp = bct.id_bp AND bct.id_ct_bp = 3 " +
                "INNER JOIN erp.bpsu_tp_bp AS btp ON bct.fid_tp_bp = btp.id_tp_bp AND btp.id_ct_bp = 3 " +
                "INNER JOIN erp.itmu_item AS i ON ded.fid_item = i.id_item " +
                "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " +
                "LEFT OUTER JOIN erp.bpsu_bp AS agt ON d.fid_sal_agt_n = agt.id_bp " +
                "WHERE NOT de.b_del AND NOT d.b_del " +
                "AND d.fid_ct_dps = 2 AND d.fid_cl_dps = 5 AND d.fid_tp_dps = 1 AND d.fid_st_dps = 2 AND d.fid_st_dps_val = 1 " +
                (period ? "AND d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dtIni) + "' " +
                "AND '" + SLibUtils.DbmsDateFormatDate.format(dtFin) + "' " : 
                "AND d.dt = '" + SLibUtils.DbmsDateFormatDate.format(dtIni) + "' ") +
                "AND de.fid_tp_dps_adj = 2 " +
                "GROUP BY item, bp " +
                "ORDER BY item, bp) " +
                "UNION " +
                //"#notas de crédito de descuentos (la cantidad NO importa) " +
                "(SELECT 1 AS co, b.id_bp, b.bp, agt.bp AS agt, ig.igen, i.item_key, i.item, btp.id_tp_bp, btp.tp_bp, " +
                "item AS f_group, " +
                "0.0 AS f_stot_r, 0.0 AS f_adj_r, COALESCE(SUM(de.stot_r), 0.0) AS f_adj_d, COALESCE(-SUM(de.stot_r), 0.0) AS f_stot_net, " +
                "0.0 AS f_qty, 0.0 AS f_qty_r, 0.0 AS f_qty_net, 0.0 AS f_avg_price, " +
                "(SELECT SUM(cfd.fid_st_xml) FROM " + dbName + ".trn_cfd AS cfd WHERE cfd.fid_dps_year_n = d.id_year AND cfd.fid_dps_doc_n = d.id_doc AND COALESCE(cfd.fid_st_xml, 1) = 1 ) AS _st_xml " +
                "FROM " + dbName + ".trn_dps_ety AS de " +
                "INNER JOIN " + dbName + ".trn_dps AS d ON de.id_year = d.id_year AND de.id_doc = d.id_doc " +
                "INNER JOIN " + dbName + ".trn_dps_dps_adj AS adj ON de.id_year = adj.id_adj_year AND de.id_doc = adj.id_adj_doc AND de.id_ety = adj.id_adj_ety " +
                "INNER JOIN " + dbName + ".trn_dps_ety AS ded ON adj.id_dps_year = ded.id_year AND adj.id_dps_doc = ded.id_doc AND adj.id_dps_ety = ded.id_ety " +
                "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bct ON b.id_bp = bct.id_bp AND bct.id_ct_bp = 3 " +
                "INNER JOIN erp.bpsu_tp_bp AS btp ON bct.fid_tp_bp = btp.id_tp_bp AND btp.id_ct_bp = 3 " +
                "INNER JOIN erp.itmu_item AS i ON ded.fid_item = i.id_item " +
                "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " +
                "LEFT OUTER JOIN erp.bpsu_bp AS agt ON d.fid_sal_agt_n = agt.id_bp " +
                "WHERE NOT de.b_del AND NOT d.b_del " +
                "AND d.fid_ct_dps = 2 AND d.fid_cl_dps = 5 AND d.fid_tp_dps = 1 AND d.fid_st_dps = 2 AND d.fid_st_dps_val = 1 " +
                (period ? "AND d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dtIni) + "' " +
                "AND '" + SLibUtils.DbmsDateFormatDate.format(dtFin) + "' " : 
                "AND d.dt = '" + SLibUtils.DbmsDateFormatDate.format(dtIni) + "' ") +
                "AND de.fid_tp_dps_adj = 3 " +
                "GROUP BY item, bp " +
                "ORDER BY item, bp) " +
                " " 
                ;
    }
    
    private static String composeMailBody() {
        return "<html><body>" +
                "<b>" + msBody + "</b>" + 
                "<br>" + 
                STrnUtilities.composeMailFooter("warning") +
                "</body></html>";
    }
}

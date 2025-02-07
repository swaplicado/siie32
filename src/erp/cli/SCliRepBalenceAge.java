package erp.cli;

import erp.SClient;
import erp.SParamsApp;
import erp.lib.SLibUtilities;
import erp.mod.SModSysConsts;
import erp.mtrn.data.STrnUtilities;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
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
 * @author Claudio Peña
 * Clase SCliRepBalenceAge para generar y enviar reportes de antigüedad de saldos de clientes.
 */
public class SCliRepBalenceAge {
    public static final String ERR_ARGS_INVALID = "Argumentos inválidos."; 
    
    private static final int ARG_ID_COMPANIES_IDS = 0;
    private static final int ARG_MAIL_TO = 1;
    private static final int ARG_MAIL_BCC = 2;
    private static final String DEF_ID_COMPANIES_IDS = "2852;2217"; // AETH, AME
//    private static final String DEF_MAIL_TO = "gortiz@aeth.mx";
    private static final String DEF_MAIL_TO = "claudio.pena@swaplicado.com.mx";
//    private static final String DEF_MAIL_TO = "claudio.pena@swaplicado.com.mx;sflores@swaplicado.com.mx";
    private static final String DEF_MAIL_BCC = "claudio.pena@swaplicado.com.mx";
//    private static final String DEF_MAIL_BCC = "claudio.pena@swaplicado.com.mx;sflores@swaplicado.com.mx";
    private static final int BP_AETH = 2852;
    private static final int BP_AME = 2217;
            
    private static String msSubject;
    private static String msCompaniesNames;
    private static String msSqlWhere;
    
    private static SMailSender moMailSender;
    
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {
        try {
            SParamsApp paramsApp;
            SDbDatabase dbErp;
            
            String companiesIds = DEF_ID_COMPANIES_IDS;
            String mailTo = DEF_MAIL_TO;
            String mailBcc = DEF_MAIL_BCC;
            LocalDate localDate = LocalDate.now();
            Date hora = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String formattedTime = formatter.format(hora);

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
            msSubject = "[SIIE] Antiguedad saldos clientes " + localDate;
            List<File> reportFiles = createPdfReports(dbErp);  // Crear los reportes
            
            ArrayList<String> recipientsTo = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mailTo, ";")));
            ArrayList<String> recipientsBcc = null;
            if (!mailBcc.isEmpty()) {
                recipientsBcc = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mailBcc, ";")));
            }
            
            String bodyUtf8 = new String(composeMailBody(localDate, formattedTime).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            
            
            SMail mail = new SMail(moMailSender, SMailUtils.encodeSubjectUtf8(msSubject), bodyUtf8, recipientsTo);
               if (recipientsBcc != null) {
                   mail.getBccRecipients().addAll(recipientsBcc);
               }

               mail.setContentType(SMailConsts.CONT_TP_TEXT_HTML);
               for (File reportFile : reportFiles) {
                    mail.getAttachments().add(reportFile);
               }
            mail.send();

            dbErp.disconnect();

        } catch (Exception e) {
            SLibUtils.printException(SCliRepBalenceAge.class.getName(), e);
            System.exit(-1);
        }
    }
    
    private static List<File> createPdfReports(SDbDatabase db) throws Exception {
        List<File> reportFiles = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        LocalDate localDate = LocalDate.now();
        Date currentDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        int year = localDate.getYear();

        String sql = "SELECT id_co, bd, co, co_key FROM erp.cfgu_co " + msSqlWhere;
        ResultSet resultSet = db.getConnection().createStatement().executeQuery(sql);

        while (resultSet.next()) {
            int idBp = resultSet.getInt("id_co");
            String sqlParamBD = resultSet.getString("bd");
            String nameErp = resultSet.getString("co");
            String nameShortErp = resultSet.getString("co_key");

//            System.out.println("Generando reporte para empresa: " + nameShortErp); //Prueba para saber si el reporte se esta generando

            // Configura parámetros específicos para cada empresa
            map.clear();  
            map.put("sTitle", "ANTIGÜEDAD DE SALDOS CLIENTES");
            map.put("bShowDetail", true); 
            map.put("sCoBranch", "SUCURSAL: MORELIA"); 
            map.put("nFkCtBpId", 3 ); 
            map.put("sBizPartner", "CLIENTE: (TODOS)");
            map.put("nBizPartnerCategory", 3);
            map.put("sSalesAgent", "");
            map.put("sAnalyst", "");
            map.put("nLocalCurrencyId", 1); //Pesos mexicanos
            map.put("nCurrencyId", 1); //Pesos mexicanos
            map.put("sCurrency", "MONEDA: PESOS MEXICANOS"); //Pesos mexicanos
            map.put("nYear", year);
            map.put("tDateCutoff", currentDate); 
            map.put("nSysAccountClassId", SModSysConsts.FINS_CL_SYS_ACC_BPR_CUS);
            map.put("nAccountTypeId", SModSysConsts.FINS_TP_ACC_BAL);
            map.put("sSqlFilterBizPartner", "");
            map.put("sSqlFilterSalesAgent", "");
            map.put("sSqlFilterAnalyst", "");
            map.put("sSqlFilterCurrency", "");
            map.put("sSqlSortBy", " _due_dt, d.num_ser, d.num, d.id_year, d.id_doc, re.ref ");  
            if (idBp == BP_AETH) {
               map.put("sSqlFilterCoBranch", " AND (d.fid_cob = 2890 OR (re.fid_dps_year_n IS NULL AND r.fid_cob = 2890))" );
               map.put("sFuncText", "TODAS");
               map.put("sFilterFunctionalArea", " AND d.fid_func IN ( 1, 2, 3, 4, 5, 6 )" );
            } else if (idBp == BP_AME) {
               map.put("sSqlFilterCoBranch", " AND (d.fid_cob = 5051 OR (re.fid_dps_year_n IS NULL AND r.fid_cob = 5051))" );
               map.put("sFuncText", "(ND)");
               map.put("sFilterFunctionalArea", "" );
            }
            map.put("bShowGarntInsur", false);
            map.put("sSqlParamDB", sqlParamBD);
            map.put("sCompanyName", nameErp);
            map.put("sVendorLabel", SClient.VENDOR_COPYRIGHT);
            map.put("sVendorWebsite", SClient.VENDOR_WEBSITE);
            map.put("sUserName", "SIIE CLI");

            File fileTemplate = new File("reps/fin_bps_acc_agi_aut.jasper");
            JasperReport reportTemplate = (JasperReport) JRLoader.loadObject(fileTemplate);

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportTemplate, map, db.getConnection());

            String fileName = "Antiguedad saldos clientes " + nameShortErp + " al " + localDate + ".pdf";
            byte[] utf8Bytes = fileName.getBytes("UTF-8");
            fileName = new String(utf8Bytes, "UTF-8");

            File report = new File(fileName);
            
            try (OutputStream output = new FileOutputStream(report)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, output);
            }

            reportFiles.add(report);
        }

        return reportFiles;
    }

    private static void createParamsValues(SDbDatabase db, ArrayList<String> ids) throws Exception {
        String sql;
        ResultSet resultSet;
        
        msCompaniesNames = "";
        msSqlWhere = "";
        String bd = "";
        
        for (String id : ids) {
            msSqlWhere += (msSqlWhere.isEmpty() ? "WHERE " : "OR ") + "id_co = " + id + " ";
            sql = "SELECT * FROM erp.cfgu_co WHERE id_co = " + id;
            resultSet = db.getConnection().createStatement().executeQuery(sql);
            
            while (resultSet.next()) {
                msCompaniesNames += resultSet.getString("co") + " ";
                bd = resultSet.getString("bd");

            }
            resultSet.close();
            
            if (moMailSender == null && !bd.isEmpty()) {
                sql = "SELECT * FROM " + bd + ".cfg_mms WHERE fk_tp_mms = " + SModSysConsts.CFGS_TP_MMS_MAIL_REPS;
                resultSet = db.getConnection().createStatement().executeQuery(sql);
                if (resultSet.next()) {
                    moMailSender = new SMailSender(resultSet.getString("host"), resultSet.getString("port"), resultSet.getString("prot"), 
                            resultSet.getBoolean("b_tls"), resultSet.getBoolean("b_auth"), resultSet.getString("usr"), resultSet.getString("usr_pswd"), resultSet.getString("arb_email"));
                }
                resultSet.close();
            }
        }
    }
    
    private static String composeMailBody(LocalDate localDate, String formattedTime) {
        return "<html>" +
                SLibUtils.textToHtml("Antigüedad de saldos de clientes de AETH con corte al " + localDate + " a las " + formattedTime ) +
                "<body>" +
                "<br>" + 
                STrnUtilities.composeMailFooter("warning") +
                "</body></html>";
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cli;

import erp.SParamsApp;
import erp.lib.SLibUtilities;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;

/**
 *
 * @author Isabel Servín
 * Elimina todos los archivos de CFDI de nóminas (PDF y XML) guardados en disco.
 */
public class SCliNomDelete {
    public static final String ERR_ARGS_INVALID = "Argumentos inválidos.";
    
    private static final int ARG_ID_COMPANIES_IDS = 0;
    private static final String DEF_ID_COMPANIES_IDS = "0"; // 0 hace un barrido de todas las empresas
    
    private static final Logger LOGGER = Logger.getLogger("erp.cli.SCliNomDelete");
    
    private static final ArrayList<String> moFileExt = new ArrayList<String>(Arrays.asList("pdf", "xml"));
    
    public static void main(String[] args) {
        try {
            SParamsApp paramsApp;
            SDbDatabase dbErp;
            
            String companiesIds = DEF_ID_COMPANIES_IDS;
            
            if (args.length >= 1) {
                companiesIds = args[ARG_ID_COMPANIES_IDS];
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
            
            run(dbErp, new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(companiesIds, ";"))));
        }
        catch (Exception e) {
            SLibUtils.printException(SCliNomDelete.class.getName(), e);
        }
    }

    private static void run(SDbDatabase db, ArrayList<String> ids) throws Exception {
        String sql;
        String dbName;
        String rfc;
        String dir = "";
        Timestamp date = null;
        ResultSet resultSetCompanies;
        ResultSet resultSetDir;
        Statement statement = db.getConnection().createStatement();
        
        for (String id : ids) {
            sql = "SELECT c.bd, b.fiscal_id FROM erp.cfgu_co AS c "
                    + "INNER JOIN erp.bpsu_bp AS b ON c.id_co = b.id_bp " 
                    + (id.equals("0") ? "" : "WHERE id_co = " + id);
            resultSetCompanies = db.getConnection().createStatement().executeQuery(sql);
            while (resultSetCompanies.next()) {
                dbName = resultSetCompanies.getString(1);
                rfc = resultSetCompanies.getString(2);

                try {
                    sql = "SELECT xml_base_dir, NOW() FROM " + dbName + ".cfg_param_co";
                    resultSetDir = statement.executeQuery(sql);
                    if (resultSetDir.next()) {
                        dir = resultSetDir.getString(1);
                        date = resultSetDir.getTimestamp(2);
                    }
                    resultSetDir.close();

                    delete(date, rfc, dir);
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            resultSetCompanies.close();
        }
    }

    private static void delete(Timestamp date, String rfc, String dir) {
        try {
            File file = new File("logs/hrs");
            file.mkdir();
            Handler fileHandler = new FileHandler("logs/hrs/SCliNomDelete_" + rfc + "_" + SLibUtils.DbmsDateFormatDatetime.format(date).replaceAll(" ", "_").replaceAll(":", "") + ".log", true);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            LOGGER.addHandler(fileHandler);
            LOGGER.log(Level.INFO, "{0}\n", "Proceso inicializado");
            
            for (String ext : moFileExt) {
                String log;
                String path = dir.replaceAll("/", "\\\\") + rfc + "_N_*." + ext;
                String commandDir = "dir \"" + path + "\"";
                String commandDel = "del /s /q \"" + path + "\"";

                log = getDirLog(commandDir);
                LOGGER.log(Level.INFO,"{0}\n", log);
                
                Runtime.getRuntime().exec("cmd.exe /c " + commandDel);
                
                log = getDirLog(commandDir);
                LOGGER.log(Level.INFO,"{0}\n", log);
            }
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "{0}\n", e.getMessage());
        }
    }
    
    private static String getDirLog(String dir) throws Exception {
        String log = "";
        
        InputStream is = Runtime.getRuntime().exec("cmd.exe /c " + dir).getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        try (BufferedReader br = new BufferedReader(isr)) {
            while (br.readLine() != null) {
                log += br.readLine() + "\n";
            }
        }
        
        return log;
    }
}

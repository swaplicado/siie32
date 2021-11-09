package erp.cfd.utils;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mod.trn.db.STrnUtils;
import erp.mtrn.data.SDataCfdPayment;
import java.sql.ResultSet;
import java.sql.Statement;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.prt.SPrtConsts;
import sa.lib.prt.SPrtUtils;

/**
 * Generación de registros CFDI de recepción de pagos a partir de los CFDI de recepción de pagos existentes que no los tengan.
 * @author Sergio Flores
 */
public class SReceiptPaymentGen {
    
    private static final int ARG_DBHOST = 0;
    private static final int ARG_DBUSER = 1;
    private static final int ARG_DBPSWD = 2;
    private static final int ARG_DBNAME = 3;
    private static final int ARG_YEAR = 4;
    private static final int DIGITS = 6;
    
    public void displayApp() {
        System.out.println("===== SReceiptPaymentGen =====");
    }
    
    public void displayCopyright() {
        System.out.println("(c) Software Aplicado SA de CV. All rights reserved.");
    }
    
    public void displayHelp() {
        System.out.println("ARGUMENT \"help\"");
        System.out.println("Syntax options:");
        System.out.println("1) SReceiptPaymentGen <dbhost{:port}> <dbuser> <dbpswd> <dbname>");
        System.out.println("   To check how many receipts of payment need to be generated each year.");
        System.out.println("2) SReceiptPaymentGen <dbhost{:port}> <dbuser> <dbpswd> <dbname> <year>");
        System.out.println("   To generate receipts of payment of given year.");
        System.out.println("3) SReceiptPaymentGen help");
        System.out.println("   To display help.");
    }
    
    public void displaySyntaxError(String[] args) {
        System.out.println("Syntax error:");
        System.out.println("Try argument \"help\" for further information.");
        if (args.length == 0) {
            System.out.println("No arguments provided.");
        }
        else {
            System.out.println("List of arguments:");
            for (int arg = 0; arg < args.length; arg++) {
                System.out.println("Argument #" + (arg + 1) + ": [" + args[arg] + "]");
            }
        }
    }
    
    private String[] splitHostAndPort(String dbHostAndPort) {
        String[] splitted = dbHostAndPort.split(":");
        String host = splitted[0];
        String port = splitted.length == 1 ? "" : splitted[1];
        return new String[] { host, port };
    }
    
    private SDbDatabase connect(String dbHostAndPort, String dbUser, String dbPswd, String dbName) {
        String[] splitted = splitHostAndPort(dbHostAndPort);
        SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);
        database.connect(splitted[0], splitted[1], dbName, dbUser, dbPswd);
        return database;
    }
    
    public void check(String dbHostAndPort, String dbUser, String dbPswd, String dbName) {
        try {
            SDbDatabase database = connect(dbHostAndPort, dbUser, dbPswd, dbName);
            
            try (Statement statement = database.getConnection().createStatement()) {
                System.out.println("Checking receipts of payment to be generated...");
                System.out.println("year   CFDI");
                System.out.println("---- ------");
                
                String sql;
                ResultSet resultSet;
                int total = 0;
                
                sql = "SELECT YEAR(ts) AS _year, COUNT(*) AS _count "
                        + "FROM trn_cfd "
                        + "WHERE fid_tp_cfd = " + SDataConstantsSys.TRNS_TP_CFD_PAY_REC + " AND fid_rcp_pay_n IS NULL "
                        + "GROUP BY YEAR(ts) "
                        + "ORDER BY YEAR(ts);";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    total += resultSet.getInt("_count");
                    System.out.println(resultSet.getInt("_year") + " " + SPrtUtils.formatText(resultSet.getString("_count"), DIGITS, SPrtConsts.ALIGN_RIGHT, SPrtConsts.TRUNC_HIDE));
                }
                
                if (total > 0) {
                    System.out.println("---- ------");
                }
                System.out.println("Tot. " + SPrtUtils.formatText("" + total, DIGITS, SPrtConsts.ALIGN_RIGHT, SPrtConsts.TRUNC_HIDE));
            }
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }

    public void generate(String dbHostAndPort, String dbUser, String dbPswd, String dbName, int year) {
        SDbDatabase database = null;
        boolean isTransactionActive = false;
        
        try {
            database = connect(dbHostAndPort, dbUser, dbPswd, dbName);
            
            try (Statement statement = database.getConnection().createStatement()) {
                System.out.println("Generating pending receipts of payment for " + year + "...");
                
                String sql;
                ResultSet resultSet;
                int total = 0;
                
                sql = "SELECT COUNT(*) AS _count "
                        + "FROM trn_cfd "
                        + "WHERE fid_tp_cfd = " + SDataConstantsSys.TRNS_TP_CFD_PAY_REC + " AND fid_rcp_pay_n IS NULL AND "
                        + "YEAR(ts) = " + year + ";";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    total = resultSet.getInt("_count");
                }
                
                if (total == 0) {
                    System.out.println("There are not any pending receipts of payment for " + year + "!");
                }
                else {
                    System.out.println("Pending receipts of payment for " + year + ": " + total + ".");

                    int gen = 0;
                    Statement statementAux = database.getConnection().createStatement();

                    sql = "SELECT id_cfd, ser, num, ts "
                            + "FROM trn_cfd "
                            + "WHERE fid_tp_cfd = " + SDataConstantsSys.TRNS_TP_CFD_PAY_REC + " AND fid_rcp_pay_n IS NULL AND "
                            + "YEAR(ts) = " + year + " "
                            + "ORDER BY id_cfd;";
                    resultSet = statement.executeQuery(sql);
                    while (resultSet.next()) {
                        String cfdi = STrnUtils.formatDocNumber(resultSet.getString("ser"), resultSet.getString("num")) + " (ID = " + resultSet.getInt("id_cfd") + ")";
                        System.out.print("Processing CFDI of receipt of payment " + cfdi + " "
                                + "(" + ++gen + "/" + total + "; " + SLibUtils.DecimalFormatPercentage2D.format(gen / (double) total) + ")...");

                        SDataCfdPayment cfdPayment = new SDataCfdPayment();
                        if (cfdPayment.read(new int[] { resultSet.getInt("id_cfd") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception("\nNo se pudo leer el CFDI " + cfdi + ".");
                        }
                        else {
                            cfdPayment.setAuxIsProcessingStorageOnly(true);
                            statementAux.execute("START TRANSACTION;");
                            isTransactionActive = true;
                            if (cfdPayment.save(database.getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                                throw new Exception("\nNo se pudo leer el CFDI " + cfdi + ".");
                            }
                            else {
                                statementAux.execute("COMMIT;");
                                isTransactionActive = false;
                            }
                        }

                        System.out.println(" Generated!");
                    }
                    
                    System.out.println(gen + " out of " + total + " pending receipts of payment for " + year + " generated!");
                }
            }
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
        finally {
            if (isTransactionActive) {
                try {
                    database.getConnection().createStatement().execute("ROLLBACK;");
                }
                catch (Exception e) {
                    SLibUtils.printException(this, e);
                }
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SReceiptPaymentGen rpg = new SReceiptPaymentGen();
        rpg.displayApp();
        
//        args = new String[] { "localhost:3306", "root", "msroot", "erp_otsa" };
//        args = new String[] { "localhost:3306", "root", "msroot", "erp_otsa", "2019" };
        
        switch (args.length) {
            case 1: // help
                if (args[0].equals("help")) {
                    rpg.displayHelp();
                }
                else {
                    rpg.displaySyntaxError(args);
                }
                break;
            case 4: // check
                rpg.check(args[ARG_DBHOST], args[ARG_DBUSER], args[ARG_DBPSWD], args[ARG_DBNAME]);
                break;
            case 5: // generate
                rpg.generate(args[ARG_DBHOST], args[ARG_DBUSER], args[ARG_DBPSWD], args[ARG_DBNAME], SLibUtils.parseInt(args[ARG_YEAR]));
                break;
            default:
                rpg.displaySyntaxError(args);
        }
        
        rpg.displayCopyright();
    }
}

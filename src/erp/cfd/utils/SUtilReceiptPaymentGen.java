package erp.cfd.utils;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mod.trn.db.STrnUtils;
import erp.mtrn.data.SDataCfdPayment;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.prt.SPrtConsts;
import sa.lib.prt.SPrtUtils;

/**
 * Generación de registros CFDI de recepción de pagos a partir de los CFDI de recepción de pagos existentes que no los tengan.
 * @author Sergio Flores
 */
public class SUtilReceiptPaymentGen {
    
    private static final int ARG_COMMAND = 0;
    private static final int ARG_DBHOST = 1;
    private static final int ARG_DBUSER = 2;
    private static final int ARG_DBPSWD = 3;
    private static final int ARG_DBNAME = 4;
    private static final int ARG_YEAR = 5;
    
    private static final int DIGITS = 6;
    
    protected boolean mbIsTransactionActive;
    protected SDbDatabase moDatabase;
    
    public SUtilReceiptPaymentGen() {
        mbIsTransactionActive = false;
        moDatabase = null;
    }
    
    public boolean isTransactionActive() {
        return mbIsTransactionActive;
    }
    
    public SDbDatabase getDatabase() {
        return moDatabase;
    }
    
    public void displayApp() {
        System.out.println("===== " + getClass().getSimpleName() + " =====");
    }
    
    public void displaySeparator() {
        System.out.println("----- - - -  o O o  - - - -----");
    }
    
    public void displayCopyright() {
        System.out.println("\n(c) Software Aplicado SA de CV. All rights reserved.");
    }
    
    public void displayHelp() {
        System.out.println("ARGUMENT \"help\"");
        System.out.println("Syntax options:");
        System.out.println("1) SReceiptPaymentGen clear <dbhost{:port}> <dbuser> <dbpswd> <dbname>");
        System.out.println("   To clear all receipts of payment already generated.");
        System.out.println("2) SReceiptPaymentGen check <dbhost{:port}> <dbuser> <dbpswd> <dbname>");
        System.out.println("   To check how many receipts of payment need to be generated each year.");
        System.out.println("3) SReceiptPaymentGen generate <dbhost{:port}> <dbuser> <dbpswd> <dbname>");
        System.out.println("   To generate receipts of payment from all years.");
        System.out.println("4) SReceiptPaymentGen generate <dbhost{:port}> <dbuser> <dbpswd> <dbname> <year>");
        System.out.println("   To generate receipts of payment from given year.");
        System.out.println("5) SReceiptPaymentGen help");
        System.out.println("   To display help.");
    }
    
    /**
     * Display in CLI a syntax error.
     * @param args main() arguments.
     */
    public void displaySyntaxError(final String[] args) {
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
    
    /**
     * Sprint host and port into a String array.
     * @param dbHostAndPort Host and port in format dbhost{:port}.
     * @return String array with host and port if any, otherwise an empty String is provided for the latter.
     */
    private String[] splitHostAndPort(final String dbHostAndPort) {
        String[] splitted = dbHostAndPort.split(":");
        String host = splitted[0];
        String port = splitted.length == 1 ? "" : splitted[1];
        return new String[] { host, port };
    }
    
    /**
     * Connect to database.
     * @param dbHostAndPort Host and port in format dbhost{:port}.
     * @param dbUser Database user.
     * @param dbPswd Password of database user.
     * @param dbName Database name.
     * @return Database.
     */
    private SDbDatabase connect(final String dbHostAndPort, final String dbUser, final String dbPswd, final String dbName) {
        String[] splitted = splitHostAndPort(dbHostAndPort);
        SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);
        database.connect(splitted[0], splitted[1], dbName, dbUser, dbPswd);
        return database;
    }
    
    /**
     * Clear all receipts of payment already generated.
     * @param dbHostAndPort Host and port in format dbhost{:port}.
     * @param dbUser Database user.
     * @param dbPswd Password of database user.
     * @param dbName Database name.
     * @throws java.lang.Exception
     */
    public void clear(final String dbHostAndPort, final String dbUser, final String dbPswd, final String dbName) throws Exception {
        mbIsTransactionActive = false;
        moDatabase = connect(dbHostAndPort, dbUser, dbPswd, dbName);

        try (Statement statement = moDatabase.getConnection().createStatement()) {
            System.out.println("Clearing all receipts of payment already generated for '" + dbName + "'...");

            statement.execute("START TRANSACTION;");
            mbIsTransactionActive = true;

            String[] sqls = {
                "UPDATE trn_cfd SET fid_rcp_pay_n = NULL WHERE fid_rcp_pay_n IS NOT NULL;",
                "DELETE FROM trn_pay_pay_doc;",
                "DELETE FROM trn_pay_pay;",
                "DELETE FROM trn_pay;"
            };

            for (String sql : sqls) {
                statement.execute(sql);
            }

            statement.execute("COMMIT;");
            mbIsTransactionActive = false;

            System.out.println("Clearing of all receipts of payment done for '" + dbName + "'!");
        }
    }

    /**
     * Check how many receipts of payment need to be generated each year.
     * @param dbHostAndPort Host and port in format dbhost{:port}.
     * @param dbUser Database user.
     * @param dbPswd Password of database user.
     * @param dbName Database name.
     * @return Array of years with receipts of payment that need to be generated.
     */
    public ArrayList<Integer> check(final String dbHostAndPort, final String dbUser, final String dbPswd, final String dbName) throws Exception {
        ArrayList<Integer> years = new ArrayList<>();
        
        moDatabase = connect(dbHostAndPort, dbUser, dbPswd, dbName);
            
        try (Statement statement = moDatabase.getConnection().createStatement()) {
            System.out.println("Checking receipts of payment to be generated for '" + dbName + "'...");
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
                years.add(resultSet.getInt("_year"));
                total += resultSet.getInt("_count");
                System.out.println(resultSet.getInt("_year") + " " + SPrtUtils.formatText(resultSet.getString("_count"), DIGITS, SPrtConsts.ALIGN_RIGHT, SPrtConsts.TRUNC_HIDE));
            }

            if (total > 0) {
                System.out.println("---- ------");
            }
            System.out.println("Tot. " + SPrtUtils.formatText("" + total, DIGITS, SPrtConsts.ALIGN_RIGHT, SPrtConsts.TRUNC_HIDE));
        }
        
        return years;
    }

    /**
     * Generate receipts of payment from all years.
     * @param dbHostAndPort Host and port in format dbhost{:port}.
     * @param dbUser Database user.
     * @param dbPswd Password of database user.
     * @param dbName Database name.
     */
    public void generate(final String dbHostAndPort, final String dbUser, final String dbPswd, final String dbName) throws Exception {
        System.out.println("Generating pending receipts of payment for '" + dbName + "' in all years...");
        ArrayList<Integer> years = check(dbHostAndPort, dbUser, dbPswd, dbName);
        displaySeparator();
        
        int generated = 0;
        
        for (Integer year : years) {
            generated += generate(dbHostAndPort, dbUser, dbPswd, dbName, year);
            displaySeparator();
        }
        
        System.out.println(generated + " pending receipts of payment for '" + dbName + "' in all years already generated!");
    }

    /**
     * Generate receipts of payment from given year.
     * @param dbHostAndPort Host and port in format dbhost{:port}.
     * @param dbUser Database user.
     * @param dbPswd Password of database user.
     * @param dbName Database name.
     * @param year
     * @return Number of generated receipts of payment.
     */
    public int generate(final String dbHostAndPort, final String dbUser, final String dbPswd, final String dbName, final int year) throws Exception {
        int generated = 0;
        
        mbIsTransactionActive = false;
        moDatabase = connect(dbHostAndPort, dbUser, dbPswd, dbName);
            
        try (Statement statement = moDatabase.getConnection().createStatement()) {
            System.out.println("Generating pending receipts of payment for '" + dbName + "' in " + year + "...");

            int total = 0;
            String sql;
            ResultSet resultSet;

            sql = "SELECT COUNT(*) AS _count "
                    + "FROM trn_cfd "
                    + "WHERE fid_tp_cfd = " + SDataConstantsSys.TRNS_TP_CFD_PAY_REC + " AND fid_rcp_pay_n IS NULL AND "
                    + "YEAR(ts) = " + year + ";";
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                total = resultSet.getInt("_count");
            }

            if (total == 0) {
                System.out.println("There are not any pending receipts of payment in " + year + "!");
            }
            else {
                System.out.println("Pending receipts of payment in " + year + ": " + total + ".");

                Statement statementAux = moDatabase.getConnection().createStatement();

                sql = "SELECT id_cfd, ser, num, ts "
                        + "FROM trn_cfd "
                        + "WHERE fid_tp_cfd = " + SDataConstantsSys.TRNS_TP_CFD_PAY_REC + " AND fid_rcp_pay_n IS NULL AND "
                        + "YEAR(ts) = " + year + " "
                        + "ORDER BY id_cfd;";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    String cfdi = STrnUtils.formatDocNumber(resultSet.getString("ser"), resultSet.getString("num")) + " (CFDI ID = " + resultSet.getInt("id_cfd") + ")";
                    System.out.print("Processing CFDI No. " + cfdi + " "
                            + "(" + (generated + 1) + "/" + total + "; " + SLibUtils.DecimalFormatPercentage2D.format((generated + 1) / (double) total) + ")...");

                    SDataCfdPayment cfdPayment = new SDataCfdPayment();
                    cfdPayment.setAuxIsProcessingStorageOnly(true);

                    if (cfdPayment.read(new int[] { resultSet.getInt("id_cfd") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception("\nCFDI No. " + cfdi + " could not be read!");
                    }
                    else {
                        statementAux.execute("START TRANSACTION;");
                        mbIsTransactionActive = true;

                        if (cfdPayment.save(moDatabase.getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception("\nReceipt of payment of CFDI No. " + cfdi + " could not be generated!.");
                        }
                        else {
                            generated++;

                            statementAux.execute("COMMIT;");
                            mbIsTransactionActive = false;
                        }
                    }

                    System.out.println(" Generated! (Receipt ID = " + cfdPayment.getDbmsReceiptPayment().getPkReceiptId() + ")");
                }

                System.out.println(generated + " out of " + total + " pending receipts of payment for '" + dbName + "' in " + year + " already generated!");
            }
        }
        
        return generated;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        args = new String[] { "clear", "localhost:3306", "root", "msroot", "erp_otsa" };
//        args = new String[] { "check", "localhost:3306", "root", "msroot", "erp_otsa" };
//        args = new String[] { "generate", "localhost:3306", "root", "msroot", "erp_otsa" };
//        args = new String[] { "generate", "localhost:3306", "root", "msroot", "erp_otsa", "2018" };
//        args = new String[] { "generate", "localhost:3306", "root", "msroot", "erp_otsa", "2019" };
//        args = new String[] { "generate", "localhost:3306", "root", "msroot", "erp_otsa", "2020" };
//        args = new String[] { "generate", "localhost:3306", "root", "msroot", "erp_otsa", "2021" };
        
//        args = new String[] { "clear", "192.168.1.233:3306", "root", "msroot", "erp_aeth"};
//        args = new String[] { "check", "192.168.1.233:3306", "root", "msroot", "erp_aeth"};
//        args = new String[] { "generate", "192.168.1.233:3306", "root", "msroot", "erp_aeth" };
//        args = new String[] { "generate", "192.168.1.233:3306", "root", "msroot", "erp_aeth", "2018" };
//        args = new String[] { "generate", "192.168.1.233:3306", "root", "msroot", "erp_aeth", "2019" };
//        args = new String[] { "generate", "192.168.1.233:3306", "root", "msroot", "erp_aeth", "2020" };
//        args = new String[] { "generate", "192.168.1.233:3306", "root", "msroot", "erp_aeth", "2021" };

//        args = new String[] { "clear", "192.168.1.233:3306", "root", "msroot", "erp_amesa" };
//        args = new String[] { "check", "192.168.1.233:3306", "root", "msroot", "erp_amesa" };
//        args = new String[] { "generate", "192.168.1.233:3306", "root", "msroot", "erp_amesa" };
        
//        args = new String[] { "clear", "192.168.1.233:3306", "root", "msroot", "erp_ame_lmei" };
//        args = new String[] { "check", "192.168.1.233:3306", "root", "msroot", "erp_ame_lmei" };
//        args = new String[] { "generate", "192.168.1.233:3306", "root", "msroot", "erp_ame_lmei" };
        
//        args = new String[] { "clear", "192.168.1.233:3306", "root", "msroot", "erp_otsa" };
//        args = new String[] { "check", "192.168.1.233:3306", "root", "msroot", "erp_otsa" };
//        args = new String[] { "generate", "192.168.1.233:3306", "root", "msroot", "erp_otsa" };
        
//        args = new String[] { "clear", "192.168.1.233:3306", "root", "msroot", "erp_ggs" };
//        args = new String[] { "check", "192.168.1.233:3306", "root", "msroot", "erp_ggs" };
//        args = new String[] { "generate", "192.168.1.233:3306", "root", "msroot", "erp_ggs" };
        
//        args = new String[] { "clear", "192.168.1.233:3306", "root", "msroot", "erp_sasa" };
//        args = new String[] { "check", "192.168.1.233:3306", "root", "msroot", "erp_sasa" };
//        args = new String[] { "generate", "192.168.1.233:3306", "root", "msroot", "erp_sasa" };
        
        SUtilReceiptPaymentGen urpr = new SUtilReceiptPaymentGen();
        urpr.displayApp();
        
        try {
            switch (args.length) {
                case 1:
                    switch (args[ARG_COMMAND]) {
                        case "help":
                            urpr.displayHelp();
                            break;
                        default:
                            urpr.displaySyntaxError(args);
                    }
                    break;
                case 5:
                    switch (args[ARG_COMMAND]) {
                        case "check":
                            urpr.check(args[ARG_DBHOST], args[ARG_DBUSER], args[ARG_DBPSWD], args[ARG_DBNAME]);
                            break;
                        case "clear":
                            urpr.clear(args[ARG_DBHOST], args[ARG_DBUSER], args[ARG_DBPSWD], args[ARG_DBNAME]);
                            break;
                        case "generate":
                            urpr.generate(args[ARG_DBHOST], args[ARG_DBUSER], args[ARG_DBPSWD], args[ARG_DBNAME]);
                            break;
                        default:
                            urpr.displaySyntaxError(args);
                    }
                    break;
                case 6:
                    switch (args[ARG_COMMAND]) {
                        case "generate":
                            urpr.generate(args[ARG_DBHOST], args[ARG_DBUSER], args[ARG_DBPSWD], args[ARG_DBNAME], SLibUtils.parseInt(args[ARG_YEAR]));
                            break;
                        default:
                            urpr.displaySyntaxError(args);
                    }
                    break;
                default:
                    urpr.displaySyntaxError(args);
            }
        }
        catch (Exception e) {
            SLibUtils.printException(urpr, e);
        }
        finally {
            if (urpr.isTransactionActive() && urpr.getDatabase() != null) {
                try {
                    urpr.getDatabase().getConnection().createStatement().execute("ROLLBACK;");
                }
                catch (Exception e) {
                    SLibUtils.printException(urpr, e);
                }
            }
            
            urpr.displayCopyright();
        }
    }
}

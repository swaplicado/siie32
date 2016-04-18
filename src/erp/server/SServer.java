/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.server;

import erp.SClient;
import erp.SParamsApp;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataConnectionMonitor;
import erp.lib.data.SDataDatabase;
import erp.mcfg.data.SDataCompany;
import erp.mcfg.data.SDataParamsErp;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Vector;
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author Sergio Flores
 * To generate stub:
 * ...[siie_path]\build\classes>"C:\Program Files\Java\jdk1.8.0_XX\bin\rmic" -classpath: .;"[sa-lib-10_path]\build\classes" erp.server.SServer
 */
public class SServer extends UnicastRemoteObject implements SServerRemote, Runnable {

    private volatile boolean mbIsActive;
    private SimpleDateFormat moDatetimeFormat;
    private Date mtSystemDatetime;

    private Registry rmiRegistry;
    private SParamsApp moParamsApp;
    private SDataDatabase moDatabase;
    private SDataParamsErp moParamsErp;
    private Vector<SDataCompany> mvCompanies;

    private SServiceSessions moServiceSessions;
    private SServiceDataLocks moServiceDataLocks;
    private SDaemonSystemDatetime moDaemonSystemDatetime;
    private SDaemonTimeoutSessions moDaemonTimeoutSessions;
    private SDaemonTimeoutLocks moDaemonTimeoutDataLocks;
    private SDataConnectionMonitor moConnectionMonitor;

    public SServer() throws RemoteException {
        TimeZone.setDefault(SLibTimeUtilities.SysTimeZone);

        mbIsActive = false;
        moDatetimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        updateDatetime();

        rmiRegistry = null;
        moParamsApp = null;
        moDatabase = null;
        moParamsErp = null;
        mvCompanies = new Vector<SDataCompany>();

        moServiceSessions = null;
        moServiceDataLocks = null;
        moDaemonSystemDatetime = null;
        moDaemonTimeoutSessions = null;
        moDaemonTimeoutDataLocks = null;
        moConnectionMonitor = null;

        startService();
    }

    /*
     * Private functions
     */

    private void displayCopyright() {
        System.out.println();
        System.out.println("**********************************************************************");
        System.out.println(" " + SClient.APP_NAME + " Server");
        System.out.println(" Release: " + SClient.APP_RELEASE);
        System.out.println(" Copyright (C)" + SClient.APP_COPYRIGHT + " " + SClient.APP_PROVIDER);
        System.out.println(" Todos los derechos reservados. All rights reserved.");
        System.out.println("**********************************************************************");
        System.out.println();
    }

    private void displayServerParams(final boolean addPrompt) {
        String dbms = "";
        String[] params = new String[4];

        switch (moParamsApp.getDatabaseType()) {
            case SLibConstants.DBMS_MY_SQL:
                dbms = "MySQL";
                break;
            case SLibConstants.DBMS_SQL_SERVER_2000:
                dbms = "MS SQL Server 2000";
                break;
            case SLibConstants.DBMS_SQL_SERVER_2005:
                dbms = "MS SQL Server 2005";
                break;
            default:
                dbms = "Unknown";
        }

        params[0] = "Server Parameters:" + (!addPrompt ? "" : "\n");
        params[1] = "- ERP instance name: " + moParamsApp.getErpInstance();
        params[2] = "- DBMS type: " + dbms;
        params[3] = "- DBMS host: " + moParamsApp.getDatabaseHostSrv() + ":" + moParamsApp.getDatabasePortSrv() + (!addPrompt ? "" : "\n");

        for (String param : params) {
            if (addPrompt) {
                renderMessage(param);
            }
            else {
                System.out.println(param);
            }
        }

        if (addPrompt) {
            renderMessageLn("End of Server Parameters");
        }
    }

    private void displayServerVersion(final boolean showPrompt) {
        String[] info = new String[3];

        info[0] = "Server Information:\n";
        info[1] = "- System: " + SClient.APP_NAME + " Server";
        info[2] = "- Release: " + SClient.APP_RELEASE + "\n";

        for (String s : info) {
            renderMessage(s);
        }

        if (showPrompt) {
            renderMessageLn("End of Server Information");
        }
        else {
            renderMessage("End of Server Information");
        }
    }

    private void displayServerJava(final boolean showPrompt) {
        String[] info = new String[2];

        info[0] = "Java Information:\n";
        info[1] = "- Version: " + System.getProperty("java.version") + "\n";

        for (String s : info) {
            renderMessage(s);
        }

        if (showPrompt) {
            renderMessageLn("End of Java Information");
        }
        else {
            renderMessage("End of Java Information");
        }
    }

    private boolean readParamsServer() {
        boolean read = false;

        moParamsApp = new SParamsApp();
        read = moParamsApp.read();

        if (read) {
            displayServerParams(false);
        }

        return read;
    }

    private boolean launchRmiRegistry() {
        boolean error = false;

        try {
            System.out.println("RMI Registry launching attempt...");
            rmiRegistry = java.rmi.registry.LocateRegistry.createRegistry(Integer.parseInt(moParamsApp.getErpRmiRegistryPort()));
            System.setSecurityManager(new RMISecurityManager());
        }
        catch (Exception e) {
            error = true;
            System.err.println(e);
        }

        return !error;
    }

    private boolean exportServer() {
        boolean exported = false;
        String name = "//localhost:" + moParamsApp.getErpRmiRegistryPort() + "/" + moParamsApp.getErpInstance();

        for (int i = 1; i <= 3 && !exported; i++) {
            try {
                System.out.println("Exportation attempt " + i + ":");
                System.out.println("- Name: [" + name + "]");
                Naming.rebind(name, this);
                exported = true;
            }
            catch (Exception e) {
                System.err.println(e);
            }
        }

        return exported;
    }

    private boolean readParamsErp() {
        int result = SLibConstants.UNDEFINED;

        try {
            moParamsErp = new SDataParamsErp();
            result = moParamsErp.read(new int[] { 1 }, moDatabase.getConnection().createStatement());
        }
        catch (Exception e) {
            System.err.println(e);
        }

        return result == SLibConstants.DB_ACTION_READ_OK;
    }

    private boolean readCompanies() {
        int result = SLibConstants.UNDEFINED;
        String sql = "";
        Statement st = null;
        Statement stAux = null;
        ResultSet rs = null;

        try {
            mvCompanies.removeAllElements();
            st = moDatabase.getConnection().createStatement();
            stAux = moDatabase.getConnection().createStatement();

            sql = "SELECT id_co, co, co_key FROM cfgu_co WHERE b_del=FALSE ORDER BY co ";
            rs = st.executeQuery(sql);
            while (rs.next()) {
                SDataCompany company = new SDataCompany();

                System.out.println("- Reading " + rs.getString("co") + "...");

                result = company.read(new int[] { rs.getInt("id_co") }, stAux);
                if (result != SLibConstants.DB_ACTION_READ_OK) {
                    System.out.println("- WARNING: " + rs.getString("co") + ", " + rs.getString("co_key") + " could not be read!");
                    break;
                }
                else {
                    mvCompanies.add(company);
                }
            }
        }
        catch (Exception e) {
            System.err.println(e);
        }

        if (mvCompanies.isEmpty()) {
            result = SLibConstants.DB_ACTION_READ_ERROR;
        }

        return result == SLibConstants.DB_ACTION_READ_OK;
    }

    private void startService() {
        Thread thread = null;

        try {
            displayCopyright();
            System.out.println("Starting Server...");

            System.out.println("Reading Server Parameters...");
            if (readParamsServer()) {
                System.out.println("Server Parameters read!");

                System.out.println("Launching RMI Registry...");
                if (launchRmiRegistry()) {
                    System.out.println("RMI Registry launched!...");

                    System.out.println("Exporting Server...");
                    if (exportServer()) {
                        System.out.println("Server exported!");

                        System.out.println("Stablishing database connection...");
                        moDatabase = new SDataDatabase(moParamsApp.getDatabaseType());
                        if (moDatabase.connect(moParamsApp.getDatabaseHostSrv(), moParamsApp.getDatabasePortSrv(),
                                moParamsApp.getDatabaseName(), moParamsApp.getDatabaseUser(), moParamsApp.getDatabasePswd()) == SLibConstants.DB_CONNECTION_OK) {
                            System.out.println("Database connection stablished!");

                            System.out.println("Reading ERP Parameters...");
                            if (readParamsErp()) {
                                System.out.println("ERP Parameters read!...");

                                System.out.println("Reading ERP companies...");
                                if (readCompanies()) {
                                    System.out.println("ERP companies read!...");

                                    System.out.println("Starting Server services...");

                                    System.out.println("- Starting sessions service...");
                                    moServiceSessions = new SServiceSessions(this);

                                    System.out.println("- Starting data locks service...");
                                    moServiceDataLocks = new SServiceDataLocks(this);

                                    System.out.println("- Starting system datetime daemon...");
                                    moDaemonSystemDatetime = new SDaemonSystemDatetime(this);
                                    moDaemonSystemDatetime.startDaemon();

                                    System.out.println("- Starting sessions timeout daemon...");
                                    moDaemonTimeoutSessions = new SDaemonTimeoutSessions(this);
                                    moDaemonTimeoutSessions.startDaemon();

                                    System.out.println("- Starting data locks timeout daemon...");
                                    moDaemonTimeoutDataLocks = new SDaemonTimeoutLocks(this);
                                    moDaemonTimeoutDataLocks.startDaemon();

                                    System.out.println("- Starting database connection monitor...");
                                    moConnectionMonitor = new SDataConnectionMonitor(moDatabase);
                                    moConnectionMonitor.startMonitor();

                                    System.out.println("- Starting command service...");
                                    mbIsActive = true;
                                    thread = new Thread(this);
                                    thread.start();

                                    System.out.println("Server services started!");

                                    System.out.print("Server started!");
                                    renderMessageLn("");
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            System.err.println(e);
        }
        finally {
            if (!mbIsActive) {
                System.err.println("WARNING: Server startup failed!");
                terminateServices();
                System.err.println("EXIT CODE: [-1]");
                try {
                    System.in.read();   // Wait for any user input before closing console window
                }
                catch (Exception e) {
                    System.err.println(e);
                }
                System.exit(-1);
            }
        }
    }

    private void terminateServices() {
        System.out.println("Stopping Server services...");

        // Daemons:

        System.out.println();
        System.out.println("- Stopping daemons...");

        if (moDaemonSystemDatetime != null) {
            moDaemonSystemDatetime.stopDaemon();
        }
        if (moDaemonTimeoutSessions != null) {
            moDaemonTimeoutSessions.stopDaemon();
        }
        if (moDaemonTimeoutDataLocks != null) {
            moDaemonTimeoutDataLocks.stopDaemon();
        }
        if (moConnectionMonitor != null) {
            moConnectionMonitor.stopMonitor();
        }

        // Services:

        System.out.println();
        System.out.println("- Stopping services...");

        if (moServiceDataLocks != null) {
            moServiceDataLocks.terminateService();
            System.out.println();
        }
        if (moServiceSessions != null) {
            moServiceSessions.terminateService();
            System.out.println();
        }

        // Database:

        System.out.println();
        System.out.println("- Disconnecting database...");

        if (moDatabase != null) {
            moDatabase.disconnect();
        }

        // RMI Registry process:

        System.out.println();
        System.out.println("- Destroying RMI Registry process...");

        if (rmiRegistry != null) {
            try {
                UnicastRemoteObject.unexportObject(rmiRegistry, true);
            }
            catch (Exception e) {
                System.err.println(e);
            }
        }

        System.out.println();
        System.out.println("Server services stopped!");
    }

    private void stopService() {
        System.out.println("Stopping Server...");

        mbIsActive = false;
        terminateServices();

        System.out.println("Server stopped!");
        System.err.println("EXIT CODE: [0]");
        System.exit(0);
    }

    private void processCommandParam(final String[] values) throws Exception {
        switch (values.length) {
            case 1:
                displayServerParams(true);
                break;
            default:
                throw new Exception(SSrvConsts.MSG_ERR_COMMAND_SYNTAX);
        }
    }

    private void processCommandSession(final String[] values) throws Exception {
        switch (values.length) {
            case 1:
                moServiceSessions.renderSessions();
                break;
            default:
                throw new Exception(SSrvConsts.MSG_ERR_COMMAND_SYNTAX);
        }
    }

    private void processCommandLock(final String[] values) throws Exception {
        switch (values.length) {
            case 1:
                moServiceDataLocks.renderLocks();
                break;
            case 2:
                if (values[1].compareToIgnoreCase("kill") == 0) {
                    moServiceDataLocks.terminateAllLocks(true);
                }
                else {
                    throw new Exception(SSrvConsts.MSG_ERR_COMMAND_SYNTAX);
                }
                break;
            case 3:
                if (values[1].compareToIgnoreCase("kill") == 0) {
                    moServiceDataLocks.terminateLocks(SLibUtilities.parseInt(values[2]), true);
                }
                else {
                    throw new Exception(SSrvConsts.MSG_ERR_COMMAND_SYNTAX);
                }
                break;
            default:
                throw new Exception(SSrvConsts.MSG_ERR_COMMAND_SYNTAX);
        }
    }

    private void processCommandVersion(final String[] values) throws Exception {
        switch (values.length) {
            case 1:
                displayServerVersion(true);
                break;
            default:
                throw new Exception(SSrvConsts.MSG_ERR_COMMAND_SYNTAX);
        }
    }

    private void processCommandJava(final String[] values) throws Exception {
        switch (values.length) {
            case 1:
                displayServerJava(true);
                break;
            default:
                throw new Exception(SSrvConsts.MSG_ERR_COMMAND_SYNTAX);
        }
    }

    private void processCommandHelp(final String[] values) throws Exception {
        switch (values.length) {
            case 1:
                renderMessage("Server Command's Help\n");
                renderMessage("- Command 'quit': stops this server.");
                renderMessage("   syntaxys: quit\n");
                renderMessage("- Command 'param': displays server parameters.");
                renderMessage("   syntaxys: param\n");
                renderMessage("- Command 'session': displays current server sessions.");
                renderMessage("   syntaxys: session\n");
                renderMessage("- Command 'lock': displays current data locks.");
                renderMessage("   syntaxys: lock [kill [session ID]]\n");
                renderMessage("- Command 'version': displays server information.");
                renderMessage("   syntaxys: version\n");
                renderMessage("- Command 'java': displays java information.");
                renderMessage("   syntaxys: version\n");
                renderMessage("- Command 'help': displays this help.");
                renderMessage("   syntaxys: help\n");
                renderMessageLn("End of Server Command's Help");
                break;
            default:
                throw new Exception(SSrvConsts.MSG_ERR_COMMAND_SYNTAX);
        }
    }

    private String[] parseCommand(final byte[] command) {
        int i = 0;
        int count = 1;
        int nextBlank = 0;
        String text = "";
        String[] values = null;

        for (i = 0; i < command.length && command[i] != 0 && command[i] != 10 && command[i] != 13; i++) {
            text += Character.toString((char) command[i]);
        }

        text = SLibUtilities.textTrim(text);

        for (i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                count++;
            }
        }

        i = 0;
        values = new String[count];

        while (true) {
            nextBlank = text.indexOf(' ');
            if (nextBlank == -1) {
                values[i] = text;
                break;
            }
            else {
                values[i++] = text.substring(0, nextBlank);
                text = text.substring(nextBlank + 1);
            }
        }

        return values;
    }

    /*
     * Public functions
     */

    public SimpleDateFormat getDatetimeFormat() { return moDatetimeFormat; }
    public Date getSystemDatetime() { return mtSystemDatetime; }
    public SDataDatabase getDatabase() { return moDatabase; }
    public SDataParamsErp getParamsErp() { return moParamsErp; }
    public SParamsApp getParamsApp() { return moParamsApp; }
    public SServiceSessions getServiceSessions() { return moServiceSessions; }
    public SServiceDataLocks getServiceDataLocks() { return moServiceDataLocks; }

    @Override
    public SLoginResponse login(final SLoginRequest request) throws RemoteException {
        renderMessageLn("Calling login()...");
        return moServiceSessions.login(request);
    }

    @Override
    public void logout(final int sessionId) throws RemoteException {
        renderMessageLn("Calling logout()...");
        moServiceSessions.logout(sessionId);
    }

    public String getCompanyDatabaseName(final int companyId) {
        String database = "";

        for (int i = 0; i < mvCompanies.size(); i++) {
            if (companyId == mvCompanies.get(i).getPkCompanyId()) {
                database = mvCompanies.get(i).getDatabase();
            }
        }

        return database;
    }

    public void updateDatetime() {
        mtSystemDatetime = new GregorianCalendar().getTime();
    }

    public void renderMessage(Object object) {
        System.out.print("\n " + object + "");
    }

    public void renderMessageLn(Object object) {
        System.out.print("\n " + object + "\n<" + moDatetimeFormat.format(mtSystemDatetime) + ">");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException {
        new SServer();
    }

    @Override
    public void run() {
        byte[] command = null;
        String[] values = null;

        while (mbIsActive) {
            try {
                command = new byte[255];
                System.in.read(command);
                values = parseCommand(command);

                if (values[0].compareToIgnoreCase("quit") == 0) {
                    stopService();
                }
                else if (values[0].compareToIgnoreCase("param") == 0) {
                    processCommandParam(values);
                }
                else if (values[0].compareToIgnoreCase("session") == 0) {
                    processCommandSession(values);
                }
                else if (values[0].compareToIgnoreCase("lock") == 0) {
                    processCommandLock(values);
                }
                else if (values[0].compareToIgnoreCase("version") == 0) {
                    processCommandVersion(values);
                }
                else if (values[0].compareToIgnoreCase("java") == 0) {
                    processCommandJava(values);
                }
                else if (values[0].compareToIgnoreCase("help") == 0) {
                    processCommandHelp(values);
                }
                else {
                    renderMessageLn(SSrvConsts.MSG_ERR_COMMAND_UNKNOWN);
                }
            }
            catch (Exception e) {
                renderMessageLn(e);
            }
        }
    }
}

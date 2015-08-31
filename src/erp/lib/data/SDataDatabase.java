/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.data;

import java.sql.DriverManager;
import java.sql.SQLException;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Statement;

/**
 *
 * @author Sergio Flores
 */
public final class SDataDatabase implements java.io.Serializable {
    
    private int mnType;
    private java.lang.String msHost;
    private java.lang.String msPort;
    private java.lang.String msDatabase;
    private java.lang.String msUser;
    private java.lang.String msPassword;
    private java.sql.Connection moConnection;

    private java.lang.String[] masUserSettings;

    private boolean mbIsConnectionStablished;
    private java.lang.String[] masSystemSettings;

    /**
     * @param type Constants defined in erp.lib.SLibConstants.
     */
    public SDataDatabase(int type) {
        mnType = type;
        reset();
    }

    private void reset() {
        msHost = "";
        msPort = "";
        msDatabase = "";
        msUser = "";
        msPassword = "";
        moConnection = null;

        masUserSettings = null;

        mbIsConnectionStablished = false;

        switch (mnType) {
            case SLibConstants.DBMS_MY_SQL:
                masSystemSettings = new String[] { "SET AUTOCOMMIT=1" };
                break;
            case SLibConstants.DBMS_SQL_SERVER_2000:
            case SLibConstants.DBMS_SQL_SERVER_2005:
                masSystemSettings = null;
                break;
            default:
                masSystemSettings = null;
        }
    }

    private int createConnection(java.lang.String host, java.lang.String port, java.lang.String database, java.lang.String user, java.lang.String password) {
        int result = SLibConstants.DB_CONNECTION_ERROR;
        Statement statement = null;

        try {
            switch (mnType) {
                case SLibConstants.DBMS_MY_SQL:
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    moConnection = DriverManager.getConnection("jdbc:mysql://" + host + (port.length() == 0 ? "" : ":" + port) + "/" + database + "?user=" + user + "&password=" + password);
                    statement = moConnection.createStatement();
                    break;

                case SLibConstants.DBMS_SQL_SERVER_2000:
                    Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver").newInstance();
                    moConnection = DriverManager.getConnection("jdbc:microsoft:sqlserver://" + host + (port.length() == 0 ? "" : ":" + port) + ";databaseName=" + database + ";user=" + user + ";password=" + password);
                    statement = moConnection.createStatement();
                    break;

                case SLibConstants.DBMS_SQL_SERVER_2005:
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
                    moConnection = DriverManager.getConnection("jdbc:sqlserver://" + host + (port.length() == 0 ? "" : ":" + port) + ";databaseName=" + database + ";user=" + user + ";password=" + password);
                    statement = moConnection.createStatement();
                    break;

                default:
            }

            if (masSystemSettings != null) {
                for (String setting : masSystemSettings) {
                    statement.execute(setting);
                }
            }

            if (masUserSettings != null) {
                for (String setting : masUserSettings) {
                    statement.execute(setting);
                }
            }

            result = SLibConstants.DB_CONNECTION_OK;
        }
        catch (ClassNotFoundException e) {
            SLibUtilities.printOutException(this, e);
        }
        catch (InstantiationException e) {
            SLibUtilities.printOutException(this, e);
        }
        catch (IllegalAccessException e) {
            SLibUtilities.printOutException(this, e);
        }
        catch (SQLException e) {
            SLibUtilities.printOutException(this, e);
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
        }

        return result;
    }

    public int getType() { return mnType; }
    public java.lang.String getHost() { return msHost; }
    public java.lang.String getPort() { return msPort; }
    public java.lang.String getDatabase() { return msDatabase; }
    public java.lang.String getUser() { return msUser; }
    public java.lang.String getPassword() { return msPassword; }
    public java.sql.Connection getConnection() { return moConnection; }

    public void setUserSettings(java.lang.String[] as) { masUserSettings = as; }

    public java.lang.String[] getUserSettings() { return masUserSettings; }
    
    public int connect(java.lang.String host, java.lang.String port, java.lang.String database, java.lang.String user, java.lang.String password) {
        int result = createConnection(host, port, database, user, password);
        
        if (result != SLibConstants.DB_CONNECTION_OK) {
            reset();
        }
        else {
            msHost = host;
            msPort = port;
            msDatabase = database;
            msUser = user;
            msPassword = password;
            mbIsConnectionStablished = true;
        }
        
        return result;
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                moConnection.close();
            }
            catch (SQLException e) {
                SLibUtilities.printOutException(this, e);
            }
            catch (Exception e) {
                SLibUtilities.printOutException(this, e);
            }
        }
    }

    public int reconnect() {
        int result = SLibConstants.DB_CONNECTION_ERROR;

        if (mbIsConnectionStablished) {
            result = createConnection(msHost, msPort, msDatabase, msUser, msPassword);
        }

        return result;
    }

    public boolean isConnected() {
        boolean connected = false;

        if (mbIsConnectionStablished && moConnection != null) {
            try {
                moConnection.createStatement().execute("SELECT 0");
                connected = true;
            }
            catch (SQLException e) {
                SLibUtilities.printOutException(this, e);
            }
            catch (Exception e) {
                SLibUtilities.printOutException(this, e);
            }
        }

        return connected;
    }

    public int checkConnection() {
        int result = SLibConstants.DB_CONNECTION_ERROR;

        if (mbIsConnectionStablished) {
            if (isConnected()) {
                result = SLibConstants.DB_CONNECTION_OK;
            }
            else {
                result = createConnection(msHost, msPort, msDatabase, msUser, msPassword);
            }
        }

        return result;
    }

    @Override
    public void finalize() throws Throwable {
        disconnect();
    }
}

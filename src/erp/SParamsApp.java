/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp;

import erp.lib.SLibUtilities;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;

/**
 *
 * @author Sergio Flores
 */
public class SParamsApp implements java.io.Serializable {

    public String FILE_NAME = "setup.swa";

    private String msErpInstance;
    private String msErpHost;
    private String msErpRmiRegistryPort;
    private int mnDatabaseType;
    private String msDatabaseHostSrv;
    private String msDatabaseHostClt;
    private String msDatabasePortSrv;
    private String msDatabasePortClt;
    private String msDatabaseName;
    private String msDatabaseUser;
    private String msDatabasePswd;
    private String msRedisHost;
    private String msRedisPswd;
    private String msTimeZone;
    private Date mtTimestamp;
    private boolean mbWithServer;
    
    public SParamsApp() {
        reset();
    }

    private void reset() {
        msErpInstance = "";
        msErpHost = "";
        msErpRmiRegistryPort = "";
        mnDatabaseType = SLibConsts.UNDEFINED;
        msDatabaseHostSrv = "";
        msDatabaseHostClt = "";
        msDatabasePortSrv = "";
        msDatabasePortClt = "";
        msDatabaseName = "";
        msDatabaseUser = "";
        msDatabasePswd = "";
        msRedisHost = "";
        msRedisPswd = "";
        msTimeZone = "";
        mtTimestamp = null;
        mbWithServer = false;
    }

    public void setErpInstance(String name) { msErpInstance = name; }
    public void setErpHost(String host) { msErpHost = host; }
    public void setErpRmiRegistryPort(String port) { msErpRmiRegistryPort = port; }
    public void setDatabaseType(int type) { mnDatabaseType = type; }
    public void setDatabaseHostSrv(String host) { msDatabaseHostSrv = host; }
    public void setDatabaseHostClt(String host) { msDatabaseHostClt = host; }
    public void setDatabasePortSrv(String port) { msDatabasePortSrv = port; }
    public void setDatabasePortClt(String port) { msDatabasePortClt = port; }
    public void setDatabaseName(String name) { msDatabaseName = name; }
    public void setDatabaseUser(String user) { msDatabaseUser = user; }
    public void setDatabasePswd(String pswd) { msDatabasePswd = pswd; }
    public void setRedisHost(String redisHost) { msRedisHost = redisHost; }
    public void setRedisPswd(String redisPswd) { msRedisPswd = redisPswd; }
    public void setTimeZone(String timeZone) { msTimeZone = timeZone; }
    public void setTimestamp(Date t) { mtTimestamp = t; }
    public void setWithSerever(boolean b) { mbWithServer = b; }

    public String getErpInstance() { return msErpInstance; }
    public String getErpHost() { return msErpHost; }
    public String getErpRmiRegistryPort() { return msErpRmiRegistryPort; }
    public int getDatabaseType() { return mnDatabaseType; }
    public String getDatabaseHostSrv() { return msDatabaseHostSrv; }
    public String getDatabaseHostClt() { return msDatabaseHostClt; }
    public String getDatabasePortSrv() { return msDatabasePortSrv; }
    public String getDatabasePortClt() { return msDatabasePortClt; }
    public String getDatabaseName() { return msDatabaseName; }
    public String getDatabaseUser() { return msDatabaseUser; }
    public String getDatabasePswd() { return msDatabasePswd; }
    public String getRedisHost() { return msRedisHost; }
    public String getRedisPswd() { return msRedisPswd; }
    public String getTimeZone() { return msTimeZone; }
    public Date getTimestamp() { return mtTimestamp; }
    public boolean getWithServer() { return mbWithServer; }

    public boolean read() {
        boolean error = false;
        File file = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        SParamsApp temp = null;

        reset();

        try {
            file = new File(FILE_NAME);
            if (!file.exists()) {
                error = true;
                String warning = "warning";
                String message = SClient.APP_NAME + " cofiguration file '" + FILE_NAME + "' could not be found in system's base folder!";
                System.err.println(warning.toUpperCase() + ": " + message);
                JOptionPane.showMessageDialog(null, message, SLibUtilities.textProperCase(warning) + "!", JOptionPane.WARNING_MESSAGE);
            }
            else {
                fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                temp = (SParamsApp) ois.readObject();
                ois.close();

                msErpInstance = temp.getErpInstance();
                msErpHost = temp.getErpHost();
                msErpRmiRegistryPort = temp.getErpRmiRegistryPort();
                mnDatabaseType = temp.getDatabaseType();
                msDatabaseHostSrv = temp.getDatabaseHostSrv();
                msDatabaseHostClt = temp.getDatabaseHostClt();
                msDatabasePortSrv = temp.getDatabasePortSrv();
                msDatabasePortClt = temp.getDatabasePortClt();
                msDatabaseName = temp.getDatabaseName();
                msDatabaseUser = temp.getDatabaseUser();
                msDatabasePswd = temp.getDatabasePswd();
                msRedisHost = temp.getRedisHost();
                msRedisPswd = temp.getRedisPswd();
                msTimeZone = temp.getTimeZone();
                mtTimestamp = temp.getTimestamp();
                mbWithServer = temp.getWithServer();
            }
        }
        catch (Exception e) {
            error = true;
            System.err.println(e);
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }

        return !error;
    }

    public boolean save() {
        boolean error = false;
        File file = null;
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            file = new File(FILE_NAME);
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);

            mtTimestamp = new java.util.Date();

            oos.writeObject(this);
            oos.flush();
            oos.close();
        }
        catch (Exception e) {
            error = true;
            System.err.println(e);
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }

        return !error;
    }
}

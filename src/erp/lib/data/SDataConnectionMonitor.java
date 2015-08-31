/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Sergio Flores
 */
public class SDataConnectionMonitor extends java.lang.Thread {

    private erp.lib.data.SDataDatabase moDatabase;
    private volatile boolean mbIsActive;

    public SDataConnectionMonitor(erp.lib.data.SDataDatabase database) {
        moDatabase = database;
    }

    public void startMonitor() {
        mbIsActive = true;
        setDaemon(true);
        start();
    }

    public void stopMonitor() {
        mbIsActive = false;
    }

    @Override
    public void run() {
        while (mbIsActive) {
            try {
                sleep(1000 * 60 * 15);  // 15 minutes

                if (moDatabase.checkConnection() != SLibConstants.DB_CONNECTION_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_CON);
                }
            }
            catch (InterruptedException e) {
                SLibUtilities.printOutException(this, e);
            }
            catch (Exception e) {
                SLibUtilities.printOutException(this, e);
            }
        }

        moDatabase.disconnect();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.client;

import erp.lib.SLibUtilities;

/**
 *
 * @author Sergio Flores
 */
public class SClientDaemonTimeout extends java.lang.Thread {

    private erp.client.SClientInterface miClient;
    private javax.swing.JLabel mjlLabel;
    private long mlTimeout;

    private volatile boolean mbIsActive;
    private java.util.Date mtTimestamp;

    public SClientDaemonTimeout(erp.client.SClientInterface client, javax.swing.JLabel label, long timeout) {
        miClient = client;
        mjlLabel = label;
        mlTimeout = timeout;

        mbIsActive = false;
        mtTimestamp = new java.util.Date();
    }

    public synchronized void startDaemon() {
        mbIsActive = true;
        setDaemon(true);
        start();
    }

    public synchronized void stopDaemon() {
        mbIsActive = false;
    }

    @Override
    public void run() {
        java.util.Date date = null;

        while (mbIsActive) {
            try {
                sleep(1000);    // 1 second

                date = new java.util.Date();
                mjlLabel.setText(miClient.getSessionXXX().getFormatters().getTimeFormat().format(date));
                if (date.getTime() - mtTimestamp.getTime() >= mlTimeout) {
                    mbIsActive = false;
                    miClient.showMsgBoxWarning("El tiempo de acceso exclusivo al registro ha expirado.");
                }
            }
            catch (InterruptedException e) {
                SLibUtilities.renderException(this, e);
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
    }
}

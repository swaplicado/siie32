/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.server;

/**
 *
 * @author Sergio Flores
 */
public class SDaemonTimeoutLocks extends Thread {

    private SServer moServer;
    private volatile boolean mbIsActive;

    public SDaemonTimeoutLocks(SServer server) {
        moServer = server;
        mbIsActive = false;
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
        while (mbIsActive) {
            try {
                sleep(1000 * 60 * 1);   // 1 minute

                moServer.getServiceDataLocks().evaluateTimeouts();
            }
            catch (InterruptedException e) {
                moServer.renderMessageLn(e);
            }
            catch (Exception e) {
                moServer.renderMessageLn(e);
            }
        }
    }
}

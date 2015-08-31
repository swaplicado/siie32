/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.util.imp;

import erp.lib.SLibUtilities;

/**
 *
 * @author Sergio Flores
 */
public class ImportProgressThread extends java.lang.Thread {

    private volatile boolean mbIsActive;
    private javax.swing.JFrame moFrame;

    public ImportProgressThread(javax.swing.JFrame frame) {
        moFrame = frame;
    }

    public void startThread() {
        mbIsActive = true;
        start();
    }

    public void stopThread() {
        mbIsActive = false;
    }

    @Override
    public void run() {
        while (mbIsActive) {
            try {
                Thread.sleep(1000 * 5);
                moFrame.validate();
                //System.out.println("run()...");
            }
            catch (Exception e) {
                SLibUtilities.printOutException(this, e);
            }
        }
    }

}

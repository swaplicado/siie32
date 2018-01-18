/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.cfd.SDialogResult;
import erp.client.SClientInterface;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas, Sergio Flores
 * 
 * Maintenance Log:
 * 2018-01-12, Sergio Flores:
 *  Implementation of printing payroll into CFDI 3.3.
 */
public class SCfdPrintThread extends Thread {

    protected SClientInterface miClient;
    protected SDataCfd moCfd;
    protected int mnCfdSubtype;
    protected int mnPrintMode;
    protected int mnNumberCopies;
    protected SDialogResult moDialogResult;
    
    public SCfdPrintThread(final SClientInterface client, final SDataCfd cfd, final int cfdSubtype, final int printMode, final int numberCopies, final SDialogResult dialogResult) {
        miClient = client;
        moCfd = cfd;
        mnCfdSubtype = cfdSubtype;
        mnPrintMode = printMode;
        mnNumberCopies = numberCopies;
        moDialogResult = dialogResult;
    }

    public void startThread() {
        setDaemon(true);
        super.start();
    }

    public void stopThread() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void start() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void run() {
        try {
            SCfdUtils.printCfd(miClient, moCfd, mnCfdSubtype, mnPrintMode, mnNumberCopies, false);
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
}

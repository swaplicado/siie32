/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.print;

import java.io.*;
import erp.lib.*;
import erp.lib.print.*;

/**
 *
 * @author Sergio Flores
 */
public class SPrintDocPos {

    // private erp.data.DDataDoc moDoc;
    private erp.client.SClientInterface miClient;

    public SPrintDocPos(erp.client.SClientInterface client, int docType, int[] key) {
        miClient = client;
        // moDoc = new DDataDoc(client, null, docType, true);
        /* try {
            if (moDoc.read(key, miClient.getStatement()) != DLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(DLibConstants.DB_MSG_REGISTRY_NOT_FOUND);
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        } */
    }

    // public void setDoc(erp.data.DDataDoc o) { moDoc = o; }

    // public erp.data.DDataDoc getDoc() { return moDoc; }

    @SuppressWarnings("empty-statement")
    public void print() {
        int i;
        int width = 40;
        double[] rates;
        java.lang.String sBlankRow = SLibUtilities.textRepeat(" ", width);
        java.lang.String sSeparator = SLibUtilities.textRepeat("-", width);
        java.lang.String sTotalTab = SLibUtilities.textRepeat(" ", 15);
        // erp.data.DDataDocDetail docDetail;
        // erp.data.DDataDocValue docValue = new DDataDocValue(miClient);

        // docValue.setDoc(moDoc);

        try {

            java.io.FileOutputStream os = new FileOutputStream("LPT1");
            //java.io.FileOutputStream os = new FileOutputStream("C:\\temp\\ticket.txt");
            java.io.PrintStream ps = new PrintStream(os);


            ps.println(SPrintUtilities.formatText("HOLA MUNDO", width, SLibConstants.ALIGN_CENTER, SLibConstants.TRUNC_MODE_TRUNC));
	    ps.print(" ");
            //ps.println("" + ((char) (0x1B)) + "i");
            ps.close();
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    public void initPort() {

    }
}

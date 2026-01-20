/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cli;

import erp.SParamsApp;
import erp.mfin.utils.SXrtsHandler;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;

/**
 * Handle Exchange Rates.
 * To obtain exchange rates from RSS official sources, and register them in all company databases.
 * USD exchange rate policy will be extended to EUR exchange rate as well.
 * 
 * This class is pretended to be used from CLI, just as "java -classpath dist/siie32.jar erp.cli.SCliHandleXrts".
 * 
 * @author Sergio Flores
 */
public class SCliHandleXrts {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            SParamsApp paramsApp = new SParamsApp();

            if (!paramsApp.read()) {
                throw new Exception(erp.SClient.ERR_PARAMS_APP_READING);
            }

            //conect to erp database
            SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);
            
            int result = database.connect(
                    paramsApp.getDatabaseHostClt(), 
                    paramsApp.getDatabasePortClt(),
                    paramsApp.getDatabaseName(), 
                    paramsApp.getDatabaseUser(), 
                    paramsApp.getDatabasePswd());

            if (result != SDbConsts.CONNECTION_OK) {
                throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION);
            }
            
            new SXrtsHandler(database.getConnection()).handleXrts();
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }
}

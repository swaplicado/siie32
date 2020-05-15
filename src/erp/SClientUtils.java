/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp;

import java.sql.Connection;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;

/**
 *
 * @author Sergio Flores
 */
public abstract class SClientUtils {
    
    /**
     * Create a connection to given schema (database name.)
     * @param schema Schema (database name.)
     * @return <code>Connection</code>.
     * @throws java.lang.Exception
     */
    public static Connection createConnection(final String schema) throws Exception {
        Connection connection = null;
        
        SParamsApp paramsApp = new SParamsApp();
        if (!paramsApp.read()) {
            throw new Exception(SClient.ERR_PARAMS_APP_READING);
        }
        else {
            SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);
            int result = database.connect(paramsApp.getDatabaseHostClt(), paramsApp.getDatabasePortClt(),
                    schema, paramsApp.getDatabaseUser(), paramsApp.getDatabasePswd());

            if (result != SDbConsts.CONNECTION_OK) {
                throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION);
            }
            else {
                connection = database.getConnection();
            }
        }
        
        return connection;
    }
}

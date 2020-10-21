/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp;

import erp.client.SClientInterface;
import java.sql.Connection;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;

/**
 *
 * @author Sergio Flores, Isabel Servín
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
    
    /**
     * Devuelve el nombre de la base de datos complementaría a partir de una conexión a base de datos.
     * @param connection Conexión a base de datos.
     * @return String.
     * @throws java.lang.Exception
     */
    public static String getComplementaryDbName(Connection connection) throws Exception {
        return connection.getCatalog() + "_cfd";
    }
    
    /**
     * Devuelve el nombre de la base de datos complementaria a partir de una interfaz de cliente.
     * @param client Interfaz de cliente.
     * @return String.
     * @throws java.lang.Exception
     */
    public static String getComplementaryDdName(SClientInterface client) throws Exception {
        return getComplementaryDbName(client.getSession().getStatement().getConnection());
    }
}

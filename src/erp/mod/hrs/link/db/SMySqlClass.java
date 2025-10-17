/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edwin Carmona
 */
public class SMySqlClass {
    
    public SMySqlClass(String json) throws SConfigException {
        if (json == null || json.isEmpty()) {
            throw new SConfigException("No se recibió archivo JSON de configuración");
        }

        try {
            ObjectMapper mapper = new ObjectMapper();

            SDbConnection oDbConn = mapper.readValue(json, SDbConnection.class);

            this.gserverHost = oDbConn.getDbHost();
            this.gdbPort = oDbConn.getDbPort();
            this.gdb = oDbConn.getDbName();
            this.guser = oDbConn.getDbUser();
            this.gpass = oDbConn.getDbPass();
            this.gmaindb = oDbConn.getDbMainId();
        }
        catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(SMySqlClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (this.gmaindb <= 0) {
            // Imprimir stack trace
            Logger.getLogger(SMySqlClass.class.getName()).log(Level.SEVERE, "No existe la base de datos principal en la configuración recibida.");
            throw new SConfigException("No existe la base de datos principal en la configuración recibida.");
        }
    }

    public String gdbPort;
    public String gserverHost;
    public String gdb;
    public String guser;
    public String gpass;
    public int gmaindb;

    // Declarar una conexión estática para mantener la conexión activa
    private static Connection conn = null;
    private static String currentHost = "";
    private static String currentPort = "";
    private static String currentDb = "";
    private static String currentUser = "";
    private static String currentPass = "";

    /**
     * Establece una conexión a la base de datos si no existe o si los
     * parámetros cambian.
     *
     * @param serverHost Host del servidor de la base de datos.
     * @param dbPort Puerto del servidor de la base de datos.
     * @param db Nombre de la base de datos.
     * @param user Usuario para la conexión.
     * @param pass Contraseña para la conexión.
     * @return Objeto Connection activo.
     * @throws ClassNotFoundException Si no se encuentra el controlador MySQL.
     * @throws SQLException Si ocurre un error al conectar a la base de datos.
     */
    public Connection connect(String serverHost, String dbPort, String db, String user, String pass)
            throws ClassNotFoundException, SQLException {
        // Usar valores predeterminados si los parámetros están vacíos
        serverHost = serverHost.isEmpty() ? gserverHost : serverHost;
        dbPort = dbPort.isEmpty() ? gdbPort : dbPort;
        db = db.isEmpty() ? gdb : db;
        user = user.isEmpty() ? guser : user;
        pass = pass.isEmpty() ? gpass : pass;

        // Verificar si ya existe una conexión activa con los mismos parámetros
        if (conn != null && !conn.isClosed()
                && serverHost.equals(currentHost)
                && dbPort.equals(currentPort)
                && db.equals(currentDb)
                && user.equals(currentUser)
                && pass.equals(currentPass)) {
            System.out.println("Reutilizando conexión existente...");
            return conn;
        }

        // Actualizar los parámetros actuales
        currentHost = serverHost;
        currentPort = dbPort;
        currentDb = db;
        currentUser = user;
        currentPass = pass;

        // Cerrar conexión anterior si está activa
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }

        // Crear una nueva conexión
        String ruta = "jdbc:mysql://";
        String servidor = serverHost + ":" + dbPort + "/";

        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(ruta + servidor + db, user, pass);

        if (conn != null) {
            System.out.println("Conexión a base de datos lista...");
        }

        return conn;
    }

    public int getMainBb() {
        return gmaindb;
    }

    public String getMainDatabaseName(final int idCo) throws ClassNotFoundException {
        String query = "SELECT "
                + "    bd "
                + "FROM "
                + "    erp.cfgu_co "
                + "WHERE ";
                if (idCo > 0) {
                    query += "    id_co = " + idCo + ";";
                }
                else if (this.getMainBb() > 0) {
                    query += "    id_co = " + this.getMainBb() + ";";
                }
                else {
                    query += "    id_co = " + 2852 + ";";
                    Logger.getLogger(SMySqlClass.class.getName()).log(Level.WARNING, "id_co = " + 2852 + " por defecto.");
                }

        Statement st;
        try {
            String ruta = "jdbc:mysql://";
            if (this.gserverHost == null || this.gserverHost.isEmpty()) {
                return null;
            }
            String servidor = this.gserverHost + ":" + this.gdbPort + "/";

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(ruta + servidor + this.gdb, this.guser, this.gpass);
            st = conn.createStatement();
            Logger.getLogger(SMySqlClass.class.getName()).log(Level.INFO, "getMainDatabaseName({0}) : {1}", new Object[]{idCo, query});
            ResultSet res = st.executeQuery(query);

            String name = "";
            if (res.next()) {
                name = res.getString("bd");
            }

            conn.close();
            st.close();
            res.close();

            return name;
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(SMySqlClass.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}

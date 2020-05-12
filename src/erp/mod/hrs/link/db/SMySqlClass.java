/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Edwin Carmona
 */
public class SMySqlClass {
    
    public static String jsonConn;

    public SMySqlClass() throws SConfigException {
        if (jsonConn == null || jsonConn.isEmpty()) {
            throw new SConfigException("No se recibió archivo JSON de configuración");
        }
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            
            SDbConnection conn = mapper.readValue(jsonConn, SDbConnection.class);
            
            this.gserverHost = conn.getDbHost();
            this.gdbPort = conn.getDbPort();
            this.gdb = conn.getDbName();
            this.guser = conn.getDbUser();
            this.gpass = conn.getDbPass();
        }
        catch (IOException ex) {
            Logger.getLogger(SMySqlClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String gdbPort; 
    public String gserverHost; 
    public String gdb; 
    public String guser; 
    public String gpass; 
    Connection conn = null;
    
    public Connection connect(String serverHost, String dbPort, String db, String user, String pass) throws ClassNotFoundException, SQLException {
        serverHost = serverHost.isEmpty() ? gserverHost : serverHost;
        dbPort = dbPort.isEmpty() ? gdbPort : dbPort;
        db = db.isEmpty() ? gdb : db;
        user = user.isEmpty() ? guser : user;
        pass = pass.isEmpty() ? gpass : pass;
        
        String ruta = "jdbc:mysql://"; 
        String servidor = serverHost + ":" + dbPort + "/";

        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(ruta + servidor + db, user, pass); 
        if (conn != null){ 

            System.out.println("Conexión a base de datos lista..."); 
            return conn;
        } 

        return null;
    }

    public static String getJsonConn() {
        return jsonConn;
    }

    public static void setJsonConn(String jsonConn) {
        SMySqlClass.jsonConn = jsonConn;
    }
}

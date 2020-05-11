/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edwin Carmona
 */
public class SMySqlClass {
    
    public String gdbPort = "3306"; 
    public String gserverHost = "192.168.1.233"; 
    public String gdb = "erp"; 
    public String guser = "root"; 
    public String gpass = "msroot"; 
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
}

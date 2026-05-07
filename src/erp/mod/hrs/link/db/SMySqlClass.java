package erp.mod.hrs.link.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SMySqlClass {
    public static String jsonConn;

    public String gdbPort;

    public String gserverHost;

    public String gdb;

    public String guser;

    public String gpass;

    public int gmaindb;
  
    public SMySqlClass() throws SConfigException {
        if (jsonConn == null || jsonConn.isEmpty())
            throw new SConfigException("No se recibiarchivo JSON de configuración"); 
        try {
            ObjectMapper mapper = new ObjectMapper();
            SDbConnection conn = (SDbConnection)mapper.readValue(jsonConn, SDbConnection.class);
            this.gserverHost = conn.getDbHost();
            this.gdbPort = conn.getDbPort();
            this.gdb = conn.getDbName();
            this.guser = conn.getDbUser();
            this.gpass = conn.getDbPass();
            this.gmaindb = conn.getDbMainId();
        } catch (IOException ex) {
            Logger.getLogger(SMySqlClass.class.getName()).log(Level.SEVERE, (String)null, ex);
        } 
    }
  
    Connection conn = null;
  
    public Connection connect(String serverHost, String dbPort, String db, String user, String pass) throws ClassNotFoundException, SQLException {
        serverHost = serverHost.isEmpty() ? this.gserverHost : serverHost;
        dbPort = dbPort.isEmpty() ? this.gdbPort : dbPort;
        db = db.isEmpty() ? this.gdb : db;
        user = user.isEmpty() ? this.guser : user;
        pass = pass.isEmpty() ? this.gpass : pass;
        String ruta = "jdbc:mysql://";
        String servidor = serverHost + ":" + dbPort + "/";
        Class.forName("com.mysql.jdbc.Driver");
        this.conn = DriverManager.getConnection(ruta + servidor + db, user, pass);
        if (this.conn != null) {
            System.out.println("Conexia base de datos lista...");
            return this.conn;
        } 
            return null;
    }

    public static String getJsonConn() {
        return jsonConn;
    }

    public static void setJsonConn(String jsonConn) {
        SMySqlClass.jsonConn = jsonConn;
    }

    public int getMainBb() {
        return this.gmaindb;
    }
  }
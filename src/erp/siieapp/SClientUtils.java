/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp;

import erp.mod.SModConsts;
import erp.mod.SModUtils;
import erp.mod.SModuleHrs;
import erp.mod.hrs.link.db.SConfigException;
import erp.mod.hrs.link.db.SMySqlClass;
import erp.musr.data.SDataUser;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import org.json.simple.parser.ParseException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiDatePicker;
import sa.lib.gui.SGuiDateRangePicker;
import sa.lib.gui.SGuiSession;
import sa.lib.gui.SGuiUserGui;
import sa.lib.gui.SGuiYearMonthPicker;
import sa.lib.gui.SGuiYearPicker;

/**
 *
 * @author AdrianAviles
 */
public class SClientUtils {

    private int userId;

    public SClientUtils(int userId) {
        this.userId = userId;
    }

    public SClientUtils() {
        this.userId = SUtilConsts.USR_NA_ID;
    }

    public SGuiSession setSession(String sjon) throws SConfigException, ClassNotFoundException, SQLException, ParseException {
        SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);
        ResultSet resultSet = null;
        SMySqlClass.setJsonConn(sjon);
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", "", "", "");

        if (conn == null) {

        }

        String companies = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_CO) + " "
                + "WHERE id_co = " + mdb.getMainBb();
        Statement stCon = conn.createStatement();
        resultSet = stCon.executeQuery(companies);

        if (!resultSet.next()) {

        }

        database.connect(
                mdb.gserverHost, // agregar esta constante a la configuración de AppLink
                mdb.gdbPort, // agregar esta constante a la configuración de AppLink
                resultSet.getString("bd"), // agregar esta constante a la configuración de AppLink
                mdb.guser, // agregar esta constante a la configuración de AppLink
                mdb.gpass); // agregar esta constante a la configuración de AppLink
        SGuiSession session = new SGuiSession(null);
//        JFrame frame = new JFrame();
        SGuiClient client = new SGuiClient() {
            @Override
            public JFrame getFrame() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public JTabbedPane getTabbedPane() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public SDbDatabase getSysDatabase() {
                return database;
            }

            @Override
            public Statement getSysStatement() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public SGuiSession getSession() {
                return session;
            }

            @Override
            public SGuiDatePicker getDatePicker() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public SGuiDateRangePicker getDateRangePicker() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public SGuiYearPicker getYearPicker() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public SGuiYearMonthPicker getYearMonthPicker() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public JFileChooser getFileChooser() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public ImageIcon getImageIcon(int icon) {
                return null;
            }

            @Override
            public SGuiUserGui readUserGui(int[] key) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public SGuiUserGui saveUserGui(int[] key, String gui) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public HashMap<String, Object> createReportParams() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getTableCompany() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getTableUser() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getAppName() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getAppRelease() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getAppCopyright() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getAppProvider() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void computeSessionSettings() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void preserveSessionSettings() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void showMsgBoxError(String msg) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void showMsgBoxWarning(String msg) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void showMsgBoxInformation(String msg) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public int showMsgBoxConfirm(String msg) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public Object getLockManager() {
                return null;
            }
        };

        session.setClient(client);
        SDataUser user = new SDataUser();
        user.setPkUserId(userId); // agregar esta constante a la configuración de AppLink
        session.setUser(user);
        Date now = new Date();
        session.setSystemDate(now);
        session.setUserTs(now);
        session.setDatabase(database);
        session.setModuleUtils(new SModUtils());
        session.getModules().add(new SModuleHrs(session.getClient()));

        return session;
    }
}

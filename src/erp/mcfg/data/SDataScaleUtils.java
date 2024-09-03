/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mcfg.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiItem;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public abstract class SDataScaleUtils {
    
    /**
     * Devuelve la conección a la báscula a través de su pk 
     * @param session
     * @param pk
     * @return connection o null
     */
    public static Connection getScaleConnection(SGuiSession session, int pk) {
        Connection connection = null;
        String host = "";
        String port = "";
        String db = "";
        String driver = "";
        String user = "";
        String pass = "";
        int typeDbms = 0;
        
        try {
            String sql = "SELECT * FROM erp.cfgu_sca WHERE id_sca = " + pk;
            ResultSet resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                driver = resultSet.getString("dbms_driver");
                host = resultSet.getString("host");
                port = resultSet.getString("port");
                db = resultSet.getString("db_name");
                typeDbms = resultSet.getInt("fid_tp_dbms");
                user = resultSet.getString("user_name");
                pass = resultSet.getString("user_pswd");
            } 
            String url = "";
            Class.forName(driver).newInstance();

            switch (typeDbms){
                case SDbConsts.DBMS_SYBASE: 
                    url = "jdbc:sybase:Tds:" + host + ":" +
                            port + "/" + 
                            db;
                    break;
                case SDbConsts.DBMS_MYSQL:
                case SDbConsts.DBMS_SQL_SERVER:
                default:
                    throw new UnsupportedOperationException("Not supported yet.");
            }
            
            Properties properties = new Properties();
            properties.put("user", user);
            properties.put("password", pass);

            connection = DriverManager.getConnection(url, properties);
        }
        catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            SLibUtils.printException(SDataScaleUtils.class.getName(), e);
        }
        
        return connection;
    }
        
    /**
     * Devuelve la parte de la consulta que hace la validación de configuración de conección a una base de datos externa.
     * @return dbms_driver <> '' AND host <> '' AND port <> '' (...)
     */
    public static String getSqlWhereScalesWithConnection() {
        return "dbms_driver <> '' AND host <> '' AND port <> '' AND user_name <> '' AND user_pswd <> '' AND db_name <> '' ";
    }
    
    /**
     * Devuelve una lista con los bizPartner mapeados para el asoc. de negocio de la báscula. 
     * @param session
     * @param idSca
     * @param codeSca
     * @return ArrayList
     */
    public static ArrayList<SGuiItem> getBizPartnerScaleMap(SGuiSession session, int idSca, String codeSca) {
        ArrayList<SGuiItem> arr = new ArrayList<>();
        SGuiItem opc = new SGuiItem(new int[] { 0 }, "Seleccione una opción", false);
        arr.add(opc);
        try {
            String sql = "SELECT bp.id_bp, bp.bp, sbm.b_def FROM erp.bpsu_sca_bp_map sbm "
                    + "INNER JOIN erp.bpsu_sca_bp sb ON sbm.id_sca = sb.id_sca AND sbm.id_sca_bp = sb.id_sca_bp "
                    + "INNER JOIN erp.bpsu_bp bp ON sbm.id_bp = bp.id_bp "
                    + "WHERE sb.id_sca = " + idSca + " AND sb.code = '" + codeSca + "'";
            ResultSet resultSet = session.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                SGuiItem item = new SGuiItem(new int[] { resultSet.getInt(1) }, resultSet.getString(2), resultSet.getBoolean(3));
                arr.add(item);
            }
        }
        catch (Exception e) {
            SLibUtils.printException(SDataScaleUtils.class.getName(), e);
        }
        return arr;
    }
    
    /**
     * Devuelve una lista con los SDataItem mapeados para el ítem de la báscula. 
     * @param session
     * @param idSca
     * @param codeSca
     * @return ArrayList
     */
    public static ArrayList<SGuiItem> getItemScaleMap(SGuiSession session, int idSca, String codeSca) {
        ArrayList<SGuiItem> arr = new ArrayList<>();
        SGuiItem opc = new SGuiItem(new int[] { 0 }, "Seleccione una opción", false);
        arr.add(opc);
        try {
            String sql = "SELECT i.id_item, i.item, sim.b_def FROM erp.itmu_sca_item_map sim "
                    + "INNER JOIN erp.itmu_sca_item si ON sim.id_sca = si.id_sca AND sim.id_sca_item = si.id_sca_item "
                    + "INNER JOIN erp.itmu_item i ON sim.id_item = i.id_item "
                    + "WHERE si.id_sca = " + idSca + " AND si.code = '" + codeSca + "'";
            ResultSet resultSet = session.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                SGuiItem item = new SGuiItem(new int[] { resultSet.getInt(1) }, resultSet.getString(2), resultSet.getBoolean(3));
                arr.add(item);
            }
        }
        catch (Exception e) {
            SLibUtils.printException(SDataScaleUtils.class.getName(), e);
        }
        return arr;
    }
}

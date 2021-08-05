/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.utils;

import erp.mod.hrs.link.db.SConfigException;
import erp.mod.hrs.link.db.SMySqlClass;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edwin Carmona
 */
public class SEmployeesUtils {
    
    /**
     * Obtiene los empleados subordinados que correspondan al id del empleado recibido
     * 
     * @param head entero correspondiente al id del empleado "jefe"
     * 
     * @return ArrayList<Integer> de los ids de los empleados subordinados
     * 
     * @throws SConfigException
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public ArrayList<Integer> getEmployeesOfHead(int head) throws SConfigException, ClassNotFoundException, SQLException {
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", "", "", "");

        if (conn == null) {
            return null;
        }
        
        ArrayList<Integer> depts = this.getDeptsOfHead(head);
        
        String sDepts = "";
        for (Integer dept : depts) {
            sDepts += dept + ",";
        }
        
        if (sDepts.isEmpty()) {
            return new ArrayList<>();
        }
        
        sDepts = sDepts.substring(0, sDepts.length() - 1);

        String query = "SELECT " +
                        "    id_emp " +
                        "FROM " +
                        "    erp.hrsu_emp " +
                        "WHERE " +
                        "    fk_dep IN (" + sDepts + ");";

        ArrayList<Integer> lEmps = null;
        try {
            Statement st = conn.createStatement();
            
            ResultSet res = st.executeQuery(query);
            
            lEmps = new ArrayList();
            while (res.next()) {
                lEmps.add(res.getInt("id_emp"));
            }

            conn.close();
            st.close();
            res.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(SEmployeesUtils.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList();
        }

        return lEmps;
    }
    
    /**
     * Obtiene la lista de ids de departamentos al que el empleado tiene acceso
     * 
     * @param head
     * 
     * @return
     * @throws SConfigException
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    private ArrayList<Integer> getDeptsOfHead(int head) throws SConfigException, ClassNotFoundException, SQLException {
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", "", "", "");

        if (conn == null) {
            return null;
        }

        String query = "SELECT id_dep FROM erp.hrsu_dep WHERE fk_emp_head_n = " + head + ";";

        ArrayList<Integer> lDepts = null;
        
        try {
            Statement st = conn.createStatement();
            
            ResultSet res = st.executeQuery(query);
            
            lDepts = new ArrayList();
            while (res.next()) {
                lDepts.add(res.getInt("id_dep"));
            }

            conn.close();
            st.close();
            res.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(SEmployeesUtils.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList();
        }
        
        ArrayList<Integer> lAllDepts = new ArrayList();
        for (Integer dept : lDepts) {
            lAllDepts.addAll(this.getDeptsOfDept(dept));
        }
        
        lAllDepts.addAll(lDepts);

        return lAllDepts;
    }
    
    /**
     * 
     * @param dept
     * @return
     * @throws SConfigException
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    private ArrayList<Integer> getDeptsOfDept(int dept) throws SConfigException, ClassNotFoundException, SQLException {
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", "", "", "");

        if (conn == null) {
            return null;
        }

        String query = "SELECT " +
                        "    id_dep " +
                        "FROM " +
                        "    erp.hrsu_dep " +
                        "WHERE " +
                        "    fk_dep_sup_n = " + dept + ";";

        ArrayList<Integer> lDepts = null;
        
        try {
            Statement st = conn.createStatement();
            
            ResultSet res = st.executeQuery(query);
            
            lDepts = new ArrayList();
            while (res.next()) {
                lDepts.add(res.getInt("id_dep"));
            }

            conn.close();
            st.close();
            res.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(SEmployeesUtils.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList();
        }
        
        ArrayList<Integer> depts = new ArrayList<>();
        for (Integer iDept : lDepts) {
            depts.addAll(this.getDeptsOfDept(iDept));
        }
        
        depts.addAll(lDepts);

        return depts;
    }
    
    
}

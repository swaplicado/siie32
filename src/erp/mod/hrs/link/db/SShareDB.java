/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edwin Carmona
 */
public class SShareDB {

    private ArrayList<SCompany> getDatabasesWithPayroll() throws SQLException, ClassNotFoundException, SConfigException {
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", "", "", "");
        if (conn == null) {
            return null;
        }

        String query = "SELECT "
                + "    id_co, "
                + "    co, "
                + "    bd "
                + "FROM "
                + "    erp.cfgu_co "
                + "WHERE "
                + "    b_del = FALSE AND b_mod_hrs = TRUE;";

        ArrayList<SCompany> lBds = null;

        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(query);
            
            SCompany comp = null;
            lBds = new ArrayList();
            while (res.next()) {
                comp = new SCompany();
                comp.id_company = res.getInt("id_co");
                comp.company = res.getString("co");
                comp.database_nm = res.getString("bd");
                
                lBds.add(comp);
            }

            conn.close();
            st.close();
            res.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lBds;
    }

    private String getMainDatabase() throws SQLException, ClassNotFoundException, SConfigException {
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", "", "", "");
        if (conn == null) {
            return "";
        }

        int idDataBase = 1211;

        String query = "SELECT "
                + "    bd "
                + "FROM "
                + "    erp.cfgu_co "
                + "WHERE "
                + "    id_co = " + idDataBase + ";";
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(query);

            if (res.next()) {
                return res.getString("bd");
            }

            conn.close();
            st.close();
            res.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }
    
    public ArrayList<SDepartment> getDepartments(String strDate) throws SQLException, ClassNotFoundException, SConfigException {
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", "", "", "");

        if (conn == null) {
            return null;
        }
        
        String query = "SELECT * FROM erp.hrsu_dep "
                        + "WHERE "
                        + "    (ts_usr_ins >= '" + strDate + "' "
                        + "        OR ts_usr_upd >= '" + strDate + "')";
        
        ArrayList<SDepartment> lDepts = null;

        
        Statement st = conn.createStatement();
        ResultSet res = st.executeQuery(query);

        lDepts = new ArrayList();
        SDepartment dept = null;
        while (res.next()) {
            dept = new SDepartment();

            dept.id_department = res.getInt("id_dep");
            dept.dept_code = res.getString("code");
            dept.dept_name = res.getString("name");
            dept.is_deleted = res.getBoolean("b_del");
            dept.is_system = res.getBoolean("b_sys");

            lDepts.add(dept);
        }

        conn.close();
        st.close();
        res.close();

        return lDepts;
    }

    /**
     *
     * @param strDate
     *
     * @return
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public ArrayList<SEmployee> getEmployees(String strDate) throws SQLException, ClassNotFoundException, SConfigException {
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", "", "", "");

        if (conn == null) {
            return null;
        }

        String query = "SELECT  "
                + "    e.id_emp, "
                + "    e.num, "
                + "    e.lastname1, "
                + "    e.lastname2, "
                + "    bp.bp, "
                + "    bp.lastname, "
                + "    bp.firstname, "
                + "    e.dt_ben, "
                + "    e.dt_hire, "
                + "    e.dt_dis_n, "
                + "    e.b_uni AS x_time, "
                + "    e.fk_tp_pay, "
                + "    e.fk_dep, "
                + "    e.b_act, "
                + "    e.b_del "
                + "FROM "
                + "    erp.hrsu_emp e "
                + "        INNER JOIN "
                + "    erp.bpsu_bp bp ON e.id_emp = bp.id_bp "
                + "WHERE "
                + "    (ts_usr_ins >= '" + strDate + "' "
                + "        OR ts_usr_upd >= '" + strDate + "')"
                + "     AND bp.b_att_emp;";

        ArrayList<SEmployee> lEmps = null;

        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(query);

            lEmps = new ArrayList();
            SEmployee emp = null;
            while (res.next()) {
                emp = new SEmployee();

                emp.id_employee = res.getInt("id_emp");
                emp.num_employee = res.getInt("num");
                emp.lastname = res.getString("lastname");
                emp.firstname = res.getString("firstname");
                emp.admission_date = res.getString("dt_hire");
                emp.leave_date = res.getString("dt_dis_n");
                emp.extra_time = res.getBoolean("x_time");
                emp.way_pay = res.getInt("fk_tp_pay");
                emp.dept_rh_id = res.getInt("fk_dep");
                emp.is_active = res.getBoolean("b_act");
                emp.is_deleted = res.getBoolean("b_del");

                lEmps.add(emp);
            }

            conn.close();
            st.close();
            res.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        lEmps = this.assignCompany(lEmps);

        return lEmps;
    }
    
    private ArrayList<SEmployee> assignCompany(ArrayList<SEmployee> lEmps) throws SQLException, ClassNotFoundException, SConfigException {
        ArrayList<SCompany> companies = this.getDatabasesWithPayroll();
        ArrayList<HashMap<Integer, Integer>> ids = new ArrayList();
        for (SCompany company : companies) {
            ids.add(this.getEmployeesFromCompany(company));
        }
        
        for (SEmployee emp : lEmps) {
            for (HashMap<Integer, Integer> empCompay : ids) {
                if (empCompay.containsKey(emp.id_employee)) {
                    emp.setCompany_id(empCompay.get(emp.id_employee));
                    break;
                }
            }
        }
        
        return lEmps;
    }
    
    private HashMap<Integer, Integer> getEmployeesFromCompany(SCompany company) throws SQLException, ClassNotFoundException, SConfigException {
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", company.getDatabase_nm(), "", "");
        
        if (conn == null) {
            return null;
        }
        
        String query = "SELECT id_emp FROM hrs_emp_member;";
        
        HashMap<Integer, Integer> lEmpIds = null;

        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(query);

            lEmpIds = new HashMap();
            
            while (res.next()) {
                lEmpIds.put(res.getInt("id_emp"), company.getId_company());
            }

            conn.close();
            st.close();
            res.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lEmpIds;
    }

    public ArrayList<SHoliday> getAllHolidays(String lastSyncDate) throws SQLException, ClassNotFoundException, SConfigException {
        String mainDataBase = this.getMainDatabase();

        return this.getHolidays(lastSyncDate, mainDataBase);
    }

    private ArrayList<SHoliday> getHolidays(String strDate, String dbName) throws SQLException, ClassNotFoundException, SConfigException {
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", dbName, "", "");
        
        if (conn == null) {
            return null;
        }

        String query = "SELECT "
                + "    id_hdy,"
                + "    id_hol,"
                + "    code,"
                + "    name,"
                + "    dt,"
                + "    b_del"
                + " FROM "
                + "    hrs_hol "
                + "WHERE "
                + "    (ts_usr_ins >= '" + strDate + "' "
                + "        OR ts_usr_upd >= '" + strDate + "');";

        ArrayList<SHoliday> lHolidays = null;

        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(query);

            lHolidays = new ArrayList();

            SHoliday hol = null;
            while (res.next()) {
                hol = new SHoliday();

                hol.id_holiday = res.getInt("id_hol");
                hol.year = res.getInt("id_hdy");
                hol.code = res.getString("code");
                hol.name = res.getString("name");
                hol.dt_date = res.getString("dt");
                hol.is_deleted = res.getBoolean("b_del");

                lHolidays.add(hol);
            }

            conn.close();
            st.close();
            res.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lHolidays;
    }

    public ArrayList<SFirstDayYear> getAllFirstDayOfYear(String lastSyncDate) throws SQLException, ClassNotFoundException, SConfigException {
        String mainDataBase = this.getMainDatabase();

        return this.getFirstDayOfYear(lastSyncDate, mainDataBase);
    }

    private ArrayList<SFirstDayYear> getFirstDayOfYear(String strDate, String dbName) throws SQLException, ClassNotFoundException, SConfigException {
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", dbName, "", "");
        
        if (conn == null) {
            return null;
        }

        String query = "SELECT "
                + "    id_fdy,"
                + "    fdy,"
                + "    b_del"
                + " FROM "
                + "    erp_otsa.hrs_fdy"
                + " WHERE "
                + "    (ts_usr_ins >= '" + strDate + "' "
                + "        OR ts_usr_upd >= '" + strDate + "');";

        ArrayList<SFirstDayYear> lFDYs = null;

        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(query);

            lFDYs = new ArrayList();

            SFirstDayYear fdy = null;
            while (res.next()) {
                fdy = new SFirstDayYear();

                fdy.year = res.getInt("id_fdy");
                fdy.dt_date = res.getString("fdy");
                fdy.is_deleted = res.getBoolean("b_del");

                lFDYs.add(fdy);
            }

            conn.close();
            st.close();
            res.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lFDYs;
    }

    public ArrayList<SAbsence> getAllAbsences(String lastSyncDate) throws SQLException, ClassNotFoundException, SConfigException {
        ArrayList<SCompany> dbs = this.getDatabasesWithPayroll();

        if (dbs == null) {
            return null;
        }
        
        ArrayList<SAbsence> lAbss = new ArrayList();
        for (SCompany db : dbs) {
            lAbss.addAll(this.getAbsences(lastSyncDate, db.getDatabase_nm()));
        }

        return lAbss;
    }

    private ArrayList<SAbsence> getAbsences(String strDate, String dbName) throws SQLException, ClassNotFoundException, SConfigException {
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", dbName, "", "");
        
        if (conn == null) {
            return null;
        }

        String query = "SELECT  "
                + "    id_emp, "
                + "    id_abs, "
                + "    num, "
                + "    dt, "
                + "    dt_sta, "
                + "    dt_end, "
                + "    eff_day, "
                + "    ben_year, "
                + "    ben_ann,"
                + "    fk_cl_abs, "
                + "    fk_tp_abs, "
                + "    nts, "
                + "    b_clo, "
                + "    b_del "
                + "FROM"
                + "    hrs_abs"
                + " WHERE "
                + "    (ts_usr_ins >= '" + strDate + "' "
                + "        OR ts_usr_upd >= '" + strDate + "') "
                + " AND ts_usr_ins >= '2019-01-01';";

        ArrayList<SAbsence> lAbss = null;

        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(query);

            lAbss = new ArrayList();

            SAbsence abs = null;
            while (res.next()) {
                abs = new SAbsence();

                abs.id_emp = res.getInt("id_emp");
                abs.id_abs = res.getInt("id_abs");
                abs.num = res.getString("num");
                abs.dt_date = res.getString("dt");
                abs.dt_start = res.getString("dt_sta");
                abs.dt_end = res.getString("dt_end");
                abs.eff_days = res.getInt("eff_day");
                abs.ben_year = res.getInt("ben_year");
                abs.ben_ann = res.getInt("ben_ann");
                abs.notes = res.getString("nts");
                abs.fk_class_abs = res.getInt("fk_cl_abs");
                abs.fk_type_abs = res.getInt("fk_tp_abs");
                abs.is_closed = res.getBoolean("b_clo");
                abs.is_deleted = res.getBoolean("b_del");

                lAbss.add(abs);
            }

            conn.close();
            st.close();
            res.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lAbss;
    }

}

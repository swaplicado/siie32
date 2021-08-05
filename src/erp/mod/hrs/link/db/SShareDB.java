/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

import erp.lib.SLibUtilities;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Edwin Carmona, Cesar Orozco
 */
public class SShareDB {

    /**
     * Retorna las empresas de SIIE que tienen activa el módulo de nóminas
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws SConfigException
     */
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
        } catch (SQLException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lBds;
    }

    /**
     * Retorna el nombre de la base de datos que se considera como principal en
     * SIIE, para de esta tomar los catálogos que se necesitan para el sistema
     * externo
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws SConfigException
     */
    private String getMainDatabase() throws SQLException, ClassNotFoundException, SConfigException {
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", "", "", "");
        if (conn == null) {
            return "";
        }

        if (mdb.getMainBb() == 0) {
            throw new SConfigException("No hay configuración de base de datos principal");
        }

        int idDataBase = mdb.getMainBb();

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
        } catch (SQLException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    /**
     * Retorna los departamentos contemplados en SIIE
     *
     * @param strDate
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws SConfigException
     */
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
     * Retorna los empleados que hayan sido agregados o modificados en SIIE
     * después de la fecha recibida
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
                + "    e.overtime, "
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
                emp.overtime_policy = res.getInt("overtime");
                emp.way_pay = res.getInt("fk_tp_pay");
                emp.dept_rh_id = res.getInt("fk_dep");
                emp.is_active = res.getBoolean("b_act");
                emp.is_deleted = res.getBoolean("b_del");

                lEmps.add(emp);
            }

            conn.close();
            st.close();
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        lEmps = this.assignCompany(lEmps);

        return lEmps;
    }

    /**
     * Método que determina la empresa a la que pertenece el empleado en SIIE
     *
     * @param lEmps
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws SConfigException
     */
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

    /**
     * Obtiene los empleados que pertecen a una empresa en específico
     *
     * @param company
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws SConfigException
     */
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
        } catch (SQLException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lEmpIds;
    }

    /**
     * Obtiene los días festivos que se han agregado o modificado después de la
     * fecha recibida. Toma en cuenta la empresa considerada como principal.
     *
     * @param lastSyncDate
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws SConfigException
     */
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
        } catch (SQLException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lHolidays;
    }

    /**
     * Obtiene el primer día del año
     *
     * @param lastSyncDate
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws SConfigException
     */
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
        } catch (SQLException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lFDYs;
    }

    /**
     * Obtiene las incidencias de los empleados agregadas o modificadas después
     * de la fecha recibida.
     *
     * @param lastSyncDate
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws SConfigException
     */
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

    /**
     * Obtiene las incidencias de una empresa en específico que hayan sido
     * modificadas o agregadas después de la fecha de corte
     *
     * @param strDate
     * @param dbName
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws SConfigException
     */
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
                abs.company = dbName;

                lAbss.add(abs);
            }

            conn.close();
            st.close();
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lAbss;
    }

    /**
     * Obtiene las fechas de corte de las nóminas que hayan sido modificadas o
     * agregadas después de la fecha de corte
     *
     * @param lastSyncDate
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws SConfigException
     */
    public ArrayList<SPrepayCutCalendar> getAllCutsCalendar(String lastSyncDate) throws SQLException, ClassNotFoundException, SConfigException {
        String mainDataBase = this.getMainDatabase();

        return this.getPrepayCutCalendar(lastSyncDate, mainDataBase);
    }

    /**
     * Obtiene las fechas de corte de una empresa en específico
     *
     * @param strDate
     * @param dbName
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws SConfigException
     */
    private ArrayList<SPrepayCutCalendar> getPrepayCutCalendar(String strDate, String dbName) throws SQLException, ClassNotFoundException, SConfigException {
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", dbName, "", "");

        if (conn == null) {
            return null;
        }

        String query = "SELECT  "
                + "    id_cal, "
                + "    year, "
                + "    num, "
                + "    dt_cut, "
                + "    b_del, "
                + "    fk_tp_pay "
                + "FROM"
                + "    hrs_pre_pay_cut_cal"
                + " WHERE "
                + "    (ts_usr_ins >= '" + strDate + "' "
                + "        OR ts_usr_upd >= '" + strDate + "');";

        ArrayList<SPrepayCutCalendar> lCuts = null;

        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(query);

            lCuts = new ArrayList();

            SPrepayCutCalendar cut = null;
            while (res.next()) {
                cut = new SPrepayCutCalendar();

                cut.id_cal = res.getInt("id_cal");
                cut.year = res.getInt("year");
                cut.num = res.getInt("num");
                cut.dt_cut = res.getString("dt_cut");
                cut.fk_tp_pay = res.getInt("fk_tp_pay");
                cut.is_deleted = res.getBoolean("b_del");

                lCuts.add(cut);
            }

            conn.close();
            st.close();
            res.close();
        } catch (SQLException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lCuts;
    }

    /**
     * Obtiene las fotos de los empleados contenidos en la lista parámetro
     * 
     * @param ids 
     * 
     * @return ArrayList<SPhoto>
     * 
     * @throws SConfigException
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    public ArrayList<SPhoto> getPhotosOfEmployees(ArrayList<Integer> ids) throws SConfigException, ClassNotFoundException, SQLException, UnsupportedEncodingException, IOException {
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", "", "", "");

        if (conn == null) {
            return null;
        }
        
        String sids = "";
        for (Integer id : ids) {
            sids += id + ",";
        }

        if (sids.isEmpty()) {
            return new ArrayList<>();
        }
        
        sids = sids.substring(0, sids.length() - 1);
        
        String query = "SELECT "
                + "    id_emp, num, img_pho_n "
                + "FROM "
                + "    erp.hrsu_emp "
                + "WHERE "
                + "    b_act AND NOT b_del AND id_emp IN (" + sids +") "
                + "ORDER BY lastname1 ASC;";

        ArrayList<SPhoto> lPhotos = null;
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(query);

            lPhotos = new ArrayList<>();

            SPhoto photo = null;
            while (res.next()) {
                photo = new SPhoto();

                photo.idEmployee = res.getInt("id_emp");
                photo.numEmployee = res.getInt("num");
                
                java.sql.Blob ablob = res.getBlob("img_pho_n");

                if (ablob == null) {
                    photo.photo = null;
                } else {
                    ImageIcon icon = SLibUtilities.convertBlobToImageIcon(ablob);

                    BufferedImage image = new BufferedImage(
                            icon.getIconWidth(),
                            icon.getIconHeight(),
                            BufferedImage.TYPE_INT_RGB);
                    
                    Graphics g = image.createGraphics();
                    // paint the Icon to the BufferedImage.
                    icon.paintIcon(null, g, 0,0);
                    g.dispose();
                    
                    photo.photo = SShareDB.encodeToString(image, "jpg");
                }

                lPhotos.add(photo);
            }

            conn.close();
            st.close();
            res.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lPhotos;
    }

    /**
     * BufferedImage to String
     * 
     * @param image
     * @param type
     * @return 
     */
    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageString;
    }
}

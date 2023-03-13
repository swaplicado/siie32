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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sun.misc.BASE64Encoder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import erp.mod.SModConsts;
import static erp.mod.SModConsts.CFGU_CO;
import erp.mod.SModSysConsts;
import erp.mod.SModUtils;
import erp.mod.SModuleHrs;
import erp.mod.hrs.db.SDbAbsence;
import erp.musr.data.SDataUser;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import org.json.simple.JSONArray;
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
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
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
            dept.head_employee_id = res.getInt("fk_emp_head_n");
            dept.superior_department_id = res.getInt("fk_dep_sup_n");

            lDepts.add(dept);
        }

        conn.close();
        st.close();
        res.close();

        return lDepts;
    }
    
    /**
     * Retorna los puestos contemplados en SIIE
     *
     * @param strDate
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws SConfigException
     */
    public ArrayList<SPosition> getPositions(String strDate) throws SQLException, ClassNotFoundException, SConfigException {
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", "", "", "");

        if (conn == null) {
            return null;
        }

        String query = "SELECT * FROM erp.hrsu_pos "
                + "WHERE "
                + "    (ts_usr_ins >= '" + strDate + "' "
                + "        OR ts_usr_upd >= '" + strDate + "')";

        ArrayList<SPosition> lPositions = null;

        Statement st = conn.createStatement();
        ResultSet res = st.executeQuery(query);

        lPositions = new ArrayList();
        SPosition pos = null;
        while (res.next()) {
            pos = new SPosition();

            pos.id_position = res.getInt("id_pos");
            pos.code = res.getString("code");
            pos.name = res.getString("name");
            pos.is_deleted = res.getBoolean("b_del");
            pos.is_system = res.getBoolean("b_sys");
            pos.fk_department = res.getInt("fk_dep");

            lPositions.add(pos);
        }

        conn.close();
        st.close();
        res.close();

        return lPositions;
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
                + "    e.dt_bir, "
                + "    e.dt_ben, "
                + "    e.dt_hire, "
                + "    e.dt_dis_n, "
//                + "    e.dt_tp_pay, "
                + "    e.overtime, "
                + "    e.checker_policy, "
                + "    e.fk_tp_pay, "
                + "    e.fk_dep, "
                + "    e.fk_pos, "
                + "    e.b_act, "
                + "    e.b_del, "
                + "    bpcon.email_01 "
                + "FROM "
                + "    erp.hrsu_emp e "
                + "        INNER JOIN "
                + "    erp.bpsu_bp bp ON e.id_emp = bp.id_bp "
                + "INNER JOIN "
                + "	erp.bpsu_bpb bpb ON bp.id_bp = bpb.fid_bp "
                + "INNER JOIN "
                + "	erp.bpsu_bpb_con bpcon ON bpb.id_bpb = bpcon.id_bpb "
                + "WHERE "
                + "    (e.ts_usr_ins >= '" + strDate + "' "
                + "        OR e.ts_usr_upd >= '" + strDate + "')"
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
                emp.lastname1 = res.getString("lastname1");
                emp.lastname2 = res.getString("lastname2");
                emp.lastname = res.getString("lastname");
                emp.firstname = res.getString("firstname");
                emp.admission_date = res.getString("dt_hire");
                emp.leave_date = res.getString("dt_dis_n");
                emp.benefit_date = res.getString("dt_ben");
                emp.dt_bir = res.getString("dt_bir");
//                emp.dt_tp_pay = res.getString("dt_tp_pay");
                emp.email = res.getString("email_01");
                emp.overtime_policy = res.getInt("overtime");
                emp.checker_policy = res.getInt("checker_policy");
                emp.way_pay = res.getInt("fk_tp_pay");
                emp.dept_rh_id = res.getInt("fk_dep");
                emp.siie_job_id = res.getInt("fk_pos");
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
    
    public ArrayList<SEmployeeVacations> getEmployeeVacations(String strDate) throws SConfigException, ClassNotFoundException, SQLException {
        SMySqlClass mdb = new SMySqlClass();
        String empresas[]= new String[5];
        empresas[0] = "erp_aeth";
        empresas[1] = "erp_amesa";
        empresas[2] = "erp_otsa";
        empresas[3] = "erp_th";
        empresas[4] = "erp_sasa";
        
        ArrayList<SEmployeeVacations> lEmp = null;
        lEmp = new ArrayList();
        SEmployeeVacations emp = null;
         
        for(int num_empresas = 0 ; num_empresas < empresas.length ; num_empresas ++){
        
            Connection conn = mdb.connect("", "", empresas[num_empresas], "", "");

            if (conn == null) {
                return null;
            }

            String sql = "";
            String sqlCutOff = "";
            String sqlBenefit = "";
            // creación del hashmap utilizado para saber tipo de pago
            HashMap<Integer, Integer> paymentTypeBenefitVacationMap = new HashMap<>();

            String sqlMap = "SELECT tp.id_tp_pay, b.id_ben "
                    + "FROM hrs_ben AS b "
                    + "RIGHT OUTER JOIN erp.hrss_tp_pay AS tp ON b.fk_tp_pay_n = tp.id_tp_pay OR b.fk_tp_pay_n IS NULL "
                    + "WHERE NOT b.b_del AND b.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND "
                    + "b.fk_ear = (SELECT id_ear FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " "
                    + " WHERE fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_EAR + " AND fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND "
                    + " fk_cl_abs_n IS NOT NULL ORDER BY id_ear LIMIT 1) "
                    + "UNION "
                    + "SELECT NULL AS id_tp_pay, b.id_ben "
                    + "FROM hrs_ben AS b "
                    + "WHERE NOT b.b_del AND b.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND "
                    + "b.fk_ear = (SELECT id_ear FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " "
                    + " WHERE fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_EAR + " AND fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND "
                    + " fk_cl_abs_n IS NOT NULL ORDER BY id_ear LIMIT 1) "
                    + "ORDER BY id_tp_pay;";


                Statement st = conn.createStatement();
                ResultSet res = st.executeQuery(sqlMap);
                while (res.next()) {
                    paymentTypeBenefitVacationMap.put(res.getInt("id_tp_pay"), res.getInt("id_ben"));
                }

            try {

                // prepare SQL filter for table of benefits:

                Integer defaultBenefitId = paymentTypeBenefitVacationMap.get(0); // default benefit
                Integer benefitId;

                sqlBenefit = "CASE ";

                benefitId = paymentTypeBenefitVacationMap.get(SModSysConsts.HRSS_TP_PAY_WEE);
                sqlBenefit += "WHEN e.fk_tp_pay = " + SModSysConsts.HRSS_TP_PAY_WEE + " THEN " + (benefitId == null ? defaultBenefitId : benefitId) + " ";

                benefitId = paymentTypeBenefitVacationMap.get(SModSysConsts.HRSS_TP_PAY_FOR);
                sqlBenefit += "WHEN e.fk_tp_pay = " + SModSysConsts.HRSS_TP_PAY_FOR + " THEN " + (benefitId == null ? defaultBenefitId : benefitId) + " ";

                sqlBenefit += "ELSE " + (defaultBenefitId == null ? 0 : defaultBenefitId) + " ";

                sqlBenefit += "END";
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }


             sqlCutOff = "'" + strDate + "'";

             sql += (sql.isEmpty() ? "" : "AND ") + "NOT e.b_del ";
             sql += "AND e.b_act = 1 ";

            /*
            NOTE (due to former functionality):
            Meaning of row-view's primary key (required so for cardex dialog):
            ID_1 = employee's ID
            ID_2 = employee's anniversary (starting from 0, 1, 2 and so over)
            ID_3 = days elapsed in employee's current anniversary
            ID_4 = benefit-table's ID
            */

            String sqlVac = "SELECT b.id_bp AS _employee_id, b.bp AS _employee, e.num AS _employee_number, d.name AS _department, "
                    + "e.dt_ben AS _benefits, e.dt_dis_n AS _dismiss, e.b_act AS _active, tp.name AS _payment_type, "
                    + "@cut_off := IF(e.b_act, " + sqlCutOff + ", e.dt_dis_n) AS _cut_off, "
                    + "@seniority := TIMESTAMPDIFF(YEAR, e.dt_ben, @cut_off) AS _seniority, "
                    + "@vac_right := (SELECT COALESCE(SUM(bra.ben_day), 0) "
                    + "  FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN_ROW_AUX) + " AS bra "
                    + "  WHERE bra.id_ben = " + sqlBenefit + " AND "
                    + "  bra.ann <= TIMESTAMPDIFF(YEAR, e.dt_ben, @cut_off)) AS _vac_right, "
                    + "@vac_ear := (SELECT COALESCE(SUM(pre.unt_all), 0.0) "
                    + "  FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                    + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                    + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                    + "  WHERE pre.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del AND "
                    + "  pre.ben_year > 0 AND pre.ben_year >= YEAR(e.dt_ben) AND "
                    + "  pr.id_emp = e.id_emp AND ((p.dt_end >= e.dt_ben AND p.dt_end <= @cut_off) OR p.id_pay = 0)) AS _vac_ear, "
                    + "@vac_ded := (SELECT COALESCE(SUM(prd.unt_all), 0.0) "
                    + "  FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                    + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                    + "  INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp "
                    + "  WHERE prd.fk_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND NOT p.b_del AND NOT pr.b_del AND NOT prd.b_del AND "
                    + "  prd.ben_year > 0 AND prd.ben_year >= YEAR(e.dt_ben) AND "
                    + "  pr.id_emp = e.id_emp AND ((p.dt_end >= e.dt_ben AND p.dt_end <= @cut_off) OR p.id_pay = 0)) AS _vac_ded, "
                    + "@vac_right - (@vac_ear + @vac_ded) AS _vac_pend, "
                    + "b.id_bp AS " + SDbConsts.FIELD_ID + "1, "
                    + "@seniority AS " + SDbConsts.FIELD_ID + "2, "
                    + "DATEDIFF(@cut_off, DATE_ADD(e.dt_ben, INTERVAL @seniority YEAR)) AS " + SDbConsts.FIELD_ID + "3, "
                    + sqlBenefit + " AS " + SDbConsts.FIELD_ID + "4, "
                    + "b.bp AS " + SDbConsts.FIELD_NAME + ", "
                    + "e.num AS " + SDbConsts.FIELD_CODE + " "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON b.id_bp = e.id_emp "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " AS em ON b.id_bp = em.id_emp "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS d ON e.fk_dep = d.id_dep "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS tp ON e.fk_tp_pay = tp.id_tp_pay "
                    + "WHERE " + sql
                    + "ORDER BY b.bp, b.id_bp;";




            Statement stV = conn.createStatement();
            ResultSet resV = st.executeQuery(sqlVac);




            while (resV.next()) {
                emp = new SEmployeeVacations();

                emp.setEmployee_id(resV.getInt("_employee_id"));
                emp.setEmployee_number(resV.getInt("_employee_number"));

                String dbmsSchema = "erp_aeth.";
                int yearBenefits = SLibTimeUtils.digestYear(resV.getDate("_benefits"))[0];
                
                // Listado de vacaciones
                ArrayList<SDataVacations> lVac = null;
                lVac = new ArrayList();
                SDataVacations vac = null;
                
                //Listado de incidentes
                ArrayList<SDataIncidents> lInc = null;
                lInc = new ArrayList();
                SDataIncidents incidents = null;

                    for (int anniversary = resV.getInt("_seniority") + 1; anniversary >= 1; anniversary--) {
                        String sqlV;
                        String sqlCons;
                        String sqlInc;
                        ResultSet resultSet;
                        vac = new SDataVacations();
                        
                        double programados_no_gozados = 0;
                        double consumed = 0;
                        double portal_consumed = 0;
                        
                        int benefitYear = yearBenefits + anniversary - 1;
                        int mnFormSubtype = SModSysConsts.HRSS_TP_BEN_VAC;
                        // scheduled days (only for vacations):

                            sqlV = "SELECT SUM(eff_day) AS _days_sched "
                                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ABS) + " "
                                    + "WHERE id_emp = " + resV.getInt("_employee_id") + " AND "
                                    + "ext_req_id = 0 AND "
                                    + "fk_cl_abs = " + SModSysConsts.HRSU_TP_ABS_VAC[0] + " AND "
                                    + "fk_tp_abs = " + SModSysConsts.HRSU_TP_ABS_VAC[1] + " AND "
                                    + "ext_req_id = 0 AND "
                                    + "ben_year = " + benefitYear + " AND ben_ann = " + anniversary + " AND "
                                    + "NOT b_del;";

                            Statement stCon = conn.createStatement();

                            resultSet = stCon.executeQuery(sqlV);
                            if (resultSet.next()) {
                                programados_no_gozados = resultSet.getDouble("_days_sched");
                            }
                        
                        // consumed days:
                            sqlCons = "SELECT SUM(hrs_abs_cns.eff_day) AS _days_consu "
                                    + "FROM"
                                    + " hrs_abs_cns "
                                    + "INNER JOIN"
                                    + " hrs_abs "
                                    + "ON"
                                    + " hrs_abs_cns.id_abs = hrs_abs.id_abs AND "
                                    + " hrs_abs_cns.id_emp = hrs_abs.id_emp "
                                    + "WHERE hrs_abs.id_emp = " + resV.getInt("_employee_id") + " AND "
                                    + "ext_req_id = 0 AND "
                                    + "fk_cl_abs = " + SModSysConsts.HRSU_TP_ABS_VAC[0] + " AND "
                                    + "fk_tp_abs = " + SModSysConsts.HRSU_TP_ABS_VAC[1] + " AND "
                                    + "ben_year = " + benefitYear + " AND ben_ann = " + anniversary + " AND "
                                    + "NOT hrs_abs_cns.b_del AND NOT hrs_abs.b_del;"; 
                          
                            resultSet = stCon.executeQuery(sqlCons);
                            if (resultSet.next()) {
                                consumed = resultSet.getDouble("_days_consu");
                            }
                            
                        // consumos de incidencias de solicitudes sistema Portal GH
                        
                            sqlInc = "SELECT hrs_abs.ext_req_id AS application, hrs_abs.ben_year AS year, hrs_abs.ben_ann AS anniversary, hrs_abs_cns.eff_day AS consumed "
                                    + "FROM"
                                    + " hrs_abs_cns "
                                    + "INNER JOIN"
                                    + " hrs_abs "
                                    + "ON"
                                    + " hrs_abs_cns.id_abs = hrs_abs.id_abs AND "
                                    + " hrs_abs_cns.id_emp = hrs_abs.id_emp "
                                    + "WHERE hrs_abs.id_emp = " + resV.getInt("_employee_id") + " AND "
                                    + "ext_req_id != 0 AND "
                                    + "fk_cl_abs = " + SModSysConsts.HRSU_TP_ABS_VAC[0] + " AND "
                                    + "fk_tp_abs = " + SModSysConsts.HRSU_TP_ABS_VAC[1] + " AND "
                                    + "ben_year = " + benefitYear + " AND ben_ann = " + anniversary + " AND "
                                    + "NOT hrs_abs_cns.b_del AND NOT hrs_abs.b_del;"; 
                            
                            resultSet = stCon.executeQuery(sqlInc);
                            while (resultSet.next()) {
                                incidents = new SDataIncidents();
                                // agregar renglon de incidencia
                                incidents.setId_breakdown(resultSet.getInt("application"));
                                incidents.setAnniversary(anniversary);
                                incidents.setYear(benefitYear);
                                incidents.setDay_consumed(resultSet.getInt("consumed"));
                                
                                //sacar los días consumidos ligados al portal
                                portal_consumed = portal_consumed + resultSet.getInt("consumed");
                                
                                //agregar el renglon de vacaciones en la lista
                                lInc.add(incidents);
                            }
                        // payed days and amount:

                        double payedDays = 0;
                        double payedAmount = 0;

                        sqlV = "SELECT SUM(pre.unt_all) AS _days, SUM(pre.amt_r) AS _amount "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                                + "WHERE pr.id_emp = " + resV.getInt("_employee_id") + " AND pre.fk_tp_ben = " + mnFormSubtype + " AND "
                                + "pre.ben_year = " + benefitYear + " AND pre.ben_ann = " + anniversary + " AND "
                                + "p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(resV.getDate("_cut_off")) + "' AND "
                                + "NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del;";
                        resultSet = stCon.executeQuery(sqlV);
                        if (resultSet.next()) {
                            payedDays = resultSet.getDouble("_days");

                        }

                        sqlV = "SELECT SUM(prd.unt_all) AS _days, SUM(prd.amt_r) AS _amount "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp "
                                + "WHERE pr.id_emp = " + resV.getInt("_employee_id") + " AND prd.fk_tp_ben = " + mnFormSubtype + " AND "
                                + "prd.ben_year = " + benefitYear + " AND prd.ben_ann = " + anniversary + " AND "
                                + "p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(resV.getDate("_cut_off")) + "' AND "
                                + "NOT p.b_del AND NOT pr.b_del AND NOT prd.b_del;";
                        resultSet = stCon.executeQuery(sqlV);
                        if (resultSet.next()) {
                            payedDays = (payedDays - resultSet.getDouble("_days")); // decrement days
                        }
                        programados_no_gozados = programados_no_gozados - consumed;
                        payedDays = payedDays - portal_consumed;
                        if( payedDays < 0 ) { payedDays = 0; }
                        //insertar información de vacaciones por año y aniversario
                        vac.setVacation_programm(programados_no_gozados);
                        vac.setAnniversary(anniversary);
                        vac.setYear(benefitYear);
                        vac.setVacation_consumed(payedDays);
                        
                        //agregar el renglon de vacaciones en la lista
                        lVac.add(vac);
                        resultSet.close();
                    }
                emp.setRows(lVac);
                emp.setIncidents(lInc);
                lEmp.add(emp);
            }
            conn.close();
        }
        
        
        
        return lEmp;
    }
    
    public boolean cheakIncidents (String sJsonInc) throws SConfigException, ClassNotFoundException, SQLException{
//        SMySqlClass mdb = new SMySqlClass();
//        String empresas[]= new String[4];
//        ResultSet resultSet;
//        empresas[0] = "erp_aeth";
//        empresas[1] = "erp_amesa";
//        empresas[2] = "erp_otsa";
//        empresas[3] = "erp_th";
        
        SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);
        ResultSet resultSet;
//        String empresas[]= new String[4];
        int company = 0;
        
//        empresas[0] = "erp_aeth";
//        empresas[1] = "erp_amesa";
//        empresas[2] = "erp_otsa";
//        empresas[3] = "erp_th";

        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", "", "", "");

        if (conn == null) {
            return false;
        }
        
         JSONParser parser = new JSONParser();
         JSONObject root;
        
        try {
            boolean isAvailable = false;
            String incidents = "";
            root = (JSONObject) parser.parse(sJsonInc);
            company = Integer.parseInt(root.get("company_id").toString());
            String companies = "SELECT * "
                                    + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_CO) + " "
                                    + "WHERE id_co = " + company ;
            Statement stCon = conn.createStatement();

            resultSet = stCon.executeQuery(companies);
            if(!resultSet.next()){
                return false;
            } 
            
            conn = mdb.connect("", "", resultSet.getString("bd"), "", "");

            if (conn == null) {
                return false;
            }
            
            incidents = "SELECT * "
                                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ABS) + " "
                                    + "WHERE id_emp = " + root.get("employee_id").toString() + " AND "
                                    + "fk_cl_abs = " + SModSysConsts.HRSU_TP_ABS_VAC[0] + " AND "
                                    + "fk_tp_abs = " + SModSysConsts.HRSU_TP_ABS_VAC[1] + " AND ( "
                                    + "dt_sta BETWEEN '" + root.get("date_ini").toString() + "' AND '" + root.get("date_end").toString() + "' OR "
                                    + "dt_end BETWEEN '" + root.get("date_ini").toString() + "' AND '" + root.get("date_end").toString() + "' ) AND "
                                    + "NOT b_del;";
            stCon = conn.createStatement();

            resultSet = stCon.executeQuery(incidents);
            if (resultSet.next()) {
                isAvailable = false;
            }else{
                isAvailable = true;
            }
            
            return isAvailable;
        } catch (ParseException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean  setinIncidents(String sJsonInc) throws SConfigException, ClassNotFoundException, SQLException{
        SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);
        ResultSet resultSet;
//        String empresas[]= new String[4];
        int company = 0;
        
//        empresas[0] = "erp_aeth";
//        empresas[1] = "erp_amesa";
//        empresas[2] = "erp_otsa";
//        empresas[3] = "erp_th";

        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", "", "", "");

        if (conn == null) {
            return false;
        }
        
         boolean isAvailable = false;
         String incidents = "";
         JSONParser parser = new JSONParser();
         JSONObject root;
        try {
            root = (JSONObject) parser.parse(sJsonInc);
            company = Integer.parseInt(root.get("company_id").toString());
            String companies = "SELECT * "
                                    + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_CO) + " "
                                    + "WHERE id_co = " + company ;
            Statement stCon = conn.createStatement();

            resultSet = stCon.executeQuery(companies);
            if(!resultSet.next()){
                return false;
            } 
            
        } catch (ParseException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
           
        database.connect(
                "127.0.0.1", // agregar esta constante a la configuración de CAP Link
                "3306", // agregar esta constante a la configuración de CAP Link
                resultSet.getString("bd"), // agregar esta constante a la configuración de CAP Link
                "root", // agregar esta constante a la configuración de CAP Link
                "msroot"); // agregar esta constante a la configuración de CAP Link
        
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
        user.setPkUserId(SUtilConsts.USR_NA_ID); // agregar esta constante a la configuración de CAP Link

        session.setUser(user);

        Date now = new Date();
        
        session.setSystemDate(now);
//        session.setCurrentDate(now);
        session.setUserTs(now);
        session.setDatabase(database);
    
        session.setModuleUtils(new SModUtils());
        session.getModules().add(new SModuleHrs(session.getClient()));
        
        try {
            root = (JSONObject) parser.parse(sJsonInc);
            JSONArray rows = (JSONArray) root.get("rows");
            for (int i = 0 ; rows.size() > i ; i++){
                JSONObject row = (JSONObject) rows.get(i);
                
                SDbAbsence insert = new SDbAbsence();
                insert.initRegistry();
                
                Date date_send = new SimpleDateFormat("yyyy-MM-dd").parse(root.get("date_send").toString());
                Date start_date = new SimpleDateFormat("yyyy-MM-dd").parse(row.get("start_date").toString());
                Date end_date = new SimpleDateFormat("yyyy-MM-dd").parse(row.get("end_date").toString());
                
                insert.setPkEmployeeId(Integer.parseInt(root.get("employee_id").toString()));
                insert.computePrimaryKey(session);
                insert.setNumber(row.get("folio").toString());
                insert.setDate(date_send);
                insert.setDateStart(start_date);
                insert.setDateEnd(end_date);
                insert.setEffectiveDays(Integer.parseInt(row.get("effective_days").toString()));
                insert.setBenefitsYear(Integer.parseInt(row.get("year").toString()));
                insert.setBenefitsAnniversary(Integer.parseInt(row.get("anniversary").toString()));
                insert.setExternarRequestId(Integer.parseInt(row.get("breakdown_id").toString()));
                insert.setFkAbsenceClassId(SModSysConsts.HRSU_TP_ABS_VAC[0]);
                insert.setFkAbsenceTypeId(SModSysConsts.HRSU_TP_ABS_VAC[1]);
                insert.setFkUserClosedId(SUtilConsts.USR_NA_ID);
                           
                insert.save(session);
                
            }
            
        } catch (ParseException ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(SShareDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    
        return true;
    }
    
    public ArrayList<SPlanVacations> getSPlanVactions(String strDate) throws SConfigException, ClassNotFoundException, SQLException {
        
        String mainDataBase = this.getMainDatabase();
        
        ArrayList<SPlanVacations> lPlanVac = null;
        lPlanVac = new ArrayList();
        SPlanVacations planVac = null;
         
        SMySqlClass mdb = new SMySqlClass();
        Connection conn = mdb.connect("", "", mainDataBase, "", "");
            
            String query = "";
            query = "SELECT name AS nombre, dt_sta AS start,  id_ben AS id, fk_tp_pay_n AS pay "
                                    + "FROM"
                                    + " hrs_ben "
                                    + "WHERE fk_ear = 101 "
                                    + " AND b_del = 0;";
                          
            Statement stV = conn.createStatement();
            ResultSet resV = stV.executeQuery(query);

            if (resV.next()) {
                planVac.setName(resV.getString("nombre"));
                planVac.setWay_pay(resV.getInt("pay"));
                
                ArrayList<SPlanVacationsAux> lPlanVacAux = null;
                lPlanVacAux = new ArrayList();
                SPlanVacationsAux planVacAux = null;
                
                String queryAux = "";
                queryAux = "SELECT ann AS aniversary, ben_day AS days "
                                    + "FROM"
                                    + " hrs_ben_row_aux "
                                    + "WHERE id_ben = " + resV.getInt("id") +";";
                
                Statement stA = conn.createStatement();
                ResultSet resA = stA.executeQuery(queryAux);
                
                if (resA.next()) {
                    planVacAux.setYear(resA.getInt("aniversary"));
                    planVacAux.setDays(resA.getInt("days"));  
                    lPlanVacAux.add(planVacAux);
                }
                
                planVac.setPlanVacationsAux(lPlanVacAux);
                lPlanVac.add(planVac);
                
            }
        return lPlanVac;
    }
}

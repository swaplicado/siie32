/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos, Sergio Flores, Claudio Peña
 */
public class SDbHoliday extends SDbRegistryUser {

    protected int mnPkHolidayYearId;
    protected int mnPkHolidayId;
    protected String msCode;
    protected String msName;
    protected Date mtDate;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbHoliday() {
        super(SModConsts.HRS_HOL);
    }

    public void setPkHolidayYearId(int n) { mnPkHolidayYearId = n; }
    public void setPkHolidayId(int n) { mnPkHolidayId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setDate(Date t) { mtDate = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkHolidayYearId() { return mnPkHolidayYearId; }
    public int getPkHolidayId() { return mnPkHolidayId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public Date getDate() { return mtDate; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkHolidayYearId = pk[0];
        mnPkHolidayId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkHolidayYearId, mnPkHolidayId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkHolidayYearId = 0;
        mnPkHolidayId = 0;
        msCode = "";
        msName = "";
        mtDate = null;
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_hdy = " + mnPkHolidayYearId + " AND "
                + "id_hol = " + mnPkHolidayId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_hdy = " + pk[0] + " AND "
                + "id_hol = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkHolidayId = 0;

        msSql = "SELECT COALESCE(MAX(id_hol), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_hdy = " + mnPkHolidayYearId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkHolidayId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkHolidayYearId = resultSet.getInt("id_hdy");
            mnPkHolidayId = resultSet.getInt("id_hol");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mtDate = resultSet.getDate("dt");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkHolidayYearId + ", " +
                    mnPkHolidayId + ", " +
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_hdy = " + mnPkHolidayYearId + ", " +
                    "id_hol = " + mnPkHolidayId + ", " +
                    */
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbHoliday clone() throws CloneNotSupportedException {
        SDbHoliday registry = new SDbHoliday();

        registry.setPkHolidayYearId(this.getPkHolidayYearId());
        registry.setPkHolidayId(this.getPkHolidayId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setDate(this.getDate());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
   
    public static boolean finYearExists(SGuiSession session, int pkYearId) throws SQLException {
        boolean notYearExists = false;
        String mySql = "SELECT * FROM fin_year WHERE id_year = " + pkYearId + " AND NOT b_closed AND NOT b_del;";
        Statement statement = session.getStatement().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(mySql);
        if (resultSet.next()) {
            return notYearExists = true;
        } 
        else {
            return notYearExists;
        }
    }
    
    public static boolean existHolidaysInCurrentYear(SGuiSession session, int pkYearId) throws SQLException {
        boolean notHolidaysExists = false;
        String mySql = "SELECT * FROM hrs_hol WHERE id_hdy = " + pkYearId + " AND NOT b_del;";
        Statement statement = session.getStatement().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(mySql);
        if (resultSet.next()) {
            return notHolidaysExists = true;
        } 
        else {
            return notHolidaysExists;
        }
    }
    
    public static boolean existHolidaysToCopy(SGuiSession session, int pkYearCopy) throws SQLException {
        boolean notHolidaysExistsCopy = false;
        String mySql = "SELECT * FROM hrs_hol WHERE id_hdy = " + pkYearCopy + " AND NOT b_del;";
        Statement statement = session.getStatement().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(mySql);
        if (resultSet.next()) {
            return notHolidaysExistsCopy = true;
        }
        else {
            return notHolidaysExistsCopy;
        }
    }
    
    public static Date validateDate(int currentYear, Date dt) throws ParseException {
        SimpleDateFormat dtS = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = currentYear;
        
        if ((year % 4 == 0 && year % 100 != 0) || (year % 100 == 0 && year % 400 == 0)) {
            String dtFinal = year + "-" + (Integer.toString(month).length() <=1 ? ("0" + month ) : month) + "-" + (Integer.toString(day).length() <=1 ? ("0" + day ) : day) ;
            Date dtFinalDate = dtS.parse(dtFinal);
            
            return dtFinalDate;
        } else {
            if (month == 2 && day > 28) {
                day = 28;
                String dtFinal = year + "-" + (Integer.toString(month).length() <=1 ? ("0" + month ) : month) + "-" + (Integer.toString(day).length() <=1 ? ("0" + day ) : day) ;
                Date dtFinalDate = dtS.parse(dtFinal);
            
            return dtFinalDate;
            
            }
            else {
                String dtFinal = year + "-" + (Integer.toString(month).length() <=1 ? ("0" + month ) : month) + "-" + (Integer.toString(day).length() <=1 ? ("0" + day ) : day) ;
                Date dtFinalDate = dtS.parse(dtFinal);
            
            return dtFinalDate;
            
            }
        }
    }
}


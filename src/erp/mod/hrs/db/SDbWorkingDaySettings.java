/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbWorkingDaySettings extends SDbRegistryUser {

    protected int mnPkWorkingDaySettingsId;
    protected String msCode;
    protected String msName;
    protected int mnWorkingDaysWeek;
    protected int mnWorkingHoursDay;
    protected boolean mbWeekdaySettingSunday;
    protected boolean mbWeekdaySettingMonday;
    protected boolean mbWeekdaySettingTuesday;
    protected boolean mbWeekdaySettingWednesday;
    protected boolean mbWeekdaySettingThursday;
    protected boolean mbWeekdaySettingFriday;
    protected boolean mbWeekdaySettingSaturday;
    //protected boolean mbDeleted;
    protected int mnFkPaymentTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbWorkingDaySettings() {
        super(SModConsts.HRS_WDS);
    }

    public void setPkWorkingDaySettingsId(int n) { mnPkWorkingDaySettingsId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setWorkingDaysWeek(int n) { mnWorkingDaysWeek = n; }
    public void setWorkingHoursDay(int n) { mnWorkingHoursDay = n; }
    public void setWeekdaySettingSunday(boolean b) { mbWeekdaySettingSunday = b; }
    public void setWeekdaySettingMonday(boolean b) { mbWeekdaySettingMonday = b; }
    public void setWeekdaySettingTuesday(boolean b) { mbWeekdaySettingTuesday = b; }
    public void setWeekdaySettingWednesday(boolean b) { mbWeekdaySettingWednesday = b; }
    public void setWeekdaySettingThursday(boolean b) { mbWeekdaySettingThursday = b; }
    public void setWeekdaySettingFriday(boolean b) { mbWeekdaySettingFriday = b; }
    public void setWeekdaySettingSaturday(boolean b) { mbWeekdaySettingSaturday = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkPaymentTypeId(int n) { mnFkPaymentTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkWorkingDaySettingsId() { return mnPkWorkingDaySettingsId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public int getWorkingDaysWeek() { return mnWorkingDaysWeek; }
    public int getWorkingHoursDay() { return mnWorkingHoursDay; }
    public boolean isWeekdaySettingSunday() { return mbWeekdaySettingSunday; }
    public boolean isWeekdaySettingMonday() { return mbWeekdaySettingMonday; }
    public boolean isWeekdaySettingTuesday() { return mbWeekdaySettingTuesday; }
    public boolean isWeekdaySettingWednesday() { return mbWeekdaySettingWednesday; }
    public boolean isWeekdaySettingThursday() { return mbWeekdaySettingThursday; }
    public boolean isWeekdaySettingFriday() { return mbWeekdaySettingFriday; }
    public boolean isWeekdaySettingSaturday() { return mbWeekdaySettingSaturday; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkPaymentTypeId() { return mnFkPaymentTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public int countWorkingDays() {
        int workingDays = 0;
        
        if (mbWeekdaySettingSunday) {
            workingDays++;
        }
        if (mbWeekdaySettingMonday) {
            workingDays++;
        }
        if (mbWeekdaySettingTuesday) {
            workingDays++;
        }
        if (mbWeekdaySettingWednesday) {
            workingDays++;
        }
        if (mbWeekdaySettingThursday) {
            workingDays++;
        }
        if (mbWeekdaySettingFriday) {
            workingDays++;
        }
        if (mbWeekdaySettingSaturday) {
            workingDays++;
        }
        
        return workingDays;
    }
    
    public int countNonWorkingDays() {
        int nonWorkingDays = 0;
        
        if (!mbWeekdaySettingSunday) {
            nonWorkingDays++;
        }
        if (!mbWeekdaySettingMonday) {
            nonWorkingDays++;
        }
        if (!mbWeekdaySettingTuesday) {
            nonWorkingDays++;
        }
        if (!mbWeekdaySettingWednesday) {
            nonWorkingDays++;
        }
        if (!mbWeekdaySettingThursday) {
            nonWorkingDays++;
        }
        if (!mbWeekdaySettingFriday) {
            nonWorkingDays++;
        }
        if (!mbWeekdaySettingSaturday) {
            nonWorkingDays++;
        }
        
        return nonWorkingDays;
    }
    
    /**
     * Checks if supplied day of week is a working day.
     * @param dayOfWeek Calendar.DAY_OF_WEEK.
     * @return <code>true</code> if supplied day of week is a working day.
     */
    public boolean isWorkingDay(final int dayOfWeek) {
        boolean isWorkingDay = false;
        
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                isWorkingDay = mbWeekdaySettingSunday;
                break;
            case Calendar.MONDAY:
                isWorkingDay = mbWeekdaySettingMonday;
                break;
            case Calendar.TUESDAY:
                isWorkingDay = mbWeekdaySettingTuesday;
                break;
            case Calendar.WEDNESDAY:
                isWorkingDay = mbWeekdaySettingWednesday;
                break;
            case Calendar.THURSDAY:
                isWorkingDay = mbWeekdaySettingThursday;
                break;
            case Calendar.FRIDAY:
                isWorkingDay = mbWeekdaySettingFriday;
                break;
            case Calendar.SATURDAY:
                isWorkingDay = mbWeekdaySettingSaturday;
                break;
            default:
        }
        
        return isWorkingDay;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkWorkingDaySettingsId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkWorkingDaySettingsId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkWorkingDaySettingsId = 0;
        msCode = "";
        msName = "";
        mnWorkingDaysWeek = 0;
        mnWorkingHoursDay = 0;
        mbWeekdaySettingSunday = false;
        mbWeekdaySettingMonday = false;
        mbWeekdaySettingTuesday = false;
        mbWeekdaySettingWednesday = false;
        mbWeekdaySettingThursday = false;
        mbWeekdaySettingFriday = false;
        mbWeekdaySettingSaturday = false;
        mbDeleted = false;
        mnFkPaymentTypeId = 0;
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
        return "WHERE id_wds = " + mnPkWorkingDaySettingsId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_wds = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkWorkingDaySettingsId = 0;

        msSql = "SELECT COALESCE(MAX(id_wds), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkWorkingDaySettingsId = resultSet.getInt(1);
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
            mnPkWorkingDaySettingsId = resultSet.getInt("id_wds");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mnWorkingDaysWeek = resultSet.getInt("wrk_day_wee");
            mnWorkingHoursDay = resultSet.getInt("wrk_hrs_day");
            mbWeekdaySettingSunday = resultSet.getBoolean("b_wds_sun");
            mbWeekdaySettingMonday = resultSet.getBoolean("b_wds_mon");
            mbWeekdaySettingTuesday = resultSet.getBoolean("b_wds_tue");
            mbWeekdaySettingWednesday = resultSet.getBoolean("b_wds_wed");
            mbWeekdaySettingThursday = resultSet.getBoolean("b_wds_thu");
            mbWeekdaySettingFriday = resultSet.getBoolean("b_wds_fri");
            mbWeekdaySettingSaturday = resultSet.getBoolean("b_wds_sat");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkPaymentTypeId = resultSet.getInt("fk_tp_pay");
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
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkWorkingDaySettingsId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    mnWorkingDaysWeek + ", " + 
                    mnWorkingHoursDay + ", " + 
                    (mbWeekdaySettingSunday ? 1 : 0) + ", " + 
                    (mbWeekdaySettingMonday ? 1 : 0) + ", " + 
                    (mbWeekdaySettingTuesday ? 1 : 0) + ", " + 
                    (mbWeekdaySettingWednesday ? 1 : 0) + ", " + 
                    (mbWeekdaySettingThursday ? 1 : 0) + ", " + 
                    (mbWeekdaySettingFriday ? 1 : 0) + ", " + 
                    (mbWeekdaySettingSaturday ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkPaymentTypeId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_wds = " + mnPkWorkingDaySettingsId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "wrk_day_wee = " + mnWorkingDaysWeek + ", " +
                    "wrk_hrs_day = " + mnWorkingHoursDay + ", " +
                    "b_wds_sun = " + (mbWeekdaySettingSunday ? 1 : 0) + ", " +
                    "b_wds_mon = " + (mbWeekdaySettingMonday ? 1 : 0) + ", " +
                    "b_wds_tue = " + (mbWeekdaySettingTuesday ? 1 : 0) + ", " +
                    "b_wds_wed = " + (mbWeekdaySettingWednesday ? 1 : 0) + ", " +
                    "b_wds_thu = " + (mbWeekdaySettingThursday ? 1 : 0) + ", " +
                    "b_wds_fri = " + (mbWeekdaySettingFriday ? 1 : 0) + ", " +
                    "b_wds_sat = " + (mbWeekdaySettingSaturday ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_pay = " + mnFkPaymentTypeId + ", " +
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
    public SDbWorkingDaySettings clone() throws CloneNotSupportedException {
        SDbWorkingDaySettings registry = new SDbWorkingDaySettings();

        registry.setPkWorkingDaySettingsId(this.getPkWorkingDaySettingsId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setWorkingDaysWeek(this.getWorkingDaysWeek());
        registry.setWorkingHoursDay(this.getWorkingHoursDay());
        registry.setWeekdaySettingSunday(this.isWeekdaySettingSunday());
        registry.setWeekdaySettingMonday(this.isWeekdaySettingMonday());
        registry.setWeekdaySettingTuesday(this.isWeekdaySettingTuesday());
        registry.setWeekdaySettingWednesday(this.isWeekdaySettingWednesday());
        registry.setWeekdaySettingThursday(this.isWeekdaySettingThursday());
        registry.setWeekdaySettingFriday(this.isWeekdaySettingFriday());
        registry.setWeekdaySettingSaturday(this.isWeekdaySettingSaturday());
        registry.setDeleted(this.isDeleted());
        registry.setFkPaymentTypeId(this.getFkPaymentTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

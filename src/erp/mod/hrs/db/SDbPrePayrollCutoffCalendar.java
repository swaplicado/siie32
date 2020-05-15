/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbPrePayrollCutoffCalendar extends SDbRegistryUser {

    protected int PkCalendarId;
    protected int mnYear;
    protected int mnNumber;
    protected Date mtDateCutoff;
    
    protected int mnFkPaymentTypeId;

    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbPrePayrollCutoffCalendar() {
        super(SModConsts.HRS_PRE_PAY_CUT_CAL);
    }

    public void setPkTaxTableId(int n) { PkCalendarId = n; }
    public void setYear(int n) { mnYear = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDateCutoff(Date t) { mtDateCutoff = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkPaymentTypeId(int n) { mnFkPaymentTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkTaxTableId() { return PkCalendarId; }
    public int getYear() { return mnYear; }
    public int getNumber() { return mnNumber; }
    public Date getDateCutoff() { return mtDateCutoff; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkPaymentTypeId() { return mnFkPaymentTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        PkCalendarId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { PkCalendarId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        PkCalendarId = 0;
        mnYear = 0;
        mnNumber = 0;
        mtDateCutoff = null;
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
        return "WHERE id_cal = " + PkCalendarId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cal = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        PkCalendarId = 0;

        msSql = "SELECT COALESCE(MAX(id_cal), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            PkCalendarId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
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
            PkCalendarId = resultSet.getInt("id_cal");
            mnYear = resultSet.getInt("year");
            mnNumber = resultSet.getInt("num");
            mtDateCutoff = resultSet.getDate("dt_cut");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkPaymentTypeId = resultSet.getInt("fk_tp_pay");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Finish registry reading:

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
                    PkCalendarId + ", " +
                    mnYear + ", " +
                    mnNumber + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateCutoff) + "', " +
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
                    //"id_cal = " + PkCalendarId + ", " +
                    "year = " + mnYear + ", " +
                    "num = " + mnNumber + ", " +
                    "dt_cut = '" + SLibUtils.DbmsDateFormatDate.format(mtDateCutoff) + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Finish registry saving:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbPrePayrollCutoffCalendar clone() throws CloneNotSupportedException {
        SDbPrePayrollCutoffCalendar registry = new SDbPrePayrollCutoffCalendar();

        registry.setPkTaxTableId(this.getPkTaxTableId());
        registry.setYear(this.getYear());
        registry.setNumber(this.getNumber());
        registry.setDateCutoff(this.getDateCutoff());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

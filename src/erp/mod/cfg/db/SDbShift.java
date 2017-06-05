/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Claudio Peña, Sergio Flores
 */
public class SDbShift extends SDbRegistryUser {

    protected int mnPkShiftId;
    protected String msCode;
    protected String msName;
    protected Date mtTimeStart;
    protected Date mtTimeEnd;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbShift() {
        super(SModConsts.CFGU_SHIFT);
    }

    public void setPkShiftId(int n) { mnPkShiftId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setTimeStart(Date t) { mtTimeStart = t; }
    public void setTimeEnd(Date t) { mtTimeEnd = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkShiftId() { return mnPkShiftId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public Date getTimeStart() { return mtTimeStart; }
    public Date getTimeEnd() { return mtTimeEnd; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkShiftId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkShiftId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkShiftId = 0;
        msCode = "";
        msName = "";
        mtTimeStart = null;
        mtTimeEnd = null;
        mbDeleted = false;
        mbSystem = false;
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
        return "WHERE id_shift = " + mnPkShiftId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_shift = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkShiftId = 0;

        msSql = "SELECT COALESCE(MAX(id_shift), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkShiftId = resultSet.getInt(1);
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
            mnPkShiftId = resultSet.getInt("id_shift");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mtTimeStart = resultSet.getTime("time_sta");
            mtTimeEnd = resultSet.getTime("time_end");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
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
                    mnPkShiftId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + SLibUtils.DbmsDateFormatTime.format(mtTimeStart) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatTime.format(mtTimeEnd) + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_shift = " + mnPkShiftId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "time_sta = '" + SLibUtils.DbmsDateFormatTime.format(mtTimeStart) + "', " +
                    "time_end = '" + SLibUtils.DbmsDateFormatTime.format(mtTimeEnd) + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
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
    public SDbShift clone() throws CloneNotSupportedException {
        SDbShift registry = new SDbShift();

        registry.setPkShiftId(this.getPkShiftId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setTimeStart(this.getTimeStart());
        registry.setTimeEnd(this.getTimeEnd());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

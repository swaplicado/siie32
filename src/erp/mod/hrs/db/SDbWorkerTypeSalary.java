/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

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
 * @author Sergio Flores
 */
public class SDbWorkerTypeSalary extends SDbRegistryUser {

    protected int mnPkWorkerTypeId;
    protected int mnPkSalaryId;
    protected Date mtDateStart;
    protected double mdSalary;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbWorkerTypeSalary() {
        super(SModConsts.HRS_WRK_SAL);
    }

    public void setPkWorkerTypeId(int n) { mnPkWorkerTypeId = n; }
    public void setPkSalaryId(int n) { mnPkSalaryId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setSalary(double d) { mdSalary = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkWorkerTypeId() { return mnPkWorkerTypeId; }
    public int getPkSalaryId() { return mnPkSalaryId; }
    public Date getDateStart() { return mtDateStart; }
    public double getSalary() { return mdSalary; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkWorkerTypeId = pk[0];
        mnPkSalaryId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkWorkerTypeId, mnPkSalaryId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkWorkerTypeId = 0;
        mnPkSalaryId = 0;
        mtDateStart = null;
        mdSalary = 0;
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
        return "WHERE id_tp_wrk = " + mnPkWorkerTypeId + " AND "
                + "id_sal = " + mnPkSalaryId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_tp_wrk = " + pk[0] + " AND "
                + "id_sal = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkSalaryId = 0;

        msSql = "SELECT COALESCE(MAX(id_sal), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_tp_wrk = " + mnPkWorkerTypeId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkSalaryId = resultSet.getInt(1);
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
            mnPkWorkerTypeId = resultSet.getInt("id_tp_wrk");
            mnPkSalaryId = resultSet.getInt("id_sal");
            mtDateStart = resultSet.getDate("dt_sta");
            mdSalary = resultSet.getDouble("sal");
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
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkWorkerTypeId + ", " +
                    mnPkSalaryId + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    mdSalary + ", " +
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
                    "id_tp_wrk = " + mnPkWorkerTypeId + ", " +
                    "id_sal = " + mnPkSalaryId + ", " +
                    */
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "sal = " + mdSalary + ", " +
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
    public SDbWorkerTypeSalary clone() throws CloneNotSupportedException {
        SDbWorkerTypeSalary registry = new SDbWorkerTypeSalary();

        registry.setPkWorkerTypeId(this.getPkWorkerTypeId());
        registry.setPkSalaryId(this.getPkSalaryId());
        registry.setDateStart(this.getDateStart());
        registry.setSalary(this.getSalary());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

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
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbAbsenceType extends SDbRegistryUser {

    protected int mnPkAbsenceClassId;
    protected int mnPkAbsenceTypeId;
    protected String msCode;
    protected String msName;
    protected boolean mbPayable;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkDisabilityTypeId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbAbsenceType() {
        super(SModConsts.HRSU_TP_ABS);
    }

    public void setPkAbsenceClassId(int n) { mnPkAbsenceClassId = n; }
    public void setPkAbsenceTypeId(int n) { mnPkAbsenceTypeId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setPayable(boolean b) { mbPayable = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkDisabilityTypeId_n(int n) { mnFkDisabilityTypeId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkAbsenceClassId() { return mnPkAbsenceClassId; }
    public int getPkAbsenceTypeId() { return mnPkAbsenceTypeId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public boolean isPayable() { return mbPayable; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkDisabilityTypeId_n() { return mnFkDisabilityTypeId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAbsenceClassId = pk[0];
        mnPkAbsenceTypeId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAbsenceClassId, mnPkAbsenceTypeId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkAbsenceClassId = 0;
        mnPkAbsenceTypeId = 0;
        msCode = "";
        msName = "";
        mbPayable = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkDisabilityTypeId_n = 0;
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
        return "WHERE id_cl_abs = " + mnPkAbsenceClassId + " AND "
                + "id_tp_abs = " + mnPkAbsenceTypeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cl_abs = " + pk[0] + " AND "
                + "id_tp_abs = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAbsenceTypeId = 0;

        msSql = "SELECT COALESCE(MAX(id_tp_abs), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_cl_abs = " + mnPkAbsenceClassId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAbsenceTypeId = resultSet.getInt(1);
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
            mnPkAbsenceClassId = resultSet.getInt("id_cl_abs");
            mnPkAbsenceTypeId = resultSet.getInt("id_tp_abs");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mbPayable = resultSet.getBoolean("b_pay");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkDisabilityTypeId_n = resultSet.getInt("fk_tp_dis_n");
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
                    mnPkAbsenceClassId + ", " +
                    mnPkAbsenceTypeId + ", " +
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
                    (mbPayable ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    (mnFkDisabilityTypeId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkDisabilityTypeId_n) + ", " +
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
                    "id_cl_abs = " + mnPkAbsenceClassId + ", " +
                    "id_tp_abs = " + mnPkAbsenceTypeId + ", " +
                    */
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "b_pay = " + (mbPayable ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_tp_dis_n = " + (mnFkDisabilityTypeId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkDisabilityTypeId_n) + ", " +
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
    public SDbAbsenceType clone() throws CloneNotSupportedException {
        SDbAbsenceType registry = new SDbAbsenceType();

        registry.setPkAbsenceClassId(this.getPkAbsenceClassId());
        registry.setPkAbsenceTypeId(this.getPkAbsenceTypeId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setPayable(this.isPayable());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkDisabilityTypeId_n(this.getFkDisabilityTypeId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

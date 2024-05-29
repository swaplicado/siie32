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
 * @author Sergio Flores
 */
public class SDbEmploymentSubsidy extends SDbRegistryUser {

    protected int mnPkEmploymentSubsidyId;
    protected Date mtDateStart;
    protected double mdIncomeMonthlyCap;
    protected double mdSubsidyMonthlyCap;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbEmploymentSubsidy() {
        super(SModConsts.HRS_EMPL_SUB);
    }

    public void setPkEmploymentSubsidyId(int n) { mnPkEmploymentSubsidyId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setIncomeMonthlyCap(double d) { mdIncomeMonthlyCap = d; }
    public void setSubsidyMonthlyCap(double d) { mdSubsidyMonthlyCap = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkEmploymentSubsidyId() { return mnPkEmploymentSubsidyId; }
    public Date getDateStart() { return mtDateStart; }
    public double getIncomeMonthlyCap() { return mdIncomeMonthlyCap; }
    public double getSubsidyMonthlyCap() { return mdSubsidyMonthlyCap; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEmploymentSubsidyId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEmploymentSubsidyId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkEmploymentSubsidyId = 0;
        mtDateStart = null;
        mdIncomeMonthlyCap = 0;
        mdSubsidyMonthlyCap = 0;
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
        return "WHERE id_empl_sub = " + mnPkEmploymentSubsidyId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_empl_sub = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkEmploymentSubsidyId = 0;

        msSql = "SELECT COALESCE(MAX(id_empl_sub), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkEmploymentSubsidyId = resultSet.getInt(1);
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
            mnPkEmploymentSubsidyId = resultSet.getInt("id_empl_sub");
            mtDateStart = resultSet.getDate("dt_sta");
            mdIncomeMonthlyCap = resultSet.getDouble("inc_mon_cap");
            mdSubsidyMonthlyCap = resultSet.getDouble("sub_mon_cap");
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
                    mnPkEmploymentSubsidyId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " + 
                    mdIncomeMonthlyCap + ", " + 
                    mdSubsidyMonthlyCap + ", " + 
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
                    //"id_empl_sub = " + mnPkEmploymentSubsidyId + ", " +
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "inc_mon_cap = " + mdIncomeMonthlyCap + ", " +
                    "sub_mon_cap = " + mdSubsidyMonthlyCap + ", " +
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
    public SDbEmploymentSubsidy clone() throws CloneNotSupportedException {
        SDbEmploymentSubsidy registry = new SDbEmploymentSubsidy();

        registry.setPkEmploymentSubsidyId(this.getPkEmploymentSubsidyId());
        registry.setDateStart(this.getDateStart());
        registry.setIncomeMonthlyCap(this.getIncomeMonthlyCap());
        registry.setSubsidyMonthlyCap(this.getSubsidyMonthlyCap());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

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
public class SDbLoanTypeAdjustment extends SDbRegistryUser {

    protected int mnPkLoanTypeId;
    protected int mnPkAdjustmentId;
    protected Date mtDateStart;
    protected double mdAdjustment;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbLoanTypeAdjustment() {
        super(SModConsts.HRS_TP_LOAN_ADJ);
    }

    public void setPkLoanTypeId(int n) { mnPkLoanTypeId = n; }
    public void setPkAdjustmentId(int n) { mnPkAdjustmentId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setAdjustment(double d) { mdAdjustment = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkLoanTypeId() { return mnPkLoanTypeId; }
    public int getPkAdjustmentId() { return mnPkAdjustmentId; }
    public Date getDateStart() { return mtDateStart; }
    public double getAdjustment() { return mdAdjustment; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkLoanTypeId = pk[0];
        mnPkAdjustmentId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkLoanTypeId, mnPkAdjustmentId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkLoanTypeId = 0;
        mnPkAdjustmentId = 0;
        mtDateStart = null;
        mdAdjustment = 0;
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
        return "WHERE id_tp_loan = " + mnPkLoanTypeId + " AND "
                + "id_adj = " + mnPkAdjustmentId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_tp_loan = " + pk[0] + " AND "
                + "id_adj = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAdjustmentId = 0;

        msSql = "SELECT COALESCE(MAX(id_adj), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_tp_loan = " + mnPkLoanTypeId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAdjustmentId = resultSet.getInt(1);
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
            mnPkLoanTypeId = resultSet.getInt("id_tp_loan");
            mnPkAdjustmentId = resultSet.getInt("id_adj");
            mtDateStart = resultSet.getDate("dt_sta");
            mdAdjustment = resultSet.getDouble("adj");
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
                    mnPkLoanTypeId + ", " +
                    mnPkAdjustmentId + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    mdAdjustment + ", " +
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
                    "id_tp_loan = " + mnPkLoanTypeId + ", " +
                    "id_adj = " + mnPkAdjustmentId + ", " +
                    */
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "adj = " + mdAdjustment + ", " +
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
    public SDbLoanTypeAdjustment clone() throws CloneNotSupportedException {
        SDbLoanTypeAdjustment registry = new SDbLoanTypeAdjustment();

        registry.setPkLoanTypeId(this.getPkLoanTypeId());
        registry.setPkAdjustmentId(this.getPkAdjustmentId());
        registry.setDateStart(this.getDateStart());
        registry.setAdjustment(this.getAdjustment());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

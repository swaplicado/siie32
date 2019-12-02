/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbFunctionalAreaBudget extends SDbRegistryUser {

    protected int mnPkFunctionalAreaBudgetId;
    protected int mnPeriodYear;
    protected int mnPeriodMonth;
    protected double mdBudget;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkFunctionalAreaId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbFunctionalAreaBudget() {
        super(SModConsts.TRN_FUNC_BUDGET);
    }

    public void setPkFunctionalAreaBudgetId(int n) { mnPkFunctionalAreaBudgetId = n; }
    public void setPeriodYear(int n) { mnPeriodYear = n; }
    public void setPeriodMonth(int n) { mnPeriodMonth = n; }
    public void setBudget(double d) { mdBudget = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkFunctionalAreaId(int n) { mnFkFunctionalAreaId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkFunctionalAreaBudgetId() { return mnPkFunctionalAreaBudgetId; }
    public int getPeriodYear() { return mnPeriodYear; }
    public int getPeriodMonth() { return mnPeriodMonth; }
    public double getBudget() { return mdBudget; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkFunctionalAreaId() { return mnFkFunctionalAreaId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkFunctionalAreaBudgetId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkFunctionalAreaBudgetId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkFunctionalAreaBudgetId = 0;
        mnPeriodYear = 0;
        mnPeriodMonth = 0;
        mdBudget = 0;
        mbDeleted = false;
        mnFkFunctionalAreaId = 0;
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
        return "WHERE id_budget = " + mnPkFunctionalAreaBudgetId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_budget = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        mnPkFunctionalAreaBudgetId = 0;

        msSql = "SELECT COALESCE(MAX(id_budget), 0) + 1 FROM " + getSqlTable() + " ";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(msSql)) {
            if (resultSet.next()) {
                mnPkFunctionalAreaBudgetId = resultSet.getInt(1);
            }
        }
    }
    
    @Override
    public void verifyRegistryNew(final SGuiSession session) throws SQLException, Exception {
        msSql = "SELECT id_budget "
                + "FROM " + getSqlTable() + " "
                + "WHERE period_year = " + mnPeriodYear + " AND period_month = " + mnPeriodMonth + " AND "
                + "fk_func = " + mnFkFunctionalAreaId + ";";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(msSql)) {
            if (resultSet.next()) {
                // preserve data:
                SDbFunctionalAreaBudget clone = clone();
                
                // recover existing registry:
                read(session, new int[] { resultSet.getInt(1) });
                
                // update data:
                this.setBudget(clone.getBudget());
                this.setDeleted(clone.isDeleted());
                this.setRegistryEdited(true);
            }
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        
        try (ResultSet resultSet = session.getStatement().executeQuery(msSql)) {
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                mnPkFunctionalAreaBudgetId = resultSet.getInt("id_budget");
                mnPeriodYear = resultSet.getInt("period_year");
                mnPeriodMonth = resultSet.getInt("period_month");
                mdBudget = resultSet.getDouble("budget");
                mbDeleted = resultSet.getBoolean("b_del");
                mnFkFunctionalAreaId = resultSet.getInt("fk_func");
                mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
                mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
                mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
                mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
                
                mbRegistryNew = false;
            }
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        verifyRegistryNew(session);

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkFunctionalAreaBudgetId + ", " + 
                    mnPeriodYear + ", " + 
                    mnPeriodMonth + ", " + 
                    mdBudget + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkFunctionalAreaId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_budget = " + mnPkFunctionalAreaBudgetId + ", " +
                    "period_year = " + mnPeriodYear + ", " +
                    "period_month = " + mnPeriodMonth + ", " +
                    "budget = " + mdBudget + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_func = " + mnFkFunctionalAreaId + ", " +
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
    public SDbFunctionalAreaBudget clone() throws CloneNotSupportedException {
        SDbFunctionalAreaBudget registry = new SDbFunctionalAreaBudget();

        registry.setPkFunctionalAreaBudgetId(this.getPkFunctionalAreaBudgetId());
        registry.setPeriodYear(this.getPeriodYear());
        registry.setPeriodMonth(this.getPeriodMonth());
        registry.setBudget(this.getBudget());
        registry.setDeleted(this.isDeleted());
        registry.setFkFunctionalAreaId(this.getFkFunctionalAreaId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    /**
     * Get budget for given period year and month and functional area.
     * @param session GUI session.
     * @param periodYear Period year.
     * @param periodMonth Period month.
     * @param functionalAreaId Functional area.
     * @return Requested budget if available, otherwise <code>null</code>.
     * @throws Exception 
     */
    public static Double getBudget(final SGuiSession session, final int periodYear, final int periodMonth, final int functionalAreaId) throws Exception {
        return getBudget(session.getStatement(), periodYear, periodMonth, functionalAreaId);
    }
    
    /**
     * Get budget for given period year and month and functional area.
     * @param statement Statement of database connection.
     * @param periodYear Period year.
     * @param periodMonth Period month.
     * @param functionalAreaId Functional area.
     * @return Requested budget if available, otherwise <code>null</code>.
     * @throws Exception 
     */
    public static Double getBudget(final Statement statement, final int periodYear, final int periodMonth, final int functionalAreaId) throws Exception {
        Double budget = null;
        
        String sql = "SELECT budget "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_FUNC_BUDGET) + " "
                + "WHERE NOT b_del AND period_year = " + periodYear + " AND period_month = " + periodMonth + " AND "
                + "fk_func = " + functionalAreaId + ";"; // period year and month and functional area are properly unitarily indexed
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                budget = resultSet.getDouble(1);
            }
        }
        
        return budget;
    }
}

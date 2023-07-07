/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbFunctionalArea;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import sa.lib.SLibTimeConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbFunctionalAreaBudgets extends SDbRegistryUser {

    protected int mnPeriodYear;
    protected ArrayList<SDbFunctionalArea> maFunctionalAreas;
    protected ArrayList<SDbFunctionalAreaBudget> maBudgets;
    
    /**
     * All monthly budgets for year of all functional areas.
     */
    public SDbFunctionalAreaBudgets() {
        super(SModConsts.TRNX_FUNC_BUDGETS);
    }

    public void setPeriodYear(int n) { mnPeriodYear = n; }

    public int getPeriodYear() { return mnPeriodYear; }
    
    public ArrayList<SDbFunctionalArea> getFunctionalAreas() { return maFunctionalAreas; }
    public ArrayList<SDbFunctionalAreaBudget> getBudgets() { return maBudgets; }
    
    private SDbFunctionalAreaBudget getBudget(final int functionalAreaId, final int periodMonth) {
        SDbFunctionalAreaBudget budget = null;
        
        for (SDbFunctionalAreaBudget fab : maBudgets) {
            if (fab.getFkFunctionalAreaId() == functionalAreaId && fab.getPeriodYear() == mnPeriodYear && fab.getPeriodMonth() == periodMonth) {
                budget = fab;
                break;
            }
        }
        
        return budget;
    }
    
    public void readFunctionalAreas(final SGuiSession session) throws Exception {
        maFunctionalAreas.clear();
        
        String sql = "SELECT id_func "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " "
                + "WHERE id_func <> " + SModSysConsts.CFGU_FUNC_NA + " "
                + "ORDER BY name, id_func;";

        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                SDbFunctionalArea functionalArea = (SDbFunctionalArea) session.readRegistry(SModConsts.CFGU_FUNC, new int[] { resultSet.getInt(1) });
                maFunctionalAreas.add(functionalArea);
            }
        }

    }

    public ArrayList<SRowFunctionalAreaBudgets> createRowFunctionalAreaBudgetses() {
        ArrayList<SRowFunctionalAreaBudgets> rowFunctionalAreaBudgetses = new ArrayList<>();
        
        for (SDbFunctionalArea fa : maFunctionalAreas) {
            if (!fa.isDeleted()) {
                Double[] budgets = new Double[SLibTimeConsts.MONTHS];
                
                for (int month = 1; month <= SLibTimeConsts.MONTHS; month++) {
                    SDbFunctionalAreaBudget budget = getBudget(fa.getPkFunctionalAreaId(), month);
                    if (budget != null && !budget.isDeleted()) {
                        budgets[month - 1] = budget.getBudget();
                    }
                }
                
                SRowFunctionalAreaBudgets row = new SRowFunctionalAreaBudgets(fa, budgets);
                rowFunctionalAreaBudgetses.add(row);
            }
        }
        
        return rowFunctionalAreaBudgetses;
    }
    
    public void processRowFunctionalAreaBudgetses(ArrayList<SRowFunctionalAreaBudgets> rowFunctionalAreaBudgetses) {
        for (SRowFunctionalAreaBudgets row : rowFunctionalAreaBudgetses) {
            for (int month = 1; month <= SLibTimeConsts.MONTHS; month++) {
                Double budget = row.getBudgets()[month - 1];
                
                if (budget != null) {
                    SDbFunctionalAreaBudget fab = getBudget(row.getFunctionalArea().getPkFunctionalAreaId(), month);
                    
                    if (fab != null) {
                        fab.setDeleted(false);
                        
                        fab.setRegistryEdited(true);
                    }
                    else {
                        fab = new SDbFunctionalAreaBudget();
                        fab.setPeriodYear(mnPeriodYear);
                        fab.setPeriodMonth(month);
                        fab.setFkFunctionalAreaId(row.getFunctionalArea().getPkFunctionalAreaId());
                        
                        maBudgets.add(fab);
                    }
                    
                    fab.setBudget(budget);
                }
            }
        }
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPeriodYear = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPeriodYear };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPeriodYear = 0;
        
        maFunctionalAreas = new ArrayList<>();
        maBudgets = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        setPrimaryKey(pk); // sets period year!
        
        // read all functional areas:
        
        readFunctionalAreas(session);
        
        // read all functional area monthly-budgets in period year:
        
        msSql = "SELECT id_budget "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_FUNC_BUDGET) + " "
                + "WHERE period_year = " + mnPeriodYear + " "
                + "ORDER BY id_budget;";
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbFunctionalAreaBudget budget = (SDbFunctionalAreaBudget) session.readRegistry(SModConsts.TRN_FUNC_BUDGET, new int[] { resultSet.getInt(1) });
                maBudgets.add(budget);
            }
        }
        
        if (!maBudgets.isEmpty()) {
            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        // delete all existing monthly-budgets in period year:
        
        msSql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_FUNC_BUDGET) + " SET "
                + "b_del = 1, "
                + "fk_usr_upd = " + session.getUser().getPkUserId() + ", "
                + "ts_usr_upd = NOW() "
                + "WHERE NOT b_del AND period_year = " + mnPeriodYear + ";";

        session.getStatement().execute(msSql);
        
        // save all current monthly-budgets:
        
        for (SDbFunctionalAreaBudget fab : maBudgets) {
            if (fab.isRegistryNew() || fab.isRegistryEdited()) {
                fab.save(session);
            }
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbFunctionalAreaBudgets clone() throws CloneNotSupportedException {
        SDbFunctionalAreaBudgets registry = new SDbFunctionalAreaBudgets();

        registry.setPeriodYear(this.getPeriodYear());
        
        for (SDbFunctionalArea fa : maFunctionalAreas) {
            registry.getFunctionalAreas().add(fa.clone());
        }

        for (SDbFunctionalAreaBudget fab : maBudgets) {
            registry.getBudgets().add(fab.clone());
        }

        registry.setRegistryNew(this.isRegistryNew()); // left only for consistence, not really needed!
        return registry;
    }
    
    /**
     * Check if budgets exist, at least one, for given period year.
     * @param session GUI session.
     * @param periodYear Period jear.
     * @return <code>true</code> if budgets exist, at least one, for given period year, otherwise <code>false</code>.
     * @throws Exception 
     */
    public static boolean existBudgets(final SGuiSession session, final int periodYear) throws Exception {
        boolean existBudgets = false;
        
        String sql = "SELECT COUNT(*) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_FUNC_BUDGET) + " "
                + "WHERE NOT b_del AND period_year = " + periodYear + ";";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                existBudgets = true;
            }
        }
        
        return existBudgets;
    }
}

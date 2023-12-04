/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbPackExpenses extends SDbRegistryUser {

    protected int mnPkPackExpensesId;
    protected String msCode;
    protected String msName;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbPackExpensesItem> maChildItems;

    public SDbPackExpenses() {
        super(SModConsts.HRSU_PACK_EXP);
    }

    public void setPkPackExpensesId(int n) { mnPkPackExpensesId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkPackExpensesId() { return mnPkPackExpensesId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public ArrayList<SDbPackExpensesItem> getChildItems() { return maChildItems; }
    
    /**
     * Get expense item by expense type.
     * @param expenseType ID of expense type. Constants declared in SModSysConsts.HRSS_TP_EXP_
     * @return 
     */
    public SDbPackExpensesItem getChildItem(final int expenseType) {
        SDbPackExpensesItem item = null;
        
        for (SDbPackExpensesItem child : maChildItems) {
            if (child.getPkExpenseTypeId() == expenseType) {
                item = child;
                break;
            }
        }
        
        return item;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPackExpensesId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPackExpensesId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkPackExpensesId = 0;
        msCode = "";
        msName = "";
        mbDeleted = false;
        mbSystem = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildItems = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_pack_exp = " + mnPkPackExpensesId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_pack_exp = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkPackExpensesId = 0;

        msSql = "SELECT COALESCE(MAX(id_pack_exp), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkPackExpensesId = resultSet.getInt(1);
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
            mnPkPackExpensesId = resultSet.getInt("id_pack_exp");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // read children:
            
            try (Statement statement = session.getStatement().getConnection().createStatement()) {
                msSql = "SELECT id_tp_exp "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_PACK_EXP_ITEM) + " "
                        + "WHERE id_pack_exp = " + mnPkPackExpensesId + " "
                        + "ORDER BY id_tp_exp;";
                
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    SDbPackExpensesItem child = new SDbPackExpensesItem();
                    child.read(session, new int[] { mnPkPackExpensesId, resultSet.getInt(1) });
                    maChildItems.add(child);
                }
            }
            
            // finish registry:

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
                    mnPkPackExpensesId + ", " +
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
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
                    //"id_pack_exp = " + mnPkPackExpensesId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // save children:
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_PACK_EXP_ITEM) + " "
                + "WHERE id_pack_exp = " + mnPkPackExpensesId + ";";
        
        session.getStatement().execute(msSql);
        
        for (SDbPackExpensesItem child : maChildItems) {
            child.setPkPackExpensesId(mnPkPackExpensesId);
            child.setRegistryNew(true);
            child.save(session);
        }
        
        // finish registry:
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbPackExpenses clone() throws CloneNotSupportedException {
        SDbPackExpenses registry = new SDbPackExpenses();

        registry.setPkPackExpensesId(this.getPkPackExpensesId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        // clone children:
        
        for (SDbPackExpensesItem child : maChildItems) {
            registry.getChildItems().add(child.clone());
        }
        
        // finish registry:

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

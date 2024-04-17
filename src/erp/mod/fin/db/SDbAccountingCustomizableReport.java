/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

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
 * @author Isabel Servin
 */
public class SDbAccountingCustomizableReport extends SDbRegistryUser {
    
    protected int mnPkReportCustomizableAccountId;
    protected String msName;
    protected String msDescription;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    ArrayList<SDbAccountingCustomizableReportAccount> mlDbmsAccCusReportAccounts;
    ArrayList<SDbAccountingCustomizableReportCostCenter> mlDbmsAccCusReportCostCenters;
    ArrayList<SDbAccountingCustomizableReportUser> mlDbmsAccCusReportUsers;

    public SDbAccountingCustomizableReport() {
        super(SModConsts.FIN_REP_CUS_ACC);
        initRegistry();
    }
    
    public void setPkReportCustomizableAccountId(int n) { mnPkReportCustomizableAccountId = n; }
    public void setName(String s) { msName = s; }
    public void setDescription(String s) { msDescription = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkReportCustomizableAccountId() { return mnPkReportCustomizableAccountId; }
    public String getName() { return msName; }
    public String getDescription() { return msDescription; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public ArrayList<SDbAccountingCustomizableReportAccount> getDbmsAccCusReportAccounts() { return mlDbmsAccCusReportAccounts; }
    public ArrayList<SDbAccountingCustomizableReportCostCenter> getDbmsAccCusReportCostCenters() { return mlDbmsAccCusReportCostCenters; }
    public ArrayList<SDbAccountingCustomizableReportUser> getDbmsAccCusReportUsers() { return mlDbmsAccCusReportUsers; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkReportCustomizableAccountId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkReportCustomizableAccountId }; 
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkReportCustomizableAccountId = 0;
        msName = "";
        msDescription = "";
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        mlDbmsAccCusReportAccounts = new ArrayList<>();
        mlDbmsAccCusReportCostCenters = new ArrayList<>();
        mlDbmsAccCusReportUsers = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_rep_cus_acc = " + mnPkReportCustomizableAccountId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_rep_cus_acc = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkReportCustomizableAccountId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_rep_cus_acc), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if(resultSet.next()) {
            mnPkReportCustomizableAccountId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statement;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkReportCustomizableAccountId = resultSet.getInt("id_rep_cus_acc");
            msName = resultSet.getString("name");
            msDescription = resultSet.getString("description");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // Read accounts
            
            statement = session.getDatabase().getConnection().createStatement();
            
            mlDbmsAccCusReportAccounts = new ArrayList<>();
            msSql = "SELECT id_acc_start, id_acc_end FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REP_CUS_ACC_ACC) + " " + 
                    "WHERE id_rep_cus_acc = " + mnPkReportCustomizableAccountId;
            ResultSet resultSetAux = statement.executeQuery(msSql);
            while (resultSetAux.next()) {
                SDbAccountingCustomizableReportAccount o = new SDbAccountingCustomizableReportAccount();
                o.read(session, new int[] { mnPkReportCustomizableAccountId, resultSetAux.getInt(1), resultSetAux.getInt(2) });
                mlDbmsAccCusReportAccounts.add(o);
            }
            
            // Read cost centers
            
            mlDbmsAccCusReportCostCenters = new ArrayList<>();
            msSql = "SELECT id_cc_start, id_cc_end FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REP_CUS_ACC_CC) + " " + 
                    "WHERE id_rep_cus_acc = " + mnPkReportCustomizableAccountId;
            resultSetAux = statement.executeQuery(msSql);
            while (resultSetAux.next()) {
                SDbAccountingCustomizableReportCostCenter o = new SDbAccountingCustomizableReportCostCenter();
                o.read(session, new int[] { mnPkReportCustomizableAccountId, resultSetAux.getInt(1), resultSetAux.getInt(2) });
                mlDbmsAccCusReportCostCenters.add(o);
            }
            
            // Read users
            
            mlDbmsAccCusReportUsers = new ArrayList<>();
            msSql = "SELECT id_usr FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REP_CUS_ACC_USR) + " " + 
                    "WHERE id_rep_cus_acc = " + mnPkReportCustomizableAccountId;
            resultSetAux = statement.executeQuery(msSql);
            while (resultSetAux.next()) {
                SDbAccountingCustomizableReportUser o = new SDbAccountingCustomizableReportUser();
                o.read(session, new int[] { mnPkReportCustomizableAccountId, resultSetAux.getInt(1) });
                mlDbmsAccCusReportUsers.add(o);
            }
            
            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkReportCustomizableAccountId + ", " + 
                    "'" + msName + "', " + 
                    "'" + msDescription + "', " + 
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
                    //"id_rep_cus_acc = " + mnPkReportCustomizableAccountId + ", " +
                    "name = '" + msName + "', " +
                    "description = '" + msDescription + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REP_CUS_ACC_ACC) + " " + 
                    "WHERE id_rep_cus_acc = " + mnPkReportCustomizableAccountId;
        session.getStatement().execute(msSql);
        for (SDbAccountingCustomizableReportAccount o : mlDbmsAccCusReportAccounts) {
            o.setPkReportCustomizableAccountId(mnPkReportCustomizableAccountId);
            o.setRegistryNew(true);
            o.save(session);
        }
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REP_CUS_ACC_CC) + " " + 
                    "WHERE id_rep_cus_acc = " + mnPkReportCustomizableAccountId;
        session.getStatement().execute(msSql);
        for (SDbAccountingCustomizableReportCostCenter o : mlDbmsAccCusReportCostCenters) {
            o.setPkReportCustomizableAccountId(mnPkReportCustomizableAccountId);
            o.setRegistryNew(true);
            o.save(session);
        }
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REP_CUS_ACC_USR) + " " + 
                    "WHERE id_rep_cus_acc = " + mnPkReportCustomizableAccountId;
        session.getStatement().execute(msSql);
        for (SDbAccountingCustomizableReportUser o : mlDbmsAccCusReportUsers) {
            o.setPkReportCustomizableAccountId(mnPkReportCustomizableAccountId);
            o.setRegistryNew(true);
            o.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbAccountingCustomizableReport clone() throws CloneNotSupportedException {
        SDbAccountingCustomizableReport registry = new SDbAccountingCustomizableReport();
        
        registry.setPkReportCustomizableAccountId(this.getPkReportCustomizableAccountId());
        registry.setName(this.getName());
        registry.setDescription(this.getDescription());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        for (SDbAccountingCustomizableReportAccount o : this.getDbmsAccCusReportAccounts()) {
            registry.getDbmsAccCusReportAccounts().add(o);
        }
        
        for (SDbAccountingCustomizableReportCostCenter o : this.getDbmsAccCusReportCostCenters()) {
            registry.getDbmsAccCusReportCostCenters().add(o);
        }
        
        for (SDbAccountingCustomizableReportUser o : this.getDbmsAccCusReportUsers()) {
            registry.getDbmsAccCusReportUsers().add(o);
        }

        return registry;
    }    
}

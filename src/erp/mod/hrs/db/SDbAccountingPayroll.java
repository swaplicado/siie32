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
 * @author Juan Barajas, Sergio Flores
 */
public class SDbAccountingPayroll extends SDbRegistryUser {
    
    protected int mnPkPayrollId;
    protected int mnPkAccountingId;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected ArrayList<SDbAccountingPayrollReceipt> maChildReceipts;
    
    protected boolean mbAuxAccountingGradual;

    public SDbAccountingPayroll() {
        super(SModConsts.HRS_ACC_PAY);
    }

    public void setPkPayrollId(int n) { mnPkPayrollId = n; }
    public void setPkAccountingId(int n) { mnPkAccountingId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkPayrollId() { return mnPkPayrollId; }
    public int getPkAccountingId() { return mnPkAccountingId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public ArrayList<SDbAccountingPayrollReceipt> getChildReceipts() { return maChildReceipts; }
    
    public void setAuxAccountingGradual(boolean b) { mbAuxAccountingGradual = b; }

    public boolean isAuxAccountingGradual() { return mbAuxAccountingGradual; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPayrollId = pk[0];
        mnPkAccountingId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPayrollId, mnPkAccountingId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkPayrollId = 0;
        mnPkAccountingId = 0;
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        maChildReceipts = new ArrayList<>();
        
        mbAuxAccountingGradual = false;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_pay = " + mnPkPayrollId + " AND "
                + "id_acc = " + mnPkAccountingId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_pay = " + pk[0] + " AND "
                + "id_acc = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAccountingId = 0;

        msSql = "SELECT COALESCE(MAX(id_acc), 0) + 1 FROM " + getSqlTable() + " WHERE id_pay = " + mnPkPayrollId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAccountingId = resultSet.getInt(1);
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
            mnPkPayrollId = resultSet.getInt("id_pay");
            mnPkAccountingId = resultSet.getInt("id_acc");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_emp "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_PAY_RCP) + " "
                    + "WHERE id_pay = " + mnPkPayrollId + " AND id_acc = " + mnPkAccountingId + ";";
            
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbAccountingPayrollReceipt receipt = new SDbAccountingPayrollReceipt();
                receipt.read(session, new int[] { mnPkPayrollId, mnPkAccountingId, resultSet.getInt(1) });
                maChildReceipts.add(receipt);
            }
            
            msSql = "SELECT b_acc_grad "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " "
                    + "WHERE id_pay = " + mnPkPayrollId + ";";
            
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                mbAuxAccountingGradual = resultSet.getBoolean("b_acc_grad");
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
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkPayrollId + ", " + 
                    mnPkAccountingId + ", " + 
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
                    "id_pay = " + mnPkPayrollId + ", " +
                    "id_acc = " + mnPkAccountingId + ", " +
                    */
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Delete previous registries:

        if (!mbAuxAccountingGradual) {
            msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_PAY_RCP) + " " +
                        "WHERE id_pay = " + mnPkPayrollId + " AND id_acc = " + mnPkAccountingId + ";";

            session.getStatement().execute(msSql);
        }
        
        msSql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_PAY) + " SET b_del = 1 " +
                    "WHERE id_pay = " + mnPkPayrollId + " AND id_acc < " + mnPkAccountingId + ";";
        
        session.getStatement().execute(msSql);
        
        // Save payroll receips:
        
        for (SDbAccountingPayrollReceipt receipt : maChildReceipts) {
            receipt.setPkPayrollId(mnPkPayrollId);
            receipt.setPkAccountingId(mnPkAccountingId);
            receipt.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbAccountingPayroll clone() throws CloneNotSupportedException {
        SDbAccountingPayroll registry = new SDbAccountingPayroll();

        registry.setPkPayrollId(this.getPkPayrollId());
        registry.setPkAccountingId(this.getPkAccountingId());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        for (SDbAccountingPayrollReceipt receipt : this.getChildReceipts()) {
            registry.getChildReceipts().add(receipt.clone());
        }
        
        registry.setAuxAccountingGradual(this.isAuxAccountingGradual());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

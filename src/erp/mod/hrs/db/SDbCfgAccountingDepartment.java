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
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbCfgAccountingDepartment extends SDbRegistryUser {

    protected int mnPkDepartmentId;
    //protected boolean mbDeleted;
    protected int mnFkExpenseTypeId;
    protected int mnFkAccountId;
    protected int mnFkBizPartnerId_n;
    protected int mnFkTaxBasicId_n;
    protected int mnFkTaxTaxId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbCfgAccountingDepartment() {
        super(SModConsts.HRS_CFG_ACC_DEP);
    }

    public void setPkDepartmentId(int n) { mnPkDepartmentId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkExpenseTypeId(int n) { mnFkExpenseTypeId = n; }
    public void setFkAccountId(int n) { mnFkAccountId = n; }
    public void setFkBizPartnerId_n(int n) { mnFkBizPartnerId_n = n; }
    public void setFkTaxBasicId_n(int n) { mnFkTaxBasicId_n = n; }
    public void setFkTaxTaxId_n(int n) { mnFkTaxTaxId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkDepartmentId() { return mnPkDepartmentId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkExpenseTypeId() { return mnFkExpenseTypeId; }
    public int getFkAccountId() { return mnFkAccountId; }
    public int getFkBizPartnerId_n() { return mnFkBizPartnerId_n; }
    public int getFkTaxBasicId_n() { return mnFkTaxBasicId_n; }
    public int getFkTaxTaxId_n() { return mnFkTaxTaxId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    @Override
    public boolean canSave(final SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);
        
        if (can) {
            can = SHrsFinUtils.validateAccount(session, mnFkAccountId, 0, mnFkBizPartnerId_n, 0, mnFkTaxBasicId_n, mnFkTaxTaxId_n);
        }
        
        return can;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDepartmentId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDepartmentId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDepartmentId = 0;
        mbDeleted = false;
        mnFkExpenseTypeId = 0;
        mnFkAccountId = 0;
        mnFkBizPartnerId_n = 0;
        mnFkTaxBasicId_n = 0;
        mnFkTaxTaxId_n = 0;
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
        return "WHERE id_dep = " + mnPkDepartmentId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_dep = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
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
            mnPkDepartmentId = resultSet.getInt("id_dep");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkExpenseTypeId = resultSet.getInt("fk_tp_exp");
            mnFkAccountId = resultSet.getInt("fk_acc");
            mnFkBizPartnerId_n = resultSet.getInt("fk_bp_n");
            mnFkTaxBasicId_n = resultSet.getInt("fk_tax_bas_n");
            mnFkTaxTaxId_n = resultSet.getInt("fk_tax_tax_n");
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
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkDepartmentId + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkExpenseTypeId + ", " + 
                    mnFkAccountId + ", " + 
                    (mnFkBizPartnerId_n == 0 ? "NULL" : mnFkBizPartnerId_n) + ", " + 
                    (mnFkTaxBasicId_n == 0 ? "NULL" : mnFkTaxBasicId_n) + ", " + 
                    (mnFkTaxTaxId_n  == 0 ? "NULL" : mnFkTaxTaxId_n) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_dep = " + mnPkDepartmentId + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_exp = " + mnFkExpenseTypeId + ", " +
                    "fk_acc = " + mnFkAccountId + ", " +
                    "fk_bp_n = " + (mnFkBizPartnerId_n == 0 ? "NULL" : mnFkBizPartnerId_n) + ", " +
                    "fk_tax_bas_n = " + (mnFkTaxBasicId_n == 0 ? "NULL" : mnFkTaxBasicId_n) + ", " +
                    "fk_tax_tax_n = " + (mnFkTaxTaxId_n  == 0 ? "NULL" : mnFkTaxTaxId_n) + ", " +
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
    public SDbCfgAccountingDepartment clone() throws CloneNotSupportedException {
        SDbCfgAccountingDepartment registry = new SDbCfgAccountingDepartment();

        registry.setPkDepartmentId(this.getPkDepartmentId());
        registry.setDeleted(this.isDeleted());
        registry.setFkExpenseTypeId(this.getFkExpenseTypeId());
        registry.setFkAccountId(this.getFkAccountId());
        registry.setFkBizPartnerId_n(this.getFkBizPartnerId_n());
        registry.setFkTaxBasicId_n(this.getFkTaxBasicId_n());
        registry.setFkTaxTaxId_n(this.getFkTaxTaxId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(true);
        return registry;
    }
}

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
public class SDbCfgAccountingEmployeeDeduction extends SDbRegistryUser {

    protected int mnPkEmployeeId;
    protected int mnPkDeductionId;
    //protected boolean mbDeleted;
    protected int mnFkBizPartnerId;
    protected int mnFkAccountingRecordTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbCfgAccountingEmployeeDeduction() {
        super(SModConsts.HRS_CFG_ACC_EMP_DED);
    }

    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setPkDeductionId(int n) { mnPkDeductionId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkAccountingRecordTypeId(int n) { mnFkAccountingRecordTypeId = n; }
    public void setFkBizPartnerId(int n) { mnFkBizPartnerId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getPkDeductionId() { return mnPkDeductionId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkBizPartnerId() { return mnFkBizPartnerId; }
    public int getFkAccountingRecordTypeId() { return mnFkAccountingRecordTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEmployeeId = pk[0];
        mnPkDeductionId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEmployeeId, mnPkDeductionId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkEmployeeId = 0;
        mnPkDeductionId = 0;
        mbDeleted = false;
        mnFkBizPartnerId = 0;
        mnFkAccountingRecordTypeId = 0;
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
        return "WHERE id_emp = " + mnPkEmployeeId + " "
                + "AND id_ded = " + mnPkDeductionId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_emp = " + pk[0] + " "
                + "AND id_ded = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {

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
            mnPkEmployeeId = resultSet.getInt("id_emp");
            mnPkDeductionId = resultSet.getInt("id_ded");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkBizPartnerId = resultSet.getInt("fk_bp");
            mnFkAccountingRecordTypeId = resultSet.getInt("fk_tp_acc_rec");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
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
            verifyRegistryNew(session);
        }
        
        if (mbRegistryNew) {
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkEmployeeId + ", " + 
                    mnPkDeductionId + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkBizPartnerId + ", " + 
                    mnFkAccountingRecordTypeId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_emp = " + mnPkEmployeeId + ", " +
                    //"id_ded = " + mnPkDeductionId + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_bp = " + mnFkBizPartnerId + ", " +
                    "fk_tp_acc_rec = " + mnFkAccountingRecordTypeId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // finish registry:
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbCfgAccountingEmployeeDeduction clone() throws CloneNotSupportedException {
        SDbCfgAccountingEmployeeDeduction registry = new SDbCfgAccountingEmployeeDeduction();

        registry.setPkEmployeeId(this.getPkEmployeeId());
        registry.setPkDeductionId(this.getPkDeductionId());
        registry.setDeleted(this.isDeleted());
        registry.setFkBizPartnerId(this.getFkBizPartnerId());
        registry.setFkAccountingRecordTypeId(this.getFkAccountingRecordTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        // finish registry:

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

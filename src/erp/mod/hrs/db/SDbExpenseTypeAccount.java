/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.data.SDataConstantsSys;
import erp.mfin.data.SDataAccount;
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
public class SDbExpenseTypeAccount extends SDbRegistryUser {

    protected int mnPkExpenseTypeId;
    //protected boolean mbDeleted;
    protected int mnFkAccountId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbExpenseTypeAccount() {
        super(SModConsts.HRS_TP_EXP_ACC);
    }

    public void setPkExpenseTypeId(int n) { mnPkExpenseTypeId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkAccountId(int n) { mnFkAccountId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkExpenseTypeId() { return mnPkExpenseTypeId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkAccountId() { return mnFkAccountId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkExpenseTypeId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkExpenseTypeId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkExpenseTypeId = 0;
        mbDeleted = false;
        mnFkAccountId = 0;
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
        return "WHERE id_tp_exp = " + mnPkExpenseTypeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_tp_exp = " + pk[0] + " ";
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
            mnPkExpenseTypeId = resultSet.getInt("id_tp_exp");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkAccountId = resultSet.getInt("fk_acc");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // finish registry:

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    /**
     * Validate own bookkeeping account.
     * @param session GUI session.
     * @return
     * @throws Exception 
     */
    public boolean validateAccount(final SGuiSession session) throws Exception {
        boolean valid = true;
        
        if (mnFkAccountId == 0 || mnFkAccountId == SDataConstantsSys.NA) {
            throw new Exception("Se debe especificar una cuenta contable.");
        }

        valid = SHrsFinUtils.validateAccount(session, mnFkAccountId, -1, -1, -1, 0, 0);
        
        if (valid) {
            SDataAccount account = SHrsFinUtils.getAccount(session, mnFkAccountId);
            SDataAccount accountLedger = SHrsFinUtils.getAccountLedger(session, account);

            if (!SHrsFinUtils.isAccountTypeForResults(accountLedger)) {
                throw new Exception("La cuenta contable debe ser de resultados.");
            }
        }
        
        return valid;
    }
    
    @Override
    public boolean canSave(final SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);
        
        if (can) {
            if (mbRegistryNew) {
                // check non-duplication of configuration:
                if (countExistingRegistries(session, mnPkExpenseTypeId) > 0) {
                    throw new Exception("Ya hay un registro de configuración para este tipo de gasto '" + session.readField(SModConsts.HRSU_TP_EXP, new int[] { mnPkExpenseTypeId }, FIELD_NAME) + "'.");
                }
            }
            
            can = validateAccount(session);
        }
        
        return can;
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
                    mnPkExpenseTypeId + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkAccountId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_tp_exp = " + mnPkExpenseTypeId + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_acc = " + mnFkAccountId + ", " +
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
    public SDbExpenseTypeAccount clone() throws CloneNotSupportedException {
        SDbExpenseTypeAccount registry = new SDbExpenseTypeAccount();

        registry.setPkExpenseTypeId(this.getPkExpenseTypeId());
        registry.setDeleted(this.isDeleted());
        registry.setFkAccountId(this.getFkAccountId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    /**
     * Count existing registries of accounting configuration for this earning.
     * @param session GUI session.
     * @param idToBeCounted Earning ID to counted.
     * @return
     * @throws Exception 
     */
    public static final int countExistingRegistries(final SGuiSession session, final int idToBeCounted) throws Exception {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.HRS_TP_EXP_ACC) + " WHERE id_tp_exp = " + idToBeCounted + ";";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
                
        return count;
    }
}

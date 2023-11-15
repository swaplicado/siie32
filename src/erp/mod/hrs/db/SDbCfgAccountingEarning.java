/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.data.SDataConstantsSys;
import erp.mfin.data.SDataAccount;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
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
public class SDbCfgAccountingEarning extends SDbRegistryUser {

    protected int mnPkEarningId;
    //protected boolean mbDeleted;
    protected int mnFkAccountId;
    protected int mnFkBizPartnerId_n;
    protected int mnFkTaxBasicId_n;
    protected int mnFkTaxTaxId_n;
    protected int mnFkPackExpensesId;
    protected int mnFkPackCostCentersId;
    protected int mnFkAccountingRecordTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbCfgAccountingEarning() {
        super(SModConsts.HRS_CFG_ACC_EAR);
    }

    public void setPkEarningId(int n) { mnPkEarningId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkAccountId(int n) { mnFkAccountId = n; }
    public void setFkBizPartnerId_n(int n) { mnFkBizPartnerId_n = n; }
    public void setFkTaxBasicId_n(int n) { mnFkTaxBasicId_n = n; }
    public void setFkTaxTaxId_n(int n) { mnFkTaxTaxId_n = n; }
    public void setFkPackExpensesId(int n) { mnFkPackExpensesId = n; }
    public void setFkPackCostCentersId(int n) { mnFkPackCostCentersId = n; }
    public void setFkAccountingRecordTypeId(int n) { mnFkAccountingRecordTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkEarningId() { return mnPkEarningId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkAccountId() { return mnFkAccountId; }
    public int getFkBizPartnerId_n() { return mnFkBizPartnerId_n; }
    public int getFkTaxBasicId_n() { return mnFkTaxBasicId_n; }
    public int getFkTaxTaxId_n() { return mnFkTaxTaxId_n; }
    public int getFkPackExpensesId() { return mnFkPackExpensesId; }
    public int getFkPackCostCentersId() { return mnFkPackCostCentersId; }
    public int getFkAccountingRecordTypeId() { return mnFkAccountingRecordTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEarningId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEarningId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkEarningId = 0;
        mbDeleted = false;
        mnFkAccountId = 0;
        mnFkBizPartnerId_n = 0;
        mnFkTaxBasicId_n = 0;
        mnFkTaxTaxId_n = 0;
        mnFkPackExpensesId = 0;
        mnFkPackCostCentersId = 0;
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
        return "WHERE id_ear = " + mnPkEarningId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ear = " + pk[0] + " ";
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
            mnPkEarningId = resultSet.getInt("id_ear");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkAccountId = resultSet.getInt("fk_acc");
            mnFkBizPartnerId_n = resultSet.getInt("fk_bp_n");
            mnFkTaxBasicId_n = resultSet.getInt("fk_tax_bas_n");
            mnFkTaxTaxId_n = resultSet.getInt("fk_tax_tax_n");
            mnFkPackExpensesId = resultSet.getInt("fk_pack_exp");
            mnFkPackCostCentersId = resultSet.getInt("fk_pack_cc");
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
    public boolean canSave(final SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);
        
        if (can) {
            if (mbRegistryNew) {
                // check non-duplication of configuration:
                if (countExistingRegistries(session, mnPkEarningId) > 0) {
                    throw new Exception("Ya hay un registro de configuración para esta percepción '" + session.readField(SModConsts.HRS_EAR, new int[] { mnPkEarningId }, FIELD_NAME) + "'.");
                }
            }
            
            if (mnFkAccountId == 0) {
                throw new Exception("Se debe especificar una cuenta contable.");
            }
            
            boolean isAccountingRecordTypeGlobal = mnFkAccountingRecordTypeId == SModSysConsts.HRSS_TP_ACC_GBL; // convenience variable
            
            if (isAccountingRecordTypeGlobal) {
                if (mnFkAccountId == SDataConstantsSys.NA) {
                    throw new Exception("Se debe especificar una cuenta contable, porque el tipo de registro contable de la percepción es 'global'.");
                }
            }
            else {
                can = SHrsFinUtils.validateAccount(session, mnFkAccountId, -1, isAccountingRecordTypeGlobal || mnFkBizPartnerId_n != 0 ? mnFkBizPartnerId_n : -1, -1, mnFkTaxBasicId_n, mnFkTaxTaxId_n);

                if (can) {
                    SDataAccount account = SHrsFinUtils.getAccount(session, mnFkAccountId);
                    SDataAccount accountLedger = SHrsFinUtils.getAccountLedger(session, account);

                    if ((account.getIsRequiredItem() || accountLedger.getIsRequiredItem() || SHrsFinUtils.isAccountTypeForResults(accountLedger)) && (mnFkPackExpensesId == 0 || mnFkPackExpensesId == SModSysConsts.HRSU_PACK_EXP_NA)) {
                        throw new Exception("Se debe especificar un paquete de gastos porque así lo requiere la cuenta contable '" + account.getPkAccountIdXXX() + " " + account.getAccount() + "'.");
                    }

                    if (isAccountingRecordTypeGlobal) {
                        if ((account.getIsRequiredCostCenter() || accountLedger.getIsRequiredCostCenter() || SHrsFinUtils.isAccountTypeForResults(accountLedger)) && (mnFkPackCostCentersId == 0 || mnFkPackCostCentersId == SModSysConsts.HRS_PACK_CC_NA)) {
                            throw new Exception("Se debe especificar un paquete de centros de costo porque así lo requiere la cuenta contable '" + account.getPkAccountIdXXX() + " " + account.getAccount() + "'.");
                        }
                    }
                }
            }
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
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkEarningId + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkAccountId + ", " + 
                    (mnFkBizPartnerId_n == 0 ? "NULL" : "" + mnFkBizPartnerId_n) + ", " +
                    (mnFkTaxBasicId_n == 0 ? "NULL" : "" + mnFkTaxBasicId_n) + ", " +
                    (mnFkTaxTaxId_n == 0 ? "NULL" : "" + mnFkTaxTaxId_n) + ", " +
                    mnFkPackExpensesId + ", " +
                    mnFkPackCostCentersId + ", " + 
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
                    //"id_ear = " + mnPkEarningId + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_acc = " + mnFkAccountId + ", " +
                    "fk_bp_n = " + (mnFkBizPartnerId_n == 0 ? "NULL" : "" + mnFkBizPartnerId_n) + ", " +
                    "fk_tax_bas_n = " + (mnFkTaxBasicId_n == 0 ? "NULL" : "" + mnFkTaxBasicId_n) + ", " +
                    "fk_tax_tax_n = " + (mnFkTaxTaxId_n == 0 ? "NULL" : "" + mnFkTaxTaxId_n) + ", " +
                    "fk_pack_exp = " + mnFkPackExpensesId + ", " +
                    "fk_pack_cc = " + mnFkPackCostCentersId + ", " +
                    "fk_tp_acc_rec = " + mnFkAccountingRecordTypeId + ", " +
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
    public SDbCfgAccountingEarning clone() throws CloneNotSupportedException {
        SDbCfgAccountingEarning registry = new SDbCfgAccountingEarning();

        registry.setPkEarningId(this.getPkEarningId());
        registry.setDeleted(this.isDeleted());
        registry.setFkAccountId(this.getFkAccountId());
        registry.setFkBizPartnerId_n(this.getFkBizPartnerId_n());
        registry.setFkTaxBasicId_n(this.getFkTaxBasicId_n());
        registry.setFkTaxTaxId_n(this.getFkTaxTaxId_n());
        registry.setFkPackExpensesId(this.getFkPackExpensesId());
        registry.setFkPackCostCentersId(this.getFkPackCostCentersId());
        registry.setFkAccountingRecordTypeId(this.getFkAccountingRecordTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    public static final int countExistingRegistries(final SGuiSession session, final int idToCount) throws Exception {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.HRS_CFG_ACC_EAR) + " WHERE id_ear = " + idToCount + ";";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
                
        return count;
    }
}

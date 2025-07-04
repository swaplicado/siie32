/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mfin.data.SDataRecordEntry;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbStockValuationAccounting extends SDbRegistryUser {
    
    protected int mnPkStockValuationAccountingId;
    protected double mdProrationPercentage;
    protected boolean mbDeleted;
    protected int mnFkFinRecYearId_n;
    protected int mnFkFinRecPeriodId_n;
    protected int mnFkFinRecBookkeepingCenterId_n;
    protected String msFkFinRecRecordTypeId_n;
    protected int mnFkFinRecNumberId_n;
    protected int mnFkFinRecEntryId_n;
    protected int mnFkStockValuationId;
    protected int mnFkStockValuationMvtId;
    
    protected double mdAuxQuantity;
    protected SDataRecordEntry oAuxRecordEntry;

    public SDbStockValuationAccounting() {
        super(SModConsts.TRN_STK_VAL_ACC);
    }
    
    public void setPkStockValuationAccountingId(int n) { mnPkStockValuationAccountingId = n; }
    public void setProrationPercentage(double d) { mdProrationPercentage = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkFinRecYearId_n(int n) { mnFkFinRecYearId_n = n; }
    public void setFkFinRecPeriodId_n(int n) { mnFkFinRecPeriodId_n = n; }
    public void setFkFinRecBookkeepingCenterId_n(int n) { mnFkFinRecBookkeepingCenterId_n = n; }
    public void setFkFinRecRecordTypeId_n(String s) { msFkFinRecRecordTypeId_n = s; }
    public void setFkFinRecNumberId_n(int n) { mnFkFinRecNumberId_n = n; }
    public void setFkFinRecEntryId_n(int n) { mnFkFinRecEntryId_n = n; }
    public void setFkStockValuationId(int n) { mnFkStockValuationId = n; }
    public void setFkStockValuationMvtId(int n) { mnFkStockValuationMvtId = n; }

    public void setAuxQuantity(double d) { mdAuxQuantity = d; }
    public void setAuxRecordEntry(SDataRecordEntry o) { oAuxRecordEntry = o; }
    
    public int getPkStockValuationAccountingId() { return mnPkStockValuationAccountingId; }
    public double getProrationPercentage() { return mdProrationPercentage; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkFinRecYearId_n() { return mnFkFinRecYearId_n; }
    public int getFkFinRecPeriodId_n() { return mnFkFinRecPeriodId_n; }
    public int getFkFinRecBookkeepingCenterId_n() { return mnFkFinRecBookkeepingCenterId_n; }
    public String getFkFinRecRecordTypeId_n() { return msFkFinRecRecordTypeId_n; }
    public int getFkFinRecNumberId_n() { return mnFkFinRecNumberId_n; }
    public int getFkFinRecEntryId_n() { return mnFkFinRecEntryId_n; }
    public int getFkStockValuationId() { return mnFkStockValuationId; }
    public int getStockFkValuationMvtId() { return mnFkStockValuationMvtId; }
    
    public double getAuxQuantity() { return mdAuxQuantity; }
    public SDataRecordEntry getAuxRecordEntry() { return oAuxRecordEntry; }

    @Override
    public void setPrimaryKey(int[] key) {
        mnPkStockValuationAccountingId = key[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStockValuationAccountingId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkStockValuationAccountingId = 0;
        mdProrationPercentage = 0;
        mbDeleted = false;
        mnFkFinRecYearId_n = 0;
        mnFkFinRecPeriodId_n = 0;
        mnFkFinRecBookkeepingCenterId_n = 0;
        msFkFinRecRecordTypeId_n = "";
        mnFkFinRecNumberId_n = 0;
        mnFkFinRecEntryId_n = 0;
        mnFkStockValuationId = 0;
        mnFkStockValuationMvtId = 0;
        
        mdAuxQuantity = 0;
        oAuxRecordEntry = new SDataRecordEntry();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_stk_val_acc = " + mnPkStockValuationAccountingId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) { 
        return "WHERE id_stk_val_acc = " + pk[0] + " ";
     }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;

        mnPkStockValuationAccountingId = 0;

        msSql = "SELECT COALESCE(MAX(id_stk_val_acc), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkStockValuationAccountingId = resultSet.getInt(1);
        }
     }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkStockValuationAccountingId = resultSet.getInt("id_stk_val_acc");
            mdProrationPercentage = resultSet.getDouble("prorat_per");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkFinRecYearId_n = resultSet.getInt("fk_fin_rec_year_n");
            mnFkFinRecPeriodId_n = resultSet.getInt("fk_fin_rec_per_n");
            mnFkFinRecBookkeepingCenterId_n = resultSet.getInt("fk_fin_rec_bkc_n");
            msFkFinRecRecordTypeId_n = resultSet.getString("fk_fin_rec_tp_rec_n");
            mnFkFinRecNumberId_n = resultSet.getInt("fk_fin_rec_num_n");
            mnFkFinRecEntryId_n = resultSet.getInt("fk_fin_rec_ety_n");
            mnFkStockValuationId = resultSet.getInt("fk_stk_val");
            mnFkStockValuationMvtId = resultSet.getInt("fk_stk_val_mvt");
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
                    mnPkStockValuationAccountingId + ", " +
                    mdProrationPercentage + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkFinRecYearId_n + ", " +
                    mnFkFinRecPeriodId_n + ", " +
                    mnFkFinRecBookkeepingCenterId_n + ", " +
                    "'" + msFkFinRecRecordTypeId_n + "', " +
                    mnFkFinRecNumberId_n + ", " +
                    mnFkFinRecEntryId_n + ", " +
                    mnFkStockValuationId + ", " +
                    mnFkStockValuationMvtId + ", " +
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " +
                ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    "prorat_per = " + mdProrationPercentage + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_fin_rec_year_n = " + mnFkFinRecYearId_n + ", " +
                    "fk_fin_rec_per_n = " + mnFkFinRecPeriodId_n + ", " +
                    "fk_fin_rec_bkc_n = " + mnFkFinRecBookkeepingCenterId_n + ", " +
                    "fk_fin_rec_tp_rec_n = '" + msFkFinRecRecordTypeId_n + "', " +
                    "fk_fin_rec_num_n = " + mnFkFinRecNumberId_n + ", " +
                    "fk_fin_rec_ety_n = " + mnFkFinRecEntryId_n + ", " +
                    "fk_stk_val = " + mnFkStockValuationId + ", " +
                    "fk_stk_val_mvt = " + mnFkStockValuationMvtId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " "  + 
                    getSqlWhere();
        }
        
        session.getStatement().getConnection().createStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbStockValuationAccounting registry = new SDbStockValuationAccounting();
        
        registry.setPkStockValuationAccountingId(this.getPkStockValuationAccountingId());
        registry.setProrationPercentage(this.getProrationPercentage());
        registry.setDeleted(this.isDeleted());
        registry.setFkFinRecYearId_n(this.getFkFinRecYearId_n());
        registry.setFkFinRecPeriodId_n(this.getFkFinRecPeriodId_n());
        registry.setFkFinRecBookkeepingCenterId_n(this.getFkFinRecBookkeepingCenterId_n());
        registry.setFkFinRecRecordTypeId_n(this.getFkFinRecRecordTypeId_n());
        registry.setFkFinRecNumberId_n(this.getFkFinRecNumberId_n());
        registry.setFkFinRecEntryId_n(this.getFkFinRecEntryId_n());
        registry.setFkStockValuationId(this.getFkStockValuationId());
        registry.setFkStockValuationMvtId(this.getStockFkValuationMvtId());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

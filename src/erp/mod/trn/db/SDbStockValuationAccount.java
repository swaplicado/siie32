/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

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
public class SDbStockValuationAccount extends SDbRegistryUser {
    
    protected int mnPkStockValuationAccountId;
    protected boolean mbDeleted;
    protected int mnFkFinRecYearId;
    protected int mnFkFinRecPerId;
    protected int mnFkFinRecBkcId;
    protected String msFkFinRecTpRecId;
    protected int mnFkFinRecNum;
    protected int mnFkFinRecEty;
    protected int mnFkStockValuationId;
    protected int mnFkStockValuationMvtId;

    public SDbStockValuationAccount() {
        super(SModConsts.TRN_STK_VAL_ACC);
    }
    
    public void setPkStockValuationAccountId(int n) { mnPkStockValuationAccountId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkFinRecYearId(int n) { mnFkFinRecYearId = n; }
    public void setFkFinRecPerId(int n) { mnFkFinRecPerId = n; }
    public void setFkFinRecBkcId(int n) { mnFkFinRecBkcId = n; }
    public void setFkFinRecTpRecId(String s) { msFkFinRecTpRecId = s; }
    public void setFkFinRecNum(int n) { mnFkFinRecNum = n; }
    public void setFkFinRecEty(int n) { mnFkFinRecEty = n; }
    public void setFkStockValuationId(int n) { mnFkStockValuationId = n; }
    public void setFkValuationMvtId(int n) { mnFkStockValuationMvtId = n; }
    
    public int getPkStockValuationAccountId() { return mnPkStockValuationAccountId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkFinRecYearId() { return mnFkFinRecYearId; }
    public int getFkFinRecPerId() { return mnFkFinRecPerId; }
    public int getFkFinRecBkcId() { return mnFkFinRecBkcId; }
    public String getFkFinRecTpRecId() { return msFkFinRecTpRecId; }
    public int getFkFinRecNum() { return mnFkFinRecNum; }
    public int getFkFinRecEty() { return mnFkFinRecEty; }
    public int getFkStockValuationId() { return mnFkStockValuationId; }
    public int getFkValuationMvtId() { return mnFkStockValuationMvtId; }

    @Override
    public void setPrimaryKey(int[] key) {
        mnPkStockValuationAccountId = key[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStockValuationAccountId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkStockValuationAccountId = 0;
        mbDeleted = false;
        mnFkFinRecYearId = 0;
        mnFkFinRecPerId = 0;
        mnFkFinRecBkcId = 0;
        msFkFinRecTpRecId = "";
        mnFkFinRecNum = 0;
        mnFkFinRecEty = 0;
        mnFkStockValuationId = 0;
        mnFkStockValuationMvtId = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_stk_val_acc = " + mnPkStockValuationAccountId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) { 
        return "WHERE id_stk_val_acc = " + pk[0] + " ";
     }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;

        mnPkStockValuationAccountId = 0;

        msSql = "SELECT COALESCE(MAX(id_stk_val_acc), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkStockValuationAccountId = resultSet.getInt(1);
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
            mnPkStockValuationAccountId = resultSet.getInt("id_stk_val_acc");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkFinRecYearId = resultSet.getInt("fk_fin_rec_year");
            mnFkFinRecPerId = resultSet.getInt("fk_fin_rec_per");
            mnFkFinRecBkcId = resultSet.getInt("fk_fin_rec_bkc");
            msFkFinRecTpRecId = resultSet.getString("fk_fin_rec_tp_rec");
            mnFkFinRecNum = resultSet.getInt("fk_fin_rec_num");
            mnFkFinRecEty = resultSet.getInt("fk_fin_rec_ety");
            mnFkStockValuationId = resultSet.getInt("fk_stk_val");
            mnFkStockValuationMvtId = resultSet.getInt("fk_stk_val_mvt");
            
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
                    mnPkStockValuationAccountId + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkFinRecYearId + ", " +
                    mnFkFinRecPerId + ", " +
                    mnFkFinRecBkcId + ", " +
                    "'" + msFkFinRecTpRecId + "', " +
                    mnFkFinRecNum + ", " +
                    mnFkFinRecEty + ", " +
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
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_fin_rec_year = " + mnFkFinRecYearId + ", " +
                    "fk_fin_rec_per = " + mnFkFinRecPerId + ", " +
                    "fk_fin_rec_bkc = " + mnFkFinRecBkcId + ", " +
                    "fk_fin_rec_tp_rec = '" + msFkFinRecTpRecId + "', " +
                    "fk_fin_rec_num = " + mnFkFinRecNum + ", " +
                    "fk_fin_rec_ety = " + mnFkFinRecEty + ", " +
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
        SDbStockValuationAccount registry = new SDbStockValuationAccount();
        
        registry.setPkStockValuationAccountId(this.getPkStockValuationAccountId());
        registry.setDeleted(this.isDeleted());
        registry.setFkFinRecYearId(this.getFkFinRecYearId());
        registry.setFkFinRecPerId(this.getFkFinRecPerId());
        registry.setFkFinRecBkcId(this.getFkFinRecBkcId());
        registry.setFkFinRecTpRecId(this.getFkFinRecTpRecId());
        registry.setFkFinRecNum(this.getFkFinRecNum());
        registry.setFkFinRecEty(this.getFkFinRecEty());
        registry.setFkStockValuationId(this.getFkStockValuationId());
        registry.setFkValuationMvtId(this.getFkValuationMvtId());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

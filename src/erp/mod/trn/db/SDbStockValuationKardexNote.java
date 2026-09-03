/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.io.Serializable;
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
public class SDbStockValuationKardexNote extends SDbRegistryUser implements Serializable {
    
    protected int mnPkStockValKardexNote;
    protected String msNotes;
//    protected boolean mbSystem;
//    protected boolean mbDeleted;
    protected int mnFkStockValKardexId;
    protected int mnFkStockValuationId_n;
//    protected int mnFkUserInsertId;
//    protected int mnFkUserUpdateId;
//    protected Date mtTsUserInsert;
//    protected Date mtTsUserUpdate;

    public SDbStockValuationKardexNote() {
        super(SModConsts.TRN_STK_VAL_KARDEX_NOTE);
    }
    
    public void setPkStockValKardexNote(int n) { mnPkStockValKardexNote = n; }
    public void setNotes(String s) { msNotes = s; }
    // public void setSystem(boolean b) { mbSystem = b; }
    // public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkStockValKardexId(int n) { mnFkStockValKardexId = n; }
    public void setFkStockValuationId_n(int n) { mnFkStockValuationId_n = n; }
    // public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    // public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    // public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    // public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkStockValKardexNote() { return mnPkStockValKardexNote; }
    public String getNotes() { return msNotes; }
    // public boolean isSystem() { return mbSystem; }
    // public boolean isDeleted() { return mbDeleted; }
    public int getFkStockValKardexId() { return mnFkStockValKardexId; }
    public int getFkStockValuationId_n() { return mnFkStockValuationId_n; }
    // public int getFkUserInsertId() { return mnFkUserInsertId; }
    // public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    // public Date getTsUserInsert() { return mtTsUserInsert; }
    // public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public static void deleteAllNotesFromMvt(SGuiSession session, int fkStockValuationMvtId) throws SQLException, Exception {
        String sql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_KARDEX_NOTE) + " WHERE fk_stk_val_kardex = " + fkStockValuationMvtId;
        session.getStatement().execute(sql);
    }

    @Override
    public void setPrimaryKey(int[] key) {
        mnPkStockValKardexNote = key[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStockValKardexNote };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkStockValKardexNote = 0;
        msNotes = "";
        mbSystem = false;
        mbDeleted = false;
        mnFkStockValKardexId = 0;
        mnFkStockValuationId_n = 0;
        mnFkUserInsertId = 0;
        mtTsUserInsert = null;
        mnFkUserUpdateId = 0;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_stk_val_kardex_nts = " + mnPkStockValKardexNote;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_stk_val_kardex_nts = " + pk[0];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;

        mnPkStockValKardexNote = 0;

        msSql = "SELECT COALESCE(MAX(id_stk_val_kardex_nts), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkStockValKardexNote = resultSet.getInt(1);
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
            mnPkStockValKardexNote = resultSet.getInt("id_stk_val_kardex_nts");
            msNotes = resultSet.getString("nts");
            mbSystem = resultSet.getBoolean("b_sys");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkStockValKardexId = resultSet.getInt("fk_stk_val_kardex");
            mnFkStockValuationId_n = resultSet.getInt("fk_stk_val_n");
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
                    mnPkStockValKardexNote + ", " + 
                    "'" + (msNotes != null ? msNotes.substring(0, Math.min(msNotes.length(), 512)) : "") + "', " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkStockValKardexId + ", " + 
                    (mnFkStockValuationId_n == 0 ? "NULL" : mnFkStockValuationId_n) + ", " +
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")" ;
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    // "id_stk_val_kardex_nts = " + mnPkStockValKardexNote + ", " +
                    "nts = '" + (msNotes != null ? msNotes.substring(0, Math.min(msNotes.length(), 512)) : "") + "', " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_stk_val_kardex = " + mnFkStockValKardexId + ", " +
                    "fk_stk_val_n = " + (mnFkStockValuationId_n == 0 ? "NULL" : mnFkStockValuationId_n) + ", " +
                    // "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().getConnection().createStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbStockValuationKardexNote registry = new SDbStockValuationKardexNote();
        
        registry.setPkStockValKardexNote(this.getPkStockValKardexNote());
        registry.setNotes(this.getNotes());
        registry.setSystem(this.isSystem());
        registry.setDeleted(this.isDeleted());
        registry.setFkStockValKardexId(this.getFkStockValKardexId());
        registry.setFkStockValuationId_n(this.getFkStockValuationId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }

}

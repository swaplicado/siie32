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
public class SDbStockValuationMvtNote extends SDbRegistryUser implements Serializable {
    
    protected int mnPkStockValMvtNoteId;
    protected String msNotes;
//    protected boolean mbSystem;
//    protected boolean mbDeleted;
    protected int mnFkStockValuationMvtId;
//    protected int mnFkUserInsertId;
//    protected int mnFkUserUpdateId;
//    protected Date mtTsUserInsert;
//    protected Date mtTsUserUpdate;

    public SDbStockValuationMvtNote() {
        super(SModConsts.TRN_STK_VAL_MVT_NOTE);
    }
    
    public void setPkStockValMvtNoteId(int n) { mnPkStockValMvtNoteId = n; }
    public void setNotes(String s) { msNotes = s; }
//    public void setSystem(boolean b) { mbSystem = b; }
//    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkStockValuationMvtId(int n) { mnFkStockValuationMvtId = n; }
//    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
//    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
//    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
//    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkStockValMvtNoteId() { return mnPkStockValMvtNoteId; }
    public String getNotes() { return msNotes; }
//    public boolean isSystem() { return mbSystem; }
//    public boolean isDeleted() { return mbDeleted; }
    public int getFkStockValuationMvtId() { return mnFkStockValuationMvtId; }
//    public int getFkUserInsertId() { return mnFkUserInsertId; }
//    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
//    public Date getTsUserInsert() { return mtTsUserInsert; }
//    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public static void deleteAllNotesFromMvt(SGuiSession session, int fkStockValuationMvtId) throws SQLException, Exception {
        String sql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT_NOTE) + " WHERE fk_stk_val_mvt = " + fkStockValuationMvtId;
        session.getStatement().execute(sql);
    }

    @Override
    public void setPrimaryKey(int[] key) {
        mnPkStockValMvtNoteId = key[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkStockValMvtNoteId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkStockValMvtNoteId = 0;
        msNotes = "";
        mbSystem = false;
        mbDeleted = false;
        mnFkStockValuationMvtId = 0;
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
        return "WHERE id_stk_val_mvt_nts = " + mnPkStockValMvtNoteId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_stk_val_mvt_nts = " + pk[0];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;

        mnPkStockValMvtNoteId = 0;

        msSql = "SELECT COALESCE(MAX(id_stk_val_mvt_nts), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkStockValMvtNoteId = resultSet.getInt(1);
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
            mnPkStockValMvtNoteId = resultSet.getInt("id_stk_val_mvt_nts");
            msNotes = resultSet.getString("nts");
            mbSystem = resultSet.getBoolean("b_sys");
            mbDeleted = resultSet.getBoolean("b_del");
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
                    mnPkStockValMvtNoteId + ", " + 
                    "'" + (msNotes != null ? msNotes.substring(0, Math.min(msNotes.length(), 512)) : "") + "', " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkStockValuationMvtId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")" ;
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    "id_stk_val_mvt_nts = " + mnPkStockValMvtNoteId + ", " +
                    "nts = '" + (msNotes != null ? msNotes.substring(0, Math.min(msNotes.length(), 512)) : "") + "', " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_stk_val_mvt = " + mnFkStockValuationMvtId + ", " +
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
        SDbStockValuationMvtNote registry = new SDbStockValuationMvtNote();
        
        registry.setPkStockValMvtNoteId(this.getPkStockValMvtNoteId());
        registry.setNotes(this.getNotes());
        registry.setSystem(this.isSystem());
        registry.setDeleted(this.isDeleted());
        registry.setFkStockValuationMvtId(this.getFkStockValuationMvtId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }

}

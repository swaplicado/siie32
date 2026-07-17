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
//    protected Date mtTsUserInsert;

    public SDbStockValuationMvtNote() {
        super(SModConsts.TRN_STK_VAL_MVT_NOTE);
    }
    
    public void setPkStockValMvtNoteId(int n) { mnPkStockValMvtNoteId = n; }
    public void setNotes(String s) { msNotes = s; }
//    public void setSystem(boolean b) { mbSystem = b; }
//    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkStockValuationMvtId(int n) { mnFkStockValuationMvtId = n; }
//    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
//    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    
    public int getPkStockValMvtNoteId() { return mnPkStockValMvtNoteId; }
    public String getNotes() { return msNotes; }
//    public boolean isSystem() { return mbSystem; }
//    public boolean isDeleted() { return mbDeleted; }
    public int getFkStockValuationMvtId() { return mnFkStockValuationMvtId; }
//    public int getFkUserInsertId() { return mnFkUserInsertId; }
//    public Date getTsUserInsert() { return mtTsUserInsert; }

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
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_stk_val_mvt_note = " + mnPkStockValMvtNoteId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_stk_val_mvt_note = " + pk[0];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;

        mnPkStockValMvtNoteId = 0;

        msSql = "SELECT COALESCE(MAX(id_stk_val_mvt_note), 0) + 1 FROM " + getSqlTable() + " ";
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
            mnPkStockValMvtNoteId = resultSet.getInt("id_stk_val_mvt_note");
            msNotes = resultSet.getString("notes");
            mbSystem = resultSet.getBoolean("b_sys");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkStockValuationMvtId = resultSet.getInt("fk_stk_val_mvt");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");

            
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
                    "NOW()" + " " +
                    ")" ;
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    "id_stk_val_mvt_note = " + mnPkStockValMvtNoteId + ", " +
                    "notes = '" + (msNotes != null ? msNotes.substring(0, Math.min(msNotes.length(), 512)) : "") + "', " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_stk_val_mvt = " + mnFkStockValuationMvtId + ", " +
                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "ts_usr_ins = " + "NOW()" + " " +
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
        registry.setTsUserInsert(this.getTsUserInsert());
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.itm.db;

import erp.mitm.data.SDataItem;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbItemComposition extends SDbRegistryUser {

    protected int mnPkItemId;
    protected int mnPkCompositionId;
    protected Date mtDateStart;
    protected Date mtDateEnd_n;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected SDataItem moDbmsItem;
    
    public SDbItemComposition() {
        super(SModConsts.ITMU_ITEM_COMP);
    }
    
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkCompositionId(int n) { mnPkCompositionId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd_n(Date t) { mtDateEnd_n = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkCompositionId() { return mnPkCompositionId; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd_n() { return mtDateEnd_n; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public void setDbmsItem(SDataItem o) { moDbmsItem = o; }
    
    public SDataItem getDbmsItem() { return moDbmsItem; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkItemId = pk[0];
        mnPkCompositionId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkItemId, mnPkCompositionId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkItemId = 0;
        mnPkCompositionId = 0;
        mtDateStart = null;
        mtDateEnd_n = null;
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        moDbmsItem = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_item = " + mnPkItemId + " " + 
                "AND id_comp = " + mnPkCompositionId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_item = " + pk[0] + " " + 
                "AND id_comp = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkCompositionId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_comp), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_item = " + mnPkItemId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkCompositionId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statement;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkItemId = resultSet.getInt("id_item");
            mnPkCompositionId = resultSet.getInt("id_comp");
            mtDateStart = resultSet.getDate("dt_sta");
            mtDateEnd_n = resultSet.getDate("dt_end_n");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            statement = session.getDatabase().getConnection().createStatement();
            moDbmsItem = new SDataItem();
            moDbmsItem.read(new int[] { mnPkItemId }, statement);
            
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
                    mnPkItemId + ", " + 
                    mnPkCompositionId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " + 
                    (mtDateEnd_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd_n) + "', ") + 
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
                    //"id_item = " + mnPkItemId + ", " +
                    //"id_comp = " + mnPkCompositionId + ", " +
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end_n = " + (mtDateEnd_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd_n) + "', ") +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
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
    public SDbItemComposition clone() throws CloneNotSupportedException {
        SDbItemComposition registry = new SDbItemComposition();
        
        registry.setPkItemId(this.getPkItemId());
        registry.setPkCompositionId(this.getPkCompositionId());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd_n(this.getDateEnd_n());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setDbmsItem(this.getDbmsItem());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}

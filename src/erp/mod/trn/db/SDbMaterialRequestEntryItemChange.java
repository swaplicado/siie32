/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servin
 */
public class SDbMaterialRequestEntryItemChange extends SDbRegistryUser {
    
    protected int mnPkMatRequestId;
    protected int mnPkEntryId;
    protected int mnPkChangeId;
    protected int mnFkItemId;
    protected int mnFkUserId;
    protected Date mtTsUser;

    public SDbMaterialRequestEntryItemChange() {
        super(SModConsts.TRN_MAT_REQ_ETY_ITEM_CHG);
    }
    
    public void setPkMatRequestId(int n) { mnPkMatRequestId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setPkChangeId(int n) { mnPkChangeId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }
    
    public int getPkMatRequestId() { return mnPkMatRequestId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public int getPkChangeId() { return mnPkChangeId; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMatRequestId = pk[0];
        mnPkEntryId = pk[1];
        mnPkChangeId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMatRequestId, mnPkEntryId, mnPkChangeId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMatRequestId = 0;
        mnPkEntryId = 0;
        mnPkChangeId = 0;
        mnFkItemId = 0;
        mnFkUserId = 0;
        mtTsUser = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mat_req = " + mnPkMatRequestId + " AND id_ety = " + mnPkEntryId + " AND id_chg = " + mnPkChangeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mat_req = " + pk[0] + " AND id_ety = " + pk[1] + " AND id_chg = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkChangeId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_chg), 0) + 1 FROM " + getSqlTable() + " WHERE id_mat_req = " + mnPkMatRequestId + " AND id_ety = " + mnPkChangeId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkChangeId = resultSet.getInt(1);
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
            mnPkMatRequestId = resultSet.getInt("id_mat_req");
            mnPkEntryId = resultSet.getInt("id_ety");
            mnPkChangeId = resultSet.getInt("id_chg");
            mnFkItemId = resultSet.getInt("fk_item");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");

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
            mnFkUserId = session.getUser().getPkUserId();
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkMatRequestId + ", " + 
                    mnPkEntryId + ", " + 
                    mnPkChangeId + ", " + 
                    mnFkItemId + ", " + 
                    mnFkUserId + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_mat_req = " + mnPkMatRequestId + ", " +
                    //"id_ety = " + mnPkEntryId + ", " +
                    //"id_chg = " + mnPkChangeId + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    "fk_usr = " + mnFkUserId + ", " +
                    "ts_usr = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMaterialRequestEntryItemChange clone() throws CloneNotSupportedException {
        SDbMaterialRequestEntryItemChange registry = new SDbMaterialRequestEntryItemChange();
        
        registry.setPkMatRequestId(this.getPkMatRequestId());
        registry.setPkEntryId(this.getPkEntryId());
        registry.setPkChangeId(this.getPkChangeId());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUserId(this.getFkUserId());
        registry.setTsUser(this.getTsUser());

        return registry;
    }
}

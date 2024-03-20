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
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbMaterialConsumptionSubentity extends SDbRegistryUser implements SGridRow, Serializable {
    
    protected int mnPkMatConsumptionEntityId;
    protected int mnPkMatConsumptionSubentityId;
    protected String msCode;
    protected String msName;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkAccountConsumptionWarehouseId ;
    protected int mnFkAccountFixedAssetId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected String msXtaConsumptionEntityName;

    public SDbMaterialConsumptionSubentity() {
        super(SModConsts.TRN_MAT_CONS_SUBENT);
    }
    
    public void setPkMatConsumptionEntityId(int n) { mnPkMatConsumptionEntityId = n; }
    public void setPkMatConsumptionSubentityId(int n) { mnPkMatConsumptionSubentityId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkAccountConsumptionWarehouseId (int n) { mnFkAccountConsumptionWarehouseId  = n; }
    public void setFkAccountFixedAssetId(int n) { mnFkAccountFixedAssetId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setXtaConsumptionEntityName(String s) { msXtaConsumptionEntityName = s; }
    
    public int getPkMatConsumptionEntityId() { return mnPkMatConsumptionEntityId; }
    public int getPkMatConsumptionSubentityId() { return mnPkMatConsumptionSubentityId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkAccountConsumptionWarehouseId() { return mnFkAccountConsumptionWarehouseId; }
    public int getFkAccountFixedAssetId() { return mnFkAccountFixedAssetId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public String getXtaConsumptionEntityName() { return msXtaConsumptionEntityName; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMatConsumptionEntityId = pk[0];
        mnPkMatConsumptionSubentityId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMatConsumptionEntityId, mnPkMatConsumptionSubentityId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMatConsumptionEntityId = 0;
        mnPkMatConsumptionSubentityId = 0;
        msCode = "";
        msName = "";
        mbDeleted = false;
        mbSystem = false;
        mnFkAccountConsumptionWarehouseId = 0;
        mnFkAccountFixedAssetId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        msXtaConsumptionEntityName = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mat_cons_ent = " + mnPkMatConsumptionEntityId + " " +
                "AND id_mat_cons_subent = " + mnPkMatConsumptionSubentityId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mat_cons_ent = " + pk[0] + " " +
                "AND id_mat_cons_subent = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkMatConsumptionSubentityId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_mat_cons_subent), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_mat_cons_ent = " + mnPkMatConsumptionEntityId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkMatConsumptionSubentityId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT s.*, e.name FROM " + getSqlTable() + " AS s "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT) + " AS e "
                + "ON s.id_mat_cons_ent = e.id_mat_cons_ent "
                + "WHERE s.id_mat_cons_ent = " + pk[0] + " AND s.id_mat_cons_subent = " + pk[1];
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkMatConsumptionEntityId = resultSet.getInt("id_mat_cons_ent");
            mnPkMatConsumptionSubentityId = resultSet.getInt("id_mat_cons_subent");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("s.name");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkAccountConsumptionWarehouseId = resultSet.getInt("fk_acc_cons_whs");
            mnFkAccountFixedAssetId = resultSet.getInt("fk_acc_fa");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            msXtaConsumptionEntityName = resultSet.getString("e.name");
            
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
                    mnPkMatConsumptionEntityId + ", " + 
                    mnPkMatConsumptionSubentityId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkAccountConsumptionWarehouseId + ", " + 
                    mnFkAccountFixedAssetId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_mat_cons_ent = " + mnPkMatConsumptionEntityId + ", " +
                    //"id_mat_cons_subent = " + mnPkMatConsumptionSubentityId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_acc_cons_whs = " + mnFkAccountConsumptionWarehouseId + ", " +
                    "fk_acc_fa = " + mnFkAccountFixedAssetId + ", " +
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
    public SDbMaterialConsumptionSubentity clone() throws CloneNotSupportedException {
        SDbMaterialConsumptionSubentity registry = new SDbMaterialConsumptionSubentity();
        
        registry.setPkMatConsumptionEntityId(this.getPkMatConsumptionEntityId());
        registry.setPkMatConsumptionSubentityId(this.getPkMatConsumptionSubentityId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkAccountConsumptionWarehouseId(this.getFkAccountConsumptionWarehouseId());
        registry.setFkAccountFixedAssetId(this.getFkAccountFixedAssetId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkMatConsumptionEntityId, mnPkMatConsumptionSubentityId };
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return isRegistryEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        setRegistryEdited(edited);
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0: value = msXtaConsumptionEntityName; break;
            case 1: value = msName; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

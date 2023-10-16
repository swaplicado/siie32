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
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbMaterialConsumptionEntityWarehouse extends SDbRegistryUser implements SGridRow, Serializable {

    protected int mnPkMatConsumptionEntityId;
    protected int mnPkCompanyBranchId;
    protected int mnPkWarehouseId;
    protected boolean mbDefault;
    protected int mnFkUserId;
    protected Date mtTsUser;
    
    protected SDbMaterialConsumptionEntity moAuxMatConsEnt;
    
    public SDbMaterialConsumptionEntityWarehouse() {
        super(SModConsts.TRN_MAT_CONS_ENT_WHS);
    }
    
    public void setPkMatConsumptionEntityId(int n) { mnPkMatConsumptionEntityId = n; }
    public void setPkCompanyBranchId(int n) { mnPkCompanyBranchId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setDefault(boolean b) { mbDefault = b; }
    public void setFkUserId(int n) { mnFkUserInsertId = n; }
    public void setTsUser(Date t) { mtTsUserInsert = t; }

    public void setAuxMatConsEnt(SDbMaterialConsumptionEntity o) { moAuxMatConsEnt = o; }
    
    public int getPkMatConsumptionEntityId() { return mnPkMatConsumptionEntityId; }
    public int getPkCompanyBranchId() { return mnPkCompanyBranchId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public boolean isDefault() { return mbDefault; }
    public int getFkUserId() { return mnFkUserInsertId; }
    public Date getTsUser() { return mtTsUserInsert; }

    public SDbMaterialConsumptionEntity getAuxMatConsEnt() { return moAuxMatConsEnt; }
    
    public void readAuxMatConsEnt(SGuiSession session) throws Exception {
        moAuxMatConsEnt = new SDbMaterialConsumptionEntity();
        moAuxMatConsEnt.read(session, new int[] { mnPkMatConsumptionEntityId });
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMatConsumptionEntityId = pk[0];
        mnPkCompanyBranchId = pk[1];
        mnPkWarehouseId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMatConsumptionEntityId, mnPkCompanyBranchId, mnPkWarehouseId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMatConsumptionEntityId = 0;
        mnPkCompanyBranchId = 0;
        mnPkWarehouseId = 0;
        mbDefault = false;
        mnFkUserInsertId = 0;
        mtTsUserInsert = null;

        moAuxMatConsEnt = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mat_cons_ent = " + mnPkMatConsumptionEntityId + " " + 
                "AND id_cob = " + mnPkCompanyBranchId + " " + 
                "AND id_whs = " + mnPkWarehouseId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mat_cons_ent = " + pk[0] + " " + 
                "AND id_cob = " + pk[1] + " " + 
                "AND id_whs = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            mnPkMatConsumptionEntityId = resultSet.getInt("id_mat_cons_ent");
            mnPkCompanyBranchId = resultSet.getInt("id_cob");
            mnPkWarehouseId = resultSet.getInt("id_whs");
            mbDefault = resultSet.getBoolean("b_default");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");

            readAuxMatConsEnt(session);
            
            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        mnFkUserId = session.getUser().getPkUserId();
        
        if (mbRegistryNew) {
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" + 
                    mnPkMatConsumptionEntityId + ", " + 
                    mnPkCompanyBranchId + ", " + 
                    mnPkWarehouseId + ", " +
                    (mbDefault ? 1 : 0) + ", " + 
                    mnFkUserId + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_mat_cons_ent = " + mnPkMatProvisionEntityId + ", " +
                    //"id_cob = " + mnPkCompanyBranchId + ", " +
                    //"id_whs = " + mnPkWarehouseId + ", " +
                    "b_default = " + (mbDefault ? 1 : 0) + ", " +
                    //"fk_usr = " + mnFkUserInsertId + ", " +
                    //"ts_usr = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMaterialConsumptionEntityWarehouse clone() throws CloneNotSupportedException {
        SDbMaterialConsumptionEntityWarehouse registry = new SDbMaterialConsumptionEntityWarehouse();
        
        registry.setPkMatConsumptionEntityId(this.getPkMatConsumptionEntityId());
        registry.setPkCompanyBranchId(this.getPkCompanyBranchId());
        registry.setPkWarehouseId(this.getPkWarehouseId());
        registry.setDefault(this.isDefault());
        registry.setFkUserId(this.getFkUserId());
        registry.setTsUser(this.getTsUser());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkMatConsumptionEntityId, mnPkCompanyBranchId, mnPkWarehouseId };
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
            case 0: value = moAuxMatConsEnt.getName(); break;
            case 1: value = mbDefault; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 1: mbDefault = (boolean) value; break;
        }
    }
}

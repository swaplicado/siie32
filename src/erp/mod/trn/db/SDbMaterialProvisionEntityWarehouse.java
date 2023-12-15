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
public class SDbMaterialProvisionEntityWarehouse extends SDbRegistryUser implements SGridRow, Serializable {

    protected int mnPkMatProvisionEntityId;
    protected int mnPkCompanyBranchId;
    protected int mnPkWarehouseId;
    protected int mnFkUserId;
    protected Date mtTsUser;
    
    protected SDbMaterialProvisionEntity moAuxMatProvEnt;
    
    public SDbMaterialProvisionEntityWarehouse() {
        super(SModConsts.TRN_MAT_PROV_ENT_WHS);
    }
    
    public void setPkMatProvisionEntityId(int n) { mnPkMatProvisionEntityId = n; }
    public void setPkCompanyBranchId(int n) { mnPkCompanyBranchId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setFkUserId(int n) { mnFkUserInsertId = n; }
    public void setTsUser(Date t) { mtTsUserInsert = t; }

    public void setAuxMatProvEnt(SDbMaterialProvisionEntity o) { moAuxMatProvEnt = o; }
    
    public int getPkMatProvisionEntityId() { return mnPkMatProvisionEntityId; }
    public int getPkCompanyBranchId() { return mnPkCompanyBranchId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public int getFkUserId() { return mnFkUserInsertId; }
    public Date getTsUser() { return mtTsUserInsert; }


    public SDbMaterialProvisionEntity getAuxMatProvEnt() { return moAuxMatProvEnt; }
    
    public void readAuxMatProvEnt(SGuiSession session) throws Exception {
        moAuxMatProvEnt = new SDbMaterialProvisionEntity();
        moAuxMatProvEnt.read(session, new int[] { mnPkMatProvisionEntityId });
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMatProvisionEntityId = pk[0];
        mnPkCompanyBranchId = pk[1];
        mnPkWarehouseId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMatProvisionEntityId, mnPkCompanyBranchId, mnPkWarehouseId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMatProvisionEntityId = 0;
        mnPkCompanyBranchId = 0;
        mnPkWarehouseId = 0;
        mnFkUserInsertId = 0;
        mtTsUserInsert = null;

        moAuxMatProvEnt = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mat_prov_ent = " + mnPkMatProvisionEntityId + " " + 
                "AND id_cob = " + mnPkCompanyBranchId + " " + 
                "AND id_whs = " + mnPkWarehouseId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mat_prov_ent = " + pk[0] + " " + 
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
            mnPkMatProvisionEntityId = resultSet.getInt("id_mat_prov_ent");
            mnPkCompanyBranchId = resultSet.getInt("id_cob");
            mnPkWarehouseId = resultSet.getInt("id_whs");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");

            readAuxMatProvEnt(session);
            
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
                    mnPkMatProvisionEntityId + ", " + 
                    mnPkCompanyBranchId + ", " + 
                    mnPkWarehouseId + ", " + 
                    mnFkUserId + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_mat_prov_ent = " + mnPkMatProvisionEntityId + ", " +
                    //"id_cob = " + mnPkCompanyBranchId + ", " +
                    //"id_whs = " + mnPkWarehouseId + ", " +
                    //"fk_usr = " + mnFkUserInsertId + ", " +
                    //"ts_usr = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMaterialProvisionEntityWarehouse clone() throws CloneNotSupportedException {
        SDbMaterialProvisionEntityWarehouse registry = new SDbMaterialProvisionEntityWarehouse();
        
        registry.setPkMatProvisionEntityId(this.getPkMatProvisionEntityId());
        registry.setPkCompanyBranchId(this.getPkCompanyBranchId());
        registry.setPkWarehouseId(this.getPkWarehouseId());
        registry.setFkUserId(this.getFkUserId());
        registry.setTsUser(this.getTsUser());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkMatProvisionEntityId, mnPkCompanyBranchId, mnPkWarehouseId };
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
            case 0: value = moAuxMatProvEnt.getName(); break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

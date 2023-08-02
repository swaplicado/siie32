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
public class SDbMaterialProvisionEntityUser extends SDbRegistryUser implements SGridRow, Serializable {
    
    protected int mnPkMatProvisionEntityId;
    protected int mnPkUserId;
    protected boolean mbDefault;
    /*
    protected int mnFkUserInsertId;
    protected Date mtTsUserInsert;
    */

    
    protected SDbMaterialProvisionEntity moAuxMatProvEnt;

    public SDbMaterialProvisionEntityUser() {
        super(SModConsts.TRN_MAT_PROV_ENT_USR);
    }
    
    public void setPkMatProvisionEntityId(int n) { mnPkMatProvisionEntityId = n; }
    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setDefault(boolean b) { mbDefault = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }

    
    public void setAuxMatProvEnt(SDbMaterialProvisionEntity o) { moAuxMatProvEnt = o; }
    
    public int getPkMatProvisionEntityId() { return mnPkMatProvisionEntityId; }
    public int getPkUserId() { return mnPkUserId; }
    public boolean isDefault() { return mbDefault; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }

    
    public SDbMaterialProvisionEntity getAuxMatProvEnt() { return moAuxMatProvEnt; }
    
    public void readAuxMatProvEnt(SGuiSession session) throws Exception {
        moAuxMatProvEnt = new SDbMaterialProvisionEntity();
        moAuxMatProvEnt.read(session, new int[] { mnPkMatProvisionEntityId });
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMatProvisionEntityId = pk[0];
        mnPkUserId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMatProvisionEntityId, mnPkUserId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMatProvisionEntityId = 0;
        mnPkUserId = 0;
        mbDefault = false;
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
                "AND id_usr = " + mnPkUserId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mat_prov_ent = " + pk[0] + " " +
                "AND id_usr = " + pk[1] + " ";
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
        if(!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkMatProvisionEntityId = resultSet.getInt("id_mat_prov_ent");
            mnPkUserId = resultSet.getInt("id_usr");
            mbDefault = resultSet.getBoolean("b_default");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            
            readAuxMatProvEnt(session);

            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        mnFkUserInsertId = session.getUser().getPkUserId();
        
        if (mbRegistryNew) {
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" + 
                    mnPkMatProvisionEntityId + ", " + 
                    mnPkUserId + ", " + 
                    (mbDefault ? 1 : 0) + ", " +
                    mnFkUserInsertId + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_mat_prov_ent = " + mnPkMatProvisionEntityId + ", " +
                    //"id_usr = " + mnPkUserId + ", " +
                    "b_default = " + (mbDefault ? 1 : 0) + " " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    //"ts_usr_ins = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMaterialProvisionEntityUser clone() throws CloneNotSupportedException {
        SDbMaterialProvisionEntityUser registry = new SDbMaterialProvisionEntityUser();
        
        registry.setPkMatProvisionEntityId(this.getPkMatProvisionEntityId());
        registry.setPkUserId(this.getPkUserId());
        registry.setDefault(this.isDefault());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setTsUserInsert(this.getTsUserInsert());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkMatProvisionEntityId, mnPkUserId };
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

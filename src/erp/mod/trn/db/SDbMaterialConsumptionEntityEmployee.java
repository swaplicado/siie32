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
public class SDbMaterialConsumptionEntityEmployee extends SDbRegistryUser implements SGridRow, Serializable {
    
    protected int mnPkMatConsumptionEntityId;
    protected int mnPkEmployeeId;
    protected boolean mbDefault;
    /*
    protected int mnFkUserInsertId;
    protected Date mtTsUserInsert;
    */
    
    protected SDbMaterialConsumptionEntity moAuxMatConsEnt;

    public SDbMaterialConsumptionEntityEmployee() {
        super(SModConsts.TRN_MAT_CONS_ENT_EMP);
    }
    
    public void setPkMatConsumptionEntityId(int n) { mnPkMatConsumptionEntityId = n; }
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setDefault(boolean b) { mbDefault = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    
    public void setAuxMatConsEnt(SDbMaterialConsumptionEntity o) { moAuxMatConsEnt = o; }
    
    public int getPkMatConsumptionEntityId() { return mnPkMatConsumptionEntityId; }
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public boolean isDefault() { return mbDefault; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    
    public SDbMaterialConsumptionEntity getAuxMatConsEnt() { return moAuxMatConsEnt; }

    public void readAuxMatConsEnt(SGuiSession session) throws Exception {
        moAuxMatConsEnt = new SDbMaterialConsumptionEntity();
        moAuxMatConsEnt.read(session, new int[] { mnPkMatConsumptionEntityId });
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMatConsumptionEntityId = pk[0];
        mnPkEmployeeId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMatConsumptionEntityId, mnPkEmployeeId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMatConsumptionEntityId = 0;
        mnPkEmployeeId = 0;
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
                "AND id_emp = " + mnPkEmployeeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mat_cons_ent = " + pk[0] + " " +
                "AND id_emp = " + pk[1] + " ";
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
            mnPkMatConsumptionEntityId = resultSet.getInt("id_mat_cons_ent");
            mnPkEmployeeId = resultSet.getInt("id_emp");
            mbDefault = resultSet.getBoolean("b_default");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            
            readAuxMatConsEnt(session);

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
                    mnPkMatConsumptionEntityId + ", " + 
                    mnPkEmployeeId + ", " + 
                    (mbDefault ? 1 : 0) + ", " +
                    mnFkUserInsertId + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_mat_cons_ent = " + mnPkMatConsumptionEntityId + ", " +
                    //"id_bp = " + mnPkEmployeeId + ", " +
                    "b_default = " + (mbDefault ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    //"ts_usr_ins = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMaterialConsumptionEntityEmployee clone() throws CloneNotSupportedException {
        SDbMaterialConsumptionEntityEmployee registry = new SDbMaterialConsumptionEntityEmployee();
        
        registry.setPkMatConsumptionEntityId(this.getPkMatConsumptionEntityId());
        registry.setPkEmployeeId(this.getPkEmployeeId());
        registry.setDefault(this.isDefault());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setTsUserInsert(this.getTsUserInsert());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int [] { mnPkMatConsumptionEntityId, mnPkEmployeeId };
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

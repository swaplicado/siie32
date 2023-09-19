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
 * @author Isabel Servín
 */
public class SDbMaterialConsumptionSubentityCostCenter extends SDbRegistryUser {

    protected int mnPkMatConsumptionEntityId;
    protected int mnPkMatConsumptionSubentityId;
    protected int mnPkCostCenterId;
    protected int mnFkUserId;
    protected Date mtTsUser;

    public SDbMaterialConsumptionSubentityCostCenter() {
        super(SModConsts.TRN_MAT_CONS_SUBENT_CC);
    }

    public void setPkMatConsumptionEntityId(int n) { mnPkMatConsumptionEntityId = n; }
    public void setPkMatConsumptionSubentityId(int n) { mnPkMatConsumptionSubentityId = n; }
    public void setPkCostCenterId(int n) { mnPkCostCenterId = n; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }
    
    public int getPkMatConsumptionEntityId() { return mnPkMatConsumptionEntityId; }
    public int getPkMatConsumptionSubentityId() { return mnPkMatConsumptionSubentityId; }
    public int getPkCostCenterId() { return mnPkCostCenterId; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMatConsumptionEntityId = pk[0];
        mnPkMatConsumptionSubentityId = pk[1];
        mnPkCostCenterId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMatConsumptionEntityId, mnPkMatConsumptionSubentityId, mnPkCostCenterId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMatConsumptionEntityId = 0;
        mnPkMatConsumptionSubentityId = 0;
        mnPkCostCenterId = 0;
        mnFkUserId = 0;
        mtTsUser = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mat_cons_ent = " + mnPkMatConsumptionEntityId + " " +
                "AND id_mat_cons_subent = " + mnPkMatConsumptionSubentityId + " " +
                "AND id_cc = " + mnPkCostCenterId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
    return "WHERE id_mat_cons_ent = " + pk[0] + " " +
                "AND id_mat_cons_subent = " + pk[1] + " " +
                "AND id_cc = " + pk[2] + " ";
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
            mnPkMatConsumptionSubentityId = resultSet.getInt("id_mat_cons_subent");
            mnPkCostCenterId = resultSet.getInt("id_cc");
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
        
        mnFkUserId = session.getUser().getPkUserId();
        
        if (mbRegistryNew) {
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" + 
                    mnPkMatConsumptionEntityId + ", " + 
                    mnPkMatConsumptionSubentityId + ", " + 
                    mnPkCostCenterId + ", " + 
                    mnFkUserId + ", " + 
                    "NOW()" + " " +
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_mat_cons_ent = " + mnPkMatConsumptionEntityId + ", " +
                    //"id_mat_cons_subent = " + mnPkMatConsumptionSubentityId + ", " +
                    //"id_cc = " + mnPkCostCenterId + ", " +
                    //"fk_usr = " + mnFkUserId + ", " +
                    //"ts_usr = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMaterialConsumptionSubentityCostCenter clone() throws CloneNotSupportedException {
        SDbMaterialConsumptionSubentityCostCenter registry = new SDbMaterialConsumptionSubentityCostCenter();
        
        registry.setPkMatConsumptionEntityId(this.getPkMatConsumptionEntityId());
        registry.setPkMatConsumptionSubentityId(this.getPkMatConsumptionSubentityId());
        registry.setPkCostCenterId(this.getPkCostCenterId());
        registry.setFkUserId(this.getFkUserId());
        registry.setTsUser(this.getTsUser());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}

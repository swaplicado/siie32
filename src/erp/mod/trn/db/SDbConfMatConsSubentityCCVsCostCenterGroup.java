/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mfin.data.SDataCostCenter;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbConfMatConsSubentityCCVsCostCenterGroup extends SDbRegistryUser {
    
    protected int mnPkConsumptionEntity;
    protected int mnPkConsumptionSubentity;
    protected int mnPkCostCenterId;
    
    protected ArrayList<SDbMaterialConsumptionSubentityCostCenterCCGroup> maMatConsSubCostCenterCCGrp;
    
    protected SDbMaterialConsumptionSubentity moAuxMatConsumptionSubentity;
    protected SDataCostCenter moAuxCostCenter;

    public SDbConfMatConsSubentityCCVsCostCenterGroup() {
        super(SModConsts.TRNX_CONF_SUBENT_VS_CC_GRP);
    }

    public void setPkConsumptionEntity(int n) { mnPkConsumptionEntity = n; }
    public void setPkConsumptionSubentity(int n) { mnPkConsumptionSubentity = n; }
    public void setPkCostCenter(int n) { mnPkCostCenterId = n; }
    
    public int getPkConsumptionEntity() { return mnPkConsumptionEntity; }
    public int getPkConsumptionSubentity() { return mnPkConsumptionSubentity; }
    public int getPkCostCenter() { return mnPkCostCenterId; }
    
    public ArrayList<SDbMaterialConsumptionSubentityCostCenterCCGroup> getConsSubCostCenterCCGrp() { return maMatConsSubCostCenterCCGrp; }
    
    public SDbMaterialConsumptionSubentity getAuxMatConsumptionSubentity() { return moAuxMatConsumptionSubentity; }
    public SDataCostCenter getAuxCostCenter() { return moAuxCostCenter; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkConsumptionEntity = pk[0];
        mnPkConsumptionSubentity = pk[1];
        mnPkCostCenterId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkConsumptionEntity, mnPkConsumptionSubentity, mnPkCostCenterId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkConsumptionEntity = 0;
        mnPkConsumptionSubentity = 0;
        mnPkCostCenterId = 0;
        
        maMatConsSubCostCenterCCGrp = new ArrayList<>();
        
        moAuxMatConsumptionSubentity = null;
        moAuxCostCenter = null;
    }

    @Override
    public String getSqlTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSqlWhere() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSqlWhere(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statement;
        SDbMaterialConsumptionSubentityCostCenterCCGroup cons;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        mnPkConsumptionEntity = pk[0];
        mnPkConsumptionSubentity = pk[1];
        mnPkCostCenterId = pk[2];
        
        String id_cc = "";
        statement = session.getDatabase().getConnection().createStatement();
        msSql = "SELECT id_mat_cons_ent, id_mat_cons_subent, id_cc, id_mat_cc_grp " + 
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT_CC_CC_GRP) + " " +
                "WHERE id_mat_cons_ent = " + mnPkConsumptionEntity + " " +
                "AND id_mat_cons_subent = " + mnPkConsumptionSubentity + " " +
                "AND id_cc = " + mnPkCostCenterId + " ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            cons = new SDbMaterialConsumptionSubentityCostCenterCCGroup();
            cons.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4) });
            maMatConsSubCostCenterCCGrp.add(cons);
        }
        
        msSql = "SELECT id_cc FROM fin_cc WHERE pk_cc = " + mnPkCostCenterId;
        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            id_cc = resultSet.getString(1);
        }
        
        moAuxMatConsumptionSubentity = new SDbMaterialConsumptionSubentity();
        moAuxMatConsumptionSubentity.read(session, new int[] { mnPkConsumptionEntity, mnPkConsumptionSubentity });        
        
        moAuxCostCenter = new SDataCostCenter();
        moAuxCostCenter.read(new String[] { id_cc }, statement);
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT_CC_CC_GRP) + " " +
                "WHERE id_mat_cons_ent = " + mnPkConsumptionEntity + " " +
                "AND id_mat_cons_subent = " + mnPkConsumptionSubentity + " " +
                "AND id_cc = " + mnPkCostCenterId + " ";
        session.getStatement().execute(msSql);
        for (SDbMaterialConsumptionSubentityCostCenterCCGroup ent : maMatConsSubCostCenterCCGrp) {
            ent.setPkMatConsumptionEntityId(mnPkConsumptionEntity);
            ent.setPkMatConsumptionSubentityId(mnPkConsumptionSubentity);
            ent.setPkCostCenterId(mnPkCostCenterId);
            ent.setRegistryNew(true);
            ent.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbConfMatConsSubentityCCVsCostCenterGroup clone() throws CloneNotSupportedException {
        SDbConfMatConsSubentityCCVsCostCenterGroup registry = new SDbConfMatConsSubentityCCVsCostCenterGroup();
        
        registry.setPkConsumptionEntity(this.getPkConsumptionEntity());
        registry.setPkConsumptionSubentity(this.getPkConsumptionSubentity());
        registry.setPkCostCenter(this.getPkCostCenter());
        
        for (SDbMaterialConsumptionSubentityCostCenterCCGroup ee : this.getConsSubCostCenterCCGrp()) {
            registry.getConsSubCostCenterCCGrp().add(ee);
        }
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}

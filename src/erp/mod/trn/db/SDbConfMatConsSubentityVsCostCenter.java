/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

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
public class SDbConfMatConsSubentityVsCostCenter extends SDbRegistryUser {
    
    protected int mnPkConsumptionEntity;
    protected int mnPkConsumptionSubentity;
    
    protected ArrayList<SDbMaterialConsumptionSubentityCostCenter> maMatConsSubCC;
    
    protected SDbMaterialConsumptionSubentity moAuxMatConsumptionSubentity;

    public SDbConfMatConsSubentityVsCostCenter() {
        super(SModConsts.TRNX_CONF_SUBENT_VS_CC);
    }

    public void setPkConsumptionEntity(int n) { mnPkConsumptionEntity = n; }
    public void setPkConsumptionSubentity(int n) { mnPkConsumptionSubentity = n; }
    
    public int getPkConsumptionEntity() { return mnPkConsumptionEntity; }
    public int getPkConsumptionSubentity() { return mnPkConsumptionSubentity; }
    
    public ArrayList<SDbMaterialConsumptionSubentityCostCenter> getConsSubCC() { return maMatConsSubCC; }
    
    public SDbMaterialConsumptionSubentity getAuxMatConsumptionSubentity() { return moAuxMatConsumptionSubentity; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkConsumptionEntity = pk[0];
        mnPkConsumptionSubentity = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkConsumptionEntity, mnPkConsumptionSubentity };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkConsumptionEntity = 0;
        mnPkConsumptionSubentity = 0;
        
        maMatConsSubCC = new ArrayList<>();
        
        moAuxMatConsumptionSubentity = null;
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
        SDbMaterialConsumptionSubentityCostCenter cons;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        mnPkConsumptionEntity = pk[0];
        mnPkConsumptionSubentity = pk[1];
        
        statement = session.getDatabase().getConnection().createStatement();
        msSql = "SELECT id_mat_cons_ent, id_mat_cons_subent, id_cc " + 
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT_CC) + " " +
                "WHERE id_mat_cons_ent = " + mnPkConsumptionEntity + " " +
                "AND id_mat_cons_subent = " + mnPkConsumptionSubentity + " ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            cons = new SDbMaterialConsumptionSubentityCostCenter();
            cons.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
            maMatConsSubCC.add(cons);
        }
        
        moAuxMatConsumptionSubentity = new SDbMaterialConsumptionSubentity();
        moAuxMatConsumptionSubentity.read(session, pk);
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT_CC) + " " +
                "WHERE id_mat_cons_ent = " + mnPkConsumptionEntity + " " +
                "AND id_mat_cons_subent = " + mnPkConsumptionSubentity + " ";
        session.getStatement().execute(msSql);
        for (SDbMaterialConsumptionSubentityCostCenter ent : maMatConsSubCC) {
            ent.setPkMatConsumptionEntityId(mnPkConsumptionEntity);
            ent.setPkMatConsumptionSubentityId(mnPkConsumptionSubentity);
            ent.setRegistryNew(true);
            ent.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbConfMatConsSubentityVsCostCenter clone() throws CloneNotSupportedException {
        SDbConfMatConsSubentityVsCostCenter registry = new SDbConfMatConsSubentityVsCostCenter();
        
        registry.setPkConsumptionEntity(this.getPkConsumptionEntity());
        registry.setPkConsumptionSubentity(this.getPkConsumptionSubentity());
        
        for (SDbMaterialConsumptionSubentityCostCenter ee : this.getConsSubCC()) {
            registry.getConsSubCC().add(ee);
        }
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}

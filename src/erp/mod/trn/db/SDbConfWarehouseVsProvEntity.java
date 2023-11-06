/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mcfg.data.SDataCompanyBranchEntity;
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
public class SDbConfWarehouseVsProvEntity extends SDbRegistryUser {
    
    protected int mnPkCompanyBranch;
    protected int mnPkWarehouse;
    
    protected ArrayList<SDbMaterialProvisionEntityWarehouse> maProvEntWhs;
    
    protected SDataCompanyBranchEntity moAuxCompBrEnt;

    public SDbConfWarehouseVsProvEntity() {
        super(SModConsts.TRNX_CONF_WHS_VS_PRV_ENT);
    }
    
    public void setPkCompanyBranch(int n) { mnPkCompanyBranch = n; }  
    public void setPkWarehouse(int n) { mnPkWarehouse = n; } 
    
    public int getPkCompanyBranch() { return mnPkCompanyBranch; }
    public int getPkWarehouse() { return mnPkWarehouse; }
    
    public ArrayList<SDbMaterialProvisionEntityWarehouse> getProvEntWhs() { return maProvEntWhs; }

    public SDataCompanyBranchEntity getAuxCompBrEnt() { return moAuxCompBrEnt; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCompanyBranch = pk[0];
        mnPkWarehouse = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCompanyBranch, mnPkWarehouse };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkCompanyBranch = 0;
        mnPkWarehouse = 0;
        
        maProvEntWhs = new ArrayList<>();
        
        moAuxCompBrEnt = null;
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
        SDbMaterialProvisionEntityWarehouse prov;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        mnPkCompanyBranch = pk[0];
        mnPkWarehouse = pk[1];
        
        statement = session.getDatabase().getConnection().createStatement();
        msSql = "SELECT id_mat_prov_ent, id_cob, id_whs " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT_WHS) + " " +
                "WHERE id_cob = " + mnPkCompanyBranch + " AND id_whs = " + mnPkWarehouse + " ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            prov = new SDbMaterialProvisionEntityWarehouse();
            prov.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
            maProvEntWhs.add(prov);
        }
        
        moAuxCompBrEnt = new SDataCompanyBranchEntity();
        moAuxCompBrEnt.read(pk, statement);
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT_WHS) + " " + 
                "WHERE id_cob = " + mnPkCompanyBranch + " AND id_whs = " + mnPkWarehouse + " ";
        session.getStatement().execute(msSql);
        for (SDbMaterialProvisionEntityWarehouse ent : maProvEntWhs) {
            ent.setPkCompanyBranchId(mnPkCompanyBranch);
            ent.setPkWarehouseId(mnPkWarehouse);
            ent.setRegistryNew(true);
            ent.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbConfWarehouseVsProvEntity clone() throws CloneNotSupportedException {
        SDbConfWarehouseVsProvEntity registry = new SDbConfWarehouseVsProvEntity();
        
        registry.setPkCompanyBranch(this.getPkCompanyBranch());
        registry.setPkWarehouse(this.getPkWarehouse());
        
        for (SDbMaterialProvisionEntityWarehouse ew : this.getProvEntWhs()) {
            registry.getProvEntWhs().add(ew);
        }
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}

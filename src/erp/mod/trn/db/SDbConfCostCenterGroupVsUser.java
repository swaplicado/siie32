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
public class SDbConfCostCenterGroupVsUser extends SDbRegistryUser {

    protected int mnPkMatCostCenterGroup;
    
    protected ArrayList<SDbMaterialCostCenterGroupUser> maCostCenterGroupUser;
    
    protected SDbMaterialCostCenterGroup moAuxCCGrp;
    
    public SDbConfCostCenterGroupVsUser() {
        super(SModConsts.TRNX_CONF_CC_GRP_VS_USR);
    }

    public void setPkMatCostCenterGroup(int n) { mnPkMatCostCenterGroup = n; }
    
    public int getPkMatCostCenterGroup() { return mnPkMatCostCenterGroup; }
    
    public ArrayList<SDbMaterialCostCenterGroupUser> getCostCenterGroupUser() { return maCostCenterGroupUser; }
    
    public SDbMaterialCostCenterGroup getAuxCCGrp() { return moAuxCCGrp; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMatCostCenterGroup = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMatCostCenterGroup };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMatCostCenterGroup = 0;
        
        maCostCenterGroupUser = new ArrayList<>();
        
        moAuxCCGrp = null;
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
        SDbMaterialCostCenterGroupUser gu;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        mnPkMatCostCenterGroup = pk[0];
        
        statement = session.getDatabase().getConnection().createStatement();
        msSql = "SELECT id_mat_cc_grp, id_link, id_ref " + 
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CC_GRP_USR) + " " + 
                "WHERE id_mat_cc_grp = " + mnPkMatCostCenterGroup + " " +
                "ORDER BY id_link, id_ref";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            gu = new SDbMaterialCostCenterGroupUser();
            gu.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
            maCostCenterGroupUser.add(gu);
        }
        
        moAuxCCGrp = new SDbMaterialCostCenterGroup();
        moAuxCCGrp.read(session, pk);
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CC_GRP_USR) + " " + 
                "WHERE id_mat_cc_grp = " + mnPkMatCostCenterGroup + " ";
        session.getStatement().execute(msSql);
        for (SDbMaterialCostCenterGroupUser gu : maCostCenterGroupUser) {
            gu.setPkMatCostCenterGroup(mnPkMatCostCenterGroup);
            gu.setRegistryNew(true);
            gu.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbConfCostCenterGroupVsUser clone() throws CloneNotSupportedException {
        SDbConfCostCenterGroupVsUser registry = new SDbConfCostCenterGroupVsUser();
        
        registry.setPkMatCostCenterGroup(this.mnPkMatCostCenterGroup);
        
        for (SDbMaterialCostCenterGroupUser gu : this.getCostCenterGroupUser()) {
            registry.getCostCenterGroupUser().add(gu);
        }
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}

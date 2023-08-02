/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import erp.mod.usr.db.SDbUser;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbConfUserVsEntity extends SDbRegistryUser {
    
    protected int mnPkUser;
    
    protected ArrayList<SDbMaterialProvisionEntityUser> maProvEntUser;
    protected ArrayList<SDbMaterialConsumptionEntityUser> maConsEntUser;
    
    protected String moUserName;

    public SDbConfUserVsEntity() {
        super(SModConsts.TRNX_CONF_USR_VS_ENT);
    }
    
    public void setPkUser(int n) { mnPkUser = n; }
    
    public int getPkUser() { return mnPkUser; }
    
    public ArrayList<SDbMaterialProvisionEntityUser> getProvEntUser() { return maProvEntUser; }
    public ArrayList<SDbMaterialConsumptionEntityUser> getConsEntUser() { return maConsEntUser; }
    
    public String getUserName() { return moUserName; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkUser = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkUser };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkUser = 0;
        
        maProvEntUser = new ArrayList<>();
        maConsEntUser = new ArrayList<>();
        
        moUserName = "";
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
        SDbMaterialProvisionEntityUser prov;
        SDbMaterialConsumptionEntityUser cons;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        mnPkUser = pk[0];
        
        statement = session.getDatabase().getConnection().createStatement();
        msSql = "SELECT id_mat_prov_ent, id_usr " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT_USR) + " " + 
                "WHERE id_usr = " + mnPkUser + " ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            prov = new SDbMaterialProvisionEntityUser();
            prov.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            maProvEntUser.add(prov);
        }
        
        msSql = "SELECT id_mat_cons_ent, id_usr " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_USR) + " " + 
                "WHERE id_usr = " + mnPkUser + " ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            cons = new SDbMaterialConsumptionEntityUser();
            cons.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            maConsEntUser.add(cons);
        }
        
        SDbUser moAuxUser = new SDbUser();
        moUserName = (String) moAuxUser.readField(session.getStatement(), pk, SDbRegistry.FIELD_NAME);
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT_USR) + " " +
                "WHERE id_usr = " + mnPkUser + " ";
        session.getStatement().execute(msSql);
        for (SDbMaterialProvisionEntityUser ent : maProvEntUser) {
            ent.setPkUserId(mnPkUser);
            ent.setRegistryNew(true);
            ent.save(session);
        }
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_USR) + " " +
                "WHERE id_usr = " + mnPkUser + " ";
        session.getStatement().execute(msSql);
        for (SDbMaterialConsumptionEntityUser ent : maConsEntUser) {
            ent.setPkUserId(mnPkUser);
            ent.setRegistryNew(true);
            ent.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbConfUserVsEntity clone() throws CloneNotSupportedException {
        SDbConfUserVsEntity registry = new SDbConfUserVsEntity();
        
        registry.setPkUser(this.getPkUser());
        
        for (SDbMaterialProvisionEntityUser eu : this.getProvEntUser()) {
            registry.getProvEntUser().add(eu);
        }
        
        for (SDbMaterialConsumptionEntityUser eu : this.getConsEntUser()) {
            registry.getConsEntUser().add(eu);
        }
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}

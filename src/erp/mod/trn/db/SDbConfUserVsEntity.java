/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
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
    
    protected ArrayList<SDbMaterialConsumptionEntityUser> maConsEntUser;
    protected ArrayList<SDbMaterialConsumptionSubentityUser> maConsSubentUser;
    protected ArrayList<SDbMaterialProvisionEntityUser> maProvEntUser;
    
    protected String moUserName;

    public SDbConfUserVsEntity() {
        super(SModConsts.TRNX_CONF_USR_VS_ENT);
    }
    
    public void setPkUser(int n) { mnPkUser = n; }
    
    public int getPkUser() { return mnPkUser; }
    
    public ArrayList<SDbMaterialConsumptionEntityUser> getConsEntUser() { return maConsEntUser; }
    public ArrayList<SDbMaterialConsumptionSubentityUser> getConsSubentUser() { return maConsSubentUser; }
    public ArrayList<SDbMaterialProvisionEntityUser> getProvEntUser() { return maProvEntUser; }
    
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
        
        maConsEntUser = new ArrayList<>();
        maConsSubentUser = new ArrayList<>();
        maProvEntUser = new ArrayList<>();
        
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
        SDbMaterialConsumptionEntityUser consE;
        SDbMaterialConsumptionSubentityUser consS;
        SDbMaterialProvisionEntityUser prov;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        mnPkUser = pk[0];
        
        statement = session.getDatabase().getConnection().createStatement();
        
        msSql = "SELECT id_mat_cons_ent, id_link, id_ref " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_USR) + " " + 
                "WHERE id_link = " + SModSysConsts.USRS_LINK_USR + " AND id_ref = " + mnPkUser + " ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            consE = new SDbMaterialConsumptionEntityUser();
            consE.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
            maConsEntUser.add(consE);
        }
        
        msSql = "SELECT id_mat_cons_ent, id_mat_cons_subent, id_link, id_ref " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT_USR) + " " + 
                "WHERE id_link = " + SModSysConsts.USRS_LINK_USR + " AND id_ref = " + mnPkUser + " ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            consS = new SDbMaterialConsumptionSubentityUser();
            consS.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4) });
            maConsSubentUser.add(consS);
        }
        
        msSql = "SELECT id_mat_prov_ent, id_usr " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT_USR) + " " + 
                "WHERE id_usr = " + mnPkUser + " ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            prov = new SDbMaterialProvisionEntityUser();
            prov.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            maProvEntUser.add(prov);
        }
        
        SDbUser moAuxUser = new SDbUser();
        moUserName = (String) moAuxUser.readField(session.getStatement(), pk, SDbRegistry.FIELD_NAME);
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_USR) + " " +
                "WHERE id_link = " + SModSysConsts.USRS_LINK_USR + " AND id_ref = " + mnPkUser + " ";
        session.getStatement().execute(msSql);
        for (SDbMaterialConsumptionEntityUser ent : maConsEntUser) {
            ent.setPkLinkId(SModSysConsts.USRS_LINK_USR);
            ent.setPkReferenceId(mnPkUser);
            ent.setRegistryNew(true);
            ent.save(session);
        }
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT_USR) + " " +
                "WHERE id_link = " + SModSysConsts.USRS_LINK_USR + " AND id_ref = " + mnPkUser + " ";
        session.getStatement().execute(msSql);
        for (SDbMaterialConsumptionSubentityUser ent : maConsSubentUser) {
            ent.setPkLinkId(SModSysConsts.USRS_LINK_USR);
            ent.setPkReferenceId(mnPkUser);
            ent.setRegistryNew(true);
            ent.save(session);
        }
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT_USR) + " " +
                "WHERE id_usr = " + mnPkUser + " ";
        session.getStatement().execute(msSql);
        for (SDbMaterialProvisionEntityUser ent : maProvEntUser) {
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
        
        for (SDbMaterialConsumptionEntityUser eu : this.getConsEntUser()) {
            registry.getConsEntUser().add(eu);
        }
        
        for (SDbMaterialConsumptionSubentityUser eu : this.getConsSubentUser()) {
            registry.getConsSubentUser().add(eu);
        }
        
        for (SDbMaterialProvisionEntityUser eu : this.getProvEntUser()) {
            registry.getProvEntUser().add(eu);
        }
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}

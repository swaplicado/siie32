/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SDbBizPartner;
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
public class SDbConfEmployeeVsEntity extends SDbRegistryUser {
    
    protected int mnPkEmployee;
    
    protected ArrayList<SDbMaterialConsumptionEntityUser> maConsEntEmp;
    protected ArrayList<SDbMaterialConsumptionSubentityUser> maConsSubentEmp;
    
    protected SDbBizPartner moAuxBizPartner;

    public SDbConfEmployeeVsEntity() {
        super(SModConsts.TRNX_CONF_EMP_VS_ENT);
    }

    public void setPkEmployee(int n) { mnPkEmployee = n; }
    
    public int getPkEmployee() { return mnPkEmployee; }
    
    public ArrayList<SDbMaterialConsumptionEntityUser> getConsEntEmp() { return maConsEntEmp; }
    public ArrayList<SDbMaterialConsumptionSubentityUser> getConsSubentEmp() { return maConsSubentEmp; }
    
    public SDbBizPartner getAuxBizPartner() { return moAuxBizPartner; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEmployee = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEmployee };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkEmployee = 0;
        
        maConsEntEmp = new ArrayList<>();
        maConsSubentEmp = new ArrayList<>();
        
        moAuxBizPartner = null;
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
        SDbMaterialConsumptionEntityUser ent;
        SDbMaterialConsumptionSubentityUser sub;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        mnPkEmployee = pk[0];
        
        statement = session.getDatabase().getConnection().createStatement();
        msSql = "SELECT id_mat_cons_ent, id_link, id_ref " + 
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_USR) + " " +
                "WHERE id_link = " + SModSysConsts.USRS_LINK_EMP + " AND id_ref = " + mnPkEmployee + " ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            ent = new SDbMaterialConsumptionEntityUser();
            ent.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
            maConsEntEmp.add(ent);
        }
        
        msSql = "SELECT id_mat_cons_ent, id_mat_cons_subent, id_link, id_ref " + 
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT_USR) + " " +
                "WHERE id_link = " + SModSysConsts.USRS_LINK_EMP + " AND id_ref = " + mnPkEmployee + " ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            sub = new SDbMaterialConsumptionSubentityUser();
            sub.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4) });
            maConsSubentEmp.add(sub);
        }
        
        moAuxBizPartner = new SDbBizPartner();
        moAuxBizPartner.read(session, pk);
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_USR) + " " +
                "WHERE id_link = " + SModSysConsts.USRS_LINK_EMP + " AND id_ref = " + mnPkEmployee + " ";
        session.getStatement().execute(msSql);
        for (SDbMaterialConsumptionEntityUser ent : maConsEntEmp) {
            ent.setPkLinkId(SModSysConsts.USRS_LINK_EMP);
            ent.setPkReferenceId(mnPkEmployee);
            ent.setRegistryNew(true);
            ent.save(session);
        }
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT_USR) + " " +
                "WHERE id_link = " + SModSysConsts.USRS_LINK_EMP + " AND id_ref = " + mnPkEmployee + " ";
        session.getStatement().execute(msSql);
        for (SDbMaterialConsumptionSubentityUser ent : maConsSubentEmp) {
            ent.setPkLinkId(SModSysConsts.USRS_LINK_EMP);
            ent.setPkReferenceId(mnPkEmployee);
            ent.setRegistryNew(true);
            ent.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbConfEmployeeVsEntity clone() throws CloneNotSupportedException {
        SDbConfEmployeeVsEntity registry = new SDbConfEmployeeVsEntity();
        
        registry.setPkEmployee(this.getPkEmployee());
        
        for (SDbMaterialConsumptionEntityUser ee : this.getConsEntEmp()) {
            registry.getConsEntEmp().add(ee);
        }
        
        for (SDbMaterialConsumptionSubentityUser ee : this.getConsSubentEmp()) {
            registry.getConsSubentEmp().add(ee);
        }
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}

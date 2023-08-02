/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
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
    
    protected ArrayList<SDbMaterialConsumptionEntityEmployee> maConsEntEmp;
    
    protected SDbBizPartner moAuxBizPartner;

    public SDbConfEmployeeVsEntity() {
        super(SModConsts.TRNX_CONF_EMP_VS_ENT);
    }

    public void setPkEmployee(int n) { mnPkEmployee = n; }
    
    public int getPkEmployee() { return mnPkEmployee; }
    
    public ArrayList<SDbMaterialConsumptionEntityEmployee> getConsEntEmp() { return maConsEntEmp; }
    
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
        SDbMaterialConsumptionEntityEmployee cons;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        mnPkEmployee = pk[0];
        
        statement = session.getDatabase().getConnection().createStatement();
        msSql = "SELECT id_mat_cons_ent, id_emp " + 
                "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_EMP) + " " +
                "WHERE id_emp = " + mnPkEmployee + " ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            cons = new SDbMaterialConsumptionEntityEmployee();
            cons.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            maConsEntEmp.add(cons);
        }
        
        moAuxBizPartner = new SDbBizPartner();
        moAuxBizPartner.read(session, pk);
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT_EMP) + " " +
                "WHERE id_emp = " + mnPkEmployee + " ";
        session.getStatement().execute(msSql);
        for (SDbMaterialConsumptionEntityEmployee ent : maConsEntEmp) {
            ent.setPkEmployeeId(mnPkEmployee);
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
        
        for (SDbMaterialConsumptionEntityEmployee ee : this.getConsEntEmp()) {
            registry.getConsEntEmp().add(ee);
        }
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}

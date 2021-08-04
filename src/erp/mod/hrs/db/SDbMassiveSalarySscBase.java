/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.client.SClientInterface;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataEmployee;
import erp.mod.SModConsts;
import erp.mod.hrs.db.ssc.SSalarySscBase;
import java.sql.SQLException;
import java.util.ArrayList;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbMassiveSalarySscBase extends SDbRegistryUser {
    
    protected ArrayList<SSalarySscBase> maSalarySscBases;
            
    public SDbMassiveSalarySscBase() {
        super(SModConsts.HRSX_EMP_MASSIVE_UPD_SSC);
    }
    
    public ArrayList<SSalarySscBase> getSalarySscBases() { return maSalarySscBases; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] getPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        maSalarySscBases = new ArrayList<>();
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        for (SSalarySscBase salarySscBase : maSalarySscBases) {
            SDataEmployee employee = (SDataEmployee) SDataUtilities.readRegistry((SClientInterface) session.getClient(), SModConsts.HRSU_EMP, new int[] { salarySscBase.EmployeeId }, SLibConstants.EXEC_MODE_STEALTH);         
            employee.setPkEmployeeId(salarySscBase.EmployeeId);
            employee.setSalarySscBase(salarySscBase.SalarySscBase);
            employee.setDateSalarySscBase(salarySscBase.DateSalarySscBase);
            employee.setFkUserUpdateId(salarySscBase.UserId);
            employee.setAuxSalarySscBase(salarySscBase.SalarySscBase);
            employee.setAuxDateSalarySscBase(salarySscBase.DateSalarySscBase);
                    
            SDataUtilities.saveRegistry((SClientInterface) session.getClient(), employee);
        }

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMassiveSalarySscBase clone() throws CloneNotSupportedException {
        SDbMassiveSalarySscBase registry = new SDbMassiveSalarySscBase();

        for (SSalarySscBase salarySscBase : maSalarySscBases) {
            registry.getSalarySscBases().add(new SSalarySscBase(salarySscBase));
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

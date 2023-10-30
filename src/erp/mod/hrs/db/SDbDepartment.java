/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores, Edwin Carmona, Sergio Flores
 */
public class SDbDepartment extends SDbRegistryUser {

    protected int mnPkDepartmentId;
    protected String msCode;
    protected String msName;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkSuperiorDepartmentId_n;
    protected int mnFkTitularEmployeeId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected SDbCfgAccountingDepartment moChildCfgAccountingDepartment;

    public SDbDepartment() {
        super(SModConsts.HRSU_DEP);
    }

    public void setPkDepartmentId(int n) { mnPkDepartmentId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkSuperiorDepartmentId_n(int n) { mnFkSuperiorDepartmentId_n = n; }
    public void setFkTitularEmployeeId_n(int n) { mnFkTitularEmployeeId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkDepartmentId() { return mnPkDepartmentId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkSuperiorDepartmentId_n() { return mnFkSuperiorDepartmentId_n; }
    public int getFkTitularEmployeeId_n() { return mnFkTitularEmployeeId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setChildCfgAccountingDepartment(SDbCfgAccountingDepartment o) { moChildCfgAccountingDepartment = o; }
    
    public SDbCfgAccountingDepartment getChildCfgAccountingDepartment() { return moChildCfgAccountingDepartment; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDepartmentId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDepartmentId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDepartmentId = 0;
        msCode = "";
        msName = "";
        mbDeleted = false;
        mbSystem = false;
        mnFkSuperiorDepartmentId_n = 0;
        mnFkTitularEmployeeId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        moChildCfgAccountingDepartment = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_dep = " + mnPkDepartmentId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_dep = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkDepartmentId = 0;

        msSql = "SELECT COALESCE(MAX(id_dep), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkDepartmentId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkDepartmentId = resultSet.getInt("id_dep");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkSuperiorDepartmentId_n = resultSet.getInt("fk_dep_sup_n");
            mnFkTitularEmployeeId_n = resultSet.getInt("fk_emp_head_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // read child registry:
            
            msSql = "SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.HRS_CFG_ACC_DEP) + getSqlWhere();
            resultSet = session.getStatement().executeQuery(msSql);
            if (resultSet.getInt(1) > 0) {
                moChildCfgAccountingDepartment = (SDbCfgAccountingDepartment) session.readRegistry(SModConsts.HRS_CFG_ACC_DEP, new int[] { mnPkDepartmentId });
            }

            // finish reading this registry:

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkDepartmentId + ", " +
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    (mnFkSuperiorDepartmentId_n == 0 ? "NULL" : mnFkSuperiorDepartmentId_n) + ", " +
                    (mnFkTitularEmployeeId_n == 0 ? "NULL" : mnFkTitularEmployeeId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            // validar que no haya dependencias circulares con los departamentos superiores:
            
            ArrayList<Integer> ids = new ArrayList<>();
            ids.add(mnPkDepartmentId);
            
            if (!this.validateCircleDepartment(session, ids, mnFkSuperiorDepartmentId_n)) {
                throw new Exception("No se puede guardar el registro, error dependencia circular.");
            }
            
            // guardar el registro:
            
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_dep = " + mnPkDepartmentId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_dep_sup_n = " + (mnFkSuperiorDepartmentId_n == 0 ? "NULL" : mnFkSuperiorDepartmentId_n) + ", " +
                    "fk_emp_head_n = " + (mnFkTitularEmployeeId_n == 0 ? "NULL" : mnFkTitularEmployeeId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // save accounting configuration:
        
        int payrollAccProcess = SLibUtils.parseInt(SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_HRS_PAYROLL_ACC_PROCESS));
        
        switch (payrollAccProcess) {
            case SHrsConsts.CFG_ACC_PROCESS_ORIGINAL:
                if (mbRegistryNew) {
                    SHrsAccounting accounting = new SHrsAccounting(session);

                    accounting.setAccountingType(SModSysConsts.HRSS_TP_ACC_DEP);
                    accounting.setPkReferenceId(mnPkDepartmentId);
                    accounting.setFkUserInsertId(mnFkUserInsertId);
                    accounting.setFkUserUpdateId(mnFkUserUpdateId);

                    accounting.save();
                }
                break;
                
            case SHrsConsts.CFG_ACC_PROCESS_DYNAMIC:
                if (moChildCfgAccountingDepartment != null) {
                    moChildCfgAccountingDepartment.setPkDepartmentId(mnPkDepartmentId);
                    moChildCfgAccountingDepartment.setDeleted(mbDeleted);
                    moChildCfgAccountingDepartment.save(session);
                }
                break;
                
            default:
                // nothing
        }
        
        // finish saving this registry:
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }
    
    private boolean validateCircleDepartment(SGuiSession session, ArrayList<Integer> ids, int idDept) throws Exception {
        String sql = "SELECT fk_dep_sup_n FROM " + getSqlTable() + " WHERE id_dep = " + idDept + ";";
        
        if (idDept == 0) {
            return true;
        }
        
        int supDept = 0;
        try {
            ResultSet resultSet = session.getStatement().executeQuery(sql);
            
            if (! resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                supDept = resultSet.getInt("fk_dep_sup_n");
                
                if (supDept == 0) {
                    return true;
                }
                else {
                    if (ids.contains(supDept)) {
                        return false;
                    }
                    else {
                        ids.add(idDept);
                        return this.validateCircleDepartment(session, ids, supDept);
                    }
                }
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SDbDepartment.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    @Override
    public SDbDepartment clone() throws CloneNotSupportedException {
        SDbDepartment registry = new SDbDepartment();

        registry.setPkDepartmentId(this.getPkDepartmentId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkSuperiorDepartmentId_n(this.getFkSuperiorDepartmentId_n());
        registry.setFkTitularEmployeeId_n(this.getFkTitularEmployeeId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        if (moChildCfgAccountingDepartment != null) {
            registry.setChildCfgAccountingDepartment(this.getChildCfgAccountingDepartment().clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

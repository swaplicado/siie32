/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbMaterialConsumptionEntityBudget extends SDbRegistryUser {
    
    protected int mnPkMatConsumptionEntityId;
    protected int mnPkYearId;
    protected int mnPkPeriodId;
    protected Date mtDateStart;
    protected Date mtDateEnd;
    protected double mdBudget;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbMaterialConsumptionEntityBudget() {
        super(SModConsts.TRN_MAT_CONS_ENT_BUDGET);
    }
    
    public void setPkMatConsumptionEntityId(int n) { mnPkMatConsumptionEntityId = n; }
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkPeriodId(int n) { mnPkPeriodId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }
    public void setBudget(double d) { mdBudget = d; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkMatConsumptionEntityId() { return mnPkMatConsumptionEntityId; }
    public int getPkYearId() { return mnPkYearId; }
    public int getPkPeriodId() { return mnPkPeriodId; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public double getBudget() { return mdBudget; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMatConsumptionEntityId = pk[0];
        mnPkYearId = pk[1];
        mnPkPeriodId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMatConsumptionEntityId, mnPkYearId, mnPkPeriodId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMatConsumptionEntityId = 0;
        mnPkYearId = 0;
        mnPkPeriodId = 0;
        mtDateStart = null;
        mtDateEnd = null;
        mdBudget = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mat_cons_ent = " + mnPkMatConsumptionEntityId + " " +
                "AND id_year = " + mnPkYearId + " " + 
                "AND id_period = " + mnPkPeriodId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mat_cons_ent = " + pk[0] + " " +
                "AND id_year = " + pk[1] + " " + 
                "AND id_period = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkMatConsumptionEntityId = resultSet.getInt("id_mat_cons_ent");
            mnPkYearId = resultSet.getInt("id_year");
            mnPkPeriodId = resultSet.getInt("id_period");
            mtDateStart = resultSet.getDate("dt_start");
            mtDateEnd = resultSet.getDate("dt_end");
            mdBudget = resultSet.getDouble("budget");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkMatConsumptionEntityId + ", " + 
                    mnPkYearId + ", " + 
                    mnPkPeriodId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " + 
                    mdBudget + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_mat_cons_ent = " + mnPkMatConsumptionEntityId + ", " +
                    //"id_year = " + mnPkYearId + ", " +
                    "id_period = " + mnPkPeriodId + ", " +
                    "dt_start = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end = '" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    "budget = " + mdBudget + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMaterialConsumptionEntityBudget clone() throws CloneNotSupportedException {
        SDbMaterialConsumptionEntityBudget registry = new SDbMaterialConsumptionEntityBudget();
        
        registry.setPkMatConsumptionEntityId(this.getPkMatConsumptionEntityId());
        registry.setPkYearId(this.getPkYearId());
        registry.setPkPeriodId(this.getPkPeriodId());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd(this.getDateEnd());
        registry.setBudget(this.getBudget());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}

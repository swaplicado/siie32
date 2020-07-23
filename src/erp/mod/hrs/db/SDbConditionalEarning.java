/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

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
 * @author Edwin Carmona
 */
public class SDbConditionalEarning extends SDbRegistryUser {

    protected int mnPkConditionalEarningId;
    protected Date mtDateStart;
    protected double mdAmount;
    protected double mdPercentage;
    //protected boolean mbDeleted;
    //protected boolean mbSystem;
    protected int mnFkEarningId;
    protected int mnFkScopeId;
    protected int mnFkReferenceId;
    protected int mnFkBonusId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected SDbPayrollReceiptEarningComplement moChildEarningComplement;

    public SDbConditionalEarning() {
        super(SModConsts.HRS_COND_EAR);
    }

    public void setPkCondEarningId(int n) { mnPkConditionalEarningId = n; }
    public void setStartDate(Date t) { mtDateStart = t; }
    public void setAmount(double d) { mdAmount = d; }
    public void setPercentage(double d) { mdPercentage = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkEarningId(int n) { mnFkEarningId = n; }
    public void setFkScopeId(int n) { mnFkScopeId = n; }
    public void setFkReferenceId(int n) { mnFkReferenceId = n; }
    public void setFkBonusId(int n) { mnFkBonusId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkPayrollId() { return mnPkConditionalEarningId; }
    public Date getStartDate() { return mtDateStart; }
    public double getAmount() { return mdAmount; }
    public double getPercentage() { return mdPercentage; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkEarningId() { return mnFkEarningId; }
    public int getFkScopeId() { return mnFkScopeId; }
    public int getFkReferenceId() { return mnFkReferenceId; }
    public int getFkBonusId() { return mnFkBonusId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkConditionalEarningId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkConditionalEarningId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkConditionalEarningId = 0;
        mtDateStart = null;
        mdAmount = 0;
        mdPercentage = 0;
        mbDeleted = false;
        mbSystem = false;
        mnFkEarningId = 0;
        mnFkScopeId = 0;
        mnFkReferenceId = 0;
        mnFkBonusId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        moChildEarningComplement = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_cond_ear = " + mnPkConditionalEarningId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cond_ear = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkConditionalEarningId = 0;

        msSql = "SELECT COALESCE(MAX(id_cond_ear), 0) + 1 FROM " + getSqlTable() + "";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkConditionalEarningId = resultSet.getInt(1);
        }
    }
    
    private int existsKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        String smsSql = "SELECT id_cond_ear FROM " + getSqlTable() + " WHERE "
                + " fk_scope = " + mnFkScopeId 
                + " AND fk_ref = " + mnFkReferenceId 
                + " AND fk_ear = " + mnFkEarningId
                + " AND fk_bonus = " + mnFkBonusId
                + " AND dt_sta = '"+ SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "'; ";
        
        resultSet = session.getStatement().executeQuery(smsSql);
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        
        return 0;
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
            mnPkConditionalEarningId = resultSet.getInt("id_cond_ear");
            mtDateStart = resultSet.getDate("dt_sta");
            mdAmount = resultSet.getDouble("amt");
            mdPercentage = resultSet.getDouble("pct");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkEarningId = resultSet.getInt("fk_ear");
            mnFkScopeId = resultSet.getInt("fk_scope");
            mnFkReferenceId = resultSet.getInt("fk_ref");
            mnFkBonusId = resultSet.getInt("fk_bonus");
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
        
        int primaryKeyId = this.existsKey(session);
        
        if (primaryKeyId > 0 && primaryKeyId != mnPkConditionalEarningId)  {
            mnPkConditionalEarningId = primaryKeyId;
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            
            mbUpdatable = true;
            mbDisableable = true;
            mbDeletable = true;
            mbDisabled = false;
            mbDeleted = false;
            //mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkConditionalEarningId + ", " +
                    "'"+ SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " + 
                    mdAmount + ", " + 
                    mdPercentage + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkEarningId + ", " + 
                    mnFkScopeId + ", " + 
                    mnFkReferenceId + ", " +
                    mnFkBonusId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_cond_ear = " + mnPkConditionalEarningId + ", " +
                    "id_emp = " + mnPkEmployeeId + ", " +
                    "id_mov = " + mnPkMoveId + ", " +
                    */
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "amt = " + mdAmount + ", " +
                    "pct = " + mdPercentage + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_ear = " + mnFkEarningId + ", " +
                    "fk_scope = " + mnFkScopeId + ", " +
                    "fk_ref = " + mnFkReferenceId + ", " +
                    "fk_bonus = " + mnFkBonusId + ", " +
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
    public SDbConditionalEarning clone() throws CloneNotSupportedException {
        SDbConditionalEarning registry = new SDbConditionalEarning();

        registry.setPkCondEarningId(this.getPkPayrollId());
        registry.setStartDate(this.getStartDate());
        registry.setAmount(this.getAmount());
        registry.setPercentage(this.getPercentage());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkEarningId(this.getFkEarningId());
        registry.setFkScopeId(this.getFkScopeId());
        registry.setFkReferenceId(this.getFkReferenceId());
        registry.setFkBonusId(this.getFkBonusId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

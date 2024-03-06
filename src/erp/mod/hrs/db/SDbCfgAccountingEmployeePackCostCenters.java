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
 * @author Sergio Flores
 */
public class SDbCfgAccountingEmployeePackCostCenters extends SDbRegistryUser {

    protected int mnPkEmployeeId;
    protected int mnPkCfgAccountingId;
    protected Date mtDateStart;
    protected Date mtDateEnd_n;
    //protected boolean mbDeleted;
    protected int mnFkPackCostCentersId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbCfgAccountingEmployeePackCostCenters() {
        super(SModConsts.HRS_CFG_ACC_EMP_PACK_CC);
    }

    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setPkCfgAccountingId(int n) { mnPkCfgAccountingId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd_n(Date t) { mtDateEnd_n = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkPackCostCentersId(int n) { mnFkPackCostCentersId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getPkCfgAccountingId() { return mnPkCfgAccountingId; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd_n() { return mtDateEnd_n; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkPackCostCentersId() { return mnFkPackCostCentersId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEmployeeId = pk[0];
        mnPkCfgAccountingId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEmployeeId, mnPkCfgAccountingId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkEmployeeId = 0;
        mnPkCfgAccountingId = 0;
        mtDateStart = null;
        mtDateEnd_n = null;
        mbDeleted = false;
        mnFkPackCostCentersId = 0;
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
        return "WHERE id_emp = " + mnPkEmployeeId + " "
                + "AND id_cfg_acc = " + mnPkCfgAccountingId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_emp = " + pk[0] + " "
                + "AND id_cfg_acc = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkCfgAccountingId = 0;

        msSql = "SELECT COALESCE(MAX(id_cfg_acc), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_emp = " + mnPkEmployeeId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkCfgAccountingId = resultSet.getInt(1);
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
            mnPkEmployeeId = resultSet.getInt("id_emp");
            mnPkCfgAccountingId = resultSet.getInt("id_cfg_acc");
            mtDateStart = resultSet.getDate("dt_sta");
            mtDateEnd_n = resultSet.getDate("dt_end_n");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkPackCostCentersId = resultSet.getInt("fk_pack_cc");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // finish registry:

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
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkEmployeeId + ", " + 
                    mnPkCfgAccountingId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " + 
                    (mtDateEnd_n == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd_n) + "'") + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkPackCostCentersId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_emp = " + mnPkEmployeeId + ", " +
                    //"id_cfg_acc = " + mnPkCfgAccountingId + ", " +
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end_n = " + (mtDateEnd_n == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd_n) + "'") + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_pack_cc = " + mnFkPackCostCentersId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // finish registry:
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbCfgAccountingEmployeePackCostCenters clone() throws CloneNotSupportedException {
        SDbCfgAccountingEmployeePackCostCenters registry = new SDbCfgAccountingEmployeePackCostCenters();

        registry.setPkEmployeeId(this.getPkEmployeeId());
        registry.setPkCfgAccountingId(this.getPkCfgAccountingId());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd_n(this.getDateEnd_n());
        registry.setDeleted(this.isDeleted());
        registry.setFkPackCostCentersId(this.getFkPackCostCentersId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        // finish registry:

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

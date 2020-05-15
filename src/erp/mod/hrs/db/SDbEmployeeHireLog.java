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

/* IMPORTANT:
 * Every single change made to the definition of this class' table must be updated also in the following classes:
 * - erp.mbps.data.SDataEmployee
 * - erp.mod.hrs.db.SHrsEmployeeHireLog
 * All of them also make raw SQL insertions.
 */

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbEmployeeHireLog extends SDbRegistryUser {

    protected int mnPkEmployeeId;
    protected int mnPkLogId;
    protected Date mtDateHire;
    protected String msNotesHire;
    protected Date mtDateDismissal_n;
    protected String msNotesDismissal;
    protected boolean mbHired;
    //protected boolean mbDeleted;
    protected int mnFkEmployeeDismissalTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected String msAuxSchema;

    public SDbEmployeeHireLog() {
        super(SModConsts.HRS_EMP_LOG_HIRE);
    }

    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setPkLogId(int n) { mnPkLogId = n; }
    public void setDateHire(Date t) { mtDateHire = t; }
    public void setNotesHire(String s) { msNotesHire = s; }
    public void setDateDismissal_n(Date t) { mtDateDismissal_n = t; }
    public void setNotesDismissal(String s) { msNotesDismissal = s; }
    public void setHired(boolean b) { mbHired = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkEmployeeDismissalTypeId(int n) { mnFkEmployeeDismissalTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setAuxSchema(String s) { msAuxSchema = s; }

    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getPkLogId() { return mnPkLogId; }
    public Date getDateHire() { return mtDateHire; }
    public String getNotesHire() { return msNotesHire; }
    public Date getDateDismissal_n() { return mtDateDismissal_n; }
    public String getNotesDismissal() { return msNotesDismissal; }
    public boolean isHired() { return mbHired; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkEmployeeDismissalTypeId() { return mnFkEmployeeDismissalTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public String getAuxSchema() { return msAuxSchema; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEmployeeId = pk[0];
        mnPkLogId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEmployeeId, mnPkLogId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkEmployeeId = 0;
        mnPkLogId = 0;
        mtDateHire = null;
        msNotesHire = "";
        mtDateDismissal_n = null;
        msNotesDismissal = "";
        mbHired = false;
        mbDeleted = false;
        mnFkEmployeeDismissalTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        msAuxSchema = "";
    }

    @Override
    public String getSqlTable() {
        return (msAuxSchema.isEmpty() ? "" : (msAuxSchema + ".")) + SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_emp = " + mnPkEmployeeId + " AND id_log = " + mnPkLogId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_emp = " + pk[0] + " AND id_log = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkLogId = 0;

        msSql = "SELECT COALESCE(MAX(id_log), 0) + 1 FROM " + getSqlTable() + " WHERE id_emp = " + mnPkEmployeeId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkLogId = resultSet.getInt(1);
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
            mnPkLogId = resultSet.getInt("id_log");
            mtDateHire = resultSet.getDate("dt_hire");
            msNotesHire = resultSet.getString("nts_hire");
            mtDateDismissal_n = resultSet.getDate("dt_dis_n");
            msNotesDismissal = resultSet.getString("nts_dis");
            mbHired = resultSet.getBoolean("b_hire");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkEmployeeDismissalTypeId = resultSet.getInt("fk_tp_emp_dis");
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
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkEmployeeId + ", " + 
                    mnPkLogId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateHire) + "', " + 
                    "'" + msNotesHire + "', " + 
                    (mtDateDismissal_n == null ? null : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateDismissal_n)+ "' ") + ", " + 
                    "'" + msNotesDismissal + "', " + 
                    (mbHired ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkEmployeeDismissalTypeId + ", " + 
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
                    "id_emp = " + mnPkEmployeeId + ", " +
                    "id_log = " + mnPkLogId + ", " +
                    */
                    "dt_hire = '" + SLibUtils.DbmsDateFormatDate.format(mtDateHire) + "', " +
                    "nts_hire = '" + msNotesHire + "', " +
                    "dt_dis_n = " + (mtDateDismissal_n == null ? null : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateDismissal_n)+ "' ") + ", " +
                    "nts_dis = '" + msNotesDismissal + "', " +
                    "b_hire = " + (mbHired ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_emp_dis = " + mnFkEmployeeDismissalTypeId + ", " +
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
    public SDbEmployeeHireLog clone() throws CloneNotSupportedException {
        SDbEmployeeHireLog registry = new SDbEmployeeHireLog();

        registry.setPkEmployeeId(this.getPkEmployeeId());
        registry.setPkLogId(this.getPkLogId());
        registry.setDateHire(this.getDateHire());
        registry.setNotesHire(this.getNotesHire());
        registry.setDateDismissal_n(this.getDateDismissal_n());
        registry.setNotesDismissal(this.getNotesDismissal());
        registry.setHired(this.isHired());
        registry.setDeleted(this.isDeleted());
        registry.setFkEmployeeDismissalTypeId(this.getFkEmployeeDismissalTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setAuxSchema(this.getAuxSchema());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

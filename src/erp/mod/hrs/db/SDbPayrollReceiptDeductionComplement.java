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
 * @author Juan Barajas
 */
public class SDbPayrollReceiptDeductionComplement extends SDbRegistryUser {

    protected int mnPkPayrollId;
    protected int mnPkEmployeeId;
    protected int mnPkMoveId;
    protected Date mtDate;
    protected String msNotes;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbPayrollReceiptDeductionComplement() {
        super(SModConsts.HRS_PAY_RCP_DED_CMP);
    }

    /*
     * Private methods
     */

    public void setPkPayrollId(int n) { mnPkPayrollId = n; }
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setPkMoveId(int n) { mnPkMoveId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setNotes(String s) { msNotes = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkPayrollId() { return mnPkPayrollId; }
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getPkMoveId() { return mnPkMoveId; }
    public Date getDate() { return mtDate; }
    public String getNotes() { return msNotes; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPayrollId = pk[0];
        mnPkEmployeeId = pk[1];
        mnPkMoveId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPayrollId, mnPkEmployeeId, mnPkMoveId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkPayrollId = 0;
        mnPkEmployeeId = 0;
        mnPkMoveId = 0;
        mtDate = null;
        msNotes = "";
        mbDeleted = false;
        mbSystem = false;
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
        return "WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId + " AND id_mov = " + mnPkMoveId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_pay = " + pk[0] + " AND id_emp = " + pk[1] + " AND id_mov = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
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
            mnPkPayrollId = resultSet.getInt("id_pay");
            mnPkEmployeeId = resultSet.getInt("id_emp");
            mnPkMoveId = resultSet.getInt("id_mov");
            mtDate = resultSet.getDate("dt");
            msNotes = resultSet.getString("nts");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
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
            verifyRegistryNew(session);
        }
        
        if (mbRegistryNew) {
            //computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkPayrollId + ", " + 
                    mnPkEmployeeId + ", " + 
                    mnPkMoveId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                    "'" + msNotes + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
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
                    "id_cl_abs = " + mnPkAbsenceClassId + ", " +
                    "id_pay = " + mnPkPayrollId + ", " +
                    "id_emp = " + mnPkEmployeeId + ", " +
                    "id_mov = " + mnPkMoveId + ", " +
                    */
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "nts = '" + msNotes + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
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
    public SDbPayrollReceiptDeductionComplement clone() throws CloneNotSupportedException {
        SDbPayrollReceiptDeductionComplement registry = new SDbPayrollReceiptDeductionComplement();

        registry.setPkPayrollId(this.getPkPayrollId());
        registry.setPkEmployeeId(this.getPkEmployeeId());
        registry.setPkMoveId(this.getPkMoveId());
        registry.setDate(this.getDate());
        registry.setNotes(this.getNotes());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}

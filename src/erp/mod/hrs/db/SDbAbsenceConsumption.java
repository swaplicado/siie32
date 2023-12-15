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
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbAbsenceConsumption extends SDbRegistryUser implements SGridRow {

    protected int mnPkEmployeeId;
    protected int mnPkAbsenceId;
    protected int mnPkConsumptionId;
    protected Date mtDateStart;
    protected Date mtDateEnd;
    protected int mnEffectiveDays;
    //protected boolean mbDeleted;
    protected int mnFkReceiptPayrollId;
    protected int mnFkReceiptEmployeeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected boolean mbAuxIsClockSourced;
    
    protected SDbAbsence moParentAbsence;

    public SDbAbsenceConsumption() {
        super(SModConsts.HRS_ABS_CNS);
    }

    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setPkAbsenceId(int n) { mnPkAbsenceId = n; }
    public void setPkConsumptionId(int n) { mnPkConsumptionId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }
    public void setEffectiveDays(int n) { mnEffectiveDays = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkReceiptPayrollId(int n) { mnFkReceiptPayrollId = n; }
    public void setFkReceiptEmployeeId(int n) { mnFkReceiptEmployeeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getPkAbsenceId() { return mnPkAbsenceId; }
    public int getPkConsumptionId() { return mnPkConsumptionId; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public int getEffectiveDays() { return mnEffectiveDays; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkReceiptPayrollId() { return mnFkReceiptPayrollId; }
    public int getFkReceiptEmployeeId() { return mnFkReceiptEmployeeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public boolean isAuxIsClockSourced() { return mbAuxIsClockSourced; }
    
    public void setAuxIsClockSourced(boolean mbAuxIsClockSourced) { this.mbAuxIsClockSourced = mbAuxIsClockSourced; }
    
    public void setParentAbsence(SDbAbsence o) { moParentAbsence = o; }
    
    public SDbAbsence getParentAbsence() { return moParentAbsence; }
    
    public int[] getAbsenceKey() { return new int[] { mnPkEmployeeId, mnPkAbsenceId }; }
    
    public int getCalendarDays() {
        return SLibTimeUtils.countPeriodDays(mtDateStart, mtDateEnd);
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEmployeeId = pk[0];
        mnPkAbsenceId = pk[1];
        mnPkConsumptionId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEmployeeId, mnPkAbsenceId, mnPkConsumptionId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkEmployeeId = 0;
        mnPkAbsenceId = 0;
        mnPkConsumptionId = 0;
        mtDateStart = null;
        mtDateEnd = null;
        mnEffectiveDays = 0;
        mbDeleted = false;
        mnFkReceiptPayrollId = 0;
        mnFkReceiptEmployeeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        mbAuxIsClockSourced = false;
        
        moParentAbsence = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_emp = " + mnPkEmployeeId + " AND "
                + "id_abs = " + mnPkAbsenceId + " AND "
                + "id_cns = " + mnPkConsumptionId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_emp = " + pk[0] + " AND "
                + "id_abs = " + pk[1] + " AND "
                + "id_cns = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkConsumptionId = 0;

        msSql = "SELECT COALESCE(MAX(id_cns), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_emp = " + mnPkEmployeeId + " AND id_abs = " + mnPkAbsenceId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkConsumptionId = resultSet.getInt(1);
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
            mnPkAbsenceId = resultSet.getInt("id_abs");
            mnPkConsumptionId = resultSet.getInt("id_cns");
            mtDateStart = resultSet.getDate("dt_sta");
            mtDateEnd = resultSet.getDate("dt_end");
            mnEffectiveDays = resultSet.getInt("eff_day");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkReceiptPayrollId = resultSet.getInt("fk_rcp_pay");
            mnFkReceiptEmployeeId = resultSet.getInt("fk_rcp_emp");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // Read parent registry:
            
            moParentAbsence = (SDbAbsence) session.readRegistry(SModConsts.HRS_ABS, new int[] { mnPkEmployeeId, mnPkAbsenceId });
            
            this.mbAuxIsClockSourced = moParentAbsence.isTimeClockSourced();
            
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
                    mnPkAbsenceId + ", " + 
                    mnPkConsumptionId + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    mnEffectiveDays + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkReceiptPayrollId + ", " + 
                    mnFkReceiptEmployeeId + ", " + 
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
                    "id_abs = " + mnPkAbsenceId + ", " +
                    "id_cns = " + mnPkConsumptionId + ", " +
                    */
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end = '" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    "eff_day = " + mnEffectiveDays + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_rcp_pay = " + mnFkReceiptPayrollId + ", " +
                    "fk_rcp_emp = " + mnFkReceiptEmployeeId + ", " +
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
    public SDbAbsenceConsumption clone() throws CloneNotSupportedException {
        SDbAbsenceConsumption registry = new SDbAbsenceConsumption();

        registry.setPkEmployeeId(this.getPkEmployeeId());
        registry.setPkAbsenceId(this.getPkAbsenceId());
        registry.setPkConsumptionId(this.getPkConsumptionId());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd(this.getDateEnd());
        registry.setEffectiveDays(this.getEffectiveDays());
        registry.setDeleted(this.isDeleted());
        registry.setFkReceiptPayrollId(this.getFkReceiptPayrollId());
        registry.setFkReceiptEmployeeId(this.getFkReceiptEmployeeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setAuxIsClockSourced(this.isAuxIsClockSourced());
        
        registry.setParentAbsence(this.getParentAbsence());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch (row) {
            case 0:
                value = moParentAbsence.getXtaAbsenceClass();
                break;
            case 1:
                value = moParentAbsence.getXtaAbsenceType();
                break;
            case 2:
                value = moParentAbsence.getNumber();
                break;
            case 3:
                value = moParentAbsence.getDateStart();
                break;
            case 4:
                value = moParentAbsence.getDateEnd();
                break;
            case 5:
                value = moParentAbsence.getEffectiveDays();
                break;
            case 6:
                value = mtDateStart;
                break;
            case 7:
                value = mtDateEnd;
                break;
            case 8:
                value = mnEffectiveDays;
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

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
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SDbAbsence extends SDbRegistryUser implements SGridRow {

    protected int mnPkEmployeeId;
    protected int mnPkAbsenceId;
    protected String msNumber;
    protected Date mtDate;
    protected Date mtDateStart;
    protected Date mtDateEnd;
    protected int mnEffectiveDays;
    protected int mnBenefitsYear;
    protected int mnBenefitsAniversary;
    protected String msNotes;
    protected boolean mbClosed;
    //protected boolean mbDeleted;
    protected int mnFkAbsenceClassId;
    protected int mnFkAbsenceTypeId;
    protected int mnFkUserClosedId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserClosed;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected boolean mbAuxIsAbsencePayable;
    protected int mnAuxPendingDays;
    protected String msAuxAbsenceClass;
    protected String msAuxAbsenceType;

    public SDbAbsence() {
        super(SModConsts.HRS_ABS);
    }

    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setPkAbsenceId(int n) { mnPkAbsenceId = n; }
    public void setNumber(String s) { msNumber = s; }
    public void setDate(Date t) { mtDate = t; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }
    public void setEffectiveDays(int n) { mnEffectiveDays = n; }
    public void setBenefitsYear(int n) { mnBenefitsYear = n; }
    public void setBenefitsAniversary(int n) { mnBenefitsAniversary = n; }
    public void setNotes(String s) { msNotes = s; }
    public void setClosed(boolean b) { mbClosed = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkAbsenceClassId(int n) { mnFkAbsenceClassId = n; }
    public void setFkAbsenceTypeId(int n) { mnFkAbsenceTypeId = n; }
    public void setFkUserClosedId(int n) { mnFkUserClosedId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserClosed(Date t) { mtTsUserClosed = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setAuxIsAbsencePayable(boolean b) { mbAuxIsAbsencePayable = b; }
    public void setAuxPendingDays(int n) { mnAuxPendingDays = n; }
    public void setAuxAbsenceClass(String s) { msAuxAbsenceClass = s; }
    public void setAuxAbsenceType(String s) { msAuxAbsenceType = s; }
    
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getPkAbsenceId() { return mnPkAbsenceId; }
    public String getNumber() { return msNumber; }
    public Date getDate() { return mtDate; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public int getEffectiveDays() { return mnEffectiveDays; }
    public int getBenefitsYear() { return mnBenefitsYear; }
    public int getBenefitsAniversary() { return mnBenefitsAniversary; }
    public String getNotes() { return msNotes; }
    public boolean isClosed() { return mbClosed; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkAbsenceClassId() { return mnFkAbsenceClassId; }
    public int getFkAbsenceTypeId() { return mnFkAbsenceTypeId; }
    public int getFkUserClosedId() { return mnFkUserClosedId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserClosed() { return mtTsUserClosed; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public boolean IsAuxAbsencePayable() { return mbAuxIsAbsencePayable; }
    public int getAuxPendingDays() { return mnAuxPendingDays; }
    public String getAuxAbsenceClass() { return msAuxAbsenceClass; }
    public String getAuxAbsenceType() { return msAuxAbsenceType; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEmployeeId = pk[0];
        mnPkAbsenceId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEmployeeId, mnPkAbsenceId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkEmployeeId = 0;
        mnPkAbsenceId = 0;
        msNumber = "";
        mtDate = null;
        mtDateStart = null;
        mtDateEnd = null;
        mnEffectiveDays = 0;
        mnBenefitsYear = 0;
        mnBenefitsAniversary = 0;
        msNotes = "";
        mbClosed = false;
        mbDeleted = false;
        mnFkAbsenceClassId = 0;
        mnFkAbsenceTypeId = 0;
        mnFkUserClosedId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserClosed = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        mbAuxIsAbsencePayable = false;
        mnAuxPendingDays = 0;
        msAuxAbsenceClass = "";
        msAuxAbsenceType = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_emp = " + mnPkEmployeeId + " AND "
                + "id_abs = " + mnPkAbsenceId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_emp = " + pk[0] + " AND "
                + "id_abs = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAbsenceId = 0;

        msSql = "SELECT COALESCE(MAX(id_abs), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_emp = " + mnPkEmployeeId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAbsenceId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;
        SDbAbsenceType absenceType = null;

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
            msNumber = resultSet.getString("num");
            mtDate = resultSet.getDate("dt");
            mtDateStart = resultSet.getDate("dt_sta");
            mtDateEnd = resultSet.getDate("dt_end");
            mnEffectiveDays = resultSet.getInt("eff_day");
            mnBenefitsYear = resultSet.getInt("ben_year");
            mnBenefitsAniversary = resultSet.getInt("ben_ann");
            msNotes = resultSet.getString("nts");
            mbClosed = resultSet.getBoolean("b_clo");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkAbsenceClassId = resultSet.getInt("fk_cl_abs");
            mnFkAbsenceTypeId = resultSet.getInt("fk_tp_abs");
            mnFkUserClosedId = resultSet.getInt("fk_usr_clo");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserClosed = resultSet.getTimestamp("ts_usr_clo");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // Read Absence type:
            
            absenceType = (SDbAbsenceType) session.readRegistry(SModConsts.HRSU_TP_ABS, new int[] { mnFkAbsenceClassId, mnFkAbsenceTypeId });
            msAuxAbsenceClass = (String) session.readField(SModConsts.HRSU_CL_ABS, new int[] { mnFkAbsenceClassId }, SDbAbsenceClass.FIELD_NAME);
            msAuxAbsenceType = (String) session.readField(SModConsts.HRSU_TP_ABS, new int[] { mnFkAbsenceClassId, mnFkAbsenceTypeId }, SDbAbsenceType.FIELD_NAME);
            
            if (absenceType != null) {
                mbAuxIsAbsencePayable = absenceType.isPayable();
            }

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
            mbClosed = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkEmployeeId + ", " +
                    mnPkAbsenceId + ", " +
                    "'" + msNumber + "', " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    mnEffectiveDays + ", " +
                    mnBenefitsYear + ", " + 
                    mnBenefitsAniversary + ", " + 
                    "'" + msNotes + "', " +
                    (mbClosed ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkAbsenceClassId + ", " +
                    mnFkAbsenceTypeId + ", " +
                    mnFkUserClosedId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
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
                    */
                    "num = '" + msNumber + "', " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end = '" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    "eff_day = " + mnEffectiveDays + ", " +
                    "ben_year = " + mnBenefitsYear + ", " +
                    "ben_ann = " + mnBenefitsAniversary + ", " +
                    "nts = '" + msNotes + "', " +
                    "b_clo = " + (mbClosed ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_cl_abs = " + mnFkAbsenceClassId + ", " +
                    "fk_tp_abs = " + mnFkAbsenceTypeId + ", " +
                    "fk_usr_clo = " + mnFkUserClosedId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_clo = " + "NOW()" + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbAbsence clone() throws CloneNotSupportedException {
        SDbAbsence registry = new SDbAbsence();

        registry.setPkEmployeeId(this.getPkEmployeeId());
        registry.setPkAbsenceId(this.getPkAbsenceId());
        registry.setNumber(this.getNumber());
        registry.setDate(this.getDate());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd(this.getDateEnd());
        registry.setEffectiveDays(this.getEffectiveDays());
        registry.setBenefitsYear(this.getBenefitsYear());
        registry.setBenefitsAniversary(this.getBenefitsAniversary());
        registry.setNotes(this.getNotes());
        registry.setClosed(this.isClosed());
        registry.setDeleted(this.isDeleted());
        registry.setFkAbsenceClassId(this.getFkAbsenceClassId());
        registry.setFkAbsenceTypeId(this.getFkAbsenceTypeId());
        registry.setFkUserClosedId(this.getFkUserClosedId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserClosed(this.getTsUserClosed());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

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
                value = msAuxAbsenceClass;
                break;
            case 1:
                value = msAuxAbsenceType;
                break;
            case 2:
                value = msNumber;
                break;
            case 3:
                value = mtDateStart;
                break;
            case 4:
                value = mtDateEnd;
                break;
            case 5:
                value = mnEffectiveDays;
                break;
            case 6:
                value = mnAuxPendingDays;
                break;
            default:
                break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch (row) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                break;
            default:
                break;
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
 * @author Néstor Ávalos, Sergio Flores, Claudio Peña, Sergio Flores
 */
public class SDbAbsence extends SDbRegistryUser implements SGridRow {

    protected int mnPkEmployeeId;
    protected int mnPkAbsenceId;
    protected String msNumber;
    protected Date mtDate;
    protected Date mtDateStart;
    protected Date mtDateEnd;
    protected int mnEffectiveDays;
    protected String msDisRisk;
    protected String msDisSequel;
    protected String msDisControl;
    protected int mnBenefitsYear;
    protected int mnBenefitsAnniversary;
    protected String msNotes;
    protected int mnExternarRequestId;
    protected boolean mbTimeClockSourced;
    protected boolean mbClosed;
    //protected boolean mbDeleted;
    protected int mnFkAbsenceClassId;
    protected int mnFkAbsenceTypeId;
    protected int mnFkSourcePayrollId_n;
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
    
    protected String msXtaAbsenceClass;
    protected String msXtaAbsenceType;
    protected boolean mbXtaAbsenceTypePayable;
    
    protected int mnAuxPendingDays;

    private boolean testDelete(SGuiSession session) throws Exception {
        ResultSet resultSet = null;
        boolean can = false;
        
        msSql = "SELECT COUNT(*) AS f_count FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ABS_CNS) + " " +
                getSqlWhere() + " AND b_del = 0 ";
        resultSet = session.getStatement().executeQuery(msSql);

        if (!resultSet.next() || resultSet.getInt("f_count") == 0) {
            can = true;
        }
        
        return can;
    }
    
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
    public void setDisRisk(String s) { msDisRisk = s; }
    public void setDisSequel(String s) { msDisSequel = s; }
    public void setDisControl(String s) { msDisControl = s; }
    public void setBenefitsYear(int n) { mnBenefitsYear = n; }
    public void setBenefitsAnniversary(int n) { mnBenefitsAnniversary = n; }
    public void setNotes(String s) { msNotes = s; }
    public void setExternarRequestId(int n) { mnExternarRequestId = n; }
    public void setTimeClockSourced(boolean b) { mbTimeClockSourced = b; }
    public void setClosed(boolean b) { mbClosed = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkAbsenceClassId(int n) { mnFkAbsenceClassId = n; }
    public void setFkAbsenceTypeId(int n) { mnFkAbsenceTypeId = n; }
    public void setFkSourcePayrollId_n(int n) { mnFkSourcePayrollId_n = n; }
    public void setFkUserClosedId(int n) { mnFkUserClosedId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserClosed(Date t) { mtTsUserClosed = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getPkAbsenceId() { return mnPkAbsenceId; }
    public String getNumber() { return msNumber; }
    public Date getDate() { return mtDate; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public int getEffectiveDays() { return mnEffectiveDays; }
    public String getDisRisk() { return msDisRisk; }
    public String getDisSequel() { return msDisSequel; }
    public String getDisControl() { return msDisControl; }
    public int getBenefitsYear() { return mnBenefitsYear; }
    public int getBenefitsAnniversary() { return mnBenefitsAnniversary; }
    public String getNotes() { return msNotes; }
    public int getExternarRequestId() { return mnExternarRequestId; }
    public boolean isTimeClockSourced() { return mbTimeClockSourced; }
    public boolean isClosed() { return mbClosed; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkAbsenceClassId() { return mnFkAbsenceClassId; }
    public int getFkAbsenceTypeId() { return mnFkAbsenceTypeId; }
    public int getFkSourcePayrollId_n() { return mnFkSourcePayrollId_n; }
    public int getFkUserClosedId() { return mnFkUserClosedId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserClosed() { return mtTsUserClosed; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public void setXtaAbsenceClass(String s) { msXtaAbsenceClass = s; }
    public void setXtaAbsenceType(String s) { msXtaAbsenceType = s; }
    public void setXtaAbsenceTypePayable(boolean b) { mbXtaAbsenceTypePayable = b; }
    
    public String getXtaAbsenceClass() { return msXtaAbsenceClass; }
    public String getXtaAbsenceType() { return msXtaAbsenceType; }
    public boolean isXtaAbsenceTypePayable() { return mbXtaAbsenceTypePayable; }
    
    public void setAuxPendingDays(int n) { mnAuxPendingDays = n; }
    public int getAuxPendingDays() { return mnAuxPendingDays; }
    
    public int[] getAbsenceClassKey() { return new int[] { mnFkAbsenceClassId }; }
    public int[] getAbsenceTypeKey() { return new int[] { mnFkAbsenceClassId, mnFkAbsenceTypeId }; }

    public int getCalendarDays() {
        return SLibTimeUtils.countPeriodDays(mtDateStart, mtDateEnd);
    }
    
    public boolean isDisability() {
        return SDbAbsenceClass.isDisability(mnFkAbsenceClassId);
    }

    public boolean isVacation() {
        return SDbAbsenceClass.isVacations(mnFkAbsenceClassId);
    }
    
    public String composeAbsenceDescription() {
        return msXtaAbsenceType + ", " + msNumber + " (" + SLibUtils.DateFormatDate.format(mtDateStart) + " - " + SLibUtils.DateFormatDate.format(mtDateEnd) + ")";
    }

    /**
     * Checks if this absence consumes calendar days (e.g., disabilities). Other absences consume instead only business days (e.g., vacation, absenteeism).
     * @return 
     */
    public boolean consumesCalendarDays() {
        return SDbAbsenceClass.consumesCalendarDays(mnFkAbsenceClassId);
    }
    
    /**
     * Gets the actual consumed days. Not just the ones previously storaged.
     * @param hrsEmployee
     * @return Actual consumed days.
     */
    public int getActualConsumedDays(final SHrsEmployee hrsEmployee) {
        int consumedDays = 0;
        
        // create a pack of previous and actual consumptions:
        ArrayList<ArrayList<SDbAbsenceConsumption>> absenceConsumptionsPack = new ArrayList<>();
        absenceConsumptionsPack.add(hrsEmployee.getAbsenceConsumptions());
        absenceConsumptionsPack.add(hrsEmployee.getHrsReceipt().getAbsenceConsumptions());
        
        // process pack of consumptions:
        for (ArrayList<SDbAbsenceConsumption> absenceConsumptions : absenceConsumptionsPack) {
            for (SDbAbsenceConsumption absenceConsumption : absenceConsumptions) {
                if (SLibUtils.compareKeys(getPrimaryKey(), absenceConsumption.getAbsenceKey())) {
                    consumedDays += absenceConsumption.getEffectiveDays();
                }
            }
        }
        
        return consumedDays;
    }
    
    /**
     * Gets the actual last day of consumption.
     * @param hrsEmployee
     * @return Actual last day of consumption if any, otherwise <code>null</code>.
     */
    public Date getActualLastConsumptionDate(final SHrsEmployee hrsEmployee) {
        Date lastConsumptionDate = null;
        
        // create a pack of previous and actual consumptions:
        ArrayList<ArrayList<SDbAbsenceConsumption>> consumptionsPack = new ArrayList<>();
        consumptionsPack.add(hrsEmployee.getAbsenceConsumptions());
        consumptionsPack.add(hrsEmployee.getHrsReceipt().getAbsenceConsumptions());
        
        // process pack of consumptions:
        for (ArrayList<SDbAbsenceConsumption> consumptions : consumptionsPack) {
            for (SDbAbsenceConsumption consumption : consumptions) {
                if (SLibUtils.compareKeys(getPrimaryKey(), consumption.getAbsenceKey())) {
                    if (lastConsumptionDate == null || lastConsumptionDate.before(consumption.getDateEnd())) {
                        lastConsumptionDate = consumption.getDateEnd();
                    }
                }
            }
        }
        
        return lastConsumptionDate;
    }
    
    /**
     * Gets the actual next consumption ID.
     * @param hrsEmployee
     * @return Actual next consumption ID.
     */
    public int getActualNextConsumptionId(final SHrsEmployee hrsEmployee) {
        int nextConsumptionId = 0;
        
        for (SDbAbsenceConsumption consumption : hrsEmployee.getHrsReceipt().getAbsenceConsumptions()) {
            if (SLibUtils.compareKeys(getPrimaryKey(), consumption.getAbsenceKey())) {
                nextConsumptionId = consumption.getPkConsumptionId();
            }
        }
        
        return nextConsumptionId + 1;
    }
    
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
        msDisRisk = "";
        msDisSequel = "";
        msDisControl = "";
        mnBenefitsYear = 0;
        mnBenefitsAnniversary = 0;
        msNotes = "";
        mnExternarRequestId = 0;
        mbTimeClockSourced = false;
        mbClosed = false;
        mbDeleted = false;
        mnFkAbsenceClassId = 0;
        mnFkAbsenceTypeId = 0;
        mnFkSourcePayrollId_n = 0;
        mnFkUserClosedId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserClosed = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        msXtaAbsenceClass = "";
        msXtaAbsenceType = "";
        mbXtaAbsenceTypePayable = false;
        
        mnAuxPendingDays = 0;
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
            msDisRisk = resultSet.getString("dis_risk");
            msDisSequel = resultSet.getString("dis_sequel");
            msDisControl = resultSet.getString("dis_control");
            mnBenefitsYear = resultSet.getInt("ben_year");
            mnBenefitsAnniversary = resultSet.getInt("ben_ann");
            msNotes = resultSet.getString("nts");
            mnExternarRequestId = resultSet.getInt("ext_req_id");
            mbTimeClockSourced = resultSet.getBoolean("b_time_clock");
            mbClosed = resultSet.getBoolean("b_clo");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkAbsenceClassId = resultSet.getInt("fk_cl_abs");
            mnFkAbsenceTypeId = resultSet.getInt("fk_tp_abs");
            mnFkSourcePayrollId_n = resultSet.getInt("fk_src_pay_n");
            mnFkUserClosedId = resultSet.getInt("fk_usr_clo");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserClosed = resultSet.getTimestamp("ts_usr_clo");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // Read Absence type:
            
            absenceType = (SDbAbsenceType) session.readRegistry(SModConsts.HRSU_TP_ABS, new int[] { mnFkAbsenceClassId, mnFkAbsenceTypeId });
            msXtaAbsenceClass = (String) session.readField(SModConsts.HRSU_CL_ABS, new int[] { mnFkAbsenceClassId }, SDbAbsenceClass.FIELD_NAME);
            msXtaAbsenceType = (String) session.readField(SModConsts.HRSU_TP_ABS, new int[] { mnFkAbsenceClassId, mnFkAbsenceTypeId }, SDbAbsenceType.FIELD_NAME);
            
            if (absenceType != null) {
                mbXtaAbsenceTypePayable = absenceType.isPayable();
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
                    "'" + msDisRisk + "', " +
                    "'" + msDisSequel + "', " +
                    "'" + msDisControl + "', " +
                    mnBenefitsYear + ", " + 
                    mnBenefitsAnniversary + ", " + 
                    "'" + msNotes + "', " +
                    mnExternarRequestId + ", " + 
                    (mbTimeClockSourced ? 1 : 0) + ", " +
                    (mbClosed ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkAbsenceClassId + ", " +
                    mnFkAbsenceTypeId + ", " +
                    mnFkSourcePayrollId_n + ", " +
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
                    "dis_risk = '" + msDisRisk + "', " +
                    "dis_sequel = '" + msDisSequel + "', " +
                    "dis_control = '" + msDisControl + "', " +
                    "ben_year = " + mnBenefitsYear + ", " +
                    "ben_ann = " + mnBenefitsAnniversary + ", " +
                    "nts = '" + msNotes + "', " +
                    "ext_req_id = " + mnExternarRequestId + ", " +
                    "b_time_clock = " + (mbTimeClockSourced ? 1 : 0) + ", " +
                    "b_clo = " + (mbClosed ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_cl_abs = " + mnFkAbsenceClassId + ", " +
                    "fk_tp_abs = " + mnFkAbsenceTypeId + ", " +
                    "fk_src_pay_n = " + mnFkSourcePayrollId_n + ", " +
                    "fk_usr_clo = " + mnFkUserClosedId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_clo = " + "NOW()" + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        SDbAbsenceType absenceType = (SDbAbsenceType) session.readRegistry(SModConsts.HRSU_TP_ABS, new int[] { mnFkAbsenceClassId, mnFkAbsenceTypeId });
        msXtaAbsenceClass = (String) session.readField(SModConsts.HRSU_CL_ABS, new int[] { mnFkAbsenceClassId }, SDbAbsenceClass.FIELD_NAME);
        msXtaAbsenceType = (String) session.readField(SModConsts.HRSU_TP_ABS, new int[] { mnFkAbsenceClassId, mnFkAbsenceTypeId }, SDbAbsenceType.FIELD_NAME);

        if (absenceType != null) {
            mbXtaAbsenceTypePayable = absenceType.isPayable();
        }
            
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
        registry.setDisRisk(this.getDisRisk());
        registry.setDisSequel(this.getDisSequel());
        registry.setDisControl(this.getDisControl());
        registry.setBenefitsYear(this.getBenefitsYear());
        registry.setBenefitsAnniversary(this.getBenefitsAnniversary());
        registry.setNotes(this.getNotes());
        registry.setExternarRequestId(this.getExternarRequestId());
        registry.setTimeClockSourced(this.isTimeClockSourced());
        registry.setClosed(this.isClosed());
        registry.setDeleted(this.isDeleted());
        registry.setFkAbsenceClassId(this.getFkAbsenceClassId());
        registry.setFkAbsenceTypeId(this.getFkAbsenceTypeId());
        registry.setFkSourcePayrollId_n(this.getFkSourcePayrollId_n());
        registry.setFkUserClosedId(this.getFkUserClosedId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserClosed(this.getTsUserClosed());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setXtaAbsenceClass(this.getXtaAbsenceClass());
        registry.setXtaAbsenceType(this.getXtaAbsenceType());
        registry.setXtaAbsenceTypePayable(this.isXtaAbsenceTypePayable());

        registry.setAuxPendingDays(this.getAuxPendingDays());
    
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    @Override
    public boolean canDelete(SGuiSession session) throws SQLException, Exception {
        boolean can = super.canDelete(session);

        if (can) {
            if (!testDelete(session)) {
                can = false;
                msQueryResult = "¡No es posible eliminar la incidencia, tiene consumos en nóminas!";
            }
        }
        return can;
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
                value = msXtaAbsenceClass;
                break;
            case 1:
                value = msXtaAbsenceType;
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
                value = msDisRisk;
                break;
            case 7:
                value = msDisSequel;
                break;
            case 8:
                value = msDisControl;
                break;
            case 9:
                value = mnAuxPendingDays;
                break;
            default:
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
            case 7:
            case 8:
            case 9:
                break;
            default:
        }
    }
}

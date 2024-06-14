/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SHrsEmployeeHireLog {
    
    public static final int MODE_MODIFY = 1;
    public static final int MODE_REVERT = 2;
    
    protected Connection moConnection;
    protected SGuiSession moSession;
    
    protected int mnPkEmployeeId;
    protected Date mtLastHireDate;
    protected String msLastHireNotes;
    protected Date mtLastDismissalDate_n;
    protected String msLastDismissalNotes;
    protected boolean mbIsHire;
    protected boolean mbDeleted;
    protected int mnFkEmployeeDismissalTypeId;
    protected int mnFkRecruitmentSchemaTypeId;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    
    protected boolean mbRequestFirstHiring;
    protected int mnRequestMode;
    protected Connection moRequestFormerEmployerConnection;
    
    private SHrsEmployeeHireLog(final Connection connection, final SGuiSession session) {
        moConnection = connection;
        moSession = session;
        
        mnPkEmployeeId = 0;
        mtLastHireDate = null;
        msLastHireNotes = "";
        mtLastDismissalDate_n = null;
        msLastDismissalNotes = "";
        mbIsHire = false;
        mbDeleted = false;
        mnFkEmployeeDismissalTypeId = 0;
        mnFkRecruitmentSchemaTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        
        mbRequestFirstHiring = false;
        mnRequestMode = 0;
        moRequestFormerEmployerConnection = null;
    }

    /**
     * Create an instance for processing hiring-dismissal movements.
     * When connection provided, supposedly first-hiring movements will be processed.
     * On the other hand, when GUI session provided, supposedly modification and correction movements will be processed.
     * @param connection Database conection (for former framework of SIIE).
     */
    public SHrsEmployeeHireLog(final Connection connection) {
        this(connection, null);
    }

    /**
     * Create an instance for processing hiring-dismissal movements.
     * When GUI session provided, supposedly modification and correction movements will be processed.
     * On the other hand, when connection provided, supposedly first-hiring movements will be processed.
     * @param session GUI session (for new framework of Software Aplicado, SA-Lib 1.0).
     */
    public SHrsEmployeeHireLog(final SGuiSession session) {
        this(session.getDatabase().getConnection(), session);
    }

    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setLastHireDate(Date t) { mtLastHireDate = t; }
    public void setLastHireNotes(String s) { msLastHireNotes = s; }
    public void setLastDismissalDate_n(Date t) { mtLastDismissalDate_n = t; }
    public void setLastDismissalNotes(String s) { msLastDismissalNotes = s; }
    public void setIsHire(boolean b) { mbIsHire = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkEmployeeDismissalTypeId(int n) { mnFkEmployeeDismissalTypeId = n; }
    public void setFkRecruitmentSchemaTypeId(int n) { mnFkRecruitmentSchemaTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public Date getLastHireDate() { return mtLastHireDate; }
    public String getLastHireNotes() { return msLastHireNotes; }
    public Date getLastDismissalDate_n() { return mtLastDismissalDate_n; }
    public String getLastDismissalNotes() { return msLastDismissalNotes; }
    public boolean isHire() { return mbIsHire; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkEmployeeDismissalTypeId() { return mnFkEmployeeDismissalTypeId; }
    public int getFkRecruitmentSchemaTypeId() { return mnFkRecruitmentSchemaTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    
    private void setRequestSettings(final boolean firstHiring, final int mode, final Connection formerEmployerConnection) throws Exception {
        mbRequestFirstHiring = firstHiring;
        mnRequestMode = mode;
        moRequestFormerEmployerConnection = formerEmployerConnection;
    }
    
    public void setRequestSettings(final boolean firstHiring) throws Exception {
        setRequestSettings(firstHiring, 0, null);
    }
    
    public void setRequestSettings(final boolean firstHiring, final Connection formerEmployerConnection) throws Exception {
        setRequestSettings(firstHiring, 0, formerEmployerConnection);
    }
    
    public void setRequestSettings(final int mode) throws Exception {
        setRequestSettings(false, mode, null);
    }
    
    public void processRequest() throws Exception {
        String currentSchema = "";
        ArrayList<String> schemas = null;

        // define list of schemas:

        if (moConnection != null) {
            try (Statement statement = moConnection.createStatement()) {
                currentSchema = SHrsEmployeeUtils.getCurrentCompanySchema(statement);
                schemas = SHrsEmployeeUtils.getSiblingCompanySchemas(statement);
            }
        }
        else {
            currentSchema = SHrsEmployeeUtils.getCurrentCompanySchema(moSession.getStatement());
            schemas = SHrsEmployeeUtils.getSiblingCompanySchemas(moSession.getStatement());
        }

        if (!currentSchema.isEmpty()) {
            schemas.add(0, currentSchema);
        }
        
        // when moRequestFormerEmployerConnection has been set, use it for database updates to keep them into the global transaction:
        Connection connection = moRequestFormerEmployerConnection != null ? moRequestFormerEmployerConnection : moConnection;
        
        for (String schema : schemas) {
            if (mbRequestFirstHiring) {
                // bizarre, but member moConnection should have been previously set:

                try (Statement statement = connection.createStatement()) {
                    int logId = 0;
                    String sql = "SELECT COALESCE(MAX(id_log), 0) + 1 "
                            + "FROM " + schema + "." + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_HIRE) + " "
                            + "WHERE id_emp = " + mnPkEmployeeId + ";";
                    
                    ResultSet resultSet = statement.executeQuery(sql);
                    if (resultSet.next()) {
                        logId = resultSet.getInt(1);
                    }

                    sql = "INSERT INTO " + schema + "." + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_HIRE) + " VALUES (" +
                            mnPkEmployeeId + ", " +
                            logId + ", " +
                            "'" + SLibUtils.DbmsDateFormatDate.format(mtLastHireDate) + "', " +
                            "'" + msLastHireNotes + "', " +
                            (mtLastDismissalDate_n == null ? "NULL ," : "'" + SLibUtils.DbmsDateFormatDate.format(mtLastDismissalDate_n) + "', ") +
                            "'" + msLastDismissalNotes + "', " +
                            (mbIsHire ? 1 : 0) + ", " +
                            (mbDeleted ? 1 : 0) + ", " +
                            mnFkEmployeeDismissalTypeId + ", " +
                            mnFkRecruitmentSchemaTypeId + ", " + 
                            mnFkUserInsertId + ", " +
                            mnFkUserUpdateId + ", " +
                            "NOW()" + ", " +
                            "NOW()" + " " +
                            ");";

                    statement.execute(sql);

                    // insert employee membership into current company:
                    SHrsEmployeeUtils.insertMembership(statement, schema, mnPkEmployeeId, moSession != null ? moSession.getUser().getPkUserId() : mnFkUserInsertId);
                }
            }
            else {
                // bizarre, but member moSession should have been previously set:

                SDbEmployee employee = null;
                SDbEmployeeHireLog oldEmployeeHireLog = null;
                SDbEmployeeHireLog newEmployeeHireLog = null;

                if (mnRequestMode == MODE_MODIFY) {
                    oldEmployeeHireLog = null;

                    if (mbIsHire) {
                        oldEmployeeHireLog = SHrsUtils.getEmployeeLastHire(moSession, mnPkEmployeeId, 0, schema);
                    }
                    else {
                        oldEmployeeHireLog = SHrsUtils.getEmployeeLastDismiss(moSession, mnPkEmployeeId, 0, schema);
                    }

                    if (oldEmployeeHireLog == null) {
                        newEmployeeHireLog = new SDbEmployeeHireLog();
                    }
                    else {
                        newEmployeeHireLog = oldEmployeeHireLog;
                    }

                    if (mbIsHire) {
                        newEmployeeHireLog.setDateHire(mtLastHireDate);
                        newEmployeeHireLog.setNotesHire(msLastHireNotes);
                        newEmployeeHireLog.setFkEmployeeDismissalTypeId(SModSysConsts.HRSU_TP_EMP_DIS_NA);
                        newEmployeeHireLog.setFkRecruitmentSchemaTypeId(mnFkRecruitmentSchemaTypeId);
                    }
                    else {
                        newEmployeeHireLog.setDateDismissal_n(mtLastDismissalDate_n);
                        newEmployeeHireLog.setNotesDismissal(msLastDismissalNotes);
                        newEmployeeHireLog.setFkEmployeeDismissalTypeId(mnFkEmployeeDismissalTypeId);
                        newEmployeeHireLog.setFkRecruitmentSchemaTypeId(oldEmployeeHireLog.getFkRecruitmentSchemaTypeId()); // preserve
                    }

                    newEmployeeHireLog.setHired(mbIsHire);
                    newEmployeeHireLog.setDeleted(mbDeleted);
                    newEmployeeHireLog.setAuxSchema(schema);

                    newEmployeeHireLog.save(moSession);

                    employee = new SDbEmployee();

                    if (mbIsHire) {
                        employee.saveField(moSession.getStatement(), new int[] { mnPkEmployeeId }, SDbEmployee.FIELD_DATE_LAST_HIRE, mtLastHireDate);
                    }
                    else {
                        employee.saveField(moSession.getStatement(), new int[] { mnPkEmployeeId }, SDbEmployee.FIELD_DATE_LAST_DISMISS, mtLastDismissalDate_n);
                    }
                }
                else {
                    oldEmployeeHireLog = null;

                    if (!mbIsHire) {
                        // dismiss...
                        oldEmployeeHireLog = SHrsUtils.getEmployeeLastHire(moSession, mnPkEmployeeId, 0, schema);
                    }
                    else if (mnRequestMode == MODE_REVERT) {
                        oldEmployeeHireLog = SHrsUtils.getEmployeeLastDismiss(moSession, mnPkEmployeeId, 0, schema);
                    }

                    if (oldEmployeeHireLog == null) {
                        newEmployeeHireLog = new SDbEmployeeHireLog();
                    }
                    else {
                        newEmployeeHireLog = oldEmployeeHireLog;
                    }

                    newEmployeeHireLog.setPkEmployeeId(mnPkEmployeeId);
                    
                    if (!mbIsHire) {
                        // dismiss...
                        
                        newEmployeeHireLog.setDateDismissal_n(mtLastDismissalDate_n);
                        newEmployeeHireLog.setNotesDismissal(msLastDismissalNotes);
                        
                        newEmployeeHireLog.setFkEmployeeDismissalTypeId(mnFkEmployeeDismissalTypeId);
                        newEmployeeHireLog.setFkRecruitmentSchemaTypeId(oldEmployeeHireLog.getFkRecruitmentSchemaTypeId()); // preserve
                    }
                    else {
                        if (mnRequestMode == MODE_REVERT) {
                            newEmployeeHireLog.setDateDismissal_n(mtLastDismissalDate_n);
                            newEmployeeHireLog.setNotesDismissal(msLastDismissalNotes);
                        }
                        else {
                            newEmployeeHireLog.setDateHire(mtLastHireDate);
                            newEmployeeHireLog.setNotesHire(msLastHireNotes);
                        }
                        
                        newEmployeeHireLog.setFkEmployeeDismissalTypeId(SModSysConsts.HRSU_TP_EMP_DIS_NA);
                        newEmployeeHireLog.setFkRecruitmentSchemaTypeId(mnFkRecruitmentSchemaTypeId);
                    }

                    newEmployeeHireLog.setHired(mbIsHire);
                    newEmployeeHireLog.setDeleted(mbDeleted);
                    newEmployeeHireLog.setAuxSchema(schema);

                    newEmployeeHireLog.save(moSession);

                    if (mnRequestMode == MODE_REVERT) {
                        SDbEmployeeHireLog employeeHireLogAux = null;

                        employee = new SDbEmployee();

                        employee.saveField(moSession.getStatement(), new int[] { mnPkEmployeeId }, SDbEmployee.FIELD_ACTIVE, mbIsHire);

                        if (!mbIsHire) {
                            employeeHireLogAux = SHrsUtils.getEmployeeLastDismiss(moSession, mnPkEmployeeId, newEmployeeHireLog.getPkLogId(), schema);
                        }

                        if (mbIsHire) {
                            employee.saveField(moSession.getStatement(), new int[] { mnPkEmployeeId }, SDbEmployee.FIELD_DATE_LAST_DISMISS, null);
                        }
                        else {
                            employee.saveField(moSession.getStatement(), new int[] { mnPkEmployeeId }, SDbEmployee.FIELD_DATE_LAST_DISMISS, employeeHireLogAux.getDateDismissal_n());
                            employee.saveField(moSession.getStatement(), new int[] { mnPkEmployeeId }, SDbEmployee.FIELD_DATE_LAST_HIRE, employeeHireLogAux.getDateHire());
                        }
                    }
                }
            }
        }

        if (mbRequestFirstHiring) {
            // bizarre, but member moConnection should be instantiated:

            SHrsAccounting accounting = new SHrsAccounting(moConnection); // spreads accounting configurations to all sibling companies

            accounting.setAccountingType(SModSysConsts.HRSS_TP_ACC_EMP);
            accounting.setPkReferenceId(mnPkEmployeeId);
            
            if (mnFkUserInsertId == SUtilConsts.USR_NA_ID && mnFkUserUpdateId != SUtilConsts.USR_NA_ID) {
                accounting.setFkUserInsertId(mnFkUserUpdateId);
                accounting.setFkUserUpdateId(SUtilConsts.USR_NA_ID);
            }
            else {
                accounting.setFkUserInsertId(mnFkUserInsertId);
                accounting.setFkUserUpdateId(SUtilConsts.USR_NA_ID);
            }
            
            accounting.save();
        }
        
        if (moRequestFormerEmployerConnection != null) {
            // process an employer sustitution deleting employee memberships from former set of sibling companies:
            
            try (Statement statement = moRequestFormerEmployerConnection.createStatement()) {
                // define list of schemas:
                
                String formerCurrentSchema = SHrsEmployeeUtils.getCurrentCompanySchema(statement);
                ArrayList<String> formerSchemas = SHrsEmployeeUtils.getSiblingCompanySchemas(statement);
                
                if (!formerCurrentSchema.isEmpty()) {
                    formerSchemas.add(0, formerCurrentSchema);
                }
                
                // delete obsolete employee memberships:

                for (String formerSchema : formerSchemas) {
                    SHrsEmployeeUtils.deleteMembership(statement, formerSchema, mnPkEmployeeId);
                }
            }
        }
    }
}

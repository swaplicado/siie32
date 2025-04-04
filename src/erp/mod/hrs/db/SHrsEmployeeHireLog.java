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
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SHrsEmployeeHireLog {
    
    private static final int MODE_SWITCH = 0;
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
    
    /**
     * Create an instance for processing hiring-dismissal movements.
     * When connection provided, first-hiring movements will be processed.
     * @param connection Database conection (for former framework of SIIE).
     */
    public SHrsEmployeeHireLog(final Connection connection) {
        this(connection, null);
    }

    /**
     * Create an instance for processing hiring-dismissal movements.
     * When connection provided, first-hiring movements will be processed.
     * On the other hand, when GUI session provided, modification and correction movements will be processed.
     * @param connection Database conection (for former framework of SIIE).
     * @param session GUI session.
     */
    public SHrsEmployeeHireLog(final Connection connection, final SGuiSession session) {
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
        setRequestSettings(firstHiring, MODE_SWITCH, null);
    }
    
    public void setRequestSettings(final boolean firstHiring, final Connection formerEmployerConnection) throws Exception {
        setRequestSettings(firstHiring, MODE_SWITCH, formerEmployerConnection);
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
                // bizarre, but member moConnection should have been previously set

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
                // bizarre, but member moSession should have been previously set

                SDbEmployeeHireLog employeeHireLog;
                SDbEmployeeHireLog oldEmployeeHireLog;
                SDbEmployeeHireLog lastEmployeeHireLog;
                
                switch (mnRequestMode) {
                    case MODE_SWITCH:
                        // active status of employee already changed in SDbEmployee.save()!

                        if (mbIsHire) {
                            // new hire...
                            
                            // create new log entry:
                            
                            employeeHireLog = new SDbEmployeeHireLog();
                            employeeHireLog.setPkEmployeeId(mnPkEmployeeId);
                            
                            employeeHireLog.setHired(true);
                            employeeHireLog.setDateHire(mtLastHireDate);
                            employeeHireLog.setNotesHire(msLastHireNotes);

                            employeeHireLog.setFkEmployeeDismissalTypeId(SModSysConsts.HRSU_TP_EMP_DIS_NA);
                            employeeHireLog.setFkRecruitmentSchemaTypeId(mnFkRecruitmentSchemaTypeId);
                        }
                        else {
                            // new dismissal...
                            
                            // update last log entry:
                            
                            employeeHireLog = SHrsUtils.getEmployeeLastHireLog(moSession, schema, mnPkEmployeeId, 0);

                            employeeHireLog.setHired(false);
                            employeeHireLog.setDateDismissal_n(mtLastDismissalDate_n);
                            employeeHireLog.setNotesDismissal(msLastDismissalNotes);
                            
                            employeeHireLog.setFkEmployeeDismissalTypeId(mnFkEmployeeDismissalTypeId);
                            //employeeHireLog.setFkRecruitmentSchemaTypeId(...); // preserve
                        }

                        employeeHireLog.setAuxSchema(schema);
                        employeeHireLog.save(moSession);
                        break;
                        
                    case MODE_MODIFY:
                        // registry of employee is not yet updated!

                        lastEmployeeHireLog = SHrsUtils.getEmployeeLastHireLog(moSession, schema, mnPkEmployeeId, 0);
                        
                        if (mbIsHire) {
                            // modify last hire:
                            
                            lastEmployeeHireLog.setDateHire(mtLastHireDate);
                            lastEmployeeHireLog.setNotesHire(msLastHireNotes);
                            
                            lastEmployeeHireLog.setFkEmployeeDismissalTypeId(SModSysConsts.HRSU_TP_EMP_DIS_NA);
                            lastEmployeeHireLog.setFkRecruitmentSchemaTypeId(mnFkRecruitmentSchemaTypeId);
                        }
                        else {
                            // modify last dismissal:
                            
                            lastEmployeeHireLog.setDateDismissal_n(mtLastDismissalDate_n);
                            lastEmployeeHireLog.setNotesDismissal(msLastDismissalNotes);
                            
                            lastEmployeeHireLog.setFkEmployeeDismissalTypeId(mnFkEmployeeDismissalTypeId);
                            //lastEmployeeHireLog.setFkRecruitmentSchemaTypeId(...); // preserve
                        }

                        lastEmployeeHireLog.setAuxSchema(schema);
                        lastEmployeeHireLog.save(moSession);
                        
                        // update employee:

                        if (mbIsHire) {
                            moSession.saveField(SModConsts.HRSU_EMP, new int[] { mnPkEmployeeId }, SDbEmployee.FIELD_DATE_LAST_HIRE, mtLastHireDate);
                        }
                        else {
                            moSession.saveField(SModConsts.HRSU_EMP, new int[] { mnPkEmployeeId }, SDbEmployee.FIELD_DATE_LAST_DISMISSAL, mtLastDismissalDate_n);
                        }
                        break;
                        
                    case MODE_REVERT:
                        // registry of employee is not yet updated!

                        oldEmployeeHireLog = SHrsUtils.getEmployeeLastHireLog(moSession, schema, mnPkEmployeeId, 0);
                        
                        if (mbIsHire) {
                            // revert last dismissal...
                            
                            oldEmployeeHireLog.setHired(true);
                            oldEmployeeHireLog.setDateDismissal_n(null);
                            oldEmployeeHireLog.setNotesDismissal("");
                            oldEmployeeHireLog.setFkEmployeeDismissalTypeId(SModSysConsts.HRSU_TP_EMP_DIS_NA);
                            //oldEmployeeHireLog.setFkRecruitmentSchemaTypeId(...); // preserve
                        }
                        else {
                            // revert last hire...
                            
                            oldEmployeeHireLog.setDeleted(true);
                        }

                        oldEmployeeHireLog.setAuxSchema(schema);
                        oldEmployeeHireLog.save(moSession);

                        // update employee:
                        
                        lastEmployeeHireLog = SHrsUtils.getEmployeeLastHireLog(moSession, schema, mnPkEmployeeId, oldEmployeeHireLog.getPkLogId());
                        
                        moSession.saveField(SModConsts.HRSU_EMP, new int[] { mnPkEmployeeId }, SDbEmployee.FIELD_ACTIVE, mbIsHire);
                        
                        if (mbIsHire) {
                            // revert info of last dismissal...

                            moSession.saveField(SModConsts.HRSU_EMP, new int[] { mnPkEmployeeId }, SDbEmployee.FIELD_DATE_LAST_DISMISSAL, lastEmployeeHireLog == null ? null : lastEmployeeHireLog.getDateDismissal_n());
                        }
                        else {
                            // revert info of last hire...

                            moSession.saveField(SModConsts.HRSU_EMP, new int[] { mnPkEmployeeId }, SDbEmployee.FIELD_DATE_LAST_HIRE, lastEmployeeHireLog.getDateHire());
                            moSession.saveField(SModConsts.HRSU_EMP, new int[] { mnPkEmployeeId }, SDbEmployee.FIELD_DATE_LAST_DISMISSAL, lastEmployeeHireLog.getDateDismissal_n());
                        }
                        break;
                        
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
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

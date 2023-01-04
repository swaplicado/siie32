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
    
    protected Connection moConnection;
    protected SGuiSession moSession;
    protected int mnPkEmployeeHireLogId;
    protected int mnPkEmployeeId;
    protected Date mtLastHireDate;
    protected String msLastHireNotes;
    protected Date mtLastDismissalDate_n;
    protected String msLastDismissalNotes;
    protected boolean mbIsHire;
    protected boolean mbDeleted;
    protected int mnFkDismissalType;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    
    protected boolean mbIsAuxFirstHiring;
    protected boolean mbIsAuxForceFirstHiring;
    protected boolean mbIsAuxModification;
    protected boolean mbIsAuxCorrection;
    protected Connection moAuxFormerEmployerConnection;
    
    private SHrsEmployeeHireLog(final Connection connection, final SGuiSession session) {
        moConnection = connection;
        moSession = session;
        
        mnPkEmployeeHireLogId = 0;
        mnPkEmployeeId = 0;
        mtLastHireDate = null;
        msLastHireNotes = "";
        mtLastDismissalDate_n = null;
        msLastDismissalNotes = "";
        mbIsHire = false;
        mbDeleted = false;
        mnFkDismissalType = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        
        mbIsAuxFirstHiring = false;
        mbIsAuxForceFirstHiring = false;
        mbIsAuxModification = false;
        mbIsAuxCorrection = false;
        moAuxFormerEmployerConnection = null;
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

    public void setPkEmployeeHireLogId(int n) { mnPkEmployeeHireLogId = n; }
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setLastHireDate(Date t) { mtLastHireDate = t; }
    public void setLastHireNotes(String s) { msLastHireNotes = s; }
    public void setLastDismissalDate_n(Date t) { mtLastDismissalDate_n = t; }
    public void setLastDismissalNotes(String s) { msLastDismissalNotes = s; }
    public void setIsHire(boolean b) { mbIsHire = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkDismissalType(int n) { mnFkDismissalType = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    
    /**
     * Set auxiliar flag for first hiring. Used when employee registered for the very first time in ERP.
     * Requires that a connection has been provided in constructor of this instance.
     * @param flagStatus Enabling-disabling flag.
     */
    public void setIsAuxFirstHiring(boolean flagStatus) { mbIsAuxFirstHiring = flagStatus; } 

    /**
     * Set auxiliar flag for force (emulate) a first hiring. Used when employee is exported to a new set of friend companies within ERP.
     * Requires that a connection has been provided in constructor of this instance.
     * @param flagStatus Enabling-disabling flag.
     */
    public void setIsAuxForceFirstHiring(boolean flagStatus) { mbIsAuxForceFirstHiring = flagStatus; } 

    /**
     * Set auxiliar flag for modification.
     * Requires that a GUI session has been provided in constructor of this instance.
     * @param flagStatus Enabling-disabling flag.
     */
    public void setIsAuxModification(boolean flagStatus) { mbIsAuxModification = flagStatus; } 

    /**
     * Set auxiliar flag for correction.
     * Requires that a GUI session has been provided in constructor of this instance.
     * @param flagStatus Enabling-disabling flag.
     */
    public void setIsAuxCorrection(boolean flagStatus) { mbIsAuxCorrection = flagStatus; } 

    /**
     * Set former employer connection to execute employee new hire-log entries and memberships and to delete employee obsolete memberships within a set of sibling companies when an employeer substitution is being processed.
     * Requires as well that a connection has been provided in constructor of this instance.
     * @param formerEmployerConnection Former employer connection.
     */
    public void setAuxFormerEmployerConnection(Connection formerEmployerConnection) { moAuxFormerEmployerConnection = formerEmployerConnection; }
    
    public int getPkEmployeeHireLogId() { return mnPkEmployeeHireLogId; }
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public Date getLastHireDate() { return mtLastHireDate; }
    public String getLastHireNotes() { return msLastHireNotes; }
    public Date getLastDismissalDate_n() { return mtLastDismissalDate_n; }
    public String getLastDismissalNotes() { return msLastDismissalNotes; }
    public boolean isHire() { return mbIsHire; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkDismissalType() { return mnFkDismissalType; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    
    public void save() throws Exception {
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
        
        // when moAuxFormerEmployerConnection has been set, use it for database updates to keep them into the global transaction:
        Connection connection = moAuxFormerEmployerConnection != null ? moAuxFormerEmployerConnection : moConnection;
        
        for (String schema : schemas) {
            if (mbIsAuxFirstHiring || mbIsAuxForceFirstHiring) {
                // bizarre, but member moConnection should have been previously set:

                try (Statement statement = connection.createStatement()) {
                    int logId = 0;
                    String sql = "SELECT COALESCE(MAX(id_log), 0) + 1 "
                            + "FROM " + schema + "." + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_HIRE) + " "
                            + "WHERE id_emp = " + mnPkEmployeeId + " ";
                    
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
                            mnFkDismissalType + ", " +
                            mnFkUserInsertId + ", " +
                            mnFkUserUpdateId + ", " +
                            "NOW()" + ", " +
                            "NOW()" + " " +
                            ")";

                    statement.execute(sql);

                    // insert employee membership into current company:
                    SHrsEmployeeUtils.insertMembership(statement, schema, mnPkEmployeeId, moSession.getUser().getPkUserId());
                }
            }
            else {
                // bizarre, but member moSession should have been previously set:

                SDbEmployee employee = null;
                SDbEmployeeHireLog oldEmployeeHireLog = null;
                SDbEmployeeHireLog newEmployeeHireLog = null;

                if (mbIsAuxModification) {
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
                    }
                    else {
                        newEmployeeHireLog.setDateDismissal_n(mtLastDismissalDate_n);
                        newEmployeeHireLog.setNotesDismissal(msLastDismissalNotes);
                    }

                    newEmployeeHireLog.setHired(mbIsHire);
                    newEmployeeHireLog.setDeleted(mbDeleted);
                    newEmployeeHireLog.setFkEmployeeDismissalTypeId(mnFkDismissalType);
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
                        oldEmployeeHireLog = SHrsUtils.getEmployeeLastHire(moSession, mnPkEmployeeId, 0, schema);
                    }
                    else if (mbIsAuxCorrection) {
                        oldEmployeeHireLog = SHrsUtils.getEmployeeLastDismiss(moSession, mnPkEmployeeId, 0, schema);
                    }

                    if (oldEmployeeHireLog == null) {
                        newEmployeeHireLog = new SDbEmployeeHireLog();
                    }
                    else {
                        newEmployeeHireLog = oldEmployeeHireLog;
                    }

                    newEmployeeHireLog.setPkEmployeeId(mnPkEmployeeId);
                    
                    if (mbIsHire) {
                        if (mbIsAuxCorrection) {
                            newEmployeeHireLog.setDateDismissal_n(mtLastDismissalDate_n);
                            newEmployeeHireLog.setNotesDismissal(msLastDismissalNotes);
                        }
                        else {
                            newEmployeeHireLog.setDateHire(mtLastHireDate);
                            newEmployeeHireLog.setNotesHire(msLastHireNotes);
                        }
                    }
                    else {
                        newEmployeeHireLog.setDateDismissal_n(mtLastDismissalDate_n);
                        newEmployeeHireLog.setNotesDismissal(msLastDismissalNotes);
                    }

                    newEmployeeHireLog.setHired(mbIsHire);
                    newEmployeeHireLog.setDeleted(mbDeleted);
                    newEmployeeHireLog.setFkEmployeeDismissalTypeId(mnFkDismissalType);
                    newEmployeeHireLog.setAuxSchema(schema);

                    newEmployeeHireLog.save(moSession);

                    if (mbIsAuxCorrection) {
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

        if (mbIsAuxFirstHiring || mbIsAuxForceFirstHiring) {
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
        
        if (moAuxFormerEmployerConnection != null) {
            // process an employer sustitution deleting employee memberships from former set of sibling companies:
            
            try (Statement statement = moAuxFormerEmployerConnection.createStatement()) {
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

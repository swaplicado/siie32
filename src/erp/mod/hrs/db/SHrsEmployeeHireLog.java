/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
 */
public class SHrsEmployeeHireLog {

    protected ArrayList<String> msCompanies;
    protected Connection moConnection;
    protected SGuiSession moSession;
    protected int mnPkEmployeeHireLogId;
    protected int mnPkEmployeeId;
    protected Date mtDateLastHire;
    protected String msNotesHire;
    protected Date mtDateLastDismissed;
    protected String msNotesDismissed;
    protected boolean mbIsHire;
    protected boolean mbDeleted;
    protected int mnFkDismissedType;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    
    protected boolean mbIsFirtsHire;
    protected SDbEmployeeHireLog moXtaEmployeeHireLog;
    
    private void computeCompanies() {
        ResultSet resultSet = null;
        String sql = "";
        
        try {
            sql = "SELECT bd FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_CO) + " " +
                    "WHERE b_mod_hrs = 1; ";

            if (moConnection != null) {
                resultSet = moConnection.createStatement().executeQuery(sql);
            }
            else {
                resultSet = moSession.getStatement().executeQuery(sql);
            }
            
            while (resultSet.next()) {
                msCompanies.add(resultSet.getString(1));
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.printOutException(this, e);
        }
    }
    
    public SHrsEmployeeHireLog(final Connection connection, final SGuiSession session) {
        moConnection = connection;
        moSession = session;
        msCompanies = new ArrayList<String>();
        
        mtDateLastHire = null;
        msNotesHire = "";
        mtDateLastDismissed = null;
        msNotesDismissed = "";
        
        mbIsFirtsHire = false;
        moXtaEmployeeHireLog = null;
        
        computeCompanies();
    }

    public void setPkEmployeeHireLogId(int n) { mnPkEmployeeHireLogId = n; }
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setDateLastHire(Date t) { mtDateLastHire = t; }
    public void setNotesHire(String s) { msNotesHire = s; }
    public void setDateLastDismiss_n(Date t) { mtDateLastDismissed = t; }
    public void setNotesDismissed(String s) { msNotesDismissed = s; }
    public void setIsHire(boolean b) { mbIsHire = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkDismissedType(int n) { mnFkDismissedType = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    
    public void setIsFirtsHire(boolean b) { mbIsFirtsHire = b; } 
    public void setXtaEmployeeHireLog(SDbEmployeeHireLog o) { moXtaEmployeeHireLog = o; }
    
    public int getPkEmployeeHireLogId() { return mnPkEmployeeHireLogId; }
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public Date getDateLastHire() { return mtDateLastHire; }
    public String getNotesHire() { return msNotesHire; }
    public Date getDateLastDismiss_n() { return mtDateLastDismissed; }
    public String getNotesDismissed() { return msNotesDismissed; }
    public boolean isHire() { return mbIsHire; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkDismissedType() { return mnFkDismissedType; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    
    public boolean isFirtsHire() { return mbIsFirtsHire; } 
    public SDbEmployeeHireLog getXtaEmployeeHireLog() { return moXtaEmployeeHireLog; }
    
    public void save() throws Exception {
        SDbEmployeeHireLog employeeHireLog = null;
        String sql = "";
        
        for (String tableName : msCompanies) {
            if (mbIsFirtsHire) {
                sql = "INSERT INTO " + tableName + "." + "hrs_emp_log_hire VALUES (" +
                        mnPkEmployeeId + ", " +
                        "1, " +
                        "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLastHire) + "', " +
                        "'" + msNotesHire + "', " +
                        (mtDateLastDismissed == null ? "NULL ," : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLastDismissed) + "', ") +
                        "'" + msNotesDismissed + "', " +
                        (mbIsHire ? 1 : 0) + ", " +
                        (mbDeleted ? 1 : 0) + ", " +
                        mnFkDismissedType + ", " +
                        mnFkUserInsertId + ", " +
                        mnFkUserUpdateId + ", " +
                        "NOW()" + ", " +
                        "NOW()" + " " +
                        ")";

                    moConnection.createStatement().execute(sql);
            }
            else {
                moXtaEmployeeHireLog = null;
                
                if (!mbIsHire) {
                    moXtaEmployeeHireLog = SHrsUtils.getEmployeeLastHired(moSession, mnPkEmployeeId, tableName);
                }
                
                if (moXtaEmployeeHireLog == null) {
                    employeeHireLog = new SDbEmployeeHireLog();
                }
                else {
                    employeeHireLog = moXtaEmployeeHireLog;
                }

                employeeHireLog.setPkEmployeeId(mnPkEmployeeId);
                if (mbIsHire) {
                    employeeHireLog.setDateHire(mtDateLastHire);
                    employeeHireLog.setNotesHire(msNotesHire);
                }
                else {
                    employeeHireLog.setDateDismissed_n(mtDateLastDismissed);
                    employeeHireLog.setNotesDismissed(msNotesDismissed);
                }
                employeeHireLog.setHired(mbIsHire);
                employeeHireLog.setFkEmployeeDismissTypeId(mnFkDismissedType);
                employeeHireLog.setAuxTable(tableName);

                employeeHireLog.save(moSession);
            }
        }
    }
}

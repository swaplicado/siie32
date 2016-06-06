/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
 */
public class SHrsEmployeeSsContributionUpdate implements SGridRow {

    protected int mnEmployeeId;
    protected String msCodeEmployee;
    protected String msNameEmployee;
    protected String msCodeDepartament;
    protected String msNameDepartament;
    protected int mnDaysPeriod;
    protected int mnDaysIncapacity;
    protected int mnDaysAbsenteeism;
    protected int mnDaysSuspension;
    protected double mdAmountSys_r;
    protected double mdAmount_r;
    protected double mdPaymentDailyProportional;
    protected boolean mbApply;
    protected double mdSalaryDaily;
    protected double mdSalarySscBase;
    protected double mdSalarySscBaseNewSys;
    protected double mdSalarySscBaseNew;
    protected Date mtDateSalarySscBase;
    
    protected ArrayList<SHrsEarningsSsContributionUpdate> maEarningsSsContributionUpdates;

    public SHrsEmployeeSsContributionUpdate() throws Exception {
        mnEmployeeId = 0;
        msCodeEmployee = "";
        msNameEmployee = "";
        msCodeDepartament = "";
        msNameDepartament = "";
        mnDaysPeriod = 0;
        mnDaysIncapacity = 0;
        mnDaysAbsenteeism = 0;
        mnDaysSuspension = 0;
        mdAmountSys_r = 0;
        mdAmount_r = 0;
        mdPaymentDailyProportional = 0;
        mbApply = false;
        mdSalaryDaily = 0;
        mdSalarySscBase = 0;
        mdSalarySscBaseNewSys = 0;
        mdSalarySscBaseNew = 0;
        mtDateSalarySscBase = null;
        
        maEarningsSsContributionUpdates = new ArrayList<SHrsEarningsSsContributionUpdate>();
    }

    public void setEmployeeId(int n) { mnEmployeeId = n; }
    public void setCodeEmployee(String s) { msCodeEmployee = s; }
    public void setNameEmployee(String s) { msNameEmployee = s; }
    public void setCodeDepartament(String s) { msCodeDepartament = s; }
    public void setNameDepartament(String s) { msNameDepartament = s; }
    public void setDaysPeriod(int n) { mnDaysPeriod = n; }
    public void setDaysIncapacity(int n) { mnDaysIncapacity = n; }
    public void setDaysAbsenteeism(int n) { mnDaysAbsenteeism = n; }
    public void setDaysSuspension(int n) { mnDaysSuspension = n; }
    public void setAmountSys_r(double d) { mdAmountSys_r = d; }
    public void setAmount_r(double d) { mdAmount_r = d; }
    public void setPaymentDailyProportional(double d) { mdPaymentDailyProportional = d; }
    public void setIsApply(boolean b) { mbApply = b; }
    public void setSalaryDaily(double d) { mdSalaryDaily = d; }
    public void setSalarySscBase(double d) { mdSalarySscBase = d; }
    public void setSalarySscBaseNewSys(double d) { mdSalarySscBaseNewSys = d; }
    public void setSalarySscBaseNew(double d) { mdSalarySscBaseNew = d; }
    public void setDateSalarySscBase(Date t) { mtDateSalarySscBase = t; }
    
    public int getEmployeeId() { return mnEmployeeId; }
    public String getCodeEmployee() { return msCodeEmployee; }
    public String getNameEmployee() { return msNameEmployee; }
    public String getCodeDepartament() { return msCodeDepartament; }
    public String getNameDepartament() { return msNameDepartament; }
    public int getDaysPeriod() { return mnDaysPeriod; }
    public int getDaysIncapacity() { return mnDaysIncapacity; }
    public int getDaysAbsenteeism() { return mnDaysAbsenteeism; }
    public int getDaysSuspension() { return mnDaysSuspension; }
    public double getAmountSys_r() { return mdAmountSys_r; }
    public double getAmount_r() { return mdAmount_r; }
    public double getPaymentDailyProportional() { return mdPaymentDailyProportional; }
    public boolean isApply() { return mbApply; }
    public double getSalaryDaily() { return mdSalaryDaily; }
    public double getSalarySscBase() { return mdSalarySscBase; }
    public double getSalarySscBaseNewSys() { return mdSalarySscBaseNewSys; }
    public double getSalarySscBaseNew() { return mdSalarySscBaseNew; }
    public Date getDateSalarySscBase() { return mtDateSalarySscBase; }
    
    public ArrayList<SHrsEarningsSsContributionUpdate> getEarningsSsContributionUpdates() { return maEarningsSsContributionUpdates; }
    
    public int getTotalDays() { return mnDaysPeriod - (mnDaysIncapacity + mnDaysAbsenteeism + mnDaysSuspension); }
    
    public double getTotalAmountEarnings() { 
        calculateAmountEarnings();
        return mdAmount_r; }
    
    public void calculateAmountEarnings() {
        mdAmountSys_r = 0;
        mdAmount_r = 0;
        
        for (SHrsEarningsSsContributionUpdate earningsSsContributionUpdate : maEarningsSsContributionUpdates) {
            mdAmountSys_r += earningsSsContributionUpdate.getAmountSys();
            mdAmount_r += earningsSsContributionUpdate.getAmount();
        }
    }
    
    public void computeSSContribution() {
        mdPaymentDailyProportional = getTotalDays() == 0 ?  0 : SLibUtils.round(getTotalAmountEarnings() / getTotalDays(), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
        mdSalarySscBaseNew = SLibUtils.round(mdSalarySscBase + mdPaymentDailyProportional, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
        mdSalarySscBaseNewSys = mdSalarySscBaseNew;
    }

    public void createSalarySscBaseLog(SGuiSession session) {
        String sql = "";
        ResultSet resultSet = null;
        int nLogId = 0;

        try {
            nLogId = 0;

            sql = "SELECT COALESCE(MAX(id_log), 0) + 1 FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_SAL_SSC) + " WHERE id_emp = " + mnEmployeeId + " ";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                nLogId = resultSet.getInt(1);
            }

            sql = "INSERT INTO " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_LOG_SAL_SSC) + " VALUES (" + mnEmployeeId + ", " +
                    nLogId + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateSalarySscBase) + "', " +
                    mdSalarySscBaseNew + ", " +
                    "0, " +
                    session.getUser().getPkUserId() + ", " +
                    session.getUser().getPkUserId() + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";

            session.getStatement().execute(sql);
        }
        catch (java.lang.Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    public void updateSscEmployee(SGuiSession session) {
        String sql = "";
        
        try {
            sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " SET sal_ssc = " + mdSalarySscBaseNew + ", " +
                    "dt_sal_ssc = '" + SLibUtils.DbmsDateFormatDate.format(mtDateSalarySscBase) + "' " +
                    "WHERE id_emp = " + mnEmployeeId + " ";
            
            session.getStatement().execute(sql);
        }
        catch (java.lang.Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    public void save(SGuiSession session) {
        createSalarySscBaseLog(session);
        updateSscEmployee(session);
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnEmployeeId };
    }

    @Override
    public String getRowCode() {
        return getCodeEmployee();
    }

    @Override
    public String getRowName() {
        return getNameEmployee();
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = msNameEmployee;
                break;
            case 1:
                value = msCodeEmployee;
                break;
            case 2:
                value = msNameDepartament;
                break;
            case 3:
                value = msCodeDepartament;
                break;
            case 4:
                value = mdSalaryDaily;
                break;
            case 5:
                value = mbApply;
                break;
            case 6:
                value = mdSalarySscBase;
                break;
            case 7:
                value = mdSalarySscBaseNew;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch (row) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                mbApply = (boolean) value;
                break;
            case 6:
                break;
            case 7:
                mdSalarySscBaseNew = (double) value;
                break;
            default:
                break;
        }
    }
}

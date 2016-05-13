/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import java.util.ArrayList;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;

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
    protected double mdPaymentDailyProportional;
    protected boolean mbApply;
    protected double mdSalarySscBase;
    protected double mdSalarySscBaseNewSys;
    protected double mdSalarySscBaseNew;
    
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
        mdPaymentDailyProportional = 0;
        mbApply = false;
        mdSalarySscBase = 0;
        mdSalarySscBaseNewSys = 0;
        mdSalarySscBaseNew = 0;
        
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
    public void setPaymentDailyProportional(double d) { mdPaymentDailyProportional = d; }
    public void setIsApply(boolean b) { mbApply = b; }
    public void setSalarySscBase(double d) { mdSalarySscBase = d; }
    public void setSalarySscBaseNewSys(double d) { mdSalarySscBaseNewSys = d; }
    public void setSalarySscBaseNew(double d) { mdSalarySscBaseNew = d; }
    
    public int getEmployeeId() { return mnEmployeeId; }
    public String getCodeEmployee() { return msCodeEmployee; }
    public String getNameEmployee() { return msNameEmployee; }
    public String getCodeDepartament() { return msCodeDepartament; }
    public String getNameDepartament() { return msNameDepartament; }
    public int getDaysPeriod() { return mnDaysPeriod; }
    public int getDaysIncapacity() { return mnDaysIncapacity; }
    public int getDaysAbsenteeism() { return mnDaysAbsenteeism; }
    public int getDaysSuspension() { return mnDaysSuspension; }
    public double getPaymentDailyProportional() { return mdPaymentDailyProportional; }
    public boolean isApply() { return mbApply; }
    public double getSalarySscBase() { return mdSalarySscBase; }
    public double getSalarySscBaseNewSys() { return mdSalarySscBaseNewSys; }
    public double getSalarySscBaseNew() { return mdSalarySscBaseNew; }
    
    public ArrayList<SHrsEarningsSsContributionUpdate> getEarningsSsContributionUpdates() { return maEarningsSsContributionUpdates; }
    
    public int getTotalDays() { return mnDaysPeriod - (mnDaysIncapacity + mnDaysAbsenteeism + mnDaysSuspension); }
    
    public double getTotalAmountEarnings() {
        double total = 0;
    
        for (SHrsEarningsSsContributionUpdate earningsSsContributionUpdate : maEarningsSsContributionUpdates) {
            total += earningsSsContributionUpdate.getAmount();
        }
        
        return total;
    }
    
    public void computeSSContribution() {
        mdPaymentDailyProportional = getTotalDays() == 0 ?  0 : SLibUtils.round(getTotalAmountEarnings() / getTotalDays(), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
        mdSalarySscBaseNew = mdSalarySscBase + mdPaymentDailyProportional;
        mdSalarySscBaseNewSys = mdSalarySscBaseNew;
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
                value = mbApply;
                break;
            case 5:
                value = mdSalarySscBase;
                break;
            case 6:
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
                mbApply = (boolean) value;
                break;
            case 5:
                break;
            case 6:
                mdSalarySscBaseNew = (double) value;
                break;
            default:
                break;
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

/**
 *
 * @author Juan Barajas
 */
public class SHrsDaysByPeriod {

    protected int mnYear;
    protected int mnPeriod;                 // calendar month, 1 = January.
    protected int mnPeriodDays;             // month's calendar days
    protected int mnPeriodPayrollDays;      // payroll days considered belonging to current period
    protected int mnDaysNotWorkedNotPaid;   // days not worked and not paid in current period (days of incapacity not paid inclusive)
    protected int mnDaysIncapacityNotPaid;  // days of incapacity not paid in current period

    /**
     * 
     * @param year
     * @param period
     * @param periodDays
     * @param periodPayrollDays
     */
    public SHrsDaysByPeriod(int year, int period, int periodDays, int periodPayrollDays) {
        mnYear = year;
        mnPeriod = period;
        mnPeriodDays = periodDays;
        mnPeriodPayrollDays = periodPayrollDays;
        mnDaysNotWorkedNotPaid = 0;
        mnDaysIncapacityNotPaid = 0;
    }
    
    public int getYear() { return mnYear; }
    public int getPeriod() { return mnPeriod; }
    public int getPeriodDays() { return mnPeriodDays; }
    public int getPeriodPayrollDays() { return mnPeriodPayrollDays; }
    public int getDaysNotWorkedNotPaid() { return mnDaysNotWorkedNotPaid; }
    public int getDaysIncapacityNotPaid() { return mnDaysIncapacityNotPaid; }
    
    public void setDaysNotPaid(final int availableDaysNotWorkedNotPaid, final int availableDaysIncapacityNotPaid) {
        if (mnPeriodPayrollDays > 0) {
            if (availableDaysNotWorkedNotPaid <= mnPeriodPayrollDays) {
                mnDaysNotWorkedNotPaid = availableDaysNotWorkedNotPaid;
            }
            else {
                mnDaysNotWorkedNotPaid = mnPeriodPayrollDays;
            }
            
            if (availableDaysIncapacityNotPaid <= mnPeriodPayrollDays) {
                mnDaysIncapacityNotPaid = availableDaysIncapacityNotPaid;
            }
            else {
                mnDaysIncapacityNotPaid = mnPeriodPayrollDays;
            }  
        }
    }
    
}

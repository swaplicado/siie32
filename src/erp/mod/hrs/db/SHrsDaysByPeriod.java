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
    protected int mnPeriod;
    protected int mnDaysPeriod;
    protected int mnDaysPeriodPayrollNotWorkedNotPaid; // day period not worked
    protected int mnDaysPeriodPayrollIncapacityNotPaid; // day period not paid
    protected int mnDaysPeriodPayroll; // day period hired

    public SHrsDaysByPeriod(int year, int period, int daysPeriod, int daysPeriodPayroll, int daysPeriodPayrollNotWorkedNotPaid, int daysPeriodPayrollIncapacityNotPaid) {
        mnYear = year;
        mnPeriod = period;
        mnDaysPeriod = daysPeriod;
        mnDaysPeriodPayroll = daysPeriodPayroll;
        mnDaysPeriodPayrollNotWorkedNotPaid = daysPeriodPayrollNotWorkedNotPaid;
        mnDaysPeriodPayrollIncapacityNotPaid = daysPeriodPayrollIncapacityNotPaid;
    }

    public void setYear(final int n) { mnYear = n; }
    public void setPeriod(int n) { mnPeriod = n; }
    public void setDaysPeriod(int n) { mnDaysPeriod = n; }
    public void setDaysPeriodPayroll(int n) { mnDaysPeriodPayroll = n; }
    public void setDaysPeriodPayrollNotWorkedNotPaid(int n) { mnDaysPeriodPayrollNotWorkedNotPaid = n; }
    public void setDaysPeriodPayrollIncapacityNotPaid(int n) { mnDaysPeriodPayrollIncapacityNotPaid = n; }
    
    public int getYear() { return mnYear; }
    public int getPeriod() { return mnPeriod; }
    public int getDaysPeriod() { return mnDaysPeriod; }
    public int getDaysPeriodPayroll() { return mnDaysPeriodPayroll; }
    public int getDaysPeriodPayrollNotWorkedNotPaid() { return mnDaysPeriodPayrollNotWorkedNotPaid; }
    public int getDaysPeriodPayrollIncapacityNotPaid() { return mnDaysPeriodPayrollIncapacityNotPaid; }
}

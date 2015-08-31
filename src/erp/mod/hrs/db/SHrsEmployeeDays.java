/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import java.util.Date;
import sa.lib.SLibTimeUtils;

/**
 *
 * @author Sergio Flores
 */
public class SHrsEmployeeDays {

    protected int mnYear;                       // for accumulated earnings & deductions
    protected int mnPeriod;                       // for accumulated earnings & deductions
    protected Date mtPeriodStart;               // analyzed period start date
    protected Date mtPeriodEnd;                 // analyzed period end date
    protected int mnPeriodDays;
    
    protected double mdFactorCalendar;
    protected double mdFactorDaysPaid;
    protected double mdReceiptDays;
    protected double mdWorkingDays;
    protected double mdBusinessDays;
    protected double mdDaysWorked;
    protected double mdDaysHiredPayroll;
    protected double mdDaysHiredAnnual;
    protected double mdDaysIncapacityNotPaidPayroll;
    protected double mdDaysIncapacityNotPaidAnnual;
    protected double mdDaysNotWorkedPaid;
    protected double mdDaysNotWorkedNotPaid;
    protected double mdDaysNotWorked_r;
    protected double mdDaysToBePaid_r;
    protected double mdDaysPaid;
    

    public SHrsEmployeeDays(int year, int period, Date periodStart, Date periodEnd) throws Exception {
        mnYear = year;
        mnPeriod = period;
        mtPeriodStart = periodStart;
        mtPeriodEnd = periodEnd;
        mnPeriodDays = (int) SLibTimeUtils.getDaysDiff(mtPeriodEnd, mtPeriodStart) + 1;
        
        mdFactorCalendar = 0;
        mdFactorDaysPaid = 0;
        mdReceiptDays = 0;
        mdWorkingDays = 0;
        mdBusinessDays = 0;
        mdDaysWorked = 0;
        mdDaysHiredPayroll = 0;
        mdDaysHiredAnnual = 0;
        mdDaysIncapacityNotPaidPayroll = 0;
        mdDaysIncapacityNotPaidAnnual = 0;
        mdDaysNotWorkedPaid = 0;
        mdDaysNotWorkedNotPaid = 0;
        mdDaysNotWorked_r = 0;
        mdDaysToBePaid_r = 0;
        mdDaysPaid = 0;
        mdDaysPaid = 0;
    }

    public void setYear(final int n) { mnYear = n; }
    public void setPeriod(final int n) { mnPeriod = n; }
    public void setPeriodStart(final Date t) { mtPeriodStart = t; }
    public void setPeriodEnd(final Date t) { mtPeriodEnd = t; }
    public void setPeriodDays(int n) { mnPeriodDays = n; }
    
    public void setFactorCalendar(double d) { mdFactorCalendar = d; }
    public void setFactorDaysPaid(double d) { mdFactorDaysPaid = d; }
    public void setReceiptDays(double d) { mdReceiptDays = d; }
    public void setWorkingDays(double d) { mdWorkingDays = d; }
    public void setBusinessDays(double d) { mdBusinessDays = d; }
    public void setDaysWorked(double d) { mdDaysWorked = d; }
    public void setDaysHiredPayroll(double d) { mdDaysHiredPayroll = d; }
    public void setDaysHiredAnnual(double d) { mdDaysHiredAnnual = d; }
    public void setDaysIncapacityNotPaidPayroll(double d) { mdDaysIncapacityNotPaidPayroll = d; }
    public void setDaysIncapacityNotPaidAnnual(double d) { mdDaysIncapacityNotPaidAnnual = d; }
    public void setDaysNotWorkedPaid(double d) { mdDaysNotWorkedPaid = d; }
    public void setDaysNotWorkedNotPaid(double d) { mdDaysNotWorkedNotPaid = d; }
    public void setDaysNotWorked_r(double d) { mdDaysNotWorked_r = d; }
    public void setDaysToBePaid_r(double d) { mdDaysToBePaid_r = d; }
    public void setDaysPaid(double d) { mdDaysPaid = d; }

    public int getYear() { return mnYear; }
    public int getPeriod() { return mnPeriod; }
    public Date getPeriodStart() { return mtPeriodStart; }
    public Date getPeriodEnd() { return mtPeriodEnd; }
    public int getPeriodDays() { return mnPeriodDays; }
    
    public double getFactorCalendar() { return mdFactorCalendar; }
    public double getFactorDaysPaid() { return mdFactorDaysPaid; }
    public double getReceiptDays() { return mdReceiptDays; }
    public double getWorkingDays() { return mdWorkingDays; }
    public double getBusinessDays() { return mdBusinessDays; }
    public double getDaysWorked() { return mdDaysWorked; }
    public double getDaysHiredPayroll() { return mdDaysHiredPayroll; }
    public double getDaysHiredAnnual() { return mdDaysHiredAnnual; }
    public double getDaysIncapacityNotPaidPayroll() { return mdDaysIncapacityNotPaidPayroll; }
    public double getDaysIncapacityNotPaidAnnual() { return mdDaysIncapacityNotPaidAnnual; }
    public double getDaysNotWorkedPaid() { return mdDaysNotWorkedPaid; }
    public double getDaysNotWorkedNotPaid() { return mdDaysNotWorkedNotPaid; }
    public double getDaysNotWorked_r() { return mdDaysNotWorked_r; }
    public double getDaysToBePaid_r() { return mdDaysToBePaid_r; }
    public double getDaysPaid() { return mdDaysPaid; }

    /**
     * Gets employee's taxable days in current period.
     * @return Days active - days of disability.
     */
    public double getPayrollTaxableDays_r() { return mdDaysHiredPayroll - mdDaysIncapacityNotPaidPayroll; }
    
    /**
     * Gets employee's taxable days accumulated.
     * @return Days active - days of disability.
     */
    public double getAnnualTaxableDays_r() { return mdDaysHiredAnnual - mdDaysIncapacityNotPaidAnnual; }
}

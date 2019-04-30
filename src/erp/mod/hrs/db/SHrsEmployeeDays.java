/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModSysConsts;
import java.util.Date;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SHrsEmployeeDays {

    protected int mnYear;       // for accumulated earnings & deductions
    protected int mnPeriod;     // for accumulated earnings & deductions
    protected Date mtPeriodStart;   // analyzed period start
    protected Date mtPeriodEnd;     // analyzed period end
    protected int mnPeriodDays;     // analyzed period days
    
    protected double mdFactorCalendar;
    protected double mdFactorDaysPaid;
    protected double mdReceiptDays;
    protected double mdWorkingDays;
    protected double mdDaysWorked;
    protected double mdDaysHiredPayroll;
    protected double mdDaysHiredAnnual;
    protected double mdDaysDisabilityNotPaidPayroll;
    protected double mdDaysDisabilityNotPaidAnnual;
    protected double mdDaysNotWorkedButPaid;
    protected double mdDaysNotWorkedNotPaid;
    
    protected double mdBusinessDays;
    
    public SHrsEmployeeDays(int year, int period, Date periodStart, Date periodEnd) {
        mnYear = year;
        mnPeriod = period;
        mtPeriodStart = periodStart;
        mtPeriodEnd = periodEnd;
        mnPeriodDays = SLibTimeUtils.countPeriodDays(mtPeriodStart, mtPeriodEnd);
        
        mdFactorCalendar = 0;
        mdFactorDaysPaid = 0;
        mdReceiptDays = 0;
        mdWorkingDays = 0;
        mdDaysWorked = 0;
        mdDaysHiredPayroll = 0;
        mdDaysHiredAnnual = 0;
        mdDaysDisabilityNotPaidPayroll = 0;
        mdDaysDisabilityNotPaidAnnual = 0;
        mdDaysNotWorkedButPaid = 0;
        mdDaysNotWorkedNotPaid = 0;
        
        mdBusinessDays = 0;
    }
    
    public void setFactorCalendar(double d) { mdFactorCalendar = d; }
    public void setFactorDaysPaid(double d) { mdFactorDaysPaid = d; }
    public void setReceiptDays(double d) { mdReceiptDays = d; }
    public void setWorkingDays(double d) { mdWorkingDays = d; }
    public void setDaysWorked(double d) { mdDaysWorked = d; }
    public void setDaysHiredPayroll(double d) { mdDaysHiredPayroll = d; }
    public void setDaysHiredAnnual(double d) { mdDaysHiredAnnual = d; }
    public void setDaysDisabilityNotPaidPayroll(double d) { mdDaysDisabilityNotPaidPayroll = d; }
    public void setDaysDisabilityNotPaidAnnual(double d) { mdDaysDisabilityNotPaidAnnual = d; }
    public void setDaysNotWorkedButPaid(double d) { mdDaysNotWorkedButPaid = d; }
    public void setDaysNotWorkedNotPaid(double d) { mdDaysNotWorkedNotPaid = d; }
    
    public void setBusinessDays(double d) { mdBusinessDays = d; }

    public int getYear() { return mnYear; }
    public int getPeriod() { return mnPeriod; }
    public Date getPeriodStart() { return mtPeriodStart; }
    public Date getPeriodEnd() { return mtPeriodEnd; }
    public int getPeriodDays() { return mnPeriodDays; }
    
    public double getFactorCalendar() { return mdFactorCalendar; }
    public double getFactorDaysPaid() { return mdFactorDaysPaid; }
    public double getReceiptDays() { return mdReceiptDays; }
    public double getWorkingDays() { return mdWorkingDays; }
    public double getDaysWorked() { return mdDaysWorked; }
    public double getDaysHiredPayroll() { return mdDaysHiredPayroll; }
    public double getDaysHiredAnnual() { return mdDaysHiredAnnual; }
    public double getDaysIncapacityNotPaidPayroll() { return mdDaysDisabilityNotPaidPayroll; }
    public double getDaysIncapacityNotPaidAnnual() { return mdDaysDisabilityNotPaidAnnual; }
    public double getDaysNotWorkedButPaid() { return mdDaysNotWorkedButPaid; }
    public double getDaysNotWorkedNotPaid() { return mdDaysNotWorkedNotPaid; }
    
    public double getBusinessDays() { return mdBusinessDays; }

    public double getDaysNotWorked_r() { return mdDaysNotWorkedButPaid + mdDaysNotWorkedNotPaid; }
    public double getDaysToBePaid_r() { return (mdDaysWorked + mdDaysNotWorkedButPaid) * mdFactorCalendar; }
    public double getDaysPaid() { return getDaysToBePaid_r() * mdFactorDaysPaid; }
    
    /**
     * Gets employee's taxable days in current payroll.
     * @return Days hired - days of disability.
     */
    public double getTaxableDaysPayroll() { return mdDaysHiredPayroll - mdDaysDisabilityNotPaidPayroll; }
    
    /**
     * Gets employee's taxable days in current year.
     * @return Days hired - days of disability.
     */
    public double getTaxableDaysAnnual() { return mdDaysHiredAnnual - mdDaysDisabilityNotPaidAnnual; }

    /**
     * Computes earning units.
     * @param unitsAlleged
     * @param earning
     * @return 
     */
    public double computeEarningUnits(final double unitsAlleged, final SDbEarning earning) {
        double units;
        
        switch (earning.getFkEarningComputationTypeId()) {
            case SModSysConsts.HRSS_TP_EAR_COMP_DAYS:
                units = unitsAlleged * mdFactorCalendar * (earning.isDaysAdjustment() ? mdFactorDaysPaid : 1d);
                break;
            default:
                units = unitsAlleged;
        }
        
        return SLibUtils.round(units, SLibUtils.DecimalFormatValue8D.getMaximumFractionDigits());
    }
    
    /**
     * Computes earning amount.
     * @param units
     * @param amountUnit
     * @param earning
     * @return 
     */
    public static double computeEarningAmount(final double units, final double amountUnit, final SDbEarning earning) {
        return SLibUtils.roundAmount(units * amountUnit * earning.getUnitsFactor());
    }
}

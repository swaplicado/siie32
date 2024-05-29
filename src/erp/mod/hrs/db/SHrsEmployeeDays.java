/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

/**
 * Las instancias de esta clase son una estructura de datos
 * que contiene los valores de días tanto en la nómina como en el año actuales
 * para el procesamiento de impuestos y demás cálculos y emisión de nóminas.
 *
 * @author Sergio Flores
 */
public class SHrsEmployeeDays {

    protected int mnPayrollYear;    // for accumulated earnings & deductions
    protected int mnPayrollPeriod;  // for accumulated earnings & deductions
    
    protected double mdReceiptDays;
    protected double mdReceiptWorkingDays;
    protected double mdReceiptDaysWorked;
    protected double mdReceiptBusinessDays;
    
    /** HRS days group for tax and attendance standard processing of payroll-specific period. */
    protected SHrsEmployeeDaysGroup moHrsDaysGroupStdPayroll;
    /** HRS days group for tax and attendance standard processing of annual period. */
    protected SHrsEmployeeDaysGroup moHrsDaysGroupStdAnnual;
    /** HRS days group for tax and attendance transition processing of annual old-style period. (E.g., calculation of tax subsidy until April 2024.) */
    protected SHrsEmployeeDaysGroup moHrsDaysGroupTransAnnualOldStyle;
    /** HRS days group for tax and attendance transition processing of annual new-style period. (E.g., calculation of tax subsidy since May 2024.) */
    protected SHrsEmployeeDaysGroup moHrsDaysGroupTransAnnualNewStyle;
    
    /**
     * Create new HRS employee days.
     * Instances only created in SHrsEmployee.createEmployeeDays().
     * @param payrollYear
     * @param payrollPeriod 
     */
    public SHrsEmployeeDays(int payrollYear, int payrollPeriod) {
        mnPayrollYear = payrollYear;
        mnPayrollPeriod = payrollPeriod;
        
        mdReceiptDays = 0;
        mdReceiptWorkingDays = 0;
        mdReceiptDaysWorked = 0;
        mdReceiptBusinessDays = 0;
        
        moHrsDaysGroupStdPayroll = new SHrsEmployeeDaysGroup();
        moHrsDaysGroupStdAnnual = new SHrsEmployeeDaysGroup();
        moHrsDaysGroupTransAnnualOldStyle = null;
        moHrsDaysGroupTransAnnualNewStyle = null;
    }
    
    public void setReceiptDays(double d) { mdReceiptDays = d; }
    public void setReceiptWorkingDays(double d) { mdReceiptWorkingDays = d; }
    public void setReceiptDaysWorked(double d) { mdReceiptDaysWorked = d; }
    public void setReceiptBusinessDays(double d) { mdReceiptBusinessDays = d; }
    
    /** HRS days group for tax and attendance transition processing of annual old-style period. (E.g., calculation of tax subsidy until April 2024.) */
    public void setHrsDaysGroupTransAnnualOldStyle(SHrsEmployeeDaysGroup o) { moHrsDaysGroupTransAnnualOldStyle = o; }
    /** HRS days group for tax and attendance transition processing of annual new-style period. (E.g., calculation of tax subsidy since May 2024.) */
    public void setHrsDaysGroupTransAnnualNewStyle(SHrsEmployeeDaysGroup o) { moHrsDaysGroupTransAnnualNewStyle = o; }
    
    public void setDaysHiredAnnual(double d) { moHrsDaysGroupStdAnnual.setDaysHired(d); }
    public void setDaysHiredPayroll(double d) { moHrsDaysGroupStdPayroll.setDaysHired(d); }
    
    public void setDaysIncapacityNotPaidAnnual(double d) { moHrsDaysGroupStdAnnual.setDaysIncapacityNotPaid(d); }
    public void setDaysIncapacityNotPaidPayroll(double d) { moHrsDaysGroupStdPayroll.setDaysIncapacityNotPaid(d); }
    public void setDaysNotWorkedButPaidPayroll(double d) { moHrsDaysGroupStdPayroll.setDaysNotWorkedButPaid(d); }
    public void setDaysNotWorkedNotPaidPayroll(double d) { moHrsDaysGroupStdPayroll.setDaysNotWorkedNotPaid(d); }
    
    public int getPayrollYear() { return mnPayrollYear; }
    public int getPayrollPeriod() { return mnPayrollPeriod; }
    
    public double getReceiptDays() { return mdReceiptDays; }
    public double getReceiptWorkingDays() { return mdReceiptWorkingDays; }
    public double getReceiptDaysWorked() { return mdReceiptDaysWorked; }
    public double getReceiptBusinessDays() { return mdReceiptBusinessDays; }
    
    /** HRS days group for tax and attendance transition processing of annual old-style period. (E.g., calculation of tax subsidy until April 2024.) */
    public SHrsEmployeeDaysGroup getHrsDaysGroupTransAnnualOldStyle() { return moHrsDaysGroupTransAnnualOldStyle; }
    /** HRS days group for tax and attendance transition processing of annual new-style period. (E.g., calculation of tax subsidy since May 2024.) */
    public SHrsEmployeeDaysGroup getHrsDaysGroupTransAnnualNewStyle() { return moHrsDaysGroupTransAnnualNewStyle; }
    
    public double getDaysHiredAnnual() { return moHrsDaysGroupStdAnnual.getDaysHired(); }
    public double getDaysHiredPayroll() { return moHrsDaysGroupStdPayroll.getDaysHired(); }
    
    public double getDaysIncapacityNotPaidAnnual() { return moHrsDaysGroupStdAnnual.getDaysIncapacityNotPaid(); }
    public double getDaysIncapacityNotPaidPayroll() { return moHrsDaysGroupStdPayroll.getDaysIncapacityNotPaid(); }
    public double getDaysNotWorkedButPaidPayroll() { return moHrsDaysGroupStdPayroll.getDaysNotWorkedButPaid(); }
    public double getDaysNotWorkedNotPaidPayroll() { return moHrsDaysGroupStdPayroll.getDaysNotWorkedNotPaid(); }
    
    /**
     * Get employee's not-worked days in current payrooll.
     * @return Employee's not-worked days in current payrooll.
     */
    public double getNotWorkedDaysPayroll() { return moHrsDaysGroupStdPayroll.getNotWorkedDays(); }
    
    /**
     * Get employee's workable-calendar days in current payroll.
     * @return Receipt days (in current payroll) - not-worked days in current payrooll.
     */
    public double getWorkableCalendarDaysPayroll() { return getReceiptDays() - getNotWorkedDaysPayroll(); }
    
    /**
     * Get employee's workable-business days in current payroll.
     * @return Business days in current payroll - not-worked days in current payrooll.
     */
    public double getWorkableBusinessDaysPayroll() { return getReceiptBusinessDays() - getNotWorkedDaysPayroll(); }
    
    /**
     * Get employee's taxable days in current year.
     * @return Employee's taxable days in current year.
     */
    public double getTaxableDaysAnnual() { return moHrsDaysGroupStdAnnual.getTaxableDays(); }
    
    /**
     * Get employee's taxable days in current payroll.
     * @return Employee's taxable days in current payroll.
     */
    public double getTaxableDaysPayroll() { return moHrsDaysGroupStdPayroll.getTaxableDays(); }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModSysConsts;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SHrsEmployee {

    protected SDbEmployee moEmployee;
    protected final int mnPeriodYear;   // for accumulated earnings & deductions
    protected final int mnPeriod; // for accumulated earnings & deductions
    protected final Date mtPeriodStart; // analyzed period start date
    protected final Date mtPeriodEnd;   // analyzed period end date
    
    protected SHrsReceipt moHrsReceipt; // current receipt
    
    protected ArrayList<SDbLoan> maLoans;       // all employee loans
    protected ArrayList<SHrsLoan> maHrsLoans;   // all employee loans
    protected ArrayList<SDbAbsence> maAbsences; // all employee absences
    protected ArrayList<SDbAbsenceConsumption> maAbsenceConsumptions; // all employee absences consumptions
    protected ArrayList<SDbEmployeeHireLog> maEmployeeHireLogs;       // all hire log entries
    protected ArrayList<SHrsAccumulatedEarning> maYearHrsAccumulatedEarnigs;       // accumulated earnings in year, current payroll receipt not included (applies only for earnings exempt computation)
    protected ArrayList<SHrsAccumulatedEarning> maYearHrsAccumulatedEarnigsByType; // accumulated earnings in year, current payroll receipt not included (applies only for annual tax computation)
    protected ArrayList<SHrsAccumulatedDeduction> maYearHrsAccumulatedDeductions;       // accumulated deductions in year, current payroll receipt not included (applies only for deductions)
    protected ArrayList<SHrsAccumulatedDeduction> maYearHrsAccumulatedDeductionsByType; // accumulated deductions in year, current payroll receipt not included (applies only for annual tax computation)
    
    protected int mnDaysHiredAnnual;    // hired days in fiscal year
    protected int mnDaysHiredPayroll;   // hired days in the period payroll
    protected int mnBusinessDays;       // business days in the period payroll
    protected double mdAnnualTaxableEarnings;       // taxable amount accumulated of earnigs 
    protected double mdAnnualTaxableEarningsArt174; // taxable amount accumulated of earnigs configured for articule 174 the RLISR
    protected double mdAnnualTaxCompensated;
    protected double mdAnnualTaxSubsidyCompensated;
    protected SHrsDaysByPeriod moHrsDaysPrev;
    protected SHrsDaysByPeriod moHrsDaysCurr;
    protected SHrsDaysByPeriod moHrsDaysNext;

    public SHrsEmployee(final SHrsPayroll hrsPayroll, final SHrsReceipt hrsReceipt, final int employeeId) throws Exception {
        SDbPayroll payroll = hrsPayroll.getPayroll(); // convenience variable
        
        moEmployee = hrsPayroll.getEmployee(employeeId);
        mnPeriodYear = payroll.getPeriodYear();
        mnPeriod = payroll.getPeriod();
        mtPeriodStart = payroll.getDateStart();
        mtPeriodEnd = payroll.getDateEnd();
        
        moHrsReceipt = hrsReceipt;
        
        maLoans = new ArrayList<>();
        maHrsLoans = new ArrayList<>();
        maAbsences = new ArrayList<>();
        maAbsenceConsumptions = new ArrayList<>();
        maEmployeeHireLogs = new ArrayList<>();
        maYearHrsAccumulatedEarnigs = new ArrayList<>();
        maYearHrsAccumulatedEarnigsByType = new ArrayList<>();
        maYearHrsAccumulatedDeductions = new ArrayList<>();
        maYearHrsAccumulatedDeductionsByType = new ArrayList<>();
        
        mnDaysHiredAnnual = 0;
        mnDaysHiredPayroll = 0;
        mnBusinessDays = 0;
        mdAnnualTaxableEarnings = 0;
        mdAnnualTaxableEarningsArt174 = 0;
        mdAnnualTaxCompensated = 0;
        mdAnnualTaxSubsidyCompensated = 0;
        moHrsDaysPrev = null;
        moHrsDaysCurr = null;
        moHrsDaysNext = null;
    }

    public void setDaysHiredAnnual(int n) { mnDaysHiredAnnual = n; }
    public void setDaysHiredPayroll(int n) { mnDaysHiredPayroll = n; }
    public void setBusinessDays(int n) { mnBusinessDays = n; }
    public void setAnnualTaxableEarnings(double d) { mdAnnualTaxableEarnings = d; }
    public void setAnnualTaxableEarningArt174(double d) { mdAnnualTaxableEarningsArt174 = d; }
    public void setAnnualTaxCompensated(double d) { mdAnnualTaxCompensated = d; }
    public void setAnnualTaxSubsidyCompensated(double d) { mdAnnualTaxSubsidyCompensated = d; }
    public void setHrsDaysPrev(SHrsDaysByPeriod o) { moHrsDaysPrev = o; }
    public void setHrsDaysCurr(SHrsDaysByPeriod o) { moHrsDaysCurr = o; }
    public void setHrsDaysNext(SHrsDaysByPeriod o) { moHrsDaysNext = o; }

    public SDbEmployee getEmployee() { return moEmployee; }
    public int getPeriodYear() { return mnPeriodYear; }
    public int getPeriod() { return mnPeriod; }
    public Date getPeriodStart() { return mtPeriodStart; }
    public Date getPeriodEnd() { return mtPeriodEnd; }
    
    public SHrsReceipt getHrsReceipt() { return moHrsReceipt; }
    
    public ArrayList<SDbLoan> getLoans() { return maLoans; }
    public ArrayList<SHrsLoan> getHrsLoans() { return maHrsLoans; }
    public ArrayList<SDbAbsence> getAbsences() { return maAbsences; }
    public ArrayList<SDbAbsenceConsumption> getAbsenceConsumptions() { return maAbsenceConsumptions; }
    public ArrayList<SDbEmployeeHireLog> getEmployeeHireLogs() { return maEmployeeHireLogs; }
    public double getAnnualTaxableEarnings() { return mdAnnualTaxableEarnings; }
    public double getAnnualTaxableEarningsArt174() { return mdAnnualTaxableEarningsArt174; }
    public double getAnnualTaxCompensated() { return mdAnnualTaxCompensated; }
    public double getAnnualTaxSubsidyCompensated() { return mdAnnualTaxSubsidyCompensated; }
    public SHrsDaysByPeriod getHrsDaysPrev() { return moHrsDaysPrev; }
    public SHrsDaysByPeriod getHrsDaysCurr() { return moHrsDaysCurr; }
    public SHrsDaysByPeriod getHrsDaysNext() { return moHrsDaysNext; }
    
    public SDbLoan getLoan(final int loanId) {
        SDbLoan loan = null;

        if (loanId != 0) {
            for (SDbLoan entry : maLoans) {
                if (entry.getPkLoanId() == loanId) {
                    loan = entry;
                    break;
                }
            }
        }

        return loan;
    }
    
    public String getLoanDescription(final int loanId) {
        SDbLoan loan = getLoan(loanId);

        return loan == null ? "" : loan.composeLoanDescription();
    }
    
    /**
     * Get loan by its ID.
     * @param loanId ID of desired loan.
     * @return 
     */
    public SHrsLoan getHrsLoan(final int loanId) {
        SHrsLoan hrsLoan = null;
        SDbLoan loan = getLoan(loanId);
        
        for (SHrsLoan hl : maHrsLoans) {
            if (SLibUtils.compareKeys(loan.getPrimaryKey(), hl.getLoan().getPrimaryKey())) {
                hrsLoan = hl;
                break;
            }
        }
        
        return hrsLoan;
    }

    /**
     * Clear first existing elements, and after that add all provided elements in array.
     * @param array 
     */
    public void addAllYearHrsAccumulatedEarnigs(ArrayList<SHrsAccumulatedEarning> array) {
        maYearHrsAccumulatedEarnigs.clear();
        maYearHrsAccumulatedEarnigs.addAll(array);
    }
    
    /**
     * Clear first existing elements, and after that add all provided elements in array.
     * @param array 
     */
    public void addAllYearHrsAccumulatedEarnigsByType(ArrayList<SHrsAccumulatedEarning> array) {
        maYearHrsAccumulatedEarnigsByType.clear();
        maYearHrsAccumulatedEarnigsByType.addAll(array);
    }
    
    /**
     * Clear first existing elements, and after that add all provided elements in array.
     * @param array 
     */
    public void addAllYearHrsAccumulatedDeductions(ArrayList<SHrsAccumulatedDeduction> array) {
        maYearHrsAccumulatedDeductions.clear();
        maYearHrsAccumulatedDeductions.addAll(array);
    }
    
    /**
     * Clear first existing elements, and after that add all provided elements in array.
     * @param array 
     */
    public void addAllYearHrsAccumulatedDeductionsByType(ArrayList<SHrsAccumulatedDeduction> array) {
        maYearHrsAccumulatedDeductionsByType.clear();
        maYearHrsAccumulatedDeductionsByType.addAll(array);
    }
    
    /**
     * Get accumulated earning by its ID.
     * @param earningId ID of desired earning.
     * @return 
     */
    public SHrsAccumulatedEarning getHrsAccumulatedEarning(final int earningId) {
        SHrsAccumulatedEarning hrsAccumulatedEarning = null;

        for (SHrsAccumulatedEarning earning : maYearHrsAccumulatedEarnigs) {
            if (earning.getEarningId() == earningId) {
                hrsAccumulatedEarning = earning;
                break;
            }
        }

        return hrsAccumulatedEarning;
    }

    /**
     * Get accumulated earning by its earning type.
     * @param earningType ID of desired earning type.
     * @return 
     */
    public SHrsAccumulatedEarning getHrsAccumulatedEarningByType(final int earningType) {
        SHrsAccumulatedEarning hrsAccumulatedEarning = null;

        for (SHrsAccumulatedEarning earning : maYearHrsAccumulatedEarnigsByType) {
            if (earning.getEarningId() == earningType) {
                hrsAccumulatedEarning = earning;
                break;
            }
        }

        return hrsAccumulatedEarning;
    }

    /**
     * Get accumulated deduction by its ID.
     * @param deductionId ID of desired deduction.
     * @return 
     */
    public SHrsAccumulatedDeduction getHrsAccumulatedDeduction(final int deductionId) {
        SHrsAccumulatedDeduction hrsAccumulatedDeduction = null;

        for (SHrsAccumulatedDeduction deduction : maYearHrsAccumulatedDeductions) {
            if (deduction.getDeductionId() == deductionId) {
                hrsAccumulatedDeduction = deduction;
                break;
            }
        }

        return hrsAccumulatedDeduction;
    }

    /**
     * Get accumulated deduction by its deduction type.
     * @param deductionType ID of desired deduction type.
     * @return 
     */
    public SHrsAccumulatedDeduction getHrsAccumulatedDeductionByType(final int deductionType) {
        SHrsAccumulatedDeduction hrsAccumulatedDeduction = null;

        for (SHrsAccumulatedDeduction deduction : maYearHrsAccumulatedDeductionsByType) {
            if (deduction.getDeductionId() == deductionType) {
                hrsAccumulatedDeduction = deduction;
                break;
            }
        }

        return hrsAccumulatedDeduction;
    }

    public SHrsEmployeeDays createEmployeeDays() {
        SDbPayroll payroll = moHrsReceipt.getHrsPayroll().getPayroll(); // convenience variable
        double daysPayrollCalendar = payroll.getCalendarDays_r(); // casted to double to compute a correct factor!
        double factorCalendar = daysPayrollCalendar == 0 ? 0 : payroll.getReceiptDays() / daysPayrollCalendar;

        int daysReceipt = mnDaysHiredPayroll;
        int daysWorking;

        if (payroll.getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_WEE) {
            int daysInactive = payroll.getReceiptDays() - daysReceipt;
            daysWorking = payroll.getWorkingDays() - daysInactive;
        }
        else {
            daysWorking = mnDaysHiredPayroll;
        }

        int daysNotWorkedButPaid = 0;
        int daysNotWorkedNotPaid = 0;
        int daysDisabilityNotPaidPayroll = 0;
        int daysDisabilityNotPaidAnnual = 0;

        for (SDbAbsenceConsumption absenceConsumption : moHrsReceipt.getAbsenceConsumptions()) { // consumptions in receipt
            if (absenceConsumption.getParentAbsence().isXtaAbsenceTypePayable()) {
                daysNotWorkedButPaid += absenceConsumption.getEffectiveDays();
            }
            else {
                daysNotWorkedNotPaid += absenceConsumption.getEffectiveDays();

                if (absenceConsumption.getParentAbsence().isDisability()) {
                    daysDisabilityNotPaidPayroll += absenceConsumption.getEffectiveDays();
                }
            }
        }

        Date yearStart = SLibTimeUtils.getBeginOfYear(mtPeriodStart);
        Date yearEnd = SLibTimeUtils.getEndOfYear(mtPeriodEnd).compareTo(mtPeriodEnd) <= 0 ? SLibTimeUtils.getEndOfYear(mtPeriodEnd) : mtPeriodEnd;

        for (SDbAbsenceConsumption absenceConsumption : maAbsenceConsumptions) { // consumptions in year
            if (!absenceConsumption.getParentAbsence().isXtaAbsenceTypePayable() && absenceConsumption.getParentAbsence().isDisability() && 
                    SLibTimeUtils.isBelongingToPeriod(absenceConsumption.getDateStart(), yearStart, yearEnd)) {
                daysDisabilityNotPaidAnnual += absenceConsumption.getEffectiveDays();
            }
        }

        SHrsEmployeeDays hrsEmployeeDays = new SHrsEmployeeDays(mnPeriodYear, mnPeriod, mtPeriodStart, mtPeriodEnd);
        
        hrsEmployeeDays.setFactorCalendar(factorCalendar);
        hrsEmployeeDays.setFactorDaysPaid(moHrsReceipt.getHrsPayroll().getFactorDaysPaid());
        hrsEmployeeDays.setReceiptDays(daysReceipt);
        hrsEmployeeDays.setWorkingDays(daysWorking);
        hrsEmployeeDays.setDaysWorked(daysWorking - (daysNotWorkedButPaid + daysNotWorkedNotPaid));
        hrsEmployeeDays.setDaysHiredPayroll(mnDaysHiredPayroll);
        hrsEmployeeDays.setDaysHiredAnnual(mnDaysHiredAnnual);
        hrsEmployeeDays.setDaysDisabilityNotPaidPayroll(daysDisabilityNotPaidPayroll);
        hrsEmployeeDays.setDaysDisabilityNotPaidAnnual(daysDisabilityNotPaidAnnual);
        hrsEmployeeDays.setDaysNotWorkedButPaid(daysNotWorkedButPaid);
        hrsEmployeeDays.setDaysNotWorkedNotPaid(daysNotWorkedNotPaid);
        
        hrsEmployeeDays.setBusinessDays(mnBusinessDays);

        return hrsEmployeeDays;
    }
}

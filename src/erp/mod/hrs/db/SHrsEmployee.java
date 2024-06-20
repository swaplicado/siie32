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
 * Las instancias de esta clase permiten el enfoque en un empleado específico para la emisión de recibos de nómina.
 * 
 * @author Néstor Ávalos, Sergio Flores
 */
public class SHrsEmployee {
    
    protected SHrsReceipt moHrsReceipt;     // current HRS receipt
    protected SDbEmployee moEmployee;       // current employment of receipt
    
    /** All employee loans. */
    protected ArrayList<SDbLoan> maLoans;
    /** All employee loans excluding current payroll. */
    protected ArrayList<SHrsLoan> maHrsLoans;
    /** All employee absences. */
    protected ArrayList<SDbAbsence> maAbsences;
    /** All employee absences consumptions excluding current payroll. */
    protected ArrayList<SDbAbsenceConsumption> maAbsenceConsumptions;
    
    /** Hire log entries concerning current payroll. */
    protected ArrayList<SDbEmployeeHireLog> maEmployeeHireLogsPayroll;
    
    protected int mnDaysHiredPayroll;       // hired days in the period payroll
    protected int mnReceiptBusinessDays;    // business days in the period payroll
    /** HRS employee movements for tax and attendance standard processing of annual period. */
    protected SHrsEmployeeMvts moHrsEmployeeMvtsAnnualStd;
    /** HRS employee movements for tax and attendance transition processing of annual old-style period. (E.g., calculation of tax subsidy until April 2024.) */
    protected SHrsEmployeeMvts moHrsEmployeelMvtsAnnualTransOldStyle;
    /** HRS employee movements for tax and attendance transition processing of annual new-style period. (E.g., calculation of tax subsidy since May 2024.) */
    protected SHrsEmployeeMvts moHrsEmployeelMvtsAnnualTransNewStyle;
    
    protected SHrsDaysByPeriod moHrsDaysPrev;
    protected SHrsDaysByPeriod moHrsDaysCurr;
    protected SHrsDaysByPeriod moHrsDaysNext;

    public SHrsEmployee(final SHrsReceipt hrsReceipt, final int employeeId) throws Exception {
        moHrsReceipt = hrsReceipt;
        moEmployee = moHrsReceipt.getHrsPayroll().getEmployee(employeeId);
        
        maLoans = new ArrayList<>();
        maHrsLoans = new ArrayList<>();
        maAbsences = new ArrayList<>();
        maAbsenceConsumptions = new ArrayList<>();
        
        maEmployeeHireLogsPayroll = new ArrayList<>();
        
        mnDaysHiredPayroll = 0;
        mnReceiptBusinessDays = 0;
        moHrsEmployeeMvtsAnnualStd = new SHrsEmployeeMvts();
        moHrsEmployeelMvtsAnnualTransOldStyle = null;
        moHrsEmployeelMvtsAnnualTransNewStyle = null;
        
        moHrsDaysPrev = null;
        moHrsDaysCurr = null;
        moHrsDaysNext = null;
    }

    public void setDaysHiredAnnual(int n) { moHrsEmployeeMvtsAnnualStd.setDaysHired(n); }
    public void setDaysHiredPayroll(int n) { mnDaysHiredPayroll = n; }
    public void setReceiptBusinessDays(int n) { mnReceiptBusinessDays = n; }
    /** HRS employee movements for tax and attendance transition processing of annual old-style period. (E.g., calculation of tax subsidy until April 2024.) */
    public void setHrsEmployeelMvtsAnnualTransOldStyle(SHrsEmployeeMvts o) { moHrsEmployeelMvtsAnnualTransOldStyle = o; }
    /** HRS employee movements for tax and attendance transition processing of annual new-style period. (E.g., calculation of tax subsidy since May 2024.) */
    public void setHrsEmployeelMvtsAnnualTransNewStyle(SHrsEmployeeMvts o) { moHrsEmployeelMvtsAnnualTransNewStyle = o; }
    
    public void setHrsDaysPrev(SHrsDaysByPeriod o) { moHrsDaysPrev = o; }
    public void setHrsDaysCurr(SHrsDaysByPeriod o) { moHrsDaysCurr = o; }
    public void setHrsDaysNext(SHrsDaysByPeriod o) { moHrsDaysNext = o; }

    public SHrsReceipt getHrsReceipt() { return moHrsReceipt; }
    public SDbEmployee getEmployee() { return moEmployee; }
    
    /** All employee loans. */
    public ArrayList<SDbLoan> getLoans() { return maLoans; }
    /** All employee loans excluding current payroll. */
    public ArrayList<SHrsLoan> getHrsLoans() { return maHrsLoans; }
    /** All employee absences. */
    public ArrayList<SDbAbsence> getAbsences() { return maAbsences; }
    /** All employee absences consumptions excluding current payroll. */
    public ArrayList<SDbAbsenceConsumption> getAbsenceConsumptions() { return maAbsenceConsumptions; }
    
    /** All hire log entries concerning current payroll. */
    public ArrayList<SDbEmployeeHireLog> getEmployeeHireLogsPayroll() { return maEmployeeHireLogsPayroll; }
    
    public int getDaysHiredAnnual() { return moHrsEmployeeMvtsAnnualStd.getDaysHired(); }
    public int getDaysHiredPayroll() { return mnDaysHiredPayroll; }
    public int getReceiptBusinessDays() { return mnReceiptBusinessDays; }
    /** HRS employee movements for tax and attendance transition processing of annual old-style period. (E.g., calculation of tax subsidy until April 2024.) */
    public SHrsEmployeeMvts getHrsEmployeelMvtsAnnualTransOldStyle() { return moHrsEmployeelMvtsAnnualTransOldStyle; }
    /** HRS employee movements for tax and attendance transition processing of annual new-style period. (E.g., calculation of tax subsidy since May 2024.) */
    public SHrsEmployeeMvts getHrsEmployeelMvtsAnnualTransNewStyle() { return moHrsEmployeelMvtsAnnualTransNewStyle; }
    
    public SHrsDaysByPeriod getHrsDaysPrev() { return moHrsDaysPrev; }
    public SHrsDaysByPeriod getHrsDaysCurr() { return moHrsDaysCurr; }
    public SHrsDaysByPeriod getHrsDaysNext() { return moHrsDaysNext; }
    
    /** Ordinary taxable earnings. */
    public void setAnnualTaxableEarnings(double d) { moHrsEmployeeMvtsAnnualStd.setTaxableEarnings(d); }
    /** Taxable earnings by articule 174 of the RLISR. */
    public void setAnnualTaxableEarningsArt174(double d) { moHrsEmployeeMvtsAnnualStd.setTaxableEarningsArt174(d); }
    /** Annual tax compensated actually equals tax subsidy compensated! */
    public void setAnnualTaxCompensated(double d) { moHrsEmployeeMvtsAnnualStd.setTaxCompensated(d); }
    /** Annual tax subsidy compensated actually equals tax compensated! */
    public void setAnnualTaxSubsidyCompensated(double d) { moHrsEmployeeMvtsAnnualStd.setTaxSubsidyCompensated(d); }
    
    /** Old-style assessed tax subsidy preserved for informative purposes! It will exist only in 2024, the transition year.*/
    public void setAnnualTaxSubsidyAssessedOldInformative(double d) { moHrsEmployeeMvtsAnnualStd.setTaxSubsidyAssessedOldInformative(d); }
    
    /** Ordinary taxable earnings. */
    public double getAnnualTaxableEarnings() { return moHrsEmployeeMvtsAnnualStd.getTaxableEarnings(); }
    /** Taxable earnings by articule 174 of the RLISR. */
    public double getAnnualTaxableEarningsArt174() { return moHrsEmployeeMvtsAnnualStd.getTaxableEarningsArt174(); }
    /** Annual tax compensated actually equals tax subsidy compensated! */
    public double getAnnualTaxCompensated() { return moHrsEmployeeMvtsAnnualStd.getTaxCompensated(); }
    /** Annual tax subsidy compensated actually equals tax compensated! */
    public double getAnnualTaxSubsidyCompensated() { return moHrsEmployeeMvtsAnnualStd.getTaxSubsidyCompensated(); }
    
    /** Old-style assessed tax subsidy preserved for informative purposes! It will exist only in 2024, the transition year.*/
    public double getAnnualTaxSubsidyAssessedOldInformative() { return moHrsEmployeeMvtsAnnualStd.getTaxSubsidyAssessedOldInformative(); }
    
    /** Accumulated earnings in year, current payroll receipt not included (applies only for earnings exempt computation.) */
    public ArrayList<SHrsAccumulatedEarning> getHrsAccumulatedEarnigs() { return moHrsEmployeeMvtsAnnualStd.getHrsAccumulatedEarnings(); }
    /** Accumulated earnings in year, current payroll receipt not included (applies only for annual tax computation.) */
    public ArrayList<SHrsAccumulatedEarning> getHrsAccumulatedEarnigsByType() { return moHrsEmployeeMvtsAnnualStd.getHrsAccumulatedEarningsByType(); }
    /** Accumulated deductions in year, current payroll receipt not included (applies only for deductions.) */
    public ArrayList<SHrsAccumulatedDeduction> getHrsAccumulatedDeductions() { return moHrsEmployeeMvtsAnnualStd.getHrsAccumulatedDeductions(); }
    /** Accumulated deductions in year, current payroll receipt not included (applies only for annual tax computation.) */
    public ArrayList<SHrsAccumulatedDeduction> getHrsAccumulatedDeductionsByType() { return moHrsEmployeeMvtsAnnualStd.getHrsAccumulatedDeductionsByType(); }
    
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
     * Get accumulated earning by its ID.
     * @param earningId ID of desired earning.
     * @return 
     */
    public SHrsAccumulatedEarning getHrsAccumulatedEarning(final int earningId) {
        return moHrsEmployeeMvtsAnnualStd.getHrsAccumulatedEarning(earningId);
    }

    /**
     * Get accumulated earning by its earning type.
     * @param earningType ID of desired earning type.
     * @return 
     */
    public SHrsAccumulatedEarning getHrsAccumulatedEarningByType(final int earningType) {
        return moHrsEmployeeMvtsAnnualStd.getHrsAccumulatedEarningByType(earningType);
    }

    /**
     * Get accumulated deduction by its ID.
     * @param deductionId ID of desired deduction.
     * @return 
     */
    public SHrsAccumulatedDeduction getHrsAccumulatedDeduction(final int deductionId) {
        return moHrsEmployeeMvtsAnnualStd.getHrsAccumulatedDeduction(deductionId);
    }

    /**
     * Get accumulated deduction by its deduction type.
     * @param deductionType ID of desired deduction type.
     * @return 
     */
    public SHrsAccumulatedDeduction getHrsAccumulatedDeductionByType(final int deductionType) {
        return moHrsEmployeeMvtsAnnualStd.getHrsAccumulatedDeductionByType(deductionType);
    }

    public SHrsEmployeeDays createEmployeeDays() {
        SHrsPayroll hrsPayroll = moHrsReceipt.getHrsPayroll(); // convenience variable
        SDbPayroll payroll = hrsPayroll.getPayroll(); // convenience variable
        int daysWorking = 0;
        
        if (payroll.getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_WEE) {
            int daysInactive = payroll.getReceiptDays() - mnDaysHiredPayroll;
            daysWorking = payroll.getWorkingDays() - daysInactive;
        }
        else {
            daysWorking = mnDaysHiredPayroll;
        }
        
        int daysIncapacityNotPaidPayroll = 0;
        int daysNotWorkedButPaidPayroll = 0;
        int daysNotWorkedNotPaidPayroll = 0;
        
        for (SDbAbsenceConsumption absenceConsumption : moHrsReceipt.getAbsenceConsumptions()) { // consumptions in receipt
            if (absenceConsumption.getParentAbsence().isXtaAbsenceTypePayable()) {
                daysNotWorkedButPaidPayroll += absenceConsumption.getEffectiveDays();
            }
            else {
                daysNotWorkedNotPaidPayroll += absenceConsumption.getEffectiveDays();
                
                if (absenceConsumption.getParentAbsence().isDisability()) {
                    daysIncapacityNotPaidPayroll += absenceConsumption.getEffectiveDays();
                }
            }
        }
        
        int daysIncapacityNotPaidAnnual = 0;
        Date payrollStart = payroll.getDateStart();
        Date payrollEnd = payroll.getDateEnd();
        Date endOfYear = SLibTimeUtils.getEndOfYear(payrollEnd);
        Date yearStart = SLibTimeUtils.getBeginOfYear(payrollStart);
        Date yearEnd = payrollEnd.before(endOfYear) ? payrollEnd : endOfYear;
        
        for (SDbAbsenceConsumption absenceConsumption : maAbsenceConsumptions) { // consumptions in year
            if (SLibTimeUtils.isBelongingToPeriod(absenceConsumption.getDateStart(), yearStart, yearEnd)) {
                if (!absenceConsumption.getParentAbsence().isXtaAbsenceTypePayable() && absenceConsumption.getParentAbsence().isDisability()) {
                    daysIncapacityNotPaidAnnual += absenceConsumption.getEffectiveDays();
                }
            }
        }
        
        SHrsEmployeeDays hrsEmployeeDays = new SHrsEmployeeDays(payroll.getPeriodYear(), payroll.getPeriod());
        
        hrsEmployeeDays.setReceiptDays(mnDaysHiredPayroll);
        hrsEmployeeDays.setReceiptWorkingDays(daysWorking);
        hrsEmployeeDays.setReceiptDaysWorked(daysWorking - (daysNotWorkedButPaidPayroll + daysNotWorkedNotPaidPayroll));
        hrsEmployeeDays.setReceiptBusinessDays(mnReceiptBusinessDays);
        
        hrsEmployeeDays.setDaysHiredAnnual(moHrsEmployeeMvtsAnnualStd.getDaysHired());
        hrsEmployeeDays.setDaysHiredPayroll(mnDaysHiredPayroll);
        
        hrsEmployeeDays.setDaysIncapacityNotPaidAnnual(daysIncapacityNotPaidAnnual);
        hrsEmployeeDays.setDaysIncapacityNotPaidPayroll(daysIncapacityNotPaidPayroll);
        hrsEmployeeDays.setDaysNotWorkedButPaidPayroll(daysNotWorkedButPaidPayroll);
        hrsEmployeeDays.setDaysNotWorkedNotPaidPayroll(daysNotWorkedNotPaidPayroll);
        
        // attendance for tax subsidy year of transition of style (i.e., 2024), if applies:

        if (payroll.getFkTaxComputationTypeId() == SModSysConsts.HRSS_TP_TAX_COMP_ANN) {
            if (hrsPayroll.isTransitionYearForTaxSubsidy() && !payroll.isReadOnly()) { // please note that this block will be executed only along 2024!
                // payroll belongs to the year of transtition of style of subsidy (i.e., 2024):

                SDbEmploymentSubsidy employmentSubsidyAkaNewStyle = hrsPayroll.getEmploymentSubsidy(payroll.getFkEmploymentSubsidyId_n()); // the shiny "new-style"
                Date newStyleStart = employmentSubsidyAkaNewStyle.getDateStart(); // should be May 1st, 2024
                Date oldStyleEnd = SLibTimeUtils.addDate(newStyleStart, 0, 0, -1); // should be April 31th, 2024
                
                // HRS days group for tax and attendance transition processing of annual old-style period. (E.g., calculation of tax subsidy until April 2024.)
                
                int daysIncapacityNotPaidOldStyle = 0;
                SHrsEmployeeDaysGroup hrsDaysGroupOldStyle = new SHrsEmployeeDaysGroup();
                
                for (SDbAbsenceConsumption absenceConsumption : maAbsenceConsumptions) { // consumptions in year
                    if (SLibTimeUtils.isBelongingToPeriod(absenceConsumption.getDateStart(), yearStart, oldStyleEnd)) {
                        if (!absenceConsumption.getParentAbsence().isXtaAbsenceTypePayable() && absenceConsumption.getParentAbsence().isDisability()) {
                            daysIncapacityNotPaidOldStyle += absenceConsumption.getEffectiveDays();
                        }
                    }
                }
                
                hrsDaysGroupOldStyle.setDaysHired(moHrsEmployeelMvtsAnnualTransOldStyle.getDaysHired());
                hrsDaysGroupOldStyle.setDaysIncapacityNotPaid(daysIncapacityNotPaidOldStyle);
                
                hrsEmployeeDays.setHrsDaysGroupTransAnnualOldStyle(hrsDaysGroupOldStyle);
                
                // HRS days group for tax and attendance transition processing of annual new-style period. (E.g., calculation of tax subsidy since May 2024.)
                
                int daysIncapacityNotPaidNewStyle = 0;
                SHrsEmployeeDaysGroup hrsDaysGroupNewStyle = new SHrsEmployeeDaysGroup();
                
                for (SDbAbsenceConsumption absenceConsumption : maAbsenceConsumptions) { // consumptions in year
                    if (SLibTimeUtils.isBelongingToPeriod(absenceConsumption.getDateStart(), newStyleStart, yearEnd)) {
                        if (!absenceConsumption.getParentAbsence().isXtaAbsenceTypePayable() && absenceConsumption.getParentAbsence().isDisability()) {
                            daysIncapacityNotPaidNewStyle += absenceConsumption.getEffectiveDays();
                        }
                    }
                }
                
                hrsDaysGroupNewStyle.setDaysHired(moHrsEmployeelMvtsAnnualTransNewStyle.getDaysHired());
                hrsDaysGroupNewStyle.setDaysIncapacityNotPaid(daysIncapacityNotPaidNewStyle);
                
                hrsEmployeeDays.setHrsDaysGroupTransAnnualNewStyle(hrsDaysGroupNewStyle);
            }
        }
        
        return hrsEmployeeDays;
    }
}

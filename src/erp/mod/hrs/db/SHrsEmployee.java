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

    protected int mnYear;               // for accumulated earnings & deductions
    protected int mnPeriod;             // for accumulated earnings & deductions
    protected Date mtPeriodStart;       // analyzed period start date
    protected Date mtPeriodEnd;         // analyzed period end date
    protected int mnTaxComputationType; // current tax computation type
    protected SDbEmployee moEmployee;
    protected SHrsPayrollReceipt moHrsPayrollReceipt;           // current payroll receipt
    protected ArrayList<SDbLoan> maLoans;                       // all employee loans
    protected ArrayList<SHrsLoanPayments> maHrsLoanPayments;    // all employee loan payments
    protected ArrayList<SDbAbsence> maAbsences;                 // all employee absences
    protected ArrayList<SDbAbsenceConsumption> maAbsencesAbsenceConsumptions;       // all employee absences consumptions
    protected ArrayList<SDbEmployeeHireLog> maEmployeeHireLogs;                     // all hire log entries
    protected ArrayList<SHrsAccumulatedEarning> maYearHrsAccumulatedEarnigs;        // accumulated earnings in year, current payroll receipt not included (applies only for earnings exempt computation)
    protected ArrayList<SHrsAccumulatedEarning> maYearHrsAccumulatedEarnigsByType;  // accumulated earnings in year, current payroll receipt not included (applies only for annual tax computation)
    protected ArrayList<SHrsAccumulatedEarning> maYearHrsAccumulatedEarnigsByTaxComputation;    // accumulated earnings in year, current payroll receipt not included (applies only for annual tax computation)
    protected ArrayList<SHrsAccumulatedDeduction> maYearHrsAccumulatedDeductions;               // accumulated deductions in year, current payroll receipt not included (applies only for deductions)
    protected ArrayList<SHrsAccumulatedDeduction> maYearHrsAccumulatedDeductionsByType;         // accumulated deductions in year, current payroll receipt not included (applies only for annual tax computation)
    protected ArrayList<SHrsAccumulatedDeduction> maYearHrsAccumulatedDeductionsByTaxComputation;   // accumulated deductions in year, current payroll receipt not included (applies only for annual tax computation)
    //protected ArrayList<SDbPayrollReceiptDay> maYearPayrollReceiptDays;                           // payroll receipt days in year, current payroll receipt not included (applies only for annual tax computation)
    protected ArrayList<SHrsBenefit> maHrsBenefit;  // payroll receipt benefit paid and applied
    protected int mnDaysHiredAnnual;    // hired days in fiscal year
    protected int mnDaysHiredPayroll;   // hired days in the period payroll
    protected int mnBusinessDays;       // business days in the period payroll
    protected int mnSeniority;          // seniority to payroll end
    protected double mdAccumulatedTaxableEarnings;      // taxable amount accumulated of earnigs 
    protected double mdAccumulatedTaxableEarningsAlt;   // taxable amount accumulated of earnigs configured for articule 174 the RLISR
    protected SHrsDaysByPeriod moHrsDaysPrev;
    protected SHrsDaysByPeriod moHrsDaysCurr;
    protected SHrsDaysByPeriod moHrsDaysNext;

    public SHrsEmployee(final int year, final int period, final Date periodStart, final Date periodEnd, final int taxComputationType) throws Exception {
        mnYear = year;
        mnPeriod = period;
        mtPeriodStart = periodStart;
        mtPeriodEnd = periodEnd;
        mnTaxComputationType = taxComputationType;
        moEmployee = null;
        moHrsPayrollReceipt = null;
        maLoans = new ArrayList<SDbLoan>();
        maHrsLoanPayments = new ArrayList<SHrsLoanPayments>();
        maAbsences = new ArrayList<SDbAbsence>();
        maAbsencesAbsenceConsumptions = new ArrayList<SDbAbsenceConsumption>();
        maEmployeeHireLogs = new ArrayList<SDbEmployeeHireLog>();
        maYearHrsAccumulatedEarnigs = new ArrayList<SHrsAccumulatedEarning>();
        maYearHrsAccumulatedEarnigsByType = new ArrayList<SHrsAccumulatedEarning>();
        maYearHrsAccumulatedDeductions = new ArrayList<SHrsAccumulatedDeduction>();
        maYearHrsAccumulatedEarnigsByTaxComputation = new ArrayList<SHrsAccumulatedEarning>();
        maYearHrsAccumulatedDeductionsByTaxComputation = new ArrayList<SHrsAccumulatedDeduction>();
        maYearHrsAccumulatedDeductionsByType = new ArrayList<SHrsAccumulatedDeduction>();
        maHrsBenefit = new ArrayList<SHrsBenefit>();
        mnDaysHiredAnnual = 0;
        mnDaysHiredPayroll = 0;
        mnBusinessDays = 0;
        mnSeniority = 0;
        mdAccumulatedTaxableEarnings = 0;
        mdAccumulatedTaxableEarningsAlt = 0;
        moHrsDaysPrev = null;
        moHrsDaysCurr = null;
        moHrsDaysNext = null;
    }

    public void setYear(final int n) { mnYear = n; }
    public void setPeriod(final int n) { mnPeriod = n; }
    public void setPeriodStart(final Date t) { mtPeriodStart = t; }
    public void setPeriodEnd(final Date t) { mtPeriodEnd = t; }
    public void setTaxComputationType(int n) { mnTaxComputationType = n; }
    public void setEmployee(final SDbEmployee o) { moEmployee = o; }
    public void setHrsPayrollReceipt(final SHrsPayrollReceipt o) { moHrsPayrollReceipt = o; }
    public void setDaysHiredAnnual(int n) { mnDaysHiredAnnual = n; }
    public void setDaysHiredPayroll(int n) { mnDaysHiredPayroll = n; }
    public void setBusinessDays(int n) { mnBusinessDays = n; }
    public void setSeniority(int n) { mnSeniority = n; }
    public void setAccumulatedTaxableEarning(double d) { mdAccumulatedTaxableEarnings = d; }
    public void setAccumulatedTaxableEarningAlt(double d) { mdAccumulatedTaxableEarningsAlt = d; }
    public void setHrsDaysPrev(SHrsDaysByPeriod o) { moHrsDaysPrev = o; }
    public void setHrsDaysCurr(SHrsDaysByPeriod o) { moHrsDaysCurr = o; }
    public void setHrsDaysNext(SHrsDaysByPeriod o) { moHrsDaysNext = o; }

    public int getYear() { return mnYear; }
    public int getPeriod() { return mnPeriod; }
    public Date getPeriodStart() { return mtPeriodStart; }
    public Date getPeriodEnd() { return mtPeriodEnd; }
    public int getTaxComputationType() { return mnTaxComputationType; }
    public SDbEmployee getEmployee() { return moEmployee; }
    public SHrsPayrollReceipt getHrsPayrollReceipt() { return moHrsPayrollReceipt; }
    public ArrayList<SDbLoan> getLoans() { return maLoans; }
    public ArrayList<SHrsLoanPayments> getLoanPayments() { return maHrsLoanPayments; }
    public ArrayList<SDbAbsence> getAbsences() { return maAbsences; };
    public ArrayList<SDbAbsenceConsumption> getAbsencesConsumptions() { return maAbsencesAbsenceConsumptions; };
    public ArrayList<SDbEmployeeHireLog> getEmployeeHireLogs() { return maEmployeeHireLogs; }
    public ArrayList<SHrsAccumulatedEarning> getYearHrsAccumulatedEarnigs() { return maYearHrsAccumulatedEarnigs; };
    public ArrayList<SHrsAccumulatedEarning> getYearHrsAccumulatedEarnigsByType() { return maYearHrsAccumulatedEarnigsByType; };
    public ArrayList<SHrsAccumulatedEarning> getYearHrsAccumulatedEarnigsByTaxComputation() { return maYearHrsAccumulatedEarnigsByTaxComputation; };
    public ArrayList<SHrsAccumulatedDeduction> getYearHrsAccumulatedDeductions() { return maYearHrsAccumulatedDeductions; }
    public ArrayList<SHrsAccumulatedDeduction> getYearHrsAccumulatedDeductionsByType() { return maYearHrsAccumulatedDeductionsByType; }
    public ArrayList<SHrsAccumulatedDeduction> getYearHrsAccumulatedDeductionsByTaxComputation() { return maYearHrsAccumulatedDeductionsByTaxComputation; }
    public ArrayList<SHrsBenefit> getHrsBenefit() { return maHrsBenefit; }
    public int getDaysHiredAnnual() { return mnDaysHiredAnnual; }
    public int getDaysHiredPayroll() { return mnDaysHiredPayroll; }
    public int getBusinessDays() { return mnBusinessDays; }
    public int getSeniority() { return mnSeniority; }
    public double getAccummulatedTaxableEarnings() { return mdAccumulatedTaxableEarnings; }
    public double getAccummulatedTaxableEarningsAlt() { return mdAccumulatedTaxableEarningsAlt; }
    public SHrsDaysByPeriod getHrsDaysPrev() { return moHrsDaysPrev; }
    public SHrsDaysByPeriod getHrsDaysCurr() { return moHrsDaysCurr; }
    public SHrsDaysByPeriod getHrsDaysNext() { return moHrsDaysNext; }
    
    public ArrayList<SDbLoan> getLoanByType(final int loanType) {
        ArrayList<SDbLoan> aLoan = new ArrayList<SDbLoan>();

        for (SDbLoan loan : maLoans) {
            if (loan.getFkLoanTypeId() == loanType) {
                aLoan.add(loan);
            }
        }

        return aLoan;
    }

    public SDbLoan getLoan(final int loan) {
        SDbLoan oLoan = null;

        for (SDbLoan entry : maLoans) {
            if (entry.getPkLoanId() == loan) {
                oLoan = entry;
                break;
            }
        }

        return oLoan;
    }
    
    public SHrsLoanPayments getLoanPayment(final int loanId) {
        SHrsLoanPayments hrsLoanPayments = null;
        SDbLoan loan = getLoan(loanId);
        
        for (SHrsLoanPayments loanPayments : maHrsLoanPayments) {
            if (SLibUtils.compareKeys(loan.getPrimaryKey(), loanPayments.getLoan().getPrimaryKey())) {
                hrsLoanPayments = loanPayments;
                break;
            }
        }
        
        return hrsLoanPayments;
    }

    public SHrsAccumulatedEarning getAccumulatedEarning(final int earning) {
        SHrsAccumulatedEarning oAccumulatedEarning = null;

        for (SHrsAccumulatedEarning accumulatedEarning : maYearHrsAccumulatedEarnigs) {
            if (accumulatedEarning.getEarningId() == earning) {
                oAccumulatedEarning = accumulatedEarning;
                break;
            }
        }

        return oAccumulatedEarning;
    }

    public SHrsAccumulatedEarning getAccumulatedEarningByType(final int earningType) {
        SHrsAccumulatedEarning oAccumulatedEarning = null;

        for (SHrsAccumulatedEarning accumulatedEarning : maYearHrsAccumulatedEarnigsByType) {
            if (accumulatedEarning.getEarningId() == earningType) {
                oAccumulatedEarning = accumulatedEarning;
                break;
            }
        }

        return oAccumulatedEarning;
    }

    public SHrsAccumulatedEarning getAccumulatedEarningByTaxComputation(final int earning) {
        SHrsAccumulatedEarning oAccumulatedEarning = null;

        for (SHrsAccumulatedEarning accumulatedEarning : maYearHrsAccumulatedEarnigsByTaxComputation) {
            if (accumulatedEarning.getEarningId() == earning) {
                oAccumulatedEarning = accumulatedEarning;
                break;
            }
        }

        return oAccumulatedEarning;
    }

    public SHrsAccumulatedDeduction getAccumulatedDeduction(final int deduction) {
        SHrsAccumulatedDeduction oAccumulatedDeduction = null;

        for (SHrsAccumulatedDeduction accumulatedDeduction : maYearHrsAccumulatedDeductions) {
            if (accumulatedDeduction.getDeductionId() == deduction) {
                oAccumulatedDeduction = accumulatedDeduction;
                break;
            }
        }

        return oAccumulatedDeduction;
    }

    public SHrsAccumulatedDeduction getAccumulatedDeductionByType(final int deductionType) {
        SHrsAccumulatedDeduction oAccumulatedDeduction = null;

        for (SHrsAccumulatedDeduction accumulatedDeduction : maYearHrsAccumulatedDeductionsByType) {
            if (accumulatedDeduction.getDeductionId() == deductionType) {
                oAccumulatedDeduction = accumulatedDeduction;
                break;
            }
        }

        return oAccumulatedDeduction;
    }

    public SHrsAccumulatedDeduction getAccumulatedDeductionByTaxComputation(final int deduction) {
        SHrsAccumulatedDeduction oAccumulatedDeduction = null;

        for (SHrsAccumulatedDeduction accumulatedDeduction : maYearHrsAccumulatedDeductionsByTaxComputation) {
            if (accumulatedDeduction.getDeductionId() == deduction) {
                oAccumulatedDeduction = accumulatedDeduction;
                break;
            }
        }

        return oAccumulatedDeduction;
    }
    
    public SHrsBenefit getBenefitType(final int benefitType, final int benefitYear, final int benefitAnn) {
        SHrsBenefit benefit = new SHrsBenefit(benefitType, benefitAnn, benefitYear);

        for (SHrsBenefit benefitRow : maHrsBenefit) {
            if (SLibUtils.compareKeys(benefitRow.getBenefitKey(), new int[] { benefitType, benefitAnn, benefitYear })) {
                benefit = benefitRow;
            }
        }

        return benefit;
    }
    
    public void updateHrsBenefits(SHrsBenefit hrsBenefit) {
        boolean add = true;
        double value = 0;
        double amount = 0;
        
        for (SHrsBenefit benefitRow : maHrsBenefit) {
            if (SLibUtils.compareKeys(benefitRow.getBenefitKey(), hrsBenefit.getBenefitKey())) {
                value = benefitRow.getValuePayedReceipt() + hrsBenefit.getValuePayedReceipt();
                amount = benefitRow.getAmountPayedReceipt() + hrsBenefit.getAmountPayedReceipt();
                
                benefitRow.setValuePayedReceipt(value);
                benefitRow.setAmountPayedReceipt(amount);
                add = false;
                break;
            }
        }
        
        if (add) {
            maHrsBenefit.add(hrsBenefit);
        }
    }

    private Date getNearestHireLogDate(boolean isActive, Date dateLimit) {
        Date nearestHireLogDate = null;
        Date dateLimitDateOnly = SLibTimeUtils.convertToDateOnly(dateLimit);

        for (SDbEmployeeHireLog hireLog : maEmployeeHireLogs) {
            if (hireLog.isHired() == isActive && hireLog.getDateHire().compareTo(dateLimitDateOnly) <= 0) {
                if (nearestHireLogDate == null) {
                    nearestHireLogDate = hireLog.getDateHire();
                }
                else if (nearestHireLogDate.compareTo(hireLog.getDateHire()) <= 0) {
                    nearestHireLogDate = hireLog.getDateHire();
                }
            }
        }

        if (nearestHireLogDate == null) {
            if (isActive) {
                if (moEmployee.getDateLastHire().compareTo(dateLimit) <= 0) {
                    nearestHireLogDate = moEmployee.getDateLastHire();
                }
            }
            else {
                if (moEmployee.getDateLastDismiss_n() != null && moEmployee.getDateLastDismiss_n().compareTo(dateLimit) <= 0) {
                    nearestHireLogDate = moEmployee.getDateLastDismiss_n();
                }
            }
        }

        return nearestHireLogDate;
    }
    
    public SHrsEmployeeDays getEmployeeDays() {
        int daysHiredAnnual = 0;
        int daysIncapacityNotPaidAnnual = 0;
        int daysIncapacityNotPaidPayroll = 0;
        int daysNotWorkedPaid = 0;
        int daysNotWorkedNotPaid = 0;
        int daysNotWorked_r = 0;
        int daysEmployeeInactive = 0;
        double factorCalendar = 0;
        int daysHiredPayroll = 0;
        int receiptDays = 0;
        int workingDays = 0;
        int businessDays = 0;
        double daysWorked = 0;
        double daysToBePaid_r = 0;
        double factorDaysPaid = 0;
        double daysPaid = 0;
        Date periodStart = null;
        Date periodEnd = null;
        SHrsEmployeeDays employeeDays = null;
        
        try {
            employeeDays = new SHrsEmployeeDays(mnYear, mnPeriod, mtPeriodStart, mtPeriodEnd);
            
            periodStart = SLibTimeUtils.getBeginOfYear(mtPeriodStart);
            periodEnd = SLibTimeUtils.getEndOfYear(mtPeriodEnd).compareTo(mtPeriodEnd) < 0 ? SLibTimeUtils.getEndOfYear(mtPeriodEnd) : mtPeriodEnd;

            // Obtain status active for employee date start year:
            // Obtain employee active days accumulated in year: (edc)

            daysHiredAnnual = mnDaysHiredAnnual;

            // Obtain employee no paid sick days accumulated in year: (einc)

            for (SDbAbsenceConsumption absenceConsumption : maAbsencesAbsenceConsumptions) {
                if (SLibTimeUtils.isBelongingToPeriod(absenceConsumption.getDateStart(), periodStart, periodEnd) && 
                        absenceConsumption.getAbsence().getFkAbsenceClassId() == SModSysConsts.HRSU_CL_ABS_DIS && !absenceConsumption.getAbsence().IsAuxAbsencePayable()) {
                    daysIncapacityNotPaidAnnual += absenceConsumption.getEffectiveDays();
                }
            }

            /* Obtain employee no paid sick days in payroll (ninc),
             * Obtain employee absence days payable (ndir)
             * Obtain employee absence days not payable (ndinr)
             * Obtain employee absence days (ndi)
             */

            for (SDbAbsenceConsumption absenceConsumption : moHrsPayrollReceipt.getAbsenceConsumptions()) {
                if (absenceConsumption.getAbsence().IsAuxAbsencePayable()) {
                    daysNotWorkedPaid += absenceConsumption.getEffectiveDays();
                }
                else {
                    daysNotWorkedNotPaid += absenceConsumption.getEffectiveDays();
                    
                    if (absenceConsumption.getAbsence().getFkAbsenceClassId() == SModSysConsts.HRSU_CL_ABS_DIS) {
                        daysIncapacityNotPaidPayroll += absenceConsumption.getEffectiveDays();
                    }
                }
            }

            daysNotWorked_r = daysNotWorkedPaid + daysNotWorkedNotPaid;

            factorCalendar = moHrsPayrollReceipt.getHrsPayroll().getPayroll().getCalendarDays_r() == 0 ? 0 : (double) moHrsPayrollReceipt.getHrsPayroll().getPayroll().getReceiptDays() / (double) moHrsPayrollReceipt.getHrsPayroll().getPayroll().getCalendarDays_r(); // Obtain adjustment factor calendar days (fc)

            // Obtain status active for employee date start payroll:
            // Obtain employee active days: (ndc)
            
            daysHiredPayroll = mnDaysHiredPayroll;
            
            daysEmployeeInactive = moHrsPayrollReceipt.getHrsPayroll().getPayroll().getReceiptDays() - daysHiredPayroll;

            receiptDays = daysHiredPayroll; // Obtain employee payroll days (dne)

            // Obtain employee working days (dle):
            
            businessDays = mnBusinessDays;
            
            if (moHrsPayrollReceipt.getHrsPayroll().getPayroll().getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_WEE) {
                workingDays = moHrsPayrollReceipt.getHrsPayroll().getPayroll().getWorkingDays() - daysEmployeeInactive;;
            }
            else {
                workingDays = daysHiredPayroll;
            }

            daysWorked = workingDays - daysNotWorkedPaid - daysNotWorkedNotPaid; // Obtain employee worked days (dl)

            daysToBePaid_r = (daysWorked + daysNotWorkedPaid) * factorCalendar; // Obtain employee to be paid days (dxp)

            factorDaysPaid = moHrsPayrollReceipt.getHrsPayroll().getFactorDaysPaid(); // Obtain adjustment factor days (fp)

            daysPaid = daysToBePaid_r * factorDaysPaid;

            employeeDays.setFactorCalendar(factorCalendar);
            employeeDays.setFactorDaysPaid(factorDaysPaid);
            employeeDays.setReceiptDays(receiptDays);
            employeeDays.setWorkingDays(workingDays);
            employeeDays.setBusinessDays(businessDays);
            employeeDays.setDaysWorked(workingDays - daysNotWorked_r);
            employeeDays.setDaysHiredPayroll(daysHiredPayroll);
            employeeDays.setDaysHiredAnnual(daysHiredAnnual);
            employeeDays.setDaysIncapacityNotPaidPayroll(daysIncapacityNotPaidPayroll);
            employeeDays.setDaysIncapacityNotPaidAnnual(daysIncapacityNotPaidAnnual);
            employeeDays.setDaysNotWorkedPaid(daysNotWorkedPaid);
            employeeDays.setDaysNotWorkedNotPaid(daysNotWorkedNotPaid);
            employeeDays.setDaysNotWorked_r(daysNotWorked_r);
            employeeDays.setDaysToBePaid_r(daysToBePaid_r);
            employeeDays.setDaysPaid(daysPaid);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }

        return employeeDays;
    }
}

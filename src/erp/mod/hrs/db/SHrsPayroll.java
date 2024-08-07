/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.utils.SAnniversary;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;

/**
 * Las instancias de esta clase permiten la emisión de una nómina en específico.
 *
 * @author Juan Barajas, Néstor Ávalos, Edwin Carmona, Sergio Flores
 */
public class SHrsPayroll {
    
    /** Configuration of Human Resources Module. */
    protected SDbConfig moModuleConfig;
    protected SDbWorkingDaySettings moWorkingDaySettings;
    protected SDbPayroll moPayroll;
    protected SHrsPayrollDataProvider moHrsPayrollDataProvider;
    
    /** Employment subsidy is applicable for current payroll. */
    protected boolean mbEmploymentSubsidyApplicable;
    /** Employment subsidy is affecting the whole year, from January, 1st. */
    protected boolean mbEmploymentSubsidyAffectingWholeYear;
    
    protected ArrayList<SDbLoanTypeAdjustment> maLoanTypeAdjustments;
    protected ArrayList<SDbUma> maUmas;
    protected ArrayList<SDbUmi> maUmis;
    protected ArrayList<SDbHoliday> maHolidays;
    protected ArrayList<SDbTaxTable> maTaxTables;
    protected ArrayList<SDbTaxSubsidyTable> maTaxSubsidyTables;
    protected ArrayList<SDbEmploymentSubsidy> maEmploymentSubsidies;
    protected ArrayList<SDbSsContributionTable> maSsContributionTables;
    protected ArrayList<SDbMwzTypeWage> maMwzTypeWages;
    protected ArrayList<SDbEarning> maEarnigs;
    protected ArrayList<SDbDeduction> maDeductions;
    protected ArrayList<SDbAutomaticEarning> maAutomaticEarnings;
    protected ArrayList<SDbAutomaticDeduction> maAutomaticDeductions;
    protected ArrayList<SDbEmployee> maEmployees;
    protected ArrayList<SHrsReceipt> maHrsReceipts;
    protected HashMap<Integer, String> moEarningComputationTypesMap;
    protected HashMap<Integer, String> moDeductionComputationTypesMap;
    
    public SHrsPayroll(final SDbConfig moduleConfig, final SDbWorkingDaySettings workingDaySettings, final SDbPayroll payroll, final SHrsPayrollDataProvider hrsPayrollDataProvider) {
        moModuleConfig = moduleConfig;
        moWorkingDaySettings = workingDaySettings;
        moPayroll = payroll;
        moHrsPayrollDataProvider = hrsPayrollDataProvider;
        
        clearEmploymentSubsidyApplicability();

        maLoanTypeAdjustments = new ArrayList<>();
        maUmas = new ArrayList<>();
        maUmis = new ArrayList<>();
        maHolidays = new ArrayList<>();
        maTaxTables = new ArrayList<>();
        maTaxSubsidyTables = new ArrayList<>();
        maEmploymentSubsidies = new ArrayList<>();
        maSsContributionTables = new ArrayList<>();
        maMwzTypeWages = new ArrayList<>();
        maEarnigs = new ArrayList<>();
        maDeductions = new ArrayList<>();
        maAutomaticEarnings = new ArrayList<>();
        maAutomaticDeductions = new ArrayList<>();
        maEmployees = new ArrayList<>();
        maHrsReceipts = new ArrayList<>();
        moEarningComputationTypesMap = new HashMap<>();
        moDeductionComputationTypesMap = new HashMap<>();
    }

    /*
     * Private methods:
     */

    private ArrayList<SDbAutomaticEarning> getAutomaticEarnings(final int employeeId, final int paysheetType, final Date dateStart, final Date dateEnd) {
        ArrayList<SDbAutomaticEarning> automaticEarnings = new ArrayList<>();

        for (SDbAutomaticEarning automaticEarning : maAutomaticEarnings) {
            if ((automaticEarning.getFkEmployeeId_n() == employeeId || automaticEarning.getFkEmployeeId_n() == 0) && automaticEarning.getFkPaysheetTypeId() == paysheetType &&
                    SLibTimeUtils.isBelongingToPeriod(automaticEarning.getDateStart(), automaticEarning.getDateEnd_n(), dateStart, dateEnd)) {
                automaticEarnings.add(automaticEarning);
            }
        }

        return automaticEarnings;
    }

    private ArrayList<SDbAutomaticDeduction> getAutomaticDeductions(final int employeeId, final int paysheetType, final Date dateStart, final Date dateEnd) {
        ArrayList<SDbAutomaticDeduction> aAutomaticDeductions = new ArrayList<>();

        for (SDbAutomaticDeduction automaticDeduction : maAutomaticDeductions) {
            if ((automaticDeduction.getFkEmployeeId_n() == employeeId || automaticDeduction.getFkEmployeeId_n() == 0) && automaticDeduction.getFkPaysheetTypeId() == paysheetType &&
                    SLibTimeUtils.isBelongingToPeriod(automaticDeduction.getDateStart(), automaticDeduction.getDateEnd_n(), dateStart, dateEnd)) {
                aAutomaticDeductions.add(automaticDeduction);
            }
        }

        return aAutomaticDeductions;
    }

    /*
     * Public methods:
     */

    public void setPayroll(SDbPayroll payroll) { moPayroll = payroll; }
    
    public SDbConfig getModuleConfig() { return moModuleConfig; }
    public SDbWorkingDaySettings getWorkingDaySettings() { return moWorkingDaySettings; }
    public SDbPayroll getPayroll() { return moPayroll; }
    public SHrsPayrollDataProvider getHrsPayrollDataProvider() { return moHrsPayrollDataProvider; }

    public boolean isEmploymentSubsidyApplicable() { return mbEmploymentSubsidyApplicable; }
    public boolean isEmploymentSubsidyAffectingWholeYear() { return mbEmploymentSubsidyAffectingWholeYear; }
    
    public ArrayList<SDbLoanTypeAdjustment> getLoanTypeAdjustments() { return maLoanTypeAdjustments; }
    public ArrayList<SDbUma> getUmas() { return maUmas; }
    public ArrayList<SDbUmi> getUmis() { return maUmis; }
    public ArrayList<SDbHoliday> getHolidays() { return maHolidays; }
    public ArrayList<SDbTaxTable> getTaxTables() { return maTaxTables; }
    public ArrayList<SDbTaxSubsidyTable> getTaxSubsidyTables() { return maTaxSubsidyTables; }
    public ArrayList<SDbEmploymentSubsidy> getEmploymentSubsidies() {return  maEmploymentSubsidies; }
    public ArrayList<SDbSsContributionTable> getSsContributionTables() { return maSsContributionTables; }
    public ArrayList<SDbMwzTypeWage> getMwzTypeWages() { return maMwzTypeWages; }
    public ArrayList<SDbEarning> getEarnings() { return maEarnigs; }
    public ArrayList<SDbDeduction> getDeductions() { return maDeductions; }
    public ArrayList<SDbAutomaticEarning> getAutomaticEarnings() { return maAutomaticEarnings; }
    public ArrayList<SDbAutomaticDeduction> getAutomaticDeductions() { return maAutomaticDeductions; }
    public ArrayList<SDbEmployee> getEmployees() { return maEmployees; }
    public ArrayList<SHrsReceipt> getHrsReceipts() { return maHrsReceipts; }
    public HashMap<Integer, String> getEarningComputationTypesMap() { return moEarningComputationTypesMap; }
    public HashMap<Integer, String> getDeductionComputationTypesMap() { return moDeductionComputationTypesMap; }
    
    /** Check if payroll belongs to the year of transtition of style of subsidy (i.e., 2024)
     * @param applicable Is employment subsidy applicable.
     * @param affectingWholeYear Is employment subsidy affecting the whole year.
     */
    public static boolean isTransitionYearForTaxSubsidy(final boolean applicable, final boolean affectingWholeYear) { return applicable && !affectingWholeYear; }
    
    /** Check if payroll belongs to the year of transtition of style of subsidy (i.e., 2024) */
    public boolean isTransitionYearForTaxSubsidy() { return isTransitionYearForTaxSubsidy(mbEmploymentSubsidyApplicable, mbEmploymentSubsidyAffectingWholeYear); }
    
    public double getLoanTypeMonthlyAdjustment(final Date date, final int loanType) {
        double monthlyAdjustment = 0;
        ArrayList<SDbLoanTypeAdjustment> matchingAdjustments = new ArrayList<>();
        
        // first, filter only matching adjustments:

        for (SDbLoanTypeAdjustment adjustment : maLoanTypeAdjustments) {
            if (adjustment.getPkLoanTypeId() == loanType && !date.before(adjustment.getDateStart())) {
                matchingAdjustments.add(adjustment);
            }
        }
        
        // find the more recent matching adjustment, to take from it the requested monthly adjustment:
        
        SDbLoanTypeAdjustment moreRecentAdjustment = null;
        
        for (SDbLoanTypeAdjustment adjustment : matchingAdjustments) {
            if (moreRecentAdjustment == null) {
                moreRecentAdjustment = adjustment;
            }
            else if (adjustment.getDateStart().compareTo(moreRecentAdjustment.getDateStart()) >= 0) {
                moreRecentAdjustment = adjustment;
            }
        }
        
        if (moreRecentAdjustment != null) {
            monthlyAdjustment = moreRecentAdjustment.getAdjustment();
        }

        return monthlyAdjustment;
    }
    
    /**
     * Obtain amount UMA most appropriate for date indicated.
     * @param date Date for required UMA.
     * @return 
     */
    public double getUma(final Date date) {
        double amount = 0;

        for (SDbUma uma : maUmas) {
            if (!date.before(uma.getDateStart())) {
                amount = uma.getAmount();
                break;
            }
        }

        return amount;
    }
    
    /**
     * Obtain amount UMI most appropriate for date indicated.
     * @param date Date for required UMI.
     * @return 
     */
    public double getUmi(final Date date) {
        double amount = 0;

        for (SDbUmi umi : maUmis) {
            if (!date.before(umi.getDateStart())) {
                amount = umi.getAmount();
                break;
            }
        }

        return amount;
    }
    
    /**
     * Checks if given date is a holiday.
     * @param date
     * @return <code>true</code> if given date is a holiday.
     */
    public boolean isHoliday(final Date date) {
        boolean isHolyday = false;

        for (SDbHoliday holiday : maHolidays) {
            if (SLibTimeUtils.isSameDate(holiday.getDate(), date)) {
                isHolyday = true;
                break;
            }
        }

        return isHolyday;
    }
    
    /**
     * Checks if given date is a non-working day: non-working by default according to applying working day settings, or a holiday.
     * @param date
     * @return <code>true</code> if given date is a non-working day.
     */
    public boolean isNonWorkingDay(final Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return !moWorkingDaySettings.isWorkingDay(calendar.get(Calendar.DAY_OF_WEEK)) || isHoliday(date);
    }

    public SDbTaxTable getTaxTable(final int tableId) {
        SDbTaxTable taxTable = null;

        for (SDbTaxTable table : maTaxTables) {
            if (table.getPkTaxTableId() == tableId) {
                taxTable = table;
                break;
            }
        }

        return taxTable;
    }

    public SDbTaxSubsidyTable getTaxSubsidyTable(final int tableId) {
        SDbTaxSubsidyTable taxSubsidyTable = null;

        for (SDbTaxSubsidyTable table : maTaxSubsidyTables) {
            if (table.getPkTaxSubsidyTableId() == tableId) {
                taxSubsidyTable = table;
                break;
            }
        }

        return taxSubsidyTable;
    }

    public SDbEmploymentSubsidy getEmploymentSubsidy(final int configId) {
        SDbEmploymentSubsidy employmentSubsidy = null;

        for (SDbEmploymentSubsidy config : maEmploymentSubsidies) {
            if (config.getPkEmploymentSubsidyId() == configId) {
                employmentSubsidy = config;
                break;
            }
        }

        return employmentSubsidy;
    }

    public SDbSsContributionTable getSsContributionTable(final int tableId) {
        SDbSsContributionTable ssContributionTable = null;

        for (SDbSsContributionTable table : maSsContributionTables) {
            if (table.getPkSsContributionTableId() == tableId) {
                ssContributionTable = table;
                break;
            }
        }

        return ssContributionTable;
    }

    public SDbSsContributionTable getSsContributionTableByDate(final Date date) {
        SDbSsContributionTable ssContributionTable = null;

        for (SDbSsContributionTable table : maSsContributionTables) {
            if (!date.before(table.getDateStart())) {
                ssContributionTable = table;
                break;
            }
        }

        return ssContributionTable;
    }
    
    public SDbMwzTypeWage getMwzTypeWage(final int mwzType, final int wageId) {
        SDbMwzTypeWage mwzTypeWage = null;

        for (SDbMwzTypeWage wage : maMwzTypeWages) {
            if (wage.getPkMwzTypeId() == mwzType && wage.getPkWageId() == wageId) {
                mwzTypeWage = wage;
                break;
            }
        }

        return mwzTypeWage;
    }

    public SDbMwzTypeWage getMwzTypeWageByDate(final int mwzType, final Date date) {
        SDbMwzTypeWage oMwzTypeWage = null;

        for (SDbMwzTypeWage mwzTypeWage : maMwzTypeWages) {
            if (mwzTypeWage.getPkMwzTypeId() == mwzType && !date.before(mwzTypeWage.getDateStart())) {
                oMwzTypeWage = mwzTypeWage;
                break;
            }
        }

        return oMwzTypeWage;
    }

    /**
     * Gets the requested earning by ID.
     * @param earningId
     * @return 
     */
    public SDbEarning getEarning(final int earningId) {
        SDbEarning earning = null;

        for (SDbEarning entry : maEarnigs) {
            if (entry.getPkEarningId() == earningId) {
                earning = entry;
                break;
            }
        }

        return earning;
    }

    /**
     * Gets the requested first-found earning by earning type.
     * @param earningType
     * @return 
     */
    public SDbEarning getEarningByType(final int earningType) {
        SDbEarning earning = null;

        for (SDbEarning entry : maEarnigs) {
            if (entry.getFkEarningTypeId() == earningType) {
                earning = entry;
                break;
            }
        }

        return earning;
    }

    /**
     * Gets the requested deduction by ID.
     * @param deductionId
     * @return 
     */
    public SDbDeduction getDeduction(final int deductionId) {
        SDbDeduction deduction = null;

        for (SDbDeduction entry : maDeductions) {
            if (entry.getPkDeductionId() == deductionId) {
                deduction = entry;
                break;
            }
        }

        return deduction;
    }
    
    /**
     * Gets the requested first-found deduction by deduction type.
     * @param deductionType
     * @return 
     */
    public SDbDeduction getDeductionByType(final int deductionType) {
        SDbDeduction deduction = null;

        for (SDbDeduction entry : maDeductions) {
            if (entry.getFkDeductionTypeId() == deductionType) {
                deduction = entry;
                break;
            }
        }

        return deduction;
    }

    public SDbEmployee getEmployee(final int employeeId) {
        SDbEmployee employee = null;

        for (SDbEmployee entry : maEmployees) {
            if (entry.getPkEmployeeId() == employeeId) {
                employee = entry;
                break;
            }
        }

        return employee;
    }
    
    private void clearEmploymentSubsidyApplicability() {
        mbEmploymentSubsidyApplicable = false;
        mbEmploymentSubsidyAffectingWholeYear = false;
    }
    
    /**
     * Assess employment subsidy applicability to given period.
     * @param dateStart Start of period.
     * @param dateEnd End of period.
     * @param employmentSubsidies Array list of all employment subsidies.
     * @return Array of booleans: index 0 = Is subsidy <strong>applying</strong> for given period; index 1 = Is subsidy <strong>affecting the whole year</strong> of given period.
     */
    public static boolean[] assessEmploymentSubsidyApplicability(final Date dateStart, final Date dateEnd, final ArrayList<SDbEmploymentSubsidy> employmentSubsidies) {
        boolean applicable = false;
        boolean affectingWholeYear = false;
        
        if (employmentSubsidies != null && !employmentSubsidies.isEmpty()) {
            SDbEmploymentSubsidy employmentSubsidy = null;
            Date payrollEndOfYear = SLibTimeUtils.getEndOfYear(dateStart);
            Date lookupDate = dateEnd.before(payrollEndOfYear) ? dateEnd : payrollEndOfYear;

            for (SDbEmploymentSubsidy config : employmentSubsidies) {
                if (!lookupDate.before(config.getDateStart())) {
                    employmentSubsidy = config;
                    break;
                }
            }

            if (employmentSubsidy != null) { // is there an applicable registry?
                applicable = true;
                
                Date payrollStartOfYear = SLibTimeUtils.getBeginOfYear(dateStart);
                affectingWholeYear = !payrollStartOfYear.before(employmentSubsidies.get(0).getDateStart()); // check against fisrt registry
            }
        }
        
        return new boolean[] { applicable, affectingWholeYear };
    }

    public void assessEmploymentSubsidyApplicability() {
        clearEmploymentSubsidyApplicability();
        
        if (moPayroll != null) {
            boolean[] assessment = assessEmploymentSubsidyApplicability(moPayroll.getDateStart(), moPayroll.getDateEnd(), maEmploymentSubsidies);

            mbEmploymentSubsidyApplicable = assessment[0];
            mbEmploymentSubsidyAffectingWholeYear = assessment[1];
        }
    }

    public SDbPayrollReceiptEarning createPayrollReceiptEarning(final SHrsReceipt hrsReceipt, final SDbEarning earning, final SHrsBenefit hrsBenefit, 
            final double unitsAlleged, final double amountUnitAlleged, final boolean isAutomatic, final int loanEmployeeId_n, final int loanLoanId_n, final int moveId) {
        double amountUnit = 0;
        
        switch (earning.getFkEarningComputationTypeId()) {
            case SModSysConsts.HRSS_TP_EAR_COMP_AMT:
                amountUnit = amountUnitAlleged;
                break;
            case SModSysConsts.HRSS_TP_EAR_COMP_DAYS:
                amountUnit = hrsReceipt.getPayrollReceipt().getPaymentDaily();
                break;
            case SModSysConsts.HRSS_TP_EAR_COMP_HRS:
                amountUnit = hrsReceipt.getPayrollReceipt().getPaymentHourly();
                break;
            case SModSysConsts.HRSS_TP_EAR_COMP_PCT_DAY:
                amountUnit = hrsReceipt.getPayrollReceipt().getPaymentDaily() * earning.getPayPercentage();
                break;
            case SModSysConsts.HRSS_TP_EAR_COMP_PCT_HR:
                amountUnit = hrsReceipt.getPayrollReceipt().getPaymentHourly() * earning.getPayPercentage();
                break;
            case SModSysConsts.HRSS_TP_EAR_COMP_PCT_INCOME:
                amountUnit = hrsReceipt.getTotalEarningsDependentsDaysWorked();
                break;
            default:
        }
        
        double units = earning.computeEarningUnits(unitsAlleged, moPayroll);
        double amount = earning.computeEarningAmount(units, amountUnit);
                
        SDbPayrollReceiptEarning payrollReceiptEarning = new SDbPayrollReceiptEarning();
        
        payrollReceiptEarning.setPkPayrollId(hrsReceipt.getPayrollReceipt().getPkPayrollId());
        payrollReceiptEarning.setPkEmployeeId(hrsReceipt.getHrsEmployee().getEmployee().getPkEmployeeId());
        payrollReceiptEarning.setPkMoveId(moveId);
        payrollReceiptEarning.setUnitsAlleged(unitsAlleged);
        payrollReceiptEarning.setUnits(units);
        
        payrollReceiptEarning.setAmountUnitary(amountUnit);
        payrollReceiptEarning.setAmountSystem_r(amount);
        payrollReceiptEarning.setAmount_r(amount);
        //payrollReceiptEarning.setAmountExempt(...);
        //payrollReceiptEarning.setAmountTaxable(...);
        //payrollReceiptEarning.setAuxiliarAmount(...);
        
        if (hrsBenefit == null) {
            payrollReceiptEarning.setFactorAmount(earning.getUnitsFactor());
            //...
            payrollReceiptEarning.setBenefitsYear(0);
            payrollReceiptEarning.setBenefitsAnniversary(0);
            //...
            payrollReceiptEarning.setFkBenefitTypeId(earning.getFkBenefitTypeId());
        }
        else {
            payrollReceiptEarning.setFactorAmount(hrsBenefit.getBenefitBonusPct());
            //...
            payrollReceiptEarning.setBenefitsYear(hrsBenefit.getBenefitYear());
            payrollReceiptEarning.setBenefitsAnniversary(hrsBenefit.getBenefitAnniversary());
            //...
            payrollReceiptEarning.setFkBenefitTypeId(hrsBenefit.getBenefitType());
        }
        
        payrollReceiptEarning.setAlternativeTaxCalculation(earning.isAlternativeTaxCalculation());
        payrollReceiptEarning.setUserEdited(false);
        payrollReceiptEarning.setAutomatic(isAutomatic);
        /*
        payrollReceiptEarning.setDeleted(...);
        payrollReceiptEarning.setSystem(...);
        */
        payrollReceiptEarning.setFkEarningTypeId(earning.getFkEarningTypeId());
        payrollReceiptEarning.setFkEarningId(earning.getPkEarningId());
        payrollReceiptEarning.setFkBonusId(SModSysConsts.HRSS_BONUS_NA);
        payrollReceiptEarning.setFkLoanEmployeeId_n(loanEmployeeId_n);
        payrollReceiptEarning.setFkLoanLoanId_n(loanLoanId_n);
        payrollReceiptEarning.setFkLoanTypeId_n(loanEmployeeId_n != 0 && loanLoanId_n != 0 ? earning.getFkLoanTypeId() : 0);
        payrollReceiptEarning.setFkOtherPaymentTypeId(earning.getFkOtherPaymentTypeId());
        /*
        payrollReceiptEarning.setFkUserInsertId(...);
        payrollReceiptEarning.setFkUserUpdateId(...);
        payrollReceiptEarning.setTsUserInsert(...);
        payrollReceiptEarning.setTsUserUpdate(...);
        */

        return payrollReceiptEarning;
    }

    public SDbPayrollReceiptDeduction createPayrollReceiptDeduction(final SHrsReceipt hrsReceipt, final SDbDeduction deduction, 
            final double unitsAlleged, final double amountUnitAlleged, final boolean isAutomatic, final int loanEmployeeId_n, final int loanLoanId_n, final int moveId) {
        double units = 0;
        double amountUnit = 0;
        
        switch (deduction.getFkDeductionComputationTypeId()) {
            case SModSysConsts.HRSS_TP_DED_COMP_AMT:
                units = 1;
                amountUnit = amountUnitAlleged;
                break;
            case SModSysConsts.HRSS_TP_DED_COMP_PCT_INCOME:
                units = unitsAlleged;
                amountUnit = hrsReceipt.getTotalEarningsDependentsDaysWorked();
                break;
            default:
        }
        
        double amount = SLibUtils.roundAmount(amountUnit * units);
        
        SDbPayrollReceiptDeduction payrollReceiptDeduction = new SDbPayrollReceiptDeduction();

        payrollReceiptDeduction.setPkPayrollId(hrsReceipt.getPayrollReceipt().getPkPayrollId());
        payrollReceiptDeduction.setPkEmployeeId(hrsReceipt.getHrsEmployee().getEmployee().getPkEmployeeId());
        payrollReceiptDeduction.setPkMoveId(moveId);
        payrollReceiptDeduction.setUnitsAlleged(unitsAlleged);
        payrollReceiptDeduction.setUnits(units);
        payrollReceiptDeduction.setFactorAmount(1);
        payrollReceiptDeduction.setAmountUnitary(amountUnit);
        payrollReceiptDeduction.setAmountSystem_r(amount);
        payrollReceiptDeduction.setAmount_r(amount);
        /*
        payrollReceiptDeduction.setBenefitsYear(...);
        payrollReceiptDeduction.setBenefitsAnniversary(...);
        payrollReceiptDeduction.setUserEdited(...);
        */
        payrollReceiptDeduction.setAutomatic(isAutomatic);
        /*
        payrollReceiptDeduction.setDeleted(...);
        payrollReceiptDeduction.setSystem(...);
        */
        payrollReceiptDeduction.setFkDeductionTypeId(deduction.getFkDeductionTypeId());
        payrollReceiptDeduction.setFkDeductionId(deduction.getPkDeductionId());
        payrollReceiptDeduction.setFkBonusId(SModSysConsts.HRSS_BONUS_NA);
        payrollReceiptDeduction.setFkBenefitTypeId(deduction.getFkBenefitTypeId());
        payrollReceiptDeduction.setFkLoanEmployeeId_n(loanEmployeeId_n);
        payrollReceiptDeduction.setFkLoanLoanId_n(loanLoanId_n);
        payrollReceiptDeduction.setFkLoanTypeId_n(loanEmployeeId_n != 0 && loanLoanId_n != 0 ? deduction.getFkLoanTypeId() : 0);
        payrollReceiptDeduction.setFkBonusId(1);
        /*
        payrollReceiptDeduction.setFkUserInsertId(...);
        payrollReceiptDeduction.setFkUserUpdateId(...);
        payrollReceiptDeduction.setTsUserInsert(...);
        payrollReceiptDeduction.setTsUserUpdate(...);
        */

        return payrollReceiptDeduction;
    }

    public ArrayList<SHrsReceiptEarning> createHrsReceiptEarningAbsences(final SHrsReceipt hrsReceipt) throws Exception {
        int moveId = 0;
        ArrayList<SHrsReceiptEarning> hrsReceiptEarnings = new ArrayList<>();
        
        for (SDbAbsenceConsumption absenceConsumption : hrsReceipt.getAbsenceConsumptions()) {
            if (absenceConsumption.getParentAbsence().isXtaAbsenceTypePayable()) {
                SDbEarning earningAbsence = null;
                
                if (absenceConsumption.getParentAbsence().isVacation()) {
                    // corresponding earning should be set in configuration of module:
                    earningAbsence = getEarning(moModuleConfig.getFkEarningVacationId_n());
                }
                else {
                    // corresponding earning should be set in earnings catalog itself:
                    for (SDbEarning earning : maEarnigs) {
                        if (SLibUtils.compareKeys(earning.getAbsenceTypeKey(), absenceConsumption.getParentAbsence().getAbsenceTypeKey())) {
                            earningAbsence = earning;
                            break;
                        }
                    }
                }
                
                if (earningAbsence == null) {
                    throw new Exception("No se pudo determinar cuál es la percepción para "
                            + "'" + moHrsPayrollDataProvider.getSession().readField(SModConsts.HRSU_TP_ABS, absenceConsumption.getParentAbsence().getAbsenceTypeKey(), SDbRegistry.FIELD_NAME) + "' "
                            + (absenceConsumption.getParentAbsence().isVacation() ? "en la configuración del módulo" : "en el catálogo de percepciones") + ".");
                }
                else {
                    SHrsBenefit hrsBenefit = null;
                    
                    if (earningAbsence.isBenefit()) {
                        hrsBenefit = new SHrsBenefit(earningAbsence.getFkBenefitTypeId(), absenceConsumption.getParentAbsence().getBenefitsAnniversary(), absenceConsumption.getParentAbsence().getBenefitsYear());
                    }
                    
                    SDbPayrollReceiptEarning payrollReceiptEarning = createPayrollReceiptEarning(
                            hrsReceipt, earningAbsence, hrsBenefit, 
                            absenceConsumption.getEffectiveDays(), 0, true, 
                            0, 0, ++moveId);

                    payrollReceiptEarning.setSystem(true); // key setting in absence earnings control, preserve it!

                    SHrsReceiptEarning hrsReceiptEarning = new SHrsReceiptEarning();
                    hrsReceiptEarning.setHrsReceipt(hrsReceipt);
                    hrsReceiptEarning.setEarning(earningAbsence);
                    hrsReceiptEarning.setPayrollReceiptEarning(payrollReceiptEarning);

                    hrsReceiptEarnings.add(hrsReceiptEarning);
                }
            }
        }
        
        return hrsReceiptEarnings;
    }

    public ArrayList<SHrsReceiptEarning> createHrsReceiptEarnings(final SHrsReceipt hrsReceipt, final Date dateStart, final Date dateEnd) throws Exception {
        int moveId = 0;
        SDbEmployee employee = hrsReceipt.getHrsEmployee().getEmployee();
        ArrayList<SHrsReceiptEarning> hrsReceiptEarnings = new ArrayList<>();
        
        // add automatic earnings:

        HashSet<Integer> processedEarnings = new HashSet<>();
        ArrayList<SDbAutomaticEarning> automaticEarnings = getAutomaticEarnings(employee.getPkEmployeeId(), moPayroll.getFkPaysheetTypeId(), dateStart, dateEnd);
        
        for (SDbAutomaticEarning automaticEarning : automaticEarnings) {
            SDbEarning earning = getEarning(automaticEarning.getPkEarningId());
            
            if (earning != null && (earning.isLoan() || !processedEarnings.contains(earning.getPkEarningId()))) { // loan earnings only at employee level
                SDbPayrollReceiptEarning payrollReceiptEarning = createPayrollReceiptEarning(
                        hrsReceipt, earning, null, 
                        automaticEarning.getUnits(), automaticEarning.getAmountUnitary(), true, 
                        automaticEarning.getFkLoanEmployeeId_n(), automaticEarning.getFkLoanLoanId_n(), ++moveId);

                SHrsReceiptEarning hrsReceiptEarning = new SHrsReceiptEarning();
                hrsReceiptEarning.setHrsReceipt(hrsReceipt);
                hrsReceiptEarning.setEarning(earning);
                hrsReceiptEarning.setPayrollReceiptEarning(payrollReceiptEarning);

                hrsReceiptEarnings.add(hrsReceiptEarning);
                processedEarnings.add(earning.getPkEarningId()); // prevent from adding same earning twice (except for loan ones)
            }
        }
        
        SHrsEmployeeDays hrsEmployeeDays = hrsReceipt.getHrsEmployee().createEmployeeDays();
        
        if (moPayroll.isPayrollNormal()) {
            // add wages and salaries:
            
            if (hrsEmployeeDays.getReceiptDaysWorked() > 0) {
                SDbEarning earning = getEarning(moModuleConfig.getFkEarningEarningId_n());

                SDbPayrollReceiptEarning payrollReceiptEarning = createPayrollReceiptEarning(
                        hrsReceipt, earning, null, 
                        hrsEmployeeDays.getReceiptDaysWorked(), 0, true, 
                        0, 0, ++moveId);

                SHrsReceiptEarning hrsReceiptEarning = new SHrsReceiptEarning();
                hrsReceiptEarning.setHrsReceipt(hrsReceipt);
                hrsReceiptEarning.setEarning(earning);
                hrsReceiptEarning.setPayrollReceiptEarning(payrollReceiptEarning);

                hrsReceiptEarnings.add(hrsReceiptEarning);
            }
            
            // add absences, including vacation:
            
            for (SHrsReceiptEarning hrsReceiptEarning : createHrsReceiptEarningAbsences(hrsReceipt)) {
                hrsReceiptEarnings.add(hrsReceiptEarning);
            }
            
            // add automatic vacation bonus, if required:
            
            if (moModuleConfig.isAutoVacationBonus() && SHrsUtils.isAnniversaryBelongingToPeriod(employee.getDateBenefits(), dateStart, dateEnd)) {
                Date dateCutOff;
                
                if (!employee.isActive()) {
                    dateCutOff = employee.getDateLastDismissal_n();

                    if (!SLibTimeUtils.isBelongingToPeriod(dateCutOff, dateStart, dateEnd)) {
                        dateCutOff = dateEnd;
                    }
                }
                else {
                    dateCutOff = dateEnd;
                }
                
                SAnniversary anniversary = employee.createAnniversary(dateCutOff);
                int benefitAnniversary = anniversary.getElapsedYears();
                int benefitYear = employee.getAnniversaryYear(benefitAnniversary);
                SDbEarning earning = getEarningByType(SModSysConsts.HRSS_TP_EAR_VAC_BONUS);
                SHrsBenefitsManager benefitsManager = hrsReceipt.getHrsBenefitsManager();
                SDbEmployeeBenefitTablesAnnum benefitAnnumVacation = benefitsManager.getBenefitAnnum(SModSysConsts.HRSS_TP_BEN_VAC, benefitAnniversary);
                SDbEmployeeBenefitTablesAnnum benefitAnnumVacationBonus = benefitsManager.getBenefitAnnum(SModSysConsts.HRSS_TP_BEN_VAC_BON, benefitAnniversary);
                SHrsBenefit hrsBenefitVacationBonusPaid = benefitsManager.getBenefitPaid(SModSysConsts.HRSS_TP_BEN_VAC_BON, benefitYear, benefitAnniversary);
                
                double benefitDays = benefitAnnumVacation == null ? 0d : benefitAnnumVacation.getBenefitDays();
                double benefitBonusPct = benefitAnnumVacationBonus == null ? 0d : benefitAnnumVacationBonus.getBenefitBonusPct();
                double benefitAmount = hrsReceipt.calculateBenefit(earning, benefitDays, benefitBonusPct);
                double paidDays = 0;
                double paidAmount = 0;
                   
                if (hrsBenefitVacationBonusPaid != null) {
                    paidDays = hrsBenefitVacationBonusPaid.getPaidDays();
                    paidAmount = hrsBenefitVacationBonusPaid.getPaidAmount();
                }
                
                if (benefitDays > paidDays || benefitAmount > paidAmount) {
                    double daysToBePayed;
                    double benefitToBePayed;
                    SHrsBenefit hrsBenefit = new SHrsBenefit(SModSysConsts.HRSS_TP_BEN_VAC_BON, benefitAnniversary, benefitYear);
                    
                    hrsBenefit.setBenefitBonusPct(benefitBonusPct);
                    
                    if (earning.isBasedOnUnits()) {
                        daysToBePayed = SLibUtils.round(benefitDays <= paidDays ? 0d : benefitDays - paidDays, SLibUtils.DecimalFormatPercentage8D.getMaximumFractionDigits());
                        benefitToBePayed = 0;
                    }
                    else {
                        daysToBePayed = 1;
                        benefitToBePayed = SLibUtils.roundAmount(benefitAmount <= paidAmount ? 0d : benefitAmount - paidAmount);
                    }

                    SDbPayrollReceiptEarning payrollReceiptEarning = createPayrollReceiptEarning(
                            hrsReceipt, earning, hrsBenefit, 
                            daysToBePayed, benefitToBePayed, true, 
                            0, 0, ++moveId);

                    SHrsReceiptEarning hrsReceiptEarning = new SHrsReceiptEarning();
                    hrsReceiptEarning.setHrsReceipt(hrsReceipt);
                    hrsReceiptEarning.setEarning(earning);
                    hrsReceiptEarning.setPayrollReceiptEarning(payrollReceiptEarning);

                    hrsReceiptEarnings.add(hrsReceiptEarning);
                }
            }
        }

        return hrsReceiptEarnings;
    }
    
    public ArrayList<SHrsReceiptDeduction> createHrsReceiptDeductions(final SHrsReceipt hrsReceipt, final Date dateStart, final Date dateEnd) throws Exception {
        int moveId = 0;
        SDbEmployee employee = hrsReceipt.getHrsEmployee().getEmployee();
        ArrayList<SHrsReceiptDeduction> hrsReceiptDeductions = new ArrayList<>();

        if (moPayroll.isPayrollNormal()) {
            // Get deductions that are withholding ones (deductions by law):
            
            for (SDbDeduction deduction : maDeductions) {
                if (deduction.isWithholding()) {
                    // check if current deduction matches configuration settings:
                    
                    boolean matchesConfig = false;
                    
                    switch (deduction.getFkDeductionTypeId()) {
                        case SModSysConsts.HRSS_TP_DED_TAX:
                            matchesConfig = deduction.getPkDeductionId() == moModuleConfig.getFkDeductionTaxId_n();
                            break;
                        case SModSysConsts.HRSS_TP_DED_SSC:
                            matchesConfig = deduction.getPkDeductionId() == moModuleConfig.getFkDeductionSsContributionId_n();
                            break;
                        default:
                            // to nothing
                    }
                    
                    if (matchesConfig) {
                        SDbPayrollReceiptDeduction payrollReceiptDeduction = createPayrollReceiptDeduction(
                                hrsReceipt, deduction, 
                                1, 0, true,
                                0, 0, ++moveId);

                        SHrsReceiptDeduction hrsReceiptDeduction = new SHrsReceiptDeduction();
                        hrsReceiptDeduction.setHrsReceipt(hrsReceipt);
                        hrsReceiptDeduction.setDeduction(deduction);
                        hrsReceiptDeduction.setPayrollReceiptDeduction(payrollReceiptDeduction);

                        hrsReceiptDeductions.add(hrsReceiptDeduction);
                    }
                }
            }
        }

        // Get automatic deductions:

        HashSet<Integer> processedDeductions = new HashSet<>();
        ArrayList<SDbAutomaticDeduction> automaticDeductions = getAutomaticDeductions(employee.getPkEmployeeId(), moPayroll.getFkPaysheetTypeId(), dateStart, dateEnd);
        
        for (SDbAutomaticDeduction automaticDeduction : automaticDeductions) {
            SDbDeduction deduction = getDeduction(automaticDeduction.getPkDeductionId());
            
            if (deduction != null && (deduction.isLoan() || !processedDeductions.contains(deduction.getPkDeductionId()))) { // loan deductions only at employee level
                SDbPayrollReceiptDeduction payrollReceiptDeduction = createPayrollReceiptDeduction(
                        hrsReceipt, deduction, 
                        automaticDeduction.getUnits(), automaticDeduction.getAmountUnitary(), true,
                        automaticDeduction.getFkLoanEmployeeId_n(), automaticDeduction.getFkLoanLoanId_n(), ++moveId);

                SHrsReceiptDeduction hrsReceiptDeduction = new SHrsReceiptDeduction();
                hrsReceiptDeduction.setHrsReceipt(hrsReceipt);
                hrsReceiptDeduction.setDeduction(deduction);
                hrsReceiptDeduction.setPayrollReceiptDeduction(payrollReceiptDeduction);

                hrsReceiptDeductions.add(hrsReceiptDeduction);
                processedDeductions.add(deduction.getPkDeductionId()); // prevent from adding same deduction twice (except for loan ones)
            }
        }

        if (moPayroll.isPayrollNormal()) {
            hrsReceipt.computePayrollReceiptDays();

            // Get employee loans:

            for (SDbLoan loan : hrsReceipt.getHrsEmployee().getLoans()) {
                if (SLibTimeUtils.isBelongingToPeriod(loan.getDateStart(), loan.getDateEnd_n() == null ? dateEnd : loan.getDateEnd_n(), dateStart, dateEnd)) {
                    double loanAmount = SHrsUtils.computeLoanAmount(loan, hrsReceipt, null, null);

                    if (loanAmount > 0) {
                        SDbDeduction deductionLoan = null;

                        for (SDbDeduction deduction : maDeductions) {
                            if (deduction.getFkLoanTypeId() == loan.getFkLoanTypeId()) {
                                deductionLoan = deduction;
                                break;
                            }
                        }

                        if (deductionLoan == null) {
                            throw new Exception("No se encontró ninguna deducción para agregar el préstamo '" + loan.composeLoanDescription() + "'.");
                        }
                        else {
                            SDbPayrollReceiptDeduction payrollReceiptDeduction = createPayrollReceiptDeduction(
                                    hrsReceipt, deductionLoan, 
                                    1, loanAmount, true,
                                    loan.getPkEmployeeId(), loan.getPkLoanId(), ++moveId);

                            SHrsReceiptDeduction hrsReceiptDeduction = new SHrsReceiptDeduction();
                            hrsReceiptDeduction.setHrsReceipt(hrsReceipt);
                            hrsReceiptDeduction.setDeduction(deductionLoan);
                            hrsReceiptDeduction.setPayrollReceiptDeduction(payrollReceiptDeduction);

                            hrsReceiptDeductions.add(hrsReceiptDeduction);
                        }
                    }
                }
            }
        }
        
        return hrsReceiptDeductions;
    }
    
    private SDbPayrollReceipt createPayrollReceipt(final SDbEmployee employee, final int recruitmentSchemaType) {
        SDbPayrollReceipt payrollReceipt = new SDbPayrollReceipt();
        
        //payrollReceipt.setPkPayrollId();
        payrollReceipt.setPkEmployeeId(employee.getPkEmployeeId());
        payrollReceipt.setDateBenefits(employee.getDateBenefits());
        payrollReceipt.setDateLastHire(employee.getDateLastHire());
        payrollReceipt.setDateLastDismissal_n(employee.getDateLastDismissal_n());
        payrollReceipt.setSalary(employee.getSalary());
        payrollReceipt.setWage(employee.getWage());
        payrollReceipt.setSalarySscBase(employee.getSalarySscBase());
        payrollReceipt.setWorkingHoursDay(employee.getWorkingHoursDay());
        payrollReceipt.setPaymentDaily(employee.getEffectiveSalary(moPayroll.isFortnightStandard()));
        payrollReceipt.setPaymentHourly(employee.getWorkingHoursDay() > 0 ? payrollReceipt.getPaymentDaily() / employee.getWorkingHoursDay() : 0);
        //payrollReceipt.setFactorCalendar();
        //payrollReceipt.setFactorDaysPaid();
        //payrollReceipt.setReceiptDays();
        //payrollReceipt.setWorkingDays();
        //payrollReceipt.setDaysWorked();
        //payrollReceipt.setDaysHiredPayroll();
        //payrollReceipt.setDaysHiredAnnual();
        //payrollReceipt.setDaysIncapacityNotPaidPayroll();
        //payrollReceipt.setDaysIncapacityNotPaidAnnual();
        //payrollReceipt.setDaysNotWorkedButPaid();
        //payrollReceipt.setDaysNotWorkedNotPaid();
        //payrollReceipt.setDaysNotWorked_r();
        //payrollReceipt.setDaysToBePaid_r();
        //payrollReceipt.setDaysPaid();
        //payrollReceipt.setEarningsExemption_r();
        //payrollReceipt.setEarningsTaxable_r();
        //payrollReceipt.setEarnings_r();
        //payrollReceipt.setDeductions_r();
        //payrollReceipt.setPayment_r();
        //payrollReceipt.setPayrollTaxableDays_r();
        //payrollReceipt.setPayrollFactorTax();
        //payrollReceipt.setPayrollTaxAssessed();
        //payrollReceipt.setPayrollTaxCompensated();
        //payrollReceipt.setPayrollTaxPending_r();
        //payrollReceipt.setPayrollTaxPayed();
        //payrollReceipt.setPayrollTaxSubsidyAssessedGross();
        //payrollReceipt.setPayrollTaxSubsidyAssessed();
        //payrollReceipt.setPayrollTaxSubsidyCompensated();
        //payrollReceipt.setPayrollTaxSubsidyPending_r();
        //payrollReceipt.setPayrollTaxSubsidyPayed();
        //payrollReceipt.setAnnualTaxableDays_r();
        //payrollReceipt.setAnnualFactorTax();
        //payrollReceipt.setAnnualTaxAssessed();
        //payrollReceipt.setAnnualTaxCompensated();
        //payrollReceipt.setAnnualTaxPayed();
        //payrollReceipt.setAnnualTaxSubsidyAssessed();
        //payrollReceipt.setAnnualTaxSubsidyCompensated();
        //payrollReceipt.setAnnualTaxSubsidyPayed();
        payrollReceipt.setActive(employee.isActive());
        //payrollReceipt.setDaysAdjustment();
        //payrollReceipt.setCfdRequired();
        //payrollReceipt.setDeleted();
        payrollReceipt.setFkPaymentTypeId(employee.getFkPaymentTypeId());
        payrollReceipt.setFkEmployeeTypeId(employee.getFkEmployeeTypeId());
        payrollReceipt.setFkMwzTypeId(employee.getFkMwzTypeId());
        payrollReceipt.setFkDepartmentId(employee.getFkDepartmentId());
        payrollReceipt.setFkPositionId(employee.getFkPositionId());
        
        if (recruitmentSchemaType == SModSysConsts.HRSS_TP_REC_SCHE_NA || SHrsCfdUtils.isTypeRecruitmentSchemaForEmployment(recruitmentSchemaType)) {
            payrollReceipt.setFkRecruitmentSchemaTypeId(recruitmentSchemaType == SModSysConsts.HRSS_TP_REC_SCHE_NA ? employee.getEffectiveRecruitmentSchemaTypeId() : recruitmentSchemaType);
            
            payrollReceipt.setFkSalaryTypeId(employee.getFkSalaryTypeId());
            payrollReceipt.setFkWorkerTypeId(employee.getFkWorkerTypeId());
            payrollReceipt.setFkShiftId(employee.getFkShiftId());
            payrollReceipt.setFkContractTypeId(employee.getFkContractTypeId());
            payrollReceipt.setFkPositionRiskTypeId(employee.getFkPositionRiskTypeId());
            payrollReceipt.setFkWorkingDayTypeId(employee.getFkWorkingDayTypeId());
        }
        else {
            payrollReceipt.setFkRecruitmentSchemaTypeId(recruitmentSchemaType);
            
            payrollReceipt.setFkSalaryTypeId(SModSysConsts.HRSS_TP_SAL_FIX);
            payrollReceipt.setFkWorkerTypeId(SModSysConsts.HRSU_TP_WRK_NA);
            payrollReceipt.setFkShiftId(SModSysConsts.HRSU_SHT_NA);
            payrollReceipt.setFkContractTypeId(SModSysConsts.HRSS_TP_CON_OTH);
            payrollReceipt.setFkPositionRiskTypeId(SModSysConsts.HRSS_TP_POS_RISK_NA);
            payrollReceipt.setFkWorkingDayTypeId(SModSysConsts.HRSS_TP_WORK_DAY_NA);
        }
        //payrollReceipt.setFkUserInsertId();
        //payrollReceipt.setFkUserUpdateId();
        //payrollReceipt.setTsUserInsert();
        //payrollReceipt.setTsUserUpdate();
        
        return payrollReceipt;
    }
    
    public ArrayList<SDbAbsenceConsumption> crateAbsenceConsumptions(final SHrsReceipt hrsReceipt) throws Exception {
        ArrayList<SDbAbsenceConsumption> absenceConsumptions = new ArrayList<>();
        
        // prepare for absence consumptions:
        
        double receiptConsumedDays = 0; // consumed days added to this receipt
        Date payrollDateEnd = hrsReceipt.getHrsPayroll().getPayroll().getDateEnd();
        SHrsEmployeeDays hrsEmployeeDays = hrsReceipt.getHrsEmployee().createEmployeeDays(); // calendar and business days available for this receipt

        // go through all absences for current employee to check which ones are elegible to be added to this receipt:
        
        for (SDbAbsence absence : hrsReceipt.getHrsEmployee().getAbsences()) {
            if (!absence.isClosed() && !absence.getDateStart().after(payrollDateEnd)) { // is current absence elegible?
                /*
                 * START of algorithm #ABS001. IMPORTANT: Please sync up any changes to this algorithm in all occurrences in proyect!
                 */

                // estimate absence remaining-days to be consumed:

                int consumedDays = absence.getActualConsumedDays(hrsReceipt.getHrsEmployee());
                int remainingDays = absence.getEffectiveDays() - consumedDays;

                if (remainingDays > 0) {
                    Date lastConsumptionDate = consumedDays == 0 ? null : absence.getActualLastConsumptionDate(hrsReceipt.getHrsEmployee());
                    Date consumptionStart = lastConsumptionDate == null ? absence.getDateStart() : SLibTimeUtils.addDate(lastConsumptionDate, 0, 0, 1);
                    
                    // adjust consumption start-date, if neccesary:
                    
                    if (!absence.consumesCalendarDays() && consumedDays > 0) { // preserve original absence start-date when there aren't previous consumptions
                        while (isNonWorkingDay(consumptionStart)) {
                            consumptionStart = SLibTimeUtils.addDate(consumptionStart, 0, 0, 1); // move to next day
                        }
                    }

                    /*
                     * END of algorithm #ABS001. IMPORTANT: Please sync up any changes to this algorithm in all occurrences in proyect!
                     */
                    
                    // check if consumption does not start after payroll end-date:
                    
                    if (!consumptionStart.after(payrollDateEnd)) {
                        // prepare absence elegible-days to be consumed:

                        int absenceElegibleDays = remainingDays;
                        
                        // check if absence does start after payroll end-date:
                        
                        if (absence.getDateEnd().after(payrollDateEnd)) {
                            // adjust absence elegible-days to be consumed:
                            
                            int consumableDays;
                            
                            if (absence.consumesCalendarDays() || (consumedDays == 0 && isNonWorkingDay(consumptionStart))) {
                                consumableDays = SLibTimeUtils.countPeriodDays(consumptionStart, payrollDateEnd);
                            }
                            else {
                                consumableDays = SHrsUtils.countBusinessDays(consumptionStart, payrollDateEnd, moWorkingDaySettings, maHolidays);
                            }
                            
                            if (absenceElegibleDays > consumableDays) {
                                absenceElegibleDays = consumableDays;
                            }
                        }

                        // prepare consumption effective days:
                        
                        if (absenceElegibleDays > 0) {
                            int consumptionEffectiveDays = absenceElegibleDays;
                            int receiptRemainingDays = (int) ((absence.consumesCalendarDays() ? hrsEmployeeDays.getReceiptDays() : hrsEmployeeDays.getReceiptBusinessDays()) - receiptConsumedDays);

                            if (consumptionEffectiveDays > receiptRemainingDays) {
                                consumptionEffectiveDays = receiptRemainingDays;
                            }

                            if (consumptionEffectiveDays > 0) {
                                // prepare consumption:

                                boolean adjustingConsumptionEnd = true;
                                Date consumptionEnd;
                                
                                // iterate until necessary to set consumption end-date:

                                do {
                                    consumptionEnd = SLibTimeUtils.addDate(consumptionStart, 0, 0, consumptionEffectiveDays - 1);
                                    int consumptionBusinessDays = SHrsUtils.countBusinessDays(consumptionStart, consumptionEnd, moWorkingDaySettings, maHolidays);
                                    
                                    // check very first consumption for absences that consume business days:
                                    
                                    if (!absence.consumesCalendarDays() && consumedDays == 0) {
                                        if (consumptionBusinessDays < consumptionEffectiveDays) {
                                            // grant non-working days at the beginning of consumption period:
                                            
                                            int nonBusinessDaysGranted = 0; // non-business days considered as business days
                                            
                                            for (int day = 0; day < consumptionEffectiveDays; day++) {
                                                Date date = SLibTimeUtils.addDate(consumptionStart, 0, 0, day);

                                                if (isNonWorkingDay(date)) {
                                                    nonBusinessDaysGranted++;
                                                }
                                                else {
                                                    break;
                                                }
                                            }
                                            
                                            consumptionBusinessDays += nonBusinessDaysGranted;
                                        }
                                    }
                                    
                                    // confirm consumption end-date:

                                    if (absence.consumesCalendarDays() || consumptionBusinessDays == consumptionEffectiveDays) {
                                        /* Consumption end-date does not need any adjustment:
                                         * absence consumes calendar days OR
                                         * absences consumes business days and consumption effective-days are equal to consumption period business-days.
                                         */

                                        if (remainingDays - consumptionEffectiveDays == 0) {
                                            // force consumption end-date to be equal to absence end-date when last consumption ocurrs:
                                            consumptionEnd = absence.getDateEnd();
                                        }
                                        
                                        // stop iterations:
                                        adjustingConsumptionEnd = false;
                                    }
                                    else {
                                        /* Consumption end-date does need some adjustments:
                                         * absence consumes business days AND
                                         * consumption effective-days are not equal to consumption period business-days.
                                         */

                                        int businessDaysToPayrollEnd = SHrsUtils.countBusinessDays(consumptionEnd, payrollDateEnd, moWorkingDaySettings, maHolidays);

                                        if (businessDaysToPayrollEnd == 0) {
                                            // split consumption end backwards:

                                            if (SLibTimeUtils.addDate(consumptionEnd, 0, 0, -1).before(absence.getDateStart())) {
                                                adjustingConsumptionEnd = false;
                                            }
                                            else {
                                                consumptionEnd = SLibTimeUtils.addDate(consumptionEnd, 0, 0, -1);
                                                consumptionEffectiveDays--;
                                            }
                                            
                                            // go to next iteration!
                                        }
                                        else {
                                            // split consumptino end forward:

                                            int nonBusinessDaysBurned = 0;
                                            int nonBusinessDaysConsumed = consumptionEffectiveDays - consumptionBusinessDays;

                                            while (nonBusinessDaysBurned < businessDaysToPayrollEnd && nonBusinessDaysBurned < nonBusinessDaysConsumed && !SLibTimeUtils.addDate(consumptionEnd, 0, 0, 1).after(absence.getDateEnd())) {
                                                consumptionEnd = SLibTimeUtils.addDate(consumptionEnd, 0, 0, 1);

                                                if (!isNonWorkingDay(consumptionEnd)) {
                                                    nonBusinessDaysBurned++;
                                                }
                                            }

                                            // stop iterations:
                                            adjustingConsumptionEnd = false;
                                        }
                                    }
                                } while (adjustingConsumptionEnd && consumptionEffectiveDays > 0);
                            
                                if (consumptionEffectiveDays > 0) {
                                    // create consumption:

                                    SDbAbsenceConsumption absenceConsumption = hrsReceipt.createAbsenceConsumption(absence, consumptionStart, consumptionEnd, consumptionEffectiveDays);

                                    if (hrsReceipt.validateAbsenceConsumption(absenceConsumption)) {
                                        // process consumption:

                                        absenceConsumptions.add(absenceConsumption);

                                        receiptConsumedDays += consumptionEffectiveDays;
                                        if (receiptConsumedDays >= hrsEmployeeDays.getReceiptDays()) {
                                            break; // there is no way to add more consumptions!
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return absenceConsumptions;
    }

    /**
     * Create receipt.
     * @param employeeId
     * @param recruitmentSchemaType
     * @return
     * @throws Exception 
     */
    public SHrsReceipt createHrsReceipt(final int employeeId, final int recruitmentSchemaType) throws Exception {
        // create HRS receipt:
        SHrsReceipt hrsReceipt = new SHrsReceipt(this);
        
        // create HRS employee:
        SHrsEmployee hrsEmployee = moHrsPayrollDataProvider.createHrsEmployee(hrsReceipt, employeeId);
        
        // assign HRS employee to HRS receipt:
        hrsReceipt.setHrsEmployee(hrsEmployee);
        
        // asign payroll receipt to HRS receipt:
        SDbPayrollReceipt payrollReceipt = createPayrollReceipt(hrsEmployee.getEmployee(), recruitmentSchemaType);
        hrsReceipt.setPayrollReceipt(payrollReceipt);

        // prepare and compute HRS receipt:
        
        if (moPayroll.isPayrollNormal()) {
            hrsReceipt.getAbsenceConsumptions().addAll(crateAbsenceConsumptions(hrsReceipt));
        }
        
        hrsReceipt.getHrsReceiptEarnings().addAll(createHrsReceiptEarnings(hrsReceipt, moPayroll.getDateStart(), moPayroll.getDateEnd()));
        hrsReceipt.getHrsReceiptDeductions().addAll(createHrsReceiptDeductions(hrsReceipt, moPayroll.getDateStart(), moPayroll.getDateEnd()));

        hrsReceipt.renumberHrsReceiptEarnings();
        hrsReceipt.renumberHrsReceiptDeductions();
        
        hrsReceipt.computeReceipt();

        // add new HRS receipt:
        maHrsReceipts.add(hrsReceipt);

        return hrsReceipt;
    }

    public void replaceHrsReceipt(final SHrsReceipt hrsReceiptNew, final boolean compute) {
        for (int i = 0; i < maHrsReceipts.size(); i++) {
            SHrsReceipt hrsReceipt = maHrsReceipts.get(i);

            if (hrsReceipt.getPayrollReceipt().getPkEmployeeId() == hrsReceiptNew.getPayrollReceipt().getPkEmployeeId()) {
                maHrsReceipts.set(i, hrsReceiptNew);
                break;
            }
        }
        
        if (compute) {
            try {
                hrsReceiptNew.renumberHrsReceiptEarnings();
                hrsReceiptNew.renumberHrsReceiptDeductions();
                hrsReceiptNew.computeReceipt();
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
    }

    public void removeHrsReceipt(final int employeeId) {
        for (int i = 0; i < maHrsReceipts.size(); i++) {
            SHrsReceipt hrsReceipt = maHrsReceipts.get(i);

            if (hrsReceipt.getPayrollReceipt().getPkEmployeeId() == employeeId) {
                maHrsReceipts.remove(i);
                break;
            }
        }
    }

    public void computeReceipts() {
        try {
            for (SHrsReceipt hrsReceipt : maHrsReceipts) {
                hrsReceipt.computeReceipt();
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    public double getTotalEarnings() {
        double total = 0;

        for (SHrsReceipt hrsReceipt : maHrsReceipts) {
            for (SHrsReceiptEarning hrsReceiptEarning : hrsReceipt.getHrsReceiptEarnings()) {
                total = SLibUtils.roundAmount(total + hrsReceiptEarning.getPayrollReceiptEarning().getAmount_r());
            }
        }

        return total;
    }

    public double getTotalDeductions() {
        double total = 0;

        for (SHrsReceipt hrsReceipt : maHrsReceipts) {
            for (SHrsReceiptDeduction hrsReceiptDeduction : hrsReceipt.getHrsReceiptDeductions()) {
                total = SLibUtils.roundAmount(total + hrsReceiptDeduction.getPayrollReceiptDeduction().getAmount_r());
            }
        }

        return total;
    }
}

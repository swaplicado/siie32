/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import cfd.ver3.nom12.DNom12Consts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.utils.SAnniversary;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;

/**
 * Las instancias de esta clase permiten la emisión de un recibo de nómina en específico.
 *
 * @author Néstor Ávalos, Juan Barajas, Sergio Flores
 * 2020-05-10 Sergio Flores: Implementación del subsidio para el empleo vigente por decreto a partir del 1° de mayo de 2024.
 * 2020-08-14 Sergio Flores: Implementación del cálculo de ISR retenido por salarios de forma independiente: sueldos y asimilados.
 * 2019-06-07 Sergio Flores: Implementación compensación Subsidio para el empleo pagado en exceso contra ISR.
 * 2019-01-29 Sergio Flores: Corrección al cálculo de parte exenta en percepciones. Debe estar en en función de UMA desde 2017.
 *  Al implementarse el dato informativo en CFDI de Subsdio para el empleo causado, la conmpensación del pago en exceso dejó de hacerse contra ISR.
 */
public class SHrsReceipt {
    
    private final static int COMP_PAY = 0;
    private final static int COMP_ANN = 1;
    private final static int STYLE_OLD = 0;
    private final static int STYLE_NEW = 1;

    private SHrsPayroll moHrsPayroll;
    private SHrsEmployee moHrsEmployee;
    private SHrsBenefitsManager moHrsBenefitsManager;
    private SDbPayrollReceipt moPayrollReceipt;
    private final ArrayList<SDbAbsenceConsumption> maAbsenceConsumptions;
    private final ArrayList<SHrsReceiptEarning> maHrsReceiptEarnings;
    private final ArrayList<SHrsReceiptDeduction> maHrsReceiptDeductions;
    private final ArrayList<SHrsBenefit> maHrsBenefits;

    public SHrsReceipt(final SHrsPayroll hrsPayroll) {
        moHrsPayroll = hrsPayroll;
        moHrsEmployee = null;
        moHrsBenefitsManager = null;
        moPayrollReceipt = null;
        maAbsenceConsumptions = new ArrayList<>();
        maHrsReceiptEarnings = new ArrayList<>();
        maHrsReceiptDeductions = new ArrayList<>();
        maHrsBenefits = new ArrayList<>();
    }

    /*
     * Private methods
     */

    /**
     * Initialize, if not already instantiated, benefits manager for current employee, if any.
     */
    private void initHrsBenefitsManager() throws Exception {
        if (moHrsBenefitsManager == null && moHrsEmployee != null) {
            moHrsBenefitsManager = new SHrsBenefitsManager(moHrsPayroll.getHrsPayrollDataProvider().getSession(), moHrsEmployee.getEmployee().getPkEmployeeId(), moHrsPayroll.getPayroll().getPkPayrollId());
        }
    }

    /**
     * Compute all earnings of this receipt.
     */
    private void computeEarnings() {
        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            hrsReceiptEarning.computeEarning();
        }
    }

    /**
     * Compute all deductions of this receipt.
     */
    private void computeDeductions() {
        for (SHrsReceiptDeduction hrsReceiptDeduction : maHrsReceiptDeductions) {
            hrsReceiptDeduction.computeDeduction();
        }
    }

    /**
     * Compute the given array of earnings as a whole.
     * @param hrsReceiptEarnings Array of earnings. At least one earning is expected.
     * @throws Exception
     */
    private void computeEarningsExemptionGroup(final ArrayList<SHrsReceiptEarning> hrsReceiptEarnings) throws Exception {
        /*
        EARNING EXEMPTION TYPES IN CONFIGURATION OF EARNINGS:
        
        Earning exemption types for SDbEarning.getFkEarningExemptionTypeId() can be:
            a) NA: Not applicable
            b) PER: Minimum Wage Percentage
            c) MWZ_GBL: Minimum Wage Global (that means "for the whole year"
            d) MWZ_EVT: Minimum Wage Event
            e) MWZ_SEN: Minimum Wage Seniority
        
        Earning exemption types for SDbEarning.getFkEarningExemptionTypeYearId(), identified in GUI as "optional", can be:
            a) NA: Not applicable
            b) MWZ_GBL: Minimum Wage Global (that means "for the whole year")
        */
        
        // get earning related to given group of receipt earnings, consider that all earnings in array are the same:
        SDbEarning earning = hrsReceiptEarnings.get(0).getEarning(); // convenience variable
        
        // check if exemption apply (standard and optional settings):
        boolean applyExemption = earning.getFkEarningExemptionTypeId() != SModSysConsts.HRSS_TP_EAR_EXEM_NA; // variable improves readability!
        boolean applyExemptionMwzGlobal = earning.getFkEarningExemptionTypeId() == SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_GBL; // variable improves readability!
        boolean applyExemptionMwzGlobalYear = earning.getFkEarningExemptionTypeYearId() == SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_GBL; // variable improves readability!
        double exemptionGlobal = 0; // exemption granted in other payrolls in current year
        double exemptionPayroll = 0; // exemption granted in current payroll

        // get accumulated exemption granted in current year if necessary:
        if (applyExemptionMwzGlobal || applyExemptionMwzGlobalYear) {
            SHrsAccumulatedEarning hrsAccumEarning = moHrsEmployee.getHrsAccumulatedEarning(earning.getPkEarningId());
            if (hrsAccumEarning != null) {
                exemptionGlobal = hrsAccumEarning.getExemption();
            }
        }
        
        SDbPayroll payroll = moHrsPayroll.getPayroll(); // convenience variable

        // compute all receipt earnings in given group:
        for (SHrsReceiptEarning hrsReceiptEarning : hrsReceiptEarnings) {
            double exemption = 0;
            SDbPayrollReceiptEarning payrollReceiptEarning = hrsReceiptEarning.getPayrollReceiptEarning();

            // compute exemption:
            if (applyExemption) {
                double exemptionLimit = 0;
                
                switch (earning.getFkEarningExemptionTypeId()) {
                    case SModSysConsts.HRSS_TP_EAR_EXEM_PER: // Percentage
                        // estimate exemption proposed and exemption limit:
                        if (moPayrollReceipt.getEffectiveSalary(payroll.isFortnightStandard()) <= payroll.getMwzWage()) { // salary cannot never be less than minimum wage, but just in case
                            exemption = SLibUtils.roundAmount(earning.getExemptionSalaryEqualsMwzPercentage() * payrollReceiptEarning.getAmount_r());
                            exemptionLimit = SLibUtils.roundAmount(earning.getExemptionSalaryEqualsMwzLimit() * payroll.getUmaAmount()); // formerly minimum wage was used, now UMA
                        }
                        else {
                            exemption = SLibUtils.roundAmount(earning.getExemptionSalaryGreaterMwzPercentage() * payrollReceiptEarning.getAmount_r());
                            exemptionLimit = SLibUtils.roundAmount(earning.getExemptionSalaryGreaterMwzLimit() * payroll.getUmaAmount()); // formerly minimum wage was used, now UMA
                        }
                        break;

                    case SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_GBL: // Minimum Wage Global
                    case SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_EVT: // Minimum Wage Event
                        // estimate exemption proposed and exemption limit:
                        exemption = SLibUtils.roundAmount(earning.getExemptionMwz() * payroll.getUmaAmount()); // formerly minimum wage was used, now UMA
                        exemptionLimit = exemption;
                        break;

                    case SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_SEN: // Minimum Wage Seniority
                        // compute exact seniority:
                        SAnniversary anniversary = moHrsEmployee.getEmployee().createAnniversary(payroll.getDateEnd());

                        // estimate exemption proposed and exemption limit:
                        exemption = SLibUtils.roundAmount(earning.getExemptionMwz() * payroll.getUmaAmount() * anniversary.getElapsedYearsPlusPartForBenefits()); // formerly minimum wage was used, now UMA
                        exemptionLimit = exemption;
                        break;

                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }

                // adjust exemption:
                if (exemption <= 0) {
                    exemption = 0;
                }
                else {
                    if (exemptionLimit > 0 && (exemptionGlobal + exemptionPayroll + exemption) >= exemptionLimit) {
                        exemption = exemptionLimit;
                    }

                    if (exemption > payrollReceiptEarning.getAmount_r()) {
                        exemption = payrollReceiptEarning.getAmount_r();
                    }
                }

                // accumulate payroll exemption:
                exemptionPayroll = SLibUtils.roundAmount(exemptionPayroll + exemption);
            }

            // set earning exemption:
            payrollReceiptEarning.setAmountExempt(exemption);
            payrollReceiptEarning.setAmountTaxable(SLibUtils.roundAmount(payrollReceiptEarning.getAmount_r() - exemption));
        }
    }
    
    /**
     * Compute tax exemption of all earnings of this receipt by processing and grouping them by ID of earning into independent arrays.
     * @throws Exception 
     */
    private void computeEarningsExemption() throws Exception {
        // Group same earnings:
        
        HashMap<Integer, ArrayList<SHrsReceiptEarning>> hrsReceiptEarningsMap = new HashMap<>(); // key: earning ID; value: array of earnings
        
        // Process all receipt earnings:

        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            int earningId = hrsReceiptEarning.getEarning().getPkEarningId(); // convenience variable
            ArrayList<SHrsReceiptEarning> hrsReceiptEarnings = hrsReceiptEarningsMap.get(earningId);
            
            if (hrsReceiptEarnings == null) {
                hrsReceiptEarnings = new ArrayList<>();
            }

            hrsReceiptEarnings.add(hrsReceiptEarning);
            hrsReceiptEarningsMap.put(earningId, hrsReceiptEarnings);
        }

        // Compute array of earnings exemption in group:

        for (ArrayList<SHrsReceiptEarning> array : hrsReceiptEarningsMap.values()) {
            computeEarningsExemptionGroup(array);
        }
    }

    /**
     * Get total taxed amount of earnings of standard calculation of income tax from this receipt.
     * @return Total taxed amount of earnings of standard calculation of income tax.
     */
    private double getTaxedEarningsStd() {
        double total = 0;

        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            if (!hrsReceiptEarning.getEarning().isAlternativeTaxCalculation()) {
                total = SLibUtils.roundAmount(total + hrsReceiptEarning.getPayrollReceiptEarning().getAmountTaxable());
            }
        }

        return total;
    }
    
    /**
     * Get total taxed amount of earnings of articule-174-RLISR calculation of income tax from this receipt.
     * @return Total taxed amount of earnings of articule-174-RLISR calculation of income tax.
     */
    private double getTaxedEarningsArt174() {
        double total = 0;

        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            if (hrsReceiptEarning.getEarning().isAlternativeTaxCalculation()) {
                total = SLibUtils.roundAmount(total + hrsReceiptEarning.getPayrollReceiptEarning().getAmountTaxable());
            }
        }

        return total;
    }

    /**
     * Compute, adding ore removing, earning (as other payment) of $0.01 if payroll-tax subsidy was completely offset by payroll tax.
     * Otherwise this earning (as other payment) is removed when existing.
     * @param addInformativeSubsidy
     * @param taxAssesed
     * @param subsidyAssesed
     * @param earningOtherPayment
     * @return If available, user-defined effective tax subsidy, only if informative subsidy needs to be added to receipt, otherwise zero.
     * @throws Exception 
     */
    private double computeReceiptTaxSubsidyAsOtherPayment(final boolean addInformativeSubsidy, final double taxAssesed, final double subsidyAssesed, final SDbEarning earningOtherPayment) throws Exception {
        int countSubsidy = 0;
        boolean isUserSubsidy = false;
        double userSubsidyEffective = 0; // when available, is set in auxiliar amount No. 1
        SHrsReceiptEarning hrsReceiptEarningOld = null;
        
        // identify receipt earning for subsidy as other payment:
        
        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            SDbPayrollReceiptEarning payrollReceiptEarning = hrsReceiptEarning.getPayrollReceiptEarning(); // convenience variable
            
            if (payrollReceiptEarning.getFkEarningTypeId() == SModSysConsts.HRSS_TP_EAR_OTH && payrollReceiptEarning.getFkOtherPaymentTypeId() == SModSysConsts.HRSS_TP_OTH_PAY_TAX_SUB) {
                if (++countSubsidy > 1) {
                    throw new Exception("¡No puede haber más de una percepcion 'Subsidio para el empleo' en el recibo!");
                }
                
                // prepare for update or removal of existing subsidy earning:
                hrsReceiptEarningOld = hrsReceiptEarning;
                
                if (payrollReceiptEarning.isUserEdited() || !payrollReceiptEarning.isAutomatic()) { // consider that this earning is allways automatic!, just for consistence!
                    // user edited subsidy:
                    isUserSubsidy = true;
                    userSubsidyEffective = payrollReceiptEarning.getAuxiliarAmount1();
                }
            }
        }
        
        // remove found receipt earning for subsidy:
        
        if (hrsReceiptEarningOld != null) {
            maHrsReceiptEarnings.remove(hrsReceiptEarningOld);
        }
        
        if (addInformativeSubsidy) {
            // subsidy compensated:
            
            double amountRequired = 0.01;
            SDbPayrollReceiptEarning payrollReceiptEarning = moHrsPayroll.createPayrollReceiptEarning(
                    this, earningOtherPayment, null, 
                    1, amountRequired, true, 
                    0, 0, maHrsReceiptEarnings.size() + 1);
            
            payrollReceiptEarning.setAmountExempt(amountRequired); // subsidy is exempt
            payrollReceiptEarning.setAmountTaxable(0);
            
            if (!isUserSubsidy) {
                payrollReceiptEarning.setAuxiliarAmount1(subsidyAssesed);
            }
            else {
                if (userSubsidyEffective > taxAssesed) {
                    userSubsidyEffective = taxAssesed; // prevent subsidy to be greater than tax
                }
                
                payrollReceiptEarning.setUserEdited(true);
                payrollReceiptEarning.setAuxiliarAmount1(userSubsidyEffective);
            }
            
            SHrsReceiptEarning hrsReceiptEarningNew = new SHrsReceiptEarning();
            hrsReceiptEarningNew.setHrsReceipt(this);
            hrsReceiptEarningNew.setEarning(earningOtherPayment);
            hrsReceiptEarningNew.setPayrollReceiptEarning(payrollReceiptEarning);

            maHrsReceiptEarnings.add(hrsReceiptEarningNew);
        }
        
        if (hrsReceiptEarningOld != null || addInformativeSubsidy) {
            renumberHrsReceiptEarnings();
        }
        
        return !addInformativeSubsidy ? 0 : userSubsidyEffective;
    }
    
    /**
     * Get annual tax payed.
     * @return Annual tax payed.
     */
    private double getAnnualTaxPayed() {
        double annualTaxPayed = 0;
        
        SHrsAccumulatedDeduction hrsAccumulatedDeduction = moHrsEmployee.getHrsAccumulatedDeductionByType(SModSysConsts.HRSS_TP_DED_TAX);
        if (hrsAccumulatedDeduction != null) {
            annualTaxPayed = SLibUtils.roundAmount(hrsAccumulatedDeduction.getAcummulated());
        }
        return annualTaxPayed;
    }
    
    /**
     * Get annual tax subsidy payed.
     * @return Annual tax subsidy payed
     */
    private double getAnnualSubsidyPayed() {
        double annualSubsidyPayed = 0;
        
        SHrsAccumulatedEarning hrsAccumulatedEarning = moHrsEmployee.getHrsAccumulatedEarningByType(SModSysConsts.HRSS_TP_EAR_TAX_SUB);
        if (hrsAccumulatedEarning != null) {
            annualSubsidyPayed = SLibUtils.roundAmount(hrsAccumulatedEarning.getAcummulated());
        }
        
        return annualSubsidyPayed;
    }
    
    /**
     * Create tax and subsidy table factors array for ordinary year.
     * @param payroll Payroll.
     * @param hrsEmployeeDays HRS employee days.
     * @return Array of factors: index 0 = payroll factor; index 1 = annual factor.
     */
    private double[] createTableFactors(final SDbPayroll payroll, final SHrsEmployeeDays hrsEmployeeDays) {
        double yearDaysFactor = (double) SHrsConsts.YEAR_MONTHS / ((double) SHrsConsts.YEAR_DAYS + (SLibTimeUtils.isLeapYear(payroll.getFiscalYear()) ? 1d : 0d));
        double tableFactorAnnual = yearDaysFactor * hrsEmployeeDays.getTaxableDaysAnnual();
        double tableFactorPayroll;

        if (payroll.isPayrollFortnightStandard()) {
            double yearDaysFactorNormalized = (double) SHrsConsts.YEAR_MONTHS / (double) SHrsConsts.YEAR_DAYS_FORTNIGHTS_FIXED;
            tableFactorPayroll = yearDaysFactorNormalized * (hrsEmployeeDays.getTaxableDaysPayroll() <= SHrsConsts.FORTNIGHT_FIXED_DAYS ? hrsEmployeeDays.getTaxableDaysPayroll() : SHrsConsts.FORTNIGHT_FIXED_DAYS);
        }
        else {
            tableFactorPayroll = yearDaysFactor * hrsEmployeeDays.getTaxableDaysPayroll();
        }
        
        return new double[] { tableFactorPayroll, tableFactorAnnual };
    }
    
    /**
     * Create tax and subsidy table factors array for transition year ONLY.
     * @param employmentSubsidyAkaNewStyle Employment subsidy, the shiny "new-style".
     * @param hrsEmployeeDays HRS employee days.
     * @return Array of factors: index 0 = old-style factor; index 1 = new-style factor.
     */
    private double[] createTableFactorTrans(final SDbEmploymentSubsidy employmentSubsidyAkaNewStyle, final SHrsEmployeeDays hrsEmployeeDays) {
        Date newStyleStart = employmentSubsidyAkaNewStyle.getDateStart();   // should be May 1st, 2024
        Date newStyleEnd = SLibTimeUtils.getEndOfYear(newStyleStart);       // should be December 31st, 2024
        Date oldStyleEnd = SLibTimeUtils.addDate(newStyleStart, 0, 0, -1);  // should be April 31th, 2024
        Date oldStyleStart = SLibTimeUtils.getBeginOfYear(oldStyleEnd);     // should be January 1st, 2024

        double monthsOldStyle = SLibTimeUtils.digestMonth(oldStyleEnd)[1] - SLibTimeUtils.digestMonth(oldStyleStart)[1] + 1;
        double monthsNewStyle = SLibTimeUtils.digestMonth(newStyleEnd)[1] - SLibTimeUtils.digestMonth(newStyleStart)[1] + 1;
        double daysOldStyle = SLibTimeUtils.countPeriodDays(oldStyleStart, oldStyleEnd);
        double daysNewStyle = SLibTimeUtils.countPeriodDays(newStyleStart, newStyleEnd);

        double yearDaysFactorOldStyle = monthsOldStyle / daysOldStyle;
        double tableFactorAnnualOldStyle = yearDaysFactorOldStyle * hrsEmployeeDays.getHrsDaysGroupTransAnnualOldStyle().getTaxableDays();

        double yearDaysFactorNewStyle = monthsNewStyle / daysNewStyle;
        double tableFactorAnnualNewStyle = yearDaysFactorNewStyle * hrsEmployeeDays.getHrsDaysGroupTransAnnualNewStyle().getTaxableDays();

        return new double[] { tableFactorAnnualOldStyle, tableFactorAnnualNewStyle };
    }
    
    /**
     * Create standard taxable earnings array for transition year ONLY.
     * @param payrollIsNewStyle Current payroll is new style, otherwise old style.
     * @return Array of values: index 0 = old-style value; index 1 = new-style value.
     */
    private double[] createEarningsTaxableStdTrans(boolean payrollIsNewStyle) {
        return new double[] {
            SLibUtils.roundAmount((payrollIsNewStyle ? 0d : getTaxedEarningsStd()) + moHrsEmployee.getHrsEmployeelMvtsAnnualTransOldStyle().getTaxableEarnings()),
            SLibUtils.roundAmount((payrollIsNewStyle ? getTaxedEarningsStd() : 0d) + moHrsEmployee.getHrsEmployeelMvtsAnnualTransNewStyle().getTaxableEarnings())
        };
    }
    
    /**
     * Create Article 174 of LISR taxable earnings array for transition year ONLY.
     * @param payrollIsNewStyle Current payroll is new style, otherwise old style.
     * @return Array of values: index 0 = old-style value; index 1 = new-style value.
     */
    private double[] createEarningsTaxableArt174Trans(boolean payrollIsNewStyle) {
        return new double[] {
            SLibUtils.roundAmount((payrollIsNewStyle ? 0d : getTaxedEarningsArt174()) + moHrsEmployee.getHrsEmployeelMvtsAnnualTransOldStyle().getTaxableEarningsArt174()),
            SLibUtils.roundAmount((payrollIsNewStyle ? getTaxedEarningsArt174() : 0d) + moHrsEmployee.getHrsEmployeelMvtsAnnualTransNewStyle().getTaxableEarningsArt174())
        };
    }
    
    /**
     * Create compensated tax subsidy array for transition year ONLY.
     * @return Array of values: index 0 = old-style value; index 1 = new-style value.
     */
    private double[] createSubsidyCompensatedTrans() {
        return new double[] {
            moHrsEmployee.getHrsEmployeelMvtsAnnualTransOldStyle().getTaxSubsidyCompensated(),
            moHrsEmployee.getHrsEmployeelMvtsAnnualTransNewStyle().getTaxSubsidyCompensated()
        };
    }
    
    /**
     * Create payed tax subsidy array for transition year ONLY.
     * @return Array of values: index 0 = old-style value; index 1 = new-style value.
     */
    private double[] createSubsidyPayedTrans() {
        double subsidyPayedOldStyle = 0;
        double subsidyPayedNewStyle = 0;
        SHrsAccumulatedEarning hrsAccumulatedSub;

        hrsAccumulatedSub = moHrsEmployee.getHrsEmployeelMvtsAnnualTransOldStyle().getHrsAccumulatedEarningByType(SModSysConsts.HRSS_TP_EAR_TAX_SUB);
        if (hrsAccumulatedSub != null) {
            subsidyPayedOldStyle = SLibUtils.roundAmount(hrsAccumulatedSub.getAcummulated());
        }

        hrsAccumulatedSub = moHrsEmployee.getHrsEmployeelMvtsAnnualTransNewStyle().getHrsAccumulatedEarningByType(SModSysConsts.HRSS_TP_EAR_TAX_SUB);
        if (hrsAccumulatedSub != null) {
            subsidyPayedNewStyle = SLibUtils.roundAmount(hrsAccumulatedSub.getAcummulated());
        }

        return new double[] {
            subsidyPayedOldStyle,
            subsidyPayedNewStyle
        };
    }
    
    /**
     * Compute tax receipt.
     * @throws Exception 
     */
    private void computeReceiptTax() throws Exception {
        // Clear tax and subsidy in receipt:
        
        moPayrollReceipt.setPayrollFactorTax(0);
        moPayrollReceipt.setPayrollTaxAssessed(0);
        moPayrollReceipt.setPayrollTaxCompensated(0);
        moPayrollReceipt.setPayrollTaxPending_r(0);
        moPayrollReceipt.setPayrollTaxPayed(0);
        moPayrollReceipt.setPayrollTaxSubsidyAssessedOld_i(0);
        moPayrollReceipt.setPayrollTaxSubsidyAssessedGross(0);
        moPayrollReceipt.setPayrollTaxSubsidyAssessed(0);
        moPayrollReceipt.setPayrollTaxSubsidyCompensated(0);
        moPayrollReceipt.setPayrollTaxSubsidyPending_r(0);
        moPayrollReceipt.setPayrollTaxSubsidyPayed(0);
        
        moPayrollReceipt.setAnnualFactorTax(0);
        moPayrollReceipt.setAnnualTaxAssessed(0);
        moPayrollReceipt.setAnnualTaxCompensated(0);
        moPayrollReceipt.setAnnualTaxPayed(0);
        moPayrollReceipt.setAnnualTaxSubsidyAssessed(0);
        moPayrollReceipt.setAnnualTaxSubsidyCompensated(0);
        moPayrollReceipt.setAnnualTaxSubsidyPayed(0);
        
        double annualTaxCompensated = 0;        // tax compensated against subsidy (sumatory excludes tax compensated in current payroll)
        double annualTaxPayed = 0;              // tax already withheld from employee (sumatory excludes tax withheld in current payroll)
        double annualSubsidyCompensated = 0;    // subsidy compensated against tax (sumatory excludes subsidy compensated in current payroll)
        double annualSubsidyPayed = 0;          // subsidy already payed to employee (sumatory excludes subsidy payed in current payroll)
        double taxAssessed = 0;
        double subsidyAssessed = 0;
        double payrollTaxAssessed = 0;
        double payrollSubsidyAssessedOldStyleInformative = 0;
        double payrollSubsidyAssessedGross = 0;
        double payrollSubsidyAssessed = 0;
        
        SHrsReceiptEarning hrsReceiptEarningSubsidyNew = null;
        SHrsReceiptDeduction hrsReceiptDeductionTaxNew = null;
        
        // Check if tax computation is needed:
        
        SDbDeduction deductionTax = null;
        SDbEarning earningSubsidy = null;
        SDbEarning earningSubsidyOtherPayment = null;
        SDbPayroll payroll = moHrsPayroll.getPayroll(); // convenience variable
        boolean isTaxCalcPayroll = payroll.getFkTaxComputationTypeId() == SModSysConsts.HRSS_TP_TAX_COMP_PAY;
        boolean isTaxCalcAnnual = payroll.getFkTaxComputationTypeId() == SModSysConsts.HRSS_TP_TAX_COMP_ANN;
        boolean computeTax = isTaxCalcPayroll || isTaxCalcAnnual;
        boolean computeSubsidy = computeTax && payroll.isTaxSubsidy() && !moPayrollReceipt.isAssimilated(); // assimilables are not elegible for subsidy
        
        if (computeTax) {
            // Validate configuration of deduction for tax:

            if (moHrsPayroll.getModuleConfig().getFkDeductionTaxId_n() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Deducción de impuesto.)");
            }

            deductionTax = moHrsPayroll.getDeduction(moHrsPayroll.getModuleConfig().getFkDeductionTaxId_n());
            if (deductionTax == null) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Deducción de impuesto.)");
            }

            if (deductionTax.getFkDeductionTypeId() != SModSysConsts.HRSS_TP_DED_TAX) {
                throw new Exception(SLibConsts.ERR_MSG_WRONG_TYPE + " (Tipo de deducción de la deducción de impuesto.)");
            }
            
            // Validate configuration of earning for subsidy:

            if (moHrsPayroll.getModuleConfig().getFkEarningTaxSubsidyId_n() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Percepción de subsidio para el empleo.)");
            }

            earningSubsidy = moHrsPayroll.getEarning(moHrsPayroll.getModuleConfig().getFkEarningTaxSubsidyId_n());
            if (earningSubsidy == null) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Percepción de subsidio para el empleo.)");
            }

            if (earningSubsidy.getFkEarningTypeId() != SModSysConsts.HRSS_TP_EAR_TAX_SUB) {
                throw new Exception(SLibConsts.ERR_MSG_WRONG_TYPE + " (Tipo de percepción de la percepción de subsidio para el empleo)");
            }
            
            // Validate configuration of earning for compensated subsidy:

            if (moHrsPayroll.getModuleConfig().getFkEarningTaxSubsidyCompensatedId_n() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Percepción de subsidio para el empleo compensado.)");
            }

            earningSubsidyOtherPayment = moHrsPayroll.getEarning(moHrsPayroll.getModuleConfig().getFkEarningTaxSubsidyCompensatedId_n());
            if (earningSubsidyOtherPayment == null) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Percepción de subsidio para el empleo compensado.)");
            }

            if (earningSubsidyOtherPayment.getFkEarningTypeId() != SModSysConsts.HRSS_TP_EAR_OTH) {
                throw new Exception(SLibConsts.ERR_MSG_WRONG_TYPE + " (Tipo de percepción de la percepción de subsidio para el empleo compensado.)");
            }

            // Validate tax table:

            if (payroll.getFkTaxId() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Tabla de impuesto.)");
            }

            SDbTaxTable taxTable = moHrsPayroll.getTaxTable(payroll.getFkTaxId());
            if (taxTable == null)  {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Tabla de impuesto.)");
            }

            // Validate subsidy table:

            if (payroll.getFkTaxSubsidyId() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Tabla de subsidio para el empleo.)");
            }

            SDbTaxSubsidyTable subsidyTable = moHrsPayroll.getTaxSubsidyTable(payroll.getFkTaxSubsidyId());
            if (subsidyTable == null)  {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Tabla de subsidio para el empleo.)");
            }
            
            // Validate employment subsidy configuration:
            
            SDbEmploymentSubsidy employmentSubsidyAkaNewStyle = null; // the shiny "new-style"
            if (payroll.getFkEmploymentSubsidyId_n() != 0) {
                employmentSubsidyAkaNewStyle = moHrsPayroll.getEmploymentSubsidy(payroll.getFkEmploymentSubsidyId_n());
                if (employmentSubsidyAkaNewStyle == null) {
                    throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Configuración de subsidio para el empleo.)");
                }
            }

            // Obtain taxable earnings and, if necessary, accumulated both compensated and payed annual tax and subsidy:
            
            double earningsTaxableStd = 0;
            double earningsTaxableArt174 = 0;
            
            if (isTaxCalcPayroll) {
                // tax computation type is payroll
                
                earningsTaxableStd = SLibUtils.roundAmount(getTaxedEarningsStd());
                earningsTaxableArt174 = SLibUtils.roundAmount(getTaxedEarningsArt174());
            }
            else {
                // tax computation type is annual
                
                earningsTaxableStd = SLibUtils.roundAmount(getTaxedEarningsStd() + moHrsEmployee.getAnnualTaxableEarnings());
                earningsTaxableArt174 = SLibUtils.roundAmount(getTaxedEarningsArt174() + moHrsEmployee.getAnnualTaxableEarningsArt174());

                annualTaxCompensated = moHrsEmployee.getAnnualTaxCompensated(); // should be equal to compensated annual subsidy
                annualTaxPayed = getAnnualTaxPayed();
                annualSubsidyCompensated = moHrsEmployee.getAnnualTaxSubsidyCompensated(); // should be equal to compensated annual tax
                annualSubsidyPayed = getAnnualSubsidyPayed();
            }

            // Define payroll and annual factors for adjusting tables of tax and subsidy:

            SHrsEmployeeDays hrsEmployeeDays = moHrsEmployee.createEmployeeDays(); // convenience variable
            double[] tableFactors = createTableFactors(payroll, hrsEmployeeDays);
            
            moPayrollReceipt.setPayrollFactorTax(tableFactors[COMP_PAY]);
            moPayrollReceipt.setAnnualFactorTax(tableFactors[COMP_ANN]);
            
            double tableFactor;
            
            if (isTaxCalcPayroll) {
                // tax computation type is payroll
                tableFactor = tableFactors[COMP_PAY];
            }
            else {
                // tax computation type is annual
                tableFactor = tableFactors[COMP_ANN];
            }

            // Compute assessed and payable tax (payroll or annual, the one required):
            
            double earningsTaxableStdForTax = earningsTaxableStd;
            double earningsTaxableArt174ForTax = earningsTaxableArt174;
            double tableFactorForTax = tableFactor;
            
            if (earningsTaxableStdForTax > 0) {
                for (int row = 0; row < taxTable.getChildRows().size(); row++) {
                    SDbTaxTableRow taxTableRow = taxTable.getChildRows().get(row);
                    if (earningsTaxableStdForTax >= SLibUtils.roundAmount(taxTableRow.getLowerLimit() * tableFactorForTax) &&
                            (row + 1 == taxTable.getChildRows().size() || earningsTaxableStdForTax < SLibUtils.roundAmount(taxTable.getChildRows().get(row + 1).getLowerLimit() * tableFactorForTax))) {
                        taxAssessed = SLibUtils.roundAmount((earningsTaxableStdForTax - SLibUtils.roundAmount(taxTableRow.getLowerLimit() * tableFactorForTax)) * taxTableRow.getTaxRate() + taxTableRow.getFixedFee() * tableFactorForTax);
                        break;
                    }
                }
            }

            if (earningsTaxableArt174ForTax > 0) {
                double taxAssessedArt74 = SHrsUtils.computeTaxAlt(taxTable, earningsTaxableArt174ForTax, moPayrollReceipt.getMonthlyPayment(), tableFactorForTax);
                taxAssessed = SLibUtils.roundAmount(taxAssessed + taxAssessedArt74); // update assessed tax
            }
            
            // Compute tax:

            payrollTaxAssessed = SLibUtils.roundAmount(taxAssessed - (annualTaxCompensated + annualTaxPayed));
            
            // Create deduction for tax:

            if (payrollTaxAssessed > 0) {
                SDbPayrollReceiptDeduction payrollReceiptDeductionTax = moHrsPayroll.createPayrollReceiptDeduction(
                        this, deductionTax, 
                        1, payrollTaxAssessed, true, 
                        0, 0, maHrsReceiptDeductions.size() + 1);

                hrsReceiptDeductionTaxNew = new SHrsReceiptDeduction();
                hrsReceiptDeductionTaxNew.setHrsReceipt(this);
                hrsReceiptDeductionTaxNew.setDeduction(deductionTax);
                hrsReceiptDeductionTaxNew.setPayrollReceiptDeduction(payrollReceiptDeductionTax);
            }

            if (computeSubsidy) {
                // Prepare subsidy computation for both old and new styles:
                
                // it will be true only if payroll belongs to the year of transtition of style of subsidy (i.e., 2024), and also its tax calculation is annual:
                boolean isTransitionYearForTaxSubsidyAndTaxCalcAnnual = moHrsPayroll.isTransitionYearForTaxSubsidy() && isTaxCalcAnnual;
                
                double annualTaxSubsidyAssessedOldInformative = 0;
                
                double[] earningsTaxableStdTrans = null;    // index 0 = old syle; index 1 = new style
                double[] earningsTaxableArt174Trans = null; // index 0 = old syle; index 1 = new style
                double[] tableFactorTrans = null;           // index 0 = old syle; index 1 = new style
                double[] subsidyCompensatedTrans = null;    // index 0 = old syle; index 1 = new style
                double[] subsidyPayedTrans = null;          // index 0 = old syle; index 1 = new style
                
                if (isTransitionYearForTaxSubsidyAndTaxCalcAnnual) {
                    boolean payrollIsNewStyle = moHrsPayroll.isEmploymentSubsidyApplying() && employmentSubsidyAkaNewStyle != null && !payroll.getDateStart().before(employmentSubsidyAkaNewStyle.getDateStart());

                    annualTaxSubsidyAssessedOldInformative = moHrsEmployee.getAnnualTaxSubsidyAssessedOldInformative();
                    
                    earningsTaxableStdTrans = createEarningsTaxableStdTrans(payrollIsNewStyle);
                    earningsTaxableArt174Trans = createEarningsTaxableArt174Trans(payrollIsNewStyle);
                    tableFactorTrans = createTableFactorTrans(employmentSubsidyAkaNewStyle, hrsEmployeeDays);
                    subsidyCompensatedTrans = createSubsidyCompensatedTrans();
                    subsidyPayedTrans = createSubsidyPayedTrans();
                }
                
                // Compute assessed and payable subsidy (payroll or annual, the one required):
                
                // payroll is before May 1st, 2024, or it belongs to the year of transtition of style of subsidy (i.e., 2024), and also its tax calculation is annual:
                boolean appliesOldStyle = !moHrsPayroll.isEmploymentSubsidyApplying() || isTransitionYearForTaxSubsidyAndTaxCalcAnnual;
                // payroll is after May 1st, 2024, inclusive:
                boolean appliesNewStyle = moHrsPayroll.isEmploymentSubsidyApplying();
                
                // old-syle:
                
                double subsidyAssessedOldStyle = 0;
                double subsidyCompensatedForSubsidyOldStyle = 0;
                double subsidyPayedForSubsidyOldStyle = 0;
                
                if (appliesOldStyle) {
                    double earningsTaxableStdForSubsidyOldStyle = 0;
                    double earningsTaxableArt174ForSubsidyOldStyle = 0;
                    double tableFactorForSubsidyOldStyle = 0;

                    if (isTransitionYearForTaxSubsidyAndTaxCalcAnnual) { // payroll belongs to the year of transtition of style of subsidy (i.e., 2024), and its tax calculation is annual?
                        earningsTaxableStdForSubsidyOldStyle = earningsTaxableStdTrans[STYLE_OLD];
                        earningsTaxableArt174ForSubsidyOldStyle = earningsTaxableArt174Trans[STYLE_OLD];
                        tableFactorForSubsidyOldStyle = tableFactorTrans[STYLE_OLD];
                        subsidyCompensatedForSubsidyOldStyle = subsidyCompensatedTrans[STYLE_OLD];
                        subsidyPayedForSubsidyOldStyle = subsidyPayedTrans[STYLE_OLD];
                    }
                    else {
                        earningsTaxableStdForSubsidyOldStyle = earningsTaxableStd;
                        earningsTaxableArt174ForSubsidyOldStyle = earningsTaxableArt174;
                        tableFactorForSubsidyOldStyle = tableFactor;
                        subsidyCompensatedForSubsidyOldStyle = annualSubsidyCompensated;
                        subsidyPayedForSubsidyOldStyle = annualSubsidyPayed;
                    }

                    if (earningsTaxableStdForSubsidyOldStyle > 0) {
                        for (int row = 0; row < subsidyTable.getChildRows().size(); row++) {
                            SDbTaxSubsidyTableRow subsidyTableRow = subsidyTable.getChildRows().get(row);
                            if (earningsTaxableStdForSubsidyOldStyle >= subsidyTableRow.getLowerLimit() * tableFactorForSubsidyOldStyle &&
                                    (row + 1 == subsidyTable.getChildRows().size() || earningsTaxableStdForSubsidyOldStyle < subsidyTable.getChildRows().get(row + 1).getLowerLimit() * tableFactorForSubsidyOldStyle)) {
                                subsidyAssessedOldStyle = SLibUtils.roundAmount(subsidyTableRow.getTaxSubsidy() * tableFactorForSubsidyOldStyle);
                                break;
                            }
                        }
                    }

                    if (earningsTaxableArt174ForSubsidyOldStyle > 0) {
                        double subsidyComputedArt74 = SHrsUtils.computeTaxSubsidyAlt(subsidyTable, earningsTaxableArt174ForSubsidyOldStyle, moPayrollReceipt.getMonthlyPayment(), tableFactorForSubsidyOldStyle);
                        subsidyAssessedOldStyle = SLibUtils.roundAmount(subsidyAssessedOldStyle + subsidyComputedArt74); // update assessed subsidy
                    }
                }
                
                // new-style:
                
                double subsidyAssessedNewStyle = 0;
                double subsidyCompensatedForSubsidyNewStyle = 0;
                double subsidyPayedForSubsidyNewStyle = 0;
                
                if (appliesNewStyle) {
                    double earningsTaxableStdForSubsidyNewStyle = 0;
                    double earningsTaxableArt174ForSubsidyNewStyle = 0;
                    double tableFactorForSubsidyNewStyle = 0;
                    
                    if (isTransitionYearForTaxSubsidyAndTaxCalcAnnual) { // payroll belongs to the year of transtition of style of subsidy (i.e., 2024), and its tax calculation is annual?
                        earningsTaxableStdForSubsidyNewStyle = earningsTaxableStdTrans[STYLE_NEW];
                        earningsTaxableArt174ForSubsidyNewStyle = earningsTaxableArt174Trans[STYLE_NEW];
                        tableFactorForSubsidyNewStyle = tableFactorTrans[STYLE_NEW];
                        subsidyCompensatedForSubsidyNewStyle = subsidyCompensatedTrans[STYLE_NEW];
                        subsidyPayedForSubsidyNewStyle = subsidyPayedTrans[STYLE_NEW];
                    }
                    else {
                        earningsTaxableStdForSubsidyNewStyle = earningsTaxableStd;
                        earningsTaxableArt174ForSubsidyNewStyle = earningsTaxableArt174;
                        tableFactorForSubsidyNewStyle = tableFactor;
                        subsidyCompensatedForSubsidyNewStyle = annualSubsidyCompensated;
                        subsidyPayedForSubsidyNewStyle = annualSubsidyPayed;
                    }
                    
                    if (earningsTaxableStdForSubsidyNewStyle > 0) {
                        if (earningsTaxableStdForSubsidyNewStyle <= employmentSubsidyAkaNewStyle.getIncomeMonthlyCap() * tableFactorForSubsidyNewStyle) {
                            subsidyAssessedNewStyle = SLibUtils.roundAmount(employmentSubsidyAkaNewStyle.getSubsidyMonthlyCap() * tableFactorForSubsidyNewStyle);
                        }
                    }
                    
                    if (earningsTaxableArt174ForSubsidyNewStyle > 0) {
                        double subsidyComputedArt74 = SHrsUtils.computeEmploymentSubsidyAlt(employmentSubsidyAkaNewStyle, earningsTaxableArt174ForSubsidyNewStyle, moPayrollReceipt.getMonthlyPayment(), tableFactorForSubsidyNewStyle);
                        subsidyAssessedNewStyle = SLibUtils.roundAmount(subsidyAssessedNewStyle + subsidyComputedArt74); // update employment subsidy
                    }
                }
                
                // Compute subsidy:
                
                // old-style:
                
                double payrollSubsidyAssessedGrossOldStyle = 0;
                double payrollSubsidyAssessedOldStyle = 0;
                
                if (appliesOldStyle) {
                    double maxSubsidyAssessed = DNom12Consts.MAX_TAX_SUB; // set maximum subsidy in one single receipt according to new regulations as of January 2020
                    
                    payrollSubsidyAssessedGrossOldStyle = SLibUtils.roundAmount(subsidyAssessedOldStyle - (subsidyCompensatedForSubsidyOldStyle + subsidyPayedForSubsidyOldStyle + annualTaxSubsidyAssessedOldInformative));
                    payrollSubsidyAssessedOldStyle = payrollSubsidyAssessedGrossOldStyle <= maxSubsidyAssessed ? payrollSubsidyAssessedGrossOldStyle : maxSubsidyAssessed; // applay maximum value for subsidy when needed
                    
                    if (isTransitionYearForTaxSubsidyAndTaxCalcAnnual) { // payroll belongs to the year of transtition of style of subsidy (i.e., 2024), and its tax calculation is annual?
                        payrollSubsidyAssessedOldStyleInformative = payrollSubsidyAssessedOldStyle;
                    }
                }
                
                // new-style:
                
                double payrollSubsidyAssessedGrossNewStyle = 0;
                double payrollSubsidyAssessedNewStyle = 0;
                
                if (appliesNewStyle) {
                    double maxSubsidyAssessed = payrollTaxAssessed;
                    
                    payrollSubsidyAssessedGrossNewStyle = SLibUtils.roundAmount(subsidyAssessedNewStyle - (subsidyCompensatedForSubsidyNewStyle + subsidyPayedForSubsidyNewStyle - annualTaxSubsidyAssessedOldInformative));
                    payrollSubsidyAssessedNewStyle = payrollSubsidyAssessedGrossNewStyle <= maxSubsidyAssessed ? payrollSubsidyAssessedGrossNewStyle : maxSubsidyAssessed; // applay maximum value for subsidy when needed
                }
                
                // combine old and new styles:
                
                payrollSubsidyAssessedGross = SLibUtils.roundAmount(payrollSubsidyAssessedGrossOldStyle + payrollSubsidyAssessedGrossNewStyle);
                payrollSubsidyAssessed = SLibUtils.roundAmount(payrollSubsidyAssessedOldStyle + payrollSubsidyAssessedNewStyle);
                
                // finally, due to payroll CFDI rules still valid (by 2024-06-03), subsidy is capped:
                if (payrollSubsidyAssessed > DNom12Consts.MAX_TAX_SUB) {
                    payrollSubsidyAssessed = DNom12Consts.MAX_TAX_SUB;
                }
                
                // Create earning for subsidy:
                
                if (payrollSubsidyAssessed > 0) {
                    SDbPayrollReceiptEarning payrollReceiptEarningSub = moHrsPayroll.createPayrollReceiptEarning(
                            this, earningSubsidy, null, 
                            1, payrollSubsidyAssessed, true, 
                            0, 0, maHrsReceiptEarnings.size() + 1);

                    payrollReceiptEarningSub.setAmountExempt(payrollSubsidyAssessed); // subsidy is exempt
                    payrollReceiptEarningSub.setAmountTaxable(0);

                    hrsReceiptEarningSubsidyNew = new SHrsReceiptEarning();
                    hrsReceiptEarningSubsidyNew.setHrsReceipt(this);
                    hrsReceiptEarningSubsidyNew.setEarning(earningSubsidy);
                    hrsReceiptEarningSubsidyNew.setPayrollReceiptEarning(payrollReceiptEarningSub);
                }
            }
        }
        
        // Add tax and subsidy to payroll
        
        // Prepare tax:
        
        /* Consider that:
         * In payroll tax computation, tax cannot be negative.
         * In annual tax computation, tax can be negative.
         * If assesed tax is negative (i.e., tax excessively withheld), it will not be refund to employee.
         * If assesed subsidy is negative (i.e., subsidy excessively paid), it will be recovered from employee as tax.
         */
        
        if (payrollTaxAssessed < 0) {
            // tax cannot be negative:
            
            payrollTaxAssessed = 0;
        }
        
        // Prepare net tax: if positive = tax; if negative = subsidy:
        
        boolean isTaxNet = moHrsPayroll.getModuleConfig().isTaxNet(); // convenience variable
        double taxNet = isTaxNet ? SLibUtils.roundAmount(payrollTaxAssessed - payrollSubsidyAssessed) : 0;
        
        // Prepare removal or update of previous tax receipt deduction, if any:

        int countTax = 0; // receipt deduction entries for tax should be one at the most
        boolean isUserTax = false;
        double userTax = 0;
        SHrsReceiptDeduction hrsReceiptDeductionTaxOld = null;

        for (SHrsReceiptDeduction hrsReceiptDeduction : maHrsReceiptDeductions) {
            SDbPayrollReceiptDeduction payrollReceiptDeduction = hrsReceiptDeduction.getPayrollReceiptDeduction(); // convenience variable
            
            if (payrollReceiptDeduction.getFkDeductionTypeId() == SModSysConsts.HRSS_TP_DED_TAX) {
                if (++countTax > 1) {
                    throw new Exception("¡No puede haber más de una deducción 'ISR' en el recibo!");
                }
                
                // prepare for update or removal of existing tax deduction:
                hrsReceiptDeductionTaxOld = hrsReceiptDeduction;
                
                if (payrollReceiptDeduction.isUserEdited() || !payrollReceiptDeduction.isAutomatic() || payrollReceiptDeduction.getFkDeductionId() != moHrsPayroll.getModuleConfig().getFkDeductionTaxId_n()) {
                    // user edited tax:
                    isUserTax = true;
                    userTax = payrollReceiptDeduction.getAmount_r();
                }
            }
        }
        
        // Set payroll receipt tax:
        
        double payrollTax;
        
        if (isTaxNet) {
            payrollTax = taxNet > 0 ? taxNet : 0; // net tax: if positive = tax; if negative = subsidy
        }
        else {
            double payrollTaxCompensated = SLibUtils.roundAmount(payrollTaxAssessed - (payrollSubsidyAssessed > 0 ? payrollSubsidyAssessed : 0));
            payrollTax = payrollTaxCompensated > 0 ? payrollTaxCompensated : 0; // XXX 2021-02-25 Sergio Flores: Validar si en efecto aquí es necesario compensar el impuesto contra el subsidio, puesto que se trata del caso de impuesto no "neteado".
        }
        
        if (hrsReceiptDeductionTaxOld != null || hrsReceiptDeductionTaxNew != null) {
            if (isUserTax) {
                // preserve computed tax by system (and implicitly tax by user as well):
                SDbPayrollReceiptDeduction payrollReceiptDeduction = hrsReceiptDeductionTaxOld.getPayrollReceiptDeduction(); // convenience variable
                payrollReceiptDeduction.setAmountUnitary(userTax); // already set with tax by user!, just for consistence!
                payrollReceiptDeduction.setAmountSystem_r(payrollTax);
                payrollReceiptDeduction.setAmount_r(userTax); // already set with tax by user!, just for consistence!
            }
            else if (hrsReceiptDeductionTaxOld != null) {
                if (payrollTax > 0) {
                    // update former deduction:
                    SDbPayrollReceiptDeduction payrollReceiptDeduction = hrsReceiptDeductionTaxOld.getPayrollReceiptDeduction(); // convenience variable
                    payrollReceiptDeduction.setAmountUnitary(payrollTax);
                    payrollReceiptDeduction.setAmountSystem_r(payrollTax);
                    payrollReceiptDeduction.setAmount_r(payrollTax);
                }
                else {
                    // remove former deduction:
                    maHrsReceiptDeductions.remove(hrsReceiptDeductionTaxOld);
                    renumberHrsReceiptDeductions();
                }
            }
            else if (payrollTax > 0) {
                // add new deduction:
                
                if (isTaxNet) {
                    // reasign tax; when created new deduction was asigned with assesed tax to pay:
                    SDbPayrollReceiptDeduction payrollReceiptDeduction = hrsReceiptDeductionTaxNew.getPayrollReceiptDeduction(); // convenience variable
                    payrollReceiptDeduction.setAmountUnitary(payrollTax);
                    payrollReceiptDeduction.setAmountSystem_r(payrollTax);
                    payrollReceiptDeduction.setAmount_r(payrollTax);
                }
                
                maHrsReceiptDeductions.add(hrsReceiptDeductionTaxNew);
                renumberHrsReceiptDeductions();
            }
        }
        
        // Prepare removal or update of previous subsidy receipt earning, if any:
        
        int countSubsidy = 0; // receipt earning entries for subsidy should be one at the most
        boolean isUserSubsidy = false;
        double userSubsidy = 0;
        double userSubsidyEffective = 0;
        SHrsReceiptEarning hrsReceiptEarningSubsidyOld = null;
        
        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            SDbPayrollReceiptEarning payrollReceiptEarning = hrsReceiptEarning.getPayrollReceiptEarning(); // convenience variable
            
            if (payrollReceiptEarning.getFkEarningTypeId() == SModSysConsts.HRSS_TP_EAR_TAX_SUB) {
                if (++countSubsidy > 1) {
                    throw new Exception("¡No puede haber más de una percepcion 'Subsidio para el empleo' en el recibo!");
                }
                
                // prepare for update or removal of existing subsidy earning:
                hrsReceiptEarningSubsidyOld = hrsReceiptEarning;
                
                if (payrollReceiptEarning.isUserEdited() || !payrollReceiptEarning.isAutomatic()) {
                    // user edited subsidy:
                    isUserSubsidy = true;
                    userSubsidy = payrollReceiptEarning.getAmount_r();
                    userSubsidyEffective = payrollReceiptEarning.getAuxiliarAmount1();
                }
            }
        }
        
        // Set payroll receipt subsidy:
        
        double payrollSubsidy;
        
        if (isTaxNet) {
            payrollSubsidy = taxNet < 0 ? -taxNet : 0; // net tax: if positive = tax; if negative = subsidy
        }
        else {
            payrollSubsidy = payrollSubsidyAssessed > 0 ? payrollSubsidyAssessed : 0;
        }
        
        if (hrsReceiptEarningSubsidyOld != null || hrsReceiptEarningSubsidyNew != null) {
            if (isUserSubsidy) {
                // preserve computed subsidy by system (and implicitly subsidy by user as well):
                SDbPayrollReceiptEarning payrollReceiptEarning = hrsReceiptEarningSubsidyOld.getPayrollReceiptEarning(); // convenience variable
                payrollReceiptEarning.setAmountUnitary(userSubsidy); // already set with subsidy by user!, just for consistence!
                payrollReceiptEarning.setAmountSystem_r(payrollSubsidy);
                payrollReceiptEarning.setAmount_r(userSubsidy); // already set with subsidy by user!, just for consistence!
                
                payrollReceiptEarning.setAmountExempt(userSubsidy); // subsidy is exempt
                payrollReceiptEarning.setAmountTaxable(0);
            }
            else if (hrsReceiptEarningSubsidyOld != null) {
                if (payrollSubsidy > 0) {
                    // update former earning:
                    SDbPayrollReceiptEarning payrollReceiptEarning = hrsReceiptEarningSubsidyOld.getPayrollReceiptEarning(); // convenience variable
                    payrollReceiptEarning.setAmountUnitary(payrollSubsidy);
                    payrollReceiptEarning.setAmountSystem_r(payrollSubsidy);
                    payrollReceiptEarning.setAmount_r(payrollSubsidy);
                    
                    payrollReceiptEarning.setAmountExempt(payrollSubsidy); // subsidy is exempt
                    payrollReceiptEarning.setAmountTaxable(0);
                }
                else {
                    // remove former earning:
                    maHrsReceiptEarnings.remove(hrsReceiptEarningSubsidyOld);
                    renumberHrsReceiptEarnings();
                }
            }
            else if (payrollSubsidy > 0) {
                // add new earning:
                
                if (isTaxNet) {
                    // reasign subsidy; when created new earning was asigned with assessed subsidy to pay:
                    SDbPayrollReceiptEarning payrollReceiptEarning = hrsReceiptEarningSubsidyNew.getPayrollReceiptEarning(); // convenience variable
                    payrollReceiptEarning.setAmountUnitary(payrollSubsidy);
                    payrollReceiptEarning.setAmountSystem_r(payrollSubsidy);
                    payrollReceiptEarning.setAmount_r(payrollSubsidy);
                    
                    payrollReceiptEarning.setAmountExempt(payrollSubsidy); // subsidy is exempt
                    payrollReceiptEarning.setAmountTaxable(0);
                }
                
                maHrsReceiptEarnings.add(hrsReceiptEarningSubsidyNew);
                renumberHrsReceiptEarnings();
            }
        }
        
        // Create other earning to inform subsidy assessed in receipt, if needed:
        
        boolean addInformativeSubsidy = hrsReceiptEarningSubsidyOld == null && isTaxNet && payrollSubsidyAssessed > 0 && payrollTaxAssessed >= payrollSubsidyAssessed; // convenience variable
        double userInformativeSubsidyEffective = computeReceiptTaxSubsidyAsOtherPayment(addInformativeSubsidy, payrollTaxAssessed, payrollSubsidyAssessed, earningSubsidyOtherPayment);
        
        // Set tax and subsidy current calculations:
        
        if (computeTax) {
            double payrollTaxCompensated = 0;
            double payrollSubsidyCompensated = 0;

            if (isTaxNet) {
                // compensate tax and subsidy between each other:

                if (payrollTaxAssessed >= payrollSubsidyAssessed) {
                    payrollTaxCompensated = payrollSubsidyAssessed;
                    
                    if (userSubsidyEffective != 0) {
                        payrollSubsidyCompensated = userSubsidyEffective;
                    }
                    else if (userInformativeSubsidyEffective != 0) {
                        payrollSubsidyCompensated = userInformativeSubsidyEffective;
                    }
                    else {
                        payrollSubsidyCompensated = payrollSubsidyAssessed;
                    }
                }
                else {
                    payrollTaxCompensated = payrollTaxAssessed;
                    payrollSubsidyCompensated = payrollTaxAssessed;
                }
            }
            
            moPayrollReceipt.setPayrollTaxAssessed(payrollTaxAssessed);
            moPayrollReceipt.setPayrollTaxCompensated(payrollTaxCompensated);
            moPayrollReceipt.setPayrollTaxPending_r(payrollTax);
            moPayrollReceipt.setPayrollTaxPayed(isUserTax ? userTax : payrollTax);
            moPayrollReceipt.setPayrollTaxSubsidyAssessedOld_i(payrollSubsidyAssessedOldStyleInformative);
            moPayrollReceipt.setPayrollTaxSubsidyAssessedGross(payrollSubsidyAssessedGross);
            moPayrollReceipt.setPayrollTaxSubsidyAssessed(payrollSubsidyAssessed);
            moPayrollReceipt.setPayrollTaxSubsidyCompensated(payrollSubsidyCompensated);
            moPayrollReceipt.setPayrollTaxSubsidyPending_r(payrollSubsidy);
            moPayrollReceipt.setPayrollTaxSubsidyPayed(isUserSubsidy ? userSubsidy : payrollSubsidy);
            
            if (isTaxCalcPayroll) {
                // tax computation type is payroll
                moPayrollReceipt.setAnnualTaxAssessed(0);
                moPayrollReceipt.setAnnualTaxCompensated(0);
                moPayrollReceipt.setAnnualTaxPayed(0);
                moPayrollReceipt.setAnnualTaxSubsidyAssessed(0);
                moPayrollReceipt.setAnnualTaxSubsidyCompensated(0);
                moPayrollReceipt.setAnnualTaxSubsidyPayed(0);
            }
            else {
                // tax computation type is annual
                moPayrollReceipt.setAnnualTaxAssessed(taxAssessed);
                moPayrollReceipt.setAnnualTaxCompensated(annualTaxCompensated);
                moPayrollReceipt.setAnnualTaxPayed(annualTaxPayed);
                moPayrollReceipt.setAnnualTaxSubsidyAssessed(subsidyAssessed);
                moPayrollReceipt.setAnnualTaxSubsidyCompensated(annualSubsidyCompensated);
                moPayrollReceipt.setAnnualTaxSubsidyPayed(annualSubsidyPayed);
            }
        }
    }

    private void computeReceiptSsContribution() throws Exception {
        double sscComputed = 0;
        SHrsReceiptDeduction hrsReceiptDeductionSscNew = null;
        SDbPayroll payroll = moHrsPayroll.getPayroll(); // convenience variable
        
        if (payroll.isSsContribution() && !moPayrollReceipt.isAssimilated() && moPayrollReceipt.getEffectiveSalary(payroll.isFortnightStandard()) > payroll.getMwzWage()) {  // assimilables and minimum wage employees are not elegible for SS contribution
            // Validate configuration of SS contribution:

            if (moHrsPayroll.getModuleConfig().getFkDeductionSsContributionId_n() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración de retención de SS)");
            }

            SDbDeduction deductionSsc = moHrsPayroll.getDeduction(moHrsPayroll.getModuleConfig().getFkDeductionSsContributionId_n());
            if (deductionSsc == null) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Configuración de retención de SS)");
            }

            if (deductionSsc.getFkDeductionTypeId() != SModSysConsts.HRSS_TP_DED_SSC) {
                throw new Exception(SLibConsts.ERR_MSG_WRONG_TYPE + " (Configuración de retención de SS)");
            }

            if (payroll.getFkSsContributionId() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración de tabla de retención de SS)");
            }

            SDbSsContributionTable ssContributionTable = moHrsPayroll.getSsContributionTable(payroll.getFkSsContributionId());
            if (ssContributionTable == null)  {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Tabla de retención de SS)");
            }
            
            // Compute SS contribution:

            SHrsDaysByPeriod hrsDaysPrev = moHrsEmployee.getHrsDaysPrev();
            SHrsDaysByPeriod hrsDaysCurr = moHrsEmployee.getHrsDaysCurr();
            SHrsDaysByPeriod hrsDaysNext = moHrsEmployee.getHrsDaysNext();
            int daysWorkedAndIncapacityNotPaid = hrsDaysPrev.getPeriodPayrollDays() + hrsDaysCurr.getPeriodPayrollDays() + hrsDaysNext.getPeriodPayrollDays() - 
                    hrsDaysPrev.getDaysIncapacityNotPaid() - hrsDaysCurr.getDaysIncapacityNotPaid() - hrsDaysNext.getDaysIncapacityNotPaid();
            int daysWorkedAndNotWorkedNotPaid = hrsDaysPrev.getPeriodPayrollDays() + hrsDaysCurr.getPeriodPayrollDays() + hrsDaysNext.getPeriodPayrollDays() - 
                    hrsDaysPrev.getDaysNotWorkedNotPaid() - hrsDaysCurr.getDaysNotWorkedNotPaid() - hrsDaysNext.getDaysNotWorkedNotPaid();
            
            for (int row = 0; row < ssContributionTable.getChildRows().size(); row++) {
                double sscRow = 0;
                SDbSsContributionTableRow dbSscTableRow = ssContributionTable.getChildRows().get(row);
                
                switch(dbSscTableRow.getPkRowId()) {
                    case SHrsConsts.SS_INC_MON: // money
                    case SHrsConsts.SS_INC_PEN: // pensioner
                        sscRow = SLibUtils.roundAmount(daysWorkedAndIncapacityNotPaid * moPayrollReceipt.getSalarySscBase());
                        break;
                        
                    case SHrsConsts.SS_INC_KND_SSC_LET: // kind SSC less or equal than limit
                        sscRow = SLibUtils.roundAmount(daysWorkedAndIncapacityNotPaid * payroll.getUmaAmount()); // formerly minimum wage of reference zone was used
                        break;
                        
                    case SHrsConsts.SS_INC_KND_SSC_GT: // kind SSC greater than limit
                        double surplus = moPayrollReceipt.getSalarySscBase() - (dbSscTableRow.getLowerLimitMwzReference() * payroll.getUmaAmount()); // formerly minimum wage of reference zone was used
                        sscRow = surplus <= 0 ? 0 : SLibUtils.roundAmount(daysWorkedAndIncapacityNotPaid * surplus);
                        break;
                        
                    case SHrsConsts.SS_DIS_LIF: // disability & life
                    case SHrsConsts.SS_CRE: // creche
                    case SHrsConsts.SS_RSK: // risk
                    case SHrsConsts.SS_RET: // retirement
                    case SHrsConsts.SS_SEV: // severance
                    case SHrsConsts.SS_HOM: // home
                        sscRow = SLibUtils.roundAmount(daysWorkedAndNotWorkedNotPaid * moPayrollReceipt.getSalarySscBase());
                        break;
                        
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                
                sscComputed = SLibUtils.roundAmount(sscComputed + SLibUtils.roundAmount(sscRow * dbSscTableRow.getWorkerPercentage()));
            }
            
            if (sscComputed != 0) {
                SDbPayrollReceiptDeduction payrollReceiptDeductionSsc = moHrsPayroll.createPayrollReceiptDeduction(
                        this, deductionSsc, 
                        1, sscComputed, true, 
                        0, 0, maHrsReceiptDeductions.size() + 1);

                hrsReceiptDeductionSscNew = new SHrsReceiptDeduction();
                hrsReceiptDeductionSscNew.setHrsReceipt(this);
                hrsReceiptDeductionSscNew.setDeduction(deductionSsc);
                hrsReceiptDeductionSscNew.setPayrollReceiptDeduction(payrollReceiptDeductionSsc);
            }
        }
        
        // Remove or update previous SS contribution:

        int deductions = 0;
        boolean isUserSsc = false;
        SHrsReceiptDeduction hrsReceiptDeductionSscOld = null;

        for (SHrsReceiptDeduction hrsReceiptDeduction : maHrsReceiptDeductions) {
            SDbPayrollReceiptDeduction payrollReceiptDeduction = hrsReceiptDeduction.getPayrollReceiptDeduction();
            
            if (payrollReceiptDeduction.getFkDeductionTypeId() == SModSysConsts.HRSS_TP_DED_SSC) {
                if (++deductions > 1) {
                    throw new Exception("¡No puede haber más de una deducción 'Seguridad social' en el recibo!");
                }
                
                // prepare for removal or update of existing SS contribution deduction:
                hrsReceiptDeductionSscOld = hrsReceiptDeduction;
                
                if (payrollReceiptDeduction.isUserEdited() || !payrollReceiptDeduction.isAutomatic()) {
                    // user edited SS contribution:
                    isUserSsc = true;
                }
            }
        }

        if (hrsReceiptDeductionSscOld != null || hrsReceiptDeductionSscNew != null) {
            if (isUserSsc) {
                // preserve computed SS contribution:
                SDbPayrollReceiptDeduction payrollReceiptDeduction = hrsReceiptDeductionSscOld.getPayrollReceiptDeduction();
                payrollReceiptDeduction.setAmountSystem_r(sscComputed);
            }
            else if (hrsReceiptDeductionSscOld != null) {
                if (sscComputed > 0) {
                    // update former deduction:
                    SDbPayrollReceiptDeduction payrollReceiptDeduction = hrsReceiptDeductionSscOld.getPayrollReceiptDeduction();
                    payrollReceiptDeduction.setAmountUnitary(sscComputed);
                    payrollReceiptDeduction.setAmountSystem_r(sscComputed);
                    payrollReceiptDeduction.setAmount_r(sscComputed);
                }
                else {
                    // remove former deduction:
                    maHrsReceiptDeductions.remove(hrsReceiptDeductionSscOld);
                    renumberHrsReceiptDeductions();
                }
            }
            else if (sscComputed > 0) {
                // add new deduction:
                maHrsReceiptDeductions.add(hrsReceiptDeductionSscNew);
                renumberHrsReceiptDeductions();
            }
        }
    }

    private double computeDaysToBePaid(ArrayList<SHrsReceiptEarning> hrsReceiptEarnings) {
        double daysToBePaid = 0;

        for (SHrsReceiptEarning hrsReceiptEarning : hrsReceiptEarnings) {
            if (hrsReceiptEarning.getEarning().isDaysWorked()) {
                daysToBePaid += hrsReceiptEarning.getPayrollReceiptEarning().getUnitsAlleged();
            }
        }

        return daysToBePaid;
    }

    private double computeDaysPaid(ArrayList<SHrsReceiptEarning> hrsReceiptEarnings) {
        double daysWorkedPayed = 0;

        for (SHrsReceiptEarning hrsReceiptEarning : hrsReceiptEarnings) {
            if (hrsReceiptEarning.getEarning().isDaysWorked()) {
                daysWorkedPayed += hrsReceiptEarning.getPayrollReceiptEarning().getUnits();
            }
        }

        return daysWorkedPayed;
    }
    
    /*
     * Public methods
     */

    public void setHrsEmployee(SHrsEmployee o) { moHrsEmployee = o; }
    public void setHrsBenefitsManager(SHrsBenefitsManager o) { moHrsBenefitsManager = o; }
    public void setPayrollReceipt(SDbPayrollReceipt o)  { moPayrollReceipt = o; }

    public SHrsPayroll getHrsPayroll() { return moHrsPayroll; }
    public SHrsEmployee getHrsEmployee() { return moHrsEmployee; }
    public SHrsBenefitsManager getHrsBenefitsManager() { try { initHrsBenefitsManager(); } catch (Exception e) { SLibUtils.printException(this, e); } return moHrsBenefitsManager; }
    public SDbPayrollReceipt getPayrollReceipt()  { return moPayrollReceipt; }
    public ArrayList<SDbAbsenceConsumption> getAbsenceConsumptions() { return maAbsenceConsumptions; }
    public ArrayList<SHrsReceiptEarning> getHrsReceiptEarnings() { return maHrsReceiptEarnings; }
    public ArrayList<SHrsReceiptDeduction> getHrsReceiptDeductions() { return maHrsReceiptDeductions; }
    public ArrayList<SHrsBenefit> getHrsBenefits() { return maHrsBenefits; }

    @SuppressWarnings("unchecked")
    public void renumberHrsReceiptEarnings() {
        int moveId = 0;
        
        Collections.sort(maHrsReceiptEarnings);
        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            hrsReceiptEarning.getPayrollReceiptEarning().setPkMoveId(++moveId);
        }
    }

    @SuppressWarnings("unchecked")
    public void renumberHrsReceiptDeductions() {
        int moveId = 0;
        
        Collections.sort(maHrsReceiptDeductions);
        for (SHrsReceiptDeduction hrsReceiptDeduction : maHrsReceiptDeductions) {
            hrsReceiptDeduction.getPayrollReceiptDeduction().setPkMoveId(++moveId);
        }
    }

    public SHrsReceiptEarning getHrsReceiptEarning(final int moveId) {
        SHrsReceiptEarning hrsReceiptEarningWanted = null;

        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            if (hrsReceiptEarning.getPayrollReceiptEarning().getPkMoveId() == moveId) {
                hrsReceiptEarningWanted = hrsReceiptEarning;
                break;
            }
        }

        return hrsReceiptEarningWanted;
    }

    public SHrsReceiptDeduction getHrsReceiptDeduction(final int moveId) {
        SHrsReceiptDeduction hrsReceiptDeductionWanted = null;

        for (SHrsReceiptDeduction hrsReceiptDeduction : maHrsReceiptDeductions) {
            if (hrsReceiptDeduction.getPayrollReceiptDeduction().getPkMoveId() == moveId) {
                hrsReceiptDeductionWanted = hrsReceiptDeduction;
                break;
            }
        }

        return hrsReceiptDeductionWanted;
    }
    
    /**
     * Adds HRS Receipt Earning, renumbers earnings of receipt, and finally computes receipt.
     * @param hrsReceiptEarning
     * @return 
     */
    public SHrsReceiptEarning addHrsReceiptEarning(final SHrsReceiptEarning hrsReceiptEarning) {
        try {
            maHrsReceiptEarnings.add(hrsReceiptEarning);
            renumberHrsReceiptEarnings();
            computeReceipt();
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }

        return hrsReceiptEarning;
    }

    /**
     * Adds HRS Receipt Deduction, renumbers deductions of receipt, and finally computes receipt.
     * @param hrsReceiptDeduction
     * @return 
     */
    public SHrsReceiptDeduction addHrsReceiptDeduction(final SHrsReceiptDeduction hrsReceiptDeduction) {
        try {
            maHrsReceiptDeductions.add(hrsReceiptDeduction);
            renumberHrsReceiptDeductions();
            computeReceipt();
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }

        return hrsReceiptDeduction;
    }

    /**
     * If found, replaces supplied HRS Receipt Earning by its Move ID, and computes receipt. Otherwise it is discarded.
     * @param moveId
     * @param hrsReceiptEarning 
     */
    public void replaceHrsReceiptEarning(final int moveId, final SHrsReceiptEarning hrsReceiptEarning) {
        try {
            boolean found = false;
            
            for (int i = 0; i < maHrsReceiptEarnings.size(); i++) {
                if (maHrsReceiptEarnings.get(i).getPayrollReceiptEarning().getPkMoveId() == moveId) {
                    maHrsReceiptEarnings.set(i, hrsReceiptEarning);
                    found = true;
                    break;
                }
            }
            
            if (found) {
                computeReceipt();
            }
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    /**
     * If found, replaces supplied HRS Receipt Deduction by its Move ID, and computes receipt. Otherwise it is discarded.
     * @param moveId
     * @param hrsReceiptDeduction 
     */
    public void replaceHrsReceiptDeduction(final int moveId, final SHrsReceiptDeduction hrsReceiptDeduction) {
        try {
            boolean found = false;
            
            for (int i = 0; i < maHrsReceiptDeductions.size(); i++) {
                if (maHrsReceiptDeductions.get(i).getPayrollReceiptDeduction().getPkMoveId() == moveId) {
                    maHrsReceiptDeductions.set(i, hrsReceiptDeduction);
                    found = true;
                    break;
                }
            }
            
            if (found) {
                computeReceipt();
            }
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    /**
     * If found, removes HRS Receipt Earning by its Move ID, renumbers earnings of receipt, and finally computes receipt.
     * @param moveId 
     */
    public void removeHrsReceiptEarning(final int moveId) {
        try {
            boolean found = false;
            
            for (int i = 0; i < maHrsReceiptEarnings.size(); i++) {
                if (maHrsReceiptEarnings.get(i).getPayrollReceiptEarning().getPkMoveId() == moveId) {
                    maHrsReceiptEarnings.remove(i);
                    found = true;
                    break;
                }
            }
            
            if (found) {
                renumberHrsReceiptEarnings();
                computeReceipt();
            }
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    /**
     * If found, removes HRS Receipt Deduction by its Move ID, renumbers deductions of receipt, and finally computes receipt.
     * @param moveId 
     */
    public void removeHrsReceiptDeduction(final int moveId) {
        try {
            boolean found = false;
            
            for (int i = 0; i < maHrsReceiptDeductions.size(); i++) {
                if (maHrsReceiptDeductions.get(i).getPayrollReceiptDeduction().getPkMoveId() == moveId) {
                    maHrsReceiptDeductions.remove(i);
                    found = true;
                    break;
                }
            }
            
            if (found) {
                renumberHrsReceiptDeductions();
                computeReceipt();
            }
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    /**
     * Compute receipt.
     * @throws Exception 
     */
    public void computeReceipt() throws Exception {
        // keep the following order of invocation of methods:
        computeEarnings();
        computePayrollReceiptDays();
        computeDeductions();
        computeEarningsExemption();
        computeReceiptTax();
        computeReceiptSsContribution();
    }
    
    public double getTotalEarnings() {
        double totalEarnings = 0;

        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            if (!hrsReceiptEarning.getPayrollReceiptEarning().isDeleted()) {
                totalEarnings += hrsReceiptEarning.getPayrollReceiptEarning().getAmount_r();
            }
        }

        return totalEarnings;
    }

    public double getTotalEarningsExempt() {
        double totalEarningsExept = 0;

        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            if (!hrsReceiptEarning.getPayrollReceiptEarning().isDeleted()) {
                totalEarningsExept += hrsReceiptEarning.getPayrollReceiptEarning().getAmountExempt();
            }
        }

        return totalEarningsExept;
    }

    public double getTotalEarningsTaxable() {
        double totalEarningsTaxed = 0;

        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            if (!hrsReceiptEarning.getPayrollReceiptEarning().isDeleted()) {
                totalEarningsTaxed += hrsReceiptEarning.getPayrollReceiptEarning().getAmountTaxable();
            }
        }

        return totalEarningsTaxed;
    }
    
    public double getTotalEarningsDependentsDaysWorked() {
        double totalEarnings = 0;

        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            if (hrsReceiptEarning.getEarning().isDaysWorked()) {
                totalEarnings += hrsReceiptEarning.getPayrollReceiptEarning().getAmount_r();   
            }
        }

        return totalEarnings;
    }

    public double getTotalDeductions() {
        double totalDeductions = 0;

        for (SHrsReceiptDeduction hrsReceiptDeduction : maHrsReceiptDeductions) {
            if (!hrsReceiptDeduction.getPayrollReceiptDeduction().isDeleted()) {
                totalDeductions += hrsReceiptDeduction.getPayrollReceiptDeduction().getAmount_r();
            }
        }

        return totalDeductions;
    }
    
    public double getBenefitValue(final int benefitType, final int benefitAnniversary, final int benefitYear) {
        double value = 0;
        
        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            if (SLibUtils.compareKeys(new int[] { benefitType, benefitAnniversary, benefitYear }, new int[] { hrsReceiptEarning.getPayrollReceiptEarning().getFkBenefitTypeId(), 
                hrsReceiptEarning.getPayrollReceiptEarning().getBenefitsAnniversary(), hrsReceiptEarning.getPayrollReceiptEarning().getBenefitsYear() })) {
                value += hrsReceiptEarning.getPayrollReceiptEarning().getUnitsAlleged();
            }
        }
        
        return value;
    }
    
    public double getBenefitAmount(final int benefitType, final int benefitAnniversary, final int benefitYear) {
        double amount = 0;
        
        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            if (SLibUtils.compareKeys(new int[] { benefitType, benefitAnniversary, benefitYear }, new int[] { hrsReceiptEarning.getPayrollReceiptEarning().getFkBenefitTypeId(), 
                hrsReceiptEarning.getPayrollReceiptEarning().getBenefitsAnniversary(), hrsReceiptEarning.getPayrollReceiptEarning().getBenefitsYear() })) {
                amount += hrsReceiptEarning.getPayrollReceiptEarning().getAmount_r();
            }
        }
        
        return amount;
    }
    
    public double calculateBenefit(final SDbEarning benefitEarning, final double benefitDays, final double vacationBonusPct) {
        double units = benefitEarning.computeEarningUnits(benefitDays, moHrsPayroll.getPayroll());
        return SLibUtils.roundAmount(units * moPayrollReceipt.getPaymentDaily() * (benefitEarning.getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_VAC_BON ? vacationBonusPct : 1d));
    }
    
    public SDbAbsenceConsumption createAbsenceConsumption(final SDbAbsence absence, final Date dateStart, final Date dateEnd, final int effectiveDays) throws Exception {
        if (absence.getPkEmployeeId() != moHrsEmployee.getEmployee().getPkEmployeeId()) {
            throw new Exception("El empleado de la ausencia (ID = " + absence.getPkEmployeeId() + ") no es el empleado del recibo (" + moHrsEmployee.getEmployee().getPkEmployeeId() + ").");
        }
        
        SDbAbsenceConsumption absenceConsumption = new SDbAbsenceConsumption();
        
        absenceConsumption.setPkEmployeeId(absence.getPkEmployeeId());
        absenceConsumption.setPkAbsenceId(absence.getPkAbsenceId());
        absenceConsumption.setPkConsumptionId(absence.getActualNextConsumptionId(moHrsEmployee));
        absenceConsumption.setDateStart(dateStart);
        absenceConsumption.setDateEnd(dateEnd);
        absenceConsumption.setEffectiveDays(effectiveDays);
        //absenceConsumption.setDeleted(...);
        absenceConsumption.setFkReceiptPayrollId(moHrsPayroll.getPayroll().getPkPayrollId());
        absenceConsumption.setFkReceiptEmployeeId(absence.getPkEmployeeId());
        //absenceConsumption.setFkUserInsertId(...);
        //absenceConsumption.setFkUserUpdateId(...);
        //absenceConsumption.setTsUserInsert(...);
        //absenceConsumption.setTsUserUpdate(...);
        
        absenceConsumption.setParentAbsence(absence);
        
        return absenceConsumption;
    }
    
    public boolean validateAbsenceConsumption(SDbAbsenceConsumption absenceConsumption) throws Exception {
        // check earning for payable absence:
        
        if (absenceConsumption.getParentAbsence().isXtaAbsenceTypePayable()) {
            boolean found = false;
            
            for (SDbEarning earning : moHrsPayroll.getEarnings()) {
                if (SLibUtils.compareKeys(earning.getAbsenceTypeKey(), absenceConsumption.getParentAbsence().getAbsenceTypeKey())) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                throw new Exception("No existen percepciones configuradas para la incidencia tipo '" + absenceConsumption.getParentAbsence().getXtaAbsenceType() + "', "
                        + "clase '" + absenceConsumption.getParentAbsence().getXtaAbsenceClass() + "', que es pagable.");
            }
        }
        
        // validate absence:
        
        SDbAbsence absence = absenceConsumption.getParentAbsence();
        
        if (absenceConsumption.getEffectiveDays() > absenceConsumption.getCalendarDays()) {
            throw new Exception("Los " + absenceConsumption.getEffectiveDays() + " días " + SDbAbsenceClass.EFFECTIVE + " a consumir de la incidencia '" + absence.composeAbsenceDescription() + "'\n"
                    + "son mayores a los " + absenceConsumption.getCalendarDays()+ " días " + SDbAbsenceClass.CALENDAR + " del consumo.");
        }

        int consumptionConsumedDays = absence.getActualConsumedDays(moHrsEmployee);
        int consumptionRemainingDays = absence.getEffectiveDays() - consumptionConsumedDays;
        
        if (consumptionRemainingDays <= 0) {
            throw new Exception("Se han consumido ya " + consumptionConsumedDays + " de los " + absence.getEffectiveDays() + " días " + SDbAbsenceClass.EFFECTIVE + " de la incidencia '" + absence.composeAbsenceDescription() + "'.");
        }
        
        // validate absence consumption:
        
        SHrsEmployeeDays hrsEmployeeDays = moHrsEmployee.createEmployeeDays(); // convenience variable
        double receiptCalendarDays = hrsEmployeeDays.getReceiptDays();
        double employeeWorkableCalendarDays = hrsEmployeeDays.getWorkableCalendarDaysPayroll();
        double employeeWorkableBusinessDays = hrsEmployeeDays.getWorkableBusinessDaysPayroll();
        int consumptionActualBusinessDays = SHrsUtils.countBusinessDays(absenceConsumption.getDateStart(), absenceConsumption.getDateEnd(), moHrsPayroll.getWorkingDaySettings(), moHrsPayroll.getHolidays());
        
        if (moHrsPayroll.getPayroll().isPayrollNormal()) {
            if (absence.consumesCalendarDays()) {
                // validate calendar days:
                if (employeeWorkableCalendarDays <= 0) {
                    throw new Exception("El recibo de nómina tiene " + employeeWorkableCalendarDays + " días " + SDbAbsenceClass.CALENDAR + " disponibles "
                            + "para consumir la incidencia '" + absence.composeAbsenceDescription() + "'.");
                }
                else if (absenceConsumption.getEffectiveDays() > employeeWorkableCalendarDays) {
                    throw new Exception("Los " + absenceConsumption.getEffectiveDays() + " días " + SDbAbsenceClass.EFFECTIVE + " a consumir de la incidencia '" + absence.composeAbsenceDescription() + "'\n"
                            + "son mayores a los " + employeeWorkableCalendarDays + " días " + SDbAbsenceClass.CALENDAR + " disponibles del recibo de nómina.");
                }
            }
            else {
                // validate instead business days:
                if (employeeWorkableBusinessDays <= 0) {
                    throw new Exception("El recibo de nómina tiene " + employeeWorkableBusinessDays + " días " + SDbAbsenceClass.BUSINESS + " disponibles "
                            + "para consumir la incidencia '" + absence.composeAbsenceDescription() + "'.");
                }
                else if (consumptionActualBusinessDays > employeeWorkableBusinessDays) {
                    throw new Exception("Los " + consumptionActualBusinessDays + " días " + SDbAbsenceClass.BUSINESS + " a consumir de la incidencia '" + absence.composeAbsenceDescription() + "'\n"
                            + "son mayores a los " + employeeWorkableBusinessDays + " días " + SDbAbsenceClass.BUSINESS + " disponibles del recibo de nómina.");
                }
                else if (absenceConsumption.getEffectiveDays() > employeeWorkableBusinessDays) {
                    throw new Exception("Los días " + absenceConsumption.getEffectiveDays() + " " + SDbAbsenceClass.EFFECTIVE + " a consumir de la incidencia '" + absence.composeAbsenceDescription() + "'\n"
                            + "son mayores a los " + employeeWorkableBusinessDays + " días " + SDbAbsenceClass.BUSINESS + " disponibles del recibo de nómina.");
                }
            }
            
            // validate total consumed effective days of all absences:
            
            int totalEffectiveDays = 0;
            
            for (SDbAbsenceConsumption consumption : maAbsenceConsumptions) {
                totalEffectiveDays += consumption.getEffectiveDays();
            }
            
            totalEffectiveDays += absenceConsumption.getEffectiveDays();
            
            if (totalEffectiveDays > receiptCalendarDays) {
                throw new Exception("Los " + totalEffectiveDays + " días " + SDbAbsenceClass.CALENDAR + " consumidos de todas las ausencias "
                        + "son mayores a los " + receiptCalendarDays + " días " + SDbAbsenceClass.CALENDAR + " del recibo de nómina.");
            }
        }

        return true;
    }
    
    private void addAbsenceConsumption(final SDbAbsenceConsumption absenceConsumptionToAdd) throws Exception {
        // add consumption:
        
        if (validateAbsenceConsumption(absenceConsumptionToAdd)) {
            maAbsenceConsumptions.add(absenceConsumptionToAdd);

            if (moPayrollReceipt != null) {
                computeReceipt();
            }
        }
    }
    
    private void removeAbsenceConsumption(final SDbAbsenceConsumption absenceConsumptionToRemove) throws Exception {
        // make sure that consumptions of the same absence are removed in reverse:
        
        int consumptionsCount = 0; // consumption count of the same absence
        int maxConsumptionId = 0;  // maximum consumption ID of the same absence

        for (SDbAbsenceConsumption absenceConsumption : maAbsenceConsumptions) {
            if (SLibUtils.compareKeys(absenceConsumption.getAbsenceKey(), absenceConsumptionToRemove.getAbsenceKey())) { // key: employee ID & absence ID
                consumptionsCount++;
                if (absenceConsumption.getPkConsumptionId() > maxConsumptionId) {
                    maxConsumptionId = absenceConsumption.getPkConsumptionId();
                }
            }
        }
        
        // remove consumption and exit loop:
        
        if (consumptionsCount > 0) {
            for (int i = 0; i < maAbsenceConsumptions.size(); i++) {
                if (SLibUtils.compareKeys(maAbsenceConsumptions.get(i).getPrimaryKey(), absenceConsumptionToRemove.getPrimaryKey())) { // key: employee ID & absence ID & consumption ID
                    if (consumptionsCount > 1 && maAbsenceConsumptions.get(i).getPkConsumptionId() != maxConsumptionId) {
                        throw new Exception("Los consumos de la misma ausencia deben ser removidos en orden inverso.");
                    }

                    maAbsenceConsumptions.remove(i);
                    break;
                }
            }
        }
    }
    
    /**
     * Adds or removes absence consumption.
     * @param absenceConsumption Absence consumption to add or remove.
     * @param add Indicator to add (<code>true</code>) or remove (<code>false</code>) absence consumption.
     * @throws Exception 
     */
    public void updateHrsReceiptEarningAbsence(SDbAbsenceConsumption absenceConsumption, boolean add) throws Exception {
        // update control of absence consumptions:
        
        if (add) {
            // add absence consumption:
            addAbsenceConsumption(absenceConsumption);
        }
        else {
            // remove absence consumption:
            removeAbsenceConsumption(absenceConsumption);
        }
        
        // create a fresh Employee Days instance:
        
        SHrsEmployeeDays hrsEmployeeDays = moHrsEmployee.createEmployeeDays(); // convenience variable
        
        // remove all absence earnings added by system:
        
        ArrayList<SHrsReceiptEarning> hrsReceiptEarningsToRemove = new ArrayList<>();
        
        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            if (hrsReceiptEarning.getPayrollReceiptEarning().isSystem() && hrsReceiptEarning.getEarning().isAbsence()) {
                hrsReceiptEarningsToRemove.add(hrsReceiptEarning);
            }
        }
        
        for (SHrsReceiptEarning hrsReceiptEarning : hrsReceiptEarningsToRemove) {
            removeHrsReceiptEarning(hrsReceiptEarning.getPayrollReceiptEarning().getPkMoveId());
        }
        
        // update ordinary wages and salaries:
        
        boolean found = false;
        double daysToBePaid = 0;
        SDbEarning earningNormal = moHrsPayroll.getEarning(moHrsPayroll.getModuleConfig().getFkEarningEarningId_n());
        
        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            // locate earning of ordinary wages and salaries, and compute days to be paid:
            
            if (hrsReceiptEarning.getEarning().getPkEarningId() == earningNormal.getPkEarningId()) {
                found = true;
                
                double daysBeingPaid = hrsReceiptEarning.getPayrollReceiptEarning().getUnitsAlleged();
                
                if (add) {
                    // add absence consumption, then subtract payable days:
                    
                    daysToBePaid = daysBeingPaid - absenceConsumption.getEffectiveDays();
                    
                    if (daysToBePaid < 0) {
                        daysToBePaid = 0;
                    }
                }
                else {
                    // remove absence consumption, then add payable days:
                    
                    daysToBePaid = daysBeingPaid + absenceConsumption.getEffectiveDays();
                    
                    if (daysToBePaid > hrsEmployeeDays.getReceiptDaysWorked()) {
                        daysToBePaid = hrsEmployeeDays.getReceiptDaysWorked();
                    }
                }
                
                if (daysToBePaid == 0) {
                    removeHrsReceiptEarning(hrsReceiptEarning.getPayrollReceiptEarning().getPkMoveId());
                }
                else {
                    double units = earningNormal.computeEarningUnits(daysToBePaid, moHrsPayroll.getPayroll());
                    double amount = earningNormal.computeEarningAmount(units, hrsReceiptEarning.getPayrollReceiptEarning().getAmountUnitary());

                    hrsReceiptEarning.getPayrollReceiptEarning().setUnitsAlleged(daysToBePaid);
                    hrsReceiptEarning.getPayrollReceiptEarning().setUnits(units);
                    hrsReceiptEarning.getPayrollReceiptEarning().setAmount_r(amount);

                    replaceHrsReceiptEarning(hrsReceiptEarning.getPayrollReceiptEarning().getPkMoveId(), hrsReceiptEarning);
                }

                break;
            }
        }

        if (!found && hrsEmployeeDays.getReceiptDaysWorked() > 0) {
            SDbPayrollReceiptEarning payrollReceiptEarning = moHrsPayroll.createPayrollReceiptEarning(
                    this, earningNormal, null, 
                    hrsEmployeeDays.getReceiptDaysWorked(), 0, true, 
                    0, 0, maHrsReceiptEarnings.size() + 1);

            SHrsReceiptEarning hrsReceiptEarning = new SHrsReceiptEarning();
            hrsReceiptEarning.setHrsReceipt(this);
            hrsReceiptEarning.setEarning(earningNormal);
            hrsReceiptEarning.setPayrollReceiptEarning(payrollReceiptEarning);

            addHrsReceiptEarning(hrsReceiptEarning);
        }

        // create (again) all absence earnings (by system):

        for (SHrsReceiptEarning hrsReceiptEarning : moHrsPayroll.createHrsReceiptEarningAbsences(this)) {
            addHrsReceiptEarning(hrsReceiptEarning);
        }
    }
    
    private SHrsDaysByPeriod createHrsDaysByPeriod(final int year, final int period) {
        int daysPeriod = SLibTimeUtils.digestDate(SLibTimeUtils.getEndOfMonth(SLibTimeUtils.createDate(year, period)))[2];
        int daysPeriodPayroll = 0;
        
        for (SDbEmployeeHireLog hireLog : moHrsEmployee.getEmployeeHireLogsPayroll()) {
            Date payrollStart = moHrsPayroll.getPayroll().getDateStart();
            Date payrollEnd = moHrsPayroll.getPayroll().getDateEnd();
            Date dateStart = hireLog.getDateHire().compareTo(payrollStart) <= 0 ? payrollStart : hireLog.getDateHire();
            Date dateEnd = hireLog.getDateDismissal_n() == null ? payrollEnd : hireLog.getDateDismissal_n().compareTo(payrollEnd) >= 0 ? payrollEnd : hireLog.getDateDismissal_n();
            
            int periodDays = (int) SLibTimeUtils.getDaysDiff(dateEnd, dateStart);
            
            for (int index = 0; index <= periodDays; index++) {
                if (SLibTimeUtils.isBelongingToPeriod(SLibTimeUtils.addDate(dateStart, 0, 0, index), year, period)) {
                    daysPeriodPayroll++;
                }
            }
        }
        
        return new SHrsDaysByPeriod(year, period, daysPeriod, daysPeriodPayroll);
    }
    
    private void computeHrsDaysByPeriod(SHrsEmployeeDays hrsEmployeeDays) throws Exception {
        int year; // year
        int period; // month
        
        // Compute days by period previous:
        
        year = hrsEmployeeDays.getPayrollYear() - (hrsEmployeeDays.getPayrollPeriod() == 1 ? 1 : 0);
        period = (hrsEmployeeDays.getPayrollPeriod() == 1 ? SLibTimeConsts.MONTHS : hrsEmployeeDays.getPayrollPeriod() - 1);
        
        SHrsDaysByPeriod hrsDaysPrev = createHrsDaysByPeriod(year, period);
        
        // Compute days by period current:
        
        year = hrsEmployeeDays.getPayrollYear();
        period = hrsEmployeeDays.getPayrollPeriod();
        
        SHrsDaysByPeriod hrsDaysCurr = createHrsDaysByPeriod(year, period);
        
        // Compute days by period next:
        
        year = hrsEmployeeDays.getPayrollYear() + (hrsEmployeeDays.getPayrollPeriod() == SLibTimeConsts.MONTHS ? 1 : 0);
        period = (hrsEmployeeDays.getPayrollPeriod() == SLibTimeConsts.MONTHS ? 1 : hrsEmployeeDays.getPayrollPeriod() + 1);
        
        SHrsDaysByPeriod hrsDaysNext = createHrsDaysByPeriod(year, period);
        
        // Estimate unpaid days:
        
        int daysNotWorkedNotPaid = 0;
        int daysIncapacityNotPaid = 0;
        
        for (SDbAbsenceConsumption absenceConsumption : maAbsenceConsumptions) {
            if (!absenceConsumption.getParentAbsence().isXtaAbsenceTypePayable()) {
                daysNotWorkedNotPaid += absenceConsumption.getEffectiveDays(); // days not worked and not paid (DNWNP)

                if (absenceConsumption.getParentAbsence().isDisability()) {
                    daysIncapacityNotPaid += absenceConsumption.getEffectiveDays(); // days of incapacity not paid (DINP)
                }
            }
        }
        
        // Set DNWNP and DINP for previous period:
        hrsDaysPrev.setDaysNotPaid(daysNotWorkedNotPaid, daysIncapacityNotPaid);
        
        // Get pending DNWNP and DINP to set:
        daysNotWorkedNotPaid -= hrsDaysPrev.getDaysNotWorkedNotPaid();
        daysIncapacityNotPaid -= hrsDaysPrev.getDaysIncapacityNotPaid();
        
        // Set DNWNP and DINP for current period:
        hrsDaysCurr.setDaysNotPaid(daysNotWorkedNotPaid, daysIncapacityNotPaid);
        
        // Get pending DNTNP and DINP days to set:
        daysNotWorkedNotPaid -= hrsDaysCurr.getDaysNotWorkedNotPaid();
        daysIncapacityNotPaid -= hrsDaysCurr.getDaysIncapacityNotPaid();
        
        // Set DNWNP and DINP for next period:
        hrsDaysNext.setDaysNotPaid(daysNotWorkedNotPaid, daysIncapacityNotPaid);
        
        moHrsEmployee.setHrsDaysPrev(hrsDaysPrev);
        moHrsEmployee.setHrsDaysCurr(hrsDaysCurr);
        moHrsEmployee.setHrsDaysNext(hrsDaysNext);
    }
    
    public void computePayrollReceiptDays() throws Exception {
        // Compute moPayrollReceipt values related to days:
        
        SDbPayroll payroll = moHrsPayroll.getPayroll(); // convenience variable
        SHrsEmployeeDays hrsEmployeeDays = moHrsEmployee.createEmployeeDays(); // convenience variable
        
        moPayrollReceipt.setFactorCalendar(payroll.getFactorCalendar()); // just informative
        moPayrollReceipt.setFactorDaysPaid(payroll.getFactorDaysPaid()); // just informative
        moPayrollReceipt.setReceiptDays(hrsEmployeeDays.getReceiptDays()); // just informative
        moPayrollReceipt.setWorkingDays(hrsEmployeeDays.getReceiptWorkingDays()); // just informative
        moPayrollReceipt.setDaysWorked(hrsEmployeeDays.getReceiptDaysWorked()); // just informative
        moPayrollReceipt.setDaysHiredPayroll(hrsEmployeeDays.getDaysHiredPayroll());
        moPayrollReceipt.setDaysHiredAnnual(hrsEmployeeDays.getDaysHiredAnnual());
        moPayrollReceipt.setDaysIncapacityNotPaidPayroll(hrsEmployeeDays.getDaysIncapacityNotPaidPayroll());
        moPayrollReceipt.setDaysIncapacityNotPaidAnnual(hrsEmployeeDays.getDaysIncapacityNotPaidAnnual());
        moPayrollReceipt.setDaysNotWorkedButPaid(hrsEmployeeDays.getDaysNotWorkedButPaidPayroll());
        moPayrollReceipt.setDaysNotWorkedNotPaid(hrsEmployeeDays.getDaysNotWorkedNotPaidPayroll());
        moPayrollReceipt.setDaysNotWorked_r(hrsEmployeeDays.getNotWorkedDaysPayroll());
        moPayrollReceipt.setDaysToBePaid_r(computeDaysToBePaid(maHrsReceiptEarnings));
        moPayrollReceipt.setDaysPaid(computeDaysPaid(maHrsReceiptEarnings));
        moPayrollReceipt.setPayrollTaxableDays_r(hrsEmployeeDays.getTaxableDaysPayroll());
        moPayrollReceipt.setAnnualTaxableDays_r(hrsEmployeeDays.getTaxableDaysAnnual());
        
        computeHrsDaysByPeriod(hrsEmployeeDays);
    }
    
    public SHrsReceipt clone() throws CloneNotSupportedException {
       SHrsReceipt clone = new SHrsReceipt(this.getHrsPayroll());

       clone.setHrsEmployee(this.getHrsEmployee()); // same instance, don't clone
       clone.setHrsBenefitsManager(this.getHrsBenefitsManager()); // same instance, don't clone
       clone.setPayrollReceipt(this.getPayrollReceipt().clone());

       for (SDbAbsenceConsumption absenceConsumption : this.getAbsenceConsumptions()) {
           clone.getAbsenceConsumptions().add(absenceConsumption.clone());
       }

       for (SHrsReceiptEarning hrsReceiptEarning : this.getHrsReceiptEarnings()) {
           clone.getHrsReceiptEarnings().add(hrsReceiptEarning.clone());
       }

       for (SHrsReceiptDeduction hrsReceiptDeduction : this.getHrsReceiptDeductions()) {
           clone.getHrsReceiptDeductions().add(hrsReceiptDeduction.clone());
       }

       for (SHrsBenefit hrsBenefit : this.getHrsBenefits()) {
           clone.getHrsBenefits().add(hrsBenefit.clone());
       }

       for (SHrsReceiptEarning hrsReceiptEarning : clone.getHrsReceiptEarnings()) {
           hrsReceiptEarning.setHrsReceipt(clone);
       }

       for (SHrsReceiptDeduction hrsReceiptDeduction : clone.getHrsReceiptDeductions()) {
           hrsReceiptDeduction.setHrsReceipt(clone);
       }

       clone.getHrsPayroll().replaceHrsReceipt(clone, false);

       return clone;
    }
}

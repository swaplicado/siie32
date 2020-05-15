/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModSysConsts;
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
 *
 * @author Néstor Ávalos, Juan Barajas, Sergio Flores
 * 2019-01-29, Sergio Flores: Corrección al cálculo de parte exenta en percepciones. Debe estar en en función de UMA desde 2017.
 * 2019-06-07, Sergio Flores: Implementación compensación Subsidio para el empleo pagado en exceso contra ISR.
 *  Al implementarse el dato informativo en CFDI de Subsdio para el empleo causado, la conmpensación del pago en exceso dejó de hacerse contra ISR.
 */
public class SHrsReceipt {

    private SHrsPayroll moHrsPayroll;
    private SHrsEmployee moHrsEmployee;
    private SDbPayrollReceipt moPayrollReceipt;
    private final ArrayList<SDbAbsenceConsumption> maAbsenceConsumptions;
    private final ArrayList<SHrsReceiptEarning> maHrsReceiptEarnings;
    private final ArrayList<SHrsReceiptDeduction> maHrsReceiptDeductions;
    private final ArrayList<SHrsBenefit> maHrsBenefits;
    private ArrayList<SDbAbsence> absencesFromClock;

    public SHrsReceipt() {
        moHrsPayroll = null;
        moHrsEmployee = null;
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
     * Gets amount total taxed amount of earnings of standard calculation of income tax.
     * @return Total amount of earnings of standard calculation of income tax.
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
     * Gets amount total taxed amount of earnings configured based on articule 174 RLISR.
     * @return Total amount of earnings configured based on articule 174 RLISR.
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
     * Computes all earnings of this receipt.
     */
    private void computeEarnings() {
        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            hrsReceiptEarning.computeEarning();
        }
    }

    /**
     * Computes altogether the hrsReceiptEarnings of the given array of earnings as a group.
     * @param hrsReceiptEarnings Array of earnings. At least one earning is expected.
     * @throws Exception 
     */
    private void computeEarningsExemptionGroup(final ArrayList<SHrsReceiptEarning> hrsReceiptEarnings) throws Exception {
        /*
        EARNING EXEMPTION TYPES IN CONFIGURATION OF EARNINGS:
        
        Earning exemption types for SDbEarning.getFkEarningExemptionTypeId() can be:
            a) NON: not applicable (note that 'NON' is misspelled!)
            b) PER: Minimum Wage Percentage
            c) MWZ_GBL: Minimum Wage Global (that means "for the whole year"
            d) MWZ_EVT: Minimum Wage Event
            e) MWZ_SEN: Minimum Wage Seniority
        
        Earning exemption types for SDbEarning.getFkEarningExemptionTypeYearId(), identified in GUI as "optional", can be:
            a) NON: not applicable (note that 'NON' is misspelled!)
            b) MWZ_GBL: Minimum Wage Global (that means "for the whole year")
        */
        
        // get earning related to given group of receipt earnings:
        SDbEarning earning = hrsReceiptEarnings.get(0).getEarning();
        
        // check if exemption apply (standard and optional settings):
        boolean applyExemptionStd = earning.getFkEarningExemptionTypeId() != SModSysConsts.HRSS_TP_EAR_EXEM_NON; // variable improves readability!
        boolean applyExemptionStdGlobal = earning.getFkEarningExemptionTypeId() == SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_GBL; // variable improves readability!
        boolean applyExemptionYearGlobal = earning.getFkEarningExemptionTypeYearId() == SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_GBL; // variable improves readability!
        double exemptionPayroll = 0; // preserve exemption granted in current payroll
        double exemptionGlobal = 0; // preserve exemption granted in other payrolls in current year

        // get accumulated exemption granted in current year if necessary:
        if (applyExemptionStdGlobal || applyExemptionYearGlobal) {
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
            if (applyExemptionStd) {
                double exemptionProposed = 0;
                double exemptionLimit = 0;
                
                switch (earning.getFkEarningExemptionTypeId()) {
                    case SModSysConsts.HRSS_TP_EAR_EXEM_PER: // Percentage
                        // estimate exemption proposed and exemption limit:
                        if (moPayrollReceipt.getEffectiveSalary(payroll.isFortnightStandard()) <= payroll.getMwzWage()) { // salary cannot never be less than minimum wage, but just in case
                            exemptionProposed = SLibUtils.roundAmount(earning.getExemptionSalaryEqualsMwzPercentage() * payrollReceiptEarning.getAmount_r());
                            exemptionLimit = SLibUtils.roundAmount(earning.getExemptionSalaryEqualsMwzLimit() * payroll.getUmaAmount()); // formerly minimum wage was used
                        }
                        else {
                            exemptionProposed = SLibUtils.roundAmount(earning.getExemptionSalaryGreaterMwzPercentage() * payrollReceiptEarning.getAmount_r());
                            exemptionLimit = SLibUtils.roundAmount(earning.getExemptionSalaryGreaterMwzLimit() * payroll.getUmaAmount()); // formerly minimum wage was used
                        }
                        break;

                    case SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_GBL: // Minimum Wage Global
                    case SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_EVT: // Minimum Wage Event
                        // estimate exemption proposed and exemption limit:
                        exemptionProposed = SLibUtils.roundAmount(earning.getExemptionMwz() * payroll.getUmaAmount()); // formerly minimum wage was used
                        exemptionLimit = exemptionProposed;
                        break;

                    case SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_SEN: // Minimum Wage Seniority
                        // compute exact seniority:
                        int years = moHrsEmployee.getSeniority();
                        int yearDays = (int) SLibTimeUtils.getDaysDiff(payroll.getDateEnd(), SLibTimeUtils.addDate(moHrsEmployee.getEmployee().getDateBenefits(), years, 0, 0));
                        double seniority = (double) years + ((double) yearDays / SHrsConsts.YEAR_DAYS);

                        // estimate exemption proposed and exemption limit:
                        exemptionProposed = SLibUtils.roundAmount(earning.getExemptionMwz() * payroll.getUmaAmount() * seniority); // formerly minimum wage was used
                        exemptionLimit = exemptionProposed;
                        break;

                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }

                // adjust exemption:

                exemption = SLibUtils.roundAmount(exemptionProposed - exemptionPayroll - exemptionGlobal);

                if (exemption < 0) {
                    exemption = 0;
                }
                else if (exemption > 0) {
                    if (exemptionLimit > 0 && exemption > exemptionLimit) {
                        exemption = exemptionLimit;
                    }

                    if (exemption > payrollReceiptEarning.getAmount_r()) {
                        exemption = payrollReceiptEarning.getAmount_r();
                    }
                }

                // accumulate payroll and global exemption:
                exemptionPayroll = SLibUtils.roundAmount(exemptionPayroll + exemption);
                exemptionGlobal = SLibUtils.roundAmount(exemptionGlobal + exemption);
            }

            // set earning exemption:
            payrollReceiptEarning.setAmountExempt(exemption);
            payrollReceiptEarning.setAmountTaxable(SLibUtils.roundAmount(payrollReceiptEarning.getAmount_r() - exemption));
        }
    }
    
    /**
     * Computes exemption of all earnings of this receipt by grouping them in blocks of arrays.
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
     * Computes all deductions of this receipt.
     */
    private void computeDeductions() {
        for (SHrsReceiptDeduction hrsReceiptDeduction : maHrsReceiptDeductions) {
            hrsReceiptDeduction.computeDeduction();
        }
    }

    /**
     * Computes, adding ore removing, earning (as other payment) of $0.01 if tax subsidy was totally offset by tax.
     * Otherwise this earning (as other payment) is removed when existing.
     * @param isSubsidyOffsetTotally
     * @param dbEarningOther
     * earningOther Exception 
     */
    private void computeReceiptTaxSubsidyAsOtherPayment(final boolean isSubsidyOffsetTotally, final SDbEarning earningOtherPayment, final SHrsEmployeeDays hrsEmployeeDays) throws Exception {
        int countSubsidy = 0;
        SHrsReceiptEarning hrsReceiptEarningPrevious = null;
        
        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            SDbPayrollReceiptEarning payrollReceiptEarning = hrsReceiptEarning.getPayrollReceiptEarning();
            
            if (payrollReceiptEarning.getFkEarningTypeId() == SModSysConsts.HRSS_TP_EAR_OTH && payrollReceiptEarning.isAutomatic()) {
                if (++countSubsidy > 1) {
                    throw new Exception("¡No puede haber más de una percepcion 'Subsidio para el empleo' en el recibo!");
                }
                
                // prepare for update or removal of existing subsidy earning:
                hrsReceiptEarningPrevious = hrsReceiptEarning;
                break;
            }
        }
        
        if (hrsReceiptEarningPrevious != null) {
            maHrsReceiptEarnings.remove(hrsReceiptEarningPrevious);
        }
        
        if (isSubsidyOffsetTotally) {
            // subsidy compensated:
            
            double amountRequired = 0.01;
            SDbPayrollReceiptEarning payrollReceiptEarningOther = moHrsPayroll.createPayrollReceiptEarning(
                    this, earningOtherPayment, hrsEmployeeDays, null, 
                    1, amountRequired, true, 
                    0, 0, maHrsReceiptEarnings.size() + 1);
            
            payrollReceiptEarningOther.setAmountExempt(amountRequired); // subsidy is exempt
            payrollReceiptEarningOther.setAmountTaxable(0);
            
            SHrsReceiptEarning hrsReceiptEarningNew = new SHrsReceiptEarning();
            hrsReceiptEarningNew.setHrsReceipt(this);
            hrsReceiptEarningNew.setEarning(earningOtherPayment);
            hrsReceiptEarningNew.setPayrollReceiptEarning(payrollReceiptEarningOther);

            maHrsReceiptEarnings.add(hrsReceiptEarningNew);
        }
        
        if (hrsReceiptEarningPrevious != null || isSubsidyOffsetTotally) {
            renumberHrsReceiptEarnings();
        }
    }

    private void computeReceiptTax() throws Exception {
        // Clear tax and subsidy existing computations from receipt:

        moPayrollReceipt.setPayrollFactorTax(0);
        moPayrollReceipt.setPayrollTaxAssessed(0);
        moPayrollReceipt.setPayrollTaxCompensated(0);
        moPayrollReceipt.setPayrollTaxPending_r(0);
        moPayrollReceipt.setPayrollTaxPayed(0);
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

        double annualTaxCompensated = 0; // sumatory excluding tax compensated in current payroll
        double annualTaxPayed = 0;       // actually withheld from employee: sumatory excluding tax payed in current payroll
        double annualSubsidyCompensated = 0; // sumatory excluding subsidy compensated in current payroll
        double annualSubsidyPayed = 0;       // actually payed to employee: sumatory excluding subsidy payed in current payroll
        double taxAssessed = 0;
        double subsidyAssessed = 0;
        double payrollTaxAssessed = 0;
        double payrollSubsidyAssessedGross = 0;
        double payrollSubsidyAssessed = 0;
        
        SHrsReceiptEarning hrsReceiptEarningSubsidyNew = null;
        SHrsReceiptDeduction hrsReceiptDeductionTaxNew = null;
        
        // Check if tax computation is needed:
        
        SDbDeduction deductionTax = null;
        SDbEarning earningSubsidy = null;
        SDbEarning earningSubsidyOtherPayment = null;
        SDbPayroll payroll = moHrsPayroll.getPayroll();
        boolean computeTax = SLibUtils.belongsTo(payroll.getFkTaxComputationTypeId(), new int[] { SModSysConsts.HRSS_TP_TAX_COMP_PAY, SModSysConsts.HRSS_TP_TAX_COMP_ANN });
        boolean computeSubsidy = computeTax && payroll.isTaxSubsidy() && !moHrsEmployee.getEmployee().isAssimilable(); // assimilables are not elegible for subsidy

        if (computeTax) {
            // Validate configuration of deduction for tax:

            if (moHrsPayroll.getConfig().getFkDeductionTaxId_n() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Deducción de impuesto)");
            }

            deductionTax = moHrsPayroll.getDeduction(moHrsPayroll.getConfig().getFkDeductionTaxId_n());
            if (deductionTax == null) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Deducción de impuesto)");
            }

            if (deductionTax.getFkDeductionTypeId() != SModSysConsts.HRSS_TP_DED_TAX) {
                throw new Exception(SLibConsts.ERR_MSG_WRONG_TYPE + " (Tipo de deducción en deducción de impuesto)");
            }

            if (payroll.getFkTaxId() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Tabla de impuesto)");
            }

            SDbTaxTable taxTable = moHrsPayroll.getTaxTable(payroll.getFkTaxId());
            if (taxTable == null)  {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Tabla de impuesto)");
            }

            // Validate configuration of earning for subsidy:

            if (moHrsPayroll.getConfig().getFkEarningTaxSubsidyId_n() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Percepción de subsidio para el empleo)");
            }

            earningSubsidy = moHrsPayroll.getEarning(moHrsPayroll.getConfig().getFkEarningTaxSubsidyId_n());
            if (earningSubsidy == null) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Percepción de subsidio para el empleo)");
            }

            if (earningSubsidy.getFkEarningTypeId() != SModSysConsts.HRSS_TP_EAR_TAX_SUB) {
                throw new Exception(SLibConsts.ERR_MSG_WRONG_TYPE + " (Tipo de percepción en percepción de subsidio para el empleo)");
            }

            if (payroll.getFkTaxSubsidyId() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Tabla de subsidio para el empleo)");
            }

            SDbTaxSubsidyTable subsidyTable = moHrsPayroll.getTaxSubsidyTable(payroll.getFkTaxSubsidyId());
            if (subsidyTable == null)  {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Tabla de subsidio para el empleo)");
            }

            // Validate configuration of earning for compensated subsidy:

            if (moHrsPayroll.getConfig().getFkEarningTaxSubsidyCompensatedId_n() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Percepción de subsidio para el empleo compensado)");
            }

            earningSubsidyOtherPayment = moHrsPayroll.getEarning(moHrsPayroll.getConfig().getFkEarningTaxSubsidyCompensatedId_n());
            if (earningSubsidyOtherPayment == null) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Percepción de subsidio para el empleo compensado)");
            }

            if (earningSubsidyOtherPayment.getFkEarningTypeId() != SModSysConsts.HRSS_TP_EAR_OTH) {
                throw new Exception(SLibConsts.ERR_MSG_WRONG_TYPE + " (Tipo de percepción en percepción de subsidio para el empleo compensado)");
            }

            // Obtain taxable earnings and, if necessary, accumulated annual tax and subsidy:
            
            double earningsTaxableStd = 0;
            double earningsTaxableArt174 = 0;

            switch (payroll.getFkTaxComputationTypeId()) {
                case SModSysConsts.HRSS_TP_TAX_COMP_PAY: // Payroll
                    earningsTaxableStd = SLibUtils.roundAmount(getTaxedEarningsStd());
                    earningsTaxableArt174 = SLibUtils.roundAmount(getTaxedEarningsArt174());
                    break;

                case SModSysConsts.HRSS_TP_TAX_COMP_ANN: // Annual
                    earningsTaxableStd = SLibUtils.roundAmount(getTaxedEarningsStd() + moHrsEmployee.getAccummulatedTaxableEarnings());
                    earningsTaxableArt174 = SLibUtils.roundAmount(getTaxedEarningsArt174() + moHrsEmployee.getAccummulatedTaxableEarningsAlt());
                    
                    annualTaxCompensated = moHrsEmployee.getAnnualTaxCompensated(); // should be equal to compensated annual subsidy
                    
                    SHrsAccumulatedDeduction hrsAccumulatedTax = moHrsEmployee.getHrsAccumulatedDeductionByType(SModSysConsts.HRSS_TP_DED_TAX);
                    if (hrsAccumulatedTax != null) {
                        annualTaxPayed = SLibUtils.roundAmount(hrsAccumulatedTax.getAcummulated());
                    }
                    
                    annualSubsidyCompensated = moHrsEmployee.getAnnualTaxSubsidyCompensated(); // should be equal to compensated annual tax

                    SHrsAccumulatedEarning hrsAccumulatedSub = moHrsEmployee.getHrsAccumulatedEarningByType(SModSysConsts.HRSS_TP_EAR_TAX_SUB);
                    if (hrsAccumulatedSub != null) {
                        annualSubsidyPayed = SLibUtils.roundAmount(hrsAccumulatedSub.getAcummulated());
                    }
                    break;

                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }

            // Define payroll and annual factors for adjusting tables of tax and subsidy:

            double tableFactorPayroll;
            double tableFactorAnnual;
            SHrsEmployeeDays hrsEmployeeDays = moHrsEmployee.createEmployeeDays();
            double yearDaysFactor = (double) SHrsConsts.YEAR_MONTHS / ((double) SHrsConsts.YEAR_DAYS + (SLibTimeUtils.isLeapYear(payroll.getFiscalYear()) ? 1d : 0d));
            
            if (payroll.isPayrollFortnightStandard()) {
                double yearDaysFactorStd = (double) SHrsConsts.YEAR_MONTHS / (double) SHrsConsts.YEAR_DAYS_FORTNIGHTS_FIXED;
                tableFactorPayroll = yearDaysFactorStd * (hrsEmployeeDays.getTaxableDaysPayroll() <= SHrsConsts.FORTNIGHT_FIXED_DAYS ? hrsEmployeeDays.getTaxableDaysPayroll() : SHrsConsts.FORTNIGHT_FIXED_DAYS);
                tableFactorAnnual = yearDaysFactor * hrsEmployeeDays.getTaxableDaysAnnual();
            }
            else {
                tableFactorPayroll = yearDaysFactor * hrsEmployeeDays.getTaxableDaysPayroll();
                tableFactorAnnual = yearDaysFactor * hrsEmployeeDays.getTaxableDaysAnnual();
            }
            
            moPayrollReceipt.setPayrollFactorTax(tableFactorPayroll);
            moPayrollReceipt.setAnnualFactorTax(tableFactorAnnual);
            
            double tableFactor;
            
            if (payroll.getFkTaxComputationTypeId() == SModSysConsts.HRSS_TP_TAX_COMP_ANN) {
                tableFactor = tableFactorAnnual;
            }
            else {
                tableFactor = tableFactorPayroll;
            }

            // Compute assessed and payable tax (payroll or annual, the one required):
            
            if (earningsTaxableStd > 0) {
                for (int row = 0; row < taxTable.getChildRows().size(); row++) {
                    SDbTaxTableRow taxTableRow = taxTable.getChildRows().get(row);
                    if (earningsTaxableStd >= SLibUtils.roundAmount(taxTableRow.getLowerLimit() * tableFactor) &&
                            (row + 1 == taxTable.getChildRows().size() || earningsTaxableStd < SLibUtils.roundAmount(taxTable.getChildRows().get(row + 1).getLowerLimit() * tableFactor))) {
                        taxAssessed = SLibUtils.roundAmount((earningsTaxableStd - SLibUtils.roundAmount(taxTableRow.getLowerLimit() * tableFactor)) * taxTableRow.getTaxRate() + taxTableRow.getFixedFee() * tableFactor);
                        break;
                    }
                }
            }

            if (earningsTaxableArt174 > 0) {
                double taxAssessedAlt = SHrsUtils.computeTaxAlt(taxTable, earningsTaxableArt174, moPayrollReceipt.getMonthlyPayment(), tableFactor);
                taxAssessed = SLibUtils.roundAmount(taxAssessed + taxAssessedAlt); // update assessed tax
            }

            payrollTaxAssessed = SLibUtils.roundAmount(taxAssessed - (annualTaxCompensated + annualTaxPayed));

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
                // Compute assessed and payable subsidy (payroll or annual, the one required):
                
                if (earningsTaxableStd > 0) {
                    for (int row = 0; row < subsidyTable.getChildRows().size(); row++) {
                        SDbTaxSubsidyTableRow subsidyTableRow = subsidyTable.getChildRows().get(row);
                        if (earningsTaxableStd >= subsidyTableRow.getLowerLimit() * tableFactor &&
                                (row + 1 == subsidyTable.getChildRows().size() || earningsTaxableStd < subsidyTable.getChildRows().get(row + 1).getLowerLimit() * tableFactor)) {
                            subsidyAssessed = SLibUtils.roundAmount(subsidyTableRow.getTaxSubsidy() * tableFactor);
                            break;
                        }
                    }
                }

                if (earningsTaxableArt174 > 0) {
                    double subsidyComputedAlt = SHrsUtils.computeTaxSubsidyAlt(subsidyTable, earningsTaxableArt174, moPayrollReceipt.getMonthlyPayment(), tableFactor);
                    subsidyAssessed = SLibUtils.roundAmount(subsidyAssessed + subsidyComputedAlt); // update assessed subsidy
                }
                
                // Set maximum tax subsidy in one single receipt according to new regulations as of January 2020:
                
                double maxSubsidyAssessed = subsidyTable.getChildRows().get(0).getTaxSubsidy();
                
                // Compute subsidy:
                
                payrollSubsidyAssessedGross = SLibUtils.roundAmount(subsidyAssessed - (annualSubsidyCompensated + annualSubsidyPayed));
                payrollSubsidyAssessed = payrollSubsidyAssessedGross <= maxSubsidyAssessed ? payrollSubsidyAssessedGross : maxSubsidyAssessed; // applay maximum value for subsidy when needed

                if (payrollSubsidyAssessed > 0) {
                    SDbPayrollReceiptEarning payrollReceiptEarningSub = moHrsPayroll.createPayrollReceiptEarning(
                            this, earningSubsidy, hrsEmployeeDays, null, 
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
        
        boolean isTaxNet = moHrsPayroll.getConfig().isTaxNet();
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
                
                if (payrollReceiptDeduction.isUserEdited() || !payrollReceiptDeduction.isAutomatic()) {
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
            payrollTax = payrollTaxCompensated > 0 ? payrollTaxCompensated : 0;
        }
        
        if (hrsReceiptDeductionTaxOld != null || hrsReceiptDeductionTaxNew != null) {
            if (isUserTax) {
                // preserve computed tax by system (and implicitly tax by user as well):
                SDbPayrollReceiptDeduction payrollReceiptDeduction = hrsReceiptDeductionTaxOld.getPayrollReceiptDeduction();
                payrollReceiptDeduction.setAmountSystem_r(payrollTax);
            }
            else if (hrsReceiptDeductionTaxOld != null) {
                if (payrollTax > 0) {
                    // update former deduction:
                    SDbPayrollReceiptDeduction payrollReceiptDeduction = hrsReceiptDeductionTaxOld.getPayrollReceiptDeduction();
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
                    SDbPayrollReceiptDeduction payrollReceiptDeduction = hrsReceiptDeductionTaxNew.getPayrollReceiptDeduction();
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
                SDbPayrollReceiptEarning payrollReceiptEarning = hrsReceiptEarningSubsidyOld.getPayrollReceiptEarning();
                payrollReceiptEarning.setAmountSystem_r(payrollSubsidy);
                
                payrollReceiptEarning.setAmountExempt(userSubsidy); // subsidy is exempt
                payrollReceiptEarning.setAmountTaxable(0);
            }
            else if (hrsReceiptEarningSubsidyOld != null) {
                if (payrollSubsidy > 0) {
                    // update former earning:
                    SDbPayrollReceiptEarning payrollReceiptEarning = hrsReceiptEarningSubsidyOld.getPayrollReceiptEarning();
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
                    SDbPayrollReceiptEarning payrollReceiptEarning = hrsReceiptEarningSubsidyNew.getPayrollReceiptEarning();
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
        
        // create other earning to inform subsidy assessed in receipt, if needed:
        boolean isSubOffsetTotally = isTaxNet && payrollSubsidyAssessed > 0 && payrollTaxAssessed >= payrollSubsidyAssessed; // convenience variable
        computeReceiptTaxSubsidyAsOtherPayment(isSubOffsetTotally, earningSubsidyOtherPayment, moHrsEmployee.createEmployeeDays());
        
        // Set tax and subsidy current calculations:
        
        if (computeTax) {
            double payrollTaxCompensated = 0;
            double payrollSubsidyCompensated = 0;

            if (isTaxNet) {
                // compensate tax and subsidy between each other:
                
                if (payrollTaxAssessed >= payrollSubsidyAssessed) {
                    payrollTaxCompensated = payrollSubsidyAssessed;
                    payrollSubsidyCompensated = payrollSubsidyAssessed;
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
            moPayrollReceipt.setPayrollTaxSubsidyAssessedGross(payrollSubsidyAssessedGross);
            moPayrollReceipt.setPayrollTaxSubsidyAssessed(payrollSubsidyAssessed);
            moPayrollReceipt.setPayrollTaxSubsidyCompensated(payrollSubsidyCompensated);
            moPayrollReceipt.setPayrollTaxSubsidyPending_r(payrollSubsidy);
            moPayrollReceipt.setPayrollTaxSubsidyPayed(isUserSubsidy ? userSubsidy : payrollSubsidy);
            
            switch (payroll.getFkTaxComputationTypeId()) {
                case SModSysConsts.HRSS_TP_TAX_COMP_PAY: // Payroll
                    moPayrollReceipt.setAnnualTaxAssessed(0);
                    moPayrollReceipt.setAnnualTaxCompensated(0);
                    moPayrollReceipt.setAnnualTaxPayed(0);
                    moPayrollReceipt.setAnnualTaxSubsidyAssessed(0);
                    moPayrollReceipt.setAnnualTaxSubsidyCompensated(0);
                    moPayrollReceipt.setAnnualTaxSubsidyPayed(0);
                    break;

                case SModSysConsts.HRSS_TP_TAX_COMP_ANN: // Annual
                    moPayrollReceipt.setAnnualTaxAssessed(taxAssessed);
                    moPayrollReceipt.setAnnualTaxCompensated(annualTaxCompensated);
                    moPayrollReceipt.setAnnualTaxPayed(annualTaxPayed);
                    moPayrollReceipt.setAnnualTaxSubsidyAssessed(subsidyAssessed);
                    moPayrollReceipt.setAnnualTaxSubsidyCompensated(annualSubsidyCompensated);
                    moPayrollReceipt.setAnnualTaxSubsidyPayed(annualSubsidyPayed);
                    break;

                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN); // unnecessary, just for consistence!
            }
        }
    }

    private void computeReceiptSsContribution() throws Exception {
        double sscComputed = 0;
        SHrsReceiptDeduction hrsReceiptDeductionSscNew = null;
        SDbPayroll payroll = moHrsPayroll.getPayroll(); // convenience variable
        
        if (payroll.isSsContribution() && !moHrsEmployee.getEmployee().isAssimilable()) {  // assimilables are not elegible for SS contribution
            // Validate configuration of SS contribution:

            if (moHrsPayroll.getConfig().getFkDeductionSsContributionId_n() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración de retención de SS)");
            }

            SDbDeduction deductionSsc = moHrsPayroll.getDeduction(moHrsPayroll.getConfig().getFkDeductionSsContributionId_n());
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

    public void setHrsPayroll(SHrsPayroll o) { moHrsPayroll = o; }
    public void setHrsEmployee(SHrsEmployee o) { moHrsEmployee = o; }
    public void setPayrollReceipt(SDbPayrollReceipt o)  { moPayrollReceipt = o; }
    public void setAbsencesFromClock(ArrayList<SDbAbsence> absencesFromClock) { this.absencesFromClock = absencesFromClock;}

    public SHrsPayroll getHrsPayroll() { return moHrsPayroll; }
    public SHrsEmployee getHrsEmployee() { return moHrsEmployee; }
    public SDbPayrollReceipt getPayrollReceipt()  { return moPayrollReceipt; }
    public ArrayList<SDbAbsenceConsumption> getAbsenceConsumptions() { return maAbsenceConsumptions; }
    public ArrayList<SHrsReceiptEarning> getHrsReceiptEarnings() { return maHrsReceiptEarnings; }
    public ArrayList<SHrsReceiptDeduction> getHrsReceiptDeductions() { return maHrsReceiptDeductions; }
    public ArrayList<SHrsBenefit> getHrsBenefits() { return maHrsBenefits; }
    public ArrayList<SDbAbsence> getAbsencesFromClock() { return absencesFromClock; }

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
    
    public double getBenefitValue(final int benefitType, final int benefitAnn, final int benefitYear) {
        double value = 0;

        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            if (SLibUtils.compareKeys(new int[] { benefitType, benefitAnn, benefitYear }, new int[] { hrsReceiptEarning.getPayrollReceiptEarning().getFkBenefitTypeId(), 
                hrsReceiptEarning.getPayrollReceiptEarning().getBenefitAnniversary(), hrsReceiptEarning.getPayrollReceiptEarning().getBenefitYear() })) {
                value += hrsReceiptEarning.getPayrollReceiptEarning().getUnitsAlleged();
            }
        }

        return value;
    }
    
    public double getBenefitAmount(final int benefitType, final int benefitAnn, final int benefitYear) {
        double amount = 0;

        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            if (SLibUtils.compareKeys(new int[] { benefitType, benefitAnn, benefitYear }, new int[] { hrsReceiptEarning.getPayrollReceiptEarning().getFkBenefitTypeId(), 
                hrsReceiptEarning.getPayrollReceiptEarning().getBenefitAnniversary(), hrsReceiptEarning.getPayrollReceiptEarning().getBenefitYear() })) {
                amount += hrsReceiptEarning.getPayrollReceiptEarning().getAmount_r();
            }
        }

        return amount;
    }
    
    public double calculateBenefit(final SDbEarning earningBenefit, SHrsEmployeeDays hrsEmployeeDays, final double days, final double percentage) {
        double units = hrsEmployeeDays.computeEarningUnits(days, earningBenefit);
        return SLibUtils.roundAmount(units * moPayrollReceipt.getPaymentDaily() * (earningBenefit.getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_VAC_BON ? percentage : 1d));
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
        
        SHrsEmployeeDays hrsEmployeeDays = moHrsEmployee.createEmployeeDays();
        double receiptCalendarDays = hrsEmployeeDays.getReceiptDays();
        double employeeWorkableCalendarDays = hrsEmployeeDays.getReceiptDays() - hrsEmployeeDays.getDaysNotWorked_r();
        double employeeWorkableBusinessDays = hrsEmployeeDays.getBusinessDays() - hrsEmployeeDays.getDaysNotWorked_r();
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
        
        SHrsEmployeeDays hrsEmployeeDays = moHrsEmployee.createEmployeeDays();
        
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
        SDbEarning earningNormal = moHrsPayroll.getEarning(moHrsPayroll.getConfig().getFkEarningEarningId_n());
        
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
                    
                    if (daysToBePaid > hrsEmployeeDays.getDaysWorked()) {
                        daysToBePaid = hrsEmployeeDays.getDaysWorked();
                    }
                }
                
                if (daysToBePaid == 0) {
                    removeHrsReceiptEarning(hrsReceiptEarning.getPayrollReceiptEarning().getPkMoveId());
                }
                else {
                    double units = hrsEmployeeDays.computeEarningUnits(daysToBePaid, earningNormal);
                    double amount = SHrsEmployeeDays.computeEarningAmount(units, hrsReceiptEarning.getPayrollReceiptEarning().getAmountUnitary(), earningNormal);

                    hrsReceiptEarning.getPayrollReceiptEarning().setUnitsAlleged(daysToBePaid);
                    hrsReceiptEarning.getPayrollReceiptEarning().setUnits(units);
                    hrsReceiptEarning.getPayrollReceiptEarning().setAmount_r(amount);

                    replaceHrsReceiptEarning(hrsReceiptEarning.getPayrollReceiptEarning().getPkMoveId(), hrsReceiptEarning);
                }

                break;
            }
        }

        if (!found && hrsEmployeeDays.getDaysWorked() > 0) {
            SDbPayrollReceiptEarning payrollReceiptEarning = moHrsPayroll.createPayrollReceiptEarning(
                    this, earningNormal, hrsEmployeeDays, null, 
                    hrsEmployeeDays.getDaysWorked(), 0, true, 
                    0, 0, maHrsReceiptEarnings.size() + 1);

            SHrsReceiptEarning hrsReceiptEarning = new SHrsReceiptEarning();
            hrsReceiptEarning.setHrsReceipt(this);
            hrsReceiptEarning.setEarning(earningNormal);
            hrsReceiptEarning.setPayrollReceiptEarning(payrollReceiptEarning);

            addHrsReceiptEarning(hrsReceiptEarning);
        }

        // create (again) all absence earnings (by system):
        
        for (SHrsReceiptEarning hrsReceiptEarning : moHrsPayroll.createHrsReceiptEarningsAbsence(this, hrsEmployeeDays)) {
            addHrsReceiptEarning(hrsReceiptEarning);
        }
    }
    
    private SHrsDaysByPeriod createHrsDaysByPeriod(final int year, final int periodYear) {
        int daysPeriod = SLibTimeUtils.digestDate(SLibTimeUtils.getEndOfMonth(SLibTimeUtils.createDate(year, periodYear)))[2];
        int daysPeriodPayroll = 0;
        
        for (SDbEmployeeHireLog hireLog : moHrsEmployee.getEmployeeHireLogs()) {
            Date dateStart = hireLog.getDateHire().compareTo(moHrsEmployee.getPeriodStart()) <= 0 ? moHrsEmployee.getPeriodStart() : hireLog.getDateHire();
            Date dateEnd = hireLog.getDateDismissal_n() == null ? moHrsEmployee.getPeriodEnd() : hireLog.getDateDismissal_n().compareTo(moHrsEmployee.getPeriodEnd()) >= 0 ? moHrsEmployee.getPeriodEnd() : hireLog.getDateDismissal_n();
            
            int periodDays = (int) SLibTimeUtils.getDaysDiff(dateEnd, dateStart);
            
            for (int i = 0; i <= periodDays; i++) {
                if (SLibTimeUtils.isBelongingToPeriod(SLibTimeUtils.addDate(dateStart, 0, 0, i), year, periodYear)) {
                    daysPeriodPayroll++;
                }
            }
        }
        
        return new SHrsDaysByPeriod(year, periodYear, daysPeriod, daysPeriodPayroll);
    }
    
    private void computeHrsDaysByPeriod(SHrsEmployeeDays hrsEmployeeDays) throws Exception {
        int year;
        int periodYear;
        
        // Compute days by period previous:
        
        year = hrsEmployeeDays.getYear() - (hrsEmployeeDays.getPeriod() == 1 ? 1 : 0);
        periodYear = (hrsEmployeeDays.getPeriod() == 1 ? SLibTimeConsts.MONTHS : hrsEmployeeDays.getPeriod() - 1);
        
        SHrsDaysByPeriod hrsDaysPrev = createHrsDaysByPeriod(year, periodYear);
        
        // Compute days by period current:
        
        year = hrsEmployeeDays.getYear();
        periodYear = hrsEmployeeDays.getPeriod();
        
        SHrsDaysByPeriod hrsDaysCurr = createHrsDaysByPeriod(year, periodYear);
        
        // Compute days by period next:
        
        year = hrsEmployeeDays.getYear() + (hrsEmployeeDays.getPeriod() == SLibTimeConsts.MONTHS ? 1 : 0);
        periodYear = (hrsEmployeeDays.getPeriod() == SLibTimeConsts.MONTHS ? 1 : hrsEmployeeDays.getPeriod() + 1);
        
        SHrsDaysByPeriod hrsDaysNext = createHrsDaysByPeriod(year, periodYear);
        
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
        
        SHrsEmployeeDays hrsEmployeeDays = moHrsEmployee.createEmployeeDays();
        
        moPayrollReceipt.setFactorCalendar(hrsEmployeeDays.getFactorCalendar()); // just an informative datum
        moPayrollReceipt.setFactorDaysPaid(hrsEmployeeDays.getFactorDaysPaid()); // just an informative datum
        moPayrollReceipt.setReceiptDays(hrsEmployeeDays.getReceiptDays()); // just an informative datum
        moPayrollReceipt.setWorkingDays(hrsEmployeeDays.getWorkingDays()); // just an informative datum
        moPayrollReceipt.setDaysWorked(hrsEmployeeDays.getDaysWorked()); // just an informative datum
        moPayrollReceipt.setDaysHiredPayroll(hrsEmployeeDays.getDaysHiredPayroll());
        moPayrollReceipt.setDaysHiredAnnual(hrsEmployeeDays.getDaysHiredAnnual());
        moPayrollReceipt.setDaysIncapacityNotPaidPayroll(hrsEmployeeDays.getDaysIncapacityNotPaidPayroll());
        moPayrollReceipt.setDaysIncapacityNotPaidAnnual(hrsEmployeeDays.getDaysIncapacityNotPaidAnnual());
        moPayrollReceipt.setDaysNotWorkedButPaid(hrsEmployeeDays.getDaysNotWorkedButPaid());
        moPayrollReceipt.setDaysNotWorkedNotPaid(hrsEmployeeDays.getDaysNotWorkedNotPaid());
        moPayrollReceipt.setDaysNotWorked_r(hrsEmployeeDays.getDaysNotWorked_r());
        moPayrollReceipt.setDaysToBePaid_r(computeDaysToBePaid(maHrsReceiptEarnings));
        moPayrollReceipt.setDaysPaid(computeDaysPaid(maHrsReceiptEarnings));
        moPayrollReceipt.setPayrollTaxableDays_r(hrsEmployeeDays.getTaxableDaysPayroll());
        moPayrollReceipt.setAnnualTaxableDays_r(hrsEmployeeDays.getTaxableDaysAnnual());
        
        computeHrsDaysByPeriod(hrsEmployeeDays);
    }
    
    public SHrsReceipt clone() throws CloneNotSupportedException {
       SHrsReceipt clone = new SHrsReceipt();

       clone.setHrsPayroll(this.getHrsPayroll());
       clone.setHrsEmployee(this.getHrsEmployee());
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
       clone.getHrsEmployee().setHrsReceipt(clone);

       return clone;
    }
}

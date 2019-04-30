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
 * 2019-01-29 Sergio Flores: Corrección al cálculo de parte exenta en percepciones. Debe estar en en función de UMA desde 2017.
 */
public class SHrsReceipt {

    private SHrsPayroll moHrsPayroll;
    private SHrsEmployee moHrsEmployee;
    private SDbPayrollReceipt moPayrollReceipt;
    private final ArrayList<SDbAbsenceConsumption> maAbsenceConsumptions;
    private final ArrayList<SHrsReceiptEarning> maHrsReceiptEarnings;
    private final ArrayList<SHrsReceiptDeduction> maHrsReceiptDeductions;
    private final ArrayList<SHrsBenefit> maHrsBenefits;

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
     * Get amount total taxed amount of earnings of standard calculation of income tax.
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
     * Get amount total taxed amount of earnings configured based on articule 174 RLISR.
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
     * Compute all earnings of this receipt.
     */
    private void computeEarnings() {
        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            hrsReceiptEarning.computeEarning();
        }
    }

    /**
     * Compute exemption of all earnings of this receipt by grouping them in blocks of arrays.
     * @throws Exception 
     */
    private void computeEarningsExemption() throws Exception {
        // Group same earnings:
        
        HashMap<Integer, ArrayList<SHrsReceiptEarning>> hrsReceiptEarningsMap = new HashMap<>(); // key: earning ID; value: array of earnings
        
        // Process all receipt earnings:

        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            int earningId = hrsReceiptEarning.getEarning().getPkEarningId();
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
     * Compute altogether the hrsReceiptEarnings of the given array of earnings as a group.
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

        // compute all receipt earnings in given group:
        for (SHrsReceiptEarning hrsReceiptEarning : hrsReceiptEarnings) {
            double exemption = 0;
            SDbPayrollReceiptEarning payrollReceiptEarning = hrsReceiptEarning.getPayrollReceiptEarning();

            // compute exemption:
            if (applyExemptionStd) {
                double exemptionProp = 0;
                double exemptionLimit = 0;
                
                switch (earning.getFkEarningExemptionTypeId()) {
                    case SModSysConsts.HRSS_TP_EAR_EXEM_PER: // Percentage
                        // estimate exemption proposed and exemption limit:
                        if (moPayrollReceipt.getEffectiveSalary(moHrsPayroll.getPayroll().isFortnightStandard()) <= moHrsPayroll.getPayroll().getMwzWage()) { // salary cannot never be less than minimum wage, but just in case
                            exemptionProp = SLibUtils.roundAmount(earning.getExemptionSalaryEqualsMwzPercentage() * payrollReceiptEarning.getAmount_r());
                            exemptionLimit = SLibUtils.roundAmount(earning.getExemptionSalaryEqualsMwzLimit() * moHrsPayroll.getPayroll().getUmaAmount()); // formerly minimum wage was used
                        }
                        else {
                            exemptionProp = SLibUtils.roundAmount(earning.getExemptionSalaryGreaterMwzPercentage() * payrollReceiptEarning.getAmount_r());
                            exemptionLimit = SLibUtils.roundAmount(earning.getExemptionSalaryGreaterMwzLimit() * moHrsPayroll.getPayroll().getUmaAmount()); // formerly minimum wage was used
                        }
                        break;

                    case SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_GBL: // Minimum Wage Global
                    case SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_EVT: // Minimum Wage Event
                        // estimate exemption proposed and exemption limit:
                        exemptionProp = SLibUtils.roundAmount(earning.getExemptionMwz() * moHrsPayroll.getPayroll().getUmaAmount()); // formerly minimum wage was used
                        exemptionLimit = exemptionProp;
                        break;

                    case SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_SEN: // Minimum Wage Seniority
                        // compute exact seniority:
                        int years = moHrsEmployee.getSeniority();
                        int yearDays = (int) SLibTimeUtils.getDaysDiff(moHrsPayroll.getPayroll().getDateEnd(), SLibTimeUtils.addDate(moHrsEmployee.getEmployee().getDateBenefits(), years, 0, 0));
                        double seniority = (double) years + ((double) yearDays / SHrsConsts.YEAR_DAYS);

                        // estimate exemption proposed and exemption limit:
                        exemptionProp = SLibUtils.roundAmount(earning.getExemptionMwz() * moHrsPayroll.getPayroll().getUmaAmount() * seniority); // formerly minimum wage was used
                        exemptionLimit = exemptionProp;
                        break;

                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }

                // adjust exemption:

                exemption = SLibUtils.roundAmount(exemptionProp - exemptionPayroll - exemptionGlobal);

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
    
    private void computeDeductions() {
        for (SHrsReceiptDeduction hrsReceiptDeduction : maHrsReceiptDeductions) {
            hrsReceiptDeduction.computeDeduction();
        }
    }

    /**
     * Creates and adds earning of $0.01 if subsidy was totally offset by tax.
     * Otherwise this earning is removed when existing.
     * @param isSubOffset
     * @param dbEarningOther
     * earningOther Exception 
     */
    private void createEarningOther(final boolean isSubOffset, final SDbEarning earningOther, final SHrsEmployeeDays hrsEmployeeDays) throws Exception {
        int countSub = 0;
        SHrsReceiptEarning hrsReceiptEarningPrevious = null;
        
        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            SDbPayrollReceiptEarning payrollReceiptEarning = hrsReceiptEarning.getPayrollReceiptEarning();
            
            if (payrollReceiptEarning.getFkEarningTypeId() == SModSysConsts.HRSS_TP_EAR_OTH && payrollReceiptEarning.isAutomatic()) {
                if (++countSub > 1) {
                    throw new Exception("¡No puede haber más de una percepcion 'Subsidio para el empleo' en el recibo!");
                }
                
                // prepare for update or removal of existing subsidy earning:
                hrsReceiptEarningPrevious = hrsReceiptEarning;
            }
        }
        
        if (hrsReceiptEarningPrevious != null) {
            maHrsReceiptEarnings.remove(hrsReceiptEarningPrevious);
        }
        
        if (isSubOffset) {
            // subsidy compensated:
            
            double amountRequired = 0.01;
            SDbPayrollReceiptEarning payrollReceiptEarningOther = moHrsPayroll.createPayrollReceiptEarning(
                    this, earningOther, hrsEmployeeDays, null, 
                    1, amountRequired, true, 
                    0, 0, maHrsReceiptEarnings.size() + 1);
            
            // atypish, but this earning will not be computed anymore, so:
            payrollReceiptEarningOther.setAmountExempt(amountRequired);
            payrollReceiptEarningOther.setAmountTaxable(0);
            
            SHrsReceiptEarning hrsReceiptEarningNew = new SHrsReceiptEarning();
            hrsReceiptEarningNew.setHrsReceipt(this);
            hrsReceiptEarningNew.setEarning(earningOther);
            hrsReceiptEarningNew.setPayrollReceiptEarning(payrollReceiptEarningOther);

            maHrsReceiptEarnings.add(hrsReceiptEarningNew);
            renumberHrsReceiptEarnings();
        }
        
        if (hrsReceiptEarningPrevious != null || isSubOffset) {
            renumberHrsReceiptEarnings();
        }
    }

    private void computeReceiptTax() throws Exception {
        // Clear tax and subsidy existing calculations:

        moPayrollReceipt.setPayrollFactorTax(0);
        moPayrollReceipt.setPayrollTaxAssessed(0);
        moPayrollReceipt.setPayrollTaxCompensated(0);
        moPayrollReceipt.setPayrollTaxPending_r(0);
        moPayrollReceipt.setPayrollTaxPayed(0);
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

        double annualTaxCompensated = 0; // annual tax compensated
        double annualTaxPayed = 0;       // annual tax payed (withheld from employee)
        double annualSubCompensated = 0; // annual subsidy compensated
        double annualSubPayed = 0;       // annual subsidy payed (to employee)
        double taxAssessed = 0;
        double taxPending = 0;
        double subAssessed = 0;
        double subPending = 0;
        
        SHrsReceiptEarning hrsReceiptEarningSubNew = null;
        SHrsReceiptDeduction hrsReceiptDeductionTaxNew = null;
        
        // Check if tax computation is needed:
        
        SDbDeduction deductionTax = null;
        SDbEarning earningSub = null;
        SDbEarning earningOther = null;
        boolean computeTax = SLibUtils.belongsTo(moHrsPayroll.getPayroll().getFkTaxComputationTypeId(), new int[] { SModSysConsts.HRSS_TP_TAX_COMP_PAY, SModSysConsts.HRSS_TP_TAX_COMP_ANN });
        boolean computeSub = computeTax & moHrsPayroll.getPayroll().isTaxSubsidy() && !moHrsEmployee.getEmployee().isAssimilable(); // assimilables are not elegible for subsidy

        if (computeTax) {
            // Validate deduction configuration for tax:

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

            if (moHrsPayroll.getPayroll().getFkTaxId() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Tabla de impuesto)");
            }

            SDbTaxTable dbTaxTable = moHrsPayroll.getTaxTable(moHrsPayroll.getPayroll().getFkTaxId());
            if (dbTaxTable == null)  {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Tabla de impuesto)");
            }

            // Validate earning configuration for subsidy:

            if (moHrsPayroll.getConfig().getFkEarningTaxSubsidyId_n() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Percepción de subsidio para el empleo)");
            }

            earningSub = moHrsPayroll.getEarning(moHrsPayroll.getConfig().getFkEarningTaxSubsidyId_n());
            if (earningSub == null) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Percepción de subsidio para el empleo)");
            }

            if (earningSub.getFkEarningTypeId() != SModSysConsts.HRSS_TP_EAR_TAX_SUB) {
                throw new Exception(SLibConsts.ERR_MSG_WRONG_TYPE + " (Tipo de percepción en percepción de subsidio para el empleo)");
            }

            if (moHrsPayroll.getPayroll().getFkTaxSubsidyId() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Tabla de subsidio para el empleo)");
            }

            SDbTaxSubsidyTable dbSubTable = moHrsPayroll.getTaxSubsidyTable(moHrsPayroll.getPayroll().getFkTaxSubsidyId());
            if (dbSubTable == null)  {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Tabla de subsidio para el empleo)");
            }

            // Validate earning configuration for compensated subsidy:

            if (moHrsPayroll.getConfig().getFkEarningTaxSubsidyCompensatedId_n() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Percepción de subsidio para el empleo compensado)");
            }

            earningOther = moHrsPayroll.getEarning(moHrsPayroll.getConfig().getFkEarningTaxSubsidyCompensatedId_n());
            if (earningOther == null) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Percepción de subsidio para el empleo compensado)");
            }

            if (earningOther.getFkEarningTypeId() != SModSysConsts.HRSS_TP_EAR_OTH) {
                throw new Exception(SLibConsts.ERR_MSG_WRONG_TYPE + " (Tipo de percepción en percepción de subsidio para el empleo compensado)");
            }

            // Obtain taxable earnings and, if necessary, accumulated tax and subsidy:
            
            double earningsTaxable = 0;
            double earningsTaxableArt174 = 0;

            switch (moHrsPayroll.getPayroll().getFkTaxComputationTypeId()) {
                case SModSysConsts.HRSS_TP_TAX_COMP_PAY: // Payroll
                    earningsTaxable = SLibUtils.roundAmount(getTaxedEarningsStd());
                    earningsTaxableArt174 = SLibUtils.roundAmount(getTaxedEarningsArt174());
                    break;

                case SModSysConsts.HRSS_TP_TAX_COMP_ANN: // Annual
                    earningsTaxable = SLibUtils.roundAmount(getTaxedEarningsStd() + moHrsEmployee.getAccummulatedTaxableEarnings());
                    earningsTaxableArt174 = SLibUtils.roundAmount(getTaxedEarningsArt174() + moHrsEmployee.getAccummulatedTaxableEarningsAlt());
                    
                    annualTaxCompensated = moHrsEmployee.getAnnualTaxCompensated(); // should be equal to compensated annual subsidy
                    
                    SHrsAccumulatedDeduction hrsAccumulatedTax = moHrsEmployee.getHrsAccumulatedDeductionByType(SModSysConsts.HRSS_TP_DED_TAX);
                    if (hrsAccumulatedTax != null) {
                        annualTaxPayed = SLibUtils.roundAmount(hrsAccumulatedTax.getAcummulated());
                    }
                    
                    annualSubCompensated = moHrsEmployee.getAnnualTaxSubsidyCompensated(); // should be equal to compensated annual tax

                    SHrsAccumulatedEarning hrsAccumulatedSub = moHrsEmployee.getHrsAccumulatedEarningByType(SModSysConsts.HRSS_TP_EAR_TAX_SUB);
                    if (hrsAccumulatedSub != null) {
                        annualSubPayed = SLibUtils.roundAmount(hrsAccumulatedSub.getAcummulated());
                    }
                    break;

                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }

            // Define payroll and annual factors for adjusting of tax and subsidy tables:

            double tableFactorPayroll;
            double tableFactorAnnual;
            SHrsEmployeeDays hrsEmployeeDays = moHrsEmployee.createEmployeeDays();
            
            if (moHrsPayroll.getPayroll().getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_FOR && moHrsPayroll.getConfig().isFortnightStandard()) {
                tableFactorPayroll = ((double) SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS_FORTNIGHTS_FIXED) * hrsEmployeeDays.getTaxableDaysPayroll();
                tableFactorAnnual = ((double) SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS_FORTNIGHTS_FIXED) * hrsEmployeeDays.getTaxableDaysAnnual();
            }
            else {
                tableFactorPayroll = ((double) SHrsConsts.YEAR_MONTHS / (SHrsConsts.YEAR_DAYS + (SLibTimeUtils.isLeapYear(moHrsPayroll.getPayroll().getFiscalYear()) ? 1d : 0d))) * hrsEmployeeDays.getTaxableDaysPayroll();
                tableFactorAnnual = ((double) SHrsConsts.YEAR_MONTHS / (SHrsConsts.YEAR_DAYS + (SLibTimeUtils.isLeapYear(moHrsPayroll.getPayroll().getFiscalYear()) ? 1d : 0d))) * hrsEmployeeDays.getTaxableDaysAnnual();
            }
            
            moPayrollReceipt.setPayrollFactorTax(tableFactorPayroll);
            moPayrollReceipt.setAnnualFactorTax(tableFactorAnnual);
            
            double tableFactor;
            
            if (moHrsPayroll.getPayroll().getFkTaxComputationTypeId() == SModSysConsts.HRSS_TP_TAX_COMP_ANN) {
                tableFactor = tableFactorAnnual;
            }
            else {
                tableFactor = tableFactorPayroll;
            }

            // Compute tax (payroll tax or annual tax, that one that is needed):
            
            for (int row = 0; row < dbTaxTable.getChildRows().size(); row++) {
                SDbTaxTableRow taxTableRow = dbTaxTable.getChildRows().get(row);
                if (earningsTaxable >= SLibUtils.roundAmount(taxTableRow.getLowerLimit() * tableFactor) &&
                        (row + 1 == dbTaxTable.getChildRows().size() || earningsTaxable < SLibUtils.roundAmount(dbTaxTable.getChildRows().get(row + 1).getLowerLimit() * tableFactor))) {
                    taxAssessed = SLibUtils.roundAmount((earningsTaxable - SLibUtils.roundAmount(taxTableRow.getLowerLimit() * tableFactor)) * taxTableRow.getTaxRate() + taxTableRow.getFixedFee() * tableFactor);
                    break;
                }
            }
            
            if (earningsTaxableArt174 > 0) {
                double taxAssessedAlt = SHrsUtils.computeAmountTaxAlt(dbTaxTable, earningsTaxableArt174, moPayrollReceipt.getPaymentMonthly(), tableFactor);
                taxAssessed = SLibUtils.roundAmount(taxAssessed + taxAssessedAlt);
            }

            if (taxAssessed != 0 && taxAssessed > (annualTaxCompensated + annualTaxPayed)) {
                taxPending = SLibUtils.roundAmount(taxAssessed - (annualTaxCompensated + annualTaxPayed));
                
                SDbPayrollReceiptDeduction payrollReceiptDeductionTax = moHrsPayroll.createPayrollReceiptDeduction(
                        this, deductionTax, 
                        1, taxPending, true, 
                        0, 0, maHrsReceiptDeductions.size() + 1);
                
                hrsReceiptDeductionTaxNew = new SHrsReceiptDeduction();
                hrsReceiptDeductionTaxNew.setHrsReceipt(this);
                hrsReceiptDeductionTaxNew.setDeduction(deductionTax);
                hrsReceiptDeductionTaxNew.setPayrollReceiptDeduction(payrollReceiptDeductionTax);
            }

            if (computeSub) {
                // Compute subsidy:
                
                for (int row = 0; row < dbSubTable.getChildRows().size(); row++) {
                    SDbTaxSubsidyTableRow subsidyTableRow = dbSubTable.getChildRows().get(row);
                    if (earningsTaxable >= subsidyTableRow.getLowerLimit() * tableFactor &&
                            (row + 1 == dbSubTable.getChildRows().size() || earningsTaxable < dbSubTable.getChildRows().get(row + 1).getLowerLimit() * tableFactor)) {
                        subAssessed = SLibUtils.roundAmount(subsidyTableRow.getTaxSubsidy() * tableFactor);
                        break;
                    }
                }

                if (earningsTaxableArt174 > 0) {
                    double subsidyComputedAlt = SHrsUtils.computeAmountTaxSubsidyAlt(dbSubTable, earningsTaxableArt174, moPayrollReceipt.getPaymentMonthly(), tableFactor);
                    subAssessed = SLibUtils.roundAmount(subAssessed + subsidyComputedAlt);
                }
                
                if (subAssessed != 0 && subAssessed > (annualSubCompensated + annualSubPayed)) {
                    subPending = SLibUtils.roundAmount(subAssessed - (annualSubCompensated + annualSubPayed));

                    SDbPayrollReceiptEarning payrollReceiptEarningSub = moHrsPayroll.createPayrollReceiptEarning(
                            this, earningSub, hrsEmployeeDays, null, 
                            1, subPending, true, 
                            0, 0, maHrsReceiptEarnings.size() + 1);

                    // atypish, but this earning will not be computed anymore, so:
                    payrollReceiptEarningSub.setAmountExempt(subPending); 
                    payrollReceiptEarningSub.setAmountTaxable(0);

                    hrsReceiptEarningSubNew = new SHrsReceiptEarning();
                    hrsReceiptEarningSubNew.setHrsReceipt(this);
                    hrsReceiptEarningSubNew.setEarning(earningSub);
                    hrsReceiptEarningSubNew.setPayrollReceiptEarning(payrollReceiptEarningSub);
                }
            }
        }
        
        // Adequate tax and subsidy into payroll:
        
        boolean isTaxNet = moHrsPayroll.getConfig().isTaxNet();
        double taxNet = isTaxNet ? SLibUtils.roundAmount(taxPending - subPending) : 0; // when positive: tax; when negative: subsidy
        
        // Remove or update previous tax:

        int countTax = 0;
        double userTax = 0;
        boolean isUserTax = false;
        SHrsReceiptDeduction hrsReceiptDeductionTaxOld = null;

        for (SHrsReceiptDeduction hrsReceiptDeduction : maHrsReceiptDeductions) {
            SDbPayrollReceiptDeduction payrollReceiptDeduction = hrsReceiptDeduction.getPayrollReceiptDeduction();
            
            if (payrollReceiptDeduction.getFkDeductionTypeId() == SModSysConsts.HRSS_TP_DED_TAX) {
                if (++countTax > 1) {
                    throw new Exception("¡No puede haber más de una deducción 'ISR' en el recibo!");
                }
                
                // prepare for removal or update of existing tax deduction:
                hrsReceiptDeductionTaxOld = hrsReceiptDeduction;
                
                if (payrollReceiptDeduction.isUserEdited() || !payrollReceiptDeduction.isAutomatic()) {
                    // user edited tax:
                    isUserTax = true;
                    userTax = payrollReceiptDeduction.getAmount_r();
                }
            }
        }
        
        // Asign payroll receipt tax:
        
        double payrollTax = isTaxNet ? (taxNet > 0 ? taxNet : 0) : taxPending;
        
        if (hrsReceiptDeductionTaxOld != null || hrsReceiptDeductionTaxNew != null) {
            if (isUserTax) {
                // preserve computed tax:
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
                    // reasign tax; when created new deduction was asigned with tax pending:
                    SDbPayrollReceiptDeduction payrollReceiptDeduction = hrsReceiptDeductionTaxNew.getPayrollReceiptDeduction();
                    payrollReceiptDeduction.setAmountUnitary(payrollTax);
                    payrollReceiptDeduction.setAmountSystem_r(payrollTax);
                    payrollReceiptDeduction.setAmount_r(payrollTax);
                }
                
                maHrsReceiptDeductions.add(hrsReceiptDeductionTaxNew);
                renumberHrsReceiptDeductions();
            }
        }
        
        // Remove or update previous subsidy:
        
        int countSub = 0;
        double userSub = 0;
        boolean isUserSub = false;
        SHrsReceiptEarning hrsReceiptEarningSubOld = null;
        
        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            SDbPayrollReceiptEarning payrollReceiptEarning = hrsReceiptEarning.getPayrollReceiptEarning();
            
            if (payrollReceiptEarning.getFkEarningTypeId() == SModSysConsts.HRSS_TP_EAR_TAX_SUB) {
                if (++countSub > 1) {
                    throw new Exception("¡No puede haber más de una percepcion 'Subsidio para el empleo' en el recibo!");
                }
                
                // prepare for update or removal of existing subsidy earning:
                hrsReceiptEarningSubOld = hrsReceiptEarning;
                
                if (payrollReceiptEarning.isUserEdited() || !payrollReceiptEarning.isAutomatic()) {
                    // user edited subsidy:
                    isUserSub = true;
                    userSub = payrollReceiptEarning.getAmount_r();
                }
            }
        }
        
        // Asign payroll receipt subsidy:
        
        double payrollSub = isTaxNet ? (taxNet < 0 ? -taxNet : 0) : subPending;
        
        if (hrsReceiptEarningSubOld != null || hrsReceiptEarningSubNew != null) {
            if (isUserSub) {
                // preserve computed subsidy:
                SDbPayrollReceiptEarning payrollReceiptEarning = hrsReceiptEarningSubOld.getPayrollReceiptEarning();
                payrollReceiptEarning.setAmountSystem_r(payrollSub);
                payrollReceiptEarning.setAmountExempt(userSub); // exempt user edited subsidy
            }
            else if (hrsReceiptEarningSubOld != null) {
                if (payrollSub > 0) {
                    // update former earning:
                    SDbPayrollReceiptEarning payrollReceiptEarning = hrsReceiptEarningSubOld.getPayrollReceiptEarning();
                    payrollReceiptEarning.setAmountUnitary(payrollSub);
                    payrollReceiptEarning.setAmountSystem_r(payrollSub);
                    payrollReceiptEarning.setAmount_r(payrollSub);
                    payrollReceiptEarning.setAmountExempt(payrollSub);
                }
                else {
                    // remove former earning:
                    maHrsReceiptEarnings.remove(hrsReceiptEarningSubOld);
                    renumberHrsReceiptEarnings();
                }
            }
            else if (payrollSub > 0) {
                // add new earning:
                
                if (isTaxNet) {
                    // reasign subsidy; when created new earning was asigned with subsidy pending:
                    SDbPayrollReceiptEarning payrollReceiptEarning = hrsReceiptEarningSubNew.getPayrollReceiptEarning();
                    payrollReceiptEarning.setAmountUnitary(payrollSub);
                    payrollReceiptEarning.setAmountSystem_r(payrollSub);
                    payrollReceiptEarning.setAmount_r(payrollSub);
                    payrollReceiptEarning.setAmountExempt(payrollSub);
                }
                
                maHrsReceiptEarnings.add(hrsReceiptEarningSubNew);
                renumberHrsReceiptEarnings();
            }
        }
        
        createEarningOther(isTaxNet && subPending > 0 && taxPending > subPending, earningOther, moHrsEmployee.createEmployeeDays());
        
        // Set tax and subsidy current calculations:
        
        if (computeTax) {
            double payrollTaxCompensated = 0;
            double payrollSubCompensated = 0;
            boolean computeTaxByPayroll = moHrsPayroll.getPayroll().getFkTaxComputationTypeId() == SModSysConsts.HRSS_TP_TAX_COMP_PAY;

            if (isTaxNet) {
                if (taxPending > subPending) {
                    payrollTaxCompensated = subPending;
                    payrollSubCompensated = subPending;
                }
                else if (taxPending < subPending) {
                    payrollTaxCompensated = taxPending;
                    payrollSubCompensated = taxPending;
                }
            }

            moPayrollReceipt.setPayrollTaxAssessed(computeTaxByPayroll ? taxAssessed : taxPending);
            moPayrollReceipt.setPayrollTaxCompensated(payrollTaxCompensated);
            moPayrollReceipt.setPayrollTaxPending_r(payrollTax);
            moPayrollReceipt.setPayrollTaxPayed(isUserTax ? userTax : payrollTax);
            moPayrollReceipt.setPayrollTaxSubsidyAssessed(computeTaxByPayroll ? subAssessed : subPending);
            moPayrollReceipt.setPayrollTaxSubsidyCompensated(payrollSubCompensated);
            moPayrollReceipt.setPayrollTaxSubsidyPending_r(payrollSub);
            moPayrollReceipt.setPayrollTaxSubsidyPayed(isUserSub ? userSub : payrollSub);
            
            switch (moHrsPayroll.getPayroll().getFkTaxComputationTypeId()) {
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
                    moPayrollReceipt.setAnnualTaxSubsidyAssessed(subAssessed);
                    moPayrollReceipt.setAnnualTaxSubsidyCompensated(annualSubCompensated);
                    moPayrollReceipt.setAnnualTaxSubsidyPayed(annualSubPayed);
                    break;

                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN); // unnecessary, just for consistence!
            }
        }
    }

    private void computeReceiptSsContribution() throws Exception {
        double sscComputed = 0;
        SHrsReceiptDeduction hrsReceiptDeductionSscNew = null;
        
        if (moHrsPayroll.getPayroll().isSsContribution()) {
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

            if (moHrsPayroll.getPayroll().getFkSsContributionId() == 0) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración de tabla de retención de SS)");
            }

            SDbSsContributionTable ssContributionTable = moHrsPayroll.getSsContributionTable(moHrsPayroll.getPayroll().getFkSsContributionId());
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
                        sscRow = SLibUtils.roundAmount(daysWorkedAndIncapacityNotPaid * moHrsPayroll.getPayroll().getUmaAmount()); // formerly minimum wage of reference zone was used
                        break;
                        
                    case SHrsConsts.SS_INC_KND_SSC_GT: // kind SSC greater than limit
                        double surplus = moPayrollReceipt.getSalarySscBase() - (dbSscTableRow.getLowerLimitMwzReference() * moHrsPayroll.getPayroll().getUmaAmount()); // formerly minimum wage of reference zone was used
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

    public SHrsPayroll getHrsPayroll() { return moHrsPayroll; }
    public SHrsEmployee getHrsEmployee() { return moHrsEmployee; }
    public SDbPayrollReceipt getPayrollReceipt()  { return moPayrollReceipt; }
    public ArrayList<SDbAbsenceConsumption> getAbsenceConsumptions() { return maAbsenceConsumptions; }
    public ArrayList<SHrsReceiptEarning> getHrsReceiptEarnings() { return maHrsReceiptEarnings; }
    public ArrayList<SHrsReceiptDeduction> getHrsReceiptDeductions() { return maHrsReceiptDeductions; }
    public ArrayList<SHrsBenefit> getHrsBenefits() { return maHrsBenefits; }

    public void renumberHrsReceiptEarnings() {
        int moveId = 0;
        
        Collections.sort(maHrsReceiptEarnings);
        for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            hrsReceiptEarning.getPayrollReceiptEarning().setPkMoveId(++moveId);
        }
    }

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
    
    public void updateBenefits() {
        SDbBenefitTable benefitTable = null;
        SDbBenefitTable benefitTableAux = null;
        SDbBenefitTableRow benefitTableRow = null;
        SDbBenefitTableRow benefitTableRowAux = null;
        
        try {
            for (SHrsBenefit benefit : maHrsBenefits) {
                for (SHrsReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
                    if (SLibUtils.compareKeys(benefit.getBenefitKey(), new int[] { hrsReceiptEarning.getPayrollReceiptEarning().getFkBenefitTypeId(), 
                        hrsReceiptEarning.getPayrollReceiptEarning().getBenefitAnniversary(), hrsReceiptEarning.getPayrollReceiptEarning().getBenefitYear() })) {

                        benefit.setValuePayedReceipt(0d);
                        benefit.setAmountPayedReceipt(0d);

                        // Obtain benefit table row more appropiate for seniority:

                        benefitTable = moHrsPayroll.getBenefitTable(hrsReceiptEarning.getPayrollReceiptEarning().getFkBenefitTypeId(), moHrsPayroll.getPayroll().getDateEnd(), moHrsPayroll.getPayroll().getFkPaymentTypeId());

                        for (SDbBenefitTableRow row : benefitTable.getChildRows()) {
                            if (row.getMonths() >= (hrsReceiptEarning.getPayrollReceiptEarning().getBenefitAnniversary() * SHrsConsts.YEAR_MONTHS)) {
                                benefitTableRow = row;
                                break;
                            }
                        }

                        // Obtain benefit table row more appropiate for seniority, it's for vacation bonus:

                        if (hrsReceiptEarning.getPayrollReceiptEarning().getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_VAC_BON) {
                            benefitTableAux = moHrsPayroll.getBenefitTable(SModSysConsts.HRSS_TP_BEN_VAC, moHrsPayroll.getPayroll().getDateEnd(), moHrsPayroll.getPayroll().getFkPaymentTypeId());
                            
                            for (SDbBenefitTableRow row : benefitTableAux.getChildRows()) {
                                if (row.getMonths() >= (hrsReceiptEarning.getPayrollReceiptEarning().getBenefitAnniversary() * SHrsConsts.YEAR_MONTHS)) {
                                    benefitTableRowAux = row;
                                    break;
                                }
                            }
                        }
                        if (hrsReceiptEarning.getPayrollReceiptEarning().getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_VAC_BON) {
                            benefit.setValue(benefitTableRow.getBenefitBonusPercentage());
                            benefit.setAmount(benefitTableRowAux.getBenefitDays() * moPayrollReceipt.getPaymentDaily() * benefitTableRow.getBenefitBonusPercentage());
                        }
                        else {
                            benefit.setValue(benefitTableRow.getBenefitDays());
                            benefit.setAmount(benefitTableRow.getBenefitDays() * moPayrollReceipt.getPaymentDaily());
                        }
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    public void computeReceipt() throws Exception {
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
    
    public SDbAbsenceConsumption createAbsenceConsumption(final SDbAbsence absence, final Date dateStart, final Date dateEnd, final int effectiveDays) throws Exception {
        SDbAbsenceConsumption absenceConsumption = new SDbAbsenceConsumption();

        absenceConsumption.setPkEmployeeId(absence.getPkEmployeeId());
        absenceConsumption.setPkAbsenceId(absence.getPkAbsenceId());
        absenceConsumption.setPkConsumptionId(absence.getActualNextConsumptionId(moHrsEmployee));
        absenceConsumption.setDateStart(dateStart);
        absenceConsumption.setDateEnd(dateEnd);
        absenceConsumption.setEffectiveDays(effectiveDays);
        //absenceConsumption.setDeleted();
        //absenceConsumption.setFkReceiptPayrollId();
        absenceConsumption.setFkReceiptEmployeeId(absence.getPkEmployeeId());
        //absenceConsumption.setFkUserInsertId();
        //absenceConsumption.setFkUserUpdateId();
        //absenceConsumption.setTsUserInsert();
        //absenceConsumption.setTsUserUpdate();
        
        absenceConsumption.setParentAbsence(absence);
        
        return absenceConsumption;
    }
    
    public boolean validateAbsenceConsumption(SDbAbsenceConsumption absenceConsumption) throws Exception {
        // check earning for payable absence:
        
        if (absenceConsumption.getParentAbsence().isXtaAbsenceTypePayable()) {
            boolean found = false;
            
            for (SDbEarning earning : moHrsPayroll.getEarnigs()) {
                if (SLibUtils.compareKeys(earning.getAbsenceTypeKey(), absenceConsumption.getParentAbsence().getAbsenceTypeKey())) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                throw new Exception("No existen percepciones correspondientes a la incidencia tipo '" + absenceConsumption.getParentAbsence().getXtaAbsenceType() + "', clase '" + absenceConsumption.getParentAbsence().getXtaAbsenceClass() + "', la cual es pagable.");
            }
        }
        
        // validate absence:
        
        SDbAbsence absence = absenceConsumption.getParentAbsence();
        
        if (absenceConsumption.getEffectiveDays() > absenceConsumption.getCalendarDays()) {
            throw new Exception("Los días " + SDbAbsenceClass.EFFECTIVE + " a consumir de la incidencia '" + absence.getXtaAbsenceType() + "' (" + absenceConsumption.getEffectiveDays() + ")\n"
                    + "son mayores a los días " + SDbAbsenceClass.CALENDAR + " (" + absenceConsumption.getCalendarDays()+ ") del consumo.");
        }

        int consumptionConsumedDays = absence.getActualConsumedDays(moHrsEmployee);
        int consumptionRemainingDays = absence.getEffectiveDays() - consumptionConsumedDays;
        
        if (consumptionRemainingDays <= 0) {
            throw new Exception("Todos los días " + SDbAbsenceClass.EFFECTIVE + " de la incidencia '" + absence.getXtaAbsenceType() + "' (" + absenceConsumption.getEffectiveDays() + ") "
                    + "ya están consumidos (" + consumptionConsumedDays + ").");
        }
        
        // validate absence consumption:
        
        SHrsEmployeeDays hrsEmployeeDays = moHrsEmployee.createEmployeeDays();
        double receiptCalendarDays = hrsEmployeeDays.getReceiptDays();
        double employeeWorkableCalendarDays = hrsEmployeeDays.getReceiptDays() - hrsEmployeeDays.getDaysNotWorked_r();
        double employeeWorkableBusinessDays = hrsEmployeeDays.getBusinessDays() - hrsEmployeeDays.getDaysNotWorked_r();
        int consumptionActualBusinessDays = SHrsUtils.countBusinessDays(absenceConsumption.getDateStart(), absenceConsumption.getDateEnd(), moHrsPayroll.getWorkingDaySettings(), moHrsPayroll.getHolidays());
        
        if (moHrsPayroll.getPayroll().isNormal()) {
            if (absence.consumesCalendarDays()) {
                // validate calendar days:
                if (employeeWorkableCalendarDays <= 0) {
                    throw new Exception("El recibo de nómina no tiene días " + SDbAbsenceClass.CALENDAR + " disponibles (" + employeeWorkableCalendarDays + ")\n"
                            + "para asignar la incidencia '" + absence.getXtaAbsenceType() + "'.");
                }
                else if (absenceConsumption.getEffectiveDays() > employeeWorkableCalendarDays) {
                    throw new Exception("Los días " + SDbAbsenceClass.EFFECTIVE + " a consumir de la incidencia '" + absence.getXtaAbsenceType() + "' (" + absenceConsumption.getEffectiveDays() + ")\n"
                            + "son mayores a los días " + SDbAbsenceClass.CALENDAR + " disponibles (" + employeeWorkableCalendarDays + ") del recibo de nómina.");
                }
            }
            else {
                // validate instead business days:
                if (employeeWorkableBusinessDays <= 0) {
                    throw new Exception("El recibo de nómina no tiene días " + SDbAbsenceClass.BUSINESS + " disponibles (" + employeeWorkableBusinessDays + ")\n"
                            + "para asignar la incidencia '" + absence.getXtaAbsenceType() + "'.");
                }
                else if (consumptionActualBusinessDays > employeeWorkableBusinessDays) {
                    throw new Exception("Los días " + SDbAbsenceClass.BUSINESS + " a consumir de la incidencia '" + absence.getXtaAbsenceType() + "' (" + consumptionActualBusinessDays + ")\n"
                            + "son mayores a los días " + SDbAbsenceClass.BUSINESS + " disponibles (" + employeeWorkableBusinessDays + ") del recibo de nómina.");
                }
                else if (absenceConsumption.getEffectiveDays() > employeeWorkableBusinessDays) {
                    throw new Exception("Los días " + SDbAbsenceClass.EFFECTIVE + " a consumir de la incidencia '" + absence.getXtaAbsenceType() + "' (" + absenceConsumption.getEffectiveDays() + ")\n"
                            + "son mayores a los días " + SDbAbsenceClass.BUSINESS + " disponibles (" + employeeWorkableBusinessDays + ") del recibo de nómina.");
                }
            }
            
            // validate total consumed effective days of all absences:
            
            int totalEffectiveDays = 0;
            
            for (SDbAbsenceConsumption consumption : maAbsenceConsumptions) {
                totalEffectiveDays += consumption.getEffectiveDays();
            }
            
            totalEffectiveDays += absenceConsumption.getEffectiveDays();
            
            if (totalEffectiveDays > receiptCalendarDays) {
                throw new Exception("El total de días " + SDbAbsenceClass.CALENDAR + " consumidos de todas las ausencias (" + totalEffectiveDays + ") "
                        + "son mayores a los días " + SDbAbsenceClass.CALENDAR + " (" + receiptCalendarDays + ") del recibo de nómina.");
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
        // make sure that consumptions are removed in reverse:
        
        int consumptionsCount = 0;
        int maxConsumptionId = 0;

        for (SDbAbsenceConsumption absenceConsumption : maAbsenceConsumptions) {
            if (SLibUtils.compareKeys(absenceConsumption.getAbsenceKey(), absenceConsumptionToRemove.getAbsenceKey())) {
                consumptionsCount++;
                if (absenceConsumption.getPkConsumptionId() > maxConsumptionId) {
                    maxConsumptionId = absenceConsumption.getPkConsumptionId();
                }
            }
        }
        
        // remove consumption:
        
        if (consumptionsCount > 0) {
            for (int i = 0; i < maAbsenceConsumptions.size(); i++) {
                if (SLibUtils.compareKeys(maAbsenceConsumptions.get(i).getPrimaryKey(), absenceConsumptionToRemove.getPrimaryKey())) {
                    if (consumptionsCount > 1 && maAbsenceConsumptions.get(i).getPkConsumptionId() != maxConsumptionId) {
                        throw new Exception("Los consumos de la misma ausencia deben ser removidos en orden inverso.");
                    }

                    maAbsenceConsumptions.remove(i);
                    break;
                }
            }
        }
    }
    
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
                    /*
                    units = SLibUtils.round(!earningNormal.isDaysAdjustment() ?
                            unitsAlleged * hrsEmployeeDays.getFactorCalendar() :
                            unitsAlleged * hrsEmployeeDays.getFactorCalendar() * hrsEmployeeDays.getFactorDaysPaid(),
                            SLibUtils.DecimalFormatValue8D.getMaximumFractionDigits()); // unit needs rounding to 8 decimals
                    amount = SLibUtils.roundAmount(units * earning.getPayrollReceiptEarning().getAmountUnitary() * earningNormal.getUnitsFactor());
                    */
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
    
    public void computePayrollReceiptDays() throws Exception {
        // Compute dbPayrollReceipt values referent to days:
        
        SHrsEmployeeDays hrsEmployeeDays = moHrsEmployee.createEmployeeDays();
        
        moPayrollReceipt.setFactorCalendar(hrsEmployeeDays.getFactorCalendar());
        moPayrollReceipt.setFactorDaysPaid(hrsEmployeeDays.getFactorDaysPaid());
        moPayrollReceipt.setReceiptDays(hrsEmployeeDays.getReceiptDays());
        moPayrollReceipt.setWorkingDays(hrsEmployeeDays.getWorkingDays());
        moPayrollReceipt.setDaysWorked(hrsEmployeeDays.getDaysWorked());
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
    
    private SHrsDaysByPeriod createHrsDaysByPeriod(final int year, final int periodYear) {
        int daysPeriod = SLibTimeUtils.digestDate(SLibTimeUtils.getEndOfMonth(SLibTimeUtils.createDate(year, periodYear)))[2];
        int daysPeriodPayroll = 0;
        
        for (SDbEmployeeHireLog hireLog : moHrsEmployee.getEmployeeHireLogs()) {
            Date dateStart = hireLog.getDateHire().compareTo(moHrsEmployee.getPeriodStart()) <= 0 ? moHrsEmployee.getPeriodStart() : hireLog.getDateHire();
            Date dateEnd = hireLog.getDateDismissed_n() == null ? moHrsEmployee.getPeriodEnd() : hireLog.getDateDismissed_n().compareTo(moHrsEmployee.getPeriodEnd()) >= 0 ? moHrsEmployee.getPeriodEnd() : hireLog.getDateDismissed_n();
            
            int periodDays = (int) SLibTimeUtils.getDaysDiff(dateEnd, dateStart);
            
            for (int i = 0; i <= periodDays; i++) {
                if (SLibTimeUtils.isBelongingToPeriod(SLibTimeUtils.addDate(dateStart, 0, 0, i), year, periodYear)) {
                    daysPeriodPayroll++;
                }
            }
        }
        
        return new SHrsDaysByPeriod(year, periodYear, daysPeriod, daysPeriodPayroll);
    }
    
    public double calculateBenefit(final SDbEarning earningBenefit, SHrsEmployeeDays hrsEmployeeDays, final double days, final double percentage) {
        double units = hrsEmployeeDays.computeEarningUnits(days, earningBenefit);
        return SLibUtils.roundAmount(units * getPayrollReceipt().getPaymentDaily() * (earningBenefit.getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_VAC_BON ? percentage : 1d));
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

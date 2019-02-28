/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.lib.SLibUtilities;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;

/**
 *
 * @author Néstor Ávalos, Sergio Flores, Juan Barajas, Sergio Flores
 * 2019-01-29 Sergio Flores: Corrección al cálculo de parte excenta en percepciones. Debe estar en en función de UMA desde 2017.
 */
public class SHrsPayrollReceipt {

    private SDbPayrollReceipt moReceipt;
    private SHrsPayroll moHrsPayroll;
    private SHrsEmployee moHrsEmployee;
    private final ArrayList<SDbAbsenceConsumption> maAbsenceConsumptions;
    private final ArrayList<SHrsPayrollReceiptEarning> maHrsReceiptEarnings;
    private final ArrayList<SHrsPayrollReceiptDeduction> maHrsReceiptDeductions;
    private final ArrayList<SHrsBenefit> maHrsBenefits;

    public SHrsPayrollReceipt() {
        moReceipt = null;
        moHrsPayroll = null;
        moHrsEmployee = null;
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

        for (SHrsPayrollReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            if (!hrsReceiptEarning.getEarning().isAlternativeTaxCalculation()) {
                total = SLibUtils.roundAmount(total + hrsReceiptEarning.getReceiptEarning().getAmountTaxable());
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

        for (SHrsPayrollReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            if (hrsReceiptEarning.getEarning().isAlternativeTaxCalculation()) {
                total = SLibUtils.roundAmount(total + hrsReceiptEarning.getReceiptEarning().getAmountTaxable());
            }
        }

        return total;
    }
    
    /**
     * Compute all earnings of this receipt.
     */
    private void computeEarnings() {
        for (SHrsPayrollReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            hrsReceiptEarning.computeEarning();
        }
    }

    /**
     * Compute exemption of all earnings of this receipt by grouping them in blocks of arrays.
     * @throws Exception 
     */
    private void computeEarningsExemption() throws Exception {
        // Group same earnings:
        
        HashMap<Integer, ArrayList<SHrsPayrollReceiptEarning>> earningsArraysMap = new HashMap<>(); // key: earning ID; value: array of earnings
        
        // Process all receipt earnings:

        for (SHrsPayrollReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            int earningId = hrsReceiptEarning.getEarning().getPkEarningId();
            ArrayList<SHrsPayrollReceiptEarning> earningsArray = earningsArraysMap.get(earningId);
            
            if (earningsArray == null) {
                earningsArray = new ArrayList<>();
            }

            earningsArray.add(hrsReceiptEarning);
            earningsArraysMap.put(earningId, earningsArray);
        }

        // Compute array of earnings exemption in group:

        for (ArrayList<SHrsPayrollReceiptEarning> earningsArray : earningsArraysMap.values()) {
            computeEarningsExemptionGroup(earningsArray);
        }
    }

    /**
     * Compute altogether the exemption of the given array of earnings as a group.
     * @param earningsArray Array of earnings. At least one earning is expected.
     * @throws Exception 
     */
    private void computeEarningsExemptionGroup(final ArrayList<SHrsPayrollReceiptEarning> earningsArray) throws Exception {
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
        SDbEarning dbEarning = earningsArray.get(0).getEarning();
        
        // check if exemption apply (standard and optional settings):
        boolean applyExemptionStd = dbEarning.getFkEarningExemptionTypeId() != SModSysConsts.HRSS_TP_EAR_EXEM_NON; // variable improves readability!
        boolean applyExemptionStdGlobal = dbEarning.getFkEarningExemptionTypeId() == SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_GBL; // variable improves readability!
        boolean applyExemptionYearGlobal = dbEarning.getFkEarningExemptionTypeYearId() == SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_GBL; // variable improves readability!
        double exemptionPayroll = 0; // preserve exemption granted in current payroll
        double exemptionGlobal = 0; // preserve exemption granted in other payrolls in current year

        // get accumulated exemption granted in current year if necessary:
        if (applyExemptionStdGlobal || applyExemptionYearGlobal) {
            SHrsAccumulatedEarning hrsAccumEarning = moHrsEmployee.getAccumulatedEarning(dbEarning.getPkEarningId());
            if (hrsAccumEarning != null) {
                exemptionGlobal = hrsAccumEarning.getExemption();
            }
        }

        // compute all receipt earnings in given group:
        for (SHrsPayrollReceiptEarning hrsReceiptEarning : earningsArray) {
            double exemption = 0;
            SDbPayrollReceiptEarning dbReceiptEarning = hrsReceiptEarning.getReceiptEarning();

            // compute exemption:
            if (applyExemptionStd) {
                double exemptionProp = 0;
                double exemptionLimit = 0;
                
                switch (dbEarning.getFkEarningExemptionTypeId()) {
                    case SModSysConsts.HRSS_TP_EAR_EXEM_PER: // Percentage
                        // estimate exemption proposed and exemption limit:
                        if (moReceipt.getEffectiveSalary(moHrsPayroll.getPayroll().isFortnightStandard()) <= moHrsPayroll.getPayroll().getMwzWage()) { // salary cannot never be less than minimum wage, but just in case
                            exemptionProp = SLibUtils.roundAmount(dbEarning.getExemptionSalaryEqualsMwzPercentage() * dbReceiptEarning.getAmount_r());
                            exemptionLimit = SLibUtils.roundAmount(dbEarning.getExemptionSalaryEqualsMwzLimit() * moHrsPayroll.getPayroll().getUmaAmount()); // formerly minimum wage was used
                        }
                        else {
                            exemptionProp = SLibUtils.roundAmount(dbEarning.getExemptionSalaryGreaterMwzPercentage() * dbReceiptEarning.getAmount_r());
                            exemptionLimit = SLibUtils.roundAmount(dbEarning.getExemptionSalaryGreaterMwzLimit() * moHrsPayroll.getPayroll().getUmaAmount()); // formerly minimum wage was used
                        }
                        break;

                    case SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_GBL: // Minimum Wage Global
                    case SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_EVT: // Minimum Wage Event
                        // estimate exemption proposed and exemption limit:
                        exemptionProp = SLibUtils.roundAmount(dbEarning.getExemptionMwz() * moHrsPayroll.getPayroll().getUmaAmount()); // formerly minimum wage was used
                        exemptionLimit = exemptionProp;
                        break;

                    case SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_SEN: // Minimum Wage Seniority
                        // compute exact seniority:
                        int years = moHrsEmployee.getSeniority();
                        int yearDays = (int) SLibTimeUtils.getDaysDiff(moHrsPayroll.getPayroll().getDateEnd(), SLibTimeUtils.addDate(moHrsEmployee.getEmployee().getDateBenefits(), years, 0, 0));
                        double seniority = (double) years + ((double) yearDays / SHrsConsts.YEAR_DAYS);

                        // estimate exemption proposed and exemption limit:
                        exemptionProp = SLibUtils.roundAmount(dbEarning.getExemptionMwz() * moHrsPayroll.getPayroll().getUmaAmount() * seniority); // formerly minimum wage was used
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

                    if (exemption > dbReceiptEarning.getAmount_r()) {
                        exemption = dbReceiptEarning.getAmount_r();
                    }
                }

                // accumulate payroll and global exemption:
                exemptionPayroll = SLibUtils.roundAmount(exemptionPayroll + exemption);
                exemptionGlobal = SLibUtils.roundAmount(exemptionGlobal + exemption);
            }

            // set earning exemption:
            dbReceiptEarning.setAmountExempt(exemption);
            dbReceiptEarning.setAmountTaxable(SLibUtils.roundAmount(dbReceiptEarning.getAmount_r() - exemption));
        }
    }
    
    private void computeDeductions() {
        for (SHrsPayrollReceiptDeduction receiptDeduction : maHrsReceiptDeductions) {
            receiptDeduction.computeDeduction();
        }
    }

    /**
     * Creates and adds earning of $0.01 if subsidy was totally offset against tax.
     * Otherwise this earning is removed when existing.
     * @param isSubCompensated
     * @param dbEarningOther
     * @throws Exception 
     */
    private void createEarningOther(final boolean isSubCompensated, final SDbEarning dbEarningOther) throws Exception {
        int countSub = 0;
        SHrsPayrollReceiptEarning hrsReceiptEarningOtherOld = null;
        
        for (SHrsPayrollReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            SDbPayrollReceiptEarning dbReceiptEarning = hrsReceiptEarning.getReceiptEarning();
            
            if (dbReceiptEarning.getFkEarningTypeId() == SModSysConsts.HRSS_TP_EAR_OTH && dbReceiptEarning.isAutomatic()) {
                if (++countSub > 1) {
                    throw new Exception("¡No puede haber más de una percepcion 'Subsidio para el empleo' en el recibo!");
                }
                
                // prepare for update or removal of existing subsidy earning:
                hrsReceiptEarningOtherOld = hrsReceiptEarning;
            }
        }
        
        if (!isSubCompensated) {
            // no subsidy compensated:
            
            if (hrsReceiptEarningOtherOld != null) {
                maHrsReceiptEarnings.remove(hrsReceiptEarningOtherOld);
                renumberEarnings();
            }
        }
        else {
            // subsidy compensated:
            
            SDbPayrollReceiptEarning dbReceiptEarningOther;
            SHrsPayrollReceiptEarning hrsReceiptEarningOther;
            
            if (hrsReceiptEarningOtherOld != null) {
                dbReceiptEarningOther = hrsReceiptEarningOtherOld.getReceiptEarning();
                hrsReceiptEarningOther = hrsReceiptEarningOtherOld;
            }
            else {
                dbReceiptEarningOther = new SDbPayrollReceiptEarning();
                dbReceiptEarningOther.setPkPayrollId(moReceipt.getPkPayrollId());
                dbReceiptEarningOther.setPkEmployeeId(moReceipt.getPkEmployeeId());
                dbReceiptEarningOther.setPkMoveId(maHrsReceiptEarnings.size() + 1);
                
                hrsReceiptEarningOther = new SHrsPayrollReceiptEarning();
                hrsReceiptEarningOther.setPkMoveId(dbReceiptEarningOther.getPkMoveId());
            }
            
            dbReceiptEarningOther.setUnitsAlleged(1);
            dbReceiptEarningOther.setUnits(1);
            dbReceiptEarningOther.setFactorAmount(1);
            dbReceiptEarningOther.setAmountUnitary(0.01);
            dbReceiptEarningOther.setAmountSystem_r(0.01);
            dbReceiptEarningOther.setAmount_r(0.01);
            dbReceiptEarningOther.setAmountExempt(0.01);
            dbReceiptEarningOther.setAutomatic(true);
            dbReceiptEarningOther.setAlternativeTaxCalculation(dbEarningOther.isAlternativeTaxCalculation());
            dbReceiptEarningOther.setFkEarningId(dbEarningOther.getPkEarningId());
            dbReceiptEarningOther.setFkEarningTypeId(dbEarningOther.getFkEarningTypeId());
            dbReceiptEarningOther.setFkBenefitTypeId(dbEarningOther.getFkBenefitTypeId());
            
            hrsReceiptEarningOther.setHrsReceipt(this);
            hrsReceiptEarningOther.setEarning(dbEarningOther);
            hrsReceiptEarningOther.setReceiptEarning(dbReceiptEarningOther);
            hrsReceiptEarningOther.setXtaValueAlleged(dbReceiptEarningOther.getUnitsAlleged());
            hrsReceiptEarningOther.setXtaValue(dbReceiptEarningOther.getUnits());

            if (hrsReceiptEarningOtherOld == null) {
                maHrsReceiptEarnings.add(hrsReceiptEarningOther);
                renumberEarnings();
            }
        }
    }

    private void computeReceiptTax() throws Exception {
        // Clear tax and subsidy existing calculations:

        moReceipt.setPayrollFactorTax(0);
        moReceipt.setPayrollTaxAssessed(0);
        moReceipt.setPayrollTaxCompensated(0);
        moReceipt.setPayrollTaxPending_r(0);
        moReceipt.setPayrollTaxPayed(0);
        moReceipt.setPayrollTaxSubsidyAssessed(0);
        moReceipt.setPayrollTaxSubsidyCompensated(0);
        moReceipt.setPayrollTaxSubsidyPending_r(0);
        moReceipt.setPayrollTaxSubsidyPayed(0);
        
        moReceipt.setAnnualFactorTax(0);
        moReceipt.setAnnualTaxAssessed(0);
        moReceipt.setAnnualTaxCompensated(0);
        moReceipt.setAnnualTaxPayed(0);
        moReceipt.setAnnualTaxSubsidyAssessed(0);
        moReceipt.setAnnualTaxSubsidyCompensated(0);
        moReceipt.setAnnualTaxSubsidyPayed(0);

        double annualTaxCompensated = 0; // annual tax compensated
        double annualTaxPayed = 0;       // annual tax payed (withheld from employee)
        double annualSubCompensated = 0; // annual subsidy compensated
        double annualSubPayed = 0;       // annual subsidy payed (to employee)
        double taxAssessed = 0;
        double taxPending = 0;
        double subAssessed = 0;
        double subPending = 0;
        
        SHrsPayrollReceiptDeduction hrsReceiptDeductionTaxNew = null;
        SHrsPayrollReceiptEarning hrsReceiptEarningSubNew = null;
        
        // Check if tax computation is needed:
        
        SDbDeduction dbDeductionTax = null;
        SDbEarning dbEarningSub = null;
        SDbEarning dbEarningOther = null;
        boolean computeTax = SLibUtils.belongsTo(moHrsPayroll.getPayroll().getFkTaxComputationTypeId(), new int[] { SModSysConsts.HRSS_TP_TAX_COMP_PAY, SModSysConsts.HRSS_TP_TAX_COMP_ANN });
        boolean computeSub = computeTax & moHrsPayroll.getPayroll().isTaxSubsidy() && !moHrsEmployee.getEmployee().isAssimilable(); // assimilables are not elegible for subsidy

        if (computeTax) {
            // Validate deduction configuration for tax:

            if (moHrsPayroll.getConfig().getFkDeductionTaxId_n() == SLibConsts.UNDEFINED) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Deducción de impuesto)");
            }

            dbDeductionTax = moHrsPayroll.getDataDeduction(moHrsPayroll.getConfig().getFkDeductionTaxId_n());
            if (dbDeductionTax == null) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Deducción de impuesto)");
            }

            if (dbDeductionTax.getFkDeductionTypeId() != SModSysConsts.HRSS_TP_DED_TAX) {
                throw new Exception(SLibConsts.ERR_MSG_WRONG_TYPE + " (Tipo de deducción en deducción de impuesto)");
            }

            if (moHrsPayroll.getPayroll().getFkTaxId() == SLibConsts.UNDEFINED) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Tabla de impuesto)");
            }

            SDbTaxTable dbTaxTable = moHrsPayroll.getTaxTable(moHrsPayroll.getPayroll().getFkTaxId());
            if (dbTaxTable == null)  {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Tabla de impuesto)");
            }

            // Validate earning configuration for subsidy:

            if (moHrsPayroll.getConfig().getFkEarningTaxSubsidyId_n() == SLibConsts.UNDEFINED) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Percepción de subsidio para el empleo)");
            }

            dbEarningSub = moHrsPayroll.getDataEarning(moHrsPayroll.getConfig().getFkEarningTaxSubsidyId_n());
            if (dbEarningSub == null) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Percepción de subsidio para el empleo)");
            }

            if (dbEarningSub.getFkEarningTypeId() != SModSysConsts.HRSS_TP_EAR_TAX_SUB) {
                throw new Exception(SLibConsts.ERR_MSG_WRONG_TYPE + " (Tipo de percepción en percepción de subsidio para el empleo)");
            }

            if (moHrsPayroll.getPayroll().getFkTaxSubsidyId() == SLibConsts.UNDEFINED) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Tabla de subsidio para el empleo)");
            }

            SDbTaxSubsidyTable dbSubTable = moHrsPayroll.getTaxSubsidyTable(moHrsPayroll.getPayroll().getFkTaxSubsidyId());
            if (dbSubTable == null)  {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Tabla de subsidio para el empleo)");
            }

            // Validate earning configuration for compensated subsidy:

            if (moHrsPayroll.getConfig().getFkEarningTaxSubsidyCompensatedId_n() == SLibConsts.UNDEFINED) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Percepción de subsidio para el empleo compensado)");
            }

            dbEarningOther = moHrsPayroll.getDataEarning(moHrsPayroll.getConfig().getFkEarningTaxSubsidyCompensatedId_n());
            if (dbEarningOther == null) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Percepción de subsidio para el empleo compensado)");
            }

            if (dbEarningOther.getFkEarningTypeId() != SModSysConsts.HRSS_TP_EAR_OTH) {
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
                    
                    SHrsAccumulatedDeduction hrsAccumulatedTax = moHrsEmployee.getAccumulatedDeductionByType(SModSysConsts.HRSS_TP_DED_TAX);
                    if (hrsAccumulatedTax != null) {
                        annualTaxPayed = SLibUtils.roundAmount(hrsAccumulatedTax.getAcummulated());
                    }
                    
                    annualSubCompensated = moHrsEmployee.getAnnualTaxSubsidyCompensated(); // should be equal to compensated annual tax

                    SHrsAccumulatedEarning hrsAccumulatedSub = moHrsEmployee.getAccumulatedEarningByType(SModSysConsts.HRSS_TP_EAR_TAX_SUB);
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
            SHrsEmployeeDays hrsEmployeeDays = moHrsEmployee.getEmployeeDays();
            
            if (moHrsPayroll.getPayroll().getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_FOR && moHrsPayroll.getConfig().isFortnightStandard()) {
                tableFactorPayroll = ((double) SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS_FORTNIGHTS_FIXED) * hrsEmployeeDays.getPayrollTaxableDays_r();
                tableFactorAnnual = ((double) SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS_FORTNIGHTS_FIXED) * hrsEmployeeDays.getAnnualTaxableDays_r();
            }
            else {
                tableFactorPayroll = ((double) SHrsConsts.YEAR_MONTHS / (SHrsConsts.YEAR_DAYS + (SLibTimeUtils.isLeapYear(moHrsPayroll.getPayroll().getFiscalYear()) ? 1d : 0d))) * hrsEmployeeDays.getPayrollTaxableDays_r();
                tableFactorAnnual = ((double) SHrsConsts.YEAR_MONTHS / (SHrsConsts.YEAR_DAYS + (SLibTimeUtils.isLeapYear(moHrsPayroll.getPayroll().getFiscalYear()) ? 1d : 0d))) * hrsEmployeeDays.getAnnualTaxableDays_r();
            }
            
            moReceipt.setPayrollFactorTax(tableFactorPayroll);
            moReceipt.setAnnualFactorTax(tableFactorAnnual);
            
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
                double taxAssessedAlt = SHrsUtils.computeAmountTaxAlt(dbTaxTable, earningsTaxableArt174, moReceipt.getPaymentMonthly(), tableFactor);
                taxAssessed = SLibUtils.roundAmount(taxAssessed + taxAssessedAlt);
            }

            if (taxAssessed != 0 && taxAssessed > (annualTaxCompensated + annualTaxPayed)) {
                taxPending = SLibUtils.roundAmount(taxAssessed - (annualTaxCompensated + annualTaxPayed));
                
                SDbPayrollReceiptDeduction dbReceiptDeductionTax = new SDbPayrollReceiptDeduction();
                dbReceiptDeductionTax.setPkPayrollId(moReceipt.getPkPayrollId());
                dbReceiptDeductionTax.setPkEmployeeId(moReceipt.getPkEmployeeId());
                dbReceiptDeductionTax.setPkMoveId(maHrsReceiptDeductions.size() + 1);
                dbReceiptDeductionTax.setUnitsAlleged(1);
                dbReceiptDeductionTax.setUnits(1);
                dbReceiptDeductionTax.setAmountUnitary(taxPending);  
                dbReceiptDeductionTax.setAmountSystem_r(taxPending);
                dbReceiptDeductionTax.setAmount_r(taxPending);
                dbReceiptDeductionTax.setAutomatic(true);
                dbReceiptDeductionTax.setFkDeductionId(dbDeductionTax.getPkDeductionId());
                dbReceiptDeductionTax.setFkDeductionTypeId(dbDeductionTax.getFkDeductionTypeId());
                dbReceiptDeductionTax.setFkBenefitTypeId(dbDeductionTax.getFkBenefitTypeId());

                hrsReceiptDeductionTaxNew = new SHrsPayrollReceiptDeduction();
                hrsReceiptDeductionTaxNew.setPkMoveId(dbReceiptDeductionTax.getPkMoveId());
                hrsReceiptDeductionTaxNew.setHrsReceipt(this);
                hrsReceiptDeductionTaxNew.setDeduction(dbDeductionTax);
                hrsReceiptDeductionTaxNew.setReceiptDeduction(dbReceiptDeductionTax);
                hrsReceiptDeductionTaxNew.setXtaValueAlleged(dbReceiptDeductionTax.getUnitsAlleged()); // XXX 2019-02-01, Sergio Flores: This is only experimental. Just for consistence. If it works, please remove this comment!
                hrsReceiptDeductionTaxNew.setXtaValue(dbReceiptDeductionTax.getUnits()); // XXX 2019-02-01, Sergio Flores: This is only experimental. Just for consistence. If it works, please remove this comment!
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
                    double subsidyComputedAlt = SHrsUtils.computeAmountTaxSubsidyAlt(dbSubTable, earningsTaxableArt174, moReceipt.getPaymentMonthly(), tableFactor);
                    subAssessed = SLibUtils.roundAmount(subAssessed + subsidyComputedAlt);
                }
                
                if (subAssessed != 0 && subAssessed > (annualSubCompensated + annualSubPayed)) {
                    subPending = SLibUtils.roundAmount(subAssessed - (annualSubCompensated + annualSubPayed));

                    SDbPayrollReceiptEarning dbReceiptEarningSub = new SDbPayrollReceiptEarning();
                    dbReceiptEarningSub.setPkPayrollId(moReceipt.getPkPayrollId());
                    dbReceiptEarningSub.setPkEmployeeId(moReceipt.getPkEmployeeId());
                    dbReceiptEarningSub.setPkMoveId(maHrsReceiptEarnings.size() + 1);
                    dbReceiptEarningSub.setUnitsAlleged(1);
                    dbReceiptEarningSub.setUnits(1);
                    dbReceiptEarningSub.setFactorAmount(1);
                    dbReceiptEarningSub.setAmountUnitary(subPending);
                    dbReceiptEarningSub.setAmountSystem_r(subPending);
                    dbReceiptEarningSub.setAmount_r(subPending);
                    dbReceiptEarningSub.setAmountExempt(subPending);
                    dbReceiptEarningSub.setAutomatic(true);
                    dbReceiptEarningSub.setAlternativeTaxCalculation(dbEarningSub.isAlternativeTaxCalculation());
                    dbReceiptEarningSub.setFkEarningId(dbEarningSub.getPkEarningId());
                    dbReceiptEarningSub.setFkEarningTypeId(dbEarningSub.getFkEarningTypeId());
                    dbReceiptEarningSub.setFkBenefitTypeId(dbEarningSub.getFkBenefitTypeId());

                    hrsReceiptEarningSubNew = new SHrsPayrollReceiptEarning();
                    hrsReceiptEarningSubNew.setPkMoveId(dbReceiptEarningSub.getPkMoveId());
                    hrsReceiptEarningSubNew.setHrsReceipt(this);
                    hrsReceiptEarningSubNew.setEarning(dbEarningSub);
                    hrsReceiptEarningSubNew.setReceiptEarning(dbReceiptEarningSub);
                    hrsReceiptEarningSubNew.setXtaValueAlleged(dbReceiptEarningSub.getUnitsAlleged());
                    hrsReceiptEarningSubNew.setXtaValue(dbReceiptEarningSub.getUnits());
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
        SHrsPayrollReceiptDeduction hrsReceiptDeductionTaxOld = null;

        for (SHrsPayrollReceiptDeduction hrsReceiptDeduction : maHrsReceiptDeductions) {
            SDbPayrollReceiptDeduction dbReceiptDeduction = hrsReceiptDeduction.getReceiptDeduction();
            
            if (dbReceiptDeduction.getFkDeductionTypeId() == SModSysConsts.HRSS_TP_DED_TAX) {
                if (++countTax > 1) {
                    throw new Exception("¡No puede haber más de una deducción 'ISR' en el recibo!");
                }
                
                // prepare for removal or update of existing tax deduction:
                hrsReceiptDeductionTaxOld = hrsReceiptDeduction;
                
                if (dbReceiptDeduction.isUserEdited() || !dbReceiptDeduction.isAutomatic()) {
                    // user edited tax:
                    isUserTax = true;
                    userTax = dbReceiptDeduction.getAmount_r();
                }
            }
        }
        
        // Asign payroll receipt tax:
        
        double payrollTax = isTaxNet ? (taxNet > 0 ? taxNet : 0) : taxPending;
        
        if (hrsReceiptDeductionTaxOld != null || hrsReceiptDeductionTaxNew != null) {
            if (isUserTax) {
                // preserve computed tax:
                SDbPayrollReceiptDeduction deduction = hrsReceiptDeductionTaxOld.getReceiptDeduction();
                deduction.setAmountSystem_r(payrollTax);
            }
            else if (hrsReceiptDeductionTaxOld != null) {
                if (payrollTax > 0) {
                    // update former deduction:
                    SDbPayrollReceiptDeduction deduction = hrsReceiptDeductionTaxOld.getReceiptDeduction();
                    deduction.setAmountUnitary(payrollTax);
                    deduction.setAmountSystem_r(payrollTax);
                    deduction.setAmount_r(payrollTax);
                }
                else {
                    // remove former deduction:
                    maHrsReceiptDeductions.remove(hrsReceiptDeductionTaxOld);
                    renumberDeductions();
                }
            }
            else if (payrollTax > 0) {
                // add new deduction:
                
                if (isTaxNet) {
                    // reasign tax; when created new deduction was asigned with tax pending:
                    SDbPayrollReceiptDeduction deduction = hrsReceiptDeductionTaxNew.getReceiptDeduction();
                    deduction.setAmountUnitary(payrollTax);
                    deduction.setAmountSystem_r(payrollTax);
                    deduction.setAmount_r(payrollTax);
                }
                
                maHrsReceiptDeductions.add(hrsReceiptDeductionTaxNew);
                renumberDeductions();
            }
        }
        
        // Remove or update previous subsidy:
        
        int countSub = 0;
        double userSub = 0;
        boolean isUserSub = false;
        SHrsPayrollReceiptEarning hrsReceiptEarningSubOld = null;
        
        for (SHrsPayrollReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            SDbPayrollReceiptEarning dbReceiptEarning = hrsReceiptEarning.getReceiptEarning();
            
            if (dbReceiptEarning.getFkEarningTypeId() == SModSysConsts.HRSS_TP_EAR_TAX_SUB) {
                if (++countSub > 1) {
                    throw new Exception("¡No puede haber más de una percepcion 'Subsidio para el empleo' en el recibo!");
                }
                
                // prepare for update or removal of existing subsidy earning:
                hrsReceiptEarningSubOld = hrsReceiptEarning;
                
                if (dbReceiptEarning.isUserEdited() || !dbReceiptEarning.isAutomatic()) {
                    // user edited subsidy:
                    isUserSub = true;
                    userSub = dbReceiptEarning.getAmount_r();
                }
            }
        }
        
        // Asign payroll receipt subsidy:
        
        double payrollSub = isTaxNet ? (taxNet < 0 ? -taxNet : 0) : subPending;
        
        if (hrsReceiptEarningSubOld != null || hrsReceiptEarningSubNew != null) {
            if (isUserSub) {
                // preserve computed subsidy:
                SDbPayrollReceiptEarning earning = hrsReceiptEarningSubOld.getReceiptEarning();
                earning.setAmountSystem_r(payrollSub);
                earning.setAmountExempt(userSub); // exempt user edited subsidy
            }
            else if (hrsReceiptEarningSubOld != null) {
                if (payrollSub > 0) {
                    // update former earning:
                    SDbPayrollReceiptEarning earning = hrsReceiptEarningSubOld.getReceiptEarning();
                    earning.setAmountUnitary(payrollSub);
                    earning.setAmountSystem_r(payrollSub);
                    earning.setAmount_r(payrollSub);
                    earning.setAmountExempt(payrollSub);
                }
                else {
                    // remove former earning:
                    maHrsReceiptEarnings.remove(hrsReceiptEarningSubOld);
                    renumberEarnings();
                }
            }
            else if (payrollSub > 0) {
                // add new earning:
                
                if (isTaxNet) {
                    // reasign subsidy; when created new earning was asigned with subsidy pending:
                    SDbPayrollReceiptEarning earning = hrsReceiptEarningSubNew.getReceiptEarning();
                    earning.setAmountUnitary(payrollSub);
                    earning.setAmountSystem_r(payrollSub);
                    earning.setAmount_r(payrollSub);
                    earning.setAmountExempt(payrollSub);
                }
                
                maHrsReceiptEarnings.add(hrsReceiptEarningSubNew);
                renumberEarnings();
            }
        }
        
        createEarningOther(isTaxNet && subPending > 0 && taxPending > subPending, dbEarningOther);
        
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

            moReceipt.setPayrollTaxAssessed(computeTaxByPayroll ? taxAssessed : taxPending);
            moReceipt.setPayrollTaxCompensated(payrollTaxCompensated);
            moReceipt.setPayrollTaxPending_r(payrollTax);
            moReceipt.setPayrollTaxPayed(isUserTax ? userTax : payrollTax);
            moReceipt.setPayrollTaxSubsidyAssessed(computeTaxByPayroll ? subAssessed : subPending);
            moReceipt.setPayrollTaxSubsidyCompensated(payrollSubCompensated);
            moReceipt.setPayrollTaxSubsidyPending_r(payrollSub);
            moReceipt.setPayrollTaxSubsidyPayed(isUserSub ? userSub : payrollSub);
            
            switch (moHrsPayroll.getPayroll().getFkTaxComputationTypeId()) {
                case SModSysConsts.HRSS_TP_TAX_COMP_PAY: // Payroll
                    moReceipt.setAnnualTaxAssessed(0);
                    moReceipt.setAnnualTaxCompensated(0);
                    moReceipt.setAnnualTaxPayed(0);
                    moReceipt.setAnnualTaxSubsidyAssessed(0);
                    moReceipt.setAnnualTaxSubsidyCompensated(0);
                    moReceipt.setAnnualTaxSubsidyPayed(0);
                    break;

                case SModSysConsts.HRSS_TP_TAX_COMP_ANN: // Annual
                    moReceipt.setAnnualTaxAssessed(taxAssessed);
                    moReceipt.setAnnualTaxCompensated(annualTaxCompensated);
                    moReceipt.setAnnualTaxPayed(annualTaxPayed);
                    moReceipt.setAnnualTaxSubsidyAssessed(subAssessed);
                    moReceipt.setAnnualTaxSubsidyCompensated(annualSubCompensated);
                    moReceipt.setAnnualTaxSubsidyPayed(annualSubPayed);
                    break;

                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN); // unnecessary, just for consistence!
            }
        }
        
        /* 2018-02-06, Sergio Flores: Original code preserved, just in case!
        // Adequate tax and subsidy into payroll:
        
        boolean isTaxNet = moHrsPayroll.getConfig().isTaxNet();
        
        // Remove or update previous tax:

        int deductions = 0;
        double dUserTax = 0;
        boolean isUserTax = false;
        ArrayList<SHrsPayrollReceiptDeduction> hrsReceiptDeductionsToProcess = new ArrayList<>();

        for (SHrsPayrollReceiptDeduction hrsReceiptDeduction : maHrsReceiptDeductions) {
            SDbPayrollReceiptDeduction dbReceiptDeduction = hrsReceiptDeduction.getReceiptDeduction();
            
            if (dbReceiptDeduction.getFkDeductionTypeId() == SModSysConsts.HRSS_TP_DED_TAX) {
                if (++deductions > 1) {
                    throw new Exception("¡No puede haber más de una deducción 'ISR' en el recibo!");
                }
                
                if (dbReceiptDeduction.isUserEdited() || !dbReceiptDeduction.isAutomatic()) {
                    // preserve user edited tax, updating only system computed amount:
                    dbReceiptDeduction.setAmountSystem_r(isTaxNet ? (dTaxNet > 0 ? dTaxNet : 0) : dTaxComputed);
                    dUserTax = SLibUtils.roundAmount(dUserTax + SLibUtils.roundAmount(dbReceiptDeduction.getAmount_r()));
                    isUserTax = true;
                    // Note that a user edited tax will remain in receipt even if payroll does not require tax computation!
                }
                else {
                    // process previous tax:
                    hrsReceiptDeductionsToProcess.add(hrsReceiptDeduction); // prepare removal or update of previous system computations
                }
            }
        }
        
        if (!hrsReceiptDeductionsToProcess.isEmpty()) {
            if (isTaxNet ? dTaxNet <= 0 : dTaxComputed <= 0) {
                for (SHrsPayrollReceiptDeduction deductionToRemove : hrsReceiptDeductionsToProcess) {
                    maHrsReceiptDeductions.remove(deductionToRemove);
                }
                renumberDeductions();
            }
            else {
                for (SHrsPayrollReceiptDeduction deductionToUpdate : hrsReceiptDeductionsToProcess) {
                    for (SHrsPayrollReceiptDeduction hrsReceiptDeduction : maHrsReceiptDeductions) {
                        if (hrsReceiptDeduction.getPkMoveId() == deductionToUpdate.getPkMoveId()) {
                            double amount = isTaxNet ? (dTaxNet > 0 ? dTaxNet : 0) : dTaxComputed;
                            hrsReceiptDeduction.getReceiptDeduction().setAmountUnitary(amount);
                            hrsReceiptDeduction.getReceiptDeduction().setAmountSystem_r(amount);
                            hrsReceiptDeduction.getReceiptDeduction().setAmount_r(amount);
                            break;
                        }
                    }
                }
            }
        }
        else if (!isUserTax && hrsReceiptDeductionTax != null) {
            boolean addDeduction = false;
            
            if (!isTaxNet) {
                addDeduction = true;
            }
            else if (dTaxNet > 0) {
                double amount = dTaxNet;
                hrsReceiptDeductionTax.getReceiptDeduction().setAmountUnitary(amount);
                hrsReceiptDeductionTax.getReceiptDeduction().setAmountSystem_r(amount);
                hrsReceiptDeductionTax.getReceiptDeduction().setAmount_r(amount);
                addDeduction = true;
            }
            
            if (addDeduction) {
                maHrsReceiptDeductions.add(hrsReceiptDeductionTax);
                renumberDeductions();
            }
        }
        
        // Remove or update previous subsidy:
        
        int earnings = 0;
        double dUserSubsidy = 0;
        boolean isUserSubsidy = false;
        ArrayList<SHrsPayrollReceiptEarning> hrsReceiptEarningsToProcess = new ArrayList<>();
        
        for (SHrsPayrollReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
            SDbPayrollReceiptEarning dbReceiptEarning = hrsReceiptEarning.getReceiptEarning();
            
            if (dbReceiptEarning.getFkEarningTypeId() == SModSysConsts.HRSS_TP_EAR_TAX_SUB) {
                if (++earnings > 1) {
                    throw new Exception("¡No puede haber más de una percepcion 'Subsidio para el empleo' en el recibo!");
                }
                
                if ((dbReceiptEarning.isUserEdited() || !dbReceiptEarning.isAutomatic()) && computeSubsidy) {
                    // preserve user edited subsidy, updating only system computed amount:
                    dbReceiptEarning.setAmountSystem_r(isTaxNet ? (dTaxNet < 0 ? -dTaxNet : 0) : dSubsidyComputed);
                    dUserSubsidy = SLibUtils.roundAmount(dUserSubsidy + SLibUtils.roundAmount(dbReceiptEarning.getAmount_r()));
                    isUserSubsidy = true;
                    // Note that a user edited subsidy will remain in receipt only if payroll does require subsidy computation!
                }
                else {
                    // process previous subsidy:
                    hrsReceiptEarningsToProcess.add(hrsReceiptEarning); // prepare removal or update of previous system computations
                }
            }
        }

        if (!hrsReceiptEarningsToProcess.isEmpty()) {
            if ((isTaxNet ? dTaxNet >= 0 : dSubsidyComputed <= 0) || !computeSubsidy) {
                for (SHrsPayrollReceiptEarning earningToRemove : hrsReceiptEarningsToProcess) {
                    maHrsReceiptEarnings.remove(earningToRemove);
                }
                renumberEarnings();
            }
            else {
                for (SHrsPayrollReceiptEarning earningToUpdate : hrsReceiptEarningsToProcess) {
                    for (SHrsPayrollReceiptEarning hrsReceiptEarning : maHrsReceiptEarnings) {
                        if (hrsReceiptEarning.getPkMoveId() == earningToUpdate.getPkMoveId()) {
                            double amount = isTaxNet ? (dTaxNet < 0 ? -dTaxNet : 0) : dSubsidyComputed;
                            hrsReceiptEarning.getReceiptEarning().setAmountUnitary(amount);
                            hrsReceiptEarning.getReceiptEarning().setAmountSystem_r(amount);
                            hrsReceiptEarning.getReceiptEarning().setAmount_r(amount);
                            hrsReceiptEarning.getReceiptEarning().setAmountExempt(amount);
                            break;
                        }
                    }
                }
            }
        }
        else if (!isUserSubsidy && hrsReceiptEarningSubsidy != null && computeSubsidy) {
            boolean addSubsidy = false;
            
            if (!isTaxNet) {
                addSubsidy = true;
            }
            else if (dTaxNet < 0) {
                double amount = -dTaxNet;
                hrsReceiptEarningSubsidy.getReceiptEarning().setAmountUnitary(amount);
                hrsReceiptEarningSubsidy.getReceiptEarning().setAmountSystem_r(amount);
                hrsReceiptEarningSubsidy.getReceiptEarning().setAmount_r(amount);
                hrsReceiptEarningSubsidy.getReceiptEarning().setAmountExempt(amount);
                addSubsidy = true;
            }
            
            if (addSubsidy) {
                maHrsReceiptEarnings.add(hrsReceiptEarningSubsidy);
                renumberEarnings();
            }
        }
        
        if (computeTax) {
            switch (moHrsPayroll.getPayroll().getFkTaxComputationTypeId()) {
                case SModSysConsts.HRSS_TP_TAX_COMP_PAY: // Payroll
                    moReceipt.setTaxPayrollTheorical(isTaxNet ? (dTaxNet > 0 ? dTaxNet : 0) : dTaxProposed);
                    moReceipt.setTaxSubsidyPayrollTheorical(isTaxNet ? (dTaxNet < 0 ? -dTaxNet : 0) : dSubsidyProposed);
                    
                    moReceipt.setTaxAnnualTheorical(0);
                    moReceipt.setTaxSubsidyAnnualTheorical(0);
                    
                    moReceipt.setTaxAnnualActual(0);
                    moReceipt.setTaxSubsidyAnnualActual(0);
                    break;
                    
                case SModSysConsts.HRSS_TP_TAX_COMP_ANN: // Annual
                    double dTaxNetAccum = (dTaxAccum - dSubsidyAccum);
                    
                    moReceipt.setTaxPayrollTheorical(isTaxNet ? (dTaxNetAccum > 0 ? dTaxNetAccum : 0) : (dTaxProposed <= dTaxAccum ? 0 : dTaxComputed));
                    moReceipt.setTaxSubsidyPayrollTheorical(isTaxNet ? (dTaxNetAccum < 0 ? -dTaxNetAccum : 0) : (dSubsidyProposed <= dSubsidyAccum ? 0 : dSubsidyComputed));
                    
                    moReceipt.setTaxAnnualTheorical(isTaxNet ? (dTaxNet > 0 ? dTaxNet : 0) : dTaxProposed);
                    moReceipt.setTaxSubsidyAnnualTheorical(isTaxNet ? (dTaxNet < 0 ? -dTaxNet : 0) : dSubsidyProposed);
                    
                    moReceipt.setTaxAnnualActual(isTaxNet ? (dTaxNetAccum > 0 ? dTaxNetAccum : 0) : dTaxAccum);
                    moReceipt.setTaxSubsidyAnnualActual(isTaxNet ? (dTaxNetAccum < 0 ? -dTaxNetAccum : 0) : dSubsidyAccum);
                    break;
                    
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN); // unnecessary, just for consistence!
            }
        }
        
        moReceipt.setTaxPayrollActual(isUserTax ? dUserTax : moReceipt.getTaxPayrollTheorical());
        moReceipt.setTaxSubsidyPayrollActual(!computeSubsidy ? 0 : (isUserSubsidy ? dUserSubsidy : moReceipt.getTaxSubsidyPayrollTheorical()));
        */
    }

    private void computeReceiptSsContribution() throws Exception {
        double sscComputed = 0;
        SHrsPayrollReceiptDeduction hrsReceiptDeductionSscNew = null;
        
        if (moHrsPayroll.getPayroll().isSsContribution()) {
            // Validate configuration of SS contribution:

            if (moHrsPayroll.getConfig().getFkDeductionSsContributionId_n() == SLibConsts.UNDEFINED) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración de retención de SS)");
            }

            SDbDeduction dbDeductionSsc = moHrsPayroll.getDataDeduction(moHrsPayroll.getConfig().getFkDeductionSsContributionId_n());
            if (dbDeductionSsc == null) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Configuración de retención de SS)");
            }

            if (dbDeductionSsc.getFkDeductionTypeId() != SModSysConsts.HRSS_TP_DED_SSC) {
                throw new Exception(SLibConsts.ERR_MSG_WRONG_TYPE + " (Configuración de retención de SS)");
            }

            if (moHrsPayroll.getPayroll().getFkSsContributionId() == SLibConsts.UNDEFINED) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración de tabla de retención de SS)");
            }

            SDbSsContributionTable dbSscTable = moHrsPayroll.getSsContributionTable(moHrsPayroll.getPayroll().getFkSsContributionId());
            if (dbSscTable == null)  {
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
            
            for (int row = 0; row < dbSscTable.getChildRows().size(); row++) {
                double sscRow = 0;
                
                SDbSsContributionTableRow dbSscTableRow = dbSscTable.getChildRows().get(row);
                switch(dbSscTableRow.getPkRowId()) {
                    case SHrsConsts.SS_INC_MON: // money
                    case SHrsConsts.SS_INC_PEN: // pensioner
                        sscRow = SLibUtils.roundAmount(daysWorkedAndIncapacityNotPaid * moReceipt.getSalarySscBase());
                        break;
                        
                    case SHrsConsts.SS_INC_KND_SSC_LET: // kind SSC less or equal than limit
                        sscRow = SLibUtils.roundAmount(daysWorkedAndIncapacityNotPaid * moHrsPayroll.getPayroll().getUmaAmount()); // formerly minimum wage of reference zone was used
                        break;
                        
                    case SHrsConsts.SS_INC_KND_SSC_GT: // kind SSC greater than limit
                        double surplus = moReceipt.getSalarySscBase() - (dbSscTableRow.getLowerLimitMwzReference() * moHrsPayroll.getPayroll().getUmaAmount()); // formerly minimum wage of reference zone was used
                        sscRow = surplus <= 0 ? 0 : SLibUtils.roundAmount(daysWorkedAndIncapacityNotPaid * surplus);
                        break;
                        
                    case SHrsConsts.SS_DIS_LIF: // disability & life
                    case SHrsConsts.SS_CRE: // creche
                    case SHrsConsts.SS_RSK: // risk
                    case SHrsConsts.SS_RET: // retirement
                    case SHrsConsts.SS_SEV: // severance
                    case SHrsConsts.SS_HOM: // home
                        sscRow = SLibUtils.roundAmount(daysWorkedAndNotWorkedNotPaid * moReceipt.getSalarySscBase());
                        break;
                        
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                
                sscComputed = SLibUtils.roundAmount(sscComputed + SLibUtils.roundAmount(sscRow * dbSscTableRow.getWorkerPercentage()));
            }
            
            if (sscComputed != 0) {
                SDbPayrollReceiptDeduction dbReceiptDeductionSsc = new SDbPayrollReceiptDeduction();
                dbReceiptDeductionSsc.setPkPayrollId(moReceipt.getPkPayrollId());
                dbReceiptDeductionSsc.setPkEmployeeId(moReceipt.getPkEmployeeId());
                dbReceiptDeductionSsc.setPkMoveId(maHrsReceiptDeductions.size() + 1);
                dbReceiptDeductionSsc.setUnitsAlleged(1);
                dbReceiptDeductionSsc.setUnits(1);
                dbReceiptDeductionSsc.setAmountUnitary(sscComputed);
                dbReceiptDeductionSsc.setAmountSystem_r(dbReceiptDeductionSsc.getAmountUnitary());
                dbReceiptDeductionSsc.setAmount_r(dbReceiptDeductionSsc.getAmountUnitary());
                dbReceiptDeductionSsc.setAutomatic(true);
                dbReceiptDeductionSsc.setFkDeductionId(dbDeductionSsc.getPkDeductionId());
                dbReceiptDeductionSsc.setFkDeductionTypeId(dbDeductionSsc.getFkDeductionTypeId());
                dbReceiptDeductionSsc.setFkBenefitTypeId(dbDeductionSsc.getFkBenefitTypeId());

                hrsReceiptDeductionSscNew = new SHrsPayrollReceiptDeduction();
                hrsReceiptDeductionSscNew.setPkMoveId(dbReceiptDeductionSsc.getPkMoveId());
                hrsReceiptDeductionSscNew.setHrsReceipt(this);
                hrsReceiptDeductionSscNew.setDeduction(dbDeductionSsc);
                hrsReceiptDeductionSscNew.setReceiptDeduction(dbReceiptDeductionSsc);
            }
        }
        
        // Remove or update previous SS contribution:

        int deductions = 0;
        boolean isUserSsc = false;
        SHrsPayrollReceiptDeduction hrsReceiptDeductionSscOld = null;

        for (SHrsPayrollReceiptDeduction hrsReceiptDeduction : maHrsReceiptDeductions) {
            SDbPayrollReceiptDeduction dbReceiptDeduction = hrsReceiptDeduction.getReceiptDeduction();
            
            if (dbReceiptDeduction.getFkDeductionTypeId() == SModSysConsts.HRSS_TP_DED_SSC) {
                if (++deductions > 1) {
                    throw new Exception("¡No puede haber más de una deducción 'Seguridad social' en el recibo!");
                }
                
                // prepare for removal or update of existing SS contribution deduction:
                hrsReceiptDeductionSscOld = hrsReceiptDeduction;
                
                if (dbReceiptDeduction.isUserEdited() || !dbReceiptDeduction.isAutomatic()) {
                    // user edited SS contribution:
                    isUserSsc = true;
                }
            }
        }

        if (hrsReceiptDeductionSscOld != null || hrsReceiptDeductionSscNew != null) {
            if (isUserSsc) {
                // preserve computed SS contribution:
                SDbPayrollReceiptDeduction deduction = hrsReceiptDeductionSscOld.getReceiptDeduction();
                deduction.setAmountSystem_r(sscComputed);
            }
            else if (hrsReceiptDeductionSscOld != null) {
                if (sscComputed > 0) {
                    // update former deduction:
                    SDbPayrollReceiptDeduction deduction = hrsReceiptDeductionSscOld.getReceiptDeduction();
                    deduction.setAmountUnitary(sscComputed);
                    deduction.setAmountSystem_r(sscComputed);
                    deduction.setAmount_r(sscComputed);
                }
                else {
                    // remove former deduction:
                    maHrsReceiptDeductions.remove(hrsReceiptDeductionSscOld);
                    renumberDeductions();
                }
            }
            else if (sscComputed > 0) {
                // add new deduction:
                maHrsReceiptDeductions.add(hrsReceiptDeductionSscNew);
                renumberDeductions();
            }
        }
    }

    private double getDaysWorkedPayed(ArrayList<SHrsPayrollReceiptEarning> aHrsPayrollReceiptEarnings) {
        double daysWorkedPayed = 0;

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : aHrsPayrollReceiptEarnings) {
            if (hrsPayrollReceiptEarning.getEarning().isDaysWorked()) {
                daysWorkedPayed += hrsPayrollReceiptEarning.getReceiptEarning().getUnits();
            }
        }

        return daysWorkedPayed;
    }
    
    private double getDaysToBePaid(ArrayList<SHrsPayrollReceiptEarning> aHrsPayrollReceiptEarnings) {
        double daysToBePaid = 0;

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : aHrsPayrollReceiptEarnings) {
            if (hrsPayrollReceiptEarning.getEarning().isDaysWorked()) {
                daysToBePaid += hrsPayrollReceiptEarning.getReceiptEarning().getUnitsAlleged();
            }
        }

        return daysToBePaid;
    }

    /*
     * Public methods
     */

    public void setReceipt(SDbPayrollReceipt o)  { moReceipt = o; }
    public void setHrsPayroll(SHrsPayroll o) { moHrsPayroll = o; }
    public void setHrsEmployee(SHrsEmployee o) { moHrsEmployee = o; }

    public SDbPayrollReceipt getReceipt()  { return moReceipt; }
    public SHrsPayroll getHrsPayroll() { return moHrsPayroll; }
    public SHrsEmployee getHrsEmployee() { return moHrsEmployee; }
    public ArrayList<SDbAbsenceConsumption> getAbsenceConsumptions() { return maAbsenceConsumptions; }
    public ArrayList<SHrsPayrollReceiptEarning> getHrsReceiptEarnings() { return maHrsReceiptEarnings; }
    public ArrayList<SHrsPayrollReceiptDeduction> getHrsReceiptDeductions() { return maHrsReceiptDeductions; }
    public ArrayList<SHrsBenefit> getHrsBenefits() { return maHrsBenefits; }

    public SHrsPayrollReceiptEarning getEarning(final int moveId) {
        SHrsPayrollReceiptEarning oPayrollReceiptEarning = null;

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : maHrsReceiptEarnings) {
            if (hrsPayrollReceiptEarning.getReceiptEarning().getPkMoveId() == moveId) {
                oPayrollReceiptEarning = hrsPayrollReceiptEarning;
                break;
            }
        }

        return oPayrollReceiptEarning;
    }

    public void renumberEarnings() {
        int i = 0;
        
        Collections.sort(maHrsReceiptEarnings);
        for (SHrsPayrollReceiptEarning earning : maHrsReceiptEarnings) {
            earning.setPkMoveId(++i);
        }
    }

    public SHrsPayrollReceiptEarning addEarning(final SHrsPayrollReceiptEarning hrsPayrollReceiptEarning) {
        try {
            maHrsReceiptEarnings.add(hrsPayrollReceiptEarning);
            renumberEarnings();
            computeReceipt();
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }

        return hrsPayrollReceiptEarning;
    }

    public void replaceEarning(final int moveId, final SHrsPayrollReceiptEarning hrsPayrollReceiptEarning) {
        try {
            for (int i = 0; i < maHrsReceiptEarnings.size(); i++) {
                if (maHrsReceiptEarnings.get(i).getPkMoveId() == moveId) {
                    maHrsReceiptEarnings.set(i, hrsPayrollReceiptEarning);
                    break;
                }
            }
            computeReceipt();
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }

    public void removeEarning(final int moveId) {
        try {
            for (int i = 0; i < maHrsReceiptEarnings.size(); i++) {
                if (maHrsReceiptEarnings.get(i).getPkMoveId() == moveId) {
                    maHrsReceiptEarnings.remove(i);
                    break;
                }
            }
            renumberEarnings();
            computeReceipt();
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }

    public void renumberDeductions() {
        int i = 0;
        
        Collections.sort(maHrsReceiptDeductions);
        for (SHrsPayrollReceiptDeduction deduction : maHrsReceiptDeductions) {
            deduction.setPkMoveId(++i);
        }
    }

    public SHrsPayrollReceiptDeduction getDeduction(final int moveId) {
        SHrsPayrollReceiptDeduction oPayrollReceiptDeduction = null;

        for (SHrsPayrollReceiptDeduction hrsPayrollReceiptDeduction : maHrsReceiptDeductions) {
            if (hrsPayrollReceiptDeduction.getReceiptDeduction().getPkMoveId() == moveId) {
                oPayrollReceiptDeduction = hrsPayrollReceiptDeduction;
                break;
            }
        }

        return oPayrollReceiptDeduction;
    }

    public SHrsPayrollReceiptDeduction addDeduction(final SHrsPayrollReceiptDeduction hrsPayrollReceiptDeduction) {
        try {
            maHrsReceiptDeductions.add(hrsPayrollReceiptDeduction);
            renumberDeductions();
            computeReceipt();
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }

        return hrsPayrollReceiptDeduction;
    }

    public void replaceDeduction(final int moveId, final SHrsPayrollReceiptDeduction hrsPayrollReceiptDeduction) {
        try {
            for (int i = 0; i < maHrsReceiptDeductions.size(); i++) {
                if (maHrsReceiptDeductions.get(i).getPkMoveId() == moveId) {
                    maHrsReceiptDeductions.set(i, hrsPayrollReceiptDeduction);
                    break;
                }
            }
            computeReceipt();
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }

    public void removeDeduction(final int moveId) {
        try {
            for (int i = 0; i < maHrsReceiptDeductions.size(); i++) {
                if (maHrsReceiptDeductions.get(i).getPkMoveId() == moveId) {
                    maHrsReceiptDeductions.remove(i);
                    break;
                }
            }
            renumberDeductions();
            computeReceipt();
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    public void addAbsenceConsumption(final SDbAbsence absence, final SDbAbsenceConsumption absenceConsumption) throws Exception {
        if (validateAbsenceConsumption(absence, absenceConsumption)) {
            maAbsenceConsumptions.add(absenceConsumption);

            if (moReceipt != null) {
                computeReceipt();
            }
        }
    }
    
    public void replaceAbsenceConsumption(final SDbAbsenceConsumption absenceConsumption) {
        try {
            for (int i = 0; i < maAbsenceConsumptions.size(); i++) {
                if (maAbsenceConsumptions.get(i).getPkAbsenceId() == absenceConsumption.getPkAbsenceId() &&
                        maAbsenceConsumptions.get(i).getPkEmployeeId() == absenceConsumption.getPkEmployeeId() &&
                        maAbsenceConsumptions.get(i).getPkConsumptionId() == absenceConsumption.getPkConsumptionId()) {
                    maAbsenceConsumptions.set(i, absenceConsumption);
                    break;
                }
            }
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    public void removeAbsenceConsumption(final SDbAbsenceConsumption absenceConsumption) {
        try {
            for (int i = 0; i < maAbsenceConsumptions.size(); i++) {
                if (maAbsenceConsumptions.get(i).getPkAbsenceId() == absenceConsumption.getPkAbsenceId() &&
                        maAbsenceConsumptions.get(i).getPkEmployeeId() == absenceConsumption.getPkEmployeeId() &&
                        maAbsenceConsumptions.get(i).getPkConsumptionId() == absenceConsumption.getPkConsumptionId()) {
                    maAbsenceConsumptions.remove(i);
                    break;
                }
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
                for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : maHrsReceiptEarnings) {
                    if (SLibUtils.compareKeys(benefit.getBenefitKey(), new int[] { hrsPayrollReceiptEarning.getReceiptEarning().getFkBenefitTypeId(), 
                        hrsPayrollReceiptEarning.getReceiptEarning().getBenefitAnniversary(), hrsPayrollReceiptEarning.getReceiptEarning().getBenefitYear() })) {

                        benefit.setValuePayedReceipt(0d);
                        benefit.setAmountPayedReceipt(0d);

                        // Obtain benefit table row more appropiate for seniority:

                        benefitTable = moHrsPayroll.getBenefitTable(hrsPayrollReceiptEarning.getReceiptEarning().getFkBenefitTypeId(), moHrsPayroll.getPayroll().getDateEnd(), moHrsPayroll.getPayroll().getFkPaymentTypeId());

                        for (SDbBenefitTableRow row : benefitTable.getChildRows()) {
                            if (row.getMonths() >= (hrsPayrollReceiptEarning.getReceiptEarning().getBenefitAnniversary() * SHrsConsts.YEAR_MONTHS)) {
                                benefitTableRow = row;
                                break;
                            }
                        }

                        // Obtain benefit table row more appropiate for seniority, it's for vacation bonus:

                        if (hrsPayrollReceiptEarning.getReceiptEarning().getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_VAC_BON) {
                            benefitTableAux = moHrsPayroll.getBenefitTable(SModSysConsts.HRSS_TP_BEN_VAC, moHrsPayroll.getPayroll().getDateEnd(), moHrsPayroll.getPayroll().getFkPaymentTypeId());
                            
                            for (SDbBenefitTableRow row : benefitTableAux.getChildRows()) {
                                if (row.getMonths() >= (hrsPayrollReceiptEarning.getReceiptEarning().getBenefitAnniversary() * SHrsConsts.YEAR_MONTHS)) {
                                    benefitTableRowAux = row;
                                    break;
                                }
                            }
                        }
                        if (hrsPayrollReceiptEarning.getReceiptEarning().getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_VAC_BON) {
                            benefit.setValue(benefitTableRow.getBenefitBonusPercentage());
                            benefit.setAmount(benefitTableRowAux.getBenefitDays() * moReceipt.getPaymentDaily() * benefitTableRow.getBenefitBonusPercentage());
                        }
                        else {
                            benefit.setValue(benefitTableRow.getBenefitDays());
                            benefit.setAmount(benefitTableRow.getBenefitDays() * moReceipt.getPaymentDaily());
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
        computeDbPayrollReceiptDays();
        computeDeductions();
        computeEarningsExemption();
        computeReceiptTax();
        computeReceiptSsContribution();
        // XXX (jbarajas, 2016-04-11) compute amount per deductions of law
    }
    
    public double getTotalEarnings() {
        double totalEarnings = 0;

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : maHrsReceiptEarnings) {
            if (!hrsPayrollReceiptEarning.getReceiptEarning().isDeleted()) {
                totalEarnings += hrsPayrollReceiptEarning.getReceiptEarning().getAmount_r();
            }
        }

        return totalEarnings;
    }

    public double getTotalEarningsExempt() {
        double totalEarningsExept = 0;

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : maHrsReceiptEarnings) {
            if (!hrsPayrollReceiptEarning.getReceiptEarning().isDeleted()) {
                totalEarningsExept += hrsPayrollReceiptEarning.getReceiptEarning().getAmountExempt();
            }
        }

        return totalEarningsExept;
    }

    public double getTotalEarningsTaxable() {
        double totalEarningsTaxed = 0;

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : maHrsReceiptEarnings) {
            if (!hrsPayrollReceiptEarning.getReceiptEarning().isDeleted()) {
                totalEarningsTaxed += hrsPayrollReceiptEarning.getReceiptEarning().getAmountTaxable();
            }
        }

        return totalEarningsTaxed;
    }
    
    public double getTotalEarningsDependentsDaysWorked() {
        double totalEarnings = 0;

        for (SHrsPayrollReceiptEarning receiptEarning : maHrsReceiptEarnings) {
            if (receiptEarning.getEarning().isDaysWorked()) {
                totalEarnings += receiptEarning.getReceiptEarning().getAmount_r();   
            }
        }

        return totalEarnings;
    }

    public double getTotalDeductions() {
        double totalDeductions = 0;

        for (SHrsPayrollReceiptDeduction hrsPayrollReceiptDeduction : maHrsReceiptDeductions) {
            if (!hrsPayrollReceiptDeduction.getReceiptDeduction().isDeleted()) {
                totalDeductions += hrsPayrollReceiptDeduction.getReceiptDeduction().getAmount_r();
            }
        }

        return totalDeductions;
    }
    
    public double getBenefitValue(final int benefitType, final int benefitAnn, final int benefitYear) {
        double value = 0;

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : maHrsReceiptEarnings) {
            if (SLibUtils.compareKeys(new int[] { benefitType, benefitAnn, benefitYear }, new int[] { hrsPayrollReceiptEarning.getReceiptEarning().getFkBenefitTypeId(), 
                hrsPayrollReceiptEarning.getReceiptEarning().getBenefitAnniversary(), hrsPayrollReceiptEarning.getReceiptEarning().getBenefitYear() })) {
                value += hrsPayrollReceiptEarning.getReceiptEarning().getUnitsAlleged();
            }
        }

        return value;
    }
    
    public double getBenefitAmount(final int benefitType, final int benefitAnn, final int benefitYear) {
        double amount = 0;

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : maHrsReceiptEarnings) {
            if (SLibUtils.compareKeys(new int[] { benefitType, benefitAnn, benefitYear }, new int[] { hrsPayrollReceiptEarning.getReceiptEarning().getFkBenefitTypeId(), 
                hrsPayrollReceiptEarning.getReceiptEarning().getBenefitAnniversary(), hrsPayrollReceiptEarning.getReceiptEarning().getBenefitYear() })) {
                amount += hrsPayrollReceiptEarning.getReceiptEarning().getAmount_r();
            }
        }

        return amount;
    }
    
    public SDbAbsenceConsumption createAbsenceConsumption(final SDbAbsence absence, 
            final Date dateStartConsumption, final Date dateEndConsumption, final int consumptionCurrentDays) throws Exception {
        SDbAbsenceConsumption absenceConsumption = null;
        
        absenceConsumption = new SDbAbsenceConsumption();

        absenceConsumption.setPkEmployeeId(absence.getPkEmployeeId());
        absenceConsumption.setPkAbsenceId(absence.getPkAbsenceId());
        absenceConsumption.setPkConsumptionId(SHrsUtils.getConsumptionNextId(absence, moHrsEmployee));
        absenceConsumption.setDateStart(dateStartConsumption);
        absenceConsumption.setDateEnd(dateEndConsumption);
        absenceConsumption.setEffectiveDays(consumptionCurrentDays);
        absenceConsumption.setDeleted(false);
        //absenceConsumption.setFkReceiptPayrollId();
        absenceConsumption.setFkReceiptEmployeeId(absence.getPkEmployeeId());
        /*
        absenceConsumption.setFkUserInsertId();
        absenceConsumption.setFkUserUpdateId();
        absenceConsumption.setTsUserInsert();
        absenceConsumption.setTsUserUpdate();
        */
        absenceConsumption.setAbsence(absence);
        
        return absenceConsumption;
    }
    
    public void updateHrsPayrollReceiptEarningAbsence(SDbAbsenceConsumption absenceConsumption, boolean add) throws Exception {
        boolean found = false;
        double unit = 0;
        double unitAlleged = 0;
        double amount_unt = 0;
        double amount = 0;
        int businessDays = 0;
        SDbEarning earningNormal;
        SDbPayrollReceiptEarning payrollReceiptEarning = null;
        SHrsPayrollReceiptEarning hrsPayrollReceiptEarning = null;
        ArrayList<SHrsPayrollReceiptEarning> aEarningDelete = null;
        double workingDaysAvailable = 0;
        
        aEarningDelete = new ArrayList<>();
        
        for (SHrsPayrollReceiptEarning earning : moHrsEmployee.getHrsPayrollReceipt().getHrsReceiptEarnings()) {
            if (earning.getReceiptEarning().isSystem()) {
                aEarningDelete.add(earning);
            }
        }
        
        for (SHrsPayrollReceiptEarning earning : aEarningDelete) {
            moHrsEmployee.getHrsPayrollReceipt().removeEarning(earning.getPkMoveId());
        }
        
        workingDaysAvailable = moHrsEmployee.getEmployeeDays().getBusinessDays() - moHrsEmployee.getEmployeeDays().getDaysNotWorked_r();
        businessDays = SHrsUtils.getEmployeeBusinessDays(absenceConsumption.getDateStart(), absenceConsumption.getDateEnd(), moHrsPayroll.getHolidays(), moHrsPayroll.getWorkingDaySettings());
        unitAlleged = (absenceConsumption.getAbsence().getFkAbsenceClassId() == SModSysConsts.HRSU_CL_ABS_DIS ? businessDays : absenceConsumption.getEffectiveDays());
        
        // Update normal earning:
        
        earningNormal = new SDbEarning();
        for (SDbEarning earning : moHrsPayroll.maEarnigs) {
            if (SLibUtils.compareKeys(earning.getPrimaryKey(), new int[] { moHrsPayroll.moConfig.getFkEarningEarningId_n() })) {
                earningNormal = earning;
                break;
            }
        }
        
        for (SHrsPayrollReceiptEarning earning : moHrsEmployee.getHrsPayrollReceipt().getHrsReceiptEarnings()) {
            if (SLibUtils.compareKeys(earning.getEarning().getPrimaryKey(), earningNormal.getPrimaryKey())) {
                if (add) {
                    unitAlleged = earning.getReceiptEarning().getUnitsAlleged() - (unitAlleged > earning.getReceiptEarning().getUnitsAlleged() ? earning.getReceiptEarning().getUnitsAlleged() : unitAlleged);
                }
                else {
                    unitAlleged = earning.getReceiptEarning().getUnitsAlleged() + (unitAlleged > workingDaysAvailable ? workingDaysAvailable : unitAlleged);
                }
                unit = SLibUtils.round((!earningNormal.isDaysAdjustment() ?
                        unitAlleged * moHrsEmployee.getEmployeeDays().getFactorCalendar() :
                        (unitAlleged * moHrsEmployee.getEmployeeDays().getFactorCalendar() * moHrsEmployee.getEmployeeDays().getFactorDaysPaid())),
                        SLibUtils.DecimalFormatValue8D.getMaximumFractionDigits()); // unit needs rounding to 8 decimals
                amount = SLibUtils.roundAmount((unit * earning.getReceiptEarning().getAmountUnitary() * earningNormal.getUnitsFactor()));

                earning.getReceiptEarning().setUnitsAlleged(unitAlleged);
                earning.getReceiptEarning().setUnits(unit);
                earning.getReceiptEarning().setAmount_r(amount);
                // XXX (jbarajas, 2016-04-01) remove earning normal with value zero
                if (unit <= 0) {
                    moHrsEmployee.getHrsPayrollReceipt().removeEarning(earning.getPkMoveId());                        
                }
                else {
                    moHrsEmployee.getHrsPayrollReceipt().replaceEarning(earning.getPkMoveId(), earning);                        
                }

                found = true;
                break;
            }
        }

        if (!found) {

            if (earningNormal.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT) {
                amount_unt = unitAlleged;
            }

            // XXX (jbarajas, 2016-04-01) remove earning normal with value zero
            if (unitAlleged > 0) {
                hrsPayrollReceiptEarning = new SHrsPayrollReceiptEarning();
                hrsPayrollReceiptEarning.setEarning(earningNormal);
                
                payrollReceiptEarning = moHrsPayroll.createHrsPayrollReceiptEarning(moHrsEmployee.getHrsPayrollReceipt(), null, earningNormal, unitAlleged, amount_unt, true, 
                        SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, moHrsEmployee.getHrsPayrollReceipt().getHrsReceiptEarnings().size() + 1);

                hrsPayrollReceiptEarning.setReceiptEarning(payrollReceiptEarning);
                hrsPayrollReceiptEarning.setPkMoveId(moHrsEmployee.getHrsPayrollReceipt().getHrsReceiptEarnings().size() + 1);
                hrsPayrollReceiptEarning.setHrsReceipt(moHrsEmployee.getHrsPayrollReceipt());
                
                moHrsEmployee.getHrsPayrollReceipt().addEarning(hrsPayrollReceiptEarning);
            }
        }

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarningRow : moHrsPayroll.getHrsPayrollReceiptEarningAbsence(getAbsenceConsumptions(), moHrsEmployee.getHrsPayrollReceipt())) {
            moHrsEmployee.getHrsPayrollReceipt().addEarning(hrsPayrollReceiptEarningRow);
        }
    }
    
    public void removeHrsPayrollReceiptEarningAbsence(final SHrsPayrollReceiptEarning earning) throws Exception {
        ArrayList<SDbAbsenceConsumption> aAbsenceConsumptionsDelete = null;
        SDbAbsenceType absenceType = null;
        
        for (SDbAbsenceConsumption absenceConsumption : moHrsEmployee.getHrsPayrollReceipt().getAbsenceConsumptions()) {
            if (SLibUtilities.compareKeys(absenceType.getPrimaryKey(), new int[] { absenceConsumption.getAbsence().getFkAbsenceClassId(), absenceConsumption.getAbsence().getFkAbsenceTypeId() })) {
                aAbsenceConsumptionsDelete.add(absenceConsumption);
                break;
            }
        }
        
        for (SDbAbsenceConsumption absenceConsumptionDelete : aAbsenceConsumptionsDelete) {
            for (SDbAbsenceConsumption absenceConsumption : moHrsEmployee.getHrsPayrollReceipt().getAbsenceConsumptions()) {
                if (SLibUtilities.compareKeys(absenceConsumption.getPrimaryKey(), absenceConsumptionDelete.getPrimaryKey())) {
                    moHrsEmployee.getHrsPayrollReceipt().removeAbsenceConsumption(absenceConsumption);
                    break;
                }
            }
        }
        
        moHrsEmployee.getHrsPayrollReceipt().removeEarning(earning.getPkMoveId());
    }

    public void computeDbPayrollReceiptDays() throws Exception {
        // Compute dbPayrollReceipt values referent to days:
        
        moReceipt.setFactorCalendar(moHrsEmployee.getEmployeeDays().getFactorCalendar());
        moReceipt.setFactorDaysPaid(moHrsEmployee.getEmployeeDays().getFactorDaysPaid());
        moReceipt.setReceiptDays(moHrsEmployee.getEmployeeDays().getReceiptDays());
        moReceipt.setWorkingDays(moHrsEmployee.getEmployeeDays().getWorkingDays());
        moReceipt.setDaysWorked(moHrsEmployee.getEmployeeDays().getDaysWorked());
        moReceipt.setDaysHiredPayroll(moHrsEmployee.getEmployeeDays().getDaysHiredPayroll());
        moReceipt.setDaysHiredAnnual(moHrsEmployee.getEmployeeDays().getDaysHiredAnnual());
        moReceipt.setDaysIncapacityNotPaidPayroll(moHrsEmployee.getEmployeeDays().getDaysIncapacityNotPaidPayroll());
        moReceipt.setDaysIncapacityNotPaidAnnual(moHrsEmployee.getEmployeeDays().getDaysIncapacityNotPaidAnnual());
        moReceipt.setDaysNotWorkedPaid(moHrsEmployee.getEmployeeDays().getDaysNotWorkedPaid());
        moReceipt.setDaysNotWorkedNotPaid(moHrsEmployee.getEmployeeDays().getDaysNotWorkedNotPaid());
        moReceipt.setDaysNotWorked_r(moHrsEmployee.getEmployeeDays().getDaysNotWorked_r());
        moReceipt.setPayrollTaxableDays_r(moHrsEmployee.getEmployeeDays().getPayrollTaxableDays_r());
        moReceipt.setAnnualTaxableDays_r(moHrsEmployee.getEmployeeDays().getAnnualTaxableDays_r());
        moReceipt.setDaysToBePaid_r(getDaysToBePaid(moHrsEmployee.getHrsPayrollReceipt().getHrsReceiptEarnings()));
        moReceipt.setDaysPaid(getDaysWorkedPayed(moHrsEmployee.getHrsPayrollReceipt().getHrsReceiptEarnings()));
        
        computeHrsDaysByPeriod();
    }
    
    public void computeHrsDaysByPeriod() throws Exception {
        int difDays = 0;
        int year = 0;
        int periodYear = 0;
        int daysNotWorkedNotPaid = 0;
        int daysIncapacityNotPaid = 0;
        SHrsEmployeeDays employeeDays = moHrsEmployee.getEmployeeDays();
        SHrsDaysByPeriod hrsDaysPrev = null;
        SHrsDaysByPeriod hrsDaysCurr = null;
        SHrsDaysByPeriod hrsDaysNext = null;
        
        // Compute days by period previous:
        
        year = employeeDays.getYear() - (employeeDays.getPeriod() == 1 ? 1 : 0);
        periodYear = (employeeDays.getPeriod() == 1 ? 12 : employeeDays.getPeriod() - 1);
        
        hrsDaysPrev = createHrsDaysByPeriod(year, periodYear);
        
        // Compute days by period current:
        
        year = employeeDays.getYear();
        periodYear = employeeDays.getPeriod();
        
        hrsDaysCurr = createHrsDaysByPeriod(year, periodYear);
        
        // Compute days by period next:
        
        year = employeeDays.getYear() + (employeeDays.getPeriod() == 12 ? 1 : 0);
        periodYear = (employeeDays.getPeriod() == 12 ? 1 : employeeDays.getPeriod() + 1);
        
        hrsDaysNext = createHrsDaysByPeriod(year, periodYear);
        
        for (SDbAbsenceConsumption absenceConsumption : maAbsenceConsumptions) {
            difDays = absenceConsumption.getEffectiveDays();

            for (int i = 0; i < difDays; i++) {
                if (!absenceConsumption.getAbsence().IsAuxAbsencePayable() &&
                    absenceConsumption.getAbsence().getFkAbsenceClassId() != SModSysConsts.HRSU_CL_ABS_VAC) {
                    daysNotWorkedNotPaid++; // sum days not worked and not paid (DNWNP)

                    if (absenceConsumption.getAbsence().getFkAbsenceClassId() == SModSysConsts.HRSU_CL_ABS_DIS) {
                        daysIncapacityNotPaid++; // sum days of incapacity not paid (DINP)
                    }
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
        int daysPeriod = 0;
        int daysPeriodPayroll = 0;
        int difDays = 0;
        Date dateStart = null;
        Date dateEnd = null;
        
        daysPeriod = SLibTimeUtils.digestDate(SLibTimeUtils.getEndOfMonth(SLibTimeUtils.createDate(year, periodYear)))[2];
        for (SDbEmployeeHireLog hireLog : moHrsEmployee.getEmployeeHireLogs()) {
            dateStart = hireLog.getDateHire().compareTo(moHrsEmployee.getPeriodStart()) <= 0 ? moHrsEmployee.getPeriodStart() : hireLog.getDateHire();
            dateEnd = hireLog.getDateDismissed_n() == null ? moHrsEmployee.getPeriodEnd() : hireLog.getDateDismissed_n().compareTo(moHrsEmployee.getPeriodEnd()) >= 0 ? moHrsEmployee.getPeriodEnd() : hireLog.getDateDismissed_n();
            
            difDays = (int) SLibTimeUtils.getDaysDiff(dateEnd, dateStart);
            
            for (int i = 0; i <= difDays; i++) {
                if (SLibTimeUtils.isBelongingToPeriod(SLibTimeUtils.addDate(dateStart, 0, 0, i), year, periodYear)) {
                    daysPeriodPayroll++;
                }
            }
        }
        
        return new SHrsDaysByPeriod(year, periodYear, daysPeriod, daysPeriodPayroll);
    }
    
    public boolean validateAbsenceConsumption(SDbAbsence absence, SDbAbsenceConsumption absenceConsumption) throws Exception {
        int diferencePendingDays = 0;
        int pendigEffectiveDays = 0;
        int diferenceDays = 0;
        int diferenceDaysPeriod = 0;
        double absenceConsumptionPendingDays = 0;
        double workingDaysAvailable = 0;
        double receiptDaysAvailable = 0;
        double receiptDays = 0;
        int businessDays = 0;
        boolean earFound = false;
        SHrsEmployeeDays employeeDays = moHrsEmployee.getEmployeeDays();
        
        receiptDays = employeeDays.getReceiptDays();
        workingDaysAvailable = employeeDays.getBusinessDays() - employeeDays.getDaysNotWorked_r();
        receiptDaysAvailable = employeeDays.getReceiptDays() - employeeDays.getDaysNotWorked_r();
        diferenceDays = (int) SLibTimeUtils.getDaysDiff(absenceConsumption.getDateEnd(), absenceConsumption.getDateStart()) + 1;
        absenceConsumptionPendingDays = absence.getEffectiveDays() - SHrsUtils.getConsumptionPreviousDays(absence, moHrsEmployee);
        businessDays = SHrsUtils.getEmployeeBusinessDays(absenceConsumption.getDateStart(), absenceConsumption.getDateEnd(), moHrsPayroll.getHolidays(), moHrsPayroll.getWorkingDaySettings());
        
        diferencePendingDays = (int) SLibTimeUtils.getDaysDiff(absence.getDateEnd(), absenceConsumption.getDateEnd());
        pendigEffectiveDays = (absence.getEffectiveDays() - SHrsUtils.getConsumptionPreviousDays(absence, moHrsEmployee)) - absenceConsumption.getEffectiveDays();
        
        if (moHrsPayroll.getPayroll().isNormal()) {
            if (absence.getFkAbsenceClassId() == SModSysConsts.HRSU_CL_ABS_DIS) {
                if (receiptDaysAvailable <= 0) {
                    throw new Exception("No existen días naturales que asociar con la incidencia.");
                }
                else if (absenceConsumption.getEffectiveDays() > receiptDaysAvailable) {
                    throw new Exception("Los días efectivos de consumo deben ser menor o igual a " + receiptDaysAvailable + " (días naturales).");
                }
                else if (businessDays > workingDaysAvailable) {
                    throw new Exception("Los días efectivos de consumo deben ser menor o igual a " + workingDaysAvailable + " (días laborables).");
                }
            }
            else if (workingDaysAvailable <= 0) {
                throw new Exception("No existen días laborables que asociar con la incidencia.");
            }
            else if (absenceConsumption.getEffectiveDays() > (absence.getFkAbsenceClassId() == SModSysConsts.HRSU_CL_ABS_DIS ? receiptDays : workingDaysAvailable)) {
                throw new Exception("Los días efectivos de consumo deben ser menor o igual a " + (absence.getFkAbsenceClassId() == SModSysConsts.HRSU_CL_ABS_DIS ? receiptDays : workingDaysAvailable) + ".");
            }
            
            for (SDbAbsenceConsumption absenceConsumptionAux : moHrsEmployee.getHrsPayrollReceipt().getAbsenceConsumptions()) {
                diferenceDaysPeriod += (int) SLibTimeUtils.getDaysDiff(absenceConsumptionAux.getDateEnd(), absenceConsumptionAux.getDateStart()) + 1;
            }
            
            diferenceDaysPeriod += (int) SLibTimeUtils.getDaysDiff(absenceConsumption.getDateEnd(), absenceConsumption.getDateStart()) + 1;
            
            if (diferenceDaysPeriod > receiptDays) {
                throw new Exception("Los días calendario comprendidos en los periodos de consumo son '" + diferenceDaysPeriod + "' y deben ser menor o igual a los días de la nómina del empleado que son '" + receiptDays + "'.");
            }
        }

        if (absenceConsumption.getDateEnd().compareTo(absence.getDateEnd()) > 0) {
            throw new Exception("La fecha final del consumo debe ser anterior o igual a " + "la fecha final de la incidencia '" + SLibUtils.DateFormatDate.format(absence.getDateEnd()) + "'.");
        }

        if (absenceConsumption.getEffectiveDays() > diferenceDays) {
            throw new Exception("Los días efectivos de consumo deben ser menor o igual a " + diferenceDays + ".");
        }

        if (absenceConsumption.getDateEnd().compareTo(absence.getDateEnd()) == 0 && absenceConsumption.getEffectiveDays() != absenceConsumptionPendingDays) {
            throw new Exception("Los días efectivos de consumo deben ser igual a " + absenceConsumptionPendingDays + ".");
        }

        if (absenceConsumption.getEffectiveDays() == absenceConsumptionPendingDays && absenceConsumption.getDateEnd().compareTo(absence.getDateEnd()) != 0) {
            throw new Exception("La fecha final del consumo debe ser igual a " + "la fecha final de la incidencia '" + SLibUtils.DateFormatDate.format(absence.getDateEnd()) + "'.");
        }

        if (pendigEffectiveDays > diferencePendingDays) {
            throw new Exception("Los días efectivos de consumo deben ser mayor o igual a '" + ((pendigEffectiveDays - diferencePendingDays) + absenceConsumption.getEffectiveDays()) + "',\n " +
                    "porque de otra manera no será posible consumir el resto de días efectivos restantes '" + pendigEffectiveDays + "'.");
        }
        
        if (absenceConsumption.getAbsence().IsAuxAbsencePayable()) {
            for (SDbEarning earning : moHrsPayroll.getEarnigs()) {
                if (SLibUtils.compareKeys(new int[] { earning.getFkAbsenceClassId_n(), earning.getFkAbsenceTypeId_n() }, new int[] { absenceConsumption.getAbsence().getFkAbsenceClassId(), absenceConsumption.getAbsence().getFkAbsenceTypeId() })) {
                    earFound = true;
                    break;
                }
            }
            
            if (!earFound) {
                throw new Exception("No existe percepción asociada a la incidencia '" + absenceConsumption.getAbsence().getAuxAbsenceClass() + " - " + absenceConsumption.getAbsence().getAuxAbsenceType() + "' para agregar al recibo.");
            }
        }
        
        return true;
    }
    
    public boolean validateDaysToBePaidWithAbsence() throws Exception {
        double daysPayed = 0;
        double daysAbsencePayed = 0;
        
        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : maHrsReceiptEarnings) {
            if (hrsPayrollReceiptEarning.getEarning().isDaysWorked()) {
                daysPayed += hrsPayrollReceiptEarning.getReceiptEarning().getUnits();
            }
        }
        
        for (SDbAbsenceConsumption absenceConsumption : maAbsenceConsumptions) {
            if (absenceConsumption.getAbsence().IsAuxAbsencePayable()) {
                daysAbsencePayed += absenceConsumption.getEffectiveDays();
            }
        }
        
        if (daysAbsencePayed > daysPayed) {
            throw new Exception("Los días consumidos y pagados, no corresponden con los días pagados como percepciones.");
        }
        
        return true;
    }
    
    public double calculateBenefit(final SDbEarning earning, final double days, final double percentage) {
        double units = days * getHrsEmployee().getEmployeeDays().getFactorCalendar() * (!earning.isDaysAdjustment() ? 1d : getHrsEmployee().getEmployeeDays().getFactorDaysPaid());
        return SLibUtils.roundAmount(units * getReceipt().getPaymentDaily() * (earning.getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_VAC_BON ? percentage : 1d));
    }
    
    public SHrsPayrollReceipt clone() throws CloneNotSupportedException {
       SHrsPayrollReceipt registry = new SHrsPayrollReceipt();

       registry.setReceipt(this.getReceipt().clone());
       registry.setHrsPayroll(this.getHrsPayroll());
       registry.setHrsEmployee(this.getHrsEmployee());

       for (SDbAbsenceConsumption absenceConsumption : this.getAbsenceConsumptions()) {
           registry.getAbsenceConsumptions().add(absenceConsumption.clone());
       }

       for (SHrsPayrollReceiptEarning hrsEarning : this.getHrsReceiptEarnings()) {
           registry.getHrsReceiptEarnings().add(hrsEarning.clone());
       }

       for (SHrsPayrollReceiptDeduction hrsDeduction : this.getHrsReceiptDeductions()) {
           registry.getHrsReceiptDeductions().add(hrsDeduction.clone());
       }

       for (SHrsBenefit hrsBenefit : this.getHrsBenefits()) {
           registry.getHrsBenefits().add(hrsBenefit.clone());
       }

       for (SHrsPayrollReceiptEarning hrsEarning : registry.getHrsReceiptEarnings()) {
           hrsEarning.setHrsReceipt(registry);
       }

       for (SHrsPayrollReceiptDeduction hrsDeduction : registry.getHrsReceiptDeductions()) {
           hrsDeduction.setHrsReceipt(registry);
       }

       registry.getHrsPayroll().replaceReceipt(registry.getHrsEmployee().getEmployee().getPkEmployeeId(), registry, false);
       registry.getHrsEmployee().setHrsPayrollReceipt(registry);

       return registry;
    }
}

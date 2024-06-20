/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.utils.SAnniversary;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SHrsTaxUtils {
    
    /**
     * Compute tax.
     * @param taxTable Table of tax for computation.
     * @param taxableEarnings Taxable earnings.
     * @param tableFactor Adjustment factor to apply to tax table.
     * @return Computed tax.
     * @throws Exception 
     */
    public static double computeTax(final SDbTaxTable taxTable, final double taxableEarnings, final double tableFactor) throws Exception {
        double taxAssessed = 0;
        
        for (int row = 0; row < taxTable.getChildRows().size(); row++) {
            SDbTaxTableRow taxTableRow = taxTable.getChildRows().get(row);
            if (taxableEarnings >= SLibUtils.roundAmount(taxTableRow.getLowerLimit() * tableFactor) &&
                    (row + 1 == taxTable.getChildRows().size() || taxableEarnings < SLibUtils.roundAmount(taxTable.getChildRows().get(row + 1).getLowerLimit() * tableFactor))) {
                taxAssessed = SLibUtils.roundAmount((taxableEarnings - SLibUtils.roundAmount(taxTableRow.getLowerLimit() * tableFactor)) * taxTableRow.getTaxRate() + taxTableRow.getFixedFee() * tableFactor);
                break;
            }
        }
        
        return taxAssessed;
    }
    
    /**
     * Compute tax subsidy.
     * @param taxSubsidyTable Table of tax subsidy for computation.
     * @param taxableEarnings Taxable earnings.
     * @param tableFactor Adjustment factor to apply to tax subsidy table.
     * @return Computed tax subsidy.
     * @throws Exception 
     */
    public static double computeTaxSubsidy(final SDbTaxSubsidyTable taxSubsidyTable, final double taxableEarnings, final double tableFactor) throws Exception {
        double taxSubsidyAssessed = 0;
        
        for (int row = 0; row < taxSubsidyTable.getChildRows().size(); row++) {
            SDbTaxSubsidyTableRow taxSubsidyTableRow = taxSubsidyTable.getChildRows().get(row);
            if (taxableEarnings >= taxSubsidyTableRow.getLowerLimit() * tableFactor &&
                    (row + 1 == taxSubsidyTable.getChildRows().size() || taxableEarnings < taxSubsidyTable.getChildRows().get(row + 1).getLowerLimit() * tableFactor)) {
                taxSubsidyAssessed = SLibUtils.roundAmount(taxSubsidyTableRow.getTaxSubsidy() * tableFactor);
                break;
            }
        }
        
        return taxSubsidyAssessed;
    }
    
    /**
     * Compute employment subsidy.
     * @param employmentSubsidy Employment subsidy configuration.
     * @param taxableEarnings Taxable earnings.
     * @param tableFactor Adjustment factor to apply to tax subsidy table.
     * @return Computed tax subsidy.
     * @throws Exception 
     */
    public static double computeEmploymentSubsidy(final SDbEmploymentSubsidy employmentSubsidy, final double taxableEarnings, final double tableFactor) throws Exception {
        double employmentSubsidyAssessed = 0;
        
        if (taxableEarnings <= employmentSubsidy.getIncomeMonthlyCap() * tableFactor) {
            employmentSubsidyAssessed = SLibUtils.roundAmount(employmentSubsidy.getSubsidyMonthlyCap() * tableFactor);
        }
        
        return employmentSubsidyAssessed;
    }
    
    /**
     * Compute tax based in Articule 174 RLISR.
     * @param taxTable Table of tax for computation.
     * @param taxableEarnings Taxable earnings.
     * @param monthlyIncome Ordinary monthly income.
     * @param tableFactor Adjustment factor to apply to tax table.
     * @return Computed tax.
     * @throws Exception 
     */
    public static double computeTaxAlt(final SDbTaxTable taxTable, final double taxableEarnings, final double monthlyIncome, final double tableFactor) throws Exception {
        double amountFractionI = taxableEarnings / SHrsConsts.YEAR_DAYS * SHrsConsts.MONTH_DAYS_FIXED;
        
        double amountFractionII = computeTax(taxTable, (monthlyIncome + amountFractionI), tableFactor);
        
        double amountFractionIIIAux = computeTax(taxTable, monthlyIncome, tableFactor);
        double amountFractionIII = amountFractionIIIAux > 0 ? (amountFractionII - amountFractionIIIAux) : 0;
        
        double amountFractionV = amountFractionI == 0 ? 0 : (amountFractionIII / amountFractionI);
        
        double amountFractionIV = amountFractionIIIAux > 0 ? (taxableEarnings * amountFractionV) : 0;
        
        return amountFractionIV;
    }
    
    /**
     * Compute tax subsidy based in Articule 174 RLISR.
     * @param taxSubsidyTable Table of tax subsidy for computation.
     * @param taxableEarnings Taxable earnings.
     * @param monthlyIncome Ordinary monthly income.
     * @param tableFactor Adjustment factor to apply to tax subsidy table.
     * @return Computed tax subsidy.
     * @throws Exception 
     */
    public static double computeTaxSubsidyAlt(final SDbTaxSubsidyTable taxSubsidyTable, final double taxableEarnings, final double monthlyIncome, final double tableFactor) throws Exception {
        double amountFractionI = taxableEarnings / SHrsConsts.YEAR_DAYS * SHrsConsts.MONTH_DAYS_FIXED;
        
        double amountFractionII = computeTaxSubsidy(taxSubsidyTable, (monthlyIncome + amountFractionI), tableFactor);
        
        double amountFractionIIIAux = computeTaxSubsidy(taxSubsidyTable, monthlyIncome, tableFactor);
        double amountFractionIII = amountFractionIIIAux > 0 ? (amountFractionII - amountFractionIIIAux) : 0;
        
        double amountFractionV = amountFractionI == 0 ? 0 : (amountFractionIII / amountFractionI);
        
        double amountFractionIV = amountFractionIIIAux > 0 ? (taxableEarnings * amountFractionV) : 0;
        
        return amountFractionIV;
    }
    
    /**
     * Compute employment subsidy based in Articule 174 RLISR.
     * @param employmentSubsidy Table of tax subsidy for computation.
     * @param taxableEarnings Taxable earnings.
     * @param monthlyIncome Ordinary monthly income.
     * @param tableFactor Adjustment factor to apply to tax subsidy table.
     * @return Computed tax subsidy.
     * @throws Exception 
     */
    public static double computeEmploymentSubsidyAlt(final SDbEmploymentSubsidy employmentSubsidy, final double taxableEarnings, final double monthlyIncome, final double tableFactor) throws Exception {
        double amountFractionI = taxableEarnings / SHrsConsts.YEAR_DAYS * SHrsConsts.MONTH_DAYS_FIXED;
        
        double amountFractionII = computeEmploymentSubsidy(employmentSubsidy, (monthlyIncome + amountFractionI), tableFactor);
        
        double amountFractionIIIAux = computeEmploymentSubsidy(employmentSubsidy, monthlyIncome, tableFactor);
        double amountFractionIII = amountFractionIIIAux > 0 ? (amountFractionII - amountFractionIIIAux) : 0;
        
        double amountFractionV = amountFractionI == 0 ? 0 : (amountFractionIII / amountFractionI);
        
        double amountFractionIV = amountFractionIIIAux > 0 ? (taxableEarnings * amountFractionV) : 0;
        
        return amountFractionIV;
    }
    
    /**
     * Compute Social Security Contribution.
     * @param sscTable Table of Social Security Contribution for computation.
     * @param salarySsc Employee's base salary for Social Security Contribution.
     * @param mwzReferenceWage Minimum wage of reference zone.
     * @param hrsDaysPrev Number of employee's days hired in previous period the payroll.
     * @param hrsDaysCurr Number of employee's days hired in current period the payroll.
     * @param hrsDaysNext Number of employee's days hired in next period the payroll.
     * @return double amount calculated of security social contribution.
     * @throws Exception 
     */
    public static double computeSsContribution(final SDbSsContributionTable sscTable, final double salarySsc, final double mwzReferenceWage, 
            final SHrsDaysByPeriod hrsDaysPrev, final SHrsDaysByPeriod hrsDaysCurr, final SHrsDaysByPeriod hrsDaysNext) throws Exception {
        double sscAssessed = 0;
        
        for (int row = 0; row < sscTable.getChildRows().size(); row++) {
            SDbSsContributionTableRow sscTableRow = sscTable.getChildRows().get(row);
            double sscEarning = 0;
            
            switch(sscTableRow.getPkRowId()) {
                case SHrsConsts.SS_INC_MON:
                case SHrsConsts.SS_INC_PEN:
                    sscEarning = SLibUtils.roundAmount((hrsDaysPrev.getPeriodPayrollDays() + hrsDaysCurr.getPeriodPayrollDays() + hrsDaysNext.getPeriodPayrollDays() - hrsDaysPrev.getDaysIncapacityNotPaid() - hrsDaysCurr.getDaysIncapacityNotPaid() - hrsDaysNext.getDaysIncapacityNotPaid()) * salarySsc);
                    break;
                    
                case SHrsConsts.SS_INC_KND_SSC_LET:
                    sscEarning = SLibUtils.roundAmount((hrsDaysPrev.getPeriodPayrollDays() + hrsDaysCurr.getPeriodPayrollDays() + hrsDaysNext.getPeriodPayrollDays() - hrsDaysPrev.getDaysIncapacityNotPaid() - hrsDaysCurr.getDaysIncapacityNotPaid() - hrsDaysNext.getDaysIncapacityNotPaid()) * mwzReferenceWage);
                    break;
                    
                case SHrsConsts.SS_INC_KND_SSC_GT:
                    sscEarning = SLibUtils.roundAmount(salarySsc <= (sscTableRow.getLowerLimitMwzReference() * mwzReferenceWage) ? 0 :
                           ((hrsDaysPrev.getPeriodPayrollDays() + hrsDaysCurr.getPeriodPayrollDays() + hrsDaysNext.getPeriodPayrollDays() - hrsDaysPrev.getDaysIncapacityNotPaid() - hrsDaysCurr.getDaysIncapacityNotPaid() - hrsDaysNext.getDaysIncapacityNotPaid()) * (salarySsc - (sscTableRow.getLowerLimitMwzReference() * mwzReferenceWage))));
                    break;
                    
                case SHrsConsts.SS_DIS_LIF:
                case SHrsConsts.SS_CRE:
                case SHrsConsts.SS_RSK:
                case SHrsConsts.SS_RET:
                case SHrsConsts.SS_SEV:
                case SHrsConsts.SS_HOM:
                    sscEarning = SLibUtils.roundAmount((hrsDaysPrev.getPeriodPayrollDays() + hrsDaysCurr.getPeriodPayrollDays() + hrsDaysNext.getPeriodPayrollDays() - hrsDaysPrev.getDaysNotWorkedNotPaid() - hrsDaysCurr.getDaysNotWorkedNotPaid() - hrsDaysNext.getDaysNotWorkedNotPaid()) * salarySsc);
                    break;
                    
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
            
            sscAssessed += SLibUtils.roundAmount(sscEarning * sscTableRow.getWorkerPercentage());
        }
        
        return sscAssessed;
    }
    
    private static int getRecentBenefitTable(final SGuiSession session, final int benefitType, final int paymentType, final Date dateCutoff) throws Exception {
        int tableId = 0;

        String sql = "SELECT id_ben "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " "
            + "WHERE NOT b_del AND fk_tp_ben = " + benefitType + " AND dt_sta <= '" + SLibUtils.DbmsDateFormatDate.format(dateCutoff) + "' "
            + (paymentType == 0 ? "AND fk_tp_pay_n IS NULL" : "AND (fk_tp_pay_n IS NULL OR fk_tp_pay_n = " + paymentType + ")") + " "
            + "ORDER BY dt_sta DESC, id_ben "
            + "LIMIT 1;";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                tableId = resultSet.getInt(1);
            }
        }

        return tableId;
    }
    
    /**
     * Create anniversary entries from 1 to 99 for each given table of benefits.
     * @param benefitTables
     * @return Array of anniversaries (from 1 to 100) for each given table of benefits.
     */
    private static ArrayList<SHrsBenefitTableAnniversary> createBenefitTablesAnniversaries(ArrayList<SDbBenefitTable> benefitTables) {
        ArrayList<SHrsBenefitTableAnniversary> benefitTableAnniversarys = new ArrayList<>();
        
        benefitTables.stream().filter((table) -> (!table.getChildRows().isEmpty())).forEach((table) -> {
            int currIndex = -1; // current index of row for current benefit table
            double benefit = 0; // current benefit, expressed as days or bonus percentage
            SDbBenefitTableRow tableRow = null; // current benefit-table row
            
            for (int year = 1; year <= 100; year++) {
                if (tableRow == null || (year * SLibTimeConsts.MONTHS) > tableRow.getMonths()) {
                    if (currIndex + 1 < table.getChildRows().size()) {
                        ++currIndex;
                        tableRow = table.getChildRows().get(currIndex);
                        benefit = table.getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_VAC_BON ? tableRow.getBenefitBonusPercentage() : tableRow.getBenefitDays();
                    }
                }
                
                benefitTableAnniversarys.add(new SHrsBenefitTableAnniversary(table.getPkBenefitId(), year, benefit));
            }
        });

        return benefitTableAnniversarys;
    }
    
    // xxx123 refactorizar
    @Deprecated
    private static double getSbcIntegrationFactor(final SGuiSession session, final Date dateBenefits, final Date dateCutoff) throws Exception {
        int seniority = 0;
        int daysTableAnnualBonus = 0;
        int daysTableVacation = 0;
        double percentageTableVacationBonus = 0;
        double salaryUnit = 1;
        double integrationFactorSbc = 0;
        SHrsBenefitTableAnniversary benefitTableAnniversary = null;
        ArrayList<SDbBenefitTable> benefitTableAnnualBonus = new ArrayList<>();
        ArrayList<SDbBenefitTable> benefitTableVacation = new ArrayList<>();
        ArrayList<SDbBenefitTable> benefitTableVacationBonus = new ArrayList<>();
        
        benefitTableAnnualBonus.add((SDbBenefitTable) session.readRegistry(SModConsts.HRS_BEN, new int[] { getRecentBenefitTable(session, SModSysConsts.HRSS_TP_BEN_ANN_BON, 0, dateCutoff) }));
        benefitTableVacation.add((SDbBenefitTable) session.readRegistry(SModConsts.HRS_BEN, new int[] { getRecentBenefitTable(session, SModSysConsts.HRSS_TP_BEN_VAC, 0, dateCutoff) }));
        benefitTableVacationBonus.add((SDbBenefitTable) session.readRegistry(SModConsts.HRS_BEN, new int[] { getRecentBenefitTable(session, SModSysConsts.HRSS_TP_BEN_VAC_BON, 0, dateCutoff) }));
        
        if (dateBenefits != null) {
            SAnniversary anniversary = new SAnniversary(dateBenefits, dateCutoff);
            seniority = anniversary.getElapsedYears();
        }
        else {
            seniority = 1;
        }
        
        ArrayList<SHrsBenefitTableAnniversary> benefitTableAnnualBonusAnniversaries = createBenefitTablesAnniversaries(benefitTableAnnualBonus);
        ArrayList<SHrsBenefitTableAnniversary> benefitTableVacationAnniversaries = createBenefitTablesAnniversaries(benefitTableVacation);
        ArrayList<SHrsBenefitTableAnniversary> benefitTableVacationBonusAnniversaries = createBenefitTablesAnniversaries(benefitTableVacationBonus);
        
        for (SHrsBenefitTableAnniversary anniversary : benefitTableAnnualBonusAnniversaries) {
            if (anniversary.getAnniversary() <= seniority) {
                benefitTableAnniversary = anniversary;
            }
        }
        daysTableAnnualBonus = benefitTableAnniversary == null ? 0 : (int) benefitTableAnniversary.getValue();
        
        for (SHrsBenefitTableAnniversary anniversary : benefitTableVacationAnniversaries) {
            if (anniversary.getAnniversary() <= seniority) {
                benefitTableAnniversary = anniversary;
            }
        }
        daysTableVacation = benefitTableAnniversary == null ? 0 : (int) benefitTableAnniversary.getValue();
        
        for (SHrsBenefitTableAnniversary anniversary : benefitTableVacationBonusAnniversaries) {
            if (anniversary.getAnniversary() <= seniority) {
                benefitTableAnniversary = anniversary;
            }
        }
        percentageTableVacationBonus = benefitTableAnniversary == null ? 0 : (double) benefitTableAnniversary.getValue();
        
        integrationFactorSbc = salaryUnit + ((double) daysTableAnnualBonus / SHrsConsts.YEAR_DAYS) + (double) (daysTableVacation * percentageTableVacationBonus / SHrsConsts.YEAR_DAYS);
        
        return integrationFactorSbc;
    }
    
    /**
     * Estimates net monthly payment from a suggested gross payment.
     * @param session GUI session.
     * @param grossAmount Suggested gross monthly payment.
     * @param dateCutoff Cutoff date.
     * @param dateBenefits Benefits date.
     * @return Estimation.
     * @throws Exception 
     */
    public static SHrsCalculatedNetGrossAmount estimateMonthlyPaymentNet(final SGuiSession session, final double grossAmount, final Date dateCutoff, final Date dateBenefits) throws Exception {
        SHrsCalculatedNetGrossAmount hrsCalculatedNetGrossAmount = null;
        SDbTaxTable taxTable = null;
        SDbTaxSubsidyTable taxSubsidyTable = null;
        SDbSsContributionTable sscTable = null;
        SDbConfig config = null;
        double salaryDaily = 0;
        double salarySsc = 0;
        double mwzReferenceWage = 0;
        double netAmount = 0;
        double taxAmount = 0;
        double taxSubsidyAmount = 0;
        double sscAmount = 0;
        double tableFactor = 0;
        int year = SLibTimeUtils.digestYear(dateCutoff)[0];
        int days = SLibTimeUtils.getMaxDayOfMonth(dateCutoff);
        SHrsDaysByPeriod hrsDaysPrev = new SHrsDaysByPeriod(0, 0, 0, 0);
        SHrsDaysByPeriod hrsDaysCurr = new SHrsDaysByPeriod(year, 0, days, days);
        SHrsDaysByPeriod hrsDaysNext = new SHrsDaysByPeriod(0, 0, 0, 0);
        
        salaryDaily = grossAmount * SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS;
        salarySsc = salaryDaily * getSbcIntegrationFactor(session, dateBenefits, dateCutoff);
        
        config = (SDbConfig) session.readRegistry(SModConsts.HRS_CFG, new int[] { SUtilConsts.BPR_CO_ID });
        taxTable = (SDbTaxTable) session.readRegistry(SModConsts.HRS_TAX, new int[] { SHrsUtils.getRecentTaxTable(session, dateCutoff) });
        taxSubsidyTable = (SDbTaxSubsidyTable) session.readRegistry(SModConsts.HRS_TAX_SUB, new int[] { SHrsUtils.getRecentTaxSubsidyTable(session, dateCutoff) });
        sscTable = (SDbSsContributionTable) session.readRegistry(SModConsts.HRS_SSC, new int[] { SHrsUtils.getRecentSsContributionTable(session, dateCutoff) });
        mwzReferenceWage = SHrsUtils.getRecentMinimumWage(session, config.getFkMwzReferenceTypeId(), dateCutoff);
        
        tableFactor = ((double) SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS) * days;
        
        taxAmount = computeTax(taxTable, grossAmount, tableFactor);
        taxSubsidyAmount = computeTaxSubsidy(taxSubsidyTable, grossAmount, tableFactor);
        sscAmount = computeSsContribution(sscTable, salarySsc, mwzReferenceWage, hrsDaysPrev, hrsDaysCurr, hrsDaysNext);
        
        netAmount = grossAmount - taxAmount - sscAmount;
        
        hrsCalculatedNetGrossAmount = new SHrsCalculatedNetGrossAmount(netAmount, grossAmount, taxAmount, taxSubsidyAmount, sscAmount);
        hrsCalculatedNetGrossAmount.setCalculatedAmountType(SHrsConsts.CAL_NET_AMT_TYPE);
        hrsCalculatedNetGrossAmount.setSalary(salaryDaily);
        hrsCalculatedNetGrossAmount.setSalarySs(salarySsc);
        
        return hrsCalculatedNetGrossAmount;
    }
    
    /**
     * Estimates gross monthly payment from a suggested net payment.
     * @param session GUI session.
     * @param netAmount Suggested net monthly payment.
     * @param dateCutoff Cutoff date.
     * @param dateBenefits Benefits date.
     * @param tolerance Estimation tolerance as an amount of money.
     * @return
     * @throws Exception 
     */
    public static SHrsCalculatedNetGrossAmount estimateMonthlyPaymentGross(final SGuiSession session, final double netAmount, final Date dateCutoff, final Date dateBenefits, final double tolerance) throws Exception {
        SHrsCalculatedNetGrossAmount hrsCalculatedNetGrossAmount = null;
        SDbTaxTable taxTable = null;
        SDbTaxTableRow taxTableRow = null;
        int days = SLibTimeUtils.getMaxDayOfMonth(dateCutoff);
        double salaryDaily = 0;
        double salarySsc = 0;
        double tableFactor = 0;
        double average = 0;
        double grossAmount = 0;
        double limitInf = 0;
        double limitSup = 0;
        double toleranceAux = 0;
        boolean calculate = true;
        
        taxTable = (SDbTaxTable) session.readRegistry(SModConsts.HRS_TAX, new int[] { SHrsUtils.getRecentTaxTable(session, dateCutoff) });
        
        tableFactor = ((double) SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS) * days;
        
        if (taxTable != null) {
            for (int i = 0; i < taxTable.getChildRows().size(); i++) {
                taxTableRow = taxTable.getChildRows().get(i);
                if (i == 0) {
                    limitInf = SLibUtils.roundAmount(taxTableRow.getLowerLimit() * tableFactor);
                }
                if (netAmount <= SLibUtils.roundAmount(taxTableRow.getLowerLimit() * tableFactor)) {
                    limitSup = SLibUtils.roundAmount(taxTableRow.getLowerLimit() * tableFactor);
                }
                
                average = (limitInf + limitSup) / 2;
                
                salaryDaily = limitSup * SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS;
                salarySsc = salaryDaily * getSbcIntegrationFactor(session, dateBenefits, dateCutoff);

                hrsCalculatedNetGrossAmount = estimateMonthlyPaymentNet(session, average, dateCutoff, dateBenefits);
                
                if (hrsCalculatedNetGrossAmount.getNetAmount() > netAmount) {
                    break;
                }
            }
        }
        
        average = 0;

        while (calculate) {
            average = (limitInf + limitSup) / 2;
            
            salaryDaily = limitSup * SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS;
            salarySsc = salaryDaily * getSbcIntegrationFactor(session, dateBenefits, dateCutoff);

            hrsCalculatedNetGrossAmount = estimateMonthlyPaymentNet(session, average, dateCutoff, dateBenefits);

            if (hrsCalculatedNetGrossAmount.getNetAmount() > netAmount) {
                limitSup = average;
            }
            else {
                limitInf = average;
            }
            toleranceAux = SLibUtils.roundAmount(netAmount - hrsCalculatedNetGrossAmount.getNetAmount());
            
            calculate = SLibUtils.roundAmount(limitInf) != SLibUtils.roundAmount(limitSup) && Math.abs(toleranceAux) > tolerance;
        }
        grossAmount = average;
        
        hrsCalculatedNetGrossAmount.setSalary(salaryDaily);
        hrsCalculatedNetGrossAmount.setSalarySs(salarySsc);
        hrsCalculatedNetGrossAmount.setGrossAmount(grossAmount);
        hrsCalculatedNetGrossAmount.setCalculatedAmountType(SHrsConsts.CAL_GROSS_AMT_TYPE);
        
        return hrsCalculatedNetGrossAmount;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.lib.SLibUtilities;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SHrsPayrollReceipt {

    protected SDbPayrollReceipt moReceipt;
    protected SHrsPayroll moHrsPayroll;
    protected SHrsEmployee moHrsEmployee;
    protected ArrayList<SDbAbsenceConsumption> maAbsenceConsumptionDays;
    protected ArrayList<SHrsPayrollReceiptEarning> maHrsEarnings;
    protected ArrayList<SHrsPayrollReceiptDeduction> maHrsDeductions;
    protected ArrayList<SHrsBenefit> maHrsBenefits;

    public SHrsPayrollReceipt() {
        moReceipt = null;
        moHrsPayroll = null;
        moHrsEmployee = null;
        maAbsenceConsumptionDays = new ArrayList<SDbAbsenceConsumption>();
        maHrsEarnings = new ArrayList<SHrsPayrollReceiptEarning>();
        maHrsDeductions = new ArrayList<SHrsPayrollReceiptDeduction>();
        maHrsBenefits = new ArrayList<SHrsBenefit>();
    }

    /*
     * Private methods
     */

    private double getTaxableEarnings() {
        double taxableEarnings = 0;

        for (SHrsPayrollReceiptEarning receiptEarning : maHrsEarnings) {
            taxableEarnings += receiptEarning.getReceiptEarning().getAmountTaxable();
        }

        return taxableEarnings;
    }
    
    private void computeEarnings() {
        for (SHrsPayrollReceiptEarning receiptEarning : maHrsEarnings) {
            // receiptEarning.computeEarning(); XXX jbarajas 15/04/2015
        }
    }

    private void computeEarningsExempt() throws Exception {
        SHrsPayrollReceiptAccumulatedEarnings hrsPayrollReceiptAccumulatedEarnings = null;
        HashMap<Integer, SHrsPayrollReceiptAccumulatedEarnings> earningsMap = new HashMap<Integer, SHrsPayrollReceiptAccumulatedEarnings>();

        // Group earnings:

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : maHrsEarnings) {

            hrsPayrollReceiptAccumulatedEarnings = earningsMap.get(hrsPayrollReceiptEarning.getEarning().getPkEarningId());
            if (hrsPayrollReceiptAccumulatedEarnings == null) {

                hrsPayrollReceiptAccumulatedEarnings = new SHrsPayrollReceiptAccumulatedEarnings();
            }

            hrsPayrollReceiptAccumulatedEarnings.getHrsPayrollReceiptEarnings().add(hrsPayrollReceiptEarning);
            earningsMap.put(hrsPayrollReceiptEarning.getEarning().getPkEarningId(), hrsPayrollReceiptAccumulatedEarnings);
        }

        // Calculate part exempt:

        for (SHrsPayrollReceiptAccumulatedEarnings payrollReceiptAccumulatedEarnings : earningsMap.values()) {

            computeEarningExempt(payrollReceiptAccumulatedEarnings);
        }
    }

    private void computeEarningExempt(SHrsPayrollReceiptAccumulatedEarnings payrollReceiptAccumulatedEarnings) throws Exception {
        double amountExemp = 0;
        double amountAccumulateExemp = 0;
        double minimumExemptionWage = 0;
        double limitExemption = 0;
        double salaryEmployee = 0;
        int seniority = 0;
        int seniorityDays = 0;
        double minimumExemptionWageSeniority = 0;

        SDbEarning earning = null;
        SDbPayrollReceiptEarning payrollReceiptEarning = null;

        SHrsAccumulatedEarning hrsAccumulatedEarning = null;
        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : payrollReceiptAccumulatedEarnings.getHrsPayrollReceiptEarnings()) {

            // Earning configuration:

            earning = hrsPayrollReceiptEarning.getEarning();
            payrollReceiptEarning = hrsPayrollReceiptEarning.getReceiptEarning();
            //if (earning.getFkEarningTypeId() != SModSysConsts.HRSS_TP_EAR_TAX_SUB) { XXX jbarajas 2016-01-16 calculate exemption for all earnings

                // Verify type of exemption:

                amountExemp = 0;
                switch (earning.getFkEarningExemptionTypeId()) {
                    case SModSysConsts.HRSS_TP_EAR_EXEM_NON:
                        break;
                    case SModSysConsts.HRSS_TP_EAR_EXEM_PER:

                        // Compute employe salary if is necessary:

                        salaryEmployee = SLibUtils.round(moHrsEmployee.getEmployee().getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_WEE ?
                            moHrsEmployee.getEmployee().getSalary() : ((moHrsEmployee.getEmployee().getWage() * SHrsConsts.YEAR_MONTHS) / SHrsConsts.YEAR_DAYS), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());

                        // Compute exempt:

                        if (salaryEmployee == moHrsPayroll.getPayroll().getMwzWage()) {

                            // Limit exempt:

                            limitExemption = SLibUtils.round(earning.getExemptionSalaryEqualsMwzLimit() * moHrsPayroll.getPayroll().getMwzWage(), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
                            amountExemp = SLibUtils.round(earning.getExemptionSalaryEqualsMwzPercentage() * hrsPayrollReceiptEarning.getReceiptEarning().getAmount_r(), SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits());
                        }
                        else if (salaryEmployee > moHrsPayroll.getPayroll().getMwzWage()) {

                            // Limit exempt:

                            limitExemption = SLibUtils.round(earning.getExemptionSalaryGreaterMwzLimit() * moHrsPayroll.getPayroll().getMwzWage(), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
                            amountExemp = SLibUtils.round(earning.getExemptionSalaryGreaterMwzPercentage() * hrsPayrollReceiptEarning.getReceiptEarning().getAmount_r(), SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits());
                        }

                        if (limitExemption != 0 && amountExemp > limitExemption) {
                            amountExemp = limitExemption;
                        }

                        break;

                    case SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_GBL:
                    case SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_EVT:

                        // Obtain accumulated if is necessary:

                        if (amountAccumulateExemp == 0 &&
                                earning.getFkEarningExemptionTypeId() == SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_GBL) {

                            hrsAccumulatedEarning = moHrsEmployee.getAccumulatedEarning(earning.getPkEarningId());
                            amountAccumulateExemp = hrsAccumulatedEarning != null ? hrsAccumulatedEarning.getExemption() : 0;
                        }

                        // Compute exempt:

                        if (earning.getExemptionMwz() > 0) {

                            minimumExemptionWage = SLibUtils.round(earning.getExemptionMwz() * moHrsPayroll.getPayroll().getMwzWage(), SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits());
                            if ((minimumExemptionWage - amountAccumulateExemp) < 0) {
                                amountExemp = 0;
                            }
                            else if ((minimumExemptionWage - amountAccumulateExemp) <= hrsPayrollReceiptEarning.getReceiptEarning().getAmount_r()) {
                                amountExemp = SLibUtils.round(minimumExemptionWage - amountAccumulateExemp, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits());
                            }
                            else {
                                amountExemp = hrsPayrollReceiptEarning.getReceiptEarning().getAmount_r();
                            }
                        }

                        // Accumulate exempt if there is another earning:

                        amountAccumulateExemp += amountExemp;

                        break;
                    case SModSysConsts.HRSS_TP_EAR_EXEM_MWZ_SEN:
                        // Compute exempt:
                        seniority = moHrsEmployee.getSeniority();
                                
                        seniorityDays = ((int) SLibTimeUtils.getDaysDiff(moHrsPayroll.getPayroll().getDateEnd(), SLibTimeUtils.addDate(moHrsEmployee.getEmployee().getDateBenefits(), seniority, 0, 0)));
                                //((double) seniority + ((double) seniorityDays / SHrsConsts.YEAR_DAYS)));
                        
                        minimumExemptionWageSeniority = ((double) seniority + ((double) seniorityDays / SHrsConsts.YEAR_DAYS)) * earning.getExemptionMwz();

                        if (earning.getExemptionMwz() > 0) {

                            minimumExemptionWage = SLibUtils.round(minimumExemptionWageSeniority * moHrsPayroll.getPayroll().getMwzWage(), SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits());
                            if (minimumExemptionWage <= hrsPayrollReceiptEarning.getReceiptEarning().getAmount_r()) {
                                amountExemp = SLibUtils.round(minimumExemptionWage, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits());
                            }
                            else {
                                amountExemp = hrsPayrollReceiptEarning.getReceiptEarning().getAmount_r();
                            }
                        }
                        break;

                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración percepción)");
                }

                // Validate that amountExemp doesn´t exceed the amount_r:

                if (amountExemp > hrsPayrollReceiptEarning.getReceiptEarning().getAmount_r()) {
                    amountExemp = hrsPayrollReceiptEarning.getReceiptEarning().getAmount_r();
                }

                payrollReceiptEarning.setAmountExempt(amountExemp);
                payrollReceiptEarning.setAmountTaxable(SLibUtils.round(hrsPayrollReceiptEarning.getReceiptEarning().getAmount_r() - amountExemp, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));
                hrsPayrollReceiptEarning.setReceiptEarning(payrollReceiptEarning);
            //} XXX jbarajas 2016-01-16 calculate exemption for all earnings
        }
    }

    private void computeDeductions() {
        for (SHrsPayrollReceiptDeduction receiptDeduction : maHrsDeductions) {
            receiptDeduction.computeDeduction();
        }
    }

    private void computeReceiptTax() throws Exception {
        int i = 0;
        double fTableFactor = 0;
        double dTableFactorPayroll = 0;
        double dTableFactorAnnull = 0;
        double dTaxSetByUser = 0;
        double dTaxComputed = 0;
        double dTaxAccumulated = 0;
        double dSubsidySetByUser = 0;
        double dSubsidyComputed = 0;
        double dSubsidyAccumulated = 0;
        double dTaxableEarnings = 0;
        boolean bComputeTax = false;
        boolean foundTax = false;
        boolean foundSubsidy = false;
        SHrsEmployeeDays oEmployeeDays = null;
        SDbTaxTable dbTaxTable = null;
        SDbTaxTableRow dbTaxTableRow = null;
        SDbTaxSubsidyTable dbSubsidyTable = null;
        SDbTaxSubsidyTableRow dbSubsidyTableRow = null;
        SDbDeduction dbDeductionTax = null;
        SDbEarning dbEarningSubsidy = null;
        SHrsAccumulatedDeduction oAccumulatedTax = null;
        SHrsAccumulatedEarning oAccumulatedSubsidy = null;
        SHrsPayrollReceiptDeduction oReceiptDeductionTax = null;
        SHrsPayrollReceiptEarning oReceiptEarningSubsidy = null;
        SDbPayrollReceiptDeduction dbReceiptDeductionTax = null;
        SDbPayrollReceiptEarning dbReceiptEarningSubsidy = null;
        ArrayList<SHrsPayrollReceiptDeduction> aDeductionsToRemove = null;
        ArrayList<SHrsPayrollReceiptDeduction> aDeductionsByUser = null;
        ArrayList<SHrsPayrollReceiptEarning> aEarningsToRemove = null;
        ArrayList<SHrsPayrollReceiptEarning> aEarningsByUser = null;

        moReceipt.setTaxPayrollTheorical(0);
        moReceipt.setTaxPayrollActual(0);
        moReceipt.setTaxSubsidyPayrollTheorical(0);
        moReceipt.setTaxSubsidyPayrollActual(0);
        moReceipt.setTaxAnnualTheorical(0);
        moReceipt.setTaxAnnualActual(0);
        moReceipt.setTaxSubsidyAnnualTheorical(0);
        moReceipt.setTaxSubsidyAnnualActual(0);

        oEmployeeDays = moHrsEmployee.getEmployeeDays();
        bComputeTax = SLibUtils.belongsTo(moHrsPayroll.getPayroll().getFkTaxComputationTypeId(), new int[] { SModSysConsts.HRSS_TP_TAX_COMP_PAY, SModSysConsts.HRSS_TP_TAX_COMP_ANN });

        // Obtain taxable earnings:

        switch (moHrsPayroll.getPayroll().getFkTaxComputationTypeId()) {
            case SModSysConsts.HRSS_TP_TAX_COMP_WOT:
                dTaxableEarnings = 0;
                break;
            case SModSysConsts.HRSS_TP_TAX_COMP_PAY:
                dTaxableEarnings = getTaxableEarnings();
                break;
            case SModSysConsts.HRSS_TP_TAX_COMP_ANN:
                dTaxableEarnings = getTaxableEarnings() + moHrsEmployee.getAccummulatedTaxableEarnings();
                oAccumulatedTax = moHrsEmployee.getAccumulatedDeductionByType(SModSysConsts.HRSS_TP_DED_TAX);
                oAccumulatedSubsidy = moHrsEmployee.getAccumulatedEarningByType(SModSysConsts.HRSS_TP_EAR_TAX_SUB);
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        /* XXX jbarajas 2015-09-10 update deduction when is user
        // Remove previous tax:

        aDeductionsToRemove = new ArrayList<SHrsPayrollReceiptDeduction>();
        aDeductionsByUser = new ArrayList<SHrsPayrollReceiptDeduction>();

        for (SHrsPayrollReceiptDeduction deduction : maHrsDeductions) {
            if (deduction.getReceiptDeduction().getFkDeductionTypeId() == SModSysConsts.HRSS_TP_DED_TAX) {
                if (deduction.getReceiptDeduction().isUserEdited()) {
                    aDeductionsByUser.add(deduction);    // preserve user input
                }
                else {
                    aDeductionsToRemove.add(deduction);  // delete previous system computations
                }
            }
        }

        for (SHrsPayrollReceiptDeduction deduction : aDeductionsToRemove) {
            maHrsDeductions.remove(deduction);
        }

        aDeductionsToRemove.clear();

        i = 0;
        for (SHrsPayrollReceiptDeduction deduction : maHrsDeductions) {
            deduction.setPkMoveId(++i);
        }
        */

        if (oAccumulatedTax != null) {
            dTaxAccumulated = SLibUtils.round(oAccumulatedTax.getAcummulated(), SUtilConsts.DECS_AMT);
        }

        /* XXX jbarajas 2015-09-10 update deduction when is user
        // Preserve tax set by user:

        for (SHrsPayrollReceiptDeduction deduction : aDeductionsByUser) {
            deduction.getReceiptDeduction().setAmountSystem_r(0);
            dTaxSetByUser += SLibUtils.round(deduction.getReceiptDeduction().getAmount_r(), SUtilConsts.DECS_AMT);
        }
        dTaxSetByUser = SLibUtils.round(dTaxSetByUser, SUtilConsts.DECS_AMT);

        // Remove previous tax subsidy:

        aEarningsToRemove = new ArrayList<SHrsPayrollReceiptEarning>();
        aEarningsByUser = new ArrayList<SHrsPayrollReceiptEarning>();

        for (SHrsPayrollReceiptEarning earning : maHrsEarnings) {
            if (earning.getReceiptEarning().getFkEarningTypeId() == SModSysConsts.HRSS_TP_EAR_TAX_SUB) {
                if (earning.getReceiptEarning().isUserEdited()) {
                    aEarningsByUser.add(earning);    // preserve user input
                }
                else {
                    aEarningsToRemove.add(earning);  // delete previous system computations
                }
            }
        }

        for (SHrsPayrollReceiptEarning earning : aEarningsToRemove) {
            maHrsEarnings.remove(earning);
        }

        aEarningsToRemove.clear();

        i = 0;
        for (SHrsPayrollReceiptEarning earning : maHrsEarnings) {
            earning.setPkMoveId(++i);
        }
        */

        if (oAccumulatedSubsidy != null) {
            dSubsidyAccumulated = SLibUtils.round(oAccumulatedSubsidy.getAcummulated(), SUtilConsts.DECS_AMT);
        }

        /* XXX jbarajas 2015-09-10 update deduction when is user
        // Preserve tax subsidy set by user:

        for (SHrsPayrollReceiptEarning earning : aEarningsByUser) {
            earning.getReceiptEarning().setAmountSystem_r(0);
            dSubsidySetByUser += SLibUtils.round(earning.getReceiptEarning().getAmount_r(), SUtilConsts.DECS_AMT);
        }
        dSubsidySetByUser = SLibUtils.round(dSubsidySetByUser, SUtilConsts.DECS_AMT);
        */

        if (bComputeTax) {
            for (SHrsPayrollReceiptEarning earning : maHrsEarnings) {
                bComputeTax = false;
                if (earning.getReceiptEarning().getAmount_r() != 0) {
                    bComputeTax = true;
                    break;
                }
            }
        }
        
        if (bComputeTax) {
            // Define factor for table adjusting:

            if (moHrsPayroll.getPayroll().getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_FOR && moHrsPayroll.getConfig().isFornightStandard()) {
                dTableFactorPayroll = ((double) SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS_FORNIGHTS_FIXED) * oEmployeeDays.getPayrollTaxableDays_r();
                dTableFactorAnnull = ((double) SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS_FORNIGHTS_FIXED) * oEmployeeDays.getAnnualTaxableDays_r();
            }
            else {
                dTableFactorPayroll = ((double) SHrsConsts.YEAR_MONTHS / (SHrsConsts.YEAR_DAYS + (SLibTimeUtils.isLeapYear(moHrsPayroll.getPayroll().getFiscalYear()) ? 1d : 0d))) * oEmployeeDays.getPayrollTaxableDays_r();
                dTableFactorAnnull = ((double) SHrsConsts.YEAR_MONTHS / (SHrsConsts.YEAR_DAYS + (SLibTimeUtils.isLeapYear(moHrsPayroll.getPayroll().getFiscalYear()) ? 1d : 0d))) * oEmployeeDays.getAnnualTaxableDays_r();
            }
            moReceipt.setPayrollFactorTax(dTableFactorPayroll);
            moReceipt.setAnnualFactorTax(dTableFactorAnnull);
            
            if (moHrsPayroll.getPayroll().getFkTaxComputationTypeId() == SModSysConsts.HRSS_TP_TAX_COMP_ANN) {
                fTableFactor = dTableFactorAnnull;
            }
            else {
                fTableFactor = dTableFactorPayroll;
            }

            // Compute tax:

            if (moHrsPayroll.getConfig().getFkDeductionTaxId_n() == SLibConsts.UNDEFINED) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración impuesto)");
            }

            dbDeductionTax = moHrsPayroll.getDataDeduction(moHrsPayroll.getConfig().getFkDeductionTaxId_n());
            if (dbDeductionTax == null) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Configuración impuesto)");
            }

            if (dbDeductionTax.getFkDeductionTypeId() != SModSysConsts.HRSS_TP_DED_TAX) {
                throw new Exception(SLibConsts.ERR_MSG_WRONG_TYPE + " (Configuración impuesto)");
            }

            if (moHrsPayroll.getPayroll().getFkTaxId() == SLibConsts.UNDEFINED) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración tabla impuesto)");
            }

            dbTaxTable = moHrsPayroll.getTaxTable(moHrsPayroll.getPayroll().getFkTaxId());
            if (dbTaxTable == null)  {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Tabla impuesto)");
            }

            for (i = 0; i < dbTaxTable.getChildRows().size(); i++) {
                dbTaxTableRow = dbTaxTable.getChildRows().get(i);
                if (dTaxableEarnings >= SLibUtils.round(dbTaxTableRow.getLowerLimit() * fTableFactor, SUtilConsts.DECS_AMT) &&
                        (i + 1 == dbTaxTable.getChildRows().size() || dTaxableEarnings < SLibUtils.round(dbTaxTable.getChildRows().get(i + 1).getLowerLimit() * fTableFactor, SUtilConsts.DECS_AMT))) {
                    dTaxComputed = SLibUtils.round((dTaxableEarnings - SLibUtils.round(dbTaxTableRow.getLowerLimit() * fTableFactor, SUtilConsts.DECS_AMT)) * dbTaxTableRow.getTaxRate() + dbTaxTableRow.getFixedFee() * fTableFactor, SUtilConsts.DECS_AMT);
                }
            }

            /* XXX jbarajas 2015-09-10 update deduction when is user
            if (aDeductionsByUser.isEmpty() && dTaxComputed != 0 && dTaxComputed > dTaxAccumulated) {
            */
            if (dTaxComputed != 0 && dTaxComputed > dTaxAccumulated) {
                dbReceiptDeductionTax = new SDbPayrollReceiptDeduction();
                dbReceiptDeductionTax.setPkPayrollId(moReceipt.getPkPayrollId());
                dbReceiptDeductionTax.setPkEmployeeId(moReceipt.getPkEmployeeId());
                dbReceiptDeductionTax.setPkMoveId(maHrsDeductions.size() + 1);
                dbReceiptDeductionTax.setUnitsAlleged(1);
                dbReceiptDeductionTax.setUnits(1);
                dbReceiptDeductionTax.setAmountUnitary(SLibUtils.round(dTaxComputed - dTaxAccumulated, SUtilConsts.DECS_AMT));  // accumulated is equal to 0 when tax computation type is per payroll
                dbReceiptDeductionTax.setAmountSystem_r(dbReceiptDeductionTax.getAmountUnitary());
                dbReceiptDeductionTax.setAmount_r(dbReceiptDeductionTax.getAmountUnitary());
                dbReceiptDeductionTax.setAutomatic(true);
                dbReceiptDeductionTax.setFkDeductionId(dbDeductionTax.getPkDeductionId());
                dbReceiptDeductionTax.setFkDeductionTypeId(dbDeductionTax.getFkDeductionTypeId());
                dbReceiptDeductionTax.setFkBenefitTypeId(dbDeductionTax.getFkBenefitTypeId());

                oReceiptDeductionTax = new SHrsPayrollReceiptDeduction();
                oReceiptDeductionTax.setPkMoveId(dbReceiptDeductionTax.getPkMoveId());
                oReceiptDeductionTax.setHrsReceipt(this);
                oReceiptDeductionTax.setDeduction(dbDeductionTax);
                oReceiptDeductionTax.setReceiptDeduction(dbReceiptDeductionTax);

                //maHrsDeductions.add(oReceiptDeductionTax); XXX jbarajas 2015-09-10 update deduction when is user
            }

            // Compute tax subsidy:

            if (moHrsPayroll.getConfig().getFkEarningTaxSubsidyId_n() == SLibConsts.UNDEFINED) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración subsidio impuesto)");
            }

            dbEarningSubsidy = moHrsPayroll.getDataEarning(moHrsPayroll.getConfig().getFkEarningTaxSubsidyId_n());
            if (dbEarningSubsidy == null) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Configuración subsidio impuesto)");
            }

            if (dbEarningSubsidy.getFkEarningTypeId() != SModSysConsts.HRSS_TP_EAR_TAX_SUB) {
                throw new Exception(SLibConsts.ERR_MSG_WRONG_TYPE + " (Configuración subsidio impuesto)");
            }

            if (moHrsPayroll.getPayroll().getFkTaxSubsidyId() == SLibConsts.UNDEFINED) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración tabla subsidio impuesto)");
            }

            dbSubsidyTable = moHrsPayroll.getTaxSubsidyTable(moHrsPayroll.getPayroll().getFkTaxSubsidyId());
            if (dbSubsidyTable == null)  {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Tabla subsidio impuesto)");
            }

            for (i = 0; i < dbSubsidyTable.getChildRows().size(); i++) {
                dbSubsidyTableRow = dbSubsidyTable.getChildRows().get(i);
                if (dTaxableEarnings >= dbSubsidyTableRow.getLowerLimit() * fTableFactor &&
                        (i + 1 == dbSubsidyTable.getChildRows().size() || dTaxableEarnings < dbSubsidyTable.getChildRows().get(i + 1).getLowerLimit() * fTableFactor)) {
                    dSubsidyComputed = SLibUtils.round(dbSubsidyTableRow.getTaxSubsidy() * fTableFactor, SUtilConsts.DECS_AMT);
                }
            }

            /* XXX jbarajas 2015-09-10 update deduction when is user
            if (aEarningsByUser.isEmpty() && dSubsidyComputed != 0 && dSubsidyComputed > dSubsidyAccumulated) {
            */
            if (dSubsidyComputed != 0 && dSubsidyComputed > dSubsidyAccumulated) {
                dbReceiptEarningSubsidy = new SDbPayrollReceiptEarning();
                dbReceiptEarningSubsidy.setPkPayrollId(moReceipt.getPkPayrollId());
                dbReceiptEarningSubsidy.setPkEmployeeId(moReceipt.getPkEmployeeId());
                dbReceiptEarningSubsidy.setPkMoveId(maHrsEarnings.size() + 1);
                dbReceiptEarningSubsidy.setUnitsAlleged(1);
                dbReceiptEarningSubsidy.setUnits(1);
                dbReceiptEarningSubsidy.setFactorAmount(1);
                dbReceiptEarningSubsidy.setAmountUnitary(SLibUtils.round(dSubsidyComputed - dSubsidyAccumulated, SUtilConsts.DECS_AMT));    // accumulated is equal to 0 when tax computation type is per payroll
                dbReceiptEarningSubsidy.setAmountSystem_r(dbReceiptEarningSubsidy.getAmountUnitary());
                dbReceiptEarningSubsidy.setAmount_r(dbReceiptEarningSubsidy.getAmountUnitary());
                dbReceiptEarningSubsidy.setAmountExempt(dbReceiptEarningSubsidy.getAmountUnitary());
                dbReceiptEarningSubsidy.setAutomatic(true);
                dbReceiptEarningSubsidy.setFkEarningId(dbEarningSubsidy.getPkEarningId());
                dbReceiptEarningSubsidy.setFkEarningTypeId(dbEarningSubsidy.getFkEarningTypeId());
                dbReceiptEarningSubsidy.setFkBenefitTypeId(dbEarningSubsidy.getFkBenefitTypeId());

                oReceiptEarningSubsidy = new SHrsPayrollReceiptEarning();
                oReceiptEarningSubsidy.setPkMoveId(dbReceiptEarningSubsidy.getPkMoveId());
                oReceiptEarningSubsidy.setHrsReceipt(this);
                oReceiptEarningSubsidy.setEarning(dbEarningSubsidy);
                oReceiptEarningSubsidy.setReceiptEarning(dbReceiptEarningSubsidy);
                oReceiptEarningSubsidy.setXtaValueAlleged(dbReceiptEarningSubsidy.getUnitsAlleged());
                oReceiptEarningSubsidy.setXtaValue(dbReceiptEarningSubsidy.getUnits());

                //maHrsEarnings.add(oReceiptEarningSubsidy); XXX jbarajas 2015-09-10 update deduction when is user
            }
        }

         // Remove previous tax:

        aDeductionsToRemove = new ArrayList<SHrsPayrollReceiptDeduction>();

        for (SHrsPayrollReceiptDeduction deduction : maHrsDeductions) {
            if (deduction.getReceiptDeduction().getFkDeductionTypeId() == SModSysConsts.HRSS_TP_DED_TAX) {
                if (deduction.getReceiptDeduction().isUserEdited() || !deduction.getReceiptDeduction().isAutomatic()) {
                    deduction.getReceiptDeduction().setAmountSystem_r(SLibUtils.round(dTaxComputed - dTaxAccumulated, SUtilConsts.DECS_AMT));    // preserve user input
                    dTaxSetByUser += SLibUtils.round(deduction.getReceiptDeduction().getAmount_r(), SUtilConsts.DECS_AMT);
                    foundTax = true;
                }
                else {
                    aDeductionsToRemove.add(deduction);  // delete previous system computations
                }
            }
        }

        if (!aDeductionsToRemove.isEmpty()) {
            if ((dTaxComputed - dTaxAccumulated) == 0) {
                for (SHrsPayrollReceiptDeduction deduction : aDeductionsToRemove) {
                    maHrsDeductions.remove(deduction);
                }

                aDeductionsToRemove.clear();

                renumberDeductions();
            }
            else {
                for (SHrsPayrollReceiptDeduction deductionRemove : aDeductionsToRemove) {
                    for (SHrsPayrollReceiptDeduction deduction : maHrsDeductions) {
                        if (SLibUtils.compareKeys(new int[] { deduction.getPkMoveId() }, new int[] { deductionRemove.getPkMoveId() })) {
                            deduction.getReceiptDeduction().setAmountUnitary(SLibUtils.round(dTaxComputed - dTaxAccumulated, SUtilConsts.DECS_AMT));
                            deduction.getReceiptDeduction().setAmountSystem_r(deduction.getReceiptDeduction().getAmountUnitary());
                            deduction.getReceiptDeduction().setAmount_r(deduction.getReceiptDeduction().getAmountUnitary());
                        }
                    }
                }
            }
        }
        else if (!foundTax && oReceiptDeductionTax != null) {
            maHrsDeductions.add(oReceiptDeductionTax);
        }
        
        // Remove previous tax subsidy:

        aEarningsToRemove = new ArrayList<SHrsPayrollReceiptEarning>();

        for (SHrsPayrollReceiptEarning earning : maHrsEarnings) {
            if (earning.getReceiptEarning().getFkEarningTypeId() == SModSysConsts.HRSS_TP_EAR_TAX_SUB) {
                if (earning.getReceiptEarning().isUserEdited() || !earning.getReceiptEarning().isAutomatic()) {
                    earning.getReceiptEarning().setAmountSystem_r(SLibUtils.round(dSubsidyComputed - dSubsidyAccumulated, SUtilConsts.DECS_AMT));    // preserve user input
                    dSubsidySetByUser += SLibUtils.round( earning.getReceiptEarning().getAmount_r(), SUtilConsts.DECS_AMT);
                    foundSubsidy = true;
                }
                else {
                    aEarningsToRemove.add(earning);  // delete previous system computations
                }
            }
        }

        if (!aEarningsToRemove.isEmpty()) {
            if ((dSubsidyComputed - dSubsidyAccumulated) == 0) {
                for (SHrsPayrollReceiptEarning earning : aEarningsToRemove) {
                    maHrsEarnings.remove(earning);
                }

                aEarningsToRemove.clear();

                renumberEarnings();
            }
            else {
                for (SHrsPayrollReceiptEarning earningRemove : aEarningsToRemove) {
                    for (SHrsPayrollReceiptEarning earning : maHrsEarnings) {
                        if (SLibUtils.compareKeys(new int[] { earning.getPkMoveId() }, new int[] { earningRemove.getPkMoveId() })) {
                            earning.getReceiptEarning().setAmountUnitary(SLibUtils.round(dSubsidyComputed - dSubsidyAccumulated, SUtilConsts.DECS_AMT));
                            earning.getReceiptEarning().setAmountSystem_r(earning.getReceiptEarning().getAmountUnitary());
                            earning.getReceiptEarning().setAmount_r(earning.getReceiptEarning().getAmountUnitary());
                        }
                    }
                }
            }
        }
        else if (!foundSubsidy && oReceiptEarningSubsidy != null) {
            maHrsEarnings.add(oReceiptEarningSubsidy);
        }
        
        if (!bComputeTax) {
            moReceipt.setTaxPayrollTheorical(0);
            //moReceipt.setTaxPayrollActual(!aDeductionsByUser.isEmpty() ? dTaxSetByUser : 0); XXX jbarajas 2015-09-10 update deduction when is user
            moReceipt.setTaxPayrollActual(dTaxSetByUser != 0 ? dTaxSetByUser : 0);
            moReceipt.setTaxSubsidyPayrollTheorical(0);
            //moReceipt.setTaxSubsidyPayrollActual(!aEarningsByUser.isEmpty() ? dSubsidySetByUser : 0); XXX jbarajas 2015-09-10 update deduction when is user
            moReceipt.setTaxSubsidyPayrollActual(dSubsidySetByUser != 0 ? dSubsidySetByUser : 0);
            moReceipt.setTaxAnnualTheorical(0);
            moReceipt.setTaxAnnualActual(0);
            moReceipt.setTaxSubsidyAnnualTheorical(0);
            moReceipt.setTaxSubsidyAnnualActual(0);
        }
        else {
            switch (moHrsPayroll.getPayroll().getFkTaxComputationTypeId()) {
                case SModSysConsts.HRSS_TP_TAX_COMP_WOT:
                    moReceipt.setTaxPayrollTheorical(0);
                    //moReceipt.setTaxPayrollActual(!aDeductionsByUser.isEmpty() ? dTaxSetByUser : 0); XXX jbarajas 2015-09-10 update deduction when is user
                    moReceipt.setTaxPayrollActual(dTaxSetByUser != 0 ? dTaxSetByUser : 0);
                    moReceipt.setTaxSubsidyPayrollTheorical(0);
                    //moReceipt.setTaxSubsidyPayrollActual(!aEarningsByUser.isEmpty() ? dSubsidySetByUser : 0); XXX jbarajas 2015-09-10 update deduction when is user
                    moReceipt.setTaxSubsidyPayrollActual(dSubsidySetByUser != 0 ? dSubsidySetByUser : 0);
                    moReceipt.setTaxAnnualTheorical(0);
                    moReceipt.setTaxAnnualActual(0);
                    moReceipt.setTaxSubsidyAnnualTheorical(0);
                    moReceipt.setTaxSubsidyAnnualActual(0);
                    break;
                case SModSysConsts.HRSS_TP_TAX_COMP_PAY:
                    moReceipt.setTaxPayrollTheorical(dTaxComputed);
                    //moReceipt.setTaxPayrollActual(!aDeductionsByUser.isEmpty() ? dTaxSetByUser : moReceipt.getTaxPayrollTheorical()); XXX jbarajas 2015-09-10 update deduction when is user
                    moReceipt.setTaxPayrollActual(dTaxSetByUser != 0 ? dTaxSetByUser : moReceipt.getTaxPayrollTheorical());
                    moReceipt.setTaxSubsidyPayrollTheorical(dSubsidyComputed);
                    //moReceipt.setTaxSubsidyPayrollActual(!aEarningsByUser.isEmpty() ? dSubsidySetByUser : moReceipt.getTaxSubsidyPayrollTheorical()); XXX jbarajas 2015-09-10 update deduction when is user
                    moReceipt.setTaxSubsidyPayrollActual(dSubsidySetByUser != 0 ? dSubsidySetByUser : moReceipt.getTaxSubsidyPayrollTheorical());
                    moReceipt.setTaxAnnualTheorical(0);
                    moReceipt.setTaxAnnualActual(0);
                    moReceipt.setTaxSubsidyAnnualTheorical(0);
                    moReceipt.setTaxSubsidyAnnualActual(0);
                    break;
                case SModSysConsts.HRSS_TP_TAX_COMP_ANN:
                    moReceipt.setTaxPayrollTheorical(dTaxComputed <= dTaxAccumulated ? 0 : SLibUtils.round(dTaxComputed - dTaxAccumulated, SUtilConsts.DECS_AMT));
                    //moReceipt.setTaxPayrollActual(!aDeductionsByUser.isEmpty() ? dTaxSetByUser : moReceipt.getTaxPayrollTheorical()); XXX jbarajas 2015-09-10 update deduction when is user
                    moReceipt.setTaxPayrollActual(dTaxSetByUser != 0 ? dTaxSetByUser : moReceipt.getTaxPayrollTheorical());
                    moReceipt.setTaxSubsidyPayrollTheorical(dSubsidyComputed <= dSubsidyAccumulated ? 0 : SLibUtils.round(dSubsidyComputed - dSubsidyAccumulated, SUtilConsts.DECS_AMT));
                    //moReceipt.setTaxSubsidyPayrollActual(!aEarningsByUser.isEmpty() ? dSubsidySetByUser : moReceipt.getTaxSubsidyPayrollTheorical()); XXX jbarajas 2015-09-10 update deduction when is user
                    moReceipt.setTaxSubsidyPayrollActual(dSubsidySetByUser != 0 ? dSubsidySetByUser : moReceipt.getTaxSubsidyPayrollTheorical());
                    moReceipt.setTaxAnnualTheorical(dTaxComputed);
                    moReceipt.setTaxAnnualActual(dTaxAccumulated);
                    moReceipt.setTaxSubsidyAnnualTheorical(dSubsidyComputed);
                    moReceipt.setTaxSubsidyAnnualActual(dSubsidyAccumulated);
                    break;
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
    }

    private void computeReceiptSsContribution() throws Exception {
        int i = 0;
        double dMwzTimes = 0;
        double dEarningSsc = 0;
        double dSscSetByUser = 0;
        double dSscComputed = 0;
        boolean foundSsc = false;
        SDbSsContributionTable dbSscTable = null;
        SDbSsContributionTableRow dbSscTableRow = null;
        SDbDeduction dbDeductionSsc = null;
        SHrsPayrollReceiptDeduction oReceiptDeductionSsc = null;
        SDbPayrollReceiptDeduction dbReceiptDeductionSsc = null;
        ArrayList<SHrsPayrollReceiptDeduction> aDeductionsToRemove = null;
        ArrayList<SHrsPayrollReceiptDeduction> aDeductionsByUser = null;
        SHrsDaysByPeriod hrsDaysPrev = moHrsEmployee.getHrsDaysPrev();
        SHrsDaysByPeriod hrsDaysCurr = moHrsEmployee.getHrsDaysCurr();
        SHrsDaysByPeriod hrsDaysNext = moHrsEmployee.getHrsDaysNext();
        
        /* XXX jbarajas 2015-09-09 update deduction when is user
        // Remove previous SS contribution:

        aDeductionsToRemove = new ArrayList<SHrsPayrollReceiptDeduction>();
        aDeductionsByUser = new ArrayList<SHrsPayrollReceiptDeduction>();

        for (SHrsPayrollReceiptDeduction deduction : maHrsDeductions) {
            if (deduction.getReceiptDeduction().getFkDeductionTypeId() == SModSysConsts.HRSS_TP_DED_SSC) {
                if (deduction.getReceiptDeduction().isUserEdited()) {
                    aDeductionsByUser.add(deduction);    // preserve user input
                }
                else {
                    aDeductionsToRemove.add(deduction);  // delete previous system computations
                }
            }
        }

        for (SHrsPayrollReceiptDeduction deduction : aDeductionsToRemove) {
            maHrsDeductions.remove(deduction);
        }

        aDeductionsToRemove.clear();

        i = 0;
        for (SHrsPayrollReceiptDeduction deduction : maHrsDeductions) {
            deduction.setPkMoveId(++i);
        }

        // Preserve SS contribution set by user:

        for (SHrsPayrollReceiptDeduction deduction : aDeductionsByUser) {
            deduction.getReceiptDeduction().setAmountSystem_r(0);
            dSscSetByUser += SLibUtils.round(deduction.getReceiptDeduction().getAmount_r(), SUtilConsts.DECS_AMT);
        }
        dSscSetByUser = SLibUtils.round(dSscSetByUser, SUtilConsts.DECS_AMT);
        
        if (moHrsPayroll.getPayroll().isSsContribution() && aDeductionsByUser.isEmpty()) {
            
        */
        if (moHrsPayroll.getPayroll().isSsContribution()) {
            // Compute SS contribution:

            if (moHrsPayroll.getConfig().getFkDeductionSsContributionId_n() == SLibConsts.UNDEFINED) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración SS)");
            }

            dbDeductionSsc = moHrsPayroll.getDataDeduction(moHrsPayroll.getConfig().getFkDeductionSsContributionId_n());
            if (dbDeductionSsc == null) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Configuración SS)");
            }

            if (dbDeductionSsc.getFkDeductionTypeId() != SModSysConsts.HRSS_TP_DED_SSC) {
                throw new Exception(SLibConsts.ERR_MSG_WRONG_TYPE + " (Configuración SS)");
            }

            if (moHrsPayroll.getPayroll().getFkSsContributionId() == SLibConsts.UNDEFINED) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración tabla SS)");
            }

            dbSscTable = moHrsPayroll.getSsContributionTable(moHrsPayroll.getPayroll().getFkSsContributionId());
            if (dbSscTable == null)  {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + " (Tabla SS)");
            }
            
            for (i = 0; i < dbSscTable.getChildRows().size(); i++) {
                dbSscTableRow = dbSscTable.getChildRows().get(i);
                switch(dbSscTableRow.getPkRowId()) {
                    case SHrsConsts.SS_INC_MON:
                    case SHrsConsts.SS_INC_PEN:
                        //dEarningSsc = SLibUtils.round((moReceipt.getDaysHiredPayroll() - moReceipt.getDaysIncapacityNotPaidPayroll()) * moReceipt.getSalarySscBase(), SUtilConsts.DECS_AMT);
                        dEarningSsc = SLibUtils.round((hrsDaysPrev.getDaysPeriodPayroll() + hrsDaysCurr.getDaysPeriodPayroll() + hrsDaysNext.getDaysPeriodPayroll() - hrsDaysPrev.getDaysPeriodPayrollIncapacityNotPaid() - hrsDaysCurr.getDaysPeriodPayrollIncapacityNotPaid() - hrsDaysNext.getDaysPeriodPayrollIncapacityNotPaid()) * moReceipt.getSalarySscBase(), SUtilConsts.DECS_AMT);
                        break;
                    case SHrsConsts.SS_INC_KND_SSC_LET:
                        //dEarningSsc = SLibUtils.round((moReceipt.getDaysHiredPayroll() - moReceipt.getDaysIncapacityNotPaidPayroll()) * moHrsPayroll.getPayroll().getMwzReferenceWage(), SUtilConsts.DECS_AMT);
                        dEarningSsc = SLibUtils.round((hrsDaysPrev.getDaysPeriodPayroll() + hrsDaysCurr.getDaysPeriodPayroll() + hrsDaysNext.getDaysPeriodPayroll() - hrsDaysPrev.getDaysPeriodPayrollIncapacityNotPaid() - hrsDaysCurr.getDaysPeriodPayrollIncapacityNotPaid() - hrsDaysNext.getDaysPeriodPayrollIncapacityNotPaid()) * moHrsPayroll.getPayroll().getMwzReferenceWage(), SUtilConsts.DECS_AMT);
                        break;
                    case SHrsConsts.SS_INC_KND_SSC_GT:
                        //dEarningSsc = SLibUtils.round(moReceipt.getSalarySscBase() <= (dbSscTableRow.getLowerLimitMwzReference() * moHrsPayroll.getPayroll().getMwzReferenceWage()) ? 0 :
                        //       ((moReceipt.getDaysHiredPayroll() - moReceipt.getDaysIncapacityNotPaidPayroll()) * (moReceipt.getSalarySscBase() - (dbSscTableRow.getLowerLimitMwzReference() * moHrsPayroll.getPayroll().getMwzReferenceWage()))), SUtilConsts.DECS_AMT);
                        dEarningSsc = SLibUtils.round(moReceipt.getSalarySscBase() <= (dbSscTableRow.getLowerLimitMwzReference() * moHrsPayroll.getPayroll().getMwzReferenceWage()) ? 0 :
                               ((hrsDaysPrev.getDaysPeriodPayroll() + hrsDaysCurr.getDaysPeriodPayroll() + hrsDaysNext.getDaysPeriodPayroll() - hrsDaysPrev.getDaysPeriodPayrollIncapacityNotPaid() - hrsDaysCurr.getDaysPeriodPayrollIncapacityNotPaid() - hrsDaysNext.getDaysPeriodPayrollIncapacityNotPaid()) * (moReceipt.getSalarySscBase() - (dbSscTableRow.getLowerLimitMwzReference() * moHrsPayroll.getPayroll().getMwzReferenceWage()))), SUtilConsts.DECS_AMT);
                        break;
                    case SHrsConsts.SS_DIS_LIF:
                    case SHrsConsts.SS_CRE:
                    case SHrsConsts.SS_RSK:
                    case SHrsConsts.SS_RET:
                    case SHrsConsts.SS_SEV:
                    case SHrsConsts.SS_HOM:
                        //dEarningSsc = SLibUtils.round((moReceipt.getDaysHiredPayroll() - moReceipt.getDaysNotWorkedNotPaid()) * moReceipt.getSalarySscBase(), SUtilConsts.DECS_AMT);
                        dEarningSsc = SLibUtils.round((hrsDaysPrev.getDaysPeriodPayroll() + hrsDaysCurr.getDaysPeriodPayroll() + hrsDaysNext.getDaysPeriodPayroll() - hrsDaysPrev.getDaysPeriodPayrollNotWorkedNotPaid() - hrsDaysCurr.getDaysPeriodPayrollNotWorkedNotPaid() - hrsDaysNext.getDaysPeriodPayrollNotWorkedNotPaid()) * moReceipt.getSalarySscBase(), SUtilConsts.DECS_AMT);
                        break;
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                dSscComputed += SLibUtils.round(dEarningSsc * dbSscTableRow.getWorkerPercentage(), SUtilConsts.DECS_AMT);
            }
            /* XXX jbarajas 2015-05-13
            dMwzTimes = moHrsPayroll.getPayroll().getMwzWage() == 0 ? 0 : moReceipt.getPaymentDaily() / moHrsPayroll.getPayroll().getMwzWage();
            dEarningSsc = SLibUtils.round(moReceipt.getDaysPaid() * moReceipt.getSalarySscBase(), SUtilConsts.DECS_AMT);

            for (i = 0; i < dbSscTable.getChildRows().size(); i++) {
                dbSscTableRow = dbSscTable.getChildRows().get(i);
                dSscComputed += SLibUtils.round(dMwzTimes < dbSscTableRow.getLowerLimitMwzReference() ? 0 :
                        (dEarningSsc - (dbSscTableRow.getLowerLimitMwzReference() * moHrsPayroll.getPayroll().getMwzReferenceWage() * moReceipt.getDaysPaid())) * dbSscTableRow.getWorkerPercentage(), SUtilConsts.DECS_AMT);
             }
             */

            /* XXX jbarajas 2015-09-09 update deduction when is user
            if (!aDeductionsByUser.isEmpty()) {
                aDeductionsByUser.get(0).getReceiptDeduction().setAmountSystem_r(dSscComputed);
            }
            */
            if (dSscComputed != 0) {
                dbReceiptDeductionSsc = new SDbPayrollReceiptDeduction();
                dbReceiptDeductionSsc.setPkPayrollId(moReceipt.getPkPayrollId());
                dbReceiptDeductionSsc.setPkEmployeeId(moReceipt.getPkEmployeeId());
                dbReceiptDeductionSsc.setPkMoveId(maHrsDeductions.size() + 1);
                dbReceiptDeductionSsc.setUnitsAlleged(1);
                dbReceiptDeductionSsc.setUnits(1);
                dbReceiptDeductionSsc.setAmountUnitary(dSscComputed);
                dbReceiptDeductionSsc.setAmountSystem_r(dbReceiptDeductionSsc.getAmountUnitary());
                dbReceiptDeductionSsc.setAmount_r(dbReceiptDeductionSsc.getAmountUnitary());
                dbReceiptDeductionSsc.setAutomatic(true);
                dbReceiptDeductionSsc.setFkDeductionId(dbDeductionSsc.getPkDeductionId());
                dbReceiptDeductionSsc.setFkDeductionTypeId(dbDeductionSsc.getFkDeductionTypeId());
                dbReceiptDeductionSsc.setFkBenefitTypeId(dbDeductionSsc.getFkBenefitTypeId());

                oReceiptDeductionSsc = new SHrsPayrollReceiptDeduction();
                oReceiptDeductionSsc.setPkMoveId(dbReceiptDeductionSsc.getPkMoveId());
                oReceiptDeductionSsc.setHrsReceipt(this);
                oReceiptDeductionSsc.setDeduction(dbDeductionSsc);
                oReceiptDeductionSsc.setReceiptDeduction(dbReceiptDeductionSsc);

                //maHrsDeductions.add(oReceiptDeductionSsc);
            }
        }
            
        // Remove previous SS contribution:

        aDeductionsToRemove = new ArrayList<SHrsPayrollReceiptDeduction>();

        for (SHrsPayrollReceiptDeduction deduction : maHrsDeductions) {
            if (deduction.getReceiptDeduction().getFkDeductionTypeId() == SModSysConsts.HRSS_TP_DED_SSC) {
                if (deduction.getReceiptDeduction().isUserEdited() || !deduction.getReceiptDeduction().isAutomatic()) {
                    deduction.getReceiptDeduction().setAmountSystem_r(SLibUtils.round(dSscComputed, SUtilConsts.DECS_AMT));    // preserve user input
                    foundSsc = true;
                }
                else {
                    aDeductionsToRemove.add(deduction);  // delete previous system computations
                }
            }
        }

        if (!aDeductionsToRemove.isEmpty()) {
            if (dSscComputed == 0) {
                for (SHrsPayrollReceiptDeduction deduction : aDeductionsToRemove) {
                    maHrsDeductions.remove(deduction);
                }

                aDeductionsToRemove.clear();

                renumberDeductions();
            }
            else {
                for (SHrsPayrollReceiptDeduction deductionRemove : aDeductionsToRemove) {
                    for (SHrsPayrollReceiptDeduction deduction : maHrsDeductions) {
                        if (SLibUtils.compareKeys(new int[] { deduction.getPkMoveId() }, new int[] { deductionRemove.getPkMoveId() })) {
                            deduction.getReceiptDeduction().setAmountUnitary(dSscComputed);
                            deduction.getReceiptDeduction().setAmountSystem_r(deduction.getReceiptDeduction().getAmountUnitary());
                            deduction.getReceiptDeduction().setAmount_r(deduction.getReceiptDeduction().getAmountUnitary());
                        }
                    }
                }
            }
        }
        else if (!foundSsc && oReceiptDeductionSsc != null) {
            maHrsDeductions.add(oReceiptDeductionSsc);
        }
    }

    private int getDaysAbsencePayed(ArrayList<SHrsPayrollReceiptEarning> aHrsPayrollReceiptEarnings) {
        int daysAbsencePayed = 0;

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : aHrsPayrollReceiptEarnings) {
            if (hrsPayrollReceiptEarning.getEarning().isDaysAbsence()) {
                daysAbsencePayed += hrsPayrollReceiptEarning.getReceiptEarning().getUnits();
            }
        }

        return daysAbsencePayed;
    }

    private double getDaysWorkedPayed(ArrayList<SHrsPayrollReceiptEarning> aHrsPayrollReceiptEarnings) {
        double daysWorkedPayed = 0;

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : aHrsPayrollReceiptEarnings) {
            if (hrsPayrollReceiptEarning.getEarning().isDaysWorked()) {
                daysWorkedPayed += hrsPayrollReceiptEarning.getReceiptEarning().getUnits();
            }
        }
        
        /*
        if (daysWorkedPayed >= moHrsPayroll.getPayroll().getReceiptDays()) {
            daysWorkedPayed = 0;
            
            for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : aHrsPayrollReceiptEarnings) {
                if (hrsPayrollReceiptEarning.getEarning().isDaysWorked()) {
                    daysWorkedPayed += hrsPayrollReceiptEarning.getReceiptEarning().getUnits();
                }
            }
        }
        */

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
    public ArrayList<SDbAbsenceConsumption> getAbsenceConsumptions() { return maAbsenceConsumptionDays; }
    public ArrayList<SHrsPayrollReceiptEarning> getHrsEarnings() { return maHrsEarnings; }
    public ArrayList<SHrsPayrollReceiptDeduction> getHrsDeductions() { return maHrsDeductions; }
    public ArrayList<SHrsBenefit> getHrsBenefits() { return maHrsBenefits; }

    public SHrsPayrollReceiptEarning getEarning(final int moveId) {
        SHrsPayrollReceiptEarning oPayrollReceiptEarning = null;

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : maHrsEarnings) {
            if (hrsPayrollReceiptEarning.getReceiptEarning().getPkMoveId() == moveId) {
                oPayrollReceiptEarning = hrsPayrollReceiptEarning;
                break;
            }
        }

        return oPayrollReceiptEarning;
    }

    public void renumberEarnings() {
        int i = 0;
        for (SHrsPayrollReceiptEarning earning : maHrsEarnings) {
            earning.setPkMoveId(++i);
        }
    }

    public SHrsPayrollReceiptEarning addEarning(final SHrsPayrollReceiptEarning hrsPayrollReceiptEarning) {
        try {
            maHrsEarnings.add(hrsPayrollReceiptEarning);
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
            for (int i = 0; i < maHrsEarnings.size(); i++) {
                if (maHrsEarnings.get(i).getPkMoveId() == moveId) {
                    maHrsEarnings.set(i, hrsPayrollReceiptEarning);
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
            for (int i = 0; i < maHrsEarnings.size(); i++) {
                if (maHrsEarnings.get(i).getPkMoveId() == moveId) {
                    maHrsEarnings.remove(i);
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
        for (SHrsPayrollReceiptDeduction deduction : maHrsDeductions) {
            deduction.setPkMoveId(++i);
        }
    }

    public SHrsPayrollReceiptDeduction getDeduction(final int moveId) {
        SHrsPayrollReceiptDeduction oPayrollReceiptDeduction = null;

        for (SHrsPayrollReceiptDeduction hrsPayrollReceiptDeduction : maHrsDeductions) {
            if (hrsPayrollReceiptDeduction.getReceiptDeduction().getPkMoveId() == moveId) {
                oPayrollReceiptDeduction = hrsPayrollReceiptDeduction;
                break;
            }
        }

        return oPayrollReceiptDeduction;
    }

    public SHrsPayrollReceiptDeduction addDeduction(final SHrsPayrollReceiptDeduction hrsPayrollReceiptDeduction) {
        try {
            maHrsDeductions.add(hrsPayrollReceiptDeduction);
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
            for (int i = 0; i < maHrsDeductions.size(); i++) {
                if (maHrsDeductions.get(i).getPkMoveId() == moveId) {
                    maHrsDeductions.set(i, hrsPayrollReceiptDeduction);
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
            for (int i = 0; i < maHrsDeductions.size(); i++) {
                if (maHrsDeductions.get(i).getPkMoveId() == moveId) {
                    maHrsDeductions.remove(i);
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
            maAbsenceConsumptionDays.add(absenceConsumption);

            if (moReceipt != null) {
                computeReceipt();
            }
        }
    }
    
    public void replaceAbsenceConsumption(final SDbAbsenceConsumption absenceConsumption) {
        try {
            for (int i = 0; i < maAbsenceConsumptionDays.size(); i++) {
                if (maAbsenceConsumptionDays.get(i).getPkAbsenceId() == absenceConsumption.getPkAbsenceId() &&
                        maAbsenceConsumptionDays.get(i).getPkEmployeeId() == absenceConsumption.getPkEmployeeId() &&
                        maAbsenceConsumptionDays.get(i).getPkConsumptionId() == absenceConsumption.getPkConsumptionId()) {
                    maAbsenceConsumptionDays.set(i, absenceConsumption);
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
            for (int i = 0; i < maAbsenceConsumptionDays.size(); i++) {
                if (maAbsenceConsumptionDays.get(i).getPkAbsenceId() == absenceConsumption.getPkAbsenceId() &&
                        maAbsenceConsumptionDays.get(i).getPkEmployeeId() == absenceConsumption.getPkEmployeeId() &&
                        maAbsenceConsumptionDays.get(i).getPkConsumptionId() == absenceConsumption.getPkConsumptionId()) {
                    maAbsenceConsumptionDays.remove(i);
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
                for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : maHrsEarnings) {
                    if (SLibUtils.compareKeys(benefit.getPrimaryBenefitType(), new int[] { hrsPayrollReceiptEarning.getReceiptEarning().getFkBenefitTypeId(), 
                        hrsPayrollReceiptEarning.getReceiptEarning().getBenefitAniversary(), hrsPayrollReceiptEarning.getReceiptEarning().getBenefitYear() })) {
                        
                        benefit.setValuePayedReceipt(0d);
                        benefit.setAmountPayedReceipt(0d);
                        
                        // Obtain benefit table row more appropiate for seniority:
            
                        
                        benefitTable = moHrsPayroll.getBenefitTable(moHrsPayroll.getPayroll().getDateEnd(), hrsPayrollReceiptEarning.getReceiptEarning().getFkBenefitTypeId());
                        
                        for (SDbBenefitTableRow row : benefitTable.getChildRows()) {
                            if (row.getMonths() >= (hrsPayrollReceiptEarning.getReceiptEarning().getBenefitAniversary() * SHrsConsts.YEAR_MONTHS)) {
                                benefitTableRow = row;
                                break;
                            }
                        }

                        // Obtain benefit table row more appropiate for seniority, it's for vacation bonification:

                        if (hrsPayrollReceiptEarning.getReceiptEarning().getFkBenefitTypeId() == SModSysConsts.HRSS_TP_BEN_VAC_BON) {
                            benefitTableAux = moHrsPayroll.getBenefitTable(moHrsPayroll.getPayroll().getDateEnd(), SModSysConsts.HRSS_TP_BEN_VAC);
                            
                            for (SDbBenefitTableRow row : benefitTableAux.getChildRows()) {
                                if (row.getMonths() >= (hrsPayrollReceiptEarning.getReceiptEarning().getBenefitAniversary() * SHrsConsts.YEAR_MONTHS)) {
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
        computeEarningsExempt();
         //computeDbPayrollReceipt(); XXX jbarajas 2015-05-11
        computeReceiptTax();
        computeReceiptSsContribution();
    }

    public double getTotalEarnings() {
        double totalEarnings = 0;

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : maHrsEarnings) {
            if (!hrsPayrollReceiptEarning.getReceiptEarning().isDeleted()) {
                totalEarnings += hrsPayrollReceiptEarning.getReceiptEarning().getAmount_r();
            }
        }

        return totalEarnings;
    }

    public double getTotalEarningsExempt() {
        double totalEarningsExept = 0;

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : maHrsEarnings) {
            if (!hrsPayrollReceiptEarning.getReceiptEarning().isDeleted()) {
                totalEarningsExept += hrsPayrollReceiptEarning.getReceiptEarning().getAmountExempt();
            }
        }

        return totalEarningsExept;
    }

    public double getTotalEarningsTaxable() {
        double totalEarningsTaxed = 0;

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : maHrsEarnings) {
            if (!hrsPayrollReceiptEarning.getReceiptEarning().isDeleted()) {
                totalEarningsTaxed += hrsPayrollReceiptEarning.getReceiptEarning().getAmountTaxable();
            }
        }

        return totalEarningsTaxed;
    }

    public double getTotalDeductions() {
        double totalDeductions = 0;

        for (SHrsPayrollReceiptDeduction hrsPayrollReceiptDeduction : maHrsDeductions) {
            if (!hrsPayrollReceiptDeduction.getReceiptDeduction().isDeleted()) {
                totalDeductions += hrsPayrollReceiptDeduction.getReceiptDeduction().getAmount_r();
            }
        }

        return totalDeductions;
    }
    
    public double getBenefitValue(final int benefitType, final int benefitAnn, final int benefitYear) {
        double value = 0;

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : maHrsEarnings) {
            if (SLibUtils.compareKeys(new int[] { benefitType, benefitAnn, benefitYear }, new int[] { hrsPayrollReceiptEarning.getReceiptEarning().getFkBenefitTypeId(), 
                hrsPayrollReceiptEarning.getReceiptEarning().getBenefitAniversary(), hrsPayrollReceiptEarning.getReceiptEarning().getBenefitYear() })) {
                value += hrsPayrollReceiptEarning.getReceiptEarning().getUnitsAlleged();
            }
        }

        return value;
    }
    
    public double getBenefitAmount(final int benefitType, final int benefitAnn, final int benefitYear) {
        double amount = 0;

        for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : maHrsEarnings) {
            if (SLibUtils.compareKeys(new int[] { benefitType, benefitAnn, benefitYear }, new int[] { hrsPayrollReceiptEarning.getReceiptEarning().getFkBenefitTypeId(), 
                hrsPayrollReceiptEarning.getReceiptEarning().getBenefitAniversary(), hrsPayrollReceiptEarning.getReceiptEarning().getBenefitYear() })) {
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
        aEarningDelete = new ArrayList<SHrsPayrollReceiptEarning>();
        
        for (SHrsPayrollReceiptEarning earning : moHrsEmployee.getHrsPayrollReceipt().getHrsEarnings()) {
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
        
        if (earningNormal != null) {
            
            for (SHrsPayrollReceiptEarning earning : moHrsEmployee.getHrsPayrollReceipt().getHrsEarnings()) {
                if (SLibUtils.compareKeys(earning.getEarning().getPrimaryKey(), earningNormal.getPrimaryKey())) {
                    if (add) {
                        unitAlleged = earning.getReceiptEarning().getUnitsAlleged() - (unitAlleged > earning.getReceiptEarning().getUnitsAlleged() ? earning.getReceiptEarning().getUnitsAlleged() : unitAlleged);
                    }
                    else {
                        unitAlleged = earning.getReceiptEarning().getUnitsAlleged() + (unitAlleged > workingDaysAvailable ? workingDaysAvailable : unitAlleged);
                    }
                    unit = !earningNormal.isDaysAdjustment() ? unitAlleged * moHrsEmployee.getEmployeeDays().getFactorCalendar() : (unitAlleged * moHrsEmployee.getEmployeeDays().getFactorCalendar() * moHrsEmployee.getEmployeeDays().getFactorDaysPaid());
                    amount = SLibUtils.round((unit * earning.getReceiptEarning().getAmountUnitary() * earningNormal.getUnitsFactor()), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
                    
                    earning.getReceiptEarning().setUnitsAlleged(unitAlleged);
                    earning.getReceiptEarning().setUnits(unit);
                    earning.getReceiptEarning().setAmount_r(amount);
                    moHrsEmployee.getHrsPayrollReceipt().replaceEarning(earning.getPkMoveId(), earning);
                    found = true;
                    break;
                }
            }

            if (!found) {
                if (earningNormal.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT) {
                    amount_unt = unitAlleged;
                    unit = 1;
                }
                else if (earningNormal.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_DAY) {
                    amount_unt = moHrsEmployee.getHrsPayrollReceipt().getReceipt().getPaymentDaily();
                    unit = !earningNormal.isDaysAdjustment() ? unitAlleged * moHrsEmployee.getEmployeeDays().getFactorCalendar() : (unitAlleged * moHrsEmployee.getEmployeeDays().getFactorCalendar() * moHrsEmployee.getEmployeeDays().getFactorDaysPaid());;
                }
                else if (earningNormal.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_HRS) {
                    amount_unt = moHrsEmployee.getHrsPayrollReceipt().getReceipt().getPaymentHourly();
                    unit = !earningNormal.isDaysAdjustment() ? unitAlleged * moHrsEmployee.getEmployeeDays().getFactorCalendar() : (unitAlleged * moHrsEmployee.getEmployeeDays().getFactorCalendar() * moHrsEmployee.getEmployeeDays().getFactorDaysPaid());;
                }
                else if (earningNormal.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_PER_DAY) {
                    amount_unt = moHrsEmployee.getHrsPayrollReceipt().getReceipt().getPaymentDaily() * earningNormal.getPayPercentage();
                    unit = !earningNormal.isDaysAdjustment() ? unitAlleged * moHrsEmployee.getEmployeeDays().getFactorCalendar() : (unitAlleged * moHrsEmployee.getEmployeeDays().getFactorCalendar() * moHrsEmployee.getEmployeeDays().getFactorDaysPaid());;
                }
                else if (earningNormal.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_PER_HRS) {
                    amount_unt = moHrsEmployee.getHrsPayrollReceipt().getReceipt().getPaymentHourly() * earningNormal.getPayPercentage();
                    unit = !earningNormal.isDaysAdjustment() ? unitAlleged * moHrsEmployee.getEmployeeDays().getFactorCalendar() : (unitAlleged * moHrsEmployee.getEmployeeDays().getFactorCalendar() * moHrsEmployee.getEmployeeDays().getFactorDaysPaid());;
                }
                amount = SLibUtils.round((unit * amount_unt), SLibUtils.DecimalFormatValue2D.getMaximumFractionDigits());
            
                hrsPayrollReceiptEarning = new SHrsPayrollReceiptEarning();
                hrsPayrollReceiptEarning.setEarning(earningNormal);

                payrollReceiptEarning = moHrsPayroll.createHrsPayrollReceiptEarning(moHrsEmployee.getEmployee().getPkEmployeeId(), unitAlleged, amount_unt, amount, true, earningNormal,
                    SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, earningNormal.getFkLoanTypeId(), moHrsEmployee.getHrsPayrollReceipt());

                hrsPayrollReceiptEarning.setReceiptEarning(payrollReceiptEarning);
                hrsPayrollReceiptEarning.setPkMoveId(moHrsEmployee.getHrsPayrollReceipt().getHrsEarnings().size() + 1);
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
        //moReceipt.setPayrollFactorTax(d); // Update in the computeReceiptTax function
        moReceipt.setAnnualTaxableDays_r(moHrsEmployee.getEmployeeDays().getAnnualTaxableDays_r());
        //moReceipt.setAnnualFactorTax(d); // Update in the computeReceiptTax function
        moReceipt.setDaysToBePaid_r(getDaysToBePaid(moHrsEmployee.getHrsPayrollReceipt().getHrsEarnings()));
        moReceipt.setDaysPaid(getDaysWorkedPayed(moHrsEmployee.getHrsPayrollReceipt().getHrsEarnings()));
        /*
        moReceipt.setDaysAbsence(moHrsEmployee.getEmployeeDays().getDaysCalendarAbsence());
        moReceipt.setDaysAbsencePayed(getDaysAbsencePayed(moHrsEmployee.getHrsPayrollReceipt().getHrsEarnings()));
        moReceipt.setDaysWorkedPayed(getDaysWorkedPayed(moHrsEmployee.getHrsPayrollReceipt().getHrsEarnings()));
        */
        computeHrsDaysByPeriod();
    }
    
    /* XXX jbarajas 2015-05-11
    public void computeDbPayrollReceipt() throws Exception {
        // Compute dbPayrollReceipt values:

        moReceipt.setEarningsExemption_r(moHrsEmployee.getHrsPayrollReceipt().getTotalEarningsExempt());
        moReceipt.setEarningsTaxable_r(moHrsEmployee.getHrsPayrollReceipt().getTotalEarningsTaxable());
        moReceipt.setEarnings_r(moHrsEmployee.getHrsPayrollReceipt().getTotalEarnings());
        moReceipt.setDeductions_r(moHrsEmployee.getHrsPayrollReceipt().getTotalDeductions());
        moReceipt.setPayment_r(moReceipt.getEarnings_r() - moReceipt.getDeductions_r());
        moReceipt.setDaysAdjustment(false); // XXX: Pending
     }
    */
     
    public void computeHrsDaysByPeriod() throws Exception {
        int year = 0;
        int periodYear = 0;
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
        
        moHrsEmployee.setHrsDaysPrev(hrsDaysPrev);
        moHrsEmployee.setHrsDaysCurr(hrsDaysCurr);
        moHrsEmployee.setHrsDaysNext(hrsDaysNext);
    }
    
    private SHrsDaysByPeriod createHrsDaysByPeriod(final int year, final int periodYear) {
        int daysPeriod = 0;
        int daysPeriodPayroll = 0;
        int daysPeriodPayrollNotWorkedNotPaid = 0;
        int daysPeridPayrollIncapacityNotPaid = 0;
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

        if (daysPeriodPayroll > 0) {
            for (SDbAbsenceConsumption absenceConsumption : moHrsEmployee.getAbsencesConsumptions()) {
                difDays = absenceConsumption.getEffectiveDays();
                
                /*
                EXCLUIR LAS VACACIONES COMO DIA NO TRABAJADOS PARA CALCULAR DIAS DEL PERIODO EN CREDITOS
                */
                
                for (int i = 0; i < difDays; i++) {
                    if (SLibTimeUtils.isBelongingToPeriod(SLibTimeUtils.addDate(absenceConsumption.getDateStart(), 0, 0, i), year, periodYear) &&
                        SLibTimeUtils.isBelongingToPeriod(SLibTimeUtils.addDate(absenceConsumption.getDateStart(), 0, 0, i), moHrsEmployee.getPeriodStart(), moHrsEmployee.getPeriodEnd()) &&
                        !absenceConsumption.getAbsence().IsAuxAbsencePayable() &&
                        absenceConsumption.getAbsence().getFkAbsenceClassId() != SModSysConsts.HRSU_CL_ABS_VAC) {
                        daysPeriodPayrollNotWorkedNotPaid++;
                        
                        if (absenceConsumption.getAbsence().getFkAbsenceClassId() == SModSysConsts.HRSU_CL_ABS_DIS) {
                            daysPeridPayrollIncapacityNotPaid++;
                        }
                    }
                }
            }
            
            for (SDbAbsenceConsumption absenceConsumption : maAbsenceConsumptionDays) {
                difDays = absenceConsumption.getEffectiveDays();
                
                for (int i = 0; i < difDays; i++) {
                    if (SLibTimeUtils.isBelongingToPeriod(SLibTimeUtils.addDate(absenceConsumption.getDateStart(), 0, 0, i), year, periodYear) &&
                        SLibTimeUtils.isBelongingToPeriod(SLibTimeUtils.addDate(absenceConsumption.getDateStart(), 0, 0, i), moHrsEmployee.getPeriodStart(), moHrsEmployee.getPeriodEnd()) &&
                        !absenceConsumption.getAbsence().IsAuxAbsencePayable() &&
                        absenceConsumption.getAbsence().getFkAbsenceClassId() != SModSysConsts.HRSU_CL_ABS_VAC) {
                        daysPeriodPayrollNotWorkedNotPaid++;
                        
                        if (absenceConsumption.getAbsence().getFkAbsenceClassId() == SModSysConsts.HRSU_CL_ABS_DIS) {
                            daysPeridPayrollIncapacityNotPaid++;
                        }
                    }
                }
            }
        }
        
        return new SHrsDaysByPeriod(year, periodYear, daysPeriod, daysPeriodPayroll, daysPeriodPayrollNotWorkedNotPaid, daysPeridPayrollIncapacityNotPaid);
    }
    
    public boolean validateAbsenceConsumption(SDbAbsence absence, SDbAbsenceConsumption absenceConsumption) throws Exception {
        int diferencePendingDays = 0;
        int pendigEffectiveDays = 0;
        int diferenceDays = 0;
        int diferenceDaysPeriod = 0;
        double absenceConsumptionPendingDays = 0;
        double workingDaysAvailable = 0;
        double recepitDays = 0;
        boolean earFound = false;
        SHrsEmployeeDays employeeDays = moHrsEmployee.getEmployeeDays();
        
        recepitDays = employeeDays.getReceiptDays();
        workingDaysAvailable = employeeDays.getBusinessDays() - employeeDays.getDaysNotWorked_r();
        diferenceDays = (int) SLibTimeUtils.getDaysDiff(absenceConsumption.getDateEnd(), absenceConsumption.getDateStart()) + 1;
        absenceConsumptionPendingDays = absence.getEffectiveDays() - SHrsUtils.getConsumptionPreviousDays(absence, moHrsEmployee);
        
        diferencePendingDays = (int) SLibTimeUtils.getDaysDiff(absence.getDateEnd(), absenceConsumption.getDateEnd());
        pendigEffectiveDays = (absence.getEffectiveDays() - SHrsUtils.getConsumptionPreviousDays(absence, moHrsEmployee)) - absenceConsumption.getEffectiveDays();
        
        if (moHrsPayroll.getPayroll().isNormal()) {
            if (workingDaysAvailable <= 0) {
                throw new Exception("No existen días laborables que asociar con la incidencia.");
            }
            else if (absenceConsumption.getEffectiveDays() > (absence.getFkAbsenceClassId() == SModSysConsts.HRSU_CL_ABS_DIS ? recepitDays : workingDaysAvailable)) {
                throw new Exception("Los días efectivos de consumo deben ser menor o igual a " + (absence.getFkAbsenceClassId() == SModSysConsts.HRSU_CL_ABS_DIS ? recepitDays : workingDaysAvailable) + ".");
            }
            
            for (SDbAbsenceConsumption absenceConsumptionAux : moHrsEmployee.getHrsPayrollReceipt().getAbsenceConsumptions()) {
                diferenceDaysPeriod += (int) SLibTimeUtils.getDaysDiff(absenceConsumptionAux.getDateEnd(), absenceConsumptionAux.getDateStart()) + 1;
            }
            
            diferenceDaysPeriod += (int) SLibTimeUtils.getDaysDiff(absenceConsumption.getDateEnd(), absenceConsumption.getDateStart()) + 1;
            
            if (diferenceDaysPeriod > recepitDays) {
                throw new Exception("Los días calendario comprendidos en los periodos de consumo son '" + diferenceDaysPeriod + "' y deben ser menor o igual a los días de la nómina del empleado que son '" + recepitDays + "'.");
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
     
     public SHrsPayrollReceipt clone() throws CloneNotSupportedException {
        SHrsPayrollReceipt registry = new SHrsPayrollReceipt();
        
        registry.setReceipt(this.getReceipt().clone());
        registry.setHrsPayroll(this.getHrsPayroll());
        registry.setHrsEmployee(this.getHrsEmployee());
        
        for (SDbAbsenceConsumption absenceConsumption : this.getAbsenceConsumptions()) {
            registry.getAbsenceConsumptions().add(absenceConsumption.clone());
        }
        
        for (SHrsPayrollReceiptEarning hrsEarning : this.getHrsEarnings()) {
            registry.getHrsEarnings().add(hrsEarning.clone());
        }
        
        for (SHrsPayrollReceiptDeduction hrsDeduction : this.getHrsDeductions()) {
            registry.getHrsDeductions().add(hrsDeduction.clone());
        }
        
        for (SHrsBenefit hrsBenefit : this.getHrsBenefits()) {
            registry.getHrsBenefits().add(hrsBenefit.clone());
        }
        
        for (SHrsPayrollReceiptEarning hrsEarning : registry.getHrsEarnings()) {
            hrsEarning.setHrsReceipt(registry);
        }
        
        for (SHrsPayrollReceiptDeduction hrsDeduction : registry.getHrsDeductions()) {
            hrsDeduction.setHrsReceipt(registry);
        }
        
        registry.getHrsPayroll().replaceReceipt(registry.getHrsEmployee().getEmployee().getPkEmployeeId(), registry, false);
        registry.getHrsEmployee().setHrsPayrollReceipt(registry);
        
        return registry;
     }
}

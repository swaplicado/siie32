/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModSysConsts;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas, Néstor Ávalos, Sergio Flores, Edwin Carmona, Sergio Flores
 */
public class SHrsPayroll {

    protected SDbConfig moConfig;
    protected SDbWorkingDaySettings moWorkingDaySettings;
    protected SDbPayroll moPayroll;
    protected SHrsPayrollDataProvider moPayrollDataProvider;

    protected ArrayList<SDbLoanTypeAdjustment> maLoanTypeAdjustment;
    protected ArrayList<SDbUma> maUmas;
    protected ArrayList<SDbHoliday> maHolidays;
    protected ArrayList<SDbTaxTable> maTaxTables;
    protected ArrayList<SDbTaxSubsidyTable> maTaxSubsidyTables;
    protected ArrayList<SDbSsContributionTable> maSsContributionTables;
    protected ArrayList<SDbBenefitTable> maBenefitTables;
    protected ArrayList<SHrsBenefitTableByAnniversary> maBenefitTablesAnniversarys;
    protected ArrayList<SDbMwzTypeWage> maMwzTypeWages;
    protected ArrayList<SDbEarning> maEarnigs;
    protected ArrayList<SDbDeduction> maDeductions;
    protected ArrayList<SDbAutomaticEarning> maAutomaticEarnings;
    protected ArrayList<SDbAutomaticDeduction> maAutomaticDeductions;
    protected ArrayList<SDbEmployee> maEmployees;
    protected ArrayList<SHrsPayrollReceipt> maReceipts;

    protected boolean mbIsLastPayrollPeriod;
    
    public SHrsPayroll() {
        moConfig = null;
        moWorkingDaySettings = null;
        moPayroll = null;
        moPayrollDataProvider = null;

        maLoanTypeAdjustment = new ArrayList<>();
        maUmas = new ArrayList<>();
        maHolidays = new ArrayList<>();
        maTaxTables = new ArrayList<>();
        maTaxSubsidyTables = new ArrayList<>();
        maSsContributionTables = new ArrayList<>();
        maBenefitTables = new ArrayList<>();
        maBenefitTablesAnniversarys = new ArrayList<>();
        maMwzTypeWages = new ArrayList<>();
        maEarnigs = new ArrayList<>();
        maDeductions = new ArrayList<>();
        maAutomaticEarnings = new ArrayList<>();
        maAutomaticDeductions = new ArrayList<>();
        maEmployees = new ArrayList<>();
        maReceipts = new ArrayList<>();
        mbIsLastPayrollPeriod = false;
    }

    /*
     * Private methods:
     */

    private ArrayList<SDbAutomaticEarning> getAutomaticEarnings(final int employeeId, final Date dateStart, final Date dateEnd, final int paysheetTypeId) {
        ArrayList<SDbAutomaticEarning> aAutomaticEarnings = new ArrayList<>();

        for (SDbAutomaticEarning automaticEarning : maAutomaticEarnings) {
            if ((automaticEarning.getFkEmployeeId_n() == SLibConsts.UNDEFINED ||
                    automaticEarning.getFkEmployeeId_n() == employeeId) &&
                    (SLibTimeUtils.isBelongingToPeriod(automaticEarning.getDateStart(), automaticEarning.getDateEnd_n() == null ? dateEnd : automaticEarning.getDateEnd_n(), dateStart, dateEnd)) &&
                    automaticEarning.getFkPaysheetTypeId() == paysheetTypeId) {
                aAutomaticEarnings.add(automaticEarning);
            }
        }

        return aAutomaticEarnings;
    }

    private ArrayList<SHrsPayrollReceiptEarning> getPayrollReceiptEarnings(final SHrsPayrollReceipt hrsPayrollReceipt, final Date dateStart, final Date dateEnd) throws Exception {
        int move = 0;
        SDbEmployee employee = hrsPayrollReceipt.getHrsEmployee().getEmployee();
        ArrayList<SHrsPayrollReceiptEarning> aHrsPayrollReceiptEarnings = new ArrayList<>();
        
        // add automatic earnings:

        ArrayList<SDbAutomaticEarning> dbAutomaticEarnings = getAutomaticEarnings(employee.getPkEmployeeId(), dateStart, dateEnd, moPayroll.getFkPaysheetTypeId());
        
        for (SDbAutomaticEarning dbAutomaticEarning : dbAutomaticEarnings) {
            double amountUnit = 0;
            SHrsPayrollReceiptEarning hrsPayrollReceiptEarning = new SHrsPayrollReceiptEarning();
            
            for (SDbEarning dbEarning : maEarnigs) {
                if (dbEarning.getPkEarningId() == dbAutomaticEarning.getPkEarningId()) {
                    hrsPayrollReceiptEarning.setEarning(dbEarning);
                    
                    if (dbEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT) {
                        amountUnit = dbAutomaticEarning.getAmountUnitary();
                    }
                    break;
                }
            }

            SDbPayrollReceiptEarning dbPayrollReceiptEarning = createHrsPayrollReceiptEarning(
                    hrsPayrollReceipt, null, hrsPayrollReceiptEarning.getEarning(), dbAutomaticEarning.getUnits(), amountUnit, true, 
                    dbAutomaticEarning.getFkLoanEmployeeId_n(), dbAutomaticEarning.getFkLoanLoanId_n(), ++move);

            hrsPayrollReceiptEarning.setReceiptEarning(dbPayrollReceiptEarning);
            hrsPayrollReceiptEarning.setPkMoveId(move);
            hrsPayrollReceiptEarning.setHrsReceipt(hrsPayrollReceipt);
            
            aHrsPayrollReceiptEarnings.add(hrsPayrollReceiptEarning);
        }
        
        if (moPayroll.isNormal()) {
            // add earning and vacation:

            double unitsAlleged = hrsPayrollReceipt.getHrsEmployee().getEmployeeDays().getDaysWorked();

            if (unitsAlleged > 0 && moConfig.getFkEarningEarningId_n() == SLibConsts.UNDEFINED) {
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Configuración percepción normal)");
            }

            if (moConfig.getFkEarningEarningId_n() != SLibConsts.UNDEFINED && unitsAlleged > 0) {
                for (SDbEarning dbEarning : maEarnigs) {
                    double amountUnit = 0;
                    
                    if (dbEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT) {
                        amountUnit = unitsAlleged;
                    }

                    if (SLibUtils.compareKeys(dbEarning.getPrimaryKey(), new int[] { moConfig.getFkEarningEarningId_n() })) {
                        SHrsPayrollReceiptEarning hrsPayrollReceiptEarning = new SHrsPayrollReceiptEarning();
                        hrsPayrollReceiptEarning.setEarning(dbEarning);

                        SDbPayrollReceiptEarning dbPayrollReceiptEarning = createHrsPayrollReceiptEarning(
                                hrsPayrollReceipt, null, dbEarning, unitsAlleged, amountUnit, true, 
                                SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, ++move);
                        
                        hrsPayrollReceiptEarning.setReceiptEarning(dbPayrollReceiptEarning);
                        hrsPayrollReceiptEarning.setPkMoveId(move);
                        hrsPayrollReceiptEarning.setHrsReceipt(hrsPayrollReceipt);
                        
                        aHrsPayrollReceiptEarnings.add(hrsPayrollReceiptEarning);
                    }
                }
            }

            for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarningRow : getHrsPayrollReceiptEarningAbsence(hrsPayrollReceipt.getAbsenceConsumptions(), hrsPayrollReceipt)) {
                aHrsPayrollReceiptEarnings.add(hrsPayrollReceiptEarningRow);
            }
            
            // add automatic vacation bonus, if required:
            if (moConfig.isAutoVacationBonus() && SHrsUtils.isAnniversaryBelongingToPeriod(employee.getDateBenefits(), dateStart, dateEnd)) {
                Date dateCutOff;

                if (!employee.isActive()) {
                    dateCutOff = employee.getDateLastDismiss_n();

                    if (!SLibTimeUtils.isBelongingToPeriod(dateCutOff, dateStart, dateEnd)) {
                        dateCutOff = dateEnd;
                    }
                }
                else {
                    dateCutOff = dateEnd;
                }

                SDbEarning dbEarning = getDataEarningByType(SModSysConsts.HRSS_TP_EAR_VAC_BON);
                SDbBenefitTable dbBenefitTableVacBon = getBenefitTable(SModSysConsts.HRSS_TP_BEN_VAC_BON, dateEnd, moPayroll.getFkPaymentTypeId());
                SDbBenefitTable deBenefitTableVac = getBenefitTable(SModSysConsts.HRSS_TP_BEN_VAC, dateEnd, moPayroll.getFkPaymentTypeId());
                ArrayList<SHrsBenefitTableByAnniversary> hrsBenefitTableByAnniversariesVacBon = getBenefitTableAnniversary(dbBenefitTableVacBon.getPkBenefitId());
                ArrayList<SHrsBenefitTableByAnniversary> hrsBenefitTableByAnniversariesVac = getBenefitTableAnniversary(deBenefitTableVac.getPkBenefitId());
                SHrsBenefitTableByAnniversary hrsBenefitTableByAnniversaryVacBon = null;
                SHrsBenefitTableByAnniversary hrsBenefitTableByAnniversaryVac = null;
                int benefitAnniv = SHrsUtils.getSeniorityEmployee(employee.getDateBenefits(), dateCutOff);
                int benefitYear = SLibTimeUtils.digestDate(dateCutOff)[0] - 1;
                
                for (SHrsBenefitTableByAnniversary anniversaryVacBon : hrsBenefitTableByAnniversariesVacBon) {
                    if (anniversaryVacBon.getBenefitAnn() <= benefitAnniv) {
                        hrsBenefitTableByAnniversaryVacBon = anniversaryVacBon;
                    }
                }
        
                for (SHrsBenefitTableByAnniversary anniversaryVac : hrsBenefitTableByAnniversariesVac) {
                    if (anniversaryVac.getBenefitAnn() <= benefitAnniv) {
                        hrsBenefitTableByAnniversaryVac = anniversaryVac;
                    }
                }
        
                double days = hrsBenefitTableByAnniversaryVac == null ? 0d : hrsBenefitTableByAnniversaryVac.getValue();
                double perc = hrsBenefitTableByAnniversaryVacBon == null ? 0d : hrsBenefitTableByAnniversaryVacBon.getValue();
                double benefit = hrsPayrollReceipt.calculateBenefit(dbEarning, days, perc);
                
                ArrayList<SHrsBenefit> hrsBenefits = SHrsUtils.readHrsBenefits(moPayrollDataProvider.getSession(), employee, SModSysConsts.HRSS_TP_BEN_VAC_BON, benefitAnniv, benefitYear, moPayroll.getPkPayrollId(), hrsBenefitTableByAnniversariesVacBon, hrsBenefitTableByAnniversariesVac, hrsPayrollReceipt.getReceipt().getPaymentDaily());

                double daysPayed = 0;
                double benefitPayed = 0;
                        
                for (SHrsBenefit hrsBenefit : hrsBenefits) {
                    if (SLibUtils.compareKeys(hrsBenefit.getBenefitKey(), new int[] { SModSysConsts.HRSS_TP_BEN_VAC_BON, benefitAnniv, benefitYear })) {
                        daysPayed = hrsBenefit.getValuePayed();
                        benefitPayed = hrsBenefit.getAmountPayed();
                        break;
                    }
                }
                
                if (days > daysPayed || benefit > benefitPayed) {
                    double daysToPay = SLibUtils.round(days <= daysPayed ? 0d : days - daysPayed, SLibUtils.DecimalFormatPercentage8D.getMaximumFractionDigits());
                    double benefitToPay = SLibUtils.roundAmount(benefit <= benefitPayed ? 0d : benefit - benefitPayed);
                    SHrsBenefit hrsBenefit = new SHrsBenefit(SModSysConsts.HRSS_TP_BEN_VAC_BON, benefitAnniv, benefitYear);

                    hrsBenefit.setValue(daysToPay);
                    hrsBenefit.setValuePayed(daysToPay);
                    hrsBenefit.setAmount(benefitToPay);
                    hrsBenefit.setAmountPayed(benefitToPay);

                    hrsBenefit.setFactorAmount(perc);
                    hrsBenefit.setEditAmount(false);
                    hrsBenefit.setValuePayedReceipt(daysToPay);
                    hrsBenefit.setAmountPayedReceipt(benefitToPay);
                    hrsBenefit.setAmountPayedReceiptSys(benefitToPay);

                    SHrsPayrollReceiptEarning hrsPayrollReceiptEarning = new SHrsPayrollReceiptEarning();
                    hrsPayrollReceiptEarning.setEarning(dbEarning);

                    SDbPayrollReceiptEarning dbPayrollReceiptEarning = createHrsPayrollReceiptEarning(
                            hrsPayrollReceipt, hrsBenefit, dbEarning, daysToPay, benefitToPay, true, 
                            SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, ++move);

                    hrsPayrollReceiptEarning.setReceiptEarning(dbPayrollReceiptEarning);
                    hrsPayrollReceiptEarning.setPkMoveId(move);
                    hrsPayrollReceiptEarning.setHrsReceipt(hrsPayrollReceipt);

                    aHrsPayrollReceiptEarnings.add(hrsPayrollReceiptEarning);
                }
            }
        }

        return aHrsPayrollReceiptEarnings;
    }
    
    private ArrayList<SDbAutomaticDeduction> getAutomaticDeductions(final int employeeId, final Date dateStart, final Date dateEnd, final int paysheetTypeId) {
        ArrayList<SDbAutomaticDeduction> aAutomaticDeductions = new ArrayList<>();

        for (SDbAutomaticDeduction automaticDeduction : maAutomaticDeductions) {
            if ((automaticDeduction.getFkEmployeeId_n() == SLibConsts.UNDEFINED ||
                    automaticDeduction.getFkEmployeeId_n() == employeeId) &&
                    (SLibTimeUtils.isBelongingToPeriod(automaticDeduction.getDateStart(), automaticDeduction.getDateEnd_n() == null ? dateEnd : automaticDeduction.getDateEnd_n(), dateStart, dateEnd)) &&
                    automaticDeduction.getFkPaysheetTypeId() == paysheetTypeId) {
                aAutomaticDeductions.add(automaticDeduction);
            }
        }

        return aAutomaticDeductions;
    }

    private ArrayList<SHrsPayrollReceiptDeduction> getPayrollReceiptDeductions(final SHrsPayrollReceipt hrsPayrollReceipt, final Date dateStart, final Date dateEnd) {
        int move = 0;
        int employeeId = hrsPayrollReceipt.getHrsEmployee().getEmployee().getPkEmployeeId();
        double amountLoan = 0;
        double balanceLoan = 0;
        SDbPayrollReceiptDeduction payrollReceiptDeduction = null;
        SHrsPayrollReceiptDeduction hrsPayrollReceiptDeduction = null;

        ArrayList<SDbAutomaticDeduction> aAutomaticDeductions;
        ArrayList<SHrsPayrollReceiptDeduction> aHrsPayrollReceiptDeductions = new ArrayList<>();

        try {
            if (moPayroll.isNormal()) {
                // Get earnings of withholding:

                move = 1;
                for (SDbDeduction deduction : maDeductions) {
                    if (deduction.isWithholding()) {
                        hrsPayrollReceiptDeduction = new SHrsPayrollReceiptDeduction();
                        hrsPayrollReceiptDeduction.setDeduction(deduction);

                        payrollReceiptDeduction = createHrsPayrollReceiptDeduction(employeeId, 0, 0, 0, deduction,
                            SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, deduction.getFkLoanTypeId());

                        payrollReceiptDeduction.setAutomatic(true);
                        hrsPayrollReceiptDeduction.setReceiptDeduction(payrollReceiptDeduction);
                        hrsPayrollReceiptDeduction.setPkMoveId(move);
                        hrsPayrollReceiptDeduction.setHrsReceipt(hrsPayrollReceipt);
                        aHrsPayrollReceiptDeductions.add(hrsPayrollReceiptDeduction);

                        move++;
                    }
                }
            }

            // Get automatic deductions:

            aAutomaticDeductions = getAutomaticDeductions(employeeId, dateStart, dateEnd, moPayroll.getFkPaysheetTypeId());
            for (SDbAutomaticDeduction automaticDeduction : aAutomaticDeductions) {

                hrsPayrollReceiptDeduction = new SHrsPayrollReceiptDeduction();
                for (SDbDeduction deduction : maDeductions) {
                    if (deduction.getPkDeductionId() == automaticDeduction.getPkDeductionId()) {
                        hrsPayrollReceiptDeduction.setDeduction(deduction);
                        break;
                    }
                }

                payrollReceiptDeduction = createHrsPayrollReceiptDeduction(employeeId, automaticDeduction.getUnits(), automaticDeduction.getAmountUnitary(), automaticDeduction.getAmount_r(),
                        hrsPayrollReceiptDeduction.getDeduction(), automaticDeduction.getFkLoanEmployeeId_n(), automaticDeduction.getFkLoanLoanId_n(),
                        automaticDeduction.getFkLoanTypeId_n());

                hrsPayrollReceiptDeduction.setReceiptDeduction(payrollReceiptDeduction);
                hrsPayrollReceiptDeduction.setPkMoveId(move);
                hrsPayrollReceiptDeduction.setHrsReceipt(hrsPayrollReceipt);
                aHrsPayrollReceiptDeductions.add(hrsPayrollReceiptDeduction);

                move++;
            }

            if (moPayroll.isNormal()) {
                // Get loans employee:

                for (SDbLoan loan : hrsPayrollReceipt.getHrsEmployee().getLoans()) {
                    hrsPayrollReceiptDeduction = new SHrsPayrollReceiptDeduction();

                    if (SLibTimeUtils.isBelongingToPeriod(loan.getDateStart(), loan.getDateEnd_n() == null ? dateEnd : loan.getDateEnd_n(), dateStart, dateEnd)) {
                        for (SDbDeduction deduction : maDeductions) {
                            if (deduction.getFkLoanTypeId() == loan.getFkLoanTypeId()) {
                                hrsPayrollReceiptDeduction.setDeduction(deduction);
                                break;
                            }
                        }

                        amountLoan = SHrsUtils.computeAmoutLoan(hrsPayrollReceipt, loan);

                        if (SLibUtils.belongsTo(loan.getFkLoanTypeId(), new int[] { SModSysConsts.HRSS_TP_LOAN_LOA_COM, SModSysConsts.HRSS_TP_LOAN_LOA_UNI, SModSysConsts.HRSS_TP_LOAN_LOA_TPS })) {
                            balanceLoan = SHrsUtils.getBalanceLoan(loan, hrsPayrollReceipt.getHrsEmployee());

                            amountLoan = (amountLoan > balanceLoan ? balanceLoan : amountLoan);
                        }

                        if (amountLoan > 0) {
                            if (hrsPayrollReceiptDeduction.getDeduction() == null) {
                                throw new Exception("No se encontró ninguna deducción para agregar el préstamo '" + loan.getLoanIdentificator() + "'.");
                            }
                            else {
                                payrollReceiptDeduction = createHrsPayrollReceiptDeduction(employeeId, 1, amountLoan, amountLoan,
                                        hrsPayrollReceiptDeduction.getDeduction(), loan.getPkEmployeeId(), loan.getPkLoanId(),
                                        loan.getFkLoanTypeId());

                                payrollReceiptDeduction.setUserEdited(false);
                                hrsPayrollReceiptDeduction.setReceiptDeduction(payrollReceiptDeduction);
                                hrsPayrollReceiptDeduction.setPkMoveId(move);
                                hrsPayrollReceiptDeduction.setHrsReceipt(hrsPayrollReceipt);
                                aHrsPayrollReceiptDeductions.add(hrsPayrollReceiptDeduction);

                                move++;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        return aHrsPayrollReceiptDeductions;
    }

    public SDbPayrollReceiptEarning createHrsPayrollReceiptEarning(final SHrsPayrollReceipt pHrsPayrollReceipt, final SHrsBenefit pHrsBenefit, final SDbEarning pDbEarning, 
            final double pUnitsAlleged, final double pAmountUnit, final boolean pIsAutomatic, final int pLoanEmployeeId_n, final int pLoanLoanId_n, final int pMoveId) {
        double units = 0;
        double amountUnit = 0;
        double amount = 0;
        SDbPayrollReceiptEarning dbPayrollReceiptEarning = new SDbPayrollReceiptEarning();
        
        if (pDbEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT) {
            amountUnit = pAmountUnit;
        }
        else if (pDbEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_DAY) {
            amountUnit = pHrsPayrollReceipt.getReceipt().getPaymentDaily();
        }
        else if (pDbEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_HRS) {
            amountUnit = pHrsPayrollReceipt.getReceipt().getPaymentHourly();
        }
        else if (pDbEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_PER_DAY) {
            amountUnit = pHrsPayrollReceipt.getReceipt().getPaymentDaily() * pDbEarning.getPayPercentage();
        }
        else if (pDbEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_PER_HRS) {
            amountUnit = pHrsPayrollReceipt.getReceipt().getPaymentHourly() * pDbEarning.getPayPercentage();
        }
        else if (pDbEarning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_PER_EAR) {
            amountUnit = pHrsPayrollReceipt.getTotalEarningsDependentsDaysWorked() * pDbEarning.getPayPercentage();
        }
        
        units = SLibUtils.round(pUnitsAlleged * pHrsPayrollReceipt.getHrsEmployee().getEmployeeDays().getFactorCalendar() * (!pDbEarning.isDaysAdjustment() ? 1d : pHrsPayrollReceipt.getHrsEmployee().getEmployeeDays().getFactorDaysPaid()), SLibUtils.DecimalFormatValue8D.getMaximumFractionDigits());
        amount = SLibUtils.roundAmount((units * amountUnit * pDbEarning.getUnitsFactor()));
                
        dbPayrollReceiptEarning.setPkPayrollId(pHrsPayrollReceipt.getReceipt().getPkPayrollId());
        dbPayrollReceiptEarning.setPkEmployeeId(pHrsPayrollReceipt.getHrsEmployee().getEmployee().getPkEmployeeId());
        dbPayrollReceiptEarning.setPkMoveId(pMoveId);
        dbPayrollReceiptEarning.setUnitsAlleged(pUnitsAlleged);
        dbPayrollReceiptEarning.setUnits(units);
        dbPayrollReceiptEarning.setAlternativeTaxCalculation(pDbEarning.isAlternativeTaxCalculation());// XXX (jbarajas, 2016-04-06) articule 174 RLISR
        /*
        payrollReceiptEarning.setAmountExempt(double d);
        payrollReceiptEarning.setAmountTaxable(double d);
        payrollReceiptEarning.setUserEdited(boolean b);
        payrollReceiptEarning.setDeleted(boolean b);
        payrollReceiptEarning.setSystem(boolean b);
        */
        dbPayrollReceiptEarning.setFkEarningTypeId(pDbEarning.getFkEarningTypeId());
        dbPayrollReceiptEarning.setFkEarningId(pDbEarning.getPkEarningId());
        dbPayrollReceiptEarning.setFkLoanEmployeeId_n(pLoanEmployeeId_n);
        dbPayrollReceiptEarning.setFkLoanLoanId_n(pLoanLoanId_n);
        dbPayrollReceiptEarning.setFkLoanTypeId_n(pLoanEmployeeId_n != 0 && pLoanLoanId_n != 0 ? pDbEarning.getFkLoanTypeId() : 0);
        
        if (pHrsBenefit == null) {
            dbPayrollReceiptEarning.setFactorAmount(pDbEarning.getUnitsFactor());
            dbPayrollReceiptEarning.setAmountUnitary(amountUnit);
            dbPayrollReceiptEarning.setAmountSystem_r(amount);
            dbPayrollReceiptEarning.setAmount_r(amount);
            dbPayrollReceiptEarning.setFkBenefitTypeId(pDbEarning.getFkBenefitTypeId());
            dbPayrollReceiptEarning.setBenefitYear(0);
            dbPayrollReceiptEarning.setBenefitAniversary(0);
        }
        else {
            dbPayrollReceiptEarning.setFactorAmount(pHrsBenefit.getFactorAmount());
            dbPayrollReceiptEarning.setAmountUnitary(pHrsPayrollReceipt.getReceipt().getPaymentDaily());
            dbPayrollReceiptEarning.setAmountSystem_r(pHrsBenefit.getAmountPayedReceiptSys());
            dbPayrollReceiptEarning.setAmount_r(pHrsBenefit.getAmountPayedReceipt());
            dbPayrollReceiptEarning.setFkBenefitTypeId(pHrsBenefit.getBenefitTypeId());
            dbPayrollReceiptEarning.setBenefitYear(pHrsBenefit.getBenefitYear());
            dbPayrollReceiptEarning.setBenefitAniversary(pHrsBenefit.getBenefitAnn());
        }
        dbPayrollReceiptEarning.setAutomatic(pIsAutomatic);
        /*
        payrollReceiptEarning.setFkUserInsertId(int n);
        payrollReceiptEarning.setFkUserUpdateId(int n);
        payrollReceiptEarning.setTsUserInsert(Date t);
        payrollReceiptEarning.setTsUserUpdate(Date t);
        */

        return dbPayrollReceiptEarning;
    }

    private SDbPayrollReceiptDeduction createHrsPayrollReceiptDeduction(final int employeeId, final double units, final double amount_unit, final double amount_r,
            final SDbDeduction deduction, final int loanEmployeeId_n, final int loanLoanId_n, final int loanTypeId_n) {
        SDbPayrollReceiptDeduction payrollReceiptDeduction = new SDbPayrollReceiptDeduction();

        payrollReceiptDeduction.setUnitsAlleged(units);
        payrollReceiptDeduction.setUnits(units);
        payrollReceiptDeduction.setAmountUnitary(amount_unit);
        payrollReceiptDeduction.setAmount_r(amount_r);
        payrollReceiptDeduction.setAutomatic(true);
        payrollReceiptDeduction.setPkEmployeeId(employeeId);
        payrollReceiptDeduction.setFkDeductionTypeId(deduction.getFkDeductionTypeId());
        payrollReceiptDeduction.setFkDeductionId(deduction.getPkDeductionId());
        payrollReceiptDeduction.setFkBenefitTypeId(deduction.getFkBenefitTypeId());
        payrollReceiptDeduction.setFkLoanEmployeeId_n(loanEmployeeId_n);
        payrollReceiptDeduction.setFkLoanLoanId_n(loanLoanId_n);
        payrollReceiptDeduction.setFkLoanTypeId_n(loanTypeId_n);

        return payrollReceiptDeduction;
    }

    /*
     * Public methods:
     */

    public void setConfig(SDbConfig o) { moConfig = o; }
    public void setWorkingDaySettings(SDbWorkingDaySettings o) { moWorkingDaySettings = o; }
    public void setPayroll(SDbPayroll o) { moPayroll = o; }
    public void setPayrollDataProvider(SHrsPayrollDataProvider o) { moPayrollDataProvider = o; }
    
    public void setLastPayrollPeriod(boolean b) { mbIsLastPayrollPeriod = b; }

    public SDbConfig getConfig() { return moConfig; }
    public SDbWorkingDaySettings getWorkingDaySettings() { return moWorkingDaySettings; }
    public SDbPayroll getPayroll() { return moPayroll; }

    public ArrayList<SDbLoanTypeAdjustment> getLoanTypeAdjustment() { return maLoanTypeAdjustment; }
    public ArrayList<SDbUma> getUmas() { return maUmas; }
    public ArrayList<SDbHoliday> getHolidays() { return maHolidays; }
    public ArrayList<SDbTaxTable> getTaxTables() { return maTaxTables; }
    public ArrayList<SDbTaxSubsidyTable> getTaxSubsidyTables() { return maTaxSubsidyTables; }
    public ArrayList<SDbSsContributionTable> getSsContributionTables() { return maSsContributionTables; }
    public ArrayList<SDbBenefitTable> getBenefitTables() { return maBenefitTables; }
    public ArrayList<SHrsBenefitTableByAnniversary> getBenefitTablesAnniversarys() { return maBenefitTablesAnniversarys; }
    public ArrayList<SDbMwzTypeWage> getMwzTypeWages() { return maMwzTypeWages; }
    public ArrayList<SDbEarning> getEarnigs() { return maEarnigs; }
    public ArrayList<SDbDeduction> getDeductions() { return maDeductions; }
    public ArrayList<SDbAutomaticEarning> getAutomaticEarnings() { return maAutomaticEarnings; }
    public ArrayList<SDbAutomaticDeduction> getAutomaticDeductions() { return maAutomaticDeductions; }
    public ArrayList<SDbEmployee> getEmployees() { return maEmployees; }
    public ArrayList<SHrsPayrollReceipt> getReceipts() { return maReceipts; }
    
    public boolean isLastPayrollPeriod() { return mbIsLastPayrollPeriod; }
    
    public double getLoanTypeAdjustment(final Date date, final int loanType) {
        double amount = 0;

        for (SDbLoanTypeAdjustment adjustment : maLoanTypeAdjustment) {
            if (!date.before(adjustment.getDateStart()) && adjustment.getPkLoanTypeId() == loanType) {
                amount = adjustment.getAdjustment();
                break;
            }
        }

        return amount;
    }
    
    /**
     * Obtain amount UMA most appropriate for date indicated.
     * @param date Date for required UMA.
     * @return 
     */
    public double getUma(final Date date) {
        double amount = 0;

        for (SDbUma adjustment : maUmas) {
            if (!date.before(adjustment.getDateStart())) {
                amount = adjustment.getAmount();
                break;
            }
        }

        return amount;
    }
    
    public ArrayList<SDbHoliday> getHolidays(final Date dateStart, final Date dateEnd) {
        ArrayList<SDbHoliday> aHolidays = new ArrayList<>();

        for (SDbHoliday holiday : maHolidays) {
            if (holiday.getDate().after(dateStart) && holiday.getDate().before(dateEnd)) {
                aHolidays.add(holiday);
            }
        }

        return aHolidays;
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

    public SDbTaxTable getTaxTableByDate(final Date date) {
        SDbTaxTable taxTable = null;

        for (SDbTaxTable table : maTaxTables) {
            if (!date.before(table.getDateStart())) {
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

    public SDbTaxSubsidyTable getTaxSubsidyTableByDate(final Date date) {
        SDbTaxSubsidyTable taxSubsidyTable = null;

        for (SDbTaxSubsidyTable table : maTaxSubsidyTables) {
            if (!date.before(table.getDateStart())) {
                taxSubsidyTable = table;
                break;
            }
        }

        return taxSubsidyTable;
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
    
    public SDbBenefitTable getBenefitTable(final int tableId) {
        SDbBenefitTable benefitTable = null;

        for (SDbBenefitTable table : maBenefitTables) {
            if (table.getPkBenefitId() == tableId) {
                benefitTable = table;
                break;
            }
        }

        return benefitTable;
    }
    
    /**
     * Gets benefit table according to cut off date and payment type.
     * @param benefitType
     * @param cutOff
     * @param paymentType_n Can be undefined, i. e., zero.
     * @return 
     */
    public SDbBenefitTable getBenefitTable(final int benefitType, final Date cutOff, final int paymentType_n) {
        SDbBenefitTable benefitTable = null;

        // get table with matching benefit type, cut off date and payment type, if any:
        for (SDbBenefitTable table : maBenefitTables) {
            if (table.getFkBenefitTypeId() == benefitType && !cutOff.before(table.getDateStart()) && table.getFkPaymentTypeId_n() == paymentType_n) {
                benefitTable = table;
                break;
            }
        }
        
        // if no table found, get table with matching only benefit type and cut off date, if any:
        if (benefitTable == null) {
            for (SDbBenefitTable table : maBenefitTables) {
                if (table.getFkBenefitTypeId() == benefitType && !cutOff.before(table.getDateStart()) && table.getFkPaymentTypeId_n() == 0) {
                    benefitTable = table;
                    break;
                }
            }
        }

        return benefitTable;
    }
    
    public ArrayList<SHrsBenefitTableByAnniversary> getBenefitTableAnniversary(final int tableId) {
        ArrayList<SHrsBenefitTableByAnniversary> aBenefitTableByAnniversary = new ArrayList<>();

        for (SHrsBenefitTableByAnniversary table : maBenefitTablesAnniversarys) {
            if (table.getBenefitId() == tableId) {
                aBenefitTableByAnniversary.add(table);
            }
        }

        return aBenefitTableByAnniversary;
    }

    public SDbMwzTypeWage getMwzTypeWage(final int mwzTypeId, final int wageId) {
        SDbMwzTypeWage mwzTypeWage = null;

        for (SDbMwzTypeWage wage : maMwzTypeWages) {
            if (wage.getPkMwzTypeId() == mwzTypeId && wage.getPkWageId() == wageId) {
                mwzTypeWage = wage;
                break;
            }
        }

        return mwzTypeWage;
    }

    public SDbMwzTypeWage getMwzTypeWageByDate(final int mwzTypeId, final Date date) {
        SDbMwzTypeWage oMwzTypeWage = null;

        for (SDbMwzTypeWage mwzTypeWage : maMwzTypeWages) {
            if (mwzTypeWage.getPkMwzTypeId() == mwzTypeId && !date.before(mwzTypeWage.getDateStart())) {
                oMwzTypeWage = mwzTypeWage;
                break;
            }
        }

        return oMwzTypeWage;
    }

    public SDbEarning getDataEarning(final int id) {
        SDbEarning earning = null;

        for (SDbEarning entry : maEarnigs) {
            if (entry.getPkEarningId() == id) {
                earning = entry;
                break;
            }
        }

        return earning;
    }

    public SDbEarning getDataEarningByType(final int type) {
        SDbEarning earning = null;

        for (SDbEarning entry : maEarnigs) {
            if (entry.getFkEarningTypeId() == type) {
                earning = entry;
                break;
            }
        }

        return earning;
    }

    public SDbDeduction getDataDeduction(final int id) {
        SDbDeduction deduction = null;

        for (SDbDeduction entry : maDeductions) {
            if (entry.getPkDeductionId() == id) {
                deduction = entry;
                break;
            }
        }

        return deduction;
    }

    public SDbDeduction getDataDeductionByType(final int type) {
        SDbDeduction deduction = null;

        for (SDbDeduction entry : maDeductions) {
            if (entry.getFkDeductionTypeId() == type) {
                deduction = entry;
                break;
            }
        }

        return deduction;
    }

    public SDbEmployee getDataEmployee(final int id) {
        SDbEmployee employee = null;

        for (SDbEmployee entry : maEmployees) {
            if (entry.getPkEmployeeId() == id) {
                employee = entry;
                break;
            }
        }

        return employee;
    }

    private SDbPayrollReceipt createPayrollReceipt(final SHrsEmployee hrsEmployee) throws Exception {
        SDbPayrollReceipt payrollReceipt = null;

        // Create dbReceipt:

        payrollReceipt = new SDbPayrollReceipt();
        payrollReceipt.setPkEmployeeId(hrsEmployee.getEmployee().getPkEmployeeId());
        payrollReceipt.setDateBenefits(hrsEmployee.getEmployee().getDateBenefits());
        payrollReceipt.setDateLastHire(hrsEmployee.getEmployee().getDateLastHire());
        payrollReceipt.setDateLastDismiss_n(hrsEmployee.getEmployee().getDateLastDismiss_n());
        payrollReceipt.setSalary(hrsEmployee.getEmployee().getSalary());
        payrollReceipt.setWage(hrsEmployee.getEmployee().getWage());
        payrollReceipt.setSalarySscBase(hrsEmployee.getEmployee().getSalarySscBase());
        payrollReceipt.setWorkingHoursDay(hrsEmployee.getEmployee().getWorkingHoursDay());
        //payrollReceipt.setBankAccount(hrsEmployee.getEmployee().getBankAccount()); XXX (jbarajas, 2015-10-07) remove by new table
        // XXX modify when payment for fornight is fijo
        payrollReceipt.setPaymentDaily(hrsEmployee.getEmployee().getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_WEE ? hrsEmployee.getEmployee().getSalary() :
                (moConfig.isFornightStandard() ? ((hrsEmployee.getEmployee().getWage() * SHrsConsts.YEAR_MONTHS) / (SHrsConsts.FORNIGHT_FIXED_DAYS * SHrsConsts.YEAR_FORNIGHTS)) :
                ((hrsEmployee.getEmployee().getWage() * SHrsConsts.YEAR_MONTHS) / SHrsConsts.YEAR_DAYS)));
        payrollReceipt.setPaymentHourly(hrsEmployee.getEmployee().getWorkingHoursDay() > 0 ? payrollReceipt.getPaymentDaily() / hrsEmployee.getEmployee().getWorkingHoursDay() : 0);
        payrollReceipt.setFkPaymentTypeId(hrsEmployee.getEmployee().getFkPaymentTypeId());
        payrollReceipt.setFkSalaryTypeId(hrsEmployee.getEmployee().getFkSalaryTypeId());
        payrollReceipt.setFkEmployeeTypeId(hrsEmployee.getEmployee().getFkEmployeeTypeId());
        payrollReceipt.setFkWorkerTypeId(hrsEmployee.getEmployee().getFkWorkerTypeId());
        payrollReceipt.setFkMwzTypeId(hrsEmployee.getEmployee().getFkMwzTypeId());
        payrollReceipt.setFkDepartmentId(hrsEmployee.getEmployee().getFkDepartmentId());
        payrollReceipt.setFkPositionId(hrsEmployee.getEmployee().getFkPositionId());
        payrollReceipt.setFkShiftId(hrsEmployee.getEmployee().getFkShiftId());
        payrollReceipt.setFkContractTypeId(hrsEmployee.getEmployee().getFkContractTypeId());
        payrollReceipt.setFkRecruitmentSchemeTypeId(hrsEmployee.getEmployee().getFkRecruitmentSchemeTypeId());
        payrollReceipt.setFkPositionRiskTypeId(hrsEmployee.getEmployee().getFkPositionRiskTypeId());
        payrollReceipt.setFkWorkingDayTypeId(hrsEmployee.getEmployee().getFkWorkingDayTypeId());
        payrollReceipt.setActive(hrsEmployee.getEmployee().isActive());
        hrsEmployee.getHrsPayrollReceipt().setReceipt(payrollReceipt);
        hrsEmployee.getHrsPayrollReceipt().setHrsPayroll(this);

        return payrollReceipt;
    }
    
    private ArrayList<SDbAbsenceConsumption> getAbsenceConsumptionDays(final SHrsPayrollReceipt hrsPayrollReceipt, final SHrsEmployee hrsEmployee) {
        int consumptionPreviousDays = 0;
        double receiptDays = 0;
        double consumptionEffectiveDays = 0;
        int consumptionCurrentDays = 0;
        int businessDays = 0;
        int diferencePendingDays = 0;
        int pendigEffectiveDays = 0;
        double workingDaysAbsence = 0;
        boolean isConsumptionlast = false;
        Date dateStartConsumption = null;
        Date dateEndConsumption = null;
        SDbAbsenceConsumption absenceConsumption = null;
        ArrayList<SDbAbsenceConsumption> aAbsenceConsumption = null;
        
        try {
            aAbsenceConsumption = new ArrayList<>();
            
            receiptDays = hrsEmployee.getEmployeeDays().getReceiptDays();
            workingDaysAbsence = hrsEmployee.getEmployeeDays().getBusinessDays();
            
            for (SDbAbsence absence : hrsEmployee.getAbsences()) {
                isConsumptionlast = false;
                consumptionPreviousDays = SHrsUtils.getConsumptionPreviousDays(absence, hrsEmployee);
                consumptionCurrentDays = (absence.getEffectiveDays() - consumptionPreviousDays);
                
                if (consumptionCurrentDays > 0 && !absence.isClosed()) {
                    if (absence.getDateStart().compareTo(hrsPayrollReceipt.getHrsPayroll().getPayroll().getDateEnd()) <= 0) {
                        /* XXX jbarajas 2016-03-22 modified for consumption of days naturals
                        if (consumptionCurrentDays > (workingDaysAbsence - consumptionEffectiveDays)) {
                            consumptionCurrentDays = (int) (workingDaysAbsence - consumptionEffectiveDays);
                        }
                        */
                        if (consumptionCurrentDays > ((absence.getFkAbsenceClassId() == SModSysConsts.HRSU_CL_ABS_DIS ? receiptDays : workingDaysAbsence) - consumptionEffectiveDays)) {
                            consumptionCurrentDays = (int) ((absence.getFkAbsenceClassId() == SModSysConsts.HRSU_CL_ABS_DIS ? receiptDays : workingDaysAbsence) - consumptionEffectiveDays);
                        }
                        else {
                            isConsumptionlast = true;
                        }

                        dateStartConsumption = consumptionPreviousDays == 0 ? absence.getDateStart() : SLibTimeUtils.addDate(SHrsUtils.getConsumptionDateLast(absence, hrsEmployee), 0, 0, 1);
                        dateEndConsumption = isConsumptionlast ? absence.getDateEnd() : SLibTimeUtils.addDate(dateStartConsumption, 0, 0, (consumptionCurrentDays - 1));

                        businessDays = consumptionCurrentDays - SHrsUtils.getEmployeeBusinessDays(dateStartConsumption, dateEndConsumption, maHolidays, moWorkingDaySettings);
                        
                        if (businessDays > 0) {
                            while (businessDays > 0) {
                                diferencePendingDays = (int) SLibTimeUtils.getDaysDiff(absence.getDateEnd(), SLibTimeUtils.addDate(dateEndConsumption, 0, 0, businessDays));
                                pendigEffectiveDays = (absence.getEffectiveDays() - consumptionPreviousDays) - consumptionCurrentDays;

                                if (pendigEffectiveDays > diferencePendingDays) {
                                    businessDays--;
                                }
                                else {
                                    dateEndConsumption = SLibTimeUtils.addDate(dateEndConsumption, 0, 0, businessDays);
                                    businessDays = -1;
                                }
                            }
                        }
                        
                        absenceConsumption = hrsPayrollReceipt.createAbsenceConsumption(absence, dateStartConsumption, dateEndConsumption, consumptionCurrentDays);

                        if (hrsPayrollReceipt.validateAbsenceConsumption(absence, absenceConsumption)) {
                            aAbsenceConsumption.add(absenceConsumption);
                        }

                        consumptionEffectiveDays += consumptionCurrentDays;
                        //if (consumptionEffectiveDays >= workingDaysAbsence) {  XXX jbarajas 2016-03-22 modified for consumption of days naturals
                        if (consumptionEffectiveDays >= (absence.getFkAbsenceClassId() == SModSysConsts.HRSU_CL_ABS_DIS ? receiptDays : workingDaysAbsence)) {
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        return aAbsenceConsumption;
    }

    public SHrsPayrollReceipt createReceipt(final int employeeId, int payrollYear, int payrollYearPeriod, int fiscalYear, Date dateStart, Date dateEnd, final int taxComputationType) throws Exception {
        SDbPayrollReceipt payrollReceipt = null;
        SHrsEmployee oHrsEmployee = null;
        SHrsPayrollReceipt oHrsPayrollReceipt = new SHrsPayrollReceipt();
        
        oHrsPayrollReceipt.setHrsPayroll(this);
        
        // Get receipt employee days:
        oHrsEmployee = moPayrollDataProvider.createHrsEmployee(this, moPayroll.getPkPayrollId(), employeeId, payrollYear, payrollYearPeriod, fiscalYear, dateStart, dateEnd, taxComputationType);
        oHrsEmployee.setHrsPayrollReceipt(oHrsPayrollReceipt);
        oHrsPayrollReceipt.setHrsEmployee(oHrsEmployee);
        
        // Create payrollReceipt:
        payrollReceipt = createPayrollReceipt(oHrsEmployee);
        
        oHrsPayrollReceipt.setReceipt(payrollReceipt);

        if (moPayroll.isNormal()) {
            // Get absence consumption:
            oHrsPayrollReceipt.getAbsenceConsumptions().addAll(getAbsenceConsumptionDays(oHrsPayrollReceipt, oHrsEmployee));
        }
        
        // Get earnings:
        oHrsPayrollReceipt.getHrsEarnings().addAll(getPayrollReceiptEarnings(oHrsPayrollReceipt, dateStart, dateEnd));
        
        // Compute days of payroll receipt:
        oHrsPayrollReceipt.computeDbPayrollReceiptDays();

        // Get deductions:
        oHrsPayrollReceipt.getHrsDeductions().addAll(getPayrollReceiptDeductions(oHrsPayrollReceipt, dateStart, dateEnd));

        // Compute payroll receipt:
        oHrsPayrollReceipt.computeReceipt();

        // Add new payroll receipt:
        maReceipts.add(oHrsPayrollReceipt);
        
        oHrsPayrollReceipt.renumberEarnings();
        oHrsPayrollReceipt.renumberDeductions();

        return oHrsPayrollReceipt;
    }

    public void replaceReceipt(final int employeeId, final SHrsPayrollReceipt receipt, final boolean compute) {
        SHrsPayrollReceipt hrsPayrollReceipt = null;

        for (int i = 0; i < maReceipts.size(); i++) {
            hrsPayrollReceipt = maReceipts.get(i);

            if (hrsPayrollReceipt.getReceipt().getPkEmployeeId() == employeeId) {
                maReceipts.set(i, receipt);
                break;
            }
        }
        
        if (compute) {
            try {
                receipt.computeReceipt();
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
    }

    public void removeReceipt(final int employeeId) {
        SHrsPayrollReceipt hrsPayrollReceipt = null;

        for (int i = 0; i < maReceipts.size(); i++) {
            hrsPayrollReceipt = maReceipts.get(i);

            if (hrsPayrollReceipt.getReceipt().getPkEmployeeId() == employeeId) {
                maReceipts.remove(i);
                break;
            }
        }
    }

    public void computeEmployees(final boolean isCopy) {
        try {
            for (SHrsPayrollReceipt receipt : maReceipts) {
                receipt.setHrsEmployee(moPayrollDataProvider.computeEmployee(receipt.getHrsEmployee(), isCopy ? SLibConsts.UNDEFINED : moPayroll.getPkPayrollId(), receipt.getHrsEmployee().getEmployee().getPkEmployeeId(),
                        moPayroll.getPeriodYear(), moPayroll.getPeriod(), moPayroll.getFiscalYear(), moPayroll.getDateStart(), moPayroll.getDateEnd(), moPayroll.getFkTaxComputationTypeId()));
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    public void computeReceipts() {
        try {
            for (SHrsPayrollReceipt receipt : maReceipts) {
                receipt.computeReceipt();
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    public double getFactorDaysPaid() {
        return moPayroll.getWorkingDays() == 0 ? 0 : (double) moPayroll.getReceiptDays() / moPayroll.getWorkingDays();
    }
    
    public double getTotalEarnings() {
        double totalEarnings = 0;

        for (SHrsPayrollReceipt hrsPayrollReceipt : maReceipts) {
            for (SHrsPayrollReceiptEarning hrsPayrollReceiptEarning : hrsPayrollReceipt.getHrsEarnings()) {

                totalEarnings += hrsPayrollReceiptEarning.getReceiptEarning().getAmount_r();
            }
        }

        return totalEarnings;
    }

    public double getTotalDeductions() {
        double totalDeductions = 0;

        for (SHrsPayrollReceipt hrsPayrollReceipt : maReceipts) {
            for (SHrsPayrollReceiptDeduction hrsPayrollReceiptDeduction : hrsPayrollReceipt.getHrsDeductions()) {

                totalDeductions += hrsPayrollReceiptDeduction.getReceiptDeduction().getAmount_r();
            }
        }

        return totalDeductions;
    }

    public ArrayList<SHrsPayrollReceiptEarning> getHrsPayrollReceiptEarningAbsence(final ArrayList<SDbAbsenceConsumption> aAbsenceConsumption, final SHrsPayrollReceipt hrsPayrollReceipt) throws Exception {
        int move = 0;
        int earningId = 0;
        double unit = 0;
        double unitAlleged = 0;
        double amount_unt = 0;
        double amount = 0;
        SDbPayrollReceiptEarning payrollReceiptEarning = null;
        SHrsPayrollReceiptEarning hrsPayrollReceiptEarning = null;
        ArrayList<SHrsPayrollReceiptEarning> aHrsPayrollReceiptEarnings = null;
        SDbEarning earning = null;
        
        aHrsPayrollReceiptEarnings = new ArrayList<>();
        
        move = 1;
        for (SDbAbsenceConsumption receiptAbsenceConsumption : aAbsenceConsumption) {
            earning = null;
            
            for (SDbEarning earningAux : maEarnigs) {
                if (SLibUtils.compareKeys(new int[] { receiptAbsenceConsumption.getAbsence().getFkAbsenceClassId(), receiptAbsenceConsumption.getAbsence().getFkAbsenceTypeId() }, new int[] { earningAux.getFkAbsenceClassId_n(), earningAux.getFkAbsenceTypeId_n() })) {
                    earning = earningAux;
                    break;
                }
            }
            
            if (earning != null) {
                if (earning.getFkAbsenceClassId_n() == SModSysConsts.HRSU_CL_ABS_VAC) { // jbarajas 19/03/2015 validate the class of the absence, of the module configuration or of the type of absence
                    earningId = moConfig.getFkEarningVacationsId_n();
                }
                else {
                   earningId = earning.getPkEarningId();
                }
                
                if (earningId == SLibConsts.UNDEFINED) {
                    throw new Exception("No se encontró ninguna percepción configurada " +
                            (earning.getFkAbsenceClassId_n() == SModSysConsts.HRSU_CL_ABS_VAC ? " en la configuración del módulo para 'vacaciones'":
                            "en el catálogo de tipos de incidencias para el tipo '" + earning.getName()) + "'.");
                }
                unitAlleged = receiptAbsenceConsumption.getEffectiveDays();
                
                hrsPayrollReceiptEarning = new SHrsPayrollReceiptEarning();
                hrsPayrollReceiptEarning.setEarning(earning);
                
                if (earning.getFkEarningComputationTypeId() == SModSysConsts.HRSS_TP_EAR_COMP_AMT) {
                    amount_unt = unitAlleged;
                }

                payrollReceiptEarning = createHrsPayrollReceiptEarning(
                        hrsPayrollReceipt, null, earning, unitAlleged, amount_unt, false, 
                        SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, move);
                
                payrollReceiptEarning.setSystem(true);
                if (earning.getFkBenefitTypeId() != SModSysConsts.HRSS_TP_BEN_NON) {
                    payrollReceiptEarning.setFactorAmount(1);
                    payrollReceiptEarning.setBenefitAniversary(receiptAbsenceConsumption.getAbsence().getBenefitsAniversary());
                    payrollReceiptEarning.setBenefitYear(receiptAbsenceConsumption.getAbsence().getBenefitsYear());
                }
                hrsPayrollReceiptEarning.setReceiptEarning(payrollReceiptEarning);
                hrsPayrollReceiptEarning.setPkMoveId(move);
                hrsPayrollReceiptEarning.setHrsReceipt(hrsPayrollReceipt);
                
                aHrsPayrollReceiptEarnings.add(hrsPayrollReceiptEarning);
            }
            move++;
        }
        
        return aHrsPayrollReceiptEarnings;
    }
}

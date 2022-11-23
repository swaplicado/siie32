/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos, Juan Barajas, Sergio Flores
 */
public class SDbPayrollReceipt extends SDbRegistryUser {

    protected int mnPkPayrollId;
    protected int mnPkEmployeeId;
    protected Date mtDateBenefits;
    protected Date mtDateLastHire;
    protected Date mtDateLastDismissal_n;
    protected double mdSalary;
    protected double mdWage;
    protected double mdSalarySscBase;
    protected int mnWorkingHoursDay;
    protected double mdPaymentDaily;
    protected double mdPaymentHourly;
    protected double mdFactorCalendar;
    protected double mdFactorDaysPaid;
    protected double mdReceiptDays;
    protected double mdWorkingDays;
    protected double mdDaysWorked;
    protected double mdDaysHiredPayroll;
    protected double mdDaysHiredAnnual;
    protected double mdDaysIncapacityNotPaidPayroll;
    protected double mdDaysIncapacityNotPaidAnnual;
    protected double mdDaysNotWorkedButPaid;
    protected double mdDaysNotWorkedNotPaid;
    protected double mdDaysNotWorked_r;
    protected double mdDaysToBePaid_r;
    protected double mdDaysPaid;
    protected double mdEarningsExemption_r;
    protected double mdEarningsTaxable_r;
    protected double mdEarnings_r;
    protected double mdDeductions_r;
    protected double mdPayment_r;
    protected double mdPayrollTaxableDays_r;
    protected double mdPayrollFactorTax;
    protected double mdPayrollTaxAssessed;
    protected double mdPayrollTaxCompensated;
    protected double mdPayrollTaxPending_r;
    protected double mdPayrollTaxPayed;
    protected double mdPayrollTaxSubsidyAssessedGross;
    protected double mdPayrollTaxSubsidyAssessed;
    protected double mdPayrollTaxSubsidyCompensated;
    protected double mdPayrollTaxSubsidyPending_r;
    protected double mdPayrollTaxSubsidyPayed;
    protected double mdAnnualTaxableDays_r;
    protected double mdAnnualFactorTax;
    protected double mdAnnualTaxAssessed;
    protected double mdAnnualTaxCompensated;
    protected double mdAnnualTaxPayed;
    protected double mdAnnualTaxSubsidyAssessed;
    protected double mdAnnualTaxSubsidyCompensated;
    protected double mdAnnualTaxSubsidyPayed;
    protected boolean mbActive;
    protected boolean mbDaysAdjustment;
    protected boolean mbCfdRequired;
    //protected boolean mbDeleted;
    protected int mnFkPaymentTypeId;
    protected int mnFkSalaryTypeId;
    protected int mnFkEmployeeTypeId;
    protected int mnFkWorkerTypeId;
    protected int mnFkMwzTypeId;
    protected int mnFkDepartmentId;
    protected int mnFkPositionId;
    protected int mnFkShiftId;
    protected int mnFkContractTypeId;
    protected int mnFkRecruitmentSchemaTypeId;
    protected int mnFkPositionRiskTypeId;
    protected int mnFkWorkingDayTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbPayrollReceiptEarning> maChildPayrollReceiptEarnings;
    protected ArrayList<SDbPayrollReceiptDeduction> maChildPayrollReceiptDeductions;
    protected ArrayList<SDbAbsenceConsumption> maChildAbsenceConsumptions;
    protected SDbPayrollReceiptIssue moChildPayrollReceiptIssue;
    
    protected Date mtAuxIssueDateOfPayment;
    protected String msAuxIssueUuidRelated;
    protected boolean mbAuxForceReissue;
    
    public SDbPayrollReceipt() {
        super(SModConsts.HRS_PAY_RCP);
    }

    /*
     * Private methods
     */
    
    private void computeReceiptValue() {
        mdEarningsExemption_r = 0;
        mdEarningsTaxable_r = 0;
        mdEarnings_r = 0;
        mdDeductions_r = 0;
        
        for (SDbPayrollReceiptEarning child : maChildPayrollReceiptEarnings) {
            mdEarningsExemption_r += child.getAmountExempt();
            mdEarningsTaxable_r += child.getAmountTaxable();
            mdEarnings_r = SLibUtils.roundAmount(mdEarnings_r + child.getAmount_r());
        }
        
        for (SDbPayrollReceiptDeduction child : maChildPayrollReceiptDeductions) {
            mdDeductions_r = SLibUtils.roundAmount(mdDeductions_r + child.getAmount_r());
        }
        
        mdPayment_r = SLibUtils.roundAmount(mdEarnings_r - mdDeductions_r);
    }
    
    private void requiredCfd() {
        for (SDbPayrollReceiptEarning child : maChildPayrollReceiptEarnings) {
            if (child.getAmount_r() != 0) {
                mbCfdRequired = true;
                break;
            }
        }
        
        if (!mbCfdRequired) {
            for (SDbPayrollReceiptDeduction child : maChildPayrollReceiptDeductions) {
                if (child.getAmount_r() != 0) {
                    mbCfdRequired = true;
                    break;
                }
            }
        }
        
        if (mbCfdRequired) {
           mbCfdRequired = mdPayment_r >= 0; 
        }
    }

    private void deleteDependentRegistries(final SGuiSession session) throws SQLException {
        String[] sentences = new String [] {
            "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED_CMP) + " WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId + ";",
            "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId + ";",
            "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR_CMP) + " WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId + ";",
            "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId + ";",
            "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ABS_CNS) + " WHERE fk_rcp_pay = " + mnPkPayrollId + " AND fk_rcp_emp = " + mnPkEmployeeId + ";"
            };
        
        try (Statement statement = session.getDatabase().getConnection().createStatement()) {
            for (String sql : sentences) {
                statement.execute(msSql = sql);
            }
        }
    }   

    /*
     * Public methods
     */
    
    public void setPkPayrollId(int n) { mnPkPayrollId = n; }
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setDateBenefits(Date t) { mtDateBenefits = t; }
    public void setDateLastHire(Date t) { mtDateLastHire = t; }
    public void setDateLastDismissal_n(Date t) { mtDateLastDismissal_n = t; }
    public void setSalary(double d) { mdSalary = d; }
    public void setWage(double d) { mdWage = d; }
    public void setSalarySscBase(double d) { mdSalarySscBase = d; }
    public void setWorkingHoursDay(int n) { mnWorkingHoursDay = n; }
    public void setPaymentDaily(double d) { mdPaymentDaily = d; }
    public void setPaymentHourly(double d) { mdPaymentHourly = d; }
    /** Set factor for calendar (days). Just an informative datum. */
    public void setFactorCalendar(double d) { mdFactorCalendar = d; }
    /** Set factor for days paids. Just an informative datum. */
    public void setFactorDaysPaid(double d) { mdFactorDaysPaid = d; }
    /** Set receipt days. Just an informative datum. */
    public void setReceiptDays(double d) { mdReceiptDays = d; }
    /** Set working days. Just an informative datum. */
    public void setWorkingDays(double d) { mdWorkingDays = d; }
    /** Set days worked. Just an informative datum. */
    public void setDaysWorked(double d) { mdDaysWorked = d; }
    public void setDaysHiredPayroll(double d) { mdDaysHiredPayroll = d; }
    public void setDaysHiredAnnual(double d) { mdDaysHiredAnnual = d; }
    public void setDaysIncapacityNotPaidPayroll(double d) { mdDaysIncapacityNotPaidPayroll = d; }
    public void setDaysIncapacityNotPaidAnnual(double d) { mdDaysIncapacityNotPaidAnnual = d; }
    public void setDaysNotWorkedButPaid(double d) { mdDaysNotWorkedButPaid = d; }
    public void setDaysNotWorkedNotPaid(double d) { mdDaysNotWorkedNotPaid = d; }
    public void setDaysNotWorked_r(double d) { mdDaysNotWorked_r = d; }
    public void setDaysToBePaid_r(double d) { mdDaysToBePaid_r = d; }
    public void setDaysPaid(double d) { mdDaysPaid = d; }
    public void setEarningsExemption_r(double d) { mdEarningsExemption_r = d; }
    public void setEarningsTaxable_r(double d) { mdEarningsTaxable_r = d; }
    public void setEarnings_r(double d) { mdEarnings_r = d; }
    public void setDeductions_r(double d) { mdDeductions_r = d; }
    public void setPayment_r(double d) { mdPayment_r = d; }
    public void setPayrollTaxableDays_r(double d) { mdPayrollTaxableDays_r = d; }
    public void setPayrollFactorTax(double d) { mdPayrollFactorTax = d; }
    public void setPayrollTaxAssessed(double d) { mdPayrollTaxAssessed = d; }
    public void setPayrollTaxCompensated(double d) { mdPayrollTaxCompensated = d; }
    public void setPayrollTaxPending_r(double d) { mdPayrollTaxPending_r = d; }
    public void setPayrollTaxPayed(double d) { mdPayrollTaxPayed = d; }
    public void setPayrollTaxSubsidyAssessedGross(double d) { mdPayrollTaxSubsidyAssessedGross = d; }
    public void setPayrollTaxSubsidyAssessed(double d) { mdPayrollTaxSubsidyAssessed = d; }
    public void setPayrollTaxSubsidyCompensated(double d) { mdPayrollTaxSubsidyCompensated = d; }
    public void setPayrollTaxSubsidyPending_r(double d) { mdPayrollTaxSubsidyPending_r = d; }
    public void setPayrollTaxSubsidyPayed(double d) { mdPayrollTaxSubsidyPayed = d; }
    public void setAnnualTaxableDays_r(double d) { mdAnnualTaxableDays_r = d; }
    public void setAnnualFactorTax(double d) { mdAnnualFactorTax = d; }
    public void setAnnualTaxAssessed(double d) { mdAnnualTaxAssessed = d; }
    public void setAnnualTaxCompensated(double d) { mdAnnualTaxCompensated = d; }
    public void setAnnualTaxPayed(double d) { mdAnnualTaxPayed = d; }
    public void setAnnualTaxSubsidyAssessed(double d) { mdAnnualTaxSubsidyAssessed = d; }
    public void setAnnualTaxSubsidyCompensated(double d) { mdAnnualTaxSubsidyCompensated = d; }
    public void setAnnualTaxSubsidyPayed(double d) { mdAnnualTaxSubsidyPayed = d; }
    public void setActive(boolean b) { mbActive = b; }
    public void setDaysAdjustment(boolean b) { mbDaysAdjustment = b; }
    public void setCfdRequired(boolean b) { mbCfdRequired = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkPaymentTypeId(int n) { mnFkPaymentTypeId = n; }
    public void setFkSalaryTypeId(int n) { mnFkSalaryTypeId = n; }
    public void setFkEmployeeTypeId(int n) { mnFkEmployeeTypeId = n; }
    public void setFkWorkerTypeId(int n) { mnFkWorkerTypeId = n; }
    public void setFkMwzTypeId(int n) { mnFkMwzTypeId = n; }
    public void setFkDepartmentId(int n) { mnFkDepartmentId = n; }
    public void setFkPositionId(int n) { mnFkPositionId = n; }
    public void setFkShiftId(int n) { mnFkShiftId = n; }
    public void setFkContractTypeId(int n) { mnFkContractTypeId = n; }
    public void setFkRecruitmentSchemaTypeId(int n) { mnFkRecruitmentSchemaTypeId = n; }
    public void setFkPositionRiskTypeId(int n) { mnFkPositionRiskTypeId = n; }
    public void setFkWorkingDayTypeId(int n) { mnFkWorkingDayTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkPayrollId() { return mnPkPayrollId; }
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public Date getDateBenefits() { return mtDateBenefits; }
    public Date getDateLastHire() { return mtDateLastHire; }
    public Date getDateLastDismissal_n() { return mtDateLastDismissal_n; }
    public double getSalary() { return mdSalary; }
    public double getWage() { return mdWage; }
    public double getSalarySscBase() { return mdSalarySscBase; }
    public int getWorkingHoursDay() { return mnWorkingHoursDay; }
    public double getPaymentDaily() { return mdPaymentDaily; }
    public double getPaymentHourly() { return mdPaymentHourly; }
    /** Get factor for calendar (days). Just an informative datum. */
    public double getFactorCalendar() { return mdFactorCalendar; }
    /** Get factor for days paid. Just an informative datum. */
    public double getFactorDaysPaid() { return mdFactorDaysPaid; }
    /** Get receipt days. Just an informative datum. */
    public double getReceiptDays() { return mdReceiptDays; }
    /** Get working days. Just an informative datum. */
    public double getWorkingDays() { return mdWorkingDays; }
    /** Get days worked. Just an informative datum. */
    public double getDaysWorked() { return mdDaysWorked; }
    public double getDaysHiredPayroll() { return mdDaysHiredPayroll; }
    public double getDaysHiredAnnual() { return mdDaysHiredAnnual; }
    public double getDaysIncapacityNotPaidPayroll() { return mdDaysIncapacityNotPaidPayroll; }
    public double getDaysIncapacityNotPaidAnnual() { return mdDaysIncapacityNotPaidAnnual; }
    public double getDaysNotWorkedButPaid() { return mdDaysNotWorkedButPaid; }
    public double getDaysNotWorkedNotPaid() { return mdDaysNotWorkedNotPaid; }
    public double getDaysNotWorked_r() { return mdDaysNotWorked_r; }
    public double getDaysToBePaid_r() { return mdDaysToBePaid_r; }
    public double getDaysPaid() { return mdDaysPaid; }
    public double getEarningsExemption_r() { return mdEarningsExemption_r; }
    public double getEarningsTaxable_r() { return mdEarningsTaxable_r; }
    public double getEarnings_r() { return mdEarnings_r; }
    public double getDeductions_r() { return mdDeductions_r; }
    public double getPayment_r() { return mdPayment_r; }
    public double getPayrollTaxableDays_r() { return mdPayrollTaxableDays_r; }
    public double getPayrollFactorTax() { return mdPayrollFactorTax; }
    public double getPayrollTaxAssessed() { return mdPayrollTaxAssessed; }
    public double getPayrollTaxCompensated() { return mdPayrollTaxCompensated; }
    public double getPayrollTaxPending_r() { return mdPayrollTaxPending_r; }
    public double getPayrollTaxPayed() { return mdPayrollTaxPayed; }
    public double getPayrollTaxSubsidyAssessedGross() { return mdPayrollTaxSubsidyAssessedGross; }
    public double getPayrollTaxSubsidyAssessed() { return mdPayrollTaxSubsidyAssessed; }
    public double getPayrollTaxSubsidyCompensated() { return mdPayrollTaxSubsidyCompensated; }
    public double getPayrollTaxSubsidyPending_r() { return mdPayrollTaxSubsidyPending_r; }
    public double getPayrollTaxSubsidyPayed() { return mdPayrollTaxSubsidyPayed; }
    public double getAnnualTaxableDays_r() { return mdAnnualTaxableDays_r; }
    public double getAnnualFactorTax() { return mdAnnualFactorTax; }
    public double getAnnualTaxAssessed() { return mdAnnualTaxAssessed; }
    public double getAnnualTaxCompensated() { return mdAnnualTaxCompensated; }
    public double getAnnualTaxPayed() { return mdAnnualTaxPayed; }
    public double getAnnualTaxSubsidyAssessed() { return mdAnnualTaxSubsidyAssessed; }
    public double getAnnualTaxSubsidyCompensated() { return mdAnnualTaxSubsidyCompensated; }
    public double getAnnualTaxSubsidyPayed() { return mdAnnualTaxSubsidyPayed; }
    public boolean isActive() { return mbActive; }
    public boolean isDaysAdjustment() { return mbDaysAdjustment; }
    public boolean isCfdRequired() { return mbCfdRequired; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkPaymentTypeId() { return mnFkPaymentTypeId; }
    public int getFkSalaryTypeId() { return mnFkSalaryTypeId; }
    public int getFkEmployeeTypeId() { return mnFkEmployeeTypeId; }
    public int getFkWorkerTypeId() { return mnFkWorkerTypeId; }
    public int getFkMwzTypeId() { return mnFkMwzTypeId; }
    public int getFkDepartmentId() { return mnFkDepartmentId; }
    public int getFkPositionId() { return mnFkPositionId; }
    public int getFkShiftId() { return mnFkShiftId; }
    public int getFkContractTypeId() { return mnFkContractTypeId; }
    public int getFkRecruitmentSchemaTypeId() { return mnFkRecruitmentSchemaTypeId; }
    public int getFkPositionRiskTypeId() { return mnFkPositionRiskTypeId; }
    public int getFkWorkingDayTypeId() { return mnFkWorkingDayTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public void setChildPayrollReceiptIssue(SDbPayrollReceiptIssue o) { moChildPayrollReceiptIssue = o; }

    public ArrayList<SDbPayrollReceiptEarning> getChildPayrollReceiptEarnings() { return maChildPayrollReceiptEarnings; }
    public ArrayList<SDbPayrollReceiptDeduction> getChildPayrollReceiptDeductions() { return maChildPayrollReceiptDeductions; }
    public ArrayList<SDbAbsenceConsumption> getChildAbsenceConsumption() { return maChildAbsenceConsumptions; }
    public SDbPayrollReceiptIssue getChildPayrollReceiptIssue() { return moChildPayrollReceiptIssue; }
    
    public void setAuxIssueDateOfPayment(Date t) { mtAuxIssueDateOfPayment = t; }
    public void setAuxIssueUuidRelated(String s) { msAuxIssueUuidRelated = s; }
    /**
     * Set receipt reissue flag to force a receipt reissue when invoking method updatePayrollReceiptIssue().
     */
    public void setAuxForceReissue(boolean b) { mbAuxForceReissue = b; }

    public Date getAuxIssueDateOfPayment() { return mtAuxIssueDateOfPayment; }
    public String getAuxIssueUuidRelated() { return msAuxIssueUuidRelated; }
    /**
     * Get receipt reissue flag to force a receipt reissue when invoking method updatePayrollReceiptIssue().
     * @return Receipt reissue flag.
     */
    public boolean isAuxForceReissue() { return mbAuxForceReissue; }
    
    /**
     * Create and/or update payroll receipt issue.
     * @param session GUI session.
     * @param dateOfIssue Date of issue.
     * @throws Exception 
     */
    public void updatePayrollReceiptIssue(final SGuiSession session, final Date dateOfIssue) throws Exception {
        boolean issueFromScratch = moChildPayrollReceiptIssue == null || moChildPayrollReceiptIssue.getFkReceiptStatusId() == SModSysConsts.TRNS_ST_DPS_ANNULED;
        boolean issueReactivation = moChildPayrollReceiptIssue != null && moChildPayrollReceiptIssue.getFkReceiptStatusId() == SModSysConsts.TRNS_ST_DPS_NEW;
        
        if (mbAuxForceReissue || issueFromScratch || issueReactivation) {
            String numberSeries = "";
            int paymentSystemType = 0;
            Date dateOfPayment = mtAuxIssueDateOfPayment;

            if (issueFromScratch) {
                // set basic data:
                numberSeries = ((SDbConfig) session.readRegistry(SModConsts.HRS_CFG, new int[] { SUtilConsts.BPR_CO_ID })).getNumberSeries();
                paymentSystemType = SDataConstantsSys.TRNU_TP_PAY_SYS_NA;
                if (dateOfPayment == null) {
                    dateOfPayment = dateOfIssue;
                }
            }
            else {
                // preserve basic data from existing issue:
                numberSeries = moChildPayrollReceiptIssue.getNumberSeries();
                paymentSystemType = moChildPayrollReceiptIssue.getFkPaymentSystemTypeId();
                if (dateOfPayment == null) {
                    dateOfPayment = moChildPayrollReceiptIssue.getDateOfPayment();
                }
            }

            if (mbAuxForceReissue || issueFromScratch) {
                moChildPayrollReceiptIssue = new SDbPayrollReceiptIssue();
                
                moChildPayrollReceiptIssue.setPkPayrollId(mnPkPayrollId);
                moChildPayrollReceiptIssue.setPkEmployeeId(mnPkEmployeeId);
                //moChildPayrollReceiptIssue.setPkIssueId(...); // set when saved
                moChildPayrollReceiptIssue.setNumberSeries(numberSeries); // updated when CFDI generated
                moChildPayrollReceiptIssue.setNumber(0); // updated when CFDI generated
            }

            moChildPayrollReceiptIssue.setDateOfIssue(dateOfIssue); // updated when CFDI generated
            moChildPayrollReceiptIssue.setDateOfPayment(dateOfPayment); // updated when CFDI generated
            moChildPayrollReceiptIssue.setBankAccount(""); // updated when CFDI generated
            moChildPayrollReceiptIssue.setUuidRelated(msAuxIssueUuidRelated); // updated when CFDI generated
            moChildPayrollReceiptIssue.setEarnings_r(mdEarnings_r);
            moChildPayrollReceiptIssue.setDeductions_r(mdDeductions_r);
            moChildPayrollReceiptIssue.setPayment_r(mdPayment_r);
            moChildPayrollReceiptIssue.setDeleted(false);
            moChildPayrollReceiptIssue.setFkReceiptStatusId(SModSysConsts.TRNS_ST_DPS_EMITED);
            moChildPayrollReceiptIssue.setFkBankId_n(0); // updated when CFDI generated
            moChildPayrollReceiptIssue.setFkPaymentSystemTypeId(paymentSystemType); // updated when CFDI generated
            /* Update when save the registry.
            payrollReceiptIssues.setFkUserInsertId(0);
            payrollReceiptIssues.setFkUserUpdateId(0);
            payrollReceiptIssues.setTsUserInsert(null);
            payrollReceiptIssues.setTsUserUpdate(null);
            */
            moChildPayrollReceiptIssue.save(session);

            mtAuxIssueDateOfPayment = null;
            msAuxIssueUuidRelated = "";
            mbAuxForceReissue = false;
        }
    }
    
    /**
     * Reset payroll receipt issue, if already issued an CFD is editable.
     * @param session GUI session.
     * @throws Exception 
     */
    public void updatePayrollReceiptIssueReset(final SGuiSession session) throws Exception {
        if (moChildPayrollReceiptIssue != null && moChildPayrollReceiptIssue.isCfdEditable()) {
            moChildPayrollReceiptIssue.setFkReceiptStatusId(SModSysConsts.TRNS_ST_DPS_NEW);
            moChildPayrollReceiptIssue.save(session);
        }
    }
    
    public String getPayrollReceiptIssueNumber() {
        return moChildPayrollReceiptIssue == null ? "" : moChildPayrollReceiptIssue.getIssueNumber();
    }

    /**
     * Gets effective salary.
     * @param isFortnightStandard Flag that indicates if fortnights are allways fixed to 15 days.
     * @return Effective salary.
     */
    public double getEffectiveSalary(boolean isFortnightStandard) {
        double effectiveSalary;
        
        if (mnFkPaymentTypeId == SModSysConsts.HRSS_TP_PAY_WEE) {
            effectiveSalary = mdSalary;
        }
        else {
            int yearDays = isFortnightStandard ? SHrsConsts.YEAR_DAYS_FORTNIGHTS_FIXED : SHrsConsts.YEAR_DAYS;
            effectiveSalary = SLibUtils.roundAmount((mdWage * SHrsConsts.YEAR_MONTHS) / yearDays);
        }
        
        return effectiveSalary;
    }
    
    /**
     * Gets monthly payment recorded on (fortnightly payrolls) or calculated (weekly payrolls) from the receipt.
     * @return Monthly payment.
     */
    public double getMonthlyPayment() {
        double paymentMonthly;
        
        if (mnFkPaymentTypeId == SModSysConsts.HRSS_TP_PAY_WEE) {
            paymentMonthly = SLibUtils.roundAmount(mdSalary * SHrsConsts.MONTH_DAYS);
        }
        else {
            paymentMonthly = mdWage;
        }
        
        return paymentMonthly;
    }
    
    /**
     * Check if receipt's type of recuitment schema is for wages.
     * @return 
     */
    public boolean isWage() {
        return SHrsUtils.isWages(mnFkRecruitmentSchemaTypeId);
    }
    
    /**
     * Check if receipt's type of recuitment schema is for assimilated.
     * @return 
     */
    public boolean isAssimilated() {
        return SHrsUtils.isAssimilated(mnFkRecruitmentSchemaTypeId);
    }
    
    /**
     * Check if receipt's type of recuitment schema is for retirees.
     * @return 
     */
    public boolean isRetiree() {
        return SHrsUtils.isRetirees(mnFkRecruitmentSchemaTypeId);
    }
    
    /**
     * Check if receipt's type of recuitment schema is for others.
     * @return 
     */
    public boolean isOther() {
        return SHrsUtils.isOthers(mnFkRecruitmentSchemaTypeId);
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPayrollId = pk[0];
        mnPkEmployeeId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPayrollId, mnPkEmployeeId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mlTimeout = 1000 * 60 * 120; // 2 hrs

        mnPkPayrollId = 0;
        mnPkEmployeeId = 0;
        mtDateBenefits = null;
        mtDateLastHire = null;
        mtDateLastDismissal_n = null;
        mdSalary = 0;
        mdWage = 0;
        mdSalarySscBase = 0;
        mnWorkingHoursDay = 0;
        mdPaymentDaily = 0;
        mdPaymentHourly = 0;
        mdFactorCalendar = 0;
        mdFactorDaysPaid = 0;
        mdReceiptDays = 0;
        mdWorkingDays = 0;
        mdDaysWorked = 0;
        mdDaysHiredPayroll = 0;
        mdDaysHiredAnnual = 0;
        mdDaysIncapacityNotPaidPayroll = 0;
        mdDaysIncapacityNotPaidAnnual = 0;
        mdDaysNotWorkedButPaid = 0;
        mdDaysNotWorkedNotPaid = 0;
        mdDaysNotWorked_r = 0;
        mdDaysToBePaid_r = 0;
        mdDaysPaid = 0;
        mdEarningsExemption_r = 0;
        mdEarningsTaxable_r = 0;
        mdEarnings_r = 0;
        mdDeductions_r = 0;
        mdPayment_r = 0;
        mdPayrollTaxableDays_r = 0;
        mdPayrollFactorTax = 0;
        mdPayrollTaxAssessed = 0;
        mdPayrollTaxCompensated = 0;
        mdPayrollTaxPending_r = 0;
        mdPayrollTaxPayed = 0;
        mdPayrollTaxSubsidyAssessedGross = 0;
        mdPayrollTaxSubsidyAssessed = 0;
        mdPayrollTaxSubsidyCompensated = 0;
        mdPayrollTaxSubsidyPending_r = 0;
        mdPayrollTaxSubsidyPayed = 0;
        mdAnnualTaxableDays_r = 0;
        mdAnnualFactorTax = 0;
        mdAnnualTaxAssessed = 0;
        mdAnnualTaxCompensated = 0;
        mdAnnualTaxPayed = 0;
        mdAnnualTaxSubsidyAssessed = 0;
        mdAnnualTaxSubsidyCompensated = 0;
        mdAnnualTaxSubsidyPayed = 0;
        mbActive = false;
        mbDaysAdjustment = false;
        mbCfdRequired = false;
        mbDeleted = false;
        mnFkPaymentTypeId = 0;
        mnFkSalaryTypeId = 0;
        mnFkEmployeeTypeId = 0;
        mnFkWorkerTypeId = 0;
        mnFkMwzTypeId = 0;
        mnFkDepartmentId = 0;
        mnFkPositionId = 0;
        mnFkShiftId = 0;
        mnFkContractTypeId = 0;
        mnFkRecruitmentSchemaTypeId = 0;
        mnFkPositionRiskTypeId = 0;
        mnFkWorkingDayTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildPayrollReceiptEarnings = new ArrayList<>();
        maChildPayrollReceiptDeductions = new ArrayList<>();
        maChildAbsenceConsumptions = new ArrayList<>();
        moChildPayrollReceiptIssue = null;
        
        mtAuxIssueDateOfPayment = null;
        msAuxIssueUuidRelated = "";
        mbAuxForceReissue = false;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_pay = " + pk[0] + " AND id_emp = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {

    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkPayrollId = resultSet.getInt("id_pay");
            mnPkEmployeeId = resultSet.getInt("id_emp");
            mtDateBenefits = resultSet.getDate("dt_ben");
            mtDateLastHire = resultSet.getDate("dt_hire");
            mtDateLastDismissal_n = resultSet.getDate("dt_dis_n");
            mdSalary = resultSet.getDouble("sal");
            mdWage = resultSet.getDouble("wage");
            mdSalarySscBase = resultSet.getDouble("sal_ssc");
            mnWorkingHoursDay = resultSet.getInt("wrk_hrs_day");
            mdPaymentDaily = resultSet.getDouble("pay_day_r");
            mdPaymentHourly = resultSet.getDouble("pay_hr_r");
            mdFactorCalendar = resultSet.getDouble("fac_cal");
            mdFactorDaysPaid = resultSet.getDouble("fac_pad");
            mdReceiptDays = resultSet.getDouble("rcp_day");
            mdWorkingDays = resultSet.getDouble("wrk_day");
            mdDaysWorked = resultSet.getDouble("day_wrk");
            mdDaysHiredPayroll = resultSet.getDouble("day_hire_pay");
            mdDaysHiredAnnual = resultSet.getDouble("day_hire_ann");
            mdDaysIncapacityNotPaidPayroll = resultSet.getDouble("day_inc_not_pad_pay");
            mdDaysIncapacityNotPaidAnnual = resultSet.getDouble("day_inc_not_pad_ann");
            mdDaysNotWorkedButPaid = resultSet.getDouble("day_not_wrk_pad");
            mdDaysNotWorkedNotPaid = resultSet.getDouble("day_not_wrk_not_pad");
            mdDaysNotWorked_r = resultSet.getDouble("day_not_wrk_r");
            mdDaysToBePaid_r = resultSet.getDouble("day_tob_pad_r");
            mdDaysPaid = resultSet.getDouble("day_pad");
            mdEarningsExemption_r = resultSet.getDouble("ear_exem_r");
            mdEarningsTaxable_r = resultSet.getDouble("ear_taxa_r");
            mdEarnings_r = resultSet.getDouble("ear_r");
            mdDeductions_r = resultSet.getDouble("ded_r");
            mdPayment_r = resultSet.getDouble("pay_r");
            mdPayrollTaxableDays_r = resultSet.getDouble("pay_taxa_day_r");
            mdPayrollFactorTax = resultSet.getDouble("pay_fac_tax");
            mdPayrollTaxAssessed = resultSet.getDouble("pay_tax_assd");
            mdPayrollTaxCompensated = resultSet.getDouble("pay_tax_comp");
            mdPayrollTaxPending_r = resultSet.getDouble("pay_tax_pend_r");
            mdPayrollTaxPayed = resultSet.getDouble("pay_tax_payd");
            mdPayrollTaxSubsidyAssessedGross = resultSet.getDouble("pay_tax_sub_assd_gross");
            mdPayrollTaxSubsidyAssessed = resultSet.getDouble("pay_tax_sub_assd");
            mdPayrollTaxSubsidyCompensated = resultSet.getDouble("pay_tax_sub_comp");
            mdPayrollTaxSubsidyPending_r = resultSet.getDouble("pay_tax_sub_pend_r");
            mdPayrollTaxSubsidyPayed = resultSet.getDouble("pay_tax_sub_payd");
            mdAnnualTaxableDays_r = resultSet.getDouble("ann_taxa_day_r");
            mdAnnualFactorTax = resultSet.getDouble("ann_fac_tax");
            mdAnnualTaxAssessed = resultSet.getDouble("ann_tax_assd");
            mdAnnualTaxCompensated = resultSet.getDouble("ann_tax_comp");
            mdAnnualTaxPayed = resultSet.getDouble("ann_tax_payd");
            mdAnnualTaxSubsidyAssessed = resultSet.getDouble("ann_tax_sub_assd");
            mdAnnualTaxSubsidyCompensated = resultSet.getDouble("ann_tax_sub_comp");
            mdAnnualTaxSubsidyPayed = resultSet.getDouble("ann_tax_sub_payd");
            mbActive = resultSet.getBoolean("b_act");
            mbDaysAdjustment = resultSet.getBoolean("b_day_adj");
            mbCfdRequired = resultSet.getBoolean("b_cfd_req");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkPaymentTypeId = resultSet.getInt("fk_tp_pay");
            mnFkSalaryTypeId = resultSet.getInt("fk_tp_sal");
            mnFkEmployeeTypeId = resultSet.getInt("fk_tp_emp");
            mnFkWorkerTypeId = resultSet.getInt("fk_tp_wrk");
            mnFkMwzTypeId = resultSet.getInt("fk_tp_mwz");
            mnFkDepartmentId = resultSet.getInt("fk_dep");
            mnFkPositionId = resultSet.getInt("fk_pos");
            mnFkShiftId = resultSet.getInt("fk_sht");
            mnFkContractTypeId = resultSet.getInt("fk_tp_con");
            mnFkRecruitmentSchemaTypeId = resultSet.getInt("fk_tp_rec_sche");
            mnFkPositionRiskTypeId = resultSet.getInt("fk_tp_pos_risk");
            mnFkWorkingDayTypeId = resultSet.getInt("fk_tp_work_day");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // read as well dependent registries:
            
            try (Statement statement = session.getDatabase().getConnection().createStatement()) {
                msSql = "SELECT id_mov " +
                        "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " " +
                        "WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId + " " +
                        "ORDER BY id_mov;";
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    SDbPayrollReceiptEarning child = new SDbPayrollReceiptEarning();
                    child.read(session, new int[] { mnPkPayrollId, mnPkEmployeeId, resultSet.getInt(1) });
                    maChildPayrollReceiptEarnings.add(child);
                }
                
                msSql = "SELECT id_mov " +
                        "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " " +
                        "WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId + " " +
                        "ORDER BY id_mov;";
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    SDbPayrollReceiptDeduction child = new SDbPayrollReceiptDeduction();
                    child.read(session, new int[] { mnPkPayrollId, mnPkEmployeeId, resultSet.getInt(1) });
                    maChildPayrollReceiptDeductions.add(child);
                }
                
                msSql = "SELECT id_emp, id_abs, id_cns " +
                        "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ABS_CNS) + " " +
                        "WHERE fk_rcp_pay = " + mnPkPayrollId + " AND fk_rcp_emp = " + mnPkEmployeeId + " " +
                        "ORDER BY id_emp, id_abs, id_cns;";
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    SDbAbsenceConsumption child = new SDbAbsenceConsumption();
                    child.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) }); // employee ID, then absence ID, finally consumption ID
                    maChildAbsenceConsumptions.add(child);
                }
                
                // read last issue:
                
                msSql = "SELECT id_iss "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " "
                        + "WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId + " AND NOT b_del "
                        + "ORDER BY id_iss DESC LIMIT 1";
                
                resultSet = statement.executeQuery(msSql);
                if (resultSet.next()) {
                    moChildPayrollReceiptIssue = new SDbPayrollReceiptIssue(); // reading receipt issue
                    moChildPayrollReceiptIssue.read(session, new int[] { mnPkPayrollId, mnPkEmployeeId, resultSet.getInt(1) });
                }
            }

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }
        
        computeReceiptValue();
        requiredCfd();
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbUpdatable = true;
            mbDisableable = true;
            mbDeletable = true;
            mbDisabled = false;
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkPayrollId + ", " + 
                    mnPkEmployeeId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateBenefits) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLastHire) + "', " + 
                    (mtDateLastDismissal_n == null ? "null" :  "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLastDismissal_n) + "'") + ", " +
                    mdSalary + ", " + 
                    mdWage + ", " + 
                    mdSalarySscBase + ", " + 
                    mnWorkingHoursDay + ", " + 
                    mdPaymentDaily + ", " + 
                    mdPaymentHourly + ", " + 
                    mdFactorCalendar + ", " + 
                    mdFactorDaysPaid + ", " + 
                    mdReceiptDays + ", " + 
                    mdWorkingDays + ", " + 
                    mdDaysWorked + ", " + 
                    mdDaysHiredPayroll + ", " + 
                    mdDaysHiredAnnual + ", " + 
                    mdDaysIncapacityNotPaidPayroll + ", " + 
                    mdDaysIncapacityNotPaidAnnual + ", " + 
                    mdDaysNotWorkedButPaid + ", " + 
                    mdDaysNotWorkedNotPaid + ", " + 
                    mdDaysNotWorked_r + ", " + 
                    mdDaysToBePaid_r + ", " + 
                    mdDaysPaid + ", " + 
                    mdEarningsExemption_r + ", " + 
                    mdEarningsTaxable_r + ", " + 
                    mdEarnings_r + ", " + 
                    mdDeductions_r + ", " + 
                    mdPayment_r + ", " + 
                    mdPayrollTaxableDays_r + ", " + 
                    mdPayrollFactorTax + ", " + 
                    mdPayrollTaxAssessed + ", " + 
                    mdPayrollTaxCompensated + ", " + 
                    mdPayrollTaxPending_r + ", " + 
                    mdPayrollTaxPayed + ", " + 
                    mdPayrollTaxSubsidyAssessedGross + ", " + 
                    mdPayrollTaxSubsidyAssessed + ", " + 
                    mdPayrollTaxSubsidyCompensated + ", " + 
                    mdPayrollTaxSubsidyPending_r + ", " + 
                    mdPayrollTaxSubsidyPayed + ", " + 
                    mdAnnualTaxableDays_r + ", " + 
                    mdAnnualFactorTax + ", " + 
                    mdAnnualTaxAssessed + ", " + 
                    mdAnnualTaxCompensated + ", " + 
                    mdAnnualTaxPayed + ", " + 
                    mdAnnualTaxSubsidyAssessed + ", " + 
                    mdAnnualTaxSubsidyCompensated + ", " + 
                    mdAnnualTaxSubsidyPayed + ", " + 
                    (mbActive ? 1 : 0) + ", " + 
                    (mbDaysAdjustment ? 1 : 0) + ", " + 
                    (mbCfdRequired ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkPaymentTypeId + ", " + 
                    mnFkSalaryTypeId + ", " + 
                    mnFkEmployeeTypeId + ", " + 
                    mnFkWorkerTypeId + ", " + 
                    mnFkMwzTypeId + ", " + 
                    mnFkDepartmentId + ", " + 
                    mnFkPositionId + ", " + 
                    mnFkShiftId + ", " + 
                    mnFkContractTypeId + ", " + 
                    mnFkRecruitmentSchemaTypeId + ", " + 
                    mnFkPositionRiskTypeId + ", " +
                    mnFkWorkingDayTypeId + ", " + 
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_pay = " + mnPkPayrollId + ", " +
                    "id_emp = " + mnPkEmployeeId + ", " +
                    */
                    "id_pay = " + mnPkPayrollId + ", " +
                    "id_emp = " + mnPkEmployeeId + ", " +
                    "dt_ben = '" + SLibUtils.DbmsDateFormatDate.format(mtDateBenefits) + "', " +
                    "dt_hire = '" + SLibUtils.DbmsDateFormatDate.format(mtDateLastHire) + "', " +
                    "dt_dis_n = " + (mtDateLastDismissal_n == null ? "null" :  "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLastDismissal_n) + "'") + ", " +
                    "sal = " + mdSalary + ", " +
                    "wage = " + mdWage + ", " +
                    "sal_ssc = " + mdSalarySscBase + ", " +
                    "wrk_hrs_day = " + mnWorkingHoursDay + ", " +
                    "pay_day_r = " + mdPaymentDaily + ", " +
                    "pay_hr_r = " + mdPaymentHourly + ", " +
                    "fac_cal = " + mdFactorCalendar + ", " +
                    "fac_pad = " + mdFactorDaysPaid + ", " +
                    "rcp_day = " + mdReceiptDays + ", " +
                    "wrk_day = " + mdWorkingDays + ", " +
                    "day_wrk = " + mdDaysWorked + ", " +
                    "day_hire_pay = " + mdDaysHiredPayroll + ", " +
                    "day_hire_ann = " + mdDaysHiredAnnual + ", " +
                    "day_inc_not_pad_pay = " + mdDaysIncapacityNotPaidPayroll + ", " +
                    "day_inc_not_pad_ann = " + mdDaysIncapacityNotPaidAnnual + ", " +
                    "day_not_wrk_pad = " + mdDaysNotWorkedButPaid + ", " +
                    "day_not_wrk_not_pad = " + mdDaysNotWorkedNotPaid + ", " +
                    "day_not_wrk_r = " + mdDaysNotWorked_r + ", " +
                    "day_tob_pad_r = " + mdDaysToBePaid_r + ", " +
                    "day_pad = " + mdDaysPaid + ", " +
                    "ear_exem_r = " + mdEarningsExemption_r + ", " +
                    "ear_taxa_r = " + mdEarningsTaxable_r + ", " +
                    "ear_r = " + mdEarnings_r + ", " +
                    "ded_r = " + mdDeductions_r + ", " +
                    "pay_r = " + mdPayment_r + ", " +
                    "pay_taxa_day_r = " + mdPayrollTaxableDays_r + ", " +
                    "pay_fac_tax = " + mdPayrollFactorTax + ", " +
                    "pay_tax_assd = " + mdPayrollTaxAssessed + ", " +
                    "pay_tax_comp = " + mdPayrollTaxCompensated + ", " +
                    "pay_tax_pend_r = " + mdPayrollTaxPending_r + ", " +
                    "pay_tax_payd = " + mdPayrollTaxPayed + ", " +
                    "pay_tax_sub_assd_gross = " + mdPayrollTaxSubsidyAssessedGross + ", " +
                    "pay_tax_sub_assd = " + mdPayrollTaxSubsidyAssessed + ", " +
                    "pay_tax_sub_comp = " + mdPayrollTaxSubsidyCompensated + ", " +
                    "pay_tax_sub_pend_r = " + mdPayrollTaxSubsidyPending_r + ", " +
                    "pay_tax_sub_payd = " + mdPayrollTaxSubsidyPayed + ", " +
                    "ann_taxa_day_r = " + mdAnnualTaxableDays_r + ", " +
                    "ann_fac_tax = " + mdAnnualFactorTax + ", " +
                    "ann_tax_assd = " + mdAnnualTaxAssessed + ", " +
                    "ann_tax_comp = " + mdAnnualTaxCompensated + ", " +
                    "ann_tax_payd = " + mdAnnualTaxPayed + ", " +
                    "ann_tax_sub_assd = " + mdAnnualTaxSubsidyAssessed + ", " +
                    "ann_tax_sub_comp = " + mdAnnualTaxSubsidyCompensated + ", " +
                    "ann_tax_sub_payd = " + mdAnnualTaxSubsidyPayed + ", " +
                    "b_act = " + (mbActive ? 1 : 0) + ", " +
                    "b_day_adj = " + (mbDaysAdjustment ? 1 : 0) + ", " +
                    "b_cfd_req = " + (mbCfdRequired ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_pay = " + mnFkPaymentTypeId + ", " +
                    "fk_tp_sal = " + mnFkSalaryTypeId + ", " +
                    "fk_tp_emp = " + mnFkEmployeeTypeId + ", " +
                    "fk_tp_wrk = " + mnFkWorkerTypeId + ", " +
                    "fk_tp_mwz = " + mnFkMwzTypeId + ", " +
                    "fk_dep = " + mnFkDepartmentId + ", " +
                    "fk_pos = " + mnFkPositionId + ", " +
                    "fk_sht = " + mnFkShiftId + ", " +
                    "fk_tp_con = " + mnFkContractTypeId + ", " +
                    "fk_tp_rec_sche = " + mnFkRecruitmentSchemaTypeId + ", " +
                    "fk_tp_pos_risk = " + mnFkPositionRiskTypeId + ", " +
                    "fk_tp_work_day = " + mnFkWorkingDayTypeId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // save as well dependent registries:

        deleteDependentRegistries(session);
        
        for (SDbPayrollReceiptEarning child : maChildPayrollReceiptEarnings) {
            child.setRegistryNew(true); // treat child as new
            child.setDeleted(mbDeleted);
            child.setPkPayrollId(mnPkPayrollId);
            child.setPkEmployeeId(mnPkEmployeeId);
            child.save(session);
        }

        for (SDbPayrollReceiptDeduction child : maChildPayrollReceiptDeductions) {
            child.setRegistryNew(true); // treat child as new
            child.setDeleted(mbDeleted);
            child.setPkPayrollId(mnPkPayrollId);
            child.setPkEmployeeId(mnPkEmployeeId);
            child.save(session);
        }

        for (SDbAbsenceConsumption child : maChildAbsenceConsumptions) {
            child.setRegistryNew(true); // treat child as new
            child.setDeleted(mbDeleted);
            child.setFkReceiptPayrollId(mnPkPayrollId);
            child.setFkReceiptEmployeeId(mnPkEmployeeId);
            child.save(session);
        }
        
        // XXX 2019-04-23, Sergio Flores: check that member moChildPayrollReceiptIssue is not saved, just as is supposed to be!

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbPayrollReceipt clone() throws CloneNotSupportedException {
        SDbPayrollReceipt registry = new SDbPayrollReceipt();

        registry.setPkPayrollId(this.getPkPayrollId());
        registry.setPkEmployeeId(this.getPkEmployeeId());
        registry.setDateBenefits(this.getDateBenefits());
        registry.setDateLastHire(this.getDateLastHire());
        registry.setDateLastDismissal_n(this.getDateLastDismissal_n());
        registry.setSalary(this.getSalary());
        registry.setWage(this.getWage());
        registry.setSalarySscBase(this.getSalarySscBase());
        registry.setWorkingHoursDay(this.getWorkingHoursDay());
        registry.setPaymentDaily(this.getPaymentDaily());
        registry.setPaymentHourly(this.getPaymentHourly());
        registry.setFactorCalendar(this.getFactorCalendar());
        registry.setFactorDaysPaid(this.getFactorDaysPaid());
        registry.setReceiptDays(this.getReceiptDays());
        registry.setWorkingDays(this.getWorkingDays());
        registry.setDaysWorked(this.getDaysWorked());
        registry.setDaysHiredPayroll(this.getDaysHiredPayroll());
        registry.setDaysHiredAnnual(this.getDaysHiredAnnual());
        registry.setDaysIncapacityNotPaidPayroll(this.getDaysIncapacityNotPaidPayroll());
        registry.setDaysIncapacityNotPaidAnnual(this.getDaysIncapacityNotPaidAnnual());
        registry.setDaysNotWorkedButPaid(this.getDaysNotWorkedButPaid());
        registry.setDaysNotWorkedNotPaid(this.getDaysNotWorkedNotPaid());
        registry.setDaysNotWorked_r(this.getDaysNotWorked_r());
        registry.setDaysToBePaid_r(this.getDaysToBePaid_r());
        registry.setDaysPaid(this.getDaysPaid());
        registry.setEarningsExemption_r(this.getEarningsExemption_r());
        registry.setEarningsTaxable_r(this.getEarningsTaxable_r());
        registry.setEarnings_r(this.getEarnings_r());
        registry.setDeductions_r(this.getDeductions_r());
        registry.setPayment_r(this.getPayment_r());
        registry.setPayrollTaxableDays_r(this.getPayrollTaxableDays_r());
        registry.setPayrollFactorTax(this.getPayrollFactorTax());
        registry.setPayrollTaxAssessed(this.getPayrollTaxAssessed());
        registry.setPayrollTaxCompensated(this.getPayrollTaxCompensated());
        registry.setPayrollTaxPending_r(this.getPayrollTaxPending_r());
        registry.setPayrollTaxPayed(this.getPayrollTaxPayed());
        registry.setPayrollTaxSubsidyAssessedGross(this.getPayrollTaxSubsidyAssessedGross());
        registry.setPayrollTaxSubsidyAssessed(this.getPayrollTaxSubsidyAssessed());
        registry.setPayrollTaxSubsidyCompensated(this.getPayrollTaxSubsidyCompensated());
        registry.setPayrollTaxSubsidyPending_r(this.getPayrollTaxSubsidyPending_r());
        registry.setPayrollTaxSubsidyPayed(this.getPayrollTaxSubsidyPayed());
        registry.setAnnualTaxableDays_r(this.getAnnualTaxableDays_r());
        registry.setAnnualFactorTax(this.getAnnualFactorTax());
        registry.setAnnualTaxAssessed(this.getAnnualTaxAssessed());
        registry.setAnnualTaxCompensated(this.getAnnualTaxCompensated());
        registry.setAnnualTaxPayed(this.getAnnualTaxPayed());
        registry.setAnnualTaxSubsidyAssessed(this.getAnnualTaxSubsidyAssessed());
        registry.setAnnualTaxSubsidyCompensated(this.getAnnualTaxSubsidyCompensated());
        registry.setAnnualTaxSubsidyPayed(this.getAnnualTaxSubsidyPayed());
        registry.setActive(this.isActive());
        registry.setDaysAdjustment(this.isDaysAdjustment());
        registry.setCfdRequired(this.isCfdRequired());
        registry.setDeleted(this.isDeleted());
        registry.setFkPaymentTypeId(this.getFkPaymentTypeId());
        registry.setFkSalaryTypeId(this.getFkSalaryTypeId());
        registry.setFkEmployeeTypeId(this.getFkEmployeeTypeId());
        registry.setFkWorkerTypeId(this.getFkWorkerTypeId());
        registry.setFkMwzTypeId(this.getFkMwzTypeId());
        registry.setFkDepartmentId(this.getFkDepartmentId());
        registry.setFkPositionId(this.getFkPositionId());
        registry.setFkShiftId(this.getFkShiftId());
        registry.setFkContractTypeId(this.getFkContractTypeId());
        registry.setFkRecruitmentSchemaTypeId(this.getFkRecruitmentSchemaTypeId());
        registry.setFkPositionRiskTypeId(this.getFkPositionRiskTypeId());
        registry.setFkWorkingDayTypeId(this.getFkWorkingDayTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        for (SDbPayrollReceiptEarning child : this.getChildPayrollReceiptEarnings()) {
            registry.getChildPayrollReceiptEarnings().add(child.clone());
        }

        for (SDbPayrollReceiptDeduction child : this.getChildPayrollReceiptDeductions()) {
            registry.getChildPayrollReceiptDeductions().add(child.clone());
        }
        
        for (SDbAbsenceConsumption child : this.getChildAbsenceConsumption()) {
            registry.getChildAbsenceConsumption().add(child.clone());
        }
        
        registry.setChildPayrollReceiptIssue(this.getChildPayrollReceiptIssue() == null ? null : this.getChildPayrollReceiptIssue().clone());
        
        registry.setAuxIssueDateOfPayment(this.getAuxIssueDateOfPayment());
        registry.setAuxIssueUuidRelated(this.getAuxIssueUuidRelated());
        registry.setAuxForceReissue(this.isAuxForceReissue());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    @Override
    public boolean canDelete(SGuiSession session) throws SQLException, Exception {
        boolean can = super.canDelete(session);

        if (can && moChildPayrollReceiptIssue != null) {
            if (moChildPayrollReceiptIssue.isCfdStamped()) {
                can = false;
                msQueryResult = "¡No es posible eliminar el recibo, está timbrado!";
            }
            else if (moChildPayrollReceiptIssue.isCfdAnnulled()) {
                can = false;
                msQueryResult = "¡No es posible eliminar el recibo, está anulado!";
            }
        }
        return can;
    }

    @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        mbDeleted = !mbDeleted;
        mnFkUserUpdateId = session.getUser().getPkUserId();

        msSql = "UPDATE " + getSqlTable() + " SET " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                "ts_usr_upd = NOW() " +
                getSqlWhere();
        session.getStatement().execute(msSql);

        deleteDependentRegistries(session);

        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    public static void checkDummyRegistry(final SGuiSession session, final int employeeId) throws Exception {
        SDbPayroll.checkDummyRegistry(session);
        
        String sql = "SELECT COUNT(*) "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " "
            + "WHERE id_pay = 0 AND id_emp = " + employeeId + ";";
        
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next() && resultSet.getInt(1) == 0) {
            SDbPayrollReceipt dummyPayrollReceipt = new SDbPayrollReceipt();
            dummyPayrollReceipt.setPkPayrollId(0);
            dummyPayrollReceipt.setPkEmployeeId(employeeId);
            dummyPayrollReceipt.setDateBenefits(SLibTimeUtils.createDate(2000, 1, 1));
            dummyPayrollReceipt.setDateLastHire(SLibTimeUtils.createDate(2000, 1, 1));
            dummyPayrollReceipt.setFkPaymentTypeId(SModSysConsts.HRSS_TP_PAY_WEE);
            dummyPayrollReceipt.setFkSalaryTypeId(SModSysConsts.HRSS_TP_SAL_FIX);
            dummyPayrollReceipt.setFkEmployeeTypeId(SModSysConsts.HRSU_TP_EMP_NA);
            dummyPayrollReceipt.setFkWorkerTypeId(SModSysConsts.HRSU_TP_WRK_NA);
            dummyPayrollReceipt.setFkMwzTypeId(SModSysConsts.HRSU_TP_MWZ_DEF);
            dummyPayrollReceipt.setFkDepartmentId(SModSysConsts.HRSU_DEP_NA);
            dummyPayrollReceipt.setFkPositionId(SModSysConsts.HRSU_POS_NA);
            dummyPayrollReceipt.setFkShiftId(SModSysConsts.HRSU_SHT_NA);
            dummyPayrollReceipt.setFkContractTypeId(SModSysConsts.HRSS_TP_CON_OTH);
            dummyPayrollReceipt.setFkRecruitmentSchemaTypeId(SModSysConsts.HRSS_TP_REC_SCHE_NA);
            dummyPayrollReceipt.setFkPositionRiskTypeId(SModSysConsts.HRSS_TP_POS_RISK_CL1);
            dummyPayrollReceipt.setFkWorkingDayTypeId(SModSysConsts.HRSS_TP_WORK_DAY_DIU);

            dummyPayrollReceipt.save(session);
        }
    }
}

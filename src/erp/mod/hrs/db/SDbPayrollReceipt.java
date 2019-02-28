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
import sa.lib.SLibConsts;
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
    protected Date mtDateLastDismiss_n;
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
    protected double mdDaysNotWorkedPaid;
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
    protected int mnFkRecruitmentSchemeTypeId;
    protected int mnFkPositionRiskTypeId;
    protected int mnFkWorkingDayTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected Date mtAuxDateIssue;
    protected SDbPayrollReceiptIssue moPayrollReceiptIssue;

    protected ArrayList<SDbPayrollReceiptEarning> maChildPayrollReceiptEarnings;
    protected ArrayList<SDbPayrollReceiptDeduction> maChildPayrollReceiptDeductions;
    protected ArrayList<SDbAbsenceConsumption> maChildAbsenceConsumptions;
    
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
        
        for (SDbPayrollReceiptEarning earning : maChildPayrollReceiptEarnings) {
            mdEarningsExemption_r += earning.getAmountExempt();
            mdEarningsTaxable_r += earning.getAmountTaxable();
            mdEarnings_r += earning.getAmount_r();
        }
        
        for (SDbPayrollReceiptDeduction deduction : maChildPayrollReceiptDeductions) {
            mdDeductions_r += deduction.getAmount_r();
        }
        mdPayment_r = mdEarnings_r - mdDeductions_r;
    }
    
    private void requiredCfd() {
        for (SDbPayrollReceiptEarning earning : maChildPayrollReceiptEarnings) {
            if (earning.getAmount_r() != 0) {
                mbCfdRequired = true;
                break;
            }
        }
        
        if (!mbCfdRequired) {
            for (SDbPayrollReceiptDeduction deduction : maChildPayrollReceiptDeductions) {
                if (deduction.getAmount_r() != 0) {
                    mbCfdRequired = true;
                    break;
                }
            }
        }
        
        if (mbCfdRequired) {
           mbCfdRequired = mdPayment_r >= 0; 
        }
    }

    private void deleteDependentRegistries(final SGuiSession session, final boolean isEdit, final boolean isDeleted) throws SQLException {
        Statement statement = null;

        statement = session.getDatabase().getConnection().createStatement();

        if (!isEdit) {
            // Update to deleted deductions from receipts:

            msSql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " SET b_del = " + (isDeleted ? mbDeleted : true) + " WHERE id_pay = " + mnPkPayrollId;
            statement.executeUpdate(msSql);

            // Update to deleted earnings from receipts:

            msSql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " SET b_del = " + (isDeleted ? mbDeleted : true) + " WHERE id_pay = " + mnPkPayrollId;
            statement.executeUpdate(msSql);

            // Update to deleted absence consumption from receipts:

            msSql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.HRS_ABS_CNS) + " SET b_del = " + (isDeleted ? mbDeleted : true) + " WHERE fk_rcp_pay = " + mnPkPayrollId;
            statement.executeUpdate(msSql);
        }
        else {
            // Delete deductions from receipts:

            msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId;
            statement.executeUpdate(msSql);

            // Delete earnings from receipts:

            msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId;
            statement.executeUpdate(msSql);

            // Delete absence consumption from receipts:

            msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ABS_CNS) + " WHERE fk_rcp_pay = " + mnPkPayrollId + " AND fk_rcp_emp = " + mnPkEmployeeId;
            statement.executeUpdate(msSql);
        }
    }   

    /*
     * Public methods
     */
    
    public void createIssues(final SGuiSession session) throws Exception {
        int issueId = 0;
        String numberSerie = "";
        int number = 0;
        int paymentSystemType = SDataConstantsSys.TRNU_TP_PAY_SYS_NA;
        
        SDbPayrollReceiptIssue payrollReceiptIssues = null;
        
        if (moPayrollReceiptIssue != null && moPayrollReceiptIssue.getFkReceiptStatusId() != SModSysConsts.TRNS_ST_DPS_ANNULED) {
            issueId = moPayrollReceiptIssue.getPkIssueId();
            numberSerie = moPayrollReceiptIssue.getNumberSeries();
            number = moPayrollReceiptIssue.getNumber();
            mtAuxDateIssue = moPayrollReceiptIssue.getDateIssue();
            paymentSystemType = moPayrollReceiptIssue.getFkPaymentSystemTypeId();
        }
        
        if (moPayrollReceiptIssue == null || moPayrollReceiptIssue.getFkReceiptStatusId() != SModSysConsts.TRNS_ST_DPS_EMITED) {
            payrollReceiptIssues = new SDbPayrollReceiptIssue();
            
            payrollReceiptIssues.setPkPayrollId(mnPkPayrollId);
            payrollReceiptIssues.setPkEmployeeId(mnPkEmployeeId);
            payrollReceiptIssues.setPkIssueId(issueId);
            payrollReceiptIssues.setNumberSeries(numberSerie); // Update when generate the CFDI.
            payrollReceiptIssues.setNumber(number); // Update when generate the CFDI.
            payrollReceiptIssues.setDateIssue(mtAuxDateIssue); // Update when generate the CFDI.
            payrollReceiptIssues.setDatePayment(mtAuxDateIssue); // Update when generate the CFDI.
            payrollReceiptIssues.setBankAccount(""); // Update when generate the CFDI.
            payrollReceiptIssues.setEarnings_r(mdEarnings_r);
            payrollReceiptIssues.setDeductions_r(mdDeductions_r);
            payrollReceiptIssues.setPayment_r(mdPayment_r);
            payrollReceiptIssues.setDeleted(false);
            payrollReceiptIssues.setFkReceiptStatusId(SModSysConsts.TRNS_ST_DPS_EMITED);
            payrollReceiptIssues.setFkBankId_n(SLibConsts.UNDEFINED); // Update when generate the CFDI.
            payrollReceiptIssues.setFkPaymentSystemTypeId(paymentSystemType); // Update when generate the CFDI.
            /* Update when save the registry.
            payrollReceiptIssues.setFkUserInsertId(0);
            payrollReceiptIssues.setFkUserUpdateId(0);
            payrollReceiptIssues.setTsUserInsert(null);
            payrollReceiptIssues.setTsUserUpdate(null);
            */
            payrollReceiptIssues.save(session);
        }
    }
    
    public void updateToNewStatusIssues(final SGuiSession session) throws Exception {
        if (moPayrollReceiptIssue != null && moPayrollReceiptIssue.getFkReceiptStatusId() == SModSysConsts.TRNS_ST_DPS_EMITED && !moPayrollReceiptIssue.isStamped()) {
            moPayrollReceiptIssue.setFkReceiptStatusId(SModSysConsts.TRNS_ST_DPS_NEW);
            moPayrollReceiptIssue.save(session);
        }
    }
    
    public void setPkPayrollId(int n) { mnPkPayrollId = n; }
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setDateBenefits(Date t) { mtDateBenefits = t; }
    public void setDateLastHire(Date t) { mtDateLastHire = t; }
    public void setDateLastDismiss_n(Date t) { mtDateLastDismiss_n = t; }
    public void setSalary(double d) { mdSalary = d; }
    public void setWage(double d) { mdWage = d; }
    public void setSalarySscBase(double d) { mdSalarySscBase = d; }
    public void setWorkingHoursDay(int n) { mnWorkingHoursDay = n; }
    public void setPaymentDaily(double d) { mdPaymentDaily = d; }
    public void setPaymentHourly(double d) { mdPaymentHourly = d; }
    public void setFactorCalendar(double d) { mdFactorCalendar = d; }
    public void setFactorDaysPaid(double d) { mdFactorDaysPaid = d; }
    public void setReceiptDays(double d) { mdReceiptDays = d; }
    public void setWorkingDays(double d) { mdWorkingDays = d; }
    public void setDaysWorked(double d) { mdDaysWorked = d; }
    public void setDaysHiredPayroll(double d) { mdDaysHiredPayroll = d; }
    public void setDaysHiredAnnual(double d) { mdDaysHiredAnnual = d; }
    public void setDaysIncapacityNotPaidPayroll(double d) { mdDaysIncapacityNotPaidPayroll = d; }
    public void setDaysIncapacityNotPaidAnnual(double d) { mdDaysIncapacityNotPaidAnnual = d; }
    public void setDaysNotWorkedPaid(double d) { mdDaysNotWorkedPaid = d; }
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
    public void setFkRecruitmentSchemeTypeId(int n) { mnFkRecruitmentSchemeTypeId = n; }
    public void setFkPositionRiskTypeId(int n) { mnFkPositionRiskTypeId = n; }
    public void setFkWorkingDayTypeId(int n) { mnFkWorkingDayTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setAuxDateIssue(Date t) { mtAuxDateIssue = t; }
    public void setPayrollReceiptIssue(SDbPayrollReceiptIssue o) { moPayrollReceiptIssue = o; }

    public int getPkPayrollId() { return mnPkPayrollId; }
    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public Date getDateBenefits() { return mtDateBenefits; }
    public Date getDateLastHire() { return mtDateLastHire; }
    public Date getDateLastDismiss_n() { return mtDateLastDismiss_n; }
    public double getSalary() { return mdSalary; }
    public double getWage() { return mdWage; }
    public double getSalarySscBase() { return mdSalarySscBase; }
    public int getWorkingHoursDay() { return mnWorkingHoursDay; }
    public double getPaymentDaily() { return mdPaymentDaily; }
    public double getPaymentHourly() { return mdPaymentHourly; }
    public double getFactorCalendar() { return mdFactorCalendar; }
    public double getFactorDaysPaid() { return mdFactorDaysPaid; }
    public double getReceiptDays() { return mdReceiptDays; }
    public double getWorkingDays() { return mdWorkingDays; }
    public double getDaysWorked() { return mdDaysWorked; }
    public double getDaysHiredPayroll() { return mdDaysHiredPayroll; }
    public double getDaysHiredAnnual() { return mdDaysHiredAnnual; }
    public double getDaysIncapacityNotPaidPayroll() { return mdDaysIncapacityNotPaidPayroll; }
    public double getDaysIncapacityNotPaidAnnual() { return mdDaysIncapacityNotPaidAnnual; }
    public double getDaysNotWorkedPaid() { return mdDaysNotWorkedPaid; }
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
    public int getFkRecruitmentSchemeTypeId() { return mnFkRecruitmentSchemeTypeId; }
    public int getFkPositionRiskTypeId() { return mnFkPositionRiskTypeId; }
    public int getFkWorkingDayTypeId() { return mnFkWorkingDayTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public Date getAuxDateIssue() { return mtAuxDateIssue; }
    public SDbPayrollReceiptIssue getPayrollReceiptIssue() { return moPayrollReceiptIssue; }

    public ArrayList<SDbPayrollReceiptEarning> getChildPayrollReceiptEarnings() { return maChildPayrollReceiptEarnings; }
    public ArrayList<SDbPayrollReceiptDeduction> getChildPayrollReceiptDeductions() { return maChildPayrollReceiptDeductions; }
    public ArrayList<SDbAbsenceConsumption> getChildAbsenceConsumption() { return maChildAbsenceConsumptions; }
    
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
    
    public double getPaymentMonthly() {
        double paymentMonthly;
        
        if (mnFkPaymentTypeId == SModSysConsts.HRSS_TP_PAY_WEE) {
            paymentMonthly = SLibUtils.roundAmount(mdSalary * SHrsConsts.YEAR_DAYS);
        }
        else {
            paymentMonthly = mdWage;
        }
        
        return paymentMonthly;
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
        mtDateLastDismiss_n = null;
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
        mdDaysNotWorkedPaid = 0;
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
        mnFkRecruitmentSchemeTypeId = 0;
        mnFkPositionRiskTypeId = 0;
        mnFkWorkingDayTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        mtAuxDateIssue = null;

        //maChildPayrollReceiptDays = new ArrayList<>();  XXX jbarajas deleted by new control schema incidents
        maChildPayrollReceiptEarnings = new ArrayList<>();
        maChildPayrollReceiptDeductions = new ArrayList<>();
        maChildAbsenceConsumptions = new ArrayList<>();
        
        moPayrollReceiptIssue = null;
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
        Statement statement = null;
        ResultSet resultSet = null;
        SDbPayrollReceiptEarning payrollReceiptEarning = null;
        SDbPayrollReceiptDeduction payrollReceiptDeduction = null;
        SDbAbsenceConsumption absenceConsumption = null;

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
            mtDateLastDismiss_n = resultSet.getDate("dt_dis_n");
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
            mdDaysNotWorkedPaid = resultSet.getDouble("day_not_wrk_pad");
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
            mnFkRecruitmentSchemeTypeId = resultSet.getInt("fk_tp_rec_sche");
            mnFkPositionRiskTypeId = resultSet.getInt("fk_tp_pos_risk");
            mnFkWorkingDayTypeId = resultSet.getInt("fk_tp_work_day");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read payrollReceiptEarnings:

            msSql = "SELECT pre.id_pay, pre.id_emp, pre.id_mov " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON " +
                    "p.id_pay = pr.id_pay " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON " +
                    "pr.id_pay = pre.id_pay AND pr.id_emp = pre.id_emp " +
                    "WHERE pre.id_pay = " + mnPkPayrollId + " AND pre.id_emp = " + mnPkEmployeeId + " " +
                    "ORDER BY pre.id_pay, pre.id_emp, pre.id_mov; ";
            statement = session.getDatabase().getConnection().createStatement();
            
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {

                payrollReceiptEarning = new SDbPayrollReceiptEarning();
                payrollReceiptEarning.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
                maChildPayrollReceiptEarnings.add(payrollReceiptEarning);
            }

            // Read payrollReceiptDeductions:

            msSql = "SELECT prd.id_pay, prd.id_emp, prd.id_mov " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON " +
                    "p.id_pay = pr.id_pay " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON " +
                    "pr.id_pay = prd.id_pay AND pr.id_emp = prd.id_emp " +
                    "WHERE prd.id_pay = " + mnPkPayrollId + " AND prd.id_emp = " + mnPkEmployeeId + " " +
                    "ORDER BY prd.id_pay, prd.id_emp, prd.id_mov; ";
            statement = session.getDatabase().getConnection().createStatement();
            
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {

                payrollReceiptDeduction = new SDbPayrollReceiptDeduction();
                payrollReceiptDeduction.read(session, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
                maChildPayrollReceiptDeductions.add(payrollReceiptDeduction);
            }
            
            // Read absenceConsumption:

            msSql = "SELECT ac.id_emp, ac.id_abs, ac.id_cns " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON " +
                    "p.id_pay = pr.id_pay " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_ABS_CNS) + " AS ac ON " +
                    "pr.id_pay = ac.fk_rcp_pay AND pr.id_emp = ac.fk_rcp_emp " +
                    "WHERE ac.b_del = 0 AND ac.fk_rcp_pay = " + mnPkPayrollId + " AND ac.fk_rcp_emp = " + mnPkEmployeeId + " " +
                    "ORDER BY ac.id_emp, ac.id_abs, ac.id_cns; ";
            statement = session.getDatabase().getConnection().createStatement();
            
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {

                absenceConsumption = new SDbAbsenceConsumption();
                absenceConsumption.read(session, new int[] { resultSet.getInt("ac.id_emp"), resultSet.getInt("ac.id_abs"), resultSet.getInt("ac.id_cns") });
                maChildAbsenceConsumptions.add(absenceConsumption);
            }

            // Read Issue last:

            msSql = "SELECT id_iss "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " "
                    + "WHERE id_pay = " + mnPkPayrollId + " AND id_emp = " + mnPkEmployeeId + " "
                    + "ORDER BY id_iss DESC LIMIT 1";
            statement = session.getDatabase().getConnection().createStatement();
            
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                moPayrollReceiptIssue = new SDbPayrollReceiptIssue();
                moPayrollReceiptIssue.read(session, new int[] { mnPkPayrollId, mnPkEmployeeId, resultSet.getInt("id_iss") });
                mtAuxDateIssue = moPayrollReceiptIssue.getDateIssue();
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
                    (mtDateLastDismiss_n == null ? "null" :  "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLastDismiss_n) + "'") + ", " +
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
                    mdDaysNotWorkedPaid + ", " + 
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
                    mnFkRecruitmentSchemeTypeId + ", " + 
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
                    "dt_dis_n = " + (mtDateLastDismiss_n == null ? "null" :  "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLastDismiss_n) + "'") + ", " +
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
                    "day_not_wrk_pad = " + mdDaysNotWorkedPaid + ", " +
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
                    "fk_tp_rec_sche = " + mnFkRecruitmentSchemeTypeId + ", " +
                    "fk_tp_pos_risk = " + mnFkPositionRiskTypeId + ", " +
                    "fk_tp_work_day = " + mnFkWorkingDayTypeId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Delete registries dependent:
        
        deleteDependentRegistries(session, true, false);

        // Save payrollEarnings:

        for (SDbPayrollReceiptEarning payrollReceiptEarning : maChildPayrollReceiptEarnings) {
            payrollReceiptEarning.setRegistryNew(true);
            payrollReceiptEarning.setDeleted(mbDeleted);
            payrollReceiptEarning.setPkPayrollId(mnPkPayrollId);
            payrollReceiptEarning.setPkEmployeeId(mnPkEmployeeId);
            payrollReceiptEarning.save(session);
        }

        // Save payrollDeductions:

        for (SDbPayrollReceiptDeduction payrollReceiptDeduction : maChildPayrollReceiptDeductions) {
            payrollReceiptDeduction.setRegistryNew(true);
            payrollReceiptDeduction.setDeleted(mbDeleted);
            payrollReceiptDeduction.setPkPayrollId(mnPkPayrollId);
            payrollReceiptDeduction.setPkEmployeeId(mnPkEmployeeId);
            payrollReceiptDeduction.save(session);
        }
        
        // Save absenceConsumption:

        for (SDbAbsenceConsumption absenceConsumption : maChildAbsenceConsumptions) {
            absenceConsumption.setRegistryNew(true);
            absenceConsumption.setDeleted(mbDeleted);
            absenceConsumption.setFkReceiptPayrollId(mnPkPayrollId);
            absenceConsumption.setFkReceiptEmployeeId(mnPkEmployeeId);
            absenceConsumption.save(session);
        }

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
        registry.setDateLastDismiss_n(this.getDateLastDismiss_n());
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
        registry.setDaysNotWorkedPaid(this.getDaysNotWorkedPaid());
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
        registry.setFkRecruitmentSchemeTypeId(this.getFkRecruitmentSchemeTypeId());
        registry.setFkPositionRiskTypeId(this.getFkPositionRiskTypeId());
        registry.setFkWorkingDayTypeId(this.getFkWorkingDayTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        for (SDbPayrollReceiptEarning receiptEarning : this.getChildPayrollReceiptEarnings()) {
            registry.getChildPayrollReceiptEarnings().add(receiptEarning.clone());
        }

        for (SDbPayrollReceiptDeduction receiptDeduction : this.getChildPayrollReceiptDeductions()) {
            registry.getChildPayrollReceiptDeductions().add(receiptDeduction.clone());
        }
        
        for (SDbAbsenceConsumption receiptAbsenceConsumption : this.getChildAbsenceConsumption()) {
            registry.getChildAbsenceConsumption().add(receiptAbsenceConsumption.clone());
        }
        
        registry.setPayrollReceiptIssue(this.getPayrollReceiptIssue() == null ? null : this.getPayrollReceiptIssue().clone());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    @Override
    public boolean canDelete(SGuiSession session) throws SQLException, Exception {
        boolean can = super.canDelete(session);

        if (can && moPayrollReceiptIssue != null) {
            if (moPayrollReceiptIssue.isStamped()) {
                can = false;
                msQueryResult = "¡No es posible eliminar el recibo, está timbrado!";
            }
            else if (moPayrollReceiptIssue.isAnnul()) {
                can = false;
                msQueryResult = "¡No es posible eliminar el recibo, está anulado!";
            }
        }
        return can;
    }

    @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        mbDeleted = !mbDeleted;
        mnFkUserUpdateId = session.getUser().getPkUserId();

        msSql = "UPDATE " + getSqlTable() + " SET " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                "ts_usr_upd = NOW() " +
                getSqlWhere();
        session.getStatement().execute(msSql);

        if (!msQueryResult.isEmpty()) {
            throw new Exception(msQueryResult);
        }
        
        // Delete registries dependent:

        deleteDependentRegistries(session, false, true);

        mnQueryResultId = msQueryResult.isEmpty() ? SDbConsts.SAVE_OK : SDbConsts.SAVE_ERROR;
    }

    public static void checkDummyRegistry(final SGuiSession session, final int employeeId) throws Exception {
        String sql = "";
        ResultSet resultSet = null;
        SDbPayrollReceipt registryDummy = null;

        SDbPayroll.checkDummyRegistry(session);
        
        sql = "SELECT COUNT(*) "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " "
            + "WHERE id_pay = " + SLibConsts.UNDEFINED + " AND id_emp = " + employeeId;
        
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next() && resultSet.getInt(1) == 0) {
            // Create dbReceipt:

            registryDummy = new SDbPayrollReceipt();
            registryDummy.setPkPayrollId(SLibConsts.UNDEFINED);
            registryDummy.setPkEmployeeId(employeeId);
            registryDummy.setDateBenefits(SLibTimeUtils.createDate(2000, 1, 1));
            registryDummy.setDateLastHire(SLibTimeUtils.createDate(2000, 1, 1));
            registryDummy.setFkPaymentTypeId(1);
            registryDummy.setFkSalaryTypeId(SModSysConsts.HRSS_TP_SAL_FIX);
            registryDummy.setFkEmployeeTypeId(SModSysConsts.HRSU_TP_EMP_NON);
            registryDummy.setFkWorkerTypeId(SModSysConsts.HRSU_TP_WRK_NON);
            registryDummy.setFkMwzTypeId(SModSysConsts.HRSU_TP_MWZ_DEF);
            registryDummy.setFkDepartmentId(SModSysConsts.HRSU_DEP_NON);
            registryDummy.setFkPositionId(SModSysConsts.HRSU_POS_NON);
            registryDummy.setFkShiftId(SModSysConsts.HRSU_SHT_NON);
            registryDummy.setFkContractTypeId(SModSysConsts.HRSS_TP_CON_OTH);
            registryDummy.setFkRecruitmentSchemeTypeId(SModSysConsts.HRSS_TP_REC_SCHE_WAG);
            registryDummy.setFkPositionRiskTypeId(SModSysConsts.HRSS_TP_POS_RISK_CL1);
            registryDummy.setFkWorkingDayTypeId(SModSysConsts.HRSS_TP_WORK_DAY_DIU);

            registryDummy.save(session);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

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
 * @author Néstor Ávalos, Edwin Carmona, Sergio Flores
 */
public class SDbPayroll extends SDbRegistryUser {
    
    public static final int FIELD_CLOSE = FIELD_BASE + 1;
    public static final int FIELD_ACCOUNTING = FIELD_BASE + 2;
    public static final int FIELD_ACCOUNTING_GRADUAL = FIELD_BASE + 3;

    protected int mnPkPayrollId;
    protected int mnFiscalYear;
    protected int mnPeriodYear;
    protected int mnPeriod;
    protected int mnNumber;
    protected Date mtDateStart;
    protected Date mtDateEnd;
    protected int mnCalendarDays_r;
    protected int mnReceiptDays;
    protected int mnWorkingDays;
    protected double mdUmaAmount;
    protected double mdUmiAmount;
    protected double mdMwzWage;
    protected double mdMwzReferenceWage;
    protected String msHint;
    protected String msNotes;
    protected boolean mbTaxSubsidy;
    protected boolean mbSsContribution;
    protected boolean mbFortnightStandard;
    protected boolean mbAccounting;
    protected boolean mbAccountingGradual;
    protected boolean mbClosed;
    //protected boolean mbDeleted;
    protected int mnFkPaymentTypeId;
    protected int mnFkPaysheetTypeId;
    protected int mnFkPaysheetCustomTypeId;
    protected int mnFkRecruitmentSchemaTypeId;
    protected int mnFkMwzTypeId;
    protected int mnFkMwzReferenceTypeId;
    protected int mnFkTaxComputationTypeId;
    protected int mnFkTaxId;
    protected int mnFkTaxSubsidyId;
    protected int mnFkEmploymentSubsidyId_n;
    protected int mnFkSsContributionId;
    protected int mnFkUserClosedId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserClosed;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected boolean mbAuxIsDummy;
    protected int mnAuxFkUserCloseId;
    protected double mdAuxTotalEarnings;
    protected double mdAuxTotalDeductions;
    protected String msAuxPaymentType;

    protected ArrayList<SDbPayrollReceipt> maChildPayrollReceipts;
    protected ArrayList<SDbPayrollReceipt> maChildPayrollReceiptsToDelete;

    public SDbPayroll() {
        super(SModConsts.HRS_PAY);
    }

    /*
     * Private methods
     */
    
    private double readTotalEarnings(final SGuiSession session) throws Exception {
        double total = 0;
        ResultSet resultSet = null;

        msSql = "SELECT SUM(pe.amt_r) "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON "
            + "p.id_pay = pr.id_pay AND pr.b_del = 0 "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pe ON "
            + "pr.id_pay = pe.id_pay AND pr.id_emp = pe.id_emp AND pe.b_del = 0 "
            + "WHERE p.id_pay = " + mnPkPayrollId + " "
            + "GROUP BY p.id_pay";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            total = resultSet.getDouble(1);
        }

        return total;
    }

    private double readTotalDeductions(final SGuiSession session) throws Exception {
        double total = 0;
        ResultSet resultSet = null;

        msSql = "SELECT SUM(pd.amt_r) "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON "
            + "p.id_pay = pr.id_pay AND pr.b_del = 0 "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS pd ON "
            + "pr.id_pay = pd.id_pay AND pr.id_emp = pd.id_emp AND pd.b_del = 0 "
            + "WHERE p.id_pay = " + mnPkPayrollId + " "
            + "GROUP BY p.id_pay";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            total = resultSet.getDouble(1);
        }

        return total;
    }

    /*
     * Public methods
     */

    public void setPkPayrollId(int n) { mnPkPayrollId = n; }
    public void setFiscalYear(int n) { mnFiscalYear = n; }
    public void setPeriodYear(int n) { mnPeriodYear = n; }
    public void setPeriod(int n) { mnPeriod = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }
    public void setCalendarDays_r(int n) { mnCalendarDays_r = n; }
    public void setReceiptDays(int n) { mnReceiptDays = n; }
    public void setWorkingDays(int n) { mnWorkingDays = n; }
    public void setUmaAmount(double d) { mdUmaAmount = d; }
    public void setUmiAmount(double d) { mdUmiAmount = d; }
    public void setMwzWage(double d) { mdMwzWage = d; }
    public void setMwzReferenceWage(double d) { mdMwzReferenceWage = d; }
    public void setHint(String s) { msHint = s; }
    public void setNotes(String s) { msNotes = s; }
    public void setTaxSubsidy(boolean b) { mbTaxSubsidy = b; }
    public void setSsContribution(boolean b) { mbSsContribution = b; }
    public void setFortnightStandard(boolean b) { mbFortnightStandard = b; }
    public void setAccounting(boolean b) { mbAccounting = b; }
    public void setAccountingGradual(boolean b) { mbAccountingGradual = b; }
    public void setClosed(boolean b) { mbClosed = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkPaymentTypeId(int n) { mnFkPaymentTypeId = n; }
    public void setFkPaysheetTypeId(int n) { mnFkPaysheetTypeId = n; }
    public void setFkPaysheetCustomTypeId(int n) { mnFkPaysheetCustomTypeId = n; }
    public void setFkRecruitmentSchemaTypeId(int n) { mnFkRecruitmentSchemaTypeId = n; }
    public void setFkMwzTypeId(int n) { mnFkMwzTypeId = n; }
    public void setFkMwzReferenceTypeId(int n) { mnFkMwzReferenceTypeId = n; }
    public void setFkTaxComputationTypeId(int n) { mnFkTaxComputationTypeId = n; }
    public void setFkTaxId(int n) { mnFkTaxId = n; }
    public void setFkTaxSubsidyId(int n) { mnFkTaxSubsidyId = n; }
    public void setFkEmploymentSubsidyId_n(int n) { mnFkEmploymentSubsidyId_n = n; }
    public void setFkSsContributionId(int n) { mnFkSsContributionId = n; }
    public void setFkUserClosedId(int n) { mnFkUserClosedId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserClosed(Date t) { mtTsUserClosed = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkPayrollId() { return mnPkPayrollId; }
    public int getFiscalYear() { return mnFiscalYear; }
    public int getPeriodYear() { return mnPeriodYear; }
    public int getPeriod() { return mnPeriod; }
    public int getNumber() { return mnNumber; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public int getCalendarDays_r() { return mnCalendarDays_r; }
    public int getReceiptDays() { return mnReceiptDays; }
    public int getWorkingDays() { return mnWorkingDays; }
    public double getUmaAmount() { return mdUmaAmount; }
    public double getUmiAmount() { return mdUmiAmount; }
    public double getMwzWage() { return mdMwzWage; }
    public double getMwzReferenceWage() { return mdMwzReferenceWage; }
    public String getHint() { return msHint; }
    public String getNotes() { return msNotes; }
    public boolean isTaxSubsidy() { return mbTaxSubsidy; }
    public boolean isSsContribution() { return mbSsContribution; }
    public boolean isFortnightStandard() { return mbFortnightStandard; }
    public boolean isAccounting() { return mbAccounting; }
    public boolean isAccountingGradual() { return mbAccountingGradual; }
    public boolean isClosed() { return mbClosed; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkPaymentTypeId() { return mnFkPaymentTypeId; }
    public int getFkPaysheetTypeId() { return mnFkPaysheetTypeId; }
    public int getFkPaysheetCustomTypeId() { return mnFkPaysheetCustomTypeId; }
    public int getFkRecruitmentSchemaTypeId() { return mnFkRecruitmentSchemaTypeId; }
    public int getFkMwzTypeId() { return mnFkMwzTypeId; }
    public int getFkMwzReferenceTypeId() { return mnFkMwzReferenceTypeId; }
    public int getFkTaxComputationTypeId() { return mnFkTaxComputationTypeId; }
    public int getFkTaxId() { return mnFkTaxId; }
    public int getFkTaxSubsidyId() { return mnFkTaxSubsidyId; }
    public int getFkEmploymentSubsidyId_n() { return mnFkEmploymentSubsidyId_n; }
    public int getFkSsContributionId() { return mnFkSsContributionId; }
    public int getFkUserClosedId() { return mnFkUserClosedId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserClosed() { return mtTsUserClosed; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setAuxIsDummy(boolean b) { mbAuxIsDummy = b; }
    public void setAuxFkUserCloseId(int n) { mnAuxFkUserCloseId = n; }
    public void setAuxTotalEarnings(double d) { mdAuxTotalEarnings = d; }
    public void setAuxTotalDeductions(double d) { mdAuxTotalDeductions = d; }
    public void setAuxPaymentType(String s) { msAuxPaymentType = s; }

    public boolean isAuxIsDummy() { return mbAuxIsDummy; }
    public int getAuxFkUserCloseId() { return mnAuxFkUserCloseId; }
    public double getAuxTotalEarnings() { return mdAuxTotalEarnings; }
    public double getAuxTotalDeductions() { return mdAuxTotalDeductions; }
    public double getAuxTotalNet() { return mdAuxTotalEarnings - mdAuxTotalDeductions; }
    public String getAuxPaymentType() { return msAuxPaymentType; }
    
    public ArrayList<SDbPayrollReceipt> getChildPayrollReceipts() { return maChildPayrollReceipts; }
    public ArrayList<SDbPayrollReceipt> getChildPayrollReceiptsToDelete() { return maChildPayrollReceiptsToDelete; }
    
    public void clearPayrollForeingKeys() {
        mnFkPaymentTypeId = 0;
        mnFkPaysheetTypeId = 0;
        mnFkPaysheetCustomTypeId = 0;
        mnFkRecruitmentSchemaTypeId = 0;
        mnFkMwzTypeId = 0;
        mnFkMwzReferenceTypeId = 0;
        mnFkTaxComputationTypeId = 0;
        mnFkTaxId = 0;
        mnFkTaxSubsidyId = 0;
        mnFkEmploymentSubsidyId_n = 0;
        mnFkSsContributionId = 0;
    }
    
    public boolean isPayrollNormal() {
        return mnFkPaysheetTypeId == SModSysConsts.HRSS_TP_PAY_SHT_NOR;
    }
    
    public boolean isPayrollFortnightStandard() {
        return mnFkPaymentTypeId == SModSysConsts.HRSS_TP_PAY_FOR && mbFortnightStandard;
    }
    
    public boolean isReadOnly() { return mbClosed || mbDeleted; }

    /**
     * Compose name of payroll as payroll year and number number in format: 'yyyy' + " " + 'abbreviation of type of payment' + ". " + 'payroll number' + [", " + 'payroll notes'].
     */
    @Override
    public String getName() {
        return composePayrollYearAndNumber() + (msNotes.isEmpty() ? "" : ", " + msNotes);
    }
    
    public double getFactorCalendar() {
        return mnCalendarDays_r == 0 ? 0.0 : mnReceiptDays / (double) mnCalendarDays_r;
    }
    
    public double getFactorDaysPaid() {
        return mnWorkingDays == 0 ? 0.0 : mnReceiptDays / (double) mnWorkingDays;
    }
    
    /**
     * Get count of active (not deleted) payroll receipts.
     * @return Count of active (not deleted) payroll receipts.
     */
    public int getPayrollReceiptsActiveCount() {
        int count = 0;
        
        for (SDbPayrollReceipt pr : maChildPayrollReceipts) {
            if (!pr.isDeleted()) {
                count++;
            }
        }
        
        return count;
    }
    
    public SDbPayrollReceipt getChildPayrollReceipt(final int[] key) {
        SDbPayrollReceipt payrollReceipt = null;
        
        for (SDbPayrollReceipt pr : maChildPayrollReceipts) {
            if (SLibUtils.compareKeys(pr.getPrimaryKey(), key)) {
                payrollReceipt = pr;
                break;
            }
        }
        
        return payrollReceipt;
    }
    
    public SDbPayrollReceipt getChildPayrollReceipt(final int employeeId) {
        SDbPayrollReceipt payrollReceipt = null;
        
        for (SDbPayrollReceipt pr : maChildPayrollReceipts) {
            if (pr.getPkEmployeeId() == employeeId) {
                payrollReceipt = pr;
                break;
            }
        }
        
        return payrollReceipt;
    }
    
    /**
     * Create or update payroll receipt issues when payroll is closed.
     * @param session
     * @throws Exception 
     */
    public void updatePayrollReceiptIssuesOnClose(final SGuiSession session) throws Exception {
        for (SDbPayrollReceipt payrollReceipt : maChildPayrollReceipts) {
            payrollReceipt.updatePayrollReceiptIssue(session, mtDateEnd);
        }
    }
    
    /**
     * Update payroll receipt issues when payroll is opened.
     * @param session
     * @throws Exception 
     */
    public void updatePayrollReceiptIssuesOnOpen(final SGuiSession session) throws Exception {
        for (SDbPayrollReceipt payrollReceipt : maChildPayrollReceipts) {
            payrollReceipt.updatePayrollReceiptIssueReset(session);
        }
    }
    
    public String getPaymentType() {
        return SDbPayroll.getPaymentType(mnFkPaymentTypeId);
    }
    
    public String getPaymentTypeAbbr() {
        return SDbPayroll.getPaymentTypeAbbr(mnFkPaymentTypeId);
    }
    
    public static String getPaymentType(final int payrollTypeId) {
        String type = "";
        
        switch (payrollTypeId) {
            case SModSysConsts.HRSS_TP_PAY_WEE:
                type = SHrsFormerConsts.PAY_WEE;
                break;
            case SModSysConsts.HRSS_TP_PAY_FOR:
                type = SHrsFormerConsts.PAY_FOR;
                break;
            default:
                // nothing
        }
        
        return type;
    }

    public static String getPaymentTypeAbbr(final int payrollTypeId) {
        String typeAbbr = "";
        
        switch (payrollTypeId) {
            case SModSysConsts.HRSS_TP_PAY_WEE:
                typeAbbr = SHrsFormerConsts.PAY_WEE_ABB;
                break;
            case SModSysConsts.HRSS_TP_PAY_FOR:
                typeAbbr = SHrsFormerConsts.PAY_FOR_ABB;
                break;
            default:
                // nothing
        }
        
        return typeAbbr;
    }

    /**
     * Compose payroll number in format: 'abbreviation of type of payment' + ". " + 'payroll number'.
     */
    public String composePayrollNumber() {
        return getPaymentTypeAbbr(mnFkPaymentTypeId) + ". " + mnNumber;
    }

    /**
     * Compose payroll year and number number in format: 'yyyy' + " " + 'abbreviation of type of payment' + ". " + 'payroll number'.
     */
    public String composePayrollYearAndNumber() {
        return SLibUtils.DecimalFormatCalendarYear.format(mnPeriodYear) + " " + composePayrollNumber();
    }

    /**
     * Compose payroll period in format: 'yyyy' + "-" + 'mm'.
     */
    public String composePayrollPeriod() {
        return SLibUtils.DecimalFormatCalendarYear.format(mnPeriodYear) + "-" + SLibUtils.DecimalFormatCalendarMonth.format(mnPeriod);
    }

    /**
     * Compose payroll dates in format: 'dd/mm/yyyy' + "-" + 'dd/mm/yyyy'.
     */
    public String composePayrollDates() {
        return SLibUtils.DateFormatDate.format(mtDateStart) + "-" + SLibUtils.DateFormatDate.format(mtDateEnd);
    }

    public static String composePayrollYearAndNumber(final SGuiSession session, final int payrollId) throws Exception {
        String payrollNumber = "";
        
        String sql = "SELECT per_year, num, fk_tp_pay "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " "
                + "WHERE id_pay = " + payrollId + ";";
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            payrollNumber = resultSet.getInt("per_year") + " " + getPaymentTypeAbbr(resultSet.getInt("fk_tp_pay")) + " " + resultSet.getInt("num");
        }
        
        return payrollNumber;
    }

    public static String retrievePayrollHint(final SGuiSession session, final int payrollId) throws Exception {
        String payrollHint = "";
        
        String sql = "SELECT hint "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " "
                + "WHERE id_pay = " + payrollId + ";";
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            payrollHint = resultSet.getString("hint");
        }
        
        return payrollHint;
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPayrollId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPayrollId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mlTimeout = 1000 * 60 * 120; // 2 hrs

        mnPkPayrollId = 0;
        mnFiscalYear = 0;
        mnPeriodYear = 0;
        mnPeriod = 0;
        mnNumber = 0;
        mtDateStart = null;
        mtDateEnd = null;
        mnCalendarDays_r = 0;
        mnReceiptDays = 0;
        mnWorkingDays = 0;
        mdUmaAmount = 0;
        mdUmiAmount = 0;
        mdMwzWage = 0;
        mdMwzReferenceWage = 0;
        msHint = "";
        msNotes = "";
        mbTaxSubsidy = false;
        mbSsContribution = false;
        mbFortnightStandard = false;
        mbAccounting = false;
        mbAccountingGradual = false;
        mbClosed = false;
        mbDeleted = false;
        mnFkPaymentTypeId = 0;
        mnFkPaysheetTypeId = 0;
        mnFkPaysheetCustomTypeId = 0;
        mnFkRecruitmentSchemaTypeId = 0;
        mnFkMwzTypeId = 0;
        mnFkMwzReferenceTypeId = 0;
        mnFkTaxComputationTypeId = 0;
        mnFkTaxId = 0;
        mnFkTaxSubsidyId = 0;
        mnFkEmploymentSubsidyId_n = 0;
        mnFkSsContributionId = 0;
        mnFkUserClosedId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserClosed = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mbAuxIsDummy = false;
        mnAuxFkUserCloseId = 0;
        mdAuxTotalEarnings = 0;
        mdAuxTotalDeductions = 0;
        msAuxPaymentType = "";

        maChildPayrollReceipts = new ArrayList<>();
        maChildPayrollReceiptsToDelete = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_pay = " + mnPkPayrollId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_pay = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkPayrollId = 0;

        msSql = "SELECT COALESCE(MAX(id_pay), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkPayrollId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT p.*, t.name "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS t ON "
                + "p.fk_tp_pay = t.id_tp_pay "
                + "WHERE id_pay = " + pk[0] + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkPayrollId = resultSet.getInt("p.id_pay");
            mnFiscalYear = resultSet.getInt("p.fis_year");
            mnPeriodYear = resultSet.getInt("p.per_year");
            mnPeriod = resultSet.getInt("p.per");
            mnNumber = resultSet.getInt("p.num");
            mtDateStart = resultSet.getDate("p.dt_sta");
            mtDateEnd = resultSet.getDate("p.dt_end");
            mnCalendarDays_r = resultSet.getInt("cal_day_r");
            mnReceiptDays = resultSet.getInt("p.rcp_day");
            mnWorkingDays = resultSet.getInt("p.wrk_day");
            mdUmaAmount = resultSet.getDouble("p.uma_amt");
            mdUmiAmount = resultSet.getDouble("p.umi_amt");
            mdMwzWage = resultSet.getDouble("p.mwz_wage");
            mdMwzReferenceWage = resultSet.getDouble("p.mwz_ref_wage");
            msHint = resultSet.getString("hint");
            msNotes = resultSet.getString("p.nts");
            mbTaxSubsidy = resultSet.getBoolean("p.b_tax_sub");
            mbSsContribution = resultSet.getBoolean("p.b_ssc");
            mbFortnightStandard = resultSet.getBoolean("p.b_for_std");
            mbAccounting = resultSet.getBoolean("p.b_acc");
            mbAccountingGradual = resultSet.getBoolean("p.b_acc_grad");
            mbClosed = resultSet.getBoolean("p.b_clo");
            mbDeleted = resultSet.getBoolean("p.b_del");
            mnFkPaymentTypeId = resultSet.getInt("p.fk_tp_pay");
            mnFkPaysheetTypeId = resultSet.getInt("fk_tp_pay_sht");
            mnFkPaysheetCustomTypeId = resultSet.getInt("fk_tp_pay_sht_cus");
            mnFkRecruitmentSchemaTypeId = resultSet.getInt("fk_tp_rec_sche");
            mnFkMwzTypeId = resultSet.getInt("p.fk_tp_mwz");
            mnFkMwzReferenceTypeId = resultSet.getInt("p.fk_tp_mwz_ref");
            mnFkTaxComputationTypeId = resultSet.getInt("p.fk_tp_tax_comp");
            mnFkTaxId = resultSet.getInt("p.fk_tax");
            mnFkTaxSubsidyId = resultSet.getInt("p.fk_tax_sub");
            mnFkEmploymentSubsidyId_n = resultSet.getInt("fk_empl_sub_n");
            mnFkSsContributionId = resultSet.getInt("p.fk_ssc");
            mnFkUserClosedId = resultSet.getInt("p.fk_usr_clo");
            mnFkUserInsertId = resultSet.getInt("p.fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("p.fk_usr_upd");
            mtTsUserClosed = resultSet.getTimestamp("p.ts_usr_clo");
            mtTsUserInsert = resultSet.getTimestamp("p.ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("p.ts_usr_upd");

            msAuxPaymentType = resultSet.getString("t.name");

            mdAuxTotalEarnings = readTotalEarnings(session);
            mdAuxTotalDeductions = readTotalDeductions(session);

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_emp " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " " +
                    "WHERE id_pay = " + mnPkPayrollId + " " +
                    "ORDER BY id_emp;";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbPayrollReceipt payrollReceipt = new SDbPayrollReceipt();
                payrollReceipt.read(session, new int[] { mnPkPayrollId, resultSet.getInt(1) });
                maChildPayrollReceipts.add(payrollReceipt);
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
            if (!mbAuxIsDummy)  {
                computePrimaryKey(session);
            }
            mbUpdatable = true;
            mbDisableable = true;
            mbDeletable = true;
            mbDisabled = false;
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            mnFkUserClosedId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkPayrollId + ", " +
                    mnFiscalYear + ", " + 
                    mnPeriodYear + ", " +
                    mnPeriod + ", " +
                    mnNumber + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    mnCalendarDays_r + ", " + 
                    mnReceiptDays + ", " +
                    mnWorkingDays + ", " +
                    mdUmaAmount + ", " +
                    mdUmiAmount + ", " +
                    mdMwzWage + ", " +
                    mdMwzReferenceWage + ", " +
                    "'" + msHint + "', " + 
                    "'" + msNotes + "', " +
                    (mbTaxSubsidy ? 1 : 0) + ", " + 
                    (mbSsContribution ? 1 : 0) + ", " +
                    (mbFortnightStandard ? 1 : 0) + ", " + 
                    (mbAccounting ? 1 : 0) + ", " + 
                    (mbAccountingGradual ? 1 : 0) + ", " + 
                    (mbClosed ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkPaymentTypeId + ", " +
                    mnFkPaysheetTypeId + ", " +
                    mnFkPaysheetCustomTypeId + ", " +
                    mnFkRecruitmentSchemaTypeId + ", " + 
                    mnFkMwzTypeId + ", " +
                    mnFkMwzReferenceTypeId + ", " +
                    mnFkTaxComputationTypeId + ", " +
                    mnFkTaxId + ", " +
                    mnFkTaxSubsidyId + ", " +
                    (mnFkEmploymentSubsidyId_n == 0 ? "NULL" : "" + mnFkEmploymentSubsidyId_n) + ", " + 
                    mnFkSsContributionId + ", " +
                    mnFkUserClosedId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_pay = " + mnPkPayrollId + ", " +
                    "fis_year = " + mnFiscalYear + ", " +
                    "per_year = " + mnPeriodYear + ", " +
                    "per = " + mnPeriod + ", " +
                    "num = " + mnNumber + ", " +
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end = '" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    "cal_day_r = " + mnCalendarDays_r + ", " +
                    "rcp_day = " + mnReceiptDays + ", " +
                    "wrk_day = " + mnWorkingDays + ", " +
                    "uma_amt = " + mdUmaAmount + ", " +
                    "umi_amt = " + mdUmiAmount + ", " +
                    "mwz_wage = " + mdMwzWage + ", " +
                    "mwz_ref_wage = " + mdMwzReferenceWage + ", " +
                    "hint = '" + msHint + "', " +
                    "nts = '" + msNotes + "', " +
                    "b_tax_sub = " + (mbTaxSubsidy ? 1 : 0) + ", " +
                    "b_ssc = " + (mbSsContribution ? 1 : 0) + ", " +
                    "b_for_std = " + (mbFortnightStandard ? 1 : 0) + ", " +
                    "b_acc = " + (mbAccounting ? 1 : 0) + ", " +
                    "b_acc_grad = " + (mbAccountingGradual ? 1 : 0) + ", " +
                    "b_clo = " + (mbClosed ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_pay = " + mnFkPaymentTypeId + ", " +
                    "fk_tp_pay_sht = " + mnFkPaysheetTypeId + ", " +
                    "fk_tp_pay_sht_cus = " + mnFkPaysheetCustomTypeId + ", " +
                    "fk_tp_rec_sche = " + mnFkRecruitmentSchemaTypeId + ", " +
                    "fk_tp_mwz = " + mnFkMwzTypeId + ", " +
                    "fk_tp_mwz_ref = " + mnFkMwzReferenceTypeId + ", " +
                    "fk_tp_tax_comp = " + mnFkTaxComputationTypeId + ", " +
                    "fk_tax = " + mnFkTaxId + ", " +
                    "fk_tax_sub = " + mnFkTaxSubsidyId + ", " +
                    "fk_empl_sub_n = " + (mnFkEmploymentSubsidyId_n == 0 ? "NULL" : "" + mnFkEmploymentSubsidyId_n) + ", " +
                    "fk_ssc = " + mnFkSsContributionId + ", " +
                    "fk_usr_clo = " + mnFkUserClosedId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_clo = " + "NOW()" + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save payroll receips:

        for (SDbPayrollReceipt payrollReceipt : maChildPayrollReceiptsToDelete) {
            payrollReceipt.setDeleted(true);
            payrollReceipt.setPkPayrollId(mnPkPayrollId);
            payrollReceipt.save(session);
        }
        
        for (SDbPayrollReceipt payrollReceipt : maChildPayrollReceipts) {
            if (payrollReceipt.getChildPayrollReceiptIssue() == null || !payrollReceipt.getChildPayrollReceiptIssue().isCfdStamped()) {
                payrollReceipt.setRegistryNew(true);
                payrollReceipt.setPkPayrollId(mnPkPayrollId);
                payrollReceipt.save(session);
            }
        }

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbPayroll clone() throws CloneNotSupportedException {
        SDbPayroll registry = new SDbPayroll();

        registry.setPkPayrollId(this.getPkPayrollId());
        registry.setFiscalYear(this.getFiscalYear());
        registry.setPeriodYear(this.getPeriodYear());
        registry.setPeriod(this.getPeriod());
        registry.setNumber(this.getNumber());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd(this.getDateEnd());
        registry.setCalendarDays_r(this.getCalendarDays_r());
        registry.setReceiptDays(this.getReceiptDays());
        registry.setWorkingDays(this.getWorkingDays());
        registry.setUmaAmount(this.getUmaAmount());
        registry.setUmiAmount(this.getUmiAmount());
        registry.setMwzWage(this.getMwzWage());
        registry.setMwzReferenceWage(this.getMwzReferenceWage());
        registry.setHint(this.getHint());
        registry.setNotes(this.getNotes());
        registry.setTaxSubsidy(this.isTaxSubsidy());
        registry.setSsContribution(this.isSsContribution());
        registry.setFortnightStandard(this.isFortnightStandard());
        registry.setAccounting(this.isAccounting());
        registry.setAccountingGradual(this.isAccountingGradual());
        registry.setClosed(this.isClosed());
        registry.setDeleted(this.isDeleted());
        registry.setFkPaymentTypeId(this.getFkPaymentTypeId());
        registry.setFkPaysheetTypeId(this.getFkPaysheetTypeId());
        registry.setFkPaysheetCustomTypeId(this.getFkPaysheetCustomTypeId());
        registry.setFkRecruitmentSchemaTypeId(this.getFkRecruitmentSchemaTypeId());
        registry.setFkMwzTypeId(this.getFkMwzTypeId());
        registry.setFkMwzReferenceTypeId(this.getFkMwzReferenceTypeId());
        registry.setFkTaxComputationTypeId(this.getFkTaxComputationTypeId());
        registry.setFkTaxId(this.getFkTaxId());
        registry.setFkTaxSubsidyId(this.getFkTaxSubsidyId());
        registry.setFkEmploymentSubsidyId_n(this.getFkEmploymentSubsidyId_n());
        registry.setFkSsContributionId(this.getFkSsContributionId());
        registry.setFkUserClosedId(this.getFkUserClosedId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserClosed(this.getTsUserClosed());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setAuxPaymentType(this.getAuxPaymentType());
        registry.setAuxTotalEarnings(this.getAuxTotalEarnings());
        registry.setAuxTotalDeductions(this.getAuxTotalDeductions());

        for (SDbPayrollReceipt child : this.getChildPayrollReceipts()) {
            registry.getChildPayrollReceipts().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    @Override
    public boolean canDelete(SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);
        
        if (can && mbClosed) {
            can = false;
            msQueryResult = "¡No es posible eliminar nóminas cerradas!";
        }
        
        if (can) {
            for (SDbPayrollReceipt payrollReceipt : maChildPayrollReceipts) {
                can = payrollReceipt.canDelete(session);

                if (!can) {
                    msQueryResult = payrollReceipt.getQueryResult();
                    break;
                }
            }
        }
        
        if (can) {
            can = !mbDeleted;
            
            if (!can) {
                can = false;
                msQueryResult = "¡No es posible reactivar nóminas eliminadas!";
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

        if (!msQueryResult.isEmpty()) {
            throw new Exception(msQueryResult);
        }

        // Delete dependent registries:

        for (SDbPayrollReceipt payrollReceipt : maChildPayrollReceipts) {
            payrollReceipt.delete(session);
        }

        mnQueryResultId = msQueryResult.isEmpty() ? SDbConsts.SAVE_OK : SDbConsts.SAVE_ERROR;
    }

    @Override
    public void saveField(Statement statement, int[] pk, int field, Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";
        
        switch (field) {
            case FIELD_CLOSE:
                msSql += "b_clo = " + (Boolean) value + ", ";
                msSql += "fk_usr_clo = " + (mnAuxFkUserCloseId == 0 ? SUtilConsts.USR_NA_ID : mnAuxFkUserCloseId) + ", ";
                msSql += "ts_usr_clo = NOW() ";
                break;
            case FIELD_ACCOUNTING:
                msSql += "b_acc = " + (Boolean) value + " ";
                break;
            case FIELD_ACCOUNTING_GRADUAL:
                msSql += "b_acc_grad = " + (Boolean) value + " ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);
        
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    public static void checkDummyRegistry(final SGuiSession session) throws Exception {
        String sql = "SELECT COUNT(*) "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " "
            + "WHERE id_pay = 0;";
        
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next() && resultSet.getInt(1) == 0) {
            // Create dbReceipt:

            SDbPayroll dummyPayroll = new SDbPayroll();
            dummyPayroll.setAuxIsDummy(true);
            dummyPayroll.setPkPayrollId(0);
            dummyPayroll.setDateStart(SLibTimeUtils.createDate(2000, 1, 1));
            dummyPayroll.setDateEnd(SLibTimeUtils.createDate(2000, 1, 1));
            dummyPayroll.setDeleted(true);
            dummyPayroll.setFkPaymentTypeId(SModSysConsts.HRSS_TP_PAY_WEE);
            dummyPayroll.setFkPaysheetTypeId(SModSysConsts.HRSS_TP_PAY_SHT_NOR);
            dummyPayroll.setFkPaysheetCustomTypeId(SModSysConsts.HRSU_TP_PAY_SHT_CUS_DEF);
            dummyPayroll.setFkRecruitmentSchemaTypeId(SModSysConsts.HRSS_TP_REC_SCHE_NA);
            dummyPayroll.setFkMwzTypeId(SModSysConsts.HRSU_TP_MWZ_DEF);
            dummyPayroll.setFkMwzReferenceTypeId(SModSysConsts.HRSU_TP_MWZ_DEF);
            dummyPayroll.setFkTaxComputationTypeId(SModSysConsts.HRSS_TP_TAX_COMP_WOT);
            dummyPayroll.setFkTaxId(1);
            dummyPayroll.setFkTaxSubsidyId(1);
            dummyPayroll.setFkEmploymentSubsidyId_n(0);
            dummyPayroll.setFkSsContributionId(1);

            dummyPayroll.save(session);
        }
    }
}

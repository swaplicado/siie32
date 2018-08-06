/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos, Sergio Flores, Juan Barajas, Sergio Flores
 */
public class SHrsPayrollDataProvider implements SHrsDataProvider {

    private SGuiSession moSession;

    public SHrsPayrollDataProvider(SGuiSession session) {
        moSession = session;
    }

    /*
     * Private methods
     */

    private ArrayList<SDbLoanTypeAdjustment> getLoanTypeAdjustment() throws Exception {
        String sql = "";
        SDbLoanTypeAdjustment adjustment = null;
        ArrayList<SDbLoanTypeAdjustment> aAdjustments = new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        sql = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_TP_LOAN_ADJ) + " "
                + "WHERE b_del = 0 "
                + "ORDER BY dt_sta DESC, id_adj DESC ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            adjustment = new SDbLoanTypeAdjustment();
            adjustment.read(moSession, new int[] { resultSet.getInt("id_tp_loan"), resultSet.getInt("id_adj") });
            aAdjustments.add(adjustment);
        }

        return aAdjustments;
    }
    
    private ArrayList<SDbUma> readUmas() throws Exception {
        SDbUma uma = null;
        ArrayList<SDbUma> umas = new ArrayList<>();
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT id_uma "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_UMA) + " "
                + "ORDER BY dt_sta DESC, id_uma ";

        resultSet = moSession.getDatabase().getConnection().createStatement().executeQuery(sql);
        while (resultSet.next()) {
            uma = new SDbUma();
            uma.read(moSession, new int[] { resultSet.getInt("id_uma") });
            umas.add(uma);
        }

        return umas;
    }
    
    private ArrayList<SDbHoliday> getHolidays() throws Exception {
        String sql = "";
        SDbHoliday holiday = null;
        ArrayList<SDbHoliday> aHolidays = new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        sql = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_HOL) + " "
                + "WHERE b_del = 0 "
                + "ORDER BY dt, id_hol ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            holiday = new SDbHoliday();
            holiday.read(moSession, new int[] { resultSet.getInt("id_hdy"), resultSet.getInt("id_hol") });
            aHolidays.add(holiday);
        }

        return aHolidays;
    }

    private ArrayList<SDbTaxTable> getTaxTables() throws Exception {
        String sql = "";
        SDbTaxTable taxTable = null;
        ArrayList<SDbTaxTable> aTaxTables = new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        sql = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_TAX) + " "
                + "WHERE b_del = 0 "
                + "ORDER BY dt_sta, id_tax ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            taxTable = new SDbTaxTable();
            taxTable.read(moSession, new int[] { resultSet.getInt("id_tax") });
            aTaxTables.add(taxTable);
        }

        return aTaxTables;
    }

    private ArrayList<SDbTaxSubsidyTable> getTaxSubsidyTables() throws Exception {
        String sql = "";
        SDbTaxSubsidyTable taxSubsidyTable = null;
        ArrayList<SDbTaxSubsidyTable> aTaxSubsidyTables = new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        sql = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_TAX_SUB) + " "
                + "WHERE b_del = 0 "
                + "ORDER BY dt_sta, id_tax_sub ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            taxSubsidyTable = new SDbTaxSubsidyTable();
            taxSubsidyTable.read(moSession, new int[] { resultSet.getInt("id_tax_sub") });
            aTaxSubsidyTables.add(taxSubsidyTable);
        }

        return aTaxSubsidyTables;
    }

    private ArrayList<SDbSsContributionTable> getSsContributionTables() throws Exception {
        String sql = "";
        SDbSsContributionTable sSContributionTable = null;
        ArrayList<SDbSsContributionTable> aSsContributionTables = new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        sql = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_SSC) + " "
                + "WHERE b_del = 0 "
                + "ORDER BY dt_sta, id_ssc ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            sSContributionTable = new SDbSsContributionTable();
            sSContributionTable.read(moSession, new int[] { resultSet.getInt("id_ssc") });
            aSsContributionTables.add(sSContributionTable);
        }

        return aSsContributionTables;
    }
    
    private ArrayList<SDbBenefitTable> getBenefitTables() throws Exception {
        String sql = "";
        SDbBenefitTable benfitTables = null;
        ArrayList<SDbBenefitTable> aBenefitTables = new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        sql = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " "
                + "WHERE b_del = 0 "
                + "ORDER BY dt_sta DESC, id_ben ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            benfitTables = new SDbBenefitTable();
            benfitTables.read(moSession, new int[] { resultSet.getInt("id_ben") });
            aBenefitTables.add(benfitTables);
        }

        return aBenefitTables;
    }
    
    private ArrayList<SHrsBenefitTableAnniversary> getBenefitTableAnniversarys(ArrayList<SDbBenefitTable> benefitTables) throws Exception {
        return SHrsUtils.createBenefitTablesAnniversarys(benefitTables);
    }

    private ArrayList<SDbMwzTypeWage> getMwzTypeWages() throws Exception {
        String sql = "";
        SDbMwzTypeWage mwzTypeWage = null;
        ArrayList<SDbMwzTypeWage> aMwzTypeWages = new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        sql = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_MWZ_WAGE) + " "
                + "WHERE b_del = 0 "
                + "ORDER BY dt_sta, id_tp_mwz, id_wage ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            mwzTypeWage = new SDbMwzTypeWage();
            mwzTypeWage.read(moSession, new int[] { resultSet.getInt("id_tp_mwz"), resultSet.getInt("id_wage") });
            aMwzTypeWages.add(mwzTypeWage);
        }

        return aMwzTypeWages;
    }

    private ArrayList<SDbEarning> getEarnings() throws Exception {
        String sql = "";
        SDbEarning earning = null;
        ArrayList<SDbEarning> aEarnings = new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        sql = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " "
                + "WHERE b_del = 0 "
                + "ORDER BY CONCAT(code, ' - ', name), id_ear ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            earning = new SDbEarning();
            earning.read(moSession, new int[] { resultSet.getInt("id_ear") });
            aEarnings.add(earning);
        }

        return aEarnings;
    }

    private ArrayList<SDbDeduction> getDeductions() throws Exception {
        String sql = "";
        SDbDeduction deduction = null;
        ArrayList<SDbDeduction> aDeductions = new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        sql = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " "
                + "WHERE b_del = 0 "
                + "ORDER BY CONCAT(code, ' - ', name), id_ded ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            deduction = new SDbDeduction();
            deduction.read(moSession, new int[] { resultSet.getInt("id_ded") });
            aDeductions.add(deduction);
        }

        return aDeductions;
    }

    private ArrayList<SDbAutomaticEarning> getAutomaticEarnings(final int paymentType) throws Exception {
        String sql = "";
        SDbAutomaticEarning automaticEarning = null;
        ArrayList<SDbAutomaticEarning> aAutomaticEarnings = new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        // Global automatic earnings:

        sql = "SELECT id_ear, id_aut "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_EAR) + " "
                + "WHERE b_del = 0 AND fk_emp_n IS NULL "
                + "ORDER BY id_ear, id_aut ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            automaticEarning = new SDbAutomaticEarning();
            automaticEarning.read(moSession, new int[] { resultSet.getInt("id_ear"), resultSet.getInt("id_aut") });
            aAutomaticEarnings.add(automaticEarning);
        }

        // Employee automatic earnings:

        sql = "SELECT ae.id_ear, ae.id_aut "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_EAR) + " AS ae "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON "
                + "ae.fk_emp_n = e.id_emp "
                + "WHERE ae.b_del = 0 AND e.fk_tp_pay = " + paymentType + " "
                + "ORDER BY ae.id_ear, ae.id_aut ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            automaticEarning = new SDbAutomaticEarning();
            automaticEarning.read(moSession, new int[] { resultSet.getInt("ae.id_ear"), resultSet.getInt("ae.id_aut") });
            aAutomaticEarnings.add(automaticEarning);
        }

        return aAutomaticEarnings;
    }

    private ArrayList<SDbAutomaticDeduction> getAutomaticDeductions(final int paymentType) throws Exception {
        String sql = "";
        SDbAutomaticDeduction automaticDeduction = null;
        ArrayList<SDbAutomaticDeduction> aAutomaticDeductions = new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        // Global automatic deduction:

        sql = "SELECT id_ded, id_aut "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_DED) + " "
                + "WHERE b_del = 0 AND fk_emp_n IS NULL "
                + "ORDER BY id_ded, id_aut ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            automaticDeduction = new SDbAutomaticDeduction();
            automaticDeduction.read(moSession, new int[] { resultSet.getInt("id_ded"), resultSet.getInt("id_aut") });
            aAutomaticDeductions.add(automaticDeduction);
        }

        // Employee automatic deduction:

        sql = "SELECT ad.id_ded, ad.id_aut "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_DED) + " AS ad "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON "
                + "ad.fk_emp_n = e.id_emp "
                + "WHERE ad.b_del = 0 AND e.fk_tp_pay = " + paymentType + " "
                + "ORDER BY ad.id_ded, ad.id_aut ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            automaticDeduction = new SDbAutomaticDeduction();
            automaticDeduction.read(moSession, new int[] { resultSet.getInt("ad.id_ded"), resultSet.getInt("ad.id_aut") });
            aAutomaticDeductions.add(automaticDeduction);
        }

        return aAutomaticDeductions;
    }

    private String getEmployeesByPayroll(final int payrollId) throws Exception {
        String sql = "";
        String employeeIds = "";
        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        sql = "SELECT pr.id_emp " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON " +
                "p.id_pay = pr.id_pay " +
                "WHERE pr.b_del = 0 AND p.id_pay = " + payrollId + " ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            employeeIds += ((employeeIds.length() == 0 ? "" : ", ") + resultSet.getInt("pr.id_emp"));
        }

        return employeeIds;
    }
    
    private ArrayList<SDbEmployee> getEmployees(final int paymentType, final int payrollId) throws Exception {
        String sql = "";
        String employeeIds = "";
        SDbEmployee employee = null;
        ArrayList<SDbEmployee> aEmployees = new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();
        
        if (payrollId != SLibConsts.UNDEFINED) {
            employeeIds = getEmployeesByPayroll(payrollId);
        }

        sql = "SELECT * "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " "
                + "WHERE b_del = 0 AND (fk_tp_pay = " + paymentType + (employeeIds.isEmpty() ? "" : " OR id_emp IN(" + employeeIds + ")") + ") "
                + "ORDER BY id_emp ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            employee = new SDbEmployee();
            employee.read(moSession, new int[] { resultSet.getInt("id_emp") });
            aEmployees.add(employee);
        }

        return aEmployees;
    }

    private ArrayList<SHrsPayrollReceipt> getHrsPayrollReceipts(final SHrsPayroll hrsPayroll, final int payrollId, final boolean isCopy, final boolean isNew) throws Exception {
        String sql = "";
        SDbPayrollReceipt payrollReceipt = null;

        SHrsEmployee hrsEmployee = null;
        SHrsPayrollReceipt hrsPayrollReceipt = null;
        SHrsPayrollReceiptEarning hrsPayrollReceiptEarning = null;
        SHrsPayrollReceiptDeduction hrsPayrollReceiptDeduction = null;

        ArrayList<SHrsPayrollReceipt> aPayrollReceipts = new ArrayList<>();

        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        if (!isNew) {
            sql = "SELECT pr.id_pay, pr.id_emp " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON " +
                    "p.id_pay = pr.id_pay " +
                    "WHERE pr.b_del = 0 AND p.id_pay = " + payrollId + " ";

            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                payrollReceipt = new SDbPayrollReceipt();
                payrollReceipt.read(moSession, new int[] { resultSet.getInt("pr.id_pay"), resultSet.getInt("pr.id_emp") });

                hrsPayrollReceipt = new SHrsPayrollReceipt();
                hrsPayrollReceipt.setHrsPayroll(hrsPayroll);

                // Obtain payrollReceiptEarning:

                for (SDbPayrollReceiptEarning payrollReceiptEarning : payrollReceipt.getChildPayrollReceiptEarnings()) {
                    hrsPayrollReceiptEarning = new SHrsPayrollReceiptEarning();

                    /* XXX (jbarajas, 2016-04-01) slowly open payroll
                    earning = new SDbEarning();
                    earning.read(moSession, new int[] { payrollReceiptEarning.getFkEarningId() });
                    */

                    hrsPayrollReceiptEarning.setPkMoveId(payrollReceiptEarning.getPkMoveId());
                    //hrsPayrollReceiptEarning.setEarning(earning); XXX (jbarajas, 2016-04-01) slowly open payroll
                    hrsPayrollReceiptEarning.setEarning(hrsPayroll.getDataEarning(payrollReceiptEarning.getFkEarningId()));
                    hrsPayrollReceiptEarning.setReceiptEarning(payrollReceiptEarning);
                    hrsPayrollReceiptEarning.setHrsReceipt(hrsPayrollReceipt);

                    hrsPayrollReceipt.getHrsEarnings().add(hrsPayrollReceiptEarning);
                }

                // Obtain payrollReceiptDeduction:

                for (SDbPayrollReceiptDeduction payrollReceiptDeduction : payrollReceipt.getChildPayrollReceiptDeductions()) {
                    hrsPayrollReceiptDeduction = new SHrsPayrollReceiptDeduction();

                    /* XXX (jbarajas, 2016-04-01) slowly open payroll
                    deduction = new SDbDeduction();
                    deduction.read(moSession, new int[] { payrollReceiptDeduction.getFkDeductionId() });
                    */

                    hrsPayrollReceiptDeduction.setPkMoveId(payrollReceiptDeduction.getPkMoveId());
                    //hrsPayrollReceiptDeduction.setDeduction(deduction); XXX (jbarajas, 2016-04-01) slowly open payroll
                    hrsPayrollReceiptDeduction.setDeduction(hrsPayroll.getDataDeduction(payrollReceiptDeduction.getFkDeductionId()));
                    hrsPayrollReceiptDeduction.setReceiptDeduction(payrollReceiptDeduction);
                    hrsPayrollReceiptDeduction.setHrsReceipt(hrsPayrollReceipt);

                    hrsPayrollReceipt.getHrsDeductions().add(hrsPayrollReceiptDeduction);
                }

                // Obtain absenceConsumption:

                if (!payrollReceipt.getChildAbsenceConsumption().isEmpty()) {
                    hrsPayrollReceipt.getAbsenceConsumptions().addAll(payrollReceipt.getChildAbsenceConsumption());
                }

                // XXX (jbarajas, 2016-04-01) slowly open payroll
                hrsEmployee = createHrsEmployee(hrsPayroll, isCopy ? SLibConsts.UNDEFINED : hrsPayroll.getPayroll().getPkPayrollId(), payrollReceipt.getPkEmployeeId(), hrsPayroll.getPayroll().getPeriodYear(), hrsPayroll.getPayroll().getPeriod(), hrsPayroll.getPayroll().getFiscalYear(),
                        hrsPayroll.getPayroll().getDateStart(), hrsPayroll.getPayroll().getDateEnd(), hrsPayroll.getPayroll().getFkTaxComputationTypeId());
                hrsEmployee.setHrsPayrollReceipt(hrsPayrollReceipt);
                hrsPayrollReceipt.setHrsEmployee(hrsEmployee);
                hrsPayrollReceipt.setReceipt(payrollReceipt);
                //hrsPayrollReceipt.setReceipt(hrsPayroll.createPayrollReceipt(hrsEmployee)); XXX (jbarajas, 2016-08-16) slowly open payroll
                hrsPayrollReceipt.getReceipt().setRegistryNew(payrollReceipt.isRegistryNew());
                hrsPayrollReceipt.getReceipt().setPayrollReceiptIssue(payrollReceipt.getPayrollReceiptIssues());
                /* XXX (jbarajas, 2015-10-07) remove by new table
                hrsPayrollReceipt.getReceipt().setDbmsDataCfd(payrollReceipt.getDbmsDataCfd());
                hrsPayrollReceipt.getReceipt().setNumberSeries(payrollReceipt.getNumberSeries());
                hrsPayrollReceipt.getReceipt().setNumber(payrollReceipt.getNumber());
                hrsPayrollReceipt.getReceipt().setDateIssue(payrollReceipt.getDateIssue());
                hrsPayrollReceipt.getReceipt().setDatePayment(payrollReceipt.getDatePayment());
                hrsPayrollReceipt.getReceipt().setFkPaymentSystemTypeId(payrollReceipt.getFkPaymentSystemTypeId());
                */
                //hrsPayrollReceipt.computeDbPayrollReceiptDays(); XXX (jbarajas, 2016-08-16) slowly open payroll
                //hrsPayrollReceipt.computeDbPayrollReceipt();  XXX jbarajas 2015-05-11

                aPayrollReceipts.add(hrsPayrollReceipt);
            }
        }

        return aPayrollReceipts;
    }

    public boolean isLastPayrollForCredits(final int payrollId, final int payrollYear, final int payrollPeriod, final int paymentPeriod) throws Exception {
        boolean isLast = false;
        String sql = "";
        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();
        
        sql = "SELECT COUNT(*) AS f_count " +
                "FROM hrs_pay AS p " +
                "INNER JOIN hrs_pay_rcp AS r ON r.id_pay = p.id_pay " +
                "WHERE p.b_del = 0 AND r.b_del = 0 AND p.id_pay <> " + payrollId + " AND p.per_year = " + payrollYear + " AND " +
                "p.per = " + payrollPeriod + " AND p.fk_tp_pay = " + paymentPeriod + " ";
        
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            isLast = paymentPeriod== SModSysConsts.HRSS_TP_PAY_WEE ? resultSet.getInt("f_count") == 3 : resultSet.getInt("f_count") == 1;
        }
        return isLast;
    }
    
    private ArrayList<SDbLoan> getEmployeeLoans(final int employeeId) throws Exception {
        ArrayList<SDbLoan> aLoans = new ArrayList<>();
        SDbLoan loan = null;
        String sql = "";
        ResultSet resultSet = null;
        Statement statement = null;
        
        statement = moSession.getDatabase().getConnection().createStatement();

        sql = "SELECT id_emp, id_loan " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_LOAN) + " " +
            "WHERE b_del = 0 AND id_emp = " + employeeId + " AND b_clo = 0;";
        
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            loan = new SDbLoan();
            loan.read(moSession, new int[] { resultSet.getInt("id_emp"), resultSet.getInt("id_loan") });
            aLoans.add(loan);
        }

        return aLoans;
    }
    
    private ArrayList<SHrsLoanPayments> getEmployeeLoanPayments(final ArrayList<SDbLoan> loans, final int payrollId, final int payrollYear, final int payrollPeriod) throws Exception {
        ArrayList<SHrsLoanPayments> aLoanPayments = new ArrayList<>();
        SHrsLoanPayments loanPayments = null;
        SDbPayrollReceiptEarning receiptEarning = null;
        SDbPayrollReceiptDeduction receiptDeduction = null;
        String sql = "";
        ResultSet resultSet = null;
        Statement statement = null;
        double amount = 0;
        double days = 0;
        
        statement = moSession.getDatabase().getConnection().createStatement();

        for (SDbLoan loan : loans) {
            loanPayments = new SHrsLoanPayments();
            
            sql = "SELECT rcp_ear.id_pay, rcp_ear.id_emp, rcp_ear.id_mov "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS rcp ON rcp.id_pay = p.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS rcp_ear ON rcp_ear.id_pay = rcp.id_pay AND rcp_ear.id_emp = rcp.id_emp "
                + "WHERE (p.id_pay = 0 OR p.b_del = 0) AND rcp.b_del = 0 AND rcp_ear.b_del = 0 " + (payrollId == 0 ? "" : "AND p.id_pay <> " + payrollId) + " AND rcp_ear.fk_loan_emp_n = " + loan.getPkEmployeeId() + " AND rcp_ear.fk_loan_loan_n = " + loan.getPkLoanId() + " ";

            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                receiptEarning = new SDbPayrollReceiptEarning();
                receiptEarning.read(moSession, new int[] { resultSet.getInt("rcp_ear.id_pay"), resultSet.getInt("rcp_ear.id_emp"), resultSet.getInt("rcp_ear.id_mov") });
                
                loanPayments.getReceiptEarnings().add(receiptEarning);
            }
            
            sql = "SELECT rcp_ded.id_pay, rcp_ded.id_emp, rcp_ded.id_mov "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS rcp ON rcp.id_pay = p.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS rcp_ded ON rcp_ded.id_pay = rcp.id_pay AND rcp_ded.id_emp = rcp.id_emp "
                + "WHERE (p.id_pay = 0 OR p.b_del = 0) AND rcp.b_del = 0 AND rcp_ded.b_del = 0 " + (payrollId == 0 ? "" : "AND p.id_pay <> " + payrollId) + " AND rcp_ded.fk_loan_emp_n = " + loan.getPkEmployeeId() + " AND rcp_ded.fk_loan_loan_n = " + loan.getPkLoanId() + " ";

            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                receiptDeduction = new SDbPayrollReceiptDeduction();
                receiptDeduction.read(moSession, new int[] { resultSet.getInt("rcp_ded.id_pay"), resultSet.getInt("rcp_ded.id_emp"), resultSet.getInt("rcp_ded.id_mov") });
                
                loanPayments.getReceiptDeductions().add(receiptDeduction);
            }
            
            sql = "SELECT COALESCE(SUM(rd.amt_r), 0) AS f_amt " +
                "FROM hrs_pay AS p " +
                "INNER JOIN hrs_pay_rcp AS r ON r.id_pay = p.id_pay " +
                "INNER JOIN hrs_pay_rcp_ded AS rd ON rd.id_pay = r.id_pay AND rd.id_emp = r.id_emp " +
                "WHERE p.b_del = 0 AND r.b_del = 0 AND rd.b_del = 0 AND p.id_pay <> " + payrollId + " AND p.per_year = " + payrollYear + " AND p.per = " + payrollPeriod + " AND r.id_emp = " + loan.getPkEmployeeId() + " AND " +
                "rd.fk_loan_emp_n = " + loan.getPkEmployeeId() + " AND rd.fk_loan_loan_n = " + loan.getPkLoanId() + " AND rd.fk_tp_loan_n = " + loan.getFkLoanTypeId() + " ";
        
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                amount = resultSet.getDouble("f_amt");
            }
            
            sql = "SELECT SUM(r.day_hire_pay - r.day_inc_not_pad_pay - r.day_not_wrk_not_pad) AS f_days " +
                "FROM hrs_pay AS p " +
                "INNER JOIN hrs_pay_rcp AS r ON r.id_pay = p.id_pay " +
                "WHERE p.b_del = 0 AND r.b_del = 0 AND p.id_pay <> " + payrollId + " AND p.per_year = " + payrollYear + " AND " +
                "p.per = " + payrollPeriod + " AND r.id_emp = " + loan.getPkEmployeeId() + " ";
        
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                days = resultSet.getDouble("f_days");
            }
            
            if (!loanPayments.getReceiptDeductions().isEmpty()) {
                loanPayments.setLoan(loan);
                loanPayments.setAmountPeriod(amount);
                loanPayments.setDaysPeriod(days);
                aLoanPayments.add(loanPayments);
            }
        }

        return aLoanPayments;
    }

    private ArrayList<SDbAbsence> getEmployeeAbsences(final int employeeId) throws Exception {
        ArrayList<SDbAbsence> aAbsences = new ArrayList<>();
        SDbAbsence absence = null;
        String sql = "";
        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        sql = "SELECT id_emp, id_abs, eff_day " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ABS) + " " +
            "WHERE b_del = 0 AND b_clo = 0 AND id_emp = " + employeeId + " " +
            "ORDER BY dt_sta, id_emp, id_abs ";

        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            absence = new SDbAbsence();
            absence.read(moSession, new int[] { resultSet.getInt("id_emp"), resultSet.getInt("id_abs") });
            aAbsences.add(absence);
        }
        
        return aAbsences;
    }
    
    private ArrayList<SDbAbsenceConsumption> getEmployeeAbsencesConsumption(final ArrayList<SDbAbsence> aAbsences, final int payrollId) throws Exception {
        ArrayList<SDbAbsenceConsumption> aAbsencesConsumptions = new ArrayList<>();
        SDbAbsenceConsumption absenceConsumption = null;
        String sql = "";
        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        for (SDbAbsence absence : aAbsences) {
            sql = "SELECT id_cns " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ABS_CNS) + " " +
            "WHERE b_del = 0 AND id_emp = " + absence.getPkEmployeeId() + " AND id_abs = " + absence.getPkAbsenceId() + " AND fk_rcp_pay <> " + payrollId + " " +
            "ORDER BY id_emp, id_abs, id_cns ";

            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                absenceConsumption = new SDbAbsenceConsumption();
                absenceConsumption.read(moSession, new int[] { absence.getPkEmployeeId(), absence.getPkAbsenceId(), resultSet.getInt("id_cns") });
                aAbsencesConsumptions.add(absenceConsumption);
            }
        }
        
        return aAbsencesConsumptions;
    }

    private ArrayList<SDbEmployeeHireLog> getEmployeeHireLogs(final int employeeId, final Date dateStart, final Date dateEnd)  throws Exception {
        return SHrsUtils.getEmployeeHireLogs(moSession, employeeId, dateStart, dateEnd);
    }
    
    private int getEmployeeHireDays(final ArrayList<SDbEmployeeHireLog> aEmployeeHireLogs, final Date dateStart, final Date dateEnd) throws Exception {
        int daysHired = 0;
        
        for (SDbEmployeeHireLog hireLog : aEmployeeHireLogs) {
            if (hireLog.getDateHire().compareTo(dateStart) <= 0) {
                daysHired += SLibTimeUtils.getDaysDiff(hireLog.getDateDismissed_n() == null ? dateEnd : hireLog.getDateDismissed_n().compareTo(dateEnd) >= 0 ? dateEnd : hireLog.getDateDismissed_n(), dateStart) + 1;
            }
            else if (hireLog.getDateHire().compareTo(dateStart) >= 0) {
                daysHired += SLibTimeUtils.getDaysDiff(hireLog.getDateDismissed_n() == null ? dateEnd : hireLog.getDateDismissed_n().compareTo(dateEnd) >= 0 ? dateEnd : hireLog.getDateDismissed_n(), hireLog.getDateHire()) + 1;
            }
        }
        
        return daysHired;
    }
    
    public int getEmployeeBusinessDays(final ArrayList<SDbEmployeeHireLog> aEmployeeHireLogs, final int paymentType, final Date dateStart, final Date dateEnd) throws Exception {
        int businessdays = 0;
        ArrayList<SDbHoliday> aHolidays = null;
        SDbWorkingDaySettings workingDaySettings = null;
        
        workingDaySettings = SHrsUtils.getPayrollWorkingDaySettings(moSession, paymentType);
        aHolidays = getHolidays();
        
        for (SDbEmployeeHireLog hireLog : aEmployeeHireLogs) {
            businessdays += SHrsUtils.getEmployeeBusinessDays(hireLog.getDateHire().compareTo(dateStart) <= 0 ? dateStart : hireLog.getDateHire(), hireLog.getDateDismissed_n() == null ? dateEnd : hireLog.getDateDismissed_n().compareTo(dateEnd) >= 0 ? dateEnd : hireLog.getDateDismissed_n(), aHolidays, workingDaySettings);
        }
        
        return businessdays;
    }

    private double getEmployeeAccumulatedTaxableEarnings(final int payrollId, final int employeeId, final int periodYear, final Date dateEnd) throws Exception {
        double accumulatedTaxableEarnings = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT SUM(pre.amt_taxa) AS f_taxa " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON " +
            "p.id_pay = pr.id_pay " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON " +
            "pr.id_pay = pre.id_pay AND pr.id_emp = pre.id_emp " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS ear ON " + // XXX (jbarajas, 2016-04-06) articule 174 RLISR
            "ear.id_ear = pre.fk_ear " +
            "WHERE p.b_del = 0 AND pr.b_del = 0 AND pre.b_del = 0 AND p.id_pay <> " + payrollId + " AND pr.id_emp = " + employeeId + " AND  " +
            "p.fis_year = " + periodYear + " AND p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
            "AND pre.b_alt_tax = 0 " + // XXX (jbarajas, 2016-04-06) articule 174 RLISR
            "GROUP BY pr.id_emp " +
            "ORDER BY pr.id_emp ";
        resultSet = moSession.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            accumulatedTaxableEarnings = resultSet.getDouble("f_taxa");
        }

        return accumulatedTaxableEarnings;
    }
    
    // XXX (jbarajas, 2016-04-06) articule 174 RLISR
    private double getEmployeeAccumulatedTaxableEarningsAlt(final int payrollId, final int employeeId, final int periodYear, final Date dateEnd) throws Exception {
        double accumulatedTaxableEarnings = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT SUM(pre.amt_taxa) AS f_taxa " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON " +
            "p.id_pay = pr.id_pay " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON " +
            "pr.id_pay = pre.id_pay AND pr.id_emp = pre.id_emp " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS ear ON " +
            "ear.id_ear = pre.fk_ear " +
            "WHERE p.b_del = 0 AND pr.b_del = 0 AND pre.b_del = 0 AND p.id_pay <> " + payrollId + " AND pr.id_emp = " + employeeId + " AND  " +
            "p.fis_year = " + periodYear + " AND p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' " +
            "AND pre.b_alt_tax = 1 " +
            "GROUP BY pr.id_emp " +
            "ORDER BY pr.id_emp ";
        resultSet = moSession.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            accumulatedTaxableEarnings = resultSet.getDouble("f_taxa");
        }

        return accumulatedTaxableEarnings;
    }
    
    private ArrayList<SHrsAccumulatedEarning> getEmployeeAccumulatedEarnings(final int payrollId, final int employeeId, final int periodYear, final Date dateStart, final Date dateEnd,
            final int taxComputationType, final boolean byType) throws Exception {
        ArrayList<SHrsAccumulatedEarning> aAccumulatedEarning = new ArrayList<>();
        SHrsAccumulatedEarning accumulatedEarning = null;
        String sql = "";

        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        sql = "SELECT pre.id_pay, pre.id_emp, p.per_year, " + (!byType ? "pre.fk_ear" : "pre.fk_tp_ear") + " AS f_ear, " +
            "SUM(pre.amt_r) AS f_amount, SUM(pre.amt_exem) AS f_exem, SUM(pre.amt_taxa) AS f_taxa " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON " +
            "p.id_pay = pr.id_pay " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON " +
            "pr.id_pay = pre.id_pay AND pr.id_emp = pre.id_emp " +
            "WHERE p.b_del = 0 AND pr.b_del = 0 AND pre.b_del = 0 AND p.id_pay <> " + payrollId + " AND pr.id_emp = " + employeeId + " AND  " +
            "p.per_year = " + periodYear + " AND " + ((taxComputationType == SModSysConsts.HRSS_TP_TAX_COMP_ANN ||
                taxComputationType == SLibConsts.UNDEFINED) ?
                "p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "'" :
                "p.dt_sta >= '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND " +
                "p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "'") + " " +
            "GROUP BY " + (!byType ? "pre.fk_ear" : "pre.fk_tp_ear") + " " +
            "ORDER BY " + (!byType ? "pre.fk_ear" : "pre.fk_tp_ear") + " ";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            accumulatedEarning = new SHrsAccumulatedEarning(resultSet.getInt("f_ear"), resultSet.getDouble("f_amount"), resultSet.getDouble("f_taxa"));
            aAccumulatedEarning.add(accumulatedEarning);
        }

        return aAccumulatedEarning;
    }

    private ArrayList<SHrsAccumulatedDeduction> getEmployeeAccumulatedDeductions(final int payrollId, final int employeeId, final int periodYear, final Date dateStart, final Date dateEnd, final int taxComputationType, final boolean byType) throws Exception {
        ArrayList<SHrsAccumulatedDeduction> aAccumulatedDeductions = new ArrayList<>();
        SHrsAccumulatedDeduction accumulatedDeduction = null;
        String sql = "";

        ResultSet resultSet = null;
        Statement statement = moSession.getDatabase().getConnection().createStatement();

        sql = "SELECT prd.id_pay, prd.id_emp, p.per_year, " + (!byType ? "prd.fk_ded" : "prd.fk_tp_ded") + " AS f_ded, " +
            "SUM(prd.amt_r) AS f_amount " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON " +
            "p.id_pay = pr.id_pay " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON " +
            "pr.id_pay = prd.id_pay AND pr.id_emp = prd.id_emp " +
            "WHERE p.b_del = 0 AND pr.b_del = 0 AND prd.b_del = 0 AND p.id_pay <> " + payrollId + " AND pr.id_emp = " + employeeId + " AND " +
            "p.per_year = " + periodYear + " AND " + ((taxComputationType == SModSysConsts.HRSS_TP_TAX_COMP_ANN ||
                taxComputationType == SLibConsts.UNDEFINED) ?
                "p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "'" :
                "p.dt_sta >= '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND " +
                "p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "'") + " " +
            "GROUP BY " + (!byType ? "prd.fk_ded" : "prd.fk_tp_ded") + " " +
            "ORDER BY " + (!byType ? "prd.fk_ded" : "prd.fk_tp_ded") + " ";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            accumulatedDeduction = new SHrsAccumulatedDeduction(resultSet.getInt("f_ded"), resultSet.getDouble("f_amount"));
            aAccumulatedDeductions.add(accumulatedDeduction);
        }

        return aAccumulatedDeductions;
    }

    /*
     * Public methods
     */

    public SGuiSession getSession() {
        return moSession;
    }

    @Override
    public SHrsPayroll createHrsPayroll(final SDbConfig config, final SDbWorkingDaySettings workingDaySettings, final SDbPayroll payroll, final boolean isCopy) throws Exception {
        SHrsPayroll hrsPayroll = new SHrsPayroll();

        hrsPayroll.setConfig(config);
        hrsPayroll.setWorkingDaySettings(workingDaySettings);
        hrsPayroll.setPayroll(payroll);
        hrsPayroll.setLastPayrollPeriod(isLastPayrollForCredits(payroll.getPkPayrollId(), payroll.getPeriodYear(), payroll.getPeriod(), payroll.getFkPaymentTypeId()));

        // LoanTypeAdjustment:

        hrsPayroll.getLoanTypeAdjustment().addAll(getLoanTypeAdjustment());

        // Uma:

        hrsPayroll.getUmas().addAll(readUmas());
        
        // Holidays:

        hrsPayroll.getHolidays().addAll(getHolidays());

        // Tax tables:

        hrsPayroll.getTaxTables().addAll(getTaxTables());

        // Tax subsidy tables:

        hrsPayroll.getTaxSubsidyTables().addAll(getTaxSubsidyTables());

        // SS Contribution tables:

        hrsPayroll.getSsContributionTables().addAll(getSsContributionTables());
        
        // Benefit tables:

        hrsPayroll.getBenefitTables().addAll(getBenefitTables());
        
        // Benefit tables:

        hrsPayroll.getBenefitTablesAnniversarys().addAll(getBenefitTableAnniversarys((getBenefitTables())));

        // Mwz type wages:

        hrsPayroll.getMwzTypeWages().addAll(getMwzTypeWages());

        // Earnings:

        hrsPayroll.getEarnigs().addAll(getEarnings());

        // Deductions:

        hrsPayroll.getDeductions().addAll(getDeductions());

        // Automatic earnings:

        hrsPayroll.getAutomaticEarnings().addAll(getAutomaticEarnings(payroll.getFkPaymentTypeId()));

        // Automatic deductions:

        hrsPayroll.getAutomaticDeductions().addAll(getAutomaticDeductions(payroll.getFkPaymentTypeId()));

        // Employees:

        hrsPayroll.getEmployees().addAll(getEmployees(payroll.getFkPaymentTypeId(), payroll.getPkPayrollId()));

        // Receipts:

        hrsPayroll.getReceipts().addAll(getHrsPayrollReceipts(hrsPayroll, payroll.getPkPayrollId(), isCopy, payroll.isRegistryNew()));

        // Preserve data provider:

        hrsPayroll.setPayrollDataProvider(this);

        return hrsPayroll;
    }

    public SHrsEmployee computeEmployee(final SHrsEmployee pHrsEmployee, final int payrollId, final int employeeId, final int payrollYear, final int payrollYearPeriod, final int fiscalYear, final Date dateStart, final Date dateEnd, final int taxComputationType) throws Exception {
        Date periodStart = null;
        Date periodEnd = null;
        SHrsEmployee hrsEmployee = new SHrsEmployee(payrollYear, payrollYearPeriod, dateStart, dateEnd, taxComputationType);

        hrsEmployee.setEmployee(pHrsEmployee.getEmployee());
        hrsEmployee.setHrsPayrollReceipt(pHrsEmployee.getHrsPayrollReceipt());
        hrsEmployee.getLoans().addAll(getEmployeeLoans(employeeId));
        hrsEmployee.getLoanPayments().addAll(getEmployeeLoanPayments(hrsEmployee.getLoans(), payrollId, payrollYear, payrollYearPeriod));
        hrsEmployee.getAbsences().addAll(getEmployeeAbsences(employeeId));
        hrsEmployee.getAbsencesConsumptions().addAll(getEmployeeAbsencesConsumption(hrsEmployee.getAbsences(), payrollId));
        hrsEmployee.getEmployeeHireLogs().addAll(getEmployeeHireLogs(employeeId, dateStart, dateEnd));
        
        periodStart = SLibTimeUtils.getBeginOfYear(SLibTimeUtils.createDate(fiscalYear));
        periodEnd = SLibTimeUtils.getEndOfYear(SLibTimeUtils.createDate(fiscalYear)).compareTo(dateEnd) < 0 ? SLibTimeUtils.getEndOfYear(SLibTimeUtils.createDate(fiscalYear)) : dateEnd;
        hrsEmployee.setDaysHiredAnnual(getEmployeeHireDays(getEmployeeHireLogs(employeeId, periodStart, periodEnd), periodStart, periodEnd));
        hrsEmployee.setAccumulatedTaxableEarning(getEmployeeAccumulatedTaxableEarnings(payrollId, employeeId, fiscalYear, periodEnd));
        hrsEmployee.setAccumulatedTaxableEarningAlt(getEmployeeAccumulatedTaxableEarningsAlt(payrollId, employeeId, fiscalYear, periodEnd));
        
        hrsEmployee.setDaysHiredPayroll(getEmployeeHireDays(hrsEmployee.getEmployeeHireLogs(), dateStart, dateEnd));
        hrsEmployee.setBusinessDays(getEmployeeBusinessDays(hrsEmployee.getEmployeeHireLogs(), pHrsEmployee.getEmployee().getFkPaymentTypeId(), dateStart, dateEnd));
        hrsEmployee.setSeniority(SHrsUtils.getSeniorityEmployee(pHrsEmployee.getEmployee().getDateBenefits(), dateEnd));
        hrsEmployee.getYearHrsAccumulatedEarnigs().addAll(getEmployeeAccumulatedEarnings(payrollId, employeeId, payrollYear, dateStart, dateEnd, SLibConsts.UNDEFINED, false));
        hrsEmployee.getYearHrsAccumulatedEarnigsByType().addAll(getEmployeeAccumulatedEarnings(payrollId, employeeId, payrollYear, dateStart, dateEnd, taxComputationType, true));
        hrsEmployee.getYearHrsAccumulatedEarnigsByTaxComputation().addAll(getEmployeeAccumulatedEarnings(payrollId, employeeId, payrollYear, dateStart, dateEnd, taxComputationType, false));
        hrsEmployee.getYearHrsAccumulatedDeductions().addAll(getEmployeeAccumulatedDeductions(payrollId, employeeId, payrollYear, dateStart, dateEnd, SLibConsts.UNDEFINED, false));
        hrsEmployee.getYearHrsAccumulatedDeductionsByType().addAll(getEmployeeAccumulatedDeductions(payrollId, employeeId, payrollYear, dateStart, dateEnd, taxComputationType, true));
        hrsEmployee.getYearHrsAccumulatedDeductionsByTaxComputation().addAll(getEmployeeAccumulatedDeductions(payrollId, employeeId, payrollYear, dateStart, dateEnd, taxComputationType, false));
        //hrsEmployee.getYearPayrollReceiptDays().addAll(getYearPayrollReceiptDays(payrollId, employeeId, periodYear, dateStart, dateEnd, taxComputationType));

        return hrsEmployee;
    }

    @Override
    public SHrsEmployee createHrsEmployee(final SHrsPayroll hrsPayroll, final int payrollId, final int employeeId, final int payrollYear, final int payrollYearPeriod, final int fiscalYear, final Date dateStart, final Date dateEnd, final int taxComputationType) throws Exception {
        SHrsEmployee hrsEmployee = new SHrsEmployee(payrollYear, payrollYearPeriod, dateStart, dateEnd, taxComputationType);
        
        hrsEmployee.setEmployee(hrsPayroll.getDataEmployee(employeeId));
        hrsEmployee = computeEmployee(hrsEmployee, payrollId, employeeId, payrollYear, payrollYearPeriod, fiscalYear, dateStart, dateEnd, taxComputationType);

        return hrsEmployee;
    }
}

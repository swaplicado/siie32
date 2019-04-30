/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos, Juan Barajas, Sergio Flores
 */
public class SHrsPayrollDataProvider implements SHrsDataProvider {
    
    private static final int TAX = 1;
    private static final int TAX_ALT = 2; // Articule 174 RLISR
    private static final int COMP_TAX = 1;
    private static final int COMP_TAX_SUB = 2;

    private final SGuiSession moSession;
    private final Statement miStatement;
    private final HashMap<Integer, SDbWorkingDaySettings> moWorkingDaySettingsMap; // by type of payment

    public SHrsPayrollDataProvider(SGuiSession session) throws Exception {
        moSession = session;
        miStatement = moSession.getStatement().getConnection().createStatement();
        moWorkingDaySettingsMap = new HashMap<>();
        moWorkingDaySettingsMap.put(SModSysConsts.HRSS_TP_PAY_WEE, SHrsUtils.getPayrollWorkingDaySettings(moSession, SModSysConsts.HRSS_TP_PAY_WEE));
        moWorkingDaySettingsMap.put(SModSysConsts.HRSS_TP_PAY_FOR, SHrsUtils.getPayrollWorkingDaySettings(moSession, SModSysConsts.HRSS_TP_PAY_FOR));
    }

    /*
     * Private methods
     */

    private ArrayList<SDbLoanTypeAdjustment> getLoanTypeAdjustment() throws Exception {
        ArrayList<SDbLoanTypeAdjustment> registries = new ArrayList<>();

        String sql = "SELECT id_tp_loan, id_adj "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_TP_LOAN_ADJ) + " "
                + "WHERE NOT b_del "
                + "ORDER BY dt_sta DESC, id_tp_loan, id_adj DESC;";

        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SDbLoanTypeAdjustment registry = new SDbLoanTypeAdjustment();
            registry.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            registries.add(registry);
        }
        resultSet.close();

        return registries;
    }
    
    private ArrayList<SDbUma> readUmas() throws Exception {
        ArrayList<SDbUma> registries = new ArrayList<>();

        String sql = "SELECT id_uma "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_UMA) + " "
                + "WHERE NOT b_del "
                + "ORDER BY dt_sta DESC, id_uma;";

        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SDbUma registry = new SDbUma();
            registry.read(moSession, new int[] { resultSet.getInt(1) });
            registries.add(registry);
        }
        resultSet.close();

        return registries;
    }
    
    private ArrayList<SDbUmi> readUmis() throws Exception {
        ArrayList<SDbUmi> registries = new ArrayList<>();

        String sql = "SELECT id_umi "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_UMI) + " "
                + "WHERE NOT b_del "
                + "ORDER BY dt_sta DESC, id_umi;";

        ResultSet resultSet = moSession.getDatabase().getConnection().createStatement().executeQuery(sql);
        while (resultSet.next()) {
            SDbUmi registry = new SDbUmi();
            registry.read(moSession, new int[] { resultSet.getInt(1) });
            registries.add(registry);
        }
        resultSet.close();

        return registries;
    }
    
    private ArrayList<SDbHoliday> getHolidays(final int year) throws Exception {
        // 7 days back from end of year of previous year:
        Date dateStart = SLibTimeUtils.addDate(SLibTimeUtils.getEndOfYear(SLibTimeUtils.createDate(year - 1, SLibTimeConsts.MONTH_MAX)), 0, 0, -SHrsConsts.WEEK_DAYS);
        
        // 7 days forward from beginning of year of next year:
        Date dateEnd = SLibTimeUtils.addDate(SLibTimeUtils.getBeginOfYear(SLibTimeUtils.createDate(year + 1, SLibTimeConsts.MONTH_MIN)), 0, 0, SHrsConsts.WEEK_DAYS);
        
        return getHolidays(dateStart, dateEnd);
    }
    
    private ArrayList<SDbHoliday> getHolidays(final Date dateStart, final Date dateEnd) throws Exception {
        ArrayList<SDbHoliday> registries = new ArrayList<>();

        String sql = "SELECT id_hdy, id_hol "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_HOL) + " "
                + "WHERE NOT b_del AND "
                + "dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' "
                + "ORDER BY dt, id_hol;";

        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SDbHoliday registry = new SDbHoliday();
            registry.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            registries.add(registry);
        }
        resultSet.close();

        return registries;
    }

    private ArrayList<SDbTaxTable> getTaxTables() throws Exception {
        ArrayList<SDbTaxTable> registries = new ArrayList<>();

        String sql = "SELECT id_tax "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_TAX) + " "
                + "WHERE NOT b_del "
                + "ORDER BY dt_sta, id_tax;";

        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SDbTaxTable registry = new SDbTaxTable();
            registry.read(moSession, new int[] { resultSet.getInt(1) });
            registries.add(registry);
        }
        resultSet.close();

        return registries;
    }

    private ArrayList<SDbTaxSubsidyTable> getTaxSubsidyTables() throws Exception {
        ArrayList<SDbTaxSubsidyTable> registries = new ArrayList<>();

        String sql = "SELECT id_tax_sub "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_TAX_SUB) + " "
                + "WHERE NOT b_del "
                + "ORDER BY dt_sta, id_tax_sub;";

        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SDbTaxSubsidyTable registry = new SDbTaxSubsidyTable();
            registry.read(moSession, new int[] { resultSet.getInt(1) });
            registries.add(registry);
        }
        resultSet.close();

        return registries;
    }

    private ArrayList<SDbSsContributionTable> getSsContributionTables() throws Exception {
        ArrayList<SDbSsContributionTable> registries = new ArrayList<>();

        String sql = "SELECT id_ssc "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_SSC) + " "
                + "WHERE NOT b_del "
                + "ORDER BY dt_sta, id_ssc;";

        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SDbSsContributionTable registry = new SDbSsContributionTable();
            registry.read(moSession, new int[] { resultSet.getInt(1) });
            registries.add(registry);
        }
        resultSet.close();

        return registries;
    }
    
    private ArrayList<SDbBenefitTable> getBenefitTables() throws Exception {
        ArrayList<SDbBenefitTable> registries = new ArrayList<>();

        String sql = "SELECT id_ben "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " "
                + "WHERE NOT b_del "
                + "ORDER BY dt_sta DESC, id_ben;";

        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SDbBenefitTable registry = new SDbBenefitTable();
            registry.read(moSession, new int[] { resultSet.getInt(1) });
            registries.add(registry);
        }
        resultSet.close();

        return registries;
    }
    
    private ArrayList<SHrsBenefitTableAnniversary> getBenefitTableAnniversarys(ArrayList<SDbBenefitTable> benefitTables) throws Exception {
        return SHrsUtils.createBenefitTablesAnniversarys(benefitTables);
    }

    private ArrayList<SDbMwzTypeWage> getMwzTypeWages() throws Exception {
        ArrayList<SDbMwzTypeWage> registries = new ArrayList<>();

        String sql = "SELECT id_tp_mwz, id_wage "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_MWZ_WAGE) + " "
                + "WHERE NOT b_del "
                + "ORDER BY dt_sta, id_tp_mwz, id_wage;";

        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SDbMwzTypeWage registry = new SDbMwzTypeWage();
            registry.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            registries.add(registry);
        }
        resultSet.close();

        return registries;
    }

    private ArrayList<SDbEarning> getEarnings() throws Exception {
        ArrayList<SDbEarning> registries = new ArrayList<>();

        String sql = "SELECT id_ear "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " "
                + "WHERE NOT b_del "
                + "ORDER BY CONCAT(code, ' - ', name), id_ear;";

        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SDbEarning registry = new SDbEarning();
            registry.read(moSession, new int[] { resultSet.getInt(1) });
            registries.add(registry);
        }
        resultSet.close();

        return registries;
    }

    private ArrayList<SDbDeduction> getDeductions() throws Exception {
        ArrayList<SDbDeduction> registries = new ArrayList<>();

        String sql = "SELECT id_ded "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " "
                + "WHERE NOT b_del "
                + "ORDER BY CONCAT(code, ' - ', name), id_ded;";

        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SDbDeduction deduction = new SDbDeduction();
            deduction.read(moSession, new int[] { resultSet.getInt(1) });
            registries.add(deduction);
        }
        resultSet.close();

        return registries;
    }

    private ArrayList<SDbAutomaticEarning> getAutomaticEarnings(final int paymentType) throws Exception {
        ArrayList<SDbAutomaticEarning> registries = new ArrayList<>();

        // employee automatic earnings (mayor precedence, can hide global automatic earnings):

        String sql = "SELECT ae.id_ear, ae.id_aut "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_EAR) + " AS ae "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON ae.fk_emp_n = e.id_emp "
                + "WHERE NOT ae.b_del AND e.fk_tp_pay = " + paymentType + " "
                + "ORDER BY ae.id_ear, ae.id_aut;";

        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SDbAutomaticEarning registry = new SDbAutomaticEarning();
            registry.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            registries.add(registry);
        }
        resultSet.close();

        // global automatic earnings (minor precedence):

        sql = "SELECT id_ear, id_aut "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_EAR) + " "
                + "WHERE NOT b_del AND fk_emp_n IS NULL "
                + "ORDER BY id_ear, id_aut;";

        resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SDbAutomaticEarning registry = new SDbAutomaticEarning();
            registry.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            registries.add(registry);
        }
        resultSet.close();

        return registries;
    }

    private ArrayList<SDbAutomaticDeduction> getAutomaticDeductions(final int paymentType) throws Exception {
        ArrayList<SDbAutomaticDeduction> registries = new ArrayList<>();

        // employee automatic deductions (mayor precedence, can hide global automatic deductions):

        String sql = "SELECT ad.id_ded, ad.id_aut "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_DED) + " AS ad "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON ad.fk_emp_n = e.id_emp "
                + "WHERE NOT ad.b_del AND e.fk_tp_pay = " + paymentType + " "
                + "ORDER BY ad.id_ded, ad.id_aut;";

        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SDbAutomaticDeduction registry = new SDbAutomaticDeduction();
            registry.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            registries.add(registry);
        }
        resultSet.close();

        // global automatic deductions (minor precedence):

        sql = "SELECT id_ded, id_aut "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_DED) + " "
                + "WHERE NOT b_del AND fk_emp_n IS NULL "
                + "ORDER BY id_ded, id_aut;";

        resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SDbAutomaticDeduction registry = new SDbAutomaticDeduction();
            registry.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            registries.add(registry);
        }
        resultSet.close();

        return registries;
    }

    private ArrayList<SDbEmployee> getEmployees(final int paymentType, final int payrollId) throws Exception {
        ArrayList<SDbEmployee> registries = new ArrayList<>();
        
        String sql = "SELECT e.id_emp "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e "
                + "WHERE NOT e.b_del AND (e.fk_tp_pay = " + paymentType + (payrollId == 0 ? "" : " OR e.id_emp IN ("
                + " SELECT pr.id_emp "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + " WHERE p.id_pay = " + payrollId + " AND NOT pr.b_del)") + ") "
                + "ORDER BY e.id_emp;";

        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            registries.add((SDbEmployee) moSession.readRegistry(SModConsts.HRSU_EMP, new int[] { resultSet.getInt(1) }));
        }
        resultSet.close();

        return registries;
    }

    private ArrayList<SHrsReceipt> getHrsReceipts(final SHrsPayroll hrsPayroll, final int payrollId, final boolean isCopy, final boolean isNew) throws Exception {
        ArrayList<SHrsReceipt> hrsReceipts = new ArrayList<>();

        if (!isNew) {
            String sql = "SELECT id_emp " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " " +
                    "WHERE id_pay = " + payrollId + " AND NOT b_del " +
                    "ORDER BY id_emp;";

            ResultSet resultSet = moSession.getStatement().getConnection().createStatement().executeQuery(sql);
            while (resultSet.next()) {
                SDbPayrollReceipt payrollReceipt = new SDbPayrollReceipt();
                payrollReceipt.read(moSession, new int[] { payrollId, resultSet.getInt(1) });

                SHrsReceipt hrsReceipt = new SHrsReceipt();
                hrsReceipt.setHrsPayroll(hrsPayroll);

                // Obtain payroll receipt earnings:

                for (SDbPayrollReceiptEarning payrollReceiptEarning : payrollReceipt.getChildPayrollReceiptEarnings()) {
                    if (!payrollReceiptEarning.isDeleted()) {
                        SHrsReceiptEarning hrsReceiptEarning = new SHrsReceiptEarning();

                        hrsReceiptEarning.setHrsReceipt(hrsReceipt);
                        hrsReceiptEarning.setEarning(hrsPayroll.getEarning(payrollReceiptEarning.getFkEarningId()));
                        hrsReceiptEarning.setPayrollReceiptEarning(payrollReceiptEarning);

                        hrsReceipt.getHrsReceiptEarnings().add(hrsReceiptEarning);
                    }
                }

                // Obtain payroll receipt deductions:

                for (SDbPayrollReceiptDeduction payrollReceiptDeduction : payrollReceipt.getChildPayrollReceiptDeductions()) {
                    if (!payrollReceiptDeduction.isDeleted()) {
                        SHrsReceiptDeduction hrsReceiptDeduction = new SHrsReceiptDeduction();

                        hrsReceiptDeduction.setHrsReceipt(hrsReceipt);
                        hrsReceiptDeduction.setDeduction(hrsPayroll.getDeduction(payrollReceiptDeduction.getFkDeductionId()));
                        hrsReceiptDeduction.setPayrollReceiptDeduction(payrollReceiptDeduction);

                        hrsReceipt.getHrsReceiptDeductions().add(hrsReceiptDeduction);
                    }
                }

                // Obtain absence consumptions:
                
                for (SDbAbsenceConsumption absenceConsumption : payrollReceipt.getChildAbsenceConsumption()) {
                    if (!absenceConsumption.isDeleted()) {
                        hrsReceipt.getAbsenceConsumptions().add(absenceConsumption);
                    }
                }
                
                // Create employee:

                SHrsEmployee hrsEmployee = createHrsEmployee(hrsPayroll, isCopy ? 0 : hrsPayroll.getPayroll().getPkPayrollId(), payrollReceipt.getPkEmployeeId(), 
                        hrsPayroll.getPayroll().getPeriodYear(), hrsPayroll.getPayroll().getPeriod(), hrsPayroll.getPayroll().getFiscalYear(),
                        hrsPayroll.getPayroll().getDateStart(), hrsPayroll.getPayroll().getDateEnd(), hrsPayroll.getPayroll().getFkTaxComputationTypeId());
                hrsEmployee.setHrsReceipt(hrsReceipt);
                hrsReceipt.setHrsEmployee(hrsEmployee);
                hrsReceipt.setPayrollReceipt(payrollReceipt);

                hrsReceipts.add(hrsReceipt);
            }
            resultSet.close();
        }

        return hrsReceipts;
    }
    
    /**
     * Populates descriptions map.
     * @param type Desired registry type SModConsts.
     * @param field Desired registry field SDbRegistry.FIELD_...
     * @param map Map to populate.
     * @return 
     */
    private void populateDescriptionsMap(final int type, final int field, final HashMap<Integer, String> map) throws Exception {
        String name = "";
        
        switch (field) {
            case SDbRegistry.FIELD_CODE:
                name = "code";
                break;
            case SDbRegistry.FIELD_NAME:
                name = "name";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        String id = "";
        
        switch (type) {
            case SModConsts.HRSS_TP_EAR_COMP:
                id = "id_tp_ear_comp";
                break;
            case SModConsts.HRSS_TP_DED_COMP:
                id = "id_tp_ded_comp";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        map.clear();
        String sql = "SELECT " + id + ", " + name + " "
                + "FROM " + SModConsts.TablesMap.get(type) + " "
                + "WHERE NOT b_del "
                + "ORDER BY " + id + ";";
        ResultSet resultSet = moSession.getStatement().executeQuery(sql);
        while (resultSet.next()) {
            map.put(resultSet.getInt(1), resultSet.getString(2));
        }
        resultSet.close();
    }

    private ArrayList<SDbLoan> getEmployeeLoans(final int employeeId) throws Exception {
        ArrayList<SDbLoan> loans = new ArrayList<>();

        String sql = "SELECT id_emp, id_loan "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_LOAN) + " "
                + "WHERE id_emp = " + employeeId + " AND NOT b_del AND NOT b_clo "
                + "ORDER BY id_loan;";
        
        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SDbLoan loan = new SDbLoan();
            loan.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            loans.add(loan);
        }
        resultSet.close();

        return loans;
    }
    
    private ArrayList<SHrsLoanPayments> getEmployeeLoanPayments(final ArrayList<SDbLoan> loans, final int payrollId, final int payrollYear, final int payrollPeriod) throws Exception {
        String sql;
        ArrayList<SHrsLoanPayments> registries = new ArrayList<>();
        
        sql = "SELECT rcp_ear.id_pay, rcp_ear.id_emp, rcp_ear.id_mov "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS rcp ON rcp.id_pay = p.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS rcp_ear ON rcp_ear.id_pay = rcp.id_pay AND rcp_ear.id_emp = rcp.id_emp "
                + "WHERE (p.id_pay = 0 OR NOT p.b_del) AND NOT rcp.b_del AND NOT rcp_ear.b_del AND "
                + "p.id_pay <> " + payrollId + " AND rcp_ear.fk_loan_emp_n = ? AND rcp_ear.fk_loan_loan_n = ?;";
        PreparedStatement psEarnings = miStatement.getConnection().prepareStatement(sql);
        
        sql = "SELECT rcp_ded.id_pay, rcp_ded.id_emp, rcp_ded.id_mov "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS rcp ON rcp.id_pay = p.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS rcp_ded ON rcp_ded.id_pay = rcp.id_pay AND rcp_ded.id_emp = rcp.id_emp "
                + "WHERE (p.id_pay = 0 OR NOT p.b_del) AND NOT rcp.b_del AND NOT rcp_ded.b_del AND "
                + "p.id_pay <> " + payrollId + " AND rcp_ded.fk_loan_emp_n = ? AND rcp_ded.fk_loan_loan_n = ?;";
        PreparedStatement psDeductions = miStatement.getConnection().prepareStatement(sql);
        
        sql = "SELECT COALESCE(SUM(rd.amt_r), 0.0) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS r ON r.id_pay = p.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS rd ON rd.id_pay = r.id_pay AND rd.id_emp = r.id_emp "
                + "WHERE NOT p.b_del AND NOT r.b_del AND NOT rd.b_del AND "
                + "p.id_pay <> " + payrollId + " AND p.per_year = " + payrollYear + " AND p.per = " + payrollPeriod + " AND r.id_emp = ? AND "
                + "rd.fk_loan_emp_n = ? AND rd.fk_loan_loan_n = ? AND rd.fk_tp_loan_n = ?;";
        PreparedStatement psAmountDeductions = miStatement.getConnection().prepareStatement(sql);
        
        sql = "SELECT COALESCE(SUM(re.amt_r), 0.0) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS r ON r.id_pay = p.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS re ON re.id_pay = r.id_pay AND re.id_emp = r.id_emp "
                + "WHERE NOT p.b_del AND NOT r.b_del AND NOT re.b_del AND "
                + "p.id_pay <> " + payrollId + " AND p.per_year = " + payrollYear + " AND p.per = " + payrollPeriod + " AND r.id_emp = ? AND "
                + "re.fk_loan_emp_n = ? AND re.fk_loan_loan_n = ? AND re.fk_tp_loan_n = ?;";
        PreparedStatement psAmountEarnings = miStatement.getConnection().prepareStatement(sql);
        
        sql = "SELECT COALESCE(SUM(r.day_hire_pay - r.day_inc_not_pad_pay - r.day_not_wrk_not_pad), 0.0) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS r ON r.id_pay = p.id_pay "
                + "WHERE NOT p.b_del AND NOT r.b_del AND "
                + "p.id_pay <> " + payrollId + " AND p.per_year = " + payrollYear + " AND p.per = " + payrollPeriod + " AND r.id_emp = ?;";
        PreparedStatement psDays = miStatement.getConnection().prepareStatement(sql);
        
        for (SDbLoan loan : loans) {
            ResultSet resultSet;
            SHrsLoanPayments hrsLoanPayments = new SHrsLoanPayments();
            
            psEarnings.setInt(1, loan.getPkEmployeeId());
            psEarnings.setInt(2, loan.getPkLoanId());

            resultSet = psEarnings.executeQuery();
            while (resultSet.next()) {
                SDbPayrollReceiptEarning payrollReceiptEarning = new SDbPayrollReceiptEarning();
                payrollReceiptEarning.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
                hrsLoanPayments.getPayrollReceiptEarnings().add(payrollReceiptEarning);
            }
            resultSet.close();
            
            psDeductions.setInt(1, loan.getPkEmployeeId());
            psDeductions.setInt(2, loan.getPkLoanId());

            resultSet = psDeductions.executeQuery();
            while (resultSet.next()) {
                SDbPayrollReceiptDeduction payrollReceiptDeduction = new SDbPayrollReceiptDeduction();
                payrollReceiptDeduction.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
                hrsLoanPayments.getPayrollReceiptDeductions().add(payrollReceiptDeduction);
            }
            resultSet.close();
            
            // loan amount already paid:
            
            double amount = 0;
            
            psAmountDeductions.setInt(1, loan.getPkEmployeeId());
            psAmountDeductions.setInt(2, loan.getPkEmployeeId());
            psAmountDeductions.setInt(3, loan.getPkLoanId());
            psAmountDeductions.setInt(4, loan.getFkLoanTypeId());
            
            resultSet = psAmountDeductions.executeQuery();
            if (resultSet.next()) {
                amount = resultSet.getDouble(1);
            }
            resultSet.close();
            
            psAmountEarnings.setInt(1, loan.getPkEmployeeId());
            psAmountEarnings.setInt(2, loan.getPkEmployeeId());
            psAmountEarnings.setInt(3, loan.getPkLoanId());
            psAmountEarnings.setInt(4, loan.getFkLoanTypeId());
        
            resultSet = psAmountEarnings.executeQuery();
            if (resultSet.next()) {
                amount = SLibUtils.roundAmount(amount - resultSet.getDouble(1));
            }
            resultSet.close();
            
            // days active in period year for employee:
            
            double days = 0;
            
            psDays.setInt(1, loan.getPkEmployeeId());
        
            resultSet = psDays.executeQuery();
            if (resultSet.next()) {
                days = resultSet.getDouble(1);
            }
            resultSet.close();
            
            if (!hrsLoanPayments.getPayrollReceiptDeductions().isEmpty()) {
                hrsLoanPayments.setLoan(loan);
                hrsLoanPayments.setAmountPeriod(amount);
                hrsLoanPayments.setDaysPeriod(days);
                registries.add(hrsLoanPayments);
            }
        }
        
        psEarnings.close();
        psDeductions.close();
        psAmountDeductions.close();
        psAmountEarnings.close();
        psDays.close();

        return registries;
    }

    private ArrayList<SDbAbsence> getEmployeeAbsences(final int employeeId) throws Exception {
        ArrayList<SDbAbsence> absences = new ArrayList<>();

        String sql = "SELECT id_emp, id_abs " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ABS) + " " +
            "WHERE id_emp = " + employeeId + " AND NOT b_del AND NOT b_clo " +
            "ORDER BY dt_sta, id_emp, id_abs;";

        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SDbAbsence absence = new SDbAbsence();
            absence.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            absences.add(absence);
        }
        resultSet.close();
        
        return absences;
    }
    
    private ArrayList<SDbAbsenceConsumption> getEmployeeAbsencesConsumptions(final ArrayList<SDbAbsence> absences, final int payrollId) throws Exception {
        ArrayList<SDbAbsenceConsumption> absencesConsumptions = new ArrayList<>();

        String sql = "SELECT ac.id_cns "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ABS_CNS) + " AS ac "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON ac.fk_rcp_pay = pr.id_pay AND ac.fk_rcp_emp = pr.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p ON pr.id_pay = p.id_pay "
                + "WHERE ac.id_emp = ? AND ac.id_abs = ? AND ac.fk_rcp_pay <> " + payrollId + " AND NOT ac.b_del AND NOT pr.b_del AND NOT p.b_del "
                + "ORDER BY ac.id_cns;";
        PreparedStatement psConsumptions = miStatement.getConnection().prepareStatement(sql);
        
        for (SDbAbsence absence : absences) {
            psConsumptions.setInt(1, absence.getPkEmployeeId());
            psConsumptions.setInt(2, absence.getPkAbsenceId());

            ResultSet resultSet = psConsumptions.executeQuery();
            while (resultSet.next()) {
                SDbAbsenceConsumption absenceConsumption = new SDbAbsenceConsumption();
                absenceConsumption.read(moSession, new int[] { absence.getPkEmployeeId(), absence.getPkAbsenceId(), resultSet.getInt(1) });
                absencesConsumptions.add(absenceConsumption);
            }
            resultSet.close();
        }
        
        psConsumptions.close();
        
        return absencesConsumptions;
    }

    private ArrayList<SDbEmployeeHireLog> getEmployeeHireLogs(final int employeeId, final Date dateStart, final Date dateEnd)  throws Exception {
        return SHrsUtils.getEmployeeHireLogs(moSession, miStatement, employeeId, dateStart, dateEnd);
    }
    
    private int getEmployeeHiredDays(final ArrayList<SDbEmployeeHireLog> employeeHireLogs, final Date dateStart, final Date dateEnd) throws Exception {
        int hiredDays = 0;
        
        for (SDbEmployeeHireLog entry : employeeHireLogs) {
            hiredDays += SLibTimeUtils.countPeriodDays(
                    entry.getDateHire().compareTo(dateStart) <= 0 ? dateStart : entry.getDateHire(), 
                    entry.getDateDismissed_n() == null || entry.getDateDismissed_n().compareTo(dateEnd) >= 0 ? dateEnd : entry.getDateDismissed_n());
        }
        
        return hiredDays;
    }
    
    private double getEmployeeAccumulatedTaxableEarnings(final int payrollId, final int employeeId, final int fiscalYear, final Date payrollDateEnd, int taxType) throws Exception {
        double earnings = 0;
        String sqlAltTax;
        
        switch (taxType) {
            case TAX:
                sqlAltTax = "0";
                break;
            case TAX_ALT:
                sqlAltTax = "1";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        String sql = "SELECT SUM(pre.amt_taxa) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pr.id_pay = pre.id_pay AND pr.id_emp = pre.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS ear ON ear.id_ear = pre.fk_ear "
                + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del AND "
                + "p.id_pay <> " + payrollId + " AND pr.id_emp = " + employeeId + " AND "
                + "p.fis_year = " + fiscalYear + " AND p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(payrollDateEnd) + "' AND "
                + "pre.b_alt_tax = " + sqlAltTax + ";"; // Articule 174 RLISR
        ResultSet resultSet = miStatement.executeQuery(sql);
        if (resultSet.next()) {
            earnings = resultSet.getDouble(1);
        }
        resultSet.close();

        return earnings;
    }
    
    private double getEmployeeAnnualCompensation(final int payrollId, final int employeeId, final int fiscalYear, final Date payrollDateEnd, int compensationType) throws Exception {
        double compensation = 0;
        String sqlCompensation;
        
        switch (compensationType) {
            case COMP_TAX:
                sqlCompensation = "pr.pay_tax_comp";
                break;
            case COMP_TAX_SUB:
                sqlCompensation = "pr.pay_tax_sub_comp";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        String sql = "SELECT SUM(" + sqlCompensation + ") "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + "WHERE NOT p.b_del AND NOT pr.b_del AND "
                + "p.id_pay <> " + payrollId + " AND pr.id_emp = " + employeeId + " AND "
                + "p.fis_year = " + fiscalYear + " AND p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(payrollDateEnd) + "' "
                + "GROUP BY pr.id_emp "
                + "ORDER BY pr.id_emp ";
        
        ResultSet resultSet = miStatement.executeQuery(sql);
        if (resultSet.next()) {
            compensation = resultSet.getDouble(1);
        }
        resultSet.close();

        return compensation;
    }
    
    private int getEmployeeBusinessDays(final ArrayList<SDbEmployeeHireLog> employeeHireLogs, final int paymentType, final Date dateStart, final Date dateEnd) throws Exception {
        int businessDays = 0;
        
        for (SDbEmployeeHireLog entry : employeeHireLogs) {
            businessDays += SHrsUtils.countBusinessDays(
                    entry.getDateHire().compareTo(dateStart) <= 0 ? dateStart : entry.getDateHire(), 
                    entry.getDateDismissed_n() == null || entry.getDateDismissed_n().compareTo(dateEnd) >= 0 ? dateEnd : entry.getDateDismissed_n(), 
                    moWorkingDaySettingsMap.get(paymentType), 
                    getHolidays(dateStart, dateEnd));
        }
        
        return businessDays;
    }

    private ArrayList<SHrsAccumulatedEarning> getEmployeeAccumulatedEarnings(final int payrollId, final int employeeId, final int periodYear, 
            final Date dateStart, final Date dateEnd, final int taxComputationType, final boolean byType) throws Exception {
        ArrayList<SHrsAccumulatedEarning> hrsAccumulatedEarnings = new ArrayList<>();

        String sql = "SELECT pre.id_pay, pre.id_emp, p.per_year, " + (!byType ? "pre.fk_ear" : "pre.fk_tp_ear") + " AS f_ear, "
                + "SUM(pre.amt_r) AS f_amount, SUM(pre.amt_exem) AS f_exem, SUM(pre.amt_taxa) AS f_taxa "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pr.id_pay = pre.id_pay AND pr.id_emp = pre.id_emp "
                + "WHERE p.b_del = 0 AND pr.b_del = 0 AND pre.b_del = 0 AND p.id_pay <> " + payrollId + " AND pr.id_emp = " + employeeId + " AND "
                + "p.per_year = " + periodYear + " AND " + ((taxComputationType == SModSysConsts.HRSS_TP_TAX_COMP_ANN ||
                    taxComputationType == 0) ?
                    "p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "'" :
                    "p.dt_sta >= '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND " +
                    "p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "'") + " "
                + "GROUP BY " + (!byType ? "pre.fk_ear" : "pre.fk_tp_ear") + " "
                + "ORDER BY " + (!byType ? "pre.fk_ear" : "pre.fk_tp_ear") + ";";
        
        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SHrsAccumulatedEarning hrsAccumulatedEarning = new SHrsAccumulatedEarning(resultSet.getInt("f_ear"), resultSet.getDouble("f_amount"), resultSet.getDouble("f_taxa"));
            hrsAccumulatedEarnings.add(hrsAccumulatedEarning);
        }
        resultSet.close();

        return hrsAccumulatedEarnings;
    }

    private ArrayList<SHrsAccumulatedDeduction> getEmployeeAccumulatedDeductions(final int payrollId, final int employeeId, final int periodYear, 
            final Date dateStart, final Date dateEnd, final int taxComputationType, final boolean byType) throws Exception {
        ArrayList<SHrsAccumulatedDeduction> hrsAccumulatedDeductions = new ArrayList<>();

        String sql = "SELECT prd.id_pay, prd.id_emp, p.per_year, " + (!byType ? "prd.fk_ded" : "prd.fk_tp_ded") + " AS f_ded, " +
            "SUM(prd.amt_r) AS f_amount " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON " +
            "p.id_pay = pr.id_pay " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON " +
            "pr.id_pay = prd.id_pay AND pr.id_emp = prd.id_emp " +
            "WHERE p.b_del = 0 AND pr.b_del = 0 AND prd.b_del = 0 AND p.id_pay <> " + payrollId + " AND pr.id_emp = " + employeeId + " AND " +
            "p.per_year = " + periodYear + " AND " + ((taxComputationType == SModSysConsts.HRSS_TP_TAX_COMP_ANN ||
                taxComputationType == 0) ?
                "p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "'" :
                "p.dt_sta >= '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND " +
                "p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "'") + " " +
            "GROUP BY " + (!byType ? "prd.fk_ded" : "prd.fk_tp_ded") + " " +
            "ORDER BY " + (!byType ? "prd.fk_ded" : "prd.fk_tp_ded") + " ";
        
        ResultSet resultSet = miStatement.executeQuery(sql);
        while (resultSet.next()) {
            SHrsAccumulatedDeduction hrsAccumulatedDeduction = new SHrsAccumulatedDeduction(resultSet.getInt("f_ded"), resultSet.getDouble("f_amount"));
            hrsAccumulatedDeductions.add(hrsAccumulatedDeduction);
        }
        resultSet.close();

        return hrsAccumulatedDeductions;
    }

    /*
     * Public methods
     */

    public SGuiSession getSession() {
        return moSession;
    }

    @Override
    public SHrsPayroll createHrsPayroll(final SDbConfig config, final SDbWorkingDaySettings workingDaySettings, final SDbPayroll payroll, final boolean isCopy) throws Exception {
        SHrsPayroll hrsPayroll = new SHrsPayroll(this);

        hrsPayroll.setConfig(config);
        hrsPayroll.setWorkingDaySettings(workingDaySettings);
        hrsPayroll.setPayroll(payroll);

        // Adjustments by loan type:
        hrsPayroll.getLoanTypeAdjustments().addAll(getLoanTypeAdjustment());

        // UMA:
        hrsPayroll.getUmas().addAll(readUmas());
        
        // UMI:
        hrsPayroll.getUmis().addAll(readUmis());
        
        // Holidays:
        hrsPayroll.getHolidays().addAll(getHolidays(payroll.getPeriodYear()));

        // Tax tables:
        hrsPayroll.getTaxTables().addAll(getTaxTables());

        // Tax subsidy tables:
        hrsPayroll.getTaxSubsidyTables().addAll(getTaxSubsidyTables());

        // SS Contribution tables:
        hrsPayroll.getSsContributionTables().addAll(getSsContributionTables());
        
        // Benefit tables:
        hrsPayroll.getBenefitTables().addAll(getBenefitTables());
        
        // Benefit tables:
        hrsPayroll.getHrsBenefitTablesAnniversarys().addAll(getBenefitTableAnniversarys((getBenefitTables())));

        // MWZ type wages:
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
        hrsPayroll.getHrsReceipts().addAll(getHrsReceipts(hrsPayroll, payroll.getPkPayrollId(), isCopy, payroll.isRegistryNew()));
        
        // Earning computation types:
        populateDescriptionsMap(SModConsts.HRSS_TP_EAR_COMP, SDbRegistry.FIELD_CODE, hrsPayroll.getEarningComputationTypesMap());
        
        // Deduction computation types:
        populateDescriptionsMap(SModConsts.HRSS_TP_DED_COMP, SDbRegistry.FIELD_CODE, hrsPayroll.getDeductionComputationTypesMap());

        return hrsPayroll;
    }

    public SHrsEmployee computeEmployee(final SHrsEmployee pHrsEmployee, final int payrollId, final int employeeId, final int payrollYear, final int payrollYearPeriod, final int fiscalYear, final Date dateStart, final Date dateEnd, final int taxComputationType) throws Exception {
        SHrsEmployee hrsEmployee = new SHrsEmployee(payrollYear, payrollYearPeriod, dateStart, dateEnd, taxComputationType);

        hrsEmployee.setEmployee(pHrsEmployee.getEmployee());
        hrsEmployee.setHrsReceipt(pHrsEmployee.getHrsReceipt());
        hrsEmployee.getLoans().addAll(getEmployeeLoans(employeeId));
        hrsEmployee.getLoanPayments().addAll(getEmployeeLoanPayments(hrsEmployee.getLoans(), payrollId, payrollYear, payrollYearPeriod));
        hrsEmployee.getAbsences().addAll(getEmployeeAbsences(employeeId));
        hrsEmployee.getAbsenceConsumptions().addAll(getEmployeeAbsencesConsumptions(hrsEmployee.getAbsences(), payrollId));
        hrsEmployee.getEmployeeHireLogs().addAll(getEmployeeHireLogs(employeeId, dateStart, dateEnd));
        
        Date periodStart = SLibTimeUtils.getBeginOfYear(SLibTimeUtils.createDate(fiscalYear));
        Date periodEnd = SLibTimeUtils.getEndOfYear(SLibTimeUtils.createDate(fiscalYear)).compareTo(dateEnd) < 0 ? SLibTimeUtils.getEndOfYear(SLibTimeUtils.createDate(fiscalYear)) : dateEnd;
        
        hrsEmployee.setDaysHiredAnnual(getEmployeeHiredDays(getEmployeeHireLogs(employeeId, periodStart, periodEnd), periodStart, periodEnd));
        hrsEmployee.setAccumulatedTaxableEarning(getEmployeeAccumulatedTaxableEarnings(payrollId, employeeId, fiscalYear, periodEnd, TAX));
        hrsEmployee.setAccumulatedTaxableEarningAlt(getEmployeeAccumulatedTaxableEarnings(payrollId, employeeId, fiscalYear, periodEnd, TAX_ALT));
        hrsEmployee.setAnnualTaxCompensated(getEmployeeAnnualCompensation(payrollId, employeeId, fiscalYear, periodEnd, COMP_TAX));
        hrsEmployee.setAnnualTaxSubsidyCompensated(getEmployeeAnnualCompensation(payrollId, employeeId, fiscalYear, periodEnd, COMP_TAX_SUB));
        
        hrsEmployee.setDaysHiredPayroll(getEmployeeHiredDays(hrsEmployee.getEmployeeHireLogs(), dateStart, dateEnd));
        hrsEmployee.setBusinessDays(getEmployeeBusinessDays(hrsEmployee.getEmployeeHireLogs(), pHrsEmployee.getEmployee().getFkPaymentTypeId(), dateStart, dateEnd));
        hrsEmployee.setSeniority(SHrsUtils.getSeniorityEmployee(pHrsEmployee.getEmployee().getDateBenefits(), dateEnd));
        hrsEmployee.getYearHrsAccumulatedEarnigs().addAll(getEmployeeAccumulatedEarnings(payrollId, employeeId, payrollYear, dateStart, dateEnd, 0, false));
        hrsEmployee.getYearHrsAccumulatedEarnigsByType().addAll(getEmployeeAccumulatedEarnings(payrollId, employeeId, payrollYear, dateStart, dateEnd, taxComputationType, true));
        hrsEmployee.getYearHrsAccumulatedEarnigsByTaxComputation().addAll(getEmployeeAccumulatedEarnings(payrollId, employeeId, payrollYear, dateStart, dateEnd, taxComputationType, false));
        hrsEmployee.getYearHrsAccumulatedDeductions().addAll(getEmployeeAccumulatedDeductions(payrollId, employeeId, payrollYear, dateStart, dateEnd, 0, false));
        hrsEmployee.getYearHrsAccumulatedDeductionsByType().addAll(getEmployeeAccumulatedDeductions(payrollId, employeeId, payrollYear, dateStart, dateEnd, taxComputationType, true));
        hrsEmployee.getYearHrsAccumulatedDeductionsByTaxComputation().addAll(getEmployeeAccumulatedDeductions(payrollId, employeeId, payrollYear, dateStart, dateEnd, taxComputationType, false));

        return hrsEmployee;
    }

    @Override
    public SHrsEmployee createHrsEmployee(final SHrsPayroll hrsPayroll, final int payrollId, final int employeeId, 
            final int payrollYear, final int payrollYearPeriod, final int fiscalYear, 
            final Date dateStart, final Date dateEnd, final int taxComputationType) throws Exception {
        SHrsEmployee hrsEmployee = new SHrsEmployee(payrollYear, payrollYearPeriod, dateStart, dateEnd, taxComputationType);
        
        hrsEmployee.setEmployee(hrsPayroll.getEmployee(employeeId));
        hrsEmployee = computeEmployee(hrsEmployee, payrollId, employeeId, payrollYear, payrollYearPeriod, fiscalYear, dateStart, dateEnd, taxComputationType);

        return hrsEmployee;
    }
}

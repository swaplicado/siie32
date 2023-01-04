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
public class SHrsPayrollDataProvider {
    
    private static final int TAX_STD = 1;     // standard tax
    private static final int TAX_ART_174 = 2; // tax by Articule 174 RLISR
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

    /*
     * Specific methods for a payroll
     */

    private ArrayList<SDbLoanTypeAdjustment> readLoanTypeAdjustment() throws Exception {
        ArrayList<SDbLoanTypeAdjustment> registries = new ArrayList<>();

        String sql = "SELECT id_tp_loan, id_adj "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_TP_LOAN_ADJ) + " "
                + "WHERE NOT b_del "
                + "ORDER BY dt_sta DESC, id_tp_loan, id_adj DESC;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbLoanTypeAdjustment registry = new SDbLoanTypeAdjustment();
                registry.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
                registries.add(registry);
            }
        }

        return registries;
    }
    
    private ArrayList<SDbUma> readUmas() throws Exception {
        ArrayList<SDbUma> registries = new ArrayList<>();

        String sql = "SELECT id_uma "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_UMA) + " "
                + "WHERE NOT b_del "
                + "ORDER BY dt_sta DESC, id_uma;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbUma registry = new SDbUma();
                registry.read(moSession, new int[] { resultSet.getInt(1) });
                registries.add(registry);
            }
        }

        return registries;
    }
    
    private ArrayList<SDbUmi> readUmis() throws Exception {
        ArrayList<SDbUmi> registries = new ArrayList<>();

        String sql = "SELECT id_umi "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_UMI) + " "
                + "WHERE NOT b_del "
                + "ORDER BY dt_sta DESC, id_umi;";

        try (ResultSet resultSet = moSession.getDatabase().getConnection().createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                SDbUmi registry = new SDbUmi();
                registry.read(moSession, new int[] { resultSet.getInt(1) });
                registries.add(registry);
            }
        }

        return registries;
    }
    
    private ArrayList<SDbHoliday> readHolidays(final int year) throws Exception {
        // 7 days back from end of year of previous year:
        Date dateStart = SLibTimeUtils.addDate(SLibTimeUtils.getEndOfYear(SLibTimeUtils.createDate(year - 1, SLibTimeConsts.MONTH_MAX)), 0, 0, -SHrsConsts.WEEK_DAYS);
        
        // 7 days forward from beginning of year of next year:
        Date dateEnd = SLibTimeUtils.addDate(SLibTimeUtils.getBeginOfYear(SLibTimeUtils.createDate(year + 1, SLibTimeConsts.MONTH_MIN)), 0, 0, SHrsConsts.WEEK_DAYS);
        
        return readHolidays(dateStart, dateEnd);
    }
    
    private ArrayList<SDbHoliday> readHolidays(final Date dateStart, final Date dateEnd) throws Exception {
        ArrayList<SDbHoliday> registries = new ArrayList<>();

        String sql = "SELECT id_hdy, id_hol "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_HOL) + " "
                + "WHERE NOT b_del AND "
                + "dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' "
                + "ORDER BY dt, id_hol;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbHoliday registry = new SDbHoliday();
                registry.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
                registries.add(registry);
            }
        }

        return registries;
    }

    private ArrayList<SDbTaxTable> readTaxTables() throws Exception {
        ArrayList<SDbTaxTable> registries = new ArrayList<>();

        String sql = "SELECT id_tax "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_TAX) + " "
                + "WHERE NOT b_del "
                + "ORDER BY dt_sta, id_tax;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbTaxTable registry = new SDbTaxTable();
                registry.read(moSession, new int[] { resultSet.getInt(1) });
                registries.add(registry);
            }
        }

        return registries;
    }

    private ArrayList<SDbTaxSubsidyTable> readTaxSubsidyTables() throws Exception {
        ArrayList<SDbTaxSubsidyTable> registries = new ArrayList<>();

        String sql = "SELECT id_tax_sub "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_TAX_SUB) + " "
                + "WHERE NOT b_del "
                + "ORDER BY dt_sta, id_tax_sub;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbTaxSubsidyTable registry = new SDbTaxSubsidyTable();
                registry.read(moSession, new int[] { resultSet.getInt(1) });
                registries.add(registry);
            }
        }

        return registries;
    }

    private ArrayList<SDbSsContributionTable> readSsContributionTables() throws Exception {
        ArrayList<SDbSsContributionTable> registries = new ArrayList<>();

        String sql = "SELECT id_ssc "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_SSC) + " "
                + "WHERE NOT b_del "
                + "ORDER BY dt_sta, id_ssc;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbSsContributionTable registry = new SDbSsContributionTable();
                registry.read(moSession, new int[] { resultSet.getInt(1) });
                registries.add(registry);
            }
        }

        return registries;
    }
    
    private ArrayList<SDbBenefitTable> readBenefitTables() throws Exception {
        ArrayList<SDbBenefitTable> registries = new ArrayList<>();

        String sql = "SELECT id_ben "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN) + " "
                + "WHERE NOT b_del "
                + "ORDER BY dt_sta DESC, id_ben;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbBenefitTable registry = new SDbBenefitTable();
                registry.read(moSession, new int[] { resultSet.getInt(1) });
                registries.add(registry);
            }
        }

        return registries;
    }
    
    private ArrayList<SHrsBenefitTableAnniversary> createBenefitTableAnniversarys(ArrayList<SDbBenefitTable> benefitTables) throws Exception {
        return SHrsUtils.createBenefitTablesAnniversaries(benefitTables);
    }

    private ArrayList<SDbMwzTypeWage> readMwzTypeWages() throws Exception {
        ArrayList<SDbMwzTypeWage> registries = new ArrayList<>();

        String sql = "SELECT id_tp_mwz, id_wage "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_MWZ_WAGE) + " "
                + "WHERE NOT b_del "
                + "ORDER BY dt_sta, id_tp_mwz, id_wage;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbMwzTypeWage registry = new SDbMwzTypeWage();
                registry.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
                registries.add(registry);
            }
        }

        return registries;
    }

    private ArrayList<SDbEarning> readEarnings() throws Exception {
        ArrayList<SDbEarning> registries = new ArrayList<>();

        String sql = "SELECT id_ear "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " "
                + "WHERE NOT b_del "
                + "ORDER BY CONCAT(code, ' - ', name), id_ear;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbEarning registry = new SDbEarning();
                registry.read(moSession, new int[] { resultSet.getInt(1) });
                registries.add(registry);
            }
        }

        return registries;
    }

    private ArrayList<SDbDeduction> readDeductions() throws Exception {
        ArrayList<SDbDeduction> registries = new ArrayList<>();

        String sql = "SELECT id_ded "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " "
                + "WHERE NOT b_del "
                + "ORDER BY CONCAT(code, ' - ', name), id_ded;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbDeduction deduction = new SDbDeduction();
                deduction.read(moSession, new int[] { resultSet.getInt(1) });
                registries.add(deduction);
            }
        }

        return registries;
    }

    private ArrayList<SDbAutomaticEarning> readAutomaticEarnings(final int paymentType) throws Exception {
        ArrayList<SDbAutomaticEarning> registries = new ArrayList<>();

        // employee automatic earnings (mayor precedence, can hide global automatic earnings):

        String sql = "SELECT ae.id_ear, ae.id_aut "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_EAR) + " AS ae "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON ae.fk_emp_n = e.id_emp "
                + "WHERE NOT ae.b_del AND e.fk_tp_pay = " + paymentType + " "
                + "ORDER BY ae.id_ear, ae.id_aut;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbAutomaticEarning registry = new SDbAutomaticEarning();
                registry.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
                registries.add(registry);
            }
        }

        // global automatic earnings (minor precedence):

        sql = "SELECT id_ear, id_aut "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_EAR) + " "
                + "WHERE NOT b_del AND fk_emp_n IS NULL "
                + "ORDER BY id_ear, id_aut;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbAutomaticEarning registry = new SDbAutomaticEarning();
                registry.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
                registries.add(registry);
            }
        }

        return registries;
    }

    private ArrayList<SDbAutomaticDeduction> readAutomaticDeductions(final int paymentType) throws Exception {
        ArrayList<SDbAutomaticDeduction> registries = new ArrayList<>();

        // employee automatic deductions (mayor precedence, can hide global automatic deductions):

        String sql = "SELECT ad.id_ded, ad.id_aut "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_DED) + " AS ad "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON ad.fk_emp_n = e.id_emp "
                + "WHERE NOT ad.b_del AND e.fk_tp_pay = " + paymentType + " "
                + "ORDER BY ad.id_ded, ad.id_aut;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbAutomaticDeduction registry = new SDbAutomaticDeduction();
                registry.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
                registries.add(registry);
            }
        }

        // global automatic deductions (minor precedence):

        sql = "SELECT id_ded, id_aut "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_DED) + " "
                + "WHERE NOT b_del AND fk_emp_n IS NULL "
                + "ORDER BY id_ded, id_aut;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbAutomaticDeduction registry = new SDbAutomaticDeduction();
                registry.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
                registries.add(registry);
            }
        }

        return registries;
    }

    private ArrayList<SDbEmployee> readEmployees(final int paymentType, final int payrollId) throws Exception {
        ArrayList<SDbEmployee> registries = new ArrayList<>();
        
        String sql = "SELECT e.id_emp "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e "
                + "WHERE NOT e.b_del AND (e.fk_tp_pay = " + paymentType + " "
                + (payrollId == 0 ? 
                "AND e.id_emp IN ("
                + " SELECT em.id_emp "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " AS em "
                + " ORDER BY em.id_emp) " :
                "OR e.id_emp IN ("
                + " SELECT pr.id_emp "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + " INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + " WHERE p.id_pay = " + payrollId + " AND NOT pr.b_del "
                + " ORDER BY pr.id_emp)") + ") "
                + "ORDER BY e.id_emp;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbEmployee registry = new SDbEmployee();
                registry.read(moSession, new int[] { resultSet.getInt(1) });
                registries.add(registry);
            }
        }

        return registries;
    }

    private ArrayList<SHrsReceipt> createHrsReceipts(final SHrsPayroll hrsPayroll, final int payrollId, final boolean isCopy, final boolean isNew) throws Exception {
        ArrayList<SHrsReceipt> hrsReceipts = new ArrayList<>();

        if (!isNew) {
            String sql = "SELECT id_emp " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " " +
                    "WHERE id_pay = " + payrollId + " AND NOT b_del " +
                    "ORDER BY id_emp;";

            try (ResultSet resultSet = moSession.getStatement().getConnection().createStatement().executeQuery(sql)) {
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
                            hrsPayroll.getPayroll().getPeriodYear(), hrsPayroll.getPayroll().getPeriod(), hrsPayroll.getPayroll().getFiscalYear(), hrsPayroll.getPayroll().getDateStart(), hrsPayroll.getPayroll().getDateEnd());
                    hrsEmployee.setHrsReceipt(hrsReceipt);
                    hrsReceipt.setHrsEmployee(hrsEmployee);
                    hrsReceipt.setPayrollReceipt(payrollReceipt);
                    
                    hrsReceipts.add(hrsReceipt);
                }
            }
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
        try (ResultSet resultSet = moSession.getStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                map.put(resultSet.getInt(1), resultSet.getString(2));
            }
        }
    }
    
    /*
     * Specific methods for an individual employee
     */

    private ArrayList<SDbLoan> readEmployeeLoans(final int employeeId) throws Exception {
        ArrayList<SDbLoan> loans = new ArrayList<>();

        String sql = "SELECT id_emp, id_loan "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_LOAN) + " "
                + "WHERE id_emp = " + employeeId + " AND NOT b_del AND NOT b_clo "
                + "ORDER BY id_loan;";
        
        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbLoan loan = new SDbLoan();
                loan.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
                loans.add(loan);
            }
        }

        return loans;
    }
    
    private ArrayList<SHrsLoan> createEmployeeHrsLoans(final ArrayList<SDbLoan> loans, final int excludePayrollId) throws Exception {
        String sql;
        ArrayList<SHrsLoan> hrsLoans = new ArrayList<>();
        
        /* Get loan refunds:
         * NOTE: please remember that loan adjustments are stored in payroll with ID = 0, 
         *  so make sure that these rows are allways included!
         */
        
        sql = "SELECT pre.id_pay, pre.id_emp, pre.id_mov "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                + "WHERE (p.id_pay = 0 OR NOT p.b_del) AND NOT pr.b_del AND NOT pre.b_del AND "
                + (excludePayrollId == 0 ? "" : "p.id_pay <> " + excludePayrollId + " AND ") // do not exclude loan adjustments!
                + "pre.fk_loan_emp_n = ? AND pre.fk_loan_loan_n = ?;";
        PreparedStatement psEarnings = miStatement.getConnection().prepareStatement(sql);
        
        /* Get loan payments:
         * NOTE: please remember that loan adjustments are stored in payroll with ID = 0, 
         *  so make sure that these rows are allways included!
         */
        
        sql = "SELECT prd.id_pay, prd.id_emp, prd.id_mov "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp "
                + "WHERE (p.id_pay = 0 OR NOT p.b_del) AND NOT pr.b_del AND NOT prd.b_del AND "
                + (excludePayrollId == 0 ? "" : "p.id_pay <> " + excludePayrollId + " AND ") // do not exclude loan adjustments!
                + "prd.fk_loan_emp_n = ? AND prd.fk_loan_loan_n = ?;";
        PreparedStatement psDeductions = miStatement.getConnection().prepareStatement(sql);
        
        for (SDbLoan loan : loans) {
            SHrsLoan hrsLoan = new SHrsLoan(loan);
            
            psEarnings.setInt(1, loan.getPkEmployeeId());
            psEarnings.setInt(2, loan.getPkLoanId());

            try (ResultSet resultSet = psEarnings.executeQuery()) {
                while (resultSet.next()) {
                    SDbPayrollReceiptEarning payrollReceiptEarning = new SDbPayrollReceiptEarning();
                    payrollReceiptEarning.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
                    hrsLoan.getPayrollReceiptEarnings().add(payrollReceiptEarning);
                }
            }
            
            psDeductions.setInt(1, loan.getPkEmployeeId());
            psDeductions.setInt(2, loan.getPkLoanId());

            try (ResultSet resultSet = psDeductions.executeQuery()) {
                while (resultSet.next()) {
                    SDbPayrollReceiptDeduction payrollReceiptDeduction = new SDbPayrollReceiptDeduction();
                    payrollReceiptDeduction.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
                    hrsLoan.getPayrollReceiptDeductions().add(payrollReceiptDeduction);
                }
            }
            
            hrsLoans.add(hrsLoan);
        }
        
        psEarnings.close();
        psDeductions.close();

        return hrsLoans;
    }

    private ArrayList<SDbAbsence> readEmployeeAbsences(final int employeeId) throws Exception {
        ArrayList<SDbAbsence> absences = new ArrayList<>();

        String sql = "SELECT id_emp, id_abs " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ABS) + " " +
            "WHERE id_emp = " + employeeId + " AND NOT b_del AND NOT b_clo " +
            "ORDER BY dt_sta, id_emp, id_abs;";

        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SDbAbsence absence = new SDbAbsence();
                absence.read(moSession, new int[] { resultSet.getInt(1), resultSet.getInt(2) });
                absences.add(absence);
            }
        }
        
        return absences;
    }
    
    private ArrayList<SDbAbsenceConsumption> readEmployeeAbsencesConsumptions(final ArrayList<SDbAbsence> absences, final int excludePayrollId) throws Exception {
        ArrayList<SDbAbsenceConsumption> absencesConsumptions = new ArrayList<>();

        String sql = "SELECT ac.id_cns "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_ABS_CNS) + " AS ac ON ac.fk_rcp_pay = pr.id_pay AND ac.fk_rcp_emp = pr.id_emp "
                + "WHERE p.id_pay <> " + excludePayrollId + " AND NOT p.b_del AND NOT pr.b_del AND "
                + "ac.id_emp = ? AND ac.id_abs = ? AND NOT ac.b_del "
                + "ORDER BY ac.id_cns;";
        
        try (PreparedStatement psConsumptions = miStatement.getConnection().prepareStatement(sql)) {
            for (SDbAbsence absence : absences) {
                psConsumptions.setInt(1, absence.getPkEmployeeId());
                psConsumptions.setInt(2, absence.getPkAbsenceId());
                
                try (ResultSet resultSet = psConsumptions.executeQuery()) {
                    while (resultSet.next()) {
                        SDbAbsenceConsumption absenceConsumption = new SDbAbsenceConsumption();
                        absenceConsumption.read(moSession, new int[] { absence.getPkEmployeeId(), absence.getPkAbsenceId(), resultSet.getInt(1) });
                        absencesConsumptions.add(absenceConsumption);
                    }
                }
            }
        }
        
        return absencesConsumptions;
    }

    private ArrayList<SDbEmployeeHireLog> readEmployeeHireLogs(final int employeeId, final Date dateStart, final Date dateEnd)  throws Exception {
        return SHrsUtils.readEmployeeHireLogs(moSession, miStatement, employeeId, dateStart, dateEnd);
    }
    
    private int getEmployeeHiredDays(final ArrayList<SDbEmployeeHireLog> employeeHireLogs, final Date dateStart, final Date dateEnd) throws Exception {
        int hiredDays = 0;
        
        for (SDbEmployeeHireLog entry : employeeHireLogs) {
            hiredDays += SLibTimeUtils.countPeriodDays(
                    entry.getDateHire().compareTo(dateStart) <= 0 ? dateStart : entry.getDateHire(), 
                    entry.getDateDismissal_n() == null || entry.getDateDismissal_n().compareTo(dateEnd) >= 0 ? dateEnd : entry.getDateDismissal_n());
        }
        
        return hiredDays;
    }
    
    private double getEmployeeAnnualTaxableEarnings(final int employeeId, final int recruitmentSchemaCat, final int fiscalYear, final Date payrollDateEnd, int taxType, final int excludePayrollId) throws Exception {
        double earnings = 0;
        String sqlAltTaxArt174;
        
        switch (taxType) {
            case TAX_STD:
                sqlAltTaxArt174 = "0";
                break;
            case TAX_ART_174:
                sqlAltTaxArt174 = "1";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        String sql = "SELECT COALESCE(SUM(pre.amt_taxa), 0.0) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pr.id_pay = pre.id_pay AND pr.id_emp = pre.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS trs ON pr.fk_tp_rec_sche = trs.id_tp_rec_sche "
                + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del AND "
                + "p.id_pay <> " + excludePayrollId + " AND pr.id_emp = " + employeeId + " AND trs.rec_sche_cat = " + recruitmentSchemaCat + " AND "
                + "p.fis_year = " + fiscalYear + " AND p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(payrollDateEnd) + "' AND "
                + "pre.b_alt_tax = " + sqlAltTaxArt174 + ";"; // Articule 174 RLISR
        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            if (resultSet.next()) {
                earnings = resultSet.getDouble(1);
            }
        }

        return earnings;
    }
    
    private double getEmployeeAnnualCompensation(final int employeeId, final int recruitmentSchemaCat, final int fiscalYear, final Date payrollDateEnd, int compensationType, final int excludePayrollId) throws Exception {
        double compensation = 0;
        String sqlColumn;
        
        switch (compensationType) {
            case COMP_TAX:
                sqlColumn = "pr.pay_tax_comp";
                break;
            case COMP_TAX_SUB:
                sqlColumn = "pr.pay_tax_sub_comp";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        String sql = "SELECT COALESCE(SUM(" + sqlColumn + "), 0.0) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS trs ON pr.fk_tp_rec_sche = trs.id_tp_rec_sche "
                + "WHERE NOT p.b_del AND NOT pr.b_del AND "
                + "p.id_pay <> " + excludePayrollId + " AND pr.id_emp = " + employeeId + " AND trs.rec_sche_cat = " + recruitmentSchemaCat + " AND "
                + "p.fis_year = " + fiscalYear + " AND p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(payrollDateEnd) + "';";
        
        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            if (resultSet.next()) {
                compensation = resultSet.getDouble(1);
            }
        }

        return compensation;
    }
    
    private int getEmployeeBusinessDays(final ArrayList<SDbEmployeeHireLog> employeeHireLogs, final int paymentType, final Date dateStart, final Date dateEnd) throws Exception {
        int businessDays = 0;
        
        for (SDbEmployeeHireLog entry : employeeHireLogs) {
            businessDays += SHrsUtils.countBusinessDays(
                    entry.getDateHire().compareTo(dateStart) <= 0 ? dateStart : entry.getDateHire(), 
                    entry.getDateDismissal_n() == null || entry.getDateDismissal_n().compareTo(dateEnd) >= 0 ? dateEnd : entry.getDateDismissal_n(), 
                    moWorkingDaySettingsMap.get(paymentType), 
                    readHolidays(dateStart, dateEnd));
        }
        
        return businessDays;
    }
    
    /**
     * Get category or recruitment schema for given payroll receipt.
     * @param payrollId ID of payroll.
     * @param employeeId ID of employee.
     * @return
     * @throws Exception 
     */
    private int getRecruitmentSchemaCat(final int payrollId, final int employeeId) throws Exception {
        int category = 0;
        
        String sql = "SELECT COUNT(*) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr "
                + "WHERE pr.id_pay = " + payrollId + " AND pr.id_emp = " + employeeId + " AND NOT pr.b_del;";
        
        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            if (resultSet.next()) {
                if (payrollId == 0 || resultSet.getInt(1) == 0) {
                    // el empleado no tiene recibo en la nómina aún:
                    // obtener la categoría de su esquema de contratación ya sea de su membresía en la empresa o de su propio registro...
                    
                    sql = "SELECT COALESCE(trsem.rec_sche_cat, trse.rec_sche_cat) AS _rec_sche_cat "
                            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e "
                            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " AS em ON em.id_emp = e.id_emp "
                            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS trse ON trse.id_tp_rec_sche = e.fk_tp_rec_sche "
                            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS trsem ON trsem.id_tp_rec_sche = em.fk_tp_rec_sche_n "
                            + "WHERE e.id_emp = " + employeeId + " AND NOT e.b_del;";

                    try (ResultSet resultSet1 = miStatement.executeQuery(sql)) {
                        if (!resultSet1.next()) {
                            throw new Exception("No fue posible determinar la categoría del régimen de contratación del empleado ID = " + employeeId + ".");
                        }
                        else {
                            category = resultSet1.getInt("_rec_sche_cat");
                        }
                    }
                }
                else {
                    // el empleado ya tiene recibo en la nómina:
                    // obtener la categoría de su esquema de contratación directamente de su recibo...
                    
                    sql = "SELECT trs.rec_sche_cat "
                            + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr "
                            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS trs ON trs.id_tp_rec_sche = pr.fk_tp_rec_sche "
                            + "WHERE pr.id_pay = " + payrollId + " AND pr.id_emp = " + employeeId + " AND NOT pr.b_del;";

                    try (ResultSet resultSet1 = miStatement.executeQuery(sql)) {
                        if (!resultSet1.next()) {
                            throw new Exception("No fue posible determinar la categoría del régimen de contratación del empleado ID = " + employeeId + ", nómina ID = " + payrollId + ".");
                        }
                        else {
                            category = resultSet1.getInt("trs.rec_sche_cat");
                        }
                    }
                }
            }
        }
        
        return category;
    }

    /**
     * Retrieve accumulated earnings for arguments provided ONLY that accountable for compatible category of recruitment schema in payroll receipts.
     * @param employeeId ID of employee.
     * @param periodYear Period year.
     * @param dateStart Start date.
     * @param dateEnd End date.
     * @param taxComputationType Tax computation type (SModSysConsts.HRSS_TP_TAX_COMP_...) or zero value to accumulate earnings up to end date.
     * @param byEarningType Is accumulation requested by earning type instead of by earning.
     * @param excludePayrollId ID of payroll to be excluded.
     * @return
     * @throws Exception 
     */
    private ArrayList<SHrsAccumulatedEarning> createEmployeeAccumulatedEarnings(final int employeeId, 
            final int periodYear, final Date dateEnd, final boolean byEarningType, final int excludePayrollId) throws Exception {
        int recrutimentSchemaCat = getRecruitmentSchemaCat(excludePayrollId, employeeId);
        ArrayList<SHrsAccumulatedEarning> hrsAccumulatedEarnings = new ArrayList<>();

        String field = (!byEarningType ? "pre.fk_ear" : "pre.fk_tp_ear");
        String sql = "SELECT " + field + " AS _ear, SUM(pre.amt_r) AS _amount, SUM(pre.amt_taxa) AS _taxa "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pr.id_pay = pre.id_pay AND pr.id_emp = pre.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS trs ON pr.fk_tp_rec_sche = trs.id_tp_rec_sche "
                + "WHERE p.b_del = 0 AND pr.b_del = 0 AND pre.b_del = 0 AND p.id_pay <> " + excludePayrollId + " AND pr.id_emp = " + employeeId + " AND "
                + "p.per_year = " + periodYear + " AND p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' AND "
                + "trs.rec_sche_cat = " + recrutimentSchemaCat + " "
                + "GROUP BY " + field + " "
                + "ORDER BY " + field + ";";
        
        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SHrsAccumulatedEarning hrsAccumulatedEarning = new SHrsAccumulatedEarning(resultSet.getInt("_ear"), resultSet.getDouble("_amount"), resultSet.getDouble("_taxa"));
                hrsAccumulatedEarnings.add(hrsAccumulatedEarning);
            }
        }

        return hrsAccumulatedEarnings;
    }

    /**
     * Retrieve accumulated deductions for arguments provided ONLY that accountable for compatible category of recruitment schema in payroll receipts.
     * @param employeeId ID of employee.
     * @param periodYear Period year.
     * @param dateStart Start date.
     * @param dateEnd End date.
     * @param taxComputationType Tax computation type (SModSysConsts.HRSS_TP_TAX_COMP_...) or zero value to accumulate deductions up to end date.
     * @param byDeductionType Is accumulation requested by deduction type instead of by deduction.
     * @param excludePayrollId ID of payroll to be excluded.
     * @return
     * @throws Exception 
     */
    private ArrayList<SHrsAccumulatedDeduction> createEmployeeAccumulatedDeductions(final int employeeId, 
            final int periodYear, final Date dateEnd, final boolean byDeductionType, final int excludePayrollId) throws Exception {
        int recrutimentSchemaCat = getRecruitmentSchemaCat(excludePayrollId, employeeId);
        ArrayList<SHrsAccumulatedDeduction> hrsAccumulatedDeductions = new ArrayList<>();

        String field = (!byDeductionType ? "prd.fk_ded" : "prd.fk_tp_ded");
        String sql = "SELECT " + field + " AS _ded, SUM(prd.amt_r) AS _amount "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON pr.id_pay = prd.id_pay AND pr.id_emp = prd.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS trs ON pr.fk_tp_rec_sche = trs.id_tp_rec_sche "
                + "WHERE p.b_del = 0 AND pr.b_del = 0 AND prd.b_del = 0 AND p.id_pay <> " + excludePayrollId + " AND pr.id_emp = " + employeeId + " AND "
                + "p.per_year = " + periodYear + " AND p.dt_end <= '" + SLibUtils.DbmsDateFormatDate.format(dateEnd) + "' AND "
                + "trs.rec_sche_cat = " + recrutimentSchemaCat + " "
                + "GROUP BY " + field + " "
                + "ORDER BY " + field + ";";
        
        try (ResultSet resultSet = miStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                SHrsAccumulatedDeduction hrsAccumulatedDeduction = new SHrsAccumulatedDeduction(resultSet.getInt("_ded"), resultSet.getDouble("_amount"));
                hrsAccumulatedDeductions.add(hrsAccumulatedDeduction);
            }
        }

        return hrsAccumulatedDeductions;
    }

    /*
     * Public methods
     */

    public SGuiSession getSession() {
        return moSession;
    }

    public SHrsPayroll createHrsPayroll(final SDbConfig config, final SDbWorkingDaySettings workingDaySettings, final SDbPayroll payroll, final boolean isCopy) throws Exception {
        SHrsPayroll hrsPayroll = new SHrsPayroll(this, config, workingDaySettings, payroll);

        // Adjustments by loan type:
        hrsPayroll.getLoanTypeAdjustments().addAll(readLoanTypeAdjustment());

        // UMA:
        hrsPayroll.getUmas().addAll(readUmas());
        
        // UMI:
        hrsPayroll.getUmis().addAll(readUmis());
        
        // Holidays:
        hrsPayroll.getHolidays().addAll(readHolidays(payroll.getPeriodYear()));

        // Tax tables:
        hrsPayroll.getTaxTables().addAll(readTaxTables());

        // Tax subsidy tables:
        hrsPayroll.getTaxSubsidyTables().addAll(readTaxSubsidyTables());

        // SS Contribution tables:
        hrsPayroll.getSsContributionTables().addAll(readSsContributionTables());
        
        // Benefit tables:
        hrsPayroll.getBenefitTables().addAll(readBenefitTables());
        
        // Benefit tables:
        hrsPayroll.getHrsBenefitTablesAnniversarys().addAll(createBenefitTableAnniversarys((readBenefitTables())));

        // MWZ type wages:
        hrsPayroll.getMwzTypeWages().addAll(readMwzTypeWages());

        // Earnings:
        hrsPayroll.getEarnings().addAll(readEarnings());

        // Deductions:
        hrsPayroll.getDeductions().addAll(readDeductions());

        // Automatic earnings:
        hrsPayroll.getAutomaticEarnings().addAll(readAutomaticEarnings(payroll.getFkPaymentTypeId()));

        // Automatic deductions:
        hrsPayroll.getAutomaticDeductions().addAll(readAutomaticDeductions(payroll.getFkPaymentTypeId()));

        // Employees:
        hrsPayroll.getEmployees().addAll(readEmployees(payroll.getFkPaymentTypeId(), payroll.getPkPayrollId()));

        // Receipts:
        hrsPayroll.getHrsReceipts().addAll(createHrsReceipts(hrsPayroll, payroll.getPkPayrollId(), isCopy, payroll.isRegistryNew()));
        
        // Earning computation types:
        populateDescriptionsMap(SModConsts.HRSS_TP_EAR_COMP, SDbRegistry.FIELD_CODE, hrsPayroll.getEarningComputationTypesMap());
        
        // Deduction computation types:
        populateDescriptionsMap(SModConsts.HRSS_TP_DED_COMP, SDbRegistry.FIELD_CODE, hrsPayroll.getDeductionComputationTypesMap());

        return hrsPayroll;
    }

    public SHrsEmployee computeEmployee(final SHrsEmployee srcHrsEmployee, final int payrollId, final int employeeId, 
            final int payrollYear, final int payrollYearPeriod, final int fiscalYear, final Date dateStart, final Date dateEnd) throws Exception {
        SHrsEmployee newHrsEmployee = new SHrsEmployee(payrollYear, payrollYearPeriod, dateStart, dateEnd);

        newHrsEmployee.setEmployee(srcHrsEmployee.getEmployee());
        newHrsEmployee.setHrsReceipt(srcHrsEmployee.getHrsReceipt());
        newHrsEmployee.getLoans().addAll(readEmployeeLoans(employeeId));
        newHrsEmployee.getHrsLoans().addAll(createEmployeeHrsLoans(newHrsEmployee.getLoans(), payrollId));
        newHrsEmployee.getAbsences().addAll(readEmployeeAbsences(employeeId));
        newHrsEmployee.getAbsenceConsumptions().addAll(readEmployeeAbsencesConsumptions(newHrsEmployee.getAbsences(), payrollId));
        newHrsEmployee.getEmployeeHireLogs().addAll(readEmployeeHireLogs(employeeId, dateStart, dateEnd));

        Date annualStart = SLibTimeUtils.getBeginOfYear(SLibTimeUtils.createDate(fiscalYear));
        Date annualEnd = SLibTimeUtils.getEndOfYear(SLibTimeUtils.createDate(fiscalYear)).compareTo(dateEnd) < 0 ? SLibTimeUtils.getEndOfYear(SLibTimeUtils.createDate(fiscalYear)) : dateEnd;
        int recruitmentSchemaCat = srcHrsEmployee.getEmployee().getXtaEffectiveRecruitmentSchemaCat(); // convenience variable

        newHrsEmployee.setDaysHiredAnnual(getEmployeeHiredDays(readEmployeeHireLogs(employeeId, annualStart, annualEnd), annualStart, annualEnd));
        newHrsEmployee.setAnnualTaxableEarnings(getEmployeeAnnualTaxableEarnings(employeeId, recruitmentSchemaCat, fiscalYear, annualEnd, TAX_STD, payrollId));
        newHrsEmployee.setAnnualTaxableEarningArt174(getEmployeeAnnualTaxableEarnings(employeeId, recruitmentSchemaCat, fiscalYear, annualEnd, TAX_ART_174, payrollId));
        newHrsEmployee.setAnnualTaxCompensated(getEmployeeAnnualCompensation(employeeId, recruitmentSchemaCat, fiscalYear, annualEnd, COMP_TAX, payrollId));
        newHrsEmployee.setAnnualTaxSubsidyCompensated(getEmployeeAnnualCompensation(employeeId, recruitmentSchemaCat, fiscalYear, annualEnd, COMP_TAX_SUB, payrollId));

        newHrsEmployee.setDaysHiredPayroll(getEmployeeHiredDays(newHrsEmployee.getEmployeeHireLogs(), dateStart, dateEnd));
        newHrsEmployee.setBusinessDays(getEmployeeBusinessDays(newHrsEmployee.getEmployeeHireLogs(), srcHrsEmployee.getEmployee().getFkPaymentTypeId(), dateStart, dateEnd));
        newHrsEmployee.setSeniority(SHrsUtils.getEmployeeSeniority(srcHrsEmployee.getEmployee().getDateBenefits(), dateEnd));
        newHrsEmployee.addAllYearHrsAccumulatedEarnigs(createEmployeeAccumulatedEarnings(employeeId, payrollYear, dateEnd, false, payrollId)); // by earning ID
        newHrsEmployee.addAllYearHrsAccumulatedEarnigsByType(createEmployeeAccumulatedEarnings(employeeId, payrollYear, dateEnd, true, payrollId)); // by earning type
        newHrsEmployee.addAllYearHrsAccumulatedDeductions(createEmployeeAccumulatedDeductions(employeeId, payrollYear, dateEnd, false, payrollId)); // by deduction ID
        newHrsEmployee.addAllYearHrsAccumulatedDeductionsByType(createEmployeeAccumulatedDeductions(employeeId, payrollYear, dateEnd, true, payrollId)); // by deduction type

        return newHrsEmployee;
    }

    public SHrsEmployee createHrsEmployee(final SHrsPayroll hrsPayroll, final int payrollId, final int employeeId, 
            final int payrollYear, final int payrollYearPeriod, final int fiscalYear, final Date dateStart, final Date dateEnd) throws Exception {
        SHrsEmployee hrsEmployee = new SHrsEmployee(payrollYear, payrollYearPeriod, dateStart, dateEnd);
        
        hrsEmployee.setEmployee(hrsPayroll.getEmployee(employeeId));
        hrsEmployee = computeEmployee(hrsEmployee, payrollId, employeeId, payrollYear, payrollYearPeriod, fiscalYear, dateStart, dateEnd);

        return hrsEmployee;
    }
}

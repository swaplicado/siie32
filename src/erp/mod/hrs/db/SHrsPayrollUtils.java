/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SHrsPayrollUtils {
    
    /**
     * Obtain row-payroll-employees that are available at the time.
     * Used only in <code>erp.mod.hrs.form.SFormPayroll</code>.
     * @param session
     * @param paymentType
     * @param payrollStart
     * @param payrollEnd
     * @param activesOnly
     * @param selectedEmployeesIds List of pre-selected employess.
     * @return List of available row-payroll-employees.
     * @throws Exception 
     */
    public static ArrayList<SRowPayrollEmployee> obtainRowPayrollEmployeesAvailable(final SGuiSession session, final int paymentType, final Date payrollStart, final Date payrollEnd, final boolean activesOnly, final ArrayList<Integer> selectedEmployeesIds) throws Exception {
        ArrayList<SRowPayrollEmployee> rows = new ArrayList<>();
        
        String[] employeesIds = new String[selectedEmployeesIds.size()];
        for (int i = 0; i < selectedEmployeesIds.size(); i++) {
            employeesIds[i] = "" + selectedEmployeesIds.get(i);
        }

        String sql = "SELECT b.bp, e.num, e.id_emp, e.b_act, " +
                "e.fk_tp_rec_sche, trse.code, trse.name, em.fk_tp_rec_sche_n, trsem.code, trsem.name " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " AS em ON em.id_emp = e.id_emp " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = e.id_emp " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS trse ON trse.id_tp_rec_sche = e.fk_tp_rec_sche " + // taken from employee
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS trsem ON trsem.id_tp_rec_sche = em.fk_tp_rec_sche_n " + // taken from employee's membership
                "WHERE NOT e.b_del AND NOT b.b_del AND e.fk_tp_pay = " + paymentType + " " +
                (!activesOnly ? "" : "AND e.b_act = 1 AND e.dt_hire <= '" + SLibUtils.DbmsDateFormatDate.format(payrollEnd) + "' ") +
                (selectedEmployeesIds.isEmpty() ? "" : "AND e.id_emp NOT IN (" + SLibUtils.textImplode(employeesIds, ", ") + ") ") +
                "ORDER BY b.bp, e.num, e.id_emp;";
        
        try (ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                SRowPayrollEmployee row = new SRowPayrollEmployee(SRowPayrollEmployee.CASE_EMPLOYEE, true); // recruitment schema icon required in erp.mod.hrs.form.SFormPayroll
                
                row.setPkEmployeeId(resultSet.getInt("e.id_emp"));
                row.setFkPaymentTypeId(paymentType);
                row.setNumber(resultSet.getString("e.num"));
                row.setName(resultSet.getString("b.bp"));
                row.setActive(resultSet.getBoolean("e.b_act"));
                
                // type of recruitment schema of employee:

                if (resultSet.getInt("em.fk_tp_rec_sche_n") != 0) {
                    // type of recruitment schema set in employee's membership:
                    row.setEmployeeRecruitmentSchemaTypeId(resultSet.getInt("em.fk_tp_rec_sche_n"));
                    row.setEmployeeRecruitmentSchemaType(resultSet.getString("trsem.code") + " - " + resultSet.getString("trsem.name"));
                }
                else {
                    // actual type of recruitment schema taken from directly from employee:
                    row.setEmployeeRecruitmentSchemaTypeId(resultSet.getInt("e.fk_tp_rec_sche"));
                    row.setEmployeeRecruitmentSchemaType(resultSet.getString("trse.code") + " - " + resultSet.getString("trse.name"));
                }
                
                // type of recruitment schema of receipt (same as employee's):

                row.setReceiptRecruitmentSchemaTypeId(row.getEmployeeRecruitmentSchemaTypeId());
                row.setReceiptRecruitmentSchemaType(row.getEmployeeRecruitmentSchemaType());
                
                rows.add(row);
            }
        }
        
        return rows;
    }

    /**
     * Obtain row-payroll-employees that belongs to given payroll.
     * Used only in <code>erp.mod.hrs.form.SDialogLayoutPayroll</code>.
     * @param session
     * @param idPayroll
     * @return List of row-payroll-employees from given payroll.
     * @throws Exception 
     */
    public static ArrayList<SRowPayrollEmployee> obtainRowPayrollEmployees(SGuiSession session, int idPayroll) throws Exception {
        ArrayList<SRowPayrollEmployee> rows = new ArrayList<>();

        String sql = "SELECT b.bp, e.num, e.id_emp, e.bank_acc, e.b_act, " +
                "pr.fk_tp_rec_sche, trspr.code, trspr.name, e.fk_tp_rec_sche, trse.code, trse.name, em.fk_tp_rec_sche_n, trsem.code, trsem.name, " +
                "pr.fk_tp_pay, .pr.ear_r, pr.ded_r, COALESCE(bank.name, '') AS _bank, COALESCE(e.fk_bank_n, 0) AS _bank_id " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON pr.id_emp = e.id_emp " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON e.id_emp = b.id_bp " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS trspr ON trspr.id_tp_rec_sche = pr.fk_tp_rec_sche " + // taken from receipt
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS trse ON trse.id_tp_rec_sche = e.fk_tp_rec_sche " + // taken from employee
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_BANK) + " AS bank ON bank.id_bank = e.fk_bank_n " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_MEMBER) + " AS em ON em.id_emp = e.id_emp " +
                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS trsem ON trsem.id_tp_rec_sche = em.fk_tp_rec_sche_n " + // taken from employee's membership
                "WHERE p.id_pay = " + idPayroll + " AND NOT pr.b_del " +
                "ORDER BY b.bp, e.num, e.id_emp;";
        
        try (ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                SRowPayrollEmployee row = new SRowPayrollEmployee(SRowPayrollEmployee.CASE_RECEIPT, false); // recruitment schema icon not required in erp.mod.hrs.form.SDialogLayoutPayroll
                
                row.setPkEmployeeId(resultSet.getInt("e.id_emp"));
                row.setFkPaymentTypeId(resultSet.getInt("pr.fk_tp_pay"));
                row.setNumber(resultSet.getString("e.num"));
                row.setName(resultSet.getString("b.bp"));
                row.setActive(resultSet.getBoolean("e.b_act"));
                
                // type of recruitment schema of employee:

                if (resultSet.getInt("em.fk_tp_rec_sche_n") != 0) {
                    // type of recruitment schema set in employee's membership:
                    row.setEmployeeRecruitmentSchemaTypeId(resultSet.getInt("em.fk_tp_rec_sche_n"));
                    row.setEmployeeRecruitmentSchemaType(resultSet.getString("trsem.code") + " - " + resultSet.getString("trsem.name"));
                }
                else {
                    // actual type of recruitment schema taken from directly from employee:
                    row.setEmployeeRecruitmentSchemaTypeId(resultSet.getInt("e.fk_tp_rec_sche"));
                    row.setEmployeeRecruitmentSchemaType(resultSet.getString("trse.code") + " - " + resultSet.getString("trse.name"));
                }
                
                // type of recruitment schema of receipt:

                row.setReceiptRecruitmentSchemaTypeId(resultSet.getInt("pr.fk_tp_rec_sche"));
                row.setReceiptRecruitmentSchemaType(resultSet.getString("trspr.code") + " - " + resultSet.getString("trspr.name"));
                
                // remaining receipt info:

                row.setTotalEarnings(resultSet.getDouble("pr.ear_r"));
                row.setTotalDeductions(resultSet.getDouble("pr.ded_r"));
                row.setBankId(resultSet.getInt("_bank_id"));
                row.setBank(resultSet.getString("_bank"));
                row.setBankAccount(resultSet.getString("e.bank_acc"));
                
                rows.add(row);
            }
        }
        
        return rows;
    }
    
    /**
     * Obtain row-payroll-employees contained in given list of receipts.
     * Used only in <code>erp.mod.hrs.form.SFormPayroll</code>.
     * @param session
     * @param hrsReceipts
     * @return List of row-payroll-employees from given list of receipts.
     */
    public static ArrayList<SRowPayrollEmployee> createRowPayrollEmployees(SGuiSession session, ArrayList<SHrsReceipt> hrsReceipts) {
        ArrayList<SRowPayrollEmployee> rows = new ArrayList<>();
        HashMap<Integer, String> recruitmentSchemaTypesMap = new HashMap<>(); // key: ID; value: name
        
        for (SHrsReceipt hrsReceipt : hrsReceipts) {
            int recruitmentSchemaTypeId;
            String recruitmentSchemaType;
            SRowPayrollEmployee row = new SRowPayrollEmployee(SRowPayrollEmployee.CASE_RECEIPT, true); // recruitment schema icon required in erp.mod.hrs.form.SFormPayroll
            
            row.setPkEmployeeId(hrsReceipt.getHrsEmployee().getEmployee().getPkEmployeeId());
            row.setFkPaymentTypeId(hrsReceipt.getPayrollReceipt().getFkPaymentTypeId());
            row.setNumber(hrsReceipt.getHrsEmployee().getEmployee().getNumber());
            row.setName(hrsReceipt.getHrsEmployee().getEmployee().getXtaEmployeeName());
            row.setActive(hrsReceipt.getPayrollReceipt().isActive());
            
            // type of recruitment schema of employee:
            
            recruitmentSchemaTypeId = hrsReceipt.getHrsEmployee().getEmployee().getXtaMembershipRecruitmentSchemaTypeId();
            recruitmentSchemaType = recruitmentSchemaTypesMap.get(recruitmentSchemaTypeId);
            
            if (recruitmentSchemaType == null) {
                recruitmentSchemaType = (String) session.readField(SModConsts.HRSS_TP_REC_SCHE, new int[] { recruitmentSchemaTypeId }, SDbRegistry.FIELD_NAME);
                recruitmentSchemaTypesMap.put(recruitmentSchemaTypeId, recruitmentSchemaType);
            }
            
            row.setEmployeeRecruitmentSchemaTypeId(recruitmentSchemaTypeId);
            row.setEmployeeRecruitmentSchemaType(recruitmentSchemaType);
            
            // type of recruitment schema of receipt:
            
            recruitmentSchemaTypeId = hrsReceipt.getPayrollReceipt().getFkRecruitmentSchemaTypeId();
            recruitmentSchemaType = recruitmentSchemaTypesMap.get(recruitmentSchemaTypeId);
            
            if (recruitmentSchemaType == null) {
                recruitmentSchemaType = (String) session.readField(SModConsts.HRSS_TP_REC_SCHE, new int[] { recruitmentSchemaTypeId }, SDbRegistry.FIELD_NAME);
                recruitmentSchemaTypesMap.put(recruitmentSchemaTypeId, recruitmentSchemaType);
            }
            
            row.setReceiptRecruitmentSchemaTypeId(recruitmentSchemaTypeId);
            row.setReceiptRecruitmentSchemaType(recruitmentSchemaType);
            
            // remaining receipt info:
            
            row.setTotalEarnings(hrsReceipt.getTotalEarnings());
            row.setTotalDeductions(hrsReceipt.getTotalDeductions());
            row.setHrsReceipt(hrsReceipt);

            rows.add(row);
        }
        
        return rows;
    }
}

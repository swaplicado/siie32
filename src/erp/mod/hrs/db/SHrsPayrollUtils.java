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
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SHrsPayrollUtils {
    
    public static ArrayList<SRowPayrollEmployee> obtainRowPayrollEmployeesAvailable(SGuiSession session, int idPayroll) throws Exception {
        ArrayList<SRowPayrollEmployee> rows = new ArrayList<>();

        String sql = "SELECT b.bp, e.num, e.id_emp, pr.ear_r, pr.ded_r " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON pr.id_emp = e.id_emp " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON e.id_emp = b.id_bp " +
            "WHERE p.id_pay = " + idPayroll + " AND NOT pr.b_del " +
            "ORDER BY b.bp, e.num, e.id_emp;";
        
        try (ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                SRowPayrollEmployee row = new SRowPayrollEmployee();
                
                row.setPkEmployeeId(resultSet.getInt("e.id_emp"));
                row.setCode(resultSet.getString("e.num"));
                row.setName(resultSet.getString("b.bp"));
                row.setTotalEarnings(resultSet.getDouble("pr.ear_r"));
                row.setTotalDeductions(resultSet.getDouble("pr.ded_r"));
                
                rows.add(row);
            }
        }
        
        return rows;
    }
    
    public static ArrayList<SRowPayrollEmployee> obtainRowPayrollEmployeesAvailable(final SGuiSession session, final int paymentType, final Date payrollStart, final Date payrollEnd, final boolean activeOnly, final ArrayList<Integer> selectedEmployees) throws Exception {
        ArrayList<SRowPayrollEmployee> rows = new ArrayList<>();
        
        String[] employees = new String[selectedEmployees.size()];
        for (int i = 0; i < selectedEmployees.size(); i++) {
            employees[i] = "" + selectedEmployees.get(i);
        }

        String sql = "SELECT b.bp, e.num, e.id_emp " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON b.id_bp = e.id_emp " +
                "WHERE NOT e.b_del AND NOT b.b_del AND e.fk_tp_pay = " + paymentType + " " +
                (!activeOnly ? "" : "AND e.b_act = 1 AND e.dt_hire <= '" + SLibUtils.DbmsDateFormatDate.format(payrollEnd) + "' ") +
                (selectedEmployees.isEmpty() ? "" : "AND e.id_emp NOT IN (" + SLibUtils.textImplode(employees, ", ") + ") ") +
                "ORDER BY b.bp, e.num, e.id_emp;";
        
        try (ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                SRowPayrollEmployee row = new SRowPayrollEmployee();
                row.setPkEmployeeId(resultSet.getInt("e.id_emp"));
                row.setFkPaymentTypeId(paymentType);
                row.setCode(resultSet.getString("e.num"));
                row.setName(resultSet.getString("b.bp"));
                rows.add(row);
            }
        }
        
        return rows;
    }
    
    public static ArrayList<SRowPayrollEmployee> createRowPayrollEmployeesSelected(ArrayList<SHrsReceipt> hrsReceipts) {
        ArrayList<SRowPayrollEmployee> rows = new ArrayList<>();
        
        for (SHrsReceipt hrsReceipt : hrsReceipts) {
            SRowPayrollEmployee row = new SRowPayrollEmployee();
            row.setPkEmployeeId(hrsReceipt.getHrsEmployee().getEmployee().getPkEmployeeId());
            row.setFkPaymentTypeId(hrsReceipt.getHrsEmployee().getEmployee().getFkPaymentTypeId());
            row.setCode(hrsReceipt.getHrsEmployee().getEmployee().getNumber());
            row.setName(hrsReceipt.getHrsEmployee().getEmployee().getAuxEmployee());
            row.setTotalEarnings(hrsReceipt.getTotalEarnings());
            row.setTotalDeductions(hrsReceipt.getTotalDeductions());
            row.setHrsReceipt(hrsReceipt);

            rows.add(row);
        }
        
        return rows;
    }
}

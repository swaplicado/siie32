/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos
 */
public class SHrsPayrollRowEmployeeAvailable implements SGridRow {

    protected int mnPkEmployeeId;
    protected int mnFkPaymentTypeId;
    protected String msCode;
    protected String msName;

    protected ArrayList<SHrsPayrollRowEmployeeAvailable> maEmployeesAvailable;

    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setFkPaymentTypeId(int n) { mnFkPaymentTypeId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }

    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getFkPaymentTypeId() { return mnFkPaymentTypeId; }
    public String getCode() { return msCode ; }
    public String getName() { return msName ; }

    public ArrayList<SHrsPayrollRowEmployeeAvailable> getHrsPayrollEmployeesAvailable() { return maEmployeesAvailable; }

    public SHrsPayrollRowEmployeeAvailable() {
        maEmployeesAvailable = new ArrayList<SHrsPayrollRowEmployeeAvailable>();

        mnPkEmployeeId = 0;
        mnFkPaymentTypeId = 0;
        msCode = "";
        msName = "";

        maEmployeesAvailable.clear();
    }

    public void obtainEmployeesAvailable(SGuiSession session, int paymentType, boolean active, Date datePayrollStart, Date datePayrollEnd, String employeesReceiptIds) throws SQLException, Exception {
        String sql = "";
        ResultSet resultSet = null;
        int employeeId = 0;
        ArrayList<SDbEmployeeHireLog> aEmployeeHireLogs = new ArrayList<SDbEmployeeHireLog>();
        SHrsPayrollRowEmployeeAvailable employeeAvailable = null;

        sql = "SELECT e.id_emp, e.fk_tp_pay, bp.bp, e.num " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON " +
            "bp.id_bp = e.id_emp " +
            "WHERE e.b_del = 0 AND e.fk_tp_pay = " + paymentType + " " + (!active ? "AND e.b_act = 1 AND e.dt_hire <= '" + SLibUtils.DbmsDateFormatDate.format(datePayrollEnd) + "' " : "") + " " +
                (employeesReceiptIds.isEmpty() ? "" : "AND e.id_emp NOT IN (" + employeesReceiptIds + "0)") + " " +
            "ORDER BY bp.bp, e.num ";

        resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql);
        maEmployeesAvailable.clear();
        while (resultSet.next()) {
            employeeId = resultSet.getInt("e.id_emp");

            aEmployeeHireLogs = SHrsUtils.getEmployeeHireLogs(session, employeeId, datePayrollStart, datePayrollEnd);

            if (!aEmployeeHireLogs.isEmpty() || (aEmployeeHireLogs.isEmpty() && !active)) {
                employeeAvailable = new SHrsPayrollRowEmployeeAvailable();

                employeeAvailable.setPkEmployeeId(employeeId);
                employeeAvailable.setFkPaymentTypeId(resultSet.getInt("e.fk_tp_pay"));
                employeeAvailable.setCode(resultSet.getString("e.num"));
                employeeAvailable.setName(resultSet.getString("bp.bp"));

                maEmployeesAvailable.add(employeeAvailable);
            }
        }
    }
    
    public void obtainEmployeesAvailableByPayroll(SGuiSession session, int idPayroll) throws Exception {
        String sql = "";
        ResultSet resultSet = null;
        SHrsPayrollRowEmployeeAvailable employeeAvailable = null;

        sql = "SELECT e.id_emp, bp.bp, e.num " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS hp " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS hpr ON hp.id_pay = hpr.id_pay " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS e ON hpr.id_emp = e.id_emp " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON e.id_emp = bp.id_bp " +
            "WHERE e.b_del = 0 AND hpr.id_pay = " + idPayroll + "  " +
            "ORDER BY bp.bp, e.num ";

        resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql);
        maEmployeesAvailable.clear();
        while (resultSet.next()) {
            employeeAvailable = new SHrsPayrollRowEmployeeAvailable();
            
            employeeAvailable.setPkEmployeeId(resultSet.getInt("e.id_emp"));
            employeeAvailable.setCode(resultSet.getString("e.num"));
            employeeAvailable.setName(resultSet.getString("bp.bp"));

            maEmployeesAvailable.add(employeeAvailable);
        }
    }

    @Override
    public int[] getRowPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRowEdited(final boolean edited) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch(row) {
            case 0:
                value = msName;
                break;
            case 1:
                value = msCode;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch(row) {
            case 0:
            case 1:
                break;
            default:
        }
    }
}

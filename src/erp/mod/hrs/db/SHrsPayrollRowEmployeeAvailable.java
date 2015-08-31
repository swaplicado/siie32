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
    protected String msCode;
    protected String msName;

    protected ArrayList<SHrsPayrollRowEmployeeAvailable> maEmployeesAvailable;

    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }

    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public String getCode() { return msCode ; }
    public String getName() { return msName ; }

    public ArrayList<SHrsPayrollRowEmployeeAvailable> getHrsPayrollEmployeesAvailable() { return maEmployeesAvailable; }

    public SHrsPayrollRowEmployeeAvailable() {
        maEmployeesAvailable = new ArrayList<SHrsPayrollRowEmployeeAvailable>();

        mnPkEmployeeId = 0;
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

        sql = "SELECT e.id_emp, bp.bp, e.num " +
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

            if (!aEmployeeHireLogs.isEmpty()) {
                employeeAvailable = new SHrsPayrollRowEmployeeAvailable();

                employeeAvailable.setPkEmployeeId(employeeId);
                employeeAvailable.setCode(resultSet.getString("e.num"));
                employeeAvailable.setName(resultSet.getString("bp.bp"));

                maEmployeesAvailable.add(employeeAvailable);
            }
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

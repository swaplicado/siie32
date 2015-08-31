/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.hrs.db;

import java.sql.SQLException;
import java.util.ArrayList;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SHrsPayrollRowEmployeeReceipt implements SGridRow {

    protected int mnPkEmployeeId;
    protected String msCode;
    protected String msName;
    protected double mdTotalEarnings;
    protected double mdTotalDeductions;
    protected double mdTotalNet;

    protected SHrsPayrollReceipt moHrsPayrollReceipt;
    protected ArrayList<SHrsPayrollRowEmployeeReceipt> maHrsEmployeeReceipts;

    public SHrsPayrollRowEmployeeReceipt() {
        mnPkEmployeeId = 0;
        msCode = "";
        msName = "";
        mdTotalEarnings = 0;
        mdTotalDeductions = 0;
        mdTotalNet = 0;

        moHrsPayrollReceipt = null;
        maHrsEmployeeReceipts = new ArrayList<SHrsPayrollRowEmployeeReceipt>();
    }

    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setTotalEarnings(double d) { mdTotalEarnings = d; }
    public void setTotalDeductions(double d) { mdTotalDeductions = d; }
    public void setTotalNet(double d) { mdTotalNet = d; }

    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public String getCode() { return msCode ; }
    public String getName() { return msName ; }
    public double getTotalEarnings() { return mdTotalEarnings ; }
    public double getTotalDeductions() { return mdTotalDeductions ; }
    public double getTotalNet() { return mdTotalNet ; }

    public void setHrsPayrollReceipt(SHrsPayrollReceipt o) { moHrsPayrollReceipt = o; }

    public SHrsPayrollReceipt getHrsPayrollReceipt() { return moHrsPayrollReceipt; }
    public ArrayList<SHrsPayrollRowEmployeeReceipt> getHrsPayrollEmployeesReceipt() { return maHrsEmployeeReceipts; }

    public void setEmployeeReceipts(ArrayList<SHrsPayrollReceipt> aHrsPayrollReceipt) throws SQLException, Exception {
        SHrsPayrollRowEmployeeReceipt employeeReceipt = null;

        maHrsEmployeeReceipts.clear();
        for (SHrsPayrollReceipt hrsPayrollReceipt : aHrsPayrollReceipt) {

            employeeReceipt = new SHrsPayrollRowEmployeeReceipt();

            employeeReceipt.setPkEmployeeId(hrsPayrollReceipt.getHrsEmployee().getEmployee().getPkEmployeeId());
            employeeReceipt.setCode(hrsPayrollReceipt.getHrsEmployee().getEmployee().getNumber());
            employeeReceipt.setName(hrsPayrollReceipt.getHrsEmployee().getEmployee().getAuxEmployee());
            employeeReceipt.setTotalEarnings(hrsPayrollReceipt.getTotalEarnings());
            employeeReceipt.setTotalDeductions(hrsPayrollReceipt.getTotalDeductions());
            employeeReceipt.setTotalNet(hrsPayrollReceipt.getTotalEarnings() - hrsPayrollReceipt.getTotalDeductions());
            employeeReceipt.setHrsPayrollReceipt(hrsPayrollReceipt);

            maHrsEmployeeReceipts.add(employeeReceipt);
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
            case 2:
                value = mdTotalEarnings;
                break;
            case 3:
                value = mdTotalDeductions;
                break;
            case 4:
                value = mdTotalNet;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

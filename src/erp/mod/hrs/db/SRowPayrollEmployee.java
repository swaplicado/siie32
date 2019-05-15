/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SRowPayrollEmployee implements SGridRow {

    protected int mnPkEmployeeId;
    protected int mnFkPaymentTypeId;
    protected String msCode;
    protected String msName;
    protected double mdTotalEarnings;
    protected double mdTotalDeductions;
    protected SHrsReceipt moHrsReceipt;

    public SRowPayrollEmployee() {
        mnPkEmployeeId = 0;
        mnFkPaymentTypeId = 0;
        msCode = "";
        msName = "";
        mdTotalEarnings = 0;
        mdTotalDeductions = 0;
        moHrsReceipt = null;
    }
    
    public SRowPayrollEmployee(SRowPayrollEmployee row) {
        mnPkEmployeeId = row.getPkEmployeeId();
        mnFkPaymentTypeId = row.getFkPaymentTypeId();
        msCode = row.getCode();
        msName = row.getName();
        mdTotalEarnings = row.getTotalEarnings();
        mdTotalDeductions = row.getTotalDeductions();
        moHrsReceipt = row.getHrsReceipt();
    }

    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setFkPaymentTypeId(int n) { mnFkPaymentTypeId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setTotalEarnings(double d) { mdTotalEarnings = d; }
    public void setTotalDeductions(double d) { mdTotalDeductions = d; }
    public void setHrsReceipt(SHrsReceipt o) { moHrsReceipt = o; }

    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getFkPaymentTypeId() { return mnFkPaymentTypeId; }
    public String getCode() { return msCode ; }
    public String getName() { return msName ; }
    public double getTotalEarnings() { return mdTotalEarnings ; }
    public double getTotalDeductions() { return mdTotalDeductions ; }
    public SHrsReceipt getHrsReceipt() { return moHrsReceipt; }

    public double getTotalNet() {
        return SLibUtils.roundAmount(mdTotalEarnings - mdTotalDeductions);
    }
    
    public void clearReceipt() {
        mdTotalEarnings = 0;
        mdTotalDeductions = 0;
        moHrsReceipt = null;
    }
    
    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkEmployeeId };
    }

    @Override
    public String getRowCode() {
        return getCode();
    }

    @Override
    public String getRowName() {
        return getName();
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
                value = getTotalNet();
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

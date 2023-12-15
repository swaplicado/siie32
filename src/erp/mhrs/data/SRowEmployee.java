/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mhrs.data;

import erp.mod.hrs.db.SDbAccountingPayrollReceipt;

/**
 *
 * @author Sergio Flores
 */
public class SRowEmployee extends erp.lib.table.STableRow {

    protected java.lang.String msEmployeeCategory;
    protected java.lang.String msEmployeeType;
    protected java.lang.String msSalaryType;
    protected java.lang.String msBank;
    protected double mdSalary;
    protected double mdPayment;
    protected int mnDaysWorked;
    protected int mnDaysNotWorked;
    protected int mnDaysPayed;
    protected int mnFkBizPartnerId;
    protected int mnFkPaymentSystemTypeId;
    protected SDbAccountingPayrollReceipt moAccountingPayrollReceipt;

    public SRowEmployee() {
        super();
    }

    public SRowEmployee(int[] pk, String code, String name) {
        super(pk, code, name);

        msEmployeeCategory = "";
        msEmployeeType = "";
        msSalaryType = "";
        msBank = "";
        mdSalary = 0;
        mdPayment = 0;
        mnDaysWorked = 0;
        mnDaysNotWorked = 0;
        mnDaysPayed = 0;
        mnFkBizPartnerId = 0;
        mnFkPaymentSystemTypeId = 0;
    }

    @Override
    public void prepareTableRow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEmployeeCategory(java.lang.String s) { msEmployeeCategory = s; }
    public void setEmployeeType(java.lang.String s) { msEmployeeType = s; }
    public void setSalaryType(java.lang.String s) { msSalaryType = s; }
    public void setBank(java.lang.String s) { msBank = s; }
    public void setSalary(double d) { mdSalary = d; }
    public void setPayment(double d) { mdPayment = d; }
    public void setDaysWorked(int n) { mnDaysWorked = n; }
    public void setDaysNotWorked(int n) { mnDaysNotWorked = n; }
    public void setDaysPayed(int n) { mnDaysPayed = n; }
    public void setFkBizPartnerId(int n) { mnFkBizPartnerId = n; }
    public void setFkPaymentSystemTypeId(int n) { mnFkPaymentSystemTypeId = n; }
    public void setAccountingPayrollReceipt(SDbAccountingPayrollReceipt o) { moAccountingPayrollReceipt = o; }

    public java.lang.String getEmployeeCategory() { return msEmployeeCategory; }
    public java.lang.String getEmployeeType() { return msEmployeeType; }
    public java.lang.String getSalaryType() { return msSalaryType; }
    public java.lang.String getBank() { return msBank; }
    public double getSalary() { return mdSalary; }
    public double getPayment() { return mdPayment; }
    public int getDaysWorked() { return mnDaysWorked; }
    public int getDaysNotWorked() { return mnDaysNotWorked; }
    public int getDaysPayed() { return mnDaysPayed; }
    public int getFkBizPartnerId() { return mnFkBizPartnerId; }
    public int getFkPaymentSystemTypeId() { return mnFkPaymentSystemTypeId; }
    public SDbAccountingPayrollReceipt getAccountingPayrollReceipt() { return moAccountingPayrollReceipt; }
}

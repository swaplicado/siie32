/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import java.util.ArrayList;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SHrsLoanPayments {

    protected SDbLoan moLoan;
    protected ArrayList<SDbPayrollReceiptEarning> maPayrollReceiptEarnings;
    protected ArrayList<SDbPayrollReceiptDeduction> maPayrollReceiptDeductions;
    protected double mdDaysPeriod = 0;
    protected double mdAmountPeriod = 0;

    public SHrsLoanPayments() {
        moLoan = new SDbLoan();
        maPayrollReceiptEarnings = new ArrayList<>();
        maPayrollReceiptDeductions = new ArrayList<>();
    }

    public void setLoan(SDbLoan o) { moLoan = o; }
    public void setDaysPeriod(double d) { mdDaysPeriod = d; }
    public void setAmountPeriod(double d) { mdAmountPeriod = d; }
    
    public SDbLoan getLoan() { return moLoan; }
    public double getAmountPeriod() { return mdAmountPeriod; }
    public double getDaysPeriod() { return mdDaysPeriod; }
    
    public ArrayList<SDbPayrollReceiptEarning> getPayrollReceiptEarnings() { return maPayrollReceiptEarnings; }
    public ArrayList<SDbPayrollReceiptDeduction> getPayrollReceiptDeductions() { return maPayrollReceiptDeductions; }
    
    public double getTotalEarnings() {
        double amount = 0;
        
        for (SDbPayrollReceiptEarning payrollReceiptEarning : maPayrollReceiptEarnings) {
            amount += payrollReceiptEarning.getAmount_r();
        }
        
        return amount;
    }
    
    public double getTotalPayment() {
        double payment = 0;
        
        for (SDbPayrollReceiptDeduction payrollReceiptDeduction : maPayrollReceiptDeductions) {
            payment += payrollReceiptDeduction.getAmount_r();
        }
        
        return payment;
    }
}

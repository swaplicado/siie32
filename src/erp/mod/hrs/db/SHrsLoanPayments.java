/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import java.util.ArrayList;

/**
 *
 * @author Juan Barajas
 */
public class SHrsLoanPayments {

    protected SDbLoan moLoan;
    protected ArrayList<SDbPayrollReceiptDeduction> maReceiptDeductions;
    protected double mdDaysPeriod = 0;
    protected double mdAmountPeriod = 0;

    public SHrsLoanPayments() {
        moLoan = new SDbLoan();
        maReceiptDeductions = new ArrayList<SDbPayrollReceiptDeduction>();
    }

    public void setLoan(SDbLoan o) { moLoan = o; }
    public void setDaysPeriod(double d) { mdDaysPeriod = d; }
    public void setAmountPeriod(double d) { mdAmountPeriod = d; }
    
    public SDbLoan getLoan() { return moLoan; }
    public double getAmountPeriod() { return mdAmountPeriod; }
    public double getDaysPeriod() { return mdDaysPeriod; }
    
    public ArrayList<SDbPayrollReceiptDeduction> getReceiptDeductions() { return maReceiptDeductions; }
    
    public double getTotalPayment() {
        double payment = 0;
        
        for (SDbPayrollReceiptDeduction deduction : maReceiptDeductions) {
            payment += deduction.getAmount_r();
        }
        
        return payment;
    }
}

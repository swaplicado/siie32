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
    protected ArrayList<SDbPayrollReceiptEarning> maReceiptEarnings;
    protected ArrayList<SDbPayrollReceiptDeduction> maReceiptDeductions;
    protected double mdDaysPeriod = 0;
    protected double mdAmountPeriod = 0;

    public SHrsLoanPayments() {
        moLoan = new SDbLoan();
        maReceiptEarnings = new ArrayList<SDbPayrollReceiptEarning>();
        maReceiptDeductions = new ArrayList<SDbPayrollReceiptDeduction>();
    }

    public void setLoan(SDbLoan o) { moLoan = o; }
    public void setDaysPeriod(double d) { mdDaysPeriod = d; }
    public void setAmountPeriod(double d) { mdAmountPeriod = d; }
    
    public SDbLoan getLoan() { return moLoan; }
    public double getAmountPeriod() { return mdAmountPeriod; }
    public double getDaysPeriod() { return mdDaysPeriod; }
    
    public ArrayList<SDbPayrollReceiptEarning> getReceiptEarnings() { return maReceiptEarnings; }
    public ArrayList<SDbPayrollReceiptDeduction> getReceiptDeductions() { return maReceiptDeductions; }
    
    public double getTotalEarnings() {
        double amount = 0;
        
        for (SDbPayrollReceiptEarning earning : maReceiptEarnings) {
            amount += earning.getAmount_r();
        }
        
        return amount;
    }
    
    public double getTotalPayment() {
        double payment = 0;
        
        for (SDbPayrollReceiptDeduction deduction : maReceiptDeductions) {
            payment += deduction.getAmount_r();
        }
        
        return payment;
    }
}

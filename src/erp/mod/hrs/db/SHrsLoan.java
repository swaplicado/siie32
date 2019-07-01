/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import java.util.ArrayList;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SHrsLoan {

    protected SDbLoan moLoan;
    protected ArrayList<SDbPayrollReceiptEarning> maPayrollReceiptEarnings;     // contains all earnings, except for current payroll receipt
    protected ArrayList<SDbPayrollReceiptDeduction> maPayrollReceiptDeductions; // contains all earnings, except for current payroll receipt

    public SHrsLoan() {
        moLoan = new SDbLoan();
        maPayrollReceiptEarnings = new ArrayList<>();
        maPayrollReceiptDeductions = new ArrayList<>();
    }

    public void setLoan(SDbLoan o) { moLoan = o; }
    
    public SDbLoan getLoan() { return moLoan; }
    
    public ArrayList<SDbPayrollReceiptEarning> getPayrollReceiptEarnings() { return maPayrollReceiptEarnings; }
    public ArrayList<SDbPayrollReceiptDeduction> getPayrollReceiptDeductions() { return maPayrollReceiptDeductions; }
    
    private double getRefunds() {
        double refunds = 0;
        
        for (SDbPayrollReceiptEarning earning : maPayrollReceiptEarnings) {
            refunds = SLibUtils.roundAmount(refunds + earning.getAmount_r());
        }
        
        return refunds;
    }
    
    private double getPayments() {
        double payments = 0;
        
        for (SDbPayrollReceiptDeduction deduction : maPayrollReceiptDeductions) {
            payments = SLibUtils.roundAmount(payments + deduction.getAmount_r());
        }
        
        return payments;
    }
    
    public double getLoanBalance() {
        return SLibUtils.roundAmount(moLoan.getTotalAmount() + getRefunds() - getPayments());
    }
}

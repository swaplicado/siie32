/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SFinAmount {
    
    public double Amount;
    public double AmountCy;
    public boolean IsPrepayment;
    public SFinMovement Movement;
    
    public int[] KeyRefDocument;
    public int[] KeyRefCashAccount;

    public SFinAmount() {
        this(0, 0, false, SFinMovement.INCREMENT);
    }

    public SFinAmount(SFinAmount amount) {
        this(amount.Amount, amount.AmountCy, amount.IsPrepayment, amount.Movement);
    }

    public SFinAmount(double amount, double amountCy) {
        this(amount, amountCy, false, SFinMovement.INCREMENT);
    }

    public SFinAmount(double amount, double amountCy, boolean isPrepayment, SFinMovement movement) {
        Amount = amount;
        AmountCy = amountCy;
        IsPrepayment = isPrepayment;
        Movement = movement;
        
        KeyRefDocument = null;
        KeyRefCashAccount = null;
    }
    
    public void addAmount(SFinAmount amount) {
        addAmount(amount.Amount, amount.AmountCy);
    }
    
    public void addAmount(double amount, double amountCy) {
        Amount += amount;
        AmountCy += amountCy;
    }
}

/*
 * Copyright © Software Aplicado SA de CV. All rights reserverd.
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
    public SFinAmountType AmountType;
    public SFinMovement Movement;
    
    public int[] KeyRefDocument;
    public int[] KeyRefCashAccount;

    public SFinAmount(SFinAmount amount) {
        this(amount.Amount, amount.AmountCy, amount.IsPrepayment, amount.AmountType, amount.Movement);
        KeyRefDocument = amount.KeyRefDocument;
        KeyRefCashAccount = amount.KeyRefCashAccount;
    }

    public SFinAmount(double amount, double amountCy) {
        this(amount, amountCy, false, SFinAmountType.UNDEFINED, SFinMovement.INCREMENT);
    }

    public SFinAmount(double amount, double amountCy, boolean isPrepayment, SFinAmountType amountType, SFinMovement movement) {
        Amount = amount;
        AmountCy = amountCy;
        IsPrepayment = isPrepayment;
        AmountType = amountType;
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

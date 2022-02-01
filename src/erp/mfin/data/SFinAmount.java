/*
 * Copyright © Software Aplicado SA de CV. All rights reserverd.
 */
package erp.mfin.data;

import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores, Edwin Carmona, Sergio Flores
 */
public class SFinAmount {
    
    public double Amount;
    public double AmountCy;
    public boolean IsPrepaymentInvoiced;
    public SFinAccountType AccountType;
    public SFinMovementType MovementType;
    public boolean OmitWhenAmountsChecked;
    
    public int[] RefDocumentKey;
    public int[] RefCashAccountKey;
    public int[] RefTaxKey;

    public SFinAmount(SFinAmount amount) {
        this(amount.Amount, amount.AmountCy, amount.IsPrepaymentInvoiced, amount.AccountType, amount.MovementType, amount.OmitWhenAmountsChecked);
        
        RefDocumentKey = amount.RefDocumentKey;
        RefCashAccountKey = amount.RefCashAccountKey;
        RefTaxKey = amount.RefTaxKey;
    }

    public SFinAmount(double amount, double amountCy) {
        this(amount, amountCy, false, SFinAccountType.ACC_BIZ_PARTNER, SFinMovementType.Increment, false);
    }

    public SFinAmount(double amount, double amountCy, boolean isPrepaymentInvoiced, SFinAccountType accountType, SFinMovementType movementType) {
        this(amount, amountCy, isPrepaymentInvoiced, accountType, movementType, false);
    }
    
    public SFinAmount(double amount, double amountCy, boolean isPrepaymentInvoiced, SFinAccountType accountType, SFinMovementType movementType, boolean omitWhenAmountsChecked) {
        Amount = amount;
        AmountCy = amountCy;
        IsPrepaymentInvoiced = isPrepaymentInvoiced;
        AccountType = accountType;
        MovementType = movementType;
        OmitWhenAmountsChecked = omitWhenAmountsChecked;
        
        RefDocumentKey = null;
        RefCashAccountKey = null;
        RefTaxKey = null;
    }

    public void addAmount(SFinAmount amount) {
        addAmount(amount.Amount, amount.AmountCy);
    }
    
    public void addAmount(double amount, double amountCy) {
        Amount = SLibUtils.round(Amount + amount, 2);
        AmountCy = SLibUtils.round(AmountCy + amountCy, 2);;
    }
}

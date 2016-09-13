/*
 * Copyright © Software Aplicado SA de CV. All rights reserverd.
 */
package erp.mfin.data;

import java.util.ArrayList;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SFinAmounts {

    private final ArrayList<SFinAmount> maAmounts;
    
    public SFinAmounts() {
        maAmounts = new ArrayList<>();
    }
    
    public ArrayList<SFinAmount> getAmounts() { return maAmounts; }
    
    /**
     * Gets amount that matches the document key provided if any, otherwise null.
     * @param key Document key.
     * @return Found amount.
     */
    public SFinAmount getAmountByDocument(final int[] key) {
        SFinAmount amount = null;
        
        for (SFinAmount a : maAmounts) {
            if (SLibUtils.compareKeys(key, a.KeyRefDocument) && a.AmountType == SFinAmountType.DOCUMENT) {
                amount = a;
                break;
            }
        }
        
        return amount;
    }
    
    /**
     * Gets amount that matches the cash account key provided if any, otherwise null.
     * @param key Cash account key.
     * @return Found amount.
     */
    public SFinAmount getAmountByCashAccount(final int[] key) {
        SFinAmount amount = null;
        
        for (SFinAmount a : maAmounts) {
            if (SLibUtils.compareKeys(key, a.KeyRefCashAccount) && a.AmountType == SFinAmountType.CASH_ACCOUNT) {
                amount = a;
                break;
            }
        }
        
        return amount;
    }
    
    /**
     * Gets amount for advance billed if any, otherwise null.
     * @return Found amount.
     */
    public SFinAmount getAmountForAdvanceBilled() {
        SFinAmount amount = null;
        
        for (SFinAmount a : maAmounts) {
            if (a.AmountType == SFinAmountType.ADVANCE_BILLED) {
                amount = a;
                break;
            }
        }
        
        return amount;
    }
    
    /**
     * Adds amount that matches the document key provided.
     * @param key Document key.
     * @param amount Amount to be added.
     */
    public void addAmountForDocument(final int[] key, final SFinAmount amount) {
        SFinAmount a = getAmountByDocument(key);
        
        if (a == null) {
            a = new SFinAmount(amount);
            a.AmountType = SFinAmountType.DOCUMENT;
            a.KeyRefDocument = key;
            maAmounts.add(a);
        }
        else {
            a.addAmount(amount);
        }
    }
    
    /**
     * Adds amount that matches the cash account key provided.
     * @param key Cash account key.
     * @param amount Amount to be added.
     */
    public void addAmountForCashAccount(final int[] key, final SFinAmount amount) {
        SFinAmount a = getAmountByCashAccount(key);
        
        if (a == null) {
            a = new SFinAmount(amount);
            a.AmountType = SFinAmountType.CASH_ACCOUNT;
            a.KeyRefCashAccount = key;
            maAmounts.add(a);
        }
        else {
            a.addAmount(amount);
        }
    }
    
    /**
     * Adds amount for advance billed.
     * @param amount Amount to be added.
     */
    public void addAmountForAdvanceBilled(final SFinAmount amount) {
        SFinAmount a = getAmountForAdvanceBilled();
        
        if (a == null) {
            a = new SFinAmount(amount);
            a.AmountType = SFinAmountType.ADVANCE_BILLED;
            maAmounts.add(a);
        }
        else {
            a.addAmount(amount);
        }
    }
    
    /**
     * Checks sum of all amounts against provided total amount, and adjust difference (if any) on greatest member amount.
     * @param total Total amount.
     */
    public void checkAmount(double total) {
        double sum = 0;
        SFinAmount greatest = null;
        
        for (SFinAmount a : maAmounts) {
            if (a.Movement == SFinMovement.INCREMENT) {
                sum += a.Amount;
            }
            else {
                sum -= a.Amount;
            }
            
            if (greatest == null || a.Amount > greatest.Amount) {
                greatest = a;
            }
        }
        
        sum = SLibUtils.round(sum, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
        
        if (total != sum && greatest != null) {
            greatest.Amount = SLibUtils.round(greatest.Amount + (total - sum), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
        }
    }
}

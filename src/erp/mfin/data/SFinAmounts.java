/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
     * Gets amount that matches document key provided if any, otherwise null.
     * @param key Document key.
     * @return Found amount.
     */
    public SFinAmount getAmountByDocument(final int[] key) {
        SFinAmount amount = null;
        
        for (SFinAmount sfa : maAmounts) {
            if (SLibUtils.compareKeys(key, sfa.KeyRefDocument)) {
                amount = sfa;
                break;
            }
        }
        
        return amount;
    }
    
    /**
     * Gets amount that matches cash account key provided if any, otherwise null.
     * @param key Document key.
     * @return Found amount.
     */
    public SFinAmount getAmountByCashAccount(final int[] key) {
        SFinAmount amount = null;
        
        for (SFinAmount sfa : maAmounts) {
            if (SLibUtils.compareKeys(key, sfa.KeyRefCashAccount)) {
                amount = sfa;
                break;
            }
        }
        
        return amount;
    }
    
    public void addAmountForDocument(final int[] key, final SFinAmount amount) {
        SFinAmount sma = getAmountByDocument(key);
        
        if (sma == null) {
            sma = new SFinAmount(amount);
            sma.KeyRefDocument = key;
            maAmounts.add(sma);
        }
        else {
            sma.addAmount(amount);
        }
    }
    
    public void addAmountForCashAccount(final int[] key, final SFinAmount amount) {
        SFinAmount sma = getAmountByCashAccount(key);
        
        if (sma == null) {
            sma = new SFinAmount(amount);
            sma.KeyRefCashAccount = key;
            maAmounts.add(sma);
        }
        else {
            sma.addAmount(amount);
        }
    }
    
    /**
     * Checks sum of all amounts against provided total amount, and adjust difference (if any) on greatest member amount.
     * @param total Total amount.
     */
    public void checkAmount(double total) {
        double sum = 0;
        SFinAmount greatest = null;
        
        for (SFinAmount sfa : maAmounts) {
            if (sfa.Movement == SFinMovement.INCREMENT) {
                sum += sfa.Amount;
            }
            else {
                sum -= sfa.Amount;
            }
            
            if (greatest == null || sfa.Amount > greatest.Amount) {
                greatest = sfa;
            }
        }
        
        sum = SLibUtils.round(sum, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
        
        if (total != sum && greatest != null) {
            greatest.Amount = SLibUtils.round(total - sum, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
        }
    }
}

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
     * @param keyTax
     * @return Found amount.
     */
    public SFinAmount getAmountByDocument(final int[] key, final int[] keyTax) {
        SFinAmount amount = null;
        
        for (SFinAmount a : maAmounts) {
            if (a.AccountType == SFinAccountType.ACC_BIZ_PARTNER_DOC && SLibUtils.compareKeys(key, a.KeyRefDocument)
                    && keyTax[0] == a.getKeyTax()[0] && keyTax[1] == a.getKeyTax()[1]) {
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
            if (a.AccountType == SFinAccountType.ACC_CASH_ACCOUNT && SLibUtils.compareKeys(key, a.KeyRefCashAccount)) {
                amount = a;
                break;
            }
        }
        
        return amount;
    }
    
    /**
     * Gets amount for prepayments to invoice if any, otherwise null.
     * @return Found amount.
     */
    public SFinAmount getAmountForPrepaymentsToInvoice() {
        SFinAmount amount = null;
        
        for (SFinAmount a : maAmounts) {
            if (a.AccountType == SFinAccountType.ACC_PREPAY_TO_INVOICE) {
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
        SFinAmount a = getAmountByDocument(key, amount.getKeyTax());
        
        if (a == null) {
            a = new SFinAmount(amount);
            a.AccountType = SFinAccountType.ACC_BIZ_PARTNER_DOC;
            a.KeyRefDocument = key;
            a.KeyTax = amount.getKeyTax();
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
            a.AccountType = SFinAccountType.ACC_CASH_ACCOUNT;
            a.KeyRefCashAccount = key;
            maAmounts.add(a);
        }
        else {
            a.addAmount(amount);
        }
    }
    
    /**
     * Adds amount for prepayments to invoice.
     * @param amount Amount to be added.
     */
    public void addAmountForPrepaymentsToInvoice(final SFinAmount amount) {
        SFinAmount a = getAmountForPrepaymentsToInvoice();
        
        if (a == null) {
            a = new SFinAmount(amount);
            a.AccountType = SFinAccountType.ACC_PREPAY_TO_INVOICE;
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
    public void checkAmounts(double total) {
        double sum = 0;
        SFinAmount greatest = null;
        
        for (SFinAmount a : maAmounts) {
            if (!a.OmitWhenAmountsChecked) {
                sum = SLibUtils.round(sum + a.Amount, 2);

                if (greatest == null || a.Amount > greatest.Amount) {
                    greatest = a;
                }
            }
        }
        
        if (total != sum && greatest != null) {
            greatest.Amount = SLibUtils.round(greatest.Amount + (total - sum), 2);
        }
    }
}

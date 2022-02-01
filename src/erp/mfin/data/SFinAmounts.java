/*
 * Copyright © Software Aplicado SA de CV. All rights reserverd.
 */
package erp.mfin.data;

import java.util.ArrayList;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores, Edwin Carmona, Sergio Flores
 */
public class SFinAmounts {

    private final ArrayList<SFinAmount> maAmounts;
    
    public SFinAmounts() {
        maAmounts = new ArrayList<>();
    }
    
    public ArrayList<SFinAmount> getAmounts() { return maAmounts; }
    
    /**
     * Gets amount that matches the document key provided if any, otherwise <code>null</code>.
     * @param documentKey Document key.
     * @param taxKey
     * @return Found amount if any, otherwise <code>null</code>.
     */
    public SFinAmount getAmountByDocument(final int[] documentKey, final int[] taxKey) {
        SFinAmount amount = null;
        
        for (SFinAmount a : maAmounts) {
            if (a.AccountType == SFinAccountType.ACC_BIZ_PARTNER_DOC && SLibUtils.compareKeys(documentKey, a.RefDocumentKey) && SLibUtils.compareKeys(taxKey, a.RefTaxKey)) {
                amount = a;
                break;
            }
        }
        
        return amount;
    }
    
    /**
     * Gets amount that matches the cash account key provided if any, otherwise <code>null</code>.
     * @param cashAccountKey Cash account key.
     * @return Found amount if any, otherwise <code>null</code>.
     */
    public SFinAmount getAmountByCashAccount(final int[] cashAccountKey) {
        SFinAmount amount = null;
        
        for (SFinAmount a : maAmounts) {
            if (a.AccountType == SFinAccountType.ACC_CASH_ACCOUNT && SLibUtils.compareKeys(cashAccountKey, a.RefCashAccountKey)) {
                amount = a;
                break;
            }
        }
        
        return amount;
    }
    
    /**
     * Gets amount for prepayments to invoice if any, otherwise <code>null</code>.
     * @return Found amount if any, otherwise <code>null</code>.
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
     * @param documentKey Document key.
     * @param amount Amount to be added.
     */
    public void addAmountForDocument(final int[] documentKey, final SFinAmount amount) {
        SFinAmount a = getAmountByDocument(documentKey, amount.RefTaxKey);
        
        if (a == null) {
            a = new SFinAmount(amount);
            a.AccountType = SFinAccountType.ACC_BIZ_PARTNER_DOC;
            a.RefDocumentKey = documentKey;
            a.RefTaxKey = amount.RefTaxKey;
            
            maAmounts.add(a);
        }
        else {
            a.addAmount(amount);
        }
    }
    
    /**
     * Adds amount that matches the cash account key provided.
     * @param cashAccountKey Cash account key.
     * @param amount Amount to be added.
     */
    public void addAmountForCashAccount(final int[] cashAccountKey, final SFinAmount amount) {
        SFinAmount a = getAmountByCashAccount(cashAccountKey);
        
        if (a == null) {
            a = new SFinAmount(amount);
            a.AccountType = SFinAccountType.ACC_CASH_ACCOUNT;
            a.RefCashAccountKey = cashAccountKey;
            
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
     * Checks sum of all amounts against provided total amount, and adjust difference, if any, on greatest member amount.
     * @param total Total amount.
     */
    public void checkAmounts(double total) {
        double sum = 0;
        SFinAmount greatest = null;
        
        for (SFinAmount a : maAmounts) {
            if (!a.OmitWhenAmountsChecked) {
                sum = SLibUtils.roundAmount(sum + a.Amount);

                if (greatest == null || a.Amount > greatest.Amount) {
                    greatest = a;
                }
            }
        }
        
        if (total != sum && greatest != null) {
            greatest.Amount = SLibUtils.roundAmount(greatest.Amount + (total - sum));
        }
    }
}

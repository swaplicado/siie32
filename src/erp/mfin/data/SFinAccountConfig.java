/*
 * Copyright © Software Aplicado SA de CV. All rights reserverd.
 */
package erp.mfin.data;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Sergio Flores, Edwin Carmona, Sergio Flores
 */
public class SFinAccountConfig {
    
    private ArrayList<SFinAccountConfigEntry> maAccountConfigEntries;
    private double[] madPercentages;
    private int[] manTaxKey;
    
    public SFinAccountConfig(Collection<SFinAccountConfigEntry> accountConfigEntries) {
        this(accountConfigEntries, new int[] { 0, 0 });
    }
    
    public SFinAccountConfig(Collection<SFinAccountConfigEntry> accountConfigEntries, int[] taxKey) {
        maAccountConfigEntries = new ArrayList<>(accountConfigEntries);
        
        madPercentages = new double[maAccountConfigEntries.size()];
        for (int i = 0; i < madPercentages.length; i++) {
            madPercentages[i] = maAccountConfigEntries.get(i).getPercentage();
        }
        
        manTaxKey = taxKey;
    }

    public void setTaxKey(int[] taxKey) {
        manTaxKey = taxKey;
    }

    public int[] getTaxKey() {
        return manTaxKey;
    }
    
    public ArrayList<SFinAccountConfigEntry> getAccountConfigEntries() {
        return maAccountConfigEntries;
    };
    
    public ArrayList<SFinAmount> prorateAmount(final SFinAmount amount) {
        double[] values = null;
        double[] valuesCy = null;
        ArrayList<SFinAmount> amounts = new ArrayList<>();
        
        values = SFinAccountUtilities.prorateAmount(amount.Amount, madPercentages);
        valuesCy = SFinAccountUtilities.prorateAmount(amount.AmountCy, madPercentages);
        
        for (int i = 0; i < madPercentages.length; i++) {
            amounts.add(new SFinAmount(values[i], valuesCy[i]));
        }
        
        return amounts;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.data;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Sergio Flores
 */
public class SFinAccountConfig {
    
    private ArrayList<SFinAccountConfigEntry> maAccountConfigEntries;
    private double[] madPercentages;
    
    public SFinAccountConfig(Collection<SFinAccountConfigEntry> configEntries) {
        maAccountConfigEntries = new ArrayList(configEntries);
        
        madPercentages = new double[maAccountConfigEntries.size()];

        for (int i = 0; i < madPercentages.length; i++) {
            madPercentages[i] = maAccountConfigEntries.get(i).getPercentage();
        }
    }
    
    public ArrayList<SFinAccountConfigEntry> getAccountConfigEntries() { return maAccountConfigEntries; };
    
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

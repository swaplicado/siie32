/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.util.ArrayList;

/**
 *
 * @author Irving Sánchez
 */
public class SGuiDpsLink {
    private SDataDps moDataDpsDestiny;
    private ArrayList<SGuiDps> maGuiDpsSources;
    private SClientInterface miClient;
	
    public SGuiDpsLink(SClientInterface client) {
        miClient = client;
        maGuiDpsSources = new ArrayList<>();
        
    }	
	
    public void addDataDpsDestiny (SDataDps dataDpsDestiny) { 
        moDataDpsDestiny = dataDpsDestiny;
        if (moDataDpsDestiny != null) {
            for (SDataDpsEntry destinyEntry : moDataDpsDestiny.getDbmsDpsEntries()) {
                addDataDpsDestinyEntry(destinyEntry);
            }
        }
    }
    public void addDataDpsDestinyEntry(SDataDpsEntry dataDpsDestinyEntry) {
        // TODO:
        SDataDps dpsSource = null;
        
        for (SDataDpsDpsLink destinyEntryLink : dataDpsDestinyEntry.getDbmsDpsLinksAsDestiny()) {
            dpsSource = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, destinyEntryLink.getDbmsSourceDpsKey(), SLibConstants.EXEC_MODE_STEALTH);
            addDataDpsSource(dpsSource);
            
            for (SGuiDps guiDps: maGuiDpsSources) {
                for (SGuiDpsEntry dpsEntry : guiDps.getGuiDpsSourceEntries()) {
                    for (SGuiDpsEntryPrice guiDpsEntryPrice : dpsEntry.getGuiDpsSourceEntryPrices()) {
                        if(SLibUtilities.compareKeys(guiDps.getDataDpsSource().getPrimaryKey(), destinyEntryLink.getDbmsSourceDpsKey())){
                             if (dataDpsDestinyEntry.getContractPriceYear() == guiDpsEntryPrice.getDataDpsEntryPrice().getContractPriceYear() && dataDpsDestinyEntry.getContractPriceMonth() == guiDpsEntryPrice.getDataDpsEntryPrice().getContractPriceMonth()) {
                                 guiDpsEntryPrice.addDataDpsDestinyEntry(dataDpsDestinyEntry);
                             }
                        }
                    }
                }
            }
        }
    }
    
    public void addDataDpsSource(SDataDps dataDpsSource) {
        // TODO:
        SGuiDps guiDps = null;
        SGuiDpsEntry guiDpsEntry = null;
        SGuiDpsEntryPrice guiDpsEntryPrice = null;
        
        for (SGuiDps dps: maGuiDpsSources) {
            if (SLibUtilities.compareKeys(dps.getDataDpsSource().getPrimaryKey(), dataDpsSource.getPrimaryKey())) {
                return;
            }
        }
        
        guiDps = new SGuiDps(dataDpsSource);
        
        for (SDataDpsEntry sourceEntry : dataDpsSource.getDbmsDpsEntries()) {
            guiDpsEntry = new SGuiDpsEntry(miClient, sourceEntry);
            
            for (SDataDpsEntryPrice sourceEntryPrice : sourceEntry.getDbmsEntryPrices()) {
                guiDpsEntryPrice = new SGuiDpsEntryPrice(sourceEntryPrice);                
                guiDpsEntryPrice.setQtyUsedOther(STrnUtilities.getQuantityProcessForDpsEntryPrice(miClient, (int[]) sourceEntryPrice.getPrimaryKey(), moDataDpsDestiny != null ? (int[]) moDataDpsDestiny.getPrimaryKey() : new int[] {0, 0}));
                guiDpsEntry.getGuiDpsSourceEntryPrices().add(guiDpsEntryPrice);
            }
            
            guiDps.getGuiDpsSourceEntries().add(guiDpsEntry);
        }

        maGuiDpsSources.add(guiDps);
    }

    public SGuiDps pickGuiDpsSource(int[] pkGuiDpsSource) {
        SGuiDps guiDps = null;
        for (SGuiDps dps: maGuiDpsSources) {
            if (SLibUtilities.compareKeys(dps.getDataDpsSource().getPrimaryKey(), pkGuiDpsSource)) {
                guiDps = dps;   
            }
        }
        
        return guiDps;
    }
    
    public SGuiDpsEntry pickGuiDpsSourceEntry(int[] pkGuiDpsSource, int[] pkGuiDpsSourceEntry) {
        SGuiDps guiDps = null;
        SGuiDpsEntry guiDpsEntry = null;
        
        guiDps = pickGuiDpsSource(pkGuiDpsSource);
        
        if (guiDps != null) {
            for (SGuiDpsEntry dpsEntry : guiDps.getGuiDpsSourceEntries()) {
                if (SLibUtilities.compareKeys(dpsEntry.getDataDpsSourceEntry().getPrimaryKey(), pkGuiDpsSourceEntry)) {
                    guiDpsEntry = dpsEntry;
                }
            }
        }
        
        return guiDpsEntry;
    }
    
    public double obtainQtyAvailable(SDataDpsEntry dataDpsDestinyEntry) {
        double qtyAvailable = 0.0;
        for (SGuiDps guiDps: maGuiDpsSources) {
            for (SGuiDpsEntry dpsEntry : guiDps.getGuiDpsSourceEntries()) {
                for (SGuiDpsEntryPrice guiDpsEntryPrice : dpsEntry.getGuiDpsSourceEntryPrices()) {
                    if (dataDpsDestinyEntry.getContractPriceYear() == guiDpsEntryPrice.getDataDpsEntryPrice().getContractPriceYear() && dataDpsDestinyEntry.getContractPriceMonth() == guiDpsEntryPrice.getDataDpsEntryPrice().getContractPriceMonth()) {
                        qtyAvailable = guiDpsEntryPrice.obtainQtyAvailable(dataDpsDestinyEntry);
                    }
                }
            }
        }
        return qtyAvailable;
    }
}
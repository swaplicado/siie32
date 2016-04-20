/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.client.SClientInterface;
import erp.lib.SLibTimeUtilities;
import erp.lib.form.SFormComponentItem;
import java.util.ArrayList;

/**
 *
 * @author Irving Sánchez
 */
public class SGuiDpsEntry {
    private SDataDpsEntry moDataDpsSourceEntry;
    private ArrayList<SGuiDpsEntryPrice> maGuiDpsSourceEntryPrices;
    private SClientInterface miClient;
	
    public SGuiDpsEntry(SClientInterface client, SDataDpsEntry dataDpsSourceEntry) {
        miClient = client;
        moDataDpsSourceEntry = dataDpsSourceEntry;
        maGuiDpsSourceEntryPrices = new ArrayList<>();
    }
 
    public SDataDpsEntry getDataDpsSourceEntry() { return moDataDpsSourceEntry; }    
    public ArrayList<SGuiDpsEntryPrice> getGuiDpsSourceEntryPrices() { return maGuiDpsSourceEntryPrices; }

    public ArrayList<SFormComponentItem> createFormComponentEntryPrices() {
        String item = null;
        SFormComponentItem componentItem = null;
        ArrayList<SFormComponentItem> formComponentEntryPrices =  new ArrayList<>();
        
        for(SGuiDpsEntryPrice guiEntryPrice: maGuiDpsSourceEntryPrices) {
            if (guiEntryPrice.getDataDpsEntryPrice().getIsDeleted()|| guiEntryPrice.obtainQtyAvailable() <= 0.0d) {
                continue;
            }

            item = miClient.getSessionXXX().getFormatters().getDateYearMonthFormat().format(SLibTimeUtilities.createDate(guiEntryPrice.getDataDpsEntryPrice().getContractPriceYear(), guiEntryPrice.getDataDpsEntryPrice().getContractPriceMonth()));
            if (!guiEntryPrice.getDataDpsEntryPrice().getIsPriceVariable()) {
                item += "; base" + miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(guiEntryPrice.getDataDpsEntryPrice().getContractBase()) + "; futuro: " + miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(guiEntryPrice.getDataDpsEntryPrice().getContractFuture());            
            }
            else {
                item += "; P. U.:" + miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormat().format(guiEntryPrice.getDataDpsEntryPrice().getOriginalPriceUnitaryCy());
            }

            componentItem = new SFormComponentItem(guiEntryPrice.getDataDpsEntryPrice().getPrimaryKey(), item);
            componentItem.setComplement(guiEntryPrice);
            formComponentEntryPrices.add(componentItem);
        }
        
        return formComponentEntryPrices;
                   
    }
}
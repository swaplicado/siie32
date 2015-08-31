/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.lib.SLibUtilities;
import java.util.ArrayList;

/**
 *
 * @author Irving Sánchez
 */
public class SGuiDps {
    private SDataDps moDataDpsSource;
    private ArrayList<SGuiDpsEntry> maGuiDpsSourceEntries;
	
    public SGuiDps(SDataDps dataDpsSource) {
        moDataDpsSource = dataDpsSource;
        maGuiDpsSourceEntries = new ArrayList<>();
    }
	
    public SDataDps getDataDpsSource() { return moDataDpsSource; }
    public ArrayList<SGuiDpsEntry> getGuiDpsSourceEntries() {  return maGuiDpsSourceEntries; }
    
    public SGuiDpsEntry	pickGuiDpsSourceEntry(int[] pkGuiDpsSourceEntry) {
        SGuiDpsEntry guiDpsEntry = null;
        for (SGuiDpsEntry guiEntry : maGuiDpsSourceEntries) {
            if (SLibUtilities.compareKeys((int[]) guiEntry.getDataDpsSourceEntry().getPrimaryKey(), pkGuiDpsSourceEntry)) {
                guiDpsEntry = guiEntry; 
            }
        }
        
        return guiDpsEntry;
    }   
}
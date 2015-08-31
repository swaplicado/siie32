/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import erp.lib.SLibTimeUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataDrcEntryRow extends erp.lib.table.STableRow {

    public SDataDrcEntryRow(java.lang.Object data){
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {        
        SDataDrcEntry data = (SDataDrcEntry) moData;
        SDataDrcEntryHour oDrcEntryHour = null;

        mvValues.clear();
        mvValues.add(data.getFkOrdDocId_n() > 0 ? data.getDbmsProductionOrder() : data.getDbmsPlant());
        
        // Read record hours:
        
        for (int i=0; i<data.getDbmsDrcEntriesHours().size(); i++) {

            /*
             * Record hours are sorted from query (SELECT).
             */
            
            /*oDrcEntryHour = data.getDbmsDrcEntriesHours().get(
                SLibTimeUtilities.getFirstDayOfWeekStd() == 1 ?
                i+3<6 ? i+18 : i-3 :
                i);*/
            oDrcEntryHour = data.getDbmsDrcEntriesHours().get(i);
            mvValues.add(oDrcEntryHour.getQuantity());
        }
    }
}

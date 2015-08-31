/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import erp.data.SDataConstants;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataCostRow extends erp.lib.table.STableRow {

    public SDataCostRow(java.lang.Object data){
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        String sDbmsReference = "";
        SDataCost oCost = null;
        SDataCost data = (SDataCost) moData;

        mvValues.clear();

        // Read record hours:
        
        for (int i=0; i<data.getDbmsCost().size(); i++) {

            /*
             * Record hours are sorted from query (SELECT).
             */
            
            /*oDrcEntryHour = data.getDbmsDrcEntriesHours().get(
                SLibTimeUtilities.getFirstDayOfWeekStd() == 1 ?
                i+3<6 ? i+18 : i-3 :
                i);*/

            oCost = data.getDbmsCost().get(i);

            if (oCost.getDbmsType() == SDataConstants.MFG_COST) {

                // Assing bizPartner name when bizPartner is different:

                if (sDbmsReference.compareTo(oCost.getDbmsBizPartner()) != 0) {
                    mvValues.add(oCost.getDbmsBizPartner());
                    sDbmsReference = oCost.getDbmsBizPartner();
                }
            }
            else {

                // Assing reference when it is different:

                if (sDbmsReference.compareTo(oCost.getDbmsReference()) != 0) {
                    mvValues.add(oCost.getDbmsReference());
                    sDbmsReference = oCost.getDbmsReference();
                }
            }

            mvValues.add(oCost.getQuantity());
        }
    }
}

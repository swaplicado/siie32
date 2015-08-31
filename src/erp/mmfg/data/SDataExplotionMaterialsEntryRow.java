/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataExplotionMaterialsEntryRow extends erp.lib.table.STableRow {

    public SDataExplotionMaterialsEntryRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        String sLots = "";
        SDataExplotionMaterialsEntry data = (SDataExplotionMaterialsEntry) moData;

        // Concat lots:

        /* XXX: Change form lot by new form lot general
        for (int i=0; i<data.getDbmsLots().size(); i++) {
            sLots += data.getDbmsLots().get(i).getLot().length() > 0 ? data.getDbmsLots().get(i).getLot() : "<vacio>";

            if ((i + 1) < data.getDbmsLots().size()) {
                sLots += ", ";
            }
        }
         */
        
        mvValues.clear();
        mvValues.add(data.getDbmsItemKey());
        mvValues.add(data.getDbmsItem());
        mvValues.add(data.getDbmsItemUnitSymbol());
        mvValues.add(data.getDbmsBizPartner());
        mvValues.add(data.getGrossReq());
        mvValues.add(data.getSafetyStock());
        mvValues.add(data.getAvailable());
        mvValues.add(data.getBackorder());
        mvValues.add((data.getNet()));
        mvValues.add(data.getLtimeTs());
        mvValues.add(data.getDeliveryTs());
        mvValues.add(sLots);
    }
}

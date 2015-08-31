/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataExplotionMaterialsResultRow extends erp.lib.table.STableRow {

    public SDataExplotionMaterialsResultRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataExplotionMaterialsEntry data = (SDataExplotionMaterialsEntry) moData;

        mvValues.clear();
        mvValues.add(data.getDbmsItemKey());
        mvValues.add(data.getDbmsItem());
        mvValues.add(data.getDbmsItemUnitSymbol());
        mvValues.add(data.getDbmsBizPartner());
        mvValues.add(data.getGrossReq());
        mvValues.add(data.getSafetyStock());
        mvValues.add(data.getAvailable());
        mvValues.add(data.getBackorder());
        mvValues.add(data.getNet());
        mvValues.add(data.getLtimeTs());
        mvValues.add(data.getDeliveryTs());        
    }
}

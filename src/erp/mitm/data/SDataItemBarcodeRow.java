/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataItemBarcodeRow extends erp.lib.table.STableRow {

    public SDataItemBarcodeRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataItemBarcode itemBarcode = (SDataItemBarcode) moData;

        mvValues.clear();
        mvValues.add(itemBarcode.getBarcode());
        mvValues.add(itemBarcode.getIsDeleted());
    }
}

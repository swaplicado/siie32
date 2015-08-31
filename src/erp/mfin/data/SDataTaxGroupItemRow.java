/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SDataTaxGroupItemRow extends erp.lib.table.STableRow {

    public SDataTaxGroupItemRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataTaxGroupItem item = (SDataTaxGroupItem) moData;

        mvValues.clear();
        mvValues.add(item.getDbmsTaxRegion());
        mvValues.add(item.getPkDateStartId());
        mvValues.add(item.getDbmsTaxGroup());
        mvValues.add(item.getIsDeleted());
    }
}

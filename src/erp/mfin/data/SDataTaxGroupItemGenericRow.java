/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SDataTaxGroupItemGenericRow extends erp.lib.table.STableRow {

    public SDataTaxGroupItemGenericRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataTaxGroupItemGeneric itemGeneric = (SDataTaxGroupItemGeneric) moData;

        mvValues.clear();
        mvValues.add(itemGeneric.getDbmsTaxRegion());
        mvValues.add(itemGeneric.getPkDateStartId());
        mvValues.add(itemGeneric.getDbmsTaxGroup());
        mvValues.add(itemGeneric.getIsDeleted());
    }
}

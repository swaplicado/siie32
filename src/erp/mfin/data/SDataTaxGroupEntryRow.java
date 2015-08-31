/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataTaxGroupEntryRow extends erp.lib.table.STableRow {

    public SDataTaxGroupEntryRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataTaxGroupEntry entry = (SDataTaxGroupEntry) moData;

        mvValues.clear();
        mvValues.add(entry.getDbmsTaxIdentityEmisorType());
        mvValues.add(entry.getDbmsTaxIdentityReceptorType());
        mvValues.add(entry.getDateStart());
        mvValues.add(entry.getDateEnd_n());
        mvValues.add(entry.getDbmsTax());
        mvValues.add(entry.getApplicationOrder());
    }
}

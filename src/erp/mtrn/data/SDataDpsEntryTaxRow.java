/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Sergio Flores
 */
public class SDataDpsEntryTaxRow extends erp.lib.table.STableRow {

    public SDataDpsEntryTaxRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataDpsEntryTax tax = (SDataDpsEntryTax) moData;

        mvValues.clear();
        mvValues.add(tax.getDbmsTax());
        mvValues.add(tax.getPercentage());
        mvValues.add(tax.getValueUnitary());
        mvValues.add(tax.getValue());
        mvValues.add(tax.getTax());
        mvValues.add(tax.getTaxCy());
        mvValues.add(tax.getDbmsTaxType());
        mvValues.add(tax.getDbmsTaxCalculationType());
        mvValues.add(tax.getDbmsTaxApplicationType());
    }
}

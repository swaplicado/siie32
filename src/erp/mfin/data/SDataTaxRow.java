/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataTaxRow extends erp.lib.table.STableRow {

    public SDataTaxRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataTax tax = (SDataTax) moData;

        mvValues.clear();
        mvValues.add(tax.getTax());
        mvValues.add(tax.getDbmsTaxType());
        mvValues.add(tax.getDbmsTaxCalculationType());
        mvValues.add(tax.getDbmsTaxApplicationType());
        mvValues.add(tax.getPercentage());
        mvValues.add(tax.getValueUnitary());
        mvValues.add(tax.getValue());
        mvValues.add(tax.getVatType());
        mvValues.add(tax.getIsDeleted());
        mvValues.add(tax.getDbmsUserNew());
        mvValues.add(tax.getUserNewTs());
        mvValues.add(tax.getDbmsUserEdit());
        mvValues.add(tax.getUserEditTs());
        mvValues.add(tax.getDbmsUserDelete());
        mvValues.add(tax.getUserDeleteTs());
    }
}

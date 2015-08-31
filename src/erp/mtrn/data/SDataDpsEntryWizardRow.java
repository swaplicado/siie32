/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Sergio Flores
 */
public class SDataDpsEntryWizardRow extends erp.lib.table.STableRow {

    public SDataDpsEntryWizardRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataDpsEntry entry = (SDataDpsEntry) moData;

        mvValues.clear();
        mvValues.add(entry.getConceptKey());
        mvValues.add(entry.getConcept());
        mvValues.add(entry.getDbmsItemRef_n());
        mvValues.add(entry.getFkCostCenterId_n());
        mvValues.add(entry.getDbmsCostCenter_n());
        mvValues.add(entry.getTotalCy_r());
    }
}

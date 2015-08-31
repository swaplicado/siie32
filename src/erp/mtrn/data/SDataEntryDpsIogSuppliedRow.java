/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataEntryDpsIogSuppliedRow extends erp.lib.table.STableRow {

    public SDataEntryDpsIogSuppliedRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataEntryDpsIogSupplied entry = (SDataEntryDpsIogSupplied) moData;

        mvValues.clear();
        mvValues.add(entry.getItemKey());
        mvValues.add(entry.getConcept());
        mvValues.add(entry.getQuantityNet());
        mvValues.add(entry.getUnitSymbol());
        mvValues.add(entry.getSurplusPercentage());
        mvValues.add(entry.getQuantityActualSupplied());
        mvValues.add(entry.getQuantityToActualSupply());
        mvValues.add(entry.getQuantityToSupplyNet());
        mvValues.add(entry.getLot());
    }
}

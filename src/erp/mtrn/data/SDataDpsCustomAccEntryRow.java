/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 * Esta clase permite mostrar en un renglón de una tabla la definición de partidas de contabilización personalizadas para DPS.
 * @author Sergio Flores
 */
public class SDataDpsCustomAccEntryRow extends erp.lib.table.STableRow {

    public SDataDpsCustomAccEntryRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataDpsCustomAccEntry entry = (SDataDpsCustomAccEntry) moData;

        mvValues.clear();
        mvValues.add(entry.getDbmsItemKey());
        mvValues.add(entry.getDbmsItem());
        mvValues.add(entry.getConcept());
        mvValues.add(entry.getQuantity());
        mvValues.add(entry.getDbmsUnitSymbol());
        mvValues.add(entry.getSubtotalPct());
        mvValues.add(entry.getSubtotalCy());
        mvValues.add(entry.getFkCostCenterId_n());
        mvValues.add(entry.getDbmsCostCenter_n());
        mvValues.add(entry.getDbmsItemRefKey_n());
        mvValues.add(entry.getDbmsItemRef_n());
    }
}

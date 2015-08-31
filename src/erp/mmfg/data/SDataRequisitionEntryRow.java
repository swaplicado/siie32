/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataRequisitionEntryRow extends erp.lib.table.STableRow {

    public SDataRequisitionEntryRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataRequisitionEntry data = (SDataRequisitionEntry) moData;

        mvValues.clear();
        mvValues.add(data.getDbmsItemKey());
        mvValues.add(data.getDbmsItem());
        mvValues.add(data.getQuantity());
        mvValues.add(data.getDbmsItemUnitSymbol());
    }
}

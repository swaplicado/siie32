/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Sergio Flores, Claudio Peña
 */
public class SDataDiogMaintMovementEntryRow extends erp.lib.table.STableRow {

    public SDataDiogMaintMovementEntryRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataDiogEntry diogEntry = (SDataDiogEntry) moData;
                
        mvValues.clear();
        mvValues.add(diogEntry.getDbmsItemKey());
        mvValues.add(diogEntry.getDbmsItem());
//        mvValues.add(diogEntry.getDbmsPartNum());
        mvValues.add(diogEntry.getDbmsMaintArea());
//        mvValues.add(diogEntry.getDbmsConsumeEntity());
//        mvValues.add(diogEntry.getDbmsSubConsumeEntity());
        mvValues.add(diogEntry.getOriginalQuantity());
        mvValues.add(diogEntry.getDbmsOriginalUnitSymbol());
        mvValues.add(diogEntry.getOriginalValueUnitary());
        mvValues.add(diogEntry.getValue());
        mvValues.add(diogEntry.getIsDeleted());
        mvValues.add(diogEntry.getDbmsUserNew());
        mvValues.add(diogEntry.getUserNewTs());
        mvValues.add(diogEntry.getDbmsUserEdit());
        mvValues.add(diogEntry.getUserEditTs());
        mvValues.add(diogEntry.getDbmsUserDelete());
        mvValues.add(diogEntry.getUserDeleteTs());
    }
}

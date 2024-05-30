/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Alfonso Flores, Sergio Flores, Claudio Peña, Isabel Servín
 */
public class SDataDiogEntryRow extends erp.lib.table.STableRow {

    public SDataDiogEntryRow(java.lang.Object data) {
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
        mvValues.add(diogEntry.getOriginalQuantity());
        mvValues.add(diogEntry.getDbmsOriginalUnitSymbol());
        mvValues.add(diogEntry.getAuxStockMovesAsString());
        mvValues.add(diogEntry.getOriginalValueUnitary());
        mvValues.add(diogEntry.getValue());
        mvValues.add(diogEntry.getIsDeleted());
        mvValues.add(diogEntry.getDbmsUserNew());
        mvValues.add(diogEntry.getUserNewTs());
        mvValues.add(diogEntry.getDbmsUserEdit());
        mvValues.add(diogEntry.getUserEditTs());
        mvValues.add(diogEntry.getDbmsUserDelete());
        mvValues.add(diogEntry.getUserDeleteTs());
        mvValues.add(diogEntry.getDbmsCostCenter() != null ? diogEntry.getDbmsCostCenter().getPkCostCenterIdXXX() : "");
    }
}

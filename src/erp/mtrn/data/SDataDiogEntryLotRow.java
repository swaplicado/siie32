/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.mtrn.data.*;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataDiogEntryLotRow extends erp.lib.table.STableRow {

    public SDataDiogEntryLotRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataDiogEntryLot data = (SDataDiogEntryLot) moData;

        mvValues.clear();
        mvValues.add(data.getLot());
        mvValues.add(data.getQuantity());
        mvValues.add(data.getIsDeleted());
        mvValues.add(data.getDbmsUserNew());
        mvValues.add(data.getUserNewTs());
        mvValues.add(data.getDbmsUserEdit());
        mvValues.add(data.getUserEditTs());
        mvValues.add(data.getDbmsUserDelete());
        mvValues.add(data.getUserDeleteTs());
    }
}

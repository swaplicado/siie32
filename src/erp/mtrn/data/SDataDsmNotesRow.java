/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.mtrn.data.SDataDsmNotes;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataDsmNotesRow extends erp.lib.table.STableRow {

    public SDataDsmNotesRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataDsmNotes notes = (SDataDsmNotes) moData;

        mvValues.clear();
        mvValues.add(notes.getNotes());
        mvValues.add(notes.getIsPrintable());
        mvValues.add(notes.getIsDeleted());
        mvValues.add(notes.getDbmsUserNew());
        mvValues.add(notes.getUserNewTs());
        mvValues.add(notes.getDbmsUserEdit());
        mvValues.add(notes.getUserEditTs());
        mvValues.add(notes.getDbmsUserDelete());
        mvValues.add(notes.getUserDeleteTs());
    }
}

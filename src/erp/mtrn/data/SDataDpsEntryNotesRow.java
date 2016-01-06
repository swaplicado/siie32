/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Sergio Flores
 */
public class SDataDpsEntryNotesRow extends erp.lib.table.STableRow {

    public SDataDpsEntryNotesRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataDpsEntryNotes notes = (SDataDpsEntryNotes) moData;

        mvValues.clear();
        mvValues.add(notes.getNotes());
        mvValues.add(notes.getIsAllDocs());
        mvValues.add(notes.getIsPrintable());
        mvValues.add(notes.getIsCfd());
        mvValues.add(notes.getIsDeleted());
        mvValues.add(notes.getDbmsUserNew());
        mvValues.add(notes.getUserNewTs());
        mvValues.add(notes.getDbmsUserEdit());
        mvValues.add(notes.getUserEditTs());
        mvValues.add(notes.getDbmsUserDelete());
        mvValues.add(notes.getUserDeleteTs());
    }
}

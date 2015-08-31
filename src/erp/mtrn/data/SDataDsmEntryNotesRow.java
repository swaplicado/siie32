/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.mtrn.data.SDataDsmEntryNotes;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataDsmEntryNotesRow extends erp.lib.table.STableRow {

    public SDataDsmEntryNotesRow(java.lang.Object data){
        moData = data;
        
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataDsmEntryNotes notes = (SDataDsmEntryNotes) moData;

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

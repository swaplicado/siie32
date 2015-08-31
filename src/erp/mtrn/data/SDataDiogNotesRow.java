/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataDiogNotesRow extends erp.lib.table.STableRow {

    public SDataDiogNotesRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataDiogNotes diogNote = (SDataDiogNotes) moData;

        mvValues.clear();
        mvValues.add(diogNote.getNotes());
        mvValues.add(diogNote.getIsPrintable());
        mvValues.add(diogNote.getIsDeleted());
        mvValues.add(diogNote.getDbmsUserNew());
        mvValues.add(diogNote.getUserNewTs());
        mvValues.add(diogNote.getDbmsUserEdit());
        mvValues.add(diogNote.getUserEditTs());
        mvValues.add(diogNote.getDbmsUserDelete());
        mvValues.add(diogNote.getUserDeleteTs());
    }
}

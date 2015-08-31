/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataBomNotesRow extends erp.lib.table.STableRow {

    public SDataBomNotesRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataBomNotes data = (SDataBomNotes) moData;

        mvValues.clear();
        mvValues.add(data.getNotes());
        mvValues.add(data.getIsDeleted());
        mvValues.add(data.getDbmsUserNew());
        mvValues.add(data.getUserNewTs());
        mvValues.add(data.getDbmsUserEdit());
        mvValues.add(data.getUserEditTs());
        mvValues.add(data.getDbmsUserDelete());
        mvValues.add(data.getUserDeleteTs());
    }
}

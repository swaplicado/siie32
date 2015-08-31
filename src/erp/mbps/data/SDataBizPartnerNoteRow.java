/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataBizPartnerNoteRow extends erp.lib.table.STableRow {

    public SDataBizPartnerNoteRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataBizPartnerNote note = (SDataBizPartnerNote) moData;

        mvValues.clear();
        mvValues.add(note.getNotes());
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataBizPartnerBranchNoteRow extends erp.lib.table.STableRow {

    public SDataBizPartnerBranchNoteRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataBizPartnerBranchNote branchNote = (SDataBizPartnerBranchNote) moData;

        mvValues.clear();
        mvValues.add(branchNote.getNotes());
    }
}

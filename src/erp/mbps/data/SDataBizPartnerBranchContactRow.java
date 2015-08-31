/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataBizPartnerBranchContactRow extends erp.lib.table.STableRow {

    public SDataBizPartnerBranchContactRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataBizPartnerBranchContact contactRow = (SDataBizPartnerBranchContact) moData;

        mvValues.clear();
        mvValues.add(contactRow.getDbmsContactType());
        mvValues.add(contactRow.getCharge());
        mvValues.add(contactRow.getContact());
        mvValues.add(contactRow.getTelAreaCode01() + " " + contactRow.getTelNumber01() + " " + contactRow.getTelExt01());
        mvValues.add(contactRow.getDbmsTelephoneType01());
        mvValues.add(contactRow.getTelAreaCode02() + " " + contactRow.getTelNumber02() + " " + contactRow.getTelExt02());
        mvValues.add(contactRow.getDbmsTelephoneType02());
        mvValues.add(contactRow.getTelAreaCode03() + " " + contactRow.getTelNumber03() + " " + contactRow.getTelExt03());
        mvValues.add(contactRow.getDbmsTelephoneType03());
        mvValues.add(contactRow.getNextelId01());
        mvValues.add(contactRow.getNextelId02());
        mvValues.add(contactRow.getEmail01());
        mvValues.add(contactRow.getEmail02());
        mvValues.add(contactRow.getSkype01());
        mvValues.add(contactRow.getSkype02());
        mvValues.add(contactRow.getIsDeleted());
        mvValues.add(contactRow.getDbmsUserNew());
        mvValues.add(contactRow.getUserNewTs());
        mvValues.add(contactRow.getDbmsUserEdit());
        mvValues.add(contactRow.getUserEditTs());
        mvValues.add(contactRow.getDbmsUserDelete());
        mvValues.add(contactRow.getUserDeleteTs());
    }
}

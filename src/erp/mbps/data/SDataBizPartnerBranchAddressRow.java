/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDataBizPartnerBranchAddressRow extends erp.lib.table.STableRow {

    public SDataBizPartnerBranchAddressRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataBizPartnerBranchAddress bizPartnerBranchAddress = (SDataBizPartnerBranchAddress) moData;

        mvValues.clear();
        mvValues.add(bizPartnerBranchAddress.getAddress());
        mvValues.add(bizPartnerBranchAddress.getStreet());
        mvValues.add(bizPartnerBranchAddress.getStreetNumberExt());
        mvValues.add(bizPartnerBranchAddress.getStreetNumberInt());
        mvValues.add(bizPartnerBranchAddress.getNeighborhood());
        mvValues.add(bizPartnerBranchAddress.getReference());
        mvValues.add(bizPartnerBranchAddress.getZipCode());
        mvValues.add(bizPartnerBranchAddress.getPoBox());
        mvValues.add(bizPartnerBranchAddress.getIsDefault());
        mvValues.add(bizPartnerBranchAddress.getIsDeleted());
        mvValues.add(bizPartnerBranchAddress.getDbmsUserNew());
        mvValues.add(bizPartnerBranchAddress.getUserNewTs());
        mvValues.add(bizPartnerBranchAddress.getDbmsUserEdit());
        mvValues.add(bizPartnerBranchAddress.getUserEditTs());
        mvValues.add(bizPartnerBranchAddress.getDbmsUserDelete());
        mvValues.add(bizPartnerBranchAddress.getUserDeleteTs());
    }
}

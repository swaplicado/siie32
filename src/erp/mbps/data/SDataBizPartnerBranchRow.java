/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataBizPartnerBranchRow extends erp.lib.table.STableRow {

    public SDataBizPartnerBranchRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataBizPartnerBranch bizPartnerBranch = (SDataBizPartnerBranch) moData;

        mvValues.clear();
        mvValues.add(bizPartnerBranch.getBizPartnerBranch());
        mvValues.add(bizPartnerBranch.getDbmsBizPartnerBranchType());
        mvValues.add(bizPartnerBranch.getDbmsTaxRegion());
        mvValues.add(bizPartnerBranch.getIsDeleted());
        mvValues.add(bizPartnerBranch.getDbmsUserNew());
        mvValues.add(bizPartnerBranch.getUserNewTs());
        mvValues.add(bizPartnerBranch.getDbmsUserEdit());
        mvValues.add(bizPartnerBranch.getUserEditTs());
        mvValues.add(bizPartnerBranch.getDbmsUserDelete());
        mvValues.add(bizPartnerBranch.getUserDeleteTs());
    }
}

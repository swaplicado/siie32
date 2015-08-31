/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SDataAccountBizPartnerBpRow extends erp.lib.table.STableRow {

    public SDataAccountBizPartnerBpRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataAccountBizPartnerBp bizPartnerType = (SDataAccountBizPartnerBp) moData;

        mvValues.clear();
        mvValues.add(bizPartnerType.getDbmsBookkeepingCenter());
        mvValues.add(bizPartnerType.getDbmsBizPartnerCategory());
        mvValues.add(bizPartnerType.getDbmsBizPartner());
        mvValues.add(bizPartnerType.getPkDateStartId());
        mvValues.add(bizPartnerType.getIsDeleted());
    }
}

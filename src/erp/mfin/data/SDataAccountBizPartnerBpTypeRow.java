/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SDataAccountBizPartnerBpTypeRow extends erp.lib.table.STableRow {

    public SDataAccountBizPartnerBpTypeRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataAccountBizPartnerBpType bizPartnerType = (SDataAccountBizPartnerBpType) moData;

        mvValues.clear();
        mvValues.add(bizPartnerType.getDbmsBookkeepingCenter());
        mvValues.add(bizPartnerType.getDbmsBizPartnerCategory());
        mvValues.add(bizPartnerType.getDbmsBizPartnerType());
        mvValues.add(bizPartnerType.getPkDateStartId());
        mvValues.add(bizPartnerType.getIsDeleted());
    }
}

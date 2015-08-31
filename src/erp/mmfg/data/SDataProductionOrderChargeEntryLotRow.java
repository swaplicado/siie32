/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import erp.data.SDataConstantsSys;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataProductionOrderChargeEntryLotRow extends erp.lib.table.STableRow {

    private int mnFkSortingItemTypeId;

    public SDataProductionOrderChargeEntryLotRow(java.lang.Object data, int nFkSortingItemTypeId) {
        moData = data;
        mnFkSortingItemTypeId = nFkSortingItemTypeId;

        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataProductionOrderChargeEntryLot data = (SDataProductionOrderChargeEntryLot) moData;

        mvValues.clear();

        if (mnFkSortingItemTypeId == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            mvValues.add(data.getDbmsItemKey());
            mvValues.add(data.getDbmsItem());
        }
        else {
            mvValues.add(data.getDbmsItem());
            mvValues.add(data.getDbmsItemKey());
        }

        mvValues.add(data.getDbmsUnit());
        mvValues.add(data.getQuantity());
        mvValues.add(data.getDbmsLot());
        mvValues.add(data.getIsDeleted());
        mvValues.add(data.getDbmsUserNew());
        mvValues.add(data.getUserNewTs());
        mvValues.add(data.getDbmsUserEdit());
        mvValues.add(data.getUserEditTs());
        mvValues.add(data.getDbmsUserDelete());
        mvValues.add(data.getUserDeleteTs());
    }
}

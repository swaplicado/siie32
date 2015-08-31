/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.data;

import erp.data.SDataConstantsSys;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataPriceListItemPriceRow extends erp.lib.table.STableRow {

    protected int mnFkSortingItemTypeId;

    public SDataPriceListItemPriceRow(java.lang.Object data, int nFkSortingItemTypeId) {
        moData = data;
        mnFkSortingItemTypeId = nFkSortingItemTypeId;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataPriceListItemPrice oData = (SDataPriceListItemPrice) moData;

        mvValues.clear();

        if (SDataConstantsSys.CFGS_TP_SORT_KEY_NAME == mnFkSortingItemTypeId) {
            mvValues.add(oData.getDbmsItemKey());
            mvValues.add(oData.getDbmsItem());
        }
        else {
            mvValues.add(oData.getDbmsItem());
            mvValues.add(oData.getDbmsItemKey());
        }
        mvValues.add(oData.getPrice());
    }
}

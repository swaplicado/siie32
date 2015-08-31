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

public class SDataProductionOrderChargeEntryRow extends erp.lib.table.STableRow {

    private int mnItemSort;

    public SDataProductionOrderChargeEntryRow(java.lang.Object data, int nItemSort) {
        moData = data;

        mnItemSort = nItemSort;

        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataProductionOrderChargeEntry data = (SDataProductionOrderChargeEntry) moData;

        mvValues.clear();

        if (mnItemSort == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            mvValues.add(data.getDbmsLevelItemKey());
            mvValues.add(data.getDbmsLevelItem());
        }
        else {
            mvValues.add(data.getDbmsLevelItem());
            mvValues.add(data.getDbmsLevelItemKey());
        }
        
        mvValues.add(data.getGrossRequirement_r());
        mvValues.add(data.getDbmsLevelUnitSymbol());
        mvValues.add(data.getDbmsLots());
        mvValues.add(data.getDbmsItemSubstitute());
        mvValues.add(data.getIsDeleted());
        mvValues.add(data.getDbmsUserNew());
        mvValues.add(data.getUserNewTs());
        mvValues.add(data.getDbmsUserEdit());
        mvValues.add(data.getUserEditTs());
        mvValues.add(data.getDbmsUserDelete());
        mvValues.add(data.getUserDeleteTs());
    }
}

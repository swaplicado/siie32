/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataProductionOrderChargeRow extends erp.lib.table.STableRow {

    public SDataProductionOrderChargeRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataProductionOrderCharge data = (SDataProductionOrderCharge) moData;

        mvValues.clear();
        
        mvValues.add(SLibUtilities.parseInt(data.getNumber()));
        mvValues.add(data.getIsDeleted());
        mvValues.add(data.getDbmsUserNew());
        mvValues.add(data.getUserNewTs());
        mvValues.add(data.getDbmsUserEdit());
        mvValues.add(data.getUserEditTs());
        mvValues.add(data.getDbmsUserDelete());
        mvValues.add(data.getUserDeleteTs());
    }
}

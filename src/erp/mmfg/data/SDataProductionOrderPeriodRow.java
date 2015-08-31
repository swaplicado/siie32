/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataProductionOrderPeriodRow extends erp.lib.table.STableRow {

    public SDataProductionOrderPeriodRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataProductionOrderPeriod data = (SDataProductionOrderPeriod) moData;

        mvValues.clear();
        mvValues.add(data.getDateStart());
        mvValues.add(data.getDateEnd());
        mvValues.add(data.getQty());
        mvValues.add(data.getDbmsUnitSymbol());
    }
}

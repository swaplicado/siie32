/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataBomSubgoodsRow extends erp.lib.table.STableRow {

    public SDataBomSubgoodsRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataBomSubgoods data = (SDataBomSubgoods) moData;

        mvValues.clear();
        mvValues.add(data.getDbmsItemKey());
        mvValues.add(data.getDbmsItem());
        mvValues.add(data.getQuantity());
        mvValues.add(data.getDbmsUnitSymbol());
        mvValues.add(data.getCostPercentage());
        mvValues.add(data.getDateStart());
        mvValues.add(data.getDateEnd_n());
        mvValues.add(data.getIsDeleted());
        mvValues.add(data.getDbmsUserNew());
        mvValues.add(data.getUserNewTs());
        mvValues.add(data.getDbmsUserEdit());
        mvValues.add(data.getUserEditTs());
        mvValues.add(data.getDbmsUserDelete());
        mvValues.add(data.getUserDeleteTs());
    }
}

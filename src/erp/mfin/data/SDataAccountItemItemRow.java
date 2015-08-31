/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SDataAccountItemItemRow extends erp.lib.table.STableRow {

    public SDataAccountItemItemRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataAccountItemItem item = (SDataAccountItemItem) moData;

        mvValues.clear();
        mvValues.add(item.getDbmsBookkeepingCenter());
        mvValues.add(item.getDbmsLinkType());
        mvValues.add(item.getDbmsReference());
        mvValues.add(item.getPkDateStartId());
        mvValues.add(item.getIsDeleted());
    }
}

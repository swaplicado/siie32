/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SDataAccountItemEntryRow extends erp.lib.table.STableRow {

    public SDataAccountItemEntryRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataAccountItemEntry entry = (SDataAccountItemEntry) moData;

        mvValues.clear();
        mvValues.add(entry.getDbmsAccountItemType());
        mvValues.add(entry.getFkAccountId_n());
        mvValues.add(entry.getDbmsAccount_n());
        mvValues.add(entry.getFkCostCenterId_n());
        mvValues.add(entry.getDbmsCostCenter_n());
        mvValues.add(entry.getDbmsBookkeepingRegistryType());
        mvValues.add(entry.getPercentage());
    }
}

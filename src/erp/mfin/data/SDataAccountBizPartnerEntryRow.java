/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SDataAccountBizPartnerEntryRow extends erp.lib.table.STableRow {

    public SDataAccountBizPartnerEntryRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataAccountBizPartnerEntry entry = (SDataAccountBizPartnerEntry) moData;

        mvValues.clear();
        mvValues.add(entry.getDbmsAccountBizPartnerType());
        mvValues.add(entry.getFkAccountId());
        mvValues.add(entry.getDbmsAccount());
        mvValues.add(entry.getFkCostCenterId_n());
        mvValues.add(entry.getDbmsCostCenter_n());
        mvValues.add(entry.getDbmsBookkeepingRegistryType());
        mvValues.add(entry.getPercentage());
    }
}

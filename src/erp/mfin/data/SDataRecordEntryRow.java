/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SDataRecordEntryRow extends erp.lib.table.STableRow {

    public SDataRecordEntryRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataRecordEntry entry = (SDataRecordEntry) moData;

        mvValues.clear();
        mvValues.add(entry.getSortingPosition());
        mvValues.add(entry.getFkAccountIdXXX());
        mvValues.add(entry.getDbmsAccount() + (entry.getDbmsAccountComplement().isEmpty() ? "" : "; " + entry.getDbmsAccountComplement()));
        mvValues.add(entry.getConcept());
        mvValues.add(entry.getDebit());
        mvValues.add(entry.getCredit());
        mvValues.add(entry.getExchangeRate());
        mvValues.add(entry.getDebitCy());
        mvValues.add(entry.getCreditCy());
        mvValues.add(entry.getDbmsCurrencyKey());
        mvValues.add(entry.getIsSystem());
        mvValues.add(entry.getIsExchangeDifference());
        mvValues.add(entry.getDbmsAccountingMoveSubclass());
        mvValues.add(entry.getFkCostCenterIdXXX_n());
        mvValues.add(entry.getDbmsCostCenter_n());
        mvValues.add(entry.getAuxCheckNumber() == 0 ? "" : "" + entry.getAuxCheckNumber());
        mvValues.add(entry.getIsDeleted());
        mvValues.add(entry.getDbmsUserNew());
        mvValues.add(entry.getUserNewTs());
        mvValues.add(entry.getDbmsUserEdit());
        mvValues.add(entry.getUserEditTs());
        mvValues.add(entry.getDbmsUserDelete());
        mvValues.add(entry.getUserDeleteTs());
        mvValues.add(entry.getUserId());
    }
}

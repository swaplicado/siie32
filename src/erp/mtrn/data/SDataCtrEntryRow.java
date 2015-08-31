/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataCtrEntryRow extends erp.lib.table.STableRow {

    public SDataCtrEntryRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataCtrEntry entry = (SDataCtrEntry) moData;

        mvValues.clear();
        mvValues.add(entry.getDate());
        mvValues.add(entry.getDbmsDpsType());
        mvValues.add(entry.getNumberSeries() + (entry.getNumberSeries().length() == 0 ? "" : "-") + entry.getNumber());
        mvValues.add(entry.getDbmsCurrencyKey());
        mvValues.add(entry.getTotalCy());
        mvValues.add(entry.getExchangeRate());
        mvValues.add(entry.getTotal_r());
        mvValues.add(entry.getNumberSeriesDps() + (entry.getNumberSeriesDps().length() == 0 ? "" : "-") + entry.getNumberDps());
        mvValues.add(entry.getIsDeleted());
        mvValues.add(entry.getDbmsUserNew());
        mvValues.add(entry.getUserNewTs());
        mvValues.add(entry.getDbmsUserEdit());
        mvValues.add(entry.getUserEditTs());
        mvValues.add(entry.getDbmsUserDelete());
        mvValues.add(entry.getUserDeleteTs());
    }
}

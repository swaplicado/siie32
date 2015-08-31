/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Sergio Flores
 */
public class SDataYearPeriodRow extends erp.lib.table.STableRow {

    public SDataYearPeriodRow(java.lang.Object data) {
        moData = data;
    }

    @Override
    public void prepareTableRow() {
        SDataYearPeriod period = (SDataYearPeriod) moData;

        mvValues.clear();
        mvValues.add(period.getPkPeriodId());
        mvValues.add(period.getExtraPeriodText());
        mvValues.add(period.getIsClosed());
        mvValues.add(period.getUserEditTs());
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Sergio Flores
 */
public class SDataDpsEntryCommissionsRow extends erp.lib.table.STableRow {

    public SDataDpsEntryCommissionsRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataDpsEntryCommissions commissions = (SDataDpsEntryCommissions) moData;

        mvValues.clear();
        mvValues.add(commissions.getPercentage());
        mvValues.add(commissions.getValueUnitary());
        mvValues.add(commissions.getValue());
        mvValues.add(commissions.getCommissions());
        mvValues.add(commissions.getCommissionsCy());
        mvValues.add(commissions.getDbmsCommissionsType());
    }
}

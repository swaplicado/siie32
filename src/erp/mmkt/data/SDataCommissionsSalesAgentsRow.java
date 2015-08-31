/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.data;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataCommissionsSalesAgentsRow extends erp.lib.table.STableRow {

    public SDataCommissionsSalesAgentsRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataCommissionsSalesAgent data = (SDataCommissionsSalesAgent) moData;

        mvValues.clear();
        mvValues.add(data.getDbmsSalesAgent());
        mvValues.add(data.getAgentPercentage() * 100);
        mvValues.add(data.getSupervisorPercentage() * 100);
    }
}

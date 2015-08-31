/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.data;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataCommissionsSalesAgentTypesRow extends erp.lib.table.STableRow {

    public SDataCommissionsSalesAgentTypesRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataCommissionsSalesAgentType data = (SDataCommissionsSalesAgentType) moData;

        mvValues.clear();
        mvValues.add(data.getDbmsSalesAgentType());
        mvValues.add(data.getAgentPercentage() * 100);
        mvValues.add(data.getSupervisorPercentage() * 100);
    }
}

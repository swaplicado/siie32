/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.data;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataCustomerBranchConfigRow extends erp.lib.table.STableRow {

    public SDataCustomerBranchConfigRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataCustomerBranchConfig data = (SDataCustomerBranchConfig) moData;

        mvValues.clear();
        mvValues.add(data.getDbmsCustomerBranch());
        mvValues.add(data.getDbmsSalesRoute());
        mvValues.add(data.getDbmsSalesAgent());        
        mvValues.add(data.getDbmsSalesSupervisor());        
        mvValues.add(data.getIsDeleted());
        mvValues.add(data.getDbmsUserNew());
        mvValues.add(data.getUserNewTs());
        mvValues.add(data.getDbmsUserEdit());
        mvValues.add(data.getUserEditTs());
        mvValues.add(data.getDbmsUserDelete());
        mvValues.add(data.getUserDeleteTs());
    }
}

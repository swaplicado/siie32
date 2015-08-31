/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataUserRoleRow extends erp.lib.table.STableRow {

    public SDataUserRoleRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataUserRoleUser role = (SDataUserRoleUser) moData;

        mvValues.clear();
        mvValues.add(role.getDbmsTypeRole());
        mvValues.add(role.getDbmsRole());
        mvValues.add(role.getDbmsLevelType());
    }
}

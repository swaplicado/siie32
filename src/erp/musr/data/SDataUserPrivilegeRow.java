/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataUserPrivilegeRow extends erp.lib.table.STableRow {

    public SDataUserPrivilegeRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataUserPrivilegeUser privilege = (SDataUserPrivilegeUser) moData;

        mvValues.clear();
        mvValues.add(privilege.getDbmsPrivilege());
        mvValues.add(privilege.getDbmsLevelType());
    }
}

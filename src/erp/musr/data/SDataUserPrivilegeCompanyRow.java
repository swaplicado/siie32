/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataUserPrivilegeCompanyRow extends erp.lib.table.STableRow {

    public SDataUserPrivilegeCompanyRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataUserPrivilegeCompany privilege = (SDataUserPrivilegeCompany) moData;

        mvValues.clear();
        mvValues.add(privilege.getDbmsCompany());
        mvValues.add(privilege.getDbmsPrivilege());
        mvValues.add(privilege.getDbmsLevelType());
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataUserRoleCompanyRow extends erp.lib.table.STableRow {

    public SDataUserRoleCompanyRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataUserRoleCompany role = (SDataUserRoleCompany) moData;

        mvValues.clear();
        mvValues.add(role.getDbmsCompany());
        mvValues.add(role.getDbmsTypeRole());
        mvValues.add(role.getDbmsRole());
        mvValues.add(role.getDbmsLevelType());
    }
}

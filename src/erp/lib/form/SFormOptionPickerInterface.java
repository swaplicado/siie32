/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.form;

/**
 *
 * @author Sergio Flores
 */
public interface SFormOptionPickerInterface {

    public void formReset();
    public void formRefreshOptionPane();
    public void setFormVisible(boolean visible);
    public int getOptionType();
    public int getFormResult();
    public void setFilterKey(java.lang.Object filterKey);
    public void setSelectedPrimaryKey(java.lang.Object pk);
    public java.lang.Object getSelectedPrimaryKey();
    public erp.lib.table.STableRow getSelectedOption();
}

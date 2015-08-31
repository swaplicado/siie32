/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

/**
 *
 * @author Sergio Flores
 */
public interface STableTabInterface {

    public int getTabType();
    public int getTabTypeAux01();
    public int getTabTypeAux02();
    public java.util.Vector<java.lang.Integer> getSuscriptors();
    public java.util.Vector<erp.lib.table.STableSetting> getTableSettings();
    public void addSetting(erp.lib.table.STableSetting setting);
    public void updateSetting(erp.lib.table.STableSetting setting);
    public void actionRefresh(int mode);
}
